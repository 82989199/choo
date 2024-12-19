package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SIEGE_INJURY_TIME_NOIT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_SIEGE_INJURY_TIME_NOIT newInstance(){
		return new SC_SIEGE_INJURY_TIME_NOIT();
	}
	private SIEGE_KIND _siegeKind;
	private int _remainSecond;
	private String _pledgeName;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SIEGE_INJURY_TIME_NOIT(){
	}
	public SIEGE_KIND get_siegeKind(){
		return _siegeKind;
	}
	public void set_siegeKind(SIEGE_KIND val){
		_bit |= 0x1;
		_siegeKind = val;
	}
	public boolean has_siegeKind(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_remainSecond(){
		return _remainSecond;
	}
	public void set_remainSecond(int val){
		_bit |= 0x2;
		_remainSecond = val;
	}
	public boolean has_remainSecond(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_pledgeName(){
		return _pledgeName;
	}
	public void set_pledgeName(String val){
		_bit |= 0x4;
		_pledgeName = val;
	}
	public boolean has_pledgeName(){
		return (_bit & 0x4) == 0x4;
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
		if (has_siegeKind())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _siegeKind.toInt());
		if (has_remainSecond())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeSInt32Size(2, _remainSecond);
		if (has_pledgeName())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _pledgeName);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_siegeKind()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remainSecond()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_siegeKind()){
			output.writeEnum(1, _siegeKind.toInt());
		}
		if (has_remainSecond()){
			output.writeSInt32(2, _remainSecond);
		}
		if (has_pledgeName()){
			output.writeString(3, _pledgeName);
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
					set_siegeKind(SIEGE_KIND.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_remainSecond(input.readSInt32());
					break;
				}
				case 0x0000001A:{
					set_pledgeName(input.readString());
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
		return new SC_SIEGE_INJURY_TIME_NOIT();
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
	public enum SIEGE_KIND{
		SIEGE_DEFFENCE(1),
		SIEGE_ATTACK(2);
		private int value;
		SIEGE_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SIEGE_KIND v){
			return value == v.value;
		}
		public static SIEGE_KIND fromInt(int i){
			switch(i){
			case 1:
				return SIEGE_DEFFENCE;
			case 2:
				return SIEGE_ATTACK;
			default:
				return null;
			}
		}
	}
}
