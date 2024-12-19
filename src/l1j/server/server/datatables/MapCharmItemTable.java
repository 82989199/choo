package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Controller.LimitShopController;
import l1j.server.server.Controller.LimitShopController.ShopLimitItem;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.SQLUtil;

public class MapCharmItemTable {
	public class CharmDropItem{
		public int mapid;
		public int DropItemid;
		public int DropItemcount;
		public boolean is_auto_loot; 
		public int chance;
		public CharmDropItem(int _mapid, int _DropItemid, int _DropItemcount, boolean _is_auto_loot, int _chance){
			mapid = _mapid;
			DropItemid = _DropItemid;
			DropItemcount = _DropItemcount;
			is_auto_loot = _is_auto_loot;
			chance = _chance;
		}
	}	
	private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,ArrayList<CharmDropItem>>> item_charm = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,ArrayList<CharmDropItem>>>();
	private static MapCharmItemTable _instance;
	public static MapCharmItemTable getInstance() {
		if (_instance == null) {
			_instance = new MapCharmItemTable();
		}
		return _instance;
	}
	public MapCharmItemTable(){
		Load();
	}
	
	public void Load(){
		for(ConcurrentHashMap<Integer,ArrayList<CharmDropItem>> temp_list : item_charm.values()){
			if(temp_list != null){
				for(ArrayList<CharmDropItem> _temp2 : temp_list.values()){
					_temp2.clear();
				}
			}
			temp_list.clear();
		}
		item_charm.clear();
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM map_charm_item");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemid = rs.getInt("itemid");
				int mapid = rs.getInt("mapid");
				int drop_itemid = rs.getInt("drop_itemid");
				int drop_itemid_count = rs.getInt("drop_itemid_count");
				String str = rs.getString("is_auto_loot");
				boolean is_auto_loot = false;
				if(str.equals("true")){
					is_auto_loot = true;
				}else {
					is_auto_loot = false;
				}				
				int chance = rs.getInt("chance");
				
				if(item_charm.containsKey(itemid)){
					ConcurrentHashMap<Integer,ArrayList<CharmDropItem>> _list = item_charm.get(itemid);					
					if(_list.containsKey(mapid)){
						ArrayList<CharmDropItem> _CharmDropItemlist = _list.get(mapid);
						_CharmDropItemlist.add(new CharmDropItem(mapid, drop_itemid, drop_itemid_count, is_auto_loot, chance));
					}else {
						ArrayList<CharmDropItem> _CharmDropItemlist = new ArrayList<CharmDropItem>();
						_CharmDropItemlist.add(new CharmDropItem(mapid, drop_itemid, drop_itemid_count, is_auto_loot, chance));
						_list.put(mapid, _CharmDropItemlist);
					}
				}else {
					ConcurrentHashMap<Integer,ArrayList<CharmDropItem>> _list = new ConcurrentHashMap<Integer,ArrayList<CharmDropItem>>();
					ArrayList<CharmDropItem> _CharmDropItemlist = new ArrayList<CharmDropItem>();
					_CharmDropItemlist.add(new CharmDropItem(mapid, drop_itemid, drop_itemid_count, is_auto_loot, chance));
					_list.put(mapid, _CharmDropItemlist);
					item_charm.put(itemid, _list);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	Random random = new Random(System.nanoTime());	
	
	/**찬스 10000 */
	public CharmDropItem getCharmItem(L1PcInstance pc, L1Character target, int mapid){
		if(pc.getAI() != null) return null;
		for(L1ItemInstance _item : pc.getInventory().getItems()){
			if(item_charm.containsKey(_item.getItemId())){
				ConcurrentHashMap<Integer,ArrayList<CharmDropItem>> _CharmDropItemlist = item_charm.get(_item.getItemId());
				if(_CharmDropItemlist != null){
					ArrayList<CharmDropItem> _list = _CharmDropItemlist.get(mapid);
					if(_list != null){
						for(CharmDropItem _CharmDropItem : _list){
							int rnd = random.nextInt(10000) + 1;
							if(rnd < _CharmDropItem.chance){
								if(_CharmDropItem.is_auto_loot){
									L1ItemInstance ttem = pc.getInventory().storeItem(_CharmDropItem.DropItemid, _CharmDropItem.DropItemcount);
									
									if(pc.RootMent == true)
										pc.sendPackets(new S_ServerMessage(403, ttem.getName() + "(" + _CharmDropItem.DropItemcount + ")"));
									
								}else {
									L1World.getInstance().getInventory(target.getX(), target.getY(), target.getMapId()).storeItem(_CharmDropItem.DropItemid, _CharmDropItem.DropItemcount);
								}
								
							}
						}
					}
				}
				
			}
		}
		return null;
	}
}
