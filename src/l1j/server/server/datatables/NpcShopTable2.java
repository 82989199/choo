package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class NpcShopTable2 {

	private static Logger _log = Logger.getLogger(NpcShopTable2.class.getName());

	private static NpcShopTable2 _instance;

	private final Map<Integer, L1Shop> _npcShops = new HashMap<Integer, L1Shop>();

	public static NpcShopTable2 getInstance() {
		if (_instance == null) {
			_instance = new NpcShopTable2();
		}
		return _instance;
	}

	public static void reloding() {
		NpcShopTable2 oldInstance = _instance;
		_instance = new NpcShopTable2();
		if(oldInstance != null && oldInstance._npcShops != null)
			oldInstance._npcShops.clear();
	}

	private NpcShopTable2() {
		loadShops();
	}

	private ArrayList<Integer> enumNpcIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop_npc2");
			rs = pstm.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("npc_id"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "NpcShopTable2[]Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return ids;
	}

	private L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
		L1ShopItem item = null;
		while (rs.next()) {
			int itemId = rs.getInt("item_id");
			int sellingPrice = rs.getInt("selling_price");
			int purchasingPrice = rs.getInt("purchasing_price");
			int count = rs.getInt("count");
			int enchant = rs.getInt("enchant");
			if (0 <= sellingPrice) {
				item = new L1ShopItem(itemId, sellingPrice, 1, enchant, false);
				item.set_count(count);
				sellingList.add(item);
			}
			if (0 <= purchasingPrice) {
				item = new L1ShopItem(itemId, purchasingPrice, 1, enchant, false);
				purchasingList.add(item);
			}
		}
		return new L1Shop(npcId, sellingList, purchasingList);
	}

	private void loadShops() {
		ArrayList<Integer> npcids = enumNpcIds();
		for(Integer i : npcids){
			Selector.exec("select * from shop_npc2 where npc_id=?", new SelectorHandler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, i);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					_npcShops.put(i, loadShop(i, rs));
				}
			});
		}
	}

	public L1Shop get(int npcId) {
		return _npcShops.get(npcId);
	}
	
	/** 2016.11.24 MJ 앱센터 시세 **/
	public  Map<Integer, L1Shop> get(){
		return _npcShops;
	}
}
