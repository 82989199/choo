package l1j.server.MJItemSkillSystem.Model.Attack;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_MPDrainAndGet extends MJItemSkillModel{
	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if(!isPlay(attacker, t_item))
			return 0D;
		
		double dmg = 0D;
		
		if(is_magic){	
			dmg = MJCommons.getMagicDamage(attacker, defender, min_val, max_val);
		}else {
			dmg = _rnd.nextInt(max_val - min_val) + min_val;
		}
		
		dmg = calcEnchant(t_item, dmg);
		dmg = calcAttr(defender, dmg);
		dmg = calcStat(attacker, dmg);
		if(dmg > max_val) 		dmg = max_val;
		else if(dmg < min_val) 	dmg = min_val;
		
		if(eff_id > 0){
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
		}
		if(MJCommons.isUnbeatable(defender))
			return 0D;
		
		int d = 0;
		if(dmg > 0)
			d = ((int)dmg / 5);
		if(d > 0){
			if(defender.getCurrentMp() < d)
				d = defender.getCurrentMp();
			defender.setCurrentMp((defender.getCurrentMp() - d));
			attacker.setCurrentMp((attacker.getCurrentMp() + d));
		}
		return dmg;
	}
}
