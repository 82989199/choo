package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class IpConnectDelay {

	private static IpConnectDelay _instance;

	public static IpConnectDelay getInstance() {
		if (_instance == null) {
			_instance = new IpConnectDelay();
		}
		return _instance;
	}

	private Map<String, Integer> _ip_list = new HashMap<String, Integer>();

	public boolean ipDelayChech(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ip_delay_check WHERE ip=?");
			pstm.setString(1, ip);
			rs = pstm.executeQuery();
			if (rs.next()) {
				Timestamp _lastActive = rs.getTimestamp("login_date");
				if (_lastActive.getTime() + 1000 < System.currentTimeMillis()) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	public boolean isDelayBan(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ip_delay_check WHERE ip=?");
			pstm.setString(1, ip);
			rs = pstm.executeQuery();
			if (rs.next()) {
				int ban = rs.getInt("is_ban");
				if (ban == 1) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	public void setBanIp(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE ip_delay_check SET is_ban=? WHERE ip=?");
			pstm.setInt(1, 1);
			pstm.setString(2, ip);
			;
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void setDelayInsert(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			String sql = "INSERT INTO ip_delay_check (ip,login_date,is_ban) VALUES (?, ?, ?) " + "ON DUPLICATE KEY UPDATE ip=?,login_date=?,is_ban=?";

			Timestamp _lastActive = new Timestamp(System.currentTimeMillis());

			SQLUtil.execute(con, sql, new Object[] { ip, _lastActive, Integer.valueOf(0), ip, _lastActive, Integer.valueOf(0) });

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getConnetCount(String ip) {
		return _ip_list.get(ip);
	}

	public void addConnetCount(String ip) {
		if (_ip_list.get(ip) == null) {
			_ip_list.put(ip, 0);
		}
		_ip_list.put(ip, _ip_list.get(ip) + 1);
	}

	public void setConnetCount(String ip) {
		_ip_list.put(ip, 0);
	}
}
