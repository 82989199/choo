package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_NOTIFICATION_MESSAGE implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ProtoOutputStream make_stream(String message, MJSimpleRgb rgb){
		return make_stream(message, rgb, 60);
	}
	
	public static ProtoOutputStream make_stream(String message, MJSimpleRgb rgb, int duration){
		SC_NOTIFICATION_MESSAGE noti = newInstance(message, rgb, duration);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_NOTIFICATION_MESSAGE);
		noti.dispose();
		return stream;
	}
	
	public static SC_NOTIFICATION_MESSAGE newInstance(String message, MJSimpleRgb rgb){
		return newInstance(message, rgb, 60);
	}
	
	public static SC_NOTIFICATION_MESSAGE newInstance(String message, MJSimpleRgb rgb, int duration){
		SC_NOTIFICATION_MESSAGE noti = newInstance();
		noti.set_pos(display_position.screen_top);
		noti.set_desc(message);
		noti.set_messageRGB(rgb);
		noti.set_duration(duration);
		return noti;
	}
	
	public static SC_NOTIFICATION_MESSAGE newInstance(){
		return new SC_NOTIFICATION_MESSAGE();
	}
	private display_position _pos;
	private String _desc;
	private byte[] _messageRGB;
	private int _duration;
	private int _serverno;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_NOTIFICATION_MESSAGE(){
	}
	public display_position get_pos(){
		return _pos;
	}
	public void set_pos(display_position val){
		_bit |= 0x1;
		_pos = val;
	}
	public boolean has_pos(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_desc(){
		return _desc;
	}
	public void set_desc(String val){
		_bit |= 0x2;
		_desc = val;
	}
	public boolean has_desc(){
		return (_bit & 0x2) == 0x2;
	}
	public byte[] get_messageRGB(){
		return _messageRGB;
	}
	public void set_messageRGB(byte[] val){
		_bit |= 0x4;
		_messageRGB = val;
	}
	public void set_messageRGB(MJSimpleRgb rgb){
		set_messageRGB(rgb.get_bytes());
	}
	public boolean has_messageRGB(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_duration(){
		return _duration;
	}
	public void set_duration(int val){
		_bit |= 0x8;
		_duration = val;
	}
	public boolean has_duration(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_serverno(){
		return _serverno;
	}
	public void set_serverno(int val){
		_bit |= 0x10;
		_serverno = val;
	}
	public boolean has_serverno(){
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
		if (has_pos())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _pos.toInt());
		if (has_desc())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _desc);
		if (has_messageRGB())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _messageRGB);
		if (has_duration())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _duration);
		if (has_serverno())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _serverno);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_pos()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_desc()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_messageRGB()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_duration()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_pos()){
			output.writeEnum(1, _pos.toInt());
		}
		if (has_desc()){
			output.writeString(2, _desc);
		}
		if (has_messageRGB()){
			output.writeBytes(3, _messageRGB);
		}
		if (has_duration()){
			output.wirteInt32(4, _duration);
		}
		if (has_serverno()){
			output.wirteInt32(5, _serverno);
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
					set_pos(display_position.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_desc(input.readString());
					break;
				}
				case 0x0000001A:{
					set_messageRGB(input.readBytes());
					break;
				}
				case 0x00000020:{
					set_duration(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_serverno(input.readInt32());
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
		return new SC_NOTIFICATION_MESSAGE();
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
	public enum display_position{
		screen_top(1),
		screen_middle(2),
		screen_bottom(3);
		private int value;
		display_position(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(display_position v){
			return value == v.value;
		}
		public static display_position fromInt(int i){
			switch(i){
			case 1:
				return screen_top;
			case 2:
				return screen_middle;
			case 3:
				return screen_bottom;
			default:
				return null;
			}
		}
	}
}
