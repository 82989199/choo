package l1j.server.DogFight.History;

import java.util.ArrayList;

import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.DogFight.Game.MJDogFightGame;
import l1j.server.DogFight.Game.MJDogFightGameInfo;
import l1j.server.DogFight.History.Log.MJDogFightTicketLogInfo;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJKeyValuePair;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.templates.L1Item;

public class MJDogFightHistoryInfo {
	public static MJDogFightHistoryInfo newInstance(int game_number, MJDogFightGameInfo gInfo){
		return newInstance()
				.set_game_number(game_number)
				.set_game_id(gInfo.get_game_id())
				.set_game_name(gInfo.get_game_name())
				.set_winner_id(0)
				.set_winner_members("")
				.set_winner_ticket_count(0)
				.set_winner_dividend(0D)
				.set_winner_price(0)
				.set_winner_item_id(0)
				.set_total_ticket_count(0)
				.set_total_price(0)
				.set_pure_total_price(0)
				.set_recovery_rate(0D)
				.set_recovery_price(0)
				.set_ticket_price(MJDogFightSettings.TICKET_PRICE)
				.create_tickets(gInfo);
	}
	
	private static MJDogFightHistoryInfo newInstance(){
		return new MJDogFightHistoryInfo();
	}

	private int m_game_number;
	private int m_game_id;
	private String m_game_name;
	private int m_winner_id;
	private String m_winner_members;
	private int m_winner_ticket_count;
	private double m_winner_dividend;
	private int m_winner_price;
	private int m_winner_item_id;
	private int m_total_ticket_count;
	private int m_total_price;
	private int m_pure_total_price;
	private double m_recovery_rate;
	private int m_recovery_price;
	private int m_ticket_width;
	private int m_ticket_price;
	private ArrayList<MJDogFightTicketInfo> m_tickets_info;

	private MJDogFightHistoryInfo(){}

	public MJDogFightHistoryInfo set_game_number(int game_number){
		m_game_number = game_number;
		return this;
	}
	public MJDogFightHistoryInfo set_game_id(int game_id){
		m_game_id = game_id;
		return this;
	}
	public MJDogFightHistoryInfo set_game_name(String game_name){
		m_game_name = game_name;
		return this;
	}
	public MJDogFightHistoryInfo set_winner_id(int winner_id){
		m_winner_id = winner_id;
		return this;
	}
	public MJDogFightHistoryInfo set_winner_members(String winner_members){
		m_winner_members = winner_members;
		return this;
	}
	public MJDogFightHistoryInfo set_winner_ticket_count(int winner_ticket_count){
		m_winner_ticket_count = winner_ticket_count;
		return this;
	}
	public MJDogFightHistoryInfo set_winner_dividend(double winner_dividend){
		m_winner_dividend = winner_dividend;
		return this;
	}
	public MJDogFightHistoryInfo set_winner_price(int winner_price){
		m_winner_price = winner_price;
		return this;
	}
	public MJDogFightHistoryInfo set_winner_item_id(int winner_item_id){
		m_winner_item_id = winner_item_id;
		return this;
	}
	public MJDogFightHistoryInfo set_total_ticket_count(int total_ticket_count){
		m_total_ticket_count = total_ticket_count;
		return this;
	}
	public MJDogFightHistoryInfo set_total_price(int total_price){
		m_total_price = total_price;
		return this;
	}
	public MJDogFightHistoryInfo set_pure_total_price(int pure_total_price){
		m_pure_total_price = pure_total_price;
		return this;
	}
	public MJDogFightHistoryInfo set_recovery_rate(double recovery_rate){
		m_recovery_rate = recovery_rate;
		return this;
	}
	public MJDogFightHistoryInfo set_recovery_price(int recovery_price){
		m_recovery_price = recovery_price;
		return this;
	}
	public MJDogFightHistoryInfo set_ticket_price(int ticket_price){
		m_ticket_price = ticket_price;
		return this;
	}
	public int get_game_number(){
		return m_game_number;
	}
	public int get_game_id(){
		return m_game_id;
	}
	public String get_game_name(){
		return m_game_name;
	}
	public int get_winner_id(){
		return m_winner_id;
	}
	public String get_winner_members(){
		return m_winner_members;
	}
	public int get_winner_ticket_count(){
		return m_winner_ticket_count;
	}
	public double get_winner_dividend(){
		return m_winner_dividend;
	}
	public int get_winner_price(){
		return m_winner_price;
	}
	public int get_winner_item_id(){
		return m_winner_item_id;
	}
	public int get_total_ticket_count(){
		return m_total_ticket_count;
	}
	public int get_total_price(){
		return m_total_price;
	}
	public int get_pure_total_price(){
		return m_pure_total_price;
	}
	public double get_recovery_rate(){
		return m_recovery_rate;
	}
	public int get_recovery_price(){
		return m_recovery_price;
	}
	public int get_ticket_width(){
		return m_ticket_width;
	}
	public int get_ticket_price(){
		return m_ticket_price;
	}
	public ArrayList<MJDogFightTicketInfo> get_tickets_info(){
		return m_tickets_info;
	}

