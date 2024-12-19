package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_PARTY_MEMBER_LIST_CHANGE implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_PARTY_MEMBER_LIST_CHANGE newInstance(){
		return new SC_PARTY_MEMBER_LIST_CHANGE();
	}
	private String _leader_name;
	private String _out_user;
	private String _goto_inter_server_user;
	private PartyMember _new_user;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_PARTY_MEMBER_LIST_CHANGE(){
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
	public String get_out_user(){
		return _out_user;
	}
	public void set_out_user(String val){
		_bit |= 0x2;
		_out_user = val;
	}
	public boolean has_out_user(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_goto_inter_server_user(){
		return _goto_inter_server_user;
	}
	public void set_goto_inter_server_user(String val){
		_bit |= 0x4;
		_goto_inter_server_user = val;
	}
	public boolean has_goto_inter_server_user(){
		return (_bit & 0x4) == 0x4;
	}
	public PartyMember get_new_user(){
		return _new_user;
	}
	public void set_new_user(PartyMember val){
		_bit |= 0x8;
		_new_user = val;
	}
	public boolean has_new_user(){
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
		if (has_leader_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _leader_name);
		if (has_out_user())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _out_user);
		if (has_goto_inter_server_user())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _goto_inter_server_user);
		if (has_new_user())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _new_user);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_leader_name()){
			output.writeString(1, _leader_name);
		}
		if (has_out_user()){
			output.writeString(2, _out_user);
		}
		if (has_goto_inter_server_user()){
			output.writeString(3, _goto_inter_server_user);
		}
		if (has_new_user()){
			output.writeMessage(4, _new_user);
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
					set_out_user(input.readString());
					break;
				}
				case 0x0000001A:{
					set_goto_inter_server_user(input.readString());
					break;
				}
				case 0x00000022:{
					set_new_user((PartyMember)input.readMessage(PartyMember.newInstance()));
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
		return new SC_PARTY_MEMBER_LIST_CHANGE();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_new_user() && _new_user != null){
			_new_user.dispose();
			_new_user = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
