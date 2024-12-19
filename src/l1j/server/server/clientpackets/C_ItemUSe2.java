package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import MJShiftObject.Battle.MJShiftBattleItemWhiteList;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.AutoSystemController;
import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.MJINNSystem.MJINNRoom;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.MJRankSystem.Business.MJRankBusiness;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.Stadium.StadiumManager;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.UserCommands;
import l1j.server.server.Controller.CrockController;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1GuardianInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.function.additem2;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_BookMarkLoad;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DelSkill;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UserCommands1;
import l1j.server.server.serverpackets.S_UserCommands10;
import l1j.server.server.serverpackets.S_UserCommands11;
import l1j.server.server.serverpackets.S_UserCommands12;
import l1j.server.server.serverpackets.S_UserCommands13;
import l1j.server.server.serverpackets.S_UserCommands14;
import l1j.server.server.serverpackets.S_UserCommands15;
import l1j.server.server.serverpackets.S_UserCommands16;
import l1j.server.server.serverpackets.S_UserCommands17;
import l1j.server.server.serverpackets.S_UserCommands18;
import l1j.server.server.serverpackets.S_UserCommands2;
import l1j.server.server.serverpackets.S_UserCommands3;
import l1j.server.server.serverpackets.S_UserCommands6;
import l1j.server.server.serverpackets.S_UserCommands7;
import l1j.server.server.serverpackets.S_UserCommands8;
import l1j.server.server.serverpackets.S_UserCommands9;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class C_ItemUSe2 extends ClientBasePacket {

	private static final String C_ITEM_USE2 = "[C] C_ItemUSe2";

	private static Random _random = new Random(System.nanoTime());

	public C_ItemUSe2(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int itemObjid = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;

		L1ItemInstance l1iteminstance = pc.getInventory().getItem(itemObjid);
		
		if (l1iteminstance == null) {
			return;
		}
		
		
		if(Config.USE_SHIFT_SERVER && !MJShiftBattleItemWhiteList.getInstance().use(pc, l1iteminstance)){
			pc.sendPackets(String.format("%s(은)는 현재 사용할 수 없습니다.", l1iteminstance.getName()));
			return;
		}

		if (CommonUtil.teleport_check(pc, l1iteminstance)) {
			pc.sendPackets(new S_Paralysis(7, false));
			return;
		}

		int itemId;
		int spellsc_objid = 0;
		int l = 0;
		
		try {
			itemId = l1iteminstance.getItem().getItemId();
		} catch (Exception e) {
			return;
		}
		//TODO 아이템번호 추가해야한다 아니면 사용이안됨
		switch(itemId){
		case 3000393:
		case 40964:		// 흑마법가루....
		case 60035:		// 룬 마력제거제
		case 3000148:
		case 500715:	// 할파스 갑옷 랜덤 변경권
		case 500717:	// 랜덤 변경권: 집행급 무기
		case 5007177:	// 사신의 검 무기 변환석(집행급)
		case 719:		// 랜덤 변경권: 4대 마법
		case 707:		// 랜덤 변경권: 용갑옷
		case 706:		// 랜덤 변경권: 수호 휘장
		case 705:		// 랜덤 변경권: 수호 문장
		case 704:		// 랜덤 변경권: 축 룸티스 귀걸이
		case 703:		// 랜덤 변경권: 축 스냅퍼 반지
		case 698:		// 랜덤 변경권: 숨결
		case 776:		// 랜덤 변경권: 4단계 인형
		case 777:		// 랜덤 변경권: 5단계 인형
		case 779:		// 랜덤 변경권: 5단계 축인형
		case 778:		// 랜덤 변경권: 5단계 용인형
		case 780:		// 랜덤 변경권: 6단계 인형
			l =readD();
			break;
			default: break;
		}

		int loc_x = 0 + CommonUtil.random(-5, 5), loc_y = 0 + CommonUtil.random(-5, 5);
		int[] loc = null;

		if(itemId >= 40033 && itemId <= 40038){
			boolean _elixirContinue = true;
			if(pc.getElixirStats() == 10){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 55 && pc.getElixirStats() >= 1){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 60 && pc.getElixirStats() >= 2){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 65 && pc.getElixirStats() >= 3){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 70 && pc.getElixirStats() >= 4){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 75 && pc.getElixirStats() >= 5){
				_elixirContinue = false;
			}
		/*	if(pc.getLevel() < 80 && pc.getElixirStats() >= 6){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 85 && pc.getElixirStats() >= 7){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 90 && pc.getElixirStats() >= 8){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 95 && pc.getElixirStats() >= 9){
				_elixirContinue = false;
			}
			if(pc.getLevel() < 100 && pc.getElixirStats() >= 10){
				_elixirContinue = false;
			}*/
			if(!_elixirContinue){
				// 현재 레벨에서 섭취할 수 있는 엘릭서의 수량을 모두 사용했습니다. 50레벨부터 5레벨당 1개씩 섭취 가능합니다.
				pc.sendPackets(new S_ServerMessage(4472));
				return;
			}
		}

		L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(l);
		
		switch (itemId) {
		case 5990: // 랭킹진입물약
			if (pc.getInventory().checkItem(itemId, 1)) {
				if(MJRankBusiness.getInstance().onExpendiant(pc))
					pc.getInventory().removeItem(l1iteminstance, 1);
			}
			break;
		case 820018:
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "pureelixir"));//액션창
			break;
		/** 오만의탑 혼돈부적,변이된 부적 **/
		case 30106: {// 숨겨진 계곡 마을 귀환 부적
			loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HIDDEN_VALLEY);
			pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, false);
		}
			break;
		case 3000356:
		case 3000363: 
		case 3000364:
		case 3000365://장비교환권
			pc.sendPackets(new S_SystemMessage( "메티스 에게 전달하세요."));
			break;
		case 3000519:// 신규 사냥터 이동주문서
			if (pc.getLevel() >= Config.New_Cha1) {
				pc.sendPackets(new S_SystemMessage("당신은 신규레벨이 아닙니다."));
				return;
			}
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(3);
				int ry = _random.nextInt(3);
				int ux = 32792 + rx;
				int uy = 32737 + ry;
				pc.start_teleport(ux, uy, 785, pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 855: // 2차 코인상자
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(855, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {// 전사
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 859, 1, 0); // 전사2차
				}
				if (pc.isKnight()) {// 기사
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 857, 1, 0); // 전사2차
				}
				if (pc.isDragonknight()) {// 용기사
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 858, 1, 0); // 전사2차
				}
				if (pc.isCrown()) { // 군주
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 856, 1, 0); // 전사2차
				}
				if (pc.isWizard()) {// 마법사
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 862, 1, 0); // 전사2차
				}
				if (pc.isBlackwizard()) {// 환술사
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 863, 1, 0); // 전사2차
				}
				if (pc.isElf()) {// 요정
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 860, 1, 0); // 전사2차
				}
				if (pc.isDarkelf()) {// 다크엘프
					createNewItem2(pc, 800101, 3, 0); // 2차이동주문서
					createNewItem2(pc, 861, 1, 0); // 전사2차
				}
			}
			break;
		case 7021: // 영웅 패키지
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(7021, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {// 전사
					봉인템(pc, 816, 1, 0, 1, 0, true); // 전사 영웅패키지
				}
				if (pc.isKnight()) {// 기사
					봉인템(pc, 818, 1, 0, 1, 0, true); // 기사 영웅패키지
				}
				if (pc.isDragonknight()) {// 용기사
					봉인템(pc, 822, 1, 0, 1, 0, true); // 용기사 영웅패키지
				}
				if (pc.isCrown()) { // 군주
					봉인템(pc, 817, 1, 0, 1, 0, true); // 군주 영웅패키지
				}
				if (pc.isWizard()) {// 마법사
					봉인템(pc, 820, 1, 0, 1, 0, true); // 마법사 영웅패키지
				}
				if (pc.isBlackwizard()) {// 환술사
					봉인템(pc, 823, 1, 0, 1, 0, true); // 환술사 영웅패키지
				}
				if (pc.isElf()) {// 요정
					봉인템(pc, 819, 1, 0, 1, 0, true); // 요정 영웅패키지
				}
				if (pc.isDarkelf()) {// 다크엘프
					봉인템(pc, 821, 1, 0, 1, 0, true); // 다크엘프 영웅패키지
				}
			}
			break;
		case 751: //수련의 1차 패키지
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(751, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {// 전사
					봉인템(pc, 816, 1, 0, 1, 0, true); // 전사 영웅패키지
				}
				if (pc.isKnight()) {// 기사
					봉인템(pc, 818, 1, 0, 1, 0, true); // 기사 영웅패키지
				}
				if (pc.isDragonknight()) {// 용기사
					봉인템(pc, 822, 1, 0, 1, 0, true); // 용기사 영웅패키지
				}
				if (pc.isCrown()) { // 군주
					봉인템(pc, 817, 1, 0, 1, 0, true); // 군주 영웅패키지
				}
				if (pc.isWizard()) {// 마법사
					봉인템(pc, 820, 1, 0, 1, 0, true); // 마법사 영웅패키지
				}
				if (pc.isBlackwizard()) {// 환술사
					봉인템(pc, 823, 1, 0, 1, 0, true); // 환술사 영웅패키지
				}
				if (pc.isElf()) {// 요정
					봉인템(pc, 819, 1, 0, 1, 0, true); // 요정 영웅패키지
				}
				if (pc.isDarkelf()) {// 다크엘프
					봉인템(pc, 821, 1, 0, 1, 0, true); // 다크엘프 영웅패키지
				}
			}
			break;
		case 752: //반왕의 2차 패키지
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(752, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {// 전사
					봉인템(pc, 824, 1, 0, 1, 0, true); // 전사 영웅패키지
				}
				if (pc.isKnight()) {// 기사
					봉인템(pc, 826, 1, 0, 1, 0, true); // 기사 영웅패키지
				}
				if (pc.isDragonknight()) {// 용기사
					봉인템(pc, 830, 1, 0, 1, 0, true); // 용기사 영웅패키지
				}
				if (pc.isCrown()) { // 군주
					봉인템(pc, 825, 1, 0, 1, 0, true); // 군주 영웅패키지
				}
				if (pc.isWizard()) {// 마법사
					봉인템(pc, 828, 1, 0, 1, 0, true); // 마법사 영웅패키지
				}
				if (pc.isBlackwizard()) {// 환술사
					봉인템(pc, 831, 1, 0, 1, 0, true); // 환술사 영웅패키지
				}
				if (pc.isElf()) {// 요정
					봉인템(pc, 827, 1, 0, 1, 0, true); // 요정 영웅패키지
				}
				if (pc.isDarkelf()) {// 다크엘프
					봉인템(pc, 829, 1, 0, 1, 0, true); // 다크엘프 영웅패키지
				}
			}
			break;
		case 753: //영웅의 3차 패키지
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(753, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {// 전사
					봉인템(pc, 832, 1, 0, 1, 0, true); // 전사 영웅패키지
				}
				if (pc.isKnight()) {// 기사
					봉인템(pc, 834, 1, 0, 1, 0, true); // 기사 영웅패키지
				}
				if (pc.isDragonknight()) {// 용기사
					봉인템(pc, 838, 1, 0, 1, 0, true); // 용기사 영웅패키지
				}
				if (pc.isCrown()) { // 군주
					봉인템(pc, 833, 1, 0, 1, 0, true); // 군주 영웅패키지
				}
				if (pc.isWizard()) {// 마법사
					봉인템(pc, 836, 1, 0, 1, 0, true); // 마법사 영웅패키지
				}
				if (pc.isBlackwizard()) {// 환술사
					봉인템(pc, 839, 1, 0, 1, 0, true); // 환술사 영웅패키지
				}
				if (pc.isElf()) {// 요정
					봉인템(pc, 835, 1, 0, 1, 0, true); // 요정 영웅패키지
				}
				if (pc.isDarkelf()) {// 다크엘프
					봉인템(pc, 837, 1, 0, 1, 0, true); // 다크엘프 영웅패키지
				}
			}
			break;
		case 447011:// 프리패스상자
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(447011, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {// 전사
					봉인템(pc, 147, 1, 7, 1, 0, true); // 수련자의 도끼
					봉인템(pc, 147, 1, 7, 1, 0, true); // 수련자의 도끼
					봉인템(pc, 22300, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22301, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22302, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22303, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22304, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 각반
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 203014, 1, 0, 1, 0, true); // 50퀘 대장장이의 도끼

					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 40014, 10, 0, 1, 0, true); // 용기
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				if (pc.isKnight()) {// 기사
					봉인템(pc, 48, 1, 7, 1, 0, true); // 수련자의 양손검
					봉인템(pc, 22300, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22301, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22302, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22303, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22304, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 각반
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 56, 1, 0, 1, 0, true); // 50퀘 데스블레이드

					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 40014, 10, 0, 1, 0, true); // 용기
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				if (pc.isDragonknight()) {// 용기사
					봉인템(pc, 35, 1, 7, 1, 0, true); // 수련자의 한손검
					봉인템(pc, 22300, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22301, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22302, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22303, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22304, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 각반
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 500, 1, 0, 1, 0, true); // 50퀘 소멸자의 체인소드

					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 210035, 10, 0, 1, 0, true); // 각뼈
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				if (pc.isCrown()) { // 군주
					봉인템(pc, 35, 1, 7, 1, 0, true); // 수련자의 한손검
					봉인템(pc, 22300, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22301, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22302, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22303, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22304, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 51, 1, 0, 1, 0, true); // 50퀘 황금지팡이

					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 40031, 10, 0, 1, 0, true); // 악마의피
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				if (pc.isWizard()) {// 마법사
					봉인템(pc, 120, 1, 7, 1, 0, true); // 수련자의 지팡이
					봉인템(pc, 22306, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22307, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22308, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22309, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22310, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 각반
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 20225, 1, 0, 1, 0, true); // 50퀘 마나 수정구

					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				if (pc.isBlackwizard()) {// 환술사
					봉인템(pc, 7000222, 1, 7, 1, 0, true); // 수련자의 키링크
					봉인템(pc, 22306, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22307, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22308, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22309, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22310, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 각반
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 503, 1, 0, 1, 0, true); // 50퀘 사파이어 키링크

					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 210036, 10, 0, 1, 0, true); // 유그드라
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				if (pc.isElf()) {// 요정
					봉인템(pc, 175, 1, 7, 1, 0, true); // 수련자의 활
					봉인템(pc, 22306, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22307, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22308, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22309, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22310, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 각반
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 50, 1, 0, 1, 0, true); // 50퀘 화염의 검
					봉인템(pc, 184, 1, 0, 1, 0, true); // 50퀘 화염의 활

					봉인템(pc, 40748, 3000, 0, 1, 0, true); // 화살
					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 40068, 10, 0, 1, 0, true); // 와퍼
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				if (pc.isDarkelf()) {// 다크엘프
					봉인템(pc, 156, 1, 7, 1, 0, true); // 수련자의 크로우
					봉인템(pc, 22300, 1, 7, 1, 0, true); // +0 수련자의 가죽 투구
					봉인템(pc, 22301, 1, 7, 1, 0, true); // +0 수련자의 가죽 갑옷
					봉인템(pc, 22302, 1, 7, 1, 0, true); // +0 수련자의 망토
					봉인템(pc, 22303, 1, 7, 1, 0, true); // +0 수련자의 가죽 장갑
					봉인템(pc, 22304, 1, 7, 1, 0, true); // +0 수련자의 가죽 샌달
					봉인템(pc, 22337, 1, 0, 1, 0, true); // +0 수련자의 벨트
					봉인템(pc, 22312, 1, 7, 1, 0, true); // +0 수련자의 티셔츠
					봉인템(pc, 321515, 1, 7, 1, 0, true); // +0 수련자의 각반
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22338, 1, 0, 1, 0, true); // +0 수련자의 반지
					봉인템(pc, 22339, 1, 0, 1, 0, true); // +0 수련자의 귀걸이

					봉인템(pc, 13, 1, 0, 1, 0, true); // 50퀘 핑거오브 데스

					봉인템(pc, 40088, 10, 0, 1, 0, true); // 변신주문서
					봉인템(pc, 40100, 100, 0, 1, 0, true); // 순간이동주문서
					봉인템(pc, 40010, 300, 0, 1, 0, true); // 주홍이
					봉인템(pc, 40081, 10, 0, 1, 0, true); // 기란 귀환 주문서
					봉인템(pc, 40308, 100000, 0, 1, 0, true); // 아데나
					봉인템(pc, 3000213, 1, 0, 1, 0, true); // 자동사냥인증 3일
				}
				pc.sendPackets(new S_SystemMessage( "\\aA[GM]: 신규지원 아이템은 '\\aG7일 동안'\\aA 사용 가능합니다."));
			}
			break;
		case 40097:// 상아탑의 저주 풀기 주문서
		case 40119:// 저주 풀기 주문서
		case 140119:
		case 140329:// 원주민의 토템
			L1Item template = null;
			for (L1ItemInstance eachItem : pc.getInventory().getItems()) {
				if (eachItem.getItem().getBless() != 2) {
					continue;
				}
				if (!eachItem.isEquipped() && (itemId == 40119 || itemId == 40097)) {
					// n해주는 장비 하고 있는 것 밖에 해주 하지 않는다
					continue;
				}
				int id_normal = eachItem.getItemId() - 200000;
				template = ItemTable.getInstance().getTemplate(id_normal);
				if (template == null) {
					continue;
				}
				if (pc.getInventory().checkItem(id_normal) && template.isStackable()) {
					pc.getInventory().storeItem(id_normal, eachItem.getCount());
					pc.getInventory().removeItem(eachItem, eachItem.getCount());
				} else {
					eachItem.setItem(template);
					pc.getInventory().updateItem(eachItem, L1PcInventory.COL_ITEMID);
					pc.getInventory().saveItem(eachItem, L1PcInventory.COL_ITEMID);
					eachItem.setBless(eachItem.getBless() - 1);
					pc.getInventory().updateItem(eachItem, L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(eachItem, L1PcInventory.COL_BLESS);
				}
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_ServerMessage(155)); // \f1누군가가 도와 준 것
			break;
		case 210083:// 태고의 옥쇄
			if (client.getAccount().getCharSlot() < 8) {
				client.getAccount().setCharSlot(client, client.getAccount().getCharSlot() + 1);
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.getAccount().is_changed_slot(true);
				pc.sendPackets(new S_SystemMessage("캐릭터 슬롯 확장 완료(완전히 접속종료 후 적용됨니다.)"));
			} else {
				pc.sendPackets(new S_SystemMessage("캐릭터 슬롯이 이미 가득찼습니다."));
			}
			break;
		case 60035: // 룬 마력 제거제
			if (pc.getInventory().checkItem(60035, 1)) {
				int i = 0;
				if (l1iteminstance1 == null || l1iteminstance1.getItem() == null)
					return;

				int choiceItem = l1iteminstance1.getItem().getItemId();
				switch (choiceItem) {
				/* 55 레벨 엘릭서 룬 */
				case 222295:	i = 0;	break; 	case 222296:	i = 1;	break; 	case 222297:	i = 2;	break;
				case 222298:	i = 3;	break; 	case 222299:	i = 4;	break; 
				/* 70 레벨 엘릭서 룬 */
				case 222312:	i = 5;	break; 	case 222313:	i = 6;	break; 	case 222314:	i = 7;	break;
				case 222315:	i = 8;	break; 	case 222316:	i = 9;	break; 
				/* 80 레벨 엘릭서 룬 */
				case 900135:	i = 10;	break; 	case 900136:	i = 11;	break; 	case 900137:	i = 12;	break;
				case 900138:	i = 13;	break; 	case 900139:	i = 14;	break; 
				/* 85 레벨 엘릭서 룬 */
				case 900140:	i = 15;	break; 	case 900141:	i = 16;	break; 	case 900142:	i = 17;	break;
				case 900143:	i = 18;	break; 	case 900144:	i = 19;	break; 
				/* 90 레벨 엘릭서 룬 */
				case 900145:	i = 20;	break; 	case 900146:	i = 21;	break; 	case 900147:	i = 22;	break;
				case 900148:	i = 23;	break; 	case 900149:	i = 24;	break; 
				default:
					i = 25;
					break;
				}
				if (i >= 0 && i <= 4) { // 55 레벨 룬
					pc.getInventory().consumeItem(60035, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(60034, 1);
					pc.sendPackets(new S_SystemMessage(" 마력을 잃은 룬으로 변경되었습니다."));
				} else if (i >= 5 && i <= 9) {  // 70레벨 룬
					pc.getInventory().consumeItem(60035, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68097, 1);
					pc.sendPackets(new S_SystemMessage(" 마력을 잃은 룬(70)으로 변경되었습니다."));
				} else if (i >= 10 && i <= 14) {  // 80레벨 룬
					pc.getInventory().consumeItem(60035, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68098, 1);
					pc.sendPackets(new S_SystemMessage(" 마력을 잃은 룬(80)으로 변경되었습니다."));
				} else if (i >= 15 && i <= 19) {  // 85레벨 룬
					pc.getInventory().consumeItem(60035, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68099, 1);
					pc.sendPackets(new S_SystemMessage(" 마력을 잃은 룬(85)으로 변경되었습니다."));
				} else if (i >= 20 && i <= 24) {  // 90레벨 룬
					pc.getInventory().consumeItem(60035, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(68100, 1);
					pc.sendPackets(new S_SystemMessage(" 마력을 잃은 룬(90)으로 변경되었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("엘릭서 룬에 사용할 수 있습니다."));
					return;
				}
			}
			break;
		case 410094:// 마력의 숨결
			if(l1iteminstance1 == null)
				return;

			if (pc.getInventory().checkItem(L1ItemId.MAGIC_BREATH, 1)) {
				int[] last = { 22232, 22233, 22234, 22235, 22236, 22237, 22238, 22239, 22240, 22241, 22242,
						22243, 22244, 22245, 22246, 22247, 22248, 22249 };
				int j = 0;
				int choiceItem = l1iteminstance1.getItem().getItemId();
				switch (choiceItem) {
				case 410114:
					j = 0;
					break;
				case 410115:
					j = 1;
					break;
				case 410116:
					j = 2;
					break;
				case 410117:
					j = 3;
					break;
				case 410118:
					j = 4;
					break;
				case 410119:
					j = 5;
					break;
				case 410109:
					j = 6;
					break;
				case 410124:
					j = 7;
					break;
				case 410110:
					j = 8;
					break;
				case 410125:
					j = 9;
					break;
				case 410111:
					j = 10;
					break;
				case 410126:
					j = 11;
					break;
				case 410112:
					j = 12;
					break;
				case 410113:
					j = 13;
					break;
				case 410120:
					j = 14;
					break;
				case 410121:
					j = 15;
					break;
				case 410122:
					j = 16;
					break;
				case 410123:
					j = 17;
					break;
				default:
					j = 18;
					break;
				}
				if (j == 18) {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				} else {
					pc.getInventory().consumeItem(L1ItemId.MAGIC_BREATH, 1);
					pc.getInventory().consumeItem(choiceItem, 1);
					pc.getInventory().storeItem(last[j], 1);
					pc.sendPackets( new S_SystemMessage("" + l1iteminstance1.getItem().getName() + "의 봉인이 해제 되었습니다."));
				}
			}
			break;
		case 210082: {// 균열의 핵
			int itemId2 = l1iteminstance1.getItem().getItemId();
			if (itemId2 == 210075) {
				if (pc.getInventory().checkItem(210075)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(210076, 1);
				}
			} else if (itemId2 == 210079) {
				if (pc.getInventory().checkItem(210079)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(210080, 1);
				}
			} else if (itemId2 == 500208) {
				if (pc.getInventory().checkItem(500208)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(500202, 1);
				}
			} else if (itemId2 == 500209) {
				if (pc.getInventory().checkItem(500209)) {
					pc.getInventory().removeItem(l1iteminstance1, 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.getInventory().storeItem(500203, 1);
				}
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
		}
			break;
		case 40925:// 정화의 물약
			int earingId = l1iteminstance1.getItem().getItemId();
			if (earingId >= 40987 && 40989 >= earingId) { // 저주해진 블랙 귀 링
				if (_random.nextInt(100) < Config.CREATE_CHANCE_RECOLLECTION) {
					createNewItem(pc, earingId + 186, 1);
				} else {
					pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // \f1%0이
					// 증발하고
					// 있지
					// 않게
					// 되었습니다.
				}
				pc.getInventory().removeItem(l1iteminstance1, 1);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 40926:
		case 40927:
		case 40928:
		case 40929:
			// 신비한 물약:1단계(1~4 단계)
			int earing2Id = l1iteminstance1.getItem().getItemId();
			int potion1 = 0;
			int potion2 = 0;
			if (earing2Id >= 41173 && 41184 >= earing2Id) {
				// 귀 링류
				if (itemId == 40926) {
					potion1 = 247;
					potion2 = 249;
				} else if (itemId == 40927) {
					potion1 = 249;
					potion2 = 251;
				} else if (itemId == 40928) {
					potion1 = 251;
					potion2 = 253;
				} else if (itemId == 40929) {
					potion1 = 253;
					potion2 = 255;
				}
				if (earing2Id >= (itemId + potion1) && (itemId + potion2) >= earing2Id) {
					if ((_random.nextInt(99) + 1) < Config.CREATE_CHANCE_MYSTERIOUS) {
						createNewItem(pc, (earing2Id - 12), 1);
						pc.getInventory().removeItem(l1iteminstance1, 1);
						pc.getInventory().removeItem(l1iteminstance, 1);
					} else {
						pc.sendPackets(new S_ServerMessage(160, l1iteminstance1.getName()));
						// \f1%0이%2 강렬하게%1 빛났습니다만, 다행히 무사하게 살았습니다.
						pc.getInventory().removeItem(l1iteminstance, 1);
					}
				} else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
					// 일어나지
					// 않았습니다.
				}
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 3000106:// 수련던전1층이동주문서
			if (pc.getLevel() >= 90) {
				pc.sendPackets(new S_SystemMessage( "해당 던전은 75까지만 입장 가능합니다."));
				return;
			}
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 32809 + rx;
				int uy = 32727 + ry;
				if (itemId == 3000106) {
					pc.start_teleport(ux, uy, 25, pc.getHeading(), 169, true, false);
				}
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 3000444:// +10 악몽의 장궁 (제작)
			pc.sendPackets(new S_UserCommands8(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(3000360, 100)) {
				pc.sendPackets(new S_SystemMessage( "환생의 보석이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(1136, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 악몽의 장궁이 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000444, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(3000360, 100);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(1136, 8, 1);
				
				제작비법서템지급(pc, 1136, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc,"+10 악몽의 장궁을 제작 하였습니다..", 1));
			}
			break;
		case 3000445:// +10 태풍의 도끼 (제작)
			pc.sendPackets(new S_UserCommands9(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40513, 10)) {
				pc.sendPackets(new S_SystemMessage( "오우거의 눈물이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(203006, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 태풍의 도끼가 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000445, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40513, 10);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(203006, 8, 1);
				
				제작비법서템지급(pc, 203006, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 태풍의 도끼를 제작 하였습니다.", 1));
			}
			break;
		case 3000446:// +10 섬멸자의 체인소드 (제작)
			pc.sendPackets(new S_UserCommands10(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(700020, 15)) {
				pc.sendPackets(new S_SystemMessage( "눈의 결정이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(203017, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 섬멸자의 체인소드가 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000446, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(700020, 15);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(203017, 8, 1);
				
				제작비법서템지급(pc, 203017, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 섬멸자의 체인소드를 제작 하였습니다.", 1));
			}
			break;
		case 3000447:// +10 진 싸울아비 대검 (제작)
			pc.sendPackets(new S_UserCommands11(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(410061, 270)) {
				pc.sendPackets(new S_SystemMessage( "마물의 기운이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(505010, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 진 싸울아비 대검이 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000447, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(410061, 270);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(505010, 8, 1);
				
				제작비법서템지급(pc, 505010, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 진 싸울아비 대검을 제작 하였습니다.", 1));
			}
			break;
		case 3000448:// +10 커츠의 검 (제작)
			pc.sendPackets(new S_UserCommands12(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40677, 15)) {
				pc.sendPackets(new S_SystemMessage( "어둠의 주괴가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(54, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 커츠의 검이 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000448, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40677, 15);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(54, 8, 1);

				제작비법서템지급(pc, 54, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 커츠의 검을 제작 하였습니다.", 1));
			}
			break;
		case 3000449:// +10 데스나이트의 불검 (제작)
			pc.sendPackets(new S_UserCommands13(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40644, 50)) {
				pc.sendPackets(new S_SystemMessage( "미로 구조도가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(58, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 데스나이트의 불검이 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000449, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40644, 50);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(58, 8, 1);

				제작비법서템지급(pc, 58, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 데스나이트의 불검을 제작 하였습니다.", 1));
			}
			break;
		case 3000450:// +10 론드 이도류 (제작)
			pc.sendPackets(new S_UserCommands17(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40324, 100)) {
				pc.sendPackets(new S_SystemMessage( "암황석가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(76, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 론드 이도류이 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000450, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40324, 100);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(76, 8, 1);

				제작비법서템지급(pc, 76, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 론드 이도류을 제작 하였습니다.", 1));
			}
			break;
		case 3000451:// +10 진 레이피어 (제작)
			pc.sendPackets(new S_UserCommands14(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40487, 80)) {
				pc.sendPackets(new S_SystemMessage( "황금 판금이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(505009, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 진 레이피어이 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000451, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40487, 80);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(505009, 8, 1);

				제작비법서템지급(pc, 505009, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 진 레이피어을 제작 하였습니다.", 1));
			}
			break;
		case 3000452:// +10 포효의 이도류 (제작)
			pc.sendPackets(new S_UserCommands18(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40324, 100)) {
				pc.sendPackets(new S_SystemMessage( "암황석가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(203018, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 포효의 이도류가 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000452, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40324, 100);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(203018, 8, 1);

				제작비법서템지급(pc, 203018, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 포효의 이도류가 제작 하였습니다.", 1));
			}
			break;
		case 3000453:// +10 제로스의 지팡이 (제작)
			pc.sendPackets(new S_UserCommands15(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40969, 700)) {
				pc.sendPackets(new S_SystemMessage( "영혼의 결정체가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(202003, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 제로스의 지팡이가 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000453, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40969, 700);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(202003, 8, 1);

				제작비법서템지급(pc, 202003, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 제로스의 지팡이가 제작 하였습니다.", 1));
			}
			break;
		case 3000454:// +10 나이트발드의 양손검 (제작)
			pc.sendPackets(new S_UserCommands16(1));
			if (!pc.getInventory().checkItem(40395, 10)) {
				pc.sendPackets(new S_SystemMessage( "수룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 10)) {
				pc.sendPackets(new S_SystemMessage( "풍룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40396, 10)) {
				pc.sendPackets(new S_SystemMessage( "지룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40393, 10)) {
				pc.sendPackets(new S_SystemMessage( "화룡 비늘이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40646, 60)) {
				pc.sendPackets(new S_SystemMessage( "바실리스크의 뿔이 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkItem(810003, 3)) {
				pc.sendPackets(new S_SystemMessage( "장인의 무기 마법 주문서가 부족 합니다."));
				return;
			} else if (!pc.getInventory().checkEnchantItem(59, 8, 1)) {
				pc.sendPackets(new S_SystemMessage( "+8 나이트발드의 양손검이 부족 합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000454, 1);
				pc.getInventory().consumeItem(40393, 10);
				pc.getInventory().consumeItem(40394, 10);
				pc.getInventory().consumeItem(40395, 10);
				pc.getInventory().consumeItem(40396, 10);
				pc.getInventory().consumeItem(40646, 60);
				pc.getInventory().consumeItem(810003, 3);
				pc.getInventory().consumeEnchantItem(59, 8, 1);

				제작비법서템지급(pc, 59, 1, 10);
				pc.sendPackets(new S_ChatPacket(pc, "+10 나이트발드의 양손검이 제작 하였습니다.", 1));
			}
			break;
		case 3000029:// 싸울아비장검 제작
			pc.sendPackets(new S_UserCommands1(1));
			if (!pc.getInventory().checkItem(40508, 500)) { // 오리하루콘
				pc.sendPackets(new S_ChatPacket(pc, "오리하루콘이 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40460, 30)) {
				pc.sendPackets(new S_ChatPacket(pc, "아시타지오의재가 부족합니다."));
				return;

			} else if (!pc.getInventory().checkItem(40052, 5)) {
				pc.sendPackets(new S_ChatPacket(pc, "최고급 다이아몬드가 부족합니다."));
				return;

			} else if (!pc.getInventory().checkItem(40053, 5)) {
				pc.sendPackets(new S_ChatPacket(pc, "최고급 루비가 부족합니다."));
				return;

			} else if (!pc.getInventory().checkItem(40054, 5)) {
				pc.sendPackets(new S_ChatPacket(pc, "최고급 사파이어가 부족합니다."));
				return;

			} else if (!pc.getInventory().checkItem(40055, 5)) {
				pc.sendPackets(new S_ChatPacket(pc, "최고급 에메랄드가 부족합니다."));
				return;

			} else if (!pc.getInventory().checkItem(40460, 30)) {
				pc.sendPackets(new S_ChatPacket(pc, "아시타지오의재가 부족합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000029, 1);
				pc.getInventory().consumeItem(40508, 500);
				pc.getInventory().consumeItem(40460, 30);
				pc.getInventory().consumeItem(40052, 5);
				pc.getInventory().consumeItem(40053, 5);
				pc.getInventory().consumeItem(40054, 5);
				pc.getInventory().consumeItem(40055, 5);
				pc.getInventory().storeItem(57, 1);
				pc.sendPackets(new S_ChatPacket(pc, "싸울아비 장검을 얻었습니다."));
			}
			break;
		case 3000030:// 사이하의 활 (제작)
			pc.sendPackets(new S_UserCommands2(1));
			if (!pc.getInventory().checkItem(181, 1)) { // 장궁
				pc.sendPackets(new S_ChatPacket(pc, "장궁 (1)개가 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40394, 15)) {
				pc.sendPackets(new S_ChatPacket(pc, "풍룡의 비늘이 부족합니다."));
				return;

			} else if (!pc.getInventory().checkItem(40491, 30)) {
				pc.sendPackets(new S_ChatPacket(pc, "그리폰의 깃털이 부족합니다."));
				return;

			} else if (!pc.getInventory().checkItem(40498, 50)) {
				pc.sendPackets(new S_ChatPacket(pc, "바람의 눈물이 부족합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000030, 1);
				pc.getInventory().consumeItem(181, 1);
				pc.getInventory().consumeItem(40394, 15);
				pc.getInventory().consumeItem(40491, 30);
				pc.getInventory().consumeItem(40498, 50);
				pc.getInventory().storeItem(190, 1);
				pc.sendPackets(new S_ChatPacket(pc, "사이하의 활을 얻었습니다."));
			}
			break;
		case 3000031:// 흑왕도 (제작)
			pc.sendPackets(new S_UserCommands3(1));
			if (!pc.getInventory().checkItem(81, 1)) { // 흑이
				pc.sendPackets(new S_ChatPacket(pc, "흑빛의 이도류 (1)개가 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40466, 1)) {
				pc.sendPackets(new S_ChatPacket(pc, "용의 심장이 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40525, 3)) { // 그랑카이
				pc.sendPackets(new S_ChatPacket(pc, "그랑카인의 눈물이 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40413, 9)) { // 얼음여왕
				pc.sendPackets(new S_ChatPacket(pc, "얼음여왕의 숨결이 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40402, 10)) { // 저주
				// 피혁
				pc.sendPackets(new S_ChatPacket(pc, "얼음여왕의 숨결이 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40053, 3)) {
				pc.sendPackets(new S_ChatPacket(pc, "최고급 루비가 부족합니다."));
				return;
			} else if (!pc.getInventory().checkItem(40308, 100000)) {
				pc.sendPackets(new S_ChatPacket(pc, "아데나가 부족합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000031, 1);
				pc.getInventory().consumeItem(81, 1);
				pc.getInventory().consumeItem(40525, 3);
				pc.getInventory().consumeItem(40413, 9);
				pc.getInventory().consumeItem(40402, 10);
				pc.getInventory().consumeItem(40053, 3);
				pc.getInventory().consumeItem(40466, 1);
				pc.getInventory().consumeItem(40308, 100000);
				pc.getInventory().storeItem(84, 1);
				pc.sendPackets(new S_ChatPacket(pc, "흑왕도를 얻었습니다."));
			}
			break;
		case 3000354:// 에바의왕국 수중던전
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 32734 + rx;
				int uy = 32841 + ry;
				pc.start_teleport(ux, uy, 63, pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 3000394:// 화룡의 둥지
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 33713 + rx;
				int uy = 32301 + ry;
				pc.start_teleport(ux, uy, 4, pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 3000396:// 풍룡의 둥지
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 34180 + rx;
				int uy = 32857 + ry;
				pc.start_teleport(ux, uy, 4, pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 3000395:// 발라카스 둥지
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);	
				int ux = 32779 + rx;
				int uy = 32751 + ry;
				pc.start_teleport(ux, uy, 2210, pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 40824:// 수련던전2층이동주문서
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 32807 + rx;
				int uy = 32747 + ry;
				if (itemId == 40824) {
					pc.start_teleport(ux, uy, 26, pc.getHeading(), 169, true, false);
				}
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 40825:// 수련던전3층이동주문서
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 32810 + rx;
				int uy = 32765 + ry;
				if (itemId == 40825) {
					pc.start_teleport(ux, uy, 27, pc.getHeading(), 169, true, false);
				}
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 40417:// 서울 크리스탈
			if ((pc.getX() >= 32667 && pc.getX() <= 32673)// 해적섬
					&& (pc.getY() >= 32978 && pc.getY() <= 32984) && pc.getMapId() == 440) {
				pc.start_teleport(32922, 32812, 430, 5, 169, true, false);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
				// \f1 아무것도 일어나지 않았습니다.
			}
			break;
		case 43201: //클라우디아 마을 부적
			if (pc.getLevel() >=56) {
				pc.sendPackets(new S_SystemMessage( "레벨 55 이하만 사용이 가능합니다."));
				return;
			}
			pc.start_teleport(32649, 32865, 7783, pc.getHeading(), 169, true, false);
//			pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(), l1iteminstance.getItem().get_mapid(), pc.getHeading(), 169, true, false);
			break;
		case 40003: // 랜턴오일
			for (L1ItemInstance lightItem : pc.getInventory().getItems()) {
				if (lightItem.getItem().getItemId() == 40002 || lightItem.getItem().getItemId() == 7005) {
					lightItem.setRemainingTime(l1iteminstance.getItem().getLightFuel());
					pc.sendPackets(new S_ItemName(lightItem));
					pc.sendPackets(new S_ServerMessage(230));
					break;
				}
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000255: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId()); // --
			L1ItemInstance weapon_item = pc.getInventory().getItem(l); // --
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 203006 || id == 59 || id == 202003 || id == 1136 || id == 203018 || id == 203017 || id == 1120)) {
				pc.sendPackets(new S_ServerMessage(79)); // -- 아무일도 일어나지
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 203006, 59, 202003, 1136, 203018, 203017, 1120 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 100001: { // 인형 변경 비법서
			int dollId = l1iteminstance1.getItem().getItemId();
			boolean isAppear = true;
			L1DollInstance doll = null;
			L1ItemInstance item = null;
			Object[] dollList = pc.getDollList().values().toArray();
			for (Object dollObject : dollList) {
				doll = (L1DollInstance) dollObject;
				if (doll.getItemObjId() == itemId) {
					isAppear = false;
					break;
				}
			}
			if (isAppear) {
				if (dollList.length >= 1) {
					pc.sendPackets(new S_SystemMessage("인형을 소환한 상태로 변경할 수 없습니다."));
					return;
				}
			}
			if (dollId == 41248 // 마법인형 : 버그베어
					|| dollId == 41250 // 마법인형 : 늑대인간
					|| dollId == 210086 // 마법인형 : 시댄서
					|| dollId == 210072 // 마법인형 : 크러스트시안
					|| dollId == 210070 // 마법인형 : 돌골렘
					|| dollId == 210096 // 마법인형 : 에티
					|| dollId == 500213 // 마법인형 : 에틴
					|| dollId == 41249 // 마법인형 : 서큐버스
					|| dollId == 210071 // 마법인형 : 장로
					|| dollId == 210105 // 마법인형 : 코카트리스
					|| dollId == 447012 // 마법인형 : 챔피언
					|| dollId == 447013 // 마법인형 : 새
					|| dollId == 447014 // 마법인형 : 강남스타일
					|| dollId == 500215 // 마법인형 : 허수아비
					|| dollId == 447016 // 마법인형 : 리치
					|| dollId == 447015 // 마법인형 : 그렘린
					|| dollId == 500214 // 마법인형 : 스파토이
					|| dollId == 447017 // 마법인형 : 드레이크
					|| dollId == 510216 // 마법인형 : 눈사람A
					|| dollId == 510217 // 마법인형 : 눈사람B
					|| dollId == 510218 // 마법인형 : 눈사람C
					|| dollId == 510219 // 마법인형 : 자이언트
					|| dollId == 510220 // 마법인형 : 사이클롭스
					|| dollId == 510221 // 마법인형 : 흑장로
					|| dollId == 510222) { // 마법인형 : 서큐버스퀸

				pc.getInventory().removeItem(l1iteminstance1, 1);
				pc.getInventory().removeItem(l1iteminstance, 1);

				int i = _random.nextInt(1060) + 1;
				if (i <= 150) { // 15%
					item = pc.getInventory().storeItem(41248, 1); // 버그베어
				} else if (i <= 260) { // 11%
					item = pc.getInventory().storeItem(41250, 1); // 늑대인간
				} else if (i <= 370) { // 11%
					item = pc.getInventory().storeItem(210086, 1); // 시댄서
				} else if (i <= 480) { // 11%
					item = pc.getInventory().storeItem(210072, 1); // 크러스트시안
				} else if (i <= 590) { // 11%
					item = pc.getInventory().storeItem(210070, 1); // 돌골렘
				} else if (i <= 680) { // 9%
					item = pc.getInventory().storeItem(210096, 1); // 에티
				} else if (i <= 770) { // 9%
					item = pc.getInventory().storeItem(500213, 1); // 에틴
				} else if (i <= 810) { // 4%
					item = pc.getInventory().storeItem(41249, 1); // 서큐버스
				} else if (i <= 850) { // 4%
					item = pc.getInventory().storeItem(210071, 1); // 장로
				} else if (i <= 880) { // 3%
					item = pc.getInventory().storeItem(210105, 1); // 코카트리스
				} else if (i <= 900) { // 2%
					item = pc.getInventory().storeItem(447012, 1); // 챔피언
				} else if (i <= 920) { // 2%
					item = pc.getInventory().storeItem(447013, 1); // 새
				} else if (i <= 940) { // 2%
					item = pc.getInventory().storeItem(447014, 1); // 강남스타일
				} else if (i <= 960) { // 2%
					item = pc.getInventory().storeItem(500215, 1); // 허수아비
				} else if (i <= 970) { // 1%
					item = pc.getInventory().storeItem(447016, 1); // 리치
				} else if (i <= 980) { // 1%
					item = pc.getInventory().storeItem(447015, 1); // 그렘린
				} else if (i <= 990) { // 1%
					item = pc.getInventory().storeItem(500214, 1); // 스파토이
				} else if (i <= 1000) { // 1%
					item = pc.getInventory().storeItem(447017, 1); // 드레이크
				} else if (i <= 1010) { // 1%
					item = pc.getInventory().storeItem(510216, 1); // 눈사람A
				} else if (i <= 1020) { // 1%
					item = pc.getInventory().storeItem(510217, 1); // 눈사람B
				} else if (i <= 1030) { // 1%
					item = pc.getInventory().storeItem(510218, 1); // 눈사람C
				} else if (i <= 1040) { // 1%
					item = pc.getInventory().storeItem(510221, 1); // 흑장로
				} else if (i <= 1050) { // 1%
					item = pc.getInventory().storeItem(510222, 1); // 서큐버스
					// 퀸
				} else if (i <= 1055) { // 0.5%
					item = pc.getInventory().storeItem(510219, 1); // 자이언트
				} else if (i <= 1060) { // 1%
					item = pc.getInventory().storeItem(510220, 1); // 사이클롭스
				}
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else {
				pc.sendPackets(new S_SystemMessage("변경할 수 없는 아이템 입니다."));
			}
		}
			break;
		case 3000148: {
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
		/*	if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}*/
			int id = weapon_item.getItemId();
			if (!(id == 616 || id == 617 || id == 618 || id == 619 || id == 620 || id == 622 || id == 623 || id == 505015)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 616, 617, 618, 619, 620, 622, 623, 505015 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 500717: { // 랜덤 무기 변환석(집행급)
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 61 || id == 12 || id == 134 || id == 86 || id == 202011 || id == 202012 || id == 202013 || id == 202014 || id == 66)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 61, 12, 134, 86, 202011, 202012, 202013, 202014, 66 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
			
		case 5007177: { // 사신검 변환석
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 61 || id == 12 || id == 134 || id == 86 || id == 202011 || id == 202012 || id == 202013 || id == 202014 || id == 66)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 294 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 719: {	// 랜덤 변경권: 4대 마법
			L1ItemInstance Skill_item = pc.getInventory().getItem(l);
			if (Skill_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = Skill_item.getItemId();
			if (!(id == 40222 || id == 41148 || id == 5559 || id == 210125)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int[] random_item = { 40222, 41148, 5559, 210125 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1);
			pc.getInventory().removeItem(Skill_item, 1);
			pc.getInventory().removeItem(l1iteminstance, 1); // 랜덤 변경권이 제거 안되서 유정훈 개발자가 추가로 알려준 코드
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
		}
			break;
			
			
			/* 22200: 파푸완력
			 * 22201: 파푸예지
			 * 22202: 파푸인내
			 * 22203: 파푸마력
			 * 22204: 린드완력
			 * 22205: 린드예지
			 * 22206: 린드인내
			 * 22207: 린드마력
			 * 22208: 발라완력
			 * 22209: 발라예지
			 * 22210: 발라인내
			 * 22211: 발라마력
			 * 22196: 안타완력
			 * 22197: 안타예지
			 * 22198: 안타인내
			 * 22199: 안타마력
			 */
		case 707: {	// 랜덤 변경권: 발라갑옷
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance armor_item = pc.getInventory().getItem(l);
			if (enchant_item == null || armor_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = armor_item.getItemId();
			if (!(id == 22208 || id == 22209 || id == 22210 || id == 22211)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = armor_item.getEnchantLevel();
			int[] random_item = { 22208, 22209, 22210, 22211 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(armor_item);
		}
			break;
			
		case 500715: {	// 할파스 갑옷 랜덤 변경권
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance armor_item = pc.getInventory().getItem(l);
			if (enchant_item == null || armor_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = armor_item.getItemId();
			if (!(id == 111137 || id == 111141 || id == 111140 )) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = armor_item.getEnchantLevel();
			int[] random_item = { 111137, 111141, 111140 }; // 할파스의 완력, 할파스의 예지력, 할파스의 마력
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(armor_item);
		}
			break;

		case 706: {	// 랜덤 변경권: 수호 휘장
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 900081 || id == 900082 || id == 900083)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 900081, 900082, 900083 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 705: {	// 랜덤 변경권: 수호 문장
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 900124 || id == 900125 || id == 900126)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 900124, 900125, 900126 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 704: {	// 랜덤 변경권: 축 룸티스 귀걸이
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 222337 || id == 222338 || id == 222339 || id == 222341)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 222337, 222338, 222339, 222341 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 703: {	// 랜덤 변경권: 축 스냅퍼 반지
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 222330 || id == 222331 || id == 222332 || id == 222333 || id == 222334 || id == 222335 || id == 222336)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 222330, 222331, 222332, 222333, 222334, 222335, 222336 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
		case 30156:
			L1SkillUse aa = new L1SkillUse();
			aa.handleCommands(pc, L1SkillId.DRAGON_HUNTER_BLESS, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
			pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009));
			pc.sendPackets(new S_SkillSound(pc.getId(), 9009));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 510012://미소피아의 성장 축복
			L1SkillUse cc = new L1SkillUse();
			cc.handleCommands(pc, L1SkillId.miso1, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 510013://미소피아의 성장 축복
			L1SkillUse dd = new L1SkillUse();
			dd.handleCommands(pc, L1SkillId.miso2, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 510014://미소피아의 성장 축복
			L1SkillUse ff = new L1SkillUse();
			ff.handleCommands(pc, L1SkillId.miso3, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100076:
			long timeItem7 = System.currentTimeMillis();
			int useTimeItem7 = 600;// 10분뒤
			if(pc.hasSkillEffect(L1SkillId.DRAGON_SET)){
				int n = pc.getSkillEffectTimeSec(L1SkillId.DRAGON_SET);
				pc.sendPackets(new S_SystemMessage(String.format("%d초 후에 사용할 수 있습니다.", n)));
				return;
			}
			L1SkillUse bb = new L1SkillUse();
			bb.handleCommands(pc, L1SkillId.DRAGON_SET, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009));
			pc.sendPackets(new S_SkillSound(pc.getId(), 9009));
			pc.setSkillEffect(L1SkillId.DRAGON_SET, useTimeItem7 * 1000);
			break;
		case 40566:// 신비한 소라 껍데기
			if (pc.isElf()
					&& (pc.getX() >= 33971 && pc.getX() <= 33975)
					&& (pc.getY() >= 32324 && pc.getY() <= 32328) && pc.getMapId() == 4
					&& !pc.getInventory().checkItem(40548)) { // 망령의 봉투
				boolean found = false;
				L1MonsterInstance mob = null;
				for (L1Object obj : L1World.getInstance().getObject()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob != null) {
							if (mob.getNpcTemplate().get_npcId() == 45300) {
								found = true;
								break;
							}
						}
					}
				}
				if (found) {
					pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				} else {
					L1SpawnUtil.spawn(pc, 45300, 0, 0); // 고대인의 망령
				}
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
			}
			break;
		case 40557:// 살생부 (글루딘 마을)
			if (pc.getX() == 32620 && pc.getY() == 32641 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45883) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45883, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40558:// 살생부 (기란 마을)
			if (pc.getX() == 33513 && pc.getY() == 32890 && pc.getMapId() == 4) {
				L1NpcInstance npc = null;
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45889) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45889, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40559:// 살생부 (아덴 마을)
			if (pc.getX() == 34215 && pc.getY() == 33195 && pc.getMapId() == 4) {
				L1NpcInstance npc = null;
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45888) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45888, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40560:// 살생부 (우드벡 마을)
			if (pc.getX() == 32580 && pc.getY() == 33260 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45886) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45886, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40561:// 살생부 (켄트 마을)
			if (pc.getX() == 33046 && pc.getY() == 32806 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45885) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45885, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40562:// 살생부 (하이네 마을)
			if (pc.getX() == 33447 && pc.getY() == 33476 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45887) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45887, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40563:// 살생부 (화전민 마을)
			if (pc.getX() == 32730 && pc.getY() == 32426 && pc.getMapId() == 4) {
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) object;
						if (npc.getNpcTemplate().get_npcId() == 45884) {
							pc.sendPackets(new S_ServerMessage(79));
							return;
						}
					}
				}
				L1SpawnUtil.spawn(pc, 45884, 0, 300000);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40826:// 수련던전4층이동주문서
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ry = _random.nextInt(2);
				int ux = 32799 + rx;
				int uy = 32798 + ry;
				if (itemId == 40826) {
					// L1Teleport.teleport(pc, ux, uy, (short) 28,
					// pc.getHeading(), true);
					pc.start_teleport(ux, uy, 28, pc.getHeading(), 169, true, false);
				}
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(647));
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 3000393:
			if (l1iteminstance1.getItem().getType2() == 0){
				pc.sendPackets(new S_SystemMessage("장비에만 사용이 가능합니다."));
				return;
			}
			if (l1iteminstance1.get_durability() > 0){
				pc.sendPackets(new S_SystemMessage("손상된 아이템은 변경이 불가합니다."));
				return;
			}
			if (l1iteminstance1.isEquipped()){
				pc.sendPackets(new S_SystemMessage("착용중인 아이템에는 사용이 불가합니다."));
				return;
			}
			if (!l1iteminstance1.isIdentified()) {
				pc.sendPackets(new S_SystemMessage("확인된 아이템에만 사용이 가능합니다."));
				return;
			}
			l1iteminstance1.setIdentified(false);
			pc.getInventory().tradeItem(l1iteminstance1, l1iteminstance1.getCount(), pc.getInventory());
			pc.sendPackets(new S_SystemMessage(l1iteminstance1.getLogName() + "에 어두운 그림자가 스며듭니다."));
			pc.sendPackets(new S_SystemMessage("해당아이템이 새로 갱신되었습니다."));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40017:// 비취 물약
		case 40507:// 엔트의 줄기
			if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
				pc.sendPackets(new S_ServerMessage(698)); // 마력에 의해 아무것도
			} else {
				cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
				pc.sendPackets(new S_SkillSound(pc.getId(), 192));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 192));
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.curePoison();
			}
			break;
		case 40616:
		case 40782:
		case 40783:// 그림자의 신전 3층의 열쇠
			if ((pc.getX() >= 32698 && pc.getX() <= 32702) && (pc.getY() >= 32894 && pc.getY() <= 32898)
					&& pc.getMapId() == 523) {
				pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
						l1iteminstance.getItem().get_mapid(), 5, 169, true, false);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40700: // 실버 플룻
			pc.sendPackets(new S_Sound(10));
			pc.broadcastPacket(new S_Sound(10));
			if ((pc.getX() >= 32619 && pc.getX() <= 32623) && (pc.getY() >= 33120 && pc.getY() <= 33124)
					&& pc.getMapId() == 440) {
				// 해적 시마마에반매직 스퀘어 좌표
				boolean found = false;
				L1MonsterInstance mob = null;
				for (L1Object obj : L1World.getInstance().getObject()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob != null) {
							if (mob.getNpcTemplate().get_npcId() == 45875) {
								found = true;
								break;
							}
						}
					}
				}
				if (found) {
				} else {
					L1SpawnUtil.spawn(pc, 45875, 0, 0);
				}
			}
			break;
		case 41121:// 카헬의 계약서
			if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END
					|| pc.getInventory().checkItem(41122, 1)) {
				pc.sendPackets(new S_ServerMessage(79));
			} else {
				createNewItem(pc, 41122, 1);
			}
			break;
		case 41130:// 혈흔의 계약서
			if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END
					|| pc.getInventory().checkItem(41131, 1)) {
				pc.sendPackets(new S_ServerMessage(79));
			} else {
				createNewItem(pc, 41131, 1);
			}
			break;
		case 40692:// 완성된 보물의 지도
			if (pc.getInventory().checkItem(40621)) {
				// \f1 아무것도 일어나지 않았습니다.
				pc.sendPackets(new S_ServerMessage(79));
			} else if ((pc.getX() >= 32856 && pc.getX() <= 32858) && (pc.getY() >= 32857 && pc.getY() <= 32858)
					&& pc.getMapId() == 443) { // 해적섬의
				pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
						l1iteminstance.getItem().get_mapid(), 5, 169, true, false);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 41208:// 사그러지는 영혼
			if ((pc.getX() >= 32844 && pc.getX() <= 32845) && (pc.getY() >= 32693 && pc.getY() <= 32694)
					&& pc.getMapId() == 550) {
				pc.start_teleport(l1iteminstance.getItem().get_locx(), l1iteminstance.getItem().get_locy(),
						l1iteminstance.getItem().get_mapid(), 5, 169, true, false);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 40964:// 흑마법 가루
			int historybookId = l1iteminstance1.getItem().getItemId();
			if (historybookId >= 41011 && 41018 >= historybookId) {
				if ((_random.nextInt(99) + 1) <= Config.CREATE_CHANCE_HISTORY_BOOK) {
					createNewItem(pc, historybookId + 8, 1);
				} else {
					pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName()));
				}
				pc.getInventory().removeItem(l1iteminstance1, 1);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 41036:// 풀
			int diaryId = l1iteminstance1.getItem().getItemId();
			if (diaryId >= 41038 && 41047 >= diaryId) {
				if ((_random.nextInt(99) + 1) <= Config.CREATE_CHANCE_DIARY) {
					createNewItem(pc, diaryId + 10, 1);
				} else {
					pc.sendPackets(new S_ServerMessage(158, l1iteminstance1.getName())); // \f1%0이
					// 증발하고
					// 있지
					// 않게
					// 되었습니다.
				}
				pc.getInventory().removeItem(l1iteminstance1, 1);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 30084:// 상아탑의 비취 물약
			/*if (!(pc.getMapId() == 7783 || pc.getMapId() == 12152 || pc.getMapId() == 12149 || pc.getMapId() == 12154 || pc.getMapId() == 3
					|| pc.getMapId() == 12358 || pc.getMapId() == 12153 || pc.getMapId() == 12146
					|| pc.getMapId() == 12147 || pc.getMapId() == 12148 || pc.getMapId() == 12258
					|| pc.getMapId() == 12150 || pc.getMapId() == 12257 || pc.getMapId() == 12358)) {
				pc.sendPackets(new S_SystemMessage( "클라우디아 맵에서만 사용이 가능합니다."));
				return;
			}*/
			if (pc.hasSkillEffect(71) == true) { // 디케이포션 상태
				pc.sendPackets(new S_ServerMessage(698)); // 마력에 의해 아무것도
				// 마실 수가
				// 없습니다.
			} else {
				cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
				pc.sendPackets(new S_SkillSound(pc.getId(), 192));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 192));
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.curePoison();
			}
			break;
		case 3000209:// 자동사냥 1일
		case 3000213:// 자동사냥 3일
		case 3000214:// 자동사냥 7일
			AutoSystemController.getInstance().removeAuto(pc);
			break;
		case 3000193:// 요던1층
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int rx = _random.nextInt(2);
				int ux = 32805 + rx;
				int uy = 32724 + rx;
				
				pc.start_teleport(ux, uy, 19, pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
				cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			}
			break;
		case 3000226:// 소환이벤트막대1
			int rx10 = _random.nextInt(10);
			int ry10 = _random.nextInt(10);

			int rx11 = _random.nextInt(20);
			int ry11 = _random.nextInt(20);

			int rx12 = _random.nextInt(30);
			int ry12 = _random.nextInt(30);

			int ux10 = 32926 + rx10;
			int uy10 = 33250 + ry10;
			int um = 4;

			int ux11 = 32926 + rx11;
			int uy11 = 33250 + ry11;

			int ux12 = 32926 + rx12;
			int uy12 = 33250 + ry12;

			L1SpawnUtil.spawnfieldboss(ux10, uy10, (short) um, 45545, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux11, uy11, (short) um, 7000091, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux12, uy12, (short) um, 7000092, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux12, uy12, (short) um, 7000089, 0, 0, 0);
			break;
		case 3000227:// 소환이벤트막대2
			int rx13 = _random.nextInt(10);
			int ry13 = _random.nextInt(10);

			int rx14 = _random.nextInt(20);
			int ry14 = _random.nextInt(20);

			int rx15 = _random.nextInt(30);
			int ry15 = _random.nextInt(30);

			int ux13 = 32926 + rx13;
			int uy13 = 33250 + ry13;
			int um1 = 4;

			int ux14 = 32926 + rx14;
			int uy14 = 33250 + ry14;

			int ux15 = 32926 + rx15;
			int uy15 = 33250 + ry15;

			L1SpawnUtil.spawnfieldboss(ux13, uy13, (short) um1, 45203, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux14, uy14, (short) um1, 45206, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux15, uy15, (short) um1, 45257, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux13, uy13, (short) um1, 45263, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux14, uy14, (short) um1, 45341, 0, 0, 0);
			break;
		case 3000228:// 소환이벤트막대3
			int rx16 = _random.nextInt(10);
			int ry16 = _random.nextInt(10);

			int rx17 = _random.nextInt(20);
			int ry17 = _random.nextInt(20);

			int rx18 = _random.nextInt(30);
			int ry18 = _random.nextInt(30);

			int ux16 = 32926 + rx16;
			int uy16 = 33250 + ry16;
			int um2 = 4;

			int ux17 = 32926 + rx17;
			int uy17 = 33250 + ry17;

			int ux18 = 32926 + rx18;
			int uy18 = 33250 + ry18;

			L1SpawnUtil.spawnfieldboss(ux16, uy16, (short) um2, 707001, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux17, uy17, (short) um2, 707002, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux18, uy18, (short) um2, 707007, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux16, uy16, (short) um2, 707008, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux17, uy17, (short) um2, 707013, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux18, uy18, (short) um2, 707015, 0, 0, 0);
			L1SpawnUtil.spawnfieldboss(ux16, uy16, (short) um2, 707016, 0, 0, 0);
			break;
		case 3000432:
			petbuy(client, 46043, 41159, 0);//불꽃의 캥거루
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000433:
			petbuy(client, 46045, 41159, 0);//공포의 판다곰 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000434:
			petbuy(client, 46046, 41159, 0);//골드 드래곤 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000435:
			petbuy(client, 45695, 41159, 0);//하이 래빗 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000436:
			petbuy(client, 45693, 41159, 0);//하이 세인트버나드 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000437:
			petbuy(client, 45692, 41159, 0);//하이 비글 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000438:
			petbuy(client, 45694, 41159, 0);//하이 폭스 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000439:
			petbuy(client, 45696, 41159, 0);//하이 캣 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000440:
			petbuy(client, 45697, 41159, 0);//하이 라쿤 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000441:
			petbuy(client, 45712, 41159, 0);//진돗개 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000442:
			petbuy(client, 45710, 41159, 0);//배틀 타이거 목걸이
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000194: { // PC방 출석 상자
			// -- 랜덤 값의 횟수
			// -- 랜덤 값
			int[] itemid = { 40308 };
			int random500 = CommonUtil.random(100);

			int[] itemid2 = { 3000176 };
			int random502 = CommonUtil.random(100);

			if (random500 <= 10) { // -- 5만 에서 1000만.
				createNewItem(pc, itemid[0], CommonUtil.random(10000, 15000));
			} else {// -- 5만 에서 500만.
				createNewItem(pc, itemid[0], CommonUtil.random(15000, 20000));
			}
			if (random502 <= 0.3) { // -- 2% 확률
				createNewItem(pc, itemid2[0], CommonUtil.random(1, 1));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41299: {
			int[] itemid = { 41302 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // 50만에서 60만
				createNewItem(pc, itemid[0], CommonUtil.random(500000, 600000));
			} else {// 30만에서 40만
				createNewItem(pc, itemid[0], CommonUtil.random(300000, 400000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41300: {
			int[] itemid = { 41302 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // 500만에서 600만
				createNewItem(pc, itemid[0], CommonUtil.random(5000000, 6000000));
			} else {// 300만에서 400만
				createNewItem(pc, itemid[0], CommonUtil.random(3000000, 4000000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41303: {
			int[] itemid = { 41302 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // 60만에서 70만
				createNewItem(pc, itemid[0], CommonUtil.random(600000, 700000));
			} else {// 40만에서 50만
				createNewItem(pc, itemid[0], CommonUtil.random(400000, 500000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41304: {
			int[] itemid = { 41302 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 7) { // 600만에서 700만
				createNewItem(pc, itemid[0], CommonUtil.random(6000000, 7000000));
			} else {// 400만에서 500만
				createNewItem(pc, itemid[0], CommonUtil.random(4000000, 5000000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000145: { // 픽시의 깃털 랜덤상자
			int[] itemid = { 41159 };
			int random500 = CommonUtil.random(100);
			
			if (random500 <= 90) { // -- 5만 에서 1000만.
				createNewItem(pc, itemid[0], CommonUtil.random(50, 100));
			} else {// -- 5만 에서 500만.
				createNewItem(pc, itemid[0], CommonUtil.random(100, 200));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000146: { // 오림의 가넷 랜덤상자
			int[] itemid = { 3000246 };
			int random500 = CommonUtil.random(100);
			
			if (random500 <= 90) { // -- 5만 에서 1000만.
				createNewItem(pc, itemid[0], CommonUtil.random(30, 100));
			} else {// -- 5만 에서 500만.
				createNewItem(pc, itemid[0], CommonUtil.random(100, 200));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000195:
//			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "pbook0"));
			for (L1Object obj : L1World.getInstance().getObject()) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcId() == 4500015) {
						pc.sendPackets(new S_Board(npc));
						break;
					}
				}
			}
			break;
		case 4100203:// 슬롯 전체 개방권
			pc.getQuest().set_end(L1Quest.QUEST_SLOT59);
			pc.sendPackets(new S_ReturnedStat(67, 1, 16));
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			
			pc.getQuest().set_end(L1Quest.QUEST_SLOT76);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 1));
			
			pc.getQuest().set_end(L1Quest.QUEST_SLOT81);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 2));
			
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_SHOULD);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_SHOULD));
			
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_BADGE));
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_BADGE);
			
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12358));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100140:// 귀걸이 확장권:59레벨
			pc.getQuest().set_end(L1Quest.QUEST_SLOT59);
			pc.sendPackets(new S_ReturnedStat(67, 1, 16));
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100141:// 반지 확장권:76레벨
			pc.getQuest().set_end(L1Quest.QUEST_SLOT76);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 1));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100142:// 반지 확장권:81레벨
			pc.getQuest().set_end(L1Quest.QUEST_SLOT81);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 2));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100143:// 견갑 확장권:83레벨
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_SHOULD);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_SHOULD));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12358));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100144:// 휘장 확장권:70레벨
			pc.getQuest().set_end(L1Quest.QUEST_SLOT_BADGE);
			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_BADGE));
			pc.sendPackets(new S_SkillSound(pc.getId(), 12360));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40067:// 쑥송편
		case 41414:// 복월병
			pc.setCurrentMp(pc.getCurrentMp() + (15 + _random.nextInt(30)));
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));// 이펙트발생
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40066:// 송편
		case 41413:// 월병
			pc.setCurrentMp(pc.getCurrentMp() + (5 + _random.nextInt(10)));
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));// 이펙트발생
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 714:
			if (pc.getLevel() < 80) {
				pc.sendPackets("레벨 80 달성하셔야 사용이 가능합니다.");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 715:
			if (pc.getLevel() < 82) {
				pc.sendPackets("레벨 82 달성하셔야 사용이 가능합니다.");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 716:
			if (pc.getLevel() < 84) {
				pc.sendPackets("레벨 84 달성하셔야 사용이 가능합니다.");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 717:
			if (pc.getLevel() < 86) {
				pc.sendPackets("레벨 86 달성하셔야 사용이 가능합니다.");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 721:
			if (pc.getLevel() < 88) {
				pc.sendPackets("레벨 88 달성하셔야 사용이 가능합니다.");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 722:
			if (pc.getLevel() < 90) {
				pc.sendPackets("레벨 90 달성하셔야 사용이 가능합니다.");
				return;
			}
			additem2.clickItem(pc, itemId, l1iteminstance);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			break;
		case 500220:
			long timeItem = System.currentTimeMillis();
			int useTimeItem = 600;// 10분뒤
			if(pc.hasSkillEffect(L1SkillId.WITCH_MANA_POTION)){
				int n = pc.getSkillEffectTimeSec(L1SkillId.WITCH_MANA_POTION);
				pc.sendPackets(new S_SystemMessage(String.format("%d초 후에 사용할 수 있습니다.", n)));
				return;
			}
			pc.setSkillEffect(L1SkillId.WITCH_MANA_POTION, useTimeItem * 1000);
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + (1000 + _random.nextInt(1))); // 15~30
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));// 이펙트발생
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 410002:// 빛나는 나뭇잎
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + 44);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40735:// 용기의 코인
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + 60);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40042:// 정신력의 물약
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + 50);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 41404:// 쿠작의 영약
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + (80 + _random.nextInt(21))); // 80~100
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000211:
			pc.sendPackets(new S_SystemMessage( "제작 수량을 모은후 기란마을 에서 제작 하세요."));
			break;
		case 41412:// 금쫑즈
			pc.sendPackets(new S_ServerMessage(338, "$1084")); // 당신의%0가 회복해 갈
			pc.setCurrentMp(pc.getCurrentMp() + (5 + _random.nextInt(16))); // 5~20
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 810011:
			pc.sendPackets(new S_ChatPacket(pc, "몽환의 섬 입구 크루나에게 물건을 구입할 수 있다."));
			break;
		case 3000048: // 꼬마 요정의 마음
			pc.sendPackets(new S_ChatPacket(pc, "생일 축하 요정에게 '코마'를 한 번 받을 수 있다."));
			break;
		case 40493:// 마법의 플룻
			pc.sendPackets(new S_Sound(165));
			pc.broadcastPacket(new S_Sound(165));
			L1GuardianInstance guardian = null;
			for (L1Object visible : pc.getKnownObjects()) {
				if (visible instanceof L1GuardianInstance) {
					guardian = (L1GuardianInstance) visible;
					if (guardian.getNpcTemplate().get_npcId() == 70850) { // 빵
						if (createNewItem(pc, 88, 1)) {
							pc.getInventory().removeItem(l1iteminstance, 1);
						}
					}
				}
			}
			break;
		case 40325:// 2단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3237 + _random.nextInt(2);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 40326:// 3단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3229 + _random.nextInt(3);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 40327:// 4단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3241 + _random.nextInt(4);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도
				// 일어나지
				// 않았습니다.
			}
			break;
		case 40328:// 6단계 마법주사위
			if (pc.getInventory().checkItem(40318, 1)) {
				int gfxid = 3204 + _random.nextInt(6);
				pc.sendPackets(new S_SkillSound(pc.getId(), gfxid));
				pc.broadcastPacket(new S_SkillSound(pc.getId(), gfxid));
				pc.getInventory().consumeItem(40318, 1);
			} else {
				// \f1 아무것도 일어나지 않았습니다.
				pc.sendPackets(new S_ServerMessage(79));
			}
			break;
		case 3000150:// 생명의 마안 (제작)
			pc.sendPackets(new S_UserCommands6(1));
			if (!pc.getInventory().checkItem(3000150, 100)) {
				pc.sendPackets(new S_SystemMessage( "생명의마안 조각 이 부족합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(3000150, 100);
				pc.getInventory().storeItem(410038, 1);
				pc.sendPackets(new S_ChatPacket(pc, "생명의마안 이 제작되었습니다."));
			}
			break;
		case 41027:// 완성한 라스타바드의 역사서
			pc.sendPackets(new S_UserCommands7(1));
			if (!pc.getInventory().checkItem(41027, 1)) {
				pc.sendPackets(new S_SystemMessage( "완성한 라스타바드의 역사서 가 부족합니다."));
				return;
			} else {
				pc.getInventory().consumeItem(41027, 1);
				pc.getInventory().storeItem(40965, 1);
				pc.sendPackets(new S_SystemMessage( "라스타바드 무기제작 비법서 제작 되었습니다."));
			}
			break;
		case 490028: // 라우풀물약
			if (pc.getLawful() >= 32767) {
				pc.sendPackets("현재 사용할수있는 조건이 아닙니다.");
				return;
			}
			if (pc.getLawful() >= -32768 && pc.getLawful() <= 32767) {
				pc.addLawful(10000);
				pc.sendPackets(new S_ServerMessage(674));
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.save();
			} else {
				pc.sendPackets("카오틱 성향에서만 사용하실 수 있습니다.");
			}
			break;
		case 490029:// 카오틱물약
			if (pc.getLawful() <= -32768) {
				pc.sendPackets("현재 사용할수있는 조건이 아닙니다.");
				return;
			}
			if (pc.getLawful() >= -32768 && pc.getLawful() <= 32767) {
				pc.addLawful(-10000);
				pc.sendPackets(new S_ServerMessage(674));
				pc.getInventory().removeItem(l1iteminstance, 1);
				pc.save();
			} else {
				pc.sendPackets("라우풀 성향에서만 사용하실 수 있습니다.");
			}
			break;
		case 3000165: // 지룡 용의티셔츠 상자
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getInventory().storeItem(3000160, 3);
			pc.getInventory().storeItem(3000161, 2);
			pc.getInventory().storeItem(3000162, 1);
			pc.getInventory().storeItem(900025, 1);
			pc.sendPackets(new S_SystemMessage( "용의티셔츠 세트 를 얻었습니다."));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000166: // 화룡 용의티셔츠 상자
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getInventory().storeItem(3000160, 3);
			pc.getInventory().storeItem(3000161, 2);
			pc.getInventory().storeItem(3000162, 1);
			pc.getInventory().storeItem(900026, 1);
			pc.sendPackets(new S_SystemMessage( "용의티셔츠 세트 를 얻었습니다."));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000167: // 풍룡 용의티셔츠 상자
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getInventory().storeItem(3000160, 3);
			pc.getInventory().storeItem(3000161, 2);
			pc.getInventory().storeItem(3000162, 1);
			pc.getInventory().storeItem(900027, 1);
			pc.sendPackets(new S_SystemMessage( "용의티셔츠 세트 를 얻었습니다."));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000168: // 수룡 용의티셔츠 상자
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getInventory().storeItem(3000160, 3);
			pc.getInventory().storeItem(3000161, 2);
			pc.getInventory().storeItem(3000162, 1);
			pc.getInventory().storeItem(900028, 1);
			pc.sendPackets(new S_SystemMessage( "용의티셔츠 세트 를 얻었습니다."));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000175: // 탐상자
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, pc.getNetConnection()), true);// 탐지급
			pc.getNetConnection().getAccount().tam_point += 10000;// 탐지급갯수
			pc.getNetConnection().getAccount().updateTam();// 탐업뎃
			pc.sendPackets(new S_SystemMessage( "탐 1만개를 얻어습니다."));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 45450:
//		case 45451:
//		case 45452:
//		case 45453:
//			pc.sendPackets(new S_SkillSound(pc.getId(), 21129));//21178 편지소리
			UserCommands.getInstance().execute(pc, "자동물약자동버프");
			break;
		case 4100056:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets("소지하고 있는 아이템이 너무 많습니다.");
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets("소지품이 너무 무거워서 사용 할 수 없습니다.");
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 5000;
			pc.getNetConnection().getAccount().updateNcoin();
//			pc.sendPackets("계정의 마일리지 5,000원이 적립 되었습니다. 명령어(.마일리지)");
			pc.sendPackets("계정에 N코인 5,000원이 적립되었습니다 [.엔코인]");
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100057:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets("소지하고 있는 아이템이 너무 많습니다.");
				return;
			}
			/*if (pc.getLevel() < 80) {
				pc.sendPackets("레벨 80이상만 사용 가능합니다.");
				return;
			}*/
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets("소지품이 너무 무거워서 사용 할 수 없습니다.");
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 15000;
			pc.getNetConnection().getAccount().updateNcoin();
//			pc.sendPackets("계정의 마일리지 15,000원이 적립 되었습니다. 명령어(.마일리지)");
			pc.sendPackets("계정에 N코인 15,000원이 적립되었습니다 [.엔코인]");
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100058:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets("소지하고 있는 아이템이 너무 많습니다.");
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets("소지품이 너무 무거워서 사용 할 수 없습니다.");
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 30000;
			pc.getNetConnection().getAccount().updateNcoin();
//			pc.sendPackets("계정의 마일리지 30,000원이 적립 되었습니다. 명령어(.마일리지)");
			pc.sendPackets("계정에 N코인 30,000원이 적립되었습니다 [.엔코인]");
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000177: // 엔코인 100원
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 100;// 탐지급갯수
			pc.getNetConnection().getAccount().updateNcoin();// 엔코인업뎃
//			pc.sendPackets(new S_SystemMessage("N코인(100원) 충전완료 \\aH(엔피씨 : 수상한 마을  확인)"));
			pc.sendPackets(new S_SystemMessage("N코인(100원) 충전 완료"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000178: // 엔코인 300원
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 300;// 탐지급갯수
			pc.getNetConnection().getAccount().updateNcoin();// 엔코인업뎃
//			pc.sendPackets(new S_SystemMessage( "N코인(300원) 충전완료 \\aH(엔피씨 : 수상한 마을  확인)"));
			pc.sendPackets(new S_SystemMessage( "N코인(300원) 충전 완료"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000179: // 엔코인 500원
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 500;// 탐지급갯수
			pc.getNetConnection().getAccount().updateNcoin();// 엔코인업뎃
//			pc.sendPackets(new S_SystemMessage( "N코인(500원) 충전완료 \\aH(엔피씨 : 수상한 마을  확인)"));
			pc.sendPackets(new S_SystemMessage( "N코인(500원) 충전 완료"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000187: // 엔코인 1000원
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 1000;// 탐지급갯수
			pc.getNetConnection().getAccount().updateNcoin();// 엔코인업뎃
//			pc.sendPackets(new S_SystemMessage("N코인(1,000원) 충전완료 \\aH(엔피씨 : 수상한 마을  확인)"));
			pc.sendPackets(new S_SystemMessage("N코인(1,000원) 충전 완료"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000188: // 엔코인 100000원
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 100000;// 탐지급갯수
			pc.getNetConnection().getAccount().updateNcoin();// 엔코인업뎃
//			pc.sendPackets(new S_SystemMessage( "N코인(100,000원) 충전완료 \\aH(엔피씨 : 수상한 마을  확인)"));
			pc.sendPackets(new S_SystemMessage( "N코인(100,000원) 충전 완료"));
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000249:
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage( "소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage( "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			pc.getNetConnection().getAccount().Ncoin_point += 1000;
			pc.getNetConnection().getAccount().updateNcoin();// 엔코인업뎃
//			pc.sendPackets("계정의 마일리지 1,000원이 적립 되었습니다. 명령어(.마일리지)");
			pc.sendPackets("계정에 N코인 1,000원이 적립되었습니다 [.엔코인]");
			pc.sendPackets(new S_SkillSound(pc.getId(), 10964));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000173:// 전체 9~10검 랜덤 상자
			if (pc.getInventory().checkItem(3000173, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000173, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 4, 54, 58, 62, 203018, 100189, 100164, 1115, 1116, 1117, 1118, 1119, 1120, 1108, 1109,
						1110, 1111, 1112, 1113, 1114, 42, 41, 202002, 66, 203019, 203020, 203021, 203022, 205, 1136 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
						9, 9, 9, 9, 9, 9, 9, 10 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("아이템을 [획득] 하였습니다."));
			break;
		case 3000389:// 무기 랜덤 상자(나발급~잊섬급)
			if (pc.getInventory().checkItem(3000389, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000389, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 54,58,76,203006,59,202003,203018,203017,1120,
										54,58,76,203006,59,202003,203018,203017,1120,
										54,58,76,203006,59,202003,203018,203017,1120,
										54,58,76,203006,59,202003,203018,203017,1120,
										12,61,86,134,202011,202012,202013,202014};// 랜덤아이템
				int[] enchantrnd = { 0 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
			}
			pc.sendPackets(new S_SystemMessage("아이템을 획득 하였습니다."));
			break;
		case 3000378:// 무기 랜덤 상자(무양급)
			if (pc.getInventory().checkItem(3000378, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000378, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 203005,62,127,189,164,84,1123,1119,1135 };// 랜덤아이템
				int[] enchantrnd = { 0 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("아이템을 획득 하였습니다."));
			break;
		case 3000379:// 무기 랜덤 상자(나발급)
			if (pc.getInventory().checkItem(3000379, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000379, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 203006,59,202003,1136,203018,203017,1120 };// 랜덤아이템
				int[] enchantrnd = { 0 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("아이템을 획득 하였습니다."));
			break;
		case 3000381:// 격노상자
			if (pc.getInventory().checkItem(3000381, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000381, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 202011 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 가이아의 격노 획득"));
			break;
		case 3000382:// 절망상자
			if (pc.getInventory().checkItem(3000382, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000382, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 202012 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 히페리온의 절망 획득"));
			break;
		case 3000383:// 공포상자
			if (pc.getInventory().checkItem(3000383, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000383, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 202013 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 크로노스의 공포 획득"));
			break;
		case 3000384:// 분노상자
			if (pc.getInventory().checkItem(3000384, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000384, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 202014 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 타이탄의 분노 획득"));
			break;
		case 3000385:// 집행상자
			if (pc.getInventory().checkItem(3000385, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000385, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 61 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 진명황의 집행검 획득"));
			break;
		case 3000386:// 붉이상자
			if (pc.getInventory().checkItem(3000386, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000386, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 86 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 붉은 그림자의 이도류 획득"));
			break;
		case 3000387:// 수결지상자
			if (pc.getInventory().checkItem(3000387, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000387, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 134 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 수정결정체 지팡이 획득"));
			break;
		case 3000388:// 바칼상자
			if (pc.getInventory().checkItem(3000388, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000388, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 12 };// 랜덤아이템
				int[] enchantrnd = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
											2,2,2,2,2,2,2,2,2,2,2,
											3}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 바람칼날의 단검 획득"));
			break;
		case 3000368:// 커츠 랜덤 상자
			if (pc.getInventory().checkItem(3000368, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000368, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 54 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
											8,8,8,8,8,8,8,8,8,8,8,
											9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 커츠의 검 획득"));
			break;
		case 3000369:// 데불 랜덤 상자
			if (pc.getInventory().checkItem(3000369, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000369, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 58 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 데스나이트의 불검 획득"));
			break;
		case 3000370:// 론드 랜덤 상자
			if (pc.getInventory().checkItem(3000370, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000370, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 76 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 론드의 이도류 획득"));
			break;
		case 3000371:// 태도 랜덤 상자
			if (pc.getInventory().checkItem(3000371, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000371, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 203006 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 태풍의 도끼 획득"));
			break;
		case 3000372:// 나발 랜덤 상자
			if (pc.getInventory().checkItem(3000372, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000372, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 59 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("획득: 나이트발드의 양손검 획득"));
			break;
		case 3000373:// 제지 랜덤 상자
			if (pc.getInventory().checkItem(3000373, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000373, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 202003 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
			}
			pc.sendPackets(new S_SystemMessage("획득: 제로스의 지팡이 획득"));
			break;
		case 3000374:// 악장 랜덤 상자
			if (pc.getInventory().checkItem(3000374, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000374, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 1136 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
			}
			pc.sendPackets(new S_SystemMessage("획득: 악몽의 장궁 획득"));
			break;
		case 3000375:// 포효 랜덤 상자
			if (pc.getInventory().checkItem(3000375, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000375, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 203018 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
			}
			pc.sendPackets(new S_SystemMessage("획득: 포효의 이도류 획득"));
			break;
		case 3000376:// 섬체 랜덤 상자
			if (pc.getInventory().checkItem(3000376, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000376, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 203017 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
			}
			pc.sendPackets(new S_SystemMessage("획득: 섬멸자의 체인소드 획득"));
			break;
		case 3000377:// 냉키 랜덤 상자
			if (pc.getInventory().checkItem(3000377, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000377, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 1120 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
						,8,8,8,8,8,8,8,8,8,8,8,9}; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
			}
			pc.sendPackets(new S_SystemMessage("획득: 냉한의 키링크 획득"));
			break;
		case 3000038:// 고대물품:투구
			if (pc.getInventory().checkItem(3000038, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000038, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011,
						20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011,
						20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20011, 20017 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			break;
			
		case 4100120: { // 타버린 불멸의 가호, 본 서버: 더블 클릭 시 10만 ~ 1000만 랜덤 획득하거나 아덴 상단이 25만에 매입한다.
			int[] itemid = { 40308 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 90) { // 90%는 1천 ~ 1만 랜덤 획득
				createNewItem(pc, itemid[0], CommonUtil.random(1000, 10000));
			} else { // 나머지 확률은 1.1만 ~ 5만
				createNewItem(pc, itemid[0], CommonUtil.random(10001, 50000));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
			
		case 4100119: { // 타버린 고급 불멸의 가호, 본 서버: 더블 클릭 시 80만 ~ 1000만 랜덤 획득하거나 아덴 상단이 120만에 매입한다.
			int[] itemid = { 40308 };
			int random500 = CommonUtil.random(100);

			if (random500 <= 90) { // 90%는 100만 ~ 700만 랜덤 획득
				createNewItem(pc, itemid[0], CommonUtil.random(1000000, 7000000));
			} else { // 나머지 확률은 701만 ~ 999만
				createNewItem(pc, itemid[0], CommonUtil.random(7000001, 9999999));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
			
		case 3000039:// 고대물품:망토
			if (pc.getInventory().checkItem(3000039, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000039, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056,
						20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056,
						20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20056, 20074, 20079 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("아이템을 [획득] 하였습니다."));
			break;
		case 3000196: { // 홍보상자
			// -- 랜덤 값의 횟수
			// -- 랜덤 값
			int[] itemid = { 40308 };
			int random500 = CommonUtil.random(100);

			int[] itemid1 = { 3000197 };
			int random501 = CommonUtil.random(100);

			int[] itemid2 = { 3000198 };
			int random502 = CommonUtil.random(100);

			int[] itemid3 = { 3000199 };
			int random503 = CommonUtil.random(100);

			if (random500 <= 30) { // -- 5만 에서 1000만.
				createNewItem(pc, itemid[0], CommonUtil.random(10000, 200000));
			} else {// -- 5만 에서 500만.
				createNewItem(pc, itemid[0], CommonUtil.random(50000, 300000));
			}
			if (random501 <= 0.3) { // -- 5% 확률
				createNewItem(pc, itemid1[0], CommonUtil.random(1, 1));
			}
			if (random502 <= 0.5) { // -- 2% 확률
				createNewItem(pc, itemid2[0], CommonUtil.random(1, 1));
			}
			if (random503 <= 0.8) { // -- 2% 확률
				createNewItem(pc, itemid3[0], CommonUtil.random(1, 1));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000174:// 전체 9방어구 랜덤 상자
			if (pc.getInventory().checkItem(3000174, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000174, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 22365, 222324, 22360, 20085, 20085, 20085, 20085, 20084, 20084, 20084, 20084, 20084,
						20084, 20084, 22196, 22197, 22198, 22199, 22200, 22201, 22202, 22203, 22204, 22205, 22206,
						22207, 20049, 20050, 20076, 20079, 20178, 22261, 222327, 20235, 22214, 22263, 20200, 20216,
						22213, 222307, 222308, 222309, 22257, 22258, 22259 };// 랜덤아이템
				// 번호
				int[] enchantrnd = { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
						8, 8, 8, 8, 8, 8, 8, 8, 8, 9 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				// item.setIdentified(false);
			}
			pc.sendPackets(new S_SystemMessage("아이템을 [획득] 하였습니다."));
			break;
		case 3000169:// 랜덤무기 인챈 상자
			if (pc.getInventory().checkItem(3000169, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(3000169, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 66, 12, 134, 86, 61, 202011, 202012, 202013, 202014 };// 랜덤아이템 번호
				int[] enchantrnd = { 
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						3, 3 };// 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				pc.sendPackets(new S_SkillSound(pc.getId(), 11308));
			}
			break;
	/*	case 4100004:// 랜덤무기 인챈 상자
			if (pc.getInventory().checkItem(4100004, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(4100004, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 12, 134, 86, 61, 202011, 202012, 202013, 202014 };// 랜덤아이템 번호
				int[] enchantrnd = { 
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						3, 3, 3, 3, 3, 3, 3, 3, 3,
						4, 4, 4, 4 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				pc.sendPackets(new S_SkillSound(pc.getId(), 11308));
			}
			break;*/
		case 718:// 랜덤무기 인챈 상자
			if (pc.getInventory().checkItem(718, 1)) { // 체크 되는 아이템과 수량
				pc.getInventory().consumeItem(718, 1); // 삭제되는 아이템과 수량
				Random random = new Random();
				L1ItemInstance item = null;
				int[] itemrnd = { 315, 316, 317, 318, 319, 320, 1104, 7000136 ,7000213 };// 랜덤아이템 번호
				int[] enchantrnd = { 
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
						3, 3, 3, 3 }; // 랜덤인챈수치
				int ran1 = random.nextInt(itemrnd.length);
				int ran2 = random.nextInt(enchantrnd.length);
				item = pc.getInventory().storeItem(itemrnd[ran1], 1);
				item.setEnchantLevel(enchantrnd[ran2]);
				pc.sendPackets(new S_SkillSound(pc.getId(), 11308));
				// item.setIdentified(false);
			}
//			pc.sendPackets(new S_SystemMessage("시스템: 아이템을 [획득] 하였습니다."));
			break;
			
		case 698: {	// 랜덤 변경권: 숨결
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			if (!(id == 40346 || id == 40370 || id == 40362 || id == 40354)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 40346, 40370, 40362, 40354 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage( new_item.getLogName() + "으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
			
		case 776: { // 랜덤 변경권: 4단계 인형
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			/* 3000351	-> 머미로드
			 * 743		-> 나이트발드
			 * 744		-> 시어
			 * 447016	-> 리치
			 * 3000086	-> 아이리스
			 * 3000087	-> 뱀파이어
			 * 510220	-> 사이클롭스
			 * */
			if (!(id == 3000351 || id == 743 || id == 744 || id == 447016 || id == 3000086 || id == 3000087 || id == 510220)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 3000351, 743, 744, 447016, 3000086, 3000087, 510220 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
			
		case 777: { // 랜덤 변경권: 5단계 인형
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			// 3000088	-> 마법인형: 바란카
			// 3000352	-> 마법인형: 타락
			// 755		-> 마법인형: 바포메트
			// 756		-> 마법인형: 얼음여왕
			// 757		-> 마법인형: 커츠
			// 745		-> 마법인형: 데몬
			// 746		-> 마법인형: 데스나이트
			if (!(id == 3000088 || id == 3000352 || id == 755 || id == 756 || id == 757 || id == 745 || id == 746)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 3000088, 3000352, 755, 756, 757, 745, 746 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
			
		case 779: { // 랜덤 변경권: 5단계 축인형
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			// 950013	-> 축복받은 마법인형: 데몬
			// 950014	-> 축복받은 마법인형: 데스나이트
			// 950015	-> 축복받은 마법인형: 바란카
			// 950016	-> 축복받은 마법인형: 타락
			// 950017	-> 축복받은 마법인형: 바포메트
			// 950018	-> 축복받은 마법인형: 얼음여왕
			// 950019	-> 축복받은 마법인형: 커츠
			if (!(id == 950013 || id == 950014 || id == 950015 || id == 950016 || id == 950017 || id == 950018 || id == 950019)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 950013, 950014, 950015, 950016, 950017, 950018, 950019 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
			
		case 778: { // 랜덤 변경권: 5단계 용인형
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			// 4100007	-> 안타라스
			// 4100008	-> 파푸리온
			// 4100009	-> 린드비오르
			// 4100010	-> 발라카스
			if (!(id == 4100007 || id == 4100008 || id == 4100009 || id == 4100010 )) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 4100007, 4100008, 4100009, 4100010 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;
			
		case 780: { // 랜덤 변경권: 6단계 인형
			L1ItemInstance enchant_item = pc.getInventory().getItem(l1iteminstance.getId());
			L1ItemInstance weapon_item = pc.getInventory().getItem(l);
			if (enchant_item == null || weapon_item == null) {
				return;
			}
			if (l1iteminstance1.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int id = weapon_item.getItemId();
			// 141400	-> 할파스
			// 141401	-> 아우라키아
			// 141402	-> 베히모스
			// 141403	-> 기르타스
			// 141404	-> 알비노 데몬
			// 141405	-> 알비노 피닉스
			// 141406	-> 알비노 유니콘
			// 141407	-> 유니콘
			// 141408	-> 그림 리퍼
			// 141409	-> 다크스타 조우
			// 141410	-> 디바인 크루세이더
			// 141411	-> 군터
			// 141412	-> 오우거 킹
			// 141413	-> 다크 하딘
			// 141415	-> 드래곤 슬레이어
			// 141416	-> 피닉스
			// 141417	-> 암흑 대장로

			if (!(id == 141400 || id == 141401 || id == 141402
			   || id == 141403 || id == 141404 || id == 141405
			   || id == 141406 || id == 141407 || id == 141408
			   || id == 141409 || id == 141410 || id == 141411
			   || id == 141412 || id == 141413 || id == 141415
			   || id == 141416 || id == 141417)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			int enchant = weapon_item.getEnchantLevel();
			int[] random_item = { 141400, 141401, 141402, 141403, 141404,
					              141405, 141406, 141407, 141408, 141409,
					              141410, 141411, 141412, 141413, 141415, 141416, 141417 };
			int random = CommonUtil.random(random_item.length);
			L1ItemInstance new_item = pc.getInventory().storeItem(random_item[random], 1, enchant);
			pc.sendPackets(new S_SystemMessage(new_item.getLogName() + " 으로 변경되었습니다."));
			pc.getInventory().removeItem(enchant_item, 1);
			pc.getInventory().removeItem(weapon_item);
		}
			break;	
			
		case 3000458: {// 클라우디아 패스 이용권
			if (pc.getLevel() >= 1 && pc.getLevel() >= 56) {
				pc.sendPackets(new S_SystemMessage( "레벨 55이하만 사용이 가능합니다."));
				return;
			}
				pc.addExp((ExpTable.getExpByLevel(Config.경험치지급단) - 1) - pc.getExp()
						+ ((ExpTable.getExpByLevel(Config.경험치지급단) - 1) / 30000000));
				pc.sendPackets(new S_SystemMessage( "클라우디아 패스 이용권을 사용 하셨습니다."));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 41316:// 신성한 미스릴 가루
			if (pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			if (pc.hasSkillEffect(STATUS_HOLY_WATER)) {
				pc.removeSkillEffect(STATUS_HOLY_WATER);
			}
			pc.setSkillEffect(STATUS_HOLY_MITHRIL_POWDER, 900 * 1000);
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
			pc.sendPackets(new S_ServerMessage(1142));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 41354:// 신성한 에바의 물
			if (pc.hasSkillEffect(STATUS_HOLY_WATER) || pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			pc.setSkillEffect(STATUS_HOLY_WATER_OF_EVA, 900 * 1000);
			pc.sendPackets(new S_SkillSound(pc.getId(), 190));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 190));
			pc.sendPackets(new S_ServerMessage(1140));
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 3000147:// 테베대항전
			loc_x += 32769;
			loc_y += 32832;
			if (itemId == 3000147) {
				pc.start_teleport(loc_x, loc_y, 782, pc.getHeading(), 169, true, false);
				pc.sendPackets(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG[주의] 강력한 힘을 가지고 있는 몬스터가 있습니다."));
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case 202099: {// 클라우디아 마을
			// if (pc.get_DuelLine() != 0) {
			// pc.sendPackets(new S_SystemMessage("배틀존지역에서 사용할 수 없습니다."));
			// return;
			// }
			// if (pc.getMap().isEscapable() || pc.isGm()) {
			// int[]
			loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_claudia);
			// L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2],
			// pc.getHeading(), true);
			pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, false);
			// }
		}
			break;
		case 3000229: {// 잊혀진 귀환 주문서
			if (pc.hasSkillEffect(L1SkillId.DESPERADO))
				return;
			if (!(pc.getMapId() >= 1708 && pc.getMapId() <= 1709)) {
				pc.sendPackets(new S_SystemMessage( "잊혀진 섬/대기실 에서만 사용 가능합니다."));
				return;
			}
			int[] x = { 32775, 32787, 32788, 32773 };
			int[] y = { 32757, 32755, 32765, 32769 };
			int type = _random.nextInt(x.length);
			L1Location base = new L1Location(x[type], y[type], 1710);
			L1Location random = L1Location.randomLocation(base, 0, 3, true);
			pc.start_teleport(random.getX(), random.getY(), random.getMapId(), pc.getHeading(), 169, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000413: {// 강제 귀환 주문서 (잊섬->기란)
			if (!(pc.getMapId() >= 1710)) {
				pc.sendPackets(new S_SystemMessage( "잊혀진 섬 대기실 에서만 사용 가능합니다."));
				return;
			}
			int[] x = { 33443, 33444, 33437, 33428 };
			int[] y = { 32804, 32810, 32812, 32808 };
			int type = _random.nextInt(x.length);
			L1Location base = new L1Location(x[type], y[type], 4);
			L1Location random = L1Location.randomLocation(base, 0, 3, true);
			pc.start_teleport(random.getX(), random.getY(), random.getMapId(), pc.getHeading(), 169, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 41260:// 장작
			for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
				if (object instanceof L1EffectInstance) {
					if (((L1NpcInstance) object).getNpcTemplate().get_npcId() == 81170) {
						pc.sendPackets(new S_ServerMessage(1162));
						return;
					}
				}
			}
			int[] loc1 = new int[2];
			loc1 = pc.getFrontLoc();
			L1EffectSpawn.getInstance().spawnEffect(81170, 300000, loc1[0], loc1[1], pc.getMapId());
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 240100:// 저주해진 텔레포트 스크롤(오리지날 아이템)
			pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.cancelAbsoluteBarrier(); // 아브소르트바리아의 해제
			break;
		case 748: // 잡화상점 이동부적
			pc.start_teleport(33453, 32820, 4, pc.getHeading(), 169, true, false);
			break;
		case 731: {
			if (pc.hasSkillEffect(L1SkillId.TOGO_BUFF)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			pc.sendPackets(S_InventoryIcon.icoReset(L1SkillId.TOGO_BUFF, 4992, 1800L, true));
			pc.sendPackets(new S_SkillSound(pc.getId(), 7470));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7470));
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000397: {// 야히군사 버프주문서
			if (pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			pc.setSkillEffect(STATUS_CURSE_BARLOG, 1020 * 1000); // 1020
			pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 1, 1020));
			pc.sendPackets(new S_SkillSound(pc.getId(), 750));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
			pc.sendPackets(new S_ServerMessage(1127));
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 3000398: {// 발록군사 버프주문서
			if (pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
				pc.sendPackets(new S_ServerMessage(79));
				return;
			}
			pc.setSkillEffect(STATUS_CURSE_YAHEE, 1020 * 1000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 2, 1020));
			pc.sendPackets(new S_SkillSound(pc.getId(), 750));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));
			pc.sendPackets(new S_ServerMessage(1127));
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 749: // 장비상점 이동부적
			// L1Teleport.teleport(pc, 33435, 32754, (short) 4, pc.getHeading(),
			// true);
			pc.start_teleport(33435, 32754, 4, pc.getHeading(), 169, true, false);
			break;
		case 500076: {// 근거리버프물약
			int[] allBuffSkill = { 14, 26, 42, 48, 158, 160, 206 };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.sendPackets(new S_SkillSound(pc.getId(), 830));
			pc.curePoison();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 500077: {// 원거리버프물약
			int[] allBuffSkill = { 14, 26, 42, 48, 149, 160, 206 };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.sendPackets(new S_SkillSound(pc.getId(), 830));
			pc.curePoison();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
			
		case 3000128: {// 메티스의 축복주문서
			int[] allBuffSkill = { 22000 };
			L1SkillUse l1skilluse = new L1SkillUse();
			if (pc.hasSkillEffect(L1SkillId.메티스축복주문서))
				pc.removeSkillEffect(L1SkillId.메티스축복주문서);
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 4100040: {// 통합버프
			int[] allBuffSkill = { 14, 26, 42, 43, 48, 158, 160, 206 };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.sendPackets(new S_SkillSound(pc.getId(), 830));
			pc.curePoison();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 500708: {// 꿀 송편(어드밴스 스피릿)
			int[] allBuffSkill = { 79 };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.curePoison();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 500709: {// 꿀 송편(아이언 스킨)
			int[] allBuffSkill = { 168 };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.curePoison();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 500710: {// 꿀 송편(힘, 민첩)
			int[] allBuffSkill = { 26, 42 };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.curePoison();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 500711: {// 꿀 송편(드래곤 스킨)
			int[] allBuffSkill = { 181 };
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.curePoison();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 500724: { // 무한 버프 근거리
			int[] allBuffSkill = {	L1SkillId.DECREASE_WEIGHT,		// 디크리즈 웨이트
									L1SkillId.REMOVE_CURSE,			// 리무브 커스
									L1SkillId.BLESSED_ARMOR,		// 블레스드 아머
									L1SkillId.PHYSICAL_ENCHANT_DEX,	// 덱스업
									L1SkillId.PHYSICAL_ENCHANT_STR,	// 힘업
									L1SkillId.BLESS_WEAPON,			// 블레스 웨폰
									L1SkillId.NATURES_TOUCH,		// 네이처스 터치
									L1SkillId.AQUA_PROTECTER,		// 아쿠아 프로텍트
									L1SkillId.REDUCTION_ARMOR,		// 리덕션 아머
									L1SkillId.BURNING_WEAPON		// 버닝 웨폰
									};
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}		
			pc.setBuffnoch(0);
		}
		break;
		
		case 500725: { // 무한 버프 원거리
			int[] allBuffSkill = {	L1SkillId.DECREASE_WEIGHT,		// 디크리즈 웨이트
									L1SkillId.REMOVE_CURSE,			// 리무브 커스
									L1SkillId.BLESSED_ARMOR,		// 블레스드 아머
									L1SkillId.PHYSICAL_ENCHANT_DEX,	// 덱스업
									L1SkillId.PHYSICAL_ENCHANT_STR,	// 힘업
									L1SkillId.BLESS_WEAPON,			// 블레스 웨폰
									L1SkillId.NATURES_TOUCH,		// 네이처스 터치
									L1SkillId.AQUA_SHOT,			// 아쿠아 샷
									L1SkillId.EAGGLE_EYE,			// 이글 아이
									L1SkillId.STORM_SHOT			// 스톰 샷
									};
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}	
			pc.setBuffnoch(0);
		}
		break;
		
		case 500726: { // 무한 버프 마법 강화
			int[] allBuffSkill = {	L1SkillId.DECREASE_WEIGHT,		// 디크리즈 웨이트
									L1SkillId.REMOVE_CURSE,			// 리무브 커스
									L1SkillId.BLESSED_ARMOR,		// 블레스드 아머
									L1SkillId.PHYSICAL_ENCHANT_DEX,	// 덱스업
									L1SkillId.PHYSICAL_ENCHANT_STR,	// 힘업
									L1SkillId.BLESS_WEAPON,			// 블레스 웨폰
									L1SkillId.NATURES_TOUCH,		// 네이처스 터치
									L1SkillId.CONCENTRATION,		// 컨센트레이션
									L1SkillId.INSIGHT,				// 인사이트
									L1SkillId.PATIENCE				// 페이션스
									};
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}		
			pc.setBuffnoch(0);
		}
			break;
			
		case 500732: { // 고급 근거리 버프: 무한
			int[] allBuffSkill = {	L1SkillId.DECREASE_WEIGHT,		// 디크리즈 웨이트
									L1SkillId.REMOVE_CURSE,			// 리무브 커스
									L1SkillId.BLESSED_ARMOR,		// 블레스드 아머
									L1SkillId.PHYSICAL_ENCHANT_DEX,	// 덱스업
									L1SkillId.PHYSICAL_ENCHANT_STR,	// 힘업
									L1SkillId.BLESS_WEAPON,			// 블레스 웨폰
									L1SkillId.NATURES_TOUCH,		// 네이처스 터치
									L1SkillId.AQUA_PROTECTER,		// 아쿠아 프로텍트
									L1SkillId.REDUCTION_ARMOR,		// 리덕션 아머
									L1SkillId.BURNING_WEAPON,		// 버닝 웨폰
									
									L1SkillId.GREATER_HASTE,		// 그레이터 헤이스트
									L1SkillId.COMA_B,				// 코마의 축복 코인
									L1SkillId.God_buff,				// 흑사의 버프
									L1SkillId.LIFE_MAAN,			// 생명의 마안
									L1SkillId.miso1,				// 미소피아의 성장 축복
									L1SkillId.miso2,				// 미소피아의 방어 축복
									L1SkillId.miso3,				// 미소피아의 공격 축복
									L1SkillId.ADVANCE_SPIRIT,		// 어드밴스 스피릿
									L1SkillId.IRON_SKIN,			// 아이언 스킨
									L1SkillId.메티스축복주문서,		// 메티스의 축복 주문서
									};
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}		
			pc.setBuffnoch(0);
		}
		break;
		
		case 500733: { // 고급 원거리 버프: 무한
			int[] allBuffSkill = {	L1SkillId.DECREASE_WEIGHT,		// 디크리즈 웨이트
									L1SkillId.REMOVE_CURSE,			// 리무브 커스
									L1SkillId.BLESSED_ARMOR,		// 블레스드 아머
									L1SkillId.PHYSICAL_ENCHANT_DEX,	// 덱스업
									L1SkillId.PHYSICAL_ENCHANT_STR,	// 힘업
									L1SkillId.BLESS_WEAPON,			// 블레스 웨폰
									L1SkillId.NATURES_TOUCH,		// 네이처스 터치
									L1SkillId.AQUA_SHOT,			// 아쿠아 샷
									L1SkillId.EAGGLE_EYE,			// 이글 아이
									L1SkillId.STORM_SHOT,			// 스톰 샷
									
									L1SkillId.GREATER_HASTE,		// 그레이터 헤이스트
									L1SkillId.COMA_B,				// 코마의 축복 코인
									L1SkillId.God_buff,				// 흑사의 버프
									L1SkillId.LIFE_MAAN,			// 생명의 마안
									L1SkillId.miso1,				// 미소피아의 성장 축복
									L1SkillId.miso2,				// 미소피아의 방어 축복
									L1SkillId.miso3,				// 미소피아의 공격 축복
									L1SkillId.ADVANCE_SPIRIT,		// 어드밴스 스피릿
									L1SkillId.IRON_SKIN,			// 아이언 스킨
									L1SkillId.메티스축복주문서,		// 메티스의 축복 주문서
									};
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}	
			pc.setBuffnoch(0);
		}
		break;
		
		case 500734: { // 고급 마법강화 버프: 무한
			int[] allBuffSkill = {	L1SkillId.DECREASE_WEIGHT,		// 디크리즈 웨이트
									L1SkillId.REMOVE_CURSE,			// 리무브 커스
									L1SkillId.BLESSED_ARMOR,		// 블레스드 아머
									L1SkillId.PHYSICAL_ENCHANT_DEX,	// 덱스업
									L1SkillId.PHYSICAL_ENCHANT_STR,	// 힘업
									L1SkillId.BLESS_WEAPON,			// 블레스 웨폰
									L1SkillId.NATURES_TOUCH,		// 네이처스 터치
									L1SkillId.CONCENTRATION,		// 컨센트레이션
									L1SkillId.INSIGHT,				// 인사이트
									L1SkillId.PATIENCE,				// 페이션스
									
									L1SkillId.GREATER_HASTE,		// 그레이터 헤이스트
									L1SkillId.COMA_B,				// 코마의 축복 코인
									L1SkillId.God_buff,				// 흑사의 버프
									L1SkillId.LIFE_MAAN,			// 생명의 마안
									L1SkillId.miso1,				// 미소피아의 성장 축복
									L1SkillId.miso2,				// 미소피아의 방어 축복
									L1SkillId.miso3,				// 미소피아의 공격 축복
									L1SkillId.ADVANCE_SPIRIT,		// 어드밴스 스피릿
									L1SkillId.IRON_SKIN,			// 아이언 스킨
									L1SkillId.메티스축복주문서,		// 메티스의 축복 주문서
									};
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = null;
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}		
			pc.setBuffnoch(0);
		}
			break;
			
		case 60208: { // 완력의 체리빙수
			pc.get_skill().start_str_ice();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 60209: { // 민첩의 녹차빙수
			pc.get_skill().start_dex_ice();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 60210: { // 지식의 단팥빙수
			pc.get_skill().start_int_ice();
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		case 700021:// 봉인해제주문서 신청서
			if (pc.getInventory().checkItem(50021)) { //
				pc.sendPackets(new S_ChatPacket(pc, "이미 봉인해제 주문서를 갖고 있습니다."));
				return;
			}
			if (pc.getInventory().checkItem(700021, 1)) {
				pc.getInventory().consumeItem(700021, 1);
				pc.getInventory().storeItem(50021, 15);
			}
			break;
		case 700000:// 경험치 물약
			if (pc.getLevel() >= 1 && pc.getLevel() <= 48) {
				pc.setExp(pc.getExp() + 326144);
			} else if (pc.getLevel() >= 49 && pc.getLevel() <= 64) {
				pc.setExp(pc.getExp() + 2609152);
			} else if (pc.getLevel() >= 65 && pc.getLevel() <= 69) {
				pc.setExp(pc.getExp() + 1304576);
			} else if (pc.getLevel() >= 70 && pc.getLevel() <= 74) {
				pc.setExp(pc.getExp() + 652288);
			} else if (pc.getLevel() >= 75 && pc.getLevel() <= 78) {
				pc.setExp(pc.getExp() + 326144);
			} else if (pc.getLevel() == 79) {
				pc.setExp(pc.getExp() + 163072);
			} else if (pc.getLevel() >= 80 && pc.getLevel() <= 81) {
				pc.setExp(pc.getExp() + 81536);
			} else if (pc.getLevel() >= 82 && pc.getLevel() <= 83) {
				pc.setExp(pc.getExp() + 40768);
			} else if (pc.getLevel() >= 84 && pc.getLevel() <= 85) {
				pc.setExp(pc.getExp() + 20384);
			} else if (pc.getLevel() == 86) {
				pc.setExp(pc.getExp() + 10192);
			} else if (pc.getLevel() == 87) {
				pc.setExp(pc.getExp() + 5096);
			} else if (pc.getLevel() == 88) {
				pc.setExp(pc.getExp() + 2048);
			} else if (pc.getLevel() == 89) {
				pc.setExp(pc.getExp() + 1024);
			} else if (pc.getLevel() >= 90) {
				pc.setExp(pc.getExp() + 512);
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100074:
			if (pc.getLevel() > Config.ExpPo) {
				pc.sendPackets(""+Config.ExpPo+"레벨 이하만 사용이 가능합니다.");
				return;
			}
			pc.setExp(pc.getExp() + 360650);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 4100075:
			if (pc.getLevel() < Config.ExpPo1) {
				pc.sendPackets(""+Config.ExpPo1+"레벨 이상만 사용이 가능합니다.");
				return;
			}
			pc.setExp(pc.getExp() + 360650);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 350198:// 티칼달력
			if (CrockController.getInstance().isTimeCrock()) {
				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tcalendaro"));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tcalendarc"));
			}
			break;
		case 40033: // 엘릭서힘
			if (pc.getLevel() <= 49) {
				pc.sendPackets(new S_SystemMessage( "레벨 (50) 이상부터 복용 가능 합니다."));
				return;
			}
			if (pc.getElixirStats() < 6) {//10
				if (pc.getAbility().getStr() < 45) {
					pc.getAbility().addStr((byte) 1);
					pc.setElixirStats(pc.getElixirStats() + 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
					pc.save(); // DB에 캐릭터 정보를 기입한다
				} else {
					pc.sendPackets(new S_ServerMessage(481));
					// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
				}
			} else {
				pc.sendPackets(new S_SystemMessage("엘릭서(6)개 사용을 다하셨습니다."));
				// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
			}
			break;
		case 40034:// 엘릭서콘
			if (pc.getLevel() <= 49) {
				pc.sendPackets(new S_SystemMessage( "레벨 (50) 이상부터 복용 가능 합니다."));
				return;
			}
			if (pc.getElixirStats() < 6) {
				if (pc.getAbility().getCon() < 45) {
					pc.getAbility().addCon((byte) 1);
					pc.setElixirStats(pc.getElixirStats() + 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
					pc.save(); // DB에 캐릭터 정보를 기입한다
				} else {
					pc.sendPackets(new S_ServerMessage(481));
					// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
				}
			} else {
				pc.sendPackets(new S_SystemMessage("엘릭서(6)개 사용을 다하셨습니다."));
				// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
			}

			break;
		case 40035:// 엘릭서덱스
			if (pc.getLevel() <= 49) {
				pc.sendPackets(new S_SystemMessage( "레벨 (50) 이상부터 복용 가능 합니다."));
				return;
			}
			if (pc.getElixirStats() < 6) {
				if (pc.getAbility().getDex() < 45) {
					pc.getAbility().addDex((byte) 1);
					pc.resetBaseAc();
					pc.setElixirStats(pc.getElixirStats() + 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
					pc.save();
					; // DB에 캐릭터 정보를 기입한다
					break;
				} else {
					pc.sendPackets(new S_ServerMessage(481));
					// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
				}
			} else {
				pc.sendPackets(new S_SystemMessage("엘릭서(6)개 사용을 다하셨습니다."));
				// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
			}

			break;
		case 40036:// 엘릭서인트
			if (pc.getLevel() <= 49) {
				pc.sendPackets(new S_SystemMessage( "레벨 (50) 이상부터 복용 가능 합니다."));
				return;
			}
			if (pc.getElixirStats() < 6) {
				if (pc.getAbility().getInt() < 45) {
					pc.getAbility().addInt((byte) 1);
					pc.setElixirStats(pc.getElixirStats() + 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
					pc.save();
					; // DB에 캐릭터 정보를 기입한다
				} else {
					pc.sendPackets(new S_ServerMessage(481));
					// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
				}
			} else {
				pc.sendPackets(new S_SystemMessage("엘릭서(6)개 사용을 다하셨습니다."));
				// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
			}
			break;
		case 40037:// 엘릭서위즈
			if (pc.getLevel() <= 49) {
				pc.sendPackets(new S_SystemMessage( "레벨 (50) 이상부터 복용 가능 합니다."));
				return;
			}
			if (pc.getElixirStats() < 6) {
				if (pc.getAbility().getWis() < 45) {
					pc.getAbility().addWis((byte) 1);
					pc.resetBaseMr();
					pc.setElixirStats(pc.getElixirStats() + 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
					pc.save();
					; // DB에 캐릭터 정보를 기입한다
				} else {
					pc.sendPackets(new S_ServerMessage(481));
					// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
				}
			} else {
				pc.sendPackets(new S_SystemMessage("엘릭서(6)개 사용을 다하셨습니다."));
				// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
			}
			break;
		case 40038:// 엘릭서카리
			if (pc.getLevel() <= 49) {
				pc.sendPackets(new S_SystemMessage( "레벨 (50) 이상부터 복용 가능 합니다."));
				return;
			}
			if (pc.getElixirStats() < 6) {
				if (pc.getAbility().getCha() < 45) {
					pc.getAbility().addCha((byte) 1);
					pc.setElixirStats(pc.getElixirStats() + 1);
					pc.getInventory().removeItem(l1iteminstance, 1);
					pc.sendPackets(new S_OwnCharStatus2(pc));
					pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));
					pc.save();
					; // DB에 캐릭터 정보를 기입한다
				} else {
					pc.sendPackets(new S_ServerMessage(481));
					// \f1 하나의 능력치의 최대치는 25입니다. 다른 능력치를 선택해 주세요.
				}
			} else {
				pc.sendPackets(new S_SystemMessage("엘릭서(6)개 사용을 다하셨습니다."));
			}
			break;
		case 3000126:// 회복의문장 주머니
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(3000126, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				createNewItem2(pc, 900021, 1, 3); // 회복의문장 +3
			}
			break;
		case 3000127:// 성장의문장 주머니
			if (pc.getInventory().getSize() > 120) {
				pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(3000127, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				createNewItem2(pc, 900020, 1, 3); // 회복의문장 +3
			}
			break;
		case 3000123: { // 보스소환 주문서
			if(pc.is_combat_field() || StadiumManager.getInstance().is_on_stadium(pc.getMapId())){
				pc.sendPackets("해당 맵에서는 사용할 수 없습니다.");
				return;
			}
			
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (castle_id != 0) {
				pc.sendPackets("공성지역은 소환이 불가능합니다.");
				return;
			}
			if (itemId == 3000123) {
				if (pc.getZoneType() != 0) {// 세이프존
					pc.sendPackets("노멀존(필드) 에서만 사용이 가능합니다.");
					return;
				}
				useMobEventSpownWand(pc, l1iteminstance);
				pc.sendPackets("누군가가 소환되었다.");
			} else {
				pc.sendPackets("노멀 존에서만 사용이 가능합니다.");
			}
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
			break;
		/** 패키지이동주문서 **/
		case 800100:
			pc.setCashStep(1);
			pc.start_teleport(32866, 32878, 631, 5, 169, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 800101:
			pc.setCashStep(2);
			pc.start_teleport(32866, 32878, 631, 5, 169, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 800102:
			pc.setCashStep(3);
			pc.start_teleport(32866, 32878, 631, 5, 169, true, false);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 40124:
		case 30086:// 혈맹 귀환 스크롤
			int castle_id = 0;
			int house_id = 0;

			if (pc.getMapId() >= 1708 && pc.getMapId() <= 1712) {
				pc.sendPackets(new S_ServerMessage(647));
				return;
			}
			
			if (pc.getClanid() != 0) { // 크란 소속
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null) {
					castle_id = clan.getCastleId();
					house_id = clan.getHouseId();
				}
			}
			if (castle_id != 0) { // 성주 크란원
				loc = L1CastleLocation.getCastleLoc(castle_id);
				pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else if (house_id != 0) { // 아지트 소유 크란원
				loc = L1HouseLocation.getHouseLoc(house_id);
				pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				if (pc.getHomeTownId() > 0) {
					loc = L1TownLocation.getGetBackLoc(pc.getHomeTownId());
					pc.start_teleport(loc[0], loc[1], loc[2], 5, 169, true, false);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else {
					loc = Getback.GetBack_Location(pc, true);
					pc.start_teleport(loc[0], loc[1], loc[2], 5, 169, true, false);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
			}
			cancelAbsoluteBarrier(pc); // 아브소르트바리아의 해제
			break;
		case MJINNHelper.INN_KEYID:
			if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
				return;
			}
			if ((pc.hasSkillEffect(L1SkillId.SHOCK_STUN)) 
					|| pc.hasSkillEffect(L1SkillId.EMPIRE)
					|| (pc.hasSkillEffect(L1SkillId.ICE_LANCE))
					|| (pc.hasSkillEffect(L1SkillId.BONE_BREAK))
					|| (pc.hasSkillEffect(L1SkillId.EARTH_BIND) || pc.hasSkillEffect(L1SkillId.DESPERADO))
					|| (pc.hasSkillEffect(L1SkillId.FOG_OF_SLEEPING))) {
				return;
			}
			MJINNRoom.input(l1iteminstance, pc);
			break;
		case 9995:
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc, L1SkillId.나루토감사캔디, pc.getId(), pc.getX(), pc.getY(), null, 0,L1SkillUse.TYPE_GMBUFF);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 60256:
			if (pc.getInventory().checkItem(60255)) {
				pc.sendPackets(new S_ServerMessage(939));
				pc.sendPackets(new S_SystemMessage("드래곤의 자수정 보유."));
				return;
			}
			pc.getInventory().storeItem(60255, 1);
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 89136:
			if (pc.get_SpecialSize() == 40) {
				pc.sendPackets(new S_ServerMessage(1622));
				return;
			}
			if (pc.get_SpecialSize() == 0) {
				pc.set_SpecialSize(20);
				pc.getInventory().consumeItem(89136, 1);
				pc.sendPackets(new S_ServerMessage(1624, "20"));
			} else if (pc.get_SpecialSize() == 20) {
				pc.set_SpecialSize(40);
				pc.getInventory().consumeItem(89136, 1);
				pc.sendPackets(new S_ServerMessage(1624, "40"));
			}
			break;
		case 51093:
		case 51094:
		case 51095:
		case 51096:
		case 51097:
		case 51098:
		case 51099:
		case 51100:
			// 클래스 변경물약
			if (pc.getClanid() != 0) {
				pc.sendPackets(new S_ChatPacket(pc, "혈맹을 먼저 탈퇴하여 주시기 바랍니다."));
				return;
			} else if (itemId == 51093 && pc.getType() == 0) { // 자네 군주?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 군주 클래스 입니다."));
				return;
			} else if (itemId == 51094 && pc.getType() == 1) { // 자네 기사?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 기사 클래스 입니다."));
				return;
			} else if (itemId == 51095 && pc.getType() == 2) { // 자네 요정?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 요정 클래스 입니다."));
				return;
			} else if (itemId == 51096 && pc.getType() == 3) { // 자네 마법사?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 마법사 클래스 입니다."));
				return;
			} else if (itemId == 51097 && pc.getType() == 4) { // 자네 다크엘프?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 다크엘프 클래스 입니다."));
				return;
			} else if (itemId == 51098 && pc.getType() == 5) { // 자네 용기사?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 용기사 클래스 입니다."));
				return;
			} else if (itemId == 51099 && pc.getType() == 6) { // 자네 환술사?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 환술사 클래스 입니다."));
				return;
			} else if (itemId == 51100 && pc.getType() == 7) { // 자네 환술사?
				pc.sendPackets(new S_ChatPacket(pc, "당신은 이미 전사 클래스 입니다."));
				return;
			}
			pc.set_자동물약사용(false);
			pc.set_자동버프사용(false);
			pc.set_자동숫돌사용(false);
			
			스텟초기화(pc);
			int[] Mclass = new int[] { 0, 61, 138, 734, 2786, 6658, 6671, 12490 };
			int[] Wclass = new int[] { 1, 48, 37, 1186, 2796, 6661, 6650, 12494 };
			
			if (itemId == 51093 && pc.getType() != 0 && pc.get_sex() == 0) {// 군주
				pc.setType(0);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(40228, 1);
				pc.getInventory().storeItem(40226, 1);
			} else if (itemId == 51093 && pc.getType() != 0 && pc.get_sex() == 1) {// 군주
				pc.setType(0);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(40228, 1);
				pc.getInventory().storeItem(40226, 1);
			} else if (itemId == 51094 && pc.getType() != 1 && pc.get_sex() == 0) { // 기사
				pc.setType(1);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(40164, 1);
				pc.getInventory().storeItem(40165, 1);
			} else if (itemId == 51094 && pc.getType() != 1 && pc.get_sex() == 1) {// 기사
				pc.setType(1);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(40164, 1);
				pc.getInventory().storeItem(40165, 1);
			} else if (itemId == 51095 && pc.getType() != 2 && pc.get_sex() == 0) { // 요정
				pc.setType(2);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(40243, 1);
				pc.getInventory().storeItem(40240, 1);
				pc.getInventory().storeItem(40233, 1);
			} else if (itemId == 51095 && pc.getType() != 2 && pc.get_sex() == 1) {// 요정
				pc.setType(2);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(40243, 1);
				pc.getInventory().storeItem(40240, 1);
				pc.getInventory().storeItem(40233, 1);
			} else if (itemId == 51096 && pc.getType() != 3 && pc.get_sex() == 0) { // 마법사
				pc.setType(3);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(40188, 1);
				pc.getInventory().storeItem(40170, 1);
				pc.getInventory().storeItem(40176, 1);
				pc.getInventory().storeItem(40197, 1);
			} else if (itemId == 51096 && pc.getType() != 3 && pc.get_sex() == 1) {// 마법사
				pc.setType(3);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(40188, 1);
				pc.getInventory().storeItem(40170, 1);
				pc.getInventory().storeItem(40176, 1);
				pc.getInventory().storeItem(40197, 1);
			} else if (itemId == 51097 && pc.getType() != 4 && pc.get_sex() == 0) { // 다크엘프
				pc.setType(4);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(40276, 1);
				pc.getInventory().storeItem(40270, 1);
				pc.getInventory().storeItem(40268, 1);
			} else if (itemId == 51097 && pc.getType() != 4 && pc.get_sex() == 1) {// 다크엘프
				pc.setType(4);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(40276, 1);
				pc.getInventory().storeItem(40270, 1);
				pc.getInventory().storeItem(40268, 1);
			} else if (itemId == 51098 && pc.getType() != 5 && pc.get_sex() == 0) { // 용기사
				pc.setType(5);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(210025, 1);
				pc.getInventory().storeItem(210026, 1);
				pc.getInventory().storeItem(210020, 1);
				pc.getInventory().storeItem(210021, 1);
			} else if (itemId == 51098 && pc.getType() != 5 && pc.get_sex() == 1) {// 용기사
				pc.setType(5);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(210025, 1);
				pc.getInventory().storeItem(210026, 1);
				pc.getInventory().storeItem(210020, 1);
				pc.getInventory().storeItem(210021, 1);
			} else if (itemId == 51099 && pc.getType() != 6 && pc.get_sex() == 0) { // 환술사
				pc.setType(6);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(210014, 1);
				pc.getInventory().storeItem(210004, 1);
				pc.getInventory().storeItem(210000, 1);
				pc.getInventory().storeItem(210001, 1);
			} else if (itemId == 51099 && pc.getType() != 6 && pc.get_sex() == 1) {// 환술사
				pc.setType(6);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(210014, 1);
				pc.getInventory().storeItem(210004, 1);
				pc.getInventory().storeItem(210000, 1);
				pc.getInventory().storeItem(210001, 1);
			} else if (itemId == 51100 && pc.getType() != 7 && pc.get_sex() == 0) { // 전사
				pc.setType(7);
				pc.setClassId(Mclass[pc.getType()]);
				pc.getInventory().storeItem(210126, 1);
				pc.getInventory().storeItem(210121, 1);
				pc.getInventory().storeItem(210128, 1);
			} else if (itemId == 51100 && pc.getType() != 7 && pc.get_sex() == 1) {// 전사
				pc.setType(7);
				pc.setClassId(Wclass[pc.getType()]);
				pc.getInventory().storeItem(210126, 1);
				pc.getInventory().storeItem(210121, 1);
				pc.getInventory().storeItem(210128, 1);
			}
			
			SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
			if(noti != null){
				noti.set_class(MJEClassesType.fromGfx(pc.getClassId()).toInt());
			}
			
			if (pc.getWeapon() != null)
				pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
			pc.getInventory().takeoffEquip(945);
			pc.sendPackets(new S_CharVisualUpdate(pc));
			for (L1ItemInstance armor : pc.getInventory().getItems()) {
				for (int type = 0; type <= 12; type++) {
					if (armor != null) {
						pc.getInventory().setEquipped(armor, false, false, false, false);
					}
				}
			}
			pc.sendPackets(new S_DelSkill(255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
					255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255));
			deleteSpell(pc);
			deletePassiveSpell(pc);
			
			/** 스왑 삭제 **/
			pc.getSlotItems(0).clear();
			pc.getSlotItems(1).clear();
			
			pc.setCurrentSprite(pc.getClassId());
			pc.sendShape(pc.getClassId());
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.save();
			
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					GameClient clnt = pc.getNetConnection();
					C_NewCharSelect.restartProcess(pc);
					Account acc		= clnt.getAccount();
					clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
					if(acc.countCharacters() > 0)
						C_CommonClick.sendCharPacks(clnt);
				}
			}, 500L);
			break;
		case 210104:
			Connection connection = null;
			connection = L1DatabaseFactory.getInstance().getConnection();
//			PreparedStatement preparedstatement = connection.prepareStatement("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,5166,39,34,701,2000)"); // 운영자의방,감옥,배틀존대기실
			// 회상의 촛불 → 간헐적으로 튕긴다 → 좌표 복구 주문서 안먹힌다. → 5166을 정의에서 제거했다.
			PreparedStatement preparedstatement = connection.prepareStatement("UPDATE characters SET LocX=33432,LocY=32807,MapID=4 WHERE account_name=? and MapID not in (38,5001,99,997,39,34,701,2000)"); // 운영자의방,감옥,배틀존대기실
			preparedstatement.setString(1, client.getAccountName());
			preparedstatement.execute();
			preparedstatement.close();
			connection.close();
			pc.getInventory().removeItem(l1iteminstance, 1);
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "좌표가 정상적으로 복구 되었습니다. 잠시후 리스타트 됩니다."));
			pc.sendPackets(new S_SystemMessage("모든 캐릭터의 좌표가 정상적으로 복구 되었습니다."));
			
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					GameClient clnt = pc.getNetConnection();
					C_NewCharSelect.restartProcess(pc);
					Account acc		= clnt.getAccount();
					clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
					if(acc.countCharacters() > 0)
						C_CommonClick.sendCharPacks(clnt);
				}
			}, 3000L);
			break;
			
	/*	case 408991: //케릭명 변경 주문서
			pc.sendPackets(new S_SystemMessage(".이름 변경 바꿀 캐릭명으로 입력해주세요."));
			break;
			*/
		case 700023:
			ArrayList<L1ItemBookMark> _books = l1iteminstance.getBookMark();
			for (int i = 0; i < pc._bookmarks.size(); i++) {
				L1BookMark.deleteBookmark(pc, pc._bookmarks.get(i).getName());
			}
			pc._bookmarks.clear();
			pc._speedbookmarks.clear();
			L1BookMark.deleteBookmarkItem(pc);

			for (int i = 0; i < _books.size(); i++) {
				L1BookMark.addBookmarkItem(pc, _books.get(i));
			}
			pc.sendPackets(new S_BookMarkLoad(pc));
			L1ItemBookMark.deleteBookmarkItem(l1iteminstance.getId());
			pc.getInventory().removeItem(l1iteminstance, 1);
			break;
		case 5994:// 수련자의 상자(투구)
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(5994, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사() || pc.isKnight() || pc.isDragonknight() || pc.isCrown() || pc.isDarkelf()) {// 격수
					createNewItem2(pc, 22300, 1, 0);//수련자의 견고한 투구
				} else {//비격수
					createNewItem2(pc, 22306, 1, 0);//수련자의 신성한 투구
				}
			}
			break;
		case 5995:// 수련자의 상자(장갑)
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(5995, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {
					createNewItem2(pc, 22303, 1, 0);//수련자의 견고한 장갑
					createNewItem2(pc, 30073, 10, 0);//용기의 물약
					createNewItem2(pc, 210128, 1, 0);//전사의 인장(슬레이어)
				} else if (pc.isKnight()) {
					createNewItem2(pc, 22303, 1, 0);//수련자의 견고한 장갑
					createNewItem2(pc, 30073, 10, 0);//용기의 물약
					createNewItem2(pc, 40165, 1, 0);//기술서 (리덕션 아머)
				} else if (pc.isDragonknight()) {
					createNewItem2(pc, 22303, 1, 0);//수련자의 견고한 장갑
					createNewItem2(pc, 210023, 1, 0);//용기사의 서판(마그마 브레스)
				} else if (pc.isCrown()) {
					createNewItem2(pc, 22303, 1, 0);//수련자의 견고한 장갑
					createNewItem2(pc, 30075, 10, 0);//악마의피
					createNewItem2(pc, 40226, 1, 0);//트루 타겟
				} else if (pc.isDarkelf()) {
					createNewItem2(pc, 22303, 1, 0);//수련자의 견고한 장갑
					createNewItem2(pc, 40267, 1, 0);//흑정령의 수정 (쉐도우 아머)
				} else if (pc.isElf()) {
					createNewItem2(pc, 22309, 1, 0);//수련자의 신성한 장갑
					createNewItem2(pc, 30076, 10, 0);//엘븐 와퍼
					createNewItem2(pc, 40233, 1, 0);//바디 투 마인드
				} else if (pc.isWizard()) {
					createNewItem2(pc, 22309, 1, 0);//수련자의 신성한 장갑
					createNewItem2(pc, 40170, 1, 0);//파이어볼
				} else if (pc.isBlackwizard()) {
					createNewItem2(pc, 22309, 1, 0);//수련자의 신성한 장갑
					createNewItem2(pc, 30077, 10, 0);//유그드라 열매
					createNewItem2(pc, 210002, 1, 0);//기억의 수정(스매쉬 에너지)
				}
			}
			break;
			
		case 5996:// 수련자의 상자(망토)
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(5996, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사() || pc.isKnight() || pc.isDragonknight() || pc.isCrown() || pc.isDarkelf()) {// 격수
					createNewItem2(pc, 22302, 1, 0);//수련자의 견고한 망토
				} else {//비격수
					createNewItem2(pc, 22308, 1, 0);//수련자의 신성한 망토
				}
			}
			break;
			
		case 5997:// 수련자의 상자(신발)
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(5997, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사() || pc.isKnight() || pc.isDragonknight() || pc.isCrown() || pc.isDarkelf()) {// 격수
					createNewItem2(pc, 22304, 1, 0);//수련자의 견고한 망토
				} else {//비격수
					createNewItem2(pc, 22310, 1, 0);//수련자의 신성한 망토
				}
			}
			break;
			
		case 5998:// 수련자의 상자(보급품)
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(5998, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사() || pc.isKnight()) {
					createNewItem2(pc, 30073, 5, 0);//용기의 물약
				} else if (pc.isCrown()) {
					createNewItem2(pc, 30075, 5, 0);//악마의피
				} else if (pc.isDragonknight()) {
					createNewItem2(pc, 30081, 5, 0);//각인의 뼈조각
				} else if (pc.isWizard()) {
					createNewItem2(pc, 30083, 5, 0);//마나 회복 물약
				} else if (pc.isDarkelf()) {
					createNewItem2(pc, 30080, 5, 0);//수련자의 흑요석
				} else if (pc.isElf()) {
					createNewItem2(pc, 30076, 5, 0);//엘븐 와퍼
				} else if (pc.isBlackwizard()) {
					createNewItem2(pc, 30077, 5, 0);//유그드라 열매
				}
			}
			break;
			
		case 6007:// 수련자의 은 무기 상자
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(6007, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사() || pc.isBlackwizard()) {
					createNewItem2(pc, 9003, 1, 0);//수련자의 은 도끼
				} else if (pc.isKnight() || pc.isCrown() || pc.isDragonknight()) {
					createNewItem2(pc, 9001, 1, 0);//수련자의 은 장검
				} else if (pc.isDarkelf() || pc.isWizard()) {
					createNewItem2(pc, 9000, 1, 0);//수련자의 은 단검
				} else if (pc.isElf()) {
					createNewItem2(pc, 9004, 2000, 0);//수련자의 은화살
				}
			}
			break;
			
		case 5999:// 수련자의 상자(무기)
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(5999, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				if (pc.is전사()) {
					createNewItem2(pc, 203014, 1, 0);//대장장이의 도끼
					createNewItem2(pc, 22365, 1, 0);//전사단의 투구
				} else if (pc.isKnight()) {
					createNewItem2(pc, 56, 1, 0);//데스블레이드
					createNewItem2(pc, 20318, 1, 0);//용기의 벨트
				} else if (pc.isDragonknight()) {
					createNewItem2(pc, 22001, 1, 0);//용비늘 가더
					createNewItem2(pc, 500, 1, 0);//소멸자의 체인소드
				} else if (pc.isCrown()) {
					createNewItem2(pc, 51, 1, 0);//환긍지휘봉
					createNewItem2(pc, 20051, 1, 0);//군주의 위엄
				} else if (pc.isDarkelf()) {
					createNewItem2(pc, 13, 1, 0);//핑거오브 데스
					createNewItem2(pc, 20195, 1, 0);//그림자부츠
				} else if (pc.isElf()) {
					createNewItem2(pc, 50, 1, 0);//화염의 활
					createNewItem2(pc, 184, 1, 0);//화염의 검
				} else if (pc.isWizard()) {
					createNewItem2(pc, 20225, 1, 0);//마나수정구
					createNewItem2(pc, 20055, 1, 0);//마나망토
				} else if (pc.isBlackwizard()) {
					createNewItem2(pc, 503, 1, 0);//사파이어 키링크
					createNewItem2(pc, 22006, 1, 0);//환술사의 마법서
				}
			}
			break;
			
		case 7009:// 수련자의 상자(장신구)
			if (pc.getInventory().getSize() > 150) {
				pc.sendPackets(new S_ChatPacket(pc, "소지하고 있는 아이템이 너무 많습니다.상자 개봉시 150개이하로 해주세요"));
				return;
			}
			if (pc.getInventory().getWeight100() > 82) { // 이부분을 수정하면 오류가 발생한다.
				pc.sendPackets(new S_ChatPacket(pc, "소지품이 너무 무거워서 사용 할 수 없습니다."));
				return;
			}
			if (pc.getInventory().checkItem(7009, 1)) { // 체크되는 아이템과 수량
				pc.getInventory().removeItem(l1iteminstance, 1);
				
				createNewItem2(pc, 22073, 1, 0);//수련자의 귀걸지
				createNewItem2(pc, 22338, 1, 0);//수련자의 반지
				createNewItem2(pc, 7004, 3, 0);//수련자의 장신구 마법 주문서
			
			}
			break;
		}
	}
	private boolean 제작비법서템지급(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));// 무게 게이지가 부족하거나 인벤토리가
				return false;
			}
			pc.sendPackets(new S_SystemMessage("아이템 제작에 성공했습니다."));
			pc.sendPackets(new S_ServerMessage(143, item.getLogName())); // %0를
			pc.sendPackets(new S_SkillSound(pc.getId(), 7976));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7976));
			return true;
		} else {
			return false;
		}
	}
	private void deleteSpell(L1PcInstance pc) {
		int player = pc.getId();
		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id=?");
			pstm.setInt(1, player);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void deletePassiveSpell(L1PcInstance pc) {
		int player = pc.getId();
		Connection con = null;
		PreparedStatement pstm = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM passive_user_info WHERE character_id=?");
			pstm.setInt(1, player);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void useMobEventSpownWand(L1PcInstance pc, L1ItemInstance item) {
		try {
			int[][] mobArray = {
					{ 45573, 81201, 45609, 7320217, 7310015, 7310021, 7310028, 7310034, 7310041, 7310046, 7310051, 7310056, 7310061, 7310066 },
					{ 45546, 45600, 45601, 7320072, 7320073, 7320074, 7320075, 7320076, 7320077, 7320078, 7320079, 7320080, 7320081 },
					{ 5136, 5135, 5146, 45529, 7000093, 7310148, 7310154, 7310160, 45684 } };
			
			int category = 0;
			int rnd = _random.nextInt(mobArray[category].length);
			L1SpawnUtil.spawn(pc, mobArray[category][rnd], 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void petbuy(GameClient client, int npcid, int paytype, int paycount) {
		L1PcInstance pc = client.getActiveChar();
		L1PcInventory inv = pc.getInventory();
		int charisma = pc.getAbility().getTotalCha();
		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
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
		charisma -= petcost;
		int petCount = charisma / 6;
		if (petCount <= 0) {
			pc.sendPackets(new S_ServerMessage(489)); // 물러가려고 하는 애완동물이 너무 많습니다.
			return;
		}
		if (pc.getInventory().checkItem(paytype, paycount)) {
			pc.getInventory().consumeItem(paytype, paycount);
			L1SpawnUtil.spawn(pc, npcid, 0, 0);
			L1MonsterInstance targetpet = null;
			L1ItemInstance petamu = null;
			L1PetType petType = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
				if (object instanceof L1MonsterInstance) {
					targetpet = (L1MonsterInstance) object;
					petType = PetTypeTable.getInstance().get(targetpet.getNpcTemplate().get_npcId());
					if (petType == null || targetpet.isDead()) {
						return;
					}
					if (charisma >= 6 && inv.getSize() < 180) {
						petamu = inv.storeItem(40314, 1); // 펫의 아뮤렛트
						if (petamu != null) {
							new L1PetInstance(targetpet, pc, petamu.getId());
							pc.sendPackets(new S_ItemName(petamu));
						}
					}
				}
			}
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
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // %0를
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
			return true;
		} else {
			return false;
		}
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
	private void 스텟초기화(L1PcInstance pc) {
		try {

			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0,L1SkillUse.TYPE_LOGIN);

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
			pc.save();
		} catch (Exception e) {
			System.out.println("스텟초기화 명령어 에러");
			e.printStackTrace();
		}
	}
	private void cancelAbsoluteBarrier(L1PcInstance pc) { // 아브소르트바리아의 해제
		if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			pc.removeSkillEffect(ABSOLUTE_BARRIER);
		}
	}

	@Override
	public String getType() {
		return C_ITEM_USE2;
	}
}
