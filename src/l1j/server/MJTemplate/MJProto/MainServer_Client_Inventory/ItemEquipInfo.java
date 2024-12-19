package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class ItemEquipInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ItemEquipInfo newInstance(){
		return new ItemEquipInfo();
	}
	private int _object_id;
	private int _equip_position;
	private boolean _is_equiped;
	private ItemInfo _item_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ItemEquipInfo(){
		set_is_equiped(false);
	}
	public int get_object_id(){
		return _object_id;
	}
	public void set_object_id(int val){
		_bit |= 0x1;
		_object_id = val;
	}
	public boolean has_object_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_equip_position(){
		return _equip_position;
	}
	public void set_equip_position(int val){
		_bit |= 0x2;
		_equip_position = val;
	}
	public boolean has_equip_position(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean get_is_equiped(){
		return _is_equiped;
	}
	public void set_is_equiped(boolean val){
		_bit |= 0x4;
		_is_equiped = val;
	}
	public boolean has_is_equiped(){
		return (_bit & 0x4) == 0x4;
	}
	public ItemInfo get_item_info(){
		return _item_info;
	}
	public void set_item_info(ItemInfo val){
		_bit |= 0x8;
		_item_info = val;
	}
	public boolean has_item_info(){
		return (_bit & 0x8) == 0x8;
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
		if (has_object_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _object_id);
		if (has_equip_position())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _equip_position);
		if (has_is_equiped())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _is_equiped);
		if (has_item_info())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _item_info);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_object_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_equip_position()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_object_id()){
			output.writeUInt32(1, _object_id);
		}
		if (has_equip_position()){
			output.wirteInt32(2, _equip_position);
		}
		if (has_is_equiped()){
			output.writeBool(3, _is_equiped);
		}
		if (has_item_info()){
			output.writeMessage(4, _item_info);
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
				case 0x00000008:{
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_equip_position(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_is_equiped(input.readBool());
					break;
				}
				case 0x00000022:{
					set_item_info((ItemInfo)input.readMessage(ItemInfo.newInstance()));
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
		return new ItemEquipInfo();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_info() && _item_info != null){
			_item_info.dispose();
			_item_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
