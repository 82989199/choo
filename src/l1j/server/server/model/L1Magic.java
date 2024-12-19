package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.Random;

import com.sun.glass.ui.Window.Level;

import l1j.server.Config;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.datatables.CharacterReduc;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.datatables.UserProtectMonsterTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.templates.L1SpecialMap;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.MJCommons;

public class L1Magic {

	private int _calcType;

	private final int PC_PC = 1;

	private final int PC_NPC = 2;

	private final int NPC_PC = 3;

	private final int NPC_NPC = 4;

	private L1PcInstance _pc = null;

	private L1PcInstance _targetPc = null;

	private L1NpcInstance _npc = null;

	private L1NpcInstance _targetNpc = null;

	private int _leverage = 10;

	private static Random _random = new Random(System.nanoTime());

	public boolean _CriticalDamage = false;

	public boolean isCriticalDamage() {
		return _CriticalDamage;
	}

	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	private double _simsimLeverage = 1;

	public void setSimSimLeverge(double i) {
		_simsimLeverage = i;
	}

	private double getSimSimLeverage() {
		return _simsimLeverage;
	}

	public L1Magic(L1Character attacker, L1Character target) {
		if (attacker instanceof L1PcInstance) {
			if (target instanceof L1PcInstance) {
				_calcType = PC_PC;
				_pc = (L1PcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = PC_NPC;
				_pc = (L1PcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		} else {
			if (target instanceof L1PcInstance) {
				_calcType = NPC_PC;
				_npc = (L1NpcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = NPC_NPC;
				_npc = (L1NpcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		}
	}
	
	private int getSpellPower() {
		int spellPower = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			spellPower = _pc.getAbility().getSp();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			spellPower = _npc.getAbility().getSp();
		}
		return spellPower;
	}

	private int getMagicLevel() {
		int magicLevel = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicLevel = _pc.getAbility().getMagicLevel();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicLevel = _npc.getAbility().getMagicLevel();
		}
		return magicLevel;
	}

	private int getMagicBonus() {
		int magicBonus = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicBonus = _pc.getAbility().getMagicBonus();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicBonus = _npc.getAbility().getMagicBonus();
		}
		return magicBonus;
	}

	private int getLawful() {
		int lawful = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			lawful = _pc.getLawful();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			lawful = _npc.getLawful();
		}
		return lawful;
	}

	private int getTargetMr() {
		int mr = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			mr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			mr = _targetNpc.getResistance().getEffectedMrBySkill();
		}
		return mr;
	}

	/* ■■■■■■■■■■■■■■ 성공 판정[명중] ■■■■■■■■■■■■■ */
	// ●●●● 확률계 마법의 성공 판정 ●●●●
	// 계산방법
	// 공격측 포인트：LV + ((MagicBonus * 3) * 마법 고유 계수)
	// 방어측 포인트：((LV / 2) + (MR * 3)) / 2
	// 공격 성공율：공격측 포인트 - 방어측 포인트
	public boolean calcProbabilityMagic(int skillId) {
		double probability = 0;
		boolean isSuccess = false;

		if (_pc != null && _pc.isGm()) {
			return true;
		}

		if ((_calcType == PC_PC || _calcType == NPC_PC) && _targetPc != null) {
			if (_targetPc.getMagicDodgeProbability() > 0) {
				if (MJRnd.isWinning(100, _targetPc.getMagicDodgeProbability())) {
					S_OnlyEffect eff = new S_OnlyEffect(_targetPc.getId(), 10702);
					_targetPc.sendPackets(eff, false);
					_targetPc.broadcastPacket(eff);
					return false;
				}
			}
		}

		if (_calcType == PC_NPC && _targetNpc != null) {//
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId == 8500138)
				return false;
			if (npcId >= 45912 && npcId <= 45915 && !_pc.hasSkillEffect(STATUS_HOLY_WATER)) {
				return false;
			}
			if (npcId == 45916 && !_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
				return false;
			}
			if (npcId == 45941 && !_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
				return false;
			}
//			if (npcId == 45752 && !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
//				return false;
//			}
//			if (npcId == 45753 && !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
//				return false;
//			}
//			if (npcId == 45675 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//				return false;
//			}
//			if (npcId == 81082 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//				return false;
//			}
//			if (npcId == 45625 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//				return false;
	//		}
		/*	if (npcId == 45674 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
				return false;
			}
			if (npcId == 45685 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
				return false;
			}*/
			if (npcId >= 46068 && npcId <= 46091 && _pc.getCurrentSpriteId() == 6035) {
				return false;
			}
			if (npcId >= 46092 && npcId <= 46106 && _pc.getCurrentSpriteId() == 6034) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
				return false;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7720) {
				return false;
			}
		}

		if (_calcType == NPC_NPC) {
			if (_targetNpc.getNpcTemplate().get_npcId() == 8500138)
				return false;
		}

		if (!checkZone(skillId)) {
			return false;
		}

		if (skillId == CANCELLATION) {
			if (_calcType == PC_PC && _pc != null && _targetPc != null) {

				if (_pc.getId() == _targetPc.getId()) {
					return true;
				}
				if (_pc.getClanid() > 0 && (_pc.getClanid() == _targetPc.getClanid())) {
					_targetPc.sendPackets(new S_SystemMessage("혈맹원 " + _pc.getName() + " 님이 캔슬레이션 마법을 시전했습니다."));
					return true;
				}
				if (_pc.isInParty()) {
					if (_pc.getParty().isMember(_targetPc)) {
						_targetPc.sendPackets(new S_SystemMessage("파티원 " + _pc.getName() + " 님이 캔슬레이션 마법을 시전했습니다."));
						return true;
					}
				}
				// 대상이 인비지 상태일땐 켄슬 무효
				if (_targetPc.isInvisble()) {
					return false;
				}

				if (_pc.getZoneType() == 1 || _targetPc.getZoneType() == 1) {
					return false;
				}
			}
			if (_calcType == PC_NPC || _calcType == NPC_PC || _calcType == NPC_NPC) {
				return true;
			}
		}

		// 50렙 이상 npc 에게 아래 마법 안걸림.
		/*
		 * if (_calcType == PC_NPC && _targetNpc.getLevel() >= 50 &&
		 * _targetNpc.getNpcTemplate().isCantResurrect()) { if ( skillId ==
		 * WEAPON_BREAK || skillId == SLOW || skillId == CURSE_PARALYZE ||
		 * skillId == MANA_DRAIN || skillId == WEAKNESS || skillId == DISEASE ||
		 * skillId == DECAY_POTION || skillId == GREATER_SLOW || skillId ==
		 * QUAKE || skillId == ERASE_MAGIC || skillId == AREA_OF_SILENCE ||
		 * skillId == WIND_SHACKLE || skillId == STRIKER_GALE || skillId ==
		 * SHOCK_STUN || skillId == FOG_OF_SLEEPING || skillId == ICE_LANCE ||
		 * skillId == POLLUTE_WATER || skillId == ELEMENTAL_FALL_DOWN || skillId
		 * == RETURN_TO_NATURE || skillId == THUNDER_GRAB || skillId ==
		 * ARMOR_BRAKE || skillId == DARKNESS ) { return false; } }
		 */

		/*** 신규레벨보호 ***/
		// if (_calcType == PC_PC) {
		// if (_targetPc.getLevel() < Config.AUTO_REMOVELEVEL || _pc.getLevel()
		// < Config.AUTO_REMOVELEVEL && !_pc.isPinkName()) {
		// if (skillId != EXTRA_HEAL && skillId != HEAL && skillId !=
		// GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL
		// && skillId != NATURES_BLESSING) { // 버프계
		// _pc.sendPackets(new S_SystemMessage("신규보호로 상대방의 마법을 보호받고 있습니다"));
		// _targetPc.sendPackets(new S_SystemMessage("신규보호로 상대방의 마법을 보호받고
		// 있습니다"));
		// return false;
		// }
		// }
		// }
		/** 신규혈맹 공격안되게 **/
		if (_calcType == PC_PC) {
			if (_pc.getClanid() == Config.신규혈맹클랜 || _targetPc.getClanid() == Config.신규혈맹클랜 && !_pc.isPinkName()) {
				if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL
						&& skillId != NATURES_BLESSING) { // 버프계

					boolean attack_ok = false;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(_targetPc)) {
						if (!(obj instanceof L1MonsterInstance)) {
							continue;
						}

						if (obj instanceof L1MonsterInstance) {
							L1MonsterInstance mon = (L1MonsterInstance) obj;
							int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId());
							if (monid != 0) {
								attack_ok = true;
								break;
							}
						}
					}

