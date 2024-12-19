package l1j.server.MJCharacterActionSystem.AttackContinue;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.io.IOException;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Attack.AttackActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.MJCommons;

public class AttackContinueActionHandler extends AbstractActionHandler implements MJPacketParser {
	protected int tId;
	protected L1Character target;
	protected boolean isUnregister;

	public AttackContinueActionHandler() {
		isUnregister = false;
	}

	@Override
	public void dispose() {
		target = null;
		super.dispose();
	}

	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		tId = pck.readD();
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
		target.onAction(owner);
		if (owner.isElf() && owner.isAutoTreeple && !target.isDead() && !owner.hasAction(CharacterActionExecutor.ACTION_IDX_SPELL)) {
			if (owner.getCurrentMp() >= 15 && SkillCheck.getInstance().CheckSkill(owner, L1SkillId.TRIPLE_ARROW)) {
				if (owner.lastSpellUseMillis < System.currentTimeMillis() - 300L) {
					BinaryOutputStream bos = new BinaryOutputStream();
					bos.writeC(Opcodes.C_USE_SPELL);
					bos.writeC(16);
					bos.writeC(3);
					bos.writeD(target.getId());
					bos.writeH(target.getX());
					bos.writeH(target.getY());
					final byte[] buff = bos.getBytes();
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					GeneralThreadPool.getInstance().execute(new Runnable() {
						@Override
						public void run() {
							if (target != null && !target.isDead() && owner != null && !owner.isDead()) {
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

	@Override
	public boolean validation() {
		if (tId <= 0) {
			unregister();
			return false;
		}

		if (owner.isGmInvis() || owner.is_non_action() || owner.isFishing() || owner.isDead() || (owner.isInvisble()) || owner.isGhost() || owner.get_teleport()
				|| owner.isInvisDelay()) {
			unregister();
			return false;
		}

		if (owner.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
			unregister();
			return false;
		}

		if (MJCommons.isNonAction(owner))
			return false;

		if (!owner.is_top_ranker() && owner.getInventory().getWeight100() > 82) {
			owner.sendPackets(new S_ServerMessage(110));
			return false;
		}

		L1Object obj = L1World.getInstance().findObject(tId);

		if (!owner.isGm()) {
			if (obj != null && obj instanceof L1PcInstance) {
				if (owner.getCurrentSpriteId() == 12015 || owner.getCurrentSpriteId() == 5641 || owner.getCurrentSpriteId() == 11685
						|| owner.getCurrentSpriteId() == 11620 || owner.getCurrentSpriteId() == 11621) {
					owner.sendPackets("전설/신화 변신상태에서는 PVP가 불가합니다.");
					return false;
				}
			}
		}

		if (obj == null || !obj.instanceOf(MJL1Type.L1TYPE_CHARACTER)) {
			if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_CLANJOIN)) {
				AttackActionHandler.do_clanjoin(owner, (L1ClanJoinInstance) obj);
			}
			unregister();
			return false;
		}

		target = (L1Character) obj;
		if (target.isDead() || owner.getMapId() != target.getMapId() || target.isInvisble()) {
			unregister();
			return false;
		}

		int tx = target.getX();
		int ty = target.getY();
		int mid = owner.getMapId();
		int ox = owner.getX();
		int oy = owner.getY();
		if (!(mid >= 2600 && mid <= 2699) && owner.getInventory().checkEquipped(203003)) {
			unregister();
			return false;
		}

		SpriteInformation sInfo = target.getCurrentSprite();
		int cx = Math.abs(ox - tx);
		int cy = Math.abs(oy - ty);
		int r = owner.getAttackRang();
		if (cx > sInfo.getWidth() + r || cy > sInfo.getHeight() + r) {
			if (owner.getWeapon() != null)
				unregister();// 끊김현상 때문에 주석
			return false;
		}

		return true;
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_ATTACKCONTINUE;
	}

	@Override
	public void unregister() {
		isUnregister = true;
		super.unregister();
	}

	@Override
	public long getInterval() {
		return isUnregister ? 10L : owner.getCurrentSpriteInterval(EActionCodes.fromInt(owner.getCurrentWeapon() + 1));
	}
}
