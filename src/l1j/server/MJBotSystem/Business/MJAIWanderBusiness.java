package l1j.server.MJBotSystem.Business;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

public class MJAIWanderBusiness extends MJAIBusiness{
	
	public MJAIWanderBusiness(int capacity) {
		super(capacity);
	}
	
	@Override
	protected void toWork(MJBotMovableAI ai, long time){
		L1PcInstance body 	= ai.getBody();
		MJBotStatus status 	= ai.getStatus();		
		
		if(MJCommons.isLock(body)){
			long interval = ai.getBrain().getDecisionTime();
			ai.setActSleepMs(time + interval);
			super.toWork(ai, time);
			return;
		}
		
		if(body.isDead()){
			ai.death();
			long interval = body.getCurrentSpriteInterval(EActionCodes.death);
			ai.setActSleepMs(time + interval);
			super.toWork(ai, time);
			return;
		}
		
		if (Config.STANDBY_SERVER)
			ai.setStatus(MJBotStatus.WALK);
	
		if(MJBotStatus.ATTACK == status){
			if(ai.isEmptyTarget())
				ai.setStatus(MJBotStatus.WALK);
		}else if(ai.getStatus() != MJBotStatus.DEAD && body.isDead())
			ai.setStatus(MJBotStatus.DEAD);
		
		ai.healingPotion(false, time);
		if (!Config.STANDBY_SERVER){
			ai.poly();
		}
		
		status 			= ai.getStatus();
		long interval 	= 0;
		if(status == MJBotStatus.SETTING){
			if(!Config.STANDBY_SERVER)
				ai.buff(time);
			ai.setting(time);
			interval = ai.getBrain().getDecisionTime() + ai.getBrain().toRand(MJBotLoadManager.MBO_WANDER_IDLETIME);
		}else if(status == MJBotStatus.WALK){
			body.updateObject();
			ai.walk(time);
			if(ai.getStatus() == MJBotStatus.SETTING)
				interval = ai.getBrain().getDecisionTime() + (ai.getBrain().toRand(MJBotLoadManager.MBO_WANDER_IDLETIME)*2);
			else
				interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else if(status == MJBotStatus.ATTACK){
			ai.doping();
			ai.attack(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon() + 1));
		}else if(status == MJBotStatus.DEAD){
			ai.death();
			interval = body.getCurrentSpriteInterval(EActionCodes.death);
		}else if(status == MJBotStatus.PICKUP){
			ai.pickUp();
			interval = body.getCurrentSpriteInterval(EActionCodes.get);
		}else if(status == MJBotStatus.SEARCHMOVE){
			ai.searchWalk(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else{
			interval = ai.getBrain().getDecisionTime() + ai.getBrain().toRand(MJBotLoadManager.MBO_WANDER_IDLETIME);
		}
		ai.setActSleepMs(time + interval);
		super.toWork(ai, time);
	}
}
