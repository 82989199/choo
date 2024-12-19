
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands5 extends ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";

	public S_UserCommands5(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//넘버
		writeS(" 메티스 ");//글쓴이?
		writeS(" 제작 비법서 ");//날짜?
		writeS("");//제목?
		          writeS("\n === 연금술사의 돌 ===\n" +
				 "\n" +
				 " 고대인의 주술서 1권(1개)\n" +
				 " 고대인의 주술서 2권(1개)\n" +
				 " 고대인의 주술서 3권(1개)\n" +
				 " 고대인의 주술서 4권(1개)\n" +
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

