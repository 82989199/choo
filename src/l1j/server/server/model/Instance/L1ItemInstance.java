package l1j.server.server.model.Instance;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.MJCTSystem.MJCTObject;
import l1j.server.MJCTSystem.Loader.MJCTLoadManager;
import l1j.server.MJCTSystem.Loader.MJCTSystemLoader;
import l1j.server.MJINNSystem.MJINNHelper;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.ItemPresentator.ItemPresentatorFactory;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.PrideSystem.PrideInfo;
import l1j.server.PrideSystem.PrideLoadManager;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool; //CrockController
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EquipmentTimer;
import l1j.server.server.model.L1ItemOwnerTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_UnityIcon;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemBookMark;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.ItemPresentOutStream;
import l1j.server.server.utils.MJBytesOutputStream;

public class L1ItemInstance extends L1Object {

	public static String to_simple_description(String name, int bless, int enchant, int elemental, int level) {
		StringBuilder sb = new StringBuilder(name.length() + 32);
		sb.append(get_blessed_description(bless));
		sb.append(get_attribute_enchant_description(elemental));
		sb.append(get_enchant_description(enchant));
		sb.append(name);
		sb.append(get_level_description(level));
		return sb.toString();
	}

	public static String get_level_description(int level) {
		switch (level) {
		case 1:
			return "[1단계]";
		case 2:
			return "[2단계]";
		case 3:
			return "[3단계]";
		case 4:
			return "[4단계]";
		case 5:
			return "[5단계]";
		}
		return "";
	}

	public static String get_enchant_description(int enchant) {
		if (enchant > 0)
			return String.format("+%d ", enchant);
		else if (enchant < 0)
			return String.format("-%d ", enchant);
		return "";
	}

	public static String get_blessed_description(int bless) {
		switch (bless) {
		case 0:
			return "축복받은 ";
		case 2:
			return "저주받은 ";
		}
		return "";
	}

	private static final String[] elemental_descriptions = new String[] { "", // 무속성
			"$6115", // 화령1
			"$6116", // 화령2
			"$6117", // 화령3
			"$14361", // 화령4
			"$14365", // 화령5

			"$6118", // 수령1
			"$6119", // 수령2
			"$6120", // 수령3
			"$14362", // 수령4
			"$14366", // 수령5

			"$6121", // 풍령1
			"$6122", // 풍령2
			"$6123", // 풍령3
			"$14363", // 풍령4
			"$14367", // 풍령5

			"$6124", // 지령1
			"$6125", // 지령2
			"$6126", // 지령3
			"$14364", // 지령4
			"$14368", // 지령5
	};

	public static String get_attribute_enchant_description(int attrEnchantLevel) {
		return attrEnchantLevel >= elemental_descriptions.length ? ""
				: attrEnchantLevel < 0 ? "" : elemental_descriptions[attrEnchantLevel];
	}

	private static final int[] _attrMask = new int[] { 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4 };

	public static int attrEnchantToElementalType(int attrEnchantLevel) {
		return _attrMask[attrEnchantLevel];
	}

	public static int attrEnchantToElementalType(L1ItemInstance item) {
		return attrEnchantToElementalType(item.getAttrEnchantLevel());
	}

	public static int pureAttrEnchantLevel(int attrEnchantLevel) {
		return attrEnchantLevel <= 0 ? attrEnchantLevel
				: attrEnchantLevel - ((attrEnchantToElementalType(attrEnchantLevel) - 1) * 5);
	}

	public static int pureAttrEnchantLevel(L1ItemInstance item) {
		return pureAttrEnchantLevel(item.getAttrEnchantLevel());
	}

	public static boolean equalsElement(L1ItemInstance item, int elementalType, int elementalValue) {
		int attr = item.getAttrEnchantLevel();
		int type = attrEnchantToElementalType(attr);
		if (type != elementalType)
			return false;

		int value = attr - ((type - 1) * 5);
		return value == elementalValue;
	}

	public static int calculateElementalEnchant(int elementalType, int elementalValue) {
		return ((elementalType - 1) * 5) + elementalValue;
	}

	public static final int CHAOS_SPIRIT = 1;
	public static final int CORRUPT_SPIRIT = 2;
	public static final int BALLACAS_SPIRIT = 3;
	public static final int ANTARAS_SPIRIT = 4;
	public static final int LINDBIOR_SPIRIT = 5;
	public static final int PAPURION_SPIRIT = 6;
	public static final int DEATHKNIGHT_SPIRIT = 7;
	public static final int BAPPOMAT_SPIRIT = 8;
	public static final int BALLOG_SPIRIT = 9;
	public static final int ARES_SPIRIT = 10;

	private static final long serialVersionUID = 1L;

	public boolean _isSecond = false;

	private int _count;

	private int _itemId;

	private L1Item _item;

	private boolean _isEquipped = false;

	private int _enchantLevel;

	private int _attrenchantLevel;

	private boolean _isIdentified = false;

	private int _durability;

	private int _chargeCount;

	private int _specialEnchant;

	private int _remainingTime;

	private Timestamp _lastUsed = null;

	private Timestamp _endTime = null;

	/** 패키지상점 **/
	private boolean _isPackage = false;

	private int bless;

	private int _lastWeight;

	/** 인형 착용 여부 **/
	private boolean _isDollOn = false;

	private final LastStatus _lastStatus = new LastStatus();

	private Map<Integer, EnchantTimer> _skillEffect = new HashMap<Integer, EnchantTimer>();

	public L1PcInstance _cha;

	public L1ItemInstance() {
		_count = 1;
		_enchantLevel = 0;
		_specialEnchant = 0;
		_bookmarks = new ArrayList<L1ItemBookMark>();
	}

	public L1ItemInstance(L1Item item, int count) {
		this();
		setItem(item);
		setCount(count);
	}

	public L1ItemInstance(L1Item item) {
		this(item, 1);
	}

	public void clickItem(L1Character cha, ClientBasePacket packet) {
		
	}

	/** 인형 착용 여부 **/
	public boolean isDollOn() {
		return _isDollOn;
	}

	public void setDollOn(boolean DollOn) {
		_isDollOn = DollOn;
	}

	/** 인형 착용 여부 **/

	public boolean isSpecialEnchantable() {
		return (_specialEnchant & 0xFF) == 1;
	}

	public void setSpecialEnchantable() {
		_specialEnchant = 1;
	}

	public int getSpecialEnchant() {
		return _specialEnchant;
	}

	public int getSpecialEnchant(int index) {
		return ((_specialEnchant >> (8 * index)) & 0xFF);
	}

	public void setSpecialEnchant(int enchant) {
		_specialEnchant = enchant;
	}

	public void setSpecialEnchant(int index, int enchant) {
		_specialEnchant |= enchant << (8 * index);
	}

	public boolean isIdentified() {
		return _isIdentified;
	}

	public void setIdentified(boolean identified) {
		_isIdentified = identified;
	}

	public String getName() {
		return _item.getName();
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int count) {
		_count = count;
	}

	public boolean isEquipped() {
		return _isEquipped;
	}

	public void setEquipped(boolean equipped) {
		_isEquipped = equipped;
	}

	public L1Item getItem() {
		return _item;
	}

	public void setItem(L1Item item) {
		_item = item;
		_itemId = item.getItemId();
	}

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int itemId) {
		_itemId = itemId;
	}
	
	
	
	private String _ShopStr = "";
	public String getShopStr() {
		return _ShopStr;
	}

	public void setShopStr(String ShopStr) {
		_ShopStr = ShopStr;
	}
	
	
	

	public boolean isStackable() {
		return _item.isStackable();
	}

	@Override
	public void onAction(L1PcInstance player) {
	}

	public int getEnchantLevel() {
		return _enchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		_enchantLevel = enchantLevel;
	}

	public int getAttrEnchantLevel() {
		return _attrenchantLevel;
	}

	public int getHitModifierByAttrEnchant() {
		if (getAttrEnchantLevel() == 0) {
			return 0;
		} else if (getAttrEnchantLevel() % 3 == 0) {
			return 3;
		}

		return getAttrEnchantLevel() % 3;
	}

	public void setAttrEnchantLevel(int attrenchantLevel) {
		_attrenchantLevel = attrenchantLevel;
	}

	public int get_gfxid() {
		return _item.getGfxId();
	}

	public int get_durability() {
		return _durability;
	}

	public int getChargeCount() {
		return _chargeCount;
	}

	public void setChargeCount(int i) {
		_chargeCount = i;
	}

	public int getRemainingTime() {
		return _remainingTime;
	}

	public void setRemainingTime(int i) {
		_remainingTime = i;
	}

	public void setLastUsed(Timestamp t) {
		_lastUsed = t;
	}

	public Timestamp getLastUsed() {
		return _lastUsed;
	}

	public int getBless() {
		return bless;
	}

	public void setBless(int i) {
		bless = i;
	}

	public int getLastWeight() {
		return _lastWeight;
	}

	public void setLastWeight(int weight) {
		_lastWeight = weight;
	}

	public Timestamp getEndTime() {
		return _endTime;
	}

	public void setEndTime(Timestamp t) {
		_endTime = t;
	}

	/** 패키지상점 **/
	public boolean isPackage() {
		return _isPackage;
	}

	public void setPackage(boolean _isPackage) {
		this._isPackage = _isPackage;
	}

	private long _itemdelay3;

	public long getItemdelay3() {
		return _itemdelay3;
	}

	public void setItemdelay3(long itemdelay3) {
		_itemdelay3 = itemdelay3;
	}

	public int getMr() {
		int mr = _item.get_mdef();
		int itemid = getItemId();
		if (itemid == 20011 || itemid == 20110 || itemid == 120011 || itemid == 22223 || itemid == 20117 // 바포갑빠
				|| getItemId() == 22204 || itemid == 22205 || itemid == 22206 || itemid == 22207 // 린드비오르마갑주
				|| getItemId() == 22213 || itemid == 120110 || itemid == 93001 || itemid == 490008 || itemid == 22365
				|| itemid == 222328 || getItemId() >= 222300 && getItemId() <= 222303 || itemid == 222328 || itemid == 111137 || itemid == 111141 || itemid == 111140) {
			mr += getEnchantLevel();
		} else if (itemid == 20056 || itemid == 120056 || itemid == 220056 || itemid == 93002 || itemid == 20074
				|| itemid == 900090 || itemid == 120074 || itemid == 222324 || itemid == 222325 || itemid == 900043
				|| itemid == 900044 || itemid == 202022 || itemid == 22360 || itemid == 21117) {
			mr += getEnchantLevel() * 2;
		} else if (itemid == 20079 || itemid == 20078 || itemid == 900047 || itemid == 900055 || itemid == 20049
				|| itemid == 20050 || itemid == 900057 || itemid == 900056 || itemid == 900189) {
			mr += getEnchantLevel() * 3;
		}
		if (mr < 0)
			mr = 0;
		return mr;
	}

	public int addhp() {
		int hp = _item.get_addhp();

		return hp;
	}

	public int addmp() {
		int mp = _item.get_addmp();
		return mp;
	}

	public int addsp() {
		int sp = _item.get_addsp();
		return sp;
	}

	public void set_durability(int i) {
		if (i < 0) {
			i = 0;
		}

		if (i > 127) {
			i = 127;
		}
		_durability = i;
	}

	public int getWeight() {
		if (getItem().getWeight() == 0) {
			return 0;
		} else {
			return Math.max(getCount() * getItem().getWeight() / 1000, 1);
		}
	}

	public class LastStatus {
		public int count;
		public int itemId;
		public boolean isEquipped = false;
		public int enchantLevel;
		public boolean isIdentified = true;
		public int durability;
		public int chargeCount;
		public int remainingTime;
		public Timestamp lastUsed = null;
		public int bless;
		public int attrenchantLevel;
		public int specialEnchant;
		public int bless_level;
		public Timestamp endTime = null;
		/** 특수 인챈트 시스템 **/
		public int item_level;
		/** 여관 열쇠 리뉴얼 **/
		public String town_name;

		public void updateAll() {
			count = getCount();
			itemId = getItemId();
			isEquipped = isEquipped();
			isIdentified = isIdentified();
			enchantLevel = getEnchantLevel();
			durability = get_durability();
			chargeCount = getChargeCount();
			remainingTime = getRemainingTime();
			lastUsed = getLastUsed();
			bless = getBless();
			attrenchantLevel = getAttrEnchantLevel();
			specialEnchant = getSpecialEnchant();
			endTime = getEndTime();
			bless_level = get_bless_level();
			/** 특수 인챈트 시스템 **/
			item_level = get_item_level();
			/** 여관 열쇠 리뉴얼 **/
			town_name = getHotel_Town();
		}

		public void updateSpecialEnchant() {
			specialEnchant = getSpecialEnchant();
		}

		public void updateCount() {
			count = getCount();
		}

		public void updateItemId() {
			itemId = getItemId();
		}

		public void updateEquipped() {
			isEquipped = isEquipped();
		}

		public void updateIdentified() {
			isIdentified = isIdentified();
		}

		public void updateEnchantLevel() {
			enchantLevel = getEnchantLevel();
		}

		public void updateDuraility() {
			durability = get_durability();
		}

		public void updateChargeCount() {
			chargeCount = getChargeCount();
		}

		public void updateRemainingTime() {
			remainingTime = getRemainingTime();
		}

		public void updateLastUsed() {
			lastUsed = getLastUsed();
		}

		public void updateBless() {
			bless = getBless();
		}

		public void updateAttrEnchantLevel() {
			attrenchantLevel = getAttrEnchantLevel();
		}

		public void updateEndTime() {
			endTime = getEndTime();
		}

		public void update_bless_level() {
			bless_level = get_bless_level();
		}

		/** 특수 인챈트 시스템 **/
		public void update_item_level() {
			item_level = get_item_level();
		}
		/** 특수 인챈트 시스템 **/

		/** 여관 열쇠 리뉴얼 **/
		public void update_town_name() {
			town_name = getHotel_Town();
		}
		/** 여관 열쇠 리뉴얼 **/
	}

	public LastStatus getLastStatus() {
		return _lastStatus;
	}

	public int getRecordingColumns() {
		int column = 0;

		if (getCount() != _lastStatus.count) {
			column += L1PcInventory.COL_COUNT;
		}
		if (getItemId() != _lastStatus.itemId) {
			column += L1PcInventory.COL_ITEMID;
		}
		if (isEquipped() != _lastStatus.isEquipped) {
			column += L1PcInventory.COL_EQUIPPED;
		}
		if (getEnchantLevel() != _lastStatus.enchantLevel) {
			column += L1PcInventory.COL_ENCHANTLVL;
		}
		if (get_durability() != _lastStatus.durability) {
			column += L1PcInventory.COL_DURABILITY;
		}
		if (getChargeCount() != _lastStatus.chargeCount) {
			column += L1PcInventory.COL_CHARGE_COUNT;
		}
		if (getLastUsed() != _lastStatus.lastUsed) {
			column += L1PcInventory.COL_DELAY_EFFECT;
		}
		if (isIdentified() != _lastStatus.isIdentified) {
			column += L1PcInventory.COL_IS_ID;
		}
		if (getRemainingTime() != _lastStatus.remainingTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		if (getBless() != _lastStatus.bless) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getAttrEnchantLevel() != _lastStatus.attrenchantLevel) {
			column += L1PcInventory.COL_ATTRENCHANTLVL;
		}

		if (getSpecialEnchant() != _lastStatus.specialEnchant) {
			column += L1PcInventory.COL_ATTRENCHANTLVL;
		}
		if (get_bless_level() != _lastStatus.bless_level) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getEndTime() != _lastStatus.endTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		return column;
	}

	public String getNumberedViewName(int count) {
		if (PrideLoadManager.USE_PRIDE_SYSTEM && _itemId == PrideLoadManager.PRIDE_ITEM_ID) {
			PrideInfo pInfo = PrideLoadManager.getInstance().get_pride(getId());
			if (pInfo != null)
				return String.format("%s의 자존심", pInfo.get_target_name());
		}

		StringBuilder name = new StringBuilder();
		if (isSpecialEnchantable()) {
			name.append("\\f3");
		}
		name.append(getNumberedName(count));
		int itemType2 = getItem().getType2();
		int itemId = getItem().getItemId();

		if (itemId == 40314 || itemId == 40316) {
			L1Pet pet = PetTable.getInstance().getTemplate(getId());
			if (pet != null) {
				name.append("[Lv.");
				name.append(pet.get_level());
				name.append(" ");
				name.append(pet.get_name());
				name.append("]HP");
				name.append(pet.get_hp());
			}
		}

		if (getItem().getType2() == 0 && getItem().getType() == 2) { // light
			if (isNowLighting()) {
				name.append(" ($10)");
			}
			if (itemId == 40001 || itemId == 40002 || itemId == 7005) {
				if (getRemainingTime() <= 0) {
					name.append(" ($11)");
				}
			}
		}

		if (itemId == MJINNHelper.INN_KEYID) {
			name.append(" (" + getHotel_Town() + ")");
		}

		/** 특수 인챈트 시스템 **/
		if (get_item_level() != 0) {
			switch (get_item_level()) {
			case 1:
				name.append(" [1단계]");
				break;
			case 2:
				name.append(" [2단계]");
				break;
			case 3:
				name.append(" [3단계]");
				break;
			case 4:
				name.append(" [4단계]");
				break;
			case 5:
				name.append(" [5단계]");
				break;
			default:
				break;
			}
		}
		/** 특수 인챈트 시스템 **/

		if (isEquipped()) {
			if (itemType2 == 1) {
				name.append(" ($9)");
			} else if (itemType2 == 2) {
				name.append(" ($117)");
			} else if (itemType2 == 0 && getItem().getType() == 11) { // petitem
				name.append(" ($117)");
			}
		}

		if (itemType2 == 0 && getItem().getType() == 0) {
			if (_cha != null) {
				if (_cha.getInventory().getArrowItemId() == getItemId()) {
					name.append(" ($117)");
				}
			}
		}

		return name.toString();
	}

	public String getViewName() {
		return getNumberedViewName(_count);
	}

	public String getLogName() {
		return getNumberedName(_count);
	}

	/** 속성 인챈트 **/
	public String getNumberedName(int count) {
		StringBuilder name = new StringBuilder();

		if (isIdentified()) {
			if (getItem().getType2() == 1 || getItem().getType2() == 2) {
				switch (getAttrEnchantLevel()) {
				case 1:
					name.append("$6115");
					break; // 화령1단
				case 2:
					name.append("$6116");
					break; // 화령2단
				case 3:
					name.append("$6117");
					break; // 화령3단 (불의속성)
				case 4:
					name.append("$14361");
					break; // 화령4단
				case 5:
					name.append("$14365");
					break; // 화령5단
				case 6:
					name.append("$6118");
					break; // 수령1단
				case 7:
					name.append("$6119");
					break; // 수령2단
				case 8:
					name.append("$6120");
					break; // 수령3단 (물의속성)
				case 9:
					name.append("$14362");
					break; // 수령4단
				case 10:
					name.append("$14366");
					break; // 수령5단
				case 11:
					name.append("$6121");
					break; // 풍령1단
				case 12:
					name.append("$6122");
					break; // 풍령2단
				case 13:
					name.append("$6123");
					break; // 풍령3단 (바람의속성)
				case 14:
					name.append("$14363");
					break; // 풍령4단
				case 15:
					name.append("$14367");
					break; // 풍령5단
				case 16:
					name.append("$6124");
					break; // 지령1단
				case 17:
					name.append("$6125");
					break; // 지령2단
				case 18:
					name.append("$6126");
					break; // 지령3단 (땅의속성)
				case 19:
					name.append("$14364");
					break; // 지령4단
				case 20:
					name.append("$14368");
					break; // 지령5단
				default:
					break;
				}
				if (getEnchantLevel() >= 0) {
					name.append("+" + getEnchantLevel() + " ");
				} else if (getEnchantLevel() < 0) {
					name.append(String.valueOf(getEnchantLevel()) + " ");
				}
			}
		}
		// TODO 미확인상태 이름 확인후 이름 변경
		if (!isIdentified() && getItem().getItemId() == 900075) {
			name.append("$25459");
		} else if (!isIdentified() && getItem().getItemId() == 40008
				|| !isIdentified() && getItem().getItemId() == 140008) {
			name.append("$27");
		} else if (!isIdentified() && getItem().getItemId() == 40007) {
			name.append("$263");
		} else if (!isIdentified() && getItem().getItemId() == 40006) {
			name.append("$28");
		} else if (!isIdentified() && getItem().getItemId() == 40015
				|| !isIdentified() && getItem().getItemId() == 140015) {
			name.append("$232");
		} else if (!isIdentified() && getItem().getItemId() == 40012) {
			name.append("$238");
		} else if (!isIdentified() && getItem().getItemId() == 40011) {
			name.append("$235");
		} else if (!isIdentified() && getItem().getItemId() == 40010
				|| !isIdentified() && getItem().getItemId() == 140010) {
			name.append("$237");

			/** MJCTSystem **/
		} else if (getItem().getItemId() == MJCTLoadManager.CTSYSTEM_LOAD_ID) {
			MJCTObject obj = MJCTSystemLoader.getInstance().get(getId());
			if (obj == null)
				name.append(_item.getNameId());
			else
				name.append("[").append(obj.name).append("]봉인 구슬");
		} else {
			name.append(_item.getNameId());
		}

		if (isSpecialEnchantable()) {
			for (int i = 1; i <= 3; ++i) {
				if (getSpecialEnchant(i) == 0) {
					break;
				}
				switch (getSpecialEnchant(i)) {
				case CHAOS_SPIRIT:
					name.append("[혼돈] ");
					break;
				case CORRUPT_SPIRIT:
					name.append("[타락] ");
					break;
				case BALLACAS_SPIRIT:
					name.append("[발라카스] ");
					break;
				case ANTARAS_SPIRIT:
					name.append("[안타라스] ");
					break;
				case LINDBIOR_SPIRIT:
					name.append("[린드비오르] ");
					break;
				case PAPURION_SPIRIT:
					name.append("[파푸리온] ");
					break;
				case DEATHKNIGHT_SPIRIT:
					name.append("[데스나이트] ");
					break;
				case BAPPOMAT_SPIRIT:
					name.append("[바포메트] ");
					break;
				case BALLOG_SPIRIT:
					name.append("[발록] ");
					break;
				case ARES_SPIRIT:
					name.append("[아레스] ");
					break;
				}
			}
		}

		if (isIdentified()) {
			if (getItem().getMaxChargeCount() > 0) {
				name.append(" (" + getChargeCount() + ")");
			}
			if (getItem().getItemId() == 20383) {
				name.append(" (" + getChargeCount() + ")");
			}
			if (getItem().getMaxUseTime() > 0 && getItem().getType2() != 0) {
				name.append(" [" + getRemainingTime() + "]");
			}
		}
		if (count > 1) {
			name.append(" (" + count + ")");
		}
		return name.toString();
	}

	/**
	 * 아이템 상태로부터 서버 패킷으로 이용하는 형식의 바이트열을 생성해, 돌려준다. 1: 타격치 , 2: 인챈트 레벨, 3: 손상도,
	 * 4: 양손검, 5: 공격 성공, 6: 추가 타격 7: 왕자/공주 , 8: Str, 9: Dex, 10: Con, 11: Wiz,
	 * 12: Int, 13: Cha, 14: Hp,Mp 15: Mr, 16: 마나흡수, 17: 주술력, 18: 헤이스트효과, 19:
	 * Ac, 20: 행운, 21: 영양, 22: 밝기, 23: 재질, 24: 활 명중치, 25: 종류[writeH], 26:
	 * 레벨[writeH], 27: 불속성 28: 물속성, 29: 바람속성, 30: 땅속성, 31: 최대Hp, 32: 최대Mp, 33:
	 * 공포내성, 34: 생명흡수, 35: 활 타격치, 36: branch용dummy, 37: 체력회복률, 38: 마나회복률, 40:
	 * 마법명중, 42: 레벨, 47: 근거리대미지, 48: 근거리명중, 50: 마법치명타, 55: 헤스트효과, 56: 추가방어력, 59:
	 * PVP 추가 대미지 60: PVP 대미지 감소,61. 이후 
		, 63: 대미지 감소, 68: 소지 무게 증가율,71:
	 * 데스나이트 세트 표기, 72. 지원아이템 사용기간,75: 성향 라우풀, 79: 사용 레벨, 89: 확률 마법 회피 92: 추가
	 * 대미지 확률 94: 관통효과 96: 회복 악화 방어, 97: 대미지 리덕션 무시,`, os.writeH(23929) 데스셋표기?
	 * 
	 * @param armor
	 */

	public static int presentationCode = 0;

	@SuppressWarnings("deprecation")
	public byte[] getStatusBytes() {
		int itemType2 = getItem().getType2();
		int itemId = getItemId();
		int itemgrade = getItem().getGrade();
		

		try {
			byte[] presentation_bytes = ItemPresentatorFactory.do_presentation(this);
			if (presentation_bytes != null)
				return presentation_bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}

		ItemPresentOutStream os = new ItemPresentOutStream();
		try {
			
			
			
			
			
			if (itemId == 30001111) {
				if (GMCommands._gm == null)
					System.out.println(presentationCode);
				else
					GMCommands._gm.sendPackets(String.format("[%d]", presentationCode));
				if (presentationCode > 0xff)
					os.writeH(presentationCode++);
				else
					os.writeC(presentationCode++);
				os.writeC(0x01);
			}
			if (itemType2 == 0) { // etcitem
				
				
				
				
				switch (getItem().getType()) {
				case 2: // light
					os.writeLightRange(getItem().getLightRange());
					break;
				case 7: // food
					os.writeFoodVolume(getItem().getFoodVolume());
					break;
				case 0: // arrow
				case 15: // sting
					os.writeDMG(getItem().getDmgSmall(), getItem().getDmgLarge());
					break;
				default:
					os.writeC(23);
					break;
				}
				os.writeC(getItem().getMaterial());
				os.writeD(getWeight());
				
				
				

				// 자동사냥 사용권(1일)
				switch (getItem().getItemId()) {
				case 3000209:
					os.writeC(39);
					os.writeS("\\fI설명: \\aA소지 시 자동사냥");
					break;
					
				}
				
				/** 속성 변환 주문서 **/
				switch (getItem().getItemId()) {
				case 560030:
				case 560031:
				case 560032:
				case 560033:
					os.writeC(39);
					os.writeS("\\fI설명:\\aA속성 5단 무기만 가능");
					break;
				}
				
				/** 캐릭명 변경권 **/
				switch (getItem().getItemId()) {
				case 408991:
					os.writeC(39);
					os.writeS("\\fI설명:\\aA.캐릭명변경 입력");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100076: // 예술가의 마안
//					os.writeAddDMG(3);
//					os.writeLongDMG(3);
//					os.writeAddSP(2);
//					os.writeShortHIT(3);
//					os.writeLongHIT(3);
//					os.writeAddEXP(20);
//					os.writeAddAc(3);
					break;
				}
				switch (getItem().getItemId()) {
				case 4100039: // 현자의 전투 강화 주문서
					os.writeAddSP(3);
					os.writeMagicHIT(5);
					os.writePVPAddDMGdown(3);
					break;
				}
				switch (getItem().getItemId()) {
				case 4100041: // 명궁의 전투 강화 주문서
					os.writeLongDMG(3);
					os.writeLongHIT(5);
					os.writePVPAddDMGdown(3);
					break;
				}
				switch (getItem().getItemId()) {
				case 4100042: // 투사의 전투 강화 주문서
					os.writeShortDMG(3);
					os.writeShortHIT(5);
					os.writePVPAddDMGdown(3);
					break;
				}
				// TODO 각클래스 스킬 표기
				switch (getItem().getItemId()) {
				// 1단계 힐
				case 45000:
					os.writeClass(1 + 2 + 4 + 8 + 16 + 128);
					os.writeLawful(1);
					os.writeStep(0);
					break;
				}
				switch (getItem().getItemId()) {
				// 1단계
				case 45001:
				case 45002:
				case 45003:
				case 45004:
				case 45005:
				case 45006:
				case 45007:
					os.writeClass(1 + 2 + 4 + 8 + 16 + 128);
					os.writeLawful(0);
					os.writeStep(0);
					break;
				}
				switch (getItem().getItemId()) {
				// 2단계 큐어
				case 45008:
					os.writeClass(1 + 4 + 8 + 16);
					os.writeLawful(1);
					os.writeStep(2);
					break;
				}
				switch (getItem().getItemId()) {
				// 2단계 칠터치,커스
				case 45009:
				case 45010:
					os.writeClass(1 + 4 + 8 + 16);
					os.writeLawful(2);
					os.writeStep(2);
					break;
				}
				switch (getItem().getItemId()) {
				// 2단계
				case 45011:
				case 45012:
				case 45013:
				case 45014:
				case 45015:
					os.writeClass(1 + 4 + 8 + 16);
					os.writeLawful(0);
					os.writeStep(2);
					break;
				}
				switch (getItem().getItemId()) {
				// 3단계
				case 45019:
					os.writeClass(4 + 8);
					os.writeLawful(2);
					os.writeStep(3);
					break;
				}
				switch (getItem().getItemId()) {
				// 3단계
				case 45018:
				case 45021:
					os.writeClass(4 + 8);
					os.writeLawful(1);
					os.writeStep(3);
					break;
				}
				switch (getItem().getItemId()) {
				// 3단계
				case 45016:
				case 45017:
				case 45020:
				case 45022:
					os.writeClass(4 + 8);
					os.writeLawful(0);
					os.writeStep(3);
					break;
				}
				switch (getItem().getItemId()) {
				// 4단계
				case 40171:
					os.writeClass(4 + 8);
					os.writeLawful(1);
					os.writeStep(4);
					break;
				}
				switch (getItem().getItemId()) {
				// 4단계
				case 40173:
					os.writeClass(4 + 8);
					os.writeLawful(2);
					os.writeStep(4);
					break;
				}
				switch (getItem().getItemId()) {
				// 4단계
				case 40170:
				case 40172:
				case 40174:
				case 40175:
				case 40176:
				case 40177:
					os.writeClass(4 + 8);
					os.writeLawful(0);
					os.writeStep(4);
					break;
				}
				switch (getItem().getItemId()) {
				// 5단계
				case 40178:
				case 40185:
					os.writeClass(4 + 8);
					os.writeLawful(2);
					os.writeStep(5);
					break;
				}
				switch (getItem().getItemId()) {
				// 5단계
				case 40180:
				case 40182:
					os.writeClass(4 + 8);
					os.writeLawful(1);
					os.writeStep(5);
					break;
				}
				switch (getItem().getItemId()) {
				// 5단계
				case 40184:
				case 40183:
				case 40179:
				case 40181:
					os.writeClass(4 + 8);
					os.writeLawful(0);
					os.writeStep(5);
					break;
				}
				switch (getItem().getItemId()) {
				// 6단계
				case 40186:
				case 40192:
					os.writeClass(4 + 8);
					os.writeLawful(2);
					os.writeStep(6);
					break;
				}
				switch (getItem().getItemId()) {
				// 6단계
				case 40187:
				case 40188:
				case 40189:
				case 40190:
				case 40191:
				case 40193:
					os.writeClass(4 + 8);
					os.writeLawful(0);
					os.writeStep(6);
					break;
				}
				// 7단계
				switch (getItem().getItemId()) {
				case 40199:
				case 40200:
				case 40195:
				case 40198:
					os.writeClass(8);
					os.writeLawful(0);
					os.writeStep(7);
					break;
				}
				// 7단계
				switch (getItem().getItemId()) {
				case 40201:
				case 40196:
					os.writeClass(8);
					os.writeLawful(2);
					os.writeStep(7);
					break;
				}
				// 7단계
				switch (getItem().getItemId()) {
				case 40197:
				case 40194:
					os.writeClass(8);
					os.writeLawful(1);
					os.writeStep(7);
					break;
				}
				// 8단계
				switch (getItem().getItemId()) {
				case 40208:
				case 40209:
				case 40207:
				case 40205:
				case 40203:
					os.writeClass(8);
					os.writeLawful(0);
					os.writeStep(8);
					break;
				}
				// 8단계
				switch (getItem().getItemId()) {
				case 40206:
				case 40202:
					os.writeClass(8);
					os.writeLawful(1);
					os.writeStep(8);
					break;
				}
				// 8단계
				switch (getItem().getItemId()) {
				case 40204:
					os.writeClass(8);
					os.writeLawful(2);
					os.writeStep(8);
					break;
				}
				// 9단계
				switch (getItem().getItemId()) {
				case 40216:
				case 40210:
				case 40214:
				case 40212:
				case 40217:
				case 40215:
					os.writeClass(8);
					os.writeLawful(0);
					os.writeStep(9);
					break;
				}
				// 9단계
				switch (getItem().getItemId()) {
				case 40213:
					os.writeClass(8);
					os.writeLawful(1);
					os.writeStep(9);
					break;
				}
				// 9단계
				switch (getItem().getItemId()) {
				case 40211:
					os.writeClass(8);
					os.writeLawful(2);
					os.writeStep(9);
					break;
				}
				// 10단계
				switch (getItem().getItemId()) {
				case 40220:
				case 40222:
					os.writeClass(8);
					os.writeLawful(1);
					os.writeStep(10);
					break;
				}
				// 10단계
				switch (getItem().getItemId()) {
				case 40219:
				case 40221:
				case 40223:
				case 40224:
				case 40225:
					os.writeClass(8);
					os.writeLawful(0);
					os.writeStep(10);
					break;
				}
				// 10단계
				switch (getItem().getItemId()) {
				case 3000095:
					os.writeClass(8);
					os.writeLawful(2);
					os.writeStep(10);
					break;
				}
				switch (getItem().getItemId()) {
				case 40066:
					os.writeC(39);
					os.writeS("\\fIMP회복: \\aA5~10");
					break;
				}
				switch (getItem().getItemId()) {
				case 40067:
					os.writeC(39);
					os.writeS("\\fIMP회복: \\aA15~30");
					break;
				}
				switch (getItem().getItemId()) {
				case 65648: // 흑사의 코인
					os.writeAddAc(2);
					os.writeaspirit_resis(10);
					os.writeAddMaxHP(20);
					os.writeMaxMP(13);
					os.writeDMGdown(3);
					break;
				}
				switch (getItem().getItemId()) {
				case 1000007:
					os.writeC(39);
					os.writeS("\\fI축복 수치: \\aA+500%");
					break;
				}
				
				
				switch (getItem().getItemId()) {
				case 3000231:
					os.writeC(39);
					os.writeS("\\fI축복 수치: \\aA+1300%");
					break;
				}
				
				// 미사용
//				switch (getItem().getItemId()) {
//				case 3000225: // 잊혀진 상자
//					os.writeC(39);
//					os.writeS("\\fI내용: \\aA최상급 무기 상자.");
//					break;
//				}
				
				switch (getItem().getItemId()) {
				case 500220:
					os.writeC(39);
					os.writeS("\\fI효과: \\aAMP 1000 회복.");
					break;
				}
				switch (getItem().getItemId()) {
				// 사용안함
//				case 3000201: // PC 버프 1일
//					os.writeC(39);
//					os.writeS("\\fI기간: \\aA1일");
//					os.writeAddEXP(40);
//					break;
				case 3000237:// PC방 버프 3일
					os.writeC(39);
					os.writeS("\\fI기간: \\aA3일");
					os.writeAddEXP(40);
					break;
				case 600223: // PC방 버프 7일
					os.writeC(39);
					os.writeS("\\fI기간: \\aA7일");
					os.writeAddEXP(40);
					break;
				case 600225: // PC방 버프 30일
					os.writeC(39);
					os.writeS("\\fI기간: \\aA30일");
					os.writeAddEXP(40);
					break;
				}

				switch (getItem().getItemId()) {
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
					os.writeC(39);
					os.writeS("\\fI기간: \\aA7일");
					break;
				}
				// 사용안함
//				switch (getItem().getItemId()) {
//				case 3000210: // 전사의 마나 물약
//					os.writeC(39);
//					os.writeS("\\fI효과: \\aAMP 300 회복.");
//					break;
//				}
				switch (getItem().getItemId()) {
				case 510012: // 미소피아 성장
//					os.writeAddEXP(10);
					break;
				}
				switch (getItem().getItemId()) {
				case 510013: // 미소피아 방어
//					os.writeAddMR(10);
//					os.writeDMGdown(2);
//					os.writeAddMaxHP(100);
//					os.writeAddHPPrecovery(2);
					break;
				}
				switch (getItem().getItemId()) {
				case 510014: // 미소피아 공격
//					os.writeShortDMG(3);
//					os.writeLongDMG(3);
//					os.writeAddSP(3);
//					os.writeMaxMP(50);
//					os.writeAddMPPrecovery(2);
					break;
				}
				// TODO 마법인형 리스트
				switch (getItem().getItemId()) {
				case 210070:// 마법인형: 돌 골렘
					os.writeDMGdown(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 41250:// 마법인형: 늑대인간
					os.writeC(39);
					os.writeS("\\fI일정확률 추가대미지: \\aA+15");
					break;
				}
				switch (getItem().getItemId()) {
				case 41248:// 마법인형: 버그베어
					os.writeAddWeight(500);
					break;
				}
				switch (getItem().getItemId()) {
				case 210072:// 마법인형: 크러스트시안
					os.writeC(39);
					os.writeS("\\fI일정확률 추가대미지: \\aA+15");
					break;
				}
				switch (getItem().getItemId()) {
				case 210096:// 마법인형: 에티
					os.writeAddAc(3);
					break;
				}
				switch (getItem().getItemId()) {
				case 210096:// 마법인형: 에티
					os.writeAddAc(3);
					break;
				}
				switch (getItem().getItemId()) {
				case 740:// 마법인형: 목각
					os.writeAddMaxHP(50);
					break;
				}
				switch (getItem().getItemId()) {
				case 41249:// 마법인형: 서큐버스
					os.writeaMPUP(15);
					break;
				}
				switch (getItem().getItemId()) {
				case 210071:// 마법인형: 장로
					os.writeaMPUP(15);
					break;
				}
				switch (getItem().getItemId()) {
				case 210105:// 마법인형: 코카트리스
					os.writeLongDMG(1);
					os.writeLongHIT(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 410172:// 마법인형: 인어
					os.writeAddEXP(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 741:// 마법인형: 라바 골렘
					os.writeShortDMG(1);
					os.writeDMGdown(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 510219:// 마법인형: 자이언트
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
					}
					os.writeAddEXP(10);
					os.writeDMGdown(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 510221:// 마법인형: 흑장로
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
					}
					os.writeaMPUP(15);
					os.writeMagic("라이트닝");
					break;
				}
				switch (getItem().getItemId()) {
				case 510222:// 마법인형: 서큐버스 퀸
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
					}
					os.writeaMPUP(15);
					os.writeAddSP(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 447017:// 마법인형: 드레이크
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
					}
					os.writeLongDMG(2);
					os.writeaMPUP(6);
					break;
				}
				switch (getItem().getItemId()) {
				case 410173:// 마법인형 : 킹 버그베어
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
					}
					os.writeability_resis(8);
					os.writeaMPUP(10);
					break;
				}
				switch (getItem().getItemId()) {
				case 742:// 마법인형: 다이아몬드 골렘
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
					}
					os.writeDMGdown(2);
					break;
				}
				switch (getItem().getItemId()) {
				case 447016:// 리치인형
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
						os.writePVPAddDMG(2);
					}
					os.writeAddSP(2);
					os.writeAddMaxHP(80);
					break;
				}
				switch (getItem().getItemId()) {
				case 510220:// 사이클롭스
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
						os.writePVPAddDMG(2);
					}
					os.writeShortDMG(2);
					os.writeability_resis(12);
					os.writeShortHIT(2);
					break;
				}
				switch (getItem().getItemId()) {
				case 746:// 데스나이트인형
					if (getBless() % 128 == 0) {
						os.writeAddAc(3);
						os.writePVPAddDMG(2);
						os.writePVPAddDMGdown(4);
					}
					os.writeDMGdown(5);
					os.writeMagic("헬파이어");
					os.writeaBlesssomo(10);
					os.writeAddEXP(20);
					break;
				}
				switch (getItem().getItemId()) {
				case 950014:// 축데스 데스나이트  
					os.writeAddAc(5);
					os.writePVPAddDMG(2);
					os.writePVPAddDMGdown(4);
					os.writeDMGdown(6);
					os.writeMagic("헬파이어");
					os.writeaBlesssomo(10);
					os.writeAddEXP(30);
					break;
				}
				switch (getItem().getItemId()) {
				case 950019:// 축커츠
					os.writePVPAddDMG(2);
					os.writePVPAddDMGdown(4);
					os.writeaFouslayer(10);
					os.writeDMGdown(3);
					os.writeability_resis(10);
					os.writeadragonS_pierce(5);
					os.writeAddAc(5);
					break;					
				}
				switch (getItem().getItemId()) {
				case 950018:// 축얼음여왕
					os.writeAddAc(3);
					os.writePVPAddDMG(2);
					os.writePVPAddDMGdown(4);
					os.writeLongDMG(5);
					os.writeLongHIT(5);
					os.writeability_resis(10);
					os.writeaspirit_pierce(5);
					break;
			}
				switch (getItem().getItemId()) {
				case 141400:// 마법인형: 할파스
					os.writeaBlesssomo(10); // 축복 소묘 효율 10%
					os.writeShortDMG(10); // 근거리 대미지 +10
					os.writeShortHIT(10); // 근거리 명중 +10
					os.writeShortCritical(10); // 근거리 치명타 +10%
					os.writeAddAc(6); // 추가 방어력 +6
					os.writeDMGdown(8); // 대미지 감소 +8
					os.writeaMPUP(10); // MP 절대 회복 +10
					os.writePVPAddDMG(2); // PVP 추가 대미지 +2
					os.writePVPAddDMGdown(2); // PVP 대미지 감소 +2
					os.writeAddEXP(25); // EXP +25%
					os.writeaAll_pierce(10); // 적중 전체 +10
					os.writeaAll_resis(11); // 내성 전체 +11
					break;
				}
				switch (getItem().getItemId()) {
				case 141401:// 마법인형: 아우라키아
					os.writeaBlesssomo(10); // 축복 소묘 효율 10%
					os.writeAddSP(10); // SP +10
					os.writeMagicHIT(12); // 마법 명중 +12
					os.writeMagicCritical(5); // 마법 치명타 +5%
					os.writeAddAc(6); // 추가 방어력 +6
					os.writeDMGdown(8); // 대미지 감소 +8
					os.writeAddMaxHP(300); //최대 HP +300
					os.writeaMPUP(20); // MP 절대 회복 +20
					os.writePVPAddDMG(2); // PVP 추가 대미지 +2
					os.writePVPAddDMGdown(2); // PVP 대미지 감소 +2
					os.writeAddEXP(25); // EXP +25%
					os.writeaAll_pierce(10); // 적중 전체 +10
					os.writeaAll_resis(11); // 내성 전체 +11
					break;
				}
				switch (getItem().getItemId()) {
				case 141402:// 마법인형: 베히모스
					os.writeaBlesssomo(10); // 축복 소묘 효율 10%
					os.writeLongDMG(10); // 원거리 대미지 +10
					os.writeLongHIT(10); // 원거리 명중 +10
					os.writeLongCritical(10); // 원거리 치명타 +10%
					os.writeAddAc(6); // 추가 방어력 +6
					os.writeDMGdown(8); // 대미지 감소 +8
					os.writeAddMaxHP(200); //최대 HP +200
					os.writeMaxMP(100); // 최대 MP +100
					os.writeaMPUP(15); // MP 절대 회복 +15
					os.writePVPAddDMG(2); // PVP 추가 대미지 +2
					os.writePVPAddDMGdown(2); // PVP 대미지 감소 +2
					os.writeAddEXP(25); // EXP +25%
					os.writeaAll_pierce(10); // 적중 전체 +10
					os.writeaAll_resis(11); // 내성 전체 +11
					break;
				}
				switch (getItem().getItemId()) {
				case 141403:// 마법인형: 기르타스	
					os.writeaBlesssomo(35); // 축복 소묘 효율 35%
					os.writeShortDMG(20); // 근거리 대미지 +20
					os.writeShortHIT(20); // 근거리 명중 +20
					os.writeShortCritical(20); // 근거리 치명타 +20%
					os.writeAddSP(20); // SP +20
					os.writeMagicHIT(20); // 마법 명중 +20
					os.writeMagicCritical(20); // 마법 치명타 +20%
					os.writeLongDMG(20); // 원거리 대미지 +20
					os.writeLongHIT(20); // 원거리 명중 +20
					os.writeLongCritical(20); // 원거리 치명타 +20%
					os.writeAddAc(12); // 추가 방어력 +12
					os.writeDMGdown(16); // 대미지 감소 +16
					os.writeAddMaxHP(2500); //최대 HP +2500
					os.writeMaxMP(2500); // 최대 MP +2500
					os.writeaMPUP(30); // MP 절대 회복 +30
					os.writePVPAddDMG(10); // PVP 추가 대미지 +10
					os.writePVPAddDMGdown(10); // PVP 대미지 감소 +10
					os.writeAddEXP(50); // EXP +50%
					os.writeaAll_pierce(20); // 적중 전체 +20
					os.writeaAll_resis(20); // 내성 전체 +20
					os.writeAddWeight(5000); // 무게 보너스 +5000
					break;
				}
				switch (getItem().getItemId()) {
				case 141404:// 알비노데몬
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeShortDMG(3); // 근거리 대미지 +3
					os.writeability_pierce(12); // 적중 전체 +12
					os.writeability_resis(5); // 내성 전체 +5
					os.writeAddEXP(27); // EXP +27%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141405:// 알비노피닉스
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeLongDMG(3); // 원거리 대미지 +3
					os.writeability_pierce(12); // 적중 전체 +12
					os.writeability_resis(5); // 내성 전체 +5
					os.writeAddEXP(27); // EXP +27%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141406:// 알비노유니콘
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeAddSP(3); // SP +3
					os.writeability_pierce(7); // 적중 전체 +7
					os.writeability_resis(5); // 내성 전체 +5
					os.writeAddEXP(27); // EXP +27%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141407:// 유니콘
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeShortDMG(5); // 근거리 대미지 +5
					os.writeAddSP(3); // SP +3
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141416:// 피닉스
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeLongDMG(5); // 원거리 대미지 +5
					os.writeLongCritical(2); // 원거리 치명타 +2%
					os.writeAddEXP(25); // EXP +25%
					os.writeability_resis(10); // 내성 전체 +10
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141417:// 암흑 대장로
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeAddSP(3); // SP +3
					os.writeAddMPPrecovery(16); // MP 회복 +16
					os.writeAddEXP(25); // EXP +25%
					os.writeability_resis(10); // 내성 전체 +10
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141410:// 디바인크루세이더
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeShortDMG(4); // 근거리 대미지 +4
					os.writeDMGdown(2); // 대미지 감소 +2
					os.writePVPAddDMG(2); // PVP 대미지 +2
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141411:// 군터
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeShortDMG(5); // 근거리 대미지 +5
					os.writeShortCritical(2); // 근거리 치명타 +2
					os.writeability_pierce(8); // 적중 전체 +8%
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141412:// 오우거킹
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeShortDMG(4); // 근거리 대미지 +4
					os.writeAddMaxHP(150); // 최대 HP +150
					os.writeability_resis(10); // 내성 전체 +10
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141409:// 다크스타조우
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeAddSP(3); // SP +3
					os.writeMagicHIT(1); // 마법 명중 +1
					os.writeAddMPPrecovery(16); // MP 회복 +16
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141408:// 그림리퍼
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writeability_pierce(8); // 적중 전체 +8%
					os.writeability_resis(8); // 내성 전체 +8%
					os.writeafear_resis(5); // 공포 내성 +5%
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141413:// 다크하딘
					os.writeaBlesssomo(21); // 축복 소모 효율 +21%
					os.writePVPAddDMGdown(5); // PVP 대미지 감소 +5
					os.writeability_resis(8); // 내성 전체 +8%
					os.writeafear_resis(5); // 공포 내성 +5%
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
				switch (getItem().getItemId()) {
				case 141415:// 드래곤슬레이어
					os.writeaBlesssomo(23); // 축복 소모 효율 +23%
					os.writeDMGdown(2); // 대미지 감소 +2
					os.writeShortDMG(3); // 근거리 대미지 +3
					os.writeLongDMG(3); // 원거리 대미지 +3
					os.writeability_resis(8); // 내성 전체 +8%
					os.writeafear_resis(8); // 공포 내성 +8%
					os.writeAddEXP(25); // EXP +25%
					os.writeAddWeight(2400); // 무게 보너스 +2400
					break;
				}
			switch (getItem().getItemId()) {
			case 950017:// 축바포메트
				os.writeAddAc(3);
				os.writePVPAddDMG(2);
				os.writePVPAddDMGdown(4);
				os.writeafear_pierce(5);
				os.writeability_resis(10);
				break;
			}
			switch (getItem().getItemId()) {
			case 950016: // 축타락
				os.writeAddAc(3);
				os.writePVPAddDMG(2);
				os.writePVPAddDMGdown(4);
				os.writeAddSP(3);
				os.writeMagicHIT(5);
				os.writeability_resis(10);
				os.writeadragonS_pierce(5);
				break;
			}
			switch (getItem().getItemId()) {
			case 950015: // 축바란카
				os.writeAddAc(3);
				os.writePVPAddDMG(2);
				os.writePVPAddDMGdown(4);
				os.writeability_resis(12);
				os.writeaspirit_pierce(10);
				break;
			}
			switch (getItem().getItemId()) {
			case 950012: // 축머미로드
				os.writeAddAc(2);
				os.writePVPAddDMG(2);
				os.writeDMGdown(2);
				os.writeaBlesssomo(2);
				os.writeAddEXP(10);
				os.writeAddMPPrecovery(15);
				break;
			}
			switch (getItem().getItemId()) {
			case 950011: // 축뱀파이어
				os.writeAddAc(2);
				os.writePVPAddDMG(2);
				os.writeShortDMG(2);
				os.writeShortHIT(2);
				os.writeafear_pierce(3);
				os.writeability_resis(5);
				break;
			}
			switch (getItem().getItemId()) {
			case 950010: // 축아이리스
				os.writeAddAc(2);
				os.writePVPAddDMG(2);
				os.writeaFouslayer(10);
				os.writeDMGdown(3);
				break;
			}
			switch (getItem().getItemId()) {
			case 950008: // 축나이트발드
				os.writeAddAc(2);
				os.writePVPAddDMG(2);
				os.writeShortDMG(2);
				os.writeShortHIT(2);
				os.writeability_pierce(5);
				break;
			}
			switch (getItem().getItemId()) {
			case 950006: // 축사이클롭스
				os.writeAddAc(2);
				os.writePVPAddDMG(2);
				os.writeShortDMG(2);
				os.writeability_resis(12);
				os.writeShortHIT(2);
				break;
			}
			switch (getItem().getItemId()) {
			case 950005: // 축다이아몬드 골렘
				os.writeDMGdown(2);
				os.writeAddAc(2);
				break;
			}
			switch (getItem().getItemId()) {
			case 950004: // 축킹버그베어
				os.writeAddAc(2);
				os.writeability_resis(8);
				os.writeaMPUP(10);
				break;
			}
			switch (getItem().getItemId()) {
			case 950003: // 축드레이크
				os.writeaMPUP(6);
				os.writeAddAc(2);
				os.writeLongDMG(2);
				break;
			}
			switch (getItem().getItemId()) {
			case 950002: // 축자이언트
				os.writeAddEXP(10);
				os.writeDMGdown(1);
				os.writeAddAc(2);
				break;
			}
			switch (getItem().getItemId()) {
			case 950001: // 축흑장로
				os.writeaMPUP(15);
				os.writeAddAc(2);
				os.writeMagic2("콜 라이트닝");
				break;
			}
			switch (getItem().getItemId()) {
			case 950000: // 축서큐버스 퀸
				os.writeaMPUP(15);
				os.writeAddAc(2);
				os.writeAddSP(2);
				break;
			}
			switch (getItem().getItemId()) {
			case 950007: // 축리치
				os.writeAddAc(2);
				os.writePVPAddDMG(2);
				os.writeAddSP(2);
				os.writeAddMaxHP(80);
				break;
			}
			switch (getItem().getItemId()) {
			case 950009: // 축시어
				os.writeAddAc(2);
				os.writePVPAddDMG(2);
				os.writeLongDMG(5);
				os.writeaHPUP(30);
				break;
			}
			switch (getItem().getItemId()) {
			case 950013: // 축데몬
				os.writeAddAc(3);
				os.writePVPAddDMG(2);
				os.writePVPAddDMGdown(4);
				os.writeability_resis(12);
				os.writeability_pierce(10);
				break;
			}
				switch (getItem().getItemId()) {
				case 743:// 마법인형 : 나이트발드
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
						os.writePVPAddDMG(2);
					}
					os.writeShortDMG(2);
					os.writeShortHIT(2);
					os.writeability_pierce(5);
					break;
				}
				switch (getItem().getItemId()) {
				case 500214:// 스파토이
					os.writeaBlesssomo(5);
					os.writeShortDMG(2);
					break;
				}
				switch (getItem().getItemId()) {
				case 3000086:// 아이리스
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
						os.writePVPAddDMG(2);
					}
					os.writeaFouslayer(10);
					os.writeDMGdown(3);
					break;
				}
				switch (getItem().getItemId()) {
				case 3000087:// 뱀파이어
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
						os.writePVPAddDMG(2);
					}
					os.writeShortDMG(2);
					os.writeShortHIT(2);
					os.writeafear_pierce(3);
					os.writeability_resis(5);
					break;
				}
				switch (getItem().getItemId()) {
				case 3000088:// 바란카
					if (getBless() % 128 == 0) {
						os.writeAddAc(3);
						os.writePVPAddDMG(2);
						os.writePVPAddDMGdown(4);
					}
					os.writeability_resis(12);
					os.writeaspirit_pierce(10);
					break;
				}
				switch (getItem().getItemId()) {
				case 745:// 데몬
					if (getBless() % 128 == 0) {
						os.writeAddAc(3);
						os.writePVPAddDMG(2);
						os.writePVPAddDMGdown(4);
					}
					os.writeability_resis(12);
					os.writeability_pierce(10);
					break;
				}
				switch (getItem().getItemId()) {
				case 744:// 시어
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
						os.writePVPAddDMG(2);
					}
					os.writeLongDMG(5);
					os.writeaHPUP(30);
					break;
				}
				switch (getItem().getItemId()) {
				case 3000351:// 머미로드
					if (getBless() % 128 == 0) {
						os.writeAddAc(2);
						os.writePVPAddDMG(2);
					}
					os.writeDMGdown(2);
					os.writeaBlesssomo(2);
					os.writeAddEXP(10);
					os.writeAddMPPrecovery(15);
					break;
				}
				switch (getItem().getItemId()) {
				case 3000352:// 타락
					if (getBless() % 128 == 0) {
						os.writeAddAc(3);
						os.writePVPAddDMG(2);
						os.writePVPAddDMGdown(4);
					}
					os.writeAddSP(3);
					os.writeMagicHIT(5);
					os.writeability_resis(10);
					os.writeadragonS_pierce(5);
					break;
				}
				switch (getItem().getItemId()) {
				case 755:// 마법인형: 바포메트
					if (getBless() % 128 == 0) {
						os.writeAddAc(3);
						os.writePVPAddDMG(2);
						os.writePVPAddDMGdown(4);
					}
					os.writeafear_pierce(5);
					os.writeability_resis(10);
					break;
				}
				switch (getItem().getItemId()) {
				case 756:// 마법인형: 얼음여왕

					os.writeLongDMG(5);
					os.writeLongHIT(5);
					os.writeability_resis(10);
					os.writeaspirit_pierce(7);
					if (getBless() % 128 == 0) {
						os.writeAddAc(3);
						os.writePVPAddDMG(2);
						os.writePVPAddDMGdown(4);
					}
					break;
				}
				switch (getItem().getItemId()) {
				case 757:// 마법인형: 커츠
					if (getBless() % 128 == 0) {
						os.writePVPAddDMG(2);
						os.writePVPAddDMGdown(4);
					}
					os.writeaFouslayer(10);
					os.writeDMGdown(3);
					os.writeability_resis(10);
					os.writeadragonS_pierce(5);
					os.writeAddAc(2 + getBless() % 128 == 0 ? 0 : 3);
					break;
				}
