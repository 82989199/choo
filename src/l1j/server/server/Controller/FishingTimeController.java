package l1j.server.server.Controller;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.MJExpRevision.MJEFishingType;
import l1j.server.MJExpRevision.MJFishingExpInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_낚시;
import l1j.server.server.serverpackets.ServerBasePacket;

public class FishingTimeController implements Runnable {

	public static final int SLEEP_TIME = 1000;

	private static FishingTimeController _instance;

	private final List<L1PcInstance> _fishingList = new FastTable<L1PcInstance>();

	private static Random _random = new Random(System.nanoTime());

	public static FishingTimeController getInstance() {
		if (_instance == null) {
			_instance = new FishingTimeController();
		}
		return _instance;
	}

	public void run() {
		try {
			fishing();
		} catch (Exception e1) {
			e1.printStackTrace();//예외처리 출력
		} finally {
		}
	}

	public void addMember(L1PcInstance pc) {
		if (pc == null || _fishingList.contains(pc)) {
			return;
		}
		_fishingList.add(pc);
	}

	public boolean isMember(L1PcInstance pc) {
		if (_fishingList.contains(pc)) {
			return true;
		}
		return false;
	}

	public void removeMember(L1PcInstance pc) {
		if (pc == null || !_fishingList.contains(pc)) {
			return;
		}
		_fishingList.remove(pc);
	}

	public boolean 성장낚시 = false;
	public boolean 고급성장낚시 = false;
	public boolean 고대은빛낚시 = false;
	public boolean 고대금빛낚시 = false;
	
	
	private void fishing() {
		if (_fishingList.size() > 0) {
			long currentTime = System.currentTimeMillis();
			L1PcInstance pc = null;
			Iterator<L1PcInstance> iter = _fishingList.iterator();
			while (iter.hasNext()) {
				pc = iter.next();
				if (pc == null || pc.getMapId() != 5490 || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0)
					continue;
				if (pc.isFishing()) {
					long time = pc.getFishingTime();
					if (currentTime > (time + 1000)) {
						//TODO 미끼가 있을경우
						if (pc._fishingRod.getItemId() == 600229 || pc._fishingRod.getItemId() == 87058 || pc._fishingRod.getItemId() == 87059 
								|| pc._fishingRod.getItemId() == 4100293 || pc.getInventory().consumeItem(41295, 1)) {
							//TODO 릴 장착 고탄력 낚싯대
							if (pc._fishingRod.getItemId() == 41294) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if(Config.INFINIE_FISHING){
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_낚시(80));
										릴장착고탄력낚싯대(pc);
									}else {
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(41293, 1);
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 80000);
											pc.sendPackets(new S_낚시(80));
											릴장착고탄력낚싯대(pc);
										}
									}
								}
								//TODO 릴장착은빛 낚싯대
							} else if (pc._fishingRod.getItemId() == 41305) { 
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if(Config.INFINIE_FISHING){
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_낚시(80));
										릴장착은빛낚싯대(pc);
									}else {
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(41293, 1);
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 80000);
											pc.sendPackets(new S_낚시(80));
											릴장착은빛낚싯대(pc);
										}
									}
								}
								//TODO 릴장착 금빛 낚싯대
							} else if (pc._fishingRod.getItemId() == 41306) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if(Config.INFINIE_FISHING){
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_낚시(80));
										릴장착금빛낚싯대(pc);
									}else {
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(41293, 1);
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 80000);
											pc.sendPackets(new S_낚시(80));
											릴장착금빛낚싯대(pc);
										}
									}
								}
								//TODO 황소개구리 낚시대
							} else if (pc._fishingRod.getItemId() == 9991) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if(Config.INFINIE_FISHING){
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_낚시(80));
										황소개구리낚싯대(pc);
									}else {
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(9993, 1); // 부러진낚싯대
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 80000);
											pc.sendPackets(new S_낚시(80));
											황소개구리낚싯대(pc);
										}
									}
								}
								// TODO 고대의 은빛 낚싯대
							} else if (pc._fishingRod.getItemId() == 87058) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if(Config.INFINIE_FISHING){
										pc.setFishingTime(System.currentTimeMillis() + 90000);
										pc.sendPackets(new S_낚시(90));
										고대은빛낚시 = true;
										Ancientsilver(pc);
									}else {
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(41293, 1);
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 90000);
											pc.sendPackets(new S_낚시(90));
											고대은빛낚시 = true;
											Ancientsilver(pc);
										}
									}
								}
								// TODO 고대의 금빛 낚싯대
							} else if (pc._fishingRod.getItemId() == 87059) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if(Config.INFINIE_FISHING){
										pc.setFishingTime(System.currentTimeMillis() + 90000);
										pc.sendPackets(new S_낚시(90));
										고대금빛낚시 = true;
										Ancientgold(pc);
									}else {
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(41293, 1);
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 90000);
											pc.sendPackets(new S_낚시(90));
											고대금빛낚시 = true;
											Ancientgold(pc);
										}
									}
								}
								//TODO 성장의 낚시대
							} else if (pc._fishingRod.getItemId() == 600229) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									/*if(Config.INFINIE_FISHING){
										pc.setFishingTime(System.currentTimeMillis() + 30000);
										pc.sendPackets(new S_낚시(30));
										성장낚시 = true;
										성장의낚시대(pc);
									}else {*/
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(41293, 1);
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 30000);	// 틱 당 30초
											pc.sendPackets(new S_낚시(30));
											성장낚시 = true;
											성장의낚시대(pc);
										}
