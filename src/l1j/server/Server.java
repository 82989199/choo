package l1j.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.LogManager;

import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.server.GameServer;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.utils.PerformanceTimer;

/**
 * 启动 l1j-jp 的服务器。
 */
public class Server {
	/** 日志配置文件的文件夹。 */
	private static final String LOG_PROP = "./config/log.properties";

	private static volatile Server uniqueInstance;

	public static Server createServer()
	{
		if (uniqueInstance == null) {
			synchronized (Server.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new Server();
				}
			}
		}

		return uniqueInstance;
	}

	public static void startLoginServer() {
		LoginController.getInstance().setMaxAllowedOnlinePlayers(Config.MAX_ONLINE_USERS);
		MJNetServerLoadManager.getInstance().run();
	}

	public void changePort(int port){
		MJNetServerLoadManager.reload();
	}

	public void shutdown() {
		GameServer.getInstance().shutdown();
	}

	public static PrintStream _dout;

	public class ConsoleScanner implements Runnable {
		@Override
		public void run() {
			try {
				// test
				ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
				_dout = System.out;
				PrintStream ps = new PrintStream(baos);
				System.setOut(ps);
				System.setErr(ps);
				while (true) {
					Thread.sleep(1000L);
					String s = null;
					synchronized(baos){
						s = baos.toString();
						baos.reset();
					}

					if(s == null)
						continue;
					s = s.trim();
					if(s.length() > 0){
						String msg = String.format("%s\r\n", s);
						LoggerInstance.getInstance().addCmd(msg);
						_dout.print(msg);
					}
				}
			} catch (InterruptedException e) {
			} finally  {
			}
		}
	}

	/**
	 * 服务器主程序。
	 *
	 * @param args
	 *            命令行参数
	 * @throws SQLException
	 * @throws Exception
	 */
	public Server()  {
		new Thread(new ConsoleScanner()).start();
		initLogManager();
		initDBFactory();
		try {
			PerformanceTimer timer = new PerformanceTimer();
			timer.reset();
			timer = null;
            /*startGameServer();
            startLoginServer();*/
		} catch (Exception e) {}
	}

	public static void startGameServer() {
		OpeningMent();
		try {
			GameServer.getInstance().initialize();
		} catch(Exception e) {
			e.printStackTrace();
		};
		ClosingMent();
	}

	private void initLogManager() {
		File logFolder = new File("log");
		logFolder.mkdir();

		try {
			InputStream is = new BufferedInputStream(new FileInputStream(LOG_PROP));
			LogManager.getLogManager().readConfiguration(is);
			is.close();
		} catch (IOException e) {
			System.exit(0);
		}
		try {
			//Config.load();
		} catch (Exception e) {
			System.exit(0);
		}
	}

	private static void OpeningMent() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = Server.class.getClassLoader().getResourceAsStream("./config/buildtime.properties");
			if (input == null) {
				System.err.println("无法加载'config/buildtime.properties'。请确保文件存在并可访问。");
				return; // Early return to avoid further null handling
			}
			prop.load(input);
			String buildDate = prop.getProperty("build.date");
			if (buildDate == null) {
				System.err.println("文件中未设置'build.date'属性。");
				buildDate = "日期未设置。"; // Default message or handling
			}
			System.out.println("********************************************************************");
			System.out.println("** L1J Server Console");
			System.out.println("** Build Date: " + buildDate + "");
			System.out.println("** Original Author: MJsoft");
			System.out.println("** 提交者: Nodim(第17次 Dunk Pack 运营包)");
			System.out.println("** 版本: Nodim 2.0");
			System.out.println("********************************************************************");
			System.out.println("");
		} catch (Exception e) {
			System.err.println("加载属性文件时发生错误。" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void ClosingMent() {
		try {
			System.out.println("");
			System.out.println("********************************************************************");
			System.out.println("");
			System.out.println("** rates");
			System.out.println("** 经验值: " + Config.RATE_XP + "倍");
			System.out.println("** 血盟经验值追加: " + (Config.성혈경험치 * 100) + "%");
			System.out.println("** 金币: " + Config.RATE_DROP_ADENA + "倍");
			System.out.println("** 道具: " + Config.RATE_DROP_ITEMS + "倍");
			System.out.println("** 正义值: " + Config.RATE_LAWFUL + "倍");
			System.out.println("** 负重: " + Config.RATE_WEIGHT_LIMIT + "倍");
			System.out.println("** 武器强化: " + Config.ENCHANT_CHANCE_WEAPON + "%");
			System.out.println("** 防具强化: " + Config.ENCHANT_CHANCE_ARMOR + "%");
			System.out.println("** 饰品强化: " + Config.ENCHANT_CHANCE_ACCESSORY + "%");
			System.out.println("** 公告周期: " + Config.Show_Announcecycle_Time + "分钟");
			System.out.println("** 连接奖励周期: " + Config.EVENT_TIME + "分钟");
			System.out.println("");
			System.out.println("** altsettings");
			System.out.println("** 自动狩猎状态: " + Config.자동사냥 + "");
			System.out.println("** 自动药水状态: " + Config.자동물약버프 + "");
			System.out.println("** 新血盟保护状态: " + Config.신규혈맹보호처리 + "");
			System.out.println("** 队伍最大人数: " + Config.MAX_PT + "人");
			System.out.println("** 队伍最大聊天人数: " + Config.MAX_CHAT_PT + "人");
			System.out.println("** 血盟BUFF最小人数: " + Config.CLAN_COUNT + "人");
			System.out.println("** 个人仓库最大数量: " + Config.MAX_PERSONAL_WAREHOUSE_ITEM + "个");
			System.out.println("** 血盟仓库最大数量: " + Config.MAX_CLAN_WAREHOUSE_ITEM + "个");
			System.out.println("** 自动拾取状态: " + Config.AUTO_LOOT + "");
			System.out.println("** 自动拾取范围: " + Config.LOOTING_RANGE + "格");
			System.out.println("** 世界地图清理周期: " + Config.ALT_ITEM_DELETION_TIME + "分钟");
			System.out.println("");
			System.out.println("** charsettings");
			System.out.println("** 王族: (最大HP/MP) " + Config.PRINCE_MAX_HP + "/"+ Config.PRINCE_MAX_MP +"");
			System.out.println("** 骑士: (最大HP/MP) " + Config.KNIGHT_MAX_HP + "/"+ Config.KNIGHT_MAX_MP +"");
			System.out.println("** 精灵: (最大HP/MP) " + Config.ELF_MAX_HP + "/"+ Config.ELF_MAX_MP +"");
			System.out.println("** 法师: (最大HP/MP) " + Config.WIZARD_MAX_HP + "/"+ Config.WIZARD_MAX_MP +"");
			System.out.println("** 黑暗精灵: (最大HP/MP) " + Config.DARKELF_MAX_HP + "/"+ Config.DARKELF_MAX_MP +"");
			System.out.println("** 龙骑士: (最大HP/MP) " + Config.DRAGONKNIGHT_MAX_HP + "/"+ Config.DRAGONKNIGHT_MAX_MP +"");
			System.out.println("** 幻术师: (最大HP/MP) " + Config.BLACKWIZARD_MAX_HP + "/"+ Config.BLACKWIZARD_MAX_MP +"");
			System.out.println("** 狂战士: (最大HP/MP) " + Config.전사_MAX_HP + "/"+ Config.전사_MAX_MP +"");
			System.out.println("** 等级限制: " + Config.LIMITLEVEL + "");
			System.out.println("** 防具限制: -" + Config.ACLEVEL + "");
			System.out.println("");
			System.out.println("** 其他设置");
			System.out.println("** 彩票状态: " + Config.LOTTO + "");
			System.out.println("** 彩票参与等级: " + Config.LOTTO_LEVEL + "");
			System.out.println("** 彩票投注金额: " + Config.LOTTO_BATTING + "");
			System.out.println("** 彩票奖金: " + Config.LOTTO_BONUS + " = (在线人数 × 投注金额)");
			System.out.println("** 彩票开奖时间: " + Config.LOTTO_TIME + "");
			System.out.println("** 是否无限钓鱼: " + Config.INFINIE_FISHING + "");
			System.out.println("** 角色槽数量: " + Config.Characters_CharSlot + "个");
			System.out.println("");
			System.out.println("********************************************************************");
			System.out.println("** IP   " + InetAddress.getLocalHost().getHostAddress() +"   端口 " + Config.GAME_SERVER_PORT + "");
			System.out.println("********************************************************************");
		} catch (Exception localException) {
		}
	}

	private void initDBFactory() {// L1DatabaseFactory 初始设置
		L1DatabaseFactory.setDatabaseSettings(Config.DB_DRIVER, Config.DB_URL, Config.DB_LOGIN, Config.DB_PASSWORD);
		try {
			L1DatabaseFactory.getInstance();
		} catch(Exception e) {};
	}

}
