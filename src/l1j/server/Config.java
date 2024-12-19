package l1j.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.MJHexHelper;

public final class Config {

	/** 自动打猎地图坐标 **/
	public static int 자동사냥맵번호; // 自动打猎地图编号
	public static int 자동사냥도착_X; // 自动打猎到达点X
	public static int 자동사냥도착_Y; // 自动打猎到达点Y 
	public static int 자동사냥물약; // 自动打猎药水
	
	public static boolean USE_ATTENDANCE_SYSTEM; // 签到系统
	
	public static boolean USE_ACTION_TIME_LOGGING; // 动作时间记录
	public static boolean USE_POTION_EFFECT_LOGGIN; // 药水效果记录
	/** 角色信息自动保存系统 **/
	public static int AUTOSAVE_INTERVAL;
	public static int AUTOSAVE_INTERVAL_INVENTORY;
	
	public static boolean is_leaf = false;
	
	/** 角色信息自动保存系统 **/
	
	public static boolean IP_DELAY_CHECK_USE;
	//TODO 掉落提示物品
	public static HashMap<Integer, Integer> DROPMENT_ITEM = new HashMap<Integer, Integer>(32);
	
	//TODO 机器人君主自动生成
	public static boolean Clan_leader;
	
	public static boolean LOGIN_ENCRYPTION;
	
	public static int Number_Count;
	
	public static boolean 자동사냥; // 自动打猎
	public static boolean 자동물약버프; // 自动药水BUFF
	
	/** 角色创建时的起始等级 **/
	public static int Start_Char_Level;
	
	public static int PER_AC_PC_TO_PC;
	public static int PER_AC_NPC_TO_PC;
	public static int PER_DODGE;
	public static int PER_EVASION;
	
	//TODO 经验值保护BUFF
	public static int Start_Char_Boho;
	
	//TODO 中介交易系统
	public static int ADENASHOP_LEVEL;
	public static int MAX_SELL_ADENA;
	public static int MIN_SELL_ADENA;
	public static int MIN_SELL_CASH;
	public static int MAX_SELL_CASH;
	
	public static int QestLevel_End;
	public static boolean WALK_POSITION_CHECK;
	
	/** 乐透系统 **/
	public static boolean LOTTO;
	public static int LOTTO_LEVEL;
	public static int LOTTO_BATTING;
	public static int LOTTO_BONUS;
	public static String LOTTO_TIME;
	/** 乐透系统 **/
	
	/** 服务器管理线程 **/
	public static boolean CHARACTER_SAVED_SYSTEM;
	public static boolean CHARACTER_CHECK_SYSTEM;
	
	public static int STRIKERGALEs;
	public static int EMPIREHIT_TO_LEVEL;
	public static int STUNHIT_TO_LEVEL;
	public static int HORRORHIT_TO_LEVEL;
	public static int DESTROYHIT_TO_LEVEL;
	
	private static final Logger _log = Logger.getLogger(Config.class.getName());
	public static boolean IS_CHECKBOX_RULES_USE;
	public static double CHARACTER_MAGICHIT_RATE;
	public static double CHARACTER_MAGICCRI_RATE;
	public static double MISSILE_CRITICAL_DAMAGE_RATE;
	public static double MELEE_CRITICAL_DAMAGE_RATE;
	public static double MAGIC_CRITICAL_DAMAGE_RATE;
	public static double REDUCTION_IGNORE_DAMAGERATE;

	public static boolean IS_VALIDSHOP;
	public static boolean IS_VALIDITEMID;
	/** 机器人系统 **/
	public static String ROBOT_NAME;
	/** 机器人系统 **/

	/** 排名进入药水外部化 **/
	public static int RANK_POTION;

	/** 11以上附魔物品是否必定消失 **/
	public static boolean ENCHANT_MAX_FAIL;
	public static boolean MASTERENCHANTMESS;
	public static double ENCHANT_FAIL_RATE_ONEST;
	public static double ENCHANT_FAIL_RATE_ONESTO;
	public static double ENCHANT_FAIL_RATE_ONE;
	public static double ENCHANT_FAIL_RATE_TWO;
	public static double ENCHANT_COENT;
	public static double Master_Enchant;
	public static int New_Cha;
	public static int New_Cha1;
	public static int Npc_Max;
	public static int Pc_Reload;
	public static int Npc_Location;
	public static boolean MANAGER_CLEAR;
	public static int MANAGER_CLEAR_TIME;
	public static int COMBO_CHANCE;
	public static double BossGht_CHANCE;
	public static int 아덴상점타입; // 金币商店类型
	public static double 아덴상점엔샵아덴샵설정; // 金币商店设置
	public static double Finalburnpc;
	public static double Finalburnnpc;
	public static double DOUBLE_BREAK_CHANCE;;
	public static double BURNING_SPIRIT_PC;
	public static double BURNING_SPIRIT_NPC;
	
	public static double DOUBLE_BREAK_DESTINY_PC;
	public static double DOUBLE_BREAK_DESTINY_NPC;
	
	public static boolean LOGGING_ACCELERATOR;
	public static double DOUBLE_DMG;
	
	public static boolean USE_DELAY = false;//反延迟检查

	/** 幸运商店活动 **/
	public static boolean 깃털이벤트상점; // 羽毛活动商店

	// 龙之戒指黑暗耳环伤害追加概率
	public static int FEATHER_SHOP_NUM; // 羽毛商店使用次数限制
	public static int FEATHERTIME; // 羽毛发放时间

	// 强化BUFF(活力,攻击,魔法,定身,昏迷,防御)时间外部化
	// 强化BUFF_活力, 强化BUFF_攻击, 强化BUFF_防御, 强化BUFF_魔法, 强化BUFF_昏迷, 强化BUFF_定身
	public static boolean ACCOUNT_N_BUFF;
	public static int LIMIT_N_BUFF;
	public static int TIME_N_BUFF;

	/** NPC 物理伤害/魔法伤害 **/
	public static int npcdmg1;
	public static int npcdmg2;
	public static int npcdmg3;
	public static double New_MagicDmg;
	public static int YN_pclevel;
	public static int pcnpcmagicdmg;
	public static int tamsc;// 稻草人探测
	public static int tamsc1;
	public static int tamsc2;
	public static int SCARELEVEL;
	public static int Characters_CharSlot;

	/** 古代武器类 **/
	public static Integer[] rareweapon;
	public static Integer[] sasinweapon;
	public static Integer[] weapon;
	public static int rareweaponLevel;
	public static int sasinweaponLevel;
	public static int weaponLevel;
	public static Integer[] armor;
	public static int armorLevel;
	public static Integer[] accessory;
	public static int accessorytest;
	public static Integer[] ancient;
	public static int ancientLevel;
	public static Integer[] bless_orim;

	public static Integer[] Roomtis;
	public static int RoomtisLevel;
	public static Integer[] Sanpper;
	public static int SnapperLevel;
	public static Integer[] Sentence;
	public static int SentenceLevel;
	public static Integer[] Insignia;
	public static int Insignia_Level;
	public static Integer[] Should;
	public static int Should_Level;

	public static int BugBug;
	public static int BugBug1;
	public static int Dr_weapon;
	public static int Hero_weapon;
	public static boolean HUNT_EVENT;
	public static int ADEN_SHOP_NPC;
	public static int Tam_Time;
	public static int[] level1 = new int[2];
	public static int[] level2_1 = new int[2];
	public static int[] level2_2 = new int[2];
	public static int[] level3_1 = new int[2];
	public static int[] level3_2 = new int[2];
	public static int[] level3_3 = new int[2];
	public static int[] level3_4 = new int[2];

	// TODO mjbot.properties 配置
	public static int RedKnight_dieCount;

	/** 服务器管理 **/
	public static boolean normal = false;
	public static boolean world = false;
	public static boolean whisper = false;
	public static boolean alliance = false;
	public static boolean party = false;
	public static boolean shout = false;
	public static boolean business = false;
	public static boolean shutdownCheck = false;
	/** 服务器管理 **/

	public static boolean IS_SELLINGS_SHOP_LOCK;

	/** Debug/release mode */
	public static final boolean DEBUG = false;
	public static boolean STANDBY_SERVER = false;
	public static boolean TARGETGFX = false;

	/** Thread pools size */
	public static int THREAD_P_EFFECTS;
	public static int THREAD_P_GENERAL;
	public static int AI_MAX_THREAD;
	public static int THREAD_P_SIZE_GENERAL;
	public static int THREAD_P_TYPE_GENERAL;
	public static int SELECT_THREAD_COUNT;

	/** Server control */
	public static boolean USE_SHIFT_SERVER;
	public static int SCHEDULED_CORE_POOLSIZE;
	public static String GAME_SERVER_MENT;
	public static String GAME_SERVER_NAME;
	public static int GAME_SERVER_PORT;
	public static int AD_REPORT_SERVER_PORT;
	public static String DB_DRIVER;
	public static String DB_URL;
	public static String DB_LOGIN;
	public static String DB_PASSWORD;
	public static String TIME_ZONE;
	public static int CLIENT_LANGUAGE;
	public static boolean HOSTNAME_LOOKUPS;
	public static int AUTOMATIC_KICK;
	public static boolean AUTO_CREATE_ACCOUNTS;
	public static short MAX_ONLINE_USERS;
	public static boolean CACHE_MAP_FILES;
	public static boolean LOAD_V2_MAP_FILES;
	public static byte LOGGING_WEAPON_ENCHANT;
	public static byte LOGGING_ARMOR_ENCHANT;
	public static boolean LOGGING_CHAT_NORMAL;
	public static boolean LOGGING_CHAT_WHISPER;
	public static boolean LOGGING_CHAT_SHOUT;
	public static boolean LOGGING_CHAT_WORLD;
	public static boolean LOGGING_CHAT_CLAN;
	public static boolean LOGGING_CHAT_PARTY;
	public static boolean LOGGING_CHAT_COMBINED;
	public static boolean LOGGING_CHAT_CHAT_PARTY;
	public static boolean CHAR_PASSWORD;
	public static int CHAR_PASSWORD_MAXIMUM_FAILCOUNT;

	public static String PRIVATE_SHOP_CHAT;
	public static int HAJA;
	public static boolean 테스트 = false; // 测试

	public static int PC_RECOGNIZE_RANGE;
	public static boolean CHARACTER_CONFIG_IN_SERVER_SIDE;
	public static boolean ALLOW_2PC;
	public static int LEVEL_DOWN_RANGE;
	public static boolean SEND_PACKET_BEFORE_TELEPORT;
	public static boolean DETECT_DB_RESOURCE_LEAKS;
	public static boolean AUTH_CONNECT;
	public static int AUTH_KEY;

	/** Rate control */
	public static double RATE_XP;
	public static double RATE_XP_CLAUDIA;
	public static double RATE_LAWFUL;
	public static double RATE_KARMA;
	public static double RATE_DROP_ADENA;
	public static int Adena_LevelMin;
	public static int Adena_LevelMax;
	public static double Drop_Adena;
	public static int Adena_LevelMin1;
	public static int Adena_LevelMax1;
	public static double Drop_Adena1;
	public static double Drop_Adena2;
	public static double RATE_DROP_ITEMS;
	public static double RATE_DROP_RABBIT;// 新年活动
	public static double RATE_DROP_RABBIT1;// 万圣节
	public static int ENCHANT_CHANCE_WEAPON;
	public static int ENCHANT_CHANCE_ARMOR;
	public static int ENCHANT_CHANCE_ACCESSORY;
	public static double RATE_WEIGHT_LIMIT;
	public static double RATE_WEIGHT_LIMIT_PET;
	public static double RATE_SHOP_SELLING_PRICE;
	public static double RATE_SHOP_PURCHASING_PRICE;
	public static int CREATE_CHANCE_DIARY;
	public static int CREATE_CHANCE_RECOLLECTION;
	public static int CREATE_CHANCE_MYSTERIOUS;
	public static int CREATE_CHANCE_PROCESSING;
	public static int CREATE_CHANCE_PROCESSING_DIAMOND;
	public static int CREATE_CHANCE_DANTES;
	public static int CREATE_CHANCE_ANCIENT_AMULET;
	public static int CREATE_CHANCE_HISTORY_BOOK;
	public static int FEATHER_NUM;
	public static int FEATHER_NUM1;
	public static int FEATHER_NUM2;
	public static int FEATHER_NUM3;
	
	public static boolean INFINIE_FISHING;//无限钓鱼
	
	
	public static int 탐갯수; // 探测数量
	public static int 탐혈맹갯수; // 血盟探测数量
	public static int 탐성혈갯수; // 圣盟探测数量

	/** 活动袜子发放 **/
	public static boolean 양말작동유무; // 袜子活动开关
	public static int EVENT_TIME;
	public static int EVENT_NUMBER;
	public static int EVENT_ITEM;

