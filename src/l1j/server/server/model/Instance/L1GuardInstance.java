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
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;

public class L1GuardInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void searchTarget() {
		L1Character targetPlayer = null;

		for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
			
//			if (chr.getCurrentHp() <= 0 || chr.isDead() ) {
//				continue;
//			}
			
//			if(chr  instanceof L1PcInstance){
//				L1PcInstance pc = (L1PcInstance) chr;
//				if(pc.isGm() || pc.isGhost()) {
//					continue;
//				}
//			}
			//System.out.println(getMapId());
			if(obj instanceof L1MonsterInstance){
				L1MonsterInstance mon = (L1MonsterInstance)obj;
			
				if(getMapId() == 1708){
					if(mon.getCurrentHp() > 0 || !mon.isDead()){
						targetPlayer = mon;
						break;
					}
				}
			}
		//////// 경비병 인식으로 인해서 주석 ///////////////
//			if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) {
//				if (pc.isWanted()) {
//					targetPlayer = pc;
//					break;
//				}
//			}
		//////// 경비병 인식으로 인해서 주석 ///////////////
		}

		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}

	public void setTarget(L1PcInstance targetPlayer) {
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}

	@Override
	public boolean noTarget() {
		if (getLocation().getTileLineDistance(new Point(getHomeX(), getHomeY())) > 0) {
			int dir = moveDirection(getHomeX(), getHomeY());
			if (dir != -1) {
				setSleepTime(setDirectionMove(dir));
			} else {
				teleport(getHomeX(), getHomeY(), 1);
			}
		} else {
			if (L1World.getInstance().getRecognizePlayer(this).size() == 0) {
				return true;
			}
		}
		return false;
	}

	public L1GuardInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		setActived(false);
		startAI();
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (pc == null)
			return;
		if (!isDead()) {
			if (getCurrentHp() > 0) {
				L1Attack attack = new L1Attack(pc, this);
				if (attack.calcHit()) {
					attack.calcDamage();
					attack.addPcPoisonAttack(pc, this);
				}
				attack.action();
				attack.commit();
			} else {
				L1Attack attack = new L1Attack(pc, this);
				attack.calcHit();
				attack.action();
			}
		}
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		String htmlid = null;
		String[] htmldata = null;
		boolean hascastle = false;
		String clan_name = "";
		String pri_name = "";

		if (talking != null) {
			if (npcid == 70549 || npcid == 70985) {
				hascastle = checkHasCastle(player, L1CastleLocation.KENT_CASTLE_ID);
				if (hascastle) {
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70656) {
				hascastle = checkHasCastle(player, L1CastleLocation.KENT_CASTLE_ID);
				if (hascastle) {
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70600 || npcid == 70986) {
				hascastle = checkHasCastle(player, L1CastleLocation.OT_CASTLE_ID);
				if (hascastle) {
					htmlid = "orckeeper";
				} else {
					htmlid = "orckeeperop";
				}
			} else if (npcid == 70687 || npcid == 70987) {
				hascastle = checkHasCastle(player, L1CastleLocation.WW_CASTLE_ID);
				if (hascastle) {
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70778) {
				hascastle = checkHasCastle(player, L1CastleLocation.WW_CASTLE_ID);
				if (hascastle) {
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70800 || npcid == 70988 || npcid == 70989 || npcid == 70990 || npcid == 70991) {
				hascastle = checkHasCastle(player, L1CastleLocation.GIRAN_CASTLE_ID);
				if (hascastle) {
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70817) {
				hascastle = checkHasCastle(player, L1CastleLocation.GIRAN_CASTLE_ID);
				if (hascastle) {
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70862 || npcid == 70992) {
				hascastle = checkHasCastle(player, L1CastleLocation.HEINE_CASTLE_ID);
				if (hascastle) {
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70863) {
				hascastle = checkHasCastle(player, L1CastleLocation.HEINE_CASTLE_ID);
				if (hascastle) {
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70993 || npcid == 70994) {
				hascastle = checkHasCastle(player, L1CastleLocation.DOWA_CASTLE_ID);
				if (hascastle) {
					htmlid = "gateokeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70995) {
				hascastle = checkHasCastle(player, L1CastleLocation.DOWA_CASTLE_ID);
				if (hascastle) {
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			} else if (npcid == 70996) {
				hascastle = checkHasCastle(player, L1CastleLocation.ADEN_CASTLE_ID);
				if (hascastle) {
					htmlid = "gatekeeper";
					htmldata = new String[] { player.getName() };
				} else {
					htmlid = "gatekeeperop";
				}
			}

			else if (npcid == 60514) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.KENT_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "ktguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60560) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.OT_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "orcguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60552 || npcid == 5155) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.WW_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "wdguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60524 || npcid == 60525 || npcid == 60529 || npcid == 7320232) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.GIRAN_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "grguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 70857) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.HEINE_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "heguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60530 || npcid == 60531) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.DOWA_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "dcguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 60533 || npcid == 60534) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.ADEN_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "adguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			} else if (npcid == 81156) {
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getCastleId() == L1CastleLocation.DIAD_CASTLE_ID) {
						clan_name = clan.getClanName();
						pri_name = clan.getLeaderName();
						break;
					}
				}
				htmlid = "ktguard6";
				htmldata = new String[] { getName(), clan_name, pri_name };
			}

			if (htmlid != null) {
				if (htmldata != null) {
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
				} else {
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
				}
			} else {
				if (player.getLawful() < -1000) {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		}
	}

	public void onFinalAction() {

	}

	public void doFinalAction() {

	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (attacker == null)
			return;
		if (getCurrentHp() > 0 && !isDead()) {
			if (damage >= 0) {
				if (!(attacker instanceof L1EffectInstance)) {
					setHate(attacker, damage);
				}
			}
			if (damage > 0) {
				if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
					removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
					removeSkillEffect(L1SkillId.PHANTASM);
				}
			}

			onNpcAI();

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance pc = (L1PcInstance) attacker;
				pc.set_pet_target(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				setCurrentHp(0);
				setDead(true);
				setStatus(ActionCodes.ACTION_Die);
				Death death = new Death(attacker);
				GeneralThreadPool.getInstance().execute(death);
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) {
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			Death death = new Death(attacker);
			GeneralThreadPool.getInstance().execute(death);
		}
	}

	@Override
	public void checkTarget() {
		// System.out.println("오버라이드 했는데 여기 진입할까?");
		if (_target == null || (Math.abs(this.getX() - this.getHomeX())) > 20
				|| (Math.abs(this.getY() - this.getHomeY())) > 20 || _target.getMapId() != getMapId()
				|| _target.getCurrentHp() <= 0 || _target.isDead()
				|| (_target.isInvisble() && !getNpcTemplate().is_agrocoi() && !_hateList.containsKey(_target))
				|| (_target instanceof L1SummonInstance && ((L1SummonInstance) _target).isDestroyed())
				|| (_target instanceof L1PetInstance && ((L1PetInstance) _target).isDestroyed())
				|| (_target instanceof MJCompanionInstance && ((MJCompanionInstance) _target).isDestroyed())) {
			
			if (_target != null) {
				tagertClear();

			}
			if (!_hateList.isEmpty()) {
				_target = _hateList.getMaxHateCharacter();
				checkTarget();
			}

		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);

		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();

		}
	}

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			setDeathProcessing(true);
			setCurrentHp(0);
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);

			getMap().setPassable(getLocation(), true);

			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));

			startChat(CHAT_TIMING_DEAD);

			setDeathProcessing(false);

			allTargetClear();

			startDeleteTimer();
		}
	}

	private boolean checkHasCastle(L1PcInstance pc, int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		if (!isExistDefenseClan) {
			return true;
		}

		if (pc.getClanid() != 0) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
			if (clan != null) {
				if (clan.getCastleId() == castleId) {
					return true;
				}
			}
		}
		return false;
	}

}
