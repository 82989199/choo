package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_HUNTING_GUIDE_BOOKMARK_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_HUNTING_GUIDE_BOOKMARK_NOTI newInstance(){
		return new SC_HUNTING_GUIDE_BOOKMARK_NOTI();
	}
	private java.util.LinkedList<HUNTING_GUIDE_BOOKMARK_DATA> _bookmarks;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_HUNTING_GUIDE_BOOKMARK_NOTI(){
	}
	public java.util.LinkedList<HUNTING_GUIDE_BOOKMARK_DATA> get_bookmarks(){
		return _bookmarks;
	}
	public void add_bookmarks(HUNTING_GUIDE_BOOKMARK_DATA val){
		if(!has_bookmarks()){
			_bookmarks = new java.util.LinkedList<HUNTING_GUIDE_BOOKMARK_DATA>();
			_bit |= 0x1;
		}
		_bookmarks.add(val);
	}
	public boolean has_bookmarks(){
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
		if (has_bookmarks()){
			for(HUNTING_GUIDE_BOOKMARK_DATA val : _bookmarks)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_bookmarks()){
			for(HUNTING_GUIDE_BOOKMARK_DATA val : _bookmarks){
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
		if (has_bookmarks()){
			for(HUNTING_GUIDE_BOOKMARK_DATA val : _bookmarks){
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
					add_bookmarks((HUNTING_GUIDE_BOOKMARK_DATA)input.readMessage(HUNTING_GUIDE_BOOKMARK_DATA.newInstance()));
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
		return new SC_HUNTING_GUIDE_BOOKMARK_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_bookmarks()){
			for(HUNTING_GUIDE_BOOKMARK_DATA val : _bookmarks)
				val.dispose();
			_bookmarks.clear();
			_bookmarks = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class HUNTING_GUIDE_BOOKMARK_DATA implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static HUNTING_GUIDE_BOOKMARK_DATA newInstance(){
			return new HUNTING_GUIDE_BOOKMARK_DATA();
		}
		private int _seq;
		private int _level;
		private int _mapNo;
		private int _x;
		private int _y;
		private String _desc;
		private String _strIndex;
		private int _icon;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private HUNTING_GUIDE_BOOKMARK_DATA(){
		}
		public int get_seq(){
			return _seq;
		}
		public void set_seq(int val){
			_bit |= 0x1;
			_seq = val;
		}
		public boolean has_seq(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_level(){
			return _level;
		}
		public void set_level(int val){
			_bit |= 0x2;
			_level = val;
		}
		public boolean has_level(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_mapNo(){
			return _mapNo;
		}
		public void set_mapNo(int val){
			_bit |= 0x4;
			_mapNo = val;
		}
		public boolean has_mapNo(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_x(){
			return _x;
		}
		public void set_x(int val){
			_bit |= 0x8;
			_x = val;
		}
		public boolean has_x(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_y(){
			return _y;
		}
		public void set_y(int val){
			_bit |= 0x10;
			_y = val;
		}
		public boolean has_y(){
			return (_bit & 0x10) == 0x10;
		}
		public String get_desc(){
			return _desc;
		}
		public void set_desc(String val){
			_bit |= 0x20;
			_desc = val;
		}
		public boolean has_desc(){
			return (_bit & 0x20) == 0x20;
		}
		public String get_strIndex(){
			return _strIndex;
		}
		public void set_strIndex(String val){
			_bit |= 0x40;
			_strIndex = val;
		}
		public boolean has_strIndex(){
			return (_bit & 0x40) == 0x40;
		}
		public int get_icon(){
			return _icon;
		}
		public void set_icon(int val){
			_bit |= 0x80;
			_icon = val;
		}
		public boolean has_icon(){
			return (_bit & 0x80) == 0x80;
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
			if (has_seq())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _seq);
			if (has_level())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _level);
			if (has_mapNo())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _mapNo);
			if (has_x())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _x);
			if (has_y())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _y);
			if (has_desc())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(6, _desc);
			if (has_strIndex())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(7, _strIndex);
			if (has_icon())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _icon);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_seq()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_level()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_mapNo()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_x()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_y()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_desc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_strIndex()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_icon()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_seq()){
				output.wirteInt32(1, _seq);
			}
			if (has_level()){
				output.wirteInt32(2, _level);
			}
			if (has_mapNo()){
				output.wirteInt32(3, _mapNo);
			}
			if (has_x()){
				output.wirteInt32(4, _x);
			}
			if (has_y()){
				output.wirteInt32(5, _y);
			}
			if (has_desc()){
				output.writeString(6, _desc);
			}
			if (has_strIndex()){
				output.writeString(7, _strIndex);
			}
			if (has_icon()){
				output.wirteInt32(8, _icon);
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
						set_seq(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_level(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_mapNo(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_x(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_y(input.readInt32());
						break;
					}
					case 0x00000032:{
						set_desc(input.readString());
						break;
					}
					case 0x0000003A:{
						set_strIndex(input.readString());
						break;
					}
					case 0x00000040:{
						set_icon(input.readInt32());
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
			return new HUNTING_GUIDE_BOOKMARK_DATA();
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
