package l1j.server.CharmSystem.Model.Defence;

import l1j.server.Config;
import l1j.server.CharmSystem.Model.CharmSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Def_Dmg_Mirror extends CharmSkillModel {

	@Override
	protected boolean isPlay(L1Character c, L1ItemInstance t_item) {
		return super.isPlay(c, t_item);
	}

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if (MJCommons.isUnbeatable(defender) || !isPlay(defender, t_item))
			return 0D;
		double reduc = min_val;
		if (min_val != max_val) {
			reduc = calcEnchant(t_item, reduc);
			reduc = calcAttr(defender, reduc);
			reduc = calcStat(defender, reduc);
		}
		if (reduc > max_val)
			reduc = max_val;

		L1PcInstance pc = null;
		L1PcInstance target = null;
		L1NpcInstance targetnpc = null;
		
		if (defender instanceof L1PcInstance)
			pc = (L1PcInstance) defender;

		if (pc.getEquipSlot().getWeapon() != null) {
			if (pc.getInventory().checkItem(41246, 10)) {
				if (eff_id > 0)
					broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
				
				if (attacker instanceof L1PcInstance)
					target = (L1PcInstance) attacker;
				
				if (target != null)
					target.receiveCounterBarrierDamage(attacker, MirorDmg(pc, attacker));
				
				if (attacker instanceof L1NpcInstance)
					targetnpc = (L1NpcInstance) attacker;
				
				if (targetnpc != null)
					targetnpc.receiveCounterBarrierDamage(attacker, MirorDmg(pc, attacker));
				
				pc.getInventory().consumeItem(41246, 10);
			} else {
				pc.sendPackets("伤害反弹：魔法结晶体不足.");
				return 0D;
			}
		}

		return reduc;
	}

	private int MirorDmg(L1Character defender, L1Character attacker) {
		L1PcInstance pc = null;
		if (defender instanceof L1PcInstance)
			pc = (L1PcInstance) defender;
		double damage = 0;
		L1ItemInstance weapon = null;
		weapon = pc.getWeapon();
		if (weapon != null) {
			damage = Math.round(
					(weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier())
							* Config.RockDMG);
		}
		return (int) damage;
	}

	@Override
	public boolean isAttack() {
		return false;
	}
}
