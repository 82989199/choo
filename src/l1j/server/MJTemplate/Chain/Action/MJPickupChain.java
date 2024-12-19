package l1j.server.MJTemplate.Chain.Action;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJPickupChain extends MJAbstractActionChain<MJIPickupHandler>{
	private static MJPickupChain _instance;
	public static MJPickupChain getInstance(){
		if(_instance == null)
			_instance = new MJPickupChain();
		return _instance;
	}
	
	private MJPickupChain(){
		super();
	}
	
	public boolean handle(L1PcInstance pc, L1ItemInstance item, int amount){
		for(MJIPickupHandler handler : m_handlers){
			if(handler.on_pickup(pc, item, amount))
				return true;
		}
		return false;
	}
}
