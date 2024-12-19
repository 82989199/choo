package l1j.server.MJTemplate.Chain.Action;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJTeleportChain extends MJAbstractActionChain<MJITeleportHandler>{
	private static MJTeleportChain _instance;
	public static MJTeleportChain getInstance(){
		if(_instance == null)
			_instance = new MJTeleportChain();
		return _instance;
	}
	
	private MJTeleportChain(){
		super();
	}
	
	public boolean is_teleport(L1PcInstance pc, int next_x, int next_y, int map_id){
		for(MJITeleportHandler handler : m_handlers){
			if(handler.is_teleport(pc, next_x, next_y, map_id))
				return true;
		}
		return false;
	}
	
	public void teleported(L1PcInstance pc, int next_x, int next_y, int map_id){
		for(MJITeleportHandler handler : m_handlers){
			handler.on_teleported(pc, next_x, next_y, map_id);
		}
	}
}