	/** AltSettings control */
	public static short GLOBAL_CHAT_LEVEL;
	public static short WHISPER_CHAT_LEVEL;
	public static short To_ChatLevel;
	public static int BoardLevel;
	public static byte AUTO_LOOT;
	public static int LOOTING_RANGE;
	public static boolean ALT_NONPVP;
	public static boolean ALT_ATKMSG;
	public static boolean CHANGE_TITLE_BY_ONESELF;
	public static int MAX_CLAN_MEMBER;
	public static boolean CLAN_ALLIANCE;
	public static int MAX_PT;
	public static int MAX_CHAT_PT;
	public static boolean SIM_WAR_PENALTY;
	public static boolean GET_BACK;
	public static String ALT_ITEM_DELETION_TYPE;
	public static int ALT_ITEM_DELETION_TIME;
	public static int ALT_ITEM_DELETION_RANGE;
	public static boolean ALT_GMSHOP;
	public static int ALT_GMSHOP_MIN_ID;
	public static int ALT_GMSHOP_MAX_ID;
	public static boolean ALT_BASETOWN;
	public static int ALT_BASETOWN_MIN_ID;
	public static int ALT_BASETOWN_MAX_ID;
	public static int WHOIS_CONTER;
	public static boolean ALT_BEGINNER_BONUS;
	public static int NOTIS_TIME; // 公告时间
	public static int CLAN_COUNT; // 血盟BUFF人数
	public static int Crown_Level;
	public static double 성혈경험치; // 圣盟经验值
	public static int AUTO_REMOVELEVEL;
	public static int 라던입장레벨; // 地下城入场等级
	public static double 마법사마법대미지; // 法师魔法伤害

	/** 新增加 **/
	public static int 경험치지급단; // 经验值发放
	public static int 키링크; // 钥匙环
	public static int 데스나이트헬파이어; // 死亡骑士地狱之火
	public static int DEDMG;
	public static int 지배자헌신마법확률; // 支配者献身魔法概率
	public static int DEDMG1;
	public static int DEDMG2;

	/** 气运类物品细分 **/
	public static int Bless_Chance;
	public static double 기사; // 骑士
	public static double 용기사; // 龙骑士
	public static double 요정; // 精灵
	public static double 군주; // 君主
	public static double 법사; // 法师
	public static double 다엘; // 黑暗精灵
	public static double 환술사; // 幻术师
	public static double 전사; // 战士
	public static int 신규혈맹보호레벨; // 新血盟保护等级
	public static int 신규혈맹클랜; // 新血盟编号
	public static int 신규혈맹엠블런; // 新血盟徽章
	public static boolean 신규혈맹보호처리; // 新血盟保护处理
	public static String 신규혈맹이름; // 新血盟名称
	public static int 무기인첸트; // 武器附魔
	public static int 무기고급인첸트; // 高级武器附魔
	public static int 방어구인첸트; // 防具附魔
	public static int 방어구고급인첸트; // 高级防具附魔
	public static int 룸티스; // 龙之戒指
	public static int 스냅퍼; // 黑暗耳环
	public static int 악세사리; // 饰品
	public static int accessoryLevel;
	public static int 문장류; // 纹章类
	public static double New_LevelExp1;
	public static double New_LevelMin1;
	public static double New_LevelMax1;
	public static double New_LevelExp2;
	public static double New_LevelMin2;
	public static double New_LevelMax2;
	public static double New_LevelExp3;
	public static double New_LevelMin3;
	public static double New_LevelMax3;
	public static double New_LevelExp4;
	public static double New_LevelMin4;
	public static double New_LevelMax4;

	public static boolean POLY_EVENT1;
	public static boolean POLY_EVENT;
	public static int 선포레벨; // 宣战等级
	public static int 혈맹접속인원; // 血盟在线人数
	public static boolean Arden = false;// 金币商店检查
	public static boolean ALT_WHO_COMMAND;
	public static boolean ALT_REVIVAL_POTION;
	public static int ALT_WAR_TIME;
	public static int ALT_WAR_TIME_UNIT;
	public static int ALT_WAR_INTERVAL;
	public static int ALT_WAR_INTERVAL_UNIT;
	public static int ALT_RATE_OF_DUTY;
	public static boolean SPAWN_HOME_POINT;
	public static int SPAWN_HOME_POINT_RANGE;
	public static int SPAWN_HOME_POINT_COUNT;
	public static int SPAWN_HOME_POINT_DELAY;
	public static boolean INIT_BOSS_SPAWN;
	public static int ELEMENTAL_STONE_AMOUNT;
	public static int HOUSE_TAX_INTERVAL;
	public static int MAX_DOLL_COUNT;
	public static boolean RETURN_TO_NATURE;
	public static int MAX_NPC_ITEM;
	public static int MAX_PERSONAL_WAREHOUSE_ITEM;
	public static int MAX_CLAN_WAREHOUSE_ITEM;
	public static boolean DELETE_CHARACTER_AFTER_7DAYS;
	public static int GMCODE;
	public static int SUB_GMCODE;
	public static int NEW_PLAYER;
	public static int PC_SHOP;
	public static int ALT_DROPLEVELLIMIT;
	public static boolean Use_Show_Announcecycle; // 追加
	public static int Show_Announcecycle_Time; // 追加
	public static int HELL_TIME;
	public static int HELL_LEVEL;

	/** CharSettings control */
	public static double PHYSICAL_REDUCTION_RATION;
	public static double MAGIC_REDUCTION_RATION;
	
	public static int PRINCE_MAX_HP;
	public static int PRINCE_MAX_MP;
	public static int KNIGHT_MAX_HP;
	public static int KNIGHT_MAX_MP;
	public static int ELF_MAX_HP;
	public static int ELF_MAX_MP;
	public static int WIZARD_MAX_HP;
	public static int WIZARD_MAX_MP;
	public static int DARKELF_MAX_HP;
	public static int DARKELF_MAX_MP;
	public static int DRAGONKNIGHT_MAX_HP;
	public static int DRAGONKNIGHT_MAX_MP;
	public static int BLACKWIZARD_MAX_HP;
	public static int BLACKWIZARD_MAX_MP;
	public static int 전사_MAX_HP; // 战士最大HP
	public static int 전사_MAX_MP; // 战士最大MP
	public static int PRINCE_ADD_DAMAGEPC;
	public static int KNIGHT_ADD_DAMAGEPC;
	public static int ELF_ADD_DAMAGEPC;
	public static int WIZARD_ADD_DAMAGEPC;
	public static int DARKELF_ADD_DAMAGEPC;
	public static int DRAGONKNIGHT_ADD_DAMAGEPC;
	public static int BLACKWIZARD_ADD_DAMAGEPC;
	public static int 전사_ADD_DAMAGEPC; // 战士额外伤害
	public static int LIMITLEVEL;
//	public static int NEWDMG; // 骑士团武器类
	/*
	 * public static int MAX_LEVEL; public static int MAX_LEVEL_EXP = -1;
	 */
	public static int ACLEVEL;
	public static int LV50_EXP;
	public static int LV51_EXP;
	public static int LV52_EXP;
	public static int LV53_EXP;
	public static int LV54_EXP;
	public static int LV55_EXP;
	public static int LV56_EXP;
	public static int LV57_EXP;
	public static int LV58_EXP;
	public static int LV59_EXP;
	public static int LV60_EXP;
	public static int LV61_EXP;
	public static int LV62_EXP;
	public static int LV63_EXP;
	public static int LV64_EXP;
	public static int LV65_EXP;
	public static int LV66_EXP;
	public static int LV67_EXP;
	public static int LV68_EXP;
	public static int LV69_EXP;
	public static int LV70_EXP;
	public static int LV71_EXP;
	public static int LV72_EXP;
	public static int LV73_EXP;
	public static int LV74_EXP;
	public static int LV75_EXP;
	public static int LV76_EXP;
	public static int LV77_EXP;
	public static int LV78_EXP;
	public static int LV79_EXP;
	public static int LV80_EXP;
	public static int LV81_EXP;
	public static int LV82_EXP;
	public static int LV83_EXP;
	public static int LV84_EXP;
	public static int LV85_EXP;
	public static int LV86_EXP;
	public static int LV87_EXP;
	public static int LV88_EXP;
	public static int LV89_EXP;
	public static int LV90_EXP;
	public static int LV91_EXP;
	public static int LV92_EXP;
	public static int LV93_EXP;
	public static int LV94_EXP;
	public static int LV95_EXP;
	public static int LV96_EXP;
	public static int LV97_EXP;
	public static int LV98_EXP;
	public static int LV99_EXP;
	
	/*
	public static int LV100_EXP;
	public static int LV101_EXP;
	public static int LV102_EXP;
	public static int LV103_EXP;
	public static int LV104_EXP;
	public static int LV105_EXP;
	public static int LV106_EXP;
	public static int LV107_EXP;
	public static int LV108_EXP;
	public static int LV109_EXP;
	public static int LV110_EXP;
	public static int LV111_EXP;
	public static int LV112_EXP;
	public static int LV113_EXP;
	public static int LV114_EXP;
	public static int LV115_EXP;
	public static int LV116_EXP;
	public static int LV117_EXP;
	public static int LV118_EXP;
	public static int LV119_EXP;
	public static int LV120_EXP;
	public static int LV121_EXP;
	public static int LV122_EXP;
	public static int LV123_EXP;
	public static int LV124_EXP;
	public static int LV125_EXP;
	public static int LV126_EXP;
	public static int LV127_EXP;
	*/
	

	/** ServerCodes.properties 设置 **/ // TODO
	public static int summon_hpdmg;
	public static boolean IS_PARTY_EXP;
	public static double ADD_PARTY_EXP;
	public static int Ad_Sc;
	public static int Tax_Rate;
	public static int Tax_RateMin;
	public static int Tax_RateMax;
	public static int Lineage_Buff;
	public static int ExpPo;
	public static int ExpPo1;
	public static double BLESSING;
	public static double UN_BLESSING;
	public static double ADENA_RATE_OF_BLESSING;
	public static double ADENA_RATE_OF_UNBLESSING;
	
	public static int Garnet_Rnd;
	public static int Garnet_Count;

	// TODO Magic.properties
	public static int SPELL_DELAYOVER_PENDING;
	public static boolean IS_SPELL_DELAY_RUN;
	public static long SPELL_DELAYERROR;

	public static int IMMUNE_Level;
	public static double IMMUNE_DMG;
	public static int IMMUNE_Level1;
	public static double IMMUNE_DMG1;
	public static int IMMUNE_Level2;
	public static double IMMUNE_DMG2;
	public static int IMMUNE_Level3;
	public static double IMMUNE_DMG3;
	public static int IMMUNE_Level4;
	public static double IMMUNE_DMG4;
	public static int IMMUNE_Level5;
	public static double IMMUNE_DMG5;
	public static int IMMUNE_Level6;
	public static double IMMUNE_DMG6;
	public static double IMMUNE_DMG7;

	//TODO 新技能公式变更
	public static double Erase_MagicT;
	public static double EARTH_BINDT;
	public static double STRIKER_GALET; 
	public static double UNCANNYDODGE1;
	public static double UNCANNYDODGE2;
	public static double UNCANNYDODGE3;
	public static double UNCANNYDODGE4;
	public static double UNCANNYDODGE5;
	public static double UNCANNYDODGE6;
	public static double UNCANNYDODGE7;
	public static double UNCANNYDODGE8;
	public static double UNCANNYDODGE9;
	public static double UNCANNYDODGE10;
	public static double UNCANNYDODGE11;
	public static double UNCANNYDODGE12;
	public static double UNCANNYDODGE13;
	public static double UNCANNYDODGE14;
	public static double UNCANNYDODGE15;
	public static double UNCANNYDODGE16;
	public static double UNCANNYDODGE17;
	public static double UNCANNYDODGE18;
	public static double UNCANNYDODGE19;
	public static double MIRRORIMAGE;
	public static double POLLUTE_WATERT;
	public static double WIND_SHACKLET;
	public static double ARMOR_BRAKET;
	public static double GUARD_BREAKT;
	public static double FEAR_T; 
	public static double MORTALBODY;
	public static double THUNDER_GRABT;
	public static double HORROR_OF_DEATHT;
	public static double CONFUSION_PHANTASMT;
	public static double POWERRIP_T;
	public static double TOMAHAWKPoint_T;
	public static int CYCLONE_PROBABILITY;
	public static int INFERNO_PROBABILITY;
	public static double[] INFERNO_RATION;
	public static int[] INFERNO_EFFECTS;
	
