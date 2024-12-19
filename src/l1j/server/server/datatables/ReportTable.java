package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class ReportTable {

	private static ReportTable _instance;
	private HashMap<String, String> reportList = new HashMap<String, String>();

	public static ReportTable getInstance() {
		if (_instance == null) {
			_instance = new ReportTable();
		}
		return _instance;
	}
	public ReportTable() {
		loadReport();
	}
	private void loadReport() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM report");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String objectName = rs.getString("object_name");
				String reporter = rs.getString("reporter");
				reportList.put(objectName, reporter);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public boolean isReport(String objectName) {
		return reportList.get(objectName) != null ? true : false;
	}
	public void reportUpdate(String objectName, String reporter, Timestamp date) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO report SET object_name=?, reporter=?, date=?");
			pstm.setString(1, objectName);
			pstm.setString(2, reporter); 
			pstm.setTimestamp(3, date);
			pstm.execute();
			reportList.put(objectName, reporter);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}	
}







