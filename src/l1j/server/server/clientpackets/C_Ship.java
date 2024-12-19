/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
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

import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_SystemMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Ship extends ClientBasePacket {

	private static final String C_SHIP = "[C] C_Ship";

	public C_Ship(byte abyte0[], GameClient client) {
		super(abyte0);

		int shipMapId = readH();
		int locX = readH();
		int locY = readH();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		int mapId = pc.getMapId();

		switch (mapId) {
		case 5:
			pc.getInventory().consumeItem(40299, 1);
			break;
		case 6:
			pc.getInventory().consumeItem(40298, 1);
			break;
		case 83:
			pc.getInventory().consumeItem(40300, 1);
			break;
		case 84:
			pc.getInventory().consumeItem(40301, 1);
			break;
		case 446:
			pc.getInventory().consumeItem(40303, 1);
			break;
		case 447:
			pc.getInventory().consumeItem(40302, 1);
			break;
		default:
			Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
			client.kick();
			try{
			client.close();
			}catch(Exception e){
				e.printStackTrace();
			}

			pc.sendPackets(new S_SystemMessage( "버그쓰지마 시발니미창년아"));
			System.out.println("특정 좌표 이동 중계기 버그 > " + pc.getName() + " 이동시도 맵> " + mapId);
			break;
		}
		pc.start_teleport(locX, locY, shipMapId, 0, 169, true, false);
		client.kick();//추방
	}

	@Override
	public String getType() {
		return C_SHIP;
	}
}
