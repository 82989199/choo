package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.PlaySupportInfoTable;
import l1j.server.server.datatables.PlaySupportInfoTable.SupportInfo;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Ping;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SocialAction;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TamWindow;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.SQLUtil;

public class C_ActionUi extends ClientBasePacket {

	Random _Random = new Random(System.nanoTime());

	private static final String C_ACTION_UI = "[C] C_ActionUi";
	private static final int CS_PING_ACK = 0x03E9;
	private static final int ACCOUNT_TAM = 0x01cc;// 탐창
	private static final int ACCOUNT_TAM_CANCEL = 0x01e0;// 탐 취소
	private static final int ACCOUNT_TAM_UPDATE = 0x013d;// 탐
	private static final int 액션 = 0x013F;
	private static final int 수상한하늘정원 = 0x84;
	/*
	 * private static final int 혈맹가입 = 0x0142; private static final int
	 * 혈맹가입신청받기설정 = 0x0146; private static final int 혈맹모집세팅 = 0x014C;
	 */
	private static final int 가입대기 = 0x44;
	private static final int 공성관련 = 0x45;// /선포
	private static final int 표식설정 = 0x0152;
	private static final int EVENT_TELEPORT = 143;
	private static final int NEWSHOP = 0x0331;
	private static final int BLESSED_TELEPORT = 0x033D;

	/**
	 * 플레이 서포터 타입 0627
	 */
	private static final int START_PLAY_SUPPORT_REQ = 2101; // 0x0835
	private static final int FINISH_PLAY_SUPPORT_REQ = 2103; // 0x0837
	
	private static final int ENVIRONMENT_SETTING = 1002;

