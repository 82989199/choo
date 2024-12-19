package l1j.server.server.Controller;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;

public class ClanBuffController implements Runnable {

	private static ClanBuffController _instance;

	public static ClanBuffController getInstance() {
		if (_instance == null) {
			_instance = new ClanBuffController();
			GeneralThreadPool.getInstance().execute(_instance);
		}
		return _instance;
	}

	public ClanBuffController() {
	}

	public void run() {
		try {				
			/** 혈맹버프 **/
			Clanbuff();			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			GeneralThreadPool.getInstance().schedule(this, 60000);
		}
	}

	private void Clanbuff() {
		try {
			for (L1Clan clan : L1World.getInstance().getAllClans()) {
				int bless = clan.getBless();			
				if (bless != 0) {					
					int[] times = clan.getBuffTime();
					ClanTable.getInstance().updateBuffTime(times[0], times[1], times[2], times[3], clan.getClanId());
				}				
				ClanTable.getInstance().updateBlessCount(clan.getClanId(), clan.getBlessCount());				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
