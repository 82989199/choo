package l1j.server.server.model.trap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.TrapTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TrapInstance;
import l1j.server.server.types.Point;
import l1j.server.server.utils.SQLUtil;

// 여기 타이머를 놔두는 이유는;; 자주 콜도 안되는 애인데다가, TimerTask쓰는게 편해서...다.-_-;;; by magik
public class L1WorldTraps {
	private static Logger _log = Logger.getLogger(L1WorldTraps.class.getName());

	private List<L1TrapInstance> _allTraps = new ArrayList<L1TrapInstance>(5120);
	private List<L1TrapInstance> _allBases = new ArrayList<L1TrapInstance>(260);

	private Timer _timer = new Timer();

	private static L1WorldTraps _instance;

	private L1WorldTraps() {
		initialize();
	}

	public static L1WorldTraps getInstance() {
		if (_instance == null) {
			_instance = new L1WorldTraps();
		}
		return _instance;
	}

	private TrapsComparator _comp = new TrapsComparator();
	private void initialize() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			pstm = con.prepareStatement("SELECT * FROM spawnlist_trap");

			rs = pstm.executeQuery();
			L1TrapInstance trap = null;
			L1TrapInstance base = null;
			L1Trap trapTemp = null;
			L1Location loc = null;
			Point rndPt = null;
			while (rs.next()) {
				int trapId = rs.getInt("trapId");
				trapTemp = TrapTable.getInstance().getTemplate(trapId);
				loc = new L1Location();
				loc.setMap(rs.getInt("mapId"));
				loc.setX(rs.getInt("locX"));
				loc.setY(rs.getInt("locY"));
				rndPt = new Point();
				rndPt.setX(rs.getInt("locRndX"));
				rndPt.setY(rs.getInt("locRndY"));
				int count = rs.getInt("count");
				int span = rs.getInt("span");

				for (int i = 0; i < count; i++) {
					trap = new L1TrapInstance(IdFactory.getInstance().nextId(), trapTemp, loc, rndPt, span);
					L1World.getInstance().addVisibleObject(trap);
					_allTraps.add(trap);
				}
				base = new L1TrapInstance(IdFactory.getInstance().nextId(), loc);
				L1World.getInstance().addVisibleObject(base);
				_allBases.add(base);
			}
			_allTraps.sort(_comp);
			_allBases.sort(_comp);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void reloadTraps() {
		TrapTable.reload();
		L1WorldTraps oldInstance = _instance;
		_instance = new L1WorldTraps();
		oldInstance.resetTimer();
		removeTraps(oldInstance._allTraps);
		removeTraps(oldInstance._allBases);
	}

	private static void removeTraps(List<L1TrapInstance> traps) {
		for (L1TrapInstance trap : traps) {
			trap.disableTrap();
			L1World.getInstance().removeVisibleObject(trap);
		}
	}

	private void resetTimer() {
		synchronized (this) {
			_timer.cancel();
			_timer = new Timer();
		}
	}

	private void disableTrap(L1TrapInstance trap) {
		trap.disableTrap();
		synchronized (this) {
			_timer.schedule(new TrapSpawnTimer(trap), trap.getSpan());
		}
	}

	public void resetAllTraps() {
		for (L1TrapInstance trap : _allTraps) {
			trap.resetLocation();
			trap.enableTrap();
		}
	}

	public void onPlayerMoved(L1PcInstance player) {
		int idx = Collections.binarySearch(_allTraps, player, _comp);
		if(idx < 0)
			return;
		
		L1TrapInstance t = _allTraps.get(idx);
		if(t.isEnable()){
			t.onTrod(player);
			disableTrap(t);
		}
		
		/*L1Location loc = player.getLocation();
		for (L1TrapInstance trap : _allTraps) {
			if (trap.isEnable() && loc.equals(trap.getLocation())) {
				trap.onTrod(player);
				disableTrap(trap);
			}
		}*/
	}

	public void onDetection(L1PcInstance caster) {
		int idx = Collections.binarySearch(_allTraps, caster, _comp);
		if(idx < 0)
			return;
		
		L1TrapInstance 	t 	= null;
		L1Location 		loc = caster.getLocation();
		for(int i=idx; i>=0; i--){
			t = _allTraps.get(i);
			if(t.isEnable() && loc.isInScreen(t.getLocation())){
				t.onDetection(caster);
				disableTrap(t);
			}
		}
		for(int i=idx+1; i<_allTraps.size(); i++){
			t = _allTraps.get(i);
			if(t.isEnable() && loc.isInScreen(t.getLocation())){
				t.onDetection(caster);
				disableTrap(t);
			}
		}
	}

	class TrapsComparator implements Comparator<L1Object>{
		@Override
		public int compare(L1Object o1, L1Object o2) {
			if(o1.getMapId() > o2.getMapId())
				return 1;
			else if(o1.getMapId() < o2.getMapId())
				return -1;
			else if(o1.getX() > o2.getX())
				return 1;
			else if(o1.getX() < o2.getX())
				return -1;
			else if(o1.getY() > o2.getY())
				return 1;
			else if(o1.getY() < o2.getY())
				return -1;
			return 0;
		}
		
	}
	
	private class TrapSpawnTimer extends TimerTask {
		private final L1TrapInstance _targetTrap;

		public TrapSpawnTimer(L1TrapInstance trap) {
			_targetTrap = trap;
		}

		@Override
		public void run() {
			_targetTrap.resetLocation();
			_targetTrap.enableTrap();
		}
	}
	
	// 오림 인던 관련
	public synchronized void addTrap(L1TrapInstance trap) {
		_allTraps.add(trap);
	}

	 public synchronized void removeTrap(L1TrapInstance trap) {
		 for (Iterator<L1TrapInstance> i =_allTraps.listIterator(); i.hasNext();) {
			  L1TrapInstance traps = i.next();
			  if(traps.getId() == trap.getId()){
					  i.remove();
				  break;
			  }
		 }
		List<L1TrapInstance> traps = new ArrayList<L1TrapInstance>();
		traps.add(trap);
		removeTraps(traps);
	}
}
