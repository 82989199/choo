package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_OnlyEffect extends ServerBasePacket {
	private int _gfxId;
	
	public S_OnlyEffect(int objId, int gfxId){
		writeC(Opcodes.S_EFFECT);
		writeD(objId);
		writeH(gfxId);
		_gfxId = gfxId;
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_OnlyEffect.class.getName();
	}
}
