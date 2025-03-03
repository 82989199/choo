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

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_CharacterConfig extends ServerBasePacket {

	private static final String S_CHARACTER_CONFIG = "[S] S_CharacterConfig";

	public S_CharacterConfig(int objectId) {
		buildPacket(objectId);
	}

	private void buildPacket(int objectId) {
		int length = 0;
		byte data[] = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_config WHERE object_id=?");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				length = rs.getInt(2);
				data = rs.getBytes(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		if (length != 0) {
			writeC(Opcodes.S_EVENT);
			writeC(41);
			writeD(length);
			writeByte(data);
			writeH(0x00);
		} else {
			writeC(Opcodes.S_EVENT);
			writeC(0x29);
			writeD(0);
			writeH(0x00);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHARACTER_CONFIG;
	}
}
