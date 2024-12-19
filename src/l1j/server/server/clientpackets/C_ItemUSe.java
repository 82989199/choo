package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.ANTA_MAAN;
import static l1j.server.server.model.skill.L1SkillId.BIRTH_MAAN;
import static l1j.server.server.model.skill.L1SkillId.CURSE_BLIND;
import static l1j.server.server.model.skill.L1SkillId.DARKNESS;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_PUPLE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_TOPAZ;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;
import static l1j.server.server.model.skill.L1SkillId.FAFU_MAAN;
import static l1j.server.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.HOLY_WALK;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.LIFE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.LINDBIOR_SPIRIT_EFFECT;
import static l1j.server.server.model.skill.L1SkillId.LIND_MAAN;
import static l1j.server.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.SLOW;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL4;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL5;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL6;
import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FLOATING_EYE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;
import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;
import static l1j.server.server.model.skill.L1SkillId.STATUS_WISDOM_POTION;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit1;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit2;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit3;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit4;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit5;
import static l1j.server.server.model.skill.L1SkillId.VALA_MAAN;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.MJShiftBattleItemWhiteList;
import MJShiftObject.Template.CommonServerInfo;
import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJActionListener.ActionListenerLinkageLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJCTSystem.MJCTHandler;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCharacterActionSystem.WandActionHandlerFactory;
import l1j.server.MJCompanion.Basic.Potion.MJCompanionPotionInfo;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJDTSSystem.MJDTSLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimePotionLoader;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJItemEnchantSystem.MJItemEnchanterLoader;
import l1j.server.MJItemExChangeSystem.MJItemExChangeLoader;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveLoader;
import l1j.server.MJRaidSystem.Loader.MJRaidCreatorLoader;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.PrideSystem.PrideLoadManager;
import l1j.server.Stadium.StadiumManager;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.datatables.AccessoryEnchantInformationTable;
import l1j.server.server.datatables.ArmorEnchantInformationTable;
import l1j.server.server.datatables.ArmorEnchantList;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.ItemShopTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.WeaponEnchantInformationTable;
import l1j.server.server.datatables.WeaponEnchantList;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1SupportInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.L1TradeItemBox;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.item.function.L1OrimScrollEnchant;
import l1j.server.server.model.item.function.OmanAmulet;
import l1j.server.server.model.item.function.OmanRandomAmulet;
import l1j.server.server.model.item.function.TelBook;
import l1j.server.server.model.item.function.Telbookitem;
import l1j.server.server.model.item.function.additem;
import l1j.server.server.model.item.function.additem2;
import l1j.server.server.model.item.function.additemnew;
import l1j.server.server.model.item.function.omanTel;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanName;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DollOnOff;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_IdentifyDesc;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShowPolyList;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconWisdomPotion;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TamWindow;
import l1j.server.server.serverpackets.S_TradeItemShop;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.serverpackets.S_UserCommands5;
import l1j.server.server.serverpackets.S_War;
import l1j.server.server.serverpackets.S_낚시;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;

public class C_ItemUSe extends ClientBasePacket {

	private static final String C_ITEM_USE = "[C] C_ItemUSe";
	private static Logger _log = Logger.getLogger(C_ItemUSe.class.getName());

	private static Random _random = new Random(System.nanoTime());

	private static boolean _randomInit = false;
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

	public C_ItemUSe(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);

		int itemObjid = readD();
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost() || pc.getMapId() == 5166) {
			return;
		}
		if (!_randomInit) {// 예쁜 분포를 위해 좀 돌려준다.
			for (int i = 0; i < 100000; ++i) {
				_random.nextInt(2000000000);
			}
			_randomInit = true;
		}
		//279728854
		//278991660

		L1ItemInstance l1iteminstance = pc.getInventory().getItem(itemObjid);

		if (l1iteminstance == null || l1iteminstance.getItem() == null) {
			return;
		}

		if (l1iteminstance.getItem().getUseType() == -1) { // none:사용할 수 없는 아이템
			// \f1%0은 사용할 수 없습니다.
			pc.sendPackets(new S_ServerMessage(74, l1iteminstance.getLogName()));
			return;
		}
		if(!MJShiftBattleItemWhiteList.getInstance().use(pc, l1iteminstance)){
			pc.sendPackets(String.format("%s(은)는 현재 사용할 수 없습니다.", l1iteminstance.getName()));
			return;
		}
		
		int pcObjid = pc.getId();
		if (pc.get_teleport()) { // 텔레포트 처리중
			return;
		}

		// 존재버그 관련 추가
		L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
		if (jonje == null && pc.getAccessLevel() != 200) {
			pc.sendPackets(new S_SystemMessage("존재버그 강제종료! 재접속하세요"));
			client.kick();
			return;
		}

		if (pc.isDead() == true) {
			return;
		}
		if (!pc.getMap().isUsableItem()) { // -- 해당 맵에선 사용 할수 없다고 표기 .
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			pc.sendPackets(new S_Paralysis(7, false));
			return;
		}
		if (CommonUtil.teleport_check(pc, l1iteminstance)) {
			pc.sendPackets(new S_Paralysis(7, false));
			pc.sendPackets(new S_ServerMessage(647)); // \f1 이 곳에서는 텔레포트를 할 수 없습니다.
			return;
		}

		int itemId;
		try {
			itemId = l1iteminstance.getItem().getItemId();
		} catch (Exception e) {
			return;
		}
		
		/*
		 * 퀘스트 아이템사용부분 40030 수련자의 강화속도향상물약 42655 수련자의 영웅변신주문서 43201 클라우디아 귀환부적
		 * 클라우디아 수정요청 혹시 아래 아이템 번호가 기존서버의 번호와 다르게 적용할경우 수정해주세요 변수를 타고가서 거기도 번호가 존재합니다
		 */
		if (itemId == 7006 || itemId == 40096 || itemId == 30027 || itemId == 30028 || itemId == 41245
				|| itemId == 42654 || itemId == 7004 || itemId == 7005 || itemId >= 9000 && itemId <= 9004
				|| itemId == 40095) {
			quest_consumeitem(pc, itemId);
		}

		if (MJRaidCreatorLoader.getInstance().createRaid(pc, l1iteminstance))
			return;

		/** MJCTSystem **/
		if (itemId == MJCTLoadManager.CTSYSTEM_LOAD_ID) {
			MJCTHandler.load(pc, l1iteminstance);
			return;
		}

		if (MJCompanionInstance.use_item(pc, l1iteminstance))
			return;
		MJCompanionPotionInfo pInfo = MJCompanionPotionInfo.get_potion_info(l1iteminstance.getItemId());
		if (pInfo != null) {
			pInfo.use_item(pc, l1iteminstance);
			return;
		}

		if (MJBotLoadManager.useBotItem(pc, itemId))
			return;

		MJPacketParser parser = WandActionHandlerFactory.create(l1iteminstance);
		if (parser != null) {
			parser.parse(pc, this);
			parser.doWork();
			return;
		}

		if (MJPassiveLoader.getInstance().useItem(pc, l1iteminstance))
			return;

		if (DungeonTimePotionLoader.getInstance().use_potion(pc, l1iteminstance))
			return;

		if (ActionListenerLinkageLoader.getInstance().use_item(pc, l1iteminstance))
			return;

		if (L1ClanJoinInstance.use_item(pc, l1iteminstance))
			return;

		if (MJDTSLoader.getInstance().use_item(pc, l1iteminstance))
			return;
		if (PrideLoadManager.getInstance().use_item(pc, l1iteminstance))
			return;
		
		/**아이템 샵 */		
		if (ItemShopTable.getInstance().UseItemShop(pc, l1iteminstance.getItemId())) {
			return;
		}

		if (Config.USE_SHIFT_SERVER && itemId == MJShiftObjectManager.getInstance().get_character_transfer_itemid()) {
			if (MJShiftObjectManager.getInstance().is_battle_server_running()) {
				pc.sendPackets("서버 대항전 진행중에는 서버이전을 사용할 수 없습니다.");
				return;
			}
			if (pc.getInventory().checkItem(itemId)) {
				List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(true);
				if (servers == null || servers.size() <= 0) {
					pc.sendPackets("현재 이전할 수 있는 서버가 없습니다.");
					return;
				}
				int success_count = servers.size();
				for (CommonServerInfo csInfo : servers) {
					String message = "이전 가능";
					if (!csInfo.server_is_on) {
						--success_count;
						message = "이전 불가능(서버OFF)";
					}
					if (!csInfo.server_is_transfer) {
						--success_count;
						message = "이전 불가능(기능 닫힘)";
					}
					pc.sendPackets(String.format("- [%s] %s", csInfo.server_description, message));
				}
				if (success_count <= 0) {
					pc.sendPackets("현재 이전할 수 있는 서버가 없습니다.");
					return;
				}
				pc.sendPackets("이전할 서버를 입력해주세요.");
				pc.set_ready_server_shift(true);
				return;
			}
		}
		
		if (IntRange.includes(itemId, 4100255, 4100259)) {
			if (!pc.isGm())
				return;

			int idx = itemId - 4100255;
			BugRaceController.getInstance()._is_downs[idx] = true;
			pc.sendPackets(String.format("%d번 버그베어를 넘어뜨렸습니다.", idx + 1));
			return;
		}

		int l_id = readD();
		if (MJItemExChangeLoader.getInstance().use_item(pc, l1iteminstance, l_id)) {
			return;
		} else {
			readP(-4);
		}

		int l = 0;

		String s = "";

		int blanksc_skillid = 0;
		int spellsc_objid = 0;
		int spellsc_x = 0;
		int spellsc_y = 0;
		int resid = 0;
		int cookStatus = 0;
		int cookNo = 0;
		int fishX = 0;
		int fishY = 0;
		short bookmark_mapid = 0;
		int bookmark_x = 0;
		int bookmark_y = 0;
		int BookTel = 0;
		int use_objid = 0;
		int use_type = l1iteminstance.getItem().getUseType();

		/** 아이템번호 추가해야한다 아니면 사용이안됨 **/
		switch (itemId) {
		case 40088:
		case 40096:
		case 140088:
		case 210112:
		case 900075: //변신 지배반지
			s = readS();
		break;
		/** 특수 인챈트 시스템 **/
		case 300000:
		/** 특수 마법인형 인챈트 시스템 **/
		case 300001:
		case 40074:
		case 40087:
		case 724:
		case 3000546:
		case 3000547:
		case 40660:
		case 40128:
		case 40127:
		case 814: // 할로윈 마법 주문서
		case 30027: // 수련자갑옷마법
		case 30028: // 수련자무기마법
		case 7004:
		case 30068:
		case 30069:
		case 40077:
		case 40078:
		case 40126:
		case 40098:
		case 40129:
		case 40130:
		case 140129:
		case 140130:
		case 140074:
		case 140087:
		case 240074:
		case 240087:
		case 41029:
		case 40317:
		case 41036:
		case 41245:
		case 30087:
		case 210073:
		case 210077:
		case 500205:
		case 500207:
		case 210082:
		case 210084:
		case 210085:
		case 40964:
		case 41030:
		case 50020:
		case 50021:
		case 210064:
		case 210065:
		case 210066:
		case 210067:
		case 210068:
		case 41048:
		case 41049:
		case 41050:
		case 41051:
		case 41052:
		case 41053:
		case 41054:
		case 41055:
		case 41056:
		case 41057:
		case 40925:
		case 40926:
		case 40927:
		case 40928:
		case 40929:
		case 40931:
		case 40932:
		case 40933:
		case 40934:
		case 40935:
		case 40936:
		case 40937:
		case 40938:
		case 40939:
		case 40940:
		case 40941:
		case 40942:
		case 40943:
		case 40944:
		case 40945:
		case 40946:
		case 40947:
		case 40948:
		case 40949:
		case 40950:
		case 40951:
		case 40952:
		case 40953:
		case 40954:
		case 40955:
		case 40956:
		case 40957:
		case 40958:
		case 410017:
		case 100001: // 인형 변경 비법 주문서
		case 1000021:
		case 1000022:
		case 1000023:
		case 1000024:
		case 1000025:
		case 1000026:
		case 1000027:
		case 1000028:
		case 1000029:
		case 1000030:
		case 1000031:
		case 1000032:
		case 1000033:
		case 1000034:
		case 1000035:
		case 1000036:
		case 1000037:
		case 1000038:
		case 1000039:
		case 1000040:
		case 1000012:
		case 3000160: // 일반 용의 티셔츠 강화 주문서
		case 3000161: // 저주 용의 티셔츠 강화 주문서
		case 3000162: // 축복 용의 티셔츠 강화 주문서
		case 3000217: // 일반 유니콘의 갑옷 마법 주문서
		case 3000218: // 저주 유니콘의 갑옷 마법 주문서
		case 3000219: // 축복 유니콘의 갑옷 마법 주문서
		case 410066:
		case 410067:
		case 410068:
		case 410083:
		case 410089:
		case 3000430:
		case 410094:
		case 30107:
		case 30108:
		case 30109:
		case 30110:
		case 30111:
		case 30112:
		case 30113:
		case 30114:
		case 30115:
		case 30116:
		case 30117:
		case 68076:
		case 68077: // 고대의 서:갑옷
		case 680777: // 할파스의 숨결
		case 4100136: // 사신의 숨결
		case 4100034: // 빛나는 아덴 용사의 보호 주문서
		case 3000380:
		case 3000428: // 스냅퍼보호주문서
		case 810003:
		case 4100148: // 사신의 무기 마법 주문서
		case 127000:
		case 560030:
		case 560031:
		case 560032:
		case 560033:
		case 410140:
		case 410141:
		case 410142:
		case 810012:
		case 810013:
		case 7024:
		case 87056:
		case 87057:
		case 4100292:
		case 600228:
		case 3000065:
		case 3000256: // 생명의 나뭇잎
		case 3110509:
		case 3000509: // 축복받은 생명의 나뭇잎	
		case 500716:
		case 3010255:
		case 500723:
		case 3000100: // 문장강화석
		case 3000517:
		case 5991:
		case 3000518:
		case 7010: //축복의 돌
		case 7011: //빛나는 축복의 돌
		case 3000255:
		case 3000224:
		case 202220: //장비교환권
			l = readD();
		break;
		case 40090:
		case 40091:
		case 40092:
		case 40093:
		case 40094:
			blanksc_skillid = readC();
		break;
		case 40895:
		case 40870:
		case 40879:
			spellsc_objid = readD();
		break;
		case 40089:
		case 140089:
		case 30074:
			resid = readD();
		break;
		case 560025:
		case 560027:
		case 560028:
		case 560029:
		case 4100135:
			BookTel = readC();
		break;
		case 150100:
		case 140100:
		case 40100:
		case 40099:
		case 40086:
		case 40863:
			bookmark_mapid = (short) readH();
			bookmark_x = readH();
			bookmark_y = readH();
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		break;
		case 41293:
		case 41294:
		case 41305:
		case 41306:
		case 4100293:
		case 600229: // 성장의 낚싯대
		case 87058:
		case 87059:
		case 9991:
			fishX = readH();
			fishY = readH();
		break;
		case 40008:
		case 140008:
			spellsc_objid = readD();
			s = readS();
		break;
		default:
			if ((use_type == 30)) { // spell_buff
				spellsc_objid = readD();

				/** MJCTSystem **/
			} else if (itemId == 600226 || itemId == 600227 || itemId == 3000235
					|| itemId == MJCTLoadManager.CTSYSTEM_STORE_ID) {
				use_objid = readD();
			} else if ((use_type == 5) || (use_type == 17)) { // spell_long、spell_short
				spellsc_objid = readD();
				spellsc_x = readH();
				spellsc_y = readH();
			} else if ((itemId >= 41255) && (itemId <= 41259)) { // 요리책
				cookStatus = readC();
				cookNo = readC();
			} else {
				l = readC();
			}
		break;
		}

		if (pc.getCurrentHp() > 0) {
			int delay_id = 0;
			if (l1iteminstance.getItem().getType2() == 0) { // 종별：그 외의 아이템
				if (l1iteminstance.getItem() instanceof L1EtcItem) {
					delay_id = ((L1EtcItem) l1iteminstance.getItem()).get_delayid();
				}
			}
			if (delay_id != 0) { // 지연 설정 있어
				if (pc.hasItemDelay(delay_id) == true) {
					return;
				}
			}
			// 재사용 체크
			boolean isDelayEffect = false;

			// 티켓이라면 리턴
			if (itemId >= 8000000 && itemId <= 9000000) {
				return;
			}

			if (l1iteminstance.getItem().getType2() == 0) {
				int delayEffect = ((L1EtcItem) l1iteminstance.getItem()).get_delayEffect();
				if (delayEffect > 0) {
					isDelayEffect = true;
					Timestamp lastUsed = l1iteminstance.getLastUsed();
					if (lastUsed != null) {
						Calendar cal = Calendar.getInstance();
						if ((cal.getTimeInMillis() - lastUsed.getTime()) / 1000 <= delayEffect) {
							pc.sendPackets(
									new S_SystemMessage(
											((delayEffect - (cal.getTimeInMillis() - lastUsed.getTime()) / 1000) / 60)
													+ "분 "
													+ ((delayEffect
															- (cal.getTimeInMillis() - lastUsed.getTime()) / 1000) % 60)
													+ "초 후에 사용할 수 있습니다. "));
							return;
						}
					}
				}
			}
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(l);
			_log.finest("request item use (obj) = " + itemObjid + " action = " + l + " value = " + s);

			if (l1iteminstance.getItem().getType2() == 0) { // 종별：그 외의 아이템
				int item_minlvl = ((L1EtcItem) l1iteminstance.getItem()).getMinLevel();
				int item_maxlvl = ((L1EtcItem) l1iteminstance.getItem()).getMaxLevel();
				if (item_minlvl != 0 && item_minlvl > pc.getLevel() && !pc.isGm()) {
					pc.sendPackets(new S_SystemMessage(item_minlvl + "레벨 이상이 되면 사용할 수 있는 아이템입니다."));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
					return;
				} else if (item_maxlvl != 0 && item_maxlvl < pc.getLevel() && !pc.isGm()) {
					pc.sendPackets(new S_SystemMessage(item_maxlvl + "레벨 이하일 때만 사용할 수 있는 아이템입니다."));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
					return;
				}
				if ((itemId == 40576 && !pc.isElf()) || (itemId == 40577 && !pc.isWizard()) // 영혼의
						|| (itemId == 40578 && !pc.isKnight())) { // 영혼의 결정의
					pc.sendPackets(new S_ServerMessage(264)); // \f1당신의 클래스에서는 이
					return;
				}

				if (l1iteminstance.getItem().getType() == 0) { // 에로우
					if (pc.getWeapon() == null) {
						pc.getInventory().setArrow(l1iteminstance.getItem().getItemId());
						pc.sendPackets(new S_ItemName(l1iteminstance));
					} else if (pc.getWeapon().getItem().getType() != 4) {
						L1ItemInstance arrow = pc.getInventory().findItemId(pc.getInventory().getArrowItemId());
						if (arrow != null) {
							pc.getInventory().setArrow(0);
							pc.sendPackets(new S_ItemName(arrow));
							pc.sendPackets(new S_ItemName(l1iteminstance));
						}
					} else if (pc.getInventory().getArrow() != null) {
						L1ItemInstance arrow = pc.getInventory().findItemId(pc.getInventory().getArrow().getItemId());
						pc.getInventory().setArrow(l1iteminstance.getItem().getItemId());
						pc.sendPackets(new S_ItemName(arrow));
						pc.sendPackets(new S_ServerMessage(452, l1iteminstance.getLogName())); // %0가
																								// 선택되었습니다.
						pc.sendPackets(new S_ItemName(l1iteminstance));
					} else {
						pc.getInventory().setArrow(l1iteminstance.getItem().getItemId());
						pc.sendPackets(new S_ServerMessage(452, l1iteminstance.getLogName())); // %0가
						pc.sendPackets(new S_ItemName(l1iteminstance));
					}
				} else if (l1iteminstance.getItem().getType() == 15) { // 스팅
					if (pc.getInventory().getSting() != null) {
						L1ItemInstance arrow = pc.getInventory().findItemId(pc.getInventory().getSting().getItemId());
						pc.getInventory().setSting(l1iteminstance.getItem().getItemId());
						pc.sendPackets(new S_ItemName(arrow));
						pc.sendPackets(new S_ServerMessage(452, l1iteminstance.getLogName())); // %0가
																								// 선택되었습니다.
						pc.sendPackets(new S_ItemName(l1iteminstance));
					} else {
						pc.getInventory().setArrow(l1iteminstance.getItem().getItemId());
						pc.sendPackets(new S_ServerMessage(452, l1iteminstance.getLogName())); // %0가
																								// 선택되었습니다.
						pc.sendPackets(new S_ItemName(l1iteminstance));
					}
				} else if (l1iteminstance.getItem().getType() == 16) { // treasure_box
					L1TreasureBox box = L1TreasureBox.get(itemId);
					if (pc.getInventory().getSize() > 170) {
						pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다"));
						return;
					}
					if (pc.getInventory().getWeight100() > 82) { // 이부분 수정하면오류난다
						pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
						return;
					}
					if (box != null) {
						if (box.open(pc)) {
							L1EtcItem temp = (L1EtcItem) l1iteminstance.getItem();
							if (temp.get_delayEffect() > 0) {
								isDelayEffect = true;
							} else {
								pc.getInventory().removeItem(l1iteminstance.getId(), 1);
							}
						}
					}

				} else if (l1iteminstance.getItem().getType() == 2) { // light
					if (l1iteminstance.getRemainingTime() <= 0 && itemId != 40004) {
						return;
					}
					if (l1iteminstance.isNowLighting()) {
						l1iteminstance.setNowLighting(false);
						pc.getLight().turnOnOffLight();
					} else {
						l1iteminstance.setNowLighting(true);
						pc.getLight().turnOnOffLight();
					}
					pc.sendPackets(new S_ItemName(l1iteminstance));
				}

				switch (itemId) {
				// 스위치문시작
				case 140009:	// 변신 유일 반지
					L1PolyMorph.doPoly(pc, 5146, 7200, L1PolyMorph.MORPH_BY_ITEMMAGIC, true,true);
					break;
				case 700085:
				case 700086:
					if (pc.getLevel() >= 82) {
						pc.setTemporaryItemObjectId(l1iteminstance.getId());
						pc.sendPackets("확성할 메시지를 입력해주세요.");
					} else
						pc.sendPackets("레벨 82이하의 용사님은 사용이 불가능 합니다.");
					break;
				case 42656: // 전사의 환영 물약 상자
					// -- 사용시 횟수 감소
					l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
					// -- 업데이트
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
					// -- 0 일시 삭제
					if (l1iteminstance.getChargeCount() == 0) {
						pc.getInventory().removeItem(l1iteminstance);
						pc.sendPackets(new S_SystemMessage(l1iteminstance.getLogName() + " 다 사용 하였습니다."));
					}
					break;
				case 90736:
				case 90735:
				case 3000245:
				case 3000244:
				case 3000242:
				case 90737:
				case 6012:
				case 6016: {
					int random = CommonUtil.random(100);
					L1ItemInstance item = null;
					if (itemId == 90737) { // -- 마빈의 큐브 100회
						item = pc.getInventory().storeItem(700013, 1);
						pc.sendPackets(new S_SystemMessage("획득: " + item.getLogName()));
					} else if (itemId == 6012) { // -- 게렝의 마법 주머니 100회
						createNewItem(pc, 6003, 1);
					} else if (itemId == 6016) { // -- 수련자의 보급 상자 20회
						createNewItem(pc, 40098, 10);
						createNewItem(pc, 40099, 10);
						createNewItem(pc, 40029, 100);
						createNewItem(pc, 40096, 3);
						createNewItem(pc, 40096, 2);
						if (pc.isElf()) {
							createNewItem(pc, 42651, 3);
						} else if (pc.isWizard() || pc.isBlackwizard()) {
							createNewItem(pc, 42652, 3);
						} else {
							createNewItem(pc, 42650, 3);
						}
					}
					// -- 사용시 횟수 감소
					l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
					// -- 업데이트
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
					// -- 0 일시 삭제
					if (l1iteminstance.getChargeCount() == 0) {
						pc.getInventory().removeItem(l1iteminstance);
						pc.sendPackets(new S_SystemMessage(l1iteminstance.getLogName() + " 다 사용 하였습니다."));
					}
				}
					break;
				case 41922:
				case 41923:
				case 41924:
				case 41925: {
					int buffId = itemId - 34243;
					L1SkillUse l1Skilluse = new L1SkillUse();
					l1Skilluse.handleCommands(pc, buffId, pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_GMBUFF);
				}
					break;
				case 50020: // 봉인줌서
					if (l1iteminstance1.getBless() == 0 || l1iteminstance1.getBless() == 1
							|| l1iteminstance1.getBless() == 2 || l1iteminstance1.getBless() == 3) {
						int Bless = 0;
						switch (l1iteminstance1.getBless()) {
						case 0:
							Bless = 128;
							break; // 축
						case 1:
							Bless = 129;
							break; // 보통
						case 2:
							Bless = 130;
							break; // 저주
						case 3:
							Bless = 131;
							break; // 미확인
						}
						l1iteminstance1.setBless(Bless);
						int st = 0;
						if (l1iteminstance1.isIdentified())
							st += 1;
						if (!l1iteminstance1.getItem().isTradable())
							st += 2;
						if (l1iteminstance1.getItem().isCantDelete())
							st += 4;
						if (l1iteminstance1.getItem().get_safeenchant() < 0)
							st += 8;
						if (l1iteminstance1.getBless() >= 128) {
							st = 32;
							if (l1iteminstance1.isIdentified()) {
								st += 15;
							} else {
								st += 14;
							}
						}
						pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
						pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else
						pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
					// 일어나지
					// 않았습니다.
					break;

				case 50021:// 봉인해제줌서
					if (l1iteminstance1.getBless() == 128 || l1iteminstance1.getBless() == 129
							|| l1iteminstance1.getBless() == 130 || l1iteminstance1.getBless() == 131) {
						int Bless = 0;
						switch (l1iteminstance1.getBless()) {
						case 128:
							Bless = 0;
							break;// 축
						case 129:
							Bless = 1;
							break;// 보통
						case 130:
							Bless = 2;
							break;// 저주
						case 131:
							Bless = 3;
							break; // 미확인
						}
						l1iteminstance1.setBless(Bless);
						int st = 0;
						if (l1iteminstance1.isIdentified())
							st += 1;
						if (!l1iteminstance1.getItem().isTradable())
							st += 2;
						if (l1iteminstance1.getItem().isCantDelete())
							st += 4;
						if (l1iteminstance1.getItem().get_safeenchant() < 0)
							st += 8;
						if (l1iteminstance1.getBless() >= 128) {
							st = 32;
							if (l1iteminstance1.isIdentified()) {
								st += 15;
							} else {
								st += 14;
							}
						}
						pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, l1iteminstance1, st));
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
						pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else
						pc.sendPackets(new S_ServerMessage(79));// \f1 아무것도 일어나지
					// 않았습니다.
					break;

				case L1ItemId.DRAGON_PEARL:// 드래곤 진주
				case L1ItemId.DRAGON_PEARL1:// 드래곤 진주 교환불가
					useDragonPearl(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 500727: // 무한 드래곤의 진주
					useDragonPearl(pc, itemId);
					break;
				case 3000224:
					if (l1iteminstance1.getItem().getItemId() != 1000004
							&& l1iteminstance1.getItem().getItemId() != 410064) {
						pc.sendPackets(new S_ServerMessage(79));// 아무것도일어나지않았습니다.
						return;
					}
					pc.sendPackets(new S_SystemMessage("드래곤의 다이아몬드/에메랄드를 드래곤의 토파즈 2개로 변환."));
					pc.getInventory().storeItem(7241, 2);
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 410139:
					if (pc.getLevel() >= 82) {
						pc.sendPackets("레벨 82이상은 사용이 불가능 합니다.");
						return;
					}
				case 6017:
				case 410138:
				case 410064:
				case 1000002:
				case 1000003:
				case 1000004:
				case 1000007:
				case 3000231:
				case 7241:
				case 3000457:
				case 60255:
					/** 드래곤의 최고급 다이아몬드 **/
					if (itemId == L1ItemId.DRAGON_DIAMOND2) {
						if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
							pc.sendPackets(new S_ServerMessage(2146));
							return;
						}
//						if (pc.getAccount().getBlessOfAin() <= 12000000) {
						if (pc.getAccount().getBlessOfAin() <= 34000000) {
							if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
								if (pc.getAccount().getGrangKinAngerTime() != 0) {
									pc.getAccount().setGrangKinAngerTime(0, pc);
								}
							}
							pc.getAccount().addBlessOfAin(16000000, pc);
							pc.sendPackets(new S_SystemMessage("아인하사드의 축복이 1600% 추가되었습니다."));
							pc.sendPackets(new S_SkillSound(pc.getId(), 15357));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 15357));
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("축복지수 3400미만에서 사용할 수 있습니다."));
						}
					} else if (itemId == L1ItemId.DRAGON_DIAMOND || itemId == L1ItemId.DRAGON_DIAMOND1 || itemId == 1000007) {
						if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
							pc.sendPackets(new S_ServerMessage(2146));
							return;
						}
						if (pc.getAccount().getBlessOfAin() <= 45000000) {
							if (itemId == 1000007) {
								if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
									if (pc.getAccount().getGrangKinAngerTime() != 0) {
										pc.getAccount().setGrangKinAngerTime(0, pc);
									}
								}
								pc.getAccount().addBlessOfAin(6000000, pc);
							} else {
								if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
									if (pc.getAccount().getGrangKinAngerTime() != 0) {
										pc.getAccount().setGrangKinAngerTime(0, pc);
									}
								}
								pc.getAccount().addBlessOfAin(1000000, pc);
							}
							if (itemId == 1000007) {
								pc.sendPackets(new S_SystemMessage("아인하사드의 축복이 600% 추가되었습니다."));
							} else {
								pc.sendPackets(new S_SystemMessage("아인하사드의 축복이 100% 추가되었습니다."));
							}
							pc.sendPackets(new S_SkillSound(pc.getId(), 15357));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 15357));
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("축복지수 4500미만에서 사용할 수 있습니다."));
						}
					} else if (itemId == L1ItemId.DRAGON_SAPPHIRE) {
						if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
							pc.sendPackets(new S_ServerMessage(2146));
							return;
						}
						if (pc.getAccount().getBlessOfAin() <= 47000000) {
							if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
								if (pc.getAccount().getGrangKinAngerTime() != 0) {
									pc.getAccount().setGrangKinAngerTime(0, pc);
								}
							}
							pc.getAccount().addBlessOfAin(500000, pc);
							pc.sendPackets(new S_SystemMessage("아인하사드의 축복: 50% 추가."));
							pc.sendPackets(new S_SkillSound(pc.getId(), 15357));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 15357));
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("축복지수 4700미만에서만 사용하실수 있습니다."));
						}
					} else if (itemId == L1ItemId.DRAGON_RUBY || itemId == 6017) {
						if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
							pc.sendPackets(new S_ServerMessage(2146));
							return;
						}
						if (pc.getAccount().getBlessOfAin() <= 47000000) {
							if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
								if (pc.getAccount().getGrangKinAngerTime() != 0) {
									pc.getAccount().setGrangKinAngerTime(0, pc);
								}
							}
							pc.getAccount().addBlessOfAin(300000, pc);
							pc.sendPackets(new S_SystemMessage("아인하사드의 축복: 30% 추가."));
							pc.sendPackets(new S_SkillSound(pc.getId(), 15357));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 15357));
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							pc.sendPackets(new S_SystemMessage("축복지수 4700미만에서만 사용하실수 있습니다."));
						}
					} else if (itemId == L1ItemId.EMERALD || itemId == L1ItemId.EMERALD1) {
						if (pc.hasSkillEffect(L1SkillId.EMERALD_NO) == true) {
							pc.sendPackets(new S_ServerMessage(2145));
							return;
						} else if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
							pc.sendPackets(new S_ServerMessage(2147));
							return;
						} else if (pc.hasSkillEffect(L1SkillId.DRAGON_PUPLE)
								|| pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
							pc.sendPackets(new S_ServerMessage(2147));
							return;
						}
						if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
							if (pc.getAccount().getGrangKinAngerTime() != 0) {
								pc.getAccount().setGrangKinAngerTime(0, pc);
							}
						}
						pc.getAccount().addBlessOfAin(1000000, pc);
						pc.setSkillEffect(L1SkillId.EMERALD_YES, 1800 * 1000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 0x02, 1800));
						pc.sendPackets(new S_ServerMessage(2140));
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else if (itemId == 60255) {
						if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
							pc.sendPackets(new S_ServerMessage(2146));
							return;
						}
						if (pc.getAccount().getBlessOfAin() <= 10000) {
							pc.sendPackets(new S_SystemMessage("축복지수가 있어야 사용하실수 있습니다."));
							return;
						}
						if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
							pc.removeSkillEffect(L1SkillId.DRAGON_TOPAZ);
						}

						pc.setSkillEffect(DRAGON_PUPLE, 1800 * 1000);
						pc.sendPackets(new S_PacketBox(1800, 1, true, true));
						pc.sendPackets(new S_SkillSound(pc.getId(), 197));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 197));
						pc.getInventory().removeItem(l1iteminstance, 1);

					} else if (itemId == 7241 || itemId == 3000457) {
						if (pc.hasSkillEffect(L1SkillId.EMERALD_YES) == true) {
							pc.sendPackets(new S_ServerMessage(2146));
							return;
						}
						if (pc.getAccount().getBlessOfAin() <= 10000) {
							pc.sendPackets(new S_SystemMessage("축복지수가 있어야 사용하실수 있습니다."));
							return;
						}
						if (pc.hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
							pc.removeSkillEffect(L1SkillId.DRAGON_PUPLE);
						}

						pc.setSkillEffect(DRAGON_TOPAZ, 1800 * 1000);
						pc.sendPackets(new S_PacketBox(1800, 2, true, true));
						pc.sendPackets(new S_SkillSound(pc.getId(), 197));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 197));
						pc.getInventory().removeItem(l1iteminstance, 1);
					}

					break;

				case 410032:
				case 410033:
				case 410034:
				case 410035:
				case 410036:
				case 410037:
				case 410038:
				case 3000159: // 일회용
					/*
					 * 수룡의 마안, 풍룡의 마안 , 지룡의 마안 , 탄생의 마안, 형상의 마안 , 생명의 마안
					 * 
					 */

					if (itemId >= 410032 && itemId <= 410035) {//수룡 ~ 화룡
						if (pc.getInventory().checkItem(41246, 1000) == false) {
							pc.sendPackets(new S_SystemMessage("결정체가 부족합니다."));
							return;
						}
					}
					if (itemId >= 410036 && itemId <= 410038) {//탄생 형상 생명
						if (pc.getInventory().checkItem(41246, 2000) == false) {
							pc.sendPackets(new S_SystemMessage("결정체가 부족합니다."));
							return;
						}
					}
					
					if (l1iteminstance.getLastUsed() == null) {
						Timestamp ts1 = new Timestamp(System.currentTimeMillis());
						l1iteminstance.setLastUsed(ts1);
					}
					Timestamp lastUsed = l1iteminstance.getLastUsed();
					Calendar cal = Calendar.getInstance();
					if ((cal.getTimeInMillis() - lastUsed.getTime()) / 1000 >= 0) {

						if (itemId >= 410032 && itemId <= 410035) {//수룡 ~ 화룡
							if (pc.getInventory().consumeItem(41246, 1000) == false) {
								pc.sendPackets(new S_SystemMessage("결정체가 부족합니다."));
								return;
							}
						}
						if (itemId >= 410036 && itemId <= 410038) {//탄생 형상 생명
							if (pc.getInventory().consumeItem(41246, 2000) == false) {
								pc.sendPackets(new S_SystemMessage("결정체가 부족합니다."));
								return;
							}
						}						
						if (itemId == 410032) { // 수룡
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, FAFU_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
						}
						if (itemId == 410033) { // 풍룡
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, LIND_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
						}
						if (itemId == 410034) { // 지룡
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, ANTA_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
						}
						if (itemId == 410035) { // 화룡
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, VALA_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
						}
						if (itemId == 410036) { // 탄생
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, BIRTH_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
						}
						if (itemId == 410037) { // 형상
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, SHAPE_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
						}
						if (itemId == 410038) { // 생명
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, LIFE_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
						}
						if (itemId == 3000159) { // 생명 일회용
							long curtimea = System.currentTimeMillis() / 1000;
							if (pc.getQuizTime() + 1800 > curtimea) {
								long time = (pc.getQuizTime() + 1800) - curtimea;
								pc.sendPackets(new S_ChatPacket(pc, time + " 초 후 사용 하시길 바랍니다."));
								return;
							}
							L1SkillUse l1Skilluse = new L1SkillUse();
							l1Skilluse.handleCommands(pc, LIFE_MAAN, pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
							Timestamp ts = new Timestamp(System.currentTimeMillis());
							l1iteminstance.setLastUsed(ts);
							pc.setQuizTime(curtimea);
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
				break;
				case 410010:
				case 410011:
				case 410012:
				case 4100039:
				case 4100041:
				case 4100042:
				case 30063:
					/*
					 * 체력 증강의 주문서, 마력 증강의 주문서, 전투 강화의 주문서 , 드래곤의 돌
					 */
					if (itemId == 30063) { // 드래곤의 돌
						if (!(pc.getMapId() >= 1005 && pc.getMapId() <= 1022
								|| pc.getMapId() > 6000 && pc.getMapId() < 6999)) {
							pc.sendPackets(new S_ServerMessage(1891));// 드래곤의
							// 숨결이
							return;
						}
					}
					useCashScroll(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
				break;
					
				//물약회복량 처리관련
				case 7007:
				case 40010:
				case 40011:
				case 40012:
				case 4100021:
				case 40019:
				case 40020:
				case 40021:
				case 40022:
				case 40023:
				case 40024:
				case 40026:
				case 40027:
				case 40028:
				case 40043:
				case 40058:
				case 40071:
				case 40506:
				case 40930:
				case 41141:
				case 41337:
				case 60029:
				case 60030:
				case 140010:
				case 140011:
				case 140012:
				case 140506:
				case 240010:
				case 41403:
				case 410000:
				case 410003:
				case 30062:
				case 30056:
				case 42658: // 전사의 체력 회복제
				case 40029:
				case 4100152:
				case 4100153:
				case 4100154:
					if (itemId == 30062) {
						if (!(pc.getMapId() >= 1005 && pc.getMapId() <= 1022
								|| pc.getMapId() > 6000 && pc.getMapId() < 6999)) {
							pc.sendPackets(new S_ServerMessage(1891));
							return;
						}
					}
					if (itemId == 30056) {
						if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2151
								|| pc.getMapId() >= 12152 && pc.getMapId() <= 12200)) {
							pc.sendPackets(new S_SystemMessage("특정지역에서만 사용이 가능합니다."));
							return;
						}
					}

					L1HealingPotion healingPotion = L1HealingPotion.get(itemId);
					healingPotion.use(pc, l1iteminstance);
				break;

				// 물약회복량 처리관련
				case 40858:// 술
					pc.setDrink(true);
					pc.getInventory().removeItem(l1iteminstance, 1);
				break;
				case 708:
					UseExpPotion1(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
				break;
				case 3000456:
				case 30105:// 빛나는 성장의 물약
				case 210094:// 빛나는 성장의 물약
					UseExpPotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
				break;
				case 40030:
					useGreenPotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
				break;
				case 40013:
				case 7006:
				case 40018:
				case 40039:
				case 40040:
				case 41338:
				case 41261:
				case 41262:
				case 41268:
				case 41269:
				case 41271:
				case 41272:
				case 41273:
				case 41342:
				case 30067:
				case 140013:
				case 140018:
				case 30158:
					/*
					 * 초록 물약, 강화 초록 물약, 와인, 위스키, 상아탑의 속도향상 물약 축복받은 포도주, 주먹밥,
					 * 닭꼬치구이, 조각 피자, 옥수수구이 뻥튀기,오뎅, 와플, 메두사의 피, 용사의 속도 향상 물약,
					 */
					useGreenPotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
				break;
				case 40014:
				case 140014:
				case 41415:
					// 용기의 물약, 복지 용기의 물약, 상아탑의 용기의 물약
					if (pc.isKnight() || pc.is전사() || pc.isCrown()) {
						useBravePotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				break;
				case 30073:
					// 용기의 물약, 복지 용기의 물약, 상아탑의 용기의 물약
					if (pc.isKnight() || pc.is전사()) {
						useBravePotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 40068:
				case 140068:
				case 210110:
					// 엘븐 와퍼, 복지 엘븐 와퍼, 상아탑의 엘븐 와퍼
					if (pc.isElf()) {
						useBravePotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30076:
					// 엘븐 와퍼, 복지 엘븐 와퍼, 상아탑의 엘븐 와퍼
					if (pc.isElf()) {
						useBravePotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 712:
				case 40031:
					// 악마의 피, 상아탑의 악마의 피 , 복지 악마의피
					if (pc.isCrown()) {
						useBravePotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30075:
					// 악마의 피, 상아탑의 악마의 피 , 복지 악마의피
					if (pc.isCrown()) {
						useBravePotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 713:
				case 210036:
					// 유그드라 열매, 상아탑의 유그드라 열매
					if (pc.isBlackwizard()) {
						useFruit(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));// \f1 아무것도 일어나지
						// 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30077:
					// 유그드라 열매, 상아탑의 유그드라 열매
					if (pc.isBlackwizard()) {
						useFruit(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));// \f1 아무것도 일어나지
						// 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 40733:// 명예의 코인
					useBravePotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 210115:// 농축 호흡의 물약
				case 40032:// 에바의 축복
				case 40041:// 인어의 비늘
				case 41344:// 물의 정수
					useBlessOfEva(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30083:// 파란 물약
					useBluePotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 40015:
				case 140015:
				case 40736:// 지혜의 코인
				case 41142:// 픽시의 마나포션
				case 210114:// 복지 파란 물약
					useBluePotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30089:// 지혜의물약
					if (pc.isWizard() || pc.isBlackwizard()) {
						useWisdomPotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 40016:
				case 140016:
				case 210113:
					if (pc.isWizard() || pc.isBlackwizard()) {
						useWisdomPotion(pc, itemId);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 40025:// 불투명 물약
					useBlindPotion(pc);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 4100133:
					pc.sendPackets(new S_ShowPolyList(pc.getId(), "jindeath2017"));
					break;
				case 30060:// 픽시 변신막대
					pc.sendPackets(new S_ShowPolyList(pc.getId(), "pixies"));
					if (!pc.isMagicItem()) {
						pc.setMagicItem(true);
						pc.setMagicItemId(itemId);
					}
					break;
				case 3000421:// 조우의 변신 주문서
					pc.sendPackets(new S_ShowPolyList(pc.getId(), "tam60"));
					if (!pc.isMagicItem()) {
						pc.setMagicItem(true);
						pc.setMagicItemId(itemId);
					}
					break;
					
				case 600198:
				case 600199:
				case 600200:
				case 600201:
				case 600202:
				case 600203:
				case 600204:
				case 600205:
				case 600206:
				case 600207:
				case 600208:
				case 600209:
				case 600210:
				case 600211:
				case 600212:
				case 600213:
				case 600214:
				case 600215:
				case 600216:
				case 600217:
				case 600218:
					강화버프(pc, itemId, l1iteminstance);
					break;
				case 600223:
					if (pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("이미 PC방버프가 적용 되어있습니다."));
						return;
					} else {
						피씨방코인(pc, itemId, l1iteminstance, 7);
						pc.sendPackets(new S_SystemMessage("PC방 이용 시간: 7일 동안 PC방 혜택이 적용 됩니다."));
					}
				break;
				case 3000237:
					if (pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("이미 PC방버프가 적용 되어있습니다."));
						return;
					} else {
						피씨방코인(pc, itemId, l1iteminstance, 3);
						pc.sendPackets(new S_SystemMessage("PC방 이용 시간: 3일 동안 PC방 혜택이 적용 됩니다."));
					}
				break;
				case 600225:
					if (pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("이미 PC방버프가 적용 되어있습니다."));
						return;
					} else {
						피씨방코인(pc, itemId, l1iteminstance, 30);
						pc.sendPackets(new S_SystemMessage("PC방 이용 시간: 30일 동안 PC방 혜택이 적용 됩니다."));
					}
				break;
				case 3000201:
					if (pc.getLevel() <= 55) {
						pc.sendPackets(new S_SystemMessage("레벨 56 이상만 사용가능 합니다."));
						return;
					}
					if (pc.PC방_버프) {
						pc.sendPackets(new S_SystemMessage("이미 PC방버프가 적용 되어있습니다."));
						return;
					} else {
						피씨방코인(pc, itemId, l1iteminstance, 1);
						pc.sendPackets(new S_SystemMessage("PC방 이용 시간: 1일 동안 PC방 혜택이 적용 됩니다."));
					}
				break;
				/** MJCTSystem **/
				case MJCTLoadManager.CTSYSTEM_STORE_ID:
					MJCTHandler.store(pc, l1iteminstance, use_objid);
				break;

				case 600226:// 탐 탐나는 성장의 열매(3일)
				case 3000235:// 탐 탐나는 성장의 열매(7일)
				case 600227:// 탐 탐나는 성장의 열매(30일)
					// System.out.println("objid "+ pc.getId());
					if (pc.getId() == 0) {
						return;
					}
					int day = 0;
					if (itemId == 600226)
						day = 3;// 기간
					if (itemId == 3000235)
						day = 7;// 기간
					if (itemId == 600227)
						day = 30;
					탐열매(pc, use_objid, l1iteminstance, day);
				break;
				case 40096:// 상아탑의 변신 주문서
					if (usePolyScroll(pc, itemId, s)) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(181));// \f1 그러한
					}
					break;
				case 40088:// 변신 주문서
				case 210112:// 복지 변신 주문서
					if (usePolyScroll(pc, itemId, s)) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(181));// \f1 그러한
					}
					break;
				case 900075://변신 지배반지
					usePolyScroll(pc, itemId, s);
					break;
				case 3000467:
					if (!Config.POLY_EVENT) {
						pc.sendPackets(new S_SystemMessage("변신이벤트중일때만 사용가능합니다."));
						return;
					}
					pc.sendPackets(new S_ShowPolyList(pc.getId()));
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 140088:// 축복 변신 주문서
					if (usePolyScroll(pc, itemId, s)) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(181));// \f1 그러한
						// monster에게는
						// 변신할 수
						// 없습니다.
					}
					break;
				/** 랭커 변신 주문서 **/
				case 3000392:
					usePolyRangking(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				/** 영웅 변신 주문서 **/
				case 4100023:
				case 4100024:
				case 4100025:
				case 4100026:
				case 4100027:
				case 4100028:
				case 4100029:
				case 4100030:
				case 4100031:
				case 4100032:
					usePolyScale3(pc, itemId);
					break;
					
				/** 리니지M 변신 카드 **/
				case 220004: // 드래곤 슬레이어
				case 220005: //	나이트 슬레이셔 변신 카드
				case 220006: //	아툰 변신 카드
				case 220007: //	팬텀 나이트 변신 카드
				case 220008: //	사신 변신 카드
				case 220009: //	마커스 변신 카드
				case 220010: //	베리스 변신 카드
				case 220011: //	로엔그린 변신 카드
				case 220012: //	드루가 변신 카드
				case 220013: //	신성검사 변신 카드
				case 220014: //	암흑기사 변신 카드
				case 220015: //	플래티넘 데스나이트 변신 카드
				case 220016: //	플래티넘 바포메트 변신 카드
				case 220017: //	칠흑 도펠갱어 변신 카드
				case 220018: //	금빛 도펠갱어 변신 카드
				case 220019: //	엑스터 변신 카드
				case 220020: //	가드리아 변신 카드
				case 220021: //	게렝 변신 카드
					usePolyScale5(pc, itemId);
					pc.sendPackets(new S_SkillSound(pc.getId(), 6082)); // 변신될때 이펙트
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
					
				/** 전설,신화 변신 주문서 **/
				case 4100160:
				case 4100161:
				case 4100162:
				case 4100163:
					usePolyScale4(pc, itemId);
					break;
					/** 87진데스 변신주문서 **/
					case 4105284:
					case 4105285:
					case 4105286:
					case 4105287:
					case 4105288:
					case 4105289:
						usePolyScale3(pc, itemId);
						break;
						/** 84진데스 변신주문서 **/
						case 4106284:
						case 4106285:
						case 4106286:
						case 4106287:
						case 4106288:
							usePolyScale2(pc, itemId);
							break;
							/** 82진데스 변신주문서 **/
							case 4107284:
							case 4107285:
							case 4107286:
							case 4107287:
							case 4107288:
								usePolyScale2(pc, itemId);
								break;
								
				
				/** 싸이변신주문서 */
				case 220001:
				case 220002:
				case 220003:
					usePolyScale2(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				/** 싸이변신주문서 */
				case 41154:// 어둠의 비늘
				case 41155:// 열화의 비늘
				case 41156:// 배덕의 비늘
				case 41157:// 증오의 비늘
					usePolyScale(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 42655:
					if (!(pc.getMapId() == 7783 || pc.getMapId() == 12152 || pc.getMapId() == 12149
							|| pc.getMapId() == 3 || pc.getMapId() == 12154 || pc.getMapId() == 12358
							|| pc.getMapId() == 12153 || pc.getMapId() == 12146 || pc.getMapId() == 12147
							|| pc.getMapId() == 12148 || pc.getMapId() == 12258 || pc.getMapId() == 12150
							|| pc.getMapId() == 12257 || pc.getMapId() == 12358)) {
						pc.sendPackets(new S_SystemMessage("클라우디아 맵에서만 사용이 가능합니다."));
						return;
					}
					usePolyPotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 4100290:
					usePolyPotion(pc, itemId);
					break;
				case 4100284:
				case 4100285:
				case 4100286:
				case 4100287:
				case 4100288:
				case 4100289:
				case 41143:// 라버본 헤드 변신물약
				case 41144:// 라버본 솔져 변신물약
				case 41145:// 라버본 나이프 변신물약
				case 30057:// 꼬꼬마(파랑)변신망치
				case 30058:// 꼬꼬마(노랑)변신망치
				case 30059:// 꼬꼬마(분홍)변신망치
				case 8000: // 진데스나이트변신
				case 8001: // 진랜스마스터변신
				case 8002: // 82레벨진데스나이트변신
					usePolyPotion(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 210097:// 샤르나의 변신 주문서 (레벨 30)
				case 210098:// 샤르나의 변신 주문서 (레벨 40)
				case 210099:// 샤르나의 변신 주문서 (레벨 52)
				case 210100:// 샤르나의 변신 주문서 (레벨 55)
				case 210101:// 샤르나의 변신 주문서 (레벨 60)
				case 210102:// 샤르나의 변신 주문서 (레벨 65)
				case 210103:// 샤르나의 변신 주문서 (레벨 70)
				case 210116:// 샤르나의 변신 주문서 (레벨 75)
				case 210117:// 샤르나의 변신 주문서 (레벨 80)
					// case 813:
					useLevelPolyScroll(pc, itemId);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 40317:// 숫돌
				case 30087:
					// 무기나 방어용 기구의 경우만
					if (l1iteminstance1.getItem().getType2() != 0 && l1iteminstance1.get_durability() > 0) {
						String msg0;
						if (l1iteminstance1.getItem().getType2() == 2 && l1iteminstance1.isEquipped()) {
							pc.getAC().addAc(-1);
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.getInventory().recoveryDamage(l1iteminstance1);
						} else {
							pc.getInventory().recoveryDamage(l1iteminstance1);
						}
						msg0 = l1iteminstance1.getLogName();
						if (l1iteminstance1.get_durability() == 0) {
							// %0%s는 신품 같은 상태가 되었습니다.
							pc.sendPackets(new S_ServerMessage(464, msg0));
						} else {
							//%0상태가 좋아졌습니다.
							pc.sendPackets(new S_ServerMessage(463, msg0));
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79));// 아무것도일어나지않았습니다.
						return;
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;

				case 210073:// 하급 오시리스의 보물상자 조각(하)
				case 210077:// 상급 오시리스의 보물상자 조각(하)
				case 500205:// 하급 쿠쿨칸의 보물상자 조각(하)
				case 500207: {// 상급 쿠쿨칸의 보물상자 조각(하)
					int itemId2 = l1iteminstance1.getItem().getItemId();
					if (itemId == 210073 && itemId2 == 210074) {
						if (pc.getInventory().checkItem(210074)) {
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.getInventory().storeItem(210075, 1);
						}
					} else if (itemId == 210077 && itemId2 == 210078) {
						if (pc.getInventory().checkItem(210078)) {
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.getInventory().storeItem(210079, 1);
						}
					}
					if (itemId == 500205 && itemId2 == 500204) {
						if (pc.getInventory().checkItem(500204)) {
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.getInventory().storeItem(500208, 1);
						}
					} else if (itemId == 500207 && itemId2 == 500206) {
						if (pc.getInventory().checkItem(500206)) {
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
							pc.getInventory().storeItem(500209, 1);
						}
					} else {
						// \f1 아무것도 일어나지 않았습니다.
						pc.sendPackets(new S_ServerMessage(79)); 
					}
				}
					break;
				case 40126:// 확인 스크롤
				case 40098: {// 상아탑의 확인 주문서
					int add_mpr = l1iteminstance1.getItem().get_addmpr();
					int add_hpr = l1iteminstance1.getItem().get_addhpr();
					int safe_enchant = l1iteminstance1.getItem().get_safeenchant();
					if (!l1iteminstance1.isIdentified()) {
						l1iteminstance1.setIdentified(true);
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
					}
					pc.sendPackets(new S_IdentifyDesc(l1iteminstance1));
//					pc.sendPackets("정확한 설명을 보시려면 (CTRL+F)누루신후 아이템을 클릭 하세요.");
					pc.getInventory().removeItem(l1iteminstance, 1);
					StringBuffer sb = new StringBuffer();
					if (l1iteminstance1.getItem().getType2() == 1 || l1iteminstance1.getItem().getType2() == 2) {
						if (safe_enchant == -1) {
							sb.append("피틱: " + add_hpr + " /");
							sb.append("엠틱: " + add_mpr + " /");
							sb.append("기본 인첸트: 불가능");
						} else if (safe_enchant == 0) {
							sb.append("피틱: " + add_hpr + " /");
							sb.append("엠틱: " + add_mpr + " /");
							sb.append("기본 인첸트: 0");
						} else {
							sb.append("피틱: " + add_hpr + " /");
							sb.append("엠틱: " + add_mpr + " /");
							sb.append("기본 인첸트: " + safe_enchant + "");
						}
					}
					pc.sendPackets(new S_SystemMessage(sb.toString()));
					sb = null;
				}
					break;
				case 41048:
				case 41049:
				case 41050:
				case 41051:
				case 41052:
				case 41053:
				case 41054:
				case 41055: {
					// 풀먹임 된 항해 일지 페이지：1~8 페이지
					int logbookId = l1iteminstance1.getItem().getItemId();
					if (logbookId == (itemId + 8034)) {
						createNewItem(pc, logbookId + 2, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						// \f1 아무것도 일어나지 않았습니다.
						pc.sendPackets(new S_ServerMessage(79)); 
					}
				}
					break;

				case 41056:
				case 41057: {
					// 풀먹임 된 항해 일지 페이지：9~10 페이지
					int logbookId = l1iteminstance1.getItem().getItemId();
					if (logbookId == (itemId + 8034)) {
						createNewItem(pc, 41058, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						// \f1 아무것도 일어나지 않았습니다.
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
					break;
				case 40931:
				case 40932:
				case 40933:
				case 40934:
				case 40935:
				case 40936:
				case 40937:
				case 40938:
				case 40939:
				case 40940:
				case 40941:
				case 40942:
					// 가공된 보석류(사파이어·루비·에메랄드)
					int earing3Id = l1iteminstance1.getItem().getItemId();
					int earinglevel = 0;
					if (earing3Id >= 41161 && 41172 >= earing3Id) {
						// 신비적인 귀 링류
						if (earing3Id == (itemId + 230)) {
							if ((_random.nextInt(99) + 1) < Config.CREATE_CHANCE_PROCESSING) {
								switch (earing3Id) {
								case 41161:
									earinglevel = 21014;
									break;
								case 41162:
									earinglevel = 21006;
									break;
								case 41163:
									earinglevel = 21007;
									break;
								case 41164:
									earinglevel = 21015;
									break;
								case 41165:
									earinglevel = 21009;
									break;
								case 41166:
									earinglevel = 21008;
									break;
								case 41167:
									earinglevel = 21016;
									break;
								case 41168:
									earinglevel = 21012;
									break;
								case 41169:
									earinglevel = 21010;
									break;
								case 41170:
									earinglevel = 21017;
									break;
								case 41171:
									earinglevel = 21013;
									break;
								case 41172:
									earinglevel = 21011;
									break;
								}
								createNewItem(pc, earinglevel, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
								// \f1%0이 증발하고 있지 않게 되었습니다.
							}
							pc.getInventory().removeItem(l1iteminstance1, 1);
							pc.getInventory().removeItem(l1iteminstance, 1);
						} else {
							// \f1 아무것도 일어나지 않았습니다.
							pc.sendPackets(new S_ServerMessage(79));
						}
					} else {
						// \f1 아무것도 일어나지 않았습니다.
						pc.sendPackets(new S_ServerMessage(79));
					}
					break;

				case 40943:
				case 40944:
				case 40945:
				case 40946:
				case 40947:
				case 40948:
				case 40949:
				case 40950:
				case 40951:
				case 40952:
				case 40953:
				case 40954:
				case 40955:
				case 40956:
				case 40957:
				case 40958:
					// 가공된 다이아몬드(워타·지구·파이어·윈드)
					int ringId = l1iteminstance1.getItem().getItemId();
					int ringlevel = 0;
					int gmas = 0;
					int gmam = 0;
					if (ringId >= 41185 && 41200 >= ringId) {
						// 세공된 링류
						if (itemId == 40943 || itemId == 40947 || itemId == 40951 || itemId == 40955) {
							gmas = 443;
							gmam = 447;
						} else if (itemId == 40944 || itemId == 40948 || itemId == 40952 || itemId == 40956) {
							gmas = 442;
							gmam = 446;
						} else if (itemId == 40945 || itemId == 40949 || itemId == 40953 || itemId == 40957) {
							gmas = 441;
							gmam = 445;
						} else if (itemId == 40946 || itemId == 40950 || itemId == 40954 || itemId == 40958) {
							gmas = 444;
							gmam = 448;
						}
						if (ringId == (itemId + 242)) {
							if ((_random.nextInt(99) + 1) < Config.CREATE_CHANCE_PROCESSING_DIAMOND) {
								switch (ringId) {
								case 41185:
									ringlevel = 20435;
								break;
								case 41186:
									ringlevel = 20436;
								break;
								case 41187:
									ringlevel = 20437;
								break;
								case 41188:
									ringlevel = 20438;
								break;
								case 41189:
									ringlevel = 20439;
								break;
								case 41190:
									ringlevel = 20440;
								break;
								case 41191:
									ringlevel = 20441;
								break;
								case 41192:
									ringlevel = 20442;
								break;
								case 41193:
									ringlevel = 20443;
								break;
								case 41194:
									ringlevel = 20444;
								break;
								case 41195:
									ringlevel = 20445;
								break;
								case 41196:
									ringlevel = 20446;
								break;
								case 41197:
									ringlevel = 20447;
								break;
								case 41198:
									ringlevel = 20448;
								break;
								case 41199:
									ringlevel = 20449;
								break;
								case 41200:
									ringlevel = 20450;
								break;
								}
								pc.sendPackets(new S_ServerMessage(gmas, l1iteminstance1.getName()));
								createNewItem(pc, ringlevel, 1);
								pc.getInventory().removeItem(l1iteminstance1, 1);
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(gmam, l1iteminstance.getName()));
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							// \f1 아무것도일어나지않았습니다.
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					break;

				case 41029:// 소환구 조각
					int dantesId = l1iteminstance1.getItem().getItemId();
					if (dantesId >= 41030 && 41034 >= dantesId) {
						// 소환공의 코어· 각단계
						if ((_random.nextInt(99) + 1) < Config.CREATE_CHANCE_DANTES) {
							createNewItem(pc, dantesId + 1, 1);
						} else {
							pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
							// \f1%0이 증발하고 있지 않게 되었습니다.
						}
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도일어나지않았습니다.
					}
				break;
				case 40090:
				case 40091:
				case 40092:
				case 40093:
				case 40094:// 빈 주문서 (레벨 1)~빈 주문서 (레벨 5)
					if (pc.isWizard()) { // 위저드
						if (itemId == 40090 && blanksc_skillid <= 7 || // 공백
						// 스크롤(Lv1)로 레벨 1 이하의 마법
								itemId == 40091 && blanksc_skillid <= 15 || // 공백
								// 스크롤(Lv2)로 레벨 2 이하의 마법
								itemId == 40092 && blanksc_skillid <= 22 || // 공백
								// 스크롤(Lv3)로 레벨 3 이하의 마법
								itemId == 40093 && blanksc_skillid <= 31 || // 공백
								// 스크롤(Lv4)로 레벨 4 이하의 마법
								itemId == 40094 && blanksc_skillid <= 39) { // 공백
							// 스크롤(Lv5)로 레벨 5 이하의 마법
							L1ItemInstance spellsc = ItemTable.getInstance().createItem(40859 + blanksc_skillid);
							if (spellsc != null) {
								if (pc.getInventory().checkAddItem(spellsc, 1) == L1Inventory.OK) {
									L1Skills l1skills = SkillsTable.getInstance().getTemplate(blanksc_skillid + 1);
									// blanksc_skillid는 0 시작
									if (pc.getCurrentHp() + 1 < l1skills.getHpConsume() + 1) {
										pc.sendPackets(new S_ServerMessage(279));
										// \f1HP가 부족해 마법을 사용할 수 있지 않습니다.
										return;
									}
									if (pc.getCurrentMp() < l1skills.getMpConsume()) {
										pc.sendPackets(new S_ServerMessage(278));
										// \f1MP가 부족해 마법을 사용할 수 있지 않습니다.
										return;
									}
									if (l1skills.getItemConsumeId() != 0) {
										// 재료가 필요
										if (!pc.getInventory().checkItem(l1skills.getItemConsumeId(),
												l1skills.getItemConsumeCount())) {
											// 필요 재료를 체크
											pc.sendPackets(new S_ServerMessage(299));
											// \f1마법을 영창하기 위한 재료가 충분하지 않습니다.
											return;
										}
									}
									pc.setCurrentHp(pc.getCurrentHp() - l1skills.getHpConsume());
									pc.setCurrentMp(pc.getCurrentMp() - l1skills.getMpConsume());
									int lawful = pc.getLawful() + l1skills.getLawful();
									if (lawful > 32767) {
										lawful = 32767;
									}
									if (lawful < -32767) {
										lawful = -32767;
									}
									pc.setLawful(lawful);
									if (l1skills.getItemConsumeId() != 0) {
										// 재료가 필요
										pc.getInventory().consumeItem(l1skills.getItemConsumeId(),
												l1skills.getItemConsumeCount());
									}
									pc.getInventory().removeItem(l1iteminstance, 1);
									pc.getInventory().storeItem(spellsc);
									pc.sendPackets(new S_SystemMessage(spellsc.getName() + "  획득"));
								}
							}
						} else {
							pc.sendPackets(new S_ServerMessage(591));
							// \f1스크롤이 그렇게 강한 마법을 기록하려면 너무나 약합니다.
						}
					} else {
						pc.sendPackets(new S_ServerMessage(264));
						// \f1당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
					}
				break;
				case 40314:// 펫 목걸이
				case 40316:// 하이펫 목걸이
					if (pc.getInventory().checkItem(41160)) {
						// 소환의 피리
						if (withdrawPet(pc, itemObjid)) {
							pc.getInventory().consumeItem(41160, 1);
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					break;

				case 40315:// 펫 호루라기
					pc.sendPackets(new S_Sound(437));
					pc.broadcastPacket(new S_Sound(437));
					Object[] petList = pc.getPetList().values().toArray();
					for (Object petObject : petList) {
						if (petObject instanceof L1PetInstance) { // 펫
							L1PetInstance pet = (L1PetInstance) petObject;
							pet.call();
						}
					}
					break;
				case 40089:// 부활 주문서
				case 140089:// 축복부활 주문서
				case 30074:// 상아탑 축복부활
					if(StadiumManager.getInstance().is_on_stadium(pc.getMapId())){
						pc.sendPackets("사용할 수 없는 지역입니다.");
						return;
					}
					
					if (L1World.getInstance().findObject(resid) instanceof L1Character) {
						L1Character resobject = (L1Character) L1World.getInstance().findObject(resid);
						if (resobject != null) {
							if (resobject instanceof L1PcInstance) {
								L1PcInstance target = (L1PcInstance) resobject;
								if (pc.getId() == target.getId()) {
									return;
								}

								/** 공성장에서는 부활불가능하도록 **/
								int castle_id = L1CastleLocation.getCastleIdByArea(pc);
								if (castle_id != 0) {
									pc.sendPackets(new S_SystemMessage("사용할 수 없는 지역입니다."));
									return;
								}
								/** 공성장에서는 부활불가능하도록 **/
								if (L1World.getInstance().getVisiblePlayer(target, 0).size() > 0) {
									for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(target, 0)) {
										if (!visiblePc.isDead()) {
											// \f1그 자리소에 다른 사람이 서 있으므로 부활시킬 수가
											// 없습니다.
											pc.sendPackets(new S_ServerMessage(592));
											return;
										}
									}
								}
								if (target.getCurrentHp() == 0 && target.isDead() == true) {
									if (pc.getMap().isUseResurrection()) {
										target.setTempID(pc.getId());
										if (itemId == 40089 || itemId == 30074) {
											// 또 부활하고 싶습니까? (Y/N)
											target.sendPackets(new S_Message_YN(321, ""));
										} else if (itemId == 140089) {
											// 또 부활하고 싶습니까? (Y/N)
											target.sendPackets(new S_Message_YN(322, ""));
										}
									} else {
										return;
									}
								}
							} else if (resobject instanceof L1NpcInstance) {
								if (!(resobject instanceof L1TowerInstance)) {
									L1NpcInstance npc = (L1NpcInstance) resobject;
									int npcId = npc.getNpcId();
									if (npcId == 7320052 || npcId == 45021 || npcId == 45022 || npcId == 45040 || npcId == 45048) {
										pc.sendPackets("부활 할 수 없는 몬스터 입니다.");
										return;
									}

									if ((npc instanceof L1PetInstance || npc instanceof MJCompanionInstance) && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
										for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
											if (!visiblePc.isDead()) {
												pc.sendPackets(new S_ServerMessage(592));
												return;
											}
										}
									} else if (npc.getNpcTemplate().isCantResurrect() && !(npc instanceof L1PetInstance || npc instanceof MJCompanionInstance)) {
										pc.getInventory().removeItem(l1iteminstance, 1);
										return;
									}
									if ((npc instanceof L1PetInstance || npc instanceof MJCompanionInstance) && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
										for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
											if (!visiblePc.isDead()) {
												// \f1그 자리소에 다른 사람이 서 있으므로 부활시킬 수가 없습니다.
												pc.sendPackets(new S_ServerMessage(592));
												return;
											}
										}
									}
									if (npc.getCurrentHp() == 0 && npc.isDead()) {
										npc.resurrect(npc.getMaxHp() / 4);
										npc.setResurrect(true);
									}
								}
							}
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}

					break;
				case 40095:// 상아탑의 귀환 주문서
					if ((pc.getLevel() >= 70)) {
						pc.sendPackets("해당 아이템은 69 레벨까지만 사용 가능합니다.");
						return;
					}
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int objid = pc.getId();
						int[] loc = Getback.GetBack_Location(pc, true);
						pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, false);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(647));
					}
					cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
					break;
				case 40079:
				case 40521: // 귀환 스크롤
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int[] loc = Getback.GetBack_Location(pc, false);
						pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, false);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(647));
					}
					cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
					break;
				case 41159:// 픽시의 깃털
				case 41921:{// 픽시의 금빛깃털
					  if (pc.getMapId() >= 1708 && pc.getMapId() == 1712 ||
					  pc.getMapId() == 7783 || pc.getMapId() == 3 ||
					  pc.getMapId() == 13004 || pc.getMapId() == 12152 ||
					  pc.getMapId() == 12149 || pc.getMapId() == 12154 ||
					  pc.getMapId() == 12358 || pc.getMapId() == 12153 ||
					  pc.getMapId() == 12146 || pc.getMapId() == 12147 ||
					  pc.getMapId() == 12148 || pc.getMapId() == 12258 ||
					  pc.getMapId() == 12150 || pc.getMapId() == 12257 ||
					  pc.getMapId() == 12358) { pc.sendPackets(new
					  S_SystemMessage("해당 위치 및 맵에서는 사용이 불가능 합니다.")); 
					  return; 
					  }
					  
					  int count = 1;
					  if(!pc.PC방_버프) {
						  count = 2;
					  }
					if (pc.getMap().isEscapable() || pc.isGm()) {
						int x = 0;
						int y = 0;
						int mapid = 622;
						if (pc.getInventory().consumeItem(l1iteminstance, count)) {

							do {
								x = CommonUtil.random(32765, 32783);
								y = CommonUtil.random(32827, 32837);
								if (L1WorldMap.getInstance().getMap((short) mapid).isPassable(x, y) == false) {
									continue;
								}

								if (L1WorldMap.getInstance().getMap((short) mapid).isSafetyZone(x, y) == false) {
									continue;
								}

							} while (L1WorldMap.getInstance().getMap((short) mapid).getOriginalTile(x, y) == 0
									|| L1WorldMap.getInstance().getMap((short) mapid).getOriginalTile(x, y) == 16
									|| L1WorldMap.getInstance().getMap((short) mapid).getOriginalTile(x, y) == 12
									|| L1WorldMap.getInstance().getMap((short) mapid).getOriginalTile(x, y) == 4
									|| L1World.getInstance().isVisibleObject(x, y, mapid));

							pc.start_teleport(x, y, mapid, pc.getHeading(), 169, true, false);
						} else {
							pc.sendPackets(new S_Paralysis(7, false));
							pc.sendPackets(new S_SystemMessage("깃털이 부족합니다."));
						}
					  } else {
						  pc.sendPackets(new S_Paralysis(7, false));
						  pc.sendPackets(new S_ServerMessage(647));
					  }
					  cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
					}
					  break;
				case 40117:// 은기사 마을 귀환 주문서
				break;
				case 40099:// 상아탑의 순간이동 주문서
					Telbookitem.clickItem(pc, itemId, bookmark_x, bookmark_y, bookmark_mapid, l1iteminstance);
				break;
				case 40100:// 순간이동 주문서
				case 40086:// 매스 텔레포트 주문서
				case 40863:// 마법 주문서 (텔레포트)
				case 140100:// 축복 순간이동 주문서
				case 150100:
					Telbookitem.clickItem(pc, itemId, bookmark_x, bookmark_y, bookmark_mapid, l1iteminstance);
				break;
				/** 오만의탑 이동주문서 **/
				case 830001:
				case 830002:
				case 830003:
				case 830004:
				case 830005:
				case 830006:
				case 830007:
				case 830008:
				case 830009:
				case 830010:
				case 830011:
					omanTel.clickItem(pc, itemId, l1iteminstance);
				break;
				/** 오만의탑 이동주문서 **/

				/** 오만의탑 이동부적, 지배부적 **/
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
					OmanAmulet.clickItem(pc, itemId, l1iteminstance);
					break;
				/** 오만의탑 이동부적, 지배부적 **/

				/** 오만의탑 혼돈부적,변이된 부적 **/
				case 830042:
				case 830043:
				case 830044:
				case 830045:
				case 830046:
				case 830047:
				case 830048:
				case 830049:
				case 830050:
				case 830051:
				case 830052:
				case 830053:
				case 830054:
				case 830055:
				case 830056:
				case 830057:
				case 830058:
				case 830059:
				case 830060:
				case 830061:
					OmanRandomAmulet.clickItem(pc, itemId, l1iteminstance);
					break;
				case 40901:
				case 40902:
				case 40903:
				case 40904:
				case 40905:
				case 40906:
				case 40907:
				case 40908: { // 각종 약혼 반지
					L1PcInstance partner = null;
					boolean partner_stat = false;
					if (pc.getHellTime() > 0) {
						pc.sendPackets(new S_ChatPacket(pc, "지옥에서는 사용할 수 없습니다."));
						return;
					}
					int chargeCount = l1iteminstance.getChargeCount();
					if (pc.getPartnerId() != 0) { // 결혼중
						partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
						if (chargeCount > 0) {
							if (partner != null && partner.getPartnerId() != 0 && pc.getPartnerId() == partner.getId()
									&& partner.getPartnerId() == pc.getId()) {
								partner_stat = true;
								l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
								pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
							}
						}
					} else {
						// \f1당신은 결혼하지 않았습니다.
						pc.sendPackets(new S_ServerMessage(662)); 
						return;
					}

					if (partner_stat) {
						// 몇개의 성에리어
						boolean castle_area = L1CastleLocation.checkInAllWarArea(
								partner.getX(), partner.getY(), partner.getMapId());
						if (castle_area == true || partner.isDead() || partner.getMapId() == 603
								|| partner.getMapId() == 255 || partner.getMapId() == 777 || partner.getMapId() == 778
								|| partner.getMapId() == 39 || partner.getMapId() == 5167 || partner.getMapId() == 5153
								|| partner.getMapId() == 5001 || (partner.getMapId() > 190 && partner.getMapId() < 201)
								|| (partner.getMapId() > 255 && partner.getMapId() < 260) || partner.getMapId() == 23
								|| partner.getMapId() == 5153 || partner.getMapId() == 5001 || partner.getMapId() == 24
								|| (partner.getMapId() > 239 && partner.getMapId() < 244)
								|| (partner.getMapId() > 247 && partner.getMapId() < 252)
								|| (partner.getMapId() > 280 && partner.getMapId() < 289)
								|| (partner.getMapId() > 1 && partner.getMapId() < 2)
								|| (partner.getMapId() > 1700 && partner.getMapId() < 1712)) {
							pc.sendPackets(new S_SystemMessage("당신의 파트너는 죽어있거나 갈 수 없는 곳에 있습니다."));
						}
					} else if (l1iteminstance.getChargeCount() > 0) {
						pc.sendPackets(new S_ServerMessage(546));
						// \f1당신의 파트너는 지금 플레이를 하고 있지 않습니다.
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
				}
					break;

				case 40555:// 비밀의 방의 키
					if (pc.isKnight()
							&& (pc.getX() >= 32806 && // 오림 방
									pc.getX() <= 32814)
							&& (pc.getY() >= 32798 && pc.getY() <= 32807) && pc.getMapId() == 13) {
						pc.start_teleport(32815, 32810, 13, 5, 169, false, false);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
						// \f1 아무것도 일어나지 않았습니다.
					}
					break;

				case 40572:// 어쌔신의 증표
					if (pc.getX() == 32778 && pc.getY() == 32738 && pc.getMapId() == 21) {
						pc.start_teleport(32781, 32728, 21, 5, 169, true, false);
					} else if (pc.getX() == 32781 && pc.getY() == 32728 && pc.getMapId() == 21) {
						pc.start_teleport(32778, 32738, 21, 5, 169, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
					}
					break;
				case 700022:// 기억 확장 구슬
					if (pc.getMark_count() < 100) {
						int booksize = pc.getMark_count() + 10;
						pc.setMark_count(booksize);
						pc.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK_SIZE_PLUS_10, booksize));
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.save();
					} else {
						pc.sendPackets(new S_ServerMessage(2930));
					}
					break;
				case 40420:
				case 40421:
				case 40422:
				case 40423:// 연금술사의돌
					pc.sendPackets(new S_UserCommands5(1));
					if (!pc.getInventory().checkItem(40420, 1)) {
						pc.sendPackets(new S_SystemMessage("고대인의 주술서 1권 이 부족합니다."));
						return;
					} else if (!pc.getInventory().checkItem(40421, 1)) {
						pc.sendPackets(new S_SystemMessage("고대인의 주술서 2권 이 부족합니다."));
						return;

					} else if (!pc.getInventory().checkItem(40422, 1)) {
						pc.sendPackets(new S_SystemMessage("고대인의 주술서 3권 이 부족합니다."));
						return;

					} else if (!pc.getInventory().checkItem(40423, 1)) {
						pc.sendPackets(new S_SystemMessage("고대인의 주술서 4권 이 부족합니다."));
						return;
					} else {
						pc.getInventory().consumeItem(40420, 1);
						pc.getInventory().consumeItem(40421, 1);
						pc.getInventory().consumeItem(40422, 1);
						pc.getInventory().consumeItem(40423, 1);
						pc.getInventory().storeItem(702, 1);
						pc.sendPackets(new S_ChatPacket(pc, "연금술사의 돌 이 제작되었습니다."));
					}
					break;
				case 3000455:
				case 30104: {// 코마의 축복 코인
					int[] allBuffSkill = { 50007 };
					L1SkillUse l1skilluse = new L1SkillUse();
					for (int i = 0; i < allBuffSkill.length; i++) {
						l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_SPELLSC);
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
					break;
				case 3000366:// 신규영입상자
					additemnew.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_SkillSound(pc.getId(), 13561));
					break;
				case 3000508:
					additem2.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 4100080:
				case 7020:
				case 7021:
					additem2.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 4100164:
				case 4100165: // 장비 지원 상자
				case 3000035:// 초보지원상자
					additem.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 60036:
				case 60037:
				case 60038:
				case 60039:
				case 60040:
					additem.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 60041:
				case 60042:
				case 60043:
				case 60044:
				case 60045:
					additem.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 68078:
				case 68079:
				case 68080:
				case 68081:
				case 68082:
					additem.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 68083:
				case 68084:
				case 68085:
				case 68086:
				case 68087:
					additem.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 68088:
				case 68089:
				case 68090:
				case 68091:
				case 68092:
					additem.clickItem(pc, itemId, l1iteminstance);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30144: // LV52 페어리의 축하 선물
					if (pc.getInventory().getSize() > 120) {
						pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
						return;
					}
					if (pc.getInventory().getWeight100() > 82) {
						pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
						return;
					}
					if (pc.getInventory().checkItem(30144, 1)) {
						pc.getInventory().consumeItem(30144, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473));

						createNewItem2(pc, 60032, 1, 0); // 낡은 고서
						createNewItem2(pc, 200000, 1, 0); // 회상의촛불

						if (pc.isCrown()) {
							createNewItem2(pc, 51, 1, 0); // 황금지휘봉
							createNewItem2(pc, 20051, 1, 0); // 군주의위엄
							createNewItem2(pc, 40228, 1, 0); // 콜 클렌
							createNewItem2(pc, 40226, 1, 0); // 트루 타겟
						}
						if (pc.isKnight()) {
							createNewItem2(pc, 56, 1, 0); // 데스 블레이드
							createNewItem2(pc, 20318, 1, 0); // 용기의 벨트
						}
						if (pc.isWizard()) {
							createNewItem2(pc, 20225, 1, 0); // 마나수정구
							createNewItem2(pc, 20055, 1, 0); // 마나망토
							createNewItem2(pc, 40188, 1, 0); // 헤이스트
							createNewItem2(pc, 40170, 1, 0); // 파이어볼
							createNewItem2(pc, 40176, 1, 0); // 메디테이션
						}
						if (pc.isElf()) {
							createNewItem2(pc, 50, 1, 0); // 화염의검
							createNewItem2(pc, 184, 1, 0); // 화염의활
							createNewItem2(pc, 40243, 1, 0); // 서먼 레서 엘리멘탈
							createNewItem2(pc, 40240, 1, 0);// 트리플 애로우
							createNewItem2(pc, 40233, 1, 0); // 바디 투 마인드
							createNewItem2(pc, 40234, 1, 0); // 텔레포트 투 마더
						}
						if (pc.isDarkelf()) {
							createNewItem2(pc, 13, 1, 0); // 핑거오브데스
							createNewItem2(pc, 20195, 1, 0); // 그림자부츠
							createNewItem2(pc, 40276, 1, 0); // 언케니 닷지
							createNewItem2(pc, 40270, 1, 0); // 무빙 엑셀레이션
							createNewItem2(pc, 40268, 1, 0); // 브링 스톤
						}
						if (pc.isDragonknight()) {
							createNewItem2(pc, 500, 1, 0); // 소멸자의체인소드
							createNewItem2(pc, 22001, 1, 0); // 용비늘가더
							createNewItem2(pc, 210025, 1, 0); // 블러드러스트
							createNewItem2(pc, 210026, 1, 0); // 포우 슬레이어
							createNewItem2(pc, 210020, 1, 0); // 드래곤 스킨
							createNewItem2(pc, 210021, 1, 0); // 버닝 슬래쉬
							createNewItem2(pc, 210025, 1, 0); // 블러드러스트
						}
						if (pc.isBlackwizard()) {
							createNewItem2(pc, 503, 1, 0); // 사파이어키링크
							createNewItem2(pc, 22006, 1, 0); // 환술사의마법서
							createNewItem2(pc, 210014, 1, 0); // 큐브(쇼크)
							createNewItem2(pc, 210004, 1, 0); // 큐브(이그니션)
							createNewItem2(pc, 210000, 1, 0); // 미러 이미지
							createNewItem2(pc, 210001, 1, 0); // 컨퓨전
						}
						if (pc.is전사()) {
							createNewItem2(pc, 22365, 1, 0); // 전사단투구
							createNewItem2(pc, 203014, 1, 0); // 대장장이의도끼
							createNewItem2(pc, 210126, 1, 0); // 전사의 인장(크래쉬)
							createNewItem2(pc, 210121, 1, 0); // 전사의 인장(하울)
							createNewItem2(pc, 210128, 1, 0); // 전사의 인장(슬레이어)
						}
					}
					break;
				case 30127: // 52레벨 퀘스트 아이템 상자
					if (pc.getInventory().getSize() > 120) {
						pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
						return;
					}
					if (pc.getInventory().getWeight100() > 82) { // 28 == 100%
						pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
						return;
					}
					if (pc.getInventory().checkItem(30127, 1)) {
						pc.getInventory().consumeItem(30127, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473));

						if (pc.isCrown()) {
							createNewItem2(pc, 51, 1, 0);
						}
						if (pc.isKnight()) {
							createNewItem2(pc, 56, 1, 0);
						}
						if (pc.isWizard()) {
							createNewItem2(pc, 20225, 1, 0);
						}
						if (pc.isElf()) {
							createNewItem2(pc, 184, 1, 0);
						}
						if (pc.isDarkelf()) {
							createNewItem2(pc, 13, 1, 0);
						}
						if (pc.isDragonknight()) {
							createNewItem2(pc, 500, 1, 0);
						}
						if (pc.isBlackwizard()) {
							createNewItem2(pc, 503, 1, 0);
						}
						if (pc.is전사()) {
							createNewItem2(pc, 22365, 1, 0);
						}
					}
					break;
				case 30124:// 포탄
					pc.sendPackets(new S_SystemMessage("공성전 시 투석기를 사용할 때 필요한 소모성 아이템"));
					break;
				case 500218:// 야히봉헌주문서(우호도)
					if (pc.getKarma() >= -10000000) {
						pc.addKarma((int) (-15000 * Config.RATE_KARMA));
						pc.sendPackets(new S_Karma(pc));
						pc.sendPackets(new S_SystemMessage(pc.getName() + "님의 우호도가 향상되었습니다."));
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else
						pc.sendPackets(new S_ServerMessage(79));
					break;

				case 1000010:// 상아셋 상자
					pc.getInventory().removeItem(l1iteminstance, 1);
					Beginner.getInstance().GiveItemToActivePc(pc);
					break;
				case 40008:
				case 140008:
				case 40410:// 단풍막대
					if (pc.getMapId() == 552 || pc.getMapId() == 555 || pc.getMapId() == 557 || pc.getMapId() == 558
							|| pc.getMapId() == 779) {
						// HC4f·배의 묘지 수중에서는 사용불가
						pc.sendPackets(new S_ServerMessage(563));
					} else {
						pc.sendPackets(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand));
						pc.broadcastPacket(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand));
						int chargeCount = l1iteminstance.getChargeCount();
						int spriteId = pc.getCurrentSpriteId();
						if (chargeCount <= 0 && itemId != 40410 || spriteId == 6034 || spriteId == 6035) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
						L1Object target = L1World.getInstance().findObject(spellsc_objid);
						if (spellsc_objid == pc.getId() || (target != null && target instanceof L1Character)) {
							L1Character cha = spellsc_objid == pc.getId() ? pc : (L1Character) target;
							if (cha.getAI() == null)
								polyAction(pc, cha, itemId, s);
							cancelAbsoluteBarrier(pc);
							if (itemId == 40008 || itemId == 140008) {
								l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
								if (l1iteminstance.getChargeCount() == 0) {
									pc.getInventory().removeItem(l1iteminstance);
								} else {
									pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
								}
							} else {
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					}
				break;
				case 40289:
				case 40290:
				case 40291:
				case 40292:
				case 40293:
				case 40294:
				case 40295:
				case 40296:
				case 40297:
					// 오만의 탑 이동 부적11~91
					useToiTeleportAmulet(pc, itemId, l1iteminstance);
					break;
				case 40280:
				case 40281:
				case 40282:
				case 40283:
				case 40284:
				case 40285:
				case 40286:
				case 40287:
				case 40288:
					// 봉인된 오만의 탑 이동 부적 11~91층
					pc.getInventory().removeItem(l1iteminstance, 1);
					L1ItemInstance item3 = pc.getInventory().storeItem(itemId + 9, 1);
					if (item3 != null) {
						pc.sendPackets(new S_ServerMessage(403, item3.getLogName()));
					}
					break;
				case 830032:
				case 830033:
				case 830034:
				case 830035:
				case 830036:
				case 830037:
				case 830038:
				case 830039:
				case 830040:
				case 830041:
					// 봉인된 오만의 탑 이동 부적 1~10층
					pc.getInventory().removeItem(l1iteminstance, 1);
					L1ItemInstance item1 = pc.getInventory().storeItem(itemId - 20, 1);
					if (item1 != null) {
						pc.sendPackets(new S_ServerMessage(403, item1.getLogName()));
					}
					break;
				case 40056:
				case 40057:
				case 40059:
				case 40060:
				case 40061:
				case 40062:
				case 40063:
				case 40064:
				case 40065:
				case 40069:
				case 40072:
				case 40073:
				case 41297:
				case 49092:
				case 41266:
				case 41267:
				case 41274:
				case 41275:
				case 41276:
				case 41252:
				case 49040:
				case 49041:
				case 49042:
				case 49043:
				case 49044:
				case 49045:
				case 49046:
				case 49047:
				case 140061:
				case 140062:
				case 140065:
				case 140069:
				case 140072:
				case 410056:
				case 210039:
				case 30085:
					pc.getInventory().removeItem(l1iteminstance, 1);
					// XXX 음식 마다의 만복도가 차이가 나지 않는다
					if (itemId == 40057) { // 후로팅아이육
						pc.setSkillEffect(STATUS_FLOATING_EYE, 0);
						if (pc.hasSkillEffect(CURSE_BLIND) || pc.hasSkillEffect(DARKNESS)
								|| pc.hasSkillEffect(LINDBIOR_SPIRIT_EFFECT)
								|| pc.hasSkillEffect(L1SkillId.INVISIBILITY)
								|| pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
							pc.sendPackets(new S_CurseBlind(2));
						}
					}
					if (pc.get_food() < 225) { //
						pc.set_food(pc.get_food() + 10);
						if (itemId == 210039 || itemId == 30085) { // 허브
							pc.set_food(pc.get_food() + 90);
						}
						int foodvolume = (l1iteminstance.getItem().getFoodVolume() / 10);
						pc.add_food(foodvolume <= 0 ? 5 : foodvolume);
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						pc.sendPackets(new S_ServerMessage(76, l1iteminstance.getItem().getNameId()));
					}
					break;
				case 40070:// 진화의 열매
					pc.sendPackets(new S_ServerMessage(76, l1iteminstance.getLogName()));
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 408991:
					if (pc.is_combat_field() || !pc.getSafetyZone() || StadiumManager.getInstance().is_on_stadium(pc.getMapId())) {
						pc.sendPackets("해당 지역에서는 사용할 수 없습니다.");
						return;
					}
					if (!pc.getInventory().checkItem(408991))
						return;

					if (MJInstanceSpace.isInInstance(pc)) {
						if (pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE) {
							pc.sendPackets("해당 지역에서는 사용할 수 없습니다.");
							return;
						}
					}

					int locx = 32723 + CommonUtil.random(10);
					int locy = 32851 + CommonUtil.random(10);
					pc.start_teleport(locx, locy, 5166, 5, 169, false, false);
					pc.sendPackets(new S_DisplayEffect(S_DisplayEffect.BLACK_DISPLAY));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, true));
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "캐릭명 변경창으로 이동합니다 잠시만 기다려 주세요..."));
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							pc.getNetConnection().setStatus(MJClientStatus.CLNT_STS_CHANGENAME);
							pc.sendPackets(S_ChangeCharName.getChangedStart());
							int[] loc = null;
							loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
							pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, true);
						}
					}, 2500L);
					break;
				case 408990:
					if (pc.getLevel() <= 84) {
						pc.sendPackets("레벨 85 이하는 불가능 합니다.");
						return;
					}
					pc.sendPackets(new S_SystemMessage(".이름변경 바꿀캐릭명 으로 입력해주세요."));
					break;
				case 41146:// 드로몬드의 초대장
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei001"));
					break;
				case 41209:// 포피레아의 의뢰서
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei002"));
					break;
				case 41210:// 연마재
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei003"));
					break;
				case 41211:// 허브
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei004"));
					break;
				case 41212:// 특제 캔디
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei005"));
					break;
				case 41213:// 티미의 바스켓
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei006"));
					break;
				case 41214:// 운의 증거
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei012"));
					break;
				case 41215:// 지의 증거
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei010"));
					break;
				case 41216:// 력의 증거
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei011"));
					break;
				case 41222:// 마슈르
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei008"));
					break;
				case 41223:// 무기의 파편
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei007"));
					break;
				case 41224:// 배지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei009"));
					break;
				case 41225:// 케스킨의 발주서
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei013"));
					break;
				case 41226:// 파고의 약
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei014"));
					break;
				case 41227:// 알렉스의 소개장
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei033"));
					break;
				case 41228:// 율법박사의 부적
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei034"));
					break;
				case 41229:// 스켈리턴의 머리
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei025"));
					break;
				case 41230:// 지난에의 편지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei020"));
					break;
				case 41231:// 맛티에의 편지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei021"));
					break;
				case 41233:// 케이이에의 편지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei019"));
					break;
				case 41234: // 뼈가 들어온 봉투
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei023"));
					break;
				case 41235:// 재료표
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei024"));
					break;
				case 41236:// 본아챠의 뼈
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei026"));
					break;
				case 41237:// 스켈리턴 스파이크의 뼈
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei027"));
					break;
				case 41239:// 브트에의 편지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei018"));
					break;
				case 41240:// 페다에의 편지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "ei022"));
					break;
				case 41060:// 노나메의 추천서
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "nonames"));
					break;
				case 41061:// 조사단의 증서：에르프 지역 두다마라카메
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kames"));
					break;
				case 41062:// 조사단의 증서：인간 지역 네르가바크모
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bakumos"));
					break;
				case 41063:// 조사단의 증서：정령 지역 두다마라브카
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bukas"));
					break;
				case 41064:// 조사단의 증서：오크 지역 네르가후우모
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "huwoomos"));
					break;
				case 41065:// 조사단의 증서：조사단장 아트바노아
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "noas"));
					break;
				case 41356:// 파룸의 자원 리스트
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rparum3"));
					break;
				case 40701:// 작은 보물의 지도
					if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 1) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "firsttmap"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 2) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapa"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 3) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapb"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 4) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapc"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 5) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapd"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 6) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmape"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 7) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapf"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 8) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapg"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 9) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmaph"));
					} else if (pc.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 10) {
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapi"));
					}
					break;
				case 40663:// 아들의 편지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "sonsletter"));
					break;
				case 40630:// 디에고의 낡은 일기
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "diegodiary"));
					break;
				case 41340:// 용병단장 티온
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tion"));
					break;
				case 41317:// 랄슨의 추천장
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rarson"));
					break;
				case 41318:// 쿠엔의 메모
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kuen"));
					break;
				case 41329:// 박제의 제작 의뢰서
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "anirequest"));
					break;
				case 41346:// 로빈훗드의 메모 1
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll"));
					break;
				case 41347:// 로빈훗드의 메모 2
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinscroll2"));
					break;
				case 41348:// 로빈훗드의 소개장
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "robinhood"));
					break;
				case 41007:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll"));
					break;
				case 41009:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll2"));
					break;
				case 41019:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory1"));
					break;
				case 41020:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory2"));
					break;
				case 41021:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory3"));
					break;
				case 41022:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory4"));
					break;
				case 41023:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory5"));
					break;
				case 41024:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory6"));
					break;
				case 41025:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory7"));
					break;
				case 41026:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory8"));
					break;
				case 210087:
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "first_p"));
					break;
				case 210093:// 실레인의 첫 번째 편지
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "silrein1lt"));
					break;
				case 410106:// 하딘의 일기 11월 10일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s00"));
					break;
				case 410101:// 하딘의 일기:6월 2일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s01"));
					break;
				case 410103:// 하딘의 일기:8월 9일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s02"));
					break;
				case 410105:// 하딘의 일기:10월 12일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s03"));
					break;
				case 410098:// 하딘의 일기:2월 24일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s04"));
					break;
				case 410099:// 하딘의 일기:2월 25일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s05"));
					break;
				case 410100:// 하딘의 일기:5월 5일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s06"));
					break;
				case 410097:// 하딘의 일기:1월 1일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s07"));
					break;
				case 410102:// 하딘의 일기:6월 9일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s08"));
					break;
				case 410104:// 하딘의 일기:8월 19일
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s09"));
					break;
				case 410107:// 어두운 하딘의 일기장
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "j_ep0s10"));
					break;

				case 40615:// 그림자의 신전 2층의 열쇠
					if ((pc.getX() >= 32701 && pc.getX() <= 32705) && (pc.getY() >= 32894 && pc.getY() <= 32898)
							&& pc.getMapId() == 522) { // 그림자의 신전
						pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
								l1iteminstance.getItem().get_mapid(), 5, 169, true, false);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
					}
					break;
				case 42501:// 스톰 워크
					pc.sendPackets(new S_SkillSound(pc.getId(), 12446));// 2235
					pc.start_teleport(spellsc_x, spellsc_y, pc.getMapId(), pc.getHeading(), 169, false, false);
					break;
				case 41293:
				case 41294:
				case 41305:
				case 41306:
				case 4100293:
				case 600229: // 성장의 낚싯대
				case 87058:
				case 87059:
				case 9991: // 낚싯대
					startFishing(pc, itemId, fishX, fishY, l1iteminstance, itemObjid);
					break;
				case 7024: {
					long curtime = System.currentTimeMillis() / 1000;
					if (pc.getQuizTime() + 5 > curtime) {
						long time = (pc.getQuizTime() + 5) - curtime;
						pc.sendPackets(new S_ChatPacket(pc, time + " 초 후 사용 하시길 바랍니다."));
						return;
					}
					int i = 1;
					if (pc.문장주시) {
						i = 3;
						pc.문장주시 = false;
					} else
						pc.문장주시 = true;
					pc.sendPackets(new S_SystemMessage("모든 혈맹의 마크를 표시하거나 종료하였습니다."));
					for (L1Clan clan : L1World.getInstance().getAllClans()) {
						if (clan != null) {
							pc.sendPackets(new S_War(i, pc.getClanname(), clan.getClanName()));
						}
						pc.setQuizTime(curtime);
					}
				}
					break;
				case 200000: { // -- 회상의 촛불
					if (!pc.getMap().isSafetyZone(pc.getLocation())) {
						pc.sendPackets(new S_ChatPacket(pc, "안전한 지역에서만 사용할 수 있습니다."));
						return;
					}
					if (pc.getLevel() != pc.getHighLevel()) {
						pc.sendPackets(new S_SystemMessage("레벨이 다운된 캐릭입니다. 레벨업 후 이용하세요."));
						return;
					}
					if (pc.getLevel() > 54) {
						pc.getInventory().consumeItem(200000, 1);
						int locx2 = 32723 + CommonUtil.random(10);
						int locy2 = 32851 + CommonUtil.random(10);
						pc.start_teleport(locx2, locy2, 5166, 5, 169, true, false);
						pc.set_자동물약사용(false);
					    pc.set_자동버프사용(false);
					    pc.set_자동숫돌사용(false);
						스텟초기화(pc);
					} else {
						pc.sendPackets(new S_SystemMessage("스텟초기화는 55레벨 이상만 가능합니다."));
					}
				}
					break;
				/** 특수 인챈트 시스템 **/
				case 300000: { // 특수 인챈 주문서
					if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0
							|| l1iteminstance1.getItem().getType2() == 2) {
						pc.sendPackets("무기에만 사용할 수 있습니다.");
						return;
					}

					if (!(l1iteminstance1.getItemId() == 12 || l1iteminstance1.getItemId() == 61
							|| l1iteminstance1.getItemId() == 86 || l1iteminstance1.getItemId() == 66
							|| l1iteminstance1.getItemId() == 134
							|| l1iteminstance1.getItemId() >= 202011 && l1iteminstance1.getItemId() <= 202014)) {
						pc.sendPackets("전설급 무기에만 사용하실 수 있습니다.");
						return;
					}

					if (l1iteminstance1.getEnchantLevel() < 5 && l1iteminstance1.get_item_level() == 0) {// 찬스
						pc.sendPackets("\\aG[1단계]는 무기인챈트 +5이상에만 사용이 가능합니다.");
						return;
					} else if (l1iteminstance1.getEnchantLevel() < 7 && l1iteminstance1.get_item_level() == 1) {// 찬스
						pc.sendPackets("\\aG[2단계]는 무기인챈트 +7이상에만 사용이 가능합니다.");
						return;
					} else if (l1iteminstance1.getEnchantLevel() < 8 && l1iteminstance1.get_item_level() == 2) {// 찬스
						pc.sendPackets("\\aG[3단계]는 무기인챈트 +8이상에만 사용이 가능합니다.");
						return;
					} else if (l1iteminstance1.getEnchantLevel() < 9 && l1iteminstance1.get_item_level() == 3) {// 찬스
						pc.sendPackets("\\aG[4단계]는 무기인챈트 +9이상에만 사용이 가능합니다.");
						return;
					} else if (l1iteminstance1.get_item_level() == 4) {
						pc.sendPackets("\\aG4단계 이상 강화를 하실 수 없습니다.");
						return;
					}

					int random = CommonUtil.random(100);
					if (l1iteminstance1.getEnchantLevel() >= 5 && l1iteminstance1.get_item_level() == 0
							&& random < Config.Weapon_Enchant_Per_lvl1) {// 찬스
						if (l1iteminstance1.getItem().getType2() == 1) {
							l1iteminstance1.set_item_level(1);
						}
					} else if (l1iteminstance1.getEnchantLevel() >= 7 && l1iteminstance1.get_item_level() == 1
							&& random < Config.Weapon_Enchant_Per_lvl2) {// 찬스
						if (l1iteminstance1.getItem().getType2() == 1) {
							l1iteminstance1.set_item_level(2);
						}
					} else if (l1iteminstance1.getEnchantLevel() >= 8 && l1iteminstance1.get_item_level() == 2
							&& random < Config.Weapon_Enchant_Per_lvl3) {// 찬스
						if (l1iteminstance1.getItem().getType2() == 1) {
							l1iteminstance1.set_item_level(3);
						}
					} else if (l1iteminstance1.getEnchantLevel() >= 9 && l1iteminstance1.get_item_level() == 3
							&& random < Config.Weapon_Enchant_Per_lvl4) {// 찬스
						if (l1iteminstance1.getItem().getType2() == 1) {
							l1iteminstance1.set_item_level(4);
						}
					} else {
						pc.sendPackets(l1iteminstance1.getLogName() + "\\fH에 마법의 기운이 스며들지 못했습니다.");
						pc.getInventory().removeItem(l1iteminstance, 1);
						return;
					}
					pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
					pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(l1iteminstance1.getLogName() + "\\aL에 마법의 기운이 스며듭니다.");
					pc.save();
				}
					break;
				/** 특수 인챈트 시스템 **/

				/** 특수 마법인형 인챈트 시스템 **/
				case 300001: { // 특수 마법인형 인챈트 주문서
					int random = CommonUtil.random(100);
					switch (l1iteminstance1.getItem().getItemId()) {// 신규 인형 추가시
					case 740:
					case 741:
					case 742:
					case 743:
					case 744:
					case 745:
					case 746:
					case 750:
					case 41248:
					case 41249:
					case 41250:
					case 210071:
					case 210072:
					case 210070:
					case 210086:
					case 210096:
					case 210105:
					case 210106:
					case 210107:
					case 210108:
					case 210109:
					case 500212:
					case 500213:
					case 500214:
					case 500215:
					case 447012:
					case 447013:
					case 447014:
					case 30022:
					case 30023:
					case 30024:
					case 30025:
					case 447015:
					case 447016:
					case 447017:
					case 510216:
					case 510217:
					case 510218:
					case 510219:
					case 510220:
					case 510221:
					case 510222:
					case 410172:
					case 410173:
					case 3000086:
					case 3000087:
					case 3000088:
					case 3000351:
					case 3000352:
					case 755:
					case 756:
					case 757:
					case 42654:
					case 4100007:
					case 4100008:
					case 4100009:
					case 4100010:
					case 758:
					case 4100134:
						if (l1iteminstance1.get_item_level() == 5) {
							pc.sendPackets("\\aG[5단계] 이상 강화를 하실 수 없습니다.");
							return;
						}

						if (l1iteminstance1.get_item_level() == 0 && random < Config.Doll_Enchant_Per_lvl1) {// 찬스
							l1iteminstance1.set_item_level(1);
						} else if (l1iteminstance1.get_item_level() == 1 && random < Config.Doll_Enchant_Per_lvl2) {// 찬스
							l1iteminstance1.set_item_level(2);
						} else if (l1iteminstance1.get_item_level() == 2 && random < Config.Doll_Enchant_Per_lvl3) {// 찬스
							l1iteminstance1.set_item_level(3);
						} else if (l1iteminstance1.get_item_level() == 3 && random < Config.Doll_Enchant_Per_lvl4) {// 찬스
							l1iteminstance1.set_item_level(4);
						} else if (l1iteminstance1.get_item_level() == 4 && random < Config.Doll_Enchant_Per_lvl5) {// 찬스
							l1iteminstance1.set_item_level(5);
						} else {
							pc.sendPackets(l1iteminstance1.getLogName() + "\\aG에 마법의 기운이 스며들지 못했습니다.");
							pc.getInventory().removeItem(l1iteminstance, 1);
							return;
						}
						pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
						pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
						pc.getInventory().removeItem(l1iteminstance, 1);
						pc.sendPackets(l1iteminstance1.getLogName() + "\\aL에 마법의 기운이 스며듭니다.");
						pc.save();
						break;
					}
				}
					break;
				/** 특수 마법인형 인챈트 시스템 **/
				case 3000049: // 구호의증서
					pc.sendPackets(new S_SystemMessage("아덴 성당에서 경험치를 무료로 복구할 수 있습니다."));
					break;
				case 3000124: { // -- 서먼 몬스터 주문서
					if (!pc.getMap().isRecallPets() || pc.isInWarArea()) {
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}
					int pet_size = pc.getPetList().values().size();
					if (pet_size >= 1) {
						pc.sendPackets(new S_SystemMessage("더 이상 소환이 불가능 합니다."));
						return;
					}

					if (pet_size == 0) {
						pet_size = 1;
					}

					int[] summon_id = { 7320072, 7320073, 7320074, 7320075, 7320076, 7320077, 7320078, 7320079, 7320080,
							7320081, 7320000 };
					int random_id = CommonUtil.random(3);
					int pet_cost = 0;

					Iterator<L1NpcInstance> iter = pc.getPetList().values().iterator();
					L1NpcInstance npc = null;

					while (iter.hasNext()) {
						npc = iter.next();
						if (npc == null) {
							continue;
						}
						pet_cost = npc.getPetcost();
					}

					L1Npc npc_temp = NpcTable.getInstance().getTemplate(summon_id[random_id]);

					if (npc_temp == null) {
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}

					L1SummonInstance summon = null;

					for (int i = 0; i < pet_size; i++) {
						summon = new L1SummonInstance(npc_temp, pc);
						summon.setPetcost(pet_cost);
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
					break;
				case 210118: {// 신규보호혈맹
					if (pc.getLevel() >= Config.신규혈맹보호레벨) {
						pc.sendPackets(new S_SystemMessage(Config.신규혈맹보호레벨 + "레벨 이상은 신규혈맹에 가입할 수 없습니다."));
						return;
					}

					if (pc.getClanid() != 0) {
						pc.sendPackets(new S_SystemMessage("당신은 이미 혈맹에 가입하였습니다."));
						break;
					}
//					String[] clan_id = { "스타", "신화", "전투" };// 해당몬스터
					String[] clan_id = { "신규보호" };
					int random_id = CommonUtil.random(1);

					L1Clan clan = L1World.getInstance().findClan(clan_id[random_id]);
					L1ClanJoin.getInstance().tutorialJoin(clan, pc);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, false, false);
					break;
				}
				case 7643:// 축복의 정수
					if (pc.getInventory().consumeItem(itemId, 1)) {
						if (pc.getClan() != null)
							pc.getClan().addBlessCount(10000000);
						pc.sendPackets(new S_SystemMessage("축복의 기운 1000 추가되었습니다."));
					}
					break;
				case 400253: // 1억아덴통장
					if (pc.getInventory().checkItem(40308, 100000000)) {
						pc.getInventory().consumeItem(40308, 100000000);
						pc.getInventory().storeItem(400254, 1);
						pc.sendPackets(new S_SystemMessage("1억 아데나 → 1억 수표로 변환 완료"));
					} else {
						pc.sendPackets(new S_SystemMessage("1억 아데나가 부족합니다."));
					}
					break;
				case 3000121:// 신비한무기상자
					if (pc.getInventory().checkItem(3000121, 1)) { // 체크 되는 아이템과
						// 수량
						pc.getInventory().consumeItem(3000121, 1); // 삭제되는 아이템과
						// 수량
						Random random = new Random();
						L1ItemInstance item = null;
						int[] itemrnd = { 12, 1136, 293, 134, 61, 203017, 202003, 203007 };// 랜덤아이템
						// 번호
						int[] enchantrnd = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1,
								1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2 }; // 랜덤인챈수치
						int ran1 = random.nextInt(itemrnd.length);
						int ran2 = random.nextInt(enchantrnd.length);
						item = pc.getInventory().storeItem(itemrnd[ran1], 1);
						item.setEnchantLevel(enchantrnd[ran2]);
					}
					pc.sendPackets(new S_SystemMessage("아이템을 [획득] 하였습니다."));
					break;
				case 3000190: { // 아덴랜덤상자
					// -- 랜덤 값의 횟수
					// -- 랜덤 값
					int[] itemid = { 40308 };
					int random500 = CommonUtil.random(100);

					if (random500 <= 60) { // -- 2000만 에서 4000만.
						createNewItem(pc, itemid[0], CommonUtil.random(42000000, 52000000));
					} else {// -- 5만 에서 500만.
						createNewItem(pc, itemid[0], CommonUtil.random(45000000, 55000000));
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
					break;
				case 810006:
				case 810007: {
					if (!(pc.getMapId() >= 1936 && pc.getMapId() <= 2035)) {
						pc.sendPackets(new S_SystemMessage("중앙 사원에서만 사용이 가능합니다."));
						return;
					}
					if (delay_id != 0) { // 지연 설정 있어
						if (pc.hasItemDelay(delay_id) == true) {
							return;
						}
					}
					int chargeCount = l1iteminstance.getChargeCount();

					if (chargeCount <= 0) {
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}

					if (pc.isInvisble()) {
						pc.sendPackets(new S_ServerMessage(1003));
						return;
					}

					int gfx = 0;
					int dmg = 0;
					int range = 0;
					if (itemId == 810006) {
						gfx = 1819;
						dmg = 150;
						range = 3;
					} else {
						gfx = 3934;
						dmg = 500;
						range = 22;
					}
					L1MonsterInstance mon = null;
					for (L1Object object : L1World.getInstance().getVisibleObjects(pc, range)) {
						if (object == null) {
							continue;
						}
						if (!(object instanceof L1Character)) {
							continue;
						}
						if (object.getId() == pc.getId()) {
							continue;
						}

						if (object instanceof L1MonsterInstance) {
							mon = (L1MonsterInstance) object;
							if (mon.getNpcId() != 7200003) {
								Broadcaster.broadcastPacket(mon,
										new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage));
								mon.receiveDamage(pc, (int) dmg);
							}
						}
					}
					pc.sendPackets(new S_UseAttackSkill(pc, 0, gfx, pc.getX(), pc.getY(), 18));
					Broadcaster.broadcastPacket(pc, new S_UseAttackSkill(pc, 0, gfx, pc.getX(), pc.getY(), 18));
					l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);

					if (chargeCount <= 1) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					L1ItemDelay.onItemUse(pc, l1iteminstance);
				}
					break;
				case 410140: { // 폴의 쾌속 릴
					int item = l1iteminstance1.getItem().getItemId();
					if (item == 41293) {
						createNewItem(pc, 41294, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					if (item == 41294) {
						if (l1iteminstance1.getChargeCount() >= 500) {
							pc.sendPackets(new S_ServerMessage(3457));
							// 더 이상 쾌속 릴을 사용할 수 없습니다.
							return;
						} else if (l1iteminstance1.getChargeCount() > 4900) {
							l1iteminstance1.setChargeCount(5000);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 50);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
					break;
				case 410141: { // 은빛 릴
					int item = l1iteminstance1.getItem().getItemId();
					if (item == 41293) {
						createNewItem(pc, 41305, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					if (item == 41305) {
						if (l1iteminstance1.getChargeCount() >= 5000) {
							pc.sendPackets(new S_ServerMessage(3457));
							// 더 이상 쾌속 릴을 사용할 수 없습니다.
							return;
						} else if (l1iteminstance1.getChargeCount() > 4950) {
							l1iteminstance1.setChargeCount(5000);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 50);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
					break;
				case 410142: { // 금빛 릴
					int item = l1iteminstance1.getItem().getItemId();
					if (item == 41293) {
						createNewItem(pc, 41306, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					if (item == 41306) {
						if (l1iteminstance1.getChargeCount() >= 5000) {
							pc.sendPackets(new S_ServerMessage(3457));
							// 더 이상 쾌속 릴을 사용할 수 없습니다.
							return;
						} else if (l1iteminstance1.getChargeCount() > 4950) {
							l1iteminstance1.setChargeCount(5000);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 50);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
					break;
				case 87056: { // 고대의 은빛 릴 300회
					int item = l1iteminstance1.getItem().getItemId();
					if (item == 41293) {
						createNewItem(pc, 87058, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					if (item == 87058) {
						if (l1iteminstance1.getChargeCount() >= 4000) {// 횟수
																		// 본섭1000
							pc.sendPackets(new S_ServerMessage(3457)); // 더 이상쾌속
																		// 릴을사용할
																		// 수
																		// 없습니다.
							return;
						} else if (l1iteminstance1.getChargeCount() > 3900) {
							l1iteminstance1.setChargeCount(4000);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 300);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
					break;
				case 87057: { // 고대의 금빛 릴 300회
					int item = l1iteminstance1.getItem().getItemId();
					if (item == 41293) {
						createNewItem(pc, 87059, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					if (item == 87059) {
						if (l1iteminstance1.getChargeCount() >= 4000) {// 횟수
																		// 본섭1000
							pc.sendPackets(new S_ServerMessage(3457)); // 더 이상쾌속
																		// 릴을사용할
																		// 수
																		// 없습니다.
							return;
						} else if (l1iteminstance1.getChargeCount() > 3900) {
							l1iteminstance1.setChargeCount(4000);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 300);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
					break;
				case 600228: { // 성장의 릴
					int item = l1iteminstance1.getItem().getItemId();
					if (item == 41293) {
						createNewItem(pc, 600229, 1);	// 성장의 낚싯대
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					if (item == 600229) {	// 성장의 낚싯대
						if (l1iteminstance1.getChargeCount() >= 10000) {// 횟수
																		// 본섭1000
							pc.sendPackets(new S_ServerMessage(3457)); // 더 이상쾌속
																		// 릴을사용할
																		// 수
																		// 없습니다.
							return;
						} else if (l1iteminstance1.getChargeCount() > 9900) {
							l1iteminstance1.setChargeCount(10000);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 100);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
					break;
				case 4100292: { // 고급 성장의 릴 500회
					int item = l1iteminstance1.getItem().getItemId();
					if (item == 41293) {
						createNewItem(pc, 4100293, 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
					if (item == 4100293) {// 고급 성장의 낚싯대
						if (l1iteminstance1.getChargeCount() >= 10000) {// 횟수 본섭1000
							pc.sendPackets(new S_ServerMessage(3457)); // 더 이상쾌속 릴을사용할 수 없습니다.
							return;
						} else if (l1iteminstance1.getChargeCount() > 9900) {
							l1iteminstance1.setChargeCount(10000);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						} else {
							l1iteminstance1.setChargeCount(l1iteminstance1.getChargeCount() + 100);
							pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_CHARGE_COUNT);
						}
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				}
					break;
				case 700081:
					군주광역스턴(pc);
					break;
				case 123123:// 운영자셋트
					if (pc.getInventory().checkItem(123123, 1)) {
						pc.getInventory().consumeItem(123123, 1);
						createGMItem(pc, 40088, 100);
						createGMItem(pc, 3000204, 1);
						createGMItem(pc, 700078, 1);
						createGMItem(pc, 700079, 1);
						createGMItem(pc, 5563, 1);
						createGMItem(pc, 5564, 1);
						createGMItem(pc, 5565, 1);
						createGMItem(pc, 5566, 1);
						createGMItem(pc, 5567, 1);
						createGMItem(pc, 5568, 1);
						createGMItem(pc, 46162, 1);
						createGMItem(pc, 46160, 1);
						createGMItem(pc, 42501, 1);
						createGMItem(pc, 410014, 1);
						createGMItem(pc, 410015, 1);
						createGMItem(pc, 900010, 1);
						createGMItem(pc, 50020, 500);
						createGMItem(pc, 50021, 500);
						createGMItem(pc, 40126, 1000);
						createGMItem(pc, 140100, 100);
						createGMItem(pc, 410063, 100);
						createGMItem(pc, 40308, 100000000);
						createGMItem(pc, 4100254, 1);
						createGMItem(pc, 4100255, 1);
						createGMItem(pc, 4100256, 1);
						createGMItem(pc, 4100257, 1);
						createGMItem(pc, 4100258, 1);
						createGMItem(pc, 4100259, 1);
						createGMItem(pc, 4100260, 1);
					}
					break;
				case 3000063:// 크로노스의벨트
					if (pc.getInventory().checkItem(3000063, 1)) {
						pc.getInventory().consumeItem(3000063, 1);
						createNewItem(pc, 900007, 1);
					}
					break;
				case 700078:// 메티스의가호
					int objid = pc.getId();
					pc.sendPackets(new S_SkillSound(objid, 4856)); // 3944
					Broadcaster.broadcastPacket(pc, new S_SkillSound(objid, 4856));
					for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer(pc)) {
						if (tg.getCurrentHp() == 0 && tg.isDead()) {
							tg.sendPackets(new S_SystemMessage("GM이 부활을 해주었습니다. "));
							Broadcaster.broadcastPacket(tg, new S_SkillSound(tg.getId(), 3944));
							tg.sendPackets(new S_SkillSound(tg.getId(), 3944));
							// 축복된 부활 스크롤과 같은 효과
							tg.setTempID(objid);
							tg.sendPackets(new S_Message_YN(322, "")); // 또 부활하고
							// 싶습니까?
							// (Y/N)
						} else {
							// tg.sendPackets(new S_SystemMessage("GM이 HP,MP를
							// 회복해주었습니다."));
							Broadcaster.broadcastPacket(tg, new S_SkillSound(tg.getId(), 832));
							tg.sendPackets(new S_SkillSound(tg.getId(), 832));
							tg.setCurrentHp(tg.getMaxHp());
							tg.setCurrentMp(tg.getMaxMp());
						}
					}
					break;
				case 700079:
					for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 17)) {
						if (obj instanceof L1MonsterInstance) { // 몬스터라면
							L1NpcInstance npc = (L1NpcInstance) obj;
							npc.receiveDamage(pc, 50000); // 대미지
						}
					}
					break;
					
				case 7010: { // 축복주문서 100% 확률
				    // 무기에만 축복을 부여할 수 있음을 확인
				    if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0 || l1iteminstance1.getItem().getType2() == 2) {
				        pc.sendPackets(new S_SystemMessage("무기에만 축복을 부여할 수 있습니다."));
				        return;
				    }
				    int random = CommonUtil.random(100);
				    // 축복 성공 확률 확인
				    if (random < Config.Bless_Chance) {
				        l1iteminstance1.setBless(0); // 아이템을 축복 상태로 설정
				        pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
				        pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);

				        // 무기 타입에 따른 추가 타격치 부여
				        if (l1iteminstance1.getItem().getType2() == 1) {
				            int type = l1iteminstance1.getItem().getType();
				            if (type == 7 || type == 16 || type == 17) {
				                l1iteminstance1.set_bless_level(random <= 95 ? CommonUtil.random(1, 4) : 5); // 추타 랜덤 1~5
				            } else {
				                l1iteminstance1.set_bless_level(random <= 95 ? CommonUtil.random(1, 4) : 5); // 추타 랜덤 1~5
				            }
				            pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
				            pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
				        }

				        pc.getInventory().removeItem(l1iteminstance, 1); // 사용한 아이템 제거
				        pc.sendPackets(new S_SkillSound(pc.getId(), 9268));
				        pc.sendPackets(new S_SystemMessage("축복: 추타 1 ~ 5 적용되었습니다."));
				        pc.sendPackets(new S_SystemMessage(l1iteminstance1.getLogName() + "에 축복의 기운이 스며듭니다."));
				        pc.save();
				    } else {
				        pc.sendPackets(new S_SystemMessage("축복의 기운이 스며들지 못하였습니다."));
				        pc.getInventory().removeItem(l1iteminstance, 1);
				    }
				}
				    break;
				case 7011: {    // 축복 부여 주문서(무기) (확률 100%)
				    // 무기에만 축복을 부여할 수 있음을 확인
				    if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() == 0 || l1iteminstance1.getItem().getType2() == 2) {
				        pc.sendPackets(new S_SystemMessage("무기에만 축복을 부여할 수 있습니다."));
				        return;
				    }
				    int random = CommonUtil.random(100);
				    l1iteminstance1.setBless(0); // 아이템을 축복 상태로 설정
				    pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS);
				    pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS);

				    // 무기 타입에 따른 추가 타격치 부여
				    if (l1iteminstance1.getItem().getType2() == 1) {
				        int type = l1iteminstance1.getItem().getType();
				        if (type == 7 || type == 16 || type == 17) {
				            l1iteminstance1.set_bless_level(random <= 70 ? CommonUtil.random(1, 4) : 5); // 추타 랜덤 1~5
				        } else {
				            l1iteminstance1.set_bless_level(random <= 70 ? CommonUtil.random(1, 4) : 5); // 추타 랜덤 1~5
				        }
				        pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
				        pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_BLESS_LEVEL);
				    }

				    pc.getInventory().removeItem(l1iteminstance, 1); // 사용한 아이템 제거
				    pc.sendPackets(new S_SkillSound(pc.getId(), 9268)); // 이펙트 발생
				    pc.sendPackets(new S_SystemMessage("축복: 추타 1 ~ 5 적용되었습니다."));
				    pc.sendPackets(new S_SystemMessage(l1iteminstance1.getLogName() + "에 축복의 기운이 스며듭니다."));
				    pc.save();
				}
				    break;				
				
				case 65648: {// 흑사의 코인
					int[] allBuffSkill = { 4914 };
					L1SkillUse l1skilluse = new L1SkillUse();
					if (pc.hasSkillEffect(L1SkillId.God_buff))
						pc.removeSkillEffect(L1SkillId.God_buff);
					for (int i = 0; i < allBuffSkill.length; i++) {
						l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
					break;
				case 3000210:
					long curtime1 = System.currentTimeMillis() / 1000;
					if (pc.getQuizTime() + 1 > curtime1) {
						long sec = (pc.getQuizTime() + 1) - curtime1;
						pc.sendPackets(new S_ChatPacket(pc, sec + "초 후에 가능합니다."));
						return;
					}
					pc.sendPackets(new S_SystemMessage("당신의 MP가 회복 되었습니다."));
					pc.setCurrentMp(pc.getCurrentMp() + (300));
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));// 이펙트발생
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 400254: // 1억 아데나 수표
					int 수표 = pc.getInventory().countItems(400254);
					int 아데나수량 = pc.getInventory().countItems(40308);

					if (아데나수량 >= 1900000000) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "20억 아데나 초과 시 거래 불가 합니다."));
						return;
					}
					if (수표 >= 1) {
						pc.getInventory().storeItem(40308, 100000000);
						pc.sendPackets("1억 수표 → 1억 아데나로 변환 완료");
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "20억 아데나 초과 시 거래 불가 합니다."));
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets("수표가 부족합니다.");
					}
					break;
				case 41245: // 용해제
					useResolvent(pc, l1iteminstance1, l1iteminstance);
					break;
				case 700076:
					if ((pc.getX() >= 33311 && pc.getX() <= 33351) && (pc.getY() >= 32432 && pc.getY() <= 32472)
							&& pc.getMapId() == 15430) {
						pc.getInventory().removeItem(l1iteminstance, 1);
						L1SpawnUtil.spawn(pc, 45529, 0, 60 * 20000);
						L1World.getInstance()
								.broadcastPacketToAll(new S_ChatPacket(pc, "용의 계곡에서 누군가가 '거대 드레이크'를 소환하였습니다"));
						L1World.getInstance().broadcastPacketToAll(
								new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "용의 계곡에서 누군가가 '거대 드레이크' 를 소환하였습니다."));
					} else {
						pc.sendPackets(new S_SystemMessage("용의 계곡 입구에서 사용하면 '거대 드레이크'가 등장합니다."));
					}
					break;
				case 700077:
					if ((pc.getX() >= 33311 && pc.getX() <= 33351) && (pc.getY() >= 32432 && pc.getY() <= 32472)
							&& pc.getMapId() == 15430) {
						pc.getInventory().removeItem(l1iteminstance, 1);
						L1SpawnUtil.spawn(pc, 7000093, 0, 60 * 20000);
						L1World.getInstance()
								.broadcastPacketToAll(new S_ChatPacket(pc, "용의 계곡에서 누군가가 '제로스'를 소환하였습니다."));
						L1World.getInstance().broadcastPacketToAll(
								new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "용의 계곡에서 누군가가 '제로스'를 소환하였습니다."));
					} else {
						pc.sendPackets(new S_SystemMessage("용의 계곡 입구에서 사용하면 '제로스'가 등장합니다."));
					}
					break;
					
// 본섭화가 아니라서 주석처리했음.
//				case 41303: // 큰 은빛 베리아나
//					int 랜덤 = _random.nextInt(120) + 1;
//					pc.getInventory().storeItem(40308, 500000);
//					pc.sendPackets(new S_SystemMessage("아데나 (500,000)을 얻었습니다."));
//					if (랜덤 >= 1 && 랜덤 <= 12) {
//						pc.getInventory().storeItem(20315, 1);
//						pc.sendPackets(new S_SystemMessage("영양 만점 허리끈 을 얻었습니다."));
//					} else if (랜덤 >= 13 && 랜덤 <= 24) {
//						pc.getInventory().storeItem(20262, 1);
//						pc.sendPackets(new S_SystemMessage("영양 만점 목걸이 을 얻었습니다."));
//					} else if (랜덤 >= 25 && 랜덤 <= 36) {
//						pc.getInventory().storeItem(20291, 1);
//						pc.sendPackets(new S_SystemMessage("영양 만점 반지 을 얻었습니다."));
//					} else if (랜덤 >= 37 && 랜덤 <= 48) {
//						pc.getInventory().storeItem(40087, 1);
//						pc.sendPackets(new S_SystemMessage("무기 마법 주문서 을 얻었습니다."));
//					} else if (랜덤 >= 49 && 랜덤 <= 59) {
//						pc.getInventory().storeItem(40074, 1);
//						pc.sendPackets(new S_SystemMessage("갑옷 마법 주문서 을 얻었습니다."));
//					} else if (랜덤 >= 60 && 랜덤 <= 65) {
//						pc.getInventory().storeItem(41248, 1);
//						pc.sendPackets(new S_SystemMessage("마법인형(버그베어) 을 얻었습니다."));
//					} else if (랜덤 >= 66 && 랜덤 <= 71) {
//						pc.getInventory().storeItem(210096, 1);
//						pc.sendPackets(new S_SystemMessage("마법인형(에티) 을 얻었습니다."));
//					} else if (랜덤 >= 72 && 랜덤 <= 74) {
//						pc.getInventory().storeItem(210105, 1);
//						pc.sendPackets(new S_SystemMessage("마법인형(코카트리스) 을 얻었습니다."));
//					} else if (랜덤 >= 75 && 랜덤 <= 77) {
//						pc.getInventory().storeItem(20422, 1);
//						pc.sendPackets(new S_SystemMessage("빛나는 고대 목걸이 을 얻었습니다."));
//					} else if (랜덤 >= 78 && 랜덤 <= 79) {
//						pc.getInventory().storeItem(22000, 1);
//						pc.sendPackets(new S_SystemMessage("고대명궁가더 을 얻었습니다."));
//					} else if (랜덤 >= 80 && 랜덤 <= 81) {
//						pc.getInventory().storeItem(22003, 1);
//						pc.sendPackets(new S_SystemMessage("고대투사가더 을 얻었습니다."));
//					} else if (랜덤 >= 82 && 랜덤 <= 86) {
//						pc.getInventory().storeItem(30127, 1);
//						pc.sendPackets(new S_SystemMessage("52레벨 퀘스트 아이템 상자를 얻었습니다."));
//					} else {
//						pc.sendPackets(new S_SystemMessage("아이템을 획득하지 못하였습니다."));
//					}
//					pc.getInventory().removeItem(l1iteminstance, 1);
//					break;
					
					
// 본섭화가 아니라서 주석처리했음.
//				case 41304: // 큰 금빛 베리아나
//					int 랜덤1 = _random.nextInt(170) + 1;
//					pc.getInventory().storeItem(40308, 5000000);
//					pc.sendPackets(new S_SystemMessage("아데나 (5,000,000)을 얻었습니다."));
//					if (랜덤1 >= 1 && 랜덤1 <= 25) {
//						pc.getInventory().storeItem(41249, 1);
//						pc.sendPackets(new S_SystemMessage("마법인형(서큐버스) 을 얻었습니다."));
//					} else if (랜덤1 >= 26 && 랜덤1 <= 51) {
//						pc.getInventory().storeItem(41250, 1);
//						pc.sendPackets(new S_SystemMessage("마법인형(늑대인간) 을 얻었습니다."));
//					} else if (랜덤1 >= 52 && 랜덤1 <= 77) {
//						pc.getInventory().storeItem(210070, 1);
//						pc.sendPackets(new S_SystemMessage("마법인형(돌 골렘) 을 얻었습니다."));
//					} else if (랜덤1 >= 78 && 랜덤1 <= 88) {
//						pc.getInventory().storeItem(40038, 1);
//						pc.sendPackets(new S_SystemMessage("엘릭서(CHA) 을 얻었습니다."));
//					} else if (랜덤1 >= 89 && 랜덤1 <= 99) {
//						pc.getInventory().storeItem(140087, 1);
//						pc.sendPackets(new S_SystemMessage("축복받은 무기 마법 주문서 을 얻었습니다."));
//					} else if (랜덤1 >= 100 && 랜덤1 <= 110) {
//						pc.getInventory().storeItem(140074, 1);
//						pc.sendPackets(new S_SystemMessage("축복받은 갑옷 마법 주문서 을 얻었습니다."));
//					} else if (랜덤1 >= 111 && 랜덤1 <= 112) {
//						pc.getInventory().storeItem(202002, 1);
//						pc.sendPackets(new S_SystemMessage("붉은 기사의 대검 을 얻었습니다."));
//					} else if (랜덤1 >= 113 && 랜덤1 <= 114) {
//						pc.getInventory().storeItem(504, 1);
//						pc.sendPackets(new S_SystemMessage("흑요석 키링크 을 얻었습니다."));
//					} else if (랜덤1 >= 115 && 랜덤1 <= 116) {
//						pc.getInventory().storeItem(205, 1);
//						pc.sendPackets(new S_SystemMessage("달의 장궁 을 얻었습니다."));
//					} else if (랜덤1 >= 117 && 랜덤1 <= 118) {
//						pc.getInventory().storeItem(20165, 1);
//						pc.sendPackets(new S_SystemMessage("데몬의 장갑 을 얻었습니다."));
//					} else if (랜덤1 >= 119 && 랜덤1 <= 120) {
//						pc.getInventory().storeItem(20197, 1);
//						pc.sendPackets(new S_SystemMessage("데몬의 부츠 을 얻었습니다."));
//					} else if (랜덤1 >= 121 && 랜덤1 <= 122) {
//						pc.getInventory().storeItem(20160, 1);
//						pc.sendPackets(new S_SystemMessage("흑장로의 로브 을 얻었습니다."));
//					} else if (랜덤1 >= 123 && 랜덤1 <= 124) {
//						pc.getInventory().storeItem(20218, 1);
//						pc.sendPackets(new S_SystemMessage("흑장로의 샌달 을 얻었습니다."));
//					} else if (랜덤1 >= 125 && 랜덤1 <= 126) {
//						pc.getInventory().storeItem(20298, 1);
//						pc.sendPackets(new S_SystemMessage("제니스의 반지 을 얻었습니다."));
//					} else if (랜덤1 >= 127 && 랜덤1 <= 131) {
//						pc.getInventory().storeItem(30127, 1);
//						pc.sendPackets(new S_SystemMessage("52레벨 퀘스트 아이템 상자를 얻었습니다."));
//					} else {
//						pc.sendPackets(new S_SystemMessage("아이템을 획득하지 못하였습니다."));
//					}
//					pc.getInventory().removeItem(l1iteminstance, 1);
//					break;

				case 500035: // 자기 혈맹가입주문서
					if (pc.getInventory().checkItem(500035, 1)) {
						pc.getInventory().consumeItem(500035, 1);
						if (pc.isCrown()) { // 군주라면
							if (pc.get_sex() == 0) { // 왕자라면
								pc.sendPackets(new S_ServerMessage(87));
								// 당신은 왕자입니다
							} else {
								pc.sendPackets(new S_ServerMessage(88));
								// 당신은공주입니다
							}
							return;
						}
						if (pc.getClanid() != 0) { // 혈맹이 있다면
							pc.sendPackets(new S_ServerMessage(89));
							// 이미혈맹이있습니다
							return;
						}
						Connection con = null;
						con = L1DatabaseFactory.getInstance().getConnection();
						Statement pstm2 = con.createStatement();
						ResultSet rs2 = pstm2.executeQuery(
								"SELECT `account_name`, `char_name`, `ClanID`, `Clanname` FROM `characters` WHERE Type = 0");
						while (rs2.next()) {
							if (pc.getNetConnection().getAccountName()
									.equalsIgnoreCase(rs2.getString("account_name"))) {
								if (rs2.getInt("ClanID") != 0) { // 군주의 혈맹이 있다면
									L1Clan clan = L1World.getInstance().findClan(rs2.getString("Clanname"));
									// 군주의 혈맹으로 가입
									L1PcInstance clanMember[] = clan.getOnlineClanMember();
									for (int cnt = 0; cnt < clanMember.length; cnt++) {
										// 접속한 혈맹원에게 메세지 뿌리고
										clanMember[cnt].sendPackets(new S_ServerMessage(94, pc.getName()));
										// \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
									}
									pc.setClanid(rs2.getInt("ClanID"));
									pc.setClanname(rs2.getString("Clanname"));
									pc.setClanRank(L1Clan.수련);
									pc.save(); // DB에 캐릭터 정보를 기입한다
									clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), "", pc.getId(),
											pc.getType(), pc.getOnlineStatus(), pc);
									pc.setClanMemberNotes("");
									pc.sendPackets(new S_ClanName(pc, clan.getEmblemId(), pc.getClanRank()));
									pc.sendPackets(new S_ReturnedStat(pc.getId(), clan.getClanId()));
									pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS,
											pc.getClan().getEmblemStatus())); // TODO
									// pc.sendPackets(new S_ClanAttention());
									for (L1PcInstance player : clan.getOnlineClanMember()) {
										player.sendPackets(new S_ReturnedStat(pc.getId(), pc.getClan().getEmblemId()));
										player.broadcastPacket(
												new S_ReturnedStat(player.getId(), pc.getClan().getEmblemId()));
									}
									pc.sendPackets(new S_ServerMessage(95, rs2.getString("Clanname")));
									// L1Teleport.teleport(pc, pc.getX(),
									// pc.getY(), pc.getMapId(),
									// pc.getHeading(), false);
									pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, false,
											false);
									pc.getInventory().removeItem(l1iteminstance, 1);
									break;
								}
							}
						}
						rs2.first(); // 쿼리를 처음으로 되돌리고
						SQLUtil.close(rs2, pstm2, con);
						if (pc.getClanid() == 0) { // 혈맹이 있다면
							pc.sendPackets(new S_SystemMessage("계정내에 군주가 없거나 혈맹이 창설되지 않았습니다."));
						}
					}
					break;
				case 400246:
					if (pc.getLevel() >= 50) {
						pc.sendPackets(new S_Message_YN(C_Attr.MSGCODE_622_KDINIT, 622, "당신의 킬/데스를 초기화 하시겠습니까?"));
						pc.setKillDeathInitializeItem(l1iteminstance);
					} else
						pc.sendPackets("레벨 50 이하는 사용이 불가능 합니다.");
					break;
				case 410009:
					if (pc.getLevel() >= 50) {
						pc.sendPackets(new S_Message_YN(C_Attr.MSGCODE_622_Name, 622, "당신의 성별을 전환 하시겠습니까?"));
						pc.setNameInstance(l1iteminstance);
					} else
						pc.sendPackets("레벨 50 이하는 사용이 불가능 합니다.");
					break;
				case 87052:
					if (pc.getInventory().checkItem(87052, 1)) {
						pc.getInventory().consumeItem(87052, 1);
						pc.getInventory().storeItem(87054, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 8473));
						pc.sendPackets(new S_SystemMessage("호랑이 사육장을 얻었습니다."));
					}
					break;
				case 87053:
					if (pc.getInventory().checkItem(87053, 1)) {
						pc.getInventory().consumeItem(87053, 1);
						pc.getInventory().storeItem(87055, 1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 8473));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 8473));
						pc.sendPackets(new S_SystemMessage("진돗개 바구니를 얻었습니다."));
					}
					break;
				case 87054: // 호랑이 사육장
					pc.getInventory().removeItem(l1iteminstance, 1);
					L1SpawnUtil.spawn(pc, 45313, 0, 120000); // 78161 펫id
					break;
				case 87055: // 진돗개 바구니
					pc.getInventory().removeItem(l1iteminstance, 1);
					L1SpawnUtil.spawn(pc, 45711, 0, 120000); // 78161 펫id
					break;
				case 560025:
				case 560027:
				case 560028:
				case 560029:
				case 4100135:
					TelBook.clickItem(pc, itemId, BookTel, l1iteminstance);
					break;
				/** 인형관련 **/
				case 41248:
				case 41249:
				case 41250:
				case 210070:
				case 210071:
				case 210072:
				case 210086:
				case 210096:
				case 210105:
				case 210106:
				case 210107:
				case 210108:
				case 210109:
				case 500212:
				case 500213:
				case 500214:
				case 500215:
				case 447012:
				case 447013:
				case 447014:
				case 30022:
				case 30023:
				case 30024:
				case 30025:
				case 447015:
				case 447016:
				case 447017:
				case 510216:
				case 510217:
				case 510218: // 눈사람 인형 추가
				case 510219:
				case 510220:
				case 510221:
				case 510222:// 신규인형4종
				case 410172:
				case 410173:// 데스나이트, 인어, 킹버그베어
				case 4100007:
				case 4100008:
				case 4100009:
				case 4100010:
				case 740:
				case 741:
				case 742:
				case 743:
				case 744:
				case 745:
				case 746:// 데스나이트인형진
				case 750:
				case 3000086:// 마법인형 : 아이리스
				case 3000087:// 마법인형 : 뱀파이어
				case 3000088:// 마법인형 : 바란카
				case 3000351:// 마법인형 : 머미로드
				case 3000352:// 마법인형 : 타락
				case 42654: // 수련자 마법인형
				case 755:
				case 756:
				case 757:
				case 758:
				case 759:
				case 760:
				case 761:
				case 772:
				case 773:
				case 774:
				case 775:
				case 4100134:
				case 950000://축복받은 마법인형 : 서큐버스 퀸
				case 950001://축복받은 마법인형 : 흑장로
				case 950002://축복받은 마법인형 : 자이언트
				case 950003://축복받은 마법인형 : 드레이크
				case 950004://축복받은 마법인형 : 킹 버그베어
				case 950005://축복받은 마법인형 : 다이아몬드 골렘
				case 950006://축복받은 마법인형 : 사이클롭스
				case 950007://축복받은 마법인형 : 리치
				case 950008://축복받은 마법인형 : 나이트발드
				case 950009://축복받은 마법인형 : 시어
				case 950010://축복받은 마법인형 : 아이리스
				case 950011://축복받은 마법인형 : 뱀파이어
				case 950012://축복받은 마법인형 : 머미로드
				case 950013://축복받은 마법인형 : 데몬
				case 950014://축복받은 마법인형 : 데스나이트
				case 950015://축복받은 마법인형 : 바란카
				case 950016://축복받은 마법인형 : 타락
				case 950017://축복받은 마법인형 : 바포메트
				case 950018://축복받은 마법인형 : 얼음여왕
				case 950019://축복받은 마법인형 : 커츠
				case 141400://할파스
				case 141401://아우라키아
				case 141402://베히모스
				case 141403://기르타스
				case 141404://알비노 데몬
				case 141405://알비노 피닉스
				case 141406://알비노 유니콘
				case 141407://유니콘
				case 141408://그림 리퍼
				case 141409://다크스타 조우
				case 141410://디바인 크루세이더
				case 141411://군터
				case 141412://오우거 킹
				case 141413://다크 하딘
				case 141415://드래곤 슬레이어
				case 141416://피닉스
				case 141417://암흑 대장로
					/** 특수 마법인형 인챈트 시스템 **/
/*					if (l1iteminstance.get_item_level() != 0) {
						L1DollInstance.setDollLevel(l1iteminstance.get_item_level());
					} else {
						L1DollInstance.setDollLevel(0);
					}
					/** 특수 마법인형 인챈트 시스템 **/
					// 각종 마법인형들
//					useMagicDoll(pc, itemId, itemObjid, l1iteminstance);
					useMagicDoll(pc, l1iteminstance);
					break;
				case 2100952:// 쫄다엘
					useSupport4(pc, itemId, itemObjid);
					break;
				case 2100953:// 쫄요정
					useSupport3(pc, itemId, itemObjid);
					break;
				case 2100950:// 쫄기사
					useSupport1(pc, itemId, itemObjid);
					break;
				case 2100951:// 쫄군주
					useSupport2(pc, itemId, itemObjid);
					break;
				case 210095:// 쫄법사
					useSupport(pc, itemId, itemObjid);
					break;
				case 41401:// 가구 제거 막대
					useFurnitureRemovalWand(pc, spellsc_objid, l1iteminstance);
					break;
				case 410014:// 창고 소환 막대
					useNpcSpownWand(pc, 60001, l1iteminstance);
					break;
				case 5568:
					MJBotLoadManager.delBotItem(pc, spellsc_objid);
					break;
				case 4100254:
					use_bug_down_wand(pc, spellsc_objid, l1iteminstance);
					break;
				case 46160:// npc제거막대
					useFieldObjectRemovalWand(pc, spellsc_objid, l1iteminstance);
					break;
				case 46162:// npc확인막대
					useFieldObjectRemovalWand1(pc, spellsc_objid, l1iteminstance);
					break;
				case 410015:// 상점 소환 막대
					useNpcSpownWand(pc, 7320002, l1iteminstance);
					break;
				case 41345:// 산성의 유액
					if (pc.getZoneType() == 1) {
						pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
						return;
					}
					L1DamagePoison.doInfection(pc, pc, 3000, 5, false);
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 41315:// 성수
					if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
						pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
						return;
					}
					if (pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
						pc.removeSkillEffect(STATUS_HOLY_MITHRIL_POWDER);
					}
					pc.setSkillEffect(STATUS_HOLY_WATER, 900 * 1000);
					pc.sendPackets(new S_SkillSound(pc.getId(), 190));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
					pc.sendPackets(new S_ServerMessage(1141));
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30055:// 얼던화염의 막대
					if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2151
							|| pc.getMapId() >= 2151 && pc.getMapId() <= 2201
							|| pc.getMapId() >= 12152 && pc.getMapId() <= 12200)) {
						pc.sendPackets(new S_SystemMessage("특정지역에서만 사용이 가능합니다."));
						return;
					}
					if (pc.isInvisble()) {
						return;
					}
					cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
					L1Object target = L1World.getInstance().findObject(spellsc_objid);
					if (target != null) {
						for (L1Object object : L1World.getInstance().getVisiblePoint(target.getLocation(), 4)) {
							if (object instanceof L1MonsterInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (!npc.isDead() && npc.getId() != target.getId()) {
									npc.setStatus(ActionCodes.ACTION_Damage);
									Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
								}
								npc.receiveDamage(pc, 250);
							} else if (object instanceof L1DoorInstance || object instanceof L1TowerInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								npc.receiveDamage(pc, 250);
							}
						}
						pc.sendPackets(new S_UseAttackSkill(pc, target.getId(), 762, target.getX(), target.getY(), 18));
						Broadcaster.broadcastPacket(pc,
								new S_UseAttackSkill(pc, target.getId(), 762, target.getX(), target.getY(), 18));
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 3000425:// 봄바 다이너마이트
					if (!(pc.getMapId() >= 1911 && pc.getMapId() <= 1912 || pc.getMapId() == 623)) {
						pc.sendPackets(new S_SystemMessage("이벤트맵에서만 사용가능 합니다."));
						return;
					}
					if (pc.getLevel() > 99) {
						pc.sendPackets(new S_SystemMessage("레벨 99까지만 사용가능 합니다."));
						return;
					}
					if (pc.isInvisble()) {
						return;
					}
					cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
					L1Object target1 = L1World.getInstance().findObject(spellsc_objid);
					if (target1 != null) {
						for (L1Object object : L1World.getInstance().getVisiblePoint(target1.getLocation(), 4)) {
							if (object instanceof L1MonsterInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (!npc.isDead() && npc.getId() != target1.getId()) {
									npc.setStatus(ActionCodes.ACTION_Damage);
									Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
								}
								npc.receiveDamage(pc, 250);
							}
						}
						pc.sendPackets(
								new S_UseAttackSkill(pc, target1.getId(), 5789, target1.getX(), target1.getY(), 18));
						Broadcaster.broadcastPacket(pc,
								new S_UseAttackSkill(pc, target1.getId(), 5789, target1.getX(), target1.getY(), 18));
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 711:
					if (!(pc.getMapId() >= 1911 && pc.getMapId() <= 1912 || pc.getMapId() == 623)) {
						pc.sendPackets(new S_SystemMessage("이벤트맵에서만 사용가능 합니다."));
						return;
					}
					if (pc.getLevel() > 99) {
						pc.sendPackets(new S_SystemMessage("레벨 99까지만 사용가능 합니다."));
						return;
					}
					if (pc.isInvisble()) {
						return;
					}
					cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
					L1Object target2 = L1World.getInstance().findObject(spellsc_objid);
					if (target2 != null) {
						for (L1Object object : L1World.getInstance().getVisiblePoint(target2.getLocation(), 4)) {
							if (object instanceof L1MonsterInstance) {
								L1NpcInstance npc = (L1NpcInstance) object;
								if (!npc.isDead() && npc.getId() != target2.getId()) {
									npc.setStatus(ActionCodes.ACTION_Damage);
									Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), 2));
								}
								npc.receiveDamage(pc, 300);
							}
						}
						pc.sendPackets(
								new S_UseAttackSkill(pc, target2.getId(), 16060, target2.getX(), target2.getY(), 18));
						Broadcaster.broadcastPacket(pc,
								new S_UseAttackSkill(pc, target2.getId(), 16060, target2.getX(), target2.getY(), 18));
					}
					pc.getInventory().removeItem(l1iteminstance, 1);
					break;
				case 30071:// 아덴왕국 장비 지급함
					pc.getInventory().removeItem(l1iteminstance, 1);
					int[] Weapon = null;
					int[] Armor = null;
					int[] ArmorEnchant = null;
					int[] Accessory = null;
					int[] AccessoryEnchant = null;
					int MagicDoll = 0;
					int[] SpellBook = null;
					if (pc.isCrown()) {
						Weapon = new int[] { 1113, 169 }; // 숨겨진 마족의 검, 사냥꾼 활
						// 마투, 힘티, 호갑, 마망, 파글, 강부, 신방
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20194, 20234 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 9 };
						// 푸귀, 빛고목, 순백마방, 순백체반, 이반, 오벨
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 500214; // 스파토이
						SpellBook = new int[] { 40226, 40227, 40228, 40229, 40230, 40231 };
					} else if (pc.isKnight()) {
						Weapon = new int[] { 9, 62, 180 }; // 오리하루콘 단검, 무관의 양손검
						// , 크로스보우
						// 마투, 힘티, 호갑, 마망, 파글, 강부, 요방, 고투사
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20194, 20236, 22003 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 9, 0 };
						// 푸귀, 빛고목, 순백마방, 순백체반, 이반, 오벨
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 500214; // 스파토이
						SpellBook = new int[] { 40164, 40165 };
					} else if (pc.isElf()) {
						Weapon = new int[] { 9, 508 }; // 오리하루콘 단검, 테베 오시리스의 활
						// 마투, 민티, 호갑, 마망, 파글, 강부, 명궁
						Armor = new int[] { 20011, 21029, 21060, 20056, 20187, 20194, 22000 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 0 };
						// 푸귀, 빛고목, 순백마방, 순백체반, 이반, 오벨
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 210105; // 코카트리스
						SpellBook = new int[] { 40232, 40233, 40234, 40235, 40236, 40237, 40238, 40239, 40240, 40241,
								40242, 40244, 40170, 40171, 40172, 40173, 40174, 40175, 40176, 40177, 40178, 40179,
								40180, 40181, 40182, 40183, 40184, 40185, 40186, 40187, 40188, 40189, 40190, 40191,
								40192, 40193 };
					} else if (pc.isWizard()) {
						Weapon = new int[] { 509, 169 }; // 테베 오시리스의 지팡이, 사냥꾼 활
						// 마투, 쿠마, 마티, 고롭, 마망, 마토, 파글, 강부, 신마, 마나수정구, 마법사가더
						Armor = new int[] { 20011, 22192, 21031, 20093, 20056, 20055, 20187, 20194, 20233, 20225,
								22255 };
						ArmorEnchant = new int[] { 8, 8, 8, 0, 8, 7, 8, 8, 8, 5, 0 };
						// 푸귀, 블목, 순백마방, 순백집중, 이반, 오벨
						Accessory = new int[] { 22231, 20257, 22228, 22228, 22225, 22225, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 210071; // 장로
						SpellBook = new int[] { 40170, 40171, 40172, 40173, 40174, 40175, 40176, 40177, 40178, 40179,
								40180, 40181, 40182, 40183, 40184, 40185, 40186, 40187, 40188, 40189, 40190, 40191,
								40192, 40193, 40197, 40224, 40213 };
					} else if (pc.isDarkelf()) {
						Weapon = new int[] { 507, 180 }; // 테베 오리시의 이도류, 크로스보우
						// 마투, 힘티, 호갑, 마망, 파글, 그부, 요방, 고투사
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20195, 20236, 22003 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 9, 0 };
						// 푸귀, 빛고목, 순백마방, 순백체반, 이반, 오벨
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 500214; // 스파토이
						SpellBook = new int[] { 40265, 40266, 40267, 40269, 40271, 40272, 40273, 40274, 40275, 40278,
								40279 };
					} else if (pc.isDragonknight()) {
						Weapon = new int[] { 62, 501, 615 }; // 무관의 양손검, 파멸자의
						// 체인소드. 쿠쿨칸의
						// 건틀렛
						// 마투, 힘티, 호갑, 마망, 파글, 강부, 요방, 고투사
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20194, 20236, 22003 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 9, 0 };
						// 푸귀, 빛고목, 순백마방, 순백체반, 이반, 오벨
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 500214; // 스파토이
						SpellBook = new int[] { 210021, 210022, 210023, 210024, 210026, 210027, 210028, 210029, 210030,
								210031, 210032, 210033, 210034 };
					} else if (pc.isBlackwizard()) {
						Weapon = new int[] { 509, 504 }; // 테베 오시리스의 지팡이, 흑요석
						// 키링크, 테베 오시리스의 활
						// 마투, 힘티, 호갑, 마망, 파글, 강부, 신마, 고투사
						Armor = new int[] { 20011, 21028, 21060, 20056, 20187, 20194, 20233, 22003 };
						ArmorEnchant = new int[] { 8, 8, 8, 8, 8, 8, 8, 0 };
						// 푸귀, 빛고목, 순백마방, 순백체반, 이반, 오벨
						Accessory = new int[] { 22230, 20422, 22228, 22228, 22226, 22226, 20288, 20317 };
						AccessoryEnchant = new int[] { 5, 0, 5, 5, 5, 5, 0, 0 };
						MagicDoll = 500214; // 스파토이
						SpellBook = new int[] { 210000, 210001, 210002, 210003, 210005, 210006, 210007, 210008, 210009,
								210010, 210011, 210012, 210013, 210015, 210016, 210017, 210018, 210019 };
					}
					for (int i = 0; i < Weapon.length; i++) { // 무기
						createNewItemTrade(pc, Weapon[i], 1, 9, 129, 3, true);
					}
					for (int i = 0; i < Armor.length; i++) { // 방어구
						createNewItemTrade(pc, Armor[i], 1, ArmorEnchant[i], 129, 0, true);
					}
					for (int i = 0; i < Accessory.length; i++) { // 악세사리
						createNewItemTrade(pc, Accessory[i], 1, AccessoryEnchant[i], 129, 0, true);
					}
					for (int i = 0; i < SpellBook.length; i++) { // 법서
						createNewItemTrade(pc, SpellBook[i], 1, 0, 1, 0, false);
					}
					createNewItemTrade(pc, MagicDoll, 1, 0, 129, 0, false);
					createNewItemTrade(pc, 30072, 200, 0, 129, 0, false); // 단테스의
					// 유물
					// 주머니
					createNewItemTrade(pc, 40308, 2000000, 0, 1, 0, false); // 아데나
					break;
				case 700024:// 희미한 기억의 구슬
					L1BookMark.Bookmarkitem(pc, l1iteminstance, itemId, false);
					break;
				case 3000065: // 영생의 빛
				case 3000256: // 생명의 나뭇잎
				case 3110509: // 변신권 강화석
				case 3000509: // 축복받은 생명의 나뭇잎
				case 500716: // 전설 무기 소생서
				case 3010255: // 소생 주문서
				case 500723: //	치킨 소생 주문서
					MJItemEnchanterLoader.getInstance().do_enchant(pc, l1iteminstance, l1iteminstance1);
					break;
				case 5558:
					정상의가호(pc, l1iteminstance);
					break;
				case 30107:
				case 30108:
				case 30109:
				case 30110:
				case 30111:
				case 30112:
				case 30113:
				case 30114:
				case 30115: {
					// 순백의 티 인장
					int targetItem = l1iteminstance1.getItemId();
					int[] item = new int[] { 30107, 30108, 30109, 30110, 30111, 30112, 30113, 30114, 30115 };
					int[] t = new int[] { 22349, 22350, 22351, 22352, 22353, 22354, 22355, 22356, 22357 };
					int[] elf_t = new int[] { 22340, 22341, 22342, 22343, 22344, 22345, 22346, 22347, 22348 };
					if (targetItem == 20084) { // 요정족 티셔츠
						for (int i = 0; i < item.length; i++) {
							if (l1iteminstance.getItemId() == item[i]) {
								createNewItem2(pc, elf_t[i], 1, l1iteminstance1.getEnchantLevel());
								pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(),
										l1iteminstance1.getEnchantLevel());
								pc.getInventory().removeItem(l1iteminstance, 1);
								break;
							}
						}
					} else if (targetItem == 20085) { // 티셔츠
						for (int i = 0; i < item.length; i++) {
							if (l1iteminstance.getItemId() == item[i]) {
								createNewItem2(pc, t[i], 1, l1iteminstance1.getEnchantLevel());
								pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(),
										l1iteminstance1.getEnchantLevel());
								pc.getInventory().removeItem(l1iteminstance, 1);
								break;
							}
						}
					} else {
						pc.sendPackets(new S_ServerMessage(79));
					}
				}
					break;
				case 30116:// 천연 비누:순백의 티
					int targetItem = l1iteminstance1.getItemId();
					if (targetItem >= 22340 && targetItem <= 22348) { // 순백의 요정족
						// 티
						createNewItem2(pc, 20084, 1, l1iteminstance1.getEnchantLevel());
						pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(), l1iteminstance1.getEnchantLevel());
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else if (targetItem >= 22349 && targetItem <= 22357) { // 순백의
						// 티
						createNewItem2(pc, 20085, 1, l1iteminstance1.getEnchantLevel());
						pc.getInventory().DeleteEnchant(l1iteminstance1.getItemId(), l1iteminstance1.getEnchantLevel());
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(79));
					}
					break;
					
				case 202220:{//장비 교환권
					if (l1iteminstance1.getBless() >= 128) { 
						pc.sendPackets(new S_SystemMessage("봉인된 템은 교환할수없습니다."));
						return;
					}
					if(l1iteminstance1.isEquipped()){
						pc.sendPackets(new S_SystemMessage("장착중인 템은 교환할수없습니다."));
						return;
					}
					if(l1iteminstance1.getEndTime() != null){
						pc.sendPackets(new S_SystemMessage("교환 할수 없습니다."));
						return;
					}
					L1Item item = l1iteminstance1.getItem();
					int enchant = l1iteminstance1.getEnchantLevel();
							
					List<L1ItemInstance> TradeItemlist = L1TradeItemBox.GetItemInstanceList(l1iteminstance1.getItemId(), enchant);       
					
					if(TradeItemlist != null){
						if(TradeItemlist.size() > 0){								
							pc.setTradeItem(l1iteminstance1);
							pc.sendPackets(new S_TradeItemShop(pc.getId(), TradeItemlist));
						}else {
							pc.sendPackets(new S_SystemMessage("교환할 수 있는 아이템이 없습니다."));
							return;
						}
					}else {
						pc.sendPackets(new S_SystemMessage("교환할 수 있는 아이템이 없습니다."));
						return;
					}
					break;
				}
				/**************************
				 * 무기강화인첸트 관련 주문서
				 ***********************************************/
				/** 속성변환주문서 **/
				case 560030:
				case 560031:
				case 560032:
				case 560033:
					/** 속성변환주문서 **/
				case 40130:
				case 140130:
				case L1ItemId.SCROLL_OF_ENCHANT_WEAPON:
				case L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON:
				case L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON: // 환상의 무기마법주문서
				case L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON:
				case L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON:
				case L1ItemId.IVORYTOWER_WEAPON_SCROLL:
				case L1ItemId.ENCHANT_WEAPONA:
				case 210085:
				case 210064:
				case 724: // 영웅무기 주문서
				case 210065:
				case 210066:
				case 210067:
				case 810003:
				case 4100148: // 사신의 무기 마법 주문서
				case 4100136: // 사신의 숨결
				case 127000:
				case 68076: // 고대의 서 무기
				case 30146:
				case 30068: {
     				if (l1iteminstance1 == null || l1iteminstance1.isEquipped()) {
						pc.sendPackets(new S_ServerMessage(4302));// 착용중인 아이템에는
						return;
					}
					if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 1) {
						pc.sendPackets(new S_ServerMessage(79)); // 아무것도 일어나지
						return;
					}

					int safe_enchant = l1iteminstance1.getItem().get_safeenchant();
					if (safe_enchant < 0) { // 강화 불가
						pc.sendPackets(new S_ServerMessage(79)); // 아무것도 일어나지
						return;
					}
					int weaponId = l1iteminstance1.getItem().getItemId();
					if (weaponId >= 246 && weaponId <= 249) { // 강화 불가
						if (itemId == L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON) {// 시련의
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1
							return;
						}
					}
					if (itemId == L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON) {
						// 시련의 스크롤
						if (weaponId >= 246 && weaponId <= 249) { // 강화 불가
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					/** 아놀드 무기 마법 주문서 **/
					if (weaponId >= 307 && weaponId <= 314) {
						if (itemId == 30146) {
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					if (itemId == 30146) {
						if (weaponId >= 307 && weaponId <= 314) {
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					/** 환상의 무기 마법 주문서 **/
					if (weaponId >= 413000 && weaponId <= 413007) { // 이외에 강화 불가
						if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON) {// 환상의무기마법주문서
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON) {// 환상의무기마법주문서
						if (weaponId >= 413000 && weaponId <= 413007) { // 이외에
							// 강화 불가
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					// 용사의 무기 마법 주문서
					if (weaponId >= 1126 && weaponId <= 1133) { // 이외에 강화 불가
						if (itemId == 30068) {
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					if (itemId == 30068) {
						if (weaponId >= 1126 && weaponId <= 1133) { // 이외에 강화 불가
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					
					/** 사신의 숨결 **/
//					if ((weaponId >= 294 && weaponId <= 294)	|| (weaponId >= 2944 && weaponId <= 2944)
//					 || (weaponId >= 2945 && weaponId <= 2945)	|| (weaponId >= 217 && weaponId <= 217)
//					 || (weaponId >= 61 && weaponId <= 61)		|| (weaponId >= 12 && weaponId <= 12)
//					 || (weaponId >= 134 && weaponId <= 134)	|| (weaponId >= 86 && weaponId <= 86)
//					 || (weaponId >= 220011 && weaponId <= 220014)) {
//					
//						if (itemId == 4100136 || itemId == 210064 || itemId == 210065 || itemId == 210066
//						 || itemId == 210067 || itemId == 560030 || itemId == 560031 || itemId == 560032
//						 || itemId == 560033 || itemId == 4100148) {

/*					if ((weaponId == 294)	||	// 사신의 검
						(weaponId == 2944)	||	// 아인하사드의 섬광
						(weaponId == 2945)	||	// 그랑카인의 심판
						(weaponId == 217)	||	// 기르타스의 검
						(weaponId == 61)	||	// 진명황의 집행검
						(weaponId == 12)	||	// 바람칼날의 단검
						(weaponId == 134)	||	// 수정결정체 지팡이
						(weaponId == 86)	||	// 붉은그림자의 이도류
						(weaponId >= 220011 && weaponId <= 220014)) {
									
					if (itemId == 4100136	||	// 사신의 숨결
						itemId == 210064	||	// 풍령의 무기 강화 주문서
						itemId == 210065	||	// 지령의 무기 강화 주문서
						itemId == 210066	||	// 수령의 무기 강화 주문서
						itemId == 210067	||	// 화령의 무기 강화 주문서
						itemId == 560030	||	// 화령의 속성 변환 주문서
						itemId == 560031	||	// 수령의 속성 변환 주문서
						itemId == 560032	||	// 풍령의 속성 변환 주문서
						itemId == 560033	||	// 지령의 속성 변환 주문서
						itemId == 4100148) {	// 사신의 무기 마법 주문서
						 // 이외에 강화 불가
						} else {
							pc.sendPackets("사신의 숨결, 사신의 무기 마법 주문서로만 강화할 수 있습니다.");
							pc.sendPackets(79);
							return;
						}
					}*/
					
					/** 창천 무기 마법 주문서 **/
					if (itemId == 210085) {
						if ((weaponId >= 231 && weaponId <= 240) || (weaponId >= 510 && weaponId <= 539)) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if ((weaponId >= 231 && weaponId <= 240) || (weaponId >= 510 && weaponId <= 539)) {
						if (itemId == 210085) {// 창천무기마법주문서
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
							return;
						}
					}
					
					// 영웅 무기류
					switch (itemId) {
					case 140087:	// 축데이
					case 40087:		// 걍데이
					case 240087:	// 저데이
					case 68076:		// 고대인의 고서:무기
						if (weaponId == 315 || weaponId == 316 || weaponId == 317 || weaponId == 318 || weaponId == 319
								|| weaponId == 320 || weaponId == 1104 || weaponId == 7000136 || weaponId == 7000213) {
							pc.sendPackets("영웅의 무기 마법 주문서로만 강화할 수 있습니다.");
							pc.sendPackets(79);
							return;
						}
					}
					
					// 집행급 무기류
/*					switch (itemId) {
					case 140087:	// 축데이
					case 40087:		// 걍데이
					case 240087:	// 저데이
					case 68076:		// 고대인의 고서:무기
					case 724:		// 영웅의 무기 마법 주문서
					case 810003:	// 장인의 무기 마법 주문서
						if (weaponId == 12 || weaponId == 61 || weaponId == 86 || weaponId == 134 || weaponId == 202014
							|| weaponId == 202011 || weaponId == 202013 || weaponId == 202012 || weaponId == 66) {
							pc.sendPackets("이벨빈, 사신의 숨결, 사신의 무기 마법 주문서로만 강화할 수 있습니다.");
							pc.sendPackets(79);
							return;
						}
					}*/
					
					/** 사신의검, 아인하사드의섬광, 그랑카인의 심판, 기르타스의검 **/
/*					switch (itemId) {
					case 140087:	// 축데이
					case 40087:		// 걍데이
					case 240087:	// 저데이
					case 68076:		// 고대인의 고서:무기
					case 724:		// 영웅의 무기 마법 주문서
					case 810003:	// 장인의 무기 마법 주문서
						if (weaponId == 294 || weaponId == 2944 || weaponId == 2945 || weaponId == 217 || weaponId == 12 || weaponId == 61 || weaponId == 86 || weaponId == 134 || weaponId == 202014
								|| weaponId == 202011 || weaponId == 202013 || weaponId == 202012 || weaponId == 66) {
							pc.sendPackets("이벨빈, 사신의 숨결, 사신의 무기 마법 주문서로만 강화할 수 있습니다.");
							pc.sendPackets("사신의 숨결, 사신의 무기 마법 주문서로만 강화할 수 있습니다.");
							pc.sendPackets(79);
							return;
						}
					}*/
					
					
					// 상아탑의 무기 마법 주문서
					if (itemId == L1ItemId.IVORYTOWER_WEAPON_SCROLL) {
						if (weaponId == 7 || weaponId == 35 || weaponId == 48 || weaponId == 73 || weaponId == 105
								|| weaponId == 120 || weaponId == 147 || weaponId == 156 || weaponId == 174
								|| weaponId == 9000 || weaponId == 9001 || weaponId == 9002 || weaponId == 9003
								|| weaponId == 175 || weaponId == 224 || weaponId == 203012 || weaponId == 7000222) {
							if (l1iteminstance1.getEnchantLevel() >= 6) {
								pc.sendPackets(new S_ServerMessage(79));
								return;
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (weaponId == 7 || weaponId == 35 || weaponId == 48 || weaponId == 73 || weaponId == 105
							|| weaponId == 120 || weaponId == 147 || weaponId == 156 || weaponId == 174
							|| weaponId == 9003 || weaponId == 175 || weaponId == 224 || weaponId == 203012
							|| weaponId == 7000222) {
						if (itemId != L1ItemId.IVORYTOWER_WEAPON_SCROLL) {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							return;
						}
					}
					if (l1iteminstance1.getBless() >= 128 && (!(itemId >= 210064 && itemId <= 210067
							|| itemId >= 560030 && itemId <= 560033 || itemId == 810003 || itemId == 4100148))) { // 봉인템
						pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
						return;
					}
					/** 속성 인챈 **/
					if (itemId == 210064 && l1iteminstance1.getAttrEnchantLevel() != 0
							&& l1iteminstance1.getAttrEnchantLevel() != 11
							&& l1iteminstance1.getAttrEnchantLevel() != 12
							&& l1iteminstance1.getAttrEnchantLevel() != 13
							&& l1iteminstance1.getAttrEnchantLevel() != 14
							&& l1iteminstance1.getAttrEnchantLevel() != 15) {
						pc.sendPackets(new S_ServerMessage(1294));
						return;
					}
					if (itemId == 210065 && l1iteminstance1.getAttrEnchantLevel() != 0
							&& l1iteminstance1.getAttrEnchantLevel() != 16
							&& l1iteminstance1.getAttrEnchantLevel() != 17
							&& l1iteminstance1.getAttrEnchantLevel() != 18
							&& l1iteminstance1.getAttrEnchantLevel() != 19
							&& l1iteminstance1.getAttrEnchantLevel() != 20) {
						pc.sendPackets(new S_ServerMessage(1294));
						return;
					}
					if (itemId == 210066 && l1iteminstance1.getAttrEnchantLevel() != 0
							&& l1iteminstance1.getAttrEnchantLevel() != 6 && l1iteminstance1.getAttrEnchantLevel() != 7
							&& l1iteminstance1.getAttrEnchantLevel() != 8 && l1iteminstance1.getAttrEnchantLevel() != 9
							&& l1iteminstance1.getAttrEnchantLevel() != 10) {
						pc.sendPackets(new S_ServerMessage(1294));
						return;
					}
					if (itemId == 210067 && l1iteminstance1.getAttrEnchantLevel() != 0
							&& l1iteminstance1.getAttrEnchantLevel() != 1 && l1iteminstance1.getAttrEnchantLevel() != 2
							&& l1iteminstance1.getAttrEnchantLevel() != 3 && l1iteminstance1.getAttrEnchantLevel() != 4
							&& l1iteminstance1.getAttrEnchantLevel() != 5) {
						pc.sendPackets(new S_ServerMessage(1294));
						return;
					}
					int enchant_level = l1iteminstance1.getEnchantLevel();

					if (enchant_level >= Config.무기인첸트 && (!(itemId >= 210064 && itemId <= 210067)) && (!(itemId >= 560030 && itemId <= 560033))) {
						pc.sendPackets(new S_SystemMessage("무기는 +" + Config.무기인첸트 + "이상 강화할 수 없습니다."));
						return;
					}
					if (safe_enchant == 0) {
						if (enchant_level >= Config.무기고급인첸트 && (!(itemId >= 210064 && itemId <= 210067))
								&& (!(itemId >= 560030 && itemId <= 560033))) {
							if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON || itemId == L1ItemId.ENCHANT_WEAPONA) {
								pc.getInventory().removeItem(l1iteminstance, 1);
								SuccessEnchant(pc, l1iteminstance1, client, -1);
							} else {
								pc.sendPackets(new S_SystemMessage("무기는 +" + Config.무기고급인첸트 + "이상 강화할 수 없습니다."));
							}
							return;
						}
					}
					if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON || itemId == L1ItemId.ENCHANT_WEAPONA) { // c-dai
						pc.getInventory().removeItem(l1iteminstance, 1);
						int rnd = _random.nextInt(100) + 1;
						if (safe_enchant == 0 && rnd <= 30) {
							FailureEnchant(pc, l1iteminstance1, client);
							return;
						}
						if (enchant_level < -6) { // -7이상은 할 수 없다.
							FailureEnchant(pc, l1iteminstance1, client);
						} else {
							SuccessEnchant(pc, l1iteminstance1, client, -1);
						}

					} else if (itemId == 210064 || itemId == 210065 || itemId == 210066 || itemId == 210067) {
						AttrEnchant(pc, l1iteminstance1, itemId);

						/** 속성변환주문서 **/
					} else if (itemId >= 560030 && itemId <= 560033) {
						AttrChangeEnchant(pc, l1iteminstance1, itemId);

						/** 고대인의 고서:무기 확률 **/
					} else if (itemId == 68076) { // 고대의 서: 무기
						if (l1iteminstance1 != null && l1iteminstance1.getItem().getType2() == 1) {
							int max_enchantLevel = l1iteminstance1.getItem().get_safeenchant() == 0
									? Config.rareweaponLevel : Config.weaponLevel;

							if (enchant_level >= max_enchantLevel) {
								pc.sendPackets(new S_SystemMessage("더 이상 인챈이 불가능합니다."));
								return;
							}
							/*
							 * if (enchant_level < 3) { pc.sendPackets(new
							 * S_SystemMessage(
							 * "고대인의고서:무기는 +3 이상의 무기에만 사용이 가능합니다")); return; }
							 */
							int enchant = enchant_level < 0 ? 0
									: l1iteminstance1.getItem().get_safeenchant() == 0
											? enchant_level >= Config.rareweapon.length ? Config.rareweapon.length - 1
													: enchant_level
											: enchant_level >= Config.weapon.length ? Config.weapon.length - 1
													: enchant_level;
							int dice = l1iteminstance1.getItem().get_safeenchant() == 0 ? Config.rareweapon[enchant]
									: Config.weapon[enchant];
							if (MJRnd.isWinning(100, dice)) {
								SuccessEnchant(pc, l1iteminstance1, client, +1);
								pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
						
						
						/** 사신의 숨결 확률 **/
					} else if (itemId == 4100136) { // 사신의 숨결
						if (l1iteminstance1 != null && l1iteminstance1.getItem().getType2() == 1) {
							if (enchant_level >= Config.sasinweaponLevel) {
								pc.sendPackets(new S_SystemMessage("더 이상 인챈이 불가능합니다."));
								return;
							}
							/*
							 * if (enchant_level < 5) { pc.sendPackets(new
							 * S_SystemMessage(
							 * "고대인의고서:방어는 +5 이상의 방어에만 사용이 가능합니다")); return; }
							 */

							int enchant = enchant_level < 0 ? 0
									: enchant_level >= Config.sasinweapon.length ? Config.sasinweapon.length - 1 : enchant_level;
							if (MJRnd.isWinning(100, Config.sasinweapon[enchant])) {
								SuccessEnchant(pc, l1iteminstance1, client, +1);
								pc.sendPackets(new S_SystemMessage("사신의 숨결이 스며 들어 인챈트에 성공 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_SystemMessage("사신의 숨결이 스며 들지 못해 인챈트에 실패 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
						
						
						
					/** 영웅 무기 주문서 **/
					} else if (itemId == 724) {
						if (l1iteminstance1 != null && weaponId == 315 || weaponId == 316 || weaponId == 317
								|| weaponId == 318 || weaponId == 319 || weaponId == 320 || weaponId == 1104
								|| weaponId == 7000136 || weaponId == 7000213 || weaponId == 259 || weaponId == 260
								|| weaponId == 261 || weaponId == 262 || weaponId == 263 || weaponId == 264
								|| weaponId == 265 || weaponId == 266 || weaponId == 267) {
							if (enchant_level > 10) { // 사용최대 인챈수치
								pc.sendPackets(new S_SystemMessage("더 이상 주 인챈트가 불가능 합니다."));
								return;
							}
							int k3 = CommonUtil.random(100);

							if (k3 >= 0 && k3 <= Config.Hero_weapon) { // +1 될확율
																		// 5%
								if (Config.ENCHANT_MAX_FAIL && enchant_level >= 11) {
									pc.sendPackets(new S_SystemMessage("강렬하게 빛났지만 인챈트에 실패 하였습니다."));
									pc.getInventory().removeItem(l1iteminstance, 1);
								} else {
									SuccessEnchant(pc, l1iteminstance1, client, +1);
									pc.sendPackets(new S_SystemMessage("강렬하게 빛을 내어 인챈트 성공 하였습니다."));
									pc.getInventory().removeItem(l1iteminstance, 1);
								}
							} else {
								pc.sendPackets(new S_SystemMessage("강렬하게 빛났지만 인챈트에 실패 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
						//		FailureEnchant(pc, l1iteminstance1, client);
							}
						}

					} else if (itemId == 810003) {// 장인의 무기 마법 주문서
						if (!(l1iteminstance1.getItem().getMaterial() == 9
								|| l1iteminstance1.getItem().getMaterial() == 18)) {
							if (enchant_level >= 9) { // 9와 같거나 클경우
								int rnd = _random.nextInt(100);
								if (rnd <= Config.Master_Enchant) {
									SuccessEnchant(pc, l1iteminstance1, client, 1);
								} else {
									pc.sendPackets(new S_ServerMessage(1310));
									// 인챈트: 강렬하게 빛났지만 아무 일도 없었습니다.
								}
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_SystemMessage("무기 인챈트 +9 이상에서만 사용이 가능합니다."));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(1294));
							// 인챈트: 해당 강화 주문서 사용 불가
						}
						
					} else if (itemId == 4100148) {// 사신의 무기 마법 주문서
						if (!(l1iteminstance1.getItem().getMaterial() == 9999)) {
							if (enchant_level >= 0) {
								int rnd = _random.nextInt(100);
								if (rnd <= 100) {
									SuccessEnchant(pc, l1iteminstance1, client, 1);
								}
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_SystemMessage("무기인챈트 +8 이상에서만 사용이 가능합니다.")); // 0도 가능
								// 인챈트 +9 무기만 사용 가능
							}
						} else {
							pc.sendPackets(new S_ServerMessage(1294));
							// 인챈트: 해당 강화 주문서 사용 불가
						}
					} else if (enchant_level < safe_enchant) {
						pc.getInventory().removeItem(l1iteminstance, 1);
						SuccessEnchant(pc, l1iteminstance1, client, RandomELevel(l1iteminstance1, itemId));
					} else {
						pc.getInventory().removeItem(l1iteminstance, 1);
						int rnd = _random.nextInt(100) + 1;
						int enchant_chance_wepon;
						int chance = 0;
						try {
							chance = WeaponEnchantList.getInstance().getWeaponEnchant(l1iteminstance1.getItemId());
						} catch (Exception e) {
							System.out.println("WeaponEnchantList chance Error");
						}
						int weaponChance = WeaponEnchantInformationTable.getInstance().getChance(weaponId,
								enchant_level);
						if (enchant_level >= 7 && enchant_level < 8) {
							enchant_chance_wepon = 10 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
							rnd = _random.nextInt(100) + 1;
							enchant_chance_wepon *= Config.ENCHANT_FAIL_RATE_ONEST;
						}
						if (enchant_level >= 8 && enchant_level < 9) {
							enchant_chance_wepon = 10 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
							rnd = _random.nextInt(120) + 1;
							enchant_chance_wepon *= Config.ENCHANT_FAIL_RATE_ONESTO;
						}
						if (enchant_level >= 9 && enchant_level < 10) {
							enchant_chance_wepon = 10 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
							rnd = _random.nextInt(150) + 1;
							enchant_chance_wepon *= Config.ENCHANT_FAIL_RATE_ONE;
						}
						if (enchant_level >= 10 && enchant_level < 11) {
							enchant_chance_wepon = 1 / ((enchant_level - safe_enchant + 1) * 2)
									/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
							rnd = _random.nextInt(200) + 1;
							enchant_chance_wepon *= Config.ENCHANT_FAIL_RATE_TWO;
						}
						if (enchant_level >= Config.ENCHANT_COENT && enchant_level <= 15) { // 11이상은
																							// 확률
																							// 외부화
							if (!Config.ENCHANT_MAX_FAIL) {
								enchant_chance_wepon = 3 / ((enchant_level - safe_enchant + 1) * 2)
										/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
							} else {
								enchant_chance_wepon = 0;
							}
						} else {
							if (l1iteminstance1.getItem().get_safeenchant() == 0) {// 안전인챈시
								enchant_chance_wepon = 70 / ((enchant_level - safe_enchant + 1) * 2)
										/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance + chance;
							} else {// 6~8까지
								enchant_chance_wepon = 60 / ((enchant_level - safe_enchant + 1) * 2)
										/ (enchant_level / 9 != 0 ? 1 * 2 : 1) + weaponChance;
							}
						}
						if (pc.isGm()) {
							pc.sendPackets(new S_SystemMessage("\\aA확률 : [\\aG " + enchant_chance_wepon + "\\aA ]"));
							pc.sendPackets(new S_SystemMessage("\\aA찬스 : [\\aG " + rnd + " \\aA]"));
							pc.sendPackets(new S_SystemMessage("\\aA디비 : [\\aG " + weaponChance + " \\aA]"));
							pc.sendPackets(new S_SystemMessage("\\aA추가 : [ \\aG" + chance + "\\aA ]"));
						}

						if (pc._EnchantWeaponSuccess == true) {
							int randomEnchantLevel = 0;
							// TODO 집행무기 +1 이상일때 1씩만뜨도록
							if (enchant_level >= 1 && (weaponId == 12 || weaponId == 61 || weaponId == 86
									|| weaponId == 134 || weaponId == 7000136 || weaponId == 7000213 || weaponId == 1104
									|| weaponId == 66 || (weaponId >= 20201 && weaponId <= 202015))) {
								randomEnchantLevel = 1;
							} else {
								randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);
							}
							SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
							pc._EnchantWeaponSuccess = false;
						} else if (rnd < enchant_chance_wepon) {
							int randomEnchantLevel = 0;
							// TODO 집행무기 +1 이상일때 1씩만뜨도록
							if (enchant_level >= 1 && (weaponId == 12 || weaponId == 61 || weaponId == 86
									|| weaponId == 134 || weaponId == 7000136 || weaponId == 7000213 || weaponId == 1104
									|| weaponId == 66 || (weaponId >= 20201 && weaponId <= 202015))) {
								randomEnchantLevel = 1;
							} else {
								randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);
							}
							SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
						} else {
							FailureEnchant(pc, l1iteminstance1, client);
						}
					}
				}
					break;

				// TODO 방어구 강화 인챈트 관련 소스
				case L1ItemId.SCROLL_OF_ENCHANT_ARMOR:
				case L1ItemId.SCROLL_OF_ENCHANT_ARMOR4:
				case L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR5:
				case L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR6:
				case L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR:
				case L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR:
				case L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR:
				case L1ItemId.ENCHANT_ARMOR:
				case L1ItemId.Inadril_T_ScrollA:
				case L1ItemId.Inadril_T_ScrollB:
				case L1ItemId.Inadril_T_ScrollC:
				case L1ItemId.Pure_white_Scroll:
				case L1ItemId.Roomtis_Scroll:
				case L1ItemId.IVORYTOWER_ARMOR_SCROLL:
				case L1ItemId.IVORYTOWER_GKFFHDNLS:
				case L1ItemId.Inadril_T_ScrollA1:
				case L1ItemId.Inadril_T_ScrollB2:
				case L1ItemId.Inadril_T_ScrollB3:
				case L1ItemId.ENCHANT_TEST1:
				case L1ItemId.ENCHANT_TEST2:
				case L1ItemId.ENCHANT_TEST3:
				case L1ItemId.ENCHANT_TEST4:
				case L1ItemId.ENCHANT_TEST5:
			//	case L1ItemId.ENCHANT_TEST6:  // 빛나는 아덴 용사의 보호 주문서

				case 4100034:  // 빛나는 아덴 용사의 보호 주문서
				case 7004: // 수련자의 장신구 마법 주문서
				case 40129: // 고대의 갑옷 주술 두루마리
				case 140129:
				case 210084:
				case 3000100:// 문장강화석
				case 5991: // 휘장강화주문서
				case 3000517: // 고대의룬 강화석
				case 210068: // 장신구 마법 주문서
				case 68077: // 고대인의 서: 갑옷
				case 680777:// 할파스의 숨결
				case 3000380: // 고대인의 고서:악세
				case 30069: // 용사의 갑옷 마법 주문서 
				case 30147: // 할로윈 갑옷 마법 주문서
				{	if (l1iteminstance1 == null || l1iteminstance1.getItem().getType2() != 2) {
						pc.sendPackets("\\aG[해당 방어구]는 인챈이 불가 합니다.");
						pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
						return;
					}
				
					/** 방어구에 따른 강화 주문서 필터링 **/
					int fill_itemId = l1iteminstance.getItemId();
					switch (l1iteminstance1.getItem().getType()) {
					case 30: // 휘장
						if (fill_itemId != 3000546 && fill_itemId != 5991) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
						break;
					case 29: // 견갑
						if (fill_itemId != 4100034 && fill_itemId != 68077 && fill_itemId != 40074 && fill_itemId != 140074 && fill_itemId != 240074) { 
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
						break;
					case 4: //망토
						if (fill_itemId != 4100034 && fill_itemId != 68077 && fill_itemId != 40074 && fill_itemId != 140074	&& fill_itemId != 240074) { 
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
						break;
					case 15: // 각반
						if (fill_itemId != 68077 && fill_itemId != 40074 && fill_itemId != 140074 && fill_itemId != 240074) { 
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
						break;
					case 28: // 문장
						if (fill_itemId != 3000547 && fill_itemId != 3000100) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
						break;
					}
					if (l1iteminstance1.isEquipped()) {
						pc.sendPackets(new S_ServerMessage(4302));// 착용중인 아이템에는
						return;
					}

					int safe_enchant = ((L1Armor) l1iteminstance1.getItem()).get_safeenchant();
					if (safe_enchant < 0) { // 강화 불가
						pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
						return;
					}

					int armorId = l1iteminstance1.getItem().getItemId();
					int armortype = l1iteminstance1.getItem().getType();

					/** 할로윈 마법 주문서 **/
					if (armorId == 21095) {
						if (itemId == 30147) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (itemId == 30147) {
						if (armorId == 21095) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					

					
					/** 할파스 갑옷류 **/
					if (armorId >= 111137 && armorId <= 111141) {
						if (itemId == 680777) {
						} else {
							pc.sendPackets("\\aG[할파스의 숨결]로만 인챈이 가능 합니다.");
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (itemId == 680777) {
						if (armorId >= 111137 && armorId <= 111141) {
						} else {
							pc.sendPackets("\\aG[할파스의 숨결]로만 인챈이 가능 합니다.");
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					
					/** 빛나는 아덴 용사의 견갑, 망토 **/
					if (itemId == 4100034) {
						if (armorId >= 900121 && armorId <= 900121 || armorId >= 900189 && armorId <= 900189) {
						} else {
							pc.sendPackets("\\aG[빛나는 아덴 용사의 보호 주문서]로만 인챈이 가능 합니다.");
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (armorId >= 900121 && armorId <= 900121 || armorId >= 900189 && armorId <= 900189) {
						if (itemId == 4100034) {
						} else {
							pc.sendPackets("\\aG[빛나는 아덴 용사의 보호 주문서]로만 인챈이 가능 합니다.");
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
		
					
					
					/** 빛나는 아덴 용사의 망토 **/
			/*		if (itemId == 4100034) {
						if ( armorId >= 900189 && armorId <= 900189) {
						} else {
							pc.sendPackets("\\aG[빛나는 아덴 용사의 보호 주문서]로만 인챈이 가능 합니다.");
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}*/
				
		
					
					/** 인나드릴 티셔츠 갑옷 마법 주문서 **/
					if (armorId >= 22215 && armorId <= 22223 || armorId >= 490000 && armorId <= 490008) {
						if (itemId >= 410066 && itemId <= 410068) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (itemId >= 410066 && itemId <= 410068) {
						if (armorId >= 22215 && armorId <= 22223 || armorId >= 490000 && armorId <= 490008) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					
//					/** 유니콘의 각반류 갑옷 마법 주문서 **/
//					if (armorId >= 900030 && armorId <= 900032) {
////						if ((itemId >= 3000217 && itemId <= 3000219) || itemId == 68077) {
//						if ((itemId == 3000217 && itemId == 3000218) && itemId == 3000219) {
//						} else {
//							pc.sendPackets(new S_ServerMessage(79));
//							return;
//						}
//					}
////					if (itemId >= 3000217 && itemId <= 3000219) {
//					if (itemId == 3000217 && itemId == 3000218 && itemId == 3000219) {
//						if (armorId >= 900030 && armorId <= 900032) {
//						} else {
//							pc.sendPackets(new S_ServerMessage(79));
//							return;
//						}
//					}
					
					/** 유니콘의 각반 갑옷 마법 주문서 **/
					if (armorId >= 900030 && armorId <= 900032) {
						if ((itemId >= 3000217 && itemId <= 3000219)) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (itemId >= 3000217 && itemId <= 3000219) {
						if (armorId >= 900030 && armorId <= 900032) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}

					/** 용의 티셔츠 갑옷 마법 주문서 **/
//					if (armorId >= 900025 && armorId <= 900028) {
					if (armorId == 900025 || armorId == 900026 || armorId == 900027 || armorId == 900028 ||
						armorId == 900184 || armorId == 900185 || armorId == 900186 || armorId == 900187) {
						if ((itemId >= 3000160 && itemId <= 3000162) || itemId == 68077) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (itemId >= 3000160 && itemId <= 3000162) {
//						if (armorId >= 900025 && armorId <= 900028) {
						if (armorId == 900025 || armorId == 900026 || armorId == 900027 || armorId == 900028 ||
							armorId == 900184 || armorId == 900185 || armorId == 900186 || armorId == 900187) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}

								
					/** 문장 강화석 **/
					if (itemId == 5991 || itemId == 3000546) {
						if (armorId >= 900081 && armorId <= 900084 || armorId >= 900152 && armorId <= 900154) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					/** 문장 강화석 **/
					if (itemId == 3000100 || itemId == L1ItemId.ENCHANT_TEST5) {
						if (armorId == 900020 || armorId == 900021 || armorId == 900049 || armorId == 900050
								|| armorId == 900051 || armorId == 900124 || armorId == 900125 || armorId == 900126
								|| armorId >= 900093 && armorId <= 900099 || armorId >= 900127 && armorId <= 900130) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					/** 고대의 룬 강화석 **/
					if (itemId == 3000517 || itemId == L1ItemId.ENCHANT_TEST3) {
						int enchant_level = l1iteminstance1.getEnchantLevel();
						if (enchant_level >= Config.accessorytest) {
							pc.sendPackets(new S_SystemMessage("해당 인챈 이하의 고대의 룬 에만 사용 가능합니다."));
							return;
						}
					}
					if (armorId == 900116) {
						if (itemId == 3000517 || itemId == 3000518) {
						} else {
							pc.sendPackets(new S_SystemMessage("고대의 룬 보호석으로만 인챈이 가능합니다."));
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					/** 환상의 갑옷 마법 주문서 **/
					if (armorId >= 423000 && armorId <= 423008) {
						if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
						if (armorId >= 423000 && armorId <= 423008) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (itemId == 30069) {// 용사의 갑옷 마법 주문서
						if (armorId >= 22328 && armorId <= 22335) { // 이외에 강화 불가
						} else {
							pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
							// 일어나지
							// 않았습니다.
							return;
						}
					}
					if (armorId >= 22328 && armorId <= 22335) {
						if (itemId == 30069) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					/** 창천의 갑옷 마법 주문서 **/
					if (itemId == 210084) {
						if (armorId >= 22034 && armorId <= 22064) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (armorId >= 22034 && armorId <= 22064) {
						if (itemId == 210084) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}

					/** 장신구 강화 주문서 */
					if (itemId == 210068 || itemId == L1ItemId.Pure_white_Scroll || itemId == L1ItemId.Roomtis_Scroll
							|| itemId == L1ItemId.IVORYTOWER_GKFFHDNLS || itemId == 810012 || itemId == 810013
							|| itemId == 7004 || itemId == L1ItemId.ENCHANT_TEST1 || itemId == L1ItemId.ENCHANT_TEST2) {
						if (armortype >= 8 && armortype <= 12) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (armortype >= 8 && armortype <= 12) {
						if (itemId == 210068 || itemId == L1ItemId.Pure_white_Scroll || itemId == L1ItemId.ENCHANT_TEST1
								|| itemId == 7004 || itemId == L1ItemId.ENCHANT_TEST2
								|| itemId == L1ItemId.Roomtis_Scroll || itemId == 810012 || itemId == 810013
								|| itemId == L1ItemId.IVORYTOWER_GKFFHDNLS || itemId == 3000380) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}

					if ((itemId == L1ItemId.ENCHANT_TEST3 || itemId == 3000517) && armorId != 900116) {
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}

					// TODO 할로윈 마법 주문서
					if (itemId == L1ItemId.IVORYTOWER_GKFFHDNLS) {
						if (armorId == 22367 || armorId == 900024) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (armorId == 22367 || armorId == 900024) {
						if (itemId == L1ItemId.IVORYTOWER_GKFFHDNLS) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					/** 룸티스 강화 주문서 **/
					if (itemId == L1ItemId.Roomtis_Scroll || itemId == L1ItemId.ENCHANT_TEST2) {
						if (armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
								|| armorId == 222340 || armorId == 222341) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
							|| armorId == 222340 || armorId == 222341) {
						if (itemId == L1ItemId.Roomtis_Scroll || itemId == L1ItemId.ENCHANT_TEST2) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					/** 스냅퍼의 반지 강화 주문서 **/
					if (itemId == L1ItemId.Pure_white_Scroll || itemId == L1ItemId.ENCHANT_TEST1) {
						if (armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291
								|| armorId >= 222330 && armorId <= 222336) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291
							|| armorId >= 222330 && armorId <= 222336) {
						if (itemId == L1ItemId.Pure_white_Scroll || itemId == L1ItemId.ENCHANT_TEST1) {
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					/** 수련자의 장신구 마법 주문서 **/
					if (itemId == 7004) {
						if ((armorId >= 22337 && armorId <= 22339 || armorId == 22073)) {
							if (l1iteminstance1.getEnchantLevel() >= 4) {
								pc.sendPackets(new S_ServerMessage(79));
								return;
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					// 상아탑의 갑옷 마법 주문서
					if (itemId == L1ItemId.IVORYTOWER_ARMOR_SCROLL) {
						if (armorId == 20028 || armorId == 20082 || armorId == 20126 || armorId == 20173
								|| armorId >= 22300 && armorId <= 22311 || armorId == 20206 || armorId == 20232
								|| armorId == 20283) {
							if (l1iteminstance1.getEnchantLevel() >= 6) {
								pc.sendPackets(new S_ServerMessage(79));
								return;
							}
						} else if (armorId == 22312 || armorId == 321515 || armorId >= 20500 && armorId <= 20505) {
							if (l1iteminstance1.getEnchantLevel() >= 4) {
								pc.sendPackets(new S_ServerMessage(79));
								return;
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}

					if (armorId == 20028 || armorId == 20082 || armorId == 20126 || armorId == 20173 || armorId == 20206
							|| armorId == 20232 || armorId == 20283 || (armorId >= 22300 && armorId <= 22312)
							|| (armorId >= 20500 && armorId <= 20505)) {
						if (itemId != L1ItemId.IVORYTOWER_ARMOR_SCROLL) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
					if (l1iteminstance1.getBless() >= 128) { // 봉인템
						pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
						return;
					}

					int enchant_level = l1iteminstance1.getEnchantLevel();

					// 룸티스의 귀걸이 인챈제한
					if (armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
							|| armorId >= 222340 && armorId <= 222341) {
						if (enchant_level >= Config.룸티스) {
							pc.sendPackets(new S_SystemMessage("룸티스귀걸이는 +" + Config.룸티스 + "이상은 인챈할 수 없습니다."));
							pc.sendPackets(new S_SystemMessage("현재는 인첸트 제한중 입니다."));
							return;
						}
					} else if (armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291 // 스냅퍼의
																												// 반지
																												// 인첸제한
							|| armorId >= 222330 && armorId <= 222336) {
						if (enchant_level >= Config.스냅퍼) {
							pc.sendPackets(new S_SystemMessage("스냅퍼 반지는 +" + Config.스냅퍼 + " 이상은 인챈할 수 없습니다."));
							pc.sendPackets(new S_SystemMessage("현재는 인첸트 제한중 입니다."));
							return;
						}
					} else if (armorId >= 900020 && armorId <= 900021 || armorId >= 900049 && armorId <= 900051
							|| armorId >= 900093 && armorId <= 900099 || armorId >= 900124 && armorId <= 900126
							|| armorId >= 900127 && armorId <= 900130) {
						if (enchant_level >= Config.문장류) {
							pc.sendPackets(new S_SystemMessage("문장류는 +" + Config.문장류 + " 이상은 인챈할 수 없습니다."));
							pc.sendPackets(new S_SystemMessage("현재는 인첸트 제한중 입니다."));
							return;
						}
					} else if (armortype >= 8 && armortype <= 12) {
						if (!(armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339
								|| armorId >= 222340 && armorId <= 222341 || armorId >= 22224 && armorId <= 22228
								|| armorId == 222290 || armorId == 222291 || armorId >= 222330 && armorId <= 222336)) {
							if (enchant_level >= Config.악세사리) {
								pc.sendPackets(new S_SystemMessage("악세사리는 +" + Config.악세사리 + " 이상은 인챈할 수 없습니다."));
								pc.sendPackets(new S_SystemMessage("현재는 인첸트 제한중 입니다."));
								return;
							}
						}
					} else {
						if (safe_enchant == 0) {
							if (enchant_level >= Config.방어구고급인첸트) {
								if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.ENCHANT_ARMOR) { // c-dai
									pc.getInventory().removeItem(l1iteminstance, 1);
									SuccessEnchant(pc, l1iteminstance1, client, -1);
								} else {
									pc.sendPackets(new S_SystemMessage("방어구는 +" + Config.방어구고급인첸트 + "이상 강화할 수 없습니다."));
								}
								return;
							}
						} else {
							if (enchant_level >= Config.방어구인첸트) { // 인첸트 제한
								pc.sendPackets(new S_SystemMessage("방어구는 +" + Config.방어구인첸트 + "이상 강화할 수 없습니다."));
								return;
							}
						}
					}

					if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.Inadril_T_ScrollC
							|| itemId == L1ItemId.ENCHANT_ARMOR || itemId == L1ItemId.Inadril_T_ScrollB3
							|| itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR5) { // 저주
																				// 갑옷마법
																				// 주문서류
						pc.getInventory().removeItem(l1iteminstance, 1);
						int rnd = _random.nextInt(100) + 1;
						if (safe_enchant == 0 && rnd <= 30) {
							FailureEnchant(pc, l1iteminstance1, client);
							return;

						} else if (enchant_level < -1) { // 기본적인템들은 -2에서 저젤
							// 바를시증발
							FailureEnchant(pc, l1iteminstance1, client);
						} else {
							SuccessEnchant(pc, l1iteminstance1, client, -1);
						}
//					} else if (itemId == 810013) { // 축복받은 오림의 장신구 마법 주문서
//						enchant_bless_orim(pc, l1iteminstance, l1iteminstance1);
//						return;
					} else if (itemId == 3000428) {
						enchant_snapper(pc, l1iteminstance, l1iteminstance1);
						return;
					} else if (itemId == 3000430) {
						enchant_roomtis(pc, l1iteminstance, l1iteminstance1);
						return;
					} else if (itemId == 3000546) {
						enchant_insignia(pc, l1iteminstance, l1iteminstance1);
						return;
					} else if (itemId == 3000547) {
						enchant_sentence(pc, l1iteminstance, l1iteminstance1);
						return;
					} else if (itemId == 4100034) {
						enchant_should(pc, l1iteminstance, l1iteminstance1);
						return;

						/** 고대의 서:방어구 확률 **/
					} else if (itemId == 68077 || itemId == 680777 ) { // 고대의 서: 방어구, 할파스의 숨결
						if (l1iteminstance1 != null && l1iteminstance1.getItem().getType2() == 2) {
							if (enchant_level >= Config.armorLevel) {
								pc.sendPackets(new S_SystemMessage("더 이상 인챈이 불가능합니다."));
								return;
							}
							/*
							 * if (enchant_level < 5) { pc.sendPackets(new
							 * S_SystemMessage(
							 * "고대인의고서:방어는 +5 이상의 방어에만 사용이 가능합니다")); return; }
							 */

							int enchant = enchant_level < 0 ? 0
									: enchant_level >= Config.armor.length ? Config.armor.length - 1 : enchant_level;
							if (MJRnd.isWinning(100, Config.armor[enchant])) {
								SuccessEnchant(pc, l1iteminstance1, client, +1);
								pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
					} else if (itemId == 3000380) {// 고대의 서: 악세
						if (l1iteminstance1 != null) {
							if (enchant_level > Config.accessoryLevel) {
								pc.sendPackets(new S_SystemMessage("더 이상 인챈이 불가능합니다."));
								return;
							}
							int k3 = CommonUtil.random(100);

							int enchant = enchant_level < 0 ? 0
									: enchant_level >= Config.accessory.length ? Config.accessory.length - 1
											: enchant_level;
							if (MJRnd.isWinning(100, Config.accessory[enchant])) {
								SuccessEnchant(pc, l1iteminstance1, client, +1);
								pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
						// TODO 고대의 룬 보호석
					} else if (itemId == 3000518) {
						if (l1iteminstance1 != null) {
						/*	if (enchant_level < 2) {
								pc.sendPackets(new S_SystemMessage("고대의 룬 보호석은 최소 +2 이상부터 가능합니다."));
								return;
							}*/
							if (enchant_level >= Config.accessorytest) {
								pc.sendPackets(new S_SystemMessage("해당 인챈 이하의 고대의 룬 에만 사용 가능합니다."));
								return;
							}

							int enchant = enchant_level < 0 ? 0
									: enchant_level >= Config.ancient.length ? Config.ancient.length - 1
											: enchant_level;
							if (MJRnd.isWinning(100, Config.ancient[enchant])) {
								SuccessEnchant(pc, l1iteminstance1, client, +1);
								pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
								pc.getInventory().removeItem(l1iteminstance, 1);
							}
						}
					} else if (enchant_level < safe_enchant) {
						pc.getInventory().removeItem(l1iteminstance, 1);
						SuccessEnchant(pc, l1iteminstance1, client, RandomELevel(l1iteminstance1, itemId));
					} else {
						pc.getInventory().removeItem(l1iteminstance, 1);

						int rnd = _random.nextInt(100) + 1;
						int enchant_chance_armor;
						int enchant_level_tmp;
						int chance1 = 0;

						if (safe_enchant == 0) { // 뼈, 브락크미스릴용 보정
							enchant_level_tmp = 2;
						} else {
							enchant_level_tmp = 1;
						}

						if (armortype >= 8 && armortype <= 12) {// 장신구 인챈트 성공 확률
							int acceChance = AccessoryEnchantInformationTable.getInstance().getChance(armorId,
									enchant_level);
							pc.getInventory().setEquipped(l1iteminstance1, false);// 러쉬할때착용해제
																					// 않한다

							if (enchant_level <= 0) {
								enchant_chance_armor = 9 * acceChance; // 콘피그 5면
																		// 0짜리
																		// 성공확률=45%
							} else {
								enchant_chance_armor = (8 * acceChance) / enchant_level;
							}
							/** 운영자인경우 */
							if (pc.isGm()) {
								pc.sendPackets(
										new S_SystemMessage("\\aA확률 : [\\aG " + enchant_chance_armor + "\\aA ]"));
								pc.sendPackets(new S_SystemMessage("\\aA찬스 : [\\aG " + rnd + " \\aA]"));
							}

							/** 룸티스 특정인챈 찬스 부여 **/
							if (armorId == 22229 || armorId == 22230 || armorId == 22231 || armorId == 222337
									|| armorId == 222338 || armorId == 222339 || armorId == 222340
									|| armorId == 222341) {
								if (enchant_level == 4) {
									enchant_chance_armor = 15;
								} else if (enchant_level >= 5) { // -- 인챈트가 4라면
									enchant_chance_armor = 10;
								}
							}

						} else {
							int chance = 0;

							try {
								chance = ArmorEnchantList.getInstance().getArmorEnchant(l1iteminstance1.getItemId());
							} catch (Exception e) {
								System.out.println("WeaponEnchantList chance Error");
							}

							int armorChance = ArmorEnchantInformationTable.getInstance().getChance(armorId,
									enchant_level);
							if (enchant_level >= 6) {
								if (l1iteminstance1.getMr() > 0) {
									enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2)
											/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + armorChance
											+ chance;
								} else {
									enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2)
											/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp) + armorChance
											+ chance;
								}
							} else {
								if (l1iteminstance1.getItem().get_safeenchant() == 0) {
									if (l1iteminstance1.getMr() > 0) {
										enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2)
												/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
												+ armorChance + chance;
									} else {
										enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2)
												/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
												+ armorChance + chance;
									}
								} else {
									if (l1iteminstance1.getMr() > 0) {
										enchant_chance_armor = 80 / ((enchant_level - safe_enchant + 1) * 2)
												/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
												+ armorChance;
									} else {
										enchant_chance_armor = 90 / ((enchant_level - safe_enchant + 1) * 2)
												/ (enchant_level / 7 != 0 ? 1 * 2 : 1) / (enchant_level_tmp)
												+ armorChance;
									}
								}
							}
							if (pc.isGm()) {
								pc.sendPackets(
										new S_SystemMessage("\\aA확률 : [\\aG " + enchant_chance_armor + "\\aA ]"));
								pc.sendPackets(new S_SystemMessage("\\aA찬스 : [\\aG " + rnd + " \\aA]"));
								pc.sendPackets(new S_SystemMessage("\\aA디비 : [\\aG " + armorChance + " \\aA]"));
								pc.sendPackets(new S_SystemMessage("\\aA추가 : [ \\aG" + chance + "\\aA ]"));
							}
						}

						if (pc._EnchantArmorSuccess == true) {
							int randomEnchantLevel = 0;
							randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);
							SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
							pc._EnchantArmorSuccess = false;
						} else if (rnd < enchant_chance_armor) {
							int randomEnchantLevel = RandomELevel(l1iteminstance1, itemId);

							/** 가더류 +0 이상일때 1만뜨도록 (본섭화) **/
							if ((enchant_level >= 0 && armorId >= 22000 && armorId <= 22003)
									|| (armorId >= 22254 && armorId <= 22256) || armorId == 22252 || armorId == 20190) {
								randomEnchantLevel = 1;
							}
							/** 특별아이템 +1 이상일때 1만뜨도록 (본섭화) **/
							if ((enchant_level >= 1 && armorId == 900188 || armorId == 900121 || armorId == 900123 || armorId == 900189)) {
								randomEnchantLevel = 1;
							}
							/** 용의티셔츠 +6 이상일때 1만뜨도록 **/
//							if ((enchant_level >= 6 && armorId >= 900025 && armorId <= 900028)) {
							if ((enchant_level >= 6 && armorId == 900025 || armorId == 900026 || armorId == 900027 || armorId == 900028 ||
									 				   armorId == 900184 || armorId == 900185 || armorId == 900186 || armorId == 900187)) {
								randomEnchantLevel = 1;
							}
							/** 고대암석마물류 +6 이상일때 1만뜨도록 **/
							if ((enchant_level >= 6 && armorId >= 900011 && armorId <= 900018)) {
								randomEnchantLevel = 1;
							}
							SuccessEnchant(pc, l1iteminstance1, client, randomEnchantLevel);
						} else if (enchant_level >= 9 && rnd < (enchant_chance_armor * 2)) {
							pc.sendPackets(new S_ServerMessage(160, l1iteminstance1.getLogName(), "$245", "$248"));
//						} else if (itemId == 810012) {
//							pc.sendPackets(new S_ServerMessage(4056, l1iteminstance1.getLogName()));
//							// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
//							if (enchant_level == 0) {
//								SuccessEnchant(pc, l1iteminstance1, client, 0);
//							} else {
//								SuccessEnchant(pc, l1iteminstance1, client, -1);
//							}
//						} else if (itemId == 810013) {
//							pc.sendPackets(new S_ServerMessage(4056, l1iteminstance1.getLogName()));
//							// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
						} else {
							FailureEnchant(pc, l1iteminstance1, client);
						}
					}
				}
					break;
				default:
					if (itemId >= 40136 && itemId <= 40161 || itemId == 410027) { // 불꽃
						int soundid = 3198;
						if (itemId == 40154) {
							soundid = 3198;
						} else if (itemId == 40152) {
							soundid = 2031;
						} else if (itemId == 40141) {
							soundid = 2028;
						} else if (itemId == 40160) {
							soundid = 2030;
						} else if (itemId == 40145) {
							soundid = 2029;
						} else if (itemId == 40159) {
							soundid = 2033;
						} else if (itemId == 40151) {
							soundid = 2032;
						} else if (itemId == 40161) {
							soundid = 2037;
						} else if (itemId == 40142) {
							soundid = 2036;
						} else if (itemId == 40146) {
							soundid = 2039;
						} else if (itemId == 40148) {
							soundid = 2043;
						} else if (itemId == 40143) {
							soundid = 2041;
						} else if (itemId == 40156) {
							soundid = 2042;
						} else if (itemId == 40139) {
							soundid = 2040;
						} else if (itemId == 40137) {
							soundid = 2047;
						} else if (itemId == 40136) {
							soundid = 2046;
						} else if (itemId == 40138) {
							soundid = 2048;
						} else if (itemId == 40140) {
							soundid = 2051;
						} else if (itemId == 40144) {
							soundid = 2053;
						} else if (itemId == 40147) {
							soundid = 2045;
						} else if (itemId == 40149) {
							soundid = 2034;
						} else if (itemId == 40150) {
							soundid = 2055;
						} else if (itemId == 40153) {
							soundid = 2038;
						} else if (itemId == 40155) {
							soundid = 2044;
						} else if (itemId == 40157) {
							soundid = 2035;
						} else if (itemId == 40158) {
							soundid = 2049;
						} else {
							soundid = 3198;
						}

						S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
						pc.sendPackets(s_skillsound);
						pc.broadcastPacket(s_skillsound);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else if (itemId == 810012 || itemId == 810013) {
						L1OrimScrollEnchant ose = L1OrimScrollEnchant.get(itemId);
						if(ose != null){
							ose.use(pc, l1iteminstance, l1iteminstance1);
						} else {
							pc.sendPackets(new S_ServerMessage(74, l1iteminstance1.getLogName()));
						}
						// 스펠 스크롤
					} else if ((itemId >= 40859 && itemId <= 40898) && itemId != 40863) {
						if (pc.isSkillDelay()) {
							pc.sendPackets(new S_ServerMessage(281));
							return;
						}

						int useType = l1iteminstance.getItem().getUseType();
						if (useType == 30 || useType == 0) {
							spellsc_objid = pc.getId();
						} else {
							if (spellsc_objid == pc.getId() || spellsc_objid <= 0) {
								pc.sendPackets(new S_ServerMessage(281)); // \f1마법이
																			// 무효가
																			// 되었습니다.
								return;
							}
						}
						pc.getInventory().removeItem(l1iteminstance, 1);

						cancelAbsoluteBarrier(pc);
						// 아브소르트바리아의 해제
						int skillid = itemId - 40858;
						L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);

						if (skill == null) {
							pc.sendPackets(new S_SystemMessage("사용되지 않는 스킬입니다. 운영자에게 문의하세요. " + skillid));
							return;
						}

						/*if (skillid == L1SkillId.BLESSED_ARMOR) {
							L1ItemInstance armor = pc.getInventory().getItemEquipped(2, 2);
							if (armor != null) {
								pc.sendPackets(new S_SkillSound(pc.getId(), 748));
								armor.setSkillArmorEnchant(pc, L1SkillId.BLESSED_ARMOR, skill.getBuffDuration() * 1000);
								pc.sendPackets(new S_ServerMessage(161, String.valueOf(armor.getLogName()).trim(),
										"$245", "$247"));
								if (armor.isEquipped())
									pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, skill.getBuffDuration(),
											skill.getCastGfx(), 0));
							} else {
								pc.sendPackets(new S_ServerMessage(79));
							}
						} else*/ if (skillid == L1SkillId.ENCHANT_WEAPON) {
							int count = 0;
							boolean use_skill = false;
							if (pc.getWeapon() != null) {
								use_skill = true;
								if (pc.getEquipSlot().getWeaponCount() == 2) {
									count = pc.getEquipSlot().getWeaponCount() - 1;
								} else {
									count = pc.getEquipSlot().getWeaponCount();
								}
								if (pc.getEquipSlot().getWeaponCount() == 2) {
									if (pc.getSecondWeapon() != null
											&& !pc.getSecondWeapon().hasSkillEffectTimer(L1SkillId.ENCHANT_WEAPON)) {
										pc.getWeapon().setSkillWeaponEnchant(pc, skillid,
												skill.getBuffDuration() * 1000);
										pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, skill.getBuffDuration(),
												skill.getCastGfx(), count - 1));
										pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 747,
												skill.getBuffDuration(), false, false));
										pc.getWeapon().setSkillWeaponEnchant(pc, skillid,
												skill.getBuffDuration() * 1000);
										pc.sendPackets(
												new S_ServerMessage(161, pc.getWeapon().getLogName(), "$245", "$247"));
									} else {
										pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, skill.getBuffDuration(),
												skill.getCastGfx(), count - 1));
										pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 747,
												skill.getBuffDuration(), true, false));
										pc.getWeapon().setSkillWeaponEnchant(pc, skillid,
												skill.getBuffDuration() * 1000);
										pc.sendPackets(
												new S_ServerMessage(161, pc.getWeapon().getLogName(), "$245", "$247"));
									}
								} else {
									pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, skill.getBuffDuration(),
											skill.getCastGfx(), count - 1));
									pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 747, skill.getBuffDuration(),
											false, false));
									pc.getWeapon().setSkillWeaponEnchant(pc, skillid, skill.getBuffDuration() * 1000);
									pc.sendPackets(
											new S_ServerMessage(161, pc.getWeapon().getLogName(), "$245", "$247"));
								}
							}
							if (pc.getSecondWeapon() != null) {
								use_skill = true;
								if (pc.getWeapon() != null && pc.getWeapon().hasSkillEffectTimer(L1SkillId.ENCHANT_WEAPON)) {
									count = pc.getEquipSlot().getWeaponCount();
									pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, skill.getBuffDuration(),
											skill.getCastGfx(), count - 1));
									pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 747, skill.getBuffDuration(),
											true, false));
									pc.getSecondWeapon().setSkillWeaponEnchant(pc, skillid,
											skill.getBuffDuration() * 1000);
									pc.sendPackets(new S_ServerMessage(161, pc.getSecondWeapon().getLogName(), "$245",
											"$247"));
								} else {
									pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, skill.getBuffDuration(),
											skill.getCastGfx(), count - 1));
									pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 747, skill.getBuffDuration(),
											false, false));
									pc.getSecondWeapon().setSkillWeaponEnchant(pc, skillid,
											skill.getBuffDuration() * 1000);
									pc.sendPackets(new S_ServerMessage(161, pc.getSecondWeapon().getLogName(), "$245",
											"$247"));
								}
							}
							if (use_skill)
								pc.sendPackets(new S_SkillSound(pc.getId(), 747));

							if (!use_skill)
								pc.sendPackets(new S_ServerMessage(79));

						} else {
							L1SkillUse l1skilluse = new L1SkillUse();
							l1skilluse.handleCommands(client.getActiveChar(), skillid, spellsc_objid, spellsc_x,
									spellsc_y, null, 0, L1SkillUse.TYPE_SPELLSC);
						}
					} else if (itemId >= 41357 && itemId <= 41382) {
						// 알파벳 불꽃
						int soundid = itemId - 34946;
						S_SkillSound s_skillsound = new S_SkillSound(pc.getId(), soundid);
						pc.sendPackets(s_skillsound);
						pc.broadcastPacket(s_skillsound);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else if ((itemId >= 41277 && itemId <= 41292) || (itemId >= 49049 && itemId <= 49064) || (itemId >= 210048 && itemId <= 210063)
							|| (itemId >= 30051 && itemId <= 30054) || (itemId >= 4100156 && itemId <= 4100159) || (itemId >= 3000129 && itemId <= 3000130)
							|| (itemId >= 42650 && itemId <= 42653)) { // 요리아이템
						L1Cooking.useCookingItem(pc, l1iteminstance);
					} else if (itemId >= 41383 && itemId <= 41400) { // 가구
						useFurnitureItem(pc, itemId, itemObjid);
					} else if (itemId > 40169 && itemId < 40226 || itemId >= 45000 && itemId <= 45022
							|| itemId == 3000095) { // 마법서
						useSpellBook(pc, l1iteminstance, itemId);
					} else if (itemId > 40225 && itemId < 40232 || itemId == 5560 || itemId == 3000090 || itemId == 3000089) {
						if (pc.isCrown() || pc.isGm()) {
							if (itemId == 40226 && pc.getLevel() >= 50) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 40228 && pc.getLevel() >= 55) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 40227 && pc.getLevel() >= 60) {
								SpellBook4(pc, l1iteminstance, client);
							} else if ((itemId == 40231) && pc.getLevel() >= 65) {
								SpellBook4(pc, l1iteminstance, client);
							} else if ((itemId == 40232) && pc.getLevel() >= 45) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 40230 && pc.getLevel() >= 70) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 40229 && pc.getLevel() >= 75) {
								SpellBook4(pc, l1iteminstance, client);
							} else if (itemId == 5560 || itemId == 3000090 || itemId == 3000089 && pc.getLevel() >= 80) {
								SpellBook4(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312)); // LV가낮아서
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
						// 정령의 수정
					} else if ((itemId >= 40232 && itemId <= 40264) || (itemId >= 41149 && itemId <= 41153)
							|| itemId == 3000091 || (itemId >= 3000511 && itemId <= 3000513) || itemId == 4100295) {
						useElfSpellBook(pc, l1iteminstance, itemId);
					} else if (itemId > 40264 && itemId < 40280 || itemId == 5559 || itemId == 4100103
							|| itemId == 4100104 || itemId == 4100105) {
						if (pc.isDarkelf() || pc.isGm()) {
							if (itemId >= 40265 && itemId <= 40269 && pc.getLevel() >= 20) {
								SpellBook1(pc, l1iteminstance, client);
							} else if (itemId >= 40270 && itemId <= 40274 && pc.getLevel() >= 40) {
								SpellBook1(pc, l1iteminstance, client);
							} else if (itemId >= 40275 && itemId <= 40279 && pc.getLevel() >= 60) {
								SpellBook1(pc, l1iteminstance, client);
							} else if (itemId == 5559 && pc.getLevel() >= 60) {
								SpellBook1(pc, l1iteminstance, client);
							} else if ((itemId == 4100103 || itemId == 4100104) && pc.getLevel() >= 80) {
								SpellBook1(pc, l1iteminstance, client);
							} else if ((itemId == 4100105) && pc.getLevel() >= 85) {
								SpellBook1(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
							// (원문:어둠 정령의 수정은 다크 에르프만을 습득할 수 있습니다. )
						}
						// 기술서
					} else if (itemId >= 40164 && itemId <= 40166 || itemId >= 41147 && itemId <= 41148
							|| itemId == 3000092 || itemId == 4100100 || itemId == 4100101 || itemId == 4100102) {
						if (pc.isKnight() || pc.isGm()) {
							if (itemId == 40165 && pc.getLevel() >= 50) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 41147 && pc.getLevel() >= 55) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 40164 && pc.getLevel() >= 60) {
								SpellBook3(pc, l1iteminstance, client);
							} else if ((itemId == 40166) && pc.getLevel() >= 65) {
								SpellBook3(pc, l1iteminstance, client);
							} else if ((itemId == 4100100) && pc.getLevel() >= 60) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 4100101 && pc.getLevel() >= 75) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 41148 && pc.getLevel() >= 80) {
								SpellBook3(pc, l1iteminstance, client);
							} else if (itemId == 4100102 || itemId == 3000092 && pc.getLevel() >= 85) {
								SpellBook3(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					} else if ((itemId >= 210020 && itemId <= 210034) || itemId == 4100106) {
						if (pc.isDragonknight() || pc.isGm()) {
							if (itemId >= 210020 && itemId <= 210023 && pc.getLevel() >= 20) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId >= 210024 && itemId <= 210031 && pc.getLevel() >= 40) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId >= 210032 && itemId <= 210034 && pc.getLevel() >= 60) {
								SpellBook5(pc, l1iteminstance, client);
							} else if (itemId == 4100106 && pc.getLevel() >= 80) {
								SpellBook5(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					} else if ((itemId >= 210000 && itemId <= 210019) || itemId == 3000096 || itemId == 4100109 || itemId == 4100449) {
						if (pc.isBlackwizard() || pc.isGm()) {
							if (itemId >= 210000 && itemId <= 210004 && pc.getLevel() >= 15) {
								SpellBook6(pc, l1iteminstance, client);
							} else if (itemId >= 210005 && itemId <= 210009 && pc.getLevel() >= 30) {
								SpellBook6(pc, l1iteminstance, client);
							} else if (itemId >= 210010 && itemId <= 210014 && pc.getLevel() >= 45) {
								SpellBook6(pc, l1iteminstance, client);
							} else if (itemId >= 210015 && itemId <= 210019 && pc.getLevel() >= 60) {
								SpellBook6(pc, l1iteminstance, client);
							} else if (itemId == 4100109 && pc.getLevel() >= 75) {
								SpellBook6(pc, l1iteminstance, client);
							} else if (itemId == 3000096 && pc.getLevel() >= 80) {
								SpellBook6(pc, l1iteminstance, client);
							} else if (itemId == 4100449 && pc.getLevel() >= 85) {
								SpellBook6(pc, l1iteminstance, client);
							} else {
								pc.sendPackets(new S_ServerMessage(312));

							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					} else if (itemId >= 210121 && itemId <= 210132 || itemId == 3000094) { // 전사
						if (pc.is전사()) {
							if (itemId == 210121 && pc.getLevel() >= 30) {
								전사스킬(pc, l1iteminstance, false);
							} else if (itemId == 210122 || itemId == 210129 && pc.getLevel() >= 60) {
								전사스킬(pc, l1iteminstance, false);
							} else if (itemId == 210130 || itemId == 210123 && pc.getLevel() >= 75) {
								전사스킬(pc, l1iteminstance, false);
							} else if (itemId == 210131 || itemId == 210125 && pc.getLevel() >= 80) {
								전사스킬(pc, l1iteminstance, false);
							} else if (itemId == 3000094 && pc.getLevel() >= 80) {
								전사스킬(pc, l1iteminstance, false);
							} else {
								전사스킬(pc, l1iteminstance, true);
							}
						}
					} else {
						int locX = ((L1EtcItem) l1iteminstance.getItem()).get_locx();
						int locY = ((L1EtcItem) l1iteminstance.getItem()).get_locy();
						short mapId = ((L1EtcItem) l1iteminstance.getItem()).get_mapid();
						if (locX != 0 && locY != 0) {
							if (pc.getMap().isEscapable() || pc.isGm()) {
								pc.start_teleport(locX, locY, mapId, pc.getHeading(), 169, true, false);
								pc.getInventory().removeItem(l1iteminstance, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(647));
							}
							cancelAbsoluteBarrier(pc);
						} else {
							if (l1iteminstance.getCount() < 1) {
								pc.sendPackets(new S_ServerMessage(329, l1iteminstance.getLogName()));
							}
						}
					}
					break;
				}
			} else if (l1iteminstance.getItem().getType2() == 1)

			{
				int min = l1iteminstance.getItem().getMinLevel();
				int max = l1iteminstance.getItem().getMaxLevel();
				if (min != 0 && min > pc.getLevel()) {// 이 아이템은%0레벨 이상이 되지 않으면
					pc.sendPackets(new S_ServerMessage(318, String.valueOf(min)));
				} else if (max != 0 && max < pc.getLevel()) {// 이 아이템은%d레벨 이하만
					if (max < 50) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max));
					} else {
						pc.sendPackets(new S_SystemMessage("이 아이템은" + max + "레벨 이하만 사용할 수 있습니다. "));
					}
				} else {
					if (pc.isGm()) {
						UseWeapon(pc, l1iteminstance);
					} else if (pc.isCrown() && l1iteminstance.getItem().isUseRoyal()
							|| pc.isKnight() && l1iteminstance.getItem().isUseKnight()
							|| pc.isElf() && l1iteminstance.getItem().isUseElf()
							|| pc.isWizard() && l1iteminstance.getItem().isUseMage()
							|| pc.isDarkelf() && l1iteminstance.getItem().isUseDarkelf()
							|| pc.isDragonknight() && l1iteminstance.getItem().isUseDragonKnight()
							|| pc.isBlackwizard() && l1iteminstance.getItem().isUseBlackwizard()
							|| pc.is전사() && l1iteminstance.getItem().isUse전사()) {
						UseWeapon(pc, l1iteminstance);
					} else {
						// \f1당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
						pc.sendPackets(new S_ServerMessage(264));

					}
				}
			} else if (l1iteminstance.getItem().getType2() == 2)

			{ // 종별：방어용 기구
				if (pc.isGm()) {
					UseArmor(pc, l1iteminstance);
				} else if (pc.isCrown() && l1iteminstance.getItem().isUseRoyal()
						|| pc.isKnight() && l1iteminstance.getItem().isUseKnight()
						|| pc.isElf() && l1iteminstance.getItem().isUseElf()
						|| pc.isWizard() && l1iteminstance.getItem().isUseMage()
						|| pc.isDarkelf() && l1iteminstance.getItem().isUseDarkelf()
						|| pc.isDragonknight() && l1iteminstance.getItem().isUseDragonKnight()
						|| pc.isBlackwizard() && l1iteminstance.getItem().isUseBlackwizard()
						|| pc.is전사() && l1iteminstance.getItem().isUse전사()) {

					int min = ((L1Armor) l1iteminstance.getItem()).getMinLevel();
					int max = ((L1Armor) l1iteminstance.getItem()).getMaxLevel();
					if (min != 0 && min > pc.getLevel()) {
						// 이 아이템은%0레벨 이상이 되지 않으면 사용할 수 없습니다.
						pc.sendPackets(new S_ServerMessage(318, String.valueOf(min)));
					} else if (max != 0 && max < pc.getLevel()) {
						if (max < 50) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max));
						} else {
							pc.sendPackets(new S_SystemMessage("이 아이템은" + max + "레벨 이하만 사용할 수 있습니다. "));
						}
					} else {
						UseArmor(pc, l1iteminstance);
					}
				} else {
					// \f1당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
					pc.sendPackets(new S_ServerMessage(264));
				}
			}
			// 효과 지연이 있는 경우는 현재 시간을 세트
			if (isDelayEffect) {
				if (itemId == 410008 || itemId == 40414 || itemId == 700012 || itemId == 30043 || itemId == 30045
						|| itemId == 702 || itemId == 30026 || itemId == 4100131 || itemId == 4100132) {
					int chargeCount = l1iteminstance.getChargeCount();
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					l1iteminstance.setChargeCount(l1iteminstance.getChargeCount() - 1);
					if (chargeCount <= 1) {
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						l1iteminstance.setLastUsed(ts);
						pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
						pc.getInventory().saveItem(l1iteminstance, L1PcInventory.COL_CHARGE_COUNT);
					}
				} else {
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					l1iteminstance.setLastUsed(ts);
					pc.getInventory().updateItem(l1iteminstance, L1PcInventory.COL_DELAY_EFFECT);
					pc.getInventory().saveItem(l1iteminstance, L1PcInventory.COL_DELAY_EFFECT);
				}
			}
			L1ItemDelay.onItemUse(pc, l1iteminstance); // 아이템 지연 개시

		}
	}

	@SuppressWarnings("deprecation")
	private void 정상의가호(L1PcInstance pc, L1ItemInstance useItem) {
		if (!pc.is_top_ranker()) {
			pc.sendPackets(String.format("현재 전체 랭킹 %d위 이므로 사용할 수 없습니다.", pc.getRankLevel()));
			pc.getInventory().removeItem(useItem);
			return;
		}

		Calendar currentDate = RealTimeClock.getInstance().getRealTimeCalendar();
		Timestamp lastUsed = pc.getLastTopBless();
//		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 10)) {
			if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * 60 * 60 * 1)) {
			pc.sendPackets(new S_SkillSound(pc.getId(), 12536));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 12536));
			if (pc.hasSkillEffect(L1SkillId.TOP_RANKER)) {
				pc.setSkillEffect(L1SkillId.TOP_RANKER, 600 * 1000);
			} else {
				pc.setSkillEffect(L1SkillId.TOP_RANKER, 600 * 1000);
			}

			pc.setLastTopBless(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (1000 * 60 * 60 * 1)) - currentDate.getTimeInMillis();
//			long i = (lastUsed.getTime() + (1000 * 60 * 10)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			pc.sendPackets(new S_SystemMessage(i / 60000 + "분 동안(" + cal.getTime().getHours() + ":" + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
		}
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			return true;
		} else {
			return false;
		}
	}

	private boolean createGMItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			return true;
		} else {
			return false;
		}
	}

	private boolean createNewItem2(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
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

	private boolean createNewItemTrade(L1PcInstance pc, int item_id, int count, int enchant, int bless, int attr,
			boolean identi) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(enchant);
			item.setAttrEnchantLevel(attr);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else {
				pc.sendPackets(new S_ServerMessage(82)); // 무게 게이지가 부족하거나 인벤토리가
				// 꽉차서 더 들 수 없습니다.
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

	private void AttrEnchant(L1PcInstance pc, L1ItemInstance item, int item_id) {
		int attr_level = item.getAttrEnchantLevel();
		int chance = _random.nextInt(80) + 1;
		int weapon_id = item.getItemId();

		if (L1ItemInstance.pureAttrEnchantLevel(attr_level) >= 5) {
			pc.sendPackets("더 이상 강화할 수 없습니다.");
			return;
		}

		/**
		 * 속성 무기 강화 주문서
		 * */
		if (item_id == 210067) { // 불의 무기 강화 주문서
			if (attr_level == 0) {
				if (chance < 8) { // 30
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(1);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 1) {
				if (chance < 4) { // 20
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(2);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 2) {
				if (chance < 4) { // 10
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(3);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 3) {
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 7
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(4);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 4) {
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 4
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(5);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 14) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
		} else if (item_id == 210066) { // 물의 무기 강화 주문서
			if (attr_level == 0) {
				if (chance < 8) { // 30
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(6);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 6) {
				if (chance < 4) { // 20
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(7);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 7) {
				if (chance < 4) { // 10
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(8);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 8) {
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 7
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(9);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 9) {
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 4
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(10);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 10) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
		} else if (item_id == 210064) { // 바람의 무기 강화 주문서
			if (attr_level == 0) {
				if (chance < 8) { // 30
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(11);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 11) {
				if (chance < 4) { // 20
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(12);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 12) {
				if (chance < 4) { // 10
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(13);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 13) {// 9이하인챈 3단계
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 7
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(14);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 14) {// 10같거나 이상 4단계
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 4
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(15);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 15) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
		} else if (item_id == 210065) { // 땅의 무기 강화 주문서
			if (attr_level == 0) {
				if (chance < 8) { // 30
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(16);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 16) {
				if (chance < 4) { // 20
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(17);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 17) {
				if (chance < 4) { // 10
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
					item.setAttrEnchantLevel(18);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
				}
			} else if (attr_level == 18) {
				if (item.getEnchantLevel() >= 9 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 7
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(19);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 19) {
				if (item.getEnchantLevel() >= 10 || is_legend_weapon(weapon_id)) {
					if (chance < 4) { // 4
						pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
						item.setAttrEnchantLevel(20);
					} else {
						pc.sendPackets(new S_ServerMessage(1411, item.getLogName()));
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else if (attr_level == 20) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
		}
		pc.getInventory().consumeItem(item_id, 1);
		pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
	}

	public void AttrChangeEnchant(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int attr_level = item.getAttrEnchantLevel();
		int AttrScroll = 0;

		if (itemId == 560030) { // 불의 속성(화령의 속성 변환 주문서)
			AttrScroll = 0;
		} else if (itemId == 560031) { // 물의 속성(수령의 속성 변환 주문서)
			AttrScroll = 5;
		} else if (itemId == 560032) { // 바람의 속성(풍령의 속성 변환 주문서)
			AttrScroll = 10;
		} else if (itemId == 560033) { // 땅의 속성(지령의 속성 변환 주문서)
			AttrScroll = 15;
		} else {
			pc.sendPackets(new S_ServerMessage(79));
			// 아무일도 일어나지 않았습니다.
			return;
		}
		if (!pc.getInventory().checkItem(itemId, 1)) {
			return;
		}
		if (attr_level > 0) {
			if (AttrScroll + 1 <= attr_level && attr_level <= AttrScroll + 5) {
				pc.sendPackets(new S_ServerMessage(3319));
				// 동일한 속성에는 사용하실 수없습니다.
				return;
			}
			if (attr_level % 5 == 0) {
				pc.sendPackets(new S_ServerMessage(3296, item.getLogName()));
				// 인챈트: %0에 찬란한 대자연의 힘이 스며듭니다.
				item.setAttrEnchantLevel(AttrScroll + 5);
			} else {
				pc.sendPackets(new S_ServerMessage(1410, item.getLogName()));
				// 인챈트: %0에 영롱한 대자연의 힘이 스며듭니다.
				item.setAttrEnchantLevel(attr_level % 5 + AttrScroll);
			}
			pc.getInventory().consumeItem(itemId, 1);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		} else {
			pc.sendPackets(new S_ServerMessage(79)); // 아무일도 일어나지 않았습니다.
		}
	}

	private void enchant_insignia(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_enchant = target.getEnchantLevel();
		if (target.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}
		if (target_enchant < 1) {
			pc.sendPackets(String.format("휘장보호주문서는 +1부터 사용이 가능합니다."));
			return;
		}
		if (target_enchant >= Config.Insignia_Level) {
			pc.sendPackets(String.format("휘장은 +%d 이상 보호주문서로 인챈트할 수 없습니다.", Config.Insignia_Level));
			pc.sendPackets("현재는 인챈트가 제한중 입니다.");
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.Insignia.length ? Config.Insignia.length - 1 : target_enchant;
		if (MJRnd.isWinning(100, Config.Insignia[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
		} else {
			pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	/** 빛나는 아덴 용사의 방어구류 **/
	private void enchant_should(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_enchant = target.getEnchantLevel();
		if (target.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}
		if (target_enchant < 0) {
			pc.sendPackets(String.format("빛나는 아덴 용사 보호주문서는 +0부터 사용이 가능합니다."));
			return;
		}
		if (target_enchant >= Config.Should_Level) {
			pc.sendPackets(String.format("빛나는 아덴 용사 방어구 +%d 이상 보호주문서로 인챈트할 수 없습니다.", Config.Should_Level));
			pc.sendPackets("현재는 인챈트 제한중 입니다.");
			return;
		}

		int index = target_enchant < 0 ? 0 : target_enchant >= Config.Should.length ? Config.Should.length - 1 : target_enchant;
		if (MJRnd.isWinning(100, Config.Should[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
		} else {
			pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_sentence(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_enchant = target.getEnchantLevel();
		if (target.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}
		if (target_enchant < 1) {
			pc.sendPackets(String.format("문장보호주문서는 +1부터 사용이 가능합니다."));
			return;
		}
		if (target_enchant >= Config.SentenceLevel) {
			pc.sendPackets(String.format("문장은 +%d 이상 보호주문서로 인챈트할 수 없습니다.", Config.SentenceLevel));
			pc.sendPackets("현재는 인챈트 제한중 입니다.");
			return;
		}

		int index = target_enchant < 0 ? 0 : target_enchant >= Config.Sentence.length ? Config.Sentence.length - 1 : target_enchant;
		if (MJRnd.isWinning(100, Config.Sentence[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
		} else {
			pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_roomtis(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_type = target.getItem().getType();
		int target_enchant = target.getEnchantLevel();
		if (target_type < 8 || target_type > 12 || target.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}
		if (target_enchant < 1) {
			pc.sendPackets(String.format("룸티스보호주문서는 +1부터 사용이 가능합니다."));
			return;
		}
		if (target_enchant >= Config.RoomtisLevel) {
			pc.sendPackets(String.format("룸티스류 악세사리는 +%d 이상 보호주문서로 인챈트할 수 없습니다.", Config.RoomtisLevel));
			pc.sendPackets("현재는 인챈트가 제한중 입니다.");
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.Roomtis.length ? Config.Roomtis.length - 1 : target_enchant;
		if (MJRnd.isWinning(100, Config.Roomtis[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
		} else {
			pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
		}
		pc.getInventory().removeItem(scroll, 1);
	}

	private void enchant_snapper(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
		int target_type = target.getItem().getType();
		int target_enchant = target.getEnchantLevel();
		if (target_type < 8 || target_type > 12 || target.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(79));
			return;
		}
		if (target_enchant < 1) {
			pc.sendPackets(String.format("스냅퍼보호주문서는 +1부터 사용이 가능합니다."));
			return;
		}
		if (target_enchant >= Config.SnapperLevel) {
			pc.sendPackets(String.format("스냅퍼류 악세사리는 +%d 이상 보호주문서로 인챈트할 수 없습니다.", Config.SnapperLevel));
			pc.sendPackets("현재 인챈트 제한중 입니다.");
			return;
		}

		int index = target_enchant < 0 ? 0
				: target_enchant >= Config.Sanpper.length ? Config.Sanpper.length - 1 : target_enchant;
		if (MJRnd.isWinning(100, Config.Sanpper[index])) {
			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
			pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
		} else {
			pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
		}
		pc.getInventory().removeItem(scroll, 1);
	}

//	private void enchant_bless_orim(L1PcInstance pc, L1ItemInstance scroll, L1ItemInstance target) {
//		int target_type = target.getItem().getType();
//		int target_enchant = target.getEnchantLevel();
//		if (target_type < 8 || target_type > 12 || target.getBless() >= 128) {
//			pc.sendPackets(new S_ServerMessage(79));
//			return;
//		}
//		if (target_enchant >= Config.악세사리) {
//			pc.sendPackets(String.format("악세사리는 +%d 이상은 인챈할 수 없습니다.", Config.악세사리));
//			pc.sendPackets("제한해제시 공지 하겠습니다.");
//			return;
//		}
//
//		int index = target_enchant < 0 ? 0
//				: target_enchant >= Config.bless_orim.length ? Config.bless_orim.length - 1 : target_enchant;
//		if (MJRnd.isWinning(100, Config.bless_orim[index])) {
//			SuccessEnchant(pc, target, pc.getNetConnection(), +1);
//			pc.sendPackets(new S_SystemMessage("순간 강렬하게 빛을내어 인챈트에 성공 하였습니다."));
//		} else {
//			pc.sendPackets(new S_SystemMessage("한 순간 강렬하게 빛났지만 인챈트에 실패 하였습니다."));
//		}
//		pc.getInventory().removeItem(scroll, 1);
//	}

	public static void SuccessEnchant(L1PcInstance pc, L1ItemInstance item, GameClient client, int i) {
		String s = "";
		String sa = "";
		String sb = "";
		String s1 = item.getName();
		String pm = "";
		if (item.getEnchantLevel() > 0) {
			pm = "+";
		}
		if (item.getItem().getType2() == 1) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
				case -1:
					s = s1;
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					s = s1;
					sa = "$245";
					sb = "$247";
					break;

				case 2: // '\002'
					s = s1;
					sa = "$245";
					sb = "$248";
					break;

				case 3: // '\003'
					s = s1;
					sa = "$245";
					sb = "$248";
					break;
				}
			} else {
				switch (i) {
				case -1:
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$245";
					sb = "$247";
					break;

				case 2: // '\002'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$245";
					sb = "$248";
					break;

				case 3: // '\003'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$245";
					sb = "$248";
					break;
				}
			}
		} else if (item.getItem().getType2() == 2) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
				case -1:
					s = s1;
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					s = s1;
					sa = "$252";
					sb = "$247 ";
					break;

				case 2: // '\002'
					s = s1;
					sa = "$252";
					sb = "$248 ";
					break;

				case 3: // '\003'
					s = s1;
					sa = "$252";
					sb = "$248 ";
					break;
				}
			} else {
				switch (i) {
				case -1:
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$252";
					sb = "$247 ";
					break;

				case 2: // '\002'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$252";
					sb = "$248 ";
					break;

				case 3: // '\003'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$252";
					sb = "$248 ";
					break;
				}
			}
		}
		pc.sendPackets(new S_ServerMessage(161, s, sa, sb));
		int oldEnchantLvl = item.getEnchantLevel();
		int newEnchantLvl = item.getEnchantLevel() + i;
		int safe_enchant = item.getItem().get_safeenchant();
		item.setEnchantLevel(newEnchantLvl);
		client.getActiveChar().getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
		pc.saveInventory();
		if (newEnchantLvl > safe_enchant) {
			client.getActiveChar().getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.saveInventory();
			/** 로그파일저장 **/
			LoggerInstance.getInstance().addEnchant(pc, item, true);
		}

		if (Config.MASTERENCHANTMESS && newEnchantLvl >= 10) {
			String message = String.format("어느 아덴 용사 님께서 +%d %s 인챈트에 성공하였습니다.", oldEnchantLvl, item.getName());
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
		}

		if (item.getItem().getType2() == 1 && Config.LOGGING_WEAPON_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LOGGING_WEAPON_ENCHANT) {
			}
		}
		if (item.getItem().getType2() == 2 && Config.LOGGING_ARMOR_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LOGGING_ARMOR_ENCHANT) {
			}
		}

		if (item.getItem().getType2() == 2) {
			if (item.isEquipped()) {
				if (item.getItem().getType() >= 8 && item.getItem().getType() <= 12) {
				} else {
					pc.getAC().addAc(-i);
				}
				int i2 = item.getItem().getItemId();
				if (i2 == 20011 || i2 == 20110 || i2 == 120011 || i2 == 22204 || i2 == 22223 || i2 == 22205
						|| i2 == 22206 || i2 == 22207 || i2 == 22213 || i2 == 22365 || i2 == 120110 || i2 == 93001
						|| i2 >= 222300 && i2 <= 222303 || i2 == 222328 || i2 == 111137 || i2 == 111141 || i2 == 111140) { //린드 용갑, 할파스 MR
					pc.getResistance().addMr(i);
					pc.sendPackets(new S_SPMR(pc));
				}
				if (i2 == 20056 || i2 == 120056 || i2 == 220056 || i2 == 93002 || i2 == 20074 || i2 == 120074) { // 매직
					// 클로크
					pc.getResistance().addMr(i * 2);
					pc.sendPackets(new S_SPMR(pc));
				}
				if (i2 == 20079 || i2 == 20078) {
					pc.getResistance().addMr(i * 3);
					pc.sendPackets(new S_SPMR(pc));
				}
			}

			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	private void FailureEnchant(L1PcInstance pc, L1ItemInstance item, GameClient client) {
		String s = "";
		String sa = "";
		int itemType = item.getItem().getType2();
		int itemId = item.getItem().getItemId();
		String nameId = item.getName();
		String pm = "";

		if (itemType == 1) { // 무기
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				s = nameId;
				// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				sa = "$245";
			} else {
				if (item.getEnchantLevel() > 0) {
					pm = "+";

				}
				s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(nameId).toString();
				// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				sa = "$245";
			}
		} else if (itemType == 2) { // 방어용 기구
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				s = nameId;
				// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				sa = " $252";
			} else {
				if (item.getEnchantLevel() > 0) {
					pm = "+";
				}
				s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(" ").append(nameId).toString();
				// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				sa = " $252";
			}
		}
		// if ((itemId >= 1115 && itemId <= 1118) || (itemId >= 22250 && itemId
		// <= 22252)) {//신묘한 날자에 러쉬부분?
		if ((itemId >= 1115 && itemId <= 1118)) {

			pc.sendPackets(new S_ServerMessage(1310));
			pc.getInventory().setEquipped(item, false);
			item.setEnchantLevel(0);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.saveInventory();
			if (itemType == 1) {
			} else if (itemType == 2) {
			}
		} else {
			pc.getInventory().setEquipped(item, false);
			pc.sendPackets(new S_ServerMessage(164, s, sa));
			pc.getInventory().removeItem(item, item.getCount());
			/** 로그파일저장 **/
			LoggerInstance.getInstance().addEnchant(pc, item, false);
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private void UseExpPotion1(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698, ""));
			return;
		}
		cancelAbsoluteBarrier(pc);

		int time = 1800;

		pc.setSkillEffect(L1SkillId.EXP_BUFF, time * 1000);
		S_SkillSound s = new S_SkillSound(pc.getId(), 10049);
		pc.sendPackets(s, false);
		pc.broadcastPacket(s);
		long hasad = pc.getAccount().getBlessOfAin();
		if (hasad < 10000) {
			pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.EXP_BUFF + 1, 5087, true)); // 세이프티존
		} else{
			pc.sendPackets(S_InventoryIcon.icoNew(L1SkillId.EXP_BUFF, 5087, time, true)); // 사냥터
		}
		pc.sendPackets(new S_ServerMessage(5087));
	}

	private void UseExpPotion(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698, ""));
			// 마력에 의해 아무것도 마실 수가 없습니다.
			return;
		}
		cancelAbsoluteBarrier(pc);

		if (item_id != 210094 && item_id != 30105 && item_id != 3000456)
			return;
		int time = 1800;

		pc.setSkillEffect(L1SkillId.EXP_POTION, time * 1000);
		S_SkillSound s = new S_SkillSound(pc.getId(), 11308);
		pc.sendPackets(s, false);
		pc.broadcastPacket(s);
		if (pc.getMap().isSafetyZone(pc.getX(), pc.getY()))
			pc.sendPackets(S_InventoryIcon.iconNewUnLimit(EXP_POTION + 1, 4973, true)); // 세이프티존
		
		else
			pc.sendPackets(S_InventoryIcon.icoNew(EXP_POTION, 4973, time, true)); // 사냥터
		pc.sendPackets(new S_ServerMessage(2116)); //40
	}

	private void useGreenPotion(L1PcInstance pc, int itemId) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698)); // \f1마력에 의해 아무것도 마실 수가
			// 없습니다.
			return;
		}

		// 아브소르트바리아의 해제
		cancelAbsoluteBarrier(pc);

		int time = 0;
		if (itemId == L1ItemId.POTION_OF_HASTE_SELF || itemId == 7006) { // 그린
			time = 300;
		} else if (itemId == L1ItemId.B_POTION_OF_HASTE_SELF) { // 축복된 그린 일부
			time = 350;
		} else if (itemId == 40018 || itemId == 41342) { // 강화 그린 일부, 축복된 와인,
			time = 1800;
		} else if (itemId == 140018 || itemId == 41338 || itemId == 30158) { // 축복된
			time = 2250;
		} else if (itemId == 40039) { // 와인
			time = 600;
		} else if (itemId == 40040) { // 위스키
			time = 900;
		} else if (itemId == 30067) {
			time = 300;
		} else if (itemId == 40030) { // 상아의 탑의 헤이 파업 일부
			time = 1800;
		} else if (itemId == 41261 || itemId == 41262 || itemId == 41268 || itemId == 41269 || itemId == 41271
				|| itemId == 41272 || itemId == 41273) {
			time = 30;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), 191));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 191));
		pc.sendPackets(new S_ServerMessage(183));
		// XXX:헤이스트아이템 장비시, 취한 상태가 해제되는지 불명
		if (pc.getHasteItemEquipped() > 0) {
			return;
		}
		// 취한 상태를 해제
		pc.setDrink(false);

		// 헤이 파업, 그레이터 헤이 파업과는 중복 하지 않는다
		if (pc.hasSkillEffect(HASTE)) {
			pc.killSkillEffectTimer(HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(GREATER_HASTE)) {
			pc.killSkillEffectTimer(GREATER_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			pc.setMoveSpeed(0);
		} else if (pc.hasSkillEffect(STATUS_HASTE)) {
			int currentTime = 0;
			currentTime = pc.getSkillEffectTimeSec(STATUS_HASTE);
			if (currentTime >= 7200) {
				time = 7200;
			} else {
				time += currentTime;
			}
		}

		// 슬로우, 그레이터 슬로우, 엔탕르중은 슬로우 상태를 해제할 뿐
		if (pc.hasSkillEffect(SLOW)) { // 슬로우
			pc.killSkillEffectTimer(SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
		} else {
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, time >= 7200 ? 7200 : time));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
			pc.setMoveSpeed(1);
			pc.setSkillEffect(STATUS_HASTE, (time >= 7200 ? 7200 : time) * 1000);
		}
	}

	private void useBravePotion(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698));// \f1마력에 의해 아무것도 마실 수가
			// 없습니다.
			return;
		}
		// 아브소르트바리아의 해제
		cancelAbsoluteBarrier(pc);

		int time = 0;
		if (item_id == L1ItemId.POTION_OF_EMOTION_BRAVERY || item_id == 30073) { // 치우침
																					// 이브
																					// 일부
			time = 300;
		} else if (item_id == L1ItemId.B_POTION_OF_EMOTION_BRAVERY) { // 축복된
																		// 치우침이브
																		// 일부
			time = 350;
		} else if (item_id == 41415) { // 강화 치우침 이브 일부
			time = 1800;
		} else if (item_id == 712) {
			time = 1800;
		} else if (item_id == 40068 || item_id == 30076) { // 에르브왓훌
			time = 480;
			if (pc.hasSkillEffect(STATUS_BRAVE)) { // 용기와는 중복 하지 않는다.
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			pc.remove_elf_second_brave();
		} else if (item_id == 140068) { // 축복된 에르브왓훌
			time = 700;
			if (pc.hasSkillEffect(STATUS_BRAVE)) { // 용기 효과와는 중복 하지 않는다.
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			pc.remove_elf_second_brave();
		} else if (item_id == 210110) { // 복지 엘븐와퍼
			time = 1800;
			if (pc.hasSkillEffect(STATUS_BRAVE)) { // 용기 효과와는 중복 하지 않는다.
				pc.killSkillEffectTimer(STATUS_BRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
		} else if (item_id == 40031 || item_id == 30075) { // 이비르브랏드
			time = 600;
		} else if (item_id == 40733) { // 명예의 코인
			time = 600;
			if (pc.hasSkillEffect(STATUS_ELFBRAVE)) {
				pc.killSkillEffectTimer(STATUS_ELFBRAVE);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(HOLY_WALK)) { // 호-리 워크와는 중복 하지 않는다
				pc.killSkillEffectTimer(HOLY_WALK);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(MOVING_ACCELERATION)) { // 무빙 악 세레이션과는 중복 하지
															// 않는다
				pc.killSkillEffectTimer(MOVING_ACCELERATION);
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
				pc.setBraveSpeed(0);
			}
			if (pc.hasSkillEffect(STATUS_FRUIT)) { // 유그드라열매와는 중복안됨
				pc.killSkillEffectTimer(STATUS_FRUIT);
				pc.setBraveSpeed(0);
			}
		}

		if (item_id == 40068 || item_id == 140068 || item_id == 210110 || item_id == 30076) { // 엘븐
																								// 와퍼
			pc.sendPackets(new S_SkillBrave(pc.getId(), 3, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
			pc.setSkillEffect(STATUS_ELFBRAVE, time * 1000);
		} else {
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, time));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
			pc.setSkillEffect(STATUS_BRAVE, time * 1000);
		}
		pc.sendPackets(new S_SkillSound(pc.getId(), 751));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 751));
		pc.setBraveSpeed(1);
	}

	private void useDragonPearl(L1PcInstance pc, int itemId) {// 드래곤의 진주
		if (pc.hasSkillEffect(DECAY_POTION) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698));
			return;
		}
		cancelAbsoluteBarrier(pc);
		int time = (10 * 60 * 1000) + 1000;
		if (pc.hasSkillEffect(STATUS_DRAGON_PEARL)) {
			pc.killSkillEffectTimer(STATUS_DRAGON_PEARL);
			pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, 0));
			pc.sendPackets(new S_Liquor(pc.getId(), 0));
			pc.setPearl(0);
		}
		pc.sendPackets(new S_ServerMessage(1065));
		pc.sendPackets(new S_SkillSound(pc.getId(), 197));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 197));

		pc.sendPackets(new S_SkillSound(pc.getId(), 15355));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 15355));
		pc.setSkillEffect(STATUS_DRAGON_PEARL, time);
		pc.sendPackets(new S_Liquor(pc.getId(), 8));
		pc.broadcastPacket(new S_Liquor(pc.getId(), 8));
		pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, 151));
		pc.setPearl(1);
	}

	private void useFruit(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(DECAY_POTION) == true) {
			pc.sendPackets(new S_ServerMessage(698));
			return;
		}
		cancelAbsoluteBarrier(pc);
		int time = 0;
		if (item_id == 210036 || item_id == 30077) {
			time = 480;
		}

		if (item_id == 713) {
			time = 1800;
		}		
		if (pc.hasSkillEffect(L1SkillId.STATUS_BRAVE)) {
			pc.killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
			pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
			pc.setBraveSpeed(0);

		} else if (pc.hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
			pc.killSkillEffectTimer(L1SkillId.STATUS_FRUIT);
			pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
			pc.setBraveSpeed(0);

		}

		if (pc.isPassive(MJPassiveID.DARK_HORSE.toInt())){ 
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, time));
			pc.setSkillEffect(L1SkillId.STATUS_BRAVE, time * 1000);
			pc.setBraveSpeed(1);

		} else {
		    pc.sendPackets(new S_SkillBrave(pc.getId(), 4, time));
			pc.setSkillEffect(L1SkillId.STATUS_FRUIT, time * 1000);
			pc.setBraveSpeed(4);

		}
		pc.sendPackets(new S_SkillSound(pc.getId(), 7110));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 7110));

	}

	private void useBluePotion(L1PcInstance pc, int itemId) {
		if (pc.hasSkillEffect(DECAY_POTION)) {// 디케이 포션
			pc.sendPackets(new S_ServerMessage(698));
			return;
		}
		cancelAbsoluteBarrier(pc);// 앱솔루트 해제
		int time = 0;
		switch (itemId) {
		case 30083:
		case 40015:
		case 40736:
			time = 600;
			break;
		case 140015:
			time = 700;
			break;
		case 41142:
			time = 300;
			break;
		case 210114:
			time = 1800;
			break;
		default:
			break;
		}
		pc.sendPackets(new S_SkillIconGFX(34, time, true));
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		if (itemId == 41142) {
			pc.setSkillEffect(STATUS_BLUE_POTION2, time * 1000);
		} else {
			pc.setSkillEffect(STATUS_BLUE_POTION, time * 1000);
		}
		pc.sendPackets(new S_ServerMessage(1007));// MP 회복 속도가 빨라집니다.
	}

	private void useWisdomPotion(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698)); // \f1마력에 의해 아무것도 마실 수가
			// 없습니다.
			return;
		}
		// 아브소르트바리아의 해제
		cancelAbsoluteBarrier(pc);

		int time = 0; // 시간은 4의 배수로 하는 것
		switch (item_id) {
		case 40016:
		case 30089:
			time = 300;
			break;
		case 140016:
			time = 360;
			break;
		case 210113:
			time = 1000;
			break;
		default:
			return;
		}

		if (!pc.hasSkillEffect(STATUS_WISDOM_POTION)) {
			pc.getAbility().addSp(2);
			pc.addMpr(2);
		}
		pc.sendPackets(new S_SkillIconWisdomPotion((int) (time)));
		pc.sendPackets(new S_SkillSound(pc.getId(), 750));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
		pc.setSkillEffect(STATUS_WISDOM_POTION, time * 1000);

	}

	private void useBlessOfEva(L1PcInstance pc, int item_id) {
		if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
			pc.sendPackets(new S_ServerMessage(698)); // \f1마력에 의해 아무것도 마실 수가
			// 없습니다.
			return;
		}

		// 아브소르트바리아의 해제
		cancelAbsoluteBarrier(pc);

		int time = 0;
		switch (item_id) {
		case 210115:// 농축 호흡의 물약
			time = 7200;
			break;
		case 40032:// 에바의축복
			time = 1800;
			break;
		case 40041:
			time = 300;
			break;
		case 41344:
			time = 2100;
			break;
		default:
			return;
		}
		if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
			int timeSec = pc.getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
			time += timeSec;
			if (time > 3600) {
				time = 3600;
			}
		}
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time));
		pc.sendPackets(new S_SkillSound(pc.getId(), 190));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
		pc.setSkillEffect(STATUS_UNDERWATER_BREATH, time * 1000);
	}

	private void useBlindPotion(L1PcInstance pc) {
		if (pc.hasSkillEffect(DECAY_POTION)) {
			pc.sendPackets(new S_ServerMessage(698)); // \f1마력에 의해 아무것도 마실 수가
			// 없습니다.
			return;
		}

		// 아브소르트바리아의 해제
		cancelAbsoluteBarrier(pc);

		int time = 480;
		if (pc.hasSkillEffect(CURSE_BLIND)) {
			pc.killSkillEffectTimer(CURSE_BLIND);
		} else if (pc.hasSkillEffect(DARKNESS)) {
			pc.killSkillEffectTimer(DARKNESS);
		} else if (pc.hasSkillEffect(LINDBIOR_SPIRIT_EFFECT)) {
			pc.killSkillEffectTimer(LINDBIOR_SPIRIT_EFFECT);
		}

		if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
			pc.sendPackets(new S_CurseBlind(2));
		} else {
			pc.sendPackets(new S_CurseBlind(1));
		}

		pc.setSkillEffect(CURSE_BLIND, time * 1000);
	}

	private void useCashScroll(L1PcInstance pc, int item_id) {
		int time = 1800;
		int scroll = 0;

		if (pc.hasSkillEffect(STATUS_CASHSCROLL)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL);
			pc.addMaxHp(-50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL2)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL2);
			pc.addMaxMp(-40);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL3)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL3);
			pc.addDmgup(-3);
			pc.addHitup(-3);
			pc.getAbility().addSp(-3);
			pc.sendPackets(new S_SPMR(pc));
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL4)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL4);
			pc.getAbility().addSp(-3);
			pc.addBaseMagicHitUp(-5);
			pc.getResistance().addcalcPcDefense(-3);
			pc.sendPackets(new S_SPMR(pc));
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL5)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL5);
			pc.addBowDmgup(-3);
			pc.addBowHitup(-5);
			pc.getResistance().addcalcPcDefense(-3);
		}
		if (pc.hasSkillEffect(STATUS_CASHSCROLL6)) {
			pc.killSkillEffectTimer(STATUS_CASHSCROLL6);
			pc.addDmgRate(-3);
			pc.addHitup(-5);
			pc.getResistance().addcalcPcDefense(-3);
		}

		if (item_id == 410010) { // 마력 증강 주문서
			scroll = 7893;
			pc.addMaxHp(50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				// TODO 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		} else if (item_id == 410011) { 
			scroll = 7894;
			pc.addMaxMp(40);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		} else if (item_id == 410012 || item_id == 30063) {
			scroll = 7895;
			pc.addDmgup(3);
			pc.addHitup(3);
			pc.getAbility().addSp(3);
			pc.sendPackets(new S_SPMR(pc));
		} else if (item_id == 4100039) {
			scroll = 16553;
			pc.getAbility().addSp(3);
			pc.addBaseMagicHitUp(5);
			pc.getResistance().addcalcPcDefense(3);
			pc.sendPackets(new S_SPMR(pc));
		} else if (item_id == 4100041) {
			scroll = 16552;
			pc.addBowDmgup(3);
			pc.addBowHitup(5);
			pc.getResistance().addcalcPcDefense(3);
		} else if (item_id == 4100042) {
			scroll = 16551;
			pc.addDmgRate(3);
			pc.addHitup(5);
			pc.getResistance().addcalcPcDefense(3);
		}
		pc.sendPackets(new S_SkillSound(pc.getId(), scroll));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), scroll));
		pc.setSkillEffect(scroll, time * 1000);
	}

	private boolean usePolyScroll(L1PcInstance pc, int item_id, String s) {
//		 System.out.println(s);//변신 액션찾을때
		int time = 0;
		boolean ring = false;
		switch (item_id) {
		case 40088:
		case 40096:
			time = 1800;
			break;
		case 140088:
			time = 2100;
			break;
		case 210112:
			time = 3600;
			break;
		case 40008:
			time = 7200;
			break;
		case 900075://변신 지배반지
		case 140008:
			time = 7200;
			break;
		default:
			return false;
		}
		if (s.startsWith("maple")) {
			String aa = s;
			String bb = aa.replace("maple ", "");
			s = bb;
		}
		if (s.equalsIgnoreCase("ranking class polymorph")) {
			if (pc.isCrown()) {
				if (pc.get_sex() == 0)
					s = "rangking prince male";
				else
					s = "rangking prince female";
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0)
					s = "rangking knight male";
				else
					s = "rangking knight female";
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0)
					s = "rangking elf male";
				else
					s = "rangking elf female";
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0)
					s = "rangking wizard male";
				else
					s = "rangking wizard female";
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0)
					s = "rangking darkelf male";
				else
					s = "rangking darkelf female";
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0)
					s = "rangking dragonknight male";
				else
					s = "rangking dragonknight female";
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0)
					s = "rangking illusionist male";
				else
					s = "rangking illusionist female";
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0)
					s = "rangking warrior male";
				else
					s = "rangking warrior female";
			}
		}
		if (s.equalsIgnoreCase("ranking class polymorph2")) {
			if (pc.isCrown()) {
				if (pc.get_sex() == 0)
					s = "myth prince male";
				else
					s = "myth prince female";
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0)
					s = "myth knight male";
				else
					s = "myth knight female";
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0)
					s = "myth elf male";
				else
					s = "myth elf female";
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0)
					s = "myth magition male";
				else
					s = "myth magition female";
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0)
					s = "myth darkelf male";
				else
					s = "myth darkelf female";
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0)
					s = "myth dragonknight male";
				else
					s = "myth dragonknight female";
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0)
					s = "myth illusionist male";
				else
					s = "myth illusionist female";
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0)
					s = "myth warrior male";
				else
					s = "myth warrior female";
			}
		}

		if (s.startsWith("rangking ") || s.startsWith("myth ")) {
			if (!MJRankUserLoader.getInstance().isRankPoly(pc)) {				
				return false;
			}
		}
		if (s.startsWith("maple")) {
			if (!pc.isPolyRingMaster())
				return false;
		}

		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);

		if (poly != null || s.equals("")) {
			if (s.equals("")) {
				int spriteId = pc.getCurrentSpriteId();
				if (spriteId == 6034 || spriteId == 6035) {
					return true;
				} else {
					if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)) {
						pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
					} else if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)) {
						pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
					}
					else {
						pc.removeSkillEffect(SHAPE_CHANGE);
					}
					return true;
				}
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
				/*if ((item_id == 40008 || item_id == 140008 || item_id == 40410) && pc.isPolyRingMaster()) {
					ring = true;
				}*/
				if(item_id == 900075){//변신 지배반지
					ring = true;
				}
				
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC, ring);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private void usePolyScale(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 41154) { // 어둠의 비늘
			polyId = 3101;
		} else if (itemId == 41155) { // 열화의 비늘
			polyId = 3126;
		} else if (itemId == 41156) { // 배덕자의 비늘
			polyId = 3888;
		} else if (itemId == 41157) { // 증오의 비늘
			polyId = 3784;
		}
		L1PolyMorph.doPoly(pc, polyId, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
	}

	private void usePolyScale2(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 220001) { // 싸이변신
			polyId = 11232;
		} else if (itemId == 220002) { // 싸이변신
			polyId = 11234;
		} else if (itemId == 220003) { // 싸이변신
			polyId = 11236;
		} else if (itemId == 4106285) {// 진 84쉐도우 변신 주문서
			polyId = 15868;
		} else if (itemId == 4106286) {// 진 84랜스마스터 변신 주문서
			polyId = 15539;
		} else if (itemId == 4106287) {// 진 84바포메트 변신 주문서
			polyId = 15550;
		} else if (itemId == 4106288) {// 진 84경비병(활) 변신 주문서
			polyId = 13635;
		} else if (itemId == 4106284) {// 진 84데스나이트 변신 주문서
			polyId = 13152;
		} else if (itemId == 4107285) {// 진 82쉐도우 변신 주문서
			polyId = 11389;
		} else if (itemId == 4107286) {// 진 82랜스마스터 변신 주문서
			polyId = 15537;
		} else if (itemId == 4107287) {// 진 82바포메트 변신 주문서
			polyId = 15548;
		} else if (itemId == 4107288) {// 진 82경비병(활) 변신 주문서
			polyId = 13631;
		} else if (itemId == 4107284) {// 진 82데스나이트 변신 주문서
			polyId = 13153;
		}
		L1PolyMorph.doPoly(pc, polyId, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
	}

	private void usePolyScale3(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 4100023) { // 암흑성의 커츠 영웅 변신 주문서
			polyId = 16014;
		} else if (itemId == 4100024) { // 암흑성 슬레이브 영웅 변신 주문서
			polyId = 15986;
		} else if (itemId == 4100025) { // 암흑성 아이리스 영웅 변신 주문서
			polyId = 16008;
		} else if (itemId == 4100026) { // 암흑성 헬바인 영웅 변신 주문서
			polyId = 16002;
		} else if (itemId == 4100027) { // 암흑성 하딘 영웅 변신 주문서
			polyId = 16027;
		} else if (itemId == 4100028) { // 수호성 군터 영웅 변신 주문서
			polyId = 16284;
		} else if (itemId == 4100029) { // 수호성 블루디카 영웅 변신 주문서
			polyId = 16053;
		} else if (itemId == 4100030) { // 수호성 프로켈 영웅 변신 주문서
			polyId = 16056;
		} else if (itemId == 4100031) { // 수호성 질리언 영웅 변신 주문서
			polyId = 16074;
		} else if (itemId == 4100032) { // 수호성 조우 영웅 변신 주문서
			polyId = 16040;
		} else if (itemId == 4105284) {// 진 데스나이트 변신 주문서
			polyId = 17541;
		} else if (itemId == 4105285) {// 진 쉐도우 변신 주문서
			polyId = 17531;
		} else if (itemId == 4105286) {// 진 랜스마스터 변신 주문서
			polyId = 17545;
		} else if (itemId == 4105287) {// 진 바포메트 변신 주문서
			polyId = 17515;
		} else if (itemId == 4105288) {// 진 경비병(활) 변신 주문서
			polyId = 17535;
		} else if (itemId == 4105289) {// 진 경비병(창) 변신 주문서
			polyId = 17549;
		}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
	}


	private void usePolyScale4(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 4100160) { // 전설 변신권
			polyId = 11685;
		} else if (itemId == 4100161) { // 신화 변신권
			polyId = 12015;
		} else if (itemId == 4100162) { // 전설 변신권
			polyId = 11621;
			pc.sendPackets("무기를 해제 하시면 속도가 하향 됩니다.");
		} else if (itemId == 4100163) { // 신화 변신권
			polyId = 11620;
			pc.sendPackets("무기를 해제 하시면 속도가 하향 됩니다.");
		}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
	}
	
	// 리니지M 변신 카드
	private void usePolyScale5(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 220004) { // 리니지M 드래곤 슬레이어 변신 카드
			polyId = 14491;
		} else if (itemId == 220005) { // 리니지M 나이트 슬레이셔 변신 카드
			polyId = 18265;
		} else if (itemId == 220006) { // 리니지M 아툰 변신 카드
			polyId = 18269;
		} else if (itemId == 220007) { // 리니지M 팬텀 나이트 변신 카드
			polyId = 18273;
		} else if (itemId == 220008) { // 리니지M 사신 변신 카드
			polyId = 18291;
		} else if (itemId == 220009) { // 리니지M 마커스 변신 카드
			polyId = 18295;
		} else if (itemId == 220010) { // 리니지M 베리스 변신 카드
			polyId = 18300;
		} else if (itemId == 220011) { // 리니지M 로엔그린 변신 카드
			polyId = 18304;
		} else if (itemId == 220012) { // 리니지M 드루가 변신 카드
			polyId = 18308;
		} else if (itemId == 220013) { // 리니지M 신성검사 변신 카드
			polyId = 18432;
		} else if (itemId == 220014) { // 리니지M 암흑기사 변신 카드
			polyId = 18436;
		} else if (itemId == 220015) { // 리니지M 플래티넘 데스나이트 변신 카드
			polyId = 18440;
		} else if (itemId == 220016) { // 리니지M 플래티넘 바포메트 변신 카드
			polyId = 18444;
		} else if (itemId == 220017) { // 리니지M 칠흑 도펠갱어 변신 카드
			polyId = 18448;
		} else if (itemId == 220018) { // 리니지M 금빛 도펠갱어 변신 카드
			polyId = 18452;
		} else if (itemId == 220019) { // 리니지M 엑스터 변신 카드
			polyId = 18456;
		} else if (itemId == 220020) { // 리니지M 가드리아 변신 카드
			polyId = 18460;
		} else if (itemId == 220021) { // 리니지M 게렝 변신 카드
			polyId = 18464;
		}
		// 이걸 넣어줘야 변신지배반지, 변줌과 구분되어 시간이 지나면 변신이 풀린다.
		if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)) {
			pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
		} else if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)) {
			pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
		}
		else {
			pc.removeSkillEffect(SHAPE_CHANGE);
		}
		L1PolyMorph.doPoly(pc, polyId, 1200, L1PolyMorph.MORPH_BY_ITEMMAGIC, false); // 20분 (1,200초)
	}

	
	private void usePolyRangking(L1PcInstance pc, int itemId) {
		int polyid = 0;
		if (itemId == 3000392) {
			if (pc.isCrown()) {
				if (pc.get_sex() == 0)
					polyid = 13715;
				else
					polyid = 13717;
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0)
					polyid = 15115;
				else
					polyid = 13721;
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0)
					polyid = 13723;
				else
					polyid = 13725;
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0)
					polyid = 13727;
				else
					polyid = 13729;
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0)
					polyid = 13731;
				else
					polyid = 13733;
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0)
					polyid = 13735;
				else
					polyid = 13737;
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0)
					polyid = 13739;
				else
					polyid = 13741;
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0)
					polyid = 13743;
				else
					polyid = 13745;
			}
			L1PolyMorph.doPoly(pc, polyid, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
		}
	}

	private void usePolyPotion(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 41143) {
			polyId = 6086;
		} else if (itemId == 41144) {
			polyId = 6087;
		} else if (itemId == 41145) {
			polyId = 6088;
		} else if (itemId == 30057) {
			polyId = 10429;
		} else if (itemId == 30058) {
			polyId = 10431;
		} else if (itemId == 30059) {
			polyId = 10430;
		} else if (itemId == 8000) {
			polyId = 12792;
		} else if (itemId == 8001) { // 7580랜스마스터변신
			polyId = 12237;
		} else if (itemId == 8002) { // 뽕데스
		  //polyId = 12015;		
			polyId = 11685;	
		} else if (itemId == 42655) { // 신규영웅주문서.
			if (pc.isKnight() || pc.isDragonknight() || pc.is전사() || pc.isCrown()) {
				polyId = 12283;
			} else if (pc.isDarkelf()) {
				polyId = 12280;
			} else if (pc.isWizard()) {
				polyId = 12295;
			} else if (pc.isBlackwizard()) {
				polyId = 12286;
			} else if (pc.isElf()) {
				polyId = 12314;
			}
		} else if (itemId == 4100290) { // 진 변신 주문서
			if (pc.isKnight() || pc.is전사() || pc.isCrown()) {
				polyId = 17541;
			} else if (pc.isDragonknight()) {
				polyId = 17549;
			} else if (pc.isDarkelf()) {
				polyId = 17531;
			} else if (pc.isWizard()) {
				polyId = 17515;
			} else if (pc.isBlackwizard()) {
				polyId = 12286;
			} else if (pc.isElf()) {
				polyId = 17535;
			}
		} else if (itemId == 4100284) {// 진 데스나이트 변신 주문서
			polyId = 17541;
		} else if (itemId == 4100285) {// 진 쉐도우 변신 주문서
			polyId = 17531;
		} else if (itemId == 4100286) {// 진 랜스마스터 변신 주문서
			polyId = 17545;
		} else if (itemId == 4100287) {// 진 바포메트 변신 주문서
			polyId = 17515;
		} else if (itemId == 4100288) {// 진 경비병(활) 변신 주문서
			polyId = 17535;
		} else if (itemId == 4100289) {// 진 경비병(창) 변신 주문서
			polyId = 17549;
		}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
	}

	private void useLevelPolyScroll(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 210097) { // 30
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6822;
				} else {
					polyId = 6823;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6824;
				} else {
					polyId = 6825;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6826;
				} else {
					polyId = 6827;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6828;
				} else {
					polyId = 6829;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6830;
				} else {
					polyId = 6831;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7139;
				} else {
					polyId = 7140;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 7141;
				} else {
					polyId = 7142;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210098) { // 40
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6832;
				} else {
					polyId = 6833;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6834;
				} else {
					polyId = 6835;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6836;
				} else {
					polyId = 6837;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6838;
				} else {
					polyId = 6839;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6840;
				} else {
					polyId = 6841;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7143;
				} else {
					polyId = 7144;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 7145;
				} else {
					polyId = 7146;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210099) { // 52
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6842;
				} else {
					polyId = 6843;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6844;
				} else {
					polyId = 6845;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6846;
				} else {
					polyId = 6847;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6848;
				} else {
					polyId = 6849;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6850;
				} else {
					polyId = 6851;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7147;
				} else {
					polyId = 7148;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 7149;
				} else {
					polyId = 7150;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210100) { // 55
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6852;
				} else {
					polyId = 6853;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6854;
				} else {
					polyId = 6855;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6856;
				} else {
					polyId = 6857;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6858;
				} else {
					polyId = 6859;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6860;
				} else {
					polyId = 6861;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7151;
				} else {
					polyId = 7152;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 7153;
				} else {
					polyId = 7154;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210101) { // 60
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6862;
				} else {
					polyId = 6863;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6864;
				} else {
					polyId = 6865;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6866;
				} else {
					polyId = 6867;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6868;
				} else {
					polyId = 6869;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6870;
				} else {
					polyId = 6871;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7155;
				} else {
					polyId = 7156;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 7157;
				} else {
					polyId = 7158;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210102) { // 65
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6872;
				} else {
					polyId = 6873;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6874;
				} else {
					polyId = 6875;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6876;
				} else {
					polyId = 6877;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6878;
				} else {
					polyId = 6879;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6880;
				} else {
					polyId = 6881;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7159;
				} else {
					polyId = 7160;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 7161;
				} else {
					polyId = 7162;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210103) { // 70
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6882;
				} else {
					polyId = 6883;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6884;
				} else {
					polyId = 6885;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6886;
				} else {
					polyId = 6887;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6888;
				} else {
					polyId = 6889;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6890;
				} else {
					polyId = 6891;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7163;
				} else {
					polyId = 7164;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 7165;
				} else {
					polyId = 7166;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210116) { // 75
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 10987;
				} else {
					polyId = 10988;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 10989;
				} else {
					polyId = 10990;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 10991;
				} else {
					polyId = 10992;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 10993;
				} else {
					polyId = 10994;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 10995;
				} else {
					polyId = 10996;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 10997;
				} else {
					polyId = 10998;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 10999;
				} else {
					polyId = 11000;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
			}
		} else if (itemId == 210117) { // 80
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 11001;
				} else {
					polyId = 11002;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 11003;
				} else {
					polyId = 11004;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 11005;
				} else {
					polyId = 11006;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 11007;
				} else {
					polyId = 11008;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 11009;
				} else {
					polyId = 11010;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 11011;
				} else {
					polyId = 11012;
				}
			} else if (pc.isBlackwizard()) {
				if (pc.get_sex() == 0) {
					polyId = 11013;
				} else {
					polyId = 11014;
				}
			} else if (pc.is전사()) {
				if (pc.get_sex() == 0) {
					polyId = 12490;
				} else {
					polyId = 12494;
				}
				// } else if (itemId == 813) { // 수련자 영웅 변신 주문서
				// if (pc.isCrown()) {
				// polyId = 12283;
				// } else if (pc.isKnight()) {
				// polyId = 12283;
				// } else if (pc.isElf()) {
				// polyId = 12314;
				// } else if (pc.isWizard()) {
				// polyId = 12286;
				// } else if (pc.isDarkelf()) {
				// polyId = 12280;
				// } else if (pc.isDragonknight()) {
				// polyId = 12283;
				// } else if (pc.isBlackwizard()) {
				// polyId = 12286;
				// } else if (pc.is전사()) {
				// polyId = 12283;
				// }
			}
		}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
	}

	private void UseArmor(L1PcInstance activeChar, L1ItemInstance armor) {
		int type = armor.getItem().getType();
		L1PcInventory pcInventory = activeChar.getInventory();
		
		/*if(armor.getItemId() == 900075){//변신 지배반지
			activeChar.sendPackets(new S_Ability(7, true));
			activeChar.sendPackets(new S_Ability(2, true));
			activeChar.sendPackets(new S_ShowPolyList(activeChar.getId()));
			activeChar.sendPackets(new S_Ability(7, false));
			activeChar.sendPackets(new S_Ability(2, false));
			return;
		}*/
		
		
		if (armor.getItemId() == 900111 && !armor.isEquipped()) {
			if (pcInventory.checkEquippedAtOnce(new int[] { 900111, 20288 })) {
				activeChar.sendPackets("순간이동 지배반지는 여러 개 착용하거나, 순간이동 조종 반지와 함께 착용할 수 없습니다.");
				return;
			}
		}

		/** 용갑옷 착용 멘트 띄우기 **/
	/*	if(type ==2) { // 안타라스
		if( pcInventory.checkEquipped(22196) ||pcInventory.checkEquipped(22197) 
		 || pcInventory.checkEquipped(22198) || pcInventory.checkEquipped(22199)){
			activeChar.sendPackets(new S_SystemMessage("\\fe안타라스의 분노를 느낍니다."));
		   }
		}
		if(type ==2) { // 파푸리온
		if( pcInventory.checkEquipped(22200) ||pcInventory.checkEquipped(22201) 
		 || pcInventory.checkEquipped(22202) || pcInventory.checkEquipped(22203)){
			activeChar.sendPackets(new S_SystemMessage("\\fB파푸리온의 분노를 느낍니다."));
		   }
		}
		if(type ==2) { // 린드비오르
		if( pcInventory.checkEquipped(22204) ||pcInventory.checkEquipped(22205) 
		 || pcInventory.checkEquipped(22206) || pcInventory.checkEquipped(22207)){
			activeChar.sendPackets(new S_SystemMessage("\\fK린드비오르의 분노를 느낍니다."));
		   }
		}
		if(type ==2) { // 발라카스
		if( pcInventory.checkEquipped(22208) ||pcInventory.checkEquipped(22209) 
		 || pcInventory.checkEquipped(22210) || pcInventory.checkEquipped(22211)){
			activeChar.sendPackets(new S_SystemMessage("\\fA발라카스의 분노를 느낍니다."));
		   }
		}
		if(type ==2) { // 할파스
		if( pcInventory.checkEquipped(111137) ||pcInventory.checkEquipped(111138) 
		 || pcInventory.checkEquipped(111140)){
			activeChar.sendPackets(new S_SystemMessage("\\fF할파스의 분노를 느낍니다."));
		   }
		}*/
		
		boolean equipeSpace; // 장비 하는 개소가 비어 있을까
		if (type == 9) { // 링의 경우
			equipeSpace = pcInventory.getTypeEquipped(2, 9) <= 4;
		} else if (type == 12) { // 링의 경우
			equipeSpace = pcInventory.getTypeEquipped(2, 12) <= 2;
		} else {
			equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
		}

		if (equipeSpace && !armor.isEquipped()) {
			// 사용한 방어용 기구를 장비 하고 있지 않아, 그 장비 개소가 비어 있는 경우(장착을 시도한다)
			if (type == 9) { // 타입이 9 라면
				if (!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT76) && pcInventory.getTypeEquipped(2, 9) >= 2) { // 1~75사이
					activeChar.sendPackets(new S_SystemMessage("스냅퍼에게 76레벨 슬롯 개방 후 착용 가능"));
					return;
				} else if (!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT81)
						&& pcInventory.getTypeEquipped(2, 9) >= 3) { // 76~80사이
					activeChar.sendPackets(new S_SystemMessage("스냅퍼에게 81레벨 슬롯 개방 후 착용 가능"));
					return;
				} else if (pcInventory.getTypeEquipped(2, 9) == 4) { // 4개가
																		// 장착중이면
																		// 더이상착용불가
					activeChar.sendPackets(144);// 슬롯에 이미 아이템을 착용하고 있습니다.
					return;
				}
			}
			if (pcInventory.getTypeAndItemIdEquipped(2, 9, armor.getItem().getItemId()) == 2) { // 이미
																								// 2개
																								// 장착
																								// 중
				activeChar.sendPackets(new S_ServerMessage(3278));// 슬롯 확장: 해당
																	// 아이템 추가 착용
																	// 불가
				return;
			} else if (pcInventory.getTypeAndGradeEquipped(2, 9, armor.getItem().getGrade()) == 2) {// 2였는데
																									// 3으로수정
				if (type == 9) { // 아머,링,착용하려고하는 아이템고유속성번호 3을 만족하는 아이템이 2개착용중일때
					activeChar.sendPackets(new S_ServerMessage(3279));// 슬롯 확장:
																		// 같은 종류
																		// 추가 착용
																		// 불가
					return;
				}
			}
			if (type == 12) { // 타입이 12 라면
				if (!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT59) && pcInventory.getTypeEquipped(2, 12) >= 1) {
					activeChar.sendPackets(new S_SystemMessage("스냅퍼에게 59레벨 슬롯 개방 후 착용 가능"));
					return;
				} else if (pcInventory.getTypeEquipped(2, 12) == 2) {
					activeChar.sendPackets(144);// 슬롯에 이미 아이템을 착용하고 있습니다.
					return;
				} else {

				}
			} else if (type == 29) { // 견갑
				if (!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT_SHOULD)) {
					activeChar.sendPackets(new S_ServerMessage(5077));
					return;
				} else if (pcInventory.getTypeEquipped(2, 29) >= 1) {
					activeChar.sendPackets(144);// 슬롯에 이미 아이템을 착용하고 있습니다.
					return;
				}
			} else if (type == 30) { // 휘장
				if (!activeChar.getQuest().isEnd(L1Quest.QUEST_SLOT_BADGE)) {
					activeChar.sendPackets(new S_ServerMessage(5078));
					return;
				} else if (pcInventory.getTypeEquipped(2, 30) >= 1) {
					activeChar.sendPackets(144);// 슬롯에 이미 아이템을 착용하고 있습니다.
					return;
				}
			}
			if (pcInventory.getTypeAndItemIdEquipped(2, 12, armor.getItem().getItemId()) >= 1) { // 이미
																									// 2개
																									// 장착
																									// 중
				activeChar.sendPackets(new S_ServerMessage(3278));// 슬롯 확장: 해당
																	// 아이템 추가 착용
																	// 불가
				return;
			} else if (type == 12) {
				if (pcInventory.getNameEquipped(2, 12, armor.getName()) >= 1) {
					activeChar.sendPackets(new S_ServerMessage(3278)); // 슬롯 확장:
																		// 같은 종류
																		// 추가 착용
																		// 불가
					return;
				}
			}
			

			int polyid = activeChar.getCurrentSpriteId();
			if (!L1PolyMorph.isEquipableArmor(polyid, type)) { // 그 변신에서는 장비 불가
				return;
			}
			if (type == 7 && pcInventory.getTypeEquipped(2, 13) >= 1
					|| type == 13 && pcInventory.getTypeEquipped(2, 7) >= 1) {
				activeChar.sendPackets(new S_ServerMessage(124));
				// \f1 벌써 무엇인가를 장비 하고 있습니다.
				return;
			}

			if ((type == 7 || type == 13) && activeChar.getEquipSlot().getWeaponCount() == 2) {
				// \f1양손의 무기를 무장한 채로 쉴드(shield)를 착용할 수 없습니다.
				activeChar.sendPackets(new S_ServerMessage(129));
				// 가더일경우 쌍수상태일때는 무시.
				return;
			}

			if (type == 7 && activeChar.getWeapon() != null) {
				// 쉴드(shield)의 경우, 무기를 장비 하고 있으면(자) 양손 무기 체크
				if (activeChar.getWeapon().getItem().isTwohandedWeapon() && armor.getItem().getUseType() != 13) {
					// 양손 무기
					activeChar.sendPackets(new S_ServerMessage(129));
					// \f1양손의 무기를 무장한 채로 쉴드(shield)를 착용할 수 없습니다.
					return;
				}
			}
			cancelAbsoluteBarrier(activeChar); // 아브소르트바리아의 해제
			pcInventory.setEquipped(armor, true);
			
		} else if (armor.isEquipped()) { // 사용한 방어용 기구를 장비 하고 있었을 경우(탈착을 시도한다)
			pcInventory.setEquipped(armor, false);
		} else {
			activeChar.sendPackets(new S_ServerMessage(124)); // \f1 벌써 무엇인가를 장비
			// 하고 있습니다.
		}
		activeChar.setCurrentHp(activeChar.getCurrentHp());
		activeChar.setCurrentMp(activeChar.getCurrentMp());
		activeChar.sendPackets(new S_OwnCharAttrDef(activeChar));
		activeChar.sendPackets(new S_OwnCharStatus(activeChar));
		activeChar.sendPackets(new S_SPMR(activeChar));
	}

	private void UseWeapon(L1PcInstance activeChar, L1ItemInstance weapon) {
		boolean shieldWeapon = false;
		L1PcInventory pcInventory = activeChar.getInventory();
		L1ItemInstance current_weapon = activeChar.getEquipSlot().getWeapon();
		if (current_weapon == null || !activeChar.getEquipSlot().isWeapon(weapon)) {
			// 지정된 무기가 장비 하고 있는 무기와 다른 경우, 장비 할 수 있을까 확인
			int weapon_type = weapon.getItem().getType();
			int polyid = activeChar.getCurrentSpriteId();

			if (!L1PolyMorph.isEquipableWeapon(polyid, weapon_type)) {
				// 그변신에서는장비 불가
				return;
			}

			if (weapon.getItem().isTwohandedWeapon() && pcInventory.getTypeEquipped(2, 7) >= 1) {
				// 양손 무기의 경우, 쉴드(shield) 장비의 확인
				activeChar.sendPackets(new S_ServerMessage(128));
				// \f1쉴드(shield)를 장비 하고 있을 때는 양손으로 가지는 무기를 사용할 수 없습니다.
				return;
			}
		}

		cancelAbsoluteBarrier(activeChar); // 아브소르트바리아의 해제

		if (current_weapon != null) {
			// 이미 무엇인가를 장비 하고 있는 경우, 전의 장비를 뗀다
			if (current_weapon.getItem().getBless() == 2) {
				// 저주해지고 있었을 경우
				activeChar.sendPackets(new S_ServerMessage(150));
				// \f1 뗄 수가 없습니다. 저주를 걸칠 수 있고 있는 것 같습니다.
				return;
			}

			// 착용중인 무기와 요청한 무기가 같을경우.
			if (activeChar.getEquipSlot().isWeapon(weapon)) {
				if (activeChar.getEquipSlot().getWeaponCount() >= 2) {
					// 장착된 2개 무기를 해제.
					L1ItemInstance slot_1 = activeChar.getEquipSlot().getWeapon();
					pcInventory.setEquipped(slot_1, false, false, false, false);
					L1ItemInstance slot_2 = activeChar.getEquipSlot().getWeapon();
					pcInventory.setEquipped(slot_2, false, false, false, false);
					// 1개만 다시 착용.
					if (slot_2.getId() == weapon.getId())
						pcInventory.setEquipped(slot_1, true, false, false, false);
					else
						pcInventory.setEquipped(slot_2, true, false, false, false);
				} else {
					// 장비 교환은 아니고 제외할 뿐
					pcInventory.setEquipped(weapon, false, false, false, false);
				}

				return;
				// 착용중인 무기와 요청한 무기가 다를경우.
			} else {
				// by.lins
				// 착용하려는 아이템과 착용중인 아이템이 한손도끼일경우.
				if (activeChar.isPassive(MJPassiveID.SLAYER.toInt()) && weapon.getItem().getType1() == 11
						&& current_weapon.getItem().getType1() == 11 && weapon.getItem().getType() == 6
						&& current_weapon.getItem().getType() == 6) {
					if (pcInventory.getTypeEquipped(2, 7) >= 1) {
						// \f1쉴드(shield)를 장비 하고 있을 때는 양손으로 가지는 무기를 사용할 수 없습니다.
						activeChar.sendPackets(new S_ServerMessage(128));
						return;
					}
					// 가더 착용중이라면
					if (pcInventory.getTypeEquipped(2, 13) >= 1) {
						// \f1쉴드(shield)를 장비 하고 있을 때는 양손으로 가지는 무기를 사용할 수 없습니다.
						activeChar.sendPackets(new S_ServerMessage(128));
						return;
					}

					if (activeChar.getEquipSlot().getWeaponCount() >= 2) {
						// 이미 착용중입니다.
						activeChar.sendPackets(new S_ServerMessage(124));
						return;
					}

					// // 착용 슬롯을 쉴드로 바꾸기는걸 알리기.
					shieldWeapon = true;
				} else {
					// 현재 착용중인 무기를 해제함.
					for (L1ItemInstance item : activeChar.getEquipSlot().getWeapons())
						pcInventory.setEquipped(item, false, false, true, false);
				}
				// by.lins
			}
		}

		if (weapon.getItemId() == 200002) { // 저주해진 다이스다가
			activeChar.sendPackets(new S_ServerMessage(149, weapon.getLogName())); // \f1%0이
			// 손에
			// 들러붙었습니다.
		}
		pcInventory.setEquipped(weapon, true, false, false, shieldWeapon);
	}

	private int RandomELevel(L1ItemInstance item, int itemId) {
		int j = _random.nextInt(100) + 1;
		if (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON
				|| itemId == L1ItemId.Inadril_T_ScrollB || itemId == L1ItemId.Inadril_T_ScrollB2
				|| itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR6) {

			if (item.getEnchantLevel() <= -1) {
				return 1;
			} else if (item.getEnchantLevel() <= 2) {
				if (j < 30) {
					return 1;
				} else if (j >= 31 && j <= 70) {
					return 2;
				} else if (j >= 71 && j <= 100) {
					return 2;// 원본3
				}
			} else if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				if (j < 50) {
					return 2;
				} else {
					return 1;
				}
			}
			return 1;
		} else if (itemId == 140129 || itemId == 140130) {
			if (item.getEnchantLevel() < 0) {
				if (j < 30) {
					return 2;
				} else {
					return 1;
				}
			} else if (item.getEnchantLevel() <= 2) {
				if (j < 32) {
					return 1;
				} else if (j >= 33 && j <= 60) {
					return 2;
				} else if (j >= 61 && j <= 100) {
					return 3;
				}
			} else if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				if (j < 60) {
					return 2;
				} else {
					return 1;
				}
			}
			return 1;
		}
		return 1;
	}

	private void useSpellBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int itemAttr = 1;
		int locAttr = 1; // 0:other 1:law 2:chaos
		boolean isLawful = true;
		// int pcX = pc.getX();
		// int pcY = pc.getY();
		// int mapId = pc.getMapId();
		int level = pc.getLevel();
		if (itemId == 45000 || itemId == 45008 || itemId == 45018 || itemId == 45021 || itemId == 40171
				|| itemId == 40179 || itemId == 40180 || itemId == 40182 || itemId == 40194 || itemId == 40197
				|| itemId == 40202 || itemId == 40206 || itemId == 40213 || itemId == 40220 || itemId == 40222) {
			itemAttr = 1;
		}
		if (itemId == 45009 || itemId == 45010 || itemId == 45019 || itemId == 40172 || itemId == 40173
				|| itemId == 40178 || itemId == 40185 || itemId == 40186 || itemId == 40192 || itemId == 40196
				|| itemId == 40201 || itemId == 40204 || itemId == 40211 || itemId == 40221 || itemId == 40225) {
			itemAttr = 1;
		}
		/** 마을에서 마법 배우기 **/
		/*
		 * if (pcX > 33116 && pcX < 33128 && pcY > 32930 && pcY < 32942 && mapId
		 * == 4 || pcX > 33135 && pcX < 33147 && pcY > 32235 && pcY < 32247 &&
		 * mapId == 4 || pcX >= 32783 && pcX <= 32803 && pcY >= 32831 && pcY <=
		 * 32851 && mapId == 77 || pcX >= 32448 && pcX <= 32831 && pcY >= 32704
		 * && pcY <= 33023 && mapId == 7783 || pcX >= 32704 && pcX <= 32831 &&
		 * pcY >= 32768 && pcY <= 32959 && mapId == 12152 || pcX >= 32704 && pcX
		 * <= 32895 && pcY >= 32768 && pcY <= 32959 && mapId == 12149 || pcX >=
		 * 32704 && pcX <= 32831 && pcY >= 32768 && pcY <= 32895 && mapId ==
		 * 12146 || pcX >= 32640 && pcX <= 32831 && pcY >= 32896 && pcY <= 33151
		 * && mapId == 12147 || pcX >= 32640 && pcX <= 32831 && pcY >= 32768 &&
		 * pcY <= 32895 && mapId == 12148 || pcX >= 32512 && pcX <= 32639 && pcY
		 * >= 32832 && pcY <= 33023 && mapId == 12150 || pcX >= 32512 && pcX <=
		 * 32639 && pcY >= 32832 && pcY <= 33023 && mapId == 12257 || pcX >=
		 * 32768 && pcX <= 32831 && pcY >= 32768 && pcY <= 32831 && mapId ==
		 * 12358) { locAttr = 1; isLawful = true; } if (pcX > 32880 && pcX <
		 * 32892 && pcY > 32646 && pcY < 32658 && mapId == 4 || pcX > 33135 &&
		 * pcX < 33147 && pcY > 32235 && pcY < 32247 && mapId == 4 || pcX >
		 * 32662 && pcX < 32674 && pcY > 32297 && pcY < 32309 && mapId == 4 ||
		 * pcX >= 32448 && pcX <= 32831 && pcY >= 32704 && pcY <= 33023 && mapId
		 * == 7783 || pcX >= 32704 && pcX <= 32831 && pcY >= 32768 && pcY <=
		 * 32959 && mapId == 12152 || pcX >= 32704 && pcX <= 32895 && pcY >=
		 * 32768 && pcY <= 32959 && mapId == 12149 || pcX >= 32704 && pcX <=
		 * 32831 && pcY >= 32768 && pcY <= 32895 && mapId == 12146 || pcX >=
		 * 32640 && pcX <= 32831 && pcY >= 32896 && pcY <= 33151 && mapId ==
		 * 12147 || pcX >= 32640 && pcX <= 32831 && pcY >= 32768 && pcY <= 32895
		 * && mapId == 12148 || pcX >= 32512 && pcX <= 32639 && pcY >= 32832 &&
		 * pcY <= 33023 && mapId == 12150 || pcX >= 32512 && pcX <= 32639 && pcY
		 * >= 32832 && pcY <= 33023 && mapId == 12257 || pcX >= 32768 && pcX <=
		 * 32831 && pcY >= 32768 && pcY <= 32831 && mapId == 12358) { locAttr =
		 * 1; isLawful = true; }
		 */
		if (pc.isGm()) {
			SpellBook(pc, item, isLawful);
		} else if ((itemAttr == locAttr || itemAttr == 0) && locAttr != 0) {
			if (pc.isKnight() || pc.is전사()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 50) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 45007) {
					pc.sendPackets(new S_ServerMessage(312));
				} else {
					pc.sendPackets(new S_ServerMessage(79));
				}
			} else if (pc.isCrown() || pc.isDarkelf()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 15) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 && level >= 30) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 || itemId >= 45000 && itemId <= 45007) {
					pc.sendPackets(new S_ServerMessage(312));
				} else {
					pc.sendPackets(new S_ServerMessage(79));
				}
			} else if (pc.isElf()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 10) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 && level >= 20) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45016 && itemId <= 45022 && level >= 30) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40170 && itemId <= 40177 && level >= 40) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40178 && itemId <= 40185 && level >= 50) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40186 && itemId <= 40193 && level >= 60) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45000 && itemId <= 45022 || itemId >= 40170 && itemId <= 40193) {
					pc.sendPackets(new S_ServerMessage(312));
				} else {
					pc.sendPackets(new S_ServerMessage(79));
				}
			} else if (pc.isWizard()) {
				if (itemId >= 45000 && itemId <= 45007 && level >= 8) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45008 && itemId <= 45015 && level >= 16) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 45016 && itemId <= 45022 && level >= 24) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40170 && itemId <= 40177 && level >= 32) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40178 && itemId <= 40185 && level >= 40) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40186 && itemId <= 40193 && level >= 48) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40194 && itemId <= 40201 && level >= 56) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40202 && itemId <= 40209 && level >= 64) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40210 && itemId <= 40217 && level >= 72) {
					SpellBook(pc, item, isLawful);
				} else if (itemId >= 40218 && itemId <= 40225 && level >= 80) {
					SpellBook(pc, item, isLawful);
				} else if (itemId == 3000095 && level >= 80) {
					SpellBook(pc, item, isLawful);
				} else {
					pc.sendPackets(new S_ServerMessage(312));
				}
			}
		} else if (itemAttr != locAttr && itemAttr != 0 && locAttr != 0) {
			pc.sendPackets(new S_ServerMessage(79));
			S_SkillSound effect = new S_SkillSound(pc.getId(), 10);
			pc.sendPackets(effect);
			pc.broadcastPacket(effect);
			pc.setCurrentHp(Math.max(pc.getCurrentHp() - 45, 0));
			if (pc.getCurrentHp() <= 0) {
				pc.death(null, true);
			}
			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(new S_ServerMessage(79));
		}
	}

	private void useElfSpellBook(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int level = pc.getLevel();
		if ((pc.isElf() || pc.isGm())/* && isLearnElfMagic(pc) */) {
			if (itemId >= 40232 && itemId <= 40234 && level >= 15) {
				SpellBook2(pc, item);
			} else if (itemId >= 40235 && itemId <= 40236 && level >= 30) {
				SpellBook2(pc, item);
			}
			if (itemId >= 40237 && itemId <= 40240 && level >= 45) {
				SpellBook2(pc, item);
			} else if (itemId >= 40241 && itemId <= 40243 && level >= 60) {
				SpellBook2(pc, item);
			} else if (itemId >= 40244 && itemId <= 40246 && level >= 75) {
				SpellBook2(pc, item);
			} else if (itemId >= 40247 && itemId <= 40248 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40250 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId >= 40251 && itemId <= 40252 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 40253 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40254 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId == 40255 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 40256 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40257 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId >= 40258 && itemId <= 40259 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId >= 40260 && itemId <= 40261 && level >= 30) {
				SpellBook2(pc, item);
			} else if (itemId == 40262 && level >= 40) {
				SpellBook2(pc, item);
			} else if (itemId >= 40263 && itemId <= 40264 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 41150 && level >= 50) {
				SpellBook2(pc, item);
			} else if (itemId == 41151 && level >= 40) {
				SpellBook2(pc, item);
			} else if ((itemId == 3000512 || itemId == 3000513) && level >= 60) {
				SpellBook2(pc, item);
			} else if (itemId == 3000511 && level >= 75) {
				SpellBook2(pc, item);
			} else if (itemId == 3000091 && level >= 80) {
				SpellBook2(pc, item);
			} else if (itemId == 40249 || itemId == 41149 || itemId == 41149 || itemId == 41152 || itemId == 41153 || itemId == 4100295 && level >= 80) {
				SpellBook2(pc, item);
			}
		} else {
			pc.sendPackets(new S_ServerMessage(79));
		}
	}

	private void SpellBook(L1PcInstance pc, L1ItemInstance item, boolean isLawful) {
		String s = "";
		int i = 0;
		int level1 = 0;
		int level2 = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int dk3 = 0;
		int bw1 = 0;
		int bw2 = 0;
		int bw3 = 0;
		L1Skills l1skills = null;
		for (int skillId = 1; skillId < 81; skillId++) {
			l1skills = SkillsTable.getInstance().getTemplate(skillId);
			String s1 = "마법서 (" + l1skills.getName() + ")";
			if (item.getItem().getName().equalsIgnoreCase(s1)) {
				int skillLevel = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (skillLevel) {
				case 1:
					level1 = i7;
					break;
				case 2:
					level2 = i7;
					break;
				case 3:
					l = i7;
					break;
				case 4:
					i1 = i7;
					break;
				case 5:
					j1 = i7;
					break;
				case 6:
					k1 = i7;
					break;
				case 7:
					l1 = i7;
					break;
				case 8:
					i2 = i7;
					break;
				case 9:
					j2 = i7;
					break;
				case 10:
					k2 = i7;
					break;
				case 11:
					l2 = i7;
					break;
				case 12:
					i3 = i7;
					break;
				case 13:
					j3 = i7;
					break;
				case 14:
					k3 = i7;
					break;
				case 15:
					l3 = i7;
					break;
				case 16:
					i4 = i7;
					break;
				case 17:
					j4 = i7;
					break;
				case 18:
					k4 = i7;
					break;
				case 19:
					l4 = i7;
					break;
				case 20:
					i5 = i7;
					break;
				case 21:
					j5 = i7;
					break;
				case 22:
					k5 = i7;
					break;
				case 23:
					l5 = i7;
					break;
				case 24:
					i6 = i7;
					break;
				case 25:
					dk3 = i7;
					break;
				case 26:
					bw1 = i7;
					break;
				case 27:
					bw2 = i7;
					break;
				case 28:
					bw3 = i7;
					break;
				}
			}
		}
		if (pc.isSkillMastery(i)) {
			pc.sendPackets(new S_SystemMessage("이미 배운 마법입니다."), true);
			return;
		}
		int objid = pc.getId();
		pc.sendPackets(new S_AddSkill(level1, level2, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4,
				i5, j5, k5, l5, i6, dk3, bw1, bw2, bw3, 0, 0, pc.getElfAttr()));
		S_SkillSound s_skillSound = new S_SkillSound(objid, isLawful ? 224 : 231);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(objid, i, s, 0, 0);
		pc.getInventory().removeItem(item, 1);
	}

	private void SpellBook1(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int dk3 = 0;
		int bw1 = 0;
		int bw2 = 0;
		int bw3 = 0;
		int de3 = 0;
		L1Skills l1skills = null;
		for (int j6 = 97; j6 <= 241; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			if (l1skills == null) {
				System.out.println(String.format("누락된 스킬 아이디 : %d", j6));
				continue;
			}
			String s1 = "흑정령의 수정 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
				case 1:
					j = i7;
					break;
				case 2:
					k = i7;
					break;
				case 3:
					l = i7;
					break;
				case 4:
					i1 = i7;
					break;
				case 5:
					j1 = i7;
					break;
				case 6:
					k1 = i7;
					break;
				case 7:
					l1 = i7;
					break;
				case 8:
					i2 = i7;
					break;
				case 9:
					j2 = i7;
					break;
				case 10:
					k2 = i7;
					break;
				case 11:
					l2 = i7;
					break;
				case 12:
					i3 = i7;
					break;
				case 13:
					j3 = i7;
					break;
				case 14:
					k3 = i7;
					break;
				case 15:
					l3 = i7;
					break;
				case 16:
					i4 = i7;
					break;
				case 17:
					j4 = i7;
					break;
				case 18:
					k4 = i7;
					break;
				case 19:
					l4 = i7;
					break;
				case 20:
					i5 = i7;
					break;
				case 21:
					j5 = i7;
					break;
				case 22:
					k5 = i7;
					break;
				case 23:
					l5 = i7;
					break;
				case 24:
					i6 = i7;
					break;
				case 25:
					dk3 = i7;
					break;
				case 26:
					bw1 = i7;
					break;
				case 27:
					bw2 = i7;
					break;
				case 28:
					bw3 = i7;
					break;
				case 29:
					de3 = i7;
					break;
				case 30:
					break;
				}
			}
		}
		if (pc.isSkillMastery(i)) {
			pc.sendPackets(new S_SystemMessage("이미 배운 마법입니다."), true);
			return;
		}
		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5,
				k5, l5, i6, dk3, bw1, bw2, bw3, 0, de3, pc.getElfAttr()));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 231);
		pc.sendPackets(s_skillSound);
		Broadcaster.broadcastPacket(pc, s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook2(L1PcInstance pc, L1ItemInstance l1iteminstance) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int dk3 = 0;
		int bw1 = 0;
		int bw2 = 0;
		int bw3 = 0;
		L1Skills l1skills = null;
		for (int j6 = 129; j6 <= 179; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = "정령의 수정 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				if (!pc.isGm() && l1skills.getAttr() != 0 && pc.getElfAttr() != l1skills.getAttr()) {
					if (pc.getElfAttr() == 0 || pc.getElfAttr() == 1 || pc.getElfAttr() == 2 || pc.getElfAttr() == 4
							|| pc.getElfAttr() == 8) { // 속성치가
						// 이상한
						// 경우는
						// 전속성을
						// 기억할
						// 수 있도록(듯이) 해 둔다
						pc.sendPackets(new S_ServerMessage(79));
						return;
					}
				}
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
				case 1: // '\001'
					j = i7;
					break;

				case 2: // '\002'
					k = i7;
					break;

				case 3: // '\003'
					l = i7;
					break;

				case 4: // '\004'
					i1 = i7;
					break;

				case 5: // '\005'
					j1 = i7;
					break;

				case 6: // '\006'
					k1 = i7;
					break;

				case 7: // '\007'
					l1 = i7;
					break;

				case 8: // '\b'
					i2 = i7;
					break;

				case 9: // '\t'
					j2 = i7;
					break;

				case 10: // '\n'
					k2 = i7;
					break;

				case 11: // '\013'
					l2 = i7;
					break;

				case 12: // '\f'
					i3 = i7;
					break;

				case 13: // '\r'
					j3 = i7;
					break;

				case 14: // '\016'
					k3 = i7;
					break;

				case 15: // '\017'
					l3 = i7;
					break;

				case 16: // '\020'
					i4 = i7;
					break;

				case 17: // '\021'
					j4 = i7;
					break;

				case 18: // '\022'
					k4 = i7;
					break;

				case 19: // '\023'
					l4 = i7;
					break;

				case 20: // '\024'
					i5 = i7;
					break;

				case 21: // '\025'
					j5 = i7;
					break;

				case 22: // '\026'
					k5 = i7;
					break;

				case 23: // '\027'
					l5 = i7;
					break;

				case 24: // '\030'
					i6 = i7;
					break;

				case 25: // 용기사 3단계 마법
					dk3 = i7;
					break;

				case 26: // 환술사 1단계 마법
					bw1 = i7;
					break;

				case 27: // 환술사 2단계 마법
					bw2 = i7;
					break;

				case 28: // 환술사 3단계 마법
					bw3 = i7;
					break;

				}
			}
		}
		if (pc.isSkillMastery(i)) {
			pc.sendPackets(new S_SystemMessage("이미 배운 마법입니다."), true);
			return;
		}
		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5,
				k5, l5, i6, dk3, bw1, bw2, bw3, 0, 0, pc.getElfAttr()));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook3(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int dk3 = 0;
		int bw1 = 0;
		int bw2 = 0;
		int bw3 = 0;
		L1Skills l1skills = null;
		for (int j6 = 87; j6 <= 96; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = (new StringBuilder()).append("기술서 (").append(l1skills.getName()).append(")").toString();
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
				case 1: // '\001'
					j = i7;
					break;

				case 2: // '\002'
					k = i7;
					break;

				case 3: // '\003'
					l = i7;
					break;

				case 4: // '\004'
					i1 = i7;
					break;

				case 5: // '\005'
					j1 = i7;
					break;

				case 6: // '\006'
					k1 = i7;
					break;

				case 7: // '\007'
					l1 = i7;
					break;

				case 8: // '\b'
					i2 = i7;
					break;

				case 9: // '\t'
					j2 = i7;
					break;

				case 10: // '\n'
					k2 = i7;
					break;

				case 11: // '\013'
					l2 = i7;
					break;

				case 12: // '\f'
					i3 = i7;
					break;

				case 13: // '\r'
					j3 = i7;
					break;

				case 14: // '\016'
					k3 = i7;
					break;

				case 15: // '\017'
					l3 = i7;
					break;

				case 16: // '\020'
					i4 = i7;
					break;

				case 17: // '\021'
					j4 = i7;
					break;

				case 18: // '\022'
					k4 = i7;
					break;

				case 19: // '\023'
					l4 = i7;
					break;

				case 20: // '\024'
					i5 = i7;
					break;

				case 21: // '\025'
					j5 = i7;
					break;

				case 22: // '\026'
					k5 = i7;
					break;

				case 23: // '\027'
					l5 = i7;
					break;

				case 24: // '\030'
					i6 = i7;
					break;

				case 25: // 용기사 3단계 마법
					dk3 = i7;
					break;

				case 26: // 환술사 1단계 마법
					bw1 = i7;
					break;

				case 27: // 환술사 2단계 마법
					bw2 = i7;
					break;

				case 28: // 환술사 3단계 마법
					bw3 = i7;
					break;

				}
			}
		}
		if (pc.isSkillMastery(i)) {
			pc.sendPackets(new S_SystemMessage("이미 배운 마법입니다."), true);
			return;
		}
		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5,
				k5, l5, i6, dk3, bw1, bw2, bw3, 0, 0, pc.getElfAttr()));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook4(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int dk3 = 0;
		int bw1 = 0;
		int bw2 = 0;
		int bw3 = 0;
		L1Skills l1skills = null;
		for (int j6 = 113; j6 <= 123; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = "마법서 (" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
				case 1: // '\001'
					j = i7;
					break;

				case 2: // '\002'
					k = i7;
					break;

				case 3: // '\003'
					l = i7;
					break;

				case 4: // '\004'
					i1 = i7;
					break;

				case 5: // '\005'
					j1 = i7;
					break;

				case 6: // '\006'
					k1 = i7;
					break;

				case 7: // '\007'
					l1 = i7;
					break;

				case 8: // '\b'
					i2 = i7;
					break;

				case 9: // '\t'
					j2 = i7;
					break;

				case 10: // '\n'
					k2 = i7;
					break;

				case 11: // '\013'
					l2 = i7;
					break;

				case 12: // '\f'
					i3 = i7;
					break;

				case 13: // '\r'
					j3 = i7;
					break;

				case 14: // '\016'
					k3 = i7;
					break;

				case 15: // '\017'
					l3 = i7;
					break;

				case 16: // '\020'
					i4 = i7;
					break;

				case 17: // '\021'
					j4 = i7;
					break;

				case 18: // '\022'
					k4 = i7;
					break;

				case 19: // '\023'
					l4 = i7;
					break;

				case 20: // '\024'
					i5 = i7;
					break;

				case 21: // '\025'
					j5 = i7;
					break;

				case 22: // '\026'
					k5 = i7;
					break;

				case 23: // '\027'
					l5 = i7;
					break;

				case 24: // '\030'
					i6 = i7;
					break;

				case 25: // 용기사 3단계 마법
					dk3 = i7;
					break;

				case 26: // 환술사 1단계 마법
					bw1 = i7;
					break;

				case 27: // 환술사 2단계 마법
					bw2 = i7;
					break;

				case 28: // 환술사 3단계 마법
					bw3 = i7;
					break;

				}
			}
		}
		if (pc.isSkillMastery(i)) {
			pc.sendPackets(new S_SystemMessage("이미 배운 마법입니다."), true);
			return;
		}
		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5,
				k5, l5, i6, dk3, bw1, bw2, bw3, 0, 0, pc.getElfAttr()));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook5(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int dk3 = 0;
		int bw1 = 0;
		int bw2 = 0;
		int bw3 = 0;
		L1Skills l1skills = null;
		for (int j6 = 181; j6 < 200; j6++) {
			l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = "용기사의 서판(" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
				case 1: // '\001'
					j = i7;
					break;

				case 2: // '\002'
					k = i7;
					break;

				case 3: // '\003'
					l = i7;
					break;

				case 4: // '\004'
					i1 = i7;
					break;

				case 5: // '\005'
					j1 = i7;
					break;

				case 6: // '\006'
					k1 = i7;
					break;

				case 7: // '\007'
					l1 = i7;
					break;

				case 8: // '\b'
					i2 = i7;
					break;

				case 9: // '\t'
					j2 = i7;
					break;

				case 10: // '\n'
					k2 = i7;
					break;

				case 11: // '\013'
					l2 = i7;
					break;

				case 12: // '\f'
					i3 = i7;
					break;

				case 13: // '\r'
					j3 = i7;
					break;

				case 14: // '\016'
					k3 = i7;
					break;

				case 15: // '\017'
					l3 = i7;
					break;

				case 16: // '\020'
					i4 = i7;
					break;

				case 17: // '\021'
					j4 = i7;
					break;

				case 18: // '\022'
					k4 = i7;
					break;

				case 19: // '\023'
					l4 = i7;
					break;

				case 20: // '\024'
					i5 = i7;
					break;

				case 21: // '\025'
					j5 = i7;
					break;

				case 22: // '\026'
					k5 = i7;
					break;

				case 23: // '\027'
					l5 = i7;
					break;

				case 24: // '\030'
					i6 = i7;
					break;

				case 25: // 용기사 3단계 마법
					dk3 = i7;
					break;

				case 26: // 환술사 1단계 마법
					bw1 = i7;
					break;

				case 27: // 환술사 2단계 마법
					bw2 = i7;
					break;

				case 28: // 환술사 3단계 마법
					bw3 = i7;
					break;

				}
			}
		}
		if (pc.isSkillMastery(i)) {
			pc.sendPackets(new S_SystemMessage("이미 배운 마법입니다."), true);
			return;
		}
		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5,
				k5, l5, i6, dk3, bw1, bw2, bw3, 0, 0, pc.getElfAttr()));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void SpellBook6(L1PcInstance pc, L1ItemInstance l1iteminstance, GameClient clientthread) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		int l2 = 0;
		int i3 = 0;
		int j3 = 0;
		int k3 = 0;
		int l3 = 0;
		int i4 = 0;
		int j4 = 0;
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		int j5 = 0;
		int k5 = 0;
		int l5 = 0;
		int i6 = 0;
		int dk3 = 0;
		int bw1 = 0;
		int bw2 = 0;
		int bw3 = 0;
		for (int j6 = 201; j6 <= 224; j6++) {
			L1Skills l1skills = SkillsTable.getInstance().getTemplate(j6);
			String s1 = "기억의 수정(" + l1skills.getName() + ")";
			if (l1iteminstance.getItem().getName().equalsIgnoreCase(s1)) {
				int l6 = l1skills.getSkillLevel();
				int i7 = l1skills.getId();
				s = l1skills.getName();
				i = l1skills.getSkillId();
				switch (l6) {
				case 1: // '\001'
					j = i7;
					break;

				case 2: // '\002'
					k = i7;
					break;

				case 3: // '\003'
					l = i7;
					break;

				case 4: // '\004'
					i1 = i7;
					break;

				case 5: // '\005'
					j1 = i7;
					break;

				case 6: // '\006'
					k1 = i7;
					break;

				case 7: // '\007'
					l1 = i7;
					break;

				case 8: // '\b'
					i2 = i7;
					break;

				case 9: // '\t'
					j2 = i7;
					break;

				case 10: // '\n'
					k2 = i7;
					break;

				case 11: // '\013'
					l2 = i7;
					break;

				case 12: // '\f'
					i3 = i7;
					break;

				case 13: // '\r'
					j3 = i7;
					break;

				case 14: // '\016'
					k3 = i7;
					break;

				case 15: // '\017'
					l3 = i7;
					break;

				case 16: // '\020'
					i4 = i7;
					break;

				case 17: // '\021'
					j4 = i7;
					break;

				case 18: // '\022'
					k4 = i7;
					break;

				case 19: // '\023'
					l4 = i7;
					break;

				case 20: // '\024'
					i5 = i7;
					break;

				case 21: // '\025'
					j5 = i7;
					break;

				case 22: // '\026'
					k5 = i7;
					break;

				case 23: // '\027'
					l5 = i7;
					break;

				case 24: // '\030'
					i6 = i7;
					break;

				case 25: // 용기사 3단계 마법
					dk3 = i7;
					break;

				case 26: // 환술사 1단계 마법
					bw1 = i7;
					break;

				case 27: // 환술사 2단계 마법
					bw2 = i7;
					break;

				case 28: // 환술사 3단계 마법
					bw3 = i7;
					break;
				}
			}
		}
		if (pc.isSkillMastery(i)) {
			pc.sendPackets(new S_SystemMessage("이미 배운 마법입니다."), true);
			return;
		}
		int k6 = pc.getId();
		pc.sendPackets(new S_AddSkill(j, k, l, i1, j1, k1, l1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4, i5, j5,
				k5, l5, i6, dk3, bw1, bw2, bw3, 0, 0, pc.getElfAttr()));
		S_SkillSound s_skillSound = new S_SkillSound(k6, 224);
		pc.sendPackets(s_skillSound);
		pc.broadcastPacket(s_skillSound);
		SkillsTable.getInstance().spellMastery(k6, i, s, 0, 0);
		pc.getInventory().removeItem(l1iteminstance, 1);
	}

	private void polyAction(L1PcInstance attacker, L1Character cha, int itemId, String s) {
		boolean isSameClan = false;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getClanid() != 0 && attacker.getClanid() == pc.getClanid()) {
				isSameClan = true;
			}

			if (attacker.getId() != pc.getId() && (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER) || pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2))) {
				// 대상이 변신 지배 반지로 변신했을경우
				pc.sendPackets(new S_SkillSound(pc.getId(), 15846));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 15846));
				attacker.sendPackets(new S_ServerMessage(280));
				return;
			}
		}
		if (cha instanceof L1MonsterInstance) {
			return;
		}
		if (attacker.getId() != cha.getId() && !isSameClan) {
			int probability = 3 * (attacker.getLevel() - cha.getLevel()) - cha.getResistance().getEffectedMrBySkill();
			int rnd = _random.nextInt(100) + 1;
			if (rnd > probability) {
				attacker.sendPackets(new S_ServerMessage(280));
				return;
			}
		}

		int[] polyArray = { 29, 979, 1037, 1039, 3860, 3861, 3862, 3863, 3864, 3865, 3904, 3906, 95, 146, 2374, 2376,
				2377, 2378, 3866, 3867, 3868, 3869, 3870, 3871, 3872, 3873, 3874, 3875, 3876, 3882, 3883, 3884, 3885,
				11358, 11396, 11397, 12225, 12226, 11399, 11398, 12227 }; // 단풍막대
		// 리뉴얼로
		// 추가

		int pid = _random.nextInt(polyArray.length);
		int polyId = polyArray[pid];

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getInventory().checkEquipped(20281) || pc.isPolyRingMaster()) {
				if (usePolyScroll(pc, itemId, s)) {
				} else {
					pc.sendPackets(new S_ServerMessage(181));
				}
			} else {
				L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
				L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
				if (attacker.getId() != pc.getId()) {
					pc.sendPackets(new S_ServerMessage(241, attacker.getName())); // 누구가
					// 당신을
					// 변신시켰습니다.
				}
			}
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			if (mob.getLevel() < 50) {
				int npcId = mob.getNpcTemplate().get_npcId();
				if (npcId != 45338 && npcId != 45370 && npcId != 45456 && npcId != 45464 && npcId != 45473
						&& npcId != 45488 && npcId != 45497 && npcId != 45516 && npcId != 45529 && npcId != 45458) {
					L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
					L1PolyMorph.doPoly(mob, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
				}
			}
		}
	}

	private void cancelAbsoluteBarrier(L1PcInstance pc) { // 아브소르트바리아의 해제
		if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			pc.removeSkillEffect(ABSOLUTE_BARRIER);
		}
	}

	private void useToiTeleportAmulet(L1PcInstance pc, int itemId, L1ItemInstance item) {
		boolean isTeleport = false;
		/** 오만부적 귀환지역제외한 아무곳이나 사용되게 **/
		if (itemId >= 40289 && itemId <= 40297) {
			if (pc.getMap().isEscapable()) {
				isTeleport = true;
			}
		}

		if (isTeleport) {
			pc.start_teleport(item.getItem().get_locx(), item.getItem().get_locy(), item.getItem().get_mapid(), 5, 169,
					true, false);
		} else {
			pc.sendPackets(new S_ServerMessage(79));
			// \f1 아무것도 일어나지 않았습니다.
		}
	}

	private boolean withdrawPet(L1PcInstance pc, int itemObjectId) {
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return false;
		}

		if (pc.getMapId() == 781 || pc.getMapId() == 782) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return false;
		}

		if (pc.isInWarArea()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return false;
		}

		int petCost = 0;
		Object[] petList = pc.getPetList().values().toArray();
		for (Object pet : petList) {
			if (pet instanceof L1PetInstance) {
				if (((L1PetInstance) pet).getItemObjId() == itemObjectId) {
					return false;
				}
			}

			if (pet instanceof L1PetInstance) {
				petCost += ((L1NpcInstance) pet).getPetcost();
			}
		}
		int charisma = pc.getAbility().getTotalCha();
		if (pc.isCrown()) { // CROWN
			charisma += 6;
		} else if (pc.isElf()) { // ELF
			charisma += 12;
		} else if (pc.isWizard()) { // WIZ
			charisma += 6;
		} else if (pc.isDarkelf()) { // DE
			charisma += 6;
		} else if (pc.isDragonknight()) { // 용기사
			charisma += 6;
		} else if (pc.isBlackwizard()) { // 환술사
			charisma += 6;
		}

		charisma -= petCost;
		int petCount = charisma / 6;
		if (petCount <= 0) {
			pc.sendPackets(new S_ServerMessage(489)); // 물러가려고 하는 애완동물이 너무 많습니다.
			return false;
		}

		L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
		if (l1pet != null) {
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(l1pet.get_npcid());
			L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
			pet.setPetcost(6);
		}
		return true;
	}

	private void startFishing(L1PcInstance pc, int itemId, int fishX, int fishY, L1ItemInstance item,
			int itemObjectId) {
		int chargeCount = item.getChargeCount();

		if (pc.getMapId() != 5490 || fishX <= 32704 || fishX >= 32831 || fishY <= 32768 || fishY >= 32895) {
			// 여기에 낚싯대를 던질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(1138));
			return;
		}

		if ((itemId == 41294 || itemId == 41305 || itemId == 41306 || itemId == 600229 || itemId == 9991
				|| itemId == 87058 || itemId == 87059 || itemId == 4100293) && chargeCount <= 0) {
			return;
		}
		if (pc.getInventory().getWeight100() > 82) { // 중량 오버
			pc.sendPackets(new S_SystemMessage("무게가 너무 무거워서 낚시를 할 수 없습니다."));
			return;
		}
		if (pc.getInventory().getSize() >= 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return;
		}
		int gab = 0;
		int heading = pc.getHeading(); // ● 방향: (0.좌상)(1.상)(
		// 2.우상)(3.오른쪽)(4.우하)(5.하)(6.좌하)(7.좌)
		switch (heading) {
		case 0: // 상좌
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX(), pc.getY() - 5);
			break;
		case 1: // 상
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX() + 5, pc.getY() - 5);
			break;
		case 2: // 우상
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX() + 5, pc.getY() - 5);
			break;
		case 3: // 오른쪽
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX() + 5, pc.getY() + 5);
			break;
		case 4: // 우하
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX(), pc.getY() + 5);
			break;
		case 5: // 하
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX() - 5, pc.getY() + 5);
			break;
		case 6: // 좌하
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX() - 5, pc.getY());
			break;
		case 7: // 좌
			gab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(pc.getX() - 5, pc.getY() - 5);
			break;
		}
		int fishGab = L1WorldMap.getInstance().getMap((short) 5490).getOriginalTile(fishX, fishY);
		if (gab == 28 && fishGab == 28) {
			L1ItemInstance useItem = pc.getInventory().getItem(itemObjectId);
			if (useItem != null) {
				pc._fishingRod = useItem;
			} else {
				pc.sendPackets(new S_ServerMessage(1137));
				return;
			}
			// TODO 낚시대를 던지고 난후 미끼부족 멘트 출력
			if (pc._fishingRod.getItemId() == 600229 || pc._fishingRod.getItemId() == 87058 
					|| pc._fishingRod.getItemId() == 87059 || pc._fishingRod.getItemId() == 4100293 || pc.getInventory().consumeItem(41295, 1)) {
				pc._fishingX = fishX;
				pc._fishingY = fishY;
				pc.sendPackets(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY));
				pc.broadcastPacket(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY));
				pc.setFishing(true);
				if (pc._fishingRod.getItemId() == 600229) {	// 성장의 낚싯대
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 30000);
					pc.sendPackets(new S_낚시(30));
					pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Fishing_etc, 4497, true));
				} else if (pc._fishingRod.getItemId() == 4100293) {
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 30000);
					pc.sendPackets(new S_낚시(30));
					pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Fishing_etc, 4497, true));
				} else if (pc._fishingRod.getItemId() == 87058) {
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 90000);
					pc.sendPackets(new S_낚시(90));
					pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Fishing_etc, 4497, true));
				} else if (pc._fishingRod.getItemId() == 87059) {
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 90000);
					pc.sendPackets(new S_낚시(90));
					pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Fishing_etc, 4497, true));
				} else if (pc._fishingRod.getItemId() == 41293) {
					pc.setFishingTime(System.currentTimeMillis() + 240000);
					pc.sendPackets(new S_낚시(240));
				} else {
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 80000);
					pc.sendPackets(new S_낚시(80));
				}
				FishingTimeController.getInstance().addMember(pc);
			} else {
				// 낚시를 하기 위해서는 먹이가 필요합니다.
				pc.sendPackets(new S_ServerMessage(1137));
			}
		} else {
			// 여기에 낚싯대를 던질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(1138));
		}
	}

	private void useResolvent(L1PcInstance pc, L1ItemInstance item, L1ItemInstance resolvent) {
		if (item == null || resolvent == null) {
			pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
			return;
		}
		if (item.getItem().getType2() == 1 || item.getItem().getType2() == 2) { // 무기·방어용
			// 기구
			if (item.getEnchantLevel() != 0) { // 강화가 끝난 상태
				pc.sendPackets(new S_ServerMessage(1161)); // 용해할 수 없습니다.
				return;
			}
			if (item.isEquipped()) { // 장비중
				pc.sendPackets(new S_ServerMessage(1161)); // 용해할 수 없습니다.
				return;
			}
			if (item.getBless() >= 128) { // 봉인중
				pc.sendPackets(new S_ServerMessage(1161)); // 용해할 수 없습니다.
				return;
			}
		}
		int crystalCount = ResolventTable.getInstance().getCrystalCount(item.getItem().getItemId());
		if (crystalCount == 0) {
			pc.sendPackets(new S_ServerMessage(1161)); // 용해할 수 없습니다.
			return;
		}

		int rnd = _random.nextInt(100) + 1;
		if (rnd >= 1 && rnd <= 20) {
			crystalCount = 0;
			pc.sendPackets(new S_ServerMessage(158, item.getName())); // \f1%0이
			// 증발하고
			// 있지 않게
			// 되었습니다.
		} else if (rnd >= 21 && rnd <= 90) {
			crystalCount *= 1;
		} else if (rnd >= 91 && rnd <= 100) {
			crystalCount *= 1.5;
			pc.getInventory().storeItem(41246, (int) (crystalCount * 1.5));
		}
		if (crystalCount != 0) {
			L1ItemInstance crystal = ItemTable.getInstance().createItem(41246);
			crystal.setCount(crystalCount);
			if (pc.getInventory().checkAddItem(crystal, 1) == L1Inventory.OK) {
				pc.getInventory().storeItem(crystal);
				pc.sendPackets(new S_ServerMessage(403, crystal.getLogName())); // %0를
				// 손에
				// 넣었습니다.
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(crystal);
			}
		}
		pc.getInventory().removeItem(item, 1);
		pc.getInventory().removeItem(resolvent, 1);
	}
	
	  public static void useResolvent(L1PcInstance pc, L1ItemInstance item, L1ItemInstance resolvent, int crystalCount)
	  {
	    int rnd = _random.nextInt(100) + 1;
	    if (rnd >= 70) {
	      crystalCount = (int)(crystalCount * 0.5D);
	    } else {
	      crystalCount *= 1;
	    }
	    if (crystalCount != 0)
	    {
	      L1ItemInstance crystal = ItemTable.getInstance().createItem(41246);
	      crystal.setCount(crystalCount);
	      if (pc.getInventory().checkAddItem(crystal, 1) == 0)
	      {
	        pc.getInventory().storeItem(crystal);
	        pc.sendPackets(new S_ServerMessage(403, crystal.getLogName()), true);
	      }
	      else
	      {
	        L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(crystal);
	      }
	    }
	    pc.getInventory().removeItem(item, 1);
	    pc.getInventory().removeItem(resolvent, 1);
	  }

	private void useMagicDoll(L1PcInstance pc, L1ItemInstance item) {
		int itemId = item.getItemId();
		int itemObjectId = item.getId();
		
		if (pc.isInvisble()) {
			return;
		}
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime3() + 3 > curtime) {
			return;
		}
		boolean isAppear = true;
		L1DollInstance doll = null;
		Object[] dollList = pc.getDollList().values().toArray();
		for (Object dollObject : dollList) {
			doll = (L1DollInstance) dollObject;
			if (doll.getItemObjId() == itemObjectId) {
				// 이미 꺼내고 있는 매직 실업 수당
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			if (itemId == 210106 || itemId == 210107 || itemId == 210108 || itemId == 210109) {
				if (!pc.getInventory().checkItem(41246, 500)) {
					pc.sendPackets(new S_ServerMessage(337, "$5240"));
					return;
				}
			} else {
				if (!pc.getInventory().checkItem(41246, 50)) {
					pc.sendPackets(new S_ServerMessage(337, "$5240"));
					return;
				}
			}

			int npcId = 0;
			int dollType = 0;
			int dolltime = 0;
			int itemlevel = 0;
			switch (itemId) {
			// TODO 1단계 마법인형
			case 210070:
				npcId = 200017;
				dollType = L1DollInstance.DOLLTYPE_STONEGOLEM;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 41250:
				npcId = 80108;
				dollType = L1DollInstance.DOLLTYPE_WAREWOLF;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 41248:
				npcId = 80106;
				dollType = L1DollInstance.DOLLTYPE_BUGBEAR;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210072:
				npcId = 200019;
				dollType = L1DollInstance.DOLLTYPE_CRUSTACEA;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210096:
				npcId = 200074;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 740:
				npcId = 507;
				dollType = L1DollInstance.DOLLTYPE_MOKAK;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			// TODO 2단계 마법인형
			case 41249:
				npcId = 80107;
				dollType = L1DollInstance.DOLLTYPE_SUCCUBUS;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210071:
				npcId = 200018;
				dollType = L1DollInstance.DOLLTYPE_ELDER;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210105:
				npcId = 200012;
				dollType = L1DollInstance.DOLLTYPE_COCA;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 750:
				npcId = 900233;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN_NEW;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 510216:
				npcId = 900226;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN_A;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 510217:
				npcId = 900227;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN_B;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 510218:
				npcId = 900228;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN_C;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 410172:
				npcId = 81212;
				dollType = L1DollInstance.DOLLTYPE_인어;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 741:
				npcId = 508;
				dollType = L1DollInstance.DOLLTYPE_LAVAGOLREM;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			// TODO 3단계 마법인형
			case 510219:
				npcId = 900229;
				dollType = L1DollInstance.DOLLTYPE_자이언트;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 510221:
				npcId = 900231;
				dollType = L1DollInstance.DOLLTYPE_흑장로;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 510222:
				npcId = 900232;
				dollType = L1DollInstance.DOLLTYPE_SUCCUBUSQUEEN;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 447017:
				npcId = 900225;
				dollType = L1DollInstance.DOLLTYPE_DRAKE;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 410173:
				npcId = 81213;
				dollType = L1DollInstance.DOLLTYPE_킹버그베어;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 742:
				npcId = 509;
				dollType = L1DollInstance.DOLLTYPE_DIAMONDGOLREM;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			// TODO 4단계 마법인형
			case 447016:
				npcId = 900224;
				dollType = L1DollInstance.DOLLTYPE_LICH;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 510220:
				npcId = 900230;
				dollType = L1DollInstance.DOLLTYPE_사이클롭스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 743:
				npcId = 510;
				dollType = L1DollInstance.DOLLTYPE_NIGHTBALD;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 744:
				npcId = 511;
				dollType = L1DollInstance.DOLLTYPE_SIER;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 3000086:
				npcId = 7310082;
				dollType = L1DollInstance.DOLL_Iris;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 3000087:
				npcId = 7310083;
				dollType = L1DollInstance.DOLL_vampire;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 3000351:
				npcId = 7320069;
				dollType = L1DollInstance.DOLL_mummy;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			// TODO 5단계 마법인형
			case 745:
				npcId = 512;
				dollType = L1DollInstance.DOLLTYPE_DEMON;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 746:
				npcId = 513;
				dollType = L1DollInstance.DOLLTYPE_DEATHNIGHT;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 3000088:
				npcId = 7310084;
				dollType = L1DollInstance.DOLL_barranca;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 3000352:
				npcId = 7320070;
				dollType = L1DollInstance.DOLL_corruption;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 755:
				npcId = 514;
				dollType = L1DollInstance.DOLL_Baphomet;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 756:
				npcId = 515;
				dollType = L1DollInstance.DOLL_icequeen;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 757:
				npcId = 516;
				dollType = L1DollInstance.DOLL_Kouts;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			// TODO 기타 마법인형
			case 210086:
				npcId = 200068;
				dollType = L1DollInstance.DOLLTYPE_SEADANCER;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 500214:
				npcId = 900179;
				dollType = L1DollInstance.DOLLTYPE_SKELETON;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 500215:
				npcId = 900180;
				dollType = L1DollInstance.DOLLTYPE_SCARECROW;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 500213:
				npcId = 900178;
				dollType = L1DollInstance.DOLLTYPE_ETHYNE;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 30022:
				npcId = 5074;
				dollType = L1DollInstance.DOLLTYPE_PIXIE_BLAG;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 30023:
				npcId = 5075;
				dollType = L1DollInstance.DOLLTYPE_PIXIE_LESDAG;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 30024:
				npcId = 5076;
				dollType = L1DollInstance.DOLLTYPE_PIXIE_ELREGEU;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 30025:
				npcId = 5077;
				dollType = L1DollInstance.DOLLTYPE_PIXIE_GREG;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210106:
				npcId = 200008;
				dollType = L1DollInstance.DOLLTYPE_HATCHLING;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210107:
				npcId = 200009;
				dollType = L1DollInstance.DOLLTYPE_HATCHLING;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210108:
				npcId = 200010;
				dollType = L1DollInstance.DOLLTYPE_HATCHLING;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 210109:
				npcId = 200011;
				dollType = L1DollInstance.DOLLTYPE_HATCHLING;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 4100007:
				npcId = 7320200;
				dollType = L1DollInstance.Antaras;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 4100008:
				npcId = 7320201;
				dollType = L1DollInstance.Papou;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 4100009:
				npcId = 7320202;
				dollType = L1DollInstance.Lynd;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 4100010:
				npcId = 7320203;
				dollType = L1DollInstance.Valakas;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			// TODO 이벤트 마법인형
			case 447012:
				npcId = 900220;
				dollType = L1DollInstance.DOLLTYPE_PSY_CHAMPION;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 447013:
				npcId = 900221;
				dollType = L1DollInstance.DOLLTYPE_PSY_BIRD;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 447014:
				npcId = 900222;
				dollType = L1DollInstance.DOLLTYPE_PSY_GANGNAM_STYLE;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 447015:
				npcId = 900223;
				dollType = L1DollInstance.DOLLTYPE_GREMLIN;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 500212:
				npcId = 900176;
				dollType = L1DollInstance.DOLLTYPE_COBO;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 758:
				npcId = 503;
				dollType = L1DollInstance.ruler1;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 759:
				npcId = 504;
				dollType = L1DollInstance.ruler2;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 760:
				npcId = 505;
				dollType = L1DollInstance.ruler3;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 761:
				npcId = 506;
				dollType = L1DollInstance.ruler4;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 772:
				npcId = 537;
				dollType = L1DollInstance.ruler5;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 773:
				npcId = 538;
				dollType = L1DollInstance.ruler6;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 774:
				npcId = 539;
				dollType = L1DollInstance.ruler7;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 775:
				npcId = 540;
				dollType = L1DollInstance.ruler8;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 42654:
				if (pc.getLevel() >= 82) {
					return;
				}
				npcId = 203073;
				dollType = L1DollInstance.수련자마법인형;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 4100134:
				npcId = 7320261;
				dollType = L1DollInstance.jindeathknight;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950000://축복받은 마법인형 : 서큐버스 퀸	
				npcId = 410124;
				dollType = L1DollInstance.DOLLTYPE_축서큐퀸;
				dolltime = 1800;
				itemlevel = item.get_item_level();
						
				break;
			case 950001://축복받은 마법인형 : 흑장로
				npcId = 410125;
				dollType = L1DollInstance.DOLLTYPE_축흑장로;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950002:// 축복받은 마법인형 : 자이언트
				npcId = 410126;
				dollType = L1DollInstance.DOLLTYPE_축자이언트;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950003:// 축복받은 마법인형 : 드레이크
				npcId = 410127;
				dollType = L1DollInstance.DOLLTYPE_축드레이크;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950004:// 축복받은 마법인형 : 킹 버그베어
				npcId = 410128;
				dollType = L1DollInstance.DOLLTYPE_축킹버그;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950005:// 축복받은 마법인형 : 다이아몬드 골렘
				npcId = 410129;
				dollType = L1DollInstance.DOLLTYPE_축다이아골렘;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950006:// 축복받은 마법인형 : 사이클롭스
				npcId = 410130;
				dollType = L1DollInstance.DOLLTYPE_축사이클롭스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950007:// 축복받은 마법인형 : 리치
				npcId = 410131;
				dollType = L1DollInstance.DOLLTYPE_축리치;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950008:// 축복받은 마법인형 : 나이트발드
				npcId = 410132;
				dollType = L1DollInstance.DOLLTYPE_축나발;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950009:// 축복받은 마법인형 : 시어
				npcId = 410133;
				dollType = L1DollInstance.DOLLTYPE_축시어;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950010:// 축복받은 마법인형 : 아이리스
				npcId = 410134;
				dollType = L1DollInstance.DOLLTYPE_축아이리스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950011:// 축복받은 마법인형 : 뱀파이어
				npcId = 410135;
				dollType = L1DollInstance.DOLLTYPE_축뱀파;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950012:// 축복받은 마법인형 : 머미로드
				npcId = 410136;
				dollType = L1DollInstance.DOLLTYPE_축머미;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950013:// 축복받은 마법인형 : 데몬
				npcId = 410137;
				dollType = L1DollInstance.DOLLTYPE_축데몬;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950014:// 축복받은 마법인형 : 데스나이트
				npcId = 410138;
				dollType = L1DollInstance.DOLLTYPE_축데스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950015:// 축복받은 마법인형 : 바란카
				npcId = 410139;
				dollType = L1DollInstance.DOLLTYPE_축바란카;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950016:// 축복받은 마법인형 : 타락
				npcId = 410140;
				dollType = L1DollInstance.DOLLTYPE_축타락;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950017:// 축복받은 마법인형 : 바포메트
				npcId = 410141;
				dollType = L1DollInstance.DOLLTYPE_축바포;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950018:// 축복받은 마법인형 : 얼음여왕
				npcId = 410142;
				dollType = L1DollInstance.DOLLTYPE_축얼녀;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 950019:// 축복받은 마법인형 : 커츠
				npcId = 410143;
				dollType = L1DollInstance.DOLLTYPE_축커츠;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141400:// 할파스 인형 
				npcId = 141411;
				dollType = L1DollInstance.DOLLTYPE_할파스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141401:// 아우라키아 인형
				npcId = 141412;				
				dollType = L1DollInstance.DOLLTYPE_아우라키아;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141402:// 베히모스 인형
				npcId = 141413;
				dollType = L1DollInstance.DOLLTYPE_베히모스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141403:// 기르타스 인형
				npcId = 141414;				
				dollType = L1DollInstance.DOLLTYPE_기르타스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141404:// 알비노 데몬
				npcId = 141415;				
				dollType = L1DollInstance.DOLLTYPE_알비노데몬;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141405:// 알비노 피닉스
				npcId = 141416;				
				dollType = L1DollInstance.DOLLTYPE_알비노피닉스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141406:// 알비노 유니콘
				npcId = 141417;				
				dollType = L1DollInstance.DOLLTYPE_알비노유니콘;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141407:// 유니콘
				npcId = 141418;				
				dollType = L1DollInstance.DOLLTYPE_유니콘;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141408:// 그림 리퍼
				npcId = 141419;				
				dollType = L1DollInstance.DOLLTYPE_그림리퍼;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141409:// 다크스타 조우
				npcId = 141420;				
				dollType = L1DollInstance.DOLLTYPE_다크스타조우;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141410:// 디바인 크루세이더
				npcId = 141421;				
				dollType = L1DollInstance.DOLLTYPE_디바인크루세이더;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141411:// 군터
				npcId = 141422;				
				dollType = L1DollInstance.DOLLTYPE_군터;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141412:// 오우거킹
				npcId = 141423;				
				dollType = L1DollInstance.DOLLTYPE_오우거킹;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141413:// 다크 하딘
				npcId = 141424;				
				dollType = L1DollInstance.DOLLTYPE_다크하딘;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141415:// 드래곤 슬레이어
				npcId = 141425;				
				dollType = L1DollInstance.DOLLTYPE_드래곤슬레이어;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141416:// 피닉스
				npcId = 141426;				
				dollType = L1DollInstance.DOLLTYPE_피닉스;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			case 141417:// 암흑 대장로
				npcId = 141427;				
				dollType = L1DollInstance.DOLLTYPE_암흑대장로;
				dolltime = 1800;
				itemlevel = item.get_item_level();
				break;
			}
			
			if (dollList.length >= Config.MAX_DOLL_COUNT) {
				pc.sendPackets(new S_DollOnOff(doll, item, 0, false));
				doll.deleteDoll(false);
			}
			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
//			doll = new L1DollInstance(template, pc, dollType, item.getId(), dolltime);
			doll = new L1DollInstance(template, pc, itemlevel, dollType, item.getId(), dolltime);
			pc.sendPackets(new S_SkillSound(doll.getId(), 5935));
			pc.broadcastPacket(new S_SkillSound(doll.getId(), 5935));
			pc.getInventory().consumeItem(41246, 50);
			pc.setQuizTime3(curtime);
			pc.sendPackets(new S_DollOnOff(doll, item, dolltime, true));
			pc.sendPackets(new S_OwnCharStatus(pc));
		} else {
			doll.deleteDoll(false);
			pc.sendPackets(new S_DollOnOff(null, null, 0, false));
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	private void useSupport(L1PcInstance pc, int itemId, int itemObjectId) { // 쫄법사
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.isInWarArea()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.getMapId() == 5153 || pc.getMapId() == 5140) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (!pc.getInventory().checkItem(40308, 5000)) {
			pc.sendPackets(new S_ServerMessage(337, "$4")); // \f1%0이 부족합니다.
			return;
		}
		if (pc.getLevel() > 75) {
			pc.sendPackets(new S_SystemMessage("도우미: [75] 레벨까지 사용하실수 있습니다."));
			return;
		}
		boolean isAppear = true;
		L1SupportInstance support = null;
		Object[] supportList = pc.getSupportList().values().toArray();
		for (Object supportObject : supportList) {
			support = (L1SupportInstance) supportObject;
			if (support.getItemObjId() == itemObjectId) { // 이미 꺼내고 있는 매직 실업 수당
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			if (supportList.length >= Config.MAX_DOLL_COUNT) {// 더 이상의 monster를
				// 조종할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(319));
				return;
			}
			int npcId = 0;
			int supportType = 0;
			if (itemId == 210095) {
				npcId = 200073;
				supportType = L1SupportInstance.SUPPORTTYPE_DWARF;
			}

			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
			support = new L1SupportInstance(template, pc, supportType, itemObjectId);
			support.broadcastPacket(new S_SkillSound(support.getId(), 5935));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.getInventory().consumeItem(40308, 1);
			pc.setSupporting(true);
		} else {
			support.broadcastPacket(new S_SkillSound(support.getId(), 5936));
			support.deleteSupport();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.setSupporting(false);
		}
	}

	private void useSupport1(L1PcInstance pc, int itemId, int itemObjectId) { // 쫄법사
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.isInWarArea()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.getMapId() == 5153 || pc.getMapId() == 5140) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (!pc.getInventory().checkItem(40308, 5000)) {
			pc.sendPackets(new S_ServerMessage(337, "$4")); // \f1%0이 부족합니다.
			return;
		}
		if (pc.getLevel() > 75) {
			pc.sendPackets(new S_SystemMessage("도우미: [75] 레벨까지 사용하실수 있습니다."));
			return;
		}
		boolean isAppear = true;
		L1SupportInstance support = null;
		Object[] supportList = pc.getSupportList().values().toArray();
		for (Object supportObject : supportList) {
			support = (L1SupportInstance) supportObject;
			if (support.getItemObjId() == itemObjectId) { // 이미 꺼내고 있는 매직 실업 수당
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			if (supportList.length >= Config.MAX_DOLL_COUNT) {// 더 이상의 monster를
				// 조종할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(319));
				return;
			}
			int npcId = 0;
			int supportType = 0;
			if (itemId == 2100950) {
				npcId = 7310105;
				supportType = L1SupportInstance.SUPPORTTYPE_DWARF;
			}

			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
			support = new L1SupportInstance(template, pc, supportType, itemObjectId);
			support.broadcastPacket(new S_SkillSound(support.getId(), 5935));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.getInventory().consumeItem(40308, 1);
			pc.setSupporting(true);
		} else {
			support.broadcastPacket(new S_SkillSound(support.getId(), 5936));
			support.deleteSupport();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.setSupporting(false);
		}
	}

	private void useSupport2(L1PcInstance pc, int itemId, int itemObjectId) { // 쫄법사
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.isInWarArea()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.getMapId() == 5153 || pc.getMapId() == 5140) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (!pc.getInventory().checkItem(40308, 5000)) {
			pc.sendPackets(new S_ServerMessage(337, "$4")); // \f1%0이 부족합니다.
			return;
		}
		if (pc.getLevel() > 75) {
			pc.sendPackets(new S_SystemMessage("도우미: [75] 레벨까지 사용하실수 있습니다."));
			return;
		}
		boolean isAppear = true;
		L1SupportInstance support = null;
		Object[] supportList = pc.getSupportList().values().toArray();
		for (Object supportObject : supportList) {
			support = (L1SupportInstance) supportObject;
			if (support.getItemObjId() == itemObjectId) { // 이미 꺼내고 있는 매직 실업 수당
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			if (supportList.length >= Config.MAX_DOLL_COUNT) {// 더 이상의 monster를
				// 조종할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(319));
				return;
			}
			int npcId = 0;
			int supportType = 0;
			if (itemId == 2100951) {
				npcId = 7310106;
				supportType = L1SupportInstance.SUPPORTTYPE_DWARF;
			}

			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
			support = new L1SupportInstance(template, pc, supportType, itemObjectId);
			support.broadcastPacket(new S_SkillSound(support.getId(), 5935));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.getInventory().consumeItem(40308, 1);
			pc.setSupporting(true);
		} else {
			support.broadcastPacket(new S_SkillSound(support.getId(), 5936));
			support.deleteSupport();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.setSupporting(false);
		}
	}

	private void useSupport3(L1PcInstance pc, int itemId, int itemObjectId) { // 쫄법사
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.isInWarArea()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.getMapId() == 5153 || pc.getMapId() == 5140) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (!pc.getInventory().checkItem(40308, 5000)) {
			pc.sendPackets(new S_ServerMessage(337, "$4")); // \f1%0이 부족합니다.
			return;
		}
		if (pc.getLevel() > 75) {
			pc.sendPackets(new S_SystemMessage("도우미: [75] 레벨까지 사용하실수 있습니다."));
			return;
		}
		boolean isAppear = true;
		L1SupportInstance support = null;
		Object[] supportList = pc.getSupportList().values().toArray();
		for (Object supportObject : supportList) {
			support = (L1SupportInstance) supportObject;
			if (support.getItemObjId() == itemObjectId) { // 이미 꺼내고 있는 매직 실업 수당
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			if (supportList.length >= Config.MAX_DOLL_COUNT) {// 더 이상의 monster를
				// 조종할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(319));
				return;
			}
			int npcId = 0;
			int supportType = 0;
			if (itemId == 2100953) {
				npcId = 7310107;
				supportType = L1SupportInstance.SUPPORTTYPE_DWARF;
			}

			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
			support = new L1SupportInstance(template, pc, supportType, itemObjectId);
			support.broadcastPacket(new S_SkillSound(support.getId(), 5935));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.getInventory().consumeItem(40308, 1);
			pc.setSupporting(true);
		} else {
			support.broadcastPacket(new S_SkillSound(support.getId(), 5936));
			support.deleteSupport();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.setSupporting(false);
		}
	}

	private void useSupport4(L1PcInstance pc, int itemId, int itemObjectId) { // 쫄다엘
		if (!pc.getMap().isTakePets()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.isInWarArea()) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (pc.getMapId() == 5153 || pc.getMapId() == 5140) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		if (!pc.getInventory().checkItem(40308, 5000)) {
			pc.sendPackets(new S_ServerMessage(337, "$4")); // \f1%0이 부족합니다.
			return;
		}
		if (pc.getLevel() > 75) {
			pc.sendPackets(new S_SystemMessage("도우미: [75] 레벨까지 사용하실수 있습니다."));
			return;
		}
		boolean isAppear = true;
		L1SupportInstance support = null;
		Object[] supportList = pc.getSupportList().values().toArray();
		for (Object supportObject : supportList) {
			support = (L1SupportInstance) supportObject;
			if (support.getItemObjId() == itemObjectId) { // 이미 꺼내고 있는 매직 실업 수당
				isAppear = false;
				break;
			}
		}

		if (isAppear) {
			if (supportList.length >= Config.MAX_DOLL_COUNT) {// 더 이상의 monster를
				// 조종할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(319));
				return;
			}
			int npcId = 0;
			int supportType = 0;
			if (itemId == 2100952) {
				npcId = 7310108;
				supportType = L1SupportInstance.SUPPORTTYPE_DWARF;
			}

			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
			support = new L1SupportInstance(template, pc, supportType, itemObjectId);
			support.broadcastPacket(new S_SkillSound(support.getId(), 5935));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.getInventory().consumeItem(40308, 1);
			pc.setSupporting(true);
		} else {
			support.broadcastPacket(new S_SkillSound(support.getId(), 5936));
			support.deleteSupport();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.setSupporting(false);
		}
	}

	private void useFurnitureItem(L1PcInstance pc, int itemId, int itemObjectId) {
		if (!L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
			pc.sendPackets(new S_ServerMessage(563)); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}

		boolean isAppear = true;
		L1FurnitureInstance furniture = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1FurnitureInstance) {
				furniture = (L1FurnitureInstance) l1object;
				if (furniture.getItemObjId() == itemObjectId) { // 이미 꺼내고 있는 가구
					isAppear = false;
					break;
				}
			}
		}

		if (isAppear) {
			if (pc.getHeading() != 0 && pc.getHeading() != 2) {
				return;
			}
			int npcId = 0;
			switch (itemId) {
			case 41383:
				npcId = 80109;
				break;
			case 41384:
				npcId = 80110;
				break;
			case 41385:
				npcId = 80113;
				break;
			case 41386:
				npcId = 80114;
				break;
			case 41387:
				npcId = 80115;
				break;
			case 41388:
				npcId = 80124;
				break;
			case 41389:
				npcId = 80118;
				break;
			case 41390:
				npcId = 80118;
				break;
			case 41391:
				npcId = 80120;
				break;
			case 41392:
				npcId = 80121;
				break;
			case 41393:
				npcId = 80126;
				break;
			case 41394:
				npcId = 80125;
				break;
			case 41395:
				npcId = 80111;
				break;
			case 41396:
				npcId = 80112;
				break;
			case 41397:
				npcId = 80116;
				break;
			case 41398:
				npcId = 80117;
				break;
			case 41399:
				npcId = 80122;
				break;
			case 41400:
				npcId = 80123;
				break;
			}

			try {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
				if (l1npc != null) {
					try {
						String s = l1npc.getImpl();
						Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance")
								.getConstructors()[0];
						Object aobj[] = { l1npc };
						furniture = (L1FurnitureInstance) constructor.newInstance(aobj);
						furniture.setId(IdFactory.getInstance().nextId());
						furniture.setMap(pc.getMapId());
						if (pc.getHeading() == 0) {
							furniture.setX(pc.getX());
							furniture.setY(pc.getY() - 1);
						} else if (pc.getHeading() == 2) {
							furniture.setX(pc.getX() + 1);
							furniture.setY(pc.getY());
						}
						furniture.setHomeX(furniture.getX());
						furniture.setHomeY(furniture.getY());
						furniture.setHeading(0);
						furniture.setItemObjId(itemObjectId);

						L1World.getInstance().storeObject(furniture);
						L1World.getInstance().addVisibleObject(furniture);
						FurnitureSpawnTable.getInstance().insertFurniture(furniture);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception exception) {
			}
		} else {
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
		}
	}

	private void useFieldObjectRemovalWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		Broadcaster.broadcastPacket(pc, s_attackStatus);
		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1NpcInstance && !(target instanceof L1DollInstance)) {
			L1NpcInstance npc = (L1NpcInstance) target;
			NpcSpawnTable.getInstance().removeSpawn(npc);
			npc.setRespawn(false);
			new L1NpcDeleteTimer(npc, 1 * 1).begin();
			pc.sendPackets(new S_SystemMessage("GM: 해당 엔피씨가 삭제 되었습니다."));
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}
	
	private void use_bug_down_wand(L1PcInstance pc, int target_id, L1ItemInstance item){
		if(!pc.isGm())
			return;

		pc.sendPackets(BugRaceController.getInstance().down_bug(target_id) ? "선택한 버그베어를 넘어뜨렸습니다." : "넘어뜨릴 버그베어를 찾을 수 없습니다.");
	}

	private void useFieldObjectRemovalWand1(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		Broadcaster.broadcastPacket(pc, s_attackStatus);
		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) target;

			// NPC확인막대
			// pc.sendPackets(new S_ChatPacket(pc, "번호 : (" + npc.getNpcId() + "-" + targetId + ") // " + "이름 : (" + npc.getName() + ")"));
			// pc.sendPackets(new S_ChatPacket(pc, "위치 : " + "(x:" + npc.getX() + "), (y:" + npc.getY() + "), (" + "Map:" + npc.getMapId() + ") // GfxId : (" + npc.getCurrentSpriteId() + ")"));
			
			pc.sendPackets(new S_ChatPacket(pc, "\\f2" + npc.getNpcId() + " | " + npc.getMapId() + "번 맵 | " + npc.getX() + ", " + npc.getY() +  " | GFX: " + npc.getCurrentSpriteId() + ""));
			pc.sendPackets(new S_ChatPacket(pc, "\\f2" + npc.getName() + " | " + npc.getLevel() + "레벨 | HP: " + npc.getMaxHp() + " | MP: " + npc.getMaxMp() + ""));
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

	private void useFurnitureRemovalWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackStatus s_attackStatus = new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		pc.broadcastPacket(s_attackStatus);
		int chargeCount = item.getChargeCount();
		if (chargeCount <= 0) {
			return;
		}

		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1FurnitureInstance) {
			L1FurnitureInstance furniture = (L1FurnitureInstance) target;
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
			item.setChargeCount(item.getChargeCount() - 1);
			if (item.getChargeCount() == 0) {
				pc.getInventory().removeItem(item);
			} else {
				pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
			}
		}
	}

	private void useNpcSpownWand(L1PcInstance pc, int targetId, L1ItemInstance item) {
		S_AttackStatus s_attackStatus = new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand);
		pc.sendPackets(s_attackStatus);
		pc.broadcastPacket(s_attackStatus);
		int chargeCount = item.getChargeCount();
		if (chargeCount <= 0) {
			return;
		}

		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(targetId);
			String s = l1npc.getImpl();
			Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance")
					.getConstructors()[0];
			Object aobj[] = { l1npc };
			L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(aobj);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			npc.setX(pc.getX());
			npc.setY(pc.getY());
			npc.setHomeX(pc.getX());
			npc.setHomeY(pc.getY());
			npc.setMap(pc.getMapId());
			npc.setHeading(2);
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, 60000);
			// 60초후바로삭제처리
			timer.begin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		item.setChargeCount(item.getChargeCount() - 1);
		if (item.getChargeCount() == 0) {
			pc.getInventory().removeItem(item);
		} else {
			pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		}
	}

	public static void 군주광역스턴(L1PcInstance pc) {
		if (pc.isCrown() || pc.isGm()) {
			if (pc.hasSkillEffect(L1SkillId.DELAY)) { // 딜레이
				pc.sendPackets(new S_SystemMessage("아직 광역 스턴을 사용할 수 없습니다."));
				return;
			}
			if (pc.isInvisble()) {
				pc.sendPackets(new S_SystemMessage("광역 스턴은 투명상태로 시전이 불가능합니다."));
				return;
			}
			if (pc.getMapId() == 800) {
				pc.sendPackets(new S_SystemMessage("광역 스턴은 시장에서 시전이 불가능합니다."));
				return;
			}
			if (pc.getZoneType() == 1) {
				pc.sendPackets(new S_SystemMessage("광역 스턴은 마을에서 시전이 불가능합니다."));
				return;
			}
			if (pc.getCurrentMp() < 30) {
				pc.sendPackets(new S_ServerMessage(278)); // \f1MP가 부족해 마법을 사용할
				// 수 있지 않습니다.
				return;
			}
			pc.setCurrentMp(pc.getCurrentMp() - 30);
			pc.sendPackets(new S_SystemMessage("광역 스턴을 시전 합니다."));
			pc.setSkillEffect(L1SkillId.DELAY, 10 * 1000);
			pc.sendPackets(new S_SkillIconGFX(74, 3));

			int actionId = ActionCodes.ACTION_SkillBuff;
			S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
			pc.sendPackets(gfx);
			Broadcaster.broadcastPacket(pc, gfx);

			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 10)) {
				Random random = new Random();
				int[] stunTimeArray = { 2000, 2500, 3000, 3500, 4000 };
				int rnd = random.nextInt(stunTimeArray.length);
				int probability = random.nextInt(100) + 1;

				if (probability < 50) {
					int _shockStunDuration = stunTimeArray[rnd];
					if (obj instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) obj;
						L1PinkName.onAction(target, pc);
						if ((pc.getClanid() > 0 && (pc.getClanid() == target.getClanid())) || target.isGm()) {
						} else {
							L1Character cha = (L1Character) obj;

							if (!cha.hasSkillEffect(SHOCK_STUN) && !cha.hasSkillEffect(L1SkillId.EMPIRE) && !cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE)) {
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, target.getX(), target.getY(), target.getMapId());
								target.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
								target.setSkillEffect(SHOCK_STUN, _shockStunDuration);
								target.sendPackets(new S_SkillSound(target.getId(), 15101)); // 스턴
								Broadcaster.broadcastPacket(target, new S_SkillSound(target.getId(), 15101));
							}
						}
					} else if (obj instanceof L1MonsterInstance || obj instanceof L1SummonInstance || obj instanceof L1PetInstance) {
						L1NpcInstance targetnpc = (L1NpcInstance) obj;
						L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, targetnpc.getX(),
								targetnpc.getY(), targetnpc.getMapId());
						targetnpc.setParalyzed(true);
						targetnpc.setSkillEffect(SHOCK_STUN, _shockStunDuration);
						Broadcaster.broadcastPacket(targetnpc, new S_SkillSound(obj.getId(), 15101));
					}
				}
			}
		} else {
			pc.sendPackets(new S_SystemMessage("해당 기술은 군주만 시전이 가능합니다."));
		}
		System.currentTimeMillis();
		return;
	}

	private void 전사스킬(L1PcInstance pc, L1ItemInstance item, boolean ispassibe) {
		L1Skills skill = SkillsTable.getInstance().getTemplateByItem(item.getItemId());
		if (skill != null) {
			int skillLevel = skill.getSkillLevel();
			int id = skill.getId();
			int[] arr = new int[29];
			arr[skillLevel - 1] = id;
			int skillId = skill.getSkillId();
			int objid = pc.getId();
			pc.sendPackets(new S_AddSkill(pc, arr));
			S_SkillSound s_skillSound = new S_SkillSound(objid, 224);
			pc.sendPackets(s_skillSound);
			Broadcaster.broadcastPacket(pc, s_skillSound);
			SkillsTable.getInstance().spellMastery(objid, skillId, skill.getName(), 0, 0);
		}
		pc.getInventory().removeItem(item, 1);
	}

	private void 피씨방코인(L1PcInstance pc, int itemId, L1ItemInstance useItem, int day) {
		long sysTime = System.currentTimeMillis();
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7일
		try {
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 293, true));
			pc.setPC방_버프(true);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.PCBANG_SET, true));
			if (day == 7) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "PC방 이용 시간 해당 기간 동안 PC방버프 혜택이 적용 됩니다."));
			} else {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "PC방 이용 시간:  해당 기간 동안 PC방버프 혜택이 적용 됩니다."));
			}
			pc.getAccount().setBuff_PC방(deleteTime);
			pc.getAccount().update피씨방();
			pc.getInventory().removeItem(useItem, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void 강화버프(L1PcInstance pc, int itemId, L1ItemInstance useItem) {
		if (itemId >= 600198 && itemId <= 600217) {
			int buffId = itemId - 596123;
			int time = 86400000 * Config.TIME_N_BUFF;

			if (pc.hasSkillEffect(buffId)) {
				pc.sendPackets(new S_SystemMessage("이미 적용중인 강화 버프입니다."));
				return;
			}

			ArrayList<Integer> skillIdList = new ArrayList<Integer>();
			for (int skillId = 4075; skillId <= 4094; skillId++) {
				if (pc.hasSkillEffect(skillId)) {
					skillIdList.add(Integer.valueOf(skillId));
				}
			}

			if (skillIdList.size() >= Config.LIMIT_N_BUFF) {
				pc.sendPackets(new S_SystemMessage("강화 버프는 최대 " + Config.LIMIT_N_BUFF + "종류까지 받을 수 있습니다."));
				return;
			}
			
			if (itemId >= 600209 && itemId <= 600212) {
				for (int skillId = 4086; skillId <= 4089; skillId++) {
					if (pc.hasSkillEffect(skillId)) {
						pc.sendPackets(new S_SystemMessage("완력,민첩,지식,지혜는 한가지만 사용가능합니다."));
						return;
					}
				}
			}

			new L1SkillUse().handleCommands(pc, buffId, pc.getId(), pc.getX(), pc.getY(), null, time / 1000, L1SkillUse.TYPE_GMBUFF);
			Updator.exec("insert into character_tams set "
					+ "account_id=?, char_id=?, char_name=?, skill_id=?, expiration_time=?, reserve=? "
					+ "on duplicate key update "
					+ "account_id=?, char_name=?, expiration_time=?", 
					new Handler(){
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							int idx = 0;
							Timestamp ts = new Timestamp(System.currentTimeMillis() + time);
							pstm.setInt(++idx, pc.getAccount().getAccountId());
							pstm.setInt(++idx, pc.getId());
							pstm.setString(++idx, pc.getName());
							pstm.setInt(++idx, buffId);
							pstm.setTimestamp(++idx, ts);
							pstm.setInt(++idx, 0);
							pstm.setInt(++idx, pc.getAccount().getAccountId());
							pstm.setString(++idx, pc.getName());
							pstm.setTimestamp(++idx, ts);							
						}
					});
			/*
			SQLUtil.execute("INSERT INTO character_tams SET account_id=?, char_id=?, char_name=?, skill_id=?, expiration_time=?, reserve=?",
					new Object[] { Integer.valueOf(pc.getAccount().getAccountId()), Integer.valueOf(pc.getId()), pc.getName(), Integer.valueOf(buffId),
							new Timestamp(System.currentTimeMillis() + time), Integer.valueOf(0) });
			*/
			pc.getInventory().removeItem(useItem, 1);
		} else if (itemId == 600218) {
			for (int skillId = 4075; skillId <= 4094; skillId++) {
				if (pc.hasSkillEffect(skillId)) {
					pc.removeSkillEffect(skillId);
				}
			}
			SQLUtil.execute("DELETE FROM character_tams WHERE account_id=? AND skill_id BETWEEN ? AND ?",
					new Object[] { Integer.valueOf(pc.getAccount().getAccountId()), Integer.valueOf(4075), Integer.valueOf(4094) });
			pc.sendPackets(new S_SystemMessage("모든 N샵 버프가 초기화되었습니다."));
			pc.getInventory().removeItem(useItem, 1);
		}
	}

	public void tamadd(String _name, int objectId, int _day, String _encobjid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO Tam SET objid=?, Name=?, Day=? , encobjid=?");
			pstm.setInt(1, objectId);
			pstm.setString(2, _name);
			pstm.setInt(3, _day);
			pstm.setString(4, _encobjid);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void tamupdate(int objectId, Timestamp date) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET TamEndTime=? WHERE objid=?");
			pstm.setTimestamp(1, date);
			pstm.setInt(2, objectId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c,
			0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b, 0x9c, 0x9d, 0x9e,
			0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0,
			0xb1, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2,
			0xc3, 0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2, 0xd3, 0xd4,
			0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6,
			0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8,
			0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe, 0xff };

	private String byteWrite(long value) {
		long temp = value / 128;
		StringBuffer sb = new StringBuffer();
		if (temp > 0) {
			sb.append((byte) hextable[(int) value % 128]);
			while (temp >= 128) {
				sb.append((byte) hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				sb.append((int) temp);
		} else {
			if (value == 0) {
				sb.append(0);
			} else {
				sb.append((byte) hextable[(int) value]);
				sb.append(0);
			}
		}
		return sb.toString();
	}

	private void 탐열매(L1PcInstance pc, int _objid, L1ItemInstance item, int day) {
		try {
			Timestamp tamtime = null;
			long time = 0;
			long sysTime = System.currentTimeMillis();
			String _Name = null;
			int tamcount = pc.tamcount();

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT TamEndTime, char_name FROM characters WHERE objid=?");
				pstm.setInt(1, _objid);
				rs = pstm.executeQuery();
				while (rs.next()) {
					_Name = rs.getString("char_name");
					tamtime = rs.getTimestamp("TamEndTime");
					if (tamtime != null) {
						if (sysTime < tamtime.getTime()) {
							time = tamtime.getTime() - sysTime;
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}

			if (time != 0) {
				tamadd(_Name, _objid, day, byteWrite(_objid));
				pc.sendPackets(new S_TamWindow(pc.getAccountName()));
				pc.sendPackets(new S_SystemMessage("[" + _Name + "] 에 이미 이용중인 상품이 있어 예약 되었습니다."), true);
				pc.getInventory().removeItem(item, 1);
				return;
			} else if (tamcount >= 5) {// 여기에서 계정당 3개먹었는지 체크하면될듯
				pc.sendPackets(new S_ServerMessage(3904));
				return;
			}
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7일
			// deleteTime = new Timestamp(sysTime + 1000*60);//7일

			if (pc.getId() == _objid) {
				pc.setTamTime(deleteTime);
				try {
					pc.save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				tamupdate(_objid, deleteTime);
			}

			pc.sendPackets(new S_TamWindow(pc.getAccountName()));
			int aftertamtime = (int) pc.TamTime() / 1000;
			int aftertamcount = pc.tamcount();

			if (pc.hasSkillEffect(L1SkillId.Tam_Fruit1)) {
				pc.killSkillEffectTimer(L1SkillId.Tam_Fruit1);
				pc.getAC().addAc(1);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit2)) {
				pc.killSkillEffectTimer(L1SkillId.Tam_Fruit2);
				pc.getAC().addAc(2);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit3)) {
				pc.killSkillEffectTimer(L1SkillId.Tam_Fruit3);
				pc.getAC().addAc(3);
				pc.addDamageReductionByArmor(-2);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit4)) {
				pc.killSkillEffectTimer(L1SkillId.Tam_Fruit4);
				pc.getAC().addAc(4);
				pc.addDamageReductionByArmor(-2);
			} else if (pc.hasSkillEffect(L1SkillId.Tam_Fruit5)) {
				pc.killSkillEffectTimer(L1SkillId.Tam_Fruit5);
				pc.getAC().addAc(5);
				pc.addDamageReductionByArmor(-2);
			} else {
			}

			if (aftertamtime < 0) {
				aftertamtime = 0;
			}

			if (aftertamcount == 1) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8265, 4181));
				pc.setSkillEffect(Tam_Fruit1, (int) aftertamtime * 1000);
				pc.getAC().addAc(-1);
			} else if (aftertamcount == 2) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8266, 4182));
				pc.setSkillEffect(Tam_Fruit2, (int) aftertamtime * 1000);
				pc.getAC().addAc(-2);
			} else if (aftertamcount == 3) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8267, 4183));
				pc.setSkillEffect(Tam_Fruit3, (int) aftertamtime * 1000);
				pc.getAC().addAc(-3);
				pc.addDamageReductionByArmor(2);
			} else if (aftertamcount == 4) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8268, 5046));
				pc.setSkillEffect(Tam_Fruit4, (int) aftertamtime * 1000);
				pc.getAC().addAc(-4);
				pc.addDamageReductionByArmor(2);
			} else if (aftertamcount == 5) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) aftertamtime, 8269, 5047));
				pc.setSkillEffect(Tam_Fruit5, (int) aftertamtime * 1000);
				pc.getAC().addAc(-5);
				pc.addDamageReductionByArmor(2);
			}

			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(new S_ServerMessage(3916));
			pc.sendPackets(new S_SkillSound(pc.getId(), 2028), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2028), true);
			pc.getInventory().removeItem(item, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** TAM **/
	private FastMap<Integer, L1PcInstance> charlist = new FastMap<Integer, L1PcInstance>();

	public FastMap<Integer, L1PcInstance> getCharList() {
		return charlist;
	}

	public void addCharList(int id, L1PcInstance pc) {
		charlist.put(id, pc);
	}

	public void deleteCharList(int id) {
		charlist.remove(id);
	}

	private void 스텟초기화(L1PcInstance pc) {
		try {

			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0,
					L1SkillUse.TYPE_LOGIN);

			if (pc.getWeapon() != null) {
				pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
			}

			pc.sendPackets(new S_CharVisualUpdate(pc));
			pc.sendPackets(new S_OwnCharStatus2(pc));

			for (L1ItemInstance armor : pc.getInventory().getItems()) {
				for (int type = 0; type <= 12; type++) {
					if (armor != null) {
						pc.getInventory().setEquipped(armor, false, false, false, false);
					}
				}
			}
			pc.setReturnStat(pc.getExp());
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_OwnCharStatus2(pc));
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
			pc.save();
		} catch (Exception e) {
			System.out.println("스텟초기화 명령어 에러");
			e.printStackTrace();
		}
	}

	// 말하는섬 수정요청
	private void quest_consumeitem(L1PcInstance pc, int itemId) {
		if (itemId == 7006) { // 수련자의 속도향상물약
			L1QuestInfo info = pc.getQuestList(256);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 256, info));
			}
		} else if (itemId == 30027) { // 수련자의 갑옷 마법 주문서
			L1QuestInfo info = pc.getQuestList(262);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 262, info));
				return;
			}
		} else if (itemId == 30028) { // 수련자의 무기 마법 주문서
			L1QuestInfo info = pc.getQuestList(264);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 264, info));
				return;
			}
		} else if (itemId == 7005) { // 랜턴
			L1QuestInfo info = pc.getQuestList(259);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 259, info));
				return;
			}
		} else if (itemId == 40096) { // 수련자의 영웅변신주문서
			L1QuestInfo info = pc.getQuestList(260);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 260, info));
				return;
			}
		} else if (itemId == 40095) { // 수련자의 귀환 주문서
			L1QuestInfo info = pc.getQuestList(268);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 268, info));
				return;
			}
		} else if (itemId == 41245) { // 용해제
			L1QuestInfo info = pc.getQuestList(274);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 274, info));
				return;
			}
		} else if (itemId == 42654) { // 수련자의 마법인형
			L1QuestInfo info = pc.getQuestList(275);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 275, info));
				return;
			}
		} else if (itemId >= 9000 && itemId <= 9005) { // 은 무기 장착
			L1QuestInfo info = pc.getQuestList(277);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 277, info));
				return;
			}
		} else if (itemId == 7004) { // 수련자의 장신구 마법 주문서282
			L1QuestInfo info = pc.getQuestList(282);
			if (info != null && info.end_time == 0) {
				info.ck[0] = 1;
				pc.sendPackets(new S_QuestTalkIsland(pc, 282, info));
				return;
			}

		}
	}

	@Override
	public String getType() {
		return C_ITEM_USE;
	}

	public static boolean is_legend_weapon(int itemId) {
		switch (itemId) {
		case 12:	// 바람칼날의 단검
		case 61:	// 진명황의 집행검
		case 66:	// 드래곤 슬레이어
		case 86:	// 붉은그림자의 이도류
		case 134:	// 수정결정체 지팡이
		case 202011:// 가이아의 격노
		case 202012:// 히페리온의 절망
		case 202013:// 크로노스의 공포
		case 202014:// 타이탄의 분노
		case 217:	// 기르타스의 검
		case 294:	// 사신의 검
		case 2944:	// 아인하사드의 섬광
		case 2945:	// 그랑카인의 심판
			return true;
		}

		return false;
	}
}
