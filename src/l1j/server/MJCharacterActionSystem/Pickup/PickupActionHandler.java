package l1j.server.MJCharacterActionSystem.Pickup;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJRaidSystem.MJRaidObject;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Action.MJPickupChain;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServer;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_AttackStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.MJCommons;

public class PickupActionHandler extends AbstractActionHandler implements MJPacketParser {
	private static final long MAX_STACKABLE = 2000000000L;

	protected int x;
	protected int y;
	protected int tId;
	protected long amount;
	protected L1ItemInstance item;
	protected L1PcInventory inven;
	protected L1Inventory gInven;

	@Override
	public void dispose() {
		item = null;
		inven = null;
		gInven = null;
		super.dispose();
	}

	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		x = pck.readH();
		y = pck.readH();
		tId = pck.readD();
		amount = pck.readD();
	}

	@Override
	public void doWork() {
		owner.killSkillEffectTimer(L1SkillId.MEDITATION);
		register();
	}

	@Override
	public void handle() {
		if (!validation())
			return;

		L1Party party = owner.getParty();
		if (party != null)
			handleParty(party);
		else
			handleNonParty();
		owner.getLight().turnOnOffLight();
		S_AttackStatus pck = new S_AttackStatus(owner, tId, ActionCodes.ACTION_Pickup);
		owner.sendPackets(pck, false);
		if (!owner.isGmInvis() && !owner.isGhost() && !owner.isInvisble())
			owner.broadcastPacket(pck, false);
		pck.clear();
		if (MJRaidSpace.getInstance().isInInstance(owner)) {
			MJRaidObject obj = MJRaidSpace.getInstance().getOpenObject(owner.getMapId());
			if (obj != null)
				obj.notifyRaidPickupItem(owner, item, (int) amount);
		}
		
		if(!item.isGiveItem()){		
			int pickupItemId = item.getItemId();
			if(!owner.isGm() && Config.DROPMENT_ITEM.containsKey(pickupItemId)){
				String ownerName = owner.getName();
				String locationName =  MapsTable.getInstance().getMapName(owner.getMapId());
				String itemName = item.getName();
				L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
					new S_SystemMessage(
							String.format("%s 님이 (%s\\fH)을(를) %s 에서 획득하였습니다.", ownerName, locationName, itemName)),
					new S_PacketBox(
							S_PacketBox.GREEN_MESSAGE, String.format("%s 님이 (%s\\f2)을(를) %s 에서 획득하였습니다.", ownerName, locationName, itemName))
				});
			}
		}
		item.setGiveItem(false);
		LoggerInstance.getInstance().addItemAction(ItemActionType.Pickup, owner, item, (int) amount);
	}

	private void handleNonParty() {
		if(MJPickupChain.getInstance().handle(owner, item, (int)amount))
			return;

		gInven.tradeItem(item, (int) amount, inven);
	}

	private void handleParty(L1Party party) {
		L1Npc npc = NpcTable.getInstance().getTemplate(item.isDropMobId());
		if (party.getLeader().getPartyType() == 1 && item.isDropMobId() != 0 && party.getNumOfMembers() > 1) {
			try {
				item.setDropMobId(0);
				Stream<L1PcInstance> stream = party.createVisibleMembersStream(owner);
				ArrayList<L1PcInstance> members = stream.collect(Collectors.toCollection(ArrayList::new));
				long len = members.size();
				if (item.getItemId() == L1ItemId.ADENA && amount > len) {
					int div = (int) (amount / len);
					int adder = (int) (amount % len);
					for (L1PcInstance pc : members) {
						int amt = div;
						if (pc.getId() == owner.getId())
							amt += adder;

						if(MJPickupChain.getInstance().handle(pc, item, (int)amount))
							continue;
						

						gInven.tradeItem(item, amt, pc.getInventory());
						//ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(),
						//		pc.getName());
						ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), "아데나 ("+amt+")",
								pc.getName());
						party.broadcastRootMent(pck);
					}
				} else {
					L1PcInstance luck = members.get(MJRnd.next(members.size()));
					if(MJPickupChain.getInstance().handle(luck, item, (int)amount))
						return;

					gInven.tradeItem(item, (int) amount, luck.getInventory());
					ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(), luck.getName());
					party.broadcastRootMent(pck);
					members.clear();
				}
			} catch (Exception e) {
				if(MJPickupChain.getInstance().handle(owner, item, (int)amount))
					return;

				gInven.tradeItem(item, (int) amount, inven);
				if (npc != null) {
					ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(), owner.getName());
					party.broadcastRootMent(pck);
				}
			}
		} else {
			if(MJPickupChain.getInstance().handle(owner, item, (int)amount))
				return;

			gInven.tradeItem(item, (int) amount, inven);
			if (npc != null) {
				ServerBasePacket pck = new S_ServerMessage(813, npc.get_name(), item.getLogName(), owner.getName());
				party.broadcastRootMent(pck);
			}
		}
	}

	@Override
	public boolean validation() {
		if (owner.isDead())
			return false;

		if (MJCommons.isNonAction(owner))
			return false;
		
		if (owner.hasSkillEffect(ABSOLUTE_BARRIER)) 
			return false;

		return validationInventory() && validationState() && validationAmount() && validationPosition();
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_PICKUP;
	}

	@Override
	public long getInterval() {
		return owner.hasSkillEffect(L1SkillId.SHAPE_CHANGE) || owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER) || owner.hasSkillEffect(L1SkillId.POLY_RING_MASTER2) ? 10L
				: owner.getCurrentSpriteInterval(EActionCodes.get);
	}

	private boolean validationInventory() {
		gInven = L1World.getInstance().getInventory(x, y, owner.getMapId());
		L1Object obj = gInven.getItem(tId);
		if (obj == null || obj.getId() != tId)
			return false;
		item = (L1ItemInstance) obj;
		inven = owner.getInventory();
		
		if (inven.getWeight100() > 90 && owner.is_ranking_buff()) {
			return true;
		}

		if (inven.getWeight100() > 90 || owner.getMaxWeight() <= inven.getWeight()) {
			owner.sendPackets("소지품이 너무 무겁습니다.");
			return false;
		}
		return true;
	}

	private boolean validationState() {
		L1PcInstance pc = item.getItemOwner();
		if (pc != null && pc.getId() != owner.getId()) {
			L1Party party = pc.getParty();
			if (party == null || !party.isMember(owner)) {
				owner.sendPackets(new S_ServerMessage(623));
				return false;
			}
		}
		return true;
	}

	private boolean validationAmount() {
		if (amount > item.getCount())
			amount = item.getCount();

		int itemAmount = item.getCount();
		if (amount <= 0 || itemAmount <= 0 || itemAmount > MAX_STACKABLE) {
			GameServer.disconnectChar(owner);
			gInven.deleteItem(item);
			return false;
		}

		if (item.isStackable()) {
			L1ItemInstance invItem = inven.findItemId(item.getItemId());
			long sum = invItem == null ? 0L : invItem.getCount();
			if (amount + sum > MAX_STACKABLE) {
				owner.sendPackets("아이템(아데나) 획득 초과로 드랍이 불가합니다.(창고이용)");
				return false;
			} else if (item.getItemId() != L1ItemId.ADENA && amount > 1000000) {
				owner.sendPackets("너무많은 드랍은 불가능합니다.");
				return false;
			}
		} else if (amount > 1) {
			GameServer.disconnectChar(owner);
			return false;
		}

		return inven.checkAddItem(item, (int) amount) == L1Inventory.OK;
	}

	private boolean validationPosition() {
		int cx = Math.abs(x - owner.getX());
		int cy = Math.abs(y - owner.getY());
		return (cx < 2 && cy < 2) && (item.getX() != 0 && item.getY() != 0);
	}

}
