package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;

public class MJWideArea extends MJRaidBossSkill{

	@Override
	public String toString(){
		return MJWideArea.class.getName();
	}

	@Override
	public void action(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0 || own.isParalyzed())
			return;

		try{
			synchronized(own.synchObject){
				own.setParalyzed(true);
				if(_actid >= 0)
					Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
				
				GeneralThreadPool.getInstance().execute(new tightenEffectThread(own, pcs, _effectId, _range));
				sleepAction(own, _actid);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			own.setParalyzed(false);			
		}
	}

	@Override
	public void actionNoDelay(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0)
			return;

		try{
			if(_actid >= 0)
				Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
			GeneralThreadPool.getInstance().execute(new tightenEffectThread(own, pcs, _effectId, _range));
			sleepAction(own, _actid);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 원형으로 조여오는 이펙트 **/
	public class tightenEffectThread implements Runnable{
		private L1NpcInstance 			_own;
		private ArrayList<L1PcInstance> _pcs;
		private int						_range;
		private int						_effect;
		public tightenEffectThread(L1NpcInstance own, ArrayList<L1PcInstance> pcs, int range, int effect){
			_own 	= own;
			_pcs	= pcs;
			_range	= range;
			_effect	= effect;
		}
		
		@Override
		public void run(){
			double radX = _range / 2;
			double radY = radX;
			double cx	= _own.getX();
			double cy	= _own.getY();
			int rx		= 0;
			int ry		= 0;
			S_EffectLocation eff = null;
			for(int z=_range; z>=1; z-=3){
				for(int i=1; i<=360; i += radX){
					rx		= (int)(cx+Math.cos(i) * radX);
					ry		= (int)(cy+Math.sin(i) * radY);
					eff		= new S_EffectLocation(rx, ry, _effect);
					for(L1PcInstance pc : _pcs){
						if(pc == null || pc.getMapId() != _own.getMapId())
							continue;
						
						pc.sendPackets(eff, false);
						if(!pc.isDead() && pc.getX() == rx && pc.getY() == ry){
							if(isSkillSet(_own, pc))
								pc.receiveDamage(_own, 500);
						}
					}
					eff.clear();
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
