package l1j.server.MJTemplate.Chain.KillChain;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMonsterKillChain extends MJAbstractActionChain<MJIMonsterKillHandler>{
	private static MJMonsterKillChain _instance;
	public static MJMonsterKillChain getInstance(){
		if(_instance == null)
			_instance = new MJMonsterKillChain();
		return _instance;
	}
	
	private MJMonsterKillChain(){
		super();
	}
	
	public void on_kill(L1PcInstance attacker, L1MonsterInstance m){
		for(MJIMonsterKillHandler handler : m_handlers)
			handler.on_kill(attacker, m);
	}
}
