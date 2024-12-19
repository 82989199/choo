package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BOX_ATTR_CHANGE_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ProtoOutputStream create_box(int mapid, Box box, int attr){
		SC_BOX_ATTR_CHANGE_NOTI_PACKET noti = newInstance();
		noti.set_worldNumber(mapid);
		noti.set_box(box);
		noti.set_attribute(attr);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_BOX_ATTR_CHANGE_NOTI_PACKET);
		noti.dispose();
		return stream;
	}
	
	public static SC_BOX_ATTR_CHANGE_NOTI_PACKET newInstance(){
		return new SC_BOX_ATTR_CHANGE_NOTI_PACKET();
	}
	private int _worldNumber;
	private Box _box;
	private int _attribute;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BOX_ATTR_CHANGE_NOTI_PACKET(){
	}
	public int get_worldNumber(){
		return _worldNumber;
	}
	public void set_worldNumber(int val){
		_bit |= 0x1;
		_worldNumber = val;
	}
	public boolean has_worldNumber(){
		return (_bit & 0x1) == 0x1;
	}
	public Box get_box(){
		return _box;
	}
	public void set_box(Box val){
		_bit |= 0x2;
		_box = val;
	}

	public boolean has_box(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_attribute(){
		return _attribute;
	}
	public void set_attribute(int val){
		_bit |= 0x4;
		_attribute = val;
	}
	public boolean has_attribute(){
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
		if (has_worldNumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _worldNumber);
		if (has_box())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _box);
		if (has_attribute())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _attribute);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_worldNumber()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_box()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_attribute()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_worldNumber()){
			output.wirteInt32(1, _worldNumber);
		}
		if (has_box()){
			output.writeMessage(2, _box);			
		}
		if (has_attribute()){
			output.wirteInt32(3, _attribute);
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
					set_worldNumber(input.readInt32());
					break;
				}
				case 0x00000012:{
					set_box((Box)input.readMessage(Box.newInstance()));
					break;
				}
				case 0x00000018:{
					set_attribute(input.readInt32());
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
		return new SC_BOX_ATTR_CHANGE_NOTI_PACKET();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_box() && _box != null){
			_box = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class Box implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Box[] create_line_boxes(int left, int top, int right, int bottom){
			Box[] boxes = new Box[4];
			
			Box box = Box.newInstance();
			box.set_sx(left);
			box.set_sy(top);
			box.set_ex(right);
			box.set_ey(top + 1);
			boxes[0] = box;
			
			box = Box.newInstance();
			box.set_sx(right - 1);
			box.set_sy(top);
			box.set_ex(right);
			box.set_ey(bottom);
			boxes[1] = box;
			
			box = Box.newInstance();
			box.set_sx(left);
			box.set_sy(bottom - 1);
			box.set_ex(right);
			box.set_ey(bottom);
			boxes[2] = box;
			
			box.set_sx(left);
			box.set_sy(top);
			box.set_ex(left + 1);
			box.set_ey(bottom);
			boxes[3] = box;
			return boxes;
		}
		
		public static Box newInstance(){
			return new Box();
		}
		private int _sx;
		private int _sy;
		private int _ex;
		private int _ey;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Box(){
		}
		public int get_sx(){
			return _sx;
		}
		public void set_sx(int val){
			_bit |= 0x1;
			_sx = val;
		}
		public boolean has_sx(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_sy(){
			return _sy;
		}
		public void set_sy(int val){
			_bit |= 0x2;
			_sy = val;
		}
		public boolean has_sy(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_ex(){
			return _ex;
		}
		public void set_ex(int val){
			_bit |= 0x4;
			_ex = val;
		}
		public boolean has_ex(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_ey(){
			return _ey;
		}
		public void set_ey(int val){
			_bit |= 0x8;
			_ey = val;
		}
		public boolean has_ey(){
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
			if (has_sx())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _sx);
			if (has_sy())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _sy);
			if (has_ex())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _ex);
			if (has_ey())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _ey);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_sx()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_sy()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ex()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ey()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_sx()){
				output.wirteInt32(1, _sx);
			}
			if (has_sy()){
				output.wirteInt32(2, _sy);
			}
			if (has_ex()){
				output.wirteInt32(3, _ex);
			}
			if (has_ey()){
				output.wirteInt32(4, _ey);
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
						set_sx(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_sy(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_ex(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_ey(input.readInt32());
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
			return new Box();
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