					if (!attack_ok) {
						_pc.sendPackets(new S_SystemMessage("신규유저는 상대방의 마법을 보호받고 있습니다"));
						_targetPc.sendPackets(new S_SystemMessage("신규유저는 상대방의 마법을 보호받고 있습니다"));
						return false;
					}
				}
			}
		}

		if (_calcType == PC_NPC && (_targetNpc.getNpcId() == 5042)) {
			if (skillId == TAMING_MONSTER)
				return false;
		}

		// if (_calcType == PC_NPC && (_targetNpc.getNpcId() == 45684 ||
		// _targetNpc.getNpcId() == 45683 || _targetNpc.getNpcId() == 45682
		// || _targetNpc.getNpcId() == 45681 || _targetNpc.getNpcId() == 81163
		// || _targetNpc.getNpcId() == 81047 || _targetNpc.getNpcId() == 45653))
		// {
		// if (skillId == DARKNESS)
		// return false;
		// }

		// 아스바인드중은 WB, 왈가닥 세레이션 이외 무효
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			/*
			 * if (_calcType == PC_PC) { // 스턴중에 스턴실패 if
			 * (_targetPc.hasSkillEffect(SHOCK_STUN) ||
			 * _targetPc.hasSkillEffect(BONE_BREAK)) { if (skillId == SHOCK_STUN
			 * || skillId == BONE_BREAK) { return false; } } }
			 */
			if (_targetPc.hasSkillEffect(EARTH_BIND)) { //여기 추가 해야 어바 걸린 상태에서 스턴 안들어가는듯 EMPIRE
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION // 확률계
						&& skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL
						&& skillId != NATURES_BLESSING && skillId == MANA_DRAIN || skillId == CURSE_PARALYZE || skillId == THUNDER_GRAB
						|| skillId == ERASE_MAGIC || skillId == SHOCK_STUN || skillId == EARTH_BIND || skillId == BONE_BREAK
						|| skillId == EMPIRE) { // 버프계
					return false;
				}
			}
		} else {
			if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION) {
					return false;
				}
			}
		}

		if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			if ((skillId == SILENCE || skillId == AREA_OF_SILENCE) && (_targetNpc.getNpcId() == 45684 || _targetNpc.getNpcId() == 45683
					|| _targetNpc.getNpcId() == 45681 || _targetNpc.getNpcId() == 45682 || _targetNpc.getNpcId() == 900011 || _targetNpc.getNpcId() == 900012
					|| _targetNpc.getNpcId() == 900013 || _targetNpc.getNpcId() == 900038 || _targetNpc.getNpcId() == 900039 || _targetNpc.getNpcId() == 900040
					|| _targetNpc.getNpcId() == 5096 || _targetNpc.getNpcId() == 5097 || _targetNpc.getNpcId() == 5098 || _targetNpc.getNpcId() == 5099
					|| _targetNpc.getNpcId() == 5100)) {
				return false;
			}
		}

		// 100% 확률을 가지는 스킬
		if (skillId == MIND_BREAK || skillId == IllUSION_AVATAR) {
			return true;
		}

		probability = calcProbability(skillId);
		// int rnd = 0;
		// -- 에러가 뜬 이유 . int 형 에서
		int rnd = CommonUtil.random(100);
		switch (skillId) {
		case DARK_BLIND:
		case CANCELLATION:
		case DECAY_POTION:
		case SILENCE:
		case CURSE_PARALYZE:
		case SLOW:
		case WEAKNESS:
		case CURSE_POISON:
		case CURSE_BLIND:
		case WEAPON_BREAK:
		case MANA_DRAIN:
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (skillId == 44 && _targetPc.getMr() >= 150) { // -- 캔슬
					if (_targetPc.getMr() >= 200)
						probability = 0;
					else if (_targetPc.getMr() >= 190)
						probability = 0.5;
					else if (_targetPc.getMr() >= 188)
						probability = 0.6;
					else if (_targetPc.getMr() >= 186)
						probability = 0.7;
					else if (_targetPc.getMr() >= 184)
						probability = 0.8;
					else if (_targetPc.getMr() >= 182)
						probability = 0.9;
					else if (_targetPc.getMr() >= 180)
						probability = 1.0;
					else if (_targetPc.getMr() >= 178)
						probability = 1.1;
					else if (_targetPc.getMr() >= 176)
						probability = 1.2;
					else if (_targetPc.getMr() >= 174)
						probability = 1.3;
					else if (_targetPc.getMr() >= 172)
						probability = 1.4;
					else if (_targetPc.getMr() >= 170)
						probability = 1.5;
					else if (_targetPc.getMr() >= 168)
						probability = 1.6;
					else if (_targetPc.getMr() >= 166)
						probability = 1.7;
					else if (_targetPc.getMr() >= 164)
						probability = 1.8;
					else if (_targetPc.getMr() >= 162)
						probability = 1.9;
					else if (_targetPc.getMr() >= 160)
						probability = 2.0;
					else if (_targetPc.getMr() >= 158)
						probability = 2.5;
					else if (_targetPc.getMr() >= 156)
						probability = 3.0;
					else if (_targetPc.getMr() >= 154)
						probability = 3.5;
					else if (_targetPc.getMr() >= 152)
						probability = 4.0;
					else if (_targetPc.getMr() >= 150)
						probability = 4.5;
					// mr 150 미만 세부화 by 노딤
					else if (_targetPc.getMr() >= 140)
						probability = 5.0;
					else if (_targetPc.getMr() >= 130)
						probability = 5.5;
					else if (_targetPc.getMr() >= 120)
						probability = 6.0;
					else if (_targetPc.getMr() >= 110)
						probability = 6.5;
					else if (_targetPc.getMr() >= 100)
						probability = 7.0;
					else if (_targetPc.getMr() >= 90)
						probability = 7.5;
					else if (_targetPc.getMr() >= 80)
						probability = 8.0;
					else if (_targetPc.getMr() >= 70)
						probability = 8.5;
					else if (_targetPc.getMr() >= 60)
						probability = 9.0;
					else if (_targetPc.getMr() >= 50)
						probability = 10.0;

					// -- 캔슬이며, 상대방 마방이 150 이상이라면 probability =
					// CommonUtil.random(1, 5); //-- 확률 조정 바람 1 ~ 5
				} else {
					if (_calcType == PC_PC) {
						rnd += CommonUtil.random(_targetPc.getResistance().getEffectedMrBySkill() + 1);
					} else if (_calcType == PC_NPC) {
						rnd += CommonUtil.random(_targetNpc.getResistance().getEffectedMrBySkill() + 1);
					}
				}
			}
			break;
		default:
			// rnd = _random.nextInt(100) + 1;
			if (probability > 90)
				probability = 90;
			break;
		}

		if (probability >= rnd) { // 마법실패시에도 미쓰 뜨게
			isSuccess = true;
		} else {
			if (_calcType == NPC_PC || _calcType == PC_PC) {
				/*
				 * _targetPc.sendPackets(new S_SkillSound(_targetPc.getId(),
				 * 13418)); Broadcaster.broadcastPacket(_targetPc, new
				 * S_SkillSound(_targetPc.getId(), 13418));
				 */
				isSuccess = false;
			} else if (_calcType == PC_NPC) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 13418));
				// Broadcaster.broadcastPacket(_pc, new
				// S_SkillSound(_targetNpc.getId(), 13418));// 이거는 다른 사람도 보게...
				isSuccess = false;
			}
		}
		if (!isSuccess & skillId == TURN_UNDEAD) {
			if (_calcType == PC_NPC) {
				_targetNpc.setHate(_pc, 1);
				int ran = _random.nextInt(100) + 1;
				if (ran <= 50) {
					Broadcaster.broadcastPacket(_targetNpc, new S_SkillSound(_targetNpc.getId(), 8987));
					Broadcaster.broadcastPacket(_targetNpc, new S_SkillHaste(_targetNpc.getId(), 1, 0));
					_targetNpc.setMoveSpeed(1);
				}
			}
		}

		if (!Config.ALT_ATKMSG) {
			return isSuccess;
		}
		if (Config.ALT_ATKMSG) {
			if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
				return isSuccess;
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && !_targetPc.isGm()) {
				return isSuccess;
			}
		}

		String msg0 = "";
		String msg2 = "";
		String msg3 = "";
		String msg4 = "";

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			msg0 = _pc.getName();
		} else if (_calcType == NPC_PC) {
			msg0 = _npc.getName();
		}

		msg2 = "확률:" + probability + "%";
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			msg4 = _targetPc.getName();
		} else if (_calcType == PC_NPC) {
			msg4 = _targetNpc.getName();
		}
		if (isSuccess == true) {
			msg3 = "성공";
		} else {
			msg3 = "실패";
		}

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			_pc.sendPackets(new S_SystemMessage("\\f3[" + msg0 + "->" + msg4 + "] " + msg2 + " / " + msg3));
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			_targetPc.sendPackets(new S_SystemMessage("\\f3[" + msg0 + "->" + msg4 + "] " + msg2 + " / " + msg3));
		}

		return isSuccess;
	}

	private boolean checkZone(int skillId) {
		if (_pc != null && _targetPc != null) {
			if (_pc.getZoneType() == 1 || _targetPc.getZoneType() == 1) {
				if (skillId == CURSE_POISON || skillId == CURSE_BLIND || skillId == WEAPON_BREAK || skillId == SLOW || skillId == CURSE_PARALYZE
						|| skillId == MANA_DRAIN || skillId == DARKNESS || skillId == WEAKNESS || skillId == DISEASE || skillId == SILENCE
						|| skillId == FOG_OF_SLEEPING || skillId == DECAY_POTION || skillId == SHOCK_STUN || skillId == ERASE_MAGIC || skillId == EARTH_BIND
						|| skillId == AREA_OF_SILENCE || skillId == WIND_SHACKLE || skillId == POLLUTE_WATER || skillId == STRIKER_GALE
						|| skillId == GUARD_BREAK || skillId == FEAR || skillId == HORROR_OF_DEATH || skillId == ICE_LANCE || skillId == ELEMENTAL_FALL_DOWN
						|| skillId == RETURN_TO_NATURE || skillId == PHANTASM || skillId == CONFUSION || skillId == DESPERADO || skillId == POWERRIP) {
					return false;
				}
			}
		}
		return true;
	}

	private int calcProbability(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int attackLevel = 0;
		int defenseLevel = 0;
		int probability = 0;
		int attackInt = 0;
		int defenseMr = 0;
		L1Character attacker = null;
		L1Character receiver = null;

		switch (_calcType) {
		case PC_PC:
			attacker = _pc;
			receiver = _targetPc;
			break;
		case PC_NPC:
			attacker = _pc;
			receiver = _targetNpc;
			break;
		case NPC_PC:
			attacker = _npc;
			receiver = _targetPc;
			break;
		case NPC_NPC:
			attacker = _npc;
			receiver = _targetNpc;
			break;
		}

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			attackLevel = _pc.getLevel();
			attackInt = _pc.getAbility().getTotalInt();
		} else {
			attackLevel = _npc.getLevel();
			attackInt = _npc.getAbility().getTotalInt();
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			defenseLevel = _targetPc.getLevel();
			defenseMr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			if (_targetNpc != null) {
				defenseLevel = _targetNpc.getLevel();
				defenseMr = _targetNpc.getResistance().getEffectedMrBySkill();
				if (skillId == RETURN_TO_NATURE) {
					if (_targetNpc instanceof L1SummonInstance) {
						L1SummonInstance summon = (L1SummonInstance) _targetNpc;
						defenseLevel = summon.getMaster().getLevel();
					}
				}
			}
		}
		switch (skillId) {
		/** 이레이즈매직 엘리멘탈폴다운 **/
		/** 동레벨일경우 40% 레벨 아래당 2% 성공확률 상향 레벨 높을때 2% 성공확률 감소 **/
		case ERASE_MAGIC:
		case ELEMENTAL_FALL_DOWN: {
			probability = 55 + (attackLevel - defenseLevel) * 3;
			if (probability > 70) {
				probability = 70;
			}
			
/*
			if (attackLevel >= defenseLevel)
				probability = (attackLevel - defenseLevel) * 2 + 43;
			else if (attackLevel < defenseLevel) {
				probability = (attackLevel - defenseLevel) * 3 + 43;
			}
			if (probability > 70) {
				probability = 70;
			}*/
		}
			break;
		/** 어스바인드 **/
		/** 동레벨일경우 35% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
		case EARTH_BIND: {
			probability = 55 + (attackLevel - defenseLevel) * 3;
			if (probability > 70) {
				probability = 70;
			}
			

			/*if (attackLevel >= defenseLevel)
				// 시전자가 대상자보다 레벨이 같거나 높은경우
				probability = (attackLevel - defenseLevel) * 2 + 40;
			else if (attackLevel < defenseLevel) {
				// 시전자가 대상자보다 레벨이 낮은경우
				// ex 84 : 85 = 84 - 85 = -1 * 3 = -3 + 30 = 27
				probability = (attackLevel - defenseLevel) * 3 + 30;
			}
			// 너무 잘걸리면 보정치를 낮춰라
			if (probability > 70) {
				probability = 70;
			}*/
		}
			break;
		/** 스트라이커게일 **/
		/** 동레벨일경우 35% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
		case STRIKER_GALE: {
			probability = 55 + (attackLevel - defenseLevel) * 3;
			if (probability > 70) {
				probability = 70;
			}

			/*if (attackLevel >= defenseLevel)
				probability = (attackLevel - defenseLevel) * 2 + 40;
			else if (attackLevel < defenseLevel) {
				probability = (attackLevel - defenseLevel) * 3 + 30;
			}
			if (probability > 70) {
				probability = 70;
			}*/
		}
			break;
		/** 폴루토워터 **/
		/** 동레벨일경우 50% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
		case POLLUTE_WATER: {
			probability = 55 + (attackLevel - defenseLevel) * 3;
			if (probability > 70) {
				probability = 70;
			}

			/*if (attackLevel >= defenseLevel)
				probability = (attackLevel - defenseLevel) * 2 + 40;
			else if (attackLevel < defenseLevel) {
				probability = (attackLevel - defenseLevel) * 3 + 30;
			}
			if (probability > 70) {
				probability = 70;
			}*/
			// _pc.sendPackets(new S_SystemMessage("[확률] -> " + defenseMr + " "
			// + probability + "%"));
			// System.out.println("[마법확률] -> " + defenseMr + " " + probability +
			// "%");
		}
			/** 윈드세클 **/
			/** 동레벨일경우 30% 레벨아래당 2% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
		case WIND_SHACKLE: {
			

			if (attackLevel >= defenseLevel)
				probability = (attackLevel - defenseLevel) * 2 + 40;
			else if (attackLevel < defenseLevel) {
				probability = (attackLevel - defenseLevel) * 3 + 30;
			}
			if (probability > 50) {
				probability = 50;
			}
		}
			break;
		case SHAPE_CHANGE: // 셰이프 본섭 마방140에게 60% - 마방당 -1%
		case CANCELLATION:// 켄슬 본섭 마방100에게 46% - 마방당 -1%
		case SLOW: // 슬로우 본섭 마방100에게 58% - 마방당 -1%
		case DISEASE: // 디지즈 본섭 마방100에게 68% - 마방당 -1%
		case CURSE_PARALYZE:// 패럴라이즈 마방100에게 15%
			// case ICE_LANCE: // 아이스랜스 마방100에게 30%
		case WEAPON_BREAK:// 웨폰브레이크 마방100에게 30%
		case DECAY_POTION: // 디케이포션 마방100에게 23%
		case CURSE_BLIND: // 야매로 15로 설정함
		case CURSE_POISON: // 야매로 50로 설정함
		case SILENCE: // 야매로 35로 설정함
		case FOG_OF_SLEEPING:// 야매로 25로 설정함
		case DEATH_HEAL:
		case DEATH_HEAL_Mob:
			if (attackInt > 25)
				attackInt = 25;
			probability = (int) (attackInt * 4 + l1skills.getProbabilityValue() - defenseMr);
			if (_pc != null && _pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
				probability -= 30;
			}
			if (probability < 1) {
				probability = 1;
			}
			if (probability > 80) {
				probability = 80;
			}
			/*
			 * _pc.sendPackets(new S_SystemMessage("[확률] -> " + defenseMr + " "
			 * + probability + "%")); System.out.println("[마법확률] -> " +
			 * defenseMr + " " + probability + "%");
			 */
			break;
		case WEAKNESS:
		case ICE_LANCE:
		case DARKNESS:
			if (attackInt > 10)
				attackInt = 10;
			probability = (int) (attackInt * 4 + l1skills.getProbabilityValue() - defenseMr);
			if (_pc != null && _pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
				probability -= 10;
			}
			if (probability < 5) {
				probability = 5;
			}
			if (probability > 70) {
				probability = 70;
			}

			// _pc.sendPackets(new S_SystemMessage("[확률] -> " + defenseMr + " "
			// + probability + "%"));
			// System.out.println("[마법확률] -> " + defenseMr + " " + probability +
			// "%");

			break;
		case THUNDER_GRAB:
			probability = 45 + (attackLevel - defenseLevel) * 3;
			if (probability > 90) {
				probability = 90;
			}
			
			/*probability = 50;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2;
			}*/
			break;
		case COUNTER_BARRIER: // 카운터 배리어
			probability = l1skills.getProbabilityValue(); // 확률
			// System.out.print(String.format("카운터 배리어 확률 : 기본(%d)", probability));
			
			if (_pc != null && _pc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) {
				int lvl = _pc.getLevel();
				if (lvl >= 85) {
					probability += (lvl - 84);
				}
			}
			// System.out.println(String.format(", 최종(%d)", probability));
			break;
		case GUARD_BREAK:
			probability = 55 + (attackLevel - defenseLevel) * 3;
			if (probability > 90) {
				probability = 90;
			}
			
			//probability = 40;
			break;
		case DESPERADO:
		
			probability = 45 + (attackLevel - defenseLevel) * 3;
			if (probability > 90) {
				probability = 90;
			}

			//probability = (int) +Config.DESPERADO + ((attackLevel - defenseLevel) * 9);// 레벨에대한
																						// 데페확률올려야됨
																						// 그럼
																						// 차이가심해짐(4레벨-9)4~6방1번걸림
			probability += _pc.getImpactUp();
			if (probability < 15) {// 레벨차에 대해 최소 10퍼을 발동한다
				probability = 15;
			}
			if (probability > 100) {// 100%확률가정하에
				probability = 100;
			}
			// System.out.println(horroPoint);
			// System.out.println("확률: " + probability);
			break;
		case POWERRIP:
			
			probability = (int) 50 + ((attackLevel - defenseLevel) * 5);
			if (probability < 15) {
				probability = 15;
			}
			if (probability > 80) {
				probability = 80;
			}
			break;
		/** 전사스킬 토마호크 : 동레벨일경우 63% 레벨아래당 5% 성공확률 증가, 레벨높을때 3% 성공확률 감소 **/
		case TOMAHAWK: {
			
			if (attackLevel >= defenseLevel)
				probability = (attackLevel - defenseLevel) * 5 + 63;
			else if (attackLevel < defenseLevel) {
				probability = (attackLevel - defenseLevel) * 3 + 63;
			}
			if (probability > 90) {
				probability = 90;
			}
		}
			break;
		case FEAR: // 피어 본섭 22% ~ 42% 베이스 스텟에 의한
			probability = 55 + (attackLevel - defenseLevel) * 3;
			if (probability > 90) {
				probability = 90;
			}
			
			/*probability = 35;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2 * _pc.getMagicBonus();
			}*/
			break;
		case HORROR_OF_DEATH: // 인트빨 본섭화
			
			probability = (int) ((attackInt - 11) * 6);
			break;
		case MORTAL_BODY:
			probability = (int) Config.MORTALBODY;
			break;
		// 컨퓨젼, 판타즘 본섭 30%
		case CONFUSION:
		case PHANTASM:
			
			probability = 38;
			break;
		case RETURN_TO_NATURE:
			probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel)) + l1skills.getProbabilityValue();
			break;
		case BONE_BREAK:
			if (_calcType == PC_PC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					L1ItemInstance weapon = _pc.getWeapon();
					if (weapon == null) {
						probability = 0;
						break;
					}
					int itemId = weapon.getItem().getItemId();
					if (itemId == 202012 || itemId == 546) { // 히페리온의 절망
						attackLevel += 5;
					}
				}
			}
			probability = 30 + (attackLevel - defenseLevel) * 3; // 본브레이크
			if (probability > 90) {
				probability = 90;
			}
			
			
			/*probability = (int) Config.BONE_BREAK + (attackLevel - defenseLevel);
			if (probability < 10) {
				probability = 10;
			}
			if (probability > 100) {
				probability = 100;
			}*/
			break;
		case EMPIRE:
		{
			/*probability =  75 + ((attackLevel - defenseLevel) * 5);//
			if (probability > 80) {// 총 스턴확률80 이하
				probability = 80;
			}*/

			/** 엠파이어 외부화 **/
			probability = (int) + Config.EMPIRE + ((attackLevel - defenseLevel) * 2);// *2배
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += _pc.getImpactUp();
			}
			if (probability < 15) {// 기본정의 10
				probability = 15;
			}
			if (probability > 100) {// 총 스턴확률 100 이하
				probability = 100;
			}
		}
			break;
		case SHOCK_STUN:// 쇼크스턴	
			/*probability =  95 + ((attackLevel - defenseLevel) * 5);//
			if (probability > 80) {// 총 스턴확률80 이하
				probability = 80;
			}*/
			/** 쇼크스턴 외부화 **/
			probability = (int) +Config.SHOCK_STUN + ((attackLevel - defenseLevel) * 2);// *2배
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += _pc.getImpactUp();
			}
			if (probability < 15) {// 기본정의 10
				probability = 15;
			}
			if (probability > 100) {// 총 스턴확률 100 이하
				probability = 100;
			}
			System.out.println("확률: " + probability);
			break;
		case MANA_DRAIN:
			if (attackInt > 25)
				attackInt = 25;
			probability = (int) ((attackInt - (defenseMr / 5.95)) + l1skills.getProbabilityValue());
			if (_pc != null && _pc.isElf() && (_calcType == PC_PC || _calcType == PC_NPC)) {
				probability -= 50;
			}
			if (probability < 0)
				probability = 0;
			break;
		case FINAL_BURN:
			probability = (int) ((attackLevel) - (defenseMr / 5)); // 파이널 번
			break;
			
		case TURN_UNDEAD:
			if (attackInt > 25)
				attackInt = 25;	
			int level_probability = 0;
			// 턴 언데드 확률 외부화
			if(attackLevel >= 80){
				level_probability = Config.TurnChant80UP;
			}else if(attackLevel >= 79){
				level_probability = Config.TurnChant79UP;
			}else if(attackLevel >= 78){
				level_probability = Config.TurnChant78UP;
			}else if(attackLevel >= 77){
				level_probability = Config.TurnChant77UP;
			}else if(attackLevel >= 76){
				level_probability = Config.TurnChant76UP;
			}else if (attackLevel >= 75){
				level_probability = Config.TurnChant75UP;				
			}else if (attackLevel >= 74){
				level_probability = Config.TurnChant74UP;				
			}else if (attackLevel >= 73){
				level_probability = Config.TurnChant73UP;				
			}else if (attackLevel >= 72){
				level_probability = Config.TurnChant72UP;				
			}else if (attackLevel >= 71){
				level_probability = Config.TurnChant71UP;				
			}else if (attackLevel >= 70){
				level_probability = Config.TurnChant70UP;				
			}else if (attackLevel >= 69){
				level_probability = Config.TurnChant69UP;			
			}else if (attackLevel >= 68){
				level_probability = Config.TurnChant68UP;				
			}else if (attackLevel >= 67){
				level_probability = Config.TurnChant67UP;				
			}else if (attackLevel >= 66){
				level_probability = Config.TurnChant66UP;				
			}else if (attackLevel >= 65){
				level_probability = Config.TurnChant65UP;
			}else if (attackLevel >= 64){
				level_probability = Config.TurnChant64UP;
			}else if (attackLevel >= 63){
				level_probability = Config.TurnChant63UP;
			}else if (attackLevel >= 62){
				level_probability = Config.TurnChant62UP;
			}else if (attackLevel >= 61){
				level_probability = Config.TurnChant61UP;
			}else if (attackLevel >= 60){
				level_probability = Config.TurnChant60UP;
			}else if (attackLevel >= 59){
				level_probability = Config.TurnChant59UP;
			}else if (attackLevel >= 58){
				level_probability = Config.TurnChant58UP;
			}else if (attackLevel >= 57){
				level_probability = Config.TurnChant57UP;
			}else if (attackLevel >= 56){
				level_probability = Config.TurnChant56UP;
			}else if (attackLevel >= 55){
				level_probability = Config.TurnChant57UP;
			}
			
			probability = (int) ((attackInt * 3 + (level_probability * 2.5)) - (defenseMr + (defenseLevel / 2)) - 80);
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (!_pc.isWizard()) {
					probability -= 30;
				}
				if (!_pc.isElf()) {
					probability -= Config.TurnChantElf;
				}
			}
			break;
		/** 바란카 스턴레벨 상승에 따른 아머브레이크 확률 증가 **/
		case ARMOR_BRAKE:
			
			probability = (int) (55 + ((attackLevel - defenseLevel) * 3));// 3배
			probability += _pc.getImpactUp();
			if (_pc.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
				int lvl = _pc.getLevel();
				if (lvl >= 85)
					probability += ((lvl - 84) * 3);
			}
			

			/** 바란카 스턴레벨 상승에 따른 아머브레이크 확률 증가 **/
			/*probability = (int) (Config.ARMOR_BRAKET + ((attackLevel - defenseLevel) * 3));// 3배
			probability += _pc.getImpactUp();
			if (_pc.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
				int lvl = _pc.getLevel();
				if (lvl >= 85)
					probability += ((lvl - 84) * 3);
			}*/

			probability = IntRange.ensure(probability, 10, 80);

			break;
		default: {
			int dice1 = l1skills.getProbabilityDice();
			int diceCount1 = 0;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.isWizard()) {
					diceCount1 = getMagicBonus() + getMagicLevel() + 1;
				} else if (_pc.isElf()) {
					diceCount1 = getMagicBonus() + getMagicLevel() - 1;
				} else if (_pc.isDragonknight()) {
					diceCount1 = getMagicBonus() + getMagicLevel();
				} else {
					diceCount1 = getMagicBonus() + getMagicLevel() - 1;
				}
			} else {
				diceCount1 = getMagicBonus() + getMagicLevel();
			}
			if (diceCount1 < 1) {
				diceCount1 = 1;
			}
			if (dice1 > 0) {
				for (int i = 0; i < diceCount1; i++) {
					probability += (_random.nextInt(dice1) + 1);
				}
			}

			probability = probability * getLeverage() / 10;
			probability -= getTargetMr();

			if (skillId == TAMING_MONSTER) {
				double probabilityRevision = 1;
				if ((_targetNpc.getMaxHp() * 1 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.3;
				} else if ((_targetNpc.getMaxHp() * 2 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.2;
				} else if ((_targetNpc.getMaxHp() * 3 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.1;
				}
				probability *= probabilityRevision;
			}
		}
			break;
		}

		eKind kind = eKind.NONE;
		// TODO 내성에 대한 타입
		switch (skillId) {

		// 공포내성
		case DESPERADO:
		case POWERRIP:
		case TOMAHAWK:
			kind = eKind.FEAR;
			break;

		// 정령내성
		case ARMOR_BRAKE:
		case EARTH_BIND:
		case ERASE_MAGIC:
		case ELEMENTAL_FALL_DOWN:
		case STRIKER_GALE:
		case POLLUTE_WATER:
		case WIND_SHACKLE:
			kind = eKind.SPIRIT;
			break;

		// 용언내성
		case BONE_BREAK:
		case THUNDER_GRAB:
		case GUARD_BREAK:
		case FEAR:
		case PHANTASM:
			kind = eKind.DRAGON_SPELL;
			break;

		// 기술내성
		case EMPIRE:
		case SHOCK_STUN:
		case 30081:
			kind = eKind.ABILITY;
			break;
		default:
			break;
		}
		
		
		
		if (kind.toInt() != eKind.NONE.toInt()) {
			int resistance = receiver.getSpecialResistance(kind) + receiver.getSpecialResistance(eKind.ALL);
			if (resistance > 0)
				probability -= resistance;
			
			resistance = attacker.getSpecialPierce(kind) + attacker.getSpecialPierce(eKind.ALL);
			if (resistance > 0)
				probability += resistance;
		}
		
		
		
		if(skillId == SHOCK_STUN || skillId == EMPIRE){
			if (probability > 100) {// 총 스턴확률80 이하
				probability = 100;
			}
		}
		
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (_pc.isGm()) {
				System.out.println("마법확률 : " + probability);
			}
		}

		return calc_character_magic_hit(skillId, probability);
	}

	// 마법적중 실제 적용
	private int calc_character_magic_hit(int skill_id, int probability) {
		if (_calcType != PC_PC && _calcType != PC_NPC) {
			return probability;
		}

		double result = probability;
		switch (skill_id) {
		default: {
			double p = Config.CHARACTER_MAGICHIT_RATE * _pc.getTotalMagicHitup();
			result += p;
			break;
		}
		}

		return (int) result;
	}

	public int calcMagicDamage(int skillId) { 
		int damage = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			damage = calcPcMagicDamage(skillId);
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			damage = calcNpcMagicDamage(skillId);
		}

		if (skillId != CONFUSION && skillId != MIND_BREAK && skillId != MAGMA_BREATH) {
			damage = calcMrDefense(damage);
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {

			if (skillId == ENERGY_BOLT || skillId == CALL_LIGHTNING || skillId == DISINTEGRATE) {
				for (L1ItemInstance armor : _targetPc.getEquipSlot().getArmors()) {
					// 붉은 기사의 방패
					if (armor.getItemId() == 20230) {
						int probability = 1;

						if (armor.getEnchantLevel() >= 10) {
							probability = 5;
						} else if (armor.getEnchantLevel() < 6) {
							probability = 1;
						} else {
							probability = armor.getEnchantLevel() - 5;
						}

						if (_random.nextInt(100) < probability) {
							damage *= 0.8D;
						}

						break;
					}
				}
			}
			if (damage > _targetPc.getCurrentHp()) {
				if (_targetPc.isElf() && _targetPc.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
					if (damage > _targetPc.getCurrentHp() + _targetPc.getCurrentMp()) {
						damage = _targetPc.getCurrentHp() + _targetPc.getCurrentMp();
					}
				} else {
					damage = _targetPc.getCurrentHp();
				}
			}
			// 전사스킬 : 타이탄 매직
			// HP가 40% 미만일때 마법공격을 확률적으로 반사.
			if (_targetPc.isPassive(MJPassiveID.TITAN_MAGIC.toInt())) {
				int percent = (int) Math.round(((double) _targetPc.getCurrentHp() / (double) _targetPc.getMaxHp()) * 100);
				int chance = _random.nextInt(100) + 1;
				int 락구간 = 0;
				int titan = _targetPc.getInventory().checkEquippedcount(202014);
				int sumgwang = _targetPc.getInventory().checkEquippedcount(547);
				락구간 += titan == 1 ? 5 : titan == 2 ? 10 : 0;
				락구간 += sumgwang == 1 ? 5 : sumgwang == 2 ? 10 : 0;
				// System.out.println("락구간 얼마상승하는가" + 락구간);
				//if (!MJCommons.isUnbeatable(_targetPc) && (percent + _targetPc.getRisingUp() + 락구간) <= 40 && chance <= 33) {
				if (!MJCommons.isUnbeatable(_targetPc) && percent <= (40 + 락구간 + _targetPc.getRisingUp()) && chance <= Config.Titanmagic_probability) {
					if (_targetPc.getInventory().checkItem(41246, 10)) {
						if (_calcType == PC_PC)
							_pc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
						else if (_calcType == PC_NPC)
							_npc.receiveCounterBarrierDamage(_targetPc, 타이탄대미지());
						damage = 0;
						_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 12559));
						_targetPc.getInventory().consumeItem(41246, 10);
					} else {
						_targetPc.sendPackets(new S_SystemMessage("타이탄 매직: 촉매제가 부족합니다."));
					}
				}
				return (int) damage;
			}

		} else {
			if (damage > _targetNpc.getCurrentHp()) {
				damage = _targetNpc.getCurrentHp();
			}
		}
		return damage;
	}

	public int calcPcFireWallDamage() {
		int dmg = 0;

		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = calcAttrDefence(l1skills.getDamageValue(), L1Skills.ATTR_FIRE);

		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			dmg = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			dmg = 0;
		}
		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	public int calcNpcFireWallDamage() {
		int dmg = 0;

		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = calcAttrDefence(l1skills.getDamageValue(), L1Skills.ATTR_FIRE);

		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			dmg = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			dmg = 0;
		}
		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	/** NPC -> PC (PC->PC) 마법 대미지 산출 **/
	private int calcPcMagicDamage(int skillId) {
		int dmg = 0;
		
		dmg = calcMagicDiceDamage(skillId);
		dmg = (dmg * getLeverage()) / 10;
		
		/** 대미지 감소 부분 **/
		dmg -= _targetPc.getDamageReductionByArmor();

		if (_calcType == PC_PC){
			if (_targetPc.getPVPMagicDamageReduction() > 0){
			    dmg -= _targetPc.getPVPMagicDamageReduction();
			}
		}

		if (skillId == FINAL_BURN) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				dmg = _pc.getCurrentMp() / 2;
				dmg = (int) ((dmg * getLeverage()) / Config.Finalburnpc);
			} else {
				dmg = _npc.getCurrentMp() / 2;
				dmg = (int) ((dmg * getLeverage()) / Config.Finalburnpc);
			}
		} else {
			dmg = calcMagicDiceDamage(skillId);
			dmg = (dmg * getLeverage()) / 10;
		}

		if (_targetPc.hasSkillEffect(COOKING_1_0_S) || _targetPc.hasSkillEffect(COOKING_1_1_S) || _targetPc.hasSkillEffect(COOKING_1_2_S)
				|| _targetPc.hasSkillEffect(COOKING_1_3_S) || _targetPc.hasSkillEffect(COOKING_1_4_S) || _targetPc.hasSkillEffect(COOKING_1_5_S)
				|| _targetPc.hasSkillEffect(COOKING_1_6_S) || _targetPc.hasSkillEffect(COOKING_1_8_S) || _targetPc.hasSkillEffect(COOKING_1_9_S)
				|| _targetPc.hasSkillEffect(COOKING_1_10_S) || _targetPc.hasSkillEffect(COOKING_1_11_S) || _targetPc.hasSkillEffect(COOKING_1_12_S)
				|| _targetPc.hasSkillEffect(COOKING_1_13_S) || _targetPc.hasSkillEffect(COOKING_1_14_S) || _targetPc.hasSkillEffect(COOKING_1_16_S)
				|| _targetPc.hasSkillEffect(COOKING_1_17_S) || _targetPc.hasSkillEffect(COOKING_1_18_S) || _targetPc.hasSkillEffect(COOKING_1_19_S)
				|| _targetPc.hasSkillEffect(COOKING_1_20_S) || _targetPc.hasSkillEffect(COOKING_1_21_S) || _targetPc.hasSkillEffect(COOKING_1_22_S)) {
			dmg -= 4;
		}
		if (_targetPc.hasSkillEffect(COOKING_1_7_S) || _targetPc.hasSkillEffect(COOKING_1_15_S) || _targetPc.hasSkillEffect(COOKING_1_20_S)) {
			dmg -= 4;
		}

		if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 50) {
				targetPcLvl = 50;
			}
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		
		if (_targetPc.hasSkillEffect(MAJESTY)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 80) {
				targetPcLvl = 80;
			}
			dmg -= (targetPcLvl - 80) / 2 + 1;
		}

		if (_targetPc.hasSkillEffect(EARTH_GUARDIAN)) {
			dmg -= 2;
		}
		
		/**마안 데미지 50% 감소*/
		if (_targetPc.hasSkillEffect(BIRTH_MAAN) || _targetPc.hasSkillEffect(SHAPE_MAAN) ||
				_targetPc.hasSkillEffect(LIFE_MAAN)) {
			dmg /= 2;
		}
		

		if (_calcType == NPC_PC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
			if (castleId > 0) {
				isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
			}
			if (!isNowWar) {
				if (_npc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_npc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _npc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
		}
		/** 마법사 일경우 대미지외부화 적용 */
		if (_calcType == PC_PC) {
			if (_pc.getType() == 3) {
				dmg *= Config.마법사마법대미지;
			}
		}
		if (_targetPc.hasSkillEffect(IllUSION_AVATAR)) {
			dmg += dmg / 3;
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
				dmg -= 5 + ((_targetPc.getLevel() - 80) / 4);
			} else {
				dmg -= 2;
			}
		}
		
		
		if (dmg > 0 && _targetPc.hasSkillEffect(IMMUNE_TO_HARM))
			dmg -= (dmg * _targetPc.getImmuneReduction());

		if (_targetPc.hasSkillEffect(FEATHER_BUFF_A)) {
			dmg -= 3;
		}
		if (_targetPc.hasSkillEffect(FEATHER_BUFF_B)) {
			dmg -= 2;
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

		/*** 신규레벨보호 ***/
		// if (_calcType == PC_PC) {
		// int castle_id = L1CastleLocation.getCastleIdByArea(_pc);
		// if (castle_id == 0) {
		// if (_targetPc.getLevel() < Config.AUTO_REMOVELEVEL
		// || _pc.getLevel() < Config.AUTO_REMOVELEVEL && !_pc.isPinkName()) {
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
							int monid = UserProtectMonsterTable.getInstance().getUserProtectMonsterId(mon.getNpcId());
							if (monid != 0) {
								attack_ok = true;
								break;
							}
						}
					}

					if (!attack_ok) {
						if (_pc.getClanid() == Config.신규혈맹클랜 || _targetPc.getClanid() == Config.신규혈맹클랜) {
							if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL
									&& skillId != NATURES_BLESSING) { // 버프계
								_pc.sendPackets(new S_SystemMessage("신규유저는 상호간에 공격이 되지 않습니다."));
								_targetPc.sendPackets(new S_SystemMessage("신규유저는 상호간에 공격이 되지 않습니다."));
								return 0;
							}
						}
					}
				} else {
					if (_pc.getClanid() == Config.신규혈맹클랜 || _targetPc.getClanid() == Config.신규혈맹클랜) {
						if (skillId != EXTRA_HEAL && skillId != HEAL && skillId != GREATER_HEAL && skillId != HEAL_ALL && skillId != FULL_HEAL
								&& skillId != NATURES_BLESSING) { // 버프계
							dmg /= 2;
							_pc.sendPackets(new S_SystemMessage("신규유저는 대미지의 50%만 가해집니다."));
							_targetPc.sendPackets(new S_SystemMessage("신규유저는 대미지를 50%만 받습니다."));
						}
					}
				}
			}
		}

		if (_calcType == NPC_PC) {
			if (_targetPc.getAccount() != null && _targetPc.getAccount().getGrangKinAngerStat() != 0 && dmg > 0) {
				dmg *= _targetPc.getAccount().getGrangKinAngerReducCalc();
			}
		}

		/** 특정지역 PK금지 **/
		if (_calcType == PC_PC) {
			if (_pc.getMapId() == 254 || _pc.getMapId() == 612 || _pc.getMapId() == 1930) {
				_pc.sendPackets("\\fY육성 사냥터에서는 PK가 불가능 합니다.");
				_targetPc.sendPackets("\\fY육성 사냥터에서는 PK가 불가능 합니다.");
				dmg = 0;
			}
		}
		double total_reduction = 0;
		Object[] dollList = _targetPc.getDollList().values().toArray(); // 마법인형에 의한 추가  방어
		L1DollInstance doll = null;
		for (Object dollObject : dollList) {
			doll = (L1DollInstance) dollObject;
			total_reduction += doll.getDamageReductionByDoll();
		}
		total_reduction += _targetPc.getDamageReductionByArmor();
		total_reduction += _targetPc.getDamageReduction(); // 방어구에 의한 대미지 감소
		total_reduction *= Config.MAGIC_REDUCTION_RATION;
		dmg = (int) Math.max(dmg - total_reduction, 0);
		if (dmg < 0) {
			dmg = 0;
		}
		return dmg;
	}

	/** 플레이어-> NPC에게 마법 대미지 산출 **/
	private int calcNpcMagicDamage(int skillId) {
		int dmg = 0;
		if (skillId == FINAL_BURN) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				dmg = _pc.getCurrentMp();
				dmg = (int) ((dmg * getLeverage()) / Config.Finalburnnpc);
			} else {
				dmg = _npc.getCurrentMp();
				dmg = (int) ((dmg * getLeverage()) / Config.Finalburnnpc);
			}
		} else {
			dmg = calcMagicDiceDamage(skillId);
			/** PC->NPC 마법 대미지 외부화 **/
			if (_calcType == PC_NPC) {
				dmg = (dmg * getLeverage()) / Config.pcnpcmagicdmg;
				dmg *= getSimSimLeverage();
			} else {
				dmg = (dmg * getLeverage()) / Config.pcnpcmagicdmg;
			}
		}
		if (_calcType == PC_NPC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
			if (castleId > 0) {
				isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
			}
			if (!isNowWar) {
				if (_targetNpc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_targetNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _targetNpc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
		}

		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId >= 45912 && npcId <= 45915 && !_pc.hasSkillEffect(STATUS_HOLY_WATER)) {
				dmg = 0;
			}
			if (npcId == 45916 && !_pc.hasSkillEffect(STATUS_HOLY_MITHRIL_POWDER)) {
				dmg = 0;
			}
			if (npcId == 45941 && !_pc.hasSkillEffect(STATUS_HOLY_WATER_OF_EVA)) {
				dmg = 0;
			}
//			if (npcId == 45752 && !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
//				dmg = 0;
//			}
//			if (npcId == 45753 && !_pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
//				dmg = 0;
//			}
//			if (npcId == 45675 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//				dmg = 0;
//			}
//			if (npcId == 81082 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
//				dmg = 0;
//			}
		/*	if (npcId == 45625 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
				dmg = 0;
			}
			if (npcId == 45674 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
				dmg = 0;
			}
			if (npcId == 45685 && !_pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
				dmg = 0;
			}*/
			if (npcId >= 46068 && npcId <= 46091 && _pc.getCurrentSpriteId() == 6035) {
				dmg = 0;
			}
			if (npcId >= 46092 && npcId <= 46106 && _pc.getCurrentSpriteId() == 6034) {
				dmg = 0;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && !_pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
				dmg = 0;
			}
			if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && !_pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
				dmg = 0;
			}
			if ((_targetNpc.getNpcTemplate().get_gfxid() == 7864 || _targetNpc.getNpcTemplate().get_gfxid() == 7869
					|| _targetNpc.getNpcTemplate().get_gfxid() == 7870)) {
				dmg *= 1.5;
			} // 파푸리온 혈흔1.5뎀

			L1SpecialMap sm = SpecialMapTable.getInstance().getSpecialMap(_pc.getMapId());

			if (sm != null) {
				dmg -= sm.getMdmgReduction();
				if (dmg <= 0)
					dmg = 1;
			}
		}
		if (_calcType == PC_NPC) {
			if (_pc.getAccount() != null && _pc.getAccount().getGrangKinAngerStat() != 0 && dmg > 0) {
				dmg = _pc.getAccount().getGrangKinAngerDmgCalc(dmg);
			}
		}

		return dmg;
	}

	/** 플레이어->플레이어 에게 마법 대미지 산출 **/
	private int calcMagicDiceDamage(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int diceCount = l1skills.getDamageDiceCount();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;
		double PowerMr = 0; // 마방

		Random random = new Random();

		dice += getSpellPower() / 2;

		for (int i = 0; i < diceCount; i++) {
			int plus_dmg = (_random.nextInt(dice) + 1);
			magicDamage += plus_dmg;
		}

		magicDamage += value * (1 + getSpellPower() / 10);

		/** 치명타 발생 부분 */
		double criticalCoefficient = 1.4;
		int rnd = random.nextInt(100) + 1;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int propCritical = CalcStat.calcMagicCritical(_pc.ability.getTotalInt());
			switch (skillId) {
			// 6레벨 이하 광역마법 제외한 공격마법
			case ENERGY_BOLT:
			case ICE_DAGGER:
			case WIND_CUTTER:
			case CHILL_TOUCH:
			case SMASH:
			case FIRE_ARROW:
			case STALAC:
			case VAMPIRIC_TOUCH:
			case CONE_OF_COLD:
			case CONE_OF_COLD_mob:
			case CALL_LIGHTNING:
			case DISINTEGRATE:
				propCritical = +10;
				break;
			}

			// 마안 일정확률로 마법치명타+1
			if (_pc.hasSkillEffect(LIND_MAAN) || _pc.hasSkillEffect(SHAPE_MAAN) || _pc.hasSkillEffect(LIFE_MAAN)) {
				propCritical += 1;
			}
			propCritical += (_pc.getBaseMagicCritical() * Config.CHARACTER_MAGICCRI_RATE);
			if (criticalOccur(propCritical)) {
				magicDamage *= 1.5;
			}
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			if (rnd <= 15) {
				magicDamage *= criticalCoefficient;
			}
		}

		// 디스마법은 라우풀에 따라 데미지 상향처리.
		// : 카오틱수치가 높을수록 데미지 하향
		/** 디스 본섭화 **/
		if (skillId == DISINTEGRATE) {
			int lawful = getLawful();
			if (lawful > 0) {
				// magicDamage += (magicDamage * (lawful * 0.000001));
				magicDamage += (lawful / Config.DISINT_LAWFUL_WEIGHT);
			} else if (lawful < 0) {
				magicDamage += (lawful / Config.DISINT_CHAOTIC_WEIGHT);
			}

			if (_calcType == PC_PC) {
				if (skillId == DISINTEGRATE && _targetPc instanceof L1PcInstance) {
					if (_targetPc.hasSkillEffect(ANTI_DISINTEGRATE)) {
						return 0;
					}
					_targetPc.setSkillEffect(ANTI_DISINTEGRATE, 3000);
				}
			}
		}

		if (getTargetMr() < 101) {
			PowerMr = getTargetMr() / (double) 170; // 마방170되면 10당 (기본대미지*마법상수)의
													// 5% 대미지 줄어들게 설정 총50%
		} else {
			PowerMr = 0.5 + (getTargetMr() - 100) / (double) 400; // 마방100초과분에
																	// 대해 10당
																	// (기본대미지*마법상수)의
																	// 1% 줄어들게
																	// 설정 100당
																	// 10%
		} // 마방 400되면 마법대미지 0

		if (skillId == FINAL_BURN) {
			PowerMr = 0;
		}
		magicDamage -= magicDamage * PowerMr; // 먼저 마방에 의한 대미지 감소부터 처리

		double attrDeffence = calcAttrResistance(l1skills.getAttr());

		// 속성방어 100당 45% 줄어듬.
		// 10당4.5%초과분에대해서
		// 10당 0.9% 줄어들게 설정
		magicDamage -= magicDamage * attrDeffence; // 마방에 의한 대미지 감소후 속성방어에 의한
		// 대미지 감소 처리

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicDamage += _pc.getBaseMagicDmg(); // 베이스 스탯 마법 대미지 보너스 추가
		}
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int weaponAddDmg = 0;
			L1ItemInstance weapon = _pc.getWeapon();
			if (weapon != null) {
				weaponAddDmg = weapon.getItem().getMagicDmgModifier();
			}
			magicDamage += weaponAddDmg; // 무기에 의한 마법 대미지 추가
		}
		return magicDamage;
	}

	public int calcHealing(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;

		int magicBonus = getMagicBonus();
		if (magicBonus > 10) {
			magicBonus = 10;
		}

		int diceCount = value + magicBonus;
		for (int i = 0; i < diceCount; i++) {
			magicDamage += (_random.nextInt(dice) + 1);
		}

		double alignmentRevision = 1.0;
		if (getLawful() > 0) {
			alignmentRevision += (getLawful() / 32768.0);
		}

		magicDamage *= alignmentRevision;

		magicDamage = (magicDamage * getLeverage()) / 10;

		return magicDamage;
	}

	/**
	 * MR에 의한 마법 대미지 감소를 처리 한다 수정일자 : 2013.02.22 수정자 : 메르키스
	 * 
	 * @param dmg
	 * @return dmg
	 */

	public int calcMrDefense(int dmg) {
		int PInt = 0;
		int mrs = 0;
		int attackPcLvSp = 0;
		int targetPcLvMr = 0;
		int ran1 = 0;
		int mrset = 0;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			PInt = _pc.getSp() * 2;
		} else if (_calcType == NPC_PC) {
			PInt = _npc.getSp() * 2;
		}
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			mrs = (int) (_targetPc.getMr() * 1.7D - 20);
		} else {
			mrs = (int) (_targetNpc.getMr() * 1.7D - 20);
		}
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			attackPcLvSp = _pc.getLevel();
		} else if (_calcType == NPC_PC) {
			attackPcLvSp = _npc.getLevel();
		}
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			targetPcLvMr = _targetPc.getLevel();
		} else {
			targetPcLvMr = _targetNpc.getLevel();
		}

		Random random = new Random();
		ran1 = random.nextInt(15) + 1;
		mrset = mrs - ran1;

		int PPPP = (int) (attackPcLvSp / 8D + 1);
		int TTTT = (int) (targetPcLvMr / 10D + 1);
		int fail = PInt + PPPP - TTTT;

		if ((mrset - fail) >= 151) {
			dmg *= 0.01D;
		} else if ((mrset - fail) >= 146 && (mrset - fail) <= 150) {
			dmg *= 0.03D;
		} else if ((mrset - fail) >= 141 && (mrset - fail) <= 145) {
			dmg *= 0.07D;
		} else if ((mrset - fail) >= 136 && (mrset - fail) <= 140) {
			dmg *= 0.10D;
		} else if ((mrset - fail) >= 131 && (mrset - fail) <= 135) {
			dmg *= 0.13D;
		} else if ((mrset - fail) >= 126 && (mrset - fail) <= 130) {
			dmg *= 0.17D;
		} else if ((mrset - fail) >= 121 && (mrset - fail) <= 125) {
			dmg *= 0.20D;
		} else if ((mrset - fail) >= 116 && (mrset - fail) <= 120) {
			dmg *= 0.23D;
		} else if ((mrset - fail) >= 111 && (mrset - fail) <= 115) {
			dmg *= 0.27D;
		} else if ((mrset - fail) >= 106 && (mrset - fail) <= 110) {
			dmg *= 0.30D;
		} else if ((mrset - fail) >= 101 && (mrset - fail) <= 105) {
			dmg *= 0.33D;
		} else if ((mrset - fail) >= 96 && (mrset - fail) <= 100) {
			dmg *= 0.37D;
		} else if ((mrset - fail) >= 91 && (mrset - fail) <= 95) {
			dmg *= 0.40D;
		} else if ((mrset - fail) >= 86 && (mrset - fail) <= 90) {
			dmg *= 0.43D;
		} else if ((mrset - fail) >= 81 && (mrset - fail) <= 85) {
			dmg *= 0.47D;
		} else if ((mrset - fail) >= 76 && (mrset - fail) <= 80) {
			dmg *= 0.50D;
		} else if ((mrset - fail) >= 71 && (mrset - fail) <= 75) {
			dmg *= 0.53D;
		} else if ((mrset - fail) >= 66 && (mrset - fail) <= 70) {
			dmg *= 0.57D;
		} else if ((mrset - fail) >= 60 && (mrset - fail) <= 65) {
			dmg *= 0.60D;
		} else if ((mrset - fail) >= 51 && (mrset - fail) <= 56) {
			dmg *= 0.63D;
		} else if ((mrset - fail) >= 46 && (mrset - fail) <= 50) {
			dmg *= 0.67D;
		} else if ((mrset - fail) >= 41 && (mrset - fail) <= 45) {
			dmg *= 0.70D;
		} else if ((mrset - fail) >= 36 && (mrset - fail) <= 40) {
			dmg *= 0.73D;
		} else if ((mrset - fail) >= 31 && (mrset - fail) <= 35) {
			dmg *= 0.77D;
		} else if ((mrset - fail) >= 26 && (mrset - fail) <= 30) {
			dmg *= 0.80D;
		} else if ((mrset - fail) >= 21 && (mrset - fail) <= 25) {
			dmg *= 0.85D;
		} else if ((mrset - fail) >= 16 && (mrset - fail) <= 20) {
			dmg *= 0.90D;
		} else if ((mrset - fail) >= 11 && (mrset - fail) <= 15) {
			dmg *= 0.95D;
		} else if ((mrset - fail) >= 6 && (mrset - fail) <= 10) {
			dmg *= 1.00D;
		} else {
			dmg *= 1.05D;
		}
		return dmg;
	}

	private boolean criticalOccur(int prop) {
		if (_pc != null) {
			prop += _pc.get_magic_critical_rate();
		}

		int num = _random.nextInt(100) + 1;

		if (prop == 0) {
			return false;
		}
		if (num <= prop) {
			_CriticalDamage = true;
		}
		return _CriticalDamage;
	}

	private double calcAttrResistance(int attr) {
		int resist = 0;
		int resistFloor = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			switch (attr) {
			case L1Skills.ATTR_EARTH:
				resist = _targetPc.getResistance().getEarth();
				break;
			case L1Skills.ATTR_FIRE:
				resist = _targetPc.getResistance().getFire();
				break;
			case L1Skills.ATTR_WATER:
				resist = _targetPc.getResistance().getWater();
				break;
			case L1Skills.ATTR_WIND:
				resist = _targetPc.getResistance().getWind();
				break;
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
		}
		if (resist < 0) {
			resistFloor = (int) (-0.45 * Math.abs(resist));
		} else if (resist < 101) {
			resistFloor = (int) (0.45 * Math.abs(resist));
		} else {
			resistFloor = (int) (45 + 0.09 * Math.abs(resist));
			// 속성100초과분에 대해0.45의 1/5정도 감소되게 변경
		}
		double attrDeffence = resistFloor / 100;
		return attrDeffence;
	}

	private int calcAttrDefence(int dmg, int attr) {
		if (dmg < 1) {
			return dmg;
		}

		int resist = 0;

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			switch (attr) {
			case L1Skills.ATTR_EARTH:
				resist = _targetPc.getResistance().getEarth();
				break;
			case L1Skills.ATTR_FIRE:
				resist = _targetPc.getResistance().getFire();
				break;
			case L1Skills.ATTR_WATER:
				resist = _targetPc.getResistance().getWater();
				break;
			case L1Skills.ATTR_WIND:
				resist = _targetPc.getResistance().getWind();
				break;
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
		}

		dmg -= resist / 2;

		if (dmg < 1) {
			dmg = 1;
		}

		return dmg;
	}

	public void commit(int damage, int drainMana) {
	    if (_calcType == PC_PC || _calcType == NPC_PC) {
	        commitPc(damage, drainMana);
	    } else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
	        commitNpc(damage, drainMana);
	    }

	    // 여기부터 마법 이펙트 발동 로직 추가
	    if (damage > 0) {
	        int magicEffectId = -1; // 이펙트 ID 초기화
	        // 대미지 값에 따라 이펙트 결정
	        if (damage >= 1000) {
	            magicEffectId = 18558; // DamageX2 이펙트 ID
	        } else if (damage >= 500) {
	            magicEffectId = 18556; // DamageX1.5 이펙트 ID
	        } else if (damage >= 300) {
	            magicEffectId = 18554; // Critical 이펙트 ID
	        }

	        // 결정된 이펙트 ID로 이펙트 발동
	        if (magicEffectId != -1) {
	            if (_calcType == PC_NPC || _calcType == NPC_NPC) {
	                _pc.sendPackets(new S_SkillSound(_targetNpc.getId(), magicEffectId));
	            } else if (_calcType == PC_PC || _calcType == NPC_PC) {
	                _targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), magicEffectId));
	            }
	        }
	    }
	    // 마법 이펙트 발동 로직 끝

		if (!Config.ALT_ATKMSG) {
			return;
		}
		if (Config.ALT_ATKMSG) {
			if ((_calcType == PC_PC || _calcType == PC_NPC) && !_pc.isGm()) {
				return;
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && !_targetPc.isGm()) {
				return;
			}
		}
		String msg0 = "";
		String msg2 = "";
		String msg3 = "";
		String msg4 = "";

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			msg0 = _pc.getName();
		} else if (_calcType == NPC_PC) {
			msg0 = _npc.getName();
		}

		if (_calcType == NPC_PC || _calcType == PC_PC) {
			msg4 = _targetPc.getName();
			msg2 = "HP:" + _targetPc.getCurrentHp();
		} else if (_calcType == PC_NPC) {
			msg4 = _targetNpc.getName();
			msg2 = "HP:" + _targetNpc.getCurrentHp();
		}

		msg3 = "DMG:" + damage;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			_pc.sendPackets(new S_SystemMessage("\\fR[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2));
		}
		if (_calcType == NPC_PC || _calcType == PC_PC) {
			_targetPc.sendPackets(new S_SystemMessage("\\fY[" + msg0 + "->" + msg4 + "] " + msg3 + " / " + msg2));
		}
	}

	private void commitPc(int damage, int drainMana) {
		if (_targetPc.hasSkillEffect(ABSOLUTE_BARRIER)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(ICE_LANCE)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(EARTH_BIND)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			damage = 0;
			drainMana = 0;
		}
		if (_targetPc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			damage = 0;
			drainMana = 0;
		}

		if (_calcType == PC_PC) {
			if (drainMana > 0 && _targetPc.getCurrentMp() > 0) {
				if (drainMana > _targetPc.getCurrentMp()) {
					drainMana = _targetPc.getCurrentMp();
				}
				int newMp = _pc.getCurrentMp() + drainMana;
				_pc.setCurrentMp(newMp);
			}
			_targetPc.receiveManaDamage(_pc, drainMana);
			_targetPc.receiveDamage(_pc, damage);
		} else if (_calcType == NPC_PC) {
			_targetPc.receiveDamage(_npc, damage);
		}
	}

	private void commitNpc(int damage, int drainMana) {
		if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_BASILL)) { // 바실얼리기대미지0
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.hasSkillEffect(MOB_COCA)) { // 코카얼리기대미지0
			damage = 0;
			drainMana = 0;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7684 && _pc.hasSkillEffect(PAP_FIVEPEARLBUFF)) {
			damage = 1;
			drainMana = 0;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7805 && _pc.hasSkillEffect(PAP_MAGICALPEARLBUFF)) {
			damage = 1;
			drainMana = 0;
		}
		if (_targetNpc.getNpcTemplate().get_gfxid() == 7720) {
			damage = 1;
			drainMana = 0;
		}

		if (_calcType == PC_NPC) {
			if (drainMana > 0) {
				int drainValue = _targetNpc.drainMana(drainMana);
				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);
			}
			_targetNpc.ReceiveManaDamage(_pc, drainMana);
			_targetNpc.receiveDamage(_pc, damage);
		} else if (_calcType == NPC_NPC) {
			_targetNpc.receiveDamage(_npc, damage);
		}
	}

	// ●●●● 전사 타이탄 대미지를 산출 ●●●●
	private int 타이탄대미지() {
		double damage = 0;
		L1ItemInstance weapon = null;
		weapon = _targetPc.getWeapon();
		if (weapon != null) {
			damage = Math.round((weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon.getItem().getDmgModifier()) * 2);
		}
		return (int) damage;
	}
}
