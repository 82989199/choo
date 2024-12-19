/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_EnchantRanking1 extends ServerBasePacket {

	private static final String S_EnchantRanking1 = "[C] S_EnchantRanking";

	private byte[] _byte = null;
	private int j = 0;
	static String[] name;
	static String[] name1;
	static String[] castlename;
	static String[] clanname;
	static String[] leadername;
	static int[] enchantlvl;
	static int[] aden;
	static int[] armor;
	static int[] level;
	static int[] Ac;
	static int[] priaden;
	static int[] castleid;
	static int[] hascastle;
	static int[] taxrate;
	static int[] castleaden;
	static int[] MaxHp;
	static int[] MaxMp;

	public S_EnchantRanking1(L1PcInstance pc, int number) {
		name = new String[10];
		name1 = new String[10];
		enchantlvl = new int[10];
		aden = new int[10];
		armor = new int[10];
		level = new int[10];
		Ac = new int[10];
		priaden = new int[10];
		castlename = new String[10];
		clanname = new String[10];
		leadername = new String[10];
		castleid = new int[10];
		hascastle = new int[10];
		taxrate = new int[10];
		castleaden = new int[10];
		MaxHp = new int[10];
		MaxMp = new int[10];
		buildPacket(pc, number);
	}

	private void buildPacket(L1PcInstance pc, int number) {
		String date = time();
		String type = null;
		String title = null;
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS("GM");// 글쓴이
		switch (number) {
		
		case 1:
			title = "배팅코인 랭킹";
			break;
//		case 2:
//			title = "마일리지 랭킹";
//			break;
		}
		writeS(title);
		writeS(date);
		switch (pc.getType()) {
		case 0:
			type = "군주";
			break;
		case 1:
			type = "기사";
			break;
		case 2:
			type = "요정";
			break;
		case 3:
			type = "마법사";
			break;
		case 4:
			type = "다크엘프";
			break;
		}
		int p = Rank(pc, number);
		if (number == 1) { // 추가부분입니다
			writeS("  1위 " + priaden[0] + "개의 배팅 코인\n\r" + "  소유자 : " + name[0] + "\n\r" + "  2위 " + priaden[1] + "개의 배팅 코인\n\r" + "  소유자 : " + name[1]
					+ "\n\r" + "  3위 " + priaden[2] + "개의 배팅 코인\n\r" + "  소유자 : " + name[2] + "\n\r" + "  4위 " + priaden[3] + "개의 배팅 코인\n\r" + "  소유자 : " + name[3]
					+ "\n\r" + "  5위 " + priaden[4] + "개의 배팅 코인\n\r" + "  소유자 : " + name[4] + "\n\r" + "  6위 " + priaden[5] + "개의 배팅 코인\n\r" + "  소유자 : " + name[5]
					+ "\n\r" + "  7위 " + priaden[6] + "개의 배팅 코인\n\r" + "  소유자 : " + name[6] + "\n\r" + "  8위 " + priaden[7] + "개의 배팅 코인\n\r" + "  소유자 : " + name[7]
					+ "\n\r" + "  9위 " + priaden[8] + "개의 배팅 코인\n\r" + "  소유자 : " + name[8] + "\n\r" + " 10위 " + priaden[9] + "개의 배팅 코인\n\r" + "  소유자 : " + name[9]
					+ "\n\r" + "      ");
//		} else if (number == 2) { // 추가부분입니다
//			writeS("  1위 " + MaxMp[0] + "개의 마일리지\n\r" + "  소유자 : " + name[0] + "\n\r" + "  2위 " + MaxMp[1] + "개의 마일리지\n\r" + "  소유자 : " + name[1]
//					+ "\n\r" + "  3위 " + MaxMp[2] + "개의 마일리지\n\r" + "  소유자 : " + name[2] + "\n\r" + "  4위 " + MaxMp[3] + "개의 마일리지\n\r" + "  소유자 : " + name[3]
//					+ "\n\r" + "  5위 " + MaxMp[4] + "개의 마일리지\n\r" + "  소유자 : " + name[4] + "\n\r" + "  6위 " + MaxMp[5] + "개의 마일리지\n\r" + "  소유자 : " + name[5]
//					+ "\n\r" + "  7위 " + MaxMp[6] + "개의 마일리지\n\r" + "  소유자 : " + name[6] + "\n\r" + "  8위 " + MaxMp[7] + "개의 마일리지\n\r" + "  소유자 : " + name[7]
//					+ "\n\r" + "  9위 " + MaxMp[8] + "개의 마일리지\n\r" + "  소유자 : " + name[8] + "\n\r" + " 10위 " + MaxMp[9] + "개의 마일리지\n\r" + "  소유자 : " + name[9]
//					+ "\n\r" + "      ");
		}

	}

	private int Rank(L1PcInstance pc, int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int objid = pc.getId();
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			switch (number) {
			
			case 1:
				pstm = con.prepareStatement(
						"SELECT count, characters.char_name FROM character_items, characters WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 3000184 order by count desc limit 10");
				break;
		
//			case 2: // 추가부분입니다
//				pstm = con.prepareStatement(
//						"SELECT count, accounts.login FROM character_warehouse, accounts WHERE  login in(select login from accounts where access_level = 0) And character_warehouse.account_name =accounts.login And item_id = 202221 order by count desc limit 10");
//				break;
			default:
				pstm = con.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by Exp desc limit 10");
				break;
			}

			rs = pstm.executeQuery();
			if (number == 1) { // 추가부분입니다
				while (rs.next()) {
					priaden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
//			} else if (number == 2) { // 추가부분입니다
//				while (rs.next()) {
//					MaxMp[i] = rs.getInt(1);
//					name[i] = rs.getString(2);
//					i++;
//				}
		
			} else {
				while (rs.next()) {
					name[i] = rs.getString(1);
					i++;
				}

				// 레코드가 없거나 5보다 작을때
				while (i < 10) {
					name[i] = "없음.";
					i++;
				}
			}
		} catch (SQLException e) {
			// _log.log(Level.SEVERE, "S_EnchantRanking1[]Error", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return i;
	}

	private static String time() {
		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		String year2;
		if (year < 10) {
			year2 = "0" + year;
		} else {
			year2 = Integer.toString(year);
		}
		int Month = cal.get(Calendar.MONTH) + 1;
		String Month2 = null;
		if (Month < 10) {
			Month2 = "0" + Month;
		} else {
			Month2 = Integer.toString(Month);
		}
		int date = cal.get(Calendar.DATE);
		String date2 = null;
		if (date < 10) {
			date2 = "0" + date;
		} else {
			date2 = Integer.toString(date);
		}
		return year2 + "/" + Month2 + "/" + date2;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_EnchantRanking1;
	}

}
