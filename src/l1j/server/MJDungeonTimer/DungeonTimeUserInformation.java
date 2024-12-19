package l1j.server.MJDungeonTimer;

import java.util.HashMap;

import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJDungeonTimer.Progress.AccountTimeProgress;
import l1j.server.MJDungeonTimer.Progress.CharacterTimeProgress;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EVENT_COUNTDOWN_NOTI_PACKET;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;

public class DungeonTimeUserInformation {
	public static DungeonTimeUserInformation newInstance(){
		return new DungeonTimeUserInformation();
	}
	
	private HashMap<Integer, DungeonTimeProgress<?>> _character_progresses;
	private HashMap<Integer, DungeonTimeProgress<?>> _account_progresses;
	private DungeonTimeUserInformation(){
		_character_progresses = new HashMap<Integer, DungeonTimeProgress<?>>(8);
		_account_progresses = new HashMap<Integer, DungeonTimeProgress<?>>(8);
	}
	
	public void put_dungeon_progress(int timer_id, CharacterTimeProgress progress){
		_character_progresses.put(timer_id, progress);
	}
	
	public void put_dungeon_progress(int timer_id, AccountTimeProgress progress){
		_account_progresses.put(timer_id, progress);		
	}
	
	public DungeonTimeProgress<?> remove_dungeon_progress(int timer_id){
		DungeonTimeProgress<?> p = _character_progresses.remove(timer_id);
		if(p == null)
			p = _account_progresses.remove(timer_id);
		return p;
	}
	
	public HashMap<Integer, DungeonTimeProgress<?>> get_character_progresses(){
		return _character_progresses;
	}
	
	public HashMap<Integer, DungeonTimeProgress<?>> get_account_progresses(){
		return _account_progresses;
	}
	
	public DungeonTimeProgress<?> get_progress(DungeonTimeInformation dtInfo){
		if(dtInfo.get_is_account_share()){
			return _account_progresses.get(dtInfo.get_timer_id());
		}
		return _character_progresses.get(dtInfo.get_timer_id());
	}
	
	public DungeonTimeProgress<?> get_progress(int timer_id){
		DungeonTimeProgress<?> progress =  _account_progresses.get(timer_id);
		if(progress == null)
			progress =  _character_progresses.get(timer_id);
		return progress;
	}
	
	public void dec_dungeon_progress(L1PcInstance pc, DungeonTimeInformation dtInfo){
		int timer_id = dtInfo.get_timer_id();
		HashMap<Integer, DungeonTimeProgress<?>> progresses = dtInfo.get_is_account_share() ? _account_progresses : _character_progresses;
		DungeonTimeProgress<?> progress = progresses.get(timer_id);
		if(progress == null){
			progress = dtInfo.to_progress(pc);
			progresses.put(timer_id, progress);
			DungeonTimeProgressLoader.update(progress);
		}
		
		if(progress.dec_remain_seconds() <= 0){
			pc.do_simple_teleport(MJCopyMapObservable.RESET_X, MJCopyMapObservable.RESET_Y, MJCopyMapObservable.RESET_MAPID);
			pc.sendPackets(String.format("%s 사용 시간이 만료되었습니다.", dtInfo.get_description()));
		}
	}
	
	public void send_dungeon_progress(L1PcInstance pc, DungeonTimeInformation dtInfo){
		int timer_id = dtInfo.get_timer_id();
		HashMap<Integer, DungeonTimeProgress<?>> progresses = dtInfo.get_is_account_share() ? _account_progresses : _character_progresses;
		DungeonTimeProgress<?> progress = progresses.get(timer_id);
		if(progress == null){
			progress = dtInfo.to_progress(pc);
			progresses.put(timer_id, progress);
			DungeonTimeProgressLoader.update(progress);
		}
		//SC_EVENT_COUNTDOWN_NOTI_PACKET.send(pc, progress.get_remain_seconds(), dtInfo.get_description());
		pc.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIMER, progress.get_remain_seconds()));
	}
	
	public void initialize(){
		_character_progresses.clear();
		_account_progresses.clear();
	}
}
