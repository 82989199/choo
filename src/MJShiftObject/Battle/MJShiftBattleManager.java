package MJShiftObject.Battle;

import java.util.ArrayList;

import MJShiftObject.Template.CommonServerBattleInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftBattleManager implements Runnable{
	private CommonServerBattleInfo m_battle_info;
	private boolean m_is_cancel;
	private MJIShiftBattlePlayManager m_play_manager;
	private int m_loop_count;
	private ArrayList<MJIShiftBattleNotify> m_notifies;
	public MJShiftBattleManager(CommonServerBattleInfo battle_info, MJIShiftBattlePlayManager play_manager){
		m_battle_info = battle_info;
		m_is_cancel = false;
		m_play_manager = play_manager;
		if(m_play_manager != null)
			m_loop_count = m_play_manager.next_update_tick();
		m_notifies = new ArrayList<MJIShiftBattleNotify>();
	}
	public void set_cancel_state(boolean is_cancel){
		m_is_cancel = is_cancel;
	}
	
	public String get_battle_server_identity(){
		return m_battle_info.get_server_identity();
	}
	public boolean is_battle_server_running(){
		return m_battle_info.is_run();
	}
	public void add_notify(MJIShiftBattleNotify notify){
		m_notifies.add(notify);
	}
	public void execute(){
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		try{
			while(!m_battle_info.is_ended()){
				if(m_is_cancel)
					return;
				
				do_tick_play_manager();
				do_update_play_manager();
				Thread.sleep(1000);
			}
			if(m_is_cancel)
				return;
			
			for(MJIShiftBattleNotify notify : m_notifies)
				notify.do_ended(m_battle_info);
				
			if(m_play_manager != null){
				m_play_manager.on_closed();
				m_play_manager = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void do_tick_play_manager(){
		if(m_play_manager != null){
			m_play_manager.on_tick();
		}
	}
	
	private void do_update_play_manager(){
		if(m_play_manager == null || m_loop_count == -1)
			return;
		
		if(--m_loop_count == 0){
			m_loop_count = m_play_manager.next_update_tick();
			GeneralThreadPool.getInstance().execute(new Runnable(){
				@Override
				public void run() {
					m_play_manager.on_update_tick();
				}
			});
		}
	}
	
	public void do_enter_battle_character(L1PcInstance pc){
		if(m_play_manager != null)
			m_play_manager.on_enter(pc);
	}
}