	public static double EMPIRE;
	public static double SHOCK_STUN;
	public static double DESPERADO;
	public static double BONE_BREAK;
	public static int DISINT_CHAOTIC_WEIGHT;
	public static int DISINT_LAWFUL_WEIGHT;
	public static int DISINT_MAX_DMG;
	public static int DISINT_MIN_DMG;
	public static int TurnChant80UP;
	public static int TurnChant79UP;
	public static int TurnChant78UP;
	public static int TurnChant77UP;
	public static int TurnChant76UP;
	public static int TurnChant75UP;
	public static int TurnChant74UP;
	public static int TurnChant73UP;
	public static int TurnChant72UP;
	public static int TurnChant71UP;
	public static int TurnChant70UP;
	public static int TurnChant69UP;
	public static int TurnChant68UP;
	public static int TurnChant67UP;
	public static int TurnChant66UP;
	public static int TurnChant65UP;
	public static int TurnChant64UP;
	public static int TurnChant63UP;
	public static int TurnChant62UP;
	public static int TurnChant61UP;
	public static int TurnChant60UP;
	public static int TurnChant59UP;
	public static int TurnChant58UP;
	public static int TurnChant57UP;
	public static int TurnChant56UP;
	public static int TurnChant55UP;
	
	public static int TurnChantElf;
	public static int furyFo;
	public static double furydmg;
	public static int crashFo;
	public static double crashdmg;
	public static int BLOW_ATTACKPR; // 整数
	public static double BLOW_ATTACKDMG;

	public static double Counter;
	public static double RockDMG;
	public static int TitanRock_probability;
	public static int Titanmagic_probability;
	public static int Titanbullet_probability;

	public static int FOU_DAMAGEN;
	public static int TRIPLE_DAMAGEN;
	
	public static double[] FOU_DAMAGE_TABLE;
	public static double[] TRIPLE_DAMAGE_TABLE;

	public static double sim_levelmin1;
	public static double sim_levelmax1;
	public static double sim_levelmin2;
	public static double sim_levelmax2;
	public static double sim_levelmin3;
	public static double sim_levelmax3;
	public static double sim_levelmin4;
	public static double sim_levelmax4;
	public static double sim_levelmin5;
	public static double sim_levelmax5;
	public static double sim_levelmin6;
	public static double sim_levelmax6;
	public static double simsimDmg1;
	public static double simsimDmg2;
	public static double simsimDmg3;
	public static double simsimDmg4;
	public static double simsimDmg5;
	public static double simsimDmg6;

	/** 数据库池相关 */
	public static int min;
	public static int max;
	public static boolean run;

	/** 特殊强化系统 **/
	public static int Doll_Enchant_Per_lvl1;
	public static int Doll_Enchant_Per_lvl2;
	public static int Doll_Enchant_Per_lvl3;
	public static int Doll_Enchant_Per_lvl4;
	public static int Doll_Enchant_Per_lvl5;
	public static int Doll_Attack_Chance;
	public static int Doll_Attack_Dmg_Lvl1;
	public static int Doll_Attack_Dmg_Lvl2;
	public static int Doll_Attack_Dmg_Lvl3;
	public static int Doll_Attack_Dmg_Lvl4;
	public static int Doll_Attack_Dmg_Lvl5;

	public static int Weapon_Magic_Per;
	public static int Weapon_Enchant_Per_lvl1;
	public static int Weapon_Enchant_Per_lvl2;
	public static int Weapon_Enchant_Per_lvl3;
	public static int Weapon_Enchant_Per_lvl4;

	public static int Weapon_Enchant_Dmg_lvl1;
	public static int Weapon_Enchant_Dmg_lvl2;
	public static int Weapon_Enchant_Dmg_lvl3;
	public static int Weapon_Enchant_Dmg_lvl4;
	/** 特殊强化系统 **/

	/** 配置文件 */
	public static final String SERVER_CONFIG_FILE = "./config/server.properties";
	public static final String RATES_CONFIG_FILE = "./config/rates.properties";
	public static final String ALT_SETTINGS_FILE = "./config/altsettings.properties";
	public static final String CHAR_SETTINGS_CONFIG_FILE = "./config/charsettings.properties";
	public static final String ServerCodes_SETTINGS_CONFIG_FILE = "./config/ServerCodes.properties";
	public static final String Magic_SETTINGS_CONFIG_FILE = "./config/Magic.properties";
	/** 特殊强化系统 **/
	public static final String MAGIC_ENCHANT_SETTINGS_CONFIG_FILE = "./config/magicenchant.properties";
	public static final String LOTTO_SETTINGS_CONFIG_FILE = "./config/Lotto.properties";
	public static final String AUCTION_SETTINGS_CONFIG_FILE = "./config/Auction.properties";
	
	/** 其他设置 */

	// 从NPC身上可吸收的MP上限
	public static final int MANA_DRAIN_LIMIT_PER_NPC = 40;

	// 单次攻击可吸收的MP上限(灵魂魔法、钢铁灵魂魔法)
	public static final int MANA_DRAIN_LIMIT_PER_SOM_ATTACK = 9;

	public static double CRAFT_INCREASE_PROB_BYMILLION;
	public static int[] CRAFT_VERSION_HASH;
	public static int CRAFT_TRANSMIT_SAFELINE;
	public static int CRAFT_FLAGMENTATION_SIZE;
	public static int[] ALCHEMY_VERSION_HASH;
	public static int ALCHEMY_TRANSMIT_SAFELINE;
	public static int ALCHEMY_HYPERSUCCESS_PROB_BYMILLION;
	public static int ALCHEMY_NOTIFY_LEVEL;

	//
	public static void loadCraftConfig() {
		MJPropertyReader reader = new MJPropertyReader("./config/mj_craftinfo.properties");
		CRAFT_INCREASE_PROB_BYMILLION = reader.readDouble("CraftIncreaseProbByMillionPercent", "1.00");
		CRAFT_VERSION_HASH = MJHexHelper.parseHexStringToInt32Array(
				reader.readString("CraftVersionHashVal", "C4 C3 E9 D0 DC 18 04 24 DB A5 CD AD E0 0D 31 27 4C EE 48 28"),
				" ");
		
		
		
		
		CRAFT_TRANSMIT_SAFELINE = reader.readInt("CraftTransmitSafeLine", "500");
		CRAFT_FLAGMENTATION_SIZE = reader.readInt("CraftFlagmentationSize", "20");

		ALCHEMY_VERSION_HASH = MJHexHelper.parseHexStringToInt32Array(reader.readString("AlchemyVersionHashVal",
				"C4 C3 E9 D0 DC 18 04 24 DB A5 CD AD E0 0D 31 27 4C EE 48 28"), " ");
		ALCHEMY_TRANSMIT_SAFELINE = reader.readInt("AlchemyTransmitSafeLine", "1000");

		ALCHEMY_HYPERSUCCESS_PROB_BYMILLION = reader.readInt("AlchemyHyperSuccessProbability_by_million", "100000");

		ALCHEMY_NOTIFY_LEVEL = reader.readInt("AlchemyNotifyLevel", "4");
		reader.dispose();
	}

