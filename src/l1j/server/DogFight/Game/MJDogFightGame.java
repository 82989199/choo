package l1j.server.DogFight.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.DogFight.History.MJDogFightHistory;
import l1j.server.DogFight.History.MJDogFightTicketInfo;
import l1j.server.DogFight.Instance.MJDogFightInstance;
import l1j.server.DogFight.Instance.MJIFighterDeadNotifyHandler;
import l1j.server.DogFight.Instance.MJIFighterGameEndedHandler;
import l1j.server.MJCompanion.Basic.ClassInfo.MJCompanionClassInfo;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.MJMessengerInstance;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_NpcChatPacket;

public class MJDogFightGame implements MJIFighterDeadNotifyHandler{
	public static MJDogFightGame newInstance(MJDogFightGameInfo gInfo, MJIFighterGameEndedHandler ended_handler){
		MJDogFightGame game = newInstance();
		game.set_game_info(gInfo);
		game.set_ready_remain_seconds(gInfo.get_ready_seconds());
		game.set_ended_handler(ended_handler);
		ArrayList<MJDogFightInstance> instances = gInfo.create_fighters(MJCompanionClassInfo.get_shuffles_classes());
		for(MJDogFightInstance instance : instances)
			game.add_fighter(instance);
		return game;
	}
	
	private static MJDogFightGame newInstance(){
		return new MJDogFightGame();
	}
	
	private L1NpcInstance m_caster;
	private MJMessengerInstance m_p_caster;
	private MJMessengerInstance m_messenger;
	private int m_ready_remain_seconds;
	private boolean m_is_run;
	private boolean m_is_fight;
	private MJDogFightGameInfo m_game_info;
	private MJIFighterGameEndedHandler m_ended_handler;
	private Object m_fighter_dead_lock;
	private ArrayList<MJDogFightInstance> m_all_fighters;
	private static HashMap<Integer, ArrayList<MJDogFightInstance>> m_fighters;
	private HashMap<Integer, ArrayList<MJDogFightInstance>> m_down_fighters;
	private MJDogFightGame(){
		m_is_run = false;
		m_is_fight = false;
		m_all_fighters = new ArrayList<MJDogFightInstance>();
		m_fighters = new HashMap<Integer, ArrayList<MJDogFightInstance>>();
		m_down_fighters = new HashMap<Integer, ArrayList<MJDogFightInstance>>();
		m_caster = L1World.getInstance().findNpc(MJDogFightSettings.CASTER_NPC_ID);
		
		L1NpcInstance Pmessenger = L1World.getInstance().findNpc(MJDogFightSettings.P_CASTER_NPC_ID);
		if(Pmessenger != null && Pmessenger instanceof MJMessengerInstance)
			m_p_caster = (MJMessengerInstance)Pmessenger;
		
		
		L1NpcInstance messenger = L1World.getInstance().findNpc(MJDogFightSettings.MESSENGER_NPC_ID);
		if(messenger != null && messenger instanceof MJMessengerInstance)
			m_messenger = (MJMessengerInstance)messenger;
		m_fighter_dead_lock = new Object();
		if(m_caster == null)
			throw new IllegalArgumentException(String.format("[斗狗] 找不到施法者NPC。NPC ID：%d", MJDogFightSettings.CASTER_NPC_ID));
		// [투견] 캐스터npc를 찾을 수 없습니다. 엔피씨아이디

		if(m_p_caster == null)
			throw new IllegalArgumentException(String.format("[斗狗] 找不到施法者NPC。NPC ID：%d", MJDogFightSettings.P_CASTER_NPC_ID));
		// [투견] 캐스터npc를 찾을 수 없습니다. 엔피씨아이디

		m_ready_remain_seconds = 0;  // 准备剩余时间设为0
	}
	private void set_game_info(MJDogFightGameInfo game_info){
		m_game_info = game_info;
	}
	public MJDogFightGameInfo get_game_info(){
		return m_game_info;
	}
	private void set_ready_remain_seconds(int ready_remain_seconds){
		m_ready_remain_seconds = ready_remain_seconds;
	}
	private void set_ended_handler(MJIFighterGameEndedHandler ended_handler){
		m_ended_handler = ended_handler;
	}
	public void add_fighter(MJDogFightInstance fighter){
		ArrayList<MJDogFightInstance> fighters = m_fighters.get(fighter.get_corner_id());
		if(fighters == null){
			fighters = new ArrayList<MJDogFightInstance>();
			m_fighters.put(fighter.get_corner_id(), fighters);
		}
		m_all_fighters.add(fighter);
		fighters.add(fighter.set_dead_notify(this));
	}
	
