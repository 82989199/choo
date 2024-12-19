package l1j.server.server.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class MySqlCharactersItemStorage extends CharactersItemStorage {

	private static final Logger _log = Logger.getLogger(MySqlCharactersItemStorage.class.getName());

	@Override
	public ArrayList<L1ItemInstance> loadItems(int objId) throws Exception {
		ArrayList<L1ItemInstance> items = null;

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			items = new ArrayList<L1ItemInstance>();
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id = ?");
			pstm.setInt(1, objId);

			L1ItemInstance item = null;
			rs = pstm.executeQuery();
			L1Item itemTemplate = null;

			while (rs.next()) {
				int itemId = rs.getInt("item_id");
				itemTemplate = ItemTable.getInstance().getTemplate(itemId);
				if (itemTemplate == null) {
					_log.warning(String.format("item id:%d not found", itemId));
					System.out.println(String.format("item id:%d not found", itemId));
					continue;
				}
				item = new L1ItemInstance();
				item.setId(rs.getInt("id"));
				item.setItem(itemTemplate);
				item.setCount(rs.getInt("count"));
				item.setEquipped(rs.getInt("Is_equipped") != 0 ? true : false);
				item.setEnchantLevel(rs.getInt("enchantlvl"));
				item.setIdentified(rs.getInt("is_id") != 0 ? true : false);
				item.set_durability(rs.getInt("durability"));
				item.setChargeCount(rs.getInt("charge_count"));
				item.setRemainingTime(rs.getInt("remaining_time"));
				item.setLastUsed(rs.getTimestamp("last_used"));
				item.setBless(rs.getInt("bless"));
				item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
				item.setSpecialEnchant(rs.getInt("special_enchant"));
				item.setEndTime(rs.getTimestamp("end_time"));
				/** 패키지상점 **/
				item.setPackage(rs.getInt("package") != 0 ? true : false);
				item.set_bless_level(rs.getInt("bless_level"));
				/**특수 인챈트 시스템**/
				item.set_item_level(rs.getInt("item_level"));
				/**여관 리뉴얼**/
				item.setHotel_Town(rs.getString("Hotel_Town"));
				item.getLastStatus().updateAll();
				if (item.getItem().getItemId() == 80500) {
					KeyTable.checkey(item);
				}
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		if(Config.USE_SHIFT_SERVER){
			List<L1ItemInstance> shift_pickups = MJShiftObjectHelper.select_pickup_items(objId, MJShiftObjectManager.getInstance().get_home_server_identity());
			if(shift_pickups != null){
				MJShiftObjectHelper.delete_pickup_items(objId, MJShiftObjectManager.getInstance().get_home_server_identity());
				for(L1ItemInstance item : shift_pickups){
					boolean is_non_add = false;
					if(item.isStackable()){
						for(L1ItemInstance i : items){
							if(i.getItemId() == item.getItemId()){
								i.setCount(i.getCount() + item.getCount());
								is_non_add = true;
							}
						}
					}
					if(!is_non_add){
						item.setId(IdFactory.getInstance().nextId());
						items.add(item);
					}
				}
			}
		}
		return items;
	}

	@Override
	public void storeItem(int objId, L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int idx = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("INSERT INTO character_items SET id = ?, item_id = ?, char_id = ?, item_name = ?, count = ?, is_equipped = 0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchantlvl = ?, end_time =?,buy_time=?, package = ?, bless_level = ?, item_level = ?, Hotel_Town = ? "
					+ "on duplicate key update item_id = ?, char_id = ?, item_name = ?, count = ?, is_equipped = 0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchantlvl = ?, end_time =?,buy_time=?, package = ?, bless_level = ?, item_level = ?, Hotel_Town=?");
			//pstm = con.prepareStatement("INSERT INTO character_items SET id = ?, item_id = ?, char_id = ?, item_name = ?, count = ?, is_equipped = 0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, attr_enchantlvl = ?, end_time =?,buy_time=?, package = ?, bless_level = ?, item_level = ?, Hotel_Town = ?");
			pstm.setInt(++idx, item.getId());
			pstm.setInt(++idx, item.getItem().getItemId());
			pstm.setInt(++idx, objId);
			pstm.setString(++idx, item.getItem().getName());
			pstm.setInt(++idx, item.getCount());
			pstm.setInt(++idx, item.getEnchantLevel());
			pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
			pstm.setInt(++idx, item.get_durability());
			pstm.setInt(++idx, item.getChargeCount());
			pstm.setInt(++idx, item.getRemainingTime());
			pstm.setTimestamp(++idx, item.getLastUsed());
			pstm.setInt(++idx, item.getBless());
			pstm.setInt(++idx, item.getAttrEnchantLevel());
			pstm.setTimestamp(++idx, item.getEndTime());
			pstm.setTimestamp(++idx, item.getEndTime());
			/** 패키지상점 **/
			pstm.setInt(++idx, item.isPackage() == false ? 0 : 1);
			pstm.setInt(++idx, item.get_bless_level());
			/**특수 인챈트 시스템**/
			pstm.setInt(++idx, item.get_item_level());
			/**여관 리뉴얼**/
			pstm.setString(++idx, item.getHotel_Town());
			
			// on duplicate key
			pstm.setInt(++idx, item.getItem().getItemId());
			pstm.setInt(++idx, objId);
			pstm.setString(++idx, item.getItem().getName());
			pstm.setInt(++idx, item.getCount());
			pstm.setInt(++idx, item.getEnchantLevel());
			pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
			pstm.setInt(++idx, item.get_durability());
			pstm.setInt(++idx, item.getChargeCount());
			pstm.setInt(++idx, item.getRemainingTime());
			pstm.setTimestamp(++idx, item.getLastUsed());
			pstm.setInt(++idx, item.getBless());
			pstm.setInt(++idx, item.getAttrEnchantLevel());
			pstm.setTimestamp(++idx, item.getEndTime());
			pstm.setTimestamp(++idx, item.getEndTime());
			pstm.setInt(++idx, item.isPackage() == false ? 0 : 1);
			pstm.setInt(++idx, item.get_bless_level());
			pstm.setInt(++idx, item.get_item_level());
			pstm.setString(++idx, item.getHotel_Town());
			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("디비 아이템 저장 오류 아이템명 : " + item.getName() + " 소유자 : " + item.getItemOwner().getName());
			throw e;
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		item.getLastStatus().updateAll();
	}

	@Override
	public void deleteItem(L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_items WHERE id = ?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public void updateItemAll(L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_items SET " + "item_id = ?" + ",count = ?" + ",durability = ?"
					+ ",charge_count = ?" + ",remaining_time = ?" + ",enchantlvl = ?" + ",is_equipped = ?"
					+ ",is_id = ?" + ",last_used = ?" + ",bless = ?" + ",attr_enchantlvl = ?" + ",special_enchant = ?"
					+ ",end_time = ?" + ",bless_level = ?" + ",item_level = ?" + ",Hotel_Town = ?" + " WHERE id = ?");
			pstm.setInt(1, item.getItemId());
			pstm.setInt(2, item.getCount());
			pstm.setInt(3, item.get_durability());
			pstm.setInt(4, item.getChargeCount());
			pstm.setInt(5, item.getRemainingTime());
			pstm.setInt(6, item.getEnchantLevel());
			pstm.setInt(7, (item.isEquipped() ? 1 : 0));
			pstm.setInt(8, (item.isIdentified() ? 1 : 0));
			pstm.setTimestamp(9, item.getLastUsed());
			pstm.setInt(10, item.getBless());
			pstm.setInt(11, item.getAttrEnchantLevel());
			pstm.setInt(12, item.getSpecialEnchant());
			pstm.setTimestamp(13, item.getEndTime());
			pstm.setInt(14, item.get_bless_level());
			/**특수 인챈트 시스템**/
			pstm.setInt(15, item.get_item_level());
			/**여관 리뉴얼**/
			pstm.setString(16, item.getHotel_Town());
			pstm.setInt(17, item.getId());

			pstm.execute();

			item.getLastStatus().updateItemId();
			item.getLastStatus().updateCount();
			item.getLastStatus().updateDuraility();
			item.getLastStatus().updateChargeCount();
			item.getLastStatus().updateRemainingTime();
			item.getLastStatus().updateEnchantLevel();
			item.getLastStatus().updateEquipped();
			item.getLastStatus().updateIdentified();
			item.getLastStatus().updateLastUsed();
			item.getLastStatus().updateBless();
			item.getLastStatus().updateAttrEnchantLevel();
			item.getLastStatus().updateEndTime();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public void updateItemId(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET item_id = ? WHERE id = ?", item.getItemId());
		item.getLastStatus().updateItemId();
	}

	@Override
	public void updateItemCount(L1ItemInstance item) throws Exception {
		// executeUpdate(item.getId(), "UPDATE character_items SET count = ?
		// WHERE id = ?", item.getCount());
		/** 패키지상점 **/
		executeUpdate(item.getId(), "UPDATE character_items SET count = ?, package = ? WHERE id = ?", item.getCount(),
				item.isPackage() == false ? 0 : 1);
		item.getLastStatus().updateCount();
	}

	@Override
	public void updateItemDurability(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET durability = ? WHERE id = ?", item.get_durability());
		item.getLastStatus().updateDuraility();
	}

	@Override
	public void updateItemChargeCount(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET charge_count = ? WHERE id = ?", item.getChargeCount());
		item.getLastStatus().updateChargeCount();
	}

	@Override
	public void updateItemRemainingTime(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET remaining_time = ? WHERE id = ?",
				item.getRemainingTime());
		item.getLastStatus().updateRemainingTime();
	}

	@Override
	public void updateItemEnchantLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET enchantlvl = ? WHERE id = ?", item.getEnchantLevel());
		item.getLastStatus().updateEnchantLevel();
	}

	@Override
	public void updateItemEquipped(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET is_equipped = ? WHERE id = ?",
				(item.isEquipped() ? 1 : 0));
		item.getLastStatus().updateEquipped();
	}

	@Override
	public void updateItemIdentified(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET is_id = ? WHERE id = ?", (item.isIdentified() ? 1 : 0));
		item.getLastStatus().updateIdentified();
	}

	@Override
	public void updateSpecialEnchant(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET special_enchant = ? WHERE id = ?",
				item.getSpecialEnchant());
		item.getLastStatus().updateSpecialEnchant();
	}

	@Override
	public void updateItemDelayEffect(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET last_used = ? WHERE id = ?", item.getLastUsed());
		item.getLastStatus().updateLastUsed();
	}

	@Override
	public void updateItemBless(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET bless = ? WHERE id = ?", item.getBless());
		item.getLastStatus().updateBless();
	}

	@Override
	public void updateItemBlessLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET bless_level = ? WHERE id = ?", item.get_bless_level());
		item.getLastStatus().update_bless_level();
	}

	@Override
	public void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET attr_enchantlvl = ? WHERE id = ?",
				item.getAttrEnchantLevel());
		item.getLastStatus().updateAttrEnchantLevel();
	}

	@Override
	public void updateItemEndTime(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET end_time = ? WHERE id = ?", item.getEndTime());
		item.getLastStatus().updateEndTime();
	}
	
	/**특수 인챈트 시스템**/
	@Override
	public void updateItemLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET item_level = ? WHERE id = ?", item.get_item_level());
		item.getLastStatus().update_item_level();
	}
	/**특수 인챈트 시스템**/
	
	/**여관 리뉴얼**/
	@Override
	public void updateHotelTowm(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET Hotel_Town = ? WHERE id = ?", item.getHotel_Town());
		item.getLastStatus().update_town_name();
	}
	/**여관 리뉴얼**/

	@Override
	public int getItemCount(int objId) throws Exception {
		int count = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id = ?");
			pstm.setInt(1, objId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return count;
	}

	private void executeUpdate(int objId, String sql, int updateNum) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setInt(1, updateNum);
			pstm.setInt(2, objId);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void executeUpdate(int objId, String sql, String string) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setString(1, string);
			pstm.setInt(2, objId);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/** 패키지상점 **/
	private void executeUpdate(int objId, String sql, int updateNum, int updatePackage) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setInt(1, updateNum);
			pstm.setInt(2, updatePackage);
			pstm.setInt(3, objId);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void executeUpdate(int objId, String sql, Timestamp ts) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setTimestamp(1, ts);
			pstm.setInt(2, objId);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
