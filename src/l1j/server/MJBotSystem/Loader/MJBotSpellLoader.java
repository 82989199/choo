package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotSpell;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class MJBotSpellLoader {
	private static MJBotSpellLoader _instance;
	public static MJBotSpellLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotSpellLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJBotSpellLoader tmp = _instance;
		_instance = new MJBotSpellLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private ArrayList<ArrayList<MJBotSpell>> _buffs;
	private ArrayList<ArrayList<MJBotSpell>> _skills;
	private MJBotSpellLoader(){
		MJBotSpell 				spell	= null;
		Connection 				con		= null;
		PreparedStatement 		pstm 	= null;
		ResultSet 				rs		= null;
		ArrayList<Integer>		classes = null;
		_buffs							= new ArrayList<ArrayList<MJBotSpell>>(8);
		_skills							= new ArrayList<ArrayList<MJBotSpell>>(8);
		for(int i=0; i<8; i++){
			_buffs.add(new ArrayList<MJBotSpell>(8));
			_skills.add(new ArrayList<MJBotSpell>(8));
		}
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_spell");
			rs		= pstm.executeQuery();
			while(rs.next()){
				classes 		= MJCommons.parseToIntArray(rs.getString("classes"), ",");
				if(classes == null)
					continue;
								
				spell 				= new MJBotSpell();
				spell.skillId 		= rs.getInt("skillId");
				spell.dice			= rs.getInt("dice");
				spell.delay			= rs.getInt("delay");
				spell.direction		= rs.getInt("direction");
				spell.spellTarget	= MJBotUtil.spellTargetAnalyst(rs.getString("target"));
				String s		= rs.getString("type");
				if(s.equals("버프")){
					for(Integer i : classes)
						_buffs.get(i).add(spell);					
				}else{
					for(Integer i : classes)
						_skills.get(i).add(spell);										
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public ArrayList<MJBotSpell> getBuffs(int i){
		return _buffs.get(i);
	}
	
	public ArrayList<MJBotSpell> getSkills(int i){
		return _skills.get(i);
	}
	
	public MJBotSpell get(int i){
		for(ArrayList<MJBotSpell> list : _buffs){
			for(MJBotSpell sp : list){
				if(sp.skillId == i)
					return sp;
			}
		}
		
		for(ArrayList<MJBotSpell> list : _skills){
			for(MJBotSpell sp : list){
				if(sp.skillId == i)
					return sp;
			}
		}
		return null;
	}
	
	public void clear(){
		if(_buffs != null){
			for(ArrayList<MJBotSpell> list : _buffs)
				list.clear();
			
			_buffs.clear();
			_buffs = null;
		}
		
		if(_skills != null){
			for(ArrayList<MJBotSpell> list : _skills)
				list.clear();
			
			_skills.clear();
			_skills = null;
		}
	}
}
