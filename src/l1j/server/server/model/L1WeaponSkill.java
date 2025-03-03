package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FREEZE;
import static l1j.server.server.model.skill.L1SkillId.TURN_UNDEAD;

import java.util.Random;

import l1j.server.Config;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1Skills;

public class L1WeaponSkill {

	private static Random _random = new Random(System.nanoTime());

	private int _weaponId;

	private int _probability;

	private int _fixDamage;

	private int _randomDamage;

	private int _area;

	private int _skillId;

	private int _skillTime;

	private int _effectId;

	private int _effectTarget;

	private boolean _isArrowType;

	private int _attr;

	public L1WeaponSkill(int weaponId, int probability, int fixDamage, int randomDamage, int area, int skillId,
			int skillTime, int effectId, int effectTarget, boolean isArrowType, int attr) {
		_weaponId = weaponId;
		_probability = probability;
		_fixDamage = fixDamage;
		_randomDamage = randomDamage;
		_area = area;
		_skillId = skillId;
		_skillTime = skillTime;
		_effectId = effectId;
		_effectTarget = effectTarget;
		_isArrowType = isArrowType;
		_attr = attr;
	}

	public int getWeaponId() {
		return _weaponId;
	}

	public int getProbability() {
		return _probability;
	}

	public int getFixDamage() {
		return _fixDamage;
	}

	public int getRandomDamage() {
		return _randomDamage;
	}

	public int getArea() {
		return _area;
	}

	public int getSkillId() {
		return _skillId;
	}

	public int getSkillTime() {
		return _skillTime;
	}

	public int getEffectId() {
		return _effectId;
	}

	public int getEffectTarget() {
		return _effectTarget;
	}

	public boolean isArrowType() {
		return _isArrowType;
	}

	public int getAttr() {
		return _attr;
	}

