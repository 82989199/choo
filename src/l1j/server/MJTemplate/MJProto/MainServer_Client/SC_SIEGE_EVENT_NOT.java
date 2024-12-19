package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SIEGE_EVENT_NOT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_SIEGE_EVENT_NOT newInstance(){
		return new SC_SIEGE_EVENT_NOT();
	}
	private SIEGE_EVENT_KIND _eventKind;
	private java.util.LinkedList<CASTLE_INFO> _castles;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SIEGE_EVENT_NOT(){
	}
	public SIEGE_EVENT_KIND get_eventKind(){
		return _eventKind;
	}
	public void set_eventKind(SIEGE_EVENT_KIND val){
		_bit |= 0x1;
		_eventKind = val;
	}
	public boolean has_eventKind(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<CASTLE_INFO> get_castles(){
		return _castles;
	}
	public void add_castles(CASTLE_INFO val){
		if(!has_castles()){
			_castles = new java.util.LinkedList<CASTLE_INFO>();
			_bit |= 0x2;
		}
		_castles.add(val);
	}
	public boolean has_castles(){
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
		if (has_eventKind())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _eventKind.toInt());
		if (has_castles()){
			for(CASTLE_INFO val : _castles)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_eventKind()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_castles()){
			for(CASTLE_INFO val : _castles){
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
		if (has_eventKind()){
			output.writeEnum(1, _eventKind.toInt());
		}
		if (has_castles()){
			for(CASTLE_INFO val : _castles){
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
				case 0x00000008:{
					set_eventKind(SIEGE_EVENT_KIND.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_castles((CASTLE_INFO)input.readMessage(CASTLE_INFO.newInstance()));
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
		return new SC_SIEGE_EVENT_NOT();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_castles()){
			for(CASTLE_INFO val : _castles)
				val.dispose();
			_castles.clear();
			_castles = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class CASTLE_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CASTLE_INFO newInstance(){
			return new CASTLE_INFO();
		}
		private int _stringNumber;
		private String _ownerName;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CASTLE_INFO(){
		}
		public int get_stringNumber(){
			return _stringNumber;
		}
		public void set_stringNumber(int val){
			_bit |= 0x1;
			_stringNumber = val;
		}
		public boolean has_stringNumber(){
			return (_bit & 0x1) == 0x1;
		}
		public String get_ownerName(){
			return _ownerName;
		}
		public void set_ownerName(String val){
			_bit |= 0x2;
			_ownerName = val;
		}
		public boolean has_ownerName(){
			return (_bit & 0x2) == 0x2;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_stringNumber())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _stringNumber);
			if (has_ownerName())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _ownerName);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_stringNumber()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ownerName()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_stringNumber()){
				output.wirteInt32(1, _stringNumber);
			}
			if (has_ownerName()){
				output.writeString(2, _ownerName);
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
						set_stringNumber(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_ownerName(input.readString());
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
			return new CASTLE_INFO();
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
	public enum SIEGE_EVENT_KIND{
		SIEGE_EVENT_KIND_START(1),
		SIEGE_EVENT_PROGRESSING(2),
		SIEGE_EVENT_KIND_COLLECT_END(3),
		SIEGE_EVENT_KIND_END(4);
		private int value;
		SIEGE_EVENT_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(SIEGE_EVENT_KIND v){
			return value == v.value;
		}
		public static SIEGE_EVENT_KIND fromInt(int i){
			switch(i){
			case 1:
				return SIEGE_EVENT_KIND_START;
			case 2:
				return SIEGE_EVENT_PROGRESSING;
			case 3:
				return SIEGE_EVENT_KIND_COLLECT_END;
			case 4:
				return SIEGE_EVENT_KIND_END;
			default:
				return null;
			}
		}
	}
}
