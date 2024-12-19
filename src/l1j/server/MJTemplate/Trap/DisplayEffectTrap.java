package l1j.server.MJTemplate.Trap;

import java.util.Collection;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;

public class DisplayEffectTrap extends AbstractTrap{	
	public static DisplayEffectTrap newInstance(){
		return new DisplayEffectTrap();
	}
	
	private DisplayEffectTrap(){
	}

	@Override
	public void do_trap(L1PcInstance pc) {
		pc.sendPackets(S_DisplayEffect.newInstance(get_asset_id()));
	}

	@Override
	public void do_traps(L1PcInstance[] pc_array) {
		int asset_id = get_asset_id();
		final S_DisplayEffect eff = S_DisplayEffect.newInstance(asset_id);
		
		for(L1PcInstance pc : pc_array){
			if(pc == null)
				continue;
			
			pc.sendPackets(eff, false);
		}
		
		if(asset_id == S_DisplayEffect.QUAKE_DISPLAY){
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					for(L1PcInstance pc : pc_array){
						if(pc == null)
							continue;
						
						pc.sendPackets(eff, false);
					}
					eff.clear();
				}
			}, 1000L);
		}else{
			eff.clear();
		}
		
	}

	@Override
	public void do_traps(Collection<L1PcInstance> pc_collections) {
		do_traps(pc_collections.toArray(new L1PcInstance[pc_collections.size()]));
	}
}
