package l1j.server.server.model.item.function;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;

import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_20_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_20_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_21_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_21_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_23_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_23_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_3_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_3_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_4_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_4_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_5_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_5_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_9_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_9_S;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_12;
import static l1j.server.server.model.skill.L1SkillId.나루터감사캔디;
import static l1j.server.server.model.skill.L1SkillId.메티스정성스프;
import static l1j.server.server.model.skill.L1SkillId.메티스정성요리;
import static l1j.server.server.model.skill.L1SkillId.수련자의닭고기스프;
import static l1j.server.server.model.skill.L1SkillId.수련자의연어찜;
import static l1j.server.server.model.skill.L1SkillId.수련자의칠면조구이;
import static l1j.server.server.model.skill.L1SkillId.수련자의한우스테이크;
import static l1j.server.server.model.skill.L1SkillId.천하장사버프;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Item;

public class 메티스스프6
  extends L1ItemInstance
{
  public 메티스스프6(L1Item item)
  {
    super(item);
  }
  
  public static void checkCondition(L1PcInstance pc, L1ItemInstance item)
  {
    int itemId = item.getItemId();
    if (pc.hasSkillEffect(71))
    {
      pc.sendPackets(new S_ServerMessage(698), true);
      return;
    }
    if ((pc.hasSkillEffect(157)) || 
      (pc.hasSkillEffect(33)) || 
      (pc.hasSkillEffect(10101)) || 
      (pc.hasSkillEffect(30007)) || 
      (pc.hasSkillEffect(30002)) || 
      (pc.hasSkillEffect(30006)) || 
      (pc.hasSkillEffect(40007)) || 
      (pc.hasSkillEffect(30081)) || 
      (pc.hasSkillEffect(22055)) || 
      (pc.hasSkillEffect(22025)) || 
      (pc.hasSkillEffect(22026)) || 
      (pc.hasSkillEffect(22027)) || 
      (pc.hasSkillEffect(30004)) || 
      (pc.hasSkillEffect(87)) || 
      (pc.hasSkillEffect(208)) || 
      (pc.hasSkillEffect(103)) || 
      (pc.hasSkillEffect(212)) || 
      (pc.hasSkillEffect(123))) {
      return;
    }
    메티스스프11(pc, itemId);
    pc.getInventory().removeItem(item, 1);
  }
  private int _skillId;
  private static void 메티스스프11(L1PcInstance pc, int item_id)
  {
    pc.cancelAbsoluteBarrier();
    pc.cancelMoebius();

	
//		int cookingId = pc.getCookingId();
//		if (cookingId != 3000129) {
//			pc.removeSkillEffect(cookingId);
////			return;
//		
//	}
    int[] skillIds = null;
    
    skillIds = new int[] {COOKING_1_0_N, COOKING_1_1_N , COOKING_1_2_N, COOKING_1_3_N
    		,COOKING_1_4_N,COOKING_1_5_N, COOKING_1_6_N, COOKING_1_0_S
    		,COOKING_1_1_S, COOKING_1_2_S,COOKING_1_3_S,COOKING_1_4_S ,COOKING_1_5_S,COOKING_1_6_S
    		,COOKING_1_8_N,COOKING_1_9_N,COOKING_1_10_N,COOKING_1_11_N,COOKING_1_12_N,COOKING_1_13_N,COOKING_1_14_N
    		,COOKING_1_8_S,COOKING_1_9_S,COOKING_1_10_S,COOKING_1_11_S,COOKING_1_12_S,COOKING_1_13_S,COOKING_1_14_S
    ,COOKING_1_16_N,COOKING_1_17_N,COOKING_1_18_N,COOKING_1_19_N,COOKING_1_20_N,COOKING_1_21_N,COOKING_1_22_N
    ,COOKING_1_16_S,COOKING_1_17_S,COOKING_1_18_S,COOKING_1_19_S,COOKING_1_20_S,COOKING_1_21_S,COOKING_1_22_S
    ,COOK_STR,COOK_DEX,COOK_INT,메티스정성요리,COOK_STR_12,COOK_INT_1,COOK_DEX_1
    ,COOK_STR_1
    };
   
    
    for (int i = 0; i < skillIds.length; i++) {
    	if (pc.hasSkillEffect(skillIds[i])) {
    		pc.removeSkillEffect(skillIds[i]);
    	} 
    	}
    	
   /* if (pc.hasSkillEffect(skillIds[i])) {
		pc.removeSkillEffect(skillIds[i]);
	} */
    
    eatCooking(pc, L1SkillId.COOK_INT_1, 1800);
    pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));
