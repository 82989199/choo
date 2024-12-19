package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.STRIKER_GALE;
import static l1j.server.server.model.skill.L1SkillId.MIRROR_IMAGE;
import static l1j.server.server.model.skill.L1SkillId.UNCANNY_DODGE;
import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.ARMOR_BRAKE;
import static l1j.server.server.model.skill.L1SkillId.BRAVE_AURA;
import static l1j.server.server.model.skill.L1SkillId.BURNING_SLASH;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_12;
import static l1j.server.server.model.skill.L1SkillId.BURNING_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.CLAN_BUFF3;
import static l1j.server.server.model.skill.L1SkillId.CLAN_BUFF4;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_S;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_1;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_20_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_21_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_23_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_3_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_4_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_5_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_9_S;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_1;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.DOUBLE_BRAKE;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.EARTH_GUARDIAN;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.ENCHANT_VENOM;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_B;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_AVATAR;
import static l1j.server.server.model.skill.L1SkillId.PAP_FIVEPEARLBUFF;
import static l1j.server.server.model.skill.L1SkillId.PAP_MAGICALPEARLBUFF;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.PHANTASM;
import static l1j.server.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static l1j.server.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FREEZE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_MITHRIL_POWDER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HOLY_WATER_OF_EVA;
import static l1j.server.server.model.skill.L1SkillId.WIDE_ARMORBREAK;

import java.util.Random;

import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.ArmorClass.MJArmorClass;
import l1j.server.CharmSystem.CharmModelLoader;
import l1j.server.CharmSystem.Model.CharmSkillModel;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJItemSkillSystem.MJItemSkillModelLoader;
import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Action.MJAttackChain;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.datatables.CharacterBalance;
import l1j.server.server.datatables.CharacterHitRate;
import l1j.server.server.datatables.CharacterReduc;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.datatables.UserProtectMonsterTable;
import l1j.server.server.datatables.WeaponAddDamage;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.gametime.L1GameTimeClock;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Attack;
import l1j.server.server.serverpackets.S_AttackCritical;
import l1j.server.server.serverpackets.S_AttackMissPacket;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_AttackPacketForNpc;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.templates.L1SpecialMap;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJCommons;

public class L1Attack {

	private L1PcInstance _pc = null;
	private L1Character _target = null;
	private L1PcInstance _targetPc = null;
	private L1NpcInstance _npc = null;
	private L1NpcInstance _targetNpc = null;
	private final int _targetId;
	private int _targetX;
	private int _targetY;
	private int _statusDamage = 0;
	private static final Random _random = new Random(System.nanoTime());
	private int _hitRate = 0;
	private int _calcType;
	private static final int PC_PC = 1;
	private static final int PC_NPC = 2;
	private static final int NPC_PC = 3;
	private static final int NPC_NPC = 4;
	public boolean _isHit = false;
	public boolean _isCritical = false;
	private int _damage = 0;
	private int _attckGrfxId = 0;
	private int _attckActId = 0;
	
	// 공격자가 플레이어의 경우의 무기 정보
	private L1ItemInstance weapon = null;

	// 전사 쌍수
	private L1ItemInstance Sweapon = null;// 세컨웨폰
	private int _SweaponId = 0;
	private int _SweaponType = 0;
	private int _SweaponType1 = 0;
	private int _SweaponAddHit = 0;
	private int _SweaponAddDmg = 0;
	private int _SweaponSmall = 0;
	private int _SweaponLarge = 0;
	private int _SweaponRange = 1;
	private int _SweaponBless = 1;
	private int _SweaponEnchant = 0;
	private int _SweaponMaterial = 0;
	private int _SweaponAttrEnchantLevel = 0;

	private int _weaponId = 0;
	private int _weaponType = 0;
	private int _weaponType2 = 0;
	// private int _weaponType1 = 0;
	private int _weaponAddHit = 0;
	private int _weaponAddDmg = 0;
	private int _weaponSmall = 0;
	private int _weaponLarge = 0;
	private int _weaponBless = 1;
	private int _weaponEnchant = 0;
	private int _weapon_bless_level = 0;
	private int _weaponMaterial = 0;
	private int _weaponDoubleDmgChance = 0;
	private int _weaponAttrLevel = 0; // 속성 레벨
	private int _attackType = 0;
	private L1ItemInstance _arrow = null;
	private L1ItemInstance _sting = null;
	private int _leverage = 10; // 1/10배로 표현한다.
	private double _baseDamage;
	private double _critical;
	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	private static final int[] strHit = new int[128];

	static {
		for (int str = 0; str <= 7; str++) {
			strHit[str] = -2;
		}
		strHit[8] = -1;
		strHit[9] = -1;
		strHit[10] = 0;
		strHit[11] = 0;
		strHit[12] = 1;
		strHit[13] = 1;
		strHit[14] = 2;
		strHit[15] = 2;
		strHit[16] = 3;
		strHit[17] = 3;
		strHit[18] = 4;
		strHit[19] = 4;
		strHit[20] = 4;
		strHit[21] = 5;
		strHit[22] = 5;
		strHit[23] = 5;
		strHit[24] = 6;
		strHit[25] = 6;
		strHit[26] = 6;
		strHit[27] = 7;
		strHit[28] = 7;
		strHit[29] = 7;
		strHit[30] = 8;
		strHit[31] = 8;
		strHit[32] = 8;
		strHit[33] = 9;
		strHit[34] = 9;
		strHit[35] = 9;
		strHit[36] = 10;
		strHit[37] = 10;
		strHit[38] = 10;
		strHit[39] = 11;
		strHit[40] = 11;
		strHit[41] = 11;
		strHit[42] = 12;
		strHit[43] = 12;
		strHit[44] = 12;
		strHit[45] = 13;
		strHit[46] = 13;
		strHit[47] = 13;
		strHit[48] = 14;
		strHit[49] = 14;
		strHit[50] = 14;
		strHit[51] = 15;
		strHit[52] = 15;
		strHit[53] = 15;
		strHit[54] = 16;
		strHit[55] = 16;
		strHit[56] = 16;
		strHit[57] = 17;
		strHit[57] = 17;
		strHit[58] = 17;
		int Hit = 18;
		for (int str = 59; str <= 127; str++) { // 59~127은 3마다＋1
			if (str % 3 == 1) {
				Hit++;
			}
			strHit[str] = Hit;
		}
	}

	private static final int[] dexHit = new int[128];

	static {
		// DEX 데미지 보정
		for (int dex = 0; dex <= 6; dex++) {
			// 0~11는 0
			dexHit[dex] = -2;
		}
		dexHit[7] = -1;
		dexHit[8] = -1;
		dexHit[9] = 0;
		dexHit[10] = 0;
		dexHit[11] = 1;
		dexHit[12] = 1;
		dexHit[13] = 2;
		dexHit[14] = 2;
		dexHit[15] = 3;
		dexHit[16] = 3;
		dexHit[17] = 4;
		dexHit[18] = 4;
		dexHit[19] = 5;
		dexHit[20] = 6;
		dexHit[21] = 7;
		dexHit[22] = 8;
		dexHit[23] = 9;
		dexHit[24] = 10;
		dexHit[25] = 11;
		dexHit[26] = 12;
		dexHit[27] = 13;
		dexHit[28] = 14;
		dexHit[29] = 15;
		dexHit[30] = 16;
		dexHit[31] = 17;
		dexHit[32] = 18;
		dexHit[33] = 19;
		dexHit[34] = 19;
		dexHit[35] = 19;
		dexHit[36] = 20;
		dexHit[37] = 20;
		dexHit[38] = 20;
		dexHit[39] = 21;
		dexHit[40] = 21;
		dexHit[41] = 21;
		dexHit[42] = 22;
		dexHit[43] = 22;
		dexHit[44] = 22;
		dexHit[45] = 23;
		dexHit[46] = 23;
		dexHit[47] = 23;
		dexHit[48] = 24;
		dexHit[49] = 24;
		dexHit[50] = 24;
		dexHit[51] = 25;
		dexHit[52] = 25;
		dexHit[53] = 25;
		dexHit[54] = 26;
		dexHit[55] = 26;
		dexHit[56] = 26;
		dexHit[57] = 27;
		dexHit[58] = 27;
		dexHit[59] = 27;
		dexHit[60] = 28;
		dexHit[61] = 28;
		dexHit[62] = 28;
		dexHit[63] = 29;
		dexHit[64] = 29;
		dexHit[65] = 29;
		dexHit[66] = 30;
		dexHit[67] = 30;
		dexHit[68] = 30;

		int hit = 31;
		for (int dex = 69; dex <= 127; dex++) { // 48~127은 3마다＋1 //#
			if (dex % 3 == 1) {
				hit++;
			}
			dexHit[dex] = hit;
		}
	}

	private static final int[] strDmg = new int[128];

	static {
		// STR 대미지 보정
		for (int str = 0; str <= 8; str++) {
			// 1~8는 -2
			strDmg[str] = -2;
		}
		for (int str = 9; str <= 10; str++) {
			// 9~10는 -1
			strDmg[str] = -1;
		}
		strDmg[11] = 0;
		strDmg[12] = 2;
		strDmg[13] = 3;
		strDmg[14] = 3;
		strDmg[15] = 4;
		strDmg[16] = 4;
		strDmg[17] = 5;
		strDmg[18] = 5;
		strDmg[19] = 6;
		strDmg[20] = 6;
		strDmg[21] = 7;
		strDmg[22] = 7;
		strDmg[23] = 8;
		strDmg[24] = 10;
		strDmg[25] = 12;
		strDmg[26] = 14;
		strDmg[27] = 16;
		strDmg[28] = 18;
		strDmg[29] = 20;
		strDmg[30] = 22;
		strDmg[31] = 24;
		strDmg[32] = 26;
		strDmg[33] = 28;
		strDmg[34] = 30;
		int dmg = 31;//31
		for (int str = 35; str <= 127; str++) { // 35~127은 3마다＋1
			if (str % 3 == 1) {//힘 3마다 1대미지
				dmg++;
			}
			strDmg[str] = dmg;
		}
	}

	private static final int[] dexDmg = new int[128];

	static {
		// DEX 대미지 보정
		for (int dex = 0; dex <= 14; dex++) {
			// 0~14는 0
			dexDmg[dex] = -1;
		}
		dexDmg[15] = 1;
		dexDmg[16] = 2;
		dexDmg[17] = 3;
		dexDmg[18] = 4;
		dexDmg[19] = 5;
		dexDmg[20] = 6;
		dexDmg[21] = 7;
		dexDmg[22] = 8;
		dexDmg[23] = 9;
		dexDmg[24] = 10;
		dexDmg[25] = 11;
		dexDmg[26] = 12;
		dexDmg[27] = 13;
		dexDmg[28] = 14;
		dexDmg[29] = 15;
		dexDmg[30] = 16;
		dexDmg[31] = 17;
		dexDmg[32] = 18;
		dexDmg[33] = 19;
		dexDmg[34] = 20;
		dexDmg[35] = 21;
		int dmg = 22;
		for (int dex = 36; dex <= 127; dex++) { // 36~127은 4마다＋1 //#
			if (dex % 4 == 1) {
				dmg++;
			}
			dexDmg[dex] = dmg;
		}
	}

	private static final int[] intDmg = new int[128];

	static {
		// int 대미지 보정
		for (int int1 = 0; int1 <= 14; int1++) {
			intDmg[int1] = 0;
		}
		intDmg[15] = 3;
		intDmg[16] = 4;
		intDmg[17] = 5;
		intDmg[18] = 6;
		intDmg[19] = 6;
		intDmg[20] = 7;
		intDmg[21] = 7;
		intDmg[22] = 8;
		intDmg[23] = 8;
		intDmg[24] = 9;
		intDmg[25] = 9;
		intDmg[26] = 9;
		intDmg[27] = 10;
		intDmg[28] = 10;
		intDmg[29] = 11;
		intDmg[30] = 11;
		intDmg[31] = 12;
		intDmg[32] = 12;
		intDmg[33] = 13;
		intDmg[34] = 13;
		int dmg = 25;
		for (int int1 = 35; int1 <= 127; int1++) { // 35~127은 1마다＋1
			dmg += 1;
			intDmg[int1] = dmg;
		}
	}

	public void setActId(int actId) {
		_attckActId = actId;
	}

	public void setGfxId(int gfxId) {
		_attckGrfxId = gfxId;
	}

	public int getActId() {
		return _attckActId;
	}

	public int getGfxId() {
		return _attckGrfxId;
	}
	
	public L1Attack(L1Character attacker, L1Character target) {
		if (attacker instanceof L1PcInstance) {
			_pc = (L1PcInstance) attacker;
			if (target instanceof L1PcInstance) {
				_targetPc = (L1PcInstance) target;
				_calcType = PC_PC;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				_calcType = PC_NPC;
			}
			// 무기 정보의 취득
			weapon = _pc.getWeaponSwap();
			Sweapon = _pc.getSecondWeapon();
			/*if (Sweapon != null) {
				_SweaponId = Sweapon.getItem().getItemId();
				_SweaponType = Sweapon.getItem().getType1();
				_SweaponAddHit = Sweapon.getItem().getHitModifier() + Sweapon.getHitByMagic();
				_SweaponAddDmg = Sweapon.getItem().getDmgModifier() + Sweapon.getDmgByMagic();
				if (C_ItemUSe.is_legend_weapon(_weaponId))
					_SweaponAddDmg *= 2;
				_SweaponType1 = Sweapon.getItem().getType();
				_SweaponSmall = Sweapon.getItem().getDmgSmall();
				_SweaponLarge = Sweapon.getItem().getDmgLarge();
				_SweaponBless = Sweapon.getItem().getBless();
				_SweaponEnchant = Sweapon.getEnchantLevel() - Sweapon.get_durability(); // 손상분
																						// 마이너스
				_SweaponMaterial = Sweapon.getItem().getMaterial();
				_SweaponAttrEnchantLevel = Sweapon.getAttrEnchantLevel();
			}*/
			
			if (_pc.is전사() && _pc.isPassive(MJPassiveID.SLAYER.toInt()) && _pc.getSecondWeapon() != null) {
				int ran = _random.nextInt(100) + 1;
				if (ran < 50) {
					weapon = Sweapon;
				}
			}
			
			
			if (weapon != null) {
				_weaponId = weapon.getItem().getItemId();
				_weaponType = weapon.getItem().getType1();
				_weaponType2 = weapon.getItem().getType();// 변경
				_weaponAddHit = weapon.getItem().getHitModifier() + weapon.getHitByMagic();
				_weaponAddDmg = weapon.getItem().getDmgModifier() + weapon.getDmgByMagic();
				if (C_ItemUSe.is_legend_weapon(_weaponId))
					_weaponAddDmg *= 2;

				_weaponSmall = weapon.getItem().getDmgSmall();
				_weaponLarge = weapon.getItem().getDmgLarge();
				_weaponBless = weapon.getItem().getBless();
				if (_weaponType == 0) {
					_weaponEnchant = 0;
				}
				if (_weaponType != 20 && _weaponType != 62) {
					_weaponEnchant = weapon.getEnchantLevel() - weapon.get_durability(); // 손상분
																							// 마이너스
				} else {
					_weaponEnchant = weapon.getEnchantLevel();
				}
				_weapon_bless_level = weapon.get_bless_level();
				_weaponMaterial = weapon.getItem().getMaterial();
				if (_weaponType == 20) { // 아로의 취득
					_arrow = _pc.getInventory().getArrow();
					if (_arrow != null) {
						_weaponBless = _arrow.getItem().getBless();
						_weaponMaterial = _arrow.getItem().getMaterial();
					}
				}
				if (_weaponType == 62) { // 스팅의 취득
					_sting = _pc.getInventory().getSting();
					if (_sting != null) {
						_weaponBless = _sting.getItem().getBless();
						_weaponMaterial = _sting.getItem().getMaterial();
					}
				}
				_weaponDoubleDmgChance = weapon.getItem().getDoubleDmgChance();
				_weaponAttrLevel = weapon.getAttrEnchantLevel();
			}
			// 스테이터스에 의한 추가 대미지 보정
			if (_weaponType == 20) { // 활의 경우는 DEX치 참조
				_statusDamage = dexDmg[_pc.getAbility().getTotalDex()];
			} else if (_weaponType2 == 17) {// 키링크는 인트영향
				_statusDamage = intDmg[_pc.getAbility().getTotalInt()];
			} else {
				_statusDamage = strDmg[_pc.getAbility().getTotalStr()];
			}
		} else if (attacker instanceof L1NpcInstance) {
			_npc = (L1NpcInstance) attacker;
			if (target instanceof L1PcInstance) {
				_targetPc = (L1PcInstance) target;
				_calcType = NPC_PC;
			} else if (target instanceof L1NpcInstance) {
				_targetNpc = (L1NpcInstance) target;
				_calcType = NPC_NPC;
			}
		}
		_target = target;
		_targetId = target.getId();
		_targetX = target.getX();
		_targetY = target.getY();
	}

	/* ■■■■■■■■■■■■■■■■ 명중 판정 ■■■■■■■■■■■■■■■■ */

	public boolean calcHit() {
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (_pc == null || _target == null)
				return _isHit;

			if(_target != null && _target instanceof MJDogFightInstance)
				return _isHit;
			
			// 키링크 일경우 상대방 앱솔일경우 무시
			if (_weaponType2 == 17) {
				if (_target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER) || MJCommons.isCounterMagic(_target)) {
					_isHit = false;
				} else {
					_isHit = true;
				}
				return _isHit;
			}
			
			if (_target.hasSkillEffect(L1SkillId.MOEBIUS)) {
				if (_weaponType == 20 || _weaponType == 62) {
					_isHit = false;
				} else {
					_isHit = true;
				}
				return _isHit;
			}

			/** 2016.11.26 MJ 앱센터 LFC **/
			if (_pc instanceof L1PcInstance) {
				/* lfc 게임인 래디중일때는 miss */
				if (_pc.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY)
					return false;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (!_pc.glanceCheck(_targetX, _targetY) || !_target.glanceCheck(_pc.getX(), _pc.getY()))
					return _isHit = false; // 공격자가 플레이어의 경우는 장애물 판정
			} else {
				if (_npc.glanceCheck(_targetX, _targetY) || _target.glanceCheck(_npc.getX(), _npc.getY()))
					return _isHit = false; // 공격자가 플레이어의 경우는 장애물 판정
			}

