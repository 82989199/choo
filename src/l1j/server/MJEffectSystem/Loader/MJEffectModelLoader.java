package l1j.server.MJEffectSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJEffectSystem.MJEffectModel;
import l1j.server.server.utils.SQLUtil;

public class MJEffectModelLoader {
	private static MJEffectModelLoader _instance;
	public static MJEffectModelLoader getInstance(){
		if(_instance == null)
			_instance = new MJEffectModelLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJEffectModelLoader tmp = _instance;
		_instance = new MJEffectModelLoader();
		tmp.clear();
		tmp = null;
	}
	
	private HashMap<Integer, MJEffectModel> _models;
	private MJEffectModelLoader(){
		Connection 			con			= null;
		PreparedStatement 	pstm 		= null;
		ResultSet 			rs 			= null;
		MJEffectModel		model		= null;
		int[]				ids			= null;
		_models							= new HashMap<Integer, MJEffectModel>(32);
		try{
			con 		= L1DatabaseFactory.getInstance().getConnection();
			pstm 		= con.prepareStatement("select * from tb_mjeffects");
			rs			= pstm.executeQuery();
			while(rs.next()){
				model	= new MJEffectModel();
				model.setType(rs.getString("type"));
				model.setEffectId(rs.getInt("effectId"));
				model.setActionId(rs.getInt("actId"));
				model.setImpact(rs.getInt("impact"));
				model.setRange(rs.getInt("range"));
				model.setDelay(rs.getInt("delay"));
				model.setDamageMin(rs.getInt("dmgMin"));
				model.setDamageMax(rs.getInt("dmgMax"));
				model.setZPos(rs.getInt("zpos"));
				model.setRatio(rs.getInt("ratio"));
				ids	= parseIntArray(rs.getString("owner_ids"));
				if(ids == null)
					continue;
				
				for(int i=0; i<ids.length; i++)
					_models.put(ids[i], model);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void clear(){
		if(_models != null){
			_models.clear();
			_models = null;
		}
	}
	
	private int[] parseIntArray(String s){
		String[] arrs	= s.split(",");
		int size		= arrs.length;
		int[]	results	= new int[size];
		for(int i=0; i<size; i++)
			results[i] = Integer.parseInt(arrs[i]);
		return results;
	}
	
	public MJEffectModel get(int i){
		return _models.get(i);
	}
}
