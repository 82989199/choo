/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.NumberUtil;
import l1j.server.server.utils.SQLUtil;

public class SpawnTable {
	private static Logger _log = Logger.getLogger(SpawnTable.class.getName());

	private static SpawnTable _instance;

	private Map<Integer, L1Spawn> _spawntable = new HashMap<Integer, L1Spawn>();

	private int _highestId;

	public static SpawnTable getInstance() {
		if (_instance == null) {
			_instance = new SpawnTable();
		}
		return _instance;
	}

	private SpawnTable() {
		fillSpawnTable();
	}

	public void reload() {
		SpawnTable oldInstance = _instance;
		_instance = new SpawnTable();
		oldInstance._spawntable.clear();
	}

	public void reload(int mpaid) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc template1;
		int npcTemplateId, count;
		double amount_rate;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist WHERE mapid =?");
			pstm.setInt(1, mpaid);
			rs = pstm.executeQuery();
			L1Spawn spawnDat;
			while (rs.next()) {
				int spawnid = rs.getInt("id");

				if (_spawntable.containsKey(spawnid))
					_spawntable.remove(spawnid);

				npcTemplateId = rs.getInt("npc_templateid");
				template1 = NpcTable.getInstance().getTemplate(npcTemplateId);

				if (template1 == null) {
					_log.warning("mob data for id:" + npcTemplateId + " missing in npc table");
					System.out.println("mob data for id:" + npcTemplateId + " missing in npc table");
					spawnDat = null;
				} else {
					if (rs.getInt("count") == 0) {
						continue;
					}

					amount_rate = MapsTable.getInstance().getMonsterAmount(rs.getShort("mapid"));
					count = calcCount(template1, rs.getInt("count"), amount_rate);

					if (count == 0) {
						continue;
					}

					spawnDat = new L1Spawn(template1);
					spawnDat.setId(rs.getInt("id"));
					spawnDat.setAmount(count);
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
					spawnDat.setMinRespawnDelay(rs.getInt("min_respawn_delay"));
					spawnDat.setMaxRespawnDelay(rs.getInt("max_respawn_delay"));
					spawnDat.setMapId(rs.getShort("mapid"));
					spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
					spawnDat.setMovementDistance(rs.getInt("movement_distance"));
					spawnDat.setRest(rs.getBoolean("rest"));
					spawnDat.setSpawnType(rs.getInt("near_spawn"));
					spawnDat.setName(template1.get_name());

					if (count > 1 && spawnDat.getLocX1() == 0) {
						// 복수 또한 고정 spawn의 경우는, 개체수 * 6 의 범위 spawn로 바꾼다.
						// 다만 범위가 30을 넘지 않게 한다
						int range = Math.min(count * 6, 30);
						spawnDat.setLocX1(spawnDat.getLocX() - range);
						spawnDat.setLocY1(spawnDat.getLocY() - range);
						spawnDat.setLocX2(spawnDat.getLocX() + range);
						spawnDat.setLocY2(spawnDat.getLocY() + range);
					}
					/*
					 * if(spawnDat.getMapId() == 2228){ System.out.println(
					 * "2228 테이블에서읽음"); }
					 */
					// start the spawning
					spawnDat.init();

				}

				_spawntable.put(new Integer(spawnDat.getId()), spawnDat);
				if (spawnDat.getId() > _highestId) {
					_highestId = spawnDat.getId();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillSpawnTable() {

		int spawnCount = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist");
			rs = pstm.executeQuery();

			L1Spawn spawnDat;
			L1Npc template1;
			while (rs.next()) {

				int npcTemplateId = rs.getInt("npc_templateid");

				template1 = NpcTable.getInstance().getTemplate(npcTemplateId);
				int count;

				if (template1 == null) {
					_log.warning("mob data for id:" + npcTemplateId + " missing in npc table");
					System.out.println("mob data for id:" + npcTemplateId + " missing in npc table");
					spawnDat = null;
				} else {
					if (rs.getInt("count") == 0) {
						continue;
					}
					double amount_rate = MapsTable.getInstance().getMonsterAmount(rs.getShort("mapid"));
					count = calcCount(template1, rs.getInt("count"), amount_rate);
					if (count == 0) {
						continue;
					}

					spawnDat = new L1Spawn(template1);
					spawnDat.setId(rs.getInt("id"));
					spawnDat.setAmount(count);
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
					spawnDat.setMinRespawnDelay(rs.getInt("min_respawn_delay"));
					spawnDat.setMaxRespawnDelay(rs.getInt("max_respawn_delay"));
					spawnDat.setMapId(rs.getShort("mapid"));
					spawnDat.setRespawnScreen(rs.getBoolean("respawn_screen"));
					spawnDat.setMovementDistance(rs.getInt("movement_distance"));
					spawnDat.setRest(rs.getBoolean("rest"));
					spawnDat.setSpawnType(rs.getInt("near_spawn"));

					spawnDat.setName(template1.get_name());

					if (count > 1 && spawnDat.getLocX1() == 0) {
						// 복수 또한 고정 spawn의 경우는, 개체수 * 6 의 범위 spawn로 바꾼다.
						// 다만 범위가 30을 넘지 않게 한다
						int range = Math.min(count * 6, 30);
						spawnDat.setLocX1(spawnDat.getLocX() - range);
						spawnDat.setLocY1(spawnDat.getLocY() - range);
						spawnDat.setLocX2(spawnDat.getLocX() + range);
						spawnDat.setLocY2(spawnDat.getLocY() + range);
					}

					// start the spawning
					spawnDat.init();
					spawnCount += spawnDat.getAmount();
				}

				_spawntable.put(new Integer(spawnDat.getId()), spawnDat);
				if (spawnDat.getId() > _highestId) {
					_highestId = spawnDat.getId();
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
		_log.fine("총monster수 " + spawnCount + "마리");
	}

	public L1Spawn getTemplate(int Id) {
		return _spawntable.get(new Integer(Id));
	}

	public void delspawntable(int Id) {

		if (_spawntable.containsKey(Id))
			_spawntable.remove(Id);

	}

	public void addNewSpawn(L1Spawn spawn) {
		_highestId++;
		spawn.setId(_highestId);
		_spawntable.put(new Integer(spawn.getId()), spawn);
	}

	public static void storeSpawn(L1PcInstance pc, L1Npc npc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int count = 1;
			int randomXY = 12;
			int minRespawnDelay = 60;
			int maxRespawnDelay = 120;
			String note = npc.get_name();

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"INSERT INTO spawnlist SET location=?,count=?,npc_templateid=?,group_id=?,locx=?,locy=?,randomx=?,randomy=?,heading=?,min_respawn_delay=?,max_respawn_delay=?,mapid=?");
			pstm.setString(1, note);
			pstm.setInt(2, count);
			pstm.setInt(3, npc.get_npcId());
			pstm.setInt(4, 0);
			pstm.setInt(5, pc.getX());
			pstm.setInt(6, pc.getY());
			pstm.setInt(7, randomXY);
			pstm.setInt(8, randomXY);
			pstm.setInt(9, pc.getHeading());
			pstm.setInt(10, minRespawnDelay);
			pstm.setInt(11, maxRespawnDelay);
			pstm.setInt(12, pc.getMapId());
			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private static int calcCount(L1Npc npc, int count, double rate) {
		if (rate == 0) {
			return 0;
		}
		if (rate == 1 || npc.isAmountFixed()) {
			return count;
		} else {
			return NumberUtil.randomRound((count * rate));
		}

	}
}