//				switch (getItem().getItemId()) {
//				case 758:// 마법인형: 지배자의 현신(1등급)
//					os.writeDMGdown(5);
//					os.writeAddEXP(30);
//				  //os.writeaBlesssomo(30);
//					os.writeMagic("파이어 밤");
//					break;
//				}
//				switch (getItem().getItemId()) {
//				case 772: // 마법인형: 지배자의 현신(1등급)
//					os.writeDMGdown(5);
//					os.writeAddEXP(30);
//					os.writeMagic("파이어 밤");
//					break;
//				}
//				switch (getItem().getItemId()) {
//				case 759:// 마법인형: 지배자의 현신(2등급)
//				case 773:
//					os.writeDMGdown(3);
//					os.writeAddEXP(10);
//					os.writeaBlesssomo(4);
//					os.writeMagic("파이어 밤");
//					break;
//				}
//				switch (getItem().getItemId()) {
//				case 760:// 마법인형: 지배자의 현신(3등급)
//				case 774:
//					os.writeDMGdown(2);
//					os.writeAddEXP(5);
//					os.writeaBlesssomo(3);
//					os.writeMagic("파이어 밤");
//					break;
//				}
//				switch (getItem().getItemId()) {
//				case 761:// 마법인형: 지배자의 현신(4등급)
//				case 775: // 마법인형: 지배자의 현신(4등급)
//					os.writeAddEXP(2);
//					os.writeaBlesssomo(2);
//					os.writeMagic("파이어 밤");
//					break;
//				}
				switch (getItem().getItemId()) {
				case 4100007:// 마법인형: 안타라스
					os.writeDMGdown(6);
					os.writeAddEXP(25);
					os.writeaMPUP(15);
					os.writeaBlesssomo(10);
					os.writeAddAc(3);
					os.writePVPAddDMG(4);
					os.writePVPAddDMGdown(2);
					break;
				}
				switch (getItem().getItemId()) {
				case 4100008:// 마법인형: 파푸리온
					os.writeAddSP(4);
					os.writeMagicHIT(8);
					os.writeaAll_pierce(3);
					os.writeaAll_resis(8);
					os.writeaMPUP(5);
					os.writeAddAc(3);
					os.writePVPAddDMG(4);
					os.writePVPAddDMGdown(2);
					break;
				}
				switch (getItem().getItemId()) {
				case 4100009:// 마법인형: 린드비오르
					os.writeLongDMG(4);
					os.writeLongHIT(8);
					os.writeaAll_pierce(3);
					os.writeaAll_resis(8);
					os.writeaMPUP(5);
					os.writeAddAc(3);
					os.writePVPAddDMG(4);
					os.writePVPAddDMGdown(2); 
					break;
				}
				switch (getItem().getItemId()) {
				case 4100010:// 마법인형: 발라카스
					os.writeShortDMG(4);
					os.writeShortHIT(8);
					os.writeaAll_resis(8);
					os.writeaAll_pierce(3);
					os.writeaMPUP(5);
					os.writeAddAc(3);
					os.writePVPAddDMG(4);
					os.writePVPAddDMGdown(2);
					break;
				}
				switch (getItem().getItemId()) {
				case 4100134:// 마법인형: 진 데스나이트
					os.writeC(39);
					os.writeS("\\fI시간: \\aA7일 이용가능");
					os.writeAddEXP(7);
					os.writeaBlesssomo(5);
					break;
				}
				
				// TODO ETC
				switch (getItem().getItemId()) {
				case 4100121:// 고급 불멸의 가호
					os.writeC(39);
					os.writeS("\\fI아데나: \\aA획득량 30% 증가");
					os.writeC(39);
					os.writeS("\\fI사망 패널티: \\aA1회 방지");
					os.writeC(39);
					os.writeS("\\fI보호 효과: \\aA떨굼 방지");
					break;
				}
				switch (getItem().getItemId()) {
				case 4100146:// 진 데스나이트의 변신 반지 상자
				case 4100133:// 진 데스나이트의 변신 반지
//					os.writeC(39);
//					os.writeS("\\fI효과: \\aA레벨84 속도");
					os.writeC(39);
					os.writeS("\\fI시간: \\aA7일 이용가능");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 600228:// 성장의 릴
					os.writeC(39);
					os.writeS("\\fI사용법: \\aA낚싯대에 장착");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100292:// 고급 성장의 릴 100회
					os.writeC(39);
					os.writeS("\\fI효율: \\aA33% 시간 감소");
					os.writeC(39);
					os.writeS("\\fI특징: \\aA런커 획득 가능");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100291:// 진 변신 주문서 상자
				case 4100290:// 진 변신 주문서
//					os.writeC(39);
//					os.writeS("\\fI효과: \\aA레벨87 속도");
					os.writeC(39);
					os.writeS("\\fI시간: \\aA6시간 이용가능");
					break;
				}
				switch (getItem().getItemId()) {
				case 3000428:// 스냅퍼 보호 주문서
				case 3000430:// 룸티스 보호 주문서
					os.writeC(39);
					os.writeS("\\fI인챈트: \\aA+1부터 사용가능.");
					break;
				}
				switch (getItem().getItemId()) {
				case 3000546:// 휘장 보호 주문서
				case 3000547:// 문장 보호 주문서
					os.writeC(39);
					os.writeS("\\fI인챈트: \\aA+1부터 사용가능.");
					break;
				}
				switch (getItem().getItemId()) {
				case 4100034:// 빛나는 아덴 용사 보호 주문서
					os.writeC(39);
					os.writeS("\\fI사용: \\aA(빛나는 아덴 용사) 방어구류");
					os.writeC(39);
					os.writeS("\\fI인챈트: \\aA+0부터 사용가능.");
					break;
				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000544:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 견갑 변경.");
//					break;
//				}
				
				switch (getItem().getItemId()) {
				case 4100044: // 신비한 마법인형 상자(4단계)
					os.writeC(39);
					os.writeS("\\fI내용: \\aA4단계 마법 인형.");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100045:
					os.writeC(39);
					os.writeS("\\fI내용: \\aA5단계 마법 인형.");
					os.writeC(39);
					os.writeS("        \\aA용/데스나이트 제외.");
					break;
				}
				switch (getItem().getItemId()) {
				case 4100049:
					os.writeC(39);
					os.writeS("\\fI내용: \\aA지배자의 인형(랜덤).");
					break;
				}
				switch (getItem().getItemId()) {
				case 4100046:
					os.writeC(39);
					os.writeS("\\fI내용: \\aA마법인형(랜덤).");
					os.writeC(39);
					os.writeS("        \\aA용/데스나이트.");
					break;
				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 4100035:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 일반 무기.");
//					break;
//				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000529:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 전설 무기.");
//					break;
//				}
				
				switch (getItem().getItemId()) {
				case 3000582:
					os.writeC(39);
					os.writeS("\\fI방법: \\aA[운영자 편지 & 귓말해서 교환]");
					os.writeC(39);
					os.writeS("\\fI재료: \\aA[속성5단계 전설급 무기+ 해당 주문서]");
					break;
				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000534:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 용의 티셔츠.");
//					break;
//				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000539:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 유니콘의 각반.");
//					break;
//				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000541:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 반지.");
//					break;
//				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000542:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 목걸이.");
//					break;
//				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000543:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 벨트.");
//					break;
//				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000530:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA동급 4대 마법.");
//					break;
//				}
				
				// 없는 아이템
//				switch (getItem().getItemId()) {
//				case 3000581:
//					os.writeC(39);
//					os.writeS("\\fI변경: \\aA암석/마물.");
//					break;
//				}
				
				switch (getItem().getItemId()) {
				case 5549: // 금빛 마법 인형 주머니
					os.writeC(39);
					os.writeS("\\fI확률: \\aA3단계 이상 획득");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100081: // 악세서리 패키지 (근거리)
					os.writeC(39);
					os.writeS("\\aA[5쌍용반/5검귀/5붉귀]");
					os.writeC(39);
					os.writeS("\\aA[5투사문장/5투사휘장]");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100082: // 악세서리 패키지 (원거리)
					os.writeC(39);
					os.writeS("\\aA[5쌍용반/5검귀/5붉귀]");
					os.writeC(39);
					os.writeS("\\aA[5명궁문장/5명궁휘장]");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100083: // 악세서리 패키지 (마법강화)
					os.writeC(39);
					os.writeS("\\aA[5쌍지혜/5검귀/5보귀]");
					os.writeC(39);
					os.writeS("\\aA[5현자문장/5현자휘장]");
					break;
				}

				switch (getItem().getItemId()) {
				case 3000582:
					os.writeC(39);
					os.writeS("다른부위 마물 교환.");
					break;
				}
				switch (getItem().getItemId()) {
				case 410032:
				case 410033:
				case 410034:
				case 410035:
				case 410036:
				case 410037:
				case 410038:
					os.writeC(39);
					os.writeS("\\fI재사용 대기: \\aA1800초.");
					break;
				}
				switch (getItem().getItemId()) {
				case 51093:
				case 51094:
				case 51095:
				case 51096:
				case 51097:
				case 51098:
				case 51099:
				case 51100:
					os.writeC(39);
					os.writeS("\\fI효과: \\aA클래스 변경.");
					os.writeC(39);
					os.writeS("        \\aA기존 스킬 삭제.");
					break;
				}
				switch (getItem().getItemId()) {
				case 60208:// 완력의빙수
					os.writeShortDMG(3);
					os.writeShortHIT(5);
					os.writeaSTR_Bu(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 60209:// 민첩의빙수
					os.writeLongDMG(3);
					os.writeLongHIT(5);
					os.writeaDEX_Bu(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 60210:// 지식의빙수
					os.writeaINT_Bu(1);
					os.writeAddSP(2);
					os.writeMaxMP(50);
					os.writeAddMPPrecovery(5);
					break;
				}
				switch (getItem().getItemId()) {
				case 3000456: // 빛나는 성장의 물약
				case 30105:// 성장의물약
					os.writeAddEXP(40);
					break;
				}
				switch (getItem().getItemId()) {
				case 701: // 드래곤의 성장 물약 상자
//					os.writeC(39);
//					os.writeS("\\fI수량: \\aA7개.");
					break;
				}
				switch (getItem().getItemId()) {
				case 4100037:
//					os.writeC(39);
//					os.writeS("\\fI내용: \\aA영웅의 가넷");
					break;
				}
				switch (getItem().getItemId()) {
				case 1000002:
					os.writeC(39);
					os.writeS("\\fI축복 수치: \\aA+30%");
					break;
				}
				switch (getItem().getItemId()) {
				case 1000003:
					os.writeC(39);
					os.writeS("\\fI축복 수치: \\aA+50%");
					break;
				}
				switch (getItem().getItemId()) {
				case 410064:
				case 1000004:
					os.writeC(39);
					os.writeS("\\fI축복 수치: \\aA+100%");
					break;
				}
				switch (getItem().getItemId()) {
				case 4100160:
				case 4100161:
				case 4100162:
				case 4100163:
//					os.writeC(39);
//					os.writeS("\\fI불가능: \\f3PK시 어택/마법");
					break;
				}
				switch (getItem().getItemId()) {
				case 410012:
					os.writeShortHIT(3);
					os.writeShortDMG(3);
					os.writeLongHIT(3);
					os.writeLongDMG(3);
					os.writeAddSP(3);
					os.writeC(39);
					os.writeS("\\fI사용 시간: \\aA30분.");
					break;
				}
				switch (getItem().getItemId()) {
				case 410010:
					os.writeAddMaxHP(50);
					os.writeAddHPPrecovery(4);
					os.writeC(39);
					os.writeS("\\fI사용 시간: \\aA30분.");
					break;
				}
				switch (getItem().getItemId()) {// 다이노스
				case 500212:
//					os.writeC(39);
//					os.writeS("\\fIMP 회복 + 15");
//					os.writeC(39);
//					os.writeS("\\fI근거리 대미지 +1");
//					os.writeC(39);
//					os.writeS("\\fI원거리 대미지 +1");
//					os.writeC(39);
//					os.writeS("\\fI추가 대미지 +1");
//					os.writeC(39);
//					os.writeS("\\fISP +1");
//					os.writeAddEXP(5);
					break;
				}
				switch (getItem().getItemId()) {
				case 410011:
					os.writeAddMaxHP(40);
					os.writeAddMPPrecovery(4);
					os.writeC(39);
					os.writeS("\\fI사용 시간: \\aA30분.");
					break;
				}
				switch (getItem().getItemId()) {
				case 201548:
					os.writeC(39);
					os.writeS("\\fI내용: \\aA아르카의 유물.");
					os.writeC(39);
					os.writeS("        \\aA행운 아이템.");
					break;
				}

				// 미사용
//				switch (getItem().getItemId()) {
//				case 30072:
//					os.writeC(39);
//					os.writeS("\\fI내용: \\aA단테스의 유물.");
//					break;
//				}
				
				// 미사용
//				switch (getItem().getItemId()) {
//				case 700085: // 확성기(일반)
//					os.writeC(39);
//					os.writeS("\\fI효과: \\aA메세지 출력(20초).");
//					os.writeUseLevel(82);
//					break;
//				}
				
				// 미사용
//				switch (getItem().getItemId()) {
//				case 700086: // 확성기(고성능)
//					os.writeC(39);
//					os.writeC(39);
//					os.writeS("\\fI효과: \\aA메세지 출력(40초).");
//					os.writeUseLevel(82);
//					break;
//				}
				
				switch (getItem().getItemId()) {
				case 3000455:
				case 30104:// 코마의축복
					os.writeaSTR_Bu(5);
					os.writeaDEX_Bu(5);
					os.writeaCON_Bu(1);
					os.writeShortHIT(5);
					os.writeAddSP(1);
					os.writeAddAc(8);
					os.writeAddEXP(20);
					os.writeC(39);
					os.writeS("\\fI사용 시간: \\aA2시간.");
					break;
				}
				switch (getItem().getItemId()) {
				case 3000129:// 정성스런 스프
					os.writeC(39);
					os.writeS("\\fI효과: \\aA공격력,방어력");
					os.writeC(39);
					os.writeS("        \\aA회피력 상승");
					os.writeC(39);
					os.writeS("\\fI사용 시간: \\aA30분.");
					break;
				}
				switch (getItem().getItemId()) {
				case 3000130:// 정성스런 요리
					os.writeDMGdown(5);
					os.writeAddEXP(5);
					os.writeC(39);
					os.writeS("\\fI사용 시간: \\aA30분.");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 3000128:// 메티스의축복
					os.writeC(39);
					os.writeS("\\fI효과: \\aA공격력,방어력");
					os.writeC(39);
					os.writeS("        \\aA회피력 상승");
					os.writeC(39);
					os.writeS("        \\aA최대 MP/HP 상승");
					os.writeC(39);
					os.writeS("\\fI사용 시간: \\aA30분.");
					break;
				}
				
				
				switch (getItem().getItemId()) {
				case 3000049:// 구호 증서
					os.writeC(39);
					os.writeS("\\fI효과: \\aA경험치 100% 복구");
					break;
				}
				switch (getItem().getItemId()) {
				case 3000155:// 속죄 성서
					os.writeC(39);
					os.writeS("\\fI효과: \\aA라우풀 복구");
					break;
				}
				switch (getItem().getItemId()) {
				case 490028:// 라우풀물약
					os.writeC(39);
					os.writeS("\\fI효과: \\aA라우풀 +10000");
					break;
				}
				switch (getItem().getItemId()) {
				case 490029:// 카오틱물약
					os.writeC(39);
					os.writeS("\\fI효과: \\aA카오틱 -10000");
					break;
				}
				
				// 미사용
//				switch (getItem().getItemId()) {
//				case 4100209:	// 서버 이전권
//					os.writeC(39);
//					os.writeS("\\fI내용: \\aA특정 기간에 사용");
//					break;
//				}
				
				switch (getItem().getItemId()) {
				case 698: // 4대 숨결 랜덤 변경권
				case 703: // 축스냅퍼 랜덤 변경권
				case 704: // 축룸티스 랜덤 변경권
				case 705: // 수호 문장 랜덤 변경권
				case 706: // 수호 휘장 랜덤 변경권
				case 707: // 발라 갑옷 랜덤 변경권
				case 719: // 4대 마법 랜덤 변경권
				case 776: // 4단계 인형 랜덤 변경권
				case 777: // 5단계 인형 랜덤 변경권
				case 778: // 용인형 랜덤 변경권
				case 779: // 축데스급 인형 랜덤 변경권
				case 780: // 할파스급 인형 랜덤 변경권
				case 500715: // 할파스 갑옷 랜덤 변경권
				case 500717: // 집행급 무기 랜덤 변경권
					os.writeC(39);
					os.writeS("\\fI효과: \\aA동급 랜덤 변경");
					break;
				}

				switch (getItem().getItemId()) {
				case 3020025: // 용인형 랜덤 뽑기
					os.writeC(39);
					os.writeS("\\fI효과: \\aA용 인형 뽑기");
					os.writeC(39);
					os.writeS("\\fI설명: \\aA실패시 코인 회수");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 3000148:
					os.writeC(39);
					os.writeS("\\fI내용: \\aA나발급 무기.");
					os.writeC(39);
					os.writeS("\\fI획득: \\aA동일 인챈 랜덤.");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100148: // 사신의 무기 마법 주문서
					os.writeC(39);
					os.writeS("\\fI대상: \\aA신화급 무기.");
					os.writeC(39);
					os.writeS("\\fI내용: \\aA+1 인첸(100%).");
					break;
				}

				switch (getItem().getItemId()) {
				case 5007177: // 사신검 무기 변환석
					os.writeC(39);
					os.writeS("\\fI내용: \\aA집행급 무기");
					os.writeC(39);
					os.writeS("\\fI획득: \\aA사신의 검 동일 인챈");
					break;
				}
				switch (getItem().getItemId()) {
				case 3000169: // 랜덤 인챈 무기 상자
					os.writeC(39);
					os.writeS("\\fI내용: \\aA접설급 인챈 무기.");
					os.writeC(39);
					os.writeS("\\fI획득: \\aA+0~3 인챈 랜덤.");
					break;
				}
				switch (getItem().getItemId()) {
				case 718:	// 랜덤 인챈 무기상자 (0~3)
					os.writeC(39);
					os.writeS("\\fI내용: \\aA영웅급 인챈 무기.");
					os.writeC(39);
					os.writeS("\\fI획득: \\aA+0~3 인챈 랜덤.");
					break;
				}
				switch (getItem().getItemId()) {
				case 100002:	// 캐릭터 교환증(lv70 이상)
				case 100003:	// 캐릭터 교환증(lv70 미만)
					os.writeC(39);
					os.writeS("\\fI내용: \\aA캐릭터 교환.");
					os.writeC(39);
					os.writeS("\\fI사용: \\aA교환 창 거래.");
					break;
				}
				switch (getItem().getItemId()) {
				case 410136:	// 시간 충전석: 낚시터
				case 1000011:	// 시간 충전석: 기감
				case 410062:	// 시간 충전석: 오만
				case 410135:	// 시간 충전석: 본던, 용던
				case 1000013:	// 시간 충전석: 4대 계곡
				case 500216:	// 던전 초기화 : 용던
				case 1000012:	// 던전 초기화 : 몽환의 섬
				case 3000353:	// 던전 초기화 : 잊혀진 섬
				case 4100185:	// 던전 초기화 : 육성
					os.writeC(39);
					os.writeS("\\fI효과: \\aA던전 시간 충전");
					break;
				}
				switch (getItem().getItemId()) {
				case 3000159:// 생마 일회용
					os.writeC(39);
					os.writeS("\\fI효과: \\aA1회용 생명의 마안.");
					break;
				}

				switch (getItem().getItemId()) {
				case 7010: // 축복의 돌 (20%)
					os.writeC(39);
					os.writeS("\\fI확률: \\aA0% ~ 20%");
					os.writeC(39);
					os.writeS("\\fI추타:\\aA +1 ~ +5");
					os.writeC(39);
					os.writeS("\\fISP:\\aA +1 ~ +5");
					break;
				}

				switch (getItem().getItemId()) {
				case 7011: // 빛나는 축복의 돌 (20%)
					os.writeC(39);
					os.writeS("\\fI확률: \\aA확정 확률");
					os.writeC(39);
					os.writeS("\\fI추타:\\aA +1 ~ +5");
					os.writeC(39);
					os.writeS("\\fISP:\\aA +1 ~ +5");
					break;
				}
				
				// 이걸 쓰나...?
//				switch (getItem().getItemId()) {
//				case 3000171:// 마안 상자
//					os.writeC(39);
//					os.writeS("\\fI내용: \\aA마안 랜덤 획득.");
//					break;
//				}
				
				switch (getItem().getItemId()) {
				case 40308:	// 아데나
					os.writeC(39);
					os.writeS("\\fI설명: \\aA화폐.");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 4100136:	// 사신의 숨결
				case 680777:	// 할파스의 숨결
				case 68076:		// 고대의 서: 무기
				case 68077:		// 고대의 서: 방어구
				case 3000380:	// 고대의 서: 악세
					os.writeC(39);
					os.writeS("\\fI효과: \\aA확률 인챈트 +1");
					os.writeC(39);
					os.writeS("\\fI설명: \\aA증발되지 않음");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 40346:	// 안타라스의 숨결
				case 40362:	// 파푸리온의 숨결
				case 40370:	// 린드비오르의 숨결
				case 40354:	// 발라카스의 숨결
					os.writeC(39);
					os.writeS("\\fI위치: \\aA강화술사 이벨빈");
					os.writeC(39);
					os.writeS("\\fI설명: \\aA강화 재료");
					break;
				}

//				switch (getItem().getItemId()) {
//				case 300000: // 무기 마법 부여 코인(사용X)
//					os.writeC(39);
//					os.writeS("\\fI설명: \\aA전설급 무기류");
//					os.writeC(39);
//					os.writeS("\\fI인챈: \\aA+5/+7/+8/+9 사용");
//					break;
//				}
//				switch (getItem().getItemId()) {
//				case 4100139: //분신 구슬(1단계)
//				case 4100179: //분신 구슬(2단계)
//				case 4100180: //분신 구슬(3단계)
//					os.writeC(39);
//					os.writeS("\\fI설명: \\aA자신과 동일인 분신");
//					os.writeC(39);
//					os.writeS("\\fI업그레이드: \\aA총 3단계 가능");
//					os.writeC(39);
//					os.writeS("\\fI인챈소비재료: \\aA영생의 빛");
//					break;
//				}
				
				/* 리니지M 변신 카드 옵션 표기 */
				switch (getItem().getItemId()) {
				case 220004: //드래곤 슬레이어 변신 카드
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
					os.writeC(39);
					os.writeS("\\fIPVP 추가 대미지: \\aA+2");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 45450: // 자동 설정
				case 4200098: // 자동 물약 상자
					os.writeC(39);
					os.writeS("\\fI기능: \\aA자동 기능 설정");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 900075: // 변신 지배 반지
					os.writeC(39);
					os.writeS("\\fI설명: \\aA리니지m 변신");
					os.writeC(39);
					os.writeS("\\fI효과: \\aAHP+3000 증가");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 140009: // 변신 유일 반지
					os.writeC(39);
					os.writeS("\\fI설명: \\aA기르타스 변신");
					os.writeC(39);
					os.writeS("\\fI효과: \\aAHP+5000 증가");
					break;
				}
				
				switch (getItem().getItemId()) {
				case 410063: // 드래곤의 진주
				case 410137: // 수련자의 드래곤의 진주
					os.writeC(39);
					os.writeS("\\fI설명: \\aA3단 가속");
					break;
				}

				
				
				switch (getItem().getItemId()) {
				case 500731: // 무한 드래곤의 진주 상자
				case 500727: // 무한 드래곤의 진주 7일
					os.writeC(39);
//					os.writeS("\\fI기간: \\aA7일 후 자동 삭제");
					os.writeS("\\fI설명: \\aA3단 가속");
					break;
				}

				switch (getItem().getItemId()) {
				case 3000249: // N코인 1000원
				case 4100056: // N코인 5000원
				case 4100057: // N코인 15000원
				case 4100058: // N코인 30000원
//					os.writeC(39);
//					os.writeS("\\fI매입: \\aA주차별 매입");
//					os.writeC(39);
//					os.writeS("\\fI1주차:70%");
//					os.writeC(39);
//					os.writeS("\\fI2주차:80%");
//					os.writeC(39);
//					os.writeS("\\fI3주차:90%");
//					os.writeC(39);
//					os.writeS("\\fI4주차:100%");
					break;
				}
				switch (getItem().getItemId()) {
				case 300001:
					os.writeC(39);
					os.writeS("\\fI설명: \\aA스턴/공포 내성 증가");
					os.writeC(39);
					os.writeS("\\fI인챈: \\aA총 5단계 가능");
					break;
				}
//				switch (getItem().getItemId()) {
//				case 4100186: //빛나는 아덴 용사 랜덤 주머니
//					os.writeC(39);
//					os.writeS("\\fI설명: \\aA아덴용사 방어구류");
//					os.writeC(39);
//					os.writeS("\\fI인챈: \\aA3~5 랜덤");
//					break;
//				}
//				switch (getItem().getItemId()) {
//				case 3000518: //고대의 룬 보호석
//					os.writeC(39);
//					os.writeS("\\fI설명: \\aA확률 인챈 +1");
//					os.writeC(39);
//					os.writeS("         \\aA+6부터 전체옵션 +1");
//					break;
//				}
//				switch (getItem().getItemId()) {
//				case 3000517: //고대의 룬 강화석
//					os.writeC(39);
//					os.writeS("\\fI설명: \\aA확률 인챈 +1");
//					os.writeC(39);
//					os.writeS("         \\aA+6부터 전체옵션 +1");
//					break;
//				}
				switch (getItem().getItemId()) {
				case 40014:
				case 140014:
				case 41415:
					os.writeClass(2 + 128);
					break;
				}
				switch (getItem().getItemId()) {
				case 40068:
				case 140068:
				case 210110:
					os.writeClass(4);
					break;
				}
				switch (getItem().getItemId()) {
				case 713:
				case 210036:
					os.writeClass(64);
					break;
				}
				switch (getItem().getItemId()) {
				case 712:
				case 40031:
					os.writeClass(1);
					break;
				}
				switch (getItem().getItemId()) {
				case 40016:
				case 140016:
				case 210113:
					os.writeClass(8 + 64);
					break;
				}
				switch (getItem().getItemId()) {
				case 40184:
				case 40183:
				case 40179:
				case 40181:
					os.writeClass(4 + 8);
					os.writeLawful(0);
					os.writeStep(5);
					break;
				}
				switch (getItem().getItemId()) {
			/*	case 41288: // 환상 개미다리 치즈구이
					os.writeAddAc(1);
					os.writeDMGdown(5);
					break;
				case 41280: // 개미다리 치즈구이
					os.writeAddAc(1);
					os.writeDMGdown(5);
					break;
				case 41286: // 환상 곰 고기 구이
					os.writeAddMaxHP(30);
					os.writeDMGdown(5);
					break;
				case 41278: // 곰 고기 구이
					os.writeAddMaxHP(30);
					os.writeDMGdown(5);
					break;
				case 41289: // 환상 과일 샐러드
					os.writeAddMPPrecovery(20);
					break;
				case 41281: // 과일 샐러드
					os.writeAddMPPrecovery(20);
					break;
				case 41290: // 환상 과일 탕수육
					os.writeAddHPPrecovery(3);
					break;
				case 41282: // 과일 탕수육
					os.writeAddHPPrecovery(3);
					break;
				case 41285: // 환상 괴물 눈 스테이크
					os.writeRegistEarth(10);
					os.writeRegistFire(10);
					os.writeRegistWater(10);
					os.writeRegistWind(10);
					break;
				case 41277: // 괴물 눈 스테이크
					os.writeRegistEarth(10);
					os.writeRegistFire(10);
					os.writeRegistWater(10);
					os.writeRegistWind(10);
					break;
				case 41291: // 환상 멧돼지 꼬치 구이
					os.writeAddMR(5);
					break;
				case 41283: // 멧돼지 꼬치 구이
					os.writeAddMR(5);
					break;
				case 41292: // 환상 버섯스프
					os.writeAddEXP(1);
					break;
				case 41284: // 버섯스프
					os.writeAddEXP(1);
					break;
				case 41287: // 환상 씨호떡
					os.writeAddMPPrecovery(3);
					break;
				case 41279: // 씨호떡
					os.writeAddMPPrecovery(3);
					break;
				case 49063: // 환상 거미 다리 꼬치 구이
					os.writeAddSP(1);
					break;
				case 49055: // 거미 다리 꼬치 구이
					os.writeAddSP(1);
					break;
				case 49061: // 환상 스콜피온 구이
					os.writeAddHPPrecovery(2);
					os.writeAddMPPrecovery(2);
					break;
				case 49053: // 스콜피온 구이
					os.writeAddHPPrecovery(2);
					os.writeAddMPPrecovery(2);
					break;
				case 49058: // 환상 악어 스테이크
					os.writeAddMaxHP(30);
					os.writeAddMaxHP(30);
					break;
				case 49050: // 악어 스테이크
					os.writeAddMaxHP(30);
					os.writeAddMaxHP(30);
					break;
				case 49062: // 환상 일렉카둠 스튜
					os.writeAddMR(10);
					break;
				case 49054: // 일렉카둠 스튜
					os.writeAddMR(10);
					break;
				case 49057: // 환상 캐비어 카나페
					os.writeShortDMG(1);
					os.writeShortHIT(1);
					break;
				case 49049: // 캐비어 카나페
					os.writeShortDMG(1);
					os.writeShortHIT(1);
					break;
				case 49064: // 환상 크랩살스프
					os.writeAddEXP(2);
					break;
				case 49056: // 크랩살스프
					os.writeAddEXP(2);
					break;
				case 49060: // 환상 키위 패롯 구이
					os.writeLongDMG(1);
					os.writeLongHIT(1);
					break;
				case 49052: // 키위 패롯 구이
					os.writeLongDMG(1);
					os.writeLongHIT(1);
					break;
				case 49059: // 환상 터틀 드래곤 과자
					os.writeAddAc(2);
					break;
				case 49051: // 터틀 드래곤 과자
					os.writeAddAc(2);
					break;
				case 210057: // 환상 그리폰 구이
					os.writeAddMaxHP(50);
					os.writeAddMaxHP(50);
					break;
				case 210049: // 그리폰 구이
					os.writeAddMaxHP(50);
					os.writeAddMaxHP(50);
					break;
				case 210059: // 환상 대왕거북 구이
					os.writeAddAc(3);
					break;
				case 210051: // 대왕거북 구이
					os.writeAddAc(3);
					break;
				case 210061: // 환상 드레이크 구이
					os.writeAddSP(2);
					os.writeAddMPPrecovery(2);
					break;
				case 210053: // 드레이크 구이
					os.writeAddSP(2);
					os.writeAddMPPrecovery(2);
					break;
				case 210060: // 환상 레서 드래곤 날개 꼬치
					os.writeAddMR(15);
					os.writeRegistEarth(10);
					os.writeRegistFire(10);
					os.writeRegistWater(10);
					os.writeRegistWind(10);
					break;
				case 210052: // 레서 드래곤 날개 꼬치
					os.writeAddMR(15);
					os.writeRegistEarth(10);
					os.writeRegistFire(10);
					os.writeRegistWater(10);
					os.writeRegistWind(10);
					break;
				case 210063: // 환상 바실리스크 알 스프
					os.writeAddEXP(3);
					break;
				case 210055: // 바실리스크 알 스프
					os.writeAddEXP(3);
					break;
				case 210062: // 환상 심해어 스튜
					os.writeAddMaxHP(30);
					os.writeAddHPPrecovery(2);
					break;
				case 210054: // 심해어 스튜
					os.writeAddMaxHP(30);
					os.writeAddHPPrecovery(2);
					break;
				case 210058: // 환상 코카트리스 스테이크
					os.writeShortHIT(2);
					os.writeShortDMG(1);
					break;
				case 210050: // 코카트리스 스테이크
					os.writeShortHIT(2);
					os.writeShortDMG(1);
					break;
				case 210056: // 환상 크러스트시안 집게발 구이
					os.writeLongHIT(2);
					os.writeLongDMG(1);
					break;
					
				case 210048: // 크러스트시안 집게발 구이
					os.writeLongHIT(2);
					os.writeLongDMG(1);
					break;*/
					
				case 30054:// 수련의 닭고기 스프
						os.writeDMGdown(2); // 리덕
						os.writeAddEXP(4); // 경험치 보너스
						os.writeC(39);
						os.writeS("\\fI사용 시간: \\aA30분.");
						break;
	
				case 30051:// 힘센한우스테이크
						os.writeDMGdown(2); // 리덕
						os.writeShortDMG(2); // 근뎀
						os.writeShortHIT(1); // 근거리명중
						os.writeAddHPPrecovery(2); // 피회북 
						os.writeAddMPPrecovery(2); // 엠회복
						os.writeAddMR(10); // 마방
						os.writeC(39);
						os.writeS("\\fI속성 저항: \\aA전체 +10");
						os.writeAddEXP(2); // 경험치 보너스
						break;
											
				case 4100156:// (축)힘센한우스테이크
						os.writeDMGdown(2); // 리덕
						os.writeShortDMG(2); // 근뎀
						os.writeShortHIT(1); // 근거리명중	
						os.writeAddHPPrecovery(2); // 피회북 
						os.writeAddMPPrecovery(2); // 엠회복
						os.writeAddMR(10); // 마방
						os.writeC(39);
						os.writeS("\\fI속성 저항: \\aA전체 +10");
						os.writeAddEXP(2); // 경험치 보너스
						os.writeaAll_pierce(3); // 모든 적중
						break;
						
				case 30052: // 날쎈 연어 찜
					os.writeDMGdown(2); // 리덕
					os.writeLongDMG(2); // 원뎀
					os.writeLongHIT(1); // 원거리명중
					os.writeAddHPPrecovery(2); // 피회북 
					os.writeAddMPPrecovery(2); // 엠회복
					os.writeAddMR(10); // 마방
					os.writeC(39);
					os.writeS("\\fI속성 저항: \\aA전체 +10");
					os.writeAddEXP(2); // 경험치 보너스
					break;
					
				case 4100157: // (축) 날쌘 연어 찜
					os.writeDMGdown(2); // 리덕
					os.writeLongDMG(2); // 원뎀
					os.writeLongHIT(1); // 원거리명중
					os.writeAddHPPrecovery(2); // 피회북 
					os.writeAddMPPrecovery(2); // 엠회복
					os.writeAddMR(10); // 마방
					os.writeC(39);
					os.writeS("\\fI속성 저항: \\aA전체 +10");
					os.writeAddEXP(2); // 경험치 보너스
					os.writeaAll_pierce(3); // 모든 적중
					break;
					
				case 30053: // 영리한 칠면조 구이
					os.writeDMGdown(2); // 리덕
					os.writeAddSP(2); // sp
					os.writeAddHPPrecovery(2); // 피회북 
					os.writeAddMPPrecovery(2); // 엠회복
					os.writeAddMR(10); // 마방
					os.writeC(39);
					os.writeS("\\fI속성 저항: \\aA전체 +10");
					os.writeAddEXP(2); // 경험치 보너스
					os.writeaAll_pierce(3); // 모든 적중
					break;
					
				case 4100158: // (축) 영리한 칠면조 구이
					os.writeDMGdown(2); // 리덕
					os.writeAddSP(2); // sp
					os.writeAddHPPrecovery(2); // 피회북 
					os.writeAddMPPrecovery(2); // 엠회복
					os.writeAddMR(10); // 마방
					os.writeC(39);
					os.writeS("\\fI속성 저항: \\aA전체 +10");
					os.writeAddEXP(2); // 경험치 보너스
					os.writeaAll_pierce(3); // 모든 적중
					break;
				}

				/** 특수 마법인형 인챈트 시스템 **/
				switch (get_item_level()) {
				case 1:
					os.writeC(39);
					os.writeS("\\fI1단계: \\aA스턴/공포 내성+1");
					break;
				case 2:
					os.writeC(39);
					os.writeS("\\fI2단계: \\aA스턴/공포 내성+2");
					break;
				case 3:
					os.writeC(39);
					os.writeS("\\fI3단계: \\aA스턴/공포 내성+4");
					break;
				case 4:
					os.writeC(39);
					os.writeS("\\fI4단계: \\aA스턴/공포 내성+7");
					break;
				case 5:
					os.writeC(39);
					os.writeS("\\fI5단계: \\aA스턴/공포 내성+10");
					break;
				}
				
				/**일일 구매제한*/
				if(getShopStr().equals("") == false){
					os.writeC(39);
					os.writeS(getShopStr());
				}
				
				// TODO ETC 표기 설정 시작
				int dmg = getItem().getDmgModifier();
				int hit = getItem().getHitModifier();

				if (getItem().getMinLevel() != 0) { // 최소 사용 레벨
					os.writeUseLevel(getItem().getMinLevel());
				}
				if (getItem().getMaxLevel() != 0) { // 최소~최대 사용 레벨
					os.writeLimitLevel(getItem().getMinLevel(), getItem().getMaxLevel());
				}

				if (dmg > 0)
					os.writeLongDMG(dmg);
				if (hit > 0)
					os.writeLongHIT(hit);
				if (itemId == 3000516) {
					os.writeAttrDmg(3);
				}

				if (itemType2 == 0 || itemType2 == 15) {
					if (isUndeadDmg()) {
						os.writeC(114);
						os.writeD(1);
					}
				}

				os.writeC(130);
				if (!getItem().isTradable()) {
					os.writeD(6);
				} else {
					os.writeD(7);
				}
				
				
			} else if (itemType2 == 1 || itemType2 == 2) { // weapon | armor
				if (itemId == 900116) { // 고대의 룬
			    switch (getEnchantLevel()) {
			     case 0:
			     case 1:
			     case 2:
			     case 3:
				 os.writeAddEXP(5);
			      break;
			     case 4:
				 os.writeAddEXP(6);			 
			      break;
			     case 5:
			     os.writeAddEXP(7);			 
			      break;
			     case 6:
				os.writeShortDMG(4); // 근뎀
			    os.writeLongDMG(4); // 원뎀
				os.writeAddSP(4); // sp
				os.writeShortHIT(4); // 근명
				os.writeLongHIT(4); // 롱명
			    os.writeMagicHIT(4);
				os.writeAddMR(4);
				os.writeAddEXP(8);			 
			      break;
			     case 7:
						os.writeShortDMG(5); // 근뎀
					    os.writeLongDMG(5); // 원뎀
						os.writeAddSP(5); // sp
						os.writeShortHIT(6); // 근명
						os.writeLongHIT(6); // 롱명
					    os.writeMagicHIT(6);
					    os.writeAddMR(6);
						os.writeAddEXP(9);			 
			      break;
			     case 8:
						os.writeShortDMG(6); // 근뎀
					    os.writeLongDMG(6); // 원뎀
						os.writeAddSP(6); // sp
						os.writeShortHIT(8); // 근거리명중
						os.writeLongHIT(8); // 원거리명중
					    os.writeMagicHIT(8); // 마법명중
					    os.writeAddMR(8);
						os.writeAddEXP(10);			
			      break;
			     default:
			           }
			    }
			
				
				
				int op_addAc = 0;
				/**일일 구매제한*/
				if(getShopStr().equals("") == false){
					os.writeC(39);
					os.writeS(getShopStr());
				}

				/** 아이템 안전인챈 표시 추가 **/
			/*	int SafeEnchant = getItem().get_safeenchant(); os.writeC(39);
				if (SafeEnchant < 0) { SafeEnchant = 0; }
				os.writeS("\\aG[안전인챈:+" + SafeEnchant + "]");*/
				 

				if (itemType2 == 1) { // weapon 무기 타격치
					os.writeC(1);
					os.writeC(getItem().getDmgSmall());
					os.writeC(getItem().getDmgLarge());
					os.writeC(getItem().getMaterial());
					os.writeD(getWeight());
				} else if (itemType2 == 2) { // armor
					if (getItem().getType() == 14) {
						os.writeC(19);
						int ac = getRuneAc();
						os.writeC(ac - ac - ac);
					} else {
						os.writeC(19);
						int ac = ((L1Armor) getItem()).get_ac();
						int Grade = ((L1Armor) getItem()).getGrade();
						if (ac < 0) {
							ac = ac - ac - ac;
						} else
							ac *= -1;
						os.writeC(ac - get_durability());
					}
					
					os.writeC(getItem().getMaterial());
					os.writeH(-1);
					os.writeD(getWeight());
				}
				
				
				
				if (itemId == 900081 || itemId == 900082 || itemId == 900083) { // 커츠의
					if (getEnchantLevel() == 4) {
						os.writeC(2);
						os.writeC(1);
					} else if (getEnchantLevel() == 5) {
						os.writeC(2);
						os.writeC(3);
					} else if (getEnchantLevel() == 6) {
						os.writeC(2);
						os.writeC(5);
					} else if (getEnchantLevel() == 7) {
						os.writeC(2);
						os.writeC(6);
					} else if (getEnchantLevel() == 8) {
						os.writeC(2);
						os.writeC(7);
					}
				} else if (itemId == 900152 || itemId == 900153 || itemId == 900154) {
					if (getEnchantLevel() == 4) {
						os.writeC(2);
						os.writeC(1);
					} else if (getEnchantLevel() == 5) {
						os.writeC(2);
						os.writeC(2);
					} else if (getEnchantLevel() == 6) {
						os.writeC(2);
						os.writeC(3);
					} else if (getEnchantLevel() == 7) {
						os.writeC(2);
						os.writeC(3);
					} else if (getEnchantLevel() == 8) {
						os.writeC(2);
						os.writeC(3);
					}
				} else if (itemId == 900084) { // 수호 휘장
					if (getEnchantLevel() >= 6) {
						os.writeC(2);
						os.writeC(getEnchantLevel() - 1);
					} else if (getEnchantLevel() >= 3) {
						os.writeC(2);
						os.writeC(getEnchantLevel() - 2);
					}
				} else if (itemId == 900118 || itemId == 900117 || itemId == 900119 || itemId == 900120) {
					int enchant = getEnchantLevel();
					os.write(0x02);
					if (enchant >= 5) {
						os.write(enchant);
					} else {
						os.write(0x00);
					}
				} else if (getEnchantLevel() >= 5 && itemType2 == 2
						&& (getItem().get장신구처리() == 8 || getItem().get장신구처리() == 12)) {
					int enchantAc = getEnchantLevel() - 4;
					if (enchantAc >= 5) {
						enchantAc = 5;
					}
					os.writeC(2);
					os.writeC(enchantAc);
				} else if (getEnchantLevel() != 0 && !(itemType2 == 2 && getItem().getGrade() >= 0)) {
					if (getItemId() == 66) {
						os.writeC(0x6B);
						int v = getEnchantLevel() * 2;
						os.writeC(v);
						os.writeC(v);
					} else {
						os.writeC(2);
						os.writeC(getEnchantLevel());
					}
					/** 룸티스 검은빛 귀걸이 AC표현처리 부분 **/
				} else if (itemId == 22229) {
					int enchant = getEnchantLevel();
					if (enchant >= 6) {
						os.writeC(0x02);
						if (enchant >= 8)
							os.writeC(9);
						else if (enchant >= 7)
							os.writeC(8);
						else
							os.writeC(7);
					}
				} else if (itemId == 222337) {
					int enchant = getEnchantLevel();
					if (enchant >= 5) {
						os.writeC(0x02);
						if (enchant >= 8)
							os.writeC(10);
						else if (enchant >= 7)
							os.writeC(9);
						else if (enchant >= 6)
							os.writeC(8);
						else
							os.writeC(7);
					}
				} else if (itemId == 22231) {
					int enchant = getEnchantLevel();
					if (enchant >= 6) {
						os.writeC(0x02);
						if (enchant >= 8)
							os.writeC(3);
						else if (enchant >= 7)
							os.writeC(2);
						else
							os.writeC(1);
					}
				} else if (itemId == 222339) {
					int enchant = getEnchantLevel();
					if (enchant >= 5) {
						os.writeC(0x02);
						if (enchant >= 8)
							os.writeC(4);
						else if (enchant >= 7)
							os.writeC(3);
						else if (enchant >= 6)
							os.writeC(2);
						else
							os.writeC(1);
					}
				} else if (itemType2 == 2 && itemId == 222340 || itemId == 222341) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(1 + op_addAc);
						break;
					case 2:
						os.writeC(2 + op_addAc);
						break;
					case 3:
						if (itemId == 222341) {
							os.writeC(4 + op_addAc);
						} else {
							os.writeC(3 + op_addAc);
						}
						break;
					case 4:
						if (itemId == 222341) {
							os.writeC(5 + op_addAc);
						} else {
							os.writeC(4 + op_addAc);
						}
						break;
					case 5:
						if (itemId == 222341) {
							os.writeC(6 + op_addAc);
						} else {
							os.writeC(5 + op_addAc);
						}
						break;
					case 6:
						if (itemId == 222341) {
							os.writeC(7 + op_addAc);
						} else {
							os.writeC(6 + op_addAc);
						}
						break;
					case 7:
						if (itemId == 222341) {
							os.writeC(8 + op_addAc);
						} else {
							os.writeC(7 + op_addAc);
						}
						break;
					case 8:
						if (itemId == 222341) {
							os.writeC(9 + op_addAc);
						} else {
							os.writeC(8 + op_addAc);
						}
						break;
					default:
						os.writeC(0 + op_addAc);
					}
				} else if (itemType2 == 2 && itemId == 22226) {
					int enchant = getEnchantLevel();
					if (enchant >= 6) {
						os.writeC(0x02);
						os.writeC(4);
					} else if (enchant >= 4) {
						os.writeC(0x02);
						os.writeC(0x03);
					} else if (enchant >= 3) {
						os.writeC(0x02);
						os.writeC(0x02);
					} else if (enchant >= 2) {
						os.writeC(0x02);
						os.writeC(0x01);
					}
					/** 스냅퍼의 회복 반지/스냅퍼의 집중 반지/스냅퍼의 마나 반지 AC부분 처리 **/
				} else if (itemType2 == 2 && itemId == 22224 || itemId == 22225 || itemId == 22227) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 2:
						os.writeC(1 + op_addAc);
						break;
					case 3:
						os.writeC(2 + op_addAc);
						break;
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						os.writeC(3 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
					/** 스냅퍼의 마법저항반지 AC부분 처리 **/
				} else if (itemType2 == 2 && itemId == 22228 || itemId == 22226) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 2:
						os.writeC(1 + op_addAc);
						break;
					case 3:
						os.writeC(2 + op_addAc);
						break;
					case 4:
					case 5:
						os.writeC(3 + op_addAc);
						break;
					case 6:
					case 7:
					case 8:
						os.writeC(4 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
					/** 스냅퍼의 지혜 반지 AC부분 처리 **/
				} else if (itemType2 == 2 && itemId == 222290) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
						break;
					case 2:
						os.writeC(2);
						os.writeC(1 + op_addAc);
						break;
					case 3:
						os.writeC(2);
						os.writeC(2 + op_addAc);
						break;
					case 4:
					case 5:
						os.writeC(2);
						os.writeC(3 + op_addAc);
						break;
					case 6:
						os.writeC(2);
						os.writeC(4 + op_addAc);
						break;
					case 7:
						os.writeC(2);
						os.writeC(4 + op_addAc);
						os.writeMagicHIT(1);
						break;
					case 8:
						os.writeC(2);
						os.writeC(5 + op_addAc);
						os.writeMagicHIT(2);
						break;
					default:
						os.writeC(2);
						os.writeC(0 + op_addAc);
					}
					/** 축복받은 스냅퍼의 지혜 반지 AC부분 처리 **/
				} else if (itemType2 == 2 && itemId == 222335) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 2:
						os.writeC(1 + op_addAc);
						break;
					case 3:
						os.writeC(2 + op_addAc);
						break;
					case 4:
					case 5:
						os.writeC(3 + op_addAc);
						break;
					case 6:
						os.writeC(3 + op_addAc);
						os.writeMagicHIT(1);
						break;
					case 7:
						os.writeC(4 + op_addAc);
						os.writeMagicHIT(2);
						break;
					case 8:
						os.writeC(4 + op_addAc);
						os.writeMagicHIT(3);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
					/** 스냅퍼의 용사 반지 AC부분 처리 **/
				} else if (itemType2 == 2 && itemId == 222291) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(1 + op_addAc);
						break;
					case 2:
						os.writeC(2 + op_addAc);
						break;
					case 3:
						os.writeC(3 + op_addAc);
						break;
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						os.writeC(4 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
				} else if (itemId == 222332) {
					int enchant = getEnchantLevel();
					if (enchant >= 7) {
						os.writeC(0x02);
						os.writeC(0x05);
					} else if (enchant >= 5) {
						os.writeC(0x02);
						os.writeC(0x04);
					} else if (enchant >= 4) {
						os.writeC(0x02);
						os.writeC(0x03);
					} else if (enchant >= 3) {
						os.writeC(0x02);
						os.writeC(0x02);
					} else if (enchant >= 2) {
						os.writeC(0x02);
						os.writeC(0x01);
					}
					/** 축복받은 스냅퍼 체력,마법저항 반지 AC부분 처리 **/
				} else if (itemId == 222334) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 2:
						os.writeC(1 + op_addAc);
						break;
					case 3:
						os.writeC(2 + op_addAc);
						break;
					case 4:
						os.writeC(3 + op_addAc);
						break;
					case 5:
					case 6:
						os.writeC(4 + op_addAc);
						break;
					case 7:
					case 8:
						os.writeC(5 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
					/** 축복받은 스냅퍼의 회복,집중,마나 반지 AC부분 처리 **/
				} else if (itemType2 == 2 && itemId == 222330 || itemId == 222331 || itemId == 222333) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 2:
						os.writeC(1 + op_addAc);
						break;
					case 3:
						os.writeC(2 + op_addAc);
						break;
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						os.writeC(3 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
					/** 축복받은 스냅퍼 용사의반지 AC부분 처리 **/
				} else if (itemType2 == 2 && itemId == 222336) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(1 + op_addAc);
						break;
					case 2:
						os.writeC(2 + op_addAc);
						break;
					case 3:
						os.writeC(3 + op_addAc);
						break;
					case 4:
					case 5:
					case 6:
						os.writeC(4 + op_addAc);
						break;
					case 7:
					case 8:
						os.writeC(5 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
					/** 룸티스의 푸른빛 귀걸이 AC부분 처리 **/
				} else if (itemType2 == 2 && getItem().getGrade() == 4 && itemId == 22230) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 5:
						os.writeC(1 + op_addAc);
						break;
					case 6:
					case 7:
						os.writeC(2 + op_addAc);
						break;
					case 8:
						os.writeC(3 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
					/** 축복받은 룸티스의 푸른빛 귀걸이 AC부분 처리 **/
				} else if (itemType2 == 2 && getItem().getGrade() == 4 && itemId == 222338) {
					os.writeC(2);
					switch (getEnchantLevel()) {
					case 4:
						os.writeC(1 + op_addAc);
						break;
					case 5:
					case 6:
						os.writeC(2 + op_addAc);
						break;
					case 7:
						os.writeC(3 + op_addAc);
						break;
					case 8:
						os.writeC(4 + op_addAc);
						break;
					default:
						os.writeC(0 + op_addAc);
					}
				}
				
				
				/** 스냅퍼의 반지 추타 표기 **/
				if (itemType2 == 2 && itemId >= 22224 && itemId <= 22228 && getEnchantLevel() > 4
						|| itemId == 222291 && getEnchantLevel() > 4) {
					os.writeC(6);
					os.writeC(getItem().getDmgModifier() + getEnchantLevel() - 4);
					/** 축복받은 스냅퍼의 반지 추타 표기 **/
				} else if (itemType2 == 2 && itemId >= 222330 && itemId <= 222334 && getEnchantLevel() > 3
						|| itemId == 222336 && getEnchantLevel() > 3) {
					os.writeC(6);
					os.writeC(getItem().getDmgModifier() + getEnchantLevel() - 3);
					
				/** 무기 인챈 당 추타 [전설급+드슬x2(지배 및 집행은 따로)] **/
				} else if (getItem().getDmgModifier() != 0) {
					if (itemType2 == 1 && getItem().getType1() != 20) {
						if (getEnchantLevel() > 0) {
							if(itemId == 2944 || itemId == 217 || itemId == 2945 ||itemId == 66){
								os.writeWeaponDmg(getEnchantLevel() * 2, getEnchantLevel() * 2);
							}else {
								os.writeWeaponDmg(getEnchantLevel(), getEnchantLevel());
							}
						}
						os.writeShortDMG(getItem().getDmgModifier());
					} else if (itemType2 == 1) {
						if (getEnchantLevel() > 0) {
							if(itemId == 2944 || itemId == 217 || itemId == 2945 ||itemId == 66){
								os.writeWeaponDmg(getEnchantLevel() * 2, getEnchantLevel() * 2);
							}else {
								os.writeWeaponDmg(getEnchantLevel(), getEnchantLevel());
							}
						}
						os.writeLongDMG(getItem().getDmgModifier());
						//enchant * 2
					} else {
						os.writeAddDMG(getItem().getDmgModifier());
					}
				} else {
					if (isUndeadDmg()) {
						if (itemType2 == 1 && getItem().getType1() != 20) {
							os.writeShortDMG(0);
						} else {
							os.writeLongDMG(0);
						}
					}else {
						if(itemId == 2944 || itemId == 217 || itemId == 2945 ||itemId == 66){
							os.writeWeaponDmg(getEnchantLevel() * 2, getEnchantLevel() * 2);
							os.writeShortDMG(0);
						}
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				if (getItem().isTwohandedWeapon()) { // 양손무기
					os.writeC(4);
				}
				if (get_durability() != 0) { // 손상도
					os.writeC(3);
					os.writeC(get_durability());
				}
				/** 클래스 착용 부분 **/
				int bit = 0;
				bit |= getItem().isUseRoyal() ? 1 : 0;
				bit |= getItem().isUseKnight() ? 2 : 0;
				bit |= getItem().isUseElf() ? 4 : 0;
				bit |= getItem().isUseMage() ? 8 : 0;
				bit |= getItem().isUseDarkelf() ? 16 : 0;
				bit |= getItem().isUseDragonKnight() ? 32 : 0;
				bit |= getItem().isUseBlackwizard() ? 64 : 0;
				bit |= getItem().isUse전사() ? 128 : 0;
				os.writeC(7);
				os.writeC(bit);

				os.writeC(130);
				if (!getItem().isTradable()) {
					os.writeD(6);
				} else {
					os.writeD(7);
				}

				if (isCanbeDmg()) {
					os.writeC(131);
					os.writeD(1); // 비손상
				}

				os.writeC(132);
				os.writeD(3); // 성별 구분 1.남자 2.여자 3.전체

				if (itemType2 == 1 && isUndeadDmg()) {
					os.writeC(114);
					os.writeD(1); // 언데드
				}
				
				
				
				
				
				
				

				/** 55레벨 엘릭서 룬 옵션 표시 **/
				if (itemId == 222295 && _cha != null) { // 민첩의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						break;
					case 1:
					case 7:
						os.writeC(31);
						os.writeH(50);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						break;
					}
				}
				if (itemId == 222296 && _cha != null) { // 체력의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						break;
					case 1:
					case 7:
						os.writeC(31);
						os.writeH(50);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						break;

					case 5:
						os.writeC(48);
						os.writeC(3);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						break;
					}
				}
				if (itemId == 222297 && _cha != null) { // 지식의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						break;
					case 1:
					case 7:
						os.writeC(31);
						os.writeH(50);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						break;

					case 5:
						os.writeC(48);
						os.writeC(3);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						break;
					}
				}
				if (itemId == 222298 && _cha != null) { // 지혜의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						break;
					case 1:
					case 7:
						os.writeC(31);
						os.writeH(50);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						break;

					case 5:
						os.writeC(48);
						os.writeC(3);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						break;
					}
				}
				if (itemId == 222299 && _cha != null) { // 힘의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						break;
					case 1:
					case 7:
						os.writeC(31);
						os.writeH(50);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						break;

					case 5:
						os.writeC(5);
						os.writeC(3);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						break;
					}
				}
				/** 70레벨 엘릭서 룬 옵션 표시 **/
				if (itemId == 222312 && _cha != null) { // 민첩의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(5);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(32);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 222313 && _cha != null) { // 체력의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 222314 && _cha != null) { // 지식의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 222315 && _cha != null) { // 지혜의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 222316 && _cha != null) { // 힘의 엘릭서
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeAddAc(3);
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				// TODO 80레벨 엘릭서 룬 옵션 표시
				if (itemId == 900135 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(5);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(32);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900136 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900137 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900138 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900139 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				// TODO 85레벨 엘릭서 룬 옵션 표시
				if (itemId == 900140 && _cha != null) {
					// System.out.println(_cha.getType());
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:// 기사
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(5);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(32);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900141 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900142 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900143 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900144 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				// TODO 90레벨 엘릭서 룬 옵션 표시
				if (itemId == 900145 && _cha != null) {
					// System.out.println(_cha.getType());
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:// 기사
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(5);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(32);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900146 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900147 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900148 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}
				if (itemId == 900149 && _cha != null) {
					switch (_cha.getType()) {
					case 0:
						os.writeC(63);
						os.writeC(3);
						os.writeC(48);
						os.writeC(2);
						break;
					case 1:
						os.writeC(31);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						break;
					case 2:
						os.writeC(32);
						os.writeH(50);
						os.writeC(47);
						os.writeC(1);
						os.writeC(35);
						os.writeC(1);
						break;
					case 3:
						os.writeC(38);
						os.writeC(3);
						os.writeC(17);
						os.writeC(1);
						break;
					case 4:
						os.writeC(32);
						os.writeH(30);
						break;
					case 5:
						os.writeC(48);
						os.writeC(3);
						os.writeC(63);
						os.writeC(1);
						break;
					case 6:
						os.writeC(68);
						os.writeC(5);
						os.writeC(31);
						os.writeH(50);
						break;
					case 7:
						os.writeC(15);
						os.writeH(5);
						os.writeC(31);
						os.writeH(50);
						break;
					}
				}

				/** 스냅퍼의 용사 반지 공격성공 표기 **/
				if (itemType2 == 2 && itemId == 222291 && getEnchantLevel() > 4) {
					os.writeShortHIT(getItem().getHitModifier() + getEnchantLevel() - 4);
					os.writeLongHIT(getItem().getHitModifier() + getEnchantLevel() - 4);
					/** 축복받은 스냅퍼의 용사 반지 공격성공 표기 **/
				} else if (itemType2 == 2 && itemId == 222336 && getEnchantLevel() > 3) {
					os.writeShortHIT(getItem().getHitModifier() + getEnchantLevel() - 3);
					os.writeLongHIT(getItem().getHitModifier() + getEnchantLevel() - 3);
				}

				/** 반지 5이상 추가 대미지 **/
				if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& getItem().get장신구처리() == 9 || getItem().get장신구처리() == 11) {
					if (getEnchantLevel() > 4) {
						os.writeShortDMG(getEnchantLevel() - 4);
						os.writeLongDMG(getEnchantLevel() - 4);
					}
				}
				/** 스냅퍼의 반지 PvP추가대미지 표시 **/
				if (itemType2 == 2 && itemId >= 22224 && itemId <= 22228 && getEnchantLevel() == 7
						|| itemId >= 222290 && itemId <= 222291 && getEnchantLevel() == 7
						|| itemId >= 222330 && itemId <= 222336 && getEnchantLevel() == 7) { // 인챈트가
																								// 7이면
					os.writeC(59);
					os.writeC(getEnchantLevel() - 6);
				} else if (itemType2 == 2 && itemId >= 22224 && itemId <= 22228 && getEnchantLevel() == 8
						|| itemId >= 222290 && itemId <= 222291 && getEnchantLevel() == 8
						|| itemId >= 222330 && itemId <= 222336 && getEnchantLevel() == 8) { // 인챈트가8이면
					os.writeC(59);
					os.writeC(getEnchantLevel() - 6);
				} else if (itemId >= 307 && itemId <= 314) {
					if (getEnchantLevel() == 7) {
						os.writeC(59);
						os.writeC(getEnchantLevel() - 4);
					} else if (getEnchantLevel() == 8) {
						os.writeC(59);
						os.writeC(getEnchantLevel() - 3);
					} else if (getEnchantLevel() == 9) {
						os.writeC(59);
						os.writeC(getEnchantLevel() - 2);
					} else if (getEnchantLevel() == 10) {
						os.writeC(59);
						os.writeC(getEnchantLevel());
					}
				}

				/** 스냅퍼의 반지류 HP증가 표시 **/
				if (itemType2 == 2 && itemId >= 22224 && itemId <= 22228 && getEnchantLevel() > 0) {
					int 스냅퍼HP증가 = getEnchantLevel() * 5 + 10;
					os.writeC(14);
					os.writeH(getItem().get_addhp() + 스냅퍼HP증가);

					/** 축복받은 스냅퍼의 체력 반지 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 222332 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 15);
						break;
					case 2:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 20);
						break;
					case 3:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 30);
						break;
					case 4:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 35);
						break;
					case 5:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 40);
						break;
					case 6:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 45);
						break;
					case 7:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 55);
						break;
					case 8:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 65);
						break;
					default:
						break;
					}
					/** 축복받은 스냅퍼의 마법저항,집중,마나 반지 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId >= 222330 && itemId <= 222331
						|| itemId >= 222333 && itemId <= 222334 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 15);
						break;
					case 2:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 20);
						break;
					case 3:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 30);
						break;
					case 4:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 35);
						break;
					case 5:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 40);
						break;
					case 6:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 45);
						break;
					case 7:
					case 8:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 50);
						break;
					default:
						break;
					}
					/** 스냅퍼의 지혜 반지 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 222290 && getEnchantLevel() > 0) {
					int 지혜반지HP증가 = (getEnchantLevel() * 5);
					os.writeC(14);
					os.writeH(getItem().get_addhp() + 지혜반지HP증가);
					/** 축복받은 스냅퍼의 지혜 반지 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 222335 && getEnchantLevel() >= 0) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 1:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 5);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 2:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 10);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 3:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 20);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 4:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 25);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 5:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 30);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 6:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 35);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 7:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 40);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 30);
						break;
					case 8:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 50);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 35);
						break;
					default:
						break;
					}
					/** 스냅퍼의 용사 반지 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 222291 && getEnchantLevel() >= 3) {
					int 용사반지HP증가 = (getEnchantLevel() - 2) * 5;
					os.writeC(14);
					os.writeH(getItem().get_addhp() + 용사반지HP증가);
					/** 축복받은 스냅퍼의 용사 반지 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 222336 && getEnchantLevel() > 2) {
					switch (getEnchantLevel()) {
					case 3:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 10);
						break;
					case 4:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 15);
						break;
					case 5:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 20);
						break;
					case 6:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 25);
						break;
					case 7:
					case 8:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 30);
						break;
					default:
						break;
					}
					/** 룸티스 붉은빛 귀걸이 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 22229 && getEnchantLevel() > 0) {
					int 붉귀HP증가 = (getEnchantLevel() * 10) + 10;
					os.writeC(14);
					os.writeH(getItem().get_addhp() + 붉귀HP증가);
					/** 축복받은 룸티스 붉은빛 귀걸이 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 222337 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 20);
						break;
					case 2:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 30);
						break;
					case 3:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 50);
						break;
					case 4:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 60);
						break;
					case 5:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 70);
						break;
					case 6:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 80);
						break;
					case 7:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 90);
						break;
					case 8:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 140);
						break;
					default:
						break;
					}
					/** 체력의 가더 HP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 22256 && getEnchantLevel() >= 5) {
					switch (getEnchantLevel()) {
					case 5:
					case 6:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 25);
						break;
					case 7:
					case 8:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 50);
						break;
					case 9:
					case 10:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 75);
						break;
					default:
						break;
					}
					// TODO 반지 귀걸이 인챈에대한 HP표기
				} else if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5 || itemgrade == 6)
						&& getItem().get장신구처리() != 10 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 5);
						break;
					case 2:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 10);
						break;
					case 3:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 20);
						break;
					case 4:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 30);
						break;
					case 5:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 40);
						break;
					case 6:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 40);
						break;
					case 7:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 50);
						break;
					case 8:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 50);
						break;
					case 9:
						os.writeC(14);
						os.writeH(getItem().get_addhp() + 60);
						break;
					default:
						break;
					}
				} else if (getEnchantLevel() >= 6 && itemType2 == 2 && getItem().get장신구처리() == 10) {
					int enchantAddHp = (getEnchantLevel() - 4) * 10;
					if (enchantAddHp >= 50) {
						enchantAddHp = 50;
					}
					os.writeC(14);
					os.writeH(getItem().get_addhp() + enchantAddHp);
				} else if (getItem().get_addhp() != 0) {
					os.writeC(14);
					os.writeH(getItem().get_addhp());
				}
				// TODO 벨트 인챈에 대한 MP증가 표기
				if (itemType2 == 2 && getItem().getType() == 10 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 5);
						break;
					case 2:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 10);
						break;
					case 3:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 20);
						break;
					case 4:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 30);
						break;
					case 5:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 40);
						break;
					case 6:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 40);
						break;
					case 7:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 50);
						break;
					case 8:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 50);
						break;
					case 9:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 50);
						break;
					default:
						break;
					}
					/** 룸티스의 보랏빛 귀걸이 MP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 22231 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 10);
						break;
					case 2:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 3:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 30);
						break;
					case 4:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 35);
						break;
					case 5:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 50);
						break;
					case 6:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 55);
						break;
					case 7:
						os.writeMagicHIT(1);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 70);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 70);
						break;
					case 8:
						os.writeMagicHIT(3);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 95);
						break;
					default:
						break;
					}
					/** 축복받은 룸티스의 보랏빛 귀걸이 MP증가 표시 **/
				} else if (itemType2 == 2 && itemId == 222339 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 10);
						break;
					case 2:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 15);
						break;
					case 3:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 35);
						break;
					case 4:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 50);
						break;
					case 5:
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 55);
						break;
					case 6:
						os.writeMagicHIT(1);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 70);
						break;
					case 7:
						os.writeMagicHIT(3);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 95);
						break;
					case 8:
						os.writeMagicHIT(5);
						os.writeC(32);
						os.writeH(getItem().get_addmp() + 125);
						break;
					default:
						break;
					}
					/** MP패킷변경됨 C/H **/
				} else if (addmp() != 0) {
					os.writeC(32);
					os.writeH(addmp());// mp부분패킷변경됨
				}
				// 피틱 표시
				if (getItem().get_addhpr() != 0) {
					os.writeC(37);
					os.writeC(getItem().get_addhpr());
				}
				// 엠틱 표시
				if (itemId == 1134 || itemId == 101134) {
					os.writeC(38);
					os.writeC(getItem().get_addmpr() + getEnchantLevel()); // 명상의
				} else if (getItem().get_addmpr() != 0) {
					os.writeC(38);
					os.writeC(getItem().get_addmpr());
				}
				/** 룸티스의 푸른빛 귀걸이 물약효율 표시 **/
				if (itemType2 == 2 && itemId == 22230 && getEnchantLevel() >= 0) {
					int lvl = getEnchantLevel();
					switch (lvl) {
					case 0:
						os.writePotionrecovery(2, 2);
						os.writeHealDefence(2);
						break;
					case 1:
						os.writePotionrecovery(6, 6);
						os.writeHealDefence(6);
						break;
					case 2:
						os.writePotionrecovery(8, 8);
						os.writeHealDefence(8);
						break;
					case 3:
						os.writePotionrecovery(10, 10);
						os.writeHealDefence(10);
						break;
					case 4:
						os.writePotionrecovery(12, 12);
						os.writeHealDefence(12);
						break;
					case 5:
						os.writePotionrecovery(14, 14);
						os.writeHealDefence(14);
						break;
					case 6:
						os.writePotionrecovery(16, 16);
						os.writeHealDefence(16);
						break;
					case 7:
						os.writePotionrecovery(18, 18);
						os.writeHealDefence(18);
						break;
					case 8:
						os.writePotionrecovery(20, 20);
						os.writeHealDefence(20);
						break;
					default:
						break;
					}
				}
				/** 축복받은 룸티스의 푸른빛 귀걸이 물약효율 표시 **/
				if (itemType2 == 2 && itemId == 222338 && getEnchantLevel() >= 0) {
					int lvl = getEnchantLevel();
					switch (lvl) {
					case 0:
						os.writePotionrecovery(2, 2);
						os.writeHealDefence(2);
						break;
					case 1:
						os.writePotionrecovery(6, 6);
						os.writeHealDefence(6);
						break;
					case 2:
						os.writePotionrecovery(8, 8);
						os.writeHealDefence(8);
						break;
					case 3:
						os.writePotionrecovery(12, 12);
						os.writeHealDefence(12);
						break;
					case 4:
						os.writePotionrecovery(14, 14);
						os.writeHealDefence(14);
						break;
					case 5:
						os.writePotionrecovery(16, 16);
						os.writeHealDefence(16);
						break;
					case 6:
						os.writePotionrecovery(18, 18);
						os.writeHealDefence(18);
						break;
					case 7:
						os.writePotionrecovery(20, 20);
						os.writeHealDefence(20);
						break;
					case 8:
						os.writePotionrecovery(22, 22);
						os.writeHealDefence(22);
						break;
					default:
						break;
					}
				}
				/** 장신구 목걸이 5이상 물약 회복량, 회복 악화 방어(공포) **/
				if (itemType2 == 2 && getItem().getGrade() >= 0 && getItem().getGrade() <= 2 && getItem().get장신구처리() == 8){
					if (getEnchantLevel() == 5) {
						int op = (getEnchantLevel() - 4) * 3;
						int op2 = (getEnchantLevel() - 5) * 3;
						os.writeC(65); // 물약 회복량 표기
						os.writeC(op); // 퍼센트
						os.writeC(op2); // +추가량
						os.writeC(96); // 회복 악화 방어
						os.writeC(op); // 퍼센트
						os.writeC(15);
						os.writeH(getMr() + 1);
					} else if (getEnchantLevel() == 6) {
						int op = (getEnchantLevel() - 5) * 5;
						int op2 = (getEnchantLevel() - 5) * 3;
						os.writeC(65); // 물약 회복량 표기
						os.writeC(op); // 퍼센트
						os.writeC(op2); // + 추가량
						os.writeC(96); // 회복 악화 방어
						os.writeC(op); // 퍼센트
						os.writeC(15);
						os.writeH(getMr() + 3);
					} else if (getEnchantLevel() == 7) {
						int op = (getEnchantLevel() - 6) * 7;
						int op2 = (getEnchantLevel() - 6) * 5;
						os.writeC(65); // 물약 회복량 표기
						os.writeC(op); // 퍼센트
						os.writeC(op2); // + 추가량
						os.writeC(96); // 회복 악화 방어
						os.writeC(op); // 퍼센트
						os.writeC(15);
						os.writeH(getMr() + 5);
					} else if (getEnchantLevel() == 8) {
						int op = (getEnchantLevel() - 7) * 9;
						int op2 = (getEnchantLevel() - 7) * 7;
						os.writeC(65); // 물약 회복량 표기
						os.writeC(op); // 퍼센트
						os.writeC(op2); // + 추가량
						os.writeC(96); // 회복 악화 방어
						os.writeC(op); // 퍼센트
						os.writeC(15);
						os.writeH(getMr() + 7);
					} else if (getEnchantLevel() == 9) {
						int op = (getEnchantLevel() - 8) * 10;
						int op2 = (getEnchantLevel() - 8) * 8;
						os.writeC(65); // 물약 회복량 표기
						os.writeC(op); // 퍼센트
						os.writeC(op2); // + 추가량
						os.writeC(96); // 회복 악화 방어
						os.writeC(op); // 퍼센트
						os.writeC(15);
						os.writeH(getMr() + 10);
					}
				}
