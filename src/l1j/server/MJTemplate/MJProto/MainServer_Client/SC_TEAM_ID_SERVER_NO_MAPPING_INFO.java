package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_TEAM_ID_SERVER_NO_MAPPING_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_TEAM_ID_SERVER_NO_MAPPING_INFO newInstance(){
		return new SC_TEAM_ID_SERVER_NO_MAPPING_INFO();
	}
	private java.util.LinkedList<MAPPING> _mapping_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TEAM_ID_SERVER_NO_MAPPING_INFO(){
	}
	public java.util.LinkedList<MAPPING> get_mapping_info(){
		return _mapping_info;
	}
	public void add_mapping_info(MAPPING val){
		if(!has_mapping_info()){
			_mapping_info = new java.util.LinkedList<MAPPING>();
			_bit |= 0x1;
		}
		_mapping_info.add(val);
	}
	public boolean has_mapping_info(){
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
		if (has_mapping_info()){
			for(MAPPING val : _mapping_info)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_mapping_info()){
			for(MAPPING val : _mapping_info){
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
		if (has_mapping_info()){
			for(MAPPING val : _mapping_info){
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
					add_mapping_info((MAPPING)input.readMessage(MAPPING.newInstance()));
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
		return new SC_TEAM_ID_SERVER_NO_MAPPING_INFO();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_mapping_info()){
			for(MAPPING val : _mapping_info)
				val.dispose();
			_mapping_info.clear();
			_mapping_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class MAPPING implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static MAPPING newInstance(){
			return new MAPPING();
		}
		private int _server_no;
		private int _team_id;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MAPPING(){
		}
		public int get_server_no(){
			return _server_no;
		}
		public void set_server_no(int val){
			_bit |= 0x1;
			_server_no = val;
		}
		public boolean has_server_no(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_team_id(){
			return _team_id;
		}
		public void set_team_id(int val){
			_bit |= 0x2;
			_team_id = val;
		}
		public boolean has_team_id(){
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
			if (has_server_no())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _server_no);
			if (has_team_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _team_id);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_server_no()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_team_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_server_no()){
				output.wirteInt32(1, _server_no);
			}
			if (has_team_id()){
				output.wirteInt32(2, _team_id);
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
						set_server_no(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_team_id(input.readInt32());
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
			return new MAPPING();
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
