package l1j.server.MJTemplate.Chain.KillChain;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJCharacterKillChain extends MJAbstractActionChain<MJICharacterKillHandler>{
	private static MJCharacterKillChain _instance;
	public static MJCharacterKillChain getInstance(){
		if(_instance == null)
			_instance = new MJCharacterKillChain();
		return _instance;
	}
	
	private MJCharacterKillChain(){
		super();
	}
	
	public void on_kill(L1PcInstance killer, L1PcInstance victim){
		for(MJICharacterKillHandler handler : m_handlers)
			handler.on_kill(killer, victim);
	}
}
