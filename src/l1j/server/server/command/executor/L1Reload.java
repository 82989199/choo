package l1j.server.server.command.executor;

import java.util.HashMap;

import kr.cholong.PushItemSystem.PushItemController;
import l1j.server.Config;
import l1j.server.ArmorClass.MJArmorClass;
import l1j.server.CharmSystem.CharmModelLoader;
import l1j.server.DogFight.History.MJDogFightTicketIdManager;
import l1j.server.MJ3SEx.MJNpcSpeedData;
import l1j.server.MJ3SEx.MJSprBoundary;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJActionListener.ActionListenerLinkageLoader;
import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJDTSSystem.MJDTSLoader;
import l1j.server.MJEffectSystem.Loader.MJEffectModelLoader;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.MJExpRevision.MJFishingExpInfo;
import l1j.server.MJItemExChangeSystem.MJItemExChangeLoader;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJPassiveSkill.MJPassiveLoader;
import l1j.server.MJServerMacroSystem.MJServerMacroLoader;
import l1j.server.MJTemplate.Chain.Etc.MJHealingPotionDrinkChain;
import l1j.server.MJTemplate.DateSchedulerModel.MinuteScheduler;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.PrideSystem.PrideLoadManager;
import l1j.server.server.Announcecycle;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.Controller.LimitShopController;
import l1j.server.server.datatables.AccessoryEnchantInformationTable;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.datatables.AlchemyProbability;
import l1j.server.server.datatables.ArmorEnchantInformationTable;
import l1j.server.server.datatables.ArmorSetTable;
import l1j.server.server.datatables.AutoLoot;
import l1j.server.server.datatables.BossMonsterSpawnList;
import l1j.server.server.datatables.CharacterBalance;
import l1j.server.server.datatables.CharacterHitRate;
import l1j.server.server.datatables.CharacterQuestTable;
import l1j.server.server.datatables.CharacterReduc;
import l1j.server.server.datatables.CharactersGiftItemTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.CraftProbability;
import l1j.server.server.datatables.DropItemTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.EventAlarmTable;
import l1j.server.server.datatables.EventTimeTable;
import l1j.server.server.datatables.IncreaseEinhasadMap;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.ItemShopTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MJNpcMarkTable;
import l1j.server.server.datatables.MapCharmItemTable;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.MonsterParalyzeDelay;
import l1j.server.server.datatables.NCoinGiveMonsterTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcCashShopSpawnTable;
import l1j.server.server.datatables.NpcChatTable;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcShopSpawnTable2;
import l1j.server.server.datatables.NpcShopSpawnTable3;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcShopTable2;
import l1j.server.server.datatables.NpcShopTable3;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PartyMapInfoTable;
import l1j.server.server.datatables.PlaySupportInfoTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.datatables.QuestMonsterTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.ResolventTable1;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.datatables.UserProtectMonsterTable;
import l1j.server.server.datatables.WeaponAddDamage;
import l1j.server.server.datatables.WeaponEnchantInformationTable;
import l1j.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1TradeItemBox;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.item.function.L1OrimScrollEnchant;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.utils.Teleportation;

public class L1Reload implements L1CommandExecutor {

	private L1Reload() {  }

	public static L1CommandExecutor getInstance() {
		return new L1Reload();
	}

