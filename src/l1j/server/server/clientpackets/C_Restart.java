package l1j.server.server.clientpackets;

import l1j.server.MJCombatSystem.MJCombatObserver;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.Chain.Action.MJDeadRestartChain;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_IvenBuffIcon;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.templates.L1Skills;

public class C_Restart extends ClientBasePacket {

	private static final String C_RESTART = "[C] C_Restart";

	public C_Restart(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		// clientthread.sendPacket(new S_PacketBox(S_PacketBox.LOGOUT));//리스버튼
		// 눌럿을시 보내져오는 패킷 로그인창으로 이동

		if (pc == null) {
			return;
		}
		if (pc.is_combat_field()) {
			MJCombatObserver observer = MJCombatLoadManager.getInstance()
					.get_current_observer(pc.get_current_combat_id());
			if (observer != null)
				observer.remove(pc);
		}

		// 레이드 처리
		MJRaidSpace.getInstance().getBackPc(pc);
		

		/** 2016.11.26 MJ 앱센터 LFC **/
		/* instance space 안에서는 리스불가 */
		if (MJInstanceSpace.isInInstance(pc) && pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE)
			return;

		/** 2016.11.26 MJ 앱센터 LFC **/

		pc.setCurrentSprite(pc.getClassId());
		if (!pc.isDead()) {
			return;
		}
		if (pc.isGhost()) { // 관람모드 버그 막기.
			pc.endGhost();
		}
		/** 2016.11.26 MJ 앱센터 LFC **/
		processRestart(pc);
		/** 2016.11.26 MJ 앱센터 LFC **/
		/** 인스턴스 던전 아이템삭제 **/
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item.getItemId() == 203003 || item.getItemId() == 810006 || item.getItemId() == 810007
					|| item.getItemId() == 30055 || item.getItemId() == 30056) {
				pc.getInventory().removeItem(item);
			}
		}
		/** 인스턴스 던전 아이템삭제 **/

	}

	@Override
	public String getType() {
		return C_RESTART;
	}

	/** 2016.11.26 MJ 앱센터 LFC **/
	public static void processRestart(L1PcInstance pc) {
		synchronized(pc){
			try{
				int[] loc = MJDeadRestartChain.getInstance().get_death_location(pc);
				if(loc == null){
					loc = Getback.GetBack_Restart(pc);
					if (pc.getHellTime() > 0) {
						loc = new int[3];
						loc[0] = 32701;
						loc[1] = 32777;
						loc[2] = 666;
					} else {
						loc = Getback.GetBack_Location(pc, true);
					}
				}
				pc.removeAllKnownObjects();
				pc.broadcastPacket(new S_RemoveObject(pc));
				// pc.killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);//랭킹변신 풀리도록 주석하면 안풀린다
				pc.setCurrentHp(pc.getLevel());
				pc.set_food(39); // 죽었을때 겟지? 10%
				pc.setDead(false);

				reloadNBuff(pc);
				pc.set_teleport(false);
				pc.set_is_non_action(false);
				pc.set_instance_status(MJEPcStatus.WORLD);
				pc.set_mark_status(0);
				pc.set_current_combat_id(0);
				pc.set_current_combat_team_id(-1);
				pc.setStatus(0);
				L1World.getInstance().moveVisibleObject(pc, loc[2]);
				pc.setX(loc[0]);
				pc.setY(loc[1]);
				pc.setMap((short) loc[2]);
				pc.sendPackets(new S_MapID(pc.getMap().getBaseMapId(), pc.getMap().isUnderwater()));
				MJExpAmplifierLoader.getInstance().set(pc);
				pc.broadcastPacket(S_WorldPutObject.get(pc));
				pc.sendPackets(S_WorldPutObject.put(pc));
				pc.sendPackets(new S_CharVisualUpdate(pc));
				pc.startHpMpRegeneration();
				pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()));

				L1Party party = pc.getParty();
				if (party != null)
					party.refreshPartyMemberStatus(pc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/** 2016.11.26 MJ 앱센터 LFC **/

	private static void reloadNBuff(L1PcInstance pc) {
		int[] skills = { 4075, 4076, 4077, 4078, 4079, 4080, 4081, 4082, 4083, 4084, 4085, 4086, 4087, 4088, 4089, 4090,
				4091, 4092, 4093, 4094, 4095 };

		for (Integer number : skills) {
			if (pc.hasSkillEffect(number)) {
				int time = pc.getSkillEffectTimeSec(number);
				L1Skills _skill = SkillsTable.getInstance().getTemplate(number);
				pc.sendPackets(new S_IvenBuffIcon(number, true, _skill.getSysmsgIdHappen(), time));
			}
		}
		
		int aftertamtime = (int) pc.TamTime() / 1000;
		int aftertamcount = pc.tamcount();
		
		if (aftertamcount == 1) {
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8265, 4181));
		} else if (aftertamcount == 2) {
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8266, 4182));
		} else if (aftertamcount == 3) {
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8267, 4183));
		} else if (aftertamcount == 4) {
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8268, 5046));
		} else if (aftertamcount == 5) {
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8269, 5047));
		}
	}
}
