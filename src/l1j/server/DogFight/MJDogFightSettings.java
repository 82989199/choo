package l1j.server.DogFight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJSqlHelper.Property.MJIPropertyHandler;
import l1j.server.MJTemplate.MJSqlHelper.Property.MJSqlPropertyReader;

public class MJDogFightSettings {
	public static int CASTER_NPC_ID;
	public static int P_CASTER_NPC_ID;
	public static int MESSENGER_NPC_ID;
	public static String REMAIN_MESSAGE;
	public static String START_MESSAGE;
	public static int TICKET_PRICE;
	public static int TICKET_ITEM_ID;
	public static String DIVIDEND_START_MESSAGE;
	public static String DIVIDEND_MESSAGE;
	public static String WINNER_MESSAGE;
	public static String DRAW_MESSAGE;
	public static String TICKET_NAME;
	public static int DEFAULT_TICKET_COUNT;
	public static int NEXT_MATCH_READY_SECONDS;
	public static boolean IS_ON_RUN;
	public static ArrayList<Integer> IS_ON_GAMES;
	public static int ON_SKILL_PERCENT;
	public static String NEXT_GAME_MESSAGE;
	public static String MESSAGE_BY_MINUTE;
	public static double EXCHANGE_RATE;
	public static double DRAW_RATE;
	public static double LIMIT_DIVIDEND;
	
	public static String PERCENT_MESSAGE;
	
	public static String MESSENGER_MESSAGE_BY_MINUTE;
	public static String MESSENGER_REMAIN_MESSAGE;
	public static int MESSENGER_REMAIN_MESSAGE_SECONDS;
	public static String MESSENGER_SELL_CLOSING_MESSAGE;
	
	public static int FAKE_HIT;
	public static int FAKE_DAMAGE;
	public static int FAKE_SKILL_RATE;
	
	public static int MILEAGE_ITEM;
	public static double MILEAGE_PERCENT;
	
	public static void do_load(){
		MJSqlPropertyReader.do_exec("dogfight_settings", "section", "val", new MJIPropertyHandler(){
			@Override
			public void on_load(String section, MJSqlPropertyReader reader, ResultSet rs) throws SQLException {
				switch(section){
				case "CasterNpcId":
					CASTER_NPC_ID = reader.read_int(rs);
					break;
				case "PercentCasterNpcId":
					P_CASTER_NPC_ID = reader.read_int(rs);
					break;					
				case "PercentMessage":
					PERCENT_MESSAGE = reader.read_string(rs);
					break;
				case "DrawRate":
					DRAW_RATE = reader.read_double(rs);
					break;
					
				case "MileageItem":
					MILEAGE_ITEM = reader.read_int(rs);
					break;
				case "MileagePercent":
					MILEAGE_PERCENT = reader.read_double(rs);
					break;
					
					
					
				case "MessengerNpcId":
					MESSENGER_NPC_ID = reader.read_int(rs);
					break;
				case "RemainMessage":
					REMAIN_MESSAGE = reader.read_string(rs);
					break;
				case "StartMessage":
					START_MESSAGE = reader.read_string(rs);
					break;
				case "TicketPrice":
					TICKET_PRICE = reader.read_int(rs);
					break;
				case "TicketItemId":
					TICKET_ITEM_ID = reader.read_int(rs);
					break;
				case "DividendStartMessage":
					DIVIDEND_START_MESSAGE = reader.read_string(rs);
					break;
				case "DividendMessage":
					DIVIDEND_MESSAGE = reader.read_string(rs);
					break;
				case "WinnerMessage":
					WINNER_MESSAGE = reader.read_string(rs);
					break;
				case "DrawMessage":
					DRAW_MESSAGE = reader.read_string(rs);
					break;
				case "TicketName":
					TICKET_NAME = reader.read_string(rs);
					break;
				case "DefaultTicketCount":
					DEFAULT_TICKET_COUNT = reader.read_int(rs);
					break;
				case "NextMatchReadySeconds":
					NEXT_MATCH_READY_SECONDS = reader.read_int(rs);
					break;
				case "IsOnRun":
					IS_ON_RUN = reader.read_boolean(rs);
					break;
				case "IsOnGames":
					IS_ON_GAMES = reader.read_int_array(rs, "\\,");
					break;
				case "OnKillPercent":
					ON_SKILL_PERCENT = reader.read_int(rs);
					break;
				case "NextGameMessage":
					NEXT_GAME_MESSAGE = reader.read_string(rs);
					break;
				case "MessageByMinute":
					MESSAGE_BY_MINUTE = reader.read_string(rs);
					break;
				case "ExchangeRate":
					EXCHANGE_RATE = reader.read_double_by_percent(rs);
					break;
				case "LimitDividend":
					LIMIT_DIVIDEND = reader.read_double(rs);
					break;
				case "MessengerMessageByMinute":
					MESSENGER_MESSAGE_BY_MINUTE = reader.read_string(rs);
					break;
				case "MessengerRemainMessage":
					MESSENGER_REMAIN_MESSAGE = reader.read_string(rs);
					break;
				case "MessengerRemainMessageSeconds":
					MESSENGER_REMAIN_MESSAGE_SECONDS = reader.read_int(rs);
					break;
				case "MessengerSellClosingMessage":
					MESSENGER_SELL_CLOSING_MESSAGE = reader.read_string(rs);
					break;
				case "Fake_Hit":
					FAKE_HIT = reader.read_int(rs);
					break;
				case "Fake_Damage":
					FAKE_DAMAGE = reader.read_int(rs);
					break;
				case "Fake_Skill_Rate":
					FAKE_SKILL_RATE = reader.read_int(rs);
					break;
					
				}
			}
		});
	}
}
