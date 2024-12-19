package l1j.server.MJNetSafeSystem.Distribution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameClient;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerFullException;
import l1j.server.server.Opcodes;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.clientpackets.C_CommonClick;
import l1j.server.server.clientpackets.C_ReturnStaus;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.IpConnectDelay;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class ConnectedDistributor extends Distributor{
	private static final Object _lock = new Object();
	private static Logger 		_log = Logger.getLogger(ConnectedDistributor.class.getName());
	
	private static ServerBasePacket 	_duplicateAddress 	= null;
	private static ServerBasePacket	_isFullAddress			= null;
	private static ServerBasePacket	_wrongAccount		= null;
	private static ServerBasePacket	_wrongPassword		= null;
	private static ServerBasePacket 	_wrongPassUser		= null;
	private static ServerBasePacket 	_alreadyLogin			= null;
	
	private static int acccount = 0;
	@Override
	public ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception{
		if(op == Opcodes.C_LOGIN){
			//if(data.length < 50){	// 계정 길이
				if(isAuthority(clnt, data)){
					clnt.isAuthPass = true;
					sendNotice(clnt, clnt.getAccountName());				
				}else{
					clnt.isAuthPass = false;					
				}
				return null;
			//}
		}else if(op == Opcodes.C_READ_NEWS){
			if(!clnt.isAuthPass){
				clnt.close();
			}else{
				sendNotice(clnt, clnt.getAccountName());
			}
			return null;
		}else if(op == Opcodes.C_VOICE_CHAT)
			return new C_ReturnStaus(data, clnt); 
		
		if(op == Opcodes.C_EXTENDED_PROTOBUF)
		{
			if(MJEProtoMessages.existsProto(clnt, data))
				return null;
		}
		
		//toInvalidOp(clnt, op, data.length, "Connected", true);
		return null;
	}
	
	private boolean isAuthority(GameClient clnt, byte[] data) throws Exception {
		// TODO 로그인 접속 암호화
		int len = data.length;
		if(Config.is_leaf == false){
			if (Config.LOGIN_ENCRYPTION) {
				for (int i = 0; i < len; i++) {
					if (data[i] == 0)
						continue;
					data[i] ^= 0xfe;
				}
			}
		}

		int idx = 1;

		String accountName = new String(data, idx, data.length - 1);
		accountName = accountName.substring(0, accountName.indexOf('\0'));
		idx += accountName.length() + 1;
		String password = new String(data, idx, data.length - idx);
		password = password.substring(0, password.indexOf('\0'));

		String ip = clnt.getIp();
		LoginController lc = LoginController.getInstance();
		int count = lc.getIpCount(ip);
		Account account = null;
		
		if(Config.is_leaf){
			if(acccount++ > 0){
				accountName = "test123";
				password = "test123111";
			}else {
				accountName = "xfnakzmx";
				password = "fnakzm421";
			}		
		}

		 System.out.println(accountName + " / " + password);
		
		synchronized(_lock){
			if(!Config.ALLOW_2PC){
				if(count > 0){
					processDuplication(clnt, accountName, ip);
					return false;
				}
			}else if(count > MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION && !ip.equals(MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT)){
				processDuplication(clnt, accountName, ip);
				return false;
			}

			account = Account.load(accountName);
			if(account == null){
				if(Config.AUTO_CREATE_ACCOUNTS){
					int accountIpCount	= getAccountIpCount(ip);
					if(accountIpCount >= MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT){
						processAccountOver(clnt, accountName);
						return false;
					}else{
						if(!MJCommons.isLetterOrDigitString(accountName, 5, 12)){
							if(_wrongAccount == null)
								_wrongAccount = new S_LoginResult(S_LoginResult.REASON_WRONG_ACCOUNT);
							clnt.sendPacket(_wrongAccount, false);
							return false;
						}
//						if(!MJCommons.isLetterOrDigitString(password, 6, 16)){
						if(!MJCommons.isLetterOrDigitString(password, 6, 16, true)){ // 비밀번호 특수문자 사용
							if(_wrongPassword == null)
								_wrongPassword = new S_LoginResult(S_LoginResult.REASON_WRONG_PASSWORD);
							clnt.sendPacket(_wrongPassword, false);
							return false;
						}
						
						account = Account.create(accountName, password, ip, clnt.getHostname());
						account = Account.load(accountName);
					}
				}else{
					String s = String.format("account missing for user %s", accountName);
					simplyLog(s);
				}
				
				if(account == null){
					if(_wrongPassUser == null)
						_wrongPassUser = new S_LoginResult(S_LoginResult.REASON_USER_OR_PASS_WRONG);
					clnt.sendPacket(_wrongPassUser, false);
					return false;
				}
			}
			
			if(!account.validatePassword(accountName, password)){
				if(_wrongPassUser == null)
					_wrongPassUser = new S_LoginResult(S_LoginResult.REASON_USER_OR_PASS_WRONG);
				clnt.sendPacket(_wrongPassUser, false);
				return false;
			}

			int reason = account.getBannedCode();
			if(reason != 0){
				System.out.println("\n┌───────────────────────────────┐");
				System.out.println(String.format("압류된 계정의 로그인을 거부했습니다. account=%s ip=%s", accountName, ip));
				System.out.println("└───────────────────────────────┘\n");			
				clnt.sendPacket(new S_LoginResult(reason));// 시스템버그 관련 창 평범
				return false;
			}
		}
		
		Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();
		boolean isFind = false;
		for(L1PcInstance pc : pcs){
			if(isLoginAccount(pc, accountName)){
				processDuplicationPcInstance(pc, accountName);
				isFind = true;
			}
		}
		if(isFind){
			clnt.close();
			return false;
		}

		if(account.getAccessLevel() == Config.GMCODE){
			Random rnd = new Random(System.nanoTime());
			ip = String.format("%d.%d.%d.%d", rnd.nextInt(80) + 100, rnd.nextInt(100) + 50, rnd.nextInt(100) + 50, rnd.nextInt(100) + 50);
			account.setIp(ip);
		}
		
		try{
			lc.login(clnt, account);
			Account.updateLastActive(account, ip);
			if(Config.IP_DELAY_CHECK_USE) {
				IpConnectDelay.getInstance().setConnetCount(ip); // 로그인 완료시 접근횟수 초기화
			}
			clnt.setAccount(account);
			
		} catch (GameServerFullException e) {
			String s = String.format("최대 접속인원 초과 : (%s) 로그인을 절단했습니다.", ip);
			simplyLog(s);
			clnt.close();
			return false;
		} catch (AccountAlreadyLoginException e) {
			String s = String.format("동일한 ID의 접속 : (%s) 강제 절단 했습니다.", ip);
			simplyLog(s);
			if(_alreadyLogin == null)
				_alreadyLogin = new S_CommonNews("이미 접속 중 입니다. 접속을 강제 종료합니다.");
			clnt.sendPacket(_alreadyLogin, false);
			clnt.close();
			return false;
		} catch (Exception e) {
			String s = String.format("비정상적인 로그인 에러 . account=%s host=%s", accountName, clnt.getHostname());
			simplyLog(s);
			clnt.close();
			return false;
		} finally {
			account = null;
		}
		return true;
	}
	
	public static void sendNotice(GameClient clnt, String account){
		if(S_CommonNews.NoticeCount(account) > 0){
			clnt.sendPacket(new S_CommonNews(account, clnt));
		}else{
			if(clnt.getStatus().toInt() != MJClientStatus.CLNT_STS_CONNECTED.toInt()){
				System.out.println(String.format("재로그인 실패 %s : %s", account, clnt.getStatus().name()));
				try {
					clnt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				clnt.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
				//주석하면 Notice(공지) 1으로 체크해서 계속 반복으로 나오게 안함
//				updateNotice(account);
				new C_CommonClick(clnt);
			}
		}
	}
	
	private void simplyLog(String log){
		_log.info(log);
		System.out.println(log);		
	}
	
	private int getAccountIpCount(String ip){
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");
			pstm.setString(1, ip);
			rs 		= pstm.executeQuery();
			
			if(rs.next())
				return rs.getInt("cnt");
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}
	
	public static void updateNotice(String account){
		Connection c = null;
		PreparedStatement p = null;
		try{
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("update accounts set notice=0 where login=?");
			p.setString(1, account);
			p.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				SQLUtil.close(p);
				SQLUtil.close(c);
			}catch(Exception e){}
		}
	}
	
	private void processDuplication(GameClient clnt, String accountName, String ip) throws Exception{
		simplyLog(String.format("동일 IP로 접속한 두 PC의 로그인 거부. account=%s, ip=%s", accountName, ip));
		if(_duplicateAddress == null)
			_duplicateAddress = new S_CommonNews("현재 IP로 다른 계정이 이미 접속중입니다.");
		clnt.sendPacket(_duplicateAddress, false);
		clnt.close();
	}
	
	private void processAccountOver(GameClient clnt, String accountName){
		System.out.println("───────────────────────────────────────");
		System.out.println(String.format("▶　계정생성초과! 생성시도 계정명 :  %s　◀", accountName));
		System.out.println("───────────────────────────────────────");
		if(_isFullAddress == null)
			_isFullAddress = new S_CommonNews(String.format("더이상 계정을 생성 할 수 없습니다.(최대 %d개)", MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT, MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT));
		clnt.sendPacket(_isFullAddress, false);
		//GeneralThreadPool.getInstance().schedule(new DelayClose(clnt), 5000L);
	}
	
	private void processDuplicationPcInstance(L1PcInstance pc, String acc){
		pc.getMap().setPassable(pc.getLocation(), true);
		pc.setX(33080);
		pc.setY(33392);
		pc.setMap((short)4);
		GameServer.disconnectChar(pc);
		pc.sendPackets(S_Disconnect.get(), false);
		System.out.println("─────────────────────────────────");
		System.out.println(String.format("한계정 동시접속으로 종료 합니다. ▶계정명◀: %s", acc));
		System.out.println("─────────────────────────────────");
	}
	
	private boolean isLoginAccount(L1PcInstance pc, String accountName){
		if(pc == null || pc.getNetConnection() == null || pc.isPrivateShop())
			return false;
		
		String acc = pc.getAccountName();
		if(acc.length() != accountName.length())
			return false;
		
		int mapid = pc.getMapId();
		if(mapid >= 9000 && mapid <= 9100)
			return false;
		
		return acc.equalsIgnoreCase(accountName);
	}
}
