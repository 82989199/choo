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

package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_AuctionBoardRead extends ServerBasePacket {

	private static final String S_AUCTIONBOARDREAD = "[S] S_AuctionBoardRead";

	public S_AuctionBoardRead(int objectId, String house_number) {
		buildPacket(objectId, house_number);
	}

	private void buildPacket(int objectId, String house_number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			int number = Integer.valueOf(house_number);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			Calendar cal = null;
			while (rs.next()) {
				writeC(Opcodes.S_HYPERTEXT);
				writeD(objectId);
				writeS("agsel");
				writeS(house_number); // 아지트의 번호
				writeH(9); // 이하의 캐릭터 라인의 개수
				writeS(rs.getString(2)); // 아지트의 이름
				writeS(rs.getString(6)); // 아지트의 위치
				writeS(String.valueOf(rs.getString(3))); // 아지트의 넓이
				writeS(rs.getString(7)); // 이전의 소유자
				writeS(rs.getString(9)); // 현재의 입찰자
				writeS(String.valueOf(rs.getInt(5))); // 현재의 입찰 가격
				cal = timestampToCalendar((Timestamp) rs.getObject(4));
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DATE);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				writeS(String.valueOf(month)); // 마감월
				writeS(String.valueOf(day)); // 마감일
				writeS(String.valueOf(hour)); // 마감시
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private Calendar timestampToCalendar(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal;
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_AUCTIONBOARDREAD;
	}
}
