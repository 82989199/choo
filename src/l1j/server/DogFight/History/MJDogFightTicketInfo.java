package l1j.server.DogFight.History;

import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.templates.L1Item;

public class MJDogFightTicketInfo {
	public static MJDogFightTicketInfo newInstance(){
		return new MJDogFightTicketInfo();
	}

	private int m_corner_id;
	private String m_corner_name;
	private int m_ticket_count;
	private double m_dividend;
	private int m_price;
	private L1Item m_template;
	private double m_win_rate;
	private String Condition;
	private MJDogFightTicketInfo(){}
	
	
	public String get_Condition(){
		return Condition;
	}
	public MJDogFightTicketInfo set_Condition(String _Condition){
		Condition = _Condition;
		return this;
	}

	public MJDogFightTicketInfo set_corner_id(int corner_id){
		m_corner_id = corner_id;
		return this;
	}
	public MJDogFightTicketInfo set_corner_name(String corner_name){
		m_corner_name = corner_name;
		return this;
	}
	public MJDogFightTicketInfo set_ticket_count(int ticket_count){
		m_ticket_count = ticket_count;
		return this;
	}
	public MJDogFightTicketInfo set_dividend(double dividend){
		if(m_corner_id == 2){
			// 투견 배율 무승부
			m_dividend = 8.00d;
		}else {
			// 투견 배율 일반
			m_dividend = 1.92d;
		}
		
		//m_dividend = dividend;
		
		//m_dividend = MJDogFightSettings.LIMIT_DIVIDEND;
		//if(m_dividend >= MJDogFightSettings.LIMIT_DIVIDEND)
		//	m_dividend = (double)MJRnd.next((int)(MJDogFightSettings.LIMIT_DIVIDEND - 1) * 100, (int)(MJDogFightSettings.LIMIT_DIVIDEND) * 100) * 0.01;
		return this;
	}
	public MJDogFightTicketInfo set_price(int price){
		m_price = price;
		return this;
	}
	public MJDogFightTicketInfo set_template(L1Item template){
		m_template = template;
		return this;
	}
	public MJDogFightTicketInfo set_win_rate(double win_rate){
		m_win_rate = win_rate;
		return this;
	}
	public int get_corner_id(){
		return m_corner_id;
	}
	public String get_corner_name(){
		return m_corner_name;
	}
	public int get_ticket_count(){
		return m_ticket_count;
	}
	public double get_dividend(){
		return m_dividend;
	}
	public int get_price(){
		return m_price;
	}
	public L1Item get_template(){
		return m_template;
	}
	public double get_win_rate(){
		return m_win_rate;
	}
	public MJDogFightTicketInfo update_ticket_count(int amount, int price){
		m_ticket_count += amount;
		m_price += price;
		return this;
	}
	
	public void update_ticket_dividend(int total_price){
		
		set_dividend((double)total_price / (double)get_price());
//		set_dividend((double)total_amount / (double)get_ticket_count());
	}
}


