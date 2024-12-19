package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;

import java.util.HashSet;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.Chain.Action.MJTeleportChain;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.ActionCodes;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1SupportInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DollPack;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.S_SupportPack;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.utils.CommonUtil;

public class L1Teleport {
	
	private static L1Teleport instance;
	public static L1Teleport getInstance() {
		if (instance == null) {
			instance = new L1Teleport();
		}
		return instance;
	}
//	public static final int TELEPORT = 0;
//	public static final int CHANGE_POSITION = 1;
//	public static final int ADVANCED_MASS_TELEPORT = 2;
//	public static final int CALL_CLAN = 3;
//	public static final int DUNGEON_TELEPORT = 4;
//	public static final int NODELAY_TELEPORT = 5;
//	// 차례로 teleport(흰색), change position e(파랑), ad mass teleport e(빨강), call clan(초록)
//	public static final int[] EFFECT_SPR =  { 169, 149, 169, 2281 };
//	public static final int[] EFFECT_TIME = { 280, 440, 440, 1120 };
/*	public static final int[] EFFECT_SPR = { 169, 2235, 2236, 2281, 169, 12261, 169 };
	public static final int[] EFFECT_TIME = { 280, 280, 280, 280, 280, 280, 600 };*/
//	private L1Teleport() {}
//	public static void teleport(L1PcInstance pc, L1Location loc, int head, boolean effectable) {
//		teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), head, effectable, TELEPORT);
//	}
//
//	public static void teleport(L1PcInstance pc, L1Location loc, int head, boolean effectable, int skillType) {
//		teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), head, effectable, skillType);
//	}
//
//	public static void teleport(L1PcInstance pc, int x, int y, short mapid, int head, boolean effectable) {
//		teleport(pc, x, y, mapid, head, effectable, TELEPORT);
//	}
	 