/////////////////////////귀걸이 업데이트
				if (getItem().getType2() == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& getEnchantLevel() > 4) {
					if (getItem().getType() == 12) { // 귀걸이
						int enchantPotion = (getEnchantLevel() - 4) * 2;
						if (enchantPotion >= 9) {
							enchantPotion = 9;
						}
						os.writeC(65);
						os.writeC(enchantPotion);
						os.writeC(getEnchantLevel() == 6 ? 2 : getEnchantLevel() == 7 ? 4 : getEnchantLevel() == 8 ? 6 : getEnchantLevel() >= 9 ? 7 : 0);
						os.writeC(96);
						os.writeC(enchantPotion);
					}
				}
				
				if (itemType2 == 2 && itemId == 222340 || itemId == 222341) {
					if (getEnchantLevel() == 3) {
						if (getItemId() == 222341) {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 2);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 2);
						} else {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 2);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 2);
						}
					} else if (getEnchantLevel() == 4) {
						if (getItemId() == 222341) {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 2);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 2);
							os.writeAddDmgPer(2, 20);
						} else {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 3);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 3);
						}
					} else if (getEnchantLevel() == 5) {
						if (getItemId() == 222341) {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 2);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 2);
							os.writeAddDmgPer(3, 20);
						} else {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 3);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 3);
							os.writeAddDmgPer(2, 20);
						}
					} else if (getEnchantLevel() == 6) {
						if (getItemId() == 222341) {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 2);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 2);
							os.writeAddDmgPer(4, 20);
						} else {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 3);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 3);
							os.writeAddDmgPer(3, 20);
						}
					} else if (getEnchantLevel() == 7) {
						if (getItemId() == 222341) {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 2);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 2);
							os.writeAddDmgPer(5, 20);
						} else {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 3);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 3);
							os.writeAddDmgPer(4, 20);
						}
					} else if (getEnchantLevel() == 8) {
						if (getItemId() == 222341) {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 2);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 2);
							os.writeAddDmgPer(6, 20);
						} else {
							os.writeC(6);
							os.writeC(getEnchantLevel() - 3);
							os.writeC(35);
							os.writeC(getEnchantLevel() - 3);
							os.writeAddDmgPer(5, 20);
						}
					}
				}

				/** 축복받은 스냅퍼의 마법 저항 반지 MR표시 **/
				if (itemType2 == 2 && itemId == 222334 && getEnchantLevel() > 5) {
					os.writeC(15);
					os.writeH(getMr() + (getEnchantLevel() - 5));
					/** 룸티스의 보랏빛 귀걸이 마방 표시 **/
				} else if (itemType2 == 2 && itemId == 22231 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(15);
						os.writeH(getMr() + 3);
						break;
					case 2:
						os.writeC(15);
						os.writeH(getMr() + 4);
						break;
					case 3:
						os.writeC(15);
						os.writeH(getMr() + 5);
						break;
					case 4:
						os.writeC(15);
						os.writeH(getMr() + 6);
						break;
					case 5:
						os.writeC(15);
						os.writeH(getMr() + 7);
						break;
					case 6:
						os.writeC(15);
						os.writeH(getMr() + 8);
						break;
					case 7:
						os.writeC(15);
						os.writeH(getMr() + 10);
						break;
					case 8:
						os.writeC(15);
						os.writeH(getMr() + 13);
						break;
					default:
						break;
					}
					/** 축복받은 룸티스의 보랏빛 귀걸이 마방 표시 **/
				} else if (itemType2 == 2 && itemId == 222339 && getEnchantLevel() > 0) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeC(15);
						os.writeH(getMr() + 3);
						break;
					case 2:
						os.writeC(15);
						os.writeH(getMr() + 4);
						break;
					case 3:
						os.writeC(15);
						os.writeH(getMr() + 6);
						break;
					case 4:
						os.writeC(15);
						os.writeH(getMr() + 7);
						break;
					case 5:
						os.writeC(15);
						os.writeH(getMr() + 8);
						break;
					case 6:
						os.writeC(15);
						os.writeH(getMr() + 10);
						break;
					case 7:
						os.writeC(15);
						os.writeH(getMr() + 13);
						break;
					case 8:
						os.writeC(15);
						os.writeH(getMr() + 18);
						break;
					default:
						break;
					}
				} else if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& (getItem().get장신구처리() == 9 || getItem().get장신구처리() == 11) && getEnchantLevel() >= 6) {
					int enchantMr = 0;
					switch (getEnchantLevel()) {
					case 6:
						enchantMr = 1;
						break;
					case 7:
						enchantMr = 3;
						break;
					case 8:
						enchantMr = 5;
						break;
					case 9:
						enchantMr = 7;
						break;
					}
					if (getEnchantLevel() >= 9) {
						enchantMr = 7;
					}
					os.writeC(15);
					os.writeH(getMr() + enchantMr);
				} else if (getMr() != 0) { // MR
					os.writeC(15);
					os.writeH(getMr());
				}
				/** 스냅퍼의 지혜 반지 SP표시 **/
				if (itemType2 == 2 && itemId == 222290 && getEnchantLevel() > 4) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 6:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break;
					case 7:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 3);
						break;
					case 8:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 4);
						break;
					default:
						break;
					}
					/** 축복받은 스냅퍼의 지혜 반지 SP표시 **/
				} else if (itemType2 == 2 && itemId == 222335 && getEnchantLevel() > 3) {
					switch (getEnchantLevel()) {
					case 4:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 5:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break; 
					case 6:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 3);
						break;
					case 7:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 4);
						break;
					case 8:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 5);
						break;
					default:
						break;
					}
					/** 룸티스의 보랏빛 귀걸이 SP표시 **/
				} else if (itemType2 == 2 && itemId == 22231 && getEnchantLevel() > 2) {
					switch (getEnchantLevel()) {
					case 3:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 4:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 5:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break;
					case 6:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break;
					case 7:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 3);
						break;
					case 8:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 3);
						break;
					default:
						break;
					}
					/** 축복받은 룸티스의 보랏빛 귀걸이 SP표시 **/
				} else if (itemType2 == 2 && itemId == 222339 && getEnchantLevel() > 2) {
					switch (getEnchantLevel()) {
					case 3:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 4:
					case 5:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break;
					case 6:
					case 7:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 3);
						break;
					case 8:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 4);
						break;
					default:
						break;
					}
					/** 화령의 가더 **/
				} else if (itemId == 900117) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeAddDMG(1);
						break;
					case 5:
						os.writeAddDMG(1);
						os.writeShortHIT(1);
						break;
					case 6:
						os.writeAddDMG(1);
						os.writeShortHIT(2);
						os.writeaAll_resis(1);
						break;
					case 7:
						os.writeAddDMG(2);
						os.writeShortHIT(3);
						os.writeaAll_resis(2);
						break;
					case 8:
						os.writeAddDMG(3);
						os.writeShortHIT(4);
						os.writeaAll_resis(3);
						break;
					case 9:
						os.writeAddDMG(4);
						os.writeShortHIT(5);
						os.writeaAll_resis(4);
						break;
					case 10:
						os.writeAddDMG(5);
						os.writeShortHIT(6);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
					/** 풍령의 가더 **/
				} else if (itemId == 900118) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeLongDMG(1);
						break;
					case 5:
						os.writeLongDMG(1);
						os.writeLongHIT(1);
						break;
					case 6:
						os.writeLongDMG(1);
						os.writeLongHIT(2);
						os.writeaAll_resis(1);
						break;
					case 7:
						os.writeLongDMG(2);
						os.writeLongHIT(3);
						os.writeaAll_resis(2);
						break;
					case 8:
						os.writeLongDMG(3);
						os.writeLongHIT(4);
						os.writeaAll_resis(3);
						break;
					case 9:
						os.writeLongDMG(4);
						os.writeLongHIT(5);
						os.writeaAll_resis(4);
						break;
					case 10:
						os.writeLongDMG(5);
						os.writeLongHIT(6);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
					/** 수령의 가더 **/
				} else if (itemId == 900119) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeAddSP(1);
						break;
					case 5:
						os.writeAddSP(1);
						os.writeMagicHIT(1);
						break;
					case 6:
						os.writeAddSP(1);
						os.writeMagicHIT(2);
						os.writeaAll_resis(1);
						break;
					case 7:
						os.writeAddSP(2);
						os.writeMagicHIT(3);
						os.writeaAll_resis(2);
						break;
					case 8:
						os.writeAddSP(3);
						os.writeMagicHIT(4);
						os.writeaAll_resis(3);
						break;
					case 9:
						os.writeAddSP(4);
						os.writeMagicHIT(5);
						os.writeaAll_resis(4);
						break;
					case 10:
						os.writeAddSP(5);
						os.writeMagicHIT(6);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
					/** 지령의 가더 **/
				} else if (itemId == 900120) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeDMGdown(1);
						break;
					case 5:
						os.writeDMGdown(1);
						os.writeAddMR(2);
						break;
					case 6:
						os.writeDMGdown(1);
						os.writeAddMR(3);
						break;
					case 7:
						os.writeDMGdown(2);
						os.writeAddMR(4);
						break;
					case 8:
						os.writeDMGdown(3);
						os.writeAddMR(5);
						break;
					case 9:
						os.writeDMGdown(4);
						os.writeAddMR(6);
						break;
					case 10:
						os.writeDMGdown(5);
						os.writeAddMR(7);
						break;
					default:
						break;
					}
					/** 마법사의 가더 SP표시 **/
				} else if (itemType2 == 2 && itemId == 22255 && getEnchantLevel() > 4) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 6:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 7:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break;
					case 8:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break;
					case 9:
					case 10:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 3);
						break;
					default:
						break;
					}
					/** 바포메트의 지팡이 SP표시 **/
				} else if (itemType2 == 1 && itemId == 124 && getEnchantLevel() > 6) {
					switch (getEnchantLevel()) {
					case 7:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 1);
						break;
					case 8:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 2);
						break;
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeC(17);
						os.writeC(getItem().get_addsp() + 3);
						break;
					default:
						break;
					}
					/** 리치 로브 SP표시 **/
				} else if (itemType2 == 2 && itemId == 20107 && getEnchantLevel() >= 3) {
					os.writeC(17);
					os.writeC(getItem().get_addsp() + getEnchantLevel() - 2);
				} else if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& (getItem().get장신구처리() == 9 || getItem().get장신구처리() == 11) && getEnchantLevel() >= 7) {
					int enchantSp = getEnchantLevel() - 6;
					if (enchantSp >= 3) {
						enchantSp = 3;
					}
					os.writeC(17);
					os.writeC(addsp() + enchantSp);
					
				}else {
					if (addsp() != 0) { // sp
						os.writeAddSP(addsp());
					}
				}

				// 마나 흡수
				if (itemId == 126 || itemId == 127) {
					os.writeC(16);
				}

				// 피 흡수
				if (itemId == 12 || itemId == 601 || itemId == 1123 || itemId == 202013) {
					os.writeC(34);
				}

				/** 벨트 5이상 대미지 리덕션 **/
				if (itemType2 == 2 && (getItem().getType() == 10 && getEnchantLevel() >= 5)) {// 벨트
					os.writeDMGdown(getEnchantLevel() - 4);
				}
				/** 룸티스의 붉은빛 귀걸이 대미지 리덕션 **/
				if (getItemId() == 22229 && getEnchantLevel() > 2) {
					switch (getEnchantLevel()) {
					case 3:
					case 4:
						os.writeDMGdown(getItem().get_damage_reduction() + 1);
						break;
					case 5:
						os.writeDMGdownprobability(2, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 2);
						break;
					case 6:
						os.writeDMGdownprobability(3, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 3);
						break;
					case 7:
						os.writeDMGdownprobability(4, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 4);
						os.writeShortHIT(1);
						os.writeLongHIT(1);
						break;
					case 8:
						os.writeDMGdownprobability(5, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 5);
						os.writeShortHIT(3);
						os.writeLongHIT(3);
						break;
					default:
						break;
					}
				}
				if (getItemId() == 22228) {
					switch (getEnchantLevel()) {
					case 6:
						os.writeability_resis(5);
						break;
					case 7:
						os.writeC(39);
						os.writeS("\\fI확률 마법 회피: \\aA+1");
					//	os.writeability_resis(7);
						break;
					case 8:
						os.writeC(39);
						os.writeS("\\fI확률 마법 회피: \\aA+3");
					//	os.writeability_resis(9);
						break;
					default:
						break;
					}
				}
				if (getItemId() == 222334) {
					switch (getEnchantLevel()) {
					case 6:
						os.writeC(39);
						os.writeS("\\fI확률 마법 회피: \\aA+1");
					//	os.writeability_resis(5);
						break;
					case 7:
						os.writeC(39);
						os.writeS("\\fI확률 마법 회피: \\aA+3");
					//	os.writeability_resis(7);
						break;
					case 8:
						os.writeC(39);
						os.writeS("\\fI확률 마법 회피: \\aA+5");
					//	os.writeability_resis(9);
						break;
					default:
						break;
					}
				}
				/** 축복받은 룸티스의 붉은빛 귀걸이 대미지 리덕션 **/
				if (itemId == 222337 && getEnchantLevel() > 2) {
					switch (getEnchantLevel()) {
					case 3:
						os.writeDMGdown(getItem().get_damage_reduction() + 1);
						break;
					case 4:
						os.writeDMGdownprobability(2, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 2);
						break;
					case 5:
						os.writeDMGdownprobability(3, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 3);
						break;
					case 6:
						os.writeDMGdownprobability(4, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 4);
						os.writeShortHIT(1);
						os.writeLongHIT(1);
						break;
					case 7:
						os.writeDMGdownprobability(5, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 5);
						os.writeShortHIT(3);
						os.writeLongHIT(3);
						break;
					case 8:
						os.writeDMGdownprobability(6, 20);
						os.writeDMGdown(getItem().get_damage_reduction() + 6);
						os.writeShortHIT(5);
						os.writeLongHIT(5);
						break;
					default:
						break;
					}
				}
				
	
					
				/** 실프의티셔츠 **/
				if (itemId == 900019) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeDMGdown(1);
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeAddSP(1);
						break;
					case 5:
						os.writeDMGdown(1);
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeAddSP(1);
						os.writeAddMR(4);
						os.writeability_resis(8);
						break;
					case 6:
						os.writeDMGdown(1);
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeAddSP(1);
						os.writeAddMR(5);
						os.writeability_resis(9);
						break;
					case 7:
						os.writeDMGdown(1);
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeAddSP(1);
						os.writeShortHIT(1);
						os.writeLongHIT(1);
						os.writeAddMR(6);
						os.writeability_resis(10);
						os.writeaAll_resis(1);
						break;
					case 8:
						os.writeDMGdown(1);
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeAddSP(1);
						os.writeShortHIT(3);
						os.writeLongHIT(3);
						os.writeMagicHIT(2);// 마법 적중
						os.writeAddMR(8);
						os.writeability_resis(12);
						os.writeaAll_resis(2);
						break;
					case 9:
						os.writeDMGdown(2);
						os.writeShortDMG(2);
						os.writeLongDMG(2);
						os.writeAddSP(2);
						os.writeShortHIT(5);
						os.writeLongHIT(5);
						os.writeMagicHIT(4);// 마법 적중
						os.writeAddMR(11);
						os.writeability_resis(15);
						os.writeaAll_resis(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeDMGdown(3);
						os.writeShortDMG(3);
						os.writeLongDMG(3);
						os.writeAddSP(3);
						os.writeShortHIT(7);
						os.writeLongHIT(7);
						os.writeMagicHIT(5);// 마법 적중
						os.writeAddMR(14);
						os.writeability_resis(17);
						os.writeaAll_resis(6);
						os.writeAddMaxHP(200);
						os.writeAddEXP(8);		
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}
				/** 수호의 가더 대미지 리덕션 **/
				if (itemId == 22254) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeDMGdown(1);
						break;
					case 5:
					case 6:
						os.writeDMGdown(2);
						break;
					case 7:
					case 8:
						os.writeDMGdown(3);
						break;
					case 9:
					case 10:
						os.writeDMGdown(4);
						break;
					default:
						break;
					}
				}
				/** 유니콘의 완력 각반 **/
				if (itemId == 900030) {
					if (getEnchantLevel() >= 9)
						os.writeShortDMG(1);
				}
				/** 유니콘의 민첩 각반 **/
				if (itemId == 900031) {
					if (getEnchantLevel() >= 9)
						os.writeLongDMG(1);
				}
				/** 유니콘의 지식 각반 **/
				if (itemId == 900032) {
					if (getEnchantLevel() >= 9)
						os.writeAddSP(1);
				}
				/** 나이트발드의 양손검 스턴레벨+1 **/ 
				if (itemId == 59 || itemId == 617) {
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 15)
						os.writeability_pierce(5);
				}
			
				// TODO 투사의 문장
				if (itemId == 900127) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 4:
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						os.writeShortHIT(1);
						break;
					case 5:
						os.writeShortDMG(1);
						os.writeShortHIT(1);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 6:
						os.writeShortDMG(2);
						os.writeShortHIT(2);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					case 7:
						os.writeShortDMG(3);
						os.writeShortHIT(3);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						break;
					case 8:
						os.writeShortDMG(4);
						os.writeShortHIT(4);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
					default:
						break;
					}
				}
				// TODO 명궁의 문장
				if (itemId == 900128) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 4:
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						os.writeLongHIT(1);
						break;
					case 5:
						os.writeLongDMG(1);
						os.writeLongHIT(1);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 6:
						os.writeLongDMG(2);
						os.writeLongHIT(2);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					case 7:
						os.writeLongDMG(3);
						os.writeLongHIT(3);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						break;
					case 8:
						os.writeLongDMG(4);
						os.writeLongHIT(4);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						break;
					default:
						break;
					}
				}
				// TODO 현자의 문장
				if (itemId == 900129) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 4:
						os.writeMagicHIT(1);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 5:
						os.writeAddSP(1);
						os.writeMagicHIT(1);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 6:
						os.writeAddSP(2);
						os.writeMagicHIT(2);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					case 7:
						os.writeAddSP(3);
						os.writeMagicHIT(3);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						break;
					case 8:
						os.writeAddSP(4);
						os.writeMagicHIT(4);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						break;
					default:
						break;
					}
				}
				// TODO 수호의 문장
				if (itemId == 900130) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeAddEXP(1);
						break;
					case 2:
						os.writeAddEXP(2);
						break;
					case 3:
						os.writeAddEXP(3);
						break;
					case 4:
						os.writeAddMR(1);
						os.writeAddEXP(4);
						break;
					case 5:
						os.writeAddMR(2);
						os.writeAddEXP(5);
						break;
					case 6:
						os.writeAddMR(3);
						os.writeAddEXP(6);
						break;
					case 7:
						os.writeAddMR(4);
						os.writeAddEXP(7);
						break;
					case 8:
						os.writeAddMR(5);
						os.writeAddEXP(8);
						break;
					default:
						break;
					}
				}
				if (itemId == 910322) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddEXP(10);
						break;
					default:
						break;
					}
				}
				if (itemId == 910323) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddEXP(10);
						break;
					default:
						break;
					}
				}
				if (itemId == 910324) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddEXP(10);
						break;
					default:
						break;
					}
				}
				// TODO 수호의 투사 문장
				if (itemId == 900124) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeShortDMG(1);
						os.writeShortHIT(2);
						os.writeAddMR(4);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						os.writeaBlesssomo(5);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(1);
						break;
					case 6:
						os.writeShortDMG(2);
						os.writeShortHIT(3);
						os.writeAddMR(6);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						os.writeaBlesssomo(10);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(2);
						break;
					case 7:
						os.writeShortDMG(3);
						os.writeShortHIT(4);
						os.writeAddMR(8);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						os.writeaBlesssomo(15);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(3);
						break;
					case 8:
						os.writeShortDMG(4);
						os.writeShortHIT(5);
						os.writeAddMR(10);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						os.writeaBlesssomo(20);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(5);
						break;
					default:
						break;
					}
				}
				// TODO 수호의 명궁 문장
				if (itemId == 900125) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeLongDMG(1);
						os.writeLongHIT(2);
						os.writeAddMR(4);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						os.writeaBlesssomo(5);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(1);
						break;
					case 6:
						os.writeLongDMG(2);
						os.writeLongHIT(3);
						os.writeAddMR(6);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						os.writeaBlesssomo(10);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(2);
						break;
					case 7:
						os.writeLongDMG(3);
						os.writeLongHIT(4);
						os.writeAddMR(8);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						os.writeaBlesssomo(15);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(3);
						break;
					case 8:
						os.writeLongDMG(4);
						os.writeLongHIT(5);
						os.writeAddMR(10);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						os.writeaBlesssomo(20);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(5);
						break;
					default:
						break;
					}
				}
				// TODO 수호의 현자 문장
				if (itemId == 900126) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeAddSP(1);
						os.writeMagicHIT(2);
						os.writeAddMR(4);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						os.writeaBlesssomo(5);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(1);
						break;
					case 6:
						os.writeAddSP(2);
						os.writeMagicHIT(3);
						os.writeAddMR(6);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						os.writeaBlesssomo(10);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(2);
						break;
					case 7:
						os.writeAddSP(3); //sp 
						os.writeMagicHIT(4);  //마법적중 
						os.writeAddMR(8);   //mr 
						os.writeHealDefence(16); //물약 회복
						os.writePotionrecovery(16, 16); //회복 악화방어 
						os.writeaBlesssomo(15);  //추복소모효율 
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(15);  //pvp 마법 데미지 감소 
						break;
					case 8:
						os.writeAddSP(4);
						os.writeMagicHIT(5);
						os.writeAddMR(10);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						os.writeaBlesssomo(20);
						os.writeDMGdown(1);
						os.writePVPMagicDamageReduction(5);
						break;
					default:
						break;
					}
				}
				
			
				/** 회복의 문장 **/
				if (itemId == 900021) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 1:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 2:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 4:
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 5:
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 6:
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					case 7:
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						break;
					case 8:
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						break;
					case 9:
						os.writeHealDefence(20);
						os.writePotionrecovery(20, 20);
						break;
					case 10:
						os.writeHealDefence(22);
						os.writePotionrecovery(22, 22);
						break;
					default:
						break;
					}
				}
				/** 성장의 문장 **/
				if (itemId == 900020) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddEXP(getItem().get_addexp1() + 1);
						break;
					case 1:
						os.writeAddEXP(getItem().get_addexp1() + 2);
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeAddEXP(getItem().get_addexp1() + 3);
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeAddEXP(getItem().get_addexp1() + 4);
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						break;
					case 4:
						os.writeAddEXP(getItem().get_addexp1() + 5);
						os.writeHealDefence(2);
						os.writePotionrecovery(8, 8);
						break;
					case 5:
						os.writeAddEXP(getItem().get_addexp1() + 6);
						os.writeHealDefence(9);
						os.writePotionrecovery(9, 9);
						break;
					case 6:
						os.writeAddEXP(getItem().get_addexp1() + 7);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 7:
						os.writeAddEXP(getItem().get_addexp1() + 9);
						os.writeHealDefence(11);
						os.writePotionrecovery(11, 11);
						break;
					case 8:
						os.writeAddEXP(getItem().get_addexp1() + 11);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 9:
						os.writeAddEXP(getItem().get_addexp1() + 13);
						os.writeHealDefence(13);
						os.writePotionrecovery(13, 13);
						break;
					case 10:
						os.writeAddEXP(getItem().get_addexp1() + 15);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					default:
						break;
					}
				}
				/** 완력의 문장 **/
				if (itemId == 900049) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						break;
					case 4:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 5:
						os.writeShortHIT(1);
						os.writeHealDefence(9);
						os.writePotionrecovery(9, 9);
						break;
					case 6:
						os.writeShortHIT(1);
						os.writeShortDMG(1);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 7:
						os.writeShortHIT(2);
						os.writeShortDMG(2);
						os.writeHealDefence(11);
						os.writePotionrecovery(11, 11);
						break;
					case 8:
						os.writeShortHIT(3);
						os.writeShortDMG(3);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 9:
						os.writeShortHIT(4);
						os.writeShortDMG(4);
						os.writeHealDefence(13);
						os.writePotionrecovery(13, 13);
						break;
					case 10:
						os.writeShortHIT(5);
						os.writeShortDMG(5);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					default:
						break;
					}
				}
				/** 민첩의 문장 **/
				if (itemId == 900050) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						break;
					case 4:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 5:
						os.writeLongDMG(1);
						os.writeHealDefence(9);
						os.writePotionrecovery(9, 9);
						break;
					case 6:
						os.writeLongDMG(1);
						os.writeLongHIT(1);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 7:
						os.writeLongDMG(2);
						os.writeLongHIT(2);
						os.writeHealDefence(11);
						os.writePotionrecovery(11, 11);
						break;
					case 8:
						os.writeLongDMG(3);
						os.writeLongHIT(3);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 9:
						os.writeLongDMG(4);
						os.writeLongHIT(4);
						os.writeHealDefence(13);
						os.writePotionrecovery(13, 13);
						break;
					case 10:
						os.writeLongDMG(5);
						os.writeLongHIT(5);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					default:
						break;
					}
				}
				/** 지식의 문장 **/
				if (itemId == 900051) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						break;
					case 4:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 5:
						os.writeMagicHIT(1);
						os.writeHealDefence(9);
						os.writePotionrecovery(9, 9);
						break;
					case 6:
						os.writeMagicHIT(1);
						os.writeAddSP(1);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 7:
						os.writeMagicHIT(2);
						os.writeAddSP(2);
						os.writeHealDefence(11);
						os.writePotionrecovery(11, 11);
						break;
					case 8:
						os.writeMagicHIT(3);
						os.writeAddSP(3);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 9:
						os.writeMagicHIT(4);
						os.writeAddSP(4);
						os.writeHealDefence(13);
						os.writePotionrecovery(13, 13);
						break;
					case 10:
						os.writeMagicHIT(5);
						os.writeAddSP(5);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					default:
						break;
					}
				}
				// TODO 완력의 문장:성장
				if (itemId == 900093) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						os.writeAddEXP(4);
						break;
					case 4:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						os.writeAddEXP(5);
						break;
					case 5:
						os.writeShortHIT(1);
						os.writeHealDefence(9);
						os.writePotionrecovery(9, 9);
						os.writeAddEXP(6);
						break;
					case 6:
						os.writeShortHIT(1);
						os.writeShortDMG(1);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						os.writeAddEXP(7);
						break;
					case 7:
						os.writeShortHIT(2);
						os.writeShortDMG(2);
						os.writeHealDefence(11);
						os.writePotionrecovery(11, 11);
						os.writeAddEXP(9);
						break;
					case 8:
						os.writeShortHIT(3);
						os.writeShortDMG(3);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						os.writeAddEXP(11);
						break;
					case 9:
						os.writeShortHIT(4);
						os.writeShortDMG(4);
						os.writeHealDefence(13);
						os.writePotionrecovery(13, 13);
						os.writeAddEXP(13);
						break;
					case 10:
						os.writeShortHIT(5);
						os.writeShortDMG(5);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						os.writeAddEXP(15);
						break;
					default:
						break;
					}
				}
				// TODO 민첩의 문장:성장
				if (itemId == 900094) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						os.writeAddEXP(4);
						break;
					case 4:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						os.writeAddEXP(5);
						break;
					case 5:
						os.writeLongHIT(1);
						os.writeHealDefence(9);
						os.writePotionrecovery(9, 9);
						os.writeAddEXP(6);
						break;
					case 6:
						os.writeLongHIT(1);
						os.writeLongDMG(1);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						os.writeAddEXP(7);
						break;
					case 7:
						os.writeLongHIT(2);
						os.writeLongDMG(2);
						os.writeHealDefence(11);
						os.writePotionrecovery(11, 11);
						os.writeAddEXP(9);
						break;
					case 8:
						os.writeLongHIT(3);
						os.writeLongDMG(3);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						os.writeAddEXP(11);
						break;
					case 9:
						os.writeLongHIT(4);
						os.writeLongDMG(4);
						os.writeHealDefence(13);
						os.writePotionrecovery(13, 13);
						os.writeAddEXP(13);
						break;
					case 10:
						os.writeLongHIT(5);
						os.writeLongDMG(5);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						os.writeAddEXP(15);
						break;
					default:
						break;
					}
				}
				// TODO 지식의 문장:성장
				if (itemId == 900095) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						os.writeAddEXP(4);
						break;
					case 4:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						os.writeAddEXP(5);
						break;
					case 5:
						os.writeMagicHIT(1);
						os.writeHealDefence(9);
						os.writePotionrecovery(9, 9);
						os.writeAddEXP(6);
						break;
					case 6:
						os.writeMagicHIT(1);
						os.writeAddSP(1);
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						os.writeAddEXP(7);
						break;
					case 7:
						os.writeMagicHIT(2);
						os.writeAddSP(2);
						os.writeHealDefence(11);
						os.writePotionrecovery(11, 11);
						os.writeAddEXP(9);
						break;
					case 8:
						os.writeMagicHIT(3);
						os.writeAddSP(3);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						os.writeAddEXP(11);
						break;
					case 9:
						os.writeMagicHIT(4);
						os.writeAddSP(4);
						os.writeHealDefence(13);
						os.writePotionrecovery(13, 13);
						os.writeAddEXP(13);
						break;
					case 10:
						os.writeMagicHIT(5);
						os.writeAddSP(5);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						os.writeAddEXP(15);
						break;
					default:
						break;
					}
				}
				// TODO 완력의 문장:회복
				if (itemId == 900096) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 4:
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 5:
						os.writeShortHIT(1);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 6:
						os.writeShortHIT(1);
						os.writeShortDMG(1);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					case 7:
						os.writeShortHIT(2);
						os.writeShortDMG(2);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						break;
					case 8:
						os.writeShortHIT(3);
						os.writeShortDMG(3);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						break;
					case 9:
						os.writeShortHIT(4);
						os.writeShortDMG(4);
						os.writeHealDefence(20);
						os.writePotionrecovery(20, 20);
						break;
					case 10:
						os.writeShortHIT(5);
						os.writeShortDMG(5);
						os.writeHealDefence(22);
						os.writePotionrecovery(22, 22);
						break;
					default:
						break;
					}
				}
				// TODO 민첩의 문장:회복
				if (itemId == 900097) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 4:
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 5:
						os.writeLongHIT(1);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 6:
						os.writeLongHIT(1);
						os.writeLongDMG(1);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					case 7:
						os.writeLongHIT(2);
						os.writeLongDMG(2);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						break;
					case 8:
						os.writeLongHIT(3);
						os.writeLongDMG(3);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						break;
					case 9:
						os.writeLongHIT(4);
						os.writeLongDMG(4);
						os.writeHealDefence(20);
						os.writePotionrecovery(20, 20);
						break;
					case 10:
						os.writeLongHIT(5);
						os.writeLongDMG(5);
						os.writeHealDefence(22);
						os.writePotionrecovery(22, 22);
						break;
					default:
						break;
					}
				}
				// TODO 지식의 문장:회복
				if (itemId == 900098) {
					switch (getEnchantLevel()) {
					case 1:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 2:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						break;
					case 4:
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						break;
					case 5:
						os.writeMagicHIT(1);
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						break;
					case 6:
						os.writeMagicHIT(1);
						os.writeAddSP(1);
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						break;
					case 7:
						os.writeMagicHIT(2);
						os.writeAddSP(2);
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						break;
					case 8:
						os.writeMagicHIT(3);
						os.writeAddSP(3);
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						break;
					case 9:
						os.writeMagicHIT(4);
						os.writeAddSP(4);
						os.writeHealDefence(20);
						os.writePotionrecovery(20, 20);
						break;
					case 10:
						os.writeMagicHIT(5);
						os.writeAddSP(5);
						os.writeHealDefence(22);
						os.writePotionrecovery(22, 22);
						break;
					default:
						break;
					}
				}
				// TODO 회복의 문장:성장
				if (itemId == 900099) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeHealDefence(2);
						os.writePotionrecovery(2, 2);
						break;
					case 1:
						os.writeHealDefence(4);
						os.writePotionrecovery(4, 4);
						break;
					case 2:
						os.writeHealDefence(6);
						os.writePotionrecovery(6, 6);
						break;
					case 3:
						os.writeHealDefence(8);
						os.writePotionrecovery(8, 8);
						os.writeAddEXP(4);
						break;
					case 4:
						os.writeHealDefence(10);
						os.writePotionrecovery(10, 10);
						os.writeAddEXP(5);
						break;
					case 5:
						os.writeHealDefence(12);
						os.writePotionrecovery(12, 12);
						os.writeAddEXP(6);
						break;
					case 6:
						os.writeHealDefence(14);
						os.writePotionrecovery(14, 14);
						os.writeAddEXP(7);
						break;
					case 7:
						os.writeHealDefence(16);
						os.writePotionrecovery(16, 16);
						os.writeAddEXP(9);
						break;
					case 8:
						os.writeHealDefence(18);
						os.writePotionrecovery(18, 18);
						os.writeAddEXP(11);
						break;
					case 9:
						os.writeHealDefence(20);
						os.writePotionrecovery(20, 20);
						os.writeAddEXP(13);
						break;
					case 10:
						os.writeHealDefence(22);
						os.writePotionrecovery(22, 22);
						os.writeAddEXP(15);
						break;
					default:
						break;
					}
				}
				/** 아르카의 유물 **/
				if (itemId == 900033 || itemId == 9961) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
						os.writeAddEXP(2);
						break;
					default:
						break;
					}
				}
				if (itemId == 900024) {
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 10)
						os.writeMagicHIT(3);
				}
				if (itemId == 900114) {
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 10)
						os.writeMagicHIT(2);
				}
