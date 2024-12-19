package l1j.server.MJBotSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotBrain;
import l1j.server.MJBotSystem.MJBotDropItem;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

public class MJBotDropItemLoader {
	private static MJBotDropItemLoader _instance;
	public static MJBotDropItemLoader getInstance(){
		if(_instance == null)
			_instance = new MJBotDropItemLoader();
		return _instance;
	}
	
	public static void reload(){
		MJBotDropItemLoader tmp = _instance;
		_instance = new MJBotDropItemLoader();
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
	
	private HashMap<Integer, ArrayList<MJBotDropItem>> _dropItems;
	private MJBotDropItemLoader(){
		Connection 					con		= null;
		PreparedStatement 			pstm 	= null;
		ResultSet 					rs		= null;
		MJBotDropItem				item	= null;
		ArrayList<MJBotDropItem> 	list	= null;
		ArrayList<Integer>			types	= null;
		_dropItems							= new HashMap<Integer, ArrayList<MJBotDropItem>>(8);
		for(int i=0; i<8; i++)
			_dropItems.put(i, new ArrayList<MJBotDropItem>(16));
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from tb_mjbot_dropItem");
			rs		= pstm.executeQuery();
			while(rs.next()){
				types = MJCommons.parseToIntArray(rs.getString("classes"), ",");
				if(types == null)
					continue;
				
				item 			= new MJBotDropItem();
				item.itemId 	= rs.getInt("itemid");
				item.count		= rs.getInt("count");
				item.enchant	= rs.getInt("enchant");
				item.attrLevel	= Integer.parseInt(rs.getString("attrEnchant"));
				item.dice		= rs.getInt("dice");
				for(Integer i : types){
					list = _dropItems.get(i);
					if(list != null){
						list.add(item);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public ArrayDeque<MJBotDropItem> getMixDropItems(MJBotAI ai){
		L1PcInstance body 	= ai.getBody();
		MJBotBrain brn		= ai.getBrain();
		if(body == null || brn == null)
			return null;
		
		ArrayList<MJBotDropItem> list = _dropItems.get(body.getType());
		if(list == null)
			return null;
		
		int dropCount 	= 0;
		int initCount	= 3;
		int lawful		= body.getLawful();
		if(lawful != 0)
			initCount += ((lawful / 10000) * -1);
		
		dropCount = brn.toRand(initCount);
		if(dropCount <= 0)
			return null;
		
		int size = list.size();
		if(size <= dropCount)
			return new ArrayDeque<MJBotDropItem>(list);
		
		HashMap<Integer, MJBotDropItem> items = new HashMap<Integer, MJBotDropItem>(dropCount);
		while(items.size() < dropCount){
			int idx = brn.toRand(list.size());
			items.put(idx, list.get(idx));
		}

		return new ArrayDeque<MJBotDropItem>(items.values());
	}
	
	public void clear(){
		if(_dropItems != null){
			for(ArrayList<MJBotDropItem> list : _dropItems.values())
				list.clear();
			_dropItems.clear();
			_dropItems = null;
		}
	}
}
