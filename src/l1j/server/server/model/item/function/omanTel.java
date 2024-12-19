package l1j.server.server.model.item.function;

import l1j.server.server.model.L1Location;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class omanTel {

	public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
		L1Location newLocation = pc.getLocation().randomLocation(200, true);
		int newX = newLocation.getX();
		int newY = newLocation.getY();
		short mapId = (short) newLocation.getMapId();
		if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
			return;
		}
		if (!pc.getMap().isEscapable()){
			return;
		}
		switch (itemId) {
		case 830001: //오만의 탑 1층 이동 주문서
			if (pc.getMapId() == 101) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32735, 32798, (short) 101, pc.getHeading(), true);
				pc.start_teleport(32735, 32798, 101, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830002: //오만의 탑 2층 이동 주문서
			if (pc.getMapId() == 102) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32726, 32803, (short) 102, pc.getHeading(), true);
				pc.start_teleport(32726, 32803, 102, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830003: //오만의 탑 3층 이동 주문서
			if (pc.getMapId() == 103) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32726, 32803, (short) 103, pc.getHeading(), true);
				pc.start_teleport(32726, 32803, 103, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830004: //오만의 탑 4층 이동 주문서
			if (pc.getMapId() == 104) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32613, 32863, (short) 104, pc.getHeading(), true);
				pc.start_teleport(32613, 32863, 104, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830005: //오만의 탑 5층 이동 주문서
			if (pc.getMapId() == 105) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32597, 32867, (short) 105, pc.getHeading(), true);
				pc.start_teleport(32597, 32867, 105, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830006: //오만의 탑 6층 이동 주문서
			if (pc.getMapId() == 106) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32607, 32865, (short) 106, pc.getHeading(), true);
				pc.start_teleport(32607, 32865, 106, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830007: //오만의 탑 7층 이동 주문서
			if (pc.getMapId() == 107) {
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
				pc.start_teleport(32618, 32866, 107, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830008: //오만의 탑 8층 이동 주문서
			if (pc.getMapId() == 108) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32598, 32867, (short) 108, pc.getHeading(), true);
				pc.start_teleport(32598, 32867, 108, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830009: //오만의 탑 9층 이동 주문서
			if (pc.getMapId() == 109) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32609, 32866, (short) 109, pc.getHeading(), true);
				pc.start_teleport(32609, 32866, 109, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830010: //오만의 탑 10층 이동 주문서
			if (pc.getMapId() == 110) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32726, 32803, (short) 110, pc.getHeading(), true);
				pc.start_teleport(32726, 32803, 110, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 830011: //오만의 탑 정상 이동 주문서
			if (pc.getMapId() == 200) {
//				L1Teleport.teleport(pc, newX, newY, mapId, pc.getHeading(), true);
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 169, true, false);
			} else {
//				L1Teleport.teleport(pc, 32657, 32797, (short) 111, pc.getHeading(), true);
				pc.start_teleport(32657, 32797, 111, pc.getHeading(), 169, true, false);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		}
	}
}
