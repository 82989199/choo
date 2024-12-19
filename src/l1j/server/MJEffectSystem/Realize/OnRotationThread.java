package l1j.server.MJEffectSystem.Realize;

import java.util.ArrayList;

import l1j.server.MJEffectSystem.MJEffectModel;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;

public class OnRotationThread extends OnEffectThread{
	public OnRotationThread(MJEffectModel model, L1Character owner) {
		super(model, owner);
	}

	@Override
	public void run() {
		
		if(_model == null || _owner == null || _owner.isDead())
			return;
		
		synchronized(_lock){
			L1NpcInstance npc = _maps.get(_owner.getId());
			if(npc != null)
				return;
			
			_maps.put(_owner.getId(), (L1NpcInstance) _owner);
		}
		
		synchronized(((L1NpcInstance)_owner).synchObject){
			try{
				Broadcaster.broadcastPacket(_owner, new S_DoActionGFX(_owner.getId(), _model.getActionId()));
				ArrayList<L1PcInstance> pcs = L1World.getInstance().getVisiblePlayer(_owner, _model.getRange());
				int range	 				= _model.getRange();
				int impact					= _model.getImpact();
				int eff						= _model.getEffectId();
				int ox						= _owner.getX();
				int oy						= _owner.getY();
				int divRange 				= range / 2;
				SimplePoint[] pts			= null;
				for(int i=0; i<divRange; i+=2){
					pts						= createStartPoints(ox, oy, range);
					for(int j=0; j<range; j+=2){
						processEffect(pcs, pts[0].x, pts[0].y, eff, impact);
						processEffect(pcs, pts[1].x, pts[1].y, eff, impact);
						processEffect(pcs, pts[2].x, pts[2].y, eff, impact);
						processEffect(pcs, pts[3].x, pts[3].y, eff, impact);
						
						pts[0].x+=2;
						pts[1].y+=2;
						pts[2].x-=2;
						pts[3].y-=2;
						Thread.sleep(_model.getDelay());
					}
					range -= 2;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		synchronized(_lock){
			_maps.put(_owner.getId(), null);
		}
	}
	
	private SimplePoint[] createStartPoints(int ox, int oy, int range){
		int div = range / 2;
		return new SimplePoint[]{
			new SimplePoint(ox - div, oy - div),
			new SimplePoint(ox + div, oy - div),
			new SimplePoint(ox + div, oy + div),
			new SimplePoint(ox - div, oy + div)
		};
	}
	
	private void processEffect(ArrayList<L1PcInstance> pcs, int left, int top, int eff, int impact){
		int pcsize				= pcs.size();
		int sx					= 0;
		int sy					= 0;
		L1PcInstance pc 		= null;
		S_EffectLocation pck	= null;
		
		pck = new S_EffectLocation(left, top, eff);
		for(int p=0; p<pcsize; p++){
			pc = pcs.get(p);
			
			if(pc == null || pc.getMapId() != _owner.getMapId())
				continue;
			
			pc.sendPackets(pck, false);
			if(pc.isDead() || isUnBeatable(pc) || isCounterMagic(pc))
				continue;
			
			sx = Math.abs(pc.getX() - left);
			sy = Math.abs(pc.getY() - top);
			if(sx <= impact && sy <= impact){
				S_DoActionGFX s_gfx = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
				pc.sendPackets(s_gfx, false);
				Broadcaster.broadcastPacket(pc, s_gfx, true);
				pc.receiveDamage(_owner, calcDamage(_owner, pc, _model.getDamageMin(), _model.getDamageMax()));
			}
		}
		pck.clear();
	}
}
