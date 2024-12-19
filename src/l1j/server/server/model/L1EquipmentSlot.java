package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;

import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.MJTemplate.ItemPresentator.ItemPresentatorFactory;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.ItemInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_AddSkill;
import l1j.server.server.serverpackets.S_DelSkill;
import l1j.server.server.serverpackets.S_DeleteInventoryItem;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;

public class L1EquipmentSlot {

	private L1PcInstance _owner;

	private ArrayList<L1ArmorSet> _currentArmorSet;

	private ArrayList<L1ItemInstance> _weapons;

	private ArrayList<L1ItemInstance> _armors;

	private int weapons_idx = 0;
	public int worldjoin_weapon_idx = 0;

	public L1EquipmentSlot(L1PcInstance owner) {
		_owner = owner;
		_weapons = new ArrayList<L1ItemInstance>();
		_armors = new ArrayList<L1ItemInstance>();
		_currentArmorSet = new ArrayList<L1ArmorSet>();
	}

	private void setWeapon(L1ItemInstance weapon) {// 무기류
		int itemId = weapon.getItem().getItemId();
		int enchant = weapon.getEnchantLevel();
		if (itemId == 1134 || itemId == 101134) { // 명상의 지팡이
			if (enchant > 0) {
				_owner.addMpr(enchant);
			}
		}
		/** 칠흑의 수정구 **/
		if (itemId == 118) {
			switch (enchant) {
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addCha(1);
				break;
			default:
				break;
			}
		}
		
		
		/** 아인하사드의 섬광 **/
		if (itemId == 2944) {
			switch (enchant) {
			case 0:
				_owner.add_melee_critical_rate(7);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(20); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 12); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, 12); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 12); // 공포적중
				break;
			case 1:
				_owner.add_melee_critical_rate(9);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(21); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 13);
				_owner.addSpecialPierce(eKind.SPIRIT, 13); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 13); // 공포적중
				break;
			case 2:
				_owner.add_melee_critical_rate(11);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(22); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 14);
				_owner.addSpecialPierce(eKind.SPIRIT, 14); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 14); // 공포적중
				break;
			case 3:
				_owner.add_melee_critical_rate(13);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(23); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 15);
				_owner.addSpecialPierce(eKind.SPIRIT, 15); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 15); // 공포적중
				break;
			case 4:
				_owner.add_melee_critical_rate(15);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(24); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 16);
				_owner.addSpecialPierce(eKind.SPIRIT, 16); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 16); // 공포적중
				break;
			case 5:
				_owner.add_melee_critical_rate(17);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(25); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 17);
				_owner.addSpecialPierce(eKind.SPIRIT, 17); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 17); // 공포적중
				break;
			case 6:
				_owner.add_melee_critical_rate(19);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(26); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 18);
				_owner.addSpecialPierce(eKind.SPIRIT, 18); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 18); // 공포적중
				break;
			case 7:
				_owner.add_melee_critical_rate(21);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(27); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 19);
				_owner.addSpecialPierce(eKind.SPIRIT, 19); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 19); // 공포적중
				break;
			case 8:
				_owner.add_melee_critical_rate(23);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(28); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 20);
				_owner.addSpecialPierce(eKind.SPIRIT, 20); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 20); // 공포적중
				break;
			case 9:
				_owner.add_melee_critical_rate(25);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(29); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 21);
				_owner.addSpecialPierce(eKind.SPIRIT, 21); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 21); // 공포적중
				break;
			case 10:
				_owner.add_melee_critical_rate(27);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(30); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 22);
				_owner.addSpecialPierce(eKind.SPIRIT, 22); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 22); // 공포적중
				break;

			}
		}
		
		/** 그랑카인의 심판 **/
		if (itemId == 2945) {
			switch (enchant) {
			case 0:
				_owner.add_melee_critical_rate(7);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(20); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 15); // 기술적중
				break;
			case 1:
				_owner.add_melee_critical_rate(9);
			//	_owner.getResistance().addcalcPcDefense(21); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 16); // 기술적중
				break;
			case 2:
				_owner.add_melee_critical_rate(11);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(22); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 17); // 기술적중
				break;
			case 3:
				_owner.add_melee_critical_rate(13);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(23); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 18); // 기술적중
				break;
			case 4:
				_owner.add_melee_critical_rate(15);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(24); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 19); // 기술적중
				break;
			case 5:
				_owner.add_melee_critical_rate(17);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(25); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 20); // 기술적중
				break;
			case 6:
				_owner.add_melee_critical_rate(19);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(26); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 21); // 기술적중
				break;
			case 7:
				_owner.add_melee_critical_rate(21);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(27); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 22); // 기술적중
				break;
			case 8:
				_owner.add_melee_critical_rate(23);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(28); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 23); // 기술적중
				break;
			case 9:
				_owner.add_melee_critical_rate(25);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(29); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 24); // 기술적중
				break;
			case 10:
				_owner.add_melee_critical_rate(27);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(30); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, 25); // 기술적중
				break;
		
				
			}
		}
		/** 축복받은 제로스의 지팡이 **/
		if (itemId == 620) {
			switch (enchant) {
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
				_owner.addBaseMagicCritical(5);// 마법 치명타
				break;
			}
		}
		/** 살기의 키링크 **/
		if (itemId == 7000223 || itemId == 505015) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.addBaseMagicCritical(1);// 마법 치명타
				break;
			case 7:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 1);
				_owner.addBaseMagicCritical(2);// 마법 치명타
				break;
			case 8:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 2);
				_owner.addBaseMagicCritical(3);// 마법 치명타
				break;
			case 9:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 3);
				_owner.addBaseMagicCritical(4);// 마법 치명타
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 4);
				_owner.addBaseMagicCritical(5);// 마법 치명타
				break;
			}
		}
		/** 바포메트의 지팡이 SP **/
		if (itemId == 124) {
			switch (enchant) {
			case 7:
				_owner.getAbility().addSp(1);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 8:
				_owner.getAbility().addSp(2);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(3);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			}
		}
		if (itemId == 541) {
			switch (enchant) {
			case 0:
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				break;
			}
		}
		if (itemId == 542) {
			switch (enchant) {
			case 0:
				_owner.addDamageReductionByArmor(2);
				break;
			}
		}
		if (itemId == 543) {
			switch (enchant) {
			case 0:
				_owner.addHitup(2);
				break;
			}
		}
		if (itemId == 544) {
			switch (enchant) {
			case 0:
				_owner.addHitup(10);
				break;
			}
		}
		if (itemId == 545) {
			switch (enchant) {
			case 0:
				_owner.addHitup(17);
				break;
			}
		}
		if (itemId == 547) {
			switch (enchant) {
			case 0:
				_owner.addHitup(11);
				break;
			}
		}
		/** 드래곤슬레이어 **/
		if (itemId == 66) {
			_owner.addDmgup(enchant * 2);
		}
		/** 나이트발드의 양손검 기술적중 **/
		if (itemId == 59 || itemId == 617) {
			_owner.addSpecialPierce(eKind.ABILITY, 5);
		}
		/** 태풍의도끼 공포적중+1 **/
		if (itemId == 203006 || itemId == 616) {
			switch (enchant) {
			case 8:
				_owner.addSpecialPierce(eKind.FEAR, 1);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.FEAR, 2);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.FEAR, 3);
				break;
			}
		}
		/** 안타라스의 도끼 **/
		if (itemId == 7000016) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.FEAR, 2);
				break;
			case 8:
				_owner.addSpecialPierce(eKind.FEAR, 3);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.FEAR, 4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.FEAR, 5);
				break;
			}
		}
		
		/** 안타라스의 지팡이 **/
		if (itemId == 7000017) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addBaseMagicHitUp(1);// 마법 적중 적용
				break;
			case 8:
				_owner.addBaseMagicHitUp(2);
				break;
			case 9:
				_owner.addBaseMagicHitUp(3);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBaseMagicHitUp(4);
				break;
			}
		}
		/** 파푸리온의 장궁 **/
		if (itemId == 7000018) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.SPIRIT, 1); // 정령적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.SPIRIT, 2); // 정령적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.SPIRIT, 3); // 정령적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.SPIRIT, 4); // 정령적중
				break;
			}
		}
		
		/** 파푸리온의 이도류 **/
		if (itemId == 7000019) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.SPIRIT, 2); // 정령적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.SPIRIT, 3); // 정령적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.SPIRIT, 4); // 정령적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.SPIRIT, 5); // 정령적중
				break;
			}
		}
		
		/** 린드비오르의 체인소드 **/
		if (itemId == 7000020) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 5); // 용언적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 6); // 용언적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 7); // 용언적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 8); // 용언적중
				break;
			}
		}
		
		/** 린드비오르의 키링크 **/
		if (itemId == 7000021) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.getAbility().addSp(1);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 8:
				_owner.getAbility().addSp(2);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 9:
				_owner.getAbility().addSp(3);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(4);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			}
		}
		
		/** 발라카스의 장검 **/
		if (itemId == 7000022) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.ABILITY, 2); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, 2); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 2); // 공포적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.ABILITY, 3); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, 3); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 3); // 공포적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.ABILITY, 4); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, 4); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 4); // 공포적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.ABILITY, 5); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, 5); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, 5); // 공포적중
				break;
			}
		}
		
		/** 발라카스의 양손검 **/
		if (itemId == 7000023) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.ABILITY, 5); // 기술적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.ABILITY, 6); // 기술적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.ABILITY, 7); // 기술적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.ABILITY, 8); // 기술적중
				break;
			}
		}
		
		/** 섬멸자의 체인소드 적중 **/
		if (itemId == 203017 || itemId == 618) {
			switch (enchant) {
			case 7:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 1);
				break;
			case 8:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 2);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 3);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, 4);
				break;
			}
		}
		/** 썸타는 도끼 공포적중+5 **/
		if (itemId == 7000221) 
			_owner.addSpecialPierce(eKind.FEAR, 5);

		/** 썸타는 무기류 PVP 효과 부여 **/
		if (itemId >= 7000214 || itemId <= 7000221) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.getResistance().addPVPweaponTotalDamage(3);
				break;
			case 8:
				_owner.getResistance().addPVPweaponTotalDamage(5);
				break;
			case 9:
				_owner.getResistance().addPVPweaponTotalDamage(7);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getResistance().addPVPweaponTotalDamage(10);
				break;
			}
		}

		weapon.startEquipmentTimer(_owner);
		_weapons.add(weapon);
		for (int i = 0; i < 2; ++i) {
			try {
				if (i == 0)
					_owner.setWeapon(_weapons.get(i));
				else
					_owner.setSecondWeapon(_weapons.get(i));
			} catch (Exception e) {
				if (i == 0)
					_owner.setWeapon(null);
				else
					_owner.setSecondWeapon(null);
			}
		}
		// System.out.println("왼쪽("+_owner.getWeapon()+")
		// 오른쪽("+_owner.getSecondWeapon()+")");

		if (_weapons.size() == 2) {
			_owner.setCurrentWeapon(88);
			_owner.sendPackets(new S_SkillSound(_owner.getId(), 12534));
		} else {
			_owner.setCurrentWeapon(weapon.getItem().getType1());
		}

		int type = weapon.getItem().getType();
		if ((type == 7 || type == 16 || type == 17) && (weapon.get_bless_level() != 0)) {
			_owner.getAbility().addSp(weapon.get_bless_level());
			_owner.sendPackets(new S_SPMR(_owner));
		}

		if (itemId == 203003) { // 데스나이트의 불검:진
			L1PolyMorph.doPoly(_owner, 12232, 0, L1PolyMorph.MORPH_BY_ITEMMAGIC, false);
		}

		if (weapon.hasSkillEffectTimer(L1SkillId.BLESS_WEAPON)) {
			int time = weapon.getSkillEffectTimeSec(L1SkillId.BLESS_WEAPON);
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, time, weapon.getEnchantMagic(), getWeapons().size() - 1));
		} else if (weapon.hasSkillEffectTimer(L1SkillId.ENCHANT_WEAPON)) {
			int time = weapon.getSkillEffectTimeSec(L1SkillId.ENCHANT_WEAPON);
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, time, weapon.getEnchantMagic(), getWeapons().size() - 1));
		} else if (weapon.hasSkillEffectTimer(L1SkillId.SHADOW_FANG)) {
			int time = weapon.getSkillEffectTimeSec(L1SkillId.SHADOW_FANG);
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, time, weapon.getEnchantMagic(), getWeapons().size() - 1));
		}
	}

	/**
	 * 착용중인 무기에 젤 마지막 무기를 리턴.
	 * 
	 * @return
	 */
	public L1ItemInstance getWeapon() {
		return _weapons.size() > 0 ? _weapons.get(_weapons.size() - 1) : null;
	}

	/**
	 * 착용중인 무기를 번갈아 가면서 리턴.
	 * 
	 * @return
	 */
	public L1ItemInstance getWeaponSwap() {
		if (_weapons.size() > 0) {
			if (_weapons.size() > 1)
				return _weapons.get(weapons_idx++ % 2);
			else
				return _weapons.get(0);
		}
		return null;
	}

	public boolean isWeapon(L1ItemInstance weapon) {
		return _weapons.contains(weapon);
	}

	public int getWeaponCount() {
		return _weapons.size();
	}

	public List<L1ItemInstance> getWeapons() {
		return new ArrayList<L1ItemInstance>(_weapons);
	}

	private static boolean isRoomteece(int itemId) {
		return itemId == 222340 || itemId == 222341 || itemId == 22229 || itemId == 22230 || itemId == 22231
				|| itemId == 222337 || itemId == 222338 || itemId == 222339;
	}

	private void setArmor(L1ItemInstance armor) {	
		L1Item item = armor.getItem();
		int itemlvl = armor.getEnchantLevel();
		int itemtype = armor.getItem().getType();
		int itemId = armor.getItem().getItemId();
		int itemgrade = armor.getItem().getGrade();
		int constat = armor.getItem().get_addcon();
		int wisstat = armor.getItem().get_addwis();
		
		if (constat > 0) {
			_owner.addMaxHp((_owner.getAbility().getCon() / 2) * constat);
		}
		
		if (wisstat > 0) {
			_owner.addMaxMp((_owner.getAbility().getWis() / 2) * wisstat);
		}

		if(itemId == 900111){
			_owner.setSkillEffect(L1SkillId.TELEPORT_RULER, -1);
		}
		
		if(itemId == 22228){
			if(itemlvl >= 8){
				_owner.addMagicDodgeProbability(3);
			}else if(itemlvl >= 7){
				_owner.addMagicDodgeProbability(1);					
			}
		}else if(itemId == 222334){
			if(itemlvl >= 8){
				_owner.addMagicDodgeProbability(5);
			}else if(itemlvl >= 7){
				_owner.addMagicDodgeProbability(3);
			}else if(itemlvl >= 6){
				_owner.addMagicDodgeProbability(1);
			}
		}
		
		 if (itemtype >= 8 && itemtype <= 12 && !isRoomteece(itemId)) {
			if ((itemtype == 8 || itemtype == 12) && itemlvl >= 5) {
				int enchantAc = itemlvl - 4;
				if (enchantAc >= 5) {
					enchantAc = 5;
				}
				
				_owner.getAC().addAc(item.get_ac() - armor.getAcByMagic() - enchantAc + armor.get_durability());
				_owner.sendPackets(new S_OwnCharAttrDef(_owner));
			} else {
				_owner.getAC().addAc(item.get_ac() - armor.getAcByMagic() + armor.get_durability());
				_owner.sendPackets(new S_OwnCharAttrDef(_owner));
			}
		} else if(itemtype == 30){	// 휘장
			
		}else if(itemId == 900118 || itemId == 900117 || itemId == 900119 || itemId == 900120){
			int enchant = armor.getEnchantLevel();
			int enchant_ac = enchant >= 1 ? enchant : 0;
			_owner.getAC().addAc(item.get_ac() - enchant_ac - armor.getAcByMagic() + armor.get_durability());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		} else if(!isRoomteece(itemId)){
			_owner.getAC().addAc(item.get_ac() - armor.getEnchantLevel() - armor.getAcByMagic() + armor.get_durability());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		} else{
			_owner.getAC().addAc(item.get_ac());
		}
		/** 리치 로브 인챈당 sp증가 **/
		if (itemId == 20107) {
			if (itemlvl >= 3) {
				_owner.getAbility().addSp(itemlvl - 2);
			}
		}

		if (armor.getItem().get_regist_calcPcDefense() != 0) {
			_owner.set_pvp_defense(_owner.get_pvp_defense() + armor.getItem().get_regist_calcPcDefense());
		}

		_owner.addDamageReductionByArmor(item.get_damage_reduction());
		_owner.addWeightReduction(item.getWeightReduction());
		_owner.addBowHitRate(item.getBowHitRate());
		_owner.addBowDmgRate(item.getBowDmgRate());	
		_owner.addDmgRate(item.getDmgRate());	
		_owner.addHitRate(item.getHitRate());
		_owner.addBaseMagicHitUp(item.getMagicHitup());
		
		
		
		_owner.getResistance().addEarth(item.get_defense_earth());
		_owner.getResistance().addWind(item.get_defense_wind());
		_owner.getResistance().addWater(item.get_defense_water());
		_owner.getResistance().addFire(item.get_defense_fire());		
		item.equipmentItem(_owner, true);
		
		
		_owner.getResistance().addcalcPcDefense(item.get_regist_calcPcDefense());
		_owner.getResistance().addPVPweaponTotalDamage(item.get_regist_PVPweaponTotalDamage());
		_owner.addPVPMagicDamageReduction(item.get_PVPMagicDamageReduction());
		_armors.add(armor);

		if (armor.hasSkillEffectTimer(L1SkillId.BLESSED_ARMOR)) {
			int time = armor.getSkillEffectTimeSec(L1SkillId.BLESSED_ARMOR);
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, time, armor.getEnchantMagic(), 0));
		}

		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && armorSet.isValid(_owner)) {
				if (armor.getItem().getType2() == 2 && armor.getItem().getType() == 9) {
					if (!armorSet.isEquippedRingOfArmorSet(_owner)) {
						armorSet.giveEffect(_owner);
						_currentArmorSet.add(armorSet);
					}
				} else {
					armorSet.giveEffect(_owner);
					_currentArmorSet.add(armorSet);
				}
			}
		}
		

		if (itemId == 111137 || itemId == 111140 || itemId == 111141) {
			_owner.get_skill().setHalpas(armor);
		}
		if (itemId == 900022) {
			if (_owner.getMapId() >= 1708 && _owner.getMapId() <= 1712) {
				_owner.sendPackets(new S_SkillSound(_owner.getId(), 11101));
				_owner.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, true));
			}
		}

		if (itemId == 423014) {
			_owner.startAHRegeneration();
		}
		if (itemId == 423015) {
			_owner.startSHRegeneration();
		}
		if (itemId == 20380) {
			_owner.startHalloweenRegeneration();
		}
		if (itemId == 20077 || itemId == 20062 || itemId == 120077) {
			if (!_owner.hasSkillEffect(L1SkillId.INVISIBILITY) && !_owner.is_combat_field()) {
				Object[] dollList = _owner.getDollList().values().toArray();
				L1DollInstance doll = null;
				for (Object dollObject : dollList) {
					doll = (L1DollInstance) dollObject;
					doll.deleteDoll(false);
				}
				_owner.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
				_owner.setSkillEffect(L1SkillId.INVISIBILITY, 0);
				_owner.sendPackets(new S_Invis(_owner.getId(), 1));
				
				_owner.broadcastPacket(S_WorldPutObject.get(_owner));
				
				
				L1Party party = _owner.getParty();
				if(party != null){
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_owner)) {
						if(party.isMember(pc)){						
							pc.sendPackets(new S_HPMeter(_owner));
						}
					}
					party.refreshPartyMemberStatus(_owner);
				}
				
				//owner.broadcastPacket_party(S_WorldPutObject.getinvis_party(_owner, false), _owner.getParty());
				
				
				/*ServerBasePacket packet = S_WorldPutObject.getinvis(_owner, true);// 이름 안보이는 패킷
				ServerBasePacket party_packet = S_WorldPutObject.get(_owner);// 이름 보이는 패킷
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_owner)) {
					L1Party party = pc.getParty();
					
					if(party != null && party.isMember(pc)){						
						pc.sendPackets(party_packet, false);
					}else {
						pc.sendPackets(packet, false);
					}
				}
				packet.clear();
				party_packet.clear();*/
				
				
				
				
				
				
				
				//_owner.broadcastPacket(new S_OtherCharPacks(_owner));
				// _owner.broadcastPacket(new S_Invis(_owner.getId(), 1));
			}
		}
		if (itemId == 20288 || itemId == 900111) {
			_owner.sendPackets(new S_Ability(1, true));
			L1ItemInstance teleport_item = _owner.getInventory().findItemId(40100);
			if(teleport_item != null){
				_owner.sendPackets(new S_DeleteInventoryItem(teleport_item));
				SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
				noti.add_item_info(ItemInfo.newInstance(teleport_item, 6));
				_owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
			}
		}
		if (itemId == 20281) {
			_owner.sendPackets(new S_Ability(2, true));
		}
		if (itemId == 900075) {
			_owner.sendPackets(new S_Ability(7, true));
			_owner.sendPackets(new S_Ability(2, true));
			_owner.setPolyRingMaster(true);
		}
		if (itemId == 20036) {
			_owner.sendPackets(new S_Ability(3, true));
		}
		if (itemId == 20284) {
			_owner.sendPackets(new S_Ability(5, true));
		}
		if (itemId == 20207) {
			_owner.sendPackets(new S_SkillIconBlessOfEva(_owner.getId(), -1));
		}

		if (itemId == 20383) {
			if (armor.getChargeCount() != 0) {
				armor.setChargeCount(armor.getChargeCount() - 1);
				_owner.getInventory().updateItem(armor, L1PcInventory.COL_CHARGE_COUNT);
			}
		}
		/*** 50레벨 엘릭서 룬 ***/
		// 민첩의 엘릭서 룬
		if (itemId == 222295) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 222296) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 222297) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.addMaxMp(30);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 222298) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				break;
			}
		}
		// 힘의 엘릭서 룬
		if (itemId == 222299) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				break;
			}
		}
		//TODO 70레벨 엘릭서 룬
		// 민첩의 엘릭서 룬
		if (itemId == 222312) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 222313) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 222314) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 222315) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				break;
			}
		}
		// 힘의 엘릭서 룬
		if (itemId == 222316) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				break;
			}
		}
		//TODO 80레벨 엘릭서 룬
		// 힘의 엘릭서 룬
		if (itemId == 900135) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			}
		}
		// 민첩의 엘릭서 룬
		if (itemId == 900136) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 900137) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 900138) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 900139) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(1);
				break;
			}
		}
		// TODO 85레벨 엘릭서 룬
		// 힘의 엘릭서 룬
		if (itemId == 900140) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			}
		}
		// 민첩의 엘릭서 룬
		if (itemId == 900141) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 900142) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 900143) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 900144) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(2);
				break;
			}
		}
		// TODO 90레벨 엘릭서 룬
		// 힘의 엘릭서 룬
		if (itemId == 900145) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			}
		}
		// 민첩의 엘릭서 룬
		if (itemId == 900146) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 900147) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 900148) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 900149) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(3);
				_owner.addDmgRate(2);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(50);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(50);
				_owner.addBowDmgRate(1);
				_owner.addDmgRate(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 법사
			case 3:
				_owner.addMpr(3);
				_owner.getAbility().addSp(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(-3);
				_owner.addMaxMp(30);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(3);
				_owner.addDamageReductionByArmor(1);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(5);
				_owner.addMaxHp(50);
				_owner.addPVPMagicDamageReduction(3);
				break;
			}
		}
		
		/** 화령의 가더 **/
		if (itemId == 900117) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDmgRate(1);
				break;
			case 5:
				_owner.addDmgRate(1);
				_owner.addHitup(1);
				break;
			case 6:
				_owner.addDmgRate(1);
				_owner.addHitup(2);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 7:
				_owner.addDmgRate(2);
				_owner.addHitup(3);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 8:
				_owner.addDmgRate(3);
				_owner.addHitup(4);
				_owner.addSpecialResistance(eKind.ALL, 3);
				break;
			case 9:
				_owner.addDmgRate(4);
				_owner.addHitup(5);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 10:
				_owner.addDmgRate(5);
				_owner.addHitup(6);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			default:
				break;
			}
		}
		/** 풍령의 가더 **/
		if (itemId == 900118) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowDmgup(1);
				break;
			case 5:
				_owner.addBowDmgup(1);
				_owner.addBowHitup(1);
				break;
			case 6:
				_owner.addBowDmgup(1);
				_owner.addBowHitup(2);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 7:
				_owner.addBowDmgup(2);
				_owner.addBowHitup(3);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 8:
				_owner.addBowDmgup(3);
				_owner.addBowHitup(4);
				_owner.addSpecialResistance(eKind.ALL, 3);
				break;
			case 9:
				_owner.addBowDmgup(4);
				_owner.addBowHitup(5);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 10:
				_owner.addBowDmgup(5);
				_owner.addBowHitup(6);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			default:
				break;
			}
		}
		/** 수령의 가더 **/
		if (itemId == 900119) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getAbility().addSp(1);
				break;
			case 5:
				_owner.getAbility().addSp(1);
				_owner.addBaseMagicHitUp(1);
				break;
			case 6:
				_owner.getAbility().addSp(1);
				_owner.addBaseMagicHitUp(2);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 7:
				_owner.getAbility().addSp(2);
				_owner.addBaseMagicHitUp(3);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 8:
				_owner.getAbility().addSp(3);
				_owner.addBaseMagicHitUp(4);
				_owner.addSpecialResistance(eKind.ALL, 3);
				break;
			case 9:
				_owner.getAbility().addSp(4);
				_owner.addBaseMagicHitUp(5);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 10:
				_owner.getAbility().addSp(5);
				_owner.addBaseMagicHitUp(6);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			default:
				break;
			}
		}
		/** 에바의 방패 **/
		if (itemId == 20235) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 2);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 3);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 4);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 6);
				break;
			case 10:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			default:
				break;
			}
		}
		/** 시어의 심안 **/
		if (itemId == 22214) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.SPIRIT, 2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, 3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, 4);
				break;
			case 7:
				_owner.addBaseMagicHitUp(1);// 마법 적중 적용
				_owner.addSpecialResistance(eKind.SPIRIT, 5);
				break;
			case 8:
				_owner.addBaseMagicHitUp(2);
				_owner.addSpecialResistance(eKind.SPIRIT, 6);
				break;
			case 9:
				_owner.addBaseMagicHitUp(3);
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				break;
			case 10:
				_owner.addBaseMagicHitUp(4);
				_owner.addSpecialResistance(eKind.SPIRIT, 8);
				break;
			default:
				break;
			}
		}
		
		/** 리치의 수정구 **/
		if (itemId == 900165) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				
				_owner.addSpecialResistance(eKind.SPIRIT, 2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, 3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, 4);
				break;
			case 7:
				_owner.getAbility().addSp(1);
				_owner.addBaseMagicHitUp(1);// 마법 적중 적용
				_owner.addSpecialResistance(eKind.SPIRIT, 5);
				break;
			case 8:
				_owner.getAbility().addSp(2);
				_owner.addBaseMagicHitUp(2);
				_owner.addSpecialResistance(eKind.SPIRIT, 6);
				break;
			case 9:
				_owner.getAbility().addSp(3);
				_owner.addBaseMagicHitUp(3);
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				break;
			case 10:
				_owner.getAbility().addSp(4);
				_owner.addBaseMagicHitUp(4);
				_owner.addSpecialResistance(eKind.SPIRIT, 8);
				break;
			default:
				break;
			}
		}
		/** 반역자의 방패 **/
		if (itemId == 22263) {
			switch (itemlvl) {
			case 5:
				_owner.addMaxHp(20);
				_owner.addSpecialPierce(eKind.ABILITY, 1);
				break;
			case 6:
				_owner.addMaxHp(40);
				_owner.addSpecialPierce(eKind.ABILITY, 2);
				break;
			case 7:
				_owner.addMaxHp(60);
				_owner.addSpecialPierce(eKind.ABILITY, 3);
				break;
			case 8:
				_owner.addMaxHp(80);
				_owner.addSpecialPierce(eKind.ABILITY, 4);
				break;
			case 9:
				_owner.addMaxHp(100);
				_owner.addSpecialPierce(eKind.ABILITY, 5);
				break;
			case 10:
				_owner.addMaxHp(120);
				_owner.addSpecialPierce(eKind.ABILITY, 6);
				break;
			default:
				break;
			}
		}
		/** 지령의 가더 **/
		if (itemId == 900120) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getResistance().addcalcPcDefense(1);
				break;
			case 5:
				_owner.getResistance().addcalcPcDefense(1);
				_owner.getResistance().addMr(2);
				break;
			case 6:
				_owner.getResistance().addcalcPcDefense(1);
				_owner.getResistance().addMr(3);
				break;
			case 7:
				_owner.getResistance().addcalcPcDefense(2);
				_owner.getResistance().addMr(4);
				break;
			case 8:
				_owner.getResistance().addcalcPcDefense(3);
				_owner.getResistance().addMr(5);
				break;
			case 9:
				_owner.getResistance().addcalcPcDefense(4);
				_owner.getResistance().addMr(6);
				break;
			case 10:
				_owner.getResistance().addcalcPcDefense(5);
				_owner.getResistance().addMr(7);
				break;
			default:
				break;
			}
		}
		
		/** 마법사의가더 **/
		if (itemId == 22255) {
			switch (itemlvl) {
			case 5:
			case 6:
				_owner.getAbility().addSp(1);
				break;
			case 7:
			case 8:
				_owner.getAbility().addSp(2);
				break;
			case 9:
			case 10:
				_owner.getAbility().addSp(3);
				break;
			default:
				break;
			}
		}
		/** 체력의 가더 **/
		if (itemId == 22256) {
			switch (itemlvl) {
			case 5:
			case 6:
				_owner.addMaxHp(25);
				break;
			case 7:
			case 8:
				_owner.addMaxHp(50);
				break;
			case 9:
			case 10:
				_owner.addMaxHp(75);
				break;
			default:
				break;
			}
		}
		// TODO 투사의 휘장
		if (itemId == 900152) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(5);
				break;
			case 1:
				_owner.addMaxHp(10);
				break;
			case 2:
				_owner.addMaxHp(15);
				break;
			case 4:
				_owner.getAC().addAc(-1);
				_owner.addMaxHp(25);
				break;
			case 5:
				_owner.getAC().addAc(-2);
				_owner.addMaxHp(30);
				_owner.addDmgRate(1);// 근거리 대미지
				break;
			case 6:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(35);
				_owner.addDmgRate(2);// 근거리 대미지
				_owner.add_melee_critical_rate(1);// 근거리 치명타
				break;
			case 7:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(40);
				_owner.addDmgRate(3);// 근거리 대미지
				_owner.add_melee_critical_rate(3);// 근거리 치명타
				break;
			case 8:
				_owner.getAC().addAc(-3);
				_owner.addDmgRate(4);// 근거리 대미지
				_owner.add_melee_critical_rate(5);// 근거리 치명타
				break;
			default:
				break;
			}
		}
		// TODO 명궁의 휘장
		if (itemId == 900153) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(5);
				break;
			case 1:
				_owner.addMaxHp(10);
				break;
			case 2:
				_owner.addMaxHp(15);
				break;
			case 4:
				_owner.getAC().addAc(-1);
				_owner.addMaxHp(25);
				break;
			case 5:
				_owner.getAC().addAc(-2);
				_owner.addMaxHp(30);
				_owner.addBowDmgup(1);//원거리 대미지
				break;
			case 6:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(35);
				_owner.addBowDmgup(2);//원거리 대미지
				_owner.add_missile_critical_rate(1);// 원거리 치명타
				break;
			case 7:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(40);
				_owner.addBowDmgup(3);//원거리 대미지
				_owner.add_missile_critical_rate(3);// 원거리 치명타
				break;
			case 8:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(50);
				_owner.addBowDmgup(4);//원거리 대미지
				_owner.add_missile_critical_rate(5);// 원거리 치명타
				break;
			default:
				break;
			}
		}
		// TODO 현자의 휘장
		if (itemId == 900154) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(5);
				break;
			case 1:
				_owner.addMaxHp(10);
				break;
			case 2:
				_owner.addMaxHp(15);
				break;
			case 4:
				_owner.getAC().addAc(-1);
				_owner.addMaxHp(25);
				break;
			case 5:
				_owner.getAC().addAc(-2);
				_owner.addMaxHp(30);
				_owner.addHitup(1);//근거리 명중
				break;
			case 6:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(35);
				_owner.addHitup(2);//근거리 명중
				_owner.addBaseMagicCritical(1);// 마법 치명타
				break;
			case 7:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(40);
				_owner.addHitup(3);//근거리 명중
				_owner.addBaseMagicCritical(2);// 마법 치명타
				break;
			case 8:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(50);
				_owner.addHitup(4);//근거리 명중
				_owner.addBaseMagicCritical(4);// 마법 치명타
				break;
			default:
				break;
			}
		}
		// TODO 투사의 문장
		if (itemId == 900127) {
			switch (itemlvl) {
			case 4:
				_owner.addHitup(1);// 근거리 명중
				break;
			case 5:
				_owner.addDmgRate(1);// 근거리 대미지
				_owner.addHitup(1);// 근거리 명중
				break;
			case 6:
				_owner.addDmgRate(2);// 근거리 대미지
				_owner.addHitup(2);// 근거리 명중
				break;
			case 7:
				_owner.addDmgRate(3);// 근거리 대미지
				_owner.addHitup(3);// 근거리 명중
				break;
			case 8:
				_owner.addDmgRate(4);// 근거리 대미지
				_owner.addHitup(4);// 근거리 명중
				break;
			default:
				break;
			}
		}
		// TODO 명궁의 문장
		if (itemId == 900128) {
			switch (itemlvl) {
			case 4:
				_owner.addBowHitup(1);//원거리 명중
				break;
			case 5:
				_owner.addBowDmgup(1);//원거리 대미지
				_owner.addBowHitup(1);//원거리 명중
				break;
			case 6:
				_owner.addBowDmgup(2);//원거리 대미지
				_owner.addBowHitup(2);//원거리 명중
				break;
			case 7:
				_owner.addBowDmgup(3);//원거리 대미지
				_owner.addBowHitup(3);//원거리 명중
				break;
			case 8:
				_owner.addBowDmgup(4);//원거리 대미지
				_owner.addBowHitup(4);//원거리 명중
				break;
			default:
				break;
			}
		}
		// TODO 현자의 문장
		if (itemId == 900129) {
			switch (itemlvl) {
			case 4:
				_owner.addBaseMagicHitUp(1);// 마법 적중
				break;
			case 5:
				_owner.getAbility().addSp(1);// SP
				_owner.addBaseMagicHitUp(1);// 마법 적중
				break;
			case 6:
				_owner.getAbility().addSp(2);// SP
				_owner.addBaseMagicHitUp(2);// 마법 적중
				break;
			case 7:
				_owner.getAbility().addSp(3);// SP
				_owner.addBaseMagicHitUp(3);// 마법 적중
				break;
			case 8:
				_owner.getAbility().addSp(4);// SP
				_owner.addBaseMagicHitUp(4);// 마법 적중
				break;
			default:
				break;
			}
		}
		// TODO 수호의 투사 문장
		if (itemId == 900124) {
			switch (itemlvl) {
			case 5:
				_owner.addDmgRate(1);// 근거리 대미지
				_owner.addHitup(2);// 근거리 명중
				_owner.getResistance().addMr(4);// MR
				_owner.addEinhasadBlessper(5);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(1);//pvp마법대미지감소		
				break;
			case 6:
				_owner.addDmgRate(2);// 근거리 대미지
				_owner.addHitup(3);// 근거리 명중
				_owner.getResistance().addMr(6);// MR
				_owner.addEinhasadBlessper(10);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(2);//pvp마법대미지감소	
				break;
			case 7:
				_owner.addDmgRate(3);// 근거리 대미지
				_owner.addHitup(4);// 근거리 명중
				_owner.getResistance().addMr(8);// MR
				_owner.addEinhasadBlessper(15);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(3);//pvp마법대미지감소	
				break;
			case 8:
				_owner.addDmgRate(4);// 근거리 대미지
				_owner.addHitup(5);// 근거리 명중
				_owner.getResistance().addMr(10);// MR
				_owner.addEinhasadBlessper(20);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(5);//pvp마법대미지감소	
				break;
			default:
				break;
			}
		}
		// TODO 수호의 명궁 문장
		if (itemId == 900125) {
			switch (itemlvl) {
			case 5:
				_owner.addBowDmgup(1);//원거리 대미지
				_owner.addBowHitup(2);//원거리 명중
				_owner.getResistance().addMr(4);// MR
				_owner.addEinhasadBlessper(5);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(1);//pvp마법대미지감소		
				break;
			case 6:
				_owner.addBowDmgup(2);//원거리 대미지
				_owner.addBowHitup(3);//원거리 명중
				_owner.getResistance().addMr(6);// MR
				_owner.addEinhasadBlessper(10);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(2);//pvp마법대미지감소		
				break;
			case 7:
				_owner.addBowDmgup(3);//원거리 대미지
				_owner.addBowHitup(4);//원거리 명중
				_owner.getResistance().addMr(8);// MR
				_owner.addEinhasadBlessper(15);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(3);//pvp마법대미지감소		
				break;
			case 8:
				_owner.addBowDmgup(4);//원거리 대미지
				_owner.addBowHitup(5);//원거리 명중
				_owner.getResistance().addMr(10);// MR
				_owner.addEinhasadBlessper(20);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(5);//pvp마법대미지감소	
				break;
			default:
				break;
			}
		}
		// TODO 수호의 현자 문장
		if (itemId == 900126) {
			switch (itemlvl) {
			case 5:
				_owner.getAbility().addSp(1);// SP
				_owner.addBaseMagicHitUp(2);// 마법 적중
				_owner.getResistance().addMr(4);// MR
				_owner.addEinhasadBlessper(5);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(1);//pvp마법대미지감소	
				break;
			case 6:
				_owner.getAbility().addSp(2);// SP
				_owner.addBaseMagicHitUp(3);// 마법 적중
				_owner.getResistance().addMr(6);// MR
				_owner.addEinhasadBlessper(10);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(2);//pvp마법대미지감소	
				break;
			case 7:
				_owner.getAbility().addSp(3);// SP
				_owner.addBaseMagicHitUp(4);// 마법 적중
				_owner.getResistance().addMr(8);// MR
				_owner.addEinhasadBlessper(15);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(3);//pvp마법대미지감소	
				break;
			case 8:
				_owner.getAbility().addSp(4);// SP
				_owner.addBaseMagicHitUp(5);// 마법 적중
				_owner.getResistance().addMr(10);// MR
				_owner.addEinhasadBlessper(20);// 축복 소모 효율
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(5);//pvp마법대미지감소	
				break;
			default:
				break;
			}
		}
		/** 완력의 문장 효과 **/
		if (itemId == 900049 || itemId == 900093 || itemId == 900096) {
			switch (itemlvl) {
			case 5:
				_owner.addHitup(1);
				break;
			case 6:
				_owner.addHitup(1);
				_owner.addDmgRate(1);
				break;
			case 7:
				_owner.addHitup(2);
				_owner.addDmgRate(2);
				break;
			case 8:
				_owner.addHitup(3);
				_owner.addDmgRate(3);
				break;
			case 9:
				_owner.addHitup(4);
				_owner.addDmgRate(4);
				break;
			case 10:
				_owner.addHitup(5);
				_owner.addDmgRate(5);
				break;
			default:
				break;
			}
		}
		//고대 마물 시리즈
		if (itemId == 900015 || itemId == 900016 || itemId == 900017 || itemId == 900018) {
			switch (itemlvl) {
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
				_owner.addDmgRate(Config.DEDMG2);
				_owner.addBowDmgup(Config.DEDMG2);
				break;
		
			default:
				break;
			}
		}
		
		
		
		
		/** 민첩의 문장 효과 **/
		if (itemId == 900050 || itemId == 900094 || itemId == 900097) {
			switch (itemlvl) {
			case 5:
				_owner.addBowHitup(1);
				break;
			case 6:
				_owner.addBowHitup(1);
				_owner.addBowDmgup(1);
				break;
			case 7:
				_owner.addBowHitup(2);
				_owner.addBowDmgup(2);
				break;
			case 8:
				_owner.addBowHitup(3);
				_owner.addBowDmgup(3);
				break;
			case 9:
				_owner.addBowHitup(4);
				_owner.addBowDmgup(4);
				break;
			case 10:
				_owner.addBowHitup(5);
				_owner.addBowDmgup(5);
				break;
			default:
				break;
			}
		}
		/** 지식의 문장 효과 **/
		if (itemId == 900051 || itemId == 900095 || itemId == 900098) {
			switch (itemlvl) {
			case 5:
				_owner.addBaseMagicHitUp(1);
				break;
			case 6:
				_owner.addBaseMagicHitUp(1);
				_owner.getAbility().addSp(1);
				break;
			case 7:
				_owner.addBaseMagicHitUp(2);
				_owner.getAbility().addSp(2);
				break;
			case 8:
				_owner.addBaseMagicHitUp(3);
				_owner.getAbility().addSp(3);
				break;
			case 9:
				_owner.addBaseMagicHitUp(4);
				_owner.getAbility().addSp(4);
				break;
			case 10:
				_owner.addBaseMagicHitUp(5);
				_owner.getAbility().addSp(5);
				break;
			default:
				break;
			}
		}
		/** 성장/회복의 문장 **/
		if (itemId == 900020 || itemId == 900021 || itemId == 900049 || itemId == 900050 || itemId == 900051 || itemId >= 900093 && itemId <= 900099
				|| itemId >= 900124 && itemId <= 900126 || itemId >= 900127 && itemId <= 900130) {
			switch (itemlvl) {
			case 0:
				_owner.getAC().addAc(0);
				break;
			case 1:
				_owner.getAC().addAc(1);
				break;
			case 2:
				_owner.getAC().addAc(2);
				break;
			case 3:
				_owner.getAC().addAc(3);
				break;
			case 4:
				_owner.getAC().addAc(4);
				break;
			case 5:
				_owner.getAC().addAc(5);
				break;
			case 6:
				_owner.getAC().addAc(6);
				break;
			case 7:
				_owner.getAC().addAc(7);
				break;
			case 8:
				_owner.getAC().addAc(8);
				break;
			case 9:
				_owner.getAC().addAc(9);
				break;
			case 10:
				_owner.getAC().addAc(10);
				break;
			default:
				break;
			}
		}

		/** 실프의티셔츠 내성 인챈별 **/
		if (itemId == 900019) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDamageReductionByArmor(1);//대미지 감소
				_owner.addDmgRate(1); 
				_owner.addBowDmgRate(1);
				_owner.getAbility().addSp(1);
				break;
			case 5:
				_owner.addDamageReductionByArmor(1);//대미지 감소
				_owner.addDmgRate(1); 
				_owner.addBowDmgRate(1);
				_owner.getAbility().addSp(1);
				_owner.getResistance().addMr(4);
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 6:
				_owner.addDamageReductionByArmor(1);//대미지 감소
				_owner.addDmgRate(1); 
				_owner.addBowDmgRate(1);
				_owner.getAbility().addSp(1);
				_owner.getResistance().addMr(5);
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			case 7:
				_owner.addDamageReductionByArmor(1);//대미지 감소
				_owner.addDmgRate(1); 
				_owner.addBowDmgRate(1);
				_owner.getAbility().addSp(1);
				_owner.addHitup(1);// 근거리 명중
				_owner.addBowHitup(1); // 원거리 명중
				_owner.getResistance().addMr(6);
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 8:
				_owner.addDamageReductionByArmor(1);//대미지 감소
				_owner.addDmgRate(1); 
				_owner.addBowDmgRate(1);
				_owner.getAbility().addSp(1);
				_owner.addHitup(3);// 근거리 명중
				_owner.addBowHitup(3); // 원거리 명중			
				_owner.addBaseMagicHitUp(2);// 마법 적중
				_owner.getResistance().addMr(8);
				_owner.addSpecialResistance(eKind.ABILITY, 12);
				_owner.addSpecialResistance(eKind.ALL, 2);	
				break;
			case 9:
				_owner.addDamageReductionByArmor(2);//대미지 감소
				_owner.addDmgRate(2); 
				_owner.addBowDmgRate(2);
				_owner.getAbility().addSp(2);
				_owner.addHitup(5);// 근거리 명중
				_owner.addBowHitup(5); // 원거리 명중			
				_owner.addBaseMagicHitUp(4);// 마법 적중
				_owner.getResistance().addMr(11);
				_owner.addSpecialResistance(eKind.ABILITY, 15);
				_owner.addSpecialResistance(eKind.ALL, 4);	
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDamageReductionByArmor(3);//대미지 감소
				_owner.addDmgRate(3); 
				_owner.addBowDmgRate(3);
				_owner.getAbility().addSp(3);
				_owner.addHitup(7);// 근거리 명중
				_owner.addBowHitup(7); // 원거리 명중			
				_owner.addBaseMagicHitUp(5);// 마법 적중
				_owner.getResistance().addMr(14);
				_owner.addSpecialResistance(eKind.ABILITY, 17);
				_owner.addSpecialResistance(eKind.ALL, 6);	
				_owner.addMaxHp(200);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			default:
				break;
			}
		}

		/** 체력의 각반 **/
		if (itemId == 900023) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(10);
				break;
			case 1:
				_owner.addMaxHp(15);
				break;
			case 2:
				_owner.addMaxHp(20);
				break;
			case 3:
				_owner.addMaxHp(25);
				break;
			case 4:
				_owner.addMaxHp(30);
				break;
			case 5:
				_owner.addMaxHp(35);
				break;
			case 6:
				_owner.addMaxHp(40);
				break;
			case 7:
				_owner.addMaxHp(45);
				break;
			case 8:
				_owner.addMaxHp(50);
				break;
			case 9:
				_owner.addMaxHp(55);
				break;
			default:
				break;
			}
		}
		/** 유니콘의 완력 각반 **/
		if (itemId == 900030) {
			switch (itemlvl) {
			case 9:
			case 10:
			case 11:
			case 12:
				_owner.addDmgRate(1);
				break;
			default:
				break;
			}
		}
		/** 유니콘의 민첩 각반 **/
		if (itemId == 900031) {
			switch (itemlvl) {
			case 9:
			case 10:
			case 11:
			case 12:
				_owner.addBowHitup(1);
				break;
			default:
				break;
			}
		}
		/** 유니콘의 지식 각반 **/
		if (itemId == 900032) {
			switch (itemlvl) {
			case 9:
			case 10:
			case 11:
			case 12:
				_owner.getAbility().addSp(1);
				break;
			}
		}
		/** 맘보 모자 **/
		if (itemId == 20016 || itemId == 120016) {
			switch (itemlvl) {
			case 7:
			case 8:
			case 9:
			case 10:
				_owner.getAbility().addCha(2);
				break;
			default:
				break;
			}
		}
		/** 맘보 코트 **/
		if (itemId == 20112 || itemId == 120112){
			switch (itemlvl) {
			case 7:
			case 8:
			case 9:
			case 10:
				_owner.getAbility().addCha(3);
				break;
			default:
				break;
			}
		}
		
		/** 예언자의 견갑 **/
		if (itemId == 900080) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(1);// 마법 적중
				break;
			default:
				break;
			}
		}
		
		/** 지휘관/사이하/대마법사의 견갑 pvp감소+공포+피 **/
		if (itemId >= 900021 && itemId <= 900203) {
			switch (itemlvl) {
			case 5:
				_owner.getResistance().addcalcPcDefense(1); 
				_owner.addSpecialResistance(eKind.FEAR, 1);
				_owner.addMaxHp(20);
				break;
			case 6:
				_owner.getResistance().addcalcPcDefense(2); 
				_owner.addSpecialResistance(eKind.FEAR, 2);
				_owner.addMaxHp(40);
				break;
			case 7:
				_owner.getResistance().addcalcPcDefense(1); 
				_owner.addSpecialResistance(eKind.FEAR, 3);
				_owner.addMaxHp(60);
				break;
			case 8:
				_owner.getResistance().addcalcPcDefense(1); 
				_owner.addSpecialResistance(eKind.FEAR, 4);
				_owner.addMaxHp(80);
				break;
			case 9:
				_owner.getResistance().addcalcPcDefense(1); 
				_owner.addSpecialResistance(eKind.FEAR, 5);
				_owner.addMaxHp(100);
				break;
			default:
				break;
			}
		}
		
	
	
		/** 칠흑의 망토  **/
		if (itemId == 900076) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.getAbility().addCha(1);
				break;
			case 7:
				_owner.getAbility().addCha(2);
				break;
			case 8:
				_owner.getAbility().addCha(3);
				break;
			case 9:
				_owner.getAbility().addCha(4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addCha(4);
				break;
			default:
				break;
			}
		}
		/** 커츠의 투사 휘장 **/
		if (itemId == 900081) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(5);
				break;	
			case 1:
				_owner.addMaxHp(10);
				break;	
			case 2:
				_owner.addMaxHp(15);
				break;	
			case 3:
				_owner.addMaxHp(20);
				break;	
			case 4:
				_owner.getAC().addAc(-1);
				_owner.addMaxHp(25);
				break;	
			case 5:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(30);
				_owner.addHitup(1);
				_owner.addDamageReductionByArmor(1);
				break;
			case 6:
				_owner.getAC().addAc(-5);//AC
				_owner.addMaxHp(35);//HP
				_owner.addHitup(1);//근거리 명중
				_owner.addDamageReductionByArmor(2);//대미지감소
				_owner.getResistance().addMr(3);//MR
				_owner.addDmgRate(2);//근거리 대미지
				break;
			case 7:
				_owner.getAC().addAc(-6);
				_owner.addMaxHp(40);
				_owner.addDamageReductionByArmor(3);//대미지감소
				_owner.getResistance().addMr(5);//MR
				_owner.addDmgRate(3);//근거리 대미지
				_owner.addHitup(3);//근거리 명중
				_owner.getResistance().addcalcPcDefense(1);//PVP대미지감소
				break;
			case 8:
				_owner.getAC().addAc(-7);
				_owner.addMaxHp(50);
				_owner.addDamageReductionByArmor(4);//대미지감소
				_owner.getResistance().addMr(7);//MR
				_owner.addDmgRate(4);//근거리 대미지
				_owner.addHitup(5);//근거리 명중
				_owner.getResistance().addcalcPcDefense(2);//PVP대미지감소
				break;
			default:
				break;
			}
		}		
		/** 커츠의 명궁 휘장 **/
		if (itemId == 900082) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(5);
				break;
			case 1:
				_owner.addMaxHp(10);
				break;
			case 2:
				_owner.addMaxHp(15);
				break;
			case 3:
				_owner.addMaxHp(20);
				break;
			case 4:
				_owner.getAC().addAc(-1);
				_owner.addMaxHp(25);
				break;
			case 5:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(30);
				_owner.addBowHitup(1);
				_owner.addDamageReductionByArmor(1);
				break;
			case 6:
				_owner.getAC().addAc(-5);
				_owner.addMaxHp(35);
				_owner.addDamageReductionByArmor(2);
				_owner.getResistance().addMr(3);//MR
				_owner.addBowDmgup(2);
				_owner.addBowHitup(1);
				break;
			case 7:
				_owner.getAC().addAc(-6);
				_owner.addMaxHp(40);
				_owner.addDamageReductionByArmor(3);
				_owner.getResistance().addMr(5);//MR
				_owner.addBowDmgup(3);
				_owner.addBowHitup(3);
				_owner.getResistance().addcalcPcDefense(1);//PVP대미지감소
				break;
			case 8:
				_owner.getAC().addAc(-7);
				_owner.addMaxHp(50);
				_owner.addDamageReductionByArmor(4);//대미지 감소
				_owner.getResistance().addMr(7);//MR
				_owner.addBowDmgup(4);
				_owner.addBowHitup(5);
				_owner.getResistance().addcalcPcDefense(2);//PVP대미지감소
				break;
			default:
				break;
			}
		}	
		/** 커츠의 현자 휘장 **/
		if (itemId == 900083) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(5);
				break;
			case 1:
				_owner.addMaxHp(10);
				break;
			case 2:
				_owner.addMaxHp(15);
				break;
			case 3:
				_owner.addMaxHp(20);
				break;
			case 4:
				_owner.getAC().addAc(-1);
				_owner.addMaxHp(25);
				break;
			case 5:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(30);
				_owner.addDamageReductionByArmor(1);//대미지 감소
				_owner.addHitup(1);
				break;
			case 6:
				_owner.getAC().addAc(-5);
				_owner.addMaxHp(35);
				_owner.addDamageReductionByArmor(2);//대미지 감소
				_owner.getResistance().addMr(3);//MR
				_owner.addHitup(2);//근거리 명중
				_owner.addBaseMagicCritical(1);// 마법 치명타
				break;
			case 7:
				_owner.getAC().addAc(-6);
				_owner.addMaxHp(40);
				_owner.addDamageReductionByArmor(3);//대미지 감소
				_owner.getResistance().addMr(5);//MR
				_owner.addHitup(3);//근거리 명중
				_owner.addBaseMagicCritical(3);// 마법 치명타
				_owner.getResistance().addcalcPcDefense(1);//PVP대미지감소
				break;
			case 8:
				_owner.getAC().addAc(-7);
				_owner.addMaxHp(50);
				_owner.addDamageReductionByArmor(4);//대미지 감소
				_owner.getResistance().addMr(7);//MR
				_owner.addHitup(4);//근거리 명중
				_owner.addBaseMagicCritical(5);// 마법 치명타
				_owner.getResistance().addcalcPcDefense(2);//PVP대미지감소
				break;
			default:
				break;
			}
		}	
		/** 커츠의 수호 휘장 **/
		if (itemId == 900084) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(5);
				break;	
			case 1:
				_owner.addMaxHp(10);
				break;	
			case 2:
				_owner.addMaxHp(15);
				break;	
			case 3:
				_owner.getAC().addAc(-1);
				_owner.addMaxHp(20);
				break;	
			case 4:
				_owner.getAC().addAc(-2);
				_owner.addMaxHp(25);
				break;	
			case 5:
				_owner.getAC().addAc(-3);
				_owner.addMaxHp(30);
				_owner.addDamageReductionByArmor(1);
				break;	
			case 6:
				_owner.getAC().addAc(-5);
				_owner.addMaxHp(35);
				_owner.addDamageReductionByArmor(2);
				_owner.getResistance().addMr(3);
				break;	
			case 7:
				_owner.getAC().addAc(-6);
				_owner.addMaxHp(40);
				_owner.addDamageReductionByArmor(3);
				_owner.getResistance().addMr(5);
				break;	
			case 8:
				_owner.getAC().addAc(-7);
				_owner.addMaxHp(50);
				_owner.addDamageReductionByArmor(4);
				_owner.getResistance().addMr(7);
				break;
			default:
				break;
			}
		}	
		/** 거대여왕 개미의 금빛 날개 **/
		if (itemId == 20049 || itemId == 900057) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.SPIRIT, 2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, 3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, 4);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.SPIRIT, 5);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.SPIRIT, 6);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				break;
			}
		}
		/** 거대여왕 개미의 은빛 날개 **/
		if (itemId == 20050 || itemId == 900056) {
			switch (itemlvl) {
			case 5:
				_owner.addSpecialPierce(eKind.SPIRIT, 1);
				break;
			case 6:
				_owner.addSpecialPierce(eKind.SPIRIT, 2);
				break;
			case 7:
				_owner.addSpecialPierce(eKind.SPIRIT, 3);
				break;
			case 8:
				_owner.addSpecialPierce(eKind.SPIRIT, 4);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.SPIRIT, 5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				break;
			}
		}
		
		/** 빛나는 아덴 용사의 망토 **/
		if (itemId == 900189) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.SPIRIT, 2);
				_owner.addSpecialResistance(eKind.FEAR, 2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, 3);
				_owner.addSpecialResistance(eKind.FEAR, 2);
				_owner.addDamageReductionByArmor(1);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, 4);
				_owner.addSpecialResistance(eKind.FEAR, 2);
				_owner.addDamageReductionByArmor(2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.SPIRIT, 5);
				_owner.addSpecialResistance(eKind.FEAR, 3);
				_owner.addDamageReductionByArmor(3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.SPIRIT, 6);
				_owner.addSpecialResistance(eKind.FEAR, 4);
				_owner.addDamageReductionByArmor(4);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				_owner.addSpecialResistance(eKind.FEAR, 5);
				_owner.addDamageReductionByArmor(5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				_owner.addSpecialResistance(eKind.FEAR, 5);
				_owner.addDamageReductionByArmor(5);
				break;
			}
		}
		
		/** 리치의 반지 **/
		if (itemId == 900163) {
			switch (itemlvl) {
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
				_owner.addBaseMagicCritical(3);// 마법 치명타
				break;
			}
		}
		/** 뱀파이어의 망토 **/
		if (itemId == 20079) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.addSpecialResistance(eKind.FEAR, 2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.FEAR, 3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.FEAR, 4);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.FEAR, 5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.FEAR, 5);
				break;
			}
		}
		
		/** 아이리스, 머미로드, 프로켈의 부츠 **/
		if (itemId == 900155) {
			switch (itemlvl) {
			case 5:
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.ALL, 3);
				_owner.getResistance().addMr(3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.ALL, 4);
				_owner.getResistance().addMr(4);
				break;
			case 9:
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.ALL, 5);
				_owner.getResistance().addMr(5);
				_owner.addDamageReductionByArmor(1);
				break;
			default:
				break;
			}
		}
		/** 머미로드의 장갑 **/
		if (itemId == 900156) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.addBaseMagicHitUp(1);
				_owner.getAbility().addSp(1);
				break;
			case 7:
				_owner.addBaseMagicHitUp(2);
				_owner.getAbility().addSp(2);
				break;
			case 8:
				_owner.addBaseMagicHitUp(3);
				_owner.getAbility().addSp(3);
				break;
			case 9:
				_owner.addBaseMagicHitUp(3);
				_owner.getAbility().addSp(3);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(3);
				break;
			default:
				break;
			}
		}
		/** 쿠거의 가더 **/
		if (itemId == 900157) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addHitup(3);
				break;
			case 5:
				_owner.addHitup(4);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 6:
				_owner.addHitup(5);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 7:
				_owner.addHitup(6);
				_owner.addSpecialResistance(eKind.ALL, 3);
				break;
			case 8:
				_owner.addHitup(7);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 9:
				_owner.addHitup(8);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addHitup(8);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			default:
				break;
			}
		}
		/** 우그누스의 가더 **/
		if (itemId == 900158) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowHitup(3);
				break;
			case 5:
				_owner.addBowHitup(4);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 6:
				_owner.addBowHitup(5);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 7:
				_owner.addBowHitup(6);
				_owner.addSpecialResistance(eKind.ALL, 3);
				break;
			case 8:
				_owner.addBowHitup(7);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 9:
				_owner.addBowHitup(8);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowHitup(8);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			default:
				break;
			}
		}
		/** 나이트발드의 각반 **/
		if (itemId == 900159) {
			switch (itemlvl) {
			case 5:
				_owner.addHitup(1);
			//	_owner.addDmgup(1);
				break;
			case 6:
				_owner.addHitup(2);
			//	_owner.addDmgup(2);
				break;
			case 7:
				_owner.addHitup(3);
			//	_owner.addDmgup(3);
				break;
			case 8:
				_owner.addHitup(4);
			//	_owner.addDmgup(4);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addHitup(5);
			//	_owner.addDmgup(5);
				break;
			default:
				break;
			}
		}
		/** 아이리스의 각반 **/
		if (itemId == 900160) {
			switch (itemlvl) {
			case 5:
				_owner.addBowHitup(1);
			//	_owner.addBowDmgup(1);
				break;
			case 6:
				_owner.addBowHitup(2);
			//	_owner.addBowDmgup(2);
				break;
			case 7:
				_owner.addBowHitup(3);
			//	_owner.addBowDmgup(3);
				break;
			case 8:
				_owner.addBowHitup(4);
			//	_owner.addBowDmgup(4);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowHitup(5);
			//	_owner.addBowDmgup(5);
				break;
			default:
				break;
			}
		}
		/** 뱀파이어의 각반 **/
		if (itemId == 900161) {
			switch (itemlvl) {
			case 5:
				_owner.addBaseMagicHitUp(1);
			//	_owner.getAbility().addSp(1);
				break;
			case 6:
				_owner.addBaseMagicHitUp(2);
			//	_owner.getAbility().addSp(2);
				break;
			case 7:
				_owner.addBaseMagicHitUp(3);
			//	_owner.getAbility().addSp(3);
				break;
			case 8:
				_owner.addBaseMagicHitUp(4);
			//	_owner.getAbility().addSp(4);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBaseMagicHitUp(5);
			//	_owner.getAbility().addSp(5);
				break;
			default:
				break;
			}
		}
		
		/** 드래곤 슬레이어의 각반 **/
		if (itemId == 900200) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getResistance().addMr(7);
				_owner.getResistance().addcalcPcDefense(2);
				_owner.addMaxHp(100);
				break;
			case 5:
				_owner.getResistance().addMr(9);
				_owner.getResistance().addcalcPcDefense(3);
				_owner.addMaxHp(100);
				break;
			case 6:
				_owner.getResistance().addMr(11);
				_owner.getResistance().addcalcPcDefense(4);
				_owner.addMaxHp(100);
				break;
			case 7:
				_owner.getResistance().addMr(13);
				_owner.getResistance().addcalcPcDefense(5);
				_owner.addMaxHp(150);
				break;
			case 8:
				_owner.getResistance().addMr(15);
				_owner.getResistance().addcalcPcDefense(6);
				_owner.addMaxHp(200);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getResistance().addMr(17);
				_owner.getResistance().addcalcPcDefense(7);
				_owner.addMaxHp(250);
				break;
			default:
				break;
			}
		}
		/** 대마법사의 모자 **/
		if (itemId == 202022) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, 1);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, 2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.SPIRIT, 3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.SPIRIT, 4);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.SPIRIT, 5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, 6);
				break;
			default:
				break;
			}
		}
		/** 붉은 해의 각반 **/
		if (itemId == 9001117) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addMaxHp(5);
				_owner.addDamageReductionByArmor(1);
				break;
			case 5:
				_owner.addMaxHp(15);
				_owner.addDamageReductionByArmor(1);
				break;
			case 6:
				_owner.addMaxHp(25);
				_owner.addDamageReductionByArmor(1);
				break;
			case 7:
				_owner.addDmgRate(1);
				_owner.addBowDmgRate(1);
				_owner.addBaseMagicHitUp(1);
				_owner.addMaxHp(35);
				_owner.addDamageReductionByArmor(1);
				break;
			case 8:
				_owner.addDmgRate(2);
				_owner.addBowDmgRate(2);
				_owner.addBaseMagicHitUp(2);
				_owner.addMaxHp(45);
				_owner.addDamageReductionByArmor(1);
				break;
			case 9:
				_owner.addDmgRate(3);
				_owner.addBowDmgRate(3);
				_owner.addBaseMagicHitUp(3);
				_owner.addMaxHp(55);
				_owner.addDamageReductionByArmor(1);
				break;
			case 10:
				_owner.addDmgRate(4);
				_owner.addBowDmgRate(4);
				_owner.addBaseMagicHitUp(4);
				_owner.addMaxHp(65);
				_owner.addDamageReductionByArmor(1);
				break;
			case 11:
				_owner.addDmgRate(5);
				_owner.addBowDmgRate(5);
				_owner.addBaseMagicHitUp(5);
				_owner.addMaxHp(75);
				_owner.addDamageReductionByArmor(1);
				break;
			case 12:
				_owner.addDmgRate(6);
				_owner.addBowDmgRate(6);
				_owner.addBaseMagicHitUp(6);
				_owner.addMaxHp(85);
				_owner.addDamageReductionByArmor(1);
				break;
			case 13:
				_owner.addDmgRate(7);
				_owner.addBowDmgRate(7);
				_owner.addBaseMagicHitUp(7);
				_owner.addMaxHp(95);
				_owner.addDamageReductionByArmor(1);
				break;
			case 14:
				_owner.addDmgRate(8);
				_owner.addBowDmgRate(8);
				_owner.addBaseMagicHitUp(8);
				_owner.addMaxHp(105);
				_owner.addDamageReductionByArmor(1);
				break;
			case 15:
				_owner.addDmgRate(9);
				_owner.addBowDmgRate(9);
				_owner.addBaseMagicHitUp(9);
				_owner.addMaxHp(115);
				_owner.addDamageReductionByArmor(1);
				break;
			}
		}
		/** 붉은 해의 각반 **/
		if (itemId == 9001118) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addMaxHp(5);
				_owner.addDamageReductionByArmor(1);
				break;
			case 5:
				_owner.addMaxHp(15);
				_owner.addDamageReductionByArmor(1);
				break;
			case 6:
				_owner.addMaxHp(25);
				_owner.addDamageReductionByArmor(1);
				break;
			case 7:
				_owner.addDmgRate(1);
				_owner.addBowDmgRate(1);
				_owner.addBaseMagicHitUp(1);
				_owner.addMaxHp(35);
				_owner.addDamageReductionByArmor(1);
				break;
			case 8:
				_owner.addDmgRate(2);
				_owner.addBowDmgRate(2);
				_owner.addBaseMagicHitUp(2);
				_owner.addMaxHp(45);
				_owner.addDamageReductionByArmor(1);
				break;
			case 9:
				_owner.addDmgRate(3);
				_owner.addBowDmgRate(3);
				_owner.addBaseMagicHitUp(3);
				_owner.addMaxHp(55);
				_owner.addDamageReductionByArmor(1);
				break;
			case 10:
				_owner.addDmgRate(4);
				_owner.addBowDmgRate(4);
				_owner.addBaseMagicHitUp(4);
				_owner.addMaxHp(65);
				_owner.addDamageReductionByArmor(1);
				break;
			case 11:
				_owner.addDmgRate(5);
				_owner.addBowDmgRate(5);
				_owner.addBaseMagicHitUp(5);
				_owner.addMaxHp(75);
				_owner.addDamageReductionByArmor(1);
				break;
			case 12:
				_owner.addDmgRate(6);
				_owner.addBowDmgRate(6);
				_owner.addBaseMagicHitUp(6);
				_owner.addMaxHp(85);
				_owner.addDamageReductionByArmor(1);
				break;
			case 13:
				_owner.addDmgRate(7);
				_owner.addBowDmgRate(7);
				_owner.addBaseMagicHitUp(7);
				_owner.addMaxHp(95);
				_owner.addDamageReductionByArmor(1);
				break;
			case 14:
				_owner.addDmgRate(8);
				_owner.addBowDmgRate(8);
				_owner.addBaseMagicHitUp(8);
				_owner.addMaxHp(105);
				_owner.addDamageReductionByArmor(1);
				break;
			case 15:
				_owner.addDmgRate(9);
				_owner.addBowDmgRate(9);
				_owner.addBaseMagicHitUp(9);
				_owner.addMaxHp(115);
				_owner.addDamageReductionByArmor(1);
				break;
			}
		}
		/** 지휘관의 투구 **/
		if (itemId == 22360) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.ABILITY, 3);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.ABILITY, 4);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ABILITY, 5);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.ABILITY, 6);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.ABILITY, 7);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			}
		}
		/** 지룡의 티셔츠 **/
		if (itemId == 900025) {
			switch (itemlvl) {
			case 0:
				_owner.addDamageReductionByArmor(1);
				break;
			case 1:
				_owner.addDamageReductionByArmor(1);
				break;
			case 2:
				_owner.addDamageReductionByArmor(1);
				break;
			case 3:
				_owner.addDamageReductionByArmor(1);
				break;
			case 4:
				_owner.addDamageReductionByArmor(1);
				break;
			case 5:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(4);
				break;
			case 6:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(5);
				break;
			case 7:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(8);
				break;
			case 8:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(8);
				break;
			case 9:
				_owner.addDamageReductionByArmor(2);
				_owner.getResistance().addMr(11);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDamageReductionByArmor(2);
				_owner.getResistance().addMr(14);
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			}
		}
		/** 화룡의 티셔츠 **/
		if (itemId == 900026) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDmgRate(1);
				break;
			case 5:
				_owner.addDmgRate(1);
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 6:
				_owner.addDmgRate(1);
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			case 7:
				_owner.addDmgRate(1);
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				break;
			case 8:
				_owner.addDmgRate(1);
				_owner.addHitup(2);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 12);
				break;
			case 9:
				_owner.addDmgRate(2);
				_owner.addHitup(4);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 15);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDmgRate(2);
				_owner.addHitup(6);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 18);
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			default:
				break;
			}
		}
		/** 풍룡의 티셔츠 **/
		if (itemId == 900027) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowHitup(1);
				break;
			case 5:
				_owner.addBowHitup(1);
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 6:
				_owner.addBowHitup(1);
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			case 7:
				_owner.addBowDmgRate(1);
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				break;
			case 8:
				_owner.addBowDmgRate(1);
				_owner.addBowHitup(2);// 원거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 12);
				break;
			case 9:
				_owner.addBowDmgRate(2);
				_owner.addBowHitup(4);// 원거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 15);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowDmgRate(2);
				_owner.addBowHitup(6);// 원거리 명중
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				_owner.addSpecialResistance(eKind.ABILITY, 18);
				break;
			default:
				break;
			}
		}
		/** 수룡의 티셔츠 **/
		if (itemId == 900028) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getAbility().addSp(1);
				break;
			case 5:
				_owner.getAbility().addSp(1);
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 6:
				_owner.getAbility().addSp(1);
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			case 7:
				_owner.getAbility().addSp(1);
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				break;
			case 8:
				_owner.getAbility().addSp(2);
				_owner.addBaseMagicHitUp(1);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, 12);
				break;
			case 9:
				_owner.getAbility().addSp(2);
				_owner.addBaseMagicHitUp(3);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, 15);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(3);
				_owner.addBaseMagicHitUp(4);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, 18);
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			default:
				break;
			}
		}
		/** 축복받은 지룡의 티셔츠 **/
		if (itemId == 900184) {
			switch (itemlvl) {
			case 0:
				_owner.addDamageReductionByArmor(1);
				break;
			case 1:
				_owner.addDamageReductionByArmor(1);
				break;
			case 2:
				_owner.addDamageReductionByArmor(1);
				break;
			case 3:
				_owner.addDamageReductionByArmor(1);
				break;
			case 4:
				_owner.addDamageReductionByArmor(1);
				break;
			case 5:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(4);
				break;
			case 6:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(5);
				break;
			case 7:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(8);
				break;
			case 8:
				_owner.addDamageReductionByArmor(1);
				_owner.getResistance().addMr(8);
				break;
			case 9:
				_owner.addDamageReductionByArmor(2);
				_owner.getResistance().addMr(11);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDamageReductionByArmor(2);
				_owner.getResistance().addMr(14);
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			}
		}
		/** 축복받은 화룡의 티셔츠 **/
		if (itemId == 900185) {
			switch (itemlvl) {
			case 5:
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			case 7:
				_owner.addDmgRate(1);
				_owner.addHitup(1);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 8:
				_owner.addDmgRate(1);
				_owner.addHitup(3);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 12);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 9:
				_owner.addDmgRate(2);
				_owner.addHitup(5);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 15);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDmgRate(2);
				_owner.addHitup(7);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, 18);
				_owner.addSpecialResistance(eKind.ALL, 6);
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			default:
				break;
			}
		}
		/** 축복받은 풍룡의 티셔츠 **/
		if (itemId == 900186) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowDmgRate(1);
				break;
			case 5:
				_owner.addBowDmgRate(1);
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 6:
				_owner.addBowDmgRate(1);
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			case 7:
				_owner.addBowDmgRate(1);
				_owner.addBowHitup(1);
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 8:
				_owner.addBowDmgRate(1);
				_owner.addBowHitup(3);
				_owner.addSpecialResistance(eKind.ABILITY, 12);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 9:
				_owner.addBowDmgRate(2);
				_owner.addBowHitup(5);
				_owner.addSpecialResistance(eKind.ABILITY, 15);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowDmgRate(2);
				_owner.addBowHitup(7);
				_owner.addSpecialResistance(eKind.ABILITY, 18);
				_owner.addSpecialResistance(eKind.ALL, 6);
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			default:
				break;
			}
		}
		/** 축복받은 수룡의 티셔츠 **/
		if (itemId == 900187) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getAbility().addSp(1);
				break;
			case 5:
				_owner.getAbility().addSp(1);
				_owner.addSpecialResistance(eKind.ABILITY, 8);
				break;
			case 6:
				_owner.getAbility().addSp(1);
				_owner.addSpecialResistance(eKind.ABILITY, 9);
				break;
			case 7:
				_owner.getAbility().addSp(2);
				_owner.addSpecialResistance(eKind.ABILITY, 10);
				_owner.addSpecialResistance(eKind.ALL, 1);
				break;
			case 8:
				_owner.getAbility().addSp(2);
				_owner.addBaseMagicHitUp(2);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, 12);
				_owner.addSpecialResistance(eKind.ALL, 2);
				break;
			case 9:
				_owner.getAbility().addSp(2);
				_owner.addBaseMagicHitUp(4);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, 15);
				_owner.addSpecialResistance(eKind.ALL, 4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(3);
				_owner.addBaseMagicHitUp(5);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, 18);
				_owner.addSpecialResistance(eKind.ALL, 6);
				_owner.addMaxHp(100);
				_owner.getResistance().addPVPweaponTotalDamage(1);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			default:
				break;
			}
		}
		
		/** 스텟부츠 리뉴얼 **/
		if (itemId == 22359 || itemId == 222308 || itemId ==222309 || itemId ==222307) {
			switch (itemlvl) {
			case 7:
				_owner.addMaxHp(20);
				break;
			case 8:
				_owner.addMaxHp(40);
				break;
			case 9:
				_owner.addMaxHp(60);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			case 10:
				_owner.addMaxHp(60);
				_owner.getResistance().addcalcPcDefense(1);
				break;
			default:
				break;
			}
		}
		/** 고대 투사의 가더 **/
		if (itemId == 22003) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDmgRate(1);
				break;
			case 5:
			case 6:
				_owner.addDmgRate(2);
				break;
			case 7:
			case 8:
				_owner.addDmgRate(3);
				break;
			case 9:
			case 10:
				_owner.addDmgRate(4);
				break;
			default:
				break;
			}
		}
		/** 고대 명궁의 가더 **/
		if (itemId == 22000) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowHitup(1);
				break;
			case 5:
			case 6:
				_owner.addBowHitup(2);
				break;
			case 7:
			case 8:
				_owner.addBowHitup(3);
				break;
			case 9: case 10:
				_owner.addBowHitup(4);
				break;
			default:
				break;
			}
		}
		/** 호박 목걸이 **/
		if (itemId == 900024) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(3);// 마법 적중
				break;
			}
		}
		/** 수령의 귀걸이 **/
		if (itemId == 900114) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(2);
				break;
			}
		}
		/** 수호의 가더 **/
		if (itemId == 22254) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDamageReductionByArmor(1);
				break;
			case 5:
			case 6:
				_owner.addDamageReductionByArmor(2);
				break;
			case 7:
			case 8:
				_owner.addDamageReductionByArmor(3);
				break;
			case 9:
			case 10:
				_owner.addDamageReductionByArmor(4);
				break;
			}
		}
		/** 흑기사의 면갑 대미지감소 **/
		if (itemId == 900038 || itemId == 900054) {
			switch (itemlvl) {
			case 5:
				_owner.getResistance().addMr(4);
				_owner.addDamageReductionByArmor(1);
				break;
			case 6:
				_owner.getResistance().addMr(8);
				_owner.addDamageReductionByArmor(1);
				break;
			case 7:
				_owner.getResistance().addMr(12);
				_owner.addDamageReductionByArmor(1);
				break;
			case 8:
				_owner.getResistance().addMr(16);
				_owner.addDamageReductionByArmor(1);
				break;
			case 9:
				_owner.getResistance().addMr(20);
				_owner.addDamageReductionByArmor(1);
				break;
			case 10:
				_owner.getResistance().addMr(24);
				_owner.addDamageReductionByArmor(1);
				break;
			}
		}
		/** 발라카스의 완력, 예지력 **/
		if (itemId >= 22208 && itemId <= 22209) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 3);
				break;
			case 6:
			//	_owner.getResistance().addcalcPcDefense(3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 4);
				break;
			case 7:
			//	_owner.getResistance().addcalcPcDefense(4);  // 대미지 리덕션 무시
				_owner.add_melee_critical_rate(1);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				break;
			case 8:
			//	_owner.getResistance().addcalcPcDefense(5);
				_owner.add_melee_critical_rate(2);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 6);
				break;
			case 9:
			//	_owner.getResistance().addcalcPcDefense(6);
				_owner.add_melee_critical_rate(3);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			case 10:
			//	_owner.getResistance().addcalcPcDefense(7);
				_owner.add_melee_critical_rate(3);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			}
		}
		
		/** 발라카스의 인내력 **/
		if (itemId == 22210) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 3);
				break;
			case 6:
				//_owner.getResistance().addcalcPcDefense(3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 4);
				break;
			case 7:
				//_owner.getResistance().addcalcPcDefense(4);  // 대미지 리덕션 무시
				_owner.add_missile_critical_rate(1);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				break;
			case 8:
				//_owner.getResistance().addcalcPcDefense(5);
				_owner.add_missile_critical_rate(2);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 6);
				break;
			case 9:
				//_owner.getResistance().addcalcPcDefense(6);
				_owner.add_missile_critical_rate(3);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			case 10:
				//_owner.getResistance().addcalcPcDefense(7);
				_owner.add_missile_critical_rate(3);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			}
		}
		
		/** 발라카스의 마력 **/
		if (itemId == 22211) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 3);
				break;
			case 6:
				//_owner.getResistance().addcalcPcDefense(3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 4);
				break;
			case 7:
				//_owner.getResistance().addcalcPcDefense(4);  // 대미지 리덕션 무시
				_owner.addBaseMagicCritical(1);// 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				break;
			case 8:
				//_owner.getResistance().addcalcPcDefense(5);
				_owner.addBaseMagicCritical(2);// 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 6);
				break;
			case 9:
				//_owner.getResistance().addcalcPcDefense(6);
				_owner.addBaseMagicCritical(3);// 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			case 10:
				//_owner.getResistance().addcalcPcDefense(7);
				_owner.addBaseMagicCritical(3);// 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			}
		}
	
		
		/** 할파스의 완력 **/
		if (itemId == 111137) {
			switch (itemlvl) {
			case 0:
				_owner.addDamageReductionByArmor(7);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 1:
				_owner.addDamageReductionByArmor(9);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 2:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 3:
				_owner.addDamageReductionByArmor(13);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 4:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 5:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 6:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 7:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 8:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 9:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 10:
				_owner.addDamageReductionByArmor(11); 
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			}
		}
		
		/** 할파스의 예지력 **/
		if (itemId == 111141) {
			switch (itemlvl) {
			case 0:
				_owner.addDamageReductionByArmor(7);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 1:
				_owner.addDamageReductionByArmor(9);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 2:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 3:
				_owner.addDamageReductionByArmor(13);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 4:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 5:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 6:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 7:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 8:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 9:
				_owner.addDamageReductionByArmor(11);
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 10:
				_owner.addDamageReductionByArmor(11); 
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			}
		}
		
		/** 할파스의 마력 **/
		if (itemId == 111140) {
			switch (itemlvl) {
			case 0:
		
				_owner.addDamageReductionByArmor(7);
	
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 1:
	
				_owner.addDamageReductionByArmor(9);
		
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 2:
	
				_owner.addDamageReductionByArmor(11);
		
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 3:
	
				_owner.addDamageReductionByArmor(13);
		
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 4:
			
				_owner.addDamageReductionByArmor(11);
			
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 5:

				_owner.addDamageReductionByArmor(11);

				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 6:
	
				_owner.addDamageReductionByArmor(11);
			
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 7:
	
				_owner.addDamageReductionByArmor(11);
		
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 8:
	
				_owner.addDamageReductionByArmor(11);
		
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 9:
		
				_owner.addDamageReductionByArmor(11);
		
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			case 10:

				_owner.addDamageReductionByArmor(11); 
			
				_owner.addSpecialResistance(eKind.ALL, 5);
				break;
			}
		}
		
		/** 용갑옷 착용 멘트 띄우기 **/
		
		/** 안타라스 갑옷 **/
		if (itemId == 22196 || itemId == 22197 || itemId == 22198 || itemId == 22199) {
			_owner.sendPackets(new S_SystemMessage("\\fe안타라스의 분노가 느껴집니다."));
		}

		/** 린드비오르 갑옷 **/
		if (itemId == 22204 || itemId == 22205 || itemId == 22206 || itemId == 22207) {
			_owner.sendPackets(new S_SystemMessage("\\fK린드비오르의 분노가 느껴집니다."));
		}

		/** 파푸리온 갑옷 **/
		if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203) {
			_owner.sendPackets(new S_SystemMessage("\\fB파푸리온의 분노가 느껴집니다."));
		}
		/** 발라카스 갑옷 **/
		if (itemId == 22208 || itemId == 22209 || itemId == 22210 || itemId == 22211) {
			_owner.sendPackets(new S_SystemMessage("\\fA발라카스의 분노가 느껴집니다."));
		}
		/** 할파스 갑옷 **/
		if (itemId == 111137 || itemId == 111141 || itemId == 111140) {
			_owner.sendPackets(new S_SystemMessage("\\fA할파스의 분노가 느껴집니다."));
		}

	
		
	
		/** 신성한 엘름의 축복 **/
		if (itemId == 900035 || itemId == 900072) {
			switch (itemlvl) {
			case 5:
				_owner.getResistance().addMr(4);
				break;
			case 6:
				_owner.getResistance().addMr(8);
				break;
			case 7:
				_owner.getResistance().addMr(12);
				break;
			case 8:
				_owner.getResistance().addMr(16);
				break;
			case 9:
				_owner.getResistance().addMr(20);
				break;
			case 10:
				_owner.getResistance().addMr(24);
				break;
			default:
				break;
			}
		}
		if (itemId == 900039) {
			switch (itemlvl) {
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
				_owner.addHitup(2);
				break;
			default:
				break;
			}
		}
		if (itemId == 900040) {
			switch (itemlvl) {
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
				_owner.addBowHitup(2);
				break;
			default:
				break;
			}
		}
		if (itemId == 900041) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(2);// 마법 적중
				break;
			default:
				break;
			}
		}
		if (itemId == 900036 || itemId == 900073) {
			switch (itemlvl) {
			case 5:
				_owner.addHitup(1);
				break;
			case 6:
				_owner.addHitup(2);
				break;
			case 7:
				_owner.addHitup(3);
				break;
			case 8:
				_owner.addHitup(4);
				break;
			case 9:
				_owner.addHitup(5);
				break;
			case 10:
				_owner.addHitup(5);
				break;
			default:
				break;
			}
		}
		if (itemId == 900037 || itemId == 900074) {
			switch (itemlvl) {
			case 5:
				_owner.addBowHitup(1);
				break;
			case 6:
				_owner.addBowHitup(2);
				break;
			case 7:
				_owner.addBowHitup(3);
				break;
			case 8:
				_owner.addBowHitup(4);
				break;
			case 9:
				_owner.addBowHitup(5);
				break;
			case 10:
				_owner.addBowHitup(5);
				break;
			default:
				break;
			}
		}
		/** 안타라스의 완력/예지력/인내력/마력 **/
		if (itemId >= 22196 && itemId <= 22199) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addDamageReductionByArmor(3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 3);
				break;
			case 6:
				_owner.addDamageReductionByArmor(3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 4);
				break;
			case 7:
				_owner.addDamageReductionByArmor(4);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				break;
			case 8:
				_owner.addDamageReductionByArmor(5);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 6);
				break;
			case 9:
				_owner.addDamageReductionByArmor(6);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			case 10:
				_owner.addDamageReductionByArmor(6);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			default:
				break;
			}
		}
		/** 파푸리온의 완력/예지력/인내력/마력 **/
		if (itemId >= 22200 && itemId <= 22203) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 4);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 6);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			case 10:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			default:
				break;
			}
		}
		/** 린드비오르의 완력/예지력/인내력/마력 **/
		if (itemId >= 22204 && itemId <= 22207) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 4);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 5);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 6);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			case 10:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, 7);
				break;
			default:
				break;
			}
		}
		/** 빛나는 마력의장갑 **/
		if (itemId == 20274) {
			switch (itemlvl) {
			case 5:
				_owner.addWeightReduction(1);
				break;
			case 6:
				_owner.addWeightReduction(2);
				break;
			case 7:
				_owner.addWeightReduction(3);
				break;
			case 8:
				_owner.addWeightReduction(4);
				break;
			case 9:
				_owner.addWeightReduction(5);
				break;
			default:
				break;
			}
		}
		/** 격분의 장갑 **/
		if (itemId == 222317) {
			switch (itemlvl) {
			case 7:
				_owner.addDmgRate(4);
				_owner.addDmgup(1);
				break;
			case 8:
				_owner.addDmgRate(5);
				_owner.addDmgup(2);
				break;
			case 9:
				_owner.addDmgRate(6);
				_owner.addDmgup(3);
				break;
			case 10:
				_owner.addDmgRate(6);
				_owner.addDmgup(3);
				break;
			default:
				break;
			}
		}
		
		/** 빛나는 아덴 용사의 견갑 **/
		if (itemId == 900121) {
			switch (itemlvl) {
			case 6:
				_owner.addDmgup(1);
				_owner.addBowDmgup(1);
				_owner.getAbility().addSp(1);
				_owner.addBowHitup(1);
				_owner.addDmgRate(1);
				_owner.addBaseMagicHitUp(1);// 마법 적중
				break;
			case 7:
				_owner.addDmgup(2);
				_owner.addBowDmgup(2);
				_owner.getAbility().addSp(2);
				_owner.addBowHitup(2);
				_owner.addDmgRate(2);
				_owner.addBaseMagicHitUp(2);// 마법 적중
				_owner.addDamageReductionByArmor(1);//대미지 리덕
				break;
			case 8:
				_owner.addDmgup(3);
				_owner.addBowDmgup(3);
				_owner.getAbility().addSp(3);
				_owner.addBowHitup(3);
				_owner.addDmgRate(3);
				_owner.addBaseMagicHitUp(3);// 마법 적중
				_owner.addDamageReductionByArmor(2);//대미지 리덕
				break;
			case 9:
				_owner.addDmgup(3);
				_owner.addBowDmgup(3);
				_owner.getAbility().addSp(3);
				_owner.addBowHitup(3);
				_owner.addDmgRate(3);
				_owner.addBaseMagicHitUp(3);// 마법 적중
				_owner.addDamageReductionByArmor(3);//대미지 리덕
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDmgup(3);
				_owner.addBowDmgup(3);
				_owner.getAbility().addSp(3);
				_owner.addBowHitup(3);
				_owner.addDmgRate(3);
				_owner.addBaseMagicHitUp(3);// 마법 적중
				_owner.addDamageReductionByArmor(3);//대미지 리덕
				break;
			default:
				break;
			}
		}
		
		/** 에르자베의 왕관 **/
		if (itemId == 21117) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.ABILITY, 5); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, 5); // 공포내성
			case 5:
				_owner.addSpecialResistance(eKind.ABILITY, 6); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, 6); // 공포내성
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ABILITY, 7); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, 7); // 공포내성
				break;
			case 7:
				_owner.addSpecialResistance(eKind.ABILITY, 8); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, 8); // 공포내성
				break;
			case 8:
				_owner.addSpecialResistance(eKind.ABILITY, 9); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, 9); // 공포내성
				break;
			case 9:
				_owner.addSpecialResistance(eKind.ABILITY, 10); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, 10); // 공포내성
				break;
			case 10:
				_owner.addSpecialResistance(eKind.ABILITY, 11); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, 11); // 공포내성
				break;
			default:
				break;
			}
		}
		/** 머미로드의 왕관 **/
		if (itemId == 20017) {
			switch (itemlvl) {
			case 5:
				_owner.addBowDmgup(1);
				_owner.getResistance().addMr(3);//개별 마방 적용
				break;
			case 6:
				_owner.addBowDmgup(2);
				_owner.getResistance().addMr(6);//개별 마방 적용
				break;
			case 7:
				_owner.addBowDmgup(3);
				_owner.getResistance().addMr(9);//개별 마방 적용
				break;
			case 8:
				_owner.addBowDmgup(4);
				_owner.getResistance().addMr(12);//개별 마방 적용
				break;
			case 9:
				_owner.addBowDmgup(5);
				_owner.getResistance().addMr(15);//개별 마방 적용
				break;
			case 10:
				_owner.addBowDmgup(6);
				_owner.getResistance().addMr(18);//개별 마방 적용
				break;
			default:
				break;
			}
		}

		if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203 
				/*|| itemId == 111137 || itemId == 111141 || itemId == 111140*/) {// 파푸리온 및 할파스의 완력, 할파스의 예지력
			_owner.startPapuBlessing();
		}else if(itemId >= 22208 && itemId <= 22211 /*|| itemId == 111140*/) // 발라카스 및 할파스의 마력
			_owner.startValaBlessing();
		
		//TODO (장신구) 목걸이 인챈트 리뉴얼
		if (itemtype == 8) {
			if ((itemgrade >= 0 && itemgrade <= 2) && (itemtype == 8)) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(5);
					break;
				case 2:
					_owner.addMaxHp(10);
					break;
				case 3:
					_owner.addMaxHp(20);
					break;
				case 4:
					_owner.addMaxHp(30);
					break;
				case 5:
					_owner.setAccessoryHeal(1);
					_owner.addMaxHp(40);
					_owner.getResistance().addMr(1);
					break;
				case 6:
					_owner.setAccessoryHeal(2);
					_owner.addMaxHp(40);
					_owner.getResistance().addMr(3);
					break;
				case 7:
					_owner.setAccessoryHeal(3);
					_owner.addMaxHp(50);
					_owner.getResistance().addMr(5);
					_owner.addSpecialResistance(eKind.ABILITY, 2);
					break;
				case 8:
					_owner.setAccessoryHeal(4);
					_owner.addMaxHp(50);
					_owner.getResistance().addMr(7);
					_owner.addSpecialResistance(eKind.ABILITY, 3);
					break;
				case 9:
					_owner.setAccessoryHeal(5);
					_owner.addMaxHp(60);
					_owner.getResistance().addMr(10);
					_owner.addSpecialResistance(eKind.ABILITY, 4);
					break;
				}
			}
		}
