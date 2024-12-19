package l1j.server.MJCharacterActionSystem;

import l1j.server.MJCharacterActionSystem.Walk.WalkActionHandler;

public class WalkActionHandlerFactory {
	public static WalkActionHandler create(){
		return new WalkActionHandler();
	}
}
