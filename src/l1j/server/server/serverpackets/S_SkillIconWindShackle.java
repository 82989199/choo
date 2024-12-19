package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SkillIconWindShackle extends ServerBasePacket {

	public S_SkillIconWindShackle(int objectId, int time) {
		writeC(Opcodes.S_EVENT);
		writeC(0x2c);
		writeD(objectId);
		writeC(0);
		writeC(0x0a);
		writeC(0x80);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
