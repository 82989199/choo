package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.ANTA_BUFF;
import static l1j.server.server.model.skill.L1SkillId.ANTA_MAAN;
import static l1j.server.server.model.skill.L1SkillId.BERSERKERS;
import static l1j.server.server.model.skill.L1SkillId.BIRTH_MAAN;
import static l1j.server.server.model.skill.L1SkillId.CLEAR_MIND;
import static l1j.server.server.model.skill.L1SkillId.COMA_A;
import static l1j.server.server.model.skill.L1SkillId.COMA_B;
import static l1j.server.server.model.skill.L1SkillId.CONCENTRATION;
import static l1j.server.server.model.skill.L1SkillId.COOKING_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.COOKING_END;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MIRROR;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static l1j.server.server.model.skill.L1SkillId.DISEASE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_PUPLE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_TOPAZ;
import static l1j.server.server.model.skill.L1SkillId.DRESS_EVASION;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_FALL_DOWN;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.BLOOD_LUST;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_PROTECTION;
import static l1j.server.server.model.skill.L1SkillId.EMERALD_NO;
import static l1j.server.server.model.skill.L1SkillId.EMERALD_YES;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EXP_BUFF;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;
import static l1j.server.server.model.skill.L1SkillId.FAFU_BUFF;
import static l1j.server.server.model.skill.L1SkillId.FAFU_MAAN;
import static l1j.server.server.model.skill.L1SkillId.FEAR;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_B;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_C;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_D;
import static l1j.server.server.model.skill.L1SkillId.GUARD_BREAK;
import static l1j.server.server.model.skill.L1SkillId.God_buff;
import static l1j.server.server.model.skill.L1SkillId.HORROR_OF_DEATH;
import static l1j.server.server.model.skill.L1SkillId.INSIGHT;
import static l1j.server.server.model.skill.L1SkillId.LIFE_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.LIFE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.LIND_MAAN;
import static l1j.server.server.model.skill.L1SkillId.MORTAL_BODY;
import static l1j.server.server.model.skill.L1SkillId.NATURES_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.NEW_START_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.PANIC;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.POLLUTE_WATER;
import static l1j.server.server.model.skill.L1SkillId.POLY_RING_MASTER;
import static l1j.server.server.model.skill.L1SkillId.POLY_RING_MASTER2;
import static l1j.server.server.model.skill.L1SkillId.REDUCE_WEIGHT;
import static l1j.server.server.model.skill.L1SkillId.RESIST_ELEMENTAL;
import static l1j.server.server.model.skill.L1SkillId.RESIST_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.RE_START_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.RIND_BUFF;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.SIDE_OF_ME_BLESSING;
import static l1j.server.server.model.skill.L1SkillId.SILENCE;
import static l1j.server.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL4;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL5;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL6;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGON_PEARL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STRIKER_GALE;
import static l1j.server.server.model.skill.L1SkillId.SetBuff;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit1;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit2;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit3;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit4;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit5;
import static l1j.server.server.model.skill.L1SkillId.VALA_MAAN;
import static l1j.server.server.model.skill.L1SkillId.VENOM_RESIST;
import static l1j.server.server.model.skill.L1SkillId.WEAKNESS;
import static l1j.server.server.model.skill.L1SkillId.WIND_SHACKLE;
import static l1j.server.server.model.skill.L1SkillId.miso1;
import static l1j.server.server.model.skill.L1SkillId.miso2;
import static l1j.server.server.model.skill.L1SkillId.miso3;
import static l1j.server.server.model.skill.L1SkillId.레벨업보너스;
import static l1j.server.server.model.skill.L1SkillId.정상의가호;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import MJShiftObject.MJShiftObjectManager;
import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJExpAmpSystem.MJExpAmplifierLoader;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveUserLoader;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.FindInventory.InventoryFindItemFilterFactory;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_GROUP_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_INFO_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.GameServer;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SkillCheck;
import l1j.server.server.Controller.LimitShopController;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.datatables.CharacterQuestMonsterTable;
import l1j.server.server.datatables.CharacterQuestTable;
import l1j.server.server.datatables.CharacterSlotItemTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.EventAlarmTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanMatching;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_BookMarkLoad;
import l1j.server.server.serverpackets.S_ChangeItemUseType;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_CharacterConfig;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ElfIcon;
import l1j.server.server.serverpackets.S_FairlyConfig;
import l1j.server.server.serverpackets.S_FishingAddItem;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InvList;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SlotChange;
import l1j.server.server.serverpackets.S_SlotOpen;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UnityIcon;
import l1j.server.server.serverpackets.S_Unknown1;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_Weight;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1GetBackRestart;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CheckInitStat;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.SystemUtil;

public class C_LoginToServer extends ClientBasePacket {

	/** 날짜 및 시간 기록 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int sec = rightNow.get(Calendar.SECOND);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = String.format("[%d:%d:%d]", year, month, day);
	String totime1 = String.format("[%d:%d:%d]", hour, min, sec);
	String date = String.format("%d_%d_%d", year, month, day);

	static class BuffInfo {
		public int skillId;
		public int remainTime;
		public int polyId;
	}

	private static final String C_LOGIN_TO_SERVER = "[C] C_LoginToServer";
	private static Logger _log = Logger.getLogger(C_LoginToServer.class.getName());

	private static void print_message(GameClient clnt, String message, boolean is_kick) throws Exception{
		System.out.println("─────────────────────────────────");
		System.out.println(message);
		System.out.println("─────────────────────────────────");
		if(is_kick)
		{
			clnt.kick();
			clnt.close();
		}
	}
	
	public static void doEnterWorld(String charName, GameClient client) throws FileNotFoundException, Exception {
		L1PcInstance pc = null;
		try {
			client.setLoginRecord(true);
			Calendar cal = Calendar.getInstance();
			int 시간 = Calendar.HOUR;
			int 분 = Calendar.MINUTE;
			/** 0 오전 , 1 오후 * */
			String 오전오후 = "오후";
			if (cal.get(Calendar.AM_PM) == 0) {
				오전오후 = "오전";
			}

			String login = client.getAccountName();

			if (GMCommands.isCharacterBlock(charName)) {
				System.out.println("─────────────────────────────────");
				System.out.println(String.format("압류중인 캐릭터 [%s]이(가) 접속을 시도했습니다.", charName));
				System.out.println("─────────────────────────────────");
				client.sendPacket(new S_LoginResult(52));
				return;
			}

			pc = L1PcInstance.load(charName);
			Account account = null;
			if(pc == null){
				print_message(client, String.format("캐릭터를 찾을 수 없습니다.(월드 접속) : %s(%s)", charName, client.getIp()), true);
				return;
			}
			if (pc.getAccountName() != null) {
				account = Account.load(pc.getAccountName());// 오류부근
			} else {
				System.out.println("─────────────────────────────────");
				System.out.println("pc.getAccountName  Null  " + charName);
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return;
			}
			if (account == null) {
				System.out.println("─────────────────────────────────");
				System.out.println("account Null  " + charName);
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return;
			}

			if (client.getAccount() == null) {
				System.out.println("─────────────────────────────────");
				System.out.println("계정 Null 접속 시도 " + charName);
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return;
			}

			if (client.getActiveChar() != null) {
				System.out.println("─────────────────────────────────");
				System.out.println("동일 ID의 중복 접속이므로 (" + client.getIp() + ")의 접속을 강제 종료합니다. ");
				System.out.println("─────────────────────────────────");
				client.close();
				return;
			}
			
			if(!client.is_shift_client()){
				GameClient clientByAccount = LoginController.getInstance().getClientByAccount(login);
				if (clientByAccount == null || clientByAccount != client) {
					System.out.println(clientByAccount);
					System.out.println(client);
					System.out.println("─────────────────────────────────");
					System.out.println("동일 Account의 중복 접속이므로 (" + client.getIp() + ")의 접속을 강제 종료합니다. ");
					System.out.println("─────────────────────────────────");
					client.close();
					return;
				}
			}

			/** 2캐릭 버그 방지 Start */
			L1PcInstance OtherPc = L1World.getInstance().getPlayer(charName);

			if (OtherPc != null) {
				boolean isPrivateShop = OtherPc.isPrivateShop();
				boolean 오토군주 = OtherPc.isAutoClanjoin();
				boolean 오토낚시 = OtherPc.isAutoFish();
				GameServer.disconnectChar(OtherPc);
				OtherPc = null;

				if (isPrivateShop == false /* && isAutoCrown == false */) {
					System.out.println("─────────────────────────────────");
					System.out.println("동일 ID의 중복 접속이므로 (" + client.getIp() + ")의 접속을 강제 종료합니다. #2");
					System.out.println("─────────────────────────────────");
					client.kick();
					return;
				}
			}

			Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();// 원본
			for (L1PcInstance bugpc : pcs) {
				if (bugpc.getAccountName().equals(client.getAccountName())) {
					if (!bugpc.isPrivateShop() || bugpc.getNetConnection() != null) {
						System.out.println("─────────────────────────────────");
						System.out.println("동일 Account의 중복 접속이므로 (" + client.getIp() + ")의 접속을 강제 종료합니다.2");
						System.out.println("─────────────────────────────────");
						client.kick();
						GameServer.disconnectChar(bugpc);
					}
				}
			}
			pcs = null;
			/** 2캐릭 버그 방지 End */

