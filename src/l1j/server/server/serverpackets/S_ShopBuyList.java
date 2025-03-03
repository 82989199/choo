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

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1AssessedItem;
import l1j.server.server.model.shop.L1Shop;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SystemMessage

public class S_ShopBuyList extends ServerBasePacket {

	private static final String S_SHOP_BUY_LIST = "[S] S_ShopBuyList";

	public S_ShopBuyList(int objid, L1PcInstance pc) {
		L1Object object = L1World.getInstance().findObject(objid);
		if (!(object instanceof L1NpcInstance)) {
			throw new IllegalArgumentException(String.format("알 수 없는 오브젝트가 상점 구매목록으로 전송되었습니다.[objectid : %d, L1Object : %d]", objid, object == null ? 0 : object.getL1Type()));
		}
		L1NpcInstance npc = (L1NpcInstance) object;
		int npcId = npc.getNpcTemplate().get_npcId();
		L1Shop shop = ShopTable.getInstance().get(npcId);
		if (shop == null) {
			pc.sendPackets(new S_NoSell(npc));
			throw new IllegalArgumentException(String.format("판매 샵을 찾을 수 없습니다.[npcid : %d]", npcId));
		}

		List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());
//		if (assessedItems.isEmpty()) {
//			pc.sendPackets(new S_NoSell(npc));
//			return;
//		}
		System.out.println("상점 판매시");
		writeC(Opcodes.S_SELL_LIST);
		writeD(objid);
		writeH(assessedItems.size());

		int real_count = 0;
		for (L1AssessedItem item : assessedItems) {
			writeD(item.getTargetId());
			writeD(item.getAssessedPrice());
			++real_count;
		}
		if(real_count <= 0){
			pc.sendPackets(new S_NoSell(npc));
//			throw new IllegalArgumentException(String.format("판매 아이템 목록을 찾을 수 없습니다.[npcid : %d]", npcId));
		}
		
		 if ((npcId == 7000077)) {//행 베리
				writeC(73);
				writeC(58);
				writeC(0);
				writeC(0);
		 }else {
			 writeH(0x07);//상점판매 리스트
		 }
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SHOP_BUY_LIST;
	}
}