//		switch (item_id) {
//		case 3100129:
//			eatCooking(pc, L1SkillId.메티스정성스프, 10);
//			break;
//		case 30051:
//			eatCooking(pc, L1SkillId.COOK_STR, 10);
//			break;
//		case 4100156:
//			eatCooking(pc, L1SkillId.COOK_STR_1, 10);
//			break;
//		default:
//			break;
//		}
    
  }
  
  public static void eatCooking(L1PcInstance pc, int cookingId, int time) {
		int cookingType = 0;
		
		switch(cookingId){
		
		case 수련자의한우스테이크:
			pc.setDessertId(cookingId);
			cookingType = 157;
			pc.addHitup(1);
			pc.addDmgup(2);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case 수련자의연어찜:
			pc.setDessertId(cookingId);
			cookingType = 158;
			pc.addBowHitup(1);
			pc.addBowDmgup(2);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case 수련자의칠면조구이:
			pc.setDessertId(cookingId);
			cookingType = 159;
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getAbility().addSp(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case 수련자의닭고기스프: cookingType = 160; break;
		case COOKING_1_0_N:
		case COOKING_1_0_S:
			cookingType = 0;
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case 나루터감사캔디:
			if (pc.getLevel() >= 1 && pc.getLevel() <= 60){
				pc.getAbility().addStr(7);
				pc.getAbility().addDex(7);
			} else {
				pc.getAbility().addStr(6);
				pc.getAbility().addDex(6);
			}
			pc.sendPackets(new S_OwnCharStatus(pc));
			break;
		case COOKING_1_1_N:
		case COOKING_1_1_S:
			cookingType = 1;
			pc.addMaxHp(30);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				//TODO 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			break;
		case COOKING_1_2_N:
		case COOKING_1_2_S:
			cookingType = 2;
			break;
		case COOKING_1_3_N:
		case COOKING_1_3_S:
			cookingType = 3;
			pc.getAC().addAc(-1);
			pc.sendPackets(new S_OwnCharStatus(pc));
			break;
		case COOKING_1_4_N:
		case COOKING_1_4_S:
			cookingType = 4;
			pc.addMaxMp(20);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_5_N:
		case COOKING_1_5_S:
			cookingType = 5;
			break;
		case COOKING_1_6_N:
		case COOKING_1_6_S:
			cookingType = 6;
			pc.getResistance().addMr(5);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_7_N:
		case COOKING_1_7_S:
			cookingType = 7;
			break;
			/**1차요리 효과끝 */
		case COOKING_1_8_N:
		case COOKING_1_8_S:
			cookingType = 16;
			pc.addBowHitRate(2); 
			pc.addBowDmgup(1); 
			break;
		case COOKING_1_9_N:
		case COOKING_1_9_S:
			cookingType = 17;
			pc.addMaxHp(30);
			pc.addMaxMp(30);
			
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				//TODO 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_10_N:
		case COOKING_1_10_S:
			cookingType = 18;
			pc.getAC().addAc(-2);
			pc.sendPackets(new S_OwnCharStatus2(pc));
			break;
		case COOKING_1_11_N:
		case COOKING_1_11_S:
			cookingType = 19;
			break;
		case COOKING_1_12_N:
		case COOKING_1_12_S:
			cookingType = 20;
			break;
		case COOKING_1_13_N:
		case COOKING_1_13_S:
			cookingType = 21;
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_14_N:
		case COOKING_1_14_S:
			cookingType = 22;
//			pc.addSp(1);
			pc.getAbility().addSp(1);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_15_N:
		case COOKING_1_15_S:
			cookingType = 23;
			break;
			/**2차요리 효과끝 */
		case COOKING_1_16_N:
		case COOKING_1_16_S:
			cookingType = 45; 
			pc.addBowHitRate(2);
			pc.addBowDmgup(1);
			break;
		case COOKING_1_17_N:
		case COOKING_1_17_S:
			cookingType = 46;
			pc.addMaxHp(50);
			pc.addMaxMp(50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				//TODO 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));	
			break;
		case COOKING_1_18_N:
		case COOKING_1_18_S:
			cookingType = 47;
			pc.addHitup(2);
			pc.addDmgup(1);
			break;
		case COOKING_1_19_N:
		case COOKING_1_19_S:
			cookingType = 48;
			pc.getAC().addAc(-3);
			pc.sendPackets(new S_OwnCharStatus2(pc));
			break;
		case COOKING_1_20_N:
		case COOKING_1_20_S:
			cookingType = 49;
			pc.getResistance().addAllNaturalResistance(10);
			pc.getResistance().addMr(15);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case COOKING_1_21_N:
		case COOKING_1_21_S:
			cookingType = 50;
//			pc.addSp(2);
			pc.getAbility().addSp(2);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_22_N:
		case COOKING_1_22_S:
			cookingType = 51;
			pc.addMaxHp(30);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				//TODO 파티 프로토
				pc.getParty().refreshPartyMemberStatus(pc);
			}
			break;
		case COOKING_1_23_N:
		case COOKING_1_23_S:
			cookingType = 52;
			break;
		case COOK_STR:
			cookingType = 157;
			pc.addDmgup(2);
			pc.addHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOK_STR_1: 
			cookingType = 215;
			pc.addDmgup(2);
			pc.addHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.addSpecialPierce(eKind.ALL, 3);
			break;
	
		case COOK_DEX_1:
			cookingType = 216;
			pc.addBowDmgup(2);
			pc.addBowHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.addSpecialPierce(eKind.ALL, 3);
			break;
		case COOK_INT_1:
			cookingType = 217;
			pc.getAbility().addSp(2);
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.addSpecialPierce(eKind.ALL, 3);
			break;
		case COOK_DEX:
			cookingType = 158;
			pc.addBowDmgup(2);
			pc.addBowHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOK_GROW_1:
			cookingType = 218;
			//pc.addSpecialPierce(eKind.ALL, 2);
			pc.addDamageReductionByArmor(2);
			pc.addSpecialPierce(eKind.ALL, 3);
			break;
		case COOK_INT:
			cookingType = 159;
			pc.getAbility().addSp(2);
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOK_GROW:
			cookingType = 160;
			break;
		case 천하장사버프:
			cookingType = 187;
			pc.addDamageReductionByArmor(5);
			break;
		case 메티스정성스프:
			cookingType = 151;
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));// 요리이펙트
			break;
		case 메티스정성요리:
			cookingType = 162;
			pc.addHitup(2);
			pc.addDmgup(2);
			pc.addBowHitup(2);
			pc.addBowDmgup(2);
			pc.getAbility().addSp(2);
			pc.addHpr(3);
			pc.addMpr(4);
			pc.getResistance().addMr(15);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			
	/*		pc.sendPackets(new S_SkillSound(pc.getId(), 830));//힐
			pc.curePoison();//큐어포이즌
			pc.sendPackets(new S_DoActionGFX(pc.getId(), 18));//액션을취하게한다
*/			break;
		default:
			break;
		}
		
