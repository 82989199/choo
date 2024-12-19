package l1j.server.MJCharacterActionSystem;

import l1j.server.MJCharacterActionSystem.Pickup.PickupActionHandler;

public class PickupActionHandlerFactory {
	public static PickupActionHandler create(){
		return new PickupActionHandler();
	}
}
