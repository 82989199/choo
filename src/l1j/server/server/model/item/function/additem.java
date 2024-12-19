package l1j.server.server.model.item.function;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;

public class additem {

	public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {

		switch (itemId) {
		case 4100164:// 반놀자 지원 상자
			if (pc.getInventory().checkItem(4100164, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(4100164, 1); // 삭제되는 아이템과

				if (pc.is전사()) {
					beginnerItem(pc, 3000573, 1, 0, 129, 0, true);
				}
				if (pc.isKnight()) {
					beginnerItem(pc, 3000572, 1, 0, 129, 0, true);
				}
				if (pc.isDragonknight()) {
					beginnerItem(pc, 3000578, 1, 0, 129, 0, true);
				}
				if (pc.isCrown()) {
					beginnerItem(pc, 3000579, 1, 0, 129, 0, true);
				}
				if (pc.isWizard()) {
					beginnerItem(pc, 3000575, 1, 0, 129, 0, true);
				}
				if (pc.isBlackwizard()) {
					beginnerItem(pc, 3000576, 1, 0, 129, 0, true);
				}
				if (pc.isElf()) {
					beginnerItem(pc, 3000574, 1, 0, 129, 0, true);
				}
				if (pc.isDarkelf()) {
					beginnerItem(pc, 3000577, 1, 0, 129, 0, true);
				}
			}
			break;
		case 4100165:// 반하자 지원상자
			if (pc.getInventory().checkItem(4100165, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(4100165, 1); // 삭제되는 아이템과

				if (pc.is전사()) {
					beginnerItem(pc, 4200090, 1, 0, 129, 0, true);
				}
				if (pc.isKnight()) {
					beginnerItem(pc, 4200091, 1, 0, 129, 0, true);
				}
				if (pc.isDragonknight()) {
					beginnerItem(pc, 4200092, 1, 0, 129, 0, true);
				}
				if (pc.isCrown()) {
					beginnerItem(pc, 4200093, 1, 0, 129, 0, true);
				}
				if (pc.isWizard()) {
					beginnerItem(pc, 4200094, 1, 0, 129, 0, true);
				}
				if (pc.isBlackwizard()) {
					beginnerItem(pc, 4200095, 1, 0, 129, 0, true);
				}
				if (pc.isElf()) {
					beginnerItem(pc, 4200096, 1, 0, 129, 0, true);
				}
				if (pc.isDarkelf()) {
					beginnerItem(pc, 4200097, 1, 0, 129, 0, true);
				}
			}
			break;
		case 3000035:// 하자 지원 상자
			if (pc.getInventory().checkItem(3000035, 1)) { // 체크 되는 아이템과 수량				
				pc.getInventory().consumeItem(3000035, 1); // 삭제되는 아이템과

				if (pc.is전사()) {
					beginnerItem(pc, 4100087, 1, 0, 129, 0, true);
				}
				if (pc.isKnight()) {
					beginnerItem(pc, 4100088, 1, 0, 129, 0, true);
				}
				if (pc.isDragonknight()) {
					beginnerItem(pc, 4100089, 1, 0, 129, 0, true);
				}
				if (pc.isCrown()) {
					beginnerItem(pc, 4100090, 1, 0, 129, 0, true);
				}
				if (pc.isWizard()) {
					beginnerItem(pc, 4100091, 1, 0, 129, 0, true);
				}
				if (pc.isBlackwizard()) {
					beginnerItem(pc, 4100092, 1, 0, 129, 0, true);
				}
				if (pc.isElf()) {
					beginnerItem(pc, 4100093, 1, 0, 129, 0, true);
				}
				if (pc.isDarkelf()) {
					beginnerItem(pc, 4100094, 1, 0, 129, 0, true);
				}
			}
			break;
		case 68088:
			if (pc.getInventory().checkItem(68088, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68088, 1); // 삭제되는 아이템과
				봉인템1(pc, 68096, 1, 0, 1, 0, true); // 마력의 고서(90)
				if (pc.is전사()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900145, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(90)
				}
			}
			break;
		case 68089:
			if (pc.getInventory().checkItem(68089, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68089, 1); // 삭제되는 아이템과
				봉인템1(pc, 68096, 1, 0, 1, 0, true); // 마력의 고서(90)
				if (pc.is전사()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900146, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(90)
				}
			}
			break;
		case 68090:
			if (pc.getInventory().checkItem(68090, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68090, 1); // 삭제되는 아이템과
				봉인템1(pc, 68096, 1, 0, 1, 0, true); // 마력의 고서(90)
				if (pc.is전사()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900147, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(90)
				}
			}
			break;
		case 68091:
			if (pc.getInventory().checkItem(68091, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68091, 1); // 삭제되는 아이템과
				봉인템1(pc, 68096, 1, 0, 1, 0, true); // 마력의 고서(90)
				if (pc.is전사()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900148, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(90)
				}
			}
			break;
		case 68092:
			if (pc.getInventory().checkItem(68092, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68092, 1); // 삭제되는 아이템과
				봉인템1(pc, 68096, 1, 0, 1, 0, true); // 마력의 고서(90)
				if (pc.is전사()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900149, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(90)
				}
			}
			break;
		case 68083:
			if (pc.getInventory().checkItem(68083, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68083, 1); // 삭제되는 아이템과
				봉인템1(pc, 68095, 1, 0, 1, 0, true); // 마력의 고서(85)
				if (pc.is전사()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900140, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(85)
				}
			}
			break;
		case 68084:
			if (pc.getInventory().checkItem(68084, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68084, 1); // 삭제되는 아이템과
				봉인템1(pc, 68095, 1, 0, 1, 0, true); // 마력의 고서(85)
				if (pc.is전사()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900141, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(85)
				}
			}
			break;
		case 68085:
			if (pc.getInventory().checkItem(68085, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68085, 1); // 삭제되는 아이템과
				봉인템1(pc, 68095, 1, 0, 1, 0, true); // 마력의 고서(85)
				if (pc.is전사()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900142, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(85)
				}
			}
			break;
		case 68086:
			if (pc.getInventory().checkItem(68086, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68086, 1); // 삭제되는 아이템과
				봉인템1(pc, 68095, 1, 0, 1, 0, true); // 마력의 고서(85)
				if (pc.is전사()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900143, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(85)
				}
			}
			break;
		case 68087:
			if (pc.getInventory().checkItem(68087, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68087, 1); // 삭제되는 아이템과
				봉인템1(pc, 68095, 1, 0, 1, 0, true); // 마력의 고서(85)
				if (pc.is전사()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900144, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(85)
				}
			}
			break;
		case 68078:
			if (pc.getInventory().checkItem(68078, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68078, 1); // 삭제되는 아이템과
				봉인템1(pc, 68094, 1, 0, 1, 0, true); // 마력의 고서(80)
				if (pc.is전사()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900135, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(80)
				}
			}
			break;
		case 68079:
			if (pc.getInventory().checkItem(68079, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68079, 1); // 삭제되는 아이템과
				봉인템1(pc, 68094, 1, 0, 1, 0, true); // 마력의 고서(80)
				if (pc.is전사()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900136, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(80)
				}
			}
			break;
		case 68080:
			if (pc.getInventory().checkItem(68080, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68080, 1); // 삭제되는 아이템과
				봉인템1(pc, 68094, 1, 0, 1, 0, true); // 마력의 고서(80)
				if (pc.is전사()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900137, 1, 0, 1, 0, true); // 체력의 엘릭서 룬(80)
				}
			}
			break;
		case 68081:
			if (pc.getInventory().checkItem(68081, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68081, 1); // 삭제되는 아이템과
				봉인템1(pc, 68094, 1, 0, 1, 0, true); // 마력의 고서(80)
				if (pc.is전사()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900138, 1, 0, 1, 0, true); // 지식의 엘릭서 룬(80)
				}
			}
			break;
		case 68082:
			if (pc.getInventory().checkItem(68082, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(68082, 1); // 삭제되는 아이템과
				봉인템1(pc, 68094, 1, 0, 1, 0, true); // 마력의 고서(80)
				if (pc.is전사()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
				if (pc.isElf()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 900139, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(80)
				}
			}
			break;
		case 60041:
			if (pc.getInventory().checkItem(60041, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60041, 1); // 삭제되는 아이템과
				봉인템1(pc, 68093, 1, 0, 1, 0, true); // 마력의 고서(70)
				if (pc.is전사()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
				if (pc.isElf()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222316, 1, 0, 1, 0, true); // 힘의 엘릭서 룬(70레벨)
				}
			}
			break;
		case 60042:
			if (pc.getInventory().checkItem(60042, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60042, 1); // 삭제되는 아이템과
				봉인템1(pc, 68093, 1, 0, 1, 0, true); // 마력의 고서(70)
				if (pc.is전사()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isElf()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222312, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
			}
			break;
		case 60043:
			if (pc.getInventory().checkItem(60043, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60043, 1); // 삭제되는 아이템과
				봉인템1(pc, 68093, 1, 0, 1, 0, true); // 마력의 고서(70)
				if (pc.is전사()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isElf()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222313, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
			}
			break;
		case 60044:
			if (pc.getInventory().checkItem(60044, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60044, 1); // 삭제되는 아이템과
				봉인템1(pc, 68093, 1, 0, 1, 0, true); // 마력의 고서(70)
				if (pc.is전사()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isElf()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222314, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬(70레벨)
				}
			}
			break;
		case 60045:
			if (pc.getInventory().checkItem(60045, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60045, 1); // 삭제되는 아이템과
				봉인템1(pc, 68093, 1, 0, 1, 0, true); // 마력의 고서(70)
				if (pc.is전사()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
				if (pc.isElf()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222315, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬(70레벨)
				}
			}
			break;
		case 60036:
			if (pc.getInventory().checkItem(60036, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60036, 1); // 삭제되는 아이템과
				봉인템1(pc, 60033, 1, 0, 1, 0, true); // 빛바랜 고서
				if (pc.is전사()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); // 힘의 엘릭서 룬: 55레벨
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); // 힘의 엘릭서 룬: 55레벨
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); // 힘의 엘릭서 룬: 55레벨
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); // 힘의 엘릭서 룬: 55레벨
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); // 힘의 엘릭서 룬: 55레벨
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); // 힘의 엘릭서 룬: 55레벨
				}
				if (pc.isElf()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); //힘의 엘릭서 룬: 55레벨
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222299, 1, 0, 1, 0, true); // 힘의 엘릭서 룬: 55레벨
				}
			}
			break;
		case 60037:
			if (pc.getInventory().checkItem(60037, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60037, 1); // 삭제되는 아이템과
				봉인템1(pc, 60033, 1, 0, 1, 0, true); // 빛바랜 고서
				if (pc.is전사()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
				if (pc.isElf()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222295, 1, 0, 1, 0, true); // 민첩의 엘릭서 룬: 55레벨
				}
			}
			break;
		case 60038:
			if (pc.getInventory().checkItem(60038, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60038, 1); // 삭제되는 아이템과
				봉인템1(pc, 60033, 1, 0, 1, 0, true); // 빛바랜 고서
				if (pc.is전사()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
				if (pc.isElf()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222296, 1, 0, 1, 0, true); // 체력의 엘릭서 룬: 55레벨
				}
			}
			break;
		case 60039:
			if (pc.getInventory().checkItem(60039, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60039, 1); // 삭제되는 아이템과
				봉인템1(pc, 60033, 1, 0, 1, 0, true); // 빛바랜 고서
				if (pc.is전사()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
				if (pc.isElf()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222297, 1, 0, 1, 0, true); // 지식의 엘릭서 룬: 55레벨
				}
			}
			break;
		case 60040:
			if (pc.getInventory().checkItem(60040, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(60040, 1); // 삭제되는 아이템과
				봉인템1(pc, 60033, 1, 0, 1, 0, true); // 빛바랜 고서
				if (pc.is전사()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
				if (pc.isKnight()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
				if (pc.isDragonknight()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
				if (pc.isCrown()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
				if (pc.isWizard()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
				if (pc.isBlackwizard()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
				if (pc.isElf()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
				if (pc.isDarkelf()) {
					봉인템1(pc, 222298, 1, 0, 1, 0, true); // 지혜의 엘릭서 룬: 55레벨
				}
			}
			break;
		}
	}

	public static boolean beginnerItem(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr,
			boolean identi) {

		/*long now = System.currentTimeMillis();
		long diff = now - MJClientStatus.TestMillis;
		if(diff > 500)
			System.out.println("use1 : " +  diff + " " + now + " " + MJClientStatus.TestMillis + " " + chocco.cpu + "%");
		MJClientStatus.TestMillis = now;*/
		
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(attr);
			item.setIdentified(true);
			if (!item.isStackable())
				CommonUtil.SetTodayDeleteTime(item, 4320);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				/*now = System.currentTimeMillis();
				diff = now - MJClientStatus.TestMillis;
				if(diff > 500)
					System.out.println("use2 : " +  diff + " " + now + " " + MJClientStatus.TestMillis + " " + chocco.cpu + "%");
				MJClientStatus.TestMillis = now;*/
				
				pc.getInventory().storeItem(item);
				item.setBless(Bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				
				/*now = System.currentTimeMillis();
				diff = now - MJClientStatus.TestMillis;
				if(diff > 500)
					System.out.println("use3 : " +  diff + " " + now + " " + MJClientStatus.TestMillis + " " + chocco.cpu + "%");
				MJClientStatus.TestMillis = now;*/
				
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
				
				/*now = System.currentTimeMillis();
				diff = now - MJClientStatus.TestMillis;
				if(diff > 500)
					System.out.println("use4 : " +  diff + " " + now + " " + MJClientStatus.TestMillis + " " + chocco.cpu + "%");
				MJClientStatus.TestMillis = now;*/
				
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				pc.sendPackets(new S_ServerMessage(82));
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				
				/*for (L1ItemInstance itm : pc.getInventory().getItems()) {

					pc.getInventory().removeItem(itm);
				}
				pc.getInventory().storeItem(3000035, 400);*/
				
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			return true;
		} else {
			return false;
		}
	}
	private static boolean 봉인템1(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr,
			boolean identi) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(attr);
			item.setIdentified(false);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(Bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				pc.sendPackets(new S_ServerMessage(82));
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
			return true;
		} else {
			return false;
		}
	}

}
