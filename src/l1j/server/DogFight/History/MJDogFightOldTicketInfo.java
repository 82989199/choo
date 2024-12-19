package l1j.server.DogFight.History;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MJDogFightOldTicketInfo {
	public static MJDogFightOldTicketInfo newInstance(MJDogFightHistoryInfo hInfo){
		return newInstance()
				.set_item_id(hInfo.get_winner_item_id())
				.set_dividend(hInfo.get_winner_dividend())
				.set_price(hInfo.get_ticket_price());
	}
	
	public static MJDogFightOldTicketInfo newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_item_id(rs.getInt("winner_item_id"))
				.set_dividend(rs.getDouble("winner_dividend"))
				.set_price(rs.getInt("ticket_price"));
	}

	private static MJDogFightOldTicketInfo newInstance(){
		return new MJDogFightOldTicketInfo();
	}

	private int m_item_id;
	private double m_dividend;
	private int m_price;
	private MJDogFightOldTicketInfo(){}

	public MJDogFightOldTicketInfo set_item_id(int item_id){
		m_item_id = item_id;
		return this;
	}
	public MJDogFightOldTicketInfo set_dividend(double dividend){
		m_dividend = dividend;
		return this;
	}
	public MJDogFightOldTicketInfo set_price(int price){
		m_price = price;
		return this;
	}
	public int get_item_id(){
		return m_item_id;
	}
	public double get_dividend(){
		return m_dividend;
	}
	public int get_price(){
		return m_price;
	}
	
	public int get_price_by_ticket(int count){
		return (int)Math.floor((double)get_price() * get_dividend()) * count;
	}
}


