package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_INVENTORY_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_INVENTORY_INFO_NOTI newInstance(){
		return new SC_INVENTORY_INFO_NOTI();
	}
	private java.util.LinkedList<ItemInfo> _item_infos;
	private int _owner_object_id;
	private int _total_pages;
	private int _cur_pages;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INVENTORY_INFO_NOTI(){
	}
	public java.util.LinkedList<ItemInfo> get_item_infos(){
		return _item_infos;
	}
	public void add_item_infos(ItemInfo val){
		if(!has_item_infos()){
			_item_infos = new java.util.LinkedList<ItemInfo>();
			_bit |= 0x1;
		}
		_item_infos.add(val);
	}
	public boolean has_item_infos(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_owner_object_id(){
		return _owner_object_id;
	}
	public void set_owner_object_id(int val){
		_bit |= 0x2;
		_owner_object_id = val;
	}
	public boolean has_owner_object_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_total_pages(){
		return _total_pages;
	}
	public void set_total_pages(int val){
		_bit |= 0x4;
		_total_pages = val;
	}
	public boolean has_total_pages(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_cur_pages(){
		return _cur_pages;
	}
	public void set_cur_pages(int val){
		_bit |= 0x8;
		_cur_pages = val;
	}
	public boolean has_cur_pages(){
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
		if (has_item_infos()){
			for(ItemInfo val : _item_infos)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_owner_object_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _owner_object_id);
		if (has_total_pages())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _total_pages);
		if (has_cur_pages())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _cur_pages);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_item_infos()){
			for(ItemInfo val : _item_infos){
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
		if (has_item_infos()){
			for(ItemInfo val : _item_infos){
				output.writeMessage(1, val);
			}
		}
		if (has_owner_object_id()){
			output.writeUInt32(2, _owner_object_id);
		}
		if (has_total_pages()){
			output.wirteInt32(3, _total_pages);
		}
		if (has_cur_pages()){
			output.wirteInt32(4, _cur_pages);
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
					add_item_infos((ItemInfo)input.readMessage(ItemInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_owner_object_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_total_pages(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_cur_pages(input.readInt32());
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
		return new SC_INVENTORY_INFO_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_infos()){
			for(ItemInfo val : _item_infos)
				val.dispose();
			_item_infos.clear();
			_item_infos = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
