package l1j.server.MJTemplate.Chain.Action;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJDeadRestartChain extends MJAbstractActionChain<MJIDeadRestartHandler>{
	private static MJDeadRestartChain _instance;
	public static MJDeadRestartChain getInstance(){
		if(_instance == null)
			_instance = new MJDeadRestartChain();
		return _instance;
	}
	
	private MJDeadRestartChain(){
		super();
	}
	
	public void on_death_restarted(L1PcInstance pc){
		for(MJIDeadRestartHandler handler : m_handlers){
			handler.on_death_restarted(pc);
		}
	}

	public int[] get_death_location(L1PcInstance pc){
		for(MJIDeadRestartHandler handler : m_handlers){
			int[] loc = handler.get_death_location(pc);
			if(loc != null)
				return loc;
		}
		return null;
	}
}