			if ((pc == null) || !login.equals(pc.getAccountName())) {
				System.out.println("─────────────────────────────────");
				System.out.println("현재 계정에 없는 캐릭 접속시도 : " + charName + " 계정 : " + client.getAccountName());
				System.out.println("─────────────────────────────────");
				client.kick();
				client.close();
				return;
			}

			if (!pc.isGm() && Config.LEVEL_DOWN_RANGE != 0) {
				if (pc.getHighLevel() - pc.getLevel() >= Config.LEVEL_DOWN_RANGE) {
					System.out.println("─────────────────────────────────");
					_log.info("렙다운 허용범위 초과: " + charName + " 계정= " + login + " host=" + client.getIp());
					System.out.println("렙다운 허용범위 초과: " + charName + " 계정= " + login + " host=" + client.getIp());
					System.out.println("─────────────────────────────────");
					client.kick();
					return;
				}
			}

			/** 방어구 버그방지소스 2/18 **/
			if (pc.getAC().getAc() < -Config.ACLEVEL)// 이상시 추방
			{
				// 영구추방
				// if(!isBanned)
				// iptable.banIp(client.getIp());

				// 어카운트를 BAN 한다
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);

				client.kick();
				client.close();

				System.out.println("▶ 방어구버그 추방 : " + pc.getName());
				return;
			}

			/** 스텟,레벨 버그방지소스 2/18 **/
			if (pc.getLevel() > pc.getHighLevel()) {
				client.kick();
				client.close();
				System.out.println("▶ 레벨버그 추방 : " + pc.getName());
				return;
			}
			if (pc.getLevel() >= Config.LIMITLEVEL && !pc.isGm()) {// 경험치
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				pc.sendPackets(new S_SystemMessage(pc.getName() + " 를 계정압류 하였습니다."));
				pc.sendPackets(new S_Disconnect());

				if (pc.getOnlineStatus() == 1) {
					pc.sendPackets(new S_Disconnect());
				}
				client.kick();
				client.close();
				System.out.println("▶ 컨피그레벨버그 재접속으로 [압류] : " + pc.getName());
				return;
			}
			if (pc.getLevel() >= 90 || pc.getHighLevel() >= 90) {
				if (pc.getAbility().getCon() > 50 || pc.getAbility().getStr() > 50 || pc.getAbility().getDex() > 50
						|| pc.getAbility().getCha() > 50 || pc.getAbility().getInt() > 50
						|| pc.getAbility().getWis() > 50) {
					client.kick();
					client.close();
					System.out.println("▶ 스텟버그 추방 : " + pc.getName());
					return;
				}
			} else {
				if (pc.getAbility().getCon() > 45 || pc.getAbility().getStr() > 45 || pc.getAbility().getDex() > 45
						|| pc.getAbility().getCha() > 45 || pc.getAbility().getInt() > 45
						|| pc.getAbility().getWis() > 45) {
					client.kick();
					client.close();
					System.out.println("▶ 스텟버그 추방 : " + pc.getName());
					return;
				}
			}

			if (pc.getType() < 0 || pc.getType() > 7) {
				client.kick();
				client.close();
				System.out.println("▶ 삭제요청된 케릭터로 접속. 추방 : " + pc.getName());
			}

			if (pc.is무인상점()) {
				pc.set무인상점(false);
			}

			if (pc.getLevel() == 1) {
				pc.setExp(ExpTable.getExpByLevel(Config.Start_Char_Level));
			}
			
			// 월드 진입 시 CMD에 표기
			System.out.println(String.format("### [클라이언트 접속] 계정: %s | 캐릭터: %s | %s %d시 %d분 | 아이피: %s | 메모리: %d MB" , login, charName, 오전오후, cal.get(시간), cal.get(분), client.getIp(), SystemUtil.getUsedMemoryMB()));

			/** 로그파일저장 **/
			LoggerInstance.getInstance()
					.addConnection("접속 캐릭=" + charName + "	계정=" + login + "	IP=" + client.getHostname());

			pc.set_instance_status(MJEPcStatus.WORLD);
			pc.setOnlineStatus(1);
			pc.create_captcha();
			CharacterTable.updateOnlineStatus(pc);
			L1World.getInstance().storeObject(pc);

			pc.setNetConnection(client);
			client.setActiveChar(pc);

			pc.sendPackets(new S_Unknown1(pc));

			if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
				pc.sendPackets(new S_CharacterConfig(pc.getId()));
			}

			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 0); // 1번슬롯
			CharacterSlotItemTable.getInstance().selectCharSlot(pc, 1); // 2번슬롯

			// TODO 캐릭생성시 동영상 출력
			/*
			 * if (pc.getMapId() == 7783) { NewCharacter(pc); }
			 */

			MJKDALoader.getInstance().install(pc, false);
			/*loadItems(pc, false);
			sendItemPacket(pc);

			int[] skillList = loadSkills(pc);
			sendSkillPacket(pc, skillList);

			MJPassiveUserLoader.load(pc);*/

			MJCopyMapObservable.getInstance().resetPosition(pc);
			L1BookMark.bookmarkDB(pc);
			getItemBookMark(pc);
			pc.sendPackets(new S_BookMarkLoad(pc));
			// 엘릭서 섭취량 로드
			pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.Elixir, pc.getElixirStats()));

			int mapId = pc.getMapId();
			if (mapId >= 1700 && mapId <= 1712) {
				pc.setX(33437);
				pc.setY(32813);
				pc.setMap((short) 4);
			}

			GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
			L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
			for (L1GetBackRestart gbr : gbrList) {
				if (pc.getMapId() == gbr.getArea()) {
					pc.setX(gbr.getLocX());
					pc.setY(gbr.getLocY());
					pc.setMap(gbr.getMapId());
					break;
				}
			}

			MJCopyMapObservable.getInstance().resetPosition(pc);
			MJRaidSpace.getInstance().getBackPc(pc);
			DungeonTimeProgressLoader.load(pc);

			/** 2016.11.26 MJ 앱센터 LFC **/
			MJInstanceSpace.getInstance().getBackPc(pc);
			/** 2016.11.26 MJ 앱센터 LFC **/

			// altsettings.properties로 GetBack가 true라면 거리에 이동시킨다
			if (Config.GET_BACK) {
				int[] loc = Getback.GetBack_Location(pc, true);
				pc.setX(loc[0]);
				pc.setY(loc[1]);
				pc.setMap((short) loc[2]);
			}
			
			
			
			

			// 전쟁중의 기내에 있었을 경우, 성주 혈맹이 아닌 경우는 귀환시킨다.
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (pc.getMapId() == 66) {
				castle_id = 6;
			}
			if (0 < castle_id) {
				if (MJCastleWarBusiness.getInstance().isNowWar(castle_id)) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					if (clan != null && clan.getCastleId() != castle_id) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					} else if (pc.getMapId() == 4) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					}
				}
			}

			L1Map l1map = pc.getMap();
			if(l1map == null || !l1map.isInMap(pc.getX(), pc.getY())){
				MJCopyMapObservable.getInstance().resetAlwaysPositon(pc);
			}
			
			pc.beginGameTimeCarrier();
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(new S_MapID(pc.getMap().getBaseMapId(), pc.getMap().isUnderwater()));
			MJExpAmplifierLoader.getInstance().set(pc);
			pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()));
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.Unknown_LOGIN2, 0, 0));
			pc.sendPackets(S_WorldPutObject.put(pc));
			L1World.getInstance().addVisibleObject(pc);
			
			final L1PcInstance p = pc;
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					loadItems(p, false);
					sendItemPacket(p);
					
					/**일일 제한 상점 */
					LimitShopController.getInstance().LoadLimitpc(p);
					

					int[] skillList = loadSkills(p);
					sendSkillPacket(p, skillList);
														
					MJPassiveUserLoader.load(p);
					p.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RUNE, 1));
					
					loadItems(p, true);
					List<BuffInfo> buffList = loadBuff(p);
					processBuff(p, buffList);

					
					for (Iterator<L1ItemInstance> partner = p.getInventory().getItems().iterator(); partner.hasNext();) {
						L1ItemInstance item = (L1ItemInstance) partner.next();
						if (item.getItemId() == 700024)
							p.sendPackets(new S_PacketBox(S_PacketBox.BOOKMARK, item.getItemId(), "$13719",
									L1BookMark.ShowBookmarkitem(p, item.getItemId())));
					}
					
					p.sendPackets(new S_PacketBox(S_PacketBox.INIT_DODGE, 0x0000));
					p.sendPackets(new S_PacketBox(S_PacketBox.DODGE, 0));
					p.getLight().turnOnOffLight();
					p.sendPackets(new S_SPMR(p));
					p.sendPackets(new S_PacketBox(S_PacketBox.인벤저장));// 위치변경
					p.sendPackets(new S_SPMR(p));
					p.startHpMpRegeneration(); // hpr , mpr 시작.
					p.startObjectAutoUpdate();
					p.beginExpMonitor();
					p.setCryOfSurvivalTime();
					p.getInventory().consumeItem(810006);
					p.getInventory().consumeItem(810007);
					
					/**던전 타임 재검사*/
					DungeonTimeInformationLoader loader = DungeonTimeInformationLoader.getInstance();
					DungeonTimeInformation dtInfo = loader.from_map_id(p.getMapId());
					if(dtInfo == null){
						p.dec_dungeon_progress(dtInfo);
					}
					
				}
			}, 100); // 접속 로드 시 지연 시간, 딜레이
			
			if (account.getphone() != null && account.getphone().length() >= 11) {
				보안버프(pc);
			}

			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.UI4));
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.UI5));
			pc.sendVisualEffectAtLogin(); // 크라운, 독, 수중등의 시각 효과를 표
			pc.sendPackets(new S_PacketBox(S_PacketBox.LOGIN_UNKNOWN3, 1));
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LOGIN));
			pc.sendClanMarks();// 성혈군주 왕관표시
			client.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);

			L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
			if (jonje == null) {
				pc.sendPackets(new S_SystemMessage("존재버그 강제종료! 재접속하세요"));
				client.kick();
				return;
			}
			if (pc.getCurrentHp() > 0) {
				pc.setDead(false);
				pc.setStatus(0);
			} else {
				pc.setDead(true);
				pc.setStatus(ActionCodes.ACTION_Die);
			}
			if (pc.getLevel() >= 51 && pc.getLevel() - 50 > pc.getBonusStats() && pc.getAbility().getAmount() < 150) {
				int upstat = (pc.getLevel() - 50) - (pc.getBonusStats());
				String s = Integer.toString(upstat);
				pc.sendPackets(new S_Message_YN(479, s));
			}
			serchSummon(pc);
			MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			// 온라인 알리기.
			if (clan != null)
				clan.updateClanMemberOnline(pc);

			if (pc.getClanid() != 0) { // 크란 소속중
				if (clan != null) {
					if (clan.getBless() != 0) {
						new L1SkillUse().handleCommands(pc, 504 + clan.getBless(), pc.getId(), pc.getX(), pc.getY(),
								null, clan.getBuffTime()[clan.getBless() - 1], L1SkillUse.TYPE_LOGIN);
					}
					pc.sendPackets(new S_ACTION_UI(clan.getClanName(), pc.getClanRank()));
					pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));
					if (clan.getGazeSize() != 0) {
						pc.sendPackets(new S_ClanAttention(clan.getGazeSize(), clan.getGazeList()));
					}
					if (pc.getClanid() == clan.getClanId() &&
							// 크란을 해산해, 재차, 동명의 크란이 창설되었을 때의 대책
							pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
						for (L1PcInstance clanMember : clan.getOnlineClanMember()) {
							if (clanMember.getId() != pc.getId()) {
								clanMember.sendPackets(new S_ServerMessage(843, pc.getName()));
								// 지금, 혈맹원의%0%s가게임에접속했습니다.
							}
						}

						MJWar war = clan.getCurrentWar();
						if (war != null) {
							war.notifyEnenmy(pc);
							if (war instanceof MJCastleWar) {
								MJCastleWar castleWar = (MJCastleWar) war;
								if (castleWar.isRun())
									castleWar.onLordBuff(pc);
							}
						}

					} else {
						pc.setClanid(0);
						pc.setClanname("");
						pc.setClanRank(0);
						pc.save(); // DB에 캐릭터 정보를 기입한다
					}
				} else {
					pc.setClanid(0);
					pc.setClanname("");
					pc.setClanRank(0);
					pc.save(); // DB에 캐릭터 정보를 기입한다
				}
			}
			
			loadNBuff(pc);

			if (pc.getPartnerId() != 0) { // 결혼중
				L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
				if (partner != null && partner.getPartnerId() != 0) {
					if (pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
						pc.sendPackets(new S_ServerMessage(548));
						// 당신의 파트너는 지금게임중입니다.
						partner.sendPackets(new S_ServerMessage(549));
						// 당신의 파트너는 방금로그인했습니다.
					}
				}
			}

			int tamcount = pc.tamcount();
			if (tamcount > 0) {
				long tamtime = pc.TamTime() / 1000;

				int aftertamtime = (int) tamtime;

				if (aftertamtime < 0) {
					aftertamtime = 0;
				}

				if (tamcount == 1) {
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, aftertamtime, 8265, 4181));
					pc.setSkillEffect(Tam_Fruit1, aftertamtime * 1000);
					pc.getAC().addAc(-1);
				} else if (tamcount == 2) {
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, aftertamtime, 8266, 4182));
					pc.setSkillEffect(Tam_Fruit2, aftertamtime * 1000);
					pc.getAC().addAc(-2);
				} else if (tamcount == 3) {
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, aftertamtime, 8267, 4183));
					pc.setSkillEffect(Tam_Fruit3, aftertamtime * 1000);
					pc.getAC().addAc(-3);
					pc.addDamageReductionByArmor(2);
				} else if (tamcount == 4) {
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, aftertamtime, 8268, 5046));
					pc.setSkillEffect(Tam_Fruit4, aftertamtime * 1000);
					pc.getAC().addAc(-4);
					pc.addDamageReductionByArmor(2);
				} else if (tamcount == 5) {
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, aftertamtime, 8269, 5047));
					pc.setSkillEffect(Tam_Fruit5, aftertamtime * 1000);
					pc.getAC().addAc(-5);
					pc.addDamageReductionByArmor(2);
				}

				pc.sendPackets(new S_OwnCharStatus(pc));
			}

			pc.setSkillEffect(SetBuff, 30 * 1000);

			/*
			if (pc.getLevel() < Config.NEW_PLAYER) {// 바포메트 시스템 관련 처리
				pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, 6, true));
				pc.setNBapoLevel(7);
			}*/

			if (pc.getLevel() <= Config.Start_Char_Boho) {
				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Start_BUFF, 3804, true));
			} else {
				pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Start_BUFF));
			}

			pc.sendPackets(new S_PacketBox(S_PacketBox.공격가능거리, pc, pc.getWeapon()), true);

			// 아인하사드
			if (pc.getLevel() > 5) {
				int einhasad = pc.getAccount().getBlessOfAin() + (pc.getLastLoginTime() == null ? 0 : ((int) (System.currentTimeMillis() - pc.getLastLoginTime().getTime()) / (15 * 60 * 1000)));
				if (einhasad > 50000000) {
					einhasad = 50000000;
				}
				pc.getAccount().setBlessOfAin(einhasad);

				if (pc.getZoneType() == 1) {
					pc.startEinhasadTimer();
				}
				if (einhasad > 10000) {
					SC_REST_EXP_INFO_NOTI.send(pc);
				}
			}

			long sysTime = System.currentTimeMillis();
			if (pc.getNetConnection().getAccount().getDragonRaid() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getDragonRaid().getTime()) {
					long BloodTime = pc.getNetConnection().getAccount().getDragonRaid().getTime() - sysTime;
					pc.removeSkillEffect(L1SkillId.DRAGONRAID_BUFF);
					pc.setSkillEffect(L1SkillId.DRAGONRAID_BUFF, (int) BloodTime);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONRAID_BUFF, (int) BloodTime / 1000), true);
				}
			}
			
			/** 차단 리스트 불러오기 **/
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
			if (exList != null) {
				setExcludeList(pc, exList);
			}
			
			/** 스탯 리뉴얼 표기 **/
			// RenewStat(pc);
			
			pc.RenewStat();
			/** 무게 게이지 **/
			pc.sendPackets(new S_Weight(pc));

			try {
				l1j.server.swing.chocco.count += 1;
				l1j.server.swing.chocco.label2.setText(" " + l1j.server.swing.chocco.count);

				l1j.server.swing.chocco.userlist.add(pc.getName());
				// System.out.println(String.format("게임접속: [%s] 사용자가 월드에 진입
				// 하였습니다. IP: %s", pc.getName(), client.getIp()));
			} catch (Exception e) {
			}

			// TODO 유저접속 알림(운영자용)
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (player.isGm()) {
					player.sendPackets(new S_SystemMessage("\\aA접속: [\\aG" + pc.getName() + "\\aA] / \\aG"
							+ client.getAccountName() + "\\aA /\\aG " + client.getIp() + ""));
				}
			}

			// 3.63아이템패킷처리
			pc.isWorld = true;
			L1ItemInstance temp = null;
			try {
				// 착용한 아이템이 슬롯에 정상적으로 표현하도록 하기위해 임시로 작업함.
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					temp = item;
					if (item.isEquipped())
						pc.getInventory().toSlotPacket(pc, item, true);

				}
			} catch (Exception e) {
				System.out.println("에러 남 의심되는 아이템은 ->> " + temp.getItem().getName());
			}
			DragonknightPolyCheck(pc);
			ClanMatching(pc);
			Clanclan(pc);

			/** 몽섬리뉴얼 **/
			if (pc.getMap().getBaseMapId() == 1936) {
				pc.start_teleport(33968, 32961, 4, 0, 169, false, false);
			}
			/** 카이저 훈련소 **/
			if (pc.getMap().getBaseMapId() == 1400) {
				pc.start_teleport(33491, 32762, 4, 0, 169, false, false);
			}
			/** 화룡의 안식처 **/
			if (pc.getMap().getBaseMapId() == 2600 || pc.getMap().getBaseMapId() == 2699) {
				pc.start_teleport(33705, 32504, 4, 0, 169, false, false);
			}
			/** 정령의무덤 **/
			if (pc.getMapId() == 430) {
				pc.start_teleport(32779, 32831, 622, 0, 169, false, false);
			}

			// 얼던맵 기
			if (pc.getMapId() >= 2101 && pc.getMapId() <= 2151 || pc.getMapId() >= 2151 && pc.getMapId() <= 2201) {
				pc.start_teleport(33442, 32809, 4, 5, 169, false, false);
			}

			// 화염의 막대/신비한 회복 물약 삭제.
			if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2151 || pc.getMapId() >= 2151 && pc.getMapId() <= 2201)) {
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					if (item.getItemId() == 30055 || item.getItemId() == 30056) {
						if (item != null) {
							pc.getInventory().removeItem(item, item.getCount());
						}
					}
				}
			}

			pc.sendPackets(new S_FairlyConfig(pc));
			/** 세이프존패킷 **/
			safetyzone(pc);
			
		
			// 피씨방버프
			if (pc.getAccount().getBuff_PC방() != null) {
				if (sysTime <= pc.getAccount().getBuff_PC방().getTime()) {
					long 피씨타임 = pc.getAccount().getBuff_PC방().getTime() - sysTime;
					TimeZone seoul = TimeZone.getTimeZone("UTC");
					Calendar calendar = Calendar.getInstance(seoul);
					calendar.setTimeInMillis(피씨타임);
					int d = calendar.get(Calendar.DATE) - 1;
					int h = calendar.get(Calendar.HOUR_OF_DAY);
					int m = calendar.get(Calendar.MINUTE);
					int sc = calendar.get(Calendar.SECOND);
					StringBuilder sb = new StringBuilder(128);
					sb.append("[PC방 이용 시간] ");
					if (d > 0)
						sb.append(d).append("일 ").append(h).append("시간 ").append(m).append("분 ").append(sc)
								.append("초 남았습니다.");
					else if (h > 0)
						sb.append(h).append("시간 ").append(m).append("분 ").append(sc).append("초 남았습니다.");
					else if (m > 0)
						sb.append(m).append("분 ").append(sc).append("초 남았습니다.");
					else
						sb.append(sc).append("초 남았습니다.");

					pc.sendPackets(new S_SystemMessage(sb.toString()));
					pc.setPC방_버프(true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.PC방버프, 1));
				}
			} else {
				pc.setPC방_버프(false);
			}

			if (Config.깃털이벤트상점) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[이벤트 진행중] 행운의상점 이벤트"));
			}

			if (pc.PC방_버프) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.PCBANG_SET, true));
			} else {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.PCBANG_SET, false));
				
				
			}

			// 76반지 개방완료
			int slotStatus = 0;

			if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT76)) // (왼짝반지)
				slotStatus |= S_ReturnedStat.OPEN_SLOT_LRING;
			if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT81)) // 81반지 개방완료(오른짝)
				slotStatus |= S_ReturnedStat.OPEN_SLOT_RRING;
			if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT59)) // 귀걸이 개방완료
				slotStatus |= S_ReturnedStat.OPEN_SLOT_EARRING;
			if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT_SHOULD))
				slotStatus |= S_ReturnedStat.OPEN_SLOT_SHOULD;
			if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT_BADGE))
				slotStatus |= S_ReturnedStat.OPEN_SLOT_BADGE;

			pc.sendPackets(new S_SlotOpen(S_ReturnedStat.SUBTYPE_RING, slotStatus));

			if (pc.getHellTime() > 0) {
				pc.beginHell(false);
			}
			if (CheckMail(pc) > 0) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 1091));
				pc.sendPackets(new S_ServerMessage(428)); // 편지가 도착했습니다.
			}
			pc.LoadCheckStatus();
			if (!CheckInitStat.CheckPcStat(pc)) {
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
				client.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);
				return;
			}
			pc.sendPackets(new S_Karma(pc));
			
			/**이벤트 알림*/
		//	pc.sendPackets(EventAlarmTable.delS_ActionBox);
		//	pc.sendPackets(EventAlarmTable.newS_ActionBox);
			
			//pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EVENT_SYSTEM, 0, false));
			
			pc.sendPackets(new S_SlotChange(S_SlotChange.SLOT_CHANGE, pc));

			/** 봉줌신청후 로그인시 알림 **/
			int currentTime = (int) (System.currentTimeMillis() / 1000);
			if (pc.getSealScrollTime() > 0) {
				if (pc.getSealScrollTime() < currentTime) {
					// 인벤에 아aa이템 지급
					pc.getInventory().storeItem(50021, pc.getSealScrollCount());
					pc.setSealScrollTime(0);
					pc.setSealScrollCount(0);
					pc.save();
					pc.sendPackets(new S_ChatPacket(pc, "봉인해제 주문서가 지급되었습니다.", 1));
				} else {
					int remainMin = (pc.getSealScrollTime() - currentTime) / 60 + 1;
					int remainHour = remainMin / 60;
					remainMin -= remainHour * 60;
					int remainDay = remainHour / 24;
					remainHour -= remainDay * 24;
					pc.sendPackets(new S_ChatPacket(pc,String.format("봉인해제주문서 지급까지 %d일 %d시간 %d분 남았습니다.", remainDay, remainHour, remainMin), 1));
				}
			}

			if (pc.getReturnStat() != 0) {
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
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
			}

			

			if (pc.getLevel() > 1 && pc.getLevel() <= Config.Lineage_Buff) {
				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.HUNTER_BLESS3, 4126, true));
			} else {
				pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.HUNTER_BLESS3));
			}

			/*SupplementaryService warehouse = WarehouseManager.getInstance()
					.getSupplementaryService(pc.getAccountName());
			int size = warehouse.getSize();
			if (size > 0) {
				pc.sendPackets(new S_SystemMessage("부가 아이템 창고 : 수령하지 않은 아이템이 있습니다. 부가 아이템 창고 확인."));
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "부가 아이템 창고 : 수령하지 않은 아이템이 있습니다.\\aA 부가 아이템 창고 확인."));
			}*/
			
			
			
			/*if (pc.get_food() < 225) {
				pc.sendPackets("\\f2자연회복불가:포만감 상태:(" + pc.get_food() + "% : 200%) 입니다.");
				pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.FOOD_BUFF));
				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.NO_FOOD_BUFF, 3116, true));
			} else {
				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.FOOD_BUFF, 1082, true));
				pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.NO_FOOD_BUFF));
			}*/

			pc.load_lateral_status();
			
			if(Config.USE_ATTENDANCE_SYSTEM){
				SC_ATTENDANCE_BONUS_INFO_EXTEND.send(pc);
				SC_ATTENDANCE_BONUS_GROUP_INFO.send(pc);
				SC_ATTENDANCE_USER_DATA_EXTEND.send(pc);
			}

			if (pc.getLevel() <= 95) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, Config.GAME_SERVER_MENT));
			}
			
			//if (Config.STANDBY_SERVER) {
			//	pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "오픈대기 6~8시 입니다. 30분마다 오픈대기 감사 상자가 지급 됩니다."));
			//	pc.sendPackets("오대기 6~8시 입니다. 30분마다 오픈대기감사상자가 지급됨");
			//}

			// 퀘스트 몬스터 시스템
			CharacterQuestMonsterTable.getInstance().LoginQuestInfo(pc);

			if (pc.getLevel() <= 69) {
				CharacterQuestTable.getInstance().LoginQuestInfo(pc); // 퀘스트 정보로딩
				pc.sendPackets(new S_QuestTalkIsland(pc));
				pc.sendPackets(new S_QuestTalkIsland(14, 257));
			}
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			pc.sendPackets(new S_OwnCharAttrDef(pc));

			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					
					BQSCharacterDataLoader.in(p);
					
					SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(p.getAccountName());		
					if(pwh.getItems().size() > 0){
						p.sendPackets(new S_SystemMessage("부가 아이템 창고 : 수령하지 않은 아이템이 있습니다. \\aA 부가 아이템 창고 확인."));
						p.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "부가 아이템 창고 : 수령하지 않은 아이템이 있습니다.\\aA 부가 아이템 창고 확인."));
						}
					
					/**
					 * 그랑카인의 분노 체크
					 */
					if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
						grangKinAngerCheck(p);
						grangKinAngerMessage(p);
					}
				}
			}, 500); // 접속 로드 시 지연 시간
			try {
				if (!p.get_자동물약리스트().isEmpty()) {
					p.sendPackets(new S_SystemMessage("\\f2자동 물약과 버프 목록이 저장되어있습니다."));
					p.sendPackets(new S_SystemMessage("\\f2[시작]버튼을 누르시면 기존 설정대로 실행됩니다."));
				} else {
						p.sendPackets(new S_SystemMessage("\\f3자동 물약이 선택되지 않았습니다."));
						}
					for (Iterator<Integer> item = p.get_자동물약리스트().iterator(); item.hasNext();) {
						int id = ((Integer) item.next()).intValue();
						L1Item _item = ItemTable.getInstance().getTemplate(id);
						p.sendPackets(new S_SystemMessage("\\f2자동 물약: " + (_item == null ? "알수없음" : _item.getName() + "로 지정되어있습니다.")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			p.set_자동물약사용(false);
			p.set_자동버프사용(false);
			p.set_자동숫돌사용(false);	
			//L1NeigeBugCheck.shapechange(pc);
			pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 169, false, false);	
			    
			    
			
			
			
		} catch (Exception e) {
			System.out.println("■접속 오류■ : 스크린샷을 찍어 주시길 바랍니다.");
			e.printStackTrace();
		}
		
		// if(pc != null)
		// pc.updateObject();
	}


	public C_LoginToServer(byte abyte0[], GameClient client) throws FileNotFoundException, Exception {
		super(abyte0);

		String charName = readS();
		doEnterWorld(charName, client);
	}

	private static void getItemBookMark(L1PcInstance pc) {
		L1ItemInstance[] items = pc.getInventory().findItemsId(700023);
		for (int i = 0; i < items.length; i++) {
			L1ItemBookMark.bookmarItemkDB(pc, items[i]);
		}
	}

	private static void loadItems(L1PcInstance pc, boolean sendOption) {
		// DB로부터 캐릭터와 창고의 아이템을 읽어들인다
		if (sendOption)
			pc.getInventory().sendOptioon();
		else
			CharacterTable.getInstance().restoreInventory(pc);
		if (sendOption)
			MJRankUserLoader.getInstance().onUser(pc);
	}

	private static void sendItemPacket(L1PcInstance pc) {
		SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
		for(L1ItemInstance item : pc.getInventory().getItems()){
			noti.add_item_info(ItemInfo.newInstance(item));
		}
		noti.set_on_start(true);
		noti.set_owner_oid(pc.getId());
		pc.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);

		L1ItemInstance teleport_item = pc.getInventory().findItemId(40100);
		if(teleport_item != null){
			if (pc.getInventory().checkEquippedAtOnce(new int[] { 900111, 20288 })){
				pc.sendPackets(new S_DeleteInventoryItem(teleport_item));
				noti = SC_ADD_INVENTORY_NOTI.newInstance();
				noti.add_item_info(ItemInfo.newInstance(teleport_item, 6));
				pc.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
			}
		}
		
		L1ItemInstance jivea_item = pc.getInventory().findItemId(900075);
		if(jivea_item != null){
			pc.sendPackets(new S_Ability(7, true));
			pc.sendPackets(new S_Ability(2, true));
		}
		
		/*
		List<L1ItemInstance> fishingItems = pc.getInventory()
				.getItems(InventoryFindItemFilterFactory.createFishItemsFilter());
		if (fishingItems.size() > 0) {
			try {
				pc.sendPackets(S_FishingAddItem.getList(fishingItems));
				pc.sendPackets(new S_InvList(pc.getInventory().getItems(), fishingItems));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pc.sendPackets(new S_InvList(pc.getInventory().getItems()));
		}
		fishingItems.clear();*/
	}

	/** Safetyzone 표시 **/
	private static void safetyzone(L1PcInstance pc) {
		if (pc.getZoneType() == 0) {
			if (pc.getSafetyZone() == true) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
				pc.setSafetyZone(false);
			}
		} else {
			if (pc.getSafetyZone() == false) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
				pc.setSafetyZone(true);
			}
		}
	}

	private static int CheckMail(L1PcInstance pc) {
		int count = 0;
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement(" SELECT count(*) as cnt FROM letter where receiver = ? AND isCheck = 0");
			pstm1.setString(1, pc.getName());

			rs = pstm1.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}

		return count;
	}

	private static int[] loadSkills(L1PcInstance pc) {
		int[] skillList = new int[30];

		for (int i = 0; i < 30; ++i) {
			skillList[i] = 0;
		}

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			L1Skills l1skills = null;

			List<Integer> skillIdList = new ArrayList<Integer>();
			while (rs.next()) {
				int skillId = rs.getInt("skill_id");

				l1skills = SkillsTable.getInstance().getTemplate(skillId);

				if (l1skills != null && l1skills.getSkillLevel() > 0 && l1skills.getSkillLevel() <= 29) {
					if (skillId == 234) {
						skillList[l1skills.getSkillLevel()] |= l1skills.getId();
					} else {
						skillList[l1skills.getSkillLevel() - 1] |= l1skills.getId();
					}
				}

				skillIdList.add(skillId);
				pc.setSkillMastery(skillId);
			}
			SkillCheck.getInstance().AddSkill(pc.getId(), skillIdList);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return skillList;
	}

	private static void sendSkillPacket(L1PcInstance pc, int[] skillList) {
		pc.sendPackets(new S_AddSkill(skillList[0], skillList[1], skillList[2], skillList[3], skillList[4],
				skillList[5], skillList[6], skillList[7], skillList[8], skillList[9], skillList[10], skillList[11],
				skillList[12], skillList[13], skillList[14], skillList[15], skillList[16], skillList[17], skillList[18],
				skillList[19], skillList[20], skillList[21], skillList[22], skillList[23], skillList[24], skillList[25],
				skillList[26], skillList[27], skillList[28], skillList[29], pc.getElfAttr()));
	}

	private static void 보안버프(L1PcInstance pc) {
		pc.getAC().addAc(-1);
		pc.sendPackets(new S_PacketBox(pc, S_PacketBox.ICON_SECURITY_SERVICES));
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private static void serchSummon(L1PcInstance pc) {
		try {
			for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
				if (summon.getMaster().getId() == pc.getId()) {
					summon.setMaster(pc);
					pc.addPet(summon);
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc));
					}
				}
			}
		} catch (Exception e) {

		}
	}
	/*
	 * private void RenewStat(L1PcInstance pc) { pc.sendPackets(new
	 * S_CharStat(pc, S_CharStat.STAT_REFRESH)); pc.sendPackets(new
	 * S_CharStat(pc, 1, S_CharStat.Stat_Str)); // 스탯 상세능력 pc.sendPackets(new
	 * S_CharStat(pc, 1, S_CharStat.Stat_Int)); pc.sendPackets(new
	 * S_CharStat(pc, 1, S_CharStat.Stat_Wis)); pc.sendPackets(new
	 * S_CharStat(pc, 1, S_CharStat.Stat_Dex)); pc.sendPackets(new
	 * S_CharStat(pc, 1, S_CharStat.Stat_Con)); pc.sendPackets(new
	 * S_CharStat(S_CharStat.STAT_VIEW, 25)); // 스탯능력치 pc.sendPackets(new
	 * S_CharStat(S_CharStat.STAT_VIEW, 35)); pc.sendPackets(new
	 * S_CharStat(S_CharStat.STAT_VIEW, 45)); pc.sendPackets(new S_CharStat(pc,
	 * S_CharStat.STAT_REFRESH)); }
	 */

	// 용기사
	private static void DragonknightPolyCheck(L1PcInstance pc) {
		L1ItemInstance weapon = pc.getWeapon();
		int polyId = pc.getCurrentSpriteId();
		if (pc.isDragonknight()) {
			if (polyId == 9206 || polyId == 6137 || polyId == 6142 || polyId == 6147 || polyId == 6152 || polyId == 6157
					|| polyId == 9205 || polyId == 6267 || polyId == 6270 || polyId == 6273 || polyId == 6276) {
				for (L1ItemInstance items : pc.getInventory().getItems()) {
					if (items.getItem().getType() == 18) {
						if (items.getItem().getType1() == 24) {
							items.getItem().setType1(50);
							if (weapon != null) {
								pc.getInventory().setEquipped(weapon, false);
								pc.getInventory().setEquipped(weapon, true);
							}
						}
					}
				}
			} else {
				for (L1ItemInstance items : pc.getInventory().getItems()) {
					if (items.getItem().getType() == 18) {
						if (items.getItem().getType1() == 50) {
							items.getItem().setType1(24);
							if (weapon != null) {
								pc.getInventory().setEquipped(weapon, false);
								pc.getInventory().setEquipped(weapon, true);
							}
						}
					}
				}
			}
		}
	}

	private static void ClanMatching(L1PcInstance pc) {
		L1ClanMatching cml = L1ClanMatching.getInstance();
		if (pc.getClanid() == 0) {
			if (!pc.isCrown()) {
				cml.loadClanMatchingApcList_User(pc);
			}
		} else {
			switch (pc.getClanRank()) {
			case 3:
			case 4:
			case 6:
			case 10:
			case 9:
				// 부군주, 혈맹군주, 수호기사
				cml.loadClanMatchingApcList_Crown(pc);
				break;
			}
		}
	}

	private static void Clanclan(L1PcInstance pc) {
		// 3245군주의 부름: 혈맹에 가입하세요//3246군주의 부름: 혈원을 모집하세요
		// 3247혈맹을 창설하고 쉽게 알리세요//3248혈맹 가입 요청이 왔습니다
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan == null && pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(3247)); // 혈맹을 창설하고 쉽게 알리세요
			// pc.sendPackets(new S_SystemMessage(pc.getName() + "의 계급이 " +
			// L1Clan.군주 + " 로 변경되었습니다."));
		} else if (clan != null && pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(3246)); // 혈맹원을 모집하세요
		} else if (clan == null && !pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(3245)); // 혈맹에 가입하세요
		}
	}

	private static List<BuffInfo> loadBuff(L1PcInstance pc) {
		List<BuffInfo> buffList = new ArrayList<BuffInfo>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT skill_id, remaining_time, poly_id FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			while (rs.next()) {
				BuffInfo buffInfo = new BuffInfo();

				buffInfo.skillId = rs.getInt("skill_id");
				buffInfo.remainTime = rs.getInt("remaining_time");
				buffInfo.polyId = rs.getInt("poly_id");

				buffList.add(buffInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return buffList;
	}

	private static void processBuff(L1PcInstance pc, List<BuffInfo> buffList) {
		// int icon[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		// 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0 };
		int[] icon = new int['±'];
		Arrays.fill(icon, 0);

		for (BuffInfo buffInfo : buffList) {
			int skillid = buffInfo.skillId;
			int remaining_time = buffInfo.remainTime;

			if ((skillid >= COOKING_BEGIN && skillid <= COOKING_END) || skillid == 1541) {
				L1Cooking.eatCooking(pc, skillid, remaining_time);
				continue;
			}
			switch (skillid) {
			case L1SkillId.USER_WANTED:
				pc.doWanted(true);
				break;
			case BLOOD_LUST:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 6, remaining_time));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 6, 0));
				pc.setBraveSpeed(1);
				break;
			case God_buff: // 흑사 버프
				pc.getAC().addAc(-2);
				pc.addSpecialResistance(eKind.SPIRIT, 10);
				pc.addMaxHp(20);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.addMaxMp(13);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 4914, remaining_time));
				break;
			case SHAPE_CHANGE:
				int poly_id = buffInfo.polyId;
				L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_LOGIN, false);
				break;
			case STATUS_BRAVE:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remaining_time));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0));
				pc.setBraveSpeed(1);
				break;
			case STATUS_ELFBRAVE:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 3, remaining_time));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0));
				pc.setBraveSpeed(1);
				break;
			case STATUS_HASTE:
				pc.sendPackets(new S_SkillHaste(pc.getId(), 1, remaining_time));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
				pc.setMoveSpeed(1);
				break;
			case STATUS_BLUE_POTION:
			case STATUS_BLUE_POTION2:
				pc.sendPackets(new S_SkillIconGFX(34, remaining_time, true));
				break;
			case STATUS_CHAT_PROHIBITED:
				pc.sendPackets(new S_SkillIconGFX(36, remaining_time));
				break;
			case COUNTER_MIRROR:
				// icon[0] = remaining_time / 16;
				pc.addWeightReduction(300);
				break;
			case DECREASE_WEIGHT:// 마법사 디크리즈 웨이트
			case REDUCE_WEIGHT:// 환술사 리듀스 웨이트
				icon[0] = remaining_time / 16;
				pc.addWeightReduction(180);
				break;
			case DECAY_POTION:
				icon[1] = remaining_time / 4;
				break;
			case SILENCE:
				icon[2] = remaining_time / 4;
				break;
			case VENOM_RESIST:
				icon[3] = remaining_time / 4;
				break;
			case WEAKNESS:
				icon[4] = remaining_time / 4;
				pc.addDmgup(-5);
				pc.addHitup(-1);
				break;
			case DISEASE:
				icon[5] = remaining_time / 4;
				pc.addHitup(-6);
				pc.getAC().addAc(12);
				break;
			case DRESS_EVASION:
				icon[6] = remaining_time / 4;
				break;
			case BERSERKERS:
				icon[7] = remaining_time / 4;
				pc.getAC().addAc(10);
				pc.addDmgup(2);
				pc.addHitup(8);
				break;
			case NATURES_TOUCH:
				icon[8] = remaining_time / 4;
				break;
			case WIND_SHACKLE:
				icon[9] = remaining_time / 4;
				break;
			case ERASE_MAGIC:
				icon[10] = remaining_time / 4;
				break;
			case ADDITIONAL_FIRE:
				icon[11] = remaining_time / 4;
				break;
			case ELEMENTAL_FALL_DOWN:
				icon[12] = remaining_time / 4;
				int playerAttr = pc.getElfAttr();
				int i = -50;
				switch (playerAttr) {
				case 0:
					pc.sendPackets(new S_ServerMessage(79));
					break;
				case 1:
					pc.getResistance().addEarth(i);
					pc.setAddAttrKind(1);
					break;
				case 2:
					pc.getResistance().addFire(i);
					pc.setAddAttrKind(2);
					break;
				case 4:
					pc.getResistance().addWater(i);
					pc.setAddAttrKind(4);
					break;
				case 8:
					pc.getResistance().addWind(i);
					pc.setAddAttrKind(8);
					break;
				default:
					break;
				}
				break;
			case ELEMENTAL_FIRE:
				icon[13] = remaining_time / 4;
				break;
			case STRIKER_GALE:
				icon[14] = remaining_time / 4;
				break;
			case SOUL_OF_FLAME:
				icon[15] = remaining_time / 4;
				break;
			case POLLUTE_WATER:
				icon[16] = remaining_time / 4;
			case COMA_A:
				icon[30] = (remaining_time + 16) / 32;
				icon[31] = 40;
				pc.getAbility().addAddedCon(1);
				pc.getAbility().addAddedDex(5);
				pc.getAbility().addAddedStr(5);
				pc.addHitRate(3);
				pc.getAC().addAc(-3);
				break;
			case COMA_B:
				icon[30] = (remaining_time + 16) / 32;
				icon[31] = 41;
				pc.getAbility().addSp(1);
				pc.getAbility().addAddedCon(3);
				pc.getAbility().addAddedDex(5);
				pc.getAbility().addAddedStr(5);
				pc.addHitRate(5);
				pc.getAC().addAc(-8);
				break;
			case EXP_POTION:
				if (pc.getSafetyZone()) {
					pc.sendPackets(S_InventoryIcon.iconNewUnLimit(EXP_POTION + 1, 4973, true)); // 세이프티존
				} else {
					pc.sendPackets(S_InventoryIcon.icoNew(EXP_POTION, 4973, remaining_time, true)); // 사냥터
				}
				break;
			case EXP_BUFF:
				long hasad = pc.getAccount().getBlessOfAin();
				if (hasad < 10000) {
					pc.sendPackets(S_InventoryIcon.iconNewUnLimit(EXP_BUFF + 1, 5087, true)); // 세이프티존
				} else {
					pc.sendPackets(S_InventoryIcon.icoNew(EXP_BUFF, 5087, remaining_time, true)); // 사냥터
				}
				break;
			case STATUS_CASHSCROLL:
				int time = (remaining_time / 4) - 255;
				if (time <= 0) {
					time += 255;
				}
				icon[18] = time;
				icon[19] = 61;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addMaxHp(50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				break;
			case STATUS_CASHSCROLL2:
				int time2 = (remaining_time / 4) - 255;
				if (time2 <= 0) {
					time2 += 255;
				}
				icon[18] = time2;
				icon[19] = 62;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addMaxMp(40);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				break;
			case STATUS_CASHSCROLL3:
				int time3 = (remaining_time / 4) - 255;
				if (time3 <= 0) {
					time3 += 255;
				}
				icon[18] = time3;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addDmgup(3);
				pc.addHitup(3);
				pc.getAbility().addSp(3);
				break;
			case STATUS_CASHSCROLL4:
				int time4 = (remaining_time / 4) - 255;
				if (time4 <= 0) {
					time4 += 255;
				}
				icon[18] = time4;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.getAbility().addSp(3);
				pc.addBaseMagicHitUp(5);
				pc.getResistance().addcalcPcDefense(3);
				break;
			case STATUS_CASHSCROLL5:
				int time5 = (remaining_time / 4) - 255;
				if (time5 <= 0) {
					time5 += 255;
				}
				icon[18] = time5;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addBowDmgup(3);
				pc.addBowHitup(5);
				pc.getResistance().addcalcPcDefense(3);
				break;
			case STATUS_CASHSCROLL6:
				int time6 = (remaining_time / 4) - 255;
				if (time6 <= 0) {
					time6 += 255;
				}
				icon[18] = time6;
				icon[19] = 63;
				icon[38] = remaining_time <= 1020 ? 0 : 1;
				pc.addDmgRate(3);
				pc.addHitup(5);
				pc.getResistance().addcalcPcDefense(3);
				break;
			case CONCENTRATION:
				icon[20] = remaining_time / 16;
				break;
			case INSIGHT:
				icon[21] = remaining_time / 16;
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedCon((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				pc.getAbility().addAddedWis((byte) 1);
				pc.resetBaseMr();
				break;
			case PANIC:
				icon[22] = remaining_time / 16;
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.getAbility().addAddedCha((byte) -1);
				pc.resetBaseMr();
				break;
			case MORTAL_BODY:
				icon[23] = remaining_time / 4;
				break;
			case HORROR_OF_DEATH:
				icon[24] = remaining_time / 4;
				pc.getAbility().addAddedStr((byte) -10);
				pc.getAbility().addAddedInt((byte) -10);
				break;
			case FEAR:
				icon[25] = remaining_time / 4;
				break;
			case PATIENCE:
				icon[26] = remaining_time / 4;
				break;
			case GUARD_BREAK:
				icon[27] = remaining_time / 4;
				pc.getAC().addAc(15);
				break;
			case DRAGON_SKIN:
				icon[28] = remaining_time / 16;
				break;
			case STATUS_FRUIT:
				icon[29] = remaining_time / 4;
				if (pc.isPassive(MJPassiveID.DARK_HORSE.toInt())){ 
					pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remaining_time));
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0));
					pc.setBraveSpeed(1);

				} else {
		            pc.sendPackets(new S_SkillBrave(pc.getId(), 4, remaining_time));
		            Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 4, 0));
		            pc.setBraveSpeed(4);

				}

				break;	
			case RESIST_MAGIC:
				pc.getResistance().addMr(10);
				pc.sendPackets(new S_ElfIcon(remaining_time / 16, 0, 0, 0));
				break;
			case RESIST_ELEMENTAL:
				pc.getResistance().addAllNaturalResistance(10);
				pc.sendPackets(new S_ElfIcon(0, 0, remaining_time / 16, 0));
				break;
			case ELEMENTAL_PROTECTION:
				int attr = pc.getElfAttr();
				if (attr == 1) {
					pc.getResistance().addEarth(50);
				} else if (attr == 2) {
					pc.getResistance().addFire(50);
				} else if (attr == 4) {
					pc.getResistance().addWater(50);
				} else if (attr == 8) {
					pc.getResistance().addWind(50);
				}
				pc.sendPackets(new S_ElfIcon(0, 0, 0, remaining_time / 16));
				break;
			case ANTA_MAAN:
				icon[34] = remaining_time / 30;
				icon[35] = 46;
				pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case FAFU_MAAN:
				icon[34] = remaining_time / 30;
				icon[35] = 47;
				pc.addSpecialResistance(eKind.SPIRIT, 5);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case LIND_MAAN:
				icon[34] = remaining_time / 30;
				icon[35] = 48;
				pc.add_magic_critical_rate(2);
				pc.addSpecialResistance(eKind.FEAR, 5);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_SPMR(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case VALA_MAAN:
				icon[34] = remaining_time / 30;
				icon[35] = 49;
				pc.addDmgup(2);
				pc.addBowDmgup(2);
				pc.addSpecialResistance(eKind.ABILITY, 5);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case BIRTH_MAAN:
				icon[34] = remaining_time / 30;
				icon[35] = 50;
				pc.addSpecialResistance(eKind.SPIRIT, 5);
				pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case SHAPE_MAAN:
				icon[34] = remaining_time / 30;
				icon[35] = 51;
				pc.add_magic_critical_rate(1);
				pc.addSpecialResistance(eKind.SPIRIT, 5);
				pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				pc.addSpecialResistance(eKind.FEAR, 5);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_SPMR(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case LIFE_MAAN:
				icon[34] = remaining_time / 30;
				icon[35] = 52;
				pc.addSpecialResistance(eKind.ALL, 5);
				pc.addDmgup(2);
				pc.addBowDmgup(2);
				pc.add_magic_critical_rate(1);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_SPMR(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				break;
			case FEATHER_BUFF_A:
				icon[36] = remaining_time / 16;
				icon[37] = 70;
				pc.addHpr(3);
				pc.addMpr(3);
				pc.addDmgup(2);
				pc.addHitup(2);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				// pc.addSp(2);
				pc.getAbility().addSp(2);
				break;
			case FEATHER_BUFF_B:
				icon[36] = remaining_time / 16;
				icon[37] = 71;
				pc.addHitup(2);
				// pc.addSp(1);
				pc.getAbility().addSp(1);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				break;
			case FEATHER_BUFF_C:
				icon[36] = remaining_time / 16;
				icon[37] = 72;
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				pc.getAC().addAc(-2);
				break;
			case FEATHER_BUFF_D:
				icon[36] = remaining_time / 16;
				icon[37] = 73;
				pc.getAC().addAc(-1);
				break;
			case ANTA_BUFF:
				pc.getAC().addAc(-2);
				pc.getResistance().addWater(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, remaining_time / 60));
				break;
			case FAFU_BUFF:
				pc.addHpr(3);
				pc.addMpr(1);
				pc.getResistance().addWind(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, remaining_time / 60));
				break;
			case RIND_BUFF:
				pc.addHitup(3);
				pc.addBowHitup(3);
				pc.getResistance().addFire(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, remaining_time / 60));
				break;
			case STATUS_DRAGON_PEARL:
				pc.sendPackets(new S_Liquor(pc.getId(), 8));
				pc.broadcastPacket(new S_Liquor(pc.getId(), 8));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, remaining_time));			
				pc.setPearl(1);
				break;
			case 레벨업보너스:
				pc.sendPackets(new S_PacketBox(remaining_time, true, true));
				break;
			case DRAGON_TOPAZ:
				pc.sendPackets(new S_PacketBox(remaining_time, 2, true, true));
				SC_REST_EXP_INFO_NOTI.send(pc);
				break;
			case DRAGON_PUPLE:
				pc.sendPackets(new S_PacketBox(remaining_time, 1, true, true));
				break;
			case EMERALD_NO:
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 0x01, remaining_time));
				break;
			case EMERALD_YES:
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 0x02, remaining_time));
				break;
			case 정상의가호:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 12536, remaining_time));
				break;
			case SetBuff:
				remaining_time = 30;
				break;
			case POLY_RING_MASTER:
				poly_id = buffInfo.polyId;
				L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_LOGIN, true);
				break;
			case POLY_RING_MASTER2:
				poly_id = buffInfo.polyId;
				L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_LOGIN, true,true);
				break;
			case SIDE_OF_ME_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14646, remaining_time));
				break;
			case RE_START_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14647, remaining_time));
				break;
			case NEW_START_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14648, remaining_time));
				break;
			case LIFE_BLESSING:
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 14649, remaining_time));
				break;
			case L1SkillId.FREEZEENG_ARMOR:
				pc.addEffectedER(5);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));

				SC_SPELL_BUFF_NOTI FREEZEENG_ARMOR = SC_SPELL_BUFF_NOTI.newInstance();
				FREEZEENG_ARMOR.set_noti_type(eNotiType.RESTAT);
				FREEZEENG_ARMOR.set_spell_id(L1SkillId.FREEZEENG_ARMOR);
				FREEZEENG_ARMOR.set_duration(remaining_time);
				FREEZEENG_ARMOR.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				FREEZEENG_ARMOR.set_on_icon_id(9490);
				FREEZEENG_ARMOR.set_off_icon_id(9490);
				FREEZEENG_ARMOR.set_tooltip_str_id(5889);
				FREEZEENG_ARMOR.set_new_str_id(5889);
				FREEZEENG_ARMOR.set_is_good(true);
				pc.sendPackets(FREEZEENG_ARMOR, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case L1SkillId.ENCHANT_ACURUCY:
				pc.addHitup(-5);
				
				SC_SPELL_BUFF_NOTI ENCHANT_ACURUCY = SC_SPELL_BUFF_NOTI.newInstance();
				ENCHANT_ACURUCY.set_noti_type(eNotiType.RESTAT);
				ENCHANT_ACURUCY.set_spell_id(L1SkillId.ENCHANT_ACURUCY);
				ENCHANT_ACURUCY.set_duration(remaining_time);
				ENCHANT_ACURUCY.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				ENCHANT_ACURUCY.set_on_icon_id(9487);
				ENCHANT_ACURUCY.set_off_icon_id(9487);
				ENCHANT_ACURUCY.set_tooltip_str_id(5888);
				ENCHANT_ACURUCY.set_new_str_id(5888);
				ENCHANT_ACURUCY.set_is_good(true);
				pc.sendPackets(ENCHANT_ACURUCY, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case L1SkillId.CLEAR_MIND:
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				
				SC_SPELL_BUFF_NOTI CLEAR_MIND = SC_SPELL_BUFF_NOTI.newInstance();
				CLEAR_MIND.set_noti_type(eNotiType.RESTAT);
				CLEAR_MIND.set_spell_id(L1SkillId.CLEAR_MIND);
				CLEAR_MIND.set_duration(remaining_time);
				CLEAR_MIND.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				CLEAR_MIND.set_on_icon_id(745);
				CLEAR_MIND.set_off_icon_id(5279);
				CLEAR_MIND.set_tooltip_str_id(861);
				CLEAR_MIND.set_new_str_id(861);
				CLEAR_MIND.set_is_good(true);
				pc.sendPackets(CLEAR_MIND, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case L1SkillId.MAJESTY:
				SC_SPELL_BUFF_NOTI MAJESTY = SC_SPELL_BUFF_NOTI.newInstance();
				MAJESTY.set_noti_type(eNotiType.RESTAT);
				MAJESTY.set_spell_id(L1SkillId.MAJESTY);
				MAJESTY.set_duration(remaining_time);
				MAJESTY.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				MAJESTY.set_on_icon_id(9518);
				MAJESTY.set_off_icon_id(9518);
				MAJESTY.set_tooltip_str_id(5893);
				MAJESTY.set_new_str_id(5893);
				MAJESTY.set_is_good(true);
				pc.sendPackets(MAJESTY, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case L1SkillId.SHINING_ARMOR:
				pc.addEffectedER(10);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));

				SC_SPELL_BUFF_NOTI SHINING_ARMOR = SC_SPELL_BUFF_NOTI.newInstance();
				SHINING_ARMOR.set_noti_type(eNotiType.RESTAT);
				SHINING_ARMOR.set_spell_id(L1SkillId.SHINING_ARMOR);
				SHINING_ARMOR.set_duration(remaining_time);
				SHINING_ARMOR.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				SHINING_ARMOR.set_on_icon_id(9483);
				SHINING_ARMOR.set_off_icon_id(9483);
				SHINING_ARMOR.set_tooltip_str_id(5892);
				SHINING_ARMOR.set_new_str_id(5892);
				SHINING_ARMOR.set_is_good(true);
				pc.sendPackets(SHINING_ARMOR, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				break;
			case miso1: {					
					pc.sendPackets(S_InventoryIcon.icoNew(miso1, 4995, remaining_time, true));
				}
				break;
			case miso2: {					
					pc.addMaxHp(100);
					pc.getResistance().addMr(10);
					pc.addHpr(2);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(S_InventoryIcon.icoNew(miso2, 4996, remaining_time, true));
					
			}
			break;
			case miso3: {
					pc.addDmgup(3);
					pc.addBowDmgup(3);
					pc.addMaxMp(50);
					pc.getAbility().addSp(3);
					pc.addMpr(2);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(S_InventoryIcon.icoNew(miso3, 4997, remaining_time, true));					
			}
			break;
			default:
				L1SkillUse l1skilluse = new L1SkillUse();
				l1skilluse.handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time,
						L1SkillUse.TYPE_LOGIN);
				break;
			}
			pc.setSkillEffect(skillid, remaining_time * 1000);
		}
		pc.sendPackets(new S_UnityIcon(icon[0], icon[1], icon[2], icon[3], icon[4], icon[5], icon[6], icon[7], icon[8],
				icon[9], icon[10], icon[11], icon[12], icon[13], icon[14], icon[15], icon[16], icon[17], icon[18],
				icon[19], icon[20], icon[21], icon[22], icon[23], icon[24], icon[25], icon[26], icon[27], icon[28],
				icon[29], icon[30], icon[31], icon[32], icon[33], icon[34], icon[35], icon[36], icon[37], icon[38]));

	}

	private static void setExcludeList(L1PcInstance pc, L1ExcludingList exList) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_exclude WHERE char_id = ?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			while (rs.next()) {
				int type = rs.getInt("type");
				String name = rs.getString("exclude_name");
				if (!exList.contains(type, name)) {
					exList.add(type, name);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private static void grangKinAngerMessage(L1PcInstance pc) {
		if(pc.getAccount().getGrangKinAngerStat() != 0){
			int grangKinOneStep = GrangKinConfig.GRANG_KIN_ANGER_ONE_STEP_LOGIN_TIME;
			int grangKinTwoStep = GrangKinConfig.GRANG_KIN_ANGER_TWO_STEP_LOGIN_TIME;
			int grangKinThreeStep = GrangKinConfig.GRANG_KIN_ANGER_THREE_STEP_LOGIN_TIME;
			int grangKinFourStep = GrangKinConfig.GRANG_KIN_ANGER_FOUR_STEP_LOGIN_TIME;
			int grangKinFiveStep = GrangKinConfig.GRANG_KIN_ANGER_FIVE_STEP_LOGIN_TIME;
			int grangKinSixStep = GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_LOGIN_TIME;
			
			int real_time = 0;
			int minute = 0;
			int hour = 0;
			if(pc.getAccount().getGrangKinAngerStat() == 1){
				real_time = grangKinOneStep - pc.getGrangKinAngerSafeTime();
				hour = real_time >= 60 ? real_time / 60 : 0; 
				minute = grangKinOneStep % 60 - pc.getGrangKinAngerSafeTime();
			} else if(pc.getAccount().getGrangKinAngerStat() == 2){
				real_time = grangKinTwoStep - pc.getGrangKinAngerSafeTime();
				hour = real_time >= 60 ? real_time / 60 : 0; 
				minute = grangKinTwoStep % 60 - pc.getGrangKinAngerSafeTime();
			} else if(pc.getAccount().getGrangKinAngerStat() == 3){
				real_time = grangKinThreeStep - pc.getGrangKinAngerSafeTime();
				hour = real_time >= 60 ? real_time / 60 : 0; 
				minute = grangKinThreeStep % 60 - pc.getGrangKinAngerSafeTime();
			} else if(pc.getAccount().getGrangKinAngerStat() == 4){
				real_time = grangKinFourStep - pc.getGrangKinAngerSafeTime();
				hour = real_time >= 60 ? real_time / 60 : 0; 
				minute = grangKinFourStep % 60 - pc.getGrangKinAngerSafeTime();
			} else if(pc.getAccount().getGrangKinAngerStat() == 5){
				real_time = grangKinFiveStep - pc.getGrangKinAngerSafeTime();
				hour = real_time >= 60 ? real_time / 60 : 0; 
				minute = grangKinFiveStep % 60 - pc.getGrangKinAngerSafeTime();
			} else if(pc.getAccount().getGrangKinAngerStat() == 6){
				real_time = grangKinSixStep - pc.getGrangKinAngerSafeTime();
				hour = real_time >= 60 ? real_time / 60 : 0; 
				minute = grangKinSixStep % 60 - pc.getGrangKinAngerSafeTime();
			}
			
			pc.sendPackets("\\f3현재 계정에 그랑카인의 분노 단계는 "+pc.getAccount().getGrangKinAngerStat()+"단계 입니다.");
			pc.sendPackets("그랑카인의 분노 해제시 까지 마을대기 " + hour + "시간 " + minute + "분 남았습니다.");
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "현재 계정에 그랑카인의 분노 단계는 "+pc.getAccount().getGrangKinAngerStat()+"단계 입니다."));
		}
	}
	
	private static void grangKinAngerCheck(L1PcInstance pc) {
		int grang_kin_step = pc.getAccount().getGrangKinAngerStat();
		if(grang_kin_step != 0){
			boolean reset_ok = false;
//			System.out.println((System.currentTimeMillis() - pc.getAccount().getLastLogOut().getTime()) / 1000);
			long time = (System.currentTimeMillis() - pc.getAccount().getLastLogOut().getTime()) / 1000 / 60; // 60
			long calc_time = time / 2;
//			System.out.println("재로그인 그랑카인감소 수치  : " + time);
			if(pc.getAccount().getGrangKinAngerStat() == 1){
				if(time >= GrangKinConfig.GRANG_KIN_ANGER_ONE_STEP_LOGOUT_TIME){// 80
					pc.getAccount().setGrangKinAngerTime(0, pc);
					reset_ok = true;
				}
			} else if(pc.getAccount().getGrangKinAngerStat() == 2) {
				if(time >= GrangKinConfig.GRANG_KIN_ANGER_TWO_STEP_LOGOUT_TIME){
					pc.getAccount().setGrangKinAngerTime(0, pc);
				}
			} else if(pc.getAccount().getGrangKinAngerStat() == 3) {
				if(time >= GrangKinConfig.GRANG_KIN_ANGER_THREE_STEP_LOGOUT_TIME){
					pc.getAccount().setGrangKinAngerTime(0, pc);
					reset_ok = true;
				}
			} else if(pc.getAccount().getGrangKinAngerStat() == 4) {
				if(time >= GrangKinConfig.GRANG_KIN_ANGER_FOUR_STEP_LOGOUT_TIME){
					pc.getAccount().setGrangKinAngerTime(0, pc);
					reset_ok = true;
				}
			} else if(pc.getAccount().getGrangKinAngerStat() == 5) {
				if(time >= GrangKinConfig.GRANG_KIN_ANGER_FIVE_STEP_LOGOUT_TIME){
					pc.getAccount().setGrangKinAngerTime(0, pc);
					reset_ok = true;
				}
			} else if(pc.getAccount().getGrangKinAngerStat() == 6) {
				if(time >= GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_LOGOUT_TIME){
					pc.getAccount().setGrangKinAngerTime(0, pc);
					reset_ok = true;
				}
			}
			
			if(!reset_ok)
				pc.getAccount().addGrangKinAngerTime(-(int) calc_time, pc);
			
			int new_grang_kin_step = pc.getAccount().getGrangKinAngerStat();
			if(new_grang_kin_step != 0) {
				int step = 555 + new_grang_kin_step;
				pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, step, true));
			}
		}
	}
	
	public static void loadNBuff(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (Config.ACCOUNT_N_BUFF) {
				pstm = con.prepareStatement("SELECT * FROM character_tams WHERE skill_id BETWEEN ? AND ? AND account_id=?");
			} else {
				pstm = con.prepareStatement("SELECT * FROM character_tams WHERE skill_id BETWEEN ? AND ? AND char_id=?");
			}
			pstm.setInt(1, 4075);
			pstm.setInt(2, 4095);
			if (Config.ACCOUNT_N_BUFF) {
				pstm.setInt(3, pc.getAccount().getAccountId());
			} else {
				pstm.setInt(3, pc.getId());
			}
			rs = pstm.executeQuery();
			while (rs.next()) {
				int skillId = rs.getInt("skill_id");
				Timestamp expirationTime = rs.getTimestamp("expiration_time");
				if (expirationTime.getTime() <= System.currentTimeMillis()) {
					SQLUtil.execute("DELETE FROM character_tams WHERE char_id=? AND skill_id=?",
							new Object[] { Integer.valueOf(pc.getId()), Integer.valueOf(skillId) });
					continue;
				}

				if (pc.hasSkillEffect(skillId)) {
					continue;
				}
				int time = (int) (expirationTime.getTime() - System.currentTimeMillis()) / 1000;

				new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, time, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public String getType() {
		return C_LOGIN_TO_SERVER;
	}
}
