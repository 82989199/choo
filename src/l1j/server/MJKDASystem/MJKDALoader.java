package l1j.server.MJKDASystem;
/**********************************
 * 
 * MJ Kill Death Assist Object Loader.
 * made by mjsoft, 2017.
 *  
 **********************************/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJKDASystem.Chart.MJKDAChartScheduler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class MJKDALoader {
	private static MJKDALoader _instance;
	public static MJKDALoader getInstance(){
		if(_instance == null)
			_instance = new MJKDALoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.store();
			_instance.clear();
			_instance = null;
		}
	}
	
	private ConcurrentHashMap<Integer, MJKDA> _kdas;
	private MJKDALoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("select * from tb_kda");
			rs		= pstm.executeQuery();
			_kdas 	= new ConcurrentHashMap<Integer, MJKDA>(SQLUtil.calcRows(rs) * 2);
			while(rs.next()){
				MJKDA kda 		= new MJKDA();
				kda.objid 		= rs.getInt("objid");
				kda.name		= rs.getString("name");
				kda.kill		= rs.getInt("killing");
				kda.death		= rs.getInt("death");
				kda.isChartView	= rs.getBoolean("is_chart");
				_kdas.put(kda.objid, kda);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void getChangeName(int objid, String newName){
		MJKDA kda = _kdas.get(objid);
		if(kda != null){
			kda.name = newName;
			_kdas.put(kda.objid, kda);
		}
	}
	
	public ArrayList<MJKDA> createSnapshot(){
		return new ArrayList<MJKDA>(_kdas.values());
	}
	
	public void install(L1PcInstance pc, boolean isBot){
		if(pc == null)
			return;
		
		MJKDA kda = _kdas.get(pc.getId());
		if(kda == null){
			kda = new MJKDA();
			kda.objid 		= pc.getId();
			kda.name		= pc.getName();
			kda.kill 			= 0;
			kda.death		= 0;
			kda.isBot		= isBot;
			kda.isChartView = true;
			_kdas.put(kda.objid, kda);
		}
		pc.setKDA(kda);
		
		if(MJKDALoadManager.KDA_CHART_RUN && kda.isChartView)
			MJKDAChartScheduler.getInstance().onLoginUser(pc);
	}
	
	public void store(){
		if(_kdas == null || _kdas.size() <= 0)
			return;
		
		Connection 			con	= null;
		PreparedStatement 	pstm	= null;
		ArrayList<MJKDA> 	kdas	= createSnapshot();
		int							size	= kdas.size();
		MJKDA 					kda 	= null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm	= con.prepareStatement("insert into tb_kda set objid=?, name=?, killing=?, death=?, is_chart=? on duplicate key update killing=?, death=?, is_chart=?");
			for(int i=0; i<size; i++){
				kda = kdas.get(i);
				if(kda == null || kda.isBot)
					continue;
				
				int idx = 0;
				pstm.setInt(++idx, kda.objid);
				pstm.setString(++idx, kda.name);
				pstm.setInt(++idx, kda.kill);
				pstm.setInt(++idx, kda.death);
				pstm.setString(++idx, kda.isChartView ? "true" : "false");
				pstm.setInt(++idx, kda.kill);
				pstm.setInt(++idx, kda.death);
				pstm.setString(++idx, kda.isChartView ? "true" : "false");
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			con.commit();
			pstm.clearBatch();
		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
		kdas.clear();
	}
	
	public void clear(){
		if(_kdas != null){
			_kdas.clear();
			_kdas = null;
		}
	}
}