//TODO (장신구) 목걸이,귀걸이 인챈트 리뉴얼
if (itemtype >= 8 && itemtype <= 12) {
	if ((itemgrade >= 0 && itemgrade <= 2) && (itemtype == 8 || itemtype == 12)) {
		switch (itemlvl) {
		case 1:
			_owner.addMaxHp(5);
			break;
		case 2:
			_owner.addMaxHp(10);
			break;
		case 3:
			_owner.addMaxHp(20);
			break;
		case 4:
			_owner.addMaxHp(30);
			break;
		case 5:
			_owner.setAccessoryHeal(1);
			_owner.addMaxHp(40);
			break;
		case 6:
			_owner.setAccessoryHeal(2);
			_owner.addMaxHp(40);
			break;
		case 7:
			_owner.setAccessoryHeal(3);
			_owner.addMaxHp(50);
			_owner.addSpecialResistance(eKind.ABILITY, 2);
			break;
		case 8:
			_owner.setAccessoryHeal(4);
			_owner.addMaxHp(50);
			_owner.addSpecialResistance(eKind.ABILITY, 3);
			break;
		case 9:
			_owner.setAccessoryHeal(5);
			_owner.addMaxHp(60);
			_owner.addSpecialResistance(eKind.ABILITY, 4);
			break;
		}
				// TODO (장신구) 반지 인챈트 리뉴얼
			} else
				
				
				if (((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5) && (itemtype == 9 || itemtype == 11)) {
					switch (itemlvl) {
					case 1:
						_owner.addMaxHp(5);
						break;
					case 2:
						_owner.addMaxHp(10);
						break;
					case 3:
						_owner.addMaxHp(20);
						break;
					case 4:
						_owner.addMaxHp(30);
						break;
					case 5:
						_owner.addDmgup(1);
						_owner.addBowDmgup(1);
						_owner.addMaxHp(40);
						break;
					case 6:
						_owner.addDmgup(2);
						_owner.addBowDmgup(2);
						_owner.addMaxHp(40);
						_owner.getResistance().addMr(1);
						_owner.getResistance().addPVPweaponTotalDamage(1);
						break;
					case 7:
						_owner.addDmgup(3);
						_owner.addBowDmgup(3);
						_owner.addMaxHp(50);
						_owner.getAbility().addSp(1);
						_owner.getResistance().addMr(3);
						_owner.getResistance().addPVPweaponTotalDamage(2);
						break;
					case 8:
						_owner.addDmgup(4);
						_owner.addBowDmgup(4);
						_owner.addMaxHp(50);
						_owner.getAbility().addSp(2);
						_owner.getResistance().addMr(5);
						_owner.getResistance().addPVPweaponTotalDamage(3);
						break;
					case 9:
						_owner.addDmgup(5);
						_owner.addBowDmgup(5);
						_owner.addMaxHp(60);
						_owner.getAbility().addSp(3);
						_owner.getResistance().addMr(7);
						_owner.getResistance().addPVPweaponTotalDamage(5);
						break;
					}
					// TODO (장신구) 벨트 인챈트 리뉴얼
				} else
				
				
				if ((itemgrade >= 0 && itemgrade <= 2) && (itemtype == 10)) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxMp(5);
					break;
				case 2:
					_owner.addMaxMp(10);
					break;
				case 3:
					_owner.addMaxMp(20);
					break;
				case 4:
					_owner.addMaxMp(30);
					break;
				case 5:
					_owner.addDamageReductionByArmor(1);
					_owner.addMaxMp(40);
					break;
				case 6:
					_owner.addDamageReductionByArmor(2);
					_owner.addMaxMp(40);
					_owner.addMaxHp(20);
					_owner.getResistance().addcalcPcDefense(1);
					break;
				case 7:
					_owner.addDamageReductionByArmor(3);
					_owner.addMaxMp(50);
					_owner.addMaxHp(30);
					_owner.getResistance().addcalcPcDefense(3);
					break;
				case 8:
					_owner.addDamageReductionByArmor(4);
					_owner.addMaxMp(50);
					_owner.addMaxHp(40);
					_owner.getResistance().addcalcPcDefense(5);
					break;
				case 9:
					_owner.addDamageReductionByArmor(5);
					_owner.addMaxMp(50);
					_owner.addMaxHp(50);
					_owner.getResistance().addcalcPcDefense(7);
					break;
				}
			} else if (itemgrade == 3 && itemId == 22224 || itemId == 22225) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(25);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-3);
					break;
				case 5:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 6:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(2);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
			} else if (itemgrade == 3 && itemId == 22227) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(25);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-3);
					break;
				case 5:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 6:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(2);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
				// 스냅퍼의 마법저항 반지 착용해제
			} else if (itemgrade == 3 && itemId == 22228) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(25);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-3);
					break;
				case 5:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 6:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(2);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
				//TODO 스냅퍼의 체력 반지 리뉴얼
			} else if (itemgrade == 3 && itemId == 22226) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(25);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-3);
					break;
				case 5:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 6:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(2);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					_owner.addDamageReductionByArmor(1);
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					_owner.addDamageReductionByArmor(2);
					break;
				default:
					break;
				}					
			} else if (itemgrade == 3 && itemId == 222290) { // 지혜의 반지
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(5);
					_owner.addMaxMp(15);
					break;
				case 2:
					_owner.addMaxHp(10);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(15);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(20);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-3);
					break;
				case 5:
					_owner.addMaxHp(25);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-3);
					_owner.getAbility().addSp(1);
					break;
				case 6:
					_owner.addMaxHp(30);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-4);
					_owner.getAbility().addSp(2);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(35);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-4);
					_owner.getAbility().addSp(3);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(40);
					_owner.addMaxMp(30);
					_owner.getAC().addAc(-5);
					_owner.getAbility().addSp(4);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
			} else if (itemgrade == 3 && itemId == 222291) { // 용사의 반지
				switch (itemlvl) {
				case 1:
					_owner.getAC().addAc(-1);
					break;
				case 2:
					_owner.getAC().addAc(-2);
					break;
				case 3:
					_owner.addMaxHp(5);
					_owner.getAC().addAc(-3);
					break;
				case 4:
					_owner.addMaxHp(10);
					_owner.getAC().addAc(-4);
					break;
				case 5:
					_owner.addMaxHp(15);
					_owner.getAC().addAc(-4);
					_owner.addHitup(1);
					_owner.addDmgup(1);
					break;
				case 6:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-4);
					_owner.addHitup(2);
					_owner.addDmgup(2);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(25);
					_owner.getAC().addAc(-4);
					_owner.addHitup(3);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-4);
					_owner.addHitup(4);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 체력 반지 **/
			} else if (itemgrade == 3 && itemId == 222332) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 5:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(2);
					break;
				case 6:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					_owner.addDamageReductionByArmor(1);
					break;
				case 7:
					_owner.addMaxHp(55);
					_owner.getAC().addAc(-5);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					_owner.addDamageReductionByArmor(2);
					break;
				case 8:
					_owner.addMaxHp(65);
					_owner.getAC().addAc(-5);
					_owner.addDmgup(5);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					_owner.addDamageReductionByArmor(3);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 회복 반지/축복받은 스냅퍼의 집중 반지 **/
			} else if (itemgrade == 3 && itemId == 222330 || itemId == 222331) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 5:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(2);
					break;
				case 6:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(5);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 마나 반지 **/
			} else if (itemgrade == 3 && itemId == 222333) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 5:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(2);
					break;
				case 6:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(5);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 마법 저항 반지 **/
			} else if (itemgrade == 3 && itemId == 222334) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(35);
					_owner.getAC().addAc(-3);
					_owner.addDmgup(1);
					break;
				case 5:
					_owner.addMaxHp(40);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(2);
					break;
				case 6:
					_owner.addMaxHp(45);
					_owner.getAC().addAc(-4);
					_owner.addDmgup(3);
					_owner.getResistance().addMr(1);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-5);
					_owner.addDmgup(4);
					_owner.getResistance().addMr(2);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.getAC().addAc(-5);
					_owner.addDmgup(5);
					_owner.getResistance().addMr(3);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 지혜 반지 **/
			} else if (itemgrade == 3 && itemId == 222335) {
				switch (itemlvl) {
				case 0:
					_owner.addMaxMp(15);
					break;
				case 1:
					_owner.addMaxHp(5);
					_owner.addMaxMp(15);
					break;
				case 2:
					_owner.addMaxHp(10);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-1);
					break;
				case 3:
					_owner.addMaxHp(20);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-2);
					break;
				case 4:
					_owner.addMaxHp(25);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-3);
					_owner.getAbility().addSp(1);
					break;
				case 5:
					_owner.addMaxHp(30);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-3);
					_owner.getAbility().addSp(2);
					break;
				case 6:
					_owner.addMaxHp(35);
					_owner.addMaxMp(15);
					_owner.getAC().addAc(-3);
					_owner.getAbility().addSp(3);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					_owner.addBaseMagicHitUp(1);// 마법 적중
					break;
				case 7:
					_owner.addMaxHp(40);
					_owner.addMaxMp(30);
					_owner.getAC().addAc(-4);
					_owner.getAbility().addSp(4);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					_owner.addBaseMagicHitUp(2);// 마법 적중
					break;
				case 8:
					_owner.addMaxHp(50);
					_owner.addMaxMp(35);
					_owner.getAC().addAc(-4);
					_owner.getAbility().addSp(5);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					_owner.addBaseMagicHitUp(3);// 마법 적중
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 용사 반지 **/
			} else if (itemgrade == 3 && itemId == 222336) {
				switch (itemlvl) {
				case 1:
					_owner.getAC().addAc(-1);
					break;
				case 2:
					_owner.getAC().addAc(-2);
					break;
				case 3:
					_owner.addMaxHp(10);
					_owner.getAC().addAc(-3);
					break;
				case 4:
					_owner.addMaxHp(15);
					_owner.getAC().addAc(-4);
					_owner.addHitup(1);
					_owner.addDmgup(1);
					break;
				case 5:
					_owner.addMaxHp(20);
					_owner.getAC().addAc(-4);
					_owner.addHitup(2);
					_owner.addDmgup(2);
					break;
				case 6:
					_owner.addMaxHp(25);
					_owner.getAC().addAc(-4);
					_owner.addHitup(3);
					_owner.addDmgup(3);
					_owner.addSpecialResistance(eKind.ABILITY, 5);
					break;
				case 7:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-5);
					_owner.addHitup(4);
					_owner.addDmgup(4);
					_owner.addSpecialResistance(eKind.ABILITY, 7);
					_owner.getResistance().addPVPweaponTotalDamage(1);
					break;
				case 8:
					_owner.addMaxHp(30);
					_owner.getAC().addAc(-5);
					_owner.addHitup(5);
					_owner.addDmgup(5);
					_owner.addSpecialResistance(eKind.ABILITY, 9);
					_owner.getResistance().addPVPweaponTotalDamage(2);
					break;
				default:
					break;
				}
			} else if(itemId == 22229){
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(20);
					break;
				case 2:
					_owner.addMaxHp(30);
					break;
				case 3:
					_owner.addMaxHp(40);
					_owner.addDamageReductionByArmor(1);
					break;
				case 4:
					_owner.addMaxHp(50);
					_owner.addDamageReductionByArmor(1);
					break;
				case 5:
					_owner.addMaxHp(60);
					_owner.addDamageReductionByArmor(2);
					break;
				case 6:
					_owner.addMaxHp(70);
					_owner.addDamageReductionByArmor(3);
					_owner.getAC().addAc(-7);
					break;
				case 7:
					_owner.addBowHitup(1);
					_owner.addHitup(1);
					_owner.addMaxHp(80);
					_owner.addDamageReductionByArmor(4);
					_owner.getAC().addAc(-8);
					break;
				case 8:
					_owner.addBowHitup(3);
					_owner.addHitup(3);
					_owner.addMaxHp(90);
					_owner.addDamageReductionByArmor(5);
					_owner.getAC().addAc(-9);
					break;
				default:
					break;
				}
			} else if(itemId == 222337){
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(20);
					break;
				case 2:
					_owner.addMaxHp(30);
					break;
				case 3:
					_owner.addMaxHp(50);
					_owner.addDamageReductionByArmor(1);
					break;
				case 4:
					_owner.addMaxHp(60);
					_owner.addDamageReductionByArmor(2);
					break;
				case 5:
					_owner.addMaxHp(70);
					_owner.addDamageReductionByArmor(3);
					_owner.getAC().addAc(-7);
					break;
				case 6:
					_owner.addBowHitup(1);
					_owner.addHitup(1);
					_owner.addMaxHp(80);
					_owner.addDamageReductionByArmor(4);
					_owner.getAC().addAc(-8);
					break;
				case 7:
					_owner.addBowHitup(3);
					_owner.addHitup(3);
					_owner.addMaxHp(90);
					_owner.addDamageReductionByArmor(5);
					_owner.getAC().addAc(-9);
					break;
				case 8:
					_owner.addBowHitup(5);
					_owner.addHitup(5);
					_owner.addMaxHp(150);
					_owner.addDamageReductionByArmor(6);
					_owner.getAC().addAc(-10);
					break;
				default:
					break;
				}
			} else if(itemId == 22231){
				switch (itemlvl) {
				case 1:
					_owner.addMaxMp(10);
					_owner.getResistance().addMr(3);
					break;
				case 2:
					_owner.addMaxMp(15);
					_owner.getResistance().addMr(4);
					break;
				case 3:
					_owner.addMaxMp(30);
					_owner.getResistance().addMr(5);
					_owner.getAbility().addSp(1);
					break;
				case 4:
					_owner.addMaxMp(35);
					_owner.getResistance().addMr(6);
					_owner.getAbility().addSp(1);
					break;
				case 5:
					_owner.addMaxMp(50);
					_owner.getResistance().addMr(7);
					_owner.getAbility().addSp(2);
					break;
				case 6:
					_owner.addMaxMp(55);
					_owner.getResistance().addMr(8);
					_owner.getAbility().addSp(2);
					_owner.getAC().addAc(-1);
					break;
				case 7:
					_owner.addBaseMagicHitUp(1);// 마법 적중
					_owner.addMaxMp(70);
					_owner.getResistance().addMr(10);
					_owner.getAbility().addSp(3);
					_owner.getAC().addAc(-2);
					break;
				case 8:
					_owner.addBaseMagicHitUp(3);// 마법 적중
					_owner.addMaxMp(95);
					_owner.getResistance().addMr(13);
					_owner.getAbility().addSp(3);
					_owner.getAC().addAc(-3);
					break;
				default:
					break;
				}
			} else if(itemId == 222339){
				switch (itemlvl) {
				case 0:
					_owner.addMaxMp(10);
					_owner.getResistance().addMr(2);
					break;
				case 1:
					_owner.addMaxMp(10);
					_owner.getResistance().addMr(3);
					break;
				case 2:
					_owner.addMaxMp(15);
					_owner.getResistance().addMr(4);
					break;
				case 3:
					_owner.addMaxMp(35);
					_owner.getResistance().addMr(6);
					_owner.getAbility().addSp(1);
					break;
				case 4:
					_owner.addMaxMp(50);
					_owner.getResistance().addMr(7);
					_owner.getAbility().addSp(2);
					break;
				case 5:
					_owner.addMaxMp(55);
					_owner.getResistance().addMr(8);
					_owner.getAbility().addSp(2);
					_owner.getAC().addAc(-1);
					break;
				case 6:
					_owner.addBaseMagicHitUp(1);// 마법 적중
					_owner.addMaxMp(70);
					_owner.getResistance().addMr(10);
					_owner.getAbility().addSp(3);
					_owner.getAC().addAc(-2);
					break;
				case 7:
					_owner.addBaseMagicHitUp(3);// 마법 적중
					_owner.addMaxMp(95);
					_owner.getResistance().addMr(13);
					_owner.getAbility().addSp(3);
					_owner.getAC().addAc(-3);
					break;
				case 8:
					_owner.addBaseMagicHitUp(5);// 마법 적중
					_owner.addMaxMp(125);
					_owner.getResistance().addMr(18);
					_owner.getAbility().addSp(4);
					_owner.getAC().addAc(-4);
					break;
				default:
					break;
				}
				
				/** 검귀 ac처리부분 재수정 **/
			} else if (itemId == 222340 || itemId == 222341) {
				int ac = itemlvl;

				if (item.getBless() == 0 && itemlvl >= 3) {
					ac += 1;
				}
				_owner.getAC().addAc(-ac);
				int dm = itemlvl - 2;

				if (item.getBless() != 0 && itemlvl >= 4)
					dm -= 1;

				_owner.addDmgup(dm);
				_owner.addBowDmgup(dm);
				
			} else if (itemgrade == 4 && itemId == 22230) { // 룸티스의 푸른빛 귀걸이
				switch (itemlvl) {
				case 5:
					_owner.getAC().addAc(-1);
					break;
				case 6:
					_owner.getAC().addAc(-2);
					break;
				case 7:
					_owner.getAC().addAc(-2);
					break;
				case 8:
					_owner.getAC().addAc(-3);
					break;
				default:
					break;
				}
			} else if (itemgrade == 4 && itemId == 222338) { // 축복받은 룸티스의 푸른빛
				switch (itemlvl) {
				case 4:
					_owner.getAC().addAc(-1);
					break;
				case 5:
					_owner.getAC().addAc(-2);
					break;
				case 6:
					_owner.getAC().addAc(-2);
					break;
				case 7:
					_owner.getAC().addAc(-3);
					break;
				case 8:
					_owner.getAC().addAc(-4);
					break;
				default:
					break;
				}
			}
		}
		armor.startEquipmentTimer(_owner);
	}

	public ArrayList<L1ItemInstance> getArmors() {
		return _armors;
	}

	private void removeWeapon(L1ItemInstance weapon) {
		_owner.setWeapon(null);
		_owner.setCurrentWeapon(0);

		int itemId = weapon.getItem().getItemId();
		int enchant = weapon.getEnchantLevel();
		if (itemId == 1134 || itemId == 101134) { // 명상의 지팡이
			if (enchant > 0) {
				_owner.addMpr(-enchant);
			}
		}
		/** 칠흑의 수정구 **/
		if (itemId == 118) {
			switch (enchant) {
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addCha(-1);
				break;
			default:
				break;
			}
		}
		
		
		/** 아인하사드의 섬광 **/
		if (itemId == 2944) {
			switch (enchant) {
			case 0:
				_owner.add_melee_critical_rate(-7);// 근거리 치명타
				_owner.addSpecialPierce(eKind.ABILITY, -12); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, -12); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -12); // 공포적중
				break;
			case 1:
				_owner.add_melee_critical_rate(-9);// 근거리 치명타
				_owner.addSpecialPierce(eKind.ABILITY, -13);
				_owner.addSpecialPierce(eKind.SPIRIT, -13); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -13); // 공포적중
				break;
			case 2:
				_owner.add_melee_critical_rate(-11);// 근거리 치명타
				_owner.addSpecialPierce(eKind.ABILITY, -14);
				_owner.addSpecialPierce(eKind.SPIRIT, -14); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -14); // 공포적중
				break;
			case 3:
				_owner.add_melee_critical_rate(-13);// 근거리 치명타
				_owner.addSpecialPierce(eKind.ABILITY, -15);
				_owner.addSpecialPierce(eKind.SPIRIT, -15); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -15); // 공포적중
				break;
			case 4:
				_owner.add_melee_critical_rate(-15);// 근거리 치명타
				_owner.addSpecialPierce(eKind.ABILITY, -16);
				_owner.addSpecialPierce(eKind.SPIRIT, -16); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -16); // 공포적중
				break;
			case 5:
				_owner.add_melee_critical_rate(-17);// 근거리 치명타
				_owner.addSpecialPierce(eKind.ABILITY, -17);
				_owner.addSpecialPierce(eKind.SPIRIT, -17); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -17); // 공포적중
				break;
			case 6:
				_owner.add_melee_critical_rate(-19);// 근거리 치명타
				_owner.addSpecialPierce(eKind.ABILITY, -18);
				_owner.addSpecialPierce(eKind.SPIRIT, -18); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -18); // 공포적중
				break;
			case 7:
				_owner.add_melee_critical_rate(-21);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-27); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -19);
				_owner.addSpecialPierce(eKind.SPIRIT, -19); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -19); // 공포적중
				break;
			case 8:
				_owner.add_melee_critical_rate(-23);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-28); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -20);
				_owner.addSpecialPierce(eKind.SPIRIT, -20); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -20); // 공포적중
				break;
			case 9:
				_owner.add_melee_critical_rate(-25);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-29); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -21);
				_owner.addSpecialPierce(eKind.SPIRIT, -21); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -21); // 공포적중
				break;
			case 10:
				_owner.add_melee_critical_rate(-27);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-30); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -22);
				_owner.addSpecialPierce(eKind.SPIRIT, -22); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -22); // 공포적중
				break;
			}
		}
		
		/** 그랑카인의 심판 **/
		if (itemId == 2945) {
			switch (enchant) {
			case 0:
				_owner.add_melee_critical_rate(-7);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-20); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -15); // 기술적중
				break;
			case 1:
				_owner.add_melee_critical_rate(-9);
			//	_owner.getResistance().addcalcPcDefense(-21); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -16); // 기술적중
				break;
			case 2:
				_owner.add_melee_critical_rate(-11);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-22); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -17); // 기술적중
				break;
			case 3:
				_owner.add_melee_critical_rate(-13);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-23); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -18); // 기술적중
				break;
			case 4:
				_owner.add_melee_critical_rate(-15);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-24); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -19); // 기술적중
				break;
			case 5:
				_owner.add_melee_critical_rate(-17);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-25); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -20); // 기술적중
				break;
			case 6:
				_owner.add_melee_critical_rate(-19);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-26); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -21); // 기술적중
				break;
			case 7:
				_owner.add_melee_critical_rate(-21);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-27); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -22); // 기술적중
				break;
			case 8:
				_owner.add_melee_critical_rate(-23);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-28); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -23); // 기술적중
				break;
			case 9:
				_owner.add_melee_critical_rate(-25);// 근거리 치명타
			//	_owner.getResistance().addcalcPcDefense(-29); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -24); // 기술적중
				break;
			case 10:
				_owner.add_melee_critical_rate(-27);// 근거리 치명타
				_owner.getResistance().addcalcPcDefense(-30); // 대미지 리덕션 무시
				_owner.addSpecialPierce(eKind.ABILITY, -25); // 기술적중
				break;
			}
		}
		
		/** 축복받은 제로스의 지팡이 **/
		if (itemId == 620) {
			switch (enchant) {
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
				_owner.addBaseMagicCritical(-5);// 마법 치명타
				break;
			}
		}
		/** 살기의 키링크 **/
		if (itemId == 7000223 || itemId == 505015) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.addBaseMagicCritical(-1);// 마법 치명타
				break;
			case 7:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -1);
				_owner.addBaseMagicCritical(-2);// 마법 치명타
				break;
			case 8:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -2);
				_owner.addBaseMagicCritical(-3);// 마법 치명타
				break;
			case 9:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -3);
				_owner.addBaseMagicCritical(-4);// 마법 치명타
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -4);
				_owner.addBaseMagicCritical(-5);// 마법 치명타
				break;
			}
		}
		/** 바포메트의 지팡이 SP **/
		if (itemId == 124) {
			switch (enchant) {
			case 7:
				_owner.getAbility().addSp(-1);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 8:
				_owner.getAbility().addSp(-2);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(-3);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			}
		}
		if (itemId == 541) {
			switch (enchant) {
			case 0:
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				break;
			}
		}
		if (itemId == 542) {
			switch (enchant) {
			case 0:
				_owner.addDamageReductionByArmor(-2);
				break;
			}
		}
		if (itemId == 543) {
			switch (enchant) {
			case 0:
				_owner.addHitup(-2);
				break;
			}
		}
		if (itemId == 544) {
			switch (enchant) {
			case 0:
				_owner.addHitup(-10);
				break;
			}
		}
		if (itemId == 545) {
			switch (enchant) {
			case 0:
				_owner.addHitup(-17);
				break;
			}
		}
		if (itemId == 547) {
			switch (enchant) {
			case 0:
				_owner.addHitup(-11);
				break;
			}
		}

		/** 드래곤슬레이어 **/
		if (itemId == 66) {
			_owner.addDmgup(-(enchant * 2));
		}
		/** 나이트발드의 양손검 기술적중 **/
		if (itemId == 59 || itemId == 617) {
			_owner.addSpecialPierce(eKind.ABILITY, -5);
		}
		/** 태풍의도끼 공포적중+1 **/
		if (itemId == 203006 || itemId == 616) {
			switch (enchant) {
			case 8:
				_owner.addSpecialPierce(eKind.FEAR, -1);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.FEAR, -2);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.FEAR, -3);
				break;
			}
		}
		/** 안타라스의 도끼 **/
		if (itemId == 7000016) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.FEAR, -2);
				break;
			case 8:
				_owner.addSpecialPierce(eKind.FEAR, -3);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.FEAR, -4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.FEAR, -5);
				break;
			}
		}
		
		/** 안타라스의 지팡이 **/
		if (itemId == 7000017) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addBaseMagicHitUp(-1);// 마법 적중 적용
				break;
			case 8:
				_owner.addBaseMagicHitUp(-2);
				break;
			case 9:
				_owner.addBaseMagicHitUp(-3);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBaseMagicHitUp(-4);
				break;
			}
		}
		
		/** 파푸리온의 장궁 **/
		if (itemId == 7000018) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.SPIRIT, -1); // 정령적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.SPIRIT, -2); // 정령적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.SPIRIT, -3); // 정령적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.SPIRIT, -4); // 정령적중
				break;
			}
		}
		
		/** 파푸리온의 이도류 **/
		if (itemId == 7000019) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.SPIRIT, -2); // 정령적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.SPIRIT, -3); // 정령적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.SPIRIT, -4); // 정령적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.SPIRIT, -5); // 정령적중
				break;
			}
		}
		
		/** 린드비오르의 체인소드 **/
		if (itemId == 7000020) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -5); // 용언적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -6); // 용언적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -7); // 용언적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -8); // 용언적중
				break;
			}
		}
		
		/** 린드비오르의 키링크 **/
		if (itemId == 7000021) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.getAbility().addSp(-1);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 8:
				_owner.getAbility().addSp(-2);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 9:
				_owner.getAbility().addSp(-3);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(-4);
				_owner.sendPackets(new S_SPMR(_owner));
				break;
			}
		}
		
		/** 발라카스의 장검 **/
		if (itemId == 7000022) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.ABILITY, -2); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, -2); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -2); // 공포적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.ABILITY, -3); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, -3); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -3); // 공포적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.ABILITY, -4); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, -4); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -4); // 공포적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.ABILITY, -5); // 기술적중
				_owner.addSpecialPierce(eKind.SPIRIT, -5); // 정령적중
				_owner.addSpecialPierce(eKind.FEAR, -5); // 공포적중
				break;
			}
		}
		
		/** 발라카스의 양손검 **/
		if (itemId == 7000023) {
			switch (enchant) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				_owner.addSpecialPierce(eKind.ABILITY, -5); // 기술적중
				break;
			case 8:
				_owner.addSpecialPierce(eKind.ABILITY, -6); // 기술적중
				break;
			case 9:
				_owner.addSpecialPierce(eKind.ABILITY, -7); // 기술적중
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.ABILITY, -8); // 기술적중
				break;
			}
		}
		/** 섬멸자의 체인소드 적중 **/
		if (itemId == 203017 || itemId == 618) {
			switch (enchant) {
			case 7:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -1);
				break;
			case 8:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -2);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -3);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialPierce(eKind.DRAGON_SPELL, -4);
				break;
			}
		}
		/** 썸타는 도끼 공포적중+5 **/
		if (itemId == 7000221) 
			_owner.addSpecialPierce(eKind.FEAR, -5);
		
		/** 썸타는 무기류 PVP 효과 부여 **/
		if (itemId >= 7000214 || itemId <= 7000221) {
			if(enchant <= 7)
				_owner.getResistance().addPVPweaponTotalDamage(-3);
			else if(enchant == 8)
				_owner.getResistance().addPVPweaponTotalDamage(-5);
			else if(enchant == 9)
				_owner.getResistance().addPVPweaponTotalDamage(-7);
			else if(enchant >= 10)
				_owner.getResistance().addPVPweaponTotalDamage(-10);
		}

		weapon.stopEquipmentTimer(_owner);
		_weapons.remove(weapon);
		
		if (weapon.getEnchantMagic() != 0) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 0, weapon.getEnchantMagic(), _weapons.size()));
		}
		
		if (_owner.hasSkillEffect(L1SkillId.INFERNO)) {
			_owner.removeSkillEffect(L1SkillId.INFERNO);
		}
		
		if (_owner.hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
			_owner.removeSkillEffect(L1SkillId.COUNTER_BARRIER);
		}
		if (_weapons.size() == 1)
			_owner.setCurrentWeapon(getWeapon().getItem().getType1());
		else
			_owner.setCurrentWeapon(0);

		if (itemId == 203003) { // 데스나이트의 불검:진
			L1PolyMorph.undoPoly(_owner);
		}

		int type = weapon.getItem().getType();
		if ((type == 7 || type == 16 || type == 17) && (weapon.get_bless_level() != 0)) {
			_owner.getAbility().addSp(-weapon.get_bless_level());
			_owner.sendPackets(new S_SPMR(_owner));
		}
	}

	private void removeArmor(L1ItemInstance armor) {
		L1Item item = armor.getItem();
		int itemId = armor.getItem().getItemId();
		int itemlvl = armor.getEnchantLevel();
		int itemtype = armor.getItem().getType();
		int itemgrade = armor.getItem().getGrade();
		int constat = armor.getItem().get_addcon();
		int wisstat = armor.getItem().get_addwis();
		
		if (constat > 0) {
			_owner.addMaxHp(-((_owner.getAbility().getCon() / 2) * constat));
		}
		
		if (wisstat > 0) {
			_owner.addMaxMp(-((_owner.getAbility().getWis() / 2) * wisstat));
		}
		
		if(itemId == 900111){
			_owner.removeSkillEffect(L1SkillId.TELEPORT_RULER);
		}
		
		if(itemId == 22228){
			if(itemlvl >= 8){
				_owner.addMagicDodgeProbability(-3);
			}else if(itemlvl >= 7){
				_owner.addMagicDodgeProbability(-1);					
			}
		}else if(itemId == 222334){
			if(itemlvl >= 8){
				_owner.addMagicDodgeProbability(-5);
			}else if(itemlvl >= 7){
				_owner.addMagicDodgeProbability(-3);
			}else if(itemlvl >= 6){
				_owner.addMagicDodgeProbability(-1);
			}
		}
		
		if (itemtype >= 8 && itemtype <= 12 && !isRoomteece(itemId)) {
			if ((itemtype == 8 || itemtype == 12) && itemlvl >= 5) {
				int enchantAc = itemlvl - 4;
				if (enchantAc >= 5) {
					enchantAc = 5;
				}
				_owner.getAC().addAc(-(item.get_ac() - armor.getAcByMagic() - enchantAc + armor.get_durability()));
				_owner.sendPackets(new S_OwnCharAttrDef(_owner));
			} else {
				_owner.getAC().addAc(-(item.get_ac() - armor.getAcByMagic() + armor.get_durability()));
				_owner.sendPackets(new S_OwnCharAttrDef(_owner));
			}
		} else if(itemtype == 30){	// 휘장
		} else if (itemId == 900118 || itemId == 900117 || itemId == 900119 || itemId == 900120){
			int enchant = armor.getEnchantLevel();
			int enchant_ac = enchant >= 1 ? enchant : 0;
			_owner.getAC().addAc(-(item.get_ac() - enchant_ac - armor.getAcByMagic() + armor.get_durability()));
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		} else if(!isRoomteece(itemId)){
			_owner.getAC().addAc(-(item.get_ac() - armor.getEnchantLevel() - armor.getAcByMagic() + armor.get_durability()));
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		} else{
			_owner.getAC().addAc(-item.get_ac());
		}
		/** 리치 로브 인챈당 sp증가 **/
		if (itemId == 20107) {
			if (itemlvl >= 3) {
				_owner.getAbility().addSp(-(itemlvl - 2));
			}
		}
		// -- remove --
		if (armor.getItem().get_regist_calcPcDefense() != 0) {
			_owner.set_pvp_defense(_owner.get_pvp_defense() - armor.getItem().get_regist_calcPcDefense());
		}

		_owner.addDamageReductionByArmor(-item.get_damage_reduction());
		_owner.addWeightReduction(-item.getWeightReduction());
		_owner.addBowHitRate(-item.getBowHitRate());
		_owner.getResistance().addEarth(-item.get_defense_earth());
		_owner.getResistance().addWind(-item.get_defense_wind());
		_owner.getResistance().addWater(-item.get_defense_water());
		_owner.getResistance().addFire(-item.get_defense_fire());
		item.equipmentItem(_owner, false);
		_owner.getResistance().addcalcPcDefense(-item.get_regist_calcPcDefense());
		_owner.getResistance().addPVPweaponTotalDamage(-item.get_regist_PVPweaponTotalDamage());
		_owner.addBaseMagicHitUp(-item.getMagicHitup());

		if (armor.hasSkillEffectTimer(L1SkillId.BLESSED_ARMOR)) {
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.BLESSED_ARMOR);
			noti.set_duration(0);
			noti.set_end_str_id(2240);
			noti.set_is_good(true);
			_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}

		// 셋트아이템 메소드로 변경..
		removeSetItems(itemId);

		if (itemId == 111137 || itemId == 111140 || itemId == 111141) {
			_owner.get_skill().setHalpas(null);
		}
		
		if (itemId == 900022) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, false));
		}
		
		if (itemId == 423014) {
			_owner.stopAHRegeneration();
		}
		if (itemId == 423015) {
			_owner.stopSHRegeneration();
		}
		if (itemId == 20380) {
			_owner.stopHalloweenRegeneration();
		}
		if (itemId == 20077 || itemId == 20062 || itemId == 120077) {
			_owner.delInvis();
		}
		if (itemId == 20288 || itemId == 900111) {
			_owner.sendPackets(new S_Ability(1, false));
			L1ItemInstance teleport_item = _owner.getInventory().findItemId(40100);
			if(teleport_item != null){
				_owner.sendPackets(new S_DeleteInventoryItem(teleport_item));
				SC_ADD_INVENTORY_NOTI noti = SC_ADD_INVENTORY_NOTI.newInstance();
				noti.add_item_info(ItemInfo.newInstance(teleport_item, 0));
				_owner.sendPackets(noti, MJEProtoMessages.SC_ADD_INVENTORY_NOTI, true);
			}
		}
		if (itemId == 20281) {
			_owner.sendPackets(new S_Ability(2, false));
		}
		if (itemId == 900075) {
			_owner.sendPackets(new S_Ability(7, false));
			_owner.sendPackets(new S_Ability(2, false));
			_owner.setPolyRingMaster(false);
		}
		if (itemId == 20036) {
			_owner.sendPackets(new S_Ability(3, false));
		}
		if (itemId == 20284) {
			_owner.sendPackets(new S_Ability(5, false));
		}
		if (itemId == 20207) {
			_owner.sendPackets(new S_SkillIconBlessOfEva(_owner.getId(), 0));
		}
		if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203
				/*|| itemId == 111137 || itemId == 111141 || itemId == 111140*/) {// 파푸리온 및 할파스의 완력, 할파스의 예지력
			_owner.stopPapuBlessing();
		}else if(itemId >= 22208 && itemId <= 22211 /*|| itemId == 111140*/) // 발라카스 및 할파스의 마력
			_owner.stopValaBlessing();

		/*** 50레벨 엘릭서 룬 ***/
		// 민첩의 엘릭서 룬
		if (itemId == 222295) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(-50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 222296) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(-50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 222297) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(-50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.addMaxMp(-30);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 222298) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(-50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				break;
			}
		}
		// 힘의 엘릭서 룬
		if (itemId == 222299) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				break;
			// 기사//전사
			case 7:
			case 1:
				_owner.addMaxHp(-50);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				break;
			}
		}
		//TODO 70레벨 엘릭서 룬
		// 민첩의 엘릭서 룬
		if (itemId == 222312) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 222313) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 222314) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 222315) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 힘의 엘릭서 룬
		if (itemId == 222316) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// TODO 80레벨 엘릭서 룬
		// 힘의 엘릭서 룬
		if (itemId == 900135) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 민첩의 엘릭서 룬
		if (itemId == 900136) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 900137) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 900138) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 900139) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// TODO 85레벨 엘릭서 룬
		// 힘의 엘릭서 룬
		if (itemId == 900140) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 민첩의 엘릭서 룬
		if (itemId == 900141) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 900142) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 900143) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 900144) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// TODO 90레벨 엘릭서 룬
		// 힘의 엘릭서 룬
		if (itemId == 900145) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 민첩의 엘릭서 룬
		if (itemId == 900146) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 체력의 엘릭서 룬
		if (itemId == 900147) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지식의 엘릭서 룬
		if (itemId == 900148) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		// 지혜의 엘릭서 룬
		if (itemId == 900149) {
			switch (_owner.getType()) {
			// 군주
			case 0:
				_owner.addDamageReductionByArmor(-3);
				_owner.addDmgRate(-2);
				break;
			// 기사
			case 1:
				_owner.addMaxHp(-50);
				_owner.addDmgRate(-1);
				break;
			// 요정
			case 2:
				_owner.addMaxMp(-50);
				_owner.addBowDmgRate(-1);
				_owner.addDmgRate(-1);
				break;
			// 법사
			case 3:
				_owner.addMpr(-3);
				_owner.getAbility().addSp(-1);
				break;
			// 다엘
			case 4:
				_owner.getAC().addAc(3);
				_owner.addMaxMp(-30);
				break;
			// 용기사
			case 5:
				_owner.addHitup(-3);
				_owner.addDamageReductionByArmor(-1);
				break;
			// 환술사
			case 6:
				_owner.addWeightReduction(-5);
				_owner.addMaxHp(-50);
				break;
			// 전사
			case 7:
				_owner.getResistance().addMr(-5);
				_owner.addMaxHp(-50);
				break;
			}
		}
		
		
		// 80레벨 엘릭서룬 PVP마법 대미지 감소
		if (itemId >= 900135 && itemId <= 900139) {
			_owner.addPVPMagicDamageReduction(-1);
		}
		// 85레벨 엘릭서룬 PVP마법 대미지 감소
		if (itemId >= 900140 && itemId <= 900144) {
			_owner.addPVPMagicDamageReduction(-2);
		}
		// 90레벨 엘릭서룬 PVP마법 대미지 감소
		if (itemId >= 900145 && itemId <= 900149) {
			_owner.addPVPMagicDamageReduction(-3);
		}
		
		/** 화령의 가더 **/
		if (itemId == 900117) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDmgRate(-1);
				break;
			case 5:
				_owner.addDmgRate(-1);
				_owner.addHitup(-1);
				break;
			case 6:
				_owner.addDmgRate(-1);
				_owner.addHitup(-2);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 7:
				_owner.addDmgRate(-2);
				_owner.addHitup(-3);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 8:
				_owner.addDmgRate(-3);
				_owner.addHitup(-4);
				_owner.addSpecialResistance(eKind.ALL, -3);
				break;
			case 9:
				_owner.addDmgRate(-4);
				_owner.addHitup(-5);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 10:
				_owner.addDmgRate(-5);
				_owner.addHitup(-6);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			default:
				break;
			}
		}
		/** 풍령의 가더 **/
		if (itemId == 900118) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowDmgup(-1);
				break;
			case 5:
				_owner.addBowDmgup(-1);
				_owner.addBowHitup(-1);
				break;
			case 6:
				_owner.addBowDmgup(-1);
				_owner.addBowHitup(-2);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 7:
				_owner.addBowDmgup(-2);
				_owner.addBowHitup(-3);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 8:
				_owner.addBowDmgup(-3);
				_owner.addBowHitup(-4);
				_owner.addSpecialResistance(eKind.ALL, -3);
				break;
			case 9:
				_owner.addBowDmgup(-4);
				_owner.addBowHitup(-5);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 10:
				_owner.addBowDmgup(-5);
				_owner.addBowHitup(-6);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			default:
				break;
			}
		}
		/** 수령의 가더 **/
		if (itemId == 900119) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getAbility().addSp(-1);
				break;
			case 5:
				_owner.getAbility().addSp(-1);
				_owner.addBaseMagicHitUp(-1);
				break;
			case 6:
				_owner.getAbility().addSp(-1);
				_owner.addBaseMagicHitUp(-2);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 7:
				_owner.getAbility().addSp(-2);
				_owner.addBaseMagicHitUp(-3);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 8:
				_owner.getAbility().addSp(-3);
				_owner.addBaseMagicHitUp(-4);
				_owner.addSpecialResistance(eKind.ALL, -3);
				break;
			case 9:
				_owner.getAbility().addSp(-4);
				_owner.addBaseMagicHitUp(-5);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 10:
				_owner.getAbility().addSp(-5);
				_owner.addBaseMagicHitUp(-6);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			default:
				break;
			}
		}
		/** 에바의 방패 **/
		if (itemId == 20235) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -2);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -3);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -4);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -6);
				break;
			case 10:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			default:
				break;
			}
		}
		/** 시어의 심안 **/
		if (itemId == 22214) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.SPIRIT, -2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, -3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, -4);
				break;
			case 7:
				_owner.addBaseMagicHitUp(-1);// 마법 적중 적용
				_owner.addSpecialResistance(eKind.SPIRIT, -5);
				break;
			case 8:
				_owner.addBaseMagicHitUp(-2);
				_owner.addSpecialResistance(eKind.SPIRIT, -6);
				break;
			case 9:
				_owner.addBaseMagicHitUp(-3);
				_owner.addSpecialResistance(eKind.SPIRIT, -7);
				break;
			case 10:
				_owner.addBaseMagicHitUp(-4);
				_owner.addSpecialResistance(eKind.SPIRIT, -8);
				break;
			default:
				break;
			}
		}
		
		/** 리치의 수정구 **/
		if (itemId == 900165) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				
				_owner.addSpecialResistance(eKind.SPIRIT, -2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, -3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, -4);
				break;
			case 7:
				_owner.getAbility().addSp(-1);
				_owner.addBaseMagicHitUp(-1);// 마법 적중 적용
				_owner.addSpecialResistance(eKind.SPIRIT, -5);
				break;
			case 8:
				_owner.getAbility().addSp(-2);
				_owner.addBaseMagicHitUp(-2);
				_owner.addSpecialResistance(eKind.SPIRIT, -6);
				break;
			case 9:
				_owner.getAbility().addSp(-3);
				_owner.addBaseMagicHitUp(-3);
				_owner.addSpecialResistance(eKind.SPIRIT, -7);
				break;
			case 10:
				_owner.getAbility().addSp(-4);
				_owner.addBaseMagicHitUp(-4);
				_owner.addSpecialResistance(eKind.SPIRIT, -8);
				break;
			default:
				break;
			}
		}
		/** 반역자의 방패 **/
		if (itemId == 22263) {
			switch (itemlvl) {
			case 5:
				_owner.addMaxHp(-20);
				_owner.addSpecialPierce(eKind.ABILITY, -1);
				break;
			case 6:
				_owner.addMaxHp(-40);
				_owner.addSpecialPierce(eKind.ABILITY, -2);
				break;
			case 7:
				_owner.addMaxHp(-60);
				_owner.addSpecialPierce(eKind.ABILITY, -3);
				break;
			case 8:
				_owner.addMaxHp(-80);
				_owner.addSpecialPierce(eKind.ABILITY, -4);
				break;
			case 9:
				_owner.addMaxHp(-100);
				_owner.addSpecialPierce(eKind.ABILITY, -5);
				break;
			case 10:
				_owner.addMaxHp(-120);
				_owner.addSpecialPierce(eKind.ABILITY, -6);
				break;
			default:
				break;
			}
		}
		/** 지령의 가더 **/
		if (itemId == 900120) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			case 5:
				_owner.getResistance().addcalcPcDefense(-1);
				_owner.getResistance().addMr(-2);
				break;
			case 6:
				_owner.getResistance().addcalcPcDefense(-1);
				_owner.getResistance().addMr(-3);
				break;
			case 7:
				_owner.getResistance().addcalcPcDefense(-2);
				_owner.getResistance().addMr(-4);
				break;
			case 8:
				_owner.getResistance().addcalcPcDefense(-3);
				_owner.getResistance().addMr(-5);
				break;
			case 9:
				_owner.getResistance().addcalcPcDefense(-4);
				_owner.getResistance().addMr(-6);
				break;
			case 10:
				_owner.getResistance().addcalcPcDefense(-5);
				_owner.getResistance().addMr(-7);
				break;
			default:
				break;
			}
		}
		
		/** 마법사의가더 **/
		if (itemId == 22255) {
			switch (itemlvl) {
			case 5:
			case 6:
				_owner.getAbility().addSp(-1);
				break;
			case 7:
			case 8:
				_owner.getAbility().addSp(-2);
				break;
			case 9:
			case 10:
				_owner.getAbility().addSp(-3);
				break;
			default:
				break;
			}
		}
		/** 체력의 가더 **/
		if (itemId == 22256) {
			switch (itemlvl) {
			case 5:
			case 6:
				_owner.addMaxHp(-25);
				break;
			case 7:
			case 8:
				_owner.addMaxHp(-50);
				break;
			case 9:
			case 10:
				_owner.addMaxHp(-75);
				break;
			default:
				break;
			}
		}
		// TODO 투사의 휘장
		if (itemId == 900152) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-5);
				break;
			case 1:
				_owner.addMaxHp(-10);
				break;
			case 2:
				_owner.addMaxHp(-15);
				break;
			case 4:
				_owner.getAC().addAc(1);
				_owner.addMaxHp(-25);
				break;
			case 5:
				_owner.getAC().addAc(2);
				_owner.addMaxHp(-30);
				_owner.addDmgRate(-1);// 근거리 대미지
				break;
			case 6:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-35);
				_owner.addDmgRate(-2);// 근거리 대미지
				_owner.add_melee_critical_rate(-1);// 근거리 치명타
				break;
			case 7:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-40);
				_owner.addDmgRate(-3);// 근거리 대미지
				_owner.add_melee_critical_rate(-3);// 근거리 치명타
				break;
			case 8:
				_owner.getAC().addAc(3);
				_owner.addDmgRate(-4);// 근거리 대미지
				_owner.add_melee_critical_rate(-5);// 근거리 치명타
				break;
			default:
				break;
			}
		}
		// TODO 명궁의 휘장
		if (itemId == 900153) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-5);
				break;
			case 1:
				_owner.addMaxHp(-10);
				break;
			case 2:
				_owner.addMaxHp(-15);
				break;
			case 4:
				_owner.getAC().addAc(1);
				_owner.addMaxHp(-25);
				break;
			case 5:
				_owner.getAC().addAc(2);
				_owner.addMaxHp(-30);
				_owner.addBowDmgup(-1);//원거리 대미지
				break;
			case 6:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-35);
				_owner.addBowDmgup(-2);//원거리 대미지
				_owner.add_missile_critical_rate(-1);// 원거리 치명타
				break;
			case 7:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-40);
				_owner.addBowDmgup(-3);//원거리 대미지
				_owner.add_missile_critical_rate(-3);// 원거리 치명타
				break;
			case 8:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-50);
				_owner.addBowDmgup(-4);//원거리 대미지
				_owner.add_missile_critical_rate(-5);// 원거리 치명타
				break;
			default:
				break;
			}
		}
		// TODO 현자의 휘장
		if (itemId == 900154) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-5);
				break;
			case 1:
				_owner.addMaxHp(-10);
				break;
			case 2:
				_owner.addMaxHp(-15);
				break;
			case 4:
				_owner.getAC().addAc(1);
				_owner.addMaxHp(-25);
				break;
			case 5:
				_owner.getAC().addAc(2);
				_owner.addMaxHp(-30);
				_owner.addHitup(-1);//근거리 명중
				break;
			case 6:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-35);
				_owner.addHitup(-2);//근거리 명중
				_owner.addBaseMagicCritical(-1);// 마법 치명타
				break;
			case 7:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-40);
				_owner.addHitup(-3);//근거리 명중
				_owner.addBaseMagicCritical(-2);// 마법 치명타
				break;
			case 8:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-50);
				_owner.addHitup(-4);//근거리 명중
				_owner.addBaseMagicCritical(-4);// 마법 치명타
				break;
			default:
				break;
			}
		}
		// TODO 투사의 문장
		if (itemId == 900127) {
			switch (itemlvl) {
			case 4:
				_owner.addHitup(-1);// 근거리 명중
				break;
			case 5:
				_owner.addDmgRate(-1);// 근거리 대미지
				_owner.addHitup(-1);// 근거리 명중
				break;
			case 6:
				_owner.addDmgRate(-2);// 근거리 대미지
				_owner.addHitup(-2);// 근거리 명중
				break;
			case 7:
				_owner.addDmgRate(-3);// 근거리 대미지
				_owner.addHitup(-3);// 근거리 명중
				break;
			case 8:
				_owner.addDmgRate(-4);// 근거리 대미지
				_owner.addHitup(-4);// 근거리 명중
				break;
			default:
				break;
			}
		}
		// TODO 명궁의 문장
		if (itemId == 900128) {
			switch (itemlvl) {
			case 4:
				_owner.addBowHitup(-1);//원거리 명중
				break;
			case 5:
				_owner.addBowDmgup(-1);//원거리 대미지
				_owner.addBowHitup(-1);//원거리 명중
				break;
			case 6:
				_owner.addBowDmgup(-2);//원거리 대미지
				_owner.addBowHitup(-2);//원거리 명중
				break;
			case 7:
				_owner.addBowDmgup(-3);//원거리 대미지
				_owner.addBowHitup(-3);//원거리 명중
				break;
			case 8:
				_owner.addBowDmgup(-4);//원거리 대미지
				_owner.addBowHitup(-4);//원거리 명중
				break;
			default:
				break;
			}
		}
		// TODO 현자의 문장
		if (itemId == 900129) {
			switch (itemlvl) {
			case 4:
				_owner.addBaseMagicHitUp(-1);// 마법 적중
				break;
			case 5:
				_owner.getAbility().addSp(-1);// SP
				_owner.addBaseMagicHitUp(-1);// 마법 적중
				break;
			case 6:
				_owner.getAbility().addSp(-2);// SP
				_owner.addBaseMagicHitUp(-2);// 마법 적중
				break;
			case 7:
				_owner.getAbility().addSp(-3);// SP
				_owner.addBaseMagicHitUp(-3);// 마법 적중
				break;
			case 8:
				_owner.getAbility().addSp(-4);// SP
				_owner.addBaseMagicHitUp(-4);// 마법 적중
				break;
			default:
				break;
			}
		}
		// TODO 수호의 투사 휘장
		if (itemId == 900124) {
			switch (itemlvl) {
			case 5:
				_owner.addDmgRate(-1);// 근거리 대미지
				_owner.addHitup(-2);// 근거리 명중
				_owner.getResistance().addMr(-4);// MR
				_owner.addEinhasadBlessper(-5);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-1);//pvp마법대미지감소	
				break;
			case 6:
				_owner.addDmgRate(-2);// 근거리 대미지
				_owner.addHitup(-3);// 근거리 명중
				_owner.getResistance().addMr(-6);// MR
				_owner.addEinhasadBlessper(-10);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-2);//pvp마법대미지감소
				break;
			case 7:
				_owner.addDmgRate(-3);// 근거리 대미지
				_owner.addHitup(-4);// 근거리 명중
				_owner.getResistance().addMr(-8);// MR
				_owner.addEinhasadBlessper(-15);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-3);//pvp마법대미지감소
				break;
			case 8:
				_owner.addDmgRate(-4);// 근거리 대미지
				_owner.addHitup(-5);// 근거리 명중
				_owner.getResistance().addMr(-10);// MR
				_owner.addEinhasadBlessper(-20);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-5);//pvp마법대미지감소
				break;
			default:
				break;
			}
		}
		// TODO 수호의 명궁 휘장
		if (itemId == 900125) {
			switch (itemlvl) {
			case 5:
				_owner.addBowDmgup(-1);// 원거리 대미지
				_owner.addBowHitup(-2);// 원거리 명중
				_owner.getResistance().addMr(-4);// MR
				_owner.addEinhasadBlessper(-5);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-1);//pvp마법대미지감소
				break;
			case 6:
				_owner.addBowDmgup(-2);// 원거리 대미지
				_owner.addBowHitup(-3);// 원거리 명중
				_owner.getResistance().addMr(-6);// MR
				_owner.addEinhasadBlessper(-10);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-2);//pvp마법대미지감소
				
				break;
			case 7:
				_owner.addBowDmgup(-3);// 원거리 대미지
				_owner.addBowHitup(-4);// 원거리 명중
				_owner.getResistance().addMr(-8);// MR
				_owner.addEinhasadBlessper(-15);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-3);//pvp마법대미지감소
				break;
			case 8:
				_owner.addBowDmgup(-4);// 원거리 대미지
				_owner.addBowHitup(-5);// 원거리 명중
				_owner.getResistance().addMr(-10);// MR
				_owner.addEinhasadBlessper(-20);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-5);//pvp마법대미지감소
				break;
			default:
				break;
			}
		}
		// TODO 수호의 현자 문장
		if (itemId == 900126) {
			switch (itemlvl) {
			case 5:
				_owner.getAbility().addSp(-1);// SP
				_owner.addBaseMagicHitUp(-2);// 마법 적중
				_owner.getResistance().addMr(-4);// MR
				_owner.addEinhasadBlessper(-5);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-1);//pvp마법대미지감소
				break;
			case 6:
				_owner.getAbility().addSp(-2);// SP
				_owner.addBaseMagicHitUp(-3);// 마법 적중
				_owner.getResistance().addMr(-6);// MR
				_owner.addEinhasadBlessper(-10);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-2);//pvp마법대미지감소
				break;
			case 7:
				_owner.getAbility().addSp(-3);// SP
				_owner.addBaseMagicHitUp(-4);// 마법 적중
				_owner.getResistance().addMr(-8);// MR
				_owner.addEinhasadBlessper(-15);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-3);//pvp마법대미지감소
				break;
			case 8:
				_owner.getAbility().addSp(-4);// SP
				_owner.addBaseMagicHitUp(-5);// 마법 적중
				_owner.getResistance().addMr(-10);// MR
				_owner.addEinhasadBlessper(-20);// 축복 소모 효율
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				_owner.addPVPMagicDamageReduction(-5);//pvp마법대미지감소
				break;
			default:
				break;
			}
		}
		/** 완력의 문장 효과 **/
		if (itemId == 900049 || itemId == 900093 || itemId == 900096) {
			switch (itemlvl) {
			case 5:
				_owner.addHitup(-1);
				break;
			case 6:
				_owner.addHitup(-1);
				_owner.addDmgRate(-1);
				break;
			case 7:
				_owner.addHitup(-2);
				_owner.addDmgRate(-2);
				break;
			case 8:
				_owner.addHitup(-3);
				_owner.addDmgRate(-3);
				break;
			case 9:
				_owner.addHitup(-4);
				_owner.addDmgRate(-4);
				break;
			case 10:
				_owner.addHitup(-5);
				_owner.addDmgRate(-5);
				break;
			default:
				break;
			}
		}
		
		//고대 마물 시리즈
		if (itemId == 900015 || itemId == 900016 || itemId == 900017 || itemId == 900018) {
			switch (itemlvl) {
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
				_owner.addDmgRate(-Config.DEDMG2);
				_owner.addBowDmgup(-Config.DEDMG2);
				break;
		
			default:
				break;
			}
		}
		/** 민첩의 문장 효과 **/
		if (itemId == 900050 || itemId == 900094 || itemId == 900097) {
			switch (itemlvl) {
			case 5:
				_owner.addBowHitup(-1);
				break;
			case 6:
				_owner.addBowHitup(-1);
				_owner.addBowDmgup(-1);
				break;
			case 7:
				_owner.addBowHitup(-2);
				_owner.addBowDmgup(-2);
				break;
			case 8:
				_owner.addBowHitup(-3);
				_owner.addBowDmgup(-3);
				break;
			case 9:
				_owner.addBowHitup(-4);
				_owner.addBowDmgup(-4);
				break;
			case 10:
				_owner.addBowHitup(-5);
				_owner.addBowDmgup(-5);
				break;
			default:
				break;
			}
		}
		/** 지식의 문장 효과 **/
		if (itemId == 900051 || itemId == 900095 || itemId == 900098) {
			switch (itemlvl) {
			case 5:
				_owner.addBaseMagicHitUp(-1);
				break;
			case 6:
				_owner.addBaseMagicHitUp(-1);
				_owner.getAbility().addSp(-1);
				break;
			case 7:
				_owner.addBaseMagicHitUp(-2);
				_owner.getAbility().addSp(-2);
				break;
			case 8:
				_owner.addBaseMagicHitUp(-3);
				_owner.getAbility().addSp(-3);
				break;
			case 9:
				_owner.addBaseMagicHitUp(-4);
				_owner.getAbility().addSp(-4);
				break;
			case 10:
				_owner.addBaseMagicHitUp(-5);
				_owner.getAbility().addSp(-5);
				break;
			default:
				break;
			}
		}
		/** 성장/회복의 문장 **/
		if (itemId == 900020 || itemId == 900021 || itemId == 900049 || itemId == 900050 || itemId == 900051 || itemId >= 900093 && itemId <= 900099
				|| itemId >= 900124 && itemId <= 900126 || itemId >= 900127 && itemId <= 900130) {
			switch (itemlvl) {
			case 0:
				_owner.getAC().addAc(-0);
				break;
			case 1:
				_owner.getAC().addAc(-1);
				break;
			case 2:
				_owner.getAC().addAc(-2);
				break;
			case 3:
				_owner.getAC().addAc(-3);
				break;
			case 4:
				_owner.getAC().addAc(-4);
				break;
			case 5:
				_owner.getAC().addAc(-5);
				break;
			case 6:
				_owner.getAC().addAc(-6);
				break;
			case 7:
				_owner.getAC().addAc(-7);
				break;
			case 8:
				_owner.getAC().addAc(-8);
				break;
			case 9:
				_owner.getAC().addAc(-9);
				break;
			case 10:
				_owner.getAC().addAc(-10);
				break;
			default:
				break;
			}
		}

		/** 실프의티셔츠 스턴내성 인챈별 **/
		if (itemId == 900019) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDamageReductionByArmor(-1);//대미지 감소
				_owner.addDmgRate(-1); 
				_owner.addBowDmgRate(-1);
				_owner.getAbility().addSp(-1);
				break;
			case 5:
				_owner.addDamageReductionByArmor(-1);//대미지 감소
				_owner.addDmgRate(-1); 
				_owner.addBowDmgRate(-1);
				_owner.getAbility().addSp(-1);
				_owner.getResistance().addMr(-4);
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 6:
				_owner.addDamageReductionByArmor(-1);//대미지 감소
				_owner.addDmgRate(-1); 
				_owner.addBowDmgRate(-1);
				_owner.getAbility().addSp(-1);
				_owner.getResistance().addMr(-5);
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			case 7:
				_owner.addDamageReductionByArmor(-1);//대미지 감소
				_owner.addDmgRate(-1); 
				_owner.addBowDmgRate(-1);
				_owner.getAbility().addSp(-1);
				_owner.addHitup(-1);// 근거리 명중
				_owner.addBowHitup(-1); // 원거리 명중
				_owner.getResistance().addMr(-6);
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 8:
				_owner.addDamageReductionByArmor(-1);//대미지 감소
				_owner.addDmgRate(-1); 
				_owner.addBowDmgRate(-1);
				_owner.getAbility().addSp(-1);
				_owner.addHitup(-3);// 근거리 명중
				_owner.addBowHitup(-3); // 원거리 명중			
				_owner.addBaseMagicHitUp(-2);// 마법 적중
				_owner.getResistance().addMr(-8);
				_owner.addSpecialResistance(eKind.ABILITY, -12);
				_owner.addSpecialResistance(eKind.ALL, -2);	
				break;
			case 9:
				_owner.addDamageReductionByArmor(-2);//대미지 감소
				_owner.addDmgRate(-2); 
				_owner.addBowDmgRate(-2);
				_owner.getAbility().addSp(-2);
				_owner.addHitup(-5);// 근거리 명중
				_owner.addBowHitup(-5); // 원거리 명중			
				_owner.addBaseMagicHitUp(-4);// 마법 적중
				_owner.getResistance().addMr(-11);
				_owner.addSpecialResistance(eKind.ABILITY, -15);
				_owner.addSpecialResistance(eKind.ALL, -4);	
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDamageReductionByArmor(-3);//대미지 감소
				_owner.addDmgRate(-3); 
				_owner.addBowDmgRate(-3);
				_owner.getAbility().addSp(-3);
				_owner.addHitup(-7);// 근거리 명중
				_owner.addBowHitup(-7); // 원거리 명중			
				_owner.addBaseMagicHitUp(-5);// 마법 적중
				_owner.getResistance().addMr(-14);
				_owner.addSpecialResistance(eKind.ABILITY, -17);
				_owner.addSpecialResistance(eKind.ALL, -6);	
				_owner.addMaxHp(-200);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			default:
				break;
			}
		}
		/** 유니콘의 완력 각반 **/
		if (itemId == 900030) {
			switch (itemlvl) {
			case 9:
			case 10:
			case 11:
			case 12:
				_owner.addDmgRate(-1);
				break;
			}
		}
		/** 유니콘의 민첩 각반 **/
		if (itemId == 900031) {
			switch (itemlvl) {
			case 9:
			case 10:
			case 11:
			case 12:
				_owner.addBowHitup(-1);
				break;
			}
		}
		/** 유니콘의 지식 각반 **/
		if (itemId == 900032) {
			switch (itemlvl) {
			case 9:
			case 10:
			case 11:
			case 12:
				_owner.getAbility().addSp(-1);
				break;
			}
		}
		/** 체력의 각반 **/
		if (itemId == 900023) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-10);
				break;
			case 1:
				_owner.addMaxHp(-15);
				break;
			case 2:
				_owner.addMaxHp(-20);
				break;
			case 3:
				_owner.addMaxHp(-25);
				break;
			case 4:
				_owner.addMaxHp(-30);
				break;
			case 5:
				_owner.addMaxHp(-35);
				break;
			case 6:
				_owner.addMaxHp(-40);
				break;
			case 7:
				_owner.addMaxHp(-45);
				break;
			case 8:
				_owner.addMaxHp(-50);
				break;
			case 9:
				_owner.addMaxHp(-55);
				break;
			default:
				break;
			}
		}
		/** 예언자의 견갑 **/
		if (itemId == 900080) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(-1);// 마법 적중
				break;
			default:
				break;
			}
		}
		
		/** 지휘관/사이하/대마법사의 견갑 pvp감소+공포+피 **/
		if (itemId >= 900021 && itemId <= 900203) {
			switch (itemlvl) {
			case 5:
				_owner.getResistance().addcalcPcDefense(-1); 
				_owner.addSpecialResistance(eKind.FEAR, -1);
				_owner.addMaxHp(-20);
				break;
			case 6:
				_owner.getResistance().addcalcPcDefense(-2); 
				_owner.addSpecialResistance(eKind.FEAR, -2);
				_owner.addMaxHp(-40);
				break;
			case 7:
				_owner.getResistance().addcalcPcDefense(-1); 
				_owner.addSpecialResistance(eKind.FEAR, -3);
				_owner.addMaxHp(-60);
				break;
			case 8:
				_owner.getResistance().addcalcPcDefense(-1); 
				_owner.addSpecialResistance(eKind.FEAR, -4);
				_owner.addMaxHp(-80);
				break;
			case 9:
				_owner.getResistance().addcalcPcDefense(-1); 
				_owner.addSpecialResistance(eKind.FEAR, -5);
				_owner.addMaxHp(-100);
				break;
			default:
				break;
			}
		}
		/** 칠흑의 망토  **/
		if (itemId == 900076) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.getAbility().addCha(-1);
				break;
			case 7:
				_owner.getAbility().addCha(-2);
				break;
			case 8:
				_owner.getAbility().addCha(-3);
				break;
			case 9:
				_owner.getAbility().addCha(-4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addCha(-4);
				break;
			default:
				break;
			}
		}
		/** 맘보 모자 **/
		if (itemId == 20016 || itemId == 120016) {
			switch (itemlvl) {
			case 7:
			case 8:
			case 9:
			case 10:
				_owner.getAbility().addCha(-2);
				break;
			default:
				break;
			}
		}
		/** 맘보 코트 **/
		if (itemId == 20112 || itemId == 120112){
			switch (itemlvl) {
			case 7:
			case 8:
			case 9:
			case 10:
				_owner.getAbility().addCha(-3);
				break;
			default:
				break;
			}
		}
		/** 커츠의 투사 휘장 **/
		if (itemId == 900081) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-5);
				break;	
			case 1:
				_owner.addMaxHp(-10);
				break;	
			case 2:
				_owner.addMaxHp(-15);
				break;	
			case 3:
				_owner.addMaxHp(-20);
				break;	
			case 4:
				_owner.getAC().addAc(1);
				_owner.addMaxHp(-25);
				break;	
			case 5:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-30);
				_owner.addHitup(-1);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 6:
				_owner.getAC().addAc(5);//AC
				_owner.addMaxHp(-35);//HP
				_owner.addHitup(-1);//근거리 명중
				_owner.addDamageReductionByArmor(-2);//대미지감소
				_owner.getResistance().addMr(-3);//MR
				_owner.addDmgRate(-2);//근거리 대미지
				break;
			case 7:
				_owner.getAC().addAc(6);
				_owner.addMaxHp(-40);
				_owner.addDamageReductionByArmor(-3);//대미지감소
				_owner.getResistance().addMr(-5);//MR
				_owner.addDmgRate(-3);//근거리 대미지
				_owner.addHitup(-3);//근거리 명중
				_owner.getResistance().addcalcPcDefense(-1);//PVP대미지감소
				break;
			case 8:
				_owner.getAC().addAc(7);
				_owner.addMaxHp(-50);
				_owner.addDamageReductionByArmor(-4);//대미지감소
				_owner.getResistance().addMr(-7);//MR
				_owner.addDmgRate(-4);//근거리 대미지
				_owner.addHitup(-5);//근거리 명중
				_owner.getResistance().addcalcPcDefense(-2);//PVP대미지감소
				break;
			default:
				break;
			}
		}		
		/** 커츠의 명궁 휘장 **/
		if (itemId == 900082) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-5);
				break;	
			case 1:
				_owner.addMaxHp(-10);
				break;	
			case 2:
				_owner.addMaxHp(-15);
				break;	
			case 3:
				_owner.addMaxHp(-20);
				break;	
			case 4:
				_owner.getAC().addAc(1);
				_owner.addMaxHp(-25);
				break;	
			case 5:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-30);
				_owner.addBowHitup(-1);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 6:
				_owner.getAC().addAc(5);
				_owner.addMaxHp(-35);
				_owner.addDamageReductionByArmor(-2);
				_owner.getResistance().addMr(-3);//MR
				_owner.addBowDmgup(-2);
				_owner.addBowHitup(-1);
				break;
			case 7:
				_owner.getAC().addAc(6);
				_owner.addMaxHp(-40);
				_owner.addDamageReductionByArmor(-3);
				_owner.getResistance().addMr(-5);//MR
				_owner.addBowDmgup(-3);
				_owner.addBowHitup(-3);
				_owner.getResistance().addcalcPcDefense(-1);//PVP대미지감소
				break;
			case 8:
				_owner.getAC().addAc(7);
				_owner.addMaxHp(-50);
				_owner.addDamageReductionByArmor(-4);//대미지 감소
				_owner.getResistance().addMr(-7);//MR
				_owner.addBowDmgup(-4);
				_owner.addBowHitup(-5);
				_owner.getResistance().addcalcPcDefense(-2);//PVP대미지감소
				break;
			default:
				break;
			}
		}	
		/** 커츠의 현자 휘장 **/
		if (itemId == 900083) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-5);
				break;	
			case 1:
				_owner.addMaxHp(-10);
				break;	
			case 2:
				_owner.addMaxHp(-15);
				break;	
			case 3:
				_owner.addMaxHp(-20);
				break;	
			case 4:
				_owner.getAC().addAc(1);
				_owner.addMaxHp(-25);
				break;	
			case 5:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-30);
				_owner.addDamageReductionByArmor(-1);//대미지 감소
				_owner.addHitup(-1);
				break;
			case 6:
				_owner.getAC().addAc(5);
				_owner.addMaxHp(-35);
				_owner.addDamageReductionByArmor(-2);//대미지 감소
				_owner.getResistance().addMr(-3);//MR
				_owner.addHitup(-2);//근거리 명중
				_owner.addBaseMagicCritical(-1);// 마법 치명타
				break;
			case 7:
				_owner.getAC().addAc(6);
				_owner.addMaxHp(-40);
				_owner.addDamageReductionByArmor(-3);//대미지 감소
				_owner.getResistance().addMr(-5);//MR
				_owner.addHitup(-3);//근거리 명중
				_owner.addBaseMagicCritical(-3);// 마법 치명타
				_owner.getResistance().addcalcPcDefense(-1);//PVP대미지감소
				break;
			case 8:
				_owner.getAC().addAc(7);
				_owner.addMaxHp(-50);
				_owner.addDamageReductionByArmor(-4);//대미지 감소
				_owner.getResistance().addMr(-7);//MR
				_owner.addHitup(-4);//근거리 명중
				_owner.addBaseMagicCritical(-5);// 마법 치명타
				_owner.getResistance().addcalcPcDefense(-2);//PVP대미지감소
				break;
			default:
				break;
			}
		}	
		/** 커츠의 수호 휘장 **/
		if (itemId == 900084) {
			switch (itemlvl) {
			case 0:
				_owner.addMaxHp(-5);
				break;	
			case 1:
				_owner.addMaxHp(-10);
				break;	
			case 2:
				_owner.addMaxHp(-15);
				break;	
			case 3:
				_owner.getAC().addAc(1);
				_owner.addMaxHp(-20);
				break;	
			case 4:
				_owner.getAC().addAc(2);
				_owner.addMaxHp(-25);
				break;	
			case 5:
				_owner.getAC().addAc(3);
				_owner.addMaxHp(-30);
				_owner.addDamageReductionByArmor(-1);
				break;	
			case 6:
				_owner.getAC().addAc(5);
				_owner.addMaxHp(-35);
				_owner.addDamageReductionByArmor(-2);
				_owner.getResistance().addMr(-3);
				break;	
			case 7:
				_owner.getAC().addAc(6);
				_owner.addMaxHp(-40);
				_owner.addDamageReductionByArmor(-3);
				_owner.getResistance().addMr(-5);
				break;	
			case 8:
				_owner.getAC().addAc(7);
				_owner.addMaxHp(-50);
				_owner.addDamageReductionByArmor(-4);
				_owner.getResistance().addMr(-7);
				break;
			default:
				break;
			}
		}	
		/** 대마법사의 모자 **/
		if (itemId == 202022) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, -1);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, -2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.SPIRIT, -3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.SPIRIT, -4);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.SPIRIT, -5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, -6);
				break;
			default:
				break;
			}
		}
		/** 붉은 해의 각반 **/
		if (itemId == 9001117) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addMaxHp(-5);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 5:
				_owner.addMaxHp(-15);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 6:
				_owner.addMaxHp(-25);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 7:
				_owner.addDmgRate(-1);
				_owner.addBowDmgRate(-1);
				_owner.addBaseMagicHitUp(-1);
				_owner.addMaxHp(-35);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 8:
				_owner.addDmgRate(-2);
				_owner.addBowDmgRate(-2);
				_owner.addBaseMagicHitUp(-2);
				_owner.addMaxHp(-45);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 9:
				_owner.addDmgRate(-3);
				_owner.addBowDmgRate(-3);
				_owner.addBaseMagicHitUp(-2);
				_owner.addMaxHp(-55);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 10:
				_owner.addDmgRate(-4);
				_owner.addBowDmgRate(-4);
				_owner.addBaseMagicHitUp(-4);
				_owner.addMaxHp(65);
				_owner.addDamageReductionByArmor(1);
				break;
			case 11:
				_owner.addDmgRate(-5);
				_owner.addBowDmgRate(-5);
				_owner.addBaseMagicHitUp(-5);
				_owner.addMaxHp(75);
				_owner.addDamageReductionByArmor(1);
				break;
			case 12:
				_owner.addDmgRate(-6);
				_owner.addBowDmgRate(-6);
				_owner.addBaseMagicHitUp(-6);
				_owner.addMaxHp(85);
				_owner.addDamageReductionByArmor(1);
				break;
			case 13:
				_owner.addDmgRate(-7);
				_owner.addBowDmgRate(-7);
				_owner.addBaseMagicHitUp(-7);
				_owner.addMaxHp(95);
				_owner.addDamageReductionByArmor(1);
				break;
			case 14:
				_owner.addDmgRate(-8);
				_owner.addBowDmgRate(-8);
				_owner.addBaseMagicHitUp(-8);
				_owner.addMaxHp(105);
				_owner.addDamageReductionByArmor(1);
				break;
			case 15:
				_owner.addDmgRate(-8);
				_owner.addBowDmgRate(-8);
				_owner.addBaseMagicHitUp(-8);
				_owner.addMaxHp(115);
				_owner.addDamageReductionByArmor(1);
				break;
			}
		}
		/** 붉은 해의 각반 **/
		if (itemId == 9001118) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addMaxHp(-5);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 5:
				_owner.addMaxHp(-15);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 6:
				_owner.addMaxHp(-25);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 7:
				_owner.addDmgRate(-1);
				_owner.addBowDmgRate(-1);
				_owner.addBaseMagicHitUp(-1);
				_owner.addMaxHp(-35);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 8:
				_owner.addDmgRate(-2);
				_owner.addBowDmgRate(-2);
				_owner.addBaseMagicHitUp(-2);
				_owner.addMaxHp(-45);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 9:
				_owner.addDmgRate(-3);
				_owner.addBowDmgRate(-3);
				_owner.addBaseMagicHitUp(-2);
				_owner.addMaxHp(-55);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 10:
				_owner.addDmgRate(-4);
				_owner.addBowDmgRate(-4);
				_owner.addBaseMagicHitUp(-4);
				_owner.addMaxHp(65);
				_owner.addDamageReductionByArmor(1);
				break;
			case 11:
				_owner.addDmgRate(-5);
				_owner.addBowDmgRate(-5);
				_owner.addBaseMagicHitUp(-5);
				_owner.addMaxHp(75);
				_owner.addDamageReductionByArmor(1);
				break;
			case 12:
				_owner.addDmgRate(-6);
				_owner.addBowDmgRate(-6);
				_owner.addBaseMagicHitUp(-6);
				_owner.addMaxHp(85);
				_owner.addDamageReductionByArmor(1);
				break;
			case 13:
				_owner.addDmgRate(-7);
				_owner.addBowDmgRate(-7);
				_owner.addBaseMagicHitUp(-7);
				_owner.addMaxHp(95);
				_owner.addDamageReductionByArmor(1);
				break;
			case 14:
				_owner.addDmgRate(-8);
				_owner.addBowDmgRate(-8);
				_owner.addBaseMagicHitUp(-8);
				_owner.addMaxHp(105);
				_owner.addDamageReductionByArmor(1);
				break;
			case 15:
				_owner.addDmgRate(-8);
				_owner.addBowDmgRate(-8);
				_owner.addBaseMagicHitUp(-8);
				_owner.addMaxHp(115);
				_owner.addDamageReductionByArmor(1);
				break;
			}
		}
		/** 지휘관의 투구 **/
		if (itemId == 22360) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.ABILITY, -3);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.ABILITY, -4);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ABILITY, -5);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.ABILITY, -6);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.ABILITY, -7);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			}
		}
		/** 지룡의 티셔츠 **/
		if (itemId == 900025) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.getResistance().addMr(-4);
				break;
			case 6:
				_owner.getResistance().addMr(-5);
				break;
			case 7:
				_owner.addDamageReductionByArmor(-1);
				break;
			case 8:
				_owner.getResistance().addMr(-8);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 9:
				_owner.getResistance().addMr(-11);
				_owner.addDamageReductionByArmor(-2);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getResistance().addMr(-14);
				_owner.addDamageReductionByArmor(-2);
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			}
		}
		/** 화룡의 티셔츠 **/
		if (itemId == 900026) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDmgRate(-1);
				break;
			case 5:
				_owner.addDmgRate(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 6:
				_owner.addDmgRate(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			case 7:
				_owner.addDmgRate(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				break;
			case 8:
				_owner.addDmgRate(-1);
				_owner.addHitup(-2);//근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -12);
				break;
			case 9:
				_owner.addDmgRate(-2);
				_owner.addHitup(-4);//근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -15);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDmgRate(-2);
				_owner.addHitup(-6);//근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -18);
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			default:
				break;
			}
		}
		/** 풍룡의 티셔츠 **/
		if (itemId == 900027) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowHitup(-1);
				break;
			case 5:
				_owner.addBowHitup(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 6:
				_owner.addBowHitup(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			case 7:
				_owner.addBowDmgRate(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				break;
			case 8:
				_owner.addBowDmgRate(-1);
				_owner.addBowHitup(-2);// 원거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -12);
				break;
			case 9:
				_owner.addBowDmgRate(-2);
				_owner.addBowHitup(-4);// 원거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -15);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowDmgRate(-2);
				_owner.addBowHitup(-6);// 원거리 명중
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -18);
				break;
			default:
				break;
			}
		}
		/** 수룡의 티셔츠 **/
		if (itemId == 900028) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getAbility().addSp(-1);
				break;
			case 5:
				_owner.getAbility().addSp(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 6:
				_owner.getAbility().addSp(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			case 7:
				_owner.getAbility().addSp(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				break;
			case 8:
				_owner.getAbility().addSp(-2);
				_owner.addBaseMagicHitUp(-1);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, -12);
				break;
			case 9:
				_owner.getAbility().addSp(-2);
				_owner.addBaseMagicHitUp(-3);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, -15);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(-3);
				_owner.addBaseMagicHitUp(-4);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, -18);
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			default:
				break;
			}
		}
		/** 축복받은 지룡의 티셔츠 **/
		if (itemId == 900184) {
			switch (itemlvl) {
			case 5:
				_owner.getResistance().addMr(-4);
				break;
			case 6:
				_owner.getResistance().addMr(-5);
				break;
			case 7:
				_owner.getResistance().addMr(-6);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 8:
				_owner.getResistance().addMr(-8);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 9:
				_owner.getResistance().addMr(-11);
				_owner.addDamageReductionByArmor(-2);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getResistance().addMr(-14);
				_owner.addDamageReductionByArmor(-2);
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			}
		}
		/** 축복받은 화룡의 티셔츠 **/
		if (itemId == 900185) {
			switch (itemlvl) {
			case 5:
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			case 7:
				_owner.addDmgRate(-1);
				_owner.addHitup(-1);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 8:
				_owner.addDmgRate(-1);
				_owner.addHitup(-3);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -12);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 9:
				_owner.addDmgRate(-2);
				_owner.addHitup(-5);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -15);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDmgRate(-2);
				_owner.addHitup(-7);// 근거리 명중
				_owner.addSpecialResistance(eKind.ABILITY, -18);
				_owner.addSpecialResistance(eKind.ALL, -6);
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			default:
				break;
			}
		}
		/** 축복받은 풍룡의 티셔츠 **/
		if (itemId == 900186) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowDmgRate(-1);
				break;
			case 5:
				_owner.addBowDmgRate(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 6:
				_owner.addBowDmgRate(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			case 7:
				_owner.addBowDmgRate(-1);
				_owner.addBowHitup(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 8:
				_owner.addBowDmgRate(-1);
				_owner.addBowHitup(-3);
				_owner.addSpecialResistance(eKind.ABILITY, -12);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 9:
				_owner.addBowDmgRate(-2);
				_owner.addBowHitup(-5);
				_owner.addSpecialResistance(eKind.ABILITY, -15);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowDmgRate(-2);
				_owner.addBowHitup(-7);
				_owner.addSpecialResistance(eKind.ABILITY, 18);
				_owner.addSpecialResistance(eKind.ALL, -6);
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			default:
				break;
			}
		}
		/** 축복받은 수룡의 티셔츠 **/
		if (itemId == 900187) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getAbility().addSp(-1);
				break;
			case 5:
				_owner.getAbility().addSp(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -8);
				break;
			case 6:
				_owner.getAbility().addSp(-1);
				_owner.addSpecialResistance(eKind.ABILITY, -9);
				break;
			case 7:
				_owner.getAbility().addSp(-2);
				_owner.addSpecialResistance(eKind.ABILITY, -10);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 8:
				_owner.getAbility().addSp(-2);
				_owner.addBaseMagicHitUp(-2);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, -12);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 9:
				_owner.getAbility().addSp(-2);
				_owner.addBaseMagicHitUp(-4);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, -15);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(-3);
				_owner.addBaseMagicHitUp(-5);// 마법 적중
				_owner.addSpecialResistance(eKind.ABILITY, -18);
				_owner.addSpecialResistance(eKind.ALL, -6);
				_owner.addMaxHp(-100);
				_owner.getResistance().addPVPweaponTotalDamage(-1);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			default:
				break;
			}
		}
		
		/** 스텟부츠 리뉴얼 **/
		if (itemId == 22359 || itemId == 222308 || itemId ==222309 || itemId ==222307) {
			switch (itemlvl) {
			case 7:
				_owner.addMaxHp(-20);
				break;
			case 8:
				_owner.addMaxHp(-40);
				break;
			case 9:
				_owner.addMaxHp(-60);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			case 10:
				_owner.addMaxHp(-60);
				_owner.getResistance().addcalcPcDefense(-1);
				break;
			default:
				break;
			}
		}
		/** 고대 투사의 가더 **/
		if (itemId == 22003) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDmgRate(-1);
				break;
			case 5:
			case 6:
				_owner.addDmgRate(-2);
				break;
			case 7:
			case 8:
				_owner.addDmgRate(-3);
				break;
			case 9:
			case 10:
				_owner.addDmgRate(-4);
				break;
			default:
				break;
			}
		}

		/** 고대 명궁의 가더 **/
		if (itemId == 22000) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowHitup(-1);
				break;
			case 5:
			case 6:
				_owner.addBowHitup(-2);
				break;
			case 7:
			case 8:
				_owner.addBowHitup(-3);
				break;
			case 9: case 10:
				_owner.addBowHitup(-4);
				break;
			default:
				break;
			}
		}
		/** 호박 목걸이 **/
		if (itemId == 900024) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(-3);// 마법 적중
				break;
			}
		}
		/** 수령의 귀걸이 **/
		if (itemId == 900114) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(-2);
				break;
			}
		}
		/** 수호의 가더 **/
		if (itemId == 22254) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addDamageReductionByArmor(-1);
				break;
			case 5:
			case 6:
				_owner.addDamageReductionByArmor(-2);
				break;
			case 7:
			case 8:
				_owner.addDamageReductionByArmor(-3);
				break;
			case 9:
			case 10:
				_owner.addDamageReductionByArmor(-4);
				break;
			default:
				break;
			}
		}
		
		/** 거대여왕 개미의 금빛 날개 **/
		if (itemId == 20049 || itemId == 900057) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.SPIRIT, -2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, -3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, -4);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.SPIRIT, -5);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.SPIRIT, -6);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.SPIRIT, -7);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, -7);
				break;
			}
		}
		/** 거대여왕 개미의 은빛 날개 **/
		if (itemId == 20050 || itemId == 900056) {
			switch (itemlvl) {
			case 5:
				_owner.addSpecialPierce(eKind.SPIRIT, -1);
				break;
			case 6:
				_owner.addSpecialPierce(eKind.SPIRIT, -2);
				break;
			case 7:
				_owner.addSpecialPierce(eKind.SPIRIT, -3);
				break;
			case 8:
				_owner.addSpecialPierce(eKind.SPIRIT, -4);
				break;
			case 9:
				_owner.addSpecialPierce(eKind.SPIRIT, -5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, 7);
				break;
			}
		}
		
		/** 빛나는 아덴 용사의 망토 **/
		if (itemId == 900189) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.SPIRIT, -2);
				_owner.addSpecialResistance(eKind.FEAR, -2);
				break;
			case 5:
				_owner.addSpecialResistance(eKind.SPIRIT, -3);
				_owner.addSpecialResistance(eKind.FEAR, -2);
				_owner.addDamageReductionByArmor(-1);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.SPIRIT, -4);
				_owner.addSpecialResistance(eKind.FEAR, -2);
				_owner.addDamageReductionByArmor(-2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.SPIRIT, -5);
				_owner.addSpecialResistance(eKind.FEAR, -3);
				_owner.addDamageReductionByArmor(-3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.SPIRIT, -6);
				_owner.addSpecialResistance(eKind.FEAR, -4);
				_owner.addDamageReductionByArmor(-4);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.SPIRIT, -7);
				_owner.addSpecialResistance(eKind.FEAR, -5);
				_owner.addDamageReductionByArmor(-5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.SPIRIT, -7);
				_owner.addSpecialResistance(eKind.FEAR, -5);
				_owner.addDamageReductionByArmor(-5);
				break;
			}
		}
		/** 리치의 반지 **/
		if (itemId == 900163) {
			switch (itemlvl) {
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
				_owner.addBaseMagicCritical(-3);// 마법 치명타
				break;
			}
		}
		/** 뱀파이어의 망토 **/
		if (itemId == 20079) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.addSpecialResistance(eKind.FEAR, -2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.FEAR, -3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.FEAR, -4);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.FEAR, -5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.FEAR, -5);
				break;
			}
		}
		
		/** 아이리스, 머미로드, 프로켈의 부츠 **/
		if (itemId == 900155) {
			switch (itemlvl) {
			case 5:
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.ALL, -3);
				_owner.getResistance().addMr(-3);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.ALL, -4);
				_owner.getResistance().addMr(-4);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addSpecialResistance(eKind.ALL, -5);
				_owner.getResistance().addMr(-5);
				_owner.addDamageReductionByArmor(-1);
				break;
			default:
				break;
			}
		}
		/** 에르자베의 왕관 **/
		if (itemId == 21117) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addSpecialResistance(eKind.ABILITY, -5); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, -5); // 공포내성
			case 5:
				_owner.addSpecialResistance(eKind.ABILITY, -6); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, -6); // 공포내성
				break;
			case 6:
				_owner.addSpecialResistance(eKind.ABILITY, -7); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, -7); // 공포내성
				break;
			case 7:
				_owner.addSpecialResistance(eKind.ABILITY, -8); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, -8); // 공포내성
				break;
			case 8:
				_owner.addSpecialResistance(eKind.ABILITY, -9); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, -9); // 공포내성
				break;
			case 9:
				_owner.addSpecialResistance(eKind.ABILITY, -10); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, -10); // 공포내성
				break;
			case 10:
				_owner.addSpecialResistance(eKind.ABILITY, -11); // 기술내성
				_owner.addSpecialResistance(eKind.FEAR, -11); // 공포내성
				break;
			default:
				break;
			}
		}
		
		/** 머미로드의 장갑 **/
		if (itemId == 900156) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				_owner.addBaseMagicHitUp(-1);
				_owner.getAbility().addSp(-1);
				break;
			case 7:
				_owner.addBaseMagicHitUp(-2);
				_owner.getAbility().addSp(-2);
				break;
			case 8:
				_owner.addBaseMagicHitUp(-3);
				_owner.getAbility().addSp(-3);
				break;
			case 9:
				_owner.addBaseMagicHitUp(-4);
				_owner.getAbility().addSp(-3);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getAbility().addSp(-3);
				break;
			default:
				break;
			}
		}
		/** 쿠거의 가더 **/
		if (itemId == 900157) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addHitup(-3);
				break;
			case 5:
				_owner.addHitup(-4);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 6:
				_owner.addHitup(-5);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 7:
				_owner.addHitup(-6);
				_owner.addSpecialResistance(eKind.ALL, -3);
				break;
			case 8:
				_owner.addHitup(-7);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 9:
				_owner.addHitup(-8);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addHitup(-8);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			default:
				break;
			}
		}
		/** 우그누스의 가더 **/
		if (itemId == 900158) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.addBowHitup(-3);
				break;
			case 5:
				_owner.addBowHitup(-4);
				_owner.addSpecialResistance(eKind.ALL, -1);
				break;
			case 6:
				_owner.addBowHitup(-5);
				_owner.addSpecialResistance(eKind.ALL, -2);
				break;
			case 7:
				_owner.addBowHitup(-6);
				_owner.addSpecialResistance(eKind.ALL, -3);
				break;
			case 8:
				_owner.addBowHitup(-7);
				_owner.addSpecialResistance(eKind.ALL, -4);
				break;
			case 9:
				_owner.addBowHitup(-8);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowHitup(-8);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			default:
				break;
			}
		}
		/** 나이트발드의 각반 **/
		if (itemId == 900159) {
			switch (itemlvl) {
			case 5:
				_owner.addHitup(-1);
			//	_owner.addDmgup(-1);
				break;
			case 6:
				_owner.addHitup(-2);
			//	_owner.addDmgup(-2);
				break;
			case 7:
				_owner.addHitup(-3);
			//	_owner.addDmgup(-3);
				break;
			case 8:
				_owner.addHitup(-4);
			//	_owner.addDmgup(-4);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addHitup(-5);
			//	_owner.addDmgup(-5);
				break;
			default:
				break;
			}
		}
		/** 아이리스의 각반 **/
		if (itemId == 900160) {
			switch (itemlvl) {
			case 5:
				_owner.addBowHitup(-1);
		//		_owner.addBowDmgup(-1);
				break;
			case 6:
				_owner.addBowHitup(-2);
			//	_owner.addBowDmgup(-2);
				break;
			case 7:
				_owner.addBowHitup(-3);
			//	_owner.addBowDmgup(-3);
				break;
			case 8:
				_owner.addBowHitup(-4);
			//	_owner.addBowDmgup(-4);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBowHitup(-5);
			//	_owner.addBowDmgup(-5);
				break;
			default:
				break;
			}
		}
		/** 뱀파이어의 각반 **/
		if (itemId == 900161) {
			switch (itemlvl) {
			case 5:
				_owner.addBaseMagicHitUp(-1);
			//	_owner.getAbility().addSp(-1);
				break;
			case 6:
				_owner.addBaseMagicHitUp(-2);
			//	_owner.getAbility().addSp(-2);
				break;
			case 7:
				_owner.addBaseMagicHitUp(-3);
			//	_owner.getAbility().addSp(-3);
				break;
			case 8:
				_owner.addBaseMagicHitUp(-4);
			//	_owner.getAbility().addSp(-4);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addBaseMagicHitUp(-5);
			//	_owner.getAbility().addSp(-5);
				break;
			default:
				break;
			}
		}
		
		/** 드래곤 슬레이어의 각반 **/
		if (itemId == 900200) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				_owner.getResistance().addMr(-7);
				_owner.getResistance().addcalcPcDefense(-2);
				_owner.addMaxHp(-100);
				break;		
			case 5:
				_owner.getResistance().addMr(-9);
				_owner.getResistance().addcalcPcDefense(-3);
				_owner.addMaxHp(-100);
				break;
			case 6:
				_owner.getResistance().addMr(-11);
				_owner.getResistance().addcalcPcDefense(-4);
				_owner.addMaxHp(-100);
				break;
			case 7:
				_owner.getResistance().addMr(-13);
				_owner.getResistance().addcalcPcDefense(-5);
				_owner.addMaxHp(-150);
				break;
			case 8:
				_owner.getResistance().addMr(-15);
				_owner.getResistance().addcalcPcDefense(-6);
				_owner.addMaxHp(-200);
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.getResistance().addMr(-17);
				_owner.getResistance().addcalcPcDefense(-7);
				_owner.addMaxHp(-250);
				break;
			default:
				break;
			}
		}
		/** 흑기사의 면갑 대미지감소 **/
		if (itemId == 900038 || itemId == 900054) {
			switch (itemlvl) {
			case 5:
				_owner.getResistance().addMr(-4);
				break;
			case 6:
				_owner.getResistance().addMr(-8);
				break;
			case 7:
				_owner.getResistance().addMr(-12);
				break;
			case 8:
				_owner.getResistance().addMr(-16);
				break;
			case 9:
				_owner.getResistance().addMr(-20);
				break;
			case 10:
				_owner.getResistance().addMr(-24);
				break;
			}
		}
		/** 발라카스의 완력, 예지력 **/
		if (itemId >= 22208 && itemId <= 22209) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -3);
				break;
			case 6:
			//	_owner.getResistance().addcalcPcDefense(-3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -4);
				break;
			case 7:
			//	_owner.getResistance().addcalcPcDefense(-4);
				_owner.add_melee_critical_rate(-1);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				break;
			case 8:
			//	_owner.getResistance().addcalcPcDefense(-5);
				_owner.add_melee_critical_rate(-2);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -6);
				break;
			case 9:
			//	_owner.getResistance().addcalcPcDefense(-6);
				_owner.add_melee_critical_rate(-3);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			case 10:
			//	_owner.getResistance().addcalcPcDefense(-7);
				_owner.add_melee_critical_rate(-3);// 근거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			}
		}
		
		/** 발라카스의 인내력 **/
		if (itemId == 22210) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -3);
				break;
			case 6:
			//	_owner.getResistance().addcalcPcDefense(-3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -4);
				break;
			case 7:
			//	_owner.getResistance().addcalcPcDefense(-4);  // 대미지 리덕션 무시
				_owner.add_missile_critical_rate(-1);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				break;
			case 8:
			//	_owner.getResistance().addcalcPcDefense(-5);
				_owner.add_missile_critical_rate(-2);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -6);
				break;
			case 9:
			//	_owner.getResistance().addcalcPcDefense(-6);
				_owner.add_missile_critical_rate(-3);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			case 10:
			//	_owner.getResistance().addcalcPcDefense(-7);
				_owner.add_missile_critical_rate(-3);// 원거리 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			}
		}
		
		/** 발라카스의 마력 **/
		if (itemId == 22211) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -3);
				break;
			case 6:
			//	_owner.getResistance().addcalcPcDefense(-3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -4);
				break;
			case 7:
			//	_owner.getResistance().addcalcPcDefense(-4);  // 대미지 리덕션 무시
				_owner.addBaseMagicCritical(-1); // 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				break;
			case 8:
			//	_owner.getResistance().addcalcPcDefense(-5);
				_owner.addBaseMagicCritical(-2); // 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -6);
				break;
			case 9:
			//	_owner.getResistance().addcalcPcDefense(-6);
				_owner.addBaseMagicCritical(-3); // 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			case 10:
			//	_owner.getResistance().addcalcPcDefense(-7);
				_owner.addBaseMagicCritical(-3); // 마법 치명타
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			}
		}
		
		/** 할파스의 완력 **/
		if (itemId == 111137) {
			switch (itemlvl) {
			case 0:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-7);
			//	_owner.getResistance().addMr(-24);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 1:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-9);
			//	_owner.getResistance().addMr(-25);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 2:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-26);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 3:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-13);
			//	_owner.getResistance().addMr(-27);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 4:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-28);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 5:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-29);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 6:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-30);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 7:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-31);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 8:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-32);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 9:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-33);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 10:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11); 
			//	_owner.getResistance().addMr(-34);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			}
		}
		
		/** 할파스의 예지력 **/
		if (itemId == 111141) {
			switch (itemlvl) {
			case 0:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-7);
			//	_owner.getResistance().addMr(-24);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 1:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-9);
			//	_owner.getResistance().addMr(-25);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 2:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-26);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 3:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-13);
			//	_owner.getResistance().addMr(-27);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 4:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-28);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 5:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-29);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 6:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-30);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 7:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-31);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 8:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-32);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 9:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-33);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 10:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11); 
			//	_owner.getResistance().addMr(-34);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			}
		}
		
		/** 할파스의 마력 **/
		if (itemId == 111140) {
			switch (itemlvl) {
			case 0:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-7);
			//	_owner.getResistance().addMr(-24);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 1:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-9);
			//	_owner.getResistance().addMr(-25);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 2:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-26);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 3:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-13);
			//	_owner.getResistance().addMr(-27);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 4:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-28);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 5:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-29);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 6:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-30);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 7:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-31);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 8:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-32);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 9:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11);
			//	_owner.getResistance().addMr(-33);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			case 10:
			//	_owner.addDmgRate(-5);// 근거리 대미지
			//	_owner.add_melee_critical_rate(-5);// 근거리 치명타
				_owner.addDamageReductionByArmor(-11); 
			//	_owner.getResistance().addMr(-34);
				_owner.addSpecialResistance(eKind.ALL, -5);
				break;
			}
		}
		
		/** 안타라스 갑옷 **/
		if (itemId == 22196 || itemId == 22197 || itemId == 22198 || itemId == 22199) {
			_owner.sendPackets(new S_SystemMessage("\\fe안타라스의 분노가 사라집니다."));
		}
		/** 린드비오르 갑옷 **/
		if (itemId == 22204 || itemId == 22205 || itemId == 22206 || itemId == 22207) {
			_owner.sendPackets(new S_SystemMessage("\\fK린드비오르의 분노가 사라집니다."));
		}

		/** 파푸리온 갑옷 **/
		if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203) {
			_owner.sendPackets(new S_SystemMessage("\\fB파푸리온의 분노가 사라집니다."));
		}

		/** 발라카스 갑옷 **/
		if (itemId == 22208 || itemId == 22209 || itemId == 22210 || itemId == 22211) {
			_owner.sendPackets(new S_SystemMessage("\\fA발라카스의 분노가 사라집니다."));
		}
		
		/** 할파스 갑옷 **/
		if (itemId == 111137 || itemId == 111141 || itemId == 111140) {
			_owner.sendPackets(new S_SystemMessage("\\fA할파스의 분노가 사라집니다."));
		}
		
		
		
		/** 신성한 엘름의 축복 **/
		if (itemId == 900035 || itemId == 900072) {
			switch (itemlvl) {
			case 5:
				_owner.getResistance().addMr(-4);
				break;
			case 6:
				_owner.getResistance().addMr(-8);
				break;
			case 7:
				_owner.getResistance().addMr(-12);
				break;
			case 8:
				_owner.getResistance().addMr(-16);
				break;
			case 9:
				_owner.getResistance().addMr(-20);
				break;
			case 10:
				_owner.getResistance().addMr(-24);
				break;
			default:
				break;
			}
		}
		if (itemId == 900039) {
			switch (itemlvl) {
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
				_owner.addHitup(-2);
				break;
			default:
				break;
			}
		}
		if (itemId == 900040) {
			switch (itemlvl) {
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
				_owner.addBowHitup(-2);
				break;
			default:
				break;
			}
		}
		if (itemId == 900041) {
			switch (itemlvl) {
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
				_owner.addBaseMagicHitUp(-2);// 마법 적중
				break;
			default:
				break;
			}
		}
		if (itemId == 900036 || itemId == 900073) {
			switch (itemlvl) {
			case 5:
				_owner.addHitup(-1);
				break;
			case 6:
				_owner.addHitup(-2);
				break;
			case 7:
				_owner.addHitup(-3);
				break;
			case 8:
				_owner.addHitup(-4);
				break;
			case 9:
				_owner.addHitup(-5);
				break;
			case 10:
				_owner.addHitup(-5);
				break;
			default:
				break;
			}
		}
		if (itemId == 900037 || itemId == 900074) {
			switch (itemlvl) {
			case 5:
				_owner.addBowHitup(-1);
				break;
			case 6:
				_owner.addBowHitup(-2);
				break;
			case 7:
				_owner.addBowHitup(-3);
				break;
			case 8:
				_owner.addBowHitup(-4);
				break;
			case 9:
				_owner.addBowHitup(-5);
				break;
			case 10:
				_owner.addBowHitup(-5);
				break;
			default:
				break;
			}
		}
		/** 안타라스의 완력/예지력/인내력/마력 **/
		if (itemId >= 22196 && itemId <= 22199) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addDamageReductionByArmor(-3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -3);
				break;
			case 6:
				_owner.addDamageReductionByArmor(-3);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -4);
				break;
			case 7:
				_owner.addDamageReductionByArmor(-4);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				break;
			case 8:
				_owner.addDamageReductionByArmor(-5);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -6);
				break;
			case 9:
				_owner.addDamageReductionByArmor(-6);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			case 10:
				_owner.addDamageReductionByArmor(-7);
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			default:
				break;
			}
		}
		/** 파푸리온의 완력/예지력/인내력/마력 **/
		if (itemId >= 22200 && itemId <= 22203) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -4);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -6);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			case 10:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			default:
				break;
			}
		}
		/** 린드비오르의 완력/예지력/인내력/마력 **/
		if (itemId >= 22204 && itemId <= 22207) {
			switch (itemlvl) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -3);
				break;
			case 6:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -4);
				break;
			case 7:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -5);
				break;
			case 8:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -6);
				break;
			case 9:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			case 10:
				_owner.addSpecialResistance(eKind.DRAGON_SPELL, -7);
				break;
			default:
				break;
			}	
		}
		/** 빛나는 마력의장갑 **/
		if (itemId == 20274) {
			switch (itemlvl) {
			case 5:
				_owner.addWeightReduction(-1);
				break;
			case 6:
				_owner.addWeightReduction(-2);
				break;
			case 7:
				_owner.addWeightReduction(-3);
				break;
			case 8:
				_owner.addWeightReduction(-4);
				break;
			case 9:
				_owner.addWeightReduction(-5);
				break;
			default:
				break;
			}
		}
		/** 격분의 장갑 **/
		if (itemId == 222317) {
			switch (itemlvl) {
			case 7:
				_owner.addDmgRate(-4);
				_owner.addDmgup(-1);
				break;
			case 8:
				_owner.addDmgRate(-5);
				_owner.addDmgup(-2);
				break;
			case 9:
				_owner.addDmgRate(-6);
				_owner.addDmgup(-3);
				break;
			case 10:
				_owner.addDmgRate(-6);
				_owner.addDmgup(-3);
				break;
			default:
				break;
			}
		}
		
		/** 빛나는 아덴 용사의 견갑 **/
		if (itemId == 900121) {
			switch (itemlvl) {
			case 6:
				_owner.addDmgup(-1);
				_owner.addBowDmgup(-1);
				_owner.getAbility().addSp(-1);
				_owner.addBowHitup(-1);
				_owner.addDmgRate(-1);
				_owner.addBaseMagicHitUp(-1);// 마법 적중
				break;
			case 7:
				_owner.addDmgup(-2);
				_owner.addBowDmgup(-2);
				_owner.getAbility().addSp(-2);
				_owner.addBowHitup(-2);
				_owner.addDmgRate(-2);
				_owner.addBaseMagicHitUp(-2);// 마법 적중
				_owner.addDamageReductionByArmor(-1);//대미지 리덕
				break;
			case 8:
				_owner.addDmgup(-3);
				_owner.addBowDmgup(-3);
				_owner.getAbility().addSp(-3);
				_owner.addBowHitup(-3);
				_owner.addDmgRate(-3);
				_owner.addBaseMagicHitUp(-3);// 마법 적중
				_owner.addDamageReductionByArmor(-2);//대미지 리덕
				break;
			case 9:
				_owner.addDmgup(-3);
				_owner.addBowDmgup(-3);
				_owner.getAbility().addSp(-3);
				_owner.addBowHitup(-3);
				_owner.addDmgRate(-3);
				_owner.addBaseMagicHitUp(-3);// 마법 적중
				_owner.addDamageReductionByArmor(-3);//대미지 리덕
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				_owner.addDmgup(-3);
				_owner.addBowDmgup(-3);
				_owner.getAbility().addSp(-3);
				_owner.addBowHitup(-3);
				_owner.addDmgRate(-3);
				_owner.addBaseMagicHitUp(-3);// 마법 적중
				_owner.addDamageReductionByArmor(-3);//대미지 리덕
				break;
			default:
				break;
			}
		}
		/** 머미로드의 왕관 **/
		if (itemId == 20017) {
			switch (itemlvl) {
			case 5:
				_owner.addBowDmgup(-1);
				_owner.getResistance().addMr(-3);//개별 마방 적용
				break;
			case 6:
				_owner.addBowDmgup(-2);
				_owner.getResistance().addMr(-6);//개별 마방 적용
				break;
			case 7:
				_owner.addBowDmgup(-3);
				_owner.getResistance().addMr(-9);//개별 마방 적용
				break;
			case 8:
				_owner.addBowDmgup(-4);
				_owner.getResistance().addMr(-12);//개별 마방 적용
				break;
			case 9:
				_owner.addBowDmgup(-5);
				_owner.getResistance().addMr(-15);//개별 마방 적용
				break;
			case 10:
				_owner.addBowDmgup(-6);
				_owner.getResistance().addMr(-18);//개별 마방 적용
				break;
			default:
				break;
			}
		}
		//TODO (장신구) 목걸이 인챈트 리뉴얼
		if (itemtype == 8) {
			if ((itemgrade >= 0 && itemgrade <= 2) && (itemtype == 8)) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-5);
					break;
				case 2:
					_owner.addMaxHp(-10);
					break;
				case 3:
					_owner.addMaxHp(-20);
					break;
				case 4:
					_owner.addMaxHp(-30);
					break;
				case 5:
					_owner.setAccessoryHeal(-1);
					_owner.addMaxHp(-40);
					_owner.getResistance().addMr(-1);
					break;
				case 6:
					_owner.setAccessoryHeal(-2);
					_owner.addMaxHp(-40);
					_owner.getResistance().addMr(-3);
					break;
				case 7:
					_owner.setAccessoryHeal(-3);
					_owner.addMaxHp(-50);
					_owner.getResistance().addMr(-5);
					_owner.addSpecialResistance(eKind.ABILITY, -2);
					break;
				case 8:
					_owner.setAccessoryHeal(-4);
					_owner.addMaxHp(-50);
					_owner.getResistance().addMr(-7);
					_owner.addSpecialResistance(eKind.ABILITY, -3);
					break;
				case 9:
					_owner.setAccessoryHeal(-5);
					_owner.addMaxHp(-60);
					_owner.getResistance().addMr(-10);
					_owner.addSpecialResistance(eKind.ABILITY, -4);
					break;
				}
			}
		}
		//TODO (장신구) 목걸이,귀걸이 인챈트 리뉴얼
		if (itemtype >= 8 && itemtype <= 12) {
			if ((itemgrade >= 0 && itemgrade <= 2) && (itemtype == 8 || itemtype == 12)) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-5);
					break;
				case 2:
					_owner.addMaxHp(-10);
					break;
				case 3:
					_owner.addMaxHp(-20);
					break;
				case 4:
					_owner.addMaxHp(-30);
					break;
				case 5:
					_owner.setAccessoryHeal(-1);
					_owner.addMaxHp(-40);
					break;
				case 6:
					_owner.setAccessoryHeal(-2);
					_owner.addMaxHp(-40);
					break;
				case 7:
					_owner.setAccessoryHeal(-3);
					_owner.addMaxHp(-50);
					_owner.addSpecialResistance(eKind.ABILITY, -2);
					break;
				case 8:
					_owner.setAccessoryHeal(-4);
					_owner.addMaxHp(-50);
					_owner.addSpecialResistance(eKind.ABILITY, -3);
					break;
				case 9:
					_owner.setAccessoryHeal(-5);
					_owner.addMaxHp(-60);
					_owner.addSpecialResistance(eKind.ABILITY, -4);
					break;
				}
				// TODO (장신구) 반지 인챈트 리뉴얼
			} else if (((itemgrade >= 0 && itemgrade <= 2) || itemgrade == 5) && (itemtype == 9 || itemtype == 11)) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-5);
					break;
				case 2:
					_owner.addMaxHp(-10);
					break;
				case 3:
					_owner.addMaxHp(-20);
					break;
				case 4:
					_owner.addMaxHp(-30);
					break;
				case 5:
					_owner.addDmgup(-1);
					_owner.addBowDmgup(-1);
					_owner.addMaxHp(-40);
					break;
				case 6:
					_owner.addDmgup(-2);
					_owner.addBowDmgup(-2);
					_owner.addMaxHp(-40);
					_owner.getResistance().addMr(-1);
					break;
				case 7:
					_owner.addDmgup(-3);
					_owner.addBowDmgup(-3);
					_owner.addMaxHp(-50);
					_owner.getAbility().addSp(-1);
					_owner.getResistance().addMr(-3);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addDmgup(-4);
					_owner.addBowDmgup(-4);
					_owner.addMaxHp(-50);
					_owner.getAbility().addSp(-2);
					_owner.getResistance().addMr(-5);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				case 9:
					_owner.addDmgup(-5);
					_owner.addBowDmgup(-5);
					_owner.addMaxHp(-60);
					_owner.getAbility().addSp(-3);
					_owner.getResistance().addMr(-7);
					_owner.getResistance().addPVPweaponTotalDamage(-3);
					break;
				}
				// TODO (장신구) 벨트 인챈트 리뉴얼
			} else if ((itemgrade >= 0 && itemgrade <= 2) && (itemtype == 10)) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxMp(-5);
					break;
				case 2:
					_owner.addMaxMp(-10);
					break;
				case 3:
					_owner.addMaxMp(-20);
					break;
				case 4:
					_owner.addMaxMp(-30);
					break;
				case 5:
					_owner.addDamageReductionByArmor(-1);
					_owner.addMaxMp(-40);
					break;
				case 6:
					_owner.addDamageReductionByArmor(-2);
					_owner.addMaxMp(-40);
					_owner.addMaxHp(-20);
					_owner.getResistance().addcalcPcDefense(-1);
					break;
				case 7:
					_owner.addDamageReductionByArmor(-3);
					_owner.addMaxMp(-50);
					_owner.addMaxHp(-30);
					_owner.getResistance().addcalcPcDefense(-3);
					break;
				case 8:
					_owner.addDamageReductionByArmor(-4);
					_owner.addMaxMp(-50);
					_owner.addMaxHp(-40);
					_owner.getResistance().addcalcPcDefense(-5);
					break;
				case 9:
					_owner.addDamageReductionByArmor(-5);
					_owner.addMaxMp(-50);
					_owner.addMaxHp(-50);
					_owner.getResistance().addcalcPcDefense(-7);
					break;
				}
				// 스냅퍼의 반지 착용해제 부분
			} else if (itemgrade == 3 && itemId == 22224 || itemId == 22225) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-25);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(3);
					break;
				case 5:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-1);
					break;
				case 6:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-2);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
			} else if (itemgrade == 3 && itemId == 22227) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-25);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(3);
					break;
				case 5:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-1);
					break;
				case 6:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-2);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
				// 스냅퍼의 마법저항 반지 착용해제
			} else if (itemgrade == 3 && itemId == 22228) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-25);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(3);
					break;
				case 5:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-1);
					break;
				case 6:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-2);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
			} else if (itemgrade == 3 && itemId == 22226) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-25);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(3);
					break;
				case 5:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-1);
					break;
				case 6:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-2);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					_owner.addDamageReductionByArmor(-1);
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					_owner.addDamageReductionByArmor(-2);
					break;
				default:
					break;
				}	
			} else if (itemgrade == 3 && itemId == 222290) { // 지혜의 반지
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-5);
					_owner.addMaxMp(-15);
					break;
				case 2:
					_owner.addMaxHp(-10);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-15);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-20);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(3);
					break;
				case 5:
					_owner.addMaxHp(-25);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(3);
					_owner.getAbility().addSp(-1);
					break;
				case 6:
					_owner.addMaxHp(-30);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(4);
					_owner.getAbility().addSp(-2);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-35);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(4);
					_owner.getAbility().addSp(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-40);
					_owner.addMaxMp(-30);
					_owner.getAC().addAc(5);
					_owner.getAbility().addSp(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
			} else if (itemgrade == 3 && itemId == 222291) { // 용사의 반지
				switch (itemlvl) {
				case 1:
					_owner.getAC().addAc(1);
					break;
				case 2:
					_owner.getAC().addAc(2);
					break;
				case 3:
					_owner.addMaxHp(-5);
					_owner.getAC().addAc(3);
					break;
				case 4:
					_owner.addMaxHp(-10);
					_owner.getAC().addAc(4);
					break;
				case 5:
					_owner.addMaxHp(-15);
					_owner.getAC().addAc(4);
					_owner.addHitup(-1);
					_owner.addDmgup(-1);
					break;
				case 6:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(4);
					_owner.addHitup(-2);
					_owner.addDmgup(-2);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-25);
					_owner.getAC().addAc(4);
					_owner.addHitup(-3);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(4);
					_owner.addHitup(-4);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 체력 반지 **/
			} else if (itemgrade == 3 && itemId == 222332) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-1);
					break;
				case 5:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-2);
					break;
				case 6:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					_owner.addDamageReductionByArmor(-1);
					break;
				case 7:
					_owner.addMaxHp(-55);
					_owner.getAC().addAc(5);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					_owner.addDamageReductionByArmor(-2);
					break;
				case 8:
					_owner.addMaxHp(-65);
					_owner.getAC().addAc(5);
					_owner.addDmgup(-5);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					_owner.addDamageReductionByArmor(-3);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 회복 반지/축복받은 스냅퍼의 집중 반지 **/
			} else if (itemgrade == 3 && itemId == 222330 || itemId == 222331) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-1);
					break;
				case 5:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-2);
					break;
				case 6:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-5);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 마나 반지 **/
			} else if (itemgrade == 3 && itemId == 222333) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(1);
					break;
				case 5:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-2);
					break;
				case 6:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-5);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 마법 저항 반지 **/
			} else if (itemgrade == 3 && itemId == 222334) {
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-15);
					break;
				case 2:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-35);
					_owner.getAC().addAc(3);
					_owner.addDmgup(-1);
					break;
				case 5:
					_owner.addMaxHp(-40);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-2);
					break;
				case 6:
					_owner.addMaxHp(-45);
					_owner.getAC().addAc(4);
					_owner.addDmgup(-3);
					_owner.getResistance().addMr(-1);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(5);
					_owner.addDmgup(-4);
					_owner.getResistance().addMr(-2);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.getAC().addAc(5);
					_owner.addDmgup(-5);
					_owner.getResistance().addMr(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 지혜 반지 **/
			} else if (itemgrade == 3 && itemId == 222335) {
				switch (itemlvl) {
				case 0:
					_owner.addMaxMp(-15);
					break;
				case 1:
					_owner.addMaxHp(-5);
					_owner.addMaxMp(-15);
					break;
				case 2:
					_owner.addMaxHp(-10);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(1);
					break;
				case 3:
					_owner.addMaxHp(-20);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(2);
					break;
				case 4:
					_owner.addMaxHp(-25);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(3);
					_owner.getAbility().addSp(-1);
					break;
				case 5:
					_owner.addMaxHp(-30);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(3);
					_owner.getAbility().addSp(-2);
					break;
				case 6:
					_owner.addMaxHp(-35);
					_owner.addMaxMp(-15);
					_owner.getAC().addAc(3);
					_owner.getAbility().addSp(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					_owner.addBaseMagicHitUp(-1);// 마법 적중
					break;
				case 7:
					_owner.addMaxHp(-40);
					_owner.addMaxMp(-30);
					_owner.getAC().addAc(4);
					_owner.getAbility().addSp(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					_owner.addBaseMagicHitUp(-2);// 마법 적중
					break;
				case 8:
					_owner.addMaxHp(-50);
					_owner.addMaxMp(-35);
					_owner.getAC().addAc(4);
					_owner.getAbility().addSp(-5);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					_owner.addBaseMagicHitUp(-3);// 마법 적중
					break;
				default:
					break;
				}
				/** 축복받은 스냅퍼의 용사 반지 **/
			} else if (itemgrade == 3 && itemId == 222336) {
				switch (itemlvl) {
				case 1:
					_owner.getAC().addAc(1);
					break;
				case 2:
					_owner.getAC().addAc(2);
					break;
				case 3:
					_owner.addMaxHp(-10);
					_owner.getAC().addAc(3);
					break;
				case 4:
					_owner.addMaxHp(-15);
					_owner.getAC().addAc(4);
					_owner.addHitup(-1);
					_owner.addDmgup(-1);
					break;
				case 5:
					_owner.addMaxHp(-20);
					_owner.getAC().addAc(4);
					_owner.addHitup(-2);
					_owner.addDmgup(-2);
					break;
				case 6:
					_owner.addMaxHp(-25);
					_owner.getAC().addAc(4);
					_owner.addHitup(-3);
					_owner.addDmgup(-3);
					_owner.addSpecialResistance(eKind.ABILITY, -5);
					break;
				case 7:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(5);
					_owner.addHitup(-4);
					_owner.addDmgup(-4);
					_owner.addSpecialResistance(eKind.ABILITY, -7);
					_owner.getResistance().addPVPweaponTotalDamage(-1);
					break;
				case 8:
					_owner.addMaxHp(-30);
					_owner.getAC().addAc(5);
					_owner.addHitup(-5);
					_owner.addDmgup(-5);
					_owner.addSpecialResistance(eKind.ABILITY, -9);
					_owner.getResistance().addPVPweaponTotalDamage(-2);
					break;
				default:
					break;
				}
			} else if(itemId == 22229){
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-20);
					break;
				case 2:
					_owner.addMaxHp(-30);
					break;
				case 3:
					_owner.addMaxHp(-40);
					_owner.addDamageReductionByArmor(-1);
					break;
				case 4:
					_owner.addMaxHp(-50);
					_owner.addDamageReductionByArmor(-1);
					break;
				case 5:
					_owner.addMaxHp(-60);
					_owner.addDamageReductionByArmor(-2);
					break;
				case 6:
					_owner.addMaxHp(-70);
					_owner.addDamageReductionByArmor(-3);
					_owner.getAC().addAc(7);
					break;
				case 7:
					_owner.addBowHitup(-1);
					_owner.addHitup(-1);
					_owner.addMaxHp(-80);
					_owner.addDamageReductionByArmor(-4);
					_owner.getAC().addAc(8);
					break;
				case 8:
					_owner.addBowHitup(-3);
					_owner.addHitup(-3);
					_owner.addMaxHp(-90);
					_owner.addDamageReductionByArmor(-5);
					_owner.getAC().addAc(9);
					break;
				default:
					break;
				}
			} else if(itemId == 222337){
				switch (itemlvl) {
				case 1:
					_owner.addMaxHp(-20);
					break;
				case 2:
					_owner.addMaxHp(-30);
					break;
				case 3:
					_owner.addMaxHp(-50);
					_owner.addDamageReductionByArmor(-1);
					break;
				case 4:
					_owner.addMaxHp(-60);
					_owner.addDamageReductionByArmor(-2);
					break;
				case 5:
					_owner.addMaxHp(-70);
					_owner.addDamageReductionByArmor(-3);
					_owner.getAC().addAc(7);
					break;
				case 6:
					_owner.addBowHitup(-1);
					_owner.addHitup(-1);
					_owner.addMaxHp(-80);
					_owner.addDamageReductionByArmor(-4);
					_owner.getAC().addAc(8);
					break;
				case 7:
					_owner.addBowHitup(-3);
					_owner.addHitup(-3);
					_owner.addMaxHp(-90);
					_owner.addDamageReductionByArmor(-5);
					_owner.getAC().addAc(9);
					break;
				case 8:
					_owner.addBowHitup(-5);
					_owner.addHitup(-5);
					_owner.addMaxHp(-150);
					_owner.addDamageReductionByArmor(-6);
					_owner.getAC().addAc(10);
					break;
				default:
					break;
				}
			} else if(itemId == 22231){
				switch (itemlvl) {
				case 1:
					_owner.addMaxMp(-10);
					_owner.getResistance().addMr(-3);
					break;
				case 2:
					_owner.addMaxMp(-15);
					_owner.getResistance().addMr(-4);
					break;
				case 3:
					_owner.addMaxMp(-30);
					_owner.getResistance().addMr(-5);
					_owner.getAbility().addSp(-1);
					break;
				case 4:
					_owner.addMaxMp(-35);
					_owner.getResistance().addMr(-6);
					_owner.getAbility().addSp(-1);
					break;
				case 5:
					_owner.addMaxMp(-50);
					_owner.getResistance().addMr(-7);
					_owner.getAbility().addSp(-2);
					break;
				case 6:
					_owner.addMaxMp(-55);
					_owner.getResistance().addMr(-8);
					_owner.getAbility().addSp(-2);
					_owner.getAC().addAc(1);
					break;
				case 7:
					_owner.addBaseMagicHitUp(-1);// 마법 적중
					_owner.addMaxMp(-70);
					_owner.getResistance().addMr(-10);
					_owner.getAbility().addSp(-3);
					_owner.getAC().addAc(2);
					break;
				case 8:
					_owner.addBaseMagicHitUp(-3);// 마법 적중
					_owner.addMaxMp(-95);
					_owner.getResistance().addMr(-13);
					_owner.getAbility().addSp(-3);
					_owner.getAC().addAc(3);
					break;
				default:
					break;
				}
			} else if(itemId == 222339){
				switch (itemlvl) {
				case 0:
					_owner.addMaxMp(-10);
					_owner.getResistance().addMr(-2);
					break;
				case 1:
					_owner.addMaxMp(-10);
					_owner.getResistance().addMr(-3);
					break;
				case 2:
					_owner.addMaxMp(-15);
					_owner.getResistance().addMr(-4);
					break;
				case 3:
					_owner.addMaxMp(-35);
					_owner.getResistance().addMr(-6);
					_owner.getAbility().addSp(-1);
					break;
				case 4:
					_owner.addMaxMp(-50);
					_owner.getResistance().addMr(-7);
					_owner.getAbility().addSp(-2);
					break;
				case 5:
					_owner.addMaxMp(-55);
					_owner.getResistance().addMr(-8);
					_owner.getAbility().addSp(-2);
					_owner.getAC().addAc(1);
					break;
				case 6:
					_owner.addBaseMagicHitUp(-1);// 마법 적중
					_owner.addMaxMp(-70);
					_owner.getResistance().addMr(-10);
					_owner.getAbility().addSp(-3);
					_owner.getAC().addAc(2);
					break;
				case 7:
					_owner.addBaseMagicHitUp(-3);// 마법 적중
					_owner.addMaxMp(-95);
					_owner.getResistance().addMr(-13);
					_owner.getAbility().addSp(-3);
					_owner.getAC().addAc(3);
					break;
				case 8:
					_owner.addBaseMagicHitUp(-5);// 마법 적중
					_owner.addMaxMp(-125);
					_owner.getResistance().addMr(-18);
					_owner.getAbility().addSp(-4);
					_owner.getAC().addAc(4);
					break;
				default:
					break;
				}
				/** 검귀 ac처리부분 효과 재수정 **/
			} else if (itemId == 222340 || itemId == 222341) {
				int ac = itemlvl;

				if (item.getBless() == 0 && itemlvl >= 3) {
					ac += 1;
				}
				_owner.getAC().addAc(ac);
				int dm = itemlvl - 2;

				if (item.getBless() != 0 && itemlvl >= 4)
					dm -= 1;
				
				_owner.addDmgup(-dm);
				_owner.addBowDmgup(-dm);
				
			} else if (itemgrade == 4 && itemId == 22230) { // 룸티스의 푸른빛 귀걸이
				switch (itemlvl) {
				case 5:
					_owner.getAC().addAc(1);
					break;
				case 6:
					_owner.getAC().addAc(2);
					break;
				case 7:
					_owner.getAC().addAc(2);
					break;
				case 8:
					_owner.getAC().addAc(3);
					break;
				default:
					break;
				}
			} else if (itemgrade == 4 && itemId == 222338) { // 축복받은 룸티스의 푸른빛
				// 귀걸이
				switch (itemlvl) {
				case 4:
					_owner.getAC().addAc(1);
					break;
				case 5:
					_owner.getAC().addAc(2);
					break;
				case 6:
					_owner.getAC().addAc(2);
					break;
				case 7:
					_owner.getAC().addAc(3);
					break;
				case 8:
					_owner.getAC().addAc(4);
					break;
				default:
					break;
				}
			}
		}
		armor.stopEquipmentTimer(_owner);
		_armors.remove(armor);
	}

	public void set(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();

		if (item.getType2() == 0) {
			return;
		}
		 
		if(item.getItemId() == 900116){ // 고대의 룬
			int itemlvl = equipment.getEnchantLevel();
			 switch (itemlvl) {
			   case 0:
			//    _owner.getAC().addAc(0);
			    break;
			   case 1:
			//    _owner.getAC().addAc(-1);
			    break;
			   case 2:
			//    _owner.getAC().addAc(-2);
			    break;
			   case 3:
			//    _owner.getAC().addAc(-3);
			    break;
			   case 4:
			//    _owner.getAC().addAc(-4);
			    break;
			   case 5:
		//	    _owner.getAC().addAc(-5);
			    break;
			   case 6:
		//	    _owner.getAC().addAc(-6);
			    _owner.addDmgRate(4); // 근뎀
			    _owner.addHitup(4);// 근거리 명중
			    _owner.addBowDmgRate(4); // 원뎀
			    _owner.addBowHitup(4); // 원거리 명중
			    _owner.getAbility().addSp(4); // sp
			    _owner.addBaseMagicHitUp(4);// 마법 적중
			    _owner.getResistance().addMr(4); // mr
			    break;
			   case 7:
			//    _owner.getAC().addAc(-7);
			    _owner.addDmgRate(5); // 근뎀
			    _owner.addHitup(6);// 근거리 명중
			    _owner.addBowDmgRate(5); // 원뎀
			    _owner.addBowHitup(6); // 원거리 명중
			    _owner.getAbility().addSp(5); // sp
			    _owner.addBaseMagicHitUp(6);// 마법 적중
			    _owner.getResistance().addMr(6); // mr
			    break;
			   case 8:
			//    _owner.getAC().addAc(-8);
			    _owner.addDmgRate(6); // 근뎀
			    _owner.addHitup(8);// 근거리 명중
			    _owner.addBowDmgRate(6); // 원뎀
			    _owner.addBowHitup(8); // 원거리 명중
			    _owner.getAbility().addSp(6); // sp
			    _owner.addBaseMagicHitUp(8);// 마법 적중
			    _owner.getResistance().addMr(8); // mr
			    break;
			   default:
					break;
			   }
			  }
		
		/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/
		_owner.addEinhasadBlessper(item.get_addeinhasadper());
		_owner.addMaxHp(item.get_addhp());
		_owner.addMaxMp(item.get_addmp());
		_owner.getAbility().addAddedStr(item.get_addstr());
		_owner.getAbility().addAddedCon(item.get_addcon());
		_owner.getAbility().addAddedDex(item.get_adddex());
		_owner.getAbility().addAddedInt(item.get_addint());
		_owner.getAbility().addAddedWis(item.get_addwis());
		_owner.add_missile_critical_rate(item.get_missile_critical_probability());
		_owner.add_melee_critical_rate(item.get_melee_critical_probability());
		
		if (item.get_addwis() != 0) {
			_owner.resetBaseMr();
		}
		_owner.getAbility().addAddedCha(item.get_addcha());

		int addMr = 0;
		addMr += equipment.getMr();
		if (item.getItemId() == 20236 && _owner.isElf()) {
			addMr += 5;
		}
		if (addMr != 0) {
			_owner.getResistance().addMr(addMr);
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.get_addsp() != 0) {
			_owner.getAbility().addSp(item.get_addsp());
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.isHasteItem()) {
			_owner.addHasteItemEquipped(1);
			_owner.removeHasteSkillEffect();
			if (_owner.getMoveSpeed() != 1) {
				_owner.setMoveSpeed(1);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 1, -1));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 1, 0));
			}
		}
		if (item.getItemId() == 20383) {
			if (_owner.hasSkillEffect(STATUS_BRAVE)) {
				_owner.killSkillEffectTimer(STATUS_BRAVE);
				_owner.sendPackets(new S_SkillBrave(_owner.getId(), 0, 0));
				_owner.broadcastPacket(new S_SkillBrave(_owner.getId(), 0, 0));
				_owner.setBraveSpeed(0);
			}
		}
		_owner.getEquipSlot().setMagicHelm(equipment);

		if (item.getType2() == 1) {
			setWeapon(equipment);
		} else if (item.getType2() == 2) {
			setArmor(equipment);
			_owner.sendPackets(new S_SPMR(_owner));
		}
		ItemPresentatorFactory.do_equip(_owner, equipment);
		SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		//_owner.sendPackets(String.format("%s을(를) 착용 하였습니다.", equipment.getLogName()));
	}

	public void remove(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();
		if (item.getType2() == 0) {
			return;
		}

		if(item.getType() == 7){
			if(_owner.hasSkillEffect(L1SkillId.BLOW_ATTACK)){
				_owner.removeSkillEffect(L1SkillId.BLOW_ATTACK);
			}
		}
		
		if(item.getItemId() == 900116){ // 고대의 룬
			int itemlvl = equipment.getEnchantLevel();
			 switch (itemlvl) {
			   case 0:
			 //   _owner.getAC().addAc(0);
			    break;
			   case 1:
			//    _owner.getAC().addAc(1);
			    break;
			   case 2:
			//    _owner.getAC().addAc(2);
			    break;
			   case 3:
		//	    _owner.getAC().addAc(3);
			    break;
			   case 4:
		//	    _owner.getAC().addAc(4);
			    break;
			   case 5:
	//		    _owner.getAC().addAc(5);
			    break;
			   case 6:
		//	    _owner.getAC().addAc(6);
			    _owner.addDmgRate(-4); // 근뎀
			    _owner.addHitup(-4);// 근거리 명중
			    _owner.addBowDmgRate(-4); // 원뎀
			    _owner.addBowHitup(-4); // 원거리 명중
			    _owner.getAbility().addSp(-4); // sp
			    _owner.addBaseMagicHitUp(-4);// 마법 적중
			    _owner.getResistance().addMr(-4); // mr
			    break;
			   case 7:
			//    _owner.getAC().addAc(7);
			    _owner.addDmgRate(-5); // 근뎀
			    _owner.addHitup(-6);// 근거리 명중
			    _owner.addBowDmgRate(-5); // 원뎀
			    _owner.addBowHitup(-6); // 원거리 명중
			    _owner.getAbility().addSp(-5); // sp
			    _owner.addBaseMagicHitUp(-6);// 마법 적중
			    _owner.getResistance().addMr(-6); // mr
			    break;
			   case 8:
			 //   _owner.getAC().addAc(8);
			    _owner.addDmgRate(-6); // 근뎀
			    _owner.addHitup(-8);// 근거리 명중
			    _owner.addBowDmgRate(-6); // 원뎀
			    _owner.addBowHitup(-8); // 원거리 명중
			    _owner.getAbility().addSp(-6); // sp
			    _owner.addBaseMagicHitUp(-8);// 마법 적중
			    _owner.getResistance().addMr(-8); // mr
			    break;
			   default:
					break;
			   }
			  }
		
		/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/
		_owner.addEinhasadBlessper(-item.get_addeinhasadper());
		_owner.addMaxHp(-item.get_addhp());
		_owner.addMaxMp(-item.get_addmp());
		_owner.getAbility().addAddedStr((byte) -item.get_addstr());
		_owner.getAbility().addAddedCon((byte) -item.get_addcon());
		_owner.getAbility().addAddedDex((byte) -item.get_adddex());
		_owner.getAbility().addAddedInt((byte) -item.get_addint());
		_owner.getAbility().addAddedWis((byte) -item.get_addwis());
		_owner.add_missile_critical_rate(-item.get_missile_critical_probability());
		_owner.add_melee_critical_rate(-item.get_melee_critical_probability());
		if (item.get_addwis() != 0) {
			_owner.resetBaseMr();
		}
		_owner.getAbility().addAddedCha((byte) -item.get_addcha());

		int addMr = 0;
		addMr -= equipment.getMr();
		if (item.getItemId() == 20236 && _owner.isElf()) {
			addMr -= 5;
		}
		if (addMr != 0) {
			_owner.getResistance().addMr(addMr);
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.get_addsp() != 0) {
			_owner.getAbility().addSp(-item.get_addsp());
			_owner.sendPackets(new S_SPMR(_owner));
		}
		if (item.isHasteItem()) {
			_owner.addHasteItemEquipped(-1);
			if (_owner.getHasteItemEquipped() == 0) {
				_owner.setMoveSpeed(0);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 0, 0));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 0, 0));
			}
		}
		_owner.getEquipSlot().removeMagicHelm(_owner.getId(), equipment);

		if (item.getType2() == 1) {
			removeWeapon(equipment);
			_owner.remove_elf_second_brave();
		} else if (item.getType2() == 2) {
			removeArmor(equipment);
		}
		ItemPresentatorFactory.do_unequip(_owner, equipment);
		SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		//_owner.sendPackets(String.format("%s을(를) 해제 하였습니다.", equipment.getLogName()));
	}

	public void setMagicHelm(L1ItemInstance item) {
		if (item.getItemId() == 20013) {
			_owner.sendPackets(new S_AddSkill(0, 0, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0));
			_owner.setSkillMastery(26);
			_owner.setSkillMastery(43);
		}
		if (item.getItemId() == 20014) {
			_owner.sendPackets(new S_AddSkill(1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0));
			_owner.setSkillMastery(1);
			_owner.setSkillMastery(19);
		}
		if (item.getItemId() == 20015) {
			_owner.sendPackets(new S_AddSkill(0, 24, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0));
			_owner.setSkillMastery(12);
			_owner.setSkillMastery(13);
			_owner.setSkillMastery(42);
		}
		if (item.getItemId() == 20008) {
			_owner.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0));
			_owner.setSkillMastery(43);
		}
		if (item.getItemId() == 20023) {
			_owner.sendPackets(new S_AddSkill(0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0));
			_owner.setSkillMastery(54);
		}
	}

	public void removeMagicHelm(int objectId, L1ItemInstance item) {
		if (item.getItemId() == 20013) {
			if (!SkillsTable.getInstance().spellCheck(objectId, 26)) {
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(26);
				
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, 43)) {
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(43);
			}
		}
		if (item.getItemId() == 20014) {
			if (!SkillsTable.getInstance().spellCheck(objectId, 1)) {
				_owner.sendPackets(new S_DelSkill(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(1);
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, 19)) {
				_owner.sendPackets(new S_DelSkill(0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(19);
			}
		}
		if (item.getItemId() == 20015) {
			if (!SkillsTable.getInstance().spellCheck(objectId, 12)) {
				_owner.sendPackets(new S_DelSkill(0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(12);
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, 13)) {
				_owner.sendPackets(new S_DelSkill(0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(13);
			}
			if (!SkillsTable.getInstance().spellCheck(objectId, 42)) {
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(42);
			}
		}
		if (item.getItemId() == 20008) {
			if (!SkillsTable.getInstance().spellCheck(objectId, 43)) {
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(43);
			}
		}
		if (item.getItemId() == 20023) {
			if (!SkillsTable.getInstance().spellCheck(objectId, 54)) {
				_owner.sendPackets(new S_DelSkill(0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0));
				_owner.removeSkillMastery(54);
			}
		}
	}

	/**
	 * 셋트 아이템 해제
	 * 
	 * @param itemId
	 */
	public void removeSetItems(int itemId) {
		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && !armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}
	}
}
