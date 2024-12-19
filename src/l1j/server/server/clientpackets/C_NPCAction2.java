package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import java.util.Random;

import l1j.server.Config;
import l1j.server.IndunSystem.FanstasyIsland.FantasyIslandSystem;
import l1j.server.IndunSystem.Orim.OrimController;
import l1j.server.IndunSystem.Training.BossTrainingSystem;
import l1j.server.IndunSystem.ValakasRoom.ValakasReadyStart;
import l1j.server.IndunSystem.ValakasRoom.ValakasRoomSystem;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.ActionCodes;
import l1j.server.server.Controller.잊섬Controller;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CataInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

public class C_NPCAction2 {

	private static C_NPCAction2 _instance;

	private static Random _random = new Random(System.nanoTime());

	public static C_NPCAction2 getInstance() {
		if (_instance == null) {
			_instance = new C_NPCAction2();
		}
		return _instance;
	}

	int[] materials = null;
	int[] counts = null;

	public String NpcAction(L1PcInstance pc, L1Object obj, String s, String htmlid) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		try {
//			if (npcid == 200201) {// 조우의 돌골렘
//				if (s.equalsIgnoreCase("A")) {
//					if (pc.getInventory().checkEnchantItem(5, 7, 1) && pc.getInventory().checkEnchantItem(6, 7, 1)
//							&& pc.getInventory().checkItem(41246, 30000)) {
//						pc.getInventory().consumeEnchantItem(5, 7, 1);
//						pc.getInventory().consumeEnchantItem(6, 7, 1);
//						pc.getInventory().consumeItem(41246, 3000);
//
//						pc.getInventory().storeItem(602, 1);
//						htmlid = "joegolem9";
//					} else {
//						htmlid = "joegolem15";
//					}
//				}
//				// 광풍의 도끼
//				if (s.equalsIgnoreCase("B")) {
//					if (pc.getInventory().checkEnchantItem(145, 7, 1) && pc.getInventory().checkEnchantItem(148, 7, 1)
//							&& pc.getInventory().checkItem(41246, 30000)) {
//						pc.getInventory().consumeEnchantItem(145, 7, 1);
//						pc.getInventory().consumeEnchantItem(148, 7, 1);
//						pc.getInventory().consumeItem(41246, 30000);
//
//						pc.getInventory().storeItem(605, 1);
//						htmlid = "joegolem10";
//					} else {
//						htmlid = "joegolem15";
//					}
//				}
//				// 파멸의 대검
//				if (s.equalsIgnoreCase("C")) {
//					if (pc.getInventory().checkEnchantItem(52, 7, 1) && pc.getInventory().checkEnchantItem(64, 7, 1)
//							&& pc.getInventory().checkItem(41246, 30000)) {
//						pc.getInventory().consumeEnchantItem(52, 7, 1);
//						pc.getInventory().consumeEnchantItem(64, 7, 1);
//						pc.getInventory().consumeItem(41246, 30000);
//
//						pc.getInventory().storeItem(601, 1);
//						htmlid = "joegolem11";
//					} else {
//						htmlid = "joegolem15";
//					}
//				}
//				// 아크메이지의 지팡이
//				if (s.equalsIgnoreCase("D")) {
//					if (pc.getInventory().checkEnchantItem(125, 7, 1) && pc.getInventory().checkEnchantItem(129, 7, 1)
//							&& pc.getInventory().checkItem(41246, 30000)) {
//						pc.getInventory().consumeEnchantItem(125, 7, 1);
//						pc.getInventory().consumeEnchantItem(129, 7, 1);
//						pc.getInventory().consumeItem(41246, 30000);
//
//						pc.getInventory().storeItem(603, 1);
//						htmlid = "joegolem12";
//					} else {
//						htmlid = "joegolem15";
//					}
//				}
//				// 혹한의 창
//				if (s.equalsIgnoreCase("E")) {
//					if (pc.getInventory().checkEnchantItem(99, 7, 1) && pc.getInventory().checkEnchantItem(104, 7, 1)
//							&& pc.getInventory().checkItem(41246, 30000)) {
//						pc.getInventory().consumeEnchantItem(99, 7, 1);
//						pc.getInventory().consumeEnchantItem(104, 7, 1);
//						pc.getInventory().consumeItem(41246, 30000);
//
//						pc.getInventory().storeItem(604, 1);
//						htmlid = "joegolem13";
//					} else {
//						htmlid = "joegolem15";
//					}
//				}
//				// 뇌신검
//				if (s.equalsIgnoreCase("F")) {
//					if (pc.getInventory().checkEnchantItem(32, 7, 1) && pc.getInventory().checkEnchantItem(42, 7, 1)
//							&& pc.getInventory().checkItem(41246, 30000)) {
//						pc.getInventory().consumeEnchantItem(32, 7, 1);
//						pc.getInventory().consumeEnchantItem(42, 7, 1);
//						pc.getInventory().consumeItem(41246, 30000);
//
//						pc.getInventory().storeItem(600, 1);
//						htmlid = "joegolem14";
//					} else {
//						htmlid = "joegolem15";
//					}
//				}
			if (npcid == 200201) {// 조우의 돌골렘
				// 마법인형: 할파스
				if (s.equalsIgnoreCase("G")) {
				if (pc.getInventory().checkItem(141400, 1) // 마법인형: 할파스
				 && pc.getInventory().checkItem(141401, 1) // 마법인형: 아우라키아
				 && pc.getInventory().checkItem(141402, 1)) { // 마법인형: 베히모스
						pc.getInventory().consumeItem(141400, 1);
						pc.getInventory().consumeItem(141401, 1);
						pc.getInventory().consumeItem(141402, 1);
						pc.getInventory().storeItem(141403, 1); // 마법인형: 기르타스
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 마법인형: 기르타스 제작에 성공했습니다."));
						L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 마법인형: 기르타스 제작에 성공했습니다."));
						htmlid = "joegolem22";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 마법인형: 할파스
				if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(4100007, 1) // 마법인형: 안타라스
							&& pc.getInventory().checkItem(4100010, 1)) { // 마법인형: 발라카스
						pc.getInventory().consumeItem(4100007, 1);
						pc.getInventory().consumeItem(4100010, 1);
						pc.getInventory().storeItem(141400, 1); // 마법인형: 할파스
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 마법인형: 할파스 제작에 성공했습니다."));
						L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 마법인형: 할파스 제작에 성공했습니다."));
						htmlid = "joegolem9";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 마법인형: 아우라키아
				if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkItem(4100008, 1) // 마법인형: 파푸리온
					 && pc.getInventory().checkItem(4100010, 1)) { // 마법인형: 발라카스
						pc.getInventory().consumeItem(4100008, 1);
						pc.getInventory().consumeItem(4100010, 1);
						pc.getInventory().storeItem(141401, 1); // 마법인형: 아우라키아
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 마법인형: 아우라키아 제작에 성공했습니다."));
						L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 마법인형: 아우라키아 제작에 성공했습니다."));
						htmlid = "joegolem10";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 마법인형: 베히모스
				if (s.equalsIgnoreCase("C")) {
					if (pc.getInventory().checkItem(4100009, 1) // 마법인형: 린드비오르
					 && pc.getInventory().checkItem(4100010, 1)) { // 마법인형: 발라카스
						pc.getInventory().consumeItem(4100009, 1);
						pc.getInventory().consumeItem(4100010, 1);
						pc.getInventory().storeItem(141402, 1); // 마법인형: 베히모스
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 마법인형: 베히모스 제작에 성공했습니다."));
						L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 마법인형: 베히모스 제작에 성공했습니다."));
						htmlid = "joegolem11";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 할파스의 완력
				if (s.equalsIgnoreCase("D")) {
					if (pc.getInventory().checkEnchantItem(22196, 9, 1) // 안타라스의 완력
					 && pc.getInventory().checkEnchantItem(22200, 9, 1) // 파푸리온의 완력
					 && pc.getInventory().checkEnchantItem(22204, 9, 1) // 린드비오르의 완력
					 && pc.getInventory().checkEnchantItem(22208, 9, 1) // 발라카스의 완력
					 && pc.getInventory().checkItem(40074, 1000)) { // 갑옷 마법 주문서
						
						pc.getInventory().consumeEnchantItem(22196, 9, 1); // 안타라스의 완력
						pc.getInventory().consumeEnchantItem(22200, 9, 1); // 파푸리온의 완력
						pc.getInventory().consumeEnchantItem(22204, 9, 1); // 린드비오르의 완력
						pc.getInventory().consumeEnchantItem(22208, 9, 1); // 발라카스의 완력
						pc.getInventory().consumeItem(40074, 1000); // 갑옷 마법 주문서
						
						pc.getInventory().storeItem(111137, 1); // 할파스의 완력
						
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 할파스의 완력 제작에 성공했습니다."));
						L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 할파스의 완력 제작에 성공했습니다."));
						htmlid = "joegolem12";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 할파스의 마력
				if (s.equalsIgnoreCase("E")) {
					if (pc.getInventory().checkEnchantItem(22199, 9, 1) // 안타라스의 마력
					 && pc.getInventory().checkEnchantItem(22203, 9, 1) // 파푸리온의 마력
					 && pc.getInventory().checkEnchantItem(22207, 9, 1) // 린드비오르의 마력
					 && pc.getInventory().checkEnchantItem(22211, 9, 1) // 발라카스의 마력
					 && pc.getInventory().checkItem(40074, 1000)) { // 갑옷 마법 주문서
								
						pc.getInventory().consumeEnchantItem(22199, 9, 1); // 안타라스의 마력
						pc.getInventory().consumeEnchantItem(22203, 9, 1); // 파푸리온의 마력
						pc.getInventory().consumeEnchantItem(22207, 9, 1); // 린드비오르의 마력
						pc.getInventory().consumeEnchantItem(22211, 9, 1); // 발라카스의 마력
						pc.getInventory().consumeItem(40074, 1000); // 갑옷 마법 주문서
						
						pc.getInventory().storeItem(111140, 1); // 할파스의 마력
								
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 할파스의 마력 제작에 성공했습니다."));
						L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 할파스의 마력 제작에 성공했습니다."));
						htmlid = "joegolem13";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 할파스의 예지력
				if (s.equalsIgnoreCase("F")) {
					if (pc.getInventory().checkEnchantItem(22197, 9, 1) // 안타라스의 예지력
					 && pc.getInventory().checkEnchantItem(22201, 9, 1) // 파푸리온의 예지력
					 && pc.getInventory().checkEnchantItem(22205, 9, 1) // 린드비오르의 예지력
					 && pc.getInventory().checkEnchantItem(22209, 9, 1) // 발라카스의 예지력
					 && pc.getInventory().checkItem(40074, 1000)) { // 갑옷 마법 주문서
										
						pc.getInventory().consumeEnchantItem(22197, 9, 1); // 안타라스의 예지력
						pc.getInventory().consumeEnchantItem(22201, 9, 1); // 파푸리온의 예지력
						pc.getInventory().consumeEnchantItem(22205, 9, 1); // 린드비오르의 예지력
						pc.getInventory().consumeEnchantItem(22209, 9, 1); // 발라카스의 예지력
						pc.getInventory().consumeItem(40074, 1000); // 갑옷 마법 주문서
								
						pc.getInventory().storeItem(111141, 1); // 할파스의 예지력
						L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"누군가가 할파스의 예지력 제작에 성공했습니다."));
						L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("누군가가 할파스의 예지력 제작에 성공했습니다."));
						htmlid = "joegolem14";
					} else {
						htmlid = "joegolem15";
					}
				}

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 200202) {// 마감 매입^아인하사드의 조수
					if (s.equalsIgnoreCase("a4")) {
						if (pc.getInventory().checkEnchantItem(61, 4, 1)) { // +4 진명황의 집행검
							pc.getInventory().consumeEnchantItem(61, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("a5")) {
						if (pc.getInventory().checkEnchantItem(61, 5, 1)) { // +5 진명황의 집행검
							pc.getInventory().consumeEnchantItem(61, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("a6")) {
						if (pc.getInventory().checkEnchantItem(61, 6, 1)) { // +6 진명황의 집행검
							pc.getInventory().consumeEnchantItem(61, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("a7")) {
						if (pc.getInventory().checkEnchantItem(61, 7, 1)) { // +7 진명황의 집행검
							pc.getInventory().consumeEnchantItem(61, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("a8")) {
						if (pc.getInventory().checkEnchantItem(61, 8, 1)) { // +8 진명황의 집행검
							pc.getInventory().consumeEnchantItem(61, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("a9")) {
						if (pc.getInventory().checkEnchantItem(61, 9, 1)) { // +9 진명황의 집행검
							pc.getInventory().consumeEnchantItem(61, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("a10")) {
						if (pc.getInventory().checkEnchantItem(61, 10, 1)) { // +10 진명황의 집행검
							pc.getInventory().consumeEnchantItem(61, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					if (s.equalsIgnoreCase("b4")) {
						if (pc.getInventory().checkEnchantItem(202011, 4, 1)) { // +4 가이아의 격노
							pc.getInventory().consumeEnchantItem(202011, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b5")) {
						if (pc.getInventory().checkEnchantItem(202011, 5, 1)) { // +5 가이아의 격노
							pc.getInventory().consumeEnchantItem(202011, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b6")) {
						if (pc.getInventory().checkEnchantItem(202011, 6, 1)) { // +6 가이아의 격노
							pc.getInventory().consumeEnchantItem(202011, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b7")) {
						if (pc.getInventory().checkEnchantItem(202011, 7, 1)) { // +7 가이아의 격노
							pc.getInventory().consumeEnchantItem(202011, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b8")) {
						if (pc.getInventory().checkEnchantItem(202011, 8, 1)) { // +8 가이아의 격노
							pc.getInventory().consumeEnchantItem(202011, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b9")) {
						if (pc.getInventory().checkEnchantItem(202011, 9, 1)) { // +9 가이아의 격노
							pc.getInventory().consumeEnchantItem(202011, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b10")) {
						if (pc.getInventory().checkEnchantItem(202011, 10, 1)) { // +10 가이아의 격노
							pc.getInventory().consumeEnchantItem(202011, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("c4")) {
						if (pc.getInventory().checkEnchantItem(66, 4, 1)) { // +4 드래곤 슬레이어
							pc.getInventory().consumeEnchantItem(66, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("c5")) {
						if (pc.getInventory().checkEnchantItem(66, 5, 1)) { // +5 드래곤 슬레이어
							pc.getInventory().consumeEnchantItem(66, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("c6")) {
						if (pc.getInventory().checkEnchantItem(66, 6, 1)) { // +6 드래곤 슬레이어
							pc.getInventory().consumeEnchantItem(66, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("c7")) {
						if (pc.getInventory().checkEnchantItem(66, 7, 1)) { // +7 드래곤 슬레이어
							pc.getInventory().consumeEnchantItem(66, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("c8")) {
						if (pc.getInventory().checkEnchantItem(66, 8, 1)) { // +8 드래곤 슬레이어
							pc.getInventory().consumeEnchantItem(66, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("c9")) {
						if (pc.getInventory().checkEnchantItem(66, 9, 1)) { // +9 드래곤 슬레이어
							pc.getInventory().consumeEnchantItem(66, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("c10")) {
						if (pc.getInventory().checkEnchantItem(66, 10, 1)) { // +10 드래곤 슬레이어
							pc.getInventory().consumeEnchantItem(66, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("d4")) {
						if (pc.getInventory().checkEnchantItem(134, 4, 1)) { // +4 수정결정체 지팡이
							pc.getInventory().consumeEnchantItem(134, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("d5")) {
						if (pc.getInventory().checkEnchantItem(134, 5, 1)) { // +5 수정결정체 지팡이
							pc.getInventory().consumeEnchantItem(134, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("d6")) {
						if (pc.getInventory().checkEnchantItem(134, 6, 1)) { // +6 수정결정체 지팡이
							pc.getInventory().consumeEnchantItem(134, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("d7")) {
						if (pc.getInventory().checkEnchantItem(134, 7, 1)) { // +7 수정결정체 지팡이
							pc.getInventory().consumeEnchantItem(134, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("d8")) {
						if (pc.getInventory().checkEnchantItem(134, 8, 1)) { // +8 수정결정체 지팡이
							pc.getInventory().consumeEnchantItem(134, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("d9")) {
						if (pc.getInventory().checkEnchantItem(134, 9, 1)) { // +9 수정결정체 지팡이
							pc.getInventory().consumeEnchantItem(134, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("d10")) {
						if (pc.getInventory().checkEnchantItem(134, 10, 1)) { // +10 수정결정체 지팡이
							pc.getInventory().consumeEnchantItem(134, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("e4")) {
						if (pc.getInventory().checkEnchantItem(202014, 4, 1)) { // +4 타이탄의 분노
							pc.getInventory().consumeEnchantItem(202014, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("e5")) {
						if (pc.getInventory().checkEnchantItem(202014, 5, 1)) { // +5 타이탄의 분노
							pc.getInventory().consumeEnchantItem(202014, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("e6")) {
						if (pc.getInventory().checkEnchantItem(202014, 6, 1)) { // +6 타이탄의 분노
							pc.getInventory().consumeEnchantItem(202014, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("e7")) {
						if (pc.getInventory().checkEnchantItem(202014, 7, 1)) { // +7 타이탄의 분노
							pc.getInventory().consumeEnchantItem(202014, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("e8")) {
						if (pc.getInventory().checkEnchantItem(202014, 8, 1)) { // +8 타이탄의 분노
							pc.getInventory().consumeEnchantItem(202014, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("e9")) {
						if (pc.getInventory().checkEnchantItem(202014, 9, 1)) { // +9 타이탄의 분노
							pc.getInventory().consumeEnchantItem(202014, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("e10")) {
						if (pc.getInventory().checkEnchantItem(202014, 10, 1)) { // +10 타이탄의 분노
							pc.getInventory().consumeEnchantItem(202014, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("f4")) {
						if (pc.getInventory().checkEnchantItem(86, 4, 1)) { // +4 붉은그림자의 이도류
							pc.getInventory().consumeEnchantItem(86, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}		
					if (s.equalsIgnoreCase("f5")) {
						if (pc.getInventory().checkEnchantItem(86, 5, 1)) { // +5 붉은그림자의 이도류
							pc.getInventory().consumeEnchantItem(86, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("f6")) {
						if (pc.getInventory().checkEnchantItem(86, 6, 1)) { // +6 붉은그림자의 이도류
							pc.getInventory().consumeEnchantItem(86, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("f7")) {
						if (pc.getInventory().checkEnchantItem(86, 7, 1)) { // +7 붉은그림자의 이도류
							pc.getInventory().consumeEnchantItem(86, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("f8")) {
						if (pc.getInventory().checkEnchantItem(86, 8, 1)) { // +8 붉은그림자의 이도류
							pc.getInventory().consumeEnchantItem(86, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("f9")) {
						if (pc.getInventory().checkEnchantItem(86, 9, 1)) { // +9 붉은그림자의 이도류
							pc.getInventory().consumeEnchantItem(86, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("f10")) {
						if (pc.getInventory().checkEnchantItem(86, 10, 1)) { // +10 붉은그림자의 이도류
							pc.getInventory().consumeEnchantItem(86, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("g4")) {
						if (pc.getInventory().checkEnchantItem(202013, 4, 1)) { // +4 크로노스의 공포
							pc.getInventory().consumeEnchantItem(202013, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}		
					if (s.equalsIgnoreCase("g5")) {
						if (pc.getInventory().checkEnchantItem(202013, 5, 1)) { // +5 크로노스의 공포
							pc.getInventory().consumeEnchantItem(202013, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("g6")) {
						if (pc.getInventory().checkEnchantItem(202013, 6, 1)) { // +6 크로노스의 공포
							pc.getInventory().consumeEnchantItem(202013, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("g7")) {
						if (pc.getInventory().checkEnchantItem(202013, 7, 1)) { // +7 크로노스의 공포
							pc.getInventory().consumeEnchantItem(202013, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("g8")) {
						if (pc.getInventory().checkEnchantItem(202013, 8, 1)) { // +8 크로노스의 공포
							pc.getInventory().consumeEnchantItem(202013, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("g9")) {
						if (pc.getInventory().checkEnchantItem(202013, 9, 1)) { // +9 크로노스의 공포
							pc.getInventory().consumeEnchantItem(202013, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("g10")) {
						if (pc.getInventory().checkEnchantItem(202013, 10, 1)) { // +10 크로노스의 공포
							pc.getInventory().consumeEnchantItem(202013, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("h4")) {
						if (pc.getInventory().checkEnchantItem(202012, 4, 1)) { // +4 히페리온의 절망
							pc.getInventory().consumeEnchantItem(202012, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}		
					if (s.equalsIgnoreCase("h5")) {
						if (pc.getInventory().checkEnchantItem(202012, 5, 1)) { // +5 히페리온의 절망
							pc.getInventory().consumeEnchantItem(202012, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("h6")) {
						if (pc.getInventory().checkEnchantItem(202012, 6, 1)) { // +6 히페리온의 절망
							pc.getInventory().consumeEnchantItem(202012, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("h7")) {
						if (pc.getInventory().checkEnchantItem(202012, 7, 1)) { // +7 히페리온의 절망
							pc.getInventory().consumeEnchantItem(202012, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("h8")) {
						if (pc.getInventory().checkEnchantItem(202012, 8, 1)) { // +8 히페리온의 절망
							pc.getInventory().consumeEnchantItem(202012, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("h9")) {
						if (pc.getInventory().checkEnchantItem(202012, 9, 1)) { // +9 히페리온의 절망
							pc.getInventory().consumeEnchantItem(202012, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("h10")) {
						if (pc.getInventory().checkEnchantItem(202012, 10, 1)) { // +10 히페리온의 절망
							pc.getInventory().consumeEnchantItem(202012, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("i4")) {
						if (pc.getInventory().checkEnchantItem(12, 4, 1)) { // +4 바람칼날의 단검
							pc.getInventory().consumeEnchantItem(12, 4, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인 10,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}		
					if (s.equalsIgnoreCase("i5")) {
						if (pc.getInventory().checkEnchantItem(12, 5, 1)) { // +5 바람칼날의 단검
							pc.getInventory().consumeEnchantItem(12, 5, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인 15,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("i6")) {
						if (pc.getInventory().checkEnchantItem(12, 6, 1)) { // +6 바람칼날의 단검
							pc.getInventory().consumeEnchantItem(12, 6, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인 20,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("i7")) {
						if (pc.getInventory().checkEnchantItem(12, 7, 1)) { // +7 바람칼날의 단검
							pc.getInventory().consumeEnchantItem(12, 7, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인 30,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("i8")) {
						if (pc.getInventory().checkEnchantItem(12, 8, 1)) { // +8 바람칼날의 단검
							pc.getInventory().consumeEnchantItem(12, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인 50,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("i9")) {
						if (pc.getInventory().checkEnchantItem(12, 9, 1)) { // +9 바람칼날의 단검
							pc.getInventory().consumeEnchantItem(12, 9, 1);
							pc.getInventory().storeItem(3000183, 70000); // 다음차 이월 코인 70,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("i10")) {
						if (pc.getInventory().checkEnchantItem(12, 10, 1)) { // +10 바람칼날의 단검
							pc.getInventory().consumeEnchantItem(12, 10, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인 100,000개
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					/**
					 * 할파스의 완력, 마력, 예지력
					 * */
					// 할파스의 완력
					if (s.equalsIgnoreCase("halpas-str-0")) {
						if (pc.getInventory().checkEnchantItem(111137, 0, 1)) {  // +0 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 0, 1);
							pc.getInventory().storeItem(3000185, 120000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-1")) {
						if (pc.getInventory().checkEnchantItem(111137, 1, 1)) {  // +1 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 1, 1);
							pc.getInventory().storeItem(3000185, 130000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-2")) {
						if (pc.getInventory().checkEnchantItem(111137, 2, 1)) {  // +2 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 2, 1);
							pc.getInventory().storeItem(3000185, 140000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-3")) {
						if (pc.getInventory().checkEnchantItem(111137, 3, 1)) {  // +3 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 3, 1);
							pc.getInventory().storeItem(3000185, 150000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-4")) {
						if (pc.getInventory().checkEnchantItem(111137, 4, 1)) {  // +4 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 4, 1);
							pc.getInventory().storeItem(3000185, 160000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-5")) {
						if (pc.getInventory().checkEnchantItem(111137, 5, 1)) {  // +5 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 5, 1);
							pc.getInventory().storeItem(3000185, 170000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-6")) {
						if (pc.getInventory().checkEnchantItem(111137, 6, 1)) {  // +6 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 6, 1);
							pc.getInventory().storeItem(3000185, 180000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-7")) {
						if (pc.getInventory().checkEnchantItem(111137, 7, 1)) {  // +7 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 7, 1);
							pc.getInventory().storeItem(3000185, 190000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-8")) {
						if (pc.getInventory().checkEnchantItem(111137, 8, 1)) {  // +8 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 8, 1);
							pc.getInventory().storeItem(3000185, 200000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-9")) {
						if (pc.getInventory().checkEnchantItem(111137, 9, 1)) {  // +9 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 9, 1);
							pc.getInventory().storeItem(3000185, 250000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-str-10")) {
						if (pc.getInventory().checkEnchantItem(111137, 10, 1)) {  // +10 할파스의 완력
							pc.getInventory().consumeEnchantItem(111137, 10, 1);
							pc.getInventory().storeItem(3000185, 300000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					// 할파스의 마력
					if (s.equalsIgnoreCase("halpas-int-0")) {
						if (pc.getInventory().checkEnchantItem(111140, 0, 1)) {  // +0 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 0, 1);
							pc.getInventory().storeItem(3000185, 120000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-1")) {
						if (pc.getInventory().checkEnchantItem(111140, 1, 1)) {  // +1 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 1, 1);
							pc.getInventory().storeItem(3000185, 130000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-2")) {
						if (pc.getInventory().checkEnchantItem(111140, 2, 1)) {  // +2 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 2, 1);
							pc.getInventory().storeItem(3000185, 140000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-3")) {
						if (pc.getInventory().checkEnchantItem(111140, 3, 1)) {  // +3 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 3, 1);
							pc.getInventory().storeItem(3000185, 150000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-4")) {
						if (pc.getInventory().checkEnchantItem(111140, 4, 1)) {  // +4 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 4, 1);
							pc.getInventory().storeItem(3000185, 160000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-5")) {
						if (pc.getInventory().checkEnchantItem(111140, 5, 1)) {  // +5 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 5, 1);
							pc.getInventory().storeItem(3000185, 170000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-6")) {
						if (pc.getInventory().checkEnchantItem(111140, 6, 1)) {  // +6 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 6, 1);
							pc.getInventory().storeItem(3000185, 180000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-7")) {
						if (pc.getInventory().checkEnchantItem(111140, 7, 1)) {  // +7 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 7, 1);
							pc.getInventory().storeItem(3000185, 190000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-8")) {
						if (pc.getInventory().checkEnchantItem(111140, 8, 1)) {  // +8 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 8, 1);
							pc.getInventory().storeItem(3000185, 200000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-9")) {
						if (pc.getInventory().checkEnchantItem(111140, 9, 1)) {  // +9 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 9, 1);
							pc.getInventory().storeItem(3000185, 250000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-int-10")) {
						if (pc.getInventory().checkEnchantItem(111140, 10, 1)) {  // +10 할파스의 마력
							pc.getInventory().consumeEnchantItem(111140, 10, 1);
							pc.getInventory().storeItem(3000185, 300000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					// 할파스의 예지력
					if (s.equalsIgnoreCase("halpas-dex-0")) {
						if (pc.getInventory().checkEnchantItem(111141, 0, 1)) {  // +0 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 0, 1);
							pc.getInventory().storeItem(3000185, 120000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-1")) {
						if (pc.getInventory().checkEnchantItem(111141, 1, 1)) {  // +1 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 1, 1);
							pc.getInventory().storeItem(3000185, 130000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-2")) {
						if (pc.getInventory().checkEnchantItem(111141, 2, 1)) {  // +2 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 2, 1);
							pc.getInventory().storeItem(3000185, 140000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-3")) {
						if (pc.getInventory().checkEnchantItem(111141, 3, 1)) {  // +3 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 3, 1);
							pc.getInventory().storeItem(3000185, 150000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-4")) {
						if (pc.getInventory().checkEnchantItem(111141, 4, 1)) {  // +4 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 4, 1);
							pc.getInventory().storeItem(3000185, 160000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-5")) {
						if (pc.getInventory().checkEnchantItem(111141, 5, 1)) {  // +5 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 5, 1);
							pc.getInventory().storeItem(3000185, 170000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-6")) {
						if (pc.getInventory().checkEnchantItem(111141, 6, 1)) {  // +6 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 6, 1);
							pc.getInventory().storeItem(3000185, 180000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-7")) {
						if (pc.getInventory().checkEnchantItem(111141, 7, 1)) {  // +7 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 7, 1);
							pc.getInventory().storeItem(3000185, 190000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-8")) {
						if (pc.getInventory().checkEnchantItem(111141, 8, 1)) {  // +8 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 8, 1);
							pc.getInventory().storeItem(3000185, 200000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-9")) {
						if (pc.getInventory().checkEnchantItem(111141, 9, 1)) {  // +9 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 9, 1);
							pc.getInventory().storeItem(3000185, 250000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("halpas-dex-10")) {
						if (pc.getInventory().checkEnchantItem(111141, 10, 1)) {  // +10 할파스의 예지력
							pc.getInventory().consumeEnchantItem(111141, 10, 1);
							pc.getInventory().storeItem(3000185, 300000); // 다음차 이월 코인 (B)
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}

					/**
					 * 스냅퍼/룸티스/수호문장/수호휘장
					 * */
					if (s.equalsIgnoreCase("warrior-guardian-badge8")) {
						if (pc.getInventory().checkEnchantItem(900081, 8, 1)) {  // +8 투사의 수호 휘장
							pc.getInventory().consumeEnchantItem(900081, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("archer-guardian-badge8")) {
						if (pc.getInventory().checkEnchantItem(900082, 8, 1)) {  // +8 명궁의 수호 휘장
							pc.getInventory().consumeEnchantItem(900082, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("sage-guardian-badge8")) {
						if (pc.getInventory().checkEnchantItem(900083, 8, 1)) {  // +8 현자의 수호 휘장
							pc.getInventory().consumeEnchantItem(900083, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("warrior-guardian-rune8")) {
						if (pc.getInventory().checkEnchantItem(900124, 8, 1)) {  // +8 투사의 수호 문장
							pc.getInventory().consumeEnchantItem(900124, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("archer-guardian-rune8")) {
						if (pc.getInventory().checkEnchantItem(900125, 8, 1)) {  // +8 명궁의 수호 문장
							pc.getInventory().consumeEnchantItem(900125, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("sage-guardian-rune8")) {
						if (pc.getInventory().checkEnchantItem(900126, 8, 1)) {  // +8 현자의 수호 문장
							pc.getInventory().consumeEnchantItem(900126, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-snapper-recovery8")) {
						if (pc.getInventory().checkEnchantItem(222330, 8, 1)) {  // +8 축복받은 스냅퍼의 회복 반지
							pc.getInventory().consumeEnchantItem(222330, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-snapper-concentration8")) {
						if (pc.getInventory().checkEnchantItem(222331, 8, 1)) {  // +8 축복받은 스냅퍼의 집중 반지
							pc.getInventory().consumeEnchantItem(222331, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-snapper-health8")) {
						if (pc.getInventory().checkEnchantItem(222332, 8, 1)) {  // +8 축복받은 스냅퍼의 체력 반지
							pc.getInventory().consumeEnchantItem(222332, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-snapper-mana8")) {
						if (pc.getInventory().checkEnchantItem(222333, 8, 1)) {  // +8 축복받은 스냅퍼의 마나 반지
							pc.getInventory().consumeEnchantItem(222333, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-snapper-resistance8")) {
						if (pc.getInventory().checkEnchantItem(222334, 8, 1)) {  // +8 축복받은 스냅퍼의 마법저항 반지
							pc.getInventory().consumeEnchantItem(222334, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-snapper-wisdom8")) {
						if (pc.getInventory().checkEnchantItem(222335, 8, 1)) {  // +8 축복받은 스냅퍼의 지혜 반지
							pc.getInventory().consumeEnchantItem(222335, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-snapper-warrior8")) {
						if (pc.getInventory().checkEnchantItem(222336, 8, 1)) {  // +8 축복받은 스냅퍼의 용사 반지
							pc.getInventory().consumeEnchantItem(222336, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-roomtis-red8")) {
						if (pc.getInventory().checkEnchantItem(222337, 8, 1)) {  // +8 축복받은 룸티스의 붉은빛 귀걸이
							pc.getInventory().consumeEnchantItem(222337, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-roomtis-green8")) {
						if (pc.getInventory().checkEnchantItem(222338, 8, 1)) {  // +8 축복받은 룸티스의 푸른빛 귀걸이
							pc.getInventory().consumeEnchantItem(222338, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-roomtis-purple8")) {
						if (pc.getInventory().checkEnchantItem(222339, 8, 1)) {  // +8 축복받은 룸티스의 보랏빛 귀걸이
							pc.getInventory().consumeEnchantItem(222339, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("b-roomtis-black8")) {
						if (pc.getInventory().checkEnchantItem(222341, 8, 1)) {  // +8 축복받은 룸티스의 검은빛 귀걸이
							pc.getInventory().consumeEnchantItem(222341, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					/**
					 * 속성 귀걸이
					 * */
					if (s.equalsIgnoreCase("water-element-ear8")) {
						if (pc.getInventory().checkEnchantItem(900114, 8, 1)) {  // +8 수령의 귀걸이
							pc.getInventory().consumeEnchantItem(900114, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("wind-element-ear8")) {
						if (pc.getInventory().checkEnchantItem(900113, 8, 1)) {  // +8 풍령의 귀걸이
							pc.getInventory().consumeEnchantItem(900113, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("earth-element-ear8")) {
						if (pc.getInventory().checkEnchantItem(900115, 8, 1)) {  // +8 지령의 귀걸이
							pc.getInventory().consumeEnchantItem(900115, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("fire-element-ear8")) {
						if (pc.getInventory().checkEnchantItem(900112, 8, 1)) {  // +8 화령의 귀걸이
							pc.getInventory().consumeEnchantItem(900112, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					
					/**
					 * 실프티/드슬각반
					 * */
					if (s.equalsIgnoreCase("sylph8")) {
						if (pc.getInventory().checkEnchantItem(900019, 8, 1)) {  // +8 실프의 티셔츠
							pc.getInventory().consumeEnchantItem(900019, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("sylph9")) {
						if (pc.getInventory().checkEnchantItem(900019, 9, 1)) {  // +9 실프의 티셔츠
							pc.getInventory().consumeEnchantItem(900019, 9, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("sylph10")) {
						if (pc.getInventory().checkEnchantItem(900019, 10, 1)) {  // +10 실프의 티셔츠
							pc.getInventory().consumeEnchantItem(900019, 10, 1);
							pc.getInventory().storeItem(3000183, 200000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					if (s.equalsIgnoreCase("dragonslayer-leg8")) {
						if (pc.getInventory().checkEnchantItem(900200, 8, 1)) {  // +8 드래곤 슬레이어의 각반
							pc.getInventory().consumeEnchantItem(900200, 8, 1);
							pc.getInventory().storeItem(3000183, 50000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("dragonslayer-leg9")) {
						if (pc.getInventory().checkEnchantItem(900200, 9, 1)) {  // +9 드래곤 슬레이어의 각반
							pc.getInventory().consumeEnchantItem(900200, 9, 1);
							pc.getInventory().storeItem(3000183, 100000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("dragonslayer-leg10")) {
						if (pc.getInventory().checkEnchantItem(900200, 10, 1)) {  // +10 드래곤 슬레이어의 각반
							pc.getInventory().consumeEnchantItem(900200, 10, 1);
							pc.getInventory().storeItem(3000183, 200000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					/**
					 * 레어 악세서리
					 * */
					if (s.equalsIgnoreCase("champ-neck8")) {
						if (pc.getInventory().checkEnchantItem(222304, 8, 1)) {  // +8 투사의 목걸이
							pc.getInventory().consumeEnchantItem(222304, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("shining-saiha8")) {
						if (pc.getInventory().checkEnchantItem(20271, 8, 1)) {  // +8 빛나는 사이하의 목걸이
							pc.getInventory().consumeEnchantItem(20271, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("sage-neck8")) {
						if (pc.getInventory().checkEnchantItem(222306, 8, 1)) {  // +8 현자의 목걸이
							pc.getInventory().consumeEnchantItem(222306, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("zenith-ring8")) {
						if (pc.getInventory().checkEnchantItem(20298, 8, 1)) {  // +8 제니스의 반지
							pc.getInventory().consumeEnchantItem(20298, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-ring8")) {
						if (pc.getInventory().checkEnchantItem(900167, 8, 1)) {  // +8 머미로드의 반지
							pc.getInventory().consumeEnchantItem(900167, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("iris-ear8")) {
						if (pc.getInventory().checkEnchantItem(20260, 8, 1)) {  // +8 아이리스의 귀걸이
							pc.getInventory().consumeEnchantItem(20260, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rich-ring8")) {
						if (pc.getInventory().checkEnchantItem(900163, 8, 1)) {  // +8 리치의 반지
							pc.getInventory().consumeEnchantItem(900163, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("reaper-ring8")) {
						if (pc.getInventory().checkEnchantItem(900162, 8, 1)) {  // +8 사신의 반지
							pc.getInventory().consumeEnchantItem(900162, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("leia-ring8")) {
						if (pc.getInventory().checkEnchantItem(20279, 8, 1)) {  // +8 라이아의 반지
							pc.getInventory().consumeEnchantItem(20279, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("kronos-belt8")) {
						if (pc.getInventory().checkEnchantItem(900007, 8, 1)) {  // +8 크로노스의 벨트
							pc.getInventory().consumeEnchantItem(900007, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ogre-king8")) {
						if (pc.getInventory().checkEnchantItem(900048, 8, 1)) {  // +8 오우거 킹의 벨트
							pc.getInventory().consumeEnchantItem(900048, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-giant8")) {
						if (pc.getInventory().checkEnchantItem(20314, 8, 1)) {  // +8 에이션트 자이언트의 반지
							pc.getInventory().consumeEnchantItem(20314, 8, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					/**
					 * 기본 인챈트 +4짜리 방어구: +8 인챈트
					 * */
					if (s.equalsIgnoreCase("ancient-leg8")) {
						if (pc.getInventory().checkEnchantItem(900015, 8, 1)) {  // +8 고대 마물의 각반
							pc.getInventory().consumeEnchantItem(900015, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-glove8")) {
						if (pc.getInventory().checkEnchantItem(900016, 8, 1)) {  // +8 고대 마물의 장갑
							pc.getInventory().consumeEnchantItem(900016, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-cloak8")) {
						if (pc.getInventory().checkEnchantItem(900017, 8, 1)) {  // +8 고대 마물의 망토
							pc.getInventory().consumeEnchantItem(900017, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-boots8")) {
						if (pc.getInventory().checkEnchantItem(900018, 8, 1)) {  // +8 고대 마물의 부츠
							pc.getInventory().consumeEnchantItem(900018, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-leg8")) {
						if (pc.getInventory().checkEnchantItem(900011, 8, 1)) {  // +8 고대 암석 각반
							pc.getInventory().consumeEnchantItem(900011, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-glove8")) {
						if (pc.getInventory().checkEnchantItem(900014, 8, 1)) {  // +8 고대 암석 장갑
							pc.getInventory().consumeEnchantItem(900014, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-cloak8")) {
						if (pc.getInventory().checkEnchantItem(900013, 8, 1)) {  // +8 고대 암석 망토
							pc.getInventory().consumeEnchantItem(900013, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-boots8")) {
						if (pc.getInventory().checkEnchantItem(900012, 8, 1)) {  // +8 고대 암석 부츠
							pc.getInventory().consumeEnchantItem(900012, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("saiha8")) {
						if (pc.getInventory().checkEnchantItem(900202, 8, 1)) {  // +8 사이하의 견갑
							pc.getInventory().consumeEnchantItem(900202, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("arcmage8")) {
						if (pc.getInventory().checkEnchantItem(900203, 8, 1)) {  // +8 대마법사의 견갑
							pc.getInventory().consumeEnchantItem(900203, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("traitor8")) {
						if (pc.getInventory().checkEnchantItem(22263, 8, 1)) {  // +8 반역자의 방패
							pc.getInventory().consumeEnchantItem(22263, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("vampire-leg8")) {
						if (pc.getInventory().checkEnchantItem(900161, 8, 1)) {  // +8 뱀파이어의 각반
							pc.getInventory().consumeEnchantItem(900161, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("cougar8")) {
						if (pc.getInventory().checkEnchantItem(900157, 8, 1)) {  // +8 쿠거의 가더
							pc.getInventory().consumeEnchantItem(900157, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-glove8")) {
						if (pc.getInventory().checkEnchantItem(900156, 8, 1)) {  // +8 머미로드의 장갑
							pc.getInventory().consumeEnchantItem(900156, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-leg8")) {
						if (pc.getInventory().checkEnchantItem(900166, 8, 1)) {  // +8 머미로드의 각반
							pc.getInventory().consumeEnchantItem(900166, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-boots8")) {
						if (pc.getInventory().checkEnchantItem(900168, 8, 1)) {  // +8 머미로드의 부츠
							pc.getInventory().consumeEnchantItem(900168, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("iris-boots8")) {
						if (pc.getInventory().checkEnchantItem(900155, 8, 1)) {  // +8 아이리스의 부츠
							pc.getInventory().consumeEnchantItem(900155, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("iris-leg8")) {
						if (pc.getInventory().checkEnchantItem(900160, 8, 1)) {  // +8 아이리스의 각반
							pc.getInventory().consumeEnchantItem(900160, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-leg8")) {
						if (pc.getInventory().checkEnchantItem(900159, 8, 1)) {  // +8 나이트발드의 각반
							pc.getInventory().consumeEnchantItem(900159, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-helm8")) {
						if (pc.getInventory().checkEnchantItem(22360, 8, 1)) {  // +8 지휘관의 투구
							pc.getInventory().consumeEnchantItem(22360, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-shoulder8")) {
						if (pc.getInventory().checkEnchantItem(900201, 8, 1)) {  // +8 지휘관의 견갑
							pc.getInventory().consumeEnchantItem(900201, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rich-lobe8")) {
						if (pc.getInventory().checkEnchantItem(20107, 8, 1)) {  // +8 리치 로브
							pc.getInventory().consumeEnchantItem(20107, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ugnus8")) {
						if (pc.getInventory().checkEnchantItem(900158, 8, 1)) {  // +8 우그누스의 가더
							pc.getInventory().consumeEnchantItem(900158, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-str8")) {
						if (pc.getInventory().checkEnchantItem(22208, 8, 1)) {  // +8 발라카스의 완력
							pc.getInventory().consumeEnchantItem(22208, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-wis8")) {
						if (pc.getInventory().checkEnchantItem(22209, 8, 1)) {  // +8 발라카스의 예지력
							pc.getInventory().consumeEnchantItem(22209, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-dex8")) {
						if (pc.getInventory().checkEnchantItem(22210, 8, 1)) {  // +8 발라카스의 인내력
							pc.getInventory().consumeEnchantItem(22210, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-int8")) {
						if (pc.getInventory().checkEnchantItem(22211, 8, 1)) {  // +8 발라카스의 마력
							pc.getInventory().consumeEnchantItem(22211, 8, 1);
							pc.getInventory().storeItem(3000183, 15000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					/**
					 * 기본 방어구 +4 방어구: +9 인챈트
					 * */
					if (s.equalsIgnoreCase("ancient-leg9")) {
						if (pc.getInventory().checkEnchantItem(900015, 9, 1)) {  // +9 고대 마물의 각반
							pc.getInventory().consumeEnchantItem(900015, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-glove9")) {
						if (pc.getInventory().checkEnchantItem(900016, 9, 1)) {  // +9 고대 마물의 장갑
							pc.getInventory().consumeEnchantItem(900016, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-cloak9")) {
						if (pc.getInventory().checkEnchantItem(900017, 9, 1)) {  // +9 고대 마물의 망토
							pc.getInventory().consumeEnchantItem(900017, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-boots9")) {
						if (pc.getInventory().checkEnchantItem(900018, 9, 1)) {  // +9 고대 마물의 부츠
							pc.getInventory().consumeEnchantItem(900018, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-leg9")) {
						if (pc.getInventory().checkEnchantItem(900011, 9, 1)) {  // +9 고대 암석 각반
							pc.getInventory().consumeEnchantItem(900011, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-glove9")) {
						if (pc.getInventory().checkEnchantItem(900014, 9, 1)) {  // +9 고대 암석 장갑
							pc.getInventory().consumeEnchantItem(900014, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-cloak9")) {
						if (pc.getInventory().checkEnchantItem(900013, 9, 1)) {  // +9 고대 암석 망토
							pc.getInventory().consumeEnchantItem(900013, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-boots9")) {
						if (pc.getInventory().checkEnchantItem(900012, 9, 1)) {  // +9 고대 암석 부츠
							pc.getInventory().consumeEnchantItem(900012, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("saiha9")) {
						if (pc.getInventory().checkEnchantItem(900202, 9, 1)) {  // +9 사이하의 견갑
							pc.getInventory().consumeEnchantItem(900202, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("arcmage9")) {
						if (pc.getInventory().checkEnchantItem(900203, 9, 1)) {  // +9 대마법사의 견갑
							pc.getInventory().consumeEnchantItem(900203, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("traitor9")) {
						if (pc.getInventory().checkEnchantItem(22263, 9, 1)) {  // +9 반역자의 방패
							pc.getInventory().consumeEnchantItem(22263, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("vampire-leg9")) {
						if (pc.getInventory().checkEnchantItem(900161, 9, 1)) {  // +9 뱀파이어의 각반
							pc.getInventory().consumeEnchantItem(900161, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("cougar9")) {
						if (pc.getInventory().checkEnchantItem(900157, 9, 1)) {  // +9 쿠거의 가더
							pc.getInventory().consumeEnchantItem(900157, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-glove9")) {
						if (pc.getInventory().checkEnchantItem(900156, 9, 1)) {  // +9 머미로드의 장갑
							pc.getInventory().consumeEnchantItem(900156, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-leg9")) {
						if (pc.getInventory().checkEnchantItem(900166, 9, 1)) {  // +9 머미로드의 각반
							pc.getInventory().consumeEnchantItem(900166, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-boots9")) {
						if (pc.getInventory().checkEnchantItem(900168, 9, 1)) {  // +9 머미로드의 부츠
							pc.getInventory().consumeEnchantItem(900168, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("iris-boots9")) {
						if (pc.getInventory().checkEnchantItem(900155, 9, 1)) {  // +9 아이리스의 부츠
							pc.getInventory().consumeEnchantItem(900155, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("iris-leg9")) {
						if (pc.getInventory().checkEnchantItem(900160, 9, 1)) {  // +9 아이리스의 각반
							pc.getInventory().consumeEnchantItem(900160, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-leg9")) {
						if (pc.getInventory().checkEnchantItem(900159, 9, 1)) {  // +9 나이트발드의 각반
							pc.getInventory().consumeEnchantItem(900159, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-helm9")) {
						if (pc.getInventory().checkEnchantItem(22360, 9, 1)) {  // +9 지휘관의 투구
							pc.getInventory().consumeEnchantItem(22360, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-shoulder9")) {
						if (pc.getInventory().checkEnchantItem(900201, 9, 1)) {  // +9 지휘관의 견갑
							pc.getInventory().consumeEnchantItem(900201, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rich-lobe9")) {
						if (pc.getInventory().checkEnchantItem(20107, 9, 1)) {  // +9 리치 로브
							pc.getInventory().consumeEnchantItem(20107, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ugnus9")) {
						if (pc.getInventory().checkEnchantItem(900158, 9, 1)) {  // +9 우그누스의 가더
							pc.getInventory().consumeEnchantItem(900158, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-str9")) {
						if (pc.getInventory().checkEnchantItem(22208, 9, 1)) {  // +9 발라카스의 완력
							pc.getInventory().consumeEnchantItem(22208, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-wis9")) {
						if (pc.getInventory().checkEnchantItem(22209, 9, 1)) {  // +9 발라카스의 예지력
							pc.getInventory().consumeEnchantItem(22209, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-dex9")) {
						if (pc.getInventory().checkEnchantItem(22210, 9, 1)) {  // +9 발라카스의 인내력
							pc.getInventory().consumeEnchantItem(22210, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-int9")) {
						if (pc.getInventory().checkEnchantItem(22211, 9, 1)) {  // +9 발라카스의 마력
							pc.getInventory().consumeEnchantItem(22211, 9, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					/**
					 * 기본 인챈트 +4: 방어구 +10
					 * */
					if (s.equalsIgnoreCase("ancient-leg10")) {
						if (pc.getInventory().checkEnchantItem(900015, 10, 1)) {  // +10 고대 마물의 각반
							pc.getInventory().consumeEnchantItem(900015, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-glove10")) {
						if (pc.getInventory().checkEnchantItem(900016, 10, 1)) {  // +10 고대 마물의 장갑
							pc.getInventory().consumeEnchantItem(900016, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-cloak10")) {
						if (pc.getInventory().checkEnchantItem(900017, 10, 1)) {  // +10 고대 마물의 망토
							pc.getInventory().consumeEnchantItem(900017, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ancient-boots10")) {
						if (pc.getInventory().checkEnchantItem(900018, 10, 1)) {  // +10 고대 마물의 부츠
							pc.getInventory().consumeEnchantItem(900018, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-leg10")) {
						if (pc.getInventory().checkEnchantItem(900011, 10, 1)) {  // +10 고대 암석 각반
							pc.getInventory().consumeEnchantItem(900011, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-glove10")) {
						if (pc.getInventory().checkEnchantItem(900014, 10, 1)) {  // +10 고대 암석 장갑
							pc.getInventory().consumeEnchantItem(900014, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-cloak10")) {
						if (pc.getInventory().checkEnchantItem(900013, 10, 1)) {  // +10 고대 암석 망토
							pc.getInventory().consumeEnchantItem(900013, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rock-boots10")) {
						if (pc.getInventory().checkEnchantItem(900012, 10, 1)) {  // +10 고대 암석 부츠
							pc.getInventory().consumeEnchantItem(900012, 10, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("saiha10")) {
						if (pc.getInventory().checkEnchantItem(900202, 10, 1)) {  // +10 사이하의 견갑
							pc.getInventory().consumeEnchantItem(900202, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("arcmage10")) {
						if (pc.getInventory().checkEnchantItem(900203, 10, 1)) {  // +10 대마법사의 견갑
							pc.getInventory().consumeEnchantItem(900203, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("traitor10")) {
						if (pc.getInventory().checkEnchantItem(22263, 10, 1)) {  // +10 반역자의 방패
							pc.getInventory().consumeEnchantItem(22263, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("vampire-leg10")) {
						if (pc.getInventory().checkEnchantItem(900161, 10, 1)) {  // +10 뱀파이어의 각반
							pc.getInventory().consumeEnchantItem(900161, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("cougar10")) {
						if (pc.getInventory().checkEnchantItem(900157, 10, 1)) {  // +10 쿠거의 가더
							pc.getInventory().consumeEnchantItem(900157, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-glove10")) {
						if (pc.getInventory().checkEnchantItem(900156, 10, 1)) {  // +10 머미로드의 장갑
							pc.getInventory().consumeEnchantItem(900156, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-leg10")) {
						if (pc.getInventory().checkEnchantItem(900166, 10, 1)) {  // +10 머미로드의 각반
							pc.getInventory().consumeEnchantItem(900166, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-boots10")) {
						if (pc.getInventory().checkEnchantItem(900168, 10, 1)) {  // +10 머미로드의 부츠
							pc.getInventory().consumeEnchantItem(900168, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("iris-boots10")) {
						if (pc.getInventory().checkEnchantItem(900155, 10, 1)) {  // +10 아이리스의 부츠
							pc.getInventory().consumeEnchantItem(900155, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("iris-leg10")) {
						if (pc.getInventory().checkEnchantItem(900160, 10, 1)) {  // +10 아이리스의 각반
							pc.getInventory().consumeEnchantItem(900160, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-leg10")) {
						if (pc.getInventory().checkEnchantItem(900159, 10, 1)) {  // +10 나이트발드의 각반
							pc.getInventory().consumeEnchantItem(900159, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-helm10")) {
						if (pc.getInventory().checkEnchantItem(22360, 10, 1)) {  // +10 지휘관의 투구
							pc.getInventory().consumeEnchantItem(22360, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("bald-shoulder10")) {
						if (pc.getInventory().checkEnchantItem(900201, 10, 1)) {  // +10 지휘관의 견갑
							pc.getInventory().consumeEnchantItem(900201, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rich-lobe10")) {
						if (pc.getInventory().checkEnchantItem(20107, 10, 1)) {  // +10 리치 로브
							pc.getInventory().consumeEnchantItem(20107, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("ugnus10")) {
						if (pc.getInventory().checkEnchantItem(900158, 10, 1)) {  // +10 우그누스의 가더
							pc.getInventory().consumeEnchantItem(900158, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-str10")) {
						if (pc.getInventory().checkEnchantItem(22208, 10, 1)) {  // +10 발라카스의 완력
							pc.getInventory().consumeEnchantItem(22208, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-wis10")) {
						if (pc.getInventory().checkEnchantItem(22209, 10, 1)) {  // +10 발라카스의 예지력
							pc.getInventory().consumeEnchantItem(22209, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-dex10")) {
						if (pc.getInventory().checkEnchantItem(22210, 10, 1)) {  // +10 발라카스의 인내력
							pc.getInventory().consumeEnchantItem(22210, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-int10")) {
						if (pc.getInventory().checkEnchantItem(22211, 10, 1)) {  // +10 발라카스의 마력
							pc.getInventory().consumeEnchantItem(22211, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					/**
					 * 안전 인챈트 +6
					 * */
					if (s.equalsIgnoreCase("seer9")) {
						if (pc.getInventory().checkEnchantItem(22214, 9, 1)) {  // +9 시어의 심안
							pc.getInventory().consumeEnchantItem(22214, 9, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("vampire-cloak9")) {
						if (pc.getInventory().checkEnchantItem(20079, 9, 1)) {  // +9 뱀파이어의 망토
							pc.getInventory().consumeEnchantItem(20079, 9, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("frenzy9")) {
						if (pc.getInventory().checkEnchantItem(222317, 9, 1)) {  // +9 격분의 장갑
							pc.getInventory().consumeEnchantItem(222317, 9, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-crown9")) {
						if (pc.getInventory().checkEnchantItem(20017, 9, 1)) {  // +9 머미로드의 왕관
							pc.getInventory().consumeEnchantItem(20017, 9, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rich-ball9")) {
						if (pc.getInventory().checkEnchantItem(900165, 9, 1)) {  // +9 리치의 수정구
							pc.getInventory().consumeEnchantItem(900165, 9, 1);
							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("seer10")) {
						if (pc.getInventory().checkEnchantItem(22214, 10, 1)) {  // +10 시어의 심안
							pc.getInventory().consumeEnchantItem(22214, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("vampire-cloak10")) {
						if (pc.getInventory().checkEnchantItem(20079, 10, 1)) {  // +10 뱀파이어의 망토
							pc.getInventory().consumeEnchantItem(20079, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("frenzy10")) {
						if (pc.getInventory().checkEnchantItem(222317, 10, 1)) {  // +10 격분의 장갑
							pc.getInventory().consumeEnchantItem(222317, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("mummy-crown10")) {
						if (pc.getInventory().checkEnchantItem(20017, 10, 1)) {  // +10 머미로드의 왕관
							pc.getInventory().consumeEnchantItem(20017, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("rich-ball10")) {
						if (pc.getInventory().checkEnchantItem(900165, 10, 1)) {  // +10 리치의 수정구
							pc.getInventory().consumeEnchantItem(900165, 10, 1);
							pc.getInventory().storeItem(3000183, 30000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}						
					/**
					 * 지배 반지
					 * */
					
					if (s.equalsIgnoreCase("poly-only-ring")) {
						if (pc.getInventory().checkItem(140009, 1)) { // 변신 유일 반지
							pc.getInventory().consumeItem(140009, 1);
							pc.getInventory().storeItem(3000185, 200000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					// 17차부터 매입 안함
//					if (s.equalsIgnoreCase("poly-master-ring")) {
//						if (pc.getInventory().checkItem(900075, 1)) { // 변신 지배 반지
//							pc.getInventory().consumeItem(900075, 1);
//							pc.getInventory().storeItem(3000183, 10000); // 다음차 이월 코인
//							htmlid = "veteranE2";
//						} else {
//							htmlid = "veteranE6";
//						}
//					}
					
					if (s.equalsIgnoreCase("teleport-master-ring")) {
						if (pc.getInventory().checkItem(900111, 1)) { // 순간이동 지배 반지
							pc.getInventory().consumeItem(900111, 1);
							pc.getInventory().storeItem(3000183, 20000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					
					/**
					 * 마법인형
					 * */
					if (s.equalsIgnoreCase("halpas-doll")) {
						if (pc.getInventory().checkItem(141400, 1)) { // 마법인형: 할파스
							pc.getInventory().consumeItem(141400, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("aurachia-doll")) {
						if (pc.getInventory().checkItem(141401, 1)) { // 마법인형: 아우라키아
							pc.getInventory().consumeItem(141401, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("behemoth-doll")) {
						if (pc.getInventory().checkItem(141402, 1)) { // 마법인형: 베히모스
							pc.getInventory().consumeItem(141402, 1);
							pc.getInventory().storeItem(3000183, 60000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("antharas-doll")) {
						if (pc.getInventory().checkItem(4100007, 1)) { // 마법인형: 안타라스
							pc.getInventory().consumeItem(4100007, 1);
							pc.getInventory().storeItem(3000183, 25000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("fafurion-doll")) {
						if (pc.getInventory().checkItem(4100008, 1)) { // 마법인형: 파푸리온
							pc.getInventory().consumeItem(4100008, 1);
							pc.getInventory().storeItem(3000183, 25000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("lindvior-doll")) {
						if (pc.getInventory().checkItem(4100009, 1)) { // 마법인형: 린드비오르
							pc.getInventory().consumeItem(4100009, 1);
							pc.getInventory().storeItem(3000183, 25000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}
					if (s.equalsIgnoreCase("valakas-doll")) {
						if (pc.getInventory().checkItem(4100010, 1)) { // 마법인형: 발라카스
							pc.getInventory().consumeItem(4100010, 1);
							pc.getInventory().storeItem(3000183, 25000); // 다음차 이월 코인
							htmlid = "veteranE2";
						} else {
							htmlid = "veteranE6";
						}
					}

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200018) {// 경험치 지급단
				if (s.equalsIgnoreCase("0")) {// 한번씩 지급
					if (pc.getLevel() < 51) {
						pc.addExpForReady((ExpTable.getExpByLevel(51) - 1) - pc.getExp()
								+ ((ExpTable.getExpByLevel(51) - 1) / 100));
					} else if (pc.getLevel() >= 51 && pc.getLevel() < Config.경험치지급단) {
						pc.addExpForReady((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.getExp() + 100);
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
					}
				} else if (s.equalsIgnoreCase("1")) {// 한번에 지급
					if (pc.getLevel() >= Config.경험치지급단 && pc.getLevel() <= Config.경험치지급단) {
						pc.addExpForReady((ExpTable.getExpByLevel(Config.경험치지급단) - 1) - pc.getExp()
								+ ((ExpTable.getExpByLevel(Config.경험치지급단) - 1) / 30000000));
					} else if (pc.getLevel() <= Config.경험치지급단 && pc.getLevel() < Config.경험치지급단) {
						pc.addExpForReady((ExpTable.getExpByLevel(Config.경험치지급단) - 1) - pc.getExp()
								+ ((ExpTable.getExpByLevel(Config.경험치지급단) - 1) / 30000000));
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
					}
				} else if (s.equalsIgnoreCase("2")) {// 신규지원
					if (pc.getLevel() >= Config.New_Cha) {// 이하
						pc.sendPackets(new S_SystemMessage("신규가 아니므로, 더 이상 지원은 불가능 합니다."));
						return htmlid;
					} else if (pc.getInventory().checkItem(7241, 1) || pc.getInventory().checkItem(1000004, 1)) {
						pc.sendPackets(new S_SystemMessage("중복 지급은 불가능합니다."));
						return htmlid;
					}
					봉인템(pc, 7241, 5, 0, 1, 0, true); // 토파즈
					봉인템(pc, 3000231, 3, 0, 1, 0, true); // 최고급 드다
					봉인템(pc, 1000007, 10, 0, 1, 0, true); // 고급드다
				}

				/** 붉은 기사단원 가입 데포로쥬 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5163) {
				if (s.equalsIgnoreCase("j")) {// 가입
					pc.sendPackets(new S_ServerMessage(3854)); // 52레벨 이상만 붉은
																// 기사단에 가입이
																// 가능합니다.

					/*
					 * if (pc.getLevel() < 52) { pc.sendPackets(new
					 * S_ServerMessage(3854)); // 52레벨 이상만 붉은 기사단에 가입이 가능합니다.
					 * return htmlid; }
					 * 
					 * if (pc.getRedKnightClanId() == 0) { MJCastleWarBusiness
					 * business = MJCastleWarBusiness.getInstance(); int
					 * castleId =
					 * business.isNowReady(L1CastleLocation.GIRAN_CASTLE_ID) ?
					 * L1CastleLocation.GIRAN_CASTLE_ID :
					 * business.isNowReady(L1CastleLocation.OT_CASTLE_ID) ?
					 * L1CastleLocation.OT_CASTLE_ID :
					 * business.isNowReady(L1CastleLocation.KENT_CASTLE_ID) ?
					 * L1CastleLocation.KENT_CASTLE_ID : 0; if(castleId == 0){
					 * pc.sendPackets(new S_SystemMessage("공성: 가입 실패 (가입 시간 지남)"
					 * )); return htmlid; }
					 * 
					 * pc.setRedKnightClanId(castleId);
					 * pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(),
					 * pc.getHeading(), 169, false, false);
					 * pc.sendPackets(new S_ServerMessage(3764)); // 공성: 붉은 기사단
					 * 가입 성공 } else pc.sendPackets(new S_ServerMessage(3765));
					 // 공성: 가입 실패 (가입 상태)
					 */
					return htmlid;
				} else if (s.equalsIgnoreCase("d")) {// 탈퇴
					if (pc.getRedKnightClanId() == 0) {
						pc.sendPackets(new S_SystemMessage("가입되어 있지 않습니다."));
						return htmlid;
					}

					pc.setRedKnightClanId(0);
					pc.sendPackets(new S_SystemMessage("붉은 기사단을 탈퇴하였습니다."));
					pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, false, false);
					return htmlid;
				}

				/** 카이저 */
			} else if (npcid == 7000079) {
				if (s.equalsIgnoreCase("1")) { // 대여
					int countActiveMaps = BossTrainingSystem.getInstance().countRaidPotal();
					if (pc.getInventory().checkItem(80500)) {
						htmlid = "bosskey6";
						// 이미 훈련소 열쇠를 가지고 계신 것 같군요.
						// 많은 분들께서 이용하실 수 있도록 훈련소는 한 사람 당 하나 씩만 대여해 드리고 있습니다.
					} else if (countActiveMaps >= 99) {
						htmlid = "bosskey3";
						// 죄송합니다.
						// 지금은 모든 훈련소에서 훈련이 진행 중 입니다.
					} else {
						htmlid = "bosskey4";
					}
				} else if (s.matches("[2-4]")) {
					if (!pc.getInventory().checkItem(80500)) { // 액션 조작 방지
						L1ItemInstance item = null;
						int count = 0;
						if (s.equalsIgnoreCase("2")) { // 4개
							count = 4;
						} else if (s.equalsIgnoreCase("3")) { // 8개
							count = 8;
						} else if (s.equalsIgnoreCase("4")) { // 16개
							count = 16;
						}
						if (pc.getInventory().consumeItem(40308, count * 300)) {
							int id = BossTrainingSystem.getInstance().blankMapId();
							BossTrainingSystem.getInstance().startRaid(pc, id);
							for (int i = 0; i < count; i++) {
								item = pc.getInventory().storeItem(80500, 1);
								item.setKeyId(id);
								if (KeyTable.checkey(item)) {
									KeyTable.DeleteKey(item);
									KeyTable.StoreKey(item);
								} else {
									KeyTable.StoreKey(item);
								}
							}
							htmlid = "bosskey7";
							// 같이 훈련을 받으실 분들에게 열쇠를 나누어 주신 다음 저에게 보여주시면 훈련소로 안내해
							// 드리겠습니다.
							// 훈련소의 대여시간은 최대 4시간이며, 훈련 중이라 해도 대여 시간이 종료되면 다음 사람을
							// 위해 훈련소 사용이 중지됩니다.
							// 훈련용 몬스터를 소환하실 때에는 항상 훈련소의 남은 사용 시간을 확인하시기 바랍니다.
						} else {
							htmlid = "bosskey5";
							// 죄송하지만, 사용료를 지불하지 않으시면 훈련소를 빌려드릴 수 없습니다.
							// 아덴 왕국의 지원금만으로 이 많은 훈련소를 관리하는 것이 쉬운 일은 아니라서요.
						}
					} else {
						htmlid = "bosskey6";
						// 이미 훈련소 열쇠를 가지고 계신 것 같군요.
						// 많은 분들께서 이용하실 수 있도록 훈련소는 한 사람 당 하나 씩만 대여해 드리고 있습니다.
					}
				} else if (s.equalsIgnoreCase("6")) { // 입장
					int countActiveMaps = BossTrainingSystem.getInstance().countRaidPotal();
					if (countActiveMaps < 100) {
						L1ItemInstance item = pc.getInventory().findItemId(80500);
						if (item != null) {
							int id = item.getKeyId();
							// L1Teleport.teleport(pc, 32901, 32814, (short) id,
							// 0, true);
							pc.start_teleport(32901, 32814, id, pc.getHeading(), 169, true, false);
						} else {
							htmlid = "bosskey2";
							// 훈련소 열쇠를 가지고 있지 않으신 것 같네요.
							// 먼저 훈련소를 대여하신 후에 사용하실 수 있습니다.
						}
					} else {
						htmlid = "bosskey3";
						// 죄송합니다.
						// 지금은 모든 훈련소에서 훈련이 진행 중 입니다.
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7200022) {// 생일도우미
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (pc.isInvisble()) {
					pc.sendPackets(new S_NpcChatPacket(npc, "투명 상태에서 받을 수 없습니다.", 0));
					return htmlid;
				}
				if (s.equalsIgnoreCase("a")) {
					htmlid = "birthday6";
				}
				if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().consumeItem(3000048, 1)) {
						new L1SkillUse().handleCommands(pc, L1SkillId.COMA_B, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_SPELLSC);
						htmlid = "birthday4";
					} else {
						htmlid = "birthday6";
					}
				}
				/** 세실리아 (영혼석) **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000080) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (s.equalsIgnoreCase("A")) {// 상아탑몬스터
					if (pc.getInventory().consumeItem(80466, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900076, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'하딘의분신'이 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("B")) {// 상아탑몬스터
					if (pc.getInventory().consumeItem(80467, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 900070, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'흑마법사'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("C")) {// 상아탑몬스터
					if (pc.getInventory().consumeItem(80450, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45649, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'데몬'이 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("D")) {// 상아탑몬스터
					if (pc.getInventory().consumeItem(80451, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45685, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "상아탑 최종보스 '타락'이 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				/** 라스타바드 몬스터 **/
				if (s.equalsIgnoreCase("E")) {
					if (pc.getInventory().consumeItem(80452, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45955, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'케이나'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("F")) {
					if (pc.getInventory().consumeItem(80453, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45959, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'이데아'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("G")) {
					if (pc.getInventory().consumeItem(80454, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45956, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'비아타스'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("H")) {
					if (pc.getInventory().consumeItem(80455, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45957, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'바로메스'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("I")) {
					if (pc.getInventory().consumeItem(80456, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45960, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'티아메스'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("J")) {
					if (pc.getInventory().consumeItem(80457, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45958, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'엔디아스'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("K")) {
					if (pc.getInventory().consumeItem(80458, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45961, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'라미아스'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("L")) {
					if (pc.getInventory().consumeItem(80459, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45962, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'바로드'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("M")) {
					if (pc.getInventory().consumeItem(80460, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45676, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'헬바인'이 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("N")) {
					if (pc.getInventory().consumeItem(80461, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45677, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'라이아'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("O")) {
					if (pc.getInventory().consumeItem(80462, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45844, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'바란카'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("P")) {
					if (pc.getInventory().consumeItem(80463, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45648, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "라스타바드 최종보스 '슬레이브'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				/** 글루디오 체크몬스터 **/
				if (s.equalsIgnoreCase("Q")) {
					if (pc.getInventory().consumeItem(80464, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45456, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'네크로맨서'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("S")) {
					if (pc.getInventory().consumeItem(80465, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45601, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'데스나이트'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				/** 오만의 탑 **/
				if (s.equalsIgnoreCase("T")) {
					if (pc.getInventory().consumeItem(80468, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310015, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'왜곡의 제니스 퀸'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("U")) {
					if (pc.getInventory().consumeItem(80469, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310021, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'불신의 시어'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("V")) {
					if (pc.getInventory().consumeItem(80470, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310028, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'공포의 뱀파이어'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("W")) {
					if (pc.getInventory().consumeItem(80471, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310034, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'죽음의 좀비 로드'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("X")) {
					if (pc.getInventory().consumeItem(80472, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310041, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'지옥의 쿠거'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("Y")) {
					if (pc.getInventory().consumeItem(80473, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310046, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'불사의 머미 로드'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(80474, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310051, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'잔혹한 아이리스'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().consumeItem(80475, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310056, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'어둠의 나이트 발드'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().consumeItem(80476, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310061, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'불멸의 리치'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().consumeItem(80477, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 7310077, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'사신 그림 리퍼'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				if (s.equalsIgnoreCase("d")) {
					if (pc.getInventory().consumeItem(80478, 1)) {
						L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 45600, 0, 3600 * 1000, 0);
						pc.sendPackets(new S_NpcChatPacket(npc, "'흑기사 대장 커츠'가 중앙에 소환되었습니다.", 0));
					} else {
						htmlid = "bosskey10";
					}
				}
				// 낡은책더미
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210050) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(60032)) {
						pc.sendPackets(new S_ChatPacket(pc, "이미 낡은 고서를 가지고있네요"));
						htmlid = "";
					} else {
						pc.getInventory().storeItem(60032, 1);
						htmlid = "oldbook2";
					}
				}
				// 수룡의던전
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210000) {
				if (s.equalsIgnoreCase("1")) {
					if (pc.getLevel() >= 75 & pc.getLevel() <= 80) {
						pc.start_teleport(32774, 32805, 814, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						htmlid = "newbieddw2";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310088) { // 피터리뉴얼
				if (s.equalsIgnoreCase("1")) {
					pc.start_teleport(32770, 32826, 75, pc.getHeading(), 169, true, false);
					htmlid = "";
				}
				if (s.equalsIgnoreCase("2")) {
					pc.start_teleport(32772, 32823, 76, pc.getHeading(), 169, true, false);
					htmlid = "";
				}
				if (s.equalsIgnoreCase("3")) {
					pc.start_teleport(32762, 32839, 77, pc.getHeading(), 169, true, false);
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 505011) {// 지프
																						// 리뉴얼
				if (s.equals("teleportURL")) {// 액션코드
					if (pc.getInventory().checkItem(40308, 7777)) {
						pc.getInventory().consumeItem(40308, 7777);
						int rx = _random.nextInt(7);
						int ux = 33435 + rx;
						int uy = 32812 + rx;
						pc.start_teleport(ux, uy, 4, pc.getHeading(), 169, true, false);
					}
				} else {
					pc.sendPackets(new S_SystemMessage("시스템: 아데나(7,000원)이 부족합니다."));
				}
			} else if (npcid == 535) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(3000506, 1)) {
						pc.getInventory().consumeItem(3000506, 1);
						pc.getInventory().storeItem(3000246, 3);
						pc.sendPackets(new S_SystemMessage("획득:오림의가넷(3) 감사합니다. 다음에 또 오세요."));
						htmlid = "";
					} else {
						htmlid = "falllovef";
						pc.sendPackets(new S_SystemMessage("프로톤 건(1)개 를 가져다주시면 오림의 가넷(3)로 드리겠습니다."));
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(3000506, 5)) {
						pc.getInventory().consumeItem(3000506, 5);
						pc.getInventory().storeItem(3000246, 15);
						pc.sendPackets(new S_SystemMessage("획득:오림의가넷(15) 감사합니다. 다음에 또 오세요."));
						htmlid = "";
					} else {
						htmlid = "falllovef";
						pc.sendPackets(new S_SystemMessage("프로톤 건(5)개 를 가져다주시면 오림의 가넷(15)로 드리겠습니다."));
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().checkItem(3000506, 10)) {
						pc.getInventory().consumeItem(3000506, 10);
						pc.getInventory().storeItem(3000246, 30);
						pc.sendPackets(new S_SystemMessage("획득:오림의가넷(30) 감사합니다. 다음에 또 오세요."));
						htmlid = "";
					} else {
						htmlid = "falllovef";
						pc.sendPackets(new S_SystemMessage("프로톤 건(10)개 를 가져다주시면 오림의 가넷(30)로 드리겠습니다."));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210007) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getLevel() >= 60) {
						L1Quest quest = pc.getQuest();
						int questStep = quest.get_step(L1Quest.QUEST_HAMO);
						if (!pc.getInventory().checkItem(820000) && questStep != L1Quest.QUEST_END) {
							pc.getQuest().set_end(L1Quest.QUEST_HAMO);
							pc.getInventory().storeItem(820000, 1);// 햄의주머니
							htmlid = "";
						} else {
							htmlid = "hamo1";
						}
					} else {
						htmlid = "hamo3";
						pc.sendPackets(new S_SystemMessage("60이상의 캐릭터만 받을 수 있습니다."));
					}
				}
				// 엘드나스
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210008) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getLevel() >= 60) {
						if (pc.getInventory().consumeItem(820001, 1)) {// 냉한의기운
							ValakasReadyStart.getInstance().startReady(pc);
						} else {
							htmlid = "eldnas1";
						}
					} else {
						htmlid = "eldnas3";
					}
				}
				// 진데스나이트
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210009) {
				if (s.equalsIgnoreCase("enter")) {
					if (pc.getLevel() >= 60) {
						if (!pc.getInventory().checkItem(203003, 1)) {// 데스나이트의
							// 불검:진
							pc.getInventory().storeItem(203003, 1);// 데스나이트의
							// 불검:진
							ValakasRoomSystem.getInstance().startRaid(pc);
						}
					} else {
						htmlid = "fd_death2";
					}
				}
				/** 깃털마을 피아르 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310093) {
				if (s.equalsIgnoreCase("a")) {
					if (!pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("PC방 이용권을 사용 중에만 가능한 행동입니다."));
						htmlid = "pc_tell2";
						return htmlid;
					}
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int rx = _random.nextInt(7);
						int ux = 32768 + rx;
						int uy = 32834 + rx; // 상아탑
						pc.start_teleport(ux, uy, 622, pc.getHeading(), 169, true, false);
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70611) { // 속죄의성서
				int lawful;
				byte count = 0;
				lawful = 0;
				switch (s) {
				case "0":
					count = 1;
					lawful = 3000;
					break;
				case "1":
					count = 3;
					lawful = 9000;
					break;
				case "2":
					count = 5;
					lawful = 15000;
					break;
				case "3":
					count = 10;
					lawful = 30000;
					break;
				}

				if (pc.getInventory().consumeItem(3000155, count)) {
					pc.addLawful(lawful);
					pc.sendPackets(new S_ServerMessage(674));
					pc.sendPackets(new S_SkillSound(pc.getId(), 8473));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 8473));
					htmlid = "yuris2";
				} else {
					htmlid = "yuris3";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320159) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(40308, 3000)) {
						pc.getInventory().consumeItem(40308, 3000);
						L1SkillUse aa = new L1SkillUse();
						aa.handleCommands(pc, L1SkillId.DRAGON_HUNTER_BLESS, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
						pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009));
						pc.sendPackets(new S_SkillSound(pc.getId(), 9009));
					} else {
						pc.sendPackets(new S_SystemMessage("아데나 (3,000)원이 부족 합니다."));
						htmlid = "dragonbufff";
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(41921, 30)) {
						pc.getInventory().consumeItem(41921, 30);
						L1SkillUse aa = new L1SkillUse();
						aa.handleCommands(pc, L1SkillId.DRAGON_HUNTER_BLESS, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
						pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009));
						pc.sendPackets(new S_SkillSound(pc.getId(), 9009));
					} else {
						pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 (30)개가 부족 합니다."));
						htmlid = "dragonbufff";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320260) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						L1SkillUse aa = new L1SkillUse();
						pc.setBuffnoch(1);
						aa.handleCommands(pc, L1SkillId.PHYSICAL_ENCHANT_STR, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						aa.handleCommands(pc, L1SkillId.PHYSICAL_ENCHANT_DEX, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						aa.handleCommands(pc, L1SkillId.BLESS_WEAPON, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						
						
						
						
						aa.handleCommands(pc, L1SkillId.miso1, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						aa.handleCommands(pc, L1SkillId.miso2, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						aa.handleCommands(pc, L1SkillId.miso3, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						pc.setBuffnoch(0);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나 1,000원이 부족 합니다."));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50015) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					/*if (!pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("PC방 이용권을 사용 중에만 가능한 행동입니다."));
						return htmlid;
					}*/
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
					
					
					/*if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_SystemMessage("1,000 아데나가 필요합니다."));
					}*/
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50024) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
					
					
					/*if (!pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("PC방 이용권을 사용 중에만 가능한 행동입니다."));
						return htmlid;
					}
					if (pc.getInventory().checkItem(40308, 1000)) {
						pc.getInventory().consumeItem(40308, 1000);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_SystemMessage("1,000 아데나가 필요합니다."));
					}*/
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9000) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					
					
					if (!pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("PC방 이용권을 사용 중에만 가능한 행동입니다."));
						return htmlid;
					}
					if (pc.getInventory().checkItem(140100, 1)) {
						pc.getInventory().consumeItem(140100, 1);
						pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_SystemMessage("축복받은 순간이동 주문서가 필요합니다."));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50082) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50054) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50056) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50020) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50036) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5069) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320051) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50066) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50039) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50051) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50046) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50079) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3000005) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3100005) {
				if (s.equalsIgnoreCase("T_pcbang")) {
					if (pc.PC방_버프) {
						if (pc.getInventory().checkItem(41921, 1)) {
							pc.getInventory().consumeItem(41921, 1);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 1개가 필요합니다."));
						}
					}else {
						if (pc.getInventory().checkItem(41921, 2)) {
							pc.getInventory().consumeItem(41921, 2);
							pc.start_teleport(32769, 32837, 622, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_SystemMessage("픽시의 금빛 깃털 2개가 필요합니다."));
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5126) {
				long time = System.currentTimeMillis();
				int useTime = 10 * 60 * 1000;

				if (pc.getBuffTime() + (useTime) > time) {
					long sec = ((pc.getBuffTime() + (useTime)) - time) / 1000;
					pc.sendPackets(new S_SystemMessage(sec + "초 후에 사용할 수 있습니다."));
					return htmlid;
				}
				if (pc.getLevel() < 80) {
					pc.sendPackets(new S_SystemMessage("최소레벨 80 이상 부터 가능 합니다."));
					return htmlid;
				}
				if (s.equals("0")) { // 마법을 받는다.
					int[] allBuffSkill = { 4048 };
					pc.setBuffnoch(1);
					L1SkillUse l1skilluse = null;
					l1skilluse = new L1SkillUse();
					for (int i = 0; i < allBuffSkill.length; i++) {
						l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
					}
					htmlid = "merisha2";
					pc.curePoison();
					pc.setBuffTime(time);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310121
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320030) {
				if ((pc.getClanRank() != L1Clan.수련) && (pc.getClanRank() != L1Clan.수호)
						&& (pc.getClanRank() != L1Clan.일반) && (pc.getClanRank() != L1Clan.군주)
						&& (pc.getClanRank() != L1Clan.정예)) {
					pc.sendPackets(new S_SystemMessage("해당 성혈원만 받을수 있습니다. "));
					return htmlid;
				}
			/*	if (s.equals("a")) { // 마법을 받는다.
					if (!pc.getClan().decWarPoint()) {
						pc.sendPackets(new S_SystemMessage("혈맹 포인트가 모자랍니다."));
						return htmlid;
					}*/

					int[] allBuffSkill = { 68, 79, 122};
					/**
					 * 68: 이뮨 투 함
					 * 79: 어드밴스 스피릿
					 * 122: 그레이스
					 * */
					pc.setBuffnoch(1);
					L1SkillUse l1skilluse = null;
					l1skilluse = new L1SkillUse();
					for (int i = 0; i < allBuffSkill.length; i++) {
						l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
//					}
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.curePoison();
					// pc.setBuffTime(time);// 버프딜레이
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6200008) {
				long time = System.currentTimeMillis();
				int useTime = 20 * 60 * 1000;// 20분뒤
				if (pc.getBuffTime() + (useTime) > time) {
					long sec = ((pc.getBuffTime() + (useTime)) - time) / 1000;
					pc.sendPackets(new S_SystemMessage(sec + "초 후에 사용할 수 있습니다."));
					return htmlid;
				}
				if (s.equals("a")) { // 마법을 받는다.
					int[] allBuffSkill = { 26, 42, 48, 158 };
					// 덱,힘,블레스웨폰,네이쳐스
					pc.setBuffnoch(1);
					L1SkillUse l1skilluse = null;
					l1skilluse = new L1SkillUse();
					for (int i = 0; i < allBuffSkill.length; i++) {
						l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
					}
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.curePoison();
					pc.setBuffTime(time);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900135) {// 유리에
				L1ItemInstance item = null;
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (s.equalsIgnoreCase("b")) { // 오림님의 이야기를 듣고 싶어요
					if (!OrimController.getInstance().getInDunOpen()) {
						if ((pc.isInParty()) && (pc.getParty().isLeader(pc))) {
							boolean isInMap = true; // 우선 맵에 있는걸로 선언 후
							for (L1PcInstance player : pc.getParty().getMembers()) {
								if (player.getMapId() != 0) {
									isInMap = false;
									break;
								} else if (!player.getInventory().checkItem(410096, 1)) {
									pc.sendPackets(new S_SystemMessage("파티원의 누군가가 시공의 구슬이 없습니다."));
									player.sendPackets(new S_SystemMessage("파티원의 누군가가 시공의 구슬이 없습니다."));
									return htmlid;
								}
							}
							if (pc.getParty().getNumOfMembers() > 2 && isInMap) {
								pc.getParty().getLeader().getName();
								OrimController Indun = OrimController.getInstance();
								Indun.start();
								L1Party party = pc.getParty();
								L1PcInstance[] players = party.getMembers();
								L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84,
										pc.getParty().getLeader().getName() + " 님이 동료들과 함께 해상던전으로 떠났습니다."));
								L1World.getInstance().broadcastServerMessage(
										"\\aD" + pc.getParty().getLeader().getName() + " 님이 동료들과 함께 해상던전으로 떠났습니다.");
								for (L1PcInstance pc1 : players) {
									Indun.addPlayMember(pc1);
									pc1.getInventory().consumeItem(410096, 1);
									// L1Teleport.teleport(pc1, 32796, 32801,
									// (short)9101, pc1.getHeading(), true);
									pc1.start_teleport(32796, 32801, 9101, pc1.getHeading(), 169, true, false);
								}
							} else {
								// htmlid = "id0_1";
								// htmlid = "id0_3";
								pc.sendPackets(new S_NpcChatPacket(npc, "3명~5명의 파티원으로 구성되어야합니다.", 0));
							}
						} else
							htmlid = "id0_2";
					} else {
						pc.sendPackets(new S_NpcChatPacket(npc, "이미 해상던전으로 선발대가 출발했다네. 잠시후 다시오게.", 0));
						htmlid = "";
					}
				}
				if (s.equalsIgnoreCase("c")) { // 항아리 지급
					if (!pc.getInventory().checkItem(410095, 1)) {
						item = pc.getInventory().storeItem(410095, 1);
						pc.sendPackets(new S_ServerMessage(143, "$7918", item.getName()));
					} else {
						htmlid = "j_html03";
					}
				} else if (s.equalsIgnoreCase("a")) { // 비밀 연구실 텔
					if (pc.getInventory().checkItem(410096, 1) && pc.getInventory().checkItem(L1ItemId.ADENA, 10000)) {
						pc.getInventory().consumeItem(410096, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 10000);
						// L1Teleport.teleport(pc, 32744, 32860, (short) 9100,
						// 5, true);
						pc.start_teleport(32744, 32860, 9100, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						htmlid = "j_html02";
					}
				} else if (s.equalsIgnoreCase("d")) { // 일기장 복원
					if (pc.getInventory().checkItem(410097, 1) && pc.getInventory().checkItem(410098, 1)
							&& pc.getInventory().checkItem(410099, 1) && pc.getInventory().checkItem(410100, 1)
							&& pc.getInventory().checkItem(410101, 1) && pc.getInventory().checkItem(410102, 1)
							&& pc.getInventory().checkItem(410103, 1) && pc.getInventory().checkItem(410104, 1)
							&& pc.getInventory().checkItem(410105, 1) && pc.getInventory().checkItem(410106, 1)) {
						pc.getInventory().consumeItem(410097, 1);
						pc.getInventory().consumeItem(410098, 1);
						pc.getInventory().consumeItem(410099, 1);
						pc.getInventory().consumeItem(410100, 1);
						pc.getInventory().consumeItem(410101, 1);
						pc.getInventory().consumeItem(410102, 1);
						pc.getInventory().consumeItem(410103, 1);
						pc.getInventory().consumeItem(410104, 1);
						pc.getInventory().consumeItem(410105, 1);
						pc.getInventory().consumeItem(410106, 1);
						htmlid = "j_html04";
						pc.getInventory().storeItem(410107, 1); // 어두운 하딘의 일기장
					} else {
						htmlid = "j_html06";
						pc.sendPackets(new S_SystemMessage("10권의 일기가 모두 필요합니다."));
					}
				} else if (s.equalsIgnoreCase("e")) {
					if (pc.getInventory().checkItem(410144) && pc.getInventory().checkItem(410145)
							&& pc.getInventory().checkItem(410146) && pc.getInventory().checkItem(410147)
							&& pc.getInventory().checkItem(410148) && pc.getInventory().checkItem(410149)
							&& pc.getInventory().checkItem(410150) && pc.getInventory().checkItem(410151)
							&& pc.getInventory().checkItem(410152) && pc.getInventory().checkItem(410153)
							&& pc.getInventory().checkItem(410154) && pc.getInventory().checkItem(410155)
							&& pc.getInventory().checkItem(410156) && pc.getInventory().checkItem(410157)
							&& pc.getInventory().checkItem(410158) && pc.getInventory().checkItem(410159)
							&& pc.getInventory().checkItem(410160) && pc.getInventory().checkItem(410161)) {
						pc.getInventory().consumeItem(410144, 1);
						pc.getInventory().consumeItem(410145, 1);
						pc.getInventory().consumeItem(410146, 1);
						pc.getInventory().consumeItem(410147, 1);
						pc.getInventory().consumeItem(410148, 1);
						pc.getInventory().consumeItem(410149, 1);
						pc.getInventory().consumeItem(410150, 1);
						pc.getInventory().consumeItem(410151, 1);
						pc.getInventory().consumeItem(410152, 1);
						pc.getInventory().consumeItem(410153, 1);
						pc.getInventory().consumeItem(410154, 1);
						pc.getInventory().consumeItem(410155, 1);
						pc.getInventory().consumeItem(410156, 1);
						pc.getInventory().consumeItem(410157, 1);
						pc.getInventory().consumeItem(410158, 1);
						pc.getInventory().consumeItem(410159, 1);
						pc.getInventory().consumeItem(410160, 1);
						pc.getInventory().consumeItem(410161, 1);
						htmlid = "j_html04";
						pc.getInventory().storeItem(410143, 1);// 오림의 일기장 획득
					} else {
						htmlid = "j_html06"; // 일기장에 대한 정보가 부족.
						pc.sendPackets(new S_SystemMessage("18권의 일기가 모두 필요합니다."));
					}
				}
				/** 리들 엘릭서 룬 변경 안됨 - 55레벨 밖에 없음**/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000055) {
				if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("E")) {
					if (pc.getInventory().checkItem(60034, 1)) {
						pc.getInventory().consumeItem(60034, 1);
						if (s.equals("A")) {
							pc.getInventory().storeItem(60036, 1); // 힘의 엘릭서 룬
							pc.sendPackets(new S_SystemMessage("힘의 엘릭서 룬을 획득했습니다."));
						} else if (s.equals("B")) {
							pc.getInventory().storeItem(60037, 1); // 민첩의 엘릭서 룬
							pc.sendPackets(new S_SystemMessage("민첩의 엘릭서 룬을 획득했습니다."));
						} else if (s.equals("C")) {
							pc.getInventory().storeItem(60038, 1); // 체력의 엘릭서 룬
							pc.sendPackets(new S_SystemMessage("체력의 엘릭서 룬을 획득했습니다."));
						} else if (s.equals("D")) {
							pc.getInventory().storeItem(60039, 1); // 지식의 엘릭서 룬
							pc.sendPackets(new S_SystemMessage("지식의 엘릭서 룬을 획득했습니다."));
						} else if (s.equals("E")) {
							pc.getInventory().storeItem(60040, 1); // 지혜의 엘릭서 룬
							pc.sendPackets(new S_SystemMessage("지혜의 엘릭서 룬을 획득했습니다."));
						}
					} else {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "riddle2"));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000054) {
				if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("E")) {
					if (pc.getLevel() >= 55) {
						if (pc.getInventory().checkItem(60031, 1) 
							&& pc.getInventory().checkItem(60032, 1)) {
							pc.getInventory().consumeItem(60031, 1);
							pc.getInventory().consumeItem(60032, 1);
							if (s.equals("A")) {
								pc.getInventory().storeItem(60036, 1); // 힘의 엘릭서
								// 룬주머니
							} else if (s.equals("B")) {
								pc.getInventory().storeItem(60037, 1); // 민첩의
								// 엘릭서 룬
							} else if (s.equals("C")) {
								pc.getInventory().storeItem(60038, 1); // 체력의
								// 엘릭서 룬
							} else if (s.equals("D")) {
								pc.getInventory().storeItem(60039, 1); // 지식의
								// 엘릭서 룬
							} else if (s.equals("E")) {
								pc.getInventory().storeItem(60040, 1); // 지혜의
								// 엘릭서 룬
							}
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune6"));

						} else {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
							pc.sendPackets(new S_SystemMessage("낡은 고서와 마법사의 돌이 필요하네. 마법사의 돌은 우측으로가서 포이에게 구매하게나."));
						}
					} else {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 1000001) {// 땅굴개미
				int locx = 0, locy = 0, map = 0;
				if (s.equalsIgnoreCase("b")) {// 1번동굴
					locx = 32783;
					locy = 32751;
					map = 43;
				} else if (s.equalsIgnoreCase("c")) {// 2번동굴
					locx = 32798;
					locy = 32754;
					map = 44;
				} else if (s.equalsIgnoreCase("d")) {// 3번동굴
					locx = 32776;
					locy = 32731;
					map = 45;
				} else if (s.equalsIgnoreCase("e")) {// 4번동굴
					locx = 32787;
					locy = 32795;
					map = 46;
				} else if (s.equalsIgnoreCase("f")) {// 5번동굴
					locx = 32796;
					locy = 32745;
					map = 47;
				} else if (s.equalsIgnoreCase("g")) {// 6번동굴
					locx = 32768;
					locy = 32805;
					map = 50;
				}
				if (pc.getInventory().checkItem(40308, 500)) {
					pc.getInventory().consumeItem(40308, 500);
					pc.start_teleport(locx, locy, map, pc.getHeading(), 169, true, false);
				} else {
					htmlid = "cave2";
				}

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 1000002) { // 용의전령
				if (s.equalsIgnoreCase("1")) {
					if (pc.getLevel() >= 56) {
						pc.start_teleport(32770, 32759, 30, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						htmlid = "dvdgate2";
					}
				}
				/** 클라우디아 라라 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 202057) {
				if (pc.getLevel() >= 1 & pc.getLevel() <= 55) {
					htmlid = "tel_lala1";
					pc.start_teleport(32646, 32865, 7783, pc.getHeading(), 169, true, false);
				}
				/** 전사단의 영혼 신규영입 이벤트 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7320010) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getLevel() >= 56 && pc.getLevel() <= 59) {
						if (!pc.getInventory().consumeItem(700016, 3)) {
							pc.sendPackets(new S_SystemMessage("재료 또는 레벨이 맞지 않습니다."));
							htmlid = "archekins2";
							return htmlid;
						}
						pc.addExp((ExpTable.getExpByLevel(60) - 1) - pc.getExp()
								+ ((ExpTable.getExpByLevel(60) - 1) / 30000000));
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
						pc.sendPackets(new S_SystemMessage("\\aA레벨업을 축하합니다. 이제 다음 단계를 진행 하시오."));
						// htmlid = "archekins2";
					} else {
						pc.sendPackets(new S_SystemMessage("재료 또는 레벨이 맞지 않습니다."));
						htmlid = "archekins2";
						return htmlid;
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getLevel() >= 60 && pc.getLevel() <= 64) {
						if (!pc.getInventory().consumeItem(700016, 5)) {
							pc.sendPackets(new S_SystemMessage("재료 또는 레벨이 맞지 않습니다."));
							htmlid = "archekins2";
							return htmlid;
						}
						pc.addExp((ExpTable.getExpByLevel(65) - 1) - pc.getExp()
								+ ((ExpTable.getExpByLevel(65) - 1) / 30000000));
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
						pc.sendPackets(new S_SystemMessage("\\aA레벨업을 축하합니다. 이제 다음 단계를 진행 하시오."));
						// htmlid = "archekins2";
					} else {
						pc.sendPackets(new S_SystemMessage("재료 또는 레벨이 맞지 않습니다."));
						htmlid = "archekins2";
						return htmlid;
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getLevel() >= 65 && pc.getLevel() <= 69) {
						if (!pc.getInventory().consumeItem(700016, 10)) {
							pc.sendPackets(new S_SystemMessage("재료 또는 레벨이 맞지 않습니다."));
							htmlid = "archekins2";
							return htmlid;
						}
						pc.getInventory().storeItem(3000213, 1);
						pc.addExp((ExpTable.getExpByLevel(70) - 1) - pc.getExp()
								+ ((ExpTable.getExpByLevel(70) - 1) / 30000000));
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
						pc.sendPackets(new S_SystemMessage("\\aA아크프리패스를 완료 하였습니다. 아이템을 지급 하였습니다."));
					} else {
						pc.sendPackets(new S_SystemMessage("재료 또는 레벨이 맞지 않습니다."));
						htmlid = "archekins2";
						return htmlid;
					}
				}
				/** 클라우디아 훈련 군터 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 202065) {// 군터
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(447011, 1)) {
					} else {
						if (pc.getLevel() <= 90) {// 해당레벨이하일경우
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "군터: 퀘스트를 먼저 진행 하시오.."));
							htmlid = "archgunter2";
							return htmlid;
						}
						if (pc.getLevel() >= 90) {// 해당레벨이상일경우
							htmlid = "archgunter1";
							pc.sendPackets(new S_SystemMessage("군터: 퀘스트를 먼저 진행 하시오.."));
							pc.start_teleport(32646, 32865, 7783, pc.getHeading(), 169, true, false);
						}
					}
				}
				/** 클라우디아 훈련교관 테온 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 202066) {// 테온
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(99115, 5)) {
						pc.sendPackets(new S_SystemMessage("'클라우디아 이동 주문서' 을 소지하고 계십니다."));
						htmlid = "";
					} else {
						pc.getInventory().storeItem(99115, 5);
						pc.sendPackets(new S_SystemMessage("'클라우디아 이동 주문서'을 지급 하였습니다.."));
					}
				}
				// 군터
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 60169) {
				if (s.equalsIgnoreCase("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_GUNTER, pc.getId(), pc.getX(), pc.getY(), null,
							0, L1SkillUse.TYPE_SPELLSC);
				}
				// 크레이
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7200026) {
				if (s.equalsIgnoreCase("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_CRAY, pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_SPELLSC);
					htmlid = "grayknight2";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7135) {
				if (s.equalsIgnoreCase("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_Vala, pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_SPELLSC);
					htmlid = "vdeath2";
				}
				// 저주받은 무녀 사엘 (입구 npc)
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039009) {
				if (s.equals("a")) {
					new L1SkillUse().handleCommands(pc, L1SkillId.BUFF_SAEL, pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_SPELLSC);
					if (!pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
						pc.setSkillEffect(STATUS_UNDERWATER_BREATH, 1800 * 1000);
						pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 1800));
					}
				}
				// 쿠루 몽섬 집시촌이동
			} else if (npcid == 7000097) {
				if (s.equalsIgnoreCase("teleport tamshop")) {
					pc.start_teleport(33964, 32953, 4, pc.getHeading(), 169, true, false);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50015) {
				if (s.equalsIgnoreCase("teleport island-silver")) {//
					if (pc.getInventory().checkItem(40308, 1500)) {
						pc.getInventory().consumeItem(40308, 1500);
						pc.start_teleport(33080, 33392, 4, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 522) {
				if (s.equalsIgnoreCase("giveto")) {
					if (pc.getInventory().checkItem(40308, 200000)) {
						pc.getInventory().consumeItem(40308, 200000);
						L1SkillUse aa = new L1SkillUse();
						aa.handleCommands(pc, L1SkillId.HUNTER_BLESS, pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009));
						pc.sendPackets(new S_SkillSound(pc.getId(), 9009));
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("기부하기전에..용사님 걱정부터 하셔야 할 것 같아요... 아데나가 부족 해요.."));
						htmlid = "";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81210) {
				int locx = 0, locy = 0, mapid = 0;
				if (s.equalsIgnoreCase("b")) {//
					locx = 33442;
					locy = 32797;
					mapid = 4;
				} else if (s.equalsIgnoreCase("C")) {//
					locx = 34056;
					locy = 32279;
					mapid = 4;
				} else if (s.equalsIgnoreCase("D")) {// 발라 둥지
					locx = 33705;
					locy = 32504;
					mapid = 4;
				} else if (s.equalsIgnoreCase("E")) {//
					locx = 33614;
					locy = 33253;
					mapid = 4;
				} else if (s.equalsIgnoreCase("F")) {//
					locx = 33050;
					locy = 32780;
					mapid = 4;
				} else if (s.equalsIgnoreCase("G")) {//
					locx = 32631;
					locy = 32770;
					mapid = 4;
				} else if (s.equalsIgnoreCase("H")) {//
					locx = 33080;
					locy = 33392;
					mapid = 4;
				} else if (s.equalsIgnoreCase("I")) {//
					locx = 32617;
					locy = 33201;
					mapid = 4;
				} else if (s.equalsIgnoreCase("J")) {// 오크 숲
					locx = 32741;
					locy = 32450;
					mapid = 4;
				} else if (s.equalsIgnoreCase("K")) {//
					locx = 32581;
					locy = 32940;
					mapid = 0;
				} else if (s.equalsIgnoreCase("L")) {//
					locx = 33958;
					locy = 33364;
					mapid = 4;
				} else if (s.equalsIgnoreCase("N")) {//
					locx = 32800;
					locy = 32927;
					mapid = 800;
				} else if (s.equalsIgnoreCase("V")) {// 데포류즈앞
					locx = 32595;
					locy = 33163;
					mapid = 4;
				}
				if (pc.getInventory().checkItem(40308, 100)) {
					pc.getInventory().consumeItem(40308, 100);
					pc.start_teleport(locx, locy, mapid, pc.getHeading(), 169, true, false);
					htmlid = "";
				} else {
					htmlid = "pctel2";
				}
				// 리키 수련의텔레포트
			} else if (npcid == 70798) {
				if (s.equalsIgnoreCase("a")) {// 숨겨진계곡
					if (pc.getLevel() >= 1 & pc.getLevel() <= 45) {
						pc.start_teleport(32684, 32851, 2005, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fQ리키: \\f3[Lv.45]\\fQ이하만 입장 허용 레벨입니다."));
					}
				} else if (s.equalsIgnoreCase("b")) {// 기란마을
					pc.start_teleport(33436, 32799, 4, pc.getHeading(), 169, true, false);
				} else if (s.equalsIgnoreCase("c")) {// 라우풀신전
					if (pc.getLevel() >= 10 & pc.getLevel() <= 29) {
						pc.start_teleport(33184, 33449, 4, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "은기사 필드 이동 가능레벨 10 ~ 29"));
					}
				} else if (s.equalsIgnoreCase("d")) {// 카오틱신전
					if (pc.getLevel() >= 10 & pc.getLevel() <= 29) {
						pc.start_teleport(33066, 33218, 4, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "은기사 필드 이동 가능레벨 10 ~ 29"));
					}
				} else if (s.equalsIgnoreCase("f")) {// 수련던전
					if (pc.getLevel() >= 10 & pc.getLevel() < 20) {
						// L1Teleport.teleport(pc, 32801, 32806, (short) 25,
						// pc.getHeading(), true);
						pc.start_teleport(32801, 32806, 25, pc.getHeading(), 169, true, false);
					} else if (pc.getLevel() >= 20 & pc.getLevel() < 30) {
						// L1Teleport.teleport(pc, 32806, 32746, (short) 26,
						// pc.getHeading(), true);
						pc.start_teleport(32806, 32746, 26, pc.getHeading(), 169, true, false);
					} else if (pc.getLevel() >= 30 & pc.getLevel() < 40) {
						// L1Teleport.teleport(pc, 32808, 32766, (short) 27,
						// pc.getHeading(), true);
						pc.start_teleport(32808, 32766, 27, pc.getHeading(), 169, true, false);
					} else if (pc.getLevel() >= 40 & pc.getLevel() < 44) {
						// L1Teleport.teleport(pc, 32796, 32799, (short) 28,
						// pc.getHeading(), true);
						pc.start_teleport(32796, 32799, 28, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "수련 던전 이동 가능레벨 10 ~ 44"));
					}
				} else if (s.equalsIgnoreCase("e")) {// 폭풍던전 불신 Lv 45~51
					if (pc.getLevel() >= 45 & pc.getLevel() <= 51) {
						// L1Teleport.teleport(pc, 32807, 32789, (short) 2010,
						// pc.getHeading(), true);
						pc.start_teleport(32807, 32789, 2010, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_ChatPacket(pc, "폭풍 수련 던전 이동 가능레벨 45 ~ 51"));
					}
				}
			} else if (npcid == 50078) {
				if (pc.getLevel() <= 99) {
					pc.sendPackets(new S_SystemMessage("수상한 하늘정원(PC) 에서 정령의 오브를 통해 이동하세요."));
					return htmlid;
				}
				/*
				 * if (s.equalsIgnoreCase("D_abyss")) { int rx =
				 * _random.nextInt(3); int ry = _random.nextInt(3); int ux =
				 * 32855 + rx; int uy = 32859 + ry; pc.start_teleport(ux, uy,
				 * 623, pc.getHeading(), 169, true, false); }
				 */
				/** 지구르 천상 업데이트 **/
			} else if (npcid == 7320034) {
				if (pc.getLevel() < 99) {
					
					if (s.equalsIgnoreCase("a")) {
						int rx = _random.nextInt(3);
						int ry = _random.nextInt(3);
						int ux = 32819 + rx;
						int uy = 32903 + ry;
						pc.start_teleport(ux, uy, 1911, pc.getHeading(), 169, true, false);
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aD이벤트 사냥터 입니다."));
						pc.sendPackets(new S_SystemMessage("이벤트 사냥터 입니다."));
					}
				} else {
					pc.sendPackets(new S_SystemMessage("레벨 74 까지 입장 가능합니다."));
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "레벨 74 까지 입장 가능합니다. 그후 기란 감옥 으로 입장하세요."));
				}
				/** 스타크 공성장 이동 **/
			} else if (npcid == 7320033) { // 몽섬 헤이트 리뉴얼
				if (s.equalsIgnoreCase("a")) { // 입장
					int rx = _random.nextInt(5);
					int ry = _random.nextInt(5);
					int ux = 33630 + rx;
					int uy = 32763 + ry;
					pc.start_teleport(ux, uy, 4, pc.getHeading(), 169, true, false);
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "기란 공성장으로 이동 됩니다."));
				} else if (s.equalsIgnoreCase("b")) {// 기란마을
					int rx = _random.nextInt(5);
					int ry = _random.nextInt(5);
					int ux = 33093 + rx;
					int uy = 32769 + ry;
					pc.start_teleport(ux, uy, 4, pc.getHeading(), 169, true, false);
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "켄트 공성장으로 이동 됩니다."));
				}
			} else if (npcid == 7310174) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(3000211, 300)) {
						pc.getInventory().consumeItem(3000211, 300);
						pc.getInventory().storeItem(3000210, 1);// 준다
						pc.sendPackets(new S_SystemMessage("감사합니다. 다음에 또 오세요."));
					} else {
						pc.sendPackets(new S_SystemMessage("에바의 은총 총 [300]개가 필요하다오.."));
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(3000211, 500)) {
						pc.getInventory().consumeItem(3000211, 500);
						pc.getInventory().storeItem(3000210, 2);// 준다
						pc.sendPackets(new S_SystemMessage("감사합니다. 다음에 또 오세요."));
					} else {
						pc.sendPackets(new S_SystemMessage("에바의 은총 총 [500]개가 필요하다오.."));
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().checkItem(3000211, 1000)) {
						pc.getInventory().consumeItem(3000211, 1000);
						pc.getInventory().storeItem(3000210, 5);// 준다
						pc.sendPackets(new S_SystemMessage("감사합니다. 다음에 또 오세요."));
					} else {
						pc.sendPackets(new S_SystemMessage("에바의 은총 총 [1000]개가 필요하다오.."));
					}
				}
				// 바무트 제작리뉴얼
			} else if (npcid == 70690) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40053, 10)
							&& pc.getInventory().checkItem(40393, 5)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(40053, 10);
						pc.getInventory().consumeItem(40393, 5);
						pc.getInventory().storeItem(222307, 1);// 완력의 부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 최고급 루비(10), 화룡 비늘(5)"));
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40052, 10)
							&& pc.getInventory().checkItem(40396, 5)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(40052, 10);
						pc.getInventory().consumeItem(40396, 5);
						pc.getInventory().storeItem(22359, 1);// 지혜의 부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 최고급 다이아몬드(10), 지룡 비늘(5)"));
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40055, 10)
							&& pc.getInventory().checkItem(40394, 5)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(40055, 10);
						pc.getInventory().consumeItem(40394, 5);
						pc.getInventory().storeItem(222308, 1);// 민첩의 부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 최고급 에메랄드(10), 풍룡 비늘(5)"));
					}
				} else if (s.equalsIgnoreCase("d")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(40054, 10)
							&& pc.getInventory().checkItem(40395, 5)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(40054, 10);
						pc.getInventory().consumeItem(40395, 5);
						pc.getInventory().storeItem(222309, 1);// 지식의부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 최고급 사파이어(10), 수룡 비늘(5)"));
					}
				} else if (s.equalsIgnoreCase("e")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560030)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(560030, 1);
						pc.getInventory().storeItem(222307, 1);// 완력의 부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 화령 속성 변환 주문서(1)"));
					}
				} else if (s.equalsIgnoreCase("f")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560033)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(560033, 1);
						pc.getInventory().storeItem(22359, 1);// 지혜의 부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 지령 속성 변환 주문서(1)"));
					}
				} else if (s.equalsIgnoreCase("g")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560032)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(560032, 1);
						pc.getInventory().storeItem(222308, 1);// 민첩의 부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 풍령 속성 변환 주문서(1)"));
					}
				} else if (s.equalsIgnoreCase("h")) {
					if (pc.getInventory().checkItem(410061, 50) && pc.getInventory().checkItem(560031)) {
						pc.getInventory().consumeItem(410061, 50);
						pc.getInventory().consumeItem(560031, 1);
						pc.getInventory().storeItem(222309, 1);// 지식의 부츠
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("마물의 기운(50), 수령 속성 변환 주문서(1)"));
					}
				}
				// 쥬비드 기억의 확장 구슬 제작
			} else if (npcid == 7310149) {
				if (s.equalsIgnoreCase("request memory crystal")) {
					if (pc.getInventory().checkItem(3000200, 1) && pc.getInventory().checkItem(40308, 20000)) {
						pc.getInventory().consumeItem(3000200, 1);
						pc.getInventory().consumeItem(40308, 20000);
						pc.getInventory().storeItem(700022, 1);// 기억확장구슬
						pc.sendPackets(new S_SystemMessage("제작이 완료 되었습니다."));
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("아데나(20,000) 또는 기억의파편(1)개 가 부족합니다"));
					}
				}
			} else if (npcid == 50045) { // 몽섬 헤이트 리뉴얼
				if (s.equalsIgnoreCase("a")) { // 입장
					if (pc.getInventory().checkItem(810000, 1)) {
						pc.getInventory().consumeItem(810000, 1);
						pc.start_teleport(32800, 32798, 1935, pc.getHeading(), 169, true, false);
					} else {
						pc.sendPackets(new S_SystemMessage("입장을 위해 유니콘의 사원 열쇠가 필요합니다."));
					}
				} else if (s.equalsIgnoreCase("b")) {// 기란마을
					pc.start_teleport(33436, 32799, 4, pc.getHeading(), 169, true, false);
				}

			} else if (npcid == 7200000) { // 몽섬 에킨스
				if (s.equalsIgnoreCase("d")) { // 촉매: 유니콘의 성장 징표 / 재료: 드래곤의
					// 다이아몬드(1)
					if (pc.getInventory().checkItem(3000215, 1) && pc.getInventory().checkItem(1000004, 1)) {
						pc.getInventory().consumeItem(3000215, 1);
						pc.getInventory().consumeItem(1000004, 1);
						pc.getInventory().storeItem(810010, 8);
						에킨스경험치6(pc);
					} else {
						htmlid = "ekins5";
					}
				} else if (s.equalsIgnoreCase("c")) { // 촉매: 유니콘의 성장 징표
					if (pc.getInventory().checkItem(3000215, 1)) {
						pc.getInventory().consumeItem(3000215, 1);
						pc.getInventory().storeItem(810010, 5);
						에킨스경험치2(pc);
					} else {
						htmlid = "ekins5";
					}
				} else if (s.equalsIgnoreCase("b")) { // 촉매: 성장의 구슬
					if (pc.getInventory().checkItem(810002, 1)) {
						pc.getInventory().consumeItem(810002, 1);
						pc.getInventory().storeItem(810010, 1);
						에킨스경험치6(pc);
					} else {
						htmlid = "ekins5";
					}
				} else if (s.equalsIgnoreCase("a")) { // 촉매: 성장의 구슬 조각
					if (pc.getInventory().checkItem(810001, 1)) {
						pc.getInventory().consumeItem(810001, 1);
						pc.getInventory().storeItem(810010, 1);
						에킨스경험치2(pc);
					} else {
						htmlid = "ekins5";
					}
				}

			} else if (npcid == 7200001) { // 중앙사원 문지기
				if (s.equalsIgnoreCase("enter")) {
					FantasyIslandSystem.getInstance().startRaid(pc);
				}

				/** 하버트 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70641) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(40395, 1) // 수룡비늘
							&& pc.getInventory().checkItem(410061, 10) // 마물의기운
							&& pc.getInventory().checkItem(820004, 300)) { // 마력의실타래

						pc.getInventory().consumeItem(40395, 1);
						pc.getInventory().consumeItem(410061, 10);
						pc.getInventory().consumeItem(820004, 300);
						pc.getInventory().storeItem(20273, 1);// 마력의 장갑
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("수룡 비늘(1)"));
						pc.sendPackets(new S_SystemMessage("마물의 기운(10)"));
						pc.sendPackets(new S_SystemMessage("마력의 실타래(300)"));
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkEnchantItem(20273, 7, 1) // +7
							// 마력의장갑
							&& pc.getInventory().checkItem(40395, 1) // 수룡비늘
							&& pc.getInventory().checkItem(410061, 10) // 마물의기운
							&& pc.getInventory().checkItem(820004, 300) // 마력의실타래
							&& pc.getInventory().checkItem(820005, 1)) { // 마력의핵

						pc.getInventory().consumeEnchantItem(20273, 7, 1);
						pc.getInventory().consumeItem(40395, 1);
						pc.getInventory().consumeItem(410061, 10);
						pc.getInventory().consumeItem(820004, 300);
						pc.getInventory().consumeItem(820005, 1);
						pc.getInventory().storeItem(20274, 1);// 빛나는 마력의 장갑
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						pc.sendPackets(new S_SystemMessage("수룡 비늘(1)"));
						pc.sendPackets(new S_SystemMessage("마물의 기운(10)"));
						pc.sendPackets(new S_SystemMessage("마력의 실타래(300)"));
						pc.sendPackets(new S_SystemMessage("마력의 핵(1)"));
						pc.sendPackets(new S_SystemMessage("+7 마력의 장갑(1)"));
					}
				}
				/** 잊섬 리뉴얼 **/
			} else if (npcid == 1231231) {
				
				  if (pc.getLevel() <= 84) { pc.sendPackets(new
				  S_SystemMessage("레벨 84 이상만 입장이 가능 합니다."));
				  pc.sendPackets(new S_PacketBox(84,"레벨 80 이상만 입장이 가능 합니다."));
				  htmlid = "fg_isval_fl1"; return htmlid; }
				 
				if (s.equalsIgnoreCase("a")) {
					if (잊섬Controller.getInstance().get잊섬Start() == true) {
						Random random = new Random();
						int i13 = 32786 + random.nextInt(2);
						int k19 = 32761 + random.nextInt(2);
						pc.start_teleport(i13, k19, 1710, pc.getHeading(), 169, true, false);
						pc.sendPackets(new S_SystemMessage("잊혀진 섬 열린 시간부터 '2'시간 동안 입장이 가능합니다."));
					} else {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "잊혀진 섬 시간을 확인하시길 바랍니다."));
						pc.sendPackets(new S_SystemMessage("잊혀진 섬 시간을 확인하시길 바랍니다."));
						htmlid = "fg_isval_cl";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71180) { // 제이프
				if (s.equalsIgnoreCase("A")) { // 꿈꾸는 곰인형
					if (pc.getInventory().consumeItem(49026, 1000)) {
						pc.getInventory().storeItem(41093, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("B")) { // 향수
					if (pc.getInventory().consumeItem(49026, 5000)) {
						pc.getInventory().storeItem(41094, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("C")) { // 드레스
					if (pc.getInventory().consumeItem(49026, 10000)) {
						pc.getInventory().storeItem(41095, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("D")) { // 반지
					if (pc.getInventory().consumeItem(49026, 100000)) {
						pc.getInventory().storeItem(41096, 1);
						htmlid = "jp6";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("E")) { // 위인전
					if (pc.getInventory().consumeItem(49026, 1000)) {
						pc.getInventory().storeItem(41098, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("F")) { // 세련된 모자
					if (pc.getInventory().consumeItem(49026, 5000)) {
						pc.getInventory().storeItem(41099, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("G")) { // 최고급 와인
					if (pc.getInventory().consumeItem(49026, 10000)) {
						pc.getInventory().storeItem(41100, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				} else if (s.equalsIgnoreCase("H")) { // 알 수 없는 열쇠
					if (pc.getInventory().consumeItem(49026, 100000)) {
						pc.getInventory().storeItem(41101, 1);
						htmlid = "jp8";
					} else {
						htmlid = "jp5";
					}
				}
				
				
				
				
				
				
				
				

				// 조우의 불골렘 리뉴얼
			} else if (npcid == 5066) {
				int enchant = 0;
				int itemId = 0;
				int oldArmor = 0;
				L1NpcInstance npc = (L1NpcInstance) obj;
				String npcName = npc.getNpcTemplate().get_name();
				if (s.equalsIgnoreCase("1")) { // [+7]마력의 단검
					if ((pc.getInventory().checkEnchantItem(5, 8, 1)
							|| pc.getInventory().checkEnchantItem(6, 8, 1)
							|| pc.getInventory().checkEnchantItem(32, 8, 1)
							|| pc.getInventory().checkEnchantItem(37, 8, 1)
							|| pc.getInventory().checkEnchantItem(41, 8, 1)
							|| pc.getInventory().checkEnchantItem(42, 8, 1)
							|| pc.getInventory().checkEnchantItem(52, 8, 1)
							|| pc.getInventory().checkEnchantItem(64, 8, 1)
							|| pc.getInventory().checkEnchantItem(99, 8, 1)
							|| pc.getInventory().checkEnchantItem(104, 8, 1)
							|| pc.getInventory().checkEnchantItem(125, 8, 1)
							|| pc.getInventory().checkEnchantItem(129, 8, 1)
							|| pc.getInventory().checkEnchantItem(131, 8, 1)
							|| pc.getInventory().checkEnchantItem(145, 8, 1)
							|| pc.getInventory().checkEnchantItem(148, 8, 1)
							|| pc.getInventory().checkEnchantItem(180, 8, 1)
							|| pc.getInventory().checkEnchantItem(181, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(5, 8, 1)
								|| pc.getInventory().consumeEnchantItem(6, 8, 1)
								|| pc.getInventory().consumeEnchantItem(32, 8, 1)
								|| pc.getInventory().consumeEnchantItem(37, 8, 1)
								|| pc.getInventory().consumeEnchantItem(41, 8, 1)
								|| pc.getInventory().consumeEnchantItem(42, 8, 1)
								|| pc.getInventory().consumeEnchantItem(52, 8, 1)
								|| pc.getInventory().consumeEnchantItem(64, 8, 1)
								|| pc.getInventory().consumeEnchantItem(99, 8, 1)
								|| pc.getInventory().consumeEnchantItem(104, 8, 1)
								|| pc.getInventory().consumeEnchantItem(125, 8, 1)
								|| pc.getInventory().consumeEnchantItem(129, 8, 1)
								|| pc.getInventory().consumeEnchantItem(131, 8, 1)
								|| pc.getInventory().consumeEnchantItem(145, 8, 1)
								|| pc.getInventory().consumeEnchantItem(148, 8, 1)
								|| pc.getInventory().consumeEnchantItem(180, 8, 1)
								|| pc.getInventory().consumeEnchantItem(181, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						인첸트지급(pc, 602, 1, 7); // +7마력의 단검이 제작된다.
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("2")) {// [+8]마력의 단검
					if ((pc.getInventory().checkEnchantItem(5, 9, 1) || pc.getInventory().checkEnchantItem(6, 9, 1)
							|| pc.getInventory().checkEnchantItem(32, 9, 1)
							|| pc.getInventory().checkEnchantItem(37, 9, 1)
							|| pc.getInventory().checkEnchantItem(41, 9, 1)
							|| pc.getInventory().checkEnchantItem(42, 9, 1)
							|| pc.getInventory().checkEnchantItem(52, 9, 1)
							|| pc.getInventory().checkEnchantItem(64, 9, 1)
							|| pc.getInventory().checkEnchantItem(99, 9, 1)
							|| pc.getInventory().checkEnchantItem(104, 9, 1)
							|| pc.getInventory().checkEnchantItem(125, 9, 1)
							|| pc.getInventory().checkEnchantItem(129, 9, 1)
							|| pc.getInventory().checkEnchantItem(131, 9, 1)
							|| pc.getInventory().checkEnchantItem(145, 9, 1)
							|| pc.getInventory().checkEnchantItem(148, 9, 1)
							|| pc.getInventory().checkEnchantItem(180, 9, 1)
							|| pc.getInventory().checkEnchantItem(181, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(5, 9, 1)
								|| pc.getInventory().consumeEnchantItem(6, 9, 1)
								|| pc.getInventory().consumeEnchantItem(32, 9, 1)
								|| pc.getInventory().consumeEnchantItem(37, 9, 1)
								|| pc.getInventory().consumeEnchantItem(41, 9, 1)
								|| pc.getInventory().consumeEnchantItem(42, 9, 1)
								|| pc.getInventory().consumeEnchantItem(52, 9, 1)
								|| pc.getInventory().consumeEnchantItem(64, 9, 1)
								|| pc.getInventory().consumeEnchantItem(99, 9, 1)
								|| pc.getInventory().consumeEnchantItem(104, 9, 1)
								|| pc.getInventory().consumeEnchantItem(125, 9, 1)
								|| pc.getInventory().consumeEnchantItem(129, 9, 1)
								|| pc.getInventory().consumeEnchantItem(131, 9, 1)
								|| pc.getInventory().consumeEnchantItem(145, 9, 1)
								|| pc.getInventory().consumeEnchantItem(148, 9, 1)
								|| pc.getInventory().consumeEnchantItem(180, 9, 1)
								|| pc.getInventory().consumeEnchantItem(181, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						인첸트지급(pc, 602, 1, 8);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("3")) {// [+7]환영의 체인소드
					if ((pc.getInventory().checkEnchantItem(500, 8, 1) || pc.getInventory().checkEnchantItem(501, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(500, 8, 1)
								|| pc.getInventory().consumeEnchantItem(501, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						인첸트지급(pc, 202001, 1, 7);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("4")) {// [+8]환영의 체인소드
					if ((pc.getInventory().checkEnchantItem(500, 9, 1) || pc.getInventory().checkEnchantItem(501, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(500, 9, 1)
								|| pc.getInventory().consumeEnchantItem(501, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						인첸트지급(pc, 202001, 1, 8);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("5")) {// [+7]공명의 키링크
					if ((pc.getInventory().checkEnchantItem(503, 8, 1) || pc.getInventory().checkEnchantItem(504, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(503, 8, 1)
								|| pc.getInventory().consumeEnchantItem(504, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						인첸트지급(pc, 1135, 1, 7);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("6")) {// [+8]공명의 키링크
					if ((pc.getInventory().checkEnchantItem(503, 9, 1) || pc.getInventory().checkEnchantItem(504, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(503, 9, 1)
								|| pc.getInventory().consumeEnchantItem(504, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						인첸트지급(pc, 1135, 1, 8);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("7")) {// [+7]파괴의 크로우
					if ((pc.getInventory().checkEnchantItem(81, 8, 1) || pc.getInventory().checkEnchantItem(177, 8, 1)
							|| pc.getInventory().checkEnchantItem(194, 8, 1)
							|| pc.getInventory().checkEnchantItem(13, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 8, 1)
								|| pc.getInventory().consumeEnchantItem(177, 8, 1)
								|| pc.getInventory().consumeEnchantItem(194, 8, 1)
								|| pc.getInventory().consumeEnchantItem(13, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						인첸트지급(pc, 1124, 1, 7);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("8")) {// [+8]파괴의 크로우
					if ((pc.getInventory().checkEnchantItem(81, 9, 1) || pc.getInventory().checkEnchantItem(177, 9, 1)
							|| pc.getInventory().checkEnchantItem(194, 9, 1)
							|| pc.getInventory().checkEnchantItem(13, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 9, 1)
								|| pc.getInventory().consumeEnchantItem(177, 9, 1)
								|| pc.getInventory().consumeEnchantItem(194, 9, 1)
								|| pc.getInventory().consumeEnchantItem(13, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						인첸트지급(pc, 1124, 1, 8);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("9")) {// [+7]파괴의 이도류
					if ((pc.getInventory().checkEnchantItem(81, 8, 1) || pc.getInventory().checkEnchantItem(177, 8, 1)
							|| pc.getInventory().checkEnchantItem(194, 8, 1)
							|| pc.getInventory().checkEnchantItem(13, 8, 1))
							&& pc.getInventory().checkItem(40308, 5000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 8, 1)
								|| pc.getInventory().consumeEnchantItem(177, 8, 1)
								|| pc.getInventory().consumeEnchantItem(194, 8, 1)
								|| pc.getInventory().consumeEnchantItem(13, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 5000000);
						인첸트지급(pc, 1125, 1, 7);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("10")) {// [+8]파괴의 이도류
					if ((pc.getInventory().checkEnchantItem(81, 9, 1) || pc.getInventory().checkEnchantItem(177, 9, 1)
							|| pc.getInventory().checkEnchantItem(194, 9, 1)
							|| pc.getInventory().checkEnchantItem(13, 9, 1))
							&& pc.getInventory().checkItem(40308, 10000000)) {
						if (pc.getInventory().consumeEnchantItem(81, 9, 1)
								|| pc.getInventory().consumeEnchantItem(177, 9, 1)
								|| pc.getInventory().consumeEnchantItem(194, 9, 1)
								|| pc.getInventory().consumeEnchantItem(13, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(40308, 10000000);
						인첸트지급(pc, 1125, 1, 8);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("11")) {// [+0]제로스의 지팡이
					if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 9, 1)
							&& pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
						pc.getInventory().consumeEnchantItem(119, 5, 1);
						pc.getInventory().consumeEnchantItem(121, 9, 1);
						pc.getInventory().consumeItem(700077, 1);
						pc.getInventory().consumeItem(41246, 100000);
						pc.getInventory().storeItem(202003, 1);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}

				} else if (s.equalsIgnoreCase("12")) {// [+8]제로스의 지팡이
					if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 10, 1)
							&& pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
						pc.getInventory().consumeEnchantItem(119, 5, 1);
						pc.getInventory().consumeEnchantItem(121, 10, 1);
						pc.getInventory().consumeItem(700077, 1);
						pc.getInventory().consumeItem(41246, 100000);
						인첸트지급(pc, 202003, 1, 8);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equalsIgnoreCase("13")) {// [+9]제로스의 지팡이
					if (pc.getInventory().checkEnchantItem(119, 5, 1) && pc.getInventory().checkEnchantItem(121, 11, 1)
							&& pc.getInventory().checkItem(700077) && pc.getInventory().checkItem(41246)) {
						pc.getInventory().consumeEnchantItem(119, 5, 1);
						pc.getInventory().consumeEnchantItem(121, 11, 1);
						pc.getInventory().consumeItem(700077, 1);
						pc.getInventory().consumeItem(41246, 100000);
						인첸트지급(pc, 202003, 1, 9);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}

				} else if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") // 판금
						|| s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H") // 비늘
						|| s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L") // 가죽
						|| s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) { // 로브
					if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D")) {
						if (s.equals("A")) {
							enchant = 7;
						} else if (s.equals("B")) {
							enchant = 8;
						} else if (s.equals("C")) {
							enchant = 9;
						} else if (s.equals("D")) {
							enchant = 10;
						}
						oldArmor = 20095;
						itemId = 222300;
					} else if (s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H")) {
						if (s.equals("E")) {
							enchant = 7;
						} else if (s.equals("F")) {
							enchant = 8;
						} else if (s.equals("G")) {
							enchant = 9;
						} else if (s.equals("H")) {
							enchant = 10;
						}
						oldArmor = 20094;
						itemId = 222301;
					} else if (s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L")) {
						if (s.equals("I")) {
							enchant = 7;
						} else if (s.equals("J")) {
							enchant = 8;
						} else if (s.equals("K")) {
							enchant = 9;
						} else if (s.equals("L")) {
							enchant = 10;
						}
						oldArmor = 20092;
						itemId = 222302;
					} else if (s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) {
						if (s.equals("M")) {
							enchant = 7;
						} else if (s.equals("N")) {
							enchant = 8;
						} else if (s.equals("O")) {
							enchant = 9;
						} else if (s.equals("P")) {
							enchant = 10;
						}
						oldArmor = 20093;
						itemId = 222303;
					}
					if (pc.getInventory().checkEnchantItem(20110, enchant, 1)
							&& pc.getInventory().checkItem(41246, 100000) && pc.getInventory().checkItem(oldArmor, 1)) {
						pc.getInventory().consumeEnchantItem(20110, enchant, 1);
						pc.getInventory().consumeItem(41246, 100000); // 용해제
						pc.getInventory().consumeItem(oldArmor, 1); // 고대의
						createNewItem(pc, npcName, itemId, 1, enchant - 7);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
					}
				} else if (s.equals("a")) {// []질풍의도끼
					if ((pc.getInventory().checkEnchantItem(605, 8, 1)) && pc.getInventory().checkItem(41246, 100000)) {
						if (pc.getInventory().consumeEnchantItem(605, 8, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 100000);
						인첸트지급(pc, 203015, 1, 0);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+8 광풍의도끼, 결정체(100,000)개 필요합니다."));
					}
				} else if (s.equals("b")) {// [+8]질풍의도끼
					if ((pc.getInventory().checkEnchantItem(605, 9, 1)) && pc.getInventory().checkItem(41246, 100000)) {
						if (pc.getInventory().consumeEnchantItem(605, 9, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 100000);
						인첸트지급(pc, 203015, 1, 8);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+9 광풍의도끼, 결정체(100,000)개 필요합니다."));
					}
				} else if (s.equals("c")) {// [+9]질풍의도끼
					if ((pc.getInventory().checkEnchantItem(605, 10, 1))
							&& pc.getInventory().checkItem(41246, 100000)) {
						if (pc.getInventory().consumeEnchantItem(605, 10, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 100000);
						인첸트지급(pc, 203015, 1, 9);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+10 광풍의도끼, 결정체(100,000)개 필요합니다."));
					}
				} else if (s.equals("d")) {// []마물의도끼
					if ((pc.getInventory().checkEnchantItem(151, 0, 1)) && pc.getInventory().checkItem(41246, 200000)) {
						if (pc.getInventory().consumeEnchantItem(151, 0, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 200000);
						인첸트지급(pc, 203016, 1, 0);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+0 데몬 액스, 결정체(100,000)개 필요합니다."));
					}
				} else if (s.equals("e")) {// [+1]마물의도끼
					if ((pc.getInventory().checkEnchantItem(151, 3, 1)) && pc.getInventory().checkItem(41246, 200000)) {
						if (pc.getInventory().consumeEnchantItem(151, 3, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 200000);
						인첸트지급(pc, 203016, 1, 1);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+3 데몬 액스, 결정체(100,000)개 필요합니다."));
					}
				} else if (s.equals("f")) {// [+3]마물의도끼
					if ((pc.getInventory().checkEnchantItem(151, 5, 1)) && pc.getInventory().checkItem(41246, 200000)) {
						if (pc.getInventory().consumeEnchantItem(151, 5, 1)) {
							;
						}
						pc.getInventory().consumeItem(41246, 200000);
						인첸트지급(pc, 203016, 1, 3);
						htmlid = "";
					} else {
						pc.sendPackets(new S_SystemMessage("+5 데몬 액스, 결정체(100,000)개 필요합니다."));
					}
				}
				
				
				
				
				
				
				
				
				
				
			} else if (npcid == 7) {
				if (s.equals("a")) {// 일반보상
					if (pc.getLevel() >= 52) {
						if (pc.getInventory().checkItem(30151, 1)) {
							pc.getInventory().consumeItem(30151, 1);
							pc.getInventory().storeItem(30149, 1);
							레벨52기준보상경험치(pc, 1);
							htmlid = "anold3";
						} else {
							pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
							htmlid = "anold4";
						}
					} else {
						htmlid = "anold2";
					}
				} else if (s.equals("b")) {// 특별한보상
					if (pc.getLevel() >= 52) {
						if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000004, 1)) {
							pc.getInventory().consumeItem(30151, 1);
							pc.getInventory().consumeItem(1000004, 1);
							pc.getInventory().storeItem(30149, 1);
							레벨52기준보상경험치(pc, 2);
							htmlid = "anold3";
						} else {
							pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
							htmlid = "anold4";
						}
					} else {
						htmlid = "anold2";
					}
				} else if (s.equals("c")) {// 빛나는 특별한보상
					if (pc.getLevel() >= 52) {
						if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1000007, 1)) {
							pc.getInventory().consumeItem(30151, 1);
							pc.getInventory().consumeItem(1000007, 1);
							pc.getInventory().storeItem(30149, 1);
							레벨52기준보상경험치(pc, 3);
							htmlid = "anold3";
						} else {
							pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
							htmlid = "anold4";
						}
					} else {
						htmlid = "anold2";
					}
				}
				// 나루터
			} else if (npcid == 9) {
				if (s.equals("a")) {// 일반보상
					if (pc.getLevel() >= 30) {
						if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)) {
							pc.getInventory().consumeItem(9992, 5);
							pc.getInventory().consumeItem(9993, 1);
							pc.getInventory().storeItem(9994, 1);
							레벨52기준보상경험치(pc, 1);
							htmlid = "naruto3";
						} else {
							pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
							htmlid = "naruto4";
						}
					} else {
						htmlid = "naruto2";
					}
				} else if (s.equals("b")) {// 특별한보상
					if (pc.getLevel() >= 30) {
						if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
								&& pc.getInventory().checkItem(1000004, 1)) {
							pc.getInventory().consumeItem(9992, 5);
							pc.getInventory().consumeItem(9993, 1);
							pc.getInventory().consumeItem(1000004, 1);
							pc.getInventory().storeItem(9994, 1);
							레벨52기준보상경험치(pc, 1);
							htmlid = "naruto3";
						} else {
							pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
							htmlid = "naruto4";
						}
					} else {
						htmlid = "naruto2";
					}
				} else if (s.equals("c")) {// 빛나는 특별한보상
					if (pc.getLevel() >= 30) {
						if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)
								&& pc.getInventory().checkItem(1000007, 1)) {
							pc.getInventory().consumeItem(9992, 5);
							pc.getInventory().consumeItem(9993, 1);
							pc.getInventory().consumeItem(1000007, 1);
							pc.getInventory().storeItem(9994, 1);
							레벨52기준보상경험치(pc, 1);
							htmlid = "naruto3";
						} else {
							pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
							htmlid = "naruto4";
						}
					} else {
						htmlid = "naruto2";
					}
				}
				// 알드란
			} else if (npcid == 80077) {
				if (s.equals("a")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						// L1Teleport.teleport(pc, 32674, 32871, (short) 550, 0,
						// true);
						pc.start_teleport(32674, 32871, 550, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("b")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						// L1Teleport.teleport(pc, 32778, 33009, (short) 550, 0,
						// true);
						pc.start_teleport(32778, 33009, 550, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("c")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						// L1Teleport.teleport(pc, 32471, 32766, (short) 550, 0,
						// true);
						pc.start_teleport(32471, 32766, 550, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("d")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						// L1Teleport.teleport(pc, 32511, 32998, (short) 550, 0,
						// true);
						pc.start_teleport(32511, 32998, 550, pc.getHeading(), 169, true, false);
						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				} else if (s.equals("e")) {
					if (pc.getInventory().checkItem(41207, 1)) {
						// L1Teleport.teleport(pc, 32998, 33028, (short) 558, 0,
						// true);
						pc.start_teleport(32998, 33028, 558, pc.getHeading(), 169, true, false);

						htmlid = "";
					} else {
						htmlid = "aldran9";
					}
				}

				/** 투석기 **/

			} else if (npcid == 7000082 || npcid == 7000083 || npcid == 7000084 || npcid == 7000085 || npcid == 7000086
					|| npcid == 7000087) {
				if (s.equalsIgnoreCase("0-5") // 외성문 방향으로 발사!
						|| s.equalsIgnoreCase("0-6") // 내성문 방향으로 발사!
						|| s.equalsIgnoreCase("0-7") // 수호탑 방향으로 발사!
						|| s.equalsIgnoreCase("1-16") // 외성문 방향으로 침묵포탄 발사!
						|| s.equalsIgnoreCase("1-17") // 내성문 앞쪽으로 침묵포탄 발사!
						|| s.equalsIgnoreCase("1-18") // 내성문 좌측으로 침묵포탄 발사!
						|| s.equalsIgnoreCase("1-19") // 내성문 우측으로 침묵포탄 발사!
						|| s.equalsIgnoreCase("1-20") // 수호탑 방향으로 침묵포탄 발사!
				// 수성
						|| s.equalsIgnoreCase("0-9") // 외성문 방향으로 발사!
				) {
					int locx = 0;
					int locy = 0;
					int gfxid = 0;
					int castleid = 0;
					int npcId = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					if (s.equalsIgnoreCase("0-5")) { // 외성문 방향으로 발사!
						switch (npcId) {
						case 7000086: // 5시 방향 공성 오크요새 공성측
							locx = 32795;
							locy = 32315;
							gfxid = 12197; // 우측
							castleid = 2;
							break;
						case 7000082: // 5시 방향 공성 기란성 공성측
							locx = 33632;
							locy = 32731;
							gfxid = 12197; // 우측
							castleid = 4;
							break;
						case 7000084: // 7시 방향 공성 켄트성 공성측
							locx = 33114;
							locy = 32771;
							gfxid = 12193; // 좌측
							castleid = 1;
							break;
						}
					} else if (s.equalsIgnoreCase("0-6")) { // 내성문 방향으로 발사!
						switch (npcId) {
						case 7000086: // 11시 방향 공성 오크요새 공성측
							locx = 32798;
							locy = 32268;
							gfxid = 12197; // 우측
							castleid = 2;
							break;
						case 7000082: // 11시 방향 공성 기란성 공성측
							locx = 33632;
							locy = 32664;
							gfxid = 12197; // 우측
							castleid = 4;
							break;
						case 7000084: // 2시 방향 공성 켄트성 공성측
							locx = 33171;
							locy = 32763;
							gfxid = 12197; // 좌측
							castleid = 1;
							break;
						}
					} else if (s.equalsIgnoreCase("0-7")) { // 수호탑 방향으로 발사!
						switch (npcId) {
						case 7000086: // 11시 방향 공성 오크요새 공성측
							locx = 32798;
							locy = 32285;
							gfxid = 12197; // 우측
							castleid = 2;
							break;
						case 7000082: // 11시 방향 공성 기란성 공성측
							locx = 33631;
							locy = 32678;
							gfxid = 12197; // 우측
							castleid = 4;
							break;
						case 7000084: // 2시 방향 공성 켄트성 공성측
							locx = 33168;
							locy = 32779;
							gfxid = 12197; // 좌측
							castleid = 1;
							break;
						}
					} else if (s.equalsIgnoreCase("0-9")) { // 외성문 방향으로 발사!
						int pcCastleId = 0;
						if (pc.getClanid() != 0) {
							L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
							if (clan != null) {
								pcCastleId = clan.getCastleId();
							}
						}
						switch (npcId) {
						case 7000087: // 11시 방향 공성 오크요새 수성측
							if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
								if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
									pc.sendPackets(new S_ServerMessage(3682));
									// 투석기 사용: 실패(성을 수호하는 성혈 군주만 사용 가능)
									return htmlid;
								}
							}
							locx = 32794;
							locy = 32320;
							gfxid = 12193; // 우측
							castleid = 2;
							break;
						case 7000083: // 11시 방향 공성 기란성 수성측
							if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
								if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
									pc.sendPackets(new S_ServerMessage(3682));
									// 투석기 사용: 실패(성을 수호하는 성혈 군주만 사용 가능)
									return htmlid;
								}
							}
							locx = 33631;
							locy = 32738;
							gfxid = 12193; // 우측
							castleid = 4;
							break;
						case 7000085: // 2시 방향 공성 켄트성 수성측
							if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
								if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
									pc.sendPackets(new S_ServerMessage(3682));
									// 투석기 사용: 실패(성을 수호하는 성혈 군주만 사용 가능)
									return htmlid;
								}
							}
							locx = 33107;
							locy = 32770;
							gfxid = 12197; // 우측
							castleid = 1;
							break;
						}

						/*
						 * <a action="1-16">외성문 방향으로 침묵포탄 발사!</a><br> <a
						 * action="1-17">내성문 앞쪽으로 침묵포탄 발사!</a><br> <a
						 * action="1-18">내성문 좌측으로 침묵포탄 발사!</a><br> <a
						 * action="1-19">내성문 우측으로 침묵포탄 발사!</a><br> <a
						 * action="1-20">수호탑 방향으로 침묵포탄 발사!</a><br><br> } else if
						 * (s.equalsIgnoreCase("0-9")) { //외성문 방향으로 침묵포탄 발사!
						 */

					} else {
						pc.sendPackets(new S_SystemMessage("침묵포탄은 사용 불가능 합니다."));
						return htmlid;
					}

					boolean isNowWar = false;
					isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleid);
					if (!isNowWar) {
						pc.sendPackets(new S_ServerMessage(3683));
						// 투석기 사용: 실패(공성 시간에만 사용 가능)
						return htmlid;
					}
					//
					boolean inWar = MJWar.isNowWar(pc.getClan());
					if (!(pc.isCrown() && inWar && isNowWar)) {
						pc.sendPackets(new S_ServerMessage(3681));
						// 투석기 사용: 실패(전쟁을 선포한 군주만 사용 가능)
						return htmlid;
					}
					if (pc.getlastShellUseTime() + 10000L > System.currentTimeMillis()) {
						pc.sendPackets(new S_ServerMessage(3680));
						// 투석기 사용: 실패(재장전 시간 필요)
						return htmlid;
					}

					if (obj != null) {
						if (obj instanceof L1CataInstance) {
							L1CataInstance npc = (L1CataInstance) obj;
							if (pc.getInventory().consumeItem(30124, 1)) {
								Broadcaster.broadcastPacket(npc,
										new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Attack));
								S_EffectLocation packet = new S_EffectLocation(locx, locy, gfxid);
								pc.sendPackets(packet);
								Broadcaster.wideBroadcastPacket(pc, packet, 100);
								getShellDmg(locx, locy);
								// 침묵포탄(locx, locy); // 침묵포탄 테스트
								pc.updatelastShellUseTime();
							} else {
								pc.sendPackets(new S_ServerMessage(337, "$16785"));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlid;
	}

	private static boolean 봉인템(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr,
			boolean identi) {
		// 봉인템(pc, 5000045, 1, 5, 128);
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(attr);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(Bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				pc.sendPackets(new S_ServerMessage(82));
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
			return true;
		} else {
			return false;
		}
	}

	private boolean 인첸트지급(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0를
			// 손에
			// 넣었습니다.
			return true;
		} else {
			return false;
		}
	}

	private void 레벨52기준보상경험치(L1PcInstance pc, int type) {
		int needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		int exp = 0;
		if (type == 1) {
			exp = (int) (needExp * 0.02D * exppenalty);
		} else if (type == 2) {
			exp = (int) (needExp * 0.05D * exppenalty);
		} else if (type == 3) {
			exp = (int) (needExp * 0.20D * exppenalty);
		} else {
			pc.sendPackets(new S_SystemMessage("잘못된 요구입니다."));
		}
		pc.addExp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	private void 에킨스경험치2(L1PcInstance pc) {
		int needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		int exp = 0;
		if (pc.getLevel() <= 60) {
			exp = (int) (needExp * 0.04D);
		} else if (pc.getLevel() <= 65) {
			exp = (int) (needExp * 0.03D);
		} else if (pc.getLevel() <= 70) {
			exp = (int) (needExp * 0.02D);
		} else if (pc.getLevel() <= 75) {
			exp = (int) (needExp * 0.01D);
		} else {
			exp = (int) (needExp * 16D * exppenalty);
		}
		pc.addExp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	private void 에킨스경험치6(L1PcInstance pc) {
		int needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		int exp = 0;
		if (pc.getLevel() <= 60) {
			exp = (int) (needExp * 0.12D);
		} else if (pc.getLevel() <= 65) {
			exp = (int) (needExp * 0.09D);
		} else if (pc.getLevel() <= 70) {
			exp = (int) (needExp * 0.06D);
		} else if (pc.getLevel() <= 75) {
			exp = (int) (needExp * 0.03D);
		} else {
			exp = (int) (needExp * 48D * exppenalty);
		}
		pc.addExp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	private boolean createNewItem(L1PcInstance pc, String npcName, int item_id, int count, int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(enchant);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			pc.sendPackets(new S_ServerMessage(143, npcName, item.getLogName()));
			return true;
		} else {
			return false;
		}
	}

	private boolean isExistDefenseClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		return isExistDefenseClan;
	}

	private void getShellDmg(int locx, int locy) {
		L1PcInstance targetPc = null;
		L1NpcInstance targetNpc = null;
		L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(81154, 1 * 1000, locx, locy, (short) 4);
		for (L1Object object : L1World.getInstance().getVisibleObjects(effect, 3)) {
			if (object == null) {
				continue;
			}
			if (!(object instanceof L1Character)) {
				continue;
			}
			if (object.getId() == effect.getId()) {
				continue;
			}

			if (object instanceof L1PcInstance) {
				targetPc = (L1PcInstance) object;
				targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
				Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
				targetPc.receiveDamage(targetPc, 100, 3);
			} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance) {
				targetNpc = (L1NpcInstance) object;
				Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage));
				targetNpc.receiveDamage(targetNpc, (int) 100);
			}
		}
	}
}
