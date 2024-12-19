
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands1 extends ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";
	public S_UserCommands1(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//넘버
		writeS(" 메티스 ");//글쓴이?
		writeS(" 제작 비법서 ");//날짜?
		writeS("");//제목?
		          writeS("\n === 싸울 아비 장검 ===\n" +
				 "\n" +
				 " 최고급 루비(5개)\n" +
				 " 최고급 에메랄드(5개)\n" +
				 " 최고급 사파이어(5개)\n" +
				 " 최고급 다이아몬드(5개)\n" +
				 " 오리하루콘 화살 (500개)\n" +
				 " 아시타지오의 재(30개)\n" +
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

