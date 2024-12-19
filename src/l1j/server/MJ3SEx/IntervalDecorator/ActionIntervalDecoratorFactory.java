package l1j.server.MJ3SEx.IntervalDecorator;

import l1j.server.Config;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class ActionIntervalDecoratorFactory {
	public static final double HASTE_RETARDATION = 1.33D;
	public static final double BRAVE_RETARDATION = 1.33D;
	public static final double THIRD_RETARDATION = 1.13D;
	public static final double HASTE_ACCELERATION = 1D / HASTE_RETARDATION;
	public static final double BRAVE_ACCELERATION = 1D / BRAVE_RETARDATION;
	public static final double THIRD_ACCELERATION = 1D / THIRD_RETARDATION;
	
	public static final ActionIntervalDecorator<L1PcInstance> PC_WALK_DECORATOR = new ActionIntervalDecorator<L1PcInstance>(){
		@Override
		public double decoration(L1PcInstance owner, double interval) {
			if(owner.isHaste())
				interval *= HASTE_ACCELERATION;
			else if(owner.hasSkillEffect(L1SkillId.SLOW))
				interval *= HASTE_RETARDATION;
			if(owner.isBrave() || owner.isBlood_lust() || owner.isElfBraveMagicShort() || owner.isElfBraveMagicLong() || 
					owner.isElfBrave() || owner.isFastMovable() || owner.hasSkillEffect(L1SkillId.STATUS_FRUIT) || owner.hasSkillEffect(L1SkillId.FOCUS_WAVE))
				interval *= BRAVE_ACCELERATION;
			if(owner.isDragonPearl())
				interval *= THIRD_ACCELERATION;
			
			if(Config.USE_ACTION_TIME_LOGGING){
				System.out.println(String.format("이동속도 : %.2fms", interval));
			}
			
			return interval;
		}
	};
	
	public static final ActionIntervalDecorator<L1PcInstance> PC_ATT_DECORATOR = new ActionIntervalDecorator<L1PcInstance>(){
		@Override
		public double decoration(L1PcInstance owner, double interval) {
			if(owner.isHaste())
				interval *= HASTE_ACCELERATION;
			else if(owner.hasSkillEffect(L1SkillId.SLOW))
				interval *= HASTE_RETARDATION;
			if(owner.hasSkillEffect(L1SkillId.FOCUS_WAVE)){
				for(int i=owner.get_focus_wave_level() - 1; i>=0; --i)
					interval *= THIRD_ACCELERATION;
			} else if (owner.hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
				if (owner.isPassive(MJPassiveID.DARK_HORSE.toInt())) {
					interval *= THIRD_ACCELERATION;
				}
			}
			else if(owner.isBrave() || owner.isBlood_lust() || owner.isElfBraveMagicShort() || owner.isElfBraveMagicLong())
				interval *= BRAVE_ACCELERATION;
			else if(owner.isElfBrave())
				interval *= THIRD_ACCELERATION;
			if(owner.isDragonPearl())
				interval *= THIRD_ACCELERATION;
			if(owner.hasSkillEffect(L1SkillId.WIND_SHACKLE))
				interval *= HASTE_RETARDATION;
			
			if(Config.USE_ACTION_TIME_LOGGING){
				System.out.println(String.format("공격속도 : %.2fms", interval));
			}

			return interval;	
		}
	};
	
	public static final ActionIntervalDecorator<L1Character> CHA_WALK_DECORATOR = new ActionIntervalDecorator<L1Character>(){
		@Override
		public double decoration(L1Character owner, double interval) {
			if(owner instanceof MJCompanionInstance){
				interval -= (interval * ((MJCompanionInstance) owner).get_movedelay_reduce() * 0.01);
			}else if(owner instanceof MJDogFightInstance){
				interval -= (interval * ((MJDogFightInstance) owner).get_movedelay_reduce() * 0.01);
			}else{
				if(owner.isHaste())
					interval *= HASTE_ACCELERATION;
				else if(owner.hasSkillEffect(L1SkillId.SLOW))
					interval *= HASTE_RETARDATION;
				if(owner.getBraveSpeed() == 1)
					interval *= BRAVE_ACCELERATION;
			}
			return interval;
		}
	};
	
	public static final ActionIntervalDecorator<L1Character> CHA_ATT_DECORATOR = new ActionIntervalDecorator<L1Character>(){
		@Override
		public double decoration(L1Character owner, double interval) {
			if(owner instanceof MJCompanionInstance){
				interval -= (interval * ((MJCompanionInstance) owner).get_attackdelay_reduce() * 0.01);
			}else if(owner instanceof MJDogFightInstance){
				interval -= (interval * ((MJDogFightInstance) owner).get_attackdelay_reduce() * 0.01);
			}else{
				if(owner.isHaste())
					interval *= HASTE_ACCELERATION;
				else if(owner.hasSkillEffect(L1SkillId.SLOW))
					interval *= HASTE_RETARDATION;
				if(owner.getBraveSpeed() == 1)
					interval *= BRAVE_ACCELERATION;
				if(owner.hasSkillEffect(L1SkillId.WIND_SHACKLE))
					interval *= HASTE_RETARDATION;
			}
			return interval;	
		}
	};
	
	public static final ActionIntervalDecorator<L1Character> NULL_DECORATOR = new ActionIntervalDecorator<L1Character>(){
		@Override
		public double decoration(L1Character owner, double interval) {
			return interval;	
		}
	};
}
