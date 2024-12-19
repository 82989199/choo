package l1j.server.server.model.Instance;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.templates.L1Npc;

public class L1PeopleInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private AtomicInteger _restCallCount;
	/**
	 * @param template
	 */
	public L1PeopleInstance(L1Npc template) {
		super(template);
		_restCallCount = new AtomicInteger(0);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if(pc == null || getCurrentHp() <= 0 && isDead())
			return;
		
		L1Attack attack = new L1Attack(pc, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
		attack = null;
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		setActived(false);
		startAI();
	}

	@SuppressWarnings("unused")
	@Override
	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		L1Quest quest = player.getQuest();
		String htmlid = null;
		String[] htmldata = null;

		int pcX = player.getX();
		int pcY = player.getY();
		int npcX = getX();
		int npcY = getY();

		long curtime = System.currentTimeMillis() / 1000;
		if (player.getNpcActionTime() + 2 > curtime) {
			return;
		}
		player.setNpcActionTime(curtime);

		if (getNpcTemplate().getChangeHead()) {
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

			// 얘 좀 구린듯. interlockedIncrement하는데 compare&Swap이라니. 어차피 intel 기준
			// lock xadd 한번만 콜해주면 되는데.-_-
			if (_restCallCount.getAndIncrement() == 0) {
				setRest(true);
			}

			GeneralThreadPool.getInstance().schedule(new RestMonitor(), REST_MILLISEC);
		}

		L1SkillUse l1skilluse = new L1SkillUse();
		L1Object obj = L1World.getInstance().findObject(objid);
		String npcName = ((L1NpcInstance) obj).getNpcTemplate().get_name();

		if (talking != null) {
			switch (npcid) {
			default:
				htmlid = null;
				htmldata = null;
				break;
			}
			// html 표시 패킷 송신
			if (htmlid != null) { // htmlid가 지정되고 있는 경우
				if (htmldata != null) { // html 지정이 있는 경우는 표시
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
				} else {
					player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
				}
			} else {
				if (player.getLawful() < -1000) { // 플레이어가 카오틱
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
				} else {
					player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
				}
			}
		}
	}

	private static final long REST_MILLISEC = 10000;

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getCurrentHp() > 0 && !isDead()) {
			if (damage > 0) {
				if (hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
					removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				} else if (hasSkillEffect(L1SkillId.PHANTASM)) {
					removeSkillEffect(L1SkillId.PHANTASM);
				} else if (hasSkillEffect(L1SkillId.DARK_BLIND)) {
					removeSkillEffect(L1SkillId.DARK_BLIND);
				}
			}

			// onNpcAI();

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
		} else if (getCurrentHp() == 0 && !isDead()) {
		} else if (!isDead()) {
			setDead(true);
			setStatus(ActionCodes.ACTION_Die);
			Death death = new Death(attacker);
			GeneralThreadPool.getInstance().execute(death);
		}
	}

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			try {
				setDeathProcessing(true);
				setCurrentHp(0);
				setDead(true);
				setStatus(ActionCodes.ACTION_Die);
				getMap().setPassable(getLocation(), true);
				Broadcaster.broadcastPacket(L1PeopleInstance.this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
				startChat(CHAT_TIMING_DEAD);

				int lawful = 5000;
				if (lawful > 0) {
					_lastAttacker.addLawful(-lawful);
				}
				setDeathProcessing(false);
				allTargetClear();
				startDeleteTimer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class RestMonitor implements Runnable {
		@Override
		public void run() {
			if (_restCallCount.decrementAndGet() == 0) {
				setRest(false);
			}
		}

	}
}
