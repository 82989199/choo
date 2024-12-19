package MJShiftObject.Template;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonServerInfo {
	public static CommonServerInfo newInstance(ResultSet rs) throws SQLException{
		CommonServerInfo cInfo = newInstance();
		cInfo.server_description = rs.getString("server_description");
		cInfo.server_identity = rs.getString("server_identity");
		cInfo.server_address = rs.getString("server_address");
		cInfo.server_port = rs.getInt("server_port");
		cInfo.server_is_transfer = rs.getBoolean("server_is_transfer");
		cInfo.server_is_on = rs.getBoolean("server_is_on");
		return cInfo;
	}
	
	private static CommonServerInfo newInstance(){
		return new CommonServerInfo();
	}
	
	public String server_description;
	public String server_identity;
	public String server_address;
	public int server_port;
	public boolean server_is_transfer;
	public boolean server_is_on;
	
	private CommonServerInfo(){}
}
