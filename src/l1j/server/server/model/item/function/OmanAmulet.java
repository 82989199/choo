package l1j.server.server.model.item.function;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class OmanAmulet {

	public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
		if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
			return;
		}
		if (!pc.getMap().isEscapable()){
			return;
		}
//		if(pc.getX() >=33926 && pc.getX() <=33930 && pc.getY() >=33342 && pc.getY() <=33347){ // 아덴 마을 동상 주변
		if(pc.getX() >=33425 && pc.getX() <=33435 && pc.getY() >=32808 && pc.getY() <=32818){ // 기란 마을 십자가 주변
			switch (itemId) {
			case 830012: //오만의 탑 1층 이동 부적
			case 830022: //오만의 탑 1층 지배 부적
				pc.start_teleport(32735, 32798, 101, pc.getHeading(), 169, true, false);
				break;
			case 830013: //오만의 탑 2층 이동 부적
			case 830023: //오만의 탑 2층 지배 부적
				pc.start_teleport(32727, 32803, 102, pc.getHeading(), 169, true, false);
				break;
			case 830014: //오만의 탑 3층 이동 부적
			case 830024: //오만의 탑 3층 지배 부적
				pc.start_teleport(32726, 32803, 103, pc.getHeading(), 169, true, false);
				break;
			case 830015: //오만의 탑 4층 이동 부적
			case 830025: //오만의 탑 4층 지배 부적
				pc.start_teleport(32620, 32859, 104, pc.getHeading(), 169, true, false);
				break;
			case 830016: //오만의 탑 5층 이동 부적
			case 830026: //오만의 탑 5층 지배 부적
				pc.start_teleport(32601, 32866, 105, pc.getHeading(), 169, true, false);
				break;
			case 830017: //오만의 탑 6층 이동 부적
			case 830027: //오만의 탑 6층 지배 부적a
				pc.start_teleport(32611, 32863, 106, pc.getHeading(), 169, true, false);
				break;
			case 830018: //오만의 탑 7층 이동 부적
			case 830028: //오만의 탑 7층 지배 부적
				pc.start_teleport(32618, 32866, 107, pc.getHeading(), 169, true, false);
				break;
			case 830019: //오만의 탑 8층 이동 부적
			case 830029: //오만의 탑 8층 지배 부적
				pc.start_teleport(32602, 32867, 108, pc.getHeading(), 169, true, false);
				break;
			case 830020: //오만의 탑 9층 이동 부적
			case 830030: //오만의 탑 9층 지배 부적
				pc.start_teleport(32613, 32866, 109, pc.getHeading(), 169, true, false);
				break;
			case 830021: //오만의 탑 10층 이동 부적
			case 830031: //오만의 탑 10층 지배 부적
				pc.start_teleport(32730, 32803, 110, pc.getHeading(), 169, true, false);
				break;
			}
		}else{
//			pc.sendPackets(new S_SystemMessage("아덴 마을 동상 주변에서만 사용이 가능합니다."));
			pc.sendPackets(new S_SystemMessage("기란 마을 중앙 십자가 주변에서만 사용 가능합니다."));
		}
	}
}
