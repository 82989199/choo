package l1j.server.MJBotSystem.Business;

import java.util.PriorityQueue;

import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.Pool.MJBotHuntAIPool;
import l1j.server.MJBotSystem.Pool.MJBotScarecrowAIPool;
import l1j.server.MJBotSystem.Pool.MJBotWanderAIPool;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;

/**********************************
 * 
 * MJ ai business logic realize.
 * made by mjsoft, 2016.
 *  
 **********************************/
public abstract class MJAIBusiness implements Runnable {
	
	protected boolean						_isrun;
	protected Object						_lock;
	protected PriorityQueue<MJBotMovableAI>	_workQ;
	
	public MJAIBusiness(int capacity){
		_isrun 	= false;
		_lock	= new Object();
		_workQ	= new PriorityQueue<MJBotMovableAI>(capacity);
	}
	
	public void stop(){
		_isrun = false;
		if(_workQ != null){
			synchronized(_lock){
				_workQ.clear();
			}
		}
	}
	
	public void addWork(MJBotMovableAI ai){
		if(ai == null) return;
		
		synchronized(_lock){
			_workQ.add(ai);
			if(!_isrun){
				_isrun = true;
				GeneralThreadPool.getInstance().execute(this);
			}
		}
	}
	
	public MJBotMovableAI getWork(){
		synchronized(_lock){
			return _workQ.poll();
		}
	}

	public void removeWork(MJBotMovableAI ai){
		ai.setRemoved(true);
	}
	
	@Override
	public void run(){
		long cur = 0L;
		long err = 0L;
		try{
			while(_isrun){
				cur = System.currentTimeMillis();
				err = 0;
				while(!_workQ.isEmpty()){
					MJBotMovableAI ai = getWork();
					try{
						if(ai == null)
							continue;
						
						if(ai.isRemoved()){
							MJBotType 		type = ai.getBotType();
							L1PcInstance 	body = ai.getBody();
							if(type == MJBotType.HUNT)
								MJBotHuntAIPool.getInstance().push(ai);
							else if(type == MJBotType.SCARECROW)
								MJBotScarecrowAIPool.getInstance().push(ai);
							else if(type == MJBotType.WANDER)
								MJBotWanderAIPool.getInstance().push(ai);
							else if(type == MJBotType.SIEGELEADER || type == MJBotType.REDKNIGHT || type == MJBotType.PROTECTOR || type == MJBotType.ILLUSION){
								ai.dispose();
							}
							if(body != null){
								L1Clan clan = body.getClan();
								if(clan != null){
									clan.removeClanMember(body.getName());
								}
								MJBotUtil.deleteBot(body);
							}
							continue;
						}
						
						if(ai.getActSleepMs() > cur){
							addWork(ai);
							err = ai.getActSleepMs() - cur;
							break;
						}
						toWork(ai, cur);
					}catch(Exception e){
						e.printStackTrace();
						if(ai != null)
							addWork(ai);
					}
				}
				if(err > 0)
					Thread.sleep(err);
				else if(_workQ.isEmpty())
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		_isrun = false;	
	}
	
	protected void toWork(MJBotMovableAI ai, long cur){
		L1PcInstance body = ai.getBody();
		if(body == null){
			MJAIScheduler.getInstance().removeSchedule(ai);
			return;
		}
		addWork(ai);
	}
}
