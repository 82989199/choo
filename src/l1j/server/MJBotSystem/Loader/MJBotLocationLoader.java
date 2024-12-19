package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.server.utils.SQLUtil;

public class MJBotLocationLoader {
	private static MJBotLocationLoader _instance;
	public static MJBotLocationLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotLocationLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJBotLocationLoader tmp = _instance;
		_instance = new MJBotLocationLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private ArrayList<MJBotLocation> 	_list;
	private Random						_rnd;
	private MJBotLocationLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		MJBotLocation		loc		= null;
		_rnd = new Random(System.nanoTime());
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_location");
			rs		= pstm.executeQuery();
			_list	= new ArrayList<MJBotLocation>(SQLUtil.calcRows(rs));
			while(rs.next()){
				loc = new MJBotLocation();
				loc.isTown 	= rs.getBoolean("istown");
				loc.x		= rs.getInt("x");
				loc.y		= rs.getInt("y");
				loc.map		= rs.getInt("map");
				_list.add(loc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public MJBotLocation get(){
		return _list.get(_rnd.nextInt(_list.size()));
	}
	
	public void clear(){
		if(_list != null){
			_list.clear();
			_list = null;
		}
	}
}