	public static MJDogFightInstance get_leader(int corner_id){
		ArrayList<MJDogFightInstance> fighters = m_fighters.get(corner_id);
		if(fighters == null || fighters.size() <= 0)
			return null;
		return fighters.get(0);
	}
	
	public static MJDogFightInstance getDogFight(int i) {
		return get_leader(i);
	}
	
	public L1NpcInstance get_caster(){
		return m_caster;
	}
	public boolean is_run(){
		return m_is_run;
	}
	public boolean is_fight(){
		return m_is_fight;
	}
	
	public void set_fight(boolean is_fight){
		m_is_fight = is_fight;
	}



	/** 检查是否为平局 */
	public boolean isDraw(int corner_id){
		ArrayList<MJDogFightInstance> fighters = m_fighters.get(corner_id);
		if(fighters == null) return false;
		for(MJDogFightInstance fighter : fighters){
			if(fighter.isDead()== false && fighter.getCurrentHpPercent() < MJDogFightSettings.DRAW_RATE){
				/** 处理狗死亡的代码 */
				fighter.setDead(true);
				fighter.setStatus(ActionCodes.ACTION_Die);
				fighter.setCurrentHp(0);
				fighter.getMap().setPassable(fighter.getLocation(), true);
				fighter.send_action(ActionCodes.ACTION_Die);
			}
			
			if(fighter.isDead()== false)
				return false;
		}
		
		return true;
	}
	
