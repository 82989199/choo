package l1j.server.server;

import java.io.IOException;
import java.util.Collection;

import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.MJShiftBattleArgs;
import MJShiftObject.Battle.MJShiftBattleItemWhiteList;
import kr.cholong.PushItemSystem.PushItemController;
import l1j.server.Config;
import l1j.server.ArmorClass.MJArmorClass;
import l1j.server.CharmSystem.CharmModelLoader;
import l1j.server.DogFight.MJDogFightLoader;
import l1j.server.GameSystem.AutoSystemController;
import l1j.server.GameSystem.MiniGame.LottoSystem;
import l1j.server.MJ3SEx.MJNpcSpeedData;
import l1j.server.MJ3SEx.MJSprBoundary;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJActionListener.ActionListenerLinkageLoader;
import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJAttendanceSystem.MJAttendanceLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJCompanion.MJCompanionLoader;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJDTSSystem.MJDTSLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeLoadManager;
import l1j.server.MJEffectSystem.Loader.MJEffectModelLoader;
import l1j.server.MJExpRevision.ExpRevision;
import l1j.server.MJExpRevision.MJFishingExpInfo;
import l1j.server.MJINNSystem.Loader.MJINNHelperLoader;
import l1j.server.MJInstanceSystem.Loader.MJInstanceLoadManager;
import l1j.server.MJItemEnchantSystem.MJItemEnchantSystemLoadManager;
import l1j.server.MJItemExChangeSystem.MJItemExChangeLoader;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJKDASystem.MJKDALoadManager;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJServerMacroSystem.MJServerMacroLoader;
import l1j.server.MJTemplate.Chain.Etc.MJAdenaPickupChain;
import l1j.server.MJTemplate.Chain.Etc.MJHealingPotionDrinkChain;
import l1j.server.MJTemplate.DateSchedulerModel.DateSchedulerLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.Payment.MJPaymentCipher;
import l1j.server.PrideSystem.PrideLoadManager;
import l1j.server.TowerOfDominance.DominanceDataLoader;
import l1j.server.TowerOfDominance.BossController.DominanceTimeController;
import l1j.server.server.Controller.AuctionTimeController;
import l1j.server.server.Controller.AutoBuffController;
import l1j.server.server.Controller.AutoPotionController;
import l1j.server.server.Controller.AutowhetstoneController;
import l1j.server.server.Controller.BossController;
import l1j.server.server.Controller.BraveavatarController;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.Controller.BuyerController;
import l1j.server.server.Controller.EventItemController;
import l1j.server.server.Controller.EventThread;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.Controller.GhostController;
import l1j.server.server.Controller.HouseTaxTimeController;
import l1j.server.server.Controller.LimitShopController;
import l1j.server.server.Controller.MapNightContoller;
import l1j.server.server.Controller.NpcChatTimeController;
import l1j.server.server.Controller.OneTimeController;
import l1j.server.server.Controller.PcInventoryDeleteController;
import l1j.server.server.Controller.PremiumTimeController;
import l1j.server.server.Controller.SkillDataController;
import l1j.server.server.Controller.TamController;
import l1j.server.server.Controller.TimeController;
import l1j.server.server.Controller.UbTimeController;
import l1j.server.server.Thread.MasterRingThread;
import l1j.server.server.clientpackets.C_NPCAction2;
import l1j.server.server.datatables.AccessoryEnchantInformationTable;
import l1j.server.server.datatables.AlchemyProbability;
import l1j.server.server.datatables.ArmorEnchantInformationTable;
import l1j.server.server.datatables.ArmorEnchantList;
import l1j.server.server.datatables.ArmorSetTable;
import l1j.server.server.datatables.BossMonsterSpawnList;
import l1j.server.server.datatables.CharacterBalance;
import l1j.server.server.datatables.CharacterHitRate;
import l1j.server.server.datatables.CharacterQuestMonsterTable;
import l1j.server.server.datatables.CharacterQuestTable;
import l1j.server.server.datatables.CharacterReduc;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ChatLogTable;
import l1j.server.server.datatables.ClanBuffTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.CraftProbability;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.DropItemTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.EventAlarmTable;
import l1j.server.server.datatables.EventTimeTable;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.IncreaseEinhasadMap;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.ItemShopTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.datatables.LightSpawnTable;
import l1j.server.server.datatables.MJAlchemyProbabilityBox;
import l1j.server.server.datatables.MJNpcMarkTable;
import l1j.server.server.datatables.ManagerUserTeleportTable;
import l1j.server.server.datatables.MapCharmItemTable;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MobGroupTable;
import l1j.server.server.datatables.MonsterParalyzeDelay;
import l1j.server.server.datatables.NCoinGiveMonsterTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcCashShopSpawnTable;
import l1j.server.server.datatables.NpcChatTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PartyMapInfoTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.datatables.PlaySupportInfoTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.datatables.RaceTable;
import l1j.server.server.datatables.ReportTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.datatables.UBSpawnTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.datatables.UserProtectMonsterTable;
import l1j.server.server.datatables.WeaponAddDamage;
import l1j.server.server.datatables.WeaponEnchantInformationTable;
import l1j.server.server.datatables.WeaponEnchantList;
import l1j.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.ElementalStoneGenerator;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1BossCycle;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1ClanMatching;
import l1j.server.server.model.L1ClanRanking;
import l1j.server.server.model.L1Cube;
import l1j.server.server.model.L1DeleteItemOnGround;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.L1Racing;
import l1j.server.server.model.L1SoulStoneSystem;
import l1j.server.server.model.L1TimeListener;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.L1GameTimeClock;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1TradeItemBox;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.item.function.L1OrimScrollEnchant;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.utils.MJCommons;
import server.threads.pc.AutoSaveThread;
import server.threads.pc.CharacterQuickCheckThread;

