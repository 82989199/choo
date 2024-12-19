package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.Comparator;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BASECAMP_CHART_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_BASECAMP_CHART_NOTI_PACKET newInstance(){
		return new SC_BASECAMP_CHART_NOTI_PACKET();
	}
	private java.util.LinkedList<CHART_INFO> _charts;
	private long _team_points;
	private int _winner_team_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BASECAMP_CHART_NOTI_PACKET(){
	}
	public java.util.LinkedList<CHART_INFO> get_charts(){
		return _charts;
	}
	public void add_charts(CHART_INFO val){
		if(!has_charts()){
			_charts = new java.util.LinkedList<CHART_INFO>();
			_bit |= 0x1;
		}
		_charts.add(val);
	}
	public boolean has_charts(){
		return (_bit & 0x1) == 0x1;
	}
	public long get_team_points(){
		return _team_points;
	}
	public void set_team_points(long val){
		_bit |= 0x2;
		_team_points = val;
	}
	public boolean has_team_points(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_winner_team_id(){
		return _winner_team_id;
	}
	public void set_winner_team_id(int val){
		_bit |= 0x4;
		_winner_team_id = val;
	}
	public boolean has_winner_team_id(){
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
		if (has_charts()){
			for(CHART_INFO val : _charts)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_team_points())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _team_points);
		if (has_winner_team_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _winner_team_id);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_charts()){
			for(CHART_INFO val : _charts){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_team_points()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_charts()){
			for(CHART_INFO val : _charts){
				output.writeMessage(1, val);
			}
		}
		if (has_team_points()){
			output.writeInt64(2, _team_points);
		}
		if (has_winner_team_id()){
			output.wirteInt32(3, _winner_team_id);
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
					add_charts((CHART_INFO)input.readMessage(CHART_INFO.newInstance()));
					break;
				}
				case 0x00000010:{
					set_team_points(input.readInt64());
					break;
				}
				case 0x00000018:{
					set_winner_team_id(input.readInt32());
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
		return new SC_BASECAMP_CHART_NOTI_PACKET();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_charts()){
			_charts.clear();
			_charts = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class CHART_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Comparator<CHART_INFO> create_comparator(){
			return new Comparator<CHART_INFO>(){
				@Override
				public int compare(CHART_INFO o1, CHART_INFO o2) {
					return (int)(o2.get_user_points() - o1.get_user_points());
				}
			};
		}
		
		public static CHART_INFO newInstance(L1PcInstance pc){
			CHART_INFO cInfo = newInstance();
			cInfo.set_id(pc.getId());
			cInfo.set_user_name(pc.getName());
			cInfo.set_user_points(1);
			return cInfo;
		}
		
		public static CHART_INFO newInstance(){
			return new CHART_INFO();
		}
		private int _id;
		private String _user_name;
		private long _user_points;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CHART_INFO(){
		}
		
		public void set_id(int id){
			_id = id;
		}
		
		public int get_id(){
			return _id;
		}
		
		public String get_user_name(){
			return _user_name;
		}
		public void set_user_name(String val){
			_bit |= 0x1;
			_user_name = val;
		}
		public boolean has_user_name(){
			return (_bit & 0x1) == 0x1;
		}
		public long get_user_points(){
			return _user_points;
		}
		public void set_user_points(long val){
			_bit |= 0x2;
			_user_points = val;
		}
		public long inc_user_points(){
			_bit |= 0x2;
			return ++_user_points;
		}
		public boolean has_user_points(){
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
			if (has_user_name())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _user_name);
			if (has_user_points())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _user_points);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_user_name()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_user_points()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_user_name()){
				output.writeString(1, _user_name);
			}
			if (has_user_points()){
				output.writeInt64(2, _user_points);
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
						set_user_name(input.readString());
						break;
					}
					case 0x00000010:{
						set_user_points(input.readInt64());
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
			return new CHART_INFO();
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
