package l1j.server.MJCompanion.Instance;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_S;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_12;
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
import static l1j.server.server.model.skill.L1SkillId.COOK_STR_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_INT_1;
import static l1j.server.server.model.skill.L1SkillId.COOK_GROW_1;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.EARTH_GUARDIAN;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_B;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_AVATAR;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.PHANTASM;
import static l1j.server.server.model.skill.L1SkillId.REDUCTION_ARMOR;

import l1j.server.Config;
import l1j.server.GrangKinConfig;
import l1j.server.ArmorClass.MJArmorClass;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCompanion.MJCompanionSettings;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillEffect;
import l1j.server.MJCompanion.Basic.Skills.MJCompanionSkillInfo;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Action.MJIAttackHandler;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.IntRange;

public class MJCompanionAttackHandler implements MJIAttackHandler{

	@Override
	public int do_calculate_hit(L1Attack attack_object, L1Character attacker, L1Character target) {
		if(attacker instanceof MJCompanionInstance){
			return do_calculate_attacker_hit(attack_object, (MJCompanionInstance)attacker, target);
		}
		return 0;
	}

	

	@Override
	public void on_hit_notify(L1Attack attack_object, L1Character attacker, L1Character target, boolean is_hit) {
		if(attacker instanceof MJCompanionInstance){
			MJCompanionInstance companion = (MJCompanionInstance)attacker;
			if(is_hit && companion.get_is_combo_time()){
				companion.increase_combo_count();
			}else{
				companion.set_combo_count(0);
			}
		}
	}

	@Override
	public int do_calculate_damage(L1Attack attack_object, L1Character attacker, L1Character target) {
		int damage = 0;
		if(attacker instanceof MJCompanionInstance){
			MJCompanionInstance companion = (MJCompanionInstance)attacker;
			damage = do_calculate_attacker_damage(attack_object, companion, target);
			companion.on_attacked();
		}
		return damage;
	}
	
	private int do_calculate_attacker_hit(L1Attack attack_object, MJCompanionInstance attacker, L1Character target){
		if(!(target instanceof L1MonsterInstance) && target.getZoneType() == -1)
			return 0;
		
		double hit = attacker.getLevel() * MJCompanionSettings.HIT_BY_LEVEL;
		hit += ((attacker.getLevel() - target.getLevel()) * MJCompanionSettings.HIT_BY_LEVEL_DIFF);
		hit += attacker.get_melee_hit();

		if(target instanceof L1PcInstance){
			MJArmorClass armor_class = MJArmorClass.find_armor_class(target.getAC().getAc());
			if(armor_class == null){
				hit += (target.getAC().getAc() * 0.01);
			}else{
				hit -= (armor_class.get_to_npc_dodge());
			}	
		}
		
		double target_dodge = target.getDg();
		if(target_dodge > 0){
			target_dodge = (target_dodge * (Config.PER_DODGE * 0.02D));
			hit -= (hit * target_dodge * 0.01D);
		}
		if(target.hasSkillEffect(L1SkillId.ANTA_MAAN) || target.hasSkillEffect(L1SkillId.BIRTH_MAAN) || target.hasSkillEffect(L1SkillId.SHAPE_MAAN)){
			if(MJRnd.isWinning(100, 15))
				hit -= 5;
		}

		return IntRange.ensure((int)Math.round(hit), MJCompanionSettings.MINIMUM_HIT, MJCompanionSettings.MAXIMUM_HIT);
	}
	
