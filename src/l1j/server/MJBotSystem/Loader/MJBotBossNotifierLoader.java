package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotBossNotifier;
import l1j.server.server.utils.SQLUtil;

public class MJBotBossNotifierLoader {
	private static MJBotBossNotifierLoader _instance;
	public static MJBotBossNotifierLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotBossNotifierLoader();
		return _instance;
	}
	
	public static void reload(){
		MJBotBossNotifierLoader tmp = _instance;
		_instance = new MJBotBossNotifierLoader();
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
	
	private HashMap<Integer, MJBotBossNotifier> _notifiers;
	private MJBotBossNotifierLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		MJBotBossNotifier	ntf		= null;
		_notifiers					= new HashMap<Integer, MJBotBossNotifier>(8);
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_bossNotifier");
			rs		= pstm.executeQuery();
			while(rs.next()){
				ntf	= new MJBotBossNotifier();
				ntf.npcid 	= rs.getInt("npcid");
				ntf.aggro	= rs.getInt("aggro");
				_notifiers.put(ntf.npcid, ntf);
			}
		}catch(Exception e){
			
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public MJBotBossNotifier get(int i){
		return _notifiers.get(i);
	}
	
	public void clear(){
		if(_notifiers != null){
			_notifiers.clear();
			_notifiers = null;
		}
	}
}
