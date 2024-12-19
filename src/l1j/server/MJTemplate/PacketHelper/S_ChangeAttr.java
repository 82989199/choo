package l1j.server.MJTemplate.PacketHelper;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ChangeAttr extends ServerBasePacket{
	
	public static S_ChangeAttr get(L1Object obj, boolean isWidth, boolean isClose){
		return get(obj.getX(), obj.getY(), isWidth, isClose);
	}
	
	public static S_ChangeAttr get(int x, int y, boolean isWidth, boolean isClose){
		S_ChangeAttr s = new S_ChangeAttr();
		s.writeH(x);
		s.writeH(y);
		s.writeB(isWidth);
		s.writeB(isClose);
		return s;
	}
	
	private S_ChangeAttr(){
		writeC(Opcodes.S_CHANGE_ATTR);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
