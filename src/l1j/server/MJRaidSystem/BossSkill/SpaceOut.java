package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;

import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class SpaceOut extends MJRaidBossSkill{
	@Override
	public String toString(){
		return SpaceOut.class.getName();
	}
	
	@Override
	public void action(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0 || own.isParalyzed())
			return;
		
		try{
			synchronized(own.synchObject){
				own.setParalyzed(true);
				Thread.sleep(_rnd.nextInt(_dmgMax - _dmgMin) + _dmgMin);
			}
		}catch(Exception e){
			
		}finally{
			own.setParalyzed(false);
		}
		
	}
	
	@Override
	public void actionNoDelay(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0)
			return;
		
		try {
			Thread.sleep(_rnd.nextInt(_dmgMax - _dmgMin) + _dmgMin);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
