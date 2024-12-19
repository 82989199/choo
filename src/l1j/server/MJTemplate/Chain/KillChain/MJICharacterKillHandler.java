package l1j.server.MJTemplate.Chain.KillChain;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJICharacterKillHandler {
	public void on_kill(L1PcInstance attacker, L1PcInstance victim);
}
