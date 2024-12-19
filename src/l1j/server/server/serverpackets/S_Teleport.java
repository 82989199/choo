package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Teleport extends ServerBasePacket {

	private static final String S_TELEPORT = "[S] S_Teleport";

	public S_Teleport(L1PcInstance pc) {
		writeC(Opcodes.S_REQUEST_SUMMON);//52
	    writeH(0x00); 
//		writeC(0x00);//0
//		writeC(0x40);//64
//		writeD(pc.getId());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_TELEPORT;
	}
}
