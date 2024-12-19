package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ShowCmd extends ServerBasePacket{
	private static final int SC_DEVELSERVER_MESSAGE_NOTI 		= 0x47;
	private static final int SC_SIEGE_OBJECT_PUT_NOTI 			= 0x41; 
	private static final int SC_SIEGE_ZONE_UPDATE_NOTI 			= 0x42;
	private static final int SC_PK_MESSAGE_AT_BATTLE_SERVER 	= 0x247; 
	private static final int SC_PLAY_MOVIE_NOTI 				= 0x245;
	private static final int SC_DEATH_PENALTY_STATUS 			= 0x1CF;
	private static final int SC_FATIGUE_NOTI 					= 0x14E;
	private static final int SC_DAMAGE_OF_TIME_NOTI 			= 0x193;
	private static final int SC_BASECAMP_CHART_NOTI_PACKET 		= 0x007F;
	private static final int SC_DIALOGUE_MESSAGE_NOTI 			= 0x0244;
	
	public static S_ShowCmd getQuestDesc(int surf, int msgid){
		S_ShowCmd s = new S_ShowCmd(SC_DIALOGUE_MESSAGE_NOTI);
		s.writeC(0x0A);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeBit(surf);	// image
		bos.writeC(0x10);
		bos.writeBit(msgid);	// msg
		bos.writeC(0x1A);
		bos.writeS2("music1011");
		bos.writeC(0x20);
		bos.writeBit(5000);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		s.writeBit(data.length);
		s.writeByte(data);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getChart(){
		S_ShowCmd s = new S_ShowCmd(SC_BASECAMP_CHART_NOTI_PACKET);
		s.writeC(0x0A);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x0A);
		bos.writeS2("test1");
		bos.writeC(0x10);
		bos.writeC(5);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		s.writeBit(data.length);
		s.writeByte(data);
		
		s.writeC(0x10);
		s.writeBit(75);
		s.writeC(0x18);
		s.writeBit(0x01);
		s.writeH(0x00);
		
		return s;
	}
	
	public static S_ShowCmd getDamageOfTimeNoti(L1Character c){
		S_ShowCmd s = new S_ShowCmd(SC_DAMAGE_OF_TIME_NOTI);
		s.writeC(0x08);
		s.writeBit(c.getId());
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getFatigueNoti(){
		S_ShowCmd s = new S_ShowCmd(SC_FATIGUE_NOTI);
		s.writeC(0x08);
		s.writeC(0x01);
		s.writeC(0x10);
		s.writeC(0x01);
		s.writeC(0x18);
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getPkMessageAtBattleServer(String s1, String s2){
		S_ShowCmd s = new S_ShowCmd(SC_PK_MESSAGE_AT_BATTLE_SERVER);
		s.writeC(0x08);
		s.writeC(0x00);
		s.writeC(0x12);
		s.writeS2(s1);
		s.writeC(0x18);
		s.writeC(0x00);
		s.writeC(0x22);
		s.writeS2(s2);
		s.writeC(0x28);
		s.writeC(0x01);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getDeathPenalty(boolean isPenalty){
		S_ShowCmd s = new S_ShowCmd(SC_DEATH_PENALTY_STATUS);
		s.writeC(0x08);
		s.writeC(0x00);
		s.writeC(0x10);
		s.writeB(!isPenalty);
		s.writeC(0x18);
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getPlayMovieNoti(String url, int sec){
		S_ShowCmd s = new S_ShowCmd(SC_PLAY_MOVIE_NOTI);
		s.writeC(0x0A);
		s.writeS2(url);		// url
		s.writeC(0x10);
		s.writeBit(-1);	// sec인데 -1
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getSiegeZoneUpdateNoti(boolean ison){
		S_ShowCmd s = new S_ShowCmd(SC_SIEGE_ZONE_UPDATE_NOTI);
		s.writeC(0x08);
		s.writeC(ison ? 0x01 : 0x02);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getSiegeObjectPutNoti(L1Character c, int ico){
		S_ShowCmd s = new S_ShowCmd(SC_SIEGE_OBJECT_PUT_NOTI);
		s.writeC(0x08);
		s.writeBit(c.getId());
		s.writeBit(0x10);
		s.writeC(ico);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd get(String st){
		S_ShowCmd s = new S_ShowCmd(SC_DEVELSERVER_MESSAGE_NOTI);
		s.writeC(0x08);
		s.writeC(0x01);
		s.writeC(0x12);
		s.writeS2(st);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getProto8(int n){
		S_ShowCmd s = new S_ShowCmd(n);
		s.writeC(0x08);
		s.writeC(0x01);
		s.writeC(0x10);
		s.writeC(0x00);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ShowCmd getProtoA(int n){
		S_ShowCmd s = new S_ShowCmd(n);
		s.writeC(0x0A);
		s.writeC(0x00);
		return s;
	}
	
	private S_ShowCmd(int i){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(i);
		
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
