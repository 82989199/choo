package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_STAT_RENEWAL_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_STAT_RENEWAL_INFO_NOTI newInstance(){
		return new SC_STAT_RENEWAL_INFO_NOTI();
	}
	private int _infotype;
	private STR_INFO _strinfo;
	private INT_INFO _intinfo;
	private WIS_INFO _wisinfo;
	private DEX_INFO _dexinfo;
	private CON_INFO _coninfo;
	private CHA_INFO _chainfo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_STAT_RENEWAL_INFO_NOTI(){
	}
	public int get_infotype(){
		return _infotype;
	}
	public void set_infotype(int val){
		_bit |= 0x1;
		_infotype = val;
	}
	public boolean has_infotype(){
		return (_bit & 0x1) == 0x1;
	}
	public STR_INFO get_strinfo(){
		return _strinfo;
	}
	public void set_strinfo(STR_INFO val){
		_bit |= 0x2;
		_strinfo = val;
	}
	public boolean has_strinfo(){
		return (_bit & 0x2) == 0x2;
	}
	public INT_INFO get_intinfo(){
		return _intinfo;
	}
	public void set_intinfo(INT_INFO val){
		_bit |= 0x4;
		_intinfo = val;
	}
	public boolean has_intinfo(){
		return (_bit & 0x4) == 0x4;
	}
	public WIS_INFO get_wisinfo(){
		return _wisinfo;
	}
	public void set_wisinfo(WIS_INFO val){
		_bit |= 0x8;
		_wisinfo = val;
	}
	public boolean has_wisinfo(){
		return (_bit & 0x8) == 0x8;
	}
	public DEX_INFO get_dexinfo(){
		return _dexinfo;
	}
	public void set_dexinfo(DEX_INFO val){
		_bit |= 0x10;
		_dexinfo = val;
	}
	public boolean has_dexinfo(){
		return (_bit & 0x10) == 0x10;
	}
	public CON_INFO get_coninfo(){
		return _coninfo;
	}
	public void set_coninfo(CON_INFO val){
		_bit |= 0x20;
		_coninfo = val;
	}
	public boolean has_coninfo(){
		return (_bit & 0x20) == 0x20;
	}
	public CHA_INFO get_chainfo(){
		return _chainfo;
	}
	public void set_chainfo(CHA_INFO val){
		_bit |= 0x40;
		_chainfo = val;
	}
	public boolean has_chainfo(){
		return (_bit & 0x40) == 0x40;
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
		if (has_infotype())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeSInt32Size(1, _infotype);
		if (has_strinfo())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _strinfo);
		if (has_intinfo())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _intinfo);
		if (has_wisinfo())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _wisinfo);
		if (has_dexinfo())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _dexinfo);
		if (has_coninfo())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _coninfo);
		if (has_chainfo())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _chainfo);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_infotype()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_infotype()){
			output.writeSInt32(1, _infotype);
		}
		if (has_strinfo()){
			output.writeMessage(2, _strinfo);
		}
		if (has_intinfo()){
			output.writeMessage(3, _intinfo);
		}
		if (has_wisinfo()){
			output.writeMessage(4, _wisinfo);
		}
		if (has_dexinfo()){
			output.writeMessage(5, _dexinfo);
		}
		if (has_coninfo()){
			output.writeMessage(6, _coninfo);
		}
		if (has_chainfo()){
			output.writeMessage(7, _chainfo);
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
					set_infotype(input.readSInt32());
					break;
				}
				case 0x00000012:{
					set_strinfo((STR_INFO)input.readMessage(STR_INFO.newInstance()));
					break;
				}
				case 0x0000001A:{
					set_intinfo((INT_INFO)input.readMessage(INT_INFO.newInstance()));
					break;
				}
				case 0x00000022:{
					set_wisinfo((WIS_INFO)input.readMessage(WIS_INFO.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_dexinfo((DEX_INFO)input.readMessage(DEX_INFO.newInstance()));
					break;
				}
				case 0x00000032:{
					set_coninfo((CON_INFO)input.readMessage(CON_INFO.newInstance()));
					break;
				}
				case 0x0000003A:{
					set_chainfo((CHA_INFO)input.readMessage(CHA_INFO.newInstance()));
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
		return new SC_STAT_RENEWAL_INFO_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_strinfo() && _strinfo != null){
			_strinfo.dispose();
			_strinfo = null;
		}
		if (has_intinfo() && _intinfo != null){
			_intinfo.dispose();
			_intinfo = null;
		}
		if (has_wisinfo() && _wisinfo != null){
			_wisinfo.dispose();
			_wisinfo = null;
		}
		if (has_dexinfo() && _dexinfo != null){
			_dexinfo.dispose();
			_dexinfo = null;
		}
		if (has_coninfo() && _coninfo != null){
			_coninfo.dispose();
			_coninfo = null;
		}
		if (has_chainfo() && _chainfo != null){
			_chainfo.dispose();
			_chainfo = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class STR_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static STR_INFO newInstance(){
			return new STR_INFO();
		}
		private int _damagebonus;
		private int _hitbonus;
		private int _cribonus;
		private int _carrybonus;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private STR_INFO(){
		}
		public int get_damagebonus(){
			return _damagebonus;
		}
		public void set_damagebonus(int val){
			_bit |= 0x1;
			_damagebonus = val;
		}
		public boolean has_damagebonus(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_hitbonus(){
			return _hitbonus;
		}
		public void set_hitbonus(int val){
			_bit |= 0x2;
			_hitbonus = val;
		}
		public boolean has_hitbonus(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_cribonus(){
			return _cribonus;
		}
		public void set_cribonus(int val){
			_bit |= 0x4;
			_cribonus = val;
		}
		public boolean has_cribonus(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_carrybonus(){
			return _carrybonus;
		}
		public void set_carrybonus(int val){
			_bit |= 0x8;
			_carrybonus = val;
		}
		public boolean has_carrybonus(){
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
			if (has_damagebonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _damagebonus);
			if (has_hitbonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _hitbonus);
			if (has_cribonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _cribonus);
			if (has_carrybonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _carrybonus);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_damagebonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_hitbonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cribonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_carrybonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_damagebonus()){
				output.wirteInt32(1, _damagebonus);
			}
			if (has_hitbonus()){
				output.wirteInt32(2, _hitbonus);
			}
			if (has_cribonus()){
				output.wirteInt32(3, _cribonus);
			}
			if (has_carrybonus()){
				output.wirteInt32(4, _carrybonus);
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
						set_damagebonus(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_hitbonus(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_cribonus(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_carrybonus(input.readInt32());
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
			return new STR_INFO();
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
	public static class INT_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static INT_INFO newInstance(){
			return new INT_INFO();
		}
		private int _damagebonus;
		private int _hitbonus;
		private int _cribonus;
		private int _magicbonus;
		private int _reducemp;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private INT_INFO(){
		}
		public int get_damagebonus(){
			return _damagebonus;
		}
		public void set_damagebonus(int val){
			_bit |= 0x1;
			_damagebonus = val;
		}
		public boolean has_damagebonus(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_hitbonus(){
			return _hitbonus;
		}
		public void set_hitbonus(int val){
			_bit |= 0x2;
			_hitbonus = val;
		}
		public boolean has_hitbonus(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_cribonus(){
			return _cribonus;
		}
		public void set_cribonus(int val){
			_bit |= 0x4;
			_cribonus = val;
		}
		public boolean has_cribonus(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_magicbonus(){
			return _magicbonus;
		}
		public void set_magicbonus(int val){
			_bit |= 0x8;
			_magicbonus = val;
		}
		public boolean has_magicbonus(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_reducemp(){
			return _reducemp;
		}
		public void set_reducemp(int val){
			_bit |= 0x10;
			_reducemp = val;
		}
		public boolean has_reducemp(){
			return (_bit & 0x10) == 0x10;
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
			if (has_damagebonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _damagebonus);
			if (has_hitbonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _hitbonus);
			if (has_cribonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _cribonus);
			if (has_magicbonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _magicbonus);
			if (has_reducemp())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _reducemp);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_damagebonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_hitbonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cribonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_magicbonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_reducemp()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_damagebonus()){
				output.wirteInt32(1, _damagebonus);
			}
			if (has_hitbonus()){
				output.wirteInt32(2, _hitbonus);
			}
			if (has_cribonus()){
				output.wirteInt32(3, _cribonus);
			}
			if (has_magicbonus()){
				output.wirteInt32(4, _magicbonus);
			}
			if (has_reducemp()){
				output.wirteInt32(5, _reducemp);
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
						set_damagebonus(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_hitbonus(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_cribonus(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_magicbonus(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_reducemp(input.readInt32());
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
			return new INT_INFO();
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
	public static class WIS_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static WIS_INFO newInstance(){
			return new WIS_INFO();
		}
		private int _mpregen;
		private int _mpincpotion;
		private int _mrbonus;
		private int _maxmplow;
		private int _maxmphigh;
		private int _magicpoint;
		private int _basemaxmp;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private WIS_INFO(){
		}
		public int get_mpregen(){
			return _mpregen;
		}
		public void set_mpregen(int val){
			_bit |= 0x1;
			_mpregen = val;
		}
		public boolean has_mpregen(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_mpincpotion(){
			return _mpincpotion;
		}
		public void set_mpincpotion(int val){
			_bit |= 0x2;
			_mpincpotion = val;
		}
		public boolean has_mpincpotion(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_mrbonus(){
			return _mrbonus;
		}
		public void set_mrbonus(int val){
			_bit |= 0x4;
			_mrbonus = val;
		}
		public boolean has_mrbonus(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_maxmplow(){
			return _maxmplow;
		}
		public void set_maxmplow(int val){
			_bit |= 0x8;
			_maxmplow = val;
		}
		public boolean has_maxmplow(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_maxmphigh(){
			return _maxmphigh;
		}
		public void set_maxmphigh(int val){
			_bit |= 0x10;
			_maxmphigh = val;
		}
		public boolean has_maxmphigh(){
			return (_bit & 0x10) == 0x10;
		}
		public int get_magicpoint(){
			return _magicpoint;
		}
		public void set_magicpoint(int val){
			_bit |= 0x20;
			_magicpoint = val;
		}
		public boolean has_magicpoint(){
			return (_bit & 0x20) == 0x20;
		}
		public int get_basemaxmp(){
			return _basemaxmp;
		}
		public void set_basemaxmp(int val){
			_bit |= 0x40;
			_basemaxmp = val;
		}
		public boolean has_basemaxmp(){
			return (_bit & 0x40) == 0x40;
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
			if (has_mpregen())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _mpregen);
			if (has_mpincpotion())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _mpincpotion);
			if (has_mrbonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _mrbonus);
			if (has_maxmplow())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _maxmplow);
			if (has_maxmphigh())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _maxmphigh);
			if (has_magicpoint())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _magicpoint);
			if (has_basemaxmp())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _basemaxmp);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_mpregen()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_mpincpotion()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_mrbonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_maxmplow()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_maxmphigh()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_mpregen()){
				output.wirteInt32(1, _mpregen);
			}
			if (has_mpincpotion()){
				output.wirteInt32(2, _mpincpotion);
			}
			if (has_mrbonus()){
				output.wirteInt32(3, _mrbonus);
			}
			if (has_maxmplow()){
				output.wirteInt32(4, _maxmplow);
			}
			if (has_maxmphigh()){
				output.wirteInt32(5, _maxmphigh);
			}
			if (has_magicpoint()){
				output.wirteInt32(6, _magicpoint);
			}
			if (has_basemaxmp()){
				output.wirteInt32(7, _basemaxmp);
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
						set_mpregen(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_mpincpotion(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_mrbonus(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_maxmplow(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_maxmphigh(input.readInt32());
						break;
					}
					case 0x00000030:{
						set_magicpoint(input.readInt32());
						break;
					}
					case 0x00000038:{
						set_basemaxmp(input.readInt32());
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
			return new WIS_INFO();
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
	public static class DEX_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static DEX_INFO newInstance(){
			return new DEX_INFO();
		}
		private int _damagebonus;
		private int _hitbonus;
		private int _cribonus;
		private int _acbonus;
		private int _evasionbonus;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private DEX_INFO(){
		}
		public int get_damagebonus(){
			return _damagebonus;
		}
		public void set_damagebonus(int val){
			_bit |= 0x1;
			_damagebonus = val;
		}
		public boolean has_damagebonus(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_hitbonus(){
			return _hitbonus;
		}
		public void set_hitbonus(int val){
			_bit |= 0x2;
			_hitbonus = val;
		}
		public boolean has_hitbonus(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_cribonus(){
			return _cribonus;
		}
		public void set_cribonus(int val){
			_bit |= 0x4;
			_cribonus = val;
		}
		public boolean has_cribonus(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_acbonus(){
			return _acbonus;
		}
		public void set_acbonus(int val){
			_bit |= 0x8;
			_acbonus = val;
		}
		public boolean has_acbonus(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_evasionbonus(){
			return _evasionbonus;
		}
		public void set_evasionbonus(int val){
			_bit |= 0x10;
			_evasionbonus = val;
		}
		public boolean has_evasionbonus(){
			return (_bit & 0x10) == 0x10;
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
			if (has_damagebonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _damagebonus);
			if (has_hitbonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _hitbonus);
			if (has_cribonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _cribonus);
			if (has_acbonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _acbonus);
			if (has_evasionbonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _evasionbonus);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_damagebonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_hitbonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cribonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_acbonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_evasionbonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_damagebonus()){
				output.wirteInt32(1, _damagebonus);
			}
			if (has_hitbonus()){
				output.wirteInt32(2, _hitbonus);
			}
			if (has_cribonus()){
				output.wirteInt32(3, _cribonus);
			}
			if (has_acbonus()){
				output.wirteInt32(4, _acbonus);
			}
			if (has_evasionbonus()){
				output.wirteInt32(5, _evasionbonus);
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
						set_damagebonus(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_hitbonus(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_cribonus(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_acbonus(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_evasionbonus(input.readInt32());
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
			return new DEX_INFO();
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
	public static class CON_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CON_INFO newInstance(){
			return new CON_INFO();
		}
		private int _hpregen;
		private int _hpincpotion;
		private int _carrybonus;
		private int _maxhpinc;
		private int _hitpoint;
		private int _basemaxhp;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CON_INFO(){
		}
		public int get_hpregen(){
			return _hpregen;
		}
		public void set_hpregen(int val){
			_bit |= 0x1;
			_hpregen = val;
		}
		public boolean has_hpregen(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_hpincpotion(){
			return _hpincpotion;
		}
		public void set_hpincpotion(int val){
			_bit |= 0x2;
			_hpincpotion = val;
		}
		public boolean has_hpincpotion(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_carrybonus(){
			return _carrybonus;
		}
		public void set_carrybonus(int val){
			_bit |= 0x4;
			_carrybonus = val;
		}
		public boolean has_carrybonus(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_maxhpinc(){
			return _maxhpinc;
		}
		public void set_maxhpinc(int val){
			_bit |= 0x8;
			_maxhpinc = val;
		}
		public boolean has_maxhpinc(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_hitpoint(){
			return _hitpoint;
		}
		public void set_hitpoint(int val){
			_bit |= 0x10;
			_hitpoint = val;
		}
		public boolean has_hitpoint(){
			return (_bit & 0x10) == 0x10;
		}
		public int get_basemaxhp(){
			return _basemaxhp;
		}
		public void set_basemaxhp(int val){
			_bit |= 0x20;
			_basemaxhp = val;
		}
		public boolean has_basemaxhp(){
			return (_bit & 0x20) == 0x20;
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
			if (has_hpregen())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _hpregen);
			if (has_hpincpotion())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _hpincpotion);
			if (has_carrybonus())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _carrybonus);
			if (has_maxhpinc())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _maxhpinc);
			if (has_hitpoint())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _hitpoint);
			if (has_basemaxhp())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _basemaxhp);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_hpregen()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_hpincpotion()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_carrybonus()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_maxhpinc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_hpregen()){
				output.wirteInt32(1, _hpregen);
			}
			if (has_hpincpotion()){
				output.wirteInt32(2, _hpincpotion);
			}
			if (has_carrybonus()){
				output.wirteInt32(3, _carrybonus);
			}
			if (has_maxhpinc()){
				output.wirteInt32(4, _maxhpinc);
			}
			if (has_hitpoint()){
				output.wirteInt32(5, _hitpoint);
			}
			if (has_basemaxhp()){
				output.wirteInt32(6, _basemaxhp);
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
						set_hpregen(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_hpincpotion(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_carrybonus(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_maxhpinc(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_hitpoint(input.readInt32());
						break;
					}
					case 0x00000030:{
						set_basemaxhp(input.readInt32());
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
			return new CON_INFO();
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
	public static class CHA_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static CHA_INFO newInstance(){
			return new CHA_INFO();
		}
		private boolean _dummy;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CHA_INFO(){
		}
		public boolean get_dummy(){
			return _dummy;
		}
		public void set_dummy(boolean val){
			_bit |= 0x1;
			_dummy = val;
		}
		public boolean has_dummy(){
			return (_bit & 0x1) == 0x1;
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
			if (has_dummy())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _dummy);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_dummy()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_dummy()){
				output.writeBool(1, _dummy);
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
						set_dummy(input.readBool());
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
			return new CHA_INFO();
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
	public enum INFO_TYPE{
		STAT_NORMAL(1),
		STAT_CALC(2),
		STAT_LEVUP(4),
		STAT_CALC_START(8),
		STAT_CALC_INGAME(16);
		private int value;
		INFO_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(INFO_TYPE v){
			return value == v.value;
		}
		public static INFO_TYPE fromInt(int i){
			switch(i){
			case 1:
				return STAT_NORMAL;
			case 2:
				return STAT_CALC;
			case 4:
				return STAT_LEVUP;
			case 8:
				return STAT_CALC_START;
			case 16:
				return STAT_CALC_INGAME;
			default:
				return null;
			}
		}
	}
}
