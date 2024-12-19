package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;
import java.util.Map;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SPECIAL_RESISTANCE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void sendCharacterInfo(L1PcInstance pc){
		SC_SPECIAL_RESISTANCE_NOTI noti = null;;
		Map<Integer, Integer> map = pc.getSpecialResistanceMap();
		if(map != null && map.size() > 0){
			noti = newInstance();
			for(Integer key : map.keySet())
				noti.add_resistance(eKind.fromInt(key), map.get(key));
		}
		
		map  = pc.getSpecialPierceMap();
		if(map != null && map.size() > 0){
			if(noti == null)
				noti = newInstance();
			for(Integer key : map.keySet())
				noti.add_pierce(eKind.fromInt(key), map.get(key));
		}
		
		if(noti != null)
			pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_SPECIAL_RESISTANCE_NOTI));
	}
	
	public static SC_SPECIAL_RESISTANCE_NOTI newInstance(){
		return new SC_SPECIAL_RESISTANCE_NOTI();
	}
	private java.util.LinkedList<Value> _resistance;
	private java.util.LinkedList<Value> _pierce;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SPECIAL_RESISTANCE_NOTI(){
	}
	
	public java.util.LinkedList<Value> get_resistance(){
		return _resistance;
	}
	public void add_resistance(Value val){
		if(!has_resistance()){
			_resistance = new java.util.LinkedList<Value>();
			_bit |= 0x1;
		}
		_resistance.add(val);
	}
	public void add_resistance(eKind kind, int value){
		Value v = Value.newInstance();
		v.set_kind(kind);
		v.set_value(value);
		add_resistance(v);
	}
	public boolean has_resistance(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<Value> get_pierce(){
		return _pierce;
	}
	public void add_pierce(Value val){
		if(!has_pierce()){
			_pierce = new java.util.LinkedList<Value>();
			_bit |= 0x2;
		}
		_pierce.add(val);
	}
	public void add_pierce(eKind kind, int value){
		Value v = Value.newInstance();
		v.set_kind(kind);
		v.set_value(value);
		add_pierce(v);
	}
	public boolean has_pierce(){
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
		if (has_resistance()){
			for(Value val : _resistance)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_pierce()){
			for(Value val : _pierce)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_resistance()){
			for(Value val : _resistance){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_pierce()){
			for(Value val : _pierce){
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
		if (has_resistance()){
			for(Value val : _resistance){
				output.writeMessage(1, val);
			}
		}
		if (has_pierce()){
			for(Value val : _pierce){
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
					add_resistance((Value)input.readMessage(Value.newInstance()));
					break;
				}
				case 0x00000012:{
					add_pierce((Value)input.readMessage(Value.newInstance()));
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
		return new SC_SPECIAL_RESISTANCE_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_resistance()){
			for(Value val : _resistance)
				val.dispose();
			_resistance.clear();
			_resistance = null;
		}
		if (has_pierce()){
			for(Value val : _pierce)
				val.dispose();
			_pierce.clear();
			_pierce = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class Value implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Value newInstance(){
			return new Value();
		}
		private eKind _kind;
		private int _value;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Value(){
		}
		public eKind get_kind(){
			return _kind;
		}
		public void set_kind(eKind val){
			_bit |= 0x1;
			_kind = val;
		}
		public boolean has_kind(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_value(){
			return _value;
		}
		public void set_value(int val){
			_bit |= 0x2;
			_value = val;
		}
		public boolean has_value(){
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
			if (has_kind())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _kind.toInt());
			if (has_value())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _value);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_kind()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_value()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_kind()){
				output.writeEnum(1, _kind.toInt());
			}
			if (has_value()){
				output.wirteInt32(2, _value);
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
						set_kind(eKind.fromInt(input.readEnum()));
						break;
					}
					case 0x00000010:{
						set_value(input.readInt32());
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
			return new Value();
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
	
	/*	 
	 * 	- 기술(ABILITY): 군주, 기사				// SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY
	 * 	- 정령(SPIRIT): 요정, 다크엘프			// SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT
	 * 	- 용언(DRAGON_SPELL): 용기사, 환술사	// SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL
	 * 	- 공포(FEAR): 전사						// SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR
	 */
	public enum eKind{
		/**none**/
		NONE(0),
		/**기술**/
		ABILITY(1),
		/**정령**/
		SPIRIT(2),
		/**용언**/
		DRAGON_SPELL(3),
		/**공포**/
		FEAR(4),
		/**모든 내성: 기술, 정령, 용언, 공포**/
		ALL(5);
		private int value;
		eKind(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eKind v){
			return value == v.value;
		}
		public static eKind fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return ABILITY;
			case 2:
				return SPIRIT;
			case 3:
				return DRAGON_SPELL;
			case 4:
				return FEAR;
			case 5:
				return ALL;
			default:
				return null;
			}
		}
	}
}
