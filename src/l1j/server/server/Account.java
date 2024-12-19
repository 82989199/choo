package l1j.server.server;

import static l1j.server.server.model.skill.L1SkillId.EXP_BUFF;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Base64;
import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class Account {
	/** 계정오브젝트아이디 **/
	private int _id;
	/** 계정명 */
	private String _name;
	/** 접속자 IP주소 */
	private String _ip;
	/** 패스워드(암호화 됨) */
	private String _password;
	/** 최근 접속일 */
	private Timestamp _lastActive;
	/** 최근 접속종료일 */
	private Timestamp _lastlogout;
	/** 엑세스 등급(GM인가?) */
	private int _accessLevel;
	/** 접속자 호스트명 */
	private String _host;
	/** 밴 유무(True == 금지) */
	private int _banned;
	/** 계정 유효 유무(True == 유효) */
	private boolean _isValid = false;
	/** 캐릭터 슬롯(태고의옥쇄) */
	private int _charslot;
	private boolean _is_changed_slot = false;
	/** 창고 비밀번호 */
	private int _GamePassword;
	/** 케릭비번 */
	private String _CharPassword;
	private boolean _iscpwok = Config.CHAR_PASSWORD;// 2차비번 주석처리 (초기상태 false 로
													// 해놔야 정상 작동)
	private byte[] _waitpacket = null;

	public int Ncoin_point; // N코인 마일리지
	public int Shop_open_count;

	/** Buff_PC방 */
	public Timestamp _Buff_PC방;
	public int tam_point;
	public Timestamp _lastQuit;

	private int _tam = 0;
	private int _tamStep = 0;

	private int _blessOfAin;
	private int _grangKinAngerTime;

	/** 메세지 로그용 */
	private static Logger _log = Logger.getLogger(Account.class.getName());

	public Account() {
	}

	/**
	 * 패스워드를 암호화한다.
	 *
	 * @param rawPassword
	 *            패스워드
	 * @return String
	 * @throws NoSuchAlgorithmException
	 *             암호화 알고리즘을 사용할 수 없을 때
	 * @throws UnsupportedEncodingException
	 *             인코딩이 지원되지 않을 때
	 */
	@SuppressWarnings("unused")
	private static String encodePassword(final String rawPassword)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] buf = rawPassword.getBytes("UTF-8");
		buf = MessageDigest.getInstance("SHA").digest(buf);
		return Base64.encodeBytes(buf);
	}

	// 영구추방 아이피 체크
	public static String checkIP(String name) {
		String n = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM accounts WHERE login=? ");
			pstm.setString(1, name);
			rs = pstm.executeQuery();

			if (rs.next())
				n = rs.getString("ip");

		} catch (Exception e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return n;
	}

	/**
	 * 신규 계정 생성
	 *
	 * @param name
	 *            계정명
	 * @param rawPassword
	 *            패스워드
	 * @param ip
	 *            접속자 IP주소
	 * @param host
	 *            접속자 호스트명
	 * @return Account
	 */
	public static Account create(final String name, final String rawPassword, final String ip, final String host) {
		Calendar cal = Calendar.getInstance();
		int 시간 = Calendar.HOUR;
		int 분 = Calendar.MINUTE;
		/** 0 오전 , 1 오후 * */
		String 오전오후 = "오후";
		if (cal.get(Calendar.AM_PM) == 0) {
			오전오후 = "오전";
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			Account account = new Account();
			account._name = name;
			account._password = rawPassword;
			account._ip = ip;
			account._host = host;
			account._banned = 0;
			account._lastActive = new Timestamp(System.currentTimeMillis());
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "INSERT INTO accounts SET login=?,password=?,lastactive=?,access_level=?,ip=?,host=?,banned=?,charslot=?, gamepassword=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, account._name);
			pstm.setString(2, account._password);
			pstm.setTimestamp(3, account._lastActive);
			pstm.setInt(4, 0);
			pstm.setString(5, account._ip);
			pstm.setString(6, account._host);
			pstm.setInt(7, account._banned);
			pstm.setInt(8, Config.Characters_CharSlot);
			pstm.setInt(9, 0);
			pstm.execute();
			System.out.println("계정생성:【" + name + "】 / IP:【" + ip + "】 / 시간:【" + 오전오후 + " " + cal.get(시간) + "시"
					+ cal.get(분) + "】분");
			return account;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return null;
	}

	/**
	 * DB에서 계정 정보 불러오기
	 *
	 * @param name
	 *            계정명
	 * @return Account
	 */
	public static Account load(final String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		Account account = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "SELECT * FROM accounts WHERE login=? LIMIT 1";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;
			}
			account = new Account();
			account._id = rs.getInt("id");
			account._name = rs.getString("login");
			account._password = rs.getString("password");
			account._lastActive = rs.getTimestamp("lastactive");
			account._accessLevel = rs.getInt("access_level");
			account._host = rs.getString("host");
			account._banned = rs.getInt("banned");
			account._charslot = rs.getInt("charslot");
			account._GamePassword = (rs.getInt("gamepassword"));
			account._phone = rs.getString("phone");
			account.tam_point = rs.getInt("Tam_Point");
			account._Buff_PC방 = (rs.getTimestamp("BUFF_PCROOM_Time"));

			account._CharPassword = (rs.getString("CharPassword"));

			account.Ncoin_point = (rs.getInt("Ncoin_Point"));
			account.Shop_open_count = (rs.getInt("Shop_open_count"));
			account.setDragonRaid(rs.getTimestamp("raid_buff"));

			account._blessOfAin = rs.getInt("bless_of_ain");
			account._grangKinAngerTime = rs.getInt("GrangKinAngerTime");
			account._lastlogout = rs.getTimestamp("last_log_out");
			_log.fine("account exists");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return account;
	}

	/**
	 * DB에 최근 접속일 업데이트
	 *
	 * @param account
	 *            계정명
	 */
	public static void updateLastActive(final Account account, String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET lastactive=?, ip=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setTimestamp(1, ts);
			pstm.setString(2, ip);
			pstm.setString(3, account.getName());
			pstm.execute();
			account._lastActive = ts;
			_log.fine("update lastactive for " + account.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 해당 계정의 캐릭터수를 셈
	 *
	 * @return result 캐릭터수
	 */
	public int countCharacters() {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "SELECT count(*) as cnt FROM characters WHERE account_name=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, _name);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public void updateNcoin() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Ncoin_Point=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, Ncoin_point);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void resetShopOpenCount() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Shop_open_count=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, 0);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void updateShopOpenCount() {
		Shop_open_count++;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Shop_open_count=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, Shop_open_count);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void ban(final String account, int reason) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET banned=? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, reason);
			pstm.setString(2, account);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 입력된 비밀번호와 DB에 저장된 패스워드를 비교
	 *
	 * @param rawPassword
	 *            패스워드
	 * @return boolean
	 */
	public boolean validatePassword(String accountName, final String rawPassword) {
		try {
			_isValid = (_password.equals(/* encodePassword( */rawPassword)
					/* ) */ || checkPassword(accountName, _password, rawPassword));
			/*if (_isValid) {
				_password = null; // 인증이 성공했을 경우, 패스워드를 파기한다.
			}*/
			return _isValid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void updatePhone(final Account account) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET phone=? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, account.getphone());
			pstm.setString(2, account.getName());
			pstm.execute();
			account._phone = account.getphone();
			_log.fine("update phone for " + account.getName());
		} catch (Exception e) {
			_log.log(Level.SEVERE, "accounts updatePhone 에러발생", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 유효한 계정인가
	 *
	 * @return boolean
	 */
	public boolean isValid() {
		return _isValid;
	}
	
	public void setValid(boolean is_valid){
		_isValid = is_valid;
	}

	/**
	 * GM 계정인가
	 *
	 * @return boolean
	 */
	public boolean isGameMaster() {
		return 0 < _accessLevel;
	}

	public String getName() {
		return _name;
	}
	
	public void setName(String name){
		_name = name;
	}

	public String get_Password() {
		return _password;
	}

	public void setPassword(String password){
		_password = password;
	}
	
	public Timestamp getBuff_PC방() {
		return _Buff_PC방;
	}

	public void setBuff_PC방(Timestamp ts) {
		_Buff_PC방 = ts;
	}

	// public void set_Password(String password){
	// this._password = password;
	// }

	public String getCPW() {
		return _CharPassword;
	}

	public void setCPW(String s) {
		_CharPassword = s;
	}

	public void setcpwok(boolean f) {
		_iscpwok = f;
	}

	public boolean iscpwok() {
		return _iscpwok;
	}

	public byte[] getwaitpacket() {
		return _waitpacket;
	}

	public void setwaitpacket(byte[] s) {
		_waitpacket = s;
	}

	public void setIp(String ip) {
		_ip = ip;
	}

	public String getIp() {
		return _ip;
	}

	public Timestamp getLastActive() {
		return _lastActive;
	}
	
	public void setLastActive(Timestamp ts){
		_lastActive = ts;
	}

	private long _btnTimeHome;

	public long getButtonTimeHome() {
		return _btnTimeHome;
	}

	public void setButtonTimeHome(long i) {
		_btnTimeHome = i;
	}

	private long _btnTimePc;

	public long getButtonTimePc() {
		return _btnTimePc;
	}

	public void setButtonTimePc(long i) {
		_btnTimePc = i;
	}

	/**
	 * 최종 로그인일을 취득한다.
	 */

	public int getAccessLevel() {
		return _accessLevel;
	}
	public void setAccessLevel(int accessLevel){
		_accessLevel = accessLevel;
	}

	public String getHost() {
		return _host;
	}
	
	public void setHost(String host){
		_host = host;
	}

	public int getBannedCode() {
		return _banned;
	}
	
	public void setBannedCode(int banned){
		_banned = banned;
	}

	public int getCharSlot() {
		return _charslot;
	}

	public void setCharSlot(int charSlot){
		_charslot = charSlot;
	}
	
	public void is_changed_slot(boolean is_changed){
		_is_changed_slot = is_changed;
	}
	public boolean is_changed_slot(){
		return _is_changed_slot;
	}
	
	/**
	 * 연락처를 취득한다.
	 * 
	 * @return String
	 */
	private String _phone;

	public String getphone() {
		return _phone;
	}

	public void setphone(String s) {
		_phone = s;
	}

	/**
	 * 웹패스워드관련임;
	 * 
	 * @param account
	 *            계정명
	 */
	public void UpdateCharPassword(String pwd) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET CharPassword=? WHERE login=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, pwd);
			pstm.setString(2, getName());
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 캐릭터 슬롯수 설정
	 *
	 * @return boolean
	 */
	public void setCharSlot(GameClient client, int i) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET charslot=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, i);
			pstm.setString(2, client.getAccount().getName());
			pstm.execute();
			client.getAccount()._charslot = i;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static boolean checkLoginIP(String ip) {
		int num = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");

			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next())
				num = rs.getInt("cnt");

			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

			// 동일 IP로 생성된 계정이 3개 미만인 경우
			if (num < MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT)// 계정생성외부화
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	// 웹 연동을 위한 메소드 추가 - By Sini
	public static boolean checkPassword(String accountName, String _pwd, String rawPassword) {
		String _inputPwd = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT password(?) as pwd ");

			pstm.setString(1, rawPassword);
			rs = pstm.executeQuery();
			if (rs.next()) {
				_inputPwd = rs.getString("pwd");
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
			if (_pwd.equals(_inputPwd)) { // 동일하다면
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	public static boolean checkLoginBanIP(String ip) {
		int num = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM ban_ip WHERE ip=?");

			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next()) {
				num = rs.getInt("cnt");
			}

			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

			// Ban IP가 1개 이상인경우
			if (num >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	/**
	 * update피씨방
	 */
	public void update피씨방() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET BUFF_PCROOM_Time=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setTimestamp(1, _Buff_PC방);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 창고 비번
	 *
	 * @return boolean
	 */
	public static void setGamePassword(GameClient client, int pass) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET gamepassword=? WHERE login =?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, pass);
			pstm.setString(2, client.getAccount().getName());
			pstm.execute();
			client.getAccount()._GamePassword = pass;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void 탐포인트업데이트(final Account account) {
		Timestamp 계정종료날짜 = _lastQuit;
		Timestamp 현재날짜 = new Timestamp(System.currentTimeMillis());
		
		long 계정마지막종료시간 = 0;
		long 현재날짜시간 = 현재날짜.getTime();
		long 시간차 = 0;
		if (계정종료날짜 != null) {
			계정마지막종료시간 = 계정종료날짜.getTime();
		} else {
			return;
		}
		시간차 = 현재날짜시간 - 계정마지막종료시간;
		int 탐추가횟수 = (int) (시간차 / (60000 * 12));
		if (탐추가횟수 < 1) {
			return;
		}
		탐수치적용(account, 계정마지막종료시간, 탐추가횟수);
	}

	public void 탐수치적용(final Account account, long 종료날짜, int 탐추가횟수) {
		Connection con = null;
		Connection con2 = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		Timestamp tamtime = null;
		long sysTime = System.currentTimeMillis();
		int tamcount = Config.탐갯수;

		int char_objid = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `characters` WHERE account_name = ?"); // 케릭터
																								// 테이블에서군주만골라와서
			pstm.setString(1, account.getName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				tamtime = rs.getTimestamp("TamEndTime");
				char_objid = rs.getInt("objid");
				if (tamtime != null) {
					if (sysTime <= tamtime.getTime()) {
						// 현재까지도 적용되어지고있는 경우.
						int 추가횟수 = 탐추가횟수;
						tam_point += 추가횟수 * tamcount;
						updateTam();
					} else {
						// if(Tam_wait_count(char_objid)!=0){
						int day = Nexttam(char_objid);
						if (day != 0) {
							Timestamp deleteTime = null;
							deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7일
							con2 = L1DatabaseFactory.getInstance().getConnection();
							pstm2 = con2.prepareStatement(
									"UPDATE `characters` SET TamEndTime=? WHERE account_name = ? AND objid = ?"); // 케릭터테이블에서
																													// 군주만골라와서
							pstm2.setTimestamp(1, deleteTime);
							pstm2.setString(2, account.getName());
							pstm2.setInt(3, char_objid);
							pstm2.executeUpdate();
							tamdel(char_objid);
							tamtime = deleteTime;
						}
						// }
						if (종료날짜 <= tamtime.getTime()) {
							// 현재는 아니지만 종료이후 적용되어지는 경우.
							int 추가횟수 = (int) ((tamtime.getTime() - 종료날짜) / (60000 * 12));
							tam_point += 추가횟수 * tamcount;
							updateTam();
						} else {
							// System.out.println("종료날짜 이전에 탐시간도 종료됨.");
						}

						/**/
					}
				} else {
					// System.out.println("탐타임 없음");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm2);
			SQLUtil.close(con2);
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void updateTam() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET Tam_Point=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, tam_point);
			pstm.setString(2, _name);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int Nexttam(int objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int day = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT day FROM `tam` WHERE objid = ? order by id asc limit 1");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				day = rs.getInt("Day");
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return day;
	}

	public void tamdel(int objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from Tam where objid = ? order by id asc limit 1");
			pstm.setInt(1, objectId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getTam() {
		return _tam;
	}

	public int getTamStep() {
		return _tamStep;
	}

	public void updateTamStep(String AccountName, int step) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET tamStep=? WHERE login=?");
			pstm.setInt(1, step);
			pstm.setString(2, AccountName);
			pstm.execute();
			_tamStep = step;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 최종 로그인일을 DB에 반영한다.
	 *
	 * @param account
	 *            어카운트
	 */

	// 탐 계정 정보에 저장하기 탐상점에서 갯수 로딩
	public int getTamPoint() {
		return tam_point;
	}

	public int setTamPoint(int tampoint) {
		return tam_point = tampoint;
	}

	public int addTamPoint(int tampoint) {
		return tam_point += tampoint;
	}

	public int getGamePassword() {
		return _GamePassword;
	}

	public void setGamePassword(int gamePassword){
		_GamePassword = gamePassword;
	}
	private Timestamp _raidBuff;

	public Timestamp getDragonRaid() {
		return _raidBuff;
	}

	public void setDragonRaid(Timestamp ts) {
		_raidBuff = ts;
	}

	public int getBlessOfAin() {
		return this._blessOfAin;
	}

	public void setBlessOfAin(int i) {
		this._blessOfAin = i;
	}

	public void addBlessOfAin(int i, L1PcInstance pc) {
		this._blessOfAin = IntRange.ensure(_blessOfAin + i, 0, 50000000);

		if(_blessOfAin > 10000) {
			SC_REST_EXP_INFO_NOTI.send(pc);
		}
		
		if (pc.hasSkillEffect(EXP_BUFF)) {
			int skillTime = pc.getSkillEffectTimeSec(EXP_BUFF);
			if (_blessOfAin < 10000) {
				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(EXP_BUFF + 1, 5087, true)); 
			} else {
				pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.EXP_BUFF + 1));
				pc.sendPackets(S_InventoryIcon.icoNew(EXP_BUFF, 5087, skillTime, true));
			}
		}
	}

	public void updateBlessOfAin() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET bless_of_ain=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, _blessOfAin);
			pstm.setString(2, getName());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getGrangKinAngerTime() {
		return _grangKinAngerTime;
	}

	public void addGrangKinAngerTime(int i, L1PcInstance pc) {
		int old_grangKinAngerStep = getGrangKinAngerStat();
		System.out.println(pc.getName() + " / 이전 그랑카인 시간 : " + _grangKinAngerTime + " / 이전 그랑카인스텝 : " + old_grangKinAngerStep);
		int time = _grangKinAngerTime + i;
		// 그랑카인 6단계 시간이 최대 10시간 이므로 1시간(60) * 10
		_grangKinAngerTime = IntRange.ensure(time, 0, GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_TIME);

		int new_grangKinAngerStep = getGrangKinAngerStat();

		System.out.println(pc.getName() + " / 현재 그랑카인 시간 : " + _grangKinAngerTime + " / 그랑카인스텝 : " + new_grangKinAngerStep);

		if (old_grangKinAngerStep != new_grangKinAngerStep) {
			if (new_grangKinAngerStep == 0) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 555 + old_grangKinAngerStep, false));
				SC_REST_EXP_INFO_NOTI.send(pc);
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 555 + new_grangKinAngerStep, true));
			}
		}
	}

	public void updateGrangeKinAngerTime() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET GrangKinAngerTime=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, _grangKinAngerTime);
			pstm.setString(2, getName());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void setGrangKinAngerTime(int i){
		_grangKinAngerTime = i;
	}
	
	public void setGrangKinAngerTime(int i, L1PcInstance pc) {
		int old_grangKinAngerStep = getGrangKinAngerStat();

		_grangKinAngerTime = IntRange.ensure(i, 0, GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_TIME);

		int new_grangKinAngerStep = getGrangKinAngerStat();
		if (old_grangKinAngerStep != new_grangKinAngerStep) {
			if (new_grangKinAngerStep == 0) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 555 + old_grangKinAngerStep, false));
				SC_REST_EXP_INFO_NOTI.send(pc);
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 555 + new_grangKinAngerStep, true));
			}
		}
		
		
	}

	public int getGrangKinAngerStat() {
		if (_grangKinAngerTime <= 0)
			return 0;

		if (!GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE)
			return 0;

		int grangKinOneStep = GrangKinConfig.GRANG_KIN_ANGER_ONE_STEP_TIME;
		int grangKinTwoStep = GrangKinConfig.GRANG_KIN_ANGER_TWO_STEP_TIME;
		int grangKinThreeStep = GrangKinConfig.GRANG_KIN_ANGER_THREE_STEP_TIME;
		int grangKinFourStep = GrangKinConfig.GRANG_KIN_ANGER_FOUR_STEP_TIME;
		int grangKinFiveStep = GrangKinConfig.GRANG_KIN_ANGER_FIVE_STEP_TIME;
		int grangKinSixStep = GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_TIME;

		if (_grangKinAngerTime >= grangKinOneStep && _grangKinAngerTime < grangKinTwoStep)
			return 1;
		if (_grangKinAngerTime >= grangKinTwoStep && _grangKinAngerTime < grangKinThreeStep)
			return 2;
		if (_grangKinAngerTime >= grangKinThreeStep && _grangKinAngerTime < grangKinFourStep)
			return 3;
		if (_grangKinAngerTime >= grangKinFourStep && _grangKinAngerTime < grangKinFiveStep)
			return 4;
		if (_grangKinAngerTime >= grangKinFiveStep && _grangKinAngerTime < grangKinSixStep)
			return 5;
		if (_grangKinAngerTime >= grangKinSixStep)
			return 6;

		return 0;
	}

	public Timestamp getLastLogOut() {
		return _lastlogout;
	}

	public void setLastLogOut(Timestamp ts) {
		_lastlogout = ts;
	}
	
	public void updateLastLogOut() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			_lastlogout = new Timestamp(System.currentTimeMillis());
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE accounts SET last_log_out=? WHERE login = ?";
			pstm = con.prepareStatement(sqlstr);
			Timestamp time = new Timestamp(System.currentTimeMillis());
			pstm.setTimestamp(1, time);
			pstm.setString(2, getName());
			pstm.execute();
			
			_lastlogout = time;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	// 그랑카인의 분노:%d%% (1단계)<BR>획득 경험치: -30%% <BR>리덕션: -10 <BR>공격력: -10%% 감소
	// 그랑카인의 분노:%d%% (2단계)<BR>획득 경험치: -60%% <BR>리덕션: -20 <BR>공격력: -20%% 감소
	// 그랑카인의 분노:%d%% (3단계)<BR>획득 경험치: -90%% <BR>사망 패널티: EXP -100%%<BR>리덕션:
	// -30<BR>공격력: -30%%
	// 그랑카인의 분노:%d%% (4단계)<BR>획득 경험치: -90%% <BR>사망 패널티: EXP -100%%<BR>리덕션:
	// -40<BR>공격력: -40%%
	// 그랑카인의 분노:%d%% (5단계)<BR>획득 경험치: -90%% <BR>사망 패널티: EXP -100%%<BR>리덕션:
	// -60<BR>공격력: -60%%
	// 그랑카인의 분노:%d%% (6단계)<BR>획득 경험치: -90%% <BR>사망 패널티: EXP -100%%<BR>리덕션:
	// -90<BR>공격력: -90%%<BR>몬스터 공격 시 정당방위 효과
	public double getGrangKinAngerExpCalc() {
		double exp_down = 0.0;
		// TODO 그랑카인의 분노 경험치 획득량 0.7 - 30%만깎임
		if (getGrangKinAngerStat() == 1) {
			exp_down = 0.7;
		} else if (getGrangKinAngerStat() == 2) {
			exp_down = 0.4;
		} else if (getGrangKinAngerStat() >= 3) {
			exp_down = 0.1;
		}
		return exp_down;
	}

	public double getGrangKinAngerReducCalc() {
		double dmgRate = 1.0;
		// TODO 그랑카인의 분노 몬스터가 PC에게 타격 대미지 조정 1.1(10%)추가
		if (getGrangKinAngerStat() == 1) {
			dmgRate = 3.0;
		} else if (getGrangKinAngerStat() == 2) {
			dmgRate = 3.5;
		} else if (getGrangKinAngerStat() >= 3) {
			dmgRate = 4.0;
		} else if (getGrangKinAngerStat() >= 4) {
			dmgRate = 4.5;
		} else if (getGrangKinAngerStat() >= 5) {
			dmgRate = 5.0;
		} else if (getGrangKinAngerStat() >= 6) {
			dmgRate = 5.0;
		}

		return dmgRate;
	}

	// TODO 그랑카인의 분노 PC가 몬스터에게 타격 대미지 조정 0.1(10%) 0.1~0.9 설정할 것
	public int getGrangKinAngerDmgCalc(double dmg) {
		int calcDmg = 0;

		if (getGrangKinAngerStat() == 1) {
			calcDmg = (int) (dmg * 0.5);
		} else if (getGrangKinAngerStat() == 2) {
			calcDmg = (int) (dmg * 0.6);
		} else if (getGrangKinAngerStat() >= 3) {
			calcDmg = (int) (dmg * 0.7);
		} else if (getGrangKinAngerStat() >= 4) {
			calcDmg = (int) (dmg * 0.8);
		} else if (getGrangKinAngerStat() >= 5) {
			calcDmg = (int) (dmg * 0.9);
		} else if (getGrangKinAngerStat() >= 6) {
			calcDmg = (int) (dmg * 0.9);
		}

		return (int) (dmg - calcDmg);
	}
	
	public int getAccountId() {
		return _id;
	}

	public void setAccountId(int i) {
		_id = i;
	}
	
	public static Integer[] loadAccountAddress(String accountName){
		MJObjectWrapper<Integer[]> wrapper = new MJObjectWrapper<Integer[]>();
		Selector.exec("select ip from accounts where login=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, accountName);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					try{					
						String addressValues = rs.getString("ip");
						StringTokenizer st = new StringTokenizer(addressValues, ".");

						String ip1 = st.nextToken();
						String ip2 = st.nextToken();
						String ip3 = st.nextToken();
						Integer[] itg = new Integer[]{
								Integer.parseInt(ip1),	
								Integer.parseInt(ip2),	
								Integer.parseInt(ip3),	
						};
						wrapper.value = itg;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		return wrapper.value;
	}
}
