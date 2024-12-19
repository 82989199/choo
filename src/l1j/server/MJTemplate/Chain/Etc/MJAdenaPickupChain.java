package l1j.server.MJTemplate.Chain.Etc;

import l1j.server.Config;
import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJAdenaPickupChain extends MJAbstractActionChain<MJIAdenaPickupHandler>{
	public static void setup_blessing_effect(){
		getInstance().add_handler(new BlessingEffectHandler());
	}
	
	private static MJAdenaPickupChain _instance;
	public static MJAdenaPickupChain getInstance(){
		if(_instance == null)
			_instance = new MJAdenaPickupChain();
		return _instance;
	}
	
	private MJAdenaPickupChain(){
		super();
	}
	
	public int pickup(L1PcInstance owner, L1ItemInstance item, int amount){
		int destination_amount = amount;
		for(MJIAdenaPickupHandler handler : m_handlers){
			destination_amount += handler.do_pickup(owner, item, amount);				
			if(destination_amount <= 0)
				destination_amount = 1;
		}
		return destination_amount;
	}
	
	
	private static class BlessingEffectHandler implements MJIAdenaPickupHandler{
		@Override
		public int do_pickup(L1PcInstance owner, L1ItemInstance item, int amount) {
			return is_blessing_effect(owner, item) ? 
					(int)(amount * (Config.BLESSING)) :
					-((int)(amount * (Config.UN_BLESSING)));
		}
		
		private static boolean is_blessing_effect(L1PcInstance pc, L1ItemInstance item){
			if(item.isGiveItem())
				return false;
			
			if(pc.getInventory().checkItem(4100121)){//4100121
				int mapId = pc.getMapId();
				
				if(mapId >= 0)
					return true;

				/*if(mapId >= 15410 && mapId <= 15440)
					return true;
				if(mapId >= 53 && mapId <= 56)
					return true;
				if(mapId >= 101 && mapId <= 110)
					return true;
				if(mapId >= 12852 && mapId <= 12862)
					return true;
				if(mapId >= 15403 && mapId <= 15404)
					return true;
				if(mapId >= 1708 && mapId <= 1709)
					return true;
				if(mapId == 624)
					return true;
				if(mapId == 479)
					return true;*/
			}
			return false;
		}
	}	
}
