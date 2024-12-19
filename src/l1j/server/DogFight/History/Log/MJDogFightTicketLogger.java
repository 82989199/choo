package l1j.server.DogFight.History.Log;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;

public class MJDogFightTicketLogger {
	private static final String SELLING_TABLE_NAME = "dogfight_log_selling";
	private static final String BUYING_TABLE_NAME = "dogfight_log_buying";

	public static void do_update(ArrayList<MJDogFightTicketLogInfo> logs_info, boolean is_selling){
		Updator.batch(
				String.format("insert into %s set action_date=?, character_object_id=?, character_name=?, item_id=?, item_name=?, count=?, price=?, total_price=?", 
						is_selling ? SELLING_TABLE_NAME : BUYING_TABLE_NAME), 
				new BatchHandler(){
					@Override
					public void handle(PreparedStatement pstm, int callNumber) throws Exception {
						MJDogFightTicketLogInfo lInfo = logs_info.get(callNumber);
						int idx = 0;
						pstm.setString(++idx, lInfo.get_action_date());
						pstm.setInt(++idx, lInfo.get_character_object_id());
						pstm.setString(++idx, lInfo.get_character_name());
						pstm.setInt(++idx, lInfo.get_item_id());
						pstm.setString(++idx, lInfo.get_item_name());
						pstm.setInt(++idx, lInfo.get_count());
						pstm.setInt(++idx, lInfo.get_price());
						pstm.setInt(++idx, lInfo.get_total_price());
					}
				}, logs_info.size());
	}
}
