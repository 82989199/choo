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
import l1j.server.server.model.L1Character;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_MoveCharPacket extends ServerBasePacket {

	private static final String _S__1F_MOVECHARPACKET = "[S] S_MoveCharPacket";

	public S_MoveCharPacket(int object_id, int x, int y, int h){
		switch (h) {
		case 1: x--; y++; break;
		case 2: x--; break;
		case 3: x--; y--; break;
		case 4: y--; break;
		case 5: x++; y--; break;
		case 6: x++; break;
		case 7: x++; y++;break;
		case 0: y++; break;
		}
		
		writeC(Opcodes.S_MOVE_OBJECT);
		writeD(object_id);
		writeH(x);
		writeH(y);
		writeC(h);
		writeC(129);
		writeD(0);
	}
	
	public S_MoveCharPacket(L1Character cha) {
		int x = cha.getX();
		int y = cha.getY();
		// if(cha instanceof L1PcInstance)
		// {

		switch (cha.getHeading()) {
		case 1: x--; y++; break;
		case 2: x--; break;
		case 3: x--; y--; break;
		case 4: y--; break;
		case 5: x++; y--; break;
		case 6: x++; break;
		case 7: x++; y++;break;
		case 0: y++; break;
		}

		writeC(Opcodes.S_MOVE_OBJECT);
		writeD(cha.getId());
		writeH(x);
		writeH(y);
		writeC(cha.getHeading());
		writeC(129);
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__1F_MOVECHARPACKET;
	}
}
