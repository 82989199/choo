
package l1j.server.server.serverpackets;
import l1j.server.server.Opcodes;

public class S_UserCommands2 extends ServerBasePacket {

	private static final String S_UserCommands2 = "[C] S_UserCommands2";

	public S_UserCommands2(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//넘버
		writeS(" 메티스 ");//글쓴이?
		writeS(" 제작 비법서 ");//날짜?
		writeS("");//제목?
        writeS("\n === 사이하의 활 ===\n" +
		 "\n" +
		 " 장궁(1개)\n" +
		 " 풍룡의 비늘(15개)\n" +
		 " 바람의 눈물(50개)\n" +
		 " 그리폰의 깃털(30개)\n" +
		 "\n" +
		 " ==========================");

	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands2;
	}
}

