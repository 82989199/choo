package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJRankSystem.Business.MJRankBusiness;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_TOP_RANKER_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_TOP_RANKER_REQ newInstance(){
		return new CS_TOP_RANKER_REQ();
	}
	private int _class_id;
	private long _version;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_TOP_RANKER_REQ(){
	}
	public int get_class_id(){
		return _class_id;
	}
	public void set_class_id(int val){
		_bit |= 0x00000001;
		_class_id = val;
	}
	public boolean has_class_id(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public long get_version(){
		return _version;
	}
	public void set_version(long val){
		_bit |= 0x00000002;
		_version = val;
	}
	public boolean has_version(){
		return (_bit & 0x00000002) == 0x00000002;
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
		if (has_class_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _class_id);
		if (has_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _version);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_class_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_version()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_class_id()){
			output.wirteInt32(1, _class_id);
		}
		if (has_version()){
			output.writeInt64(2, _version);
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
					set_class_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_version(input.readInt64());
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
			
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			MJRankBusiness.getInstance().ack(pc, _class_id, _version);
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_TOP_RANKER_REQ();
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
