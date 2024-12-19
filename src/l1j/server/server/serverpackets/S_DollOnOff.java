package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_DollOnOff extends ServerBasePacket {
	private static final String S_DOLLONOF = "S_DOLLONOF";
			
	public S_DollOnOff(L1DollInstance doll, L1ItemInstance item, int dolltime, boolean onoff) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x11);
		writeC(0x09);
		
		writeC(0x08);
		writeBit(!onoff ? 0 : dolltime);

		writeC(0x10);
		writeBit(!onoff ? 0 : doll != null ? doll.getDollType() : 0);

		writeC(0x18);
		writeBit(!onoff ? 0 : item != null ? item.getId() : 0);

		if (item != null) {
			byte[] status = item.getStatusBytes();
			if (status != null) {
				writeC(0x22);
				writeBit(status.length);
				writeByte(status);
			}
		}
		
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_DOLLONOF;
	}
}
