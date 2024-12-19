package l1j.server.MJTemplate.MJProto;

import java.util.HashMap;

import l1j.server.server.GameClient;
import l1j.server.server.utils.MJHexHelper;

public enum MJEProtoMessages {
	CS_CRAFT_LIST_ALL_REQ(0x0036, 								l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_LIST_ALL_REQ.newInstance()),
	SC_CRAFT_LIST_ALL_ACK(0x0037, 								l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.newInstance()),
	CS_CRAFT_ID_LIST_REQ(0x0038,  								l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_ID_LIST_REQ.newInstance()),
	SC_CRAFT_ID_LIST_ACK(0x0039,								l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.newInstance()),
	CS_CRAFT_MAKE_REQ(0x003A, 									l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ.newInstance()),
	SC_CRAFT_MAKE_ACK(0x003B, 									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_MAKE_ACK.newInstance()),
	SC_NOTIFICATION_MESSAGE_NOT(0x0040,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT.newInstance()),
	SC_SIEGE_ZONE_UPDATE_NOT(0x0042,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_ZONE_UPDATE_NOT.newInstance()),
	SC_SIEGE_INJURY_TIME_NOIT(0x004C,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SIEGE_INJURY_TIME_NOIT.newInstance()),
	CS_CRAFT_BATCH_REQ(0x005C, 									l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_BATCH_REQ.newInstance()),
	SC_CRAFT_BATCH_ACK(0x005D, 									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_BATCH_ACK.newInstance()),
	SC_SPELL_BUFF_NOTI(0x006E,									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.newInstance()),
	SC_CONNECT_HIBREEDSERVER_NOTI_PACKET(0x0071,				l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance()),
	CS_HIBREED_AUTH_REQ(0x0073,									l1j.server.MJTemplate.MJProto.MainServer_Client.CS_HIBREED_AUTH_REQ_PACKET.newInstance()),
	SC_HIBREED_AUTH_ACK_PACKET(0x0074,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HIBREED_AUTH_ACK_PACKET.newInstance()),
	SC_WORLD_PUT_OBJECT_NOTI(0x0077,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI.newInstance()),
	CS_ALCHEMY_DESIGN_REQ(0x007A,								l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ALCHEMY_DESIGN_REQ.newInstance()),
	SC_ALCHEMY_DESIGN_ACK(0x007B,								l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK.newInstance()),
	CS_ALCHEMY_MAKE_REQ(0x007C,									l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ALCHEMY_MAKE_REQ.newInstance()),
	SC_ALCHEMY_MAKE_ACK(0x007D,									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_MAKE_ACK.newInstance()),
	SC_BASECAMP_CHART_NOTI_PACKET(0x007F,						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_CHART_NOTI_PACKET.newInstance()),
	SC_ALCHEMY_EXTRA_INFO_NOTI(0x0080,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_EXTRA_INFO_NOTI.newInstance()),
	SC_BOX_ATTR_CHANGE_NOTI_PACKET(0x0081, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BOX_ATTR_CHANGE_NOTI_PACKET.newInstance()),
	SC_CHANGE_TEAM_NOTI_PACKET(0x0082, 							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET.newInstance()),
	SC_DISCONNECT_SOCKET_NOTI_PACKET(0x0083, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_DISCONNECT_SOCKET_NOTI_PACKET.newInstance()),
	SC_BASECAMP_POINTRANK_NOTI_PACKET(0x0085, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_POINTRANK_NOTI_PACKET.newInstance()),
	SC_THEBE_CAPTURE_INFO_NOTI_PACKET(0x0086, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_THEBE_CAPTURE_INFO_NOTI_PACKET.newInstance()),
	CS_TOP_RANKER_REQ(0x0087,									l1j.server.MJTemplate.MJProto.MainServer_Client.CS_TOP_RANKER_REQ.newInstance()),
	SC_TOP_RANKER_ACK(0x0088,									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_ACK.newInstance()),
	SC_TOP_RANKER_NOTI(0x0089,									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI.newInstance()),
	SC_NOTIFICATION_MESSAGE(0x013C, 							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE.newInstance()),
	SC_FATIGUE_NOTI(0x014E, 									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FATIGUE_NOTI.newInstance()),
	SC_PARTY_MEMBER_MARK_CHANGE_NOTI(0x0153, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_MARK_CHANGE_NOTI.newInstance()),
	SC_ALL_SPELL_PASSIVE_NOTI(0x0191, 							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALL_SPELL_PASSIVE_NOTI.newInstance()),
	SC_ADD_ACTIVE_SPELL_EX_NOTI(0x0192, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_ACTIVE_SPELL_EX_NOTI.newInstance()),
	SC_STAT_RENEWAL_INFO_NOTI(0x01e3,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_STAT_RENEWAL_INFO_NOTI.newInstance()),
	SC_ATTENDANCE_USER_DATA(0x0214, 							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA.newInstance()),
	SC_PARTY_OPERATION_RESULT_NOTI(0x021B, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_OPERATION_RESULT_NOTI.newInstance()),
	SC_EVENT_COUNTDOWN_NOTI_PACKET(0x021C,						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance()),
	SC_ATTENDANCE_BONUS_INFO_EXTEND(0x0220, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_INFO_EXTEND.newInstance()),
	SC_ATTENDANCE_USER_DATA_EXTEND(0x0221, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND.newInstance()),
	CS_ATTENDANCE_BONUS_REQUEST_EXTEND(0x0222, 					l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ATTENDANCE_BONUS_REQUEST_EXTEND.newInstance()),
	SC_ATTENDANCE_DATA_UPDATE_EXTEND(0x0223, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_DATA_UPDATE_EXTEND.newInstance()),
	SC_ATTENDANCE_BONUS_GROUP_INFO(0x0224, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_GROUP_INFO.newInstance()),
	SC_TEAM_EMBLEM_SWITCH_NOTI(0x022E, 							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_EMBLEM_SWITCH_NOTI.newInstance()),
	SC_ADD_COMPLETED_ACHIEVEMENT_BATCH(0x022F, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_COMPLETED_ACHIEVEMENT_BATCH.newInstance()),
	SC_ADD_CRITERIA_PROGRESS_BATCH(0x0230, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_CRITERIA_PROGRESS_BATCH.newInstance()),
	CS_COMPLETED_ACHIEVEMENT_REWARD_REQ(0x0233, 				l1j.server.MJTemplate.MJProto.MainServer_Client.CS_COMPLETED_ACHIEVEMENT_REWARD_REQ.newInstance()),
	SC_COMPLETED_ACHIEVEMENT_REWARD_ACK(0x0234, 				l1j.server.MJTemplate.MJProto.MainServer_Client.SC_COMPLETED_ACHIEVEMENT_REWARD_ACK.newInstance()),
	CS_ACHIEVEMENT_TELEPORT_REQ(0x0235, 						l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ACHIEVEMENT_TELEPORT_REQ.newInstance()),
	SC_ACHIEVEMENT_TELEPORT_ACk(0x0236, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ACHIEVEMENT_TELEPORT_ACk.newInstance()),
	SC_CRITERIA_UPDATE_NOTI(0x0237, 							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRITERIA_UPDATE_NOTI.newInstance()), 
	SC_ACHIEVEMENT_COMPLETE_NOTI(0x0238, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ACHIEVEMENT_COMPLETE_NOTI.newInstance()),
	SC_AVAILABLE_SPELL_CHANGE_NOTI(0x024A,						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_CHANGE_NOTI.newInstance()),
	SC_ADD_INVENTORY_NOTI(0x024C,								l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_ADD_INVENTORY_NOTI.newInstance()),
	SC_UPDATE_INVENTORY_NOTI(0x024D,							l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_UPDATE_INVENTORY_NOTI.newInstance()),
	SC_ARENA_PLAY_EVENT_NOTI(0x02DF,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_CHANGE_NOTI.newInstance()),								
	CS_USER_PLAY_INFO_REQ(0x0322, 								l1j.server.MJTemplate.MJProto.MainServer_Client.CS_USER_PLAY_INFO_REQ.newInstance()),
	SC_USER_PLAY_INFO_NOTI(0x0323, 								l1j.server.MJTemplate.MJProto.MainServer_Client.SC_USER_PLAY_INFO_NOTI.newInstance()),
	SC_MONSTER_BOOK_V2_INFO_NOTI(0x032A, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_INFO_NOTI.newInstance()),
	CS_MONSTER_BOOK_V2_REWARD_REQ(0x032B, 						l1j.server.MJTemplate.MJProto.MainServer_Client.CS_MONSTER_BOOK_V2_REWARD_REQ.newInstance()),
	SC_MONSTER_BOOK_V2_REWARD_ACK(0x032C, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.newInstance()),
	SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI(0x032D,				l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI.newInstance()),
	SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI(0x032E,				l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI.newInstance()),
	CS_MONSTER_BOOK_V2_TELEPORT_REQ(0x032F, 					l1j.server.MJTemplate.MJProto.MainServer_Client.CS_MONSTER_BOOK_V2_TELEPORT_REQ.newInstance()),
	SC_MONSTER_BOOK_V2_TELEPORT_ACK(0x0330, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_TELEPORT_ACK.newInstance()),
	SC_MESSAGE_BOX(0x0333, 										l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MESSAGE_BOX.newInstance()),
	CS_CLIENT_VERSION_INFO(0x0334,								l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CLIENT_VERSION_INFO.newInstance()),
	SC_SERVER_VERSION_INFO(0x0335, 								l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SERVER_VERSION_INFO.newInstance()),
	SC_TEAM_ID_SERVER_NO_MAPPING_INFO(0x0336, 					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_ID_SERVER_NO_MAPPING_INFO.newInstance()),
	SC_PARTY_MEMBER_LIST(0x0337, 								l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST.newInstance()),
	SC_PARTY_MEMBER_LIST_CHANGE(0x0338, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_LIST_CHANGE.newInstance()),
	SC_PARTY_MEMBER_STATUS(0x0339, 								l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_MEMBER_STATUS.newInstance()),
	SC_HYPERTEXT(0x033A, 										l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HYPERTEXT.newInstance()),
	SC_PARTY_SYNC_PERIODIC_INFO(0x033B, 						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_PARTY_SYNC_PERIODIC_INFO.newInstance()),
	CS_PARTY_CONTROL_REQ(0x033C,								l1j.server.MJTemplate.MJProto.MainServer_Client.CS_PARTY_CONTROL_REQ.newInstance()),
	SC_SPECIAL_RESISTANCE_NOTI(0x03F7,							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.newInstance()),
	CS_ATTENDANCE_REWARD_REQ(0x03EE, 							l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ATTENDANCE_REWARD_REQ.newInstance()),
	SC_ATTENDANCE_REWARD_ACK(0x03EF, 							l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ACK.newInstance()),
	SC_ATTENDANCE_STATUS_INFO_NOTI(0x03F0,						l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_STATUS_INFO_NOTI.newInstance()),
	SC_REST_EXP_INFO_NOTI(0x03FC, 								l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI.newInstance()),
	CS_MY_RANKING_REQ(0x03FD, 									l1j.server.MJTemplate.MJProto.MainServer_Client.CS_MY_RANKING_REQ.newInstance()),
	SC_MY_RANKING_ACK(0x03FE, 									l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MY_RANKING_ACK.newInstance()),
	SC_PERSONAL_SHOP_ITEM_LIST_NOTI(0x0407, 					l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI.newInstance()),
	SC_WAREHOUSE_ITEM_LIST_NOTI(0x0408, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI.newInstance()),
	SC_EXCHANGE_ITEM_LIST_NOTI(0x0409, 							l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_EXCHANGE_ITEM_LIST_NOTI.newInstance()),
	SC_COMPANION_STATUS_NOTI(0x07D0, 							l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_STATUS_NOTI.newInstance()),
	CS_COMPANION_NAME_CHANGE_REQ(0x07D1, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_NAME_CHANGE_REQ.newInstance()),
	SC_COMPANION_NAME_CHANGE_ACK(0x07D2, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_NAME_CHANGE_ACK.newInstance()),
	CS_COMPANION_STAT_INCREASE_REQ(0x07D3, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_STAT_INCREASE_REQ.newInstance()),
	CS_COMPANION_TM_COMMAND_REQ(0x07D4, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_TM_COMMAND_REQ.newInstance()),
	SC_COMPANION_BUFF_NOTI(0x07D5, 								l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_BUFF_NOTI.newInstance()),
	SC_COMPANION_SKILL_NOTI(0x07D6, 							l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_NOTI.newInstance()),
	CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ(0x07D7, 				l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_SKILL_NEXT_TIER_OPEN_REQ.newInstance()),
	CS_COMPANION_SKILL_ENCHANT_REQ(0x07D8, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.CS_COMPANION_SKILL_ENCHANT_REQ.newInstance()),
	SC_COMPANION_SKILL_ENCHANT_ACK(0x07D9, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_SKILL_ENCHANT_ACK.newInstance()),
	SC_COMPANION_COMBAT_DATA_NOTI(0x07DA, 						l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_COMBAT_DATA_NOTI.newInstance());
	private int value;
	private MJIProtoMessage message;
	MJEProtoMessages(int value, MJIProtoMessage message){
		this.value = value;
		this.message = message;
	}
	
