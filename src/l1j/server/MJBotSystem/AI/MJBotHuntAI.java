package l1j.server.MJBotSystem.AI;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import l1j.server.MJBotSystem.MJBotClanInfo;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.Loader.MJBotClanInfoLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Loader.MJBotLocationLoader;
import l1j.server.MJBotSystem.MJAstar.Astar;
import l1j.server.MJBotSystem.MJAstar.Node;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Bot hunt AI. made by mjsoft, 2016.
 * 
 **********************************/
public class MJBotHuntAI extends MJBotMovableAI {

	protected int _searchPending;
	protected int _pickupPending;
	protected L1Object _gItem;
	protected L1Inventory _gInventory;
	protected L1Character _towerObject;
	protected int _initPoint;
	protected long _lastSearchTime;

	@Override
	public void setWarCastle(int castleId) {
		try {
			if (getWarCastle() == castleId)
				return;

			super.setWarCastle(castleId);
			setStatus(MJBotStatus.SETTING);
			_lastSearchTime = 0;
			if (castleId == -1) {
				if (_ast != null) {
					_ast.release();
					_ast = null;
				}

				if (_deQ != null) {
					_deQ.clear();
				}
			} else {
				_ast = new Astar();
				_deQ = new ArrayDeque<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		_searchPending = _pickupPending = 0;
		_gItem = null;
		_gInventory = null;
		_towerObject = null;
		_initPoint = 0;
		_lastSearchTime = 0;
		super.dispose();
	}

	public boolean isPickupPause() {
		if (_pickupPending++ >= _brn.getPickupCount()) {
			_pickupPending = 0;
			return true;
		}
		return false;
	}

	public boolean isSearchPause() {
		if (_searchPending++ >= _brn.getSearchCount()) {
			_searchPending = 0;
			return true;
		}
		return false;
	}

	public boolean isMovePauseOffense(int way) {
		try {
			int p = 0;
			if (_brn.getClassType() == 2) {
				if (way == 0) {
					p = MJBotLoadManager.MBO_HUNT_WALKOFFENCE_SPENDING_LV1;
				} else if (way == 1) {
					p = MJBotLoadManager.MBO_HUNT_WALKOFFENCE_SPENDING_LV2;
				} else {
					p = MJBotLoadManager.MBO_HUNT_WALKOFFENCE_SPENDING_LV3;
				}
			} else {
				if (way == 0) {
					p = MJBotLoadManager.MBO_HUNT_WALKOFFENCE_PENDING_LV1;
				} else if (way == 1) {
					p = MJBotLoadManager.MBO_HUNT_WALKOFFENCE_PENDING_LV2;
				} else {
					p = MJBotLoadManager.MBO_HUNT_WALKOFFENCE_PENDING_LV3;
				}
			}
			if (_movePending++ >= p) {
				_movePending = 0;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean searchTarget() {
		_targetQ.clear();
		_gItem = null;

		ArrayList<L1Object> list = MJCommons.createKnownList(_body);
		L1Object obj = null;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			obj = list.get(i);
			if (obj == null)
				continue;

			if (obj.getId() == _body.getId())
				continue;

			if (obj instanceof L1ItemInstance) {
				_gInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
				L1ItemInstance item = _gInventory.getItem(obj.getId());
				if (item != null) {
					_gItem = item;
					_gInventory = null;
				}
			} else if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (pc.isDead() || (_body.getClanid() == pc.getClanid() && _brn.getHormon() < 90))
					continue;
				_targetQ.offer(pc);
			} else if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance m = (L1MonsterInstance) obj;
				if (m.isDead() || m.getHiddenStatus() >= 1 || !_body.glanceCheck(m.getX(), m.getY()))
					continue;
				_targetQ.offer(m);
			}
		}

		if (_targetQ.isEmpty()) {
			teleport();
			return false;
		}

		L1Character t = _targetQ.peek();
		if (t instanceof L1PcInstance)
			MJBotUtil.sendBotOnPerceiveMent(this, t);
		return true;
	}

	@Override
	public void pickUp() {
		try {
			try {
				for(L1ItemInstance item : _body.getInventory().getItems()){
					if (item == null || item.isEquipped() || item.getItemId() == 40308 || item.getItemId() == 41159)
						continue;

					_body.getInventory().removeItem(item);
				}

				if (isPickupPause()) {
					_gItem = null;
					_gInventory = null;
					_searchPending = _movePending = _pickupPending = 0;
					setStatus(MJBotStatus.WALK);
					return;
				}

				if (_gItem != null && !MJCommons.isDistance(_body.getX(), _body.getY(), _body.getMapId(), _gItem.getX(),
						_gItem.getY(), _gItem.getMapId(), (_body.isElf() ? 8 : 1))) {
					move(_gItem, _gItem.getX(), _gItem.getY(), 0);
					_gInventory = L1World.getInstance().getInventory(_gItem.getX(), _gItem.getY(), _gItem.getMapId());
					L1Object obj = _gInventory.getItem(_gItem.getId());
					if (obj == null) {
						_gItem = null;
						_gInventory = null;
						setStatus(MJBotStatus.WALK);
					}
					return;
				}
			} catch (Exception e) {
				_gItem = null;
				_gInventory = null;
				///
				e.printStackTrace();
			}

			ArrayList<L1Object> olist = MJCommons.createKnownList(_body);
			int osize = olist.size();
			for (int i = 0; i < osize; i++) {
				L1Object obj = olist.get(i);
				if (obj == null || !(obj instanceof L1ItemInstance)
						|| _body.getLocation().getTileLineDistance(obj.getLocation()) > 2)
					continue;

				L1Inventory inv = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
				L1ItemInstance item = inv.getItem(obj.getId());
				if (item != null) {
					inv.tradeItem(item.getId(), item.getCount(), _body.getInventory());
					_body.getLight().turnOnOffLight();
					Broadcaster.broadcastPacket(_body,
							new S_AttackPacket(_body, obj.getId(), ActionCodes.ACTION_Pickup));
					_gItem = null;
					inv = null;
					if (isPickup())
						setStatus(MJBotStatus.PICKUP);
					else
						setStatus(MJBotStatus.WALK);
					break;
				}
			}

		} catch (Exception e) {
			_gItem = null;
			_gInventory = null;
			_target = null;
			setStatus(MJBotStatus.WALK);
			///
			e.printStackTrace();
		}
	}

	protected boolean isPickup() {
		try {
			_gItem = null;
			_gInventory = null;

			ArrayList<L1Object> list = MJCommons.createKnownList(_body);
			int size = list.size();
			for (int i = 0; i < size; i++) {
				L1Object obj = list.get(i);
				if (obj == null || !(obj instanceof L1ItemInstance))
					continue;

				_gInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
				L1ItemInstance item = _gInventory.getItem(obj.getId());
				if (item != null) {
					_gItem = item;
					_gInventory = null;
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void setting(long time) {
		try {
			_target = null;
			_targetQ.clear();
			if (!_body.getMap().isSafetyZone(_body.getLocation()) || !MJBotUtil.isInTown(_body)) {
				_body.getMap().setPassable(_body.getLocation(), true);
				MJBotLocation loc = MJBotUtil.createRandomLocation(MJBotLoadManager.MBO_WANDER_MAT_LEFT,
						MJBotLoadManager.MBO_WANDER_MAT_TOP, MJBotLoadManager.MBO_WANDER_MAT_RIGHT,
						MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, MJBotLoadManager.MBO_WANDER_MAT_MAPID);
				teleport(loc.x, loc.y, (short) loc.map);
				return;
			}

			if (_body.getCurrentHp() != _body.getMaxHp()) {
				healingPotion(true, time);
				return;
			}

			poly();
			MJBotLocation loc = MJBotLocationLoader.getInstance().get();
			teleport(_brn.toRand(3) + loc.x, _brn.toRand(3) + loc.y, (short) loc.map);
			setStatus(MJBotStatus.WALK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void settingDefense(long time) {
		try {
			_target = null;
			_targetQ.clear();

			if (!MJBotUtil.isInTown(_body)) {
				_body.getMap().setPassable(_body.getLocation(), true);
				MJBotLocation loc = MJBotUtil.createRandomLocation(MJBotLoadManager.MBO_WANDER_MAT_LEFT,
						MJBotLoadManager.MBO_WANDER_MAT_TOP, MJBotLoadManager.MBO_WANDER_MAT_RIGHT,
						MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, MJBotLoadManager.MBO_WANDER_MAT_MAPID);
				teleport(loc.x, loc.y, (short) loc.map);
				return;
			}

			if (!MJCastleWarBusiness.getInstance().isNowWar(getWarCastle())) {
				setWarCastle(-1);
				setWar(null);
				return;
			}

			if (_body.getCurrentHp() != _body.getMaxHp()) {
				healingPotion(true, time);
				return;
			}

			poly();
			MJBotLocation loc = MJBotUtil.createDefenseLocation(getWarCastle());
			if (loc == null)
				return;

			teleport(loc.x, loc.y, (short) loc.map);
			setStatus(MJBotStatus.WALK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void settingOffense(long time) {
		try {
			_target = null;
			_targetQ.clear();

			if (!MJBotUtil.isInTown(_body)) {
				_body.getMap().setPassable(_body.getLocation(), true);
				MJBotLocation loc = MJBotUtil.createRandomLocation(MJBotLoadManager.MBO_WANDER_MAT_LEFT,
						MJBotLoadManager.MBO_WANDER_MAT_TOP, MJBotLoadManager.MBO_WANDER_MAT_RIGHT,
						MJBotLoadManager.MBO_WANDER_MAT_BOTTOM, MJBotLoadManager.MBO_WANDER_MAT_MAPID);
				teleport(loc.x, loc.y, (short) loc.map);
				return;
			}

			if (!MJCastleWarBusiness.getInstance().isNowWar(getWarCastle())) {
				setWarCastle(-1);
				setWar(null);
				return;
			}

			if (_body.getCurrentHp() != _body.getMaxHp()) {
				healingPotion(true, time);
				return;
			}

			poly();
			MJBotLocation loc = MJBotUtil.createCastleLocation(getWarCastle());
			if (loc == null)
				return;

			teleport(loc.x, loc.y, (short) loc.map);
			setStatus(MJBotStatus.WALK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void walk(long time) {
		try {
			if (isMovePause()) {
				teleport();
				return;
			}
			if (!_body.getMap().isSafetyZone(_body.getLocation())) {
				int dir = MJCommons.checkPassable(_body.getX(), _body.getY(), _body.getMapId(), _body.getHeading());
				if (dir != -1) {
					move(dir);
				}
			}
			searchTarget();
			if (_gItem != null)
				setStatus(MJBotStatus.PICKUP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MJBotLocation _defenseLoc;

	@Override
	public void walkDefense(long time) {
		try {
			if (!MJBotUtil.isInCastle(_body, getWarCastle()) && !MJBotUtil.isInCastleStartup(_body, getWarCastle())) {
				setStatus(MJBotStatus.SETTING);
				return;
			}

			MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(_body.getClanname());
			if (cInfo == null || cInfo.leaderAI == null)
				return;

			if (_towerObject != null) {
				if (_defenseLoc == null) {
					int h = _brn.toRand(8);
					int tmp = 0;
					int tmp2 = 0;
					int tx = 0;
					int ty = 0;
					switch (_brn.getClassType()) {
					case 2:
					case 3:
						tmp = _brn.toRand(1);
						tmp2 = _brn.toBoolean() ? 1 : -1;
						tx = _towerObject.getX() + ((HEADING_TABLE_X[h] * tmp) * tmp2);
						ty = _towerObject.getY() + ((HEADING_TABLE_Y[h] * tmp) * tmp2);
						break;
					default:
						tmp = _brn.toRand(1);
						tmp2 = _brn.toBoolean() ? 1 : -1;
						tx = _towerObject.getX() + (HEADING_TABLE_X[h] * tmp + tmp2);
						ty = _towerObject.getY() + (HEADING_TABLE_Y[h] * tmp + tmp2);
						break;
					}
					_defenseLoc = new MJBotLocation(tx, ty, _body.getMapId());
				}

				int h = MJCommons.calcheading(_body.getX(), _body.getY(), _defenseLoc.x, _defenseLoc.y);
				if (!moveWide(h)) {
					if (_brn.toBoolean()) {
						if (_brn.getClassType() != 2 && _brn.getClassType() != 3 && isPickupPause()) {
							setStatus(MJBotStatus.ATTACK);
							_defenseLoc = null;
							_towerObject = null;
							return;
						}

						h = MJCommons.getReverseHeading(h);
						if (!moveWide(h)) {
							_defenseLoc = null;
							return;
						}
					} else
						_defenseLoc = null;
					return;
				}

				int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _defenseLoc.x, _defenseLoc.y);
				switch (_brn.getClassType()) {
				case 2:
				case 3:
					if (dir <= 2 && !MJBotUtil.isInCastleDefense(_body, getWarCastle()) && isMovePause()) {
						setStatus(MJBotStatus.ATTACK);
						_towerObject = null;
						_defenseLoc = null;
					}
					break;
				default:
					if (dir <= 4 && !MJBotUtil.isInCastleDefense(_body, getWarCastle()) && isMovePause()) {
						setStatus(MJBotStatus.ATTACK);
						_towerObject = null;
						_defenseLoc = null;
					}
					break;
				}
				// }
				return;
			}

			if (_brn.getClassType() != 2 && _brn.getClassType() != 3 && _initPoint++ < 3)
				return;
			_initPoint = 0;

			int cid = getWarCastle();
			_towerObject = null;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_body.getMapId()).values()) {
				if (obj == null)
					continue;

				if (!MJBotUtil.isInCastle(obj, cid))
					continue;

				if (obj instanceof L1TowerInstance || obj instanceof L1CrownInstance) {
					_towerObject = (L1Character) obj;
					break;
				}
			}

			if (_towerObject == null) {
				setStatus(MJBotStatus.SETTING);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void walkOffense(long time) {
		try {
			if (!MJBotUtil.isInCastle(_body, getWarCastle()) && !MJBotUtil.isInCastleStartup(_body, getWarCastle())) {
				setStatus(MJBotStatus.SETTING);
				return;
			}

			int lvl = MJBotUtil.getCastleWayLevel(_body, getWarCastle());
			if (_loc == null || lvl != _wayLevel
					|| MJCommons.getDistance(_body.getX(), _body.getY(), _loc.x, _loc.y) <= 1) {
				_wayLevel = lvl;
				if (_brn.getClassType() == 3 || _brn.getClassType() == 2) {
					_loc = MJBotUtil.getCastleWayCenterPoint(getWarCastle(), _wayLevel);
				} else
					_loc = MJBotUtil.getCastleWayPoint(getWarCastle(), _wayLevel);
				_target = null;
				_targetQ.clear();
				findPath(1, _loc);
			}

			if (_deQ.isEmpty()) {
				_ast.release();
				findPath(1, _loc);
			}
			moveAstar();
			if (_astFailCount > 10) {
				_astFailCount = 0;
				_deQ.clear();
				_ast.release();
				return;
			}

			if (_wayLevel == 0 && _brn.toRand(100) < 70)
				return;

			if (isMovePauseOffense(_wayLevel)) {
				setStatus(MJBotStatus.ATTACK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void searchWalk(long time) {
		try {
			if (isMovePause()) {
				setStatus(MJBotStatus.WALK);
				teleport();
				return;
			}

			if (move()) {
			}

			searchTarget();
			if (_gItem != null)
				setStatus(MJBotStatus.PICKUP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void searchWalkDefense(long time) {
		try {
			if (!MJBotUtil.isInCastle(_body, getWarCastle()) && !MJBotUtil.isInCastleStartup(_body, getWarCastle())) {
				setStatus(MJBotStatus.SETTING);
				return;
			}

			MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(_body.getClanname());
			if (cInfo == null || cInfo.leaderAI == null)
				return;

			if (_towerObject != null) {
				if (_towerObject.getX() == cInfo.leaderAI.getBody().getX()
						&& _towerObject.getY() == cInfo.leaderAI.getBody().getY()) {
					int h = MJCommons.calcheading(_body.getX(), _body.getY(), _towerObject.getX(), _towerObject.getY());
					if (!moveWide(h)) {
						if (isMovePause()) {
							setStatus(MJBotStatus.ATTACK);
							return;
						}
						if (_brn.toRand(100) < 30) {
							h = MJCommons.getReverseHeading(h);
							if (!moveWide(h))
								return;
						} else
							return;
					}

					int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _towerObject.getX(),
							_towerObject.getY());
					if (_body.isWizard() || _body.isElf()) {
						if (dir <= 1) {
							_towerObject = null;
							setStatus(MJBotStatus.ATTACK);
						}
					} else {
						if (dir <= 1) {
							_towerObject = null;
							setStatus(MJBotStatus.ATTACK);
						}
					}
				}
				return;
			}

			int cid = getWarCastle();
			_towerObject = null;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_body.getMapId()).values()) {
				if (obj == null)
					continue;

				if (!MJBotUtil.isInCastle(obj, cid))
					continue;

				if (obj instanceof L1TowerInstance || obj instanceof L1CrownInstance) {
					_towerObject = (L1Character) obj;
					break;
				}
			}

			if (_towerObject == null) {
				setStatus(MJBotStatus.SETTING);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void searchWalkOffense(long time) {
		try {
			if (!MJBotUtil.isInCastle(_body, getWarCastle()) && !MJBotUtil.isInCastleStartup(_body, getWarCastle())) {
				setStatus(MJBotStatus.SETTING);
				return;
			}

			int lvl = MJBotUtil.getCastleWayLevel(_body, getWarCastle());
			if (_loc == null || lvl != _wayLevel) {
				_wayLevel = lvl;
				_loc = MJBotUtil.getCastleWayPoint(getWarCastle(), _wayLevel);
			}
			int h = MJCommons.calcheading(_body.getX(), _body.getY(), _loc.x, _loc.y);
			if (MJCommons.isVisibleObjects(_body, h))
				h = MJCommons.checkAroundPassable(_body, h);

			move(h);
			if (isMovePauseOffense(_wayLevel))
				setStatus(MJBotStatus.ATTACK);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void attack(long time) {
		try {
			_lastStatus = MJBotStatus.ATTACK;
			if (_target != null && _target.isDead()) {
				if (_target instanceof L1PcInstance) {
					MJBotUtil.sendBotKillMent(this, _target);
					_target = null;
				} else {
					_body.addLawful(_target.getLevel());
					_target = null;
				}
			}

			if (_brn.toRand(100) < _brn.getAge()) {
				if (isPickup() && !isPickupPause()) {
					setStatus(MJBotStatus.PICKUP);
					_targetQ.clear();
					return;
				}
			}

			_target = selectObject();
			if (!isAttack(_target, true)) {

				int h = MJCommons.checkPassable(_body.getX(), _body.getY(), _body.getMapId());
				if (h != -1) {
					move(h);
					_lastStatus = MJBotStatus.WALK;
				}
				_target = null;
				return;
			}

			if (_target instanceof L1PcInstance)
				MJBotUtil.sendBotOnTargetMent(this, _target);

			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());
			if (!_body.glanceCheck(_target.getX(), _target.getY()))
				teleport();
			else if (dir > _body.getAttackRang()) {
				move(_target, _target.getX(), _target.getY(), 0);
				_lastStatus = MJBotStatus.WALK;
			} else {
				if (!_target.hasSkillEffect(L1SkillId.EARTH_BIND) && !_target.hasSkillEffect(L1SkillId.ICE_LANCE))
					if (magic(_target, dir, time))
						return;
				_searchPending = 0;
				attack(_target, 0, 0, _body.isElf(), 1, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void attackDefense(long time) {
		try {
			_lastStatus = MJBotStatus.ATTACK;
			if (isEmptyTarget()) {
				searchTargetDefense();
				if (_brn.getClassType() != 2 && _brn.getClassType() != 3 && isEmptyTarget() && _brn.toRand(100) < 5)
					setStatus(MJBotStatus.WALK);
				return;
			}

			_target = selectObject();
			if (isEmptyTarget()) {
				setStatus(MJBotStatus.WALK);
				return;
			}

			if (!MJBotUtil.isInCastle(_target, getWarCastle())) {
				_target = null;
				return;
			}

			if (!isAttack(_target, false)) {
				_target = null;
				return;
			}

			if (_brn.getClassType() == 3) {
				if (_magicDelay > time)
					return;

				if (immune(_body, time))
					return;

				if (!immune(_target, time)) {
					if (_targetQ.size() > 2 && _brn.toRand(100) < 10)
						healAll(time);
					else {
						heal(_target, time);

					}
					if (_target.getCurrentHp() == _target.getMaxHp())
						_target = null;
				} else
					_target = null;
			} else {
				int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());
				if (dir > _body.getAttackRang()) {
					_target = null;
					return;
				}
				if (!_target.hasSkillEffect(L1SkillId.EARTH_BIND) && !_target.hasSkillEffect(L1SkillId.ICE_LANCE))
					if (magic(_target, dir, time))
						return;
				attack(_target, 0, 0, _body.isElf(), 1, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void attackOffense(long time) {
		try {
			_lastStatus = MJBotStatus.ATTACK;
			if (isEmptyTarget()) {
				searchTargetOffense();
				_lastSearchTime = time;
				if (isEmptyTarget()) {
					setStatus(MJBotStatus.WALK);
					_loc = null;
					return;
				}
			}

			_target = selectObjectOffense();
			if (_target == null) {
				setStatus(MJBotStatus.WALK);
				_targetQ.clear();
				_loc = null;
				return;
			}

			int dir = MJCommons.getDistance(_body.getX(), _body.getY(), _target.getX(), _target.getY());
			if (_brn.getClassType() != 2 && _brn.getClassType() != 3 && dir > 3) {
				int h = MJCommons.calcheading(_body.getX(), _body.getY(), _loc.x, _loc.y);
				if (MJCommons.isReverseHeading(_body.getX(), _body.getY(), _target.getX(), _target.getY(), h)) {
					_loc = null;
					_target = null;
					_targetQ.clear();
					setStatus(MJBotStatus.WALK);
					return;
				}
			}

			if (_brn.getClassType() == 3) {
				if (_magicDelay > time || immune(_body, time))
					return;

				if (dir > 10 || !isAttack(_target, false)) {
					int h = 0;
					if (isPickupPause() || (_body.getCurrentHp() != _body.getMaxHp() && _brn.toBoolean()))
						h = MJCommons.getReverseHeading(h);
					else
						h = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
					if (!moveWide(h)) {
						_target = null;
						if (time - _lastSearchTime > 5000) {
							_targetQ.clear();
							_loc = null;
							setStatus(MJBotStatus.WALK);
						}
					}
					_lastStatus = MJBotStatus.WALK;
				} else {
					if (!immune(_target, time)) {
						if (_targetQ.size() > 2 && _brn.toRand(100) < 10)
							healAll(time);
						else {
							heal(_target, time);
						}
						if (_target.getCurrentHp() >= _target.getMaxHp())
							_target = null;
					} else
						_target = null;
				}
			} else {
				int rang = _body.getAttackRang() > 10 ? 10 : _body.getAttackRang();
				if (dir > rang || !isAttack(_target, false)) {
					if (isPickupPause()) {
						_loc = null;
						_target = null;
						_targetQ.clear();
						setStatus(MJBotStatus.WALK);
						return;
					}

					int h = MJCommons.calcheading(_body.getX(), _body.getY(), _target.getX(), _target.getY());
					if (!moveWide(h)) {
						if (time - _lastSearchTime > 5000) {
							if (_brn.toRand(100) < 30) {
								h = MJCommons.getReverseHeading(h);
								if (moveWide(h)) {
									_lastStatus = MJBotStatus.WALK;
									_target = null;
									return;
								}
							}
							_targetQ.clear();
							_loc = null;
							setStatus(MJBotStatus.WALK);
						}
						_target = null;
					}
					_lastStatus = MJBotStatus.WALK;
					return;
				} else {
					_pickupPending = 0;

					if (!magic(_target, dir, time))
						attack(_target, 0, 0, _body.isElf(), 1, 0);
					if (_target == null || _target.isDead() || _target.getMapId() != _body.getMapId() || _target.isInvisble()) {
						_target = null;
						if (time - _lastSearchTime > 5000)
							_targetQ.clear();
						_loc = null;
						setStatus(MJBotStatus.WALK);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean searchTargetDefense() {
		try {
			_targetQ.clear();
			_target = null;
			_body.updateObject();
			ArrayDeque<L1Object> objQ = MJCommons.createKnownQ(_body);
			L1Object obj = null;
			int cid = getWarCastle();
			if (_brn.getClassType() == 3) {
				while (!objQ.isEmpty()) {
					obj = objQ.poll();
					if (obj == null)
						continue;

					if (!MJBotUtil.isInCastle(obj, cid))
						continue;

					int dir = MJCommons.getDistance(_body.getX(), _body.getY(), obj.getX(), obj.getY());
					if (dir > 10)
						continue;

					if (!(obj instanceof L1PcInstance))
						continue;

					L1PcInstance pc = (L1PcInstance) obj;
					if (pc.isDead() || pc.isElf() || pc.getClanid() != _body.getClanid())
						continue;

					if (pc.getCurrentHp() == pc.getMaxHp())
						continue;

					_targetQ.offer(pc);
				}
			} else {
				while (!objQ.isEmpty()) {
					obj = objQ.poll();
					if (obj == null)
						continue;

					if (!(obj instanceof L1Character))
						continue;

					if (!MJBotUtil.isInCastle(obj, cid))
						continue;

					L1Character c = (L1Character) obj;
					if (c.isDead())
						continue;

					int dir = MJCommons.getDistance(_body.getX(), _body.getY(), c.getX(), c.getY());
					if (dir > _body.getAttackRang())
						continue;

					if (c instanceof L1CastleGuardInstance) {
						L1CastleGuardInstance cg = (L1CastleGuardInstance) c;
						if (cg.isDead())
							continue;

						_targetQ.offer(c);
					} else if (c instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) c;
						if (pc.getClanid() == _body.getClanid())
							continue;

						_targetQ.offer(c);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean searchTargetOffense() {
		try {
			_targetQ.clear();
			_target = null;
			_body.updateObject();

			ArrayDeque<L1Object> objQ = MJCommons.createKnownQ(_body);
			L1Object obj = null;
			int cid = getWarCastle();

			if (_brn.getClassType() == 3) {
				while (!objQ.isEmpty()) {
					obj = objQ.poll();
					if (obj == null)
						continue;

					if (!(obj instanceof L1PcInstance) || !MJBotUtil.isInCastle(obj, cid))
						continue;

					L1PcInstance pc = (L1PcInstance) obj;
					if (pc.getCurrentHp() >= pc.getMaxHp())
						continue;

					int dir = MJCommons.getDistance(_body.getX(), _body.getY(), obj.getX(), obj.getY());
					if (dir > 10)
						continue;

					if (!_body.glanceCheck(obj.getX(), obj.getY()))
						continue;

					if (pc.isDead() || pc.getClanid() != _body.getClanid() || pc.getType() == 3)
						continue;

					_targetQ.offer(pc);
				}
			} else {
				_targetQ.addAll(MJBotUtil.findCastleDoors(cid));
				int h = MJCommons.calcheading(_body.getX(), _body.getY(), _loc.x, _loc.y);
				while (!objQ.isEmpty()) {
					obj = objQ.poll();
					if (obj == null)
						continue;

					if (!MJBotUtil.isInCastle(obj, cid))
						continue;

					if (obj.getMap().isSafetyZone(obj.getLocation()))
						continue;

					if (MJCommons.isReverseHeading(_body.getX(), _body.getY(), obj.getX(), obj.getY(), h))
						continue;

					if (obj instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) obj;
						if (pc.isDead() || _body.getClanid() == pc.getClanid()
								|| pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
							continue;
						_targetQ.offer(pc);
					} else if (obj instanceof L1TowerInstance || obj instanceof L1CastleGuardInstance
							|| obj instanceof L1MonsterInstance) {
						L1Character c = (L1Character) obj;
						if (c.isDead())
							continue;
						_targetQ.offer(c);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void shopMove() {

	}

	protected boolean isAttack(L1Character c, boolean isWalk) {
		try {
			if (c == null || c.getMapId() != _body.getMapId() || c.hasSkillEffect(L1SkillId.EARTH_BIND)
					|| c.hasSkillEffect(L1SkillId.ICE_LANCE))
				return false;

			if (c.isDead())
				return false;

			if (c.getMap().isSafetyZone(c.getLocation()) || c.isInvisble())
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void pickupDefense() {
		pickUp();
	}

	@Override
	public void pickupOffense() {
		pickUp();
	}

	protected L1Character selectObjectOffense() {
		try {
			if (_target != null && _target.getMapId() == _body.getMapId() && !_target.isDead()
					&& _body.glanceCheck(_target.getX(), _target.getY())
					&& MJBotUtil.isInCastle(_target, getWarCastle()))
				return _target;

			_targetQ.comparator();
			while (!_targetQ.isEmpty()) {
				L1Character tmp = _targetQ.poll();
				if(tmp == null)
					continue;
				
				if (tmp.isInvisble() || tmp.isDead() || tmp.getMapId() != _body.getMapId())
					continue;

				if (!_body.glanceCheck(tmp.getX(), tmp.getY()) || !MJBotUtil.isInCastle(tmp, getWarCastle()))
					continue;

				return tmp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
