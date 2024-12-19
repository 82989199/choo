package l1j.server.MJBookQuestSystem.Loader;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class BQSInformationLoader {
	private static BQSInformationLoader _instance;
	public static BQSInformationLoader getInstance(){
		if(_instance == null)
			_instance = new BQSInformationLoader();
		return _instance;
	}
	
	public static void reload(){
		BQSInformationLoader tmp = _instance;
		_instance = new BQSInformationLoader();
		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}
	
	private HashMap<Integer, BQSInformation> _informations;
	private HashMap<Integer, BQSInformation> _informations_for_npc_id;
	private HashMap<Integer, ArrayList<BQSInformation>> _week_informations;
	private BQSInformationLoader(){
		loadInformations();
		mappedNpcInformation();
	}
	
	private void loadInformations(){
		Selector.exec("select * from tb_mbook_information", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception{
				int size = SQLUtil.calcRows(rs);
				_informations = new HashMap<Integer, BQSInformation>(size);
				_informations_for_npc_id = new HashMap<Integer, BQSInformation>(size); 
				_week_informations = new HashMap<Integer, ArrayList<BQSInformation>>(3);
				while(rs.next()){
					BQSInformation information = BQSInformation.newInstance(rs);
					_informations.put(information.getCriteriaId(), information);
					_informations_for_npc_id.put(information.getNpcId(), information);
					mappedWeekQuestInformation(information);
				}
			}
		});
	}
	
	private void mappedWeekQuestInformation(BQSInformation information){
		int week_difficulty = information.getWeekDifficulty();
		if(week_difficulty >= 0 && week_difficulty < 3){
			ArrayList<BQSInformation> wInformations = _week_informations.get(week_difficulty);
			if(wInformations == null){
				wInformations = new ArrayList<BQSInformation>(8);
				_week_informations.put(week_difficulty, wInformations);
			}
			wInformations.add(information);
		}
	}
	
	public void mappedNpcInformation(){
		for(BQSInformation mInfo : _informations.values()){
			L1Npc npc = NpcTable.getInstance().getTemplate(mInfo.getNpcId());
			if(npc == null){
				System.out.println(String.format("[도감]엔피씨 템플릿을 찾을 수 없습니다. 북아이디 : %d(%d), 북에 할당된 엔피씨 아이디 : %d(%s)", mInfo.getCriteriaId(), mInfo.getWeekDifficulty(), mInfo.getNpcId(), mInfo.getNpcName()));
				continue;
			}
		}
	}
	
	public ArrayList<BQSInformation> createShuffleWeeks(int difficulty){
		ArrayList<BQSInformation> weeks = createWeeks(difficulty);
		Collections.shuffle(weeks);
		return weeks;
	}
	
	public ArrayList<BQSInformation> createWeeks(int difficulty){
		return new ArrayList<BQSInformation>(_week_informations.get(difficulty));
	}
	
	public BQSInformation getInformation(int criteria_id){
		return _informations.get(criteria_id);
	}
	
	public BQSInformation getInformationFromNpcId(int npc_id){
		return _informations_for_npc_id.get(npc_id);
	}
	
	public void dispose(){
		if(_informations != null){
			_informations.clear();
			_informations = null;
		}
		
		if(_informations_for_npc_id != null){
			_informations_for_npc_id.clear();
			_informations_for_npc_id = null;
		}
		
		if(_week_informations != null){
			for(ArrayList<BQSInformation> list : _week_informations.values())
				list.clear();
			_week_informations.clear();
			_week_informations = null;
		}
	}
}
