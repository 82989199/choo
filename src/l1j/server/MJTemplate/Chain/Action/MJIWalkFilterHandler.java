package l1j.server.MJTemplate.Chain.Action;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIWalkFilterHandler {
	public boolean is_moved(L1PcInstance owner, int next_x, int next_y);
}
