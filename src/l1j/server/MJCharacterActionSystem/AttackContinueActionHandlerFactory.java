package l1j.server.MJCharacterActionSystem;

import l1j.server.MJCharacterActionSystem.AttackContinue.AttackContinueActionHandler;

public class AttackContinueActionHandlerFactory {
	public static AttackContinueActionHandler create(){
		return new AttackContinueActionHandler();
	}
}
