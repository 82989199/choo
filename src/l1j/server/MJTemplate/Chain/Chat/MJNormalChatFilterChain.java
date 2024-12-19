package l1j.server.MJTemplate.Chain.Chat;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNormalChatFilterChain extends MJAbstractActionChain<MJINormalChatFilterHandler>{
	private static MJNormalChatFilterChain _instance;
	public static MJNormalChatFilterChain getInstance(){
		if(_instance == null)
			_instance = new MJNormalChatFilterChain();
		return _instance;
	}
	
	private MJNormalChatFilterChain(){
		super();
	}
	
	public boolean handle(L1PcInstance pc, String message){
		for(MJINormalChatFilterHandler handler : m_handlers){
			if(handler.is_chat(pc, message))
				return true;
		}
		return false;
	}
}
