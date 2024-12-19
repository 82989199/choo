package l1j.server.server.Controller;

import static l1j.server.server.model.skill.L1SkillId.ANTA_BUFF;
import static l1j.server.server.model.skill.L1SkillId.FAFU_BUFF;
import static l1j.server.server.model.skill.L1SkillId.RIND_BUFF;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class PremiumTimeController implements Runnable {

	public static final int SLEEP_TIME = Config.FEATHERTIME * 60000; // 원본 600초
																		// 최소
																		// 10분
	private static final SimpleDateFormat _pFormat = new SimpleDateFormat("yyyyMMdd");
	private static PremiumTimeController _instance;

	public static PremiumTimeController getInstance() {
		if (_instance == null) {
			_instance = new PremiumTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			GeneralThreadPool.getInstance().execute(new checkPremiumTime());
			GeneralThreadPool.getInstance().execute(new checkDragonBlood());
			GeneralThreadPool.getInstance().execute(new 인형청소());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isPcCk(L1PcInstance pc) {
		if (pc == null || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0 || pc.isPrivateShop() || pc.noPlayerCK || pc.noPlayerck2)
			return true;
		return false;
	}

	private class checkPremiumTime implements Runnable {// 일정시간 깃털지급
		@Override
		public void run() {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (isPcCk(pc))
					continue;
				if (!pc.isPrivateShop() && !pc.noPlayerCK && !pc.noPlayerck2 && pc != null && !pc.isDead()) {
					int FN = Config.FEATHER_NUM;
					int CLN = Config.FEATHER_NUM1;
					int CAN = Config.FEATHER_NUM2;
					int PCR = Config.FEATHER_NUM3;//PC방 추가
					int realPremiumNumber = 1; // 기본적으로 줄 아이템 개수
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

					// String savedir = "c:\\uami\\"+new
					// SimpleDateFormat("yyyyMMdd").format(new
					// Date())+"\\"+pc.getName();
					String savedir = String.format("c:\\uami\\%s\\%s", _pFormat.format(new Date()), pc.getName());
					File dir = new File(savedir);
					
					

					/** 전체유저에게 선물을 지급한다 **/
					if (dir.exists()) { // 홍보기 켰을때
						realPremiumNumber = realPremiumNumber * 1; // 홍보기를 켰을때 지급될 개수
					}
					
					if (pc.PC방_버프){
						pc.getInventory().storeItem(41921, (PCR + FN));
						if(FN > 0){
						pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털(" + FN + "+" + PCR + ")개를 얻었습니다."));
						}else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털(" + PCR + ")개를 얻었습니다."));
						}
					}else {
						if(FN > 0){
						pc.getInventory().storeItem(41921, FN);
						pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털(" + FN + ")개를 얻었습니다."));
						}
					}
					
					/*if (pc.getClanid() == 0) { // 무혈
						pc.getInventory().storeItem(41921, FN);
						pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털(" + FN + ")개 를 얻었습니다."));
					}
					if (clan != null) {
						if (clan.getCastleId() == 0 && pc.getClanid() != 0) { // 혈맹
							pc.getInventory().storeItem(41921, (CLN + FN));
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털(" + FN + "+" + CLN + ")개 를 얻었습니다."));
						}
						if (clan.getCastleId() != 0) { // 성혈
							pc.getInventory().storeItem(41921, (CAN + FN));
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털(" + FN + "+" + CAN + ")개 를 얻었습니다."));
						}
					}*/
					
				}
			}
		}
	}

	private class 인형청소 implements Runnable {
		@Override
		public void run() {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (isPcCk(pc))
					continue;
				for (Object dollObject : pc.getDollList().values().toArray()) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						/** delete 호출되어 파기된 상태 * */
						if (doll._destroyed) {
							doll.deleteDoll(false);
							continue;
						}
						/** 주인이 없을 경우(deleteDoll() 호출 후 * */
						if (doll.getMaster() == null) {
							doll.deleteDoll(false);
							continue;
						}
					}
				}
			}
		}
	}

	private class checkDragonBlood implements Runnable {
		@Override
		public void run() {
			int time = 0;
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (isPcCk(pc))
					continue;
				if (pc.hasSkillEffect(ANTA_BUFF)) {
					time = pc.getSkillEffectTimeSec(ANTA_BUFF) / 60;
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, time));
				}
				if (pc.hasSkillEffect(FAFU_BUFF)) {
					time = pc.getSkillEffectTimeSec(FAFU_BUFF) / 60;
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, time));
				}
				if (pc.hasSkillEffect(RIND_BUFF)) {
					time = pc.getSkillEffectTimeSec(RIND_BUFF) / 60;
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, time));
				}
			}
		}
	}
}
