package l1j.server.MJCharacterActionSystem;

import l1j.server.MJCharacterActionSystem.Attack.AttackActionHandler;

public class AttackActionHandlerFactory {
	public static AttackActionHandler create(){
		return new AttackActionHandler();
	}
}