	public int toInt(){
		return value;
	}
	
	public boolean equals(MJEProtoMessages v){
		return value == v.value;
	}
	
	public MJIProtoMessage getMessageInstance(){
		return message;
	}
	
	public void reloadMessage(){
		MJIProtoMessage tmp = message;
		message = message.reloadInstance();
		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	public MJIProtoMessage copyInstance(){
		return message == null ? null : message.copyInstance();
	}
	
	private static final HashMap<Integer, MJEProtoMessages> messages;
	static{
		MJEProtoMessages[] msgs = MJEProtoMessages.values();
		messages = new HashMap<Integer, MJEProtoMessages>(msgs.length);
		for(MJEProtoMessages m : msgs)
			messages.put(m.toInt(), m);
	}
	
	public static MJEProtoMessages fromInt(int messageId){
		return messages.get(messageId);
	}
	
	public static boolean existsProto(GameClient clnt, byte[] bytes){
		int messageId = bytes[1] & 0xff | bytes[2] << 8 & 0xff00;
	//	System.out.println(MJHexHelper.toString(bytes, bytes.length));
		MJEProtoMessages message = fromInt(messageId);
		if(message == null)
			return false;
		
		MJIProtoMessage iMessage = message.message.copyInstance().readFrom(clnt, bytes);
		if(!iMessage.isInitialized())
			printNotInitialized(clnt.getActiveChar() == null ? clnt.getIp() : clnt.getActiveChar().getName(), messageId, iMessage.getInitializeBit());
		iMessage.dispose();
		return true;
	}
	
	public static void printNotInitialized(String ownerInfo, int messageId, long bit){
		System.out.println(createNotInitializedMessage(ownerInfo, messageId, bit));
	}
	
	public static String createNotInitializedMessage(String ownerInfo, int messageId, long bit){
		return String.format("MJProto `It was not initialized.` ownerInfo : %s, messageid : %s, initializeBit : %08X", ownerInfo, fromInt(messageId).name(), bit);
	}
	
	public static void getInstance(){
		//System.out.println("MJEProtoMessages Initialized.");
	}
}
