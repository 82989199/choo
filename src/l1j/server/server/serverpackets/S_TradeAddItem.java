/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_TradeAddItem extends ServerBasePacket {
	private static final String S_TRADE_ADD_ITEM = "[S] S_TradeAddItem";
 
//			========================================================
//			[Server]  OPCODE :145   Size :11    
//			========================================================
//			0000: 91 00 05 00 24 34 00 03 00 07 00                   ....$4.....
//
//
//			========================================================
//			[Server]  OPCODE :98   Size :5    
//			========================================================
//			0000: 62 8B 70 8F 09                                     b.p..
//

	public S_TradeAddItem(L1ItemInstance item, int count, int type) {
		writeC(Opcodes.S_ADD_XCHG);
		writeC(type); // 0:트레이드 윈도우 상단 1:트레이드 윈도우 하단
		writeH(item.getItem().getGfxId());
		writeS(item.getNumberedViewName(count));

		// 0:축복 1:통상 2:저주 3:미감정
		if (!item.isIdentified()) { // 미감정
			//writeC(3);
			writeH(0x03);
		} else { // 교환이 끝난 상태
			byte[] status = null;
			int bless = item.getBless();
			writeC(bless);
			status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
	//	writeC(28);
		writeH(0x07);
	}

	public S_TradeAddItem(L1ItemInstance item, String name, int count, int type) {
		writeC(Opcodes.S_ADD_XCHG);
		writeC(type); // 0:트레이드 윈도우 상단 1:트레이드 윈도우 하단
		writeH(item.getItem().getGfxId());
		writeS(name);

		// 0:축복 1:통상 2:저주 3:미감정
		if (!item.isIdentified()) { // 미확인
			//writeC(3);
			writeH(0x03);
		} else { // 교환이 끝난 상태
			byte[] status = null;
			int bless = item.getBless();
			writeC(bless);
			status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
		//writeC(28);
		writeH(0x07);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_TRADE_ADD_ITEM;
	}
}
