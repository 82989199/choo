package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

import java.io.IOException;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_COMPANION_BUFF_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance master, int spell_id, int duration){
		SC_COMPANION_BUFF_NOTI noti = newInstance();
		Buff buff = Buff.newInstance();
		buff.set_spell_id(spell_id);
		buff.set_duration(duration);
		noti.add_buff_list(buff);
		master.sendPackets(noti, MJEProtoMessages.SC_COMPANION_BUFF_NOTI, true);
	}
	
	public static void send(L1PcInstance master, MJCompanionInstance companion_instance){
		SC_COMPANION_BUFF_NOTI noti = newInstance();
		for(int spell_id : L1SkillId.COMPANION_BUFFS){
			if(!companion_instance.hasSkillEffect(spell_id))
				continue;
			
			Buff buff = Buff.newInstance();
			buff.set_spell_id(spell_id);
			buff.set_duration(companion_instance.getSkillEffectTimeSec(spell_id));
			noti.add_buff_list(buff);
		}
		master.sendPackets(noti, MJEProtoMessages.SC_COMPANION_BUFF_NOTI, true);
	}
	
	public static SC_COMPANION_BUFF_NOTI newInstance(){
		return new SC_COMPANION_BUFF_NOTI();
	}
	private java.util.LinkedList<Buff> _buff_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_COMPANION_BUFF_NOTI(){
	}
	public java.util.LinkedList<Buff> get_buff_list(){
		return _buff_list;
	}
	public void add_buff_list(Buff val){
		if(!has_buff_list()){
			_buff_list = new java.util.LinkedList<Buff>();
			_bit |= 0x1;
		}
		_buff_list.add(val);
	}
	public boolean has_buff_list(){
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
		if (has_buff_list()){
			for(Buff val : _buff_list)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_buff_list()){
			for(Buff val : _buff_list){
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
		if (has_buff_list()){
			for(Buff val : _buff_list){
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
					add_buff_list((Buff)input.readMessage(Buff.newInstance()));
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
		return new SC_COMPANION_BUFF_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_buff_list()){
			for(Buff val : _buff_list)
				val.dispose();
			_buff_list.clear();
			_buff_list = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class Buff implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Buff newInstance(){
			return new Buff();
		}
		private int _spell_id;
		private int _duration;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Buff(){
		}
		public int get_spell_id(){
			return _spell_id;
		}
		public void set_spell_id(int val){
			_bit |= 0x1;
			_spell_id = val;
		}
		public boolean has_spell_id(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_duration(){
			return _duration;
		}
		public void set_duration(int val){
			_bit |= 0x2;
			_duration = val;
		}
		public boolean has_duration(){
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
			if (has_spell_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _spell_id);
			if (has_duration())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _duration);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_spell_id()){
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
			if (has_spell_id()){
				output.wirteInt32(1, _spell_id);
			}
			if (has_duration()){
				output.writeUInt32(2, _duration);
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
						set_spell_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_duration(input.readUInt32());
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
			return new Buff();
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
}
