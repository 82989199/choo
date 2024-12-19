package l1j.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class GrangKinConfig {
	/** 格兰肯的愤怒 **/
	public static boolean GRANG_KIN_ANGER_SYSTEM_USE;
	public static int GRANG_KIN_ANGER_ONE_STEP_TIME;
	public static int GRANG_KIN_ANGER_TWO_STEP_TIME;
	public static int GRANG_KIN_ANGER_THREE_STEP_TIME;
	public static int GRANG_KIN_ANGER_FOUR_STEP_TIME;
	public static int GRANG_KIN_ANGER_FIVE_STEP_TIME;
	public static int GRANG_KIN_ANGER_SIX_STEP_TIME;
	
	public static int GRANG_KIN_ANGER_ONE_STEP_LOGIN_TIME;
	public static int GRANG_KIN_ANGER_TWO_STEP_LOGIN_TIME;
	public static int GRANG_KIN_ANGER_THREE_STEP_LOGIN_TIME;
	public static int GRANG_KIN_ANGER_FOUR_STEP_LOGIN_TIME;
	public static int GRANG_KIN_ANGER_FIVE_STEP_LOGIN_TIME;
	public static int GRANG_KIN_ANGER_SIX_STEP_LOGIN_TIME;
	
	public static int GRANG_KIN_ANGER_ONE_STEP_LOGOUT_TIME;
	public static int GRANG_KIN_ANGER_TWO_STEP_LOGOUT_TIME;
	public static int GRANG_KIN_ANGER_THREE_STEP_LOGOUT_TIME;
	public static int GRANG_KIN_ANGER_FOUR_STEP_LOGOUT_TIME;
	public static int GRANG_KIN_ANGER_FIVE_STEP_LOGOUT_TIME;
	public static int GRANG_KIN_ANGER_SIX_STEP_LOGOUT_TIME;	
	

	/** Configuration files */
	public static final String SERVER_CONFIG_FILE = "./config/grangkinsetting.properties";


	public static void load() {
		// _log.info("loading gameserver config");
		// System.out.println("loading gameserver config");

		// TODO server.properties 셋팅
		try {
			Properties serverSettings = new Properties();
			InputStream is = new FileInputStream(new File(SERVER_CONFIG_FILE));
			serverSettings.load(is);
			is.close();

			GRANG_KIN_ANGER_SYSTEM_USE = Boolean.parseBoolean(serverSettings.getProperty("GrangKinAngerSystemUse", "false"));
			GRANG_KIN_ANGER_ONE_STEP_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerOneStepTime", "180"));
			GRANG_KIN_ANGER_TWO_STEP_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerTwoStepTime", "300"));
			GRANG_KIN_ANGER_THREE_STEP_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerThreeStepTime", "360"));
			GRANG_KIN_ANGER_FOUR_STEP_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerFourStepTime", "420"));
			GRANG_KIN_ANGER_FIVE_STEP_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerFiveStepTime", "480"));
			GRANG_KIN_ANGER_SIX_STEP_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerSixStepTime", "600"));
			
			GRANG_KIN_ANGER_ONE_STEP_LOGIN_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerOneStepLoginTime", "60"));
			GRANG_KIN_ANGER_TWO_STEP_LOGIN_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerTwoStepLoginTime", "120"));
			GRANG_KIN_ANGER_THREE_STEP_LOGIN_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerThreeStepLoginTime", "180"));
			GRANG_KIN_ANGER_FOUR_STEP_LOGIN_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerFourStepLoginTime", "240"));
			GRANG_KIN_ANGER_FIVE_STEP_LOGIN_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerFiveStepLoginTime", "300"));
			GRANG_KIN_ANGER_SIX_STEP_LOGIN_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerSixStepLoginTime", "360"));
			
			GRANG_KIN_ANGER_ONE_STEP_LOGOUT_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerOneStepLogOutTime", "120"));
			GRANG_KIN_ANGER_TWO_STEP_LOGOUT_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerTwoStepLogOutTime", "180"));
			GRANG_KIN_ANGER_THREE_STEP_LOGOUT_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerThreeStepLogOutTime", "240"));
			GRANG_KIN_ANGER_FOUR_STEP_LOGOUT_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerFourStepLogOutTime", "300"));
			GRANG_KIN_ANGER_FIVE_STEP_LOGOUT_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerFiveStepLogOutTime", "360"));
			GRANG_KIN_ANGER_SIX_STEP_LOGOUT_TIME = Integer.parseInt(serverSettings.getProperty("GrangKinAngerSixStepLogOutTime", "420"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Failed to Load " + SERVER_CONFIG_FILE + " File.");
		}
	}

	private GrangKinConfig() {
	}
}
