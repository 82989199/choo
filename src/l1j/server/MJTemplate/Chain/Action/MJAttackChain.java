package l1j.server.MJTemplate.Chain.Action;

import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;

public class MJAttackChain extends MJAbstractActionChain<MJIAttackHandler>{
	private static MJAttackChain _instance;
	public static MJAttackChain getInstance(){
		if(_instance == null)
			_instance = new MJAttackChain();
		return _instance;
	}
	
	private MJAttackChain(){
		super();
	}
	
	public boolean do_calculate_hit(L1Attack attack_object, L1Character attacker, L1Character target){
		int total_hit = 0;
		for(MJIAttackHandler handler : m_handlers){
			total_hit += handler.do_calculate_hit(attack_object, attacker, target);
		}
		
		if(attacker instanceof MJDogFightInstance && target instanceof MJDogFightInstance){
			MJDogFightInstance npc = (MJDogFightInstance) attacker;
			MJDogFightInstance target_npc = (MJDogFightInstance) target;
			//System.out.println("공격자 : " + npc.getTotalPrice() + " || 타겟 : " + target_npc.getTotalPrice());
			if(npc.getTotalPrice() > target_npc.getTotalPrice()) {
				total_hit -= MJDogFightSettings.FAKE_HIT;
			}
		}
		
		boolean is_hit = MJRnd.isWinning(100, total_hit);
		attack_object.set_hit_rate(total_hit);
		attack_object.set_hit(is_hit);
		for(MJIAttackHandler handler : m_handlers){
			handler.on_hit_notify(attack_object, attacker, target, is_hit);
		}
		return is_hit;
	}
	
	public int do_calculate_damage(L1Attack attack_object, L1Character attacker, L1Character target){
		int total_dmg = 0;
		for(MJIAttackHandler handler : m_handlers){
			total_dmg += handler.do_calculate_damage(attack_object, attacker, target);
		}
		
		if(attacker instanceof MJDogFightInstance && target instanceof MJDogFightInstance){
			MJDogFightInstance npc = (MJDogFightInstance) attacker;
			MJDogFightInstance target_npc = (MJDogFightInstance) target;
			//System.out.println("공격자 : " + npc.getTotalPrice() + " || 타겟 : " + target_npc.getTotalPrice());
			if(npc.getTotalPrice() > target_npc.getTotalPrice()) {
				total_dmg -= MJDogFightSettings.FAKE_DAMAGE;
			}
		}
		
		return total_dmg;
	}
}
