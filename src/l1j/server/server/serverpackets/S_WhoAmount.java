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

public class S_WhoAmount extends ServerBasePacket {
	
	private static final String S_WHO_AMOUNT = "[S] S_WhoAmount";
	public S_WhoAmount(String amount) {
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(0x0051);
		writeC(0x01);
		writeS(amount);
		
		writeD(0);	// 더미로 넣어야 안팅김
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_WHO_AMOUNT;
	}
}
