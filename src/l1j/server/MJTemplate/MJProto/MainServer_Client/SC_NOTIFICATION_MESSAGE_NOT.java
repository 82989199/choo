package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_NOTIFICATION_MESSAGE_NOT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_NOTIFICATION_MESSAGE_NOT newInstance(){
		return new SC_NOTIFICATION_MESSAGE_NOT();
	}
	private int _suffileNumber;
	private String _notificationMessage;
	private byte[] _messageRGB;
	private int _duration;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_NOTIFICATION_MESSAGE_NOT(){
	}
	public int get_suffileNumber(){
		return _suffileNumber;
	}
	public void set_suffileNumber(int val){
		_bit |= 0x1;
		_suffileNumber = val;
	}
	public boolean has_suffileNumber(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_notificationMessage(){
		return _notificationMessage;
	}
	public void set_notificationMessage(String val){
		_bit |= 0x2;
		_notificationMessage = val;
	}
	public boolean has_notificationMessage(){
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
		if (has_suffileNumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeSInt32Size(1, _suffileNumber);
		if (has_notificationMessage())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _notificationMessage);
		if (has_messageRGB())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _messageRGB);
		if (has_duration())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeSInt32Size(4, _duration);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_suffileNumber()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_notificationMessage()){
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
		if (has_suffileNumber()){
			output.writeSInt32(1, _suffileNumber);
		}
		if (has_notificationMessage()){
			output.writeString(2, _notificationMessage);
		}
		if (has_messageRGB()){
			output.writeBytes(3, _messageRGB);
		}
		if (has_duration()){
			output.writeSInt32(4, _duration);
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
					set_suffileNumber(input.readSInt32());
					break;
				}
				case 0x00000012:{
					set_notificationMessage(input.readString());
					break;
				}
				case 0x0000001A:{
					set_messageRGB(input.readBytes());
					break;
				}
				case 0x00000020:{
					set_duration(input.readSInt32());
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
		return new SC_NOTIFICATION_MESSAGE_NOT();
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