//									}
								}
								//TODO 고급 성장의 낚시대
							} else if (pc._fishingRod.getItemId() == 4100293) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
//									if(Config.INFINIE_FISHING){
//										pc.setFishingTime(System.currentTimeMillis() + 20000); // 틱 당 20초
//										pc.sendPackets(new S_낚시(30));
//										고급성장낚시 = true;
//										고급성장의낚시대(pc);
//									}else {
										if (item.getChargeCount() <= 0) {
											L1ItemInstance newfishingRod = null;
											pc.getInventory().removeItem(item, 1);
											newfishingRod = pc.getInventory().storeItem(41293, 1);
											pc._fishingRod = newfishingRod;
											endFishing(pc);
										} else {
											item.setChargeCount(item.getChargeCount() - 1);
											pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
											pc.setFishingTime(System.currentTimeMillis() + 20000);	// 틱 당 20초
											pc.sendPackets(new S_낚시(30));
											고급성장낚시 = true;
											고급성장의낚시대(pc);
										}
//									}
								}
								//TODO 일반 낚싯대
							} else if (pc._fishingRod.getItemId() == 41293) {
								pc.setFishingTime(System.currentTimeMillis() + 240000);
								pc.sendPackets(new S_낚시(240));
								고탄력낚싯대(pc);
							}
						} else {
							// 미끼가 없어서 종료 처리 구간.
							endFishing(pc);
						}
					}
				}
			}
		}
	}

	public void endFishing(L1PcInstance pc) {	
		pc.setFishingTime(0);
		pc.setFishingReady(false);
		pc.setFishing(false);
		pc._fishingRod = null;
		if (성장낚시) {
			성장낚시 = false;
			pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Fishing_etc));
		} else if(고급성장낚시) {
			고급성장낚시 = false;
			pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Fishing_etc));
		} else if(고대은빛낚시) {
			고대은빛낚시 = false;
			pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Fishing_etc));
		} else if(고대금빛낚시) {
			고대금빛낚시 = false;
			pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Fishing_etc));
		}
		pc.sendPackets(new S_CharVisualUpdate(pc));
		Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
		pc.sendPackets(new S_ServerMessage(1163)); // 낚시가 종료했습니다.
		removeMember(pc);
	}
	
	// TODO 고대의 은빛 낚싯대
	private void Ancientsilver(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 블루베리아나
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8000) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8040) { // 레드 베리아나
			successFishing(pc, 49092, "레드 베리아나");
		} else if (chance < 8070) { // 앵무베리아나
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8351) { // 앵무베리아나
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8352) { // 작은 금빛 베리아나
			successFishing(pc, 41300, "$17523");
		} else {
			pc.sendPackets(new S_ServerMessage(1136));
			// 낚시에 실패했습니다.
		}
	}

	// TODO 고대의 금빛 낚싯대
	private void Ancientgold(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 블루베리아나
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8000) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8020) { // 레드 베리아나
			successFishing(pc, 49092, "레드 베리아나");
		} else if (chance < 8050) { // 앵무베리아나
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8351) { // 레드 베리아나
			successFishing(pc, 49092, "레드 베리아나");
		} else if (chance < 8352) { // 작은 금빛 베리아나
			successFishing(pc, 41300, "$17523");
		} else {
			pc.sendPackets(new S_ServerMessage(1136));
			// 낚시에 실패했습니다.
		}
	}

