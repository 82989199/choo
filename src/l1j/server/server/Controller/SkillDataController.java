package l1j.server.server.Controller;

import java.util.Iterator;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class SkillDataController implements Runnable {

	private static SkillDataController instance;
	public static SkillDataController getInstance() {
		if (instance == null) {
			instance = new SkillDataController();
			GeneralThreadPool.getInstance().execute(instance);
		}
		return instance;
	}

	private FastTable<L1PcInstance> str_ice = new FastTable<L1PcInstance>((Config.MAX_ONLINE_USERS / 8) * 5);

	public void add_str_ice(L1PcInstance pc) {
		synchronized(str_ice) {
			if (!str_ice.contains(pc))  str_ice.add(pc);
		}
	}

	private void start_str_ice() {
		try {
			Iterator<L1PcInstance> iter = str_ice.iterator();
			L1PcInstance pc = null;

			while (iter.hasNext()) {
				pc = iter.next();
				if (pc == null) {
					continue;
				}
				pc.get_skill().set_str_ice_time(pc.get_skill().get_str_ice_time() - 1);

				if (pc.get_skill().get_str_ice_time() <= 0) {
					pc.get_skill().end_str_ice();
					iter.remove();
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	private FastTable<L1PcInstance> dex_ice = new FastTable<L1PcInstance>((Config.MAX_ONLINE_USERS / 8));

	public void add_dex_ice(L1PcInstance pc) {
		synchronized(dex_ice) {
			if (!dex_ice.contains(pc))  dex_ice.add(pc);
		}
	}

	private void start_dex_ice() {
		try {
			Iterator<L1PcInstance> iter = dex_ice.iterator();
			L1PcInstance pc = null;

			while (iter.hasNext()) {
				pc = iter.next();
				if (pc == null) {
					continue;
				}
				pc.get_skill().set_dex_ice_time(pc.get_skill().get_dex_ice_time() - 1);

				if (pc.get_skill().get_dex_ice_time() <= 0) {
					pc.get_skill().end_dex_ice();
					iter.remove();
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	private FastTable<L1PcInstance> int_ice = new FastTable<L1PcInstance>((Config.MAX_ONLINE_USERS / 8) * 2);

	public void add_int_ice(L1PcInstance pc) {
		synchronized(int_ice) {
			if (!int_ice.contains(pc))  int_ice.add(pc);
		}
	}

	private void start_int_ice() {
		try {
			Iterator<L1PcInstance> iter = int_ice.iterator();
			L1PcInstance pc = null;

			while (iter.hasNext()) {
				pc = iter.next();
				if (pc == null) {
					continue;
				} 
				pc.get_skill().set_int_ice_time(pc.get_skill().get_int_ice_time() - 1);

				if (pc.get_skill().get_int_ice_time() <= 0) {
					pc.get_skill().end_int_ice();
					iter.remove();
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			start_str_ice();
			start_dex_ice();
			start_int_ice();
		} catch (Exception e) {  
			
		}finally{
			GeneralThreadPool.getInstance().schedule(this, 1000);
		}
	}
}
