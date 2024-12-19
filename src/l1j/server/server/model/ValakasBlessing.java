package l1j.server.server.model;

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1PcInstance;

public class ValakasBlessing extends TimerTask{
	private L1PcInstance _pc;
	public ValakasBlessing(L1PcInstance pc){
		_pc = pc;
	}
	@Override
	public void run() {
		try{
			if(_pc == null || _pc.isDead())
				return;
			
			regenManaPoint();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void regenManaPoint(){
		_pc.setCurrentMp(_pc.getCurrentMp() + 4);
	}
}
