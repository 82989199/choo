package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SocialAction extends ServerBasePacket {
	private static final int SC_SOCIAL_ACTION_NOTI = 0x140;
	
	public static S_SocialAction get(int id, int type, int code){
		S_SocialAction s = new S_SocialAction();
		s.writeBit(0x08);
		s.writeBit(id);
		s.writeBit(0x10);
		s.writeBit(type);
		s.writeBit(0x18);
		s.writeBit(code);
		s.writeH(0x00);
		return s;
	}
	
	private S_SocialAction(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_SOCIAL_ACTION_NOTI);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_SocialAction";
	}
}