	private int do_calculate_attacker_damage(L1Attack attack_object, MJCompanionInstance attacker, L1Character target){
		if (target.hasSkillEffect(ABSOLUTE_BARRIER) ||
				target.hasSkillEffect(ICE_LANCE) || 
				target.hasSkillEffect(EARTH_BIND)) 
			return 0;

		double dmg = attacker.getLevel() * MJCompanionSettings.DMG_BY_LEVEL;
		dmg += ((attacker.getLevel() - target.getLevel()) * MJCompanionSettings.DMG_BY_LEVEL_DIFF);
		
		MJCompanionSkillEffect sInfo = get_damage_effect(attack_object, attacker, target);
		int melee = sInfo.get_melee_dmg();
		if(melee > 0 && sInfo.get_melee_cri_hit() > 0 && MJRnd.isWinning(100, sInfo.get_melee_cri_hit())){
			attack_object.set_is_critical(true);
			attack_object.setActId(EActionCodes.alt_attack.toInt());
			dmg += (melee * MJCompanionSettings.MAGNIFICATION_BY_CRITICAL);
		}else{
			dmg += melee;
		}
		if(attacker.get_is_combo_time()){
			if(attacker.get_combo_count() >= 3){
				attack_object.set_is_critical(true);
				attack_object.setActId(EActionCodes.alt_attack.toInt());
				dmg += (melee * MJCompanionSettings.MAGNIFICATION_BY_COMBO) + Math.max(1, sInfo.get_combo_dmg_multi() > 0 ? MJRnd.next(sInfo.get_combo_dmg_multi()) : 0);
				target.send_effect(17326);
				attacker.set_combo_count(0);
			}
		}else{
			attacker.set_combo_count(0);
		}
		if(melee > 0 && sInfo.get_blood_suck_hit() > 0 && sInfo.get_blood_suck_heal() > 0 && MJRnd.isWinning(100, sInfo.get_blood_suck_hit())){
			int heal = Math.max(MJRnd.next(attacker.get_blood_suck_heal()), 1);
			attacker.setCurrentHp(attacker.getCurrentHp() + heal);
			dmg += heal;
		}
		if(sInfo.get_spell_dmg_add() > 0){
			double mr_reduction = ((double)target.getResistance().getMrAfterEraseRemove() * MJCompanionSettings.REDUCTION_BY_MR);
			dmg += (Math.max(1, sInfo.get_spell_dmg_add() - mr_reduction));
		}
		if(sInfo.get_spell_dmg_multi() > 0){
			double mr_reduction = ((double)target.getResistance().getMrAfterEraseRemove() * MJCompanionSettings.REDUCTION_BY_MR);
			dmg += (Math.max(1, sInfo.get_spell_dmg_add() - mr_reduction));			
		}
		if(sInfo.get_fire_elemental_dmg() > 0){
			double elemental_reduction = ((double)target.getResistance().getFire() * MJCompanionSettings.REDUCTION_BY_ELEMENTAL);
			dmg += (Math.max(1, sInfo.get_fire_elemental_dmg() - elemental_reduction));						
		}
		if(sInfo.get_water_elemental_dmg() > 0){
			double elemental_reduction = ((double)target.getResistance().getWater() * MJCompanionSettings.REDUCTION_BY_ELEMENTAL);
			dmg += (Math.max(1, sInfo.get_water_elemental_dmg() - elemental_reduction));									
		}
		if(sInfo.get_air_elemental_dmg() > 0){
			double elemental_reduction = ((double)target.getResistance().getWind() * MJCompanionSettings.REDUCTION_BY_ELEMENTAL);
			dmg += (Math.max(1, sInfo.get_air_elemental_dmg() - elemental_reduction));									
		}
		if(sInfo.get_earth_elemental_dmg() > 0){
			double elemental_reduction = ((double)target.getResistance().getEarth() * MJCompanionSettings.REDUCTION_BY_ELEMENTAL);
			dmg += (Math.max(1, sInfo.get_earth_elemental_dmg() - elemental_reduction));									
		}
		if(sInfo.get_light_elemental_dmg() > 0){
			double elemental_reduction = ((double)target.getResistance().getWind() * MJCompanionSettings.REDUCTION_BY_ELEMENTAL);
			dmg += (Math.max(1, sInfo.get_light_elemental_dmg() - elemental_reduction));									
		}

		double reduction = 0;
		if(target instanceof L1PcInstance){
			reduction = Math.max(do_calculate_damage_reduction(attack_object, (L1PcInstance)target, dmg) - sInfo.get_ignore_reduction(), 0);
			dmg -= reduction;
			dmg = dmg * MJCompanionSettings.MAGNIFICATION_BY_PET_TO_PC;
			dmg += attacker.get_pvp_dmg_ratio();
		}
		
		if(sInfo.get_attacker_effect_id() > 0)
			attacker.broadcastPacket(new S_SkillSound(attacker.getId(), sInfo.get_attacker_effect_id()));
		if(sInfo.get_target_effect_id() > 0)
			target.broadcastPacket(new S_SkillSound(target.getId(), sInfo.get_target_effect_id()));
			
		int r = MJRnd.next(5);
		return IntRange.ensure((int)Math.round(dmg), r + MJCompanionSettings.MINIMUM_DMG, (MJRnd.isBoolean() ? r : -r) + MJCompanionSettings.MAXIMUM_HIT);
	}
	
