package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotClanInfo;
import l1j.server.server.utils.SQLUtil;

public class MJBotClanInfoLoader {
	private static MJBotClanInfoLoader _instance;
	public static MJBotClanInfoLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotClanInfoLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJBotClanInfoLoader tmp = _instance;
		_instance = new MJBotClanInfoLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private HashMap<String, MJBotClanInfo> _infos;
	private MJBotClanInfoLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		MJBotClanInfo		info	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_clan");
			rs		= pstm.executeQuery();
			_infos	= new HashMap<String, MJBotClanInfo>(2);
			while(rs.next()){
				info = new MJBotClanInfo();
				info.clanName 	= rs.getString("name");
				info.leaderName	= rs.getString("leaderName");
				info.emblemId	= rs.getInt("emblem");
				info.leaderX	= rs.getInt("positionX");
				info.leaderY 	= rs.getInt("positionY");
				info.clanObject	= null;
				info.leaderAI	= null;
				_infos.put(info.clanName, info);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public MJBotClanInfo get(String s){
		return _infos.get(s);
	}
	
	public void clear(){
		if(_infos != null){
			_infos.clear();
			_infos = null;
		}
	}
}
