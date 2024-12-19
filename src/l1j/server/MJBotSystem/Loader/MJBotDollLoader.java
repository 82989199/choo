package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class MJBotDollLoader {
	public static int 				_currentLevel = 1;
	private static MJBotDollLoader 	_instance;
	public static MJBotDollLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotDollLoader();
		return _instance;
	}
	
	public static void reload(){
		MJBotDollLoader tmp = _instance;
		_instance = new MJBotDollLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	private HashMap<Integer, ArrayList<Integer>> _dollIds;
	private MJBotDollLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		try{
			_dollIds				= new HashMap<Integer, ArrayList<Integer>>(8);
			for(int i=0; i<8; i++)
				_dollIds.put(i, new ArrayList<Integer>(8));
			
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_dolls where level=?");
			pstm.setInt(1, _currentLevel);
			rs		= pstm.executeQuery();
			while(rs.next()){
				int cid = MJBotUtil.classNameToId(rs.getString("class"));
				ArrayList<Integer> list = _dollIds.get(cid);
				if(list == null)
					continue;
				
				list.addAll(MJCommons.parseToIntArray(rs.getString("dolls"), ","));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public ArrayList<Integer> get(int i){
		return _dollIds.get(i);
	}
	
	public void clear(){
		if(_dollIds != null){
			for(ArrayList<Integer> list : _dollIds.values())
				list.clear();
			
			_dollIds.clear();
			_dollIds = null;
		}
	}
}
