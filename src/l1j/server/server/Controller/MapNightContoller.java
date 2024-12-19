package l1j.server.server.Controller;

import java.util.Calendar;

import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.L1GameTimeClock;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.model.map.L1WorldMap;

public class MapNightContoller implements TimeListener {

	private static MapNightContoller _instance;
	public static MapNightContoller getInstance(){
		if(_instance == null)
			_instance = new MapNightContoller();
		return _instance;
	}
	
	public void run(){
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
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
		
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		boolean isNight = L1GameTimeClock.getInstance().getGameTime().isNight();		
		if(isNight){			
			L1WorldMap.getInstance().getMap((short)54).set_isTeleportable(false);
		}else {
			L1WorldMap.getInstance().getMap((short)54).set_isTeleportable(true);
		}
	}

}
