package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotFishInfo;
import l1j.server.server.utils.SQLUtil;

public class MJBotFishInfoLoader {
	private static int 			_lastId = 1;
	private static final Object	_lock	= new Object();
	
	public static void reset(){
		synchronized(_lock){
			_lastId = 0;
		}
	}
	
	public static MJBotFishInfo next(){
		synchronized(_lock){
			Connection 			con		= null;
			PreparedStatement 	pstm 	= null;
			ResultSet 			rs		= null;
			MJBotFishInfo		fInfo	= null;
			try{
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm 	= con.prepareStatement("select * from tb_mjbot_fishInfo where id=?");
				pstm.setInt(1, _lastId++);
				rs		= pstm.executeQuery();
				if(rs.next()){
					fInfo = new MJBotFishInfo();
					fInfo.x 	= rs.getInt("x");
					fInfo.y 	= rs.getInt("y");
					fInfo.h 	= rs.getInt("h");
					fInfo.mid 	= rs.getInt("mid");
					fInfo.fx	= rs.getInt("fx");
					fInfo.fy	= rs.getInt("fy");
					return fInfo;
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				SQLUtil.close(rs, pstm, con);
			}
			
			return fInfo;
		}
	}
}