			if (_weaponType == 20 && _weaponId != 190 && _weaponId != 10000 && _weaponId != 202011 && _arrow == null) {
				_isHit = false; // 화살이 없는 경우는 미스
			} else if (_weaponType == 62 && _sting == null) {
				_isHit = false; // 스팅이 없는 경우는 미스
			} else if (_weaponId == 247 || _weaponId == 248 || _weaponId == 249) {
				_isHit = false; // 시련의 검B~C 공격 무효
			} else if (_calcType == PC_PC) {
				if (_pc.get_current_combat_id() == _targetPc.get_current_combat_team_id()
						&& _pc.get_current_combat_team_id() == _targetPc.get_current_combat_team_id()) {
					_isHit = false;
					return false;
				}

				_isHit = calcPcPcHit();
				if (_isHit == false) {
					/*_pc.sendPackets(new S_SkillSound(_target.getId(), 13418));// 이펙트
					_targetPc.sendPackets(new S_SkillSound(_target.getId(), 13418));// 이펙트
*/				}
			} else if (_calcType == PC_NPC) {
				/** 바포방 뚫어 방지 **/
				if (_pc.바포방 != true && _pc.getX() == 32758 && _pc.getY() == 32878 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32794 && _pc.getY() == 32790 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32781 && _pc.getY() == 32881 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32782 && _pc.getY() == 32881 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32781 && _pc.getY() == 32880 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else if (_pc.바포방 != true && _pc.getX() == 32782 && _pc.getY() == 32880 && _pc.getMapId() == 2) {
					return _isHit = false;
				} else {
					_isHit = calcPcNpcHit();
				}
				if (_isHit == false) {
//					 _pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 13418));// 미스 이펙트
				}
			}
		} else if (_calcType == NPC_PC) {
			if(_npc instanceof MJCompanionInstance || _npc instanceof MJDogFightInstance){
				_isHit = do_calc_hit_companion(_npc);	
			}else{
				_isHit = calcNpcPcHit();
			}
			if (_isHit == false) {
				_targetPc.sendPackets(new S_SkillSound(_target.getId(), 13418));// 이펙트
			}
		} else if (_calcType == NPC_NPC) {
			if((_npc instanceof MJCompanionInstance || _npc instanceof MJDogFightInstance)){
				_isHit = do_calc_hit_companion(_npc);
			}else{
				_isHit = calcNpcNpcHit();
			}
		} else if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
			_isHit = false;
			return _isHit;
		} else if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
			_isHit = false;
			return _isHit;
		}
		return _isHit;
	}



	private boolean do_calc_hit_companion(L1Character attacker){
		return MJAttackChain.getInstance().do_calculate_hit(this, attacker, _target);
	}
	
	private int do_calc_damage_companion(L1Character attacker){
		int damage = MJAttackChain.getInstance().do_calculate_damage(this, attacker, _target);
		if(damage <= 0)
			_isHit = false;
		return damage;
	}
	
	// ●●●● 플레이어로부터 플레이어에의 명중 판정 ●●●●
	/*
	 * PC에의 명중율 =(PC의 Lv＋클래스 보정＋STR 보정＋DEX 보정＋무기 보정＋DAI의 매수/2＋마법 보정)×0.68－10
	 * 이것으로 산출된 수치는 자신이 최대 명중(95%)을 주는 일을 할 수 있는 상대측 PC의 AC 거기로부터 상대측 PC의 AC가
	 * 1좋아질 때마다 자명중율로부터 1당겨 간다 최소 명중율5% 최대 명중율95%
	 */
	private boolean calcPcPcHit() {
		if (_pc.hasSkillEffect(L1SkillId.ABSOLUTE_BLADE)) {
			if (_target.hasSkillEffect(ABSOLUTE_BARRIER)) {
				int chance = _pc.getLevel() - 79;
				if (chance >= 8)
					chance = 8;
				if (chance >= _random.nextInt(100) + 1) {
					_targetPc.removeSkillEffect(ABSOLUTE_BARRIER);
					_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 14539));
					_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 14539));
				}
			}
		}
		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER) || _targetPc.hasSkillEffect(ICE_LANCE))
			return false;

		_hitRate = _pc.getLevel();

		/** 스탯 + 무기에 따른 공성 **/
		_hitRate += PchitAdd();
		if (_weaponType == 20 && _arrow != null) {
			_hitRate += _arrow.getItem().getHitModifier();
		}
		
		/** 요정일경우 80부터 렙당 명중 상승 **/
		if (_pc.isElf()) {
			_hitRate += Math.max(0, _pc.getLevel() - 80);
		}

		/** 전사일경우 70부터 렙당 명중+1 상승 **/
		if (_pc.is전사()) {
			_hitRate += Math.max(0, _pc.getLevel() - 70) * 1;
		}

		int attackerDice = _random.nextInt(20) + 1 + _hitRate - 10;

		/** 타겟PC의 회피 스킬 연산 **/
		attackerDice -= toPcSkillHit() * 0.1D;

		int defenderValue = (int) (_targetPc.getAC().getAc() * 1.5) * -1;
		int levelDmg = (int) ((_targetPc.getLevel() - _pc.getLevel()) * 2.0);
		if (levelDmg <= 0)
			levelDmg = 0;

		defenderValue += levelDmg;

		/** DefenderDice 연산 **/
		int defenderDice = toPcDD(defenderValue);

		// 캐릭터 공성데이터 추가
		try {
			if (_pc.isCrown()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(0);
			} else if (_pc.isKnight()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(1);
			} else if (_pc.isElf()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(2);
			} else if (_pc.isWizard()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(3);
			} else if (_pc.isDarkelf()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(4);
			} else if (_pc.isBlackwizard()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(5);
			} else if (_pc.isDragonknight()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(6);
			} else if (_pc.is전사()) {
				_hitRate -= CharacterHitRate.getInstance().getCharacterHitRate(7);
			}
		} catch (Exception e) {
			System.out.println("Character Add Damege Error");
		}

		/** 히트 최종 연산 **/
		if (hitRateCal(attackerDice, defenderDice, _hitRate - 9, _hitRate + 10))
			return false;

		if (_pc.getLocation().getLineDistance(_targetPc.getLocation()) >= 3 && _weaponType != 20 && _weaponType != 62) { // 타겟과의
			_hitRate = 0;
		}
		int rnd = _random.nextInt(100) + 1;
		if (_weaponType == 20 && _hitRate > rnd) { // 활의 경우 , 히트 했을 경우에서도
													// ER에서의회피를 재차 실시한다.
			return calcErEvasion();
		}

		int _jX = _pc.getX() - _targetPc.getX();
		int _jY = _pc.getY() - _targetPc.getY();

		if (_weaponType == 24) { // 창
			if ((_jX > 3 || _jX < -3) && (_jY > 3 || _jY < -3)) {
				_hitRate = 0;
			}
		} else if (_weaponType == 20 || _weaponType == 62) {// 활
			if ((_jX > 15 || _jX < -15) && (_jY > 15 || _jY < -15)) {
				_hitRate = 0;
			}
		} else {
			if ((_jX > 2 || _jX < -2) && (_jY > 2 || _jY < -2)) {
				_hitRate = 0;
			}
		}

		// System.out.println("Final 값 : _hitRate:"+_hitRate+" rnd:"+rnd+"
		// 결과:"+(_hitRate >= rnd));
		// return _hitRate >= rnd;
		// 미스이팩 넣기
		if (_hitRate >= rnd) {
			return true;
		} else {
			
			 _pc.sendPackets(new S_SkillSound(_targetPc.getId(), 13418));//
			  _targetPc.sendPackets(new S_SkillSound(_targetPc.getId(),
			  13418));// 이펙트
			  return false;
		}
	}

	// ●●●● 플레이어로부터 NPC 에의 명중 판정 ●●●●
	private boolean calcPcNpcHit() {
		if (_targetNpc.getNpcId() == MJRaidLoadManager.MRS_SP_VALAKAS_HPABSORB_ID || _targetNpc.getNpcId() == MJRaidLoadManager.MRS_SP_VALAKAS_MPABSORB_ID) {
			if (!_pc.isValakasProduct())
				return false;
		}

		if (_targetNpc.getNpcTemplate().get_npcId() == 8500138)
			return false;

		if (_targetNpc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE)
			return false;

		/** SPR체크 **/
		int npcId = _targetNpc.getNpcTemplate().get_npcId(); // 씨엠디오류나옴
		if (npcId >= 45912 && npcId <= 45915 && !_pc.hasSkillEffect(STATUS_HOLY_WATER)) {
			_hitRate = 0;
			return false;
		}
		if (npcId == 45916 && !_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
			_hitRate = 0;
			return false;
		}
		if (npcId == 45941 && !_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
			_hitRate = 0;
			return false;
		}
//		if (npcId == 45752 && !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
//			_hitRate = 0;
//			return false;
//		}
//		if (npcId == 45753 && !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
//			_hitRate = 0;
//			return false;
//		}
//		if (npcId == 45675 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//			_hitRate = 0;
//			return false;
//		}
//		if (npcId == 81082 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//			_hitRate = 0;
//			return false;
//		}
//		if (npcId == 45625 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//			_hitRate = 0;
//			return false;
//		}
	/*	if (npcId == 45674 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
			_hitRate = 0;
			return false;
		}
		if (npcId == 45685 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
			_hitRate = 0;
			return false;
		}*/
		if (npcId >= 46068 && npcId <= 46091 && _pc.getCurrentSpriteId() == 6035) {
			_hitRate = 0;
			return false;
		}
		if (npcId >= 46092 && npcId <= 46106 && _pc.getCurrentSpriteId() == 6034) {
			_hitRate = 0;
			return false;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) { // 오색진주
			_hitRate = 0;
			return false;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) { // 신비진주
			_hitRate = 0;
			return false;
		}

		// NPC에의 명중율 =(PC의 Lv＋클래스 보정＋STR 보정＋DEX 보정＋무기 보정＋DAI의 매수/2＋마법
		// 보정)×5－{NPC의 AC×(-5)}
		_hitRate = _pc.getLevel();

		/** 스탯 + 무기에 따른 공성 **/
		_hitRate += PchitAdd();
		if (_weaponType == 20 && _arrow != null) {
			_hitRate += _arrow.getItem().getHitModifier();
		}
		if (_targetNpc.getAC().getAc() < 0) {
			int acrate = _targetNpc.getAC().getAc() * -1;
			double aaaa = (_hitRate / 100) * (acrate / 2.5D);
			_hitRate -= (int) aaaa;
		}

		if (_pc.getLevel() < _targetNpc.getLevel()) {
			_hitRate -= _targetNpc.getLevel() - _pc.getLevel();
		}
		if (_hitRate > 95) {
			_hitRate = 95;
		} else if (_hitRate < 5) {
			_hitRate = 5;
		}

		int _jX = _pc.getX() - _targetNpc.getX();
		int _jY = _pc.getY() - _targetNpc.getY();

		if (_weaponType == 24) { // 창일때
			if ((_jX > 3 || _jX < -3) && (_jY > 3 || _jY < -3)) {
				_hitRate = 0;
			}
		} else if (_weaponType == 20 || _weaponType == 62) {// 활일때
			if ((_jX > 15 || _jX < -15) && (_jY > 15 || _jY < -15)) {
				_hitRate = 0;
			}
		} else {
			if ((_jX > 2 || _jX < -2) && (_jY > 2 || _jY < -2)) {
				_hitRate = 0;
			}
		}

		try {
			if (_pc.isCrown()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(0);
			} else if (_pc.isKnight()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(1);
			} else if (_pc.isElf()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(2);
			} else if (_pc.isWizard()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(3);
			} else if (_pc.isDarkelf()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(4);
			} else if (_pc.isBlackwizard()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(5);
			} else if (_pc.isDragonknight()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(6);
			} else if (_pc.is전사()) {
				_hitRate += CharacterHitRate.getInstance().getCharacterHitRate(7);
			}

		} catch (Exception e) {
			System.out.println("Character Add Damege Error");
		}

		// TODO 클라우디아 - 해당 맵에서는 무조건
		/*
		 * if (_targetNpc.getMapId() == 1 || _targetNpc.getMapId() == 2 ||
		 * _targetNpc.getMapId() == 3 || _targetNpc.getMapId() == 7 ||
		 * _targetNpc.getMapId() == 8 || _targetNpc.getMapId() == 10 ||
		 * _targetNpc.getMapId() == 11 || _targetNpc.getMapId() == 12 ||
		 * _targetNpc.getMapId() == 12146 || _targetNpc.getMapId() == 12147 ||
		 * _targetNpc.getMapId() == 9) { return true; }
		 */

		return _hitRate >= _random.nextInt(100) + 1;
	}
	
	// ●●●● NPC 로부터 플레이어에의 명중 판정 ●●●●
	private boolean calcNpcPcHit() {
		if (_targetPc.hasSkillEffect(L1SkillId.MOEBIUS)) {
			int bowactid = _npc.getNpcTemplate().getBowActId();
			if (bowactid == 66) {
				_isHit = false;
			} else {
				_isHit = true;
			}
			return _isHit;
		}
		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			return false;
		}
		_hitRate += _npc.getLevel() * 1.2;

		if (_npc instanceof L1PetInstance) { // 펫은 LV1마다 추가 명중+2
			_hitRate += _npc.getLevel() * 2;
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		_hitRate += _npc.getHitup();

		/** 타겟PC의 회피 스킬 연산 **/
		_hitRate += toPcSkillHit();
		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetPc.getAC().getAc());
		if(armor_class == null){
			_hitRate += (_targetPc.getAC().getAc() * 0.01);
		}else{
			_hitRate -= (armor_class.get_to_npc_dodge());
		}
		/*
		if (_targetPc.getAC().getAc() < 0)
			_hitRate += (int) ((-10 + _targetPc.getAC().getAc()) * (Config.PER_AC_PC_TO_PC * 0.01));
		else {
			_hitRate -= (int) ((10 - _targetPc.getAC().getAc()) * (Config.PER_AC_PC_TO_PC * 0.01));
		}*/

		if (_npc.getNpcTemplate().get_ranged() < 10) {
			int dg = _target.getDg();
			if (dg != 0) {
				dg = (int) (dg * (Config.PER_DODGE * 0.02D));
				_hitRate = (int) (_hitRate - _hitRate * dg * 0.01D);
			}
		} else {
			int er = _target.getTotalER();
			if (er > 0) {
				er = (int) (er * (Config.PER_EVASION * 0.03D));
				_hitRate = (int) (_hitRate - _hitRate * er * 0.01D);
			}
		}

		return _hitRate >= _random.nextInt(100) + 1;
	}

	// int attackerDice = _random.nextInt(20) + 1 + _hitRate - 1;
	//
	// /** 타겟PC의 회피 스킬 연산 **/
	// attackerDice -= toPcSkillHit() * 0.1D;
	//
	// int defenderValue = (_targetPc.getAC().getAc()) * -1;
	//
	// /** DefenderDice 연산 **/
	// int defenderDice = toPcDD(defenderValue);
	//
	// /** 히트 최종 연산 **/
	// if (hitRateCal(attackerDice, defenderDice, _hitRate, _hitRate + 19))
	// return false;
	//
	// int rnd = _random.nextInt(100) + 1;
	// NPC의 공격 레인지가 10이상의 경우로, 2이상 떨어져 있는 경우활공격으로 간주한다
	// if (_npc.getNpcTemplate().get_ranged() >= 10 && _hitRate > rnd
	// && _npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY))
	// >= 2) {
	// return calcErEvasion();
	// }
	// return _hitRate >= rnd;
	// }

	// ●●●● NPC 로부터 NPC 에의 명중 판정 ●●●●
	private boolean calcNpcNpcHit() {
		if (_targetNpc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE)
			return false;

		if (_targetNpc.getNpcTemplate().get_npcId() == 8500138)
			return false;

		int target_ac = 10 - _targetNpc.getAC().getAc();
		int attacker_lvl = _npc.getNpcTemplate().get_level();

		if (target_ac != 0) {
			_hitRate = (100 / target_ac * attacker_lvl); // 피공격자 AC = 공격자 Lv //
															// 의 때 명중율 100%
		} else {
			_hitRate = 100 / 1 * attacker_lvl;
		}

		if (_npc instanceof L1PetInstance) { // 펫은 LV1마다 추가 명중+2
			_hitRate += _npc.getLevel() * 2;
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		if (_hitRate < attacker_lvl) {
			_hitRate = attacker_lvl; // 최저 명중율=Lｖ％
		}
		if (_hitRate > 95) {
			_hitRate = 95; // 최고 명중율은 95%
		}
		if (_hitRate < 5) {
			_hitRate = 5; // 공격자 Lv가 5 미만때는 명중율 5%
		}

		int rnd = _random.nextInt(100) + 1;
		return _hitRate >= rnd;
	}

	// ●●●● ER에 의한 회피 판정 ●●●●
	private boolean calcErEvasion() {
		int er = _targetPc.getTotalER();

		int rnd = _random.nextInt(130) + 1;
		return er < rnd;
	}

	/* ■■■■■■■■■■■■■■■ 대미지 산출 ■■■■■■■■■■■■■■■ */

	/*
	 * public static int weapon_type_to_critical_effect(int weapon_type, boolean
	 * is_double_weapon) { switch (weapon_type) { case 4: return 13411; case 46:
	 * return 13412; case 24: return 13409; case 50: return 13410; case 40:
	 * return 13413; case 11: return is_double_weapon ? 13415 : 13414; case 58:
	 * return 13416; case 54: return 13417; case 20: case 62: return 13392; }
	 * return 13398; }
	 * 
	 * public static void send_critical(L1Character target, int weaponType,
	 * boolean is_double_weapon) { S_SkillSound s = new
	 * S_SkillSound(target.getId(), weapon_type_to_critical_effect(weaponType,
	 * is_double_weapon)); target.sendPackets(s, false);
	 * target.broadcastPacket(s); }
	 */

	public int calcDamage() {
		try {
			switch (_calcType) {
			case PC_PC:
				_damage = calcPcPcDamage();
				_damage += _pc.get_lateral_damage();
				_damage -= _targetPc.get_lateral_reduction();
				if (_targetPc.getDamageReductionIgnore() > 0)
					_damage += (_targetPc.getDamageReductionIgnore() * Config.REDUCTION_IGNORE_DAMAGERATE);

				if (_weaponType == 20 || _weaponType == 62) {
					int Bowcritical = CalcStat.calcBowCritical(_pc.getAbility().getTotalDex()) + 1;
					if ((Bowcritical > 0 || _pc.get_missile_critical_rate() > 0) && MJRnd.isWinning(100, Bowcritical + _pc.get_missile_critical_rate())) {
						_damage *= Config.MISSILE_CRITICAL_DAMAGE_RATE;
						_pc.sendPackets(new S_AttackCritical(_pc, _targetId, _targetX, _targetY, _weaponType, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, _targetX, _targetY, _weaponType, _isHit));
						_isCritical = true;
					}
					
					/** 키링크 크리티컬 대미지 **/
				} else if (_weaponType2 == 17) {
					int magiccritical = CalcStat.calcMagicCritical(_pc.getAbility().getTotalInt()) + 1;
					if ((magiccritical > 0 || _pc.get_magic_critical_rate() > 0) && MJRnd.isWinning(100, magiccritical + _pc.get_magic_critical_rate())) {
						_isCritical = true;
						_damage *= Config.MAGIC_CRITICAL_DAMAGE_RATE;
						_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
						Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
					}
				} else {
					if (_weaponType2 != 0) {
						int Dmgcritical = CalcStat.calcDmgCritical(_pc.getAbility().getTotalStr()) + 1;
						if ((Dmgcritical > 0 || _pc.get_melee_critical_rate() > 0) && MJRnd.isWinning(100, Dmgcritical + _pc.get_melee_critical_rate() + _pc.get_final_burn_critical_rate())) {
							_damage *= Config.MELEE_CRITICAL_DAMAGE_RATE;
							_isCritical = true;
							_pc.sendPackets(new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
							Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
						}
					}
				}

				if (_pc.getRedKnightClanId() != 0)
					_pc.addRedKnightDamage(_targetPc.getRedKnightClanId() == 0 ? _damage : -_damage);

				// 타이탄 락 : HP가 40% 미만일때 근접공격을 확률적으로 반사.
				if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17 && _weaponType2 != 19) {
//					if (_targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null) { // 기존소스
					if (_targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null && !_targetPc.isstun()) { // 스턴 걸리면 타이탄 락 풀리는 소스
						int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
						int chance = _random.nextInt(100) + 1;
						int 락구간 = 0;
						if (_target.get락구간상승() != 0) {
							락구간 += _target.get락구간상승();
						}
						int titan = _targetPc.getInventory().checkEquippedcount(202014);
						int sumgwang = _targetPc.getInventory().checkEquippedcount(547);
						락구간 += titan == 1 ? 5 : titan == 2 ? 10 : 0;
						락구간 += sumgwang == 1 ? 5 : sumgwang == 2 ? 10 : 0;
						if (!MJCommons.isUnbeatable(_targetPc) && percent <= (40 + 락구간 + _targetPc.getRisingUp()) && chance <= Config.TitanRock_probability) {
							if (_targetPc.getInventory().checkItem(41246, 10)) {
								_pc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
								_damage = 0;
								_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
								_targetPc.getInventory().consumeItem(41246, 10);
							} else {
								_targetPc.sendPackets(new S_SystemMessage("타이탄 락: 촉매제가 부족합니다."));
							}
						}
					}
				} else {
					if (_weaponType2 != 17 && _weaponType2 != 19) {
						if (_targetPc.getPassive(MJPassiveID.TITAN_BLITZ.toInt()) != null) {
							int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
							int chance = _random.nextInt(100) + 1;
							int 락구간 = 0;
							if (_target.get락구간상승() != 0) {
								락구간 += _target.get락구간상승();
							}
							int titan = _targetPc.getInventory().checkEquippedcount(202014);
							int sumgwang = _targetPc.getInventory().checkEquippedcount(547);
							락구간 += titan == 1 ? 5 : titan == 2 ? 10 : 0;
							락구간 += sumgwang == 1 ? 5 : sumgwang == 2 ? 10 : 0;
							if (!MJCommons.isUnbeatable(_targetPc) && percent <= (40 + 락구간 + _targetPc.getRisingUp()) && chance <= Config.Titanbullet_probability) {
								if (_targetPc.getInventory().checkItem(41246, 10)) {
									_pc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
									_damage = 0;
									_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12557));
									_targetPc.getInventory().consumeItem(41246, 10);
								} else {
									_targetPc.sendPackets(new S_SystemMessage("타이탄 블릿: 촉매제가 부족합니다."));
								}
							}
						}
					} else {
						if (_targetPc.getPassive(MJPassiveID.TITAN_MAGIC.toInt()) != null) {
							int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
							int chance = _random.nextInt(100) + 1;
							int 락구간 = 0;
							if (_target.get락구간상승() != 0) {
								락구간 += _target.get락구간상승();
							}
							int titan = _targetPc.getInventory().checkEquippedcount(202014);
							int sumgwang = _targetPc.getInventory().checkEquippedcount(547);
							락구간 += titan == 1 ? 5 : titan == 2 ? 10 : 0;
							락구간 += sumgwang == 1 ? 5 : sumgwang == 2 ? 10 : 0;
							if (!MJCommons.isUnbeatable(_targetPc) && percent <= (40 + 락구간 + _targetPc.getRisingUp()) && chance <= Config.Titanmagic_probability) {
								if (this._targetPc.getInventory().checkItem(41246, 10)) {
									if (this._calcType == 1)
										this._pc.receiveCounterBarrierDamage(this._targetPc, 타이탄대미지());
									else if (this._calcType == 2)
										this._npc.receiveCounterBarrierDamage(this._targetPc, 타이탄대미지());
									_damage = 0;
									this._targetPc.sendPackets(new S_SkillSound(this._targetPc.getId(), 12559));
									this._targetPc.getInventory().consumeItem(41246, 10);
								} else {
									this._targetPc.sendPackets(new S_SystemMessage("타이탄 매직: 촉매제가 부족합니다."));
								}
							}
						}
					}
				}
				break;
				
				
			case PC_NPC:
				try {
					_damage = calcPcNpcDamage();
					_damage += _pc.get_lateral_damage();

					if (_weaponType == 20 || _weaponType == 62) {
						int Bowcritical = CalcStat.calcBowCritical(_pc.getAbility().getTotalDex()) + 1;
						if ((Bowcritical > 0 || _pc.get_missile_critical_rate() > 0) && MJRnd.isWinning(100, Bowcritical + _pc.get_missile_critical_rate())) {
							_damage *= Config.MISSILE_CRITICAL_DAMAGE_RATE;
							_pc.sendPackets(new S_AttackCritical(_pc, _targetId, _targetX, _targetY, _weaponType, _isHit));
							Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, _targetX, _targetY, _weaponType, _isHit));
							_isCritical = true;
						}
					} else if (_weaponType2 == 17) {
						int magiccritical = CalcStat.calcMagicCritical(_pc.getAbility().getTotalInt()) + 1;
						if ((magiccritical > 0 || _pc.get_magic_critical_rate() > 0) && MJRnd.isWinning(100, magiccritical + _pc.get_magic_critical_rate())) {
							_isCritical = true;
							_damage *= Config.MAGIC_CRITICAL_DAMAGE_RATE;
							_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
							Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 91, Sweapon != null));
						}
					} else {
						if (_weaponType2 != 0) {
							int Dmgcritical = CalcStat.calcDmgCritical(_pc.getAbility().getTotalStr()) + 1;
							if ((Dmgcritical > 0 || _pc.get_melee_critical_rate() > 0) && MJRnd.isWinning(100, Dmgcritical + _pc.get_melee_critical_rate() + _pc.get_final_burn_critical_rate())) {
								_isCritical = true;
								_damage *= Config.MELEE_CRITICAL_DAMAGE_RATE;
								_pc.sendPackets(new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
								Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, _weaponType, Sweapon != null));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case NPC_PC:
				if(_npc instanceof MJCompanionInstance || _npc instanceof MJDogFightInstance)
					_damage = do_calc_damage_companion(_npc);
				else
					_damage = calcNpcPcDamage();
				_damage -= _targetPc.get_lateral_reduction();
				if (_targetPc.getDamageReductionIgnore() > 0)
					_damage += (_targetPc.getDamageReductionIgnore() * Config.REDUCTION_IGNORE_DAMAGERATE);

				// 타이탄 락 : HP가 40% 미만일때 근접 공격을 확률적으로 반사.
				int bowactid = _npc.getNpcTemplate().getBowActId();
				if (bowactid != 66) {
//					if (_targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null) { // 기존 소스
					if (_targetPc.getPassive(MJPassiveID.TITAN_ROCK.toInt()) != null && !_targetPc.isstun()) { // 스턴 걸렸을 때 타이탄 락 풀리는 소스
						int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
						int chance = _random.nextInt(100) + 1;
						int 락구간 = 0;
						if (_target.get락구간상승() != 0) {
							락구간 += _target.get락구간상승();
						}
						int titan = _targetPc.getInventory().checkEquippedcount(202014);
						int sumgwang = _targetPc.getInventory().checkEquippedcount(547);
						락구간 += titan == 1 ? 5 : titan == 2 ? 10 : 0;
						락구간 += sumgwang == 1 ? 5 : sumgwang == 2 ? 10 : 0;
						if (!MJCommons.isUnbeatable(_targetPc) && percent <= (40 + 락구간 + _targetPc.getRisingUp()) && chance <= Config.TitanRock_probability) {

							if (_targetPc.getInventory().checkItem(41246, 10)) {
								_npc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
								_damage = 0;
								_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12555));
								_targetPc.getInventory().consumeItem(41246, 10);
							} else {
								_targetPc.sendPackets(new S_SystemMessage("타이탄 락: 촉매제가 부족합니다."));
							}
						}
					}
				} else {
					// 타이탄 블릿 : HP가 40% 미만일때 원거리 공격을 확률적으로 반사.
					if (_targetPc.getPassive(MJPassiveID.TITAN_BLITZ.toInt()) != null) {
						int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
						int chance = _random.nextInt(100) + 1;
						int 락구간 = 0;
						if (_target.get락구간상승() != 0) {
							락구간 += _target.get락구간상승();
						}
						int titan = _targetPc.getInventory().checkEquippedcount(202014);
						int sumgwang = _targetPc.getInventory().checkEquippedcount(547);
						락구간 += titan == 1 ? 5 : titan == 2 ? 10 : 0;
						락구간 += sumgwang == 1 ? 5 : sumgwang == 2 ? 10 : 0;
						if (!MJCommons.isUnbeatable(_targetPc) && percent <= (40 + 락구간 + _targetPc.getRisingUp()) && chance <= Config.Titanbullet_probability) {
							if (_targetPc.getInventory().checkItem(41246, 10)) {
								_npc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
								_damage = 0;
								_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12557));
								_targetPc.getInventory().consumeItem(41246, 10);
							} else {
								_targetPc.sendPackets(new S_SystemMessage("타이탄 블릿: 촉매제가 부족합니다."));
							}
						}
					}
				}
				break;
			case NPC_NPC:
				if(_npc instanceof MJCompanionInstance || _npc instanceof MJDogFightInstance) {
					_damage = do_calc_damage_companion(_npc);
					
				} else {
					_damage = calcNpcNpcDamage();
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _damage;
	}

	// ●●●● 플레이어로부터 플레이어에의 데미지 산출 ●●●●
	public int calcPcPcDamage() {

		int weaponMaxDamage = _weaponSmall; // + _weaponAddDmg;

		int weaponDamage = 0;

		if ((_pc.getZoneType() == 1 && _targetPc.getZoneType() == 0) || (_pc.getZoneType() == 1 && _targetPc.getZoneType() == -1)) {
			_isHit = false;
			// 세이프티존에서 노멀/컴뱃존 공격 불가
		}

		/** 특정지역 PK금지 **/
		if (_pc.getMapId() == 612 || _pc.getMapId() == 254 || _pc.getMapId() == 1930 || _pc.getMapId() == 38) {
			weaponDamage = 0;
			_isHit = false;
			_pc.sendPackets("\\fY해당 지역에서는 PK가 불가능 합니다.");
			_targetPc.sendPackets("\\fY해당 지역에서는 PK가 불가능 합니다.");
			return (int) weaponDamage;
		}

		
		/** 포효의이도류, 파푸리온의 이도류 인첸당 발동확률증가 **/
		if (_weaponId == 203018 || _weaponId == 7000019) {
			_weaponDoubleDmgChance += _pc.getWeapon().getEnchantLevel();
		}
		if (_weaponType == 58 && (_random.nextInt(100) + 1) <= _weaponDoubleDmgChance) { // 크로우
			weaponDamage = weaponMaxDamage + _weaponAddDmg;
			_attackType = 2;
			/*
			 * _pc.sendPackets(new S_SkillSound(_pc.getId(), 3671));
			 * _pc.broadcastPacket(new S_SkillSound(_pc.getId(), 3671));
			 */
		}

		if (_weaponType == 0) { // 맨손
			weaponDamage = 0;
		} else {
			if (weaponMaxDamage <= 0)
				weaponMaxDamage = 1;
			weaponDamage = _random.nextInt(weaponMaxDamage) + _weaponAddDmg;
		}

		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
			if (_weaponType != 20 && _weaponType != 62) {
				// weaponDamage = weaponMaxDamage + 5;
				weaponDamage = weaponMaxDamage + _weaponAddDmg + 5;
			}
		}

		int weaponTotalDamage = weaponDamage + _weaponEnchant + _weapon_bless_level;
		/*boolean secondw = false;
		if (_pc.is전사() && _pc.isPassive(MJPassiveID.SLAYER.toInt()) && _pc.getSecondWeapon() != null) {
			int ran = _random.nextInt(100);
			if (ran < 50) {
				secondw = true;
				weaponMaxDamage = _SweaponSmall + _SweaponAddDmg;
			}
		}*/
		// 더블브레이크 확률 50렙부터 5렙당 1%씩 상승
		if (_pc.hasSkillEffect(DOUBLE_BRAKE) && (_weaponType == 54 || _weaponType == 58)) {
			int RealSteelLevel = _pc.getLevel();
			if (RealSteelLevel < 45) {
				RealSteelLevel = 45;
			}
			int RealSteelLevelChance = (int) ((RealSteelLevel - 45) / 5 + Config.DOUBLE_BREAK_CHANCE);
			if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
				int lvl = _pc.getLevel();
				if (lvl >= 80)
					RealSteelLevelChance += (lvl - 79);
			}
			if ((_random.nextInt(100) + 1) <= RealSteelLevelChance) {
				weaponTotalDamage += _statusDamage * Config.DOUBLE_BREAK_DESTINY_PC;
				if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
					_pc.sendPackets(new S_SkillSound(_targetPc.getId(), 17223));
					Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetPc.getId(), 17223));
				}
			}
			if ((_random.nextInt(100) + 1) <= RealSteelLevelChance) {
				weaponTotalDamage += _statusDamage * Config.BURNING_SPIRIT_PC;
				if (_pc.hasSkillEffect(BURNING_SPIRIT)) {
					_pc.sendPackets(new S_SkillSound(_targetPc.getId(), 6532));
					Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetPc.getId(), 6532));
				}
			}
		}

		if (_weaponType != 20 && _weaponType != 62) { // 근거리
			int Dmgcritical = CalcStat.calcDmgCritical(_pc.getAbility().getTotalStr()) + 1;
			int chance = _random.nextInt(100) + 1;
			if (chance <= Dmgcritical) {
				weaponDamage = weaponMaxDamage + _weaponAddDmg + 1;
				weaponDamage += weaponDamage * 1.2;
				_isCritical = true;
			}
		} else {
			int Bowcritical = CalcStat.calcBowCritical(_pc.getAbility().getTotalDex()) + 1;
			int chance = _random.nextInt(100) + 1;
			if (chance <= Bowcritical) {
				weaponDamage = weaponMaxDamage + _weaponAddDmg + 10;
				weaponDamage += weaponDamage * 1.2;
				_isCritical = true;
			}
		}

		double dmg = weaponTotalDamage + _statusDamage;

		if (_weaponType2 == 17) {
			// dmg = L1WeaponSkill.키링크데미지(_pc, _target);
			dmg = MJCommons.getMagicDamage(_pc, _targetPc, (int) dmg);
		}
		if (_weaponType != 20 && _weaponType != 62) {
			// dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() +
			// _pc.getDmgRate() + _pc.get_regist_PVPweaponTotalDamage();
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getDmgRate();
		} else {
			// dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup() +
			// _pc.getBowDmgRate() + _pc.get_regist_PVPweaponTotalDamage() + 5;
			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup() + _pc.getBowDmgRate();
		}
		// 클랜버프 pvp
		if (_pc.hasSkillEffect(CLAN_BUFF3)) {
			dmg += 1;
		}
		if (_targetPc.hasSkillEffect(CLAN_BUFF4)) {
			dmg -= 1;
		}
		if (_weaponType == 20) { // 활
			if (_arrow != null) {
				dmg += _arrow.getItem().getDmgModifier();
			} else {
				dmg += MJRnd.next(2) + 1;
			}
		} else if (_weaponType == 62) { // 암 토토 렛
			int add_dmg = _sting.getItem().getDmgSmall();
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			dmg = dmg + _random.nextInt(add_dmg) + 1;
		}
		dmg = calcBuffDamage(dmg);

		if (_targetPc.hasSkillEffect(STRIKER_GALE)) { // 게일 외부화
			if (_weaponType == 20) {
				dmg += Config.STRIKERGALEs;
			}
		}
		
		if (_targetPc.hasSkillEffect(L1SkillId.TOP_RANKER))
			dmg -= 8;
		if (_targetPc.hasSkillEffect(L1SkillId.CLAN_BUFF4))
			dmg -= 1;

		if (_targetPc.hasSkillEffect(L1SkillId.메티스정성스프))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.메티스정성요리))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.메티스축복주문서))
			dmg -= 3;

		/** 본섭 10검이상 추타 +1 표기 효과 **/
		if (_weaponType != 0 && _weaponType != 20) {
			switch (weapon.getEnchantLevel()) {
			case 10:
				dmg += 1;
				break;
			case 11:
				dmg += 2;
				break;
			case 12:
				dmg += 3;
				break;
			case 13:
				dmg += 4;
				break;
			case 14:
				dmg += 5;
				break;
			case 15:
				dmg += 6;
				break;
			default:
				break;
			}
		}

		/** 인첸트에 따른 추타관련 **/
		if (_weaponType != 0 && _weaponType != 20) {
			switch (weapon.getEnchantLevel()) {
			case 8:
				dmg += 2;
				break;
			case 9:
				dmg += 4;
				break;
			case 10:
				dmg += 6;
				break;
			case 11:
				dmg += 8;
				break;
			case 12:
				dmg += 10;
				break;
			case 13:
				dmg += 12;
				break;
			case 14:
				dmg += 14;
				break;
			case 15:
				dmg += 17;
				break;
			default:
				break;
			}
		}
		if (_weaponType2 == 17) {
			// dmg = L1WeaponSkill.키링크데미지(_pc, _target);
		}

		if (_weaponType2 == 18)
			L1WeaponSkill.체인소드(_pc);

		/** 특수 인챈트 시스템 **/
		if (_weaponType != 0) {
			switch (weapon.get_item_level()) {
			case 1:// 1단계
				WeaponLevelAttack(_pc, _target, 3740, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			case 2:// 2단계
				WeaponLevelAttack(_pc, _target, 16018, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			case 3:// 3단계
				WeaponLevelAttack(_pc, _target, 16024, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			case 4:// 4단계
				WeaponLevelAttack(_pc, _target, 4167, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			default:
				break;
			}
		}
		/** 특수 인챈트 시스템 **/

		/** 아이템 스킬 적용 **/
		MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getAtk(_weaponId);
		if (model != null && weapon.get_item_level() == 0) {
			dmg += model.get(_pc, _targetPc, weapon, dmg);
		}

		for (L1ItemInstance item : _pc.getInventory().getItems()) {
			CharmSkillModel charm = CharmModelLoader.getInstance().getAtk(item.getItemId());
			if (charm != null)
				dmg -= charm.get(_pc, _targetPc, item, dmg);
		}

		for (L1ItemInstance item : _pc.getEquipSlot().getArmors()) {
			model = MJItemSkillModelLoader.getInstance().getAtk(item.getItemId());
			if (model != null)
				dmg += model.get(_pc, _targetPc, item, dmg);
		}
		/** 아이템 스킬 적용 **/

		if (_weaponType == 0) { // 맨손
			dmg = (_random.nextInt(5) + 4) / 4;
		}

		try {
			dmg += WeaponAddDamage.getInstance().getWeaponAddDamage(_weaponId);
		} catch (Exception e) {
			System.out.println("무기추가대미지에러");
		}

		Object[] dollList = _pc.getDollList().values().toArray(); // 매직 돌 인형에 의한
																	// 추가 대미지
		for (Object dollObject : dollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			if (dollList == null)
				continue;
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += doll.getDamageByDoll();
			}
			/** 특수 마법인형 인챈트 시스템 **/
			// dmg += doll.magic_doll_enchant_dmg(_pc, _targetPc);
			dmg += doll.attackPixieDamage(_pc, _targetPc);
			doll.getPixieGreg(_pc, _targetPc);
		}

		// 전사 스킬 PC - PC
				// 전사 스킬 PC - PC
				// 크래쉬 : 공격자에 레벨에 50% 정도를 데미지에 반영한다.
				if (_weaponType != 0){
					if (_pc.getPassive(MJPassiveID.CRASH.toInt()) != null) {
						int chance = _random.nextInt(100) + 1;
						if (Config.crashFo >= chance) { // 크래쉬 : 레벨 나누기 2의 데미지
							int crashdmg = (int) (_pc.getLevel() + 2 / Config.crashdmg);
							int furydmg = 0;
							// 퓨리 : 크래쉬에서 나온 데미지에 2배
							if (_pc.getPassive(MJPassiveID.FURY.toInt()) != null) {
								chance = _random.nextInt(100) + 1;
								if (Config.furyFo >= chance) { // 퓨리 확률
									furydmg += crashdmg / Config.furydmg;
									// 성공시 이팩 2개 나가는거
									_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12489));
									_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 12489));
								}
							}
							dmg += crashdmg + furydmg;
							// 크래쉬는 크래쉬 이팩트 그대로 처리.
							_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12487));
							_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 12487));
						}
					}
				}

				if (_pc.hasSkillEffect(L1SkillId.주군의버프)) {
			if (_pc.getClanRank() >= L1Clan.수호)
				dmg += 30;
		}

		/** 대상 속성인첸트에따른 대미지연산 **/
		dmg += 피시속성인첸트효과();

		/** 캐릭별 추가데미지, 추가리덕션, 확률 **/
		if (_calcType == PC_PC) {
			if (_pc.getAddDamageRate() >= CommonUtil.random(100)) {
				dmg += _pc.getAddDamage();
			}
			if (_targetPc.getAddReductionRate() >= CommonUtil.random(100)) {
				dmg -= _targetPc.getAddReduction();
			}
		}

		/** 70레벨부터 추가타격 + 1 **/
		dmg += Math.max(0, _pc.getLevel() - 70) * 1;

		/** 아머브레이크 */
		if (_targetPc.hasSkillEffect(ARMOR_BRAKE)) { // 아머브레이크
			if (_weaponType != 20 && _weaponType != 62) {
				dmg *= 1.58;
			}
		}

		/** 보스 아머브레이크 */
		if (_targetPc.hasSkillEffect(WIDE_ARMORBREAK)) { // 보스 아머브레이크
			if (_weaponType != 20 && _weaponType != 62) {
				dmg *= 1.58;
			}
		}

		if (_targetPc.hasSkillEffect(L1SkillId.CUBE_AVATAR)) { // 큐브 아바타
			dmg *= 1.05;
		}

		/** 트루타켓 **/
		if (_targetPc.get트루타켓() > 0) {
			dmg *= 1 + (_targetPc.get트루타켓() / 100);
		}

		/*** 신규레벨보호 ***/
		// if (_calcType == PC_PC) {
		// int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
		// if (castle_id == 0) {
		// if (_targetPc.getLevel() < Config.AUTO_REMOVELEVEL || _pc.getLevel()
		// < Config.AUTO_REMOVELEVEL && !_pc.isPinkName()) {
		// dmg /= 2;
		// _pc.sendPackets(new S_SystemMessage("신규유저는 대미지의 50%만 가해집니다."));
		// _targetPc.sendPackets(new S_SystemMessage("신규유저는 대미지를 50%만 받습니다."));
		// }
		// }
		// }

		/** 신규혈맹 공격안되게 **/
		if (_calcType == PC_PC) {
			int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
			if (castle_id == 0) {
				if (Config.신규혈맹보호처리) {
					boolean attack_ok = false;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(_targetPc)) {
						if (!(obj instanceof L1MonsterInstance)) {
							continue;
						}

						if (obj instanceof L1MonsterInstance) {
							L1MonsterInstance mon = (L1MonsterInstance) obj;
							if (!mon.isDead()) {
								int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId());
								if (monid != 0) {
									attack_ok = true;
									break;
								}
							}
						}
					}

					if (!attack_ok) {
						if (_pc.getClanid() == Config.신규혈맹클랜 || _targetPc.getClanid() == Config.신규혈맹클랜) {
							_pc.sendPackets(new S_SystemMessage("신규 유저는 상호 간에 공격이 되지 않습니다."));
							_targetPc.sendPackets(new S_SystemMessage("신규 유저는 상호 간에 공격이 되지 않습니다."));
							return 0;
						}
					}
				} else {
					if (_pc.getClanid() == Config.신규혈맹클랜 || _targetPc.getClanid() == Config.신규혈맹클랜) {
						dmg /= 2;
						_pc.sendPackets(new S_SystemMessage("신규 유저는 대미지의 50%만 가해집니다."));
						_targetPc.sendPackets(new S_SystemMessage("신규 유저는 대미지를 50%만 받습니다."));
					}
				}
			}
		}

		if (_pc.hasSkillEffect(BURNING_SLASH)) {
			if (_weaponType != 20) {
				dmg += 30;
				_pc.sendPackets(new S_SkillSound(_targetPc.getId(), 6591));
				_pc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 6591));
				_pc.removeSkillEffect(BURNING_SLASH);
			}
		}
		try {
			if (_targetPc.isCrown()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(0);
			} else if (_targetPc.isKnight()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(1);
			} else if (_targetPc.isElf()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(2);
			} else if (_targetPc.isWizard()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(3);
			} else if (_targetPc.isDarkelf()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(4);
			} else if (_targetPc.isBlackwizard()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(5);
			} else if (_targetPc.isDragonknight()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(6);
			} else if (_targetPc.is전사()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(7);
			}
		} catch (Exception e) {
			System.out.println("Character Add Reduction Error");
		}

		try {
			if (_pc.isCrown() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(0);
			} else if (_pc.isKnight() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(1);
			} else if (_pc.isElf() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(2);
			} else if (_pc.isWizard() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(3);
			} else if (_pc.isDarkelf() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(4);
			} else if (_pc.isBlackwizard() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(5);
			} else if (_pc.isDragonknight() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(6);
			} else if (_pc.is전사() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(7);
			}
		} catch (Exception e) {
			System.out.println("Character Add Damege Error");
		}

		/** 할파스, 발라카스의 일격 **/
		if ( _pc.getInventory().checkEquipped(22208) 
		|| _pc.getInventory().checkEquipped(22209) || _pc.getInventory().checkEquipped(22210) 
		|| _pc.getInventory().checkEquipped(22211)) {
			int chance = _random.nextInt(100) +1;	
			if(chance <= 7){
				//weaponDamage = weaponMaxDamage + _weaponAddDmg + 10;
				dmg *= 1.2;
				_pc.sendPackets(new S_SkillSound(_pc.getId(), 15841)); 
				Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 15841)); 
			}
		}
		
		int chance6 = _random.nextInt(100) + 1;
		if (_target != _targetNpc) {
			
			if (_targetPc.getInventory().checkEquipped(22204)) {// 린드완력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22204);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (5 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
				
			}else if (_targetPc.getInventory().checkEquipped(22205)){// 린드예지
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22205);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (5 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
			}else if (_targetPc.getInventory().checkEquipped(22206)){// 린드인내력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22206);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (15 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
			}else if (_targetPc.getInventory().checkEquipped(22207)){// 린드마력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22207);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (20 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
			} else if (_targetPc.getInventory().checkEquipped(111137)) {// 할파스 완력
				long time = System.currentTimeMillis();
				if (_targetPc.LindArmorTime < time) {
					int chance = 14;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(111137);
					if (armor != null && armor.getEnchantLevel() >= 0) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (10 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
				
			} else if (_targetPc.getInventory().checkEquipped(111141)) {// 할파스 예지
				long time = System.currentTimeMillis();
				if (_targetPc.LindArmorTime < time) {
					int chance = 14;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(111141);
					if (armor != null && armor.getEnchantLevel() >= 0) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (10 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
				
			} else if (_targetPc.getInventory().checkEquipped(111140)) {// 할파스 마력
				long time = System.currentTimeMillis();
				if (_targetPc.LindArmorTime < time) {
					int chance = 14;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(111140);
					if (armor != null && armor.getEnchantLevel() >= 0) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (10 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
			}
		}
		
		 /** 파푸리온의 장궁 대미지 리덕션 무시**/
		if (_pc.getInventory().checkEquipped(7000018)) {
			L1ItemInstance item = _pc.getInventory().findEquippedItemId(7000018);
			switch (item.getEnchantLevel()) {
				case 0: 
				case 1: 
				case 2: 
				case 3: 
				case 4: 
				case 5: 
				case 6: 
				case 7: 
				case 8: dmg += 1; break;
				case 9: dmg += 2; break;
				case 10: dmg += 3; break;
			}
		 
		/** 발라카스 갑옷 대미지 리덕션 무시 **/
		} else if (_pc.getInventory().checkEquipped(22208)) {
			L1ItemInstance item = _pc.getInventory().findEquippedItemId(22208);
			switch (item.getEnchantLevel()) {
				case 0: dmg += 3; break;
				case 1: dmg += 3; break;
				case 2: dmg += 3; break;
				case 3: dmg += 3; break;
				case 4: dmg += 3; break;
				case 5: dmg += 3; break;
				case 6: dmg += 3; break;
				case 7: dmg += 4; break;
				case 8: dmg += 5; break;
				case 9: dmg += 6; break;
			}
		} else if (_pc.getInventory().checkEquipped(22209)) {
		 	L1ItemInstance item = _pc.getInventory().findEquippedItemId(22209);
			switch (item.getEnchantLevel()) {
				case 0: dmg += 3; break;
				case 1: dmg += 3; break;
				case 2: dmg += 3; break;
				case 3: dmg += 3; break;
				case 4: dmg += 3; break;
				case 5: dmg += 3; break;
				case 6: dmg += 3; break;
				case 7: dmg += 4; break;
				case 8: dmg += 5; break;
				case 9: dmg += 6; break;
			}
		} else if (_pc.getInventory().checkEquipped(22210)) {
			L1ItemInstance item = _pc.getInventory().findEquippedItemId(22210);
			switch (item.getEnchantLevel()) {
				case 0: dmg += 3; break;
				case 1: dmg += 3; break;
				case 2: dmg += 3; break;
				case 3: dmg += 3; break;
				case 4: dmg += 3; break;
				case 5: dmg += 3; break;
				case 6: dmg += 3; break;
				case 7: dmg += 4; break;
				case 8: dmg += 5; break;
				case 9: dmg += 6; break;
			}
		} else if (_pc.isBlackwizard() &&_pc.getInventory().checkEquipped(22211)) {
			L1ItemInstance item = _pc.getInventory().findEquippedItemId(22211);
			switch (item.getEnchantLevel()) {
				case 0: dmg += 3; break;
				case 1: dmg += 3; break;
				case 2: dmg += 3; break;
				case 3: dmg += 3; break;
				case 4: dmg += 3; break;
				case 5: dmg += 3; break;
				case 6: dmg += 3; break;
				case 7: dmg += 4; break;
				case 8: dmg += 5; break;
				case 9: dmg += 6; break;
			}
		
		
		/** 할파스 갑옷 대미지 리덕션 무시 **/
	} else if (_pc.getInventory().checkEquipped(111137)) {
		L1ItemInstance item = _pc.getInventory().findEquippedItemId(111137);
		switch (item.getEnchantLevel()) {
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
			case 10: dmg += 5; break;
		}
	} else if (_pc.getInventory().checkEquipped(111141)) {
	 	L1ItemInstance item = _pc.getInventory().findEquippedItemId(111141);
		switch (item.getEnchantLevel()) {
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
		case 10: dmg += 5; break;
		}
	} else if (_pc.getInventory().checkEquipped(111140)) {
		L1ItemInstance item = _pc.getInventory().findEquippedItemId(111140);
		switch (item.getEnchantLevel()) {
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
		case 10: dmg += 5; break;		
		}
	}
		
		
		/** 아인하사드 섬광, 그랑카인의 심판 무기 대미지 이뮨 무시 **/ 
		if (_weaponId == 2944 && (_target != null && _target.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM))) {
			//-- e * 5 * 0.01
			double plus = 1.2;
			double enchantDamage = 0;
			if (_weaponEnchant >= 1) {
				//-- 인챈트 5일경우 1.2 + 0.25
				//-- 인챈트 10일경우 1.2 + 0.5
				enchantDamage = (IntRange.ensure(_weaponEnchant * 5, 5, 50) * 0.01);				 
			}
			weaponDamage *= (plus + enchantDamage);
		}
		
		if (_weaponId == 2945 && (_target != null && _target.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM))) {
			//-- e * 5 * 0.01
			double plus = 1.2;
			double enchantDamage = 0;
			if (_weaponEnchant >= 1) {
				//-- 인챈트 5일경우 1.2 + 0.25
				//-- 인챈트 10일경우 1.2 + 0.5
				enchantDamage = (IntRange.ensure(_weaponEnchant * 5, 5, 50) * 0.01);				 
			}
			weaponDamage *= (plus + enchantDamage);
		}
	
		
		/** 본섭 10검이상 추타 +1 표기 효과 **/
		if (_weaponType != 0 && _weaponType != 20) {
			switch (weapon.getEnchantLevel()) {
			case 10:
				dmg += 1;
				break;
			case 11:
				dmg += 2;
				break;
			case 12:
				dmg += 3;
				break;
			case 13:
				dmg += 4;
				break;
			case 14:
				dmg += 5;
				break;
			case 15:
				dmg += 6;
				break;
			default:
				break;
			}
		}
		
		
		/** 버닝스피릿츠, 엘리멘탈파이어, 브레이브멘탈 1.5배, 블로우 어택 1.5배 스킬이펙트 및 추타 부분 **/
		int chance41 = _random.nextInt(100) + 1;
		if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17) {
			if (_pc.hasSkillEffect(BURNING_SPIRIT) && !_pc.hasSkillEffect(DOUBLE_BRAKE) || _pc.hasSkillEffect(ELEMENTAL_FIRE)
					|| _pc.hasSkillEffect(L1SkillId.QUAKE) || _pc.hasSkillEffect(BRAVE_AURA)) {
				if (chance41 <= 15) {
					if (_pc.isDarkelf()) {
						dmg *= Config.BURNING_SPIRIT_PC;
						if (_targetPc != null) {
							S_SkillSound ss = new S_SkillSound(_targetPc.getId(), 7727);
							_targetPc.sendPackets(ss, false);
							_targetPc.broadcastPacket(ss);
						}
					} else {
						dmg *= 1.8;
						if (_targetPc != null) {
							S_SkillSound ss = new S_SkillSound(_targetPc.getId(), 7727);
							_targetPc.sendPackets(ss, false);
							_targetPc.broadcastPacket(ss);
						}
					}
				}
			}
		}

		if (_pc.hasSkillEffect(L1SkillId.BLOW_ATTACK)) {
			if (chance41 <= Config.BLOW_ATTACKPR)
				;
			_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 7727));
			dmg *= Config.BLOW_ATTACKDMG;
		}

		
		
		
		// 캐릭터 간 대미지 외부화 처리
		if (_calcType == PC_PC) {
			if (_pc.isCrown()) {
				dmg += Config.PRINCE_ADD_DAMAGEPC;
			} else if (_pc.isKnight()) {
				dmg += Config.KNIGHT_ADD_DAMAGEPC;
			} else if (_pc.isElf()) {
				dmg += Config.ELF_ADD_DAMAGEPC;
			} else if (_pc.isDarkelf()) {
				dmg += Config.DARKELF_ADD_DAMAGEPC;
			} else if (_pc.isWizard()) {
				dmg += Config.WIZARD_ADD_DAMAGEPC;
			} else if (_pc.isDragonknight()) {
				dmg += Config.DRAGONKNIGHT_ADD_DAMAGEPC;
			} else if (_pc.isBlackwizard()) {
				dmg += Config.BLACKWIZARD_ADD_DAMAGEPC;
			} else if (_pc.is전사()) {
				dmg += Config.전사_ADD_DAMAGEPC;
			}
		}

		/** AC에 의한 데미지 감소 수정본 **/
		if (_calcType == PC_PC) {
			if (_targetPc.isKnight()) { // 기사/
				dmg -= dmg * (calcPcDefense() * Config.기사);
			} else if (_targetPc.isDragonknight()) {// 용기사
				dmg -= dmg * (calcPcDefense() * Config.용기사);
			} else if (_targetPc.isElf()) { // 요정
				dmg -= dmg * (calcPcDefense() * Config.요정);
			} else if (_targetPc.isCrown()) { // 군주/
				dmg -= dmg * (calcPcDefense() * Config.군주);
			} else if (_targetPc.isWizard()) { // 법사/
				dmg -= dmg * (calcPcDefense() * Config.법사);
			} else if (_targetPc.isDarkelf()) { // 다엘/
				dmg -= dmg * (calcPcDefense() * Config.다엘);
			} else if (_targetPc.isBlackwizard()) { // 환술사/
				dmg -= dmg * (calcPcDefense() * Config.환술사);
			} else if (_targetPc.is전사()) { // 전사/
				dmg -= dmg * (calcPcDefense() * Config.전사);
			}
		}

		// -- PVP 에 대한 대미지 판정 처리
		if (_pc.getResistance().getPVPweaponTotalDamage() > 0) {
			dmg += _pc.getResistance().getPVPweaponTotalDamage() + 1;
		}
		dmg -= _targetPc.getResistance().getcalcPcDefense();
		dmg -= _targetPc.get_pvp_defense();

		// 기사단의 무기류 대미지가 이상한 문제가 발생하여 주석처리
//		if (_weaponId == 7000224 || _weaponId == 7000225 || _weaponId == 7000226 || _weaponId == 7000227
//				|| _weaponId == 7000228 || _weaponId == 7000229 || _weaponId == 7000230) { //기본무기 데미지 하향
//			dmg -= Config.NEWDMG;
//		}
		
		if (_pc.FouSlayer) {
			int diff = _pc.getLevel() / 10;
			dmg *= Config.FOU_DAMAGE_TABLE[diff];
		}

		if (_pc.TripleArrow) {
			int diff = _pc.getLevel() / 10;
			dmg *= Config.TRIPLE_DAMAGE_TABLE[diff];
		}
		
		if (_weaponType == 20 || _weaponType == 62) {
			if(_pc.hasSkillEffect(L1SkillId.CYCLONE)) {
				
				int prob = 5;
				if(_pc.getLevel() >= 85){
					prob += (_pc.getLevel() - 85);
				}
				if(MJRnd.isWinning(100, prob)) {
					dmg *= 1.5;
					_targetPc.send_effect(17557);
				}
				
				/*if(MJRnd.isWinning(1000000, Config.CYCLONE_PROBABILITY)) {
					dmg *= 1.5;
					_targetPc.send_effect(17557);
				}*/
			}
		}

		/** 대상 Buff에 따른 대미지 연산 **/
		dmg = toPcBuffDmg(dmg);
		if (dmg > 0 && _targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg -= (dmg * _targetPc.getImmuneReduction());
			// dmg = IntRange.ensure(dmg, 0, high);
		}
		
		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetPc.getAC().getAc());
		if(armor_class != null)
			dmg -= armor_class.get_to_pc_reduction();
		
		double total_reduction = 0;
		Object[] dollList1 = _targetPc.getDollList().values().toArray(); // 마법인형에
		for (Object dollObject : dollList1) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			total_reduction += doll.getDamageReductionByDoll();
		}
		total_reduction += _targetPc.getDamageReductionByArmor(); // 방어구에 의한 대미지 감소
		total_reduction += _targetPc.getDamageReduction(); // 방어구에 의한 대미지 감소
		total_reduction *= Config.PHYSICAL_REDUCTION_RATION;
		
		if(_targetPc.hasSkillEffect(L1SkillId.INFERNO) && _weaponType != 20 && _weaponType != 62) {
			L1ItemInstance target_weapon = _targetPc.getWeapon();
			if(target_weapon != null && target_weapon.getItem().getType() == 1) {
				if(MJRnd.isWinning(1000000, Config.INFERNO_PROBABILITY)) {
					int weapon_index = MJRnd.next(4);
					_targetPc.send_effect(Config.INFERNO_EFFECTS[weapon_index]);
					int inferno_damage = target_weapon.getItem().getDmgSmall() + 
							target_weapon.getEnchantLevel() + 
							_targetPc.getDmgRate() + 
							_targetPc.getDmgup() + 
							_targetPc.getDmgupByArmor();
					_pc.receiveDamage(_targetPc, (int)((weapon_index + 1) * inferno_damage));
					_pc.send_action(ActionCodes.ACTION_Damage);
				}
			}
		}
		dmg = Math.max(dmg - total_reduction, 0);
		if (dmg <= 0) {
			_isHit = false;
		}
		
		//if (secondw) {
			switch (_weaponId) {
							
			case 12: // 바람칼날의 단검
				dmg += L1WeaponSkill.바람칼날의단검(_pc, _target, _weaponEnchant);
				break;
				
			case 61: // 진명황의 집행검
				dmg += L1WeaponSkill.진명황의집행검(_pc, _target, _weaponEnchant);
				break;
				
			case 86: // 붉은그림자의 이도류
			    dmg += L1WeaponSkill.붉은그림자의이도류(_pc, _target, _weaponEnchant);
			    break;
				
			case 134: // 수정결정체 지팡이
				dmg += L1WeaponSkill.수정결정체지팡이(_pc, _target, _weaponEnchant);
				break;
				
			case 202011: // 가이아의 격노
				dmg += L1WeaponSkill.가이아의격노(_pc, _target, _weaponEnchant);
				break;
				
			case 202012: // 히페리온의 절망
				dmg += L1WeaponSkill.히페리온의절망(_pc, _target, _weaponEnchant);
				break;
				
			case 202013: // 크로노스의 공포
				dmg += L1WeaponSkill.크로노스의공포(_pc, _target, _weaponEnchant);
				break;
				
			case 202014: // 타이탄의 분노
				dmg += L1WeaponSkill.타이탄의분노(_pc, _target, _weaponEnchant);
				break;
				
			case 543: // 지배자의 지팡이
				dmg += L1WeaponSkill.지배자의지팡이(_pc, _target, _weaponEnchant, 10405);
				break;	
		
			case 202003: // 제로스의 지팡이
				dmg += L1WeaponSkill.제로스의지팡이(_pc, _target, _weaponEnchant, 11760);
				break;
				
			case 7000017: // 안타라스의 지팡이
				dmg += L1WeaponSkill.안타라스의지팡이(_pc, _target, _weaponEnchant, 4906);
				break;
				
			case 2944: // 아인하사드의 섬광(PC)
				dmg += L1WeaponSkill.아인하사드의섬광PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2945: // 그랑카인의 심판(PC)
				dmg += L1WeaponSkill.그랑카인의심판PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2946: // 사이하의 집념(PC)
				dmg += L1WeaponSkill.사이하의집념PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2947: // 실렌의 결의(PC)
				dmg += L1WeaponSkill.실렌의결의PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2948: // 에바의 서약(PC)
				dmg += L1WeaponSkill.에바의서약PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2949: // 파아그리오의 공포(PC)
				dmg += L1WeaponSkill.파아그리오의공포PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2950: // 마프르의 고뇌(PC)
				dmg += L1WeaponSkill.마프르의고뇌PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2951: // 단테스의 시련(PC)
				dmg += L1WeaponSkill.단테스의시련PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2952: // 테이아의 혼돈(PC)
				dmg += L1WeaponSkill.테이아의혼돈PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2953: // 아우라키아의 초월(PC)
				dmg += L1WeaponSkill.아우라키아의초월PC(_pc, _target, _weaponEnchant);
				break;
				
			case 2954: // 아인하사드의 구원(PC)
				dmg += L1WeaponSkill.아인하사드의구원PC(_pc, _target, _weaponEnchant);
				break;
				
			default:
				dmg += L1WeaponSkill.getWeaponSkillDamage(_pc, _target, _weaponId);
				break;
			}
			
		/*	} else {
				////////////////////////////////////////////////////////////////
				switch (_weaponId) {
				
		
				case 134: // 수정 결정체 지팡이
					dmg += L1WeaponSkill.수정결정체지팡이(_pc, _target, _weaponEnchant, 10405);
					break;
			
				case 202003: // 제로스의 지팡이
					dmg += L1WeaponSkill.제로스의지팡이(_pc, _target, _weaponEnchant, 11760);
					break;

				}
			}	
			*/
		
		return (int) dmg;
	}

	// ●●●● 플레이어로부터 NPC 에의 대미지 산출 ●●●●
	@SuppressWarnings("unused")
	private int calcPcNpcDamage() {
		if (_targetNpc == null || _pc == null) {
			_isHit = false;
			return 0;
		}
		int weaponMaxDamage = 0;
		boolean secondw = false;

		if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") && _weaponSmall > 0) {
			weaponMaxDamage = _weaponSmall;
		} else if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") && _weaponLarge > 0) {
			weaponMaxDamage = _weaponLarge;
		} else if (_targetNpc instanceof L1PeopleInstance) {
			weaponMaxDamage = _weaponSmall;
		}

		// weaponMaxDamage += _weaponAddDmg;
		int weaponDamage = 0;

		if (_weaponType == 58 && (_random.nextInt(100) + 1) <= _weaponDoubleDmgChance) { // 크로우
			// weaponDamage = weaponMaxDamage;
			weaponDamage = weaponMaxDamage + _weaponAddDmg;
			_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 58, Sweapon != null));// 땁
			Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 58, Sweapon != null));// 땁
			_isCritical = true;
		}

		if (_weaponType == 0) { // 맨손, 활, 암 토토 렛_weaponType == 0 ||
			weaponDamage = 0;
		} else {
			if (weaponMaxDamage <= 0)
				weaponMaxDamage = 1;
			weaponDamage = _random.nextInt(weaponMaxDamage) + 1;
			weaponDamage = _random.nextInt(weaponMaxDamage) + _weaponAddDmg;
			// System.out.println(weaponDamage);
		}

		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
			if (_weaponType != 20 && _weaponType != 62) {
				// weaponDamage = weaponMaxDamage;
				weaponDamage = weaponMaxDamage + _weaponAddDmg + 3;
			}
		}

		int weaponTotalDamage = weaponDamage + _weaponEnchant + _weapon_bless_level;

		if (_pc.is전사() && _pc.isPassive(MJPassiveID.SLAYER.toInt()) && _pc.getSecondWeapon() != null) {
			int ran = _random.nextInt(100);
			if (ran < 50) {
				secondw = true;
				if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("small") && _SweaponSmall > 0) {
					weaponMaxDamage = _SweaponSmall + _SweaponAddDmg;
				} else if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large") && _SweaponLarge > 0) {
					weaponMaxDamage = _SweaponLarge + _SweaponAddDmg;
				} else {
					weaponMaxDamage = _SweaponSmall + _SweaponAddDmg;
				}
			}
		}

		weaponTotalDamage += calcMaterialBlessDmg(); // 은축복 대미지 보너스
		/** 포효의이도류 인첸당 발동확률증가 **/
		if (_weaponId == 203018) {
			_weaponDoubleDmgChance += _pc.getWeapon().getEnchantLevel();
		}

		if (_weaponType == 54 && (_random.nextInt(100) + 1) <= (_weaponDoubleDmgChance - weapon.get_durability()) && _pc.isDarkelf()) { // 이도류
			weaponTotalDamage *= Config.DOUBLE_DMG;
			_isCritical = true;
			_pc.sendPackets(new S_AttackCritical(_pc, _targetId, 54, Sweapon != null));// 땁
			Broadcaster.broadcastPacket(_pc, new S_AttackCritical(_pc, _targetId, 54, Sweapon != null));// 땁
		}
		// 더블브레이크 확률 50렙부터 5렙당 1%씩 상승
		if (_pc.hasSkillEffect(DOUBLE_BRAKE) && (_weaponType == 54 || _weaponType == 58)) {
			int RealSteelLevel = _pc.getLevel();
			if (RealSteelLevel < 45) {
				RealSteelLevel = 45;
			}
			int RealSteelLevelChance = (int) ((RealSteelLevel - 45) / 5 + Config.DOUBLE_BREAK_CHANCE);
			if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
				int lvl = _pc.getLevel();
				if (lvl >= 80)
					RealSteelLevelChance += (lvl - 79);
			}
			if ((_random.nextInt(100) + 1) <= RealSteelLevelChance) {
				weaponTotalDamage += _statusDamage * Config.DOUBLE_BREAK_DESTINY_NPC;
				if (_pc.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
					_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 17223));
					Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetNpc.getId(), 17223));
				}
			}
			if ((_random.nextInt(100) + 1) <= RealSteelLevelChance) {
				weaponTotalDamage += _statusDamage * Config.BURNING_SPIRIT_NPC;
				if (_pc.hasSkillEffect(BURNING_SPIRIT)) {
					_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 6532));
					Broadcaster.broadcastPacket(_pc, new S_SkillSound(_targetNpc.getId(), 6532));
				}
			}
		}

		if (_weaponType != 20 && _weaponType != 62) { // 근거리
			int Dmgcritical = CalcStat.calcDmgCritical(_pc.getAbility().getTotalStr()) + 1;
			int chance = _random.nextInt(100) + 1;
			if (chance <= Dmgcritical) {
				weaponDamage = weaponMaxDamage + _weaponAddDmg + 1;
				weaponDamage += weaponDamage * 1.2;
				_isCritical = true;
			}
		} else {
			int Bowcritical = CalcStat.calcBowCritical(_pc.getAbility().getTotalDex()) + 1;
			int chance = _random.nextInt(100) + 1;
			if (chance <= Bowcritical) {
				weaponDamage = weaponMaxDamage + _weaponAddDmg + 11;
				weaponDamage += weaponDamage * 1.2;
				_isCritical = true;
			}
		}

		double dmg = weaponTotalDamage + _statusDamage;
		if (_weaponType2 == 17) {
			dmg = MJCommons.getMagicDamage(_pc, _targetNpc, (int) dmg);
			// dmg = L1WeaponSkill.키링크데미지(_pc, _target);
		}
		if (_weaponType != 20 && _weaponType != 62) {
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getDmgRate();
		} else {
			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup() + _pc.getBowDmgRate() + 5;
		}
		if (_pc.isWizard() && _weaponId == 202003) { //제로스
			dmg += 30;
		}
		if (_pc.isElf() && _weaponId == 7000229 || _weaponId == 1122 || _weaponId == 189 || _weaponId == 1136) { //요정 활
			dmg -= 10;
		}

		dmg += 몬스터속성인첸트효과(); // 속성 대미지

		if (_weaponType == 20) { // 활
			if (_arrow != null) {
				dmg += _arrow.getItem().getDmgModifier();
			} else {
				dmg += MJRnd.next(2) + 1;
			}
			if (_targetNpc.getNpcTemplate().is_hard()) {
				dmg /= 2;
			}
		} else if (_weaponType == 62) { // 암 토토 렛 건틀렛
			int add_dmg = 0;
			if (_targetNpc.getNpcTemplate().get_size().equalsIgnoreCase("large")) {
				add_dmg = _sting.getItem().getDmgLarge();
			} else {
				add_dmg = _sting.getItem().getDmgSmall();
			}
			if (add_dmg == 0) {
				add_dmg = 1;
			}
			// System.out.println(weaponDamage);
			dmg = dmg + _random.nextInt(add_dmg) + 1;
		}

		/** 본섭 10검이상 추타 +1 표기 효과 **/
		if (_weaponType != 0 && _weaponType != 20) {
			switch (weapon.getEnchantLevel()) {
			case 10:
				dmg += 1;
				break;
			case 11:
				dmg += 2;
				break;
			case 12:
				dmg += 3;
				break;
			case 13:
				dmg += 4;
				break;
			case 14:
				dmg += 5;
				break;
			case 15:
				dmg += 6;
				break;
			default:
				break;
			}
		}

		/** 인첸트에 따른 추타관련 **/
		if (_weaponType != 0 && _weaponType != 20) {
			switch (weapon.getEnchantLevel()) {
			case 8:
				dmg += 2;
				break;
			case 9:
				dmg += 4;
				break;
			case 10:
				dmg += 6;
				break;
			case 11:
				dmg += 8;
				break;
			case 12:
				dmg += 10;
				break;
			case 13:
				dmg += 12;
				break;
			case 14:
				dmg += 14;
				break;
			case 15:
				dmg += 17;
				break;
			default:
				break;
			}
		}

		if (_weaponType2 == 18)
			L1WeaponSkill.체인소드(_pc);

		/** 특수 인챈트 시스템 **/
		if (_weaponType != 0) {
			switch (weapon.get_item_level()) {
			case 1:// 1단계
				WeaponLevelAttack(_pc, _target, 3740, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			case 2:// 2단계
				WeaponLevelAttack(_pc, _target, 16018, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			case 3:// 3단계
				WeaponLevelAttack(_pc, _target, 16024, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			case 4:// 4단계
				WeaponLevelAttack(_pc, _target, 4167, _pc.getWeapon().getEnchantLevel(), weapon.get_item_level());
				break;
			default:
				break;
			}
		}
		/** 특수 인챈트 시스템 **/

		/** 아이템 스킬 적용 **/
		MJItemSkillModel model = MJItemSkillModelLoader.getInstance().getAtk(_weaponId);
		if (model != null && weapon.get_item_level() == 0) {
			dmg += model.get(_pc, _targetNpc, weapon, dmg);
		}

		for (L1ItemInstance item : _pc.getEquipSlot().getArmors()) {
			model = MJItemSkillModelLoader.getInstance().getAtk(item.getItemId());
			if (model != null)
				dmg += model.get(_pc, _targetNpc, item, dmg);
		}

		for (L1ItemInstance item : _pc.getInventory().getItems()) {
			CharmSkillModel charm = CharmModelLoader.getInstance().getAtk(item.getItemId());
			if (charm != null)
				dmg -= charm.get(_pc, _targetNpc, item, dmg);
		}

		if (_weaponType == 0) { // 맨손
			dmg = (_random.nextInt(5) + 4) / 4;
		}
		

		try {
			dmg += WeaponAddDamage.getInstance().getWeaponAddDamage(_weaponId);
		} catch (Exception e) {
			System.out.println("Weapon Add Damege Error");
		}

		if (_pc.hasSkillEffect(BURNING_SLASH)) {
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += 20;
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 6591));
				_pc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 6591));
				_pc.removeSkillEffect(BURNING_SLASH);
			}
		}
		Object[] dollList = _pc.getDollList().values().toArray(); // 매직 돌 인형에 의한
																	// 추가 대미지
		for (Object dollObject : dollList) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			if (dollList == null)
				continue;
			if (_weaponType != 20 && _weaponType != 62) {
				dmg += doll.getDamageByDoll();
			}
			/** 특수 마법인형 인챈트 시스템 **/
			// dmg += doll.magic_doll_enchant_dmg(_pc, _targetNpc);
			dmg += doll.attackPixieDamage(_pc, _targetNpc);
			doll.getPixieGreg(_pc, _targetNpc);
		}

		// 전사 스킬 PC - NPC
		// 크래쉬 : 공격자에 레벨에 50% 정도를 데미지에 반영한다.
		if (_pc.getPassive(MJPassiveID.CRASH.toInt()) != null) {
			int chance = _random.nextInt(100) + 1;
			if (Config.crashFo >= chance) { // 크래쉬 확률
				int crashdmg = (int) (_pc.getLevel() + 2 / Config.crashdmg);
				int furydmg = 0;
				// 퓨리 : 크래쉬에서 나온 뎀지에 2배.
				if (_pc.getPassive(MJPassiveID.FURY.toInt()) != null) {
					chance = _random.nextInt(100) + 1;
					if (Config.furyFo >= chance) { // 퓨리 확률
						furydmg += crashdmg / Config.furydmg;
						// 성공시 이팩 2개 나가는거
						_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 12489));
					}
				}
				dmg += crashdmg + furydmg;
				// 크래쉬는 크래쉬 이팩트 그대로 처리.
				_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 12487));
			}
		}

		
		/** 발라카스의 일격 **/
		if ( _pc.getInventory().checkEquipped(22208) 
		|| _pc.getInventory().checkEquipped(22209) || _pc.getInventory().checkEquipped(22210) 
		|| _pc.getInventory().checkEquipped(22211)) {
			int chance = _random.nextInt(100) +1;
			if(chance <= 7){
				dmg *= 1.1;
				_pc.sendPackets(new S_SkillSound(_pc.getId(), 15841)); 
				Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 15841)); 
			}
		}
		
		int chance6 = _random.nextInt(100) + 1;
		if (_target != _targetNpc) {
			
			if (_targetPc.getInventory().checkEquipped(22204)) {// 린드완력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22204);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (5 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
				
			}else if (_targetPc.getInventory().checkEquipped(111137)){// 할파스 완력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 14;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(111137);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (5 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
				
			}else if (_targetPc.getInventory().checkEquipped(111141)){// 할파스 예지
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 14;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(111141);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (5 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
				
			}else if (_targetPc.getInventory().checkEquipped(111140)){// 할파스 마력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 14;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(111140);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (5 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2183));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
				
			}else if (_targetPc.getInventory().checkEquipped(22205)){// 린드예지
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22205);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (5 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
			}else if (_targetPc.getInventory().checkEquipped(22206)){// 린드인내력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22206);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (15 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
			}else if (_targetPc.getInventory().checkEquipped(22207)){// 린드마력
				long time = System.currentTimeMillis();
				if(_targetPc.LindArmorTime < time) {
					int chance = 5;
					L1ItemInstance armor = _targetPc.getInventory().findEquippedItemId(22207);
					if(armor != null && armor.getEnchantLevel() >= 7) {
						chance += armor.getEnchantLevel() - 5;
					}
					if (chance6 <= chance) {
						short getMp = (short) (_targetPc.getCurrentMp() + (20 + _random.nextInt(11)));
						_targetPc.setCurrentMp(getMp);
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
						_targetPc.LindArmorTime = time + 5000;
					}
				}
			}
		}
		
		
		/** 버닝스피릿츠, 엘리멘탈파이어, 브레이브멘탈 1.5배 스킬이펙트 및 추타 부분 **/
		int chance41 = _random.nextInt(100) + 1;
		if (_weaponType != 20 && _weaponType != 62 && _weaponType2 != 17) {
			if (_pc.hasSkillEffect(BURNING_SPIRIT) && !_pc.hasSkillEffect(DOUBLE_BRAKE) || _pc.hasSkillEffect(ELEMENTAL_FIRE)
					|| _pc.hasSkillEffect(L1SkillId.QUAKE) || _pc.hasSkillEffect(BRAVE_AURA)) {
				if (chance41 <= 15) {
					if (_pc.isDarkelf()) {
						dmg *= Config.BURNING_SPIRIT_NPC;
						_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 7727));
					} else {
						dmg *= 1.8;
						_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 7727));
					}
				}
			}
		}

		if (_pc.hasSkillEffect(L1SkillId.BLOW_ATTACK)) {
			if (chance41 <= Config.BLOW_ATTACKPR)
				_targetNpc.broadcastPacket(new S_SkillSound(_targetNpc.getId(), 7727));
			dmg *= Config.BLOW_ATTACKDMG;
		}
		
		if (_weaponType == 20 || _weaponType == 62) {
			if(_pc.hasSkillEffect(L1SkillId.CYCLONE)) {
				if(MJRnd.isWinning(1000000, Config.CYCLONE_PROBABILITY)) {
					dmg *= 1.5;
					_targetNpc.send_effect(17557);
				}
			}
		}

		try {
			if (_pc.isCrown() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(0);
			} else if (_pc.isKnight() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(1);
			} else if (_pc.isElf() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(2);
			} else if (_pc.isWizard() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(3);
			} else if (_pc.isDarkelf() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(4);
			} else if (_pc.isBlackwizard() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(5);
			} else if (_pc.isDragonknight() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(6);
			} else if (_pc.is전사() && _weaponType != 0) {
				dmg += CharacterBalance.getInstance().getCharacterBalance(7);
			}
		} catch (Exception e) {
			System.out.println("Character Add Damege Error");
		}

		L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(_pc.getMapId());

		if (sm != null && _pc.getWeapon() != null && _pc.getWeapon().getItem().getType() != 7) {
			dmg -= sm.getDmgReduction();
			if (dmg <= 0)
				dmg = 1;
		}
		
		/** 발라카스의 일격 **/
		if (_pc.getInventory().checkEquipped(111137) || _pc.getInventory().checkEquipped(111141) 
		 || _pc.getInventory().checkEquipped(111140) || _pc.getInventory().checkEquipped(22208) 
		|| _pc.getInventory().checkEquipped(22209) || _pc.getInventory().checkEquipped(22210) 
		|| _pc.getInventory().checkEquipped(22211)) {
			int chance = _random.nextInt(100) +1;	
			if(chance <= 7){
				//weaponDamage = weaponMaxDamage + _weaponAddDmg + 10;
				dmg *= 1.2;
				_pc.sendPackets(new S_SkillSound(_pc.getId(), 15841)); 
				Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 15841)); 
			}
		}		
		dmg -= calcNpcDamageReduction();

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
		if (castleId > 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		// TODO 펫 대미지
		if (!isNowWar) {
			if (_targetNpc instanceof L1PetInstance) {
				dmg /= 8;
			}
			// TODO 서먼몬스터 대미지
			if (_targetNpc instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) _targetNpc;
				if (summon.isExsistMaster()) {
					dmg /= 5;// 8
				}
			}
		}

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(PHANTASM)) {
			_targetNpc.removeSkillEffect(PHANTASM);
		}

		boolean isCounterBarrier = false;
		L1Magic magic = null;
		if (_targetNpc.hasSkillEffect(L1SkillId.BOSS_COUNTER_BARRIER) && dmg > 0) {
			magic = new L1Magic(_targetNpc, _pc);
			boolean isProbability = magic.calcProbabilityMagic(L1SkillId.COUNTER_BARRIER);
			if (isProbability) {
				isCounterBarrier = true;
			}
		}
		if (isCounterBarrier) {
			NpcactionCounterBarrier(_target, _pc);
			commitBossCounterBarrier(_target, _pc);
		}

		if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
			if (_pc.getAccount() != null && _pc.getAccount().getGrangKinAngerStat() != 0 && dmg > 0) {
				dmg = _pc.getAccount().getGrangKinAngerDmgCalc(dmg);
			}
		}

		if (dmg <= 0) {
			_isHit = false;
		}
	//	if (secondw) {
			switch (_weaponId) {
			
			case 12: // 바람칼날의 단검
				dmg += L1WeaponSkill.바람칼날의단검(_pc, _target, _weaponEnchant);
				break;
				
			case 61: // 진명황의 집행검
				dmg += L1WeaponSkill.진명황의집행검(_pc, _target, _weaponEnchant);
				break;
				
			case 86: // 붉은그림자의 이도류
			    dmg += L1WeaponSkill.붉은그림자의이도류(_pc, _target, _weaponEnchant);
			    break;
				
			case 134: // 수정결정체 지팡이
				dmg += L1WeaponSkill.수정결정체지팡이(_pc, _target, _weaponEnchant);
				break;
				
			case 202011: // 가이아의 격노
				dmg += L1WeaponSkill.가이아의격노(_pc, _target, _weaponEnchant);
				break;
				
			case 202012: // 히페리온의 절망
				dmg += L1WeaponSkill.히페리온의절망(_pc, _target, _weaponEnchant);
				break;
				
			case 202013: // 크로노스의 공포
				dmg += L1WeaponSkill.크로노스의공포(_pc, _target, _weaponEnchant);
				break;
				
			case 202014: // 타이탄의 분노
				dmg += L1WeaponSkill.타이탄의분노(_pc, _target, _weaponEnchant);
				break;

			case 543: // 지배자의 지팡이
				dmg += L1WeaponSkill.지배자의지팡이(_pc, _target, _weaponEnchant, 10405);
				break;	
				
			case 202003: // 제로스의 지팡이
				dmg += L1WeaponSkill.제로스의지팡이1(_pc, _target, _weaponEnchant, 11760);
				break;
			
			case 7000017: // 안타라스의 지팡이
				dmg += L1WeaponSkill.안타라스의지팡이(_pc, _target, _weaponEnchant, 4906);
				break;
				
			case 2944: // 아인하사드의 섬광(NPC)
				dmg += L1WeaponSkill.아인하사드의섬광(_pc, _target, _weaponEnchant);
				break;
				
			case 2945: // 그랑카인의 심판(NPC)
				dmg += L1WeaponSkill.그랑카인의심판(_pc, _target, _weaponEnchant);
				break;
				
			case 2946: // 사이하의 집념(NPC)
				dmg += L1WeaponSkill.사이하의집념(_pc, _target, _weaponEnchant);
				break;
				
			case 2947: // 실렌의 결의(NPC)
				dmg += L1WeaponSkill.실렌의결의(_pc, _target, _weaponEnchant);
				break;
				
			case 2948: // 에바의 서약(NPC)
				dmg += L1WeaponSkill.에바의서약(_pc, _target, _weaponEnchant);
				break;
				
			case 2949: // 파아그리오의 공포(PC)
				dmg += L1WeaponSkill.파아그리오의공포(_pc, _target, _weaponEnchant);
				break;
				
			case 2950: // 마프르의 고뇌(PC)
				dmg += L1WeaponSkill.마프르의고뇌(_pc, _target, _weaponEnchant);
				break;
				
			case 2951: // 단테스의 시련(PC)
				dmg += L1WeaponSkill.단테스의시련(_pc, _target, _weaponEnchant);
				break;
				
			case 2952: // 테이아의 혼돈(PC)
				dmg += L1WeaponSkill.테이아의혼돈(_pc, _target, _weaponEnchant);
				break;
				
			case 2953: // 아우라키아의 초월(PC)
				dmg += L1WeaponSkill.아우라키아의초월(_pc, _target, _weaponEnchant);
				break;
				
			case 2954: // 아인하사드의 구원(PC)
				dmg += L1WeaponSkill.아인하사드의구원(_pc, _target, _weaponEnchant);
				break;
				
			default:
				dmg += L1WeaponSkill.getWeaponSkillDamage(_pc, _target, _weaponId);
				break;
			}
		return (int) dmg;
	}

	// ●●●● NPC 로부터 플레이어에의 대미지 산출 ●●●●
	private int calcNpcPcDamage() {
		if (_npc == null || _targetPc == null)
			return 0;

		int lvl = this._npc.getLevel();
		double dmg = 0.0D;
		int status = 0;
		// 공격거리가 10이하이면 근거리로 인식
		if (this._npc.getNpcTemplate().get_ranged() < 10)
			status = this._npc.getAbility().getTotalStr();
		else {
			// 공격거리가 10이상이면 원거리로 인식
			status = this._npc.getAbility().getTotalDex();
		}

		// 스텟40이상
		/*
		 * if (status >= 40) { if (status < 80)// 스텟 80이하 status = (int) (status
		 * * 1.5D);// 대미지 else if (status < 120)// 스텟120이상 status *= 2;// 스텟 곱2배
		 * else status = (int) (status * 2.5D); }
		 */

		dmg = status + _random.nextInt(lvl / 2) + 1;

		if (_npc instanceof L1PetInstance) {
			dmg += (lvl / 15); // 펫은 LV16마다 추가 타격
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		}
		dmg += _npc.getDmgup();

		if (isUndeadDamage()) {
			dmg *= 1.2;
		}

		L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(_npc.getMapId());
		if (sm != null) {
			dmg *= sm.getDmgRate();
		}

		// TODO 월드 전체 몬스터 대미지 조정 상세
		dmg *= getLeverage() * 0.1D;// 올리면 쌔진다.
		if (_targetPc.getLevel() >= 1 && _targetPc.getLevel() <= 69) {
			dmg *= Config.npcdmg1 * 0.01D;// 올리면 쌔진다.
		} else if (_targetPc.getLevel() >= 70 && _targetPc.getLevel() <= 79) {
			dmg *= Config.npcdmg2 * 0.01D;// 올리면 쌔진다.
		} else if (_targetPc.getLevel() >= 80 && _targetPc.getLevel() <= 90) {
			dmg *= Config.npcdmg3 * 0.01D;// 올리면 쌔진다.
		} else {
			dmg *= 60 * 0.01D;// 올리면 쌔진다.
		}

		// TODO 월드 전체 몬스터 대미지 관련
		dmg = dmg * getLeverage() / 15;// 몬스터 물리데미지 올리면 약해진다.
		// dmg -= calcPcDefense();//몬스터때릴때 PC ac에따라서 대미지가 깎임 본섭은 안깎임

		if (_npc.isWeaponBreaked()) { // NPC가 웨폰브레이크중.
			dmg *= 0.5;
		}

		if (_targetPc.hasSkillEffect(L1SkillId.메티스정성스프))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.메티스정성요리))
			dmg -= 5;
		if (_targetPc.hasSkillEffect(L1SkillId.메티스축복주문서))
			dmg -= 3;

		try {
			if (_targetPc.isCrown()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(0);
			} else if (_targetPc.isKnight()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(1);
			} else if (_targetPc.isElf()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(2);
			} else if (_targetPc.isWizard()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(3);
			} else if (_targetPc.isDarkelf()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(4);
			} else if (_targetPc.isBlackwizard()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(5);
			} else if (_targetPc.isDragonknight()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(6);
			} else if (_targetPc.is전사()) {
				dmg -= CharacterReduc.getInstance().getCharacterReduc(7);
			}
		} catch (Exception e) {
			System.out.println("Character Add Reduction Error");
		}

		// 애완동물, 사몬으로부터 플레이어에 공격
		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
		if (castleId > 0) {
			isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		// TODO 펫 대미지
		if (!isNowWar) {
			if (_npc instanceof L1PetInstance) {
				dmg /= 8;
			}
			// TODO 서먼몬스터 대미지
			if (_npc instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) _npc;
				if (summon.isExsistMaster()) {
					dmg /= 5; // 8
				}
			}
		}

		addNpcPoisonAttack(_npc, _targetPc);

		if (_npc instanceof L1PetInstance || _npc instanceof L1SummonInstance || _npc instanceof MJCompanionInstance) {
			if (_targetPc.getZoneType() == 1) {
				_isHit = false;
			}
		}

		/** 보스 아머브레이크 */
		if (_targetPc.hasSkillEffect(WIDE_ARMORBREAK)) { // 보스 아머브레이크
			if (_weaponType != 20 && _weaponType != 62) {
				dmg *= 1.58;
			}
		}

		/** 대상 Buff에 따른 대미지 연산 **/
		dmg = toPcBuffDmg(dmg);
		if (dmg > 0 && _targetPc.hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg -= (dmg * _targetPc.getImmuneReduction());
			// dmg = IntRange.ensure(dmg, 0, high);
		}

		if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
			if (_targetPc.getAccount() != null && _targetPc.getAccount().getGrangKinAngerStat() != 0 && dmg > 0) {
				dmg *= _targetPc.getAccount().getGrangKinAngerReducCalc();
			}
		}
		
		MJArmorClass armor_class = MJArmorClass.find_armor_class(_targetPc.getAC().getAc());
		if(armor_class != null)
			dmg -= armor_class.get_to_npc_reduction();
			
		double total_reduction = 0;
		Object[] dollList1 = _targetPc.getDollList().values().toArray(); // 마법인형에
		for (Object dollObject : dollList1) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			total_reduction += doll.getDamageReductionByDoll();
		}
		total_reduction += _targetPc.getDamageReductionByArmor(); // 방어구에 의한 대미지 감소
		total_reduction += _targetPc.getDamageReduction(); // 방어구에 의한 대미지 감소
		total_reduction *= Config.PHYSICAL_REDUCTION_RATION;
		if(_targetPc.hasSkillEffect(L1SkillId.INFERNO) && _npc.getNpcTemplate().get_ranged() < 10) {
			L1ItemInstance target_weapon = _targetPc.getWeapon();
			if(target_weapon != null && target_weapon.getItem().getType() == 1) {
				int enchant = target_weapon.getEnchantLevel();
				if(MJRnd.isWinning(1000000, Config.INFERNO_PROBABILITY)) {
					int weapon_index = MJRnd.next(4);
					_targetPc.send_effect(Config.INFERNO_EFFECTS[weapon_index]);
					int inferno_damage = target_weapon.getItem().getDmgSmall() + 
							target_weapon.getEnchantLevel() + 
							_targetPc.getDmgRate() + 
							_targetPc.getDmgup() + 
							_targetPc.getDmgupByArmor();
					_npc.receiveDamage(_targetPc, (int)((weapon_index + 1) * inferno_damage));
					_npc.send_action(ActionCodes.ACTION_Damage);
				}
			}
		}
		
		dmg = Math.max(dmg - total_reduction, 0);
		if (dmg <= 0) {
			_isHit = false;
		}

		return (int) dmg;
	}

	// ●●●● NPC 로부터 NPC 에의 대미지 산출 ●●●●
	private int calcNpcNpcDamage() {
		if (_targetNpc == null || _npc == null)
			return 0;

		int lvl = _npc.getLevel();
		double dmg = 0;

		if (_npc.getNpcId() >= 7320138 && _npc.getNpcId() <= 7320147) {
			if (_npc.getCurrentHp() < _npc.getMaxHp()) {
				int adddmg = (_npc.getMaxHp() - _npc.getCurrentHp()) / Config.summon_hpdmg;
				if (adddmg < 0) {
					adddmg = 1;
				}
				dmg += adddmg;

				// System.out.println(Config.summon_hpdmg);
				// System.out.println("dmg: "+dmg);
				// System.out.println("adddmg: "+adddmg);
			}
		}
		if (_npc instanceof L1PetInstance) {
			dmg = _random.nextInt(_npc.getNpcTemplate().get_level()) + _npc.getAbility().getTotalStr() / 2 + 1;
			dmg += (lvl / 16); // 펫은 LV16마다 추가 타격
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		} else if (_npc instanceof L1SummonInstance) {
			dmg = _random.nextInt(lvl) + _npc.getAbility().getTotalStr() + 5;
		} else {
			dmg = _random.nextInt(lvl) + _npc.getAbility().getTotalStr() / 2 + 1;
		}

		if (isUndeadDamage()) {
			dmg *= 1.1;
		}

		dmg = dmg * getLeverage() / 10;

		dmg -= calcNpcDamageReduction();

		if (_npc.isWeaponBreaked()) { // NPC가 웨폰브레이크중.
			dmg /= 2;
		}

		addNpcPoisonAttack(_npc, _targetNpc);

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}

		if (dmg <= 0) {
			_isHit = false;
		}

		return (int) dmg;
	}

	// ●●●● 플레이어의 대미지 강화 마법 ●●●●
	private double calcBuffDamage(double dmg) {
		return dmg;
	}

	/** 무기 속성 인챈에 따른 효과 부여(PC-PC) **/
	private double 피시속성인첸트효과() {
		int Attr = _weaponAttrLevel;
		double AttrDmg = 0;
		switch (_weaponAttrLevel) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			// AttrDmg += (Attr - 1) * 2+1;
			AttrDmg += (Attr - 1) + 1;
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getFire() / 100;
			break;
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			// AttrDmg += (Attr - 6) * 2+1;
			AttrDmg += (Attr - 6) + 1;
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getWater() / 100;

			break;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			// AttrDmg += (Attr - 11) * 2+1;
			AttrDmg += (Attr - 11) + 1;
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getWind() / 100;
			break;
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
			// AttrDmg += (Attr - 16) * 2+1;
			AttrDmg += (Attr - 16) + 1;
			if (_arrow != null && _arrow.getItemId() == 3000516) {
				AttrDmg += 3;
			}
			AttrDmg -= AttrDmg * _targetPc.getResistance().getEarth() / 100;
			break;
		default:
			AttrDmg = 0;
			break;
		}
		return AttrDmg;
	}

	/** 무기 속성 인챈에 따른 효과 부여(PC-NPC) **/
	private int 몬스터속성인첸트효과() {
		int AttrDmg = 0;
		int Attr = _weaponAttrLevel;
		int NpcWeakAttr = _targetNpc.getNpcTemplate().get_weakAttr();
		switch (NpcWeakAttr) {
		case 1: // 땅 취약 몬스터
			if (Attr >= 15 && Attr <= 20) {
				// AttrDmg += 1 + (Attr - 15) * 2;
				AttrDmg += 1 + (Attr - 15);
				if (_arrow != null && _arrow.getItemId() == 3000516) {
					AttrDmg += 3;
				}
			}
			break;
		case 2: // 물 취약 몬스터
			if (Attr >= 6 && Attr <= 10) {
				// AttrDmg += 1 + (Attr - 6) * 2;
				AttrDmg += 1 + (Attr - 6);
				if (_arrow != null && _arrow.getItemId() == 3000516) {
					AttrDmg += 3;
				}
			}
			break;
		case 4: // 불 취약 몬스터
			if (Attr >= 1 && Attr <= 5) {
				// AttrDmg += (Attr - 1) * 2 + 1;
				AttrDmg += 1 + (Attr - 1);
				if (_arrow != null && _arrow.getItemId() == 3000516) {
					AttrDmg += 3;
				}
			}
			break;
		case 8: // 바람 취약 몬스터
			if (Attr >= 11 && Attr <= 15) {
				// AttrDmg += 1 + (Attr - 11) * 2;
				AttrDmg += 1 + (Attr - 11);
				if (_arrow != null && _arrow.getItemId() == 3000516) {
					AttrDmg += 3;
				}
			}
			break;
		default:
			AttrDmg = 0;
			break;
		}
		return AttrDmg;
	}

	// ●●●● 플레이어의 AC에 의한 대미지 경감 ●●●●
	private int calcPcDefense() {
		int ac = Math.max(0, 5 - _targetPc.getAC().getAc());
		int acDefMax = _targetPc.getClassFeature().getAcDefenseMax(ac);
		return _random.nextInt(acDefMax + 1);
	}

	// ●●●● NPC의 대미지 축소에 의한 경감 ●●●●
	private int calcNpcDamageReduction() {
		return _targetNpc.getNpcTemplate().get_damagereduction();
	}

	// ●●●● 무기의 재질과 축복에 의한 추가 대미지 산출 ●●●●
	private int calcMaterialBlessDmg() {
		int damage = 0;
		int undead = _targetNpc.getNpcTemplate().get_undead();
		if ((_weaponMaterial == 14 || _weaponMaterial == 17 || _weaponMaterial == 22) && (undead == 1 || undead == 3)) { // 은·미스릴·오리하르콘,
			damage += _random.nextInt(20) + 1;
		}
		if (_weaponBless == 0 && (undead == 1 || undead == 2 || undead == 3)) { // 축복
			damage += _random.nextInt(4) + 1;
		}
		if (weapon != null && _weaponType != 20 && _weaponType != 62 && weapon.getHolyDmgByMagic() != 0 && (undead == 1 || undead == 3)) {
			damage += weapon.getHolyDmgByMagic();
		}
		return damage;
	}

	// ●●●● NPC의 안 데드의 야간 공격력의 변화 ●●●●
	private boolean isUndeadDamage() {
		boolean flag = false;
		int undead = _npc.getNpcTemplate().get_undead();
		boolean isNight = L1GameTimeClock.getInstance().getGameTime().isNight();
		if (isNight && (undead == 1 || undead == 3)) {
			flag = true;
		}
		return flag;
	}

	// TODO ●●●● NPC의 독공격을 부가 ●●●●
	private void addNpcPoisonAttack(L1Character attacker, L1Character target) {
		if (_npc.getNpcTemplate().get_poisonatk() != 0) { // 독공격 있어
			if (15 >= _random.nextInt(100) + 1) { // 15%의 확률로 독공격
				if (_npc.getNpcTemplate().get_poisonatk() == 1) { // 통상독 3초 주기에
																	// 대미지 5
					L1DamagePoison.doInfection(attacker, target, 3000, 5, false);
				} else if (_npc.getNpcTemplate().get_poisonatk() == 2) { // 침묵독
					L1SilencePoison.doInfection(target);
				} else if (_npc.getNpcTemplate().get_poisonatk() == 4) { // 마비독
																			// 20초
																			// 후에
																			// 16초간
					
					L1ParalysisPoison.doInfection(target, 9999);
				}
			}
		} else if (_npc.getNpcTemplate().get_paralysisatk() != 0) { // / 마비 공격
																	// 있어
		}
	}

	// ■■■■ PC의 독공격을 부가 ■■■■
	public void addPcPoisonAttack(L1Character attacker, L1Character target) {
		int chance = _random.nextInt(100) + 1;
		if ((_weaponId == 13 || _weaponId == 44 // FOD, 고대의 다크 에르프 소도
				|| (_weaponId != 0 && _pc.hasSkillEffect(ENCHANT_VENOM))) // 인챈트
																			// 베놈
				&& chance <= 10) {
			L1DamagePoison.doInfection(attacker, target, 3000, 30, false);
		}
	}

	/* ■■■■■■■■■■■■■■ 공격 모션 송신 ■■■■■■■■■■■■■■ */

	public void action() {
		try {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				actionPc();
			} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
				actionNpc();
			}
		} catch (Exception e) {
		}
	}

	// ●●●● 플레이어의 공격 모션 송신 ●●●●
	private void actionPc() {
		int spriteId = _pc.getCurrentSpriteId();
		_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 방향세트

		/** 기존꺼 **/
		/*	if (_weaponType == 20) {
				if (!_pc.glanceCheck(_targetX, _targetY) || !_target.glanceCheck(_pc.getX(), _pc.getY())) {
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, ActionCodes.ACTION_BowAttack, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, ActionCodes.ACTION_BowAttack, _targetX, _targetY, _isHit));
					_isHit = false;
					return;
				}
				if (_arrow != null) {// 해당 변신 했을시 이미지를 체크해서 화살 이미지를 바꾼다
					if (!_pc.noPlayerCK)
						_pc.getInventory().removeItem(_arrow, 1);
					if (spriteId == 7967) {
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
					} else if (spriteId == 11402 || spriteId == 8900) { // 75렙 변신
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					} else if (spriteId == 11406 || spriteId == 8913) {// 80렙 변신
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					} else if (spriteId == 13631) {// 82렙 변신
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					} else if (spriteId == 13635) {// 85렙 변신
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					} else if (spriteId == 15814) { // 하이엘프 화살
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					} else if (spriteId == 16002) {// 86렙 변신
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					} else if (spriteId == 16074) {// 88렙 변신
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					} else {
						_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 66, _targetX, _targetY, _isHit));
						Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 66, _targetX, _targetY, _isHit));
					}
					if (_isHit) {
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					}
				} else if (_weaponId == 190) {// 해당 아이템착용시 아이템을 체크해서 화살 이미지를 바꾼다
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 2349, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 2349, _targetX, _targetY, _isHit));
					if (_isHit) {
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					}
				} else if (_weaponId == 202011) {// 가이아의격노
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
					if (_isHit) {
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					}
				} else if (_weaponId == 10000) {
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8771, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8771, _targetX, _targetY, _isHit));
					if (_isHit) {
						Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
					}
				}
			} else if (_weaponType == 62 && _sting != null) {// 해당 변신 했을시 이미지를 체크해서
				_pc.getInventory().removeItem(_sting, 1);
				if (spriteId == 7967) {
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 7972, _targetX, _targetY, _isHit));
				} else if (spriteId == 11402 || spriteId == 8900) {// 75렙 변신
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8904, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 11406 || spriteId == 8913) {// 80렙 변신
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 8916, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 13631) {// 82렙 변신
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 13656, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 13635) {// 85렙 변신
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 13658, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 15814) { // 하이엘프 화살
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 12243, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 16002) {// 86렙 변신
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else if (spriteId == 16074) {// 88렙 변신
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 16078, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				} else {
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, 2989, _targetX, _targetY, _isHit));
					Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, 2989, _targetX, _targetY, _isHit));
				}
				if (_isHit) {
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				}*/ 
		
		/** 기존꺼 **/
		
		/** 바꾼거 **/
		int effectId = 0;
		if (_weaponType == 20 || _weaponType == 62) {
			if (!_pc.glanceCheck(_targetX, _targetY) || !_target.glanceCheck(_pc.getX(), _pc.getY())) {
			_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, ActionCodes.ACTION_BowAttack, _targetX, _targetY, _isHit));
			Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, ActionCodes.ACTION_BowAttack, _targetX, _targetY, _isHit));
						_isHit = false;
					return;
					}			
	             if (!_pc.noPlayerCK && _weaponType == 20 && _arrow != null) _pc.getInventory().removeItem(_arrow, 1);	
				else if (!_pc.noPlayerCK && _weaponType == 62 && _sting != null)  _pc.getInventory().removeItem(_sting, 1);   
				else if (_weaponType == 62 && _sting == null) return;
						switch(_pc.getCurrentSpriteId()) {
						case 7967: 		effectId = 7972;	    break;
						case 11402:                                           //75렙변신
						case 8900:		effectId = 8904;	    break;      //75렙변신
						case 11406:                                          // 80렙 변신

						case 8913:		effectId = 8916;	    break;      // 80렙 변신

						case 13631:		effectId = 13656;	    break;   // 82렙 변신

						case 13635:		effectId = 13658;	    break;  // 85렙 변신

						case 15814:		effectId = 12243;		break;  // 하이엘프 화살

						case 16002:                                              // 86렙 변신

						case 16074:     effectId = 16078;		break;       // 88렙 변신

						default: 

							if (_weaponType == 20)	effectId = 66;
							else if (_weaponType == 62) effectId = 2989;
							break;
					 }
					if(_arrow == null && _weaponId == 190)	        effectId = 2349;  //사이하의활
					else if(_arrow == null && _weaponId == 202011)	effectId = 8916; // 가이아의격노
					else if(_arrow == null && _weaponId == 10000)   effectId = 8771;
					_pc.sendPackets(new S_UseArrowSkill(_pc, _targetId, effectId, _targetX, _targetY, _isHit));
		                    Broadcaster.broadcastPacket(_pc, new S_UseArrowSkill(_pc, _targetId, effectId, _targetX, _targetY, _isHit));
				   	if (_isHit)	Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
				   	
				   	/** 바꾼거 **/

		} else if (_weaponType2 == 17) {
			ServerBasePacket pck = null;
			if (!_isCritical) {
				pck = S_Attack.getKeylink2(_pc, _target, _attackType, _isHit);
				_pc.sendPackets(pck, false);
				_pc.broadcastPacket(pck);
			}
		} else {
			int actid = ActionCodes.ACTION_Attack;
			if (_isHit) {
				if (!_isCritical) {
					_pc.sendPackets(new S_AttackPacket(_pc, _targetId, actid, _attackType));
					Broadcaster.broadcastPacket(_pc, new S_AttackPacket(_pc, _targetId, actid, _attackType));
				}
				Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _pc);
			} else {
				if (_targetId > 0) {
					_pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
					Broadcaster.broadcastPacket(_pc, new S_AttackMissPacket(_pc, _targetId));
				} else {
					_pc.sendPackets(new S_AttackPacket(_pc, 0, actid));
					Broadcaster.broadcastPacket(_pc, new S_AttackPacket(_pc, 0, actid));
				}
			}
		}
	}

	// ●●●● NPC의 공격 모션 송신 ●●●●
	private void actionNpc() {
		int _npcObjectId = _npc.getId();
		int bowActId = 0;
		int actId = 0;

		_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 방향세트

		// 타겟과의 거리가 2이상 있으면 원거리 공격
		boolean isLongRange = (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) > 1);
		bowActId = _npc.getNpcTemplate().getBowActId();

		if (getActId() > 0) {
			actId = getActId();
		} else {
			actId = ActionCodes.ACTION_Attack;
		}

		if (isLongRange && bowActId > 0) {
			Broadcaster.broadcastPacket(_npc, new S_UseArrowSkill(_npc, _targetId, bowActId, _targetX, _targetY, _isHit));
		} else {
			if (_isHit) {
				if (getGfxId() > 0) {
					Broadcaster.broadcastPacket(_npc, new S_UseAttackSkill(_target, _npcObjectId, getGfxId(), _targetX, _targetY, actId));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _npc);
				} else {
					Broadcaster.broadcastPacket(_npc, new S_AttackPacketForNpc(_target, _npcObjectId, actId));
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetId, ActionCodes.ACTION_Damage), _npc);
				}
			} else {
				if (getGfxId() > 0) {
					Broadcaster.broadcastPacket(_npc, new S_UseAttackSkill(_target, _npcObjectId, getGfxId(), _targetX, _targetY, actId, 0));
				} else {
					Broadcaster.broadcastPacket(_npc, new S_AttackMissPacket(_npc, _targetId, actId));
				}
			}
		}
	}

	// 나는 일 도구(화살, 스팅)가 미스였다고 나무의 궤도를 계산
	public void calcOrbit(int cx, int cy, int head) // 기점 X 기점 Y 지금 향하고 있는 방향
	{
		float dis_x = Math.abs(cx - _targetX); // X방향의 타겟까지의 거리
		float dis_y = Math.abs(cy - _targetY); // Y방향의 타겟까지의 거리
		float dis = Math.max(dis_x, dis_y); // 타겟까지의 거리
		float avg_x = 0;
		float avg_y = 0;
		if (dis == 0) { // 목표와 같은 위치라면 향하고 있는 방향에 진곧
			switch (head) {
			case 1:
				avg_x = 1;
				avg_y = -1;
				break;
			case 2:
				avg_x = 1;
				avg_y = 0;
				break;
			case 3:
				avg_x = 1;
				avg_y = 1;
				break;
			case 4:
				avg_x = 0;
				avg_y = 1;
				break;
			case 5:
				avg_x = -1;
				avg_y = 1;
				break;
			case 6:
				avg_x = -1;
				avg_y = 0;
				break;
			case 7:
				avg_x = -1;
				avg_y = -1;
				break;
			case 0:
				avg_x = 0;
				avg_y = -1;
				break;
			default:
				break;
			}
		} else {
			avg_x = dis_x / dis;
			avg_y = dis_y / dis;
		}

		int add_x = (int) Math.floor((avg_x * 15) + 0.59f); // 상하 좌우가 조금 우선인 둥근
		int add_y = (int) Math.floor((avg_y * 15) + 0.59f); // 상하 좌우가 조금 우선인 둥근

		if (cx > _targetX) {
			add_x *= -1;
		}
		if (cy > _targetY) {
			add_y *= -1;
		}

		_targetX = _targetX + add_x;
		_targetY = _targetY + add_y;
	}

	/* ■■■■■■■■■■■■■■■ 계산 결과 반영 ■■■■■■■■■■■■■■■ */

