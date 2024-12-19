package l1j.server.MJRankSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class MJRankUserLoader {
	private static MJRankUserLoader _instance;
	public static MJRankUserLoader getInstance(){
		if(_instance == null)
			_instance = new MJRankUserLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJRankUserLoader tmp = _instance;
		_instance = new MJRankUserLoader();

		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	private ConcurrentHashMap<Integer, SC_TOP_RANKER_NOTI> _ranks;
	private ArrayList<SC_TOP_RANKER_NOTI> _dummys;
	private MJRankUserLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		try{
			_dummys = new ArrayList<SC_TOP_RANKER_NOTI>(MJRankLoadManager.MRK_SYS_TOTAL_RANGE);
			for(int i=MJRankLoadManager.MRK_SYS_TOTAL_RANGE - 1; i>=0; --i)
				_dummys.add(SC_TOP_RANKER_NOTI.newDummyInstance());
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from characters where level>=? and Banned=?");
			pstm.setInt(1, MJRankLoadManager.MRK_SYS_MINLEVEL);
			pstm.setInt(2, 0);
			rs = pstm.executeQuery();
			int rows = SQLUtil.calcRows(rs);
			_ranks = new ConcurrentHashMap<Integer, SC_TOP_RANKER_NOTI>(Config.MAX_ONLINE_USERS > rows ? Config.MAX_ONLINE_USERS : rows);
			while(rs.next()){
				if(rs.getInt("AccessLevel") == Config.GMCODE)
					continue;
				
				if(isBanAccounts(rs.getString("account_name")))
					continue;
				
				put(SC_TOP_RANKER_NOTI.newInstance(rs));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private boolean isBanAccounts(String login){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		try{
			con						= L1DatabaseFactory.getInstance().getConnection();
			pstm					= con.prepareStatement("select Banned from accounts where login=?");			
			pstm.setString(1, login);
			rs						= pstm.executeQuery();
			if(rs.next())
				return rs.getInt("Banned") != 0;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	public SC_TOP_RANKER_NOTI create_user_information(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = SC_TOP_RANKER_NOTI.newInstance(pc);
		put(noti);
		return noti;
	}
	
	public void onUser(L1PcInstance pc){
		if(pc == null || pc.isGm() || pc.is_shift_client()){
			return;
		}
		
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		if(noti == null){
			if(pc.getLevel() < MJRankLoadManager.MRK_SYS_MINLEVEL)
				return;
			
			noti = create_user_information(pc);
			noti.onBuff();
		}else if(noti.get_characterInstance() == null){
			noti.set_characterInstance(pc);
			noti.onBuff();
		}else{
			if(!pc.is_ranking_buff()){
				noti.set_characterInstance(pc);
				noti.onBuff();
			}
		}
		if(!noti.get_class_ranker().get_name().equalsIgnoreCase(pc.getName()))
			noti.get_class_ranker().set_name(pc.getName());
		if(!noti.get_total_ranker().get_name().equalsIgnoreCase(pc.getName()))
			noti.get_total_ranker().set_name(pc.getName());
	}
	
	public void offUser(L1PcInstance pc){
		if(pc == null || pc.isGm())
			return;
		
		pc.getInventory().findAndRemoveItemId(MJRankLoadManager.MRK_TOPPROTECTION_ID);
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		if(noti != null)
			noti.set_characterInstance(null);
	}
	
	public void banUser(L1PcInstance pc){
		if(pc == null || pc.isGm())
			return;
		pc.getInventory().findAndRemoveItemId(MJRankLoadManager.MRK_TOPPROTECTION_ID);
		SC_TOP_RANKER_NOTI noti = remove(pc.getId());
		if(noti != null)
			noti.set_characterInstance(null);
	}
	
	public void removeUser(L1PcInstance pc){
		if(pc == null || pc.isGm())
			return;
		
		pc.getInventory().findAndRemoveItemId(MJRankLoadManager.MRK_TOPPROTECTION_ID);
		SC_TOP_RANKER_NOTI noti = remove(pc.getId());
		if(noti != null)
			noti.dispose();
	}

	public void offBuff(){
		GeneralThreadPool.getInstance().execute(new Runnable(){
			@Override
			public void run(){
				try{
					createStream()
					.filter((SC_TOP_RANKER_NOTI noti) -> noti.get_characterInstance() != null)
					.forEach((SC_TOP_RANKER_NOTI noti) ->{
						try{
							noti.offBuff();
						}catch(Exception e){
							e.printStackTrace();
						}
					});
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public Stream<SC_TOP_RANKER_NOTI> createStream(){
		return _ranks.values().size() >= 1024 ? _ranks.values().parallelStream() : _ranks.values().stream();
	}
	
	public SC_TOP_RANKER_NOTI get(int id){
		return _ranks.get(id);
	}
	
	public void put(SC_TOP_RANKER_NOTI rnk){
		_ranks.put(rnk.get_objectId(), rnk);
	}
	
	public SC_TOP_RANKER_NOTI remove(int id){
		return _ranks.remove(id);
	}
	
	public SC_TOP_RANKER_NOTI remove(L1PcInstance pc){
		return _ranks.remove(pc.getId());
	}
	
	public SC_TOP_RANKER_NOTI remove(SC_TOP_RANKER_NOTI rnk){
		return _ranks.remove(rnk.get_objectId());
	}
	
	public ArrayList<SC_TOP_RANKER_NOTI> createSortSnapshot(){
		ArrayList<SC_TOP_RANKER_NOTI> rankers = createSnapshot();
		if(rankers != null){
			rankers.addAll(_dummys);
			Collections.sort(rankers);
		}
		return rankers;
	}
	
	public ArrayList<SC_TOP_RANKER_NOTI> createSnapshot(){
		return _ranks.size() <= 0 ? null : new ArrayList<SC_TOP_RANKER_NOTI>(_ranks.values());		
	}
	
	public boolean isRankPoly(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		if(noti == null)
			return false;
		
		return noti.get_total_ranker().get_rating() > 0 || noti.get_class_ranker().get_rank() <= 3;
	}
	
	public int getRankLevel(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		return noti == null ? 0 :noti.get_total_ranker().get_rating();
	}
	
	public SC_TOP_RANKER_NOTI getRankNoti(L1PcInstance pc){
		SC_TOP_RANKER_NOTI noti = get(pc.getId());
		return noti;
	}
	
	public void dispose(){
		if(_ranks != null){
			_ranks.clear();
			_ranks = null;
		}
	}
}
