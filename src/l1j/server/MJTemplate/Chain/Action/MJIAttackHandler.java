package l1j.server.MJTemplate.Chain.Action;

import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;

public interface MJIAttackHandler {
	public int do_calculate_hit(L1Attack attack_object, L1Character attacker, L1Character target);
	public void on_hit_notify(L1Attack attack_object, L1Character attacker, L1Character target, boolean is_hit);
	public int do_calculate_damage(L1Attack attack_object, L1Character attacker, L1Character target);
}
