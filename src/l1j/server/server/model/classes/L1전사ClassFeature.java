package l1j.server.server.model.classes;

public class L1전사ClassFeature extends L1ClassFeature {
	@Override
	public int getAcDefenseMax(int ac) {
		return ac / 2;
	}

	@Override
	public int getMagicLevel(int playerLevel) {
		return playerLevel / 50;
	}
}