	@Override
	public void on_dead_fighter(MJDogFightInstance dogfighter){
		try{
			ArrayList<MJDogFightInstance> winners = null;
			int select_corner_id = -1;
			synchronized(m_fighter_dead_lock){
				if(!is_run())
					return;
				
				ArrayList<MJDogFightInstance> fighters = m_fighters.get(dogfighter.get_corner_id());
				if(fighters == null)
					return;				
				for(MJDogFightInstance fighter : fighters){
					if(!fighter.isDead())
						return;
				}
				m_down_fighters.put(dogfighter.get_corner_id(), fighters);
				
				
				
				boolean isDraw = false;
				/** 检查是否为平局 */
				if(dogfighter.get_corner_id() == 0){
					isDraw = isDraw(1);
				}else {
					isDraw = isDraw(0);
				}
				/** 检查是否为平局 */
				if(isDraw == true){
					
					winners = null;
					select_corner_id = 2;// 무승부코드는 2번으로
					m_is_run = false;
					
					send_caster_message(
							String.format(
									MJDogFightSettings.DRAW_MESSAGE, 
									MJDogFightHistory.getInstance().get_current_game_number()));
					
					do_game_ended_trigger(select_corner_id);
					
					m_ended_handler.on_ended(select_corner_id, winners);
					
					return;
				}
				
				
				
				
				
				
				if(m_down_fighters.size() == m_fighters.size() - 1){
					for(Integer corner_id : m_fighters.keySet()){
						if(m_down_fighters.containsKey(corner_id))
							continue;
						
						m_is_run = false;
						winners = m_fighters.get(corner_id);
						select_corner_id = corner_id;
						break;
					}
				}
				
				
				
				
				
			}
			if(winners != null && select_corner_id != -1){
				send_caster_message(
						String.format(
								MJDogFightSettings.WINNER_MESSAGE, 
								MJDogFightHistory.getInstance().get_current_game_number(), 
								winners.get(0).get_corner_name()
						));
				do_game_ended_trigger(select_corner_id);
				m_ended_handler.on_ended(select_corner_id, winners);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void send_caster_message(String message){
		if(m_caster != null)
			m_caster.broadcastPacket(new S_NpcChatPacket(m_caster, message, 2));
	}
	
	public void send_p_caster_message(String message){
		if(m_p_caster != null){
			
			m_p_caster.set_current_message(message);
			m_p_caster.broadcast_message();		
		}
	}
	public void send_messenger_message(String message){
		if(m_messenger != null){
			m_messenger.set_current_message(message);
			m_messenger.broadcast_message();			
		}
	}
	
	private void send_start_message(){
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run(){
				try{
					send_caster_message(MJDogFightSettings.START_MESSAGE);
					Thread.sleep(1000L);
					send_caster_message(MJDogFightSettings.DIVIDEND_START_MESSAGE);
					Thread.sleep(1000L);
					for(MJDogFightTicketInfo tInfo : MJDogFightHistory.getInstance().get_current_tickets_info()){


						String name = "";
						if(tInfo.get_corner_id() == 2){
							name = "[무]";
						}else {
							name = get_leader(tInfo.get_corner_id()).getName();
						}
						
						
						send_caster_message(String.format(MJDogFightSettings.DIVIDEND_MESSAGE, name, tInfo.get_dividend()));
						Thread.sleep(1000L);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	public void execute(){
		m_is_run = true;
		send_p_caster_message("");
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run() {
				try{
					for(int i=m_ready_remain_seconds; i>=1; --i){
						if(!is_run())
							return;
						if(i % 60 == 0){
							send_caster_message(String.format(MJDogFightSettings.MESSAGE_BY_MINUTE, i / 60, m_game_info.get_game_name()));
							send_messenger_message(String.format(MJDogFightSettings.MESSENGER_MESSAGE_BY_MINUTE, i / 60));
						}
						if(i <= MJDogFightSettings.MESSENGER_REMAIN_MESSAGE_SECONDS){
							send_messenger_message(String.format(MJDogFightSettings.MESSENGER_REMAIN_MESSAGE, i));							
						}


						/** 赔率显示/输出 */
						try{
						double total_count  = MJDogFightHistory.getInstance().get_current_history().get_total_ticket_count();
						String Hmessage = "";
						String Jmessage = "";
						String Mmessage = "";
						for(MJDogFightTicketInfo tInfo : MJDogFightHistory.getInstance().get_current_tickets_info()){
							double per = 0.0f;
							if(tInfo.get_ticket_count() > 0 ){
								per = ((double)tInfo.get_ticket_count() / total_count) * 100.0d;
							}
							if(total_count  <= (MJDogFightSettings.DEFAULT_TICKET_COUNT * 3)){
								per = 0;
							}
							
							if(tInfo.get_corner_id() == 0){
								Hmessage = String.format("%.1f", per);
								//message += "[홀"+ String.format("%.2f", per)+"%]";
							}else if(tInfo.get_corner_id() == 2){
								Jmessage = String.format("%.1f", per);
								//message += " [짝"+ String.format("%.2f", per)+"%]";
							}else if(tInfo.get_corner_id() == 1){
								Mmessage = String.format("%.1f", per);
								//message += " [무"+ String.format("%.2f", per)+"%]";
							}
						}
						
						send_p_caster_message(String.format(MJDogFightSettings.PERCENT_MESSAGE, Hmessage,Jmessage,Mmessage));
						}catch(Exception e){}
						
						if(i <= m_game_info.get_ready_remain_message_cutline()){
							send_caster_message(String.format(MJDogFightSettings.REMAIN_MESSAGE, i));
						}
						if(i <= 3){
						}
						Thread.sleep(1000);
					}
					if(!is_run())
						return;
					
					m_is_fight = true;
					send_messenger_message(MJDogFightSettings.MESSENGER_SELL_CLOSING_MESSAGE);
					send_start_message();		
					do_game_start_trigger();
					for(Integer corner_id : m_fighters.keySet()){
						ArrayList<MJDogFightInstance> fighters = create_different_fighters(corner_id);
						for(MJDogFightInstance fighter : m_fighters.get(corner_id)){
							fighter.set_targets(fighters);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	private void do_three_seconds_trigger(int remain_seconds){
		ArrayList<MJDogFightInstance> fighters = get_shuffle_fighters();
		for(MJDogFightInstance fighter : fighters)
			fighter.on_three_seconds_trigger(remain_seconds);
	}
	
	private void do_game_start_trigger(){
		ArrayList<MJDogFightInstance> fighters = get_shuffle_fighters();
		for(MJDogFightInstance fighter : fighters)
			fighter.on_game_start_trigger();
	}
	
	private void do_game_ended_trigger(int winner_id){
		ArrayList<MJDogFightInstance> fighters = get_shuffle_fighters();
		for(MJDogFightInstance fighter : fighters){
			if(fighter.get_corner_id() == winner_id)
				fighter.on_game_win_trigger();
			else
				fighter.on_game_lose_trigger();
		}
	}
	
	private ArrayList<MJDogFightInstance> get_shuffle_fighters(){
		ArrayList<MJDogFightInstance> fighters = new ArrayList<MJDogFightInstance>(m_all_fighters);
		Collections.shuffle(fighters);
		return fighters;
	}
	
	private ArrayList<MJDogFightInstance> create_different_fighters(Integer corner_id){
		ArrayList<MJDogFightInstance> fighters = new ArrayList<MJDogFightInstance>();
		for(Integer c_id : m_fighters.keySet()){
			if(c_id == corner_id)
				continue;
			
			fighters.addAll(m_fighters.get(c_id));
		}
		return fighters;
	}
	
	public void dispose(){
		m_is_run = false;
		m_is_fight = false;
		if(m_fighters != null){
			for(ArrayList<MJDogFightInstance> fighters : m_fighters.values()){
				for(MJDogFightInstance fighter : fighters)
					fighter.deleteMe();
			}
		}
		m_fighters = null;
	}
}

