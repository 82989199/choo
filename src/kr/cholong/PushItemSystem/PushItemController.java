package kr.cholong.PushItemSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kr.cholong.PushItemSystem.Templates.PushItem;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class PushItemController implements Runnable {

	private static PushItemController _instance;

	private static Map<String, ArrayList<PushItem>> _list = new HashMap<String, ArrayList<PushItem>>();

	private static boolean PUSH_ON_OFF = true;

	public static PushItemController getInstance() {
		if (_instance == null) {
			_instance = new PushItemController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (PUSH_ON_OFF) {
					pushItemResult();
				}
				
				// 1분 단위로 체크
				Thread.sleep(60000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setPushSystem(boolean flag){
		PUSH_ON_OFF = flag;
	}
	
	public static boolean isPushSystem(){
		return PUSH_ON_OFF;
	}

	public static void close() {
		_list.clear();
	}

	public static void loadPushItem() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<PushItem> push_list = null;
		PushItem push = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM push_item_list");
			rs = pstm.executeQuery();

			while (rs.next()) {
				String time = rs.getString("result_time");
				push = new PushItem();
				push_list = _list.get(time);

				if (push_list == null) {
					push_list = new ArrayList<PushItem>();
					_list.put(time, push_list);
				}

				push.setItemId(rs.getInt("item_id"));
				push.setItemCount(rs.getInt("item_count"));
				push.setIventory(rs.getString("is_iven").equalsIgnoreCase("false") ? false : true);
				
				push_list.add(push);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pushItemResult() {
		try {
			ArrayList<PushItem> push_list = null;
			Set<String> keys = _list.keySet();
			String time = null;
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				time = iterator.next();
				if (checkTime(time)) {
					push_list = _list.get(time);
					if (push_list != null) {
						result(push_list);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkTime(String time) {
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		String[] real_time = time.split(":");
		if (cal.get(Calendar.HOUR_OF_DAY) == Integer.valueOf(real_time[0]) && cal.get(Calendar.MINUTE) == Integer.valueOf(real_time[1])) {
			return true;
		}
		return false;
	}

	private void result(ArrayList<PushItem> push_list) {
		try {
			if (push_list == null)
				return;

			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null)
					continue;
				if (!pc.isPacketSendOK())
					continue;

				L1ItemInstance item = null;
				for (PushItem pi : push_list) { 
					item = ItemTable.getInstance().createItem(pi.getItemId());
					if (item == null)
						continue;
					item.setCount(pi.getItemCount());
					
					if(pi.isIventory()){
						pc.getInventory().storeItem(item);
						pc.sendPackets(new S_ServerMessage(813, "系统推送", item.getName() + "(" + item.getCount() + ")", pc.getName()));
					} else {
						SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
						pwh.storeTradeItem(item);
						item.setIdentified(true);
//						pc.sendPackets("\\aD아이템이 도착 했습니다.(인벤토리→부가아이템창고)");
						pc.sendPackets("物品已到达(背包→附加物品仓库)");
//						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aD아이템이 도착 했습니다(인벤토리→부가아이템창고)"));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "物品已到达(背包→附加物品仓库)"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
