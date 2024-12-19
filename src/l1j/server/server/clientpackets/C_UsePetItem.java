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

import java.util.List;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_UsePetItem extends ClientBasePacket {

	private static final String C_USE_PET_ITEM = "[C] C_UsePetItem";

	public C_UsePetItem(byte abyte0[], GameClient clientthread)
			throws Exception {
		super(abyte0);

		int data = readC();
		int petId = readD();
		int listNo = readC();
		L1PcInstance pc = clientthread.getActiveChar();
		if ( pc == null)return;
		L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(petId);
		if (pet == null)return;

		
		List<L1ItemInstance> items = pet.getInventory().getItems();
		if(items == null)
			return;
		
		int size = items.size();
		if(size <= 0 || listNo >= size)
			return;
		
		L1ItemInstance item = items.get(listNo);
		if (item == null) {
			return;
		}

		if (item.getItem().getType2() == 0 // 종별：그 외의 아이템
				&& item.getItem().getType() == 11) { // petitem
			int itemId = item.getItem().getItemId();
			if (itemId >= 40749 && itemId <= 40752
					|| itemId >= 40756 && itemId <= 40758) {
				pet.usePetWeapon(pc, pet, item);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SOMETHING2, data, pet.getId(), pet.getAC().getAc()));
			} else if (itemId >= 40761 && itemId <= 40766) {
				pet.usePetArmor(pc, pet, item);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SOMETHING2, data, pet.getId(), pet.getAC().getAc()));
			} else {
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
			}
		} else {
			pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
		}
	}

	@Override
	public String getType() {
		return C_USE_PET_ITEM;
	}
}
