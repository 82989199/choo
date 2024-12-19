package l1j.server.MJCharacterActionSystem.Attack;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.io.IOException;

import l1j.server.IndunSystem.MiniGame.L1Gambling3;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.SkillCheck;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ClanJoinInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.MJCommons;

public class AttackActionHandler extends AbstractActionHandler implements MJPacketParser {
	public static void do_clanjoin(L1PcInstance pc, L1ClanJoinInstance cj) {
		int tx = cj.getX();
		int ty = cj.getY();
		int mid = pc.getMapId();
		int ox = pc.getX();
		int oy = pc.getY();
		int cx = Math.abs(ox - tx);
		int cy = Math.abs(oy - ty);
		int r = pc.getAttackRang();
		if (cx > r || cy > r)
			return;

		int h = MJCommons.calcheading(ox, oy, tx, ty);
		if (!pc.getMap().isUserPassable(ox, oy, h))
			return;

		cj.receiveDamage(pc, 0);
		pc.setHeading(pc.targetDirection(tx, ty));
		S_AttackStatus s = new S_AttackStatus(pc, 0, pc.getCurrentWeapon() + 1);
		pc.sendPackets(s, false);
		pc.broadcastPacket(s);
	}

	protected int tId;
	protected int x;
	protected int y;
	protected L1Character c;

	@Override
	public void dispose() {
		super.dispose();
		c = null;
	}

	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tId = pck.readD();
		x = pck.readH();
		y = pck.readH();

