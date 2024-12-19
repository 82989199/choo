package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotInvItem;
import l1j.server.server.utils.SQLUtil;
/**********************************
 * 
 * MJ Bot Inventory Item Loader.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotInvItemLoader {
	private static MJBotInvItemLoader _instance;
	public static MJBotInvItemLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotInvItemLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJBotInvItemLoader tmp = _instance;
		_instance = new MJBotInvItemLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private HashMap<Integer, MJBotInvItem> _invItems;
	private MJBotInvItemLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs		= null;
		MJBotInvItem		item	= null;
		_invItems					= new HashMap<Integer, MJBotInvItem>(64);
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_inventory");
			rs		= pstm.executeQuery();
			while(rs.next()){
				item 			= new MJBotInvItem();
				item.id 		= rs.getInt("id");
				item.itemId 	= rs.getInt("itemid");
				item.enchant 	= rs.getInt("enchant");
				item.attrLevel 	= rs.getInt("attrlevel");
				item.isEquip	= rs.getBoolean("equipped");
				_invItems.put(item.id, item);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public MJBotInvItem get(int i){
		return _invItems.get(i);
	}
	
	public void clear(){
		if(_invItems != null){
			_invItems.clear();
			_invItems = null;
		}
	}

}
