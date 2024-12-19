package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PARTY_SPELL_AVATAR_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_PARTY_SPELL_AVATAR_NOTI newInstance(){
		return new SC_PARTY_SPELL_AVATAR_NOTI();
	}
	private int _spell_id;
	private boolean _active_flag;
	private int _icon_id;
	private int _desc_index;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PARTY_SPELL_AVATAR_NOTI(){
	}
	public int get_spell_id(){
		return _spell_id;
	}
	public void set_spell_id(int val){
		_bit |= 0x1;
		_spell_id = val;
	}
	public boolean has_spell_id(){
		return (_bit & 0x1) == 0x1;
	}
	public boolean get_active_flag(){
		return _active_flag;
	}
	public void set_active_flag(boolean val){
		_bit |= 0x2;
		_active_flag = val;
	}
	public boolean has_active_flag(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_icon_id(){
		return _icon_id;
	}
	public void set_icon_id(int val){
		_bit |= 0x4;
		_icon_id = val;
	}
	public boolean has_icon_id(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_desc_index(){
		return _desc_index;
	}
	public void set_desc_index(int val){
		_bit |= 0x8;
		_desc_index = val;
	}
	public boolean has_desc_index(){
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
		if (has_spell_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _spell_id);
		if (has_active_flag())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _active_flag);
		if (has_icon_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _icon_id);
		if (has_desc_index())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _desc_index);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_spell_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_active_flag()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_icon_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_desc_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_spell_id()){
			output.writeUInt32(1, _spell_id);
		}
		if (has_active_flag()){
			output.writeBool(2, _active_flag);
		}
		if (has_icon_id()){
			output.wirteInt32(3, _icon_id);
		}
		if (has_desc_index()){
			output.wirteInt32(4, _desc_index);
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
					set_spell_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_active_flag(input.readBool());
					break;
				}
				case 0x00000018:{
					set_icon_id(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_desc_index(input.readInt32());
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
		return new SC_PARTY_SPELL_AVATAR_NOTI();
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
