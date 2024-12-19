package l1j.server.server.templates;

import java.sql.Timestamp;
import java.util.ArrayList;

public class L1EventAlarm {
	
	private final ArrayList<int[]> _Start_time = new ArrayList<int[]>();
	private final ArrayList<Integer> _Day_Of_Week = new ArrayList<Integer>();
	public L1EventAlarm() {}
	/**
	 * 구분 아이디
	 * */
	private int _id;
	public int getId() {
		return _id;
	}
	public void setId(int i) {
		_id = i;
	}
	
	/**
	 * 이벤트 이름
	 * */
	private String _name;
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
	/**
	 * 이벤트 시작 타임
	 * */
	public ArrayList<int[]> getStart_timeList() {
		return _Start_time;
	}
	public void addStart_timeList(int[] Start_time) {
		_Start_time.add(Start_time);
	}
	
	/**
	 * 이벤트 진행시간
	 * */
	private int _Period;
	public int getPeriod() {
		return _Period;
	}
	public void setPeriod(int Period) {
		_Period = Period;
	}
	
	/**
	 * 이벤트 시작 날짜
	 * */
	private Timestamp _Date;
	public Timestamp getDate() {
		return _Date;
	}
	public void setDate(Timestamp Date) {
		_Date = Date;
	}
	
	/**
	 * 이벤트 진행 요일
	 * */
	public ArrayList<Integer> getDay_Of_Week() {
		return _Day_Of_Week;
	}
	public void addDay_Of_Week(int Start_time) {
		_Day_Of_Week.add(Start_time);
	}
	
	/**
	 * 이벤트 알람 실행,종료
	 * */
	private boolean _Action;
	public boolean getAction() {
		return _Action;
	}
	public void setAction(boolean Action) {
		_Action = Action;
	}
	
	/**
	 * 이벤트 지역 X 좌표
	 * */
	private int _Loc_x;
	public int getLoc_X() {
		return _Loc_x;
	}
	public void setLoc_X(int Loc_x) {
		_Loc_x = Loc_x;
	}
	
	/**
	 * 이벤트 지역 Y 좌표
	 * */
	private int _Loc_y;
	public int getLoc_Y() {
		return _Loc_y;
	}
	public void setLoc_Y(int Loc_y) {
		_Loc_y = Loc_y;
	}
	
	/**
	 * 이벤트 지역 맵 아이디
	 * */
	private int _Mapid;
	public int getMapId() {
		return _Mapid;
	}
	public void setMapId(int Mapid) {
		_Mapid = Mapid;
	}
	
	/**
	 * 이벤트 지역 텔 비용
	 * */
	private int _price;
	public int getPrice() {
		return _price;
	}
	public void setPrice(int price) {
		_price = price;
	}
	
	
	
	
	
	
	
	
	
	
}
