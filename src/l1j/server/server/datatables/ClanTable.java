package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ManagerInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.utils.SQLUtil;

public class ClanTable {
	private static Logger _log = Logger.getLogger(ClanTable.class.getName());

	private static ClanTable _instance;

	private final HashMap<Integer, L1Clan> _clans = new HashMap<Integer, L1Clan>();
	private final HashMap<Integer, L1Clan> _redknights = new HashMap<Integer, L1Clan>(6);
	
//	public static final String CLAN_TUTORIAL_ANN = "자동탈퇴레벨: "+Config.신규혈맹보호레벨+"\r\n\r\n매너는 필수 입니다.\r\n\r\n패드립은 영구추방 대상자 입니다.";
	public static final String CLAN_TUTORIAL_ANN = "자동탈퇴레벨: "+Config.신규혈맹보호레벨;
	public static final int CLAN_TUTORIAL_EMB = Config.신규혈맹엠블런;
	
	public static ClanTable getInstance() {
		if (_instance == null) {
			_instance = new ClanTable();
		}
		return _instance;
	}

	private static L1Clan createRedKnight(int castleId){
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName("붉은 기사단");
		clan.setCastleId(castleId);
		clan.setRedKnight(true);
		L1World.getInstance().storeClan(clan);
		return clan;
	}
	
