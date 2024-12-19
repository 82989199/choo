package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_UseAttackSkill;

public class AttackSkill extends MJRaidBossSkill{
	@Override
	public String toString(){
		return AttackSkill.class.getName();
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
			S_UseAttackSkill s_uas = new S_UseAttackSkill(own, pc.getId(), _effectId, pc.getX(), pc.getY(), ActionCodes.ACTION_Attack, false);
			pc.sendPackets(s_uas, false);
			Broadcaster.broadcastPacket(pc, s_uas);
		}
		pc.receiveDamage(own, calcDamage(own, pc));
		S_DoActionGFX s_gfx = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
		pc.sendPackets(s_gfx, false);
		Broadcaster.broadcastPacket(pc, s_gfx, true);
	}
}
