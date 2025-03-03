package l1j.server.server.model.poison;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ServerMessage;

public abstract class L1Poison {
	protected static boolean isValidTarget(L1Character cha) {
		if (cha == null) {
			return false;
		}
		
		if (cha.getPoison() != null) {
			return false;
		}

		if (!(cha instanceof L1PcInstance)) {
			return true;
		}
		
		if (cha.hasSkillEffect(L1SkillId.ICE_LANCE)
				|| cha.hasSkillEffect(L1SkillId.MOB_COCA)
				|| cha.hasSkillEffect(L1SkillId.MOB_BASILL)
				|| cha.hasSkillEffect(L1SkillId.EARTH_BIND)) {
			return false;
		}

		L1PcInstance player = (L1PcInstance) cha;
		if (player.getInventory().checkEquipped(20298)//제니스의반지
				|| player.getInventory().checkEquipped(20117)//바포메트의 갑옷
				|| player.getInventory().checkEquipped(22196)//안타라스의 완력
				|| player.getInventory().checkEquipped(22197)//안타라스의 예지력
				|| player.getInventory().checkEquipped(22198)//안타라스의 인내력
				|| player.getInventory().checkEquipped(22199)//안타라스의 마력
				|| player.getInventory().checkEquipped(111137)//할파스의 완력
				|| player.getInventory().checkEquipped(111141)//할파스의 예지력
				|| player.getInventory().checkEquipped(111140)//할파스의 마력
				|| player.hasSkillEffect(L1SkillId.VENOM_RESIST)) {
			return false;
		} 
		return true;
	}

	protected static void sendMessageIfPlayer(L1Character cha, int msgId) {
		if (!(cha instanceof L1PcInstance)) {
			return;
		}

		L1PcInstance player = (L1PcInstance) cha;
		player.sendPackets(new S_ServerMessage(msgId));
	}

	public abstract int getEffectId();

	public abstract void cure();
}
