package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_OnlyEffect;

/** 유저를 소환한다. **/
public class ReCall extends MJRaidBossSkill{
	@Override
	public String toString(){
		return ReCall.class.getName();
	}
	
	@Override
	public void action(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0 || own.isParalyzed())
			return;

	
		L1PcInstance 	pc		= null;
		int 			size 	= pcs.size();
		try{
			synchronized(own.synchObject){
				own.setParalyzed(true);
				if(_actid >= 0)
					Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
				
				if((_type & BSRTYPE_ONCE) > 0){
					pc = pcs.get(_rnd.nextInt(size));
					if(isSkillSet(own, pc))
						processAction(own, pc);
				}else{
					for(int i=0; i<size; i++){
						pc = pcs.get(i);
						
						if(!isSkillSet(own, pc))
							continue;
						
						processAction(own, pc);
					}
				}
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
	
		L1PcInstance 	pc		= null;
		int 			size 	= pcs.size();
		try{
			if(_actid >= 0)
				Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
			
			if((_type & BSRTYPE_ONCE) > 0){
				pc = pcs.get(_rnd.nextInt(size));
				if(isSkillSet(own, pc))
					processAction(own, pc);
			}else{
				for(int i=0; i<size; i++){
					pc = pcs.get(i);
					
					if(!isSkillSet(own, pc))
						continue;
					processAction(own, pc);
				}
			}
			sleepAction(own, _actid);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void processAction(L1NpcInstance own, L1PcInstance pc){
		if(_effectId > 0){
			S_OnlyEffect oe = new S_OnlyEffect(pc.getId(), _effectId);
			pc.sendPackets(oe, false);
			Broadcaster.broadcastPacket(pc, oe);
		}
		
		pc.start_teleport(own.getX() + _rnd.nextInt(2) + 1, own.getY() + _rnd.nextInt(2) + 1, pc.getMapId(), pc.getHeading(), 169, true, false);
	}
}
