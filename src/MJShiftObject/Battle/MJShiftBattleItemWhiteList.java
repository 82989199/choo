package MJShiftObject.Battle;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

public class MJShiftBattleItemWhiteList {
	private static MJShiftBattleItemWhiteList _instance;
	public static MJShiftBattleItemWhiteList getInstance(){
		if(_instance == null)
			_instance = new MJShiftBattleItemWhiteList();
		return _instance;	
	}
	
	public static void reload(){
		MJShiftBattleItemWhiteList old = _instance;
		_instance = new MJShiftBattleItemWhiteList();
		if(old != null){
			old.dispose();
			old = null;
		}
	}
	
	private HashMap<Integer, Byte> m_white_list; 
	private MJShiftBattleItemWhiteList(){
		m_white_list = new HashMap<Integer, Byte>();
		Selector.exec("select itemId from server_battle_white_list", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next())
					m_white_list.put(rs.getInt("itemId"), Byte.MIN_VALUE);
			}
		});
	}
	
	public boolean use(L1PcInstance pc, L1ItemInstance item){
		if(pc.isGm())
			return true;
		
		if(item == null)
			return true;
		
		if(!pc.is_shift_client())
			return true;
		
		L1Item template = item.getItem();
		if(template instanceof L1EtcItem){
			return m_white_list.containsKey(template.getItemId());
		}
		return true;	
	}
	
	private void dispose(){
		if(m_white_list != null){
			m_white_list.clear();
			m_white_list = null;
		}
	}
}
