package l1j.server.server.clientpackets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameClient;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.utils.SQLUtil;

public class Authorization {
	private static Authorization uniqueInstance = null;
	private static Logger _log = Logger.getLogger(C_AuthLogin.class.getName());

	public static Authorization getInstance() {
		if (uniqueInstance == null) {
			synchronized (Authorization.class) {
				if (uniqueInstance == null)
					uniqueInstance = new Authorization();
			}
		}
		return uniqueInstance;
	}

	public synchronized void auth(final GameClient client, String accountName, String password, String ip, String host)
			throws IOException {
		if (!Config.ALLOW_2PC) {
			if (LoginController.getInstance().getIpCount(ip) > 0) {
				_log.info("동일 IP로 접속한 두 PC의 로그인 거부. account=" + accountName + " ip=" + ip);
				System.out.println("동일 IP로 접속한 두 PC의 로그인 거부. account=" + accountName + " ip=" + ip);
				client.sendPacket(new S_CommonNews("현재 IP로 다른 계정이 이미 접속중입니다."));
				try {
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		} else if (LoginController.getInstance().getIpCount(ip) > MJNetServerLoadManager.NETWORK_CLIENT_PERMISSION && !ip.equals(MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT)) {
			_log.info("동일한 IP로 접속한 두 PC의 로그인을 거부했습니다. account=" + accountName + " ip=" + ip);
			System.out.println("동일한 IP로 접속한 두 PC의 로그인을 거부했습니다. account=" + accountName + " ip=" + ip);
			client.sendPacket(new S_CommonNews("현재 IP로 다른 계정이 이미 접속중입니다."));
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		Account account = Account.load(accountName);
		if (account == null) {
			if (Config.AUTO_CREATE_ACCOUNTS) {
				//if (Account.checkLoginIP(ip)) {
				if (isCheckIP(ip)) {
					_log.info("    ■■■■■■■■ 계정 생성 초과 : 아디정확한지 확인- > accountName = " + accountName);
					System.out.println("    ■■■■■■■■ 계정 생성 초과 : 아디정확한지 확인- > accountName = " + accountName);
					client.sendPacket(new S_CommonNews(String.format("더이상 계정을 생성 할 수 없습니다.(최대 %d개)", MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT, MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT)));
					try {
						Runnable r = () -> {
							client.kick();
						};
						GeneralThreadPool.getInstance().schedule(r, 1500L);
					} catch (Exception e1) {
					}
					return;
				} else {
					if (!isValidAccount(accountName)) { // -- remove
						client.sendPacket(new S_LoginResult(S_LoginResult.REASON_WRONG_ACCOUNT));
						return;
					}
					if (!isValidPassword(password)) { // -- remove
						client.sendPacket(new S_LoginResult(S_LoginResult.REASON_WRONG_PASSWORD));
						return;
					} // 여기까지
					account = Account.create(accountName, password, ip, host);
					account = Account.load(accountName);
				}
			} else {
				_log.warning("account missing for user " + accountName);
				System.out.println("account missing for user " + accountName);
			}
		}
		if (account == null || !account.validatePassword(accountName, password)) {
			client.sendPacket(new S_LoginResult(S_LoginResult.REASON_USER_OR_PASS_WRONG));
			return;
		}
		{
			Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();
			boolean find = false;
			for (L1PcInstance bugpc : pcs) {
				if (bugpc.getAccountName().equalsIgnoreCase(accountName)&& (bugpc.isPrivateShop() == false || bugpc.getNetConnection() != null)) {
					if (bugpc.getMapId() >= 6000 && bugpc.getMapId() <= 6999 || bugpc.getMapId() >= 9000 && bugpc.getMapId() <= 9100){
						continue;
					}
					bugpc.getMap().setPassable(bugpc.getLocation(), true);
					bugpc.setX(33080);
					bugpc.setY(33392);
					bugpc.setMap((short) 4);
					System.out.println("─────────────────────────────────");
					System.out.println("한계정 동시접속으로 종료 합니다. ▶계정명◀: " + accountName + "");
					System.out.println("─────────────────────────────────");
					GameServer.disconnectChar(bugpc);
					bugpc.sendPackets(new S_Disconnect());
					find = true;
				}
			}
			if (find) {
				client.kick();
				return;
			}

			pcs = null;
		}

		int reason = account.getBannedCode();
		if (reason != 0) { // BAN 어카운트
			System.out.println("\n┌───────────────────────────────┐");
			System.out.println("압류된 계정의 로그인을 거부했습니다. account=" + accountName + " ip=" + ip);
			System.out.println("└───────────────────────────────┘\n");
			
			client.sendPacket(new S_LoginResult(reason));// 시스템버그 관련 창 평범
			return;
		}

		if (account.getAccessLevel() == Config.GMCODE) {
			Random random = new Random();
			ip = Integer.toString(random.nextInt(80) + 100) + "." + Integer.toString(random.nextInt(100) + 50) + "."
					+ Integer.toString(random.nextInt(100) + 50) + "." + Integer.toString(random.nextInt(100) + 50);
			account.setIp(ip);
		}
		try {
			LoginController.getInstance().login(client, account);
			Account.updateLastActive(account, ip); // 최종 로그인일을 갱신한다
			client.setAccount(account);
			sendNotice(client);
		} catch (GameServerFullException e) {
			client.kick();
			_log.info("최대 접속인원 초과 : (" + client.getIp() + ") 로그인을 절단했습니다. ");
			System.out.println("최대 접속인원 초과 : (" + client.getIp() + ") 로그인을 절단했습니다. ");
			return;
		} catch (AccountAlreadyLoginException e) {
			_log.info("동일한 ID의 접속 : (" + client.getIp() + ") 강제 절단 했습니다.");
			System.out.println("동일한 ID의 접속 : (" + client.getIp() + ") 강제 절단 했습니다.");
			client.sendPacket(new S_CommonNews("이미 접속 중 입니다. 접속을 강제 종료합니다."));
			client.kick();
			return;
		} catch (Exception e) {
			_log.info("비정상적인 로그인 에러 . account=" + accountName + " host=" + host);
			System.out.println("비정상적인 로그인 에러 . account=" + accountName + " host=" + host);
			client.kick();
			return;
		} finally {
			account = null;
		}
	}

	private void sendNotice(GameClient client) {
		String accountName = client.getAccountName();

		// 읽어야할 공지가 있는지 체크
		if (S_CommonNews.NoticeCount(accountName) > 0) {
			client.sendPacket(new S_CommonNews(accountName, client));
		} else {
			new C_CommonClick(client);
		}
		accountName = null;
	}

	private boolean isValidAccount(String account) {
		if (account.length() < 5 || account.length() > 12) {
			System.out.println("계정 길이 체크(무시): " + account.length());// 가끔 아이 비번첫을때 틀렸다고 나올때
			return false;
		}

		char[] chars = account.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isLetterOrDigit(chars[i])) {
				return false;
			}
		}

		return true;
	}

	private boolean isValidPassword(String password) {
		if (password.length() < 6) {
			return false;
		}
		if (password.length() > 16) {
			return false;
		}

		boolean hasLetter = false;
		boolean hasDigit = false;

		char[] chars = password.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				hasLetter = true;
			} else if (Character.isDigit(chars[i])) {
				hasDigit = true;
			} else {
				return false;
			}
		}

		if (!hasLetter || !hasDigit) {
			return false;
		}

		return true;
	}
	
	private boolean isCheckIP(String ip) {
		int num = 0;  
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(ip) as cnt FROM accounts WHERE ip=? ");

			pstm.setString(1, ip);
			rs = pstm.executeQuery();

			if (rs.next()) num = rs.getInt("cnt");  

			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

			// 동일 IP로 생성된 계정이 3개 미만인 경우
			if (num < MJNetServerLoadManager.NETWORK__ADDRESS2ACCOUNT)//계정생성외부화
				return false;
			else
				return true;
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}return false;
	}
}
