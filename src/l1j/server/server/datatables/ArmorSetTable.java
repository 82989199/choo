package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1ArmorSets;
import l1j.server.server.utils.SQLUtil;

public class ArmorSetTable {
	
	private static Logger _log = Logger.getLogger(ArmorSetTable.class.getName());

	private static ArmorSetTable _instance;

	private final ArrayList<L1ArmorSets> _armorSetList = new ArrayList<L1ArmorSets>();
	
	public static ArmorSetTable getInstance() {
		if (_instance == null) {
			_instance = new ArmorSetTable();
		}
		return _instance;
	}

	private ArmorSetTable() {
		load();
	}
	
	public static void reload() {
		ArmorSetTable oldInstance = _instance;
		_instance = new ArmorSetTable();
		oldInstance._armorSetList.clear();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM armor_set");
			rs = pstm.executeQuery();
			fillTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating armor_set table", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillTable(ResultSet rs) throws SQLException {
		L1ArmorSets as  = null;
		while (rs.next()) {
			as = new L1ArmorSets();
			as.setId(rs.getInt("id"));
			as.setSets(rs.getString("sets"));
			as.set_main_id(rs.getInt("main_id"));
			as.setPolyId(rs.getInt("polyid"));
			as.setAc(rs.getInt("ac"));
			as.setHp(rs.getInt("hp"));
			as.setMp(rs.getInt("mp"));
			as.setHpr(rs.getInt("hpr"));
			as.setMpr(rs.getInt("mpr"));
			as.setMr(rs.getInt("mr"));
			as.setStr(rs.getInt("str"));
			as.setDex(rs.getInt("dex"));
			as.setCon(rs.getInt("con"));
			as.setWis(rs.getInt("wis"));
			as.setCha(rs.getInt("cha"));
			as.setIntl(rs.getInt("intl"));
			as.set_defense_water(rs.getInt("defense_water"));
			as.set_defense_earth(rs.getInt("defense_earth"));
			as.set_defense_wind(rs.getInt("defense_wind"));
			as.set_defense_fire(rs.getInt("defense_fire"));
			_armorSetList.add(as);
		}
	} 

	public L1ArmorSets[] getAllList() {
		return _armorSetList.toArray(new L1ArmorSets[_armorSetList.size()]);
	}

}
