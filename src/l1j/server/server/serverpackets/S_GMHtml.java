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

public class S_GMHtml extends ServerBasePacket {

	public S_GMHtml(String s1, String s2) {
		String[] board = new String[2];
		board[0] = s1;
		board[1] = s2;
		writeC(Opcodes.S_HYPERTEXT);
		writeD(0);
		writeS("withdraw");
		if ((board != null) && (1 <= board.length)) {
			writeH(1);
			writeH(board.length);
			for (String datum : board) {
				writeS(datum);
			}
		}
	}

	public S_GMHtml(int _objid, String html) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(_objid);
		writeS("hsiw");
		writeS(html);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
