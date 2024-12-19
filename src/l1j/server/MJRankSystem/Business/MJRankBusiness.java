package l1j.server.MJRankSystem.Business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MY_RANKING_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_ACK.ResultCode;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;

public class MJRankBusiness implements TimeListener{
	private static MJRankBusiness _instance;
	public static MJRankBusiness getInstance(){
		if(_instance == null)
			_instance = new MJRankBusiness();
		return _instance;
	}
	
	private int _accumulate_second;
	private int _expendiant;
	private long _version;
	private ProtoOutputStream _nowNotServiceAck;
	private ProtoOutputStream[][] _acks;
	private SC_TOP_RANKER_ACK[][] _rankInfos;
	private ArrayBlockingQueue<Boolean>	_signal;
	private SC_TOP_RANKER_NOTI	_lastTopRanker;
	private boolean	_isrun;
	private MJRankBusiness(){
		_accumulate_second = 0;
		_signal = new ArrayBlockingQueue<Boolean>(1);
		
		SC_TOP_RANKER_ACK ack = SC_TOP_RANKER_ACK.newInstance();
		ack.set_result_code(ResultCode.RC_NOW_NOT_SERVICE);
		_nowNotServiceAck = ack.writeTo(MJEProtoMessages.SC_TOP_RANKER_ACK);
		ack.dispose();
		
		_acks = new ProtoOutputStream[9][2];
		_rankInfos = new SC_TOP_RANKER_ACK[9][2];
		for(int i=8; i>=0; --i){
			_rankInfos[i][0] = SC_TOP_RANKER_ACK.newInstance(i, 2, 1);
			_rankInfos[i][1] = SC_TOP_RANKER_ACK.newInstance(i, 2, 2);
		}
	}
	
