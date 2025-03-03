package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_AuctionBoard extends ServerBasePacket {

	private static final String S_AUCTIONBOARD = "[S] S_AuctionBoard";

	public S_AuctionBoard(L1NpcInstance board) {
		buildPacket(board);
	}

	private void buildPacket(L1NpcInstance board) {
		ArrayList<Integer> houseList = new ArrayList<Integer>();
		int houseId = 0;
		int count = 0;
		int[] id = null;
		String[] name = null;
		int[] area = null;
		int[] month = null;
		int[] day = null;
		int[] price = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction");
			rs = pstm.executeQuery();
			while (rs.next()) {
				houseId = rs.getInt(1);
				if (board.getX() == 33424 && board.getY() == 32824) { // 경매 게시판(기란)
					if (houseId >= 262145 && houseId <= 262189) {
						houseList.add(houseId);
						count++;
					}
				} else if (board.getX() == 33585 && board.getY() == 33235) { // 경매 게시판(하이네)
					if (houseId >= 327681 && houseId <= 327691) {
						houseList.add(houseId);
						count++;
					}
				} else if (board.getX() == 33959 && board.getY() == 33253) { // 경매 게시판(아덴)
					if (houseId >= 458753 && houseId <= 458819) {
						houseList.add(houseId);
						count++;
					}
				} else if (board.getX() == 32612 && board.getY() == 32775) { // 경매 게시판(글루디오)
					if (houseId >= 524289 && houseId <= 524294) {
						houseList.add(houseId);
						count++;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			id = new int[count];
			name = new String[count];
			area = new int[count];
			month = new int[count];
			day = new int[count];
			price = new int[count];
			Calendar cal = null;
			for (int i = 0; i < count; ++i) {
				pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
				houseId = houseList.get(i);
				pstm.setInt(1, houseId);
				rs = pstm.executeQuery();
				while (rs.next()) {
					id[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					area[i] = rs.getInt(3);
					cal = timestampToCalendar((Timestamp) rs.getObject(4));
					month[i] = cal.get(Calendar.MONTH) + 1;
					day[i] = cal.get(Calendar.DATE);
					price[i] = rs.getInt(5);
				}
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		writeC(Opcodes.S_AGIT_LIST);
		writeD(board.getId());
		writeH(count); // 레코드수
		for (int i = 0; i < count; ++i) {
			writeD(id[i]); // 아지트의 번호
			writeS(name[i]); // 아지트의 이름
			writeH(area[i]); // 아지트의 넓이
			writeC(month[i]); // 마감월
			writeC(day[i]); // 마감일
			writeD(price[i]); // 현재의 입찰 가격
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
		return S_AUCTIONBOARD;
	}
}
