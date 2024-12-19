package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ItemName;

import java.io.IOException;

import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_UPDATE_INVENTORY_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send_companion_control_item_name(L1PcInstance pc, int item_object_id, String class_npc_name_id, int level){
		String description = String.format("펫 목걸이 (%s LV %d)", class_npc_name_id, level);
		pc.sendPackets(new S_ItemName(item_object_id, description));
		
	}
	
	public static void send_companion_control_item(L1PcInstance pc, int item_object_id, MJCompanionInstanceCache cache){
		if(pc == null)
			return;
		
		ItemInfo iInfo = ItemInfo.newInstance(item_object_id, cache);
		SC_UPDATE_INVENTORY_NOTI noti = SC_UPDATE_INVENTORY_NOTI.newInstance();
		noti.add_item_info(iInfo);
		pc.sendPackets(noti, MJEProtoMessages.SC_UPDATE_INVENTORY_NOTI, true);
	}
	
	public static SC_UPDATE_INVENTORY_NOTI newInstance(){
		return new SC_UPDATE_INVENTORY_NOTI();
	}
	private java.util.LinkedList<ItemInfo> _item_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_UPDATE_INVENTORY_NOTI(){
	}
	public java.util.LinkedList<ItemInfo> get_item_info(){
		return _item_info;
	}
	public void add_item_info(ItemInfo val){
		if(!has_item_info()){
			_item_info = new java.util.LinkedList<ItemInfo>();
			_bit |= 0x1;
		}
		_item_info.add(val);
	}
	public boolean has_item_info(){
		return (_bit & 0x1) == 0x1;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_item_info()){
			for(ItemInfo val : _item_info)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_item_info()){
			for(ItemInfo val : _item_info){
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_item_info()){
			for(ItemInfo val : _item_info){
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x0000000A:{
					add_item_info((ItemInfo)input.readMessage(ItemInfo.newInstance()));
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
		return new SC_UPDATE_INVENTORY_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_info()){
			for(ItemInfo val : _item_info)
				val.dispose();
			_item_info.clear();
			_item_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
