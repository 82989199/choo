package server.threads.pc;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class AutoSaveThread extends Thread {

	private static AutoSaveThread _instance;
	private final int _saveCharTime;
	private final int _saveInvenTime;

	public static AutoSaveThread getInstance() {
		if (_instance == null) {
			_instance = new AutoSaveThread();
			_instance.start();
		}
		return _instance;
	}

	public AutoSaveThread() {
		super("server.threads.pc.AutoSaveThread");
		_saveCharTime = Config.AUTOSAVE_INTERVAL;
		_saveInvenTime = Config.AUTOSAVE_INTERVAL_INVENTORY;
	}
	public void run() {
		while (true) {
			try {
				final ArrayList<ExpCache> cache_datas = new ArrayList<ExpCache>(1024);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc == null || pc.getNetConnection() == null)
						continue;

					// 캐릭터 정보
					if (_saveCharTime * 1000 < System.currentTimeMillis() - pc.getlastSavedTime()) {
						pc.save();
						pc.setlastSavedTime(System.currentTimeMillis());
					}

					// 소지 아이템 정보
					if (_saveInvenTime * 1000 < System.currentTimeMillis() - pc.getlastSavedTime_inventory()) {
						pc.saveInventory();
						pc.setlastSavedTime_inventory(System.currentTimeMillis());
					}
					cache_datas.add(new ExpCache(pc.getId(), pc.getName(), pc.getExp(), pc.getLevel()));
				}
				if(cache_datas.size() > 0){
					Updator.batch("insert into character_exp_cache set object_id=?, character_name=?, exp=?, lvl=? on duplicate key update character_name=?, exp=?, lvl=?", new BatchHandler(){
						@Override
						public void handle(PreparedStatement pstm, int callNumber) throws Exception {
							int idx = 0;
							ExpCache cache = cache_datas.get(callNumber);
							pstm.setInt(++idx, cache.object_id);
							pstm.setString(++idx, cache.character_name);
							pstm.setInt(++idx, cache.exp);
							pstm.setInt(++idx, cache.lvl);
							pstm.setString(++idx, cache.character_name);
							pstm.setInt(++idx, cache.exp);
							pstm.setInt(++idx, cache.lvl);
						}
					}, cache_datas.size());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO 자동 생성된 catch 블록
					e.printStackTrace();
				}
			}
		}

	}

	public static class ExpCache{
		public int object_id;
		public String character_name;
		public int exp;
		public int lvl;
		
		public ExpCache(int object_id, String character_name, int exp, int lvl){
			this.object_id = object_id;
			this.character_name = character_name;
			this.exp = exp;
			this.lvl = lvl;
		}
	}
}
