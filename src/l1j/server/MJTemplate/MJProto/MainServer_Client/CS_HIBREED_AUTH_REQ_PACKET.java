package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;

import MJShiftObject.MJShiftObjectManager;
import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_HIBREED_AUTH_REQ_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_HIBREED_AUTH_REQ_PACKET newInstance(){
		return new CS_HIBREED_AUTH_REQ_PACKET();
	}
	private long _reservednumber;
	private String _onetimetoken;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_HIBREED_AUTH_REQ_PACKET(){
	}
	public long get_reservednumber(){
		return _reservednumber;
	}
	public void set_reservednumber(long val){
		_bit |= 0x1;
		_reservednumber = val;
	}
	public boolean has_reservednumber(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_onetimetoken(){
		return _onetimetoken;
	}
	public void set_onetimetoken(String val){
		_bit |= 0x2;
		_onetimetoken = val;
	}
	public boolean has_onetimetoken(){
		return (_bit & 0x2) == 0x2;
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
		if (has_reservednumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(1, _reservednumber);
		if (has_onetimetoken())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _onetimetoken);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_reservednumber()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_onetimetoken()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_reservednumber()){
			output.wirteUInt64(1, _reservednumber);
		}
		if (has_onetimetoken()){
			output.writeString(2, _onetimetoken);
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
					set_reservednumber(input.readUInt64());
					break;
				}
				case 0x00000012:{
					set_onetimetoken(input.readString());
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
			if(Config.USE_SHIFT_SERVER)
				MJShiftObjectManager.getInstance().do_receive(clnt, (int)get_reservednumber(), get_onetimetoken());
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_HIBREED_AUTH_REQ_PACKET();
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
