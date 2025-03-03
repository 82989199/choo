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

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterConfigTable;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket, C_RequestDoors

public class C_CharcterConfig extends ClientBasePacket {

	private static final String C_CHARCTER_CONFIG = "[C] C_CharcterConfig";

	public C_CharcterConfig(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		if (client == null){
			return;
		}
		if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
			L1PcInstance pc = client.getActiveChar();
			if (pc == null)return;
			//int length = readD() - 3;//슬롯창 초기화 원본
			int length = readD();//슬롯창 초기화 수정본
			if (length > 2048)  return;
			byte data[] = readByte();
			int count = CharacterConfigTable.getInstance().countCharacterConfig(pc.getId());
			if (count == 0) {
				CharacterConfigTable.getInstance().storeCharacterConfig(pc.getId(), length, data);
			} else {
				CharacterConfigTable.getInstance().updateCharacterConfig(pc.getId(), length, data);
			}
		}
	}

	@Override
	public String getType() {
		return C_CHARCTER_CONFIG;
	}
}