	private MJCompanionSkillEffect get_damage_effect(L1Attack attack_object, MJCompanionInstance attacker, L1Character target){
		MJCompanionSkillInfo sInfo = MJRnd.isWinning(100, attacker.get_spell_hit()) ? attacker.select_exercise_skills() : null;
		if(sInfo == null){
			return MJCompanionSkillEffect.newInstance()
					.set_melee_dmg(attacker.get_melee_dmg())
					.set_melee_cri_hit(attacker.get_melee_cri_hit())
					.set_ignore_reduction(attacker.get_ignore_reduction())
					.set_blood_suck_hit(attacker.get_blood_suck_hit())
					.set_blood_suck_heal(attacker.get_blood_suck_heal())
					.set_spell_dmg_multi(attacker.get_spell_dmg_multi())
					.set_fire_elemental_dmg(attacker.get_fire_elemental_dmg())
					.set_water_elemental_dmg(attacker.get_water_elemental_dmg())
					.set_air_elemental_dmg(attacker.get_air_elemental_dmg())
					.set_earth_elemental_dmg(attacker.get_earth_elemental_dmg())
					.set_light_elemental_dmg(attacker.get_light_elemental_dmg())
					.set_combo_dmg_multi(attacker.get_combo_dmg())
					.set_attacker_effect_id(0)
					.set_target_effect_id(0);
		}else{
			return MJCompanionSkillEffect.newInstance()
					.set_melee_dmg(attacker.get_melee_dmg() + sInfo.get_melee_dmg())
					.set_melee_cri_hit(attacker.get_melee_cri_hit() + sInfo.get_melee_cri_hit())
					.set_ignore_reduction(attacker.get_ignore_reduction() + sInfo.get_ignore_reduction())
					.set_blood_suck_hit(attacker.get_blood_suck_hit() + sInfo.get_blood_suck_hit())
					.set_blood_suck_heal(attacker.get_blood_suck_heal() + sInfo.get_blood_suck_heal())
					.set_spell_dmg_multi(attacker.get_spell_dmg_multi() + sInfo.get_spell_dmg_multi())
					.set_fire_elemental_dmg(attacker.get_fire_elemental_dmg() + sInfo.get_fire_elemental_dmg())
					.set_water_elemental_dmg(attacker.get_water_elemental_dmg() + sInfo.get_water_elemental_dmg())
					.set_air_elemental_dmg(attacker.get_air_elemental_dmg() + sInfo.get_air_elemental_dmg())
					.set_earth_elemental_dmg(attacker.get_earth_elemental_dmg() + sInfo.get_earth_elemental_dmg())
					.set_light_elemental_dmg(attacker.get_light_elemental_dmg() + sInfo.get_light_elemental_dmg())
					.set_combo_dmg_multi(attacker.get_combo_dmg() + sInfo.get_combo_dmg_multi())
					.set_attacker_effect_id(sInfo.get_attacker_effect_id())
					.set_target_effect_id(sInfo.get_target_effect_id());
		}
	}
	