//	public void commit() {
//		if (_isHit) {
//			try {
//				if (_calcType == PC_PC || _calcType == NPC_PC) {
//					commitPc();
//				} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
//					commitNpc();
//				}
//			} catch (Exception e) {
//			}
//		}
//
//		// 대미지치 및 명중율 확인용 메세지
//		if (!Config.ALT_ATKMSG) {
//			return;
//		}
//		if (Config.ALT_ATKMSG) {
//			if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
//				return;
//			}
//			if ((_calcType == PC_PC || _calcType == NPC_PC) && !_targetPc.isGm()) {
//				return;
//			}
//		}
//		String msg0 = "";
//		String msg1 = "";
//		String msg2 = "";
//		String msg3 = "";
//		if (_calcType == PC_PC || _calcType == PC_NPC) { // 어텍커가 PC의 경우
//			msg0 = _pc.getName();
//		} else if (_calcType == NPC_PC) { // 어텍커가 NPC의 경우
//			msg0 = _npc.getName();
//		}
//
//		if (_calcType == NPC_PC || _calcType == PC_PC) { // 타겟이 PC의 경우
//			msg3 = _targetPc.getName();
//			msg1 = "피" + _targetPc.getCurrentHp() + "/명중 " + _hitRate;
//		} else if (_calcType == PC_NPC) { // 타겟이 NPC의 경우
//			msg3 = _targetNpc.getName();
//			msg1 = "피" + _targetNpc.getCurrentHp() + "/명중 " + _hitRate;
//		}
//		msg2 = "/대미지 " + _damage;
//
//		if (_calcType == PC_PC || _calcType == PC_NPC) { // 어텍커가 PC의 경우
//			_pc.sendPackets(new S_SystemMessage("" + msg0 + ">" + msg3 + "" + msg2 + "/" + msg1));
//		}
//		if (_calcType == NPC_PC || _calcType == PC_PC) { // 타겟이 PC의 경우
//			_targetPc.sendPackets(new S_SystemMessage("" + msg0 + ">" + msg3 + "" + msg2 + "/" + msg1));
//		}
//	}
	
	public void commit() {
	    if (_isHit) {
	        try {
	            if (_calcType == PC_PC || _calcType == NPC_PC) {
	                commitPc();
	            } else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
	                commitNpc();
	            }
	        } catch (Exception e) {
	            // 예외 처리 또는 로깅
	        }
	    }

	    if (!Config.ALT_ATKMSG) {
	        return;
	    }

	    if (_pc == null || (_targetPc == null && _targetNpc == null)) {
	        return;
	    }

	    try {
	        if (_damage > 0) {
	            // 이펙트 발동 여부 체크를 위한 플래그
	            boolean effectDisplayed = false;

	            // 크리티컬 히트 발생 시
	            if (_isCritical) { // 물리 크리티컬&마법무기크리티컬
	                int effectId = -1; // 사용할 이펙트 ID 초기화
	                // 순수 대미지 값에 따른 이펙트 발동
	                if (_damage >= 300) { // 대미지가 300 이상일 경우
	                    effectId = 18639; // damagex2 이펙트 ID
	                } else if (_damage >= 250) { // 대미지가 250 이상일 경우
	                    effectId = 18637; // damageX1.5 이펙트 ID
	                } else if (_damage >= 200) { // 대미지가 200 이상일 경우
	                    effectId = 18635; // Great 이펙트 ID
	                }
	                // 결정된 이펙트 ID로 이펙트 발동
	                if (effectId != -1) {
	                    if (_calcType == PC_NPC || _calcType == NPC_NPC) {
	                        _pc.sendPackets(new S_SkillSound(_targetNpc.getId(), effectId));
	                    } else if (_calcType == PC_PC || _calcType == NPC_PC) {
	                        _pc.sendPackets(new S_SkillSound(_targetPc.getId(), effectId));
	                    }
	                    effectDisplayed = true;
	                }
	            }
	                    
//	            if (!effectDisplayed) {
//	                // 기존 대미지 이펙트 표시 로직
//	                boolean startEffectDisplay = false;
//	                for (int pos = 4; pos >= 0; pos--) {
//	                    int effectNumber = (int) ((_damage / Math.pow(10, pos)) % 10);
//	                    if (effectNumber > 0 || startEffectDisplay) {
//	                        startEffectDisplay = true;
//	                        int effectId = 18584 + effectNumber + (pos * 10);
//	                        if (_calcType == PC_NPC || _calcType == NPC_NPC) {
//	                            _pc.sendPackets(new S_SkillSound(_targetNpc.getId(), effectId));
//	                        } else if (_calcType == PC_PC || _calcType == NPC_PC) {
//	                            _pc.sendPackets(new S_SkillSound(_targetPc.getId(), effectId));
//	                        }
//	                    }
//	                }
//	            }
	        } else {
	            // 미스 이펙트 전송
	            int missEffectId = 13418;
	            if (_calcType == PC_NPC || _calcType == NPC_NPC) {
	                _pc.sendPackets(new S_SkillSound(_targetNpc.getId(), missEffectId));
	            } else if (_calcType == PC_PC || _calcType == NPC_PC) {
	                _pc.sendPackets(new S_SkillSound(_targetPc.getId(), missEffectId));
	                _pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
	            }
	        }
	    } catch (Exception e) {
	        // 예외 처리
	    }
	}

	// ●●●● 플레이어에 계산 결과를 반영 ●●●●
	private void commitPc() {
		if (_calcType == PC_PC) {
			if (MJCommons.isUnbeatable(_targetPc))
				_damage = 0;
			_targetPc.receiveDamage(_pc, _damage);
		} else if (_calcType == NPC_PC) {
			if (MJCommons.isUnbeatable(_targetPc))
				_damage = 0;
			_targetPc.receiveDamage(_npc, _damage);
		}
	}

	// ●●●● NPC에 계산 결과를 반영 ●●●●
	private void commitNpc() {
		if (_calcType == PC_NPC) {
			if (MJCommons.isUnbeatable(_targetNpc))
				_damage = 0;
			damageNpcWeaponDurability(); // 무기를 손상시킨다.
			_targetNpc.receiveDamage(_pc, _damage);
		} else if (_calcType == NPC_NPC) {
			if (MJCommons.isUnbeatable(_targetNpc))
				_damage = 0;
			_targetNpc.receiveDamage(_npc, _damage);
		}
	}

	/* ■■■■■■■■■■■■■■■ 카운터 배리어 ■■■■■■■■■■■■■■■ */
	// ■■■■ 카운터 배리어시의 공격 모션 송신 ■■■■
	public void actionCounterBarrier() {
		if (_calcType == PC_PC) {
			if (_pc == null)
				return;
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 방향세트
			_pc.sendPackets(new S_AttackMissPacket(_pc, _targetId));
			_pc.broadcastPacket(new S_AttackMissPacket(_pc, _targetId), _target);
			_pc.sendPackets(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			_pc.broadcastPacket(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			if (_targetPc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
				_targetPc.send_effect(17220);
			} else {
				_targetPc.send_effect(10710);
			}
		} else if (_calcType == NPC_PC) {
			if (_npc == null || _target == null)
				return;
			int actId = 0;
			_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 방향세트
			if (getActId() > 0) {
				actId = getActId();
			} else {
				actId = ActionCodes.ACTION_Attack;
			}
			if (getGfxId() > 0) {
				_npc.broadcastPacket(new S_UseAttackSkill(_target, _npc.getId(), getGfxId(), _targetX, _targetY, actId, 0), _target);
			} else {
				_npc.broadcastPacket(new S_AttackMissPacket(_npc, _targetId, actId), _target);
			}
			_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage));
			if (_targetPc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
				_targetPc.send_effect(17220);
			} else {
				_targetPc.send_effect(10710);
			}
		}
	}

	public void NpcactionCounterBarrier(L1Character attacker, L1PcInstance pc) {
		if (pc == null || _target == null)
			return;
		int actId = 0;
		pc.setHeading(pc.targetDirection(_targetX, _targetY)); // 방향세트
		if (getActId() > 0) {
			actId = getActId();
		} else {
			actId = ActionCodes.ACTION_Attack;
		}
		if (getGfxId() > 0) {
			pc.broadcastPacket(new S_UseAttackSkill(_target, pc.getId(), getGfxId(), _targetX, _targetY, actId, 0), _target);
		} else {
			pc.broadcastPacket(new S_AttackMissPacket(pc, _targetId, actId), _target);
		}
		pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
		attacker.send_effect(17220);
	}

	// ■■■■ 모탈바디 발동시의 공격 모션 송신 ■■■■
	public void actionMortalBody() {
		if (_calcType == PC_PC) {
			if (_pc == null || _target == null)
				return;
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY)); // 방향세트
			S_UseAttackSkill packet = new S_UseAttackSkill(_pc, _target.getId(), 6519, _targetX, _targetY, ActionCodes.ACTION_Attack, false);
			_pc.sendPackets(packet);
			_pc.broadcastPacket(packet, _target);
			_pc.sendPackets(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			_pc.broadcastPacket(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
		} else if (_calcType == NPC_PC) {
			if (_npc == null || _target == null)
				return;
			_npc.setHeading(_npc.targetDirection(_targetX, _targetY)); // 방향세트
			_npc.broadcastPacket(new S_SkillSound(_target.getId(), 6519));
			_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage));
		}
	}

	// ■■■■ 상대의 공격에 대해서 모탈바디가 유효한가를 판별 ■■■■
	public boolean isShortDistance1() {
		boolean isShortDistance1 = true;
		/*
		 * if (_calcType == PC_PC) { if (_weaponType == 20 || _weaponType == 62)
		 * { // 활이나 간트렛트 isShortDistance1 = false; }
		 */
		if (_calcType == NPC_PC) {
			boolean isLongRange = (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) <= 0);
			int bowActId = _npc.getNpcTemplate().getBowActId();
			// 거리가 2이상, 공격자의 활의 액션 ID가 있는 경우는 원공격
			if (isLongRange && bowActId > 0) {
				isShortDistance1 = false;
			}
		}
		return isShortDistance1;
	}

	// ■■■■ 상대의 공격에 대해서 카운터 배리어가 유효한가를 판별 ■■■■
	public boolean isShortDistance() {
		boolean isShortDistance = true;
		if (_calcType == PC_PC) {
			if (_weaponType == 20 || _weaponType == 62 || _weaponType2 == 17 || _weaponType2 == 19 || _pc.hasSkillEffect(L1SkillId.ARMOR_BRAKE)) {
				isShortDistance = false;
			}
		} else if (_calcType == NPC_PC) {
			if (_npc == null)
				return false;
			boolean isLongRange = (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) > 1);
			int bowActId = _npc.getNpcTemplate().getBowActId();
			// 거리가 2이상, 공격자의 활의 액션 ID가 있는 경우는 원공격
			if (isLongRange && bowActId > 0) {
				isShortDistance = false;
			}
		}
		return isShortDistance;
	}

	// ■■■■ 카운터 배리어의 대미지를 반영 ■■■■
	public void commitCounterBarrier() {
		int damage = calcCounterBarrierDamage();
		if (damage == 0) {
			return;
		}
		if (_calcType == PC_PC) {
			_pc.receiveCounterBarrierDamage(_targetPc, damage);
		} else if (_calcType == NPC_PC) {
			_npc.receiveCounterBarrierDamage(_targetPc, damage);
		} else if (_calcType == PC_NPC) {
			_pc.receiveCounterBarrierDamage(_targetPc, damage);
		}
	}

	public void commitBossCounterBarrier(L1Character attacker, L1PcInstance pc) {
		int damage = 0;
		L1ItemInstance weapon = null;
		weapon = pc.getWeapon();
		if (weapon == null)
			damage = 10;
		else
			damage = (int) Math.round((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * Config.Counter);
		pc.receiveCounterBarrierDamage(pc, damage);
	}

	// ■■■■ 모탈바디의 대미지를 반영 ■■■■
	public void commitMortalBody() {
		// int damage = 40;
		// if (damage == 0) {
		// return;
		// }
		int ac = Math.max(0, 10 - _targetPc.getAC().getAc());
		int damage = ac / 2;

		if (damage == 0) {
			return;
		}
		if (damage <= 40) {
			damage = 40;
		}
		if (_calcType == PC_PC) {
			_pc.receiveDamage(_targetPc, damage);
		} else if (_calcType == NPC_PC) {
			_npc.receiveDamage(_targetPc, damage);
		}
	}

	// ●●●● 카운터 배리어의 대미지를 산출 ●●●●
	private int calcCounterBarrierDamage() {
		double damage = 0;
		L1ItemInstance weapon = null;
		weapon = _targetPc.getWeapon();
		if (weapon != null) {
			if (weapon.getItem().getType() == 3) {
				damage = Math.round((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * Config.Counter);
			}
		}
		return (int) damage;
	}

	// ●●●● 전사 타이탄 대미지를 산출 ●●●●
	private int 타이탄대미지() {
		double damage = 0;
		L1ItemInstance weapon = null;
		weapon = _targetPc.getWeapon();
		if (weapon != null) {
			damage = Math.round((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * Config.RockDMG);
		}
		return (int) damage;
	}

	/*
	 * 무기를 손상시킨다. 대NPC의 경우, 손상 확률은10%로 한다. 축복 무기는3%로 한다.
	 */
	private void damageNpcWeaponDurability() {
		int chance = 3; // 일반 무기
		int bchance = 1; // 축복 받은 무기 손상확률

		/** 로봇시스템 **/
		if (_pc.getAI() != null) {
			return;
		}
		/** 로봇시스템 **/

		/*
		 * 손상하지 않는 NPC, 맨손, 손상하지 않는 무기 사용, SOF중의 경우 아무것도 하지 않는다.
		 */
		if (_calcType != PC_NPC || _targetNpc.getNpcTemplate().is_hard() == false || _weaponType == 0 || weapon.getItem().get_canbedmg() == 0
				|| _pc.hasSkillEffect(SOUL_OF_FLAME)) {
			return;
		}
		// 통상의 무기·저주해진 무기
		if ((_weaponBless == 1 || _weaponBless == 2) && ((_random.nextInt(100) + 1) < chance)) {
			// \f1당신의%0가 손상했습니다.
			_pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
			_pc.getInventory().receiveDamage(weapon);
		}
		// 축복된 무기
		if (_weaponBless == 0 && ((_random.nextInt(100) + 1) < bchance)) {
			// \f1당신의%0가 손상했습니다.
			_pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
			_pc.getInventory().receiveDamage(weapon);
		}
	}

	/** 스탯 + 무기에 따른 공성 **/
	private int PchitAdd() {
		int value = 0;
		if (_weaponType != 20 && _weaponType != 62) {
			if (_pc.getAbility().getTotalStr() > 59) {
				value += (strHit[58]);
			} else {
				value += (strHit[_pc.getAbility().getTotalStr() - 1]);
			}
			value += _weaponAddHit + _pc.getHitup() + _pc.getHitRate() + (_weaponEnchant / 2);
		} else {
			if (_pc.getAbility().getTotalDex() > 60) {
				value += (dexHit[59]);
			} else {
				value += (dexHit[_pc.getAbility().getTotalDex() - 1]);
			}
			value += _weaponAddHit + _pc.getBowHitup() + _pc.getBowHitRate() + (_weaponEnchant / 2);
		}
		return value;
	}

	/** 타겟PC 회피 스킬에 대한 연산 **/
	private int toPcSkillHit() {
	
//		 int value = _target.getDg();
//		 if (_targetPc.hasSkillEffect(UNCANNY_DODGE)) {
//		 if (_targetPc.getAC().getAc() >= -79) {
//		 value += 0;
//		 } else if (_targetPc.getAC().getAc() >= -89) {
//		 value += Config.UNCANNYDODGE1;
//		 } else if (_targetPc.getAC().getAc() >= -99) {
//			 value += Config.UNCANNYDODGE2;
//		 } else if (_targetPc.getAC().getAc() >= -110) {
//			 value += Config.UNCANNYDODGE3;
//		 } else if (_targetPc.getAC().getAc() <= -110) {
//			 value += Config.UNCANNYDODGE4;
		
		int value = _target.getDg();
		if (_targetPc.hasSkillEffect(UNCANNY_DODGE)) {
			if (_targetPc.getAC().getAc() >= -79) {
				value += 0;
			} else if (_targetPc.getAC().getAc() >= -89) {
				value += Config.UNCANNYDODGE1;
			} else if (_targetPc.getAC().getAc() >= -99) {
				value += Config.UNCANNYDODGE2;
			} else if (_targetPc.getAC().getAc() >= -110) {
				value += Config.UNCANNYDODGE3;
			} else if (_targetPc.getAC().getAc() >= -120) {
				value += Config.UNCANNYDODGE4;
			}else if (_targetPc.getAC().getAc() >= -130) {
				value += Config.UNCANNYDODGE5;
			}else if (_targetPc.getAC().getAc() >= -140) {
				value += Config.UNCANNYDODGE6;
			}else if (_targetPc.getAC().getAc() >= -150) {
				value += Config.UNCANNYDODGE7;
			}else if (_targetPc.getAC().getAc() >= -160) {
				value += Config.UNCANNYDODGE8;
			}else if (_targetPc.getAC().getAc() >= -170) {
				value += Config.UNCANNYDODGE9;
			}else if (_targetPc.getAC().getAc() >= -180) {
				value += Config.UNCANNYDODGE10;
			}else if (_targetPc.getAC().getAc() >= -190) {
				value += Config.UNCANNYDODGE11;
			}else if (_targetPc.getAC().getAc() >= -200) {
				value += Config.UNCANNYDODGE12;
			}else if (_targetPc.getAC().getAc() >= -210) {
				value += Config.UNCANNYDODGE13;
			}else if (_targetPc.getAC().getAc() >= -220) {
				value += Config.UNCANNYDODGE14;
			}else if (_targetPc.getAC().getAc() >= -230) {
				value += Config.UNCANNYDODGE15;
			}else if (_targetPc.getAC().getAc() >= -240) {
				value += Config.UNCANNYDODGE16;
			}else if (_targetPc.getAC().getAc() >= -250) {
				value += Config.UNCANNYDODGE17;
			}else if (_targetPc.getAC().getAc() >= -260) {
				value += Config.UNCANNYDODGE18;
			}else if (_targetPc.getAC().getAc() <= -261) {
				value += Config.UNCANNYDODGE19;
		 } 
		
		 }
		 if (_targetPc.hasSkillEffect(MIRROR_IMAGE)) {
			
			 value += Config.MIRRORIMAGE;
		 }
	
		 
			if (_targetPc.hasSkillEffect(L1SkillId.ANTA_MAAN) || _targetPc.hasSkillEffect(L1SkillId.BIRTH_MAAN) || _targetPc.hasSkillEffect(L1SkillId.SHAPE_MAAN)) {
				int chance = _random.nextInt(100);
				if (chance < 15) {
					value -= 5;
				}
			}
			return value;
	}

	/** Hit 최종 연산 **/
	private boolean hitRateCal(double AD, double DD, double fumble, double critical) {
		if (AD <= fumble) {
			_hitRate = 0;
			return true;
		} else if (AD >= critical) {
			_hitRate = 100;
		} else {
			if (AD > DD) {
				_hitRate = 100;
			} else if (AD <= DD) {
				_hitRate = 0;
				return true;
			}
		}
		return false;
	}

	/** 타겟PC DD 연산 **/
	private int toPcDD(int dv) {
		if (_targetPc.getAC().getAc() >= 0) {
			return 10 - _targetPc.getAC().getAc();
		} else {
			return 10 + _random.nextInt(dv) + 1;
		}
	}

	/** 대상 Buff에 따른 대미지 연산 **/
	private double toPcBuffDmg(double dmg) {
		try {
			if (_targetPc.hasSkillEffect(COOKING_1_0_S) // 요리에 의한 대미지 경감
					|| _targetPc.hasSkillEffect(COOKING_1_1_S) || _targetPc.hasSkillEffect(COOKING_1_2_S) || _targetPc.hasSkillEffect(COOKING_1_3_S)
					|| _targetPc.hasSkillEffect(COOKING_1_4_S) || _targetPc.hasSkillEffect(COOKING_1_5_S) || _targetPc.hasSkillEffect(COOKING_1_6_S)
					|| _targetPc.hasSkillEffect(COOKING_1_8_S) || _targetPc.hasSkillEffect(COOKING_1_9_S) || _targetPc.hasSkillEffect(COOKING_1_10_S)
					|| _targetPc.hasSkillEffect(COOKING_1_11_S) || _targetPc.hasSkillEffect(COOKING_1_12_S) || _targetPc.hasSkillEffect(COOKING_1_13_S)
					|| _targetPc.hasSkillEffect(COOKING_1_14_S) || _targetPc.hasSkillEffect(COOKING_1_15_S) || _targetPc.hasSkillEffect(COOKING_1_16_S)
					|| _targetPc.hasSkillEffect(COOKING_1_17_S) || _targetPc.hasSkillEffect(COOKING_1_18_S) || _targetPc.hasSkillEffect(COOKING_1_19_S)
					|| _targetPc.hasSkillEffect(COOKING_1_20_S) || _targetPc.hasSkillEffect(COOKING_1_21_S) || _targetPc.hasSkillEffect(COOKING_1_22_S)) {
				dmg -= 5;
			}
			if (_targetPc.hasSkillEffect(COOK_STR) || _targetPc.hasSkillEffect(COOK_STR_1) || _targetPc.hasSkillEffect(COOK_DEX) || _targetPc.hasSkillEffect(COOK_STR_12)
					|| _targetPc.hasSkillEffect(COOK_INT) || _targetPc.hasSkillEffect(COOK_INT_1) 
					|| _targetPc.hasSkillEffect(COOK_GROW) || _targetPc.hasSkillEffect(COOK_DEX_1) 
				) { // 리뉴얼
																									// 요리
				dmg -= 2;
			}
			if (_targetPc.hasSkillEffect(COOK_GROW_1)) { // 리뉴얼
																									// 요리
				dmg -= 4;
			}
			if (_targetPc.hasSkillEffect(COOKING_1_7_S) || _targetPc.hasSkillEffect(COOKING_1_15_S) || _targetPc.hasSkillEffect(COOKING_1_23_S)) { // 디저트에
																																					// 경감
				dmg -= 5;
			}

			// 전사스킬 : 아머가드 - 캐릭의 AC/10의 데미지감소 효과를 얻는다.
			if (_targetPc.getPassive(MJPassiveID.ARMOR_GUARD.toInt()) != null) {
				dmg -= _targetPc.getAC().getAc() / 10;
			}

			if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
				int targetPcLvl = _targetPc.getLevel();
				if (targetPcLvl < 50) {
					targetPcLvl = 50;
				}
				dmg -= (targetPcLvl - 50) / 5 + 1;
			}
			
			if (_targetPc.hasSkillEffect(L1SkillId.MAJESTY)) {
				int targetPcLvl = _targetPc.getLevel();
				if (targetPcLvl < 80) {
					targetPcLvl = 80;
				}
				dmg -= (targetPcLvl - 80) / 2 + 1;
			}

			if (_targetPc.hasSkillEffect(DRAGON_SKIN)) {
				if (_targetPc.getLevel() >= 80) {
					dmg -= 5 + ((_targetPc.getLevel() - 80) / 2);
				} else {
					dmg -= 5;
				}
			}

			
			if (_targetPc.hasSkillEffect(PATIENCE)) {
				if (_targetPc.getLevel() >= 80) {
					dmg -= 2 + ((_targetPc.getLevel() - 80) / 4);
				} else {
					dmg -= 2;
				}
			}
			
	
			if (_targetPc.hasSkillEffect(IllUSION_AVATAR)) {
				dmg += (dmg / 5);
			}
			if (_targetPc.hasSkillEffect(FEATHER_BUFF_A)) {
				dmg -= 3;
			}
			if (_targetPc.hasSkillEffect(FEATHER_BUFF_B)) {
				dmg -= 2;
			}
			if (_targetPc.hasSkillEffect(EARTH_GUARDIAN)) {
				dmg -= 2;
			}
			if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
				dmg = 0;
			}
			if (_targetPc.hasSkillEffect(ICE_LANCE)) {
				dmg = 0;
			}
			if (_targetPc.hasSkillEffect(EARTH_BIND)) {
				dmg = 0;
			}
			if (_targetPc.hasSkillEffect(PHANTASM)) {
				_targetPc.removeSkillEffect(PHANTASM);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dmg;
	}

	/** 특수 인챈트 시스템 **/
	public static double WeaponLevelAttack(L1PcInstance pc, L1Character cha, int effect, int enchant, int weaponlevel) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Stat = 0;
		int chance = _random.nextInt(100) + 1;
		int enchatndmg = 100;

		if (pc.isWizard() || pc.isBlackwizard()) {
			Stat = pc.getAbility().getTotalInt();
		}
		if (pc.isElf()) {
			Stat = pc.getAbility().getTotalDex();
		} else {
			Stat = pc.getAbility().getTotalStr();
		}

		if (weaponlevel == 1) {
			enchatndmg = Config.Weapon_Enchant_Dmg_lvl1 + Stat;
		} else if (weaponlevel == 2) {
			enchatndmg = Config.Weapon_Enchant_Dmg_lvl2 + Stat;
		} else if (weaponlevel == 3) {
			enchatndmg = Config.Weapon_Enchant_Dmg_lvl3 + Stat;
		} else if (weaponlevel == 4) {
			enchatndmg = Config.Weapon_Enchant_Dmg_lvl4 + Stat;
		}

		if (Config.Weapon_Magic_Per + enchant + weaponlevel >= chance) {// 찬스
			dmg = _random.nextInt(enchatndmg) + 1;
			if (2 >= chance) {// 이펙트 랜타 찬스
				dmg += dmg * 0.1;// 대미지
			} else if (dmg <= 0) {
				dmg = 0;
			}
			broadcast(cha, new S_SkillSound(cha.getId(), effect));
			/*
			 * S_EffectLocation packet = new S_EffectLocation(locx, locy,
			 * effect); pc.sendPackets(packet, false);
			 * pc.broadcastPacket(packet);
			 */
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double calcDamageReduction(L1Character cha, double dmg, int attr) {
		if (isFreeze(cha)) {
			return 0;
		}

		int ran1 = 0; // 랜덤 수치 적용
		int mrset = 0; // 엠알에서 랜덤 수치를 뺀값
		int mrs = cha.getResistance().getEffectedMrBySkill();
		ran1 = _random.nextInt(5) + 1;
		mrset = mrs - ran1;
		double calMr = 0.00D;
		calMr = (220 - mrset) / 250.00D;
		dmg *= calMr;

		if (dmg < 0) {
			dmg = 0;
		}

		int resist = 0;
		if (attr == L1Skills.ATTR_EARTH) {
			resist = cha.getResistance().getEarth();
		} else if (attr == L1Skills.ATTR_FIRE) {
			resist = cha.getResistance().getFire();
		} else if (attr == L1Skills.ATTR_WATER) {
			resist = cha.getResistance().getWater();
		} else if (attr == L1Skills.ATTR_WIND) {
			resist = cha.getResistance().getWind();
		}
		int resistFloor = (int) (0.32 * Math.abs(resist));
		if (resist >= 0) {
			resistFloor *= 1;
		} else {
			resistFloor *= -1;
		}
		double attrDeffence = resistFloor / 32.0;
		dmg = (1.0 - attrDeffence) * dmg;

		return dmg;
	}

	private static boolean isFreeze(L1Character cha) {

		if (cha.hasSkillEffect(STATUS_FREEZE)) {
			return true;
		}
		if (cha.hasSkillEffect(ABSOLUTE_BARRIER)) {
			return true;
		}
		if (cha.hasSkillEffect(ICE_LANCE)) {
			return true;
		}
		if (cha.hasSkillEffect(EARTH_BIND)) {
			return true;
		}

		if (cha.hasSkillEffect(COUNTER_MAGIC)) {
			cha.removeSkillEffect(COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(COUNTER_MAGIC).getCastGfx();
			cha.broadcastPacket(new S_SkillSound(cha.getId(), castgfx));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
			}
			return true;
		}
		return false;
	}

	protected static void broadcast(L1Character c, ServerBasePacket pck) {
		c.sendPackets(pck, false);
		c.broadcastPacket(pck);
	}
	/** 특수 인챈트 시스템 **/

	/*
	 * public void ArmorDestory() { for (L1ItemInstance armorItem :
	 * _targetPc.getInventory().getItems()) { if (armorItem.getItem().getType2()
	 * == 2 && armorItem.getItem().getType() == 2) { int armorId =
	 * armorItem.getItemId(); L1ItemInstance item =
	 * _targetPc.getInventory().findEquippedItemId(armorId); if (item != null) {
	 * int chance = _random.nextInt(100) + 1; if (item.get_durability() ==
	 * (armorItem.getItem().get_ac() * -1)) { break; } else { if (chance <=
	 * Config.Destroy) { item.set_durability(item.get_durability() + 1);
	 * _targetPc.getInventory().updateItem(item, L1PcInventory.COL_DURABILITY);
	 * _targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 14549));
	 * _targetPc.getAC().addAc(1); _targetPc.sendPackets(new
	 * S_OwnCharAttrDef(_targetPc)); _targetPc.sendPackets(new
	 * S_ServerMessage(268, armorItem.getLogName()));
	 * Broadcaster.broadcastPacket(_targetPc, new
	 * S_SkillSound(_targetPc.getId(), 14549)); } } } } } }
	 */
	
	public void set_hit_rate(int rate){
		_hitRate = rate;
	}
	
	public void set_hit(boolean hit){
		_isHit = hit;
	}
	public void set_is_critical(boolean is_critical){
		_isCritical = is_critical;
	}
}