	@Override
	public void execute(L1PcInstance gm, String cmdName, String arg) {		
		if (arg.equalsIgnoreCase("몹드랍")) {
			DropTable.reload();
			gm.sendPackets("droplist 테이블이 리로드 되었습니다.");	
		} else if (arg.equalsIgnoreCase("오림주문서")) {	
			L1OrimScrollEnchant.reload();
			gm.sendPackets("오림 장신구 강화주문서 정보가 리로드 되었습니다.");
		}else if(arg.equalsIgnoreCase("낚시경험치")){
			MJFishingExpInfo.do_load();
			gm.sendPackets("fishing_exp_info 테이블이 리로드 되었습니다.");
		}else if(arg.equalsIgnoreCase("변신레벨")){
			MJSprBoundary.do_load();
			gm.sendPackets("spr_boundary 테이블이 리로드 되었습니다.");
		}else if(arg.equalsIgnoreCase("엔피씨속도")){
			MJNpcSpeedData.do_load();
			for(L1NpcInstance npc : L1World.getInstance().getAllNpc())
				MJNpcSpeedData.install_npc(npc);
			gm.sendPackets("npc_speed_data  테이블이 리로드 되었습니다.");
		}else if(arg.equalsIgnoreCase("아머클래스")){
			MJArmorClass.do_load();
			gm.sendPackets("armor_class 테이블이 리로드 되었습니다.");		
		}else if(arg.equalsIgnoreCase("회복률")){
			MJHealingPotionDrinkChain.getInstance().load_healing_effect_info();
			gm.sendPackets("potion_effect 테이블이 리로드 되었습니다.");										
		}else if(arg.equalsIgnoreCase("버경")){
			BugRaceController.load_config();
			gm.sendPackets("bug_race.properties가 리로드 되었습니다.");							
		}else if(arg.equalsIgnoreCase("커스시간")){
			MonsterParalyzeDelay.reload();
			gm.sendPackets("monster_paralyze 테이블이 리로드 되었습니다.");				
		}else if(arg.equalsIgnoreCase("자존심")){
			PrideLoadManager.reload();
			gm.sendPackets("pride_items 테이블/price_system.properties이 리로드 되었습니다.");			
		} else if(arg.equalsIgnoreCase("맵아인")){
			IncreaseEinhasadMap.reload();
			gm.sendPackets("tb_increase_einhasad_map 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("패시브스킬")){
			MJPassiveLoader.reload();
			gm.sendPackets("passive_book_mapped 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("텔주문서")){
			MJDTSLoader.reload();
			gm.sendPackets("tb_designate_teleport_scroll 테이블이 리로드되었습니다.");
		} else if(arg.equalsIgnoreCase("장비교환")){
			MJItemExChangeLoader.reload();
			gm.sendPackets("tb_item_exchange_key_info/tb_item_exchange_rewards 테이블이 리로드되었습니다.");
		} else if(arg.equalsIgnoreCase("액션리스너")){
			MinuteScheduler.getInstance().clear_action_listener();
			ActionListenerLoader.getInstance().updateNpcActionListener();
			ActionListenerLinkageLoader.reload();
			gm.sendPackets("액션 리스너가 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("자동공지")){
			Announcecycle.getInstance().reloadAnnouncecycle();
			gm.sendPackets("Announcecycle이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("제작확률")){
			CraftProbability.reload();
			gm.sendPackets("craft_probability 테이블이 리로드 되었습니다.");
			//PartyMapInfoTable
		} else if(arg.equalsIgnoreCase("파티맵")){
			PartyMapInfoTable.reload();
			gm.sendPackets("party_map_info 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("인형확률")){
			AlchemyProbability.reload();
			gm.sendPackets("tb_alchemy_probability 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("제작인형컨픽")){
			Config.loadCraftConfig();
			gm.sendPackets("제작/인형 컨픽이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("푸쉬시스템")){
			PushItemController.close();
			PushItemController.loadPushItem();
			gm.sendPackets("push_item_list 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("크레프트인포")){
			MJEProtoMessages.SC_CRAFT_LIST_ALL_ACK.reloadMessage();
			gm.sendPackets("craftinfo.dat가 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("인형인포")){
			MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK.reloadMessage();
			gm.sendPackets("alchemyinfo.dat가 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("제작엔피씨")){
			SC_CRAFT_LIST_ALL_ACK.reloadedCraftNpc();
			gm.sendPackets("craftlist 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("아이템스킬")){
			MJItemSkillModelLoader.reload();
			gm.sendPackets("tb_itemskill_model 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("매크로")){
			MJServerMacroLoader.reload();			
			gm.sendPackets("tb_ServerMacro 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("아인증폭")){
			MJExpAmplifierLoader.reload();
			gm.sendPackets("MJExpAmplifierLoader 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("엔피씨마크")){
			MJNpcMarkTable.reload();
			gm.sendPackets("NpcMark 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("엔피씨액션")) { 
			NPCTalkDataTable.reload();
			gm.sendPackets("NpcAction 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("엔피씨")) {
			NpcTable.reload();
			gm.sendPackets("NpcTable/npc_born 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("템드랍아이템")) {
			DropItemTable.reload();
			gm.sendPackets("drop_item 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("용해제2")) {
			ResolventTable1.reload();
			gm.sendPackets("resolvent1 테이블이 리로드 최신화되었습니다.");
		} else if (arg.equalsIgnoreCase("변신")) {
			PolyTable.reload();
			gm.sendPackets("polymorphs 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("용해제1")) {
			ResolventTable.reload();
			gm.sendPackets("resolvent 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("박스")) {
			if(L1TreasureBox.load())
				gm.sendPackets("TreasureBox.xml 파일이 리로드 되었습니다.");
			else{
				String s = "TreasureBox에 오류가 발견되었습니다.";
				gm.sendPackets(s);
				System.out.println(s);
			}
		} else if (arg.equalsIgnoreCase("스페셜맵")) {
			SpecialMapTable.reload();
			gm.sendPackets("special_map 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("물약회복")) {
			L1HealingPotion.reload();
			gm.sendPackets("HealingPotion.xml 파일이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("컨피그")) {			
			Config.load();
			gm.sendPackets("config 폴더에 파일이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("스킬")) {
			SkillsTable.reload();
			gm.sendPackets("Skill 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("몹스킬")) {
			MobSkillTable.reload();
			gm.sendPackets("mobskill 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("엔피씨액션")) { 
			NPCTalkDataTable.reload();
			gm.sendPackets("NpcAction 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("영자상점")) {
			NpcShopTable.reloding();
			NpcShopTable2.reloding();
			NpcShopTable3.reloding();
			gm.sendPackets("NpcShopTable 테이블이 리로드 되었습니다.");	
		} else if (arg.equalsIgnoreCase("무기스킬")) {
			WeaponSkillTable.reload();
			gm.sendPackets("WeaponSkill 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("엔피씨샵스폰")) {
			NpcShopSpawnTable.reloding();
			NpcShopSpawnTable2.reloding();
			NpcShopSpawnTable3.reloding();
			gm.sendPackets("NpcShopSpawnTable(영자상점) 테이블이 리로드 되었습니다.");	
		}else if (arg.equalsIgnoreCase("레벨퀘스트")) {
			CharactersGiftItemTable.reload();
			gm.sendPackets("characters_levelup_item 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("밴아이피")) {
			IpTable.getInstance();
			IpTable.reload();
			gm.sendPackets("banIp 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("아이템")){
			ItemTable.reload();
			gm.sendPackets("아이템 정보가 리로드 되었습니다.");
			HashMap<Integer, L1Item> items = ItemTable.getInstance().getAllTemplates();
			HashMap<Integer, L1RaceTicket> map = BugRaceController.getInstance().getAllTemplates();
			for(L1RaceTicket t : map.values()){
				if(t != null)
					items.put(t.getItemId(), t);
			}
			MJDogFightTicketIdManager.getInstance().update_item_template();
		} else if (arg.equalsIgnoreCase("상점")){
			ShopTable.reload();
			gm.sendPackets("shop 테이블이 리로드 되었습니다.");	
		} else if (arg.equalsIgnoreCase("무기대미지")){
			WeaponAddDamage.reload();
			gm.sendPackets("weapon_damege 테이블이 리로드 되었습니다.");		
		} else if (arg.equalsIgnoreCase("클랜데이터")){
			ClanTable.reload();
			MJCastleWarBusiness.getInstance().reload();
			gm.sendPackets("리로드: clan_data 테이블이 리로드 되었습니다.");		
		} else if (arg.equalsIgnoreCase("공성")){
			MJCastleWarBusiness.getInstance().reload();
			gm.sendPackets("리로드: castle 테이블이 리로드 되었습니다.");		
		} else if (arg.equalsIgnoreCase("스폰리스트")){
			SpawnTable.getInstance().reload();
			gm.sendPackets("리로드: spawnlist 테이블이 리로드 되었습니다.");	
		} else if (arg.equalsIgnoreCase("엔스폰리스트")){
			NpcSpawnTable.getInstance().reload();
			gm.sendPackets("리로드: spawnlist_npc 테이블 리로드 되었습니다.");	
		} else if (arg.equalsIgnoreCase("엔캐샵스폰리스트")){
			NpcCashShopSpawnTable.reload();
			gm.sendPackets("리로드: spawnlist_npc_cash_shop 테이블이 리로드 되었습니다.");	
		} else if (arg.equalsIgnoreCase("엔피씨채팅")){
			NpcChatTable.reload();
			gm.sendPackets("리로드: npcchat 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("던전")){
			Dungeon.reload();
			gm.sendPackets("리로드: dungeon 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("엔코인몬스터")){
			NCoinGiveMonsterTable.reload();
			gm.sendPackets("리로드: ncoin_give_monster 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("맵")){
			MapsTable.reload();
			gm.sendPackets("리로드: mapids 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("벨런스")){
			CharacterBalance.reload();
			CharacterReduc.reload();
			CharacterHitRate.reload();
			gm.sendPackets("리로드: character_balance 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("신규지급템")){
			Beginner.reload();
			gm.sendPackets("리로드: beginner 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("오토드랍")){
			AutoLoot.reload();
			gm.sendPackets("리로드: autoloot 테이블이 리로드 되었습니다.");
		/*} else if (arg.equalsIgnoreCase("아머인챈트")){
			ArmorEnchantList.reload();
			gm.sendPackets("리로드: armor_enchant_list 테이블이 리로드 되었습니다");
		} else if (arg.equalsIgnoreCase("웨폰인챈트")){
			WeaponEnchantList.reload();
			gm.sendPackets("리로드: weapon_enchant_list 테이블이 리로드 되었습니다");*/
		} else if (arg.equalsIgnoreCase("엔샵")){
			AdenShopTable.reload();
			gm.sendPackets("리로드: shop_aden 테이블이 리로드 되었습니다");
		} else if(arg.equalsIgnoreCase("방어구인챈정보")) {
			ArmorEnchantInformationTable.reload();
			gm.sendPackets("리로드: armor_enchant_list 테이블이 리로드 되었습니다");
		} else if(arg.equalsIgnoreCase("무기인챈정보")) {
			WeaponEnchantInformationTable.reload();
			gm.sendPackets("리로드: weapon_enchant_list 테이블이 리로드 되었습니다");
		} else if(arg.equalsIgnoreCase("악세인챈정보")) {
			AccessoryEnchantInformationTable.reload();
			gm.sendPackets("리로드: accessory_enchant_list 테이블이 리로드 되었습니다");
		} else if(arg.equalsIgnoreCase("세트아이템")) {
			ArmorSetTable.reload();
			gm.sendPackets("리로드: armor_set 테이블이 리로드 되었습니다");
		} else if (arg.equalsIgnoreCase("이벤트알람")) {
			try {
				EventTimeTable.getInstance().reload();
				gm.sendPackets("Reload: EventTimeTable...Loading...OK!!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (arg.equalsIgnoreCase("보스스폰")) {
			BossMonsterSpawnList.init();
			gm.sendPackets("리로드: spawnlist_bosshot 테이블이 리로드 되었습니다");
		} else if (arg.equalsIgnoreCase("클라퀘스트")) {
			QuestInfoTable.reload();
			gm.sendPackets("리로드: QuestInfoTable 테이블이 리로드 되었습니다");
		} else if (arg.equalsIgnoreCase("클라퀘스트캐릭터")) {
			CharacterQuestTable.reload();
			gm.sendPackets("리로드: CharacterQuestTable 테이블이 리로드 되었습니다");
		} else if (arg.equalsIgnoreCase("답장")) {
			L1LetterCommand.reload();
			gm.sendPackets("리로드: letter_command 테이블이 리로드 되었습니다");
		} else if(arg.equalsIgnoreCase("마법딜")){			
			SpriteInformationLoader.getInstance().reloadSpellDelayInformation();
			gm.sendPackets("리로드: MagicDelay 테이블이 리로드 되었습니다");	
		} else if(arg.equalsIgnoreCase("광역마법")){
			MJEffectModelLoader.reload();
			gm.sendPackets("리로드: EffectModel 테이블이 리로드 되었습니다.");	
			// 퀘스트 몬스터 시스템
		} else if (arg.equalsIgnoreCase("퀘스트몬스터")) {
			QuestMonsterTable.reload();
			gm.sendPackets("리로드: quest_monster 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("참시스템")){
			CharmModelLoader.reload();
			gm.sendPackets("charm_item_model 테이블이 리로드 되었습니다.");
		} else if (arg.equalsIgnoreCase("장비교환")) {
			L1TradeItemBox.load();
			gm.sendPackets(new S_SystemMessage("L1TradeItemBox Reload Complete..."));
		} else if(arg.equalsIgnoreCase("신규보호몬스터")){	
			UserProtectMonsterTable.getInstance().reload();
			gm.sendPackets("신규보호몬스터 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("자동사냥정보")){ //0627		
			PlaySupportInfoTable.getInstance();
			gm.sendPackets("자동사냥정보 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("아이템샵")){ //0627		
			ItemShopTable.getInstance().reload();
			gm.sendPackets("아이템샵 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("제한상점")){ //0627		
			LimitShopController.getInstance().reload();
			gm.sendPackets("제한상점 테이블이 리로드 되었습니다.");
		} else if(arg.equalsIgnoreCase("아이템맵")){ //0627		
			MapCharmItemTable.getInstance().Load();
			gm.sendPackets("map_charm_item 테이블이 리로드 되었습니다.");
		}else if (arg.equalsIgnoreCase("알람")) {
			EventAlarmTable.reload();
			gm.sendPackets(new S_SystemMessage("EventAlarmTable Reload Complete..."));
			EventAlarmTable.newS_ActionBox = new S_ACTION_UI(S_ACTION_UI.EVENT_NOTICE,0 , true);
			
			for(L1PcInstance player : L1World.getInstance().getAllPlayers()){
				player.sendPackets(EventAlarmTable.delS_ActionBox);// 이벤트 알림
				player.sendPackets(EventAlarmTable.newS_ActionBox);// 이벤트 알림
			}
		} else {
			gm.sendPackets("---------------리로드 목록을 보여드립니다-------------");
			gm.sendPackets("몹드랍 템드랍아이템 변신 상점 박스 스킬 몹스킬");
			gm.sendPackets("아이템 용해제1~2 무기대미지 무기스킬 레벨퀘스트");
			gm.sendPackets("컨피그 물약회복 밴아이피 용해제아덴 이벤트알람");
			gm.sendPackets("엔피씨 엔피씨액션 클랜데이터 공성 보스스폰");
			gm.sendPackets("스폰리스트 엔스폰리스트 엔피씨채팅 무기인챈트");
			gm.sendPackets("엔캐샵스폰리스트 던전 맵 벨런스 신규지급템 답장");
			gm.sendPackets("오토드랍 엔샵 엔피씨샵스폰 매크로 악세인챈정보");
			gm.sendPackets("세트아이템 로봇 제작엔피씨 자동공지 북시스템");
			gm.sendPackets("클라퀘스트 클라퀘스트캐릭터 엔코인몬스터 맵아인");
			gm.sendPackets("스페셜맵 마법딜 광역마법 엔피씨마크 아인증폭");
			gm.sendPackets("아이템스킬 크레프트인포 인형인포 제작인형컨픽");
			gm.sendPackets("액션리스너 제작확률 인형확률 장비교환 파티맵");
			gm.sendPackets("텔주문서 푸쉬시스템 퀘스트몬스터 참시스템");
			gm.sendPackets("신규보호몬스터 자존심 커스시간 버경 회복률");
			gm.sendPackets("아머클래스 엔피씨속도 낚시경험치 자동사냥정보");
		}		
	}	
	public void teleport(L1PcInstance pc, int x, int y, short mapid) {
		pc.set_teleport_x(x);
		pc.set_teleport_y(y);
		pc.set_teleport_map(mapid);
		pc.setHeading(pc.getHeading());
		Teleportation.doTeleportation(pc);
	}
}
