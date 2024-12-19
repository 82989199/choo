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
package l1j.server.server.model.poison;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SingleTask;
import l1j.server.server.datatables.MonsterParalyzeDelay;
import l1j.server.server.datatables.MonsterParalyzeDelay.MonsterParalyze;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Paralysis;

public class L1ParalysisPoison extends L1Poison {

	private class ParalysisPoisonTimer extends SingleTask {
		@Override
		public void execute() {
			_effectId = 2;
			_target.setPoisonEffect(2);

			if( isActive() )
			{
				if (_target instanceof L1PcInstance) {
					L1PcInstance player = (L1PcInstance) _target;

					if (player.isDead() == false)
					{
						SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
						noti.set_noti_type(eNotiType.RESTAT);
						noti.set_spell_id(L1SkillId.CURSE_PARALYZE);
						noti.set_duration(_time / 1000);
						noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
						noti.set_on_icon_id(5270);
						noti.set_off_icon_id(5270);
						noti.set_icon_priority(10);
						noti.set_tooltip_str_id(213);
						noti.set_end_str_id(0);
						noti.set_is_good(false);
						player.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
						player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
					}

					_timer = new ParalysisTimer();
					_target.setSkillEffect(L1SkillId.STATUS_POISON_PARALYZED, 0);
					_target.setParalyzed(true);
					
					GeneralThreadPool.getInstance().schedule(_timer, _time);
				}
			}
		}
	}

	private class ParalysisTimer extends SingleTask {
		@Override
		public void execute() {
			_timer = null;
			cure();
		}
	}


	private final L1Character _target;
	private SingleTask _timer;
	private final int _delay;
	private final int _time;
	private int _effectId = 1;
	
	private L1ParalysisPoison(L1Character cha, MonsterParalyze paralyze) {
		_target = cha;
		_delay = paralyze.paralyze_delay;
		_time = paralyze.paralyze_millis;
		doInfection();
	}

	public static boolean doInfection(L1Character cha, int skill_id) {
		MonsterParalyze paralyze = MonsterParalyzeDelay.getInstance().get_paralyze(skill_id);
		return doInfection(cha, paralyze);
	}
	
	public static boolean doInfection(L1Character cha, MonsterParalyze paralyze) {
		if (!L1Poison.isValidTarget(cha)) {
			return false;
		}

		cha.setPoison(new L1ParalysisPoison(cha, paralyze));
		return true;
	}

	private void doInfection() {
		sendMessageIfPlayer(_target, 212);
		_target.setPoisonEffect(1);

		if (_target instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _target;
			_timer = new ParalysisPoisonTimer();
			_target.setSkillEffect(L1SkillId.STATUS_POISON_PARALYZING, 0);
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.CURSE_PARALYZE);
			noti.set_duration(_delay / 1000);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(5270);
			noti.set_off_icon_id(5270);
			noti.set_icon_priority(10);
			noti.set_tooltip_str_id(212);
			noti.set_end_str_id(0);
			noti.set_is_good(false);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			GeneralThreadPool.getInstance().schedule(_timer, _delay);
		}
	}

	@Override
	public int getEffectId() {
		return _effectId;
	}

	@Override
	public void cure() {
		if (_timer != null) {
			_timer.cancel();

			if (_timer instanceof ParalysisTimer) {
				if (!_timer.isExecuted()) {
					_timer.execute();
				}
			} else {
				_target.killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZING);
			}

			_timer = null;
		}

		if (_target instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _target;
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.END);
			noti.set_spell_id(L1SkillId.CURSE_PARALYZE);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_is_good(false);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
			if (!pc.isDead()) {
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, false));

			}
		}
		_target.setParalyzed(false);
		_target.setPoisonEffect(0);
		_target.setPoison(null);
	}
}
