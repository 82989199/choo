package l1j.server.server.model.trap;

import l1j.server.server.datatables.MonsterParalyzeDelay;
import l1j.server.server.datatables.MonsterParalyzeDelay.MonsterParalyze;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.storage.TrapStorage;

public class L1PoisonTrap extends L1Trap {
	private final String _type;
	private final int _delay;
	private final int _time;
	private final int _damage;

	public L1PoisonTrap(TrapStorage storage) {
		super(storage);

		_type = storage.getString("poisonType");
		_delay = storage.getInt("poisonDelay");
		_time = storage.getInt("poisonTime");
		_damage = storage.getInt("poisonDamage");
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
		sendEffect(trapObj);

		if (_type.equals("d")) {
			if( trodFrom.getZoneType() != 1 )
			{
				L1DamagePoison.doInfection(trodFrom, trodFrom, _time, _damage, false);
			}
		} else if (_type.equals("s")) {
			L1SilencePoison.doInfection(trodFrom);
		} else if (_type.equals("p")) {
			MonsterParalyze paralyze = new MonsterParalyzeDelay.MonsterParalyze();
			paralyze.paralyze_delay = _delay;
			paralyze.paralyze_millis = _time;
			paralyze.skill_id = 0;
			L1ParalysisPoison.doInfection(trodFrom, paralyze);
		}
	}
}
