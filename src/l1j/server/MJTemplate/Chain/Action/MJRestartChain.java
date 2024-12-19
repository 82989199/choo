package l1j.server.MJTemplate.Chain.Action;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJRestartChain extends MJAbstractActionChain<MJIRestartHandler>{
	private static MJRestartChain _instance;
	public static MJRestartChain getInstance(){
		if(_instance == null)
			_instance = new MJRestartChain();
		return _instance;
	}
	
	private MJRestartChain(){
		super();
	}

	public boolean is_restart(L1PcInstance pc){
		for(MJIRestartHandler handler : m_handlers){
			if(handler.is_restart(pc))
				return true;
		}
		return false;
	}
	
	public void on_restarted(L1PcInstance pc){
		for(MJIRestartHandler handler : m_handlers){
			handler.on_restarted(pc);
		}
	}
}
