package l1j.server.MJTemplate.Chain.Etc;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.Config;
import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.CalcStat;

public class MJHealingPotionDrinkChain extends MJAbstractActionChain<MJIHealingPotionDrinkHandler>{
	public static void setup_default_healing_handler(){
		getInstance().add_handler(new DefaultHealingHandler());
		getInstance().add_handler(new EquippedItemHealingHandler());
	}
	
	private static MJHealingPotionDrinkChain _instance;
	public static MJHealingPotionDrinkChain getInstance(){
		if(_instance == null)
			_instance = new MJHealingPotionDrinkChain();
		return _instance;
	}
	
	private HashMap<Integer, HashMap<Integer, HealingEffectItemInfo>> m_effect_items;
	private MJHealingPotionDrinkChain(){
		super();
		load_healing_effect_info();
	}
	
	public void load_healing_effect_info(){
		final HashMap<Integer, HashMap<Integer, HealingEffectItemInfo>> effect_items = new HashMap<Integer, HashMap<Integer, HealingEffectItemInfo>>();
		Selector.exec("select * from potion_effect", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					HealingEffectItemInfo eInfo = new HealingEffectItemInfo();
					eInfo.item_id = rs.getInt("item_id");
					eInfo.enchant = rs.getInt("enchant");
					eInfo.increase_percent = (rs.getInt("increase_percent") * 0.01);
					eInfo.increase_value = rs.getInt("increase_value");
					eInfo.decrease_defense_percent = (rs.getInt("decrease_defense_percent") * 0.01);
					HashMap<Integer, HealingEffectItemInfo> effect_map = effect_items.get(eInfo.item_id);
					if(effect_map == null){
						effect_map = new HashMap<Integer, HealingEffectItemInfo>();
						effect_items.put(eInfo.item_id, effect_map);
					}
					effect_map.put(eInfo.enchant, eInfo);
				}
			}
		});
		m_effect_items = effect_items;
	}
	
	static class HealingEffectItemInfo{
		int item_id;
		int enchant;
		double increase_percent;
		int increase_value;
		double decrease_defense_percent;
	}
	
	public double do_drink(L1PcInstance owner, double heal){
		MJHealingPotionInfo total_potion_info = new MJHealingPotionInfo();
		double destination_heal = heal;
		
		for(MJIHealingPotionDrinkHandler handler : m_handlers){
			MJHealingPotionInfo pInfo = handler.do_drink(owner, heal);
			total_potion_info.increase_percent += pInfo.increase_percent;
			total_potion_info.increase_value += pInfo.increase_value;
			total_potion_info.decrease_percent += pInfo.decrease_percent;
			total_potion_info.decrease_value += pInfo.decrease_value;
			total_potion_info.decrease_defense_percent += pInfo.decrease_defense_percent;
		}
		destination_heal += ((heal * total_potion_info.increase_percent) + total_potion_info.increase_value);
		destination_heal -= (get_user_decrease_percent(total_potion_info, owner, destination_heal) + total_potion_info.decrease_value);
		if(Config.USE_POTION_EFFECT_LOGGIN){
			System.out.println(String.format("물약 회복량 : %.2f%%, 물약 회복 : %.2f, 물약회복감소 효과 상쇄 : %.2f%%, 물약회복감소량 : %.2f%%, 물약 회복 감소 : %.2f", 
					total_potion_info.increase_percent, total_potion_info.increase_value, total_potion_info.decrease_defense_percent,
					total_potion_info.decrease_percent, total_potion_info.decrease_value));
			System.out.println(String.format("순수 물약 회복량 : %.2f, 효과 적용 후 회복량 : %.2f, 보정 회복량 : %.2f", heal, destination_heal, Math.max((heal * 0.10), destination_heal)));
		}
		return Math.max((heal * 0.10), destination_heal);
	}
	
	private double get_user_decrease_percent(MJHealingPotionInfo pInfo, L1PcInstance owner, double heal_hp){
		if (owner.hasSkillEffect(L1SkillId.DESPERADO)) {
			int atklv = owner.Desperadolevel;
			int dflv = owner.getLevel();
			if (atklv > dflv) {
				pInfo.decrease_percent += (atklv * 0.01);
			}
		}
		if(owner.hasSkillEffect(L1SkillId.POLLUTE_WATER)){
			pInfo.decrease_percent += 0.5;
		}
		if(owner.hasSkillEffect(L1SkillId.PAP_DEATH_PORTION)){
			owner.sendPackets(new S_ServerMessage(167));
			owner.setCurrentHp((int)(owner.getCurrentHp() - (heal_hp * 1.5)));
			if (owner.isInvisble()) {
				owner.delInvis();
			}
		}
		
		pInfo.decrease_percent -= pInfo.decrease_defense_percent;
		pInfo.decrease_percent = Math.max(0, pInfo.decrease_percent);
		return (pInfo.decrease_percent * heal_hp);
	}
	
	HealingEffectItemInfo get_effect_item_info(int item_id, int enchant){
		HashMap<Integer, HealingEffectItemInfo> effect_map = m_effect_items.get(item_id);
		if(effect_map == null)
			return null;
		
		return effect_map.get(enchant);
	}
	
	private static class DefaultHealingHandler implements MJIHealingPotionDrinkHandler{
		@Override
		public MJHealingPotionInfo do_drink(L1PcInstance owner, double heal_hp) {
			MJHealingPotionInfo pInfo = new MJHealingPotionInfo();
			pInfo.increase_percent += get_user_increase_percent(owner);
			pInfo.increase_percent += (CalcStat.calcHprPotion(owner.getAbility().getTotalCon()));
			return pInfo;
		}
		
		private double get_user_increase_percent(L1PcInstance owner){
			return 0D;
		}
	}
	
	private static final double[] m_accessory_healing_increase_percent = new double[]{
			0.02D,	// 5
			0.04D,	// 6
			0.06D,	// 7
			0.08D,	// 8
			0.09D,	// 9
	};
	private static final int[] m_accessory_healing_increase = new int[]{
			0,	// 5
			2,	// 6
			4,	// 7
			6,	// 8
			7,	// 9
	};
	private static final int[] m_accessory_healing_decrease_defense_percent = new int[]{
			2,	// 5
			4,	// 6
			6,	// 7
			8,	// 8
			9,	// 9
	};
	
	
	private static class EquippedItemHealingHandler implements MJIHealingPotionDrinkHandler{
		@Override
		public MJHealingPotionInfo do_drink(L1PcInstance owner, double heal_hp) {
			MJHealingPotionInfo pInfo = new MJHealingPotionInfo();
			for(L1ItemInstance item : owner.getInventory().getItems()){
				if(item == null || !item.isEquipped())
					continue;

				default_accessory_enchant_option(pInfo, item, heal_hp);

				eqquiped_effect_option(pInfo, item, heal_hp);
			}
			return pInfo;
		}
		
		private void default_accessory_enchant_option(MJHealingPotionInfo pInfo, L1ItemInstance item, double heal_hp){
			if(item.getItem().getType2() != 2 || item.getItem().getGrade() != 2)
				return;
			
			int item_type = item.getItem().getType();
			if(item_type != 8 && item_type != 12)
				return;
			
			int enchant_level = Math.min(item.getEnchantLevel(), 9) - 5;
			if(enchant_level < 0)
				return;
			pInfo.increase_percent += m_accessory_healing_increase_percent[enchant_level];
			pInfo.increase_value += m_accessory_healing_increase[enchant_level];
			pInfo.decrease_defense_percent += m_accessory_healing_decrease_defense_percent[enchant_level];
		}
		
		private void eqquiped_effect_option(MJHealingPotionInfo pInfo, L1ItemInstance item, double heal_hp){
			HealingEffectItemInfo eInfo = MJHealingPotionDrinkChain.getInstance().get_effect_item_info(item.getItemId(), item.getEnchantLevel());
			
			if(eInfo == null)
				return;
			
			pInfo.increase_percent += eInfo.increase_percent;
			pInfo.increase_value += eInfo.increase_value;
			pInfo.decrease_defense_percent += eInfo.decrease_defense_percent;
		}
	}
}
