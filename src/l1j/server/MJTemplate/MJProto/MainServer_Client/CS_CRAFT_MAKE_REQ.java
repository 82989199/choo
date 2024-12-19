package l1j.server.MJTemplate.MJProto.MainServer_Client;


import java.io.IOException;
import java.util.LinkedList;

import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK.Item;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.Craft;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.Craft.CraftInputList;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.Craft.CraftInputList.CraftInputItem;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.Craft.CraftOutputList.CraftOutputSFList.CraftOutputItem;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.CraftOutputItemResult;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_MAKE_ACK.eCraftMakeReqResultType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_CRAFT_MAKE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static final int CRAFT_ITEM_SUCCESS_MILLIONS = 1000000;
	
	public static CS_CRAFT_MAKE_REQ newInstance(){
		return new CS_CRAFT_MAKE_REQ();
	}
	private int _npc_id;
	private int _craft_id;
	private int _count;
	private LinkedList<CraftInputItemSlotInfo> _slotInfo;
	private int _batch_transaction_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_CRAFT_MAKE_REQ(){
	}
	public int get_npc_id(){
		return _npc_id;
	}
	public void set_npc_id(int val){
		_bit |= 0x00000001;
		_npc_id = val;
	}
	public boolean has_npc_id(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public int get_craft_id(){
		return _craft_id;
	}
	public void set_craft_id(int val){
		_bit |= 0x00000002;
		_craft_id = val;
	}
	public boolean has_craft_id(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x00000004;
		_count = val;
	}
	public boolean has_count(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public LinkedList<CraftInputItemSlotInfo> get_slotInfo(){
		return _slotInfo;
	}
	public void add_slotInfo(CraftInputItemSlotInfo val){
		if(!has_slotInfo()){
			_slotInfo = new LinkedList<CraftInputItemSlotInfo>();
			_bit |= 0x00000008;
		}
		_slotInfo.add(val);
	}
	public boolean has_slotInfo(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public int get_batch_transaction_id(){
		return _batch_transaction_id;
	}
	public void set_batch_transaction_id(int val){
		_bit |= 0x00000010;
		_batch_transaction_id = val;
	}
	public boolean has_batch_transaction_id(){
		return (_bit & 0x00000010) == 0x00000010;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_npc_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _npc_id);
		if (has_craft_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _craft_id);
		if (has_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _count);
		if (has_slotInfo()){
			for(CraftInputItemSlotInfo val : _slotInfo)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
		}
		if (has_batch_transaction_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _batch_transaction_id);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_npc_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_craft_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_slotInfo()){
			for(CraftInputItemSlotInfo val : _slotInfo){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
				ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_npc_id()){
			output.wirteInt32(1, _npc_id);
		}
		if (has_craft_id()){
			output.wirteInt32(2, _craft_id);
		}
		if (has_count()){
			output.wirteInt32(3, _count);
		}
		if (has_slotInfo()){
			for(CraftInputItemSlotInfo val : _slotInfo){
				output.writeMessage(4, val);
			}
		}
		if (has_batch_transaction_id()){
			output.wirteInt32(5, _batch_transaction_id);
		}
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x00000008:{
					set_npc_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_craft_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_count(input.readInt32());
					break;
				}
				case 0x00000022:{
					add_slotInfo((CraftInputItemSlotInfo)input.readMessage(CraftInputItemSlotInfo.newInstance()));
					break;
				}
				case 0x00000028:{
					set_batch_transaction_id(input.readInt32());
					break;
				}
			}
		}
		return this;
	}
	
	private eCraftMakeReqResultType doMake(L1PcInstance pc, CraftOutputItemResult outputResult){
		int removedCount = pc.getInventory().fillCraftInputItem(_slotInfo);		
		if(removedCount < _slotInfo.size()){
			System.out.println(String.format("[(제작)유저의 인벤토리에서 재료 정보를 찾을 수 없습니다.] 캐릭터이름 : %s, 제작아이디 : %d, 네임아이디 : %d, 찾은 수 : %d, 찾아야할 수 : %d", pc.getName(), get_craft_id(), craft.get_craft_attr().get_desc(), removedCount, _slotInfo.size()));
			return eCraftMakeReqResultType.RP_ERROR_NO_REQUIRED_ITEM;
		}
		
		// craft output check.
		CraftOutputItem outputItem = outputResult.outputItem;
		if(outputItem == null){
			if(outputResult.isSuccess){
				System.out.println(String.format("[(제작)출력 아이템을 찾을 수 없습니다.] 캐릭터이름 : %s, 제작아이디 : %d, 네임아이디 : %d", pc.getName(), get_craft_id(), craft.get_craft_attr().get_desc()));
				return eCraftMakeReqResultType.RP_ERROR_SUCCESS_COUNT_EXCEED;
			}
			eType = eValue = 0;
			for(CraftInputItemSlotInfo sInfo : _slotInfo)
				sInfo.doRemoved(pc.getInventory());
			return eCraftMakeReqResultType.RP_FAILURE;
		}
		
		// item DB check.
		int inheritItemElemental_descId = outputItem.get_inherit_elemental_enchant_from();
		L1Item l1item = ItemTable.getInstance().findCraftCachedItem(outputItem);
		if(l1item == null){
			eCraftMakeReqResultType.RP_ERROR_BLOCKED_CRAFT_ID.sendCachedMessage(pc);
			System.out.println(String.format("[(제작)출력 아이템에 해당하는 서버 아이템을 찾을 수 없습니다.] 캐릭터이름 : %s, 제작아이디 : %d, 네임아이디 : %d, 데스크아이디 : %d", pc.getName(), get_craft_id(), craft.get_craft_attr().get_desc(), outputItem.get_name_id()));
			return eCraftMakeReqResultType.RP_ERROR_BLOCKED_CRAFT_ID;
		}			
		
		// inventory status check.
		int addCode = pc.getInventory().checkAddItem(l1item, outputItem.get_count());
		if(addCode != L1Inventory.OK)
			return (addCode == L1Inventory.WEIGHT_OVER ? eCraftMakeReqResultType.RP_ERROR_WEIGHT_OVER : eCraftMakeReqResultType.RP_ERROR_INVEN_OVER);
		
		// success.
		L1ItemInstance result = ItemTable.getInstance().createItem(l1item);
		result.setEnchantLevel(outputItem.get_enchant());
		result.setBless(outputItem.get_bless());
		result.setCount(outputItem.get_count());
		result.setIdentified(true);
		eType = eValue = 0;
		for(CraftInputItemSlotInfo sInfo : _slotInfo){
			if(inheritItemElemental_descId != 0 && inheritItemElemental_descId == sInfo.get_item_name_id()){
				eType = sInfo.get_elemental_enchant_type();
				eValue = sInfo.get_elemental_enchant_value();
				result.setAttrEnchantLevel(L1ItemInstance.calculateElementalEnchant(eType, eValue));
				inheritItemElemental_descId = 0;
			}
			sInfo.doRemoved(pc.getInventory());
		}
		//TODO 제작 성공 메세지 출력
//		if (get_craft_id() >= 30227 && get_craft_id() <= 30231 || get_craft_id() >= 4934 && get_craft_id() <= 4949 && !pc.isGm()) {
//			String message = String.format("" + pc.getName() + " 님께서 %s 제작에 성공하였습니다.", result.getLogName());
//			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
//			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
//		}
		//-- outputItem 속성 초기화 문제
		if (!outputResult.isSuccess) {
			for (CraftInputItemSlotInfo sInfo : _slotInfo) {
				int type = sInfo.get_elemental_enchant_type(), value = sInfo.get_elemental_enchant_value();
				if (type != 0 && value != 0) {
					result.setAttrEnchantLevel(L1ItemInstance.calculateElementalEnchant(type, value));
				}
			}
		}
		
		pc.getInventory().storeItem(result);
		return outputResult.isSuccess ? eCraftMakeReqResultType.RP_SUCCESS : eCraftMakeReqResultType.RP_FAILURE;
	}
	
	private Craft craft;
	private int eType;
	private int eValue;
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		L1PcInstance pc = null;
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			if(_count <= 0){
				System.out.println(String.format("[(제작)제작 수량이 비정상적입니다.] 캐릭터이름 : %s, 제작아이디 : %d, 수량 : %d", pc.getName(), get_craft_id(), _count));
				return this;
			}
			
			// npc check.
			L1Object obj = L1World.getInstance().findObject(_npc_id);
			if(obj == null || !obj.instanceOf(MJL1Type.L1TYPE_NPC)){
				eCraftMakeReqResultType.RP_ERROR_INVALID_NPC.sendCachedMessage(pc);
				return this;
			}
			
			// npc range check.
			if(Math.abs(pc.getX() - obj.getX()) > 15 || Math.abs(pc.getY() - obj.getY()) > 15){
				eCraftMakeReqResultType.RP_ERROR_NPC_OUT_OF_RANGE.sendCachedMessage(pc);
				return this;
			}
			
			// craft info check.
			craft = SC_CRAFT_LIST_ALL_ACK.getCraft(get_craft_id());
			if(craft == null){
				eCraftMakeReqResultType.RP_ERROR_CRAFT_DOES_NOT_EXIST.sendCachedMessage(pc);
				System.out.println(String.format("[(제작)제작 아이디를 찾을 수 없습니다.] 캐릭터이름 : %s, 제작아이디 : %d", pc.getName(), get_craft_id()));
				return this;
			}
			
			// craft input check.
			CraftInputList inputs = craft.get_inputs();
			if(!inputs.has_arr_input_item()){
				eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT.sendCachedMessage(pc);
				System.out.println(String.format("[(제작)제작 재료 아이템 리스트를 찾을 수 없습니다.] 캐릭터이름 : %s, 제작아이디 : %d, 네임아이디 : $%d", pc.getName(), get_craft_id(), craft.get_craft_attr().get_desc()));
				return this;
			}
			
			// craft input slot check.
			int[] calcSlotAndIncreaseProb = inputs.calculateSlotAndIncreaseProb(_slotInfo);
			if(calcSlotAndIncreaseProb[0] - 1 != _slotInfo.size()){
				eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT.sendCachedMessage(pc);
				System.out.println(String.format("[(제작)재료 슬롯 정보가 일치하지 않습니다.] 캐릭터이름 : %s, 제작아이디 : %d, 네임아이디: %d, 슬롯번호 : %d, 유저가 보내온 패킷 슬롯 수 : %d", pc.getName(), get_craft_id(), craft.get_craft_attr().get_desc(), calcSlotAndIncreaseProb[0] - 1, _slotInfo.size()));
				return this;	
			}
		
			for(int i=0; i<_count; ++i){
				CraftOutputItemResult outputResult = craft.get_outputs().createOutputItem(calcSlotAndIncreaseProb[1], get_craft_id());
				eCraftMakeReqResultType eResult = doMake(pc, outputResult);
				if(eResult.equals(eCraftMakeReqResultType.RP_SUCCESS)){
					SC_CRAFT_MAKE_ACK.sendResultPacket(pc, eResult, outputResult.outputItem.toMakeOutputItem(eType, eValue), i + 1, _count);
					pc.sendPackets(new S_SkillSound(pc.getId(), 2029), true);

				} else if(eResult.equals(eCraftMakeReqResultType.RP_FAILURE)){
					SC_CRAFT_MAKE_ACK.sendResultPacket(pc, eResult, null, i + 1, _count);
				}else{
					eResult.sendCachedMessage(pc);
					break;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			if(pc != null){
				System.out.println(String.format("케릭터 이름 : %s", pc.getName()));
				eCraftMakeReqResultType.RP_ERROR_TOO_MANY_MAKE_REQ.sendCachedMessage(pc);
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_CRAFT_MAKE_REQ();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_slotInfo()){
			for(CraftInputItemSlotInfo val : _slotInfo)
				val.dispose();
			_slotInfo.clear();
			_slotInfo = null;
		}
		craft = null;
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class CraftInputItemSlotInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CraftInputItemSlotInfo newInstance(){
			return new CraftInputItemSlotInfo();
		}
		private int _input_slot_no;
		private int _item_name_id;
		private int _count;
		private int _enchant;
		private int _elemental_enchant_type;
		private int _elemental_enchant_value;
		private int _bless;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private LinkedList<TemporaryRemovedItemInstance> 	_temporaryRemovedItems;
		private int	_temporaryRemovedCount;
		
		public boolean isInput(L1ItemInstance item){
			if(_item_name_id != item.getItem().getItemDescId())
				return false;
			
			if(_bless != 3 && _bless != item.getBless())
				return false;
			
			if(_enchant != item.getEnchantLevel())
				return false;
			
			if(item.isStackable()){
				if(_count > item.getCount())
					return false;
			}

			if(_elemental_enchant_type >= 1 && _elemental_enchant_type <= 4)
				return L1ItemInstance.equalsElement(item, _elemental_enchant_type, _elemental_enchant_value);
			return true;
		}
		
		public void addTemporaryRemovedItems(L1ItemInstance item, int count){
			if(_temporaryRemovedItems == null)
				_temporaryRemovedItems = new LinkedList<TemporaryRemovedItemInstance>();
			_temporaryRemovedItems.add(new TemporaryRemovedItemInstance(item, count));
			_temporaryRemovedCount += count;
		}
		
		public boolean hasRemoved(){
			return _temporaryRemovedCount >= _count;
		}
		
		public void doRemoved(L1Inventory inv){
			for(TemporaryRemovedItemInstance removed : _temporaryRemovedItems){
				if(_temporaryRemovedCount <= 0)
					break;
				
				int cnt = removed.item.getCount();
				if(removed.item.isStackable()){
					if(cnt > removed.count)
						cnt = removed.count;
				}
				inv.removeItem(removed.item, cnt);
				_temporaryRemovedCount -= cnt;
			}
			clearRemoved();
		}
		
		public void clearRemoved(){
			if(_temporaryRemovedItems != null)
				_temporaryRemovedItems.clear();
			_temporaryRemovedCount = 0;
		}
		
		public boolean validation(CraftInputItem input){
			if(input.get_name_id() != _item_name_id)
				return false;
			
			int bless = input.get_bless();
			if(bless != 3 && bless != _bless)
				return false;
			_bless = bless;
			
			if(!input.get_all_enchants_allowed() && input.get_enchant() != _enchant)
				return false;
			
			int elementalType = input.get_elemental_enchant_type();
			if(elementalType >= 1 && elementalType <= 4){
				if(elementalType != _elemental_enchant_type || input.get_elemental_enchant_value() != _elemental_enchant_value)
					return false;
			}
			return true;
		}
		
		private CraftInputItemSlotInfo(){
		}
		public int get_input_slot_no(){
			return _input_slot_no;
		}
		public void set_input_slot_no(int val){
			_bit |= 0x00000001;
			_input_slot_no = val;
		}
		public boolean has_input_slot_no(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		public int get_item_name_id(){
			return _item_name_id;
		}
		public void set_item_name_id(int val){
			_bit |= 0x00000002;
			_item_name_id = val;
		}
		public boolean has_item_name_id(){
			return (_bit & 0x00000002) == 0x00000002;
		}
		public int get_count(){
			return _count;
		}
		public void set_count(int val){
			_bit |= 0x00000004;
			_count = val;
		}
		public boolean has_count(){
			return (_bit & 0x00000004) == 0x00000004;
		}
		public int get_enchant(){
			return _enchant;
		}
		public void set_enchant(int val){
			_bit |= 0x00000008;
			_enchant = val;
		}
		public boolean has_enchant(){
			return (_bit & 0x00000008) == 0x00000008;
		}
		public int get_elemental_enchant_type(){
			return _elemental_enchant_type;
		}
		public void set_elemental_enchant_type(int val){
			_bit |= 0x00000010;
			_elemental_enchant_type = val;
		}
		public boolean has_elemental_enchant_type(){
			return (_bit & 0x00000010) == 0x00000010;
		}
		public int get_elemental_enchant_value(){
			return _elemental_enchant_value;
		}
		public void set_elemental_enchant_value(int val){
			_bit |= 0x00000020;
			_elemental_enchant_value = val;
		}
		public boolean has_elemental_enchant_value(){
			return (_bit & 0x00000020) == 0x00000020;
		}
		public int get_bless(){
			return _bless;
		}
		public void set_bless(int val){
			_bit |= 0x00000040;
			_bless = val;
		}
		public boolean has_bless(){
			return (_bit & 0x00000040) == 0x00000040;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_input_slot_no())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _input_slot_no);
			if (has_item_name_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _item_name_id);
			if (has_count())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _count);
			if (has_enchant())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _enchant);
			if (has_elemental_enchant_type())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _elemental_enchant_type);
			if (has_elemental_enchant_value())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _elemental_enchant_value);
			if (has_bless())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _bless);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_input_slot_no()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_name_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
					ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try {
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_input_slot_no()){
				output.wirteInt32(1, _input_slot_no);
			}
			if (has_item_name_id()){
				output.wirteInt32(2, _item_name_id);
			}
			if (has_count()){
				output.wirteInt32(3, _count);
			}
			if (has_enchant()){
				output.wirteInt32(4, _enchant);
			}
			if (has_elemental_enchant_type()){
				output.wirteInt32(5, _elemental_enchant_type);
			}
			if (has_elemental_enchant_value()){
				output.wirteInt32(6, _elemental_enchant_value);
			}
			if (has_bless()){
				output.wirteInt32(7, _bless);
			}
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					default:{
						return this;
					}
					case 0x00000008:{
						set_input_slot_no(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_item_name_id(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_count(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_enchant(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_elemental_enchant_type(input.readInt32());
						break;
					}
					case 0x00000030:{
						set_elemental_enchant_value(input.readInt32());
						break;
					}
					case 0x00000038:{
						set_bless(input.readInt32());
						break;
					}
				}
			}
			return this;
		}
		@Override
		public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try{
				readFrom(is);

				if (!isInitialized())
					return this;
				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch(Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public MJIProtoMessage copyInstance(){
			return new CraftInputItemSlotInfo();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
			clearRemoved();
		}
	}
	
	public static class TemporaryRemovedItemInstance{
		public L1ItemInstance item;
		public int count;
		public TemporaryRemovedItemInstance(L1ItemInstance item, int count){
			this.item = item;
			this.count = count;
		}
		
	}
}
