package l1j.server.server.serverpackets;

import java.util.Collection;
import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ACTION_UI2 extends ServerBasePacket {
	private static final String S_ACTION_UI2 = "S_ACTION_UI2";

	public static final int Elixir = 0xe9;
	public static final int MAGICEVASION = 488;
	public static final int stateProfile = 0xe7;
	public static final int EMOTICON = 0x40;
	public static final int CLAN_JOIN_SETTING = 0x4D;
	public static final int CLAN_JOIN_WAIT = 0x45;
	public static final int unk1 = 0x41;
	public static final int unknown1 = 0x4E;
	public static final int unknown2 = 0x91;
	//	public static final int 활력버프 = 0x6e;
	public static final int CLAN_RANK = 0x19;
	public static final int ICON_BUFF = 0x6e;

	public S_ACTION_UI2(int type, boolean ck) {
		buildPacket(type, ck);
	}

	public S_ACTION_UI2(int i, int t, int o, int gf, int ms) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(ICON_BUFF);
		writeC(0x00);
		writeC(0x08);
		writeC(0x02);
		writeC(0x10);
		write7B(i);
		writeC(0x18);
		write7B(t);
		writeC(0x20);
		writeC(0x09);
		writeC(0x28);
		write7B(gf);
		writeC(0x30);
		writeC(0x00);
		writeC(0x38);
		writeC(o);
		writeC(0x40);
		write7B(ms);
		writeC(0x48);
		writeC(0x00);
		writeC(0x50);
		writeC(0x00);
		writeC(0x58);
		writeC(0x01);
		writeH(0);
	}

	private void buildPacket(int type, boolean ck) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case CLAN_JOIN_WAIT:
			writeC(1);
			writeC(8);
			writeC(2);
			writeH(0);
			break;
		}
	}

	public S_ACTION_UI2(L1PcInstance pc, int code) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(code);
		switch (code) {	
		case CLAN_RANK:
			writeC(0x02);
			writeC(0x0a);
			int length = 0;
			if (pc.getClanname() != null)
				length = pc.getClanname().getBytes().length;
			if (length > 0) {
				writeC(length);
				writeByte(pc.getClanname().getBytes());
				writeC(0x10);
				switch (pc.getClanRank()) {
				case L1Clan.일반:
					writeC(L1Clan.CLAN_RANK_LEAGUE_PROBATION);
					break;
				case L1Clan.수련:
					writeC(L1Clan.CLAN_RANK_LEAGUE_PUBLIC);
					break;
				case L1Clan.수호:
					writeC(L1Clan.CLAN_RANK_LEAGUE_GUARDIAN);
					break;
				case L1Clan.군주:
					writeC(L1Clan.CLAN_RANK_LEAGUE_PRINCE);
					break;
				default:
					writeC(pc.getClanRank());
					break;
				}				
			} else {
				writeC(0x00);
			}
			writeH(0x00);
			break;
		}
	}


	public S_ACTION_UI2(int subCode) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subCode);
		switch (subCode) {
		case unknown2:// 모름
			writeC(01);
			writeC(0x88);
			writeC(0xd4);
			break;
		case unk1:
			writeC(1);
			writeC(8);
			writeC(0x80);
			writeC(0xe1);
			writeC(1);
			writeC(0x10);
			writeC(0xe5);
			writeC(0xe0);
			writeC(1);
			writeC(0x4a);
			writeC(0);
			break;
		case unknown1:
			writeC(1);
			writeC(8);
			writeC(3);
			writeC(0x10);
			writeC(0);
			writeC(0x18);
			writeC(0);
			writeH(0);
			break;
		case CLAN_JOIN_WAIT:
			writeC(0x01);
			writeH(0x0208);
			int size = 1;

			if (size > 0) {
				Collection<L1PcInstance> list = L1World.getInstance()
						.getAllPlayers();
				int i = 0;
				for (L1PcInstance pc : list) {
					writeC(0x12);
					if (i == 0)
						writeC(39);
					else if (i == 1)
						writeC(38);
					else if (i == 2)
						writeC(40);
					else
						writeC(39);
					/*
					 * else if(i == 3) writeC(39); else if(i == 4) writeC(38);
					 * else if(i == 5) writeC(41);
					 */
					writeC(0x08);
					writeD(0x1203A9A2);
					writeD(0xC5C3B208);
					writeD(0xB7ACC5EB);
					writeH(0x18B4);

					// byteWrite(pc.getId());
					// if(i == 0 || i == 3 || i == 4 || i >= 6){
					if (i == 0 || i >= 3) {
						writeD(0x02D8D1BE);
					} else {
						writeC(0xC3);
						writeH(0x5C8A);
					}
					writeC(0x22);
					byte[] name = pc.getName().getBytes();
					writeC(name.length);
					writeByte(name);
					writeC(0x2A);
					writeC("1".getBytes().length);// name.length);
					writeByte("1".getBytes());
					// byte[] memo = pc.getTitle().getBytes();//임시로 호칭
					// writeC(memo.length);
					// writeByte(memo);
					writeC(0x30);
					writeC(L1World.getInstance().getPlayer(pc.getName()) != null ? 1: 0);// 접속중
					writeC(0x38);
					writeC(pc.getType());// 클래스
					i++;
					if (i == 2)
						break;
				}
			}			writeH(0x00);
			break;
		}
	}
	
	public S_ACTION_UI2(int sub, int skillId, boolean on, int msgNum, int time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(sub);
		switch (sub) {
		case ICON_BUFF:
			writeC(0x08);
			writeC(on ? 2 : 3);//true/false
			writeC(0x10);
			writeBit(sub);
			writeC(0x18);
			writeBit(time);//초
			writeC(0x20);
			writeC(0x08);
			writeC(0x28);
			writeBit(skillId);//원하는 아이콘 모양 인벤번호
			writeH(0x30);
			writeC(0x38);
			writeC(0x03);
			writeC(0x40);
			writeBit(msgNum);//스트링 번호(아이콘 안에 내용)
			writeC(0x48);
			writeC(0x00);
			writeH(0x0050);
			writeC(0x58);
			writeC(0x01);
			writeC(0x60);
			writeC(0x00);
			writeC(0x68);
			writeC(0x00);
			writeC(0x70);
			writeC(0x00);
			writeH(0x00);
			break;
		}
	}

	private static final String 활력_활력버프1 = "00 08 02 10 " + "f2 "// 버프 종류
			+ "12 18";
	private static final String 활력_활력버프2 = "20 09 28 97 34 30 00 38 00 40 "
			+ "fb 21 "// 버프종류
			+ "48 " + "00 50 00 58 01 60 01 68 e8 21 70 01 45 63";

	private static final String 활력_공격버프1 = "00 08 02 10 f3 12 18";
	private static final String 활력_공격버프2 = "20 09 28 97 34 30 00 38 00 40 fc 21 48 "
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 6d 23";

	private static final String 활력_방어버프1 = "00 08 02 10 f4 12 18";
	private static final String 활력_방어버프2 = "20 09 28 97 34 30 00 38 00 40 fd 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_마법버프1 = "00 08 02 10 f5 12 18";
	private static final String 활력_마법버프2 = "20 09 28 97 34 30 00 38 00 40 fe 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_스턴버프1 = "00 08 02 10 f6 12 18";
	private static final String 활력_스턴버프2 = "20 09 28 97 34 30 00 38 00 40 ff 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_홀드버프1 = "00 08 02 10 f7 12 18";
	private static final String 활력_홀드버프2 = "20 09 28 97 34 30 00 38 00 40 80 22 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	public S_ACTION_UI2(String 활력코드, long 시간) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		//		writeC(활력버프);
		writeC(ICON_BUFF);

		String 활력버프패킷 = "";
		if (활력코드.equals("활력")) {
			활력버프패킷 = 활력_활력버프1;
		} else if (활력코드.equals("공격")) {
			활력버프패킷 = 활력_공격버프1;
		} else if (활력코드.equals("방어")) {
			활력버프패킷 = 활력_방어버프1;
		} else if (활력코드.equals("마법")) {
			활력버프패킷 = 활력_마법버프1;
		} else if (활력코드.equals("스턴")) {
			활력버프패킷 = 활력_스턴버프1;
		} else if (활력코드.equals("홀드")) {
			활력버프패킷 = 활력_홀드버프1;
		}

		StringTokenizer st = new StringTokenizer(활력버프패킷.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}

		byteWrite(시간 / 1000);

		if (활력코드.equals("활력")) {
			활력버프패킷 = 활력_활력버프2;
		} else if (활력코드.equals("공격")) {
			활력버프패킷 = 활력_공격버프2;
		} else if (활력코드.equals("방어")) {
			활력버프패킷 = 활력_방어버프2;
		} else if (활력코드.equals("마법")) {
			활력버프패킷 = 활력_마법버프2;
		} else if (활력코드.equals("스턴")) {
			활력버프패킷 = 활력_스턴버프2;
		} else if (활력코드.equals("홀드")) {
			활력버프패킷 = 활력_홀드버프2;
		}
		st = new StringTokenizer(활력버프패킷.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}



	private int Bitsize(int objid) {
		int size = 0;
		if (objid <= 127) {
			size = 1;
		} else if (objid <= 16383) {
			size = 2;
		} else if (objid <= 2097151) {
			size = 3;
		} else if (objid <= 268435455) {
			size = 4;
		} else if ((long) objid <= 34359738367L) {
			size = 5;
		}
		return size;
	}

	public S_ACTION_UI2(int type, int stat) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		writeC(0x01);

		switch(type){
		case MAGICEVASION:
		case Elixir:
			writeC(0x08);
			writeC(stat);
			break;
		case stateProfile:
			writeC(0x0a); //힘
			if(stat == 45){
				writeH(0x0808);
				writeC(stat);
				writeD(0x03180310);
				writeH(0x0120);
			}else{
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
			}

			writeC(0x12); //인트
			if(stat == 45){
				writeH(0x0808);
				writeC(stat);
				writeD(0x03180310);
				writeH(0x0120);
			}else{
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
			}

			writeC(0x1a);
			if(stat == 25){
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
				writeH(0x3238);
			}else if(stat == 35){
				writeH(0x0808);
				writeC(stat);
				writeD(0x01180110);
				writeH(0x6438);
			}else if(stat == 45){
				writeH(0x0809);
				writeC(stat);
				writeD(0x03180310);
				writeC(0x38);
				writeH(0x0196);
			}

			writeC(0x22);
			if(stat == 45){
				writeH(0x0808);
				writeC(stat);
				writeD(0x03180310);
				writeH(0x0120);
			}else{
				writeH(0x0806);
				writeC(stat);
				writeD(0x01180110);
			}

			writeC(0x2a);
			if(stat == 25){
				writeH(0x0806);
				writeC(stat);
				writeD(0x32300110);
			}else if(stat == 35){
				writeH(0x0808);
				writeC(stat);
				writeD(0x01180110);
				writeH(0x6430);
			}else if(stat == 45){
				writeH(0x0809);
				writeC(stat);
				writeD(0x02180310);
				writeC(0x30);
				writeH(0x0196);
			}

			writeD(0xff080b32);
			writeD(0xffffffff);
			writeD(0xffffffff);
			writeC(0x01);
			break;
		}

		writeH(0);

	}

	public S_ACTION_UI2(int type, int subtype, int objid) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case EMOTICON:
			writeC(0x01);
			writeC(0x08);
			int temp = objid / 128;
			if (temp > 0) {
				writeC(hextable[objid % 128]);
				while (temp > 128) {
					writeC(hextable[temp % 128]);
					temp = temp / 128;
				}
				writeC(temp);
			} else {
				if (objid == 0) {
					writeC(0);
				} else {
					writeC(hextable[objid]);
					writeC(0);
				}
			}
			// byteWrite(value);
			writeC(0x10);
			writeC(0x02);
			writeC(0x18);
			writeC(subtype);
			writeH(0);
			break;
		case CLAN_JOIN_SETTING:
			writeD(0x10010801);
			writeC(subtype);// 가입 설정
			writeC(0x18);
			writeC(objid);// 가입 유형
			writeD(0x00001422);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			break;	

		default:
			break;
		}
	}


	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ACTION_UI2;
	}
}
