package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Fishing extends ServerBasePacket {

	private static final String S_FISHING = "[S] S_Fishing";

	public S_Fishing() {
		buildPacket();
	}

	public S_Fishing(int objectId, int motionNum, int x, int y) {
		buildPacket(objectId, motionNum, x, y);
	}

	private void buildPacket() {
		writeC(Opcodes.S_ACTION);
		writeC(0x37);
		writeD(0x76002822);
		writeH(0x8AC3);
	}

	private void buildPacket(int objectId, int motionNum, int x, int y) {
		writeC(Opcodes.S_ACTION);
		writeD(objectId);
		writeC(motionNum);
		writeH(x);
		writeH(y);
		writeD(0);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_FISHING;
	}
}
