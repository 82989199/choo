package l1j.server.server.utils;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class Message {
	
	private static Message instance;
	public static Message getInstance() {
		if (instance == null) {
			instance = new Message();
		}
		return instance;
	}
	
	public void get_system_message(L1PcInstance pc, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		pc.sendPackets(new S_SystemMessage(sb.toString()));
	}
	public void get_system_message1(L1PcInstance pc, ServerBasePacket packet) {
		StringBuilder sb = new StringBuilder();
		pc.sendPackets(new S_SystemMessage(sb.toString()));
		if(pc != null)
			pc.sendPackets(packet);
	}
}
