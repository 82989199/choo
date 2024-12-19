package l1j.server.MJExpRevision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import server.threads.pc.AutoSaveThread.ExpCache;

public class ExpRevision {
	public static void do_revision(){
		ArrayList<ExpCache> source_data = get_characters_info();
		if(source_data == null || source_data.size() <= 0)
			return;
		
		HashMap<Integer, ExpCache> cache_data = get_cache_info();
		if(cache_data == null || cache_data.size() <= 0)
			return;
		
		do_revision_logic(source_data, cache_data);
	}
	
	private static void do_revision_logic(ArrayList<ExpCache> source_data, HashMap<Integer, ExpCache> cache_data){
		ArrayDeque<ExpCache> diff_q = new ArrayDeque<ExpCache>();
		for(ExpCache source : source_data){
			ExpCache cache = cache_data.get(source.object_id);
			if(cache == null)
				continue;
			
			int level_diff = Math.abs(cache.lvl - source.lvl);
			int exp_diff = Math.abs(cache.exp - source.exp);
			if(level_diff >= 1 || exp_diff > 10000000){
				diff_q.offer(cache);
				System.out.println(String.format("[경험치 보정]%s님(%d) %d(%d) -> %d(%d)", 
						source.character_name, source.object_id, source.lvl, source.exp, cache.lvl, cache.exp));
			}
		}
		if(diff_q.size() <= 0)
			return;
		
		int size = diff_q.size();
		Updator.batch("update characters set Exp=?, level=? where objid=?", new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				int idx = 0;
				ExpCache cache = diff_q.poll();
				pstm.setInt(++idx, cache.exp);
				pstm.setInt(++idx, cache.lvl);
				pstm.setInt(++idx, cache.object_id);
			}
		}, size);
		System.out.println(String.format("%d명의 캐릭터 경험치를 보정했습니다.", size));
	}
	
	private static ArrayList<ExpCache> get_characters_info(){
		MJObjectWrapper<ArrayList<ExpCache>> wrapper = new MJObjectWrapper<ArrayList<ExpCache>>();
		wrapper.value = new ArrayList<ExpCache>();
		Selector.exec("select objid, char_name, level, Exp from characters", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					ExpCache cache = new ExpCache(rs.getInt("objid"), rs.getString("char_name"), rs.getInt("Exp"), rs.getInt("level"));
					wrapper.value.add(cache);
				}
			}
		});
		return wrapper.value;
	}
	
	private static HashMap<Integer, ExpCache> get_cache_info(){
		MJObjectWrapper<HashMap<Integer, ExpCache>> wrapper = new MJObjectWrapper<HashMap<Integer, ExpCache>>();
		wrapper.value = new HashMap<Integer, ExpCache>();
		Selector.exec("select * from character_exp_cache", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					ExpCache cache = new ExpCache(rs.getInt("object_id"), rs.getString("character_name"), rs.getInt("exp"), rs.getInt("lvl"));
					wrapper.value.put(cache.object_id, cache);
				}
			}
		});
		return wrapper.value;
	}
}
