
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands6 extends ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";

	public S_UserCommands6(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//넘버
		writeS(" 메티스 ");//글쓴이?
		writeS(" 제작 비법서 ");//날짜?
		writeS("");//제목?
		          writeS("\n === 생명의 마안 ===\n" +
				 "\n" +
				 " 생명의마안 조각(100개)\n" +
				 "\n" +
				 " ==========================");

	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands1;
	}
}

