package l1j.server.server.model.Instance;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.ANTA_MESSAGE_6;
import static l1j.server.server.model.skill.L1SkillId.ANTA_MESSAGE_7;
import static l1j.server.server.model.skill.L1SkillId.ANTA_MESSAGE_8;
import static l1j.server.server.model.skill.L1SkillId.ANTA_SHOCKSTUN;
import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.DESPERADO;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.MOB_RANGESTUN_19;
import static l1j.server.server.model.skill.L1SkillId.MOB_SHOCKSTUN_30;
import static l1j.server.server.model.skill.L1SkillId.Maeno_STUN;
import static l1j.server.server.model.skill.L1SkillId.OMAN_STUN;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.Battle.Thebe.MJThebeCharacterInfo;
import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.L1DatabaseFactory;
import l1j.server.CharmSystem.CharmModelLoader;
import l1j.server.CharmSystem.Model.CharmSkillModel;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJCaptchaSystem.MJCaptcha;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJCombatSystem.MJCombatObserver;
import l1j.server.MJCombatSystem.Loader.MJCombatLoadManager;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.DungeonTimeUserInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJDungeonTimer.Progress.AccountTimeProgress;
import l1j.server.MJDungeonTimer.Progress.CharacterTimeProgress;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJExpAmpSystem.MJExpAmplifier;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJItemExChangeSystem.S_ItemExSelectPacket;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJLevelupBonus.MJLevelBonus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJPassiveSkill.MJPassiveInfo;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJEPcStatus;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CompanionT.eCommand;
import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceUserFlag;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.MJWarSystem.MJWarFactory.WAR_TYPE;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.RepeatTask;
import l1j.server.server.SkillCheck;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.Controller.GhostController;
import l1j.server.server.Controller.HpMpRegenController;
import l1j.server.server.Controller.LimitShopController.ShopLimitItem;
import l1j.server.server.command.executor.L1HpBar;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.datatables.CharacterQuestMonsterTable;
import l1j.server.server.datatables.CharacterQuestTable;
import l1j.server.server.datatables.CharacterSlotItemTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.CharactersGiftItemTable;
import l1j.server.server.datatables.ClanBuffTable;
import l1j.server.server.datatables.ClanBuffTable.ClanBuff;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.PlaySupportInfoTable;
import l1j.server.server.datatables.PlaySupportInfoTable.SupportInfo;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.AHRegeneration;
import l1j.server.server.model.Ability;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.HalloweenRegeneration;
import l1j.server.server.model.HelpBySupport;
import l1j.server.server.model.HpRegeneration;
import l1j.server.server.model.HpRegenerationByDoll;
import l1j.server.server.model.L1Astar;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1DeathMatch;
import l1j.server.server.model.L1DwarfForPackageInventory;
import l1j.server.server.model.L1EquipmentSlot;
import l1j.server.server.model.L1HateList;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Karma;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Node;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Racing;
import l1j.server.server.model.L1ReturnStatTemp;
import l1j.server.server.model.L1StatReset;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.MpRegeneration;
import l1j.server.server.model.MpRegenerationByDoll;
import l1j.server.server.model.PapuBlessing;
import l1j.server.server.model.ReportDeley;
import l1j.server.server.model.SHRegeneration;
import l1j.server.server.model.ValakasBlessing;
import l1j.server.server.model.classes.L1ClassFeature;
import l1j.server.server.model.gametime.L1GameTimeCarrier;
import l1j.server.server.model.monitor.L1PcAutoUpdate;
import l1j.server.server.model.monitor.L1PcExpMonitor;
import l1j.server.server.model.monitor.L1PcGhostMonitor;
import l1j.server.server.model.monitor.L1PcHellMonitor;
import l1j.server.server.model.monitor.L1PcInvisDelay;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.SkillData;
import l1j.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_BlueMessage;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharSpeedUpdate;
import l1j.server.server.serverpackets.S_CharStat;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanName;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Teleport;
import l1j.server.server.serverpackets.S_Weight;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.ServerMessage;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1QuestMonsterInfo;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.PerformanceTimer;
import l1j.server.server.utils.SQLUtil;

public class L1PcInstance extends L1Character {
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
	private long FishingShopBuyTime_1;
	
	public PerformanceTimer _PerformanceTimer = new PerformanceTimer();
	public PerformanceTimer _PerformanceTimer2 = new PerformanceTimer();
	
	protected CopyOnWriteArrayList<ShopLimitItem> limit_shop = new CopyOnWriteArrayList<ShopLimitItem>();
	public CopyOnWriteArrayList<ShopLimitItem> getLimitShop(){
		return limit_shop;
	}
	public boolean _CubeEffect = false;
	public boolean _Blessleaf = false;
	public boolean _EnchantWeaponSuccess = false;
	public boolean _EnchantArmorSuccess = false;

	public boolean _ErzabeBox = false;
	public boolean _SandwormBox = false;

	public int 상인찾기Objid = 0;
	public boolean war_zone = false;

	public boolean _isPacketAttack = false;
	public ArrayList<Integer> _자동버프리스트;
	public ArrayList<Integer> _자동루팅리스트;
	public ArrayList<Integer> _자동용해리스트;
	public List<L1ItemInstance> _자동물약초이스리스트;
	public ArrayList<Integer> _자동물약리스트;
	private boolean _자동버프세이프티존사용;
	private boolean _자동버프전투시사용;
	private boolean _자동버프사용;
	private boolean _자동물약사용;
	private int _자동물약퍼센트;
	private boolean _자동숫돌사용;	
	
	public boolean getIsPacketAttack() {
		return _isPacketAttack;
	}

	public void setIsPacketAttack(boolean flag) {
		_isPacketAttack = flag;
	}

	public int getNcoin() {
		if (getAccount() != null) {
			return getNetConnection().getAccount().Ncoin_point;
		}
		return 0;
	}
    
	// 시세 명령어 관련
	private boolean _ismarket;

	public boolean isMarket() {
		return _ismarket;
	}

	public void setMarket(boolean flag) {
		this._ismarket = flag;
	}

	private boolean _isSM;

	public boolean isSM() {
		return _isSM;
	}

	public void setSM(boolean flag) {
		this._isSM = flag;
	}

	// 시세 명령어 관련
	private int _returnstatus;

	public synchronized int getReturnStatus() {
		return _returnstatus;
	}

	public synchronized void setReturnStatus(int i) {
		_returnstatus = i;
	}

	private L1StatReset _statReset;

	public void setStatReset(L1StatReset sr) {
		_statReset = sr;
	}

	public L1StatReset getStatReset() {
		return _statReset;
	}

	public L1ReturnStatTemp rst = null;

	public void addNcoin(int coin) {
		if (getNetConnection() != null) {
			if (getNetConnection().getAccount() != null) {
				getNetConnection().getAccount().Ncoin_point += coin;
				getNetConnection().getAccount().updateNcoin();
				// System.out.println(getNetConnection().getAccount().Ncoin_point);
			}
		} else {
			// System.out.println("무인에 걸린다.");
		}
	}

	public void addNcoin1(int coin) {
		if (getNetConnection() != null) {
			if (getNetConnection().getAccount() != null) {
				getNetConnection().getAccount().Ncoin_point -= coin;
				getNetConnection().getAccount().updateNcoin();
				// System.out.println(getNetConnection().getAccount().Ncoin_point);
			}
		}
	}

	private int boss_spawn_yn;

	public int getBossYN() {
		return boss_spawn_yn;
	}

	public void setBossYN(int i) {
		boss_spawn_yn = i;
	}

	private boolean specialBuff = false;

	public void setSpecialBuff(boolean flag) {
		this.specialBuff = flag;
	}

	public boolean isSpecialBuff() {
		return this.specialBuff;
	}

	private ReportDeley _reportdeley; // /신고 추가

	public void startReportDeley() { // /신고 추가
		_reportdeley = new ReportDeley(this);
		_regenTimer.schedule(_reportdeley, 100000); // 딜레이 시간 10분
	}

	// /신고 추가
	private boolean _isReport = true;

	public void setReport(boolean _isreport) {
		_isReport = _isreport;
	}

	public boolean isReport() {
		return _isReport;
	}

	private static final long serialVersionUID = 1L;

	public static final int CLASSID_PRINCE = 0;
	public static final int CLASSID_PRINCESS = 1;
	public static final int CLASSID_KNIGHT_MALE = 61;
	public static final int CLASSID_KNIGHT_FEMALE = 48;
	public static final int CLASSID_ELF_MALE = 138;
	public static final int CLASSID_ELF_FEMALE = 37;
	public static final int CLASSID_WIZARD_MALE = 734;
	public static final int CLASSID_WIZARD_FEMALE = 1186;
	public static final int CLASSID_DARK_ELF_MALE = 2786;
	public static final int CLASSID_DARK_ELF_FEMALE = 2796;
	public static final int CLASSID_DRAGONKNIGHT_MALE = 6658;
	public static final int CLASSID_DRAGONKNIGHT_FEMALE = 6661;
	public static final int CLASSID_BLACKWIZARD_MALE = 6671;
	public static final int CLASSID_BLACKWIZARD_FEMALE = 6650;
	public static final int CLASSID_전사_MALE = 12490;
	public static final int CLASSID_전사_FEMALE = 12494;

	public static final int REGENSTATE_NONE = 4;
	public static final int REGENSTATE_MOVE = 2;
	public static final int REGENSTATE_ATTACK = 2;// 원본1

	public long tamtime = 0;

	// 콤보시스템
	public int getComboCount() {
		return this.comboCount;
	}

	public void setComboCount(int comboCount) {
		this.comboCount = comboCount;
	}

	public boolean PC방_버프 = false;
	public boolean PC방_버프삭제중 = false;

	public void setPC방_버프(boolean b) {
		PC방_버프 = b;
		SC_ATTENDANCE_USER_DATA_EXTEND userData = getAttendanceData();
		if (userData != null) {
			AttendanceUserFlag flag = b ? AttendanceUserFlag.USER_FLAG_PC_CAFE : AttendanceUserFlag.USER_FLAG_NORMAL;
			if (!userData.get_userFlag().equals(flag)) {
				userData.set_userFlag(flag);
				SC_ATTENDANCE_USER_DATA_EXTEND.send(this);
			}
		}
		SC_REST_EXP_INFO_NOTI.send(this);
	}

	private boolean isSafetyZone;

	public boolean getSafetyZone() {
		return isSafetyZone;
	}

	public void setSafetyZone(boolean value) {
		if (isSafetyZone == value)
			return;
		isSafetyZone = value;
		if (hasSkillEffect(EXP_POTION)) {
			int time = getSkillEffectTimeSec(EXP_POTION);
			if (isSafetyZone) {
				sendPackets(S_InventoryIcon.icoEnd(EXP_POTION));
				sendPackets(S_InventoryIcon.iconNewUnLimit(EXP_POTION + 1, 4973, true)); // 세이프티존
			} else {
				sendPackets(S_InventoryIcon.icoEnd(EXP_POTION + 1));
				sendPackets(S_InventoryIcon.icoNew(EXP_POTION, 4973, time, true)); // 사냥터
			}
		}
	}

	private int noDelayTime = 0;

	public int getNoDelayTime() {
		return noDelayTime;
	}

	public void setNoDelayTime(int noDelayTime) {
		this.noDelayTime = noDelayTime;
	}

	private int _speedhackCount = 0;

	public int getSpeedHackCount() {
		return _speedhackCount;
	}

	public void setSpeedHackCount(int x) {
		_speedhackCount = x;
	}

	/** 레이드 Y_N **/
	private boolean _raid = false;

	public void setRaidGame(boolean flag) {
		this._raid = flag;
	}

	public boolean getRaidGame() {
		return _raid;
	}

	private boolean _Mayo = false;

	public void setMayo(boolean flag) {
		this._Mayo = flag;
	}

	public boolean getMayo() {
		return _Mayo;
	}

	private boolean _Necross = false;

	public void setNecross(boolean flag) {
		this._Necross = flag;
	}

	public boolean getNecross() {
		return _Necross;
	}

	private boolean _Tebeboss = false;

	public void setTebeboss(boolean flag) {
		this._Tebeboss = flag;
	}

	public boolean getTebeboss() {
		return _Tebeboss;
	}

	private boolean _Curch = false;

	public void setCurch(boolean flag) {
		this._Curch = flag;
	}

	public boolean getCurch() {
		return _Curch;
	}

	private boolean _dtah = false;

	public void setDeat(boolean flag) {
		this._dtah = flag;
	}

	public boolean getDeat() {
		return _dtah;
	}

	private boolean _trac = false;

	public void setTrac(boolean flag) {
		this._trac = flag;
	}

	public boolean getTrac() {
		return _trac;
	}

	private boolean _girtas = false;

	public void setGirtas(boolean flag) {
		this._girtas = flag;
	}

	public boolean getGirtas() {
		return _girtas;
	}

	private boolean _orim = false;

	public void setOrim(boolean flag) {
		this._orim = flag;
	}

	public boolean getOrim() {
		return _orim;
	}

	private boolean _erzarbe = false;

	public void setErzarbe(boolean flag) {
		this._erzarbe = flag;
	}

	public boolean getErzarbe() {
		return _erzarbe;
	}

	private boolean _Hondon = false;

	public void setHondon(boolean flag) {
		this._Hondon = flag;
	}

	public boolean getHondon() {
		return _Hondon;
	}

	private boolean _Reper = false;

	public void setReper(boolean flag) {
		this._Reper = flag;
	}

	public boolean getReper() {
		return _Reper;
	}

	private boolean _Rekt = false;

	public void setRekt(boolean flag) {
		this._Rekt = flag;
	}

	public boolean getRekt() {
		return _Rekt;
	}

	private boolean _Rekt1 = false;

	public void setRekt1(boolean flag) {
		this._Rekt1 = flag;
	}

	public boolean getRekt1() {
		return _Rekt1;
	}

	private boolean _Rekt2 = false;

	public void setRekt2(boolean flag) {
		this._Rekt2 = flag;
	}

	public boolean getRekt2() {
		return _Rekt2;
	}

	public boolean getgarmf() {
		return _garmf;
	}

	private boolean _garmf = false;

	public void setgarmf(boolean flag) {
		this._garmf = flag;
	}

	public boolean getTaros() {
		return _Taros;
	}

	private boolean _Taros = false;

	public void setTaros(boolean flag) {
		this._Taros = flag;
	}

	public boolean getCrock() {
		return _Crock;
	}

	private boolean _Crock = false;

	public void setCrock(boolean flag) {
		this._Crock = flag;
	}

	public boolean getCrock1() {
		return _Crock1;
	}

	private boolean _Crock1 = false;

	public void setCrock1(boolean flag) {
		this._Crock1 = flag;
	}

	public boolean getCrock2() {
		return _Crock2;
	}

	private boolean _Crock2 = false;

	public void setCrock2(boolean flag) {
		this._Crock2 = flag;
	}

	// 계정 정로 로드시 필요..
	public Account getAccount() {
		if (_netConnection == null)
			return null;

		return this._netConnection.getAccount();
	}

	/** 포우 **/
	public boolean FouSlayer = false;
	public boolean TripleArrow = false;
	// 문장주시 변수
	public boolean 문장주시 = false;

	public int _x;

	// 성장의 낚시관련
	private L1ItemInstance _fishingitem;

	public L1ItemInstance getFishingItem() {
		return _fishingitem;
	}

	public void setFishingItem(L1ItemInstance item) {
		_fishingitem = item;
	}

	private boolean _Attacklog = false;

	public void setAttackLog(boolean i) {
		this._Attacklog = i;
	}

	public boolean getAttackLog() {
		return this._Attacklog;
	}

	/** AttackController **/
	public long AttackControllerTime = 0;
	/** AttackController **/
	/** SPR체크 **/
	public int AttackSpeedCheck2 = 0;
	public int MoveSpeedCheck = 0;
	public int magicSpeedCheck = 0;
	public long AttackSpeed2;
	public long MoveSpeed;
	public long magicSpeed;
	/** SPR체크 **/

	public int dx = 0;
	public int dy = 0;
	public short dm = 0;
	public int dh = 0;
	public int 상점변신 = 0;

	// 수배
	public static final String WANTED_TITLE = "\\aG【수배】";

	public void doWanted(boolean isOn) {
		int mul = isOn ? 1 : -1;
		addDmgRate(3 * mul); // 근거리 대미지 +3
		addHitup(3 * mul); // 근거리 명중 +3
		addBowDmgup(3 * mul); // 원거리 +3
		addBowHitup(3 * mul); // 원거리 명중 +3
		getAbility().addSp(3 * mul); // SP +3
		addDamageReductionByArmor(3 * mul); // 리덕션 +3
		getAC().addAc(-3 * mul); // AC -3
		sendPackets(new S_SPMR(this));
		sendPackets(new S_OwnCharAttrDef(this));
		sendPackets(new S_OwnCharStatus2(this));
		sendPackets(new S_OwnCharStatus(this));
		S_CharTitle pck = new S_CharTitle(getId(), WANTED_TITLE);
		sendPackets(pck, false);
		Broadcaster.broadcastPacket(this, pck);
	}

	/** 캐릭별 추가데미지, 추가리덕션, 확률 **/
	private int _AddDamage = 0;
	private int _AddDamageRate = 0;
	private int _AddReduction = 0;
	private int _AddReductionRate = 0;

	public int getAddDamage() {
		return _AddDamage;
	}

	public void setAddDamage(int addDamage) {
		_AddDamage = addDamage;
	}

	public int getAddDamageRate() {
		return _AddDamageRate;
	}

	public void setAddDamageRate(int addDamageRate) {
		_AddDamageRate = addDamageRate;
	}

	public int getAddReduction() {
		return _AddReduction;
	}

	public void setAddReduction(int addReduction) {
		_AddReduction = addReduction;
	}

	public int getAddReductionRate() {
		return _AddReductionRate;
	}

	public void setAddReductionRate(int addReductionRate) {
		_AddReductionRate = addReductionRate;
	}

	/** 캐릭별 추가데미지, 추가리덕션, 확률 **/

	private int _ubscore;

	public int getUbScore() {
		return _ubscore;
	}

	public void setUbScore(int i) {
		_ubscore = i;
	}

	public byte[] 페어리정보 = new byte[512];

	public void 페어리경험치보상(int lv) {
		int needExp = ExpTable.getNeedExpNextLevel(lv);
		int addexp = 0;
		addexp = (int) (needExp * 0.01);
		if (addexp != 0) {
			int level = ExpTable.getLevelByExp(getExp() + addexp);
			if (level > 60) {
				sendPackets(new S_SystemMessage("더이상 경험치를 획득 할 수 없습니다."));
			} else {
				addExp(addexp);
			}
		}
	}

	public void 페어리정보저장(int id) {
		int count = fairlycount(getId());
		페어리정보[id] = 1;
		if (count == 0) {
			fairlystore(getId(), 페어리정보);
		} else {

			fairlupdate(getId(), 페어리정보);
		}
	}

	/** 경험치 pc.경험치보상(pc, 52, 0.04); / 이런식으로 넣으면 52렙 기준 4프로 **/
	public void 경험치보상(L1PcInstance pc, int level, double rate) {// 유용히 사용
		int needExp = ExpTable.getNeedExpNextLevel(level);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());

		int exp = 0;
		exp = (int) (needExp * rate * exppenalty);

