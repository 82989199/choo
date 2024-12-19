package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CRAFT_BATCH_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_CRAFT_BATCH_ACK newInstance(){
		return new SC_CRAFT_BATCH_ACK();
	}
	private eCraftBatchAckResultType _eResult;
	private int _batch_transaction_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_CRAFT_BATCH_ACK(){
	}
	public eCraftBatchAckResultType get_eResult(){
		return _eResult;
	}
	public void set_eResult(eCraftBatchAckResultType val){
		_bit |= 0x00000001;
		_eResult = val;
	}
	public boolean has_eResult(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public int get_batch_transaction_id(){
		return _batch_transaction_id;
	}
	public void set_batch_transaction_id(int val){
		_bit |= 0x00000002;
		_batch_transaction_id = val;
	}
	public boolean has_batch_transaction_id(){
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
		if (has_eResult())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _eResult.toInt());
		if (has_batch_transaction_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _batch_transaction_id);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_eResult()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_batch_transaction_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(MJEProtoMessages message){
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
		if (has_eResult()){
			output.writeEnum(1, _eResult.toInt());
		}
		if (has_batch_transaction_id()){
			output.wirteInt32(2, _batch_transaction_id);
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
					set_eResult(eCraftBatchAckResultType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_batch_transaction_id(input.readInt32());
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
		return new SC_CRAFT_BATCH_ACK();
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
	public enum eCraftBatchAckResultType{
		RP_SUCCESS(0),
		RP_FAILURE(1);
		private int value;
		eCraftBatchAckResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCraftBatchAckResultType v){
			return value == v.value;
		}
		public static eCraftBatchAckResultType fromInt(int i){
			switch(i){
			case 0:
				return RP_SUCCESS;
			case 1:
				return RP_FAILURE;
			default:
				return null;
			}
		}
	}
}
