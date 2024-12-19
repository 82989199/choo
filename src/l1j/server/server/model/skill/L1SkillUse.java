package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SkillCheck;
import l1j.server.server.datatables.MonsterParalyzeDelay;
import l1j.server.server.datatables.MonsterParalyzeDelay.MonsterParalyze;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1CurseParalysis;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1DwarfInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1SupportInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.item.function.Telbookitem;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_IvenBuffIcon;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RangeSkill;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;

public class L1SkillUse {

	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_LOGIN = 1;
	public static final int TYPE_SPELLSC = 2;
	public static final int TYPE_NPCBUFF = 3;
	public static final int TYPE_GMBUFF = 4;

	private L1Skills _skill;
	private int _skillId;
	private int _getBuffDuration;
	private int _shockStunDuration;
	private int _getBuffIconDuration;
	private int _targetID;
	private int _mpConsume = 0;
	private int _hpConsume = 0;
	private int _targetX = 0;
	private int _targetY = 0;
	private int _PowerRipDuration;
	private int _earthBindDuration;
	private int _skillTime = 0;
	private int _type = 0;
	private boolean _isPK = false;
	// private int _bookmarkId = 0;
	private int _itemobjid = 0;
	private boolean _checkedUseSkill = false;
	private int _leverage = 10;
	private boolean _isFreeze = false;
	private boolean _isCounterMagic = true;

	private L1Character _user = null;
	private L1Character _target = null;

	private L1PcInstance _player = null;
	private L1NpcInstance _npc = null;
	private L1NpcInstance _targetNpc = null;

	private int _calcType;
	private static final int PC_PC = 1;
	private static final int PC_NPC = 2;
	private static final int NPC_PC = 3;
	private static final int NPC_NPC = 4;
	private Random random = new Random(System.nanoTime());
	private ArrayList<TargetStatus> _targetList;

	private short _bookmark_mapid = 0;
	private int _bookmark_x = 0;
	private int _bookmark_y = 0;

	private boolean _isGlanceCheckFail = false;
	private boolean _isCriticalDamage = false;
	// 시전자가 시전한 트루타겟을 임시로 담을 공간.
	public static Map<Integer, L1Object> _truetarget_list = new HashMap<Integer, L1Object>();

	private static Logger _log = Logger.getLogger(L1SkillUse.class.getName());

	private static final int[] polyArray = { 945, 979, 1037, 1039, 3860, 3863, 3864, 3865, 3904, 3906, 95, 146, 2374, 2376, 2377, 2378, 3866, 3867, 3868, 3869,
			3870, 3871, 3872, 3873, 3874, 3875, 3876, 3882, 3883, 3884, 3885, 11358, 11396, 11397, 12225, 12226, 11399, 11398, 12227, 15638, 15635, 15636 };

	private static final int[] CAST_WITH_INVIS = { 1, 2, 3, 5, 8, 9, 12, 13, 14, 19, 21, 26, 31, 32, 35, 37, 42, 43, 44, 48, 49, 52, 54, 55, 57, 60, 61, 63, 67,
			68, 69, 72, 73, 75, 78, 79, REDUCTION_ARMOR, BOUNCE_ATTACK, SOLID_CARRIAGE, COUNTER_BARRIER, 97, 98, 99, 100, 101, 102, 104, 105, 106, 107, 109,
			110, 111, 113, 114, 115, 116, 117, 118, 129, 130, 131, 133, 134, 137, 138, 146, 147, 148, 149, 150, 151, 155, 156, 158, 159, 163, 164, 165, 166,
			168, 169, 170, 171, 181, SOUL_OF_FLAME, ADDITIONAL_FIRE, ANTA_BUFF, FAFU_BUFF, RIND_BUFF, INFERNO, MAJESTY };

	/** 카운터 매직으로 방어할수 없는 스킬 **/
	private static final int[] EXCEPT_COUNTER_MAGIC = { 1, 2, 3, 5, 8, 9, 12, 13, 14, 19, 21, 26, 31, 32, 35, 37, 42, 43, /*44,*/ 48, 49, 52, 54, 55, 57, 60, 61,
			63, 67, 68, 69, 72, 73, 75, 78, 79, SHOCK_STUN, REDUCTION_ARMOR, BOUNCE_ATTACK, SOLID_CARRIAGE, COUNTER_BARRIER, 97, 98, 99, 100, 101, 102, 104,
			105, 106, 107, 109, 110, 111, 113, 114, 115, 116, 117, 118, 129, 130, 131, 132, 134, 137, 138, 146, 147, 148, 149, 150, 151, 155, 156, 158, 159,
			161, 163, 164, 165, 166, 168, 169, 170, 171, 181, SOUL_OF_FLAME, ADDITIONAL_FIRE, FOU_SLAYER, SCALES_EARTH_DRAGON, SCALES_FIRE_DRAGON, DRAGON_SKIN,
			SCALES_WATER_DRAGON, MIRROR_IMAGE, IllUSION_OGRE, SCALES_RINDVIOR_DRAGON, PATIENCE, IllUSION_DIAMONDGOLEM, IllUSION_LICH, IllUSION_AVATAR, INSIGHT,
			SHAPE_CHANGE, 10026, 10027, 10028, 10029, 30060, 30000, 30078, 30079, 30011, 30081, 30082, 30083, 30080, 30084, 30010, 30002, 30086,
			OMAN_CANCELLATION, ANTA_MESSAGE_2, ANTA_MESSAGE_3, ANTA_MESSAGE_4, ANTA_MESSAGE_5, ANTA_MESSAGE_6, ANTA_MESSAGE_7, ANTA_MESSAGE_8, ANTA_MESSAGE_10,
			22034, OMAN_STUN, Maeno_STUN, PAP_PREDICATE1, PAP_PREDICATE3, PAP_PREDICATE5, PAP_PREDICATE6, PAP_PREDICATE7, PAP_PREDICATE8, PAP_PREDICATE9,
			PAP_PREDICATE11, PAP_PREDICATE12, DESPERADO, POWERRIP, INFERNO, MAJESTY };

	public L1SkillUse() {
	}

	private static class TargetStatus {
		private L1Character _target = null;
		// private boolean _isAction = false;
		// private boolean _isSendStatus = false;
		private boolean _isCalc = true;

		public TargetStatus(L1Character _cha) {
			_target = _cha;
		}

		public TargetStatus(L1Character _cha, boolean _flg) {
			_isCalc = _flg;
		}

		public L1Character getTarget() {
			return _target;
		}

		public boolean isCalc() {
			return _isCalc;
		}
	}

	public void setLeverage(int i) {
		_leverage = i;
	}

	public int getLeverage() {
		return _leverage;
	}

	private boolean isCheckedUseSkill() {
		return _checkedUseSkill;
	}

	private void setCheckedUseSkill(boolean flg) {
		_checkedUseSkill = flg;
	}

	public boolean checkUseSkill(L1PcInstance player, int skillid, int target_id, int x, int y, String message, int time, int type, L1Character attacker) {
		// ** 아래 버그 체크문 실행하면서 에러 안나게 **// By 도우너
		if (player instanceof L1PcInstance) {
			L1Object l1object = L1World.getInstance().findObject(target_id);
			if (l1object instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance) l1object;
				if (item.getX() != 0 && item.getY() != 0) { // 지면상의 아이템은 아니고,
					return false;
				}
			}
			// ** 아래 버그 체크문 실행하면서 에러 안나게 **// By 도우너
			// ** 노딜 방지 추가 **// by 도우너
			long nowtime = System.currentTimeMillis();
			if (skillid == 17 && player.getSkilldelay2() >= nowtime || skillid == 25 && player.getSkilldelay2() >= nowtime) {
				return false;
			} else if (player.getSkilldelay2() >= nowtime) {
				return false;
			}

			/** 가더 착용시도 가능하게 **/
			
		/*	if (skillid == BLOW_ATTACK) {
				if (player.getInventory().getTypeEquipped(2, 7) <= 0) {
					return false;
				}
			}*/
			
			

			// ** 노딜 방지 추가 **// by 도우너
			// ** 2차 스킬 버그 방지 소스 추가 **// by 도우너
			int[] CheckSkillID = { 45, 46, 47, 48, 49, 50, 51, 52, 53, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
					77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108,
					109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136,
					137, 138, 139, 140, 141, 142, 143, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165,
					166, 167, 169, 170, 171, 172, 173, 174, 175, 176, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 201, 202, 203,
					204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220 };
					// 3, 12, 13, 21, 26, 42, 168, 43, 54, 1, 8 치투 신투 힘투 헤이스트는
					// 빠짐

			// 스킬검사에서 빠지게 할 스킬은 위 번호에서 빼삼!

			int check = 0;
			for (int chskill : CheckSkillID) {
				if (chskill == skillid) {
					check = chskill;	
					break;
				}
			}

			if (player.getBuffnoch() == 0) {
				if (check != 0) {
					if (!SkillCheck.getInstance().CheckSkill(player, check) && player.getAI() == null) {
						return false;
					}
				}
			}
			// ** 2차 스킬 버그 방지 소스 추가 **// by 도우너

		} // ** 위 버그 체크문 실행하면서 에러 안나게 **// By 도우너

		// 존재버그 관련 추가
		if (player instanceof L1PcInstance) {
			L1PcInstance jonje = L1World.getInstance().getPlayer(player.getName());
			if (jonje == null && player.getAccessLevel() != 200) {
				player.sendPackets(new S_SystemMessage("존재버그 강제종료! 재접속하세요"));
				player.sendPackets(new S_Disconnect());
				return false;
			}

		}

		setCheckedUseSkill(true);
		_targetList = new ArrayList<TargetStatus>();

		_skill = SkillsTable.getInstance().getTemplate(skillid);
		_skillId = skillid;
		_targetX = x;
		_targetY = y;
		_skillTime = time;
		_type = type;
		boolean checkedResult = true;
		if (attacker == null) {
			// pc
			_player = player;
			_user = _player;
		} else {
			// npc
			_npc = (L1NpcInstance) attacker;
			_user = _npc;
		}

		if (_skill == null || _skill.getTarget() == null) {
	//		System.out.println(String.format("NULL 원인 스킬아이디 : ", skillid + "/" + _skill));
			return false;
		}
		if (_skill.getTarget().equals("none")) {
			_targetID = _user.getId();
			_targetX = _user.getX();
			_targetY = _user.getY();
		} else {
			_targetID = target_id;
		}

