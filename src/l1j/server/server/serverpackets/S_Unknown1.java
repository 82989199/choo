package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Unknown1 extends ServerBasePacket {
	public S_Unknown1(L1PcInstance pc) {
		writeC(Opcodes.S_ENTER_WORLD_CHECK);
		writeC(0x03);
	}
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
