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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SkillIconGFX, S_CharVisualUpdate

public class S_ChangeShape extends ServerBasePacket {

	public S_ChangeShape(int objId, int polyId) {
		buildPacket(objId, polyId, false);
	}

	public S_ChangeShape(int objId, int polyId, boolean weaponTakeoff) {
		buildPacket(objId, polyId, weaponTakeoff);
	}

	private void buildPacket(int objId, int polyId, boolean weaponTakeoff) {
		writeC(Opcodes.S_POLYMORPH);
		writeD(objId);
		writeH(polyId);
		// 왜 29인가 불명
		writeC(weaponTakeoff ? 0 : 29);
		writeC(0xff);
		writeC(0xff);
		writeC(0x00);
		writeS("abcd");
	}
	public S_ChangeShape(int objId, int polyId, int currentWeapon) {
		writeC(Opcodes.S_POLYMORPH);
		writeD(objId);
		writeH(polyId);
		writeC(currentWeapon);
		writeC(0xff);
		writeC(0xff);
		writeC(0x00);
		writeS("abcd");
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
