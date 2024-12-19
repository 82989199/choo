package l1j.server.server.Thread;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class MasterRingThread extends Thread{
	
	private static MasterRingThread _instance;	
	public static MasterRingThread getInstance() {
		if (_instance == null) {
			_instance = new MasterRingThread();
		}
		return _instance;
	}
	
	public MasterRingThread(){
		this.start();
	}
	
	public void run() {
		while(true){
			try{
				sleep(1000);				
				for(L1PcInstance _pc : L1World.getInstance().getAllPlayers()){
					try{
					if (_pc.getNetConnection() != null) {						
						if(_pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)){
							if(_pc.getInventory().checkItem(900075) == false){
								_pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
							}
						}
					}
					}catch(Exception e){}
				}
			}catch(Exception e){e.printStackTrace();}
			
		}
	}

}
