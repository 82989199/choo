package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
//by.lins
public class S_케릭터생성 extends ServerBasePacket {
	public static final String S_케릭터생성 = "[S] S_LoginResult";
	public static final int 케릭비번표시 = 51;
	public static final int 케릭비번성공 = 22;

	public S_케릭터생성(int code) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(케릭비번표시);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
	}
	
	public S_케릭터생성() {
		buildPacket();
	}

	private void buildPacket() {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x3F);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
	}

	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return "[S] S_LoginResult";
	}
}
