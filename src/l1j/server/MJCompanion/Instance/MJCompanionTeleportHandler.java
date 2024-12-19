package l1j.server.MJCompanion.Instance;

import java.util.ArrayList;

import l1j.server.MJTemplate.Chain.Action.MJITeleportHandler;
import l1j.server.MJTemplate.L1Instance.MJEffectTriggerInstance;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_RemoveObject;

public class MJCompanionTeleportHandler implements MJITeleportHandler{

	@Override
	public boolean on_teleported(L1PcInstance owner, int next_x, int next_y, int map_id) {
		final MJCompanionInstance companion = owner.get_companion();
		if(companion == null)
			return false;

		int mid = owner.getMapId();
		L1Map m = owner.getMap();
		S_RemoveObject remove = new S_RemoveObject(companion.getId());
		if(!owner.isGhost() && m.isTakePets() && mid != 5153 && mid != 5140 && mid != 781 && mid != 782){
			companion.set_is_teleporting(true);
			ArrayList<MJEffectTriggerInstance> triggers = companion.get_triggers();
			ArrayList<L1PcInstance> visible_players = L1World.getInstance().getVisiblePlayer(companion);
			MJPoint pt = MJPoint.newInstance(next_x, next_y, 2, (short)mid);
			L1World.getInstance().moveVisibleObject(companion, mid);
			companion.setX(pt.x);
			companion.setY(pt.y);
			companion.setMap((short)mid);
			owner.removeKnownObject(companion);
			if(triggers != null){
				for(MJEffectTriggerInstance trigger : triggers){
					trigger.setX(pt.x);
					trigger.setY(pt.y);
					trigger.setMap((short)mid);
					owner.removeKnownObject(trigger);					
				}
			}
			owner.updateObject();
			for(L1PcInstance p : visible_players){
				p.removeKnownObject(companion);
				if(triggers != null){
					for(MJEffectTriggerInstance trigger : triggers)
						p.removeKnownObject(trigger);
				}
				p.sendPackets(remove, false);
				p.updateObject();
			}
			companion.set_is_teleporting(false);
		}else{
			owner.remove_companion();
		}
		remove.clear();
		return false;
	}

	@Override
	public boolean is_teleport(L1PcInstance owner, int next_x, int next_y, int map_id) {
		return false;
	}
}
