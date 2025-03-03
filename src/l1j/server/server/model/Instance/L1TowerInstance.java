/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model.Instance;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.templates.L1Npc;

public class L1TowerInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1TowerInstance(L1Npc template) {
		super(template);
	}

	protected L1Character 	_lastattacker;
	private int 			_castle_id;
	protected int 			_crackStatus;

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null)
			return;
		perceivedFrom.addKnownObject(this);
		if(perceivedFrom.getAI() == null)
			perceivedFrom.sendPackets(S_WorldPutObject.get(this));
	}

	@Override
	public void onAction(L1PcInstance player) {
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(player, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(player, this);
			}
			attack.action();
			attack.commit();
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getNpcId() == 65007) { // 클라우디아 수호탑
			if (getCurrentHp() > 0 && !isDead()) {
				int newHp = getCurrentHp() - damage;
				if (newHp <= 0 && !isDead()) {
					setCurrentHp(0);
					setDead(true);
					setStatus(36);
					_lastattacker = attacker;
					_crackStatus = 0;
				}

				if (newHp > 0) {
					setCurrentHp(newHp);
					// 몹스폰

					if ((getMaxHp() * 1 / 5) > getCurrentHp()) {
						if (_crackStatus != 4) {
							Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 35));
							setStatus(35);
							_crackStatus = 4;
						}
					} else if ((getMaxHp() * 2 / 5) > getCurrentHp()) {
						if (_crackStatus != 3) {
							Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 34));
							setStatus(34);
							_crackStatus = 3;
						}
					} else if ((getMaxHp() * 3 / 5) > getCurrentHp()) {
						if (_crackStatus != 2) {
							Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 33));
							setStatus(33);
							_crackStatus = 2;
						}
					} else if ((getMaxHp() * 4 / 5) > getCurrentHp()) {
						if (_crackStatus != 1) {
							Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 32));
							setStatus(32);
							_crackStatus = 1;
						}
					}
				}
			} else if (!isDead()) {
				setDead(true);
				setStatus(36);
				_lastattacker = attacker;
				// Death death = new Death();
				// GeneralThreadPool.getInstance().execute(death);
				// Death(attacker);
			}
		}
		if (_castle_id == 0) {
			if (isSubTower()) {
				_castle_id = L1CastleLocation.ADEN_CASTLE_ID;
			} else {
				_castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());
			}
		}
		if (_castle_id > 0 && MJCastleWarBusiness.getInstance().isNowWar(_castle_id)) {

			if (_castle_id == L1CastleLocation.ADEN_CASTLE_ID && !isSubTower()) {
				int subTowerDeadCount = 0;
				L1TowerInstance tower = null;
				for (L1Object l1object : L1World.getInstance().getObject()) {
					if (l1object instanceof L1TowerInstance) {
						tower = (L1TowerInstance) l1object;
						if (tower.isSubTower() && tower.isDead()) {
							subTowerDeadCount++;
							if (subTowerDeadCount == 4) {
								break;
							}
						}
					}
				}
				if (subTowerDeadCount < 3) {
					return;
				}
			}

			L1PcInstance pc = null;
			if (attacker instanceof L1PcInstance) {
				pc = (L1PcInstance) attacker;
			} else if(attacker instanceof MJCompanionInstance){
				pc = ((MJCompanionInstance) attacker).get_master();
			} else if (attacker instanceof L1PetInstance) {
				pc = (L1PcInstance) ((L1PetInstance) attacker).getMaster();
			} else if (attacker instanceof L1SummonInstance) {
				pc = (L1PcInstance) ((L1SummonInstance) attacker).getMaster();
			}
			if (pc == null) {
				return;
			}

			MJCastleWar war = MJCastleWarBusiness.getInstance().get(_castle_id);
			L1Clan defense = war.getDefenseClan();
			L1Clan offense = pc.getClan();
			
			if(defense == null){
				System.out.println(String.format("[공성타워]수성중인 클랜이 없습니다. 성 : %d", _castle_id));
				return;
			}
			
			if(!pc.isGm()){
				if(offense == null || war.getOffenseClan(offense.getClanId()) == null)
					return;
			}
			
			if (getCurrentHp() > 0 && !isDead()) {
				int newHp = getCurrentHp() - damage;
				if (newHp <= 0 && !isDead()) {
					setCurrentHp(0);
					setDead(true);
					setStatus(ActionCodes.ACTION_TowerDie);
					setStatus(ActionCodes.ACTION_TowerDie);
					_lastattacker = attacker;
					_crackStatus = 0;
					Death death = new Death();
					GeneralThreadPool.getInstance().execute(death);
				}
				if (newHp > 0) {
					setCurrentHp(newHp);
					if ((getMaxHp() * 1 / 4) > getCurrentHp()) {
						if (_crackStatus != 3) {
							broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack3));
							setStatus(ActionCodes.ACTION_TowerCrack3);
							setStatus(ActionCodes.ACTION_TowerCrack3);
							_crackStatus = 3;
						}
					} else if ((getMaxHp() * 2 / 4) > getCurrentHp()) {
						if (_crackStatus != 2) {
							broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack2));
							setStatus(ActionCodes.ACTION_TowerCrack2);
							setStatus(ActionCodes.ACTION_TowerCrack2);
							_crackStatus = 2;
						}
					} else if ((getMaxHp() * 3 / 4) > getCurrentHp()) {
						if (_crackStatus != 1) {
							broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack1));
							setStatus(ActionCodes.ACTION_TowerCrack1);
							setStatus(ActionCodes.ACTION_TowerCrack1);
							_crackStatus = 1;
						}
					}
				}
			} else if (!isDead()) {
				setDead(true);
				setStatus(ActionCodes.ACTION_TowerDie);
				setStatus(ActionCodes.ACTION_TowerDie);
				_lastattacker = attacker;
				Death death = new Death();
				GeneralThreadPool.getInstance().execute(death);
				// Death(attacker);
			}
		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
	}

	class Death implements Runnable {
		L1Character lastAttacker = _lastattacker;
		L1Object object = L1World.getInstance().findObject(getId());
		L1TowerInstance npc = (L1TowerInstance) object;

		@Override
		public void run() {
			setCurrentHp(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_TowerDie);
			setStatus(ActionCodes.ACTION_TowerDie);
			int targetobjid = npc.getId();

			npc.getMap().setPassable(npc.getLocation(), true);

			npc.broadcastPacket(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_TowerDie));

			if (!isSubTower()) {
				L1WarSpawn warspawn = new L1WarSpawn();
				warspawn.SpawnCrown(_castle_id);
			}
		}
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		removeAllKnownObjects();
	}
	
	public boolean isSubTower() {
		return (getNpcTemplate().get_npcId() == 81190
				|| getNpcTemplate().get_npcId() == 81191
				|| getNpcTemplate().get_npcId() == 81192
				|| getNpcTemplate().get_npcId() == 81193);
	}
}
