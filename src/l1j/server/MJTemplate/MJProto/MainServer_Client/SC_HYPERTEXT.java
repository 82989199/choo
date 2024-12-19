package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_HYPERTEXT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_HYPERTEXT newInstance(){
		return new SC_HYPERTEXT();
	}
	private String _url;
	private int _npcid;
	private int _parameter;
	private java.util.LinkedList<String> _text;
	private String _cookie;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_HYPERTEXT(){
	}
	public String get_url(){
		return _url;
	}
	public void set_url(String val){
		_bit |= 0x1;
		_url = val;
	}
	public boolean has_url(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_npcid(){
		return _npcid;
	}
	public void set_npcid(int val){
		_bit |= 0x2;
		_npcid = val;
	}
	public boolean has_npcid(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_parameter(){
		return _parameter;
	}
	public void set_parameter(int val){
		_bit |= 0x4;
		_parameter = val;
	}
	public boolean has_parameter(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<String> get_text(){
		return _text;
	}
	public void add_text(String val){
		if(!has_text()){
			_text = new java.util.LinkedList<String>();
			_bit |= 0x8;
		}
		_text.add(val);
	}
	public boolean has_text(){
		return (_bit & 0x8) == 0x8;
	}
	public String get_cookie(){
		return _cookie;
	}
	public void set_cookie(String val){
		_bit |= 0x10;
		_cookie = val;
	}
	public boolean has_cookie(){
		return (_bit & 0x10) == 0x10;
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
		if (has_url())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _url);
		if (has_npcid())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _npcid);
		if (has_parameter())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _parameter);
		if (has_text()){
			for(String val : _text)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, val);
		}
		if (has_cookie())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _cookie);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_url()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_text()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_url()){
			output.writeString(1, _url);
		}
		if (has_npcid()){
			output.writeUInt32(2, _npcid);
		}
		if (has_parameter()){
			output.wirteInt32(3, _parameter);
		}
		if (has_text()){
			for(String val : _text){
				output.writeString(4, val);
			}
		}
		if (has_cookie()){
			output.writeString(5, _cookie);
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
					set_url(input.readString());
					break;
				}
				case 0x00000010:{
					set_npcid(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_parameter(input.readInt32());
					break;
				}
				case 0x00000022:{
					add_text(input.readString());
					break;
				}
				case 0x0000002A:{
					set_cookie(input.readString());
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
		return new SC_HYPERTEXT();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_text()){
			_text.clear();
			_text = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
