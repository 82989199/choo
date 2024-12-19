package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ALCHEMY_EXTRA_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc){
		Map<Integer, L1DollInstance> dolls = pc.getDollList();
		if(dolls.size() <= 0)
			return;
		
		SC_ALCHEMY_EXTRA_INFO_NOTI noti = newInstance();
		for(L1DollInstance doll : dolls.values())
			noti.add_summoned_petball_item_id(doll.getItemObjId());
		pc.sendPackets(noti, MJEProtoMessages.SC_ALCHEMY_EXTRA_INFO_NOTI, true);
	}
	
	public static SC_ALCHEMY_EXTRA_INFO_NOTI newInstance(){
		return new SC_ALCHEMY_EXTRA_INFO_NOTI();
	}
	private LinkedList<Integer> _summoned_petball_item_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ALCHEMY_EXTRA_INFO_NOTI(){
	}
	public LinkedList<Integer> get_summoned_petball_item_id(){
		return _summoned_petball_item_id;
	}
	public void add_summoned_petball_item_id(int val){
		if(!has_summoned_petball_item_id()){
			_summoned_petball_item_id = new LinkedList<Integer>();
			_bit |= 0x00000001;
		}
		_summoned_petball_item_id.add(val);
	}
	public boolean has_summoned_petball_item_id(){
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
		if (has_summoned_petball_item_id()){
			for(int val : _summoned_petball_item_id)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_summoned_petball_item_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_summoned_petball_item_id()){
			for(int val : _summoned_petball_item_id){
				output.wirteInt32(1, val);
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
				case 0x00000008:{
					add_summoned_petball_item_id(input.readInt32());
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
		return new SC_ALCHEMY_EXTRA_INFO_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_summoned_petball_item_id()){
			_summoned_petball_item_id.clear();
			_summoned_petball_item_id = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
