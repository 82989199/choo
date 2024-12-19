package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;

public class S_ExplosionNoti extends ServerBasePacket {
	private static final int SC_OBJECT_EXPLOSION_NOTI = 0x0332;
	
	public static S_ExplosionNoti get(L1Object obj, long mills){
		S_ExplosionNoti s = new S_ExplosionNoti();
		s.writeC(0x08);
		s.writeBit(obj.getId());
		s.writeC(0x10);
		s.writeBit(mills);
		s.writeH(0x00);
		return s;
	}
	
	private S_ExplosionNoti(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_OBJECT_EXPLOSION_NOTI);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

}
