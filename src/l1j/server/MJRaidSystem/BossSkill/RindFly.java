package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;

import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_WorldPutObject;

public class RindFly extends MJRaidBossSkill{
	@Override
	public String toString(){
		return RindFly.class.getName();
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
				if(_actid >= 0){
					own.allTargetClear();
					own.setHiddenStatus(L1MonsterInstance.HIDDEN_STATUS_FLY);
					Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid), true);
					own.setStatus(_actid);
					Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
				}
				Thread.sleep(_rnd.nextInt((int) (MJRaidLoadManager.MRS_BS_RINDFLY_MAX - MJRaidLoadManager.MRS_BS_RINDFLY_MIN)) + MJRaidLoadManager.MRS_BS_RINDFLY_MIN);
				while(true){
					pc = pcs.get(_rnd.nextInt(size));
					if(pc == null || pc.isDead() || pc.getMapId() != own.getMapId())
						continue;
					
					own.setX(pc.getX());
					own.setY(pc.getY());
					pc = null;
					break;
				}
				Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
				own.setHiddenStatus(L1MonsterInstance.HIDDEN_STATUS_NONE);
				Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), 11), true);
				own.setStatus(11);
				Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
				own.setStatus(0);
				Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
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
		try {
			
			if(_actid >= 0){
				own.allTargetClear();
				own.setHiddenStatus(L1MonsterInstance.HIDDEN_STATUS_FLY);
				Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid), true);
				own.setStatus(_actid);
				Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
			}
			Thread.sleep(_rnd.nextInt((int) (MJRaidLoadManager.MRS_BS_RINDFLY_MAX - MJRaidLoadManager.MRS_BS_RINDFLY_MIN)) + MJRaidLoadManager.MRS_BS_RINDFLY_MIN);
			while(true){
				pc = pcs.get(_rnd.nextInt(size));
				if(pc == null || pc.isDead() || pc.getMapId() != own.getMapId())
					continue;
				
				own.setX(pc.getX());
				own.setY(pc.getY());
				pc = null;
				break;
			}
			Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
			own.setHiddenStatus(L1MonsterInstance.HIDDEN_STATUS_NONE);
			Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), 11), true);
			own.setStatus(11);
			Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
			own.setStatus(0);
			Broadcaster.broadcastPacket(own, S_WorldPutObject.get(own), true);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processAction(L1NpcInstance own, L1PcInstance pc){
		pc.receiveDamage(own, calcDamage(own, pc));
		S_DoActionGFX s_gfx = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
		pc.sendPackets(s_gfx, false);
		Broadcaster.broadcastPacket(pc, s_gfx, true);
	}
}
