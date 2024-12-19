package l1j.server.server.model;

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
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR;
import static l1j.server.server.model.skill.L1SkillId.COOK_DEX_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT_1;
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
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.model:
// L1Cooking

public class L1Cooking {

	private L1Cooking() {
	}

	public static void useCookingItem(L1PcInstance pc, L1ItemInstance item) {
		int itemId = item.getItem().getItemId();
		if (itemId == 41284 //버섯 스프
				|| itemId == 49056 //크랩살 스프 
				|| itemId == 49064 //환상의 크랩살 스프 
				|| itemId == 41292 // 환상의 버섯스프
				|| itemId ==210055 //바실리스크 알 스프
				|| itemId ==210063) { //환상의 바실리스크 알 스프
			if (pc.get_food() != 225) { // 100%
				pc.sendPackets(new S_ServerMessage(74, item.getNumberedName(1)));
//				return;
			}
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));//포만감이 225일때 템및이펙을 사용한다.
		}

		if (itemId >= 41277 && itemId <= 41283 // 1차 요리
				|| itemId >= 49049 && itemId <= 49056// 2차 요리
				|| itemId >= 210048 && itemId <= 210055//3차 요리
				|| itemId >= 41285 && itemId <= 41291// 1차 환상의 요리
				|| itemId >= 49057 && itemId <= 49064// 2차 환상의 요리
				|| itemId >= 210056 && itemId <= 210062 // 3차 환상의 요리
				|| itemId >= 30051 && itemId <= 30053
				|| itemId == 4100156 || itemId == 4100157|| itemId == 4100158
				|| itemId == 3000130// 리뉴얼 요리
				) {
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));//요리이펙트
			int cookingId = pc.getCookingId();
			if (cookingId != 0) {
				pc.removeSkillEffect(cookingId);
//				return;
			}
		}

		if (itemId == 41284 // 버섯 스프
				|| itemId == 49056// 크랩살 스프
				|| itemId == 49064// 환상의 크랩살 스프
				|| itemId == 41292// 환상의 버섯 스프
				|| itemId == 200021 // 바실리스크 알 스프
				|| itemId == 200029 // 환상의 바실리스크 알 스프
				|| itemId == 30054 // 수련의 닭고기 스프
				|| itemId == 3000129
				|| itemId == 4100159) {
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));// 요리이펙트
			int dessertId = pc.getDessertId();
			if (dessertId != 0) {
				pc.removeSkillEffect(dessertId);
			}
		}
		if (itemId >= 42650 && itemId <= 42652) { // 수련자 닭고기 빼고 지워지게
			pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));// 요리이펙트
			int dessertId = pc.getDessertId();
			if (dessertId != 0) {
				pc.removeSkillEffect(dessertId);
			}
		}
		
		
		/**1차 요리 효과 */
		int cookingId;
		int time = 900;
		switch(itemId){
		case 41277:
		case 41285:
			if (itemId == 41277) {
				cookingId = COOKING_1_0_N;
			} else {
				cookingId = COOKING_1_0_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41278:
		case 41286:
			if (itemId == 41278) {
				cookingId = COOKING_1_1_N;
			} else {
				cookingId = COOKING_1_1_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41279:
		case 41287:
			if (itemId == 41279) {
				cookingId = COOKING_1_2_N;
			} else {
				cookingId = COOKING_1_2_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41280:
		case 41288:
			if (itemId == 41280) {
				cookingId = COOKING_1_3_N;
			} else {
				cookingId = COOKING_1_3_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41281:
		case 41289:
			if (itemId == 41281) {
				cookingId = COOKING_1_4_N;
			} else {
				cookingId = COOKING_1_4_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41282:
		case 41290:
			if (itemId == 41282) {
				cookingId = COOKING_1_5_N;
			} else {
				cookingId = COOKING_1_5_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41283:
		case 41291:
			if (itemId == 41283) {
				cookingId = COOKING_1_6_N;
			} else {
				cookingId = COOKING_1_6_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41284:
		case 41292:
			if (itemId == 41284) {
				cookingId = COOKING_1_7_N;
			} else {
				cookingId = COOKING_1_7_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49049:
		case 49057:
			if (itemId == 49049) {
				cookingId = COOKING_1_8_N;
			} else {
				cookingId = COOKING_1_8_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49050:
		case 49058:
			if (itemId == 49050) {
				cookingId = COOKING_1_9_N;
			} else {
				cookingId = COOKING_1_9_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49051:
		case 49059:
			if (itemId == 49051) {
				cookingId = COOKING_1_10_N;
			} else {
				cookingId = COOKING_1_10_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49052:
		case 49060:
			if (itemId == 49052) {
				cookingId = COOKING_1_11_N;
			} else {
				cookingId = COOKING_1_11_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49053:
		case 49061:
			if (itemId == 49053) {
				cookingId = COOKING_1_12_N;
			} else {
				cookingId = COOKING_1_12_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49054:
		case 49062:
			if (itemId == 49054) {
				cookingId = COOKING_1_13_N;
			} else {
				cookingId = COOKING_1_13_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49055:
		case 49063:
			if (itemId == 49055) {
				cookingId = COOKING_1_14_N;
			} else {
				cookingId = COOKING_1_14_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49056:
		case 49064:
			if (itemId == 49056) {
				cookingId = COOKING_1_15_N;
			} else {
				cookingId = COOKING_1_15_S;
			}
		    eatCooking(pc, cookingId, time);
			break;
		case 210048:
		case 210056:
			if (itemId == 210048) {
				cookingId = COOKING_1_16_N;
			} else {
				cookingId = COOKING_1_16_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210049:
		case 210057:
			if (itemId == 210049) {
				cookingId = COOKING_1_17_N;
			} else {
				cookingId = COOKING_1_17_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210050:
		case 210058:
			if (itemId == 210050) {
				cookingId = COOKING_1_18_N;
			} else {
				cookingId = COOKING_1_18_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210051:
		case 210059:
			if (itemId == 210051) {
				cookingId = COOKING_1_19_N;
			} else {
				cookingId = COOKING_1_19_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210052:
		case 210060:
			if (itemId == 210052) {
				cookingId = COOKING_1_20_N;
			} else {
				cookingId = COOKING_1_20_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210053:
		case 210061:
			if (itemId == 210053) {
				cookingId = COOKING_1_21_N;
			} else {
				cookingId = COOKING_1_21_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210054:
		case 210062:
			if (itemId == 210054) {
				cookingId = COOKING_1_22_N;
			} else {
				cookingId = COOKING_1_22_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 210055:
		case 210063:
			if (itemId == 210055) {
				cookingId = COOKING_1_23_N;
			} else {
				cookingId = COOKING_1_23_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 4100156: // (축)힘센 한우 스테이크 
			eatCooking(pc, COOK_STR_1, 1800);
			break;
		case 4100157: // (축) 날쌘 연어 찜
			eatCooking(pc, COOK_DEX_1, 1800);
			break;
		case 4100158: // (축) 영리한 칠면조 구이
			eatCooking(pc, COOK_INT_1, 1800);
			break;
		case 30051: // 힘센 한우 스테이크
			eatCooking(pc, COOK_STR, 1800);
			break;
		case 30052: // 날샌 연어 찜
			eatCooking(pc, COOK_DEX, 1800);
			break;	
		case 30053: // 영리한 칠면조 구이
			eatCooking(pc, COOK_INT, 1800);
			break;
		case 4100159: // 수련의 닭고기 스프
			eatCooking(pc, COOK_GROW_1, 1800);
			break;
		case 30054: // 수련의 닭고기 스프
			eatCooking(pc, COOK_GROW, 1800);
			break;
		case 3000130: // 정성스런 요리
			eatCooking(pc, 메티스정성요리, 1800);
			break;
		case 3000129: // 정성스런 스프
			eatCooking(pc, 메티스정성스프, 1800);
			break;
		case 42650 : eatCooking(pc, 수련자의한우스테이크, 1800); break;
		case 42651 : eatCooking(pc, 수련자의연어찜, 1800); break;
		case 42652 : eatCooking(pc, 수련자의칠면조구이, 1800); break;
		case 42653 : eatCooking(pc, 수련자의닭고기스프, 1800); break;
		default:
			break;
		}
		pc.sendPackets(new S_ServerMessage(76, item.getNumberedName(1)));
		pc.getInventory().removeItem(item , 1);
		
	}
	/**1차요리 효과 */
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
	
		case COOK_STR: // 힘센한우스테이크
			cookingType = 157;
			pc.addDmgup(2);
			pc.addHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
			
		case COOK_STR_1: //(축)힘센한우스테이크
			cookingType = 215;
			pc.addDmgup(2);
			pc.addHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10); // 
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.addSpecialPierce(eKind.ALL, 3); // 전체 적중 +3
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc); // 적중 받는 패킷
			break;
			
		case COOK_DEX: // 날쌘 연어찜
			cookingType = 158;
			pc.addBowDmgup(2);
			pc.addBowHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			break;
			
		case COOK_DEX_1: // (축) 날쌘 연어찜
			cookingType = 216;
			pc.addBowDmgup(2);
			pc.addBowHitup(1);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.addSpecialPierce(eKind.ALL, 3);
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
			
		case COOK_INT: // 영리한 칠면조 구이
			cookingType = 159;
			pc.getAbility().addSp(2);
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			break;
			
		case COOK_INT_1: // (축) 영리한 칠면조 구이
			cookingType = 217;
			pc.getAbility().addSp(2);
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getResistance().addMr(10);
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.addSpecialPierce(eKind.ALL, 3);
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			break;
		
		case COOK_GROW:
			cookingType = 160;
			break;
			
		case COOK_GROW_1:
			cookingType = 218;
			//pc.addSpecialPierce(eKind.ALL, 2);
			pc.addDamageReductionByArmor(2);
			pc.addSpecialResistance(eKind.ALL, 3);
			break;
	

		case 천하장사버프:
			cookingType = 187;
			pc.addDamageReductionByArmor(5);
			break;
		case 메티스정성스프:
			cookingType = 151;
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
				 || cookingId == COOK_GROW_1 || cookingId == 메티스정성스프) { // 바실리스크 알 스프 
			pc.setDessertId(cookingId);
	}
		/** By..Freedom-사탄 */
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

}
