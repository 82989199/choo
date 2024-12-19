package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_MESSAGE_BOX implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_MESSAGE_BOX newInstance(){
		return new SC_MESSAGE_BOX();
	}
	private int _msg_code;
	private boolean _modal;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_MESSAGE_BOX(){
		_modal = false;
	}
	public int get_msg_code(){
		return _msg_code;
	}
	public void set_msg_code(int val){
		_bit |= 0x00000001;
		_msg_code = val;
	}
	public boolean has_msg_code(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public boolean get_modal(){
		return _modal;
	}
	public void set_modal(boolean val){
		_bit |= 0x00000002;
		_modal = val;
	}
	public boolean has_modal(){
		return (_bit & 0x00000002) == 0x00000002;
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
		if (has_msg_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _msg_code);
		if (has_modal())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _modal);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_msg_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_modal()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_msg_code()){
			output.writeUInt32(1, _msg_code);
		}
		if (has_modal()){
			output.writeBool(2, _modal);
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
					set_msg_code(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_modal(input.readBool());
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
		return new SC_MESSAGE_BOX();
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
