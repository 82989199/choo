package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_IvenBuffIcon extends ServerBasePacket {

	public static final int SHOW_INVEN_BUFFICON = 110;

	public S_IvenBuffIcon(int skillId, boolean on, int msgNum, int time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);
		
		writeC(0x08);
		writeBit(on ? 2 : 3); // type on : off
		
		writeC(0x10);
		writeBit(skillId); // 스킬아이디
		
		writeC(0x18);
		writeBit(time); // 시간
		
		writeC(0x20);
		/**
		 * 0,4,6,8,13 : 남은시간 초로 표기 1 : 남은축복지수 2 : 분으로 표기 3,5 : 반응없음 7 : 일 시간 분
		 * 으로 표기 9 : 초로 표기 설명텍스트와 빈칸이 없음 10 : 무제한표기 11 : 팅김 12 : dummy
		 */
		int duration_show_type = 8;
		if(skillId >= 4075 && skillId <= 4094)
			duration_show_type = 9;
		writeBit(duration_show_type); // duration_show_type
		
		writeC(0x28);
		
		int iven_icon = skillId;
		if(skillId >= 4075 && skillId <= 4094)
			iven_icon = 6679;
		writeBit(iven_icon); // 시작시 표현될 인벤이미지번호
		
		writeC(0x30);
		writeBit(0); // 종료시 표현될 인벤이미지번호
		
		writeC(0x38);
		/**
		 * 엔샵,보안류 : 0 or 1 기타 버프류 : 2 이상
		 */
		int skill_order_number = 3;
		if(skillId >= 4075 && skillId <= 4094)
			skill_order_number = 0;
		writeBit(skill_order_number); // icon_priority (우선순위)
		
		writeC(0x40);
		writeBit(msgNum); // 스트링 번호(아이콘 안에 내용)
		
		writeC(0x48);
		writeBit(0); // 버프시작시 채팅메세지
		
		writeC(0x50);
		writeBit(0); // 버프종료시 채팅메세지
		
		writeC(0x58);
		writeBit(0x01); // is_good (버프 / 디버프 구분)
		
		writeC(0x60);
		int overlap_buff_icon = 0;
		if(skillId >= 4075 && skillId <= 4094)
			overlap_buff_icon = 1;
		writeBit(overlap_buff_icon); // overlap_buff_icon
		
		writeC(0x68);
		int main_tooltip_str_id = 0;
		if(skillId >= 4075 && skillId <= 4094)
			main_tooltip_str_id = 4328;
		writeBit(main_tooltip_str_id); // main_tooltip_str_id
		
		writeC(0x70);
		int buff_icon_priority = 0;
		if(skillId >= 4075 && skillId <= 4094)
			buff_icon_priority = 1;
		writeBit(buff_icon_priority); // buff_icon_priority

		writeH(0x00);
	}

	/**
	 * 순간이동 지배반지 사용
	 * @param on
	 */
	public S_IvenBuffIcon(boolean on) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);
		writeC(0x08);
		writeBit(on ? 2 : 3); // type on : off
		writeC(0x10);
		writeBit(8463); // 스킬아이디
		if (on) {
			writeC(0x18);
			writeBit(60); // 시간
			writeC(0x20);
			/**
			 * 0,4,6,8,13 : 남은시간 초로 표기 1 : 남은축복지수 2 : 분으로 표기 3,5 : 반응없음 7 : 일 시간
			 * 분 으로 표기 9 : 초로 표기 설명텍스트와 빈칸이 없음 10 : 무제한표기 11 : 팅김 12 : dummy
			 */
			writeBit(10); // duration_show_type
			writeC(0x28);
			writeBit(8463); // 시작시 표현될 인벤이미지번호
			writeC(0x30);
			writeBit(0); // 종료시 표현될 인벤이미지번호
			writeC(0x38);
			/**
			 * 엔샵,보안류 : 0 or 1 기타 버프류 : 2 이상
			 */
			writeBit(3); // icon_priority (우선순위)
			writeC(0x40);
			writeBit(5119); // 스트링 번호(아이콘 안에 내용)
			writeC(0x48);
			writeBit(0); // 버프시작시 채팅메세지
			writeC(0x50);
			writeBit(0); // 버프종료시 채팅메세지
			writeC(0x58);
			writeBit(0x01); // is_good (버프 / 디버프 구분)
			writeC(0x60);
			writeBit(0x00); // overlap_buff_icon
			writeC(0x68);
			writeBit(0x00); // main_tooltip_str_id
			writeC(0x70);
			writeBit(0x00); // buff_icon_priority
		} else {
			writeC(0x30);
			writeBit(0); // 종료시 표현될 인벤이미지번호
			writeC(0x50);
			writeBit(0); // 버프종료시 채팅메세지
		}
		writeH(0x00);
	}

	public byte[] getContent() {
		return getBytes();
	}
}
