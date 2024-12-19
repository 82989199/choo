package l1j.server.MJBotSystem.AI;

import java.util.ArrayDeque;
import java.util.Iterator;

import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.Business.MJAIScheduler;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.MJAstar.Astar;
import l1j.server.MJBotSystem.MJAstar.Node;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJRaidSystem.Loader.MJRaidLoadManager;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_OnlyEffect;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJCommons;

public class MJBotProtectorAI extends MJBotMovableAI{
	protected int	_pickupPending;
	protected long	_lastSearchTime;
	protected int	_protectorId;
	
	
	public MJBotProtectorAI(int pid){
		super();
		_protectorId	= pid;
		_ast			= new Astar();
		_deQ			= new ArrayDeque<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
	}
	
	@Override
	public void dispose(){
		_protectorId	= 0;
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
	public void setting(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void settingDefense(long time) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attackDefense(long time) {
		// TODO Auto-generated method stub
		
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
			
			_lastAction = 0;
			if(magicAttack(time, dir))
				return;
			
			if(_brn.getClassType() == 3){
				int rang = 8;
				if(dir > rang || !isAttack(_target, false)){
					if(isPickupPause()){
						_loc	= null;
						_target = null;
						_targetQ.clear();
						setStatus(MJBotStatus.WALK);
						_lastStatus = MJBotStatus.WALK;
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
					int r = _brn.toRand(100);
					if(r < 33)		new L1SkillUse().handleCommands(_body, L1SkillId.ERUPTION, _target.getId(), _target.getX(), _target.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					else if(r < 66)	new L1SkillUse().handleCommands(_body, L1SkillId.CONE_OF_COLD, _target.getId(), _target.getX(), _target.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					else			new L1SkillUse().handleCommands(_body, L1SkillId.CALL_LIGHTNING, _target.getId(), _target.getX(), _target.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					if(_target.isDead() || _target.getMapId() != _body.getMapId() || _target.isInvisble()){
						_target = null;
						if(time - _lastSearchTime > 5000)
							_targetQ.clear();
						_loc	= null;
						setStatus(MJBotStatus.WALK);
					}
				}
			}else{
				int rang = _body.getAttackRang() > 10 ? 10 : _body.getAttackRang();
				if(dir > rang || !isAttack(_target, false)){
					if(isPickupPause()){
						_loc	= null;
						_target = null;
						_targetQ.clear();
						setStatus(MJBotStatus.WALK);
						_lastStatus = MJBotStatus.WALK;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean searchTargetOffense() {
		_targetQ.clear();
		_target = null;
		_body.updateObject();
		
		ArrayDeque<L1Object> 	objQ 	= MJCommons.createKnownQ(_body);
		L1Object				obj		= null;
		int						cid		= getWarCastle();
		
		_targetQ.addAll(MJBotUtil.findCastleDoors(cid));
		//System.out.println(_targetQ.addAll(MJBotUtil.findCastleDoors(cid)));
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
				if(pc.isDead() || pc.getRedKnightClanId() != 0 || pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
					continue;	
				_targetQ.offer(pc);
			}else if(obj instanceof L1CrownInstance || obj instanceof L1TowerInstance || obj instanceof L1CastleGuardInstance || obj instanceof L1MonsterInstance){
				L1Character c = (L1Character)obj;
				if(c.isDead())
					continue;
				_targetQ.offer(c);
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

	public void healingPotion(boolean isDir, long time){
		if(_body.isDead())
			return;
		
		_body.setCurrentMp(_body.getMaxMp());
		if(_body.hasSkillEffect(71))
			return;
		
		if(!isDir && !isPotionUse(time))
			return;
		
		if(_body.getCurrentHp() < _body.getMaxHp()){
			int heal = (int)((_body.getAbility().getTotalCon() * 3) * 1.5);
			if (_body.hasSkillEffect(L1SkillId.POLLUTE_WATER))
				heal /= 2;
			_body.setCurrentHp(_body.getCurrentHp() + heal);
		}		
	}
	
	@Override
	public boolean buff(long time){
		switch(_brn.getClassType()){
		case 0:
			break;
		case 1:
			if(_body.hasSkillEffect(L1SkillId.REDUCTION_ARMOR)){
				buff(L1SkillId.REDUCTION_ARMOR);
				return true;
			}
			break;
		case 2:
			if(_body.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)){
				buff(L1SkillId.IMMUNE_TO_HARM);
				return true;
			}
			break;
		case 3:
			if(_body.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)){
				buff(L1SkillId.IMMUNE_TO_HARM);
				return true;
			}
			break;
		case 4:
			if(_body.hasSkillEffect(L1SkillId.UNCANNY_DODGE)){
				buff(L1SkillId.UNCANNY_DODGE);
				return true;
			}
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		}
		
		return false;
	}
	
	protected boolean magicBuff(long time, int dir){
		return false;
	}
	
	protected boolean magicAttack(long time, int dir){
		if(_magicDelay + MJBotLoadManager.MBO_SKILL_REVISION_DELAY > time)
			return false;
		
		switch(_protectorId){
		case 0:	// 데포로쥬
			if(_target instanceof L1PcInstance){
				if(dir <= 10 && _target.get트루타켓() == 0 && _brn.toRand(100) < 50){
					GeneralThreadPool.getInstance().execute(new TrueTarget(_target));
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 9));
					_lastAction = 9;
					_magicDelay = time + 1000;
					return true;
				}else if(dir <= 5 && _brn.toRand(100) < 50){
					toPlayerAreaDamage(_body, 5, 200, 600, true, new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 19)});
					_lastAction = 19;
					_magicDelay = time + 3000;
					return true;
				}else if(dir <= 15 && _brn.toRand(100) < 50){
					toPlayerAreaDamage(_body, 15, 500, 700, true, new ServerBasePacket[]{new S_SkillSound(_body.getId(), 12751), new S_DoActionGFX(_body.getId(), 19)});
					_lastAction = 19;
					_magicDelay = time + 3000;
					return true;
				}
			}
			break;
		case 1:	// 이실로테
			if(_target instanceof L1PcInstance){
				L1PcInstance p = (L1PcInstance)_target;
				if(dir <= 10 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 9));
					GeneralThreadPool.getInstance().execute(new DarknessMeteor(p));
					_lastAction = 9;
					_magicDelay = time + 1000;
					return true;
				}else if(dir <= 5 && _brn.toRand(100) < 50){
					toPlayerAreaDamage(_body, 5, 200, 600, true, new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 19)});
					_lastAction = 19;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 10 && _brn.toRand(100) < 50){
					toPlayerAreaDamage(_body, 20, 100, 500, true, new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 9), new S_SkillSound(_body.getId(), 12317)});
					_lastAction = 9;
					_magicDelay = time + 500;
					return true;
				}
			}
			break;
		case 2:	// 아툰
			if(_target instanceof L1PcInstance){
				if(dir <= 5 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 19));
					toPlayerAreaStun(_body, 5, 200, 500);
					_lastAction = 19;
					_magicDelay = time + 3000;
					return true;
				}else if(dir <= 3 && _brn.toRand(100) < 50){
					toPlayerAreaDamage(_body, 3, 400, 600, true, new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 19)});
					_lastAction = 19;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 2 && _target.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM) && _brn.toRand(100) <= 50){
					_target.broadcastPacket(new ServerBasePacket[]{new S_OnlyEffect(_target.getId(), 15961), new S_DoActionGFX(_body.getId(), ActionCodes.ACTION_AltAttack), new S_DoActionGFX(_target.getId(), ActionCodes.ACTION_Damage)});
					_target.removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
					_lastAction = ActionCodes.ACTION_AltAttack;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 2 && _target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER) && _brn.toRand(100) <= 50){
					_target.broadcastPacket(new ServerBasePacket[]{new S_OnlyEffect(_target.getId(), 15835), new S_DoActionGFX(_body.getId(), ActionCodes.ACTION_AltAttack), new S_DoActionGFX(_target.getId(), ActionCodes.ACTION_Damage)});
					_target.removeSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
					_lastAction = ActionCodes.ACTION_AltAttack;
					_magicDelay = time + 500;
					return true;
				}
			}
			break;
		case 3:	// 질리언(검)
			if(_target instanceof L1PcInstance){
				L1PcInstance p = (L1PcInstance)_target;
				if(dir <= 15 && _brn.toRand(100) < 50){
					GeneralThreadPool.getInstance().execute(new BloodSucking());
					_lastAction = 9;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 2 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 9));
					p.receiveDamage(_body, MJCommons.calcDamage(_body, p, 300, 600, false));
					p.broadcastPacket(new S_DoActionGFX(p.getId(), ActionCodes.ACTION_Damage));
					_lastAction = 9;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 3 && _brn.toRand(100) < 50){
					toPlayerAreaDamage(_body, 3, 200, 600, true, new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 19)});
					_lastAction = 9;
					_magicDelay = time + 500;
					return true;
				}
			}
			break;
		case 4:	// 질리언(활)
			if(_target instanceof L1PcInstance){
				L1PcInstance p = (L1PcInstance)_target;
				if(dir <= 10 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 18));
					toPlayerAreaDamage(p, 3, 500, 800, false, new ServerBasePacket[]{new S_SkillSound(p.getId(), 12256)});
					_lastAction = 18;
					_magicDelay = time + 1000;
					return true;
				}else if(dir <= 10 && _brn.toRand(100) < 10){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 18));
					p.broadcastPacket(new ServerBasePacket[]{new S_SkillSound(p.getId(), 13850), new S_DoActionGFX(p.getId(), ActionCodes.ACTION_Damage)});
					if(MJCommons.isCounterMagic(p))
						p.receiveDamage(_body, MJCommons.calcDamage(_body, p, 500, 700, true));
					_lastAction = 18;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 15 && _brn.toRand(100) < 30){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 19));					
					toPlayerAreaArrow(_body, 5, 400, 600);
					_lastAction = 19;
					_magicDelay = time + 500;
					return true;
				}
			}
			break;
		case 5:	// 조우
			if(_target instanceof L1PcInstance){
				L1PcInstance p = (L1PcInstance)_target;
				if(dir <= 15 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 19));
					GeneralThreadPool.getInstance().execute(new Arson(p));
					_lastAction = 19;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 10 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 19));
					p.broadcastPacket(new ServerBasePacket[]{new S_SkillSound(p.getId(), 12183), new S_DoActionGFX(p.getId(), ActionCodes.ACTION_Damage)});
					if(MJCommons.isCounterMagic(p))
						p.receiveDamage(_body, MJCommons.calcDamage(_body, p, 700, 800, true));
					_lastAction = 19;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 10 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new S_DoActionGFX(_body.getId(), 18));
					toPlayerAreaDamage(p, 5, 300, 600, true, new ServerBasePacket[]{new S_SkillSound(p.getId(), 13644)});
					_lastAction = 18;
					_magicDelay = time + 500;
					return true;
				}else if(dir <= 15 && dir >= 5 && _brn.toRand(100) < 50){
					int nx 	= p.getX() + _brn.toRand(3) + 1;
					int ny 	= p.getY() + _brn.toRand(3) + 1;
					int mid	= p.getMapId();
					L1Map map = L1WorldMap.getInstance().getMap(_target.getMapId());
					Iterator<L1PcInstance> it = L1World.getInstance().getVisiblePlayer(_body, 3).iterator();
					while(it.hasNext()){
						L1PcInstance t = it.next();
						if(t == null || t.isDead() || t.getRedKnightClanId() == 0)
							continue;
						
						if(map.isInMap(nx, ny) && map.isPassable(nx, ny))
							t.start_teleport(nx, ny, mid, t.getHeading(), 2236, true, true);
						else
							t.start_teleport(p.getX(), p.getY(), mid, t.getHeading(), 2236, true, true);
					}
					_lastAction = 18;
					_magicDelay = time + 500;
					return true;
				}
			}
			break;
		case 6: // 크리스터
			if(_target instanceof L1PcInstance){
				L1PcInstance p = (L1PcInstance)_target;
				if(dir <= 3 && _brn.toRand(100) < 10){
					toPlayerAreaDamage(p, 3, 300, 600, true, new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 19)});
					_lastAction = 19;
					_magicDelay = time + 3000;
					return true;
				}else if(dir <= 5 && _brn.toRand(100) < 50){
					_body.broadcastPacket(new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 18), new S_SkillSound(p.getId(), 13846)});
					p.receiveDamage(_body, MJCommons.calcDamage(_body, p, 300, 600, false));
					p.broadcastPacket(new S_DoActionGFX(p.getId(), ActionCodes.ACTION_Damage));
					_lastAction = 18;
					_magicDelay = time + 3000;
					return true;
				}else if(dir <= 15 && dir >= 5 && _brn.toRand(100) < 50){
					GeneralThreadPool.getInstance().execute(new Assassination(p));
					_lastAction = 18;
					_magicDelay = time + 3000;
					return true;
				}
			}
			break;
		}
		
		return false;
	}
	
	// cc is center character.
	private void toPlayerAreaDamage(L1Character cc, int range, int min, int max, boolean isMagic, ServerBasePacket[] effs){
		int x = cc.getX();
		int y = cc.getY();
		for(L1PcInstance pc : L1World.getInstance().getRecognizePlayer(cc)){
			if(pc == null)
				continue;
			
			if(effs != null){
				for(ServerBasePacket eff : effs)
					pc.sendPackets(eff, false);
			}
			if(pc.isDead() || MJCommons.isUnbeatable(pc) || pc.getRedKnightClanId() != 0 || pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
				continue;
			
			if(MJCommons.getDistance(x, y, pc.getX(), pc.getY()) > range)
				continue;
			
			if(!cc.glanceCheck(pc.getX(), pc.getY()))
				continue;
			
			if(isMagic){
				if(MJCommons.isCounterMagic(pc))
					continue;
			}
		
			int dmg = MJCommons.calcDamage(cc, pc, min, max, isMagic);
			pc.receiveDamage(cc, dmg);
			if(dmg > 0){
				ServerBasePacket pck = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
				pc.sendPackets(pck, false);
				pc.broadcastPacket(pck);
				pck.clear();
			}
		}
		for(ServerBasePacket eff : effs)
			eff.clear();
		effs = null;
	}
	
	private void toPlayerAreaArrow(L1Character cc, int range, int min, int max){
		int x = cc.getX();
		int y = cc.getY();
		for(L1PcInstance pc : L1World.getInstance().getRecognizePlayer(cc)){
			if(pc == null)
				continue;
			
			if(pc.isDead() || MJCommons.isUnbeatable(pc) || pc.getRedKnightClanId() != 0 || pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
				continue;
			
			if(MJCommons.getDistance(x, y, pc.getX(), pc.getY()) > range)
				continue;
			
			if(!cc.glanceCheck(pc.getX(), pc.getY()))
				continue;
			
			if(MJCommons.isCounterMagic(pc))
				continue;
		
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 13842));
			int dmg = MJCommons.calcDamage(cc, pc, min, max, true);
			pc.receiveDamage(cc, dmg);
			if(dmg > 0){
				ServerBasePacket pck = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
				pc.sendPackets(pck, false);
				pc.broadcastPacket(pck);
				pck.clear();
			}
		}
	}
	
	private void toPlayerAreaStun(L1Character cc, int range, int min, int max){
		int x = cc.getX();
		int y = cc.getY();
		for(L1PcInstance pc : L1World.getInstance().getRecognizePlayer(cc)){
			if(pc == null)
				continue;
			
			if(pc.isDead() || MJCommons.isUnbeatable(pc) || pc.getRedKnightClanId() != 0 || pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
				continue;
			
			if(MJCommons.getDistance(x, y, pc.getX(), pc.getY()) > range)
				continue;
			
			if(!cc.glanceCheck(pc.getX(), pc.getY()))
				continue;
		
			if(_brn.toRand(100) < 70){
				L1EffectSpawn.getInstance().spawnEffect(MJRaidLoadManager.MRS_BS_STUN_EFFECTID, 3000, pc.getX(), pc.getY(), pc.getMapId());
				pc.setSkillEffect(L1SkillId.SHOCK_STUN, 3000);
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
				int dmg = MJCommons.calcDamage(cc, pc, min, max, false);
				pc.receiveDamage(cc, dmg);
				if(dmg > 0){
					ServerBasePacket pck = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
					pc.sendPackets(pck, false);
					pc.broadcastPacket(pck);
					pck.clear();
				}
			}
		}
	}
	
	class DarknessMeteor implements Runnable{
		private L1PcInstance 	_t;
		private int				_step;
		DarknessMeteor(L1PcInstance c){
			_t = c;
		}
		@Override
		public void run() {
			if(_t == null || _t.isDead() || _t.getMap().isSafetyZone(_t.getX(), _t.getY()) || _t.getMapId() != _body.getMapId())
				return;
			
			if(_step == 0){
				_step++;
				_t.broadcastPacket(new S_SkillSound(_t.getId(), 11471));
				GeneralThreadPool.getInstance().schedule(this, 1000);
			}else{
				toPlayerAreaDamage(_t, 5, 700, 1000, true, new ServerBasePacket[]{new S_SkillSound(_t.getId(), 11473)});
			}
		}
	}
	
	class Arson implements Runnable{
		private L1PcInstance 	_t;
		private int				_step;
		Arson(L1PcInstance c){
			_t = c;
		}
		
		@Override
		public void run() {
			if(_t == null || _t.isDead() || _t.getMap().isSafetyZone(_t.getX(), _t.getY()) || _t.getMapId() != _body.getMapId())
				return;
			
			if(_step == 0){
				_step++;
				toPlayerAreaDamage(_t, 5, 300, 600, true, new ServerBasePacket[]{new S_SkillSound(_t.getId(), 11465)});
				GeneralThreadPool.getInstance().schedule(this, 800);
			}else if(_step == 1){
				_step++;
				toPlayerAreaDamage(_t, 5, 300, 600, true, null);
				GeneralThreadPool.getInstance().schedule(this, 800);	
			}else{
				toPlayerAreaDamage(_t, 5, 300, 600, true, null);				
			}
		}
	}
	
	class Assassination implements Runnable{
		private L1PcInstance _t;
		private int			_step;
		Assassination(L1PcInstance c){
			_t 		= c;
			_step 	= 0;
		}
		
		@Override
		public void run() {
			if(_t == null || _t.isDead() || _t.getMap().isSafetyZone(_t.getX(), _t.getY()) || _t.getMapId() != _body.getMapId())
				return;
			
			if(_step == 0){
				_step++;
				_body.broadcastPacket(new S_SkillSound(_body.getId(), 12157));
				GeneralThreadPool.getInstance().schedule(this, 80);
			}else if(_step == 1){
				_step++;
				_body.getMap().setPassable(_body.getLocation(), true);
				_body.getLocation().set(_t.getX(), _t.getY());
				_body.getMap().setPassable(_body.getLocation(), false);
				_body.broadcastPacket(new ServerBasePacket[]{new S_SkillSound(_t.getId(), 12158), new S_MoveCharPacket(_body)});
				GeneralThreadPool.getInstance().schedule(this, 80);
			}else{
				ServerBasePacket[] pcks = new ServerBasePacket[]{new S_DoActionGFX(_t.getId(), 18), new S_DoActionGFX(_t.getId(), ActionCodes.ACTION_Damage)};
				_t.sendPackets(pcks, false);
				_t.broadcastPacket(pcks, true);
				_t.receiveDamage(_body, MJCommons.calcDamage(_body, _t, 300, 600, false));
			}
		}
		
	}
	
	class BloodSucking implements Runnable{
		private int 		_step;
		BloodSucking(){
			_step 	= 0;
			_body.broadcastPacket(new ServerBasePacket[]{new S_DoActionGFX(_body.getId(), 18), new S_SkillSound(_body.getId(), 10525)});
		}
		@Override
		public void run() {
			if(_step <= 2){
				_step++;
				int x = _body.getX();
				int y = _body.getY();
				int tdmg = 0;
				for(L1PcInstance pc : L1World.getInstance().getRecognizePlayer(_body)){
					if(pc == null)
						continue;
					
					if(pc.isDead() || MJCommons.isUnbeatable(pc) || pc.getRedKnightClanId() != 0 || pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
						continue;
					
					if(MJCommons.getDistance(x, y, pc.getX(), pc.getY()) > 10)
						continue;
					
					if(!_body.glanceCheck(pc.getX(), pc.getY()))
						continue;
				
					int dmg = MJCommons.calcDamage(_body, pc, 200, 500, false);
					tdmg += dmg;
					pc.receiveDamage(_body, dmg);
					if(dmg > 0){
						ServerBasePacket pck = new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage);
						pc.sendPackets(pck, false);
						pc.broadcastPacket(pck);
						pck.clear();
					}
				}
				
				if(_step <= 2)
					GeneralThreadPool.getInstance().schedule(this, 500);
				_body.setCurrentHp(_body.getCurrentHp() + tdmg);
			}
		}
	}
	
	class TrueTarget implements Runnable{
		private int 		_count;
		private L1Character _t;
		
		TrueTarget(L1Character c){
			_t 		= c;
			_count 	= 0;
			_t.set트루타켓(10);
		}
		
		@Override
		public void run() {
			if(_t == null)
				return;
			
			if(_t.isDead() || _t.getMap().isSafetyZone(_t.getX(), _t.getY()) || _t.getMapId() != _body.getMapId()){
				_t.set트루타켓(0);
				return;
			}

			if(_count++ < 10){
				_target.broadcastPacket(new S_SkillSound(_t.getId(), 12299));
				GeneralThreadPool.getInstance().schedule(this, 300);
			}else{
				_t.set트루타켓(0);
			}
		}
	}
}
