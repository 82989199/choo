package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.SQLUtil;

public class S_Letter extends ServerBasePacket {

	private static final String S_LETTER = "[S] S_Letter";

	public S_Letter(L1ItemInstance item) {
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter WHERE item_object_id=?");
			pstm.setInt(1, item.getId());
			rs = pstm.executeQuery();
			while (rs.next()) {
				writeC(Opcodes. S_MAIL_INFO);
				writeD(item.getId());
				if (item.get_gfxid() == 465) { // 열기 전
					writeH(466); // 열기 후
				} else if (item.get_gfxid() == 606) {
					writeH(605);
				} else if (item.get_gfxid() == 616) {
					writeH(615);
				} else {
					writeH(item.get_gfxid());
				}
				writeH(rs.getInt(2));
				writeS(rs.getString(3));
				writeS(rs.getString(4));
				writeByte(rs.getBytes(7));
				writeByte(rs.getBytes(8));
				writeC(rs.getInt(6)); // 텐프레
				writeS(rs.getString(5)); // 일자
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_LETTER;
	}
}
