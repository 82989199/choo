package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_TOP_RANKER_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	
	public static SC_TOP_RANKER_ACK newInstance(long version){
		SC_TOP_RANKER_ACK ack = newInstance();
		ack.set_result_code(ResultCode.RC_NO_CHANGE);
		ack.set_version(version);
		return ack;
	}
	
	public static SC_TOP_RANKER_ACK newInstance(int class_id, int total_page, int current_page){
		SC_TOP_RANKER_ACK ack = newInstance();
		ack.set_result_code(ResultCode.RC_SUCCESS);
		ack.set_class_id(class_id);
		ack.set_total_page(total_page);
		ack.set_current_page(current_page);
		return ack;
	}
	
	public static SC_TOP_RANKER_ACK newInstance(){
		return new SC_TOP_RANKER_ACK();
	}
	private ResultCode _result_code;
	private long _version;
	private int _class_id;
	private int _total_page;
	private int _current_page;
	private ArrayList<Ranker> _rankers;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_TOP_RANKER_ACK(){
	}
	public ResultCode get_result_code(){
		return _result_code;
	}
	public void set_result_code(ResultCode val){
		_bit |= 0x00000001;
		_result_code = val;
	}
	public boolean has_result_code(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public long get_version(){
		return _version;
	}
	public void set_version(long val){
		_bit |= 0x00000002;
		_version = val;
	}
	public boolean has_version(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_class_id(){
		return _class_id;
	}
	public void set_class_id(int val){
		_bit |= 0x00000004;
		_class_id = val;
	}
	public boolean has_class_id(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public int get_total_page(){
		return _total_page;
	}
	public void set_total_page(int val){
		_bit |= 0x00000008;
		_total_page = val;
	}
	public boolean has_total_page(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public int get_current_page(){
		return _current_page;
	}
	public void set_current_page(int val){
		_bit |= 0x00000010;
		_current_page = val;
	}
	public boolean has_current_page(){
		return (_bit & 0x00000010) == 0x00000010;
	}
	public ArrayList<Ranker> get_rankers(){
		return _rankers;
	}
	public void add_rankers(Ranker val){
		if(!has_rankers()){
			_rankers = new ArrayList<Ranker>(100);
			_bit |= 0x00000020;
		}
		_rankers.add(val);
	}
	public boolean has_rankers(){
		return (_bit & 0x00000020) == 0x00000020;
	}
	
	public void clearRankers(){
		if(has_rankers())
			_rankers.clear();
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
		if (has_result_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result_code.toInt());
		if (has_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _version);
		if (has_class_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _class_id);
		if (has_total_page())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _total_page);
		if (has_current_page())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _current_page);
		if (has_rankers()){
			for(Ranker val : _rankers)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_rankers()){
			for(Ranker val : _rankers){
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
		if (has_result_code()){
			output.writeEnum(1, _result_code.toInt());
		}
		if (has_version()){
			output.writeInt64(2, _version);
		}
		if (has_class_id()){
			output.wirteInt32(3, _class_id);
		}
		if (has_total_page()){
			output.wirteInt32(4, _total_page);
		}
		if (has_current_page()){
			output.wirteInt32(5, _current_page);
		}
		if (has_rankers()){
			for(Ranker val : _rankers){
				output.writeMessage(6, val);
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
					set_result_code(ResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_version(input.readInt64());
					break;
				}
				case 0x00000018:{
					set_class_id(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_total_page(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_current_page(input.readInt32());
					break;
				}
				case 0x00000032:{
					add_rankers((Ranker)input.readMessage(Ranker.newInstance()));
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
		return new SC_TOP_RANKER_ACK();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_rankers()){
			for(Ranker val : _rankers)
				val.dispose();
			_rankers.clear();
			_rankers = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public enum ResultCode{
		RC_SUCCESS(0),
		RC_NO_CHANGE(1),
		RC_NOW_NOT_SERVICE(2),
		RC_UNKNOWN_ERROR(100);
		private int value;
		ResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ResultCode v){
			return value == v.value;
		}
		public static ResultCode fromInt(int i){
			switch(i){
			case 0:
				return RC_SUCCESS;
			case 1:
				return RC_NO_CHANGE;
			case 2:
				return RC_NOW_NOT_SERVICE;
			case 100:
				return RC_UNKNOWN_ERROR;
			default:
				return null;
			}
		}
	}
}
