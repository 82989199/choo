package server.threads.pc;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class CharacterQuickCheckThread extends Thread {

	private static CharacterQuickCheckThread _instance;

	public static CharacterQuickCheckThread getInstance() {
		if (_instance == null) {
			_instance = new CharacterQuickCheckThread();
			_instance.start();
		}
		return _instance;
	}

	public CharacterQuickCheckThread() {
		super("server.threads.pc.CharacterQuickCheckThread");
	}

	public void run() {
		System.out.println("** server.threads.pc.CharacterQuickCheckThread: 加载完成");
		while (true) {
			try {
				for (L1PcInstance _client : L1World.getInstance().getAllPlayers()) {
					if (_client.isPrivateShop() || _client.getAI() == null || _client.noPlayerCK || _client.getNetConnection() == null) {
						continue;
					} else {
						try {
							if (_client.getNetConnection().isClosed()) {
								System.out.println(_client.getName() + " << 未正常退出 : 请检查是否有多个客户端同时出现这个信息.");
								_client.logout();
								_client.getNetConnection().close();
							}
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(60000);//1분
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}

	}

}
