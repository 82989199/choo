package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_THEBE_CAPTURE_INFO_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_THEBE_CAPTURE_INFO_NOTI_PACKET newInstance(){
		return new SC_THEBE_CAPTURE_INFO_NOTI_PACKET();
	}
	private java.util.LinkedList<CapturePointT> _points;
	private int _remain_time_for_next_capture_event;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_THEBE_CAPTURE_INFO_NOTI_PACKET(){
	}
	public java.util.LinkedList<CapturePointT> get_points(){
		return _points;
	}
	public void add_points(CapturePointT val){
		if(!has_points()){
			_points = new java.util.LinkedList<CapturePointT>();
			_bit |= 0x1;
		}
		_points.add(val);
	}
	public boolean has_points(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_remain_time_for_next_capture_event(){
		return _remain_time_for_next_capture_event;
	}
	public void set_remain_time_for_next_capture_event(int val){
		_bit |= 0x2;
		_remain_time_for_next_capture_event = val;
	}
	public boolean has_remain_time_for_next_capture_event(){
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
		if (has_points()){
			for(CapturePointT val : _points)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_remain_time_for_next_capture_event())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _remain_time_for_next_capture_event);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_points()){
			for(CapturePointT val : _points){
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
		if (has_points()){
			for(CapturePointT val : _points){
				output.writeMessage(1, val);
			}
		}
		if (has_remain_time_for_next_capture_event()){
			output.writeUInt32(2, _remain_time_for_next_capture_event);
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
					add_points((CapturePointT)input.readMessage(CapturePointT.newInstance()));
					break;
				}
				case 0x00000010:{
					set_remain_time_for_next_capture_event(input.readUInt32());
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
		return new SC_THEBE_CAPTURE_INFO_NOTI_PACKET();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_points()){
			for(CapturePointT val : _points)
				val.dispose();
			_points.clear();
			_points = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class CapturePointT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CapturePointT newInstance(){
			return new CapturePointT();
		}
		private int _team_id;
		private int _capture_point;
		private int _homeserverno;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CapturePointT(){
		}
		public int get_team_id(){
			return _team_id;
		}
		public void set_team_id(int val){
			_bit |= 0x1;
			_team_id = val;
		}
		public boolean has_team_id(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_capture_point(){
			return _capture_point;
		}
		public void set_capture_point(int val){
			_bit |= 0x2;
			_capture_point = val;
		}
		public boolean has_capture_point(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_homeserverno(){
			return _homeserverno;
		}
		public void set_homeserverno(int val){
			_bit |= 0x4;
			_homeserverno = val;
		}
		public boolean has_homeserverno(){
			return (_bit & 0x4) == 0x4;
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
			if (has_team_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _team_id);
			if (has_capture_point())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _capture_point);
			if (has_homeserverno())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _homeserverno);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_team_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_capture_point()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_team_id()){
				output.writeUInt32(1, _team_id);
			}
			if (has_capture_point()){
				output.writeUInt32(2, _capture_point);
			}
			if (has_homeserverno()){
				output.writeUInt32(3, _homeserverno);
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
						set_team_id(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_capture_point(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_homeserverno(input.readUInt32());
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
			return new CapturePointT();
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
