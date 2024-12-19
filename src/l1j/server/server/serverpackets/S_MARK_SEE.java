package l1j.server.server.serverpackets;

import java.util.Collection;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_MARK_SEE extends ServerBasePacket {
	public static final String S_MARK_SEE = "[S] S_MARK_SEE";
	private byte[] _byte = null;

	public S_MARK_SEE(L1Clan clan, int type) {
		if (clan == null)
			return;
		buildPacket(clan, type);
	}

	private void buildPacket(L1Clan clan, int type) {
		writeC(Opcodes.S_PLEDGE_WATCH);
		writeH(type);
		if (type == 2) {
			if (clan == null) {
				writeD(0x00);
			}
		}
		writeH(0);
	}

	public S_MARK_SEE(L1PcInstance pc, int type, boolean on) {
		writeC(Opcodes.S_PLEDGE_WATCH);
		writeH(type);
		if (type == 2) {
			if (on) {
				Collection<L1Clan> list = L1World.getInstance().getAllClans();
				int size = list.size();
				writeD(size);
				if (size > 0) {
					for (L1Clan clan : list) {
						writeS(clan.getClanName());
					}
				}
			} else {
				writeD(0x00);
			}
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_MARK_SEE;
	}
}
