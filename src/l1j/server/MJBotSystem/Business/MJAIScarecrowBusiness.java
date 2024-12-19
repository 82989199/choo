package l1j.server.MJBotSystem.Business;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ ai scarecrow attack business logic realize.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJAIScarecrowBusiness extends MJAIBusiness{
	public MJAIScarecrowBusiness(int capacity) {
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
			long 			interval = body.getCurrentSpriteInterval(EActionCodes.death);
			ai.setActSleepMs(time + interval);
			super.toWork(ai, time);
			return;
		}
		
		if(MJBotStatus.WALK == status || MJBotStatus.SEARCHMOVE == status){
			if(!ai.isEmptyTarget())	
				ai.setStatus(MJBotStatus.ATTACK);
		}else if(MJBotStatus.ATTACK == status){
			if(ai.isEmptyTarget())
				ai.setStatus(MJBotStatus.WALK);
		}else if(ai.getStatus() != MJBotStatus.DEAD && body.isDead())
			ai.setStatus(MJBotStatus.DEAD);
		
		ai.healingPotion(false, time);
		ai.poly();
		
		status 			= ai.getStatus();
		long interval 	= 0;
		if(ai.getBrain().toBoolean() && ai.buff(time)){
			interval = body.getCurrentSpriteInterval(EActionCodes.spell_nodir);
		}else if(status == MJBotStatus.SETTING){
			ai.setting(time);
			interval = ai.getBrain().getDecisionTime();
		}else if(status == MJBotStatus.WALK){
			body.updateObject();
			ai.walk(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else if(status == MJBotStatus.ATTACK){
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
			MJBotUtil.sendBotIdleMent(ai, null);
			interval = ai.getBrain().getDecisionTime();
		}
		ai.setActSleepMs(time + interval);
		super.toWork(ai, time);
	}
}
