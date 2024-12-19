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

import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_TaxRate extends ClientBasePacket {

	private static final String C_TAX_RATE = "[C] C_TaxRate";
	public C_TaxRate(byte abyte0[], GameClient clientthread)
			throws Exception {
		super(abyte0);
		int i = readD();
		int j = readC();

		L1PcInstance player = clientthread.getActiveChar();
		if ( player == null)return;
		if (i == player.getId()) {
			L1Clan clan = L1World.getInstance().getClan(player.getClanid());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) {
					if (j >= 10 && j <= 50) {
						MJCastleWarBusiness.getInstance().setTaxRate(castle_id, j);
						MJCastleWarBusiness.getInstance().updateCastle(castle_id);
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_TAX_RATE;
	}

}
