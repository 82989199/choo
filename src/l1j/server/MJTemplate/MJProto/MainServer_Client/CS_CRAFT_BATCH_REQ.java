package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_BATCH_ACK.eCraftBatchAckResultType;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_CRAFT_BATCH_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_CRAFT_BATCH_REQ newInstance(){
		return new CS_CRAFT_BATCH_REQ();
	}
	private int _npc_id;
	private int _craft_id;
	private int _count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_CRAFT_BATCH_REQ(){
	}
	public int get_npc_id(){
		return _npc_id;
	}
	public void set_npc_id(int val){
		_bit |= 0x00000001;
		_npc_id = val;
	}
	public boolean has_npc_id(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public int get_craft_id(){
		return _craft_id;
	}
	public void set_craft_id(int val){
		_bit |= 0x00000002;
		_craft_id = val;
	}
	public boolean has_craft_id(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x00000004;
		_count = val;
	}
	public boolean has_count(){
		return (_bit & 0x00000004) == 0x00000004;
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
		if (has_npc_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _npc_id);
		if (has_craft_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _craft_id);
		if (has_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _count);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_npc_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_craft_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_count()){
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
		if (has_npc_id()){
			output.wirteInt32(1, _npc_id);
		}
		if (has_craft_id()){
			output.wirteInt32(2, _craft_id);
		}
		if (has_count()){
			output.wirteInt32(3, _count);
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
					set_npc_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_craft_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_count(input.readInt32());
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
			
			SC_CRAFT_BATCH_ACK ack = (SC_CRAFT_BATCH_ACK) MJEProtoMessages.SC_CRAFT_BATCH_ACK.copyInstance();
			ack.set_eResult(eCraftBatchAckResultType.RP_SUCCESS);
			ack.set_batch_transaction_id(SC_CRAFT_LIST_ALL_ACK.BATCH_TRANSACTION++);
			pc.sendPackets(ack, MJEProtoMessages.SC_CRAFT_BATCH_ACK.toInt(), true);
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_CRAFT_BATCH_REQ();
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
