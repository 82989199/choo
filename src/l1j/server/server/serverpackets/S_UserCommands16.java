
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands16 extends ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";

	public S_UserCommands16(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);//넘버
		writeS(" 메티스 ");//글쓴이?
		writeS(" 제작 비법서 ");//날짜?
		writeS("");//제목?
		          writeS("\n ─ +10 나이트발드의 양손검 ─\n" +
				 "\n" +
				 " 수룡 비늘 (10)개\n" +
				 " 풍룡 비늘 (10)개\n" +
				 " 지룡 비늘 (10)개\n" +
				 " 화룡 비늘 (10)개\n" +
				 " 바실리스크의 뿔 (60)개\n" +
				 " 장인의 무기 마법 주문서 (3)개\n" +
				 " +8 나이트발드의 양손검 (1)개\n" +
				 "\n" +
				 " ──────────────"+
		          "     제작은 리니지의 꽃 입니다.\n");
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_UserCommands1;
	}
}

