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

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_LeaveParty extends ClientBasePacket {

	private static final String C_LEAVE_PARTY = "[C] C_LeaveParty";

	public C_LeaveParty(byte decrypt[], GameClient client)
			throws Exception {
		super(decrypt);
		L1PcInstance player = client.getActiveChar();
		if ( player == null)return;
		
		if(!player.is_world()){
			player.sendPackets("해당 맵에서는 탈퇴할 수 없습니다.");
			return;
		}
			
		if (player.isInParty()) { // 파티중
			player.getParty().leaveMember(player);
		}
	}

	@Override
	public String getType() {
		return C_LEAVE_PARTY;
	}

}
