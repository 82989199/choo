package l1j.server.MJBookQuestSystem;

import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;

public class BQSWQUpdator implements TimeListener{
	private static BQSWQUpdator _instance;
	public static BQSWQUpdator getInstance(){
		if(_instance == null)
			_instance = new BQSWQUpdator();
		return _instance;
	}
	
	private ArrayBlockingQueue<Boolean>	_signal;
	private int							_accumulate_clock;
	private boolean						_isrun;
	private int							_current_listener_type;
	private int							_update_times;
	private BQSWQUpdator(){
		_signal = new ArrayBlockingQueue<Boolean>(1);
	}
	
	public void run(){
		_isrun = true;
		GeneralThreadPool.getInstance().execute(new WeekQuestConsumer());
		update_listener();
	}
	
	public void update_listener(){
		if(_current_listener_type > 0)
			RealTimeClock.getInstance().removeListener(this, _current_listener_type);
			
		_current_listener_type = BQSLoadManager.BQS_UPDATE_TYPE;
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		switch(BQSLoadManager.BQS_UPDATE_TYPE){
		case Calendar.MINUTE:
			_accumulate_clock = accumulate_minute(cal);
			_update_times = BQSLoadManager.BQS_UPDATE_HOURS * 60;
			break;
			
		case Calendar.SECOND:
			_accumulate_clock = accumulate_second(cal);
			_update_times = BQSLoadManager.BQS_UPDATE_HOURS * 3600;
			break;
			
		default:
			_accumulate_clock = accumulate_hour(cal);
			_update_times = BQSLoadManager.BQS_UPDATE_HOURS;
			break;
		}
		RealTimeClock.getInstance().addListener(this, _current_listener_type);
	}
	
	public int accumulate_hour(Calendar cal){
		return ((cal.get(Calendar.YEAR) - BQSLoadManager.BQS_UPDATE_CALENDAR.get(Calendar.YEAR)) * RealTimeClock.YEAR_TO_HOURS) + 
				((cal.get(Calendar.DAY_OF_YEAR) - BQSLoadManager.BQS_UPDATE_CALENDAR.get(Calendar.DAY_OF_YEAR)) * RealTimeClock.DAY_TO_HOURS) +
				(cal.get(Calendar.HOUR_OF_DAY) - BQSLoadManager.BQS_UPDATE_CALENDAR.get(Calendar.HOUR_OF_DAY));
	}

	public int accumulate_minute(Calendar cal){
		int accumulate_hour_to_minute = accumulate_hour(cal) * 60;
		return accumulate_hour_to_minute + (cal.get(Calendar.MINUTE) - BQSLoadManager.BQS_UPDATE_CALENDAR.get(Calendar.MINUTE));	
	}
	
	public int accumulate_second(Calendar cal){
		int accumulate_minute_to_second = accumulate_minute(cal) * 60;
		return accumulate_minute_to_second + (cal.get(Calendar.SECOND) - BQSLoadManager.BQS_UPDATE_CALENDAR.get(Calendar.SECOND));
	}
	
	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHourChanged(BaseTime time) {
		if(++_accumulate_clock >= _update_times){
			_accumulate_clock = 0;
			_signal.offer(Boolean.TRUE);
		}
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		if(++_accumulate_clock >= _update_times){
			_accumulate_clock = 0;
			_signal.offer(Boolean.TRUE);
		}
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		if(++_accumulate_clock >= _update_times){
			_accumulate_clock = 0;
			_signal.offer(Boolean.TRUE);
		}
	}
	
	class WeekQuestConsumer implements Runnable{
		@Override
		public void run(){
			while(_isrun){
				try{
					@SuppressWarnings("unused")
					Boolean isHourUpdate = _signal.take();
					if(!_isrun)
						return;
					
					Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
					System.out.println(String.format("[Book System Update...!] %4d-%2d%2d %2d:%2d:%2d", 
							cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
					BQSLoadManager.BQS_IS_ONUPDATE_BOOKS = false;
					try{
						BQSLoadManager.updateBqsUpdateCalendar();
					}catch(Exception e){
						e.printStackTrace();
					}
					BQSLoadManager.BQS_IS_ONUPDATE_BOOKS = true;					
				}catch(Exception e){}
			}
		}
	}
}
