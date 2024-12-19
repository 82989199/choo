package l1j.server.MJItemSkillSystem.Model;

import java.util.Random;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Item Skill Model.
 * made by mjsoft, 2017.
 *  
 **********************************/
public abstract class MJItemSkillModel {
	protected static Random _rnd = new Random(System.nanoTime());
	
	public int 		itemId;			// 아이템의 고유 ID
	public int		condition;		// 스킬 발동 조건으로 인챈트 레벨 조건을 뜻한다.
	public int 		d_prob;			// default probability: 기본 발동 확률
	public int 		e_prob;			// enchant probability: 인챈트 레벨에 따라 증가하는 발동확률
	public int 		s_prob;			// stat probability: 스탯에 따른 발동 확률을 결정
	public double 	s_weight;		// stat probability in weight: 스탯에 따른 발동 확률 가중치
	public int 		min_val;		// min value: 최소 대미지 값
	public int 		max_val;		// max value: 최대 대미지 값
	public int 		stat_val;		// stat value: 스탯에 따른 추가 대미지 또는 효과를 결정 none, str, dex 등
	public double	stat_valweight;	// stat value weight: 스탯에 따른 추가 대미지 또는 효과의 가중치
	public int		e_val;			// enchant value: 인챈트 레벨에 따른 추가 대미지 값
	public double	e_valweight;	// enchant value weight: 인챈트 레벨에 따른 추가 대미지의 가중치
	public int 		eff_id;			// effect id: 발동되는 이펙트의 ID
	public int		attr_type;		// attribute type: 속성 유형 none, earth, fire, water, wind
	public boolean	is_magic;		// is magic skill: 마법인지 여부
	
	/**
	 * t_item is traget item instance.
	 * dwd is default weapon damage.
	 * **/
	public abstract double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd);
	
	protected boolean isInPercent(int p){
		return (_rnd.nextInt(100) + 1) <= p;
	}
	
	protected int getProbability(L1Character c, L1ItemInstance t_item){
		int prob = d_prob;
		if(condition <= 0) 
			prob += (e_prob * t_item.getEnchantLevel());
		else if(e_prob > 0){
			if(t_item.getEnchantLevel() >= condition){
				int ptmp = (t_item.getEnchantLevel() - condition) + 1;
				prob += (ptmp * e_prob);
			}
		}
		if(s_prob != 0){
			int stat = 0;
			switch(s_prob){
			case 1:	stat = c.getAbility().getTotalStr(); break;
			case 2:	stat = c.getAbility().getTotalDex(); break;
			case 3:	stat = c.getAbility().getTotalCon(); break;
			case 4:	stat = c.getAbility().getTotalWis(); break;
			case 5:	stat = c.getAbility().getTotalInt(); break;
			case 6:	stat = c.getAbility().getTotalCha(); break;
			}
			
			prob += (prob * (stat * s_weight));
		}
		return prob;
	}
	
	protected boolean isPlay(L1Character c, L1ItemInstance t_item){
		return condition <= t_item.getEnchantLevel() && isInPercent(getProbability(c, t_item));
	}
	
	protected double calcStat(L1Character c, double pure_dmg){
		double dmg = pure_dmg;
		if(stat_val != 0){
			int stat = 0;
	
	
			switch(stat_val){
			case 1:	stat = c.getAbility().getTotalStr(); break;
			case 2:	stat = c.getAbility().getTotalDex(); break;
			case 3:	stat = c.getAbility().getTotalCon(); break;
			case 4:	stat = c.getAbility().getTotalWis(); break;
			case 5:	stat = c.getAbility().getTotalInt(); break;
			case 6:	stat = c.getAbility().getTotalCha(); break;

			}
			
			dmg += (dmg * (stat * stat_valweight));
			
		}
		
		return dmg;
	}
	
	protected double calcEnchant(L1ItemInstance item, double pure_dmg){
		double dmg = pure_dmg;
		if(e_val > 0){
			int ech = item.getEnchantLevel();
			if(ech > e_val){
				ech -= e_val + 1;
				dmg += (dmg * (ech * e_valweight));
			}
		}
		return dmg;
	}
	
	protected double calcAttr(L1Character defender, double pure_dmg){
		double dmg = pure_dmg;
		if(defender.getResistance() != null){
			switch(attr_type){
			case L1Skills.ATTR_EARTH:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getEarth());
				break;
			case L1Skills.ATTR_FIRE:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getFire());
				break;
			case L1Skills.ATTR_WATER:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getWater());
				break;
			case L1Skills.ATTR_WIND:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getWind());
				break;
			}
		}
		
		return dmg;
	}
	
	protected void broadcast(L1Character c, ServerBasePacket pck){
		c.sendPackets(pck, false);
		c.broadcastPacket(pck);
	}
	
	protected void sendPackets(L1Character c, ServerBasePacket pck){
		c.sendPackets(pck, false);
		c.broadcastPacket(pck);
	}
	
	protected boolean isCounterMagic(L1Character c){
		if(c.hasSkillEffect(L1SkillId.COUNTER_MAGIC)){
			c.removeSkillEffect(L1SkillId.COUNTER_MAGIC);
			broadcast(c, new S_SkillSound(c.getId(), 10702));
			return true;
		}
		return false;
	}
	
	public boolean isAttack(){
		return true;
	}
}