	public void doTeleport(L1PcInstance pc, int x, int y, int mapid){
		int old_mapid = pc.getMapId();
		L1Map map = L1WorldMap.getInstance().getMap((short) mapid);

		
		if(MJTeleportChain.getInstance().is_teleport(pc, x, y, mapid))
			return;
		
		if (!map.isInMap(x, y) && !pc.isGm()) {
			x = pc.getX();
			y = pc.getY();
			mapid = pc.getMapId();
		}

		pc.getMap().setPassable(pc.getLocation(), true);
		L1World.getInstance().moveVisibleObject(pc, mapid);
		pc.setLocation(x, y, mapid);
		pc.setHeading(pc.getHeading());
		
		pc.sendPackets(new S_MapID(pc.getMap().getBaseMapId(), pc.getMap().isUnderwater()));
		MJExpAmplifierLoader.getInstance().set(pc);
		pc.getMap().setPassable(pc.getLocation(), false);
		
		if (pc.getZoneType() == 0) {			
			if(pc.getSafetyZone() == true) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
				pc.setSafetyZone(false);	
			}
		} else {			
			if (pc.getSafetyZone() == false) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
				pc.setSafetyZone(true);	
			}
		}
		
		if (pc.isReserveGhost()) {
			pc.endGhost();
		}
		
		if (pc.getMapId() != 631) {
			pc.broadcastPacket(S_WorldPutObject.get(pc));
			//pc.broadcastPacket(new S_OtherCharPacks(pc));
		}
		if(pc.getMapId() == 5490) {
			pc.setFishingTime(0);
			pc.setFishingReady(false);
			pc.setFishing(false);
			pc.sendPackets(new S_CharVisualUpdate(pc));
			pc.broadcastPacket(new S_CharVisualUpdate(pc));
			FishingTimeController.getInstance().removeMember(pc);
		}
		pc.broadcastRemoveAllKnownObjects();
		pc.removeAllKnownObjects();
		pc.sendPackets(S_WorldPutObject.put(pc));
		pc.updateObject();
		if(pc.isInParty()){
			//TODO 파티 프로토
			pc.getParty().refreshPartyMemberStatus(pc);
		}
		pc.sendVisualEffectAtTeleport();
		pc.sendPackets(new S_CharVisualUpdate(pc));

		pc.killSkillEffectTimer(L1SkillId.MEDITATION);
		pc.setCallClanId(0);
		HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
		subjects.add(pc);		
		
		if (pc.isPinkName()) {
			pc.sendPackets(new S_PinkName(pc.getId(), pc.getPinkNameTime()));
		}

		if (pc.hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
			int reminingtime = pc.getSkillEffectTimeSec(STATUS_DRAGON_PEARL);			
			pc.sendPackets(new S_Liquor(pc.getId(), 8));
			pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, reminingtime/4));
			pc.setPearl(1);
		}
		//TODO 파티 프로토
		L1Party party = pc.getParty();
		for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(pc)) {
			if (target.isPinkName()) {
				pc.sendPackets(new S_PinkName(target.getId(), target.getPinkNameTime()));
			}
			//TODO 파티 프로토
			if(party != null && party.isMember(target)){
				party.refreshPartyMemberStatus(target);
			}
		}
		
		MJCastleWarBusiness.move(pc);
		
		if (pc.getMapId() == 781 || pc.getMapId() == 782) {
			// 애완동물을 월드 MAP상으로부터 지운다
			Object[] petList = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.dropItem();
					pc.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
					}
				}
			}
		} else if (!pc.isGhost() && pc.getMap().isTakePets() && pc.getMapId() != 5153 && pc.getMapId() != 5140) {
			for (L1NpcInstance petNpc : pc.getPetList().values()) {
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();
				if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
					nx = 32799 + CommonUtil.random(5) - 3;
					ny = 32864 + CommonUtil.random(5) - 3;
				}
				teleport(petNpc, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				if (petNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) petNpc;
					pc.sendPackets(new S_SummonPack(summon, pc));
				} else if (petNpc instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) petNpc;
					SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(pet);
					pc.broadcastPacket(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
					//pc.sendPackets(new S_PetPack(pet, pc));
				}

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(petNpc)) {
					visiblePc.removeKnownObject(petNpc);
					subjects.add(visiblePc);
				}

			}

			for (L1DollInstance doll : pc.getDollList().values()) {
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();
				teleport(doll, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				pc.sendPackets(new S_DollPack(doll, pc));

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(doll)) {
					visiblePc.removeKnownObject(doll);
					subjects.add(visiblePc);
				}

			}
			for (L1SupportInstance support : pc.getSupportList().values()) {

				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();

				//teleport(support, nx, ny, mapId, head);
				teleport(support, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				pc.sendPackets(new S_SupportPack(support, pc));

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(support)) {
					visiblePc.removeKnownObject(support);
					subjects.add(visiblePc);
				}
			}			
		} else {			
			// 애완동물을 월드 MAP상으로부터 지운다
			Object[] petList = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.dropItem();
					pc.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
					}
				}
			}

			// 마법인형을 월드 맵상으로부터 지운다
			Object[] dollList = pc.getDollList().values().toArray();
			L1DollInstance doll = null;
			for (Object dollObject : dollList) {
				doll = (L1DollInstance) dollObject;
				doll.deleteDoll(false);
			}

			Object[] supportList = pc.getSupportList().values().toArray();
			L1SupportInstance support = null;
			for (Object supportObject : supportList) {
				support = (L1SupportInstance) supportObject;
				support.deleteSupport();
			}
		}

		for (L1PcInstance updatePc : subjects) {
			updatePc.updateObject();
		}
		
		/** 인스턴스 던전 아이템삭제 **/
		if (pc.getMap().getBaseMapId() != 1936 && pc.getMap().getBaseMapId() != 2600 && pc.getMap().getBaseMapId() != 2699) {
			removeItem(pc);
		}
		/** 인스턴스 던전 아이템삭제 **/
		
		if (pc.hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
			pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), pc.getSkillEffectTimeSec(L1SkillId.WIND_SHACKLE)));
		} else if (pc.hasSkillEffect(L1SkillId.DANCING_BLADES)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(L1SkillId.DANCING_BLADES)));
			pc.sendPackets(new S_SkillIconAura(154, pc.getSkillEffectTimeSec(L1SkillId.DANCING_BLADES)));
		} else if(pc.hasSkillEffect(L1SkillId.SAND_STORM)){
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(L1SkillId.SAND_STORM)));			
		} else if(pc.hasSkillEffect(L1SkillId.HURRICANE)){
			pc.sendPackets(new S_SkillBrave(pc.getId(), 9, pc.getSkillEffectTimeSec(L1SkillId.HURRICANE)));			
		} else if(pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)){
			pc.sendPackets(new S_SkillBrave(pc.getId(), 10, pc.getSkillEffectTimeSec(L1SkillId.FOCUS_WAVE)));			
		}
		
		get_teleport_check(pc, old_mapid, pc.getMapId());
		/** 2016.12.01 MJ 앱센터 LFC **/
		if(pc.getFindMerchantId() != 0){
			pc.sendPackets(new S_NPCTalkReturn(pc.getFindMerchantId(), "usershop"), true);
			pc.setFindMerchantId(0);
		}

		MJTeleportChain.getInstance().teleported(pc, x, y, mapid);
		if(pc.isDead())
			pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Die));
	}
	
	public void doTeleportationForGM(L1PcInstance pc) {
		
		if (pc == null) {
			return;
		}
		
		int old_mapid = pc.getMapId();
		int x = pc.get_teleport_x();
		int y = pc.get_teleport_y();
		int mapid = pc.get_teleport_map();
		
		if(MJTeleportChain.getInstance().is_teleport(pc, x, y, mapid)){
			pc.set_teleport(false);
			return;
		}
		
		L1Map map = L1WorldMap.getInstance().getMap((short) mapid);
		
		if (!map.isInMap(x, y) && !pc.isGm()) {
			x = pc.getX();
			y = pc.getY();
			mapid = pc.getMapId();
		}

		pc.getMap().setPassable(pc.getLocation(), true);
		L1World.getInstance().moveVisibleObject(pc, mapid);
		pc.setLocation(x, y, mapid);
		//pc.setHeading(pc.getMoveState().getHeading());
		pc.setHeading(pc.getHeading());
		
		pc.sendPackets(new S_MapID(pc.getMap().getBaseMapId(), pc.getMap().isUnderwater()));
		MJExpAmplifierLoader.getInstance().set(pc);
		pc.getMap().setPassable(pc.getLocation(), false);
		
		if (pc.getZoneType() == 0) {			
			if(pc.getSafetyZone() == true) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
				pc.setSafetyZone(false);	
			}
		} else {			
			if (pc.getSafetyZone() == false) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
				pc.setSafetyZone(true);	
			}
		}
		
		if (pc.isReserveGhost()) {
			pc.endGhost();
		}
		
		if (pc.getMapId() != 631) {
			pc.broadcastPacket(S_WorldPutObject.get(pc));
			//pc.broadcastPacket(new S_OtherCharPacks(pc));
		}
		if(pc.getMapId() == 5490) {
			pc.setFishingTime(0);
			pc.setFishingReady(false);
			pc.setFishing(false);
			pc.sendPackets(new S_CharVisualUpdate(pc));
			pc.broadcastPacket(new S_CharVisualUpdate(pc));
			FishingTimeController.getInstance().removeMember(pc);
		}
		pc.broadcastRemoveAllKnownObjects();
		pc.removeAllKnownObjects();
		pc.sendPackets(S_WorldPutObject.put(pc));
		//pc.sendPackets(new S_OwnCharPack(pc));
		pc.updateObject();
		if(pc.isInParty()){
			//TODO 파티 프로토
			pc.getParty().refreshPartyMemberStatus(pc);
		}
		pc.sendVisualEffectAtTeleport();
		pc.sendPackets(new S_CharVisualUpdate(pc));

		pc.killSkillEffectTimer(L1SkillId.MEDITATION);
		pc.setCallClanId(0);
		HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
		subjects.add(pc);		
		
		if (pc.isPinkName()) {
			pc.sendPackets(new S_PinkName(pc.getId(), pc.getPinkNameTime()));
		}

		if (pc.hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
			int reminingtime = pc.getSkillEffectTimeSec(STATUS_DRAGON_PEARL);			
			pc.sendPackets(new S_Liquor(pc.getId(), 8));
			pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, reminingtime/4));
			pc.setPearl(1);
		}
		//TODO 파티 프로토
		L1Party party = pc.getParty();
		for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(pc)) {
			if (target.isPinkName()) {
				pc.sendPackets(new S_PinkName(target.getId(), target.getPinkNameTime()));
			}
			//TODO 파티 프로토
			if(party != null && party.isMember(target)){
				party.refreshPartyMemberStatus(target);
			}
		}
		
		MJCastleWarBusiness.move(pc);
		
		if (pc.getMapId() == 781 || pc.getMapId() == 782) {
			// 애완동물을 월드 MAP상으로부터 지운다
			Object[] petList = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.dropItem();
					pc.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
					}
				}
			}
		} else if (!pc.isGhost() && pc.getMap().isTakePets() && pc.getMapId() != 5153 && pc.getMapId() != 5140) {
			for (L1NpcInstance petNpc : pc.getPetList().values()) {
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();
				if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
					nx = 32799 + CommonUtil.random(5) - 3;
					ny = 32864 + CommonUtil.random(5) - 3;
				}
				teleport(petNpc, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				if (petNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) petNpc;
					pc.sendPackets(new S_SummonPack(summon, pc));
				} else if (petNpc instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) petNpc;
					SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(pet);
					pc.broadcastPacket(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
					//pc.sendPackets(new S_PetPack(pet, pc));
				}

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(petNpc)) {
					visiblePc.removeKnownObject(petNpc);
					subjects.add(visiblePc);
				}

			}

			for (L1DollInstance doll : pc.getDollList().values()) {
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();
				//teleport(doll, nx, ny, mapId, head);
				teleport(doll, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				pc.sendPackets(new S_DollPack(doll, pc));

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(doll)) {
					visiblePc.removeKnownObject(doll);
					subjects.add(visiblePc);
				}

			}
			for (L1SupportInstance support : pc.getSupportList().values()) {

				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();

				//teleport(support, nx, ny, mapId, head);
				teleport(support, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				pc.sendPackets(new S_SupportPack(support, pc));

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(support)) {
					visiblePc.removeKnownObject(support);
					subjects.add(visiblePc);
				}
			}			
		} else {			
			// 애완동물을 월드 MAP상으로부터 지운다
			Object[] petList = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.dropItem();
					pc.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
					}
				}
			}

			// 마법인형을 월드 맵상으로부터 지운다
			Object[] dollList = pc.getDollList().values().toArray();
			L1DollInstance doll = null;
			for (Object dollObject : dollList) {
				doll = (L1DollInstance) dollObject;
				doll.deleteDoll(false);
			}

			Object[] supportList = pc.getSupportList().values().toArray();
			L1SupportInstance support = null;
			for (Object supportObject : supportList) {
				support = (L1SupportInstance) supportObject;
				support.deleteSupport();
			}
		}

		for (L1PcInstance updatePc : subjects) {
			updatePc.updateObject();
		}

		pc.set_teleport(false);
		
		/** 인스턴스 던전 아이템삭제 **/
		if (pc.getMap().getBaseMapId() != 1936 && pc.getMap().getBaseMapId() != 2600 && pc.getMap().getBaseMapId() != 2699) {
			removeItem(pc);
		}
		/** 인스턴스 던전 아이템삭제 **/
		
		if (pc.hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
			pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), pc.getSkillEffectTimeSec(L1SkillId.WIND_SHACKLE)));
		} else if (pc.hasSkillEffect(L1SkillId.DANCING_BLADES)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(L1SkillId.DANCING_BLADES)));
			pc.sendPackets(new S_SkillIconAura(154, pc.getSkillEffectTimeSec(L1SkillId.DANCING_BLADES)));
		} else if (pc.hasSkillEffect(L1SkillId.SAND_STORM)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(L1SkillId.SAND_STORM)));
		} else if(pc.hasSkillEffect(L1SkillId.HURRICANE)){
			pc.sendPackets(new S_SkillBrave(pc.getId(), 9, pc.getSkillEffectTimeSec(L1SkillId.HURRICANE)));			
		} else if(pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)){
			pc.sendPackets(new S_SkillBrave(pc.getId(), 10, pc.getSkillEffectTimeSec(L1SkillId.FOCUS_WAVE)));			
		}

		get_teleport_check(pc, old_mapid, pc.getMapId());
		MJTeleportChain.getInstance().teleported(pc, x, y, mapid);
		if(pc.isDead())
			pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Die));
	}
	
	public void doTeleportation(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		
		L1Clan clan = pc.getClan();
		if(clan != null){
			clan.deleteClanRetrieveUser(pc.getId());
		}
		
		/** 2016.11.26 MJ 앱센터 LFC **/
		if(pc.isDead()){
			if(!(MJRaidSpace.getInstance().isInInstance(pc) || MJInstanceSpace.isInInstance(pc))){
				pc.set_teleport(false);
				return;
			}
		}
		/** 2016.11.26 MJ 앱센터 LFC **/
			
		int old_mapid = pc.getMapId();
		int x = pc.get_teleport_x();
		int y = pc.get_teleport_y();
		int mapid = pc.get_teleport_map();
		
		if(MJTeleportChain.getInstance().is_teleport(pc, x, y, mapid)){
			pc.set_teleport(false);
			return;
		}
		
		L1Map map = L1WorldMap.getInstance().getMap((short) mapid);

		if (!map.isInMap(x, y) && !pc.isGm()) {
			x = pc.getX();
			y = pc.getY();
			mapid = pc.getMapId();
		}

		pc.getMap().setPassable(pc.getLocation(), true);
		L1World.getInstance().moveVisibleObject(pc, mapid);
		pc.setLocation(x, y, mapid);
		//pc.setHeading(pc.getMoveState().getHeading());
		pc.setHeading(pc.getHeading());
		
		pc.sendPackets(new S_MapID(pc.getMap().getBaseMapId(), pc.getMap().isUnderwater()));
		MJExpAmplifierLoader.getInstance().set(pc);
		pc.getMap().setPassable(pc.getLocation(), false);
		
		if (pc.getZoneType() == 0) {			
			if(pc.getSafetyZone() == true) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
				pc.setSafetyZone(false);	
			}
		} else {			
			if (pc.getSafetyZone() == false) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
				pc.setSafetyZone(true);	
			}
		}
		
		if (pc.isReserveGhost()) {
			pc.endGhost();
		}
		
		if (pc.getMapId() != 631) {
			pc.broadcastPacket(S_WorldPutObject.get(pc));
			//pc.broadcastPacket(new S_OtherCharPacks(pc));
		}
		if(pc.getMapId() == 5490) {
			pc.setFishingTime(0);
			pc.setFishingReady(false);
			pc.setFishing(false);
			pc.sendPackets(new S_CharVisualUpdate(pc));
			pc.broadcastPacket(new S_CharVisualUpdate(pc));
			FishingTimeController.getInstance().removeMember(pc);
		}
		pc.broadcastRemoveAllKnownObjects();
		pc.removeAllKnownObjects();
		pc.sendPackets(S_WorldPutObject.put(pc));
		//pc.sendPackets(new S_OwnCharPack(pc));
		pc.updateObject();
		if(pc.isInParty()){
			pc.getParty().refreshPartyMemberStatus(pc);
		}
		pc.sendVisualEffectAtTeleport();
		pc.sendPackets(new S_CharVisualUpdate(pc));

		pc.killSkillEffectTimer(L1SkillId.MEDITATION);
		pc.setCallClanId(0);
		HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
		subjects.add(pc);		
		
		if (pc.isPinkName()) {
			pc.sendPackets(new S_PinkName(pc.getId(), pc.getPinkNameTime()));
		}

		if (pc.hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL)) {
			int reminingtime = pc.getSkillEffectTimeSec(STATUS_DRAGON_PEARL);			
			pc.sendPackets(new S_Liquor(pc.getId(), 8));
			pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, reminingtime/4));
			pc.setPearl(1);
		}
		//TODO 파티 프로토
		L1Party party = pc.getParty();
		for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(pc)) {
			if (target.isPinkName()) {
				pc.sendPackets(new S_PinkName(target.getId(), target.getPinkNameTime()));
			}
			//TODO 파티 프로토
			if(party != null && party.isMember(target)){
				party.refreshPartyMemberStatus(target);
			}
		}
		
		MJCastleWarBusiness.move(pc);
		
		if (pc.getMapId() == 781 || pc.getMapId() == 782) {
			// 애완동물을 월드 MAP상으로부터 지운다
			Object[] petList = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.dropItem();
					pc.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
					}
				}
			}
		} else if (!pc.isGhost() && pc.getMap().isTakePets() && pc.getMapId() != 5153 && pc.getMapId() != 5140) {
			for (L1NpcInstance petNpc : pc.getPetList().values()) {
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();
				if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
					nx = 32799 + CommonUtil.random(5) - 3;
					ny = 32864 + CommonUtil.random(5) - 3;
				}
				teleport(petNpc, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				if (petNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) petNpc;
					pc.sendPackets(new S_SummonPack(summon, pc));
				} else if (petNpc instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) petNpc;
					SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(pet);
					pc.broadcastPacket(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
					//pc.sendPackets(new S_PetPack(pet, pc));
				}

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(petNpc)) {
					visiblePc.removeKnownObject(petNpc);
					subjects.add(visiblePc);
				}

			}

			for (L1DollInstance doll : pc.getDollList().values()) {
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();
				//teleport(doll, nx, ny, mapId, head);
				teleport(doll, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				pc.sendPackets(new S_DollPack(doll, pc));

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(doll)) {
					visiblePc.removeKnownObject(doll);
					subjects.add(visiblePc);
				}

			}
			for (L1SupportInstance support : pc.getSupportList().values()) {

				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();

				//teleport(support, nx, ny, mapId, head);
				teleport(support, nx, ny, (short) mapid, pc.getMoveState().getHeading());
				pc.sendPackets(new S_SupportPack(support, pc));

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(support)) {
					visiblePc.removeKnownObject(support);
					subjects.add(visiblePc);
				}
			}			
		} else {			
			// 애완동물을 월드 MAP상으로부터 지운다
			Object[] petList = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.dropItem();
					pc.getPetList().remove(pet.getId());
					pet.deleteMe();
				}
				if (petObject instanceof L1SummonInstance) {
					summon = (L1SummonInstance) petObject;
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
					}
				}
			}

			// 마법인형을 월드 맵상으로부터 지운다
			Object[] dollList = pc.getDollList().values().toArray();
			L1DollInstance doll = null;
			for (Object dollObject : dollList) {
				doll = (L1DollInstance) dollObject;
				doll.deleteDoll(false);
			}

			Object[] supportList = pc.getSupportList().values().toArray();
			L1SupportInstance support = null;
			for (Object supportObject : supportList) {
				support = (L1SupportInstance) supportObject;
				support.deleteSupport();
			}
		}

		for (L1PcInstance updatePc : subjects) {
			updatePc.updateObject();
		}

		pc.set_teleport(false);
		
		/** 인스턴스 던전 아이템삭제 **/
		if (pc.getMap().getBaseMapId() != 1936 && pc.getMap().getBaseMapId() != 2600 && pc.getMap().getBaseMapId() != 2699) {
			removeItem(pc);
		}
		/** 인스턴스 던전 아이템삭제 **/
		
		if (pc.hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
			pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), pc.getSkillEffectTimeSec(L1SkillId.WIND_SHACKLE)));
		} else if (pc.hasSkillEffect(L1SkillId.DANCING_BLADES)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(L1SkillId.DANCING_BLADES)));
			pc.sendPackets(new S_SkillIconAura(154, pc.getSkillEffectTimeSec(L1SkillId.DANCING_BLADES)));
		} else if (pc.hasSkillEffect(L1SkillId.SAND_STORM)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(L1SkillId.SAND_STORM)));
		} else if(pc.hasSkillEffect(L1SkillId.HURRICANE)){
			pc.sendPackets(new S_SkillBrave(pc.getId(), 9, pc.getSkillEffectTimeSec(L1SkillId.HURRICANE)));			
		} else if(pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)){
			pc.sendPackets(new S_SkillBrave(pc.getId(), 10, pc.getSkillEffectTimeSec(L1SkillId.FOCUS_WAVE)));			
		}

		get_teleport_check(pc, old_mapid, pc.getMapId());
		MJTeleportChain.getInstance().teleported(pc, x, y, mapid);
		/** 2016.12.01 MJ 앱센터 LFC **/
		if(pc.getFindMerchantId() != 0){
			pc.sendPackets(new S_NPCTalkReturn(pc.getFindMerchantId(), "usershop"), true);
			pc.setFindMerchantId(0);
		}
		
		if(pc.isDead())
			pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Die));
	}
	
	private void removeItem(L1PcInstance pc) {
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item.getItemId() == 203003 || item.getItemId() == 810006 || item.getItemId() == 810007)
				pc.getInventory().removeItem(item);
		}
	}

	public static void teleport(L1NpcInstance npc, int x, int y, short map, int head) {
		L1World.getInstance().moveVisibleObject(npc, map);
		L1WorldMap.getInstance().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), true);
		npc.setX(x);
		npc.setY(y);
		npc.setMap(map);
		npc.setHeading(head);
		if(!(npc instanceof L1DollInstance) && !(npc instanceof L1TowerInstance))
			L1WorldMap.getInstance().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), false);
	}
	
	public void get_teleport_check(L1PcInstance pc, int mapid, int new_mapid) {
		if ((mapid == 5140 || mapid == 5143) && mapid != new_mapid) {
			L1HauntedHouse.getInstance().clearBuff(pc);
		}
		if (pc.isInFantasy) {
			if (!(mapid >= 1936 && mapid <= 2035)) {
				pc.getInventory().consumeItem(810006);
				pc.getInventory().consumeItem(810007);
				pc.isInFantasy = false;
			}
		}
		if (pc.isInValakas) {
			if (pc.getMap().getBaseMapId() != 2600 && pc.getMap().getBaseMapId() != 2699) {
				pc.getInventory().consumeItem(203003);
				pc.isInValakas = false;
			}
		}
		if (pc.isInValakasBoss && mapid != 2600) {
			pc.isInValakasBoss = false;
		}
		if (!(mapid >= 2101 && mapid <= 2151) &&
			!(mapid >= 2151 && mapid <= 2201) && 
			!(mapid >= 12152 && mapid <= 12252)) {
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item != null) {
					if (item.getItemId() == 30055 || item.getItemId() == 30056){
						pc.getInventory().removeItem(item);
					}
				}
			}
		}
		
