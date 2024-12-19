package MJShiftObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import MJShiftObject.DB.MJEShiftDBName;
import MJShiftObject.DB.Helper.MJShiftSelector;
import MJShiftObject.DB.Helper.MJShiftUpdator;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Template.CommonServerBattleInfo;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.Account;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class MJShiftObjectHelper {
	public static List<L1ItemInstance> select_pickup_items(final int object_id, final String server_identity){
		MJObjectWrapper<ArrayList<L1ItemInstance>> wrapper = new MJObjectWrapper<ArrayList<L1ItemInstance>>();
		wrapper.value = new ArrayList<L1ItemInstance>();
		MJShiftSelector.exec(String.format("select * from %s where char_id=?", MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity)), new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
			
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					int item_id = rs.getInt("item_id");
					L1Item template = ItemTable.getInstance().getTemplate(item_id);
					if(template == null){
						System.out.println(String.format("서버 이동 간에 아이템이 누락되어 해당아이템은 취소합니다. 아이디 : %d", item_id));
						continue;
					}
					L1ItemInstance item = new L1ItemInstance();
					item.setId(rs.getInt("id"));
					item.setItem(template);
					item.setCount(rs.getInt("count"));
					item.setEquipped(rs.getInt("is_equipped") != 0);
					item.setEnchantLevel(rs.getInt("enchantlvl"));
					item.setIdentified(rs.getInt("is_id") != 0);
					item.set_durability(rs.getInt("durability"));
					item.setChargeCount(rs.getInt("charge_count"));
					item.setRemainingTime(rs.getInt("remaining_time"));
					item.setLastUsed(rs.getTimestamp("last_used"));
					item.setBless(rs.getInt("bless"));
					item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
					item.setEndTime(rs.getTimestamp("end_time"));
					item.setPackage(rs.getInt("package") != 0);
					item.set_bless_level(rs.getInt("bless_level"));
					item.set_item_level(rs.getInt("item_level"));
					item.setHotel_Town(rs.getString("Hotel_Town"));
					wrapper.value.add(item);
				}
			}
		});
		return wrapper.value;
	}
	
	public static void delete_pickup_items(final int object_id, final String server_identity){
		MJShiftUpdator.exec(String.format("delete from %s where char_id=?", MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public static void update_pickup_items(final int object_id, final L1ItemInstance item, final String server_identity){
		MJShiftUpdator.exec(String.format("insert into %s set "
				+ "id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?, buy_time=?, package=?, bless_level=?, item_level=?, Hotel_Town=?",
				MJEShiftDBName.CHARACTERS_PICKUP.get_table_name(server_identity)), 
				new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, item.getId());
						pstm.setInt(++idx, item.getItem().getItemId());
						pstm.setInt(++idx, object_id);
						pstm.setString(++idx, item.getItem().getName());
						pstm.setInt(++idx, item.getCount());
						pstm.setInt(++idx, 0);
						pstm.setInt(++idx, item.getEnchantLevel());
						pstm.setInt(++idx, 0);
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
					}
				});
	}
	
	public static void delete_battle_server(final String server_identity){
		MJShiftUpdator.exec("delete from common_server_battle_reservation where server_identity=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, server_identity);
			}
		});
	}
	
	public static List<CommonServerBattleInfo> get_battle_servers_info(){
		final MJObjectWrapper<ArrayList<CommonServerBattleInfo>> wrapper = new MJObjectWrapper<ArrayList<CommonServerBattleInfo>>();
		wrapper.value = new ArrayList<CommonServerBattleInfo>();
		MJShiftSelector.exec("select * from common_server_battle_reservation", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance(rs);
					wrapper.value.add(bInfo);
				}
			}
		});
		return wrapper.value;
	}
	
	public static CommonServerBattleInfo get_battle_server_info(final String server_identity){
		final MJObjectWrapper<CommonServerBattleInfo> wrapper = new MJObjectWrapper<CommonServerBattleInfo>();
		MJShiftSelector.exec("select * from common_server_battle_reservation where server_identity=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, server_identity);
			}
			
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance(rs);
					wrapper.value = bInfo;
				}
			}
		});
		return wrapper.value;
	}
	
	public static void reservation_server_battle(final String server_identity, final long start_millis, final long ended_millis){
		MJShiftUpdator.exec("insert into common_server_battle_reservation set "
				+ "server_identity=?, start_time=?, ended_time=? "
				+ "on duplicate key update "
				+ "start_time=?, ended_time=?", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setString(++idx, server_identity);
						Timestamp start_ts = new Timestamp(start_millis);
						Timestamp ended_ts = new Timestamp(ended_millis);						
						pstm.setTimestamp(++idx, start_ts);
						pstm.setTimestamp(++idx, ended_ts);
						pstm.setTimestamp(++idx, start_ts);
						pstm.setTimestamp(++idx, ended_ts);
					}
				});
	}
	
	public static List<CommonServerInfo> get_commons_servers(String server_identity, boolean is_exclude_my_server){
		final MJObjectWrapper<ArrayList<CommonServerInfo>> wrapper = new MJObjectWrapper<ArrayList<CommonServerInfo>>();
		wrapper.value = new ArrayList<CommonServerInfo>();
		MJShiftSelector.exec("select * from common_shift_server_info", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					CommonServerInfo csInfo = CommonServerInfo.newInstance(rs);
					if(is_exclude_my_server && csInfo.server_identity.equals(server_identity))
						continue;
					
					wrapper.value.add(csInfo);
				}
			}
		});
		return wrapper.value;
	}
	
	public static void on_shift_server_info(final String server_identity){
		update_shift_server_state(server_identity, true);
	}
	
	public static void off_shift_server_info(final String server_identity){
		update_shift_server_state(server_identity, false);
	}
	
	public static void update_shift_server_state(final String server_identity, final boolean is_on){
		MJShiftUpdator.exec("update common_shift_server_info set server_is_on=? where server_identity=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, String.valueOf(is_on));
				pstm.setString(2, server_identity);				
			}
		});
	}
	
	public static void delete_shift_object(final MJShiftObject sobj, final String server_identity){
		MJShiftUpdator.exec(String.format("delete from %s where source_id=?", MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity)), 
				new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setInt(1, sobj.get_source_id());
					}
				});
	}
	public static void truncate_shift_datas(String server_identity, boolean is_truncate_pickup){
		for(MJEShiftDBName dbname : MJEShiftDBName.values()){
			if(dbname.equals(MJEShiftDBName.CHARACTERS_SLOT_ITEMS))
				continue;
			if(dbname.equals(MJEShiftDBName.CHARACTERS_TAMS))
				continue;
			if(!is_truncate_pickup && dbname.equals(MJEShiftDBName.CHARACTERS_PICKUP))
				continue;
			
			MJShiftUpdator.truncate(dbname.get_table_name(server_identity));
		}
	}
	
	public static MJShiftObject select_shift_object(final int source_id, final String server_identity){
		MJObjectWrapper<MJShiftObject> wrapper = new MJObjectWrapper<MJShiftObject>();
		MJShiftSelector.exec(String.format("select * from %s where source_id=?", MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity)), new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, source_id);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next())
					wrapper.value = MJShiftObject.newInstance(rs);
			}
		});
		return wrapper.value;
	}
	
	public static void update_shift_object(final MJShiftObject sobj, final String server_identity){
		MJShiftUpdator.exec(String.format("insert into %s set "
				+ "source_id=?, destination_id=?, shift_type=?, source_character_name=?, source_account_name=?, destination_character_name=?, destination_account_name=?"
				+ "on duplicate key update "
				+ "destination_id=?, shift_type=?, source_character_name=?, source_account_name=?, destination_character_name=?, destination_account_name=?"
				, MJEShiftDBName.OBJECT_CONVERTER.get_table_name(server_identity)), 
				new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, sobj.get_source_id());
						pstm.setInt(++idx, sobj.get_destination_id());
						pstm.setString(++idx, sobj.get_shift_type().to_name());
						pstm.setString(++idx, sobj.get_source_character_name());
						pstm.setString(++idx, sobj.get_source_account_name());
						pstm.setString(++idx, sobj.get_destination_character_name());
						pstm.setString(++idx, sobj.get_destination_account_name());
						
						pstm.setInt(++idx, sobj.get_destination_id());
						pstm.setString(++idx, sobj.get_shift_type().to_name());
						pstm.setString(++idx, sobj.get_source_character_name());
						pstm.setString(++idx, sobj.get_source_account_name());
						pstm.setString(++idx, sobj.get_destination_character_name());
						pstm.setString(++idx, sobj.get_destination_account_name());
					}
				});
	}
	
	public static void shuffle_account_password(final String account_name){
		Updator.exec("update accounts set login=? where login=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, String.format("[%s]", account_name));
				pstm.setString(2, account_name);
			}
		});
	}
	
	public static void shuffle_character_name(final int object_id, final String char_name, final String account_name){
		Updator.exec("update characters set char_name=?, account_name=? where objid=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, String.format("[%s]", char_name));
				pstm.setString(2, String.format("[%s]", account_name));
				pstm.setInt(3, object_id);
			}
		});
	}
	
	public static void update_account_name(final Account account, final String new_account_name){
		Updator.exec("update accounts set login=? where login=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, new_account_name);
				pstm.setString(2, account.getName());
				account.setName(new_account_name);
			}
		});
	}
	
	public static void update_account_name(final L1PcInstance pc, final String new_account_name){
		Updator.exec("update characters set account_name=? where objid=?", new Handler(){

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, new_account_name);
				pstm.setInt(2, pc.getId());
				pc.setAccountName(new_account_name);
			}
		});
	}
}
