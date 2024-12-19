package l1j.server.MJTemplate.Chain.Action;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIPickupHandler {
	public boolean on_pickup(L1PcInstance pc, L1ItemInstance item, int amount);
}
