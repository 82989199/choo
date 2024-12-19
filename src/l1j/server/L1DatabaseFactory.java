package l1j.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.LeakCheckedConnection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 提供访问数据库的各种接口
 */
public class L1DatabaseFactory {
	private static L1DatabaseFactory _instance = null;

	/** 数据库连接池数据源 */
	private ComboPooledDataSource _source;

	/** 消息日志记录器 */
	private static Logger _log = Logger.getLogger(L1DatabaseFactory.class.getName());

	/* 数据库访问所需信息 */
	/** 数据库驱动 */
	private static String _driver;
	/** 数据库服务器URL */
	private static String _url;     
	/** 数据库用户名 */
	private static String _user;    
	/** 数据库密码 */
	private static String _password;

	/**
	 * @return L1DatabaseFactory
	 * @throws SQLException
	 */
	public static L1DatabaseFactory getInstance() throws SQLException {
		if(_instance == null) {
			synchronized (L1DatabaseFactory.class) {
				if(_instance  == null) {
					_instance = new L1DatabaseFactory();
				}
			}
		}
		
		return _instance ;
	}
	
	/**
	 * 设置数据库访问所需信息
	 * 
	 * @param driver
	 *            数据库驱动
	 * @param url
	 *            数据库服务器URL
	 * @param user
	 *            数据库用户名
	 * @param password
	 *            数据库密码
	 */
	public static void setDatabaseSettings(final String driver,
			final String url, final String user, final String password) {
		_driver = driver;
		_url = url;
		_user = user;
		_password = password;
	}

	private L1DatabaseFactory() throws SQLException {
		try {
			// 从L2J移植部分DatabaseFactory功能
			_source = new ComboPooledDataSource();
			_source.setDriverClass(_driver);
			_source.setJdbcUrl(_url);
			_source.setUser(_user);
			_source.setPassword(_password);
			_source.setInitialPoolSize(Config.min);
			_source.setMinPoolSize(Config.min);
			_source.setMaxPoolSize(Config.max);
			_source.setAcquireIncrement(5);
			_source.setAcquireRetryAttempts(30);
			_source.setAcquireRetryDelay(1000);
			_source.setIdleConnectionTestPeriod(60);
			_source.setPreferredTestQuery("SELECT 1");
			_source.setTestConnectionOnCheckin(true);
			_source.setTestConnectionOnCheckout(false);

			/* 测试连接 */
			_source.getConnection().close();
		} catch (SQLException x) {
			_log.fine("数据库连接失败");
			// 重新抛出异常
			throw x;
		} catch (Exception e) {
			_log.fine("数据库连接失败");
			throw new SQLException("无法初始化数据库连接:" + e);
		}
	}

	public void shutdown() {
		try {
			_source.close();
		} catch (Exception e) {
			_log.log(Level.INFO, "", e);
		}
		try {
			_source = null;
		} catch (Exception e) {
			_log.log(Level.INFO, "", e);
		}
	}

	/**
	 * 连接数据库并返回连接对象
	 * 
	 * @return Connection 数据库连接对象
	 * @throws SQLException
	 */
	public Connection getConnection() {
		Connection con = null;

		while (con == null) {
			try {
				con = _source.getConnection();
			} catch (SQLException e) {
				_log.warning("L1DatabaseFactory: getConnection() failed, trying again " + e);
				System.out.println("L1DatabaseFactory: getConnection() failed, trying again " + e);
			}
		}
		return Config.DETECT_DB_RESOURCE_LEAKS ?  LeakCheckedConnection.create(con) : con;
	}
}
