package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BASECAMP_POINTRANK_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_BASECAMP_POINTRANK_NOTI_PACKET newInstance(){
		return new SC_BASECAMP_POINTRANK_NOTI_PACKET();
	}
	private java.util.LinkedList<PointRankInfoT> _top_rankers;
	private PointRankInfoT _my_rank;
	private long _team_points;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BASECAMP_POINTRANK_NOTI_PACKET(){
	}
	public java.util.LinkedList<PointRankInfoT> get_top_rankers(){
		return _top_rankers;
	}
	public void add_top_rankers(PointRankInfoT val){
		if(!has_top_rankers()){
			_top_rankers = new java.util.LinkedList<PointRankInfoT>();
			_bit |= 0x1;
		}
		_top_rankers.add(val);
	}
	public boolean has_top_rankers(){
		return (_bit & 0x1) == 0x1;
	}
	public PointRankInfoT get_my_rank(){
		return _my_rank;
	}
	public void set_my_rank(PointRankInfoT val){
		_bit |= 0x2;
		_my_rank = val;
	}
	public boolean has_my_rank(){
		return (_bit & 0x2) == 0x2;
	}
	public long get_team_points(){
		return _team_points;
	}
	public void set_team_points(long val){
		_bit |= 0x4;
		_team_points = val;
	}
	public boolean has_team_points(){
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
		if (has_top_rankers()){
			for(PointRankInfoT val : _top_rankers)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_my_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _my_rank);
		if (has_team_points())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(3, _team_points);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_top_rankers()){
			for(PointRankInfoT val : _top_rankers){
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
		if (has_top_rankers()){
			for(PointRankInfoT val : _top_rankers){
				output.writeMessage(1, val);
			}
		}
		if (has_my_rank()){
			output.writeMessage(2, _my_rank);
		}
		if (has_team_points()){
			output.writeInt64(3, _team_points);
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
					add_top_rankers((PointRankInfoT)input.readMessage(PointRankInfoT.newInstance()));
					break;
				}
				case 0x00000012:{
					set_my_rank((PointRankInfoT)input.readMessage(PointRankInfoT.newInstance()));
					break;
				}
				case 0x00000018:{
					set_team_points(input.readInt64());
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
		return new SC_BASECAMP_POINTRANK_NOTI_PACKET();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_top_rankers()){
			for(PointRankInfoT val : _top_rankers)
				val.dispose();
			_top_rankers.clear();
			_top_rankers = null;
		}
		if (has_my_rank() && _my_rank != null){
			_my_rank.dispose();
			_my_rank = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class PointRankInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static PointRankInfoT newInstance(){
			return new PointRankInfoT();
		}
		private int _user_rank;
		private byte[] _user_name;
		private long _user_points;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PointRankInfoT(){
		}
		public int get_user_rank(){
			return _user_rank;
		}
		public void set_user_rank(int val){
			_bit |= 0x1;
			_user_rank = val;
		}
		public boolean has_user_rank(){
			return (_bit & 0x1) == 0x1;
		}
		public byte[] get_user_name(){
			return _user_name;
		}
		public void set_user_name(byte[] val){
			_bit |= 0x2;
			_user_name = val;
		}
		public boolean has_user_name(){
			return (_bit & 0x2) == 0x2;
		}
		public long get_user_points(){
			return _user_points;
		}
		public void set_user_points(long val){
			_bit |= 0x4;
			_user_points = val;
		}
		public boolean has_user_points(){
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
			if (has_user_rank())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _user_rank);
			if (has_user_name())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _user_name);
			if (has_user_points())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(3, _user_points);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_user_rank()){
				_memorizedIsInitialized = -1;
				return false;
			}
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
			if (has_user_rank()){
				output.writeUInt32(1, _user_rank);
			}
			if (has_user_name()){
				output.writeBytes(2, _user_name);
			}
			if (has_user_points()){
				output.writeInt64(3, _user_points);
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
						set_user_rank(input.readUInt32());
						break;
					}
					case 0x00000012:{
						set_user_name(input.readBytes());
						break;
					}
					case 0x00000018:{
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
			return new PointRankInfoT();
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
