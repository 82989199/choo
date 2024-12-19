package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ChatPacket extends ServerBasePacket {

	private static final String _S__1F_NORMALCHATPACK = "[S] S_ChatPacket";

	public S_ChatPacket(String targetname, String chat, int opcode) {
		writeC(opcode);
		writeC(9);
		writeS("-> (" + targetname + ") " + chat);
	}
	
	public S_ChatPacket(String targetname, int type, String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(type);
		writeS("[" + targetname + "] " + chat);
	}

	// 매니저용 귓말
	public S_ChatPacket(String from, String chat) {
		writeC(Opcodes.S_TELL);
		writeS(from);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(1);//10
		writeS(chat);
	}

	public S_ChatPacket(String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(0x0F);
		writeD(000000000);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int a, int b, int c) {
		writeC(Opcodes.S_MESSAGE);
		writeC(4);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int test) {
		writeC(Opcodes.S_SAY);
		writeC(15);
		writeD(pc.getId());
		writeS(chat);
	}

	public S_ChatPacket(String chat, int opcode) {
		writeC(opcode);
		writeC(2);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int opcode, int type) {
		writeC(opcode);
				
		switch (type) {
		case 0: // 통상채팅
			writeC(type);
			writeD(pc.getId());
			writeS(pc.getName() + ": " + chat);
			break;
		case 2: // 절규
			writeC(type);
			if (pc.isInvisble()) {
				writeD(0);
			} else {
				writeD(pc.getId());
			}
			writeS("<" + pc.getName() + "> " + chat);
			writeH(pc.getX());
			writeH(pc.getY());
			break;
        case 3:
            writeC(type);
        	if (pc.getName().equalsIgnoreCase("메티스")&& !pc.getName().equalsIgnoreCase("미소피아")&& !pc.getName().equalsIgnoreCase("카시오페아")) {
				writeS("\\aD[******] " + chat);
            }
            break;
		case 4: // 혈맹채팅
			writeC(type);
			if (pc.getAge() == 0) {
				writeS("{" + pc.getName() + "} " + chat);
			} else {
				writeS("{" + pc.getName() + "(" + pc.getAge() + ")" + "} " + chat);
			}
			break;
		case 9: // 위스파
			writeC(type);
			writeS("-> (" + pc.getName() + ") " + chat);
			break;
		case 11: // 파티채팅
			writeC(type);
			writeS("(" + pc.getName() + ") " + chat);
			break;
		case 12: // 연합 채팅
			writeC(type);
			writeS("[" + pc.getName() + "] " + chat);
			break;
		case 13:
			writeC(4);
			writeS("{{" + pc.getName() + "}} " + chat);
			break;
		case 14: // 채팅파티
			writeC(type);
			writeD(pc.getId());
			writeS("\\fU(" + pc.getName() + ") " + chat); // #
			break;
		case 15:
			writeC(type);
			writeS("[" + pc.getName() + "] " + chat);
			break;
		case 16: // 위스파
			writeS(pc.getName());
			writeS(chat);
			break;
		case 17: // 군주채팅 +
			writeC(type);
			writeS("{" + pc.getName() + "} " + chat);
			break;
		default:
			break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__1F_NORMALCHATPACK;
	}

}
