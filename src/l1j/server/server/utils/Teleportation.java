package l1j.server.server.utils;

import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;

import java.util.HashSet;
import java.util.Random;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
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
import l1j.server.server.serverpackets.S_DollPack;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.S_SupportPack;
import l1j.server.server.serverpackets.S_WorldPutObject;

public class Teleportation {

	private static Random _random = new Random(System.nanoTime());

	private Teleportation() {
	}

	public static void doTeleportation(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		doTeleportation(pc, false);
	}

	public static void doTeleportation(L1PcInstance pc, boolean type) {
		if (pc == null) {
			return;
		}
		
		int oldZoneType = pc.getZoneType();
		
		L1Clan clan = pc.getClan();
		if (clan != null)
			clan.deleteClanRetrieveUser(pc.getId());
		
		if(pc.get_teleport())
			return;
		
		// 레이드에서는 죽어도 텔이되야 한다.
		/** 2016.11.26 MJ 앱센터 LFC **/
		if(pc.isDead()){
			if(!(MJRaidSpace.getInstance().isInInstance(pc) || MJInstanceSpace.isInInstance(pc)))
				return;
		}
		/** 2016.11.26 MJ 앱센터 LFC **/
		
//		int x = pc.getTeleportX();
//		int y = pc.getTeleportY();
//		short mapId = pc.getTeleportMapId();
//		int head = pc.getTeleportHeading();
		int x = pc.get_teleport_x();
		int y = pc.get_teleport_y();
		int mapid = pc.get_teleport_map();

//		L1Map map = L1WorldMap.getInstance().getMap(mapId);
		L1Map map = L1WorldMap.getInstance().getMap((short) mapid);

		if (!map.isInMap(x, y) && !pc.isGm()) {
			x = pc.getX();
			y = pc.getY();
		//	mapId = pc.getMapId();
			mapid = pc.getMapId();
		}

//		pc.setTeleport(true);
	    
		pc.getMap().setPassable(pc.getLocation(), true);

//		L1World.getInstance().moveVisibleObject(pc, mapId);
//		pc.setLocation(x, y, mapId);
//		pc.setHeading(head);
		L1World.getInstance().moveVisibleObject(pc, mapid);
		pc.setLocation(x, y, mapid);
		pc.setHeading(pc.getMoveState().getHeading());
		
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
		/** 패키지상점 **/
		if (pc.getMapId() != 631) {
			pc.broadcastPacket(S_WorldPutObject.get(pc));
			//pc.broadcastPacket(new S_OtherCharPacks(pc));
		}
		/** 패키지상점 **/
		pc.broadcastRemoveAllKnownObjects();
		pc.removeAllKnownObjects();
		pc.sendPackets(S_WorldPutObject.put(pc));
		//pc.sendPackets(new S_OwnCharPack(pc));
		pc.updateObject();
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
			pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, reminingtime));
			pc.sendPackets(new S_Liquor(pc.getId(), 8));
			pc.setPearl(1);
		}

		for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(pc)) {
			if (target.isPinkName()) {
				pc.sendPackets(new S_PinkName(target.getId(), target.getPinkNameTime()));
			}
		}

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
				if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133
						|| pc.getMapId() == 5134 || pc.getMapId() == 781 || pc.getMapId() == 782) {
					nx = 32799 + _random.nextInt(5) - 3;
					ny = 32864 + _random.nextInt(5) - 3;
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

	//	pc.setTeleport(false);
		pc.set_teleport(false);
		
		DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance().from_map_id(pc.getMapId());
		if(dtInfo != null){
			pc.send_dungeon_progress(dtInfo);
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
		} else if (pc.hasSkillEffect(L1SkillId.SAND_STORM)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, pc.getSkillEffectTimeSec(L1SkillId.SAND_STORM)));
		} else if (pc.hasSkillEffect(L1SkillId.HURRICANE)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 9, pc.getSkillEffectTimeSec(L1SkillId.HURRICANE)));
		} else if (pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)) {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 10, pc.getSkillEffectTimeSec(L1SkillId.FOCUS_WAVE)));
		}

	}
	
	private static void removeItem(L1PcInstance pc) {
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

}
