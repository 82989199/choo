package l1j.server.server.serverpackets;

import java.io.UnsupportedEncodingException;

import MJShiftObject.Battle.Thebe.MJThebeCharacterInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

public class S_NewChat extends ServerBasePacket {

	private static final String S_NewChat = "[S] S_NewChat";

	public S_NewChat(L1PcInstance pc, int type, int chat_type, String chat_text, String target_name) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);

		switch (type) {
		case 3:
			writeC(0x03);
			break;
		case 4:
			writeC(0x04);
			break;
		}

		writeC(0x02);
		writeC(0x08);
		writeC(0x00);
		writeC(0x10);
		writeC(chat_type);

		writeC(0x1a);
		byte[] text_byte = chat_text.getBytes();
		writeC(text_byte.length);
		writeByte(text_byte);

		switch (type) {
		case 3:
			 writeC(0x22);

			if (chat_type == 0) {
				writeC(0x00);
				writeC(0x28);
				writeC(0x00);
				writeC(0x30);
				writeC(0x18);
				writeC(0x38);
				writeC(0x00);
			} else if (chat_type == 1) {
				byte[] name_byte = target_name.getBytes();
				writeC(name_byte.length);
				writeByte(name_byte);
				writeC(0x28);
				writeC(0x00);
				writeC(0x30);
				writeC(0x00);
				writeH(0);
			}
			break;
		case 4:
			writeC(0x2a);
			try {
				byte[] name = null;
				if (pc.isGm() && chat_type == 3) {
					name = "******".getBytes("MS949");
				} else if(pc.is_shift_battle()){
					String server_description = pc.get_server_description();
					MJThebeCharacterInfo cInfo = pc.get_thebe_info();
					if(MJCommons.isNullOrEmpty(server_description) || cInfo == null){
						name = "미지인".getBytes("MS949");
					}else{
						name = cInfo.to_name_pair().getBytes("MS949");
					}
				} else if (pc.getAge() != 0 && chat_type == 4) {
					String names = pc.getName() + "(" + pc.getAge() + ")";
					name = names.getBytes("MS949");
				} else {
					name = pc.getName().getBytes("MS949");
				}
				writeC(name.length);
				writeByte(name);
			} catch (UnsupportedEncodingException e) {
			}
			if (chat_type == 0) {
				writeC(0x38);
				writeK(pc.getId());
				writeC(0x40);
				writeK(pc.getX());
				writeC(0x48);
				writeK(pc.getY());
			}
			int step = pc.getRankLevel();
			if (step != 0) {
				writeC(0x50);
				writeC(step);
			}
//			if(chat_type == 3 && pc.isGm()){//채팅창 아이콘
//				writeC(0x60);
//				writeC(0x01);
//			}
			writeH(0);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_NewChat;
	}
}