	// 斗狗状态条件数组
//	public String[] _DogFCondition = { "非常好", "良好", "差", "非常差", "一般" };
	public String[] _DogFCondition = { "■■■", "■■□", "□□□", "□□□", "■□□" };
	
	public MJDogFightHistoryInfo create_tickets(MJDogFightGameInfo gInfo){
		java.util.Set<Integer> keys = gInfo.get_fighters_keys();
		m_ticket_width = keys.size() + 1; // 무승부때문에 +1
		m_tickets_info = new ArrayList<MJDogFightTicketInfo>(m_ticket_width);
		int[] tick_ids = MJDogFightTicketIdManager.getInstance().next_ids(m_ticket_width);
		
		L1Item template = ItemTable.getInstance().getTemplate(MJDogFightSettings.TICKET_ITEM_ID);
		int idx = 0;
		double win_rate = create_win_rate();
		for(Integer corner_id : keys){
			String corner_name = gInfo.get_corner_name(corner_id);
			L1Item ticket_template = ItemTable.getInstance().create_dogfight_ticket(template, tick_ids[idx], String.format(MJDogFightSettings.TICKET_NAME, 
					MJDogFightHistory.getInstance().get_current_game_number(), corner_id + 1, corner_name));
			int count = MJDogFightSettings.DEFAULT_TICKET_COUNT;
			int price = count * get_ticket_price();
			m_tickets_info.add(
					MJDogFightTicketInfo
					.newInstance()
					.set_corner_id(corner_id)
					.set_corner_name(corner_name)
					.set_template(ticket_template)
					.set_win_rate(create_win_rate())
					.set_Condition(_DogFCondition[MJRnd.next(5)]) //컨디션 추가
					//.set_win_rate(win_rate)
					.update_ticket_count(count, price)
					);

			m_total_ticket_count += count;
			m_total_price += price;
			m_pure_total_price += price;
			++idx;
		}
		
		// 무승부 없애려면 주석처리 [시작]
		int corner_id = 2;
		String corner_name = "무";
		L1Item ticket_template = ItemTable.getInstance().create_dogfight_ticket(template, tick_ids[idx], String.format(MJDogFightSettings.TICKET_NAME, 
				MJDogFightHistory.getInstance().get_current_game_number(), corner_id + 1, corner_name));
		int count = MJDogFightSettings.DEFAULT_TICKET_COUNT;
		int price = count * get_ticket_price();
		m_tickets_info.add(
				MJDogFightTicketInfo
				.newInstance()
				.set_corner_id(corner_id)
				.set_corner_name(corner_name)
				.set_template(ticket_template)
				.set_win_rate(create_win_rate())
				.set_Condition(_DogFCondition[MJRnd.next(5)]) //컨디션 추가
				//.set_win_rate(win_rate)
				.update_ticket_count(count, price)
				);

		m_total_ticket_count += count;
		m_total_price += price;
		m_pure_total_price += price;
		// 무승부 없애려면 주석처리 [끝]
		
		
		update_ticket_dividend();
		
		return this;
	}
	
