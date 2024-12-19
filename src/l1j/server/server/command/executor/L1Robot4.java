package l1j.server.server.command.executor;

import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.server.model.Instance.L1PcInstance;


public class L1Robot4 implements L1CommandExecutor {
	private L1Robot4() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Robot4();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		MJBotLoadManager.commands(pc, arg);
	}
}
