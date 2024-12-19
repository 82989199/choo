package l1j.server.MJTemplate.Chain.Chat;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJINormalChatFilterHandler {
	public boolean is_chat(L1PcInstance owner, String message);
}