	public C_ActionUi(byte abyte0[], GameClient client) {
		super(abyte0);
		try {
			L1PcInstance pc = client.getActiveChar();
			int type = readH();
			if (pc == null) {
				return;
			} else if (pc.isGhost())
				return;

			switch (type) {
			case START_PLAY_SUPPORT_REQ: {
				int start = S_ACTION_UI.SUPPORT_VALID;

				SupportInfo si = PlaySupportInfoTable.getInstance().getPlaySupportInfo(pc.getMapId());
				if (si != null) {
					if (si._min_level != 0) {
						if (pc.getLevel() <= si._min_level)
							start = S_ACTION_UI.SUPPORT_INVALID_LEVEL;
					}
					if (si._max_level != 0) {
						if (pc.getLevel() >= si._max_level)
							start = S_ACTION_UI.SUPPORT_INVALID_LEVEL;
					}

					if (start == S_ACTION_UI.SUPPORT_VALID) {
						if (si._item_check_type == 1) {
							if (pc.getInventory().findEquippedItemId(si._item_id) == null) {
								start = S_ACTION_UI.SUPPORT_UNKNOWN_ERROR;
								L1Item item = ItemTable.getInstance().getTemplate(si._item_id);
								pc.sendPackets(new S_SystemMessage("구동에 필요한 " + item.getName() + "을 착용하고 있지 않습니다."));
							}
						} else if (si._item_check_type == 2) {
							if (!pc.getInventory().consumeItem(si._item_id, si._item_count)) {
								start = S_ACTION_UI.SUPPORT_UNKNOWN_ERROR;
								L1Item item = ItemTable.getInstance().getTemplate(si._item_id);
								pc.sendPackets(new S_SystemMessage(
										"구동에 필요한 " + item.getName() + "(" + si._item_count + ") 이(가) 부족합니다."));
							}
						} else if (si._item_check_type == 3) {
							if (!pc.getInventory().checkItem(si._item_id, si._item_count)) {
								start = S_ACTION_UI.SUPPORT_UNKNOWN_ERROR;
								L1Item item = ItemTable.getInstance().getTemplate(si._item_id);
								pc.sendPackets(new S_SystemMessage(
										"구동에 필요한 " + item.getName() + "(" + si._item_count + ") 이(가) 부족합니다."));
							}
						}
					}
				} else {
					start = S_ACTION_UI.SUPPORT_INVALID_MAP;
				}
				
				if(start == S_ACTION_UI.SUPPORT_VALID) {
					pc.setPlaySupport(true);
				}

				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.START_PLAY_SUPPORT_ACK, start));
			}
				break;
			case FINISH_PLAY_SUPPORT_REQ: {
				// int time = (int) System.currentTimeMillis(); // 종료시 전송될 시간
				pc.setPlaySupport(false);
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.FINISH_PLAY_SUPPORT_ACK, 0));
			}
				break;
			case ENVIRONMENT_SETTING:
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Calendar Rtime = getRealTime();
				String nowToday = sdf.format(Rtime.getTime());
				
				if (pc.isOneMinuteStart()) {
					if (Rtime.after(pc.getEnviroMentLocalTime())) {
						//System.out.println(pc.getName() + " 분당패킷 정상진입");
						// 아인하사드
						if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
							executeGrangKinCheck(pc);
						}
						
						pcbuffPremiumTime(pc);
						
						pc.setEnviroMentCount(0);
						Rtime.add(Calendar.SECOND, 55);
						pc.setEnviroMentLocalTime(Rtime);
					} else {
						pc.addEnviroMentCount(1);
						if (pc.getEnviroMentCount() > 4) {
					//		System.out.println("[ 분당패킷오류 : " + pc.getName() + " 확인요망 ] (" + pc.getEnviroMentLocalTime() + " / " + LocalTime.now() + ")");
						}
					}
				}

				if (!pc.isOneMinuteStart()) {
					pc.setOneMinuteStart(true);
					pc.setEnviroMentLocalTime(Rtime);
				}
				
				//String strToday = sdf.format(pc.getEnviroMentLocalTime().getTime());
				//System.out.println(pc.getName() + " / 현재시간 : " + nowToday + " || 계산후시간 : "+ strToday);

				break;
			case CS_PING_ACK:
				if (pc.isGm())
					S_Ping.reqForGM(pc);
				break;
			case NEWSHOP: {
				try{
					if (pc == null || pc.isGhost()) {
						return;
					}
					if (pc.isInvisble()) {
						pc.sendPackets(new S_ServerMessage(755));
						return;
					}
					if (pc.getMapId() != 800) {
						pc.sendPackets(new S_SystemMessage("개인상점은 시장에서만  열수 있습니다."));
						return;
					}

					if (pc.getMapId() != 800) {
						if (pc.isFishing()) {
							try {
								pc.setFishing(false);
								pc.setFishingTime(0);
								pc.setFishingReady(false);
								pc.sendPackets(new S_CharVisualUpdate(pc));
								Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
								FishingTimeController.getInstance().removeMember(pc);
								pc.sendPackets(new S_ServerMessage(2120));
								return;
							} catch (Exception e) {
							}
						} else {
							pc.sendPackets(new S_ServerMessage(3405));
							return;
						}
					}

					if (pc.getInventory().checkEquipped(22232) || pc.getInventory().checkEquipped(22234)
							|| pc.getInventory().checkEquipped(22233) || pc.getInventory().checkEquipped(22235)
							|| pc.getInventory().checkEquipped(22236) || pc.getInventory().checkEquipped(22237)
							|| pc.getInventory().checkEquipped(22238) || pc.getInventory().checkEquipped(22239)
							|| pc.getInventory().checkEquipped(22240) || pc.getInventory().checkEquipped(22241)
							|| pc.getInventory().checkEquipped(22242) || pc.getInventory().checkEquipped(22243)
							|| pc.getInventory().checkEquipped(22244) || pc.getInventory().checkEquipped(22245)
							|| pc.getInventory().checkEquipped(22246) || pc.getInventory().checkEquipped(22247)
							|| pc.getInventory().checkEquipped(22248) || pc.getInventory().checkEquipped(22249)) { // 룬
						pc.sendPackets(new S_ChatPacket(pc, "룬을 착용하셨다면 해제하시기 바랍니다."));
						return;
					}
					
					L1ItemInstance checkItem;
					boolean tradable = true;

					int length = readBit();

					readC(); // 0x00
					readC(); // 0x08

					int shoptype = readC();
					if (shoptype == 0) {
						//상점 오류 수정함
						if (pc.getCurrentSpriteId() != pc.getClassId())
							pc.sendPackets(new S_SystemMessage("변신이 해제됩니다."));
						if(pc.hasSkillEffect(L1SkillId.SHAPE_CHANGE))
							pc.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
						else if(pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER))
							pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
						else if(pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2))
							pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
						else
							L1PolyMorph.undoPoly(pc);
						
						for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
							if (target.getId() != pc.getId()
									&& target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase())
									&& target.isPrivateShop()) {
								pc.sendPackets("\\f3이미 당신의 보조 캐릭터가 무인상점 상태입니다.");
								pc.sendPackets("\\f3상점을 종료하시길 바랍니다. /상점");
								pc.setPrivateShop(false);
								pc.상점변신 = 0;
								pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
								pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
								pc.broadcastPacket(S_WorldPutObject.get(pc));
								return;
							}
						}

						int ObjectId = 0;
						int Price = 0;
						int Count = 0;
						Object[] petlist = null;
						for (int i = 0; i < length; i++) {
							int code = readC();
							if (code == 0x12 || code == 0x1a) {
								readP(1); // 길이.
								for (int i2 = 0; i2 < 3; i2++) {
									int code2 = readC();
									if (code2 == 0x08)
										ObjectId = readBit();
									else if (code2 == 0x10)
										Price = readBit();
									else if (code2 == 0x18)
										Count = readBit();
								}
								// 거래 가능한 아이템이나 체크
								checkItem = pc.getInventory().getItem(ObjectId);
								if (ObjectId != checkItem.getId()) {
									pc.sendPackets(new S_Disconnect());
									return;
								}
								if (!checkItem.isStackable() && Count != 1) {
									pc.sendPackets(new S_Disconnect());
									return;
								}

								if (/* checkItem.getCount() < Count || */ checkItem.getCount() <= 0 || Count <= 0) {
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									pc.disposeShopInfo();
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									return;
								}
								if (checkItem.getBless() >= 128) {
									pc.sendPackets(new S_ServerMessage(210, checkItem.getItem().getName())); // \f1%0은
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									pc.disposeShopInfo();
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									return;
								}
								if (!checkItem.getItem().isTradable()) {
									tradable = false;
									pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
									break;
								}
								
								if(!MJCompanionInstanceCache.is_companion_oblivion(checkItem.getId())){				
									tradable = false;
									pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
								}

								petlist = pc.getPetList().values().toArray();
								for (Object petObject : petlist) {
									if (petObject instanceof L1PetInstance) {
										L1PetInstance pet = (L1PetInstance) petObject;
										if (checkItem.getId() == pet.getItemObjId()) {
											tradable = false;
											pc.sendPackets(
													new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
											break;
										}
									}
								}

								Object[] dollList = pc.getDollList().values().toArray();
								for (Object dollObject : dollList) {
									if (dollObject instanceof L1DollInstance) {
										L1DollInstance doll = (L1DollInstance) dollObject;
										if (checkItem.getId() == doll.getItemObjId()) {
											tradable = false;
											pc.sendPackets(
													new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
											break;
										}
									}
								}
								if (code == 0x12) {
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									pc.addSellings(MJDShopItem.create(checkItem, Count, Price, false));
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
								} else if (code == 0x1a) {
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
									pc.addPurchasings(MJDShopItem.create(checkItem, Count, Price, true));
									/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
								}
							} else {
								/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
								// pc.disposeShopInfo();
								/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
								break;
							}
						}
						if (!tradable) { // 거래 불가능한 아이템이 포함되어 있는 경우, 개인 상점 종료
							pc.setPrivateShop(false);
							pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
							pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
							/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
							pc.disposeShopInfo();
							/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
							return;
						}
						int l1 = readC();
						byte[] chat = readByteL(l1);

						readC();
						int l2 = readC();
						String polynum = readS(l2);

						// String test =null;
						int poly = 0;

						pc.getNetConnection().getAccount().updateShopOpenCount();
						pc.sendPackets(new S_PacketBox(S_PacketBox.상점개설횟수, pc.getNetConnection().getAccount().Shop_open_count), true);

						pc.setShopChat(chat);
						pc.setPrivateShop(true);
						pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));
						pc.broadcastPacket(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));
						pc.sendPackets("리스시 무인상점이 활성화 됩니다.");

						if (polynum.equalsIgnoreCase("tradezone1"))
							poly = 11479;
						else if (polynum.equalsIgnoreCase("tradezone2"))
							poly = 11483;
						else if (polynum.equalsIgnoreCase("tradezone3"))
							poly = 11480;
						else if (polynum.equalsIgnoreCase("tradezone4"))
							poly = 11485;
						else if (polynum.equalsIgnoreCase("tradezone5"))
							poly = 11482;
						else if (polynum.equalsIgnoreCase("tradezone6"))
							poly = 11486;
						else if (polynum.equalsIgnoreCase("tradezone7"))
							poly = 11481;
						else if (polynum.equalsIgnoreCase("tradezone8")) {
							poly = 11484;
						}
						pc.상점변신 = poly;
						pc.setCurrentSprite(poly);
						pc.sendPackets(new S_ChangeShape(pc.getId(), poly, 70));
						Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), poly, 70));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
						pc.broadcastPacket(S_WorldPutObject.get(pc));
						// Broadcaster.broadcastPacket(pc, new
						// S_OtherCharPacks(pc));
						pc.curePoison();
						/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
						GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, false));
						/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
					} else if (shoptype == 1) {
						if (pc.isPrivateReady() && pc.isPrivateShop())
							break;
						
						pc.setPrivateShop(false);
						pc.상점변신 = 0;
						pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						L1PolyMorph.undoPolyPrivateShop(pc);
						/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
						GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, true));
						/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
			}
			case EVENT_TELEPORT: {// 이벤트 알림
				readH();
				readC();
				int stat = readC();
				int x = 0, y = 0, map = 0;
				/** 이벤트알람 유저가 TEL 클릭했을시 **/
				if (pc.getLevel() <= Config.YN_pclevel) {
					pc.sendPackets(new S_SystemMessage("레벨 " + Config.YN_pclevel + " 부터 이용가능 합니다."));
					return;
				}
				if (pc.getMapId() == 38 || pc.getMapId() == 1708 || pc.getMapId() == 1709) {
					pc.sendPackets("현재맵에서는 이용하실 수 없습니다.");
					return;
				}
				if (pc.getInventory().checkItem(40308, 10000)) {
					pc.getInventory().consumeItem(40308, 10000);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나 10,000원이 부족 합니다."));
					return;
				}
				if (stat == 1) { // -- 에르자베
					x = 32895;
					y = 33244;
					map = 4;
				} else if (stat == 2) { // -- 샌드웜
					x = 32723;
					y = 33149;
					map = 4;
				} else if (stat == 3) { // -- 붉은 기사단의 진격
					x = 33629;
					y = 32776;
					map = 4;
				} else if (stat == 4) { // -- 붉은 기사단의 진격
					x = 33629;
					y = 32776;
					map = 4;					
				} else if (stat == 5) { // -- 아르피어
					x = 32460;
					y = 33060;
					map = 9;
				} else if (stat == 6) { // -- 아크모
					x = 32675;
					y = 32859;
					map = 254;
				} else if (stat == 7) { // -- 리칸트
					x = 32676;
					y = 32852;
					map = 47;
				} else if (stat == 8) { // -- 커츠
					x = 32875;
					y = 32654;
					map = 4;
				} else if (stat == 9) { // -- 데스나이트
					x = 32775;
					y = 32785;
					map = 813;	
				} else if (stat == 10) { // -- 거인 모닝스타
					x = 34240;
					y = 33358;
					map = 4;	
				} else if (stat == 11) { // -- 풍룡의수호신
					x = 34255;
					y = 32792;
					map = 1540;	
				} else if (stat == 12) { // -- 풍룡의수호신
					x = 34255;
					y = 32792;
					map = 1540;	
				} else if (stat == 13) { // -- 풍룡의수호신
					x = 34255;
					y = 32792;
					map = 1540;	
				} else if (stat == 14) { // -- 풍룡의수호신
					x = 34255;
					y = 32792;
					map = 1540;	
				} else if (stat == 15) { // -- 제니스 퀸
					x = 32732;
					y = 32798;
					map = 101;	
				} else if (stat == 16) { // -- 시어
					x = 32727;
					y = 32803;
					map = 102;	
				} else if (stat == 17) { // -- 뱀파이어
					x = 32726;
					y = 32803;
					map = 103;	
				} else if (stat == 18 ) { // -- 좀비로드
					x = 32620;
					y = 32859;
					map = 104;	
				} else if (stat == 19) { // -- 쿠거
					x = 32601;
					y = 32866;
					map = 105;	
				} else if (stat == 20) { // -- 머미로드
					x = 32611;
					y = 32863;
					map = 106;	
				} else if (stat == 21) { // -- 아이리스
					x = 32618;
					y = 32866;
					map = 107;	
				} else if (stat == 22) { // -- 나이트발드
					x = 32601;
					y = 32867;
					map = 108;	
				} else if (stat == 23) { // -- 리치
					x = 32613;
					y = 32866;
					map = 109;
				} else if (stat == 24) { // -- 우구누스
					x = 32730;
					y = 32803;
					map = 110;
				} else if (stat == 25) { // -- 제니스 퀸
					x = 32732;
					y = 32798;
					map = 101;	
				} else if (stat == 26) { // -- 시어
					x = 32727;
					y = 32803;
					map = 102;	
				} else if (stat == 27) { // -- 뱀파이어
					x = 32726;
					y = 32803;
					map = 103;	
				} else if (stat == 28 ) { // -- 좀비로드
					x = 32620;
					y = 32859;
					map = 104;	
				} else if (stat == 29) { // -- 쿠거
					x = 32601;
					y = 32866;
					map = 105;	
				} else if (stat == 30) { // -- 머미로드
					x = 32611;
					y = 32863;
					map = 106;	
				} else if (stat == 31) { // -- 아이리스
					x = 32618;
					y = 32866;
					map = 107;	
				} else if (stat == 32) { // -- 나이트발드
					x = 32601;
					y = 32867;
					map = 108;	
				} else if (stat == 33) { // -- 리치
					x = 32613;
					y = 32866;
					map = 109;
				} else if (stat == 34) { // -- 우구누스
					x = 32730;
					y = 32803;
					map = 110;
				} else if (stat == 35) { // -- 사신 그림 리퍼
					x = 32601;
					y = 32866;
					map = 108;	
		      	}				
				if (x == 0 && y == 0 && map == 0) {
					return;
				}
				pc.start_teleport(x, y, map, pc.getHeading(), 169, true, true);
			}
				break;
			case 가입대기: {
				pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.CLAN_JOIN_WAIT, true));
			}
				break;
			case BLESSED_TELEPORT: {
				readC();
				readH();
				int chatlen = readC();
				BinaryOutputStream os = new BinaryOutputStream();
				for (int i = 0; i < chatlen; i++) {
					os.writeC(readC());
				}
				String code = new String(os.getBytes());
				for (L1NpcInstance tel_map_npc : L1World.getInstance().getAllNpc()) {
					if (tel_map_npc.getNpcId() == 9000) {
						L1Object npc = L1World.getInstance().findObject(tel_map_npc.getId());
						L1NpcAction action = NpcActionTable.getInstance().get(code, pc, npc);
						if (pc.getInventory().checkItem(140100)) {
							pc.getInventory().consumeItem(140100, 1);
							action.execute(code, pc, npc, null);
						}
					}
				}
			}
				break;
			case 수상한하늘정원:
				if (!pc.PC방_버프) {
					pc.sendPackets(new S_SystemMessage("PC방 이용권을 사용 중에만 가능한 행동입니다."));
					return;
				}
				if(pc.getMapId() >= 12852 && pc.getMapId() <= 12862){
					pc.sendPackets(new S_SystemMessage("지배의 탑에서는 사용할 수 없습니다."));
					return;
				}
				if(pc.getMapId() >= 3 && pc.getMapId() <= 3){
					pc.sendPackets(new S_SystemMessage("여기서는 사용할 수 없습니다."));
					return;
				}
				if (!pc.PC방_버프 && !pc.getInventory().checkItem(41921)) {
					pc.sendPackets(new S_SystemMessage("PC방 이용권을 사용 중에만 가능한 행동입니다."), true);
					return;
				}
				if (pc.getMapId() == 99 || pc.getMapId() == 6202) {
					pc.sendPackets(new S_SystemMessage("주위의 마력에의해 순간이동을 사용할 수 없습니다."), true);
					return;
				}

				if (!pc.getMap().isEscapable()) {
					pc.sendPackets(new S_SystemMessage("주위의 마력에의해 순간이동을 사용할 수 없습니다."), true);
					return;
				}
				
				/*if (!pc.PC방_버프 && pc.getInventory().checkItem(41921)) {
					pc.getInventory().consumeItem(41921, 1);
				}*/
				 int count = 1;
				  if(!pc.PC방_버프) {
					  count = 2;
				  }
				  
				if(pc.getInventory().consumeItem(41921, count) == false){
					pc.sendPackets(new S_SystemMessage("깃털이 부족합니다."), true);
					return;
				}

				int ran = _Random.nextInt(4);

				if (ran == 0) {
					pc.start_teleport(32779, 32825, 622, pc.getHeading(), 169, true, false);
				} else if (ran == 1) {
					pc.start_teleport(32779, 32825, 622, pc.getHeading(), 169, true, false);
				} else if (ran == 2) {
					pc.start_teleport(32779, 32825, 622, pc.getHeading(), 169, true, false);
				} else {
					pc.start_teleport(32779, 32825, 622, pc.getHeading(), 169, true, false);
				}
				break;
			case 0x0146: // 혈맹 가입신청 받기 설정
				if (pc.getClanid() == 0 || (!pc.isCrown() && pc.getClanRank() != L1Clan.수호))
					return;

				/** 2016.11.25 MJ 앱센터 혈맹 **/
				readC();
				readH();
				int setting = readC();
				readC();
				int setting2 = readC();
				if (setting2 == 2) {
					readC();
					int size = readC();
					StringBuilder sb = new StringBuilder(size * 3);
					for (int i = 0; i < size; i++)
						sb.append(String.format("%02X", readC()));
					pc.getClan().setJoinPassword(sb.toString());
					ClanTable.getInstance().updateClanPassword(pc.getClan());
				}

				pc.getClan().setJoinSetting(setting);
				pc.getClan().setJoinType(setting2);
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_SETTING, setting, setting2), true);
				ClanTable.getInstance().updateClan(pc.getClan());
				pc.sendPackets(new S_ServerMessage(3980), true);
				/** 2016.11.25 MJ 앱센터 혈맹 **/
				break;
			case 0x014C: // 혈맹 모집 셋팅
				if (pc.getClanid() == 0)
					return;
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_SETTING, pc.getClan().getJoinSetting(),
						pc.getClan().getJoinType()), true);
				break;
			case 322: // 혈맹가입
			{
				/** 2016.11.25 MJ 앱센터 혈맹 **/
				@SuppressWarnings("unused")
				int joinType = readC();
				readH();
				int length = readC();
				if (pc.isCrown()) {
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 4), true);
					break;
				}

				boolean is_move_clan = false;
				// 이미 혈맹에 가입한 상태 입니다.
				if (pc.getClanid() != 0) {
					L1Clan clan = pc.getClan();
					if(clan.getLeaderId() != pc.getId()){
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 9), true);
						break;
					}else{
						if (clan.getCastleId() > 0) {
							pc.sendPackets(new S_ServerMessage(	ServerMessage.HAVING_NEST_OF_CLAN));
							break;
						}

						if(clan.getCurrentWar() != null){
							pc.sendPackets(new S_ServerMessage(	ServerMessage.CANNOT_BREAK_CLAN));
							break;
						}

						if (clan.getAlliance() > 0) {
							pc.sendPackets(new S_ServerMessage(ServerMessage.CANNOT_BREAK_CLAN_HAVING_FRIENDS));
							break;
						}
						is_move_clan = true;
					}
				}

				// 군주를 만나 가입해 주세요.
				try {
					String clanname = new String(readByteL(length), 0, length, "MS949");
					L1Clan clan = L1World.getInstance().findClan(clanname);
					//
					if (clan == null) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 13), true);
						break;
					}
					L1PcInstance crown = clan.getonline간부();
					switch (clan.getJoinType()) {
					case 1:
						if (crown == null) {
							pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 11), true);
							return;
						}

						crown.setTempID(pc.getId()); // 상대의 오브젝트 ID를 보존해 둔다
						S_Message_YN myn = new S_Message_YN(97, pc.getName());
						crown.sendPackets(myn, true);
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 1), true);
						return;
					case 2:
						readD();
						readC();
						int size = readC();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < size; i++)
							sb.append(String.format("%02X", readC()));
						if (clan.getJoinPassword() == null || !clan.getJoinPassword().equalsIgnoreCase(sb.toString())) {
							pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 3), true);
							return;
						}
					case 0:
						if (L1ClanJoin.getInstance().ClanJoin(clan, pc))
							pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 0), true);
						else
							pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 1), true);
						break;
					default:
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 11), true);
						break;
					}
				} catch (Exception e) {
				}
				break;
			}
				/** 2016.11.25 MJ 앱센터 혈맹 **/
			case 표식설정: // 파티 표식설정
				int size = readH();
				byte[] flag = new byte[size];
				for (int i = 0; i < size; ++i)
					flag[i] = (byte) readC();
				//
				L1Party party = pc.getParty();
				if (party == null)
					return;
				//
				for (L1PcInstance member : party.getMembers())
					member.sendPackets(new S_ACTION_UI(flag));
				break;
			case ACCOUNT_TAM_UPDATE:// 탐창띄우기
				// pc.sendPackets(new S_TamWindow(pc.getAccountName()));
				break;
			case ACCOUNT_TAM:// 탐창띄우기
				pc.sendPackets(new S_TamWindow(pc.getAccountName()));
				break;
			case ACCOUNT_TAM_CANCEL:// 탐
				int len = readC() - 1;
				readH();
				StringBuffer sb = new StringBuffer(len * 2);
				for (int i = 0; i < len; i++) {
					sb.append(String.valueOf((byte) readC()));
				}
				/*
				 * byte[] BYTE = readByte(len); byte[] temp = new
				 * byte[BYTE.length - 1]; for (int i = 0; i < temp.length; i++)
				 * { temp[i] = BYTE[i]; }
				 * 
				 * for (byte zzz : temp) { sb.append(String.valueOf(zzz)); }
				 */

				int day = Nexttam(sb.toString());
				int charobjid = TamCharid(sb.toString());
				if (charobjid != pc.getId()) {
					pc.sendPackets(new S_SystemMessage("해당 케릭터만 취소를 할 수 있습니다."));
					return;
				}
				int itemid = 0;
				if (day != 0) {
					if (day == 3) {// 기간 3일
						itemid = 600226;
					} else if (day == 7) {// 기간7일
						itemid = 3000235;
					} else if (day == 30) {// 기간30일
						itemid = 600227;
					}
					L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));
						tamcancle(sb.toString());
						pc.sendPackets(new S_TamWindow(pc.getAccountName()));
					}
				}
				break;
			case 액션: // 소셜액션
				readH();
				readC();
				int atype = readC();
				readC();
				int code = readC();
				ServerBasePacket sbp = S_SocialAction.get(pc.getId(), atype, code);
				pc.sendPackets(sbp, false);
				Broadcaster.broadcastPacket(pc, sbp, false);
				sbp.clear();
				break;
			case 공성관련: // 45
				try {
					readH();
					readC();
					int castleId = readC();
					MJCastleWarBusiness.getInstance().proclaim(pc, castleId);
				} catch (Exception e) {
				} finally {
					clear();
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int Nexttam(String encobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int day = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT day FROM `tam` WHERE encobjid = ? order by id asc limit 1"); // 케릭터
			// 테이블에서
			// 군주만
			// 골라와서
			pstm.setString(1, encobj);
			rs = pstm.executeQuery();
			while (rs.next()) {
				day = rs.getInt("Day");
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return day;
	}

	public int TamCharid(String encobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int objid = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT objid FROM `tam` WHERE encobjid = ? order by id asc limit 1"); // 케릭터
																												// 테이블에서
																												// 군주만
																												// 골라와서
			pstm.setString(1, encobj);
			rs = pstm.executeQuery();
			while (rs.next()) {
				objid = rs.getInt("objid");
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return objid;
	}

	public void tamcancle(String objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from tam where encobjid = ? order by id asc limit 1");
			pstm.setString(1, objectId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void executeGrangKinCheck(L1PcInstance pc) {
		if (pc.getZoneType() == 1) {
			if (pc.getAccount().getBlessOfAin() < 10000 && pc.getAccount().getGrangKinAngerStat() != 0) {
				pc.addGrangKinAngerSafeTime(1);
				System.out.println("그랑카인 세이프티 시간 1분 추가 / " + pc.getGrangKinAngerSafeTime());
			}
		} else {
			if(pc.getAccount().getBlessOfAin() <= 10000){
				System.out.println("그랑카인 시간 1분 추가 / " + LocalTime.now().getMinute());
				pc.getAccount().addGrangKinAngerTime(1, pc);
			}
		}
	}
	
	public Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}
	
	private void pcbuffPremiumTime(L1PcInstance pc) {
		if (pc.PC방_버프삭제중) {
			pc.PC방_버프삭제중 = false;
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.PCBANG_SET, false));
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 293, false));
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"[PC방 상품 종료 안내] PC방 이용 시간이 종료되어 효과가 사라집니다. "));
		}
		
		long sysTime = System.currentTimeMillis();
		if (pc.PC방_버프) {
			if (pc.getAccount().getBuff_PC방() != null) {
				if (sysTime <= pc.getAccount().getBuff_PC방().getTime()) {
					long 피씨타임 = pc.getAccount().getBuff_PC방().getTime() - sysTime;
					TimeZone seoul = TimeZone.getTimeZone(Config.TIME_ZONE);
					Calendar calendar = Calendar.getInstance(seoul);
					calendar.setTimeInMillis(피씨타임);
					int d = calendar.get(Calendar.DATE) - 1;
					int h = calendar.get(Calendar.HOUR_OF_DAY);
					int m = calendar.get(Calendar.MINUTE);
					int sc = calendar.get(Calendar.SECOND);
					if (d == 0) {
						if (h < 0 && m == 1) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"[PC방 이용 시간] " + m + "분 " + sc + "초 남았습니다."));
							pc.sendPackets(new S_SystemMessage("[PC방 상품 종료 안내] 이용 시간 소진시 1분 뒤에 아이콘이 사라집니다."));
						} 
					}
				} else {
					pc.setPC방_버프(false);
					pc.PC방_버프삭제중 = true;
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_ACTION_UI;
	}

	class PrivateShopReadier implements Runnable {
		private L1PcInstance _pc;

		PrivateShopReadier(L1PcInstance pc) {
			_pc = pc;
		}

		@Override
		public void run() {
			if (_pc != null)
				_pc.setPrivateReady(false);
		}
	}
	
	
}
