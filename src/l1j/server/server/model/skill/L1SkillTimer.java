package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.CrockController;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_IvenBuffIcon;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillIconWisdomPotion;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.templates.L1Skills;

public class L1SkillTimer implements Runnable {

	public L1SkillTimer(L1Character cha, int skillId, int timeMillis) {
		_cha = cha;
		_skillId = skillId;
		_timeMillis = timeMillis;

		_remainingTime = _timeMillis / 1000;
		_stop = false;
	}

	@Override
	public void run() {
		if (_stop) {
			return;
		}
		if (_skillId == EXP_POTION) {
			if (!_cha.getMap().isSafetyZone(_cha.getX(), _cha.getY())) {
				_remainingTime--;
			}
		} else {
			_remainingTime--;
		}

		if (_remainingTime <= 0) {
			_cha.removeSkillEffect(_skillId);
			return;
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	public void end() {
		_stop = true;
		L1SkillStop.stopSkill(_cha, _skillId);
		_cha = null;
	}

	public void kill() {
		_stop = true;
		_cha = null;
	}

	public int getRemainingTime() {
		return _remainingTime;
	}

	private L1Character _cha;
	private final int _timeMillis;
	private final int _skillId;
	private int _remainingTime;
	private boolean _stop;
}

class L1SkillStop {
	public static void stopSkill(L1Character cha, int skillId) {
		if(cha instanceof MJCompanionInstance){
			if(((MJCompanionInstance) cha).on_buff_stopped(skillId))
				return;
		}
		
		switch (skillId) {
		case HALPAS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha; 
				pc.setAddReduction(pc.getAddReduction() - 5);
			}
			break;
		case N_BUFF_PVP_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addPVPweaponTotalDamage(-1);
				pc.getResistance().addcalcPcDefense(-1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4346, 0));
			}
			break;
		case N_BUFF_HPMP:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addMaxMp(-50);
				pc.addWeightReduction(-3);
				pc.sendPackets(new S_HPUpdate(pc));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4347, 0));
			}
			break;
		case N_BUFF_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-1);
				pc.addBowDmgup(-1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4348, 0));
			}
			break;
		case N_BUFF_REDUCT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4349, 0));
			}
			break;
		case N_BUFF_SP:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-1);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4350, 0));
			}
			break;
		case N_BUFF_STUN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.ABILITY, -2); // 옵션
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4351, 0));
			}
			break;
		case N_BUFF_HOLD:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.SPIRIT, -2); // 옵션
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4352, 0));
			}
			break;
		case N_BUFF_WATER_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4342, 0));
			}
			break;
		case N_BUFF_WIND_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4343, 0));
			}
			break;
		case N_BUFF_EARTH_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4344, 0));
			}
			break;
		case N_BUFF_FIRE_DMG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4345, 0));
			}
			break;
		case N_BUFF_STR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4338, 0));
			}
			break;
		case N_BUFF_DEX:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedDex(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4339, 0));
			}
			break;
		case N_BUFF_INT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedInt(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4340, 0));
			}
			break;
		case N_BUFF_WIS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedWis(-(byte) 1);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4341, 0));
			}
			break;
		case N_BUFF_WATER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addWater(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4333, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_WIND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addWind(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4334, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_EARTH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addEarth(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4335, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_FIRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addFire(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4336, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case N_BUFF_ALL_RESIST:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addFire(-5);
				pc.getResistance().addEarth(-5);
				pc.getResistance().addWater(-5);
				pc.getResistance().addWind(-5);
				pc.sendPackets(new S_IvenBuffIcon(skillId, false, 4337, 0));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;

		case FEAR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDg(25);
			}
			break;
		case L1SkillId.TELEPORT_RULER:
			break;

		case L1SkillId.EXP_POTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.getSafetyZone())
					pc.sendPackets(S_InventoryIcon.icoEnd(EXP_POTION + 1));
				else
					pc.sendPackets(S_InventoryIcon.icoEnd(EXP_POTION));
			}
			break;

		case L1SkillId.EXP_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				long hasad = pc.getAccount().getBlessOfAin();
				if (hasad <= 10000) {
					pc.sendPackets(S_InventoryIcon.icoEnd(EXP_BUFF + 1));
				} else {
					pc.sendPackets(S_InventoryIcon.icoEnd(EXP_BUFF));
				}
			}
			break;
		case MAJESTY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(MAJESTY);
				noti.set_duration(0);
				noti.set_off_icon_id(9518);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case IMMUNE_TO_HARM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(IMMUNE_TO_HARM);
				noti.set_duration(0);
				noti.set_off_icon_id(1562);
				noti.set_end_str_id(315);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				pc.setLastImmuneLevel(0);
			}
			break;

		case DRAGON_HUNTER_BLESS: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
			}
		}
			break;
		case miso1: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
			}
		}
			break;
		case miso2: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-100);
				pc.getResistance().addMr(-10);
				pc.addHpr(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
			break;
		case miso3: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-3);
				pc.addBowDmgup(-3);
				pc.addMaxMp(-50);
				pc.getAbility().addSp(-3);
				pc.addMpr(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
			break;
		case DRAGON_SET: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-3);
				pc.addDmgRate(-3);
				pc.addBowDmgup(-3);
				pc.addBowHitup(-3);
				pc.getAC().addAc(3);
				pc.getAbility().addSp(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
			break;
		case HUNTER_BLESS: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
			}
		}
			break;
		case HUNTER_BLESS1: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
		}
			break;
		case HUNTER_BLESS2: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
			}
		}
			break;
		case POLY_RING_MASTER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.ICON_BUFF, 8228, false, 5006, 0));
				
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedCha((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.addMaxHp(-3000);
				
				L1PolyMorph.undoPoly(pc);
			}
			break;
		case POLY_RING_MASTER2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.ICON_BUFF, 8228, false, 5021, 0));
				
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedCha((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.addMaxHp(-5000);
				
				L1PolyMorph.undoPoly(pc);
			}
			break;
		case 수련자의한우스테이크:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-1);
				pc.addDmgup(-2);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case 수련자의연어찜:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowHitup(-1);
				pc.addBowDmgup(-2);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case 수련자의칠면조구이:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHpr(-2);
				pc.addMpr(-3);
				pc.getAbility().addSp(-2);
				pc.getResistance().addMr(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;

		case THUNDER_GRAB:
			if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.set발묶임상태(false);
			}
			break;
		case SOUL_BARRIER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(SOUL_BARRIER);
			}
			break;

		case DOUBLE_BRAKE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 2949, 0, false, true));
			}
			break;

		case FOCUS_SPRITS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_magic_critical_rate(-5);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(FOCUS_SPRITS);
				noti.set_duration(0);
				noti.set_off_icon_id(4832);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case CUBE_RICH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-2);
				pc.sendPackets(new S_SPMR(pc));
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(CUBE_RICH);
				noti.set_duration(0);
				noti.set_off_icon_id(5309);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case CUBE_GOLEM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(8);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(CUBE_GOLEM);
				noti.set_duration(0);
				noti.set_off_icon_id(5314);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case CUBE_OGRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-4);
				pc.addHitup(-4);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(CUBE_OGRE);
				noti.set_duration(0);
				noti.set_off_icon_id(5312);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case CUBE_AVATAR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(CUBE_AVATAR);
				noti.set_duration(0);
				noti.set_off_icon_id(5322);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case IMPACT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialPierce(eKind.ALL, -pc.getImpactUp());
				pc.setImpactUp(0);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				pc.sendPackets(S_InventoryIcon.icoEnd(IMPACT));
			}
			break;

		case LUCIFER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(LUCIFER);
				noti.set_duration(0);
				noti.set_off_icon_id(4503);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case BLOW_ATTACK:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(BLOW_ATTACK);
				noti.set_duration(0);
				noti.set_off_icon_id(8843);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case ARMOR_BRAKE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(ARMOR_BRAKE);
				noti.set_duration(0);
				noti.set_off_icon_id(4473);
				noti.set_is_good(false);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case TITANL_RISING:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(TITANL_RISING);
				pc.setRisingUp(0);
			}
			break;
		case ABSOLUTE_BLADE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(ABSOLUTE_BLADE);
			}
			break;
		case DEATH_HEAL_Mob:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(DEATH_HEAL_Mob);
			}
			break;
		case DEATH_HEAL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.removeSkillEffect(DEATH_HEAL);
			}
			break;
		case GRACE_AVATAR:
			// 그레이스 아바타 - 처리되는 부분 수정.
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int resistance = (5 + pc.getGraceLv());
				pc.addSpecialResistance(eKind.ALL, -resistance);
				pc.removeSkillEffect(GRACE_AVATAR);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		/** 혈맹버프 **/
		case CLAN_BUFF1: {// 일반 공격 태세
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDmgupByArmor(-2);
			pc.addBowDmgupByArmor(-2);
			pc.sendPackets(new S_ServerMessage(4619, "$22503"));
		}
			break;
		case CLAN_BUFF2: {// 일반 방어 태세
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getAC().addAc(3);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.sendPackets(new S_ServerMessage(4619, "$22504"));
		}
			break;
		case CLAN_BUFF3: {// 전투 공격 태세
			L1PcInstance pc = (L1PcInstance) cha;
			// pc.addPvPDmgup(-1);
			pc.sendPackets(new S_ServerMessage(4619, "$22505"));
		}
			break;
		case CLAN_BUFF4: {// 전투 방어 태세
			L1PcInstance pc = (L1PcInstance) cha;
			// pc.addDmgReducPvp(-1);
			pc.sendPackets(new S_ServerMessage(4619, "$22506"));
		}
			break;
		case 정상의가호:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDamageReductionByArmor(-8);
			}
			break;
		case L1SkillId.레벨업보너스:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, true, true));
			}
			break;
		case L1SkillId.DRAGON_PUPLE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, 1, true, true));
			}
			break;
		case L1SkillId.DRAGON_TOPAZ:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(0, 2, true, true));
			}
			break;
		case 나루토감사캔디:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.getLevel() >= 1 && pc.getLevel() <= 60) {
					pc.getAbility().addAddedDex((byte) -7);
					pc.sendPackets(new S_Dexup(pc, 1, 0));
					pc.getAbility().addAddedStr((byte) -7);
					pc.sendPackets(new S_Strup(pc, 1, 0));
				} else {
					pc.getAbility().addAddedDex((byte) -6);
					pc.sendPackets(new S_Dexup(pc, 1, 0));
					pc.getAbility().addAddedStr((byte) -6);
					pc.sendPackets(new S_Strup(pc, 1, 0));
				}
			}
			break;
		case DRESS_EVASION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addEffectedER(-18);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_OwnCharStatus(pc));
				
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(DRESS_EVASION);
				noti.set_duration(0);
				noti.set_end_str_id(2203);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case AQUA_PROTECTER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addEffectedER(-5);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(AQUA_PROTECTER);
				noti.set_duration(0);
				noti.set_end_str_id(2201);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case SOLID_CARRIAGE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(SOLID_CARRIAGE);
				noti.set_duration(0);
				noti.set_end_str_id(2203);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);

				pc.addEffectedER(-15);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			break;
		case STRIKER_GALE:// -99
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(STRIKER_GALE);
				noti.set_duration(0);
				noti.set_end_str_id(2200);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				
				
				// pc.Add_Er(99);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			break;
		case 800018:// 티칼
			if (!cha.isDead()) {
				CrockController.getInstance().dieCount(0);
				CrockController.getInstance().BossSpawn(800018, 32753, 32870, (short) 784, 1900000);
			}
			break;
		case 800019:// 티칼
			if (!cha.isDead()) {
				CrockController.getInstance().dieCount(0);
				CrockController.getInstance().BossSpawn(800019, 32750, 32859, (short) 784, 1900000);
			}
			break;
		case LIGHT:
			if (cha instanceof L1PcInstance) {
				if (!cha.isInvisble()) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
				}
			}
			break;
		case TRUE_TARGET:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.set트루타켓(0);
			}
			Broadcaster.broadcastPacket(cha, new S_TrueTargetNew(cha.getId(), false));
			synchronized (L1SkillUse._truetarget_list) {
				List<Integer> remove_list = new ArrayList<Integer>();
				for (Integer id : L1SkillUse._truetarget_list.keySet()) {
					L1Object o = L1SkillUse._truetarget_list.get(id);
					if (o.getId() != cha.getId())
						continue;
					remove_list.add(id);
				}
				for (Integer id : remove_list)
					L1SkillUse._truetarget_list.remove(id);
			}
			break;
		case GLOWING_AURA:
			cha.addHitup(-5);
			cha.addBowHitup(-5);
			cha.getResistance().addMr(-20);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_SkillIconAura(113, 0));
			}
			break;
		case God_buff: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getAC().addAc(2);
			pc.addHitup(-3);
			pc.addMaxHp(-20);
			pc.addMaxMp(-13);
			pc.addSpecialResistance(eKind.SPIRIT, -10);
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
		}
			break;
		case DELAY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (!pc.hasSkillEffect(L1SkillId.DELAY)) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.MINIGAME_END));
				}
			}
			break;
		case BUFF_SAEL: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.hasSkillEffect(L1SkillId.BUFF_SAEL)) {
					pc.removeSkillEffect(L1SkillId.BUFF_SAEL);
				}
				pc.getAC().addAc(8);
				pc.addBowHitup(-6);
				pc.addBowDmgup(-3);
				pc.addMaxHp(-80);
				pc.addMaxMp(10);
				pc.addHpr(-8);
				pc.addMpr(-1);
				pc.getResistance().addWater(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_SPMR(pc));
			}
		}
			break;
		case SHINING_AURA:
			cha.getAC().addAc(8);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(114, 0));
			}
			break;
		case BRAVE_AURA:
			cha.addDmgup(-5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(116, 0));
			}
			break;
		case SHIELD:
			cha.getAC().addAc(2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(1, 0));
			}
			break;
		case BLIND_HIDING:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.delBlindHiding();
			}
			break;
		case SHADOW_ARMOR:
			cha.getResistance().addMr(-5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
				// pc.sendPackets(new S_SkillIconShield(3, 0));
			}
			break;
		case DRESS_DEXTERITY:
			cha.getAbility().addAddedDex((byte) -3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Dexup(pc, 3, 0));
			}
			break;
		case DRESS_MIGHTY:
			cha.getAbility().addAddedStr((byte) -3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Strup(pc, 3, 0));
			}
			break;
		case EARTH_GUARDIAN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(7, 0));
			}
			break;
		case RESIST_MAGIC:
			cha.getResistance().addMr(-10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case CLEAR_MIND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(CLEAR_MIND);
				noti.set_duration(0);
				noti.set_off_icon_id(5279);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case RESIST_ELEMENTAL:
			cha.getResistance().addAllNaturalResistance(-10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case ELEMENTAL_PROTECTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int attr = pc.getElfAttr();
				if (attr == 1) {
					cha.getResistance().addEarth(-50);
				} else if (attr == 2) {
					cha.getResistance().addFire(-50);
				} else if (attr == 4) {
					cha.getResistance().addWater(-50);
				} else if (attr == 8) {
					cha.getResistance().addWind(-50);
				}
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case ELEMENTAL_FALL_DOWN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int attr = pc.getAddAttrKind();
				int i = 50;
				switch (attr) {
				case 1:
					pc.getResistance().addEarth(i);
					break;
				case 2:
					pc.getResistance().addFire(i);
					break;
				case 4:
					pc.getResistance().addWater(i);
					break;
				case 8:
					pc.getResistance().addWind(i);
					break;
				default:
					break;
				}
				pc.setAddAttrKind(0);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			} else if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				int attr = npc.getAddAttrKind();
				int i = 50;
				switch (attr) {
				case 1:
					npc.getResistance().addEarth(i);
					break;
				case 2:
					npc.getResistance().addFire(i);
					break;
				case 4:
					npc.getResistance().addWater(i);
					break;
				case 8:
					npc.getResistance().addWind(i);
					break;
				default:
					break;
				}
				npc.setAddAttrKind(0);
			}
			break;
		case IRON_SKIN:
			cha.getAC().addAc(10);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(10, 0));
			}
			break;
		case FIRE_SHIELD:
			cha.getAC().addAc(4);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconShield(4, 0));
			}
			break;
		case L1SkillId.SHADOW_FANG:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, L1SkillId.SHADOW_FANG);
				pc.addDmgup(-5);
			}
			break;
		case BLESS_WEAPON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, L1SkillId.BLESS_WEAPON);
				pc.addDmgup(-2);
				pc.addHitup(-2);
			}
			break;
		case ENCHANT_WEAPON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, L1SkillId.ENCHANT_WEAPON);
				pc.addDmgup(-2);
			}
			break;
		case BLESSED_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, L1SkillId.BLESSED_ARMOR);
				pc.getAC().addAc(3);
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			break;
		case PHYSICAL_ENCHANT_STR:
			cha.getAbility().addAddedStr((byte) -5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Strup(pc, 1, 0));
			}
			break;
		case PHYSICAL_ENCHANT_DEX:
			cha.getAbility().addAddedDex((byte) -5);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Dexup(pc, 1, 0));
			}
			break;
		case EARTH_WEAPON:
			cha.addHitup(-4);
			cha.addDmgup(-2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// pc.sendPackets(new S_SkillIconAura(147, 0));
			}
			break;
		case CYCLONE:{
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(CYCLONE);
				noti.set_duration(0);
				noti.set_off_icon_id(5446);
				noti.set_end_str_id(5450);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
		}
			break;
		case INFERNO:{
			if(cha instanceof L1PcInstance) {
				cha.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 755, false));
			}
			break;
		}
		case FOCUS_WAVE:
			cha.set_focus_wave_level(0);
		case HURRICANE:
		case SAND_STORM:
		case DANCING_BLADES:
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 0, 0));
				pc.setAttackSpeed();
			}
			break;
		// 크레이 혈흔
		case BUFF_CRAY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-5);
				pc.addDmgup(-1);
				pc.addBowHitup(-5);
				pc.addBowDmgup(-1);
				pc.addMaxHp(-100);
				pc.addMaxMp(-50);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case BUFF_Vala:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-5);
				pc.addDmgup(-1);
				pc.addBowHitup(-5);
				pc.addBowDmgup(-1);
				pc.addMaxHp(-100);
				pc.addMaxMp(-50);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case BUFF_GUNTER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedDex((byte) -5);
				pc.addBowHitup(-7);
				pc.addBowDmgup(-5);
				pc.addMaxHp(-100);
				pc.addMaxMp(-40);
				pc.addHpr(-10);
				pc.addMpr(-3);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		// UI DG표시
		case UNCANNY_DODGE: // 언케니닷지
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDg(-30);
			}
			break;
		case SHINING_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addEffectedER(-10);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(SHINING_ARMOR);
				noti.set_duration(0);
				noti.set_off_icon_id(9483);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case BURNING_WEAPON:
			cha.addDmgup(-6);
			cha.addHitup(-3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(162, 0));
			}
			break;
		case MIRROR_IMAGE: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDg(-30);
		}
			break;
		case AQUA_SHOT:
			cha.addBowHitup(-4);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// pc.sendPackets(new S_SkillIconAura(148, 0));
			}
			break;
		case HOLY_WEAPON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1SkillUse.off_icons(pc, L1SkillId.HOLY_WEAPON);
				pc.addDmgup(-1);
				pc.addHitup(-1);
			}
			break;
		case MOEBIUS:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(MOEBIUS);
				noti.set_duration(0);
				noti.set_off_icon_id(9443);
				noti.set_end_str_id(5551);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case STORM_EYE:
			cha.addBowHitup(-2);
			cha.addBowDmgup(-3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(155, 0));
			}
			break;
		case STORM_SHOT:
			cha.addBowDmgup(-5);
			cha.addBowHitup(3);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconAura(165, 0));
			}
			break;
		case BERSERKERS:
			cha.getAC().addAc(-10);
			cha.addDmgup(-2);
			cha.addHitup(-8);
			break;
		case SCALES_EARTH_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(3);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(SCALES_EARTH_DRAGON);
				noti.set_duration(0);
				noti.set_end_str_id(2420);
				noti.set_off_icon_id(3182);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case SCALES_WATER_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(SCALES_WATER_DRAGON);
				noti.set_duration(0);
				noti.set_end_str_id(2424);
				noti.set_off_icon_id(3184);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case SCALES_RINDVIOR_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDg(-7);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(SCALES_RINDVIOR_DRAGON);
				noti.set_duration(0);
				noti.set_end_str_id(5271);
				noti.set_off_icon_id(8887);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case SCALES_FIRE_DRAGON:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.ABILITY, -10);
				pc.addHitup(-5);
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
				
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(SCALES_FIRE_DRAGON);
				noti.set_duration(0);
				noti.set_end_str_id(2427);
				noti.set_off_icon_id(3078);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case IllUSION_OGRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-4);
				pc.addHitup(-4);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(IllUSION_OGRE);
				noti.set_duration(0);
				noti.set_off_icon_id(3117);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case IllUSION_LICH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-2);
				pc.sendPackets(new S_SPMR(pc));
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(IllUSION_LICH);
				noti.set_duration(0);
				noti.set_off_icon_id(3115);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case IllUSION_DIAMONDGOLEM:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(8);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(IllUSION_DIAMONDGOLEM);
				noti.set_duration(0);
				noti.set_off_icon_id(3113);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case REDUCTION_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(REDUCTION_ARMOR);
				noti.set_duration(0);
				noti.set_off_icon_id(1889);
				noti.set_end_str_id(2197);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				pc.setLastImmuneLevel(0);
			}
			break;
		case FREEZEENG_ARMOR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				
				pc.addEffectedER(-5);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()));
				
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(FREEZEENG_ARMOR);
				noti.set_duration(0);
				noti.set_off_icon_id(9490);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case IllUSION_AVATAR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-10);
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(IllUSION_AVATAR);
				noti.set_duration(0);
				noti.set_off_icon_id(3111);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case INSIGHT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.resetBaseMr();
			}
			break;
		case Tam_Fruit1:// tam
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(1);
				pc.sendPackets(new S_OwnCharStatus(pc));
				int tamcount = pc.tamcount();
				if (tamcount > 0) {
					long tamtime = pc.TamTime() / 1000;
					if (tamcount == 1) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8265, 4181));
						pc.setSkillEffect(Tam_Fruit1, (int) tamtime);
						pc.getAC().addAc(-1);
					} else if (tamcount == 2) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8266, 4182));
						pc.setSkillEffect(Tam_Fruit2, (int) tamtime);
						pc.getAC().addAc(-2);
					} else if (tamcount == 3) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8267, 4183));
						pc.setSkillEffect(Tam_Fruit3, (int) tamtime);
						pc.getAC().addAc(-3);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 4) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8268, 5046));
						pc.setSkillEffect(Tam_Fruit4, (int) tamtime);
						pc.getAC().addAc(-4);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 5) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8269, 5047));
						pc.setSkillEffect(Tam_Fruit5, (int) tamtime);
						pc.getAC().addAc(-5);
						pc.addDamageReductionByArmor(2);
					}
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			break;
		case Tam_Fruit2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(2);
				pc.sendPackets(new S_OwnCharStatus(pc));
				int tamcount = pc.tamcount();
				if (tamcount > 0) {
					long tamtime = pc.TamTime() / 1000;
					if (tamcount == 1) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8265, 4181));
						pc.setSkillEffect(Tam_Fruit1, (int) tamtime);
						pc.getAC().addAc(-1);
					} else if (tamcount == 2) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8266, 4182));
						pc.setSkillEffect(Tam_Fruit2, (int) tamtime);
						pc.getAC().addAc(-2);
					} else if (tamcount == 3) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8267, 4183));
						pc.setSkillEffect(Tam_Fruit3, (int) tamtime);
						pc.getAC().addAc(-3);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 4) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8268, 5046));
						pc.setSkillEffect(Tam_Fruit4, (int) tamtime);
						pc.getAC().addAc(-4);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 5) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8269, 5047));
						pc.setSkillEffect(Tam_Fruit5, (int) tamtime);
						pc.getAC().addAc(-5);
						pc.addDamageReductionByArmor(2);
					}
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			break;
		case Tam_Fruit3:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(3);
				pc.sendPackets(new S_OwnCharStatus(pc));
				int tamcount = pc.tamcount();
				if (tamcount > 0) {
					long tamtime = pc.TamTime() / 1000;
					if (tamcount == 1) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8265, 4181));
						pc.setSkillEffect(Tam_Fruit1, (int) tamtime);
						pc.getAC().addAc(-1);
					} else if (tamcount == 2) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8266, 4182));
						pc.setSkillEffect(Tam_Fruit2, (int) tamtime);
						pc.getAC().addAc(-2);
					} else if (tamcount == 3) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8267, 4183));
						pc.setSkillEffect(Tam_Fruit3, (int) tamtime);
						pc.getAC().addAc(-3);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 4) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8268, 5046));
						pc.setSkillEffect(Tam_Fruit4, (int) tamtime);
						pc.getAC().addAc(-4);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 5) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8269, 5047));
						pc.setSkillEffect(Tam_Fruit5, (int) tamtime);
						pc.getAC().addAc(-5);
						pc.addDamageReductionByArmor(2);
					}
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			break;
		case Tam_Fruit4:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(4);
				pc.sendPackets(new S_OwnCharStatus(pc));
				int tamcount = pc.tamcount();
				if (tamcount > 0) {
					long tamtime = pc.TamTime() / 1000;
					if (tamcount == 1) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8265, 4181));
						pc.setSkillEffect(Tam_Fruit1, (int) tamtime);
						pc.getAC().addAc(-1);
					} else if (tamcount == 2) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8266, 4182));
						pc.setSkillEffect(Tam_Fruit2, (int) tamtime);
						pc.getAC().addAc(-2);
					} else if (tamcount == 3) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8267, 4183));
						pc.setSkillEffect(Tam_Fruit3, (int) tamtime);
						pc.getAC().addAc(-3);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 4) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8268, 5046));
						pc.setSkillEffect(Tam_Fruit4, (int) tamtime);
						pc.getAC().addAc(-4);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 5) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8269, 5047));
						pc.setSkillEffect(Tam_Fruit5, (int) tamtime);
						pc.getAC().addAc(-5);
						pc.addDamageReductionByArmor(2);
					}
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			break;
		case Tam_Fruit5:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(5);
				pc.sendPackets(new S_OwnCharStatus(pc));
				int tamcount = pc.tamcount();
				if (tamcount > 0) {
					long tamtime = pc.TamTime() / 1000;
					if (tamcount == 1) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8265, 4181));
						pc.setSkillEffect(Tam_Fruit1, (int) tamtime);
						pc.getAC().addAc(-1);
					} else if (tamcount == 2) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8266, 4182));
						pc.setSkillEffect(Tam_Fruit2, (int) tamtime);
						pc.getAC().addAc(-2);
					} else if (tamcount == 3) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8267, 4183));
						pc.setSkillEffect(Tam_Fruit3, (int) tamtime);
						pc.getAC().addAc(-3);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 4) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8268, 5046));
						pc.setSkillEffect(Tam_Fruit4, (int) tamtime);
						pc.getAC().addAc(-4);
						pc.addDamageReductionByArmor(2);
					} else if (tamcount == 5) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, (int) tamtime, 8269, 5047));
						pc.setSkillEffect(Tam_Fruit5, (int) tamtime);
						pc.getAC().addAc(-5);
						pc.addDamageReductionByArmor(2);
					}
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
			}
			break;
		case ENCHANT_ACURUCY:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;

				pc.addHitup(-5);

				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(ENCHANT_ACURUCY);
				noti.set_duration(0);
				noti.set_off_icon_id(9487);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case SHAPE_CHANGE:
			L1PolyMorph.undoPoly(cha);
			break;
		case 천하장사버프: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-5);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, pc, 187, 0));
			pc.setDessertId(0);
			break;
		}
		case PRIDE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(PRIDE);
				noti.set_duration(0);
				noti.set_end_str_id(5265);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);

				pc.addMaxHp(-pc.getMagicBuffHp());
				pc.setMagicBuffHp(0);
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			}
			break;
		case GIGANTIC:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(GIGANTIC);
				noti.set_duration(0);
				noti.set_end_str_id(3919);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);

				pc.addMaxHp(-pc.getMagicBuffHp());
				pc.setMagicBuffHp(0);
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			}
			break;
		case POWERRIP:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_RIP, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.set발묶임상태(false);
			}
			break;
		case DESPERADO:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PERADO, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.set발묶임상태(false);
			}
			cha.Desperadolevel = 0;
			break;
		case ADVANCE_SPIRIT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-pc.getAdvenHp());
				pc.addMaxMp(-pc.getAdvenMp());
				pc.setAdvenHp(0);
				pc.setAdvenMp(0);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(ADVANCE_SPIRIT - 1);
				noti.set_duration(0);
				noti.set_off_icon_id(2180);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;
		case HASTE:
		case GREATER_HASTE:
			cha.setMoveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			break;
		case EAGGLE_EYE:
			cha.add_missile_critical_rate(-2);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.END);
				noti.set_spell_id(EAGGLE_EYE - 1);
				noti.set_duration(0);
				noti.set_off_icon_id(8490);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			}
			break;

		case HOLY_WALK:
		case MOVING_ACCELERATION:
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
			}
			break;
		case BLOOD_LUST:
			cha.setBraveSpeed(0);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
			}
			break;
		case DARK_BLIND:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false));
			}
			cha.setSleeped(false);
			break;
		case CURSE_BLIND:
		case DARKNESS:
		case LINDBIOR_SPIRIT_EFFECT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_CurseBlind(0));
			}
			break;
		case CURSE_PARALYZE:
		case DESERT_SKILL1:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				//pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false));
			}
			break;
		case WEAKNESS:
		case MOB_WEAKNESS_1:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(5);
				pc.addHitup(1);
			}
			break;
		case DISEASE:
		case MOB_DISEASE_1:
		case MOB_DISEASE_30:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(6);
				pc.getAC().addAc(-12);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case GUARD_BREAK:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case HORROR_OF_DEATH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) 3);
				pc.getAbility().addAddedInt((byte) 3);
			}
			break;
		case PANIC:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedCon((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				pc.getAbility().addAddedWis((byte) 1);
				pc.getAbility().addAddedCha((byte) 1);
				pc.resetBaseMr();
			}
			break;
		case ICE_LANCE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.broadcastPacket(new S_Poison(npc.getId(), 0));
				npc.setParalyzed(false);
			}
			break;
		case L1SkillId.MOB_BERSERKERS:{
			if(cha instanceof L1NpcInstance){
				cha.setMoveSpeed(0);
				cha.setBraveSpeed(0);
			}
			break;
		}
		case EARTH_BIND:
		case MOB_BASILL:
		case MOB_COCA:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Poison(pc.getId(), 0));
				pc.broadcastPacket(new S_Poison(pc.getId(), 0));
				if(skillId == EARTH_BIND)
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.broadcastPacket(new S_Poison(npc.getId(), 0));
				//npc.setParalyzed(false);
			}
			break;
		case SHOCK_STUN:
		case MOB_SHOCKSTUN_30:
		case MOB_RANGESTUN_18:
		case MOB_RANGESTUN_19:
		case MOB_RANGESTUN_20:
		case Mob_RANGESTUN_30: // 쇼크 스탠
		case ANTA_MESSAGE_6:
		case ANTA_MESSAGE_7:
		case ANTA_MESSAGE_8:
		case ANTA_SHOCKSTUN:
		case OMAN_STUN:// 오만 스턴
		case Maeno_STUN:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case L1SkillId.EMPIRE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case BONE_BREAK:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setParalyzed(false);
			}
			break;
		case PHANTASM:
		case FOG_OF_SLEEPING:
			cha.setSleeped(false);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, false));
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
			break;
		case ABSOLUTE_BARRIER:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
				noti.set_noti_type(eNotiType.RESTAT);
				noti.set_spell_id(L1SkillId.ABSOLUTE_BARRIER);
				noti.set_duration(1);
				noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
				noti.set_on_icon_id(717);
				noti.set_off_icon_id(717);
				noti.set_icon_priority(10);
				noti.set_tooltip_str_id(700);
				noti.set_end_str_id(2179);
				noti.set_is_good(true);
				pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
				pc.startHpMpRegeneration();
				pc.startMpRegenerationByDoll();
			}
			break;
		/*
		 * case WIND_SHACKLE: case MOB_WINDSHACKLE_1: if (cha instanceof
		 * L1PcInstance) { L1PcInstance pc = (L1PcInstance) cha; //
		 * pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), 0)); } break;
		 */
		case SLOW:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			cha.setMoveSpeed(0);
			break;
		case STATUS_FREEZE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof MJCompanionInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				// npc.setParalyzed(false);
			}
			break;
		case STATUS_IGNITION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addFire(-30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case STATUS_QUAKE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addEarth(-30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case STATUS_SHOCK:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addWind(-30);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case STATUS_BRAVE:
		case STATUS_ELFBRAVE:
		case STATUS_FRUIT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 0, 0));
			}
			cha.setBraveSpeed(0);
			break;
		case STATUS_HASTE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 0, 0));
			}
			cha.setMoveSpeed(0);
			break;
		case STATUS_BLUE_POTION:
		case STATUS_BLUE_POTION2:
			break;
		case STATUS_UNDERWATER_BREATH:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0));
			}
			break;
		case STATUS_WISDOM_POTION:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// cha.addSp(-2);
				pc.getAbility().addSp(-2);
				pc.addMpr(-2);
				pc.sendPackets(new S_SkillIconWisdomPotion(0));
			}
			break;
		case STATUS_CHAT_PROHIBITED:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_ServerMessage(288));
			}
			break;
		case STATUS_CASHSCROLL:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case STATUS_CASHSCROLL2:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-40);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case STATUS_CASHSCROLL3:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-3);
				pc.addHitup(-3);
				pc.getAbility().addSp(-3);
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case STATUS_CASHSCROLL4:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-3);
				pc.addBaseMagicHitUp(-5);
				pc.getResistance().addcalcPcDefense(-3);
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case STATUS_CASHSCROLL5:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowDmgup(-3);
				pc.addBowHitup(-5);
				pc.getResistance().addcalcPcDefense(-3);
			}
			break;
		case STATUS_CASHSCROLL6:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgRate(-3);
				pc.addHitup(-5);
				pc.getResistance().addcalcPcDefense(-3);
			}
			break;
		case STATUS_POISON:
			cha.curePoison();
			break;

		case COOKING_1_0_N:
		case COOKING_1_0_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 0, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_1_N:
		case COOKING_1_1_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_PacketBox(53, 1, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_2_N:
		case COOKING_1_2_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 2, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_3_N:
		case COOKING_1_3_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(1);
				pc.sendPackets(new S_PacketBox(53, 3, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_4_N:
		case COOKING_1_4_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-20);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(53, 4, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_5_N:
		case COOKING_1_5_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 5, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_6_N:
		case COOKING_1_6_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addMr(-5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 6, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_7_N:
		case COOKING_1_7_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 7, 0));
				pc.setDessertId(0);
			}
			break;
		case COOKING_1_8_N:
		case COOKING_1_8_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 16, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_9_N:
		case COOKING_1_9_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxMp(-30);
				pc.addMaxHp(-30);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_PacketBox(53, 17, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_10_N:
		case COOKING_1_10_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(2);
				pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_PacketBox(53, 18, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_11_N:
		case COOKING_1_11_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 19, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_12_N:
		case COOKING_1_12_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 20, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_13_N:
		case COOKING_1_13_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addMr(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 21, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_14_N:
		case COOKING_1_14_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// pc.addSp(-1);
				pc.getAbility().addSp(-1);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 22, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_15_N:
		case COOKING_1_15_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 7, 0));
				pc.setDessertId(0);
			}
			break;
		case COOKING_1_16_N:
		case COOKING_1_16_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowHitRate(-2);
				pc.addBowDmgup(-1);
				pc.sendPackets(new S_PacketBox(53, 45, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_17_N:
		case COOKING_1_17_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addMaxMp(-50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				pc.sendPackets(new S_PacketBox(53, 46, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_18_N:
		case COOKING_1_18_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-2);
				pc.addDmgup(-1);
				pc.sendPackets(new S_PacketBox(53, 47, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_19_N:
		case COOKING_1_19_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(3);
				pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_PacketBox(53, 48, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_20_N:
		case COOKING_1_20_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getResistance().addAllNaturalResistance(-10);
				pc.getResistance().addMr(-15);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				pc.sendPackets(new S_PacketBox(53, 49, 0));
				pc.setCookingId(0);
			}
			break;
		case 메티스축복주문서:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-2);
				pc.addDmgup(-2);
				pc.addBowHitup(-2);
				pc.addBowDmgup(-2);
				pc.getAbility().addSp(-2);
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.sendPackets(new S_SPMR(pc));
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), false);
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), false);
			}
			break;
		case COOKING_1_21_N:
		case COOKING_1_21_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// pc.addSp(-2);
				pc.getAbility().addSp(-2);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 50, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_22_N:
		case COOKING_1_22_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-30);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_PacketBox(53, 51, 0));
				pc.setCookingId(0);
			}
			break;
		case COOKING_1_23_N:
		case COOKING_1_23_S:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 7, 0));
				pc.setDessertId(0);
			}
			break;
		case COMA_A:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// pc.sendPackets(new S_Coma(40, 0));
				pc.getAbility().addAddedCon(-1);
				pc.getAbility().addAddedDex(-5);
				pc.getAbility().addAddedStr(-5);
				pc.addHitRate(-3);
				pc.getAC().addAc(3);
			}
			break;
		case COMA_B:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-1);
				pc.getAbility().addAddedCon(-3);
				pc.getAbility().addAddedDex(-5);
				pc.getAbility().addAddedStr(-5);
				pc.addHitRate(-5);
				pc.getAC().addAc(8);
				pc.sendPackets(new S_SPMR(pc));
			}
			break;
		case FEATHER_BUFF_A:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHpr(-3);
				pc.addMpr(-3);
				pc.addDmgup(-2);
				pc.addHitup(-2);
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				// pc.addSp(-2);
				pc.getAbility().addSp(-2);
				pc.sendPackets(new S_SPMR(pc));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case FEATHER_BUFF_B:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-2);
				// pc.addSp(-1);
				pc.getAbility().addSp(-1);
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				pc.sendPackets(new S_SPMR(pc));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case FEATHER_BUFF_C:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addMaxHp(-50);
				pc.addMaxMp(-30);
				pc.getAC().addAc(2);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				if (pc.isInParty()) {
					// TODO 파티 프로토
					pc.getParty().refreshPartyMemberStatus(pc);
				}
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			break;
		case FEATHER_BUFF_D:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(1);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case ANTA_MAAN:// 지룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case FAFU_MAAN:// 수룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.SPIRIT, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case LIND_MAAN:// 풍룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.add_magic_critical_rate(-2);
				pc.addSpecialResistance(eKind.FEAR, -5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case VALA_MAAN:// 화룡의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-2);
				pc.addBowDmgup(-2);
				pc.addSpecialResistance(eKind.ABILITY, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case BIRTH_MAAN:// 탄생의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.SPIRIT, -5);
				pc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case SHAPE_MAAN:// 형상의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;				
				pc.add_magic_critical_rate(-1);
				pc.addSpecialResistance(eKind.SPIRIT, -5);
				pc.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				pc.addSpecialResistance(eKind.FEAR, -5);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case LIFE_MAAN:// 생명의 마안
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addSpecialResistance(eKind.ALL, -5);
				pc.addDmgup(-2);
				pc.addBowDmgup(-2);
				pc.add_magic_critical_rate(-1);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			break;
		case SIDE_OF_ME_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-5);
			pc.addDmgup(-5);
			pc.addBowDmgup(-5);
		}
			break;
		case RE_START_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-3);
			pc.addDmgup(-3);
			pc.addBowDmgup(-3);
		}
			break;
		case NEW_START_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-2);
			pc.addDmgup(-2);
			pc.addBowDmgup(-2);
		}
			break;
		case LIFE_BLESSING: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addDamageReductionByArmor(-1);
			pc.addDmgup(-1);
			pc.addBowDmgup(-1);
		}
			break;
		case ANTA_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAC().addAc(2);
				pc.getResistance().addWater(-50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, 0));
			}
			break;
		case FAFU_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHpr(-3);
				pc.addMpr(-1);
				pc.getResistance().addWind(50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, 0));
			}
			break;
		case RIND_BUFF:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-3);
				pc.addBowHitup(-3);
				pc.getResistance().addFire(-50);
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, 0));
			}
			break;
		case STATUS_DRAGON_PEARL: // 드래곤의 진주
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Liquor(pc.getId(), 0));
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_PEARL, 0, 0));
				pc.sendPackets(new S_ServerMessage(185));
				pc.setPearl(0);
			}
			break;
		case BOUNCE_ATTACK: {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addHitup(-6);
		}
			break;
		case COOK_STR: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-2);
				pc.addHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 157, 0));
				pc.setCookingId(0);
			}
		}
			break;
		case COOK_STR_1: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addDmgup(-2);
				pc.addHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 215, 0));
				pc.setCookingId(0);
			}
		}
	break;
		case COOK_DEX_1: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowDmgup(-2);
				pc.addBowHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 216, 0));
				pc.setCookingId(0);
		
			}
		}
			break;
		case COOK_INT_1: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.getAbility().addSp(-2);
				pc.addHpr(-2);
				pc.addMpr(-3);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.addSpecialPierce(eKind.ALL, -3);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 217, 0));
				pc.setCookingId(0);
			}
		}
			break;
		case COOK_GROW_1: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 218, 0));
				pc.setDessertId(0);
				pc.addSpecialResistance(eKind.ALL, -3);
				pc.addDamageReductionByArmor(-2);
			}
		}
			break;
		case COOK_DEX: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addBowDmgup(-2);
				pc.addBowHitup(-1);
				pc.addHpr(-2);
				pc.addMpr(-2);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 158, 0));
				pc.setCookingId(0);
			}
		}
			break;
		case 메티스정성요리:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addHitup(-2);
				pc.addDmgup(-2);
				pc.addBowHitup(-2);
				pc.addBowDmgup(-2);
				pc.getAbility().addSp(-2);
				pc.addHpr(-3);
				pc.addMpr(-4);
				pc.getResistance().addMr(-15);
				pc.sendPackets(new S_SPMR(pc));
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			break;
		case COOK_INT: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				// pc.addSp(-2);
				pc.getAbility().addSp(-2);
				pc.addHpr(-2);
				pc.addMpr(-3);
				pc.getResistance().addMr(-10);
				pc.getResistance().addAllNaturalResistance(-10);
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_PacketBox(53, 159, 0));
				pc.setCookingId(0);
			}
		}
			break;
		case COUNTER_MIRROR:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addWeightReduction(-300);
			}
			break;
		case DECREASE_WEIGHT:
		case REDUCE_WEIGHT:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.addWeightReduction(-180);
			}
			break;
		case COOK_GROW: {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PacketBox(53, 160, 0));
				pc.setDessertId(0);
			}
		}
			break;
		case RINDVIOR_WIND_SHACKLE:
		case RINDVIOR_WIND_SHACKLE_1:
		case DRAKE_WIND_SHACKLE:
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), 0));
			}
			break;
		default:
			break;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			sendStopMessage(pc, skillId);
			pc.sendPackets(new S_OwnCharStatus(pc));
		}
	}

	private static void sendStopMessage(L1PcInstance charaPc, int skillid) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillid);
		if (l1skills == null || charaPc == null) {
			return;
		}

		int msgID = l1skills.getSysmsgIdStop();
		if (msgID > 0) {
			charaPc.sendPackets(new S_ServerMessage(msgID));
		}
	}
}
