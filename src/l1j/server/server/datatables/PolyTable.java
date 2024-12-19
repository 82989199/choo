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
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.utils.SQLUtil;

public class PolyTable {
	private static Logger _log = Logger.getLogger(PolyTable.class.getName());

	private static PolyTable _instance;

	private final HashMap<String, L1PolyMorph> _polymorphs = new HashMap<String, L1PolyMorph>();
	private final HashMap<Integer, L1PolyMorph> _polyIdIndex = new HashMap<Integer, L1PolyMorph>();
	private final HashMap<Integer, Integer> _rank_polys = new HashMap<Integer, Integer>();
	public static PolyTable getInstance() {
		if (_instance == null) {
			_instance = new PolyTable();
		}
		return _instance;
	}

	private PolyTable() {
		loadPolymorphs();
	}

	public static void reload() { // Gn.67
		PolyTable oldInstance = _instance;
		_instance = new PolyTable();
		oldInstance._polymorphs.clear();
		oldInstance._polyIdIndex.clear();
		oldInstance._rank_polys.clear();
	}

	private void loadPolymorphs() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM polymorphs");
			rs = pstm.executeQuery();
			fillPolyTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating polymorph table", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillPolyTable(ResultSet rs) throws SQLException {
		L1PolyMorph poly = null;
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int polyId = rs.getInt("polyid");
			int minLevel = rs.getInt("minlevel");
			int weaponEquipFlg = rs.getInt("weaponequip");
			int armorEquipFlg = rs.getInt("armorequip");
			boolean canUseSkill = rs.getBoolean("isSkillUse");
			int causeFlg = rs.getInt("cause");

			// 변신 버그 방지
			int option = 0;
			if (rs.getString("option") != null) {
				option = PolyOption(rs.getString("option"));
			}
			
			poly = new L1PolyMorph(id, name, polyId, minLevel, weaponEquipFlg, armorEquipFlg, canUseSkill, causeFlg,
					option);
			
			_polymorphs.put(name, poly);
			_polyIdIndex.put(polyId, poly);
			if(name.startsWith("rangking"))
				_rank_polys.put(polyId, polyId);
		}
		_log.config("변신 리스트 " + _polymorphs.size() + "건 로드");
	}

	public L1PolyMorph getTemplate(String name) {
		return _polymorphs.get(name);
	}

	public L1PolyMorph getTemplate(int polyId) {
		return _polyIdIndex.get(polyId);
	}

	private boolean _polyEvent;

	public boolean isPolyEvent() {
		return _polyEvent;
	}

	public void setPolyEvent(boolean i) {
		_polyEvent = i;
	}

	public L1PolyMorph find(int polyId) {
		L1PolyMorph poly = null;
		for (L1PolyMorph each : _polymorphs.values()) {
			if (each.getPolyId() == polyId) {
				poly = each;
				break;
			}
		}

		return poly;
	}
	
	// 변신 버그 방지
	private int PolyOption(String type) {
		if (type.equalsIgnoreCase("Poly_Scroll")) {
			return 1;
		}
		if (type.equalsIgnoreCase("Poly_RingMaster")) {
			return 2;
		}
		if (type.equalsIgnoreCase("Poly_Event")) {
			return 3;
		}
		if (type.equalsIgnoreCase("Poly_Class")) {
			return 4;
		}
		if (type.equalsIgnoreCase("Poly_EV")) {
			return 5;
		}
		return 0;
	}
	
	public boolean isRankingPoly(int sprId){
		return _rank_polys.containsKey(sprId);
	}

}
