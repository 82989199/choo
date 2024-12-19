package l1j.server.MJDungeonTimer.Updator;

import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeLoadManager;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_SystemMessage;

public class DungeonTimeUpdator implements TimeListener{
	private static DungeonTimeUpdator _instance;
	public static DungeonTimeUpdator getInstance(){
		if(_instance == null)
			_instance = new DungeonTimeUpdator();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload(){
		DungeonTimeUpdator old = _instance;
		_instance = new DungeonTimeUpdator();
		if(old != null){
			old.dispose();
			old = null;
		}
	}
	
	public static boolean is_possible_update_character(L1PcInstance pc){
		return pc != null && pc.getNetConnection() != null && pc.getAI() == null && !pc._destroyed;
	}

	private DungeonTimeConsumer _consumer;
	private DungeonTimeUpdator(){
	}
	
	public void run(){
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
		_consumer = new DungeonTimeConsumer();
		GeneralThreadPool.getInstance().execute(_consumer);
	}

	public void dispose(){
		RealTimeClock.getInstance().removeListener(this, Calendar.SECOND);
		if(_consumer != null){
			_consumer.offer(Boolean.FALSE);
			_consumer = null;
		}
	}

	public boolean is_running(){
		return _consumer != null;
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
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	public void do_initialize(Calendar cal){
		dispose();
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run(){
				S_SystemMessage message = new S_SystemMessage("전체 던전 시간이 초기화되었습니다.");
				try{
					L1World.getInstance()
					.getAllPlayerStream()
					.filter((L1PcInstance pc)->{
						return is_possible_update_character(pc);
					})
					.forEach((L1PcInstance pc)->{
						pc.initialize_dungeon_progress();
						pc.sendPackets(message, false);
					});
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					DungeonTimeLoadManager.getInstance().do_truncate(cal);
					message.clear();
					DungeonTimeUpdator.this.run();
				}
			}
		});
	}
	
	@Override
	public void onSecondChanged(BaseTime time) {
		/** 던전초기화 시간 **/
		if(time.equals_time(9, 0, 0)){
			do_initialize(time.getCalendar());
		}else{
			if(_consumer != null){
				_consumer.offer(Boolean.TRUE);
			}
		}
	}
	
	static class DungeonTimeConsumer implements Runnable{
		private ArrayBlockingQueue<Boolean> _signal;
		DungeonTimeConsumer(){
			_signal = new ArrayBlockingQueue<Boolean>(2);
		}
		
		public void offer(Boolean b){
			_signal.offer(b);
		}
		
		@Override
		public void run() {
			try{
				boolean is_run = Boolean.TRUE;
				while(is_run){
					is_run = _signal.take();
					if(!is_run)
						return;
					
					do_update();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		private void do_update(){
			try{
				DungeonTimeInformationLoader loader = DungeonTimeInformationLoader.getInstance();
				for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
					if(!is_possible_update_character(pc))
						continue;
					
					DungeonTimeInformation dtInfo = loader.from_map_id(pc.getMapId());
					if(dtInfo == null)
						continue;
					
					pc.dec_dungeon_progress(dtInfo);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
