package MJShiftObject.Battle.Thebe;

import java.sql.ResultSet;
import java.util.ArrayList;

import MJShiftObject.Battle.MJShiftBattleArgs;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJThebeMessage {
	public static void do_message_test(final L1PcInstance pc){
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run(){
				MJThebeMessage message = new MJThebeMessage();
				int size = message.m_messages.size();
				for(int i=0; i<size; ++i){
					MessageInfo mInfo = message.m_messages.get(i);
					ProtoOutputStream stream = SC_NOTIFICATION_MESSAGE.make_stream(mInfo.message, mInfo.rgb, mInfo.duration);
					pc.sendPackets(stream);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("메시지 테스트가 완료되었습니다.", MJSimpleRgb.green(), 10));
			}
		});
	}
	
	private ArrayList<MessageInfo> m_messages;
	private int m_index;
	public MJThebeMessage(){
		m_index = -1;
		m_messages = new ArrayList<MessageInfo>();
		Selector.exec("select message, rgb, duration from server_battle_message_thebes", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MessageInfo mInfo = new MessageInfo();
					mInfo.message = rs.getString("message");
					mInfo.rgb = MJSimpleRgb.from_string(rs.getString("rgb"));
					mInfo.duration = rs.getInt("duration");
					m_messages.add(mInfo);
				}
			}
		});
	}
	
	public MessageInfo next_message(){
		int size = m_messages.size();
		if(size <= 0)
			return null;
		
		return MJShiftBattleArgs.THEBE_IS_AUTO_SEQUENCE_MESSAGE ?
			m_messages.get((++m_index) % size) :
			m_messages.get(MJRnd.next(size));
	}

	public class MessageInfo{
		public String message;
		public MJSimpleRgb rgb;
		public int duration;
	}
}
