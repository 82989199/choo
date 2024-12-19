package server;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class KeyPacket extends ServerBasePacket {
	public KeyPacket() {
		for (int i = 0; i < Opcodes.S_KEYBYTES.length; i++) {
			writeC(Opcodes.S_KEYBYTES[i]);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
