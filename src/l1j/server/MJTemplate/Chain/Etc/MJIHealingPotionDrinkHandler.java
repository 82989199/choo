package l1j.server.MJTemplate.Chain.Etc;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIHealingPotionDrinkHandler {
	public MJHealingPotionInfo do_drink(L1PcInstance owner, double heal_hp);
}