		if (owner.isOnTargetEffect()) {
			long cur = System.currentTimeMillis();
			if (cur - owner.getLastNpcClickMs() > 1000) {
				owner.setLastNpcClickMs(cur);
				owner.sendPackets(new S_SkillSound(tId, 14486));
			}
		}
	}

	@Override
	public void doWork() {
		owner.killSkillEffectTimer(L1SkillId.MEDITATION);
		
		if (owner.hasSkillEffect(L1SkillId.MOEBIUS)) {
			owner.killSkillEffectTimer(L1SkillId.MOEBIUS);

			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.MOEBIUS);
			noti.set_duration(0);
			noti.set_off_icon_id(9443);
			noti.set_end_str_id(5551);
			noti.set_is_good(true);
			owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
		
		owner.setRegenState(REGENSTATE_ATTACK);
		register();
	}

	private void do_focus_wave() {
		if (!owner.hasSkillEffect(L1SkillId.FOCUS_WAVE)) {
			return;
		}

		L1ItemInstance weapon = owner.getWeaponSwap();
		if (weapon == null || (weapon.getItem().getType1() != 20 && weapon.getItem().getType1() != 62)) {
			owner.removeSkillEffect(L1SkillId.FOCUS_WAVE);
			return;
		}

		if (tId == 0 || tId != owner.get_last_focusing_target()) {
			if (owner.get_focus_wave_level() > 1) {
				owner.sendPackets(new S_SkillBrave(owner.getId(), 10, owner.getSkillEffectTimeSec(L1SkillId.FOCUS_WAVE)));
				Broadcaster.broadcastPacket(owner, new S_SkillBrave(owner.getId(), 10, 0));
			}
			owner.set_focus_wave_level(1);
			owner.set_last_focusing_target(tId);
			return;
		}

		switch (owner.get_focus_wave_level()) {
		case 1:
			if (MJRnd.isWinning(1000000, 200000)) {
				owner.inc_focus_wave_level();
				owner.sendPackets(new S_SkillBrave(owner.getId(), 11, owner.getSkillEffectTimeSec(L1SkillId.FOCUS_WAVE)));
				Broadcaster.broadcastPacket(owner, new S_SkillBrave(owner.getId(), 11, 0));
			}
			break;
		case 2:
			if (MJRnd.isWinning(1000000, 200000)) {
				owner.inc_focus_wave_level();
				owner.sendPackets(new S_SkillBrave(owner.getId(), 12, owner.getSkillEffectTimeSec(L1SkillId.FOCUS_WAVE)));
				Broadcaster.broadcastPacket(owner, new S_SkillBrave(owner.getId(), 12, 0));
			}
			break;
		}
	}

	@Override
	public void handle() {
		if (!validation())
			return;

		do_focus_wave();

		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) {
			owner.removeSkillEffect(ABSOLUTE_BARRIER);
		}
		owner.delInvis();

		if (c == null || c.isDead()) {
			owner.setHeading(owner.targetDirection(x, y));
			S_AttackStatus s = new S_AttackStatus(owner, 0, owner.getCurrentWeapon() + 1);
			owner.sendPackets(s, false);
			owner.broadcastPacket(s);
	
		} else {
			c.onAction(owner);
			if (owner.isElf() && owner.isAutoTreeple && !c.isDead() && !owner.hasAction(CharacterActionExecutor.ACTION_IDX_SPELL)) {
				if (owner.getCurrentMp() >= 15 && SkillCheck.getInstance().CheckSkill(owner, L1SkillId.TRIPLE_ARROW)) {
					if (owner.lastSpellUseMillis < System.currentTimeMillis() - 300L) {
						BinaryOutputStream bos = new BinaryOutputStream();
						bos.writeC(Opcodes.C_USE_SPELL);
						bos.writeC(16);
						bos.writeC(3);
						bos.writeD(tId);
						bos.writeH(x);
						bos.writeH(y);
						final byte[] buff = bos.getBytes();
						try {
							bos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						GeneralThreadPool.getInstance().execute(new Runnable() {
							@Override
							public void run() {
								if (c != null && !c.isDead() && owner != null && !owner.isDead()) {
									try {
										owner.getNetConnection().handle(buff);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						});
					}
				}
			}
		}
	}

	@Override
	public boolean validation() {
		if (owner == null)
			return false;

		if (owner.isGmInvis() || owner.is_non_action() || MJCommons.isNonAction(owner) || owner.isDead() || (owner.isInvisble()) || owner.isGhost()
				|| owner.get_teleport() || owner.isInvisDelay())
			return false;

		if (owner.hasSkillEffect(L1SkillId.BLIND_HIDING))
			return false;

		if (!owner.is_top_ranker() && owner.getInventory().getWeight100() > 82) {
			owner.sendPackets(new S_ServerMessage(110));
			return false;
		}

		int mid = owner.getMapId();
		int ox = owner.getX();
		int oy = owner.getY();
		int oh = owner.getHeading();
		if (!(mid >= 2600 && mid <= 2699) && owner.getInventory().checkEquipped(203003)) {
			owner.sendPackets("화룡의 안식처에서만 사용이 가능합니다.");
			return false;
		}

		owner.offFishing();
		L1Object obj = L1World.getInstance().findObject(tId);
		// 뉴 방패 뽕데스 : 12015
		// 뉴 뽕데스: 5641 / 11685
		// 88:11620/헬바인 86:11621
		if (!owner.isGm()) {
			if (obj != null && obj instanceof L1PcInstance) {
				if (owner.getCurrentSpriteId() == 12015 || owner.getCurrentSpriteId() == 5641 || owner.getCurrentSpriteId() == 11685
						|| owner.getCurrentSpriteId() == 11620 || owner.getCurrentSpriteId() == 11621) {
					owner.sendPackets("전설/신화 변신상태에서는 PVP가 불가합니다.");
					return false;
				}
			}
		}

		if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_NPC)) {
			if (((L1NpcInstance) obj).getHiddenStatus() != 0)
				return false;
		}

		if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_CHARACTER)) {
			if (obj.instanceOf(MJL1Type.L1TYPE_NPC)) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (ox == 33515 && oy == 32851 && mid == 4) {
					if (npc.getNpcId() == 300027) {
						L1Gambling3 gam3 = new L1Gambling3();
						gam3.dealerTrade(owner);
					}
				}

				if (npc.getHiddenStatus() != 0)
					return false;
			}

			this.c = (L1Character) obj;
			SpriteInformation sInfo = c.getCurrentSprite();
			int cx = Math.abs(ox - x);
			int cy = Math.abs(oy - y);
			int r = owner.getAttackRang();
			int h = MJCommons.calcheading(ox, oy, x, y);
			/*
			 * if(oh == h && (cx == 2) || (cy == 2)) return true;
			 */

			if (r <= 3) {
				if (cx > sInfo.getWidth() + r || cy > sInfo.getHeight() + r) {
					if (System.currentTimeMillis() - owner.getLastMoveActionMillis() > 1000) {
						return false;
					}
					int diff_h = Math.abs(MJCommons.calcheading(ox, oy, c.getX(), c.getY()) - oh);
					if (diff_h <= 1 && cx < sInfo.getWidth() + r + 5 && cy < sInfo.getHeight() + r + 5)
						return true;
					return false;
				}
			}

			if (c.getLocation().getTileLineDistance(x, y) > 1)
				return false;

			if (!owner.getMap().isUserPassable(ox, oy, h))
				return false;

		} else if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_CLANJOIN)) {
			do_clanjoin(owner, (L1ClanJoinInstance) obj);
			return false;
		}

		return true;
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_ATTACK;
	}

	@Override
	public long getInterval() {
		return owner.getCurrentSpriteInterval(EActionCodes.fromInt(owner.getCurrentWeapon() + 1));
	}

}
