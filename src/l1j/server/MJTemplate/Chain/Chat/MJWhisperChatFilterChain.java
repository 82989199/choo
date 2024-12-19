package l1j.server.MJTemplate.Chain.Chat;

import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJWhisperChatFilterChain extends MJAbstractActionChain<MJIWhisperChatFilterHandler>{
	private static MJWhisperChatFilterChain _instance;
	public static MJWhisperChatFilterChain getInstance(){
		if(_instance == null)
			_instance = new MJWhisperChatFilterChain();
		return _instance;
	}
	
	private MJWhisperChatFilterChain(){
		super();
	}
	
	public boolean handle(L1PcInstance from, String to_name, String message){
		for(MJIWhisperChatFilterHandler handler : m_handlers){
			if(handler.is_chat(from, to_name, message))
				return true;
		}
		return false;
	}
}