	private ClanTable() {
		for(int i=1; i<=8; ++i){
			L1Clan clan = createRedKnight(i);
			_redknights.put(i, clan);
		}
		
		{
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM clan_data ORDER BY clan_id");

				rs = pstm.executeQuery();
				L1Clan clan = null;
				while (rs.next()) {					
					clan = new L1Clan();
					int clan_id = rs.getInt(1);
					int castle_id = rs.getInt(5);
					clan.setClanId(clan_id);
					clan.setClanName(rs.getString(2));
					clan.setLeaderId(rs.getInt(3));
					clan.setLeaderName(rs.getString(4));
					clan.setCastleId(castle_id);
					clan.setHouseId(rs.getInt(6));
					clan.setAlliance(rs.getInt(7));
					clan.setClanBirthDay(rs.getTimestamp(8));
					/** 혈맹자동가입 */
					clan.setBot(rs.getString(9).equalsIgnoreCase("true"));
					clan.setBotStyle(rs.getInt(10));
					clan.setBotLevel(rs.getInt(11));
					/** 혈맹자동가입 */
					clan.setOnlineMaxUser(rs.getInt(12));
					clan.setAnnouncement(rs.getString(13));
					clan.setEmblemId(rs.getInt(14));
					clan.setEmblemStatus(rs.getInt(15));
					//	clan.setClanExp(rs.getInt(16));
					clan.setBless(rs.getInt("bless"));
					clan.setBlessCount(rs.getInt("bless_count"));
					clan.setBuffTime(rs.getInt("attack"), rs.getInt("defence"), rs.getInt("pvpattack"), rs.getInt("pvpdefence"));
					clan.setUnderDungeon(rs.getInt("under_dungeon"));
					clan.setRankTime(rs.getInt("ranktime"));
					clan.setRankDate(rs.getTimestamp("rankdate"));
					
					/** 2016.11.25 MJ 앱센터 혈맹 **/
					clan.setJoinSetting(rs.getInt("join_setting"));
					clan.setJoinType(rs.getInt("join_type"));
					clan.setJoinPassword(rs.getString("join_password"));
					clan.setWarPoint(rs.getInt("War_point"));
					/** 2016.11.25 MJ 앱센터 혈맹 **/
					
					// TODO 혈맹 축복 소모
					clan.setEinhasadBlessBuff(rs.getInt("EinhasadBlessBuff"));
					clan.setBuffFirst(rs.getInt("Buff_List1"));
					clan.setBuffSecond(rs.getInt("Buff_List2"));
					clan.setBuffThird(rs.getInt("Buff_List3"));
					
					L1World.getInstance().storeClan(clan);
					
					_clans.put(clan_id, clan);
				}

			} catch (SQLException e) {
				_log.log(Level.SEVERE, "ClanTable[]Error", e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT char_name, ClanRank, level, notes, objid, Type FROM characters WHERE ClanID = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();

				while (rs.next()) {
					String name = rs.getString("char_name");
					int rank = rs.getInt("ClanRank");
					int level = rs.getInt("level");
					String notes = rs.getString("notes");
					int memberId = rs.getInt("objid");
					int type = rs.getInt("Type");
					clan.addClanMember(name, rank, level, notes, memberId, type, 0, null);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, "ClanTable[]Error1", e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		ClanWarehouse clanWarehouse;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
			clanWarehouse.loadItems();
		}
	}

	public L1Clan createTutorialClan(){ 
		L1ManagerInstance manager = L1ManagerInstance.getInstance();
		
		if(manager.getClanid() != 0){
			System.out.println("이미 생성된 신규 혈맹이 있습니다.");
			return null;
		}
		
		L1Clan clan = L1World.getInstance().findClan(Config.신규혈맹이름);
		if(clan != null){
			manager.setClanid(clan.getClanId());
			manager.setClanname(clan.getClanName());
			manager.setClanRank(L1Clan.군주);
			clan.addClanMember(manager.getName(), manager.getClanRank(), manager.getLevel(), "", manager.getId(), manager.getType(), manager.getOnlineStatus(), manager);
			return clan;
		}
		
		clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(Config.신규혈맹이름);
		clan.setLeaderId(manager.getId());
		clan.setLeaderName("메티스");
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setAlliance(0);
		clan.setClanBirthDay(new Timestamp(System.currentTimeMillis()));
		clan.setAnnouncement(CLAN_TUTORIAL_ANN);
		clan.setEmblemId(CLAN_TUTORIAL_EMB);
		clan.setEmblemStatus(1);
		clan.setBless(0);
		clan.setBlessCount(0);
		clan.setBuffTime(0, 0, 0, 0);
		clan.setJoinSetting(1);
		clan.setJoinType(1);
		clan.setBot(false);
		
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, clan_birthday=?, max_online_user=?, announcement=?, emblem_id=?, emblem_status=?,clan_exp=?,bless=?,bless_count=?,attack=?,defence=?,pvpattack=?,pvpdefence=?,join_setting=?,join_type=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setInt(7, clan.getAlliance());
			pstm.setTimestamp(8, clan.getClanBirthDay());
			pstm.setInt(9, clan.getOnlineMaxUser());
			pstm.setString(10, "");
			pstm.setInt(11, 0);
			pstm.setInt(12, 0);
			pstm.setInt(13, 0);
			pstm.setInt(14, 0);
			pstm.setInt(15, 0);
			pstm.setInt(16, 0);
			pstm.setInt(17, 0);
			pstm.setInt(18, 0);
			pstm.setInt(19, 0);
			pstm.setInt(20, clan.getJoinSetting());
			pstm.setInt(21, clan.getJoinType());
			pstm.setInt(22, clan.getEinhasadBlessBuff());
			pstm.setInt(23, clan.getBuffFirst());
			pstm.setInt(24, clan.getBuffSecond());
			pstm.setInt(25, clan.getBuffThird());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error2", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		manager.setClanid(clan.getClanId());
		manager.setClanname(clan.getClanName());
		manager.setClanRank(L1Clan.군주);
		clan.addClanMember(manager.getName(), manager.getClanRank(), manager.getLevel(), "", manager.getId(), manager.getType(), manager.getOnlineStatus(), manager);
		return clan;
	}
	
	public L1Clan createClan(L1PcInstance player, String clan_name, boolean isBot) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return null;
			}
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setAlliance(0);
		clan.setClanBirthDay(time);
		clan.setAnnouncement("");
		clan.setEmblemId(0);
		clan.setEmblemStatus(0);
		clan.setBless(0);
		clan.setBlessCount(0);
		clan.setBuffTime(0, 0, 0, 0);
		clan.setJoinSetting(1);
		clan.setJoinType(1);
		clan.setBot(isBot);
		
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, clan_birthday=?, max_online_user=?, announcement=?, emblem_id=?, emblem_status=?,clan_exp=?,bless=?,bless_count=?,attack=?,defence=?,pvpattack=?,pvpdefence=?,join_setting=?,join_type=?,bot=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setInt(7, clan.getAlliance());
			pstm.setTimestamp(8, clan.getClanBirthDay());
			pstm.setInt(9, clan.getOnlineMaxUser());
			pstm.setString(10, "");
			pstm.setInt(11, 0);
			pstm.setInt(12, 0);
			pstm.setInt(13, 0);
			pstm.setInt(14, 0);
			pstm.setInt(15, 0);
			pstm.setInt(16, 0);
			pstm.setInt(17, 0);
			pstm.setInt(18, 0);
			pstm.setInt(19, 0);
			pstm.setInt(20, clan.getJoinSetting());
			pstm.setInt(21, clan.getJoinType());
			pstm.setBoolean(22, clan.isBot());
			pstm.setInt(23, clan.getEinhasadBlessBuff());
			pstm.setInt(24, clan.getBuffFirst());
			pstm.setInt(25, clan.getBuffSecond());
			pstm.setInt(26, clan.getBuffThird());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error2", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());
		player.setClanRank(L1Clan.군주);
		clan.addClanMember(player.getName(), player.getClanRank(), player.getLevel(), "", player.getId(), player.getType(), player.getOnlineStatus(), player);
		try {
			player.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error3", e);
		}
		return clan;
	}
	
