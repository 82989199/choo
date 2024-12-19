package l1j.server.MJBotSystem.AI;

import java.util.ArrayDeque;

import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.MJAstar.Astar;
import l1j.server.MJBotSystem.MJAstar.Node;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ScarecrowInstance;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Bot scarecrow attack AI.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotScarecrowAI extends MJBotMovableAI{
	public MJBotScarecrowAI(){
		super();
		_ast			= new Astar();
		_deQ			= new ArrayDeque<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
	}
	
	@Override
	public void setting(long time) {
		_target = null;
		_targetQ.clear();
		_deQ.clear();
		_ast.release();
		if(!(MJBotLoadManager.MBO_WANDER_MAT_LEFT <= _body.getX() && MJBotLoadManager.MBO_WANDER_MAT_TOP <= _body.getY() &&
				MJBotLoadManager.MBO_WANDER_MAT_RIGHT >= _body.getX() && MJBotLoadManager.MBO_WANDER_MAT_BOTTOM >= _body.getY())){
			MJBotLocation loc = MJBotUtil.createRandomLocation(
					MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
					MJBotLoadManager.MBO_WANDER_MAT_TOP, 
					MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
					MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
					MJBotLoadManager.MBO_WANDER_MAT_MAPID
				);
			teleport(loc.x, loc.y, (short)loc.map);
		}
		setStatus(MJBotStatus.WALK);
	}

	@Override
	public void walk(long time) {
		if(_brn.toRand(100) < 5)
			doping();
		
		if(!_deQ.isEmpty()){
			moveAstar();
		}else if(_target != null){
			setStatus(MJBotStatus.ATTACK);
		}else
			searchTarget();
	}
	
	@Override
	public void searchWalk(long time) {
		if(!_deQ.isEmpty()){
			moveAstar();
		}else if(_target != null){
			setStatus(MJBotStatus.ATTACK);
		}else
			searchTarget();
	}

	@Override
	public void attack(long time) {
		try{
			_lastStatus = MJBotStatus.ATTACK;
			_target = selectObject();
			if(_target == null){
				setStatus(MJBotStatus.WALK);
				return;
			}
			
			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());
			if(dir > _body.getAttackRang()){
				_target = null;
				setStatus(MJBotStatus.WALK);
				return;
			}
			if(MJCommons.isPassableLine(_body.getX(), _body.getY(), _target.getX(), _target.getY(), _body.getMapId()))
				attack(_target, 0, 0, _body.isElf(), 1, 0);
		}catch(Exception e){}
	}

	@Override
	public void pickUp() {
		setStatus(MJBotStatus.SETTING);
	}

	@Override
	public boolean buff(long time) {
		return false;
	}

	@Override
	public boolean searchTarget() {
		_targetQ.clear();
		_target	= null;
		
		ArrayDeque<L1Object> objQ	= MJCommons.createKnownQ(_body);
		while(!objQ.isEmpty()){
			L1Object obj = objQ.poll();
			if(obj == null || !(obj instanceof L1ScarecrowInstance))
				continue;
			
			L1Character c = (L1Character)obj;
			
			MJBotLocation loc = new MJBotLocation();
			loc.x 	= obj.getX();
			loc.y 	= obj.getY();
			loc.map = obj.getMapId();
			findPath(loc);
			if(!_deQ.isEmpty()){
				Node tmp = _deQ.peekLast();
				int dir = MJCommons.getDistance(c.getX(), c.getY(), tmp.x, tmp.y);
				if(dir <= _body.getAttackRang()){
					_target	= c;
					objQ.clear();
					return true;
				}
				_deQ.clear();
			}
			_ast.release();
		}
		
		MJBotLocation loc = MJBotUtil.createRandomLocation(
				MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
				MJBotLoadManager.MBO_WANDER_MAT_TOP, 
				MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
				MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
				MJBotLoadManager.MBO_WANDER_MAT_MAPID
			);
		findPath(loc);
		setStatus(MJBotStatus.WALK);
		return false;
	}

	@Override
	public void healingPotion(boolean isDir, long time) {
	}

	@Override
	public void settingDefense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void walkDefense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchWalkDefense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attackDefense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickupDefense() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean searchTargetDefense() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void settingOffense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void walkOffense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchWalkOffense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attackOffense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickupOffense() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean searchTargetOffense() {
		// TODO Auto-generated method stub
		return false;
	}
}
