package l1j.server.MJLevelupBonus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class MJLevelBonus {
	public static void loadCharacterBonus(L1PcInstance pc){
		if(pc == null || pc.getAI() != null)
			return;
		
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 				rs			= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_character_bonus where objid=?");
			pstm.setInt(1, pc.getId());
			rs		= pstm.executeQuery();
			if(rs.next())
				pc.setCharLevelBonus(rs.getInt("level"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void storeCharacterBonus(L1PcInstance pc){
		if(pc.getAI() != null)
			return;
		
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("insert into tb_character_bonus set objid=?, level=? on duplicate key update level=?");
			pstm.setInt(1, pc.getId());
			pstm.setInt(2, pc.getCharLevelBonus());
			pstm.setInt(3, pc.getCharLevelBonus());
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
}
