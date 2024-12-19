package l1j.server.MJBotSystem.AI;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;

import l1j.server.MJBotSystem.MJBotDropItem;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotSpell;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.Loader.MJBotDropItemLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Loader.MJBotSpellLoader;
import l1j.server.MJBotSystem.MJAstar.Astar;
import l1j.server.MJBotSystem.MJAstar.Node;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.utils.MJCommons;

public abstract class MJBotMovableAI extends MJBotAI{
	protected int							_deadPending;
	protected int 							_movePending;
	protected int							_potionCount;
	protected long							_magicDelay;
	protected L1Character					_target;
	protected PriorityQueue<L1Character>	_targetQ;
	protected ArrayDeque<Node>				_deQ;
	protected Astar							_ast;
	protected MJWar							_war;
	protected MJBotStatus					_lastStatus;
	protected int							_astFailCount;
	protected int							_wayLevel;
	protected int							_lastAction;
	public MJBotMovableAI(){
		super();
		_deadPending 	= 0;
		_potionCount	= 0;
		_movePending	= 0;
		_magicDelay		= 0;
		_astFailCount	= 0;
		_wayLevel		= 0;
		if(this instanceof MJBotProtectorAI || this instanceof MJBotRedKnightAI)
			_targetQ		= new PriorityQueue<L1Character>(MJBotLoadManager.MBO_TARGET_Q_SIZE, new RedKnightSorter());
		else
			_targetQ		= new PriorityQueue<L1Character>(MJBotLoadManager.MBO_TARGET_Q_SIZE, new TargetSorter());
		_war			= null;
		_lastStatus		= MJBotStatus.WALK;
		_lastAction		= 0;
	}
	
	public void setLastStatus(MJBotStatus status){
		_lastStatus = status;
	}
	
	public MJBotStatus getLastStatus(){
		return _lastStatus;
	}
	
	
	public abstract void setting(long time);
	public abstract void settingDefense(long time);
	public abstract void settingOffense(long time);
	public abstract void walk(long time);
	public abstract void walkDefense(long time);
	public abstract void walkOffense(long time);
	public abstract void searchWalk(long time);
	public abstract void searchWalkDefense(long time);
	public abstract void searchWalkOffense(long time);
	public abstract void attack(long time);
	public abstract void attackDefense(long time);
	public abstract void attackOffense(long time);
	public abstract void pickUp();
	public abstract void pickupDefense();
	public abstract void pickupOffense();
	public abstract boolean searchTarget();
	public abstract boolean searchTargetDefense();
	public abstract boolean searchTargetOffense();
	
	@Override
	public void dispose(){
		_deadPending 	= 0;
		_magicDelay		= 0;
		_potionCount	= 0;
		_movePending	= 0;
		_wayLevel		= 0;
		_target			= null;
		_war			= null;
		_astFailCount	= 0;
		_lastAction		= 0;
		_targetQ.clear();
		if(_deQ != null)
			_deQ.clear();
		if(_ast != null)
			_ast.release();
		super.dispose();
	}
	
	public int getLastAction(){
		return _lastAction;
	}
	
	public void setWar(MJWar war){
		_war = war;
	}
	
	public MJWar getWar(){
		return _war;
	}
	
	public boolean isRunDead(){
		if(getWarCastle() != -1)
			return true;
		
		if(_deadPending == 0){
			MJBotUtil.sendBotDeathMent(this, _target);
			if(_body.getMap().isNormalZone(_body.getLocation()) && this instanceof MJBotHuntAI){
				ArrayDeque<MJBotDropItem> itemQ = MJBotDropItemLoader.getInstance().getMixDropItems(this);
				if(itemQ != null){
					while(!itemQ.isEmpty())	
						itemQ.poll().toDrop(this);
				}
			}
		}
		if(_deadPending++ >= _brn.getDeadCount()){
			_deadPending = 0;
			return true;
		}
		return false;
	}
	
	public boolean isMovePause(){
		if((_movePending+=2) >= _brn.getMoveCount()){
			_movePending = 0;
			return true;
		}
		return false;
	}
	
	public boolean isEmptyTarget(){
		return _target == null && _targetQ.isEmpty();
	}
	
	public void addTarget(L1Character c){
		if(getWarCastle() != -1)
			return;
		
		if(_target == null)
			_target = c;
		else{
			if(!_targetQ.contains(c))
				_targetQ.add(c);
		}
	}
	
	public void death(){
		if(!isRunDead())
			return;
		
		_wayLevel = 0;
		L1PolyMorph.undoPoly(_body);
		int[] loc = Getback.GetBack_Location(_body, true);
		_body.removeAllKnownObjects();		
		Broadcaster.broadcastPacket(_body, new S_RemoveObject(_body));
		_body.setCurrentHp(_body.getLevel());
		_body.set_food(39);
		_body.setDead(false);
		_body.setStatus(0);		
		L1World.getInstance().moveVisibleObject(_body, loc[2]);
		_body.setX(loc[0]);
		_body.setY(loc[1]);
		_body.setMap((short) loc[2]);
		Broadcaster.broadcastPacket(_body, S_WorldPutObject.get(_body));
		setStatus(MJBotStatus.SETTING);
	}
	
