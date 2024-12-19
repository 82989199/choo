package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Skills;

public class L1BuffUtil {

	public static void haste(L1PcInstance pc, int timeMillis) {

		pc.setSkillEffect(STATUS_HASTE, timeMillis);
		int objId = pc.getId();
		pc.sendPackets(new S_SkillHaste(objId, 1, timeMillis / 1000));
		pc.broadcastPacket(new S_SkillHaste(objId, 1, 0));
		pc.sendPackets(new S_SkillSound(objId, 191));
		pc.broadcastPacket(new S_SkillSound(objId, 191));
		pc.setMoveSpeed(1);
	}

	public static void barrier(L1PcInstance pc, int timeMillis) {
		pc.setSkillEffect(ABSOLUTE_BARRIER, timeMillis);
	}

	public static void cancelBarrier(L1PcInstance pc) {
		if (pc.hasSkillEffect(ABSOLUTE_BARRIER))
			pc.removeSkillEffect(ABSOLUTE_BARRIER);
	}

	public static void brave(L1PcInstance pc, int timeMillis) {

		pc.setSkillEffect(STATUS_BRAVE, timeMillis);
		int objId = pc.getId();
		pc.sendPackets(new S_SkillBrave(objId, 1, timeMillis / 1000));
		pc.broadcastPacket(new S_SkillBrave(objId, 1, 0));
		pc.sendPackets(new S_SkillSound(objId, 751));
		pc.broadcastPacket(new S_SkillSound(objId, 751));
		pc.setBraveSpeed(1);
	}

