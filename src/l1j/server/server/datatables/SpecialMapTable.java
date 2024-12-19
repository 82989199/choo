package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1SpecialMap;
import l1j.server.server.utils.SQLUtil;

public class SpecialMapTable {
	
	public static SpecialMapTable _instance;
	
	public Map<Integer, L1SpecialMap> _list = new HashMap<Integer, L1SpecialMap>();
	
	public static SpecialMapTable getInstance() {
		if (_instance == null) {
			_instance = new SpecialMapTable();
		}
		return _instance;
	}
	
	public static void reload() {
		SpecialMapTable oldInstance = _instance;
		_instance = new SpecialMapTable();
		oldInstance._list.clear();
	}
	
	private SpecialMapTable(){
		loadSpecialMap();
	}
	
	private void loadSpecialMap(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			if(_list.size() > 0){
				_list.clear();
			}
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM special_map_item");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1SpecialMap SM = new L1SpecialMap();
				int mapId = rs.getInt("맵번호");
				double expRate = rs.getInt("추가경험치배율") * 0.01;
				double dmgRate = rs.getInt("추가데미지배율") * 0.01;
				SM.setName(rs.getString("맵이름"));
				SM.setItemId(rs.getInt("아이템번호"));
				SM.setItemMinCount(rs.getInt("아이템최소갯수"));
				SM.setItemMaxCount(rs.getInt("아이템최대갯수"));
				SM.setExpRate(expRate);
				SM.setChance(rs.getInt("아이템획득확률"));
				SM.setGiveItem(rs.getBoolean("아이템획득여부"));
				SM.setDmgRate(dmgRate);
				SM.setDmgReduction(rs.getInt("데미지리덕션"));
				SM.setMdmgReduction(rs.getInt("마법데미지감소"));
				_list.put(mapId, SM);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public L1SpecialMap getSpecialMap(int mapid){
		return _list.get(mapid);
	}
	
	public boolean isSpecialMap(int mapid){
		Set<Integer> keys = _list.keySet();
		int givemon;
		boolean OK = false;
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			givemon = iterator.next();
			if(givemon == mapid){
				OK = true;
				break;
			}
		}
		return OK;
	}
}
