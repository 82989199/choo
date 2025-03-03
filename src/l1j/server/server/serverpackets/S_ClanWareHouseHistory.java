/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_ClanWareHouseHistory extends ServerBasePacket {

	private static final String S_ClanWareHouseHistory = "[C] S_ClanWareHouseHistory";

	public S_ClanWareHouseHistory(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		Connection con = null;
		Statement pstm = null;
		ResultSet rs = null;
		int time = 0;
		int realtime = (int) (System.currentTimeMillis() / 1000);
		String itemName = null;
		String itemIndex = null;
		String charName = null;
		int itemCount = 0;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.createStatement();
			rs = pstm.executeQuery("SELECT * FROM clan_warehousehistory WHERE clan_id=" + pc.getClanid()
					+ " order by elapsed_time desc");
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			writeC(Opcodes.S_EVENT);
			writeC(117);
			writeD(count); // 글 갯수.
			while (rs.next()) {
				time = (realtime - rs.getInt("elapsed_time")) / 60;
				charName = rs.getString("char_name");
				itemName = rs.getString("item_name");
				itemCount = rs.getInt("item_count");
				itemIndex = rs.getString("item_getorput");
				writeS(charName); // 이름
				if (itemIndex.equalsIgnoreCase("맡겼습니다.")) {
					writeC(0); // 1: 찾았습니다 , 0: 맡겼습니다.
				} else {
					writeC(1);
				}
				writeS(itemName); // 아이템명
				writeD(itemCount); // 아이템 갯수
				writeD(time); // 경과 시간
			}
		} catch (SQLException e) {

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

//	private static String currentTime() {
//		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
//		Calendar cal = Calendar.getInstance(tz);
//		int year = cal.get(Calendar.YEAR) - 2000;
//		String year2;
//		if (year < 10) {
//			year2 = "0" + year;
//		} else {
//			year2 = Integer.toString(year);
//		}
//		int Month = cal.get(Calendar.MONTH) + 1;
//		String Month2 = null;
//		if (Month < 10) {
//			Month2 = "0" + Month;
//		} else {
//			Month2 = Integer.toString(Month);
//		}
//		int date = cal.get(Calendar.DATE);
//		String date2 = null;
//		if (date < 10) {
//			date2 = "0" + date;
//		} else {
//			date2 = Integer.toString(date);
//		}
//		return year2 + "/" + Month2 + "/" + date2;
//	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ClanWareHouseHistory;
	}

}