		if (type == TYPE_NORMAL) {
			checkedResult = isNormalSkillUsable();
		} else if (type == TYPE_SPELLSC) {
			checkedResult = isSpellScrollUsable();
		} else if (type == TYPE_NPCBUFF) {
			checkedResult = true;
		}
		if (!checkedResult) {
			return false;
		}
		if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM) {
			return true;
		}

		L1Object l1object = L1World.getInstance().findObject(_targetID);
		if (l1object instanceof L1ItemInstance) {
			_log.fine("skill target item name: " + ((L1ItemInstance) l1object).getViewName());
			return false;
		}
		if (l1object instanceof L1ClanJoinInstance)
			return false;

		if (_user instanceof L1PcInstance) {
			if (l1object instanceof L1PcInstance) {
				_calcType = PC_PC;
			} else {
				_calcType = PC_NPC;
				_targetNpc = (L1NpcInstance) l1object;
			}
		} else if (_user instanceof L1NpcInstance) {
			if (l1object instanceof L1PcInstance) {
				_calcType = NPC_PC;
			} else if (_skill.getTarget().equals("none")) {
				_calcType = NPC_PC;
			} else {
				_calcType = NPC_NPC;
				_targetNpc = (L1NpcInstance) l1object;
			}
		}

		if(_targetNpc != null && _targetNpc instanceof MJCompanionInstance){
			if(_skill.getTarget().equals("buff"))
				return false;
		}
		if(_targetNpc != null && _targetNpc instanceof MJDogFightInstance)
			return false;

		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TRUE_TARGET) {
			_bookmark_mapid = (short) target_id;
			_bookmark_x = x;
			_bookmark_y = y;
		}
		if (_skillId == SUMMON_MONSTER) {
			_bookmark_x = x;
		}

		if (_skillId == BRING_STONE || _skillId == BLESSED_ARMOR || _skillId == ENCHANT_WEAPON || _skillId == SHADOW_FANG) {
			_itemobjid = target_id;
		}
		_target = (L1Character) l1object;

		if (!(_target instanceof L1MonsterInstance) && _skill.getTarget().equals("attack") && _user.getId() != target_id) {
			_isPK = true;
		}
		if (!(l1object instanceof L1Character)) {
			checkedResult = false;
		}

		makeTargetList();
		if (_targetList.size() == 0 && (_user instanceof L1NpcInstance)) {
			checkedResult = false;
		}

		return checkedResult;
	}

	/**
	 * 통상의 스킬 사용시에 사용자 상태로부터 스킬이 사용 가능한가 판단한다
	 * 
	 * @return false 스킬이 사용 불가능한 상태인 경우
	 */
	private boolean isNormalSkillUsable() {
		if (_user instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _user;

			if (pc.isParalyzed()) {
				return false;
			}
			if ((pc.isInvisble() || pc.isInvisDelay()) && !isInvisUsableSkill() && _skillId != 233) {
				return false;
			}
			if (!pc.is_top_ranker() && pc.getInventory().getWeight100() > 82) { // 중량
				// 오버이면
				// 스킬을사용할
				// 수
				// 없다
				pc.set_자동버프사용(false);
				pc.sendPackets(new S_ServerMessage(316));
				return false;
			}

			int polyId = pc.getCurrentSpriteId();
			L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
			if (poly != null && !poly.canUseSkill()) {
				pc.sendPackets(new S_ServerMessage(285));
				return false;
			}

			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (castle_id != 0) {
				if (_skillId == 50 || _skillId == 69 || _skillId == 157 || _skillId == 66 || _skillId == 78 || _skillId == 116) {
					pc.sendPackets(new S_SystemMessage("공성장에서 사용 할 수 없습니다."));
					return false;
				}
			}
			// TODO 세이프존에서 해당스킬 사용불가능
			if (pc.getMap().isSafetyZone(pc.getLocation())) {
				if (_skillId == 69 || _skillId == 73 || _skillId == 66 || _skillId == 11
						|| _skillId == 208 /* || _skillId == 219 */ || _skillId == 67 || _skillId == 123 ) {
					pc.sendPackets(new S_SystemMessage("마을에서 사용할 수 없습니다."));
					return false;
				}
			}

			if (!isAttrAgrees()) {
				return false;
			}

			if (_skillId == ELEMENTAL_PROTECTION && pc.getElfAttr() == 0) {
				pc.sendPackets(new S_ServerMessage(280));
				return false;
			}

			if (pc.isSkillDelay()) {
				return false;
			}

			if (_skillId == TRUE_TARGET) {
			} else if ((pc.hasSkillEffect(SILENCE) || pc.hasSkillEffect(AREA_OF_SILENCE) || pc.hasSkillEffect(STATUS_POISON_SILENCE))
					&& (_skillId < SHOCK_STUN || _skillId > COUNTER_BARRIER)) { // 사일런스상태에서도
																				// 트루타겟
																				// 시전가능
				pc.sendPackets(new S_ServerMessage(285));
				return false;
			}
			
			if(_skillId == INFERNO) {
				if(pc.getWeapon().getItem().getType() != 1) { // 한손검
					pc.sendPackets(new S_ServerMessage(1413));
					return false;
				}
			}

			if (_skillId == COUNTER_BARRIER || _skillId == SHOCK_STUN) {
				if (pc.getWeapon().getItem().getType() != 3) { // 양손검
					pc.sendPackets(new S_ServerMessage(1008));
					return false;
				}
			}
			if (_skillId == DANCING_BLADES || _skillId == SAND_STORM) {
				if (pc.getWeapon() == null) {
					pc.sendPackets(new S_SystemMessage("무기(검)을 착용시 사용가능합니다."));
					return false;
				}
				if (pc.getWeapon().getItem().getType() != 1 && pc.getWeapon().getItem().getType() != 2 ) {
					pc.sendPackets(new S_SystemMessage("무기(검)을 착용시 사용가능합니다."));
					return false;
				}
			}
			
			if (_skillId == BLOW_ATTACK) {
				if (pc.getWeapon() == null) {
					pc.sendPackets(new S_SystemMessage("무기(검)을 착용시 사용가능합니다."));
					return false;
				}
				if (pc.getWeapon().getItem().getType() != 1 && pc.getWeapon().getItem().getType() != 2 && pc.getWeapon().getItem().getType() != 3) {
					pc.sendPackets(new S_SystemMessage("무기(검)을 착용시 사용가능합니다."));
					return false;
				}
			}

			if (pc.hasSkillEffect(CONFUSION)) {
				pc.sendPackets(new S_ServerMessage(285));
				return false;
			}

			if (isItemConsume() == false && !_player.isGm()) {
				if (pc.is_자동버프사용()) {
					pc.set_자동버프사용(false);
				}
				_player.sendPackets(new S_ServerMessage(299));
				return false;
			}
		} else if (_user instanceof L1NpcInstance) {

			if (_skillId == TRUE_TARGET) {
			} else if (_user.hasSkillEffect(CONFUSION)) {
				_user.removeSkillEffect(CONFUSION);
				return false;
			} else if (_user.hasSkillEffect(SILENCE)) {
				_user.removeSkillEffect(SILENCE);
				return false;
			}
		}

		if (!isHPMPConsume()) {
			return false;
		}
		return true;
	}

	private boolean isSpellScrollUsable() {
		L1PcInstance pc = (L1PcInstance) _user;

		if (pc.isParalyzed()) {
			return false;
		}

		if ((pc.isInvisble() || pc.isInvisDelay()) && !isInvisUsableSkill()) {
			return false;
		}

		return true;
	}

	private boolean isInvisUsableSkill() {
		for (int skillId : CAST_WITH_INVIS) {
			if (skillId == _skillId) {
				return true;
			}
		}
		return false;
	}

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, String message, int timeSecs, int type) {
		L1Character attacker = null;
		handleCommands(player, skillId, targetId, x, y, message, timeSecs, type, attacker);
	}

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, String message, int timeSecs, int type, L1Character attacker) {

		try {
			if (!isCheckedUseSkill()) {
				boolean isUseSkill = checkUseSkill(player, skillId, targetId, x, y, message, timeSecs, type, attacker);
				if (!isUseSkill) {
					failSkill();
					return;
				}
			}
			
			/** 2016.11.26 MJ 앱센터 LFC **/
			if (player != null && (player instanceof L1PcInstance) && _skill != null) {
				if (_skill.getType() != L1Skills.TYPE_CHANGE && _skill.getType() != L1Skills.TYPE_HEAL
						&& player.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY)
					return;
			}

			switch (type) {
			case TYPE_NORMAL:
				if (!_isGlanceCheckFail || _skill.getArea() > 0 || _skill.getTarget().equals("none")) {
					if (skillId == DANCING_BLADES || skillId == SOLID_CARRIAGE || skillId == SAND_STORM || skillId == HURRICANE
							|| skillId == L1SkillId.FOCUS_WAVE) {
						// 특정스킬 시간초과
						sendGrfx(true);
						runSkill();
						useConsume();
						sendFailMessageHandle();
						setDelay();
					} else {
						runSkill();
						useConsume();
						sendGrfx(true);
						sendFailMessageHandle();
						setDelay();
					}
				}
				break;
			case TYPE_LOGIN:
				runSkill();
				break;
			case TYPE_SPELLSC:
				runSkill();
				sendGrfx(true);
				setDelay();
				break;
			case TYPE_GMBUFF:
				runSkill();
				sendGrfx(false);
				break;
			case TYPE_NPCBUFF:
				runSkill();
				sendGrfx(true);
				break;
			default:
				break;
			}
			setCheckedUseSkill(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void failSkill() {
		setCheckedUseSkill(false);
		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TELEPORT_TO_MATHER) {
			_player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		}
	}

	private boolean isTarget(L1Character cha) throws Exception {
		boolean _flg = false;

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isGhost() || pc.isGmInvis()) {
				return false;
			}
		}
		if (_calcType == NPC_PC && (cha instanceof L1PcInstance || cha instanceof L1PetInstance || cha instanceof L1SummonInstance || cha instanceof MJCompanionInstance)) {
			_flg = true;
		}

		if (cha instanceof L1DoorInstance) {
			if (cha.getMaxHp() == 0 || cha.getMaxHp() == 1) {
				return false;
			}
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_PC && cha instanceof L1PcInstance
				&& _user instanceof L1SummonInstance) {
			L1SummonInstance summon = (L1SummonInstance) _user;
			if (cha.getId() == summon.getMaster().getId()) {
				return false;
			}
			if (cha.getZoneType() == 1) {
				return false;
			}
		}

		if(_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK && _calcType == NPC_PC){
			if(cha instanceof L1PcInstance){
				if(_user instanceof L1PetInstance){
					if(cha.getZoneType() == 1 || cha.getId() == ((L1PetInstance)_user).getMaster().getId())
						return false;
				}else if(_user instanceof MJCompanionInstance){
					if(cha.getZoneType() == 1 || cha.getId() == ((MJCompanionInstance)_user).get_master_id())
						return false;
				}
			}
		}

		if (cha instanceof L1DollInstance && _skillId != HASTE) {
			return false;
		}

		if (_calcType == PC_NPC && _target instanceof L1NpcInstance && !(_target instanceof MJCompanionInstance) && !(_target instanceof L1PetInstance) && !(_target instanceof L1SummonInstance)
				&& !(_target instanceof L1SupportInstance)
				&& (cha instanceof L1PetInstance || cha instanceof MJCompanionInstance || cha instanceof L1SummonInstance || cha instanceof L1SupportInstance || cha instanceof L1PcInstance)) {
			return false;
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_PC && 
				!(cha instanceof L1PetInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1SupportInstance) && !(cha instanceof L1PcInstance) && !(cha instanceof MJCompanionInstance)) {
			return false;
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_NPC && _user instanceof L1MonsterInstance
				&& cha instanceof L1MonsterInstance) {
			return false;
		}

		if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK
				&& (cha instanceof L1AuctionBoardInstance || cha instanceof L1BoardInstance || cha instanceof L1CrownInstance || cha instanceof L1DwarfInstance
						|| cha instanceof L1EffectInstance || cha instanceof L1FieldObjectInstance || cha instanceof L1FurnitureInstance
						|| cha instanceof L1HousekeeperInstance || cha instanceof L1MerchantInstance || cha instanceof L1TeleporterInstance)) {
			return false;
		}

		if (_skill.getType() == L1Skills.TYPE_ATTACK && cha.getId() == _user.getId()) {
			return false;
		}

		if (cha.getId() == _user.getId() && _skillId == HEAL_ALL) {
			return false;
		}

		if (((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC
				|| (_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN
				|| (_skill.getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY) && cha.getId() == _user.getId() && _skillId != HEAL_ALL) {
			return true;
		}

		if (_user instanceof L1PcInstance && (_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _isPK == false) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (_player.getId() == summon.getMaster().getId()) {
					return false;
				}
			} else if(cha instanceof MJCompanionInstance){
				if(_player.getId() == ((MJCompanionInstance)cha).get_master_id())
					return false;
				
			} else if (cha instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) cha;
				if (_player.getId() == pet.getMaster().getId()) {
					return false;
				}
			} else if (cha instanceof L1SupportInstance) {
				L1SupportInstance supprot = (L1SupportInstance) cha;
				if (_player.getId() == supprot.getMaster().getId()) {
					return false;
				}
			}
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && !(cha instanceof L1MonsterInstance) && _isPK == false
				&& cha instanceof L1PcInstance) {
			L1PcInstance enemy = (L1PcInstance) cha;
			if (_skillId == COUNTER_DETECTION && enemy.getZoneType() != 1 && (cha.hasSkillEffect(INVISIBILITY) || cha.hasSkillEffect(BLIND_HIDING))) {
				return true;
			}
			if (_player != null) {
				if (MJWar.isSameWar(_player.getClan(), enemy.getClan()))
					return L1CastleLocation.checkInAllWarArea(enemy.getX(), enemy.getY(), enemy.getMapId());
			}
			if (_npc == null && !(_user instanceof L1NpcInstance))
				return false;
		}

		if (_user.glanceCheck(cha.getX(), cha.getY()) == false && _skill.getIsThrough() == false) {
			if (!(_skill.getType() == L1Skills.TYPE_CHANGE || _skill.getType() == L1Skills.TYPE_RESTORE)) {
				_isGlanceCheckFail = true;
				return false;
			}
		}

		/** 아이스랜스 중이라면 디버프 안걸리게 **/
		if ((cha.hasSkillEffect(ICE_LANCE))
				&& (_skillId == SHOCK_STUN || _skillId == EMPIRE || _skillId == DECAY_POTION || _skillId == WEAPON_BREAK || _skillId == SLOW || _skillId == CURSE_PARALYZE
						|| _skillId == MANA_DRAIN || _skillId == DARKNESS || _skillId == FOG_OF_SLEEPING || _skillId == ARMOR_BRAKE || _skillId == EARTH_BIND
						|| _skillId == WIND_SHACKLE || _skillId == POLLUTE_WATER || _skillId == STRIKER_GALE || _skillId == GUARD_BREAK || _skillId == FEAR || _skillId == STRIKER_GALE
						|| _skillId == HORROR_OF_DEATH || _skillId == PANIC || _skillId == IllUSION_AVATAR || _skillId == DESPERADO || _skillId == POWERRIP)) {
			return false;
		}

		if (cha.hasSkillEffect(EARTH_BIND) && _skillId != CANCELLATION) {
			return false;
		}

		// 데페 중에 데페가 들어오면 기존 데페는 삭제.
		if (cha.hasSkillEffect(DESPERADO) && _skillId == DESPERADO)
			_target.removeSkillEffect(L1SkillId.DESPERADO);

		if (cha.hasSkillEffect(MOB_BASILL) && _skillId == MOB_BASILL) {
			return false; // 바실굳기중에 바실굳기
		}
		if (cha.hasSkillEffect(MOB_COCA) && _skillId == MOB_COCA) {
			return false; // 코카굳기중에 코카굳기
		}

		if (!(cha instanceof L1MonsterInstance) && (_skillId == TAMING_MONSTER || _skillId == CREATE_ZOMBIE)) {
			return false;
		}
		if (cha.isDead() && (_skillId != CREATE_ZOMBIE && _skillId != RESURRECTION && _skillId != GREATER_RESURRECTION && _skillId != CALL_OF_NATURE)) {
			return false;
		}

		if (cha.isDead() == false
				&& (_skillId == CREATE_ZOMBIE || _skillId == RESURRECTION || _skillId == GREATER_RESURRECTION || _skillId == CALL_OF_NATURE)) {
			return false;
		}

		if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance)
				&& (_skillId == CREATE_ZOMBIE || _skillId == RESURRECTION || _skillId == GREATER_RESURRECTION || _skillId == CALL_OF_NATURE)) {
			return false;
		}

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {// 앱솔중
				if (_skillId == CURSE_BLIND || _skillId == WEAPON_BREAK || _skillId == DARKNESS || _skillId == WEAKNESS || _skillId == DISEASE
						|| _skillId == FOG_OF_SLEEPING || _skillId == SLOW || _skillId == CANCELLATION || _skillId == SILENCE || _skillId == DECAY_POTION
						|| _skillId == MASS_TELEPORT || _skillId == DETECTION || _skillId == IZE_BREAK || _skillId == HORROR_OF_DEATH
						|| _skillId == COUNTER_DETECTION || _skillId == GUARD_BREAK || _skillId == ERASE_MAGIC || _skillId == FEAR
						|| _skillId == PHYSICAL_ENCHANT_DEX || _skillId == PHYSICAL_ENCHANT_STR || _skillId == BLESS_WEAPON || _skillId == IMMUNE_TO_HARM
						|| _skillId == REMOVE_CURSE || _skillId == CONFUSION || _skillId == Sand_worms || _skillId == Sand_worms1 || _skillId == Sand_worms2
						|| _skillId == Sand_worms3 || _skillId == MOB_SLOW_1 || _skillId == MOB_SLOW_18 || _skillId == MOB_WEAKNESS_1
						|| _skillId == MOB_DISEASE_1 || _skillId == MOB_BASILL || _skillId == MOB_SHOCKSTUN_30 || _skillId == MOB_RANGESTUN_19
						|| _skillId == MOB_RANGESTUN_18 || _skillId == MOB_DISEASE_30 || _skillId == MOB_WINDSHACKLE_1 || _skillId == MOB_COCA
						|| _skillId == MOB_CURSEPARALYZ_19 || _skillId == MOB_CURSEPARALYZ_18 || _skillId == MOB_CURSEPARALYZ1 || _skillId == Mob_RANGESTUN_30
						|| _skillId == ANTA_MESSAGE_1 || _skillId == ANTA_MESSAGE_2 || _skillId == ANTA_MESSAGE_3 || _skillId == ANTA_MESSAGE_4
						|| _skillId == ANTA_MESSAGE_5 || _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8
						|| _skillId == ANTA_MESSAGE_9 || _skillId == ANTA_MESSAGE_10 || _skillId == OMAN_STUN || _skillId == Maeno_STUN || _skillId == SHOCK_STUN
						|| _skillId == OMAN_CANCELLATION) {
					return true;
				} else {
					return false;
				}
			}
		}
		if (cha instanceof L1NpcInstance) {
			int hiddenStatus = ((L1NpcInstance) cha).getHiddenStatus();
			if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
				if (_skillId == DETECTION || _skillId == IZE_BREAK || _skillId == EYE_OF_DRAGON || _skillId == COUNTER_DETECTION) {
					return true;
				} else {
					return false;
				}
			} else if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) {
				return false;
			}
		}
		if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC && cha instanceof L1PcInstance) {
			_flg = true;
		} else if ((_skill.getTargetTo() & L1Skills.TARGET_TO_NPC) == L1Skills.TARGET_TO_NPC
				&& (cha instanceof L1MonsterInstance || cha instanceof L1NpcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance)) {
			_flg = true;
		} else if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PET) == L1Skills.TARGET_TO_PET && _user instanceof L1PcInstance) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.getMaster() != null) {
					if (_player.getId() == summon.getMaster().getId()) {
						if (_skillId != L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					} else {
						if (_skillId == L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					}
				}
			}

			if(cha instanceof MJCompanionInstance){
				MJCompanionInstance companion = (MJCompanionInstance)cha;
				if(companion.get_master() != null){
					if(_player.getId() == companion.get_master_id()){
						if(_skillId != L1SkillId.RETURN_TO_NATURE)
							_flg = true;
					}else{
						if(_skillId == L1SkillId.RETURN_TO_NATURE)
							_flg = true;
					}
				}
			}
			
			if (cha instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) cha;
				if (pet.getMaster() != null) {
					if (_player.getId() == pet.getMaster().getId()) {
						if (_skillId != L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					} else {
						if (_skillId == L1SkillId.RETURN_TO_NATURE) {
							_flg = true;
						}
					}
				}
			}
		}

		if (_calcType == PC_PC && cha instanceof L1PcInstance) {
			try {
				if ((_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN
						&& ((_player.getClanid() != 0 && _player.getClanid() == ((L1PcInstance) cha).getClanid()) || _player.isGm())) {
					return true;
				}
				if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY
						&& (_player.getParty().isMember((L1PcInstance) cha) || _player.isGm())) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return _flg;
	}

	private void EffectSpawn() { // 이펙트 스폰 타입별 나누자
		int Effect = 0;
		if (_skillId == DESERT_SKILL4 || _skillId == ZENITH_Poison) {
			Effect = 5137;
		}
		int xx = 0;
		int yy = 0;
		int xx1 = 0;
		int yy1 = 0;
		int xx2 = 0;
		int yy2 = 0;
		int xx3 = 0;
		int yy3 = 0;
		int xx4 = 0;
		int yy4 = 0;
		int randomxy = random.nextInt(4);
		int r = random.nextInt(2) + 1;
		int a1 = 3 + randomxy;
		int a2 = -3 - randomxy;
		int b1 = 2 + randomxy;
		int b2 = -2 - randomxy;
		int heading = _npc.getHeading(); // 몹 방향
		switch (heading) {
		case 1:
			xx = a1 - r;
			yy = a2 + r;
			yy1 = a2;
			xx2 = a1;
			xx3 = a2;
			yy3 = b2;
			xx4 = b1;
			yy4 = a1;
			break;
		case 2:
			xx = a1 + 1;
			xx1 = b1;
			yy1 = a2;
			xx2 = b1;
			yy2 = a1;
			xx3 = b1 - 3;
			yy3 = a2 - 2;
			xx4 = b1 - 2;
			yy4 = a1 + 3;
			break;
		case 3:
			xx = a1 - r;
			yy = a1 - r;
			xx1 = a1;
			yy2 = a1;
			xx3 = a1;
			yy3 = a2;
			xx4 = a2;
			yy4 = b1;
			break;
		case 4:
			yy = a1 + 1;
			xx1 = a1;
			yy1 = b1;
			xx2 = a2;
			yy2 = b1;
			xx3 = a1 + 3;
			yy3 = b1 - 3;
			xx4 = a2 - 3;
			yy4 = b1 - 3;
			break;
		case 5:
			xx = a2 + r;
			yy = a1 - r;
			yy1 = a1;
			xx2 = a2;
			xx3 = a1;
			yy3 = b1;
			xx4 = b2;
			yy4 = a2;
			break;
		case 6:
			xx = a2 - 1;
			xx1 = b2;
			yy1 = a1;
			xx2 = b2;
			yy2 = a2;
			xx3 = b2 + 3;
			yy3 = a1 + 2;
			xx4 = b2 + 2;
			yy4 = a2 - 3;
			break;
		case 7:
			xx = a2 + r;
			yy = a2 + r;
			xx1 = a2;
			yy2 = a2;
			xx3 = a2;
			yy3 = a1;
			xx4 = a1;
			yy4 = b2;
			break;
		case 0:
			yy = a2 - 1;
			xx1 = a2;
			yy1 = b2;
			xx2 = a1;
			yy2 = b2;
			xx3 = a2 - 3;
			yy3 = b2 + 3;
			xx4 = a1 + 3;
			yy4 = b2 + 3;
			break;
		default:
			break;
		}
		int x = _npc.getX() + xx;
		int y = _npc.getY() + yy;
		// 마름모 4*4픽셀 모양 (몹 기준에서 정면에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x - 1, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 1, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x + 2, y + 1, _user.getMapId());
		int x1 = _npc.getX() + xx1;
		int y1 = _npc.getY() + yy1;
		// 마름모 4*4픽셀 모양 (몹 기준에서 좌측에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 - 1, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 1, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x1 + 2, y1 + 1, _user.getMapId());
		int x2 = _npc.getX() + xx2;
		int y2 = _npc.getY() + yy2;
		// 마름모 4*4픽셀 모양 (몹 기준에서 우측에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 - 1, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 1, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x2 + 2, y2 + 1, _user.getMapId());
		int x3 = _npc.getX() + xx3;
		int y3 = _npc.getY() + yy3;
		// 마름모 4*4픽셀 모양 (몹 기준에서 좌측2에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 - 1, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 1, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x3 + 2, y3 + 1, _user.getMapId());
		int x4 = _npc.getX() + xx4;
		int y4 = _npc.getY() + yy4;
		// 마름모 4*4픽셀 모양 (몹 기준에서 우측2에 출현)
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 - 1, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4 + 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 1, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4 - 2, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4 - 1, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4, _user.getMapId());
		L1EffectSpawn.getInstance().spawnEffect(Effect, _skill.getBuffDuration() * 1000, x4 + 2, y4 + 1, _user.getMapId());
		return;
	}

	private void makeTargetList() {
		try {
			if (_type == TYPE_LOGIN) {
				_targetList.add(new TargetStatus(_user));
				return;
			}
			if (_skill.getTargetTo() == L1Skills.TARGET_TO_ME && (_skill.getType() & L1Skills.TYPE_ATTACK) != L1Skills.TYPE_ATTACK) {
				_targetList.add(new TargetStatus(_user));
				return;
			}

			if (_target == null)
				return;

			if (_skill.getRanged() != -1) {// 사정거리 -1 화면내 오브젝트만
				if (_user.getLocation().getTileLineDistance(_target.getLocation()) > _skill.getRanged()) {
					return;
				}
			} else {
				if (!_user.getLocation().isInScreen(_target.getLocation())) {
					return;
				}
			}

			if (isTarget(_target) == false && !(_skill.getTarget().equals("none"))) {
				return;
			}

			if (_skillId == LIGHTNING) {
				ArrayList<L1Object> olist = L1World.getInstance().getVisibleLineObjects(_user, _target);
				if (olist == null)
					return;
				for (L1Object tgobj : olist) {
					if (tgobj == null) {
						continue;
					}
					if (!(tgobj instanceof L1Character)) {
						continue;
					}
					L1Character cha = (L1Character) tgobj;
					if (isTarget(cha) == false) {
						continue;
					}
					_targetList.add(new TargetStatus(cha));
				}
				return;
			}

			if (_skillId == IMMUNE_TO_HARM) {
				if (_user.glanceCheck(_target.getX(), _target.getY()) == false) {
					return;
				}
			}

			if (_skill.getArea() == 0) {
				if (_user.glanceCheck(_target.getX(), _target.getY()) == false) {
					if ((_skill.getType() & L1Skills.TYPE_ATTACK) == L1Skills.TYPE_ATTACK) {
						_targetList.add(new TargetStatus(_target, false));
						return;
					}
				}
				_targetList.add(new TargetStatus(_target));
			} else {

				if (!_skill.getTarget().equals("none")) {
					_targetList.add(new TargetStatus(_target));
				}

				if (_skillId != 49 && !(_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK)) {
					_targetList.add(new TargetStatus(_user));
				}

				List<L1Object> objects;
				if (_skill.getArea() == -1) {
					objects = L1World.getInstance().getVisibleObjects(_user);
				} else {
					objects = L1World.getInstance().getVisibleObjects(_target, _skill.getArea());
				}
				for (L1Object tgobj : objects) {
					if (tgobj == null) {
						continue;
					}
					if (!(tgobj instanceof L1Character)) {
						continue;
					}
					L1Character cha = (L1Character) tgobj;
					if (!isTarget(cha)) {
						continue;
					}

					if (_skillId == METEOR_STRIKE || _skillId == ICE_METEOR_STRIKE) {
						if (cha instanceof L1PcInstance && _user instanceof L1PcInstance) {
							boolean isNowWar = false;
							int castleId = L1CastleLocation.getCastleIdByArea((L1PcInstance) _user);
							if (castleId != 0) {
								isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
							}
							if (isNowWar == false) {
								continue;
							}
						}
					}
					_targetList.add(new TargetStatus(cha));
				}
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendHappenMessage(L1PcInstance pc) {
		int msgID = _skill.getSysmsgIdHappen();
		if (msgID > 0) {
			pc.sendPackets(new S_ServerMessage(msgID));
		}
	}

	private void sendFailMessageHandle() {
		if (_skill.getType() != L1Skills.TYPE_ATTACK && !_skill.getTarget().equals("none") && _targetList.size() == 0) {
			sendFailMessage();
		}
	}

	private void sendFailMessage() {
		int msgID = _skill.getSysmsgIdFail();
		if (msgID > 0 && (_user instanceof L1PcInstance)) {
			_player.sendPackets(new S_ServerMessage(msgID));
		}
	}

	private boolean isAttrAgrees() {
		int magicattr = _skill.getAttr();
		if (_user instanceof L1NpcInstance) {
			return true;
		}

		if ((_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && magicattr != 0) && (magicattr != _player.getElfAttr() && !_player.isGm())) {
			return false;
		}
		return true;
	}

	private boolean isHPMPConsume() {
		_mpConsume = _skill.getMpConsume();
		_hpConsume = _skill.getHpConsume();
		int currentMp = 0;
		int currentHp = 0;

		if (_user instanceof L1NpcInstance) {
			currentMp = _npc.getCurrentMp();
			currentHp = _npc.getCurrentHp();
		} else {
			currentMp = _player.getCurrentMp();
			currentHp = _player.getCurrentHp();

			double int_pre = CalcStat.get_consume_mp(_player.getAbility().getInt()) * 0.01;
			int min_mp = (int) (_skill.getMpConsume() * int_pre);

			if (min_mp > 0) {
				_mpConsume -= min_mp;
			}
			if (_skillId == ARMOR_BRAKE && _player.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
				_mpConsume = 20;
				_hpConsume = 35;
			}
			if (_skillId == PHYSICAL_ENCHANT_DEX && _player.getInventory().checkEquipped(20013)) {
				_mpConsume /= 2;
			}
			if (_skillId == HASTE && _player.getInventory().checkEquipped(20013)) {
				_mpConsume /= 2;
			}
			if (_skillId == HEAL && _player.getInventory().checkEquipped(20014)) {
				_mpConsume /= 2;
			}
			if (_skillId == EXTRA_HEAL && _player.getInventory().checkEquipped(20014)) {
				_mpConsume /= 2;
			}
			if (_skillId == ENCHANT_WEAPON && _player.getInventory().checkEquipped(20015)) {
				_mpConsume /= 2;
			}
			if (_skillId == DETECTION && _player.getInventory().checkEquipped(20015)) {
				_mpConsume /= 2;
			}
			if (_skillId == PHYSICAL_ENCHANT_STR && _player.getInventory().checkEquipped(20015)) {
				_mpConsume /= 2;
			}
			if (_skillId == HASTE && _player.getInventory().checkEquipped(20008)) {
				_mpConsume /= 2;
			}
			if (_skillId == GREATER_HASTE && _player.getInventory().checkEquipped(20023)) {
				_mpConsume /= 2;
			}

			if (0 < _skill.getMpConsume()) {
				_mpConsume = Math.max(_mpConsume, 1);
			}
		}

		if (currentHp < _hpConsume + 1) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(new S_ServerMessage(279));
			}
			return false;
		} else if (currentMp < _mpConsume) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(new S_ServerMessage(278));
			}
			return false;
		}

		return true;
	}

	private boolean isItemConsume() {
		if (_player.getAI() != null)
			return true;

		int itemConsume = _skill.getItemConsumeId();
		int itemConsumeCount = _skill.getItemConsumeCount();

		if (itemConsume == 0) {
			return true;
		}

		if (itemConsume == 40318) { // 마력의 돌
			if (_player.getInventory().checkItem(30079, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 40321) { // 흑요석
			if (_player.getInventory().checkItem(30080, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 210035) { // 각인의 뼈조각
			if (_player.getInventory().checkItem(30081, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 210038) { // 속성석
			if (_player.getInventory().checkItem(30082, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		} else if (itemConsume == 40319) { // 정령옥
			if (_player.getInventory().checkItem(30078, itemConsumeCount) && _player.getLevel() < 82) {
				return true;
			}
		}
		if (!_player.getInventory().checkItem(itemConsume, itemConsumeCount)) {
			return false;
		}

		return true;
	}

	private void useConsume() {
		if (_user instanceof L1NpcInstance) {
			int current_hp = _npc.getCurrentHp() - _hpConsume;
			_npc.setCurrentHp(current_hp);

			int current_mp = _npc.getCurrentMp() - _mpConsume;
			_npc.setCurrentMp(current_mp);
			return;
		}

		if (isHPMPConsume()) {
			if (_skillId == FINAL_BURN) {
				_player.setCurrentHp(100);
				_player.setCurrentMp(1);
			} else {
				int current_hp = _player.getCurrentHp() - _hpConsume;
				_player.setCurrentHp(current_hp);

				int current_mp = _player.getCurrentMp() - _mpConsume;
				_player.setCurrentMp(current_mp);
			}
		}

		int lawful = _player.getLawful() + _skill.getLawful();
		if (lawful > 32767) {
			lawful = 32767;
		}
		if (lawful < -32767) {
			lawful = -32767;
		}
		_player.setLawful(lawful);

		int itemConsume = _skill.getItemConsumeId();
		int itemConsumeCount = _skill.getItemConsumeCount();

		if (itemConsume == 0 || _player.getAI() != null) {
			return;
		}

		if (itemConsume == 40318) { // 마력의 돌
			if (_player.getInventory().checkItem(30079, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30079;
			}
		} else if (itemConsume == 40321) { // 흑요석
			if (_player.getInventory().checkItem(30080, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30080;
			}
		} else if (itemConsume == 210035) { // 각인의 뼈조각
			if (_player.getInventory().checkItem(30081, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30081;
			}
		} else if (itemConsume == 210038) { // 속성석
			if (_player.getInventory().checkItem(30082, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30082;
			}
		} else if (itemConsume == 40319) { // 정령옥
			if (_player.getInventory().checkItem(30078, itemConsumeCount) && _player.getLevel() < 82) {
				itemConsume = 30078;
			}
		}
		_player.getInventory().consumeItem(itemConsume, itemConsumeCount);
	}

	private void addMagicList(L1Character cha, boolean repetition) {
		if (_skillTime == 0) {
			_getBuffDuration = _skill.getBuffDuration() * 1000;
			if (_skill.getBuffDuration() == 0) {
				if (_skillId == INVISIBILITY) {
					cha.setSkillEffect(INVISIBILITY, 0);
				}
				return;
			}
		} else {
			_getBuffDuration = _skillTime * 1000;
		}

		if (_skillId == DESPERADO || _skillId == SHOCK_STUN || _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8
				|| _skillId == OMAN_STUN || _skillId == Maeno_STUN || _skillId == EMPIRE) {
			_getBuffDuration = _shockStunDuration;
		}

		if (_skillId == EARTH_BIND) {
			_getBuffDuration = _earthBindDuration;
		}

		if (_skillId == CURSE_POISON || _skillId == TOMAHAWK || _skillId == TROGIR_MILPITAS1) {
			return;
		}

		if (_skillId == CURSE_PARALYZE || _skillId == CURSE_PARALYZE2) {
			return;
		}
		if ((_skillId == ICE_LANCE) && !_isFreeze) {
			return;
		}
		if (_skillId == L1SkillId.DESPERADO) {
			return;
		}

		// 별도로 setSkillEffect 호출된 상태인 스킬들
		switch (_skillId) {
		case CUBE_AVATAR:
		case CUBE_OGRE:
		case CUBE_GOLEM:
		case CUBE_RICH:
		case IMPACT:
		case HOLY_WEAPON:
		case ENCHANT_WEAPON:
		case BLESS_WEAPON:
			return;

		}
		
		if((_skillId != MOB_COCA || !cha.hasSkillEffect(MOB_COCA)) ||
				(_skillId != MOB_BASILL || !cha.hasSkillEffect(MOB_BASILL))
				){
			if(MonsterParalyzeDelay.getInstance().contains_paralyze(_skillId)){
				MonsterParalyze paralyze = MonsterParalyzeDelay.getInstance().get_paralyze(_skillId);
				cha.setSkillEffect(_skillId, (paralyze.paralyze_delay + paralyze.paralyze_millis));
			}else{
				cha.setSkillEffect(_skillId, _getBuffDuration);
			}
		}
		if(cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if(repetition)
				sendIcon(pc);
			if(_skillId == STRIKER_GALE) {
				int er = pc.getTotalER();
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, er), true);
			}
		}
	}

	private void sendIcon(L1PcInstance pc) {
		if (_skillTime == 0) {
			_getBuffIconDuration = _skill.getBuffDuration();
		} else {
			_getBuffIconDuration = _skillTime;
		}
		switch (_skillId) {
		case SHIELD:
			pc.sendPackets(new S_SkillIconShield(1, _getBuffIconDuration));
			break;
		case DRESS_DEXTERITY:
			pc.sendPackets(new S_Dexup(pc, 2, _getBuffIconDuration));
			break;
		case DRESS_MIGHTY:
			pc.sendPackets(new S_Strup(pc, 2, _getBuffIconDuration));
			break;
		case GLOWING_AURA:
			pc.sendPackets(new S_SkillIconAura(113, _getBuffIconDuration));
			break;
		case SHINING_AURA:
			pc.sendPackets(new S_SkillIconAura(114, _getBuffIconDuration));
			break;
		case BRAVE_AURA:
			pc.sendPackets(new S_SkillIconAura(116, _getBuffIconDuration));
			break;
		case EARTH_WEAPON:
			pc.sendPackets(new S_SkillIconAura(147, _getBuffIconDuration));
			break;
		case AQUA_SHOT:
			pc.sendPackets(new S_SkillIconAura(148, _getBuffIconDuration));
			break;
		case DANCING_BLADES:
			pc.sendPackets(new S_SkillIconAura(154, _getBuffIconDuration));
			break;
		case STORM_EYE:
			pc.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration));
			break;
		case EARTH_GUARDIAN:
			pc.sendPackets(new S_SkillIconShield(7, _getBuffIconDuration));
			break;
		case BURNING_WEAPON:
			pc.sendPackets(new S_SkillIconAura(162, _getBuffIconDuration));
			break;
		case STORM_SHOT:
			pc.sendPackets(new S_SkillIconAura(165, _getBuffIconDuration));
			break;
		case IRON_SKIN:
			pc.sendPackets(new S_SkillIconShield(10, _getBuffIconDuration));
			break;
		case FIRE_SHIELD:
			pc.sendPackets(new S_SkillIconShield(4, _getBuffIconDuration));
			break;
		case PHYSICAL_ENCHANT_STR:
			pc.sendPackets(new S_Strup(pc, 5, _getBuffIconDuration));
			break;
		case PHYSICAL_ENCHANT_DEX:
			pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration));
			break;
		case 나루토감사캔디:
			if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
				pc.sendPackets(new S_Dexup(pc, 7, _getBuffIconDuration));
				pc.sendPackets(new S_Strup(pc, 7, _getBuffIconDuration));
			} else {
				pc.sendPackets(new S_Dexup(pc, 6, _getBuffIconDuration));
				pc.sendPackets(new S_Strup(pc, 6, _getBuffIconDuration));
			}
			break;
		case HASTE:
		case GREATER_HASTE:
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
			break;
		case HOLY_WALK:
		case MOVING_ACCELERATION:
			pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
			break;
		case BLOOD_LUST:
			pc.sendPackets(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration));
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration));
			break;
		case SLOW:
		case MOB_SLOW_1:
		case MOB_SLOW_18:
			pc.sendPackets(new S_SkillHaste(pc.getId(), 2, _getBuffIconDuration));
			pc.broadcastPacket(new S_SkillHaste(pc.getId(), 2, 0));
			break;
		default:
			break;
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private void sendGrfx(boolean isSkillAction) {
		int actionId = _skill.getActionId();
		int actionId2 = _skill.getActionId2();
		int actionId3 = _skill.getActionId3();
		int castgfx = _skill.getCastGfx();
		int castgfx2 = _skill.getCastGfx2();
		int castgfx3 = _skill.getCastGfx3();

		if (castgfx == 0) {
			return;
		}
		/** 기르타스 독구름 **/
		if (_skillId == TROGIR_MILPITAS1) {
			int xx = 0;
			int yy = 0;
			int xx1 = 0;
			int yy1 = 0;
			int xx2 = 0;
			int yy2 = 0;
			Random random = new Random();
			int randomxy = random.nextInt(8);
			int a1 = 3 + randomxy;
			int a2 = -3 - randomxy;
			int b1 = 2 + randomxy;
			int b2 = -2 - randomxy;
			int heading = _npc.getMoveState().getHeading();
			switch (heading) {
			case 1:
				xx = a1;
				yy = a2;
				yy1 = a2;
				xx2 = a1;
				break;
			case 2:
				xx = a1;
				xx1 = b1;
				yy1 = a2;
				xx2 = b1;
				yy2 = a1;
				break;
			case 3:
				xx = a1;
				yy = a1;
				xx1 = a1;
				yy2 = a1;
				break;
			case 4:
				yy = a1;
				xx1 = a1;
				yy1 = b1;
				xx2 = a2;
				yy2 = b1;
				break;
			case 5:
				xx = a2;
				yy = a1;
				yy1 = a1;
				xx2 = a2;
				break;
			case 6:
				xx = a2;
				xx1 = b2;
				yy1 = a1;
				xx2 = b2;
				yy2 = a2;
				break;
			case 7:
				xx = a2;
				yy = a2;
				xx1 = a2;
				yy2 = a2;
				break;
			case 0:
				yy = a2;
				xx1 = a2;
				yy1 = b2;
				xx2 = a1;
				yy2 = b2;
				break;
			default:
				break;
			}
			int x = _npc.getX() + xx;
			int y = _npc.getY() + yy;
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y + 3, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y - 4, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x, y - 6, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 1, y, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 2, y + 2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 3, y - 4, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x - 4, y - 8, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 5, y + 2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 6, y - 5, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 5, y, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 3, y - 4, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 5, y - 6, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 7, y - 4, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 3, y, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x + 1, y + 3, _user.getMapId());
			int x1 = _npc.getX() + xx1;
			int y1 = _npc.getY() + yy1;
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1 + 1, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1 - 2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1, y1 - 3, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 6, y1, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 5, y1 + 7, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 4, y1 - 6, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 - 3, y1 - 1, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 5, y1 + 2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 4, y1 - 3, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 3, y1, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 2, y1 - 4, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 8, y1 - 5, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 7, y1 - 2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 6, y1, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x1 + 5, y1 + 3, _user.getMapId());
			int x2 = _npc.getX() + xx2;
			int y2 = _npc.getY() + yy2;
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2 + 3, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2 - 4, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2, y2 - 6, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 2, y2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 8, y2 + 2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 6, y2 - 3, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 - 4, y2 - 7, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 5, y2 + 5, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 3, y2 - 3, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 7, y2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 6, y2 - 6, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 8, y2 - 5, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 3, y2 - 7, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 4, y2, _user.getMapId());
			L1EffectSpawn.getInstance().spawnEffect(71268, _skill.getBuffDuration() * 1000, x2 + 6, y2 + 2, _user.getMapId());
			return;
		}

		if (_isCriticalDamage) {
			switch (_skillId) {
			case CALL_LIGHTNING: // 콜라이트닝
				castgfx = 11737;
				break;
			case SUNBURST: // 선버스트
				castgfx = 11760;
				break;
			case CONE_OF_COLD_mob:
			case CONE_OF_COLD: // 콘 오브 콜드
				castgfx = 11742;
				break;
			case DISINTEGRATE: // 디스인티그레이트
				castgfx = 11748;
				break;
			case ERUPTION:
				castgfx = 11754;
				break;
			}
		} else {
			if (_skillId == UNCANNY_DODGE) {
				L1PcInstance pc = (L1PcInstance) _target;
				if (pc.getAC().getAc() <= -100) {
					castgfx = 11766;
				}
			} else {
				if (castgfx != _skill.getCastGfx()) {
					return; // 그래픽 번호가 다르다.
				}
			}
		}

		if (castgfx2 != _skill.getCastGfx2()) {
			return;
		}
		if (castgfx3 != _skill.getCastGfx3()) {
			return;
		}

		if (_user instanceof L1PcInstance) {
			if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM) {
				L1PcInstance pc = (L1PcInstance) _user;
				if (_skillId == FIRE_WALL) {
					pc.setHeading(pc.targetDirection(_targetX, _targetY));
					pc.sendPackets(new S_ChangeHeading(pc));
					pc.broadcastPacket(new S_ChangeHeading(pc));
				}
				S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
				pc.sendPackets(gfx);
				pc.broadcastPacket(gfx);
				return;
			}

			int targetid = _target.getId();
			if(_skillId == EMPIRE) {
				_target.send_effect(17569);
				return;
			}
			if (_skillId == SHOCK_STUN || _skillId == MOB_SHOCKSTUN_30 || _skillId == MOB_RANGESTUN_20 || _skillId == MOB_RANGESTUN_19
					|| _skillId == MOB_RANGESTUN_18 || _skillId == Mob_RANGESTUN_30 || _skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7
					|| _skillId == ANTA_MESSAGE_8 || _skillId == OMAN_STUN || _skillId == Maeno_STUN || _skillId == EMPIRE) {
				if (_targetList.size() == 0) {// 실패 스턴 모션
					if (_target instanceof L1PcInstance) { // Gn.89
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 4434));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 4434));
						pc.sendPackets(new S_ServerMessage(280));
					} else if (_target instanceof L1NpcInstance) {
						_target.broadcastPacket(new S_SkillSound(_target.getId(), 4434));
					}
					return;
				} else {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 4434));
						pc.broadcastPacket(new S_SkillSound(pc.getId(), 4434));
					} else if (_target instanceof L1NpcInstance) {
						_target.broadcastPacket(new S_SkillSound(_target.getId(), 4434));
					}
					return;
				}
			}

			if (_skillId == LIGHT) {
				L1PcInstance pc = (L1PcInstance) _target;
				pc.sendPackets(new S_Sound(145));
			}
			if (_skillId == SOUL_OF_FLAME) {
				L1PcInstance pc = (L1PcInstance) _target;
				pc.sendPackets(new S_SkillSound(pc.getId(), 11778, 19));
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 11778, _getBuffIconDuration));
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 11778));
			}

			if (_skillId == UNCANNY_DODGE) {
				L1PcInstance pc = (L1PcInstance) _target;
				if (pc.getAC().getAc() <= -100) {
					pc.sendPackets(new S_SkillSound(pc.getId(), 11766, 19));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 11766, 19));
				} else {
					pc.sendPackets(new S_SkillSound(pc.getId(), 11765, 19));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 11765, 19));
				}
			}

			if (_targetList.size() == 0 && !(_skill.getTarget().equals("none"))) {
				int tempchargfx = _player.getCurrentSpriteId();
				if (tempchargfx == 5727 || tempchargfx == 5730) {
					actionId = ActionCodes.ACTION_SkillBuff;
				} else if (tempchargfx == 5733 || tempchargfx == 5736) {
					actionId = ActionCodes.ACTION_Attack;
				}
				if (isSkillAction) {
					S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), actionId);
					_player.sendPackets(gfx);
					_player.broadcastPacket(gfx);
				}
				return;
			}

			if (_skill.getTarget().equals("attack") && _skillId != 18) {
				if (isPcSummonPet(_target)) {
					if (_player.getZoneType() == 1 || _target.getZoneType() == 1 || _player.checkNonPvP(_player, _target)) {
						if (_skillId == THUNDER_GRAB && _player.isPassive(MJPassiveID.THUNDER_GRAP_BRAVE.toInt())) {
							_player.send_action(18);
							_target.send_effect(17229);
							return;
						}
						_player.sendPackets(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId));
						_player.broadcastPacket(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId));
						return;
					}
				}
				if (_skillId == THUNDER_GRAB && _player.isPassive(MJPassiveID.THUNDER_GRAP_BRAVE.toInt())) {
					_player.send_action(18);
					_target.send_effect(17229);
					_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _player);
				} else if (_skill.getArea() == 0) {
					_player.sendPackets(new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId));
					_player.broadcastPacket(new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId), _target);
					_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _player);
				} else {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						i++;
					}
					_player.sendPackets(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR));
					_player.broadcastPacket(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), cha);
				}
			} else if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK) {
				L1Character[] cha = new L1Character[_targetList.size()];
				int i = 0;
				for (TargetStatus ts : _targetList) {
					cha[i] = ts.getTarget();
					cha[i].broadcastPacketExceptTargetSight(new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _player);
					i++;
				}
				_player.sendPackets(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR));
				_player.broadcastPacket(new S_RangeSkill(_player, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), cha);
			} else {
				if (_skillId != 5 && _skillId != 69 && _skillId != 131) {
					if (isSkillAction) {
						S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), _skill.getActionId());
						_player.sendPackets(gfx);
						_player.broadcastPacket(gfx);
					}
					if (_skillId == COUNTER_MAGIC) {
						_player.sendPackets(new S_SkillSound(targetid, castgfx));
						_player.broadcastPacket(new S_SkillSound(targetid, castgfx));
					} else if (_skillId == TRUE_TARGET) {
						return;
					} else if (_skillId == CANCELLATION) {
						return;
					} else if (_skillId == THUNDER_GRAB) {
						/*
						 * if (_player.isPassive(MJPassiveID.THUNDER_GRAP_BRAVE.
						 * toInt())) { castgfx = 17229; }
						 * _target.send_effect(castgfx);
						 */
					} else if (_skillId == ARMOR_BRAKE) {
						if (_player.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
							castgfx = 17226;
						}
						
						_player.sendPackets(new S_SkillSound(targetid, castgfx));
						/*
						 * if(_target != null && _target instanceof
						 * L1PcInstance){ int rnd = random.nextInt(5) + 1;//시간
						 * 랜덤 L1PcInstance targetPc = (L1PcInstance)_target;
						 * SC_SPELL_BUFF_NOTI noti =
						 * SC_SPELL_BUFF_NOTI.newInstance();
						 * noti.set_noti_type(eNotiType.RESTAT);
						 * noti.set_spell_id(L1SkillId.ARMOR_BRAKE);
						 * noti.set_duration(3 + rnd);//시간
						 * noti.set_duration_show_type(eDurationShowType.
						 * TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						 * noti.set_on_icon_id(4473);
						 * noti.set_off_icon_id(4473);
						 * noti.set_icon_priority(0);
						 * noti.set_tooltip_str_id(3141);
						 * noti.set_new_str_id(0); noti.set_end_str_id(2238);
						 * noti.set_is_good(false); targetPc.sendPackets(noti,
						 * MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true); }
						 */
					} else if (_skillId == DOUBLE_BRAKE) {
						if (_player.isPassive(MJPassiveID.DOUBLE_BREAK_DESTINY.toInt())) {
							castgfx = 17224;
						}
						_player.send_effect(castgfx);
					} else if (_skillId == COUNTER_BARRIER) {
						if (_player.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
							castgfx = 17219;
						}
						_player.send_effect(castgfx);
					} else if (_skillId == DESPERADO) {
						if (_player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
							castgfx = 17233;
						}
						_target.send_effect(castgfx);
					} else {
						_player.sendPackets(new S_SkillSound(targetid, castgfx));
						_player.broadcastPacket(new S_SkillSound(targetid, castgfx));
					}
				}
				for (TargetStatus ts : _targetList) {
					L1Character cha = ts.getTarget();
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_OwnCharStatus(pc));
					}
				}
			}
		} else if (_user instanceof L1NpcInstance) {
			int targetid = _target.getId();
			if (_skillId == BLACKELDER) {
				Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 4848));
				Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 2552));
			}

			if (_user instanceof L1MerchantInstance) {
				_user.broadcastPacket(new S_SkillSound(targetid, castgfx));
				return;
			}

			if (_targetList.size() == 0 && !(_skill.getTarget().equals("none"))) {
				S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
				_user.broadcastPacket(gfx);
				return;
			}

			if (_skill.getTarget().equals("attack") && _skillId != 18) {
				if (_skillId == WIDE_ARMORBREAK) {
					if (_targetList.size() > 0) {
						final int effect = castgfx;
						final L1Character[] targets = new L1Character[_targetList.size()];
						for (int i = _targetList.size() - 1; i >= 0; --i) {
							L1Character c = _targetList.get(i).getTarget();
							targets[i] = c == null || c.isDead() ? null : c;
						}
						GeneralThreadPool.getInstance().execute(new Runnable() {
							@Override
							public void run() {
								try {
									for (int i = targets.length - 1; i >= 0; --i) {
										L1Character t = targets[i];
										if (t == null || t.isDead())
											continue;
										t.send_effect(effect);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				} else if (_skill.getArea() == 0) {
					if(_skillId == 20030)
						_target.send_effect(castgfx);
//						_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx, _targetX, _targetY, actionId), _target);
					else
						_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx, _targetX, _targetY, actionId), _target);
					if (actionId2 > 0 && castgfx2 > 0) {
						_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx2, _targetX, _targetY, actionId2), _target);
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
					}
					if (actionId3 > 0 && castgfx3 > 0) {
						_user.broadcastPacket(new S_UseAttackSkill(_user, targetid, castgfx3, _targetX, _targetY, actionId3), _target);
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
					}
					_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
				} else {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						cha[i].broadcastPacketExceptTargetSight(new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _user);
						i++;
					}
					_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), cha);
					if (actionId2 > 0 && castgfx2 > 0) {
						_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx2, actionId2, S_RangeSkill.TYPE_DIR));
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
					}
					if (actionId3 > 0 && castgfx3 > 0) {
						_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx3, actionId3, S_RangeSkill.TYPE_DIR));
						_target.broadcastPacketExceptTargetSight(new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user);
					}
				}
			} else if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK) {
				L1Character[] cha = new L1Character[_targetList.size()];
				int i = 0;
				for (TargetStatus ts : _targetList) {
					cha[i] = ts.getTarget();
					i++;
				}
				_user.broadcastPacket(new S_RangeSkill(_user, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), cha);
			} else {
				if (_skillId != 5 && _skillId != 69 && _skillId != 131) {
					S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
					_user.broadcastPacket(gfx);

					if (_skillId == OMAN_CANCELLATION || _skillId == ANTA_MESSAGE_1 || _skillId == ANTA_CANCELLATION || _skillId == PAP_PREDICATE7
							|| _skillId == PAP_PREDICATE11 || _skillId == PAP_PREDICATE12 || _skillId == RINDVIOR_PREDICATE_CANCELLATION
							|| _skillId == RINDVIOR_CANCELLATION || _skillId == BLACKELDER_DEATH_HELL || _skillId == PHOENIX_CANCELLATION) {
						return;
					} else {
						_user.broadcastPacket(new S_SkillSound(targetid, castgfx));
					}

					if (actionId2 > 0 && castgfx2 > 0) {
						S_DoActionGFX gfx2 = new S_DoActionGFX(_user.getId(), _skill.getActionId2());
						_user.broadcastPacket(gfx2);
						_user.broadcastPacket(new S_SkillSound(targetid, castgfx2));
					}
					if (actionId3 > 0 && castgfx3 > 0) {
						S_DoActionGFX gfx3 = new S_DoActionGFX(_user.getId(), _skill.getActionId3());
						_user.broadcastPacket(gfx3);
						_user.broadcastPacket(new S_SkillSound(targetid, castgfx3));
					}
				}
			}
		}
	}

	private static final int[] SCALES = new int[] {
			SCALES_EARTH_DRAGON,
			SCALES_WATER_DRAGON,
			SCALES_FIRE_DRAGON,
			SCALES_RINDVIOR_DRAGON,
	};
	private boolean check_scales_skill(L1Character cha) {
		if(_skillId == SCALES[0] ||
				_skillId == SCALES[1] ||
				_skillId == SCALES[2] ||
				_skillId == SCALES[3]
				) {
			cha.removeSkillEffect(_skillId);
			int[] remains = new int[] {
					cha.getSkillEffectTimeSec(SCALES[0]),
					cha.getSkillEffectTimeSec(SCALES[1]),
					cha.getSkillEffectTimeSec(SCALES[2]),
					cha.getSkillEffectTimeSec(SCALES[3]),	
			};
			
			if(!cha.isPassive(MJPassiveID.AURAKIA.toInt())){
				for(int i=remains.length - 1; i>=0; --i) {
					int sid = SCALES[i];
					if(sid >= 0) {
						cha.removeSkillEffect(sid);
					}
				}
			}else {			
				int count = (remains[0] == -1 ? 0 : 1) +
						(remains[1] == -1 ? 0 : 1) +
						(remains[2] == -1 ? 0 : 1) +
						(remains[3] == -1 ? 0 : 1);
				if(count >= 2) {
					int removed_skill_idx = 0;
					int previous_remains = Integer.MAX_VALUE;
					for(int i = remains.length - 1; i>=0; --i) {
						if(remains[i] == -1)
							continue;

						if(remains[i] < previous_remains) {
							previous_remains = remains[i];
							removed_skill_idx = i;
						}
					}
					cha.removeSkillEffect(SCALES[removed_skill_idx]);
				}
			}
			return true;
		}
		return false;
	}
	
	private void deleteRepeatedSkills(L1Character cha) {
		if(check_scales_skill(cha))
			return;
		
		final int[][] repeatedSkills = { // (술자전용같은 효과 버프 중복안되게 처리(같은 라인에 맞춰서 적용됨)
				{ EARTH_WEAPON, AQUA_SHOT, STORM_EYE, BURNING_WEAPON, STORM_SHOT }, // 0번라인
				{ SHIELD, FIRE_SHIELD, IRON_SKIN }, // 1번라인
				{ HOLY_WALK, BLOOD_LUST, MOVING_ACCELERATION, STATUS_BRAVE, STATUS_ELFBRAVE, FOCUS_WAVE, HURRICANE, SAND_STORM, DANCING_BLADES }, // 2번라인
				{ HASTE, GREATER_HASTE, STATUS_HASTE }, // 3번라인
				{ PHYSICAL_ENCHANT_DEX, 나루토감사캔디, DRESS_DEXTERITY }, // 4번라인
				{ PHYSICAL_ENCHANT_STR, DRESS_MIGHTY, }, // 5번라인
				{ COUNTER_MIRROR, DECREASE_WEIGHT, REDUCE_WEIGHT }, // 6번라인
				{ FAFU_MAAN, ANTA_MAAN, LIND_MAAN, VALA_MAAN, LIFE_MAAN, BIRTH_MAAN, SHAPE_MAAN, 정상의가호 }, // 7번라인
				{ PAP_FIVEPEARLBUFF, PAP_MAGICALPEARLBUFF }, // 9번라인
				{ SIDE_OF_ME_BLESSING, RE_START_BLESSING, NEW_START_BLESSING, LIFE_BLESSING }, // 10번라인
				{ DRAGON_HUNTER_BLESS,HUNTER_BLESS, HUNTER_BLESS1, HUNTER_BLESS2 }, 
				{ EXP_BUFF, EXP_POTION }, 
				{ ADVANCE_SPIRIT, GIGANTIC, PRIDE },
				{ DRAGON_SET }, { CUBE_OGRE, IllUSION_OGRE }, 
				{ CUBE_AVATAR, IllUSION_AVATAR },
				{ CUBE_GOLEM, IllUSION_DIAMONDGOLEM },
				{ CUBE_RICH, IllUSION_LICH }, };
		for (int[] skills : repeatedSkills) {
			for (int id : skills) {
				if (id == _skillId) {
					stopSkillList(cha, skills);
				}
			}
		}
		
	}

	private void stopSkillList(L1Character cha, int[] repeat_skill) {
		for (int skillId : repeat_skill) {
			if (skillId != _skillId) {
				cha.removeSkillEffect(skillId);
			}
		}
	}

	private void setDelay() {
		
		int ReuseDelay = _skill.getReuseDelay();
		if (_user instanceof L1PcInstance) {
			L1PcInstance _pc = (L1PcInstance)_user;
			if(_pc.isHaste() == false){
				ReuseDelay *= 1.32;
			}
			
			if(_pc.isWizard() == false && _pc.isDarkelf() == false){
				if(_pc.isBrave() == false && _pc.isElfBrave() == false &&
						_pc.isFruit() == false  && _pc.isBlood_lust() == false){
					ReuseDelay *= 1.32;
				}
			}
			_user.setSpelldelay((int)(ReuseDelay*0.98));
		}
		
		
		/*int reuse = _skill.getReuseDelay();
		if (_player != null) {
			if (_player.isHaste()) {
				reuse -= 250;
			}
			if (_player.isBrave() || _player.isElfBrave()) {
				reuse -= 300;
			}
			reuse -= 300;
			if (reuse < 500) {
				reuse = 500;
			}
			if (_skill.getReuseDelay() > 0) {
				L1SkillDelay.onSkillUse(_user, reuse);
			}
		}*/
		
		
	}

	/** 안타라스 파푸리온 Message */
	private void MonsterMessage(int type) {
		String MonMessage = " ";
		if (type == 1) { // 안타라스
			switch (_skillId) {
			case ANTA_MESSAGE_1:
				MonMessage = "$7861";
				break;
			case ANTA_MESSAGE_2:
				MonMessage = "$7911";
				break;
			case ANTA_MESSAGE_3:
				MonMessage = "$7905";
				break;
			case ANTA_MESSAGE_4:
				MonMessage = "$7907";
				break;
			case ANTA_MESSAGE_5:
				MonMessage = "$7863";
				break;
			case ANTA_MESSAGE_6:
				MonMessage = "$7903";
				break;
			case ANTA_MESSAGE_7:
				MonMessage = "$7909";
				break;
			case ANTA_MESSAGE_8:
				MonMessage = "$7915";
				break;
			case ANTA_MESSAGE_9:
				MonMessage = "$7862";
				break;
			case ANTA_MESSAGE_10:
				MonMessage = "$7913";
				break;
			default:
				break;
			}
		} else if (type == 2) { // 파푸리온
			switch (_skillId) {
			case PAP_PREDICATE1:
				MonMessage = "$8467";
				break;
			case PAP_PREDICATE3:
				MonMessage = "$8458";
				break;
			case PAP_PREDICATE5:
				MonMessage = "$8456";
				break;
			case PAP_PREDICATE6:
				MonMessage = "$8457";
				break;
			case PAP_PREDICATE7:
				MonMessage = "$8454";
				break;
			case PAP_PREDICATE8:
				MonMessage = "$8455";
				break;
			case PAP_PREDICATE9:
				MonMessage = "$8460";
				break;
			case PAP_PREDICATE11:
				MonMessage = "$8463";
				break;
			case PAP_PREDICATE12:
				MonMessage = "$8465";
				break;
			default:
				break;
			}
		}
		_user.broadcastPacket(new S_NpcChatPacket(_npc, MonMessage, 0));
		return;
	}

	private void runSkill() {
		if (_player != null && _player.isInvisble()) {
			if (_skill.getType() == L1Skills.TYPE_ATTACK || _skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) {
				_player.delInvis();
			}
		}

		if (_skillId == METEOR_STRIKE || _skillId == ICE_METEOR_STRIKE && _target instanceof L1PcInstance) {
			if (_target.hasSkillEffect(ANTI_METEOR)) {
				return;
			}

			_target.setSkillEffect(ANTI_METEOR, 2000);
		}

		if (_skillId == FINAL_BURN && _target instanceof L1PcInstance) {
			if (_target.hasSkillEffect(ANTI_FINAL_BURN)) {
				return;
			}

			_target.setSkillEffect(ANTI_FINAL_BURN, 2000);
		}

		if (_skillId == LIFE_STREAM) {
			L1EffectSpawn.getInstance().spawnEffect(81169, _skill.getBuffDuration() * 1000, _targetX, _targetY, _user.getMapId());
			return;
		}

		if (_skillId == FIRE_WALL) {
			L1EffectSpawn.getInstance().doSpawnFireWall(_user, _targetX, _targetY);
			return;
		}

		for (int skillId : EXCEPT_COUNTER_MAGIC) {
			if (_skillId == skillId) {
				_isCounterMagic = false;
				break;
			}
		}

		if (_skillId == SHOCK_STUN || _skillId == EMPIRE || _skillId == BONE_BREAK && _user instanceof L1PcInstance) {
			_target.onAction(_player);
		}

		if (!isTargetCalc(_target)) {
			return;
		}

		// 독 구름
		if (_skillId == DESERT_SKILL4 || _skillId == ZENITH_Poison) {
			EffectSpawn();
		}

		/** MonsterMessage Type 1: 안타라스, Type 2: 파푸리온 **/
		if (_skillId >= ANTA_MESSAGE_1 && _skillId <= ANTA_MESSAGE_10) {
			MonsterMessage(1);
		}
		if (_skillId >= PAP_PREDICATE1 && _skillId <= PAP_PREDICATE12) {
			MonsterMessage(2);
		}
		try {
			TargetStatus ts = null;
			L1Character cha = null;
			int dmg = 0;
			int drainMana = 0;
			int heal = 0;
			boolean isSuccess = false;
			int undeadType = 0;

			for (Iterator<TargetStatus> iter = _targetList.iterator(); iter.hasNext();) {
				ts = null;
				cha = null;
				dmg = 0;
				heal = 0;
				isSuccess = false;
				undeadType = 0;

				ts = iter.next();
				cha = ts.getTarget();
				if (!ts.isCalc() || !isTargetCalc(cha)) {
					continue;
				}

				L1Magic _magic = new L1Magic(_user, cha);
				_magic.setLeverage(getLeverage());

				// TODO 클라우디아 마법 대미지 상향
				/*
				 * if (_user.getMapId() == 1 || _user.getMapId() == 2 ||
				 * _user.getMapId() == 3 || _user.getMapId() == 7 ||
				 * _user.getMapId() == 8 || _user.getMapId() == 10 ||
				 * _user.getMapId() == 11 || _user.getMapId() == 12 ||
				 * _user.getMapId() == 12146 || _user.getMapId() == 12147 ||
				 * _user.getMapId() == 9) {
				 */

				if (_calcType == PC_NPC && _targetList.size() >= 1) {
					_magic.setSimSimLeverge(Config.New_MagicDmg);
				}
				// TODO PC->NPC 범위 마법 대미지 외부 상세
				if (_user.getLevel() >= Config.sim_levelmin1 && _user.getLevel() <= Config.sim_levelmax1) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.simsimDmg1);
				} else if (_user.getLevel() >= Config.sim_levelmin2 && _user.getLevel() <= Config.sim_levelmax2) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.simsimDmg2);
				} else if (_user.getLevel() >= Config.sim_levelmin3 && _user.getLevel() <= Config.sim_levelmax3) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.simsimDmg3);
				} else if (_user.getLevel() >= Config.sim_levelmin4 && _user.getLevel() <= Config.sim_levelmax4) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.simsimDmg4);
				} else if (_user.getLevel() >= Config.sim_levelmin5 && _user.getLevel() <= Config.sim_levelmax5) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.simsimDmg5);
				} else if (_user.getLevel() >= Config.sim_levelmin6 && _user.getLevel() <= Config.sim_levelmax6) {
					if (_calcType == PC_NPC && _targetList.size() >= 2) // 2마리이상일경우
						_magic.setSimSimLeverge(Config.simsimDmg6);
				} else {
					_magic.setSimSimLeverge(1);
				}

				if (cha instanceof L1MonsterInstance) {
					undeadType = ((L1MonsterInstance) cha).getNpcTemplate().get_undead();
				}

				if ((_skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) && isTargetFailure(cha)) {
					iter.remove();
					continue;
				}

				if (cha instanceof L1PcInstance) {
					if (_skillTime == 0) {
						_getBuffIconDuration = _skill.getBuffDuration();
					} else {
						_getBuffIconDuration = _skillTime;
					}
				}

				deleteRepeatedSkills(cha);
				L1PcInstance ownPc = null, tarPc = null;
				if (_user instanceof L1PcInstance) {
					ownPc = (L1PcInstance) _user;
					removeNewIcon(ownPc, _skillId);
				}
				if (_target instanceof L1PcInstance) {
					tarPc = (L1PcInstance) _target;
					removeNewIcon(tarPc, _skillId);
				}
				if (_skill.getType() == L1Skills.TYPE_ATTACK && _user.getId() != cha.getId()) {
					if (isUseCounterMagic(cha)) {
						iter.remove();
						continue;
					}
					dmg = _magic.calcMagicDamage(_skillId);
					if (ownPc != null) {
						dmg += ownPc.get_lateral_magic_rate();
						if (tarPc != null) {
							dmg += ownPc.getResistance().getPVPweaponTotalDamage();
							dmg -= tarPc.get_pvp_defense();
						}
					}
					if (_calcType == PC_PC) {
						if (ownPc.getRedKnightClanId() != 0)
							ownPc.addRedKnightDamage(tarPc.getRedKnightClanId() == 0 ? dmg : -dmg);
					}

					if (_magic.isCriticalDamage()) {
						_isCriticalDamage = true;
					} else {
						_isCriticalDamage = false;
					}

					// 공격 스킬일때!! 이레이즈 여부 판멸후 제거
					if (_skillId != SHOCK_STUN && _skillId != TRIPLE_ARROW && _skillId != FOU_SLAYER && _skillId != EMPIRE) {
						if (cha instanceof L1PcInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA));
							}
						} else if (cha instanceof L1MonsterInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
							}
						}
					}
				} else if (_skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) {
					isSuccess = _magic.calcProbabilityMagic(_skillId);
					if (_skillId == SHOCK_STUN) {
						if (!isSuccess && _target != null) {
							_target.send_effect(4434);
						}
					}

					// 이레 마법이 아니고 현재 이레중이라면!!!
					if (cha instanceof L1PcInstance && _user instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1PinkName.onAction(pc, _user);
					}

					if (_skillId != ERASE_MAGIC && _skillId != EARTH_BIND) {
						if (cha instanceof L1PcInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA));
							}
						} else if (cha instanceof L1MonsterInstance) {
							if (cha.hasSkillEffect(ERASE_MAGIC)) {
								cha.killSkillEffectTimer(ERASE_MAGIC);
							}
						}
					}
					if (_skillId != FOG_OF_SLEEPING) {
						cha.removeSkillEffect(FOG_OF_SLEEPING);
					}
					if (_skillId != PHANTASM) {
						cha.removeSkillEffect(PHANTASM);
					}
					if (isSuccess) {
						if (isUseCounterMagic(cha)) {
							iter.remove();
							continue;
						}
					} else {
						if (_skillId == FOG_OF_SLEEPING && cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_ServerMessage(297));
						} else if (_skillId == DESPERADO) {
							int effectId = 12758;
							if (_player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
								effectId = 17233;
							}
							_target.send_effect(effectId);
						}
						iter.remove();
						continue;
					}
				} else if (_skill.getType() == L1Skills.TYPE_HEAL) {
					dmg = -1 * _magic.calcHealing(_skillId);
					if (cha.hasSkillEffect(WATER_LIFE)) {
						dmg *= 2;
					}
					if (cha.hasSkillEffect(POLLUTE_WATER)) {
						dmg /= 2;
					}
					if (cha.hasSkillEffect(PAP_REDUCE_HELL)) {
						dmg /= 2;
					}
					if (cha.hasSkillEffect(BLACKELDER_DEATH_HELL) || cha.hasSkillEffect(DEATH_HEAL) || cha.hasSkillEffect(DEATH_HEAL_Mob)) {
						dmg = (int) (_magic.calcHealing(_skillId) * 0.3);
						if (cha.hasSkillEffect(WATER_LIFE)) {
							dmg *= 2;
						}
						if (cha.hasSkillEffect(POLLUTE_WATER)) {
							dmg /= 2;
						}
					}

					if (_calcType == PC_PC) {
						if (ownPc.getRedKnightClanId() != 0)
							ownPc.addRedKnightDamage(tarPc.getRedKnightClanId() == 0 ? -dmg : dmg);
					}
				}
				// TODO 스킬사용시 시간 재갱신
				if (_skillId != STRIKER_GALE && _skillId != EAGGLE_EYE && _skillId != FOCUS_SPRITS && _skillId != LUCIFER && _skillId != CUBE_OGRE && _skillId != CUBE_GOLEM && _skillId != CYCLONE
						&& _skillId != CUBE_RICH && _skillId != CUBE_AVATAR && _skillId != IllUSION_OGRE && _skillId != IllUSION_LICH && _skillId != GIGANTIC && _skillId != PRIDE
						&& _skillId != L1SkillId.IllUSION_DIAMONDGOLEM && _skillId != L1SkillId.IllUSION_AVATAR && _skillId != IMMUNE_TO_HARM
						&& _skillId != BLOW_ATTACK && _skillId != MAJESTY && _skillId != CLEAR_MIND && _skillId != FREEZEENG_ARMOR
						&& _skillId != SHINING_ARMOR && _skillId != ENCHANT_ACURUCY && _skillId != SOLID_CARRIAGE && _skillId != REDUCTION_ARMOR
						&& _skillId != BLOW_ATTACK  && _skillId != DRESS_EVASION && _skillId != ADVANCE_SPIRIT && _skillId != SHADOW_FANG
						&& _skillId != HOLY_WEAPON && _skillId != AQUA_PROTECTER && _skillId != BLESS_WEAPON && _skillId != ENCHANT_WEAPON && _skillId != BLESSED_ARMOR) {
					if (cha.hasSkillEffect(_skillId) && _skillId != SHOCK_STUN && _skillId != THUNDER_GRAB && _skillId != EMPIRE && _skillId != OMAN_STUN && _skillId != Maeno_STUN
							&& _skillId != ANTA_MESSAGE_6 && _skillId != ANTA_MESSAGE_7 && _skillId != ANTA_MESSAGE_8 && _skillId != DRAGON_HUNTER_BLESS && _skillId != miso1 && _skillId != miso2
							&& _skillId != DRAGON_SET && _skillId != HUNTER_BLESS && _skillId != HUNTER_BLESS1 && _skillId != HUNTER_BLESS2 && _skillId != miso3
							&& _skillId != FOCUS_WAVE && _skillId != GIGANTIC ) {
						addMagicList(cha, true);
						if (_skillId != SHAPE_CHANGE) {
							continue;
						}
					}
				}
				// ●●●● PC, NPC 양쪽 모두 효과가 있는 스킬 ●●●●
				// GFX Check (Made by HuntBoy)
				switch (_skillId) {
				/** 세이프 체인지 이동. **/
				case SHAPE_CHANGE: {
					boolean isSameClan = false;
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getClanid() != 0 && _player.getClanid() == pc.getClanid()) {
							isSameClan = true;
						}
						if (pc.getId() != _player.getId() && (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0 || pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0)) {
							pc.sendPackets(new S_SkillSound(pc.getId(), 15846));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 15846));
							_player.sendPackets(new S_ServerMessage(280));
							return;
						}
					}
					if (cha instanceof L1MonsterInstance) {
						L1MonsterInstance mon = (L1MonsterInstance) cha;
						if (!mon.getNpcTemplate().isShapeChange())
							return;
					}

					if (_player.getId() != cha.getId() && !isSameClan) {
						int probability = 80;
						int rnd = random.nextInt(100) + 1;
						if (rnd > probability) {
							return;
						}
					}

					int pid = random.nextInt(polyArray.length);
					int polyId = polyArray[pid];
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getInventory().checkEquipped(20281) || pc.getInventory().checkEquipped(900075)) {
							pc.sendPackets(new S_Message_YN(180));
						} else {
							L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
							L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
							if (_player.getId() != pc.getId()) {
								pc.sendPackets(new S_ServerMessage(241, _player.getName()));
							}
						}
					} else if (cha instanceof L1MonsterInstance) {
						L1MonsterInstance mon = (L1MonsterInstance) cha;
						L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SHAPE_CHANGE);
						L1PolyMorph.doPoly(mon, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
					}
				}
					break;
				case HALPAS:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha; 
						pc.setAddReduction(pc.getAddReduction() + 5);
					}
					break;
				case N_BUFF_PVP_DMG:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addPVPweaponTotalDamage(1);
						pc.getResistance().addcalcPcDefense(1);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_HPMP:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addMaxHp(50);
						pc.addMaxMp(50);
						pc.addWeightReduction(3);
						pc.sendPackets(new S_HPUpdate(pc));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_DMG:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(1);
						pc.addBowDmgup(1);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_REDUCT:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDamageReductionByArmor(1);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_SP:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addSp(1);
						pc.sendPackets(new S_SPMR(pc));
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_STUN:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addSpecialResistance(eKind.ABILITY, 2); // 옵션
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_HOLD:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addSpecialResistance(eKind.SPIRIT, 2); // 옵션
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_WATER_DMG:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_WIND_DMG:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_EARTH_DMG:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_FIRE_DMG:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_STR:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) 1);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_DEX:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedDex((byte) 1);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_INT:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedInt((byte) 1);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_WIS:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedWis((byte) 1);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
					}
					break;
				case N_BUFF_WATER:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addWater(5);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
					break;
				case N_BUFF_WIND:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addWind(5);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
					break;
				case N_BUFF_EARTH:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addEarth(5);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
					break;
				case N_BUFF_FIRE:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addFire(5);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
					break;
				case N_BUFF_ALL_RESIST:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addFire(5);
						pc.getResistance().addEarth(5);
						pc.getResistance().addWater(5);
						pc.getResistance().addWind(5);
						pc.sendPackets(new S_IvenBuffIcon(_skillId, true, _skill.getSysmsgIdHappen(), _skillTime));
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
					break;

				case SOUL_BARRIER: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
							pc.removeSkillEffect(L1SkillId.SOUL_BARRIER);
							pc.sendPackets(S_InventoryIcon.icoReset(L1SkillId.SOUL_BARRIER, 600, true));
						} else
							pc.sendPackets(S_InventoryIcon.icoNew(L1SkillId.SOUL_BARRIER, 600, true));

						pc.setSkillEffect(L1SkillId.SOUL_BARRIER, 600 * 1000);
					}
				}
					break;
				case L1SkillId.CYCLONE:{
					L1PcInstance pc = (L1PcInstance) _user;
					if (_user.hasSkillEffect(L1SkillId.CYCLONE)) {
						_user.removeSkillEffect(L1SkillId.CYCLONE);
					}
					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.CYCLONE);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(9190);
					noti.set_off_icon_id(9192);
					noti.set_icon_priority(10);
					noti.set_tooltip_str_id(5446);
					noti.set_new_str_id(5446);
					noti.set_end_str_id(5450);
					noti.set_is_good(true);
					pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				}
				break;
				case FOCUS_SPRITS: {
					L1PcInstance pc = (L1PcInstance) _user;
					_user.add_magic_critical_rate(5);
					if (_user.hasSkillEffect(L1SkillId.FOCUS_SPRITS)) {
						_user.removeSkillEffect(L1SkillId.FOCUS_SPRITS);
					}
					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.FOCUS_SPRITS);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(4832);
					noti.set_off_icon_id(4832);
					noti.set_icon_priority(10);
					noti.set_tooltip_str_id(5272);
					noti.set_new_str_id(0);
					noti.set_end_str_id(5273);
					noti.set_is_good(true);
					pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					// _user.setSkillEffect(L1SkillId.FOCUS_SPRITS,
					// _skill.getBuffDuration() * 1000);
				}
					break;
				case FREEZEENG_ARMOR:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_user.hasSkillEffect(L1SkillId.FREEZEENG_ARMOR)) {
							_user.removeSkillEffect(L1SkillId.FREEZEENG_ARMOR);
						}
						
						pc.addEffectedER(5);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
						
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.FREEZEENG_ARMOR);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(9490);
						noti.set_off_icon_id(9490);
						noti.set_tooltip_str_id(5889);
						noti.set_new_str_id(5889);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
					break;
				// TODO 큐브 스킬 파티원 화면상에 있지 않을경우 들어가지 않게
				case CUBE_AVATAR: {
					L1PcInstance pc = (L1PcInstance) _user;
					L1Party party = pc.getParty();

					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.CUBE_AVATAR);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(5322);
					noti.set_off_icon_id(5322);
					noti.set_icon_priority(10);
					noti.set_tooltip_str_id(3073);
					noti.set_new_str_id(3073);
					noti.set_end_str_id(0);
					noti.set_is_good(true);
					ProtoOutputStream stream = ProtoOutputStream.newInstance(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);

					if (pc.hasSkillEffect(L1SkillId.CUBE_AVATAR)) {
						pc.removeSkillEffect(L1SkillId.CUBE_AVATAR);
					}
					pc.send_effect(_skill.getCastGfx());
					pc.sendPackets(stream, false);
					pc.setSkillEffect(L1SkillId.CUBE_AVATAR, _skill.getBuffDuration() * 1000);

					if (party != null) {
						for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
							if (pc.getParty().isMember(player)) {
								if (player.hasSkillEffect(L1SkillId.CUBE_AVATAR)) {
									player.removeSkillEffect(L1SkillId.CUBE_AVATAR);
								}
								player._CubeEffect = true;
								player.send_effect(_skill.getCastGfx());
								player.sendPackets(stream, false);
								player.setSkillEffect(L1SkillId.CUBE_AVATAR, _skill.getBuffDuration() * 1000);
							}
						}
					}

					stream.dispose();
				}
					break;
				case CUBE_RICH: {
					L1PcInstance pc = (L1PcInstance) _user;
					L1Party party = pc.getParty();

					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.CUBE_RICH);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(5309);
					noti.set_off_icon_id(5309);
					noti.set_icon_priority(10);
					noti.set_tooltip_str_id(1348);
					noti.set_new_str_id(1348);
					noti.set_end_str_id(0);
					noti.set_is_good(true);
					ProtoOutputStream stream = ProtoOutputStream.newInstance(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);

					if (pc.hasSkillEffect(L1SkillId.CUBE_RICH)) {
						pc.removeSkillEffect(L1SkillId.CUBE_RICH);
					}

					pc.send_effect(_skill.getCastGfx());
					pc.getAbility().addSp(2);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(stream, false);
					pc.setSkillEffect(L1SkillId.CUBE_RICH, _skill.getBuffDuration() * 1000);

					if (party != null) {
						for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
							if (pc.getParty().isMember(player)) {
								if (player.hasSkillEffect(L1SkillId.CUBE_RICH)) {
									player.removeSkillEffect(L1SkillId.CUBE_RICH);
								}
								player._CubeEffect = true;
								player.send_effect(_skill.getCastGfx());
								player.getAbility().addSp(2);
								player.sendPackets(new S_SPMR(player));
								player.sendPackets(stream, false);
								player.setSkillEffect(L1SkillId.CUBE_RICH, _skill.getBuffDuration() * 1000);
							}
						}
					}
					stream.dispose();
				}
					break;

				case CUBE_GOLEM: {
					L1PcInstance pc = (L1PcInstance) _user;
					L1Party party = pc.getParty();

					// 큐브 골렘
					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.CUBE_GOLEM);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(5314);
					noti.set_off_icon_id(5314);
					noti.set_icon_priority(10);
					noti.set_tooltip_str_id(3075);
					noti.set_new_str_id(3075);
					noti.set_end_str_id(0);
					noti.set_is_good(true);
					ProtoOutputStream stream = ProtoOutputStream.newInstance(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);

					if (pc.hasSkillEffect(L1SkillId.CUBE_GOLEM)) {
						pc.removeSkillEffect(L1SkillId.CUBE_GOLEM);
					}
					pc.send_effect(_skill.getCastGfx());
					pc.getAC().addAc(-8);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(stream, false);
					pc.setSkillEffect(L1SkillId.CUBE_GOLEM, _skill.getBuffDuration() * 1000);

					if (party != null) {
						for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
							if (pc.getParty().isMember(player)) {
								if (player.hasSkillEffect(L1SkillId.CUBE_GOLEM)) {
									player.removeSkillEffect(L1SkillId.CUBE_GOLEM);
								}
								player._CubeEffect = true;
								player.send_effect(_skill.getCastGfx());
								player.getAC().addAc(-8);
								player.sendPackets(new S_OwnCharAttrDef(player));
								player.sendPackets(stream, false);
								player.setSkillEffect(L1SkillId.CUBE_GOLEM, _skill.getBuffDuration() * 1000);
							}
						}
					}
					stream.dispose();
				}
					break;

				case CUBE_OGRE: {
					L1PcInstance pc = (L1PcInstance) _user;
					L1Party party = pc.getParty();

					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.CUBE_OGRE);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(5312);
					noti.set_off_icon_id(5312);
					noti.set_icon_priority(10);
					noti.set_tooltip_str_id(3074);
					noti.set_new_str_id(3074);
					noti.set_end_str_id(0);
					noti.set_is_good(true);
					ProtoOutputStream stream = ProtoOutputStream.newInstance(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI);

					if (pc.hasSkillEffect(L1SkillId.CUBE_OGRE)) {
						pc.removeSkillEffect(L1SkillId.CUBE_OGRE);
					}
					pc.addDmgup(4);
					pc.addHitup(4);
					pc.sendPackets(stream, false);
					pc.send_effect(_skill.getCastGfx());
					pc.setSkillEffect(L1SkillId.CUBE_OGRE, _skill.getBuffDuration() * 1000);

					if (party != null) {
						for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
							if (pc.getParty().isMember(player)) {
								if (player.hasSkillEffect(L1SkillId.CUBE_OGRE)) {
									player.removeSkillEffect(L1SkillId.CUBE_OGRE);
								}
								player._CubeEffect = true;
								player.addDmgup(4);
								player.addHitup(4);
								player.sendPackets(stream, false);
								player.send_effect(_skill.getCastGfx());
								player.setSkillEffect(L1SkillId.CUBE_OGRE, _skill.getBuffDuration() * 1000);
							}
						}
					}
					stream.dispose();
				}
					break;
					
				case IMPACT: {
					if (_user instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _user;
						L1Party party = pc.getParty();
						int impactUp = 5;
						int lvl = pc.getLevel();
						if (lvl >= 80)
							impactUp = IntRange.ensure(impactUp + (lvl - 79), 5, 10);

						ArrayList<L1PcInstance> members = new ArrayList<L1PcInstance>();
						members.add(pc);
						if (party != null) {
							for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
								if (party.isMember(player))
									members.add(player);
							}
						}

						for (int i = members.size() - 1; i >= 0; --i) {
							L1PcInstance member = members.get(i);
							if (member.hasSkillEffect(L1SkillId.IMPACT)) {
								member.removeSkillEffect(L1SkillId.IMPACT);
								member.sendPackets(S_InventoryIcon.icoReset(L1SkillId.IMPACT, _skill.getBuffDuration(), true));
							} else {
								member.sendPackets(S_InventoryIcon.icoNew(L1SkillId.IMPACT, _skill.getBuffDuration(), true));
							}

							member.setImpactUp(impactUp);
							member.addSpecialPierce(eKind.ALL, impactUp);
							member.setSkillEffect(L1SkillId.IMPACT, _skill.getBuffDuration() * 1000);
							SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(member);
							
							member.sendPackets(new S_SkillSound(member.getId(), 14513));
							Broadcaster.broadcastPacket(member, new S_SkillSound(member.getId(), 14513));
						}
					}
				}
					break;

				case LUCIFER: {
					if (_user instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _user;
						if (pc.hasSkillEffect(L1SkillId.LUCIFER)) {
							pc.removeSkillEffect(L1SkillId.LUCIFER);
						} else if (pc.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
							pc.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
						}

						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.LUCIFER);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(4503);
						noti.set_off_icon_id(4503);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(5268);
						noti.set_new_str_id(5268);
						noti.set_end_str_id(5269);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
				}
					break;
				case BLOW_ATTACK: {
					if (_user instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _user;
						if (pc.hasSkillEffect(L1SkillId.BLOW_ATTACK)) {
							pc.removeSkillEffect(L1SkillId.BLOW_ATTACK);
						}
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.BLOW_ATTACK);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(8843);
						noti.set_off_icon_id(8843);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(5266);
						noti.set_new_str_id(5266);
						noti.set_end_str_id(5267);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
				}
					break;
				case TITANL_RISING: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.TITANL_RISING)) {
							pc.removeSkillEffect(L1SkillId.TITANL_RISING);
							pc.sendPackets(S_InventoryIcon.icoReset(L1SkillId.TITANL_RISING, 2400, true));
						} else
							pc.sendPackets(S_InventoryIcon.icoNew(L1SkillId.TITANL_RISING, 2400, true));
						pc.setSkillEffect(L1SkillId.TITANL_RISING, 2400 * 1000);
						int upHP = pc.getLevel() - 80;
						if (upHP >= 5)
							upHP = 5;
						pc.setRisingUp(5 + upHP);
					}
				}
					break;
				case ABSOLUTE_BLADE: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.ABSOLUTE_BLADE)) {
							pc.removeSkillEffect(L1SkillId.ABSOLUTE_BLADE);
							pc.sendPackets(S_InventoryIcon.icoReset(L1SkillId.ABSOLUTE_BLADE, 32, true));
						} else
							pc.sendPackets(S_InventoryIcon.icoNew(L1SkillId.ABSOLUTE_BLADE, 32, true));
						pc.setSkillEffect(L1SkillId.ABSOLUTE_BLADE, 32 * 1000);
					}
				}
					break;
				case DEATH_HEAL_Mob:
				case DEATH_HEAL: {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						int chance = random.nextInt(10) + 1;
						if (pc.hasSkillEffect(DEATH_HEAL)) {
							pc.sendPackets(S_InventoryIcon.icoReset(DEATH_HEAL, chance, false));
							pc.removeSkillEffect(DEATH_HEAL);
						} else
							pc.sendPackets(S_InventoryIcon.icoNew(DEATH_HEAL, chance, false));
						pc.setSkillEffect(DEATH_HEAL, chance * 1000);
						pc.sendPackets(new S_SkillSound(pc.getId(), 14501));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 14501));
					}
				}
					break;
				case GRACE_AVATAR: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.GRACE_AVATAR)) 
							pc.removeSkillEffect(L1SkillId.GRACE_AVATAR);
						
						pc.setGraceLv(pc.getLevel());

						int resistance = 5 + pc.getGraceLv();
						pc.addSpecialResistance(eKind.ALL, resistance);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						pc.setSkillEffect(L1SkillId.GRACE_AVATAR, _getBuffIconDuration * 1000);
						pc.sendPackets(new S_SkillSound(pc.getId(), 14495));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 14495));
						pc.sendPackets(S_InventoryIcon.icoReset(GRACE_AVATAR, 60, true));
						for (L1PcInstance player : L1World.getInstance().getVisiblePlayer(pc, 18)) {
							if (pc.getParty() != null) {
								if (pc.getParty().isMember(player) && player != null) {
									if (player.hasSkillEffect(L1SkillId.GRACE_AVATAR))
										player.removeSkillEffect(L1SkillId.GRACE_AVATAR);
									
									player.sendPackets(S_InventoryIcon.icoReset(GRACE_AVATAR, 60, true));
									player.sendPackets(new S_SkillSound(player.getId(), 14495));
									Broadcaster.broadcastPacket(player, new S_SkillSound(player.getId(), 14495));
									player.setGraceLv(pc.getLevel());
									player.addSpecialResistance(eKind.ALL, resistance);
									SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(player);
									player.setSkillEffect(L1SkillId.GRACE_AVATAR, _getBuffIconDuration * 1000);
									player.sendPackets(new S_ServerMessage(4734));
								}
							}
						}
					}
				}
					break;
					
				/** 기르타스 관련 */
				case TROGIR_MILPITAS3: {
					S_Sound.broadcast(cha, 11002);
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case TROGIR_MILPITAS4: {
					S_Sound.broadcast(cha, 11003);
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case TROGIR_MILPITAS5: {
					S_Sound.broadcast(cha, 11004);
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case TROGIR_MILPITAS6: {
					S_Sound.broadcast(cha, 11008);
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case TROGIR_MILPITAS2: {
					S_Sound.broadcast(cha, 11009);
					S_Sound.broadcast(cha, 11006);
					S_SkillSound.broadcast(cha, 11473);
					if (cha instanceof L1PcInstance) {
						L1SpawnUtil.spawn((L1PcInstance) cha, 181163, 5, 6 * 2000, true);
					}
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case IMMUNE_TO_HARM:
					if (_target.hasSkillEffect(L1SkillId.LUCIFER)) {
						_target.removeSkillEffect(L1SkillId.LUCIFER);
					} else if (_target.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
						_target.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
					}

					if (_target != null) {
						if (_skill.getType() == L1SkillUse.TYPE_GMBUFF)
							((L1PcInstance) _target).setLastImmuneLevel(_player.getLevel());
						if (_player != null) {
							if (_player.getId() == _target.getId())
								((L1PcInstance) _target).setLastImmuneLevel(Config.IMMUNE_Level);
							else
								((L1PcInstance) _target).setLastImmuneLevel(_player.getLevel());
						}
					} // 일부러 break 걸지 않음. pink name 설정 해줘야 하기 때문.
					if (_target instanceof L1PcInstance) {
						L1PcInstance targetPc = (L1PcInstance) _target;
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.IMMUNE_TO_HARM);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(1562);
						noti.set_icon_priority(10);
						noti.set_off_icon_id(1562);
						noti.set_tooltip_str_id(966);
						noti.set_new_str_id(314);
						noti.set_end_str_id(315);
						noti.set_is_good(true);
						targetPc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
				case IllUSION_OGRE:

				case IllUSION_LICH:
				case IllUSION_DIAMONDGOLEM:
				case CONCENTRATION:
				case PATIENCE: {
					if (_user instanceof L1PcInstance) {
						L1PinkName.onHelp(cha, _user);
					}
				}
					break;
				case Phoenix_Skill1: {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillSound(pc.getId(), 16258));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 16258));
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case Phoenix_Skill2: {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillSound(pc.getId(), 16299));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 16299));
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case DRAKE_Skill1: {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillSound(pc.getId(), 16333));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 16333));
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case DRAKE_Skill2: {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SkillSound(pc.getId(), 16335));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 16335));
					dmg = _magic.calcMagicDamage(_skillId);
				}
					break;
				case DRAGON_HUNTER_BLESS: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(DRAGON_HUNTER_BLESS)) {
							pc.sendPackets(S_InventoryIcon.icoReset(DRAGON_HUNTER_BLESS, 5016, 1800L, true));
						} else {
							pc.sendPackets(S_InventoryIcon.icoNew(DRAGON_HUNTER_BLESS, 5016, 1800L, true));
						}
					}
				}
					break;
				case miso1: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(miso1)) {
							pc.sendPackets(S_InventoryIcon.icoReset(miso1, 4995, 1800L, true));
						} else {
							pc.sendPackets(S_InventoryIcon.icoNew(miso1, 4995, 1800L, true));
						}
					}
				}
					break;
				case miso2: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(miso2)) {
							pc.sendPackets(S_InventoryIcon.icoReset(miso2, 4996, 1800L, true));
						} else {
							pc.addMaxHp(100);
							pc.getResistance().addMr(10);
							pc.addHpr(2);
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(S_InventoryIcon.icoNew(miso2, 4996, 1800L, true));
						}
					}
				}
				break;
				case miso3: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(miso3)) {
							pc.sendPackets(S_InventoryIcon.icoReset(miso3, 4997, 1800L, true));
						} else {
							pc.addDmgup(3);
							pc.addBowDmgup(3);
							pc.addMaxMp(50);
							pc.getAbility().addSp(3);
							pc.addMpr(2);
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(S_InventoryIcon.icoNew(miso3, 4997, 1800L, true));
						}
					}
				}
					break;
				case DRAGON_SET: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(DRAGON_SET)) {
							pc.sendPackets(S_InventoryIcon.icoReset(DRAGON_SET, 5016, 600L, true));
						} else {
							pc.addDmgup(3);
							pc.addDmgRate(3);
							pc.addBowDmgup(3);
							pc.addBowHitup(3);
							pc.getAC().addAc(-3);
							pc.getAbility().addSp(2);
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(S_InventoryIcon.icoNew(DRAGON_SET, 5016, 600L, true));
						}
					}
				}
					break;
				case HUNTER_BLESS: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(HUNTER_BLESS)) {
							pc.sendPackets(S_InventoryIcon.icoReset(HUNTER_BLESS, 4992, 1800L, true));
						} else {
							pc.sendPackets(S_InventoryIcon.icoNew(HUNTER_BLESS, 4992, 1800L, true));
						}
					}
				}
					break;
				case HUNTER_BLESS2: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(HUNTER_BLESS2)) {
							pc.sendPackets(S_InventoryIcon.icoReset(HUNTER_BLESS2, 993, 1800L, true));
						} else {
							pc.sendPackets(S_InventoryIcon.icoNew(HUNTER_BLESS2, 993, 1800L, true));
						}
					}
				}
					break;
				case HASTE: {
					if (cha.getMoveSpeed() != 2) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getHasteItemEquipped() > 0) {
								continue;
							}
							pc.setDrink(false);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration));
						}
						cha.broadcastPacket(new S_SkillHaste(cha.getId(), 1, 0));
						cha.setMoveSpeed(1);
					} else {
						int skillNum = 0;
						if (cha.hasSkillEffect(SLOW)) {
							skillNum = SLOW;
						} else if (cha.hasSkillEffect(MOB_SLOW_1)) {
							skillNum = MOB_SLOW_1;
						} else if (cha.hasSkillEffect(MOB_SLOW_18)) {
							skillNum = MOB_SLOW_18;
						}
						if (skillNum != 0) {
							cha.removeSkillEffect(skillNum);
							cha.removeSkillEffect(HASTE);
							cha.setMoveSpeed(0);
							continue;
						}
					}
				}
					break;
				case CURE_POISON: {
					cha.curePoison();
				}
					break;
				case DRESS_EVASION:{
					L1PcInstance pc = (L1PcInstance) cha;
					if (_user.hasSkillEffect(L1SkillId.DRESS_EVASION)) {
						_user.removeSkillEffect(L1SkillId.DRESS_EVASION);
					}
					
					pc.addEffectedER(18);
					pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
					
					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.DRESS_EVASION);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(1608);
					noti.set_off_icon_id(1608);
					noti.set_tooltip_str_id(970);
					noti.set_new_str_id(970);
					noti.set_end_str_id(2176);
					noti.set_is_good(true);
					pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				}
				break;
				case AQUA_PROTECTER:{
					L1PcInstance pc = (L1PcInstance) cha;
					if (_user.hasSkillEffect(L1SkillId.AQUA_PROTECTER)) {
						_user.removeSkillEffect(L1SkillId.AQUA_PROTECTER);
					}
					
					pc.addEffectedER(5);
					pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
					
					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.AQUA_PROTECTER);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(2342);
					noti.set_off_icon_id(2342);
					noti.set_tooltip_str_id(1085);
					noti.set_new_str_id(1085);
					noti.set_end_str_id(2201);
					noti.set_is_good(true);
					pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				}
				break;
				case SOLID_CARRIAGE:{
					L1PcInstance pc = (L1PcInstance) cha;
					if (_user.hasSkillEffect(L1SkillId.SOLID_CARRIAGE)) {
						_user.removeSkillEffect(L1SkillId.SOLID_CARRIAGE);
					}
					
					pc.addEffectedER(15);
					pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
					
					SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
					noti.set_noti_type(eNotiType.RESTAT);
					noti.set_spell_id(L1SkillId.SOLID_CARRIAGE);
					noti.set_duration(_getBuffIconDuration);
					noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					noti.set_on_icon_id(2351);
					noti.set_off_icon_id(2351);
					noti.set_tooltip_str_id(1087);
					noti.set_new_str_id(1087);
					noti.set_end_str_id(2203);
					noti.set_is_good(true);
					pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				}
				break;
				case STRIKER_GALE:// 게일 실시간
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;

						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.STRIKER_GALE);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(2357);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(1084);
						noti.set_new_str_id(1084);
						noti.set_end_str_id(2200);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
					break; // 수정
				case REMOVE_CURSE: {
					cha.curePoison();
					if (cha.hasSkillEffect(STATUS_CURSE_PARALYZING) || cha.hasSkillEffect(STATUS_CURSE_PARALYZED) || cha.hasSkillEffect(ANTA_MESSAGE_1)
							|| cha.hasSkillEffect(ANTA_MESSAGE_6) || cha.hasSkillEffect(ANTA_MESSAGE_7) || cha.hasSkillEffect(ANTA_MESSAGE_8)
							|| cha.hasSkillEffect(OMAN_STUN) || cha.hasSkillEffect(Maeno_STUN)) {
						cha.cureParalaysis();
					}
					if (cha.hasSkillEffect(CURSE_BLIND) || cha.hasSkillEffect(DARKNESS)) {
						if (cha.hasSkillEffect(CURSE_BLIND)) {
							cha.removeSkillEffect(CURSE_BLIND);
						} else if (cha.hasSkillEffect(DARKNESS)) {
							cha.removeSkillEffect(DARKNESS);
						}
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_CurseBlind(0));
						}
					}
				}
					break;
				case RESURRECTION:
				case GREATER_RESURRECTION: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_player.getId() != pc.getId()) {
							if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592));
										return;
									}
								}
							}
							/** 공성장에서는 부활불가능하도록 **/
							int castle_id = L1CastleLocation.getCastleIdByArea(pc);
							if (castle_id != 0) {
								pc.sendPackets(new S_SystemMessage("사용할 수 없는 지역입니다."));
								return;
							}
							/** 공성장에서는 부활불가능하도록 **/
							if (pc.getCurrentHp() == 0 && pc.isDead()) {
								if (pc.getMap().isUseResurrection()) {
									if (_skillId == RESURRECTION) {
										pc.setGres(false);
									} else if (_skillId == GREATER_RESURRECTION) {
										pc.setGres(true);
									}
									pc.setTempID(_player.getId());
									pc.sendPackets(new S_Message_YN(322, ""));
								}
							}
						}
					}
					if (cha instanceof L1NpcInstance) {
						if (!(cha instanceof L1TowerInstance)) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							if (npc.getNpcTemplate().isCantResurrect() && !(npc instanceof L1PetInstance) && !(npc instanceof MJCompanionInstance)) {
								return;
							}
							if ((npc instanceof MJCompanionInstance || npc instanceof L1PetInstance) && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592));
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
					break;
				case CALL_OF_NATURE: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_player.getId() != pc.getId()) {
							if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592));
										return;
									}
								}
							}
							if (pc.getCurrentHp() == 0 && pc.isDead()) {
								pc.setTempID(_player.getId());
								pc.sendPackets(new S_Message_YN(322, ""));
							}
						}
					}
					if (cha instanceof L1NpcInstance) {
						if (!(cha instanceof L1TowerInstance)) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							if ((npc instanceof L1PetInstance || npc instanceof MJCompanionInstance) && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592));
										return;
									}
								}
							}
							if (npc.getCurrentHp() == 0 && npc.isDead()) {
								npc.resurrect(cha.getMaxHp());
								npc.resurrect(cha.getMaxMp() / 100);
								npc.setResurrect(true);
							}
						}
					}
				}
					break;
				// UI DG표시
				case UNCANNY_DODGE: // 언케니닷지
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDg(30);
					}
					break;
				case FEAR:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDg(-25);
					}
					break;
				case DETECTION:
				case IZE_BREAK:
				case EYE_OF_DRAGON: {
					if (cha instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						int hiddenStatus = npc.getHiddenStatus();
						if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
							npc.appearOnGround(_player);
						}
					}
				}
					break;
				case COUNTER_DETECTION: {
					if (cha instanceof L1PcInstance) {
						dmg = _magic.calcMagicDamage(_skillId);
					} else if (cha instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						int hiddenStatus = npc.getHiddenStatus();
						if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
							npc.appearOnGround(_player);
						} else {
							dmg = 0;
						}
					} else {
						dmg = 0;
					}
				}
					break;
				case MIND_BREAK: {
					if (_target.getCurrentMp() >= 5) {
						_target.setCurrentMp(_target.getCurrentMp() - 5);
						dmg = 15;
					} else {
						return;
					}
				}
					break;
				case TRUE_TARGET: {
					if (_user instanceof L1PcInstance) {
						L1PcInstance pri = (L1PcInstance) _user;
						pri.sendPackets(new S_TrueTargetNew(_targetID, true));
						if (_target instanceof L1PcInstance) {
							int step = pri.getLevel() / 15;
							L1PcInstance target = (L1PcInstance) _target;
							if (step > 0) {
								target.set트루타켓(step);
							}
						}
						for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(_target)) {
							if (pri.getClanid() == pc.getClanid()) {
								pc.sendPackets(new S_TrueTargetNew(_targetID, true));
							}
						}
						// 이전에 시전한 트루타겟 찾아서 강제 종료 시키기.
						synchronized (_truetarget_list) {
							L1Object temp = _truetarget_list.remove(_user.getId());
							if (temp != null && temp instanceof L1Character) {
								L1Character temp2 = (L1Character) temp;
								temp2.removeSkillEffect(L1SkillId.TRUE_TARGET);
							}
						}
						// 트루타겟 활성화.
						_target.setSkillEffect(L1SkillId.TRUE_TARGET, 16 * 1000);
						synchronized (_truetarget_list) {
							_truetarget_list.put(_user.getId(), _target);
						}
					}
				}
					break;
				case MOEBIUS:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_user.hasSkillEffect(L1SkillId.MOEBIUS)) {
							_user.removeSkillEffect(L1SkillId.MOEBIUS);
						}

						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.MOEBIUS);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(9443);
						noti.set_off_icon_id(9443);
						noti.set_tooltip_str_id(5550);
						noti.set_new_str_id(5550);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
					break;
				case ELEMENTAL_FALL_DOWN: {
					if (_user instanceof L1PcInstance) {
						int playerAttr = _player.getElfAttr();
						int i = -50;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							switch (playerAttr) {
							case 0:
								_player.sendPackets(new S_ServerMessage(79));
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
						} else if (cha instanceof L1MonsterInstance) {
							L1MonsterInstance mob = (L1MonsterInstance) cha;
							switch (playerAttr) {
							case 0:
								_player.sendPackets(new S_ServerMessage(79));
								break;
							case 1:
								mob.getResistance().addEarth(i);
								mob.setAddAttrKind(1);
								break;
							case 2:
								mob.getResistance().addFire(i);
								mob.setAddAttrKind(2);
								break;
							case 4:
								mob.getResistance().addWater(i);
								mob.setAddAttrKind(4);
								break;
							case 8:
								mob.getResistance().addWind(i);
								mob.setAddAttrKind(8);
								break;
							default:
								break;
							}
						}
					}
				}
					break;
				case HEAL:
				case EXTRA_HEAL:
				case GREATER_HEAL:
				case FULL_HEAL:
				case HEAL_ALL:
				case NATURES_TOUCH:
				case NATURES_BLESSING: {
					if (cha instanceof L1PcInstance) {
						cha.killSkillEffectTimer(WATER_LIFE);
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_PacketBox(S_PacketBox.DEL_ICON));
					}
				}
					break;
				case CHILL_TOUCH:
				case VAMPIRIC_TOUCH: {
					heal = dmg / 2;
				}
					break;
				case TRIPLE_ARROW: {// 트리플
					int playerGFX = _player.getCurrentSpriteId();
					if (_player.getWeapon() == null)
						break;
					int weaponType = _player.getWeapon().getItem().getType1();
					if (weaponType != 20 || playerGFX == 3784)
						return;

					_player.TripleArrow = true;
					for (int i = 3; i > 0; i--) {
						_target.onAction(_player);
					}
					_player.TripleArrow = false;
					_player.sendPackets(new S_SkillSound(_player.getId(), 15103));// 11764
					Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 15103));
				}
					break;
				case MOB_TRIPLE_ARROW:{
					if(_user.getLocation().getTileLineDistance(_target.getLocation()) > 15){
						return;
					}
					if (_user instanceof L1NpcInstance) {
						L1NpcInstance _NPC = (L1NpcInstance) _user;
						for (int i = 3; i > 0; i--) {
							_NPC.attackTarget(_target);
						}
					}
					_user.broadcastPacket(new S_SkillSound(_user.getId(), 15103));//11764
				}
				break;
					
				case Sand_worms: { // 샌드웜 이럽션
					L1PcInstance pc = (L1PcInstance) _player;
					S_UseAttackSkill packet = new S_UseAttackSkill(_user, _player.getId(), 10145, _player.getX(), _player.getY(), ActionCodes.ACTION_Attack,
							false);
					Broadcaster.broadcastPacket(_user, packet);
					Broadcaster.broadcastPacket(pc, packet);
					pc.sendPackets(new S_SkillSound(pc.getId(), 10145));
				}
					break;
				case Sand_worms1: { // 샌드웜 범위공격1
					L1PcInstance pc = (L1PcInstance) _player;
					S_UseAttackSkill packet = new S_UseAttackSkill(_user, _player.getId(), 10195, _player.getX(), _player.getY(), ActionCodes.ACTION_Attack,
							false);
					Broadcaster.broadcastPacket(_user, packet);
					Broadcaster.broadcastPacket(pc, packet);
					pc.sendPackets(new S_SkillSound(pc.getId(), 10195));
				}
					break;
				case Sand_worms2: { // 샌드웜 범위공격1
					L1PcInstance pc = (L1PcInstance) _player;
					S_UseAttackSkill packet = new S_UseAttackSkill(_user, _player.getId(), 10194, _player.getX(), _player.getY(), ActionCodes.ACTION_Attack,
							false);
					Broadcaster.broadcastPacket(_user, packet);
					Broadcaster.broadcastPacket(pc, packet);
					pc.sendPackets(new S_SkillSound(pc.getId(), 10194));
				}
					break;
				case Sand_worms3: { // 샌드웜 범위공격1
					L1PcInstance pc = (L1PcInstance) _player;
					S_UseAttackSkill packet = new S_UseAttackSkill(_user, _player.getId(), 10191, _player.getX(), _player.getY(), ActionCodes.ACTION_Attack,
							false);
					Broadcaster.broadcastPacket(_user, packet);
					Broadcaster.broadcastPacket(pc, packet);
					pc.sendPackets(new S_SkillSound(pc.getId(), 10191));
				}
					break;
				case FOU_SLAYER: { // 포우슬레이어
					if (_player.getWeapon() == null) {
						return;
					}
					int weapontype = _player.getWeapon().getItem().getType1();
					if (weapontype != 4 && weapontype != 11 && weapontype != 24 && weapontype != 50) {
						return;
					}
					_player.FouSlayer = true;
					for (int i = 3; i > 0; i--) {
						_target.onAction(_player);
					}
					_player.FouSlayer = false;
					_player.sendPackets(new S_SkillSound(_player.getId(), 7020));// 추가함

					int effect = 6509;
					if (_player.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt()))
						effect = 17231;
					_target.send_effect(effect);
					if (_player.hasSkillEffect(CHAINSWORD1)) {// 포우슬레이어
						dmg += 0.5;
						_player.killSkillEffectTimer(CHAINSWORD1);
						_player.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0)); // 추가
					}
					if (_player.hasSkillEffect(CHAINSWORD2)) {
						dmg += 1.5;
						_player.killSkillEffectTimer(CHAINSWORD2);
						_player.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0)); // 추가
					}
					if (_player.hasSkillEffect(CHAINSWORD3)) {
						_player.killSkillEffectTimer(CHAINSWORD3);
						_player.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0)); // 추가
						dmg += 3;
					}
					if (_player.hasSkillEffect(CHAINSWORD4)) {
						_player.killSkillEffectTimer(CHAINSWORD4);
						_player.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 0)); // 추가
						dmg += 5;
					}
					Object[] dollList1 = _player.getDollList().values().toArray();
					for (Object dollObject : dollList1) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						dmg += doll.fou_DamageUp();
					}
				}
					break;
				/** 혈맹버프 **/
				case CLAN_BUFF1: {// 일반 공격 태세
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgupByArmor(2);
					pc.addBowDmgupByArmor(2);
					pc.sendPackets(new S_ACTION_UI2(2724, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7233, 4650));
					pc.sendPackets(new S_ServerMessage(4618, "$22503"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
				}
					break;
				case CLAN_BUFF2: {// 일반 방어 태세
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(-3);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_ACTION_UI2(2725, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7235, 4651));
					pc.sendPackets(new S_ServerMessage(4618, "$22504"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
				}
					break;
				case CLAN_BUFF3: {// 전투 공격 태세
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_ACTION_UI2(2726, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7237, 4652));
					pc.sendPackets(new S_ServerMessage(4618, "$22505"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
				}
					break;
				case CLAN_BUFF4: {// 전투 방어 태세
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_ACTION_UI2(2727, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7239, 4653));
					pc.sendPackets(new S_ServerMessage(4618, "$22506"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
				}
					break;
				case L1SkillId.MOB_BERSERKERS:{
					if(_user instanceof L1NpcInstance){
						_user.setMoveSpeed(1);
						_user.setBraveSpeed(1);
					}
				}
					break;
					
				case 10026:
				case 10027:
				case 10028:
				case 10029: {
					if (_user instanceof L1NpcInstance) {
						_user.broadcastPacket(new S_NpcChatPacket(_npc, "$3717", 0));
					} else {
						_player.broadcastPacket(new S_ChatPacket(_player, "$3717", 0, 0));
					}
					dmg = cha.getCurrentHp();
				}
					break;
				case 10057: {
					L1Teleport.getInstance().teleportToTargetFront(cha, _user, 5, true);// 5칸앞소환
				}
					break;

				case SLOW:
				case MOB_SLOW_1:
				case MOB_SLOW_18: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getHasteItemEquipped() > 0) {
							continue;
						}

					}
					if (cha.getMoveSpeed() == 0) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillHaste(pc.getId(), 2, _getBuffIconDuration));
						}
						cha.broadcastPacket(new S_SkillHaste(cha.getId(), 2, _getBuffIconDuration));
						cha.setMoveSpeed(2);
					} else if (cha.getMoveSpeed() == 1) {
						int skillNum = 0;
						if (cha.hasSkillEffect(HASTE)) {
							skillNum = HASTE;
						} else if (cha.hasSkillEffect(GREATER_HASTE)) {
							skillNum = GREATER_HASTE;
						} else if (cha.hasSkillEffect(STATUS_HASTE)) {
							skillNum = STATUS_HASTE;
						}
						if (skillNum != 0) {
							cha.removeSkillEffect(skillNum);
							cha.killSkillEffectTimer(skillNum);
							cha.removeSkillEffect(_skillId);
							if (cha instanceof L1PcInstance)
								((L1PcInstance) cha).sendPackets(new S_SkillHaste(cha.getId(), 1, 0));
							cha.setMoveSpeed(0);
							continue;
						}
					}
				}
					break;
				case CURSE_BLIND:
				case DARKNESS: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
							pc.sendPackets(new S_CurseBlind(2));
						} else {
							pc.sendPackets(new S_CurseBlind(1));
						}
					}
				}
					break;
				case DARK_BLIND:// 쉐도우 슬립
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
					}
					cha.setSleeped(true);
					break;
				case CURSE_POISON:
				case TROGIR_MILPITAS1:
					L1DamagePoison.doInfection(_user, cha, 3000, 5, false);
					break;
				case TOMAHAWK: // 토마호크 지속 시간 동안 출혈 상태가 되어 대미지를 입는다. 레벨*2/6
					L1DamagePoison.doInfection(_user, cha, 1000, _user.getLevel() * 2 / 6, true);
					break;
				case CURSE_PARALYZE:
				case CURSE_PARALYZE2:
				case MOB_CURSEPARALYZ1:
				case MOB_CURSEPARALYZ_18:
				case MOB_CURSEPARALYZ_19: {
					L1CurseParalysis.curse(cha, _skillId);
				}
					break;
				case WEAKNESS:
				case MOB_WEAKNESS_1: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(-5);
						pc.addHitup(-1);
					}
				}
					break;
				case DISEASE:
				case MOB_DISEASE_1:
				case MOB_DISEASE_30: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(-6);
						pc.getAC().addAc(12);
					}
				}
					break;
				case GUARD_BREAK: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(10);
					}
				}
					break;
				case HORROR_OF_DEATH: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) -3);
						pc.getAbility().addAddedInt((byte) -3);
					}
				}
					break;
				case PANIC: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) -1);
						pc.getAbility().addAddedDex((byte) -1);
						pc.getAbility().addAddedCon((byte) -1);
						pc.getAbility().addAddedInt((byte) -1);
						pc.getAbility().addAddedWis((byte) -1);
						pc.getAbility().addAddedCha((byte) -1);
						pc.resetBaseMr();
					}
				}
					break;
				case ICE_LANCE: {
					_isFreeze = _magic.calcProbabilityMagic(_skillId);
					if (_isFreeze) {
						int time = _skill.getBuffDuration() * 1000;
						L1EffectSpawn.getInstance().spawnEffect(81168, time, cha.getX(), cha.getY(), cha.getMapId());
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Poison(pc.getId(), 2));
							pc.broadcastPacket(new S_Poison(pc.getId(), 2));
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.broadcastPacket(new S_Poison(npc.getId(), 2));
							npc.setParalyzed(true);
						}
					}
				}
					break;
				/** 어바지속시간 본섭화 **/
				case EARTH_BIND: {
					int[] ebTimeArray = { 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000 };
					_earthBindDuration = ebTimeArray[random.nextInt(ebTimeArray.length)];
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;

						// int rnd = random.nextInt(ebTimeArray.length);
						// _earthBindDuration = ebTimeArray[rnd];

						pc.sendPackets(new S_Poison(pc.getId(), 2));
						pc.broadcastPacket(new S_Poison(pc.getId(), 2));
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.broadcastPacket(new S_Poison(npc.getId(), 2));
						npc.setParalyzed(true);
						npc.setParalysisTime(_earthBindDuration);
					}
				}
					break;
					
				case MOB_BASILL:// 바실커스
				case MOB_COCA: {
					if(!cha.hasSkillEffect(MOB_COCA) || !cha.hasSkillEffect(MOB_BASILL))
						L1CurseParalysis.curse(cha, _skillId);
					
					/*if (cha instanceof L1PcInstance) {
						
						L1PcInstance pc = (L1PcInstance) cha;
						if (cha.hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING) || cha.hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED)) {
							pc.cureParalaysis();
						}
						pc.sendPackets(new S_Poison(pc.getId(), 2));
						pc.broadcastPacket(new S_Poison(pc.getId(), 2));
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.broadcastPacket(new S_Poison(npc.getId(), 2));
						npc.setParalyzed(true);
					}*/
				}
					break;
				case ARMOR_BRAKE: {// 아머브레이크
					
					cha.setArmorBrakeAttackerID(_user.getId());
					break;
				}
				case SHOCK_STUN: {// 스턴확률
					int targetLevel = 0;
					int diffLevel = 0;

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						targetLevel = pc.getLevel();
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						targetLevel = npc.getLevel();
					}

					diffLevel = _user.getLevel() - targetLevel;

					// TODO 쇼크스턴 시간 1~6초 최대치 사이사이 시간초를 중복으로 넣기
					if (diffLevel < -5) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 1000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= -5 && diffLevel <= -3) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 1000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= -2 && diffLevel <= 2) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 3000, 2000, 3000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= 3 && diffLevel <= 5) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 3000, 2000, 3000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= 5 && diffLevel <= 10) {
						int[] stunTimeArray = { 1000, 2000, 1000, 3000, 2000, 4000, 3000, 5000, 4000, 6000, 5000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel > 10) {
						int[] stunTimeArray = { 1000, 2000, 1000, 3000, 2000, 4000, 3000, 5000, 4000, 6000, 5000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					}
					L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
						npc.setParalysisTime(_shockStunDuration);
					}
				}
					break;
				case ENCHANT_ACURUCY:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_user.hasSkillEffect(L1SkillId.ENCHANT_ACURUCY)) {
							_user.removeSkillEffect(L1SkillId.ENCHANT_ACURUCY);
						}
						
						pc.addHitup(5);
						
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.ENCHANT_ACURUCY);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(9487);
						noti.set_off_icon_id(9487);
						noti.set_tooltip_str_id(5888);
						noti.set_new_str_id(5888);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				}
					break;
				case EMPIRE:{
					int targetLevel = 0;
					int diffLevel = 0;

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						targetLevel = pc.getLevel();
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						targetLevel = npc.getLevel();
					}

					diffLevel = _user.getLevel() - targetLevel;

					// TODO 쇼크스턴 시간 1~6초 최대치 사이사이 시간초를 중복으로 넣기
					if (diffLevel < -5) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 1000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= -5 && diffLevel <= -3) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 1000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= -2 && diffLevel <= 2) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 3000, 2000, 3000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= 3 && diffLevel <= 5) {
						int[] stunTimeArray = { 1000, 2000, 1000, 2000, 3000, 2000, 3000, 2000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel >= 5 && diffLevel <= 10) {
						int[] stunTimeArray = { 1000, 2000, 1000, 3000, 2000, 4000, 3000, 5000, 4000, 6000, 5000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					} else if (diffLevel > 10) {
						int[] stunTimeArray = { 1000, 2000, 1000, 3000, 2000, 4000, 3000, 5000, 4000, 6000, 5000 };
						_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
					}
					L1EffectInstance eff = L1EffectSpawn.getInstance().spawnEffect(50000066, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
						npc.setParalysisTime(_shockStunDuration);
					}
				}
				break;
				case MOB_RANGESTUN_18:
				case MOB_RANGESTUN_19:
				case MOB_SHOCKSTUN_30: {

					int levelDiff = _user.getLevel() - cha.getLevel();
					int duration = 2250 + levelDiff * 80;

					duration += random.nextInt(1600) - 800;

					if (duration < 1000) {
						duration = 1000;
					} else if (duration > 5000) {
						duration = 5000;
					}

					_shockStunDuration = duration;

					L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
					}
				}
					break;
				case THUNDER_GRAB: {
					_isFreeze = _magic.calcProbabilityMagic(_skillId);
					if (_isFreeze) {
						int[] grabTime = null;
						if (_player != null && _player.isPassive(MJPassiveID.THUNDER_GRAP_BRAVE.toInt())) {
							grabTime = new int[] { 3000, 4000, 4500, 5000, 5500, 6000 };
						} else {
							grabTime = new int[] { 1000, 2000, 3000, 4000 };
						}
						int rnd = random.nextInt(grabTime.length);
						int time = grabTime[rnd]; // 시간 랜덤을 위해
						L1EffectSpawn.getInstance().spawnEffect(81182, time, cha.getX(), cha.getY(), cha.getMapId());
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.setSkillEffect(L1SkillId.STATUS_FREEZE, time);
							pc.sendPackets(new S_SkillSound(pc.getId(), 4184));
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 4184));
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.broadcastPacket(new S_SkillSound(npc.getId(), 4184));
							npc.set발묶임상태(true);
						}
					}
				}
					break;
				case BONE_BREAK: {
					int bonetime = 2000;
					L1EffectSpawn.getInstance().spawnEffect(200020, bonetime, cha.getX(), cha.getY(), cha.getMapId());
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
					}
				}
					break;
				case PHANTASM: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
					}
					cha.setSleeped(true);
				}
					break;
				case WIND_SHACKLE:
				case MOB_WINDSHACKLE_1: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
						pc.setSkillEffect(pc.getId(), _getBuffIconDuration);
					}
				}
					break;
				case CANCELLATION: {
					try { // for test
						if (cha instanceof L1PcInstance) {
							((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), _skill.getCastGfx()));
						}
						cha.broadcastPacket(new S_SkillSound(cha.getId(), _skill.getCastGfx()));

						if (cha instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							int npcId = npc.getNpcTemplate().get_npcId();
							if (npcId == 71092) {
								if (!npc.equalsCurrentSprite(1314)) {
									npc.setCurrentSprite(1314);
									npc.sendShape(1314);
									return;
								} else {
									return;
								}
							}
							if (npcId == 45640) {
								if (npc.equalsCurrentSprite(2755)) {
									npc.setCurrentHp(npc.getMaxHp());
									npc.setCurrentSprite(2332);
									npc.sendShape(2332);
									npc.setName("$2103");
									npc.setNameId("$2103");
									npc.broadcastPacket(new S_ChangeName(npc.getId(), "$2103"));
								} else {
									npc.setCurrentHp(npc.getMaxHp());
									npc.setCurrentSprite(2755);
									npc.sendShape(2755);
									npc.setName("$2488");
									npc.setNameId("$2488");
									npc.broadcastPacket(new S_ChangeName(npc.getId(), "$2488"));
								}
							}
							if (npcId == 81209) {
								if (!npc.equalsCurrentSprite(4310)) {
									npc.setCurrentSprite(4310);
									npc.sendShape(4310);
									return;
								} else {
									return;
								}
							}
						}
						if (!(cha instanceof L1PcInstance)) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setMoveSpeed(0);
							npc.setBraveSpeed(0);
							npc.broadcastPacket(new S_SkillHaste(cha.getId(), 0, 0));
							npc.broadcastPacket(new S_SkillBrave(cha.getId(), 0, 0));
							npc.setWeaponBreaked(false);
							npc.setParalyzed(false);
						}

						if (cha instanceof L1PcInstance) {
							detection((L1PcInstance) cha, false);
						}

						for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
							if (isNotCancelable(skillNum) && !cha.isDead()) {
								continue;
							}
							if (skillNum == SHAPE_CHANGE) {
								if (cha instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) cha;
									L1ItemInstance item = pc.getInventory().getItem(_itemobjid);
									if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
										continue;
							
									if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
										continue;
								}
							}
							cha.removeSkillEffect(skillNum);
						}

						for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_END; skillNum++) {
							if (skillNum == STATUS_CHAT_PROHIBITED || skillNum == STATUS_CURSE_BARLOG || skillNum == STATUS_CURSE_YAHEE) {
								continue;
							}
							cha.removeSkillEffect(skillNum);
						}
						cha.removeSkillEffect(STATUS_FRUIT);

						cha.curePoison();
						cha.cureParalaysis();

						for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
							if (isNotCancelable(skillNum) && !cha.isDead()) {
								continue;
							}
							cha.removeSkillEffect(skillNum);
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getHasteItemEquipped() > 0) {
								continue;
							}
						}
						cha.removeSkillEffect(STATUS_FREEZE);

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;

							getTreePotionBuff(pc);

							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.broadcastPacket(new S_CharVisualUpdate(pc));
							if (pc.isPrivateShop()) {
								pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()));
								pc.broadcastPacket(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()));
							}
							if (_user instanceof L1PcInstance) {
								L1PinkName.onAction(pc, _user);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					break;
				case TURN_UNDEAD: {
					if (undeadType == 1 || undeadType == 3) {
						dmg = cha.getCurrentHp();
						if(_target instanceof L1MonsterInstance){
							L1MonsterInstance m = (L1MonsterInstance)_target;
							if(_user instanceof L1PcInstance)
								m.setHate(_user, 1);
						}
					}
				}
					break;
				case MANA_DRAIN: {
					int chance = random.nextInt(5) + 10;
					drainMana = chance + (_user.getAbility().getTotalInt() / 2);
					if (cha.getCurrentMp() < drainMana) {
						drainMana = cha.getCurrentMp();
					}
				}
					break;
				case WEAPON_BREAK: {
					if (_calcType == PC_PC || _calcType == NPC_PC) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1ItemInstance weapon = pc.getWeapon();
							if (weapon != null) {
								int weaponDamage = random.nextInt(_user.getAbility().getTotalInt() / 3) + 1;
								pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
								pc.getInventory().receiveDamage(weapon, weaponDamage);
							}
						}
					} else {
						((L1NpcInstance) cha).setWeaponBreaked(true);
					}
				}
					break;
				case FOG_OF_SLEEPING: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
					}
					cha.setSleeped(true);
				}
					break;
				case STATUS_FREEZE: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
					}
				}
					break;
				case OMAN_STUN: {
					int[] stunTimeArray = { 2500, 3000, 3500 };
					int rnd = random.nextInt(stunTimeArray.length);
					_shockStunDuration = stunTimeArray[rnd];
					L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
						npc.setParalysisTime(_shockStunDuration);
					}
				}
					break;
				case Maeno_STUN: {
					int[] stunTimeArray = { 2500, 3000, 3500 };
					int rnd = random.nextInt(stunTimeArray.length);
					_shockStunDuration = stunTimeArray[rnd];
					L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
						npc.setParalysisTime(_shockStunDuration);
					}
				}
					break;
				case OMAN_CANCELLATION: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (cha instanceof L1PcInstance) {
							((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
						}
						cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

						for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
							if (isNotCancelable(skillNum) && !pc.isDead()) {
								continue;
							}
							if (skillNum == SHAPE_CHANGE) {
								if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
									continue;
								if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
									continue;
							}
							pc.removeSkillEffect(skillNum);
						}
						for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
							if (skillNum == STATUS_CHAT_PROHIBITED) {
								continue;
							}
							pc.removeSkillEffect(skillNum);
						}
						for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
							if (isNotCancelable(skillNum) && !pc.isDead()) {
								continue;
							}
							pc.removeSkillEffect(skillNum);
						}
						pc.curePoison();
						pc.cureParalaysis();
						if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
							L1PolyMorph.undoPoly(pc);
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.broadcastPacket(new S_CharVisualUpdate(pc));
						}
						if (pc.getHasteItemEquipped() > 0) {
							pc.setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
						}
						if (pc != null && pc.isInvisble()) {
							if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
								pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
								pc.sendPackets(new S_Invis(pc.getId(), 0));
								pc.broadcastPacket(new S_Invis(pc.getId(), 0));
								pc.sendPackets(new S_Sound(147));
							}
							if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
								pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
								pc.sendPackets(new S_Invis(pc.getId(), 0));
								pc.broadcastPacket(new S_Invis(pc.getId(), 0));
							}
							L1Party party = pc.getParty();
							if(party != null){
								for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(pc)) {
									if(party.isMember(_pc)){						
										_pc.sendPackets(new S_HPMeter(pc));
									}
								}
								party.refreshPartyMemberStatus(pc);
							}
							
						}
						pc.removeSkillEffect(STATUS_FREEZE);
						getTreePotionBuff(pc);
					}
				}
					break;
				case ANTA_MESSAGE_1: // 안타[용언1 / 캔슬 -> 오브 모크! 케 네시]
				case ANTA_MESSAGE_2: // 안타[용언2 / 블레스+독/ 오브 모크! 켄 로우]
				case ANTA_MESSAGE_3: // 안타[용언3 / 왼손+오른펀치+고함 / 오브 모크! 티기르]
				case ANTA_MESSAGE_4: // 안타[용언4 / 펀치+블레스 / 오브 모크! 켄 티기르]
				case ANTA_MESSAGE_5: // 안타[용언5 / 고함+블레스 / 오브 모크! 루오타]
				case ANTA_MESSAGE_6: // 안타[용언6 / 스턴+점프/ 오브 모크! 뮤즈삼]
				case ANTA_MESSAGE_7: // 안타[용언7 / 스턴+발작/ 오브 모크! 너츠삼]
				case ANTA_MESSAGE_8: // 안타[용언8 / 스턴+발+점/ 오브 모크! 티프삼]
				case ANTA_MESSAGE_9: // 안타[용언9 / 웨폰+블레스 / 오브 모크! 리라프]
				case ANTA_MESSAGE_10: // 안타[용언10 / 웨폰+마비 / 오브 모크! 세이 라라프]
				case ANTA_CANCELLATION:
				case ANTA_WEAPON_BREAK:
				case ANTA_SHOCKSTUN: {
					int npcId = _npc.getNpcTemplate().get_npcId();
					if (npcId == 900011 || npcId == 900012 || npcId == 900013) {
						if (_skillId == ANTA_MESSAGE_1 || _skillId == ANTA_CANCELLATION) { // 캔슬
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (cha instanceof L1PcInstance) {
									((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
								}
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

								for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									if (skillNum == SHAPE_CHANGE) {
										if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
											continue;
										if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
											continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
									if (skillNum == STATUS_CHAT_PROHIBITED) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								pc.curePoison();
								pc.cureParalaysis();
								if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
									L1PolyMorph.undoPoly(pc);
									pc.sendPackets(new S_CharVisualUpdate(pc));
									pc.broadcastPacket(new S_CharVisualUpdate(pc));
								}
								if (pc.getHasteItemEquipped() > 0) {
									pc.setMoveSpeed(0);
									pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
									pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
								}
								if (pc != null && pc.isInvisble()) {
									if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
										pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										pc.broadcastPacket(new S_Invis(pc.getId(), 0));
										pc.sendPackets(new S_Sound(147));
									}
									if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
										pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										pc.broadcastPacket(new S_Invis(pc.getId(), 0));
									}
									L1Party party = pc.getParty();
									if(party != null){
										for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(pc)) {
											if(party.isMember(_pc)){						
												_pc.sendPackets(new S_HPMeter(pc));
											}
										}
										party.refreshPartyMemberStatus(pc);
									}
								}
								pc.removeSkillEffect(STATUS_FREEZE);
								getTreePotionBuff(pc);
							}
						}

						if (_skillId == ANTA_MESSAGE_1 || _skillId == ANTA_MESSAGE_10) {// 마비독
							Random random = new Random();
							int time = random.nextInt(5) + 1;
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (time > 10)
									L1ParalysisPoison.doInfection(pc, _skillId);
							}
						}

						if (_skillId == ANTA_MESSAGE_2 || _skillId == ANTA_MESSAGE_5 || _skillId == ANTA_MESSAGE_9) { // 대미지독
							Random random = new Random();
							int PoisonDmg = random.nextInt(50) + 1;
							int PoisonTime = random.nextInt(15) + 1;
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (PoisonTime > 2)
									L1DamagePoison.doInfection(pc, _target, PoisonTime * 1000, PoisonDmg, _skillId == TOMAHAWK);
							}
						}
						if (_skillId == ANTA_MESSAGE_6 || _skillId == ANTA_MESSAGE_7 || _skillId == ANTA_MESSAGE_8 || _skillId == ANTA_SHOCKSTUN) {// 스턴
							int[] stunTimeArray = { 4500, 5000, 5500 };
							int rnd = random.nextInt(stunTimeArray.length);
							_shockStunDuration = stunTimeArray[rnd];
							L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
							} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
								L1NpcInstance npc = (L1NpcInstance) cha;
								npc.setParalyzed(true);
								npc.setParalysisTime(_shockStunDuration);
							}
						}
						if (_skillId == ANTA_MESSAGE_9 || _skillId == ANTA_MESSAGE_10 || _skillId == ANTA_WEAPON_BREAK) { // 웨폰
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								L1ItemInstance weapon = pc.getWeapon();
								if (weapon != null) {
									int weaponDamage = random.nextInt(3) + 1;
									pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
									pc.getInventory().receiveDamage(weapon, weaponDamage);
									pc.sendPackets(new S_SkillSound(pc.getId(), 172));
									pc.broadcastPacket(new S_SkillSound(pc.getId(), 172));
								}
							}
						}
					}
				}
					break;
				case PAP_PREDICATE1: // 파푸[용언1:리오타! 피로이 나! [오색 진주3 / 신비한 오색 진주1
					// / 토르나 소환5]
				case PAP_PREDICATE3: // 파푸[용언3:리오타! 라나 오이므! [데스포션 -> 오른손 ->
					// 아이스이럽션]
				case PAP_PREDICATE5: // 파푸[용언5:리오타! 네나 우누스! [리듀스 힐 + 머리 공격 + 아이스
					// 브레스]
				case PAP_PREDICATE6: // 파푸[용언6:리오타! 테나 웨인라크! [데스 힐 + 꼬리 공격 + 아이스
					// 브레스]
				case PAP_PREDICATE7: // 파푸[용언7:리오타! 라나 폰폰! [캔슬레이션 + 오른속 2번 ] [범위
					// X]
				case PAP_PREDICATE8: // 파푸[용언8:리오타! 레포 폰폰! [웨폰브레이크 + 왼손 2번 ] [범위
					// X]
				case PAP_PREDICATE9: // 파푸[용언9:리오타! 테나 론디르 ! [꼬리 2연타 + 아이스
					// 브레스][범위 X]
				case PAP_PREDICATE11: // 파푸[용언11:리오타! 오니즈 웨인라크! [매스 캔슬레이션 + 데스 힐
					// + 아이스 미티어 + 아이스 이럽션] [범위 O]
				case PAP_PREDICATE12: { // 파푸[용언12:리오타! 오니즈 쿠스온 웨인라크! [매스 캔슬레이션
					// + 데스힐 + 아이스 미티어 + 발작] [범위 0]
					int npcId = _npc.getNpcTemplate().get_npcId();
					if (npcId == 900038 || npcId == 900039 || npcId == 900040) {
						if (_skillId == PAP_PREDICATE1) { // 리콜 소환(사엘-진주-토르나)
							int i;
							for (i = 0; i < 2; i++) { // 타이머 테이크 부분의 for 문으로
								// 돌리게되면 쓰레드 오류 동작이
								// 발생한다.
								L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900049, 8, 60 * 1000, 0);
								L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900050, 8, 60 * 1000, 0);
								L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900051, 8, 60 * 1000, 0);
								L1SpawnUtil.spawn2(_user.getX(), _user.getY(), (short) _user.getMap().getId(), 900052, 8, 120 * 1000, 0);
							}
						}

						if (_skillId == PAP_PREDICATE7 || _skillId == PAP_PREDICATE11 || _skillId == PAP_PREDICATE12) { // 캔슬
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (cha instanceof L1PcInstance) {
									((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
								}
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

								for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									if (skillNum == SHAPE_CHANGE) {
										if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
											continue;
										if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
											continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
									if (skillNum == STATUS_CHAT_PROHIBITED) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								pc.curePoison();
								pc.cureParalaysis();
								if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
									L1PolyMorph.undoPoly(pc);
									pc.sendPackets(new S_CharVisualUpdate(pc));
									pc.broadcastPacket(new S_CharVisualUpdate(pc));
								}
								if (pc.getHasteItemEquipped() > 0) {
									pc.setMoveSpeed(0);
									pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
									pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
								}
								if (pc != null && pc.isInvisble()) {
									if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
										pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										pc.broadcastPacket(new S_Invis(pc.getId(), 0));
										pc.sendPackets(new S_Sound(147));
									}
									if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
										pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										pc.broadcastPacket(new S_Invis(pc.getId(), 0));
									}
									L1Party party = pc.getParty();
									if(party != null){
										for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(pc)) {
											if(party.isMember(_pc)){						
												_pc.sendPackets(new S_HPMeter(pc));
											}
										}
										party.refreshPartyMemberStatus(pc);
									}
								}
								pc.removeSkillEffect(STATUS_FREEZE);
								getTreePotionBuff(pc);
							}
						}

						if (_skillId == PAP_PREDICATE8) { // 웨폰
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								L1ItemInstance weapon = pc.getWeapon();
								Random random = new Random();
								int rnd = random.nextInt(100) + 1;
								if (weapon != null && rnd > 33) {
									int weaponDamage = random.nextInt(2) + 1;
									pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
									pc.getInventory().receiveDamage(weapon, weaponDamage);
									pc.sendPackets(new S_SkillSound(pc.getId(), 172));
									pc.broadcastPacket(new S_SkillSound(pc.getId(), 172));
								}
							}
						}
						if (_skillId == PAP_PREDICATE3) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 7781));
								pc.broadcastPacket(new S_SkillSound(pc.getId(), 7781));
								pc.setSkillEffect(L1SkillId.PAP_DEATH_PORTION, 12 * 1000);
							}
						}
						if (_skillId == PAP_PREDICATE5) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 7782));
								pc.broadcastPacket(new S_SkillSound(pc.getId(), 7782));
								pc.setSkillEffect(L1SkillId.PAP_REDUCE_HELL, 12 * 1000);
							}
						}

						if (_skillId == PAP_PREDICATE6) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 7780));
								pc.setSkillEffect(L1SkillId.PAP_DEATH_HELL, 12 * 1000);
								pc.broadcastPacket(new S_SkillSound(pc.getId(), 7780));
							}
						}

						if (_skillId == PAP_PREDICATE11 || _skillId == PAP_PREDICATE12) {// 데스
							// 힐
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								pc.sendPackets(new S_SkillSound(pc.getId(), 7780));
								pc.setSkillEffect(L1SkillId.PAP_DEATH_HELL, 12 * 1000);
								pc.broadcastPacket(new S_SkillSound(pc.getId(), 7780));
							}
						}
					}
				}
					break;
				default:
					break;
				}

				if (_calcType == PC_PC || _calcType == NPC_PC) {
					switch (_skillId) {
					case TELEPORT:
					case MASS_TELEPORT: {
						L1PcInstance pc = (L1PcInstance) cha;
						Random random = new Random();
						if (_bookmark_x != 0) {
							if (pc.getMap().isEscapable() || pc.isGm()) {
								L1Map map = L1WorldMap.getInstance().getMap(_bookmark_mapid);
								if (_skillId == MASS_TELEPORT) {
									for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 3)) {
										if (pc.getClanid() != 0 && member.getClanid() == pc.getClanid() && member.getId() != pc.getId()
												&& !member.isPrivateShop()) {
											int newX2 = _bookmark_x + random.nextInt(3) + 1;
											int newY2 = _bookmark_y + random.nextInt(3) + 1;
											if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
												member.start_teleport(newX2, newY2, _bookmark_mapid, member.getHeading(), 169, true, true);
											} else {
												member.start_teleport(_bookmark_x, _bookmark_y, _bookmark_mapid, member.getHeading(), 169, true, true);
											}
										}
									}
								}
								if (pc.getInventory().checkEquippedAtOnce(new int[] { 20288, 900111 })) {
									pc.start_teleport(_bookmark_x, _bookmark_y, _bookmark_mapid, pc.getHeading(), 169, true, true);
								} else {
									int newX2 = _bookmark_x + random.nextInt(15);
									int newY2 = _bookmark_y + random.nextInt(15);
									if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
										pc.start_teleport(newX2, newY2, _bookmark_mapid, pc.getHeading(), 169, true, true);
									} else {
										pc.start_teleport(_bookmark_x, _bookmark_y, _bookmark_mapid, pc.getHeading(), 169, true, true);
									}
								}
							} else {
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
								pc.sendPackets(new S_ServerMessage(79));
							}
						} else {
							if (pc.getMapId() >= 101 && pc.getMapId() <= 110) {
								int find_item_ids[] = { 830022, // 1층
										830023, // 2층
										830024, // 3층
										830025, // 4층
										830026, // 5층
										830027, // 6층
										830028, // 7층
										830029, // 8층
										830030, // 9층
										830031 // 10층
								};
								L1ItemInstance findItem = pc.getInventory().findItemId(find_item_ids[pc.getMapId() - 101]);
								if (findItem != null)
									Telbookitem.toActive(pc, 0, null, _skillId);
								else
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
								pc.sendPackets(new S_ServerMessage(276));
							} else if(pc.getMapId()>=12852 && pc.getMapId()<=12861) {
								int find_item_ids[] = {
										830022, 	// 1층
										830023, 	// 2층
										830024, 	// 3층
										830025, 	// 4층
										830026, 	// 5층
										830027, 	// 6층
										830028, 	// 7층
										830029, 	// 8층
										830030, 	// 9층
										830031   	// 10층
								};
								L1ItemInstance findItem = pc.getInventory().findItemId(find_item_ids[pc.getMapId() - 12852]);
								L1ItemInstance findItem1 = pc.getInventory().findItemId(4100135); // 환상지배의탑 지배부적
								if(findItem != null || findItem1 != null )
									Telbookitem.toActive(pc, 0, null, _skillId);
								else
									pc.sendPackets(new S_ServerMessage(276));
							} else {
								L1Map map = pc.getMap();
								if (map.isTeleportable() || pc.isGm() || (pc.getInventory().checkItem(900111)) && map.isRuler()) {
									Telbookitem.toActive(pc, 0, null, _skillId);
								} else {
									pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
									pc.sendPackets(new S_ServerMessage(276));
								}
							}
						}
					}
						break;
					case TELEPORT_TO_MATHER: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getMap().isEscapable() || pc.isGm()) {
							pc.start_teleport(33051, 32337, 4, 5, 169, true, true);
						} else {
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
							pc.sendPackets(new S_ServerMessage(647));
						}
					}
						break;
					case SHINING_ARMOR:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_user.hasSkillEffect(L1SkillId.SHINING_ARMOR)) {
								_user.removeSkillEffect(L1SkillId.SHINING_ARMOR);
							}
							
							pc.addEffectedER(10);
							pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
							
							SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
							noti.set_noti_type(eNotiType.RESTAT);
							noti.set_spell_id(L1SkillId.SHINING_ARMOR);
							noti.set_duration(_getBuffIconDuration);
							noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
							noti.set_on_icon_id(9483);
							noti.set_off_icon_id(9483);
							noti.set_tooltip_str_id(5892);
							noti.set_new_str_id(5892);
							noti.set_is_good(true);
							pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
						}
						break;
					case MAJESTY:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_user.hasSkillEffect(L1SkillId.MAJESTY)) {
								_user.removeSkillEffect(L1SkillId.MAJESTY);
							}

							SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
							noti.set_noti_type(eNotiType.RESTAT);
							noti.set_spell_id(L1SkillId.MAJESTY);
							noti.set_duration(_getBuffIconDuration);
							noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
							noti.set_on_icon_id(9518);
							noti.set_off_icon_id(9518);
							noti.set_tooltip_str_id(5893);
							noti.set_new_str_id(5893);
							noti.set_is_good(true);
							pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
						}
						break;
					case BRING_STONE: {
						L1PcInstance pc = (L1PcInstance) cha;
						Random random = new Random();
						L1ItemInstance item = pc.getInventory().getItem(_itemobjid);
						if (item != null) {
							int dark = (int) (10 + (pc.getLevel() * 0.8) + (pc.getAbility().getTotalWis() - 6) * 1.2);
							int brave = (int) (dark / 2.1);
							int wise = (int) (brave / 2.0);
							int kayser = (int) (wise / 1.9);
							int chance = random.nextInt(100) + 1;

							if (item.getItem().getItemId() == 40320) {
								pc.getInventory().removeItem(item, 1);
								if (dark >= chance) {
									pc.getInventory().storeItem(40321, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2475"));
								} else {
									pc.sendPackets(new S_ServerMessage(280));
								}
							} else if (item.getItem().getItemId() == 40321) {
								pc.getInventory().removeItem(item, 1);
								if (brave >= chance) {
									pc.getInventory().storeItem(40322, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2476"));
								} else {
									pc.sendPackets(new S_ServerMessage(280));
								}
							} else if (item.getItem().getItemId() == 40322) {
								pc.getInventory().removeItem(item, 1);
								if (wise >= chance) {
									pc.getInventory().storeItem(40323, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2477"));
								} else {
									pc.sendPackets(new S_ServerMessage(280));
								}
							} else if (item.getItem().getItemId() == 40323) {
								pc.getInventory().removeItem(item, 1);
								if (kayser >= chance) {
									pc.getInventory().storeItem(40324, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2478"));
								} else {
									pc.sendPackets(new S_ServerMessage(280));
								}
							}
						}
					}
						break;
					case SUMMON_MONSTER: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.isSM()) {
							pc.sendPackets(new S_ServerMessage(319));
							break;
						}
						if ((pc.getMap().isRecallPets() && !pc.isInWarArea() && pc.getMapId() != 781 && pc.getMapId() != 782) || pc.isGm()) {
							if (pc.getInventory().checkEquipped(20284)) {
								summonMonster(pc, _bookmark_x);
							} else {
								L1Npc npcTemp = NpcTable.getInstance().getTemplate(7320137);
								L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
								summon.setPetcost(0);
							}
							pc.setSM(true);
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					}
						break;
					case LESSER_ELEMENTAL:
					case GREATER_ELEMENTAL: {
						L1PcInstance pc = (L1PcInstance) cha;
						int attr = pc.getElfAttr();
						if (attr != 0) {
							if ((pc.getMap().isRecallPets() && !pc.isInWarArea()) || pc.isGm()) {
								int petcost = 0;
								Object[] petlist = pc.getPetList().values().toArray();
								for (Object pet : petlist) {
									if (pet instanceof L1SummonInstance) {
										petcost += 1;
									}
								}

								if (petcost == 0) {
									int summonid = 0;
									int summons[];
									if (_skillId == LESSER_ELEMENTAL) {
										summons = new int[] { 45306, 45303, 45304, 45305 };
									} else {
										summons = new int[] { 81053, 81050, 81051, 81052 };
									}
									int npcattr = 1;
									for (int i = 0; i < summons.length; i++) {
										if (npcattr == attr) {
											summonid = summons[i];
											i = summons.length;
										}
										npcattr *= 2;
									}
									if (summonid == 0) {
										Random random = new Random();
										int k3 = random.nextInt(4);
										summonid = summons[k3];
									}

									L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
									L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
								} else {
									pc.sendPackets(new S_ServerMessage(319));
								}
							} else {
								pc.sendPackets(new S_ServerMessage(79));
							}
						}
					}
						break;
					case ABSOLUTE_BARRIER: {
						L1PcInstance pc = (L1PcInstance) cha;
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.ABSOLUTE_BARRIER);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(717);
						noti.set_off_icon_id(717);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(700);
						noti.set_new_str_id(700);
						noti.set_end_str_id(2179);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
						pc.stopHpMpRegeneration();
						pc.stopMpRegenerationByDoll();
					}
						break;
					case GLOWING_AURA: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(5);
						pc.addBowHitup(5);
						pc.getResistance().addMr(20);
						pc.sendPackets(new S_SPMR(pc));
						pc.sendPackets(new S_SkillIconAura(113, _getBuffIconDuration));
					}
						break;
					case SHINING_AURA: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-8);
						pc.sendPackets(new S_SkillIconAura(114, _getBuffIconDuration));
					}
						break;
					case BRAVE_AURA: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(5);
						pc.sendPackets(new S_SkillIconAura(116, _getBuffIconDuration));
					}
						break;
					case SHIELD: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-2);
						pc.sendPackets(new S_SkillIconShield(2, _getBuffIconDuration));
					}
						break;
					case SHADOW_ARMOR: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addMr(5);
						pc.sendPackets(new S_SPMR(pc));
						// pc.sendPackets(new S_SkillIconShield(3,
						// _getBuffIconDuration));
					}
						break;
					case DRESS_DEXTERITY: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedDex((byte) 3);
						pc.sendPackets(new S_Dexup(pc, 3, _getBuffIconDuration));
					}
						break;
					case DRESS_MIGHTY: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) 3);
						pc.sendPackets(new S_Strup(pc, 3, _getBuffIconDuration));
					}
						break;
					case SHADOW_FANG: {
						if (!(cha instanceof L1PcInstance)) {
							return;
						}
						L1PcInstance pc = (L1PcInstance) cha;
						remove_skills(pc, new Integer[] { _skillId, L1SkillId.SHADOW_FANG});
						pc.addDmgup(5);
						on_icons(pc, _skillId, _skill.getBuffDuration());
					}
						break;
					case ENCHANT_WEAPON: {
						if (!(cha instanceof L1PcInstance)) {
							return;
						}
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.is_자동버프사용()) {
							remove_skills(pc, new Integer[] { _skillId, L1SkillId.BLESS_WEAPON, L1SkillId.HOLY_WEAPON, L1SkillId.SHADOW_FANG });
						}
						if(check_skills(pc, new Integer[] { L1SkillId.BLESS_WEAPON, L1SkillId.SHADOW_FANG }))
							return;

						remove_skills(pc, new Integer[] { _skillId, L1SkillId.HOLY_WEAPON });
						pc.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000);
						pc.addDmgup(2);
						on_icons(pc, _skillId, _skill.getBuffDuration());
					}
						break;
					case HOLY_WEAPON: {
						if (!(cha instanceof L1PcInstance)) {
							return;
						}
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.is_자동버프사용()) {
							remove_skills(pc, new Integer[] { _skillId, L1SkillId.BLESS_WEAPON, L1SkillId.ENCHANT_WEAPON, L1SkillId.SHADOW_FANG });
						}
						if (check_skills(pc, new Integer[] { L1SkillId.BLESS_WEAPON, L1SkillId.ENCHANT_WEAPON, L1SkillId.SHADOW_FANG  }))
							return;

						pc.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000);
						pc.addDmgup(1);
						pc.addHitup(1);
						on_icons(pc, _skillId, _skill.getBuffDuration());
					}
						break;
					case BLESS_WEAPON: {
						if (!(cha instanceof L1PcInstance)) {
							return;
						}
						L1PcInstance pc = (L1PcInstance) cha;
						remove_skills(pc, new Integer[] { _skillId, L1SkillId.HOLY_WEAPON, L1SkillId.ENCHANT_WEAPON });
						pc.setSkillEffect(_skillId, _skill.getBuffDuration() * 1000);
						pc.addDmgup(2);
						pc.addHitup(2);
						on_icons(pc, _skillId, _skill.getBuffDuration());
					}
						break;
					case BLESSED_ARMOR: {
						if (!(cha instanceof L1PcInstance)) {
							return;
						}
						L1PcInstance pc = (L1PcInstance) cha;
						remove_skills(pc, new Integer[] { _skillId, L1SkillId.BLESSED_ARMOR});
						on_icons(pc, _skillId, _skill.getBuffDuration());
						pc.getAC().addAc(-3);
						pc.sendPackets(new S_OwnCharStatus(pc));
					}
						break;
					case EARTH_GUARDIAN: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillIconShield(7, _getBuffIconDuration));
					}
						break;
					case RESIST_MAGIC: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addMr(10);
						pc.sendPackets(new S_SPMR(pc));
					}
						break;
					case CLEAR_MIND:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_user.hasSkillEffect(L1SkillId.CLEAR_MIND)) {
								_user.removeSkillEffect(L1SkillId.CLEAR_MIND);
							}
							
							pc.getAbility().addAddedStr((byte) 1);
							pc.getAbility().addAddedDex((byte) 1);
							pc.getAbility().addAddedInt((byte) 1);
							
							SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
							noti.set_noti_type(eNotiType.RESTAT);
							noti.set_spell_id(L1SkillId.CLEAR_MIND);
							noti.set_duration(_getBuffIconDuration);
							noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
							noti.set_on_icon_id(745);
							noti.set_off_icon_id(5279);
							noti.set_tooltip_str_id(861);
							noti.set_new_str_id(861);
							noti.set_is_good(true);
							pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case RESIST_ELEMENTAL: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addAllNaturalResistance(10);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
						break;
					case BODY_TO_MIND: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.setCurrentMp(pc.getCurrentMp() + 2);
					}
						break;
					case BLOODY_SOUL: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.setCurrentMp(pc.getCurrentMp() + 25); // 기존 15
					}
						break;
					case ELEMENTAL_PROTECTION: {
						L1PcInstance pc = (L1PcInstance) cha;
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
					}
						break;
					case INVISIBILITY:
					case BLIND_HIDING: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.is_combat_field())
							return;

						Object[] dollList = pc.getDollList().values().toArray();
						for (Object dollObject : dollList) {
							L1DollInstance doll = (L1DollInstance) dollObject;
							doll.deleteDoll(false);
						}
						pc.setSkillEffect(L1SkillId.INVISIBILITY, _getBuffIconDuration);
						pc.sendPackets(new S_Invis(pc.getId(), 1));
						pc.broadcastPacket(new S_Invis(pc.getId(), 1));
						pc.broadcastPacket(new S_RemoveObject(pc));
						pc.broadcastPacket(S_WorldPutObject.get(pc));
						
						L1Party party = pc.getParty();
						if(party != null){
							for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(pc)) {
								if(party.isMember(_pc)){						
									_pc.sendPackets(new S_HPMeter(pc));
								}
							}
							party.refreshPartyMemberStatus(pc);
						}
						// pc.broadcastPacket(new S_OtherCharPacks(pc));
					}
						break;
					case BUFF_SAEL: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.BUFF_SAEL)) {
								pc.removeSkillEffect(L1SkillId.BUFF_SAEL);
							}
							pc.getAC().addAc(-8);
							pc.addBowHitup(6);
							pc.addBowDmgup(3);
							pc.addMaxHp(80);
							pc.addMaxMp(10);
							pc.addHpr(8);
							pc.addMpr(1);
							pc.getResistance().addWater(30);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(new S_SPMR(pc));
						}
					}
						break;
					case BUFF_GUNTER: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.BUFF_GUNTER)) {
								pc.removeSkillEffect(L1SkillId.BUFF_GUNTER);
							}
							pc.getAbility().addAddedDex((byte) 5);
							pc.addBowHitup(7);
							pc.addBowDmgup(5);
							pc.addMaxHp(100);
							pc.addMaxMp(40);
							pc.addHpr(10);
							pc.addMpr(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(new S_SPMR(pc));
						}
					}
						break;
					case God_buff: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-2);
						pc.addHitup(3);
						pc.addMaxHp(20);
						pc.addMaxMp(13);
						pc.addSpecialResistance(eKind.SPIRIT, 10);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case IRON_SKIN: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-10);
						pc.sendPackets(new S_SkillIconShield(10, _getBuffIconDuration));
					}
						break;
					case FIRE_SHIELD: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-4);
						pc.sendPackets(new S_SkillIconShield(4, _getBuffIconDuration));
					}
						break;
					case PHYSICAL_ENCHANT_STR: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) 5);
						pc.sendPackets(new S_Strup(pc, 5, _getBuffIconDuration));
					}
						break;
					case PHYSICAL_ENCHANT_DEX: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedDex((byte) 5);
						pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration));
					}
						break;
					case REDUCTION_ARMOR:{
						L1PcInstance pc = (L1PcInstance) cha;
						if (_user.hasSkillEffect(L1SkillId.REDUCTION_ARMOR)) {
							_user.removeSkillEffect(L1SkillId.REDUCTION_ARMOR);
						}
						
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.REDUCTION_ARMOR);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(1889);
						noti.set_off_icon_id(1889);
						noti.set_tooltip_str_id(1043);
						noti.set_new_str_id(1043);
						noti.set_end_str_id(2197);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
					break;
					case 나루토감사캔디: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
							pc.getAbility().addAddedDex((byte) 7);
							pc.sendPackets(new S_Dexup(pc, 7, _getBuffIconDuration));
							pc.getAbility().addAddedStr((byte) 7);
							pc.sendPackets(new S_Strup(pc, 7, _getBuffIconDuration));
						} else {
							pc.getAbility().addAddedDex((byte) 6);
							pc.sendPackets(new S_Dexup(pc, 6, _getBuffIconDuration));
							pc.getAbility().addAddedStr((byte) 6);
							pc.sendPackets(new S_Strup(pc, 6, _getBuffIconDuration));
						}
					}
						break;
					case EARTH_WEAPON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(4);
						pc.addDmgup(2);
						// pc.sendPackets(new S_SkillIconAura(147,
						// _getBuffIconDuration));
					}
						break;
					case REDUCE_WEIGHT:// 환술사,마법사 스킬같이 사용해라
					case DECREASE_WEIGHT: {// 마법사 마법
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addWeightReduction(180);
					}
						break;
					case COUNTER_MIRROR: {// 엘븐 그레비티
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addWeightReduction(300);
					}
						break;
					case HURRICANE: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillBrave(pc.getId(), 9, _getBuffIconDuration));
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 9, 0));
						pc.setAttackSpeed();
						break;
					}
					case FOCUS_WAVE: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.FOCUS_WAVE)) {
							pc.removeSkillEffect(L1SkillId.FOCUS_WAVE);
						}
						pc.set_focus_wave_level(1);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 10, _getBuffIconDuration));
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 10, 0));
						pc.setAttackSpeed();
						break;
					}
					case SAND_STORM: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillBrave(pc.getId(), 1, _getBuffIconDuration));
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0));
						pc.setAttackSpeed();
						break;
					}
					case DANCING_BLADES: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillBrave(pc.getId(), 1, _getBuffIconDuration));
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0));
						pc.sendPackets(new S_SkillIconAura(154, _getBuffIconDuration));
						pc.setAttackSpeed();
					}
						break;
					case BURNING_WEAPON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(6);
						pc.addHitup(3);
						pc.sendPackets(new S_SkillIconAura(162, _getBuffIconDuration));
					}
						break;
					case MIRROR_IMAGE: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDg(30);
					}
						break;
					case AQUA_SHOT: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addBowHitup(4);
						// pc.sendPackets(new S_SkillIconAura(148,
						// _getBuffIconDuration));
					}
						break;
					case STORM_EYE: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addBowHitup(2);
						pc.addBowDmgup(3);
						pc.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration));
					}
						break;
					case STORM_SHOT: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addBowDmgup(5);
						pc.addBowHitup(-3);
						pc.sendPackets(new S_SkillIconAura(165, _getBuffIconDuration));
					}
						break;
					case BERSERKERS: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(10);
						pc.addDmgup(2);
						pc.addHitup(8);
					}
						break;

					case SCALES_WATER_DRAGON: {
						L1PcInstance pc = (L1PcInstance) cha;
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.SCALES_WATER_DRAGON);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);

						noti.set_tooltip_str_id(2271);
						noti.set_end_str_id(2424);

						noti.set_on_icon_id(3184);
						noti.set_off_icon_id(3184);
						noti.set_icon_priority(10);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case SCALES_EARTH_DRAGON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-3);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);

						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.SCALES_EARTH_DRAGON);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_tooltip_str_id(2267);
						noti.set_end_str_id(2420);

						noti.set_on_icon_id(3182);
						noti.set_off_icon_id(3182);
						noti.set_icon_priority(10);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case SCALES_RINDVIOR_DRAGON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDg(7);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.SCALES_RINDVIOR_DRAGON);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_tooltip_str_id(5270);
						noti.set_new_str_id(5270);
						noti.set_end_str_id(5271);
						noti.set_on_icon_id(8887);
						noti.set_off_icon_id(8887);
						noti.set_icon_priority(10);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case SCALES_FIRE_DRAGON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addSpecialResistance(eKind.ABILITY, 10);
						pc.addHitup(5);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.SCALES_FIRE_DRAGON);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_tooltip_str_id(2274);
						noti.set_end_str_id(2427);
						noti.set_on_icon_id(3078);
						noti.set_off_icon_id(3078);
						noti.set_icon_priority(10);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;

					case IllUSION_OGRE: { // 일루젼 오거
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(IllUSION_OGRE))
							pc.removeSkillEffect(IllUSION_OGRE);

						pc.addDmgup(4);
						pc.addHitup(4);
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.IllUSION_OGRE);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(3117);
						noti.set_off_icon_id(3117);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(1340);
						noti.set_new_str_id(1366);
						noti.set_end_str_id(2212);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case IllUSION_LICH: { // 일루젼 리치
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(IllUSION_LICH))
							pc.removeSkillEffect(IllUSION_LICH);

						pc.getAbility().addSp(2);
						pc.sendPackets(new S_SPMR(pc));
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.IllUSION_LICH);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(3115);
						noti.set_off_icon_id(3115);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(1343);
						noti.set_new_str_id(1369);
						noti.set_end_str_id(1369);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);

					}
						break;
					case IllUSION_DIAMONDGOLEM: { // 일루젼 다이아골렘
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(IllUSION_DIAMONDGOLEM))
							pc.removeSkillEffect(IllUSION_DIAMONDGOLEM);

						pc.getAC().addAc(-8);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.IllUSION_DIAMONDGOLEM);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(3113);
						noti.set_off_icon_id(3113);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(1373);
						noti.set_new_str_id(1373);
						noti.set_end_str_id(2214);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case IllUSION_AVATAR: { // 일루젼 아바타
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(IllUSION_AVATAR))
							pc.removeSkillEffect(IllUSION_AVATAR);

						pc.addDmgup(10);
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.IllUSION_AVATAR);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(3111);
						noti.set_off_icon_id(3111);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(1377);
						noti.set_new_str_id(1377);
						noti.set_end_str_id(2215);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case INSIGHT: { // 인사이트
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) 1);
						pc.getAbility().addAddedDex((byte) 1);
						pc.getAbility().addAddedCon((byte) 1);
						pc.getAbility().addAddedInt((byte) 1);
						pc.getAbility().addAddedWis((byte) 1);
						pc.resetBaseMr();
					}
						break;
					case 천하장사버프: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(천하장사버프))
							pc.removeSkillEffect(천하장사버프);
						pc.addDamageReductionByArmor(5);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, pc, 187, 1800));
						pc.setDessertId(천하장사버프);
						pc.sendPackets(new S_ServerMessage(1426));
					}
						break;
					case ADVANCE_SPIRIT: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_user.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
							_user.removeSkillEffect(L1SkillId.ADVANCE_SPIRIT);
						}
						
						pc.setAdvenHp(pc.getBaseMaxHp() / 5);
						pc.setAdvenMp(pc.getBaseMaxMp() / 5);
						pc.addMaxHp(pc.getAdvenHp());
						pc.addMaxMp(pc.getAdvenMp());
						if (pc.isInParty()) {
							//TODO 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.ADVANCE_SPIRIT);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(1607);
						noti.set_off_icon_id(1607);
						noti.set_tooltip_str_id(982);
						noti.set_new_str_id(982);
						noti.set_end_str_id(2180);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;
					case PRIDE:{
						L1PcInstance pc = (L1PcInstance) cha;
						int current = pc.getCurrentHp();
						if (_user.hasSkillEffect(L1SkillId.PRIDE)) {
							_user.removeSkillEffect(L1SkillId.PRIDE);
						}
						
						double percent = pc.getLevel() / 4;
						int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
						pc.setMagicBuffHp(addHp);
						pc.addMaxHp(pc.getMagicBuffHp());
						
						if(current > pc.getMaxHp()){
							current = pc.getMaxHp();
						}
						pc.setCurrentHp(current);
						
						
						if (pc.isInParty()) {
							//TODO 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.setSkillEffect(L1SkillId.PRIDE, 300 * 1000);
						
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.PRIDE);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(8854);
						noti.set_off_icon_id(8854);
						noti.set_tooltip_str_id(5264);
						noti.set_new_str_id(5264);
						noti.set_end_str_id(5265);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
						
					}
					break;
					case GIGANTIC:{
						L1PcInstance pc = (L1PcInstance) cha;
						int current = pc.getCurrentHp();
						if (_user.hasSkillEffect(L1SkillId.GIGANTIC)) {
							_user.removeSkillEffect(L1SkillId.GIGANTIC);
						}
						
						double percent = pc.getLevel() / 2;
						int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
						pc.setMagicBuffHp(addHp);
						pc.addMaxHp(pc.getMagicBuffHp());
						
						if(current > pc.getMaxHp()){
							current = pc.getMaxHp();
						}
						pc.setCurrentHp(current);
						
						
						if (pc.isInParty()) {
							//TODO 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						
						
						
						
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.setSkillEffect(L1SkillId.GIGANTIC, 300 * 1000);
						
						
						
						
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.GIGANTIC);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(6168);
						noti.set_off_icon_id(6168);
						noti.set_tooltip_str_id(3918);
						noti.set_new_str_id(3918);
						noti.set_end_str_id(3919);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
					break;
					/** 파워그립 시전시간 본섭화 **/
					case POWERRIP: {
						int[] PowerRipTimeArray = { 1500,2000,2000,3000,3000,3000,4000, 5000 };
						int rnd = random.nextInt(PowerRipTimeArray.length);
						_PowerRipDuration = PowerRipTimeArray[rnd];
						L1EffectSpawn.getInstance().spawnEffect(9415, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId());
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							_target.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
						} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							npc.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
							npc.set발묶임상태(true);
						}
					}
						break;
					case DESPERADO: {// 데페
						int targetLevel = 0;
						int diffLevel = 0;

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
						}

						diffLevel = _user.getLevel() - targetLevel;

						int[] stunTimeArray = null;
						int effectNpcId = 9416;
						//TODO 밑으로 갈수록 레벨 차이가 많이남 즉 시간초가 더 길어짐
						if (diffLevel < -5) {
							stunTimeArray = new int[] { 1000, 2000, 1000, 2000, 3000, 4000, 3000, 2000 };
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
						} else if (diffLevel >= -5 && diffLevel <= -3) {
							stunTimeArray = new int[] { 1000, 2000, 1000, 2000, 3000, 4000, 3000, 2000 };
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							//위로 1~2초
						} else if (diffLevel >= -2 && diffLevel <= 2) {
							stunTimeArray = new int[] { 2000, 2500, 2000, 3000, 3000, 4000, 3000, 4000 };
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
						} else if (diffLevel >= 3 && diffLevel <= 5) {
							stunTimeArray = new int[] { 2000, 2500, 2000, 3000, 3000, 4000, 3000, 4000 };
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
							//위로 1~3초
						} else if (diffLevel >= 5 && diffLevel <= 10) {
							stunTimeArray = new int[] { 2000, 2500, 3000, 3000, 3000, 4000, 5000, 4000 };
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
						} else if (diffLevel > 10) {
							stunTimeArray = new int[] { 3000, 3500, 4000, 5000, 3000, 4000, 3000, 4000 };
							_shockStunDuration = stunTimeArray[random.nextInt(stunTimeArray.length)];
						}
						//위로 1~4초

						int rnd = random.nextInt(stunTimeArray.length);
						_shockStunDuration = stunTimeArray[rnd];

						if (_player != null && _player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
							int[] bonus = { 1000, 2000, 1000, 2000, 1000, 1000, 2000 };
							int rnd2 = random.nextInt(stunTimeArray.length);
							int bonusTime = bonus[rnd2];
							_shockStunDuration += bonusTime;
							effectNpcId = 9418;
						}

						L1EffectSpawn.getInstance().spawnEffect(effectNpcId, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId());

						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							if (pc.hasSkillEffect(L1SkillId.DESPERADO))
								pc.removeSkillEffect(L1SkillId.DESPERADO);
							pc.Desperadolevel = _user.getLevel();
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, true));
							_target.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
						} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							npc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
							npc.set발묶임상태(true);
						}
					}
						break;
					case GREATER_HASTE: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getHasteItemEquipped() > 0) {
							continue;
						}
						if (pc.getMoveSpeed() != 2) {
							pc.setDrink(false);
							pc.setMoveSpeed(1);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration));
							pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0));
						} else {
							int skillNum = 0;
							if (pc.hasSkillEffect(SLOW)) {
								skillNum = SLOW;
							} else if (pc.hasSkillEffect(MOB_SLOW_1)) {
								skillNum = MOB_SLOW_1;
							} else if (pc.hasSkillEffect(MOB_SLOW_18)) {
								skillNum = MOB_SLOW_18;
							}
							if (skillNum != 0) {
								pc.removeSkillEffect(skillNum);
								pc.removeSkillEffect(GREATER_HASTE);
								pc.setMoveSpeed(0);
								continue;
							}
						}
					}
						break;
					case EAGGLE_EYE: {
						if (cha.hasSkillEffect(EAGGLE_EYE))
							cha.removeSkillEffect(EAGGLE_EYE);
						L1PcInstance pc = (L1PcInstance) cha;
						pc.add_missile_critical_rate(2);
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.EAGGLE_EYE);
						noti.set_duration(_getBuffIconDuration);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(8490);
						noti.set_off_icon_id(8490);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(5157);
						noti.set_new_str_id(0);
						noti.set_end_str_id(5158);
						noti.set_is_good(true);
						pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					}
						break;

					case HOLY_WALK:
					case MOVING_ACCELERATION: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.setBraveSpeed(4);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration));
						pc.broadcastPacket(new S_SkillBrave(pc.getId(), 4, 0));
					}
						break;
					case BLOOD_LUST: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.setBraveSpeed(1);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration));
						pc.broadcastPacket(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration));
					}
						break;
					// 크레이 혈흔
					case BUFF_CRAY: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addHitup(5);
							pc.addDmgup(1);
							pc.addBowHitup(5);
							pc.addBowDmgup(1);
							pc.addExp(30);
							pc.addMaxHp(100);
							pc.addMaxMp(50);
							pc.addHpr(3);
							pc.addMpr(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
							pc.sendPackets(new S_SPMR(pc));
						}
					}
						break;
					case BUFF_Vala: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addHitup(5);
							pc.addDmgup(1);
							pc.addBowHitup(5);
							pc.addBowDmgup(1);
							pc.addExp(30);
							pc.addMaxHp(100);
							pc.addMaxMp(50);
							pc.addHpr(3);
							pc.addMpr(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
							pc.sendPackets(new S_SPMR(pc));
						}
					}
						break;
					case COMA_A:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addAddedCon(1);
							pc.getAbility().addAddedDex(5);
							pc.getAbility().addAddedStr(5);
							pc.addHitRate(3);
							pc.getAC().addAc(-3);
						}
						break;
					case COMA_B:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAbility().addSp(1);
							pc.getAbility().addAddedCon(3);
							pc.getAbility().addAddedDex(5);
							pc.getAbility().addAddedStr(5);
							pc.addHitRate(5);
							pc.getAC().addAc(-8);
							pc.sendPackets(new S_SPMR(pc));
						}
						break;
					case FEATHER_BUFF_A: { // 운세버프 (매우 좋은)
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHpr(3);
						pc.addMpr(3);
						pc.addDmgup(2);
						pc.addHitup(2);
						pc.addMaxHp(50);
						pc.addMaxMp(30);
						pc.getAbility().addSp(2);
						pc.sendPackets(new S_SPMR(pc));
						if (pc.isInParty()) {
							// TODO 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					}
						break;
					case FEATHER_BUFF_B: { // 운세버프 (좋은)
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(2);
						// pc.addSp(1);
						pc.getAbility().addSp(1);
						pc.addMaxHp(50);
						pc.addMaxMp(30);
						pc.sendPackets(new S_SPMR(pc));
						if (pc.isInParty()) {
							// TODO 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					}
						break;
					case FEATHER_BUFF_C: { // 운세버프 (보통)
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addMaxHp(50);
						pc.addMaxMp(30);
						pc.getAC().addAc(-2);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						if (pc.isInParty()) {
							// TODO 파티 프로토
							pc.getParty().refreshPartyMemberStatus(pc);
						}
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					}
						break;
					case FEATHER_BUFF_D: { // 운세버프 (나쁜)
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-1);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
						break;
					case 정상의가호: {// 
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDamageReductionByArmor(8);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
						break;
					case ANTA_MAAN: {// 지룡의 마안
						L1PcInstance pc = (L1PcInstance) cha;
						if(pc.hasSkillEffect(ANTA_MAAN)){
							pc.removeSkillEffect(ANTA_MAAN);
						}
						pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case FAFU_MAAN: {// 수룡의 마안
						L1PcInstance pc = (L1PcInstance) cha;
						if(pc.hasSkillEffect(FAFU_MAAN)){
							pc.removeSkillEffect(FAFU_MAAN);
						}
						pc.addSpecialResistance(eKind.SPIRIT, 5);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case LIND_MAAN: {// 풍룡의 마안
						L1PcInstance pc = (L1PcInstance) cha;
						if(pc.hasSkillEffect(LIND_MAAN)){
							pc.removeSkillEffect(LIND_MAAN);
						}
						pc.add_magic_critical_rate(2);
						pc.addSpecialResistance(eKind.FEAR, 5);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
						pc.sendPackets(new S_SPMR(pc));
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case VALA_MAAN: {// 화룡의 마안
						L1PcInstance pc = (L1PcInstance) cha;
						if(pc.hasSkillEffect(VALA_MAAN)){
							pc.removeSkillEffect(VALA_MAAN);
						}
						pc.addDmgup(2);
						pc.addBowDmgup(2);
						pc.addSpecialResistance(eKind.ABILITY, 5);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case BIRTH_MAAN: {// 탄생의 마안
						L1PcInstance pc = (L1PcInstance) cha;
						if(pc.hasSkillEffect(BIRTH_MAAN)){
							pc.removeSkillEffect(BIRTH_MAAN);
						}
						pc.addSpecialResistance(eKind.SPIRIT, 5);
						pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case SHAPE_MAAN: {// 형상의 마안
						L1PcInstance pc = (L1PcInstance) cha;
						if(pc.hasSkillEffect(SHAPE_MAAN)){
							pc.removeSkillEffect(SHAPE_MAAN);
						}
						pc.add_magic_critical_rate(1);
						pc.addSpecialResistance(eKind.SPIRIT, 5);
						pc.addSpecialResistance(eKind.DRAGON_SPELL, 5);
						pc.addSpecialResistance(eKind.FEAR, 5);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
						pc.sendPackets(new S_SPMR(pc));
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case LIFE_MAAN: {// 생명의 마안
						L1PcInstance pc = (L1PcInstance) cha;
						if(pc.hasSkillEffect(LIFE_MAAN)){
							pc.removeSkillEffect(LIFE_MAAN);
						}
						pc.addSpecialResistance(eKind.ALL, 5);
						pc.addDmgup(2);
						pc.addBowDmgup(2);
						pc.add_magic_critical_rate(1);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
						pc.sendPackets(new S_SPMR(pc));
						SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
					}
						break;
					case SIDE_OF_ME_BLESSING: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDamageReductionByArmor(5);
						pc.addDmgup(5);
						pc.addBowDmgup(5);
					}
						break;
					case RE_START_BLESSING: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDamageReductionByArmor(3);
						pc.addDmgup(3);
						pc.addBowDmgup(3);
					}
						break;
					case NEW_START_BLESSING: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDamageReductionByArmor(2);
						pc.addDmgup(2);
						pc.addBowDmgup(2);
					}
						break;
					case LIFE_BLESSING: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDamageReductionByArmor(1);
						pc.addDmgup(1);
						pc.addBowDmgup(1);
					}
						break;
					case ANTA_BUFF: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.ANTA_BUFF))
								pc.removeSkillEffect(L1SkillId.ANTA_BUFF);
							pc.getAC().addAc(-2);
							pc.getResistance().addWater(50);
							pc.sendPackets(new S_OwnCharStatus(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, _getBuffIconDuration / 60));
						}
					}
						break;
					case FAFU_BUFF: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.FAFU_BUFF))
								pc.removeSkillEffect(L1SkillId.FAFU_BUFF);
							pc.addHpr(3);
							pc.addMpr(1);
							pc.getResistance().addWind(50);
							pc.sendPackets(new S_OwnCharStatus(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, _getBuffIconDuration / 60));
						}
					}
						break;
					case RIND_BUFF: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.hasSkillEffect(L1SkillId.RIND_BUFF))
								pc.removeSkillEffect(L1SkillId.RIND_BUFF);
							pc.addHitup(3);
							pc.addBowHitup(3);
							pc.getResistance().addFire(50);
							pc.sendPackets(new S_OwnCharStatus(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, _getBuffIconDuration / 60));
						}
					}
						break;
					case BOUNCE_ATTACK: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(6);
					}
						break;
					// 린드비오르
					case RINDVIOR_SUMMON_MONSTER_CLOUD: {
						L1SpawnUtil.spawn(_npc, 5110, 10); // 구름대정령
					}
						break;
					case RINDVIOR_PREDICATE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (_npc.getLocation().getTileLineDistance(pc.getLocation()) > 4) {
								L1Location newLoc = null;
								for (int count = 0; count < 10; count++) {
									newLoc = _npc.getLocation().randomLocation(3, 4, false);
									if (_npc.glanceCheck(newLoc.getX(), newLoc.getY()) == true) {
										// L1Teleport.teleport(pc,
										// newLoc.getX(), newLoc.getY(),
										// _npc.getMapId(), 5, true);
										pc.start_teleport(newLoc.getX(), newLoc.getY(), _npc.getMapId(), 5, 169, true, true);
										break;
									}
								}
							}
						}
					}
						break;
					case RINDVIOR_SUMMON_MONSTER: {
						Random _random = new Random();
						int[] MobId = new int[] { 5106, 5107, 5108, 5109 }; // 광물
						// 골렘
						int rnd = _random.nextInt(100);
						for (int i = 0; i < _random.nextInt(2) + 1; i++) {
							L1SpawnUtil.spawn(_npc, MobId[rnd % MobId.length], _random.nextInt(3) + 8);
						}
					}
						break;
					case RINDVIOR_SILENCE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead()) {
								continue;
							}
							pc.setSkillEffect(L1SkillId.SILENCE, 12 * 1000);
							pc.sendPackets(new S_SkillSound(pc.getId(), 2177));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2177));
						}
					}
						break;
					case RINDVIOR_BOW: {
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							if (pc.isDead()) {
								continue;
							}
							int SprNum = 0;
							int pcX = pc.getX();
							int pcY = pc.getY();
							int npcId = _npc.getNpcTemplate().get_npcId();
							switch (npcId) {
							case 5097:
								pcY -= 6;
								SprNum = 7987;
								break;
							case 5098:
								pcX += 4;
								pcY -= 4;
								SprNum = 8050;
								break;
							case 5099:
								pcX += 5;
								SprNum = 8051;
								break;
							default:
								break;
							}
							S_EffectLocation packet = new S_EffectLocation(pcX, pcY, SprNum);
							pc.sendPackets(packet);
							Broadcaster.broadcastPacket(pc, packet);
						}
					}
						break;
					case RINDVIOR_WIND_SHACKLE:
					case RINDVIOR_WIND_SHACKLE_1: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead()) {
								continue;
							}
							pc.setSkillEffect(L1SkillId.WIND_SHACKLE, 12 * 1000);
							if (pc.getCurrentWeapon() == 88) {

							} else {
								pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
							}
							pc.sendPackets(new S_SkillSound(pc.getId(), 1799));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 1799));
						}
					}
						break;
					case RINDVIOR_PREDICATE_CANCELLATION: {
						Random random = new Random();
						int Chance = random.nextInt(100) + 1;
						if (Chance > 33) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (_npc.getLocation().getTileLineDistance(pc.getLocation()) > 4) {
									L1Location newLoc = null;
									for (int count = 0; count < 10; count++) {
										newLoc = _npc.getLocation().randomLocation(3, 4, false);
										if (_npc.glanceCheck(newLoc.getX(), newLoc.getY()) == true) {
											// L1Teleport.teleport(pc,
											// newLoc.getX(), newLoc.getY(),
											// _npc.getMapId(), 5, true);
											pc.start_teleport(newLoc.getX(), newLoc.getY(), _npc.getMapId(), 5, 169, true, true);
											break;
										}
									}
								}
								if (cha instanceof L1PcInstance) {
									((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
								}
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));

								for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									if (skillNum == SHAPE_CHANGE) {
										if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
											continue;
										if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
											continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
									if (skillNum == STATUS_CHAT_PROHIBITED) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								pc.curePoison();
								pc.cureParalaysis();
								if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
									L1PolyMorph.undoPoly(pc);
									pc.sendPackets(new S_CharVisualUpdate(pc));
									Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
								}
								if (pc.getHasteItemEquipped() > 0) {
									pc.setMoveSpeed(0);
									pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
									Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
								}
								if (pc != null && pc.isInvisble()) {
									if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
										pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										pc.sendPackets(new S_Sound(147));
									}
									if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
										pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
									}
								}
								pc.removeSkillEffect(STATUS_FREEZE);
								getTreePotionBuff(pc);
							}
						}
					}
						break;
					case RINDVIOR_CANCELLATION: {
						Random random = new Random();
						int Chance = random.nextInt(100) + 1;
						if (Chance > 33) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (cha instanceof L1PcInstance) {
									((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
								}
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));
								for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									if (skillNum == SHAPE_CHANGE) {
										if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
											continue;
										if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
											continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
									if (skillNum == STATUS_CHAT_PROHIBITED) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								pc.curePoison();
								pc.cureParalaysis();
								if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
									L1PolyMorph.undoPoly(pc);
									pc.sendPackets(new S_CharVisualUpdate(pc));
									Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
								}
								if (pc.getHasteItemEquipped() > 0) {
									pc.setMoveSpeed(0);
									pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
									Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
								}
								if (pc != null && pc.isInvisble()) {
									if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
										pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										pc.sendPackets(new S_Sound(147));
									}
									if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
										pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
									}
								}
								pc.removeSkillEffect(STATUS_FREEZE);
								getTreePotionBuff(pc);
							}
						}
					}
						break;
					case RINDVIOR_WEAPON:
					case RINDVIOR_WEAPON_2: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1ItemInstance weapon = pc.getWeapon();
							Random random = new Random();
							int rnd = random.nextInt(100) + 1;
							if (weapon != null && rnd > 33) {
								int weaponDamage = random.nextInt(3) + 1;
								if (pc.isDead()) {
									continue;
								}
								pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()));
								pc.getInventory().receiveDamage(weapon, weaponDamage);
								pc.sendPackets(new S_SkillSound(pc.getId(), 172));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 172));
							}
						}
					}
						break;
					// 흑장로 데스 힐 / 캔슬레이션
					case BLACKELDER_DEATH_HELL: {
						Random random = new Random();
						int Chance = random.nextInt(100) + 1;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillSound(pc.getId(), 7780));
							pc.setSkillEffect(L1SkillId.PAP_DEATH_HELL, 12 * 1000);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7780));
						}
						if (Chance > 33) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (cha instanceof L1PcInstance) {
									((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), _skill.getCastGfx()));
								}
								cha.broadcastPacket(new S_SkillSound(cha.getId(), _skill.getCastGfx()));
								for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									if (skillNum == SHAPE_CHANGE) {
										if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
											continue;
										if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
											continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								pc.curePoison();
								pc.cureParalaysis();
								if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
									L1PolyMorph.undoPoly(pc);
									pc.sendPackets(new S_CharVisualUpdate(pc));
									Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
								}
								if (pc.getHasteItemEquipped() > 0) {
									pc.setMoveSpeed(0);
									pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
									Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
								}
								if (pc != null && pc.isInvisble()) {
									if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
										pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										pc.sendPackets(new S_Sound(147));
									}
									if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
										pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
									}
								}
								pc.removeSkillEffect(STATUS_FREEZE);
								pc.sendPackets(new S_SkillSound(pc.getId(), 870));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 870));
							}
						}
					}
						break;
					// 드레이크 매스텔레포트
					case DRAKE_MASSTELEPORT: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1Location newLocation = pc.getLocation().randomLocation(5, true);
							int newX = newLocation.getX();
							int newY = newLocation.getY();
							if (pc.isDead())
								continue;
							// L1Teleport.teleport(pc, newX, newY,
							// pc.getMapId(), pc.getHeading(), true);
							pc.start_teleport(newX, newY, pc.getMapId(), pc.getHeading(), 169, true, true);
						}
					}
						break;
					// 드레이크 윈드세클
					case DRAKE_WIND_SHACKLE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead()) {
								continue;
							}
							pc.setSkillEffect(L1SkillId.WIND_SHACKLE, 12 * 1000);
							if (pc.getCurrentWeapon() == 88) {
							} else {
								pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration));
							}
							pc.sendPackets(new S_SkillSound(pc.getId(), 1799));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 1799));
						}
					}
						break;
					// 흑장로 데스포션
					case BLACKELDER_DEATH_POTION: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillSound(pc.getId(), 7781));
							pc.setSkillEffect(L1SkillId.PAP_DEATH_PORTION, 12 * 1000);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7781));
						}
					}
						break;
					// 이프리트 서먼 몬스터
					case EFRETE_SUMMON_MONSTER: {
						Random _random = new Random();
						for (int i = 0; i < 2; i++) {
							L1SpawnUtil.spawn(_npc, 5121, _random.nextInt(3) + 8);
						}
					}
						break;
					// 피닉스 서먼 몬스터
					case PHOENIX_SUMMON_MONSTER: {
						Random _random = new Random();
						for (int i = 0; i < 2; i++) {
							L1SpawnUtil.spawn(_npc, 900177, _random.nextInt(3) + 8);
						}
					}
						break;
					// 피닉스 캔슬레이션
					case PHOENIX_CANCELLATION: {
						Random random = new Random();
						int Chance = random.nextInt(100) + 1;
						if (Chance > 33) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (cha instanceof L1PcInstance) {
									((L1PcInstance) cha).sendPackets(new S_SkillSound(((L1PcInstance) cha).getId(), 870));
								}
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 870));
								for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									if (skillNum == SHAPE_CHANGE) {
										if (pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))
											continue;
										if ((pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER) > 0) || (pc.getSkillEffectTimeSec(L1SkillId.POLY_RING_MASTER2) > 0))
											continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
									if (skillNum == STATUS_CHAT_PROHIBITED) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
									if (isNotCancelable(skillNum) && !pc.isDead()) {
										continue;
									}
									pc.removeSkillEffect(skillNum);
								}
								pc.curePoison();
								pc.cureParalaysis();
								if (!(pc.getRankLevel() >= 9 && PolyTable.getInstance().isRankingPoly(pc.getCurrentSpriteId()))) {
									L1PolyMorph.undoPoly(pc);
									pc.sendPackets(new S_CharVisualUpdate(pc));
									Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
								}
								if (pc.getHasteItemEquipped() > 0) {
									pc.setMoveSpeed(0);
									pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
									Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0));
								}
								if (pc != null && pc.isInvisble()) {
									if (pc.hasSkillEffect(L1SkillId.INVISIBILITY)) {
										pc.killSkillEffectTimer(L1SkillId.INVISIBILITY);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
										pc.sendPackets(new S_Sound(147));
									}
									if (pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
										pc.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
										pc.sendPackets(new S_Invis(pc.getId(), 0));
										Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0));
									}
								}
								pc.removeSkillEffect(STATUS_FREEZE);
								getTreePotionBuff(pc);
							}
						}
					}
						break;
					case AREA_OF_SILENCE: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							int attackLevel = _user.getLevel();
							int defenseLevel = cha.getLevel();
							int probability = 0;
							if (attackLevel >= defenseLevel) {
								probability = (int) ((attackLevel - defenseLevel) * 5) + 50;
							} else if (attackLevel < defenseLevel) {
								probability = (int) ((attackLevel - defenseLevel) * 6) + 50;
							}
							if (probability > 90) {
								probability = 90;
							}
							if (random.nextInt(100) < probability) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, pc, 6, 4));
								Broadcaster.broadcastPacket(_target, new S_PacketBox(S_PacketBox.POSION_ICON, pc, 6, 4));
							} else {
								continue;
							}
						}
					}
						break;
					case DESERT_SKILL1: { // 광역 커스 패럴라이즈
						if (!cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE) && !cha.hasSkillEffect(DESERT_SKILL1)
								&& !cha.hasSkillEffect(DESERT_SKILL2)) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead())
									continue;
								L1CurseParalysis.curse(pc, _skillId);
							}
						}
					}
						break;
					case DESERT_SKILL2: { // 광역 어스 바인드
						if (!cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE) && !cha.hasSkillEffect(DESERT_SKILL1)
								&& !cha.hasSkillEffect(DESERT_SKILL2)) {
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.isDead())
									continue;

								pc.setSkillEffect(EARTH_BIND, 12 * 1000); // 디케이포션
								pc.sendPackets(new S_Poison(pc.getId(), 2));
								pc.broadcastPacket(new S_Poison(pc.getId(), 2));
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));

								pc.sendPackets(new S_SkillSound(pc.getId(), 2251));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2251));
							}
						}
					}
						break;
					case DESERT_SKILL3: { // 광역 마나 드레인
						int ranMp = random.nextInt(20);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getCurrentMp() <= ranMp || pc.isDead())
								continue;
							pc.setCurrentMp(pc.getCurrentMp() - ranMp);
							pc.sendPackets(new S_SkillSound(pc.getId(), 2172));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2172));
						}
					}
						break;
					case DESERT_SKILL4: {
						Random random = new Random();
						int PoisonTime = random.nextInt(5) + 1;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (PoisonTime > 2)
								L1DamagePoison.doInfection(_user, pc, PoisonTime * 1000, 500, _skillId == TOMAHAWK);
						}
					}
						break;
					case ZENITH_Poison: {
						Random random = new Random();
						int PoisonTime = random.nextInt(5) + 1;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (PoisonTime > 2)
								L1DamagePoison.doInfection(_user, pc, PoisonTime * 1000, 500, _skillId == TOMAHAWK);
						}
					}
						break;
					case DESERT_SKILL5: { // 커스/디케이/다크니스/디지즈/위크니스
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(CURSE_PARALYZE)) {
								continue;
							}
							L1CurseParalysis.curse(cha, _skillId); // 커스 패럴라이즈
							pc.setSkillEffect(CURSE_PARALYZE, 4 * 1000); // 커스
							// 패럴라이즈
							pc.sendPackets(new S_SkillSound(pc.getId(), 10704));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 10704));
						}
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(DECAY_POTION)) {
								continue;
							}
							pc.setSkillEffect(DECAY_POTION, 16 * 1000); // 디케이
							// 포션
							pc.sendPackets(new S_SkillSound(pc.getId(), 2232));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2232));
						}
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(DARKNESS)) {
								continue;
							}
							if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
								pc.sendPackets(new S_CurseBlind(2));
							} else {
								pc.sendPackets(new S_CurseBlind(1));
							}
							pc.setSkillEffect(DARKNESS, 32 * 1000); // 다크니스
							pc.sendPackets(new S_SkillSound(pc.getId(), 2175));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2175));
						}
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(DISEASE)) {
								continue;
							}
							pc.addDmgup(-6);
							pc.getAC().addAc(12);
							pc.setSkillEffect(DISEASE, 64 * 1000); // 디지즈
							pc.sendPackets(new S_SkillSound(pc.getId(), 2230));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2230));
						}
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(WEAKNESS)) {
								continue;
							}
							pc.addDmgup(-5);
							pc.addHitup(-1);
							pc.setSkillEffect(WEAKNESS, 64 * 1000); // 위크니스
							pc.sendPackets(new S_SkillSound(pc.getId(), 2228));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2228));
						}
					}
						break;
					case DESERT_SKILL6: { // 광역 다크니스
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(DARKNESS)) {
								continue;
							}
							if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
								pc.sendPackets(new S_CurseBlind(2));
							} else {
								pc.sendPackets(new S_CurseBlind(1));
							}
							pc.setSkillEffect(DARKNESS, 32 * 1000); // 다크니스
							pc.sendPackets(new S_SkillSound(pc.getId(), 2175));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 2175));
						}
					}
						break;
					case DESERT_SKILL7: { // 광역 포그
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(FOG_OF_SLEEPING)) {
								continue;
							}
							pc.setSkillEffect(FOG_OF_SLEEPING, 32 * 1000); // 포그
							// 오브
							// 슬리핑
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true));
							pc.sendPackets(new S_SkillSound(pc.getId(), 760));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 760));
						}
						cha.setSleeped(true);
					}
						break;
					case OMAN_ERASE_MAGIC: { // 광역 다크니스
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.isDead() || pc.hasSkillEffect(ERASE_MAGIC)) {
								continue;
							}
							
							pc.setSkillEffect(ERASE_MAGIC, 32 * 1000); // 다크니스
							pc.sendPackets(new S_SkillSound(pc.getId(), 15102));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(pc.getId(), 15102));
						}
					}
					case DESERT_SKILL8: { // 에르자베 토네이도 대미지
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillSound(pc.getId(), 10082));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 10082));
						}
					}
						break;
					case DESERT_SKILL9: { // 에르자베 서먼 몬스터
						for (int i = 0; i < 4; i++) {
							L1SpawnUtil.spawn(_npc, 5138, 6, 120 * 1000); // 그라카스
							L1SpawnUtil.spawn(_npc, 5139, 6, 120 * 1000); // 베이카스
							L1SpawnUtil.spawn(_npc, 5140, 6, 120 * 1000); // 호루카스
							L1SpawnUtil.spawn(_npc, 5141, 6, 120 * 1000); // 아르카스
							L1SpawnUtil.spawn(_npc, 5142, 6, 120 * 1000); // 여왕
							// 수호
							// 개미
							L1SpawnUtil.spawn(_npc, 5143, 6, 120 * 1000); // 여왕
							// 수호
							// 개미
							L1SpawnUtil.spawn(_npc, 5144, 6, 120 * 1000); // 여왕
							// 수호
							// 개미
							L1SpawnUtil.spawn(_npc, 5145, 6, 120 * 1000); // 여왕
							// 수호
							// 개미
						}
					}
						break;
					case DESERT_SKILL10: { // 에르자베 모래 폭풍
						for (int i = 0; i < random.nextInt(3) + 1; i++) {
							L1SpawnUtil.spawn(_npc, 5095, 6, 3 * 1000); // 모래 폭풍
						}
					}
						break;
					/*
					 * case WIDE_ARMORBREAK: { // 광역 아머 브레이크 if(cha != null &&
					 * cha instanceof L1PcInstance){ L1PcInstance pc =
					 * (L1PcInstance)cha; if (pc.isDead()) continue;
					 * SC_SPELL_BUFF_NOTI noti =
					 * SC_SPELL_BUFF_NOTI.newInstance();
					 * noti.set_noti_type(eNotiType.RESTAT);
					 * noti.set_spell_id(L1SkillId.WIDE_ARMORBREAK);
					 * noti.set_duration(8);//시간
					 * noti.set_duration_show_type(eDurationShowType.
					 * TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
					 * noti.set_on_icon_id(4473); noti.set_off_icon_id(4473);
					 * noti.set_icon_priority(0); noti.set_tooltip_str_id(3141);
					 * noti.set_new_str_id(0); noti.set_end_str_id(2238);
					 * noti.set_is_good(false); pc.sendPackets(noti,
					 * MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
					 * //Broadcaster.broadcastPacket(pc, new
					 * S_SkillSound(pc.getId(), 3400)); // 100 // 1 -> 100 //
					 * 100 -> 99 } } break;
					 */
					case BOSS_COUNTER_BARRIER: { // 보스 카운터배리어
						if (cha != null && cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.send_effect(10709);
						}
					}
						break;
					default:
						break;
					}
				}

				if (_calcType == PC_NPC || _calcType == NPC_NPC) {
					if (_skillId == TAMING_MONSTER && ((L1MonsterInstance) cha).getNpcTemplate().isTamable()) {
						int petcost = 0;
						Object[] petlist = _user.getPetList().values().toArray();
						for (Object pet : petlist) {
							petcost += ((L1NpcInstance) pet).getPetcost();
						}
						int charisma = _user.getAbility().getTotalCha();
						if (_player.isElf()) {
							charisma += 12;
						} else if (_player.isWizard()) {
							charisma += 6;
						}
						charisma -= petcost;
						if (charisma >= 6) {
							L1SummonInstance summon = new L1SummonInstance(_targetNpc, _user, false);
							_target = summon;
						} else {
							_player.sendPackets(new S_ServerMessage(319));
						}
					} else if (_skillId == CREATE_ZOMBIE) {
						int petcost = 0;
						Object[] petlist = _user.getPetList().values().toArray();
						for (Object pet : petlist) {
							petcost += ((L1NpcInstance) pet).getPetcost();
						}
						int charisma = _user.getAbility().getTotalCha();
						if (_player.isElf()) {
							charisma += 12;
						} else if (_player.isWizard()) {
							charisma += 6;
						}
						charisma -= petcost;
						if (charisma >= 6) {
							L1SummonInstance summon = new L1SummonInstance(_targetNpc, _user, true);
							_target = summon;
						} else {
							_player.sendPackets(new S_ServerMessage(319));
						}
					} else if (_skillId == WEAK_ELEMENTAL) {
						if (cha instanceof L1MonsterInstance) {
							L1Npc npcTemp = ((L1MonsterInstance) cha).getNpcTemplate();
							int weakAttr = npcTemp.get_weakAttr();
							if ((weakAttr & 1) == 1) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2169)); // 땅
							}
							if ((weakAttr & 2) == 2) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2167)); // 물
							}
							if ((weakAttr & 4) == 4) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2166)); // 불
							}
							if ((weakAttr & 8) == 8) {
								cha.broadcastPacket(new S_SkillSound(cha.getId(), 2168)); // 바람
							}
						}
					} else if (_skillId == RETURN_TO_NATURE) {
						if (Config.RETURN_TO_NATURE && cha instanceof L1SummonInstance) {
							L1SummonInstance summon = (L1SummonInstance) cha;
							summon.broadcastPacket(new S_SkillSound(summon.getId(), 2245));
							summon.returnToNature();
						} else {
							if (_user instanceof L1PcInstance) {
								_player.sendPackets(new S_ServerMessage(79));
							}
						}
					} else if (_skillId == TOMAHAWK) { // 토마호크 지속 시간 동안 출혈 상태가
						// 되어 대미지를 입는다. 레벨*2/6
						L1DamagePoison.doInfection(_user, cha, 1000, _user.getLevel() * 2 / 6, true);
					} else if (_skillId == POWERRIP) { // 전사스킬 파워그립
						int[] PowerRipTimeArray = { 1000, 2000, 3000, 4000 };
						int rnd = random.nextInt(PowerRipTimeArray.length);
						_PowerRipDuration = PowerRipTimeArray[rnd];
						L1EffectSpawn.getInstance().spawnEffect(9415, _PowerRipDuration, _target.getX(), _target.getY(), _target.getMapId());
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							_target.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, true));
						} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							npc.setSkillEffect(L1SkillId.POWERRIP, _PowerRipDuration);
							npc.set발묶임상태(true);
						}
					} else if (_skillId == DESPERADO) { // 전사스킬 데스페라도
						int[] stunTimeArray = null;
						int effectNpcId = 9416;
						if (_player != null && _player.isPassive(MJPassiveID.DESPERADO_ABSOLUTE.toInt())) {
							stunTimeArray = new int[] { 1500, 1700, 1900, 2200, 2500, 3000, 4000, 4500, 5000, 5500, 6000 };
							effectNpcId = 9418;
							// _target.sendPackets(new
							// S_SkillSound(_target.getId(), 17235));
							// Broadcaster.broadcastPacket(_target, new
							// S_SkillSound(_target.getId(), 17235));
						} else {
							stunTimeArray = new int[] { 1500, 1700, 1900, 2200, 2500, 3000, 4000 };
						}

						int rnd = random.nextInt(stunTimeArray.length);
						_shockStunDuration = stunTimeArray[rnd];
						L1EffectInstance effectInstance = L1EffectSpawn.getInstance().spawnEffect(effectNpcId, _shockStunDuration, _target.getX(),
								_target.getY(), _target.getMapId());
						effectInstance.broadcastPacket(new S_MoveCharPacket(effectInstance));
						if (_target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) _target;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, true));
							_target.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);

						} else if (_target instanceof L1MonsterInstance || _target instanceof L1SummonInstance || _target instanceof L1PetInstance || _target instanceof MJCompanionInstance) {
							L1NpcInstance npc = (L1NpcInstance) _target;
							npc.setSkillEffect(L1SkillId.DESPERADO, _shockStunDuration);
							npc.set발묶임상태(true);
						}
					}
				}

				if (_skill.getType() == L1Skills.TYPE_HEAL && _calcType == PC_NPC && undeadType == 1) {
					dmg *= -1;
				}

				if (_skill.getType() == L1Skills.TYPE_HEAL && _calcType == PC_NPC && undeadType == 3) {
					dmg = 0;
				}

				if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) && dmg < 0) {
					dmg = 0;
				}

				if (dmg != 0 || drainMana != 0) {
					/** 디스 중첩불가 소스 **/
					if (_skillId == DISINTEGRATE && dmg > 0) {
						if (dmg > Config.DISINT_MAX_DMG)
							dmg = Config.DISINT_MAX_DMG;
						else if (dmg < Config.DISINT_MIN_DMG)
							dmg = Config.DISINT_MIN_DMG;
					}
					// 포우는 무시하기
					if (_skillId != FOU_SLAYER)
						_magic.commit(dmg, drainMana);
				}

				if (heal > 0) {
					if ((heal + _user.getCurrentHp()) > _user.getMaxHp()) {
						_user.setCurrentHp(_user.getMaxHp());
					} else {
						_user.setCurrentHp(heal + _user.getCurrentHp());
					}
				}

				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_OwnCharStatus(pc));
					sendHappenMessage(pc);
				}

				addMagicList(cha, false);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
				}
			}

			if (_skillId == DETECTION || _skillId == IZE_BREAK || _skillId == EYE_OF_DRAGON || _skillId == COUNTER_DETECTION) {// 캔슬
				detection(_player, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(String.format("스킬오류 : %s %s %s %d", _player == null ? "캐릭터이름" : _player.getName(), _npc == null ? "" : _npc.getName(),
					_target == null ? "" : _target.getName(), _skillId));
		}
	}

	private void summonMonster(L1PcInstance pc, int order) {
		int summonid = 0;
		int level = 0;
		int cha = 0;
		if (order >= 1 && order <= 25)
			cha = 25;
		else if (order >= 31 && order <= 55)
			cha = 35;

		// if(pc.getAbility().getCha() < cha) //순수 카리 기준
		// return;
		if (pc.getAbility().getTotalCha() < cha) // 토탈 카리 기준(템포함)
			return;

		switch (order) {
		case 1:
			summonid = 7320137;
			level = 28;
			break;
		case 2:
			summonid = 7320138;
			level = 28;
			break;
		case 7:
			summonid = 7320139;
			level = 40;
			break;
		case 13:
			summonid = 7320140;
			level = 52;
			break;
		case 19:
			summonid = 7320141;
			level = 64;
			break;
		case 25:
			summonid = 7320142;
			level = 76;
			break;
		case 31:
			summonid = 7320143;
			level = 80;
			break;
		case 37:
			summonid = 7320144;
			level = 82;
			break;
		case 43:
			summonid = 7320145;
			level = 84;
			break;
		case 49:
			summonid = 7320146;
			level = 86;
			break;
		case 55:
			summonid = 7320147;
			level = 88;
			break;
		}

		if (pc.getLevel() < level) {
			pc.sendPackets(new S_ServerMessage(743));
			return;
		}

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
		if (npcTemp == null) {
			System.out.println("npcTemp = null " + summonid);
		}
		L1SummonInstance summon = null;
		summon = new L1SummonInstance(npcTemp, pc);
		summon.setPetcost(0);
	}

	/** 3단 가속 버프창 띄우기 */
	private void getTreePotionBuff(L1PcInstance target) {
		if (target.hasSkillEffect(STATUS_DRAGON_PEARL)) {
			int reminingtime = target.getSkillEffectTimeSec(STATUS_DRAGON_PEARL);
			target.sendPackets(new S_Liquor(target.getId(), 8));
			target.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 8, reminingtime));
			target.setPearl(1);
		}
	}

	/** 캔슬로 해제할 수 없는 스킬인지를 돌려준다. */
	public static boolean isNotCancelable(int skillNum) {
		return skillNum == ABSOLUTE_BARRIER || skillNum == ADVANCE_SPIRIT || skillNum == SHOCK_STUN || skillNum == REDUCTION_ARMOR || skillNum == SOLID_CARRIAGE
				|| skillNum == COUNTER_BARRIER || skillNum == COMA_A || skillNum == COMA_B || skillNum == ANTA_MAAN || skillNum == FAFU_MAAN || skillNum == MAJESTY
				|| skillNum == EXP_BUFF || skillNum == LIND_MAAN || skillNum == VALA_MAAN || skillNum == BIRTH_MAAN || skillNum == SHAPE_MAAN 
				|| skillNum == LIFE_MAAN || skillNum == ANTA_BUFF || skillNum == FAFU_BUFF || skillNum == ANTA_MESSAGE_6 || skillNum == ANTA_MESSAGE_7 
				|| skillNum == ANTA_MESSAGE_8 || skillNum == PREDICATEDELAY || skillNum == FEATHER_BUFF_A || skillNum == FEATHER_BUFF_B
				|| skillNum == FEATHER_BUFF_C || skillNum == FEATHER_BUFF_D || skillNum == PAP_DEATH_PORTION || skillNum == PAP_DEATH_HELL
				|| skillNum == PAP_REDUCE_HELL || skillNum == STATUS_DRAGON_PEARL || skillNum == UNCANNY_DODGE || skillNum == DRESS_EVASION
				|| skillNum == SHADOW_ARMOR || skillNum == OMAN_STUN || skillNum == Maeno_STUN || skillNum == SCALES_EARTH_DRAGON
				|| skillNum == SCALES_WATER_DRAGON || skillNum == SCALES_FIRE_DRAGON || skillNum == SCALES_RINDVIOR_DRAGON 
				|| skillNum == COOK_STR || skillNum == COOK_DEX || skillNum == COOK_INT ||  skillNum == COOK_GROW 
						|| skillNum == COOK_STR_12 || skillNum == COOK_STR_1 || skillNum == COOK_DEX_1 || skillNum == COOK_INT_1 ||  skillNum == COOK_GROW_1 
				|| skillNum == ARMOR_BRAKE || skillNum == SHADOW_FANG || skillNum == CLANBUFF_YES
				|| skillNum == God_buff || skillNum == DESPERADO || skillNum == MIRROR_IMAGE || skillNum == TOP_RANKER
				|| skillNum == 정상의가호 || skillNum == GIGANTIC || skillNum == 메티스정성스프 || skillNum == 메티스정성요리 || skillNum == 메티스축복주문서
				|| skillNum == POLY_RING_MASTER || skillNum == POLY_RING_MASTER2 || skillNum == RANK_BUFF_1 || skillNum == RANK_BUFF_2 || skillNum == RANK_BUFF_3 || skillNum == RANK_BUFF_4
				|| skillNum == RANK_BUFF_5 || skillNum == RANK_BUFF_6 || skillNum == RANK_BUFF_7 || skillNum == RANK_BUFF_8 || skillNum == RANK_BUFF_9
				|| skillNum == RANK_BUFF_10_STR || skillNum == RANK_BUFF_10_DEX || skillNum == RANK_BUFF_10_INT || skillNum == RANK_BUFF_11_STR
				|| skillNum == RANK_BUFF_11_DEX || skillNum == RANK_BUFF_11_INT;
	}

	private void detection(L1PcInstance pc, boolean detectAll) {
		if (pc == null) {
			return;
		}

		if (!pc.isGmInvis() && pc.isInvisble() && !pc.isGhost()) {
			unequipInvisItem(pc);

			pc.delInvis();
			pc.beginInvisTimer();
		}

		if (detectAll) {
			for (L1PcInstance tgt : L1World.getInstance().getVisiblePlayer(pc)) {
				if (!tgt.isGmInvis() && tgt.isInvisble() && !pc.isGhost()) {
					unequipInvisItem(tgt);
					tgt.delInvis();
				}
			}

			L1WorldTraps.getInstance().onDetection(pc);
		}
	}

	private void unequipInvisItem(L1PcInstance pc) {
		if (pc == null) {
			return;
		}

		L1ItemInstance invisItem = pc.getInventory().getEquippedItem(20077);
		if (invisItem != null) {
			pc.getInventory().setEquipped(invisItem, false);
		}

		invisItem = pc.getInventory().getEquippedItem(20062);
		if (invisItem != null) {
			pc.getInventory().setEquipped(invisItem, false);
		}

		invisItem = pc.getInventory().getEquippedItem(120077);
		if (invisItem != null) {
			pc.getInventory().setEquipped(invisItem, false);
		}
	}

	private boolean isTargetCalc(L1Character cha) {
		if (_skill.getTarget().equals("attack") && _skillId != 18) {
			if (isPcSummonPet(cha)) {
				if (_player.getZoneType() == 1 || cha.getZoneType() == 1 || _player.checkNonPvP(_player, cha)) {
					return false;
				}
			}
		}

		if (_skillId == FOG_OF_SLEEPING && _user.getId() == cha.getId()) {
			return false;
		}

		/*
		 * if (_skillId == GREATER_SLOW) { if (_user.getId() == cha.getId()) {
		 * return false; } if (cha instanceof L1SummonInstance) {
		 * L1SummonInstance summon = (L1SummonInstance) cha; if (_user.getId()
		 * == summon.getMaster().getId()) { return false; } } else
		 */ 
		if (cha instanceof L1PetInstance) {
			L1PetInstance pet = (L1PetInstance) cha;
			if (_user.getId() == pet.getMaster().getId()) {
				return false;
			}
		}
		if(cha instanceof MJCompanionInstance){
			if(_user.getId() == ((MJCompanionInstance)cha).get_master_id())
				return false;
		}
		// }

		if (_skillId == MASS_TELEPORT) {
			if (_user.getId() != cha.getId()) {
				return false;
			}
		}

		return true;
	}

	private boolean isPcSummonPet(L1Character cha) {
		if (_calcType == PC_PC) {
			return true;
		}

		if (_calcType == PC_NPC) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.isExsistMaster()) {
					return true;
				}
			}
			if (cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				return true;
			}
			if (cha instanceof L1SupportInstance) {
				return true;
			}
		}
		return false;
	}

	private boolean isUseCounterMagic(L1Character cha) {
		if (_isCounterMagic && cha.hasSkillEffect(COUNTER_MAGIC)) {
			cha.removeSkillEffect(COUNTER_MAGIC);
			cha.broadcastPacket(new S_SkillSound(cha.getId(), 10702));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), 10702));
			}
			return true;
		}
		return false;
	}

	private boolean isTargetFailure(L1Character cha) {
		boolean isTU = false;
		boolean isErase = false;
		boolean isManaDrain = false;
		int undeadType = 0;

		if (cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) {
			return true;
		}

		if (cha instanceof L1PcInstance) {
			if (_calcType == PC_PC && _player.checkNonPvP(_player, cha)) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (_player.getId() == pc.getId() || (pc.getClanid() != 0 && _player.getClanid() == pc.getClanid())) {
					return false;
				}
				return true;
			}
			return false;
		}

		if (cha instanceof L1MonsterInstance) {
			isTU = ((L1MonsterInstance) cha).getNpcTemplate().get_IsTU();
		}

		if (cha instanceof L1MonsterInstance) {
			isErase = ((L1MonsterInstance) cha).getNpcTemplate().get_IsErase();
		}

		if (cha instanceof L1MonsterInstance) {
			undeadType = ((L1MonsterInstance) cha).getNpcTemplate().get_undead();
		}

		if (cha instanceof L1MonsterInstance) {
			isManaDrain = true;
		}
		if ((_skillId == TURN_UNDEAD && (undeadType == 0 || undeadType == 2))
				|| (_skillId == TURN_UNDEAD && isTU == false) || ((_skillId == ERASE_MAGIC || _skillId == SLOW || _skillId == MOB_SLOW_1
						|| _skillId == MOB_SLOW_18 || _skillId == MANA_DRAIN || _skillId == WIND_SHACKLE) && isErase == false)
				|| (_skillId == MANA_DRAIN && isManaDrain == false)) {
			return true;
		}
		return false;
	}

	public void removeNewIcon(L1PcInstance pc, int skillid) {
		switch (skillid) {
		case ABSOLUTE_BLADE:
		case DEATH_HEAL:
		case DEATH_HEAL_Mob:
		case SOUL_BARRIER:
		case IMPACT:
		case TITANL_RISING:
		case GRACE_AVATAR:
			pc.removeSkillEffect(skillid);
			break;
		default:
			break;
		}
	}

	public static void on_icons(L1PcInstance pc, int skillId, int duration) {
		SC_SPELL_BUFF_NOTI noti = null;
		switch (skillId) {
		case L1SkillId.HOLY_WEAPON:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.HOLY_WEAPON);
			noti.set_duration(duration);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(777);
			noti.set_off_icon_id(777);
			noti.set_tooltip_str_id(685);
			noti.set_new_str_id(685);
			noti.set_end_str_id(718);
			noti.set_is_good(true);
			break;
		case L1SkillId.ENCHANT_WEAPON:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.ENCHANT_WEAPON);
			noti.set_duration(duration);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(5265);
			noti.set_off_icon_id(5265);
			noti.set_tooltip_str_id(3064);
			noti.set_end_str_id(2239);
			noti.set_is_good(true);
			break;
		case L1SkillId.BLESS_WEAPON:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.BLESS_WEAPON);
			noti.set_duration(duration);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(728);
			noti.set_off_icon_id(728);
			noti.set_tooltip_str_id(857);
			noti.set_new_str_id(857);
			noti.set_end_str_id(2167);
			noti.set_is_good(true);
			break;
		case L1SkillId.SHADOW_FANG:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.SHADOW_FANG);
			noti.set_duration(duration);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(1128);
			noti.set_off_icon_id(1128);
			noti.set_tooltip_str_id(1339);
			noti.set_new_str_id(1339);
			noti.set_end_str_id(2234);
			noti.set_is_good(true);
			break;
		case L1SkillId.BLESSED_ARMOR:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.BLESSED_ARMOR);
			noti.set_duration(duration);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(5316);
			noti.set_off_icon_id(5316);
			noti.set_tooltip_str_id(3060);
			noti.set_new_str_id(3060);
			noti.set_end_str_id(2240);
			noti.set_is_good(true);
			break;
		case L1SkillId.GRACE_AVATAR:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.GRACE_AVATAR);
			noti.set_duration(15);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_icon_priority(10);
			noti.set_on_icon_id(7427);
			noti.set_off_icon_id(7427);
			noti.set_tooltip_str_id(4734);
			noti.set_new_str_id(4734);
			noti.set_end_str_id(4741);
			noti.set_is_good(true);
			break;
		default:
			break;
		}
		if (noti != null)
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}
	
	public static void off_icons(L1PcInstance pc, int skillId) {
		SC_SPELL_BUFF_NOTI noti = null;
		switch (skillId) {
		case L1SkillId.HOLY_WEAPON:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(HOLY_WEAPON);
			noti.set_duration(0);
			noti.set_end_str_id(3919);
			noti.set_is_good(true);
			break;
		case L1SkillId.ENCHANT_WEAPON:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.ENCHANT_WEAPON);
			noti.set_duration(0);
			noti.set_end_str_id(2239);
			noti.set_is_good(true);
			break;
		case L1SkillId.BLESS_WEAPON: {
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.BLESS_WEAPON);
			noti.set_duration(0);
			noti.set_off_icon_id(2167);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
		}
			break;
		case L1SkillId.SHADOW_FANG:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.SHADOW_FANG);
			noti.set_duration(0);
			noti.set_off_icon_id(1128);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
			break;
		case L1SkillId.BLESSED_ARMOR:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(BLESSED_ARMOR);
			noti.set_duration(0);
			noti.set_end_str_id(2240);
			noti.set_is_good(true);
			break;
		case L1SkillId.GRACE_AVATAR:
			noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.GRACE_AVATAR);
			noti.set_duration(0);
			noti.set_off_icon_id(7427);
			noti.set_end_str_id(4741);
			noti.set_is_good(true);
			break;
		default:
			break;
		}
		
		if(noti != null)
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}
	
	public static void remove_skills(L1PcInstance pc, Collection<Integer> skills) {
		for(Integer skillId : skills) {
			if(pc.hasSkillEffect(skillId))
				pc.removeSkillEffect(skillId);
		}
	}
	
	public static void remove_skills(L1PcInstance pc, Integer[] skills) {
		for(Integer skillId : skills) {
			if(pc.hasSkillEffect(skillId)) {
				pc.removeSkillEffect(skillId);
			}
		}
	}
	
	public static boolean check_skills(L1PcInstance pc, Integer[] skills) {
		for(Integer skillId : skills) {
			if(pc.hasSkillEffect(skillId))
				return true;
		}
		return false;
	}
}

