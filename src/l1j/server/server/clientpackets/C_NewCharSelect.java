package l1j.server.server.clientpackets;

import java.util.Calendar;
import java.util.logging.Logger;

import l1j.server.MJCombatSystem.MJCombatObserver;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.Chain.Action.MJRestartChain;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.GameClient;
import l1j.server.server.UserCommands;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_Unknown2;

public class C_NewCharSelect extends ClientBasePacket {
	private static final String C_NEW_CHAR_SELECT = "[C] C_NewCharSelect";
	private static Logger _log = Logger.getLogger(C_NewCharSelect.class.getName());
	/** 날짜 및 시간 기록 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int sec = rightNow.get(Calendar.SECOND);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = "[" + year + ":" + month + ":" + day + "]";
	String totime1 = "[" + hour + ":" + min + ":" + sec + "]";
	String date = +year + "_" + month + "_" + day;

	public C_NewCharSelect(byte[] decrypt, GameClient client) throws Exception { //리스 버튼 눌렀을경우 들어온다.
		super(decrypt);
		if (client.getActiveChar() != null) {
			L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				return;
			}
			// 데스페라도 걸린상태라면 리스불가
			if (pc.hasSkillEffect(L1SkillId.DESPERADO)) { //스턴 본브레이크 포그 등등 해주는게 좋음. 판타즘?
				return;
			}
			
			if(pc.is_combat_field()){
				MJCombatObserver observer = MJCombatLoadManager.getInstance().get_current_observer(pc.get_current_combat_id());
				if(observer != null)
					observer.remove(pc);
			}
			
			/** MJ 여관 복사맵 **/
			MJCopyMapObservable.getInstance().resetPosition(pc);
			/** MJ 레이드 **/
			MJRaidSpace.getInstance().getBackPc(pc);
			
			/** 2016.11.26 MJ 앱센터 LFC **/
			/* instance space 안 리스 처리 */
			if(MJInstanceSpace.isInInstance(pc)){
				/*if(pc.isDead() && pc.getInstStatus() == InstStatus.INST_USERSTATUS_NONE){
					C_Restart.processRestart(pc);
					return;
				}*/
				if(pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE)
					return;
			}

			restartProcess(pc);
		} else {
			_log.fine("Disconnect Request from Account : " + client.getAccountName());
		}

	}
	
	/** MJCTSystem **/
	public static void restartProcess(L1PcInstance pc){
		pc.isWorld = false;

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
			pc.setOnlineStatus(0);
			clan.updateClanMemberOnline(pc);
		}
		_log.fine("Disconnect from: " + pc.getName());
		GameClient client = pc.getNetConnection();
		if(client == null)
			return;

		synchronized (pc) {
			try{
				if(pc.isPrivateShop() && pc.isPrivateReady()){
					UserCommands.privateShop(pc);
					return;
				}
				pc.logout();
				client.setStatus2(MJClientStatus.CLNT_STS_AUTHLOGIN);
				client.setActiveChar(null);
				client.sendPacket(new S_Unknown2(1));
				if(client.getAccount().is_changed_slot()){
					int amountOfChars = client.getAccount().countCharacters();
					int slot = client.getAccount().getCharSlot();
					client.sendPacket(new S_CharAmount(amountOfChars, slot));
					C_CommonClick.sendCharPacks(client);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		MJRestartChain.getInstance().on_restarted(pc);
	}

	@Override
	public String getType() {
		return C_NEW_CHAR_SELECT;
	}
}
