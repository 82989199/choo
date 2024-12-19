package l1j.server.server.serverpackets;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.EventAlarmTable;
import l1j.server.server.datatables.EventTimeTable;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1EventAlarm;

public class S_ACTION_UI extends ServerBasePacket {
	public static final int TAM = 450; // TAM 포인트 0627
	public static final int CLAN_JOIN_MESSAGE = 323; // 혈맹 가입 메세지 0627
	public static final int TEST = 0xcc;
	public static final int TEST2 = 0x3A;
	public static final int SAFETYZONE = 0xcf; //세이프티존
	public static final int PCBANG_SET = 0x7e;
	public static final int EVENT_SYSTEM = 141;  //--[0x8D] 141
	
	public static final int EVENT_NOTICE = 142;//이벤트 알림 142
	
	public static final int MONSTER_BOOK_WEEK_QUEST = 810;
	
	/**
	 * 플레이 서포터 타입 0627
	 */
	
	public static final int START_PLAY_SUPPORT_ACK = 2102;
	public static final int FINISH_PLAY_SUPPORT_ACK = 2104;
	
	/**
	 * 플레이 서포터 결과 타입 0627
	 */
	public static final int SUPPORT_VALID = 0;
	public static final int SUPPORT_INVALID_MAP = 1;
	public static final int SUPPORT_INVALID_LEVEL = 2;
	public static final int SUPPORT_TIME_EXPIRE = 3;
	public static final int SUPPORT_UNKNOWN_ERROR = 99;
	
	public static final int LOGIN_ADD_SKILL = 401; // 패시브스킬(로그인) 0627
	public static final int ADD_SKILL = 402; // 패시브스킬 0627
	
	private static final String S_ACTION_UI = "S_ACTION_UI";

	private static final String 테스트진행 = ""
			+ "0a e3 01 0a 55 "
			+ "12 10 08 01 10 9a 83 2c 18 00 22 06 08 d8 87 01 10 01 "
			+ "12 11 08 02 10 81 88 6e 18 c6 20 22 06 08 d8 87 01 10 02 "
			+ "12 12 08 03 10 84 a0 b8 03 18 9f 78 22 06 08 d8 87 01 10 05 "
			+ "1a 0b 08 01 10 b5 bf f0 06 18 c1 87 01 "
			+ "1a 0b 08 01 10 84 a0 b8 03 18 c0 87 01 20 01 ";
	
	public S_ACTION_UI(boolean flag){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MONSTER_BOOK_WEEK_QUEST);
		StringTokenizer st = new StringTokenizer(테스트진행.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
		writeH(0x00);
	}
	
