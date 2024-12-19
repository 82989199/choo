package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class PlaySupportInfoTable {
	private static PlaySupportInfoTable _instance;
	public static PlaySupportInfoTable getInstance() {
		if(_instance == null) {
			_instance = new PlaySupportInfoTable();
		}
		return _instance;
	}
	
	private Map<Integer, SupportInfo> _list = new HashMap<Integer, SupportInfo>();
	
	public void reload() {
		Map<Integer, SupportInfo> list = new HashMap<Integer, SupportInfo>();
		load(list);
		_list = list;
	}

	private PlaySupportInfoTable() {
		load(_list);
	}

	private void load(Map<Integer, SupportInfo> list) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		SupportInfo si = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM play_support_info");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int map_id = rs.getInt("map_id");
				int min_level = rs.getInt("min_level");
				int max_level = rs.getInt("max_level");
				int item_check_type = rs.getString("item_check_type").equalsIgnoreCase("착용") ? 1 :
						rs.getString("item_check_type").equalsIgnoreCase("사용") ? 2 :
							rs.getString("item_check_type").equalsIgnoreCase("소지") ? 3 : 0;
				int item_id = rs.getInt("item_id");
				int item_count = rs.getInt("item_count");
				
				si = new SupportInfo(min_level, max_level, item_check_type, item_id, item_count);
				
				list.put(map_id, si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public SupportInfo getPlaySupportInfo(int mapid) {
		return _list.get(mapid);
	}
	
	public class SupportInfo {
		public int _min_level;
		public int _max_level;
		public int _item_check_type;
		public int _item_id;
		public int _item_count;
		
		public SupportInfo (int min_level, int max_level, int item_check_type, int item_id, int item_count) {
			_min_level = min_level;
			_max_level = max_level;
			_item_check_type = item_check_type;
			_item_id = item_id;
			_item_count = item_count;
		}
	}
}
