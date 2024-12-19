package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Etc.MJAdenaPickupChain;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.utils.SQLUtil;

public class DropTable {

	private static Logger _log = Logger.getLogger(DropTable.class.getName());

	private static DropTable _instance;

	private static final Random _random = new Random();

	private final HashMap<Integer, ArrayList<L1Drop>> _droplists; // monster 마다의 드롭 리스트
	private final HashMap<Integer, L1Drop> _adenalists;	// monster 마다의 아덴
	
	public static DropTable getInstance() {
		if (_instance == null) {
			_instance = new DropTable();
		}
		return _instance;
	}

	private DropTable() {
		_droplists = allDropList();
		_adenalists = createAdenaDropList();
	}

	public static void reload() {
		DropTable oldInstance = _instance;
		_instance = new DropTable();
		oldInstance._droplists.clear();
		oldInstance._adenalists.clear();
	}

	public ArrayList<L1Drop> getDropList(int monid) {
		return _droplists.get(monid);
	}

	public boolean isDropListItem(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return true;
			}
		}
		return false;
	}

	public L1Drop getDrop(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return d;
			}
		}
		return null;
	}

	private HashMap<Integer, L1Drop> createAdenaDropList() {
		HashMap<Integer, L1Drop> map = new HashMap<Integer, L1Drop>();
		Selector.exec("select * from droplist_adena", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int mobId = rs.getInt("mobId");
					int itemId = rs.getInt("itemId");
					int min = rs.getInt("min");
					int max = rs.getInt("max");
					int chance = rs.getInt("chance");
					L1Drop drop = new L1Drop(mobId, itemId, min, max, chance);
					map.put(drop.getMobid(), drop);
				}
			}
		});
		return map;
	}

	private HashMap<Integer, ArrayList<L1Drop>> allDropList() {
		HashMap<Integer, ArrayList<L1Drop>> droplistMap = new HashMap<Integer, ArrayList<L1Drop>>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from droplist");
			rs = pstm.executeQuery();
			L1Drop drop = null;
			while (rs.next()) {
				int mobId = rs.getInt("mobId");
				int itemId = rs.getInt("itemId");
				int min = rs.getInt("min");
				int max = rs.getInt("max");
				int chance = rs.getInt("chance");

				drop = new L1Drop(mobId, itemId, min, max, chance);

				ArrayList<L1Drop> dropList = droplistMap.get(drop.getMobid());
				if (dropList == null) {
					dropList = new ArrayList<L1Drop>(8);
					droplistMap.put(new Integer(drop.getMobid()), dropList);
				}
				dropList.add(drop);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return droplistMap;
	}

	private void append_drop_adena(L1PcInstance last_attacker, L1NpcInstance monster){
		L1Drop drop = _adenalists.get(monster.getNpcId());
		if(drop == null || monster.isResurrect())
			return;
		
		if (Config.RATE_DROP_ADENA <= 0 || Config.RATE_DROP_ITEMS <= 0)
			return;
		
		int randomChance = MJRnd.next(0xf4240) + 1;
		double rateOfMapId = MapsTable.getInstance().getDropRate(monster.getMap().getBaseMapId());
		double rateOfItem = DropItemTable.getInstance().getDropRate(L1ItemId.ADENA);
		double rateOfbless = Config.ADENA_RATE_OF_UNBLESSING;
		if(last_attacker.getInventory().checkItem(4100121))//4100121
			rateOfbless = Config.ADENA_RATE_OF_BLESSING;
		if(drop.getChance() * Config.RATE_DROP_ITEMS * rateOfMapId * rateOfItem * rateOfbless < randomChance)
			return;
		
		double amount = DropItemTable.getInstance().getDropAmount(L1ItemId.ADENA);
		int min = (int) (drop.getMin() * amount * Config.RATE_DROP_ADENA);
		int max = (int) (drop.getMax() * amount * Config.RATE_DROP_ADENA);
		int itemCount = min;
		int addCount = max - min + 1;

		if (addCount > 1)
			itemCount += MJRnd.next(addCount);
		if (itemCount < 0)
			return;
		
		itemCount = Math.min(itemCount, 2000000000);
		L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA, false);
		item.setCount(itemCount);
		// 아이템 격납
		monster.getInventory().storeItem(item);
	}
	
	// 인벤트리에 드롭을 설정
	public void setDrop(L1NpcInstance npc, L1Inventory inventory) {
		if (Config.STANDBY_SERVER) {
			return;
		}
		// 드롭 리스트의 취득
		int mobId = npc.getNpcTemplate().get_npcId();
		ArrayList<L1Drop> dropList = _droplists.get(mobId);
		if (dropList == null) {
			return;
		}

		// 레이트 취득
		double droprate = Config.RATE_DROP_ITEMS;
		if (droprate <= 0) {
			droprate = 0;
		}
		double adenarate = Config.RATE_DROP_ADENA;
		if (adenarate <= 0) {
			adenarate = 0;
		}
		if (droprate <= 0 && adenarate <= 0) {
			return;
		}

		int itemId;
		int itemCount;
		int addCount;
		int randomChance;
		L1ItemInstance item;
		/** 환상 이벤트 **/
		L1ItemInstance Fitem;
		Random random = new Random(System.nanoTime());

		for (L1Drop drop : dropList) {
			// 드롭 아이템의 취득
			itemId = drop.getItemid();
			if (adenarate == 0 && itemId == L1ItemId.ADENA) {
				continue; // 아데나레이트 0으로 드롭이 아데나의 경우는 스르
			}

			// 드롭 찬스 판정
			randomChance = random.nextInt(0xf4240) + 1;
			double rateOfMapId = MapsTable.getInstance().getDropRate(npc.getMap().getBaseMapId());
			double rateOfItem = DropItemTable.getInstance().getDropRate(itemId);
			if (droprate == 0 || drop.getChance() * droprate * rateOfMapId * rateOfItem < randomChance) {
				continue;
			}

			// 드롭 개수를 설정
			double amount = DropItemTable.getInstance().getDropAmount(itemId);
			int min = (int) (drop.getMin() * amount);
			int max = (int) (drop.getMax() * amount);

			if (itemId == L1ItemId.ADENA) { // 드롭이 아데나의 경우는 아데나레이트를 건다
				min *= adenarate;
				max *= adenarate;
			}

			itemCount = min;
			addCount = max - min + 1;

			if (addCount > 1) {
				itemCount += random.nextInt(addCount);
			}
			/*
			 * if (itemId == L1ItemId.ADENA) { // 드롭이 아데나의 경우는 아데나레이트를 건다
			 * itemCount *= adenarate; }
			 */
			if (itemCount < 0) {
				itemCount = 0;
			}
			if (itemCount > 2000000000) {
				itemCount = 2000000000;
			}

			// 아이템의 생성
			if (ItemTable.getInstance().getTemplate(itemId) != null) {
				item = ItemTable.getInstance().createItem(itemId, false);
				item.setCount(itemCount);
				// 아이템 격납
				inventory.storeItem(item);
			} else {
				_log.info("[드랍 리스트 로딩중]없는 아이템입니다: " + itemId);
				System.out.println("[드랍 리스트 로딩중]없는 아이템입니다: " + itemId);
			}
		}

		/*
		 * int[] lastabard = { 80453, 80454, 80455, 80456, 80457, 80458, 80459,
		 * 80460, 80461, 80462, 80463, 80452 }; int[] tower = { 80450, 80451,
		 * 80466, 80467 }; int[] glu = { 80464, 80465 }; int[] oman = { 80468,
		 * 80469, 80470, 80471, 80472, 80473, 80474, 80475, 80476, 80477 }; int
		 * 드랍율 = random.nextInt(2000) + 1; int 라던 =
		 * random.nextInt(lastabard.length); int 상아탑 =
		 * random.nextInt(tower.length); int 본던 = random.nextInt(glu.length);
		 * int 오만 = random.nextInt(oman.length); switch (npc.getMapId()) { //
		 * case 479: case 475: case 462: case 453: case 492: if (2 >= 드랍율) {
		 * inventory.storeItem(lastabard[라던], 1); } break; case 78: case 79:
		 * case 80: case 81: case 82: if (2 >= 드랍율) {// 상아탑
		 * inventory.storeItem(tower[상아탑], 1); } break; case 807: case 808: case
		 * 809: case 810: case 811: case 812: case 813: if (2 >= 드랍율) {// 본던
		 * inventory.storeItem(glu[본던], 1); } break; case 101: case 102: case
		 * 103: case 104: case 105: case 106: case 107: case 108: case 109: case
		 * 110: case 111: if (3 >= 드랍율) {// 오만 inventory.storeItem(oman[오만], 1);
		 * } break; }
		 */

	}

	// 드롭을 분배
	public void dropShare(L1NpcInstance npc, ArrayList<?> acquisitorList, ArrayList<?> hateList, L1PcInstance pc) {
		if (Config.STANDBY_SERVER) {
			return;
		}

		append_drop_adena(pc, npc);
		L1Inventory inventory = npc.getInventory();
		int mobId = npc.getNpcTemplate().get_npcId();
		
		/** 보스몹 자동분배 **/
		switch (mobId) {
		case 5100:
		case 900013:
		case 900040: // 드래곤 드랍 설정.
			if (npc.getMapId() >= 1005 && npc.getMapId() <= 1022 || npc.getMapId() > 6000 && npc.getMapId() < 6499
					|| npc.getMapId() > 6501 && npc.getMapId() < 6999) {
				return;
			}
			break;
		case 400016:
		case 400017:
			Mapdrop(npc);
			break;
		/** 해당 몬스터 바닥에 아이템을 골고루 흩뿌려줌 **/
		case 5135:		// 샌드웜: 사막
		case 5136:		// 에르자베
		case 46025:		// 간수장 타로스: 기란 감옥 1층
		case 45601:		// 데스나이트: 글루디오 던전 7층
		case 7123:		// 아크모: 용의 계곡 던전 6층
		case 45649:		// 데몬: 상아탑 8층
		case 7000093:	// 제로스: 용의 계곡 필드
		case 7310147:	// 마이노 샤먼: 풍룡의 둥지
		case 5146:		// 큰 발의 마요: 오렌 설벽	
		case 45617:		// 피닉스: 화룡의 둥지
		case 8500105:	// 불멸의 리치: 오만의 탑 9층
		case 8500111:	// 오만한 우그누스: 오만의 탑 10층	
		case 8500129:	// 사신 그림 리퍼: 오만의 탑 정상
		case 45844:		// 마수군왕 바란카: 라스타바드 중앙광장
		case 45753:		// 발록(변신 후): 지배의 결계
		case 7310117:	// 테베 아누비스(변신 후) (동물): 테베 신전
		case 7310116:	// 테베 호루스 (인간): 테베 신전
		case 14212114:	// 안타라스
		case 14212120:	// 파푸리온
		case 14212134:	// 린드비오르
		case 14212144:	// 발라카스
			//20221107
			RanDrop(npc, acquisitorList, hateList, pc);
			break;
		case 81163:// 기르타스
			RanDrop1(npc);
			break;
		case 91200:
		case 91201:
		case 7320099:// 해상전
			RanDrop2(npc);
			break;
		case 800018:
		case 800019:// 제브레퀴
			RanDrop3(npc);
			break;
		}

		if (mobId == 7320052 || mobId == 45021 || mobId == 45022 || mobId == 45040 || mobId == 45048) {
			huntEventDrop(npc);
			return;
		}

		if (inventory.getItems() == null || inventory.getItems().size() <= 0) {
			return;
		}
		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		/** 로봇시스템 **/
		if (pc.getAI() != null) {
			return;
		}
		/** 로봇시스템 **/

		
		
		// 헤이트의 합계를 취득
		int totalHate = 0;
		L1Character acquisitor;
		for (int i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = (L1Character) acquisitorList.get(i);
			if ((Config.AUTO_LOOT == 0)
					// 오토 루팅 2의 경우는 사몬 및 애완동물은 생략한다
					&& (acquisitor instanceof L1SummonInstance || acquisitor instanceof L1PetInstance || acquisitor instanceof MJCompanionInstance)) {
				acquisitorList.remove(i);
				hateList.remove(i);
			} else if (acquisitor != null && acquisitor.getMapId() == npc.getMapId()
					&& acquisitor.getLocation().getTileLineDistance(npc.getLocation()) <= Config.LOOTING_RANGE) {
				totalHate += (Integer) hateList.get(i);
			} else {
				// null였거나 죽기도 하고 멀었으면 배제
				acquisitorList.remove(i);
				hateList.remove(i);
			}
		}

		// 드롭의 분배
				L1Inventory targetInventory = null;
				L1PcInstance player;
				Random random = new Random();
				int randomInt;
				int chanceHate;
				for (int i = inventory.getSize(); i > 0; i--) {
					L1ItemInstance item = null;
					try {
						item = (L1ItemInstance) inventory.getItems().get(0);
						if (item == null) {
							continue;
						}
					} catch (Exception e) {
						System.out.println("드랍리스트 오류 표시 아이디 :" + npc.getNpcId() + " [이름] :" + npc.getName());
					}

					int itemId = item.getItem().getItemId();
					if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light계
						// 아이템
						item.setNowLighting(false);
					}
			if ((Config.AUTO_LOOT != 0 || AutoLoot.getInstance().isAutoLoot(itemId)
					   	  || item.getItem().getItemId() == 40074 //갑옷마법 주문서
					      || item.getItem().getItemId() == 140074 //갑옷마법 주문서
					      || item.getItem().getItemId() == 240074 //갑옷마법 주문서
					      || item.getItem().getItemId() == 40087 //무기마법 주문서
					      || item.getItem().getItemId() == 140087 //무기마법 주문서
					      || item.getItem().getItemId() == 240087 //무기마법 주문서 
					      || item.getItem().getItemId() == 3000246 // 오림의 가넷
					      || item.getItem().getItemId() == 3000520 // 하딘의 가넷
					      || item.getItem().getItemId() == 40304 // 마프르의 유산
					      || item.getItem().getItemId() == 40305 // 파아그리오의 유산
					      || item.getItem().getItemId() == 40304 // 에바의 유산
					      || item.getItem().getItemId() == 40304 // 사이하의 유산
					      ) && totalHate > 0) {
				randomInt = random.nextInt(totalHate);
				chanceHate = 0;
				for (int j = hateList.size() - 1; j >= 0; j--) {
					chanceHate += (Integer) hateList.get(j);
					if (chanceHate > randomInt) {
						acquisitor = (L1Character) acquisitorList.get(j);
						//
						if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
							targetInventory = acquisitor.getInventory();
							if (acquisitor instanceof L1PcInstance) {
								player = (L1PcInstance) acquisitor;
								L1ItemInstance l1iteminstance = player.getInventory().findItemId(L1ItemId.ADENA);
								// 소지 아데나를 체크
								if (l1iteminstance != null && l1iteminstance.getCount() > 2000000000) {
									targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId());
									player.sendPackets(new S_SystemMessage("소지 아데나 20억을 초과 하였습니다.(창고에 맡기고 다시찾으세요)"));
								} else {
									if (player.isInParty()) { // 파티의 경우
										L1PcInstance[] partyMember = player.getParty().getMembers();// partyMember
										if (item != null && item.getItemId() != L1ItemId.ADENA && Config.AUTO_LOOT == 0) {
											int Who = _random.nextInt(partyMember.length);
											L1PcInstance pc1 = partyMember[Who];
											
											if (player.getLocation().getTileLineDistance(pc1.getLocation()) < 14) {
												String 이름 = pc1.getName();
												String 아이템이름 = item.getName();
												int 아이템갯수 = item.getCount();
												targetInventory = pc1.getInventory();
												S_SystemMessage pck = new S_SystemMessage(String.format("파티:%s님께서 [%s]을 [%d]개 획득하셨습니다.", 이름, 아이템이름, 아이템갯수));
												
												for (int p = 0; p < partyMember.length; p++) {
													if (partyMember[p].RootMent)
														partyMember[p].sendPackets(pck, false);
												}
												pck.clear();
											}
										} else if (item != null && item.getItemId() == L1ItemId.ADENA) {
											List<L1PcInstance> _membersList = new ArrayList<L1PcInstance>(partyMember.length);
											for (int a = 0; a < partyMember.length; a++) {
												if (partyMember[a].getLocation().getTileLineDistance(pc.getLocation()) < 14) {
													_membersList.add(partyMember[a]);
												}
											}
											int 아데나 = item.getCount() / _membersList.size();
											if (is_blessing_effect(pc, item))
												아데나 = (int) ((item.getCount() * 1.3) / _membersList.size());

											int 아데나2 = 0;
											for (int A = 0; A < _membersList.size(); A++) {
												targetInventory = _membersList.get(A).getInventory();

												if (_membersList.get(A).getLevel() >= Config.Adena_LevelMin
														&& _membersList.get(A).getLevel() <= Config.Adena_LevelMax) {
													아데나2 = (int) (아데나 * Config.Drop_Adena); // 기본지급
												} else if (_membersList.get(A).getLevel() >= Config.Adena_LevelMin1
														&& _membersList.get(A).getLevel() <= Config.Adena_LevelMax1) {
													아데나2 = (int) (아데나 * Config.Drop_Adena1); // 50프로
												} else { // 그외
													아데나2 = (int) (아데나 * Config.Drop_Adena2); // 80프로
												}

												if (_membersList.get(A).isBonusAdenaItem()) {
													아데나 *= 1.3;
												}

												item.setCount(아데나2 + 1);
												inventory.tradeItem(item, 아데나2, targetInventory);

												if (_membersList.get(A).RootMent)
													_membersList.get(A).sendPackets(new S_SystemMessage(String.format("파티:아데나를 [%d]만큼 분배합니다.", 아데나2)));
											}
										}
									} else if (player.RootMent) { // 솔로의 경우
										if (item != null && item.getItemId() == L1ItemId.ADENA) {
											int 아데나 = item.getCount();
											if (is_blessing_effect(pc, item))
												아데나 = (int) (item.getCount() * 1.3);

											if (player.getLevel() >= Config.Adena_LevelMin && player.getLevel() <= Config.Adena_LevelMax) {
												아데나 *= Config.Drop_Adena; // 기본지급
											} else if (player.getLevel() >= Config.Adena_LevelMin1 && player.getLevel() <= Config.Adena_LevelMax1) {
												아데나 *= Config.Drop_Adena1; // 50프로
											} else { // 80 이상유저들
												아데나 *= Config.Drop_Adena2; // 80프로
											}

											if (player.isBonusAdenaItem()) {
												아데나 *= 1.3;
											}

											item.setCount(아데나);
										}
										if (player.RootMent)
											player.sendPackets(new S_ServerMessage(143, npc.getName(), item.getLogName()));
									}
								}
							}
						} else {
							targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId());
						}
						break;
					}
				}
			} else { // Non 오토 루팅
				/** 악령의씨앗 무조건오토루팅 **/
				if (itemId == 810008) {
					return;
				}
				switch(itemId){
				case 830012:
				case 830013:
				case 830014:
				case 830015:
				case 830016:
				case 830017:
				case 830018:
				case 830019:
				case 830020:
				case 830021:
				case 830022:
				case 830023:
				case 830024:
				case 830025:
				case 830026:
				case 830027:
				case 830028:
				case 830029:
				case 830030:
				case 830031:
					System.out.println(String.format("%s 몬스터가 부적 드랍(부적:%s[%d])", npc.getName(), item.getName(), itemId));
					break;
				}
				
				item.setDropMobId(mobId);

				int maxHatePc = -1;
				int maxHate = -1;

				for (int j = hateList.size() - 1; j >= 0; j--) {
					if (maxHate < (Integer) hateList.get(j)) {
						maxHatePc = j;
						maxHate = (Integer) hateList.get(j);
					}
				}

				if (maxHatePc != -1 && acquisitorList.get(maxHatePc) instanceof L1PcInstance) {
					if (item.getItemId() == L1ItemId.ADENA) {
						if (acquisitorList.get(maxHatePc) instanceof L1PcInstance) {
							L1PcInstance maxPc = (L1PcInstance) acquisitorList.get(maxHatePc);
							int adena_count = item.getCount();
							int adena = MJAdenaPickupChain.getInstance().pickup(maxPc, item, adena_count);
							item.setCount(adena);
						}
					}
					item.startItemOwnerTimer((L1PcInstance) acquisitorList.get(maxHatePc));
				} else {
					item.startItemOwnerTimer(pc);
				}
				List<Integer> dirList = new ArrayList<Integer>();
				for (int j = 0; j < 8; j++) {
					dirList.add(j);
				}
				int x = 0;
				int y = 0;
				int dir = 0;
				do {
					if (dirList.size() == 0) {
						x = 0;
						y = 0;
						break;
					}
					randomInt = random.nextInt(dirList.size());
					dir = dirList.get(randomInt);
					dirList.remove(randomInt);
					switch (dir) {
					case 0:
						x = 0;
						y = -1;
						break;
					case 1:
						x = 1;
						y = -1;
						break;
					case 2:
						x = 1;
						y = 0;
						break;
					case 3:
						x = 1;
						y = 1;
						break;
					case 4:
						x = 0;
						y = 1;
						break;
					case 5:
						x = -1;
						y = 1;
						break;
					case 6:
						x = -1;
						y = 0;
						break;
					case 7:
						x = -1;
						y = -1;
						break;
					}
				} while (!npc.getMap().isPassable(npc.getX(), npc.getY(), dir));
				targetInventory = L1World.getInstance().getInventory(npc.getX() + x, npc.getY() + y, npc.getMapId());
			}
			inventory.tradeItem(item, item.getCount(), targetInventory);
	
		}
		npc.getLight().turnOnOffLight();
	}

	/** 보스몹 자동분배 **/
	private void Mapdrop(L1NpcInstance npc) {
		L1Inventory inventory = npc.getInventory();
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		L1PcInstance player;
		Random random = new Random();
		L1PcInstance acquisitor;
		ArrayList<L1PcInstance> acquisitorList = new ArrayList<L1PcInstance>();
		L1PcInstance[] pclist = L1World.getInstance().getAllPlayers3();
		for (L1PcInstance temppc : pclist) {
			if (temppc.getMapId() == npc.getMapId())
				acquisitorList.add(temppc);
		}
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);

			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
				item.setNowLighting(false);
			}
			acquisitor = acquisitorList.get(random.nextInt(acquisitorList.size()));
			if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
				targetInventory = acquisitor.getInventory();
				player = acquisitor;
				L1ItemInstance l1iteminstance = player.getInventory().findItemId(L1ItemId.ADENA); // 소지
				if (l1iteminstance != null && l1iteminstance.getCount() > 2000000000) {
					targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(),
							acquisitor.getMapId());
					player.sendPackets(new S_SystemMessage("소지 아데나 20억을 초과 하였습니다.(창고에 맡기고 다시찾으세요)"));
				} else {
					for (L1PcInstance temppc : acquisitorList) {
						temppc.sendPackets(
								new S_ServerMessage(813, npc.getName(), item.getLogName(), player.getName()));
					}
				}
			} else {
				targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(),
						acquisitor.getMapId()); // 가질 수
			}
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.getLight().turnOnOffLight();
	}

	/** 해당 몬스터 바닥에 아이템 떨굼 **/
	private void RanDrop1(L1NpcInstance npc) {// 기르타스 전용
		L1Inventory inventory = npc.getInventory();
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		Random random = new Random();
		L1Character acquisitor;
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);
			item.setDropMobId(npc.getNpcId());
			acquisitor = npc;
			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
				item.setNowLighting(false);
			}
			int targetX = acquisitor.getX() + random.nextInt(7) - 10;
			int targetY = acquisitor.getY() + random.nextInt(7) - 3;
			targetInventory = L1World.getInstance().getInventory(targetX, targetY, acquisitor.getMapId());
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.getLight().turnOnOffLight();
	}

	/** 해당 몬스터 바닥에 아이템 떨굼 **/
	private void RanDrop2(L1NpcInstance npc) {// 해상전 오징어 전용
		L1Inventory inventory = npc.getInventory();
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		Random random = new Random();
		L1Character acquisitor;
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);
			item.setDropMobId(npc.getNpcId());
			acquisitor = npc;
			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
				item.setNowLighting(false);
			}
			int targetX = 32795 + random.nextInt(4);
			int targetY = 32802 + random.nextInt(7);
			targetInventory = L1World.getInstance().getInventory(targetX, targetY, acquisitor.getMapId());
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.getLight().turnOnOffLight();
	}

	private void RanDrop3(L1NpcInstance npc) {
		L1Inventory inventory = npc.getInventory();
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		Random random = new Random();
		L1Character acquisitor;
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);
			item.setDropMobId(npc.getNpcId());
			acquisitor = npc;
			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
				item.setNowLighting(false);
			}
			int targetX = 32743 + random.nextInt(4);
			int targetY = 32862 + random.nextInt(7);
			targetInventory = L1World.getInstance().getInventory(targetX, targetY, acquisitor.getMapId());
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.getLight().turnOnOffLight();
	}

	private void huntEventDrop(L1NpcInstance npc) {
		if (Config.HUNT_EVENT) {
			int[] weapons = { 40087, 140087, 240087, 4100147 };
			int[] armors = { 40074, 140074, 240074, 4100147 };
			int chance = _random.nextInt(100) + 1;
			int rnd = _random.nextInt(2);
			int[] items = new int[4];
			int eventIemId;
			switch (rnd) {
			case 0:
				items = weapons;
				break;
			case 1:
				items = armors;
				break;
			}
			if (chance <= 90) {
				eventIemId = items[0];
			} else if (chance <= 60) {
				eventIemId = items[1];
			} else if (chance <= 40) {
				eventIemId = items[2];
			} else {
				eventIemId = items[3];
			}
			L1Inventory inventory = npc.getInventory();
			L1ItemInstance item = ItemTable.getInstance().createItem(eventIemId, false);
			item.setCount(1);
			inventory.storeItem(item);
			L1Character acquisitor;
			L1Inventory targetInventory = null;
			Random random = new Random();
			for (int i = inventory.getSize(); i > 0; i--) {
				item = inventory.getItems().get(0);
				acquisitor = npc;
				if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
					item.setNowLighting(false);
				}
				int targetX = acquisitor.getX() + random.nextInt(2);
				int targetY = acquisitor.getY() + random.nextInt(2);
				targetInventory = L1World.getInstance().getInventory(targetX, targetY, acquisitor.getMapId());
				inventory.tradeItem(item, item.getCount(), targetInventory);
			}
			npc.getLight().turnOnOffLight();
		}
	}

	// 몬스터 처치 시 아이템을 바닥에 골고루 흩뿌려줌//20221107
	private void RanDrop(L1NpcInstance npc, ArrayList<?> acquisitorList, ArrayList<?> hateList, L1PcInstance pc) {
		L1Inventory inventory = npc.getInventory();
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		Random random = new Random();
		L1Character acquisitor;
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);
			item.setDropMobId(npc.getNpcId());
			acquisitor = npc;
			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
				item.setNowLighting(false);
			}
			int targetX = acquisitor.getX() + random.nextInt(5) - 3;
			int targetY = acquisitor.getY() + random.nextInt(5) - 3;
			
			/*바닥에 뿌려줄때 지형물에 낑기지 않게 시작 */
			while (!npc.getMap().isPassable(targetX,targetY)) {
				targetX = acquisitor.getX() + random.nextInt(5) - 3;
				targetY = acquisitor.getY() + random.nextInt(5) - 3;
			}
			/*바닥에 뿌려줄때 지형물에 낑기지 않게 끝 */
			
			int maxHatePc = -1;
			int maxHate = -1;

			for (int j = hateList.size() - 1; j >= 0; j--) {
				if (maxHate < (Integer) hateList.get(j)) {
					maxHatePc = j;
					maxHate = (Integer) hateList.get(j);
				}
			}

			if (maxHatePc != -1 && acquisitorList.get(maxHatePc) instanceof L1PcInstance) {
				if (item.getItemId() == L1ItemId.ADENA) {
					if (acquisitorList.get(maxHatePc) instanceof L1PcInstance) {
						L1PcInstance maxPc = (L1PcInstance) acquisitorList.get(maxHatePc);
						int adena_count = item.getCount();
						int adena = MJAdenaPickupChain.getInstance().pickup(maxPc, item, adena_count);
						item.setCount(adena);
					}
				}
				item.startItemOwnerTimer((L1PcInstance) acquisitorList.get(maxHatePc));
			} else {
				item.startItemOwnerTimer(pc);
			}
			
			targetInventory = L1World.getInstance().getInventory(targetX, targetY, acquisitor.getMapId());
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.getLight().turnOnOffLight();
	}

	private static boolean is_blessing_effect(L1PcInstance pc, L1ItemInstance item) {
		if (item.isGiveItem())
			return false;

		if (pc.getInventory().checkItem(4100121)) {//4100121
			int mapId = pc.getMapId();

			if (mapId >= 0)
				return true;

			/*
			 * if(mapId >= 15410 && mapId <= 15440) return true; if(mapId >= 53
			 * && mapId <= 56) return true; if(mapId >= 101 && mapId <= 110)
			 * return true; if(mapId >= 12852 && mapId <= 12862) return true;
			 * if(mapId >= 15403 && mapId <= 15404) return true; if(mapId >=
			 * 1708 && mapId <= 1709) return true; if(mapId == 624) return true;
			 * if(mapId == 479) return true;
			 */
		}
		return false;
	}
}
