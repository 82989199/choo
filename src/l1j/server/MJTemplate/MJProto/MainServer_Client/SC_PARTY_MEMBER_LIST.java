package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PARTY_MEMBER_LIST implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_PARTY_MEMBER_LIST newInstance(){
		return new SC_PARTY_MEMBER_LIST();
	}
	private String _leader_name;
	private java.util.LinkedList<PartyMember> _member;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PARTY_MEMBER_LIST(){
	}
	public String get_leader_name(){
		return _leader_name;
	}
	public void set_leader_name(String val){
		_bit |= 0x1;
		_leader_name = val;
	}
	public boolean has_leader_name(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<PartyMember> get_member(){
		return _member;
	}
	public void add_member(PartyMember val){
		if(!has_member()){
			_member = new java.util.LinkedList<PartyMember>();
			_bit |= 0x2;
		}
		_member.add(val);
	}
	public boolean has_member(){
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
		if (has_leader_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _leader_name);
		if (has_member()){
			for(PartyMember val : _member)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_leader_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_member()){
			for(PartyMember val : _member){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_leader_name()){
			output.writeString(1, _leader_name);
		}
		if (has_member()){
			for(PartyMember val : _member){
				output.writeMessage(2, val);
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
				case 0x0000000A:{
					set_leader_name(input.readString());
					break;
				}
				case 0x00000012:{
					add_member((PartyMember)input.readMessage(PartyMember.newInstance()));
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
		return new SC_PARTY_MEMBER_LIST();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_member()){
			for(PartyMember val : _member)
				val.dispose();
			_member.clear();
			_member = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
