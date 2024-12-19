package l1j.server.server.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.RaceTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.MJMessengerInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.templates.L1Racer;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class BugRaceController implements Runnable {
	private static BugRaceController _instance;

	private static Random _random = new Random(System.nanoTime());

	public static int BUG_BASIC_SPEED;
	public static boolean IS_RATE_REALTIME_UPDATE;
	public static double BUG_MIN_RATE;
	public static double[] BUG_MAX_RATES;
	public static boolean IS_AUTO_TUMBLE;
	public static int MAX_TUMBLE_PROBABILITY;
	public static void load_config(){
		MJPropertyReader reader = null;
		try{
			reader = new MJPropertyReader("./config/bug_race.properties");
			BUG_BASIC_SPEED = reader.readInt("BugBasicSpeed", "250");
			IS_RATE_REALTIME_UPDATE = reader.readBoolean("IsRateRealTimeUpdate", "true");
			BUG_MIN_RATE = reader.readDouble("BugMinRate", "1.2");
			BUG_MAX_RATES = reader.readDoubleArray("BugMaxRates", "10,9,8,7,6", "\\,");
			IS_AUTO_TUMBLE = reader.readBoolean("IsAutoTumble", "true");
			MAX_TUMBLE_PROBABILITY = reader.readInt("MaxTumbleProbability", "100");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null)
				reader.dispose();
		}
	}
	
	
	//TODO 버경시간 조정 1이면 1분을 뜻함
	private static int RACE_INTERVAL = 1 * 60 * 1000;

	public static final int EXECUTE_STATUS_NONE = 0;
	public static final int EXECUTE_STATUS_PREPARE = 1;
	public static final int EXECUTE_STATUS_READY = 2;
	public static final int EXECUTE_STATUS_STANDBY = 3;
	public static final int EXECUTE_STATUS_PROGRESS = 4;
	public static final int EXECUTE_STATUS_FINALIZE = 5;

	private int _executeStatus = EXECUTE_STATUS_NONE;

	public int _raceCount = 0;
	long _nextRaceTime = System.currentTimeMillis() + 60 * 1000;
	public int _bugRaceState = 2;

	public int _ticketSellRemainTime;
	public int _raceWatingTime;
	public int _currentBroadcastRacer;

	L1NpcInstance[] _npc = new L1NpcInstance[3];
	MJMessengerInstance _box_npc;
	
	public int[] _ticketCount = new int[5];
	private static Random _rnd = new Random(System.nanoTime());
	private static DecimalFormat _df = new DecimalFormat("#.#");

	public int _ranking = 0;
	public boolean _complete = false;

	List<L1ShopItem> _purchasingList = new ArrayList<L1ShopItem>();
	public L1NpcInstance[] _littleBugBear = new L1NpcInstance[5];

	int Lucky = 0;
	private static Random rnd = new Random(System.nanoTime());

	/** 버경 추가 **/
	private final HashMap<Integer, L1RaceTicket> _race = new HashMap<Integer, L1RaceTicket>(32);
	//private L1Item _allTemplates[] = new L1Item[9000000];
	//private HashMap<Integer, L1Item> _allTemplates = new HashMap<Integer, L1Item>(32);
	
	public HashMap<Integer, L1RaceTicket> getAllTemplates() {
		return _race;
	}

	private int Start_X[] = { 33522, 33520, 33518, 33516, 33514 };
	private int Start_Y[] = { 32861, 32863, 32865, 32867, 32869 };

	private static final ArrayList<BugStruct> _bugs;
	static{
		//TODO 마법인형 경주회
		/*_bugs = new ArrayList<BugStruct>(32);
		_bugs.add(new BugStruct(1, 16081, "커츠"));
		_bugs.add(new BugStruct(2, 16082, "얼녀"));
		_bugs.add(new BugStruct(3, 16083, "바포"));
		_bugs.add(new BugStruct(4, 16084, "라기"));
		_bugs.add(new BugStruct(5, 16085, "랑카"));
		_bugs.add(new BugStruct(6, 16086, "데스"));
		_bugs.add(new BugStruct(7, 16087, "데몬"));
		_bugs.add(new BugStruct(8, 16088, "머미"));
		_bugs.add(new BugStruct(9, 16089, "뱀파"));
		_bugs.add(new BugStruct(10, 16090, "아리"));
		_bugs.add(new BugStruct(11, 16091, "시어"));
		_bugs.add(new BugStruct(12, 16092, "나발"));
		_bugs.add(new BugStruct(13, 16093, "싸이"));
		_bugs.add(new BugStruct(14, 16094, "리치"));
		_bugs.add(new BugStruct(15, 16095, "다골"));
		_bugs.add(new BugStruct(16, 16096, "코아"));
		_bugs.add(new BugStruct(17, 16097, "퀴니"));
		_bugs.add(new BugStruct(18, 16098, "까미"));
		_bugs.add(new BugStruct(19, 16099, "자이"));
		_bugs.add(new BugStruct(20, 16100, "라바"));*/
		
		//TODO 버경
		_bugs = new ArrayList<BugStruct>(32);
		_bugs.add(new BugStruct(1, 3478, "배크두"));
		_bugs.add(new BugStruct(2, 3479, "투투"));
		_bugs.add(new BugStruct(3, 3480, "버기"));
		_bugs.add(new BugStruct(4, 3481, "얼루"));
		_bugs.add(new BugStruct(5, 3497, "제프리"));
		_bugs.add(new BugStruct(6, 3498, "카이"));
		_bugs.add(new BugStruct(7, 3499, "아돌프"));
		_bugs.add(new BugStruct(8, 3500, "오버풋"));
		_bugs.add(new BugStruct(9, 3501, "럼프스"));
		_bugs.add(new BugStruct(10, 3502, "부카"));
		_bugs.add(new BugStruct(11, 3503, "그룩"));
		_bugs.add(new BugStruct(12, 3504, "쿤드라"));
		_bugs.add(new BugStruct(13, 3505, "쿠마토"));
		_bugs.add(new BugStruct(14, 3506, "두렉"));
		_bugs.add(new BugStruct(15, 3507, "그로돈"));
		_bugs.add(new BugStruct(16, 3508, "퀘니버"));
		_bugs.add(new BugStruct(17, 3509, "플루토"));
		_bugs.add(new BugStruct(18, 3510, "두리바"));
		_bugs.add(new BugStruct(19, 3511, "버루얼"));
		_bugs.add(new BugStruct(20, 3512, "일레자"));
	}

	public static int[] _time = new int[5];
	public static String _first = null;

	public int[] _ticket = { 0, 0, 0, 0, 0 };
	
	private HashMap<Integer, BugTicketInfo> m_tickets = new HashMap<Integer, BugTicketInfo>(24);
	
	// 승률 초기화
	public double[] _winRate = { 0, 0, 0, 0, 0 };
	public double[] _winViewRate = { 0, 0, 0, 0, 0 };
	// 상태 초기화
	public String[] _bugCondition = { "■■■■", "■■■■□", "■■■□□", "■■□□□", "■□□□□" };
	// 배율 초기화
	public double _ration[] = { 0, 0, 0, 0, 0 };

	public boolean[] _is_downs = new boolean[]{false, false, false, false, false};
	
	public int _round;
	
	public static BugRaceController getInstance() {
		if (_instance == null) {
			_instance = new BugRaceController();
		}
		return _instance;
	}
	
	private BugRaceController(){
		load_config();
		_round = 0;
		Selector.exec("select max(round) as r_nd from bug_history", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next())
					_round = rs.getInt("r_nd");
			}
		});
	}
	
	public static class BugTicketInfo{
		public int racerId;
		public int itemId;
		public int packCount;
		public int converter_itemid;
	}
	
	public BugTicketInfo find_ticket_info(int itemid){
		return m_tickets.get(itemid);
	}

	public void run() {
		try {
			switch (_executeStatus) {
			case EXECUTE_STATUS_NONE: {
				if (checkStartRace()) {
					initRaceGame();
					_executeStatus = EXECUTE_STATUS_PREPARE;
//					GeneralThreadPool.getInstance().schedule(this, 60 * 1000L);
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				} else {
					GeneralThreadPool.getInstance().schedule(this, 1000L); // 1초
				}
			}
				break;
			case EXECUTE_STATUS_PREPARE: {
				startSellTicket();
				_executeStatus = EXECUTE_STATUS_READY;
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			}
				break;
			case EXECUTE_STATUS_READY: {
				long remainTime = checkTicketSellTime();
				if (remainTime > 0) {
					GeneralThreadPool.getInstance().schedule(this, remainTime);
				} else {
					_executeStatus = EXECUTE_STATUS_STANDBY;
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
			}
				break;

			case EXECUTE_STATUS_STANDBY: {
				if (checkWatingTime()) {
					startBugRace();
					_executeStatus = EXECUTE_STATUS_PROGRESS;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			}
				break;

			case EXECUTE_STATUS_PROGRESS: {
				if (broadcastBettingRate()) {
					if (_complete) {
						_executeStatus = EXECUTE_STATUS_FINALIZE;
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			}
				break;
			case EXECUTE_STATUS_FINALIZE: {
				wrapUpRace();
				_executeStatus = EXECUTE_STATUS_NONE;
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkStartRace() {
		long currentTime = System.currentTimeMillis();
		if (_nextRaceTime < currentTime) {
			_nextRaceTime = currentTime + RACE_INTERVAL;
			return true;
		}
		return false;
	}

	public void initRaceGame() {
		try {
			++_round;
			_ranking = 0;
			_complete = false;
			_first = null;

			Lucky = rnd.nextInt(50);

			broadcastNpc("소지중인 티켓을 판매하시길 바랍니다.");
			_is_downs = new boolean[]{false, false, false, false, false};
			
			// 버그베어 경주 초기화
			initNpc();
			// 상점 Npc초기화
			initShopNpc();
			// 버그베어 달리기 속도 지정
			sleepTime();
			// 버그베어 초기화 및 로딩
			loadDog();
			// 승률 초기화
			initWinRate();
			// 게임시
			doorAction(false);
		} catch (Exception e) {
		}
	}
	
	public synchronized void on_buy_ticket(int ticket_id, int amount){
		BugTicketInfo tInfo = m_tickets.get(ticket_id);
		if(tInfo == null)
			return;
		
		int ticket_count = _ticketCount[tInfo.racerId] + (amount * tInfo.packCount);
		_ticketCount[tInfo.racerId] = ticket_count;		
		SettingRate();
		//if(IS_RATE_REALTIME_UPDATE)
			
	}

	public void initTicketCount() {
		for (int row = 0; row < 5; row++) {
			this._ticketCount[row] = 20;
			_ration[row] = 5.0D;
		}
		SettingRate();
	}

	// 생성된 Npc객체를 이니셜라이즈 한다.
	public void initNpc() {
		L1NpcInstance n = null;
		for (Object obj : L1World.getInstance().getVisibleObjects(4).values()) {
			if (obj instanceof L1NpcInstance) {
				n = (L1NpcInstance) obj;
				if (n.getNpcTemplate().get_npcId() == 70041) {
					_npc[0] = n;
				} else if (n.getNpcTemplate().get_npcId() == 70035) {
					_npc[1] = n;
				} else if (n.getNpcTemplate().get_npcId() == 70042) {
					_npc[2] = n;
				} else if(n.getNpcTemplate().get_npcId() == 50000063){
					_box_npc = (MJMessengerInstance)n;
					_box_npc.set_is_ghost(true);
				}
			}
		}
	}

	public void initShopNpc() {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		L1Shop shop = new L1Shop(70035, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70035, shop);
		L1Shop shop1 = new L1Shop(70041, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70041, shop1);
		L1Shop shop2 = new L1Shop(70042, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70042, shop2);
	}

	private void sleepTime() {
		for (int i = 0; i < 5; i++) {
			int bugState = _rnd.nextInt(5);
			int addValue = 0;

			switch (bugState) {
			case 0:
				_bugCondition[i] = "■■■■■";
				addValue = -2;
				break;
			case 1:
				_bugCondition[i] = "■■■■□";
				addValue = -1;
				break;
			case 2:
				_bugCondition[i] = "■■■□□";
				addValue = 1;
				break;
			case 3:
				_bugCondition[i] = "■■□□□";
				addValue = 2;
				break;
			default:
				_bugCondition[i] = "■□□□□";
				addValue = 0;
				break;
			}
			_time[i] = BUG_BASIC_SPEED + addValue;
		}
	}

	// 승률처리
	public void initWinRate() {
		double presentation_rate = MJRnd.next_double(20, 30);
		for (int i = 0; i < 5; i++) {
			_winRate[i] = Double.parseDouble(_df.format(MJRnd.next_double(20.1, 21)));
			 _winViewRate[i] = Double.parseDouble(_df.format(presentation_rate));
		}
	}

	private FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();

	public L1NpcInstance find_bug(int object_id){
		for(L1NpcInstance npc : list){
			if(npc.getId() == object_id)
				return npc;
		}
		return null;
	}
	
	public boolean down_bug(int object_id){
		boolean do_down = false;
		for(int i=list.size() - 1; i>=0; --i){
			L1NpcInstance npc = list.get(i);
			if(npc.getId() != object_id)
				continue;
			
			_is_downs[i] = true;
			do_down = true;
			break;
		}
		return do_down;
	}
	
	public Iterator<L1NpcInstance> get_race_iter() {
		return list.iterator();
	}

	public void setSpeed(int i, int speed){
		_time[i] = speed;
	}
	
	public int getSpeed(int i){
		return _time[i];
	}
	
	private void loadDog() {
		L1Npc dogs = null;
		List<L1PcInstance> players = null;

		list.clear(); // -- 시작전에 초기화.

		Collections.shuffle(_bugs);
		for (int m = 0; m < 5; ++m) {
			try {
				BugStruct bs = _bugs.get(m);
				dogs = new L1Npc();
				dogs.set_family(0);
				dogs.set_agrofamily(0);
				dogs.set_picupitem(false);

				Object[] parameters = { dogs };

				_littleBugBear[m] = (L1NpcInstance) Class.forName("l1j.server.server.model.Instance.L1NpcInstance")
						.getConstructors()[0].newInstance(parameters);
				_littleBugBear[m].setCurrentSprite(bs.gfx);
				
				_littleBugBear[m].setNameId(String.format("#%d%s", bs.id, bs.name));
				_littleBugBear[m].setName(bs.name);
				_littleBugBear[m].set_num(bs.id);
				_littleBugBear[m].setX(Start_X[m]);
				_littleBugBear[m].setY(Start_Y[m]);
				_littleBugBear[m].setMap((short) 4);
				_littleBugBear[m].setHeading(6);
				_littleBugBear[m].setId(IdFactory.getInstance().nextId());

				L1World.getInstance().storeObject(_littleBugBear[m]);
				L1World.getInstance().addVisibleObject(_littleBugBear[m]);

				list.add(_littleBugBear[m]);

				players = L1World.getInstance().getVisiblePlayer(_littleBugBear[m]);
				for (L1PcInstance member : players) {
					if (member != null) {
						member.updateObject();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void broadcastNpc(String msg) {
		for (int i = 0; i < _npc.length; ++i) {
			if (_npc[i] != null) {
				_npc[i].broadcastPacket(new S_NpcChatPacket(_npc[i], msg, 2));
			}
		}
	}
	
	public void broadcastBox(String msg){
		if(_box_npc != null){
			_box_npc.set_current_message(String.format("\\fS%s", msg));
			_box_npc.broadcast_message();
//			_box_npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.new_namechat_isntance(_box_npc, msg, false), MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, false);
//			_box_npc.broadcastPacket(new S_NpcChatPacket(_box_npc, msg, 4));
		}
	}

	public void 우승자멘트(String msg) {
		for (int i = 0; i < _npc.length; ++i) {
			if (_npc[i] != null) {
				_npc[i].broadcastPacket(new S_NpcChatPacket(_npc[i], msg, 2));
			}
		}
	}

	public void doorAction(boolean open) {
		L1DoorInstance door = null;
		for (Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;
				if (door != null && door.equalsCurrentSprite(1487)) {
					if (open && door.getOpenStatus() == ActionCodes.ACTION_Close) {
						door.open();
					}
					if (!open && door.getOpenStatus() == ActionCodes.ACTION_Open) {
						door.close();
					}
				}
			}
		}
	}

	public void startSellTicket() {
		LoadNpcShopList();
		// 토탈 판매 장수 초기화
		initTicketCount();
		broadcastNpc("레이스표 판매가 시작되었습니다.");
		this.setBugState(0);
		_ticketSellRemainTime = 60 * 3;
	}

	public long checkTicketSellTime() {
		if (_ticketSellRemainTime == 3 * 60) {
			_ticketSellRemainTime -= 60;
			broadcastNpc("경기시작 3분전");
			broadcastBox("판매마감: 3분 전");
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 2 * 60) {
			_ticketSellRemainTime -= 60;
			broadcastNpc("경기시작 2분 전");
			//broadcastBox("\\aY판매 마감 : 2 분 전");
			broadcastBox("판매마감: 2분 전");
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 1 * 60) {
			_ticketSellRemainTime -= 60;
			broadcastNpc("경기시작 1분 전");
			//broadcastBox("\\aY판매 마감 : 1 분 전");//색상이 표기가 안됨
			broadcastBox("판매마감: 1분 전");//색상이 표기가 안됨
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					try{
						broadcastNpc("30초 후 레이스표 판매가 마감됩니다.");
						for(int i=30; i>=1; --i){
							//broadcastBox(String.format("\\aG판매 마감 : %d 초 전", i));//판매마감이라는NPC가 안하고 모리 레티 이런들만함
							broadcastBox(String.format("판매마감: %d초 전", i));//판매마감이라는NPC가 안하고 모리 레티 이런들만함
							Thread.sleep(1000);
						}
						//broadcastBox("\\aS판매가 종료 되었습니다.");
						broadcastBox("판매가 종료 되었습니다.");
					}catch(Exception e){}
				}
			}, 35000);
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 1 * 30) {//밑으로 읽질않음
			_ticketSellRemainTime = 0;
			/*broadcastNpc("30초 후 레이스표 판매가 마감됩니다.");
			GeneralThreadPool.getInstance().execute(new Runnable(){
				@Override
				public void run(){
					try{
						for(int i=30; i>=1; ++i){
							broadcastBox(String.format("\\aG판매 마감 : %d 초 전", i));//판매마감이라는NPC가 안하고 모리 레티 이런들만함
							Thread.sleep(1000);
						}
						broadcastBox("\\f9판매가 종료 되었습니다.");
					}catch(Exception e){}
				}
			});*/
			return 30 * 1000;
		}
		initShopNpc();
		broadcastNpc("출발 준비!");
		SettingRate();
		_raceWatingTime = 5;
		return 0;
	}

	private boolean checkWatingTime() {
		setBugState(1);
		if (_raceWatingTime > 0) {
			broadcastNpc(_raceWatingTime + "초");
			--_raceWatingTime;

			return false;
		}

		return true;
	}

	private void startBugRace() {

		broadcastNpc("시작");
		doorAction(true);

		StartGame();

		_currentBroadcastRacer = 0;
	}

	private boolean broadcastBettingRate() {
		if (_currentBroadcastRacer == 5) {
			return true;
		}

		if (_currentBroadcastRacer == 0) {
			broadcastNpc("배팅 배율을 발표하겠습니다.");
		}

		broadcastNpc(_littleBugBear[_currentBroadcastRacer].getNameId() + ": " + _ration[_currentBroadcastRacer] + " ");

		++_currentBroadcastRacer;

		return false;
	}

	/*
	private int[] ticket_count = { CommonUtil.random(150, 1000), // 자동구매 인공지능
			CommonUtil.random(150, 1000), // 자동구매 인공지능
			CommonUtil.random(150, 1000), // 자동구매 인공지능
			CommonUtil.random(150, 1000), // 자동구매 인공지능
			CommonUtil.random(150, 1000),// 자동구매 인공지능
	};*/

	/*public void SettingRate() {// 배율설정
		for (int row = 0; row < 5; row++) {
			double rate = 0;
			// int total = this.getTotalTicketCount();
			int num_0 = ticket_count[0], 
					num_1 = ticket_count[1], 
					num_2 = ticket_count[2], 
					num_3 = ticket_count[3],
					num_4 = ticket_count[4];
			int total = ((num_0 + num_1 + num_2 + num_3 + num_4) + this.getTotalTicketCount());
			// int cnt = this._ticketCount[row];

			int cnt = (ticket_count[row] + this._ticketCount[row]);
			if (total == 0)
				total = 1;

			if (cnt != 0) {
				rate = (double) total / (double) cnt;
				if (Lucky == row) {
					rate *= 1.0;
				}
				// System.out.println(cnt + " !!!");
			}

			// -- 일정확률로 배율 뻥튀이기 하기
			// -- 잭팟 효과
			int i = _random.nextInt(100);
			if (i < Config.BugBug) {
				broadcastNpc("배팅 잭팟 발동되었습니다. [" + Config.BugBug1 + "]배!!!");
				rate *= Config.BugBug1;
			}
			_ration[row] = Double.parseDouble(_df.format(rate));
		}
	}*/
	

	public void SettingRate() {// 배율설정
		double total = getTotalTicketCount();
		ArrayList<RationInfo> temporary_ration = new ArrayList<RationInfo>(5);
		for (int row = 0; row < 5; row++) {
			double cnt = _ticketCount[row];
			RationInfo rInfo = new RationInfo();
			rInfo.idx = row;
			rInfo.ration = _ration[row];
			if(cnt <= 0){
				while(rInfo.ration <= BUG_MIN_RATE)
					rInfo.ration = MJRnd.next_double() * 10;
			}else{
				rInfo.ration = total / cnt;
			}
			if(rInfo.ration <= BUG_MIN_RATE)
				rInfo.ration = BUG_MIN_RATE + MJRnd.next_double();
			temporary_ration.add(rInfo);
		}
		Collections.sort(temporary_ration);
		for(int i=0; i<5; ++i){
			double need_range = BUG_MAX_RATES[i] - 1D;
			double range = BUG_MAX_RATES[i];
			RationInfo rInfo = temporary_ration.get(i);
			if(rInfo.ration >= range){
				double d = MJRnd.next_double();
				rInfo.ration = need_range + d;
			}
			_ration[rInfo.idx] = Double.parseDouble(_df.format(rInfo.ration));
		}
	}
	
	static class RationInfo implements Comparable<RationInfo>{
		int idx;
		double ration;
		@Override
		public int compareTo(RationInfo o) {
			double d = o.ration - ration;
			return d > 0 ? 1 : d < 0 ? -1 : 0;
		}
	}
	
	public double[] calc_rations(){
		double total = getTotalTicketCount();
		double[] rations = new double[5];
		for(int i=4; i>=0; --i)
			rations[i] = total / _ticketCount[i];
		return rations;
	}

	public void AddWinCount(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(_littleBugBear[j].get_num());
		if (racer != null) {
			racer.setWinCount(racer.getWinCount() + 1);
			racer.setLoseCount(racer.getLoseCount());
			SaveAllRacer(racer, _littleBugBear[j].get_num());
		}
	}

	public void AddLoseCount(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(_littleBugBear[j].get_num());
		if (racer != null) {
			racer.setWinCount(racer.getWinCount());
			racer.setLoseCount(racer.getLoseCount() + 1);
			SaveAllRacer(racer, _littleBugBear[j].get_num());
		}
	}

	public void SaveAllRacer(L1Racer racer, int num) {
		java.sql.Connection con = null;
		PreparedStatement statement = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE util_racer SET 승리횟수=?, 패횟수=? WHERE 레이서번호=" + num);
			statement.setInt(1, racer.getWinCount());
			statement.setInt(2, racer.getLoseCount());
			statement.execute();
		} catch (SQLException e) {
			System.out.println("[::::::] SaveAllRacer 메소드 에러 발생");
		} finally {
			SQLUtil.close(statement, con);
		}
	}

	public void SetWinRaceTicketPrice(int id, double rate) {
		L1ShopItem newItem = new L1ShopItem(id, (int) (500 * rate), 1);// 승리표 판매 리스트 레이스표 매입
		_purchasingList.add(newItem);
		initShopNpc();
	}

	public void SetLoseRaceTicketPrice(int id, double rate) {
		L1ShopItem newItem = new L1ShopItem(id, 0, 1);// 승리표 판매 리스트 // 레이스표 매입
		_purchasingList.add(newItem);
		initShopNpc();
	}

	private int next_ticket_id(){
		return 8000000 + GetIssuedTicket() + 1;
	}
	
	private BugTicketInfo create_ticket(int racer_id, int pack_count, int converter_itemid, String color_code){
		BugTicketInfo tInfo = new BugTicketInfo();
		tInfo.itemId = next_ticket_id();
		tInfo.racerId = racer_id;
		tInfo.packCount = pack_count;
		tInfo.converter_itemid = converter_itemid == -1 ? tInfo.itemId : converter_itemid;
/*		if(pack_count > 1) {
			SaveRace(tInfo.itemId, String.format("%s%d-%d %s X %d", color_code, _round, _littleBugBear[racer_id].get_num(), _littleBugBear[racer_id].getName(), pack_count));	
		}*/
		if(pack_count == 100000) {
		SaveRace(tInfo.itemId, String.format("%s%d-%d %s [10만장]", color_code, _round, _littleBugBear[racer_id].get_num(), _littleBugBear[racer_id].getName()));
		} else if(pack_count == 50000) {
			SaveRace(tInfo.itemId, String.format("%s%d-%d %s [5만장]", color_code, _round, _littleBugBear[racer_id].get_num(), _littleBugBear[racer_id].getName()));
			} else if(pack_count == 20000) {
				SaveRace(tInfo.itemId, String.format("%s%d-%d %s [2만장]", color_code, _round, _littleBugBear[racer_id].get_num(), _littleBugBear[racer_id].getName()));
			} else 
			SaveRace(tInfo.itemId, String.format("%s%d-%d %s", color_code, _round, _littleBugBear[racer_id].get_num(), _littleBugBear[racer_id].getName()));
		m_tickets.put(tInfo.itemId, tInfo);
		return tInfo;
	}
	
	private L1ShopItem create_ticket_shop_item(BugTicketInfo tInfo){
		return new L1ShopItem(tInfo.itemId, 500 * tInfo.packCount, tInfo.packCount);
	}
	
	public void LoadNpcShopList() { // 버경 레이스표
		try {
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
			for (int i = 0; i < 5; i++) {
				BugTicketInfo tInfo = create_ticket(i, 1, -1, "");
				sellingList.add(create_ticket_shop_item(tInfo));
				int default_itemid = tInfo.itemId;
				_ticket[i] = default_itemid;
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 20000, default_itemid, "\\f=");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 50000, default_itemid, "\\f2");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 100000, default_itemid, "\\f3");
				sellingList.add(create_ticket_shop_item(tInfo));
			}

			L1Shop shop = new L1Shop(70035, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70035, shop);
			L1Shop shop1 = new L1Shop(70041, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70041, shop1);
			L1Shop shop2 = new L1Shop(70042, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70042, shop2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reLoadNpcShopList() {
		try {
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
			for (int i = 0; i < 5; i++) {
				BugTicketInfo tInfo = create_ticket(i, 1, -1, "");
				sellingList.add(create_ticket_shop_item(tInfo));
				int default_itemid = tInfo.itemId;
				_ticket[i] = default_itemid;				
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 20000, default_itemid, "\\f=");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 50000, default_itemid, "\\f2");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 100000, default_itemid, "\\f3");
				sellingList.add(create_ticket_shop_item(tInfo));
			}

			L1Shop shop = new L1Shop(70035, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70035, shop);
			L1Shop shop1 = new L1Shop(70041, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70041, shop1);
			L1Shop shop2 = new L1Shop(70042, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70042, shop2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SaveRace(int i, String j) {
		L1RaceTicket etcItem = new L1RaceTicket();
		etcItem.setType2(0);
		etcItem.setItemId(i);
		etcItem.setName(j);
		etcItem.setNameId(j);
		etcItem.setType(12);
		etcItem.setType1(12);
		etcItem.setMaterial(5);
		etcItem.setWeight(0);
		etcItem.set_price(1000);
		etcItem.setGfxId(143);//143
		etcItem.setGroundGfxId(151);
		etcItem.setMinLevel(0);
		etcItem.setMaxLevel(0);
		etcItem.setBless(1);
		etcItem.setTradable(false);
		etcItem.setDmgSmall(0);
		etcItem.setDmgLarge(0);
		etcItem.set_stackable(true);
		// ItemTable.getInstance().AddTicket(etcItem);
		AddTicket(etcItem);
	}

	public void goalIn(final int i) {
		synchronized (this) {
			_ranking = _ranking + 1;
			// broadcastNpc(_ranking + "위 - " + _littleBugBear[i].getNameId());
			if (_ranking == 1) {
				_first = _littleBugBear[i].getName();
				SetWinRaceTicketPrice(_ticket[i], _ration[i]);
				AddWinCount(i);
				GeneralThreadPool.getInstance().schedule(new Runnable(){
					@Override
					public void run(){
						우승자멘트(String.format("제 %d회 우승자는 '%s' 입니다.", _round, _littleBugBear[i].getNameId()));						
					}
				}, 700L);
				Updator.exec("insert into bug_history set round=?, winner_id=?, winner_name=?, total_ticket_count=?, winner_ticket_count=?, winner_ration=?, total_price=?, winner_price=?", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						int total_count = getTotalTicketCount();
						double winner_ration = _ration[i];
						pstm.setInt(++idx, _round);
						pstm.setInt(++idx, i);
						pstm.setString(++idx, _littleBugBear[i].getName());
						pstm.setInt(++idx, total_count);
						pstm.setInt(++idx, _ticketCount[i]);
						pstm.setDouble(++idx, winner_ration);
						pstm.setInt(++idx, total_count * 500);
						pstm.setInt(++idx, (int)(winner_ration * _ticketCount[i]));
					}
				});
			} else {
				SetLoseRaceTicketPrice(_ticket[i], _ration[i]);
				AddLoseCount(i);
			}
		}

		if (_ranking == 5) {
			_complete = true;
		}
	}

	public void wrapUpRace() throws Exception {
		_littleBugBear[0].deleteMe();
		_littleBugBear[1].deleteMe();
		_littleBugBear[2].deleteMe();
		_littleBugBear[3].deleteMe();
		_littleBugBear[4].deleteMe();
		_raceCount = _raceCount + 1;
		setBugState(2);
		broadcastNpc("다음 경기를 준비중입니다.");
	}

	public void BroadcastAllUser(String text) {
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			try {
				player.sendPackets(new S_SystemMessage(text));
			} catch (Exception exception) {
			}
		}
	}

	private void StartGame() {
		for (int i = 0; i < 5; ++i) {
			RunBug bug = new RunBug(i);

			GeneralThreadPool.getInstance().schedule(bug, 100);
		}
	}

	public class RunBug implements Runnable {
		private int _status = 0;

		private int[][] _BUG_INFO = { 
				{ 45, 4, 5, 6, 50 }, 
				{ 42, 6, 5, 7, 50 }, 
				{ 39, 8, 5, 8, 50 },
				{ 36, 10, 5, 9, 50 }, 
				{ 33, 12, 5, 10, 50 }
		};

		private int _bugId;
		private int _remainRacingCount;
		private Random _rndGen = new Random(System.nanoTime());

		public RunBug(int bugId) {
			_bugId = bugId;
			_remainRacingCount = _BUG_INFO[_bugId][0];
		}

		private boolean is_down(){
			boolean result = _is_downs[_bugId];
			_is_downs[_bugId] = false;
			return result;
		}
		
		@Override
		public void run() {
			try {
				switch (_status) {
				case 0: {
					if (_remainRacingCount == 0) {
						_remainRacingCount = _BUG_INFO[_bugId][1];
						_status = 1;
					} else {
						if(is_down() || (IS_AUTO_TUMBLE && _rndGen.nextInt(MAX_TUMBLE_PROBABILITY) < 1 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
/*						if () {*/
							_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
							GeneralThreadPool.getInstance().schedule(this, 2000 - (int) (_winRate[_bugId]) * 5);
						} else {
							_littleBugBear[_bugId].setDirectionMove(6);
							--_remainRacingCount;
							GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
						}
						break;
					}
				}
				case 1: {
					if (_remainRacingCount == 0) {
						_remainRacingCount = _BUG_INFO[_bugId][2];
						_status = 2;
					} else {
						if(is_down() || (IS_AUTO_TUMBLE && _rndGen.nextInt(MAX_TUMBLE_PROBABILITY) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
//						if () {
							_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
							// GeneralThreadPool.getInstance().schedule(this,
							// 3500 - (int)(_winRate[_bugId]) * 5);
							GeneralThreadPool.getInstance().schedule(this, 2000 - (int) (_winRate[_bugId]) * 5);
						} else {
							_littleBugBear[_bugId].setDirectionMove(7);
							--_remainRacingCount;

							GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
						}
						break;
					}
				}
				case 2: {
					if (_remainRacingCount == 0) {
						_remainRacingCount = _BUG_INFO[_bugId][3];
						_status = 3;
					} else {
						if(is_down() || (IS_AUTO_TUMBLE && _rndGen.nextInt(MAX_TUMBLE_PROBABILITY) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
//						if () {
							_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
							GeneralThreadPool.getInstance().schedule(this, 2000 - (int) (_winRate[_bugId]) * 5);
						} else {
							_littleBugBear[_bugId].setDirectionMove(0);
							--_remainRacingCount;

							GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
						}
						break;
					}
				}
				case 3: {
					if (_remainRacingCount == 0) {
						_status = 4;
					} else {
						if(is_down() || (IS_AUTO_TUMBLE && _rndGen.nextInt(MAX_TUMBLE_PROBABILITY) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
//						if () {
							_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
							GeneralThreadPool.getInstance().schedule(this, 2000 - (int) (_winRate[_bugId]) * 5);
						} else {
							_littleBugBear[_bugId].setDirectionMove(1);
							--_remainRacingCount;

							GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
						}
						break;
					}
				}
				case 4: {
					if (_littleBugBear[_bugId].getX() == 33527) {
						goalIn(_bugId);
					} else if(is_down() || (IS_AUTO_TUMBLE && _littleBugBear[_bugId].getX() < 33522 && _rndGen.nextInt(MAX_TUMBLE_PROBABILITY) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
						_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
						GeneralThreadPool.getInstance().schedule(this, 2000 - (int) (_winRate[_bugId]) * 5);
					} else {
						_littleBugBear[_bugId].setDirectionMove(2);
						--_remainRacingCount;

						GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
					}
					break;
				}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public int getTotalTicketCount() {
		int total = 0;
		for (int row = 0; row < 5; row++) {
			total += this._ticketCount[row];
		}
		return total;
	}

	public int getBugState() {
		return this._bugRaceState;
	}

	public void setBugState(int state) {
		this._bugRaceState = state;
	}

	public int getRaceCount() {
		return this._raceCount;
	}

	public void setRaceCount(int cnt) {
		this._raceCount = cnt;
	}

	/* 버경 추가 */
	public void AddTicket(L1RaceTicket race) {
		_race.put(new Integer(race.getItemId()), race);
		ItemTable.getInstance().getAllTemplates().put(race.getItemId(), race);
	}

	public int GetIssuedTicket() {
		return _race.size();
	}
	
	static class BugStruct{
		public BugStruct(int id, int gfx, String name){
			this.id = id;
			this.gfx = gfx;
			this.name = name;
		}
		public int id;
		public int gfx;
		public String name;
	}
}