	@Override
	public void teleport(int x, int y, short mapId){
		_targetQ.clear();
		_target = null;
		super.teleport(x,  y, mapId);
	}
	
	public void teleportcrown(int x, int y, short mapId){
		_targetQ.clear();
		_target = null;
		super.teleportcrown(x,  y, mapId);
	}
	
	protected L1Character selectObject(){
		try{
			
			if(_target != null && !_target.isDead() && _target.getMapId() == _body.getMapId()){
				if(_body.glanceCheck(_target.getX(), _target.getY()))
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
				
				//if(!_body.glanceCheck(tmp.getX(), tmp.getY()))
				//	continue;
				
				return tmp;
			}
		}catch(Exception e){}
		return null;
	}
	
	public void move(L1Object o, int x, int y, int h){
		if(_body == null || _target == null)
			return;
		
		if(_target != null && _target.isDead()){
			_target = null;
			return;
		}
		
		int d = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
		if(!moveWide(d))
			setStatus(MJBotStatus.WALK);
	}
	
	protected void move(int dir){
		if (dir >= 0) {
			move(_body.getX() + HEADING_TABLE_X[dir], _body.getY() + HEADING_TABLE_Y[dir], dir);
		}
	}
	
	protected boolean moveWide(int h){
		if(MJCommons.isVisibleObjects(_body, h)){
			h = MJCommons.checkAroundWidePassable(_body, h);
			if(h == -1)
				return false;
		}
		
		move(h);
		return true;
	}
	
	protected boolean move(){
		if(_loc == null)
			_loc = MJBotUtil.createRandomLocation(_body.getX(), _body.getY(), _body.getMapId());
		
		if(_loc.map == _body.getMapId() && _loc.x == _body.getX() && _loc.y == _body.getY()){
			_loc = null;
			return false;
		}
		
		int dir = MJCommons.checkPassable(_body.getX(), _body.getY(), _body.getMapId(), MJCommons.calcheading(_body.getX(), _body.getY(), _loc.x, _loc.y));
		if(dir != -1)
			move(dir);
		else{
			if(isMovePause())
				return false;
		}
		return true;
	}
	
