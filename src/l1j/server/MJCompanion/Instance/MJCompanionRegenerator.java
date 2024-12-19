package l1j.server.MJCompanion.Instance;

import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.MJCompanion.MJCompanionSettings;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJCompanionRegenerator implements TimeListener, Runnable {
	private static MJCompanionRegenerator _instance;
	public static MJCompanionRegenerator getInstance(){
		if(_instance == null)
			_instance = new MJCompanionRegenerator();
		return _instance;
	}

	private ArrayBlockingQueue<Boolean>	m_signal;
	private boolean m_is_run;
	private int m_accumulcate_seconds;
	private MJCompanionRegenerator(){
		m_signal = new ArrayBlockingQueue<Boolean>(1);
		m_is_run = false;
		m_accumulcate_seconds = 0;
	}
	
	public void do_run(){
		if(m_is_run)
			return;
		
		m_is_run = true;
		GeneralThreadPool.getInstance().execute(this);
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
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
		if((++m_accumulcate_seconds) == MJCompanionSettings.REGENERATION_TICK_SECONDS){
			m_accumulcate_seconds = 0;
			m_signal.offer(Boolean.TRUE);
		}
		Calendar cal = time.getCalendar();
		if(cal.get(Calendar.HOUR_OF_DAY) == MJCompanionSettings.TRANING_INITIALIZE_HOUR && 
				cal.get(Calendar.MINUTE) == 0 &&
				cal.get(Calendar.SECOND) == 0){
			do_traning_initialized();
		}
	}

	public void do_traning_initialized(){
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run() {
				try{
					S_SystemMessage message = new S_SystemMessage("펫 훈련시간이 초기화 되었습니다.");
					for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
						if(pc == null)
							continue;
						
						MJCompanionInstance companion = pc.get_companion();
						if(companion != null)
							companion.set_is_traning(false);
						pc.sendPackets(message, false);
					}
					Updator.exec("update companion_instance set is_traning=?", new Handler(){
						@Override
						public void handle(PreparedStatement pstm) throws Exception {
							pstm.setInt(1, 0);
						}
					});
					message.clear();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void run() {
		try{
			while(m_is_run){
				@SuppressWarnings("unused")
				Boolean is_update = m_signal.take();
				if(!m_is_run)
					break;
				
				for(MJCompanionInstance companion : L1World.getInstance().getAllCompanions()){
					
					if(companion == null || companion.isDead() || companion.get_master() == null)
						continue;
					
					if(companion.getCurrentHp() >= companion.getMaxHp())
						continue;
					
					companion.do_update_regen_tick();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			m_is_run = false;
		}
	}
}