	public static void thirdSpeed(L1PcInstance pc, int timeMillis) {
		if (pc.hasSkillEffect(STATUS_DRAGON_PEARL)) {
			pc.killSkillEffectTimer(STATUS_DRAGON_PEARL);
		}
		pc.setSkillEffect(STATUS_DRAGON_PEARL, timeMillis);
		pc.sendPackets(new S_SkillSound(pc.getId(), 8031));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 8031));
		pc.sendPackets(new S_Liquor(pc.getId(), 8));
		pc.broadcastPacket(new S_Liquor(pc.getId(), 8));
	}

	public static void useBuff(L1PcInstance pc, int skillId) {
		new L1SkillUse().handleCommands(pc, skillId, pc.getId(), pc.getX(), pc.getY(), null, 0, 4);
	}

	public static void takeAllBuff(Collection<L1PcInstance> pcList, int buffType, int time) {
		ArrayList<Integer> buffList = null;
		if (buffType == 1) {
			buffList = L1BuffUtil.getBuffList_1();
		} else if (buffType == 2) {
			buffList = new ArrayList<Integer>();
			buffList.add(Integer.valueOf(FEATHER_BUFF_A));
		} else if (buffType == 3) {
			buffList = new ArrayList<Integer>();
			buffList.add(Integer.valueOf(God_buff));
		} else if (buffType == 4) {
			buffList = new ArrayList<Integer>();
			buffList.add(Integer.valueOf(COMA_B));
		}
		
		
		for (L1PcInstance target : pcList) {
			if(target == null || target.isFishing())
				continue;
			
			if (buffType == 2) {
				if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_A)) {
					target.removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
				}
				if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_B)) {
					target.removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
				}
				if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_C)) {
					target.removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
				}
				if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_D)) {
					target.removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
				}
			} else if (buffType == 3) {
				if (target.hasSkillEffect(L1SkillId.God_buff)) {
					target.removeSkillEffect(L1SkillId.God_buff);
				}
			} else if (buffType == 4) {
				if (target.hasSkillEffect(L1SkillId.COMA_B)) {
					target.removeSkillEffect(L1SkillId.COMA_B);
				}
			}
			if ((target != null) && (!target.isDead())) {
				if (target.noPlayerRobot) {
					for (Iterator<Integer> localIterator2 = buffList.iterator(); localIterator2.hasNext();) {
						int skillId = ((Integer) localIterator2.next()).intValue();
						L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
						target.broadcastPacket(new S_SkillSound(target.getId(), skill.getCastGfx()));
					}
				} else {
					for (Iterator<Integer> localIterator2 = buffList.iterator(); localIterator2.hasNext();) {
						int skillId = ((Integer) localIterator2.next()).intValue();
						if ((skillId == BURNING_WEAPON) && (target.getCurrentWeapon() == 20)) {
							skillId = STORM_SHOT;
						} else if ((skillId == EARTH_WEAPON) && (target.getCurrentWeapon() == 20)) {
							skillId = AQUA_SHOT;
						} else if ((skillId == ADVANCE_SPIRIT) && (target.is전사())) {
							skillId = GIGANTIC;
						}
						if (skillId <= SKILLS_END) {
							new L1SkillUse().handleCommands(target, skillId, target.getId(), target.getX(),
									target.getY(), null, time * 60, 1);
							L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
							target.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, skill.getCastGfx(), time * 60));
						} else {
							useBuff(target, skillId);
						}
					}
				}
			}
		}
	}

	public static void startAllBuffThread(L1PcInstance user, String range, int buffType) {
		new AllBuffThread(user, range, buffType).begin();
	}

	public static class AllBuffThread implements Runnable {
		L1PcInstance _user;
		String _range;
		int _buffType;
		int _time;

		AllBuffThread(L1PcInstance user, String range, int buffType) {
			_user = user;
			_range = range;
			_buffType = buffType;
		}

		public void run() {
			try {
				ArrayList<Integer> buffList = null;
				if (_buffType == 1) {
					buffList = L1BuffUtil.getBuffList_1();
				} else if (_buffType == 2) {
					buffList = new ArrayList<Integer>();
					buffList.add(Integer.valueOf(FEATHER_BUFF_A));
				} else if (_buffType == 3) {
					buffList = new ArrayList<Integer>();
					buffList.add(Integer.valueOf(God_buff));
				} else if (_buffType == 4) {
					buffList = new ArrayList<Integer>();
					buffList.add(Integer.valueOf(COMA_B));
				}
				Iterator<Integer> iter = buffList.iterator();
				while (iter.hasNext()) {
					int skillId = ((Integer) iter.next()).intValue();
					Collection<L1PcInstance> pcList = new ArrayList<L1PcInstance>();
					L1PcInstance pc;
					if (_range == null) {
						pcList = L1World.getInstance().getAllPlayers();
					} else if (_range.equals("!")) {
						pcList = L1World.getInstance().getRecognizePlayer(_user);
					} else {
						pc = L1World.getInstance().getPlayer(_range);
						if (pc == null) {
							return;
						}
						pcList.add(pc);
					}
					for (L1PcInstance target : pcList) {
						if(target == null || target.isFishing())
							continue;
						
						if (_buffType == 2) {
							if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_A)) {
								target.removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
							}
							if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_B)) {
								target.removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
							}
							if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_C)) {
								target.removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
							}
							if (target.hasSkillEffect(L1SkillId.FEATHER_BUFF_D)) {
								target.removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
							}
						} else if (_buffType == 3) {
							if (target.hasSkillEffect(L1SkillId.God_buff)) {
								target.removeSkillEffect(L1SkillId.God_buff);
							}
						} else if (_buffType == 4) {
							if (target.hasSkillEffect(L1SkillId.COMA_B)) {
								target.removeSkillEffect(L1SkillId.COMA_B);
							}	
						}
						if ((target != null) && (!target.isDead())) {
							int eachSkillId = skillId;
							if ((skillId == BURNING_WEAPON) && (target.getCurrentWeapon() == 20)) {
								eachSkillId = STORM_SHOT;
							} else if ((skillId == EARTH_WEAPON) && (target.getCurrentWeapon() == 20)) {
								eachSkillId = AQUA_SHOT;
							} else if ((skillId == ADVANCE_SPIRIT) && (target.is전사())) {
								eachSkillId = GIGANTIC;
							}
							
							L1Skills skill = SkillsTable.getInstance().getTemplate(eachSkillId);
							target.setBuffnoch(1);
							if (target.noPlayerRobot) {
								target.broadcastPacket(new S_SkillSound(target.getId(), skill.getCastGfx()));
							} else if (eachSkillId <= SKILLS_END) {
								new L1SkillUse().handleCommands(target, eachSkillId, target.getId(), target.getX(),
										target.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
							} else {
								L1BuffUtil.useBuff(target, eachSkillId);
							}
							target.setBuffnoch(0);
						}
					}
					Thread.sleep(2000L);
				}
			} catch (Exception localException) {
			}
		}

		private void begin() {
			GeneralThreadPool.getInstance().execute(this);
		}
	}
			//TODO 버프추가할때 밑에 리스트
	private static ArrayList<Integer> getBuffList_1() {
		ArrayList<Integer> buffList = new ArrayList<Integer>();
		buffList.add(Integer.valueOf(ENCHANT_WEAPON));
		buffList.add(Integer.valueOf(BLESSED_ARMOR));
		buffList.add(Integer.valueOf(BLESS_WEAPON));
		buffList.add(Integer.valueOf(GREATER_HASTE));
		buffList.add(Integer.valueOf(DECREASE_WEIGHT));
		buffList.add(Integer.valueOf(PHYSICAL_ENCHANT_DEX));
		buffList.add(Integer.valueOf(PHYSICAL_ENCHANT_STR));
		buffList.add(Integer.valueOf(FIRE_SHIELD));
		buffList.add(Integer.valueOf(AQUA_SHOT));
		buffList.add(Integer.valueOf(EAGGLE_EYE));
		buffList.add(Integer.valueOf(QUAKE));
		buffList.add(Integer.valueOf(AQUA_PROTECTER));
		buffList.add(Integer.valueOf(NATURES_TOUCH));
		buffList.add(Integer.valueOf(EXOTIC_VITALIZE));
		buffList.add(Integer.valueOf(IllUSION_DIAMONDGOLEM));
		buffList.add(Integer.valueOf(IllUSION_AVATAR));
		buffList.add(Integer.valueOf(IllUSION_DIAMONDGOLEM));
		buffList.add(Integer.valueOf(GLOWING_AURA));
		buffList.add(Integer.valueOf(SHINING_AURA));
		buffList.add(Integer.valueOf(IllUSION_DIAMONDGOLEM));
		buffList.add(Integer.valueOf(UNCANNY_DODGE));
		buffList.add(Integer.valueOf(DOUBLE_BRAKE));
		buffList.add(Integer.valueOf(BURNING_SPIRIT));
		buffList.add(Integer.valueOf(ADVANCE_SPIRIT));
		buffList.add(Integer.valueOf(IMMUNE_TO_HARM));
		return buffList;
	}

}
