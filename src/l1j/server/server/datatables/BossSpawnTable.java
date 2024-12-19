package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1BossSpawn;
import l1j.server.server.templates.L1BossSpawnData;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class BossSpawnTable {
	private static Logger _log = Logger.getLogger(BossSpawnTable.class.getName());

	private static final Set<Integer> _bossIdSet = new HashSet<Integer>();

	private static final Set<L1BossSpawnData> _data = new HashSet<L1BossSpawnData>();

	private BossSpawnTable() {
	}

	public static boolean isBoss(int templateId) {
		return _bossIdSet.contains(templateId);
	}

	public static L1BossSpawnData getSpawnData(int templateId) {
		for (L1BossSpawnData dd : _data) {
			if (dd.getBossSpawnId() == templateId) {
				return dd;
			}
		}
		return null;
	}

	public static void fillBossData() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_boss");
			rs = pstm.executeQuery();
			L1BossSpawnData data;
			while (rs.next()) {
				int npcTemplateId = rs.getInt("npc_id");
				String name_map = rs.getString("map_name");
				if (name_map == "" || name_map == null || name_map.equalsIgnoreCase("모름")) {
					name_map = "알수 없는 지역";
				}
				data = new L1BossSpawnData();
				data.setBossSpawnId(npcTemplateId);
				data.setBossSpawnMapName(name_map);
				_data.add(data);
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void fillSpawnTable() {
		int spawnCount = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_boss");
			rs = pstm.executeQuery();
			L1BossSpawn spawnDat;
			L1Npc template1;
			while (rs.next()) {
				int npcTemplateId = rs.getInt("npc_id");
				template1 = NpcTable.getInstance().getTemplate(npcTemplateId);
				if (template1 == null) {
					_log.warning("mob data for id:" + npcTemplateId + " missing in npc table");
					System.out.println("mob data for id:" + npcTemplateId + " missing in npc table");
					spawnDat = null;
				} else {
					_bossIdSet.add(npcTemplateId);

					spawnDat = new L1BossSpawn(template1);
					spawnDat.setId(rs.getInt("id"));
					spawnDat.setNpcid(npcTemplateId);
					spawnDat.setLocation(rs.getString("location"));
					spawnDat.setCycleType(rs.getString("cycle_type"));
					spawnDat.setAmount(rs.getInt("count"));
					spawnDat.setGroupId(rs.getInt("group_id"));
					spawnDat.setLocX(rs.getInt("locx"));
					spawnDat.setLocY(rs.getInt("locy"));
					spawnDat.setRandomx(rs.getInt("randomx"));
					spawnDat.setRandomy(rs.getInt("randomy"));
					spawnDat.setLocX1(rs.getInt("locx1"));
					spawnDat.setLocY1(rs.getInt("locy1"));
					spawnDat.setLocX2(rs.getInt("locx2"));
					spawnDat.setLocY2(rs.getInt("locy2"));
					spawnDat.setHeading(rs.getInt("heading"));
					spawnDat.setMapId(rs.getShort("mapid"));
					spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
					spawnDat.setMovementDistance(rs.getInt("movement_distance"));
					spawnDat.setRest(rs.getBoolean("rest"));
					spawnDat.setSpawnType(rs.getInt("spawn_type"));
					spawnDat.setPercentage(rs.getInt("percentage"));
					spawnDat.setName(template1.get_name());
					// start the spawning
					spawnDat.init();
					spawnCount += spawnDat.getAmount();

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.log(Level.FINE, "총보스 monster수 " + spawnCount + "마리");
	}
}
