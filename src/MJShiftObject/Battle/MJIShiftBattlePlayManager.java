package MJShiftObject.Battle;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIShiftBattlePlayManager {
	public void on_tick();
	public int next_update_tick();
	public void on_update_tick();
	public void on_enter(L1PcInstance pc);
	public void on_closed();
}
