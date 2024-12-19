package l1j.server.server.Controller;

import l1j.server.Config;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_SystemMessage;

public class TamController implements Runnable {

	public static final int SLEEP_TIME = Config.Tam_Time * 60000;

	private static TamController _instance;

	public static TamController getInstance() {
		if (_instance == null) {
			_instance = new TamController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			PremiumTime();
		} catch (Exception e1) {
		}
	}

	private void PremiumTime() {
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			int premium1 = Config.탐혈맹갯수;
			int premium2 = Config.탐성혈갯수;
			/** 탐지급부분 본섭화 **/
			if (!pc.isPrivateShop() && !pc.noPlayerCK && !pc.noPlayerck2 && pc != null && !pc.isDead()) {
				int tamcount = pc.tamcount();
				if (tamcount > 0) {
					int addtam = Config.탐갯수 * tamcount;
					pc.getNetConnection().getAccount().addTamPoint(addtam);
					try {
						pc.getNetConnection().getAccount().updateTam();
					} catch (Exception e) {
					}
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					pc.sendPackets(new S_SystemMessage("성장의 고리(" + tamcount + ")단계 Tam포인트(" + addtam + ")개 획득"));
					if (clan != null) {
						if (clan.getClanId() != 0) {
							pc.getAccount().addTamPoint(premium1);
							pc.sendPackets(new S_SystemMessage("혈맹 성장의 고리 Tam (" + premium1 + ")획득"));
						}else if (clan.getCastleId() != 0) {
							pc.getAccount().addTamPoint(premium2);
							pc.sendPackets(new S_SystemMessage("성혈맹 성장의 고리 Tam (" + premium2 + ")획득"));
						}
					}
					try {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, pc.getNetConnection()), true);
					} catch (Exception e) {
					}
				}
			}
		}
	}
}
