
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_ItemStatus extends ServerBasePacket {

	private static final String S_ITEM_STATUS = "[S] S_ItemStatus";

	/**
	 * 아이템의 이름, 상태, 특성, 중량등의 표시를 변경한다
	 */
	public S_ItemStatus(L1ItemInstance item) {
		writeC(Opcodes.S_CHANGE_ITEM_DESC_EX);
		writeD(item.getId());
		writeS(item.getViewName());
		writeD(item.getCount());
		if (!item.isIdentified()) {	// 미감정의 경우 스테이터스를 보낼 필요는 없다
			writeC(0);
		} else {
			byte[] status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_ITEM_STATUS;
	}
}
