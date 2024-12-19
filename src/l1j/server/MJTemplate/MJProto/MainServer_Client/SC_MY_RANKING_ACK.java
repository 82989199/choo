package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_MY_RANKING_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send_not_service(L1PcInstance pc){
		SC_MY_RANKING_ACK ack = newInstance();
		ack.set_result_code(ResultCode.RC_NOW_NOT_SERVICE.toInt());
		pc.sendPackets(ack, MJEProtoMessages.SC_MY_RANKING_ACK, true);
	}
	
	public static void send(L1PcInstance pc){
		SC_MY_RANKING_ACK ack = newInstance();
		ack.set_result_code(ResultCode.RC_NO_CHANGE.toInt());
		pc.sendPackets(ack, MJEProtoMessages.SC_MY_RANKING_ACK, true);
	}
	
	public static void send(L1PcInstance pc, SC_TOP_RANKER_NOTI noti, long current_version){
		SC_MY_RANKING_ACK ack = newInstance();
		ack.set_result_code(ResultCode.RC_SUCCESS.toInt());
		ack.set_version(current_version);
		ack.set_class_rank(noti.get_class_ranker().get_rank());
		ack.set_previous_class_rank(noti.get_class_ranker().get_previous_rank());
		ack.set_all_rank(noti.get_total_ranker().get_rank());
		ack.set_previous_all_rank(noti.get_total_ranker().get_previous_rank());
		pc.sendPackets(ack, MJEProtoMessages.SC_MY_RANKING_ACK, true);
	}
	
	public static SC_MY_RANKING_ACK newInstance(){
		return new SC_MY_RANKING_ACK();
	}
	private int _result_code;
	private long _version;
	private int _class_rank;
	private int _previous_class_rank;
	private int _all_rank;
	private int _previous_all_rank;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_MY_RANKING_ACK(){
	}
	public int get_result_code(){
		return _result_code;
	}
	public void set_result_code(int val){
		_bit |= 0x1;
		_result_code = val;
	}
	public boolean has_result_code(){
		return (_bit & 0x1) == 0x1;
	}
	public long get_version(){
		return _version;
	}
	public void set_version(long val){
		_bit |= 0x2;
		_version = val;
	}
	public boolean has_version(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_class_rank(){
		return _class_rank;
	}
	public void set_class_rank(int val){
		_bit |= 0x4;
		_class_rank = val;
	}
	public boolean has_class_rank(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_previous_class_rank(){
		return _previous_class_rank;
	}
	public void set_previous_class_rank(int val){
		_bit |= 0x8;
		_previous_class_rank = val;
	}
	public boolean has_previous_class_rank(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_all_rank(){
		return _all_rank;
	}
	public void set_all_rank(int val){
		_bit |= 0x10;
		_all_rank = val;
	}
	public boolean has_all_rank(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_previous_all_rank(){
		return _previous_all_rank;
	}
	public void set_previous_all_rank(int val){
		_bit |= 0x20;
		_previous_all_rank = val;
	}
	public boolean has_previous_all_rank(){
		return (_bit & 0x20) == 0x20;
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
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _result_code);
		if (has_version())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _version);
		if (has_class_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _class_rank);
		if (has_previous_class_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _previous_class_rank);
		if (has_all_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _all_rank);
		if (has_previous_all_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _previous_all_rank);
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
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_result_code()){
			output.wirteInt32(1, _result_code);
		}
		if (has_version()){
			output.writeInt64(2, _version);
		}
		if (has_class_rank()){
			output.wirteInt32(3, _class_rank);
		}
		if (has_previous_class_rank()){
			output.wirteInt32(4, _previous_class_rank);
		}
		if (has_all_rank()){
			output.wirteInt32(5, _all_rank);
		}
		if (has_previous_all_rank()){
			output.wirteInt32(6, _previous_all_rank);
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
					set_result_code(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_version(input.readInt64());
					break;
				}
				case 0x00000018:{
					set_class_rank(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_previous_class_rank(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_all_rank(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_previous_all_rank(input.readInt32());
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
		return new SC_MY_RANKING_ACK();
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
	public enum ResultCode{
		RC_SUCCESS(0),
		RC_NO_CHANGE(1),
		RC_NOW_NOT_SERVICE(2),
		RC_NO_RANK(3),
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
			case 3:
				return RC_NO_RANK;
			case 100:
				return RC_UNKNOWN_ERROR;
			default:
				return null;
			}
		}
	}
}
