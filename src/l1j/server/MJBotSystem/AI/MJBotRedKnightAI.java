package l1j.server.MJBotSystem.AI;

import java.util.ArrayDeque;

import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.Business.MJAIScheduler;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.MJAstar.Astar;
import l1j.server.MJBotSystem.MJAstar.Node;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.utils.MJCommons;

public class MJBotRedKnightAI extends MJBotMovableAI{
	protected int	_pickupPending;
	private boolean _isDefender;
	protected long	_lastSearchTime;
	public boolean isDefender(){
		return _isDefender;
	}
	
	public void setDefender(boolean b){
		_isDefender = b;
	}
	
	@Override
	public void dispose(){
		_pickupPending 	= 0;
		_lastSearchTime	= 0;
		super.dispose();
	}
	
	public boolean isMovePauseOffense(){
		int p = 5;
		if(_brn.getClassType() == 2)
			p = 3;
		if(_movePending++ >= p){
			_movePending = 0;
			return true;
		}
		return false;
	}
	
	public boolean isPickupPause(){
		if(_pickupPending++ >= _brn.getPickupCount()){
			_pickupPending = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public void setWarCastle(int castleId){
		if(getWarCastle() == castleId)
			return;
		
		super.setWarCastle(castleId);
		setStatus(MJBotStatus.SETTING);
		_lastSearchTime	= 0;
		if(castleId == -1){
			if(_ast != null){
				_ast.release();
				_ast = null;
			}
			
			if(_deQ != null){
				_deQ.clear();
				_deQ = null;
			}
		}else{
			_ast			= new Astar();
			_deQ			= new ArrayDeque<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
		}
	}
	
	@Override
	public void setting(long time) {
	}

	@Override
	public boolean isRunDead(){
		if(_deadPending++ >= 3){
			_deadPending = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public void death(){

		_body.getMap().setPassable(_body.getLocation(), true);
		Broadcaster.broadcastPacket(_body, new S_RemoveObject(_body));
		_body.removeAllKnownObjects();
		MJAIScheduler.getInstance().removeSchedule(this);
	}
	
	@Override
	public void settingDefense(long time) {
		_target = null;
		_targetQ.clear();
		setStatus(MJBotStatus.ATTACK);
	}

	@Override
	public void settingOffense(long time) {
		setStatus(MJBotStatus.WALK);
	}

	@Override
	public void walk(long time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void walkDefense(long time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void walkOffense(long time) {
		if(!MJBotUtil.isInCastle(_body, getWarCastle()) && !MJBotUtil.isInCastleStartup(_body, getWarCastle())){
			MJBotLocation loc	 = MJBotUtil.createCastleLocation(getWarCastle());
			teleport(loc.x, loc.y, (short)loc.map);
			return;
		}
		
		int lvl = MJBotUtil.getCastleWayLevel(_body, getWarCastle());
		if(_loc == null || lvl != _wayLevel || MJCommons.getDistance(_body.getX(), _body.getY(), _loc.x, _loc.y) <= 1){
			_wayLevel 	= lvl;
			if(_brn.getClassType() == 3 || _brn.getClassType() == 2){
				_loc 	= MJBotUtil.getCastleWayCenterPoint(getWarCastle(), _wayLevel);
			}else
				_loc	= MJBotUtil.getCastleWayPoint(getWarCastle(), _wayLevel);
			_target	= null;
			_targetQ.clear();
			findPath(1, _loc);
		}
		
		if(_deQ.isEmpty()){
			_ast.release();
			findPath(1, _loc);
		}
		moveAstar();
		if(_astFailCount > 10){
			_astFailCount = 0;
			_deQ.clear();
			_ast.release();
			return;
		}
		
		if(_wayLevel == 0 && _brn.toRand(100) < 70)
			return;
		
		if(isMovePauseOffense()){
			setStatus(MJBotStatus.ATTACK);
		}
	}

	@Override
	public void searchWalk(long time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void searchWalkDefense(long time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void searchWalkOffense(long time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void attack(long time) {
		
	}

	@Override
	public void attackDefense(long time) {
		try{
			if(isEmptyTarget()){
				searchTargetDefense();
				return;
			}
			
			_target = selectObject();
			if(_target == null){
				_targetQ.clear();
				return;
			}
			
			if(!isAttack(_target, false)){
				_target = null;
				return;
			}
			
			if(!MJCommons.isPassableLine(_body.getX(), _body.getY(), _target.getX(), _target.getY(), _body.getMapId())){
				_target = null;
				return;
			}
			
			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());
			if(_brn.getClassType() == 3){
				int r = _brn.toRand(100);
				if(r < 33)
					magic(_target, L1SkillId.ERUPTION, 400, time);
				else if(r < 66)
					magic(_target, L1SkillId.CONE_OF_COLD, 400,  time);
				else
					magic(_target, L1SkillId.CALL_LIGHTNING, 400,  time);
			}else{
				if(dir > _body.getAttackRang()){
					_target = null;
					return;
				}
				attack(_target, 0, 0, _body.isElf(), 1, 0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void attackOffense(long time) {
		try{	
			_lastStatus = MJBotStatus.ATTACK;
			if(isEmptyTarget()){
				searchTargetOffense();
				_lastSearchTime	= time;
				if(isEmptyTarget()){
					setStatus(MJBotStatus.WALK);
					_loc = null;
					return;
				}
			}
			
			_target = selectObjectOffense();
			if(_target == null){
				setStatus(MJBotStatus.WALK);
				_targetQ.clear();
				_loc = null;
				return;
			}
			
			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());		
			if(_brn.getClassType() != 2 && _brn.getClassType() != 3 && dir > 3){
				int h = MJCommons.calcheading(_body.getX(), _body.getY(), _loc.x, _loc.y);
				if(MJCommons.isReverseHeading(_body.getX(), _body.getY(), _target.getX(), _target.getY(), h)){					
					_loc	= null;
					_target = null;
					_targetQ.clear();
					setStatus(MJBotStatus.WALK);
					return;
				}
			}
			
			if(_brn.getClassType() == 3){
				if(_magicDelay > time || immune(_body, time))
					return;
				
				if(dir > 10 || !isAttack(_target, false)){
					int h = 0;
					if(isPickupPause() || (_body.getCurrentHp() != _body.getMaxHp() && _brn.toBoolean()))
						h = MJCommons.getReverseHeading(h);
					else
						h = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
					if(!moveWide(h)){
						_target = null;
						if(time - _lastSearchTime > 5000){
							_targetQ.clear();
							_loc = null;
							setStatus(MJBotStatus.WALK);
						}
					}
					_lastStatus = MJBotStatus.WALK;
				}else{
					if(!immune(_target, time)){
						if(_targetQ.size() > 2 && _brn.toRand(100) < 10)
							healAll(time);
						else{
							heal(_target, time);
						}
						if(_target.getCurrentHp() >= _target.getMaxHp())
							_target = null;
					}else
						_target = null;
				}
			}else{
				int rang = _body.getAttackRang() > 10 ? 10 : _body.getAttackRang();
				if(dir > rang || !isAttack(_target, false)){
					if(isPickupPause()){
						_loc	= null;
						_target = null;
						_targetQ.clear();
						setStatus(MJBotStatus.WALK);
						return;
					}
					
					int h = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
					if(!moveWide(h)){
						if(time - _lastSearchTime > 5000){
							if(_brn.toRand(100) < 30){
								h = MJCommons.getReverseHeading(h);
								if(moveWide(h)){
									_lastStatus = MJBotStatus.WALK;
									_target = null;
									return;
								}
							}
							_targetQ.clear();
							_loc	= null;
							setStatus(MJBotStatus.WALK);
						}
						_target = null;
					}
					_lastStatus = MJBotStatus.WALK;
					return;
				}else{
					_pickupPending = 0;
					
					//if(!magic(_target, dir, time))
						attack(_target, 0, 0, _body.isElf(), 1, 0);
					if(_target.isDead() || _target.getMapId() != _body.getMapId() || _target.isInvisble()){
						_target = null;
						if(time - _lastSearchTime > 5000)
							_targetQ.clear();
						_loc	= null;
						setStatus(MJBotStatus.WALK);
					}
				}
			}
		}catch(Exception e){}
	}

	@Override
	public void pickUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickupDefense() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickupOffense() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean searchTarget() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean searchTargetDefense() {
		_targetQ.clear();
		_target = null;
		_body.updateObject();
		ArrayDeque<L1Object> 	objQ	= MJCommons.createKnownQ(_body);
		L1Object				obj		= null;
		while(!objQ.isEmpty()){
			obj = objQ.poll();
			if(obj == null)
				continue;
			
			if(!(obj instanceof L1PcInstance))
				continue;
			
			L1PcInstance c = (L1PcInstance)obj;
			if(c.isDead())
				continue;
			
			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), c.getX(), c.getY());
			int ran = _body.getAttackRang();
			if(_brn.getClassType() == 3)
				ran = 10;
			if(dir > ran)
				continue;
			
			if(c.getClanid() == _body.getClanid())
				continue;
			
			_targetQ.offer(c);
		}		
		return true;
	}
	
	@Override
	public boolean searchTargetOffense() {
		_targetQ.clear();
		_target = null;
		_body.updateObject();
		
		ArrayDeque<L1Object> 	objQ 	= MJCommons.createKnownQ(_body);
		L1Object				obj		= null;
		int						cid		= getWarCastle();
		
		if(_brn.getClassType() == 3){
			while(!objQ.isEmpty()){
				obj = objQ.poll();
				if(obj == null)
					continue;
				
				if(!(obj instanceof L1PcInstance) || !MJBotUtil.isInCastle(obj, cid))
					continue;
				
				L1PcInstance pc = (L1PcInstance)obj;
				if(pc.getCurrentHp() >= pc.getMaxHp())
					continue;
				
				int dir = MJCommons.getDistance(_body.getX(), _body.getY(), obj.getX(), obj.getY());
				if(dir > 10)
					continue;
				
				if(!_body.glanceCheck(obj.getX(), obj.getY()))
					continue;
				
				if(pc.isDead() || pc.getClanid() != _body.getClanid() || pc.getType() == 3)
					continue;
				
				_targetQ.offer(pc);
			}
		}else{
			_targetQ.addAll(MJBotUtil.findCastleDoors(cid));
			int h = MJCommons.calcheading(_body.getX(), _body.getY(), _loc.x, _loc.y);
			while(!objQ.isEmpty()){
				obj = objQ.poll();
				if(obj == null)
					continue;
				
				if(!MJBotUtil.isInCastle(obj, cid))
					continue;
				
				if(obj.getMap().isSafetyZone(obj.getLocation()))
					continue;
				
				if(MJCommons.isReverseHeading(_body.getX(), _body.getY(), obj.getX(), obj.getY(), h))
					continue;
				
				if(obj instanceof L1PcInstance){
					L1PcInstance pc = (L1PcInstance)obj;
					if(pc.isDead() || _body.getClanid() == pc.getClanid() || pc.getRedKnightClanId() != 0 || pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
						continue;	
					_targetQ.offer(pc);
				}else if(obj instanceof L1CrownInstance || obj instanceof L1TowerInstance || obj instanceof L1CastleGuardInstance || obj instanceof L1MonsterInstance){
					L1Character c = (L1Character)obj;
					if(c.isDead())
						continue;
					_targetQ.offer(c);
				}
			}
		}
		return true;
	}
	
	protected L1Character selectObjectOffense(){
		try{
			if(_target != null && _target.getMapId() == _body.getMapId() && !_target.isDead() && _body.glanceCheck(_target.getX(), _target.getY()) && MJBotUtil.isInCastle(_target, getWarCastle()))
				return _target;
			
			_targetQ.comparator();
			while(!_targetQ.isEmpty()){
				L1Character tmp = _targetQ.poll();
				if(tmp.isInvisble() || tmp.isDead() || tmp.getMapId() != _body.getMapId())
					continue;
				
				if(!_body.glanceCheck(tmp.getX(), tmp.getY()) || !MJBotUtil.isInCastle(tmp, getWarCastle()))
					continue;
				
				return tmp;
			}
		}catch(Exception e){}
		return null;
	}
	
	protected boolean isAttack(L1Character c, boolean isWalk){
		if(c == null || c.getMapId() != _body.getMapId() || c.hasSkillEffect(L1SkillId.EARTH_BIND) || c.hasSkillEffect(L1SkillId.ICE_LANCE))
			return false;
		if (c.getMap().isSafetyZone(c.getLocation()) || c.isDead() || c.isInvisble())
			return false;
		return true;
	}
}
