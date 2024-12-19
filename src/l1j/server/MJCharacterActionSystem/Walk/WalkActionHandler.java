package l1j.server.MJCharacterActionSystem.Walk;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_MOVE;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJINNSystem.MJINNRoom;
import l1j.server.MJINNSystem.Loader.MJINNMapInfoLoader;
import l1j.server.MJTemplate.Chain.Action.MJWalkFilterChain;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.Controller.CrockController;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.DungeonRandom;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.MJCommons;
import l1j.server.swing.chocco;

public class WalkActionHandler extends AbstractActionHandler implements MJPacketParser{
	protected int x;
	protected int y;
	protected int h;
	protected int nextX;
	protected int nextY;
	
	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		x = pck.readH();
		y = pck.readH();
		h = pck.readC() % 8;
		nextX = x + MJCommons.HEADING_TABLE_X[h];
		nextY = y + MJCommons.HEADING_TABLE_Y[h];
	}

	@Override
	public void doWork() {
		owner.killSkillEffectTimer(L1SkillId.MEDITATION);
		owner.setCallClanId(0);
		if(!owner.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
			owner.setRegenState(REGENSTATE_MOVE);
		//handle();
		register();
	}

	@Override
	public void handle() {
		if(!validation())
			return;
		
		if(owner.isDead())
			return;
		
		try{
			owner.setHeading(h);
			owner.getLocation().set(nextX, nextY);
			owner.setLastMoveActionMillis(System.currentTimeMillis());
			owner.broadcastPacket(new S_MoveCharPacket(owner));
			int ztype = owner.getZoneType();
			if(ztype == 0){
				if(owner.getSafetyZone()){
					owner.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
					owner.setSafetyZone(false);
				}
				owner.stopEinhasadTimer();
			}else{
				if(ztype == 1)
					owner.startEinhasadTimer();
				else 
					owner.stopEinhasadTimer();
				if(!owner.getSafetyZone()){
					owner.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
					owner.setSafetyZone(true);	
				}	
			}
			
			MJCastleWarBusiness.move(owner);
			if(L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_PLAYING){
				if(L1HauntedHouse.getInstance().isMember(owner) && nextX >= 32872 && nextX <= 32875 && nextY >= 32828 && nextY <= 32833)
					L1HauntedHouse.getInstance().endHauntedHouse(owner);
			}

			L1WorldTraps.getInstance().onPlayerMoved(owner);
			owner.getMap().setPassable(nextX, nextY, false);
			//TODO 파티 프로토
			L1Party party = owner.getParty();
			if(party != null){
				party.refreshPartyMemberStatus(owner);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean validation() {
		if(h < 0 || h > 7)
			return false;
		
		if (owner == null) {
			return false;
		}
		
		if(chocco.cpu < 70){
			if(Config.WALK_POSITION_CHECK){
				int maxDiff = Math.max(Math.abs(owner.getX() - x), Math.abs(owner.getY() - y));
				if(maxDiff > 0){
					long lastMillis = owner.getLastMoveActionMillis();
					if(lastMillis > 0){
						long currentMillis = System.currentTimeMillis();
						long interval = getInterval();
						// currentMillis - lastMillis => 지연시간 
						// interval * maxDiff => 최소 지연시간

						if((currentMillis - lastMillis) < interval * maxDiff){
							//System.out.println(currentMillis + " " + lastMillis + " " + (currentMillis - lastMillis));
							//System.out.println(interval + " " + maxDiff + " " + (interval * maxDiff));
							owner.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, owner));
							return false;
						}
					}
				}
				
				/*if(owner.getX() != x || owner.getY() != y){
					owner.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, owner));
					return false;
				}*/
			}
			/*
			if(owner.getLocation().getTileLineDistance(nextX, nextY) > Config.WALK_DISTANCE){
				owner.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, owner));
				return false;
			}*/
		}
		
		if(MJCommons.isLock(owner)){
			owner.start_teleport(x, y, owner.getMapId(), h, 169, false, false);
			return false;
		}
		owner.offFishing();
		owner.getMap().setPassable(x, y, true);
		int mid = owner.getMapId();
		try{
			int inn = MJINNMapInfoLoader.isInInnArea(nextX, nextY, mid);
			if(inn != -1){
				if(MJINNRoom.input(owner, inn))
					return false;
			}
			
			short mapid = MJCopyMapObservable.getInstance().idenMap((short)mid);
			if (Dungeon.getInstance().dg(nextX, nextY, mapid, owner)) // 지하 감옥 텔레포트
				return false;
			if (DungeonRandom.getInstance().dg(nextX, nextY, mapid, owner)) // 랜덤 텔레포트 지점
				return false;
			
			if(MJWalkFilterChain.getInstance().handle(owner, nextX, nextY))
				return false;
			
			if(CrockController.getInstance().isMove()){
				int[] loc = CrockController.getInstance().loc();
				if(loc[0] == x && loc[1] == y && loc[2] == mid){
					if(CrockController.getInstance().crocktype() == 0)
						owner.start_teleport(32639, 32876, 780, 4, 169, false, false);
					else
						owner.start_teleport(32794, 32751, 783, 4, 169, false, false);
					return false;
				}
			}
			
			/*long findSize = L1World.getInstance().findVisibleObjectFromPositionCount(x, y, mid, h, new MJFindObjectFilter(){
				@Override
				public boolean isFilter(L1Object obj) {
					if(!obj.instanceOf(MJL1Type.L1TYPE_PC))
						return true;
					
					L1PcInstance pc = (L1PcInstance)obj;
					if(pc.isDead() || (pc.isGm() && pc.isGmInvis()))
						return true;
					return false;
				}
			});*/
			if(!owner.isGm()){
			if(/*findSize > 0 ||*/ !owner.getMap().isUserPassable(x, y, h)){
				owner.start_teleport(x, y, mid, h, 169, false, false);
				return false;
			}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_WALK;
	}
	//TODO 1000L 추가함
	@Override
	public long getInterval() {
		return owner == null ? 1000L : owner.getCurrentSpriteInterval(EActionCodes.fromInt(owner.getCurrentWeapon()));
	}
}
