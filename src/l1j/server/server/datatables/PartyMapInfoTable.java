package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class PartyMapInfoTable {
	private static PartyMapInfoTable _instance;
	
	public static PartyMapInfoTable getInstance(){
		if(_instance == null){
			_instance = new PartyMapInfoTable();
		}
		return _instance;
	}
	
	private Map<Integer, Double> _list = new HashMap<Integer, Double>();
	
	public static void reload() {
		PartyMapInfoTable oldInstance = _instance;
		_instance = new PartyMapInfoTable();
		oldInstance._list.clear();
	}
	
	private PartyMapInfoTable(){
		loadList();
	}
	
	private void loadList(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM party_map_info");
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				int mapid = rs.getInt("map_id");
				double exp = rs.getInt("exp_rate") * 0.01;
				
				_list.put(mapid, exp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public double getPartyMapExpRate(int mapid){
		if(_list.get(mapid) == null){
			return 0;
		}
		
		return _list.get(mapid);
	}
}
