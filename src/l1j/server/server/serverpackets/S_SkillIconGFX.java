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
// ServerBasePacket

public class S_SkillIconGFX extends ServerBasePacket {

	public S_SkillIconGFX(int i, int j) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		writeH(0);//추가
	}
	public S_SkillIconGFX(int i, int j, int gfxid, int objid) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
//		writeBit(gfxid);//주석 밑에추가
		writeH(gfxid);//인형더블클릭ON표기
		writeD(objid);
	}
	public S_SkillIconGFX(int i) {
		writeC(Opcodes.S_EVENT);
		writeC(0xa0);
		writeC(1);
		writeH(0);
		writeC(2);
		writeH(i);
	}
	public S_SkillIconGFX(int i, int j, boolean on) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		if (on)
			writeC(1);
		else
			writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
