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

public class S_Message_YN extends ServerBasePacket {
	
	public S_Message_YN(int type) {
		buildPacket(type, null, null, null, 1);
	}

	public S_Message_YN(int type, String msg1) {
		buildPacket(type, msg1, null, null, 1);
	}

	public S_Message_YN(boolean b) {
		writeC(Opcodes.S_ASK);
		writeH(0x0000);
		writeD(0);
		writeH(0x00); 
		writeS("");
	}
	
	public S_Message_YN(int type, String msg1, String msg2) {
		buildPacket(type, msg1, msg2, null, 2);
	}

	public S_Message_YN(int type, String msg1, String msg2, String msg3) {
		buildPacket(type, msg1, msg2, msg3, 3);
	}

	private void buildPacket(int type, String msg1, String msg2, String msg3,
			int check) {
		writeC(Opcodes.S_ASK);
		writeH(0x0000);
		writeD(0);
		writeH(type);
		if (check == 1) {
			writeS(msg1);
		} else if (check == 2) {
			writeS(msg1);
			writeS(msg2);
		} else if (check == 3) {
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
		}
	}
	
	public S_Message_YN(int idx, int type, String msg){
		writeC(Opcodes.S_ASK);
		writeH(0);
		writeD(idx);
		writeH(type);
		writeS(msg);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return "[S] S_Message_YN";
	}
}
