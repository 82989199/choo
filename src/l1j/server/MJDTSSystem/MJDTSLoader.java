package l1j.server.MJDTSSystem;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJDTSLoader{
	private static MJDTSLoader _instance;
	public static MJDTSLoader getInstance(){
		if(_instance == null)
			_instance = new MJDTSLoader();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload(){
		MJDTSLoader old = _instance;
		_instance = new MJDTSLoader();
		if(old != null){
			old.dispose();
			old = null;
		}
	}

	private HashMap<Integer, MJDTSElement> _elements;
	private MJDTSLoader(){
		_elements = load();
	}
	
	private HashMap<Integer, MJDTSElement> load(){
		HashMap<Integer, MJDTSElement> elements = new HashMap<Integer, MJDTSElement>(256);
		Selector.exec("select * from tb_designate_teleport_scroll", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){				
					MJDTSElement element = MJDTSElement.newInstance(rs);
					elements.put(element.get_item_id(), element);
				}
			}
		});
		
		return elements;
	}

	public boolean use_item(L1PcInstance pc, L1ItemInstance item){
		MJDTSElement element = _elements.get(item.getItemId());
		if(element == null)
			return false;
		
		element.use(pc, item);
		return true;
	}
	
	public void dispose(){
		if(_elements != null){
			_elements.clear();
			_elements = null;
		}
	}
}
