package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_ChangeItemUseType extends ServerBasePacket {
	public S_ChangeItemUseType(int object_id, int use_type) {
		writeC(Opcodes.S_CHANGE_ITEM_USE);
		writeD(object_id);
		writeC(use_type);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
