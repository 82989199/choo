package l1j.server.MJBotSystem.Pool;

import java.util.ArrayDeque;

import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotWanderAI;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;

public class MJBotWanderAIPool {
	private static MJBotWanderAIPool	_instance;
	public static MJBotWanderAIPool getInstance(){
		if(_instance == null)
			_instance = new MJBotWanderAIPool();
		return _instance;
	}
	private ArrayDeque<MJBotAI> _aiQ;
	private Object				_lock;
	
	private MJBotWanderAIPool(){
		_lock 	= new Object();
		_aiQ 	= new ArrayDeque<MJBotAI>(MJBotLoadManager.MBO_WANDERAI_POOL_SIZE);
		for(int i=0; i<MJBotLoadManager.MBO_WANDERAI_POOL_SIZE; i++)
			_aiQ.push(new MJBotWanderAI());
	}
	
	public MJBotAI pop(){
		synchronized(_lock){
			if(_aiQ.isEmpty())
				return new MJBotWanderAI();
			return _aiQ.pop();
		}
	}
	
	public void push(MJBotAI ai){
		if(ai == null)
			return;
		
		ai.dispose();
		synchronized(_lock){
			_aiQ.push(ai);
		}
	}
}
