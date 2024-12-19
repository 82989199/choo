package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SPELL_BUFF_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static final int[][] ANGER_STAT_MAPPER = new int[][]{
		{0, 0},
		{6432, 3932},
		{6433, 3933},
		{6434, 3934},
		{6435, 3935},
		{6436, 3936},
		{6437, 3937},
	};
	
	public static void sendFatigueOff(L1PcInstance pc, int penaltyLevel){
		if(penaltyLevel <= 0 || penaltyLevel >= ANGER_STAT_MAPPER.length)
			return;
		
		int iconId = ANGER_STAT_MAPPER[penaltyLevel][0];
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.END);
		noti.set_spell_id(iconId);
		noti.set_off_icon_id(0);
		noti.set_on_icon_id(0);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}
	
	public static void sendFatigueOn(L1PcInstance pc, int penaltyLevel, int time){
		if(penaltyLevel <= 0 || penaltyLevel >= ANGER_STAT_MAPPER.length)
			return;
		
		int iconId = ANGER_STAT_MAPPER[penaltyLevel][0];
		int tooltipId = ANGER_STAT_MAPPER[penaltyLevel][1];
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.RESTAT);
		noti.set_spell_id(iconId);
		noti.set_duration(time * 60);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
		noti.set_on_icon_id(iconId);
		noti.set_off_icon_id(iconId);
		noti.set_icon_priority(1);
		noti.set_tooltip_str_id(tooltipId);
		noti.set_new_str_id(3215);
		noti.set_end_str_id(2238);
		noti.set_is_good(true);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}
	
	public static SC_SPELL_BUFF_NOTI newInstance(){
		
		return new SC_SPELL_BUFF_NOTI();
	}
	private eNotiType _noti_type;
	private int _spell_id;
	private int _duration;
	private eDurationShowType _duration_show_type;
	private int _on_icon_id;
	private int _off_icon_id;
	private int _icon_priority;
	private int _tooltip_str_id;
	private int _new_str_id;
	private int _end_str_id;
	private boolean _is_good;
	private int _overlap_buff_icon;
	private int _main_tooltip_str_id;
	private int _buff_icon_priority;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_SPELL_BUFF_NOTI(){
	}
	public eNotiType get_noti_type(){
		return _noti_type;
	}
	public void set_noti_type(eNotiType val){
		_bit |= 0x1;
		_noti_type = val;
	}
	public boolean has_noti_type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_spell_id(){
		return _spell_id;
	}
	public void set_spell_id(int val){
		_bit |= 0x2;
		_spell_id = val;
	}
	public boolean has_spell_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_duration(){
		return _duration;
	}
	public void set_duration(int val){
		_bit |= 0x4;
		_duration = val;
	}
	public boolean has_duration(){
		return (_bit & 0x4) == 0x4;
	}
	public eDurationShowType get_duration_show_type(){
		return _duration_show_type;
	}
	public void set_duration_show_type(eDurationShowType val){
		_bit |= 0x8;
		_duration_show_type = val;
	}
	public boolean has_duration_show_type(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_on_icon_id(){
		return _on_icon_id;
	}
	public void set_on_icon_id(int val){
		_bit |= 0x10;
		_on_icon_id = val;
	}
	public boolean has_on_icon_id(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_off_icon_id(){
		return _off_icon_id;
	}
	public void set_off_icon_id(int val){
		_bit |= 0x20;
		_off_icon_id = val;
	}
	public boolean has_off_icon_id(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_icon_priority(){
		return _icon_priority;
	}
	public void set_icon_priority(int val){
		_bit |= 0x40;
		_icon_priority = val;
	}
	public boolean has_icon_priority(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_tooltip_str_id(){
		return _tooltip_str_id;
	}
	public void set_tooltip_str_id(int val){
		_bit |= 0x80;
		_tooltip_str_id = val;
	}
	public boolean has_tooltip_str_id(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_new_str_id(){
		return _new_str_id;
	}
	public void set_new_str_id(int val){
		_bit |= 0x100;
		_new_str_id = val;
	}
	public boolean has_new_str_id(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_end_str_id(){
		return _end_str_id;
	}
	public void set_end_str_id(int val){
		_bit |= 0x200;
		_end_str_id = val;
	}
	public boolean has_end_str_id(){
		return (_bit & 0x200) == 0x200;
	}
	public boolean get_is_good(){
		return _is_good;
	}
	public void set_is_good(boolean val){
		_bit |= 0x400;
		_is_good = val;
	}
	public boolean has_is_good(){
		return (_bit & 0x400) == 0x400;
	}
	public int get_overlap_buff_icon(){
		return _overlap_buff_icon;
	}
	public void set_overlap_buff_icon(int val){
		_bit |= 0x800;
		_overlap_buff_icon = val;
	}
	public boolean has_overlap_buff_icon(){
		return (_bit & 0x800) == 0x800;
	}
	public int get_main_tooltip_str_id(){
		return _main_tooltip_str_id;
	}
	public void set_main_tooltip_str_id(int val){
		_bit |= 0x1000;
		_main_tooltip_str_id = val;
	}
	public boolean has_main_tooltip_str_id(){
		return (_bit & 0x1000) == 0x1000;
	}
	public int get_buff_icon_priority(){
		return _buff_icon_priority;
	}
	public void set_buff_icon_priority(int val){
		_bit |= 0x2000;
		_buff_icon_priority = val;
	}
	public boolean has_buff_icon_priority(){
		return (_bit & 0x2000) == 0x2000;
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
		if (has_noti_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _noti_type.toInt());
		if (has_spell_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _spell_id);
		if (has_duration())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _duration);
		if (has_duration_show_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _duration_show_type.toInt());
		if (has_on_icon_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _on_icon_id);
		if (has_off_icon_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _off_icon_id);
		if (has_icon_priority())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _icon_priority);
		if (has_tooltip_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _tooltip_str_id);
		if (has_new_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _new_str_id);
		if (has_end_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _end_str_id);
		if (has_is_good())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _is_good);
		if (has_overlap_buff_icon())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(12, _overlap_buff_icon);
		if (has_main_tooltip_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(13, _main_tooltip_str_id);
		if (has_buff_icon_priority())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _buff_icon_priority);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_noti_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_spell_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_noti_type()){
			output.writeEnum(1, _noti_type.toInt());
		}
		if (has_spell_id()){
			output.writeUInt32(2, _spell_id);
		}
		if (has_duration()){
			output.writeUInt32(3, _duration);
		}
		if (has_duration_show_type()){
			output.writeEnum(4, _duration_show_type.toInt());
		}
		if (has_on_icon_id()){
			output.writeUInt32(5, _on_icon_id);
		}
		if (has_off_icon_id()){
			output.writeUInt32(6, _off_icon_id);
		}
		if (has_icon_priority()){
			output.writeUInt32(7, _icon_priority);
		}
		if (has_tooltip_str_id()){
			output.writeUInt32(8, _tooltip_str_id);
		}
		if (has_new_str_id()){
			output.writeUInt32(9, _new_str_id);
		}
		if (has_end_str_id()){
			output.writeUInt32(10, _end_str_id);
		}
		if (has_is_good()){
			output.writeBool(11, _is_good);
		}
		if (has_overlap_buff_icon()){
			output.writeUInt32(12, _overlap_buff_icon);
		}
		if (has_main_tooltip_str_id()){
			output.writeUInt32(13, _main_tooltip_str_id);
		}
		if (has_buff_icon_priority()){
			output.writeUInt32(14, _buff_icon_priority);
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
					set_noti_type(eNotiType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_spell_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_duration(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_duration_show_type(eDurationShowType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000028:{
					set_on_icon_id(input.readUInt32());
					break;
				}
				case 0x00000030:{
					set_off_icon_id(input.readUInt32());
					break;
				}
				case 0x00000038:{
					set_icon_priority(input.readUInt32());
					break;
				}
				case 0x00000040:{
					set_tooltip_str_id(input.readUInt32());
					break;
				}
				case 0x00000048:{
					set_new_str_id(input.readUInt32());
					break;
				}
				case 0x00000050:{
					set_end_str_id(input.readUInt32());
					break;
				}
				case 0x00000058:{
					set_is_good(input.readBool());
					break;
				}
				case 0x00000060:{
					set_overlap_buff_icon(input.readUInt32());
					break;
				}
				case 0x00000068:{
					set_main_tooltip_str_id(input.readUInt32());
					break;
				}
				case 0x00000070:{
					set_buff_icon_priority(input.readUInt32());
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
		return new SC_SPELL_BUFF_NOTI();
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
	public enum eNotiType{
		NEW(1),
		RESTAT(2),
		END(3);
		private int value;
		eNotiType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eNotiType v){
			return value == v.value;
		}
		public static eNotiType fromInt(int i){
			switch(i){
			case 1:
				return NEW;
			case 2:
				return RESTAT;
			case 3:
				return END;
			default:
				return null;
			}
		}
	}
	public enum eDurationShowType{
		TYPE_EFF_NONE(0),
		TYPE_EFF_PERCENT(1),
		TYPE_EFF_MINUTE(2),
		TYPE_EFF_PERCENT_ORC_SERVER(3),
		TYPE_EFF_EINHASAD_COOLTIME_MINUTE(4),
		TYPE_EFF_LEGACY_TIME(5),
		TYPE_EFF_VARIABLE_VALUE(6),
		TYPE_EFF_DAY_HOUR_MIN(7),
		TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC(8),
		TYPE_EFF_NSERVICE_TOPPING(9),
		TYPE_EFF_UNLIMIT(10),
		TYPE_EFF_CUSTOM(11),
		TYPE_EFF_COUNT(12);
		private int value;
		eDurationShowType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eDurationShowType v){
			return value == v.value;
		}
		public static eDurationShowType fromInt(int i){
			switch(i){
			case 0:
				return TYPE_EFF_NONE;
			case 1:
				return TYPE_EFF_PERCENT;
			case 2:
				return TYPE_EFF_MINUTE;
			case 3:
				return TYPE_EFF_PERCENT_ORC_SERVER;
			case 4:
				return TYPE_EFF_EINHASAD_COOLTIME_MINUTE;
			case 5:
				return TYPE_EFF_LEGACY_TIME;
			case 6:
				return TYPE_EFF_VARIABLE_VALUE;
			case 7:
				return TYPE_EFF_DAY_HOUR_MIN;
			case 8:
				return TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC;
			case 9:
				return TYPE_EFF_NSERVICE_TOPPING;
			case 10:
				return TYPE_EFF_UNLIMIT;
			case 11:
				return TYPE_EFF_CUSTOM;
			case 12:
				return TYPE_EFF_COUNT;
			default:
				return null;
			}
		}
	}
}
