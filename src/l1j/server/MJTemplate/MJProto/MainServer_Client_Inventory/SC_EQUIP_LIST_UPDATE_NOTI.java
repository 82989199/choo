package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EQUIP_LIST_UPDATE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_EQUIP_LIST_UPDATE_NOTI newInstance(){
		return new SC_EQUIP_LIST_UPDATE_NOTI();
	}
	private java.util.LinkedList<ItemEquipInfo> _item_equip_infos;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_EQUIP_LIST_UPDATE_NOTI(){
	}
	public java.util.LinkedList<ItemEquipInfo> get_item_equip_infos(){
		return _item_equip_infos;
	}
	public void add_item_equip_infos(ItemEquipInfo val){
		if(!has_item_equip_infos()){
			_item_equip_infos = new java.util.LinkedList<ItemEquipInfo>();
			_bit |= 0x1;
		}
		_item_equip_infos.add(val);
	}
	public boolean has_item_equip_infos(){
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
		if (has_item_equip_infos()){
			for(ItemEquipInfo val : _item_equip_infos)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_item_equip_infos()){
			for(ItemEquipInfo val : _item_equip_infos){
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
		if (has_item_equip_infos()){
			for(ItemEquipInfo val : _item_equip_infos){
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
					add_item_equip_infos((ItemEquipInfo)input.readMessage(ItemEquipInfo.newInstance()));
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
		return new SC_EQUIP_LIST_UPDATE_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_equip_infos()){
			for(ItemEquipInfo val : _item_equip_infos)
				val.dispose();
			_item_equip_infos.clear();
			_item_equip_infos = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
