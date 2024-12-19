package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.templates.L1EventAlarm;
import l1j.server.server.utils.SQLUtil;

public class EventAlarmTable {
	
	public Timestamp colo_time;
	
	private final ArrayList<L1EventAlarm> _EventAlarm = new ArrayList<L1EventAlarm>();
	
	public static S_ACTION_UI delS_ActionBox = new S_ACTION_UI(S_ACTION_UI.EVENT_NOTICE,0, false);
	public static S_ACTION_UI newS_ActionBox = new S_ACTION_UI(S_ACTION_UI.EVENT_NOTICE,0, true);
	
	private static Logger _log = Logger.getLogger(ClanTable.class.getName());
	private static EventAlarmTable _instance;
	public static EventAlarmTable getInstance() {
		if (_instance == null) {
			_instance = new EventAlarmTable();
		}
		return _instance;
	}
	
	public ArrayList<L1EventAlarm> GetEventAlarmList(){
		return _EventAlarm;
	}
	
	public static void reload() {
		EventAlarmTable oldInstance = _instance;
		_instance = new EventAlarmTable();
		oldInstance._EventAlarm.clear();
		System.out.println("[Server Ready] EventAlarmTable  OK!");
	}
	
	private EventAlarmTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM event_alarm");
			rs = pstm.executeQuery();
			L1EventAlarm EventAlarm = null;
			
			while (rs.next()) {
				EventAlarm = new L1EventAlarm();
				EventAlarm.setId(rs.getInt("id"));
				EventAlarm.setName(rs.getString("name"));
				String StartTime[] = rs.getString("start_time").trim().split(",");
				
				for(int i=0;i< StartTime.length;i++){
					int StartTime_2[] = new int[2];
					String StartTime_1[] = StartTime[i].trim().split(":");
					StartTime_2[0] = Integer.parseInt(StartTime_1[0].trim());
					StartTime_2[1] = Integer.parseInt(StartTime_1[1].trim());
					EventAlarm.addStart_timeList(StartTime_2);
				}
				EventAlarm.setPeriod(rs.getInt("period"));
				EventAlarm.setDate(rs.getTimestamp("date"));
				
				String day_of_week[] = rs.getString("day_of_week").trim().split(",");
				for(int i=0;i< day_of_week.length;i++){
					if(day_of_week[i].trim().equals("일")){
						EventAlarm.addDay_Of_Week(1);
					}else if(day_of_week[i].trim().equals("월")){
						EventAlarm.addDay_Of_Week(2);
					}else if(day_of_week[i].trim().equals("화")){
						EventAlarm.addDay_Of_Week(3);
					}else if(day_of_week[i].trim().equals("수")){
						EventAlarm.addDay_Of_Week(4);
					}else if(day_of_week[i].trim().equals("목")){
						EventAlarm.addDay_Of_Week(5);
					}else if(day_of_week[i].trim().equals("금")){
						EventAlarm.addDay_Of_Week(6);
					}else if(day_of_week[i].trim().equals("토")){
						EventAlarm.addDay_Of_Week(7);
					}
				}
				EventAlarm.setAction(rs.getString("action").equals("true") ? true : false);
				EventAlarm.setLoc_X(rs.getInt("loc_x"));
				EventAlarm.setLoc_Y(rs.getInt("loc_y"));
				EventAlarm.setMapId(rs.getInt("map_id"));
				EventAlarm.setPrice(rs.getInt("teleport_price"));
				_EventAlarm.add(EventAlarm);
			}

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}		
	}
	
	
	
	
	
	
	
	
	
	
	
}
