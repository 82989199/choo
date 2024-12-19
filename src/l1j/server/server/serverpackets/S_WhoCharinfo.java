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

import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_WhoCharinfo extends ServerBasePacket {
	private static final String S_WHO_CHARINFO = "[S] S_WhoCharinfo";
	private static Logger _log = Logger.getLogger(S_WhoCharinfo.class.getName());

	public S_WhoCharinfo(L1PcInstance pc) {
		_log.fine("Who charpack for : " + pc.getName());
		
		String lawfulness = "";

		/* Kill & Death 시스템?  -by 천국- */		
		float win = 0;
		float lose = 0;
		float total = 0;
		float winner = 0;
		if(pc.getKDA() != null){
			win = pc.getKDA().kill;
			lose = pc.getKDA().death;
		}
		total = win + lose;
		winner = ((win*100)/(total));
		/* Kill & Death 시스템?  -by 천국- */

		int lawful = pc.getLawful();
		if (lawful < 0) {
			lawfulness = "(Chaotic)";
		} else if (lawful >= 0 && lawful < 500) {
			lawfulness = "(Neutral)";
		} else if (lawful >= 500) {
			lawfulness = "(Lawful)";
		}

		writeC(Opcodes.S_MESSAGE);
		writeC(0x08);

		String title = "";
		String clan = "무혈";

		if (pc.getTitle().equalsIgnoreCase("") == false) {
			title = pc.getTitle() + " ";
		}

		if (pc.getClanid() > 0) {
			clan = "[" + pc.getClanname() + "]";
		}
				/** 일반 무혈일경우 누구 **/
		writeS(title + "[" + pc.getName() + "] " + lawfulness + "혈맹:" + clan + "\n\r" + "킬: " + (int) win + " / 데스: " + (int) lose + " / 승률:" + winner + "%");
		writeD(0);
	}
	
	public S_WhoCharinfo(L1NpcShopInstance shopnpc) { 

		_log.fine("Who charpack for : " + shopnpc.getName());

		String lawfulness = "";

		float win = 0;
		float lose = 0;
		float total = 0;
		float winner = 0;

		win 	= 0;
		lose 	= 0;
		total 	= win + lose;
		winner 	= ((win*100)/(total));

		int lawful = shopnpc.getLawful();
		if (lawful < 0) {
			lawfulness = "(Chaotic)";
		} else if (lawful >= 0 && lawful < 500) {
			lawfulness = "(Neutral)";
		} else if (lawful >= 500) {
			lawfulness = "(Lawful)";
		}

		writeC(Opcodes.S_MESSAGE);
		writeC(0x08);

		String title = "";

		if (shopnpc.getTitle().equalsIgnoreCase("") == false) {
			title = shopnpc.getTitle() + " ";
		}
			/** 혈가입했을경우 누구**/
		writeS(title + "["+shopnpc.getName()+"] " + lawfulness + "\n\r" + "킬: 0 / 데스: 0 / 승률:" + winner + "%");
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_WHO_CHARINFO;
	}
}
