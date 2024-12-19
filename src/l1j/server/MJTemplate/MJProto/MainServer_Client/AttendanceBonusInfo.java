package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class AttendanceBonusInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static AttendanceBonusInfo newInstance(ResultSet rs) throws SQLException{
		int itemId = rs.getInt("item_id");
		int count = rs.getInt("count");
		L1Item item = ItemTable.getInstance().getTemplate(itemId);
		if(item == null){
			System.out.println(String.format("아이템 아이디를 찾을 수 없습니다. [%d]", itemId));
			return null;
		}
		
		AttendanceBonusInfo bInfo = newInstance();
		bInfo.set_bonusType(AttendanceBonusType.GiveItem);		
		bInfo.set_itemId(item.getItemId());
		bInfo.set_itemCount(count);
		bInfo.set_itemName(item.getNameId());
		bInfo.set_itemInteractType(0x00);
		bInfo.set_itemIcon(item.getGfxId());
		bInfo.set_itemBless(item.getBless());
		bInfo.set_itemDesc(item.getNameId());
		
		L1ItemInstance inst = new L1ItemInstance();
		inst.setItem(item);
		bInfo.set_itemExtraDesc(inst.getStatusBytes());
		bInfo.set_itemAttr(0x17);
		return bInfo;
	}
	
	public static AttendanceBonusInfo newInstance(){
		return new AttendanceBonusInfo();
	}
	private AttendanceBonusType _bonusType;
	private int _itemId;
	private int _itemCount;
	private String _itemName;
	private int _itemInteractType;
	private int _itemIcon;
	private int _itemBless;
	private String _itemDesc;
	private byte[] _itemExtraDesc;
	private int _itemAttr;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	private AttendanceBonusInfo(){
	}
	public AttendanceBonusType get_bonusType(){
		return _bonusType;
	}
	public void set_bonusType(AttendanceBonusType val){
		_bit |= 0x00000001;
		_bonusType = val;
	}
	public boolean has_bonusType(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public int get_itemId(){
		return _itemId;
	}
	public void set_itemId(int val){
		_bit |= 0x00000002;
		_itemId = val;
	}
	public boolean has_itemId(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public int get_itemCount(){
		return _itemCount;
	}
	public void set_itemCount(int val){
		_bit |= 0x00000004;
		_itemCount = val;
	}
	public boolean has_itemCount(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public String get_itemName(){
		return _itemName;
	}
	public void set_itemName(String val){
		_bit |= 0x00000008;
		_itemName = val;
	}
	public boolean has_itemName(){
		return (_bit & 0x00000008) == 0x00000008;
	}
	public int get_itemInteractType(){
		return _itemInteractType;
	}
	public void set_itemInteractType(int val){
		_bit |= 0x00000010;
		_itemInteractType = val;
	}
	public boolean has_itemInteractType(){
		return (_bit & 0x00000010) == 0x00000010;
	}
	public int get_itemIcon(){
		return _itemIcon;
	}
	public void set_itemIcon(int val){
		_bit |= 0x00000020;
		_itemIcon = val;
	}
	public boolean has_itemIcon(){
		return (_bit & 0x00000020) == 0x00000020;
	}
	public int get_itemBless(){
		return _itemBless;
	}
	public void set_itemBless(int val){
		_bit |= 0x00000040;
		_itemBless = val;
	}
	public boolean has_itemBless(){
		return (_bit & 0x00000040) == 0x00000040;
	}
	public String get_itemDesc(){
		return _itemDesc;
	}
	public void set_itemDesc(String val){
		_bit |= 0x00000080;
		_itemDesc = val;
	}
	public boolean has_itemDesc(){
		return (_bit & 0x00000080) == 0x00000080;
	}
	public byte[] get_itemExtraDesc(){
		return _itemExtraDesc;
	}
	public void set_itemExtraDesc(byte[] val){
		_bit |= 0x00000100;
		_itemExtraDesc = val;
	}
	public boolean has_itemExtraDesc(){
		return (_bit & 0x00000100) == 0x00000100;
	}
	public int get_itemAttr(){
		return _itemAttr;
	}
	public void set_itemAttr(int val){
		_bit |= 0x00000200;
		_itemAttr = val;
	}
	public boolean has_itemAttr(){
		return (_bit & 0x00000200) == 0x00000200;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_bonusType())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _bonusType.toInt());
		if (has_itemId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _itemId);
		if (has_itemCount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _itemCount);
		if (has_itemName())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _itemName);
		if (has_itemInteractType())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _itemInteractType);
		if (has_itemIcon())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _itemIcon);
		if (has_itemBless())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _itemBless);
		if (has_itemDesc())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(8, _itemDesc);
		if (has_itemExtraDesc())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(9, _itemExtraDesc);
		if (has_itemAttr())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _itemAttr);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_bonusType()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemName()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemInteractType()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemIcon()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemBless()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemDesc()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemExtraDesc()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_itemAttr()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_bonusType()){
			output.writeEnum(1, _bonusType.toInt());
		}
		if (has_itemId()){
			output.wirteInt32(2, _itemId);
		}
		if (has_itemCount()){
			output.wirteInt32(3, _itemCount);
		}
		if (has_itemName()){
			output.writeString(4, _itemName);
		}
		if (has_itemInteractType()){
			output.wirteInt32(5, _itemInteractType);
		}
		if (has_itemIcon()){
			output.wirteInt32(6, _itemIcon);
		}
		if (has_itemBless()){
			output.wirteInt32(7, _itemBless);
		}
		if (has_itemDesc()){
			output.writeString(8, _itemDesc);
		}
		if (has_itemExtraDesc()){
			output.writeBytes(9, _itemExtraDesc);
		}
		if (has_itemAttr()){
			output.wirteInt32(10, _itemAttr);
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
					set_bonusType(AttendanceBonusType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_itemId(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_itemCount(input.readInt32());
					break;
				}
				case 0x00000022:{
					set_itemName(input.readString());
					break;
				}
				case 0x00000028:{
					set_itemInteractType(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_itemIcon(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_itemBless(input.readInt32());
					break;
				}
				case 0x00000042:{
					set_itemDesc(input.readString());
					break;
				}
				case 0x0000004A:{
					set_itemExtraDesc(input.readBytes());
					break;
				}
				case 0x00000050:{
					set_itemAttr(input.readInt32());
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
		return new AttendanceBonusInfo();
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
