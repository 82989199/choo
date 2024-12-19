package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1AccountBanKick implements L1CommandExecutor {

	private L1AccountBanKick() {	}

	public static L1CommandExecutor getInstance() {
		return new L1AccountBanKick();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String				name	= st.nextToken();
			if(name == null || name.equalsIgnoreCase(""))
				throw new Exception("");
			
			int reason = S_LoginResult.banServerCodes.get(Integer.parseInt(st.nextToken()));
			
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			if (target == null) {
				target = CharacterTable.getInstance().restoreCharacter(name);
			}

			if (target != null) { // 어카운트를 BAN 한다
				final GameClient clnt = target.getNetConnection();
				Account.ban(target.getAccountName(), reason);
				pc.sendPackets(new S_SystemMessage(target.getName() + " 를 계정압류 하였습니다."));
				target.sendPackets(new S_Disconnect());
				
				if (target.getOnlineStatus() == 1) {
					target.sendPackets(new S_Disconnect());
				}
				GeneralThreadPool.getInstance().schedule(new Runnable(){
					@Override
					public void run(){
						if(clnt != null && clnt.isConnected()){
							try {
								clnt.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}, 1000L);
			} else {
				pc.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다. "));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".계정압류 [캐릭터명] [압류사유번호] 으로 입력해 주세요."));
			pc.sendPackets(new S_SystemMessage("[압류사유]: 1(고정). 미풍양속으로.. 그외 없음"));
		}
	}
}
