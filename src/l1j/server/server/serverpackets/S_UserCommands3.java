
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands3 extends ServerBasePacket {

	private static final String S_UserCommands3 = "[C] S_UserCommands3";

	public S_UserCommands3(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//넘버
		writeS(" 메티스 ");//글쓴이?
		writeS(" 제작 비법서 ");//날짜?
		writeS("");//제목?
		          writeS("\n === 흑왕도 ===\n" +
				 "\n" +
				 " 용의 심장(1개)\n" +
				 " 최고급 루비(3개)\n" +
				 " 흑빛의 이도류(1개)\n" +
				 " 아데나 (100,000원)\n" +
				 " 저주받은 피혁(10개)\n" +
				 " 얼음여왕의 숨결(9개)\n" +
				 " 그랑카인의 눈물(3개)\n" +
				 "\n" +
				 " ==========================");
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands3;
	}
}

