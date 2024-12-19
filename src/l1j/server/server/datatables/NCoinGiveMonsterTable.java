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
import l1j.server.server.templates.L1NCoinMonster;
import l1j.server.server.utils.SQLUtil;

public class NCoinGiveMonsterTable {
	
	public static NCoinGiveMonsterTable _instance;
	
	public Map<Integer, L1NCoinMonster> _list = new HashMap<Integer, L1NCoinMonster>();
	
	public static NCoinGiveMonsterTable getInstance() {
		if (_instance == null) {
			_instance = new NCoinGiveMonsterTable();
		}
		return _instance;
	}
	
	public static void reload() {
		NCoinGiveMonsterTable oldInstance = _instance;
		_instance = new NCoinGiveMonsterTable();
		oldInstance._list.clear();
	}
	
	private NCoinGiveMonsterTable(){
		loadGiveMonster();
	}
	
	private void loadGiveMonster(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ncoin_give_monster");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1NCoinMonster CM = new L1NCoinMonster();
				int npcid = rs.getInt("엔피씨아이디");
				CM.setNpcName(rs.getString("엔피씨이름"));
				CM.setNCoin(rs.getInt("지급엔코인"));
				CM.setEffectNum(rs.getInt("이펙트번호"));
				CM.setAllEffect(rs.getInt("이펙전체보이기") == 1 ? true : false);//1이면 다보이고 0이면 나만
				CM.setMent(rs.getInt("멘트여부") == 1 ? true : false);
				CM.setGiveItem(rs.getInt("아이템지급여부") == 1? true : false);
				CM.setItemId(rs.getInt("아이템번호"));
				CM.setItemCount(rs.getInt("아이템갯수"));
				_list.put(npcid, CM);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public L1NCoinMonster getNCoinGiveMonster(int npcid){
		return _list.get(npcid);
	}
	
	public boolean isNCoinMonster(int npcid){
		Set<Integer> keys = _list.keySet();
		int givemon;
		boolean OK = false;
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			givemon = iterator.next();
			if(givemon == npcid){
				OK = true;
				break;
			}
		}
		return OK;
	}
}
