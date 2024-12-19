package l1j.server.server.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class LimitShopController extends Thread {
	
	public class ShopLimitItem{
		public int _npcid;
		public int _itemid;
		public int _count;
		public int _time;
		public ShopLimitItem(int npcid, int itemid, int count, int time){
			_npcid = npcid;
			_itemid = itemid;
			_count = count;
			_time = time;
		}
	}
	private static final SimpleDateFormat ss = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
	
	private final ConcurrentHashMap<Integer, Integer> limit_shop = new ConcurrentHashMap<Integer, Integer>();
	private static LimitShopController _instance;
	public static LimitShopController getInstance() {
		if (_instance == null) {
			_instance = new LimitShopController();
		}
		return _instance;
	}
	private int _time;
	public LimitShopController(){
		Calendar c = Calendar.getInstance();
		String str = ss.format(c.getTime());
		_time = Integer.parseInt(str);
		
		Load();
	}
	
	public void reload(){
		Load();
	}
	
	public void Load(){
		limit_shop.clear();
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM shop_limit");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int npcId = rs.getInt("npc_id");
				int count = rs.getInt("count");
				limit_shop.put(npcId, count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public int getLimitShop(int npcid){
		if(limit_shop.containsKey(npcid)){
			return limit_shop.get(npcid);
		}
		return -1;
	}
	
	/** false : 구매불가 , true : 구매가능 */
	public boolean CheckLimitShop(L1PcInstance pc, int npcid, int itemid, int buycount){
		if(limit_shop.containsKey(npcid)){
			if(deleteTime) return false;
			
			int count = limit_shop.get(npcid);
			boolean ck = false;
			for(ShopLimitItem _ShopLimitItem : pc.getLimitShop()){
				if(_ShopLimitItem._npcid == npcid && _ShopLimitItem._itemid == itemid){
					ck = true;
					if((_ShopLimitItem._count + buycount) > count){
						return false;
					}else {
						_ShopLimitItem._count += buycount;
						//디비 갯수 업데이트
						updateCharacterLimitShop(pc.getAccountName(), npcid, itemid, _ShopLimitItem._count);						
						return true;
					}
				}
			}
			if(ck == false){
				if(buycount > count){
					return false;
				}else {
					// 디비 추가
					pc.getLimitShop().add(new ShopLimitItem(npcid, itemid, buycount, _time));
					storeCharacterLimitShop(pc.getAccountName(), npcid, itemid, buycount);
					return true;
				}
			}
		}
		return true;
	}
	
	public void updateCharacterLimitShop(String accountName, int npc_id, int item_id, int count) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_limit_shop SET count=? WHERE account=? AND npc_id=? AND item_id=?");
			pstm.setInt(1, count);
			
			pstm.setString(2, accountName);
			pstm.setInt(3, npc_id);
			pstm.setInt(4, item_id);
			
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void storeCharacterLimitShop(String accountName, int npc_id, int item_id, int count) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_limit_shop SET account=?, npc_id=?, item_id=?, count=?, time=?");
			pstm.setString(1, accountName);
			pstm.setInt(2, npc_id);
			pstm.setInt(3, item_id);
			pstm.setInt(4, count);
			pstm.setInt(5, _time);
			
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	
	
	
	public void LoadLimitpc(L1PcInstance pc){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_limit_shop WHERE account=?");
			pstm.setString(1, pc.getAccountName());
			rs = pstm.executeQuery();
			boolean ck = false;
			while (rs.next()) {
				int npcId = rs.getInt("npc_id");
				int itemid = rs.getInt("item_id");
				int count = rs.getInt("count");
				int time = rs.getInt("time");
				if(_time > time){
					ck = true;
					break;
				}
				pc.getLimitShop().add(new ShopLimitItem(npcId,itemid,count, _time));
			}
			if(ck){
				deleteCharacterLimitShop(pc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void deleteCharacterLimitShop(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			pc.getLimitShop().clear();
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_limit_shop WHERE account=?");
			pstm.setString(1, pc.getAccountName());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void AlldeleteCharacterLimitShop() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_limit_shop");
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static boolean deleteTime = false;
	
	@Override
	public void run() {
		while(true){
			try {
			Thread.sleep(60 * 1000);
			Calendar c = Calendar.getInstance();
			String str = ss.format(c.getTime());
			int curtime = Integer.parseInt(str);
			if(curtime > _time){
				/**초기화*/
				deleteTime = true;
				AlldeleteCharacterLimitShop();
				_time = curtime;
				for(L1PcInstance _pc : L1World.getInstance().getAllPlayers()){
					try {
					_pc.getLimitShop().clear();
					} catch (Exception e1) {}
				}
				deleteTime = false;
			}
			} catch (Exception e1) {}
		}
	}
	
	
	
}