public class GameServer {
	private static GameServer _instance;

	private GameServer() {
	}

	public static GameServer getInstance() {
		if (_instance == null) {
			synchronized (GameServer.class) {
				if (_instance == null)
					_instance = new GameServer();
			}
		}
		return _instance;
	}

	public void initialize() throws Exception {

		GeneralThreadPool.getInstance();
		// 나누기
		IdFactory.getInstance();
		L1WorldMap.getInstance();
		CharacterBalance.getInstance();
		CharacterHitRate.getInstance();
		CharacterReduc.getInstance();
		SpriteInformationLoader.getInstance().loadSpriteInformation();
		MJNetSafeLoadManager.getInstance().load();

		MJFishingExpInfo.do_load();
		MJArmorClass.do_load();
		MJCommons.load();
		MJEProtoMessages.getInstance();
		CraftProbability.getInstance();
		AlchemyProbability.getInstance();
		MJAlchemyProbabilityBox.getInstance();
		CharacterTable.getInstance().loadAllCharName();
		MJSprBoundary.do_load();
		ExpRevision.do_revision();

		// 온라인 상태 리셋트
		CharacterTable.clearOnlineStatus();

		// 게임 시간 시계
		L1GameTimeClock.init();
		// 현재 시간 시계
		RealTimeClock.init();

		C_NPCAction2.getInstance();

		KeyTable.initBossKey();
		if (Config.자동물약버프) {
			AutoPotionController.getInstance();
			AutowhetstoneController.getInstance();
			AutoBuffController.getInstance();
			}
		ArmorEnchantList.getInstance(); // 방어구 인챈 정보 리스트
		WeaponEnchantList.getInstance(); // 무기 인챈 정보 리스트

		// 퀘스트 몬스터 시스템
		CharacterQuestMonsterTable.getInstance();

		//  UB타임 콘트롤러(무한대전)
		 UbTimeController ubTimeContoroller = UbTimeController.getInstance();
		 GeneralThreadPool.getInstance().scheduleAtFixedRate(ubTimeContoroller,
		 0, UbTimeController.SLEEP_TIME);

		/** 로또 시스템 **/
		if (Config.LOTTO == true) {
			LottoSystem.getInstance().start();
		}
		/** 로또 시스템 **/

		// 정령의 돌 타임 컨트롤러
		if (Config.ELEMENTAL_STONE_AMOUNT > 0) {
			ElementalStoneGenerator elementalStoneGenerator = ElementalStoneGenerator.getInstance();
			GeneralThreadPool.getInstance().scheduleAtFixedRate(elementalStoneGenerator, 0, ElementalStoneGenerator.SLEEP_TIME);
		}
		/** 로그파일저장 **/
		LoggerInstance.getInstance();

		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
		// npc shop
		NpcShopTable.getInstance();
		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
		// npc shop

		/** 잊혀진섬 **/
		// 잊섬Controller.getInstance();

		SkillDataController.getInstance();

		OneTimeController.start();
		// 프리미엄 타임 콘트롤러
		PremiumTimeController premiumTimeController = PremiumTimeController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(premiumTimeController, 0, PremiumTimeController.SLEEP_TIME); // #

		// 탐 타임 콘트롤러
		TamController tamController = TamController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(tamController, 0, TamController.SLEEP_TIME); // #

		// 이벤트 아이템 컨트롤러
		if (Config.양말작동유무) {
			EventItemController eventItemController = EventItemController.getInstance();
			GeneralThreadPool.getInstance().scheduleAtFixedRate(eventItemController, 0, EventItemController.SleepTime);
		}

		// 브레이브아바타
		BraveavatarController braveavatarController = BraveavatarController.getInstance();
		GeneralThreadPool.getInstance().execute(braveavatarController);

		// 아지트 경매 타임 콘트롤러
		AuctionTimeController auctionTimeController = AuctionTimeController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(auctionTimeController, 0, AuctionTimeController.SLEEP_TIME); // #

		// 아지트 세금 타임 콘트롤러
		HouseTaxTimeController houseTaxTimeController = HouseTaxTimeController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(houseTaxTimeController, 0, HouseTaxTimeController.SLEEP_TIME); // #

		// 낚시 타임 콘트롤러
		FishingTimeController fishingTimeController = FishingTimeController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(fishingTimeController, 0, FishingTimeController.SLEEP_TIME); // #0.3초마다
																															// 실행
		NpcChatTimeController npcChatTimeController = NpcChatTimeController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(npcChatTimeController, 0, NpcChatTimeController.SLEEP_TIME);

		PcInventoryDeleteController pcInventoryDeleteController = PcInventoryDeleteController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(pcInventoryDeleteController, 0, PcInventoryDeleteController.SLEEP_TIME);

		PushItemController.loadPushItem();

		PushItemController pic = PushItemController.getInstance();
		GeneralThreadPool.getInstance().execute(pic);

		BugRaceController bugRaceController = BugRaceController.getInstance();
		GeneralThreadPool.getInstance().execute(bugRaceController);

		L1HauntedHouse hauntedHouse = L1HauntedHouse.getInstance();
		GeneralThreadPool.getInstance().execute(hauntedHouse);

		L1Racing race = L1Racing.getInstance();
		GeneralThreadPool.getInstance().execute(race);

		NpcTable.getInstance();
		MJNpcSpeedData.do_load();
		L1DeleteItemOnGround deleteitem = new L1DeleteItemOnGround();
		deleteitem.initialize();

		if (!NpcTable.getInstance().isInitialized()) {
			throw new Exception("Could not initialize the npc table");
		}

		// 패키지상점
		NpcCashShopSpawnTable.getInstance();
		NpcCashShopSpawnTable.getInstance().Start();
		MJItemSkillModelLoader.getInstance();
		CharmModelLoader.getInstance();
		SpawnTable.getInstance();
		MobGroupTable.getInstance();
		SkillsTable.getInstance();
		PolyTable.getInstance();
		ItemTable.getInstance();
		ItemTable.getInstance().initRace();
		DropTable.getInstance();
		DropItemTable.getInstance();
		ShopTable.getInstance();
		NPCTalkDataTable.getInstance();
		L1World.getInstance();
		L1WorldTraps.getInstance();
		Dungeon.getInstance();
		NpcSpawnTable.getInstance();
		IpTable.getInstance();
		MapsTable.getInstance();
		UBSpawnTable.getInstance();
		PetTable.getInstance();
		ClanTable.getInstance();
		ClanBuffTable.getInstance();
		L1CastleLocation.setCastleTaxRate(); // CastleTable 초기화 다음 아니면 안 된다
		GetBackRestartTable.getInstance();
		DoorSpawnTable.getInstance();
		ChatLogTable.getInstance();
		WeaponSkillTable.getInstance();
		NpcActionTable.load();
		GMCommandsConfig.load();
		Getback.loadGetBack();
		PetTypeTable.load();
		L1BossCycle.load(); // -- remove
		L1TreasureBox.load();
		L1HealingPotion.load();
		DominanceDataLoader.getInstance();
		DominanceTimeController.getInstance();
		RaceTable.getInstance();
		ResolventTable.getInstance();
		FurnitureSpawnTable.getInstance();
		NpcChatTable.getInstance();
		LightSpawnTable.getInstance();
		// L1Cube.getInstance();
		Announcements.getInstance();
		WeaponAddDamage.getInstance();
		L1ClanMatching.getInstance().loadClanMatching();
		ArmorSetTable.getInstance();
		ManagerUserTeleportTable.getInstance();

		ArmorEnchantInformationTable.getInstance();
		WeaponEnchantInformationTable.getInstance();
		AccessoryEnchantInformationTable.getInstance();

		EventTimeTable.getInstance();
		GeneralThreadPool.getInstance().execute(EventThread.getInstance());
		BossMonsterSpawnList.init();
		BossController.getInstance();
		NCoinGiveMonsterTable.getInstance();
		SpecialMapTable.getInstance();
		PlaySupportInfoTable.getInstance(); // 0627
		// 혈맹포인트버프활성화
		// ClanBuffController.getInstance();

		// GeneralThreadPool.getInstance().execute(ShipTimeController.getInstance());//배표

		// GeneralThreadPool.getInstance().execute(CrockController.getInstance());//테베

		GeneralThreadPool.getInstance().execute(GhostController.getInstance());
		if (Config.Use_Show_Announcecycle) { // 자동 공지사항
			Announcecycle.getInstance();
		}
		ReportTable.getInstance(); // 신고 테이블
		BuyerController.init();
		TimeController tc = TimeController.getInstance();
		GeneralThreadPool.getInstance().scheduleAtFixedRate(tc, 0, TimeController.SLEEP_TIME);
		L1ClanRanking.getInstance().start();
		AutoSystemController.getInstance().threadStart();
		/** MJRankSystem **/
		MJRankLoadManager.getInstance().load();

		MJKDALoadManager.getInstance().load();
		// TODO 말하는 섬 퀘스트 저장
		CharacterQuestTable.getInstance();
		QuestInfoTable.getInstance();
		PartyMapInfoTable.getInstance();

		MJNpcMarkTable.getInstance();

		/** 레이드 **/
		MJRaidLoadManager.getInstance().load();
		MJEffectModelLoader.getInstance();

		/** 2019.07.04 MJcodes 앱센터 LFC **/
		MJInstanceLoadManager.getInstance().load();

		/** MJCSWSystem **/
		// MJCSWorker.getInstnace().start();

		/** MJBotSystem **/
		MJBotLoadManager.getInstance().load();

		// L1SoulStoneSystem.getInstance();

		MJServerMacroLoader.getInstance().start();

		MJCopyMapObservable.getInstance();
		MJINNHelperLoader.getInstance();

		if (Config.USE_ATTENDANCE_SYSTEM) {
			MJAttendanceLoadManager.getInstance().load();
		}

		ClanTable.getInstance().createTutorialClan();
		BQSLoadManager.getInstance().run();

		DateSchedulerLoader.getInstance().run();
		ActionListenerLoader.getInstance();
		ActionListenerLinkageLoader.getInstance();

		MJCombatLoadManager.getInstance().load();
		MJCaptchaLoadManager.getInstance().load();
		MJItemEnchantSystemLoadManager.getInstance().load();

		MJDTSLoader.getInstance();

		DungeonTimeLoadManager.getInstance().load();
		MJItemExChangeLoader.getInstance();
		IncreaseEinhasadMap.getInstance();
		UserProtectMonsterTable.getInstance();

		PrideLoadManager.getInstance();
		if (Config.USE_SHIFT_SERVER) {
			MJShiftBattleArgs.load();
			MJShiftObjectManager.getInstance();
			MJShiftObjectManager.getInstance().load_common_server_info();
			MJShiftBattleItemWhiteList.getInstance();
		}
		MonsterParalyzeDelay.getInstance();

		MJAdenaPickupChain.setup_blessing_effect();
		MJHealingPotionDrinkChain.setup_default_healing_handler();
		MJCompanionLoader.getInstance();
		MJPaymentCipher.getInstance();
		MJDogFightLoader.getInstance();

		/** MJCTSystem **/
		MJCTLoadManager.getInstance().load();
		
		/** 오림 **/
		L1OrimScrollEnchant.load();

		/** 서버관리 스레드 **/
		if (Config.CHARACTER_SAVED_SYSTEM) {
			AutoSaveThread.getInstance();
		}
		if (Config.CHARACTER_CHECK_SYSTEM) {
			CharacterQuickCheckThread.getInstance();
		}
		
		/**지배반지 인벤토리 검사 쓰레드*/
		// 지배 변반이 인벤토리에 없으면 변신해제되는 쓰레드 1초마다 돌아가고 있음
		// 개발자 유정훈님의 말에 따르면, 이거 주석처리해야 유일 반지 사용된다고 함.
//		MasterRingThread.getInstance();
		
		/**낮밤에 따른 맵 텔레포트 */
		MapNightContoller.getInstance().run();
		
		/**교환 아이템 리스트*/
		L1TradeItemBox.load();
		
		
		/**알람 테이블*/
		EventAlarmTable.getInstance();
		L1TimeListener.start();
		
		
		/**일일 상점 제한*/
		LimitShopController.getInstance().start();
		
		
		/**아이템 클릭 으로 샵 오픈*/
		ItemShopTable.getInstance();
		
		/**소지 아이템으로 인한 특정 맵 아이템 드랍*/
		MapCharmItemTable.getInstance();
		
		/** 서버관리 스레드 **/

		// 가비지 컬렉터 실행 (Null) 객체의 해제
		System.gc();

		/*
		 * System.out.println(
		 * "-------------------------------------------------------------------------------"
		 * ); System.out.println(" 【배　율】경험치: x" + Config.RATE_XP + " / 아데나: x" +
		 * Config.RATE_DROP_ADENA + " / 아이템: x" + Config.RATE_DROP_ITEMS);
		 * System.out.println(
		 * "-------------------------------------------------------------------------------"
		 * ); System.out.println(" 【인챈트】무기: +" + Config.ENCHANT_CHANCE_WEAPON +
		 * "% / 방어구: +" + Config.ENCHANT_CHANCE_ARMOR + "% / 장신구: +" +
		 * Config.ENCHANT_CHANCE_ACCESSORY + "%"); System.out.println(
		 * "-------------------------------------------------------------------------------"
		 * ); System.out.print(" 【SERVER IP】 : 미지정"); System.out.println(
		 * " 【SERVER PORT】" + Config.GAME_SERVER_PORT); System.out.println(
		 * "-------------------------------------------------------------------------------"
		 * );
		 */
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

		UBTable.getInstance().getUb(1).start();
		MJCastleWarBusiness.getInstance().run();
		if (Config.Clan_leader) {
			MJBotNameLoader.getInstance().createClanLeaders();
		}
	}