	protected void attack(L1Object o, int x, int y, boolean isLong, int gfxMode, int alpha_dmg){
		if(_target == null){
			setStatus(MJBotStatus.WALK);
			return;
		}
		
		_body.delInvis();
		_target.onAction(_body);
		if(_body.getLevel() >= 50 && _body.getWeapon().getItem().getType() == 17){
			if (_body.getWeapon().getItemId() == 504) 
				Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 6983));
			 else 
				Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 7049));
		}
	}
	
	protected void magic(L1Object o, MJBotSpell sp, long time){
		if(sp.skillId == L1SkillId.TRIPLE_ARROW){
			Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 4394));
			attack(o, 0, 0, true, 1, 0);
			attack(o, 0, 0, true, 1, 0);
			attack(o, 0, 0, true, 1, 0);
		}else
			new L1SkillUse().handleCommands(_body, sp.skillId, o.getId(), o.getX(), o.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		_magicDelay = time + sp.delay;
	}
	
	protected void magic(L1Object o, int skillId, long delay,long time){
		if(skillId == L1SkillId.TRIPLE_ARROW){
			Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 4394));
			attack(o, 0, 0, true, 1, 0);
			attack(o, 0, 0, true, 1, 0);
			attack(o, 0, 0, true, 1, 0);
		}else
			new L1SkillUse().handleCommands(_body, skillId, o.getId(), o.getX(), o.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		_magicDelay = time + delay;
	}
	
	protected boolean magic(L1Object o, int dir, long time){
		if(_magicDelay + MJBotLoadManager.MBO_SKILL_REVISION_DELAY > time)
			return false;
		
		ArrayList<MJBotSpell> spells = MJBotSpellLoader.getInstance().getSkills(_body.getType());
		if(spells == null)
			return false;
		
		int size 		= spells.size();
		MJBotSpell sp	= null;
		if(o instanceof L1MonsterInstance || o instanceof L1NpcInstance){
			for(int i=0; i<size; i++){
				sp = spells.get(i);
				if((sp.spellTarget & MJBotSpell.SPT_MOB) == 0)
					continue;
				
				int val = _brn.toRand(100);
				if(dir <= sp.direction &&  val <= sp.dice + _brn.getAddSkillDice()){
					magic(o, sp, time);
					return true;
				}
			}
		}else if(o instanceof L1PcInstance){
			for(int i=0; i<size; i++){
				sp = spells.get(i);
				if((sp.spellTarget & MJBotSpell.SPT_USER) == 0)
					continue;
				
				int val = _brn.toRand(100);
				if(dir <= sp.direction &&  val <= sp.dice + _brn.getAddSkillDice()){
					magic(o, sp, time);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean buff(long time){
		if(_magicDelay + MJBotLoadManager.MBO_SKILL_REVISION_DELAY > time)
			return false;
		
		if(_body.isWizard() && _body.getCurrentHp() <= (_brn.getSense() * 10)){
			buff(L1SkillId.FULL_HEAL);
			_magicDelay = time + 500;
			return true;
		}
		
		ArrayList<MJBotSpell> spells = MJBotSpellLoader.getInstance().getBuffs(_body.getType());
		if(spells == null)
			return false;
		
		int size 		= spells.size();
		MJBotSpell sp 	= null;
		for(int i=0; i<size; i++){
			sp = spells.get(i);
			if(!_body.hasSkillEffect(sp.skillId)){
				if(sp.dice <= 0 || _brn.toRand(100) <= sp.dice + _brn.getAddBuffDice()){
					if(MJBotUtil.isWeaponSkill(sp.skillId))
						_body.setSkillEffect(sp.skillId, 1800000);
					buff(sp.skillId);
					_magicDelay = time + sp.delay;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean immune(L1Character c, long time){
		if(_magicDelay + MJBotLoadManager.MBO_SKILL_REVISION_DELAY > time)
			return false;
		
		if(c.hasSkillEffect(L1SkillId.IMMUNE_TO_HARM))
			return false;
		
		buff(c, L1SkillId.IMMUNE_TO_HARM);
		_magicDelay = time + 1500;
		return true;
	}
	
	public boolean healAll(long time){
		if(_magicDelay + MJBotLoadManager.MBO_SKILL_REVISION_DELAY > time)
			return false;
		
		buff(L1SkillId.HEAL_ALL);
		_magicDelay = time + 700;
		return true;
	}
	
	public boolean heal(L1Character c, long time){
		if(_magicDelay + MJBotLoadManager.MBO_SKILL_REVISION_DELAY > time)
			return false;
		
		if(c.getCurrentHp() == c.getMaxHp())
			return false;
		
		buff(c, L1SkillId.GREATER_HEAL);
		_magicDelay = time + 400;
		return true;
	}
	
	protected void buff(int id){
		Broadcaster.broadcastPacket(_body, new S_DoActionGFX(_body.getId(), 19));
		new L1SkillUse().handleCommands(_body, id, _body.getId(), _body.getX(), _body.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
	}
	
	protected void buff(L1Character c, int id){
		Broadcaster.broadcastPacket(_body, new S_DoActionGFX(_body.getId(), 19));
		new L1SkillUse().handleCommands(_body, id, c.getId(), c.getX(), c.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);		
	}
	
	public void healingPotion(boolean isDir, long time){
		if(_body.isDead())
			return;
		
		_body.setCurrentMp(_body.getMaxMp());
		if(_body.hasSkillEffect(71))
			return;
		
		if(!isDir && !isPotionUse(time))
			return;
		
		int p = (int) (((double) _body.getCurrentHp() / (double) _body.getMaxHp()) * 100);
		if(isDir || p <= _brn.getPotionCutLine()){
			Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 197));
			int heal = _body.getAbility().getTotalCon() * 3;
			if (_body.hasSkillEffect(L1SkillId.POLLUTE_WATER))
				heal /= 2;
			
			_body.setCurrentHp(_body.getCurrentHp() + heal);
			_potionCount--;
		}
		
		if(_body.getMap().isSafetyZone(_body.getLocation()))
			return;
		
		if(_potionCount <= 0 || p <= _brn.getSettingCutLine()){
			if(_brn.toBoolean())
				setStatus(MJBotStatus.SETTING);
			else
				teleport();
			_potionCount = 200;
			_targetQ.clear();
			_target = null;
		}
	}
	
	protected void findPath(MJBotLocation loc){
		findPath(_body.getAttackRang(), loc);
	}
	
	protected void findPath(int range, MJBotLocation loc){
		
		_deQ.clear();
		_ast.release();
		_ast.setMapId(_body.getMapId());
		_ast.setRange(range);
		Node node = _ast.find(_body.getX(), _body.getY(), loc.x, loc.y);
		while(node != null){
			_deQ.push(node);
			node = node.parent;
		}
	}
	
	protected void moveAstar(){
		if(_deQ.isEmpty()){
			_ast.release();
			return;
		}
		
		Node node = _deQ.pop();
		int bx = _body.getX();
		int by = _body.getY();
		if(node.x == bx && node.y == by){
			_astFailCount++;
			return;
		}
		
		int h = MJCommons.calcheading(bx, by, node.x, node.y);
		if(h == -1){
			_astFailCount++;
			return;
		}
		
		if(!moveWide(h))
			_astFailCount++;
		
		if(node.x != _body.getX() && node.y != _body.getY())
			_deQ.push(node);
	}
	
	@Override
	public L1Character getCurrentTarget(){
		return _target;
	}
}
