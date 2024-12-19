package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_RetrievePackageList extends ServerBasePacket {
	public boolean NonValue = false;
	public S_RetrievePackageList(int objid, L1PcInstance pc) {
		if (pc.getInventory().getSize() < 180) {
			int size = pc.getDwarfForPackageInventory().getSize();
			if (size > 0) {
				writeC(Opcodes.S_RETRIEVE_LIST);
				writeD(objid);
				writeH(size);
				writeC(15); // 6 : 무반응 7 : 팅 8 : 요정창고 맡기기 9: 요정찾기 15:패키지상점
				L1ItemInstance item = null;
				for (Object itemObject : pc.getDwarfForPackageInventory().getItems()) {
					item = (L1ItemInstance) itemObject;
					writeD(item.getId());
					writeC(item.getItem().getType2());
					writeH(item.get_gfxid());
					writeC(item.getItem().getBless());
					writeD(item.getCount());
					writeC(item.isIdentified() ? 1 : 0);
					writeS(item.getViewName());
				}
				writeC(0x1E);//30원
			} else {
				this.NonValue = true;
			}
		} else {
			pc.sendPackets(new S_ServerMessage(263)); // \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
		}
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}
