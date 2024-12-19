package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PARTY_MEMBER_STATUS implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_PARTY_MEMBER_STATUS newInstance(){
		return new SC_PARTY_MEMBER_STATUS();
	}
	private String _name;
	private int _object_id;
	private int _hp_ratio;
	private int _mp_ratio;
	private int _status;
	private int _world;
	private int _location;
	private int _mark;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PARTY_MEMBER_STATUS(){
	}
	public String get_name(){
		return _name;
	}
	public void set_name(String val){
		_bit |= 0x1;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_object_id(){
		return _object_id;
	}
	public void set_object_id(int val){
		_bit |= 0x2;
		_object_id = val;
	}
	public boolean has_object_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_hp_ratio(){
		return _hp_ratio;
	}
	public void set_hp_ratio(int val){
		_bit |= 0x4;
		_hp_ratio = val;
	}
	public boolean has_hp_ratio(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_mp_ratio(){
		return _mp_ratio;
	}
	public void set_mp_ratio(int val){
		_bit |= 0x8;
		_mp_ratio = val;
	}
	public boolean has_mp_ratio(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_status(){
		return _status;
	}
	public void set_status(int val){
		_bit |= 0x10;
		_status = val;
	}
	public boolean has_status(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_world(){
		return _world;
	}
	public void set_world(int val){
		_bit |= 0x20;
		_world = val;
	}
	public boolean has_world(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_location(){
		return _location;
	}
	public void set_location(int val){
		_bit |= 0x40;
		_location = val;
	}
	public boolean has_location(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_mark(){
		return _mark;
	}
	public void set_mark(int val){
		_bit |= 0x80;
		_mark = val;
	}
	public boolean has_mark(){
		return (_bit & 0x80) == 0x80;
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
		if (has_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _name);
		if (has_object_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _object_id);
		if (has_hp_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _hp_ratio);
		if (has_mp_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _mp_ratio);
		if (has_status())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _status);
		if (has_world())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _world);
		if (has_location())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _location);
		if (has_mark())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _mark);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_name()){
			output.writeString(1, _name);
		}
		if (has_object_id()){
			output.writeUInt32(2, _object_id);
		}
		if (has_hp_ratio()){
			output.wirteInt32(3, _hp_ratio);
		}
		if (has_mp_ratio()){
			output.wirteInt32(4, _mp_ratio);
		}
		if (has_status()){
			output.wirteInt32(5, _status);
		}
		if (has_world()){
			output.wirteInt32(6, _world);
		}
		if (has_location()){
			output.wirteInt32(7, _location);
		}
		if (has_mark()){
			output.wirteInt32(8, _mark);
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
					set_name(input.readString());
					break;
				}
				case 0x00000010:{
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_hp_ratio(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_mp_ratio(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_status(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_world(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_location(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_mark(input.readInt32());
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
		return new SC_PARTY_MEMBER_STATUS();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
