package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FATIGUE_NOTI.FATIGUE_STATUS;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_FATIGUE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void offEinhasad(L1PcInstance pc){
		// ein off
		SC_FATIGUE_NOTI noti = newInstance();
		noti.set_combatFatigue(0);
		noti.set_fatigueStatus(FATIGUE_STATUS.FATIGUE_STATUS_OK);
		noti.set_penaltyLevel(1);
		pc.sendPackets(noti, MJEProtoMessages.SC_FATIGUE_NOTI, true);
		
		// fatigue off
		noti = newInstance();
		noti.set_combatFatigue(0);
		noti.set_fatigueStatus(FATIGUE_STATUS.FATIGUE_STATUS_OK);
		noti.set_penaltyLevel(0);
		pc.sendPackets(noti, MJEProtoMessages.SC_FATIGUE_NOTI, true);
	}
	
	public static SC_FATIGUE_NOTI newInstance(){
		return new SC_FATIGUE_NOTI();
	}
	private FATIGUE_STATUS _fatigueStatus;
	private int _penaltyLevel;
	private long _combatFatigue;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_FATIGUE_NOTI(){
	}
	public FATIGUE_STATUS get_fatigueStatus(){
		return _fatigueStatus;
	}
	public void set_fatigueStatus(FATIGUE_STATUS val){
		_bit |= 0x1;
		_fatigueStatus = val;
	}
	public boolean has_fatigueStatus(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_penaltyLevel(){
		return _penaltyLevel;
	}
	public void set_penaltyLevel(int val){
		_bit |= 0x2;
		_penaltyLevel = val;
	}
	public boolean has_penaltyLevel(){
		return (_bit & 0x2) == 0x2;
	}
	public long get_combatFatigue(){
		return _combatFatigue;
	}
	public void set_combatFatigue(long val){
		_bit |= 0x4;
		_combatFatigue = val;
	}
	public boolean has_combatFatigue(){
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
		if (has_fatigueStatus())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _fatigueStatus.toInt());
		if (has_penaltyLevel())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _penaltyLevel);
		if (has_combatFatigue())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(3, _combatFatigue);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_fatigueStatus()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_penaltyLevel()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_combatFatigue()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_fatigueStatus()){
			output.writeEnum(1, _fatigueStatus.toInt());
		}
		if (has_penaltyLevel()){
			output.wirteInt32(2, _penaltyLevel);
		}
		if (has_combatFatigue()){
			output.wirteUInt64(3, _combatFatigue);
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
					set_fatigueStatus(FATIGUE_STATUS.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_penaltyLevel(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_combatFatigue(input.readUInt64());
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
		return new SC_FATIGUE_NOTI();
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
	public enum FATIGUE_STATUS{
		FATIGUE_STATUS_OK(1),
		FATIGUE_STATUS_FAIL(2),
		FATIGUE_STATUS_NOT_USE(3);
		private int value;
		FATIGUE_STATUS(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(FATIGUE_STATUS v){
			return value == v.value;
		}
		public static FATIGUE_STATUS fromInt(int i){
			switch(i){
			case 1:
				return FATIGUE_STATUS_OK;
			case 2:
				return FATIGUE_STATUS_FAIL;
			case 3:
				return FATIGUE_STATUS_NOT_USE;
			default:
				return null;
			}
		}
	}
}
