package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_ReadLetter extends ServerBasePacket{
	private static final String S_READLETTER = "[S] S_ReadLetter";
	
	public S_ReadLetter(L1PcInstance pc, int type, int letterType, int id) {
		
		buildPacket(pc,type,letterType,id);
	}
	private void buildPacket(L1PcInstance pc, int type, int letterType, int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM letter WHERE item_object_id = ? ");
			
			pstm.setInt(1, id);
			
			rs = pstm.executeQuery();
			
			
			writeC(Opcodes. S_MAIL_INFO);
		     writeC(type); // 16:메일함 17:혈맹메일

		     if (rs.next())
		     {
		    	 writeD(rs.getInt(1));   // 게시글 넘버
		    	 writeSS(rs.getString(8)); // 내용
		    	 writeC(id); // 일
		    	 writeS(rs.getString(3)); // 보낸사람
		    	 writeSS(rs.getString(7)); // 제목
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

	public String getType() {
		return S_READLETTER;
	}
}