	public L1Clan createClan(L1PcInstance player, String clan_name) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return null;
			}
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setAlliance(0);
		clan.setClanBirthDay(time);
		clan.setAnnouncement("");
		clan.setEmblemId(0);
		clan.setEmblemStatus(0);
		clan.setBless(0);
		clan.setBlessCount(0);
		clan.setBuffTime(0, 0, 0, 0);
		clan.setJoinSetting(1);
		clan.setJoinType(1);
		
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, clan_birthday=?, max_online_user=?, announcement=?, emblem_id=?, emblem_status=?,clan_exp=?,bless=?,bless_count=?,attack=?,defence=?,pvpattack=?,pvpdefence=?,join_setting=?,join_type=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setInt(7, clan.getAlliance());
			pstm.setTimestamp(8, clan.getClanBirthDay());
			pstm.setInt(9, clan.getOnlineMaxUser());
			pstm.setString(10, "");
			pstm.setInt(11, 0);
			pstm.setInt(12, 0);
			pstm.setInt(13, 0);
			pstm.setInt(14, 0);
			pstm.setInt(15, 0);
			pstm.setInt(16, 0);
			pstm.setInt(17, 0);
			pstm.setInt(18, 0);
			pstm.setInt(19, 0);
			pstm.setInt(20, clan.getJoinSetting());
			pstm.setInt(21, clan.getJoinType());
			pstm.setInt(22, clan.getEinhasadBlessBuff());
			pstm.setInt(23, clan.getBuffFirst());
			pstm.setInt(24, clan.getBuffSecond());
			pstm.setInt(25, clan.getBuffThird());
			
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error2", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());
		player.setClanRank(L1Clan.군주);
		clan.addClanMember(player.getName(), player.getClanRank(), player.getLevel(), "", player.getId(), player.getType(), player.getOnlineStatus(), player);
		try {
			player.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error3", e);
		}
		return clan;
	}

	public static void updateWarPoint(L1Clan clan){
		Connection con = null;
		PreparedStatement pstm = null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("update clan_data set War_point=? WHERE clan_name=?");
			pstm.setInt(1, clan.getWarPoint());
			pstm.setString(2, clan.getClanName());
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void updateOnlineUser(L1Clan clan){
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("update clan_data set total_m=?, current_m=? WHERE clan_name=?");
			pstm.setInt(1, clan.getClanMemberList().size());
			pstm.setInt(2, clan.getCurrentOnlineMemebers());
			pstm.setString(3, clan.getClanName());
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateClan(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET clan_id=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, clan_birthday=?, bot_style=?, bot_level=?, max_online_user=?, announcement=?, emblem_id=?, emblem_status=?, join_setting=?, join_type=?, total_m=?, current_m=?, War_point=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=? WHERE clan_name=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setInt(2, clan.getLeaderId());
			pstm.setString(3, clan.getLeaderName());
			pstm.setInt(4, clan.getCastleId());
			pstm.setInt(5, clan.getHouseId());
			pstm.setInt(6, clan.getAlliance());
			pstm.setTimestamp(7, clan.getClanBirthDay());
			/** 혈맹자동가입 */
			pstm.setInt(8, clan.getBotStyle());
			pstm.setInt(9, clan.getBotLevel());
			/** 혈맹자동가입 */
			pstm.setInt(10, clan.getOnlineMaxUser());
			pstm.setString(11, clan.getAnnouncement());
			pstm.setInt(12, clan.getEmblemId());
			pstm.setInt(13, clan.getEmblemStatus());
			pstm.setInt(14, clan.getJoinSetting());
			pstm.setInt(15, clan.getJoinType());
			pstm.setInt(16, clan.getClanMemberList().size());
			pstm.setInt(17, clan.getCurrentOnlineMemebers());
			pstm.setInt(18, clan.getWarPoint());
			pstm.setInt(19, clan.getEinhasadBlessBuff());
			pstm.setInt(20, clan.getBuffFirst());
			pstm.setInt(21, clan.getBuffSecond());
			pstm.setInt(22, clan.getBuffThird());
			pstm.setString(23, clan.getClanName());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error4", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 ** 혈맹자동가입*
	 * 
	 * @param player
	 * @param clan_name
	 * @param style
	 * @return
	 */
	public void createClanBot(L1PcInstance player, String clan_name, int style) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name))
				return;
		}

		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setBot(true);
		clan.setBotStyle(style);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());

		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, bot=?, bot_style=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setString(7, "true");
			pstm.setInt(8, style);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error5", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		/** 혈맹자동가입 */
		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);
		/** 혈맹자동가입 */
	}

	public void deleteClan(L1Clan clan){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_data WHERE clan_name=?");
			pstm.setString(1, clan.getClanName());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error6", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		clanWarehouse.clearItems();
		clanWarehouse.deleteAllItems();

		L1World.getInstance().removeClan(clan);
		_clans.remove(clan.getClanId());
	}
	
	public void deleteClan(String clan_name) {
		L1Clan clan = L1World.getInstance().findClan(clan_name);
		if (clan == null) {
			return;
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_data WHERE clan_name=?");
			pstm.setString(1, clan_name);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error6", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		clanWarehouse.clearItems();
		clanWarehouse.deleteAllItems();

		L1World.getInstance().removeClan(clan);
		_clans.remove(clan.getClanId());
	}

	public L1Clan getTemplate(int clan_id) {
		return _clans.get(clan_id);
	}
	
	public L1Clan getRedKnight(int castleId){
		return _redknights.get(castleId);
	}

	/** 혈맹자동가입 */
	public static void reload() {
		L1World.getInstance().clearClan();
		ClanTable oldInstance = _instance;
		_instance = new ClanTable();
		if (oldInstance != null) {
			oldInstance._clans.clear();		
			oldInstance._redknights.clear();
		}
	}

	public L1Clan find(String clan_name) {
		for (L1Clan clan : _clans.values()) {
			if (clan.getClanName().equalsIgnoreCase(clan_name))
				return clan;
		}
		return null;
	}
	
	public L1Clan findCastleClan(int castleId){
		for(L1Clan clan : _clans.values()){
			if(clan == null)
				continue;
			
			if(clan.getCastleId() == castleId)
				return clan;
		}
		
		return _redknights.get(castleId);
	}

	public void updateUnderDungeon(int clanid, int type) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET under_dungeon=? WHERE clan_id=?")) {
			pstm.setInt(1, type);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRankDate(int clanid, Timestamp time) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET rankdate=? WHERE clan_id=?")) {
			pstm.setTimestamp(1, time);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRankTime(int clanid, int time) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET ranktime=? WHERE clan_id=?")) {
			pstm.setInt(1, time);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 혈맹버프 **/
	public void updateBlessCount(int clanid, int count) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET bless_count=? WHERE clan_id=?")) {
			pstm.setInt(1, count);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateBless(int clanid, int bless) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET bless=? WHERE clan_id=?")) {
			pstm.setInt(1, bless);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateBuffTime(int a, int b, int c, int d, int clanid) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET attack=?,defence=?,pvpattack=?,pvpdefence=? WHERE clan_id=?")) {
			pstm.setInt(1, a);
			pstm.setInt(2, b);
			pstm.setInt(3, c);
			pstm.setInt(4, d);
			pstm.setInt(5, clanid);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 2016.11.25 MJ 앱센터 혈맹 **/
	public void updateClanPassword(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE clan_data SET join_password=? WHERE clan_name=?");
			pstm.setString(1, clan.getJoinPassword());
			pstm.setString(2, clan.getClanName());
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	/** 2016.11.25 MJ 앱센터 혈맹 **/
	
}

