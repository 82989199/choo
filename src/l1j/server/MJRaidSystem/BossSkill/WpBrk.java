package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.S_ServerMessage;

/** weapon break. **/
public class WpBrk extends MJRaidBossSkill{
	@Override
	public String toString(){
		return WpBrk.class.getName();
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
						processAction(pc);	
				}else{
					for(int i=0; i<size; i++){
						pc = pcs.get(i);
						if(!isSkillSet(own, pc))
							continue;
						processAction(pc);
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
					processAction(pc);	
			}else{
				for(int i=0; i<size; i++){
					pc = pcs.get(i);
					if(!isSkillSet(own, pc))
						continue;
					processAction(pc);
				}
			}
			sleepAction(own, _actid);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void processAction(L1PcInstance pc){
		L1ItemInstance weapon = pc.getWeapon();
		if(weapon != null){
			S_ServerMessage sm = new S_ServerMessage(268, weapon.getLogName());
			pc.sendPackets(sm);
			sm = null;
			pc.getInventory().receiveDamage(weapon, 5);
		}
		
		if(_effectId > 0){
			S_OnlyEffect oe = new S_OnlyEffect(pc.getId(), _effectId);
			pc.sendPackets(oe);
			Broadcaster.broadcastPacket(pc, oe);
		}
	}
}
