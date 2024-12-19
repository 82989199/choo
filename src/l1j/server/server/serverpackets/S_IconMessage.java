package l1j.server.server.serverpackets;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_IconMessage extends ServerBasePacket {
	private static final int SC_NOTIFICATION_MESSAGE_NOTI 		= 0x40; 
	private static final int SC_NOTIFICATION_STRINGKINDEX_NOTI	= 0x67;
	
	public static S_IconMessage getGmMessage(String msg){
		return getMessage(msg, new MJSimpleRgb(249, 68, 253), 28, 60); //운영자 $ 공지사항 28 /37 / 38 / 68 / 69 / 71~76
	}
	
	public static S_IconMessage getMessage(String msg, MJSimpleRgb rgb, int surfNum, int duration){
		S_IconMessage s = new S_IconMessage(SC_NOTIFICATION_MESSAGE_NOTI);
		s.writeBit(0x08);				// surf		: 서프번호..
		s.writeBit(surfNum * 2);
		s.writeBit(0x12);				// message	: 메시지
		s.writeS2(msg);
		s.writeBit(0x1A);				// color	: 색상
		try {
			s.writeBytes(rgb.get_bytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.writeBit(0x20);				// duration	: 지속시간
		s.writeBit(duration);
		s.writeH(0x00);
		return s;
	}
	
	public static S_IconMessage getMessage(int scode, MJSimpleRgb rgb, int surfNum, int duration){
		S_IconMessage s = new S_IconMessage(SC_NOTIFICATION_STRINGKINDEX_NOTI);
		s.writeC(0x08);
		s.writeBit(surfNum * 2);
		s.writeBit(0x10);
		s.writeBit(scode * 2);
		s.writeC(0x1A);
		try {
			s.writeBytes(rgb.get_bytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.writeC(0x20);
		s.writeBit(duration);
		s.writeH(0x00);
		return s;
	}
	
	private S_IconMessage(int i){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(i);
	}
	
	public S_IconMessage(L1PcInstance gm, String p){
		int SC_UPDATE_INVENTORY_NOTI = 0x024D;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_UPDATE_INVENTORY_NOTI);
		writeC(0x0A);
		L1ItemInstance item = gm.getInventory().findItemObjId(269226741);
		item.setAttrEnchantLevel(3);
		byte[] data = item.serialize();
		writeBit(data.length);
		writeByte(data);
		writeH(0x00);
	}
	
	public S_IconMessage(boolean b){
		int SC_BLOODPLEDGE_USER_INFO_NOTI = 0x219;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_BLOODPLEDGE_USER_INFO_NOTI);
		try {
			byte[] bytes = "프리".getBytes("MS949");
			writeC(0x0A);
			writeBit(bytes.length);
			writeBytes(bytes);
			writeC(0x10);
			writeC(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeH(0x00);
		/*int SC_PING_REQ = 0x03E8;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_PING_REQ);
		writeC(0x08);
		writeC(0x01);
		writeH(0x00);*/
		/*int SC_ADD_INVENTORY_NOTI = 0x024C;
		//int SC_ADD_INVENTORY_NOTI = 0x024D;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ADD_INVENTORY_NOTI);
		
		writeC(0x0A);
		L1ItemInstance item = ItemTable.getInstance().createItem(40314);
		item.setIdentified(true);
		//item.setBless(0);
		byte[] data = item.serialize();
		writeBit(data.length);
		writeByte(data);
		writeC(0x10);
		writeC(0x00);
		writeH(0x00);
		*/
		/*int SC_ADD_INVENTORY_NOTI = 0x024C;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ADD_INVENTORY_NOTI);
		writeC(0x0A);
		L1ItemInstance item = ItemTable.getInstance().createItem(816);
		System.out.println(item.getId());
		item.setIdentified(true);
		item.setEnchantLevel(5);
		item.setCount(100);
		item.setBless(0);
		byte[] data = item.serialize();
		writeBit(data.length);
		writeByte(data);
		writeC(0x10);
		writeBit(268464458L);
		writeC(0x18);
		writeC(0x00);
		writeC(0x20);
		writeC(0x00);
		writeH(0x00);*/
		
		//int 
		
		/*int SC_MESSAGE_BOX = 0x0333;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_MESSAGE_BOX);
		writeC(0x08);
		writeBit(0x02);
		writeH(0x00);
		*/
		/*int SC_ADD_INVENTORY_NOTI = 0x0326;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_BUFF_NOTI);
		writeC(0x08);
		writeC(0x02);
		writeC(0x12);
		writeC(0x03);
		writeC(0x08);
		writeBit(1800);
		writeH(0x00);
		*/
		
		/*int SC_ARENA_PLAY_EVENT_NOTI = 0x02DF;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARENA_PLAY_EVENT_NOTI);
		writeC(0x08);
		writeC(0xD);
		writeH(0x00);
		*/
		/*
		int SC_ARENA_GAME_INFO_NOTI = 0x02DD;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARENA_GAME_INFO_NOTI);
		
		writeC(0x10);
		writeBit(1);
		writeC(0x18);
		writeBit(-1);
		writeC(0x20);
		writeBit(0);
		writeC(0x28);
		writeBit(0);
		writeH(0x00);*/
		
		
		/*int SC_ARENA_GAME_INFO_NOTI = 0x02DD;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARENA_GAME_INFO_NOTI);
		
		writeC(0x0A);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(0x00);
		bos.writeC(0x10);
		bos.writeC(0x01);
		bos.writeC(0x18);
		bos.writeC(0x01);
		bos.writeC(0x22);
		bos.writeS2("test");
		bos.writeC(0x28);
		bos.writeC(0x01);
		bos.writeC(0x30);
		bos.writeC(0x01);
		bos.writeC(0x38);
		bos.writeC(0x01);
		bos.writeC(0x40);
		bos.writeC(0x01);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x10);
		writeC(0x01);
		writeC(0x18);
		writeC(0x01);
		writeC(0x20);
		writeBit(10);
		writeC(0x28);
		writeBit(36);
		writeH(0x00);*/
		/*writeC(0x12);
		bos = new BinaryOutputStream();
		
		data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		*/
		/*
		int SC_ARENACO_BYPASS_CHANGE_READY_ENTER_NOTI = 0x02D5;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARENACO_BYPASS_CHANGE_READY_ENTER_NOTI);
		writeC(0x08);
		writeC(0x01);
		writeC(0x10);
		writeC(0x01);
		writeC(0x18);
		writeC(0x01);
		writeH(0x00);
		*/
		
		/*int SC_ARENACO_SERVER_STATUS_NOTI = 0x02BC;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARENACO_SERVER_STATUS_NOTI);
		writeC(0x08);
		writeC(0x02);
		writeH(0x00);
		*/
		/*int SC_DIALOGUE_MESSAGE_NOTI = 0x0244;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_DIALOGUE_MESSAGE_NOTI);
		writeC(0x0A);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeBit(3511);
		bos.writeC(0x10);
		bos.writeBit(1468);
		bos.writeC(0x1A);
		bos.writeS2("music1011");
		bos.writeC(0x20);
		bos.writeBit(5000);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x0A);
		bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeBit(3511);
		bos.writeC(0x10);
		bos.writeBit(1467);
		bos.writeC(0x1A);
		bos.writeS2("music1012");
		bos.writeC(0x20);
		bos.writeBit(5000);
		data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		writeH(0x00);*/
		
		
		/*int SC_BOX_ATTR_CHANGE_NOTI_PACKET = 0x0081;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_BOX_ATTR_CHANGE_NOTI_PACKET);
		writeC(0x08);
		writeC(0x00);
		writeC(0x12);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeBit(10);
		bos.writeC(0x10);
		bos.writeBit(10);
		bos.writeC(0x18);
		bos.writeBit(10);
		bos.writeC(0x20);
		bos.writeBit(10);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x18);
		writeBit(0x05);
		writeH(0x00);*/
		
		/*int SC_BASECAMP_POINTRANK_NOTI = 0x0085;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_BASECAMP_POINTRANK_NOTI);
		
		writeC(0x0A);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(0x01);
		bos.writeC(0x12);
		bos.writeS2("test");
		bos.writeC(0x18);
		bos.writeBit(60);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x12);
		bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(99);
		bos.writeC(0x12);
		bos.writeS2("test2");
		bos.writeC(0x18);
		bos.writeBit(60);
		data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x18);
		writeBit(60);
		
		writeH(0x00);*/
		/*int SC_BASECAMP_CHART_NOTI_PACKET = 0x007F;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_BASECAMP_CHART_NOTI_PACKET);
	
		writeC(0x0A);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x0A);
		bos.writeS2("test1");
		bos.writeC(0x10);
		bos.writeC(5);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x10);
		writeBit(75);
		writeC(0x18);
		writeBit(0x01);
		writeH(0x00);*/
		
		/*int SC_THEBE_CAPTURE_INFO_NOTI = 0x0086;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_THEBE_CAPTURE_INFO_NOTI);
		
		writeC(0x0A);
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(0x00);
		bos.writeC(0x10);
		bos.writeBit(100);
		bos.writeC(0x18);
		bos.writeC(0x00);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x0A);
		bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(0x01);
		bos.writeC(0x10);
		bos.writeBit(90);
		bos.writeC(0x18);
		bos.writeC(0x01);
		data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x0A);
		bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(0x02);
		bos.writeC(0x10);
		bos.writeBit(80);
		bos.writeC(0x18);
		bos.writeC(0x02);
		data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		
		writeC(0x10);
		writeBit(100);
		writeH(0x00);*/
		/*bos.writeC(0x32);
		BinaryOutputStream bos2 = new BinaryOutputStream();
		bos2.write
		
		bos.writeC(0x10);
		bos.writeC(0x04);
		bos.writeC(0x18);
		bos.writeBit(32767);
		bos.writeC(0x22);
		bos.writeBit("127.0.0.1".length());
		bos.writeS("127.0.0.1");
		bos.writeBit(0x7F000001);*/
		
		// skill창 on off
		/*int SC_AVAILABLE_SPELL_CHANGE_NOTI = 0x024A;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_AVAILABLE_SPELL_CHANGE_NOTI);
		writeC(0x08);
		writeC(0x01); // on(1), off(2)
		writeC(0x12);
		writeC(0x02); 	// 몇단계까지 인지, 0x02 = 2단계까지
		writeC(255);	// 1단계 마법칸 2진수로 표현 
		writeC(255);	// 2단계 마법칸 2진수로 표현
		writeH(0x00);*/
		
		/*int  SC_EVENT_COUNTDOWN_NOTI = 0x021C;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_EVENT_COUNTDOWN_NOTI);
		writeC(0x08);
		writeBit(10);
		writeC(0x12);
		writeS2("서버");
		writeH(0x00);
		*/
		/*int SC_PLAY_TIME_LIMIT_NOTI = 0x141;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_PLAY_TIME_LIMIT_NOTI);
		writeC(0x08);
		writeBit(System.currentTimeMillis() / 1000);
		writeC(0x10);
		writeBit(System.currentTimeMillis() / 1000);
		writeH(0x00);*/
		
		/*int SC_ARCA_ACTIVATION_INFO_ACK = 0x01CD;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARCA_ACTIVATION_INFO_ACK);
		writeC(0x0A);
		
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(0x01);
		bos.writeC(0x10);
		bos.writeBit(1);
		bos.writeC(0x18);
		bos.writeBit(1000);
		bos.writeC(0x20);
		bos.writeC(0x01);
		bos.writeC(0x2A);
		bos.writeC("메티스".length());
		bos.writeS("메티스");
		bos.writeC(0x30);
		bos.writeBit(80);
		bos.writeC(0x38);
		bos.writeC(0x02);
		bos.writeC(0x40);
		bos.writeC(0x01);
		
		try{
			bos.close();
		}catch(Exception e){}
		byte[] data = bos.getBytes();
		writeBit(data.length);
		writeByte(data);
		writeC(0x10);
		writeC(0x10);
		writeC(0x18);
		writeC(0x02);
		writeC(0x20);
		writeBit(100);
		writeH(0x00);*/
		/*
		int SC_ARENACO_SERVER_STATUS_NOTI = 0x02BC;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARENACO_SERVER_STATUS_NOTI);
		writeC(0x08);
		writeC(0x02);
		writeH(0x00);
		*/
		
		/*
		int SC_ARENACO_BYPASS_ROOM_CHAT_NOTI = 0x02D9;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_ARENACO_BYPASS_ROOM_CHAT_NOTI);
		writeC(0x08);
		writeBit(0x00);
		writeC(0x10);
		writeBit(268464388L);
		writeC(0x1A);
		writeS2("zzzz");
		writeC(0x22);
		writeS2("메티스");
		writeC(0x28);
		writeBit(0x00);
		writeH(0x00);*/
		
		/*int SC_CRITERIA_UPDATE_NOTI = 0x237;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_CRITERIA_UPDATE_NOTI);
		writeC(0x08);
		writeC(0x01);
		writeC(0x10);
		writeC(0x01);
		writeH(0x00);*/
		
		/*int SC_BOX_ATTR_CHANGE_NOTI_PACKET = 0x81;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_BOX_ATTR_CHANGE_NOTI_PACKET);
		writeC(0x08);
		writeC(0x04);
		writeC(0x10);
		writeBit(12);
		
		writeC(0x12);
		writeC(0x08);
		writeC(0x08);
		writeC(0x00);
		writeC(0x10);
		writeC(0x00);
		writeC(0x18);
		writeC(0x10);
		writeC(0x20);
		writeC(0x10);
		writeC(0x18);
		writeC(0x00);
		writeH(0x00);*/
		
		/*int SC_SIEGE_REDBLOOD_LOTTO_NOTI = 0x6F;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_SIEGE_REDBLOOD_LOTTO_NOTI);
		writeC(0x08);
		writeBit(6298);
		writeC(0x10);
		writeBit(3751);
		writeC(0x1A);
		writeD(new MJSimpleRgb(0xff, 0, 0).serialize());
		writeC(0x20);
		writeBit(10);
		writeC(0x2A);
		writeS2("test");
		writeC(0x30);
		writeC(10);
		writeH(0x00);*/
		
		/*int SC_BASECAMP_CHART_NOTI_PACKET = 0x7F;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_BASECAMP_CHART_NOTI_PACKET);
		writeC(0x0A);
		
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x0A);
		bos.writeBit("test".length());
		bos.writeS("test");
		bos.writeC(0x10);
		bos.writeBit(100);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		writeC(0x10);
		writeBit(1000);
		writeC(0x18);
		writeC(0x01);
		writeH(0x00);*/
		
		/*
		int SC_THEBE_CAPTURE_INFO_NOTI = 0x86;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_THEBE_CAPTURE_INFO_NOTI);
		writeC(0x0A);
		
		BinaryOutputStream bos = new BinaryOutputStream();
		bos.writeC(0x08);
		bos.writeC(0x01);
		bos.writeC(0x10);
		bos.writeC(0x01);
		bos.writeC(0x18);
		bos.writeC(0x00);
		byte[] data = bos.getBytes();
		try{
			bos.close();
		}catch(Exception e){}
		writeBit(data.length);
		writeByte(data);
		writeC(0x10);
		writeBit(1000);
		writeH(0x00);*/
		
		/*int SC_THEBE_CAPTURE_INFO_NOTI = 0x86;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_THEBE_CAPTURE_INFO_NOTI);
		writeC(0x02);
		writeC(0x08);
		writeC(0x08);
		writeC(0x01);
		writeC(0x10);
		writeC(0x02);
		writeC(0x18);
		writeBit(100);
		writeC(0x20);
		writeC(0x01);
		writeC(0x10);
		writeBit(1000);
		writeH(0x00);*/
		
		/*int SC_PLAY_MOVIE_NOTI 		= 0x245;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_PLAY_MOVIE_NOTI);
		writeC(0x0A);
		writeS2("http://blog.naver.com/mjnms");
		writeC(0x10);
		writeBit(-1);
		writeH(0x00);*/
		
		/*
		writeC(0x2A);
		writeBit(1000 * 2);
		writeC(0x32);
		writeS("ddd");
		writeH(0x00);
/*		writeC(0x28);
		writeBit(0x01);
		writeC(0x32);
		writeBit(0x01);*/
		/** 성을 소유하고 있는 혈맹 **/
		/*int SC_SIEGE_EVENT_NOTI = 0x44;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_SIEGE_EVENT_NOTI);
		writeC(0x08);
		writeC(0x01);
		writeC(0x12);
		writeBit(10);
		writeC(0x08);
		writeBit(3754 * 2);
		writeC(0x12);
		writeS2("test");
		writeH(0x00);*/
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
