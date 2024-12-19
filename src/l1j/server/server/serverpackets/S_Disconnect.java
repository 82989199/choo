package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Disconnect extends ServerBasePacket {
	private static S_Disconnect _disconnect;
	public static S_Disconnect get(){
		if(_disconnect == null)
			_disconnect = new S_Disconnect();
		return _disconnect;
	}
	
	public S_Disconnect() {
		int content = 500;
		writeC(Opcodes.S_KICK);
		writeH(content);
		writeD(0x00000000);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
