package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_OwnCharAttrDef extends ServerBasePacket {

	private static final String S_OWNCHARATTRDEF = "[S] S_OwnCharAttrDef";

	public S_OwnCharAttrDef(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_AC);
		writeD(pc.getAC().getAc());
		writeH(pc.getResistance().getFire());
		writeH(pc.getResistance().getWater());
		writeH(pc.getResistance().getWind());
		writeH(pc.getResistance().getEarth());
		writeC(pc.getDg());	// dg
		writeC(pc.getTotalER());	// er
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_OWNCHARATTRDEF;
	}
}
