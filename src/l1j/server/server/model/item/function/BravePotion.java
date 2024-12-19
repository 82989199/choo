package l1j.server.server.model.item.function;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1PcInventory;

import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class BravePotion extends L1ItemInstance {
	public BravePotion(L1Item item) {
		super(item);
	}

	public static void checkConditon(L1PcInstance pc, L1ItemInstance item) {
		if (pc.hasSkillEffect(71)) {
			pc.sendPackets(new S_ServerMessage(698), true);
			return;
		}
		if ((pc.hasSkillEffect(157)) || (pc.hasSkillEffect(33)) || (pc.hasSkillEffect(10101))
				|| (pc.hasSkillEffect(30007)) || (pc.hasSkillEffect(30002)) || (pc.hasSkillEffect(30006))
				|| (pc.hasSkillEffect(40007)) || (pc.hasSkillEffect(30081)) || (pc.hasSkillEffect(22055))
				|| (pc.hasSkillEffect(22025)) || (pc.hasSkillEffect(22026)) || (pc.hasSkillEffect(22027))
				|| (pc.hasSkillEffect(30004)) || (pc.hasSkillEffect(87)) || (pc.hasSkillEffect(208))
				|| (pc.hasSkillEffect(103)) || (pc.hasSkillEffect(212)) || (pc.hasSkillEffect(123))) {
			return;
		}
		pc.cancelAbsoluteBarrier();
		pc.cancelMoebius();

		L1ItemInstance useItem = pc.getInventory().getItem(item.getId());
		int itemId = item.getItemId();
		if (((itemId == 40014) || (itemId == 140014) || (itemId == 41415) || (itemId == 30073))
				&& ((pc.isKnight()) || (pc.is전사()) || (pc.isCrown()))) {
			useBravePotion(pc, itemId);
		} else if (((itemId == 40068) || (itemId == 140068) || (itemId == 210110)) && (pc.isElf())) {
			useBravePotion(pc, itemId);
		} else if (((itemId == 210036) || (itemId == 30077) || (itemId == 713)) && (pc.isBlackwizard())) {
			useFruit(pc, itemId);
		} else {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
			return;
		}
		pc.getInventory().removeItem(useItem, 1);
	}

	private static void useFruit(L1PcInstance pc, int item_id) {
		int time = 0;
		if ((item_id == 210036) || (item_id == 30077)) {
			time = 480;
		}
		if (item_id == 713) {
			time = 1800;
		}
		int type = 4;
		boolean isEffect = false;
		if (pc.isPassive(MJPassiveID.DARK_HORSE.toInt())) {
			type = 3;
		}
		if (pc.hasSkillEffect(1000)) {
			isEffect = true;
			pc.killSkillEffectTimer(1000);
		}
		if (pc.hasSkillEffect(20079)) {
			isEffect = true;
			pc.killSkillEffectTimer(20079);
		}
		if (isEffect) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
		}
		pc.setBraveSpeed(type);
		pc.setSkillEffect(pc.isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 1000 : 20079, time * 1000);
		pc.sendPackets(new S_SkillBrave(pc.getId(), type, time));
		pc.broadcastPacket(new S_SkillBrave(pc.getId(), type, 0));
		pc.sendPackets(new S_SkillSound(pc.getId(), pc.isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 751 : 7110));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), pc.isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 751 : 7110));
	}

	private static void useBravePotion(L1PcInstance pc, int item_id) {
		int time = 0;
		if ((item_id == 40014) || (item_id == 30073)) {
			time = 300;
		} else if (item_id == 140014) {
			time = 350;
		} else if (item_id == 41415) {
			time = 1800;
		} else if ((item_id == 40068) || (item_id == 30076)) {
			time = 480;
			if (pc.hasSkillEffect(1000)) {
				pc.killSkillEffectTimer(1000);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(177)) {
				pc.killSkillEffectTimer(177);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(178)) {
				pc.killSkillEffectTimer(178);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(179)) {
				pc.killSkillEffectTimer(179);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(155)) {
				pc.killSkillEffectTimer(155);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 140068) {
			time = 700;
			if (pc.hasSkillEffect(1000)) {
				pc.killSkillEffectTimer(1000);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(177)) {
				pc.killSkillEffectTimer(177);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(178)) {
				pc.killSkillEffectTimer(178);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(179)) {
				pc.killSkillEffectTimer(179);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(155)) {
				pc.killSkillEffectTimer(155);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 210110) {
			time = 1800;
			if (pc.hasSkillEffect(1000)) {
				pc.killSkillEffectTimer(1000);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(177)) {
				pc.killSkillEffectTimer(177);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(178)) {
				pc.killSkillEffectTimer(178);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(179)) {
				pc.killSkillEffectTimer(179);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 40733) {
			time = 600;
			if (pc.hasSkillEffect(1016)) {
				pc.killSkillEffectTimer(1016);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(52)) {
				pc.killSkillEffectTimer(52);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(101)) {
				pc.killSkillEffectTimer(101);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(177)) {
				pc.killSkillEffectTimer(177);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(178)) {
				pc.killSkillEffectTimer(178);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(179)) {
				pc.killSkillEffectTimer(179);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(20079)) {
				pc.killSkillEffectTimer(20079);
				pc.setBraveSpeed(0);
			}
		}
		if ((item_id == 40068) || (item_id == 140068) || (item_id == 210110) || (item_id == 30076)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 3, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
			pc.setSkillEffect(1016, time * 1000);
		} else {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
			pc.setSkillEffect(1000, time * 1000);
		}
		pc.sendPackets(new S_SkillSound(pc.getId(), 751));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
		pc.setBraveSpeed(1);
	}
}
