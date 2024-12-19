package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.ElfWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.SpecialWarehouse;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.storage.CharacterStorage;
import l1j.server.server.storage.mysql.MySqlCharacterStorage;
import l1j.server.server.templates.L1CharName;
import l1j.server.server.utils.SQLUtil;

public class CharacterTable {
	private CharacterStorage _charStorage;

	private static CharacterTable _instance;

	private static Logger _log = Logger.getLogger(CharacterTable.class
			.getName());

	private final Map<String, L1CharName> _charNameList =
			new ConcurrentHashMap<String, L1CharName>();

	private CharacterTable() {
		_charStorage = new MySqlCharacterStorage();
	}

	public static CharacterTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterTable();
		}
		return _instance;
	}

	public void storeNewCharacter(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.createCharacter(pc);
			if (!_charNameList.containsKey(pc.getName())) {
				L1CharName cn = new L1CharName();
				cn.setName(pc.getName());
				cn.setId(pc.getId());
				_charNameList.put(pc.getName(), cn);
			}
			_log.finest("storeNewCharacter");
		}
	}

	public void updateCharacterAccount(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.updateAccountName(pc);
			_log.finest("updateCharacterAccount");
		}
	}

	public void storeCharacter(L1PcInstance pc) throws Exception {
		synchronized (pc) {
			_charStorage.storeCharacter(pc);
			String name = pc.getName();
			if (!_charNameList.containsKey(name)) {
				L1CharName cn = new L1CharName();
				cn.setName(name);
				cn.setId(pc.getId());
				_charNameList.put(name, cn);
			}
			_log.finest("storeCharacter: " + pc.getName());
		}
	}

	public void deleteCharacter(String accountName, String charName)
			throws Exception {

		_charStorage.deleteCharacter(accountName, charName);
		if (_charNameList.containsKey(charName)) {
			_charNameList.remove(charName);
		}
		_log.finest("deleteCharacter");
	}

	public boolean isContainNameList(String name) {
		return _charNameList.containsKey(name);
	}

	public L1PcInstance restoreCharacter(String charName) throws Exception {
		L1PcInstance pc = _charStorage.loadCharacter(charName);
		return pc;
	}

	public L1PcInstance loadCharacter(String charName) throws Exception {
		L1PcInstance pc = null;
		try {
			pc = restoreCharacter(charName);

			if (pc == null) {
				return null;
			}

			L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());

			if (!map.isInMap(pc.getX(), pc.getY())) {
				pc.setX(33087);
				pc.setY(33396);
				pc.setMap((short) 4);
			}
			_log.finest("loadCharacter: " + pc.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pc;

	}

	public static void clearOnlineStatus() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=0");
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void updateOnlineStatus(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET OnlineStatus=1, lastLoginTime=now() WHERE objid=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}


	public void restoreInventory(L1PcInstance pc) {
		pc.getInventory().loadItems();
		PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
		warehouse.loadItems();
		ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
		elfwarehouse.loadItems();
		pc.getDwarfForPackageInventory().loadItems();
		SupplementaryService supplementaryservice = WarehouseManager
				.getInstance().getSupplementaryService(pc.getAccountName());
		supplementaryservice.loadItems();
		SpecialWarehouse specialwarehose = WarehouseManager.getInstance().getSpecialWarehouse(pc.getName());
		specialwarehose.loadItems();
	}

	/*public static boolean doesCharNameExist(String name) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT account_name FROM characters WHERE char_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}*/

	public void loadAllCharName() {
		L1CharName cn = null;
		String name = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM characters");
			rs = pstm.executeQuery();
			while (rs.next()) {
				cn = new L1CharName();
				name = rs.getString("char_name");
				cn.setName(name);
				cn.setId(rs.getInt("objid"));
				_charNameList.put(name, cn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int PcLevelInDB(int pcid) { // DB에 저장된 레벨값을 불러온다. 
		int result = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT level FROM characters WHERE objid=?");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
			System.out.println("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}



	public L1CharName[] getCharNameList() {
		return _charNameList.values().toArray(new L1CharName[_charNameList.size()]);
	}

	public void updateLoc(int castleid, int a, int b, int c, int d, int f) {
		Connection con = null;
		PreparedStatement pstm = null;
		int[] loc = new int[3];
		loc = L1CastleLocation.getGetBackLoc(castleid);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			// pstm =
			// con.prepareStatement("UPDATE characters SET LocX=?, LocY=?, MapID=? WHERE OnlineStatus=0, MapID IN (?,?,?,?,?)");
			pstm = con
					.prepareStatement("UPDATE characters SET LocX=?, LocY=?, MapID=? WHERE OnlineStatus=0 AND (MapID=? OR MapID=? OR MapID=? OR MapID=? OR MapID=?)");
			pstm.setInt(1, loc[0]);
			pstm.setInt(2, loc[1]);
			pstm.setInt(3, loc[2]);
			pstm.setInt(4, a);
			pstm.setInt(5, b);
			pstm.setInt(6, c);
			pstm.setInt(7, d);
			pstm.setInt(8, f);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void CharacterAccountCheck(L1PcInstance pc, String charName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet loginRs = null;
		ResultSet characterRs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT login, password FROM accounts WHERE ip = ");
			sb.append("(SELECT ip FROM accounts WHERE login = ");
			sb.append("(SELECT account_name FROM characters WHERE char_name = ?))");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, charName);
			loginRs = pstm.executeQuery();

			while (loginRs.next()) {
				pstm = con.prepareStatement(
						"SELECT char_name, level, highlevel, clanname, onlinestatus FROM characters WHERE account_name = ?");
				pstm.setString(1, loginRs.getString("login"));
				characterRs = pstm.executeQuery();

				pc.sendPackets(new S_SystemMessage("-------------------------------------------------"));
				pc.sendPackets(new S_SystemMessage("\\fYAccounts : " + loginRs.getString("login") + ", PassWord : "
						+ loginRs.getString("password")));
				String onlineStatus;
				while (characterRs.next()) {
					onlineStatus = characterRs.getInt("onlinestatus") == 0 ? "" : "(접속중)";
					pc.sendPackets(new S_SystemMessage("* " + characterRs.getString("char_name") + " (Lv:"
							+ characterRs.getInt("level") + ") (HLv:" + characterRs.getInt("highlevel") + ") " + "(혈맹:"
							+ characterRs.getString("clanname") + ") " + "\\fY" + onlineStatus));
				}

			}
			pc.sendPackets(new S_SystemMessage("-------------------------------------------------"));
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(characterRs);
			SQLUtil.close(loginRs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	public void CharacterAccountCheck1(L1PcInstance pc, String charName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet loginRs = null;
		ResultSet characterRs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("SELECT login, password, ip FROM accounts WHERE login = ");
			//sb.append("(SELECT ip FROM accounts WHERE login = ");
			sb.append("(SELECT account_name FROM characters WHERE char_name = ?)");

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, charName);
			loginRs = pstm.executeQuery();
			
			while (loginRs.next()) {
				pstm = con.prepareStatement("SELECT char_name, level, highlevel, clanname, onlinestatus FROM characters WHERE account_name = ?");
				pstm.setString(1, loginRs.getString("login"));
				characterRs = pstm.executeQuery();
				pc.sendPackets(new S_SystemMessage("\\aD-------------------------------------------------"));
				pc.sendPackets(new S_SystemMessage("\\aH계정: " + loginRs.getString("login") + ", 비번: " + loginRs.getString("password") + ", IP: " + loginRs.getString("ip")));
				String onlineStatus;
				while (characterRs.next()) {
					onlineStatus = characterRs.getInt("onlinestatus") == 0 ? "" : "(접속중)";
					pc.sendPackets(new S_SystemMessage("* " + characterRs.getString("char_name") + " (Lv:" + characterRs.getInt("level") + ") (HLv:" + characterRs.getInt("highlevel") + ") " + "(혈맹:" + characterRs.getString("clanname") + ") " + "\\aG" + onlineStatus));
				}	
				
			}
			pc.sendPackets(new S_SystemMessage("\\aD-------------------------------------------------"));
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(characterRs);
			SQLUtil.close(loginRs);			
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
}
