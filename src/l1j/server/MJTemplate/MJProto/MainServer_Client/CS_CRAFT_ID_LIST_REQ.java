package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.eCraftIdListReqResultType;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_CRAFT_ID_LIST_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_CRAFT_ID_LIST_REQ newInstance(){
		return new CS_CRAFT_ID_LIST_REQ();
	}
	private int _npc_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_CRAFT_ID_LIST_REQ(){
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
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		L1PcInstance pc = null;
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			L1Object obj = L1World.getInstance().findObject(_npc_id);
			if(obj == null || !obj.instanceOf(MJL1Type.L1TYPE_NPC)){
				eCraftIdListReqResultType.RP_ERROR_INVALID_NPC_ID.sendCachedMessage(pc);
				return this;
			}
			
			if(Math.abs(pc.getX() - obj.getX()) > 15 || Math.abs(pc.getY() - obj.getY()) > 15){
				eCraftIdListReqResultType.RP_ERROR_OUT_OF_RANGE.sendCachedMessage(pc);
				return this;
			}
			
			L1NpcInstance npc = (L1NpcInstance)obj;
			ProtoOutputStream stream = SC_CRAFT_LIST_ALL_ACK.getCraftIdListFromNpcId(npc.getNpcId());
			if(stream == null){
				System.out.println(String.format("invalid npc objectId : %d(%d). from CS_CRAFT_ID_LIST_REQ!", _npc_id, npc.getNpcId()));
				return this;
			}
			
			pc.sendPackets(stream, false);
		} catch(Exception e){
			e.printStackTrace();
			if(pc != null)
				eCraftIdListReqResultType.RP_ERROR_UNKNOWN.sendCachedMessage(pc);
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_CRAFT_ID_LIST_REQ();
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
