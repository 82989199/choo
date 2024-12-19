package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK.Alchemy;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK.Alchemy.Output;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK.InputList;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_MAKE_ACK.ResultCode;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.AlchemyProbability;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ALCHEMY_MAKE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ALCHEMY_MAKE_REQ newInstance() {
		return new CS_ALCHEMY_MAKE_REQ();
	}

	private int _resultAlchemyId;
	private SC_ALCHEMY_MAKE_ACK.ResultCode _resultCode;

	private LinkedList<Integer> _filled_slots;
	private int _alchemy_id;
	private LinkedList<Input> _inputs;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ALCHEMY_MAKE_REQ() {
		_filled_slots = new LinkedList<Integer>();
	}

	public LinkedList<Integer> get_filled_slot() {
		return _filled_slots;
	}

	public int get_alchemy_id() {
		return _alchemy_id;
	}

	public void set_alchemy_id(int val) {
		_bit |= 0x00000001;
		_alchemy_id = val;
	}

	public boolean has_alchemy_id() {
		return (_bit & 0x00000001) == 0x00000001;
	}

	public LinkedList<Input> get_inputs() {
		return _inputs;
	}

	public void add_inputs(Input val) {
		if (!has_inputs()) {
			_inputs = new LinkedList<Input>();
			_bit |= 0x00000002;
		}
		int sno = val.get_slot_no();
		_filled_slots.add(sno);
		_inputs.add(val);
	}

	public boolean has_inputs() {
		return (_bit & 0x00000002) == 0x00000002;
	}

	public void remove_inputs(L1PcInstance pc) {
		L1Inventory inv = pc.getInventory();
		for (Input input : _inputs)
			inv.removeItem(input.getTemporaryRemovedItems());
	}

	public L1Item selectRemovedItemTemplate() {
		_resultCode = SC_ALCHEMY_MAKE_ACK.ResultCode.RC_FAIL;
		return _inputs.get(MJRnd.next(_inputs.size())).getTemporaryRemovedItems().getItem();
	}

	private L1Item toInvalidOutputItem(L1PcInstance pc) {
		System.out.println(
				String.format("[(인형합성)Output을 찾을 수 없습니다.] 캐릭터 이름 : %s, 합성아이디 : %d", pc.getName(), _alchemy_id));
		return selectRemovedItemTemplate();
	}

	private CS_ALCHEMY_MAKE_REQ toInvalidAlchemyId(L1PcInstance pc) {
		SC_ALCHEMY_MAKE_ACK.ResultCode.RC_ERROR_BLOCKED_ALCHEMY_ID.sendCachedMessage(pc);
		System.out.println(
				String.format("[(인형합성)Alchemy Id를 찾을 수 없습니다.] 캐릭터 이름 : %s, 합성아이디 : %d", pc.getName(), _alchemy_id));
		return this;
	}

	private CS_ALCHEMY_MAKE_REQ toInvalidInput(L1PcInstance pc) {
		SC_ALCHEMY_MAKE_ACK.ResultCode.RC_ERROR_INVALID_INPUT.sendCachedMessage(pc);
		System.out.println(String.format("[(인형합성)Inputs를 찾을 수 없습니다.] 캐릭터 이름 : %s, 합성아이디 : %d, 캐릭터인풋사이즈 : %d",
				pc.getName(), _alchemy_id, _inputs.size()));
		return this;
	}

	private CS_ALCHEMY_MAKE_REQ toInvalidInputForInventory(L1PcInstance pc) {
		SC_ALCHEMY_MAKE_ACK.ResultCode.RC_ERROR_INVALID_INPUT.sendCachedMessage(pc);
		System.out.println(String.format("[(인형합성)유저가 보내온 인형정보를 인벤토리에서 찾을 수 없습니다.] 캐릭터 이름 : %s, 합성아이디 : %d",
				pc.getName(), _alchemy_id));
		StringBuilder sb = new StringBuilder(256);
		for (Input input : _inputs)
			sb.append(input.toString()).append("\r\n");
		System.out.println("-보내온 정보들-");
		System.out.print(sb.toString());
		return this;
	}

	private CS_ALCHEMY_MAKE_REQ doNotAllowInventory(L1PcInstance pc, int addCode) {
		(addCode == L1Inventory.WEIGHT_OVER ? SC_ALCHEMY_MAKE_ACK.ResultCode.RC_ERROR_WEIGHT_OVER
				: SC_ALCHEMY_MAKE_ACK.ResultCode.RC_ERROR_INVEN_OVER).sendCachedMessage(pc);
		return this;
	}

	private L1Item selectResultItem(Alchemy alchemy, L1PcInstance pc) {
		// original formula.
		if (_alchemy_id < 5 && !AlchemyProbability.getInstance().is_winning(_alchemy_id, _inputs.size()))
			return selectRemovedItemTemplate();

		_filled_slots.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		Output output = alchemy.findOutput(_filled_slots);
		if (output == null)
			return toInvalidOutputItem(pc);

		LinkedList<Output> hyper_outputs = output.get_hyper_outputs();
		_resultCode = SC_ALCHEMY_MAKE_ACK.ResultCode.RC_SUCCESS;

		if (_alchemy_id < 5) {
			if (hyper_outputs != null && hyper_outputs.size() > 0
					&& MJRnd.isWinning(CS_CRAFT_MAKE_REQ.CRAFT_ITEM_SUCCESS_MILLIONS,
							_alchemy_id >= 3 ? Config.ALCHEMY_HYPERSUCCESS_PROB_BYMILLION / 2
									: Config.ALCHEMY_HYPERSUCCESS_PROB_BYMILLION)) {
				output = hyper_outputs.get(MJRnd.next(hyper_outputs.size()));
				_resultCode = SC_ALCHEMY_MAKE_ACK.ResultCode.RC_HYPER_SUCCESS;
			}
		} else {
			if(MJRnd.isWinning(CS_CRAFT_MAKE_REQ.CRAFT_ITEM_SUCCESS_MILLIONS,
							_alchemy_id >= 3 ? Config.ALCHEMY_HYPERSUCCESS_PROB_BYMILLION / 2
									: Config.ALCHEMY_HYPERSUCCESS_PROB_BYMILLION)){
				_resultCode = SC_ALCHEMY_MAKE_ACK.ResultCode.RC_HYPER_SUCCESS;
			}
		}

		// L1Item selectItem =
		// SC_ALCHEMY_DESIGN_ACK.selectOutputL1Item(_alchemy_id == 5 ?
		// output.get_output_list_id() - 1 : output.get_output_list_id());
		//System.out.println("인형합성결과 : " + _resultCode);
		//System.out.println("인형합성단계 : " + output.get_output_list_id());

		L1Item selectItem = SC_ALCHEMY_DESIGN_ACK.selectOutputL1Item(output.get_output_list_id());
		if (selectItem == null)
			return selectRemovedItemTemplate();

		_resultAlchemyId = output.get_output_list_id();
		
		return selectItem;
	}

	private void doMake(L1PcInstance pc, final L1ItemInstance resultItemInstance) {
		final int resultAlchemyId = _resultAlchemyId;
		final String name = pc.getName();

		/**
		 * 0629 추가
		 */
		if (_resultCode == SC_ALCHEMY_MAKE_ACK.ResultCode.RC_HYPER_SUCCESS && resultAlchemyId >= 5
				&& !(resultItemInstance.getItemId() >= 4100007 && resultItemInstance.getItemId() <= 4100010) ) {
			resultItemInstance.setBless(0);
		}

		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (resultAlchemyId >= Config.ALCHEMY_NOTIFY_LEVEL && !ResultCode.RC_FAIL.equals(_resultCode)) {
					String message = String.format("누군가가 %s 합성에 성공 하였습니다.", resultItemInstance.getName());
					ServerBasePacket[] messages = new ServerBasePacket[] { new S_SystemMessage(message),
							new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message), };
					L1World.getInstance().broadcastPacketToAll(messages);
				}
				try {
					pc.getInventory().storeItem(resultItemInstance, resultItemInstance.getBless());
				} catch (Exception e) {
					System.out
							.println(String.format("[(인형합성)제작 결과를 받을 케릭터가 없습니다.] 캐릭터 이름 : %s, 아이템아이디 : %d, 아이템 이름 : %s",
									name, resultItemInstance.getItemId(), resultItemInstance.getName()));
					L1World.getInstance().removeObject(resultItemInstance);
				}
			}
		}, 7500L);

		SC_ALCHEMY_MAKE_ACK ack = SC_ALCHEMY_MAKE_ACK.newInstance();

		ack.set_result_code(_resultCode);
		ack.add_output_items(resultItemInstance.getId(), resultItemInstance.get_gfxid(), resultItemInstance.getBless());
		pc.sendPackets(ack, MJEProtoMessages.SC_ALCHEMY_MAKE_ACK, true);
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_alchemy_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _alchemy_id);
		if (has_inputs()) {
			for (Input val : _inputs)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_alchemy_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_inputs()) {
			for (Input val : _inputs) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_alchemy_id()) {
			output.wirteInt32(1, _alchemy_id);
		}
		if (has_inputs()) {
			for (Input val : _inputs) {
				output.writeMessage(2, val);
			}
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			default: {
				return this;
			}
			case 0x00000008: {
				set_alchemy_id(input.readInt32());
				break;
			}
			case 0x00000012: {
				add_inputs((Input) input.readMessage(Input.newInstance()));
				break;
			}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		L1PcInstance pc = null;
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			pc = clnt.getActiveChar();
			if (pc == null)
				return this;

			int alchemyId = _alchemy_id - 1;
			Alchemy alchemy = SC_ALCHEMY_DESIGN_ACK.getAlchemy(alchemyId);
			if (alchemy == null)
				return toInvalidAlchemyId(pc);

			int userInputsSize = _inputs.size();
			if (_alchemy_id == 5) {
				InputList alchemy_inputs = SC_ALCHEMY_DESIGN_ACK.getInputList(alchemyId);
				if (alchemy_inputs == null || alchemy_inputs.isInInputs(_inputs) < 1)
					return toInvalidInput(pc);
				alchemy_inputs = SC_ALCHEMY_DESIGN_ACK.getInputList(alchemyId - 1);
				if (alchemy_inputs == null || alchemy_inputs.isInInputs(_inputs) != userInputsSize - 1)
					return toInvalidInput(pc);
			} else if (_alchemy_id == 6 || _alchemy_id == 7) {
				InputList alchemy_inputs = SC_ALCHEMY_DESIGN_ACK.getInputList(alchemyId - 3);
				if (alchemy_inputs == null || alchemy_inputs.isInInputs(_inputs) < 1)
					return toInvalidInput(pc);
				alchemy_inputs = SC_ALCHEMY_DESIGN_ACK.getInputList(alchemyId - 4);
				if (alchemy_inputs == null || alchemy_inputs.isInInputs(_inputs) != userInputsSize - 1)
					return toInvalidInput(pc);
			} else {
				InputList alchemy_inputs = SC_ALCHEMY_DESIGN_ACK.getInputList(alchemyId);
				if (alchemy_inputs == null || alchemy_inputs.isInInputs(_inputs) != userInputsSize)
					return toInvalidInput(pc);
			}
			int fillCount = pc.getInventory().fillAlchemyInputItem(_inputs);
			if (fillCount != userInputsSize)
				return toInvalidInputForInventory(pc);

			_resultAlchemyId = _alchemy_id;
			_resultCode = SC_ALCHEMY_MAKE_ACK.ResultCode.RC_FAIL;
			L1Item resultItemTemplate = selectResultItem(alchemy, pc);
			int addCode = pc.getInventory().checkAddItem(resultItemTemplate, 1);
			if (addCode != L1Inventory.OK)
				return doNotAllowInventory(pc, addCode);

			remove_inputs(pc);
			doMake(pc, ItemTable.getInstance().createItem(resultItemTemplate));
		} catch (Exception e) {
			if (pc != null) {
				System.out.println(String.format("[(인형합성)예외 정보.]케릭터 이름 : %s", pc.getName()));
				SC_ALCHEMY_MAKE_ACK.ResultCode.RC_ERROR_CURRENTLY_CLOSED.sendCachedMessage(pc);
			}
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_ALCHEMY_MAKE_REQ();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_inputs()) {
			for (Input val : _inputs)
				val.dispose();
			_inputs.clear();
			_inputs = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class Input implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static Input newInstance() {
			return new Input();
		}

		private L1ItemInstance _temporaryRemovedItems;
		private int _slot_no;
		private int _item_name_id;
		private int _item_id;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private Input() {
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(32);
			sb.append("[slot:").append(_slot_no);
			sb.append(", desc:").append(_item_name_id);
			sb.append(", objectId:").append(_item_id).append("]");
			return sb.toString();
		}

		public boolean isInput(L1ItemInstance item) {
			return item.getId() == _item_id && item.getItem().getItemDescId() == _item_name_id;
		}

		public boolean hasRemoved() {
			return _temporaryRemovedItems != null;
		}

		public L1ItemInstance getTemporaryRemovedItems() {
			return _temporaryRemovedItems;
		}

		public void setTemporaryRemovedItems(L1ItemInstance item) {
			_temporaryRemovedItems = item;
		}

		public int get_slot_no() {
			return _slot_no;
		}

		public void set_slot_no(int val) {
			_bit |= 0x00000001;
			_slot_no = val;
		}

		public boolean has_slot_no() {
			return (_bit & 0x00000001) == 0x00000001;
		}

		public int get_item_name_id() {
			return _item_name_id;
		}

		public void set_item_name_id(int val) {
			_bit |= 0x00000002;
			_item_name_id = val;
		}

		public boolean has_item_name_id() {
			return (_bit & 0x00000002) == 0x00000002;
		}

		public int get_item_id() {
			return _item_id;
		}

		public void set_item_id(int val) {
			_bit |= 0x00000004;
			_item_id = val;
		}

		public boolean has_item_id() {
			return (_bit & 0x00000004) == 0x00000004;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_slot_no())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_no);
			if (has_item_name_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _item_name_id);
			if (has_item_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _item_id);
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_slot_no()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_name_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
			if (has_slot_no()) {
				output.wirteInt32(1, _slot_no);
			}
			if (has_item_name_id()) {
				output.wirteInt32(2, _item_name_id);
			}
			if (has_item_id()) {
				output.wirteInt32(3, _item_id);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try {
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					set_slot_no(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_item_name_id(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_item_id(input.readInt32());
					break;
				}
				}
			}
			return this;
		}

		@Override
		public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;
				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public MJIProtoMessage copyInstance() {
			return new Input();
		}

		@Override
		public MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
