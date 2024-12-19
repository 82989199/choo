package l1j.server.MJTemplate.Chain.Action;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJITeleportHandler {
	public boolean on_teleported(L1PcInstance owner, int next_x, int next_y, int map_id);
	public boolean is_teleport(L1PcInstance owner, int next_x, int next_y, int map_id);
}