		pc.addExp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	public int fairlycount(int objectId) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(*) as cnt FROM character_Fairly_Config WHERE object_id=?");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public void fairlystore(int objectId, byte[] data) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_Fairly_Config SET object_id=?, data=?");
			pstm.setInt(1, objectId);
			pstm.setBytes(2, data);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void fairlupdate(int objectId, byte[] data) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_Fairly_Config SET data=? WHERE object_id=?");
			pstm.setBytes(1, data);
			pstm.setInt(2, objectId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/** 브레이브 아바타 **/
	private boolean _Pbavatar = false;

	public boolean getPbavatar() {
		return _Pbavatar;
	}

	public void setPbavatar(boolean Pbavatar) {
		_Pbavatar = Pbavatar;
	}

	private boolean _Pbavataron = false;

	public boolean getPbavataron() {
		return _Pbavataron;
	}

	public void setPbavataron(boolean Pbavataron) {
		_Pbavataron = Pbavataron;
	}

	private int _Pbacount = 0;

	public int getPbacount() {
		return _Pbacount;
	}

	public void setPbacount(int i) {
		_Pbacount = i;
	}

	/** 브레이브아바타 **/

	public L1ItemInstance _fishingRod = null;

	private static Random _random = new Random(System.nanoTime());

	private L1ClassFeature _classFeature = null;
	private L1EquipmentSlot _equipSlot;
	private String _accountName;
	private int _classId;
	private int _type;
	private int _exp;
	private int _age;
	/** 나이설정 **/
	private short _accessLevel;

	private short _baseMaxHp = 0;
	private int _baseMaxMp = 0;
	private int _baseAc = 0;
	private int _originalMagicHit = 0;
	private int _baseBowDmgup = 0;
	private int _baseDmgup = 0;
	private int _baseHitup = 0;
	private int _baseBowHitup = 0;

	private int _baseMagicHitup = 0; // 베이스 스탯에 의한 마법 명중
	private int _baseMagicCritical = 0; // 베이스 스탯에 의한 마법 치명타(%)
	private int _baseMagicDmg = 0; // 베이스 스탯에 의한 마법 대미지
	private int _baseMagicDecreaseMp = 0; // 베이스 스탯에 의한 마법 대미지

	private int _DmgupByArmor = 0; // 방어용 기구에 의한 근접무기 추타율
	private int _bowDmgupByArmor = 0; // 방어용 기구에 의한 활의 추타율

	private int _PKcount;
	public int _fishingX = 0;
	public int _fishingY = 0;
	private int _clanid;
	private int _redKnightClanId;
	private int _redKnightDamage;
	private int _redKnightKill;
	private String clanname;
	private int _clanRank;
	private byte _sex;
	private int _returnstat;
	private short _hpr = 0;
	private short _trueHpr = 0;
	private short _mpr = 0;
	private short _trueMpr = 0;

	private int _advenHp;
	private int _advenMp;
	private int _magicBuffHp;
	private int _highLevel;
	private int _bonusStats;
	public boolean isInFantasy = false; // 몽섬리뉴얼

	/** 화룡의 안식처 **/
	public boolean isInValakasBoss = false;
	public boolean isInValakas = false;
	private boolean _ghost = false;
	private boolean _ghostCanTalk = true;
	private boolean _isReserveGhost = false;
	private boolean _isShowTradeChat = true;
	private boolean _isCanWhisper = true;
	private boolean _isFishing = false;
	private boolean _isFishingReady = false;
	private boolean isDeathMatch = false; // 데스매치
	private boolean _isSupporting = false;
	private boolean _isShowWorldChat = true;
	private boolean _gm;
	private boolean _monitor;
	private boolean _gmInvis;
	// private boolean _isTeleport = false;
	private boolean _isDrink = false;
	private boolean _isGres = false;
	private boolean _isPinkName = false;
	private boolean _isAutofish = false;
	private boolean _banned;
	private boolean _isAutoClanjoin = false;
	private boolean _gresValid;
	private boolean _tradeOk;
	// private boolean _mpRegenActive;
	private boolean _mpRegenActiveByDoll;
	private boolean _mpDecreaseActiveByScales;
	private boolean _HelpActiveBySupport;
	// private boolean _hpRegenActive;
	private boolean _AHRegenActive;
	private boolean _SHRegenActive;
	private boolean _HalloweenRegenActive;
	private boolean _hpRegenActiveByDoll;

	public boolean RootMent = true;// 루팅 멘트]

	public boolean noPlayerck2 = false;
	public boolean noPlayerCK = false;
	public boolean noPlayerRobot = false;

	private int invisDelayCounter = 0;
	private Object _invisTimerMonitor = new Object();

	private int _ghostSaveLocX = 0;
	private int _ghostSaveLocY = 0;
	private short _ghostSaveMapId = 0;
	// private int _ghostSaveHeading = 0;
	public byte _ghostCount = 0;
	public long ghosttime = 0;

	private ScheduledFuture<?> _ghostFuture;
	private ScheduledFuture<?> _hellFuture;
	private ScheduledFuture<?> _autoUpdateFuture;
	private ScheduledFuture<?> _expMonitorFuture;

	private Timestamp _lastPk;
	private Timestamp _deleteTime;
	private Timestamp _lastLoginTime;
	private int _einhasad;

	private int _weightReduction = 0;
	private int _hasteItemEquipped = 0;
	private int _damageReductionByArmor = 0;
	private int _damageReductionIgnore = 0;
	private int _DamageReduction = 0;
	private int _DmgRate = 0; // 방어용 기구에 의한 근접무기 추타율
	private int _HitRate = 0; // 방어용 기구에 의한 근접무기 명중율
	private int _bowHitRate = 0; // 방어용 기구에 의한 활의 명중율
	private int _bowDmgRate = 0; // 방어용 기구에 의한 활의 추타율

	// private int _teleportY = 0;
	// private int _teleportX = 0;
	// private short _teleportMapId = 0;
	// private int _teleportHeading = 0;

	private int _tempCharGfxAtDead;
	private int _fightId;
	private byte _chatCount = 0;
	private long _oldChatTimeInMillis = 0L;

	private int _elixirStats;
	private int _elfAttr;
	private int _expRes;

	private int _onlineStatus;
	private int _homeTownId;
	private int _contribution;
	private int _food;
	private int _hellTime;
	private int _partnerId;
	private long _fishingTime = 0;
	private int _dessertId = 0;
	private int _callClanId;
	private int _callClanHeading;

	private int _currentWeapon; // 로봇 관련
	private final L1Karma _karma = new L1Karma();
	private final L1PcInventory _inventory;
	private final L1DwarfForPackageInventory _dwarfForPackage;
	private final L1Inventory _tradewindow;

	private L1ItemInstance _weapon;
	private L1ItemInstance _secondweapon;
	private L1ItemInstance _armor;
	private L1Party _party;
	private L1ChatParty _chatParty;

	private int _cookingId = 0;
	private int _partyID;
	private int _partyType;
	private int _tradeID;
	private int _tempID;

	private L1Quest _quest;
	private HelpBySupport _HelpBySupport;

	private HpRegeneration _hpRegen;
	private MpRegeneration _mpRegen;
	private HpRegenerationByDoll _hpRegenByDoll;
	private MpRegenerationByDoll _mpRegenByDoll;
	// private MpDecreaseByScales _mpDecreaseByScales;
	private AHRegeneration _AHRegen;
	private SHRegeneration _SHRegen;
	private HalloweenRegeneration _HalloweenRegen;

	// //-- 수정 [ 추가된 소스 차단 ]
	// private final L1ExcludingList _excludingList = new L1ExcludingList();
	//
	// public L1ExcludingList getExcludingList() {
	// return _excludingList;
	// }

	// 피엠틱컨트롤러
	private HpMpRegenController _hpmpRegen;
	private boolean _hpmpRegenActive;

	public void setHpMpRegenActive(boolean flag) {
		_hpmpRegenActive = flag;
	}

	private static Timer _regenTimer = new Timer(true);

	private boolean _isPrivateShop = false;
	private boolean _isPrivateReady = false;
	private int _partnersPrivateShopItemCount = 0;

	private long _lastPasswordChangeTime;
	private long _lastLocalTellTime;

	private boolean 무인상점 = false;

	boolean isExpDrop;
	boolean isItemDrop;

	public final ArrayList<L1BookMark> _speedbookmarks;

	public L1BookMark[] getBookMarkArray() {
		return _bookmarks.toArray(new L1BookMark[_bookmarks.size()]);
	}

	public L1BookMark[] getSpeedBookMarkArray() {
		return _speedbookmarks.toArray(new L1BookMark[_speedbookmarks.size()]);
	}

	private int _markcount;

	public void setMark_count(int i) {
		_markcount = i;
	}

	public int getMark_count() {
		return _markcount;
	}

	public boolean is무인상점() {
		return 무인상점;
	}

	public void set무인상점(boolean c) {
		무인상점 = c;
	}

	/** 혈맹버프 **/
	private boolean _clanbuff = false;

	public boolean isClanBuff() {
		return _clanbuff;
	}

	public void setClanBuff(boolean c) {
		_clanbuff = c;
	}

	/** 혈맹버프 **/

	// 생존의외침
	public int _getLive = 0;

	public int getLive() {
		return _getLive;
	}

	public void addLive(int Live) {
		_getLive += Live;
	}

	public void setLive(int Live) {
		_getLive = Live;
	}

	// 조우의 불골렘
	public int[] FireGolem = new int[18];
	public int[] FireEnchant = new int[18];

	// 피어스
	public int[] PiersItemId = new int[19];
	public int[] PiersEnchant = new int[19];

	private boolean _텔대기 = false;
	private boolean _isHomnam = false;
	private boolean _isBosMon = false;
	private boolean _isBosMon1 = false;
	private boolean _isElrzabe = false;
	private boolean _isSandWarm = false;
	private boolean _is드레이크 = false;
	private boolean _is제로스 = false;
	private boolean _is기르타스 = false;
	private boolean _is대왕오징어 = false;
	private boolean _is발라카스 = false;
	private boolean _is파푸리온 = false;
	private boolean _is린드비오르 = false;
	private boolean _is안타라스 = false;
	private boolean _isNCoinMon = false;
	private boolean _isSpecialMap = false;
	// private boolean _isNcoin300 = false;
	// private boolean _isNcoin500 = false;
	// private boolean _isNcoin3000 = false;

	private boolean _magicitem = false;
	private int _magicitemid;
	// 3.63아이템패킷추가
	public boolean isWorld = false;
	// 3.63아이템패킷추가
	public boolean isDanteasBuff = false;

	public boolean isGotobokBuff = false;

	public boolean 서버다운중 = false;
	public boolean 바포방 = false;
	// 젠도르 제작 관련
	public int _getCount;

	private long _npcaction;

	public final ArrayList<L1BookMark> _bookmarks;
	private byte[] _shopChat;
	private AtomicInteger _pinkNameTime;
	private GameClient _netConnection;
	private static Logger _log = Logger.getLogger(L1PcInstance.class.getName());
	private final SkillData skill_data;

	public L1PcInstance() {
		_자동버프리스트 = new ArrayList<Integer>();
		_자동루팅리스트 = new ArrayList<Integer>();
		_자동용해리스트 = new ArrayList<Integer>();
		_자동물약초이스리스트 = new ArrayList<L1ItemInstance>();
		_자동물약리스트 = new ArrayList<Integer>();
		_자동물약퍼센트 = 30;
		_accessLevel = 0;
		_currentWeapon = 0;
		_inventory = new L1PcInventory(this);
		_dwarfForPackage = new L1DwarfForPackageInventory(this);
		_tradewindow = new L1Inventory();
		_bookmarks = new ArrayList<L1BookMark>();
		_speedbookmarks = new ArrayList<L1BookMark>();
		_quest = new L1Quest(this);
		_equipSlot = new L1EquipmentSlot(this);
		_pinkNameTime = new AtomicInteger(0);
		skill_data = new SkillData(this);
	}	
	public long get_lastPasswordChangeTime() {
		return _lastPasswordChangeTime;
	}

	public long get_lastLocalTellTime() {
		return _lastLocalTellTime;
	}

	public void update_lastPasswordChangeTime() {
		_lastPasswordChangeTime = System.currentTimeMillis();
	}

	public void update_lastLocalTellTime() {
		_lastLocalTellTime = System.currentTimeMillis();
	}

	private long _lastShellUseTime;

	public long getlastShellUseTime() {
		return _lastShellUseTime;
	}

	public void updatelastShellUseTime() {
		_lastShellUseTime = System.currentTimeMillis();
	}

	public int getPinkNameTime() {
		return _pinkNameTime.get();
	}

	public int DecrementPinkNameTime() {
		return _pinkNameTime.decrementAndGet();
	}

	public int SetPinkNameTime(int timeValue) {
		return _pinkNameTime.getAndSet(timeValue);
	}

	public void setSkillMastery(int skillid) {
		if (!skillList.contains(skillid)) {
			skillList.add(skillid);
		}
	}

	public void removeSkillMastery(int skillid) {
		if (skillList.contains((Object) skillid)) {
			skillList.remove((Object) skillid);
		}
	}

	public boolean isSkillMastery(int skillid) {
		return skillList.contains(skillid);
	}

	public short getHpr() {
		return _hpr;
	}

	public void addHpr(int i) {
		_trueHpr += i;
		_hpr = (short) Math.max(0, _trueHpr);
	}

	public short getMpr() {
		return _mpr;
	}

	public void addMpr(int i) {
		_trueMpr += i;
		_mpr = (short) Math.max(0, _trueMpr);
	}

	public void setHomnam(boolean flag) {
		_isHomnam = flag;
	}

	public boolean isHomnam() {
		return _isHomnam;
	}

	public void setBosMon(boolean flag) {
		_isBosMon = flag;
	}

	public boolean isBosMon() {
		return _isBosMon;
	}

	public void setBosMon1(boolean flag) {
		_isBosMon1 = flag;
	}

	public boolean isBosMon1() {
		return _isBosMon1;
	}

	public void setElrzabe(boolean flag) {
		_isElrzabe = flag;
	}

	public boolean isElrzabe() {
		return _isElrzabe;
	}

	public void setSandWarm(boolean flag) {
		_isSandWarm = flag;
	}

	public boolean isSandWarm() {
		return _isSandWarm;
	}

	public void set드레이크(boolean flag) {
		_is드레이크 = flag;
	}

	public boolean is드레이크() {
		return _is드레이크;
	}

	public void set제로스(boolean flag) {
		_is제로스 = flag;
	}

	public boolean is제로스() {
		return _is제로스;
	}

	public boolean isSpecialMap() {
		return _isSpecialMap;
	}

	public void setSpecialMap(boolean flag) {
		_isSpecialMap = flag;
	}

	public boolean isNCoinMon() {
		return _isNCoinMon;
	}

	public void setNCoinMon(boolean flag) {
		_isNCoinMon = flag;
	}
	// public void setNcoin300(boolean flag) {
	// _isNcoin300 = flag;
	// }
	//
	// public boolean isNcoin300() {
	// return _isNcoin300;
	// }
	//
	// public void setNcoin500(boolean flag) {
	// _isNcoin500 = flag;
	// }
	//
	// public boolean isNcoin500() {
	// return _isNcoin500;
	// }
	//
	// public void setNcoin3000(boolean flag) {
	// _isNcoin3000 = flag;
	// }
	//
	// public boolean isNcoin3000() {
	// return _isNcoin3000;
	// }

	public void set기르타스(boolean flag) {
		_is기르타스 = flag;
	}

	public boolean is기르타스() {
		return _is기르타스;
	}

	public void set대왕오징어(boolean flag) {
		_is대왕오징어 = flag;
	}

	public boolean is대왕오징어() {
		return _is대왕오징어;
	}

	public void set발라카스(boolean flag) {
		_is발라카스 = flag;
	}

	public boolean is발라카스() {
		return _is발라카스;
	}

	public void set파푸리온(boolean flag) {
		_is파푸리온 = flag;
	}

	public boolean is파푸리온() {
		return _is파푸리온;
	}

	public void set린드비오르(boolean flag) {
		_is린드비오르 = flag;
	}

	public boolean is린드비오르() {
		return _is린드비오르;
	}

	public void set안타라스(boolean flag) {
		_is안타라스 = flag;
	}

	public boolean is안타라스() {
		return _is안타라스;
	}

	public long getNpcActionTime() {
		return _npcaction;
	}

	public void setNpcActionTime(long flag) {
		_npcaction = flag;
	}

	public boolean isMagicItem() {
		return _magicitem;
	}

	public void setMagicItem(boolean flag) {
		_magicitem = flag;
	}

	public int getMagicItemId() {
		return _magicitemid;
	}

	public void setMagicItemId(int itemid) {
		_magicitemid = itemid;
	}

	private PapuBlessing _PapuRegen;
	private boolean _PapuBlessingActive;

	public void startPapuBlessing() {// 파푸가호
		final int RegenTime = 120000;
		if (!_PapuBlessingActive) {
			_PapuRegen = new PapuBlessing(this);
			_PapuBlessingActive = true;
			GeneralThreadPool.getInstance().schedule(_PapuRegen, RegenTime);
		}
	}

	// public void startHpRegeneration() {
	// final int INTERVAL = 1000;
	//
	// if (!_hpRegenActive) {
	// _hpRegen = new HpRegeneration(this, INTERVAL);
	// GeneralThreadPool.getInstance().schedule(_hpRegen, INTERVAL);
	// _hpRegenActive = true;
	// }
	// }
	// public void stopHpRegeneration() {
	// if (_hpRegenActive) {
	// _hpRegen.cancel();
	// _hpRegen = null;
	// _hpRegenActive = false;
	// }
	// }
	// public void startMpRegeneration() {
	// final int INTERVAL = 1000;
	//
	// if (!_mpRegenActive) {
	// _mpRegen = new MpRegeneration(this, INTERVAL);
	// GeneralThreadPool.getInstance().schedule(_mpRegen, INTERVAL);
	// _mpRegenActive = true;
	// }
	// }
	// public void stopMpRegeneration() {
	// if (_mpRegenActive) {
	// _mpRegen.cancel();
	// _mpRegen = null;
	// _mpRegenActive = false;
	// }
	// }

	/** 피엠틱 컨트롤러 **/
	public void startHpMpRegeneration() {
		if (!_hpmpRegenActive) {
			_hpmpRegen = new HpMpRegenController(this);
			_regenTimer.scheduleAtFixedRate(_hpmpRegen, 1000, 1000);
			_hpmpRegenActive = true;
		}
	}

	public void stopHpMpRegeneration() {
		if (_hpmpRegenActive) {
			_hpmpRegen.cancel();
			_hpmpRegen = null;
			_hpmpRegenActive = false;
		}
	}

	/** 피엠틱 컨트롤러 **/

	public void startHpRegenerationByDoll() {
		final int INTERVAL_BY_DOLL = 32000;
		boolean isExistHprDoll = false;
		L1DollInstance doll = null;
		Object[] dollList = getDollList().values().toArray();

		for (Object dollObject : dollList) {
			doll = (L1DollInstance) dollObject;
			if (doll.isHpRegeneration()) {
				isExistHprDoll = true;
			}
		}
		if (!_hpRegenActiveByDoll && isExistHprDoll) {
			_hpRegenByDoll = new HpRegenerationByDoll(this, INTERVAL_BY_DOLL);
			GeneralThreadPool.getInstance().schedule(_hpRegenByDoll, INTERVAL_BY_DOLL);
			_hpRegenActiveByDoll = true;
		}
	}

	public void startAHRegeneration() {
		final int INTERVAL = 600000;
		if (!_AHRegenActive) {
			_AHRegen = new AHRegeneration(this, INTERVAL);
			GeneralThreadPool.getInstance().schedule(_AHRegen, INTERVAL);
			_AHRegenActive = true;
		}
	}

	public void startSHRegeneration() {
		final int INTERVAL = 1800000;
		if (!_SHRegenActive) {
			_SHRegen = new SHRegeneration(this, INTERVAL);
			GeneralThreadPool.getInstance().schedule(_SHRegen, INTERVAL);
			_SHRegenActive = true;
		}
	}

	public void startHalloweenRegeneration() {
		final int INTERVAL = 900000;
		if (!_HalloweenRegenActive) {
			_HalloweenRegen = new HalloweenRegeneration(this, INTERVAL);
			GeneralThreadPool.getInstance().schedule(_HalloweenRegen, INTERVAL);
			_HalloweenRegenActive = true;
		}
	}

	public void stopPapuBlessing() { // 파푸가호
		if (_PapuBlessingActive) {
			_PapuRegen.cancel();
			_PapuRegen = null;
			_PapuBlessingActive = false;
		}
	}

	public void stopHpRegenerationByDoll() {
		if (_hpRegenActiveByDoll) {
			_hpRegenByDoll.cancel();
			_hpRegenByDoll = null;
			_hpRegenActiveByDoll = false;
		}
	}

	public void startMpRegenerationByDoll() {
		final int INTERVAL_BY_DOLL = 64000;
		boolean isExistMprDoll = false;
		int mpRegenAmount = 0;
		Object[] dollList = getDollList().values().toArray();
		L1DollInstance doll = null;
		for (Object dollObject : dollList) {
			doll = (L1DollInstance) dollObject;
			if (doll.isMpRegeneration()) {
				isExistMprDoll = true;
				if (mpRegenAmount < doll.getMpRegenAmount()) {
					mpRegenAmount = doll.getMpRegenAmount();
				}
			}
		}
		if (!_mpRegenActiveByDoll && isExistMprDoll) {
			_mpRegenByDoll = new MpRegenerationByDoll(this, mpRegenAmount, INTERVAL_BY_DOLL);
			GeneralThreadPool.getInstance().schedule(_mpRegenByDoll, INTERVAL_BY_DOLL);
			_mpRegenActiveByDoll = true;
		}
	}

	/*
	 * public void startMpDecreaseByScales() { final int INTERVAL_BY_SCALES =
	 * 4000; _mpDecreaseByScales = new MpDecreaseByScales(this,
	 * INTERVAL_BY_SCALES);
	 * GeneralThreadPool.getInstance().schedule(_mpDecreaseByScales,
	 * INTERVAL_BY_SCALES); _mpDecreaseActiveByScales = true; }
	 */

	public void startHelpBySupport() {
		final int INTERVAL_BY_SUPPORT = 3000;
		_HelpBySupport = new HelpBySupport(this, INTERVAL_BY_SUPPORT);
		GeneralThreadPool.getInstance().schedule(_HelpBySupport, INTERVAL_BY_SUPPORT);
		_HelpActiveBySupport = true;
	}

	public void stopMpRegenerationByDoll() {
		if (_mpRegenActiveByDoll) {
			_mpRegenByDoll.cancel();
			_mpRegenByDoll = null;
			_mpRegenActiveByDoll = false;
		}
	}

	/*
	 * public void stopMpDecreaseByScales() { if (_mpDecreaseActiveByScales) {
	 * _mpDecreaseByScales.cancel(); _mpDecreaseByScales = null;
	 * _mpDecreaseActiveByScales = false; } } `
	 */

	public void stopHelpBySupport() {
		if (_HelpActiveBySupport) {
			_HelpBySupport.cancel();
			_HelpBySupport = null;
			_HelpActiveBySupport = false;
		}
	}

	public void stopAHRegeneration() {
		if (_AHRegenActive) {
			_AHRegen.cancel();
			_AHRegen = null;
			_AHRegenActive = false;
		}
	}

	public void stopSHRegeneration() {
		if (_SHRegenActive) {
			_SHRegen.cancel();
			_SHRegen = null;
			_SHRegenActive = false;
		}
	}

	public void stopHalloweenRegeneration() {
		if (_HalloweenRegenActive) {
			_HalloweenRegen.cancel();
			_HalloweenRegen = null;
			_HalloweenRegenActive = false;
		}
	}

	public void startObjectAutoUpdate() {
		final long INTERVAL_AUTO_UPDATE = 300;
		removeAllKnownObjects();
		_autoUpdateFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new L1PcAutoUpdate(getId()), 0L,
				INTERVAL_AUTO_UPDATE);
	}

	public void stopEtcMonitor() {
		if (_autoUpdateFuture != null) {
			_autoUpdateFuture.cancel(true);
			_autoUpdateFuture = null;
		}
		if (_expMonitorFuture != null) {
			_expMonitorFuture.cancel(true);
			_expMonitorFuture = null;
		}
		if (_ghostFuture != null) {
			_ghostFuture.cancel(true);
			_ghostFuture = null;
		}

		if (_hellFuture != null) {
			_hellFuture.cancel(true);
			_hellFuture = null;
		}

	}

	public void stopEquipmentTimer() {
		List<L1ItemInstance> allItems = this.getInventory().getItems();
		for (L1ItemInstance item : allItems) {
			if (item == null)
				continue;
			if (item.isEquipped() && item.getRemainingTime() > 0) {
				item.stopEquipmentTimer(this);
			}
		}
	}

	public void onChangeExp() {
		int level = ExpTable.getLevelByExp(getExp());
		int char_level = getLevel();
		int gap = level - char_level;
		if (gap == 0) {
			sendPackets(new S_OwnCharStatus(this));
			int percent = ExpTable.getExpPercentage(char_level, getExp());
			if (char_level >= 60 && char_level <= 64) {
				if (percent >= 10)
					removeSkillEffect(L1SkillId.레벨업보너스);
			} else if (char_level >= 65) {
				if (percent >= 5) {
					removeSkillEffect(L1SkillId.레벨업보너스);
				}
			}
			return;
		}

		if (gap > 0) {
			levelUp(gap);
			if (getLevel() >= 60) {
				setSkillEffect(L1SkillId.레벨업보너스, 10800000);
				sendPackets(new S_PacketBox(10800, true, true), true);
			}
			RenewStat();
		} else if (gap < 0) {
			levelDown(gap);
			removeSkillEffect(L1SkillId.레벨업보너스);
		}
	}

	/**
	 * 레벨퀘스트
	 * 
	 * @param lv
	 */
	public void quest_level(int lv) {
		try {
			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(257);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 5) {
						info.ck[0] = 5;
					}
					sendPackets(new S_QuestTalkIsland(this, 257, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(279);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 35) {
						info.ck[0] = 35;
					}
					sendPackets(new S_QuestTalkIsland(this, 279, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(283);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 40) {
						info.ck[0] = 40;
					}
					sendPackets(new S_QuestTalkIsland(this, 283, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(287);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 45) {
						info.ck[0] = 45;
					}
					sendPackets(new S_QuestTalkIsland(this, 287, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(291);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 48) {
						info.ck[0] = 48;
					}
					sendPackets(new S_QuestTalkIsland(this, 291, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(294);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 50) {
						info.ck[0] = 50;
					}
					sendPackets(new S_QuestTalkIsland(this, 294, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(297);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 52) {
						info.ck[0] = 52;
					}
					sendPackets(new S_QuestTalkIsland(this, 297, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(300);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 54) {
						info.ck[0] = 54;
					}
					sendPackets(new S_QuestTalkIsland(this, 300, info));
				}
			}

			if (lv > 1 && lv <= 70) {
				L1QuestInfo info = getQuestList(302);
				if (info != null && info.end_time == 0) {
					info.ck[0] = lv;
					if (info.ck[0] > 55) {
						info.ck[0] = 55;
					}
					sendPackets(new S_QuestTalkIsland(this, 302, info));
				}
			}

			/*
			 * if (lv > 1 && lv <= 70) { L1QuestInfo info = getQuestList(361);
			 * if (info != null && info.end_time == 0) { info.ck[0] = lv; if
			 * (info.ck[0] > 70) { info.ck[0] = 70; } sendPackets(new
			 * S_QuestTalkIsland(this, 361, info)); } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void onPerceive(L1PcInstance pc) {
		if (isGmInvis() /* || isGhost() || isInvisble() */) {
			return;
		}

		try {
			pc.addKnownObject(this);
			if (getAI() != null && pc.getAI() != null)
				return;

			if (getWorldObject() != null) {
				pc.sendPackets(getWorldObject(), false);
			} else if (pc.is_combat_field()) {
				if (isGm() || pc.isGm()) {
					pc.sendPackets(S_WorldPutObject.get(this));
				} else {
					if (get_current_combat_team_id() == pc.get_current_combat_team_id()) {
						pc.sendPackets(S_WorldPutObject.get(this, "아군", true));
					} else {
						pc.sendPackets(S_WorldPutObject.get(this, "적군", false));
					}
				}
			} else if (pc.getMapId() == 5554) {
				if (isGm() || pc.isGm())
					pc.sendPackets(S_WorldPutObject.get(this));
				else
					pc.sendPackets(S_WorldPutObject.get(this, "ID비공개", false));
			} else {
				if (S_WorldPutObject.IS_PRESENTATION_MARK && pc.getMapId() >= 1708 && pc.getMapId() <= 1710
						|| pc.getMapId() >= 12852 && pc.getMapId() <= 12862 || pc.getMapId() == 521
						|| pc.getMapId() == 522 || pc.getMapId() == 400) {
					if (pc.getClan() != null && getClan() != null && pc.getClan() == getClan()) {
						pc.sendPackets(S_WorldPutObject.get(this, true));
					} else {
						pc.sendPackets(S_WorldPutObject.get(this, false));
						// pc.sendPackets(S_ShowCmd.getSiegeObjectPutNoti(this,
						// 1));
					}
				} else {
					pc.sendPackets(S_WorldPutObject.get(this));
				}
			}
			if (isPinkName()) {
				pc.sendPackets(new S_PinkName(pc.getId(), pc.getPinkNameTime()));
			}

			for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(pc)) {
				if (target.isPinkName()) {
					pc.sendPackets(new S_PinkName(target.getId(), target.getPinkNameTime()));
				}
			}

			if (isPrivateShop()) {
				pc.sendPackets(new S_DoActionShop(getId(), ActionCodes.ACTION_Shop, getShopChat()));
			}
			if (isFishing()) {
				pc.sendPackets(new S_Fishing(getId(), ActionCodes.ACTION_Fishing, _fishingX, _fishingY));
			}

			if (isCrown()) {
				L1Clan clan = L1World.getInstance().getClan(getClanid());
				if (clan != null) {
					if (getId() == clan.getLeaderId() && clan.getCastleId() != 0) {
						pc.sendPackets(new S_CastleMaster(clan.getCastleId(), getId()));
					}
				}
			}
		} finally {
			L1Party party = getParty();
			if (party != null && party.isMember(pc)) {
				party.handshakePartyMemberStatus(this, pc);
			}
		}
	}

	public void broadcastRemoveAllKnownObjects() {
		for (L1Object known : getKnownObjects()) {
			if (known == null) {
				continue;
			}

			sendPackets(new S_RemoveObject(known));
		}
	}

	public void updateObject() {
		try {
			List<L1Object> _Alist = null;
			_Alist = getKnownObjects();
			for (L1Object known : _Alist) {
				if (known == null)
					continue;
				if (known.getMapId() == 631 && !isGm()) {
					if (known instanceof L1PcInstance) {
						continue;
					}
					/** 패키지상점 **/
					if (known instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) known;
						if (getMapId() == 631 && getCashStep() == 0 && !isGm()) {
							continue;
						} else if (getMapId() == 631 && getCashStep() == 1 && !isGm()) {
							if (!(npc.getNpcTemplate().get_npcId() >= 6100000
									&& npc.getNpcTemplate().get_npcId() <= 6100011
									|| npc.getNpcTemplate().get_npcId() == 4200022)) {
								continue;
							}
						} else if (getMapId() == 631 && getCashStep() == 2 && !isGm()) {
							if (!(npc.getNpcTemplate().get_npcId() >= 9000013
									&& npc.getNpcTemplate().get_npcId() <= 9000024
									|| npc.getNpcTemplate().get_npcId() == 4200022)) {
								continue;
							}
						} else if (getMapId() == 631 && getCashStep() == 3 && !isGm()) {
							if (!(npc.getNpcTemplate().get_npcId() >= 9000025
									&& npc.getNpcTemplate().get_npcId() <= 9000036
									|| npc.getNpcTemplate().get_npcId() == 4200022)) {
								continue;
							}
						}
					}
					/** 패키지상점 **/
				}

				if (Config.PC_RECOGNIZE_RANGE == -1) {
					if (!getLocation().isInScreen(known.getLocation())) {
						removeKnownObject(known);
						sendPackets(new S_RemoveObject(known));
					}
				} else {
					if (getLocation().getTileLineDistance(known.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
						removeKnownObject(known);
						sendPackets(new S_RemoveObject(known));
					}
				}
			}

			ArrayList<L1Object> _Vlist = null;
			_Vlist = L1World.getInstance().getVisibleObjects(this, Config.PC_RECOGNIZE_RANGE);
			for (L1Object visible : _Vlist) {
				if (visible == null)
					continue;
				if (visible instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) visible;
					/** 패키지상점 **/
					if (getMapId() == 631 && getCashStep() == 0 && !isGm()) {
						continue;
					} else if (getMapId() == 631 && getCashStep() == 1 && !isGm()) {
						if (!(npc.getNpcTemplate().get_npcId() >= 6100000 && npc.getNpcTemplate().get_npcId() <= 6100011
								|| npc.getNpcTemplate().get_npcId() == 4200022)) {
							continue;
						}
					} else if (getMapId() == 631 && getCashStep() == 2 && !isGm()) {
						if (!(npc.getNpcTemplate().get_npcId() >= 9000013 && npc.getNpcTemplate().get_npcId() <= 9000024
								|| npc.getNpcTemplate().get_npcId() == 4200022)) {
							continue;
						}
					} else if (getMapId() == 631 && getCashStep() == 3 && !isGm()) {
						if (!(npc.getNpcTemplate().get_npcId() >= 9000025 && npc.getNpcTemplate().get_npcId() <= 9000036
								|| npc.getNpcTemplate().get_npcId() == 4200022)) {
							continue;
						}
					}
				}
				/** 패키지상점 **/
				try {

					if (!knownsObject(visible)) {
						if (visible.getMapId() == 631 && !isGm()) {
							if (visible instanceof L1PcInstance) {
								continue;
							}
						}
						visible.onPerceive(this);
						if ((hasSkillEffect(L1SkillId.GMSTATUS_HPBAR) && L1HpBar.isHpBarTarget(visible))) {
							sendPackets(new S_HPMeter((L1Character) visible));
						}

					} else {
						if (visible instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) visible;
							if (getLocation().isInScreen(npc.getLocation()) && npc.getHiddenStatus() != 0) {
								npc.approachPlayer(this);
							}
						}
						/** 패키지상점 **/
						if (visible instanceof L1NpcCashShopInstance) {
							L1NpcInstance npc = (L1NpcInstance) visible;
							if (getMapId() == 631 && getCashStep() == 0 && !isGm()) {
								continue;
							} else if (getMapId() == 631 && getCashStep() == 1 && !isGm()) {
								if (!(npc.getNpcTemplate().get_npcId() >= 6100000
										&& npc.getNpcTemplate().get_npcId() <= 6100011
										|| npc.getNpcTemplate().get_npcId() == 4200022)) {
									continue;
								}
							} else if (getMapId() == 631 && getCashStep() == 2 && !isGm()) {
								if (!(npc.getNpcTemplate().get_npcId() >= 9000013
										&& npc.getNpcTemplate().get_npcId() <= 9000024
										|| npc.getNpcTemplate().get_npcId() == 4200022)) {
									continue;
								}
							} else if (getMapId() == 631 && getCashStep() == 3 && !isGm()) {
								if (!(npc.getNpcTemplate().get_npcId() >= 9000025
										&& npc.getNpcTemplate().get_npcId() <= 9000036
										|| npc.getNpcTemplate().get_npcId() == 4200022)) {
									continue;
								}
							}
						}
						/** 패키지상점 **/
					}
					if (hasSkillEffect(L1SkillId.GMSTATUS_HPBAR) && L1HpBar.isHpBarTarget(visible)) {
						L1Character c = (L1Character) visible;
						if (c.isChangedHpAndUpdate())
							sendPackets(new S_HPMeter(c));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendVisualEffect() {
		int poisonId = 0;
		if (getPoison() != null) {
			poisonId = getPoison().getEffectId();
		}
		if (getParalysis() != null) {
			poisonId = getParalysis().getEffectId();
		}
		if (poisonId != 0) {
			sendPackets(new S_Poison(getId(), poisonId));
			broadcastPacket(new S_Poison(getId(), poisonId));
		}
	}

	public void sendClanMarks() {
		// for (L1Clan clan : L1World.getInstance().getAllClans()) {
		// if (clan.getEmblem() != null) {
		// sendPackets(new S_Emblem(clan.getClanId()));
		// }
		// }

		if (getClanid() != 0) {
			L1Clan clan = L1World.getInstance().getClan(getClanid());
			if (clan != null) {
				if (isCrown() && getId() == clan.getLeaderId() && clan.getCastleId() != 0) {
					sendPackets(new S_CastleMaster(clan.getCastleId(), getId()));
				}
			}
		}
	}

	public void sendVisualEffectAtLogin() {
		sendVisualEffect();
	}

	public void sendVisualEffectAtTeleport() {
		if (isDrink()) {
			// sendPackets(new S_Liquor(getId()));
		}

		sendVisualEffect();
	}

	@Override
	public void setCurrentHp(int i) {
		if (getCurrentHp() == i)
			return;
		/** 2016.11.26 MJ 앱센터 LFC **/
		/* lfc중이고 hp가 깎였으면, */
		if ((getInstStatus() == InstStatus.INST_USERSTATUS_LFC) && i > getCurrentHp())
			addDamageFromLfc(i - getCurrentHp());

		super.setCurrentHp(i);
		sendPackets(new S_HPUpdate(getCurrentHp(), getMaxHp()));
		if (isInParty()) {
			// TODO 파티 프로토
			getParty().refreshPartyMemberStatus(this);
		}
	}

	@Override
	public void setCurrentMp(int i) {
		if (getCurrentMp() == i)
			return;
		if (isGm())// 원본
			i = getMaxMp();

		// i = getMaxMp();//MP소비안댐 테스트용삭제요망

		super.setCurrentMp(i);
		sendPackets(new S_MPUpdate(getCurrentMp(), getMaxMp()));
		if (isInParty()) {
			// TODO 파티 프로토
			getParty().refreshPartyMemberStatus(this);
		}
	}

	@Override
	public L1PcInventory getInventory() {
		return _inventory;
	}

	public L1DwarfForPackageInventory getDwarfForPackageInventory() {
		return _dwarfForPackage;
	}

	public L1Inventory getTradeWindowInventory() {
		return _tradewindow;
	}

	public boolean isGmInvis() {
		return _gmInvis;
	}

	public void setGmInvis(boolean flag) {
		_gmInvis = flag;
	}

	public int CubeMr;

	public int getCurrentWeapon() {
		return _currentWeapon;
	}

	public void setCurrentWeapon(int i) {
		_currentWeapon = i;
	}

	private Ability pc;

	public int getType() {
		return _type;
	}

	public void setType(int i) {
		_type = i;
	}

	public short getAccessLevel() {
		return _accessLevel;
	}

	public void setAccessLevel(short i) {
		_accessLevel = i;
	}

	public int getClassId() {
		return _classId;
	}

	public void setClassId(int i) {
		_classId = i;
		_classFeature = L1ClassFeature.newClassFeature(i);
	}

	public L1ClassFeature getClassFeature() {
		return _classFeature;
	}

	@Override
	public int getExp() {
		return _exp;
	}

	@Override
	public void setExp(int i) {
		_exp = i;
	}

	public synchronized int getReturnStat() {
		return _returnstat;
	}

	public synchronized void setReturnStat(int i) {
		_returnstat = i;
	}

	private L1PcInstance getStat() {
		return null;
	}

	public void reduceCurrentHp(double d, L1Character l1character) {
		getStat().reduceCurrentHp(d, l1character);
	}

	private void notifyPlayersLogout(List<L1PcInstance> playersArray) {
		for (L1PcInstance player : playersArray) {
			if (player == null)
				continue;
			if (player.knownsObject(this)) {
				player.removeKnownObject(this);
				player.sendPackets(new S_RemoveObject(this));
			}
		}
	}

	private void quitGame() {
		try {
			remove_companion();
			if (!(noPlayerCK || noPlayerck2 || isPrivateShop() || pc != null)) {
				/** 로그파일저장 **/
				l1j.server.swing.chocco.count -= 1;
				l1j.server.swing.chocco.label2.setText(" " + l1j.server.swing.chocco.count);
				l1j.server.swing.chocco.userlist.remove(getName());
				System.out.println(String.format("### [캐릭터 접속 종료] 캐릭터: %s | 계정: %s | 아이피: %s", getName(), getAccountName(), getNetConnection().getHostname()));
			}
		} catch (Exception e) {
		}

		try {
			if (isFishing()) {
				FishingTimeController.getInstance().endFishing(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (L1HauntedHouse.getInstance().isMember(this)) {
				if (getMapId() == 5140) {
					L1HauntedHouse.getInstance().clearBuff(this);
				}

				L1HauntedHouse.getInstance().removeMember(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (L1Racing.getInstance().isMember(this)) {
				if (getMapId() == 5143) {
					L1HauntedHouse.getInstance().clearBuff(this);
				}

				L1Racing.getInstance().removeMember(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getMap().setPassable(getLocation(), true);
			// 사망하고 있으면(자) 거리에 되돌려, 공복 상태로 한다
			if (isDead()) {
				int[] loc = Getback.GetBack_Location(this, true);
				setX(loc[0]);
				setY(loc[1]);
				setMap((short) loc[2]);
				setCurrentHp(getLevel());
				set_food(39); // 10%
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 트레이드를 중지한다
		try {
			if (getTradeID() != 0) { // 트레이드중
				L1Trade trade = new L1Trade();
				trade.TradeCancel(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 결투중
		try {
			if (getFightId() != 0) {
				L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(getFightId());
				if (fightPc != null) {
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
				}
				setFightId(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 파티를 빠진다
		try {
			if (isInParty() || getParty() != null) { // 파티중
				getParty().leaveMember(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 채팅파티를 빠진다
		try {
			if (isInChatParty()) { // 채팅파티중
				getChatParty().leaveMember(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (L1DeathMatch.getInstance().isMember(this)) {
				L1DeathMatch.getInstance().removeMember(this);
			}
			if (L1HauntedHouse.getInstance().isMember(this)) {
				L1HauntedHouse.getInstance().removeMember(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 애완동물을 월드 MAP상으로부터 지운다

		try {
			Object[] petList = getPetList().values().toArray();
			L1PetInstance pet = null;
			// L1SummonInstance summon = null;
			for (Object petObject : petList) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					pet.unloadMaster();
				}
				// 서먼
				if (petObject instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) petObject;
					summon.Death(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 마법인형을 월드 맵상으로부터 지운다
		try {
			Object[] dollList = getDollList().values().toArray();
			L1DollInstance doll = null;
			for (Object dollObject : dollList) {
				doll = (L1DollInstance) dollObject;
				doll.deleteDoll(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Object[] supportList = getSupportList().values().toArray();
			L1SupportInstance support = null;
			for (Object supportObject : supportList) {
				if (supportObject == null)
					continue;
				support = (L1SupportInstance) supportObject;
				support.deleteSupport();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Object[] followerList = getFollowerList().values().toArray();
			L1FollowerInstance follower = null;
			for (Object followerObject : followerList) {
				if (followerObject == null)
					continue;
				follower = (L1FollowerInstance) followerObject;
				follower.setParalyzed(true);
				follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(),
						follower.getHeading(), follower.getMapId());
				follower.deleteMe();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BQSCharacterDataLoader.out(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 엔챤트를 DB의 character_buff에 보존한다
		try {
			CharBuffTable.DeleteBuff(this);
			CharBuffTable.SaveBuff(this);
			clearSkillEffectTimer();
			SkillCheck.getInstance().QuitDelSkill(this);//
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item == null)
					continue;
				if (item.getCount() <= 0) {
					getInventory().deleteItem(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// pc의 모니터를 stop 한다.
		stopEtcMonitor();
		// 온라인 상태를 OFF로 해, DB에 캐릭터 정보를 기입한다
		setOnlineStatus(0);

		try {
			save();
			saveInventory();
			L1BookMark.WriteBookmark(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean _destroyed = false;

	public void logout() {
		try {
			synchronized (this) {
				if (_destroyed) {
					return;
				}
				remove_companion();

				if (getAccount() != null) {
					getAccount().updateBlessOfAin();
					getAccount().updateGrangeKinAngerTime();
					/** 마지막 계정종료 업데이트 **/
					getAccount().updateLastLogOut();
				}

				DungeonTimeProgressLoader.update(this);
				if (!is무인상점()) {
					SC_ATTENDANCE_USER_DATA_EXTEND userData = getAttendanceData();
					if (userData != null) {
						SC_ATTENDANCE_USER_DATA_EXTEND.update(getAccountName(), userData);
						setAttendanceData(null);
					}
				}
				if (is_combat_field()) {
					MJCombatObserver observer = MJCombatLoadManager.getInstance()
							.get_current_observer(get_current_combat_id());
					if (observer != null) {
						observer.remove(this);
					}
				}

				MJRankUserLoader.getInstance().offUser(this);
				MJRaidSpace.getInstance().getBackPc(this);

				/** 2016.11.26 MJ 앱센터 LFC **/
				MJInstanceSpace.getInstance().getBackPc(this);
				/** 2016.11.26 MJ 앱센터 LFC **/
				
				/**일일 제한 상점*/
				limit_shop.clear();

				// 퀘스트 몬스터 시스템
				CharacterQuestMonsterTable.getInstance().LogOutQuest(this);
				CharacterSlotItemTable.getInstance().updateCharSlotItems(this);
				CharacterQuestTable.getInstance().LogOutQuest(this);
				_slotItemTwo.clear();
				_slotItemOne.clear();
				quitGame();
				L1World world = L1World.getInstance();
				notifyPlayersLogout(getKnownPlayers());
				world.removeVisibleObject(this);
				world.removeObject(this);
				notifyPlayersLogout(world.getRecognizePlayer(this));
				_inventory.clearItems();
				/*
				 * WarehouseManager w = WarehouseManager.getInstance();
				 * w.delPrivateWarehouse(this.getAccountName());
				 * w.delElfWarehouse(this.getAccountName());
				 * w.delSpecialWarehouse(this.getName());
				 */
				_dwarfForPackage.clearItems();
				removeAllKnownObjects();
				stopHpMpRegeneration();
				// stopMpRegeneration();
				stopHalloweenRegeneration();
				stopAHRegeneration();
				stopHelpBySupport();
				stopHpRegenerationByDoll();
				stopMpRegenerationByDoll();
				stopSHRegeneration();
				// stopMpDecreaseByScales();
				stopEquipmentTimer();
				setDead(true);
				setNetConnection(null);
				stopEinhasadTimer();
				stopPapuBlessing();// 파푸리온 블레싱
				getAC().addAc(1);
				_destroyed = true;
				dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GameClient getNetConnection() {
		return _netConnection;
	}

	public void setNetConnection(GameClient clientthread) {
		_netConnection = clientthread;
	}

	public boolean isInParty() {
		return getParty() != null;
	}

	public L1Party getParty() {
		return _party;
	}

	public void setParty(L1Party p) {
		_party = p;
	}

	public boolean isInChatParty() {
		return getChatParty() != null;
	}

	public L1ChatParty getChatParty() {
		return _chatParty;
	}

	public void setChatParty(L1ChatParty cp) {
		_chatParty = cp;
	}

	public int getPartyID() {
		return _partyID;
	}

	public void setPartyID(int partyID) {
		_partyID = partyID;
	}

	public int getPartyType() {
		return _partyType;
	}

	public void setPartyType(int partyType) {
		_partyType = partyType;
	}

	public int getTradeID() {
		return _tradeID;
	}

	public void setTradeID(int tradeID) {
		_tradeID = tradeID;
	}

	public void setTradeOk(boolean tradeOk) {
		_tradeOk = tradeOk;
	}

	public boolean getTradeOk() {
		return _tradeOk;
	}

	public int getTempID() {
		return _tempID;
	}

	public void setTempID(int tempID) {
		_tempID = tempID;
	}

	// public boolean isTeleport() {
	// return _isTeleport;
	// }

	// public void setTeleport(boolean flag) {
	// _isTeleport = flag;
	// if (flag)
	// AttackController.stop(this);
	// }

	public boolean 텔대기() {
		return _텔대기;
	}

	public void 텔대기(boolean flag) {
		_텔대기 = flag;
	}

	public boolean isDrink() {
		return _isDrink;
	}

	public void setDrink(boolean flag) {
		_isDrink = flag;
	}

	public boolean isGres() {
		return _isGres;
	}

	public void setGres(boolean flag) {
		_isGres = flag;
	}

	public boolean isPinkName() {
		return _isPinkName;
	}

	public void setPinkName(boolean flag) {
		_isPinkName = flag;
	}
	
	public boolean isAutoClanjoin() {
		return _isAutoClanjoin;
	}

	public void setAutoClanjoin(boolean flag) {
		_isAutoClanjoin = flag;
	}
	public boolean isAutoFish() {
		return _isAutofish;
	}

	public void setAutoFish(boolean flag) {
		_isAutofish = flag;
	}
	public void setShopChat(byte[] chat) {
		_shopChat = chat;
	}

	public byte[] getShopChat() {
		return _shopChat;
	}

	public boolean isPrivateShop() {
		return _isPrivateShop;
	}

	public void setPrivateShop(boolean flag) {
		_isPrivateShop = flag;
	}

	public boolean isPrivateReady() {
		return _isPrivateReady;
	}

	public void setPrivateReady(boolean b) {
		_isPrivateReady = b;
	}

	private int _special_size;

	public int get_SpecialSize() {
		return _special_size;
	}

	public void set_SpecialSize(int special_size) {
		_special_size = special_size;
	}

	public int getPartnersPrivateShopItemCount() {
		return _partnersPrivateShopItemCount;
	}

	public void setPartnersPrivateShopItemCount(int i) {
		_partnersPrivateShopItemCount = i;
	}

	private int birthday;// 생일

	public int getBirthDay() {
		return birthday;
	}

	public void setBirthDay(int t) {
		birthday = t;
	}

	private int _TelType = 0;

	public int getTelType() {
		return _TelType;
	}

	public void setTelType(int i) {
		_TelType = i;
	}

	private int AinState = 0;

	public int getAinState() {
		return AinState;
	}

	public void setAinState(int AinState) {
		this.AinState = AinState;
	}

	public boolean SurvivalState; // 생존의 외침 상태
	private int SurvivalGauge; // 생존의 외침 게이지

	public int getSurvivalGauge() {
		return SurvivalGauge;
	}

	public void setSurvivalGauge(int SurvivalGauge) {
		this.SurvivalGauge = SurvivalGauge;
	}

	public int[] DragonPortalLoc = new int[3];// 드래곤 포탈

	public void sendPackets(MJIProtoMessage message, int messageId) {
		sendPackets(message, messageId, true);
	}

	public void sendPackets(MJIProtoMessage message, MJEProtoMessages e, boolean isClear) {
		sendPackets(message, e.toInt(), isClear);
	}

	public void sendPackets(MJIProtoMessage message, MJEProtoMessages e) {
		sendPackets(message, e, true);
	}

	public void sendPackets(MJIProtoMessage message, int messageId, boolean isClear) {
		if (getNetConnection() != null)
			getNetConnection().sendPacket(message, messageId, isClear);
	}

	public void sendPackets(ProtoOutputStream[] streams, boolean isClear) {
		if (getNetConnection() != null) {
			GameClient clnt = getNetConnection();
			for (ProtoOutputStream stream : streams)
				clnt.sendPacket(stream, isClear);
		}
	}

	public void sendPackets(ProtoOutputStream stream, boolean isClear) {
		if (getNetConnection() != null)
			getNetConnection().sendPacket(stream, isClear);
	}

	public void sendPackets(ProtoOutputStream stream) {
		if (getNetConnection() != null)
			getNetConnection().sendPacket(stream);
	}

	public void sendPackets(ServerBasePacket[] pcks, boolean clear) {
		if (getNetConnection() != null) {
			for (ServerBasePacket pck : pcks)
				getNetConnection().sendPacket(pck, clear);
		}
	}

	public void sendPackets(ServerBasePacket serverbasepacket, boolean clear) {
		try {
			if (getNetConnection() != null) {
				
				if(serverbasepacket != null){
					if(serverbasepacket instanceof S_WorldPutObject){
						S_WorldPutObject _S_WorldPutObject = (S_WorldPutObject)serverbasepacket;
						if(_S_WorldPutObject._pc != null){
							if(_S_WorldPutObject._pc.isInvisble() && this.isGm()){
								try {
								getNetConnection().sendPacket(S_WorldPutObject.getinvis_party(_S_WorldPutObject._pc));
								} catch (Exception e) {}
								return;
							}else {
							
								if(_S_WorldPutObject._pc.isInvisble() &&
										_S_WorldPutObject._pc.getParty() != null){
									if(_S_WorldPutObject._pc.getParty().isMember(this)){
										try {
										getNetConnection().sendPacket(S_WorldPutObject.getinvis_party(_S_WorldPutObject._pc));
										} catch (Exception e) {}
										return;
									}
								}
							}
						}
					}
				}
				
				
				
				
				getNetConnection().sendPacket(serverbasepacket, clear);
			}
		} catch (Exception e) {
		}
	}

	public void sendPackets(String s) {
		sendPackets(new S_SystemMessage(s), true);
	}

	public void sendPackets(int code) {
		sendPackets(new S_ServerMessage(code), true);
	}

	public void sendPackets(ServerBasePacket serverbasepacket) {
		if (getNetConnection() == null)
			return;
		
		if(serverbasepacket != null){
			if(serverbasepacket instanceof S_WorldPutObject){
				S_WorldPutObject _S_WorldPutObject = (S_WorldPutObject)serverbasepacket;
				if(_S_WorldPutObject._pc != null){
					if(_S_WorldPutObject._pc.isInvisble() && _S_WorldPutObject._pc.getParty() != null){
						if(_S_WorldPutObject._pc.getParty().isMember(this)){
							try {
							getNetConnection().sendPacket(S_WorldPutObject.getinvis_party(_S_WorldPutObject._pc));
							} catch (Exception e) {}
							return;
						}
					}
				}
			}
		}

		try {
			getNetConnection().sendPacket(serverbasepacket);
		} catch (Exception e) {
		}
	}
	

	public boolean test = false;

	@Override
	public void onAction(L1PcInstance attacker) {
		if (attacker == null) {
			return;
		}

		if (get_teleport() && getAI() == null) {
			return;
		}

		if (getZoneType() == 1 || attacker.getZoneType() == 1) {
			L1Attack attack_mortion = new L1Attack(attacker, this);
			attack_mortion.action();
			return;
		}

		if (checkNonPvP(this, attacker) == true) {
			return;
		}

		if (attacker.isSupporting()) {
			L1SupportInstance support = null;
			Object[] supportList = attacker.getSupportList().values().toArray();
			for (Object supportObject : supportList) {
				support = (L1SupportInstance) supportObject;
				support.deleteSupport();
			}
			attacker.sendPackets(new S_OwnCharStatus(attacker));
			attacker.setSupporting(false);
		}

		if (getCurrentHp() > 0 && !isDead()) {
			attacker.delInvis();

			boolean isCounterBarrier = false;
			boolean isMortalBody = false;
			L1Attack attack = new L1Attack(attacker, this);
			L1Magic magic = null;

			if (attack.calcHit()) {
				if (hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
					magic = new L1Magic(this, attacker);
					boolean isProbability = magic.calcProbabilityMagic(L1SkillId.COUNTER_BARRIER);
					boolean isShortDistance = attack.isShortDistance();
					if (isProbability && isShortDistance) {
						isCounterBarrier = true;
					}
				} else if (hasSkillEffect(L1SkillId.MORTAL_BODY)) {
					magic = new L1Magic(this, attacker);
					boolean isProbability = magic.calcProbabilityMagic(L1SkillId.MORTAL_BODY);
					boolean isShortDistance1 = attack.isShortDistance1();
					if (isProbability && isShortDistance1) {
						isMortalBody = true;
					}
				}
				if (!isCounterBarrier && !isMortalBody) {
					attacker.set_pet_target(this);
					attack.calcDamage();
					applySpecialEnchant(attacker);
					attack.addPcPoisonAttack(attacker, this);
				}
			}

			if (isCounterBarrier) {
				attack.actionCounterBarrier();
				attack.commitCounterBarrier();
				attack.commit();
				/** 시전자도 피가 달아지게 추가 **/
			} else if (isMortalBody) {
				attack.calcDamage();
				attack.actionMortalBody();
				attack.commitMortalBody();
				attack.commit();
			} else {
				attack.action();
				attack.commit();
			}
		}
	}

	private void applySpecialEnchant(L1PcInstance attacker) {

		if (getWeapon() == null || !getWeapon().isSpecialEnchantable()) {
			return;
		}

		for (int i = 1; i <= 3; ++i) {
			int specialEnchant = getWeapon().getSpecialEnchant(i);

			if (specialEnchant == 0) {
				break;
			}

			if (_random.nextInt(100) >= 1) {
				continue;
			}

			boolean success = true;

			switch (specialEnchant) {
			// 여기 각 성능별 처리
			case L1ItemInstance.CHAOS_SPIRIT:
				success = false;
				break;
			case L1ItemInstance.CORRUPT_SPIRIT:
				new L1SkillUse().handleCommands(this, L1SkillId.COUNTER_MAGIC, getId(), getX(), getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				break;
			case L1ItemInstance.ANTARAS_SPIRIT:
			case L1ItemInstance.BALLACAS_SPIRIT:
			case L1ItemInstance.LINDBIOR_SPIRIT:
				success = false;
				break;
			case L1ItemInstance.PAPURION_SPIRIT:
				if (attacker.hasSkillEffect(L1SkillId.STATUS_BRAVE) || attacker.hasSkillEffect(L1SkillId.STATUS_HASTE)
						|| attacker.hasSkillEffect(L1SkillId.HOLY_WALK)
						|| attacker.hasSkillEffect(L1SkillId.MOVING_ACCELERATION)) {
					attacker.killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
					attacker.killSkillEffectTimer(L1SkillId.STATUS_HASTE);
					attacker.killSkillEffectTimer(L1SkillId.HOLY_WALK);
					attacker.killSkillEffectTimer(L1SkillId.MOVING_ACCELERATION);
					attacker.sendPackets(new S_SkillBrave(attacker.getId(), 0, 0));
					attacker.broadcastPacket(new S_SkillBrave(attacker.getId(), 0, 0));
					attacker.setBraveSpeed(0);
					attacker.sendPackets(new S_SkillHaste(attacker.getId(), 0, 0));
					attacker.broadcastPacket(new S_SkillHaste(attacker.getId(), 0, 0));
					attacker.setMoveSpeed(0);
				}
				break;
			case L1ItemInstance.DEATHKNIGHT_SPIRIT:
			case L1ItemInstance.BAPPOMAT_SPIRIT:
				success = false;
				break;
			case L1ItemInstance.BALLOG_SPIRIT:
				break;
			case L1ItemInstance.ARES_SPIRIT:
				success = false;
				break;
			}

			if (success) {
				break; // 동시에 2개 이상은 발동 안됨.
			}
		}
	}

	public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
		L1PcInstance targetpc = null;
		if (target instanceof L1PcInstance) {
			targetpc = (L1PcInstance) target;
		} else if (target instanceof MJCompanionInstance) {
			targetpc = ((MJCompanionInstance) target).get_master();
		} else if (target instanceof L1PetInstance) {
			targetpc = (L1PcInstance) ((L1PetInstance) target).getMaster();
		} else if (target instanceof L1SummonInstance) {
			targetpc = (L1PcInstance) ((L1SummonInstance) target).getMaster();
		}
		if (targetpc == null) {
			return false;
		}
		if (!Config.ALT_NONPVP) {
			if (getMap().isCombatZone(getLocation())) {
				return false;
			}

			L1Clan clan = pc.getClan();
			L1Clan enemyclan = targetpc.getClan();
			if (clan != null && enemyclan != null) {
				MJWar war = clan.getCurrentWar();
				MJWar enemyWar = enemyclan.getCurrentWar();
				if (war != null && enemyWar != null && war.equals(enemyWar))
					return false;
			}

			if (target instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) target;
				if (isInWarAreaAndWarTime(pc, targetPc)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
		int castleId = L1CastleLocation.getCastleIdByArea(pc);
		int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
		if (castleId != 0 && targetCastleId != 0 && castleId == targetCastleId) {
			if (MJCastleWarBusiness.getInstance().isNowWar(castleId)) {
				return true;
			}
		}
		return false;
	}

	public void setPetTarget(L1Character target) {
		Object[] petList = getPetList().values().toArray();
		L1PetInstance pets = null;
		L1SummonInstance summon = null;
		for (Object pet : petList) {
			if (pet == null)
				continue;
			if (pet instanceof L1PetInstance) {
				pets = (L1PetInstance) pet;
				pets.setMasterTarget(target);
			} else if (pet instanceof L1SummonInstance) {
				summon = (L1SummonInstance) pet;
				summon.setMasterTarget(target);
			}
		}
	}

	public void set_pet_target(L1Character target) {
		setPetTarget(target);
		if (m_companion != null)
			m_companion.set_target(target);
	}

	public boolean isstop() {
		return (hasSkillEffect(SHOCK_STUN)) || hasSkillEffect(L1SkillId.EMPIRE) || (hasSkillEffect(ICE_LANCE))
				|| (hasSkillEffect(BONE_BREAK)) || (hasSkillEffect(EARTH_BIND)) || (hasSkillEffect(MOB_RANGESTUN_19))
				|| (hasSkillEffect(MOB_SHOCKSTUN_30)) || (hasSkillEffect(OMAN_STUN)) || (hasSkillEffect(Maeno_STUN))
				|| (hasSkillEffect(ANTA_MESSAGE_6)) || (hasSkillEffect(ANTA_MESSAGE_7))
				|| (hasSkillEffect(ANTA_MESSAGE_8)) || (hasSkillEffect(ANTA_SHOCKSTUN));
	}
	
	// 스턴류 스킬을 피격했을 때 타이탄 락 풀리는 소스
	public boolean isstun() {
		return (hasSkillEffect(SHOCK_STUN)) || hasSkillEffect(L1SkillId.EMPIRE)
				|| (hasSkillEffect(BONE_BREAK)) || (hasSkillEffect(MOB_RANGESTUN_19))
				|| (hasSkillEffect(MOB_SHOCKSTUN_30)) || (hasSkillEffect(OMAN_STUN)) || (hasSkillEffect(Maeno_STUN));
	}

	public void delInvis() {
		if (hasSkillEffect(L1SkillId.INVISIBILITY) && !isGm()) {
			killSkillEffectTimer(L1SkillId.INVISIBILITY);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacket(new S_Invis(getId(), 0));
			broadcastPacket(S_WorldPutObject.get(this));
			// broadcastPacket(new S_OtherCharPacks(this));
		}
		if (hasSkillEffect(L1SkillId.BLIND_HIDING)) {
			killSkillEffectTimer(L1SkillId.BLIND_HIDING);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacket(new S_Invis(getId(), 0));
			broadcastPacket(S_WorldPutObject.get(this));
			// broadcastPacket(new S_OtherCharPacks(this));
		}
		
		L1Party party = getParty();
		if(party != null){
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
				if(party.isMember(pc)){						
					pc.sendPackets(new S_HPMeter(this));
				}
			}
			party.refreshPartyMemberStatus(this);
		}

	}

	public void delBlindHiding() {
		killSkillEffectTimer(L1SkillId.BLIND_HIDING);
		sendPackets(new S_Invis(getId(), 0));
		broadcastPacket(new S_Invis(getId(), 0));
		broadcastPacket(S_WorldPutObject.get(this));
	}

	public void receiveDamage(L1Character attacker, int damage, int attr) {
		if (damage == 0)
			return;
		Random random = new Random(System.nanoTime());
		int player_mr = getResistance().getEffectedMrBySkill();
		int rnd = random.nextInt(100) + 1;
		if (player_mr >= rnd) {
			damage /= 2;
		}

		receiveDamage(attacker, damage);
	}

	public void receiveManaDamage(L1Character attacker, int mpDamage) {
		if (mpDamage > 0 && !isDead()) {
			delInvis();
			if (attacker instanceof L1PcInstance) {
				L1PinkName.onAction(this, attacker);
			}
			int newMp = getCurrentMp() - mpDamage;
			this.setCurrentMp(newMp);
		}
	}

	public boolean isInWarArea() {
		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(this);

		if (castleId != 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		return isNowWar;
	}

	public void receiveCounterBarrierDamage(L1Character attacker, int damage) {
		try {
			if (getCurrentHp() > 0 && !isDead()) {
				if (attacker != null && attacker != this && !knownsObject(attacker)
						&& attacker.getMapId() == this.getMapId()) {
					attacker.onPerceive(this);
				}

				if (damage > 0) {
					delInvis();
					if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
						removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
					} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
						removeSkillEffect(L1SkillId.PHANTASM);
					}
					if (attacker.instanceOf(MJL1Type.L1TYPE_PC)) {
						if (is_combat_field()) {
							MJCombatObserver observer = MJCombatLoadManager.getInstance()
									.get_current_observer(get_current_combat_id());
							if (observer != null) {
								observer.on_damage((L1PcInstance) attacker, this, damage);
							} else {
								set_instance_status(MJEPcStatus.WORLD);
							}
						}
					}
				} else if (damage < 0) {
					return;
				}
				if (getInventory().checkEquipped(145) || getInventory().checkEquipped(149)) {
					damage *= 1.5;
				}
				int newHp = getCurrentHp() - damage;
				if (newHp > getMaxHp()) {
					newHp = getMaxHp();
				}
				
				if (newHp > 0) {
					setCurrentHp(newHp);
				} else if (newHp <= 0) {
					L1ItemInstance halpas = get_skill().getHalpas();
					if (halpas != null && !get_skill().isHalpasTime()) {
						long hour = ((22 - halpas.getEnchantLevel()) * 3600 * 1000);
						setCurrentHp(getMaxHp());
						new L1SkillUse().handleCommands(this, L1SkillId.HALPAS, getId(), getX(), getY(), null, 0, 4);
						sendPackets(new S_SystemMessage("할파스의 신의: 리덕션 대폭 상승 효과."));
						get_skill().setHalpasTime(new Timestamp(System.currentTimeMillis() + hour));
//						S_EffectLocation el = new S_EffectLocation(getX(), getY(), 14446);
						sendPackets(new S_EffectLocation(getX(), getY(), 14446));
//						broadcastPacket(el);
					}
					else if (isElf() && hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
						int newMp = getCurrentMp() - damage;
						this.setCurrentHp(10);
						if (newMp <= 0) {
							death(attacker, true);
							this.setCurrentHp(0);
						}
						this.setCurrentMp(newMp);
					} else {
						if (isGm()) {
							this.setCurrentHp(getMaxHp());
						} else {
							//카운터배리어, 타이탄데미지처리
							if (isDeathMatch()) {
								if (getMapId() == 5153) {
									try {
										save();
										beginGhost(getX(), getY(), (short) getMapId(), true);
										sendPackets(new S_ServerMessage(1271));
									} catch (Exception e) {
										e.printStackTrace();
									}
									return;
								}
							} else {
								death(attacker, false);
							}
						}
					}
				}
				/*
				if (newHp <= 10) {
					if (get_skill().isHalpas() && !get_skill().isHalpasTime()) {
						
					}
					else if (isElf() && hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
						int newMp = getCurrentMp() - damage;
						this.setCurrentHp(10);
						if (newMp <= 0) {
							death(attacker, true);
							this.setCurrentHp(0);
						}
						this.setCurrentMp(newMp);
					} else if (newHp <= 0) {
						if (isGm()) {
							this.setCurrentHp(getMaxHp());
						} else {
							//카운터배리어, 타이탄데미지처리
							if (isDeathMatch()) {
								if (getMapId() == 5153) {
									try {
										save();
										beginGhost(getX(), getY(), (short) getMapId(), true);
										sendPackets(new S_ServerMessage(1271));
									} catch (Exception e) {
										e.printStackTrace();
									}
									return;
								}
							} else {
								death(attacker, false);
							}
						}
					}
				}
				if (newHp > 0) {
					this.setCurrentHp(newHp);
				}
				*/
			} else if (!isDead()) {
				System.out.println(String.format("■■■■■■■ HP 감소가 올바르지 못한 캐릭발견.※혹은 최초부터 피가 0.\r\n%s -> %s : %d",
						attacker == null ? "null" : attacker.getName(), getName(), damage));
				death(attacker, false);
				sendPackets("스킬 사용이나 아이템 사용이 안된다면 리스타트 하시길 바랍니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
					String.format("%s -> %s : %d", attacker == null ? "null" : attacker.getName(), getName(), damage));
		}
	}

	
	// PVP 추가 대미지
	private int calculate_sprite_pvp_damage(int sprite_id) {
		switch (sprite_id) {
		case 15986:
		case 16002:
		case 16008:
		case 16014:
		case 16027:
		case 16040:
		case 16053:
		case 16056:
		case 16074:
		case 16284:
		case 17515:
		case 17531:
		case 17535:
		case 17541:
		case 17545:
		case 17549:
		case 13715:
		case 13717:
		case 15115:
		case 13721:
		case 13723:
		case 13725:
		case 13727:
		case 13729:
		case 13731:
		case 13733:
		case 13735:
		case 13737:
		case 13739:
		case 13741:
		case 13743:
		case 13745:
		case 14491:
		case 18265:
		case 18269:
		case 18273:
		case 18291:
		case 18295:
		case 18300:
		case 18304:
		case 18308:
		case 18384:
		case 18388:
		case 18392:
		case 18396:
		case 18400:
		case 18404:
		case 18408:
		case 18412:
		case 18416:
		case 18420:
		case 18432:
		case 18436:
		case 18440:
		case 18444:
		case 18448:
		case 18452:
		case 18456:
		case 18460:
		case 18464:
		case 18560:
		case 18564:
		case 18568:
		case 18572:
		case 18576:
		case 18580:
		case 5146: //기르타스 변신
		case 17288: //100레벨 질리언
		case 17411: //100레벨 아툰
		case 17415: //100레벨 조우
		case 17439: //100레벨 크리스터
		case 17453: //100레벨 이실로테
		case 17575: //99레벨 도펠겡어 킹
			return 2;
		}
		return 0;
	}

	public void receiveDamage(L1Character attacker, int damage) {
		if (hasSkillEffect(L1SkillId.LUCIFER) && damage > 0) {
			damage -= (damage * 0.10);
		}

		if (hasSkillEffect(L1SkillId.DARK_BLIND)) {
			removeSkillEffect(L1SkillId.DARK_BLIND);
		}

		/*
		 * if (hasSkillEffect(L1SkillId.CUBE_AVATAR)) { damage += 5; }
		 */
		if (attacker.hasSkillEffect(L1SkillId.CUBE_AVATAR)) {
			damage += 10;
		}

		if (attacker instanceof L1PcInstance) {
			damage += calculate_sprite_pvp_damage(attacker.getCurrentSpriteId());
			if (is_combat_field()) {
				L1PcInstance attacker_pc = (L1PcInstance) attacker;
				if (get_current_combat_id() == attacker_pc.get_current_combat_id()
						&& get_current_combat_team_id() == attacker_pc.get_current_combat_team_id()) {
					return;
				}
			}
		}

		if ((getCurrentHp() > 0 && !isDead()) || getAI() != null || isGm()) {
			if (getAI() != null) {
				if (attacker instanceof L1PcInstance) {
					L1Character t = getAI().getCurrentTarget();
					if (t == null || t.getId() != attacker.getId())
						MJBotUtil.sendBotOnDamageMent(getAI(), attacker);
				}

				if (damage > 0 && getAI() instanceof MJBotMovableAI && attacker instanceof L1PcInstance)
					((MJBotMovableAI) getAI()).addTarget(attacker);
			}

			if (isLock())
				damage = 0;

			if (attacker != null && attacker != this && !knownsObject(attacker)
					&& attacker.getMapId() == this.getMapId()) {
				attacker.onPerceive(this);
			}

			if (damage > 0) {
				if (attacker instanceof L1PcInstance) {
					L1PcInstance attacker_pc = (L1PcInstance) attacker;
					L1PinkName.onAction(this, attacker);
					if (is_combat_field()) {
						MJCombatObserver observer = MJCombatLoadManager.getInstance()
								.get_current_observer(get_current_combat_id());
						if (observer != null) {
							observer.on_damage(attacker_pc, this, damage);
						} else {
							set_instance_status(MJEPcStatus.WORLD);
						}
					}
					MJCompanionInstance companion = attacker_pc.get_companion();
					if (companion != null && companion.get_command_state().equals(eCommand.TM_Aggressive)) {
						if (getZoneType() == 0 && companion.getZoneType() == 0)
							companion.do_pink_name();
					}
				} else if (attacker instanceof MJCompanionInstance) {
					MJCompanionInstance companion = (MJCompanionInstance) attacker;
					if (getZoneType() == 0 && companion.getZoneType() == 0)
						companion.do_pink_name();
				}

				for (L1ItemInstance item : _equipSlot.getArmors()) {
					MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getDef(item.getItemId());
					if (model != null)
						damage -= model.get(attacker, this, item, damage);
				}

				for (L1ItemInstance item : getInventory().getItems()) {
					CharmSkillModel model = CharmModelLoader.getInstance().getDef(item.getItemId());
					if (model != null)
						damage -= model.get(attacker, this, item, damage);
				}

				if (damage <= 0)
					damage = 10;
				if (getAI() != null && getAI().getBotType() == MJBotType.REDKNIGHT) {
					damage += (getMaxHp() / Config.RedKnight_dieCount);
				}

				delInvis();
				if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
					removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
					removeSkillEffect(L1SkillId.PHANTASM);
				}
			} else if (damage < 0) {
				if (attacker instanceof L1PcInstance) {
					L1PinkName.onHelp(this, attacker);
				}
			}
			if (getInventory().checkEquipped(145) || getInventory().checkEquipped(149)) {
				damage *= 1.5;
			}

			int newHp = getCurrentHp() - damage;
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
			} else if (newHp <= 0) {
				L1ItemInstance halpas = get_skill().getHalpas();
				if (halpas != null && !get_skill().isHalpasTime()) {
					long hour = ((22 - halpas.getEnchantLevel()) * 3600 * 1000);					
					setCurrentHp(getMaxHp());
					get_skill().setHalpasTime(new Timestamp(System.currentTimeMillis() + hour));
					S_EffectLocation el = new S_EffectLocation(getX(), getY(), 16117);
					sendPackets(el);
					broadcastPacket(el);
				}
				else if (isElf() && hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
					this.setCurrentHp(10);
					newHp = 10;
					int newMp = getCurrentMp() - damage;
					if (newMp <= 0) {
						this.setCurrentHp(0);
						death(attacker, true);

					}
					this.setCurrentMp(newMp);
				} else {
					if (isGm()) {
						this.setCurrentHp(getMaxHp());
					} else {
						if (attacker instanceof L1PcInstance) {
							death(attacker, true);
						}

						if (isDeathMatch()) {
							if (getMapId() == 5153) {
								try {
									save();
									beginGhost(getX(), getY(), (short) getMapId(), true);
									sendPackets(new S_ServerMessage(1271));
								} catch (Exception e) {
									e.printStackTrace();
								}
								return;
							}

						} else {
							death(attacker, true);
						}
					}
				}
			}
			/*
			if (newHp <= 10) {
				if (isElf() && hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
					this.setCurrentHp(10);
					newHp = 10;
					int newMp = getCurrentMp() - damage;
					if (newMp <= 0) {
						this.setCurrentHp(0);
						death(attacker, true);

					}
					this.setCurrentMp(newMp);
				} else if (newHp <= 0) {
					if (isGm()) {
						setCurrentHp(getMaxHp());
					} else {
						if (attacker instanceof L1PcInstance) {
							death(attacker, true);
						}

						if (isDeathMatch()) {
							if (getMapId() == 5153) {
								try {
									save();
									beginGhost(getX(), getY(), (short) getMapId(), true);
									sendPackets(new S_ServerMessage(1271));
								} catch (Exception e) {
									e.printStackTrace();
								}
								return;
							}

						} else {
							death(attacker, true);
						}
					}
				}
			}
			if (newHp > 0) {
				this.setCurrentHp(newHp);
			}
			*/
		} else if (!isDead()) {
			System.out.println("■■■■■■■ HP 감소 처리가 올바르지 못한 캐릭발견.※혹은 최초부터 피가 0");
			death(attacker, true);
		}
	}

	public void death(L1Character lastAttacker, boolean deathPenalty) {
		L1Clan clan = getClan();
		if (clan != null) {
			clan.deleteClanRetrieveUser(getId());
		}

		synchronized (this) {
			if (isDead()) {
				return;
			}
			remove_companion();
			if (hasSkillEffect(L1SkillId.STATUS_TOMAHAWK)) {
				killSkillEffectTimer(L1SkillId.STATUS_TOMAHAWK);
			}
			setCurrentHp(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			if (lastAttacker != null && lastAttacker instanceof L1PcInstance) {
				L1PcInstance player = (L1PcInstance) lastAttacker;
				if (getRedKnightClanId() != 0 && player.getRedKnightClanId() != 0) {
					player.addRedKnightKill(1);
					if (player.getRedKnightKill() >= 5) {
						player.setRedKnightKill(0);
						player.setRedKnightDamage(0);
						player.setRedKnightClanId(0);
						player.sendPackets(new S_SystemMessage("붉은 기사단 혈원을 5회 이상 죽여 강제로 탈퇴됩니다."));
					}
				}

				if (getAI() != null) {
					MJBotType type = getAI().getBotType();
					if (type == MJBotType.REDKNIGHT || type == MJBotType.PROTECTOR) {
						broadcastPacket(new S_SkillSound(getId(), 2236));
						return;
					}
				}

				if (is_combat_field()) {
					MJCombatObserver observer = MJCombatLoadManager.getInstance()
							.get_current_observer(get_current_combat_id());
					if (observer == null) {
						set_instance_status(MJEPcStatus.WORLD);
						getKDA().onKill(player, this);
					} else {
						try {
							observer.on_kill(player, this);
							L1World.getInstance()
									.broadcastPacketToAll(new ServerBasePacket[] {
											new S_ChatPacket(String.format("\\aG승리[%s] \\aA=> \\aL패배[%s]",
													player.getName(), getName()), Opcodes.S_MESSAGE),
									new S_SystemMessage("[배틀파워/배틀필드/배틀토너먼트]"), });
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					if (getKDA() != null)
						getKDA().onKill(player, this);
				}
			}
		}
		GeneralThreadPool.getInstance().execute(new Death(lastAttacker, deathPenalty));
	}

	private class Death implements Runnable {
		L1Character _lastAttacker;

		Death(L1Character cha, boolean deathPenalty) {
			_lastAttacker = cha;
			if (GMCommands.IS_PROTECTION) {
			} else {
			}
		}

		public void run() {
			// if (isTeleport()) {
			// GeneralThreadPool.getInstance().schedule(this, 300);
			// return;
			// }

			L1Character lastAttacker = _lastAttacker;
			_lastAttacker = null;
			setCurrentHp(0);
			setGresValid(false);

			stopHpMpRegeneration();

			int targetobjid = getId();
			getMap().setPassable(getLocation(), true);

			int tempchargfx = 0;
			if (hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
				removeSkillEffect(L1SkillId.SHAPE_CHANGE);
				tempchargfx = getCurrentSpriteId();
				setTempCharGfxAtDead(tempchargfx);
			} else {
				setTempCharGfxAtDead(getClassId());
			}

			setCurrentSprite(getClassId());
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(L1PcInstance.this, L1SkillId.CANCELLATION, getId(), getX(), getY(), null, 0,
					L1SkillUse.TYPE_LOGIN);

			if (tempchargfx == 5727 || tempchargfx == 5730 || tempchargfx == 5733 || tempchargfx == 5736) {
				tempchargfx = 0;
			}
			if (tempchargfx != 0)
				sendShape(tempchargfx);
			sendShape(getTempCharGfxAtDead());

			sendPackets(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die));
			broadcastPacket(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die));

			if (lastAttacker != L1PcInstance.this) {
				L1PcInstance player = null;
				if (lastAttacker instanceof L1PcInstance) {
					player = (L1PcInstance) lastAttacker;
					/** 죽였을경우 이팩트 날리기 **/
					// player.sendPackets(new S_SkillSound(player.getId(),
					// 6354));
					// player.broadcastPacket(new S_SkillSound(player.getId(),
					// 6354));
					/** 죽였을경우 이팩트 날리기 **/
				} else if (lastAttacker instanceof MJCompanionInstance) {
					player = ((MJCompanionInstance) lastAttacker).get_master();
				} else if (lastAttacker instanceof L1PetInstance) {
					player = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
				} else if (lastAttacker instanceof L1SummonInstance) {
					player = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
				}
				if (player != null) {
					if (getZoneType() == -1) {
						return;
					}
				}

				boolean sim_ret = simWarResult(lastAttacker);
				if (sim_ret == true) {
					return;
				}
			}

			if (!getMap().isEnabledDeathPenalty()) {
				return;
			}

			L1PcInstance fightPc = null;
			if (lastAttacker instanceof L1PcInstance) {
				fightPc = (L1PcInstance) lastAttacker;
			}
			if (fightPc != null) {
				if (getFightId() == fightPc.getId() && fightPc.getFightId() == getId()) {
					setFightId(0);
					sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
					return;
				}
			}

			if (is_combat_field())
				return;

			// TODO 공성장에선 경험치 하락이 안됨
			boolean castle_ret1 = castleWarResult();
			if (lastAttacker instanceof L1PcInstance) {
				if (!castle_ret1 && getLevel() < Config.NEW_PLAYER && (lastAttacker.getLevel() - getLevel()) >= 10) {
					isExpDrop = false;
					isItemDrop = false;
				}
			}
			if (castle_ret1 == true) {
				sendPackets(new S_ServerMessage(3798));
				// 경험치 손실이 없는 지역 : 경험치가 손실되지 않았습니다.
				return;
			}

			if (GMCommands.IS_PROTECTION) {
				if (getKDA() != null)
					getKDA().onProtection(L1PcInstance.this);
				return;
			}
			if (getZoneType() == 1 || getZoneType() == -1) {
				sendPackets(new S_ServerMessage(3798));
				// 경험치 손실이 없는 지역 : 경험치가 손실되지 않았습니다.
				return;
			}

			if (getLevel() <= 55) {
				isExpDrop = false;
				isItemDrop = false;
				sendPackets(new S_ServerMessage(3804));
				return;
			}

			// TODO 불멸의 가호
			if (lastAttacker instanceof L1PcInstance) {
				if (getInventory().checkItem(4100122)) {
					gahodrop();
					return;
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if (getInventory().checkItem(4100122)) {
						gahodrop();
						return;
					}
				}
			}
			// TODO 고급 불멸의 가호
			if (lastAttacker instanceof L1PcInstance) {
				if (getInventory().checkItem(4100121)) {
					specialgahodrop();
					return;
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if (getInventory().checkItem(4100121)) {
						specialgahodrop();
						return;
					}
				}
			}

			/** 고대의 가호 효과 구현 **/
			if (lastAttacker instanceof L1PcInstance) {
				if ((getMapId() >= 1708 && getMapId() <= 1709) && getInventory().checkEquipped(900022)) { // 착용한
					drop1();
					return;
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if ((getMapId() >= 1708 && getMapId() <= 1709) && getInventory().checkEquipped(900022)) { // 착용한
																												// 아이템
						drop1();
						return;
					}
				}
			}
			if (lastAttacker instanceof L1PcInstance) {
				if (getInventory().checkEquipped(10000)) { // 착용한 아이템
					drop2();
					return;
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if (getInventory().checkEquipped(10000)) { // 착용한 아이템
						drop2();
						return;
					}
				}
			}
			if (lastAttacker instanceof L1PcInstance) {
				if (getInventory().checkEquipped(10001)) { // 착용한 아이템
					drop3();
					return;
				}
			} else {
				int chance = _random.nextInt(100);
				if (chance < 100) {
					if (getInventory().checkEquipped(10001)) { // 착용한 아이템
						drop3();
						return;
					}
				}
			}

			deathPenalty();
			setGresValid(true);

			if (getExpRes() == 0) {// /조우의 가호로 경험치 회복 fix
				if (lastAttacker instanceof L1PcInstance && getLevel() < Config.NEW_PLAYER
						&& (lastAttacker.getLevel() - getLevel()) >= 10) {
				} else {
					setExpRes(1);
				}
			}

			/** 칼질시 경비병 치도록 **/
			if (lastAttacker instanceof L1GuardInstance) {
				if (get_PKcount() > 0) {
					set_PKcount(get_PKcount() - 1);
				}
				setLastPk(null);
			}
			/** 사망시 템&마법 드랍확률. 바포시스템화. */
			if (getInventory().checkEquipped(10001) || getInventory().checkItem(4100121)) {
			} else {
				/*
				 * 캐릭터의 성향치가 lawful,Neutral 일때 고급 불멸의 가호 미소지 상태로 캐릭터 사망시 인벤토리
				 * 아이템 1개가 100% 확률로 소멸됩니다. 캐릭터의 성향치가 Chaotic 일때 고급 불멸의 가호 미소지
				 * 상태로 캐릭터 사망시 현재 인벤토리 아이템 1~3개가 100% 확률로 소멸됩니다.
				 */
				int lawful = getLawful();
				int dropCount = 1;
				if (lawful < 0) {
					dropCount = MJRnd.next(3) + 1;
				}
				caoPenaltyResult(dropCount);

				/*
				 * 
				 * int lostRate = (int) (((getLawful() - 32768D) / 100D - 65D) *
				 * 5D); if (lostRate < 0) { lostRate *= -1; if (getLawful() <
				 * 32766) { lostRate *= 5; }
				 * 
				 * Random random = new Random(); int rnd = random.nextInt(1000)
				 * + 1;
				 * 
				 * if (rnd <= lostRate) { int count = 1; int r = 1; int lawful =
				 * getLawful(); if (lawful >= 0) { // 0 ~ 32767 r = 1; } else if
				 * (lawful >= -10000) { // -1 ~ -10000 이상 r = 2; } else if
				 * (lawful >= -30000) { // 10001 ~ 30000 r = 3; } else { //
				 * -30000 미만 r = 4; } count = MJRnd.next(r); if (count > 0)
				 * caoPenaltyResult(count + 1); } }
				 */
			}

			boolean castle_ret = castleWarResult();
			if (castle_ret == true) {
				return;
			}
		}
	}

	private static final Integer[] _addDropItemIds = new Integer[] { 41921, 3000028, 3000246 };

	private void caoPenaltyResult(int count) {
		/** 공성존에서 드랍안되도록 **/
		int castle_id = L1CastleLocation.getCastleIdByArea(this);
		if (castle_id != 0) {
			return;
		}
		/** 로봇시스템 **/
		if (getAI() != null || getAccessLevel() == Config.GMCODE) {
			return;
		}
		// 라우풀일경우 (100%우선순위)할파스 1000개를 가지고있으면 반만 500 떨구도록
		// 카오일때 (100%우선순위) 할파스 1천개가지고있으면 1천개다 떨구도록
		if (getMapId() >= 53 && getMapId() <= 56 || getMapId() >= 1708 && getMapId() <= 1710 || getMapId() == 15403
				|| getMapId() == 15404 || getMapId() >= 30 && getMapId() <= 36 || getMapId() >= 101 && getMapId() <= 111
				|| getMapId() >= 121 && getMapId() <= 131) {

			if (count > 0) {
				for (Integer i : _addDropItemIds) {
					if (createAddDropItem(i))
						break;
				}
			}
		}
		ArrayList<L1ItemInstance> dropItems = getInventory().getPossibleDropItems();
		int size = 0;
		for (L1ItemInstance item : dropItems) {
			if (item.isEquipped()) {
				getInventory().setEquipped(item, false);
			}
			if (item.getBless() > 3) {
				if (getInventory().removeItem(item, item.isStackable() ? item.getCount() : 1) > 0) {
					sendPackets(new S_ServerMessage(158, item.getLogName()));// 증발
					/** 파일로그저장 **/
					LoggerInstance.getInstance().addItemAction(ItemActionType.del, this, item, count);
					++size;
				}
			} else {
				if (getInventory().tradeItem(item, item.isStackable() ? item.getCount() : 1,
						L1World.getInstance().getInventory(getX(), getY(), getMapId())) != null) {
					sendPackets(new S_ServerMessage(638, item.getLogName()));// 증발
					/** 파일로그저장 **/
					LoggerInstance.getInstance().addItemAction(ItemActionType.DeathDrop, this, item, count);
					++size;
				}
			}
			if (size >= count)
				break;
		}

		/*
		 * for (int i = 0; i < count; i++) { L1ItemInstance item =
		 * getInventory().CaoPenalty(); if (item != null) { if (item.getBless()
		 * > 3) { getInventory().removeItem(item, item.isStackable() ?
		 * item.getCount() : 1); sendPackets(new S_ServerMessage(158,
		 * item.getLogName()));// 증발
		 * 
		 * LoggerInstance.getInstance().addItemAction(ItemActionType.del, this,
		 * item, count); } else { getInventory().tradeItem(item,
		 * item.isStackable() ? item.getCount() : 1,
		 * L1World.getInstance().getInventory(getX(), getY(), getMapId()));
		 * sendPackets(new S_ServerMessage(638, item.getLogName()));// 증발
		 * 
		 * LoggerInstance.getInstance().addItemAction(ItemActionType.del, this,
		 * item, count); } } }
		 */
	}

	private boolean createAddDropItem(int itemId) {
		L1ItemInstance item = getInventory().findItemId(itemId);
		if (item == null || !MJRnd.isWinning(10000, 1000))// 떨굴 확률
			return false;

		int item_count = item.getCount();
		if (getLawful() >= 0)
			item_count = item_count <= 2 ? 1 : item_count / 2;
		getInventory().tradeItem(item, item_count, L1World.getInstance().getInventory(getX(), getY(), getMapId()));
		sendPackets(new S_ServerMessage(638, item.getLogName()));
		LoggerInstance.getInstance().addItemAction(ItemActionType.DeathDrop, this, item, item_count);
		return true;
	}

	// 군주 죽으면 혈원 자동 베르
	public boolean castleWarResult() {
		L1Clan clan = getClan();
		if (clan != null && isCrown() && getId() == clan.getLeaderId()) {
			MJWar war = clan.getCurrentWar();
			if (war != null) {
				WAR_TYPE type = war.getWarType();
				if (type.equals(WAR_TYPE.CASTLE) && war.getOffenseClan(clan.getClanId()) != null) {
					L1Clan defense = war.getDefenseClan();
					clan.outOfWarArea(defense.getCastleId());
					war.notifyEndWar(defense, clan); // 종결
				}
			}
		}

		int castleId = 0;
		boolean isNowWar = false;
		castleId = L1CastleLocation.getCastleIdByArea(this);
		if (castleId != 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		return isNowWar;
	}

	// 군주 죽으면 혈원 자동 베르 //
	public boolean simWarResult(L1Character lastAttacker) {
		if (getClanid() == 0) {
			return false;
		}
		if (Config.SIM_WAR_PENALTY) {
			return false;
		}
		L1PcInstance attacker = null;

		if (lastAttacker instanceof L1PcInstance) {
			attacker = (L1PcInstance) lastAttacker;
		} else if (lastAttacker instanceof MJCompanionInstance) {
			attacker = ((MJCompanionInstance) lastAttacker).get_master();
		} else if (lastAttacker instanceof L1PetInstance) {
			attacker = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
		} else if (lastAttacker instanceof L1SummonInstance) {
			attacker = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
		} else {
			return false;
		}
		L1Clan clan = getClan();
		L1Clan enemy = attacker.getClan();
		if (clan == null || enemy == null || clan.getLeaderId() != getId())
			return false;

		MJWar war = clan.getCurrentWar();
		MJWar enemyWar = enemy.getCurrentWar();
		if (war == null || enemyWar == null || !war.equals(enemyWar))
			return false;

		WAR_TYPE type = war.getWarType();
		if (!type.equals(WAR_TYPE.NORMAL))
			return false;

		war.notifyWinner(enemy, clan);
		war.notifyEndWar(clan, enemy);
		war.dispose();
		return true;
	}

	public void resExp() {
		int oldLevel = getLevel();
		int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		int exp = 0;
		double ratio;

		if (oldLevel < 45)
			ratio = 0.05;
		else if (oldLevel >= 49)
			ratio = 0.025;
		else
			ratio = 0.05 - (oldLevel - 44) * 0.005;

		exp = (int) (needExp * ratio);

		if (exp == 0)
			return;

		addExp(exp);
	}

	public void resExpToTemple() {
		int oldLevel = getLevel();
		int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		int exp = 0;
		double ratio;

		/*
		 * if (oldLevel < 45) ratio = 0.05; else if (oldLevel >= 45 && oldLevel
		 * < 49) ratio = 0.05 - (oldLevel - 44) * 0.005; else if (oldLevel >= 49
		 * && oldLevel < 52) ratio = 0.025; else if (oldLevel == 52) ratio =
		 * 0.026; else if (oldLevel > 52 && oldLevel < 74) ratio = 0.026 +
		 * (oldLevel - 52) * 0.001; else if (oldLevel >= 74 && oldLevel < 79)
		 * ratio = 0.048 - (oldLevel - 73) * 0.0005; else if (oldLevel >= 79)
		 */ratio = 0.025; // 79렙부터 4.9%복구

		exp = (int) (needExp * ratio);
		if (exp == 0)
			return;

		addExp(exp);
	}

	public void deathPenalty() {
		int oldLevel = getLevel();
		int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		int exp = 0;
		if (oldLevel >= 1 && oldLevel < 11)
			exp = 0;
		else if (oldLevel >= 11 && oldLevel < 45)
			exp = (int) (needExp * 0.1);
		else if (oldLevel == 45)
			exp = (int) (needExp * 0.09);
		else if (oldLevel == 46)
			exp = (int) (needExp * 0.08);
		else if (oldLevel == 47)
			exp = (int) (needExp * 0.07);
		else if (oldLevel == 48)
			exp = (int) (needExp * 0.06);
		else if (oldLevel >= 49)
			exp = (int) (needExp * 0.05);

		if (exp == 0)
			return;

		addExp(-exp);
	}

	/*
	 * public int getEr() { int er = 0; if (isKnight() || is전사()) { er =
	 * getLevel() / 4; } else if (isCrown() || isElf()) { er = getLevel() / 8; }
	 * else if (isDragonknight()) { er = getLevel() / 7; } else if (isDarkelf())
	 * { er = getLevel() / 6; } else if (isBlackwizard()) { er = getLevel() / 9;
	 * } else if (isWizard()) { er = getLevel() / 10; }
	 * 
	 * er += (getAbility().getTotalDex() - 8) / 2;
	 * 
	 * int BaseEr = CalcStat.원거리회피(getAbility().getBaseDex());
	 * 
	 * er += BaseEr;
	 * 
	 * if (hasSkillEffect(L1SkillId.STRIKER_GALE)){ er = er / 3; }
	 * 
	 * if (hasSkillEffect(L1SkillId.DRESS_EVASION)) { er += 18; // 12->18변경 } if
	 * (hasSkillEffect(L1SkillId.SOLID_CARRIAGE)) { er += 15; } if
	 * (hasSkillEffect(L1SkillId.MIRROR_IMAGE)) { er += 8; }
	 * 
	 * return er; }
	 */

	public L1BookMark getBookMark(String name) {
		L1BookMark element = null;
		int size = _bookmarks.size();
		for (int i = 0; i < size; i++) {
			element = _bookmarks.get(i);
			if (element == null)
				continue;
			if (element.getName().equalsIgnoreCase(name)) {
				return element;
			}
		}
		return null;
	}

	public L1BookMark getBookMark(int id) {
		L1BookMark element = null;
		int size = _bookmarks.size();
		for (int i = 0; i < size; i++) {
			element = _bookmarks.get(i);
			if (element == null)
				continue;
			if (element.getId() == id) {
				return element;
			}
		}
		return null;
	}

	public int getBookMarkSize() {
		return _bookmarks.size();
	}

	public void addBookMark(L1BookMark book) {
		_bookmarks.add(book);
	}

	public void removeBookMark(L1BookMark book) {
		_bookmarks.remove(book);
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	public void setWeapon(L1ItemInstance weapon) {
		_weapon = weapon;
	}

	public L1ItemInstance getWeaponSwap() {
		return getEquipSlot().getWeaponSwap();
	}

	public L1ItemInstance getArmor() {
		return _armor;
	}

	public void setArmor(L1ItemInstance armor) {
		_armor = armor;
	}

	public L1ItemInstance getSecondWeapon() {
		return _secondweapon;
	}

	public void setSecondWeapon(L1ItemInstance weapon) {
		_secondweapon = weapon;
	}

	public L1Quest getQuest() {
		return _quest;
	}

	public String getClassName() {
		if (isCrown()) {
			return "군주";
		} else if (isKnight()) {
			return "기사";
		} else if (isElf()) {
			return "엘프";
		} else if (isWizard()) {
			return "마법사";
		} else if (isDarkelf()) {
			return "다크엘프";
		} else if (isDragonknight()) {
			return "용기사";
		} else if (isBlackwizard()) {
			return "환술사";
		} else if (is전사()) {
			return "전사";
		}

		return "직업명";
	}

	public int getClassNumber() {
		if (isCrown()) {
			return 0;
		} else if (isKnight()) {
			return 1;
		} else if (isElf()) {
			return 2;
		} else if (isWizard()) {
			return 3;
		} else if (isDarkelf()) {
			return 4;
		} else if (isDragonknight()) {
			return 5;
		} else if (isBlackwizard()) {
			return 6;
		} else {
			return 7;
		}
	}

	public boolean isCrown() {
		return (getClassId() == CLASSID_PRINCE || getClassId() == CLASSID_PRINCESS);
	}

	public boolean isKnight() {
		return (getClassId() == CLASSID_KNIGHT_MALE || getClassId() == CLASSID_KNIGHT_FEMALE);
	}

	public boolean isElf() {
		return (getClassId() == CLASSID_ELF_MALE || getClassId() == CLASSID_ELF_FEMALE);
	}

	public boolean isWizard() {
		return (getClassId() == CLASSID_WIZARD_MALE || getClassId() == CLASSID_WIZARD_FEMALE);
	}

	public boolean isDarkelf() {
		return (getClassId() == CLASSID_DARK_ELF_MALE || getClassId() == CLASSID_DARK_ELF_FEMALE);
	}

	public boolean isDragonknight() {
		return (getClassId() == CLASSID_DRAGONKNIGHT_MALE || getClassId() == CLASSID_DRAGONKNIGHT_FEMALE);
	}

	public boolean isBlackwizard() {
		return (getClassId() == CLASSID_BLACKWIZARD_MALE || getClassId() == CLASSID_BLACKWIZARD_FEMALE);
	}

	public boolean is전사() {
		return (getClassId() == CLASSID_전사_MALE || getClassId() == CLASSID_전사_FEMALE);
	}

	public String getAccountName() {
		return _accountName;
	}

	public void setAccountName(String s) {
		_accountName = s;
	}

	public short getBaseMaxHp() {
		return _baseMaxHp;
	}

	public void addBaseMaxHp(short i) {
		i += _baseMaxHp;
		if (i >= 32767) {
			i = 32767;
		} else if (i < 1) {
			i = 1;
		}
		addMaxHp(i - _baseMaxHp);
		_baseMaxHp = i;
	}

	public int getBaseMaxMp() {
		return _baseMaxMp;
	}

	public void addBaseMaxMp(int i) {
		i += _baseMaxMp;
		if (i >= 32767) {
			i = 32767;
		} else if (i < 0) {
			i = 0;
		}
		addMaxMp(i - _baseMaxMp);
		_baseMaxMp = i;
	}

	public int getOriginalMagicHit() {
		return _originalMagicHit;
	}

	public int getBaseAc() {
		return _baseAc;
	}

	public int getBaseDmgup() {
		return _baseDmgup;
	}

	public int getBaseBowDmgup() {
		return _baseBowDmgup;
	}

	public int getBaseHitup() {
		return _baseHitup;
	}

	public int getBaseBowHitup() {
		return _baseBowHitup;
	}

	public void setBaseMagicHitUp(int i) {
		_baseMagicHitup = i;
	}

	/**마법 적중**/
	public void addBaseMagicHitUp(int i) {
		_baseMagicHitup += i;
	}

	public int getBaseMagicHitUp() {
		return _baseMagicHitup;
	}

	public int getTotalMagicHitup() {
		return getBaseMagicHitUp() + getOriginalMagicHit();
	}

	public void setBaseMagicCritical(int i) {
		_baseMagicCritical = i;
	}

	public int getBaseMagicCritical() {
		return _baseMagicCritical;
	}

	public void addBaseMagicCritical(int i) {
		_baseMagicCritical += i;
	}

	public void setBaseMagicDmg(int i) {
		_baseMagicDmg = i;
	}

	public int getBaseMagicDmg() {
		return _baseMagicDmg;
	}

	private int _PVPMagicDamageReduction = 0;
	    
	public int getPVPMagicDamageReduction() { //PVP마법대미지 감소
	      return _PVPMagicDamageReduction;
	    }

	    public int addPVPMagicDamageReduction(int i) {
	    	 return _PVPMagicDamageReduction += i;
	    }
	
	
	public void setBaseMagicDecreaseMp(int i) {
		_baseMagicDecreaseMp = i;
	}

	public int getBaseMagicDecreaseMp() {
		return _baseMagicDecreaseMp;
	}

	public int getAdvenHp() {
		return _advenHp;
	}

	public void setAdvenHp(int i) {
		_advenHp = i;
	}

	public int getAdvenMp() {
		return _advenMp;
	}

	public void setAdvenMp(int i) {
		_advenMp = i;
	}

	public int getMagicBuffHp() {
		return _magicBuffHp;
	}

	public void setMagicBuffHp(int i) {
		_magicBuffHp = i;
	}

	public int getHighLevel() {
		return _highLevel;
	}

	public void setHighLevel(int i) {
		_highLevel = i;
	}

	public int getBonusStats() {
		return _bonusStats;
	}

	public void setBonusStats(int i) {
		_bonusStats = i;
	}

	public int getElixirStats() {
		return _elixirStats;
	}

	public void setElixirStats(int i) {
		_elixirStats = i;
	}

	public int getElfAttr() {
		return _elfAttr;
	}

	public void setElfAttr(int i) {
		_elfAttr = i;
	}

	public int getExpRes() {
		return _expRes;
	}

	public void setExpRes(int i) {
		_expRes = i;
	}

	public int getPartnerId() {
		return _partnerId;
	}

	public void setPartnerId(int i) {
		_partnerId = i;
	}

	public int getOnlineStatus() {
		return _onlineStatus;
	}

	public void setOnlineStatus(int i) {
		_onlineStatus = i;
	}

	public int getHomeTownId() {
		return _homeTownId;
	}

	public void setHomeTownId(int i) {
		_homeTownId = i;
	}

	public int getContribution() {
		return _contribution;
	}

	public void setContribution(int i) {
		_contribution = i;
	}

	public int getHellTime() {
		return _hellTime;
	}

	public void setHellTime(int i) {
		_hellTime = i;
	}

	private boolean _morning = false;

	public void setMorning(boolean flag) {
		this._morning = flag;
	}

	public boolean getMorning() {
		return _morning;
	}

	public boolean isBanned() {
		return _banned;
	}

	public void setBanned(boolean flag) {
		_banned = flag;
	}

	public int get_food() {
		return _food;
	}

	public void set_food(int i) {
		_food = i;

		/*
		 * if (_food < 225) { sendPackets("\\f2자연회복불가:포만감 상태:(" + _food +
		 * "% : 200%) 입니다.");
		 * sendPackets(S_InventoryIcon.icoEnd(L1SkillId.FOOD_BUFF));
		 * sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.NO_FOOD_BUFF,
		 * 3116, true)); } else {
		 * sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.FOOD_BUFF, 1082,
		 * true)); sendPackets(S_InventoryIcon.icoEnd(L1SkillId.NO_FOOD_BUFF));
		 * }
		 */

	}

	/** 생존의외침 **/
	public void add_food(int i) {
		_food += i;
		if (_food > 225) {
			_food = 225;
			if (getCryOfSurvivalTime() == 0)
				_cryofsurvivaltime = System.currentTimeMillis() / 1000;
		} else if (_food < 1) {
			_food = 1;
		}
	}

	private long _cryofsurvivaltime;

	public long getCryOfSurvivalTime() {
		return _cryofsurvivaltime;
	}

	public void setCryOfSurvivalTime() {
		if (get_food() >= 225) {
			_cryofsurvivaltime = System.currentTimeMillis() / 1000;

		}
	}

	/** 생존의외침 **/

	public L1EquipmentSlot getEquipSlot() {
		return _equipSlot;
	}

	public static L1PcInstance load(String charName) {
		L1PcInstance result = null;
		try {
			result = CharacterTable.getInstance().loadCharacter(charName);
			if (result != null)
				MJLevelBonus.loadCharacterBonus(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void save() {
		try {
			if (isGhost()) {
				return;
			}
			if (noPlayerCK || noPlayerRobot || noPlayerck2)
				return;

			CharacterTable.getInstance().storeCharacter(this);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/** 로봇관련 빽섭 수정 **/
	public void saveInventory() {
		if (noPlayerCK || noPlayerRobot || noPlayerck2)
			return;
		for (L1ItemInstance item : getInventory().getItems()) {
			if (item != null)
				getInventory().saveItem(item, item.getRecordingColumns() != 0 ? L1PcInventory.COL_SAVE_ALL : 0);
		}
	}

	public void setRegenState(int state) {
		if (_mpRegen != null) {
			_mpRegen.setState(state);
		}
		if (_hpRegen != null) {
			_hpRegen.setState(state);
		}
	}

	private int _maxweight = 0;

	public int getMaxWeight() {
		try {
			_maxweight = CalcStat.getMaxWeight(getAbility().getTotalStr(), getAbility().getTotalCon());
			_maxweight += getWeightReduction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _maxweight;
	}

	public boolean isUgdraFruit() {
		return hasSkillEffect(L1SkillId.STATUS_FRUIT);
	}
	
	public boolean isFastMovable() {
		return (hasSkillEffect(L1SkillId.HOLY_WALK) || hasSkillEffect(L1SkillId.MOVING_ACCELERATION));
	}
	
	public boolean isBlood_lust() {
		return hasSkillEffect(L1SkillId.BLOOD_LUST);
	}

	public int getBraveSpeed() {
		// 환술 용기속도
		if (hasSkillEffect(L1SkillId.STATUS_FRUIT))
			return isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 3 : 4;
		return super.getBraveSpeed();
	}
	
	public boolean isBrave() {
		return (hasSkillEffect(L1SkillId.STATUS_BRAVE));
	}

	public boolean isElfBraveMagicShort() {
		return hasSkillEffect(L1SkillId.DANCING_BLADES) || hasSkillEffect(L1SkillId.SAND_STORM);
	}

	public boolean isElfBraveMagicLong() {
		return hasSkillEffect(L1SkillId.HURRICANE);
	}

	public boolean isDragonPearl() {
		return (hasSkillEffect(L1SkillId.STATUS_DRAGON_PEARL) || getPearl() == 1);
	}

	public boolean isElfBrave() {
		return hasSkillEffect(L1SkillId.STATUS_ELFBRAVE);
	}

	public boolean isFruit() {
		return hasSkillEffect(L1SkillId.STATUS_FRUIT);
	}

	private int _pearl;

	public int getPearl() {
		return _pearl;
	}

	public void setPearl(int i) {
		_pearl = i;
	}

	public boolean isInvisDelay() {
		return (invisDelayCounter > 0);
	}

	public void addInvisDelayCounter(int counter) {
		synchronized (_invisTimerMonitor) {
			invisDelayCounter += counter;
		}
	}

	public void beginInvisTimer() {
		// final long DELAY_INVIS = 5000L;//원본
		final long DELAY_INVIS = 1000L;
		
		addInvisDelayCounter(1);
		GeneralThreadPool.getInstance().schedule(new L1PcInvisDelay(getId()), DELAY_INVIS);
	}

	public synchronized void addExp(int exp) {
		if (Config.STANDBY_SERVER)
			return;

		_exp += exp;
		if (_exp > ExpTable.MAX_EXP) {
			_exp = ExpTable.MAX_EXP;
		}
	}

	public synchronized void addExpForReady(int exp) {
		_exp += exp;
		if (_exp > ExpTable.MAX_EXP) {
			_exp = ExpTable.MAX_EXP;
		}
	}

	public synchronized void addContribution(int contribution) {
		_contribution += contribution;
	}

	public void beginExpMonitor() {
		final long INTERVAL_EXP_MONITOR = 500;
		_expMonitorFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new L1PcExpMonitor(getId()), 0L,
				INTERVAL_EXP_MONITOR);
	}

	/** 해당레벨 이상시 조건을 출력한다 **/
	private void levelUp(int gap) {
		boolean isTeleport = false;
		resetLevel();
		if (getLevel() <= 80) {
			quest_level(getLevel());
		}

		/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/
		SC_REST_EXP_INFO_NOTI.send(this);
		MJRankUserLoader.getInstance().onUser(this);

		/** 특정렙 이상 초보혈맹 자동탈퇴 **/
		String BloodName = getClanname();
		if (getLevel() >= Config.신규혈맹보호레벨 && BloodName.equalsIgnoreCase(Config.신규혈맹이름)
				|| BloodName.equalsIgnoreCase("초보")) {
			try {
				// L1Clan clan = L1World.getInstance().findClan(Config.신규혈맹이름);
				L1Clan clan = L1World.getInstance().findClan("신규보호");
				L1PcInstance clanMember[] = clan.getOnlineClanMember();
				String player_name = getName();
				String clan_name = getClanname();
				for (int i = 0; i < clanMember.length; i++) {
					clanMember[i].sendPackets(new S_ServerMessage(ServerMessage.LEAVE_CLAN, player_name, clan_name));
				}
				ClearPlayerClanData(clan);
				clan.removeClanMember(player_name);
				this.start_teleport(this.getX(), this.getY(), this.getMapId(), this.getHeading(), 169, false, false);
				isTeleport = true;
				save();
				saveInventory();
			} catch (Exception e) {
			}
		}
		if (getLevel() > 90 && getTitle().contains(Config.GAME_SERVER_NAME)) {
			setTitle("");
			sendPackets(new S_CharTitle(getId(), ""));
			broadcastPacket(new S_CharTitle(getId(), ""));
		}

		if (getLevel() == 99 && Config.ALT_REVIVAL_POTION) {
			try {
				L1Item l1item = ItemTable.getInstance().getTemplate(43000);
				if (l1item != null) {
					getInventory().storeItem(43000, 1);
					sendPackets(new S_ServerMessage(403, l1item.getName()));
				} else {
					sendPackets(new S_SystemMessage("환생의 물약 입수에 실패했습니다."));
				}
			} catch (Exception e) {
				e.printStackTrace();
				sendPackets(new S_SystemMessage("환생의 물약 입수에 실패했습니다."));
			}
		}

		for (int i = 0; i < gap; i++) {
			short randomHp = CalcStat.increaseHp(getType(), getAbility().getCon());
			int randomMp = CalcStat.increaseMp(getType(), getAbility().getWis());
			addBaseMaxHp(randomHp);
			addBaseMaxMp(randomMp);
		}

		this.setCurrentHp(getMaxHp());
		this.setCurrentMp(getMaxMp());
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();
		if (getLevel() > getHighLevel() && getReturnStat() == 0) {
			setHighLevel(getLevel());
		}

		try {
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}

		L1Quest quest = getQuest();
		// 레벨업(퀘스트 선물 외부화) 1~현재레벨까지 반복하면서 검색
		int lv = getLevel();
		for (int _lv = 1; _lv <= lv; _lv++) {
			CharactersGiftItemTable.Item _levelItem = null;
			CharactersGiftItemTable.Item[] _levelItems = CharactersGiftItemTable.getInstance().getItems(_lv);
			if (_levelItems != null && _levelItems.length > 0) {
				int level_quest_step = quest.get_step(_lv);
				if (level_quest_step != L1Quest.QUEST_END) {
					for (int i = 0; i < _levelItems.length; i++) {
						_levelItem = _levelItems[i];
						if (_levelItem == null)
							continue;
						if (_levelItem.getType() != getType())
							continue;
						createNewItem(this, _levelItem.getItemId(), _levelItem.getCount(), _levelItem.getEnchant(),
								_levelItem.getAttrLevel(), _levelItem.getBless());
					}
					sendPackets(new S_ChatPacket("Level(" + _lv + ")퀘스트를 완료 하였습니다."));
					getQuest().set_end(_lv);
				}
			}
		}
		// TODO 스냅퍼 개방
		int lv59_step = quest.get_step(L1Quest.QUEST_SLOT76);
		if (getLevel() == 59 && lv59_step != L1Quest.QUEST_END) {
			this.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "Lv.59 귀걸이개방이 가능해졌습니다."));
			this.sendPackets(new S_NewCreateItem(S_NewCreateItem.신규패킷10, 0));
		}
		int lv76_step = quest.get_step(L1Quest.QUEST_SLOT76);
		if (getLevel() == 76 && lv76_step != L1Quest.QUEST_END) {
			this.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "Lv.76 반지개방이 가능해졌습니다."));
		}
		int lv81_step = quest.get_step(L1Quest.QUEST_SLOT81);
		if (getLevel() == 81 && lv81_step != L1Quest.QUEST_END) {
			this.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "Lv.81 반지개방이 가능해졌습니다."));
		}

		if (getLevel() >= 51 && getLevel() - 50 > getBonusStats() && getAbility().getAmount() < 210) {
			int upstat = (getLevel() - 50) - (getBonusStats());
			String s = Integer.toString(upstat);
			sendPackets(new S_Message_YN(479, s));
		}
		if (getLevel() > 1 && getLevel() <= Config.Lineage_Buff) {
			sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.HUNTER_BLESS3, 4126, true));
		} else {
			sendPackets(S_InventoryIcon.icoEnd(L1SkillId.HUNTER_BLESS3));
		}

		if (getLevel() > 1 && getLevel() <= Config.Start_Char_Boho) {
			sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Start_BUFF, 3804, true));
		} else {
			sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Start_BUFF));
		}

		if (getLevel() >= 99) { // 천상의 계곡
			if (getMapId() == 1911 || getMapId() == 1912) {
				this.start_teleport(33438, 32805, 4, 5, 169, true, false);
				isTeleport = true;
			}
		}
		if (getLevel() >= Config.New_Cha1) { // 말하는섬 던전
			if ((getMapId() >= 1 && getMapId() <= 2) || getMapId() == 785) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				this.start_teleport(loc[0], loc[1], loc[2], this.getHeading(), 169, true, false);
				isTeleport = true;
			}
		}
		if (getLevel() >= 70) { // 본섭은 52렙까지 가능하다.
			if (getMapId() == 777) { // 버림받은 사람들의 땅(그림자의 신전)
				this.start_teleport(34043, 32184, 4, 5, 169, true, false);
				isTeleport = true;
			} else if (getMapId() == 778 || getMapId() == 779) {
				this.start_teleport(32608, 33178, 4, 5, 169, true, false);
				isTeleport = true;
			} else if (getMapId() == 2010) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_SILVER_KNIGHT_TOWN);
				this.start_teleport(loc[0], loc[1], loc[2], this.getHeading(), 169, true, false);
				isTeleport = true;
			}
		}

		/*
		 * if (getZoneType() == 1) { startEinhasadTimer(); } }
		 */

		// TODO 캐릭터 레벨별 보상
		int lvlBonus = getCharLevelBonus();
		int lvlBonusOrgn = lvlBonus;
		if (getLevel() >= 82 && (lvlBonus & BONUS_LEVEL_80) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1966), 500L);
			getInventory().storeItem(1000004, 1, true);
			// 80 bonus
			lvlBonus |= BONUS_LEVEL_80;
		}
		if (getLevel() >= 84 && (lvlBonus & BONUS_LEVEL_82) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1967), 500L);
			getInventory().storeItem(1000004, 3, true);
			// 82 bonus
			lvlBonus |= BONUS_LEVEL_82;
		}
		if (getLevel() >= 86 && (lvlBonus & BONUS_LEVEL_84) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1968), 500L);
			getInventory().storeItem(1000004, 5, true);
			// 84 bonus
			lvlBonus |= BONUS_LEVEL_84;
		}
		if (getLevel() >= 88 && (lvlBonus & BONUS_LEVEL_86) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1969), 500L);
			getInventory().storeItem(1000004, 7, true);
			// 86 bonus
			lvlBonus |= BONUS_LEVEL_86;
		}
		if (getLevel() >= 89 && (lvlBonus & BONUS_LEVEL_88) == 0) {
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1970), 500L);
			getInventory().storeItem(1000004, 10, true);
			// 88 bonus
			lvlBonus |= BONUS_LEVEL_88;
		}
		if (getLevel() == 90 && (lvlBonus & BONUS_LEVEL_90) == 0) {
			sendPackets("레벨 90을 달성하여, 마을로 텔레포트 합니다.(기감 상아탑 시간 초기화)");
			isTeleport = true;
			start_teleport(33437, 32813, 4, getHeading(), 169, false, false);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					DungeonTimeProgress<?> progress = _dtInfo.remove_dungeon_progress(1);
					if (progress != null) {
						DungeonTimeProgressLoader.delete(L1PcInstance.this, 1, false);
					}
					progress = _dtInfo.remove_dungeon_progress(7);
					if (progress != null) {
						DungeonTimeProgressLoader.delete(L1PcInstance.this, 7, true);
					}
				}
			}, 500L);
			// DelaySender.send(this, S_ShowCmd.getQuestDesc(3511, 1971), 500L);
			getInventory().storeItem(1000004, 20, true);
			lvlBonus |= BONUS_LEVEL_90;
		}

		if (lvlBonus != lvlBonusOrgn) {
			setCharLevelBonus(lvlBonus);
			MJLevelBonus.storeCharacterBonus(this);
		}

		// TODO 특정 레벨 이상시 변신공속을 위해 텔렉풀기 자동으로 시전
		if (!isFishing() && getLevel() >= Config.Pc_Reload && !isTeleport) {
			start_teleport(getX(), getY(), getMapId(), getHeading(), 169, false, false);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					sendPackets(new S_OwnCharStatus(L1PcInstance.this));
				}
			}, 500L);
		} else
			sendPackets(new S_OwnCharStatus(this));
	}

	private static final int BONUS_LEVEL_80 = 1;
	private static final int BONUS_LEVEL_82 = 2;
	private static final int BONUS_LEVEL_84 = 4;
	private static final int BONUS_LEVEL_86 = 8;
	private static final int BONUS_LEVEL_88 = 16;
	private static final int BONUS_LEVEL_90 = 32;

	private void levelDown(int gap) {
		resetLevel();

		/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/
		SC_REST_EXP_INFO_NOTI.send(this);

		for (int i = 0; i > gap; i--) {
			short randomHp = CalcStat.increaseHp(getType(), getAbility().getCon());
			int randomMp = CalcStat.increaseMp(getType(), getAbility().getWis());
			addBaseMaxHp((short) -randomHp);
			addBaseMaxMp((short) -randomMp);
		}
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();

		if (!isGm() && Config.LEVEL_DOWN_RANGE != 0) {
			if (getHighLevel() - getLevel() == Config.LEVEL_DOWN_RANGE - 1) {
				sendPackets(new S_SystemMessage("한번 더 레벨 다운시 캐릭터가 압류 됩니다."));

			}
			if (!isGm() && getHighLevel() - getLevel() >= Config.LEVEL_DOWN_RANGE) {
				sendPackets(new S_ServerMessage(64));
				sendPackets(new S_Disconnect());
				_log.info(String.format("레벨 다운의 허용 범위를 넘었기 때문에 %s를 강제 절단 했습니다.", getName()));
			}
		}

		try {
			save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendPackets(new S_OwnCharStatus(this));
		start_teleport(getX(), getY(), getMapId(), getHeading(), 169, false, false);
	}

	public void beginGameTimeCarrier() {
		new L1GameTimeCarrier(this).start();
	}

	public boolean isGhost() {
		return _ghost;
	}

	public void setGhost(boolean flag) {
		_ghost = flag;
	}

	public boolean isGhostCanTalk() {
		return _ghostCanTalk;
	}

	public void setGhostCanTalk(boolean flag) {
		_ghostCanTalk = flag;
	}

	public boolean isReserveGhost() {
		return _isReserveGhost;
	}

	public void setReserveGhost(boolean flag) {
		_isReserveGhost = flag;
	}

	public void beginGhost() {
		if (!isGhost()) {
			setGhost(true);
			_ghostSaveLocX = getX();
			_ghostSaveLocY = getY();
			_ghostSaveMapId = getMapId();
			GhostController.getInstance().addMember(this);
		}
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk) {
		beginGhost(locx, locy, mapid, canTalk, 0);
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk, int sec) {
		if (isGhost()) {
			return;
		}
		_ghostSaveLocX = getX();
		_ghostSaveLocY = getY();
		_ghostSaveMapId = getMapId();
		// _ghostSaveHeading = getHeading();
		setGhost(true);
		setGhostCanTalk(canTalk);
		setReserveGhost(false);
		// L1Teleport.teleport(this, locx, locy, mapid, 5, true);
		this.start_teleport(locx, locy, mapid, 5, 169, true, false);
		if (sec > 0) {
			_ghostFuture = GeneralThreadPool.getInstance().schedule(new L1PcGhostMonitor(getId()), sec * 1000);
		}
	}

	public void makeReadyEndGhost() {

		setGhost(false);
		setReserveGhost(true);
		// L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY, (short)
		// _ghostSaveMapId, 5, true);
		this.start_teleport(_ghostSaveLocX, _ghostSaveLocY, _ghostSaveMapId, 5, 169, true, false);
		GhostController.getInstance().removeMember(this);

		// setReserveGhost(true);
		// L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY,
		// _ghostSaveMapId, _ghostSaveHeading, true);
	}

	public void DeathMatchEndGhost() {
		setReserveGhost(true);
		// L1Teleport.teleport(this, 32614, 32735, (short) 4, 5, true);
		this.start_teleport(32614, 32735, 4, 5, 169, true, false);
	}

	public void endGhost() {
		setGhost(false);
		setGhostCanTalk(true);
		setReserveGhost(false);
	}

	public void beginHell(boolean isFirst) {
		if (getMapId() != 666) {
			int locx = 32701;
			int locy = 32777;
			int mapid = 666;
			// short mapid = 666;
			// L1Teleport.teleport(this, locx, locy, mapid, 5, false);
			this.start_teleport(locx, locy, mapid, 5, 169, false, false);
		}

		if (isFirst) {
			if (get_PKcount() <= 10) {
				setHellTime(180);
			} else {
				setHellTime(300 * (get_PKcount() - 100) + 300);
			}
			sendPackets(new S_BlueMessage(552, String.valueOf(get_PKcount()), String.valueOf(getHellTime() / 60)));
		} else {
			sendPackets(new S_BlueMessage(637, String.valueOf(getHellTime())));
		}
		if (_hellFuture == null) {
			_hellFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new L1PcHellMonitor(getId()), 0L, 1000L);
		}
	}

	public void endHell() {
		if (_hellFuture != null) {
			_hellFuture.cancel(false);
			_hellFuture = null;
		}
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ORCISH_FOREST);
		this.start_teleport(loc[0], loc[1], loc[2], 5, 169, true, false);
		try {
			save();
		} catch (Exception ignore) {
		}
	}

	@Override
	public void setPoisonEffect(int effectId) {
		sendPackets(new S_Poison(getId(), effectId));
		if (!isGmInvis() && !isGhost() && !isInvisble()) {
			broadcastPacket(new S_Poison(getId(), effectId));
		}
	}

	@Override
	public void healHp(int pt) {
		super.healHp(pt);
		sendPackets(new S_HPUpdate(this));
	}

	@Override
	public void addDg(int i) {
		super.addDg(i);
		sendPackets(new S_OwnCharAttrDef(this));
	}

	@Override
	public int getKarma() {
		return _karma.get();
	}

	@Override
	public void setKarma(int i) {
		_karma.set(i);
	}

	public void addKarma(int i) {
		synchronized (_karma) {
			_karma.add(i);
		}
	}

	public int getKarmaLevel() {
		return _karma.getLevel();
	}

	public int getKarmaPercent() {
		return _karma.getPercent();
	}

	public Timestamp getLastPk() {
		return _lastPk;
	}

	public void setLastPk(Timestamp time) {
		_lastPk = time;
	}

	public void setLastPk() {
		_lastPk = new Timestamp(System.currentTimeMillis());
	}

	public boolean isWanted() {
		if (_lastPk == null) {
			return false;
		} else if (System.currentTimeMillis() - _lastPk.getTime() > 24 * 3600 * 1000) {
			setLastPk(null);
			return false;
		}
		return true;
	}

	public Timestamp getDeleteTime() {
		return _deleteTime;
	}

	public void setDeleteTime(Timestamp time) {
		_deleteTime = time;
	}

	public Timestamp getLastLoginTime() {
		return _lastLoginTime;
	}

	public void setLastLoginTime(Timestamp time) {
		_lastLoginTime = time;
	}

	@Override
	public int getMagicLevel() {
		return getClassFeature().getMagicLevel(getLevel());
	}

	public int getWeightReduction() {
		return _weightReduction;
	}

	/**무게감소**/
	public void addWeightReduction(int i) {
		_weightReduction += i;
		if (getAI() == null)
			this.sendPackets(new S_Weight(this));
	}

	public int getHasteItemEquipped() {
		return _hasteItemEquipped;
	}

	public void addHasteItemEquipped(int i) {
		_hasteItemEquipped += i;
	}

	public void removeHasteSkillEffect() {
		if (hasSkillEffect(L1SkillId.SLOW))
			removeSkillEffect(L1SkillId.SLOW);
		if (hasSkillEffect(L1SkillId.HASTE))
			removeSkillEffect(L1SkillId.HASTE);
		if (hasSkillEffect(L1SkillId.GREATER_HASTE))
			removeSkillEffect(L1SkillId.GREATER_HASTE);
		if (hasSkillEffect(L1SkillId.STATUS_HASTE))
			removeSkillEffect(L1SkillId.STATUS_HASTE);
	}

	private Timestamp _tamTime;

	public Timestamp getTamTime() {
		return _tamTime;
	}

	public void setTamTime(Timestamp time) {
		_tamTime = time;
	}

	private int _tamreserve;

	public int getTamReserve() {
		return _tamreserve;
	}

	public void setTamReserve(int i) {
		_tamreserve = i;
	}

	private boolean returnStatus = false;
	private boolean returnStatus_Start = false;
	private boolean returnStatus_Levelup = false;

	public boolean isReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(boolean returnStatus) {
		this.returnStatus = returnStatus;
	}

	public boolean isReturnStatus_Start() {
		return returnStatus_Start;
	}

	public void setReturnStatus_Start(boolean returnStatus_Start) {
		this.returnStatus_Start = returnStatus_Start;
	}

	public boolean isReturnStatus_Levelup() {
		return returnStatus_Levelup;
	}

	public void setReturnStatus_Levelup(boolean returnStatus_Levelup) {
		this.returnStatus_Levelup = returnStatus_Levelup;
	}

	public void resetBaseDmgup() {
		int newBaseDmgup = 0;
		int newBaseBowDmgup = 0;
		int newBaseStatDmgup = CalcStat.calcDmgup(getAbility().getBaseStr());
		int newBaseStatBowDmgup = CalcStat.calcBowDmgup(getAbility().getBaseDex());
		if (isKnight() || isDragonknight() || isDarkelf()) {
			newBaseDmgup = getLevel() / 10;
			newBaseBowDmgup = 0;
		} else if (isElf()) {
			newBaseDmgup = 0;
			newBaseBowDmgup = getLevel() / 10;
		}
		addDmgup((newBaseDmgup + newBaseStatDmgup) - _baseDmgup);
		addBowDmgup((newBaseBowDmgup + newBaseStatBowDmgup) - _baseBowDmgup);
		_baseDmgup = newBaseDmgup + newBaseStatDmgup;
		_baseBowDmgup = newBaseBowDmgup + newBaseStatBowDmgup;
	}

	public void resetBaseHitup() {
		int newBaseHitup = 0;
		int newBaseBowHitup = 0;
		int newBaseStatHitup = CalcStat.calcHitup(getAbility().getBaseStr());
		int newBaseStatBowHitup = CalcStat.calcBowHitup(getAbility().getBaseDex());

		if (isCrown()) {
			newBaseHitup = getLevel() / 5;
			newBaseBowHitup = getLevel() / 5;
		} else if (isKnight()) {
			newBaseHitup = getLevel() / 3;
			newBaseBowHitup = getLevel() / 3;
		} else if (isElf()) {
			newBaseHitup = getLevel() / 5;
			newBaseBowHitup = getLevel() / 5;
		} else if (isDarkelf()) {
			newBaseHitup = getLevel() / 3;
			newBaseBowHitup = getLevel() / 3;
		} else if (isDragonknight()) {
			newBaseHitup = getLevel() / 3;
			newBaseBowHitup = getLevel() / 3;
		}
		addHitup((newBaseHitup + newBaseStatHitup) - _baseHitup);
		addBowHitup((newBaseBowHitup + newBaseStatBowHitup) - _baseBowHitup);
		_baseHitup = newBaseHitup + newBaseStatHitup;
		_baseBowHitup = newBaseBowHitup + newBaseStatBowHitup;
	}

	// TODO 사용않함 참고
	public void resetOriginalMagicHit() {
		int originalInt = pc.getTotalInt();
		if (isCrown()) {
			if (originalInt == 12 || originalInt == 13) {
				_originalMagicHit = 1;
			} else if (originalInt >= 14) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isKnight()) {
			if (originalInt == 10 || originalInt == 11) {
				_originalMagicHit = 1;
			} else if (originalInt == 12) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isElf()) {
			if (originalInt == 13 || originalInt == 14) {
				_originalMagicHit = 1;
			} else if (originalInt >= 15) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isDarkelf()) {
			if (originalInt == 12 || originalInt == 13) {
				_originalMagicHit = 1;
			} else if (originalInt >= 14) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isWizard()) {
			if (originalInt >= 14) {
				_originalMagicHit = 1;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isDragonknight()) {
			if (originalInt == 12 || originalInt == 13) {
				_originalMagicHit = 2;
			} else if (originalInt == 14 || originalInt == 15) {
				_originalMagicHit = 3;
			} else if (originalInt >= 16) {
				_originalMagicHit = 4;
			} else {
				_originalMagicHit = 0;
			}
		} else if (isBlackwizard()) {
			if (originalInt >= 13) {
				_originalMagicHit = 1;
			} else {
				_originalMagicHit = 0;
			}
		} else if (is전사()) {
			if (originalInt == 12 || originalInt == 13) {
				_originalMagicHit = 1;
			} else if (originalInt == 14) {
				_originalMagicHit = 2;
			} else {
				_originalMagicHit = 0;
			}
		}
	}

	public void resetBaseAc() {
		int newAc = 10 + CalcStat.calcAc(getAbility().getDex());
		if (_type == 3)
			newAc -= getLevel() / 8;
		else if (_type == 4)
			newAc -= getLevel() / 6;
		else
			newAc -= getLevel() / 7;
		ac.addAc(newAc - _baseAc);
		_baseAc = newAc;
		sendPackets(new S_OwnCharAttrDef(this));
	}

	public void resetBaseMr() {
		resistance.setBaseMr(CalcStat.calcStatMr(_type, getAbility().getTotalWis()));
		sendPackets(new S_SPMR(this));
	}

	public void resetLevel() {
		setLevel(ExpTable.getLevelByExp(_exp));
		if (_hpRegen != null) {
			_hpRegen.updateLevel();
		}
	}

	public void refresh() {
		CheckChangeExp();
		resetLevel();
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseMr();
		resetBaseAc();
	}

	public void checkChatInterval() {
		long nowChatTimeInMillis = System.currentTimeMillis();
		if (_chatCount == 0) {
			_chatCount++;
			_oldChatTimeInMillis = nowChatTimeInMillis;
			return;
		}

		long chatInterval = nowChatTimeInMillis - _oldChatTimeInMillis;
		if (chatInterval > 2000) {
			_chatCount = 0;
			_oldChatTimeInMillis = 0;
		} else {
			if (_chatCount >= 3) {
				setSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED, 120 * 1000);
				sendPackets(new S_SkillIconGFX(36, 120));
				sendPackets(new S_ServerMessage(153));
				_chatCount = 0;
				_oldChatTimeInMillis = 0;
			}
			_chatCount++;
		}
	}

	// TODO 오브젝트 린텍
	/*
	 * private void removeOutOfRangeObjects() { try { for (L1Object known :
	 * getKnownObjects()) { if (known != null) { if (Config.PC_RECOGNIZE_RANGE
	 * == -1) { if (!getLocation().isInScreen(known.getLocation())) {
	 * removeKnownObject(known); sendPackets(new S_RemoveObject(known)); } }
	 * else if (getLocation().getTileLineDistance(known.getLocation()) >
	 * Config.PC_RECOGNIZE_RANGE) { removeKnownObject(known); sendPackets(new
	 * S_RemoveObject(known)); } } } } catch (Exception e) { System.out.println(
	 * "removeOutOfRangeObjects 에러 : " + e); } }
	 */

	// TODO 범위외가 된 인식이 끝난 오브젝트를 제거
	private void removeOutOfRangeObjects() {
		try {
			List<L1Object> known = getKnownObjects();
			for (int i = 0; i < known.size(); i++) {
				if (known.get(i) == null) {
					continue;
				}

				L1Object obj = known.get(i);
				if (!getLocation().isInScreen(obj.getLocation())
						|| (obj instanceof L1NpcInstance && ((L1NpcInstance) obj).isDestroyed())
						|| (obj instanceof L1PcInstance && ((L1PcInstance) obj)._destroyed)) {
					removeKnownObject(obj);
					sendPackets(new S_RemoveObject(obj));
				}
			}
		} catch (Exception e) {
			System.out.println("removeOutOfRangeObjects 에러 : " + e);
		}
	}

	// 오브젝트 인식 처리(버경)
	public void UpdateObject() {
		try {
			try {
				removeOutOfRangeObjects();
				// removeOutOfRangeObjects(17);
			} catch (Exception e) {
				System.out.println("removeOutOfRangeObjects() 에러 : " + e);
			}

			// 화면내의 오브젝트 리스트를 작성
			ArrayList<L1Object> visible2 = L1World.getInstance().getVisibleObjects(this);
			L1NpcInstance npc = null;
			for (L1Object visible : visible2) {
				if (visible == null) {
					continue;
				}
				if (!knownsObject(visible)) {
					visible.onPerceive(this);
				} else {
					if (visible instanceof L1NpcInstance) {
						npc = (L1NpcInstance) visible;
						if (npc.getHiddenStatus() != 0) {
							npc.approachPlayer(this);
						}
					}

				}
			}
		} catch (Exception e) {
			System.out.println("UpdateObject() 에러 : " + e);
		}
	}

	public void CheckChangeExp() {
		int level = ExpTable.getLevelByExp(getExp());
		int char_level = CharacterTable.getInstance().PcLevelInDB(getId());
		if (char_level == 0) {
			return;
		}
		int gap = level - char_level;
		if (gap == 0) {
			sendPackets(new S_OwnCharStatus(this));
			int percent = ExpTable.getExpPercentage(char_level, getExp());
			if (char_level >= 60 && char_level <= 64) {
				if (percent >= 10)
					removeSkillEffect(L1SkillId.레벨업보너스);
			} else if (char_level >= 65) {
				if (percent >= 5) {
					removeSkillEffect(L1SkillId.레벨업보너스);
				}
			}
			return;
		}

		if (gap > 0) {
			levelUp(gap);
			if (getLevel() >= 60) {
				setSkillEffect(L1SkillId.레벨업보너스, 10800000);
				sendPackets(new S_PacketBox(10800, true, true), true);
			}
		} else if (gap < 0) {
			levelDown(gap);
			removeSkillEffect(L1SkillId.레벨업보너스);
		}
	}

	public void LoadCheckStatus() {
		int totalS = getAbility().getAmount();
		int bonusS = getHighLevel() - 50;
		if (bonusS < 0) {
			bonusS = 0;
		}

		int calst = totalS - (bonusS + getElixirStats() + 75);

		if (calst > 0 && !isGm()) {
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(this, L1SkillId.CANCELLATION, getId(), getX(), getY(), null, 0,
					L1SkillUse.TYPE_LOGIN);

			if (getWeapon() != null) {
				getInventory().setEquipped(getWeapon(), false, false, false, false);
			}

			sendPackets(new S_CharVisualUpdate(this));
			sendPackets(new S_OwnCharStatus2(this));

			for (L1ItemInstance armor : getInventory().getItems()) {
				for (int type = 0; type <= 12; type++) {
					if (armor != null) {
						getInventory().setEquipped(armor, false, false, false, false);
					}
				}
			}

			setReturnStat(getExp());
			sendPackets(new S_SPMR(this));
			sendPackets(new S_OwnCharAttrDef(this));
			sendPackets(new S_OwnCharStatus2(this));
			sendPackets(new S_ReturnedStat(this, S_ReturnedStat.START));
			try {
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void CheckStatus() {
		int totalS = ability.getAmount();
		int bonusS = getLevel() - 50;
		if (bonusS < 0) {
			bonusS = 0;
		}

		int calst = totalS - (bonusS + getElixirStats() + 75);

		if (calst > 0 && !isGm()) {
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(this, L1SkillId.CANCELLATION, getId(), getX(), getY(), null, 0,
					L1SkillUse.TYPE_LOGIN);

			if (getWeapon() != null) {
				getInventory().setEquipped(getWeapon(), false, false, false, false);
			}

			sendPackets(new S_CharVisualUpdate(this));
			sendPackets(new S_OwnCharStatus2(this));

			for (L1ItemInstance armor : getInventory().getItems()) {
				for (int type = 0; type <= 12; type++) {
					if (armor != null) {
						getInventory().setEquipped(armor, false, false, false, false);
					}
				}
			}

			setReturnStat(getExp());
			sendPackets(new S_SPMR(this));
			sendPackets(new S_OwnCharAttrDef(this));
			sendPackets(new S_OwnCharStatus2(this));
			sendPackets(new S_ReturnedStat(this, S_ReturnedStat.START));
			try {
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public long TamTime() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Timestamp tamtime = null;
		long time = 0;
		long sysTime = System.currentTimeMillis();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"SELECT `TamEndTime` FROM `characters` WHERE account_name = ? ORDER BY `TamEndTime` ASC"); // 케릭터
			pstm.setString(1, getAccountName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				tamtime = rs.getTimestamp("TamEndTime");
				if (tamtime != null) {
					if (sysTime < tamtime.getTime()) {
						time = tamtime.getTime() - sysTime;
						break;
					}
				}
			}
			return time;
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			return time;
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getRankLevel() {
		return MJRankUserLoader.getInstance().getRankLevel(this);
	}

	public int tamcount() {
		Connection con = null;
		Connection con2 = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;
		Timestamp tamtime = null;
		int count = 0;
		long sysTime = System.currentTimeMillis();
		int char_objid = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `characters` WHERE account_name = ?"); // 케릭터
			// 테이블에서
			// 골라와서
			pstm.setString(1, getAccountName());
			rs = pstm.executeQuery();
			while (rs.next()) {
				tamtime = rs.getTimestamp("TamEndTime");
				char_objid = rs.getInt("objid");
				if (tamtime != null) {
					if ((sysTime / 1000) + 2 <= (tamtime.getTime() / 1000)) {
						count++;
					} else {
						if (Tam_wait_count(char_objid) != 0) {
							int day = Nexttam(char_objid);
							if (day != 0) {
								Timestamp deleteTime = null;
								deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7일
								// deleteTime = new Timestamp(sysTime +
								// 1000*60);//7일

								if (getId() == char_objid) {
									setTamTime(deleteTime);
								}
								con2 = L1DatabaseFactory.getInstance().getConnection();
								pstm2 = con2.prepareStatement(
										"UPDATE `characters` SET TamEndTime=? WHERE account_name = ? AND objid = ?"); // 케릭터
								// 테이블에서
								// 군주만
								// 골라와서
								pstm2.setTimestamp(1, deleteTime);
								pstm2.setString(2, getAccountName());
								pstm2.setInt(3, char_objid);
								pstm2.executeUpdate();
								tamdel(char_objid);
								count++;
							}
						}
					}
				}
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			return count;
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm2);
			SQLUtil.close(con2);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void tamdel(int objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from Tam where objid = ? order by id asc limit 1");
			pstm.setInt(1, objectId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int Nexttam(int objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int day = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT day FROM `tam` WHERE objid = ? order by id asc limit 1"); // 케릭터
			// 테이블에서
			// 군주만
			// 골라와서
			pstm.setInt(1, objectId);
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

	public int Tam_wait_count(int charid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `tam` WHERE objid = ?");
			pstm.setInt(1, charid);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count = getId();
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			return count;
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void cancelAbsoluteBarrier() { // 아브소르트바리아의 해제
		if (hasSkillEffect(ABSOLUTE_BARRIER)) {
			removeSkillEffect(ABSOLUTE_BARRIER);
		}
	}
	
	public void cancelMoebius() {
		if (hasSkillEffect(L1SkillId.MOEBIUS)) {
			removeSkillEffect(L1SkillId.MOEBIUS);
		}
	}

	public int get_PKcount() {
		return _PKcount;
	}

	public void set_PKcount(int i) {
		_PKcount = i;
	}

	public int getClanid() {
		return _clanid;
	}

	public void setClanid(int i) {
		_clanid = i;
	}

	public String getClanname() {
		return clanname;
	}

	public void setClanname(String s) {
		clanname = s;
	}

	public int getRedKnightClanId() {
		return _redKnightClanId;
	}

	public void setRedKnightClanId(int i) {
		_redKnightClanId = i;
	}

	public int getRedKnightDamage() {
		return _redKnightDamage;
	}

	public void setRedKnightDamage(int i) {
		_redKnightDamage = i;
	}

	public void addRedKnightDamage(int i) {
		_redKnightDamage += i;
	}

	public int getRedKnightKill() {
		return _redKnightKill;
	}

	public void setRedKnightKill(int i) {
		_redKnightKill = i;
	}

	public void addRedKnightKill(int i) {
		_redKnightKill += i;
	}

	private String _sealingPW; //

	public String TempQuiz = "";

	public String getSealingPW() {
		return _sealingPW;
	}

	public void setSealingPW(String s) {
		_sealingPW = s;
	}

	int _sealScrollTime;

	public void setSealScrollTime(int sealScrollTime) {
		_sealScrollTime = sealScrollTime;
	}

	public int getSealScrollTime() {
		return _sealScrollTime;
	}

	int _sealScrollCount;

	public void setSealScrollCount(int sealScrollCount) {
		_sealScrollCount = sealScrollCount;
	}

	public int getSealScrollCount() {
		return _sealScrollCount;
	}

	public L1Clan getClan() {
		return L1World.getInstance().getClan(getClanid());
	}

	public int getClanRank() {
		return _clanRank;
	}

	public void setClanRank(int i) {
		_clanRank = i;
	}

	public byte get_sex() {
		return _sex;
	}

	public void set_sex(int i) {
		_sex = (byte) i;
	}

	/** 나이설정 **/
	public int getAge() {
		return _age;
	}

	public void setAge(int i) {
		_age = i;
	}

	public boolean isGm() {
		return _gm;
	}

	public void setGm(boolean flag) {
		_gm = flag;
	}

	public boolean isMonitor() {
		return _monitor;
	}

	public void setMonitor(boolean flag) {
		_monitor = flag;
	}

	public int getDamageReductionByArmor() {
		return _damageReductionByArmor;
	}

	public void addDamageReductionByArmor(int i) {
		_damageReductionByArmor += i;
	}

	public int getDamageReductionIgnore() {
		return _damageReductionIgnore;
	}

	public void addDamageReductionIgnore(int i) {
		_damageReductionIgnore += i;
	}

	public int getDamageReduction() {
		return _DamageReduction;
	}

	public void addDamageReduction(int i) {
		_DamageReduction += i;
	}

	public int pvp_defense;

	public int get_pvp_defense() {
		return pvp_defense;
	}

	public void set_pvp_defense(int i) {
		this.pvp_defense = CommonUtil.get_current(i, 0, 60); // 이거 문제될껀데 흠;;pvp요?
	}

	public int getBowDmgRate() {
		return _bowDmgRate;
	}

	public void addBowDmgRate(int i) {
		_bowDmgRate += i;
	}

	public int getDmgRate() {
		return _DmgRate;
	}

	public void addDmgRate(int i) {
		_DmgRate += i;
	}

	public int getBowHitRate() {
		return _bowHitRate;
	}

	public void addBowHitRate(int i) {
		_bowHitRate += i;
	}

	public int getHitRate() {
		return _HitRate;
	}

	public void addHitRate(int i) {
		_HitRate += i;
	}

	public int getDmgupByArmor() {
		return _DmgupByArmor;
	}

	public void addDmgupByArmor(int i) {
		_DmgupByArmor += i;
	}

	public int getBowDmgupByArmor() {
		return _bowDmgupByArmor;
	}

	public void addBowDmgupByArmor(int i) {
		_bowDmgupByArmor += i;
	}

	private void setGresValid(boolean valid) {
		_gresValid = valid;
	}

	public boolean isGresValid() {
		return _gresValid;
	}

	public long getFishingTime() {
		return _fishingTime;
	}

	public void setFishingTime(long i) {
		_fishingTime = i;
	}

	public boolean isFishing() {
		return _isFishing;
	}

	public boolean isFishingReady() {
		return _isFishingReady;
	}

	public void setFishing(boolean flag) {
		_isFishing = flag;
	}

	public void setFishingReady(boolean flag) {
		_isFishingReady = flag;
	}

	public int getCookingId() {
		return _cookingId;
	}

	public void setCookingId(int i) {
		_cookingId = i;
	}

	public int getDessertId() {
		return _dessertId;
	}

	public void setDessertId(int i) {
		_dessertId = i;
	}

	/** 패키지상점 **/
	private int CashStep = 0;

	public int getCashStep() {
		return CashStep;
	}

	public void setCashStep(int cashStep) {
		CashStep = cashStep;
	}

	/** 패키지상점 **/

	/** 로봇 시작 **/
	private int teleportTime = 0;
	private int teleportTime2 = 0;
	private int skillTime = 0;
	private int skillTime2 = 0;
	private long _quiztime = 0;
	private long _quiztime2 = 0;
	private long _quiztime3 = 0;
	// private int currentTeleportCount = 0;
	private int currentSkillCount = 0;
	private int currentSkillCount2 = 0;

	// 콤보시스템
	private int comboCount;

	public long getQuizTime() {
		return _quiztime;
	}

	public void setQuizTime(long l) {
		_quiztime = l;
	}

	public long getQuizTime2() {
		return _quiztime2;
	}

	public void setQuizTime2(long l) {
		_quiztime2 = l;
	}

	public long getQuizTime3() {
		return _quiztime3;
	}

	public void setQuizTime3(long l) {
		_quiztime3 = l;
	}

	public int getTeleportTime() {
		return teleportTime;
	}

	public void setTeleportTime(int teleportTime) {
		this.teleportTime = teleportTime;
	}

	public int getTeleportTime2() {
		return teleportTime2;
	}

	public void setTeleportTime2(int teleportTime2) {
		this.teleportTime2 = teleportTime2;
	}

	public int getSkillTime2() {
		return skillTime2;
	}

	public void setSkillTime2(int skillTime2) {
		this.skillTime2 = skillTime2;
	}

	public int getSkillTime() {
		return skillTime;
	}

	public void setSkillTime(int skillTime) {
		this.skillTime = skillTime;
	}

	/*
	 * public int getCurrentTeleportCount() { return currentTeleportCount; }
	 * 
	 * public void setCurrentTeleportCount(int currentTeleportCount) {
	 * this.currentTeleportCount = currentTeleportCount; }
	 */

	public int getCurrentSkillCount() {
		return currentSkillCount;
	}

	public void setCurrentSkillCount(int currentSkillCount) {
		this.currentSkillCount = currentSkillCount;
	}

	public int getCurrentSkillCount2() {
		return currentSkillCount2;
	}

	public void setCurrentSkillCount2(int currentSkillCount2) {
		this.currentSkillCount2 = currentSkillCount2;
	}

	/** 로봇 종료 **/

	// public int getTeleportX() {
	// return _teleportX;
	// }
	//
	// public void setTeleportX(int i) {
	// _teleportX = i;
	// }
	//
	// public int getTeleportY() {
	// return _teleportY;
	// }
	//
	// public void setTeleportY(int i) {
	// _teleportY = i;
	// }
	//
	// public short getTeleportMapId() {
	// return _teleportMapId;
	// }
	//
	// public void setTeleportMapId(short i) {
	// _teleportMapId = i;
	// }
	//
	// public int getTeleportHeading() {
	// return _teleportHeading;
	// }
	//
	// public void setTeleportHeading(int i) {
	// _teleportHeading = i;
	// }

	public int getTempCharGfxAtDead() {
		return _tempCharGfxAtDead;
	}

	public void setTempCharGfxAtDead(int i) {
		_tempCharGfxAtDead = i;
	}

	public boolean isCanWhisper() {
		return _isCanWhisper;
	}

	public void setCanWhisper(boolean flag) {
		_isCanWhisper = flag;
	}

	public boolean isShowTradeChat() {
		return _isShowTradeChat;
	}

	public void setShowTradeChat(boolean flag) {
		_isShowTradeChat = flag;
	}

	public boolean isShowWorldChat() {
		return _isShowWorldChat;
	}

	public void setShowWorldChat(boolean flag) {
		_isShowWorldChat = flag;
	}

	public int getFightId() {
		return _fightId;
	}

	public void setFightId(int i) {
		_fightId = i;
	}

	public void setDeathMatch(boolean i) {
		this.isDeathMatch = i;
	}

	public boolean isDeathMatch() {
		return isDeathMatch;
	}

	public boolean isSupporting() {
		return _isSupporting;
	}

	public void setSupporting(boolean flag) {
		_isSupporting = flag;
	}

	public int getCallClanId() {
		return _callClanId;
	}

	public void setCallClanId(int i) {
		_callClanId = i;
	}

	public int getCallClanHeading() {
		return _callClanHeading;
	}

	public void setCallClanHeading(int i) {
		_callClanHeading = i;
	}

	/** 바포메트 시스템 **/// 여기부터0612
	// private int m_bapo_level = L1PcExpMonitor.NONE_STATE_BAPO_LEVEL;
	// public void set_bapo_level(int bapo_level){
	// m_bapo_level = bapo_level;
	// }
	//
	// public int get_bapo_level(){
	// return m_bapo_level;
	// }//여기까지0612

	/** 코마버프 시작 **/
	private int _deathmatch;

	public int getDeathMatchPiece() {
		return _deathmatch;
	}

	public void setDeathMatchPiece(int i) {
		_deathmatch = i;
	}

	private int _petrace;

	public int getPetRacePiece() {
		return _petrace;
	}

	public void setPetRacePiece(int i) {
		_petrace = i;
	}

	private int _ultimatebattle;

	public int getUltimateBattlePiece() {
		return _ultimatebattle;
	}

	public void setUltimateBattlePiece(int i) {
		_ultimatebattle = i;
	}

	private int _petmatch;

	public int getPetMatchPiece() {
		return _petmatch;
	}

	public void setPetMatchPiece(int i) {
		_petmatch = i;
	}

	private int _ghosthouse;

	public int getGhostHousePiece() {
		return _ghosthouse;
	}

	public void setGhostHousePiece(int i) {
		_ghosthouse = i;
	}

	/** 코마버프 끝 **/
	/** 인챈트 버그 예외 처리 */
	private int _enchantitemid = 0;

	public int getLastEnchantItemid() {
		return _enchantitemid;
	}

	/** 장신구인첸트리뉴얼 **/
	public int _accessoryHeal = 0;

	public int getAccessoryHeal() {
		return _accessoryHeal;
	}

	public void setAccessoryHeal(int i) {
		_accessoryHeal = i;
	}

	public void addAccessoryHeal() {
		_accessoryHeal += 1;
	}

	public void setLastEnchantItemid(int i, L1ItemInstance item) {
		// 혹시모를 방지 임시추가
		if (getLastEnchantItemid() == i && i != 0) {
			sendPackets(new S_Disconnect());
			getInventory().removeItem(item, item.getCount());
			return;
		}
		_enchantitemid = i;
	}

	/*
	 * private int _healpotion = 0; // 물약 회복량
	 * 
	 * public int getHealPotion() { return _healpotion; }
	 * 
	 * public void addHealPotion(int value) { _healpotion += value; }
	 */
	/** 미니게임 **/
	// 주사위
	private boolean _isGambling = false;

	public boolean isGambling() {
		return _isGambling;
	}

	public void setGambling(boolean flag) {
		_isGambling = flag;
	}

	private int _gamblingmoney = 0;

	public int getGamblingMoney() {
		return _gamblingmoney;
	}

	public void setGamblingMoney(int i) {
		_gamblingmoney = i;
	}

	// 소막
	private boolean _isGambling3 = false;

	public boolean isGambling3() {
		return _isGambling3;
	}

	public void setGambling3(boolean flag) {
		_isGambling3 = flag;
	}

	private int _gamblingmoney3 = 0;

	public int getGamblingMoney3() {
		return _gamblingmoney3;
	}

	public void setGamblingMoney3(int i) {
		_gamblingmoney3 = i;
	}

	private int monsterkill = 0;

	public int getMonsterkill() {
		return monsterkill;
	}

	public void setMonsterkill(int monster) {
		monsterkill = monster;
		sendPackets(new S_OwnCharStatus(this));
	}

	public void addMonsterKill(int i) {
		monsterkill += i;
		sendPackets(new S_OwnCharStatus(this));
	}

	private ArrayList<String> _cmalist = new ArrayList<String>();

	/**
	 * 클랜 매칭 신청,요청 목록 유저가 사용할땐 배열에 혈맹의 이름을 넣고 군주가 사용할땐 배열에 신청자의 이름을 넣는다.
	 */
	public void addCMAList(String name) {
		if (_cmalist.contains(name)) {
			return;
		}
		_cmalist.add(name);
	}

	public void removeCMAList(String name) {
		if (!_cmalist.contains(name)) {
			return;
		}
		_cmalist.remove(name);
	}

	public ArrayList<String> getCMAList() {
		return _cmalist;
	}

	private ArrayList<Integer> skillList = new ArrayList<Integer>();

	private int _clanMemberId;

	public int getClanMemberId() {
		return _clanMemberId;
	}

	public void setClanMemberId(int i) {
		_clanMemberId = i;
	}

	private String _clanMemberNotes;

	public String getClanMemberNotes() {
		return _clanMemberNotes;
	}

	public void setClanMemberNotes(String s) {
		_clanMemberNotes = s;
	}

	// 고대의 가호 효과 구현
	private void drop1() {
		if ((getMapId() >= 1708 && getMapId() <= 1709) && getInventory().checkEquipped(900022)) { // 착용한
																									// 아이템
			L1ItemInstance drop = ItemTable.getInstance().createItem(3000122); // 드랍
																				// 시킬아이템
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 900022 & item.isEquipped()) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$22172"));
					getInventory().removeItem(item, 1);
					L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
					break;
				}
			}
		}
	}

	// 불멸의 가호
	private void gahodrop() {
		if (getInventory().checkItem(4100122)) {
			L1ItemInstance drop = ItemTable.getInstance().createItem(4100120);
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 4100122) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$27280"));
					getInventory().removeItem(item, 1);
					L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
					break;
				}
			}
		}
	}

	// 고급 불멸의 가호
	private void specialgahodrop() {
		if (getInventory().checkItem(4100121)) {
			L1ItemInstance drop = ItemTable.getInstance().createItem(4100119);
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 4100121) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$26712"));
					getInventory().removeItem(item, 1);
					L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
					break;
				}
			}
		}
	}

	private void drop2() {
		if (getInventory().checkEquipped(10000)) { // 착용한 아이템
			L1ItemInstance drop = ItemTable.getInstance().createItem(738); // 드랍
																			// 시킬아이템
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 10000 & item.isEquipped()) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$25382"));
					getInventory().removeItem(item, 1);
					L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
					break;
				}
			}
		}
	}

	private void drop3() {
		if (getInventory().checkEquipped(10001)) { // 착용한 아이템
			L1ItemInstance drop = ItemTable.getInstance().createItem(739); // 드랍
																			// 시킬아이템
			for (L1ItemInstance item : getInventory().getItems()) {
				if (item.getItemId() == 10001 & item.isEquipped()) {
					sendPackets(new S_ServerMessage(3802));
					sendPackets(new S_ServerMessage(158, "$25384"));
					getInventory().removeItem(item, 1);
					L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
					break;
				}
			}
		}
	}

	public void movePlayerClanData(L1Clan move_clan) {
		setClanid(move_clan.getClanId());
		setClanname(move_clan.getClanName());
		setTitle("");
		setClanMemberNotes("");
		setClanRank(L1Clan.수련);
		save();
		move_clan.addClanMember(getName(), getClanRank(), getLevel(), "", getId(), getType(), getOnlineStatus(), null);
	}

	public void ClearPlayerClanData(L1Clan clan) throws Exception {
		setClanid(0);
		setClanname("");
		setTitle("");
		setClanMemberId(0);
		setClanMemberNotes("");
		if (this != null) {

			String broadcastTitle = null;
			if (hasSkillEffect(L1SkillId.USER_WANTED)) {
				broadcastTitle = WANTED_TITLE;
			}

			sendPackets(new S_CharTitle(getId(), broadcastTitle));
			Broadcaster.broadcastPacket(this, new S_CharTitle(getId(), broadcastTitle));
			sendPackets(new S_ReturnedStat(getId(), 0));
			Broadcaster.broadcastPacket(this, new S_ReturnedStat(getId(), 0));
			sendPackets(new S_ClanName(this, 0, 0));
		}
		setClanRank(0);
		// sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, 0,
		// getName()));
		save();
	}

	public void startEinhasadTimer() {
		synchronized (this) {
			if (_einhasadTimer != null) {
				return;
			}
			_einhasadTimer = new EinhasadTimer();
			GeneralThreadPool.getInstance().schedule(_einhasadTimer, EinhasadTimer.INTERVAL);
		}
	}

	public void stopEinhasadTimer() {
		synchronized (this) {
			if (_einhasadTimer == null) {
				return;
			}
			_einhasadTimer.cancel();
			_einhasadTimer = null;
		}
	}

	EinhasadTimer _einhasadTimer;

	class EinhasadTimer extends RepeatTask {

		public static final int INTERVAL = 15 * 60 * 1000;

		public EinhasadTimer() {
			super(INTERVAL);
		}

		@Override
		public void execute() {
			/*
			 * if (getLevel() < 49) { stopEinhasadTimer(); return; }
			 */
			int einhasad = getAccount().getBlessOfAin();
			if (einhasad >= 2000000) {
				return;
			}
			++einhasad;
			getAccount().setBlessOfAin(einhasad);

			if (getAccount().getBlessOfAin() > 10000) {
				if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
					if (getAccount().getGrangKinAngerTime() > 0) {
						sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1,
								555 + getAccount().getGrangKinAngerStat(), false));
						getAccount().setGrangKinAngerTime(0, L1PcInstance.this);
						getAccount().updateGrangeKinAngerTime();
					}
				}
				SC_REST_EXP_INFO_NOTI.send(L1PcInstance.this);
			}
		}
	}

	public long getFishingShopBuyTime_1() {
		return FishingShopBuyTime_1;
	}

	public void setFishingShopBuyTime_1(long fishingShopBuyTime_1) {
		FishingShopBuyTime_1 = fishingShopBuyTime_1;
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count, int EnchantLevel, int AttEnchantLevel,
			int Bless) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(AttEnchantLevel);
			item.setIdentified(true);
			pc.getInventory().storeItem(item);
			item.setBless(Bless);
			pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
			pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			return true;
		} else {
			return false;
		}
	}

	private int risingUp = 0;

	public int getRisingUp() {
		return risingUp;
	}

	public void setRisingUp(int i) {
		risingUp = i;
	}

	private int impactUp = 0;

	public void setImpactUp(int i) {
		impactUp = i;
	}

	public int getImpactUp() {
		return impactUp;
	}

	private int graceLv = 0;

	public int getGraceLv() {
		return graceLv;
	}

	public void setGraceLv(int i) {
		graceLv = i - 80;
		if (graceLv < 0) {
			graceLv = 0;
		} else if (graceLv > 5) {
			graceLv = 5;
		}
	}

	public void start_teleportForGM(final int x, final int y, final int map, final int heading, final int effect_id,
			boolean effect_check, boolean skill_check) {
		try {
			/** 낚시중 텔이나 소환 당할시 무조건 낚시 종료 **/
			if (isFishing()) {
				FishingTimeController.getInstance().endFishing(this);
			}
			if (FishingTimeController.getInstance().isMember(this)) {
				FishingTimeController.getInstance().endFishing(this);
			}

			if (hasSkillEffect(SHOCK_STUN) || hasSkillEffect(L1SkillId.EMPIRE) || hasSkillEffect(ICE_LANCE)
					|| hasSkillEffect(BONE_BREAK) || hasSkillEffect(EARTH_BIND) || hasSkillEffect(DESPERADO)
					|| isParalyzed() || this.isSleeped()) {
				sendPackets(new S_Paralysis(7, false));
				return;
			}
			teleport = true;
			teleport_x = x;
			teleport_y = y;
			teleport_map = map;
			this.setHeading(heading);
			this.sendPackets(new S_Teleport(this));

			if (getInventory().checkEquipped(900022)) {
				boolean mapcheck = getMapId() >= 1708 && getMapId() <= 1712;
				if (!mapcheck) {
					sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, false));
				}
			}

			clearTemporaryEffect();
			if (skill_check) {
				if (effect_check) {
					S_SkillSound ss = new S_SkillSound(getId(), effect_id);
					sendPackets(ss, false);
					Broadcaster.broadcastPacket(this, ss, false);
				}
				Runnable teleport = () -> {
					L1Teleport.getInstance().doTeleportation(this);
				};
				GeneralThreadPool.getInstance().schedule(teleport, 30);// 10 텔레포트 딜레이
			} else {
				if (effect_check)
					setTemporaryEffect(new S_SkillSound(getId(), effect_id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sendPackets(new S_Paralysis(7, false));
		}
	}

	// ------------------------------ 텔레포트 Start
	// ------------------------------//
	/**
	 * @param x
	 * @param y
	 * @param map
	 * @param heading
	 *            말그대로 방향
	 * @param effect_id
	 *            이펙트 번호를 넣어주면됨.
	 * @param effect_check
	 *            이펙트를 표현 할것인지 true 표현, false 표현하지 않음.
	 * @param skill_check
	 *            스킬 사용시에 true 로 작업 바람.
	 */

	public void start_teleport(final int x, final int y, final int map, final int heading, final int effect_id,
			boolean effect_check, boolean skill_check) {

		try {
			// TODO 혈맹 버프 리뉴얼 2017-11-12
			if (getClan() != null) {
				getClan().deleteClanRetrieveUser(getId());
				if (getClan().getEinhasadBlessBuff() != 0) {
					ClanBuff Buff = ClanBuffTable.getBuffList(getClan().getEinhasadBlessBuff());
					String[] Buffmap = null;
					Buffmap = Buff.buffmaplist.split(",");
					if (getClan().getEinhasadBlessBuff() != 0) {
						for (int j = 0; j < Buffmap.length; j++) {
							int mapid = 0;
							mapid = Integer.parseInt(Buffmap[j]);
							if (map == mapid) {
								int mapnum = mapid;
								if (map == mapnum && getClanBuffMap() == 0) {
									setClanBuffMap(mapnum);
									addEinhasadBlessper(5);
									SC_REST_EXP_INFO_NOTI.send(this);
								}
							}
						}
					}

					if (map != getClanBuffMap() && getClanBuffMap() != 0) {
						setClanBuffMap(0);
						addEinhasadBlessper(-5);
						SC_REST_EXP_INFO_NOTI.send(this);
					}
				}
			}
			// TODO 혈맹 버프 리뉴얼 2017-11-12
			/** 낚시중 텔이나 소환 당할시 무조건 낚시 종료 **/
			if (!noPlayerRobot) {
				if (isFishing()) {
					FishingTimeController.getInstance().endFishing(this);
				}
				if (FishingTimeController.getInstance().isMember(this)) {
					FishingTimeController.getInstance().endFishing(this);
				}
			}

			if (hasSkillEffect(SHOCK_STUN) || hasSkillEffect(L1SkillId.EMPIRE) || hasSkillEffect(ICE_LANCE)
					|| hasSkillEffect(BONE_BREAK) || hasSkillEffect(EARTH_BIND) || hasSkillEffect(DESPERADO)
					|| isParalyzed() || this.isSleeped()) {
				sendPackets(new S_Paralysis(7, false));
				return;
			}

			/** 2016.11.26 MJ 앱센터 LFC **/
			if (isDead()) {
				if (!(MJRaidSpace.getInstance().isInInstance(this) || MJInstanceSpace.isInInstance(this))) {
					sendPackets(new S_Paralysis(7, false));
					return;
				}
			}

			/** 2016.11.26 MJ 앱센터 LFC **/

			teleport = true;
			teleport_x = x;
			teleport_y = y;
			teleport_map = map;
			this.setHeading(heading);
			this.sendPackets(new S_Teleport(this));

			if (getInventory().checkEquipped(900022)) {
				boolean mapcheck = getMapId() >= 1708 && getMapId() <= 1712;
				if (!mapcheck) {
					sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, false));
				}
			}

			if (isPlaySupport()) {
				SupportInfo si = PlaySupportInfoTable.getInstance().getPlaySupportInfo(getMapId());
				if (si == null) {
					setPlaySupport(false);
					sendPackets(new S_ACTION_UI(S_ACTION_UI.FINISH_PLAY_SUPPORT_ACK, 0));
				}
			}

			clearTemporaryEffect();
			if (skill_check) {
				if (effect_check) {
					S_SkillSound ss = new S_SkillSound(getId(), effect_id);
					sendPackets(ss, false);
					Broadcaster.broadcastPacket(this, ss, false);
				}
				Runnable teleport = () -> {
					L1Teleport.getInstance().doTeleportation(this);
				};
				GeneralThreadPool.getInstance().schedule(teleport, 30);// 10 텔레포트 딜레이
			} else {
				if (effect_check)
					setTemporaryEffect(new S_SkillSound(getId(), effect_id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sendPackets(new S_Paralysis(7, false));
		}
	}

	public void start_teleport(final int x, final int y, final int map, final int heading, final int effect_id,
			boolean effect_check, boolean skill_check, boolean isAlways) {
		try {
			L1Clan clan = getClan();
			if (clan != null)
				clan.deleteClanRetrieveUser(getId());

			/** 낚시중 텔이나 소환 당할시 무조건 낚시 종료 **/
			if (isFishing()) {
				FishingTimeController.getInstance().endFishing(this);
			}
			if (FishingTimeController.getInstance().isMember(this)) {
				FishingTimeController.getInstance().endFishing(this);
			}

			if (hasSkillEffect(SHOCK_STUN) || hasSkillEffect(L1SkillId.EMPIRE) || hasSkillEffect(ICE_LANCE)
					|| hasSkillEffect(BONE_BREAK) || hasSkillEffect(EARTH_BIND) || hasSkillEffect(DESPERADO)
					|| isParalyzed() || this.isSleeped()) {
				sendPackets(new S_Paralysis(7, false));
				return;
			}

			/** 2016.11.26 MJ 앱센터 LFC **/
			if (isDead() && !isAlways) {
				if (!(MJRaidSpace.getInstance().isInInstance(this) || MJInstanceSpace.isInInstance(this))) {
					sendPackets(new S_Paralysis(7, false));
					return;
				}
			}
			/** 2016.11.26 MJ 앱센터 LFC **/

			teleport = true;
			teleport_x = x;
			teleport_y = y;
			teleport_map = map;
			this.setHeading(heading);
			this.sendPackets(new S_Teleport(this));

			if (getInventory().checkEquipped(900022)) {
				boolean mapcheck = getMapId() >= 1708 && getMapId() <= 1712;
				if (!mapcheck) {
					sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, false));
				}
			}

			clearTemporaryEffect();
			if (skill_check) {
				if (effect_check) {
					S_SkillSound ss = new S_SkillSound(getId(), effect_id);
					sendPackets(ss, false);
					Broadcaster.broadcastPacket(this, ss, false);
				}
				Runnable teleport = () -> {
					L1Teleport.getInstance().doTeleportation(this);
				};
				GeneralThreadPool.getInstance().schedule(teleport, 30);// 10 텔레포트 딜레이
			} else {
				if (effect_check)
					setTemporaryEffect(new S_SkillSound(getId(), effect_id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sendPackets(new S_Paralysis(7, false));
		}
	}

	public void do_simple_teleport(int x, int y, int mapid) {
		if (isFishing()) {
			FishingTimeController.getInstance().endFishing(this);
		}
		if (FishingTimeController.getInstance().isMember(this)) {
			FishingTimeController.getInstance().endFishing(this);
		}

		if (getInventory().checkEquipped(900022)) {
			boolean mapcheck = getMapId() >= 1708 && getMapId() <= 1712;
			if (!mapcheck) {
				sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, false));
			}
		}

		S_SkillSound ss = new S_SkillSound(getId(), 169);
		sendPackets(ss, false);
		Broadcaster.broadcastPacket(this, ss, false);
		ss.clear();
		L1Teleport.getInstance().doTeleport(L1PcInstance.this, x, y, mapid);
	}

	private boolean teleport;

	public boolean get_teleport() {
		return teleport;
	}

	public void set_teleport(boolean b) {
		this.teleport = b;
		if (teleport) {
		}
	}

	private int teleport_x;

	public int get_teleport_x() {
		return teleport_x;
	}

	public void set_teleport_x(int i) {
		this.teleport_x = i;
	}

	private int teleport_y;

	public int get_teleport_y() {
		return teleport_y;
	}

	public void set_teleport_y(int i) {
		this.teleport_y = i;
	}

	private int teleport_map;

	public int get_teleport_map() {
		return teleport_map;
	}

	public void set_teleport_map(int i) {
		this.teleport_map = i;
	}

	// -- 로봇 텔 관련
	private int teleport_count;

	public int get_teleport_count() {
		return teleport_count;
	}

	public void set_teleport_count(int i) {
		this.teleport_count = i;
	}
	// ------------------------------ 텔레포트 End ------------------------------//

	public SkillData get_skill() {
		return skill_data;
	}

	public void Stat_Reset_All() {
		resetBaseAc();
		resetBaseMr();
		sendPackets(new S_CharStat(this, S_CharStat.STAT_REFRESH));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Str)); // 스탯 상세능력
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Int));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Wis));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Dex));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Con));
		sendPackets(new S_CharStat(S_CharStat.STAT_VIEW, 25)); // 스탯능력치
		sendPackets(new S_CharStat(S_CharStat.STAT_VIEW, 35));
		sendPackets(new S_CharStat(S_CharStat.STAT_VIEW, 45));
		sendPackets(new S_CharStat(this, S_CharStat.STAT_REFRESH));
		sendPackets(new S_SPMR(this));

		if (hasSkillEffect(L1SkillId.PHYSICAL_ENCHANT_STR)) {
			getAbility().addAddedStr((byte) 5);
			int retime = getSkillEffectTimeSec(L1SkillId.PHYSICAL_ENCHANT_STR);
			sendPackets(new S_Strup(this, 5, retime));
			// System.out.println(getAbility().getAddedStr());
		}
		if (hasSkillEffect(L1SkillId.PHYSICAL_ENCHANT_DEX)) {
			getAbility().addAddedDex((byte) 5);
			sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, getDefaultER()));
			int retime = getSkillEffectTimeSec(L1SkillId.PHYSICAL_ENCHANT_DEX);
			sendPackets(new S_Dexup(this, 5, retime));
			// System.out.println(getAbility().getAddedDex());
		}

	}
	
	public ArrayList<Integer> get_자동버프리스트() {
		return _자동버프리스트;
	}

	public void set_자동버프리스트(final ArrayList<Integer> _자동버프리스트) {
		this._자동버프리스트 = _자동버프리스트;
	}

	public int check_자동버프리스트(int skillid) {
		if (_자동버프리스트.contains(skillid)) {
			_자동버프리스트.remove((Object) skillid);
			return 0;
		}
		_자동버프리스트.add(skillid);
		return 1;
	}

	public void remove_자동버프리스트(int skillid) {
		if (_자동버프리스트.contains(skillid)) {
			_자동버프리스트.remove((Object) skillid);
		}
	}

	public boolean is_자동버프리스트(int skillid) {
		return _자동버프리스트.contains(skillid);
	}
	
	
	
	public ArrayList<Integer> get_자동사냐리스트() {
		return _자동버프리스트;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	public ArrayList<Integer> get_자동루팅리스트() {
		return this._자동루팅리스트;
	}

	public void set_자동루팅리스트(final ArrayList<Integer> 자동루팅리스트) {
		this._자동루팅리스트 = 자동루팅리스트;
	}

	public int check_자동루팅리스트(final int itemId) {
		if (this._자동루팅리스트.contains(itemId)) {
			this._자동루팅리스트.remove((Object) itemId);
			return 0;
		}
		this._자동루팅리스트.add(itemId);
		return 1;
	}

	public void remove_자동루팅리스트(final int itemId) {
		if (this._자동루팅리스트.contains(itemId)) {
			this._자동루팅리스트.remove((Object) itemId);
		}
	}

	public ArrayList<Integer> get_자동용해리스트() {
		return this._자동용해리스트;
	}

	public void set_자동용해리스트(final ArrayList<Integer> 자동용해리스트) {
		this._자동용해리스트 = 자동용해리스트;
	}

	public int check_자동용해리스트(final int itemId) {
		if (this._자동용해리스트.contains(itemId)) {
			this._자동용해리스트.remove((Object) itemId);
			return 0;
		}
		this._자동용해리스트.add(itemId);
		return 1;
	}

	public ArrayList<Integer> get_자동물약리스트() {
		return this._자동물약리스트;
	}

	public void set_자동물약리스트(final ArrayList<Integer> _자동물약리스트) {
		this._자동물약리스트 = _자동물약리스트;
	}

	public boolean check_자동물약리스트(final int itemId) {
		return this._자동물약리스트.contains(itemId);
	}

	public boolean is_자동버프세이프티존사용() {
		return this._자동버프세이프티존사용;
	}

	public void set_자동버프세이프티존사용(final boolean _자동버프세이프티존사용) {
		this._자동버프세이프티존사용 = _자동버프세이프티존사용;
	}

	public boolean is_자동버프전투시사용() {
		return this._자동버프전투시사용;
	}

	public void set_자동버프전투시사용(final boolean _자동버프전투시사용) {
		this._자동버프전투시사용 = _자동버프전투시사용;
	}

	public boolean is_자동버프사용() {
		return this._자동버프사용;
	}

	public void set_자동버프사용(final boolean _자동버프사용) {
		this._자동버프사용 = _자동버프사용;
	}

	public boolean is_자동물약사용() {
		return this._자동물약사용;
	}

	public void set_자동물약사용(final boolean _자동물약사용) {
		this._자동물약사용 = _자동물약사용;
	}

	public int get_자동물약퍼센트() {
		return this._자동물약퍼센트;
	}

	public void set_자동물약퍼센트(int _자동물약퍼센트) {
		this._자동물약퍼센트 = _자동물약퍼센트;
	}

	public boolean is_자동숫돌사용() {
		return _자동숫돌사용;
	}

	public void set_자동숫돌사용(final boolean _자동숫돌사용) {
		this._자동숫돌사용 = _자동숫돌사용;
	}

	public boolean isTwoLogin(L1PcInstance c) {// 중복체크 변경
		boolean bool = false;
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			if (target.noPlayerCK)
				continue;

			if (c.getId() != target.getId() && !target.isPrivateShop()) {
				if (c.getNetConnection().getAccountName()
						.equalsIgnoreCase(target.getNetConnection().getAccountName())) {
					bool = true;
					break;
				}
			}
		}
		return bool;
	}

	// --
	private int _attackDelay;

	public int getAttackDelay() {
		return this._attackDelay;
	}

	public void setAttackDelay(int i) {
		this._attackDelay = i;
	}

	private int _attackSpeed;

	public int getAttackSpeed() {
		return this._attackSpeed;
	}

	public void setAttackSpeed() {
		int status = 0;
		// if (Config.POLY_EVENT) {
		// status = 13;
		// } else {
		L1PolyMorph poly = PolyTable.getInstance().find(getCurrentSpriteId());
		int level = getLevel();
		// int p = poly.getMinLevel()
		// if (getPolyLevel() != 0) {
		// level = getPolyLevel();
		// }
		if (poly != null) {
			level = Math.max(level, poly.getMinLevel());
		}
		status = SpriteInformationLoader.levelToIndex(level, getCurrentSpriteId());
		int armorGfx = getPolyIdByEquip();
		if (armorGfx == 11365) {
			status = Math.max(status, 9);
		}

		if (this._attackSpeed != status) {
			sendPackets(new S_CharSpeedUpdate(getId(), status, getLevel()));
			broadcastPacket(new S_CharSpeedUpdate(getId(), status, getLevel()));
		}

		this._attackSpeed = status;
	}

	private int _polyIdByEquip = 0;

	public int getPolyIdByEquip() {
		return this._polyIdByEquip;
	}

	public void setPolyIdByEquip(int i) {
		this._polyIdByEquip = i;
	}

	private L1HateList _autoTargetList = new L1HateList();

	public L1HateList getAutoTargetList() {
		return _autoTargetList;
	}

	public void setAutoTargetList(L1HateList attackList) {
		this._autoTargetList = attackList;
	}

	public void addAutoTargetList(L1Character mon) {
		if (_autoTargetList.containsKey(mon)) {
			return;
		}
		_autoTargetList.add(mon, 0);
	}

	public void removeAutoTargetList(L1Character mon) {
		if (mon == null || !_autoTargetList.containsKey(mon))
			return;
		_autoTargetList.remove(mon);
	}

	private boolean _isAutoSetting;

	public boolean isAutoSetting() {
		return _isAutoSetting;
	}

	public void setAutoSetting(boolean b) {
		_isAutoSetting = b;
	}

	private int _autoPolyId;

	public int getAutoPolyID() {
		return _autoPolyId;
	}

	public void setAutoPolyID(int i) {
		_autoPolyId = i;
	}

	private int _autoLocX;

	public int getAutoLocX() {
		return _autoLocX;
	}

	public void setAutoLocX(int i) {
		_autoLocX = i;
	}

	private int _autoLocY;

	public int getAutoLocY() {
		return _autoLocY;
	}

	public void setAutoLocY(int i) {
		_autoLocY = i;
	}

	private L1Character _autoTarget;

	public L1Character getAutoTarget() {
		return _autoTarget;
	}

	public void setAutoTarget(L1Character mon) {
		_autoTarget = mon;
	}

	private L1Astar _autoAStar = new L1Astar();

	public L1Astar getAutoAstar() {
		return _autoAStar;
	}

	public void setAutoAstar(L1Astar a) {
		_autoAStar = a;
	}

	private int[][] _autoPath = new int[300][2];

	public int[][] getAutoPath() {
		return _autoPath;
	}

	public void setAutoPath(int[][] i) {
		_autoPath = i;
	}

	private int _autoMoveCount = CommonUtil.random(50, 200);

	public int getAutoMoveCount() {
		return _autoMoveCount;
	}

	public void setAutoMoveCount(int i) {
		_autoMoveCount = i;
	}

	private long _autoSkillDelay;

	public long getAutoSkillDelay() {
		return _autoSkillDelay;
	}

	public void setAutoSkillDelay(long i) {
		_autoSkillDelay = i;
	}

	private int _autoStatus;

	public int getAutoStatus() {
		return _autoStatus;
	}

	public void setAutoStatus(int i) {
		_autoStatus = i;
	}

	private L1Node _autoTail;

	public L1Node getAutoTail() {
		return _autoTail;
	}

	public void setAutoTail(L1Node node) {
		_autoTail = node;
	}

	private boolean _autoPathFirst;

	public boolean isAutoPathFirst() {
		return _autoPathFirst;
	}

	public void setAutoPathFirst(boolean a) {
		_autoPathFirst = a;
	}

	public int _autoCurrentPath;

	private int _autoPotion;

	public int getAutoPotion() {
		return _autoPotion;
	}

	public void setAutoPotion(int i) {
		_autoPotion = i;
	}

	private long _autoTimeAttack;

	public long getAutoTimeAttack() {
		return _autoTimeAttack;
	}

	public void setAutoTimeAttack(long time) {
		_autoTimeAttack = time;
	}

	private long _autoTimeMove;

	public long getAutoTimeMove() {
		return _autoTimeMove;
	}

	public void setAutoTimeMove(long time) {
		_autoTimeMove = time;
	}

	private boolean _autoDead;

	public boolean isAutoDead() {
		return _autoDead;
	}

	public void setAutoDead(boolean b) {
		_autoDead = b;
	}

