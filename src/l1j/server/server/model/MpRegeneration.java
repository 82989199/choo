package l1j.server.server.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.CalcStat;

public class MpRegeneration extends RepeatTask {
	private static Logger _log = Logger.getLogger(MpRegeneration.class.getName());

	private final L1PcInstance _pc;

	private int _regenPoint = 0;

	private int _curPoint = 4;

	public MpRegeneration(L1PcInstance pc, long interval) {
		super(interval);
		_pc = pc;
	}

	public void setState(int state) {
		if (_curPoint < state) {
			return;
		}

		_curPoint = state;
	}

	@Override
	public void execute() {
		try {
			if (_pc.isDead()) {
				return;
			}
			if (_pc.getCurrentMp() == _pc.getMaxMp()) {
				return;
			}

			_regenPoint += _curPoint;
			_curPoint = 4;//엠틱4->1초

			if (40 <= _regenPoint) { //원본 64 -> 40
				_regenPoint = 0;
				regenMp();
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenMp() {
		try {
			int baseMpr = 1;
			int wis = _pc.getAbility().getTotalWis();
			if (wis == 15 || wis == 16) {
				baseMpr = 2;
			} else if (wis == 17) {
				baseMpr = 3;
			} else if (wis >= 18) {
				baseMpr += wis - 14;
			}
			// 베이스 WIS 회복 보너스
			int baseStatMpr = CalcStat.calcMpr(_pc.getAbility().getBaseWis());

			if (_pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION2) == true) {
				baseMpr += 1;
			} else if (_pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION) == true) {
				baseMpr += 2;
			}
			if (_pc.hasSkillEffect(L1SkillId.MEDITATION) == true) {
				baseMpr += 5;
			}
			if (_pc.hasSkillEffect(L1SkillId.CONCENTRATION) == true) {
				baseMpr += 2;
			}
			if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId())) {
				baseMpr += 3;
			}
			if (_pc.hasSkillEffect(L1SkillId.COOKING_1_2_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_2_S)) {
				baseMpr += 3;
			}
			if (_pc.hasSkillEffect(L1SkillId.COOKING_1_20_N) // 레서 드레곤 꼬치 구이
					|| _pc.hasSkillEffect(L1SkillId.COOKING_1_20_S)) {
				baseMpr += 2;
			}
			if (_pc.hasSkillEffect(L1SkillId.COOKING_1_12_N) || _pc.hasSkillEffect(L1SkillId.COOKING_1_12_S)) {
				baseMpr += 2;
			}
			if (_pc.hasSkillEffect(L1SkillId.STATUS_CASHSCROLL2) == true) {
				baseMpr += 4;
			}
			if (isInn(_pc)) {
				baseMpr += 3;
			}
			if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId())) {
				baseMpr += 3;
			}
			if (_pc.hasSkillEffect(L1SkillId.지식빙수) == true) { 
				baseMpr += 5; 
				}

			int itemMpr = _pc.getInventory().mpRegenPerTick();
			itemMpr += _pc.getMpr();

			if (_pc.get_food() < 24 || isOverWeight(_pc)) {
				baseMpr = 0;
				if (itemMpr > 0) {
					itemMpr = 0;
				}
			}
			int mpr = baseMpr + itemMpr + baseStatMpr;
			int newMp = _pc.getCurrentMp() + mpr;

			_pc.setCurrentMp(newMp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isOverWeight(L1PcInstance pc) {
		if (pc.hasSkillEffect(L1SkillId.EXOTIC_VITALIZE) || pc.hasSkillEffect(L1SkillId.ADDITIONAL_FIRE)
				|| pc.hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			return false;
		}
		if (isInn(pc)) {
			return false;
		}
		return (120 <= pc.getInventory().getWeight100()) ? true : false;
	}

	private boolean isInn(L1PcInstance pc) {
		int mapId = pc.getMapId();
		return (mapId == 16384 || mapId == 16896 || mapId == 17408 || mapId == 17492 || mapId == 17820
				|| mapId == 17920 || mapId == 18432 || mapId == 18944 || mapId == 19456 || mapId == 19968
				|| mapId == 20480 || mapId == 20992 || mapId == 21504 || mapId == 22016 || mapId == 22528
				|| mapId == 23040 || mapId == 23552 || mapId == 24064 || mapId == 24576 || mapId == 25088) ? true
				: false;
	}
}
