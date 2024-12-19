package l1j.server.MJItemExChangeSystem;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJItemExChangeLoader{
	private static MJItemExChangeLoader _instance;
	public static MJItemExChangeLoader getInstance(){
		if(_instance == null)
			_instance = new MJItemExChangeLoader();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload(){
		MJItemExChangeLoader old = _instance;
		_instance = new MJItemExChangeLoader();
		if(old != null){
			old.dispose();
			old = null;
		}
	}

	private HashMap<Integer, MJItemExChangeInformation> _exchanges;
	private MJItemExChangeLoader(){
		_exchanges = load();
	}
	
	private HashMap<Integer, MJItemExChangeInformation> load(){
		HashMap<Integer, MJItemExChangeInformation> exchanges = new HashMap<Integer, MJItemExChangeInformation>(32);
		Selector.exec("select * from tb_item_exchange_key_info", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJItemExChangeInformation exchange = MJItemExChangeInformation.newInstance(rs).load_rewards();
					exchanges.put(exchange.get_key_item_id(), exchange);
				}
			}
		});
		return exchanges;
	}
	
	public boolean use_item(L1PcInstance pc, L1ItemInstance key, int target_id){
		MJItemExChangeInformation exInfo = _exchanges.get(key.getItemId());
		if(exInfo == null)
			return false;
		
		L1ItemInstance target = pc.getInventory().getItem(target_id);
		if(target == null)
			return false;
		
		exInfo.use_item(pc, key, target);
		return true;
	}

	public void dispose(){
		if(_exchanges != null){
			_exchanges.clear();
			_exchanges = null;
		}
	}
}
