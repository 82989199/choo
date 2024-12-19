package l1j.server.MJBotSystem.AI;

import java.util.ArrayDeque;
import java.util.ArrayList;

import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.MJAstar.Astar;
import l1j.server.MJBotSystem.MJAstar.Node;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

public class MJBotWanderAI extends MJBotMovableAI{
	public MJBotWanderAI(){
		super();
		_ast			= new Astar();
		_deQ			= new ArrayDeque<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}

	@Override
	public boolean buff(long time){
		boolean isBuff = super.buff(time);
		if(isBuff){
			MJBotLocation loc = MJBotUtil.createRandomLocation(
					MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
					MJBotLoadManager.MBO_WANDER_MAT_TOP, 
					MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
					MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
					MJBotLoadManager.MBO_WANDER_MAT_MAPID
				);
			//if(_brn.toRand(100) <= 30)
			teleport(loc.x, loc.y, (short)loc.map);
		}
		return isBuff;
	}
	
	@Override
	public void setting(long time) {
		_target = null;
		_targetQ.clear();
		if(_body.getCurrentHp() != _body.getMaxHp()){
			healingPotion(true, time);
			return;
		}
		_deQ.clear();
		_ast.release();
		setStatus(MJBotStatus.WALK);
	}

	@Override
	public void walk(long time) {
		if(!_deQ.isEmpty()){
			moveAstar();
		}else if(_brn.toRand(100) <= 20){
			setStatus(MJBotStatus.SETTING);
		}else{
			if(!MJBotUtil.isInTown(_body)){
				setStatus(MJBotStatus.SETTING);
				
				MJBotLocation loc = MJBotUtil.createRandomLocation(
						MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
						MJBotLoadManager.MBO_WANDER_MAT_TOP, 
						MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
						MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
						MJBotLoadManager.MBO_WANDER_MAT_MAPID
					);
				//if(_brn.toRand(100) <= 30)
				teleport(loc.x, loc.y, (short)loc.map);
				return;
			}
			
			if(_target != null)
				setStatus(MJBotStatus.ATTACK);
			else
				searchTarget();
		}
	}

	@Override
	public void searchWalk(long time) {
		if(!_deQ.isEmpty())
			moveAstar();
		else if(_brn.toRand(100) <= 20)
			setStatus(MJBotStatus.SETTING);
		else if(_target != null)
			setStatus(MJBotStatus.ATTACK);
		else
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
			
			if(isAttack(_target)){
				if(super.buff(time))
					return;
				
				if(magic(_target, dir, time))
					return;
				
				attack(_target, 0, 0, _body.isElf(), 1, 0);
			}else{
				_target = null;
				if(_brn.toRand(100) > 10)
					setStatus(MJBotStatus.WALK);
				else
					setStatus(MJBotStatus.SETTING);
			}
		}catch(Exception e){}
	}

	@Override
	public void pickUp() {
		
	}

	@Override
	public boolean searchTarget() {
		_targetQ.clear();
		_target = null;
		_deQ.clear();
		_ast.release();
		
		if(_brn.toRand(100) <= 20){
			setStatus(MJBotStatus.SETTING);
			return true;
		}
		
		ArrayList<L1Object> list	= null;
		if(_brn.toRand(100) > _brn.getHormon())
			list = MJCommons.createKnownList(_body);
		else 
			list = MJCommons.createKnownPlayers(_body);
		
		MJBotLocation loc			= null;
		if(list == null || list.size() <= 0 || _brn.getHormon() < 10){
			loc = MJBotUtil.createRandomLocation(
					MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
					MJBotLoadManager.MBO_WANDER_MAT_TOP, 
					MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
					MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
					MJBotLoadManager.MBO_WANDER_MAT_MAPID
				);
		}else{
			L1Object obj	= list.get(_brn.toRand(list.size()));
			if(obj == null || obj.getId() == _body.getId()){
				loc = MJBotUtil.createRandomLocation(
						MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
						MJBotLoadManager.MBO_WANDER_MAT_TOP, 
						MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
						MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
						MJBotLoadManager.MBO_WANDER_MAT_MAPID
					);
			}else{
				if(obj instanceof L1PcInstance && !((L1PcInstance) obj).isDead() && obj.getMap().isCombatZone(obj.getLocation()))
					_target = (L1Character)obj;
				
				loc = new MJBotLocation();
				loc.x = obj.getX();
				loc.y = obj.getY();
				loc.map = obj.getMapId();
			}
		}

		findPath(1, loc);
		return true;
	}
	
	protected boolean isAttack(L1Character c){
		if(c == null)
			return false;
		
		if(!c.getMap().isCombatZone(c.getLocation()) || !_body.getMap().isCombatZone(_body.getLocation()))
			return false;
		
		if(c.isDead() || c.isInvisble() || _body.getMapId() != c.getMapId())
			return false;
		
		if(!_body.glanceCheck(c.getX(), c.getY()))
			return false;
		
		return MJCommons.isPassableLine(_body.getX(), _body.getY(), c.getX(), c.getY(), c.getMapId());
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
