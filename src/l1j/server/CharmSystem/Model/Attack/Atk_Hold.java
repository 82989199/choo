package l1j.server.CharmSystem.Model.Attack;

import l1j.server.CharmSystem.Model.CharmSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_Hold extends CharmSkillModel{

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if(MJCommons.isUnbeatable(defender) || defender.hasSkillEffect(L1SkillId.STATUS_FREEZE) || !isPlay(attacker, t_item))
			return 0D;
		
		int pure = getProbability(attacker, t_item);
		if(is_magic){
			if(!MJCommons.isMagicSuccess(attacker, defender, pure))
				return 0D;
			
			if(isCounterMagic(defender))
				return 0D;
		}else{
			if(!isInPercent(pure))
				return 0D;
		}
		
		double time =  _rnd.nextInt(max_val - min_val) + min_val;
		time 		= calcEnchant(t_item, time);
		time		= calcAttr(defender, time);
		time		= calcStat(attacker, time);
		int t		= (int)time;
		L1EffectSpawn.getInstance().spawnEffect(81182, t, defender.getX(), defender.getY(), defender.getMapId());
		defender.setSkillEffect(L1SkillId.STATUS_FREEZE, t);
		
		if(eff_id > 0)
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
		defender.sendPackets(MJCommons.on_paralysis, false);
		return 0D;
	}

}
