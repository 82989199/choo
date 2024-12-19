package l1j.server.server.Controller;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class GameEffectActionController implements Runnable {

	private L1NpcInstance _npc;
	private int _actId;
	private int _count;
	private int _delay;

	public GameEffectActionController(L1NpcInstance npc, int actId, int count, int delay) {
		_npc = npc;
		_actId = actId;
		_count = count;
		_delay = delay;
	}

	public void start() {
		GeneralThreadPool.getInstance().execute(this);
	}
	
	@Override
	public void run() {
		try {
			while (_count > 0) {
				Broadcaster.broadcastPacket(_npc, new S_DoActionGFX(_npc.getId(), _actId));
				_count--;
				Thread.sleep(_delay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
