package l1j.server.MJBotSystem.Pool;

import java.util.ArrayDeque;

import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotFishAI;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;

public class MJBotFishAIPool {
	private static MJBotFishAIPool	_instance;
	public static MJBotFishAIPool getInstance(){
		if(_instance == null)
			_instance = new MJBotFishAIPool();
		return _instance;
	}
	private ArrayDeque<MJBotAI> _aiQ;
	private Object				_lock;
	
	private MJBotFishAIPool(){
		_lock 	= new Object();
		_aiQ 	= new ArrayDeque<MJBotAI>(MJBotLoadManager.MBO_FISHAI_POOL_SIZE);
		for(int i=0; i<MJBotLoadManager.MBO_FISHAI_POOL_SIZE; i++)
			_aiQ.push(new MJBotFishAI());
	}
	
	public MJBotAI pop(){
		synchronized(_lock){
			if(_aiQ.isEmpty())
				return new MJBotFishAI();
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
