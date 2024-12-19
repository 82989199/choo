package l1j.server.MJTemplate.DateSchedulerModel.Acceptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJActionListener.Npc.NpcActionListener;
import l1j.server.MJActionListener.Npc.TeleporterActionListener;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;

public class AcceptorToNpcListenerAdapter extends AbstractDateAcceptor implements ActionListener{
	public static int parse_remian_type(String s){
		switch(s){
		case "시":
			return Calendar.HOUR_OF_DAY;
		case "분":
			return Calendar.MINUTE;
		case "초":
			return Calendar.SECOND;
		default:
			throw new IllegalArgumentException(String.format("invalid remain type %s", s));
		}
	}
	
	private static Integer[] parse_opened_date(String s){
		return (Integer[])MJArrangeParser.parsing(s, ",", MJArrangeParseeFactory.createIntArrange()).result();
	}
	
	public static AcceptorToNpcListenerAdapter newInstance(NpcActionListener listener, ResultSet rs) throws SQLException{
		return (AcceptorToNpcListenerAdapter) newInstance()
				.set_opened_message(rs.getString("opened_message"))
				.set_closed_message(rs.getString("closed_message"))
				.setActionListener(listener)
				.set_is_opened(false)
				.add_include_day_week(parse_opened_date(rs.getString("opened_date")))
				.set_opened_time(rs.getInt("opened_hour"), rs.getInt("opened_minute"), rs.getInt("opened_second"))
				.set_remain_time(rs.getInt("remain_time"))
				.set_remain_type(parse_remian_type(rs.getString("remain_type")))
				.do_register();
	}
	
	public static AcceptorToNpcListenerAdapter newInstance(){
		return new AcceptorToNpcListenerAdapter();
	}
	
	private NpcActionListener _listener;
	private String	_opened_message;
	private String _closed_message;
	private AcceptorToNpcListenerAdapter(){
		
	}
	
	public AcceptorToNpcListenerAdapter set_opened_message(String opened_message){
		_opened_message = opened_message;
		return this;
	}
	
	public String get_opened_message(){
		return _opened_message;
	}
	
	public AcceptorToNpcListenerAdapter set_closed_message(String closed_message){
		_closed_message = closed_message;
		return this;
	}
	
	public String get_closed_message(){
		return _closed_message;
	}
	
	@Override
	public ActionListener deep_copy(ActionListener listener){
		return ((NpcActionListener) _listener.deep_copy(listener)).set_opened(false);
	}
	
	@Override
	public ActionListener deep_copy(){
		return ((NpcActionListener) _listener.deep_copy()).set_opened(false);
	}
	
	@Override
	public ActionListener drain(ActionListener listener) {
		return _listener.drain(listener);
	}
	
	public AcceptorToNpcListenerAdapter deep_copy(ResultSet rs) throws SQLException{
		return (AcceptorToNpcListenerAdapter) newInstance()
				.set_opened_message(rs.getString("opened_message"))
				.set_closed_message(rs.getString("closed_message"))
				.setActionListener((NpcActionListener) _listener.deep_copy())
				.set_is_opened(false)
				.add_include_day_week(parse_opened_date(rs.getString("opened_date")))
				.set_opened_time(rs.getInt("opened_hour"), rs.getInt("opened_minute"), rs.getInt("opened_second"))
				.set_remain_time(rs.getInt("remain_time"))
				.set_remain_type(parse_remian_type(rs.getString("remain_type")))
				.do_register();
	}
	
	protected AcceptorToNpcListenerAdapter setActionListener(NpcActionListener listener){
		_listener = listener;
		return this;
	}
	
	@Override
	public boolean eqauls_action(String action) {
		return _listener.eqauls_action(action);
	}

	@Override
	public String to_action(L1PcInstance pc, L1Object target) {
		return _listener.to_action(pc, target);
	}

	@Override
	public AbstractDateAcceptor do_open(Calendar cal) {
		if(_opened_message != null){
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,_opened_message));
			L1World.getInstance().broadcastPacketToAll(_opened_message);
	        // 추가된 사운드 효과
	        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
	            if (pc.isPrivateShop() || !pc.isBossNotify() || !pc.is_world() || !pc.getMap().isEscapable())
	                continue;
	            pc.sendPackets(new S_SkillSound(pc.getId(), 385));
	        }
	    }
	    return this;
	}

	@Override
	public AbstractDateAcceptor do_close(Calendar cal) {
		if(_listener instanceof TeleporterActionListener){
			S_SystemMessage message = new S_SystemMessage(_closed_message == null ? String.format("%s이(가) 종료되었습니다.", ((TeleporterActionListener) _listener).get_destination_name()) : _closed_message);
			TeleporterActionListener listener = (TeleporterActionListener)_listener;
			short[] mapIds = listener.get_teleport_map_id();
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,_closed_message == null ? String.format("%s이(가) 종료되었습니다.", ((TeleporterActionListener) _listener).get_destination_name()) : _closed_message));
			GeneralThreadPool.getInstance().execute(new Runnable(){
				@Override
				public void run(){
					for(int i=0; i<2; ++i){
						for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
							if(pc == null)
								continue;
							
							if(i == 0)
								pc.sendPackets(message, false);
							boolean is_in = false;
							int mid = pc.getMapId();
							for(int mapId : mapIds){
								if(mid == mapId){
									is_in=true;
									break;
								}
							}
							if(is_in){
								if(i == 0){
									pc.start_teleport(33437, 32813, 4, pc.getHeading(), 169, true, false);
								}else{
									pc.do_simple_teleport(33437, 32813, 4);
								}
								pc.sendPackets(new S_SkillSound(pc.getId(), 386)); // 추가된 사운드 효과
							}
						}
					}
					message.clear();
				}
			});
		}
		return this;
	}
	
	@Override
	public boolean is_opened(){
		return _listener.is_opened();
	}
	
	@Override
	public boolean is_closed(){
		return !_listener.is_opened();
	}
	
	@Override
	public AbstractDateAcceptor set_is_opened(boolean is_opened){
		_listener.set_opened(is_opened);
		return this;
	}
	
	@Override
	public void dispose(){
		
		
		do_removed();
		if(is_opened())
			set_is_opened(false).do_close(null);
		set_opened_time(null).set_closed_cal(null);
		
		if(_listener != null){
			_listener.dispose();
			_listener = null;
		}
	}

	@Override
	public boolean is_action() {
		return _listener.is_action();
	}
}
