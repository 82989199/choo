package l1j.server.MJBotSystem.Pool;

import java.util.ArrayDeque;

import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotScarecrowAI;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;

public class MJBotScarecrowAIPool {
	private static MJBotScarecrowAIPool	_instance;
	public static MJBotScarecrowAIPool getInstance(){
		if(_instance == null)
			_instance = new MJBotScarecrowAIPool();
		return _instance;
	}
	private ArrayDeque<MJBotAI> _aiQ;
	private Object				_lock;
	
	private MJBotScarecrowAIPool(){
		_lock 	= new Object();
		_aiQ 	= new ArrayDeque<MJBotAI>(MJBotLoadManager.MBO_SCARECROWAI_POOL_SIZE);
		for(int i=0; i<MJBotLoadManager.MBO_SCARECROWAI_POOL_SIZE; i++)
			_aiQ.push(new MJBotScarecrowAI());
	}
	
	public MJBotAI pop(){
		synchronized(_lock){
			if(_aiQ.isEmpty())
				return new MJBotScarecrowAI();
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
