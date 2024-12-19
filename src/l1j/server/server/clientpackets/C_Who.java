package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.command.executor.L1UserCalc;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_WhoAmount;

public class C_Who extends ClientBasePacket {

	private static final String C_WHO = "[C] C_Who";

	public C_Who(byte[] decrypt, GameClient client) {
		super(decrypt);
		String s = readS();
		
		
		L1PcInstance find = L1World.getInstance().getPlayer(s);
		L1NpcShopInstance find1 = L1World.getInstance().getShopNpc(s);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;

		float win = 0;
		float lose = 0;
		float total = 0;
		float winner = 0;

		/*if (s.contentEquals("메티스") || s.contentEquals("미소피아") || s.contentEquals("카시오페아")) {
			pc.sendPackets("메티스 \\fW(Lawful) \\aH문의는 편지로 주세요");
			return;
		}*/

		if (find != null) {
			String clanname = find.getClanname();
			String lawful = "";

			if (find.getKDA() != null) {
				win = find.getKDA().kill;
				lose = find.getKDA().death;
			}
			
			String title = "";
			if (find.getTitle().equalsIgnoreCase("") == false) {
				title = find.getTitle() + " ";
			}
			
			total = win + lose;
			if(total > 0)
				winner = ((win * 100) / (total));

			if (find.getClan() != null) {
				if (find.getLawful() > 0)
					lawful = "\\fW(Lawful)";
				else
					lawful = "\\fY(Chaotic)";
				pc.sendPackets("" + find.getName() + " " + lawful + " \\aA혈맹:" + clanname);
				pc.sendPackets("\\aD[KILL]: " + (int) win+ "\\aA∥\\f3[DEATH]: " + (int) lose + "\\aA∥\\aH승률:" + winner + "%");
				//pc.sendPackets(String.format("\\aA%s%s \\aE[%s] \n\r ", find.getName(), lawful, clanname));
			} else {
				if (find.getLawful() > 0)
					lawful = "\\fW(Lawful)";
				else
					lawful = "\\fY(Chaotic)";
				pc.sendPackets("" + find.getName() + " " + lawful + " \\f2[KILL]: " + (int) win + " / [DEATH]: " + (int) lose+ " / 승률:" + winner + "%");
//				pc.sendPackets(String.format("\\aA%s%s \\aE[%s] \n\r ", find.getName(), lawful, clanname));
//				pc.sendPackets(String.format("\\aA%s%s \\aD%s \n\r \\f2Kill : \\aF%d  \\aGDeath : \\aF%d \\aI[승률: %.2f%%]", find.getName(), lawful, title, (int)win, (int)lose, winner));
			}
			return;
		}
		
		int AddUser = (int) (L1World.getInstance().getAllPlayers().size() * Config.Number_Count);// 유저1.5배뻥
		int CalcUser = L1UserCalc.getClacUser();
		AddUser += CalcUser;
		String amount = String.valueOf(AddUser);
		S_WhoAmount s_whoamount = new S_WhoAmount(amount);
		pc.sendPackets(s_whoamount);
	}

		/*String locName = MapsTable.getInstance().getMapName(pc.getMapId());
		if (pc.getMapId() == 0 || pc.getMapId() == 4 || locName == null) {
			pc.sendPackets(new S_ServerMessage(563));
			return;
		}

		if (find1 != null) {
			int i = pc.getMapId();
			int countPlayer = 0;
			for (L1Object each1 : L1World.getInstance().getVisibleObjects(i).values()) {
				if (each1 instanceof L1PcInstance) {
					countPlayer++;
					continue;
				}
			}
			pc.sendPackets(String.format("%s 이용자 : %d명",locName, (countPlayer * Config.Number_Count)));
		} else {
			int i = pc.getMapId();
			int countPlayer = 0;
			for (L1Object each1 : L1World.getInstance().getVisibleObjects(i).values()) {
				if (each1 instanceof L1PcInstance) {
					countPlayer++;
					continue;
				}
			}
			pc.sendPackets(String.format("%s 이용자 : %d명",locName, (countPlayer * Config.Number_Count)));
		}*/
//	}

	@Override
	public String getType() {
		return C_WHO;
	}
}