//		pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));//요리이펙트
		pc.sendPackets(new S_PacketBox(53, cookingType, time));
		pc.setSkillEffect(cookingId, time * 1000);
		
		if ((cookingId >= COOKING_1_0_N && cookingId <= COOKING_1_6_N) || (cookingId >= COOKING_1_0_S && cookingId <= COOKING_1_6_S)) {
			pc.setCookingId(cookingId);
		} else if (cookingId == COOKING_1_7_N || cookingId == COOKING_1_7_S || cookingId == 수련자의닭고기스프) {
			pc.setDessertId(cookingId);
		}
	/** 2차요리 효과 부여 */
		   else if (cookingId >= COOKING_1_8_N && cookingId <= COOKING_1_14_N     // 캐비어 카나페 //악어스테이크//터틀드래곤과자//키위패롯구이//스콜피온구이//일렉카둠스튜 
				 || cookingId >= COOKING_1_8_S && cookingId <= COOKING_1_14_S) {  // 요리 2단계 
			pc.setCookingId(cookingId);
		} else if (cookingId == COOKING_1_15_N || cookingId == COOKING_1_15_S || cookingId == 수련자의닭고기스프) { // 크랩살 스프 
			pc.setDessertId(cookingId);
		}
	
	/** 3차요리 효과 부여 */
		   else if (cookingId >= COOKING_1_16_N && cookingId <= COOKING_1_22_N     // 요리 3단계  
				 || cookingId >= COOKING_1_16_S && cookingId <= COOKING_1_22_S
				 || cookingId >= COOK_STR && cookingId <= COOK_INT
						 || cookingId == COOK_STR_12 || cookingId == COOK_STR_1 || cookingId == COOK_INT_1 || cookingId == COOK_DEX_1
				 || cookingId == 메티스정성요리) {   
			pc.setCookingId(cookingId);
		} else if (cookingId == COOKING_1_23_N || cookingId == COOKING_1_23_S || cookingId == COOK_GROW || cookingId == 수련자의닭고기스프
				 || cookingId == COOK_GROW_1) { // 바실리스크 알 스프 
			pc.setDessertId(cookingId);
	}
		/** By..Freedom-사탄 */
		pc.sendPackets(new S_OwnCharStatus(pc));
	}
}
