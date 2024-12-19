package l1j.server.CharmSystem.Model.Attack;

import l1j.server.CharmSystem.Model.CharmSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_MPDrain extends CharmSkillModel{

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		double dmg = _rnd.nextInt(max_val - min_val) + min_val;
		int enchant = t_item.getEnchantLevel();
		if(enchant >= 7){
			dmg += ((enchant - 7) + 4);
		}
		
		if(dmg > e_val)
			dmg = e_val;
		
		if(eff_id > 0)
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
		
		if(MJCommons.isUnbeatable(defender))
			return 0D;
		
		if(defender.getCurrentMp() < dmg)
			dmg = defender.getCurrentMp();
		defender.setCurrentMp((int)(defender.getCurrentMp() - dmg));
		attacker.setCurrentMp((int)(attacker.getCurrentMp() + dmg));
		return 0D;
	}
}
