package l1j.server.MJBotSystem.Business;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ ai hunt business logic realize.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJAIHuntBusiness extends MJAIBusiness{

	public MJAIHuntBusiness(int capacity) {
		super(capacity);
	}
	
	protected void toDefenseWork(MJBotMovableAI ai, long time){
		int 			castleId	= ai.getWarCastle();
		L1PcInstance 	body 		= ai.getBody();
		MJBotStatus 	status 		= ai.getStatus();
		
		if(status != MJBotStatus.SETTING && !MJBotUtil.isInCastle(body, castleId) && !MJBotUtil.isInCastleStartup(body, castleId)){
			status = MJBotStatus.SETTING;
			ai.setStatus(status);
		}
		
		if(ai.getStatus() != MJBotStatus.DEAD && body.isDead())
			ai.setStatus(MJBotStatus.DEAD);
		
		ai.healingPotion(false, time);
		ai.doping();
		ai.poly();
		ai.summonDoll();
		
		status 			= ai.getStatus();
		long interval 	= 0;
		if(ai.getBrain().toBoolean() && ai.buff(time)){
			interval = body.getCurrentSpriteInterval(EActionCodes.spell_nodir);
		}else if(status == MJBotStatus.SETTING){
			ai.settingDefense(time);
			interval = ai.getBrain().getDecisionTime();
		}else if(status == MJBotStatus.WALK){
			ai.walkDefense(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else if(status == MJBotStatus.ATTACK){
			ai.attackDefense(time);
			if(ai.getLastStatus() == MJBotStatus.ATTACK)
				interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon() + 1));
			else
				interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else if(status == MJBotStatus.DEAD){
			ai.death();
			interval = body.getCurrentSpriteInterval(EActionCodes.death);
		}else if(status == MJBotStatus.PICKUP){
			ai.pickUp();
			interval = body.getCurrentSpriteInterval(EActionCodes.get);
		}else if(status == MJBotStatus.SEARCHMOVE){
			ai.searchWalkDefense(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else{
			MJBotUtil.sendBotIdleMent(ai, null);
			ai.setStatus(MJBotStatus.WALK);
			interval = ai.getBrain().getDecisionTime();
		}
		ai.setActSleepMs(time + interval);
	}
	
	protected void toOffenseWork(MJBotMovableAI ai, long time){
		int 			castleId	= ai.getWarCastle();
		L1PcInstance 	body 		= ai.getBody();
		MJBotStatus 	status 		= ai.getStatus();
		
		if(status != MJBotStatus.SETTING){
			if(!MJBotUtil.isInCastle(body, castleId) && !MJBotUtil.isInCastleStartup(body, castleId)){
				status = MJBotStatus.SETTING;
				ai.setStatus(status);
			}else if(castleId == 4){
				if(MJCommons.isInArea(body, 33612, 32735, 33619, 32726, 4) || MJCommons.isInArea(body, 33645, 32726, 33651, 32737, 4)){
					status = MJBotStatus.SETTING;
					ai.setStatus(status);
				}
			}
		}
		
		if(ai.getStatus() != MJBotStatus.DEAD && body.isDead())
			ai.setStatus(MJBotStatus.DEAD);
		
		ai.healingPotion(false, time);
		ai.doping();
		ai.poly();
		ai.summonDoll();
		
		status 			= ai.getStatus();
		long interval 	= 0;
		if(ai.getBrain().toBoolean() && ai.buff(time)){
			interval = body.getCurrentSpriteInterval(EActionCodes.spell_nodir);
		}else if(status == MJBotStatus.SETTING){
			ai.settingOffense(time);
			interval = ai.getBrain().getDecisionTime();
		}else if(status == MJBotStatus.WALK){
			ai.walkOffense(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else if(status == MJBotStatus.ATTACK){
			ai.attackOffense(time);
			if(ai.getLastStatus() == MJBotStatus.ATTACK)
				interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon() + 1));
			else
				interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon())) + 50L;
		}else if(status == MJBotStatus.DEAD){
			ai.death();
			interval = body.getCurrentSpriteInterval(EActionCodes.death);
		}else if(status == MJBotStatus.PICKUP){
			ai.pickUp();
			interval = body.getCurrentSpriteInterval(EActionCodes.get);
		}else if(status == MJBotStatus.SEARCHMOVE){
			ai.searchWalkOffense(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else{
			MJBotUtil.sendBotIdleMent(ai, null);
			ai.setStatus(MJBotStatus.WALK);
			interval = ai.getBrain().getDecisionTime();
		}
		ai.setActSleepMs(time + interval);
	}
	
	@Override
	protected void toWork(MJBotMovableAI ai, long time){
		try{
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
			
			if(MJBotLoadManager.MBO_IS_CASTLE_WAR){
				if(ai.getWarCastle() == -1){
					L1Clan clan = body.getClan(); 
					if(clan != null)
						ai.setWarCastle(clan.getInCastleId());
				}		
				
				if(ai.getWarCastle() != -1){
					if(!MJCastleWarBusiness.getInstance().isNowWar(ai.getWarCastle())){
						ai.setWarCastle(-1);
						ai.setWar(null);
						ai.setActSleepMs(time + 1000);
						ai.undoPoly();
						ai.setStatus(MJBotStatus.SETTING);
						body.getMap().setPassable(body.getLocation(), true);
						MJBotLocation loc = MJBotUtil.createRandomLocation(
								MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
								MJBotLoadManager.MBO_WANDER_MAT_TOP, 
								MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
								MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
								MJBotLoadManager.MBO_WANDER_MAT_MAPID
							);
						ai.teleport(loc.x, loc.y, (short)loc.map);
					}else if(body.getClan().getCastleId() == ai.getWarCastle())
						toDefenseWork(ai, time);
					else
						toOffenseWork(ai, time);
					super.toWork(ai, time);
					return;
				}
			}
			
			if(ai.getStatus() != MJBotStatus.SETTING && body.getMap().isSafetyZone(body.getLocation())){
				status = MJBotStatus.SETTING;
				ai.setStatus(status);
			}
			
			if(MJBotStatus.WALK == status || MJBotStatus.SEARCHMOVE == status){
				if(!ai.isEmptyTarget())	
					ai.setStatus(MJBotStatus.ATTACK);
				else{
					if(ai.getBrain().toRand(100) <= 5)
						ai.setStatus(MJBotStatus.CORPSE);
				}
			}else if(MJBotStatus.ATTACK == status){
				if(ai.isEmptyTarget())
					ai.setStatus(MJBotStatus.WALK);
			}else if(ai.getStatus() != MJBotStatus.DEAD && body.isDead())
				ai.setStatus(MJBotStatus.DEAD);
			
			ai.healingPotion(false, time);
			ai.doping();
			ai.poly();
			ai.summonDoll();
			
			status 			= ai.getStatus();
			long interval 	= 0;
			if(ai.getBrain().toBoolean() && ai.buff(time)){
				interval = body.getCurrentSpriteInterval(EActionCodes.spell_nodir);
			}else if(status == MJBotStatus.SETTING){
				ai.setting(time);
				interval = ai.getBrain().getDecisionTime();
			}else if(status == MJBotStatus.WALK){
				ai.walk(time);
				interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
			}else if(status == MJBotStatus.ATTACK){
				ai.attack(time);
				if(ai.getLastStatus() == MJBotStatus.ATTACK)
					interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon() + 1));
				else
					interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
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
				ai.setStatus(MJBotStatus.WALK);
				interval = ai.getBrain().getDecisionTime();
			}
			ai.setActSleepMs(time + interval);
			super.toWork(ai, time);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
