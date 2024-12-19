package l1j.server.server.Controller;

import java.util.Date;
import java.util.Iterator;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Builder.MJLiftGateBuilder;
import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
import l1j.server.MJTemplate.Interface.MJMonsterTransformHandler;
import l1j.server.MJTemplate.L1Instance.MJL1LiftGateInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.EventTimeTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.L1SpawnUtil;

public class EventThread implements Runnable {

	private static EventThread instance;

	public static EventThread getInstance() {
		if (instance == null) {
			instance = new EventThread();
		}
		return instance;
	} 

	public static MJL1LiftGateInstance _leftBDoor;
	public static MJL1LiftGateInstance _rightBDoor;
	public static MJL1LiftGateInstance _centerBDoor;

	private EventThread(){
		if(_leftBDoor == null){
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(16115);			
			_leftBDoor = builder.build(32731, 32852, (short)15404, false, 3);
			_leftBDoor.down();
			_rightBDoor =builder.build(32731, 32878, (short)15404, false, 3);
			_rightBDoor.down();
			_centerBDoor = builder.build(16116,32718, 32863, (short)15404, true, 5);
			_centerBDoor.down();
		}
	}
	
	private static void on(int id){
		GeneralThreadPool.getInstance().schedule(new BalogDoorController(id), 30000L);
	}
	
	public static class BalogDoorController implements Runnable{
		private int _id;
		BalogDoorController(int ownerId){
			_id = ownerId;
		}
		
		@Override
		public void run(){
			L1MonsterInstance m = (L1MonsterInstance)L1World.getInstance().findObject(_id);
			if(m == null || m.isDead()){
				_leftBDoor.down();
				_rightBDoor.down();
				_centerBDoor.down();
				return;
			}
				
			try{
				if(MJRnd.isBoolean()){
					_leftBDoor.takeClose(10000L);
					_rightBDoor.up();
					_centerBDoor.up();
				}else if(MJRnd.isBoolean()){
					_leftBDoor.up();
					_rightBDoor.takeClose(10000L);
					_centerBDoor.up();
				}else{
					_leftBDoor.up();
					_rightBDoor.up();
					_centerBDoor.takeClose(10000L);
				}
			}finally{
				GeneralThreadPool.getInstance().schedule(this, 30000L);
			}
		}
	}
	
	private void start_event_boss() {
		try {
			Iterator<L1NpcInstance> npc_iter = EventTimeTable.getInstance().get_npc_iter();
			L1NpcInstance npc = null;

			while (npc_iter.hasNext()) {
				npc = npc_iter.next();
				if (npc == null) {
					continue;
				}
				if (get_boss_spawn_time(npc.get_boss_hour(), npc.get_boss_minute())) {
					L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, npc.get_boss_msg().toString()));
					//이벤트 알림에서 수호탑은 제외시킨다
					if (npc.getNpcId() == 81111 || npc.getNpcId() == 8500129  || npc.getNpcId() == 5136 || npc.getNpcId() == 5135 || npc.getNpcId() == 707026 || npc.getNpcId() == 81163) {
						continue;
					}
					// TODO NPC가 생성후 삭제되는 시간 1시간
					L1NpcInstance realNpc = L1SpawnUtil.Gmspawn(npc.getNpcId(), npc.getHomeX(), npc.getHomeY(), npc.getMapId(), npc.getHeading(), 3600 * 1000);
					if (npc.getNpcId() == 45752) {
						_leftBDoor.up();
						_rightBDoor.up();
						L1MonsterInstance m = (L1MonsterInstance) realNpc;
						m.setTransformHandler(new MJMonsterTransformHandler() {
							@Override
							public void onTransFormNotify(L1MonsterInstance m) {
								_centerBDoor.up();
								on(m.getId());
							}
						});
						m.setDeathHandler(new MJMonsterDeathHandler() {
							@Override
							public boolean onDeathNotify(L1MonsterInstance m) {
								_leftBDoor.down();
								_rightBDoor.down();
								_centerBDoor.down();
								return false;
							}
						});
					}
				}
				/*if (get_boss_spawn_time((npc.get_boss_hour() + 1), npc.get_boss_minute())) {
					npc.set_boss_time(npc.get_boss_time() + 86400);
					Iterator<L1PcInstance> pc_iter = L1World.getInstance().getAllPlayers().iterator();
					L1PcInstance pc = null;

					while (pc_iter.hasNext()) {
						pc = pc_iter.next();
						if (pc == null) {
							continue;
						} 
						pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EVENT_SYSTEM, npc.getNpcId(), true));
					}
				}*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 시간 불러오기 
	 * @param h
	 * @param m
	 * @return
	 */
	private boolean get_boss_spawn_time(int h, int m) { 
		Date set = new Date(System.currentTimeMillis());
		int hour = set.getHours();
		int minute = set.getMinutes();
		if (hour == h && minute == m) {
			return true;
		}
		return false;
	}

	/**
	 * 컨트롤러가 1분마다 한번씩 호출됨.
	 */
	@Override
	public void run() {
		try{
			start_event_boss(); 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			GeneralThreadPool.getInstance().schedule(this, 60000L);
		}
	}

}
