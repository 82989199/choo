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

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Quest;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.templates.L1Npc;

public class L1QuestInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	public L1QuestInstance(L1Npc template) {
		super(template);
		_restCallCount = new AtomicInteger(0);
	}

	@Override
	public void onNpcAI() {
		int npcId = getNpcTemplate().get_npcId();
		if (isAiRunning()) {
			return;
		}
		if (npcId == 71075 || npcId == 70957 || npcId == 81209) {
			return;
		} else {
			setActived(false);
			startAI();
		}
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		if (attack.calcHit()) {
			attack.calcDamage();
			attack.addPcPoisonAttack(pc, this);
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int pcX = pc.getX();
		int pcY = pc.getY();
		int npcX = getX();
		int npcY = getY();

		if (pcX == npcX && pcY < npcY) {
			setHeading(0);
		} else if (pcX > npcX && pcY < npcY) {
			setHeading(1);
		} else if (pcX > npcX && pcY == npcY) {
			setHeading(2);
		} else if (pcX > npcX && pcY > npcY) {
			setHeading(3);
		} else if (pcX == npcX && pcY > npcY) {
			setHeading(4);
		} else if (pcX < npcX && pcY > npcY) {
			setHeading(5);
		} else if (pcX < npcX && pcY == npcY) {
			setHeading(6);
		} else if (pcX < npcX && pcY < npcY) {
			setHeading(7);
		}
		broadcastPacket(new S_ChangeHeading(this));

		int npcId = getNpcTemplate().get_npcId();
		if (npcId == 71092 || npcId == 71093) {
			if (pc.isKnight() && pc.getQuest().get_step(3) == 4) {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "searcherk1"));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "searcherk4"));
			}
		} else if (npcId == 71094) {
			if (pc.isDarkelf() && pc.getQuest().get_step(4) == 1) {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "endiaq1"));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "endiaq4"));
			}
		} else if (npcId == 71062) {
			if (pc.getQuest().get_step(L1Quest.QUEST_CADMUS) == 2) {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "kamit1b"));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "kamit1"));
			}
		} else if (npcId == 71075) {
			if (pc.getQuest().get_step(L1Quest.QUEST_LIZARD) == 1) {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "llizard1b"));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "llizard1a"));
			}
		} else if (npcId == 70957 || npcId == 81209) {
			if (pc.getQuest().get_step(L1Quest.QUEST_ROI) != 1) {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "roi1"));
			} else {
				pc.sendPackets(new S_NPCTalkReturn(getId(), "roi2"));
			}
		}

		// 얘 좀 구린듯. interlockedIncrement하는데 compare&Swap이라니. 어차피 intel 기준 lock
		// xadd 한번만 콜해주면 되는데.-_-
		if (_restCallCount.getAndIncrement() == 0) {
			setRest(true);
		}

		GeneralThreadPool.getInstance().schedule(new RestMonitor(), REST_MILLISEC);
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String action) {
		if (action.equalsIgnoreCase("start")) {
			int npcId = getNpcTemplate().get_npcId();
			L1FollowerInstance follow = null;
			if ((npcId == 71092 || npcId == 71093) && pc.isKnight() && pc.getQuest().get_step(3) == 4) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71093);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
			} else if (npcId == 71094 && pc.isDarkelf() && pc.getQuest().get_step(4) == 1) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71094);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
			} else if (npcId == 71062 && pc.getQuest().get_step(L1Quest.QUEST_CADMUS) == 2) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71062);

				if (pc.getFollowerList().size() < 1) {
					follow = new L1FollowerInstance(l1npc, this, pc);
				}

				pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
			} else if (npcId == 71075 && pc.getQuest().get_step(L1Quest.QUEST_LIZARD) == 1) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71075);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
			} else if (npcId == 70957 || npcId == 81209) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(70957);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
			}
		}
	}

	private static final long REST_MILLISEC = 10000;

	private AtomicInteger _restCallCount;

	public class RestMonitor implements Runnable {
		@Override
		public void run() {
			if (_restCallCount.decrementAndGet() == 0) {
				setRest(false);
			}
		}
	}
}
