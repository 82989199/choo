package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Ability;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJCompanion.MJCompanionSettings;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJCompanion.Instance.MJCompanionUpdateFlag;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_COMPANION_STAT_INCREASE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_COMPANION_STAT_INCREASE_REQ newInstance(){
		return new CS_COMPANION_STAT_INCREASE_REQ();
	}
	private int _str;
	private int _int;
	private int _con;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_COMPANION_STAT_INCREASE_REQ(){
	}
	public int get_str(){
		return _str;
	}
	public void set_str(int val){
		_bit |= 0x1;
		_str = val;
	}
	public boolean has_str(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_int(){
		return _int;
	}
	public void set_int(int val){
		_bit |= 0x2;
		_int = val;
	}
	public boolean has_int(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_con(){
		return _con;
	}
	public void set_con(int val){
		_bit |= 0x4;
		_con = val;
	}
	public boolean has_con(){
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
		if (has_str())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _str);
		if (has_int())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _int);
		if (has_con())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _con);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_str()){
			output.writeUInt32(1, _str);
		}
		if (has_int()){
			output.writeUInt32(2, _int);
		}
		if (has_con()){
			output.writeUInt32(3, _con);
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
					set_str(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_int(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_con(input.readUInt32());
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
			
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			MJCompanionInstance companion = pc.get_companion();
			if(companion == null)
				return this;
			
			if(companion.isDead()){
				SC_COMPANION_STATUS_NOTI.send(pc, companion, MJCompanionUpdateFlag.UPDATE_STATS);
				pc.sendPackets("죽어있는 펫에게 스탯을 부여할 수 없습니다.");
				return this;
			}
			
			if(!valid_min_stat(pc, companion, _str, "힘") ||
					!valid_min_stat(pc, companion, _con, "콘") || 
					!valid_min_stat(pc, companion, _int, "인트"))
				return this;

			int remain_stats = companion.get_remain_stats();
			int added_total = _str + _con + _int;
			if(remain_stats < added_total){
				SC_COMPANION_STATUS_NOTI.send(pc, companion, MJCompanionUpdateFlag.UPDATE_STATS);
				pc.sendPackets("스탯이 부족합니다.");
				System.out.println(String.format("%s님의 펫 %s(%d)에게 잘못된 스탯을 부여하려고 했습니다.\r\n남아있는 스탯 : %d\r\n부여 시도한 스탯", pc.getName(), companion.getName(), companion.getId(), remain_stats, added_total));
				return this;
			}
			Ability ability = companion.getAbility();
			if(!valid_max_stat(pc, companion, ability.getTotalStr(), ability.getStr(), _str, "힘") ||
					!valid_max_stat(pc, companion, ability.getTotalCon(), ability.getCon(), _con, "콘") ||
					!valid_max_stat(pc, companion, ability.getTotalInt(), ability.getInt(), _int, "인트")
					)
				return this;

			ability.addAddedStr(_str);
			ability.addAddedCon(_con);
			ability.addAddedInt(_int);
			companion.update_stats();
			SC_COMPANION_STATUS_NOTI.send(pc, companion, MJCompanionUpdateFlag.UPDATE_ALL);
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	private boolean valid_max_stat(L1PcInstance pc, MJCompanionInstance companion, int total_stat, int base_stat, int added_stat, String section){
		int sum_stat = total_stat + added_stat;
		if(sum_stat > MJCompanionSettings.MAX_STAT){
			SC_COMPANION_STATUS_NOTI.send(pc, companion, MJCompanionUpdateFlag.UPDATE_STATS);
			pc.sendPackets(String.format("한 스탯에 최종 %d개 이상의 스탯을 부여할 수 없습니다.", MJCompanionSettings.MAX_STAT));
			System.out.println(String.format("%s님의 펫 %s(%d)에게 %d이상의 %s을(를) 부여하려고 했습니다.\r\n현재 %s : %d\r\n추가 시도한 %s : %d\r\n합계 %s : %d", 
					pc.getName(), companion.getName(), companion.getId(), MJCompanionSettings.MAX_STAT, section, section, total_stat, section, added_stat, section, sum_stat));
			return false;
		}
		if(sum_stat < base_stat){
			SC_COMPANION_STATUS_NOTI.send(pc, companion, MJCompanionUpdateFlag.UPDATE_STATS);
			pc.sendPackets(String.format("%s 스탯은 %d 이하로 부여할 수 없습니다.", section, base_stat));
			System.out.println(String.format("%s님의 펫 %s(%d)에게 %d미만의 %s을(를) 부여하려고 했습니다.\r\n현재 %s : %d\r\n추가 시도한 %s : %d\r\n합계 %s : %d\r\n베이스 %s : %d", 
					pc.getName(), companion.getName(), companion.getId(), base_stat, section, section, total_stat, section, added_stat, section, sum_stat, section, base_stat));
			return false;			
		}
		return true;
	}
	
	private boolean valid_min_stat(L1PcInstance pc, MJCompanionInstance companion, int added_stat, String section){
		if(added_stat < 0){
			SC_COMPANION_STATUS_NOTI.send(pc, companion, MJCompanionUpdateFlag.UPDATE_STATS);
			pc.sendPackets("잘못된 스탯입니다.");
			System.out.println(String.format("%s님의 펫 %s(%d)에게 마이너스 %s 스탯을 부여하려고 했습니다.\r\n추가 시도한 %s : %d", 
					pc.getName(), companion.getName(), companion.getId(), section, section, added_stat, section));
			return false;
		}
		return true;
	}
	
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_COMPANION_STAT_INCREASE_REQ();
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
