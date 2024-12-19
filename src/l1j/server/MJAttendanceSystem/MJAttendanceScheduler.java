package l1j.server.MJAttendanceSystem;

import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;

public class MJAttendanceScheduler implements TimeListener{
	private static final int UPDATE_SECOND = 1;
	public static final int ATTEN_SHD_RESULT_ERROR = -1;
	public static final int ATTEN_SHD_RESULT_NONUPDATE = 0;
	public static final int ATTEN_SHD_RESULT_ONUPDATE = 1;
	
	private static MJAttendanceScheduler _instance;
	public static MJAttendanceScheduler getInstance(){
		if(_instance == null)
			_instance = new MJAttendanceScheduler();
		return _instance;
	}
	
	private ArrayBlockingQueue<Boolean>	_signal;
	private int							_accumulate_second;
	private boolean						_isrun;
	private MJAttendanceScheduler(){
		_signal = new ArrayBlockingQueue<Boolean>(1);
	}

	public void run(){
		_isrun = true;
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		_accumulate_second += (cal.get(Calendar.HOUR_OF_DAY) * 3600);
		_accumulate_second += (cal.get(Calendar.MINUTE) * 60);
		_accumulate_second += (cal.get(Calendar.SECOND));
		_accumulate_second %= MJAttendanceLoadManager.ATTEN_RESET_PERIOD_SECOND;
		GeneralThreadPool.getInstance().execute(new MJAttendanceConsumer());
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
	}
	
	public void dispose(){
		_isrun = false;
		RealTimeClock.getInstance().removeListener(this,  Calendar.SECOND);
		_signal.offer(Boolean.TRUE);
	}
	
	@Override
	public void onMonthChanged(BaseTime time) {
	}

	@Override
	public void onDayChanged(BaseTime time) {
	}

	@Override
	public void onHourChanged(BaseTime time) {
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		_accumulate_second += UPDATE_SECOND;
		if(_accumulate_second >= MJAttendanceLoadManager.ATTEN_RESET_PERIOD_SECOND){
			_accumulate_second = 0;
			_signal.offer(Boolean.TRUE);
		}else{
			_signal.offer(Boolean.FALSE);
		}
	}
	
	class MJAttendanceConsumer implements Runnable{
		@Override
		public void run() {
			try{
				while(_isrun){
					Boolean isDayUpdate = _signal.take();
					if(!_isrun)
						return;
					
					if(isDayUpdate){
						MJAttendanceLoadManager.updateServerStartupInfo();
						doUpdateDay();
					}else
						doUpdateSecond();
				}
			}catch(Exception e){}
		}
	}
	
	private void doUpdateDay(){
		L1World.getInstance().getAllPlayerStream()
		.filter((L1PcInstance pc) -> pc != null && pc.getAttendanceData() != null)
		.forEach((L1PcInstance pc) ->{
			try{
				SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
				userData.onUpdateDay(pc, UPDATE_SECOND);
			}catch(Exception e){
				e.printStackTrace();
			}
		});
	}
	
	private void doUpdateSecond(){
		L1World.getInstance().getAllPlayerStream()
		.filter((L1PcInstance pc) -> pc != null && pc.getAttendanceData() != null)
		.forEach((L1PcInstance pc) ->{
			try{
				SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
				userData.onUpdateTime(pc, UPDATE_SECOND);
			}catch(Exception e){
				e.printStackTrace();
			}
		});
	}
}
