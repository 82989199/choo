package l1j.server.MJSurveySystem;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJSurveySystemLoader {
	private static MJSurveySystemLoader _instance;
	public static MJSurveySystemLoader getInstance(){
		if(_instance == null)
			_instance = new MJSurveySystemLoader();
		return _instance;
	}
	
	private ConcurrentHashMap<Integer, MJInterfaceSurvey> _surveys;
	private MJSurveySystemLoader(){
		_surveys = new ConcurrentHashMap<Integer, MJInterfaceSurvey>(256);
	}
	
	public boolean isSurvey(int num){
		return _surveys.containsKey(num);
	}
	
	public boolean submitSurvey(L1PcInstance pc, int num, boolean isYes){
		MJInterfaceSurvey survey = _surveys.get(num);
		if(survey != null){
			survey.survey(pc, num, isYes);
			return true;
		}
		return false;
	}
	
	public ServerBasePacket registerSurvey(String message, int num, MJInterfaceSurvey survey, long remainMillis){
		MJInterfaceSurvey old = _surveys.get(num);
		if(old != null)
			return null;
		
		_surveys.put(num, survey);
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				_surveys.remove(num);
			}
		}, remainMillis);
		return new S_Message_YN(num, 622, message);
	}
}