	public void disconnectAllCharacters() {
		Collection<L1PcInstance> pcList = L1World.getInstance().getAllPlayers();
		for (L1PcInstance pc : pcList) {
			if (pc == null || pc.noPlayerck2)
				continue;
			try {
				if (pc.getNetConnection() != null) {
					pc.save();
					pc.saveInventory();
					pc.getNetConnection().setActiveChar(null);
					pc.getNetConnection().kick();
					L1World.getInstance().removeObject(pc);
				}
				pc.logout();
			} catch (Exception e) {
				e.printStackTrace();// 추가
			}
		}
	}

	/** 로봇관련 빽섭 수정 **/
	public int saveAllCharInfo() {
		// exception 발생하면 -1 리턴, 아니면 저장한 인원 수 리턴
		int cnt = 0;
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.noPlayerCK || pc.noPlayerRobot || pc.noPlayerck2)
					continue;
				cnt++;
				pc.save();
				pc.saveInventory();
			}
		} catch (Exception e) {
			return -1;
		}

		return cnt;
	}

	/**
	 * 온라인중의 플레이어에 대해서 kick , 캐릭터 정보의 보존을 한다.
	 */
	public static void disconnectChar(L1PcInstance pc) {
		if (pc.getNetConnection() != null) {
			pc.getNetConnection().kick();
		}
		pc.logout();
	}

	public static void disconnectChar(String name) {
		L1PcInstance pc = L1World.getInstance().getPlayer(name);

		if (pc != null) {
			disconnectChar(pc);
		}
	}

	private class ServerShutdownThread extends Thread {
		private final int _secondsCount;

		public ServerShutdownThread(int secondsCount) {
			_secondsCount = secondsCount;
		}

		@Override
		public void run() {
			L1World world = L1World.getInstance();
			try {
				int secondsCount = _secondsCount;
				world.broadcastServerMessage("잠시 후, 서버를 종료 합니다.");
				world.broadcastServerMessage("안전한 장소에서 로그아웃 해 주세요");
				while (0 < secondsCount) {
					if (secondsCount <= 30) {
						System.out.println("게임이 " + secondsCount + "초 후에 종료 됩니다. 게임을 중단해 주세요.");
						world.broadcastServerMessage("게임이 " + secondsCount + "초 후에 종료 됩니다. 게임을 중단해 주세요.");
					} else {
						if (secondsCount % 60 == 0) {
							System.out.println("게임이 " + secondsCount / 60 + "분 후에 종료 됩니다.");
							world.broadcastServerMessage("게임이 " + secondsCount / 60 + "분 후에 종료 됩니다.");
						}
					}
					Thread.sleep(1000);
					secondsCount--;
				}
				shutdown();
			} catch (InterruptedException e) {
				world.broadcastServerMessage("서버 종료가 중단되었습니다. 서버는 정상 가동중입니다.");
				return;
			}
		}
	}

	private ServerShutdownThread _shutdownThread = null;

	public synchronized void shutdownWithCountdown(int secondsCount) {
		if (_shutdownThread != null) {
			// 이미 슛다운 요구를 하고 있다
			// TODO 에러 통지가 필요할지도 모른다
			return;
		}
		_shutdownThread = new ServerShutdownThread(secondsCount);
		_shutdownThread.start();
		Collection<L1PcInstance> pcList = L1World.getInstance().getAllPlayers();
		for (L1PcInstance pc : pcList) {
			if (pc == null || pc.noPlayerck2)
				continue;
			pc.서버다운중 = true;
		}
	}

	public void shutdown() {
		try {
			if (Config.USE_SHIFT_SERVER)
				MJShiftObjectManager.getInstance().release();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MJCompanionInstance.do_store_companions();
		MJKDALoader.getInstance().store();
		disconnectAllCharacters();
		MJRaidLoadManager.getInstance().release();
		/** 2019.07.04 MJcodes 앱센터 LFC **/
		MJInstanceLoadManager.getInstance().release();
		/** 2019.07.04 MJcodes 앱센터 LFC **/
		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
		MJDShopStorage.clearProcess();
		/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/

		/** MJCTSystem **/
		MJCTLoadManager.getInstance().release();

		/** MJBotSystem **/
		MJBotLoadManager.getInstance().release();

		MJItemSkillModelLoader.release();
		CharmModelLoader.release();

		MJKDALoadManager.getInstance().release();

		MJNetSafeLoadManager.getInstance().release();

		MJNetServerLoadManager.release();

		try {
			LoggerInstance.getInstance().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);

	}

	public synchronized void abortShutdown() {
		if (_shutdownThread == null) {
			// 슛다운 요구를 하지 않았다
			// TODO 에러 통지가 필요할지도 모른다
			return;
		}

		_shutdownThread.interrupt();
		_shutdownThread = null;
	}

}
