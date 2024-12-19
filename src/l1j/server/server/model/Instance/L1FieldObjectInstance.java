package l1j.server.server.model.Instance;

import l1j.server.server.Controller.CrockController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;

public class L1FieldObjectInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;
	private int moveMapId;

	public L1FieldObjectInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {  }

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int npcid = getNpcTemplate().get_npcId();

		switch(npcid){
		/** 시간의 균열 **/
		case 200:
			if (CrockController.getInstance().isTimeCrock()) {
				if (CrockController.getInstance().crocktype() == 0) {
//					L1Teleport.teleport(pc, 32639, 32876, (short) 780, 4, false);// 테베
					pc.start_teleport(32639, 32876, 780, 5, 169, false, false);
				} else {
//					L1Teleport.teleport(pc, 32794, 32751, (short) 783, 4, false);// 티칼
					pc.start_teleport(32794, 32751, 783, 5, 169, false, false);
				}
			} else {
				pc.sendPackets(new S_ChatPacket(pc, "시간의 균열은 현재 닫혀있습니다."));
				pc.sendPackets(new S_ChatPacket(pc, "오픈시간은 매일저녁 7시 입니다."));
			}
			break;
		case 7210011: //발라카스의 안식처
			telValakasRoom(pc); 
			break;
		default: 
			break; 
		}
	}

	/** 지정된 맵의 32명이 넘는지 체크해서 텔시킨다 @param pc @param mapid 
	 * 	1626: 드래곤의 혈흔이 온 몸에서 풍겨집니다. 혈흔의 냄새가 사라질 때까지 드래곤 포탈에 입장 할 수 없습니다.*/
	private void DragonRaidMap(L1PcInstance pc, int mapid){
		int count = 0;
		for(L1PcInstance player : L1World.getInstance().getAllPlayers()){
			if(player == null)
				continue;
			if(player.getMapId() == mapid){
				count += 1;
				if(count > 31)
					return;
			}
		}

		switch(getNpcTemplate().get_npcId()) {
		case 900007:
			if (pc.hasSkillEffect(L1SkillId.ANTA_BUFF) || pc.hasSkillEffect(L1SkillId.FAFU_BUFF) || pc.hasSkillEffect(L1SkillId.RIND_BUFF)){
				pc.sendPackets(new S_ServerMessage(1626)); 
				return; 
			} 
//			if(AntarasRaidSystem.getInstance().getAR(mapid).isAntaras()){
//				pc.sendPackets(new S_ServerMessage(1537));// 드래곤이 깨서 진입 못한다
//				return;
//			} else {
				pc.sendPackets(new S_Message_YN(2923, ""));
				pc.DragonPortalLoc[0] = 32600;
				pc.DragonPortalLoc[1] = 32741;
				pc.DragonPortalLoc[2] = mapid;
//			}
			break;
		case 900036:
			if (pc.hasSkillEffect(L1SkillId.ANTA_BUFF) || pc.hasSkillEffect(L1SkillId.FAFU_BUFF) || pc.hasSkillEffect(L1SkillId.RIND_BUFF)){
				pc.sendPackets(new S_ServerMessage(1626)); 
				return; 
			} 
//			if(FafurionRaidSystem.getInstance().getAR(mapid).isFafurion()){
//				pc.sendPackets(new S_ServerMessage(1537));// 드래곤이 깨서 진입 못한다
//				return;
//			} else {
				pc.sendPackets(new S_Message_YN(2923, ""));
				pc.DragonPortalLoc[0] = 32976;
				pc.DragonPortalLoc[1] = 32743;
				pc.DragonPortalLoc[2] = mapid;
//			}
			break;
		}
	}
	
	private void telValakasRoom(L1PcInstance pc) {
		pc.start_teleport(32833, 32757, getMapId(), 5, 169, false, false);
		pc.isInValakasBoss = true;
	}
	/** 이동할 맵을 설정한다. @param id */
	public void setMoveMapId(int id){ moveMapId = id; }

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		removeAllKnownObjects();
	}
}
