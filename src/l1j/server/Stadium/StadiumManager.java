package l1j.server.Stadium;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;

public class StadiumManager {
	// 竞技场地图ID数组
	private static final int[] m_index_map_mapped = new int[]{
		0, 
		13051,
		13052,
		13053,
		
	};
	// 竞技场名称数组
	private static final String[] m_index_name_mapped = new String[]{
			"无", // 없음
			"最终决战(13051)", // 최후의결판
			"竞技场(13052)", // 아레나
			"陷阱战(13053)", // 트랩전
		};
	private static StadiumManager _instance;
	public static StadiumManager getInstance(){
		if(_instance == null)
			_instance =new StadiumManager();
		return _instance;
	}
	
	// 竞技场对象映射表
	private ConcurrentHashMap<Integer, StadiumObject> m_stadiums;
	private StadiumManager(){
		m_stadiums = new ConcurrentHashMap<Integer, StadiumObject>();
	}
	
	// 注册竞技场
	public void regist(StadiumObject obj){
		m_stadiums.put(obj.get_current_play_map_id(), obj);
	}
	
	// 移除竞技场
	public void remove(StadiumObject obj){
		m_stadiums.remove(obj.get_current_play_map_id());
	}
	
	// 检查竞技场是否正在进行
	public boolean is_on_stadium(int map_id){
		StadiumObject obj = m_stadiums.get(map_id);
		return obj != null && obj.is_on();
	}
	
	// 检查是否在竞技场中
	public boolean is_in_stadium(int map_id){
		StadiumObject obj = m_stadiums.get(map_id);
		return obj != null;
	}
	
	// 获取竞技场对象
	public StadiumObject get_stadium(int map_id){
		return m_stadiums.get(map_id);
	}
	
	// 开启竞技场
	public void open_stadium(L1PcInstance gm, String param){
		if(param == null || param.equals("")){
			gm.sendPackets(".竞技场 [竞技场编号]"); // .경기장 [경기장 번호]
			int size = m_index_name_mapped.length;
			for(int i=1; i<size; ++i)
				gm.sendPackets(String.format("%d - %s : %s", i, m_index_name_mapped[i], is_on_stadium(m_index_map_mapped[i]) ? "比赛中" : "等待中")); // 경기중 : 대기중
			return;
		}
		
		StringTokenizer token = new StringTokenizer(param);
		int number = Integer.parseInt(token.nextToken());
		if(number <= 0 || number >= m_index_map_mapped.length){
			gm.sendPackets(String.format("%d 号竞技场不存在。", number)); // 경기장은 존재하지 않습니다
			return;
		}
		
		int map_id = m_index_map_mapped[number];
		if(is_in_stadium(map_id)){
			gm.sendPackets(String.format("%d(%d) 号竞技场已在进行中。", number, map_id)); // 경기장은 이미 진행중입니다
			return;			
		}
		StadiumObject obj = new StadiumObject(map_id, 10, 1800, m_index_name_mapped[number]);
		obj.execute();
	}
	
	// 结束竞技场
	public void quit_stadium(L1PcInstance gm, String param){
		StringTokenizer token = new StringTokenizer(param);
		int number = Integer.parseInt(token.nextToken());
		if(number <= 0 || number >= m_index_map_mapped.length){
			gm.sendPackets(String.format("%d 号竞技场不存在。", number)); // 경기장은 존재하지 않습니다
			return;
		}
		
		int map_id = m_index_map_mapped[number];
		StadiumObject obj = get_stadium(map_id);
		if(obj == null || !obj.is_on()){
			gm.sendPackets(String.format("%d(%d) 号竞技场已结束或尚未开始。", number, map_id)); // 경기장은 이미 종료되었거나 시작되지 않았습니다
			return;			
		}
		try {
			obj.do_ended();
			gm.sendPackets(String.format("%d(%d) %s比赛已结束。", number, map_id, m_index_name_mapped[number])); // 경기가 종료되었습니다
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
