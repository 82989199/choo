package l1j.server.DogFight.History;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.IntRange;

public class MJDogFightTicketIdManager {
	public static final int START_TICKET_ITEM_ID = 12000000;
	private static MJDogFightTicketIdManager m_instance;
	public static MJDogFightTicketIdManager getInstance(){
		if(m_instance == null)
			m_instance = new MJDogFightTicketIdManager();
		return m_instance;
	}

	private int m_item_id;
	private int m_prev_item_id;
	private MJDogFightTicketIdManager(){
		do_load();
	}
	
	private void do_load(){
		Selector.exec("select * from dogfight_item_id", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					m_item_id = rs.getInt("last_item_id");
				}else{
					m_item_id = START_TICKET_ITEM_ID;
				}
			}
		});
		if(m_item_id == START_TICKET_ITEM_ID){
			do_update();
		}else{
			L1Item template = ItemTable.getInstance().getTemplate(MJDogFightSettings.TICKET_ITEM_ID);
			for(int i=START_TICKET_ITEM_ID; i<=m_item_id; ++i)
				ItemTable.getInstance().create_dogfight_ticket(template, i, String.format("#%d회차 투견티켓", ((START_TICKET_ITEM_ID - i + 2) / 2)));
		}
		m_prev_item_id = m_item_id - 1;
	}
	
	public void update_item_template(){
		L1Item template = ItemTable.getInstance().getTemplate(MJDogFightSettings.TICKET_ITEM_ID);
		for(int i=START_TICKET_ITEM_ID; i<=m_item_id; ++i)
			ItemTable.getInstance().create_dogfight_ticket(template, i, String.format("#%d회차 투견티켓", ((START_TICKET_ITEM_ID - i + 2) / 2)));
	}
	
	public int next_id(){
		m_prev_item_id = m_item_id - 1;
		int i = m_item_id++;
		do_update();
		return i;
	}
	
	public int[] next_ids(int amount){
		m_prev_item_id = m_item_id - 1;
		int[] ids = new int[amount];
		for(int i=0; i<amount; ++i)
			ids[i] = m_item_id++;
		do_update();
		return ids;
	}
	public int get_current_id(){
		return m_item_id;
	}
	public boolean is_ticket_item_id(int item_id){
		if(m_prev_item_id == START_TICKET_ITEM_ID - 1)
			return false;
		
		return IntRange.includes(item_id, START_TICKET_ITEM_ID, m_prev_item_id);
	}
	
	private void do_update(){
		Updator.exec("insert into dogfight_item_id set identity=?, last_date=?, last_item_id=? "
				+ "on duplicate key update "
				+ "last_date=?, last_item_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				String local_time = MJNSHandler.getLocalTime();
				int idx = 0;
				pstm.setInt(++idx, 0);
				pstm.setString(++idx, local_time);
				pstm.setInt(++idx, m_item_id);
				pstm.setString(++idx, local_time);
				pstm.setInt(++idx, m_item_id);
			}
		});
	}

}
