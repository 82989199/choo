package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CONNECT_HIBREEDSERVER_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_CONNECT_HIBREEDSERVER_NOTI_PACKET newInstance(){
		return new SC_CONNECT_HIBREEDSERVER_NOTI_PACKET();
	}
	private int _destIP;
	private int _destPort;
	private int _reservednumber;
	private byte[] _onetimetoken;
	private int _interkind;
	private String _domainname;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_CONNECT_HIBREEDSERVER_NOTI_PACKET(){
	}
	public int get_destIP(){
		return _destIP;
	}
	public void set_destIP(int val){
		_bit |= 0x1;
		_destIP = val;
	}
	public boolean has_destIP(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_destPort(){
		return _destPort;
	}
	public void set_destPort(int val){
		_bit |= 0x2;
		_destPort = val;
	}
	public boolean has_destPort(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_reservednumber(){
		return _reservednumber;
	}
	public void set_reservednumber(int val){
		_bit |= 0x4;
		_reservednumber = val;
	}
	public boolean has_reservednumber(){
		return (_bit & 0x4) == 0x4;
	}
	public byte[] get_onetimetoken(){
		return _onetimetoken;
	}
	public void set_onetimetoken(byte[] val){
		_bit |= 0x8;
		_onetimetoken = val;
	}
	public boolean has_onetimetoken(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_interkind(){
		return _interkind;
	}
	public void set_interkind(int val){
		_bit |= 0x10;
		_interkind = val;
	}
	public boolean has_interkind(){
		return (_bit & 0x10) == 0x10;
	}
	public String get_domainname(){
		return _domainname;
	}
	public void set_domainname(String val){
		_bit |= 0x20;
		_domainname = val;
	}
	public boolean has_domainname(){
		return (_bit & 0x20) == 0x20;
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
		if (has_destIP())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _destIP);
		if (has_destPort())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _destPort);
		if (has_reservednumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _reservednumber);
		if (has_onetimetoken())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _onetimetoken);
		if (has_interkind())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _interkind);
		if (has_domainname())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(6, _domainname);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_destIP()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_destPort()){
			_memorizedIsInitialized = -1;
			return false;
		}
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
		if (has_destIP()){
			output.writeUInt32(1, _destIP);
		}
		if (has_destPort()){
			output.wirteInt32(2, _destPort);
		}
		if (has_reservednumber()){
			output.writeUInt32(3, _reservednumber);
		}
		if (has_onetimetoken()){
			output.writeBytes(4, _onetimetoken);
		}
		if (has_interkind()){
			output.wirteInt32(5, _interkind);
		}
		if (has_domainname()){
			output.writeString(6, _domainname);
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
					set_destIP(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_destPort(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_reservednumber(input.readUInt32());
					break;
				}
				case 0x00000022:{
					set_onetimetoken(input.readBytes());
					break;
				}
				case 0x00000028:{
					set_interkind(input.readInt32());
					break;
				}
				case 0x00000032:{
					set_domainname(input.readString());
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
		return new SC_CONNECT_HIBREEDSERVER_NOTI_PACKET();
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
