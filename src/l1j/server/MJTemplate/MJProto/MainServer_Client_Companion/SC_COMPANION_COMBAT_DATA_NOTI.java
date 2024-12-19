package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_COMPANION_COMBAT_DATA_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_COMPANION_COMBAT_DATA_NOTI newInstance(){
		return new SC_COMPANION_COMBAT_DATA_NOTI();
	}
	private int _obj_id;
	private int _melee_hit;
	private double _melee_cri_hit;
	private double _melee_cri_server_hit;
	private int _melee_dice_dmg;
	private int _melee_stat_dmg;
	private int _melee_wild_dmg;
	private int _melee_buff_dmg;
	private int _melee_elemental_dmg;
	private int _melee_last_dmg;
	private int _spell_id;
	private int _spell_hit;
	private int _spell_server_hit;
	private double _spell_cri_hit;
	private double _spell_cri_server_hit;
	private int _spell_dice_dmg;
	private int _spell_stat_dmg;
	private int _spell_wild_dmg;
	private int _spell_buff_dmg;
	private int _spell_dmg_multi;
	private int _spell_last_dmg;
	private int _total_dmg;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_COMPANION_COMBAT_DATA_NOTI(){
	}
	public int get_obj_id(){
		return _obj_id;
	}
	public void set_obj_id(int val){
		_bit |= 0x1;
		_obj_id = val;
	}
	public boolean has_obj_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_melee_hit(){
		return _melee_hit;
	}
	public void set_melee_hit(int val){
		_bit |= 0x2;
		_melee_hit = val;
	}
	public boolean has_melee_hit(){
		return (_bit & 0x2) == 0x2;
	}
	public double get_melee_cri_hit(){
		return _melee_cri_hit;
	}
	public void set_melee_cri_hit(double val){
		_bit |= 0x4;
		_melee_cri_hit = val;
	}
	public boolean has_melee_cri_hit(){
		return (_bit & 0x4) == 0x4;
	}
	public double get_melee_cri_server_hit(){
		return _melee_cri_server_hit;
	}
	public void set_melee_cri_server_hit(double val){
		_bit |= 0x8;
		_melee_cri_server_hit = val;
	}
	public boolean has_melee_cri_server_hit(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_melee_dice_dmg(){
		return _melee_dice_dmg;
	}
	public void set_melee_dice_dmg(int val){
		_bit |= 0x10;
		_melee_dice_dmg = val;
	}
	public boolean has_melee_dice_dmg(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_melee_stat_dmg(){
		return _melee_stat_dmg;
	}
	public void set_melee_stat_dmg(int val){
		_bit |= 0x20;
		_melee_stat_dmg = val;
	}
	public boolean has_melee_stat_dmg(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_melee_wild_dmg(){
		return _melee_wild_dmg;
	}
	public void set_melee_wild_dmg(int val){
		_bit |= 0x40;
		_melee_wild_dmg = val;
	}
	public boolean has_melee_wild_dmg(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_melee_buff_dmg(){
		return _melee_buff_dmg;
	}
	public void set_melee_buff_dmg(int val){
		_bit |= 0x80;
		_melee_buff_dmg = val;
	}
	public boolean has_melee_buff_dmg(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_melee_elemental_dmg(){
		return _melee_elemental_dmg;
	}
	public void set_melee_elemental_dmg(int val){
		_bit |= 0x100;
		_melee_elemental_dmg = val;
	}
	public boolean has_melee_elemental_dmg(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_melee_last_dmg(){
		return _melee_last_dmg;
	}
	public void set_melee_last_dmg(int val){
		_bit |= 0x200;
		_melee_last_dmg = val;
	}
	public boolean has_melee_last_dmg(){
		return (_bit & 0x200) == 0x200;
	}
	public int get_spell_id(){
		return _spell_id;
	}
	public void set_spell_id(int val){
		_bit |= 0x400;
		_spell_id = val;
	}
	public boolean has_spell_id(){
		return (_bit & 0x400) == 0x400;
	}
	public int get_spell_hit(){
		return _spell_hit;
	}
	public void set_spell_hit(int val){
		_bit |= 0x800;
		_spell_hit = val;
	}
	public boolean has_spell_hit(){
		return (_bit & 0x800) == 0x800;
	}
	public int get_spell_server_hit(){
		return _spell_server_hit;
	}
	public void set_spell_server_hit(int val){
		_bit |= 0x1000;
		_spell_server_hit = val;
	}
	public boolean has_spell_server_hit(){
		return (_bit & 0x1000) == 0x1000;
	}
	public double get_spell_cri_hit(){
		return _spell_cri_hit;
	}
	public void set_spell_cri_hit(double val){
		_bit |= 0x2000;
		_spell_cri_hit = val;
	}
	public boolean has_spell_cri_hit(){
		return (_bit & 0x2000) == 0x2000;
	}
	public double get_spell_cri_server_hit(){
		return _spell_cri_server_hit;
	}
	public void set_spell_cri_server_hit(double val){
		_bit |= 0x4000;
		_spell_cri_server_hit = val;
	}
	public boolean has_spell_cri_server_hit(){
		return (_bit & 0x4000) == 0x4000;
	}
	public int get_spell_dice_dmg(){
		return _spell_dice_dmg;
	}
	public void set_spell_dice_dmg(int val){
		_bit |= 0x8000;
		_spell_dice_dmg = val;
	}
	public boolean has_spell_dice_dmg(){
		return (_bit & 0x8000) == 0x8000;
	}
	public int get_spell_stat_dmg(){
		return _spell_stat_dmg;
	}
	public void set_spell_stat_dmg(int val){
		_bit |= 0x10000;
		_spell_stat_dmg = val;
	}
	public boolean has_spell_stat_dmg(){
		return (_bit & 0x10000) == 0x10000;
	}
	public int get_spell_wild_dmg(){
		return _spell_wild_dmg;
	}
	public void set_spell_wild_dmg(int val){
		_bit |= 0x20000;
		_spell_wild_dmg = val;
	}
	public boolean has_spell_wild_dmg(){
		return (_bit & 0x20000) == 0x20000;
	}
	public int get_spell_buff_dmg(){
		return _spell_buff_dmg;
	}
	public void set_spell_buff_dmg(int val){
		_bit |= 0x40000;
		_spell_buff_dmg = val;
	}
	public boolean has_spell_buff_dmg(){
		return (_bit & 0x40000) == 0x40000;
	}
	public int get_spell_dmg_multi(){
		return _spell_dmg_multi;
	}
	public void set_spell_dmg_multi(int val){
		_bit |= 0x80000;
		_spell_dmg_multi = val;
	}
	public boolean has_spell_dmg_multi(){
		return (_bit & 0x80000) == 0x80000;
	}
	public int get_spell_last_dmg(){
		return _spell_last_dmg;
	}
	public void set_spell_last_dmg(int val){
		_bit |= 0x100000;
		_spell_last_dmg = val;
	}
	public boolean has_spell_last_dmg(){
		return (_bit & 0x100000) == 0x100000;
	}
	public int get_total_dmg(){
		return _total_dmg;
	}
	public void set_total_dmg(int val){
		_bit |= 0x200000;
		_total_dmg = val;
	}
	public boolean has_total_dmg(){
		return (_bit & 0x200000) == 0x200000;
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
		if (has_obj_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _obj_id);
		if (has_melee_hit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _melee_hit);
		if (has_melee_cri_hit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(3, _melee_cri_hit);
		if (has_melee_cri_server_hit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(4, _melee_cri_server_hit);
		if (has_melee_dice_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _melee_dice_dmg);
		if (has_melee_stat_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _melee_stat_dmg);
		if (has_melee_wild_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _melee_wild_dmg);
		if (has_melee_buff_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _melee_buff_dmg);
		if (has_melee_elemental_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _melee_elemental_dmg);
		if (has_melee_last_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _melee_last_dmg);
		if (has_spell_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _spell_id);
		if (has_spell_hit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(12, _spell_hit);
		if (has_spell_server_hit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(13, _spell_server_hit);
		if (has_spell_cri_hit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(14, _spell_cri_hit);
		if (has_spell_cri_server_hit())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(15, _spell_cri_server_hit);
		if (has_spell_dice_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(16, _spell_dice_dmg);
		if (has_spell_stat_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(17, _spell_stat_dmg);
		if (has_spell_wild_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(18, _spell_wild_dmg);
		if (has_spell_buff_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(19, _spell_buff_dmg);
		if (has_spell_dmg_multi())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(20, _spell_dmg_multi);
		if (has_spell_last_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(21, _spell_last_dmg);
		if (has_total_dmg())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(22, _total_dmg);
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
		if (has_melee_hit()){
			output.wirteInt32(2, _melee_hit);
		}
		if (has_melee_cri_hit()){
			output.writeDouble(3, _melee_cri_hit);
		}
		if (has_melee_cri_server_hit()){
			output.writeDouble(4, _melee_cri_server_hit);
		}
		if (has_melee_dice_dmg()){
			output.wirteInt32(5, _melee_dice_dmg);
		}
		if (has_melee_stat_dmg()){
			output.wirteInt32(6, _melee_stat_dmg);
		}
		if (has_melee_wild_dmg()){
			output.wirteInt32(7, _melee_wild_dmg);
		}
		if (has_melee_buff_dmg()){
			output.wirteInt32(8, _melee_buff_dmg);
		}
		if (has_melee_elemental_dmg()){
			output.wirteInt32(9, _melee_elemental_dmg);
		}
		if (has_melee_last_dmg()){
			output.wirteInt32(10, _melee_last_dmg);
		}
		if (has_spell_id()){
			output.wirteInt32(11, _spell_id);
		}
		if (has_spell_hit()){
			output.wirteInt32(12, _spell_hit);
		}
		if (has_spell_server_hit()){
			output.wirteInt32(13, _spell_server_hit);
		}
		if (has_spell_cri_hit()){
			output.writeDouble(14, _spell_cri_hit);
		}
		if (has_spell_cri_server_hit()){
			output.writeDouble(15, _spell_cri_server_hit);
		}
		if (has_spell_dice_dmg()){
			output.wirteInt32(16, _spell_dice_dmg);
		}
		if (has_spell_stat_dmg()){
			output.wirteInt32(17, _spell_stat_dmg);
		}
		if (has_spell_wild_dmg()){
			output.wirteInt32(18, _spell_wild_dmg);
		}
		if (has_spell_buff_dmg()){
			output.wirteInt32(19, _spell_buff_dmg);
		}
		if (has_spell_dmg_multi()){
			output.wirteInt32(20, _spell_dmg_multi);
		}
		if (has_spell_last_dmg()){
			output.wirteInt32(21, _spell_last_dmg);
		}
		if (has_total_dmg()){
			output.wirteInt32(22, _total_dmg);
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
				case 0x00000010:{
					set_melee_hit(input.readInt32());
					break;
				}
				case 0x00000019:{
					set_melee_cri_hit(input.readDouble());
					break;
				}
				case 0x00000021:{
					set_melee_cri_server_hit(input.readDouble());
					break;
				}
				case 0x00000028:{
					set_melee_dice_dmg(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_melee_stat_dmg(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_melee_wild_dmg(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_melee_buff_dmg(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_melee_elemental_dmg(input.readInt32());
					break;
				}
				case 0x00000050:{
					set_melee_last_dmg(input.readInt32());
					break;
				}
				case 0x00000058:{
					set_spell_id(input.readInt32());
					break;
				}
				case 0x00000060:{
					set_spell_hit(input.readInt32());
					break;
				}
				case 0x00000068:{
					set_spell_server_hit(input.readInt32());
					break;
				}
				case 0x00000071:{
					set_spell_cri_hit(input.readDouble());
					break;
				}
				case 0x00000079:{
					set_spell_cri_server_hit(input.readDouble());
					break;
				}
				case 0x00000080:{
					set_spell_dice_dmg(input.readInt32());
					break;
				}
				case 0x00000088:{
					set_spell_stat_dmg(input.readInt32());
					break;
				}
				case 0x00000090:{
					set_spell_wild_dmg(input.readInt32());
					break;
				}
				case 0x00000098:{
					set_spell_buff_dmg(input.readInt32());
					break;
				}
				case 0x000000A0:{
					set_spell_dmg_multi(input.readInt32());
					break;
				}
				case 0x000000A8:{
					set_spell_last_dmg(input.readInt32());
					break;
				}
				case 0x000000B0:{
					set_total_dmg(input.readInt32());
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
		return new SC_COMPANION_COMBAT_DATA_NOTI();
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
