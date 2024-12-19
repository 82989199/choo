package l1j.server.server.Controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.MJBotSystem.MJBotBossNotifier;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.Loader.MJBotBossNotifierLoader;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.datatables.BossMonsterSpawnList;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Boss;

public class BossController implements Runnable {

	private static BossController instance;
	private static final ConcurrentHashMap<Integer, Integer> _bossesId = new ConcurrentHashMap<Integer, Integer>(256);

	public static BossController getInstance() {
		if (instance == null) {
			instance = new BossController();
			GeneralThreadPool.getInstance().execute(instance);
		}
		return instance;
	}

	private void spawn_check() {
		try {
			List<L1Boss> list = BossMonsterSpawnList.getList();
			if (list.size() > 0) {
				long time = System.currentTimeMillis();
				Date date = new Date(time);
				int hour = date.getHours();
				int min = date.getMinutes();
				for (L1Boss b : list) {
					try {
							// 객체 생성.
							if (b.isSpawnTime(hour, min, time)) {
								Integer id = _bossesId.get(b.getNpcId());
								if (id != null) {
									L1Object obj = L1World.getInstance().findObject(id);
									if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_MONSTER)) {
										L1Character c = (L1Character) obj;
										if (!c.isDead())
											continue;
									}
								}
								if (b.getnonespawntime() != 0 && MJRnd.isWinning(100, b.getnonespawntime())) {
									System.out.println(b.getMonName() + "멍탐 보스");
									System.out.println(b.getnonespawntime() + "멍탐 발동확률");
									return;
								}
								L1MonsterInstance boss = boss_spawn(b, b.getX(), b.getY(), (short) b.getMap(),
										b.getNpcId(), b.getRndLoc(), 3600000, b.isMent(), b.isYn(),
										b.get_display_effect());
								_bossesId.put(boss.getNpcId(), boss.getId());
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 컨트롤러가 1분마다 한번씩 호출됨.
	 */
	@Override
	public void run() {
		try {
			spawn_check();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GeneralThreadPool.getInstance().schedule(this, 60000L);
		}
	}

	public static L1MonsterInstance boss_spawn(L1Boss b, int x, int y, short map, int npcId, int randomRange,
			int timeMillisToDelete, boolean ment, boolean ynment, int display_effect) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
				// npc.getLocation().forward(5);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(x, y, map);
					npc.getLocation().forward(5);
				}
			}

			if (npc.getNpcId() == 900007 || npc.getNpcId() == 900015 || npc.getNpcId() == 900036
					|| npc.getNpcId() == 900219) {
				for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(npc)) {
					npc.onPerceive(_pc);
					S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk);
					_pc.sendPackets(gfx);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(5);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			if (ment) {
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(b.getMentMessage()));
				L1World.getInstance()
						.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, b.getMentMessage()));
			}

			S_DisplayEffect effect = null;
			if (ynment && !Config.STANDBY_SERVER) {
				if (display_effect > 0)
					effect = S_DisplayEffect.newInstance(display_effect);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc.isPrivateShop() || !pc.isBossNotify() || !pc.is_world() || !pc.getMap().isEscapable())
						continue;
					if (effect != null)
						pc.sendPackets(effect, false);

					if (pc.getAI() != null && pc.getAI().getBotType() == MJBotType.HUNT) {
						MJBotBossNotifier ntf = MJBotBossNotifierLoader.getInstance().get(npc.getNpcId());
						MJBotAI ai = pc.getAI();
						if (ai instanceof MJBotMovableAI) {
							if (((MJBotMovableAI) ai).getWarCastle() != -1)
								continue;
						}

						if (ntf != null && ai.getBrain().toRand(100 - ntf.aggro) < ai.getBrain().getHormon())
							ai.teleport(npc.getX(), npc.getY(), npc.getMapId());
						continue;
					}
					pc.setBossYN(npc.getNpcId());
					// pc.sendPackets(new S_Message_YN(622, b.getYnMessage()));
					pc.sendPackets(new S_Message_YN(C_Attr.MSGCODE_622_BOSS, 622, b.getYnMessage()));
				}
				if (effect != null)
					effect.clear();
				try {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
								if (pc.getBossYN() != 0) {
									pc.setBossYN(0);
								}
							}
						}
					}, 10000);// 보스스폰대기시간
				} catch (Exception e1) {
				}
			}
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return npc instanceof L1MonsterInstance ? (L1MonsterInstance) npc : null;
	}
}
