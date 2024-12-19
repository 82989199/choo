package l1j.server.MJBotSystem.Business;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJBotSystem.MJBotClanInfo;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotName;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.Loader.MJBotClanInfoLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

public class MJAISiegeLeaderBusiness extends MJAIBusiness {
	
	public MJAISiegeLeaderBusiness(int capacity) {
		super(capacity);
	}
	
	protected void toDefenseWork(MJBotMovableAI ai, long time){
		int 			castleId			= ai.getWarCastle();
		L1PcInstance 	body 			= ai.getBody();
		MJBotStatus 	status 		= ai.getStatus();

		if(MJCommons.isLock(body)){
			long interval = ai.getBrain().getDecisionTime();
			ai.setActSleepMs(time + interval);
			return;
		}
		
		if(status != MJBotStatus.SETTING && !MJBotUtil.isInCastle(body, castleId) && !MJBotUtil.isInCastleStartup(body, castleId)){
			status = MJBotStatus.SETTING;
			ai.setStatus(status);
		}
		
		if(MJBotStatus.DEAD != status && body.isDead())
			ai.setStatus(MJBotStatus.DEAD);
		
		ai.healingPotion(false, time);
		ai.doping();
		ai.poly();
		ai.summonDoll();
		
		status			= ai.getStatus();
		long interval 	= 0;
		if(status == MJBotStatus.SETTING){
			ai.settingDefense(time);
			interval 	= ai.getBrain().getDecisionTime();
		}else if(status == MJBotStatus.WALK){
			ai.walkDefense(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon()));
		}else if(status == MJBotStatus.ATTACK){
			ai.attackDefense(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.fromInt(body.getCurrentWeapon() + 1));
		}else if(status == MJBotStatus.DEAD){
			ai.death();
			interval = body.getCurrentSpriteInterval(EActionCodes.death);
		}else{
			ai.setStatus(MJBotStatus.WALK);
			interval = ai.getBrain().getDecisionTime();
		}
		ai.setActSleepMs(time + interval);
	}
	
	private static final int[] _castles = new int[]{
		L1CastleLocation.KENT_CASTLE_ID, L1CastleLocation.OT_CASTLE_ID, L1CastleLocation.GIRAN_CASTLE_ID
	};
	@Override
	protected void toWork(MJBotMovableAI ai, long time){
		int 			castleId	= ai.getWarCastle();
		L1PcInstance 	body 		= ai.getBody();
		MJBotStatus 	status 		= ai.getStatus();
		if(body.isDead()){
			ai.death();
			long 			interval = body.getCurrentSpriteInterval(EActionCodes.death);
			ai.setActSleepMs(time + interval);
			super.toWork(ai, time);
			return;
		}
		
		if(!MJBotLoadManager.MBO_IS_CASTLE_WAR){	
			ai.setActSleepMs(time + 10000);
			super.toWork(ai, time);
			return;
		}
		
		if(castleId == -1){
			for(int i : _castles){
				if(MJCastleWarBusiness.getInstance().isNowWar(i)){
					ai.setWarCastle(i);
					castleId = i;
					break;
				}
			}
		}
		
		if(castleId == -1){	
			ai.setActSleepMs(time + 10000);
			super.toWork(ai, time);
			return;
		}

		if(!MJCastleWarBusiness.getInstance().isNowWar(castleId)){
			if(MJBotUtil.isInCastle(body, castleId) || MJBotUtil.isInCastleStartup(body, castleId)){
				MJBotName bName	= MJBotNameLoader.getInstance().get();
				MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(bName.cName);
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
				ai.teleportcrown(cInfo.leaderX, cInfo.leaderY, (short) 4);
				//ai.teleport(loc.x, loc.y, (short)loc.map);
				super.toWork(ai, time);
				return;
			}
		}
		if(body.getClan().getCastleId() == castleId){
			toDefenseWork(ai, time);
			super.toWork(ai, time);
			return;
		}
		updateWarInfo(ai, castleId);
		if(MJCommons.isLock(body)){
			long interval = ai.getBrain().getDecisionTime();
			ai.setActSleepMs(time + interval);
			super.toWork(ai, time);
			return;
		}
		
		if(status != MJBotStatus.SETTING){
			if(!MJBotUtil.isInCastle(body, castleId) && !MJBotUtil.isInCastleStartup(body, castleId)){
				status = MJBotStatus.SETTING;
				ai.setStatus(status);
			}else if(castleId == 4){
				int x = body.getX();
				int y = body.getY();
				if(MJCommons.isInArea(body, 33612, 32735, 33619, 32726, 4) || MJCommons.isInArea(body, 33645, 32726, 33651, 32737, 4)){
					status = MJBotStatus.SETTING;
					ai.setStatus(status);
				}
			}
		}
		
		if(MJBotStatus.ATTACK == status){
			if(ai.isEmptyTarget())
				ai.setStatus(MJBotStatus.WALK);
		}else if(MJBotStatus.DEAD != status && body.isDead())
			ai.setStatus(MJBotStatus.DEAD);
		
		ai.healingPotion(false, time);
		ai.doping();
		ai.poly();
		ai.summonDoll();

		status			= ai.getStatus();
		long interval 	= 0;
		if(status == MJBotStatus.SETTING){
			ai.setting(time);
			interval 	= ai.getBrain().getDecisionTime();
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
		}else{
			ai.setStatus(MJBotStatus.WALK);
			interval = ai.getBrain().getDecisionTime();
		}
		ai.setActSleepMs(time + interval);
		super.toWork(ai, time);
	}
	
	private void updateWarInfo(MJBotMovableAI ai, int castleId){
		L1PcInstance 	body 	= ai.getBody();	
		if(!MJCastleWarBusiness.getInstance().isNowWar(castleId)){
			ai.setWarCastle(-1);
			ai.setWar(null);
			return;
		}
		
		MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
		MJCastleWar currentWar = (MJCastleWar)ai.getWar();
		if(currentWar == null || !war.equals(currentWar) || !MJCastleWar.isOffenseClan(body.getClan())){
			war.proclaim(body.getClan());
			ai.setWar(war);
		}
	}
}
