package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotClanInfo;
import l1j.server.MJBotSystem.MJBotName;
import l1j.server.MJBotSystem.Business.MJAIScheduler;
import l1j.server.MJBotSystem.Business.MJBotLastError;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.utils.SQLUtil;

public class MJBotNameLoader {
	private static int				_loadCount;
	private static MJBotNameLoader 	_instance;
	public static MJBotNameLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotNameLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJBotNameLoader tmp = _instance;
		_instance = new MJBotNameLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private ArrayDeque<MJBotName> _nameQ;
	private HashMap<String, String> _clanNames;
	private MJBotNameLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		MJBotName			bName	= null;
		_clanNames = new HashMap<String, String>(4);
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_name");
			rs		= pstm.executeQuery();
			_nameQ	= new ArrayDeque<MJBotName>(SQLUtil.calcRows(rs));
			while(rs.next()){
				bName = new MJBotName();
				bName.name 	= resultName(rs.getString("name"));
				bName.title	= rs.getString("title");
				bName.cName	= rs.getString("clanName");
				if(bName.title == null || bName.title.equals(""))
					bName.title = Config.GAME_SERVER_NAME;
				if(bName.cName == null || bName.equals(""))
					bName.cName = "";
				else if(!_clanNames.containsKey(bName.cName))
					_clanNames.put(bName.cName, bName.cName);
				_nameQ.offer(bName);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
			_loadCount++;
		}
	}
	
	public void clear(){
		if(_nameQ != null){
			_nameQ.clear();
			_nameQ = null;
		}
	}
	
	public MJBotName get(){
		MJBotName bName = null;
		while(!_nameQ.isEmpty()){
			bName = _nameQ.poll();
			if(!isAlreadyName(bName.name))
				break;
			
			bName = null;
		}
		return bName;
	}
	
	public List<String> getClanNames(){
		if(_clanNames.size() <= 0)
			return null;
		return new ArrayList<String>(_clanNames.keySet());
	}
	
	public void createClanLeaders(){
		for(String clanName : getClanNames()){
			MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(clanName);	
			if(cInfo != null){
				MJBotLastError result = MJAIScheduler.getInstance().setSiegeLeaderSchedule(cInfo);
				if(result.ai == null)
					System.out.println(result.message);
			}
		}
	}
	
	private String resultName(String s){
		if(_loadCount == 0)
			return s;
		
		return String.format("%s%c", s, (char)(_loadCount + 0x60));
	}
	
	public static boolean isAlreadyName(String s){
		Connection 			con 	= null;
		PreparedStatement 	pstm	= null;
		ResultSet 			rs 		= null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("SELECT char_name FROM characters WHERE char_name=?");
			pstm.setString(1, s);
			rs		= pstm.executeQuery();
			if(rs.next())
				return true;
			
			SQLUtil.close(rs, pstm);
			
			pstm 	= con.prepareStatement("SELECT name FROM tb_mjbot_clan WHERE leaderName=?");
			pstm.setString(1, s);
			rs		= pstm.executeQuery();
			return rs.next();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return true;
	}
}
