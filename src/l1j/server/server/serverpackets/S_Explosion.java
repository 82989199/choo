package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;

public class S_Explosion extends ServerBasePacket{
	private static final int SC_OBJECT_EXPLOSION_NOTI = 0x332;
	
	public static S_Explosion get(L1Object obj, long timeinMillis){
		S_Explosion s = new S_Explosion();
		s.writeC(0x08);
		s.writeBit(obj.getId());
		s.writeC(0x10);
		s.writeBit(timeinMillis);
		s.writeH(0x00);
		return s;
	}
	
	private S_Explosion(){
		super(24);
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_OBJECT_EXPLOSION_NOTI);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
