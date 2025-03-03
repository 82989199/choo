package l1j.server.server.model.Instance;

import l1j.server.server.ActionCodes;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.templates.L1Npc;

public class L1NpcCashShopInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private int _state = 0;
	private String _shopName;

	/**
	 * @param template
	 */
	public L1NpcCashShopInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.addKnownObject(this);
		if(perceivedFrom.getAI() != null)
			return;
		
		perceivedFrom.sendPackets(S_WorldPutObject.get(this));

		if (_state == 1)
			perceivedFrom.sendPackets(new S_DoActionShop(getId(), ActionCodes.ACTION_Shop, getShopName().getBytes()));
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
	}

	public int getState() {
		return _state;
	}

	public void setState(int i) {
		_state = i;
	}

	public String getShopName() {
		return _shopName;
	}

	public void setShopName(String name) {
		_shopName = name;
	}

}
