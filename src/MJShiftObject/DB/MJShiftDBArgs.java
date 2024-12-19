package MJShiftObject.DB;

import l1j.server.MJTemplate.MJPropertyReader;

public class MJShiftDBArgs {
	public String SERVER_IDENTITY;
	public String DRIVER_NAME;
	public String URL;
	public String USER_NAME;
	public String PASSWORD;
	public int MIN_POOL_SIZE;
	public int MAX_POOL_SIZE;
	
	public int CHARACTER_TRANSFER_ITEMID;
	public int MY_BATTLE_SERVER_QUIT_READY_SECONDS;
	public int MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS;
	public int BATTLE_SERVER_SEND_COUNT;
	
	public MJShiftDBArgs(String file_name){
		MJPropertyReader reader = new MJPropertyReader(file_name);
		try{
			SERVER_IDENTITY = reader.readString("ServerIdentity", "");
			DRIVER_NAME = reader.readString("DriverName", "");
			URL = reader.readString("URL", "");
			USER_NAME = reader.readString("UserName", "");
			PASSWORD = reader.readString("Password", "");
			MIN_POOL_SIZE = reader.readInt("MinPoolSize", "32");
			MAX_POOL_SIZE = reader.readInt("MaxPoolSize", "128");
			CHARACTER_TRANSFER_ITEMID = reader.readInt("CharacterTransferItemId", "4100209");
			MY_BATTLE_SERVER_QUIT_READY_SECONDS = reader.readInt("MyBattleServerQuitReadySeconds", "30");
			MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS = reader.readInt("MyBattleServerStoreReadySeconds", "10");
			BATTLE_SERVER_SEND_COUNT = reader.readInt("BattleServerSendCount", "50");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				reader.dispose();
				reader = null;
			}
		}
	}
}
