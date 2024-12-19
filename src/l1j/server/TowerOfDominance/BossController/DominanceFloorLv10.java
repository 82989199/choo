package l1j.server.TowerOfDominance.BossController;

import java.util.ArrayList;
import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class DominanceFloorLv10 implements Runnable {

	private boolean _event = false;

	private int _npcid;
	private int _mapx;
	private int _mapy;
	private int _mapid;
	private boolean _isment;
	private String _ment;
	private boolean _effectuse;
	private int _effid;

	private boolean _END = false;

	private int stage = 1;

	private static final int EVENT = 1;
	private static final int END = 2;

	private boolean Running = false;

	private int Time = 3600;

	public DominanceFloorLv10(int bossid, int x, int y, int mapid, boolean mentuse, String ment, boolean effectuse,
			int effectid) {
		_npcid = bossid;
		_mapx = x;
		_mapy = y;
		_mapid = mapid;
		_isment = mentuse;
		_ment = ment;
		_effectuse = effectuse;
		_effid = effectid;
	}

	public void setReady(boolean flag) {
		Running = flag;
	}

	public boolean isReady() {
		return Running;
	}

	@Override
	public void run() {
		while (Running) {
			try {
				TimeCheck();
				switch (stage) {
				case EVENT:
					for (L1PcInstance pc : PcCK()) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fY$27669"));
					}
					spawn(_mapx, _mapy, (short) _mapid, 0, _npcid, ActionCodes.ACTION_Appear);
					Thread.sleep(7000);
					stage = 2;
				case END:
					if (_END == true) {
						for (L1PcInstance pc : PcCK()) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fY$27677"));
						}
						Thread.sleep(7000);
						for (L1PcInstance pc : PcCK()) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fY$27678"));
						}
						Running = false;
						break;
					}
					Object_Check();
					break;
				default:
					break;
				}
			} catch (Exception e) {
			} finally {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		reset();
	}

	public void Start() {
		GeneralThreadPool.getInstance().schedule(this, 5000);
		reset();
		setReady(true);
		if (!Config.STANDBY_SERVER) {
			if (_isment) {
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(_ment));
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, _ment));
			}

			S_DisplayEffect effect = null;
			if (_effectuse) {
				if (_effid > 0)
					effect = S_DisplayEffect.newInstance(_effid);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc.isPrivateShop() || !pc.is_world())
						continue;
					if (effect != null)
						pc.sendPackets(effect, false);
				}
				if (effect != null)
					effect.clear();
			}
		}
	}

	private void TimeCheck() {
		if (Time > 0) {
			Time--;
		}
		if (Time == 0) {
			Running = false;
		}
	}

	private void Object_Check() {
		L1NpcInstance mob = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(_mapid).values()) {
			if (object instanceof L1MonsterInstance) {
				mob = (L1NpcInstance) object;
				int npc = mob.getNpcTemplate().get_npcId();
				if (npc == _npcid) {
					if (mob != null && mob.isDead() && _event == false) {
						_event = true;
					}
				}
			}
		}
		if (_event == true) {
			_event = false;
			_END = true;
		}
	}

	private void reset() {
		L1Object boss = L1World.getInstance().findNpc(_npcid);
		if (boss != null && boss instanceof L1NpcInstance && !(boss instanceof L1DollInstance)) {
			L1NpcInstance npc = (L1NpcInstance) boss;
			deleteNpc(npc);
		}
	}

	public ArrayList<L1PcInstance> PcCK() {
		ArrayList<L1PcInstance> _pc = new ArrayList<L1PcInstance>();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getMapId() == _mapid)
				_pc.add(pc);
		}
		return _pc;
	}

	private static void spawn(int x, int y, short MapId, int Heading, int npcId, int actioncode) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			int tryCount = 0;
			do {
				tryCount++;
				npc.setX(x);
				npc.setY(y);
				if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
					break;
				}
				Thread.sleep(1);
			} while (tryCount < 50);
			if (tryCount >= 50) {
				npc.getLocation().forward(Heading);
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(Heading);

			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
				npc.onPerceive(pc);
				S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), actioncode);
				pc.sendPackets(gfx);
			}

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteNpc(L1NpcInstance npc) {
		npc.getMap().setPassable(npc.getX(), npc.getY(), true);
		npc.deleteMe();
	}
}
