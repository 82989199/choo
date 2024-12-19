package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ClanName extends ServerBasePacket {
	private static final String S_ClanName = "[S] S_ClanName";

	public S_ClanName(L1PcInstance pc, int emblemId, int rank) {
		writeC(Opcodes.S_PLEDGE);
		writeD(pc.getId());
		writeS(rank > 0 ? pc.getClanname() : "");
		writeC(0);
		writeD(emblemId);		
		writeC(rank > 0 ? rank : 0x00);
		writeH(0x00);
	}

	public S_ClanName(L1PcInstance pc){
		writeC(Opcodes.S_PLEDGE);
		writeD(pc.getId());
		L1Clan c;
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ClanName;
	}
}
