package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SlotOpen extends ServerBasePacket {
	
	public S_SlotOpen(int subType, int value){
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x43);
		writeD(subType);
		writeC(value);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_SlotOpen";
	}

}
