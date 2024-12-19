package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class TradeItemInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	
	public static TradeItemInfo newInstance(){
		return new TradeItemInfo();
	}
	private int _index;
	private int _count;
	private int _price;
	private ItemInfo _item_info;
	private int _trade_info;
	private int _buy_count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TradeItemInfo(){
	}
	public int get_index(){
		return _index;
	}
	public void set_index(int val){
		_bit |= 0x1;
		_index = val;
	}
	public boolean has_index(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x2;
		_count = val;
	}
	public boolean has_count(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_price(){
		return _price;
	}
	public void set_price(int val){
		_bit |= 0x4;
		_price = val;
	}
	public boolean has_price(){
		return (_bit & 0x4) == 0x4;
	}
	public ItemInfo get_item_info(){
		return _item_info;
	}
	public void set_item_info(ItemInfo val){
		_bit |= 0x8;
		_item_info = val;
	}
	public boolean has_item_info(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_trade_info(){
		return _trade_info;
	}
	public void set_trade_info(int val){
		_bit |= 0x10;
		_trade_info = val;
	}
	public boolean has_trade_info(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_buy_count(){
		return _buy_count;
	}
	public void set_buy_count(int val){
		_bit |= 0x20;
		_buy_count = val;
	}
	public boolean has_buy_count(){
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
		if (has_index())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
		if (has_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
		if (has_price())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _price);
		if (has_item_info())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _item_info);
		if (has_trade_info())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _trade_info);
		if (has_buy_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _buy_count);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_index()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_price()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_info()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_index()){
			output.wirteInt32(1, _index);
		}
		if (has_count()){
			output.wirteInt32(2, _count);
		}
		if (has_price()){
			output.wirteInt32(3, _price);
		}
		if (has_item_info()){
			output.writeMessage(4, _item_info);
		}
		if (has_trade_info()){
			output.wirteInt32(5, _trade_info);
		}
		if (has_buy_count()){
			output.wirteInt32(6, _buy_count);
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
					set_index(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_count(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_price(input.readInt32());
					break;
				}
				case 0x00000022:{
					set_item_info((ItemInfo)input.readMessage(ItemInfo.newInstance()));
					break;
				}
				case 0x00000028:{
					set_trade_info(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_buy_count(input.readInt32());
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
		return new TradeItemInfo();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_item_info() && _item_info != null){
			_item_info.dispose();
			_item_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
