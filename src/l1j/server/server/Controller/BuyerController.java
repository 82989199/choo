package l1j.server.server.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Buyer;
import l1j.server.server.utils.SQLUtil;

public final class BuyerController {
	
	private static List<Buyer> buyerlist;
	
	private static Calendar calendar;
	
	private static boolean isok;
	
	public static int SLEEP_TIME;
	
	static public void init(){
		buyerlist = new ArrayList<Buyer>();
		calendar = Calendar.getInstance();
		isok = true;
		SLEEP_TIME = 1000;
		loadBuyer();
	}
	
	
	public static boolean isadd(String buyername){
		synchronized (buyerlist) {
			for(Buyer buyer : buyerlist){
				if(buyer.getBuyername().equalsIgnoreCase(buyername)){
					return false;		
				}
			}
		}
		return true;
	}
	
	public static void delbuyer(String buyername){
		Buyer delbuyer = null;
		synchronized (buyerlist) {
			for(Buyer buyer : buyerlist){
				if(buyer.getBuyername().equalsIgnoreCase(buyername)){
					delbuyer = 	buyer;
				}
			}
		}
		synchronized (buyerlist) {
			if(buyerlist.contains(delbuyer))
				buyerlist.remove(delbuyer);			
		}
		delBuyer(buyername);
	}
	
	public static void addbuyer(String buyername , int Level, String memo){
		Buyer buyer = new Buyer(buyername, Level, memo);
		synchronized (buyerlist) {
			if(!buyerlist.contains(buyer))
				buyerlist.add(buyer);
		}
		saveDBbuyer(buyer);
	}
	
	@SuppressWarnings("deprecation")
	static public void toTimer(long time){
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		int hour = date.getHours();
		int min = date.getMinutes();
		if(hour == 9 && min == 30){//vip 지급시간 00:00분
			if(!isok){
				synchronized (buyerlist) {
					for(Buyer buyer : buyerlist)
						buyer.buyerGive();
				}				
				isok = true;
			}
		}else{
			if(isok == true)
				isok = false;
		}
	}	
	
	static public void saveDBbuyer(Buyer buyer){
		PreparedStatement pstm = null;
		Connection con = null;		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO buyeritem SET buyer=?, memo=?, LeveL=?");
			pstm.setString(1, buyer.getBuyername());
			pstm.setString(2, buyer.getMemo());
			pstm.setInt(3, buyer.getLevel());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	static public void loadBuyer(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM buyeritem");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String name =  rs.getString("buyer");
				String memo =  rs.getString("memo");
				int Level =  rs.getInt("Level");
				Buyer buyer = new Buyer(name, Level, memo);
				buyerlist.add(buyer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
	}
	
	static public void delBuyer(String name){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM buyeritem where buyer=?");
			pstm.setString(1, name);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
	}
}
