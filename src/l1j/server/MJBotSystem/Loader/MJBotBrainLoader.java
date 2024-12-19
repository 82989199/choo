package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotBrain;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

/**********************************
 * 
 * MJ Bot Brain Loader.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotBrainLoader {
	private static MJBotBrainLoader _instance;
	public static MJBotBrainLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotBrainLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJBotBrainLoader tmp = _instance;
		_instance = new MJBotBrainLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private HashMap<Integer, MJBotBrain> _brains;
	private MJBotBrainLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 				rs			= null;
		MJBotBrain			brn		= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_brain");
			rs		= pstm.executeQuery();
			_brains	= new HashMap<Integer, MJBotBrain>(SQLUtil.calcRows(rs));
			while(rs.next()){
				brn = new MJBotBrain();
				brn.setBrainId(rs.getInt("bid"));
				brn.setAge(rs.getInt("age"));
				brn.setHormon(rs.getInt("hormon"));
				brn.setPride(rs.getInt("pride"));
				brn.setSense(rs.getInt("sense"));
				brn.setWeaponId(rs.getInt("wid"));
				brn.setArmorIds(MJCommons.parseToIntArray(rs.getString("aids"), ","));
				brn.setClassType(MJBotUtil.classNameToId(rs.getString("classType")));
				_brains.put(brn.getBrainId(), brn);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void clear(){
		if(_brains != null){
			_brains.clear();
			_brains = null;
		}	
	}
	
	public MJBotBrain get(int i){
		return _brains.get(i);
	}
	
	public ArrayList<MJBotBrain> getClassBrains(int i){
		if(_brains == null)
			return null;
		
		ArrayList<MJBotBrain> list = new ArrayList<MJBotBrain>(4);
		for(MJBotBrain brn : _brains.values()){
			if(brn.getClassType() == i)
				list.add(brn);
		}
		
		return list;
	}
	
	public ArrayList<Integer> createKeySnapshot(){
		return new ArrayList<Integer>(_brains.keySet());
	}
	
	public ArrayList<MJBotBrain> createSnapshot(){
		return new ArrayList<MJBotBrain>(_brains.values());
	}
	
	public static int getMaxId(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("select max(bid)+1 as maxid from tb_mjbot_brain");
			rs		= pstm.executeQuery();
			if(rs.next())
				return rs.getInt("maxid");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}
}
