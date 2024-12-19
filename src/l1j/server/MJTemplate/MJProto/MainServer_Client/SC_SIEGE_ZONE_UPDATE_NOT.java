package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SIEGE_ZONE_UPDATE_NOT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_SIEGE_ZONE_UPDATE_NOT newInstance(){
		return new SC_SIEGE_ZONE_UPDATE_NOT();
	}
	private SIEGE_ZONE_KIND _siegeZoneKind;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SIEGE_ZONE_UPDATE_NOT(){
	}
	public SIEGE_ZONE_KIND get_siegeZoneKind(){
		return _siegeZoneKind;
	}
	public void set_siegeZoneKind(SIEGE_ZONE_KIND val){
		_bit |= 0x1;
		_siegeZoneKind = val;
	}
	public boolean has_siegeZoneKind(){
		return (_bit & 0x1) == 0x1;
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
		if (has_siegeZoneKind())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _siegeZoneKind.toInt());
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_siegeZoneKind()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_siegeZoneKind()){
			output.writeEnum(1, _siegeZoneKind.toInt());
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
					set_siegeZoneKind(SIEGE_ZONE_KIND.fromInt(input.readEnum()));
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
		return new SC_SIEGE_ZONE_UPDATE_NOT();
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
	public enum SIEGE_ZONE_KIND{
		SIEGE_ZONE_BEGIN(1),
		SIEGE_ZONE_END(2);
		private int value;
		SIEGE_ZONE_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SIEGE_ZONE_KIND v){
			return value == v.value;
		}
		public static SIEGE_ZONE_KIND fromInt(int i){
			switch(i){
			case 1:
				return SIEGE_ZONE_BEGIN;
			case 2:
				return SIEGE_ZONE_END;
			default:
				return null;
			}
		}
	}
}
