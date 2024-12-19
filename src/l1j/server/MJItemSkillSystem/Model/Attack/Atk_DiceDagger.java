package l1j.server.MJItemSkillSystem.Model.Attack;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_DiceDagger extends MJItemSkillModel{

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if(!isPlay(attacker, t_item))
			return 0D;
		
		int hp = defender.getCurrentHp();
		if(hp <= 0)
			return 0D;
		
		if(isCounterMagic(defender) || MJCommons.isUnbeatable(defender)){
			remove(attacker, t_item);
			return 0D;
		}
		
		double dmg = (hp * 2) / 3;
		dmg = MJCommons.getMagicDamage(attacker,  defender, (int)dmg);
		dmg = calcEnchant(t_item, dmg);
		dmg = calcAttr(defender, dmg);
		dmg = calcStat(attacker, dmg);
		
		if(eff_id > 0)
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
		remove(attacker, t_item);
		if(dmg >= defender.getCurrentHp() + 10)
			return defender.getCurrentHp() - 10;
		return dmg;
	}

	private void remove(L1Character c, L1ItemInstance item){
		c.sendPackets(new S_ServerMessage(158, item.getLogName()), true);
		c.getInventory().removeItem(item, 1);
	}
}
