package l1j.server.server.command.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1ToSpawn implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1ToSpawn.class.getName());
	private static final Map<Integer, Integer> _spawnId = new HashMap<Integer, Integer>();

	private L1ToSpawn() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1ToSpawn();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (! _spawnId.containsKey(pc.getId())) {
				_spawnId.put(pc.getId(), 0);
			}
			int id = _spawnId.get(pc.getId());
			if (arg.isEmpty() || arg.equals("+")) {
				id++;
			} else if (arg.equals("-")) {
				id--;
			} else {
				StringTokenizer st = new StringTokenizer(arg);
				id = Integer.parseInt(st.nextToken());
			}
			L1Spawn spawn = NpcSpawnTable.getInstance(). getTemplate(id);
			if (spawn == null) {
				spawn = SpawnTable.getInstance(). getTemplate(id);
			}
			if (spawn != null) {
				pc.start_teleport(spawn.getLocX(), spawn.getLocY(), spawn.getMapId(), 5, 169, false, false);
				pc. sendPackets(new S_SystemMessage("spawnid(" + id	+ ")의 원래로 납니다"));
			} else {
				pc.sendPackets(new S_SystemMessage("spawnid(" + id	+ ")(은)는 발견되지 않습니다"));
			}
			_spawnId.put(pc.getId(), id);
		} catch (Exception exception) {
			pc.sendPackets(new S_SystemMessage(cmdName + " [스폰아이디] [+,-]"));
		}
	}
}
