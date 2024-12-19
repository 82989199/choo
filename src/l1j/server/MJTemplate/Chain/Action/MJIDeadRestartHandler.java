package l1j.server.MJTemplate.Chain.Action;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIDeadRestartHandler {
	public int[] get_death_location(L1PcInstance pc);
	public void on_death_restarted(L1PcInstance pc);
}