/*				if (itemId == 541) { // 지배자의 절대검
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 10)
						os.writeAddStunHit(7);
				}
				if (itemId == 542) { // 지배자의 척살궁
					switch (getEnchantLevel()) {
					case 0:
						os.writeDMGdown(2); // 데미지 감소
						os.writeReductiondown(9); // 데미지리덕션무시
				//		os.writeLongCritical(1); // 원거리 치명타
						break;
					default:
						break;
					}
				}
				if (itemId == 543) { // 지배자의 지팡이
					switch (getEnchantLevel()) {
					case 0:
			//			os.writeShortDMG(2);
						break;
					default:
						break;
					}
				}
				if (itemId == 544) { // 지배자의 파멸도
					switch (getEnchantLevel()) {
					case 0:
					//	os.writeShortDMG(10);
						os.writeaspirit_pierce(2); // 정령적중
						break;
					default:
						break;
					}
				}
				if (itemId == 545) { // 지배자의 절풍검
					switch (getEnchantLevel()) {
					case 0:
			//			os.writeShortDMG(17);
						break;
					default:
						break;
					}
				}
				
				if (itemId == 546) { // 지배자의 키링크
					switch (getEnchantLevel()) {
					case 0:
						os.writeMagicCritical(1); // 마법 치명타
						os.writeadragonS_pierce(7); //용언 적중	
						break;
					default:
						break;
					}
				}
				if (itemId == 547) { // 지배자의 섬광도
					switch (getEnchantLevel()) {
					case 0:
						os.writeafear_pierce(2); // 공포 적중
						os.writeaTitan(5); // 타이탄 발동 구간 +5
						break;
					default:
						break;
					}
				}*/
				/** 축복받은 제로스의 지팡이 **/
				if (itemId == 620) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeMagicCritical(5);
						break;
					default:
						break;
					}
				}
			
				
				if (itemId == 7000223 || itemId == 505015) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						os.writeMagicCritical(1);
						break;
					case 7:
						os.writeadragonS_pierce(1);
						os.writeMagicCritical(2);
						break;
					case 8:
						os.writeadragonS_pierce(2);
						os.writeMagicCritical(3);
						break;
					case 9:
						os.writeadragonS_pierce(3);
						os.writeMagicCritical(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeadragonS_pierce(4);
						os.writeMagicCritical(5);
						break;
					default:
						break;
					}
				}
				/** 태풍의 도끼 공포적중+1 **/
				if (itemId == 203006 || itemId == 616) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						os.writeafear_pierce(1);
						break;
					case 9:
						os.writeafear_pierce(2);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeafear_pierce(3);
						break;
					default:
						break;
					}
				}
				/** 안타라스의 도끼 **/
				if (itemId == 7000016) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeafear_pierce(2);
						break;
					case 8:
						os.writeafear_pierce(3);
						break;
					case 9:
						os.writeafear_pierce(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeafear_pierce(5);
						break;
					default:
						break;
					}
				}
				
				/** 안타라스의 지팡이 **/
				if (itemId == 7000017) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeMagicHIT(1);// 마법 적중
						break;
					case 8:
						os.writeMagicHIT(2);// 마법 적중
						break;
					case 9:
						os.writeMagicHIT(3);// 마법 적중
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeMagicHIT(4);// 마법 적중
						break;
					default:
						break;
					}
				}
				
				/** 파푸리온의 장궁 **/
				if (itemId == 7000018) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						os.writeaspirit_pierce(1); // 정령적중
						os.writeReductiondown(1); // 대미지 리덕션 무시
						break;
					case 9:
						os.writeaspirit_pierce(2); 
						os.writeReductiondown(2); // 대미지 리덕션 무시
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaspirit_pierce(3); 
						os.writeReductiondown(3); // 대미지 리덕션 무시
						break;
					default:
						break;
					}
				}
				
				/** 파푸리온의 이도류 **/
				if (itemId == 7000019) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeaspirit_pierce(2); // 정령적중
						break;
					case 8:
						os.writeaspirit_pierce(3); // 정령적중
						break;
					case 9:
						os.writeaspirit_pierce(4); 
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaspirit_pierce(5); 
						break;
					default:
						break;
					}
				}
				
				/** 린드비오르의 체인소드 **/
				if (itemId == 7000020) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeadragonS_pierce(5); // 용언 적중
						break;
					case 8:
						os.writeadragonS_pierce(6);
						break;
					case 9:
						os.writeadragonS_pierce(7);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeadragonS_pierce(8);
						break;
					default:
						break;
					}
				}
				
				/** 린드비오르의 키링크 **/
				if (itemId == 7000021) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeAddSP(1);
						break;
					case 8:
						os.writeAddSP(2);
						break;
					case 9:
						os.writeAddSP(3);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeAddSP(4);
						break;
					default:
						break;
					}
				}
				
				/** 발라카스의 장검 **/
				if (itemId == 7000022) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeability_pierce(2); // 기술적중
						os.writeaspirit_pierce(2); // 정령적중
						os.writeafear_pierce(2); // 공포적중
						break;
					case 8:
						os.writeability_pierce(3); // 기술적중
						os.writeaspirit_pierce(3); // 정령적중
						os.writeafear_pierce(3); // 공포적중
						break;
					case 9:
						os.writeability_pierce(4); // 기술적중
						os.writeaspirit_pierce(4); // 정령적중
						os.writeafear_pierce(4); // 공포적중
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeability_pierce(5); // 기술적중
						os.writeaspirit_pierce(5); // 정령적중
						os.writeafear_pierce(5); // 공포적중
						break;
					default:
						break;
					}
				}
				/** 발라카스의 양손검 **/
				if (itemId == 7000023) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeability_pierce(5); // 기술적중
						break;
					case 8:
						os.writeability_pierce(6); // 기술적중

						break;
					case 9:
						os.writeability_pierce(7); // 기술적중
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeability_pierce(8); // 기술적중
						break;
					default:
						break;
					}
				}
				
				
				if (itemId == 203017 || itemId == 618) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						os.writeadragonS_pierce(1);
						break;
					case 8:
						os.writeadragonS_pierce(2);
						break;
					case 9:
						os.writeadragonS_pierce(3);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeadragonS_pierce(4);
						break;
					default:
						break;
					}
				}
				/** 썸타는 도끼 공포적중+5 **/
			/*	if (itemId == 7000221) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeafear_pierce(5);
						break;
					default:
						break;
					}
				}*/

				/** 고대의 가호 타입 **/
				if (itemId == 900022) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeC(39);
						os.writeS("\\fI효과:\\aA착용중 사망시");
						os.writeC(39);
						os.writeS("       \\aA패널티없음");
						break;
					default:
						break;
					}
				}
				
			
				
				/** 타이탄의 분노 락구간 5% 상승 **/
				if (itemId == 202014 || itemId == 547 ) { // 타이탄의분노 & 지배자의섬광도
					switch (getEnchantLevel()) {
					case 0:
						os.writeafear_pierce(5);
						break;
					case 1:
						os.writeafear_pierce(6);
						break;
					case 2:
						os.writeafear_pierce(7);
						break;
					case 3:
						os.writeafear_pierce(8);
						break;
					case 4:
						os.writeafear_pierce(9);
						break;
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeafear_pierce(10);
						os.writeaTitan(5);
						break;
					default:
						break;
					}
				}
				/** 썸타는 무기류 PVP 표기 **/
				// L1itemInstance 용량 초과로 쓰지 않는 아이템은 주석처리
