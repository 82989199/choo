package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_ShopSellList;
import l1j.server.server.utils.SQLUtil;

public class ItemShopTable {
	
	public class ItemShop{
		public int itemid;
		public int npcid;
		public String type;
		public ItemShop(int _itemid, int _npcid, String _type){
			itemid = _itemid;
			npcid = _npcid;
			type = _type;
		}
	}
	private final Map<Integer, ItemShop> _npcShops = new HashMap<Integer, ItemShop>();
	private final ArrayList<Integer> _npclist = new ArrayList<Integer>();
	
	private static ItemShopTable _instance;
	
	public static ItemShopTable getInstance() {
		if (_instance == null) {
			_instance = new ItemShopTable();
		}
		return _instance;
	}
	public ItemShopTable(){
		load();
	}
	
	public void reload(){
		load();
	}
	
	private void load(){
		_npcShops.clear();
		_npclist.clear();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM item_shop");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemid = rs.getInt("itemid");
				int npcid = rs.getInt("npc_id");
				_npcShops.put(itemid, new ItemShop(itemid, npcid, rs.getString("type")));
				if(_npclist.contains(npcid) == false){
					_npclist.add(npcid);
				}
			}
		} catch (SQLException e) {			
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public boolean UseItemShop(L1PcInstance pc, int itemid){
		if(_npcShops.containsKey(itemid)){
			ItemShop _ItemShop = _npcShops.get(itemid);
			switch(_ItemShop.type){
				case "shop":
					L1NpcInstance npcObj = L1World.getInstance().findNpc(_ItemShop.npcid);
					if(npcObj != null){
						pc.sendPackets(new S_ShopSellList(npcObj.getId(), pc));
					}
					return true;
				default:
					break;
			}
		}
		return false;
	}
	
	public boolean isNpc(int npcid){
		if(_npclist.contains(npcid)){
			return true;
		}
		return false;
	}
	
	
	
	
	

}
