package l1j.server.MJBotSystem.AI;

import java.util.ArrayList;
import java.util.Comparator;

import l1j.server.MJBotSystem.MJBotBrain;
import l1j.server.MJBotSystem.MJBotInvItem;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.Loader.MJBotInvItemLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Bot AI.
 * made by mjsoft, 2016.
 *  
 **********************************/

public abstract class MJBotAI implements Comparable<MJBotAI>{
	protected final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	protected final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };	
	
	protected L1PcInstance 					_body;
	protected MJBotBrain 					_brn;
	protected MJBotStatus					_status;
	protected MJBotType						_type;
	protected boolean						_isRemoved;
	protected long 							_actSleepMs;
	protected long							_mentDelay;
	protected long							_lastPotionMs;
	protected MJBotLocation					_loc;
	protected int 							_warCastle;
	public MJBotAI(boolean b){
		_loc			= null;
		_warCastle		= -1;
	}
	
	public abstract L1Character getCurrentTarget();
	
	public MJBotAI(){
		_isRemoved 		= true;
		_actSleepMs 	= 0;
		_lastPotionMs	= 0;
		_warCastle		= -1;
	}
	
	public void setWarCastle(int i){
		_warCastle = i;
	}
	
	public int getWarCastle(){
		return _warCastle;
	}
	
	public long getActSleepMs(){
		return _actSleepMs;
	}
	
	public void setActSleepMs(long ms){
		_actSleepMs = ms;
	}
	
	public L1PcInstance getBody(){
		return _body;
	}
	public void setBody(L1PcInstance body){
		_body = body;
	}
	
	public MJBotBrain getBrain(){
		return _brn;
	}
	public void setBrain(MJBotBrain brn){
		_brn = brn;
	}
	
	public boolean isRemoved(){
		return _isRemoved;
	}
	public void setRemoved(boolean b){
		_isRemoved = b;
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public MJBotStatus getStatus(){
		return _status;
	}
	public void setStatus(MJBotStatus status){
		_status = status;
	}
	
	public MJBotType getBotType(){
		return _type;
	}
	public void setBotType(MJBotType type){
		_type = type;
	}
	
	public long getMentDelay(){
		return _mentDelay;
	}
	
	public void setMentDelay(long l){
		_mentDelay = l;
	}
	
	public boolean isPotionUse(long l){
		if(l - _lastPotionMs >= MJBotLoadManager.MBO_POTION_DELAY){
			_lastPotionMs = l;
			return true;
		}
		return false;
	}
	
	public void equipped(){
		MJBotInvItemLoader 	loader 	= MJBotInvItemLoader.getInstance();
		MJBotInvItem 		invitem		= loader.get(_brn.getWeaponId());
		L1ItemInstance 		item		= null;
		if(invitem != null){
			item = invitem.create();
			_body.getInventory().storeItem(item);
			_body.getInventory().setEquipped(item, true);
			
			if(_body.getType() == 7){
				item = invitem.create();
				_body.getInventory().storeItem(item);
				_body.getInventory().setEquipped(item, true, false, false, true);
				_body.setCurrentWeapon(ActionCodes.ACTION_DoubleAxeWalk);
			}else
				_body.setCurrentWeapon(item.getItem().getType1());
			
			_body.setAttackRang(1);
			switch(_body.getCurrentWeapon()){
			case ActionCodes.ACTION_ChainSwordWalk:
				_body.setAttackRang(2);
				break;
				
			case ActionCodes.ACTION_BowWalk:
				_body.setAttackRang(10);
				break;
			}
		}
		
		ArrayList<Integer> armors = _brn.getArmorIds();
		if(armors != null){
			int size = armors.size();
			for(int i=0; i<size; i++){
				invitem = loader.get(armors.get(i));
				if(invitem == null)
					continue;
				
				item = invitem.create();
				_body.getInventory().storeItem(item);
				if(_body.getInventory().getTypeEquipped(2, item.getItem().getType()) <= 0)
					_body.getInventory().setEquipped(item, true);				
			}
		}
	}
	
	public boolean isSpaceTarget(L1Character target){
		if(_body.isElf())
			return true;
		
		short mid	= target.getMapId();
		L1Map map 	= L1WorldMap.getInstance().getMap(mid);
		for(int i=0; i<8; i++){
			if(map.isPassable(target.getX() + HEADING_TABLE_X[i], target.getY() + HEADING_TABLE_Y[i]))
				return true;
		}
		
		return false;
	}

	public void dispose(){
		_actSleepMs 	= 0;
		_lastPotionMs 	= 0;
		_warCastle		= -1;
		_loc			= null;
		if(_body != null){
			try{
				if(_body.getWorldObject() != null)
					_body.getWorldObject().clear();
				_body.logout();
				L1World.getInstance().removeVisibleObject(_body);
				L1World.getInstance().removeObject(_body);
				_body		= null;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		_brn			= null;
		_status			= null;
		_type			= null;
	}
	
	public void setRandLawful(){
		_body.setLawful(_brn.toRand(32768));
		_body.refresh();
	}
	
	public void summonDoll(){
		if(_body.getDollList().size() > 0)
			return;
		
		L1Npc npc = NpcTable.getInstance().getTemplate(_brn.getDollId());
		if(npc == null)
			return;
		
		int dType = MJBotUtil.getDollType(npc.get_npcId());
		if(dType == 0)
			return;
		
//		L1DollInstance doll = new L1DollInstance(npc, _body, dType, 0, 1);
		L1DollInstance doll = new L1DollInstance(npc, _body, 0, dType, 0, 1);
		_body.broadcastPacket(new S_SkillSound(doll.getId(), 5935));
		
	}
	
	public void doping(){
		if(_body.isDead())
			return;
		
		if(_body.getMoveSpeed() == 0){
			Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 191));
			Broadcaster.broadcastPacket(_body, new S_SkillHaste(_body.getId(), 1, 0));
			_body.setMoveSpeed(1);			
			return;
		}
		
		if(_body.isDarkelf()){
			if(!_body.hasSkillEffect(L1SkillId.MOVING_ACCELERATION)){
				_body.setSkillEffect(L1SkillId.MOVING_ACCELERATION, 1800);
			}
			return;
		}
		
		if(_body.isBlackwizard()){
			if(!_body.hasSkillEffect(L1SkillId.STATUS_FRUIT))
				_body.setSkillEffect(L1SkillId.STATUS_FRUIT, 1800);
			return;
		}
		
		if(_body.isWizard()){
			if(!_body.hasSkillEffect(L1SkillId.HOLY_WALK)){
				_body.setSkillEffect(L1SkillId.HOLY_WALK, 1800);
			}
			
			if(!_body.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION)){
				_body.setSkillEffect(L1SkillId.STATUS_BLUE_POTION, 600 * 1000);
			}
			return;
		}
		
		if(_body.isDragonknight()){
			if(_body.hasSkillEffect(L1SkillId.BLOOD_LUST))
				_body.setSkillEffect(L1SkillId.BLOOD_LUST, 1800);
			return;
		}
		
		if (_body.getBraveSpeed() == 0) {
			Broadcaster.broadcastPacket(_body, new S_SkillBrave(_body.getId(), 1, 0));
			_body.setBraveSpeed(1);
			_body.setSkillEffect(L1SkillId.STATUS_BRAVE, 300 * 1000);
			Broadcaster.broadcastPacket(_body, new S_SkillSound(_body.getId(), 751));
			return;
		}
	}
	
	public void poly(){
		if(_body.getLevel() < 52 || _body.hasSkillEffect(L1SkillId.SHAPE_CHANGE))
			return;
	
		L1PolyMorph.doPoly(_body, _brn.getPolyId(_body), 1800, 1, false);
	}
	
	public void undoPoly(){
		if(_body.hasSkillEffect(L1SkillId.SHAPE_CHANGE))
			_body.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
	}
	
	
	public void teleport(){
		L1Location newLocation = _body.getLocation().randomLocation(200, true);
		if(newLocation.getMap().isTeleportable())
			teleport(newLocation.getX(), newLocation.getY(), (short) newLocation.getMapId());
	}
	
	public void teleport(int x, int y, short mapId){
		try{
			_body.set_teleport_count(_body.get_teleport_count() + 1);
			_body.getMap().setPassable(_body.getLocation(), true);
			_body.start_teleport(x, y, mapId, _brn.toRand(8), 169, true, true);
			_body.set_teleport_count(0); 	
		}catch(Exception e){}
	}
	
	public void teleportcrown(int x, int y, short mapId){
		try{
			_body.set_teleport_count(_body.get_teleport_count() + 1);
			_body.getMap().setPassable(_body.getLocation(), true);
			_body.start_teleport(x, y, mapId, 4, 169, true, true);
			_body.set_teleport_count(0); 	
		}catch(Exception e){}
	}

	protected void move(final int x, final int y, final int h) {
		try {
			L1Map m = _body.getMap();
			int ox	= _body.getX();
			int oy	= _body.getY();
			_body.getLocation().set(x, y);
			_body.setHeading(h);
			L1WorldTraps.getInstance().onPlayerMoved(_body);		
			m.setPassable(_body.getLocation(), false);
			Broadcaster.broadcastPacket(_body, new S_MoveCharPacket(_body));
			m.setPassable(ox, oy, true);
		} catch (Exception e) {
		}
	}
	
	@Override
	public int compareTo(MJBotAI o) {
		if(_actSleepMs == o._actSleepMs) 		return 0;
		else if(_actSleepMs > o._actSleepMs) 	return 1;
		return -1;
	}
	
	protected class RedKnightSorter implements Comparator<L1Character>{
		@Override
		public int compare(L1Character o1, L1Character o2) {
			int d1 	= MJCommons.getDistance(_body.getX(), _body.getY(), o1.getX(), o1.getY());
			int d2 	= MJCommons.getDistance(_body.getX(), _body.getY(), o2.getX(), o2.getY());
			if(_brn.getClassType() != 2 && _brn.getClassType() != 3){
				if(d1 <= _body.getAttackRang())
					return -1;
				else if(d1 <= _body.getAttackRang())
					return 1;
			}
			
			int dl1 = 0;
			int dl2 = 0;
			if(_brn.getClassType() == 3){
				if(o1.getAI() != null && o1.getAI().getBotType() == MJBotType.SIEGELEADER){
					if(o2.getAI() != null && o2.getAI().getBotType() == MJBotType.SIEGELEADER){
						if(d1 == d2) 		return 0;
						else if(d1 > d2) 	return 1;
					}
					return -1;
				}else if(o2.getAI() != null && o2.getAI().getBotType() == MJBotType.SIEGELEADER){
					return 1;
				}
				
				if(d1 == d2) 		return 0;
				else if(d1 > d2) 	return 1;
				else				return -1;
			}				

			if(o1 instanceof L1DoorInstance){
				d1 -= 6;
			}else if(o1 instanceof L1PcInstance || o1 instanceof L1MonsterInstance){
				d1 -= 2;
				if(_loc != null)
					dl1 = MJCommons.getDistance(_loc.x, _loc.y, o1.getX(), o1.getY());
			}else if(o1 instanceof L1TowerInstance || o1 instanceof L1CastleGuardInstance)
				d1 -= 2;
			
			if(o2 instanceof L1DoorInstance){
				d2 -= 6;
			}else if(o2 instanceof L1PcInstance || o2 instanceof L1MonsterInstance){
				d2 -= 2;
				if(_loc != null)
					dl2 = MJCommons.getDistance(_loc.x, _loc.y, o2.getX(), o2.getY());
			}else if(o2 instanceof L1TowerInstance || o2 instanceof L1CastleGuardInstance)
				d2 -= 2;
			
			if(dl1 < dl2)
				d1 -= 1;
			else if(dl2 < dl1)
				d2 -= 1;
			
			if(d1 == d2)
				return 0;
			else if(d1 > d2)
				return 1;
			return -1;
		}
	}

	protected class TargetSorter implements Comparator<L1Character>{
		@Override
		public int compare(L1Character o1, L1Character o2) {
			int d1 	= MJCommons.getDistance(_body.getX(), _body.getY(), o1.getX(), o1.getY());
			int d2 	= MJCommons.getDistance(_body.getX(), _body.getY(), o2.getX(), o2.getY());
			if(_brn.getClassType() != 2 && _brn.getClassType() != 3){
				if(d1 <= _body.getAttackRang())
					return -1;
				else if(d1 <= _body.getAttackRang())
					return 1;
			}
			
			int dl1 = 0;
			int dl2 = 0;
			if(getBotType() == MJBotType.SIEGELEADER || getBotType() == MJBotType.HUNT){
				if(_brn.getClassType() == 3){
					if(o1.getAI() != null && o1.getAI().getBotType() == MJBotType.SIEGELEADER){
						if(o2.getAI() != null && o2.getAI().getBotType() == MJBotType.SIEGELEADER){
							if(d1 == d2) 		return 0;
							else if(d1 > d2) 	return 1;
						}
						return -1;
					}else if(o2.getAI() != null && o2.getAI().getBotType() == MJBotType.SIEGELEADER){
						return 1;
					}
					
					if(d1 == d2) 		return 0;
					else if(d1 > d2) 	return 1;
					else				return -1;
				}				

				if(o1 instanceof L1DoorInstance)
					d1 -= 6;
				else if(o1.getAI() != null && o1.getAI().getBotType() == MJBotType.REDKNIGHT){
					d2 -= 5;
					if(_loc != null)
						dl1 = MJCommons.getDistance(_loc.x, _loc.y, o1.getX(), o1.getY()) - 1;
				}else if(o1 instanceof L1PcInstance || o1 instanceof L1MonsterInstance){
					d1 -= 2;
					if(_loc != null)
						dl1 = MJCommons.getDistance(_loc.x, _loc.y, o1.getX(), o1.getY());
				}else if(o1 instanceof L1TowerInstance || o1 instanceof L1CastleGuardInstance)
					d1 -= 2;
				
				if(o2 instanceof L1DoorInstance){
					d2 -= 6;
				}else if(o2.getAI() != null && o2.getAI().getBotType() == MJBotType.REDKNIGHT){
					d2 -= 5;
					if(_loc != null)
						dl2 = MJCommons.getDistance(_loc.x, _loc.y, o2.getX(), o2.getY()) - 1;
				}else if(o2 instanceof L1PcInstance || o2 instanceof L1MonsterInstance){
					d2 -= 2;
					if(_loc != null)
						dl2 = MJCommons.getDistance(_loc.x, _loc.y, o2.getX(), o2.getY());
				}else if(o2 instanceof L1TowerInstance || o2 instanceof L1CastleGuardInstance)
					d2 -= 2;
				
				if(dl1 < dl2)
					d1 -= 1;
				else if(dl2 < dl1)
					d2 -= 1;
				
				if(d1 == d2)
					return 0;
				else if(d1 > d2)
					return 1;
				else
					return -1;
			}
			
			if(d1 > d2) 		return 1;
			else if(d1 < d2)	return -1;
			
			if(_brn.getSense() > _brn.getHormon()){
				int hp1 = (int) (((double) o1.getCurrentHp() / (double) o1.getMaxHp()) * 100);
				int hp2 = (int) (((double) o2.getCurrentHp() / (double) o2.getMaxHp()) * 100);
				if(hp1 > hp2) 		return 1;
				else if(hp1 < hp2)	return -1;
			}
			
			if(o1.getLevel() > o2.getLevel()) 		
				return 1;
			return -1;
		}
	}
}
