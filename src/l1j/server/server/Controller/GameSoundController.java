package l1j.server.server.Controller;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.serverpackets.S_Sound;

public class GameSoundController implements Runnable {

	private L1NpcInstance _npc;
	private int _soundNum;
	private int _count;
	private int _delay;

	public GameSoundController(L1NpcInstance npc, int soundNum, int count, int delay) {
		_npc = npc;
		_soundNum = soundNum;
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
				Broadcaster.broadcastPacket(_npc, new S_Sound(_soundNum));
				_count--;
				Thread.sleep(_delay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