//		if (!(mapid >= 1708 && mapid <= 1712) && (new_mapid >= 1708 && new_mapid <= 1712)) {
//		if (!(mapid >= 1708 && mapid <= 1712)) {
//			잊섬Controller.getInstance().add_list(pc);
//		}
		
		if (mapid != new_mapid) {
			if (pc.noPlayerCK || pc.noPlayerck2 || pc.getAI() != null) {
				return;
			}
			
			DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_map_id(new_mapid);
			if(dtInfo != null){
				pc.send_dungeon_progress(dtInfo);
			}
		}
	}
	
	public L1Location 소환텔레포트(L1Character target, int distance) {
		L1Location loc = new L1Location();
		int locX = target.getX();
		int locY = target.getY();
		int heading = target.getHeading();
		loc.setMap(target.getMapId());
		switch (heading) {
		case 1:locX += distance; locY -= distance; break;
		case 2:locX += distance; break;
		case 3:locX += distance; locY += distance; break;
		case 4:locY += distance; break;
		case 5:locX -= distance; locY += distance; break;
		case 6:locX -= distance; break;
		case 7:locX -= distance; locY -= distance; break;
		case 0:locY -= distance; break;
		}
		loc.setX(locX); loc.setY(locY);
		return loc;
	}

	public void teleportToTargetFront(L1Character cha, L1Character target, int distance, boolean check) {
		int locX = target.getX();
		int locY = target.getY();
		int heading = target.getHeading();
		L1Map map = target.getMap();
		short mapId = target.getMapId();

		switch (heading) {
		case 1: locX += distance; locY -= distance; break;
		case 2: locX += distance; break;
		case 3: locX += distance; locY += distance; break;
		case 4: locY += distance; break;
		case 5: locX -= distance; locY += distance; break;
		case 6: locX -= distance; break;
		case 7: locX -= distance; locY -= distance; break;
		case 0: locY -= distance; break;
		default: 
			break;
		}

		if (map.isPassable(locX, locY)) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.start_teleport(locX, locY, mapId, cha.getMoveState().getHeading(), 169, true, check);
			} 
		}
	}

	public void randomTeleport(L1PcInstance pc, boolean effect) {
		L1Location newLocation = pc.getLocation().randomLocation(200, true);
		int newX = newLocation.getX();
		int newY = newLocation.getY();
		int newHeading = pc.getHeading();
		short mapId = (short) newLocation.getMapId();
		pc.start_teleport(newX, newY, mapId, newHeading, 169, effect, false);
	}
}
