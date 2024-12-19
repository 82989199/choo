package l1j.server.server.serverpackets;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Object;

public class S_ChatMessageNoti extends ServerBasePacket {
	private static final int SC_CHAT_MESSAGE_NOTI_PACKET = 0x0204;
	
	public static final int CHAT_NORMAL 		= 0x00;
	public static final int CHAT_WHISPER 		= 0x01;
	public static final int CHAT_SHOUT 			= 0x02;
	public static final int CHAT_WORLD 			= 0x03;
	public static final int CHAT_PLEDGE 		= 0x04;
	public static final int CHAT_HUNT_PARTY 	= 0x0B;
	public static final int CHAT_TRADE 			= 0x0C;
	public static final int CHAT_PLEDGE_PRINCE 	= 0x0D;
	public static final int CHAT_CHAT_PARTY 	= 0x0E;
	public static final int CHAT_PLEDGE_ALLIANCE= 0x0F;
	public static final int CHAT_PLEDGE_NOTICE 	= 0x11;
	public static final int CHAT_CLASS 			= 0x16;
	public static final int CHAT_TEAM 			= 0x1D;
	public static final int CHAT_ARENA_TEAM 	= 0x1E;
	public static final int CHAT_ARENA_OBSERVER = 0x1F;
	public static final int CHAT_ROOM_ARENA_ALL = 0x20;
	
	public static S_ChatMessageNoti getWhisper(String msg, String sender){
		S_ChatMessageNoti s = new S_ChatMessageNoti();
		s.writeC(0x08);
		s.writeBit(0x00);
		s.writeC(0x10);
		s.writeC(CHAT_WHISPER);
		s.writeC(0x1A);
		s.writeS2(msg);		
		s.writeC(0x2A);
		s.writeS2(sender);
		s.writeC(0x30);
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ChatMessageNoti getNotice(String msg, String sender){
		S_ChatMessageNoti s = new S_ChatMessageNoti();
		s.writeC(0x08);
		s.writeBit(0x00);
		s.writeC(0x10);
		s.writeC(CHAT_PLEDGE_NOTICE);
		s.writeC(0x1A);
		s.writeS2(msg);		
		s.writeC(0x2A);
		s.writeS2(sender);
		s.writeC(0x30);
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ChatMessageNoti get(int type, String msg, MJSimpleRgb rgb, String sender, L1Object obj, int rank){
		S_ChatMessageNoti s = new S_ChatMessageNoti();
		s.writeC(0x08);
		s.writeBit(0x00);
		s.writeC(0x10);
		s.writeC(type);
		s.writeC(0x1A);
		s.writeS2(msg);
		if(rgb != null){
			s.writeC(0x22);
			try {
				s.writeBytes(rgb.get_bytes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sender != null){
			s.writeC(0x2A);
			s.writeS2(sender);
		}
		s.writeC(0x30);
		s.writeC(0x01);
		if(obj != null){
			s.writeC(0x38);
			s.writeBit(obj.getId());
			s.writeC(0x40);
			s.writeBit(obj.getX());
			s.writeC(0x48);
			s.writeBit(obj.getY());
		}
		
		if(rank > 0){
			s.writeC(0x50);
			s.writeC(rank);
		}
		s.writeH(0x00);
		return s;
	}
	
	private S_ChatMessageNoti(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_CHAT_MESSAGE_NOTI_PACKET);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
