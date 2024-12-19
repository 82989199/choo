package l1j.server.MJTemplate.Chain.Action;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIRestartHandler {
	public boolean is_restart(L1PcInstance pc);
	public void on_restarted(L1PcInstance pc);
}