	private double do_calculate_damage_reduction(L1Attack attack_object, L1PcInstance target, double source_damage){
		int reduction = 0;
		for(Object doll : target.getDollList().values().toArray())
			reduction -= ((L1DollInstance)doll).getDamageReductionByDoll();
		reduction += target.getDamageReductionByArmor();
		reduction += target.getDamageReduction();
		if (target.hasSkillEffect(L1SkillId.메티스정성스프))
			reduction += 5;
		if (target.hasSkillEffect(L1SkillId.메티스정성요리))
			reduction += 5;
		if (target.hasSkillEffect(L1SkillId.메티스축복주문서))
			reduction += 3;
		if (target.hasSkillEffect(COOKING_1_0_S) // 요리에 의한 대미지 경감
				|| target.hasSkillEffect(COOKING_1_1_S) || target.hasSkillEffect(COOKING_1_2_S) || target.hasSkillEffect(COOKING_1_3_S)
				|| target.hasSkillEffect(COOKING_1_4_S) || target.hasSkillEffect(COOKING_1_5_S) || target.hasSkillEffect(COOKING_1_6_S)
				|| target.hasSkillEffect(COOKING_1_8_S) || target.hasSkillEffect(COOKING_1_9_S) || target.hasSkillEffect(COOKING_1_10_S)
				|| target.hasSkillEffect(COOKING_1_11_S) || target.hasSkillEffect(COOKING_1_12_S) || target.hasSkillEffect(COOKING_1_13_S)
				|| target.hasSkillEffect(COOKING_1_14_S) || target.hasSkillEffect(COOKING_1_15_S) || target.hasSkillEffect(COOKING_1_16_S)
				|| target.hasSkillEffect(COOKING_1_17_S) || target.hasSkillEffect(COOKING_1_18_S) || target.hasSkillEffect(COOKING_1_19_S)
				|| target.hasSkillEffect(COOKING_1_20_S) || target.hasSkillEffect(COOKING_1_21_S) || target.hasSkillEffect(COOKING_1_22_S)) {
			reduction += 5;
		}
		if (target.hasSkillEffect(COOK_STR) || 
				target.hasSkillEffect(COOK_DEX) || 
				target.hasSkillEffect(COOK_INT)
				|| target.hasSkillEffect(COOK_GROW)
				|| target.hasSkillEffect(COOK_STR_1)
				|| target.hasSkillEffect(COOK_STR_12)
				|| target.hasSkillEffect(COOK_DEX_1)
				|| target.hasSkillEffect(COOK_INT_1)
				|| target.hasSkillEffect(COOK_GROW_1)
				
				
				) {
			reduction += 2;
		}
		if (target.hasSkillEffect(COOKING_1_7_S) || 
				target.hasSkillEffect(COOKING_1_15_S) || 
				target.hasSkillEffect(COOKING_1_23_S)) { // 디저트에
																																				// 경감
			reduction += 5;
		}

		// 전사스킬 : 아머가드 - 캐릭의 AC/10의 데미지감소 효과를 얻는다.
		if (target.getPassive(MJPassiveID.ARMOR_GUARD.toInt()) != null) {
			reduction += (target.getAC().getAc() / 10);
		}

		if (target.hasSkillEffect(REDUCTION_ARMOR)) {
			int lvl = Math.max(50, target.getLevel());
			reduction += (lvl - 50) / 5 + 1;
		}

		if (target.hasSkillEffect(L1SkillId.MAJESTY)) {
			int lvl = Math.max(80, target.getLevel());
			reduction += (lvl - 80) / 2 + 1;
		}
		
		if (target.hasSkillEffect(DRAGON_SKIN)) {
			if (target.getLevel() >= 80) {
				reduction += 5 + ((target.getLevel() - 80) / 2);
			} else {
				reduction += 5;
			}
		}

		if (target.hasSkillEffect(PATIENCE)) {
			if (target.getLevel() >= 80) {
				reduction += 2 + ((target.getLevel() - 80) / 4);
			} else {
				reduction += 2;
			}
		}
		if (target.hasSkillEffect(IllUSION_AVATAR)) {
			reduction += (source_damage / 5);
		}
		if (target.hasSkillEffect(FEATHER_BUFF_A)) {
			reduction += 3;
		}
		if (target.hasSkillEffect(FEATHER_BUFF_B)) {
			reduction += 2;
		}
		if (target.hasSkillEffect(EARTH_GUARDIAN)) {
			reduction += 2;
		}
		if (target.hasSkillEffect(PHANTASM)) {
			target.removeSkillEffect(PHANTASM);
		}
		if (GrangKinConfig.GRANG_KIN_ANGER_SYSTEM_USE) {
			if (target.getAccount() != null && target.getAccount().getGrangKinAngerStat() != 0 && source_damage > 0) {
				reduction += (source_damage * target.getAccount().getGrangKinAngerReducCalc());
			}
		}
		
		MJArmorClass armor_class = MJArmorClass.find_armor_class(target.getAC().getAc());
		if(armor_class != null)
			reduction += (armor_class.get_to_npc_reduction());
		
		return reduction;
	}

}