	public static void load() {
		// _log.info("loading gameserver config");
		// System.out.println("loading gameserver config");

		// TODO server.properties 设置
		try {
			loadCraftConfig();

			Properties serverSettings = new Properties();
			InputStream is = new FileInputStream(new File(SERVER_CONFIG_FILE));
			serverSettings.load(is);
			is.close();
			
			/** 角色信息自动保存系统 **/
			AUTOSAVE_INTERVAL = Integer.parseInt(serverSettings.getProperty("AutosaveInterval", "1200"), 10);

			AUTOSAVE_INTERVAL_INVENTORY = Integer
					.parseInt(serverSettings.getProperty("AutosaveIntervalOfInventory", "300"), 10);
			/** 角色信息自动保存系统 **/

			USE_SHIFT_SERVER = Boolean.parseBoolean(serverSettings.getProperty("UseShiftServer", "false"));
			
			SCHEDULED_CORE_POOLSIZE = Integer.parseInt(serverSettings.getProperty("SCHEDULEDCOREPOOLSIZE", "512"));
			IS_SELLINGS_SHOP_LOCK = Boolean.parseBoolean(serverSettings.getProperty("IsSellingsShopLocked", "false"));
			min = Integer.parseInt(serverSettings.getProperty("min"));
			max = Integer.parseInt(serverSettings.getProperty("max"));
			run = Boolean.parseBoolean(serverSettings.getProperty("run"));
			TARGETGFX = Boolean.parseBoolean(serverSettings.getProperty("Targetgfx", "true"));
			STANDBY_SERVER = Boolean.parseBoolean(serverSettings.getProperty("StandbyServer", "true"));
			GAME_SERVER_MENT = new String(serverSettings.getProperty("GAMESERVERMENT", "서버").getBytes("ISO-8859-1"),
					"euc-kr");
			GAME_SERVER_NAME = new String(serverSettings.getProperty("GameServerName", "서버").getBytes("ISO-8859-1"),
					"euc-kr");
			GAME_SERVER_PORT = Integer.parseInt(serverSettings.getProperty("GameserverPort", "2000"));
			AD_REPORT_SERVER_PORT = Integer.parseInt(serverSettings.getProperty("AdReportServerPort", "18182"));
			DB_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
			DB_URL = serverSettings.getProperty("URL",
					"jdbc:mysql://localhost/l1jdb?useUnicode=true&characterEncoding=euckr");
			DB_LOGIN = serverSettings.getProperty("Login", "root");
			DB_PASSWORD = serverSettings.getProperty("Password", "");
			THREAD_P_TYPE_GENERAL = Integer.parseInt(serverSettings.getProperty("GeneralThreadPoolType", "0"), 10);
			THREAD_P_SIZE_GENERAL = Integer.parseInt(serverSettings.getProperty("GeneralThreadPoolSize", "0"), 10);
			SELECT_THREAD_COUNT = Integer.parseInt(serverSettings.getProperty("IoThreadPoolSize", "4"));
			CLIENT_LANGUAGE = Integer.parseInt(serverSettings.getProperty("ClientLanguage", "4"));
			TIME_ZONE = serverSettings.getProperty("TimeZone", "JST");
			HOSTNAME_LOOKUPS = Boolean.parseBoolean(serverSettings.getProperty("HostnameLookups", "false"));
			AUTOMATIC_KICK = Integer.parseInt(serverSettings.getProperty("AutomaticKick", "10"));
			AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(serverSettings.getProperty("AutoCreateAccounts", "true"));
			MAX_ONLINE_USERS = Short.parseShort(serverSettings.getProperty("MaximumOnlineUsers", "30"));
			CACHE_MAP_FILES = Boolean.parseBoolean(serverSettings.getProperty("CacheMapFiles", "false"));
			LOAD_V2_MAP_FILES = Boolean.parseBoolean(serverSettings.getProperty("LoadV2MapFiles", "false"));
			LOGGING_WEAPON_ENCHANT = Byte.parseByte(serverSettings.getProperty("LoggingWeaponEnchant", "0"));
			LOGGING_ARMOR_ENCHANT = Byte.parseByte(serverSettings.getProperty("LoggingArmorEnchant", "0"));
			LOGGING_CHAT_NORMAL = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatNormal", "false"));
			LOGGING_CHAT_WHISPER = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatWhisper", "false"));
			LOGGING_CHAT_SHOUT = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatShout", "false"));
			LOGGING_CHAT_WORLD = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatWorld", "false"));
			LOGGING_CHAT_CLAN = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatClan", "false"));
			LOGGING_CHAT_PARTY = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatParty", "false"));
			LOGGING_CHAT_COMBINED = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatCombined", "false"));
			LOGGING_CHAT_CHAT_PARTY = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatChatParty", "false"));
			PC_RECOGNIZE_RANGE = Integer.parseInt(serverSettings.getProperty("PcRecognizeRange", "20"));
			PRIVATE_SHOP_CHAT = new String(serverSettings.getProperty("PrivateShopChat").getBytes("ISO-8859-1"),
					"GBK");
			CHAR_PASSWORD = Boolean.parseBoolean(serverSettings.getProperty("CharPassword", "true"));
			CHAR_PASSWORD_MAXIMUM_FAILCOUNT = Integer.parseInt(serverSettings.getProperty("CharPasswordMaximumFailureCount", "5"));
			HAJA = Integer.parseInt(serverSettings.getProperty("Haja", "2"));
			CHARACTER_CONFIG_IN_SERVER_SIDE = Boolean
					.parseBoolean(serverSettings.getProperty("CharacterConfigInServerSide", "true"));
			ALLOW_2PC = Boolean.parseBoolean(serverSettings.getProperty("Allow2PC", "true"));
			LEVEL_DOWN_RANGE = Integer.parseInt(serverSettings.getProperty("LevelDownRange", "0"));
			SEND_PACKET_BEFORE_TELEPORT = Boolean
					.parseBoolean(serverSettings.getProperty("SendPacketBeforeTeleport", "true"));
			DETECT_DB_RESOURCE_LEAKS = Boolean
					.parseBoolean(serverSettings.getProperty("EnableDatabaseResourceLeaksDetection", "false"));
			AUTH_CONNECT = Boolean.parseBoolean(serverSettings.getProperty("AuthConnect", "false"));
			AUTH_KEY = Integer.parseInt(serverSettings.getProperty("AuthKey", "136"));
			
			CHARACTER_SAVED_SYSTEM = Boolean.parseBoolean(serverSettings.getProperty("CHARACTER_SAVED_SYSTEM", "true"));
			CHARACTER_CHECK_SYSTEM = Boolean.parseBoolean(serverSettings.getProperty("CHARACTER_CHECK_SYSTEM", "true"));
			
			is_leaf = Boolean.parseBoolean(serverSettings.getProperty("is_leaf", "false"));
			
			LOGIN_ENCRYPTION = Boolean.parseBoolean(serverSettings.getProperty("LoginEncryption", "false"));
			USE_ATTENDANCE_SYSTEM = Boolean.parseBoolean(serverSettings.getProperty("UseAttendanceSystem", "false"));
			
			IP_DELAY_CHECK_USE = Boolean.parseBoolean(serverSettings.getProperty("IpDelayCheckUse", "false"));
			
			INFINIE_FISHING = Boolean.parseBoolean(serverSettings.getProperty("InfiniteFishing", "false"));
			
			
			USE_DELAY = Boolean.parseBoolean(serverSettings.getProperty("UseDelay", "false"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Failed to Load " + SERVER_CONFIG_FILE + " File.");
		}

		// TODO rate.properties 设置
		try {
			Properties rateSettings = new Properties();
			FileReader is = new FileReader(new File(RATES_CONFIG_FILE));
			rateSettings.load(is);
			is.close();

			Show_Announcecycle_Time = Integer.parseInt(rateSettings.getProperty("Show_Announcecycle_Time", "10"));
			Use_Show_Announcecycle = Boolean.parseBoolean(rateSettings.getProperty("Use_Show_Announcecycle", "false"));

			RATE_XP = Double.parseDouble(rateSettings.getProperty("RateXp", "1.0"));
			RATE_XP_CLAUDIA = Double.parseDouble(rateSettings.getProperty("RateXpClaudia", "1.0"));
			성혈경험치 = Double.parseDouble(rateSettings.getProperty("BloodBonus", "1.0"));
			RATE_LAWFUL = Double.parseDouble(rateSettings.getProperty("RateLawful", "1.0"));
			RATE_KARMA = Double.parseDouble(rateSettings.getProperty("RateKarma", "1.0"));
			RATE_DROP_ADENA = Double.parseDouble(rateSettings.getProperty("RateDropAdena", "1.0"));
			
			Adena_LevelMin = Integer.parseInt(rateSettings.getProperty("AdenaLevelMin", "1"));
			Adena_LevelMax = Integer.parseInt(rateSettings.getProperty("AdenaLevelMax", "1"));
			Drop_Adena = Double.parseDouble(rateSettings.getProperty("DropAdena", "1.0"));
			Adena_LevelMin1 = Integer.parseInt(rateSettings.getProperty("AdenaLevelMin1", "1"));
			Adena_LevelMax1 = Integer.parseInt(rateSettings.getProperty("AdenaLevelMax1", "1"));
			Drop_Adena1 = Double.parseDouble(rateSettings.getProperty("DropAdena1", "1.0"));
			Drop_Adena2 = Double.parseDouble(rateSettings.getProperty("DropAdena2", "1.0"));

			RATE_DROP_ITEMS = Double.parseDouble(rateSettings.getProperty("RateDropItems", "1.0"));
			RATE_DROP_RABBIT = Double.parseDouble(rateSettings.getProperty("RateDropRabbit", "10.0"));
			RATE_DROP_RABBIT1 = Double.parseDouble(rateSettings.getProperty("Hallowdrop", "10.0"));
			ENCHANT_CHANCE_WEAPON = Integer.parseInt(rateSettings.getProperty("EnchantChanceWeapon", "68"));
			ENCHANT_CHANCE_ARMOR = Integer.parseInt(rateSettings.getProperty("EnchantChanceArmor", "52"));
			ENCHANT_CHANCE_ACCESSORY = Integer.parseInt(rateSettings.getProperty("EnchantChanceAccessory", "5"));
			RATE_WEIGHT_LIMIT = Double.parseDouble(rateSettings.getProperty("RateWeightLimit", "1"));
			RATE_WEIGHT_LIMIT_PET = Double.parseDouble(rateSettings.getProperty("RateWeightLimitforPet", "1"));
			RATE_SHOP_SELLING_PRICE = Double.parseDouble(rateSettings.getProperty("RateShopSellingPrice", "1.0"));
			RATE_SHOP_PURCHASING_PRICE = Double.parseDouble(rateSettings.getProperty("RateShopPurchasingPrice", "1.0"));
			CREATE_CHANCE_DIARY = Integer.parseInt(rateSettings.getProperty("CreateChanceDiary", "33"));
			CREATE_CHANCE_RECOLLECTION = Integer.parseInt(rateSettings.getProperty("CreateChanceRecollection", "90"));
			CREATE_CHANCE_MYSTERIOUS = Integer.parseInt(rateSettings.getProperty("CreateChanceMysterious", "90"));
			CREATE_CHANCE_PROCESSING = Integer.parseInt(rateSettings.getProperty("CreateChanceProcessing", "90"));
			CREATE_CHANCE_PROCESSING_DIAMOND = Integer
					.parseInt(rateSettings.getProperty("CreateChanceProcessingDiamond", "90"));
			CREATE_CHANCE_DANTES = Integer.parseInt(rateSettings.getProperty("CreateChanceDantes", "90"));
			CREATE_CHANCE_ANCIENT_AMULET = Integer
					.parseInt(rateSettings.getProperty("CreateChanceAncientAmulet", "90"));
			CREATE_CHANCE_HISTORY_BOOK = Integer.parseInt(rateSettings.getProperty("CreateChanceHistoryBook", "10"));
			FEATHER_NUM = Integer.parseInt(rateSettings.getProperty("FeatherNum", "6"));
			FEATHER_NUM1 = Integer.parseInt(rateSettings.getProperty("FeatherNum1", "2"));
			FEATHER_NUM2 = Integer.parseInt(rateSettings.getProperty("FeatherNum2", "3"));
			FEATHER_NUM3 = Integer.parseInt(rateSettings.getProperty("FeatherNum3", "2"));
			

			FEATHERTIME = Integer.parseInt(rateSettings.getProperty("featherTime", "15"));
			Tam_Time = Integer.parseInt(rateSettings.getProperty("TamTime", "15"));

			HELL_TIME = Integer.parseInt(rateSettings.getProperty("HellTime", "6"));
			HELL_LEVEL = Integer.parseInt(rateSettings.getProperty("HellLevel", "70"));
			탐갯수 = Integer.parseInt(rateSettings.getProperty("TamNum", "6"));
			탐혈맹갯수 = Integer.parseInt(rateSettings.getProperty("TamNum1", "6"));
			탐성혈갯수 = Integer.parseInt(rateSettings.getProperty("TamNum2", "6"));
			양말작동유무 = Boolean.parseBoolean(rateSettings.getProperty("Eventof", "false"));
			EVENT_TIME = Integer.parseInt(rateSettings.getProperty("EventTime", "1"));
			EVENT_NUMBER = Integer.parseInt(rateSettings.getProperty("EventNumber", "1"));
			EVENT_ITEM = Integer.parseInt(rateSettings.getProperty("EventItem", "1"));

			// TODO 经验值细分
			New_LevelExp1 = Double.parseDouble(rateSettings.getProperty("NewLevelExp1", "1"));
			New_LevelMin1 = Double.parseDouble(rateSettings.getProperty("NewLevelMin1", "1"));
			New_LevelMax1 = Double.parseDouble(rateSettings.getProperty("NewLevelMax1", "1"));

			New_LevelExp2 = Double.parseDouble(rateSettings.getProperty("NewLevelExp2", "1"));
			New_LevelMin2 = Double.parseDouble(rateSettings.getProperty("NewLevelMin2", "1"));
			New_LevelMax2 = Double.parseDouble(rateSettings.getProperty("NewLevelMax2", "1"));

			New_LevelExp3 = Double.parseDouble(rateSettings.getProperty("NewLevelExp3", "1"));
			New_LevelMin3 = Double.parseDouble(rateSettings.getProperty("NewLevelMin3", "1"));
			New_LevelMax3 = Double.parseDouble(rateSettings.getProperty("NewLevelMax3", "1"));

			New_LevelExp4 = Double.parseDouble(rateSettings.getProperty("NewLevelExp4", "1"));
			New_LevelMin4 = Double.parseDouble(rateSettings.getProperty("NewLevelMin4", "1"));
			New_LevelMax4 = Double.parseDouble(rateSettings.getProperty("NewLevelMax4", "1"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Failed to Load " + RATES_CONFIG_FILE + " File.");
		}

		// TODO altsettings.properties 设置
		try {
			Properties altSettings = new Properties();
			InputStream is = new FileInputStream(new File(ALT_SETTINGS_FILE));
			altSettings.load(is);
			is.close();

			자동사냥맵번호 = Integer.parseInt(altSettings.getProperty("AutoHunt_map", "30"));
			자동사냥도착_X = Integer.parseInt(altSettings.getProperty("AutoHunt_Landing_X", "32722"));
			자동사냥도착_Y = Integer.parseInt(altSettings.getProperty("AutoHunt_Landing_Y", "32729"));
			자동사냥물약 = Integer.parseInt(altSettings.getProperty("AutoHunt_potion", "10"));
			자동사냥 = Boolean.parseBoolean(altSettings.getProperty("Autohunt", "true"));
			자동물약버프 = Boolean.parseBoolean(altSettings.getProperty("Autohunt1", "true"));
			
			MJINNHelper.USAGECOUNT = Integer.parseInt(altSettings.getProperty("InnMaximumCount", "20"));
			WALK_POSITION_CHECK = Boolean.parseBoolean(altSettings.getProperty("WalkPositionCheck", "true"));
			USE_POTION_EFFECT_LOGGIN = Boolean.parseBoolean(altSettings.getProperty("UsePotionEffectLogging", "false"));
			USE_ACTION_TIME_LOGGING = Boolean.parseBoolean(altSettings.getProperty("UseActionTimeLogging", "false"));
			QestLevel_End = Short.parseShort(altSettings.getProperty("QestLevelEnd", "1"));
			Number_Count = Short.parseShort(altSettings.getProperty("NumberCount", "3"));
			Start_Char_Level = Short.parseShort(altSettings.getProperty("StartCharLevel", "1"));
			Clan_leader = Boolean.parseBoolean(altSettings.getProperty("Clanleader", "true"));
			GLOBAL_CHAT_LEVEL = Short.parseShort(altSettings.getProperty("GlobalChatLevel", "30"));
			WHISPER_CHAT_LEVEL = Short.parseShort(altSettings.getProperty("WhisperChatLevel", "7"));
			To_ChatLevel = Short.parseShort(altSettings.getProperty("ToChatLevel", "1"));
			BoardLevel = Integer.parseInt(altSettings.getProperty("BoardLevel", "70"));
			AUTO_LOOT = Byte.parseByte(altSettings.getProperty("AutoLoot", "2"));
			LOOTING_RANGE = Integer.parseInt(altSettings.getProperty("LootingRange", "3"));
			CLAN_COUNT = Integer.parseInt(altSettings.getProperty("Clancount", "5"));
			Crown_Level = Integer.parseInt(altSettings.getProperty("CrownLevel", "1"));
			ALT_NONPVP = Boolean.parseBoolean(altSettings.getProperty("NonPvP", "true"));
			ALT_ATKMSG = Boolean.parseBoolean(altSettings.getProperty("AttackMessageOn", "true"));
			CHANGE_TITLE_BY_ONESELF = Boolean.parseBoolean(altSettings.getProperty("ChangeTitleByOneself", "false"));
			MAX_CLAN_MEMBER = Integer.parseInt(altSettings.getProperty("MaxClanMember", "0"));
			CLAN_ALLIANCE = Boolean.parseBoolean(altSettings.getProperty("ClanAlliance", "true"));
			MAX_PT = Integer.parseInt(altSettings.getProperty("MaxPT", "8"));
			MAX_CHAT_PT = Integer.parseInt(altSettings.getProperty("MaxChatPT", "8"));
			SIM_WAR_PENALTY = Boolean.parseBoolean(altSettings.getProperty("SimWarPenalty", "true"));
			GET_BACK = Boolean.parseBoolean(altSettings.getProperty("GetBack", "false"));
			ALT_ITEM_DELETION_TYPE = altSettings.getProperty("ItemDeletionType", "auto");
			ALT_ITEM_DELETION_TIME = Integer.parseInt(altSettings.getProperty("ItemDeletionTime", "10"));
			ALT_ITEM_DELETION_RANGE = Integer.parseInt(altSettings.getProperty("ItemDeletionRange", "5"));
			ALT_GMSHOP = Boolean.parseBoolean(altSettings.getProperty("GMshop", "false"));
			ALT_GMSHOP_MIN_ID = Integer.parseInt(altSettings.getProperty("GMshopMinID", "0xffffffff"));
			ALT_GMSHOP_MAX_ID = Integer.parseInt(altSettings.getProperty("GMshopMaxID", "0xffffffff"));
			ALT_BASETOWN = Boolean.parseBoolean(altSettings.getProperty("BaseTown", "false"));
			ALT_BASETOWN_MIN_ID = Integer.parseInt(altSettings.getProperty("BaseTownMinID", "0xffffffff"));
			ALT_BASETOWN_MAX_ID = Integer.parseInt(altSettings.getProperty("BaseTownMaxID", "0xffffffff"));
			WHOIS_CONTER = Integer.parseInt(altSettings.getProperty("WhoisConter", "0")); //
			ALT_WHO_COMMAND = Boolean.parseBoolean(altSettings.getProperty("WhoCommand", "false"));
			ALT_REVIVAL_POTION = Boolean.parseBoolean(altSettings.getProperty("RevivalPotion", "false"));
			ALT_BEGINNER_BONUS = Boolean.parseBoolean(altSettings.getProperty("BeginnerEvent", "false"));
			ALT_DROPLEVELLIMIT = Integer.parseInt(altSettings.getProperty("DropLevelLimit", "90"));
			AUTO_REMOVELEVEL = Integer.parseInt(altSettings.getProperty("AutoRemoveLevel", "50"));
			라던입장레벨 = Integer.parseInt(altSettings.getProperty("LarvaLevel", "70"));
			마법사마법대미지 = Double.parseDouble(altSettings.getProperty("Wizdmg", "1.0"));
			신규혈맹보호레벨 = Integer.parseInt(altSettings.getProperty("NewClanLevel", "50"));
			신규혈맹클랜 = Integer.parseInt(altSettings.getProperty("NewClanid", "1"));
			신규혈맹엠블런 = Integer.parseInt(altSettings.getProperty("NewClanEMB", "1"));
			신규혈맹보호처리 = Boolean.parseBoolean(altSettings.getProperty("NewClanPvP", "true"));
			신규혈맹이름 = altSettings.getProperty("NewClanName", "신규보호");

			String strWar;
			strWar = altSettings.getProperty("WarTime", "1h");
			if (strWar.indexOf("d") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", "");
			} else if (strWar.indexOf("h") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", "");
			} else if (strWar.indexOf("m") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", "");
			}
			ALT_WAR_TIME = Integer.parseInt(strWar);
			strWar = altSettings.getProperty("WarInterval", "2d");
			if (strWar.indexOf("d") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", "");
			} else if (strWar.indexOf("h") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", "");
			} else if (strWar.indexOf("m") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", "");
			}
			ALT_WAR_INTERVAL = Integer.parseInt(strWar);
			SPAWN_HOME_POINT = Boolean.parseBoolean(altSettings.getProperty("SpawnHomePoint", "true"));
			SPAWN_HOME_POINT_COUNT = Integer.parseInt(altSettings.getProperty("SpawnHomePointCount", "2"));
			SPAWN_HOME_POINT_DELAY = Integer.parseInt(altSettings.getProperty("SpawnHomePointDelay", "100"));
			SPAWN_HOME_POINT_RANGE = Integer.parseInt(altSettings.getProperty("SpawnHomePointRange", "8"));
			INIT_BOSS_SPAWN = Boolean.parseBoolean(altSettings.getProperty("InitBossSpawn", "true"));
			ELEMENTAL_STONE_AMOUNT = Integer.parseInt(altSettings.getProperty("ElementalStoneAmount", "300"));
			HOUSE_TAX_INTERVAL = Integer.parseInt(altSettings.getProperty("HouseTaxInterval", "10"));
			MAX_DOLL_COUNT = Integer.parseInt(altSettings.getProperty("MaxDollCount", "1"));
			RETURN_TO_NATURE = Boolean.parseBoolean(altSettings.getProperty("ReturnToNature", "false"));
			MAX_NPC_ITEM = Integer.parseInt(altSettings.getProperty("MaxNpcItem", "8"));
			MAX_PERSONAL_WAREHOUSE_ITEM = Integer.parseInt(altSettings.getProperty("MaxPersonalWarehouseItem", "100"));
			MAX_CLAN_WAREHOUSE_ITEM = Integer.parseInt(altSettings.getProperty("MaxClanWarehouseItem", "200"));
			DELETE_CHARACTER_AFTER_7DAYS = Boolean
					.parseBoolean(altSettings.getProperty("DeleteCharacterAfter7Days", "True"));
			GMCODE = Integer.parseInt(altSettings.getProperty("GMCODE", "9999"));
			SUB_GMCODE = Integer.parseInt(altSettings.getProperty("SUBGMCODE", "8888"));
			선포레벨 = Integer.parseInt(altSettings.getProperty("WarLevel", "60"));
			혈맹접속인원 = Integer.parseInt(altSettings.getProperty("WarPlayer", "1"));
			NEW_PLAYER = Integer.parseInt(altSettings.getProperty("NewPlayer", "0"));
			PC_SHOP = Integer.parseInt(altSettings.getProperty("Pcshop", "0"));
			NOTIS_TIME = Integer.parseInt(altSettings.getProperty("NotisTime", "10"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Failed to Load " + ALT_SETTINGS_FILE + " File.");
		}

		// TODO charsettings.properties 设置
		try {
			Properties charSettings = new Properties();
			InputStream is = new FileInputStream(new File(CHAR_SETTINGS_CONFIG_FILE));
			charSettings.load(is);
			is.close();

			PHYSICAL_REDUCTION_RATION = Double.parseDouble(charSettings.getProperty("PhysicalReductionRation", "1.3"));
			MAGIC_REDUCTION_RATION = Double.parseDouble(charSettings.getProperty("MagicReductionRation", "1.3"));
			
			PRINCE_MAX_HP = Integer.parseInt(charSettings.getProperty("PrinceMaxHP", "1000"));
			PRINCE_MAX_MP = Integer.parseInt(charSettings.getProperty("PrinceMaxMP", "800"));
			KNIGHT_MAX_HP = Integer.parseInt(charSettings.getProperty("KnightMaxHP", "1400"));
			KNIGHT_MAX_MP = Integer.parseInt(charSettings.getProperty("KnightMaxMP", "600"));
			ELF_MAX_HP = Integer.parseInt(charSettings.getProperty("ElfMaxHP", "1000"));
			ELF_MAX_MP = Integer.parseInt(charSettings.getProperty("ElfMaxMP", "900"));
			WIZARD_MAX_HP = Integer.parseInt(charSettings.getProperty("WizardMaxHP", "800"));
			WIZARD_MAX_MP = Integer.parseInt(charSettings.getProperty("WizardMaxMP", "1200"));
			DARKELF_MAX_HP = Integer.parseInt(charSettings.getProperty("DarkelfMaxHP", "1000"));
			DARKELF_MAX_MP = Integer.parseInt(charSettings.getProperty("DarkelfMaxMP", "900"));
			DRAGONKNIGHT_MAX_HP = Integer.parseInt(charSettings.getProperty("DragonknightMaxHP", "1000"));
			DRAGONKNIGHT_MAX_MP = Integer.parseInt(charSettings.getProperty("DragonknightMaxMP", "900"));
			BLACKWIZARD_MAX_HP = Integer.parseInt(charSettings.getProperty("BlackwizardMaxHP", "900"));
			BLACKWIZARD_MAX_MP = Integer.parseInt(charSettings.getProperty("BlackwizardMaxMP", "1100"));
			전사_MAX_HP = Integer.parseInt(charSettings.getProperty("WarriorMaxHP", "1400"));
			전사_MAX_MP = Integer.parseInt(charSettings.getProperty("WarriorMaxMP", "600"));
			LIMITLEVEL = Integer.parseInt(charSettings.getProperty("LimitLevel", "99"));
//		    NEWDMG = Integer.parseInt(charSettings.getProperty("NEWDMG", "99")); // 骑士团的武器
			/*
			 * MAX_LEVEL = Integer.parseInt(charSettings.getProperty("maxLevel",
			 * "90")); MAX_LEVEL_EXP = ExpTable.getExpByLevel(MAX_LEVEL) + 5;
			 */
			ACLEVEL = Integer.parseInt(charSettings.getProperty("aclevel", "200"));
			PRINCE_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("PrinceAddDamagePc", "0"));
			KNIGHT_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("KnightAddDamagePc", "0"));
			ELF_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("ElfAddDamagePc", "0"));
			WIZARD_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("WizardAddDamagePc", "0"));
			DARKELF_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("DarkelfAddDamagePc", "0"));
			DRAGONKNIGHT_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("DragonknightAddDamagePc", "0"));
			BLACKWIZARD_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("BlackwizardAddDamagePc", "0"));
			전사_ADD_DAMAGEPC = Integer.parseInt(charSettings.getProperty("WarriorAddDamagePc", "0"));
			
			PER_AC_PC_TO_PC = Integer.parseInt(charSettings.getProperty("AcPercentPcToPc", "33"));
			PER_AC_NPC_TO_PC = Integer.parseInt(charSettings.getProperty("AcPercentNpcToPc", "33"));
			PER_DODGE = Integer.parseInt(charSettings.getProperty("DodgePercent", "33"));
			PER_EVASION = Integer.parseInt(charSettings.getProperty("EvasionPercent", "33"));

			LV50_EXP = Integer.parseInt(charSettings.getProperty("Lv50Exp", "1"));
			LV51_EXP = Integer.parseInt(charSettings.getProperty("Lv51Exp", "1"));
			LV52_EXP = Integer.parseInt(charSettings.getProperty("Lv52Exp", "1"));
			LV53_EXP = Integer.parseInt(charSettings.getProperty("Lv53Exp", "1"));
			LV54_EXP = Integer.parseInt(charSettings.getProperty("Lv54Exp", "1"));
			LV55_EXP = Integer.parseInt(charSettings.getProperty("Lv55Exp", "1"));
			LV56_EXP = Integer.parseInt(charSettings.getProperty("Lv56Exp", "1"));
			LV57_EXP = Integer.parseInt(charSettings.getProperty("Lv57Exp", "1"));
			LV58_EXP = Integer.parseInt(charSettings.getProperty("Lv58Exp", "1"));
			LV59_EXP = Integer.parseInt(charSettings.getProperty("Lv59Exp", "1"));
			LV60_EXP = Integer.parseInt(charSettings.getProperty("Lv60Exp", "1"));
			LV61_EXP = Integer.parseInt(charSettings.getProperty("Lv61Exp", "1"));
			LV62_EXP = Integer.parseInt(charSettings.getProperty("Lv62Exp", "1"));
			LV63_EXP = Integer.parseInt(charSettings.getProperty("Lv63Exp", "1"));
			LV64_EXP = Integer.parseInt(charSettings.getProperty("Lv64Exp", "1"));
			LV65_EXP = Integer.parseInt(charSettings.getProperty("Lv65Exp", "2"));
			LV66_EXP = Integer.parseInt(charSettings.getProperty("Lv66Exp", "2"));
			LV67_EXP = Integer.parseInt(charSettings.getProperty("Lv67Exp", "2"));
			LV68_EXP = Integer.parseInt(charSettings.getProperty("Lv68Exp", "2"));
			LV69_EXP = Integer.parseInt(charSettings.getProperty("Lv69Exp", "2"));
			LV70_EXP = Integer.parseInt(charSettings.getProperty("Lv70Exp", "4"));
			LV71_EXP = Integer.parseInt(charSettings.getProperty("Lv71Exp", "4"));
			LV72_EXP = Integer.parseInt(charSettings.getProperty("Lv72Exp", "4"));
			LV73_EXP = Integer.parseInt(charSettings.getProperty("Lv73Exp", "4"));
			LV74_EXP = Integer.parseInt(charSettings.getProperty("Lv74Exp", "4"));
			LV75_EXP = Integer.parseInt(charSettings.getProperty("Lv75Exp", "8"));
			LV76_EXP = Integer.parseInt(charSettings.getProperty("Lv76Exp", "8"));
			LV77_EXP = Integer.parseInt(charSettings.getProperty("Lv77Exp", "8"));
			LV78_EXP = Integer.parseInt(charSettings.getProperty("Lv78Exp", "8"));
			LV79_EXP = Integer.parseInt(charSettings.getProperty("Lv79Exp", "16"));
			LV80_EXP = Integer.parseInt(charSettings.getProperty("Lv80Exp", "32"));
			LV81_EXP = Integer.parseInt(charSettings.getProperty("Lv81Exp", "64"));
			LV82_EXP = Integer.parseInt(charSettings.getProperty("Lv82Exp", "128"));
			LV83_EXP = Integer.parseInt(charSettings.getProperty("Lv83Exp", "256"));
			LV84_EXP = Integer.parseInt(charSettings.getProperty("Lv84Exp", "512"));
			LV85_EXP = Integer.parseInt(charSettings.getProperty("Lv85Exp", "1024"));
			LV86_EXP = Integer.parseInt(charSettings.getProperty("Lv86Exp", "2048"));
			LV87_EXP = Integer.parseInt(charSettings.getProperty("Lv87Exp", "4096"));
			LV88_EXP = Integer.parseInt(charSettings.getProperty("Lv88Exp", "8192"));
			LV89_EXP = Integer.parseInt(charSettings.getProperty("Lv89Exp", "16384"));
			LV90_EXP = Integer.parseInt(charSettings.getProperty("Lv90Exp", "32768"));
			LV91_EXP = Integer.parseInt(charSettings.getProperty("Lv91Exp", "65536"));
			LV92_EXP = Integer.parseInt(charSettings.getProperty("Lv92Exp", "131072"));
			LV93_EXP = Integer.parseInt(charSettings.getProperty("Lv93Exp", "262144"));
			LV94_EXP = Integer.parseInt(charSettings.getProperty("Lv94Exp", "524288"));
			LV95_EXP = Integer.parseInt(charSettings.getProperty("Lv95Exp", "1048576"));
			LV96_EXP = Integer.parseInt(charSettings.getProperty("Lv96Exp", "2097152"));
			LV97_EXP = Integer.parseInt(charSettings.getProperty("Lv97Exp", "4194304"));
			LV98_EXP = Integer.parseInt(charSettings.getProperty("Lv98Exp", "8388608"));
			LV99_EXP = Integer.parseInt(charSettings.getProperty("Lv99Exp", "16777216"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Failed to Load " + CHAR_SETTINGS_CONFIG_FILE + " File.");
		}

		// TODO Magic.properties 设置
		try {
			Properties Magic = new Properties();
			InputStream is = new FileInputStream(new File(Magic_SETTINGS_CONFIG_FILE));
			Magic.load(is);
			is.close();

			String s = Magic.getProperty("FouDamageTable", "0.3,0.3,0.3,0.3,0.3,0.5,0.6,0.7,1.0,1.0,1.0,1.0");
			String[] arr = s.split(",");
			FOU_DAMAGE_TABLE = new double[arr.length];
			for (int i = 0; i < arr.length; i++)
				FOU_DAMAGE_TABLE[i] = Double.parseDouble(arr[i]);

			s = Magic.getProperty("TripleDamageTable", "0.3,0.3,0.3,0.3,0.3,0.5,0.5,0.6,0.7,0.8,0.9,1.0");
			arr = s.split(",");
			TRIPLE_DAMAGE_TABLE = new double[arr.length];
			for (int i = 0; i < arr.length; i++)
				TRIPLE_DAMAGE_TABLE[i] = Double.parseDouble(arr[i]);

			FOU_DAMAGEN = Integer.parseInt(Magic.getProperty("FOUDAMAGEN", "10"));
			TRIPLE_DAMAGEN = Integer.parseInt(Magic.getProperty("TRIPLEDAMAGEN", "10"));
			
			IS_SPELL_DELAY_RUN = Boolean.parseBoolean(Magic.getProperty("IsSpellDelayRun", "true"));
			SPELL_DELAYOVER_PENDING = Integer.parseInt(Magic.getProperty("SpellDelayOverPending", "5"));
			SPELL_DELAYERROR = Long.parseLong(Magic.getProperty("SpellDelayError", "10L"));

			// TODO 免疫设定
			IMMUNE_Level = Integer.parseInt(Magic.getProperty("IMMUNELevel", "87"));
			IMMUNE_DMG = Double.parseDouble(Magic.getProperty("IMMUNEDMG", "0.60"));
			IMMUNE_Level1 = Integer.parseInt(Magic.getProperty("IMMUNELevel1", "86"));
			IMMUNE_DMG1 = Double.parseDouble(Magic.getProperty("IMMUNEDMG1", "0.50"));
			IMMUNE_Level2 = Integer.parseInt(Magic.getProperty("IMMUNELevel2", "85"));
			IMMUNE_DMG2 = Double.parseDouble(Magic.getProperty("IMMUNEDMG2", "0.45"));
			IMMUNE_Level3 = Integer.parseInt(Magic.getProperty("IMMUNELevel3", "80"));
			IMMUNE_DMG3 = Double.parseDouble(Magic.getProperty("IMMUNEDMG3", "0.35"));
			IMMUNE_Level4 = Integer.parseInt(Magic.getProperty("IMMUNELevel4", "75"));
			IMMUNE_DMG4 = Double.parseDouble(Magic.getProperty("IMMUNEDMG4", "0.25"));
			IMMUNE_Level5 = Integer.parseInt(Magic.getProperty("IMMUNELevel5", "70"));
			IMMUNE_DMG5 = Double.parseDouble(Magic.getProperty("IMMUNEDMG5", "0.20"));
			IMMUNE_Level6 = Integer.parseInt(Magic.getProperty("IMMUNELevel6", "65"));
			IMMUNE_DMG6 = Double.parseDouble(Magic.getProperty("IMMUNEDMG6", "0.15"));
			IMMUNE_DMG7 = Double.parseDouble(Magic.getProperty("IMMUNEDMG7", "0.10"));
			
				//TODO 新增命中/抗性的外部化
			Erase_MagicT = Double.parseDouble(Magic.getProperty("EraseMagicT", "1"));
			EARTH_BINDT = Double.parseDouble(Magic.getProperty("EARTHBINDT", "1"));
			STRIKER_GALET = Double.parseDouble(Magic.getProperty("STRIKERGALET", "1"));
			UNCANNYDODGE1 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE1", "1"));
			UNCANNYDODGE2 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE2", "1"));
			UNCANNYDODGE3 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE3", "1"));
			UNCANNYDODGE4 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE4", "1"));
			UNCANNYDODGE5 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE5", "1"));
			UNCANNYDODGE6 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE6", "1"));
			UNCANNYDODGE7 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE7", "1"));
			UNCANNYDODGE8 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE8", "1"));
			UNCANNYDODGE9 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE9", "1"));
			UNCANNYDODGE10 = Double.parseDouble(Magic.getProperty("UNCANNYDODGE10", "1"));
			UNCANNYDODGE11= Double.parseDouble(Magic.getProperty("UNCANNYDODGE11", "1"));
			UNCANNYDODGE12= Double.parseDouble(Magic.getProperty("UNCANNYDODGE12", "1"));
			UNCANNYDODGE13= Double.parseDouble(Magic.getProperty("UNCANNYDODGE13", "1"));
			UNCANNYDODGE14= Double.parseDouble(Magic.getProperty("UNCANNYDODGE14", "1"));
			UNCANNYDODGE15= Double.parseDouble(Magic.getProperty("UNCANNYDODGE15", "1"));
			UNCANNYDODGE16= Double.parseDouble(Magic.getProperty("UNCANNYDODGE16", "1"));
			UNCANNYDODGE17= Double.parseDouble(Magic.getProperty("UNCANNYDODGE17", "1"));
			UNCANNYDODGE18= Double.parseDouble(Magic.getProperty("UNCANNYDODGE18", "1"));
			UNCANNYDODGE19= Double.parseDouble(Magic.getProperty("UNCANNYDODGE19", "1"));
			
			MIRRORIMAGE = Double.parseDouble(Magic.getProperty("MIRRORIMAGE", "1"));
			POLLUTE_WATERT = Double.parseDouble(Magic.getProperty("POLLUTEWATERT", "1"));
			WIND_SHACKLET = Double.parseDouble(Magic.getProperty("WINDSHACKLET", "1"));
			ARMOR_BRAKET = Double.parseDouble(Magic.getProperty("ARMORBRAKET", "1"));
			GUARD_BREAKT = Double.parseDouble(Magic.getProperty("GUARDBREAKT", "1"));
			FEAR_T = Double.parseDouble(Magic.getProperty("FEART", "1"));
			MORTALBODY = Double.parseDouble(Magic.getProperty("MORTALBODY", "1"));
			THUNDER_GRABT = Double.parseDouble(Magic.getProperty("THUNDERGRABT", "1"));
			HORROR_OF_DEATHT = Double.parseDouble(Magic.getProperty("HORROROFDEATHT", "1"));
			CONFUSION_PHANTASMT = Double.parseDouble(Magic.getProperty("CONFUSIONPHANTASMT", "1"));
			POWERRIP_T = Double.parseDouble(Magic.getProperty("POWERRIPT", "1"));
			TOMAHAWKPoint_T = Double.parseDouble(Magic.getProperty("TOMAHAWKPointT", "1"));
			CYCLONE_PROBABILITY = Integer.parseInt(Magic.getProperty("CyncloneProbability", "30")) * 10000;

			INFERNO_RATION = MJCommons.parseToDoubleArrange(Magic.getProperty("InfernoReductionPercent", "0.3,0.4,0.5,0.6"), "\\,");
			INFERNO_PROBABILITY = Integer.parseInt(Magic.getProperty("InfernoProbability", "100000"));
			INFERNO_EFFECTS = MJCommons.parseToIntArrange(Magic.getProperty("InfernoEffects", "17561,17563,17565,17567"), "\\,");
			
			
			
			STRIKERGALEs = Integer.parseInt(Magic.getProperty("STRIKERGALE", "5"));
			EMPIREHIT_TO_LEVEL = Integer.parseInt(Magic.getProperty("EmpireHitToLevel", "5"));
			STUNHIT_TO_LEVEL = Integer.parseInt(Magic.getProperty("StunHitToLevel", "5"));
			HORRORHIT_TO_LEVEL = Integer.parseInt(Magic.getProperty("HorrorHitToLevel", "5"));
			DESTROYHIT_TO_LEVEL = Integer.parseInt(Magic.getProperty("DestroyHitToLevel", "5"));
			CHARACTER_MAGICHIT_RATE = Double.parseDouble(Magic.getProperty("CharacterMagicHitRate", "0.01"));
			CHARACTER_MAGICCRI_RATE = Double.parseDouble(Magic.getProperty("CharacterMagicCriticalRate", "1.5"));
			
			MISSILE_CRITICAL_DAMAGE_RATE = Double.parseDouble(Magic.getProperty("MissileCriticalDamageRate", "1.5"));
			MELEE_CRITICAL_DAMAGE_RATE = Double.parseDouble(Magic.getProperty("MeleeCriticalDamageRate", "1.5"));
			REDUCTION_IGNORE_DAMAGERATE = Double.parseDouble(Magic.getProperty("ReductionIgnoreDamageRate", "0.5"));
			MAGIC_CRITICAL_DAMAGE_RATE  = Double.parseDouble(Magic.getProperty("MagicCriticalDamageRate", "1.5"));
			
			EMPIRE = Double.parseDouble(Magic.getProperty("Empire", "1"));
			SHOCK_STUN = Double.parseDouble(Magic.getProperty("ShockStun", "1"));
			DESPERADO = Double.parseDouble(Magic.getProperty("DESPERADO", "1"));
			BONE_BREAK = Double.parseDouble(Magic.getProperty("BONEBREAK", "1"));
			DISINT_CHAOTIC_WEIGHT = Integer.parseInt(Magic.getProperty("DisInt_Chaotic_Weight", "5000"));
			DISINT_LAWFUL_WEIGHT = Integer.parseInt(Magic.getProperty("DisInt_Lawful_Weight", "200"));
			DISINT_MAX_DMG = Integer.parseInt(Magic.getProperty("DisInt_MaxDamage", "1500"));
			DISINT_MIN_DMG = Integer.parseInt(Magic.getProperty("DisInt_MinDamage", "200"));
			TurnChant80UP = Integer.parseInt(Magic.getProperty("turnchant80up", "1"));
			TurnChant76UP = Integer.parseInt(Magic.getProperty("turnchant76up", "1"));
			TurnChant75UP = Integer.parseInt(Magic.getProperty("turnchant75up", "1"));
			TurnChant74UP = Integer.parseInt(Magic.getProperty("turnchant74up", "1"));
			TurnChant73UP = Integer.parseInt(Magic.getProperty("turnchant73up", "1"));
			TurnChant72UP = Integer.parseInt(Magic.getProperty("turnchant72up", "1"));
			TurnChant71UP = Integer.parseInt(Magic.getProperty("turnchant71up", "1"));
			TurnChant70UP = Integer.parseInt(Magic.getProperty("turnchant70up", "1"));
			TurnChant69UP = Integer.parseInt(Magic.getProperty("turnchant69up", "1"));
			TurnChant68UP = Integer.parseInt(Magic.getProperty("turnchant68up", "1"));
			TurnChant67UP = Integer.parseInt(Magic.getProperty("turnchant67up", "1"));
			TurnChant66UP = Integer.parseInt(Magic.getProperty("turnchant66up", "1"));
			TurnChant65UP = Integer.parseInt(Magic.getProperty("turnchant65up", "1"));
			TurnChant64UP = Integer.parseInt(Magic.getProperty("turnchant64up", "1"));
			TurnChant63UP = Integer.parseInt(Magic.getProperty("turnchant63up", "1"));
			TurnChant62UP = Integer.parseInt(Magic.getProperty("turnchant62up", "1"));
			TurnChant61UP = Integer.parseInt(Magic.getProperty("turnchant61up", "1"));
			TurnChant60UP = Integer.parseInt(Magic.getProperty("turnchant60up", "1"));
			TurnChant59UP = Integer.parseInt(Magic.getProperty("turnchant59up", "1"));
			TurnChant58UP = Integer.parseInt(Magic.getProperty("turnchant58up", "1"));
			TurnChant57UP = Integer.parseInt(Magic.getProperty("turnchant57up", "1"));
			TurnChant56UP = Integer.parseInt(Magic.getProperty("turnchant56up", "1"));
			TurnChant55UP = Integer.parseInt(Magic.getProperty("turnchant55up", "1"));
			
			
			
			
			TurnChantElf = Integer.parseInt(Magic.getProperty("TurnChantElf", "1"));
			crashFo = Integer.parseInt(Magic.getProperty("crashFor", "1"));
			crashdmg = Double.parseDouble(Magic.getProperty("crashDMG", "1"));
			furyFo = Integer.parseInt(Magic.getProperty("furyFor", "1"));
			furydmg = Double.parseDouble(Magic.getProperty("FuryDMG", "1"));
			Counter = Double.parseDouble(Magic.getProperty("CounterR", "1"));
			TitanRock_probability = Integer.parseInt(Magic.getProperty("TitanRockprobability", "30"));
			Titanmagic_probability = Integer.parseInt(Magic.getProperty("Titanmagicprobability", "30"));
			Titanbullet_probability = Integer.parseInt(Magic.getProperty("Titanbulletprobability", "30"));
			RockDMG = Double.parseDouble(Magic.getProperty("RockDMGR", "1"));
			Finalburnpc = Double.parseDouble(Magic.getProperty("Finalburnpc", "1"));
			Finalburnnpc = Double.parseDouble(Magic.getProperty("Finalburnnpc", "1"));
			DOUBLE_BREAK_CHANCE = Double.parseDouble(Magic.getProperty("DOUBLEBREAKCHANCE", "1"));
			BURNING_SPIRIT_PC = Double.parseDouble(Magic.getProperty("BURNINGSPIRITPC", "1"));
			BURNING_SPIRIT_NPC = Double.parseDouble(Magic.getProperty("BURNINGSPIRITPCNPC", "1"));
			DOUBLE_DMG = Double.parseDouble(Magic.getProperty("DOUBLEDMG", "1"));
			DOUBLE_BREAK_DESTINY_PC = Double.parseDouble(Magic.getProperty("DOUBLEBREAKDESTINYPC", "1"));
			DOUBLE_BREAK_DESTINY_NPC = Double.parseDouble(Magic.getProperty("DOUBLEBREAKDESTINYNPC", "1"));
			BLOW_ATTACKPR = Integer.parseInt(Magic.getProperty("BLOWATTACKPR", "1")); // 정수
			BLOW_ATTACKDMG = Double.parseDouble(Magic.getProperty("BLOWATTACKDMG", "1"));

			sim_levelmin1 = Integer.parseInt(Magic.getProperty("simlevelmin1", "1"));
			sim_levelmax1 = Integer.parseInt(Magic.getProperty("simlevelmax1", "1"));

			sim_levelmin2 = Integer.parseInt(Magic.getProperty("simlevelmin2", "1"));
			sim_levelmax2 = Integer.parseInt(Magic.getProperty("simlevelmax2", "1"));

			sim_levelmin3 = Integer.parseInt(Magic.getProperty("simlevelmin3", "1"));
			sim_levelmax3 = Integer.parseInt(Magic.getProperty("simlevelmax3", "1"));

			sim_levelmin4 = Integer.parseInt(Magic.getProperty("simlevelmin4", "1"));
			sim_levelmax4 = Integer.parseInt(Magic.getProperty("simlevelmax4", "1"));

			sim_levelmin5 = Integer.parseInt(Magic.getProperty("simlevelmin5", "1"));
			sim_levelmax5 = Integer.parseInt(Magic.getProperty("simlevelmax5", "1"));

			sim_levelmin6 = Integer.parseInt(Magic.getProperty("simlevelmin6", "1"));
			sim_levelmax6 = Integer.parseInt(Magic.getProperty("simlevelmax6", "1"));

			simsimDmg1 = Double.parseDouble(Magic.getProperty("simsimDmg1", "3"));
			simsimDmg2 = Double.parseDouble(Magic.getProperty("simsimDmg2", "3"));
			simsimDmg3 = Double.parseDouble(Magic.getProperty("simsimDmg3", "3"));
			simsimDmg4 = Double.parseDouble(Magic.getProperty("simsimDmg4", "3"));
			simsimDmg5 = Double.parseDouble(Magic.getProperty("simsimDmg5", "3"));
			simsimDmg6 = Double.parseDouble(Magic.getProperty("simsimDmg6", "3"));

		} catch (Exception e) {
			_log.log(Level.SEVERE, "Config.에서 에러가 발생했습니다.", e);
			throw new Error("Failed to Load " + Magic_SETTINGS_CONFIG_FILE + " File.");
		}

		try {
			Properties ServerCodes = new Properties();
			InputStream is = new FileInputStream(new File(ServerCodes_SETTINGS_CONFIG_FILE));
			ServerCodes.load(is);
			is.close();
			DROPMENT_ITEM.clear();
			String ments = ServerCodes.getProperty("Dropmentitem", "").trim();
			if(!MJCommons.isNullOrEmpty(ments)){
				String[] mentArray = ments.split(",");
				for(int i=mentArray.length - 1; i>=0; --i){
					int itemId = Integer.parseInt(mentArray[i]);
					DROPMENT_ITEM.put(itemId, itemId);
				}
			}

			IS_CHECKBOX_RULES_USE = Boolean.parseBoolean(ServerCodes.getProperty("IsCheckBoxRulesUse", "true"));
			IS_VALIDSHOP = Boolean.parseBoolean(ServerCodes.getProperty("IsValidShopSystem", "true"));
			IS_VALIDITEMID = Boolean.parseBoolean(ServerCodes.getProperty("IsValidItemId", "true"));
			// TODO ServerCodes.properties 셋팅
			RedKnight_dieCount = Integer.parseInt(ServerCodes.getProperty("RedKnightdieCount", "5"));
			Arden = Boolean.parseBoolean(ServerCodes.getProperty("ArdenTo", "false"));
			아덴상점엔샵아덴샵설정 = Double.parseDouble(ServerCodes.getProperty("Adentype", "5"));
			ENCHANT_MAX_FAIL = Boolean.parseBoolean(ServerCodes.getProperty("EnchantMaxFail", "true"));
			MASTERENCHANTMESS = Boolean.parseBoolean(ServerCodes.getProperty("MasterEnchantMess", "true"));
			ENCHANT_FAIL_RATE_ONEST = Double.parseDouble(ServerCodes.getProperty("EnchantFailRateOnest", "1.0"));
			ENCHANT_FAIL_RATE_ONESTO = Double.parseDouble(ServerCodes.getProperty("EnchantFailRateOnesto", "1.0"));
			ENCHANT_FAIL_RATE_ONE = Double.parseDouble(ServerCodes.getProperty("EnchantFailRateOne", "1.0"));
			ENCHANT_FAIL_RATE_TWO = Double.parseDouble(ServerCodes.getProperty("EnchantFailRateTwo", "1.0"));
			ENCHANT_COENT = Double.parseDouble(ServerCodes.getProperty("EnchantCoent", "1.0"));
			Master_Enchant = Double.parseDouble(ServerCodes.getProperty("MasterEnchant", "1.0"));
			New_Cha = Integer.parseInt(ServerCodes.getProperty("NewCha", "1"));
			New_Cha1 = Integer.parseInt(ServerCodes.getProperty("NewCha1", "1"));
			경험치지급단 = Integer.parseInt(ServerCodes.getProperty("Expreturn", "75"));
			Npc_Max = Integer.parseInt(ServerCodes.getProperty("NpcMax", "70"));
			Pc_Reload = Integer.parseInt(ServerCodes.getProperty("PcReload", "70"));
			Npc_Location = Integer.parseInt(ServerCodes.getProperty("NpcLocation", "15"));
			COMBO_CHANCE = Integer.parseInt(ServerCodes.getProperty("ComboChance", "10"));
			BossGht_CHANCE = Double.parseDouble(ServerCodes.getProperty("BossGhtChance", "0.1"));
			무기인첸트 = Integer.parseInt(ServerCodes.getProperty("LimitWeapon", "13")); // 일반무기
			무기고급인첸트 = Integer.parseInt(ServerCodes.getProperty("LimitWeapon2", "5")); // 특수무기
			방어구인첸트 = Integer.parseInt(ServerCodes.getProperty("LimitArmor", "11")); // 일반갑옷
			방어구고급인첸트 = Integer.parseInt(ServerCodes.getProperty("LimitArmor2", "7")); // 특수아머
			룸티스 = Integer.parseInt(ServerCodes.getProperty("RoomT", "3"));
			스냅퍼 = Integer.parseInt(ServerCodes.getProperty("Snapper", "3"));
			악세사리 = Integer.parseInt(ServerCodes.getProperty("Accessory", "3"));
			accessoryLevel = Integer.parseInt(ServerCodes.getProperty("accessoryLevel", "3"));
			accessorytest = Integer.parseInt(ServerCodes.getProperty("Accessorytest", "3"));
			문장류 = Integer.parseInt(ServerCodes.getProperty("sentence", "3"));
			summon_hpdmg = Integer.parseInt(ServerCodes.getProperty("summonhpdmg", "100"));
			FEATHER_SHOP_NUM = Integer.parseInt(ServerCodes.getProperty("FeatherShopNum", "100000"));
			ACCOUNT_N_BUFF = Boolean.parseBoolean(ServerCodes.getProperty("AccountNBuff", "true"));
			LIMIT_N_BUFF = Integer.parseInt(ServerCodes.getProperty("LimitNBuff", "30"));
			TIME_N_BUFF = Integer.parseInt(ServerCodes.getProperty("TimeNBuff", "30"));
			/** NPC物理伤害/魔法伤害修正 **/
			npcdmg1 = Integer.parseInt(ServerCodes.getProperty("npcdmg1", "1"));
			npcdmg2 = Integer.parseInt(ServerCodes.getProperty("npcdmg2", "1"));
			npcdmg3 = Integer.parseInt(ServerCodes.getProperty("npcdmg3", "1"));
			pcnpcmagicdmg = Integer.parseInt(ServerCodes.getProperty("pcnpcmagicdmg", "14"));
			New_MagicDmg = Double.parseDouble(ServerCodes.getProperty("NewMagicDmg", "1"));

			BugBug = Integer.parseInt(ServerCodes.getProperty("BugBug", "1"));
			BugBug1 = Integer.parseInt(ServerCodes.getProperty("BugBug1", "1"));
			tamsc = Integer.parseInt(ServerCodes.getProperty("tamsc", "0"));
			tamsc1 = Integer.parseInt(ServerCodes.getProperty("tamsc1", "0"));
			tamsc2 = Integer.parseInt(ServerCodes.getProperty("tamsc2", "0"));
			SCARELEVEL = Integer.parseInt(ServerCodes.getProperty("ScareLevel", "5"));
			Characters_CharSlot = Integer.parseInt(ServerCodes.getProperty("CharactersCharSlot", "3"));
			YN_pclevel = Integer.parseInt(ServerCodes.getProperty("YNpclevel", "3"));
			/** 古代之书 **/

			sasinweapon = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("sasinweapon", "100,100,100,50,30,15,8,7,6,5,1,1,1,1,1"), ",", MJArrangeParseeFactory.createIntArrange())
					.result();
			
			rareweapon = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("rareweapon", "50,30,15,10,8,7,6,5,4,1,1,1,1,1,1"), ",", MJArrangeParseeFactory.createIntArrange())
					.result();
			
			weapon = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("weapon", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			
			sasinweaponLevel = Integer.parseInt(ServerCodes.getProperty("sasinweaponLevel", "1"));
			
			rareweaponLevel = Integer.parseInt(ServerCodes.getProperty("rareweaponLevel", "1"));
			
			weaponLevel = Integer.parseInt(ServerCodes.getProperty("weaponLevel", "1"));
			
			
			armor = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("armor", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			armorLevel = Integer.parseInt(ServerCodes.getProperty("armorLevel", "1"));
			accessory = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("accessory", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			ancient = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("Ancient", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			bless_orim = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("Bless_orim", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();

			Roomtis = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("Roomtis", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			Sanpper = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("Sanpper", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			Sentence = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("Sentence", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			Insignia = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("Insignia", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();
			Should = (Integer[]) MJArrangeParser
					.parsing(ServerCodes.getProperty("Should", "30,20,15,13,10,8,6,4,2,1,1,1,1,1,1"), ",",
							MJArrangeParseeFactory.createIntArrange())
					.result();

			RoomtisLevel = Integer.parseInt(ServerCodes.getProperty("RoomtisLevel", "1"));
			SnapperLevel = Integer.parseInt(ServerCodes.getProperty("SanpperLevel", "1"));
			SentenceLevel = Integer.parseInt(ServerCodes.getProperty("SentenceLevel", "1"));
			Insignia_Level = Integer.parseInt(ServerCodes.getProperty("InsigniaLevel", "1"));
			Should_Level = Integer.parseInt(ServerCodes.getProperty("ShouldLevel", "1"));

			Dr_weapon = Integer.parseInt(ServerCodes.getProperty("Drweapon", "1"));
			Hero_weapon = Integer.parseInt(ServerCodes.getProperty("Heroweapon", "1"));
			HUNT_EVENT = Boolean.parseBoolean(ServerCodes.getProperty("HuntEvent", "false"));
			POLY_EVENT1 = Boolean.parseBoolean(ServerCodes.getProperty("PolyEvent1", "false"));
			POLY_EVENT = Boolean.parseBoolean(ServerCodes.getProperty("PolyEvent", "false"));
			MANAGER_CLEAR = Boolean.parseBoolean(ServerCodes.getProperty("ManagerClear", "true"));
			MANAGER_CLEAR_TIME = Integer.parseInt(ServerCodes.getProperty("ManagerClearTime", "7200"));
			/** 新增配置 **/
			키링크 = Integer.parseInt(ServerCodes.getProperty("KingD", "12"));// 王族连击
			데스나이트헬파이어 = Integer.parseInt(ServerCodes.getProperty("dethshellpa", "1"));
			DEDMG = Integer.parseInt(ServerCodes.getProperty("Dedmg", "1"));
			DEDMG2 = Integer.parseInt(ServerCodes.getProperty("Dedmg2", "1"));
			지배자헌신마법확률 = Integer.parseInt(ServerCodes.getProperty("effectDmg", "1"));
			DEDMG1 = Integer.parseInt(ServerCodes.getProperty("Dedmg1", "1"));
			RANK_POTION = Integer.parseInt(ServerCodes.getProperty("RankPotion", "10"));
			Bless_Chance = Integer.parseInt(ServerCodes.getProperty("blessChance", "1"));
			Ad_Sc = Integer.parseInt(ServerCodes.getProperty("AdSc", "10"));
			Tax_Rate = Integer.parseInt(ServerCodes.getProperty("TaxRate", "10"));
			Tax_RateMin = Integer.parseInt(ServerCodes.getProperty("TaxRateMin", "10"));
			Tax_RateMax = Integer.parseInt(ServerCodes.getProperty("TaxRateMax", "10"));
			Lineage_Buff = Integer.parseInt(ServerCodes.getProperty("LineageBuff", "10"));
			Start_Char_Boho = Integer.parseInt(ServerCodes.getProperty("StartCharBoho", "10"));
			ExpPo = Integer.parseInt(ServerCodes.getProperty("ExpPosis", "86"));
			ExpPo1 = Integer.parseInt(ServerCodes.getProperty("ExpPosis1", "87"));
			Garnet_Rnd = Integer.parseInt(ServerCodes.getProperty("GarnetRnd", "300000"));
			Garnet_Count = Integer.parseInt(ServerCodes.getProperty("GarnetCount", "1"));
			
			기사 = Double.parseDouble(ServerCodes.getProperty("KK", "1.5"));
			용기사 = Double.parseDouble(ServerCodes.getProperty("DK", "1.5"));
			요정 = Double.parseDouble(ServerCodes.getProperty("EF", "1.5"));
			군주 = Double.parseDouble(ServerCodes.getProperty("KC", "1.5"));
			법사 = Double.parseDouble(ServerCodes.getProperty("MM", "1.5"));
			다엘 = Double.parseDouble(ServerCodes.getProperty("DE", "1.5"));
			환술사 = Double.parseDouble(ServerCodes.getProperty("MB", "1.5"));
			전사 = Double.parseDouble(ServerCodes.getProperty("WR", "1.5"));

			IS_PARTY_EXP = Boolean.parseBoolean(ServerCodes.getProperty("IsPartyExp", "false"));
			ADD_PARTY_EXP = Double.parseDouble(ServerCodes.getProperty("AddPartyExp", "1.0"));
			BLESSING = Double.parseDouble(ServerCodes.getProperty("Blessing", "0.0"));
			UN_BLESSING = Double.parseDouble(ServerCodes.getProperty("Unblessing", "0.0"));
			ADENA_RATE_OF_BLESSING = Double.parseDouble(ServerCodes.getProperty("AdenaRateOfBlessing", "1.0"));
			ADENA_RATE_OF_UNBLESSING = Double.parseDouble(ServerCodes.getProperty("AdenaRateOfUnBlessing", "1.0"));
			StringTokenizer st = new StringTokenizer(ServerCodes.getProperty("buyer1", ""), ",");
			int index = 0;
			while (st.hasMoreTokens()) {
				level1[index] = (Integer.valueOf(st.nextToken()));
				index++;
			}
			st = new StringTokenizer(ServerCodes.getProperty("buyer2", ""), ",");
			index = 0;
			while (st.hasMoreTokens()) {
				level2_1[index] = (Integer.valueOf(st.nextToken()));
				index++;
			}
			st = new StringTokenizer(ServerCodes.getProperty("buyer2_2", ""), ",");
			index = 0;
			while (st.hasMoreTokens()) {
				level2_2[index] = (Integer.valueOf(st.nextToken()));
				index++;
			}
			st = new StringTokenizer(ServerCodes.getProperty("buyer3", ""), ",");
			index = 0;
			while (st.hasMoreTokens()) {
				level3_1[index] = (Integer.valueOf(st.nextToken()));
				index++;
			}
			st = new StringTokenizer(ServerCodes.getProperty("buyer3_2", ""), ",");
			index = 0;
			while (st.hasMoreTokens()) {
				level3_2[index] = (Integer.valueOf(st.nextToken()));
				index++;
			}
			st = new StringTokenizer(ServerCodes.getProperty("buyer3_3", ""), ",");
			index = 0;
			while (st.hasMoreTokens()) {
				level3_3[index] = (Integer.valueOf(st.nextToken()));
				index++;
			}

		} catch (Exception e) {
			_log.log(Level.SEVERE, "Config.에서 에러가 발생했습니다.", e);
			throw new Error("Failed to Load " + ServerCodes_SETTINGS_CONFIG_FILE + " File.");
		}
		/** 特殊强化系统 **/
		try {
			Properties MagicEnchantSettings = new Properties();
			InputStream is = new FileInputStream(new File(MAGIC_ENCHANT_SETTINGS_CONFIG_FILE));
			MagicEnchantSettings.load(is);
			is.close();

			// TODO ServerCodes.properties 셋팅
			Doll_Enchant_Per_lvl1 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_Enchant_Per_lvl1", "10"));
			Doll_Enchant_Per_lvl2 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_Enchant_Per_lvl2", "8"));
			Doll_Enchant_Per_lvl3 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_Enchant_Per_lvl3", "6"));
			Doll_Enchant_Per_lvl4 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_Enchant_Per_lvl4", "4"));
			Doll_Enchant_Per_lvl5 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_Enchant_Per_lvl5", "2"));

			Doll_Attack_Chance = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_attack_chance", "10"));
			Doll_Attack_Dmg_Lvl1 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_attack_dmg_lvl_1", "100"));
			Doll_Attack_Dmg_Lvl2 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_attack_dmg_lvl_2", "110"));
			Doll_Attack_Dmg_Lvl3 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_attack_dmg_lvl_3", "120"));
			Doll_Attack_Dmg_Lvl4 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_attack_dmg_lvl_4", "130"));
			Doll_Attack_Dmg_Lvl5 = Integer.parseInt(MagicEnchantSettings.getProperty("Doll_attack_dmg_lvl_5", "140"));

			Weapon_Magic_Per = Integer.parseInt(MagicEnchantSettings.getProperty("Weapon_Magic_Per", "10"));

			Weapon_Enchant_Per_lvl1 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Per_lvl1", "15"));
			Weapon_Enchant_Per_lvl2 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Per_lvl2", "13"));
			Weapon_Enchant_Per_lvl3 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Per_lvl3", "10"));
			Weapon_Enchant_Per_lvl4 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Per_lvl3", "7"));

			Weapon_Enchant_Dmg_lvl1 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Dmg_lvl1", "300"));
			Weapon_Enchant_Dmg_lvl2 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Dmg_lvl2", "400"));
			Weapon_Enchant_Dmg_lvl3 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Dmg_lvl3", "500"));
			Weapon_Enchant_Dmg_lvl4 = Integer
					.parseInt(MagicEnchantSettings.getProperty("Weapon_Enchant_Dmg_lvl3", "600"));

		} catch (Exception e) {
			_log.log(Level.SEVERE, "Config.에서 에러가 발생했습니다.", e);
			throw new Error("Failed to Load " + MAGIC_ENCHANT_SETTINGS_CONFIG_FILE + " File.");
		}
		/** 特殊强化系统 **/

		/** 乐透系统 **/
		try {
			Properties LottotSettings = new Properties();
			InputStream is = new FileInputStream(new File(LOTTO_SETTINGS_CONFIG_FILE));
			LottotSettings.load(is);
			is.close();

			// TODO Lotto.properties 셋팅
			LOTTO = Boolean.parseBoolean(LottotSettings.getProperty("Lotto", "true"));
			LOTTO_LEVEL = Integer.parseInt(LottotSettings.getProperty("LottoLevel", "52"));
			LOTTO_BATTING = Integer.parseInt(LottotSettings.getProperty("LottoBatting", "50000"));
			LOTTO_BONUS = Integer.parseInt(LottotSettings.getProperty("LottoBonus", "0"));
			LOTTO_TIME = LottotSettings.getProperty("LottoTime", "1:30,5:30,14:30,20:30");

		} catch (Exception e) {
			_log.log(Level.SEVERE, "Config.에서 에러가 발생했습니다.", e);
			throw new Error("Failed to Load " + LOTTO_SETTINGS_CONFIG_FILE + " File.");
		}
		/** 彩票系统 **/
		
		/** 拍卖交易系统 **/
		try {
			Properties AuctionSettings = new Properties();
			InputStream is = new FileInputStream(new File(AUCTION_SETTINGS_CONFIG_FILE));
			AuctionSettings.load(is);
			is.close();

			// TODO Auction.properties 셋팅
			ADENASHOP_LEVEL = Integer.parseInt(AuctionSettings.getProperty("ADENASHOPLEVEL", "80"));
			MAX_SELL_ADENA = Integer.parseInt(AuctionSettings.getProperty("MAXSELLADENA", "1000000000"));
			MIN_SELL_ADENA = Integer.parseInt(AuctionSettings.getProperty("MINSELLADENA", "10000000"));
			MIN_SELL_CASH = Integer.parseInt(AuctionSettings.getProperty("MINSELLCASH", "5000"));
			MAX_SELL_CASH = Integer.parseInt(AuctionSettings.getProperty("MAXSELLCASH", "500000"));

		} catch (Exception e) {
			_log.log(Level.SEVERE, "Config.에서 에러가 발생했습니다.", e);
			throw new Error("Failed to Load " + AUCTION_SETTINGS_CONFIG_FILE + " File.");
		}
		/** 中介交易系统 **/
		
		validate();
	}

	private static void validate() {
		if (!IntRange.includes(Config.ALT_ITEM_DELETION_RANGE, 0, 5)) {
			throw new IllegalStateException("ItemDeletionRange의 값이 설정 가능 범위외입니다. ");
		}
		if (!IntRange.includes(Config.ALT_ITEM_DELETION_TIME, 1, 35791)) {
			throw new IllegalStateException("ItemDeletionTime의 값이 설정 가능 범위외입니다. ");
		}
	}

	public static boolean setParameterValue(String pName, String pValue) {
		/** server.properties **/
		if (pName.equalsIgnoreCase("GameserverName")) {
			try {
				GAME_SERVER_NAME = new String(pValue.getBytes("ISO-8859-1"), "euc-kr");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if (pName.equalsIgnoreCase("AltNonPvP")) {
			ALT_NONPVP = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("AttackMessageOn")) {
			ALT_ATKMSG = Boolean.valueOf(pValue);
		} else {
			return false;
		}
		return true;
	}

	private Config() {
	}

	public final static int etc_arrow = 0;

	public final static int etc_wand = 1;

	public final static int etc_light = 2;

	public final static int etc_gem = 3;

	public final static int etc_potion = 6;

	public final static int etc_firecracker = 5;

	public final static int etc_food = 7;

	public final static int etc_scroll = 8;

	public final static int etc_questitem = 9;

	public final static int etc_spellbook = 10;

	public final static int etc_other = 12;

	public final static int etc_material = 13;

	public final static int etc_sting = 15;

	public final static int etc_treasurebox = 16;
}
