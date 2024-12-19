package l1j.server.MJBotSystem.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import l1j.server.MJBotSystem.MJBotBrain;
import l1j.server.MJBotSystem.MJBotInvItem;
import l1j.server.MJBotSystem.MJBotSpell;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.Business.MJAIScheduler;
import l1j.server.MJBotSystem.Business.MJBotLastError;
import l1j.server.MJBotSystem.Pool.MJBotFishAIPool;
import l1j.server.MJBotSystem.Pool.MJBotHuntAIPool;
import l1j.server.MJBotSystem.Pool.MJBotScarecrowAIPool;
import l1j.server.MJBotSystem.Pool.MJBotWanderAIPool;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Bot;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Bot Load Manager.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotLoadManager {
	private static final Random 	_rnd = new Random(System.nanoTime());
	private static MJBotLoadManager _instance;
	public static MJBotLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJBotLoadManager();
		return _instance;
	}
	
	public static void commands(L1PcInstance gm, String param){
		try{
			ArrayDeque<Integer> argsQ = MJCommons.parseToIntQ(param);
			if(argsQ == null || argsQ.isEmpty())
				throw new Exception("");
			
			switch(argsQ.poll()){
			case 1:
				statusCommands(gm, argsQ);
				break;
			case 2:
				createCommands(gm, argsQ);
				break;
			case 3:
				removeCommands(gm, argsQ);
				break;
			case 4:
				reloadCommands(gm, argsQ);
				break;
			case 5:
				updateDollsLevelCommands(gm, argsQ);
				break;
			default:
				throw new Exception("");	
			}
			
		}catch(Exception e){
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 1조회 | 2봇생성 | 3봇삭제 | 4리로드 | 5인형단계"));
		}
	}

	public static void statusCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			if(argsQ.isEmpty())
				throw new Exception("");
			
			switch(argsQ.poll()){
			case 1:{
				if(argsQ.isEmpty()){
//					gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 1 1 [브레인아이디]"));
					gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
					gm.sendPackets(new S_SystemMessage("\\f2 .로봇   1   1   BotBrain_ID (1-8)"));
					return;
				}
				int num = argsQ.poll();
				MJBotBrain brn = MJBotBrainLoader.getInstance().get(num);
				if(brn == null)
					gm.sendPackets(new S_SystemMessage(String.format("\\f2입력한 BotBrain_ID(%d)가 tb_mjbot_brain에 없습니다.", num))); 
				else
					gm.sendPackets(new S_SystemMessage(brn.toString()));
				break;
			}
			case 2:{
				if(argsQ.isEmpty()){
//					gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 1 2 [인벤아이템 아이디]"));
					gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
					gm.sendPackets(new S_SystemMessage("\\f2 .로봇   1   2   InvItem_ID"));
					return;
				}
				int num = argsQ.poll();
				MJBotInvItem item = MJBotInvItemLoader.getInstance().get(num);
				if(item == null)
					gm.sendPackets(new S_SystemMessage(String.format("\\f2 InvItem_ID(%d)를 찾을 수 없습니다.", num)));
				else
					gm.sendPackets(new S_SystemMessage(item.toString()));
				break;
			}
			case 3:{
				if(argsQ.isEmpty()){
//					gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 1 3 [스킬 아이디]"));
					gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
					gm.sendPackets(new S_SystemMessage("\\f2 .로봇   1   3   BotSpell_ID"));
					return;
				}
				int num = argsQ.poll();
				MJBotSpell sp = MJBotSpellLoader.getInstance().get(num);
				if(sp == null)
					gm.sendPackets(new S_SystemMessage(String.format("\\f2 BotSpell_ID(%d)를 찾을 수 없습니다.", num)));
				else
					gm.sendPackets(new S_SystemMessage(sp.toString()));
				break;
			}
			case 4:{
				if(argsQ.isEmpty()){
//					gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 1 4 [타입]"));
					gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
					gm.sendPackets(new S_SystemMessage("\\f2 .로봇   1   4   BotType"));
					return;
				}
				int num = argsQ.poll();
				MJBotType 	type = MJBotType.fromInt(num);
				if(type == null)
					gm.sendPackets(new S_SystemMessage(String.format("\\f2 BotType(%d)을 찾을 수 없습니다.", num)));
				else
					gm.sendPackets(S_Bot.getBotList(gm, type));
				break;
			}
			}
			
		}catch(Exception e){
			gm.sendPackets(new S_SystemMessage(
//					"사용방법 .인공지능 1 [선택]\n" +
//					"[1. 브레인][2. 아이템][3. 스킬][4. 봇현황]"
					"\\f2---------------------------------------------------\n" +
					"\\f2 .로봇   1   번호선택\n" +
					"\\f2 1Brain | 2아이템 | 3스킬 | 4봇현황"
					)
			);
		}
	}
	
	public static void createCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			if(argsQ.isEmpty())
				throw new Exception("");
			
			switch(argsQ.poll()){
			case 1:
				createSingleBot(gm, argsQ, false);
				break;
			case 2:
				createSingleRandomBot(gm, argsQ, false);
				break;
			case 3:
				createMultipleBots(gm, argsQ);
				break;
			case 4:
				createMultipleRandomBots(gm, argsQ);
				break;
			default:
				throw new Exception("");	
			}
			
		}catch(Exception e){
			gm.sendPackets(new S_SystemMessage(
//					"사용방법 .인공지능 2 [선택]\n" +
//					"[1.단일 생성][2.단일 랜덤][3.다중 생성][4.다중 랜덤]"
					"\\f2---------------------------------------------------\n" +
					"\\f2 .로봇   2   번호선택\n" +
					"\\f2 1단일생성 | 2단일랜덤 | 3다중생성 | 4다중랜덤"
					)
			);
		}
	}
	
	private static final HashMap<Integer, Integer> _useBotItemMap;
	static{
		_useBotItemMap = new HashMap<Integer, Integer>();
		_useBotItemMap.put(5563, 0);		
		_useBotItemMap.put(5564, 1);		
		_useBotItemMap.put(5565, 4);
		_useBotItemMap.put(5566, 2);
		_useBotItemMap.put(5567, 3);
		_useBotItemMap.put(4100260, 5);	
	}
	
	public static void delBotItem(L1PcInstance gm, int targetId){
		L1Object obj = L1World.getInstance().findObject(targetId);
		if(obj == null)
			return;
		
		if(!obj.instanceOf(MJL1Type.L1TYPE_PC))
			return;
			
		L1PcInstance body = (L1PcInstance)obj;
		if(body.getAI() == null){
			gm.sendPackets(String.format("\\f2 [%s]은(는) 로봇이 아닙니다.", body.getName()));			
			return;
		}
		
		gm.sendPackets(String.format("\\f2 [%s]을(를) 삭제합니다.", body.getName()));
		MJAIScheduler.getInstance().removeSchedule(body.getAI());
		return;
	}
	
	public static boolean useBotItem(L1PcInstance gm, int itemId){
		try{
			Integer id = _useBotItemMap.get(itemId);
			if(id == null)
				return false;
			
			ArrayDeque<Integer> aq = new ArrayDeque<Integer>(2);
			aq.offer(id == 5 ? 4 : id);
			aq.offer(gm.getLevel());
			createSingleRandomBot(gm, aq, id > 4);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private static void createSingleBot(L1PcInstance gm, ArrayDeque<Integer> argsQ, boolean is_fixed_location){
		try{
			int 		bid 	= argsQ.poll();
			MJBotType 	type	= MJBotType.fromInt(argsQ.poll());
			int			lvl		= argsQ.poll();
			if(type == null){
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 알 수 없는 BotType입니다.[%s]", MJBotType.enumString())));
				return;
			}
			MJBotLastError err = MJAIScheduler.getInstance().setSchedule(bid, type, lvl, gm.getX(), gm.getY(), gm.getMapId(), is_fixed_location);
			if(err.ai == null && err.message != null)
				gm.sendPackets(new S_SystemMessage(err.message));
			else if(type == MJBotType.FISH)
				gm.sendPackets(new S_SystemMessage("\\f2 BotType.FISH를 생성했습니다."));
			else{
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 [%s]을 생성했습니다.", err.ai.getBody().getName())));
				type.add(err.ai);
			}
		}catch(Exception e){
//			gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 2 1 \n[브레인아이디][타입][레벨]"));
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 .로봇   2   1\n\\f2 BotBrain_ID   BotType   BotLev"));
		}
	}
	
	private static void createSingleRandomBot(L1PcInstance gm, ArrayDeque<Integer> argsQ, boolean is_fixed_location){
		try{
			MJBotType 			type	= MJBotType.fromInt(argsQ.poll());
			if(type == null){
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 알 수 없는 BotType입니다.[%s]", MJBotType.enumString())));
				return;
			}
			
			int 				lvl 	= argsQ.poll();
			ArrayList<Integer>	keys	= MJBotBrainLoader.getInstance().createKeySnapshot();
			Integer				bid		= keys.get(_rnd.nextInt(keys.size()));
			MJBotLastError err = MJAIScheduler.getInstance().setSchedule(bid, type, lvl, gm.getX(), gm.getY(), gm.getMapId(), is_fixed_location);
			if(err.ai == null && err.message != null)
				gm.sendPackets(new S_SystemMessage(err.message));
			else if(type == MJBotType.FISH)
				gm.sendPackets(new S_SystemMessage("\\f2 BotType.FISH를 생성했습니다."));
			else{
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 [%s]을 생성했습니다.", err.ai.getBody().getName())));
				type.add(err.ai);
			}
			keys.clear();
		}catch(Exception e){
//			gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 2 2 \n[타입][레벨]"));
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 .로봇   2   2 \n\\f2 BotType   BotLev"));
		}
	}
	
	private static void createMultipleBots(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			int 		bid 	= argsQ.poll();
			MJBotType 	type	= MJBotType.fromInt(argsQ.poll());
			int			lvl		= argsQ.poll();
			int			count	= argsQ.poll();
			if(type == null){
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 알 수 없는 BotType입니다.[%s]", MJBotType.enumString())));
				return;
			}
			
			for(int i=0; i<count; i++){
				MJBotLastError err = MJAIScheduler.getInstance().setSchedule(bid, type, lvl, gm.getX(), gm.getY(), gm.getMapId(), false);
				if(err.ai == null){
					gm.sendPackets(new S_SystemMessage(String.format("\\f2 로봇 %d개를 생성했습니다.", i)));
					return;
				}else
					type.add(err.ai);
			}
			gm.sendPackets(new S_SystemMessage(String.format("\\f2 로봇 %d개를 생성했습니다.", count)));
		}catch(Exception e){
//			gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 2 3 \n[브레인아이디][타입][레벨][마리수]"));
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 .로봇   2   3 \n\\f2 BotBrain_ID   BotType   BotLev   Count"));
		}
	}
	
	private static void createMultipleRandomBots(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			MJBotType			type	= MJBotType.fromInt(argsQ.poll());
			if(type == null){
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 알 수 없는 BotType입니다.[%s]", MJBotType.enumString())));
				return;
			}
			
			int 				lvl 	= argsQ.poll();
			int					count	= argsQ.poll();
			ArrayList<Integer>	keys	= MJBotBrainLoader.getInstance().createKeySnapshot();
			for(int i=0; i<count; i++){
				Integer			bid		= keys.get(_rnd.nextInt(keys.size()));
				MJBotLastError 	err 	= MJAIScheduler.getInstance().setSchedule(bid, type, lvl, gm.getX(), gm.getY(), gm.getMapId(), false);
				if(err.ai == null){
					gm.sendPackets(new S_SystemMessage(err.message));
					gm.sendPackets(new S_SystemMessage(String.format("\\f2 로봇 %d개를 생성했습니다.", i)));
					return;
				}else
					type.add(err.ai);
			}
			gm.sendPackets(new S_SystemMessage(String.format("\\f2 로봇 %d개를 생성했습니다.", count)));
		}catch(Exception e){
//			gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 2 4 \n[타입][레벨][마리수]"));
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 .로봇   2   4 \n\\f2 BotType   BotLev   Count"));
		}
	}
	
	public static void removeCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			if(argsQ.isEmpty())
				throw new Exception("");
			
			switch(argsQ.poll()){
			case 1:
				removeSingleBot(gm, argsQ);
				break;
			case 2:
				removeMultipleBots(gm, argsQ);
				break;
			default:
				throw new Exception("");	
			}
			
		}catch(Exception e){
			gm.sendPackets(new S_SystemMessage(
//					"사용방법 .인공지능 3 [선택]\n" +
//					"[1. 선택삭제][2. 전체삭제]"
					"\\f2---------------------------------------------------\n" +
					"\\f2 .로봇   3   번호선택\n" +
					"\\f2 1선택삭제 | 2전체삭제"
					)
			);
		}
	}
	
	private static void removeSingleBot(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			MJBotType 	type	= MJBotType.fromInt(argsQ.poll());
			int			num		= argsQ.poll();
			MJBotAI 	ai		= type.remove(num);
			if(ai == null){
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 [%d]는 존재하지 않는 로봇번호입니다.", num)));
			}else{
				if(ai.getBody() != null)
					gm.sendPackets(new S_SystemMessage(String.format("\\f2 [%s]을(를) 삭제합니다.", ai.getBody().getName())));
				else
					gm.sendPackets(new S_SystemMessage(String.format("\\f2 [%d]번 로봇을 삭제합니다.", num)));
				MJAIScheduler.getInstance().removeSchedule(ai);
			}
		}catch(Exception e){
//			gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 3 1 \n[타입][봇번호]"));
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 .로봇   3   1 \n\\f2 BotType   BotNumber"));
		}
	}
	
	private static void removeMultipleBots(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			MJBotType 	type	= MJBotType.fromInt(argsQ.poll());
			if(type == null){
				gm.sendPackets(new S_SystemMessage(String.format("\\f2 알 수 없는 BotType입니다.[%s]", MJBotType.enumString())));
				return;
			}
			int cnt = 0;
			for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
				if(pc == null)
					continue;
				
				if(pc.getAI() != null && pc.getAI().getBotType() == type){
					MJAIScheduler.getInstance().removeSchedule(pc.getAI());
					cnt++;
				}
			}
			gm.sendPackets(new S_SystemMessage(String.format("\\f2 로봇 %d개를 삭제했습니다.", cnt)));
			type.dispose();
		}catch(Exception e){
//			gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 3 2\n[타입]"));
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 .로봇   3   2\n\\f2 BotType"));
		}
	}
	
	public static void reloadCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			if(argsQ.isEmpty())
				throw new Exception("");
			
			switch(argsQ.poll()){
			case 1:
				MJBotBrainLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot brain reload completed.]"));
				break;
			case 2:
				MJBotNameLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot name reload completed.]"));
				break;
			case 3:
				MJBotInvItemLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot inventory item reload completed.]"));
				break;
			case 4:
				MJBotLocationLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot location reload completed.]"));
				break;
			case 5:
				MJBotSpellLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot spell reload completed.]"));
				break;
			case 6:
				MJBotMentLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot ment reload completed.]"));
				break;
			case 7:
				getInstance().loadConfig();
				gm.sendPackets(new S_SystemMessage("[bot config reload completed.]"));
				break;
			case 8:
				MJBotDropItemLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot drop items reload completed.]"));
				break;
			case 9:
				MJBotBossNotifierLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot boss notifier reload completed.]"));
				break;
			case 10:
				MJBotDollLoader.reload();
				gm.sendPackets(new S_SystemMessage("[bot dolls reload completed.]"));
				break;	
			case 11:
				MJBotClanInfoLoader.reload();				
				gm.sendPackets(new S_SystemMessage("[bot clan reload completed.]"));
				break;
			case 12:
				getInstance().reload();
				gm.sendPackets(new S_SystemMessage("[bot all reload completed.]"));
				break;
			}
		}catch(Exception e){
			gm.sendPackets(new S_SystemMessage(
					"\\f2---------------------------------------------------\n" +
					"\\f2 .로봇   4   번호선택\n" + 
					"\\f2 1BotB | 2Name | 3InvItem | 4Location | 5Spell\n" +
					"\\f2 6Ment | 7컨픽 | 8DropItem | 9보스알림 | 10인형\n" +
					"\\f2 11혈맹 | 12전체"
					)
			);
		}
	}
	
	public static void updateDollsLevelCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		try{
			MJBotDollLoader._currentLevel = argsQ.poll();
			MJBotDollLoader.reload();
		}catch(Exception e){
//			gm.sendPackets(new S_SystemMessage("사용방법 .인공지능 5 [단계]"));
			gm.sendPackets(new S_SystemMessage("\\f2----------------------------------------------------"));
			gm.sendPackets(new S_SystemMessage("\\f2 .로봇   5   BotDoll_단계"));
		}
	}
	
	private MJBotLoadManager(){
	}
	
	public void load(){
		loadConfig();
		MJAIScheduler.getInstance();
		MJBotClanInfoLoader.getInstance();
		MJBotDollLoader.getInstance();
		MJBotHuntAIPool.getInstance();
		MJBotScarecrowAIPool.getInstance();
		MJBotFishAIPool.getInstance();
		MJBotWanderAIPool.getInstance();
		MJBotNameLoader.getInstance();
		MJBotBrainLoader.getInstance();
		MJBotInvItemLoader.getInstance();
		MJBotLocationLoader.getInstance();
		MJBotSpellLoader.getInstance();
		MJBotMentLoader.getInstance();
		MJBotDropItemLoader.getInstance();
		MJBotBossNotifierLoader.getInstance();
	}
	
	public void reload(){
		loadConfig();
		MJBotDollLoader.reload();
		MJBotClanInfoLoader.reload();
		MJBotBrainLoader.reload();
		MJBotNameLoader.reload();
		MJBotInvItemLoader.reload();
		MJBotLocationLoader.reload();
		MJBotSpellLoader.reload();
		MJBotMentLoader.reload();
		MJBotDropItemLoader.reload();
		MJBotBossNotifierLoader.reload();
	}
	
	public void release(){
		MJBotDollLoader.release();
		MJBotClanInfoLoader.release();
		MJBotType.release();
		MJAIScheduler.release();
		MJBotBrainLoader.release();
		MJBotNameLoader.release();
		MJBotInvItemLoader.release();
		MJBotLocationLoader.release();
		MJBotSpellLoader.release();
		MJBotMentLoader.release();
		MJBotDropItemLoader.release();
		MJBotBossNotifierLoader.release();
		MJBotUtil.deleteBots();
		//MJBotUtil.deleteClanBots();
	}
	
	public static int	MBO_DPS;
	public static int 	MBO_TARGET_Q_SIZE;
	public static int	MBO_WORK_Q_SIZE;
	public static int	MBO_FISHAI_POOL_SIZE;
	public static int	MBO_HUNTAI_POOL_SIZE;
	public static int	MBO_SCARECROWAI_POOL_SIZE;
	public static int	MBO_WANDERAI_POOL_SIZE;
	
	public static int	MBO_BRAIN_MAX_FIGURE;
	public static int	MBO_BRAIN_MIN_FIGURE;
	public static int	MBO_SEARCH_MAX_COUNT;
	public static int	MBO_SEARCH_MIN_COUNT;
	public static int	MBO_MENTDICE_DEATH;
	public static int	MBO_MENTDICE_KILL;
	public static int	MBO_MENTDICE_ONTARGET;
	public static int	MBO_MENTDICE_ONPERCEIVE;
	public static int	MBO_MENTDICE_ONDAMAGE;
	public static int	MBO_MENTDICE_IDLE;
	
	public static int 	MBO_ASTAR_LOOP;
	public static int 	MBO_ASTAR_OPENS_SIZE;
	public static int 	MBO_ASTAR_CLOSES_SIZE;
	public static int	MBO_ASTAR_NODEPOOL_SIZE;
	
	public static int	MBO_WANDER_MAT_LEFT;
	public static int	MBO_WANDER_MAT_TOP;
	public static int	MBO_WANDER_MAT_RIGHT;
	public static int	MBO_WANDER_MAT_BOTTOM;
	public static int	MBO_WANDER_MAT_MAPID;
	public static int	MBO_WANDER_IDLETIME;
	
	public static long	MBO_POTION_DELAY;
	public static long	MBO_SKILL_REVISION_DELAY;
	
	public static int	MBO_ADDDMG_HUNT;
	public static int	MBO_ADDHIT_HUNT;
	public static int	MBO_ADDRDT_HUNT;
	public static int	MBO_ADDHP_HUNT;
	public static int	MBO_ADDSP_HUNT;
	public static int   MBO_ADDDMG_RK;
	public static int   MBO_ADDHIT_RK;
	public static int	MBO_ADDRDT_RK;
	public static int	MBO_ADDHP_RK;
	public static int	MBO_ADDSP_RK;
	public static int   MBO_ADDDMG_PT;
	public static int   MBO_ADDHIT_PT;
	public static int	MBO_ADDRDT_PT;
	public static int	MBO_ADDHP_PT;
	public static int	MBO_ADDSP_PT;
	
	public static long	MBO_REGEN_RK;
	
	public static int		MBO_LEVEL_RK;
	public static int		MBO_LEVEL_PT;
	
	public static int MBO_HUNT_WALKOFFENCE_PENDING_LV1;
	public static int MBO_HUNT_WALKOFFENCE_SPENDING_LV1;
	public static int MBO_HUNT_WALKOFFENCE_PENDING_LV2;
	public static int MBO_HUNT_WALKOFFENCE_SPENDING_LV2;
	public static int MBO_HUNT_WALKOFFENCE_PENDING_LV3;
	public static int MBO_HUNT_WALKOFFENCE_SPENDING_LV3;
	public static boolean MBO_IS_CASTLE_WAR;
	private void loadConfig(){
		String column			= null;
		try{
			Properties settings = new Properties();
			InputStream is = new FileInputStream(new File("./config/mjbot.properties"));
			settings.load(is);
			is.close();
			
			column					= "BotDps";
			MBO_DPS					= Integer.parseInt(settings.getProperty(column, "24"));
			column					= "TargetQueueDefaultSize";
			MBO_TARGET_Q_SIZE		= Integer.parseInt(settings.getProperty(column, "5"));
			column					= "WorkQSize";
			MBO_WORK_Q_SIZE			= Integer.parseInt(settings.getProperty(column, "128"));
			column					= "FishAIPoolSize";
			MBO_FISHAI_POOL_SIZE	= Integer.parseInt(settings.getProperty(column, "32"));
			column					= "HuntAIPoolSize";
			MBO_HUNTAI_POOL_SIZE	= Integer.parseInt(settings.getProperty(column, "256"));
			column					= "ScarecrowAIPoolSize";
			MBO_SCARECROWAI_POOL_SIZE= Integer.parseInt(settings.getProperty(column, "32"));
			column					= "WanderAIPoolSize";
			MBO_WANDERAI_POOL_SIZE	= Integer.parseInt(settings.getProperty(column, "64"));
			
			column					= "AstarLoopCount";
			MBO_ASTAR_LOOP			= Integer.parseInt(settings.getProperty(column, "256"));
			column					= "AstarOpenListSize";
			MBO_ASTAR_OPENS_SIZE	= Integer.parseInt(settings.getProperty(column, "128"));
			column					= "AstarCloseListSize";
			MBO_ASTAR_CLOSES_SIZE	= Integer.parseInt(settings.getProperty(column, "128"));
			column					= "AstarNodePoolSize";
			MBO_ASTAR_NODEPOOL_SIZE	= Integer.parseInt(settings.getProperty(column, "10240"));
			
			column					= "BrainMaxFigure";
			MBO_BRAIN_MAX_FIGURE	= Integer.parseInt(settings.getProperty(column, "95"));
			column					= "BrainMinFigure";
			MBO_BRAIN_MIN_FIGURE	= Integer.parseInt(settings.getProperty(column, "5"));
			column					= "SearchMaxCount";
			MBO_SEARCH_MAX_COUNT	= Integer.parseInt(settings.getProperty(column, "10"));
			column					= "SearchMinCount";
			MBO_SEARCH_MIN_COUNT	= Integer.parseInt(settings.getProperty(column, "3"));
			column					= "MentDiceDeath";
			MBO_MENTDICE_DEATH		= Integer.parseInt(settings.getProperty(column, "10"));
			column					= "MentDiceKill";
			MBO_MENTDICE_KILL		= Integer.parseInt(settings.getProperty(column, "10"));
			column					= "MentDiceOnTarget";
			MBO_MENTDICE_ONTARGET	= Integer.parseInt(settings.getProperty(column, "10"));
			column					= "MentDiceOnPerceive";
			MBO_MENTDICE_ONPERCEIVE = Integer.parseInt(settings.getProperty(column, "10"));
			column					= "MentDiceOnDamage";
			MBO_MENTDICE_ONDAMAGE	= Integer.parseInt(settings.getProperty(column, "10"));
			column					= "MentDiceIdle";
			MBO_MENTDICE_IDLE		= Integer.parseInt(settings.getProperty(column, "10"));
			
			column					= "WanderMatrixLeft";
			MBO_WANDER_MAT_LEFT		= Integer.parseInt(settings.getProperty(column, "33413"));
			column					= "WanderMatrixTop";
			MBO_WANDER_MAT_TOP		= Integer.parseInt(settings.getProperty(column, "32796"));
			column					= "WanderMatrixRight";
			MBO_WANDER_MAT_RIGHT	= Integer.parseInt(settings.getProperty(column, "33447"));
			column					= "WanderMatrixBottom";
			MBO_WANDER_MAT_BOTTOM	= Integer.parseInt(settings.getProperty(column, "32831"));
			column					= "WanderMatrixMapId";
			MBO_WANDER_MAT_MAPID	= Integer.parseInt(settings.getProperty(column, "4"));
			column					= "WanderIdleTime";
			MBO_WANDER_IDLETIME		= Integer.parseInt(settings.getProperty(column, "30")) * 1000;
			
			column					= "PotionDelay";
			MBO_POTION_DELAY		= Long.parseLong(settings.getProperty(column, "300"));
			
			column					= "SkillRevisionDelay";
			MBO_SKILL_REVISION_DELAY = Long.parseLong(settings.getProperty(column, "3000"));
			
			column					= "HuntAddDamage";
			MBO_ADDDMG_HUNT			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "HuntAddHit";
			MBO_ADDHIT_HUNT			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "HuntAddReduction";
			MBO_ADDRDT_HUNT			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "HuntAddHp";
			MBO_ADDHP_HUNT			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "HuntAddSp";
			MBO_ADDSP_HUNT			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "RedKnightAddDamage";
			MBO_ADDDMG_RK			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "RedKnightAddHit";
			MBO_ADDHIT_RK			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "RedKnightAddReduction";
			MBO_ADDRDT_RK			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "RedKnightAddHP";
			MBO_ADDHP_RK			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "RedKnightAddSp";
			MBO_ADDSP_RK			= Integer.parseInt(settings.getProperty(column, "0"));
			column					= "RedKnightLevel";
			MBO_LEVEL_RK			= Integer.parseInt(settings.getProperty(column, "70"));
			column					= "ProtectorAddDamage";
			MBO_ADDDMG_PT			= Integer.parseInt(settings.getProperty(column, "50"));
			column					= "ProtectorAddHit";
			MBO_ADDHIT_PT			= Integer.parseInt(settings.getProperty(column, "50"));
			column					= "ProtectorAddReduction";
			MBO_ADDRDT_PT			= Integer.parseInt(settings.getProperty(column, "50"));
			column					= "ProtectorAddHP";
			MBO_ADDHP_PT			= Integer.parseInt(settings.getProperty(column, "5000"));
			column					= "ProtectorAddSp";
			MBO_ADDSP_PT			= Integer.parseInt(settings.getProperty(column, "20"));
			column					= "ProtectorLevel";
			MBO_LEVEL_PT			= Integer.parseInt(settings.getProperty(column, "85"));
			
			column					= "RedKnightGenTime";
			MBO_REGEN_RK			= Integer.parseInt(settings.getProperty(column, "5")) * 1000 * 60;
			
			column														= "HuntWalkOffencePendingLv1";
			MBO_HUNT_WALKOFFENCE_PENDING_LV1		= Integer.parseInt(settings.getProperty(column, "7"));
			column														= "HuntWalkOffenceSPendingLv1";
			MBO_HUNT_WALKOFFENCE_SPENDING_LV1	= Integer.parseInt(settings.getProperty(column, "5"));
			
			column														= "HuntWalkOffencePendingLv2";
			MBO_HUNT_WALKOFFENCE_PENDING_LV2		= Integer.parseInt(settings.getProperty(column, "6"));
			column														= "HuntWalkOffenceSPendingLv2";
			MBO_HUNT_WALKOFFENCE_SPENDING_LV2	= Integer.parseInt(settings.getProperty(column, "4"));
			
			column														= "HuntWalkOffencePendingLv3";
			MBO_HUNT_WALKOFFENCE_PENDING_LV3		= Integer.parseInt(settings.getProperty(column, "5"));
			column														= "HuntWalkOffenceSPendingLv3";
			MBO_HUNT_WALKOFFENCE_SPENDING_LV3	= Integer.parseInt(settings.getProperty(column, "3"));
			
			column = "IsAICastleWar";
			MBO_IS_CASTLE_WAR = Boolean.parseBoolean(settings.getProperty(column, "true"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
