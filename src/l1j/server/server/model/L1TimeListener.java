package l1j.server.server.model;

import java.util.Calendar;

import l1j.server.server.datatables.EventAlarmTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_ACTION_UI;


public class L1TimeListener implements TimeListener{
	private static L1TimeListener _instance;
	public static void start() {
		if (_instance == null) {
			_instance = new L1TimeListener();
		}
		RealTimeClock.getInstance().addListener(_instance, Calendar.MINUTE);
		RealTimeClock.getInstance().addListener(_instance, Calendar.DAY_OF_MONTH);
		
	}
	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	@Override
	public void onHourChanged(BaseTime time) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO 자동 생성된 메소드 스텁
		
		/**이벤트 알람 업데이트*/
		EventAlarmTable.newS_ActionBox = new S_ACTION_UI(S_ACTION_UI.EVENT_NOTICE,0 , true);
		for(L1PcInstance player : L1World.getInstance().getAllPlayers()){
			player.sendPackets(EventAlarmTable.delS_ActionBox);// 이벤트 알림
			player.sendPackets(EventAlarmTable.newS_ActionBox);// 이벤트 알림
		}
		
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		// TODO 자동 생성된 메소드 스텁
		
	}

}
