package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Boss;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.PerformanceTimer;
import l1j.server.server.utils.SQLUtil;

public final class BossMonsterSpawnList {

	static private List<L1Boss> list;

	static public void init() {
        PerformanceTimer timer = new PerformanceTimer(); // Start timing the loading process
        System.out.print("** spawnlist_boss_hot table: ");

		if (list == null)
			list = new ArrayList<L1Boss>();
		synchronized (list) {
			list.clear();
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				st = con.prepareStatement("SELECT * FROM spawnlist_boss_hot");
				rs = st.executeQuery();
				while (rs.next()) {
					int npcid = rs.getInt("npcid");
					String mobname = rs.getString("monname");
					String mapname = rs.getString("mapname");
					int spawn_x = rs.getInt("spawn_x");
					int spawn_y = rs.getInt("spawn_y");
					int spawn_map = rs.getInt("spawn_map");
					int rnd_xy = rs.getInt("rnd_loc");
					int isment = rs.getInt("isment");
					int isyn = rs.getInt("isyn");
					int display_effect = rs.getInt("display_effect");
					String spawn_time = rs.getString("spawn_time");
					String[] spawn_yoil = new String[] { "전체" };
					String spawn_message = rs.getString("ment");
					String yn_spawn_message = rs.getString("yn_ment");
					int nonespawntime = rs.getInt("none_spawn_time"); // 3600을 입력하면  마지막 스폰후 1시간동안 스폰되지 않는다.
					try {
						StringTokenizer stt = new StringTokenizer(rs.getString("spawn_yoil"), "|");
						spawn_yoil = new String[stt.countTokens()];
						for (int i = 0; stt.hasMoreTokens(); ++i)
							spawn_yoil[i] = stt.nextToken();
					} catch (Exception e) {
					}

					L1Npc npc = NpcTable.getInstance().getTemplate(npcid);

					if (npc != null) {
						L1Boss b = new L1Boss();
						b.setMonName(mobname);
						b.setMapName(mapname);
						b.setNpcId(npcid);
						b.setX(spawn_x);
						b.setY(spawn_y);
						b.setYoil(spawn_yoil);
						b.setMap(spawn_map);
						b.setMent(isment == 1 ? true : false);
						b.setYn(isyn == 1 ? true : false);
						b.set_display_effect(display_effect);
						b.setMentMessage(spawn_message);
						b.setYnMessage(yn_spawn_message);
						b.setRndLoc(rnd_xy);
						b.setnonespawntime(nonespawntime);
						// 스폰시간 구분 추출.
						if (spawn_time.length() > 0) {
							StringTokenizer stt = new StringTokenizer(spawn_time, ",");
							int[][] time = new int[stt.countTokens()][2];
							int idx = 0;
							while (stt.hasMoreTokens()) {
								String boss_time = stt.nextToken();
								String boss_h = boss_time.substring(0, boss_time.indexOf(":"));
								String boss_m = boss_time.substring(boss_h.length() + 1, boss_time.length());
								time[idx][0] = Integer.valueOf(boss_h);
								time[idx][1] = Integer.valueOf(boss_m);
								idx += 1;
							}
							b.setTime(time);
						}
						list.add(b);
					}
				}
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(st);
				SQLUtil.close(con);
			}
	        System.out.println("加载完成 " + timer.get() + "ms");
	    }
	}

	static public List<L1Boss> getList() {
		synchronized (list) {
			return new ArrayList<L1Boss>(list);
		}
	}

	static public int getSize() {
		return list.size();
	}

	static public L1Boss find(int npcid) {
		synchronized (list) {
			for (L1Boss b : list) {
				if (b.getNpcId() == npcid)
					return b;
			}
			return null;
		}
	}
}
