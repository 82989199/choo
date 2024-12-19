package l1j.server.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class L1ItemBookMark {

	private static Logger _log = Logger.getLogger(L1ItemBookMark.class.getName());

	private int _charId;
	private int _id;
	private String _name;
	private int _locX;
	private int _locY;
	private short _mapId;
	private int _randomX;
	private int _randomY;
	private int _speed_id;

	public int getSpeed_id() {
		return _speed_id;
	}
	public void setSpeed_id(int i) {
		_speed_id = i;
	}
	public int getId() {
		return _id;
	}
	public void setId(int i) {
		_id = i;
	}
	public int getCharId() {
		return _charId;
	}
	private int _NumId;

	public int getNumId() {
		return _NumId;
	}

	public void setNumId(int i) {
		_NumId = i;
	}

	private int _Temp_id;

	public int getTemp_id() {
		return _Temp_id;
	}

	public void setTemp_id(int i) {
		_Temp_id = i;
	}

	public void setCharId(int i) {
		_charId = i;
	}

	public String getName() {
		return _name;
	}

	public void setName(String s) {
		_name = s;
	}

	public int getLocX() {
		return _locX;
	}

	public void setLocX(int i) {
		_locX = i;
	}

	public int getLocY() {
		return _locY;
	}

	public void setLocY(int i) {
		_locY = i;
	}

	public short getMapId() {
		return _mapId;
	}

	public void setMapId(short i) {
		_mapId = i;
	}

	public int getRandomX() {
		return _randomX;
	}

	public void setRandomX(int i) {
		_randomX = i;
	}

	public int getRandomY() {
		return _randomY;
	}

	public void setRandomY(int i) {
		_randomY = i;
	}

	public L1ItemBookMark() {
	}

	public static void bookmarItemkDB(L1PcInstance pc, L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1ItemBookMark bookmark = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_teleport_item WHERE item_id='" + item.getId() + "' ORDER BY num_id ASC");
			rs = pstm.executeQuery();
			int i = 0;
			while (rs.next()) {
				bookmark = new L1ItemBookMark();
				bookmark.setCharId(rs.getInt("item_id"));
				bookmark.setId(rs.getInt("id"));
				bookmark.setNumId(i);
				bookmark.setTemp_id(i);
				bookmark.setSpeed_id(rs.getInt("speed_id"));
				bookmark.setName(rs.getString("name"));
				bookmark.setLocX(rs.getInt("locx"));
				bookmark.setLocY(rs.getInt("locy"));
				bookmark.setMapId(rs.getShort("mapid"));
				bookmark.setRandomX(rs.getShort("randomX"));
				bookmark.setRandomY(rs.getShort("randomY"));
				item.addBookMark(bookmark);
				i++;
			}		
		} catch (SQLException e) {
			_log.log(Level.WARNING, "bookmarks 예외 발생.", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}


	public static synchronized void addBookmark(L1PcInstance pc, L1ItemInstance item, L1BookMark book) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT max(id)+1 as newid FROM character_teleport_item");
			rs = pstm.executeQuery();
			rs.next();
			int id = rs.getInt("newid");
			L1ItemBookMark items = new L1ItemBookMark();
			items.setId(id);
			pstm = con.prepareStatement("SELECT max(num_id)+1 as newid FROM character_teleport_item WHERE item_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			rs.next();
			int Numid = rs.getInt("newid");                
			items.setNumId(Numid);
			items.setTemp_id(Numid);
			items.setSpeed_id(-1);
			items.setCharId(item.getId());
			items.setName(book.getName());
			items.setLocX(book.getLocX());
			items.setLocY(book.getLocY());
			items.setMapId(book.getMapId());
			pstm = con.prepareStatement("INSERT INTO character_teleport_item SET id=?,num_id=?,speed_id=?, item_id=?, name=?, locx=?, locy=?, mapid=?");
			pstm.setInt(1, items.getId());
			pstm.setInt(2, items.getNumId());
			pstm.setInt(3, items.getSpeed_id());
			pstm.setInt(4, items.getCharId());
			pstm.setString(5, items.getName());
			pstm.setInt(6, items.getLocX());
			pstm.setInt(7, items.getLocY());
			pstm.setInt(8, items.getMapId());
			pstm.execute();
			item._bookmarks.add(items);
			//pc.sendPackets(new S_Bookmarks(items.getName(), items.getMapId(), items.getLocX(), items.getLocY(), items.getNumId()));
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "북마크의 추가로 에러가 발생했습니다.", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void deleteBookmarkItem(int itemid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_teleport_item WHERE item_id=?");
			pstm.setInt(1, itemid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "북마크의 삭제로 에러가 발생했습니다.", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}