//	private void 성장의낚시대(L1PcInstance pc) {
//		int chance = _random.nextInt(10000) + 1;
//		if (chance < 6000) { // 베리아나
//			successFishing(pc, 41296, "$15564");
//		} else if (chance < 8000) { // 블루 베리아나
//			successFishing(pc, 41297, "$15565");
//		} else if (chance < 8020) { // 앵무베리아나
//			successFishing(pc, 41298, "$15566");
//		/*} else if (chance < 8130) { // 단풍의 은빛 선물상자
//			successFishing(pc, 300022, "투견의 은빛 선물상자");*/
//		} else if (chance < 8350) { // 퓨어 엘릭서
//			successFishing(pc, 820018, "$20462");
//		} else if (chance < 8360) { // 드래곤의 보물 상자
//			successFishing(pc, 1000006, "$24608");
//		/*} else if (chance < 8380) { // 단풍의 금빛 선물상자
//			successFishing(pc, 300021, "투견의 금빛 선물상자");*/
//		} else if (chance < 8380) { // 은빛 베리아나
//			successFishing(pc, 41299, "$17521");
//		} else if (chance < 8390) { // 금빛 베리아나
//			successFishing(pc, 41300, "$17523");
//		}else
//			successFishing(pc, 41296, "$15564");
//	}
	
	private void 성장의낚시대(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 9000) { // 베리아나
			successFishing(pc, 41296, "$15564");
			
		} else if (chance < 9100) { // 고대의 은빛 베리아나
			successFishing(pc, 49094, "$26231");
		
		} else if (chance < 9110) { // 고대의 금빛 베리아나
			successFishing(pc, 49095, "$26232");
		
		}else
			successFishing(pc, 41296, "$15564");
	}
	
	private void 고급성장의낚시대(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 9000) { // 베리아나
			successFishing(pc, 41296, "$15564");
			
		} else if (chance < 9100) { // 고대의 은빛 베리아나
			successFishing(pc, 49094, "$26231");
		
		} else if (chance < 9110) { // 고대의 금빛 베리아나
			successFishing(pc, 49095, "$26232");

		} else if (chance < 9120) { // 런커 베리아나
			successFishing(pc, 49093, "$26232");
			
		}else
			successFishing(pc, 41296, "$15564");
	}
	
