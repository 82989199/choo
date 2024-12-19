package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Ability;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import l1j.server.MJCompanion.Basic.ClassInfo.MJCompanionClassInfo;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJCompanion.Instance.MJCompanionUpdateFlag;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_COMPANION_STATUS_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	
	public static void send(L1PcInstance master, MJCompanionInstance companion_instance, int update_flag){

		SC_COMPANION_STATUS_NOTI noti = newInstance();
		MJCompanionClassInfo cInfo = companion_instance.get_class_info();
		noti.set_obj_id(companion_instance.getId());
		if((update_flag & MJCompanionUpdateFlag.UPDATE_NAME) == MJCompanionUpdateFlag.UPDATE_NAME){
			try {
				noti.set_name(companion_instance.getName().getBytes("MS949"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if((update_flag & MJCompanionUpdateFlag.UPDATE_CLASS_ID) == MJCompanionUpdateFlag.UPDATE_CLASS_ID)
			noti.set_class_id(cInfo.get_class_id());
		if((update_flag & MJCompanionUpdateFlag.UPDATE_EXP) == MJCompanionUpdateFlag.UPDATE_EXP){
			noti.set_level(companion_instance.getLevel());
			noti.set_exp(companion_instance.getExp());
			noti.set_minus_exp_penalty(false);
		}
		if((update_flag & MJCompanionUpdateFlag.UPDATE_HP) == MJCompanionUpdateFlag.UPDATE_HP){	
			noti.set_base_max_hp(companion_instance.get_base_max_hp());
			noti.set_temp_max_hp(companion_instance.get_temp_max_hp());
			noti.set_hp(companion_instance.getCurrentHp());
		}
		if((update_flag & MJCompanionUpdateFlag.UPDATE_STATS) == MJCompanionUpdateFlag.UPDATE_STATS){	
			Ability ability = companion_instance.getAbility();
			noti.set_base_str(ability.getStr());
			noti.set_base_con(ability.getCon());
			noti.set_base_int(ability.getInt());
			noti.set_bonus_str(ability.getAddedStr());
			noti.set_bonus_con(ability.getAddedCon());
			noti.set_bonus_int(ability.getAddedInt());
			noti.set_elixir_use_count(companion_instance.get_elixir_use_count());
			noti.set_remain_stats(companion_instance.get_remain_stats());
			noti.set_temp_str(0);
			noti.set_temp_con(0);
			noti.set_temp_int(0);
		}
		
		if((update_flag & MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP) == MJCompanionUpdateFlag.UPDATE_FRIEND_SHIP){	
			noti.set_friend_ship_guage(companion_instance.get_friend_ship_guage());
			noti.set_friend_ship_marble(companion_instance.get_friend_ship_marble());
		}
		if((update_flag & MJCompanionUpdateFlag.UPDATE_AC) == MJCompanionUpdateFlag.UPDATE_AC){	
			noti.set_ac(companion_instance.getAC().getAc());
		}
		if((update_flag & MJCompanionUpdateFlag.UPDATE_MR) == MJCompanionUpdateFlag.UPDATE_MR){	
			noti.set_mr(companion_instance.getResistance().getEffectedMrBySkill());
		}

		if((update_flag & MJCompanionUpdateFlag.UPDATE_DELAY_REDUCE) == MJCompanionUpdateFlag.UPDATE_DELAY_REDUCE){	
			noti.set_attackdelay_reduce(companion_instance.get_attackdelay_reduce());
			noti.set_movedelay_reduce(companion_instance.get_movedelay_reduce());
		}
		if((update_flag & MJCompanionUpdateFlag.UPDATE_PVP_DAMAGE) == MJCompanionUpdateFlag.UPDATE_PVP_DAMAGE){	
			noti.set_pvp_dmg_ratio(companion_instance.get_pvp_dmg_ratio());	
		}
		if((update_flag & MJCompanionUpdateFlag.UPDATE_COMBO_TIME) == MJCompanionUpdateFlag.UPDATE_COMBO_TIME){
			noti.set_is_combo_time(companion_instance.get_is_combo_time());
		}
		master.sendPackets(noti,MJEProtoMessages.SC_COMPANION_STATUS_NOTI, true);
	}
	
	public static SC_COMPANION_STATUS_NOTI newInstance(){
		return new SC_COMPANION_STATUS_NOTI();
	}
	private int _obj_id;
	private byte[] _name;
	private int _class_id;
	private int _level;
	private int _exp;
	private int _base_max_hp;
	private int _temp_max_hp;
	private int _hp;
	private int _base_str;
	private int _base_con;
	private int _base_int;
	private int _bonus_str;
	private int _bonus_con;
	private int _bonus_int;
	private int _elixir_use_count;
	private int _remain_stats;
	private int _temp_str;
	private int _temp_con;
	private int _temp_int;
	private int _friend_ship_guage;
	private int _friend_ship_marble;
	private boolean _minus_exp_penalty;
	private int _ac;
	private int _mr;
	private double _movedelay_reduce;
	private double _attackdelay_reduce;
	private int _pvp_dmg_ratio;
	private boolean _is_combo_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private long _bit;
	private SC_COMPANION_STATUS_NOTI(){
	}
	public int get_obj_id(){
		return _obj_id;
	}
	public void set_obj_id(int val){
		_bit |= 0x1L;
		_obj_id = val;
	}
	public boolean has_obj_id(){
		return (_bit & 0x1L) == 0x1L;
	}
	public byte[] get_name(){
		return _name;
	}
	public void set_name(byte[] val){
		_bit |= 0x2L;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x2L) == 0x2L;
	}
	public int get_class_id(){
		return _class_id;
	}
	public void set_class_id(int val){
		_bit |= 0x4L;
		_class_id = val;
	}
	public boolean has_class_id(){
		return (_bit & 0x4L) == 0x4L;
	}
	public int get_level(){
		return _level;
	}
	public void set_level(int val){
		_bit |= 0x8L;
		_level = val;
	}
	public boolean has_level(){
		return (_bit & 0x8L) == 0x8L;
	}
	public int get_exp(){
		return _exp;
	}
	public void set_exp(int val){
		_bit |= 0x10L;
		_exp = val;
	}
	public boolean has_exp(){
		return (_bit & 0x10L) == 0x10L;
	}
	public int get_base_max_hp(){
		return _base_max_hp;
	}
	public void set_base_max_hp(int val){
		_bit |= 0x20L;
		_base_max_hp = val;
	}
	public boolean has_base_max_hp(){
		return (_bit & 0x20L) == 0x20L;
	}
	public int get_temp_max_hp(){
		return _temp_max_hp;
	}
	public void set_temp_max_hp(int val){
		_bit |= 0x40L;
		_temp_max_hp = val;
	}
	public boolean has_temp_max_hp(){
		return (_bit & 0x40L) == 0x40L;
	}
	public int get_hp(){
		return _hp;
	}
	public void set_hp(int val){
		_bit |= 0x80L;
		_hp = val;
	}
	public boolean has_hp(){
		return (_bit & 0x80L) == 0x80L;
	}
	public int get_base_str(){
		return _base_str;
	}
	public void set_base_str(int val){
		_bit |= 0x100L;
		_base_str = val;
	}
	public boolean has_base_str(){
		return (_bit & 0x100L) == 0x100L;
	}
	public int get_base_con(){
		return _base_con;
	}
	public void set_base_con(int val){
		_bit |= 0x200L;
		_base_con = val;
	}
	public boolean has_base_con(){
		return (_bit & 0x200L) == 0x200L;
	}
	public int get_base_int(){
		return _base_int;
	}
	public void set_base_int(int val){
		_bit |= 0x400L;
		_base_int = val;
	}
	public boolean has_base_int(){
		return (_bit & 0x400L) == 0x400L;
	}
	public int get_bonus_str(){
		return _bonus_str;
	}
	public void set_bonus_str(int val){
		_bit |= 0x800L;
		_bonus_str = val;
	}
	public boolean has_bonus_str(){
		return (_bit & 0x800L) == 0x800L;
	}
	public int get_bonus_con(){
		return _bonus_con;
	}
	public void set_bonus_con(int val){
		_bit |= 0x1000L;
		_bonus_con = val;
	}
	public boolean has_bonus_con(){
		return (_bit & 0x1000L) == 0x1000L;
	}
	public int get_bonus_int(){
		return _bonus_int;
	}
	public void set_bonus_int(int val){
		_bit |= 0x2000L;
		_bonus_int = val;
	}
	public boolean has_bonus_int(){
		return (_bit & 0x2000L) == 0x2000L;
	}
	public int get_elixir_use_count(){
		return _elixir_use_count;
	}
	public void set_elixir_use_count(int val){
		_bit |= 0x4000L;
		_elixir_use_count = val;
	}
	public boolean has_elixir_use_count(){
		return (_bit & 0x4000L) == 0x4000L;
	}
	public int get_remain_stats(){
		return _remain_stats;
	}
	public void set_remain_stats(int val){
		_bit |= 0x8000L;
		_remain_stats = val;
	}
	public boolean has_remain_stats(){
		return (_bit & 0x8000L) == 0x8000L;
	}
	public int get_temp_str(){
		return _temp_str;
	}
	public void set_temp_str(int val){
		_bit |= 0x10000L;
		_temp_str = val;
	}
	public boolean has_temp_str(){
		return (_bit & 0x10000L) == 0x10000L;
	}
	public int get_temp_con(){
		return _temp_con;
	}
	public void set_temp_con(int val){
		_bit |= 0x20000L;
		_temp_con = val;
	}
	public boolean has_temp_con(){
		return (_bit & 0x20000L) == 0x20000L;
	}
	public int get_temp_int(){
		return _temp_int;
	}
	public void set_temp_int(int val){
		_bit |= 0x40000L;
		_temp_int = val;
	}
	public boolean has_temp_int(){
		return (_bit & 0x40000L) == 0x40000L;
	}
	public int get_friend_ship_guage(){
		return _friend_ship_guage;
	}
	public void set_friend_ship_guage(int val){
		_bit |= 0x80000L;
		_friend_ship_guage = val;
	}
	public boolean has_friend_ship_guage(){
		return (_bit & 0x80000L) == 0x80000L;
	}
	public int get_friend_ship_marble(){
		return _friend_ship_marble;
	}
	public void set_friend_ship_marble(int val){
		_bit |= 0x100000L;
		_friend_ship_marble = val;
	}
	public boolean has_friend_ship_marble(){
		return (_bit & 0x100000L) == 0x100000L;
	}
	public boolean get_minus_exp_penalty(){
		return _minus_exp_penalty;
	}
	public void set_minus_exp_penalty(boolean val){
		_bit |= 0x200000L;
		_minus_exp_penalty = val;
	}
	public boolean has_minus_exp_penalty(){
		return (_bit & 0x200000L) == 0x200000L;
	}
	public int get_ac(){
		return _ac;
	}
	public void set_ac(int val){
		_bit |= 0x20000000L;
		_ac = val;
	}
	public boolean has_ac(){
		return (_bit & 0x20000000L) == 0x20000000L;
	}
	public int get_mr(){
		return _mr;
	}
	public void set_mr(int val){
		_bit |= 0x40000000L;
		_mr = val;
	}
	public boolean has_mr(){
		return (_bit & 0x40000000L) == 0x40000000L;
	}
	public double get_movedelay_reduce(){
		return _movedelay_reduce;
	}
	public void set_movedelay_reduce(double val){
		_bit |= 0x80000000L;
		_movedelay_reduce = val;
	}
	public boolean has_movedelay_reduce(){
		return (_bit & 0x80000000L) == 0x80000000L;
	}
	public double get_attackdelay_reduce(){
		return _attackdelay_reduce;
	}
	public void set_attackdelay_reduce(double val){
		_bit |= 0x100000000L;
		_attackdelay_reduce = val;
	}
	public boolean has_attackdelay_reduce(){
		return (_bit & 0x100000000L) == 0x100000000L;
	}
	public int get_pvp_dmg_ratio(){
		return _pvp_dmg_ratio;
	}
	public void set_pvp_dmg_ratio(int val){
		_bit |= 0x200000000L;
		_pvp_dmg_ratio = val;
	}
	public boolean has_pvp_dmg_ratio(){
		return (_bit & 0x200000000L) == 0x200000000L;
	}
	public boolean get_is_combo_time(){
		return _is_combo_time;
	}
	public void set_is_combo_time(boolean val){
		_bit |= 0x400000000L;
		_is_combo_time = val;
	}
	public boolean has_is_combo_time(){
		return (_bit & 0x400000000L) == 0x400000000L;
	}
	@Override
	public long getInitializeBit(){
		return _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_obj_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _obj_id);
		if (has_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _name);
		if (has_class_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _class_id);
		if (has_level())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _level);
		if (has_exp())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _exp);
		if (has_base_max_hp())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _base_max_hp);
		if (has_temp_max_hp())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _temp_max_hp);
		if (has_hp())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _hp);
		if (has_base_str())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _base_str);
		if (has_base_con())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _base_con);
		if (has_base_int())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(11, _base_int);
		if (has_bonus_str())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(12, _bonus_str);
		if (has_bonus_con())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(13, _bonus_con);
		if (has_bonus_int())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _bonus_int);
		if (has_elixir_use_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(15, _elixir_use_count);
		if (has_remain_stats())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(16, _remain_stats);
		if (has_temp_str())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(17, _temp_str);
		if (has_temp_con())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(18, _temp_con);
		if (has_temp_int())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(19, _temp_int);
		if (has_friend_ship_guage())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(20, _friend_ship_guage);
		if (has_friend_ship_marble())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(21, _friend_ship_marble);
		if (has_minus_exp_penalty())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(22, _minus_exp_penalty);
		if (has_ac())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(30, _ac);
		if (has_mr())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(31, _mr);
		if (has_movedelay_reduce())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(32, _movedelay_reduce);
		if (has_attackdelay_reduce())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(33, _attackdelay_reduce);
		if (has_pvp_dmg_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(34, _pvp_dmg_ratio);
		if (has_is_combo_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(35, _is_combo_time);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_obj_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_obj_id()){
			output.writeUInt32(1, _obj_id);
		}
		if (has_name()){
			output.writeBytes(2, _name);
		}
		if (has_class_id()){
			output.writeUInt32(3, _class_id);
		}
		if (has_level()){
			output.writeUInt32(4, _level);
		}
		if (has_exp()){
			output.wirteInt32(5, _exp);
		}
		if (has_base_max_hp()){
			output.writeUInt32(6, _base_max_hp);
		}
		if (has_temp_max_hp()){
			output.writeUInt32(7, _temp_max_hp);
		}
		if (has_hp()){
			output.writeUInt32(8, _hp);
		}
		if (has_base_str()){
			output.writeUInt32(9, _base_str);
		}
		if (has_base_con()){
			output.writeUInt32(10, _base_con);
		}
		if (has_base_int()){
			output.writeUInt32(11, _base_int);
		}
		if (has_bonus_str()){
			output.writeUInt32(12, _bonus_str);
		}
		if (has_bonus_con()){
			output.writeUInt32(13, _bonus_con);
		}
		if (has_bonus_int()){
			output.writeUInt32(14, _bonus_int);
		}
		if (has_elixir_use_count()){
			output.writeUInt32(15, _elixir_use_count);
		}
		if (has_remain_stats()){
			output.writeUInt32(16, _remain_stats);
		}
		if (has_temp_str()){
			output.writeUInt32(17, _temp_str);
		}
		if (has_temp_con()){
			output.writeUInt32(18, _temp_con);
		}
		if (has_temp_int()){
			output.writeUInt32(19, _temp_int);
		}
		if (has_friend_ship_guage()){
			output.wirteInt32(20, _friend_ship_guage);
		}
		if (has_friend_ship_marble()){
			output.wirteInt32(21, _friend_ship_marble);
		}
		if (has_minus_exp_penalty()){
			output.writeBool(22, _minus_exp_penalty);
		}
		if (has_ac()){
			output.wirteInt32(30, _ac);
		}
		if (has_mr()){
			output.wirteInt32(31, _mr);
		}
		if (has_movedelay_reduce()){
			output.writeDouble(32, _movedelay_reduce);
		}
		if (has_attackdelay_reduce()){
			output.writeDouble(33, _attackdelay_reduce);
		}
		if (has_pvp_dmg_ratio()){
			output.writeUInt32(34, _pvp_dmg_ratio);
		}
		if (has_is_combo_time()){
			output.writeBool(35, _is_combo_time);
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
					set_obj_id(input.readUInt32());
					break;
				}
				case 0x00000012:{
					set_name(input.readBytes());
					break;
				}
				case 0x00000018:{
					set_class_id(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_level(input.readUInt32());
					break;
				}
				case 0x00000028:{
					set_exp(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_base_max_hp(input.readUInt32());
					break;
				}
				case 0x00000038:{
					set_temp_max_hp(input.readUInt32());
					break;
				}
				case 0x00000040:{
					set_hp(input.readUInt32());
					break;
				}
				case 0x00000048:{
					set_base_str(input.readUInt32());
					break;
				}
				case 0x00000050:{
					set_base_con(input.readUInt32());
					break;
				}
				case 0x00000058:{
					set_base_int(input.readUInt32());
					break;
				}
				case 0x00000060:{
					set_bonus_str(input.readUInt32());
					break;
				}
				case 0x00000068:{
					set_bonus_con(input.readUInt32());
					break;
				}
				case 0x00000070:{
					set_bonus_int(input.readUInt32());
					break;
				}
				case 0x00000078:{
					set_elixir_use_count(input.readUInt32());
					break;
				}
				case 0x00000080:{
					set_remain_stats(input.readUInt32());
					break;
				}
				case 0x00000088:{
					set_temp_str(input.readUInt32());
					break;
				}
				case 0x00000090:{
					set_temp_con(input.readUInt32());
					break;
				}
				case 0x00000098:{
					set_temp_int(input.readUInt32());
					break;
				}
				case 0x000000A0:{
					set_friend_ship_guage(input.readInt32());
					break;
				}
				case 0x000000A8:{
					set_friend_ship_marble(input.readInt32());
					break;
				}
				case 0x000000B0:{
					set_minus_exp_penalty(input.readBool());
					break;
				}
				case 0x000000F0:{
					set_ac(input.readInt32());
					break;
				}
				case 0x000000F8:{
					set_mr(input.readInt32());
					break;
				}
				case 0x00000101:{
					set_movedelay_reduce(input.readDouble());
					break;
				}
				case 0x00000109:{
					set_attackdelay_reduce(input.readDouble());
					break;
				}
				case 0x00000110:{
					set_pvp_dmg_ratio(input.readUInt32());
					break;
				}
				case 0x00000118:{
					set_is_combo_time(input.readBool());
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
		return new SC_COMPANION_STATUS_NOTI();
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
