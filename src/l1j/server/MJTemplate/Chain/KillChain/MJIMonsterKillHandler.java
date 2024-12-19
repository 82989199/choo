package l1j.server.MJTemplate.Chain.KillChain;

import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIMonsterKillHandler {
	public void on_kill(L1PcInstance attacker, L1MonsterInstance m);
}