	public static double getWeaponSkillDamage(L1PcInstance pc, L1Character cha, int weaponId) {
		L1WeaponSkill weaponSkill = WeaponSkillTable.getInstance().getTemplate(weaponId);
		if (pc == null || cha == null || weaponSkill == null) {
			return 0;
		}

		int chance = _random.nextInt(100) + 1;
		if (weaponSkill.getProbability() < chance) {
			return 0;
		}

		int skillId = weaponSkill.getSkillId();

		if (skillId == L1SkillId.SILENCE && cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;

			if (npc.getNpcId() == 45684 || npc.getNpcId() == 45683 || npc.getNpcId() == 45681 || npc.getNpcId() == 45682
					|| npc.getNpcId() == 900011 || npc.getNpcId() == 900012 || npc.getNpcId() == 900013
					|| npc.getNpcId() == 900038 || npc.getNpcId() == 900039 || npc.getNpcId() == 900040
					|| npc.getNpcId() == 5096 || npc.getNpcId() == 5097 || npc.getNpcId() == 5098
					|| npc.getNpcId() == 5099 || npc.getNpcId() == 5100) {
				return 0;
			}
		}

		if (skillId != 0) {
			L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
			if (skill != null && skill.getTarget().equals("buff")) {
				if (!isFreeze(cha)) {
					if (skillId == 56) {
						if (!cha.hasSkillEffect(skillId)) {
							cha.addDmgup(-6);
							cha.getAC().addAc(12);
							if (cha instanceof L1PcInstance) {
								L1PcInstance target = (L1PcInstance) cha;
								target.sendPackets(new S_OwnCharAttrDef(target));
							}
						}
					}
					cha.setSkillEffect(skillId, weaponSkill.getSkillTime() * 1000);
				}
			}
		}

		int effectId = weaponSkill.getEffectId();
		if (effectId != 0) {
			int chaId = 0;
			if (weaponSkill.getEffectTarget() == 0) {
				chaId = cha.getId();
			} else {
				chaId = pc.getId();
			}
			boolean isArrowType = weaponSkill.isArrowType();
			if (!isArrowType) {
				pc.sendPackets(new S_SkillSound(chaId, effectId));
				pc.broadcastPacket(new S_SkillSound(chaId, effectId));
			} else {
				S_UseAttackSkill packet = new S_UseAttackSkill(pc, cha.getId(), effectId, cha.getX(), cha.getY(),
						ActionCodes.ACTION_Attack, false);
				pc.sendPackets(packet);
				pc.broadcastPacket(packet, cha);
			}
		}

		double damage = 0;
		int randomDamage = weaponSkill.getRandomDamage();
		if (randomDamage != 0) {
			damage = _random.nextInt(randomDamage);
		}
		damage += weaponSkill.getFixDamage();

		if (effectId == 6985) {
			damage += pc.getAbility().getTotalInt() * 3;
		} else {
			damage += pc.getAbility().getTotalInt() * 2;
		}

		int area = weaponSkill.getArea();
		if (area > 0 || area == -1) {
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha, area)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()) {
					continue;
				}
				if (object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (targetPc.getZoneType() == 1) {
						continue;
					}
				}

				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance || object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance
							|| object instanceof MJCompanionInstance)) {
						continue;
					}
				}
				damage = calcDamageReduction((L1Character) object, damage, weaponSkill.getAttr());
				if (damage <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.broadcastPacket(new S_DoActionGFX(targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) damage);
				} else if (object instanceof L1SummonInstance || object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance || object instanceof MJCompanionInstance) {
					targetNpc = (L1NpcInstance) object;
					targetNpc.broadcastPacket(new S_DoActionGFX(targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) damage);
				}
			}
		}

		return calcDamageReduction(cha, damage, weaponSkill.getAttr());
	}

	public static int 키링크데미지(L1PcInstance pc, L1Character cha) {
		int dmg = 0;
		int dice = 9;
		int diceCount = 5;
		int value = Config.키링크;
		int KiringkuDamage = 0;
		int charaIntelligence = 0;

		for (int i = 0; i < diceCount; i++) {
			KiringkuDamage += (_random.nextInt(dice) + 1);
		}
		KiringkuDamage += value;

		int spByItem = pc.getAbility().getSp() - pc.getAbility().getTrueSp();
		charaIntelligence = pc.getAbility().getTotalInt() + spByItem - 12;
		if (charaIntelligence < 1) {
			charaIntelligence = 1;
		}
		double KiringkuCoefficientA = (1.0 + charaIntelligence * 3.0 / 32.0);

		KiringkuDamage *= KiringkuCoefficientA;

		double Mrfloor = 0;
		if (cha.getResistance().getEffectedMrBySkill() <= 100) {
			Mrfloor = Math.floor((cha.getResistance().getEffectedMrBySkill() - pc.getTotalMagicHitup()) / 2);
		} else if (cha.getResistance().getEffectedMrBySkill() >= 100) {
			Mrfloor = Math.floor((cha.getResistance().getEffectedMrBySkill() - pc.getTotalMagicHitup()) / 10);
		}

		double KiringkuCoefficientB = 0;
		if (cha.getResistance().getEffectedMrBySkill() <= 100) {
			KiringkuCoefficientB = 1 - 0.01 * Mrfloor;
		} else if (cha.getResistance().getEffectedMrBySkill() > 100) {
			KiringkuCoefficientB = 0.6 - 0.01 * Mrfloor;
		}

		double Kiringkufloor = Math.floor(KiringkuDamage);

		dmg += Kiringkufloor + (pc.getWeapon().getEnchantLevel() * 2);

		dmg *= KiringkuCoefficientB;

		if (pc.getWeapon().getItem().getItemId() == 503) {
			pc.sendPackets(new S_SkillSound(pc.getId(), 6983));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 6983));
		} else {
			pc.sendPackets(new S_SkillSound(pc.getId(), 7049));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7049));
		}
		return dmg;
	}

	public static double 악운의단검(L1PcInstance pc, L1PcInstance targetPc, L1ItemInstance weapon) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (5 >= chance) {
			dmg = targetPc.getCurrentHp() / 1.2;
			if (targetPc.getCurrentHp() - dmg < 0) {
				dmg = 0;
			}
			String msg = weapon.getLogName();
			pc.sendPackets(new S_ServerMessage(158, msg));
			pc.getInventory().removeItem(weapon, 1);
		}
		L1PinkName.onAction(targetPc, pc);
		return dmg;
	}

	public static void 체인소드(L1PcInstance pc) { // 체인소드 약점노출
		int chance = 15;
		if (pc.getInventory().checkEquipped(7000020)) { //린드비오르의 체인소드
			chance += 5;
		}
	if (_random.nextInt(100) < chance){
		if (pc.hasSkillEffect(L1SkillId.CHAINSWORD1)) {
			pc.killSkillEffectTimer(L1SkillId.CHAINSWORD1);
			pc.setSkillEffect(L1SkillId.CHAINSWORD2, 15000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 2), true);
		} else if (pc.hasSkillEffect(L1SkillId.CHAINSWORD2)) {
			pc.killSkillEffectTimer(L1SkillId.CHAINSWORD2);
			pc.setSkillEffect(L1SkillId.CHAINSWORD3, 15000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 3), true);
		} else if (pc.hasSkillEffect(L1SkillId.CHAINSWORD3)) {
			if (pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
				pc.setSkillEffect(L1SkillId.CHAINSWORD4, 15000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 4), true);
			}
		} else if (pc.hasSkillEffect(L1SkillId.CHAINSWORD4)) {
		} else {
			pc.setSkillEffect(L1SkillId.CHAINSWORD1, 15000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
		}
	}
}
	
	
	public static void getDiseaseWeapon(L1PcInstance pc, L1Character cha, int weaponid) {
		int chance = _random.nextInt(100) + 1;
		int skilltime = weaponid == 412003 ? 64 : 20;
		if (7 >= chance) {
			if (!cha.hasSkillEffect(56)) {
				cha.addDmgup(-6);
				cha.getAC().addAc(12);
				if (cha instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) cha;
					target.sendPackets(new S_OwnCharAttrDef(target));
				}
			}
			cha.setSkillEffect(56, skilltime * 1000);
			pc.sendPackets(new S_SkillSound(cha.getId(), 2230));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 2230));
			if (cha.hasSkillEffect(ERASE_MAGIC))
				cha.removeSkillEffect(ERASE_MAGIC);
		}
	}

	public static double 블레이즈쇼크(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int randmg = _random.nextInt(50) + 20;
			dmg = randmg;

			if (dmg < 20)
				dmg = 20;

			pc.sendPackets(new S_SkillSound(cha.getId(), 3939));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 3939));
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 블레이즈쇼크1(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int randmg = _random.nextInt(50) + 25;
			dmg = randmg;

			if (dmg < 25)
				dmg = 25;

			pc.sendPackets(new S_SkillSound(cha.getId(), 3939));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 3939));
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 체인소드블레이즈쇼크(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int randmg = _random.nextInt(50) + 20;
			dmg = randmg;

			if (dmg < 20)
				dmg = 20;

			pc.sendPackets(new S_SkillSound(cha.getId(), 3939));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 3939));
		}
		if (_random.nextInt(100) < 15) {
			if (pc.hasSkillEffect(L1SkillId.CHAINSWORD1)) {
				pc.killSkillEffectTimer(L1SkillId.CHAINSWORD1);
				pc.setSkillEffect(L1SkillId.CHAINSWORD2, 15000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 2), true);
			} else if (pc.hasSkillEffect(L1SkillId.CHAINSWORD2)) {
				pc.killSkillEffectTimer(L1SkillId.CHAINSWORD2);
				pc.setSkillEffect(L1SkillId.CHAINSWORD3, 15000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 3), true);
			} else if (pc.hasSkillEffect(L1SkillId.CHAINSWORD3)) {
				if (pc.isPassive(MJPassiveID.FOU_SLAYER_BRAVE.toInt())) {
					pc.setSkillEffect(L1SkillId.CHAINSWORD4, 15000);
					pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 4), true);
				}
			} else if (pc.hasSkillEffect(L1SkillId.CHAINSWORD4)) {
			} else {
				pc.setSkillEffect(L1SkillId.CHAINSWORD1, 15000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1), true);
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 환영의체인소드(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int chance = _random.nextInt(100) + 1;
		if (5 + enchant >= chance) {
			dmg = 15;
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, 7398);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER);
	}

	public static double 군주의검(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (2 + enchant >= chance) {
			dmg = _random.nextInt(intel / 2) + (intel);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER);
	}

	public static double 파괴의이도류크로우(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			dmg = _random.nextInt(intel / 2) + (intel * 2);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_EARTH);
	}

	public static double 광풍의도끼(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (2 + enchant >= chance) {
			dmg = _random.nextInt(intel / 2) + (intel * 2);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 혹한의창(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (2 + enchant >= chance) {
			dmg = _random.nextInt(intel / 2) + (intel);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 살천의활(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int dex = pc.getAbility().getTotalDex();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			dmg = _random.nextInt(dex) + (dex);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double getMoonBowDamage(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int dex = pc.getAbility().getTotalDex();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			dmg = _random.nextInt(dex) + (dex);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_UseAttackSkill packet = new S_UseAttackSkill(pc, cha.getId(), 6288, cha.getX(), cha.getY(),
					ActionCodes.ACTION_Attack, false);
			pc.sendPackets(packet);
			Broadcaster.broadcastPacket(pc, packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 뇌신검(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			dmg = _random.nextInt(intel) + (intel * 2);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 공명의키링크(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (2 + enchant >= chance) {
			dmg = _random.nextInt(intel) + 20 + (intel * 2);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 냉한의키링크(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (6 + enchant >= chance) {// 3
			dmg = _random.nextInt(intel / 2) + (intel * 1.2);
			if (cha.getCurrentMp() >= 5) {
				cha.setCurrentMp(cha.getCurrentMp() - 5);
				if (dmg <= 0) {
					dmg = 0;
				}
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER);
	}

	public static double 린드비오르의키링크(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {// 3
			dmg = _random.nextInt(intel / 2) + (intel * 2.4);
			if (cha.getCurrentMp() >= 5) {
				cha.setCurrentMp(cha.getCurrentMp() - 5);
				if (dmg <= 0) {
					dmg = 0;
				}
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}
	
	public static double 바람칼날의단검(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		int effect = (enchant <= 5) ? 22924 : 22920; // 인챈트 레벨에 따른 이펙트 ID 결정
		if (1 + enchant >= chance) {
			dmg = _random.nextInt(intel) + (intel * 2);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}
	
	public static double 진명황의집행검(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		int effect = (enchant <= 5) ? 18340 : 22850; // 인챈트 레벨에 따른 이펙트 ID 결정
		if (5 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.5);
			int min = 20 + div;
			int max = 50 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}
	
	public static double 붉은그림자의이도류(L1PcInstance pc, L1Character cha, int enchant) {
	    double dmg = 0;
	    int fettersTime = 2000; // 이펙트 타이머
	    int effect = (enchant <= 5) ? 22854 : 22911; // 인챈트 레벨에 따른 이펙트 ID 결정

	    if (isFreeze(cha)) {
	        return dmg;
	    }
	    if ((_random.nextInt(100) + 1) <= 10) {
	        L1EffectSpawn.getInstance().spawnEffect(81182, fettersTime, cha.getX(), cha.getY(), cha.getMapId());
	        if (cha instanceof L1PcInstance) {
	            L1PcInstance targetPc = (L1PcInstance) cha;
	            targetPc.setSkillEffect(STATUS_FREEZE, fettersTime);
	            targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184));
	            targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184));
	            targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
	        } else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance
	                || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
	            L1NpcInstance npc = (L1NpcInstance) cha;
	            npc.setSkillEffect(STATUS_FREEZE, fettersTime);
	            npc.broadcastPacket(new S_SkillSound(npc.getId(), 4184));
	            npc.setParalyzed(true);
	        }
	        dmg = _random.nextInt(enchant * 2 + 5) + ((pc.getSp() + pc.getAbility().getTotalInt()) * 2); // 대미지 계산 로직 추가

	        // 이펙트 발동
	        S_EffectLocation packet = new S_EffectLocation(cha.getX(), cha.getY(), effect);
	        pc.sendPackets(packet);
	        pc.broadcastPacket(packet);
	    }
	    return dmg;
	}

	public static double 수정결정체지팡이(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		int effect = (enchant <= 5) ? 10405 : 22908; // 인챈트 레벨에 따른 이펙트 ID 결정
		if (20 + enchant >= chance && 5 <= chance) {
			dmg = _random.nextInt(sp + intel) + ((sp + intel) * 6);
			if (5 >= chance) {
				dmg += 50;
			} else if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}
	
	public static double 가이아의격노(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int dex = pc.getAbility().getTotalDex();
		int chance = _random.nextInt(100) + 1;
		int effect = (enchant <= 5) ? 19465 : 22913; // 인챈트 레벨에 따른 이펙트 ID 결정
		if (1 + enchant >= chance) {
			dmg = _random.nextInt(dex) + (dex);
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}
	
	// 기존 공식 너무 쎄다
//	public static double 히페리온의절망(L1PcInstance pc, L1Character cha, int enchant) {
//	    double dmg = 0;
//	    int locx = cha.getX();
//	    int locy = cha.getY();
//	    int sp = pc.getSp();
//	    int intel = pc.getAbility().getTotalInt();
//	    int chance = _random.nextInt(100) + 1;
//	    int effect = (enchant <= 5) ? 12248 : 22860; // 인챈트 레벨에 따른 이펙트 ID 결정
//
//	    if (5 + enchant >= chance) { // 기존 10
////	        dmg = _random.nextInt(enchant * 2 + 5) + ((sp + intel) * 10); // 기존 공식
//	        dmg = _random.nextInt(sp + intel) + ((sp + intel) * 6); // 수결지와 동일한 데미지 공식
//	        if (cha.getCurrentMp() >= 5) {
//	            cha.setCurrentMp(cha.getCurrentMp() - 10);
//	            if (5 >= chance) { // 기존 5 (이펙트 랜타 찬스)
////	                dmg += dmg * 0.7; // 기존 데미지 공격
//	            	dmg += 50; // 수결지와 동일한 데미지 공식
//	            } else if (dmg <= 0) {
//	                dmg = 0;
//	            }
//	        }
//	        S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
//	        pc.sendPackets(packet);
//	        pc.broadcastPacket(packet);
//	    }
//	    return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER);
//	}
	
	// 히페리온의 절망 로직 변경
	public static double 히페리온의절망(L1PcInstance pc, L1Character cha, int enchant) {
	    double dmg = 0; // 최종 데미지
	    int locx = cha.getX(); // 대상의 X 좌표
	    int locy = cha.getY(); // 대상의 Y 좌표
	    int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
	    int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
	    int effect = (enchant <= 5) ? 12248 : 22860; // 인챈트 레벨에 따른 이펙트 ID 결정

	    if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
	        int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
	        int min = 160 + div; // 최소 데미지 범위
	        int max = 420 + div; // 최대 데미지 범위
	        dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
	        dmg *= 0.7; // 데미지를 70%로 조정 (30% 감소)
	        
	        if (dmg <= 0)
	            dmg = 0; // 데미지가 음수인 경우 0으로 설정
	        
	        // 대상에게 스킬 효과를 전달하는 코드
	        S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
	        pc.sendPackets(packet);
	        pc.broadcastPacket(packet);
	    }
	    return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE); // 최종 데미지 반환
	}

	
	public static double 크로노스의공포(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		int effect = (enchant <= 5) ? 22846 : 22856; // 인챈트 레벨에 따른 이펙트 ID 결정
		if (5 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.4);
			int min = 15 + div;
			int max = 40 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}
	
	public static double 타이탄의분노(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		int effect = (enchant <= 5) ? 19693 : 22918; // 인챈트 레벨에 따른 이펙트 ID 결정
		if (5 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.4); // 원래 1.2
			int min = 13 + div;
			int max = 47 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}


	public static double 진싸울아비대검(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (7 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.5);
			int min = 20 + div;
			int max = 50 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 발라카스의장검(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.7);
			int min = 20 + div;
			int max = 50 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}
	
	public static double 발라카스의양손검(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.7);
			int min = 20 + div;
			int max = 50 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 악몽의장궁(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int dex = pc.getAbility().getTotalDex();
		int chance = _random.nextInt(100) + 1;
		if (4 + enchant >= chance) {
			int div = (int) ((dex / 10) * 1.2);
			int min = 20 + div;
			int max = 50 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 파푸리온의장궁(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int dex = pc.getAbility().getTotalDex();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			int div = (int) ((dex / 10) * 1.4);
			int min = 20 + div;
			int max = 50 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 섬멸자의체인소드(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (7 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.2);
			int min = 15 + div;
			int max = 40 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 린드비오르의체인소드(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.4);
			int min = 15 + div;
			int max = 40 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 태풍의도끼(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (5 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.1); // 원래 1.2
			int min = 13 + div;
			int max = 47 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 안타라스의도끼(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.3);
			int min = 13 + div;
			int max = 47 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_EARTH);
	}

	public static double 파푸리온의이도류(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (1 + enchant >= chance) {
			int div = (int) ((Str / 10) * 1.2);
			int min = 13 + div;
			int max = 47 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER);
	}

	public static double 사신의검(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (5 + enchant >= chance) {
			int div = (int) ((Str / 10) * 6); // 원래 1.2
			int min = 13 + div;
			int max = 47 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 기르타스의검(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (3 + enchant >= chance) {
			int div = (int) ((Str / 10) * 12); // 원래 1.2
			int min = 13 + div;
			int max = 47 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 아인하사드의섬광PC(L1PcInstance pc, L1Character cha, int enchant) {
		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 힘 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 힘 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12337)); // 플레이어에게 섬광 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12337)); // 모든 유저에게 섬광 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
			L1PcInstance targetPc = (L1PcInstance) cha;
			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER); // 최종 데미지 반환
	}
	
	public static double 아인하사드의섬광(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 힘 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 힘 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12343)); // 플레이어에게 스킬 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12343)); // 모든 유저에게 스킬 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER); // 최종 데미지 반환
	}

	public static double 그랑카인의심판PC(L1PcInstance pc, L1Character cha, int enchant) {
		int fettersTime = 2000;
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (7 + enchant >= chance) {
			int div = (int) ((Str / 10) * 10); // 원래 1.2
			int min = 160 + div;
			int max = 420 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			pc.sendPackets(new S_SkillSound(cha.getId(), 12264));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12264));
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime);
				targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184));
				targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184));
				targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
				}
			
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 그랑카인의심판(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (7 + enchant >= chance) {
			int div = (int) ((Str / 10) * 10); // 원래 1.2
			int min = 160 + div;
			int max = 420 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			pc.sendPackets(new S_SkillSound(cha.getId(), 12268));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12268));
			
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 사이하의집념PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Dex = pc.getAbility().getTotalDex(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Dex / 10) * 10); // 덱스 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12389)); // 플레이어에게 섬광 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12389)); // 모든 유저에게 섬광 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 사이하의집념(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Dex = pc.getAbility().getTotalDex(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Dex / 10) * 10); // 덱스 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12391)); // 플레이어에게 스킬 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12391)); // 모든 유저에게 스킬 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}	
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 실렌의결의PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12395)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12395)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 실렌의결의(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12399)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12399)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}	
	
	
	
	
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 에바의서약PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12511)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12511)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 에바의서약(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12517)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12517)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}	

	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 파아그리오의공포PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12668)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12668)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 파아그리오의공포(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12672)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12672)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}	
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 마프르의고뇌PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12687)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12687)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 마프르의고뇌(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 12694)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 12694)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}		
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 단테스의시련PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13332)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13332)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 단테스의시련(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13336)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13336)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 테이아의혼돈PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13436)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13436)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE); // 최종 데미지 반환
	}
	
	public static double 테이아의혼돈(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13438)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13438)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 아우라키아의초월PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13444)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13444)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 아우라키아의초월(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13446)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13446)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	/**
	 * 이 스킬은 PC (플레이어 캐릭터)를 대상으로 하며, 대상에게 데미지를 입히고 추가 효과를 부여합니다. 데미지 계산 후에는 대상에게 홀드를 부여합니다.
	 * */
	public static double 아인하사드의구원PC(L1PcInstance pc, L1Character cha, int enchant) {
//		int fettersTime = 2000; //적에게 걸리는 효과의 지속 시간
		double dmg = 0; // 최종 대미지
		int locx = cha.getX(); // 대상의 X좌표
		int locy = cha.getY(); // 대상의 Y좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때

			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13470)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13470)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
			if (cha instanceof L1PcInstance) { // 대상이 플레이어인 경우
//			L1PcInstance targetPc = (L1PcInstance) cha;
//			targetPc.setSkillEffect(L1SkillId.STATUS_FREEZE, fettersTime); // 대상에게 홀드 효과 부여
//			targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184)); // 대상에게 홀드 효과 소리 전송
//			targetPc.broadcastPacket(new S_SkillSound(targetPc.getId(), 4184)); // 모든 유저에게 동상 효과 소리 브로드캐스트
//			targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true)); // 대상에게 마비 효과 전송
			}
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}
	
	public static double 아인하사드의구원(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0; // 최종 데미지
		int locx = cha.getX(); // 대상의 X 좌표
		int locy = cha.getY(); // 대상의 Y 좌표
		int Str = pc.getAbility().getTotalStr(); // 플레이어의 총 스탯
		int chance = _random.nextInt(100) + 1; // 확률 계산을 위한 랜덤값 생성 (1~100)
		if (7 + enchant >= chance) { // 확률 계산 조건: (7 + 인첸트 값)이 확률보다 크거나 같을 때
			int div = (int) ((Str / 10) * 10); // 스탯을 10으로 나눈 후 10을 곱하여 소수점 이하를 버림
			int min = 160 + div; // 최소 데미지 범위
			int max = 420 + div; // 최대 데미지 범위
			dmg = _random.nextInt(max - min) + min; // 최종 데미지 계산
			if (dmg <= 0)
				dmg = 0; // 데미지가 음수인 경우 0으로 설정
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			// 대상에게 스킬 효과를 전달하는 코드
			pc.sendPackets(new S_SkillSound(cha.getId(), 13474)); // 플레이어에게 이펙트 효과 소리 전송
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13474)); // 모든 유저에게 이펙트 효과 소리 브로드캐스트
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND); // 최종 데미지 반환
	}

	public static double 드래곤슬레이어(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int Str = pc.getAbility().getTotalStr();
		int chance = _random.nextInt(100) + 1;
		if (5 + enchant >= chance) {// 찬스
			dmg = _random.nextInt(Str) + (Str);
			if (3 >= chance) {// 이펙트 랜타 찬스
				dmg += dmg * 0.1;// 대미지
			} else if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double Redskill(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (3 + enchant >= chance) {
			dmg = _random.nextInt(intel / 2) + (intel * 3);
			if (cha.getCurrentMp() >= 5) {
				cha.setCurrentMp(cha.getCurrentMp() - 5);
				if (dmg <= 0) {
					dmg = 0;
				}
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WATER);
	}

	public static double 커츠의검(L1PcInstance pc, L1Character cha, int enchant, int effect) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (5 + enchant >= chance) {
			int div = (int) ((intel + (sp) / 10) * 1.2);
			int min = 10 + div;
			int max = 40 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static double 데스나이트의불검(L1PcInstance pc, L1Character cha, int enchant, int effect) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (7 + enchant >= chance) {
			int div = (int) ((intel + (sp) / 10) * 1.2);
			int min = 10 + div;
			int max = 40 + div;
			dmg = _random.nextInt(max - min) + min;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 바포메트의지팡이(L1PcInstance pc, L1Character cha, int enchant, int effect) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (15 + enchant >= chance) {
			dmg = _random.nextInt(enchant * 2 + 3) + ((sp + intel) * 3); // 리뉴얼로상향
			if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_EARTH);
	}

	public static double 제로스의지팡이(L1PcInstance pc, L1Character cha, int enchant, int effect) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (7 + enchant >= chance) {
			int div = (int) ((intel + (sp) / 10) * 2.8);
			int min = 15 + div;
			int max = 45 + div;
			dmg = _random.nextInt(sp + intel) + ((sp + intel) * 2.5) + sp * 2;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 제로스의지팡이1(L1PcInstance pc, L1Character cha, int enchant, int effect) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (10 + enchant >= chance) {
			int div = (int) ((intel + (sp) / 10) * 2.8);
			int min = 15 + div;
			int max = 45 + div;
			dmg = _random.nextInt(sp + intel) + ((sp + intel) * 2) + sp * 2;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_FIRE);
	}

	public static double 안타라스의지팡이(L1PcInstance pc, L1Character cha, int enchant, int effect) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (7 + enchant >= chance) {
			int div = (int) ((intel + (sp) / 10) * 5.6);
			int min = 15 + div;
			int max = 45 + div;
			dmg = _random.nextInt(sp + intel) + ((sp + intel) * 2.5) + sp * 2;
			if (dmg <= 0)
				dmg = 0;
			// dex 20일때, 최소 23~최대 53
			// 70일때, 최소 37~최대 67

			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_EARTH);
	}

	public static double 지배자의지팡이(L1PcInstance pc, L1Character cha, int enchant, int effect) {
		double dmg = 0;
		int locx = cha.getX();
		int locy = cha.getY();
		int sp = pc.getSp();
		int intel = pc.getAbility().getTotalInt();
		int chance = _random.nextInt(100) + 1;
		if (20 + enchant >= chance && 5 <= chance) {
			dmg = _random.nextInt(sp + intel) + ((sp + intel) * 6);
			if (5 >= chance) {
				dmg += 50;
			} else if (dmg <= 0) {
				dmg = 0;
			}
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_WIND);
	}

	public static void 천사의지팡이(L1PcInstance pc, L1Character cha, int enchant) {
		int chance = _random.nextInt(100) + 1;
		int undeadType = ((L1MonsterInstance) cha).getNpcTemplate().get_undead();
		if (undeadType == 1 || undeadType == 3) {
			if (enchant >= chance) {
				new L1SkillUse().handleCommands(pc, TURN_UNDEAD, cha.getId(), cha.getX(), cha.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
		}
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
}
