package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_USER_PLAY_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send(L1PcInstance pc){
		SC_USER_PLAY_INFO_NOTI noti = newInstance();
		for(DungeonTimeInformation dtInfo : DungeonTimeInformationLoader.getInstance().get_enumerator()){
			if(!dtInfo.get_is_presentation())
				continue;
			
			MAP_TIME_LIMIT_INFO info = MAP_TIME_LIMIT_INFO.newInstance();
			info.set_time_limit_serial(dtInfo.get_serial_id());
			try {
				info.set_description(dtInfo.get_description().getBytes("MS949"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			info.set_time_limit_stay(dtInfo.get_amount_seconds());

			DungeonTimeProgress<?> progress = pc.get_progress(dtInfo);			
			if(progress == null)
				info.set_time_remained(dtInfo.get_amount_seconds());
			else
				info.set_time_remained(progress.get_remain_seconds());
			noti.add_map_time_limit_info(info);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_USER_PLAY_INFO_NOTI, true);
	}
	
	public static SC_USER_PLAY_INFO_NOTI newInstance(){
		return new SC_USER_PLAY_INFO_NOTI();
	}
	private java.util.LinkedList<MAP_TIME_LIMIT_INFO> _map_time_limit_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_USER_PLAY_INFO_NOTI(){
	}
	public java.util.LinkedList<MAP_TIME_LIMIT_INFO> get_map_time_limit_info(){
		return _map_time_limit_info;
	}
	public void add_map_time_limit_info(MAP_TIME_LIMIT_INFO val){
		if(!has_map_time_limit_info()){
			_map_time_limit_info = new java.util.LinkedList<MAP_TIME_LIMIT_INFO>();
			_bit |= 0x1;
		}
		_map_time_limit_info.add(val);
	}
	public boolean has_map_time_limit_info(){
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
		if (has_map_time_limit_info()){
			for(MAP_TIME_LIMIT_INFO val : _map_time_limit_info)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_map_time_limit_info()){
			for(MAP_TIME_LIMIT_INFO val : _map_time_limit_info){
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
		if (has_map_time_limit_info()){
			for(MAP_TIME_LIMIT_INFO val : _map_time_limit_info){
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
					add_map_time_limit_info((MAP_TIME_LIMIT_INFO)input.readMessage(MAP_TIME_LIMIT_INFO.newInstance()));
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
		return new SC_USER_PLAY_INFO_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_map_time_limit_info()){
			for(MAP_TIME_LIMIT_INFO val : _map_time_limit_info)
				val.dispose();
			_map_time_limit_info.clear();
			_map_time_limit_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class MAP_TIME_LIMIT_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static MAP_TIME_LIMIT_INFO newInstance(){
			return new MAP_TIME_LIMIT_INFO();
		}
		private int _time_limit_serial;
		private byte[] _description;
		private int _time_remained;
		private int _time_limit_stay;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MAP_TIME_LIMIT_INFO(){
		}
		public int get_time_limit_serial(){
			return _time_limit_serial;
		}
		public void set_time_limit_serial(int val){
			_bit |= 0x1;
			_time_limit_serial = val;
		}
		public boolean has_time_limit_serial(){
			return (_bit & 0x1) == 0x1;
		}
		public byte[] get_description(){
			return _description;
		}
		public void set_description(byte[] val){
			_bit |= 0x2;
			_description = val;
		}
		public boolean has_description(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_time_remained(){
			return _time_remained;
		}
		public void set_time_remained(int val){
			_bit |= 0x4;
			_time_remained = val;
		}
		public boolean has_time_remained(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_time_limit_stay(){
			return _time_limit_stay;
		}
		public void set_time_limit_stay(int val){
			_bit |= 0x8;
			_time_limit_stay = val;
		}
		public boolean has_time_limit_stay(){
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
			if (has_time_limit_serial())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _time_limit_serial);
			if (has_description())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _description);
			if (has_time_remained())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _time_remained);
			if (has_time_limit_stay())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _time_limit_stay);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_time_limit_serial()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_description()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_time_remained()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_time_limit_stay()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_time_limit_serial()){
				output.writeUInt32(1, _time_limit_serial);
			}
			if (has_description()){
				output.writeBytes(2, _description);
			}
			if (has_time_remained()){
				output.writeUInt32(3, _time_remained);
			}
			if (has_time_limit_stay()){
				output.writeUInt32(4, _time_limit_stay);
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
						set_time_limit_serial(input.readUInt32());
						break;
					}
					case 0x00000012:{
						set_description(input.readBytes());
						break;
					}
					case 0x00000018:{
						set_time_remained(input.readUInt32());
						break;
					}
					case 0x00000020:{
						set_time_limit_stay(input.readUInt32());
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
			return new MAP_TIME_LIMIT_INFO();
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
