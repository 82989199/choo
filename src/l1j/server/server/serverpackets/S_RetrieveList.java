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

import java.io.IOException;
import java.sql.Connection;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;

public class S_RetrieveList extends ServerBasePacket {
	public boolean NonValue = false;
	public S_RetrieveList(int objid, L1PcInstance pc) {
		if (pc.getInventory().getSize() < 180) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());				
			int size = warehouse.getSize();
			if (size > 0) {
				writeC(Opcodes.S_RETRIEVE_LIST);
				writeD(objid);
				writeH(size);
				writeC(3); // 개인 창고
				L1ItemInstance item = null;
				for (Object itemObject : warehouse.getItems()) {
					item = (L1ItemInstance) itemObject;
					writeD(item.getId());
					writeC(item.getItem().getType2());
					writeH(item.get_gfxid());
					writeC(item.getItem().getBless());
					writeD(item.getCount());
					writeC(item.isIdentified() ? 1 : 0);
					writeS(item.getViewName());
					writeC(0);
				}
				writeD(100);//찾는다 수수료
				writeD(0x00000000);
				writeH(0x00);
			} else {
				this.NonValue = true;
			}
		} else {
			//한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return;
		}
	}
	
	// 인벤아이콘 보기용
		public S_RetrieveList(int objid, L1PcInstance pc, int start, int count) {
			writeC(Opcodes.S_RETRIEVE_LIST);
			writeD(objid);
			writeH(count);
			writeC(3);
			L1ItemInstance item = ItemTable.getInstance().createItem(40005);
			for (int i = 0; i < count; i++) {
				item.getItem().setGfxId(start + i);
				item.getItem().setName(String.valueOf(start + i));
				item.getItem().setNameId(String.valueOf(start + i));
				
				writeD(item.getId());
				writeC(item.getItem().getType2());
				writeH(item.get_gfxid());
				writeC(1);
				writeD(start + i);
				writeC(item.isIdentified() ? 1 : 0);
				writeS(item.getViewName());
				writeC(0);
			}
			writeD(100);
			writeD(0x00000000);
			writeH(0x00);
		}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}