	public MJDogFightHistoryInfo do_calculate(int corner_id){
		for(MJDogFightTicketInfo tInfo : m_tickets_info){
			if(tInfo.get_corner_id() != corner_id)
				continue;
			
			set_winner_item_id(tInfo.get_template().getItemId());
			set_winner_price((int)Math.floor((double)tInfo.get_price() * (double)tInfo.get_dividend()));
			set_winner_ticket_count(tInfo.get_ticket_count());
			set_winner_dividend(tInfo.get_dividend());
			set_recovery_price(get_pure_total_price() - get_winner_price());
			set_recovery_rate(100D - (((double)get_winner_price() / (double)get_pure_total_price()) * 100));
			break;
		}
		return this;
	}
	
	public synchronized ArrayList<MJDogFightTicketLogInfo> on_buy_ticket(L1PcInstance pc, ArrayList<MJKeyValuePair<Integer, Integer>> pairs, int total_price){
		ArrayList<MJDogFightTicketLogInfo> logs_info = new ArrayList<MJDogFightTicketLogInfo>(pairs.size());
		try{
			ArrayList<L1ItemInstance> tickets = new ArrayList<L1ItemInstance>(pairs.size());
			for(MJKeyValuePair<Integer, Integer> pair : pairs){
				MJDogFightTicketInfo tInfo = m_tickets_info.get(pair.key);
				L1ItemInstance ticket = ItemTable.getInstance().createItem(tInfo.get_template());
				ticket.setIdentified(true);
				ticket.setCount(pair.value);
				L1World.getInstance().storeObject(ticket);

				m_total_ticket_count += pair.value;
				int price = get_ticket_price() * pair.value;
				MJDogFightInstance fight = MJDogFightGame.getDogFight(pair.key);
				//System.out.println(pair.key);
				if(fight != null) {
					fight.addTotalPrice(price);
					//System.out.println(fight.getName() + " / "+ fight.getTotalPrice());
				}
				
				tInfo.update_ticket_count(pair.value, price);
				tickets.add(ticket);
				logs_info.add(MJDogFightTicketLogInfo
						.newInstance()
						.set_action_date(MJNSHandler.getLocalTime())
						.set_character_object_id(pc.getId())
						.set_character_name(pc.getName())
						.set_item_id(ticket.getItemId())
						.set_item_name(ticket.getName())
						.set_count(pair.value)
						.set_price(get_ticket_price())
						.set_total_price(price)
						);
			}
			m_total_price += (total_price - (total_price * MJDogFightSettings.EXCHANGE_RATE));
			m_pure_total_price += total_price;
			
//			if(!pc.getInventory().consumeItem(L1ItemId.ADENA, total_price)){
//				pc.sendPackets("구입에 필요한 아데나를 소비 할 수 없습니다.");
			
			// 배팅코인
			if(!pc.getInventory().consumeItem(L1ItemId.BETTING_COIN, total_price)){
				pc.sendPackets("구입에 필요한 배팅 코인을 소비 할 수 없습니다.");
				return null;
			}
			
			
			/**마일리지 충전*/
			int MileagePoint = 0;
			for(L1ItemInstance ticket : tickets){
				pc.getInventory().storeItem(ticket);
				MileagePoint += ticket.getCount();
			}
			
			if(MJDogFightSettings.MILEAGE_PERCENT > 0 &&
					MileagePoint > MJDogFightSettings.MILEAGE_PERCENT){
				int count = (int)(MileagePoint * MJDogFightSettings.MILEAGE_PERCENT) / 100;
				if(count > 0){
				L1ItemInstance ticket = ItemTable.getInstance().createItem(MJDogFightSettings.MILEAGE_ITEM);
				ticket.setCount(count);
				pc.getInventory().storeItem(ticket);
				pc.sendPackets("티켓 구매로 ["+ticket.getName()+"] "+count+"개가 지급되었습니다.");
				}
			}
			
			
			
			update_ticket_dividend();
		}catch(Exception e){
			e.printStackTrace();
			pc.sendPackets("购买失败.");
		}
		return logs_info;
	}
	
	private void update_ticket_dividend(){
		for(MJDogFightTicketInfo tInfo : m_tickets_info)
			tInfo.update_ticket_dividend(get_total_price());
	}
	
	private double create_win_rate(){
		return (double)MJRnd.next(101, 210) * 0.1;
	}
}

