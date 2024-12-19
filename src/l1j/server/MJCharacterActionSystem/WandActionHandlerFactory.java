package l1j.server.MJCharacterActionSystem;

import l1j.server.MJCharacterActionSystem.Wand.WandActionHandler;
import l1j.server.server.model.Instance.L1ItemInstance;

public class WandActionHandlerFactory {
	public static WandActionHandler create(L1ItemInstance item){
		switch(item.getItemId()){
		case 40007:
		case 40412:
			return new WandActionHandler(item, 10, 0.16D);
		case 40006:
		case 140006:
			return new WandActionHandler(item, 11736, 0.5D);
		}
		return null;
	}
}
