package l1j.server.MJBotSystem.AI;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.MJBotSystem.MJBotClanInfo;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.Loader.MJBotClanInfoLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.MJAstar.Astar;
import l1j.server.MJBotSystem.MJAstar.Node;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1ClanMatching;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class MJBotSiegeLeaderAI extends MJBotMovableAI{
	
	public MJBotSiegeLeaderAI(){
		super();
		_ast			= new Astar();
		_deQ			= new ArrayDeque<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
	}

	@Override
	public void death(){
		if(_body.getClan().getCastleId() != getWarCastle()){
			for(ClanMember m : _body.getClan().getClanMemberList()){
				if(m.player == null){
					continue;
				}
				
				MJBotAI ai = m.player.getAI();
				if(ai == null || !(ai instanceof MJBotHuntAI))
					continue;
				
				MJBotMovableAI mai = (MJBotMovableAI)ai;
				mai._target = null;
				mai._targetQ.clear();
				mai.setStatus(MJBotStatus.SETTING);
			}
			setWarCastle(-1);
		}
		super.death();
	}
	
	@Override
	public void setWarCastle(int castleId){
		super.setWarCastle(castleId);
		_body.getClan().setInCastleId(castleId);
		CopyOnWriteArrayList<ClanMember> list = _body.getClan().getClanMemberList();
		for(ClanMember m : list){
			if(m.player == null){
				continue;
			}
			
			MJBotAI ai = m.player.getAI();
			if(ai == null || !(ai instanceof MJBotHuntAI))
				continue;
			
			((MJBotMovableAI)ai).setWarCastle(getWarCastle());
		}
		if(castleId == -1)
			setWar(null);
	}
	
	@Override
	public void setWar(MJWar war){
		super.setWar(war);
	}
	
	@Override
	public void dispose(){
		MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(_body.getClanname());
		if(cInfo != null){
			L1Clan clan				= cInfo.clanObject;
			if(clan != null){
				cInfo.clanObject 	= null;
				cInfo.leaderAI		= null;
				L1ClanMatching.getInstance().deleteClanMatching(_body);
				for (int i = 0; i < clan.getClanMemberList().size(); i++) { 
					try{
						L1PcInstance pc = L1World.getInstance().getPlayer(clan.getClanMemberList().get(i).name);
						if (pc == null) 
							pc = CharacterTable.getInstance().restoreCharacter(clan.getClanMemberList().get(i).name);
						pc.ClearPlayerClanData(clan);
					}catch(Exception e){}
				}
				ClanTable.getInstance().deleteClan(clan);
			}
		}
		super.dispose();
	}
	
	@Override
	public void setting(long time) {
		_target = null;
		_targetQ.clear();
		_deQ.clear();
		_ast.release();
		if(!MJBotUtil.isInTown(_body)){
			_body.getMap().setPassable(_body.getLocation(), true);
			MJBotLocation loc = MJBotUtil.createRandomLocation(
					MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
					MJBotLoadManager.MBO_WANDER_MAT_TOP, 
					MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
					MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
					MJBotLoadManager.MBO_WANDER_MAT_MAPID
				);
			teleport(loc.x, loc.y, (short)loc.map);
			return;
		}
		if(getWarCastle() == -1)
			return;
		
		if(!MJCastleWarBusiness.getInstance().isNowWar(getWarCastle())){
			setWarCastle(-1);
			setWar(null);
			return;
		}
		
		if(_body.getCurrentHp() != _body.getMaxHp()){
			healingPotion(true, time);
			return;
		}
		
		
		poly();	
		MJBotLocation loc	 = MJBotUtil.createCastleLocation(getWarCastle());
		if(loc == null)
			return;
		
		teleport(loc.x, loc.y, (short)loc.map);
		setStatus(MJBotStatus.WALK);
	}
	
	@Override
	public void settingDefense(long time) {
		_target = null;
		_targetQ.clear();
		
		if(!MJBotUtil.isInTown(_body)){
			_body.getMap().setPassable(_body.getLocation(), true);
			MJBotLocation loc = MJBotUtil.createRandomLocation(
					MJBotLoadManager.MBO_WANDER_MAT_LEFT, 
					MJBotLoadManager.MBO_WANDER_MAT_TOP, 
					MJBotLoadManager.MBO_WANDER_MAT_RIGHT, 
					MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, 
					MJBotLoadManager.MBO_WANDER_MAT_MAPID
				);
			teleport(loc.x, loc.y, (short)loc.map);
		}
		
		if(!MJCastleWarBusiness.getInstance().isNowWar(getWarCastle())){
			setWarCastle(-1);
			setWar(null);
			return;
		}
		
		if(_body.getCurrentHp() != _body.getMaxHp()){
			healingPotion(true, time);
			return;
		}
		
		poly();	
		MJBotLocation loc	 = MJBotUtil.createDefenseLocation(getWarCastle());
		if(loc == null)
			return;
		
		teleport(loc.x, loc.y, (short)loc.map);
		setStatus(MJBotStatus.WALK);
	}

	@Override
	public void walk(long time) {
		if(!MJBotUtil.isInCastle(_body, getWarCastle()) && !MJBotUtil.isInCastleStartup(_body, getWarCastle())){
			setStatus(MJBotStatus.SETTING);
			return;
		}
		
		int lvl	= MJBotUtil.getCastleWayLevel(_body, getWarCastle());
		if(lvl >= 2){
			searchTarget();
			if(_target != null){
				setStatus(MJBotStatus.ATTACK);
				return;
			}
		}else if(lvl == 1){
			int sz = MJBotUtil.getCastleWayClanMemberNum(_body.getClanid(), getWarCastle(), lvl + 1);
			if(sz > (_brn.getSense() / 10))
				lvl++;
		}else{
			int sz = MJBotUtil.getCastleWayClanMemberNum(_body.getClanid(), getWarCastle(), 1);
			if(sz > (_brn.getSense() / 10))
				lvl++;
			sz = MJBotUtil.getCastleWayClanMemberNum(_body.getClanid(), getWarCastle(), 2);
			if(sz > (_brn.getSense() / 10))
				lvl++;
		}
		
		if(_loc == null || lvl != _wayLevel || MJCommons.getDistance(_body.getX(), _body.getY(), _loc.x, _loc.y) <= 1){
			_wayLevel 	= lvl;
			_loc = MJBotUtil.getCastleWayRandomLoc(getWarCastle(), _wayLevel);
			_deQ.clear();
			_ast.release();
			findPath(_loc);
		}
		
		if(_deQ.isEmpty()){
			_ast.release();
			findPath(_loc);
		}
		moveAstar();
		
		if(_astFailCount > 10){
			_astFailCount = 0;
			_deQ.clear();
			_ast.release();
			setStatus(MJBotStatus.SETTING);
			return;
		}
	}
	
	@Override
	public void walkDefense(long time) {
		if(_target == null)
			searchTargetDefense();
		else{
			if(_body.getX() != _target.getX() || _body.getY() != _target.getY()){
				int h = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
				if(MJCommons.isVisibleObjects(_body, h)){
					h = MJCommons.checkAroundPassable(_body, h);
					if(h == -1)
						return;
				}
				move(h);
			}else{
				setStatus(MJBotStatus.ATTACK);
			}
		}
	}

	@Override
	public void searchWalk(long time) {
		if(!MJBotUtil.isInCastle(_body, getWarCastle()) && !MJBotUtil.isInCastleStartup(_body, getWarCastle()))
			setStatus(MJBotStatus.SETTING);
		else if(_target == null || _target.isDead())
			searchTarget();
		else{
			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());
			if(dir > _body.getAttackRang()){
				int h = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
				if(MJCommons.isVisibleObjects(_body, h)){
					h = MJCommons.checkAroundPassable(_body, h);
					if(h == -1)
						return;
				}
				move(h);
			}else{
				if(_target instanceof L1CrownInstance)
					setStatus(MJBotStatus.ATTACK);
			}
		}
	}
	
	@Override
	public void searchWalkDefense(long time) {
		if(_target == null)
			searchTargetDefense();
		else{
			if(_body.getX() != _target.getX() || _body.getY() != _target.getY()){
				int h = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
				if(MJCommons.isVisibleObjects(_body, h)){
					h = MJCommons.checkAroundPassable(_body, h);
					if(h == -1)
						return;
				}
				move(h);
			}
		}
	}

	@Override
	public void attack(long time) {
		try{
			
			_target = selectObject();
			_lastStatus = MJBotStatus.WALK;
			if(_target == null){
				setStatus(MJBotStatus.WALK);
				return;
			}
			
			if((_target instanceof L1CrownInstance && ((L1NpcInstance)_target).isDestroyed()) || _target.isDead()){
				setStatus(MJBotStatus.SETTING);
				return;
			}
			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());
			if(dir > _body.getAttackRang() || !isAttack(_target)){
				if(_deQ.isEmpty()){
					_ast.release();
					MJBotLocation loc = new MJBotLocation(_target.getX(), _target.getY(), _target.getMapId());
					findPath(loc);
				}				
				moveAstar();
				if(_astFailCount > 10){
					_astFailCount = 0;
					_deQ.clear();
					_ast.release();
				}
				return;
			}
			
			attack(_target, 0, 0, false, 1, 0);
			_lastStatus = MJBotStatus.ATTACK;
		}catch(Exception e){}
	}

	@Override
	public void pickUp() {
		
	}

	@Override
	public boolean searchTarget() {
		_targetQ.clear();
		_target	= null;
		_target = MJBotUtil.findCastleDoor(getWarCastle());
		if(_target != null && !_target.isDead())
			return true;
		
		int cid = getWarCastle();
		_body.updateObject();
		ArrayList<L1Object> list = MJCommons.createKnownList(_body);
		for(L1Object obj : list){
			if(obj == null)
				continue;
			
			if(!MJBotUtil.isInCastle(obj, cid))
				continue;
			
			if(obj instanceof L1TowerInstance){
				_target = (L1Character)obj;
				if(_target.isDead()){
					_target = null;
					continue;
				}
				break;
			}
			
			if(obj instanceof L1CrownInstance){
				_target = (L1Character)obj;
				break;
			}
		}
		
		return true;
	}

	@Override
	public boolean searchTargetDefense() {
		_targetQ.clear();
		_target	= null;
		int cid = getWarCastle();
		for(L1Object obj : L1World.getInstance().getVisibleObjects(_body.getMapId()).values()){
			if(obj == null)
				continue;
			
			if(!MJBotUtil.isInCastle(obj, cid))
				continue;
			
			if(obj instanceof L1TowerInstance){
				_target = (L1Character)obj;
				if(_target.isDead()){
					_target = null;
					continue;
				}
				break;
			}
			
			if(obj instanceof L1CrownInstance){
				_target = (L1Character)obj;
				break;
			}
		}
		if(_target == null){
			setStatus(MJBotStatus.SETTING);
			return false;
		}
		return true;
	}
	
	@Override
	public void healingPotion(boolean isDir, long time){
		if(_body.isDead())
			return;
		
		_body.setCurrentMp(_body.getMaxMp());
		if(_body.hasSkillEffect(71))
			return;
		
		if(!isDir && !isPotionUse(time))
			return;
		
		if(isDir || _body.getCurrentHp() != _body.getMaxHp()){
			Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 197));
			int heal = _body.getAbility().getTotalCon() * 3;
			if (_body.hasSkillEffect(L1SkillId.POLLUTE_WATER))
				heal /= 2;
			
			_body.setCurrentHp(_body.getCurrentHp() + heal);
		}
	}
	
	protected boolean isAttack(L1Character c){
		return (c != null && c.getMapId() == _body.getMapId() && !c.isDead());
	}

	@Override
	public void attackDefense(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickupDefense() {
		// TODO Auto-generated method stub
		
	}
	
//	@Override
//	public void poly(){
//		if(!_body.hasSkillEffect(L1SkillId.SHAPE_CHANGE))
//			L1PolyMorph.doPoly(_body, 15526, 1800, 1, false);
//	}
	
	@Override
	public void poly() {
	    int x = _body.getX();
	    int y = _body.getY();
	    int mapId = _body.getMapId();

	    // 특정 좌표에 위치하면 변신 해제
	    if ((x >= 33629 && x <= 33634) && (y >= 32675 && y <= 32680) && mapId == 4) {
	        if (_body.hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
	            L1PolyMorph.undoPoly(_body); // 변신을 해제한다.
	        }
	    } else {
	        if (!_body.hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
	            L1PolyMorph.doPoly(_body, 15526, 1800, 1, false); // 난쟁이 변신 1800초
	        }
	    }
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
	
	@Override
	protected L1Character selectObject(){
		try{
			
			if(_target != null && !_target.isDead() && _target.getMapId() == _body.getMapId()){
				return _target;
			}
			
			_targetQ.comparator();
			if(_deQ != null)
				_deQ.clear();
			if(_ast != null)
				_ast.release();
			while(!_targetQ.isEmpty()){
				L1Character tmp = _targetQ.poll();
				if(tmp.isInvisble() || tmp.isDead() || tmp.getMapId() != _body.getMapId())
					continue;
				
				return tmp;
			}
		}catch(Exception e){}
		return null;
	}
}
