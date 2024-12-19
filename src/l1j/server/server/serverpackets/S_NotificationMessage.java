package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.server.Opcodes;

public class S_NotificationMessage extends ServerBasePacket{
	private static final int SC_NOTIFICATION_MESSAGE = 0x013C;
	
	public static final int DISPLAY_POSITION_TOP = 1;
	public static final int DISPLAY_POSITION_MIDDLE = 2;
	public static final int DISPLAY_POSITION_BOTTOM = 3;
	
	public static S_NotificationMessage getGmMessage(String message){
		return get(DISPLAY_POSITION_TOP, message, new MJSimpleRgb(0, 255, 255), 60); //운영자 $ 공지사항
	}
	
	public static S_NotificationMessage get(String message, MJSimpleRgb rgb){
		return get(DISPLAY_POSITION_MIDDLE, message, rgb, 60);
	}
	
	public static S_NotificationMessage get(String message, MJSimpleRgb rgb, int duration_second){
		return get(DISPLAY_POSITION_MIDDLE, message, rgb, duration_second);
	}
	
	public static S_NotificationMessage get(int display_position, String message, MJSimpleRgb rgb, int duration_second){
		S_NotificationMessage p = new S_NotificationMessage(message.length() + 32);
		p.writeC(0x08);
		p.writeC(display_position);
		p.writeC(0x12);
		p.writeS2(message);
		p.writeC(0x1A);
		try {
			p.writeBytes(rgb.get_bytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.writeBit(0x20);				// duration	: 지속시간
		p.writeBit(duration_second);
		p.writeH(0x00);
		return p;
	}
	
	
	private S_NotificationMessage(int capacity){
		super(capacity);
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_NOTIFICATION_MESSAGE);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
