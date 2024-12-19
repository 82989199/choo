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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.Base64;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.utils.SQLUtil;

public class CharacterShopTable {

	private static Logger _log = Logger.getLogger(CharacterShopTable.class.getName());

	private static CharacterShopTable _instance;

	public static CharacterShopTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterShopTable();
		}
		return _instance;
	}

	private CharacterShopTable() {
		load();
	}

	private L1PcInstance load2(String charName, byte[] shopTitle) {
		L1PcInstance pc = null;
		try{
		pc = L1PcInstance.load(charName);
		int currentHpAtLoad = pc.getCurrentHp();
		int currentMpAtLoad = pc.getCurrentMp();

		pc.setOnlineStatus(1);
		CharacterTable.updateOnlineStatus(pc);
		L1World.getInstance().storeObject(pc);
		items(pc);
		L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());
		int tile = map.getTile(pc.getX(), pc.getY());
		if (Config.GET_BACK || !map.isInMap(pc.getX(), pc.getY())
				|| tile == 0 || tile == 4 || tile == 12) {
			int[] loc = Getback.GetBack_Location(pc, true);
			pc.setX(loc[0]);
			pc.setY(loc[1]);
			pc.setMap((short) loc[2]);
		}
		L1World.getInstance().addVisibleObject(pc);
		pc.beginGameTimeCarrier();
		pc.sendVisualEffectAtLogin(); // 독, 수중, 캐슬마스터 등의 시각 효과를 표시

		pc.setSpeedHackCount(0);
		pc.getLight().turnOnOffLight();
		if (pc.getCurrentHp() > 0) {
			pc.setDead(false);
			pc.setStatus(0);
		} else {
			pc.setDead(true);
			pc.setStatus(ActionCodes.ACTION_Die);
		}
		MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
		if (pc.getClanid() != 0) { // 크란 소속중
			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			if (clan != null) {
				if (pc.getClanid() == clan.getClanId() && // 크란을 해산해, 재차, 동명의 크란이 창설되었을 때의 대책
						pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {

				} else {
					pc.setClanid(0);
					pc.setClanname("");
					pc.setClanRank(0);
					pc.save(); // DB에 캐릭터 정보를 기입한다	
				}
			}
		}
		if (currentHpAtLoad > pc.getCurrentHp()) {
			pc.setCurrentHp(currentHpAtLoad);
		}
		if (currentMpAtLoad > pc.getCurrentMp()) {
			pc.setCurrentMp(currentMpAtLoad);
		}
		pc.setNetConnection(null);

		pc.save(); // DB에 캐릭터 정보를 기입한다
		byte[] text = shopTitle;
		pc.setShopChat(text);
		pc.setPrivateShop(true);
		Broadcaster.broadcastPacket(pc, new S_DoActionShop(pc.getId(),ActionCodes.ACTION_Shop, text));

		}catch(Exception e){}
		return pc;
	}

	private void items(L1PcInstance pc) {
	    // DB로부터 캐릭터와 창고의 아이템을 읽어들인다
	    CharacterTable.getInstance().restoreInventory(pc);
	}

	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
	private void load() {
		Connection 			con 	= null;
		PreparedStatement 	pstm	= null;
		ResultSet 			rs 		= null;
		String 				name	= null;
		FastTable<String> _charlist = new FastTable<String>();
		L1PcInstance pc;
		int i = 0;
		try {
			System.out.print("CharacterShopTable Loading...");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_shop_store");
			rs = pstm.executeQuery();
			while (rs.next()) {
				name = rs.getString("char_Name");
				if(!_charlist.contains(name)){
					pc = load2(name, Base64.decode(rs.getString("shop_Title")));
					_charlist.add(name);
					if(pc == null)
						continue;
					ShopListLoad(pc);
					i += 1;
				}
			}
			deleteShop();
			System.out.println("OK! Count: "+i);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			_charlist.clear();
			pc = null;
			i = 0;
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/** 2019.07.04 MJcodes 이재원 앱센터 시세 **/
	private void ShopListLoad(L1PcInstance pc){
		Connection 			con 	= null;
		PreparedStatement 	pstm	= null;
		ResultSet 			rs 		= null;
		MJDShopItem			ditem	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_shop WHERE objid=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			while (rs.next()) {
				ditem = MJDShopItem.create(rs);
				if(ditem.isPurchase)
					pc.addPurchasings(ditem);
				else
					pc.addSellings(ditem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void deleteShop() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_shop");
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
