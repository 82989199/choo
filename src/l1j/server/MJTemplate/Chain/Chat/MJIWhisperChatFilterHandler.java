package l1j.server.MJTemplate.Chain.Chat;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIWhisperChatFilterHandler {
	public boolean is_chat(L1PcInstance from, String to_name, String message);
}
