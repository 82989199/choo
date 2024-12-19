package MJShiftObject.DB;

import java.sql.Connection;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import l1j.server.Config;
import l1j.server.server.utils.LeakCheckedConnection;

public class MJShiftDBFactory {
	private ComboPooledDataSource _source;
	public MJShiftDBFactory(MJShiftDBArgs args) throws SQLException{
		try {
			_source = new ComboPooledDataSource();
			_source.setDriverClass(args.DRIVER_NAME);
			_source.setJdbcUrl(args.URL);
			_source.setUser(args.USER_NAME);
			_source.setPassword(args.PASSWORD);
			_source.setInitialPoolSize(args.MIN_POOL_SIZE);
			_source.setMinPoolSize(args.MIN_POOL_SIZE);
			_source.setMaxPoolSize(args.MAX_POOL_SIZE);
			_source.setAcquireIncrement(5);
			_source.setAcquireRetryAttempts(30);
			_source.setAcquireRetryDelay(1000);
			_source.setIdleConnectionTestPeriod(60);
			_source.setPreferredTestQuery("SELECT 1");
			_source.setTestConnectionOnCheckin(true);
			_source.setTestConnectionOnCheckout(false);
			_source.getConnection().close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("could not init DB connection:" + e);
		}
	}
	public void shutdown() {
		try {
			_source.close();
		} catch (Exception e) {
		}finally{
			_source = null;
		}
	}
	
	public Connection get_connection(){
		Connection con = null;
		while (con == null) {
			try {
				con = _source.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("L1DatabaseFactory: getConnection() failed, trying again " + e);
			}
		}
		return Config.DETECT_DB_RESOURCE_LEAKS ?  LeakCheckedConnection.create(con) : con;
	}
	
}