//		int chance = _random.nextInt(10000) + 1;
//		if (chance < 6000) { // 베리아나
//			successFishing(pc, 41296, "$15564");
//			
//		} else if (chance < 8000) { // 블루 베리아나
//			successFishing(pc, 41297, "$15565");
//			
//		} else if (chance < 8020) { // 앵무베리아나
//			successFishing(pc, 41298, "$15566");
//			
//		} else if (chance < 8350) { // 런커 베리아나
//			successFishing(pc, 49093, "$20462");
//			
//		} else if (chance < 8360) { // 퓨어 엘릭서
//			successFishing(pc, 820018, "$20462");
//			
//		} else if (chance < 8370) { // 앵무베리아나
//			successFishing(pc, 41298, "$15566");
//			
//		} else if (chance < 8380) { // 은빛 베리아나
//			successFishing(pc, 41299, "$17521");
//			
//		} else if (chance < 8390) { // 금빛 베리아나
//			successFishing(pc, 41300, "$17523");
//			
//		} else if (chance < 8390) { // 아인하사드의 선물
//			successFishing(pc, 600230, "$20909");
//			
//		}else
//			successFishing(pc, 41296, "$15564");
//	}

	private void 황소개구리낚싯대(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 블루 베리아나
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8020) { // 앵무베리아나
			successFishing(pc, 41298, "$15566");
		/*} else if (chance < 8130) { // 단풍의 은빛 선물상자
			successFishing(pc, 300022, "단풍의 은빛 선물상자");*/
		} else if (chance < 8350) { // 퓨어 엘릭서
			successFishing(pc, 820018, "$20462");
		} else if (chance < 8360) { // 드래곤의 보물 상자
			successFishing(pc, 1000006, "$24608");
		/*} else if (chance < 8380) { // 단풍의 금빛 선물상자
			successFishing(pc, 300021, "단풍의 금빛 선물상자");*/
		} else if (chance < 8380) { // 은빛 베리아나
			successFishing(pc, 41299, "$17521");
		} else if (chance < 8390) { // 금빛 베리아나
			successFishing(pc, 41300, "$17523");
		} else if (chance < 8400) { // 금빛 베리아나
			successFishing(pc, 4100206, "현금 교환 쿠폰(5,000원)");
		} else if (chance < 8450) { // 금빛 베리아나
			successFishing(pc, 4100207, "현금 교환 쿠폰(10,000원)");
		} else if (chance < 8451) { // 금빛 베리아나
			successFishing(pc, 4100208, "현금 교환 쿠폰(20,000원)");
		}else
			successFishing(pc, 41296, "$15564");
	}

	private void 릴장착고탄력낚싯대(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 블루 베리아나
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8020) { // 앵무베리아나
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 축축한 낚시가방
			successFishing(pc, 41301, "$15815");
		} else if (chance < 8351) { // 작은 은빛 베리아나
			successFishing(pc, 41299, "$17521");
		} else if (chance < 8352) { // 작은 금빛 베리아나
			successFishing(pc, 41300, "$17523");
		} else {
			pc.sendPackets(new S_ServerMessage(1136));
			// 낚시에 실패했습니다.
		}
	}
	private void 릴장착은빛낚싯대(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 4000) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 블루 베리아나
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8040) { // 앵무베리아나
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 축축한 낚시가방
			successFishing(pc, 41301, "$15815");
		} else if (chance < 8352) { // 작은 은빛 베리아나
			successFishing(pc, 41299, "$17521");
		} else if (chance < 8353) { // 큰 은빛 베리아나
			successFishing(pc, 41303, "$17522");
		} else {
			pc.sendPackets(new S_ServerMessage(1136));
			// 낚시에 실패했습니다.
		}
	}
	private void 릴장착금빛낚싯대(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 3500) { // 블루베리아나
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8000) { // 베리아나
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8050) { // 앵무베리아나
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 축축한 낚시가방
			successFishing(pc, 41301, "$15815");
		} else if (chance < 8352) { // 작은 금빛 베리아나
			successFishing(pc, 41300, "$17523");
		} else if (chance < 8354) { // 큰 금빛 베리아나
			successFishing(pc, 41304, "$17524"); //
		} else {
			pc.sendPackets(new S_ServerMessage(1136));
			// 낚시에 실패했습니다.
		}
	}
	private void 고탄력낚싯대(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1; // 100%
		// 베리아나
		if (chance < 4000) {
			successFishing(pc, 41296, "$15564");
			// 블루베리아나
		} else if (chance < 8000) {
			successFishing(pc, 41297, "$15565");
			// 앵무베리아나
		} else if (chance < 8010) {
			successFishing(pc, 41298, "$15566");
			// 축축한 낚시가방
		} else if (chance < 8350) {
			successFishing(pc, 41301, "$15815");
		} else {
			pc.sendPackets(new S_ServerMessage(1136)); // 16%
			// 낚시에 실패했습니다.
		}
		pc.sendPackets(new S_ServerMessage(1147));
	}

	private boolean check_weight(L1PcInstance pc){
		if (pc.getInventory().getSize() > (180 - 16)) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		return true;
	}
	
	private void distribute_message(int owner_object_id, String message, int effect_id){
		ServerBasePacket[] pcks = new ServerBasePacket[]{
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message),
				new S_SkillSound(owner_object_id, effect_id),
		};
		L1World.getInstance().broadcast_map(5490, pcks);
	}
	
	private void distribute_message(int owner_object_id, int itemid){
		switch(itemid){
		case 41303:
			distribute_message(owner_object_id, "누군가가 큰 은빛 베리아나를 낚아 올렸습니다!", 13641); // 낚시 성공 시 13641 화이트 피쉬 이펙트
			break;
		case 41304:
			distribute_message(owner_object_id, "누군가가 큰 금빛 베리아나를 낚아 올렸습니다!", 13639); // 낚시 성공 시 13639 골드 피쉬 이펙트
			break;
		case 49094:
			distribute_message(owner_object_id, "누군가가 고대의 은빛 베리아나를 낚아 올렸습니다!", 13641); // 낚시 성공 시 13641 화이트 피쉬 이펙트
			break;
		case 49095:
			distribute_message(owner_object_id, "누군가가 고대의 금빛 베리아나를 낚아 올렸습니다!", 13639); // 낚시 성공 시 13639 골드 피쉬 이펙트
			break;
		}
	}
	
	private void calculate_exp(L1PcInstance pc, MJEFishingType f_type){
		try {
			int level = Math.max(pc.getLevel(), 2);
			MJFishingExpInfo eInfo = MJFishingExpInfo.find_fishing_exp_info(f_type, level);
			if(eInfo == null)
				return;
			
			int need_exp = ExpTable.getNeedExpNextLevel(52);
			double exp = need_exp * eInfo.get_default_exp();
			int ain = pc.getAccount().getBlessOfAin();
			if(ain >= 10000){
				double ain_effect = 1D;
				if(ain > 20000)
					ain_effect += 0.3D;
				if(pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ) && ain >= 20000)
					ain_effect += 0.8D;
				exp += (exp * ain_effect);
				pc.getAccount().addBlessOfAin(-(int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp * eInfo.get_ain_ration()), pc);
			}
			if (pc.PC방_버프)
				exp *= 1.1;
			
			exp = exp * eInfo.get_addition_exp();
			exp = Math.max(1, exp);
			
			if (pc.getLevel() >= GameServerSetting.getInstance().get_maxLevel()) {
				int maxexp = ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1);
				if (pc.getExp() + exp >= maxexp)
					return;
			}
			
			if ((exp + pc.getExp()) > ExpTable.getExpByLevel((level + 1))) {
				exp = (ExpTable.getExpByLevel((level + 1)) - pc.getExp());
			}
			pc.addExp((int)exp);
			pc.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void successFishing(L1PcInstance pc, int itemid, String message) {
		if(!check_weight(pc))
			return;
		
		L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
		if (item != null) {
			pc.sendPackets(new S_ServerMessage(1185, message)); // 낚시에 성공해%0%o를 낚시했습니다.
			ServerBasePacket pck = new S_SkillSound(pc.getId(), 763);
			pc.sendPackets(pck, false);
			pc.broadcastPacket(pck);
		}
		distribute_message(pc.getId(), itemid);
		if (성장낚시) {
			calculate_exp(pc, MJEFishingType.GROWN_UP);
			성장낚시 = false;
		} else if (고급성장낚시) {
			calculate_exp(pc, MJEFishingType.HIGH_GROWN_UP);
			고급성장낚시 = false;
		} else if (고대은빛낚시) {		
			calculate_exp(pc, MJEFishingType.ACIENT_SILVER);	
			고대은빛낚시 = false;
		} else if (고대금빛낚시) {
			calculate_exp(pc, MJEFishingType.ACIENT_GOLD);
			고대금빛낚시 = false;
		}
	}
}