//				if (itemId >= 7000214 && itemId <= 7000221) {
//					switch (getEnchantLevel()) {
//					case 0:
//					case 1:
//					case 2:
//					case 3:
//					case 4:
//					case 5:
//					case 6:
//						os.writePVPAddDMG(0);
//						break;
//					case 7:
//						os.writePVPAddDMG(3);
//						break;
//					case 8:
//						os.writePVPAddDMG(5);
//						break;
//					case 9:
//						os.writePVPAddDMG(7);
//						break;
//					case 10:
//					case 11:
//					case 12:
//					case 13:
//					case 14:
//					case 15:
//						os.writePVPAddDMG(10);
//						break;
//					}
//				}

				/** 신성한 엘름의 축복 **/
				if (itemId == 900035 || itemId == 900072 && getEnchantLevel() >= 5) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeC(15);
						os.writeH(getMr() + 4);
						break;
					case 6:
						os.writeC(15);
						os.writeH(getMr() + 8);
						break;
					case 7:
						os.writeC(15);
						os.writeH(getMr() + 12);
						break;
					case 8:
						os.writeC(15);
						os.writeH(getMr() + 16);
						break;
					case 9:
						os.writeC(15);
						os.writeH(getMr() + 20);
						break;
					case 10:
						os.writeC(15);
						os.writeH(getMr() + 24);
						break;
					default:
						break;
					}
				}
				/** 흑기사의 면갑 **/
				if (itemId == 900038 || itemId == 900054 && getEnchantLevel() >= 5) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeC(15);
						os.writeH(getMr() + 4);
						break;
					case 6:
						os.writeC(15);
						os.writeH(getMr() + 8);
						break;
					case 7:
						os.writeC(15);
						os.writeH(getMr() + 12);
						break;
					case 8:
						os.writeC(15);
						os.writeH(getMr() + 16);
						break;
					case 9:
						os.writeC(15);
						os.writeH(getMr() + 20);
						break;
					case 10:
						os.writeC(15);
						os.writeH(getMr() + 24);
						break;
					default:
						break;
					}
				}
				/** 신성한 완력의 목걸이 **/
				if (itemId == 900039) {
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 10)
						os.writeShortHIT(2);
				}
				/** 신성한 민첩의 목걸이 **/
				if (itemId == 900040) {
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 10)
						os.writeLongHIT(2);
				}
				/** 신성한 지식의 목걸이 **/
				if (itemId == 900041) {
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 10)
						os.writeMagicHIT(2);
				}
				/** 수호성의 파워 글로브 **/
				if (itemId == 900036 || itemId == 900073 && getEnchantLevel() >= 5) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeShortHIT(1);
						break;
					case 6:
						os.writeShortHIT(2);
						break;
					case 7:
						os.writeShortHIT(3);
						break;
					case 8:
						os.writeShortHIT(4);
						break;
					case 9:
						os.writeShortHIT(5);
						break;
					case 10:
						os.writeShortHIT(5);
						break;
					default:
						break;
					}
				}
				/** 수호성의 활 골무 **/
				if (itemId == 900037 || itemId == 900074 && getEnchantLevel() >= 5) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeLongHIT(1);
						break;
					case 6:
						os.writeLongHIT(2);
						break;
					case 7:
						os.writeLongHIT(3);
						break;
					case 8:
						os.writeLongHIT(4);
						break;
					case 9:
						os.writeLongHIT(5);
						break;
					case 10:
						os.writeLongHIT(5);
						break;
					default:
						break;
					}
				}
				/** 격분의 장갑 **/
				if (itemId == 222317) {
					switch (getEnchantLevel()) {
					case 7:
						os.writeShortHIT(4);
						os.writeShortDMG(1);
						break;
					case 8:
						os.writeShortHIT(5);
						os.writeShortDMG(2);
						break;
					case 9:
						os.writeShortHIT(6);
						os.writeShortDMG(3);
						break;
					case 10:
						os.writeShortHIT(6);
						os.writeShortDMG(3);
						break;
					default:
						break;
					}
				}
				/** 머미로드의 왕관 **/
				if (itemId == 20017) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeAddMR(3);
						os.writeLongDMG(1);
						break;
					case 6:
						os.writeAddMR(6);
						os.writeLongDMG(2);
						break;
					case 7:
						os.writeAddMR(9);
						os.writeLongDMG(3);
						break;
					case 8:
						os.writeAddMR(12);
						os.writeLongDMG(4);
						break;
					case 9:
						os.writeAddMR(15);
						os.writeLongDMG(5);
						break;
					case 10:
						os.writeAddMR(18);
						os.writeLongDMG(6);
						break;
					default:
						break;
					}
				}
				
				/** 에르자베의 왕관 **/
				if (itemId == 21117) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeability_resis(5);
						os.writeafear_resis(5);
						break;
					case 5:
						os.writeability_resis(6);
						os.writeafear_resis(6);
						break;
					case 6:
						os.writeability_resis(7);
						os.writeafear_resis(7);
						break;
					case 7:
						os.writeability_resis(8);
						os.writeafear_resis(8);
						break;
					case 8:
						os.writeability_resis(9);
						os.writeafear_resis(9);
						break;
					case 9:
						os.writeability_resis(10);
						os.writeafear_resis(10);
						break;
					case 10:
						os.writeability_resis(11);
						os.writeafear_resis(11);
						break;
					default:
						break;
					}
				}
				/** 체력의 각반 **/
				if (itemId == 900023) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(10);
						break;
					case 1:
						os.writeAddMaxHP(15);
						break;
					case 2:
						os.writeAddMaxHP(20);
						break;
					case 3:
						os.writeAddMaxHP(25);
						break;
					case 4:
						os.writeAddMaxHP(30);
						break;
					case 5:
						os.writeAddMaxHP(35);
						break;
					case 6:
						os.writeAddMaxHP(40);
						break;
					case 7:
						os.writeAddMaxHP(45);
						break;
					case 8:
						os.writeAddMaxHP(50);
						break;
					case 9:
						os.writeAddMaxHP(55);
						break;
					default:
						break;
					}
				}
				/** 예언자의 견갑 **/
				if (itemId == 900080) {
					if (getEnchantLevel() >= 0 && getEnchantLevel() <= 15)
						os.writeMagicHIT(1);
				}
				
				/** 지휘관/사이하/대마법사의 견갑 pvp감소+공포+피 **/
				if ((itemId >= 900201 && itemId <= 900203) && getEnchantLevel() >= 5){
					if(getEnchantLevel() == 5){
					os.writePVPAddDMGdown(1);
					os.writeC(120); // 공포내성
					os.writeC(1);
					os.writeAddMaxHP(20);
				} else if (getEnchantLevel() == 6){
					os.writePVPAddDMGdown(2);
					os.writeC(120); // 공포내성
					os.writeC(2);
					os.writeAddMaxHP(40);
				} else if (getEnchantLevel() ==7){
					os.writePVPAddDMGdown(3);
					os.writeC(120); // 공포내성
					os.writeC(3);
					os.writeAddMaxHP(60);
				} else if (getEnchantLevel() ==8){
					os.writePVPAddDMGdown(4);
					os.writeC(120); // 공포내성
					os.writeC(4);
					os.writeAddMaxHP(80);
				} else if (getEnchantLevel() ==9){
					os.writePVPAddDMGdown(5);
					os.writeC(120); // 공포내성
					os.writeC(5);
					os.writeAddMaxHP(100);
				}
				}
				
				/** 칠흑의 망토 **/
				if (itemId == 900076) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						os.writeaCHA_Bu(1);
						break;
					case 7:
						os.writeaCHA_Bu(2);
						break;
					case 8:
						os.writeaCHA_Bu(3);
						break;
					case 9:
						os.writeaCHA_Bu(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaCHA_Bu(5);
						break;
					default:
						break;
					}
				}
				/** 칠흑의 수정구 **/
				if (itemId == 118) {
					switch (getEnchantLevel()) {
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaCHA_Bu(1);
						break;
					default:
						break;
					}
				}
				/** 맘보 모자 **/
				if (itemId == 20016 || itemId == 120016) {
					switch (getEnchantLevel()) {
					case 7:
					case 8:
					case 9:
					case 10:
						os.writeaCHA_Bu(2);
						break;
					default:
						break;
					}
				}
				
				/** 맘보 코트 **/
				if (itemId == 20112 || itemId == 120112) {
					switch (getEnchantLevel()) {
					case 7:
					case 8:
					case 9:
					case 10:
						os.writeaCHA_Bu(3);
						break;
					default:
						break;
					}
				}
				/** 투사의 휘장 **/
				if (itemId == 900152) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(5);
						break;
					case 1:
						os.writeAddMaxHP(10);
						break;
					case 2:
						os.writeAddMaxHP(15);
						break;
					case 3:
						os.writeAddMaxHP(20);
						break;
					case 4:
						os.writeAddMaxHP(25);
						break;
					case 5:
						os.writeAddMaxHP(30);
						os.writeShortDMG(1);
						break;
					case 6:
						os.writeAddMaxHP(35);
						os.writeShortDMG(2);
						os.writeShortCritical(1);
						break;
					case 7:
						os.writeAddMaxHP(40);
						os.writeShortDMG(3);
						os.writeShortCritical(3);
						break;
					case 8:
						os.writeAddMaxHP(50);
						os.writeShortDMG(4);
						os.writeShortCritical(5);
						break;
					default:
						break;
					}
				}
				/** 명궁의 휘장 **/
				if (itemId == 900153) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(5);
						break;
					case 1:
						os.writeAddMaxHP(10);
						break;
					case 2:
						os.writeAddMaxHP(15);
						break;
					case 3:
						os.writeAddMaxHP(20);
						break;
					case 4:
						os.writeAddMaxHP(25);
						break;
					case 5:
						os.writeAddMaxHP(30);
						os.writeLongDMG(1);
						break;
					case 6:
						os.writeAddMaxHP(35);
						os.writeLongDMG(2);
						os.writeLongCritical(1);
						break;
					case 7:
						os.writeAddMaxHP(40);
						os.writeLongDMG(3);
						os.writeLongCritical(3);
						break;
					case 8:
						os.writeAddMaxHP(50);
						os.writeLongDMG(4);
						os.writeLongCritical(5);
						break;
					default:
						break;
					}
				}
				/** 현자의 휘장 **/
				if (itemId == 900154) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(5);
						break;
					case 1:
						os.writeAddMaxHP(10);
						break;
					case 2:
						os.writeAddMaxHP(15);
						break;
					case 3:
						os.writeAddMaxHP(20);
						break;
					case 4:
						os.writeAddMaxHP(25);
						break;
					case 5:
						os.writeAddMaxHP(30);
						os.writeShortHIT(1);
						break;
					case 6:
						os.writeAddMaxHP(35);
						os.writeShortHIT(2);
						os.writeMagicCritical(1);
						break;
					case 7:
						os.writeAddMaxHP(40);
						os.writeShortHIT(3);
						os.writeMagicCritical(2);
						break;
					case 8:
						os.writeAddMaxHP(50);
						os.writeShortHIT(4);
						os.writeMagicCritical(4);
						break;
					default:
						break;
					}
				}
				/** 커츠의 투사 휘장 **/
				if (itemId == 900081) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(5);
						break;
					case 1:
						os.writeAddMaxHP(10);
						break;
					case 2:
						os.writeAddMaxHP(15);
						break;
					case 3:
						os.writeAddMaxHP(20);
						break;
					case 4:
						os.writeAddMaxHP(25);
						break;
					case 5:
						os.writeAddMaxHP(30);
						os.writeShortDMG(1);
						os.writeDMGdown(1);
						break;
					case 6:
						os.writeAddMaxHP(35);
						os.writeShortDMG(2);
						os.writeDMGdown(2);
						os.writeAddMR(3);
						os.writeShortHIT(1);
						break;
					case 7:
						os.writeAddMaxHP(40);
						os.writeShortDMG(3);
						os.writeDMGdown(3);
						os.writeAddMR(5);
						os.writeShortHIT(3);
						os.writePVPAddDMGdown(1);
						break;
					case 8:
						os.writeAddMaxHP(50);
						os.writeShortDMG(4);
						os.writeDMGdown(4);
						os.writeAddMR(7);
						os.writeShortHIT(5);
						os.writePVPAddDMGdown(2);
						break;
					default:
						break;
					}
				}
				/** 커츠의 명궁 휘장 **/
				if (itemId == 900082) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(5);
						break;
					case 1:
						os.writeAddMaxHP(10);
						break;
					case 2:
						os.writeAddMaxHP(15);
						break;
					case 3:
						os.writeAddMaxHP(20);
						break;
					case 4:
						os.writeAddMaxHP(25);
						break;
					case 5:
						os.writeAddMaxHP(30);
						os.writeLongDMG(1);
						os.writeDMGdown(1);
						break;
					case 6:
						os.writeAddMaxHP(35);
						os.writeLongDMG(2);
						os.writeDMGdown(2);
						os.writeAddMR(3);
						os.writeLongHIT(1);
						break;
					case 7:
						os.writeAddMaxHP(40);
						os.writeLongDMG(3);
						os.writeDMGdown(3);
						os.writeAddMR(5);
						os.writeLongHIT(3);
						os.writePVPAddDMGdown(1);
						break;
					case 8:
						os.writeAddMaxHP(50);
						os.writeLongDMG(4);
						os.writeDMGdown(4);
						os.writeAddMR(7);
						os.writeLongHIT(5);
						os.writePVPAddDMGdown(2);
						break;
					default:
						break;
					}
				}
				/** 커츠의 현자 휘장 **/
				if (itemId == 900083) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(5);
						break;
					case 1:
						os.writeAddMaxHP(10);
						break;
					case 2:
						os.writeAddMaxHP(15);
						break;
					case 3:
						os.writeAddMaxHP(20);
						break;
					case 4:
						os.writeAddMaxHP(25);
						break;
					case 5:
						os.writeAddMaxHP(30);
						os.writeShortHIT(1);
						os.writeDMGdown(1);
						break;
					case 6:
						os.writeAddMaxHP(35);
						os.writeDMGdown(2);
						os.writeAddMR(3);
						os.writeShortHIT(2);
						os.writeMagicCritical(1);
						os.writeMagicHIT(1);
						break;
					case 7:
						os.writeAddMaxHP(40);
						os.writeDMGdown(3);
						os.writeAddMR(5);
						os.writeShortHIT(5);
						os.writeMagicCritical(2);
						os.writeMagicHIT(3);
						os.writePVPAddDMGdown(1);
						break;
					case 8:
						os.writeAddMaxHP(50);
						os.writeDMGdown(4);
						os.writeAddMR(7);
						os.writeShortHIT(4);
						os.writeMagicCritical(4);
						os.writeMagicHIT(5);
						os.writePVPAddDMGdown(2);
						break;
					default:
						break;
					}
				}
				/** 커츠의 수호 휘장 **/
				if (itemId == 900084) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddMaxHP(5);
						break;
					case 1:
						os.writeAddMaxHP(10);
						break;
					case 2:
						os.writeAddMaxHP(15);
						break;
					case 3:
						os.writeAddMaxHP(20);
						break;
					case 4:
						os.writeAddMaxHP(25);
						break;
					case 5:
						os.writeAddMaxHP(30);
						os.writeDMGdown(1);
						break;
					case 6:
						os.writeAddMaxHP(35);
						os.writeDMGdown(2);
						os.writeAddMR(3);
						break;
					case 7:
						os.writeAddMaxHP(40);
						os.writeDMGdown(3);
						os.writeAddMR(5);
						break;
					case 8:
						os.writeAddMaxHP(50);
						os.writeDMGdown(4);
						os.writeAddMR(7);
						break;
					default:
						break;
					}
				}
				/** 대마법사의 모자 **/
				if (itemId == 202022) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeaspirit_resis(1);
						break;
					case 6:
						os.writeaspirit_resis(2);
						break;
					case 7:
						os.writeaspirit_resis(3);
						break;
					case 8:
						os.writeaspirit_resis(4);
						break;
					case 9:
						os.writeaspirit_resis(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaspirit_resis(6);
						break;
					default:
						break;
					}
				}
				/** 지휘관의 투구 **/
				if (itemId == 22360) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeability_resis(3);
						break;
					case 5:
						os.writeability_resis(4);
						break;
					case 6:
						os.writeability_resis(5);
						break;
					case 7:
						os.writeability_resis(6);
						break;
					case 8:
						os.writeability_resis(7);
						break;
					case 9:
						os.writeability_resis(8);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeability_resis(9);
						break;
					default:
						break;
					}
				}
				/** 거대여왕 개미의 금빛 날개 **/
				if (itemId == 20049 || itemId == 900057) {
					switch (getEnchantLevel()) {
					case 0:
					case 1: 
					case 2:
					case 3:
					case 4:
						os.writeaspirit_resis(2);
						break;
					case 5:
						os.writeaspirit_resis(3);
						break;
					case 6:
						os.writeaspirit_resis(4);
						break;
					case 7:
						os.writeaspirit_resis(5);
						break;
					case 8:
						os.writeaspirit_resis(6);
						break;
					case 9:
						os.writeaspirit_resis(7);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaspirit_resis(7);
						break;
					default:
						break;
					}
				}
				/** 거대여왕 개미의 은빛 날개 **/
				if (itemId == 20050 || itemId == 900056) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeaspirit_pierce(1);
						break;
					case 6:
						os.writeaspirit_pierce(2);
						break;
					case 7:
						os.writeaspirit_pierce(3);
						break;
					case 8:
						os.writeaspirit_pierce(4);
						break;
					case 9:
						os.writeaspirit_pierce(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaspirit_resis(7);
						break;
					default:
						break;
					}
				}
				
				/** 빛나는 아덴 용사의 망토 **/
				if (itemId == 900189) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeafear_resis(2);
						os.writeaspirit_resis(2);
						break;
					case 5:
						os.writeafear_resis(2);
						os.writeaspirit_resis(3);
						os.writeDMGdown(1);
						break;
					case 6:
						os.writeaspirit_resis(2);
						os.writeafear_resis(4);
						os.writeDMGdown(2);
						break;
					case 7:
						os.writeaspirit_resis(3);
						os.writeafear_resis(5);
						os.writeDMGdown(3);
						break;
					case 8:
						os.writeaspirit_resis(4);
						os.writeafear_resis(6);
						os.writeDMGdown(4);
						break;
					case 9:
						os.writeaspirit_resis(5);
						os.writeafear_resis(7);
						os.writeDMGdown(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaspirit_resis(5);
						os.writeafear_resis(7);
						os.writeDMGdown(5);
						break;
					default:
						break;
					}
				}
				
				/** 안타라스의 완력/예지력/인내력/마력 **/
				if (itemId >= 22196 && itemId <= 22199) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						os.writeC(70);
						os.writeC(2);
						os.writeadragonS_resis(3);
						os.writeDMGdown(3);
						break;
					case 6:
						os.writeC(70);
						os.writeC(2);
						os.writeadragonS_resis(4);
						os.writeDMGdown(3);
						break;
					case 7:
						os.writeC(70);
						os.writeC(2);
						os.writeadragonS_resis(5);
						os.writeDMGdown(4);
						break;
					case 8:
						os.writeC(70);
						os.writeC(2);
						os.writeadragonS_resis(6);
						os.writeDMGdown(5);
						break;
					case 9:
						os.writeC(70);
						os.writeC(2);
						os.writeadragonS_resis(7);
						os.writeDMGdown(6);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeC(70);
						os.writeC(2);
						os.writeadragonS_resis(7);
						os.writeDMGdown(6);
						break;
					default:
						break;
					}
				}
				/** 파푸리온의 완력/예지력/인내력/마력 **/
				if (itemId >= 22200 && itemId <= 22203) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						os.writeadragonS_resis(3);
						break;
					case 6:
						os.writeadragonS_resis(4);
						break;
					case 7:
						os.writeadragonS_resis(5);
						break;
					case 8:
						os.writeadragonS_resis(6);
						break;
					case 9:
						os.writeadragonS_resis(7);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeadragonS_resis(7);
						break;
					default:
						break;
					}
				}
				/** 린드비오르의 완력/예지력/인내력/마력 **/
				if (itemId >= 22204 && itemId <= 22207) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						os.writeadragonS_resis(3);
						break;
					case 6:
						os.writeadragonS_resis(4);
						break;
					case 7:
						os.writeadragonS_resis(5);
						break;
					case 8:
						os.writeadragonS_resis(6);
						break;
					case 9:
						os.writeadragonS_resis(7);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeadragonS_resis(7);
						break;
					default:
						break;
					}
				}
				/** 발라카스의 완력, 예지력**/
				if (itemId >= 22208 && itemId <= 22209) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						 os.writeAddDMG(3); // 추가 대미지 +3 (버그가 장착/해제 중첩버그가 있는 것 같으니 나중에 더 알아보자. 현재는 옵션명만 넣은 상태. 대미지 추가 -> 연관소스 L1EquipmentSlot.java)
						os.writeReductiondown(3);
						os.writeadragonS_resis(3);
						break;
					case 6:
						os.writeAddDMG(3); // 추가 대미지 +3
						os.writeReductiondown(3);
						os.writeadragonS_resis(4);
						break;
					case 7:
						os.writeAddDMG(3); // 추가 대미지 +3
						os.writeShortCritical(1);
						os.writeReductiondown(4);
						os.writeadragonS_resis(5);
						break;
					case 8:
						os.writeAddDMG(3); // 추가 대미지 +3
						os.writeShortCritical(2);
						os.writeReductiondown(5);
						os.writeadragonS_resis(6);
						break;
					case 9:
						os.writeAddDMG(3); // 추가 대미지 +3
						os.writeShortCritical(3);
						os.writeReductiondown(6);
						os.writeadragonS_resis(7);
						break;
					case 10:
						os.writeAddDMG(3); // 추가 대미지 +3
						os.writeShortCritical(3);
						os.writeReductiondown(6);
						os.writeadragonS_resis(7);
						break;
					default:
						break;
					}
				}
				
				/** 발라카스의 인내력 **/
				if (itemId == 22210) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						os.writeLongDMG(3); // 원거리 대미지 +3
						os.writeReductiondown(3);
						os.writeadragonS_resis(3);
						break;
					case 6:
						os.writeLongDMG(3); // 원거리 대미지 +3
						os.writeReductiondown(3);
						os.writeadragonS_resis(4);
						break;
					case 7:
						os.writeLongDMG(3); // 원거리 대미지 +3
						os.writeLongCritical(1);
						os.writeReductiondown(4);
						os.writeadragonS_resis(5);
						break;
					case 8:
						os.writeLongDMG(3); // 원거리 대미지 +3
						os.writeLongCritical(2);
						os.writeReductiondown(5);
						os.writeadragonS_resis(6);
						break;
					case 9:
						os.writeLongDMG(3); // 원거리 대미지 +3
						os.writeLongCritical(3);
						os.writeReductiondown(6);
						os.writeadragonS_resis(7);
						break;
					case 10:
						os.writeLongDMG(3); // 원거리 대미지 +3
						os.writeLongCritical(3);
						os.writeReductiondown(6);
						os.writeadragonS_resis(7);
						break;
					default:
						break;
					}
				}
				
				/** 발라카스의 마력 **/
				if (itemId == 22211) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						os.writeReductiondown(3);
						os.writeadragonS_resis(3);
						break;
					case 6:
						os.writeReductiondown(3);
						os.writeadragonS_resis(4);
						break;
					case 7:
						os.writeMagicCritical(1);
						os.writeReductiondown(4);
						os.writeadragonS_resis(5);
						break;
					case 8:
						os.writeMagicCritical(2);
						os.writeReductiondown(5);
						os.writeadragonS_resis(6);
						break;
					case 9:
						os.writeMagicCritical(3);
						os.writeReductiondown(6);
						os.writeadragonS_resis(7);
						break;
					case 10:
						os.writeMagicCritical(3);
						os.writeReductiondown(6);
						os.writeadragonS_resis(7);
						break;
					default:
						break;
					}
				}
				
				/** 할파스의 완력 **/
				if (itemId == 111137) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 7);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 1:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 9);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 2:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 11);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 3:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 13);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 4:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 15);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 5:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 17);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 6:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 19);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 7:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 21);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 8:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 23);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 9:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 25);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 10:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 27);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
				}
				
				/** 할파스의 예지력 **/
				if (itemId == 111141) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 7);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 1:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 9);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 2:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 11);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 3:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 13);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 4:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 15);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 5:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 17);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 6:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 19);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 7:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 21);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 8:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 23);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 9:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 25);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 10:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 27);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
				}
				
				
				/** 할파스의 마력 **/
				if (itemId == 111140) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 7);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 1:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 9);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 2:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 11);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 3:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 13);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 4:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 15);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 5:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 17);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 6:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 19);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 7:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 21);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 8:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 23);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 9:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 25);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					case 10:
						os.writeC(70);
						os.writeC(2);
						os.writeDMGdown(getItem().get_damage_reduction() + 27);
						os.writeReductiondown(5);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
				}
				
				
				
			/** 아인하사드의 섬광 **/	
			if (itemId == 2944) {
				switch (getEnchantLevel()) {
				case 0:
					os.writeShortCritical(7); // 근거리 치명타
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+20");
					os.writeability_pierce(12); // 기술적중
					os.writeaspirit_pierce(12); // 정령적중
					os.writeafear_pierce(12); // 공포적중
					break;
				case 1:
					os.writeShortCritical(8);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+21");
					os.writeability_pierce(13);
					os.writeaspirit_pierce(13);
					os.writeafear_pierce(13); // 공포적중
					break;
				case 2:
					os.writeShortCritical(9);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+22");
					os.writeability_pierce(14);
					os.writeaspirit_pierce(14);
					os.writeafear_pierce(14); // 공포적중
					break;
				case 3:
					os.writeShortCritical(10);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+23");
					os.writeability_pierce(15); 
					os.writeaspirit_pierce(15);
					os.writeafear_pierce(15); // 공포적중
					break;
				case 4:
					os.writeShortCritical(11);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+24");
					os.writeability_pierce(16);
					os.writeaspirit_pierce(16);
					os.writeafear_pierce(16); // 공포적중
					break;
				case 5:
					os.writeShortCritical(12);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+25");
					os.writeability_pierce(17);
					os.writeaspirit_pierce(17);
					os.writeafear_pierce(17); // 공포적중
					break;
				case 6:
					os.writeShortCritical(13);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+26");
					os.writeability_pierce(18);
					os.writeaspirit_pierce(18);
					os.writeafear_pierce(18); // 공포적중
					break;
				case 7:
					os.writeShortCritical(14);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+27");
					os.writeability_pierce(19);
					os.writeaspirit_pierce(19);
					os.writeafear_pierce(19); // 공포적중
					break;
				case 8:
					os.writeShortCritical(15);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+28");
					os.writeability_pierce(20);
					os.writeaspirit_pierce(20);
					os.writeafear_pierce(20); // 공포적중
					break;
				case 9:
					os.writeShortCritical(16);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+29");
					os.writeability_pierce(21);
					os.writeaspirit_pierce(21);
					os.writeafear_pierce(21); // 공포적중
					break;
				case 10:
					os.writeShortCritical(17);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+30");
					os.writeability_pierce(22);
					os.writeaspirit_pierce(22);
					os.writeafear_pierce(22); // 공포적중
					break;
				default:
					break;
				}
			}
			
			/** 그랑카인의 심판 **/
			if (itemId == 2945) {
				switch (getEnchantLevel()) {
				case 0:
					os.writeShortCritical(7); // 근거리 치명타
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+20");
					os.writeability_pierce(15);
					break;
				case 1:
					os.writeShortCritical(8);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+21");
					os.writeability_pierce(16);
					break;
				case 2:
					os.writeShortCritical(9);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+22");
					os.writeability_pierce(17);
					break;
				case 3:
					os.writeShortCritical(10);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+23");
					os.writeability_pierce(18);
					break;
				case 4:
					os.writeShortCritical(11);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+24");
					os.writeability_pierce(19);
					break;
				case 5:
					os.writeShortCritical(12);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+25");
					os.writeability_pierce(20);
					break;
				case 6:
					os.writeShortCritical(13);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+26");
					os.writeability_pierce(21);
					break;
				case 7:
					os.writeShortCritical(14);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+27");
					os.writeability_pierce(22);
					break;
				case 8:
					os.writeShortCritical(15);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+28");
					os.writeability_pierce(23);
					break;
				case 9:
					os.writeShortCritical(16);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+29");
					os.writeability_pierce(24);
					break;
				case 10:
					os.writeShortCritical(17);
					os.writeC(39);
					os.writeS("\\fI이뮨 무시 \\aA+30");
					os.writeability_pierce(25);
					break;
				default:
					break;
				}
			}
				/** 에바의 방패 **/
				if (itemId == 20235) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						os.writeadragonS_resis(2);
						break;
					case 6:
						os.writeadragonS_resis(3);
						break;
					case 7:
						os.writeadragonS_resis(4);
						break;
					case 8:
						os.writeadragonS_resis(5);
						break;
					case 9:
						os.writeadragonS_resis(6);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeadragonS_resis(7);
						break;
					default:
						break;
					}
				}
				/** 시어의 심안 **/
				if (itemId == 22214) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeaspirit_resis(2);
						break;
					case 5:
						os.writeaspirit_resis(3);
						break;
					case 6:
						os.writeaspirit_resis(4);
						break;
					case 7:
						os.writeMagicHIT(1);// 마법 적중
						os.writeaspirit_resis(5);
						break;
					case 8:
						os.writeMagicHIT(2);// 마법 적중
						os.writeaspirit_resis(6);
						break;
					case 9:
						os.writeMagicHIT(3);// 마법 적중
						os.writeaspirit_resis(7);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeMagicHIT(4);// 마법 적중
						os.writeaspirit_resis(8);
						break;
					default:
						break;
					}
				}
				
				if (itemId == 900165) { // 리치의 수정구
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeaspirit_resis(2);
						break;
					case 5:
						os.writeaspirit_resis(3);
						break;
					case 6:
						os.writeaspirit_resis(4);
						break;
					case 7:
						os.writeAddSP(1); 
						os.writeMagicHIT(1);// 마법 적중
						os.writeaspirit_resis(5);
						break;
					case 8:
						os.writeAddSP(2);
						os.writeMagicHIT(2);// 마법 적중
						os.writeaspirit_resis(6);
						break;
					case 9:
						os.writeAddSP(3);
						os.writeMagicHIT(3);// 마법 적중
						os.writeaspirit_resis(7);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeAddSP(4);
						os.writeMagicHIT(4);// 마법 적중
						os.writeaspirit_resis(8);
						break;
					default:
						break;
					}
				}
				/** 반역자의방패 **/
				if (itemId == 22263) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeAddMaxHP(20);
						os.writeability_pierce(1);
						break;
					case 6:
						os.writeAddMaxHP(40);
						os.writeability_pierce(2);
						break;
					case 7:
						os.writeAddMaxHP(60);
						os.writeability_pierce(3);
						break;
					case 8:
						os.writeAddMaxHP(80);
						os.writeability_pierce(4);
						break;
					case 9:
						os.writeAddMaxHP(100);
						os.writeability_pierce(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeAddMaxHP(120);
						os.writeability_pierce(6);
						break;
					default:
						break;
					}
				}
				/** 리치의 반지 **/
				if (itemId == 900163) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeMagicCritical(3);
						break;
					default:
						break;
					}
				}
				
		
				/** 뱀파이어의 망토 **/
				if (itemId == 20079) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						os.writeafear_resis(2);
						break;
					case 7:
						os.writeafear_resis(3);
						break;
					case 8:
						os.writeafear_resis(4);
						break;
					case 9:
						os.writeafear_resis(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeafear_resis(5);
						break;
					default:
						break;
					}
				}
				/** 아이리스,머미로드,프로켈의 부츠: 전체내성, MR, 리덕 **/
				if (itemId == 900155 || itemId == 900168 || itemId == 900122) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeaAll_resis(1);
						break;
					case 6:
						os.writeaAll_resis(2);
						break;
					case 7:
						os.writeaAll_resis(3);
						os.writeAddMR(3);
						break;
					case 8:
						os.writeaAll_resis(4);
						os.writeAddMR(4);
						break;
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeaAll_resis(5);
						os.writeDMGdown(1);
						os.writeAddMR(5);
						break;
					default:
						break;
					}
				}
				/** 머미로드의 장갑 **/
				if (itemId == 900156) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeAddSP(1);
						break;
					case 1:
						os.writeAddSP(1);
						break;
					case 2:
						os.writeAddSP(1);
						break;
					case 3:
						os.writeAddSP(1);
						break;
					case 4:
						os.writeAddSP(1);
						break;
					case 5:
						os.writeAddSP(1);
						break;
					case 6:
						os.writeAddSP(1);
						break;
					case 7:
						os.writeMagicHIT(1);
						os.writeAddSP(1);
						break;
					case 8:
						os.writeMagicHIT(2);
						os.writeAddSP(2);
						break;
					case 9:
						os.writeMagicHIT(3);
						os.writeAddSP(3);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeMagicHIT(3);
						os.writeAddSP(3);
						break;
					default:
						break;
					}
				}
				
				/** 빛나는 아덴 용사의 견갑 **/
				if (itemId == 900121) {
					switch (getEnchantLevel()) {
					case 6:
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeAddSP(1);
						os.writeShortHIT(1);
						os.writeLongHIT(1);
						os.writeMagicHIT(1);
						break;
					case 7:
						os.writeShortDMG(2);
						os.writeLongDMG(2);
						os.writeAddSP(2);
						os.writeShortHIT(2);
						os.writeLongHIT(2);
						os.writeMagicHIT(2);
						os.writeDMGdown(1);
						break;
					case 8:
						os.writeShortDMG(3);
						os.writeLongDMG(3);
						os.writeAddSP(3);
						os.writeShortHIT(3);
						os.writeLongHIT(3);
						os.writeMagicHIT(3);
						os.writeDMGdown(2);
						break;
					case 9:
						os.writeShortDMG(3);
						os.writeLongDMG(3);
						os.writeAddSP(3);
						os.writeShortHIT(3);
						os.writeLongHIT(3);
						os.writeMagicHIT(3);
						os.writeDMGdown(3);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeShortDMG(3);
						os.writeLongDMG(3);
						os.writeAddSP(3);
						os.writeShortHIT(3);
						os.writeLongHIT(3);
						os.writeMagicHIT(3);
						os.writeDMGdown(3);
						break;
					default:
						break;
					}
				}
				/** 쿠거의 가더 **/
				if (itemId == 900157) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeShortHIT(3);
						break;
					case 5:
						os.writeShortHIT(4);
						os.writeaAll_resis(1);
						break;
					case 6:
						os.writeShortHIT(5);
						os.writeaAll_resis(2);
						break;
					case 7:
						os.writeShortHIT(6);
						os.writeaAll_resis(3);
						break;
					case 8:
						os.writeShortHIT(7);
						os.writeaAll_resis(4);
						break;
					case 9:
						os.writeShortHIT(8);
						os.writeaAll_resis(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeShortHIT(8);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
				}
				/** 우그누스의 가더 **/
				if (itemId == 900158) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeLongHIT(3);
						break;
					case 5:
						os.writeLongHIT(4);
						os.writeaAll_resis(1);
						break;
					case 6:
						os.writeLongHIT(5);
						os.writeaAll_resis(2);
						break;
					case 7:
						os.writeLongHIT(6);
						os.writeaAll_resis(3);
						break;
					case 8:
						os.writeLongHIT(7);
						os.writeaAll_resis(4);
						break;
					case 9:
						os.writeLongHIT(8);
						os.writeaAll_resis(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeLongHIT(8);
						os.writeaAll_resis(5);
						break;
					default:
						break;
					}
				}
				/** 나이트발드의 각반 **/
				if (itemId == 900159) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeShortHIT(1);
					//	os.writeShortDMG(1);
						break;
					case 6:
						os.writeShortHIT(2);
					//	os.writeShortDMG(2);
						break;
					case 7:
						os.writeShortHIT(3);
					//	os.writeShortDMG(3);
						break;
					case 8:
						os.writeShortHIT(4);
					//	os.writeShortDMG(4);
						break;
					case 9:
						os.writeShortHIT(5);
					//	os.writeShortDMG(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeShortHIT(5);
					//	os.writeShortDMG(5);
						break;
					default:
						break;
					}
				}
				/** 아이리스의 각반 **/
				if (itemId == 900160) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeLongHIT(1);
					//	os.writeLongDMG(1);
						break;
					case 6:
						os.writeLongHIT(2);
					//	os.writeLongDMG(2);
						break;
					case 7:
						os.writeLongHIT(3);
					//	os.writeLongDMG(3);
						break;
					case 8:
						os.writeLongHIT(4);
					//	os.writeLongDMG(4);
						break;
					case 9:
						os.writeLongHIT(5);
					//	os.writeLongDMG(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeLongHIT(5);
					//	os.writeLongDMG(5);
						break;
					default:
						break;
					}
				}
				/** 뱀파이어의 각반 **/
				if (itemId == 900161) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeMagicHIT(1);
					//	os.writeAddSP(1);
						break;
					case 6:
						os.writeMagicHIT(2);
					//	os.writeAddSP(2);
						break;
					case 7:
						os.writeMagicHIT(3);
					//	os.writeAddSP(3);
						break;
					case 8:
						os.writeMagicHIT(4);
					//	os.writeAddSP(4);
						break;
					case 9:
						os.writeMagicHIT(5);
					//	os.writeAddSP(5);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeMagicHIT(5);
						break;
					default:
						break;
					}
				}
				
				/** 드래곤 슬레이어의 각반 **/
				if (itemId == 900200) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeAddMR(7);
						os.writePVPAddDMGdown(2);
						os.writeAddMaxHP(100);
						break;
					case 5:
						os.writeAddMR(9);
						os.writePVPAddDMGdown(3);
						os.writeAddMaxHP(100);
						break;
					case 6:
						os.writeAddMR(11);
						os.writePVPAddDMGdown(4);
						os.writeAddMaxHP(100);
						break;
					case 7:
						os.writeAddMR(13);
						os.writePVPAddDMGdown(5);
						os.writeAddMaxHP(150);
						break;
					case 8:
						os.writeAddMR(15);
						os.writePVPAddDMGdown(6);
						os.writeAddMaxHP(200);
						break;
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeAddMR(17);
						os.writePVPAddDMGdown(7);
						os.writeAddMaxHP(250);
						break;
					default:
						break;
					}
				}
				
				
				/** 붉은 해의 각반 **///시간제아님
			/*	if (itemId == 9001118) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeAddMaxHP(5);
						os.writeDMGdown(1);
						break;
					case 5:
						os.writeAddMaxHP(15);
						os.writeDMGdown(1);
						break;
					case 6:
						os.writeAddMaxHP(25);
						os.writeDMGdown(1);
						break;
					case 7:
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeMagicHIT(1);
						os.writeAddMaxHP(35);
						os.writeDMGdown(1);
						break;
					case 8:
						os.writeShortDMG(2);
						os.writeLongDMG(2);
						os.writeMagicHIT(2);
						os.writeAddMaxHP(45);
						os.writeDMGdown(1);
						break;
					case 9:
						os.writeShortDMG(3);
						os.writeLongDMG(3);
						os.writeMagicHIT(3);
						os.writeAddMaxHP(55);
						os.writeDMGdown(1);
						break;
					case 10:
						os.writeShortDMG(4);
						os.writeLongDMG(4);
						os.writeMagicHIT(4);
						os.writeAddMaxHP(65);
						os.writeDMGdown(1);
						break;
					case 11:
						os.writeShortDMG(5);
						os.writeLongDMG(5);
						os.writeMagicHIT(5);
						os.writeAddMaxHP(75);
						os.writeDMGdown(1);
						break;
					case 12:
						os.writeShortDMG(6);
						os.writeLongDMG(6);
						os.writeMagicHIT(6);
						os.writeAddMaxHP(85);
						os.writeDMGdown(1);
						break;
					case 13:
						os.writeShortDMG(7);
						os.writeLongDMG(7);
						os.writeMagicHIT(7);
						os.writeAddMaxHP(95);
						os.writeDMGdown(1);
						break;
					case 14:
						os.writeShortDMG(8);
						os.writeLongDMG(8);
						os.writeMagicHIT(8);
						os.writeAddMaxHP(105);
						os.writeDMGdown(1);
						break;
					case 15:
						os.writeShortDMG(9);
						os.writeLongDMG(9);
						os.writeMagicHIT(9);
						os.writeAddMaxHP(115);
						os.writeDMGdown(1);
						break;
					default:
						break;
					}
				}*/
				/** 붉은 해의 각반 **/
			/*	if (itemId == 9001117) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeAddMaxHP(5);
						os.writeDMGdown(1);
						break;
					case 5:
						os.writeAddMaxHP(15);
						os.writeDMGdown(1);
						break;
					case 6:
						os.writeAddMaxHP(25);
						os.writeDMGdown(1);
						break;
					case 7:
						os.writeShortDMG(1);
						os.writeLongDMG(1);
						os.writeMagicHIT(1);
						os.writeAddMaxHP(35);
						os.writeDMGdown(1);
						break;
					case 8:
						os.writeShortDMG(2);
						os.writeLongDMG(2);
						os.writeMagicHIT(2);
						os.writeAddMaxHP(45);
						os.writeDMGdown(1);
						break;
					case 9:
						os.writeShortDMG(3);
						os.writeLongDMG(3);
						os.writeMagicHIT(3);
						os.writeAddMaxHP(55);
						os.writeDMGdown(1);
						break;
					case 10:
						os.writeShortDMG(4);
						os.writeLongDMG(4);
						os.writeMagicHIT(4);
						os.writeAddMaxHP(65);
						os.writeDMGdown(1);
						break;
					case 11:
						os.writeShortDMG(5);
						os.writeLongDMG(5);
						os.writeMagicHIT(5);
						os.writeAddMaxHP(75);
						os.writeDMGdown(1);
						break;
					case 12:
						os.writeShortDMG(6);
						os.writeLongDMG(6);
						os.writeMagicHIT(6);
						os.writeAddMaxHP(85);
						os.writeDMGdown(1);
						break;
					case 13:
						os.writeShortDMG(7);
						os.writeLongDMG(7);
						os.writeMagicHIT(7);
						os.writeAddMaxHP(95);
						os.writeDMGdown(1);
						break;
					case 14:
						os.writeShortDMG(8);
						os.writeLongDMG(8);
						os.writeMagicHIT(8);
						os.writeAddMaxHP(105);
						os.writeDMGdown(1);
						break;
					case 15:
						os.writeShortDMG(9);
						os.writeLongDMG(9);
						os.writeMagicHIT(9);
						os.writeAddMaxHP(115);
						os.writeDMGdown(1);
						break;
					default:
						break;
					}
				}*/
				/** 지룡의 티셔츠 **/
				if (itemId == 900025) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeDMGdown(1);
						break;
					case 1:
						os.writeDMGdown(1);
						break;
					case 2:
						os.writeDMGdown(1);
						break;
					case 3:
						os.writeDMGdown(1);
						break;
					case 4:
						os.writeDMGdown(1);
						break;
					case 5:
						os.writeDMGdown(1);
						os.writeAddMR(4);
						break;
					case 6:
						os.writeDMGdown(1);
						os.writeAddMR(5);
						break;
					case 7:
						os.writeDMGdown(1);
						os.writeAddMR(6);
						break;
					case 8:
						os.writeDMGdown(1);
						os.writeAddMR(8);
						os.writeAddEXP(2);
						break;
					case 9:
						os.writeDMGdown(2);
						os.writeAddMR(11);
						os.writeAddEXP(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeDMGdown(2);
						os.writeAddMR(14);
						os.writeAddMaxHP(100);
						os.writeAddEXP(6);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}
				/** 화룡의 티셔츠 **/
				if (itemId == 900026) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeShortDMG(1);
						break;
					case 5:
						os.writeShortDMG(1);
						os.writeability_resis(8);
						break;
					case 6:
						os.writeShortDMG(1);
						os.writeability_resis(9);
						break;
					case 7:
						os.writeShortDMG(1);
						os.writeability_resis(10);
						break;
					case 8:
						os.writeShortDMG(1);
						os.writeShortHIT(2);
						os.writeability_resis(12);
						break;
					case 9:
						os.writeShortDMG(2);
						os.writeShortHIT(4);
						os.writeability_resis(15);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeShortDMG(2);
						os.writeShortHIT(6);
						os.writeability_resis(18);
						os.writeAddMaxHP(100);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}
				/** 풍룡의티셔츠 **/
				if (itemId == 900027) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeLongDMG(1);
						break;
					case 5:
						os.writeLongDMG(1);
						os.writeability_resis(8);
						break;
					case 6:
						os.writeLongDMG(1);
						os.writeability_resis(9);
						break;
					case 7:
						os.writeLongDMG(1);
						os.writeability_resis(10);
						break;
					case 8:
						os.writeLongDMG(1);
						os.writeLongHIT(2);
						os.writeability_resis(12);
						break;
					case 9:
						os.writeLongDMG(2);
						os.writeLongHIT(4);
						os.writeability_resis(15);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeLongDMG(2);
						os.writeLongHIT(6);
						os.writeability_resis(18);
						os.writeAddMaxHP(100);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}
				/** 수룡의 티셔츠 **/
				if (itemId == 900028) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeAddSP(1);
						break;
					case 5:
						os.writeAddSP(1);
						os.writeability_resis(8);
						break;
					case 6:
						os.writeAddSP(1);
						os.writeability_resis(9);
						break;
					case 7:
						os.writeAddSP(1);
						os.writeability_resis(10);
						break;
					case 8:
						os.writeAddSP(2);
						os.writeMagicHIT(1);// 마법 적중
						os.writeability_resis(12);
						break;
					case 9:
						os.writeAddSP(2);
						os.writeMagicHIT(3);// 마법 적중
						os.writeability_resis(15);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeAddSP(3);
						os.writeMagicHIT(4);// 마법 적중
						os.writeability_resis(18);
						os.writeAddMaxHP(100);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}

				/** 축복받은 지룡의 티셔츠 **/
				if (itemId == 900184) {
					switch (getEnchantLevel()) {
					case 0:
						os.writeDMGdown(1);
						break;
					case 1:
						os.writeDMGdown(1);
						break;
					case 2:
						os.writeDMGdown(1);
						break;
					case 3:
						os.writeDMGdown(1);
						break;
					case 4:
						os.writeDMGdown(1);
						break;
					case 5:
						os.writeDMGdown(1);
						os.writeAddMR(4);
						break;
					case 6:
						os.writeDMGdown(1);
						os.writeAddMR(5);
						break;
					case 7:
						os.writeDMGdown(1);
						os.writeAddMR(6);
						os.writeAddEXP(2);
						break;
					case 8:
						os.writeDMGdown(1);
						os.writeAddMR(8);
						os.writeAddEXP(4);
						break;
					case 9:
						os.writeDMGdown(2);
						os.writeAddMR(11);
						os.writeAddEXP(6);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeDMGdown(2);
						os.writeAddMR(14);
						os.writeAddMaxHP(100);
						os.writeAddEXP(8);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}
				/** 축복받은 화룡의티셔츠 **/
				if (itemId == 900185) {
					switch (getEnchantLevel()) {
					case 5:
						os.writeability_resis(8);
						break;
					case 6:
						os.writeability_resis(9);
						break;
					case 7:
						os.writeShortDMG(1);
						os.writeShortHIT(1);
						os.writeability_resis(10);
						os.writeaAll_resis(1);
						break;
					case 8:
						os.writeShortDMG(1);
						os.writeShortHIT(3);
						os.writeability_resis(12);
						os.writeaAll_resis(2);
						break;
					case 9:
						os.writeShortDMG(2);
						os.writeShortHIT(5);
						os.writeability_resis(15);
						os.writeaAll_resis(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeShortDMG(2);
						os.writeShortHIT(7);
						os.writeability_resis(18);
						os.writeaAll_resis(6);
						os.writeAddMaxHP(100);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}
				/** 축복받은 풍룡의티셔츠 **/
				if (itemId == 900186) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeLongDMG(1);
						break;
					case 5:
						os.writeLongDMG(1);
						os.writeability_resis(8);
						break;
					case 6:
						os.writeLongDMG(1);
						os.writeability_resis(9);
						break;
					case 7:
						os.writeLongDMG(1);
						os.writeLongHIT(1);
						os.writeability_resis(10);
						os.writeaAll_resis(1);
						break;
					case 8:
						os.writeLongDMG(1);
						os.writeLongHIT(3);
						os.writeability_resis(12);
						os.writeaAll_resis(2);
						break;
					case 9:
						os.writeLongDMG(2);
						os.writeLongHIT(5);
						os.writeability_resis(15);
						os.writeaAll_resis(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeLongDMG(2);
						os.writeLongHIT(7);
						os.writeability_resis(18);
						os.writeaAll_resis(6);
						os.writeAddMaxHP(100);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}
				/** 축복받은 수룡의 티셔츠 **/
				if (itemId == 900187) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeAddSP(1);
						break;
					case 5:
						os.writeAddSP(1);
						os.writeability_resis(8);
						break;
					case 6:
						os.writeAddSP(1);
						os.writeability_resis(9);
						break;
					case 7:
						os.writeAddSP(2);
						os.writeability_resis(10);
						os.writeaAll_resis(1);
						break;
					case 8:
						os.writeAddSP(2);
						os.writeMagicHIT(2);// 마법 적중
						os.writeability_resis(12);
						os.writeaAll_resis(2);
						break;
					case 9:
						os.writeAddSP(2);
						os.writeMagicHIT(4);// 마법 적중
						os.writeability_resis(15);
						os.writeaAll_resis(4);
						break;
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						os.writeAddSP(3);
						os.writeMagicHIT(5);// 마법 적중
						os.writeability_resis(18);
						os.writeaAll_resis(6);
						os.writeAddMaxHP(100);
						os.writePVPAddDMG(1);
						os.writePVPAddDMGdown(1);
						break;
					default:
						break;
					}
				}

				/** 고대투사의가더 근거리 대미지 **/
				if (itemId == 22003) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeShortDMG(1);
						break;
					case 5:
					case 6:
						os.writeShortDMG(2);
						break;
					case 7:
					case 8:
						os.writeShortDMG(3);
						break;
					case 9:
					case 10:
						os.writeShortDMG(4);
						break;
					default:
						break;
					}
				}

				/** 고대명궁의가더 원거리 대미지 **/
				if (itemId == 22000) {
					switch (getEnchantLevel()) {
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						os.writeLongDMG(1);
						break;
					case 5:
					case 6:
						os.writeLongDMG(2);
						break;
					case 7:
					case 8:
						os.writeLongDMG(3);
						break;
					case 9:
					case 10:
						os.writeLongDMG(4);
						break;
					default:
						break;
					}
				}
				/** PVP 데미지 감소 **/
				
				if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
							&& (getItem().get장신구처리() == 10) && getEnchantLevel() == 6) {
						int enchantPvpDown = getEnchantLevel() - 5;
						if (enchantPvpDown >= 3) {
							enchantPvpDown = 3;
						}
					os.writePVPAddDMGdown(getItem().get_regist_calcPcDefense() + enchantPvpDown);
				}
				if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& (getItem().get장신구처리() == 10) && getEnchantLevel() == 7) {
					int enchantPvpDown = getEnchantLevel() - 4;
					if (enchantPvpDown >= 3) {
						enchantPvpDown = 3;
					}
				os.writePVPAddDMGdown(getItem().get_regist_calcPcDefense() + enchantPvpDown);
			}
				if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& (getItem().get장신구처리() == 10) && getEnchantLevel() == 8) {
					int enchantPvpDown = getEnchantLevel() - 3;
					if (enchantPvpDown >= 5) {
						enchantPvpDown = 5;
					}
				os.writePVPAddDMGdown(getItem().get_regist_calcPcDefense() + enchantPvpDown);
			}
				if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& (getItem().get장신구처리() == 10) && getEnchantLevel() == 9) {
					int enchantPvpDown = getEnchantLevel() - 2;
					if (enchantPvpDown >= 7) {
						enchantPvpDown = 7;
					}
				os.writePVPAddDMGdown(getItem().get_regist_calcPcDefense() + enchantPvpDown);
			}
				/** PVP 데미지 감소 **/
				
				/** PVP 추가 데미지 **/
				if (itemType2 == 2 && ((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5)
						&& (getItem().get장신구처리() == 9 || getItem().get장신구처리() == 11) && getEnchantLevel() >= 6) {
					int enchantPvpDmg = getEnchantLevel() - 5;
					if (enchantPvpDmg >= 5) {
						enchantPvpDmg = 5;
					}
					os.writePVPAddDMG(getItem().get_regist_PVPweaponTotalDamage() + enchantPvpDmg);
				}
				
				/** 빛나는 마력의 장갑 **/
				if (itemId == 20274 && getEnchantLevel() > 4) {
					int 무게보너스 = getEnchantLevel() - 4;
					os.writeAddWeight(+(getItem().getWeightReduction() + 무게보너스));
				}
				if (itemId == 900090) {
					os.writeDMGdownprobability(getEnchantLevel(), 20);
				}
				if (itemId == 22263) {
					os.writeDMGdownprobability(getEnchantLevel() * 2, 50);
				}
				if (itemId == 900046 || itemId == 900071) {
					os.writeDMGdownprobability(getEnchantLevel(), 10);
				}
				if (itemId == 22359 || itemId == 222308 || itemId == 222309 || itemId == 222307) {
					switch (getEnchantLevel()) {
					case 7:
						os.writeAddMaxHP(20);
						break;
					case 8:
						os.writeAddMaxHP(40);
						break;
					case 9:
						os.writeAddMaxHP(60);
						os.writeDMGdown(1);
						break;
					case 10:
						os.writeAddMaxHP(60);
						os.writeDMGdown(1);
						break;
					default:
						break;
					}
				}
				
				// TODO 축복받은 스냅퍼의 체력 반지 리뉴얼
				if (itemId == 222332) {
					switch (getEnchantLevel()) {
					case 6:
						os.writeDMGdown(1);
						os.writeDMGdownprobability(1, 20);
						break;
					case 7:
						os.writeDMGdown(2);
						os.writeDMGdownprobability(2, 20);
						break;
					case 8:
						os.writeDMGdown(3);
						os.writeDMGdownprobability(3, 20);
						break;
					}
				}
				// TODO 스냅퍼의 체력 반지 리뉴얼
				if (itemId == 22226) {
					switch (getEnchantLevel()) {
					case 6:
						break;
					case 7:
						os.writeDMGdown(1);
						os.writeDMGdownprobability(1, 20);
						break;
					case 8:
						os.writeDMGdown(2);
						os.writeDMGdownprobability(2, 20);
						break;
					}
				}

				// 디비 아이템 추가 옵션 부분
				if (getItem().getHitModifier() != 0) { // 무기에 붙는 명중
					if (itemType2 == 1 && getItem().getType1() != 20) {
						os.writeShortHIT(getItem().getHitModifier());
					} else {
						os.writeLongHIT(getItem().getHitModifier());
					}
				}
				if (getItem().getHitRate() != 0) { // 근거리 명중
					os.writeShortHIT(getItem().getHitRate());
				}
				if (getItem().getDmgRate() != 0) { // 근거리 데미지
					os.writeShortDMG(getItem().getDmgRate());
				}
				if (getItem().getBowHitRate() != 0) { // 원거리 명중
					os.writeLongHIT(getItem().getBowHitRate());
				}
				if (getItem().getBowDmgRate() != 0) { // 원거리 대미지
					os.writeLongDMG(getItem().getBowDmgRate());
				}
				// STR~CHA
				if (getItem().get_addstr() != 0) { // 스탯 힘
					os.writeaSTR_Bu(getItem().get_addstr());
				}
				if (getItem().get_adddex() != 0) { // 스탯 덱스
					os.writeaDEX_Bu(getItem().get_adddex());
				}
				if (getItem().get_addcon() != 0) { // 스탯 콘
					os.writeaCON_Bu(getItem().get_addcon());
				}
				if (getItem().get_addwis() != 0) { // 스탯 위즈
					os.writeaWIS_Bu(getItem().get_addwis());
				}
				if (getItem().get_addint() != 0) { // 스탯 인트
					os.writeaINT_Bu(getItem().get_addint());
				}
				if (getItem().get_addcha() != 0) { // 스탯 카리
					os.writeaCHA_Bu(getItem().get_addcha());
				}
				if (getItem().get_addeinhasadper() != 0) { // 축복 소모 효율
					os.writeaBlesssomo(getItem().get_addeinhasadper());
				}
				if (getItem().getMinLevel() != 0) { // 최소 사용 레벨
					os.writeUseLevel(getItem().getMinLevel());
				}
				if (getItem().getMaxLevel() != 0) { // 최소~최대 사용 레벨
					os.writeLimitLevel(getItem().getMinLevel(), getItem().getMaxLevel());
				}
				
				if (getItem().isHasteItem()) { // 헤이스트 아이템
					os.writeC(39);
					os.writeS("\\fI효과: \\aA헤이스트");
				}
				if (getItem().get_damage_reduction() != 0) { // 대미지 리덕션
					os.writeDMGdown(getItem().get_damage_reduction());
				}
				if (getItem().getWeightReduction() != 0) { // 무게 게이지
					os.writeAddWeight(getItem().getWeightReduction());
				}
				if (getItem().get_regist_PVPweaponTotalDamage() != 0) { // PVP 대미지
					os.writePVPAddDMG(getItem().get_regist_PVPweaponTotalDamage());
				}
				if (getItem().get_regist_calcPcDefense() != 0) { // PVP 리덕션
					os.writePVPAddDMGdown(getItem().get_regist_calcPcDefense());
				}
				if (getItem().get_melee_critical_probability() > 0) { // 근거리 치명타
					os.writeShortCritical(getItem().get_melee_critical_probability());
				}
				if (getItem().get_missile_critical_probability() > 0) { // 원거리
																		// 치명타
					os.writeLongCritical(getItem().get_missile_critical_probability());
				}
				if (getItem().getMagicHitup() != 0) { // 마법 적중
					os.writeMagicHIT(getItem().getMagicHitup());
				}

				// 불의 속성
				if (getItem().get_defense_fire() != 0) {
					os.writeRegistFire(getItem().get_defense_fire());
				}
				// 물의 속성
				if (getItem().get_defense_water() != 0) {
					os.writeRegistWater(getItem().get_defense_water());
				}
				// 바람 속성
				if (getItem().get_defense_wind() != 0) {
					os.writeRegistWind(getItem().get_defense_wind());
				}
				// 땅의 속성
				if (getItem().get_defense_earth() != 0) {
					os.writeRegistEarth(getItem().get_defense_earth());
				}
				
				
				switch (getAttrEnchantLevel()) {
				case 1:
					os.writeC(39);
					os.writeS("\\fI불 속성 대미지: \\aA+1");
					break; // 화령1단
				case 2:
					os.writeC(39);
					os.writeS("\\fI불 속성 대미지: \\aA+3");
					break; // 화령2단
				case 3:
					os.writeC(39);
					os.writeS("\\fI불 속성 대미지: \\aA+5");
					break; // 화령3단 (불의속성)
				case 4:
					os.writeC(39);
					os.writeS("\\fI불 속성 대미지: \\aA+7");
					break; // 화령4단
				case 5:
					os.writeC(39);
					os.writeS("\\fI불 속성 대미지: \\aA+9");
					break; // 화령5단
				case 6:
					os.writeC(39);
					os.writeS("\\fI물 속성 대미지: \\aA+1");
					break; // 수령1단
				case 7:
					os.writeC(39);
					os.writeS("\\fI물 속성 대미지: \\aA+3");
					break; // 수령2단
				case 8:
					os.writeC(39);
					os.writeS("\\fI물 속성 대미지: \\aA+5");
					break; // 수령3단 (물의속성)
				case 9:
					os.writeC(39);
					os.writeS("\\fI물 속성 대미지: \\aA+7");
					break; // 수령4단
				case 10:
					os.writeC(39);
					os.writeS("\\fI물 속성 대미지: \\aA+9");
					break; // 수령5단
				case 11:
					os.writeC(39);
					os.writeS("\\fI바람 속성 대미지: \\aA+1");
					break; // 풍령1단
				case 12:
					os.writeC(39);
					os.writeS("\\fI바람 속성 대미지: \\aA+3");
					break; // 풍령2단
				case 13:
					os.writeC(39);
					os.writeS("\\fI바람 속성 대미지: \\aA+5");
					break; // 풍령3단 (바람의속성)
				case 14:
					os.writeC(39);
					os.writeS("\\fI바람 속성 대미지: \\aA+7");
					break; // 풍령4단
				case 15:
					os.writeC(39);
					os.writeS("\\fI바람 속성 대미지: \\aA+9");
					break; // 풍령5단
				case 16:
					os.writeC(39);
					os.writeS("\\fI땅 속성 대미지: \\aA+1");
					break; // 지령1단
				case 17:
					os.writeC(39);
					os.writeS("\\fI땅 속성 대미지: \\aA+3");
					break; // 지령2단
				case 18:
					os.writeC(39);
					os.writeS("\\fI땅 속성 대미지: \\aA+5");
					break; // 지령3단 (땅의속성)
				case 19:
					os.writeC(39);
					os.writeS("\\fI땅 속성 대미지: \\aA+7");
					break; // 지령4단
				case 20:
					os.writeC(39);
					os.writeS("\\fI땅 속성 대미지: \\aA+9");
					break; // 지령5단
				default:
					break;
				}
				

				if (getItem().getMagicName() != null) {
					os.writeC(74);
					os.writeS(getItem().getMagicName());
				}

				if (itemType2 == 2) {
					int pierce = getItem().getSpecialPierce(eKind.ABILITY);
					if (pierce > 0) {
						os.writeC(122);
						os.writeC(pierce);
					}

					pierce = getItem().getSpecialPierce(eKind.SPIRIT);
					if (pierce > 0) {
						os.writeC(123);
						os.writeC(pierce);
					}

					pierce = getItem().getSpecialPierce(eKind.DRAGON_SPELL);
					if (pierce > 0) {
						os.writeC(124);
						os.writeC(pierce);
					}

					pierce = getItem().getSpecialPierce(eKind.FEAR);
					if (pierce > 0) {
						os.writeC(125);
						os.writeC(pierce);
					}

					pierce = getItem().getSpecialPierce(eKind.ALL);
					if (pierce > 0) {
						os.writeC(126);
						os.writeC(pierce);
					}
				}

				// 정령내성
				int resistance = getItem().getSpecialResistance(eKind.SPIRIT);
				if (resistance > 0) {
					os.writeC(118);
					os.writeC(resistance);
				}

				// 용언내성
				resistance = getItem().getSpecialResistance(eKind.DRAGON_SPELL);
				if (resistance > 0) {
					os.writeC(119);
					os.writeC(resistance);
				}

				// 공포내성
				resistance = getItem().getSpecialResistance(eKind.FEAR);
				if (resistance > 0) {
					os.writeC(120);
					os.writeC(resistance);
				}

				// 전체내성
				resistance = getItem().getSpecialResistance(eKind.ALL);
				if (resistance > 0) {
					os.writeC(121);
					os.writeC(resistance);
				}

				// 기술내성
				resistance = getItem().getSpecialResistance(eKind.ABILITY);
				int lvl = getEnchantLevel();
				
				
				/** 스냅퍼의 반지 스턴내성 표시 **/
				if ((itemId >= 22224 && itemId <= 22228) || (itemId >= 222290 && itemId <= 222291)
						|| (itemId >= 222330 && itemId <= 222336)) {
					if (lvl == 6)
						resistance += 5;
					else if (lvl == 7)
						resistance += 7;
					else if (lvl == 8)
						resistance += 9;
				} else if (getEnchantLevel() >= 7 && itemType2 == 2
						&& (getItem().get장신구처리() == 8 || getItem().get장신구처리() == 12)) {
					int tempval = (lvl - 5);
					if (tempval > 4)
						tempval = 4;
					resistance += tempval;
				}
				if (resistance > 0) {
					os.writeC(117);
					os.writeC(resistance);
				}

				if (get_bless_level() != 0) {
					os.writeC(39);
					int type = getItem().getType();
					if (type == 7 || type == 16 || type == 17) {
						os.writeS("\\fI축복 옵션: \\aASP +" + this.get_bless_level());
					} else {
						os.writeS("\\fI축복 옵션: \\aA대미지 +" + this.get_bless_level());
					}
				}
				
				

				/** 특수 인챈트 시스템 **/
				if (getItem().getType2() != 0 && get_item_level() != 0) {
					switch (get_item_level()) {
					case 1:
						os.writeC(73);
						os.writeS("\\fI특수 옵션: \\aA1단계 마법");
						break;
					case 2:
						os.writeC(73);
						os.writeS("\\fI특수 옵션: \\aA2단계 마법");
						break;
					case 3:
						os.writeC(73);
						os.writeS("\\fI특수 옵션: \\aA3단계 마법");
						break;
					case 4:
						os.writeC(73);
						os.writeS("\\fI특수 옵션: \\aA4단계 마법");
						break;
					default:
						break;
					}
				}
				/** 특수 인챈트 시스템 **/

				if (getItemId() == 20100 || getItemId() == 20099 || getItemId() == 20150 || getItemId() == 22258
						|| getItemId() == 20118 || getItemId() == 20151 || getItemId() == 22301
						|| getItemId() == 20091) { // 세트아이템
					os.writeC(69); // -- 셋트아이템에 대한 표기 ( 고정값 )
					os.writeC(get_main_set_armor() ? 1 : 2); // -- 1: 셋트 아이템 다
																// 착용시 /
																// 2: 셋트 아이템 미
																// 착용시
					// os.writeC(71); // -- [ 셋트 보너스 ] //더미값지정 본섭화
					switch (getItemId()) {
					case 20100:
						os.writeC(71);
						os.writeH(23929);// 검은 데스나이트
						os.writeAddAc(10);
						os.writeaSTR_Bu(2);
						os.writeShortDMG(2);
						break;
					case 20099:
						os.writeC(71);
						os.writeH(4241);// 데몬
						os.writeAddAc(2);
						os.writeAddHPPrecovery(5);
						break;
					case 22258:
						os.writeAddAc(5);
						os.writeAddMR(15);
						os.writeAddMaxHP(100);
						os.writeAddHPPrecovery(8);
						os.writeAddMPPrecovery(4);
						os.writeRegistWater(20);
						break;
					case 20150:
						os.writeC(71);
						os.writeH(23926);// 커츠
						os.writeAddAc(10);
						os.writeaCON_Bu(2);
						os.writeAddMaxHP(100);
						os.writeDMGdown(2);
						break;
					case 20118:
						os.writeC(71);
						os.writeH(23928);// 켄라우헬
						os.writeAddAc(3);
						os.writeAddMPPrecovery(18);
						break;
					case 20151:
						os.writeC(71);
						os.writeH(23927);// 케레니스
						os.writeaINT_Bu(2);
						os.writeAddMaxHP(100);
						os.writeAddMPPrecovery(18);
						break;
					case 22301:
						os.writeAddAc(3);
						os.writeAddHPPrecovery(4);
						os.writeAddMPPrecovery(1);
						break;
					case 20091:// 강철세트
						os.writeAddAc(3);
						break;
					}
					os.writeC(69); // -- dummy
					os.writeC(0x00); // -- dummy
				}
			}

			// 원거리 치명타
			if (itemId == 900082) {
				switch (getEnchantLevel()) {
				case 6:
					os.writeLongCritical(1);
					break;
				case 7:
					os.writeLongCritical(3);
					break;
				case 8:
					os.writeLongCritical(5);
					break;
				}
			}
			
			
			/** 엘릭서 룬 PVP마법 대미지 감소 **/
			if (itemId >= 900135 && itemId <= 900139){ // 80룬
				os.writeC(135); os.writeD(getItem().get_PVPMagicDamageReduction() + 1);
			} else if (itemId >= 900140 && itemId <= 900144){ // 85룬
				os.writeC(135); os.writeD(getItem().get_PVPMagicDamageReduction() + 2);
			} else if (itemId >= 900145 && itemId <= 900149){ // 90룬
				os.writeC(135); os.writeD(getItem().get_PVPMagicDamageReduction() + 3);
			}
			/** PVP 마법 데미지 감소 **/
			else if (getItem().get_PVPMagicDamageReduction() != 0) { 
				os.writeC(135); os.writeD(getItem().get_PVPMagicDamageReduction());
				
			
			}
			

			// 근거리 치명타
			if (itemId == 203019) { /** 포르세의 검 **/
				if (getEnchantLevel() >= 0 && getEnchantLevel() <= 15)
					os.writeShortCritical(10);
			} else if (itemId == 900081) {
				switch (getEnchantLevel()) {
				case 6:
					os.writeShortCritical(1);
					break;
				case 7:
					os.writeShortCritical(3);
					break;
				case 8:
					os.writeShortCritical(5);
					break;
				}
			}

			/** 라이브 형식 injection **/
			if (itemId == 40312 && getEndTime() != null) {
				os.writeC(112);
				os.writeD((getEndTime().getTime() / 1000));
			} else if (getItem().isEndedTimeMessage() && getEndTime() != null) {
				int remainSeconds = (int)((getEndTime().getTime() - 1483196400065L) / 1000);
				os.writeC(61);
				os.writeD(remainSeconds * 6);				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return os.getBytes();
	}

	private L1PcInstance _owner;

	class EnchantTimer implements Runnable {

		private int _skillId;
		private boolean _active = true;
		private boolean _effectClear = false;
		private long _expireTime;

		public EnchantTimer(int skillId, long expireTime) {
			_skillId = skillId;
			_expireTime = expireTime;
		}

		public int getRemainTime() {
			int remainTime = (int) (_expireTime - System.currentTimeMillis()) / 1000;

			if (remainTime < 1) {
				remainTime = 1;
			}

			return remainTime;
		}

		@Override
		public void run() {
			try {
				if (!_active) {
					return;
				}

				ClearEffect();
			} catch (Exception e) {
			}
		}

		public void cancel() {
			_active = false;
			ClearEffect();
			if (_owner != null)
				_owner.sendPackets(new S_ServerMessage(308, getLogName()));
		}

		public void ClearEffect() {
			synchronized (this) {
				if (_effectClear) {
					return;
				}

				_effectClear = true;
			}

			switch (_skillId) {
			case L1SkillId.ENCHANT_WEAPON:
				if (_owner != null)
					_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 747, 0, _isSecond, false));
				addDmgByMagic(-2);
				break;
			case L1SkillId.BLESS_WEAPON:
				addDmgByMagic(-2);
				addHitByMagic(-2);
				break;
			case L1SkillId.SHADOW_FANG:
				if (_owner != null)
					_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 2951, 0, false, false));
				addDmgByMagic(-5);
				break;
			case L1SkillId.BLESSED_ARMOR:
				if (_owner != null && _owner.getInventory().getItem(L1ItemInstance.this.getId()) == L1ItemInstance.this
						&& isEquipped()) {
					_owner.getAC().addAc(3);
					_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 748, 0, false, false));
					_owner.sendPackets(new S_OwnCharStatus(_owner));
				}
				addAcByMagic(-3);
				break;
			default:
				break;
			}
			removeSkillEffectTimer(_skillId);
		}
	}

	private int _acByMagic = 0;

	public int getAcByMagic() {
		return _acByMagic;
	}

	public void addAcByMagic(int i) {
		_acByMagic += i;
	}

	private int _dmgByMagic = 0;

	public int getDmgByMagic() {
		return _dmgByMagic;
	}

	public void addDmgByMagic(int i) {
		_dmgByMagic += i;
	}

	private int _holyDmgByMagic = 0;

	public int getHolyDmgByMagic() {
		return _holyDmgByMagic;
	}

	public void addHolyDmgByMagic(int i) {
		_holyDmgByMagic += i;
	}

	private int _hitByMagic = 0;

	public int getHitByMagic() {
		return _hitByMagic;
	}

	public void addHitByMagic(int i) {
		_hitByMagic += i;
	}

	public void setSkillArmorEnchant(L1PcInstance pc, int skillId, int skillTime) {

		if (getItem().getType2() != 2 || getItem().getType() != 2) {
			return;
		}

		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);

		killSkillEffectTimer(skillId);

		switch (skillId) {
		case L1SkillId.BLESSED_ARMOR: {
			addAcByMagic(3);
			setEnchantMagic(skill.getCastGfx());

			if (_owner != null && _owner.getInventory().getItem(L1ItemInstance.this.getId()) == L1ItemInstance.this
					&& isEquipped()) {
				_owner.getAC().addAc(-3);
				_owner.sendPackets(new S_OwnCharStatus(_owner));
			}
		}
			break;
		}
		EnchantTimer timer = new EnchantTimer(skillId, System.currentTimeMillis() + skillTime);
		_skillEffect.put(skillId, timer);

		GeneralThreadPool.getInstance().schedule(timer, skillTime);
	}

	public void setSkillWeaponEnchant(L1PcInstance pc, int skillId, int skillTime) {

		if (getItem().getType2() != 1) {
			return;
		}

		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);

		killSkillEffectTimer(skillId);

		switch (skillId) {
		case L1SkillId.ENCHANT_WEAPON:
			if (_owner != null && _owner.getInventory().getItem(L1ItemInstance.this.getId()) == L1ItemInstance.this
					&& isEquipped()) {
				_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 747, skillTime, _isSecond, false));
			}
			addDmgByMagic(2);
			break;
		case L1SkillId.BLESS_WEAPON:
			addDmgByMagic(2);
			addHitByMagic(2);
			break;
		case L1SkillId.SHADOW_FANG:
			addDmgByMagic(5);
			break;

		default:
			break;
		}

		EnchantTimer timer = new EnchantTimer(skillId, System.currentTimeMillis() + skillTime);
		_skillEffect.put(skillId, timer);

		GeneralThreadPool.getInstance().schedule(timer, skillTime);

		setEnchantMagic(skill.getCastGfx());
		if (skillId == L1SkillId.HOLY_WEAPON) {
			setEnchantMagic(2165);
		}
	}

	private int _enchantmagic = 0;

	public int getEnchantMagic() {
		return _enchantmagic;
	}

	public void setEnchantMagic(int i) {
		_enchantmagic = i;
	}

	protected void removeSkillEffectTimer(int skillId) {
		_skillEffect.remove(skillId);
	}

	public boolean hasSkillEffectTimer(int skillId) {
		return _skillEffect.containsKey(skillId);
	}

	protected void killSkillEffectTimer(int skillId) {
		EnchantTimer timer = _skillEffect.remove(skillId);
		if (timer != null) {
			timer.cancel();
		}
	}

	public int getSkillEffectTimeSec(int skillId) {
		EnchantTimer timer = _skillEffect.get(skillId);
		if (timer == null) {
			return -1;
		}
		return timer.getRemainTime();
	}

	private L1PcInstance _itemOwner;

	public L1PcInstance getItemOwner() {
		return _itemOwner;
	}

	public void setItemOwner(L1PcInstance pc) {
		_itemOwner = pc;
	}

	public void startItemOwnerTimer(L1PcInstance pc) {
		setItemOwner(pc);
		L1ItemOwnerTimer timer = new L1ItemOwnerTimer(this, 10000);
		timer.begin();
	}

	private L1EquipmentTimer _equipmentTimer;

	public void startEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer = new L1EquipmentTimer(pc, this, 1000);
			GeneralThreadPool.getInstance().schedule(_equipmentTimer, 1000);
		}
	}

	public void stopEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer.cancel();
			_equipmentTimer = null;
		}
	}

	private boolean _isNowLighting = false;

	public boolean isNowLighting() {
		return _isNowLighting;
	}

	public void setNowLighting(boolean flag) {
		_isNowLighting = flag;
	}

	private int _DropMobId = 0;

	public int isDropMobId() {
		return _DropMobId;
	}

	public void setDropMobId(int i) {
		_DropMobId = i;
	}

	private int _keyId = 0;

	public int getKeyId() {
		return _keyId;
	}

	public void setKeyId(int i) {
		_keyId = i;
	}

	public void onEquip(L1PcInstance pc) {
		_owner = pc;
	}

	public void onUnEquip() {
		_owner = null;
	}

	private boolean armor_set;

	public boolean get_armor_set() {
		return armor_set;
	}

	public void set_armor_set(boolean b) {
		this.armor_set = b;
	}

	private boolean main_set_armor;

	public boolean get_main_set_armor() {
		return main_set_armor;
	}

	public void set_main_set_armor(boolean b) {
		this.main_set_armor = b;
	}

	private int bless_level;

	public int get_bless_level() {
		return bless_level;
	}

