package l1j.server.server.datatables;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class CharBuffTable {
	
	private CharBuffTable() {	}

	private static Logger _log = Logger.getLogger(CharBuffTable.class.getName());

	public static final int[] buffSkill = { 2, 67, // 라이트, 셰이프 체인지
			3, 151, 168, // 쉴드 어스스킨 아이언스킨
			43, 54, 1000, 1001, STATUS_ELFBRAVE, // 헤이 파업, 그레이터 헤이 파업, 치우침 이브 일부, 그린 일부, 엘븐 와퍼
			52, 101, 150, // 호-리 워크, 무빙 악 세레이션, 윈드워크
			26, 42, 109, 110, // PE:DEX, PE:STR, 드레스마이티, 드레스데크스타리티
			114, 115, 117, // 그로윙오라, 샤이닝오라, 치우침 이브 아우라
			148, 155, 163, // 파이아웨폰, 파이어 호흡, 바닝웨폰
			149, 156, 166, // 윈도우 쇼트, 스토무아이, 스톰 쇼트
			1002, // 블루 일부, 채팅 금지
			STATUS_CHAT_PROHIBITED, //채금버프
			4914, 천하장사버프, //흑사버프
			POLY_RING_MASTER, 8135, 8136, 8137,BLOOD_LUST,POLY_RING_MASTER2,
			
			
			SIDE_OF_ME_BLESSING, RE_START_BLESSING, NEW_START_BLESSING, LIFE_BLESSING,
			
			/** 버프저장 패킷 차례 */
			DECREASE_WEIGHT, DECAY_POTION, SILENCE, VENOM_RESIST, WEAKNESS, DISEASE,
			DRESS_EVASION, BERSERKERS, NATURES_TOUCH, WIND_SHACKLE,CYCLONE,
			ERASE_MAGIC, ADDITIONAL_FIRE, ELEMENTAL_FALL_DOWN, ELEMENTAL_FIRE, QUAKE, BLOW_ATTACK,
			STRIKER_GALE, SOUL_OF_FLAME, POLLUTE_WATER, 
			CONCENTRATION, INSIGHT, PANIC, COUNTER_MIRROR,
			MORTAL_BODY, HORROR_OF_DEATH, FEAR,
			PATIENCE, GUARD_BREAK, DRAGON_SKIN,
			
			/** 드래곤 버프 **/
			ANTA_BUFF, FAFU_BUFF, RIND_BUFF,
			
			/** 요정 전용 버프 */
			RESIST_MAGIC, CLEAR_MIND, RESIST_ELEMENTAL, ELEMENTAL_PROTECTION,
			
			/** 컬러풀 패키지 아이템 및 천상의 물약 */
			EXP_POTION, EXP_BUFF,
			STATUS_BLUE_POTION2, STATUS_FRUIT,
			STATUS_CASHSCROLL, STATUS_CASHSCROLL2, STATUS_CASHSCROLL3, STATUS_CASHSCROLL4,STATUS_CASHSCROLL5,STATUS_CASHSCROLL6, STATUS_DRAGON_PEARL,
						
			/** 요리 1단계 효과 재부여 */
			COOKING_1_0_N, COOKING_1_0_S, COOKING_1_1_N, COOKING_1_1_S, // 요리
			COOKING_1_2_N, COOKING_1_2_S, COOKING_1_3_N, COOKING_1_3_S,
			COOKING_1_4_N, COOKING_1_4_S, COOKING_1_5_N, COOKING_1_5_S,
			COOKING_1_6_N, COOKING_1_6_S, 
			
			/** 요리 2단계 효과 재부여 */
			COOKING_1_8_N, COOKING_1_8_S, COOKING_1_9_N, COOKING_1_9_S, 
			COOKING_1_10_N, COOKING_1_10_S, COOKING_1_11_N, COOKING_1_11_S, 
			COOKING_1_12_N, COOKING_1_12_S, COOKING_1_13_N, COOKING_1_13_S,
			COOKING_1_14_N, COOKING_1_14_S, 
			
			/** 요리 3단계 효과 재부여 */
			COOKING_1_16_N, COOKING_1_16_S, COOKING_1_17_N, COOKING_1_17_S,
			COOKING_1_18_N, COOKING_1_18_S, COOKING_1_19_N, COOKING_1_19_S,
			COOKING_1_20_N, COOKING_1_20_S, COOKING_1_21_N, COOKING_1_21_S,
			COOKING_1_22_N, COOKING_1_22_S, 메티스축복주문서,메티스정성스프,메티스정성요리,
			
			COMA_A, COMA_B, SetBuff, 나루토감사캔디,
			/**  리뉴얼 요리 */
			COOK_STR, COOK_DEX, COOK_INT, COOK_GROW,
			COOK_DEX_1, COOK_GROW_1, COOK_STR_1, COOK_INT_1, COOK_STR_12,
			
			/** 추가 버프 **/
			레벨업보너스,

			/** N샵 버프 **/ //이건 계정에 걸리는거기때문에 저장할 필요없다.
			//강화버프_활력, 강화버프_공격, 강화버프_방어, 강화버프_마법, 강화버프_스턴, 강화버프_홀드,
			
			/** 드래곤의 에메랄드 */
			EMERALD_NO, EMERALD_YES, DRAGON_TOPAZ, DRAGON_PUPLE, TOP_RANKER,
			/** 마안 버프 & 운세 버프 **/
			ANTA_MAAN, FAFU_MAAN, VALA_MAAN, LIND_MAAN, BIRTH_MAAN, SHAPE_MAAN, LIFE_MAAN,
			FEATHER_BUFF_A, FEATHER_BUFF_B, FEATHER_BUFF_C, FEATHER_BUFF_D,
			WITCH_MANA_POTION,BUYER_COOLTIME,
			FOCUS_WAVE, HURRICANE, SAND_STORM,
			USER_WANTED,
	};
	
	private static void StoreBuff(int objId, int skillId, int time, int polyId) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?");
			pstm.setInt(1, objId);
			pstm.setInt(2, skillId);
			pstm.setInt(3, time);
			pstm.setInt(4, polyId);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void DeleteBuff(L1PcInstance pc) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}
	
	public static void SaveBuff(L1PcInstance pc) {
		if(pc.getAI() != null)
			return;
		
		for (int skillId : buffSkill) {
			if(!pc.hasSkillEffect(skillId))
				continue;
			int timeSec = pc.getSkillEffectTimeSec(skillId);
			int polyId = 0;
			if (skillId == SHAPE_CHANGE || skillId == POLY_RING_MASTER || skillId == POLY_RING_MASTER2) {
				polyId = pc.getCurrentSpriteId();
			}
			StoreBuff(pc.getId(), skillId, timeSec, polyId);
			
			/*
			int timeSec = pc.getSkillEffectTimeSec(skillId);
			if (0 < timeSec) {
				System.out.println(skillId);
				int polyId = 0;
				if (skillId == SHAPE_CHANGE || skillId == POLY_RING_MASTER) {
					polyId = pc.getCurrentSpriteId();
				}
				StoreBuff(pc.getId(), skillId, timeSec, polyId);
			}*/
		}
	}

}