	public void run(){
		_isrun = true;
		GeneralThreadPool.getInstance().execute(new MJRankConsumer());
		_accumulate_second = 0;
		_signal.offer(Boolean.TRUE);
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);		
	}
	
	public void dispose(){
		_isrun = false;
		RealTimeClock.getInstance().removeListener(this, Calendar.SECOND);
	}
	
	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHourChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		if(_isrun && ++_accumulate_second >= MJRankLoadManager.MRK_SYS_UPDATE_CLOCK){
			_accumulate_second = 0;
			_signal.offer(Boolean.TRUE);
		}
	}
	
	public void on_update(){
		_accumulate_second = 0;
		_signal.offer(Boolean.TRUE);
	}

	class MJRankConsumer implements Runnable{
		@Override
		public void run() {
			try{
				while(_isrun){
					@SuppressWarnings("unused")
					Boolean signal = _signal.take();
					if(!_isrun)
						return;

					initializeRankerInfo();
					doUpdateRank();
					makeStream();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}	
	
	private void initializeRankerInfo(){
		_version = System.currentTimeMillis();
		for(int i=8; i>=0; --i){
			_rankInfos[i][0].clearRankers();
			_rankInfos[i][0].set_version(_version);
			_rankInfos[i][1].clearRankers();
			_rankInfos[i][1].set_version(_version);
		}
	}
	
	private void makeStream(){
		if(!MJRankLoadManager.MRK_SYS_ISON)
			return;
		
		ProtoOutputStream[][] acks = new ProtoOutputStream[9][2];
		for(int i=8; i>=0; --i){
			int totalPage = 1;
			if(_rankInfos[i][1].get_rankers() != null && _rankInfos[i][1].get_rankers().size() > 0){
				totalPage = 2;
				_rankInfos[i][1].set_current_page(2);
				_rankInfos[i][1].set_total_page(2);
				acks[i][1] = _rankInfos[i][1].writeTo(MJEProtoMessages.SC_TOP_RANKER_ACK);
			}
			if(_rankInfos[i][0].get_rankers() != null && _rankInfos[i][0].get_rankers().size() > 0){
				_rankInfos[i][0].set_current_page(1);
				_rankInfos[i][0].set_total_page(totalPage);
				acks[i][0] = _rankInfos[i][0].writeTo(MJEProtoMessages.SC_TOP_RANKER_ACK);
			}
		}
		
		ProtoOutputStream[][] tmp = _acks;
		_acks = acks;
		
		for(int i=8; i>=0; --i){
			for(int j=1; j>=0; --j){
				if(tmp[i][j] != null){
					tmp[i][j].dispose();
					tmp[i][j] = null;
				}
			}
		}
	}
	
	private void doUpdateRank(){
		ArrayList<SC_TOP_RANKER_NOTI> rankers = MJRankUserLoader.getInstance().createSortSnapshot();
		if(rankers == null)
			return;
		
		int[] currentPlace = new int[]{1,1,1,1,1,1,1,1,1};
		SC_TOP_RANKER_NOTI[] prevRankers = new SC_TOP_RANKER_NOTI[9];
		
		try{
			int size = rankers.size();
			for(int i=0; i<size; ++i){
				SC_TOP_RANKER_NOTI rnk = rankers.get(i);
				int class_id = rnk.get_class();
				int current_class_place = currentPlace[class_id];
				int current_total_place = currentPlace[8];
				if(!rnk.isInRank()){
					rnk.updatePlace();
					rnk.updateAlmost();
				}else{
					rnk.updatePlace(current_class_place, current_total_place);
					rnk.updateRating(current_class_place, current_total_place);
					SC_TOP_RANKER_NOTI prev_class = prevRankers[class_id];
					SC_TOP_RANKER_NOTI prev_total = prevRankers[8];
					rnk.updateAlmost(prev_class, prev_total);
					if(current_class_place <= MJRankLoadManager.MRK_SYS_CLASS_RANGE){
						_rankInfos[class_id][current_class_place > 100 ? 1 : 0].add_rankers(rnk.get_class_ranker());
						prevRankers[class_id] = rnk;
						currentPlace[class_id] = ++current_class_place;
					}
					
					if(current_total_place <= MJRankLoadManager.MRK_SYS_TOTAL_RANGE){
						if(current_total_place == 1)
							_lastTopRanker = rnk;
						
						_rankInfos[8][current_total_place > 100 ? 1 : 0].add_rankers(rnk.get_total_ranker());
						prevRankers[8] = rnk;
						
						if(current_total_place == MJRankLoadManager.MRK_SYS_RANK_POTION - 1)
							_expendiant = rnk.get_exp();
						currentPlace[8] = ++current_total_place;
					}
				}
				rnk.buff();
				
			}
			rankers.clear();
			rankers = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int getExpendiant(){
		return _expendiant;
	}
	
	public boolean onExpendiant(L1PcInstance pc){
		try{
			if(pc == null)
				return false;
			
			if(_expendiant <= 0 || !MJRankLoadManager.MRK_SYS_ISON){
				pc.sendPackets("운영자에 의해 랭킹 시스템이 중단되어 있는 상태입니다.");
				return false;
			}
			
			if(pc.isGm()){
				pc.sendPackets("GM은 해당 아이템을 사용할 수 없습니다.");
				return false;
			}
			
			SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
			if(noti == null){
				noti = MJRankUserLoader.getInstance().create_user_information(pc);
			}else{
				if(noti.get_total_ranker().get_rank() <= MJRankLoadManager.MRK_SYS_RANK_POTION){
					pc.sendPackets("진입하려는 랭킹보다 현재 랭킹이 높습니다.");
					return false;
				}
			}
			
			pc.setExp((int)(_expendiant + 5));
			pc.sendPackets(String.format("%s님의 경험치가 랭킹 %d위의 경험치로 재조정 됩니다.", pc.getName(), MJRankLoadManager.MRK_SYS_RANK_POTION));
			return true;
		}catch(Exception e){
			e.printStackTrace();
			pc.sendPackets("운영자에 의해 랭킹 시스템이 중단되어 있는 상태입니다.");
			return false;
		}
	}
	
	public void noti(GameClient clnt, int objid){
		SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(objid);
		if(noti != null)
			clnt.sendPacket(noti.writeTo(MJEProtoMessages.SC_TOP_RANKER_NOTI), true);
	}
	
	public void ack_private_version(L1PcInstance pc, long version){
		if(!MJRankLoadManager.MRK_SYS_ISON){
			SC_MY_RANKING_ACK.send_not_service(pc);
		}else if(_version == version){
			SC_MY_RANKING_ACK.send(pc);
		}else{
			SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().getRankNoti(pc);
			if(noti != null){
				SC_MY_RANKING_ACK.send(pc, noti, _version);
			}
		}
	}
	
	public void ack(L1PcInstance pc, int classId, long version){
		if(!MJRankLoadManager.MRK_SYS_ISON)
			pc.sendPackets(_nowNotServiceAck, false);
		else if(version == _version){
			SC_TOP_RANKER_ACK ack = SC_TOP_RANKER_ACK.newInstance(version);
			pc.sendPackets(ack, MJEProtoMessages.SC_TOP_RANKER_ACK, true);
		}else{
			int sending = 0;
			for(int i=0; i<2; ++i){
				ProtoOutputStream stream = _acks[classId][i];
				if(stream != null){
					pc.sendPackets(stream, false);
					++sending;
				}
			}
			if(sending == 0)
				pc.sendPackets(_nowNotServiceAck, false);
		}
	}
	
	public SC_TOP_RANKER_NOTI getLastTopRanker(){
		return _lastTopRanker;
	}
}
