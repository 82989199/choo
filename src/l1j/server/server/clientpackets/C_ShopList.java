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

package l1j.server.server.clientpackets;

import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI.ePersonalShopType;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PrivateShop;
import l1j.server.server.serverpackets.S_PrivateShopforNpc;

public class C_ShopList extends ClientBasePacket {

	private static final String C_SHOP_LIST = "[C] C_ShopList";

	public C_ShopList(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		int type = readC();
		int objectId = readD();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}

		L1Object shopPc = L1World.getInstance().findObject(objectId);

		if (shopPc instanceof L1PcInstance) {

			L1PcInstance cha = (L1PcInstance) shopPc;

			if (pc.getAccountName().equalsIgnoreCase(cha.getAccountName())) {
				pc.sendPackets("한 계정 내 무인상점은 이용할 수 없습니다.");
				return;
			}
			SC_PERSONAL_SHOP_ITEM_LIST_NOTI.do_send(pc, objectId, ePersonalShopType.fromInt(type));
		} else if (shopPc instanceof L1NpcShopInstance) {
			SC_PERSONAL_SHOP_ITEM_LIST_NOTI.do_send_for_npc(pc, objectId, ePersonalShopType.fromInt(type));
		} else {
			if(shopPc != null && shopPc.instanceOf(MJL1Type.L1TYPE_CLANJOIN)){
				pc.sendPackets("혈맹 가입을 원하면 칼질해주세요.");
			}else{
				pc.sendPackets("상점 오브젝트가 발견되지 않았습니다");
			}
			return;
		}
	}

	@Override
	public String getType() {
		return C_SHOP_LIST;
	}

}
