package l1j.server.MJTemplate.Chain.Action;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJWalkFilterChain extends MJAbstractActionChain<MJIWalkFilterHandler>{
	private static MJWalkFilterChain _instance;
	public static MJWalkFilterChain getInstance(){
		if(_instance == null)
			_instance = new MJWalkFilterChain();
		return _instance;
	}
	
	private MJWalkFilterChain(){
		super();
	}
	
	public boolean handle(L1PcInstance pc, int next_x, int next_y){
		for(MJIWalkFilterHandler handler : m_handlers){
			if(handler.is_moved(pc, next_x, next_y))
				return true;
		}
		return false;
	}
}
