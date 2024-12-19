package l1j.server.MJTemplate.ItemPresentator;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.Stream.MJPresentationOutputStream;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.templates.L1Item;

public class ItemPresentatorFactory {
	private static ConcurrentHashMap<Integer, Presentator> _presentators = new ConcurrentHashMap<Integer, Presentator>(256);
	
	public static Presentator create_presentator(int item_id){
		Presentator presentator = _presentators.get(item_id);
		if(presentator != null)
			return presentator;
//		Presentator presentator = null;
		
		switch(item_id){
		/** 아이템 안전인챈 표시 추가 **/
		case 541: // 지배자의 절대검
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_critical(item_template.get_melee_critical_probability() + enchant)
					.writeAbilityPierce(7 + enchant)
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
					//.write_safeenchant(0)//안전인챈 수동표기
					
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.add_melee_critical_rate((enchant) * signed);
					pc.addSpecialPierce(eKind.ABILITY, (7 + enchant) * signed);
				}
			};
			break;		
			
		case 61: // 진명황의 집행검
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_critical(item_template.get_melee_critical_probability() + enchant)
					.writeAbilityPierce(8 + enchant)
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
					//.write_safeenchant(0)//안전인챈 수동표기
					
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.add_melee_critical_rate((enchant) * signed);
					pc.addSpecialPierce(eKind.ABILITY, (8 + enchant) * signed);
				}
			};
			break;		
			
	
			
		case 294: // 사신의 검
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_con(item_template.get_addcon())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_critical(item_template.get_melee_critical_probability() + enchant)
					.writeAbilityPierce(3 + enchant) // 기술적중
					.writeSpiritPierce(3 + enchant) // 정령적중
					.writeFearPierce(3 + enchant)	// 공포적중
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
					//.write_safeenchant(0)//안전인챈 수동표기
					
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.add_melee_critical_rate((enchant) * signed);
					pc.addSpecialPierce(eKind.ABILITY, (3 + enchant) * signed);
					pc.addSpecialPierce(eKind.SPIRIT, (3 + enchant) * signed);
					pc.addSpecialPierce(eKind.FEAR, (3 + enchant) * signed);
				}
			};
			break;
			
			
		case 540:	// 지배자의 살귀검
		case 12:	// 바람칼날의 단검
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_drain_hp()
					.write_max_hp(100)
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
				//	.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
				}
			};
			break;
		case 543: // 지배자의 지팡이
		case 134:	// 수정결정체 지팡이
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_int(item_template.get_addint())
					.write_sp(item_template.get_addsp() + enchant)
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_magic_hit(2 + enchant)	// for db input.
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
				//	.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
			
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.getAbility().addSp(enchant * signed);
					pc.addBaseMagicHitUp((pc.getBaseMagicHitUp() + 2 + enchant) * signed);
					pc.sendPackets(new S_SPMR(pc));
				}
			};
			break;
		case 544: // 지배자의 파멸도
		case 86:	// 붉은 그림자의 이도류
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_hit(item_template.getHitModifier())
					.write_magic_name(item, item_template.getMagicName())
					.writeSpiritPierce(5 + enchant)
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
					//.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.addSpecialPierce(eKind.SPIRIT, (5 + enchant) * signed);
				}
			};
			break;
		case 542: // 지배자의 척살궁
		case 202011:	// 가이아의 격노
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_dex(item_template.get_adddex())
					.write_addsub_long_damage(item_template.getDmgModifier())
					.write_long_hit(item_template.getHitModifier())
					.write_damage_reduction(2)	// for db input.
					.write_ignore_reduction(9 + enchant) // for db input.
					.write_long_critical(1 + enchant)
					.write_magic_name(item, item_template.getMagicName())
					.writeSpiritPierce(2 + enchant)
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					 .write_weapon_attr_options(item)
					//.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.addDamageReductionByArmor(2 * signed);
					pc.addDamageReductionIgnore((9 + enchant) * signed);
					pc.add_missile_critical_rate(enchant * signed);
					pc.addSpecialPierce(eKind.SPIRIT, (2 + enchant) * signed);
				}
			};
			break;
			
	/*	case 217: // 기르타스의 검
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_ignore_reduction(20 + enchant) // 대미지 리덕션 무시
					.write_short_critical(item_template.get_melee_critical_probability() + (enchant * 2)) // 치명타 +2
					.writeAbilityPierce(15 + enchant) // 기술적중
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
					.write_safeenchant(0)//안전인챈 수동표기
					
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.addDamageReductionIgnore((20 + enchant) * signed);
					pc.add_melee_critical_rate((7 + (enchant * 2)) * signed);
					pc.addSpecialPierce(eKind.ABILITY, (15 + enchant) * signed);
					pc.sendPackets(new S_SPMR(pc));
				}
			};
			break;*/
			
	/*	case 2944: // 아인하사드의 섬광
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_critical(item_template.get_melee_critical_probability() + (enchant * 2)) // 치명타 +2
					.writeAbilityPierce(12 + enchant) // 기술적중
					.writeSpiritPierce(12 + enchant) // 정령적중
					.writeFearPierce(12 + enchant)	// 공포적중
					.write_ignore_reduction(20 + enchant) // 대미지 리덕션 무시
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
					.write_safeenchant(0)//안전인챈 수동표기
					
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.add_melee_critical_rate((7 + (enchant * 2)) * signed);
					pc.addDamageReductionIgnore((20 + enchant) * signed);
					pc.addSpecialPierce(eKind.ABILITY, (12 + enchant) * signed);
					pc.addSpecialPierce(eKind.SPIRIT, (12 + enchant) * signed);
					pc.addSpecialPierce(eKind.FEAR, (12 + enchant) * signed);
				}
			};
			break;*/
			
		case 545: // 지배자의 절풍검
		case 202013:	// 크로노스의 공포
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_hit(item_template.getHitModifier())
					.write_short_critical(1 + enchant)
					.writeDragonSpellPierce(7 + enchant)
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
				//	.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.set_melee_critical_rate(enchant * signed);
					pc.addSpecialPierce(eKind.DRAGON_SPELL, (7 + enchant) * signed);
				}
			};
			break;
		case 546: // 지배자의 키링크
		case 202012:	// 히페리온의 절망
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_int(item_template.get_addint())
					.write_sp(item_template.get_addsp() + enchant)
					.write_max_hp(item_template.get_addhp())
					.writeDragonSpellPierce(7 + enchant)
					.write_magic_critical(1 + enchant)	// for db input.
					.write_magic_name(item, item_template.getMagicName())
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
				//	.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
				}
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.addSpecialPierce(eKind.DRAGON_SPELL, (7 + enchant) * signed);
					pc.getAbility().addSp(enchant * signed);
					pc.add_magic_critical_rate((1 + enchant) * signed);
					pc.sendPackets(new S_SPMR(pc));
				}
			};
			break;
		case 547: // 지배자의 섬광도
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_critical(item_template.get_melee_critical_probability() + enchant)
					.writeFearPierce(2 + enchant)	// for db input.
					.write_text("타이탄 발동 5% 상승")
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
				//	.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
				}
				
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.add_melee_critical_rate(enchant * signed);
					pc.addSpecialPierce(eKind.FEAR, (2 + enchant) * signed);
				}
			};
			break;
		case 202014:	// 타이탄의 분노
			presentator = new Presentator(){
				@Override
				public byte[] do_presentation(L1ItemInstance item) throws IOException {
					L1Item item_template = item.getItem();
					int enchant = item.getEnchantLevel();
					return MJPresentationOutputStream.newInstance(256) 
					.write_weapon_info(item_template, item.getWeight())
					.write_weapon_add_damage(enchant * 2, enchant * 2)
					.write_weapon_etc_info(item_template, item.get_durability())
					.write_str(item_template.get_addstr())
					.write_short_hit(item_template.getHitModifier())
					.write_addsub_short_damage(item_template.getDmgModifier())
					.write_short_critical(item_template.get_melee_critical_probability() + enchant)
					.writeFearPierce(3 + enchant)	// for db input.
					.write_magic_name(item, item_template.getMagicName())
					.write_text("타이탄 발동 5% 상승")
					.write_blessed_options(item)
					.write_weapon_level_options(item)
					.write_weapon_attr_options(item)
					//.write_safeenchant(0)//안전인챈 수동표기
					.toArray();
				}
				
				//TODO 효과적용
				@Override
				public void do_equip(L1PcInstance pc, L1ItemInstance item, int signed){
					int enchant = item.getEnchantLevel();
					pc.add_melee_critical_rate(enchant * signed);
					pc.addSpecialPierce(eKind.FEAR, (3 + enchant) * signed);
				}
			};
			break;
		}
		
		if(presentator != null){
			_presentators.put(item_id, presentator);
			return presentator;
		}
		return null;
	}
	
	public static byte[] do_presentation(L1ItemInstance item) throws IOException{
		Presentator presentator = create_presentator(item.getItemId());
		if(presentator != null)
			return presentator.do_presentation(item);		
		return null;
	}
	
	public static void do_equip(L1PcInstance pc, L1ItemInstance item){
		Presentator presentator = create_presentator(item.getItemId());
		if(presentator != null)
			presentator.do_equip(pc, item, 1);
	}
	
	public static void do_unequip(L1PcInstance pc, L1ItemInstance item){
		Presentator presentator = create_presentator(item.getItemId());
		if(presentator != null)
			presentator.do_equip(pc, item, -1);
	}
}
