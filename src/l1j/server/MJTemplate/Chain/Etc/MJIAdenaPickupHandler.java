package l1j.server.MJTemplate.Chain.Etc;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIAdenaPickupHandler {
	public int do_pickup(L1PcInstance owner, L1ItemInstance item, int amount);
}