//	public void set_bless_level(int i) {
//		this.bless_level = CommonUtil.get_current(i, 0, 3);
//	}
	
	// 빛나는 축복의 돌 (7011: 축복 추타/SP 몇줄지)
	public void set_bless_level(int i) {
		this.bless_level = CommonUtil.get_current(i, 0, 5);
	}

	/** 특수 인챈트 시스템 **/
	private int item_level;

	public int get_item_level() {
		return item_level;
	}

	public void set_item_level(int i) {
		this.item_level = i;
	}

	/** 특수 인챈트 시스템 **/

	private String _Hotel_Town;

	public String getHotel_Town() {
		return _Hotel_Town;
	}

	public void setHotel_Town(String name) {
		_Hotel_Town = name;
	}

	public ArrayList<L1ItemBookMark> _bookmarks;

	public ArrayList<L1ItemBookMark> getBookMark() {
		return _bookmarks;
	}

	public void addBookMark(L1ItemBookMark list) {
		_bookmarks.add(list);
	}

	public byte[] getStatusBytes(L1PcInstance pc) {
		int itemType2 = getItem().getType2();
		int itemId = getItemId();
		ItemPresentOutStream os = new ItemPresentOutStream();

		if (itemType2 == 0) { // etcitem
			switch (getItem().getType()) {
			case 2: // light
				os.writeC(22);
				os.writeH(getItem().getLightRange());
				break;
			case 7: // food
				os.writeC(21);
				os.writeH(getItem().getFoodVolume());
				break;
			case 0: // arrow
			case 15: // sting
				os.writeC(1);
				os.writeC(getItem().getDmgSmall());
				os.writeC(getItem().getDmgLarge());
				break;
			default:
				os.writeC(23);
				break;
			}
			os.writeC(getItem().getMaterial());
			os.writeD(getWeight());

		} else if (itemType2 == 1 || itemType2 == 2) { // weapon | armor
			if (itemType2 == 1) { // weapon 무기 타격치
				os.writeC(1);
				os.writeC(getItem().getDmgSmall());
				os.writeC(getItem().getDmgLarge());
				os.writeC(getItem().getMaterial());
				os.writeD(getWeight());
			} else if (itemType2 == 2) { // armor
				os.writeC(19);
				int ac = ((L1Armor) getItem()).get_ac();
				int Grade = ((L1Armor) getItem()).getGrade();
				if (ac < 0) {
					ac = ac - ac - ac;
				}
				os.writeC(ac - get_durability());
				os.writeC(getItem().getMaterial());
				os.writeH(-1);
				os.writeD(getWeight());
			}

			if (getEnchantLevel() != 0 && !(itemType2 == 2 && getItem().getGrade() >= 0)) {
				os.writeC(2);
				os.writeC(getEnchantLevel());
			}
			if (getItem().isTwohandedWeapon()) { // 양손무기
				os.writeC(4);
			}
			if (get_durability() != 0) { // 손상도
				os.writeC(3);
				os.writeC(get_durability());
			}

			if(getEnchantLevel() != 0 
					/** 집행급무기 **/ //itempresent에 있으니깐
					&&itemId == 61 || itemId == 134 || itemId == 86 || itemId == 12 || itemId == 66 
					|| itemId == 202011 || itemId == 202012 || itemId == 202013 || itemId == 202014
					|| itemId == 2944 || itemId == 217 || itemId == 2945 // 아인하사드 섬광, 기르타스의 검, 그랑카인의 심판
					 
					/** 지배자 무기 **/
					|| itemId == 350010 || itemId == 350011 || itemId == 350012 || itemId == 350013
					|| itemId == 350014 || itemId == 350015 || itemId == 350016 || itemId == 350017 || itemId == 202022
					){
				os.writeC(2);
				os.writeC(getEnchantLevel() * 2);
			} 
			
			
			/** 클래스 착용 부분 **/
			int bit = 0;
			bit |= getItem().isUseRoyal() ? 1 : 0;
			bit |= getItem().isUseKnight() ? 2 : 0;
			bit |= getItem().isUseElf() ? 4 : 0;
			bit |= getItem().isUseMage() ? 8 : 0;
			bit |= getItem().isUseDarkelf() ? 16 : 0;
			bit |= getItem().isUseDragonKnight() ? 32 : 0;
			bit |= getItem().isUseBlackwizard() ? 64 : 0;
			bit |= getItem().isUse전사() ? 128 : 0;
			os.writeC(7);
			os.writeC(bit);

			os.writeC(130);
			if (!getItem().isTradable()) {
				os.writeD(6);
			} else {
				os.writeD(7);
			}

			if (isCanbeDmg()) {
				os.writeC(131);
				os.writeD(1); // 비손상
			}

			os.writeC(132);
			os.writeD(3); // 성별 구분 1.남자 2.여자 3.전체

			if (itemType2 == 1 && isUndeadDmg()) {
				os.writeC(114);
				os.writeD(1); // 언데드
			}

			/** 55레벨 엘릭서 룬 옵션 표시 **/
			if (itemId == 222295) { // 민첩의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					break;
				case 1:
				case 7:
					os.writeC(31);
					os.writeH(50);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					break;
				}
			}
			if (itemId == 222296) { // 체력의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					break;
				case 1:
				case 7:
					os.writeC(31);
					os.writeH(50);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					break;
				}
			}
			if (itemId == 222297) { // 지식의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					break;
				case 1:
				case 7:
					os.writeC(31);
					os.writeH(50);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					break;
				}
			}
			if (itemId == 222298) { // 지혜의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					break;
				case 1:
				case 7:
					os.writeC(31);
					os.writeH(50);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					break;
				}
			}
			if (itemId == 222299) { // 힘의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					break;
				case 1:
				case 7:
					os.writeC(31);
					os.writeH(50);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					break;
				case 5:
					os.writeC(5);
					os.writeC(3);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					break;
				}
			}
			/** 70레벨 엘릭서 룬 옵션 표시 **/
			if (itemId == 222312) { // 민첩의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					break;
				case 5:
					os.writeC(5);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(32);
					os.writeH(50);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					break;
				}
			}
			if (itemId == 222313) { // 체력의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					break;
				}
			}
			if (itemId == 222314) { // 지식의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					break;
				}
			}
			if (itemId == 222315) { // 지혜의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					break;
				}
			}
			if (itemId == 222316) { // 힘의 엘릭서
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					break;
				case 4:
					os.writeAddAc(3);
					os.writeC(32);
					os.writeH(30);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					break;
				}
			}
			// TODO 80레벨 엘릭서 룬 옵션 표시
			if (itemId == 900135) {
				// System.out.println(pc.getType());
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaBlesssomo(5);
					break;
				case 1:// 기사
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 4:// 다엘
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(5);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(32);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900136) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900137) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900138) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900139) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaBlesssomo(5);
					break;
				}
			}
			// TODO 85레벨 엘릭서 룬 옵션 표시
			if (itemId == 900140) {
				// System.out.println(pc.getType());
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 1:// 기사
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(5);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(32);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900141) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900142) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900143) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900144) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(5);
					os.writeaBlesssomo(5);
					break;
				}
			}
			// TODO 90레벨 엘릭서 룬 옵션 표시
			if (itemId == 900145) {
				// System.out.println(pc.getType());
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 1:// 기사
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(5);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(32);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900146) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900147) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900148) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				}
			}
			if (itemId == 900149) {
				switch (pc.getType()) {
				case 0:
					os.writeC(63);
					os.writeC(3);
					os.writeC(48);
					os.writeC(2);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 1:
					os.writeC(31);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 2:
					os.writeC(32);
					os.writeH(50);
					os.writeC(47);
					os.writeC(1);
					os.writeC(35);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 3:
					os.writeC(38);
					os.writeC(3);
					os.writeC(17);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 4:
					os.writeC(19);
					os.writeC(3);
					os.writeC(32);
					os.writeH(30);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 5:
					os.writeC(48);
					os.writeC(3);
					os.writeC(63);
					os.writeC(1);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 6:
					os.writeC(68);
					os.writeC(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				case 7:
					os.writeC(15);
					os.writeH(5);
					os.writeC(31);
					os.writeH(50);
					os.writeaAll_pierce(10);
					os.writeaBlesssomo(5);
					break;
				}
			}
		}
		return os.getBytes();
	}

	public int getAttrEnchantBit(int attr) {
		int attr_bit = 0;
		int result_bit = 0;
		if (attr >= 1 && attr <= 5) {
			attr_bit = 1;
		}
		if (attr >= 6 && attr <= 10) {
			attr_bit = 2;
			attr = attr - 5;
		}
		if (attr >= 11 && attr <= 15) {
			attr_bit = 3;
			attr = attr - 10;
		}
		if (attr >= 16 && attr <= 20) {
			attr_bit = 4;
			attr = attr - 15;
		}

		if (attr > 0) {
			result_bit = attr_bit + (16 * attr);
		}

		return result_bit;
	}

	private int _openEffect = 0;

	public void setOpenEffect(int i) {
		_openEffect = i;
	}

	public int getOpenEffect() {
		return _openEffect;
	}

	public byte[] serialize() {
		byte[] data = null;
		MJBytesOutputStream os = null;
		try {
			os = new MJBytesOutputStream(128);
			os.write(0x08); // object_id
			os.writeBit(getId());
			os.write(0x10); // name_id
			os.writeBit(_item.getItemDescId());
			os.write(0x18); // db_id
			os.writeBit(getId());
			os.write(0x20); // count
			os.writeBit(_count);
			/*
			 * os.write(0x28); // interact_type os.writeBit(_item.getUseType());
			 * os.write(0x30); // number_of_use os.writeBit(0);
			 */
			os.write(0x28);
			os.writeBit(_item.getUseType());

			os.write(0x38); // icon_id
			os.writeBit(_item.getGfxId());
			os.write(0x40); // bless_code_for_display
			os.writeBit(bless);

			/**
			 * 2 : 교환 불가 4 : 삭제 불가 8 : 인챈 불가 16 : 창고 보관 가능 32 : 봉인 64 : 특수 봉인
			 **/
			os.write(0x48); // attribute_bit_set
			int bit = (!_item.isTradable() ? 2 : 16) | (_item.isCantDelete() ? 4 : 0)
					| (_item.get_safeenchant() < 0 ? 8 : 0) | (isIdentified() ? 1 : 0);
			os.writeBit(bit);

			os.write(0x50); // attribute_bit_set_ex(usetype)
			if (_item.isEndedTimeMessage())
				os.writeBit(0x01); // 각인
			else
				os.writeBit(0x08); // 낚시
			os.write(0x58); // is_timeout
			os.writeB(false);
			/*
			 * os.write(0x60); // category os.writeBit(0x1); os.write(0x68); //
			 * enchant os.writeBit(1);
			 */

			/**
			 * 0:창고불가 2:특수가능 3:개인/특수가능 7:개인/혈/특수가능
			 **/
			os.write(0x70); // deposit
			os.write(0x03);
			/*
			 * os.write(0x78); // overlay_surf_id os.writeBit(0);
			 * os.writeBit(0x80); // elemental_enchant_type os.writeBit(0x01);
			 * os.writeBit(0x88); // elemental_enchant_value os.writeBit(0x05);
			 */
			/*
			 * os.writeBit(0x8A); // description byte[] tmp = getStatusBytes();
			 * os.writeBit(tmp.length); os.write(tmp); tmp = null;
			 */
			os.writeBit(0x92); // extra_description
			os.writeS2(getViewName());
			/*
			 * os.writeBit(0x92); // description os.writeS2(getViewName());
			 * if(isIdentified()){ os.writeBit(0x9A); // extra_description
			 * byte[] tmp = getStatusBytes(); os.writeBit(tmp.length);
			 * os.write(tmp); tmp = null; } os.writeBit(0xA0); //
			 * left_time_for_pre_notify os.writeBit(100);
			 */
			data = os.toArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			os.close();
			os.dispose();
		}
		return data;
	}

	public byte[] serializeFishingItem() {
		byte[] data = null;
		MJBytesOutputStream os = null;
		try {
			os = new MJBytesOutputStream(128);
			os.write(0x08); // object_id
			os.writeBit(getId());
			os.write(0x10); // name_id
			os.writeBit(_item.getItemDescId());
			os.write(0x18); // db_id
			os.writeBit(getId());
			os.write(0x20); // count
			os.writeBit(_count);
			os.write(0x28);
			os.writeBit(_item.getUseType());
			os.write(0x38); // icon_id
			os.writeBit(_item.getGfxId());
			os.write(0x40); // bless_code_for_display
			os.writeBit(bless);
			os.write(0x48); // attribute_bit_set
			int bit = (!_item.isTradable() ? 2 : 16) | (_item.isCantDelete() ? 4 : 0)
					| (_item.get_safeenchant() < 0 ? 8 : 0) | (isIdentified() ? 1 : 0);
			os.writeBit(bit);

			os.write(0x50); // attribute_bit_set_ex(usetype)

			if (_item.isEndedTimeMessage())
				os.writeBit(0x01); // 각인
			else
				os.writeBit(0x08); // 낚시

			os.write(0x58); // is_timeout
			os.writeB(false);

			os.write(0x70); // deposit
			/**
			 * 0:창고불가 2:특수가능 3:개인/특수가능 7:개인/혈/특수가능
			 **/
			os.writeBit(3);

			os.writeBit(0x92); // extra_description
			os.writeS2(getViewName());
			if (isIdentified()) {
				os.writeBit(0x9A); // extra_description
				byte[] tmp = getStatusBytes();
				os.writeBytes(tmp);
			}
			data = os.toArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			os.close();
			os.dispose();
		}
		return data;
	}

	private int getRuneAc() {
		int ac = getItem().get_ac();

		if ((getItemId() >= 900135 && getItemId() <= 900149) || (getItemId() >= 222295 && getItemId() <= 222316)) {
			if (_cha != null && _cha.getType() == 4) {
				ac += 3;
			}
		}

		return ac;
	}

	private static int _instanceType = -1;

	@Override
	public int getL1Type() {
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_ITEMINSTANCE : _instanceType;
	}

	private boolean m_is_give = false;

	public boolean isGiveItem() {
		return m_is_give;
	}

	public void setGiveItem(boolean is_give) {
		m_is_give = is_give;
	}

	public boolean isCanbeDmg() {
		boolean result = getItem().get_canbedmg() == 0 ? true : false;

		return result;
	}

	public boolean isUndeadDmg() {
		boolean result = false;

		if (getItem().getMaterial() == 14 || getItem().getMaterial() == 17 || getItem().getMaterial() == 22)
			result = true;

		return result;
	}
	public int getStatusBit() {
		int b = 0;
		if (!getItem().isTradable()) {
			b += 2;
		}
		if (getItem().isCantDelete()) {
			b += 4;
		}
		if (getItem().get_safeenchant() < 0) {
			b += 8;
		}
		int bless = getBless();
		if (bless >= 128) {
			b = 46;
		}
		if (isIdentified()) {
			b++;
		}
		return b;
	}

	public int getTradeBit() {
		int b = 2;
		if (getBless() >= 128) {
			b = 3;
		} else if (!getItem().isTradable()) {
			b = 3;
		} else if (getItem().isTradable()) {
			b = 7;
		}
		return b;
	}
}
