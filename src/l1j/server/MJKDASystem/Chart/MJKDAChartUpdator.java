package l1j.server.MJKDASystem.Chart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;

import l1j.server.MJKDASystem.MJKDA;
import l1j.server.MJKDASystem.MJKDALoadManager;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_ChartRank;
import l1j.server.server.serverpackets.ServerBasePacket;
/**********************************
 * 
 * MJ Kill Death Assist Chart System updator.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJKDAChartUpdator {
	private boolean 			_isUpdate;
	private Calendar 			_updateCal;
	private ServerBasePacket	_pck;
	private Comparator<MJKDA>	_sorter;
	public MJKDAChartUpdator(){
		_sorter = new ChartSorter();
		updateCal();
	}
	
	public void dispose(){
		if(_pck != null){
			_pck.clear();
			_pck = null;
		}
		if(_updateCal != null){
			_updateCal.clear();
			_updateCal = null;
		}
		_sorter 	= null;
		_isUpdate 	= true;
	}
	
	public void updateCal(){
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		if(_updateCal != null){
			_updateCal.clear();
			_updateCal = null;
		}
		_updateCal = (Calendar)cal.clone();
		_updateCal.add(Calendar.SECOND, MJKDALoadManager.KDA_CHART_DELAY_SEC);
	}
	
	public boolean isBefore(Calendar cal){
		if(_updateCal == null)
			return false;
		
		return _updateCal.before(cal);
	}
	
	public void update(){
		if(!_isUpdate){
			_isUpdate = true;
			GeneralThreadPool.getInstance().execute(new UpdateChart());
		}
	}
	
	public void onLoginUser(L1PcInstance pc){
		if(_pck != null)
			pc.sendPackets(_pck, false);
	}
	
	class UpdateChart implements Runnable{
		@Override
		public void run() {
			ServerBasePacket pck = _pck;
			try{
				ArrayList<MJKDA> list = MJKDALoader.getInstance().createSnapshot();
				if(list == null || list.size() <= 0){
					_pck = S_ChartRank.getZero();
				}else{
					list.sort(_sorter);
					_pck = S_ChartRank.get(list);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(pck != null){
					pck.clear();
					pck = null;
				}
				
				if(_pck != null){
					Collection<L1PcInstance> cols = L1World.getInstance().getAllPlayers();
					for (L1PcInstance pc : cols) {
						if (pc == null || pc.getAI() != null || pc.getKDA() == null || !pc.getKDA().isChartView)
							continue;
						
						pc.sendPackets(_pck, false);
					}
				}
				updateCal();
				_isUpdate = false;
			}
		}
	}
	
	class ChartSorter implements Comparator<MJKDA>{
		@Override
		public int compare(MJKDA o1, MJKDA o2) {
			if(o1.kill > o2.kill)
				return -1;
			else if(o1.kill < o2.kill)
				return 1;
			return 0;
		}
	}
}
