package l1j.server.MJTemplate.Chain.Chat;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJWorldChatFilterChain extends MJAbstractActionChain<MJIWorldChatFilterHandler>{
	private static MJWorldChatFilterChain _instance;
	public static MJWorldChatFilterChain getInstance(){
		if(_instance == null)
			_instance = new MJWorldChatFilterChain();
		return _instance;
	}
	
	private MJWorldChatFilterChain(){
		super();
	}
	
	public boolean handle(L1PcInstance pc, String message){
		for(MJIWorldChatFilterHandler handler : m_handlers){
			if(handler.is_chat(pc, message))
				return true;
		}
		return false;
	}
}
