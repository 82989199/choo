package l1j.server.DogFight.History;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.DogFight.Game.MJDogFightGameManager;
import l1j.server.DogFight.History.Log.MJDogFightTicketLogInfo;
import l1j.server.DogFight.History.Log.MJDogFightTicketLogger;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.DogFight.Packets.S_DogFightBoard;
import l1j.server.DogFight.Packets.S_DogFightBuyList;
import l1j.server.DogFight.Packets.S_DogFightSellList;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NoSell;

public class MJDogFightHistory {
	private static MJDogFightHistory m_instance;
	public static MJDogFightHistory getInstance(){
		if(m_instance == null)
			m_instance = new MJDogFightHistory();
		return m_instance;
	}

	private int m_current_game_number;
	private MJDogFightHistoryInfo m_current_history_info;
	private MJDogFightGameManager m_current_manager;
	private HashMap<Integer, MJDogFightOldTicketInfo> m_winners_item_id;
	private MJDogFightHistory(){
		m_winners_item_id = new HashMap<Integer, MJDogFightOldTicketInfo>();
		Selector.exec("select max(game_number) + 1 as next_number from dogfight_history", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					m_current_game_number = rs.getInt("next_number");
				}else{
					m_current_game_number = 0;
				}
			}
		});
		Selector.exec("select winner_item_id, winner_dividend, ticket_price from dogfight_history", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJDogFightOldTicketInfo otInfo = MJDogFightOldTicketInfo.newInstance(rs);
					m_winners_item_id.put(otInfo.get_item_id(), otInfo);
				}
			}
		});
	}
	public int get_current_game_number(){
		return m_current_game_number;
	}
	public MJDogFightHistoryInfo create_new_history(MJDogFightGameManager manager){
		m_current_manager = manager;
		return m_current_history_info = MJDogFightHistoryInfo.newInstance(++m_current_game_number, manager.get_game_info());
	}
	public MJDogFightHistoryInfo get_current_history(){
		return m_current_history_info;
	}
	public MJDogFightGameManager get_current_manager(){
		return m_current_manager;
	}
	public ArrayList<MJDogFightTicketInfo> get_current_tickets_info(){
		return m_current_history_info == null ? null : m_current_history_info.get_tickets_info();
	}
	public MJDogFightOldTicketInfo get_old_ticket_info(int item_id){
		return m_winners_item_id.get(item_id);
	}
	public MJDogFightInstance get_leader(int corner_id){
		if(m_current_manager == null)
			return null;
		
		return m_current_manager.get_leader(corner_id);
	}
	public String get_leader_name(int corner_id){
		MJDogFightInstance dogfight = get_leader(corner_id);
		return dogfight == null ? "" : dogfight.getName();
	}
	public boolean is_fight(){
		return m_current_manager != null && m_current_manager.is_fight();
	}
	public boolean is_run(){
		return m_current_manager != null && m_current_manager.is_run();
	}
	public ArrayList<MJKeyValuePair<Integer, Integer>> find_sellable_tickets(L1PcInstance pc){
		ArrayList<MJKeyValuePair<Integer, Integer>> pairs = new ArrayList<MJKeyValuePair<Integer, Integer>>();
		for(L1ItemInstance item : pc.getInventory().getItems()){
			int item_id = item.getItemId();
			if(!MJDogFightTicketIdManager.getInstance().is_ticket_item_id(item_id))
				continue;
			
			MJDogFightOldTicketInfo otInfo = get_old_ticket_info(item_id);
			MJKeyValuePair<Integer, Integer> pair = otInfo == null ?
					new MJKeyValuePair<Integer, Integer>(item.getId(), 0) :
					new MJKeyValuePair<Integer, Integer>(item.getId(), otInfo.get_price_by_ticket(1));
			pairs.add(pair);
		}
		return pairs;
	}
	public void do_cancel_game(){
		m_current_history_info = null;
		m_current_manager = null;
	}
	public void update_game(){
		if(m_current_history_info == null)
			return;
		
		Updator.exec("insert into dogfight_history set "
				+ "game_number=?, "
				+ "game_id=?, "
				+ "game_name=?, "
				+ "winner_id=?, "
				+ "winner_members=?, "
				+ "winner_ticket_count=?, "
				+ "winner_dividend=?, "
				+ "winner_price=?, "
				+ "winner_item_id=?, "
				+ "total_ticket_count=?, "
				+ "total_price=?, "
				+ "recovery_rate=?, " 
				+ "recovery_price=?, "
				+ "ticket_price=?"
				, new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, m_current_history_info.get_game_number());
						pstm.setInt(++idx, m_current_history_info.get_game_id());
						pstm.setString(++idx, m_current_history_info.get_game_name());
						pstm.setInt(++idx, m_current_history_info.get_winner_id());
						pstm.setString(++idx, m_current_history_info.get_winner_members());
						pstm.setInt(++idx, m_current_history_info.get_winner_ticket_count());
						pstm.setString(++idx, String.format("%.2f", m_current_history_info.get_winner_dividend()));
						pstm.setInt(++idx, m_current_history_info.get_winner_price());
						pstm.setInt(++idx, m_current_history_info.get_winner_item_id());
						pstm.setInt(++idx, m_current_history_info.get_total_ticket_count());
						pstm.setInt(++idx, m_current_history_info.get_pure_total_price());
						pstm.setString(++idx, String.format("%.2f", m_current_history_info.get_recovery_rate()));
						pstm.setInt(++idx, m_current_history_info.get_recovery_price());
						pstm.setInt(++idx, m_current_history_info.get_ticket_price());
					}
				});
		
		MJDogFightOldTicketInfo otInfo = MJDogFightOldTicketInfo.newInstance(m_current_history_info);
		m_winners_item_id.put(otInfo.get_item_id(), otInfo);
		m_current_history_info = null;
	}
	public void on_show_ticket_ration_view(L1PcInstance pc, L1NpcInstance npc){
		if(!is_run() || m_current_history_info == null){
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "maeno3"));
			return;
		}
		if(is_fight()){			
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "maeno3"));
			return;
		}
		pc.sendPackets(S_DogFightBoard.newInstance(pc.isGm(), m_current_history_info.get_tickets_info()));
	}
	public void on_show_ticket_view(L1PcInstance pc, L1NpcInstance npc){
		if(!is_run() || m_current_history_info == null){
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "maeno3"));
			return; 
		}
		if(is_fight()){
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "maeno3"));
			return;
		}
		pc.sendPackets(S_DogFightBuyList.newInstance(pc, npc.getId(), m_current_history_info.get_tickets_info(), m_current_history_info.get_ticket_price()));
	}
	public void on_show_sells_view(L1PcInstance pc, L1NpcInstance npc){
		if(is_fight()){
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "maeno3"));
			return;
		}
		
		ArrayList<MJKeyValuePair<Integer, Integer>> pairs = find_sellable_tickets(pc);
		if(pairs.size() <= 0){
			pc.sendPackets(new S_NoSell(npc));
			return;
		}
		pc.sendPackets(S_DogFightSellList.newInstance(npc.getId(), pairs));
	}
	public void on_sell_ticket(L1PcInstance pc, ClientBasePacket client_packets, int size, int npc_object_id){
		if(is_fight()){
			pc.sendPackets(new S_NPCTalkReturn(npc_object_id, "maeno3"));
			return;
		}
		
		ArrayList<MJDogFightTicketLogInfo> logs_info = new ArrayList<MJDogFightTicketLogInfo>(size);
		int total_price = 0;
		for(int i=0; i<size; ++i){
			int item_object_id = client_packets.readD();
			int item_count = client_packets.readD();
			if(item_object_id <= 0 || item_count <= 0)
				return;
			
			L1ItemInstance item = pc.getInventory().getItem(item_object_id);
			if(item == null || item.getItem() == null)
				continue;

			// 检查物品数量是否足够
			if(item.getCount() < item_count || item.getCount() <= 0){
				pc.sendPackets("物品销售失败。");
				System.out.println(String.format("%s玩家尝试斗狗门票销售数量异常。(物品名称：%s，物品数量：%d，尝试销售数量：%d)",
						pc.getName(), item.getCount(), item_count));
				return;
			}

		// 检查是否为有效的斗狗门票
			if(!MJDogFightTicketIdManager.getInstance().is_ticket_item_id(item.getItemId())){
				pc.sendPackets("物品销售失败。");
				System.out.println(String.format("%s玩家尝试向斗狗门票商人出售非门票物品。(物品名称：%s，物品数量：%d，尝试销售数量：%d)",
						pc.getName(), item.getCount(), item_count));
				return;
			}

		// 获取旧门票信息并移除物品
			MJDogFightOldTicketInfo otInfo = get_old_ticket_info(item.getItemId());
			int removed_count = pc.getInventory().removeItem(item, item_count);
			if(removed_count <= 0){
				pc.sendPackets("物品销售失败。");
				System.out.println(String.format("%s玩家斗狗门票销售失败(物品名称：%s，物品数量：%d，尝试销售数量：%d，实际删除数量：%d)",
						pc.getName(), item.getCount(), item_count, removed_count));
				return;
			}

		// 计算价格
			int price = otInfo == null ?
					0 :
					otInfo.get_price_by_ticket(removed_count);

		// 检查金额是否溢出
			total_price += price;
			if(total_price > 2147483647L){
				pc.sendPackets("物品销售失败。");
				System.out.println(String.format("%s玩家斗狗门票销售金额溢出(物品名称：%s，物品数量：%d，尝试销售数量：%d，实际删除数量：%d，价格：%d)",
						pc.getName(), item.getCount(), item_count, removed_count, total_price));
			}
			
			if(price <= 0)
				continue;
			
			logs_info.add(MJDogFightTicketLogInfo
					.newInstance()
					.set_action_date(MJNSHandler.getLocalTime())
					.set_character_object_id(pc.getId())
					.set_character_name(pc.getName())
					.set_item_id(item.getItemId())
					.set_item_name(item.getName())
					.set_count(removed_count)
					.set_price(otInfo.get_price())
					.set_total_price(price)
					);
		}
		if(total_price > 0)
//			pc.getInventory().storeItem(L1ItemId.ADENA, total_price);
			
			 // 배팅코인
			pc.getInventory().storeItem(L1ItemId.BETTING_COIN, total_price);

		if(logs_info.size() > 0)
			MJDogFightTicketLogger.do_update(logs_info, true);
	}
	public void on_buy_ticket(L1PcInstance pc, ClientBasePacket client_packets, int size){
		if(!check_state(pc))
			return;
		
		ArrayList<MJKeyValuePair<Integer, Integer>> pairs = new ArrayList<MJKeyValuePair<Integer, Integer>>(size);
		int total_price = 0;
		for(int i=0; i<size; ++i){
			MJKeyValuePair<Integer, Integer> pair = new MJKeyValuePair<Integer, Integer>();
			pair.key = client_packets.readD();
			pair.value = client_packets.readD();
			if(pair.value <= 0)
				break;
			
			if(pair.value >= 10000)
				return;

			if(pair.key >= m_current_history_info.get_ticket_width() + 6)
				return;
			
			if(pair.key == 3 || pair.key == 4|| pair.key == 5){
				pair.value *= 10000;
				if(pair.key == 3){
					pair.key = 0;
				}else if(pair.key == 4){
					pair.key = 1;
				}else if(pair.key == 5){
					pair.key = 2;
				}
			}else if(pair.key == 6 || pair.key == 7|| pair.key == 8){
				pair.value *= 100000;
				if(pair.key == 6){
					pair.key = 0;
				}else if(pair.key == 7){
					pair.key = 1;
				}else if(pair.key == 8){
					pair.key = 2;
				}
			}
			
			int price = MJDogFightSettings.TICKET_PRICE * pair.value;
			if(price <= 0){
				pc.sendPackets("购买数量过多.");
				return;
			}
			total_price += price;
			pairs.add(pair);
		}
		if(total_price <= 0){
				pc.sendPackets("购买数量过多.");
			return;			
		}
//		if(!pc.getInventory().checkItem(L1ItemId.ADENA, total_price)){
//			pc.sendPackets("구입에 필요한 아데나를 소비 할 수 없습니다.");
		
		// 배팅코인
		if(!pc.getInventory().checkItem(L1ItemId.BETTING_COIN, total_price)){
			pc.sendPackets("无法消耗购买所需的下注币.");
			return;
		}
		
		if(pairs.size() <= 0)
			return;
		
		ArrayList<MJDogFightTicketLogInfo> logs_info = m_current_history_info.on_buy_ticket(pc, pairs, total_price);
		if(logs_info == null || logs_info.size() <= 0)
			return;
		
		MJDogFightTicketLogger.do_update(logs_info, false);
	}
	private boolean check_state(L1PcInstance pc){
		if(!is_run() || m_current_history_info == null){
			pc.sendPackets("当前无法购买门票.");
			return false; 
		}
		if(is_fight()){
			pc.sendPackets("比赛正在进行中.");  // 이미 경기가 진행중입니다
			return false;
		}
		if(pc.서버다운중){  // 服务器关闭标识
			pc.sendPackets("服务器正在关闭中.");  // 서버 다운이 진행중입니다
			return false;
		}
		return true;
	}
}
