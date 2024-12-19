package l1j.server.MJBotSystem.Pool;

import java.util.ArrayDeque;

import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotHuntAI;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;

public class MJBotHuntAIPool {
	private static MJBotHuntAIPool	_instance;
	public static MJBotHuntAIPool getInstance(){
		if(_instance == null)
			_instance = new MJBotHuntAIPool();
		return _instance;
	}
	private ArrayDeque<MJBotAI> _aiQ;
	private Object				_lock;
	
	private MJBotHuntAIPool(){
		_lock 	= new Object();
		_aiQ 	= new ArrayDeque<MJBotAI>(MJBotLoadManager.MBO_HUNTAI_POOL_SIZE);
		for(int i=0; i<MJBotLoadManager.MBO_HUNTAI_POOL_SIZE; i++)
			_aiQ.push(new MJBotHuntAI());
	}
	
	public MJBotAI pop(){
		synchronized(_lock){
			if(_aiQ.isEmpty())
				return new MJBotHuntAI();
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
