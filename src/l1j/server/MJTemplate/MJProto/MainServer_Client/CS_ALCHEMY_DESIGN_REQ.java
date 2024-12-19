package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJHexHelper;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ALCHEMY_DESIGN_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_ALCHEMY_DESIGN_REQ newInstance(){
		return new CS_ALCHEMY_DESIGN_REQ();
	}
	private byte[] _hash_value;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_ALCHEMY_DESIGN_REQ(){
	}
	public byte[] get_hash_value(){
		return _hash_value;
	}
	public void set_hash_value(byte[] val){
		_bit |= 0x00000001;
		_hash_value = val;
	}
	public boolean has_hash_value(){
		return (_bit & 0x00000001) == 0x00000001;
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
		if (has_hash_value())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _hash_value);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_hash_value()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_hash_value()){
			output.writeBytes(1, _hash_value);
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
					set_hash_value(input.readBytes());
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
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			if(pc.isGm())
				System.out.println(MJHexHelper.toSourceString(_hash_value, _hash_value.length));
			
			SC_ALCHEMY_EXTRA_INFO_NOTI.send(pc);
			SC_ALCHEMY_DESIGN_ACK.send(pc, _hash_value);
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_ALCHEMY_DESIGN_REQ();
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