	/**
	 * 파티 표식 설정시 사용함.
	 */
	public S_ACTION_UI(byte[] flag) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(339);
		writeByte(flag);
		writeH(0);
	}  

	public S_ACTION_UI(int type, int npcid, boolean check) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);

		switch (type) {
		case EVENT_SYSTEM: {			//-- 이벤트알람 //141
			writeC(0x08);
			writeC(0x01);
			writeC(0x10);
			writeC(0x01); 

			Iterator<L1NpcInstance> iter = EventTimeTable.getInstance().get_npc_iter();
			L1NpcInstance npc = null;

			while (iter.hasNext()) {
				npc = iter.next();
				if (npc == null) {
					continue;
				}
				if (check) {
					if (npc.getNpcId() != npcid) {
						continue;
					}
				}
				String main_name = "orc", monster_name = npc.getName();

				if (npc.getNpcId() == 81111) {
					monster_name = "붉은 기사단의 진격";
				}
				int main_length = 0, monster_length = 0;
				int start_time = (int) npc.get_boss_time();
				int end_time = (int) npc.get_boss_time() + (3500);//TODO 이벤트알람에 없어지는 시간
				int totalLength = 0;
				main_length = main_name.getBytes().length;
				monster_length = monster_name.getBytes().length;

				totalLength = getBitSize(npc.get_boss_type()) + getBitSize(main_length) + main_length + getBitSize(monster_length) + monster_length + getBitSize(start_time) + getBitSize(end_time) + 16; //5는 진행중 16은 TEL
				writeC(26);
				writeBit(totalLength);
				
				writeC(8);
				writeBit(npc.get_boss_type());  //-- 1: 에르자베, 2: 샌드 웜, 3: 붉은 기사단 멘트
				
				writeC(26);
				writeBit(main_length);
				writeByte(main_name.getBytes());
				
				writeC(34);
				writeBit(monster_length);
				writeByte(monster_name.getBytes());
				
				writeC(40);
				writeBit(start_time);
				
				writeC(48);
				writeBit(end_time);

				writeC(0x3a);
				writeC(0x09);
				writeC(0x0a);
				writeC(0x04);
				writeC(0x34);
				writeC(0x36);
				writeC(0x35);
				writeC(0x34);
				writeC(0x10);
				writeC(0x90);
				writeC(0x4e);
			}
		}
			break;
			
		case EVENT_NOTICE:
			if(check == false){
				for(int i = 1; i < 30;i++){
					writeC(0x08);
					writeC(i);// 1
					writeC(0x10);
					writeC(0x01);// 1
				}
				return;
			}
			writeC(0x08);
			writeC(0x01);// 1
			writeC(0x10);
			writeC(0x01);// 1
			if(check){
				/**1: 에르자배, 2: 샌드 웜, 3: 붉은기사*/
				for(L1EventAlarm EventAlarm : EventAlarmTable.getInstance().GetEventAlarmList()){
					if(EventAlarm.getAction() == false)continue;
					writeC(0x1A);
					ByteArrayOutputStream _bao;
					_bao = new ByteArrayOutputStream();
					_bao.write(0x08 & 0xff);
					_bao.write(EventAlarm.getId() & 0xff);// 1: 에르자베, 2. 샌드웜, 3, 공성전
					_bao.write(0x1A & 0xff);
					writeS2(_bao, "http://g.lineage.power.plaync.com/wiki/");// 연결시킬 파워북 주소
					_bao.write(0x22 & 0xff);
					writeS2(_bao, EventAlarm.getName());//이벤트 이름
					long time = 0;
					Calendar startcal = Calendar.getInstance();	
					Calendar temp1_cal = Calendar.getInstance();	
					Calendar temp2_cal = Calendar.getInstance();
					Calendar temp3_cal = null;
					int whilecount = 0;
					if(EventAlarm.getDate() != null){
						temp3_cal = Calendar.getInstance();
						temp3_cal.setTimeInMillis(EventAlarm.getDate().getTime());
					}
					if(temp3_cal != null && temp3_cal.after(startcal)){
						startcal.setTimeInMillis(EventAlarm.getDate().getTime());
						time = (startcal.getTimeInMillis() / 1000);
					}else if(EventAlarm.getDay_Of_Week().size() > 0){
						if(EventAlarm.getStart_timeList().size() <= 0)continue;
						int v = startcal.get(Calendar.DAY_OF_WEEK);
						boolean Of_Week = true;;
						int Weekcount = 0;
						while(Of_Week){
							for(int a : EventAlarm.getDay_Of_Week()){
								if(v == a){
									Of_Week = false;
									break;
								}
							}
							if(Of_Week == false)break;
							if(v >= 7)v = 1;
							else v++;
							Weekcount++;
						}
						temp1_cal.set(Calendar.DATE, temp1_cal.get(Calendar.DATE) + Weekcount);	
						temp2_cal.set(Calendar.DATE, temp2_cal.get(Calendar.DATE) + Weekcount);
						do{
							for(int[] Stime : EventAlarm.getStart_timeList()){
								temp1_cal.set(Calendar.HOUR_OF_DAY, Stime[0]);
								temp1_cal.set(Calendar.MINUTE, Stime[1]);
								temp2_cal.set(Calendar.HOUR_OF_DAY, Stime[0]);
								temp2_cal.set(Calendar.MINUTE, Stime[1]);
								temp2_cal.add(Calendar.MINUTE, EventAlarm.getPeriod());
								if(temp1_cal.after(startcal)){
									startcal.set(Calendar.DATE, temp1_cal.get(Calendar.DATE));
									startcal.set(Calendar.HOUR_OF_DAY, Stime[0]);
									startcal.set(Calendar.MINUTE, Stime[1]);
									startcal.set(Calendar.SECOND, 0);
									//startcal.add(Calendar.MINUTE, -1);//1분 빼자
									time = (startcal.getTimeInMillis() / 1000);
									//continue;
									break;
								}else if(temp1_cal.before(startcal) && temp2_cal.after(startcal) ){
									startcal.set(Calendar.DATE, temp1_cal.get(Calendar.DATE));
									startcal.set(Calendar.HOUR_OF_DAY, Stime[0]);
									startcal.set(Calendar.MINUTE, Stime[1]);
									startcal.set(Calendar.SECOND, 0);
									//startcal.add(Calendar.MINUTE, -1);//1분 빼자
									time = (startcal.getTimeInMillis() / 1000);
									//continue;
									break;
								}
							}
							if(time == 0){
								temp1_cal.set(Calendar.DATE, temp1_cal.get(Calendar.DATE) + 1);	
								temp2_cal.set(Calendar.DATE, temp2_cal.get(Calendar.DATE) + 1);
							}
							
							if(whilecount++ > 5)break;
						}while(time == 0);
					}else {
						if(EventAlarm.getStart_timeList().size() <= 0)continue;
						do{
							for(int[] Stime : EventAlarm.getStart_timeList()){
								temp1_cal.set(Calendar.HOUR_OF_DAY, Stime[0]);
								temp1_cal.set(Calendar.MINUTE, Stime[1]);
								temp2_cal.set(Calendar.HOUR_OF_DAY, Stime[0]);
								temp2_cal.set(Calendar.MINUTE, Stime[1]);
								temp2_cal.add(Calendar.MINUTE, EventAlarm.getPeriod());
								if(temp1_cal.after(startcal)){
									startcal.set(Calendar.DATE, temp1_cal.get(Calendar.DATE));
									startcal.set(Calendar.HOUR_OF_DAY, Stime[0]);
									startcal.set(Calendar.MINUTE, Stime[1]);
									startcal.set(Calendar.SECOND, 0);
									//startcal.add(Calendar.MINUTE, -1);//1분 빼자
									time = (startcal.getTimeInMillis() / 1000);
									
									//continue;
									break;
								}else if(temp1_cal.before(startcal) && temp2_cal.after(startcal) ){
									startcal.set(Calendar.DATE, temp1_cal.get(Calendar.DATE));
									startcal.set(Calendar.HOUR_OF_DAY, Stime[0]);
									startcal.set(Calendar.MINUTE, Stime[1]);
									startcal.set(Calendar.SECOND, 0);
									//startcal.add(Calendar.MINUTE, -1);//1분 빼자
									time = (startcal.getTimeInMillis() / 1000);
									
									//continue;
									break;
								}
							}
							if(time == 0){
								temp1_cal.set(Calendar.DATE, temp1_cal.get(Calendar.DATE) + 1);	
								temp2_cal.set(Calendar.DATE, temp2_cal.get(Calendar.DATE) + 1);
							}
							if(whilecount++ > 5)break;
							
						}while(time == 0);
					}
					_bao.write(0x28 & 0xff);
					byteWrite(_bao, time);//이벤트 시작 시간
					_bao.write(0x30 & 0xff);
					startcal.add(Calendar.MINUTE, EventAlarm.getPeriod());
					time =  (startcal.getTimeInMillis() / 1000);
					byteWrite(_bao, time);//이벤트 종료 시간
					_bao.write(0x3A & 0xff);
					ByteArrayOutputStream _bao2 = new ByteArrayOutputStream();
					if(EventAlarm.getLoc_X() != 0 && EventAlarm.getLoc_Y() != 0){
						_bao2.write(0x0A & 0xff);
						writeS2(_bao2, "4654");// 해당 지역으로 이동하시겠습니까 (메세지id)
					}else{
						_bao2.write(0x0A & 0xff);
						writeS2(_bao2, "4654");//627
					}
					_bao2.write(0x10 & 0xff);
					byteWrite(_bao2, EventAlarm.getPrice());//이동 비용
					_bao.write(_bao2.size() & 0xff);
					writeByte(_bao, _bao2.toByteArray());
					writeC(_bao.size());
					writeByte(_bao.toByteArray());
				}
			}
			break;
			
			
		}
		writeH(0x00);
	}
 
	/**
	 * @param PC방,세이프티존 설정 by 맞고
	 * @param isOpen on/off
	 **/
	public S_ACTION_UI(int code, boolean isOpen) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(code);
		switch (code) {
		case PCBANG_SET: {
			writeC(0x00);
			writeC(0x08);
			writeC(isOpen ? 1 : 0);
			writeC(0x10);
			writeC(0x01); //랭킹버튼활성화 패킷
			//	writeH(0);
		}
		break;
		case SAFETYZONE: {
			writeC(0x01);
			writeC(0x08);
			write7B(isOpen? 128 : 0);
			writeC(0x10);
			writeC(0x00);
			writeC(0x18);
			writeC(0x00);
			//	writeH(0);
		} 
		break;
		}
		writeH(0x00);
	}
	
	/**
	 * 혈맹관련
	 */
	public S_ACTION_UI(String clanname, int rank) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x19);
		writeC(0x02);
		writeC(0x0a);
		int length = 0;
		if (clanname != null)
			length = clanname.getBytes().length;
		if (length > 0) {
			writeC(length); // 클랜명 SIZE
			writeByte(clanname.getBytes()); // 클랜명
			writeC(0x10);
			writeC(rank); // 클랜 랭크
		} else {
			writeC(0x00);
		}
		writeH(0x00);
	}

	/**
	 * 전사스킬을 위해  0627
	 */
	public S_ACTION_UI(int type, int skillnum) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch(type) {
		case FINISH_PLAY_SUPPORT_ACK:
		case START_PLAY_SUPPORT_ACK:
			writeC(0x08);
			writeBit(skillnum);
			break;
		case TAM:
			writeC(0x08);
			write4bit(skillnum);
			break;
		case CLAN_JOIN_MESSAGE:
			writeC(0x08);
			writeBit(skillnum);
			break;
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ACTION_UI;
	}
}