//	private int _autoDeadTime = 5;

	/*public int getAutoDeadTime() {
		return _autoDeadTime;
	}*/

	/*public void setAutoDeadTime(int i) {
		_autoDeadTime = i;
	}*/

	private int _autoTeleportTime;

	public int getAutoTeleportTime() {
		return _autoTeleportTime;
	}

	public void setAutoTeleportime(int i) {
		_autoTeleportTime = i;
	}

	private int _autoRange;

	public int getAutoRange() {
		return _autoRange;
	}

	public void setAutoRange(int i) {
		_autoRange = i;
	}

	private long _autoAiTime;
	public long getAutoAiTime() {
		return _autoAiTime;
	}

	public void setAutoAiTime(long l) {
		_autoAiTime = l;
	}

	private int _autoMapId;

	public int getAutoMapId() {
		return _autoMapId;
	}

	public void setAutoMapId(int i) {
		_autoMapId = i;
	}

	 private int _autosleep;
		public void setAutoSleep(int sleep) {
			_autosleep = sleep;
		}
	
		public int getAutoSleep() {
			return _autosleep;
		}
		
		  private int _automovesleep;
		    public int getAutoDmgMotion() {
		        return _automovesleep;
		    }
		    public void setAutoDmgMotion(int i) {
		    	_automovesleep = i;
		    }
		    
		    private int _autoDeadTime;
		    public int getAutoDeadTime() {
		        return _autoDeadTime;
		    }
		    public void setAutoDeadTime(int i) {
		        _autoDeadTime = i;
		    }
		    
		    private int _autocurrentweapon = 0;
		    public int getAutoCurrentWeapon() {
		        return _autocurrentweapon;
		    }
		    public void setAutoCurrentWeapon(int i) {
		    	_autocurrentweapon = i;
		    }
		    
		    private int _move_gfxid = -1;
			private int _move_weapon = -1;
			private int _move_interval = -1;
			private static final int MOVE = 0;
			private static final int ATTACK = 1;
			private static final int SPELL_DIR = 2;
			private static final int SPELL_NODIR = 3;
			private static final int DMG_MOTION = 4;
			
			private static final double HASTE_RATE = 0.745;
			private static final double WAFFLE_RATE = 0.874;
			private static final double PEARL_RATE = 0.874;
			private static final double FOCUS_RATE = 0.810;
			
			private int calcActionSpeed(int frameCount, int frameRate) {
				return (int) (frameCount * 40 * (24D / frameRate));
			}
		    
			
			
	/** 슬롯체인지 **/
	private List<Integer> _slotItemOne = new ArrayList<Integer>();
	private List<Integer> _slotItemTwo = new ArrayList<Integer>();

	public void addSlotItem(int slotNum, int itemobjid, boolean flag) {
		if (flag) {
			if (slotNum == 0) {
				_slotItemOne.clear();
				for (L1ItemInstance item : getInventory().getItems()) {
					if (item.isEquipped()) {
						_slotItemOne.add(item.getId());
					}
				}
			} else if (slotNum == 1) {
				_slotItemTwo.clear();
				for (L1ItemInstance item : getInventory().getItems()) {
					if (item.isEquipped()) {
						_slotItemTwo.add(item.getId());
					}
				}
			}
		} else {
			if (slotNum == 0) {
				_slotItemOne.add(itemobjid);
			} else if (slotNum == 1) {
				_slotItemTwo.add(itemobjid);
			}
		}
	}

	public List<Integer> getSlotItems(int slotNum) {
		if (slotNum == 0) {
			return _slotItemOne;
		} else if (slotNum == 1) {
			return _slotItemTwo;
		}
		return null;
	}

	public void getChangeSlot(int slotNum) {
		if (slotNum == 0) {
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (!_slotItemOne.contains(item.getId())) {
					if (item.isEquipped()) {
						getInventory().setEquipped(item, false);
					} else {

					}
				}
			}
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (_slotItemOne.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType())) {
								getInventory().setEquipped(item, false);
							}
						}
					} else {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType())) {
								continue;
							}
						}
						getInventory().setEquipped(item, true);
					}
				}
			}
		} else if (slotNum == 1) {
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (!_slotItemTwo.contains(item.getId())) {
					if (item.isEquipped()) {
						getInventory().setEquipped(item, false);
					} else {

					}
				}
			}
			for (L1ItemInstance item : this.getInventory().getItems()) {
				if (_slotItemTwo.contains(item.getId())) {
					if (item.isEquipped()) {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType())) {
								getInventory().setEquipped(item, false);
							}
						}
					} else {
						if (item.getItem().getType2() == 1) {
							if (!L1PolyMorph.isEquipableWeapon(getCurrentSpriteId(), item.getItem().getType())) {
								continue;
							}
						}
						getInventory().setEquipped(item, true);
					}
				}
			}
		}
	}

	private int slotNumber = 0;

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int i) {
		slotNumber = i;
	}

	private long _buffTime;

	public long getBuffTime() {
		return _buffTime;
	}

	public void setBuffTime(long i) {
		_buffTime = i;
	}

	private boolean _isOneTel = false;

	public void setOneTel(boolean a) {
		_isOneTel = a;
	}

	public boolean isOneTel() {
		return _isOneTel;
	}

	private int _robotAIType;

	public int getRobotAIType() {
		return _robotAIType;
	}

	public void setRobotAIType(int i) {
		_robotAIType = i;
	}

	private int _robotPattern = -1;

	public int getRobotPattern() {
		return _robotPattern;
	}

	public void setRobotPattern(int i) {
		_robotPattern = i;
	}

	private long _robotStopTime;

	public long getRobotStopTime() {
		return _robotStopTime;
	}

	public void setRobotStopTime(long time) {
		_robotStopTime = time;
	}

	private long _robotPotionTime;

	public long getRobotPotionTime() {
		return _robotPotionTime;
	}

	public void setRobotPotionTime(long time) {
		_robotPotionTime = time;
	}

	private int attackRange;

	public void setAttackRang(int attackRange) {
		this.attackRange = attackRange;
	}

	public int getAttackRang() {
		return attackRange;
	}

	public boolean isCastle;

	private boolean _isPotyRingMaster = false;

	public boolean isPolyRingMaster() {
		return getInventory().checkItem(900075);
		//return _isPotyRingMaster;
	}

	public void setPolyRingMaster(boolean flag) {
		_isPotyRingMaster = flag;
	}

	private int _elfAttrInitCount;

	public int getElfAttrInitCount() {
		return _elfAttrInitCount;
	}

	public void setElfAttrInitCount(int i) {
		if (i >= 20) {
			i = 20;
		}
		_elfAttrInitCount = i;
	}

	public void addElfAttrInitCount(int i) {
		int count = _elfAttrInitCount + i;
		if (count >= 20) {
			count = 20;
		}
		_elfAttrInitCount = count;
	}

	private int _lastImmuneLevel = 0;

	// TODO 이뮨투함 대미지 감소율 %
	public double getImmuneReduction() {
	//	 System.out.println(_lastImmuneLevel);

		if (_lastImmuneLevel < 36)
			return 0;
		else if (_lastImmuneLevel >= Config.IMMUNE_Level)
			return Config.IMMUNE_DMG;
		else if (_lastImmuneLevel >= Config.IMMUNE_Level1)
			return Config.IMMUNE_DMG1;
		else if (_lastImmuneLevel >= Config.IMMUNE_Level2)
			return Config.IMMUNE_DMG2;
		else if (_lastImmuneLevel >= Config.IMMUNE_Level3)
			return Config.IMMUNE_DMG3;
		else if (_lastImmuneLevel >= Config.IMMUNE_Level4)
			return Config.IMMUNE_DMG4;
		else if (_lastImmuneLevel >= Config.IMMUNE_Level5)
			return Config.IMMUNE_DMG5;
		else if (_lastImmuneLevel >= Config.IMMUNE_Level6)
			return Config.IMMUNE_DMG6;
		else
			return Config.IMMUNE_DMG7;
	}

	public int getLastImmuneLevel() {
		return _lastImmuneLevel;
	}

	public void setLastImmuneLevel(int i) {
		_lastImmuneLevel = i;
	}

	// 발라카스
	private static final long _valaRegenTime = 16000;
	private boolean _isValakasBlessing = false;
	private ValakasBlessing _vBlessing;

	public void startValaBlessing() {
		if (!_isValakasBlessing) {
			_isValakasBlessing = true;
			_vBlessing = new ValakasBlessing(this);
			GeneralThreadPool.getInstance().schedule(_vBlessing, _valaRegenTime);
		}
	}

	public void stopValaBlessing() {
		if (_isValakasBlessing) {
			if (_vBlessing != null) {
				_vBlessing.cancel();
				_vBlessing = null;
			}
			_isValakasBlessing = false;
		}
	}

	public boolean isValakasBlessing() {
		return _isValakasBlessing;
	}

	private boolean _isBossNotify = true;

	public boolean isBossNotify() {
		return _isBossNotify;
	}

	public void setBossNotify(boolean b) {
		_isBossNotify = b;
	}

	/** 2016.11.26 MJ 앱센터 LFC **/
	/** instance space 공간중 어떤 상태에 있는지 여부를 나타냄 **/
	private InstStatus _instStatus = InstStatus.INST_USERSTATUS_NONE;

	public InstStatus getInstStatus() {
		return _instStatus;
	}

	public void setInstStatus(InstStatus status) {
		_instStatus = status;
	}

	/** lfc 중 받은 데미지를 누적한다. **/
	private int _dmgLfc;

	public int getDamageFromLfc() {
		return _dmgLfc;
	}

	public void addDamageFromLfc(int i) {
		_dmgLfc = +i;
	}

	public void setDamageFromLfc(int i) {
		_dmgLfc = i;
	}
	/** 2016.11.26 MJ 앱센터 LFC **/

	/** 2016.12.01 MJ 앱센터 LFC **/
	private int _findMerchantId = 0;

	public int getFindMerchantId() {
		return _findMerchantId;
	}

	public void setFindMerchantId(int i) {
		_findMerchantId = i;
	}

	/** 2016.12.01 MJ 앱센터 LFC **/

	private boolean _isValakasProduct;

	public boolean isValakasProduct() {
		return _isValakasProduct;
	}

	public void setValakasProduct(boolean b) {
		_isValakasProduct = b;
	}

	private MJExpAmplifier _expAmplifier;

	public MJExpAmplifier getExpAmplifier() {
		return _expAmplifier;
	}

	public void setExpAmplifier(MJExpAmplifier amp) {
		if (_expAmplifier != null) {
			if (_expAmplifier.equals(amp))
				return;

			killSkillEffectTimer(L1SkillId.EINHASAD_AMPLIFIER);
			sendPackets(S_InventoryIcon.icoEnd(L1SkillId.EINHASAD_AMPLIFIER));
		}
		_expAmplifier = amp;
		if (amp != null) {
			setSkillEffect(L1SkillId.EINHASAD_AMPLIFIER, -1);
			sendPackets(S_InventoryIcon.iconNewUnLimitAndPriority(0, L1SkillId.EINHASAD_AMPLIFIER, amp.getMessageId(),
					true));
		}
	}

	private S_WorldPutObject _wrdPck;

	public S_WorldPutObject getWorldObject() {
		return _wrdPck;
	}

	public void setWorldObject(S_WorldPutObject wrdPck) {
		_wrdPck = wrdPck;
	}

	/** kill death initialize temporary item instance. **/
	private L1ItemInstance _kdInitItem;

	public L1ItemInstance getKillDeathInitializeItem() {
		return _kdInitItem;
	}

	public void setKillDeathInitializeItem(L1ItemInstance item) {
		_kdInitItem = item;
	}

	public void RenewStat() {
		sendPackets(new S_CharStat(this, S_CharStat.STAT_REFRESH));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Str)); // 스탯 상세능력
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Int));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Wis));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Dex));
		sendPackets(new S_CharStat(this, 1, S_CharStat.Stat_Con));
		sendPackets(new S_CharStat(S_CharStat.STAT_VIEW, 25)); // 스탯능력치
		sendPackets(new S_CharStat(S_CharStat.STAT_VIEW, 35));
		sendPackets(new S_CharStat(S_CharStat.STAT_VIEW, 45));
		sendPackets(new S_CharStat(this, S_CharStat.STAT_REFRESH));
	}

	private boolean _isOutsideChat = true;

	public boolean isOutsideChat() {
		return _isOutsideChat;
	}

	public void setOutSideChat(boolean b) {
		_isOutsideChat = b;
	}

	private long _lastNpcClickMs = 0L;

	public long getLastNpcClickMs() {
		return _lastNpcClickMs;
	}

	public void setLastNpcClickMs(long l) {
		_lastNpcClickMs = l;
	}

	private int _charLevelBonus;

	public int getCharLevelBonus() {
		return _charLevelBonus;
	}

	public void setCharLevelBonus(int i) {
		_charLevelBonus = i;
	}

	public synchronized boolean onStat(String s) throws Exception {
		final int BONUS_ABILITY = getBonusStats();
		if (!(getLevel() - 50 > BONUS_ABILITY))
			return false;

		if (getOnlineStatus() != 1) { // 127 스텟 버그 수정
			sendPackets(new S_Disconnect());
			return false;
		}

		if (s.toLowerCase().equals("str".toLowerCase())) {
			if (getAbility().getStr() < 45 || (getLevel() >= 90 && getAbility().getStr() < 50)) {
				getAbility().addStr((byte) 1); // 소의 STR치에+1
				setBonusStats(BONUS_ABILITY + 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this)); // 케릭정보 업뎃
				sendPackets(new S_Weight(this)); // 무게정보갱신
				save(); // DB에 캐릭터 정보 저장
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("dex".toLowerCase())) {
			if (getAbility().getDex() < 45 || (getLevel() >= 90 && getAbility().getDex() < 50)) {
				getAbility().addDex((byte) 1);
				resetBaseAc();

				setBonusStats(BONUS_ABILITY + 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("con".toLowerCase())) {
			if (getAbility().getCon() < 45 || (getLevel() >= 90 && getAbility().getCon() < 50)) {
				getAbility().addCon((byte) 1);
				setBonusStats(BONUS_ABILITY + 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				sendPackets(new S_Weight(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("int".toLowerCase())) {
			if (getAbility().getInt() < 45 || (getLevel() >= 90 && getAbility().getInt() < 50)) {
				getAbility().addInt((byte) 1);
				setBonusStats(BONUS_ABILITY + 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("wis".toLowerCase())) {
			if (getAbility().getWis() < 45 || (getLevel() >= 90 && getAbility().getWis() < 50)) {
				getAbility().addWis((byte) 1);
				setBonusStats(BONUS_ABILITY + 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else if (s.toLowerCase().equals("cha".toLowerCase())) {
			if (getAbility().getCha() < 45 || (getLevel() >= 90 && getAbility().getCha() < 50)) {
				getAbility().addCha((byte) 1);
				setBonusStats(BONUS_ABILITY + 1);
				sendPackets(new S_OwnCharStatus2(this));
				sendPackets(new S_CharVisualUpdate(this));
				save();
			} else {
				sendPackets(new S_ServerMessage(481));
				throw new Exception(s);
			}
		} else
			throw new Exception(s);

		CheckStatus();
		if (getLevel() >= 51 && getLevel() - 50 > getBonusStats()) {
			if ((getAbility().getStr() + getAbility().getDex() + getAbility().getCon() + getAbility().getInt()
					+ getAbility().getWis() + getAbility().getCha()) < 150) {
				int upstat = (getLevel() - 50) - (getBonusStats());
				String up = Integer.toString(upstat);
				sendPackets(new S_Message_YN(479, up));
			}
		}
		return true;
	}

	private L1ItemInstance _NameInstance;

	public L1ItemInstance getNameInstance() {
		return _NameInstance;
	}

	public void setNameInstance(L1ItemInstance item) {
		_NameInstance = item;
	}

	private int _magicDodgeProb = 0;

	public int getMagicDodgeProbability() {
		return _magicDodgeProb;
	}

	public void setMagicDodgeProbability(int i) {
		_magicDodgeProb = i;
	}

	public void addMagicDodgeProbability(int i) {
		_magicDodgeProb += i;

		sendPackets(new S_ACTION_UI2(S_ACTION_UI2.MAGICEVASION, _magicDodgeProb), true);
	}

	private int _temporaryItemObjectId = -1;

	public int getTemporaryItemObjectId() {
		return _temporaryItemObjectId;
	}

	public void setTemporaryItemObjectId(int i) {
		_temporaryItemObjectId = i;
	}

	public void clearTemporaryItemObjectId() {
		_temporaryItemObjectId = -1;
	}

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_PC : _instanceType;
	}

	@Override
	public long getCurrentSpriteInterval(EActionCodes actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}

	@Override
	public void sendShape(int poly) {
		S_ChangeShape shape = new S_ChangeShape(getId(), poly, getCurrentWeapon());
		sendPackets(shape, false);
		broadcastPacket(shape);
	}

	public void offFishing() {
		if (isFishing()) {
			try {
				setFishing(false);
				setFishingTime(0);
				setFishingReady(false);
				sendPackets(new S_CharVisualUpdate(this));
				Broadcaster.broadcastPacket(this, new S_CharVisualUpdate(this));
				FishingTimeController.getInstance().removeMember(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private CharacterActionExecutor _actionExecutor;

	public void registerActionHandler(int idx, AbstractActionHandler handler) {
		if (_actionExecutor == null)
			_actionExecutor = CharacterActionExecutor.execute(this);
		_actionExecutor.register(idx, handler);
	}

	public void unreigsterActionHandler(int idx) {
		if (_actionExecutor != null)
			_actionExecutor.unregister(idx);
	}

	public boolean hasAction(int idx) {
		return _actionExecutor != null ? _actionExecutor.hasAction(idx) : false;
	}

	private ServerBasePacket _tempEffect;

	public void setTemporaryEffect(ServerBasePacket pck) {
		clearTemporaryEffect();
		_tempEffect = pck;
	}

	public ServerBasePacket getTemporaryEffect() {
		return _tempEffect;
	}

	public void clearTemporaryEffect() {
		if (_tempEffect != null) {
			_tempEffect.clear();
			_tempEffect = null;
		}
	}

	public long lastSpellUseMillis = 0L;
	public int lastSpellUsePending = 0;

	private SC_ATTENDANCE_USER_DATA_EXTEND _attendance_data;

	public void setAttendanceData(SC_ATTENDANCE_USER_DATA_EXTEND attendance_data) {
		_attendance_data = attendance_data;
	}

	public SC_ATTENDANCE_USER_DATA_EXTEND getAttendanceData() {
		return _attendance_data;
	}

	// 타겟티 사용 유무 _isOnTargetEffect
	private boolean _isOnTargetEffect = Config.TARGETGFX;

	public boolean isOnTargetEffect() {
		return _isOnTargetEffect;
	}

	public void setOnTargetEffect(boolean b) {
		_isOnTargetEffect = b;
	}

	private Timestamp _lastTopBless;

	public void setLastTopBless(Timestamp ts) {
		_lastTopBless = ts;
	}

	public Timestamp getLastTopBless() {
		return _lastTopBless;
	}

	private BQSCharacterData _bqsData;

	public BQSCharacterData getBqs() {
		return _bqsData;
	}

	public void setBqs(BQSCharacterData bqs) {
		_bqsData = bqs;
	}

	private MJEPcStatus _instance_status = MJEPcStatus.NONE;
	private int _current_combat_id = 0;
	private int _current_combat_team_id = -1;

	public void set_instance_status(MJEPcStatus instance_status) {
		_instance_status = instance_status;
	}

	public MJEPcStatus get_instance_status() {
		return _instance_status;
	}

	public boolean is_none() {
		return _instance_status.equals(MJEPcStatus.NONE);
	}

	public boolean is_world() {
		return _instance_status.equals(MJEPcStatus.WORLD);
	}

	public boolean is_combat_field() {
		return _instance_status.equals(MJEPcStatus.COMBAT_FIELD);
	}

	public void set_current_combat_id(int current_combat_id) {
		_current_combat_id = current_combat_id;
	}

	public int get_current_combat_id() {
		return _current_combat_id;
	}

	public void set_current_combat_team_id(int current_combat_team_id) {
		_current_combat_team_id = current_combat_team_id;
	}

	public int get_current_combat_team_id() {
		return _current_combat_team_id;
	}

	private boolean _is_non_action = false;

	public boolean is_non_action() {
		return _is_non_action;
	}

	public void set_is_non_action(boolean b) {
		_is_non_action = b;
	}

	private int _mark_status = 0;

	public void set_mark_status(int mark_status) {
		_mark_status = mark_status;
	}

	public int get_mark_status() {
		return _mark_status;
	}

	private MJCaptcha _captcha;

	public MJCaptcha get_captcha() {
		return _captcha;
	}

	public MJCaptcha create_captcha() {
		return (_captcha = MJCaptcha.newInstance(getId()));
	}

	private int _lateral_damage;
	private int _lateral_reduction;
	private int _lateral_magic_rate;

	public void set_lateral_damage(int lateral_damage) {
		_lateral_damage = lateral_damage;
	}

	public void add_lateral_damage(int lateral_damage) {
		_lateral_damage += lateral_damage;
	}

	public int get_lateral_damage() {
		return _lateral_damage;
	}

	public void set_lateral_reduction(int lateral_reduction) {
		_lateral_reduction = lateral_reduction;
	}

	public void add_lateral_reduction(int lateral_reduction) {
		_lateral_reduction += lateral_reduction;
	}

	public int get_lateral_reduction() {
		return _lateral_reduction;
	}

	public void set_lateral_magic_rate(int lateral_magic_rate) {
		_lateral_magic_rate = lateral_magic_rate;
	}

	public void add_lateral_magic_rate(int lateral_magic_rate) {
		_lateral_magic_rate += lateral_magic_rate;
	}

	public int get_lateral_magic_rate() {
		return _lateral_magic_rate;
	}

	public void load_lateral_status() {
		Selector.exec("select * from tb_lateral_status where character_id=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					add_lateral_damage(rs.getInt("lateral_damage"));
					add_lateral_reduction(rs.getInt("lateral_reduction"));
					add_lateral_magic_rate(rs.getInt("lateral_magic_rate"));
				}
			}
		});
	}

	public void delete_lateral_status() {
		set_lateral_damage(0);
		set_lateral_reduction(0);
		set_lateral_magic_rate(0);
		Updator.exec("delete from tb_lateral_status where character_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, getId());
			}
		});
	}

	public void update_lateral_status() {
		Updator.exec(
				"insert into tb_lateral_status set character_id=?, lateral_damage=?, lateral_reduction=?, lateral_magic_rate=? on duplicate key update  lateral_damage=?, lateral_reduction=?, lateral_magic_rate=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, getId());
						pstm.setInt(++idx, get_lateral_damage());
						pstm.setInt(++idx, get_lateral_reduction());
						pstm.setInt(++idx, get_lateral_magic_rate());
						pstm.setInt(++idx, get_lateral_damage());
						pstm.setInt(++idx, get_lateral_reduction());
						pstm.setInt(++idx, get_lateral_magic_rate());
					}
				});
	}

	private DungeonTimeUserInformation _dtInfo = DungeonTimeUserInformation.newInstance();

	public DungeonTimeUserInformation get_dungeon_information() {
		return _dtInfo;
	}

	public DungeonTimeProgress<?> get_progress(DungeonTimeInformation dtInfo) {
		return _dtInfo.get_progress(dtInfo);
	}

	public void dec_dungeon_progress(DungeonTimeInformation dtInfo) {
		_dtInfo.dec_dungeon_progress(this, dtInfo);
	}

	public Collection<DungeonTimeProgress<?>> get_character_progresses() {
		return _dtInfo.get_character_progresses().values();
	}

	public Collection<DungeonTimeProgress<?>> get_account_progresses() {
		return _dtInfo.get_account_progresses().values();
	}

	public void put_dungeon_progress(int timer_id, AccountTimeProgress progress) {
		_dtInfo.put_dungeon_progress(timer_id, progress);
	}

	public void put_dungeon_progress(int timer_id, CharacterTimeProgress progress) {
		_dtInfo.put_dungeon_progress(timer_id, progress);
	}

	public void send_dungeon_progress(DungeonTimeInformation dtInfo) {
		_dtInfo.send_dungeon_progress(this, dtInfo);
	}

	public void initialize_dungeon_progress() {
		_dtInfo.initialize();
	}

	private S_ItemExSelectPacket _select_item;

	public void on_select_item(S_ItemExSelectPacket pck) {
		final int id = pck.get_id();
		_select_item = pck;
		sendPackets(pck);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				S_ItemExSelectPacket pck = _select_item;
				if (pck != null) {
					if (pck.get_id() == id) {
						_select_item = null;
						pck.dispose();
					}
				}
			}
		}, 20000L);
	}

	public S_ItemExSelectPacket get_select_item() {
		S_ItemExSelectPacket pck = _select_item;
		_select_item = null;
		return pck;
	}

	public boolean is_ranking_buff() {
		return hasSkillEffect(L1SkillId.RANK_BUFF_1) || hasSkillEffect(L1SkillId.RANK_BUFF_2)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_3) || hasSkillEffect(L1SkillId.RANK_BUFF_4)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_5) || hasSkillEffect(L1SkillId.RANK_BUFF_6)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_7) || hasSkillEffect(L1SkillId.RANK_BUFF_8)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_9) || hasSkillEffect(L1SkillId.RANK_BUFF_10_STR)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_10_DEX) || hasSkillEffect(L1SkillId.RANK_BUFF_10_INT)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_11_STR) || hasSkillEffect(L1SkillId.RANK_BUFF_11_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_11_INT);
	}

	public boolean is_top_ranker() {
		return hasSkillEffect(L1SkillId.RANK_BUFF_11_STR) || hasSkillEffect(L1SkillId.RANK_BUFF_11_DEX)
				|| hasSkillEffect(L1SkillId.RANK_BUFF_11_INT);
	}

	public boolean isPacketSendOK() {
		if (getNetConnection() == null || getAccount() == null)
			return false;
		if (!isWorld)
			return false;
		if (getAccountName().equals("") || getAccountName().equals("인공지능"))
			return false;
		if (is무인상점())
			return false;
		if (isPrivateShop())
			return false;
		if (getAI() != null)
			return false;
		if (getOnlineStatus() == 0)
			return false;

		return true;
	}

	/** 캐릭정보 자동저장 시스템 **/
	private long lastSavedTime = System.currentTimeMillis();
	private long lastSavedTime_inventory = System.currentTimeMillis();

	public long getlastSavedTime() {
		return lastSavedTime;
	}

	public long getlastSavedTime_inventory() {
		return lastSavedTime_inventory;
	}

	public void setlastSavedTime(long stime) {
		this.lastSavedTime = stime;
	}

	public void setlastSavedTime_inventory(long stime) {
		this.lastSavedTime_inventory = stime;
	}
	/** 캐릭정보 자동저장 시스템 **/

	/** 로또 시스템 **/
	private int _lotto;

	public int getLotto() {
		return _lotto;
	}

	public void setLotto(int i) {
		_lotto = i;
	}

	/** 로또 시스템 **/

	public FastMap<Integer, L1QuestInfo> quest_list = new FastMap<Integer, L1QuestInfo>();
	public Object syncTalkIsland = new Object();

	public L1QuestInfo getQuestList(int id) {
		L1QuestInfo info = quest_list.get(id);
		return info;
	}

	private long _PostDelay;

	public long getPostDelay() {
		return _PostDelay;
	}

	public void setPostDelay(long i) {
		_PostDelay = i;
	}

	private HashMap<Integer, MJPassiveInfo> m_passives;

	public void addPassive(MJPassiveInfo pInfo) {
		if (m_passives == null)
			m_passives = new HashMap<Integer, MJPassiveInfo>();

		m_passives.put(pInfo.getPassiveId(), pInfo);
	}

	public ArrayList<MJPassiveInfo> getPassives() {
		if (m_passives == null)
			return null;
		return new ArrayList<MJPassiveInfo>(m_passives.values());
	}

	public MJPassiveInfo getPassive(int passiveId) {
		if (m_passives == null)
			return null;
		return m_passives.get(passiveId);
	}

	public boolean isPassive(int passiveId) {
		return getPassive(passiveId) != null;
	}

	public boolean isAutoTreeple = false;

	// TODO 혈맹 버프 리뉴얼 2017-11-12
	private int _ClanBuffMap = 0;

	public int getClanBuffMap() {
		return _ClanBuffMap;
	}

	public void setClanBuffMap(int i) {
		_ClanBuffMap = i;
	}

	private long _lastMoveActionMillis;

	public void setLastMoveActionMillis(long lastMoveActionMillis) {
		_lastMoveActionMillis = lastMoveActionMillis;
	}

	public long getLastMoveActionMillis() {
		return _lastMoveActionMillis;
	}

	// 퀘스트 몬스터 시스템
	public FastMap<Integer, L1QuestMonsterInfo> monster_list = new FastMap<Integer, L1QuestMonsterInfo>();

	public L1QuestMonsterInfo getmonsterList(int id) {
		L1QuestMonsterInfo info = monster_list.get(id);
		return info;
	}

	private int _grangKinAngerSafeTime;

	public int getGrangKinAngerSafeTime() {
		return _grangKinAngerSafeTime;
	}

	public void setGrangKinAngerSafeTime(int i) {
		_grangKinAngerSafeTime = i;
	}

	public void addGrangKinAngerSafeTime(int i) {
		int time = _grangKinAngerSafeTime + i;
		_grangKinAngerSafeTime = IntRange.ensure(time, 0, GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_LOGIN_TIME);

		int grangKinAngerStep = 0;
		// System.out.println("현재 그랑카인 세이프티 존 시간 : " + _grangKinAngerSafeTime);

		if (getAccount().getGrangKinAngerStat() == 1) {
			if (_grangKinAngerSafeTime >= GrangKinConfig.GRANG_KIN_ANGER_ONE_STEP_LOGIN_TIME) {
				grangKinAngerStep = 1;
				setGrangKinAngerSafeTime(0);
				getAccount().setGrangKinAngerTime(0, this);
			}
		} else if (getAccount().getGrangKinAngerStat() == 2) {
			if (_grangKinAngerSafeTime >= GrangKinConfig.GRANG_KIN_ANGER_TWO_STEP_LOGIN_TIME) {
				grangKinAngerStep = 2;
				setGrangKinAngerSafeTime(0);
				getAccount().setGrangKinAngerTime(0, this);
			}
		} else if (getAccount().getGrangKinAngerStat() == 3) {
			if (_grangKinAngerSafeTime >= GrangKinConfig.GRANG_KIN_ANGER_THREE_STEP_LOGIN_TIME) {
				grangKinAngerStep = 3;
				setGrangKinAngerSafeTime(0);
				getAccount().setGrangKinAngerTime(0, this);
			}
		} else if (getAccount().getGrangKinAngerStat() == 4) {
			if (_grangKinAngerSafeTime >= GrangKinConfig.GRANG_KIN_ANGER_FOUR_STEP_LOGIN_TIME) {
				grangKinAngerStep = 4;
				setGrangKinAngerSafeTime(0);
				getAccount().setGrangKinAngerTime(0, this);
			}
		} else if (getAccount().getGrangKinAngerStat() == 5) {
			if (_grangKinAngerSafeTime >= GrangKinConfig.GRANG_KIN_ANGER_FIVE_STEP_LOGIN_TIME) {
				grangKinAngerStep = 5;
				setGrangKinAngerSafeTime(0);
				getAccount().setGrangKinAngerTime(0, this);
			}
		} else if (getAccount().getGrangKinAngerStat() == 6) {
			if (_grangKinAngerSafeTime >= GrangKinConfig.GRANG_KIN_ANGER_SIX_STEP_LOGIN_TIME) {
				grangKinAngerStep = 6;
				setGrangKinAngerSafeTime(0);
				getAccount().setGrangKinAngerTime(0, this);
			}
		}

		if (getAccount().getGrangKinAngerTime() != 0) {
			getAccount().addGrangKinAngerTime(-1, this);
		}

		if (grangKinAngerStep != 0) {
			sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 555 + grangKinAngerStep, false));
			SC_REST_EXP_INFO_NOTI.send(this);
		}
	}
	
	

	private boolean _one_minute_start = false;

	public boolean isOneMinuteStart() {
		return _one_minute_start;
	}

	public void setOneMinuteStart(boolean flag) {
		_one_minute_start = flag;
	}

	private int _EnviroMentCount;

	public int getEnviroMentCount() {
		return _EnviroMentCount;
	}

	public void setEnviroMentCount(int i) {
		_EnviroMentCount = i;
	}

	public void addEnviroMentCount(int i) {
		int plus = _EnviroMentCount + i;
		if (plus > 4) {
			plus = 5;
		}
		_EnviroMentCount = plus;
	}

	private Calendar _pcLocalTime;

	public Calendar getEnviroMentLocalTime() {
		return _pcLocalTime;
	}

	public void setEnviroMentLocalTime(Calendar time) {
		_pcLocalTime = time;
	}

	/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/
	private int _EinhasadBlessper = 0;

	public int getEinhasadBlessper() {
		return _EinhasadBlessper;
	}

	public void setEinhasadBlessper(int einhasadblessper) {
		_EinhasadBlessper = einhasadblessper;

		if (noPlayerCK || getAI() != null)
			return;

		SC_REST_EXP_INFO_NOTI.send(this);
	}

	public void addEinhasadBlessper(int i) {
		_EinhasadBlessper += i;

		if (noPlayerCK || getAI() != null)
			return;

		SC_REST_EXP_INFO_NOTI.send(this);
	}

	/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/

	public MJEShiftObjectType get_shift_type() {
		return _netConnection == null ? MJEShiftObjectType.NONE : _netConnection.get_shift_type();
	}

	public void set_shift_type(MJEShiftObjectType shift_type) {
		if (_netConnection != null)
			_netConnection.set_shift_type(shift_type);
	}

	public boolean is_shift_client() {
		return _netConnection != null && _netConnection.is_shift_client();
	}

	public boolean is_shift_transfer() {
		return _netConnection == null ? false : _netConnection.is_shift_transfer();
	}

	public boolean is_shift_battle() {
		return _netConnection == null ? false : _netConnection.is_shift_battle();
	}

	private boolean m_is_ready_server_shift = false;

	public boolean is_ready_server_shift() {
		return m_is_ready_server_shift;
	}

	public void set_ready_server_shift(boolean is_ready_server_shift) {
		m_is_ready_server_shift = is_ready_server_shift;
	}

	private MJThebeCharacterInfo m_thebe_info;

	public MJThebeCharacterInfo get_thebe_info() {
		return m_thebe_info;
	}

	public void set_thebe_info(MJThebeCharacterInfo tbInfo) {
		m_thebe_info = tbInfo;
	}

	public String get_server_description() {
		return _netConnection == null ? "" : _netConnection.get_server_description();
	}

	public String get_server_identity() {
		return _netConnection == null ? "" : _netConnection.get_server_identity();
	}

	private MJCompanionInstance m_companion;

	public void set_companion(MJCompanionInstance companion) {
		if (companion != null)
			remove_companion();
		m_companion = companion;
	}

	public MJCompanionInstance get_companion() {
		return m_companion;
	}

	public void remove_companion() {
		if (m_companion != null) {
			m_companion.deleteMe();
			m_companion = null;
		}
	}

	@Override
	public void send_pink_name(int remain_seconds) {
		S_PinkName pnk = new S_PinkName(getId(), remain_seconds);
		sendPackets(pnk, false);
		if (!isGmInvis())
			broadcastPacket(pnk, false);
		pnk.clear();
	}

	private boolean _playsupport;

	public boolean isPlaySupport() {
		return _playsupport;
	}

	public void setPlaySupport(boolean flag) {
		_playsupport = flag;
	}
	
	public long LindArmorTime = 0;
	
	
	private L1ItemInstance _TradeItem;
	 public void setTradeItem(L1ItemInstance Item){
		_TradeItem = Item;
	 } 
	 public L1ItemInstance getTradeItem(){
		 return _TradeItem;
	 }
	 
	 
	// 자동 사냥 활성화 여부를 나타내는 변수
	 private boolean _autoHunt;

	 // 자동 사냥 활성화 여부를 반환하는 메서드
	 public boolean isAutoHunt() {
	     return _autoHunt;
	 }

	 // 자동 사냥 활성화 여부를 설정하는 메서드
	 public void setAutoHunt(boolean autoHunt) {
	     _autoHunt = autoHunt;
	 }

	 
	 
}
