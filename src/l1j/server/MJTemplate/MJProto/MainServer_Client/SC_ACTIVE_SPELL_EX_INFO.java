package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ACTIVE_SPELL_EX_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ACTIVE_SPELL_EX_INFO newInstance(){
		return new SC_ACTIVE_SPELL_EX_INFO();
	}
	private java.util.LinkedList<info> _infos;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ACTIVE_SPELL_EX_INFO(){
	}
	public java.util.LinkedList<info> get_infos(){
		return _infos;
	}
	public void add_infos(info val){
		if(!has_infos()){
			_infos = new java.util.LinkedList<info>();
			_bit |= 0x1;
		}
		_infos.add(val);
	}
	public boolean has_infos(){
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
		if (has_infos()){
			for(info val : _infos)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_infos()){
			for(info val : _infos){
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
		if (has_infos()){
			for(info val : _infos){
				output.writeMessage(1, val);
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
					add_infos((info)input.readMessage(info.newInstance()));
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
		return new SC_ACTIVE_SPELL_EX_INFO();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_infos()){
			for(info val : _infos)
				val.dispose();
			_infos.clear();
			_infos = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class info implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static info newInstance(){
			return new info();
		}
		private int _spellid;
		private int _spelltype;
		private int _graphic;
		private java.util.LinkedList<Integer> _value;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private info(){
		}
		public int get_spellid(){
			return _spellid;
		}
		public void set_spellid(int val){
			_bit |= 0x1;
			_spellid = val;
		}
		public boolean has_spellid(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_spelltype(){
			return _spelltype;
		}
		public void set_spelltype(int val){
			_bit |= 0x2;
			_spelltype = val;
		}
		public boolean has_spelltype(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_graphic(){
			return _graphic;
		}
		public void set_graphic(int val){
			_bit |= 0x4;
			_graphic = val;
		}
		public boolean has_graphic(){
			return (_bit & 0x4) == 0x4;
		}
		public java.util.LinkedList<Integer> get_value(){
			return _value;
		}
		public void add_value(int val){
			if(!has_value()){
				_value = new java.util.LinkedList<Integer>();
				_bit |= 0x8;
			}
			_value.add(val);
		}
		public boolean has_value(){
			return (_bit & 0x8) == 0x8;
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
			if (has_spellid())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _spellid);
			if (has_spelltype())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _spelltype);
			if (has_graphic())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _graphic);
			if (has_value()){
				for(int val : _value)
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, val);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_spellid()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_spelltype()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_graphic()){
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
			if (has_spellid()){
				output.wirteInt32(1, _spellid);
			}
			if (has_spelltype()){
				output.wirteInt32(2, _spelltype);
			}
			if (has_graphic()){
				output.wirteInt32(3, _graphic);
			}
			if (has_value()){
				for(int val : _value){
					output.wirteInt32(4, val);
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
						set_spellid(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_spelltype(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_graphic(input.readInt32());
						break;
					}
					case 0x00000020:{
						add_value(input.readInt32());
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
			return new info();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_value()){
				_value.clear();
				_value = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
