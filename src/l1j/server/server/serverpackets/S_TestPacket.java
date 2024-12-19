//13 2f 02 08 00 10 00 84 e9
package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;

public class S_TestPacket extends ServerBasePacket {
	private static final String S_TestPack = "S_TestPack";

	public static final int a = 103;
	
	public static final int b = 103;
	
	public static final int 서버인사멘트 = 103;
			
	public S_TestPacket(int type,int gfxid, int messageCode, String color) { 
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch(type) {
		case a:
			writeC(0x00);
			writeC(0x08);
			writeBit(gfxid*2);
			writeC(0x10);
			writeBit(messageCode*2);
			writeC(0x1a);
			writeC(0x03);
			StringTokenizer st = new StringTokenizer(color.toString());
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
			writeC(0x20);
			writeC(0x14);
			writeH(0x00);
			break;
		}
	}
		public S_TestPacket(int type) { 
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeC(type);
			switch(type) {
			case b:
				writeC(0x00);
				writeC(0x08);
				String 메세지 = "00 08 " 
						+ "b8 62 " // b4 62 = 가운데 군주얼굴 , 01 = 이미지없음
						+ "10 " // ?
						+ "c6 49 " // 메세지코드
						+ "1a 03 " // ?
						+ "00 ff ff " // 글자색깔 (html글자색상표참조)
						+ "20 14";
				StringTokenizer st = new StringTokenizer(메세지.toString());
				while (st.hasMoreTokens()) {
					writeC(Integer.parseInt(st.nextToken(), 16));
				}
				writeH(0x00);
				break;
			}
	/*	case 서버인사멘트:
			writeC(0x00);
			writeC(0x08);
			String 메세지 = "00 08 " 
					+ "b4 62 " // b4 62 = 가운데 군주얼굴 , 01 = 이미지없음
					+ "10 " // ?
					+ "c0 20 " // 메세지코드
					+ "1a 03 " // ?
					+ "00 ff ff " // 글자색깔 (html글자색상표참조)
					+ "20 14";

			StringTokenizer st = new StringTokenizer(메세지.toString());
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
			writeH(0x00);
			break;
		}*/
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_TestPack;
	}
}
