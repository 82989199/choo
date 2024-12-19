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
package l1j.server.server.model;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.SingleTask;
import l1j.server.server.datatables.MonsterParalyzeDelay;
import l1j.server.server.datatables.MonsterParalyzeDelay.MonsterParalyze;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ServerMessage;

public class L1CurseParalysis extends L1Paralysis {
	
	private class ParalysisDelayTimer extends SingleTask {
		@Override
		public void execute() {

			if( isActive() )
			{
//				_target.killSkillEffectTimer(L1SkillId.STATUS_CURSE_PARALYZING);

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
	private final int _delay;
	private final int _time;
	private SingleTask _timer;
	private L1CurseParalysis(L1Character cha, int skill_id) {
		_target = cha;
		MonsterParalyze paralyze = MonsterParalyzeDelay.getInstance().get_paralyze(skill_id);
		_delay = paralyze.paralyze_delay;
		_time = paralyze.paralyze_millis;

		curse();
	}

	private void curse() {
		if (_target instanceof L1PcInstance) {
			L1PcInstance player = (L1PcInstance) _target;
			player.sendPackets(new S_ServerMessage(212));
		}

		_target.setPoisonEffect(2);
		_timer = new ParalysisDelayTimer();
		_target.setSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING, _delay);
		if(_target instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance)_target;
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
		}
		GeneralThreadPool.getInstance().schedule(_timer, _delay);
	}

	public static boolean curse(L1Character cha, int skill_id) {
		if (!(cha instanceof L1PcInstance || cha instanceof L1MonsterInstance)) {
			return false;
		}

		if (cha.hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING)
				|| cha.hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED)) {
			return false;
		}
		cha.setParalaysis(new L1CurseParalysis(cha, skill_id));
		return true;
	}

	@Override
	public int getEffectId() {
		return 2;
	}

	@Override
	public void cure() {
		
		if (_timer != null) {
			_timer.cancel();

			if( _timer instanceof ParalysisTimer)
			{
				if( !_timer.isExecuted())
				{
					_timer.execute();
				}
			}
			else
			{
				_target.killSkillEffectTimer(L1SkillId.STATUS_POISON_PARALYZING);
			}

			_timer = null;
		}

		_target.setPoisonEffect(0);
		_target.setParalaysis(null);

		
		if(_target instanceof L1PcInstance){
			
			L1PcInstance pc = (L1PcInstance)_target;
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
		_target.removeSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING);
		_target.removeSkillEffect(L1SkillId.STATUS_CURSE_PARALYZED);
		
	}
}
