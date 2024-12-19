package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SlotChange extends ServerBasePacket {	
	private static final String S_SlotChange = "S_SlotChange";

	public static final int SLOT_CHANGE = 32;
		
	public S_SlotChange(int type, L1PcInstance pc) { // 로그인시 불러옴
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch(type) {
		case SLOT_CHANGE:
			List<Integer> one = pc.getSlotItems(0);
			List<Integer> two = pc.getSlotItems(1);
			int size = 6;
			int sizecheck = one.size() > 0 ? (size*one.size())+2 : 2;
			int sizecheck2 = two.size() > 0 ? (size*two.size())+2 : 2;
			writeC(0x03);
			writeC(0x08);
			writeBit(pc.getSlotNumber());
			writeC(0x12);
			writeBit(sizecheck);
			writeC(0x08);
			writeC(0x00);
			
			// 1번 슬롯 아이템 
			if (one.size() > 0) {
				for (int i = 0; i < one.size(); i++) {
					writeC(0x10);
					writeBit(one.get(i));
				}
			}
			
			writeC(0x12);
			writeBit(sizecheck2);
			writeC(0x08);
			writeC(0x01);
			
			// 2번 슬롯아이템
			if (two.size() > 0) {
				for (int i = 0; i < two.size(); i++) {
					writeC(0x10);
					writeBit(two.get(i));
				}
			}
			
			writeC(0x18);
			writeC(0x02);
			writeC(0x20);
			writeC(0x46); // 착용제한레벨 46=70
			writeH(0);
			break;
		}
	}

	public S_SlotChange(int type, int slot) { // 변경될때 불러옴
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch(type) {
		case SLOT_CHANGE:
			writeC(0x03);
			writeC(0x08);
			writeBit(slot);
			break;
		}
		
		writeH(0x00);
	}
	

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_SlotChange;
	}
}
