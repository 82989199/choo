package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CompanionT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CompanionT newInstance(){
		return new CompanionT();
	}
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CompanionT(){
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
		return new CompanionT();
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
	public static class StatT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static StatT newInstance(){
			return new StatT();
		}
		private java.util.LinkedList<BaseStatBonusT> _BaseStatBonus;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StatT(){
		}
		public java.util.LinkedList<BaseStatBonusT> get_BaseStatBonus(){
			return _BaseStatBonus;
		}
		public void add_BaseStatBonus(BaseStatBonusT val){
			if(!has_BaseStatBonus()){
				_BaseStatBonus = new java.util.LinkedList<BaseStatBonusT>();
				_bit |= 0x1;
			}
			_BaseStatBonus.add(val);
		}
		public boolean has_BaseStatBonus(){
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
			if (has_BaseStatBonus()){
				for(BaseStatBonusT val : _BaseStatBonus)
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_BaseStatBonus()){
				for(BaseStatBonusT val : _BaseStatBonus){
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
			if (has_BaseStatBonus()){
				for(BaseStatBonusT val : _BaseStatBonus){
					output.writeMessage(1, val);
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
					case 0x0000000A:{
						add_BaseStatBonus((BaseStatBonusT)input.readMessage(BaseStatBonusT.newInstance()));
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
			return new StatT();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_BaseStatBonus()){
				for(BaseStatBonusT val : _BaseStatBonus)
					val.dispose();
				_BaseStatBonus.clear();
				_BaseStatBonus = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public static class BaseStatBonusT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static BaseStatBonusT newInstance(){
				return new BaseStatBonusT();
			}
			private int _id;
			private eStatType _statType;
			private int _value;
			private int _meleeDmg;
			private int _meleeHit;
			private int _regenHP;
			private int _AC;
			private int _spellDmg;
			private int _spellHit;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private BaseStatBonusT(){
			}
			public int get_id(){
				return _id;
			}
			public void set_id(int val){
				_bit |= 0x1;
				_id = val;
			}
			public boolean has_id(){
				return (_bit & 0x1) == 0x1;
			}
			public eStatType get_statType(){
				return _statType;
			}
			public void set_statType(eStatType val){
				_bit |= 0x2;
				_statType = val;
			}
			public boolean has_statType(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_value(){
				return _value;
			}
			public void set_value(int val){
				_bit |= 0x4;
				_value = val;
			}
			public boolean has_value(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_meleeDmg(){
				return _meleeDmg;
			}
			public void set_meleeDmg(int val){
				_bit |= 0x8;
				_meleeDmg = val;
			}
			public boolean has_meleeDmg(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_meleeHit(){
				return _meleeHit;
			}
			public void set_meleeHit(int val){
				_bit |= 0x10;
				_meleeHit = val;
			}
			public boolean has_meleeHit(){
				return (_bit & 0x10) == 0x10;
			}
			public int get_regenHP(){
				return _regenHP;
			}
			public void set_regenHP(int val){
				_bit |= 0x20;
				_regenHP = val;
			}
			public boolean has_regenHP(){
				return (_bit & 0x20) == 0x20;
			}
			public int get_AC(){
				return _AC;
			}
			public void set_AC(int val){
				_bit |= 0x40;
				_AC = val;
			}
			public boolean has_AC(){
				return (_bit & 0x40) == 0x40;
			}
			public int get_spellDmg(){
				return _spellDmg;
			}
			public void set_spellDmg(int val){
				_bit |= 0x80;
				_spellDmg = val;
			}
			public boolean has_spellDmg(){
				return (_bit & 0x80) == 0x80;
			}
			public int get_spellHit(){
				return _spellHit;
			}
			public void set_spellHit(int val){
				_bit |= 0x100;
				_spellHit = val;
			}
			public boolean has_spellHit(){
				return (_bit & 0x100) == 0x100;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
				if (has_statType())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _statType.toInt());
				if (has_value())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _value);
				if (has_meleeDmg())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _meleeDmg);
				if (has_meleeHit())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _meleeHit);
				if (has_regenHP())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _regenHP);
				if (has_AC())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _AC);
				if (has_spellDmg())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _spellDmg);
				if (has_spellHit())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _spellHit);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_statType()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_value()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_id()){
					output.wirteInt32(1, _id);
				}
				if (has_statType()){
					output.writeEnum(2, _statType.toInt());
				}
				if (has_value()){
					output.wirteInt32(3, _value);
				}
				if (has_meleeDmg()){
					output.wirteInt32(4, _meleeDmg);
				}
				if (has_meleeHit()){
					output.wirteInt32(5, _meleeHit);
				}
				if (has_regenHP()){
					output.wirteInt32(6, _regenHP);
				}
				if (has_AC()){
					output.wirteInt32(7, _AC);
				}
				if (has_spellDmg()){
					output.wirteInt32(8, _spellDmg);
				}
				if (has_spellHit()){
					output.wirteInt32(9, _spellHit);
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
							set_id(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_statType(eStatType.fromInt(input.readEnum()));
							break;
						}
						case 0x00000018:{
							set_value(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_meleeDmg(input.readInt32());
							break;
						}
						case 0x00000028:{
							set_meleeHit(input.readInt32());
							break;
						}
						case 0x00000030:{
							set_regenHP(input.readInt32());
							break;
						}
						case 0x00000038:{
							set_AC(input.readInt32());
							break;
						}
						case 0x00000040:{
							set_spellDmg(input.readInt32());
							break;
						}
						case 0x00000048:{
							set_spellHit(input.readInt32());
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
				return new BaseStatBonusT();
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
	}
	public static class SkillEnchantTierT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SkillEnchantTierT newInstance(){
			return new SkillEnchantTierT();
		}
		private int _tier;
		private EnchantCostT _EnchantCost;
		private OpenCostT _OpenCost;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SkillEnchantTierT(){
		}
		public int get_tier(){
			return _tier;
		}
		public void set_tier(int val){
			_bit |= 0x1;
			_tier = val;
		}
		public boolean has_tier(){
			return (_bit & 0x1) == 0x1;
		}
		public EnchantCostT get_EnchantCost(){
			return _EnchantCost;
		}
		public void set_EnchantCost(EnchantCostT val){
			_bit |= 0x2;
			_EnchantCost = val;
		}
		public boolean has_EnchantCost(){
			return (_bit & 0x2) == 0x2;
		}
		public OpenCostT get_OpenCost(){
			return _OpenCost;
		}
		public void set_OpenCost(OpenCostT val){
			_bit |= 0x4;
			_OpenCost = val;
		}
		public boolean has_OpenCost(){
			return (_bit & 0x4) == 0x4;
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
			if (has_tier())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _tier);
			if (has_EnchantCost())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _EnchantCost);
			if (has_OpenCost())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _OpenCost);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_tier()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_EnchantCost()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_OpenCost()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_tier()){
				output.writeUInt32(1, _tier);
			}
			if (has_EnchantCost()){
				output.writeMessage(2, _EnchantCost);
			}
			if (has_OpenCost()){
				output.writeMessage(3, _OpenCost);
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
						set_tier(input.readUInt32());
						break;
					}
					case 0x00000012:{
						set_EnchantCost((EnchantCostT)input.readMessage(EnchantCostT.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_OpenCost((OpenCostT)input.readMessage(OpenCostT.newInstance()));
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
			return new SkillEnchantTierT();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_EnchantCost() && _EnchantCost != null){
				_EnchantCost.dispose();
				_EnchantCost = null;
			}
			if (has_OpenCost() && _OpenCost != null){
				_OpenCost.dispose();
				_OpenCost = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public static class EnchantCostT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static EnchantCostT newInstance(){
				return new EnchantCostT();
			}
			private java.util.LinkedList<Integer> _friendship;
			private int _catalystItem;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private EnchantCostT(){
			}
			public java.util.LinkedList<Integer> get_friendship(){
				return _friendship;
			}
			public void add_friendship(int val){
				if(!has_friendship()){
					_friendship = new java.util.LinkedList<Integer>();
					_bit |= 0x1;
				}
				_friendship.add(val);
			}
			public boolean has_friendship(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_catalystItem(){
				return _catalystItem;
			}
			public void set_catalystItem(int val){
				_bit |= 0x2;
				_catalystItem = val;
			}
			public boolean has_catalystItem(){
				return (_bit & 0x2) == 0x2;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_friendship()){
					for(int val : _friendship)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, val);
				}
				if (has_catalystItem())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _catalystItem);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_friendship()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_friendship()){
					for(int val : _friendship){
						output.writeUInt32(1, val);
					}
				}
				if (has_catalystItem()){
					output.writeUInt32(2, _catalystItem);
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
							add_friendship(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_catalystItem(input.readUInt32());
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
				return new EnchantCostT();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_friendship()){
					_friendship.clear();
					_friendship = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		public static class OpenCostT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static OpenCostT newInstance(){
				return new OpenCostT();
			}
			private int _level;
			private int _minEnchant;
			private int _friendship;
			private int _adena;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private OpenCostT(){
			}
			public int get_level(){
				return _level;
			}
			public void set_level(int val){
				_bit |= 0x1;
				_level = val;
			}
			public boolean has_level(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_minEnchant(){
				return _minEnchant;
			}
			public void set_minEnchant(int val){
				_bit |= 0x2;
				_minEnchant = val;
			}
			public boolean has_minEnchant(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_friendship(){
				return _friendship;
			}
			public void set_friendship(int val){
				_bit |= 0x4;
				_friendship = val;
			}
			public boolean has_friendship(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_adena(){
				return _adena;
			}
			public void set_adena(int val){
				_bit |= 0x8;
				_adena = val;
			}
			public boolean has_adena(){
				return (_bit & 0x8) == 0x8;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_level())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _level);
				if (has_minEnchant())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _minEnchant);
				if (has_friendship())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _friendship);
				if (has_adena())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _adena);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_level()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_level()){
					output.writeUInt32(1, _level);
				}
				if (has_minEnchant()){
					output.writeUInt32(2, _minEnchant);
				}
				if (has_friendship()){
					output.writeUInt32(3, _friendship);
				}
				if (has_adena()){
					output.writeUInt32(4, _adena);
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
							set_level(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_minEnchant(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_friendship(input.readUInt32());
							break;
						}
						case 0x00000020:{
							set_adena(input.readUInt32());
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
				return new OpenCostT();
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
	}
	public static class WildSkillT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static WildSkillT newInstance(){
			return new WildSkillT();
		}
		private java.util.LinkedList<SkillT> _Skill;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private WildSkillT(){
		}
		public java.util.LinkedList<SkillT> get_Skill(){
			return _Skill;
		}
		public void add_Skill(SkillT val){
			if(!has_Skill()){
				_Skill = new java.util.LinkedList<SkillT>();
				_bit |= 0x1;
			}
			_Skill.add(val);
		}
		public boolean has_Skill(){
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
			if (has_Skill()){
				for(SkillT val : _Skill)
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_Skill()){
				for(SkillT val : _Skill){
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
			if (has_Skill()){
				for(SkillT val : _Skill){
					output.writeMessage(1, val);
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
					case 0x0000000A:{
						add_Skill((SkillT)input.readMessage(SkillT.newInstance()));
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
			return new WildSkillT();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_Skill()){
				for(SkillT val : _Skill)
					val.dispose();
				_Skill.clear();
				_Skill = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public static class SkillT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static SkillT newInstance(){
				return new SkillT();
			}
			private int _id;
			private int _descNum;
			private java.util.LinkedList<EnchantBonusT> _EnchantBonus;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private SkillT(){
			}
			public int get_id(){
				return _id;
			}
			public void set_id(int val){
				_bit |= 0x1;
				_id = val;
			}
			public boolean has_id(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_descNum(){
				return _descNum;
			}
			public void set_descNum(int val){
				_bit |= 0x2;
				_descNum = val;
			}
			public boolean has_descNum(){
				return (_bit & 0x2) == 0x2;
			}
			public java.util.LinkedList<EnchantBonusT> get_EnchantBonus(){
				return _EnchantBonus;
			}
			public void add_EnchantBonus(EnchantBonusT val){
				if(!has_EnchantBonus()){
					_EnchantBonus = new java.util.LinkedList<EnchantBonusT>();
					_bit |= 0x4;
				}
				_EnchantBonus.add(val);
			}
			public boolean has_EnchantBonus(){
				return (_bit & 0x4) == 0x4;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
				if (has_descNum())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _descNum);
				if (has_EnchantBonus()){
					for(EnchantBonusT val : _EnchantBonus)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_descNum()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_EnchantBonus()){
					for(EnchantBonusT val : _EnchantBonus){
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
				if (has_id()){
					output.wirteInt32(1, _id);
				}
				if (has_descNum()){
					output.wirteInt32(2, _descNum);
				}
				if (has_EnchantBonus()){
					for(EnchantBonusT val : _EnchantBonus){
						output.writeMessage(3, val);
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
							set_id(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_descNum(input.readInt32());
							break;
						}
						case 0x0000001A:{
							add_EnchantBonus((EnchantBonusT)input.readMessage(EnchantBonusT.newInstance()));
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
				return new SkillT();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_EnchantBonus()){
					for(EnchantBonusT val : _EnchantBonus)
						val.dispose();
					_EnchantBonus.clear();
					_EnchantBonus = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
			public static class EnchantBonusT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
				public static EnchantBonusT newInstance(){
					return new EnchantBonusT();
				}
				private int _value;
				private int _meleeDmg;
				private int _meleeHit;
				private double _meleeCriHit;
				private int _ignoreReduction;
				private double _bloodSuckHit;
				private int _bloodSuckHeal;
				private int _regenHP;
				private int _AC;
				private int _MR;
				private int _potionHP;
				private int _dmgReduction;
				private int _maxHP;
				private int _spellDmgMulti;
				private int _spellHit;
				private double _moveDelayReduce;
				private double _attackDelayReduce;
				private int _fireElementalDmg;
				private int _waterElementalDmg;
				private int _airElementalDmg;
				private int _earthElementalDmg;
				private int _lightElementalDmg;
				private int _comboDmgMulti;
				private int _spellDmgAdd;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private EnchantBonusT(){
				}
				public int get_value(){
					return _value;
				}
				public void set_value(int val){
					_bit |= 0x1;
					_value = val;
				}
				public boolean has_value(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_meleeDmg(){
					return _meleeDmg;
				}
				public void set_meleeDmg(int val){
					_bit |= 0x2;
					_meleeDmg = val;
				}
				public boolean has_meleeDmg(){
					return (_bit & 0x2) == 0x2;
				}
				public int get_meleeHit(){
					return _meleeHit;
				}
				public void set_meleeHit(int val){
					_bit |= 0x4;
					_meleeHit = val;
				}
				public boolean has_meleeHit(){
					return (_bit & 0x4) == 0x4;
				}
				public double get_meleeCriHit(){
					return _meleeCriHit;
				}
				public void set_meleeCriHit(double val){
					_bit |= 0x8;
					_meleeCriHit = val;
				}
				public boolean has_meleeCriHit(){
					return (_bit & 0x8) == 0x8;
				}
				public int get_ignoreReduction(){
					return _ignoreReduction;
				}
				public void set_ignoreReduction(int val){
					_bit |= 0x10;
					_ignoreReduction = val;
				}
				public boolean has_ignoreReduction(){
					return (_bit & 0x10) == 0x10;
				}
				public double get_bloodSuckHit(){
					return _bloodSuckHit;
				}
				public void set_bloodSuckHit(double val){
					_bit |= 0x20;
					_bloodSuckHit = val;
				}
				public boolean has_bloodSuckHit(){
					return (_bit & 0x20) == 0x20;
				}
				public int get_bloodSuckHeal(){
					return _bloodSuckHeal;
				}
				public void set_bloodSuckHeal(int val){
					_bit |= 0x40;
					_bloodSuckHeal = val;
				}
				public boolean has_bloodSuckHeal(){
					return (_bit & 0x40) == 0x40;
				}
				public int get_regenHP(){
					return _regenHP;
				}
				public void set_regenHP(int val){
					_bit |= 0x80;
					_regenHP = val;
				}
				public boolean has_regenHP(){
					return (_bit & 0x80) == 0x80;
				}
				public int get_AC(){
					return _AC;
				}
				public void set_AC(int val){
					_bit |= 0x100;
					_AC = val;
				}
				public boolean has_AC(){
					return (_bit & 0x100) == 0x100;
				}
				public int get_MR(){
					return _MR;
				}
				public void set_MR(int val){
					_bit |= 0x200;
					_MR = val;
				}
				public boolean has_MR(){
					return (_bit & 0x200) == 0x200;
				}
				public int get_potionHP(){
					return _potionHP;
				}
				public void set_potionHP(int val){
					_bit |= 0x400;
					_potionHP = val;
				}
				public boolean has_potionHP(){
					return (_bit & 0x400) == 0x400;
				}
				public int get_dmgReduction(){
					return _dmgReduction;
				}
				public void set_dmgReduction(int val){
					_bit |= 0x800;
					_dmgReduction = val;
				}
				public boolean has_dmgReduction(){
					return (_bit & 0x800) == 0x800;
				}
				public int get_maxHP(){
					return _maxHP;
				}
				public void set_maxHP(int val){
					_bit |= 0x1000;
					_maxHP = val;
				}
				public boolean has_maxHP(){
					return (_bit & 0x1000) == 0x1000;
				}
				public int get_spellDmgMulti(){
					return _spellDmgMulti;
				}
				public void set_spellDmgMulti(int val){
					_bit |= 0x2000;
					_spellDmgMulti = val;
				}
				public boolean has_spellDmgMulti(){
					return (_bit & 0x2000) == 0x2000;
				}
				public int get_spellHit(){
					return _spellHit;
				}
				public void set_spellHit(int val){
					_bit |= 0x4000;
					_spellHit = val;
				}
				public boolean has_spellHit(){
					return (_bit & 0x4000) == 0x4000;
				}
				public double get_moveDelayReduce(){
					return _moveDelayReduce;
				}
				public void set_moveDelayReduce(double val){
					_bit |= 0x8000;
					_moveDelayReduce = val;
				}
				public boolean has_moveDelayReduce(){
					return (_bit & 0x8000) == 0x8000;
				}
				public double get_attackDelayReduce(){
					return _attackDelayReduce;
				}
				public void set_attackDelayReduce(double val){
					_bit |= 0x10000;
					_attackDelayReduce = val;
				}
				public boolean has_attackDelayReduce(){
					return (_bit & 0x10000) == 0x10000;
				}
				public int get_fireElementalDmg(){
					return _fireElementalDmg;
				}
				public void set_fireElementalDmg(int val){
					_bit |= 0x20000;
					_fireElementalDmg = val;
				}
				public boolean has_fireElementalDmg(){
					return (_bit & 0x20000) == 0x20000;
				}
				public int get_waterElementalDmg(){
					return _waterElementalDmg;
				}
				public void set_waterElementalDmg(int val){
					_bit |= 0x40000;
					_waterElementalDmg = val;
				}
				public boolean has_waterElementalDmg(){
					return (_bit & 0x40000) == 0x40000;
				}
				public int get_airElementalDmg(){
					return _airElementalDmg;
				}
				public void set_airElementalDmg(int val){
					_bit |= 0x80000;
					_airElementalDmg = val;
				}
				public boolean has_airElementalDmg(){
					return (_bit & 0x80000) == 0x80000;
				}
				public int get_earthElementalDmg(){
					return _earthElementalDmg;
				}
				public void set_earthElementalDmg(int val){
					_bit |= 0x100000;
					_earthElementalDmg = val;
				}
				public boolean has_earthElementalDmg(){
					return (_bit & 0x100000) == 0x100000;
				}
				public int get_lightElementalDmg(){
					return _lightElementalDmg;
				}
				public void set_lightElementalDmg(int val){
					_bit |= 0x200000;
					_lightElementalDmg = val;
				}
				public boolean has_lightElementalDmg(){
					return (_bit & 0x200000) == 0x200000;
				}
				public int get_comboDmgMulti(){
					return _comboDmgMulti;
				}
				public void set_comboDmgMulti(int val){
					_bit |= 0x400000;
					_comboDmgMulti = val;
				}
				public boolean has_comboDmgMulti(){
					return (_bit & 0x400000) == 0x400000;
				}
				public int get_spellDmgAdd(){
					return _spellDmgAdd;
				}
				public void set_spellDmgAdd(int val){
					_bit |= 0x800000;
					_spellDmgAdd = val;
				}
				public boolean has_spellDmgAdd(){
					return (_bit & 0x800000) == 0x800000;
				}
				@Override
				public long getInitializeBit(){
					return (long)_bit;
				}
				@Override
				public int getMemorizedSerializeSizedSize(){
					return _memorizedSerializedSize;				}
				@Override
				public int getSerializedSize(){
					int size = 0;
					if (has_value())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _value);
					if (has_meleeDmg())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _meleeDmg);
					if (has_meleeHit())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _meleeHit);
					if (has_meleeCriHit())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(4, _meleeCriHit);
					if (has_ignoreReduction())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _ignoreReduction);
					if (has_bloodSuckHit())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(6, _bloodSuckHit);
					if (has_bloodSuckHeal())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _bloodSuckHeal);
					if (has_regenHP())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _regenHP);
					if (has_AC())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _AC);
					if (has_MR())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _MR);
					if (has_potionHP())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _potionHP);
					if (has_dmgReduction())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(12, _dmgReduction);
					if (has_maxHP())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(13, _maxHP);
					if (has_spellDmgMulti())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(14, _spellDmgMulti);
					if (has_spellHit())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(15, _spellHit);
					if (has_moveDelayReduce())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(16, _moveDelayReduce);
					if (has_attackDelayReduce())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(17, _attackDelayReduce);
					if (has_fireElementalDmg())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(18, _fireElementalDmg);
					if (has_waterElementalDmg())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(19, _waterElementalDmg);
					if (has_airElementalDmg())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(20, _airElementalDmg);
					if (has_earthElementalDmg())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(21, _earthElementalDmg);
					if (has_lightElementalDmg())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(22, _lightElementalDmg);
					if (has_comboDmgMulti())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(23, _comboDmgMulti);
					if (has_spellDmgAdd())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(24, _spellDmgAdd);
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_value()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
					if (has_value()){
						output.wirteInt32(1, _value);
					}
					if (has_meleeDmg()){
						output.wirteInt32(2, _meleeDmg);
					}
					if (has_meleeHit()){
						output.wirteInt32(3, _meleeHit);
					}
					if (has_meleeCriHit()){
						output.writeDouble(4, _meleeCriHit);
					}
					if (has_ignoreReduction()){
						output.wirteInt32(5, _ignoreReduction);
					}
					if (has_bloodSuckHit()){
						output.writeDouble(6, _bloodSuckHit);
					}
					if (has_bloodSuckHeal()){
						output.wirteInt32(7, _bloodSuckHeal);
					}
					if (has_regenHP()){
						output.wirteInt32(8, _regenHP);
					}
					if (has_AC()){
						output.wirteInt32(9, _AC);
					}
					if (has_MR()){
						output.wirteInt32(10, _MR);
					}
					if (has_potionHP()){
						output.wirteInt32(11, _potionHP);
					}
					if (has_dmgReduction()){
						output.wirteInt32(12, _dmgReduction);
					}
					if (has_maxHP()){
						output.wirteInt32(13, _maxHP);
					}
					if (has_spellDmgMulti()){
						output.wirteInt32(14, _spellDmgMulti);
					}
					if (has_spellHit()){
						output.wirteInt32(15, _spellHit);
					}
					if (has_moveDelayReduce()){
						output.writeDouble(16, _moveDelayReduce);
					}
					if (has_attackDelayReduce()){
						output.writeDouble(17, _attackDelayReduce);
					}
					if (has_fireElementalDmg()){
						output.wirteInt32(18, _fireElementalDmg);
					}
					if (has_waterElementalDmg()){
						output.wirteInt32(19, _waterElementalDmg);
					}
					if (has_airElementalDmg()){
						output.wirteInt32(20, _airElementalDmg);
					}
					if (has_earthElementalDmg()){
						output.wirteInt32(21, _earthElementalDmg);
					}
					if (has_lightElementalDmg()){
						output.wirteInt32(22, _lightElementalDmg);
					}
					if (has_comboDmgMulti()){
						output.wirteInt32(23, _comboDmgMulti);
					}
					if (has_spellDmgAdd()){
						output.wirteInt32(24, _spellDmgAdd);
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
								set_value(input.readInt32());
								break;
							}
							case 0x00000010:{
								set_meleeDmg(input.readInt32());
								break;
							}
							case 0x00000018:{
								set_meleeHit(input.readInt32());
								break;
							}
							case 0x00000021:{
								set_meleeCriHit(input.readDouble());
								break;
							}
							case 0x00000028:{
								set_ignoreReduction(input.readInt32());
								break;
							}
							case 0x00000031:{
								set_bloodSuckHit(input.readDouble());
								break;
							}
							case 0x00000038:{
								set_bloodSuckHeal(input.readInt32());
								break;
							}
							case 0x00000040:{
								set_regenHP(input.readInt32());
								break;
							}
							case 0x00000048:{
								set_AC(input.readInt32());
								break;
							}
							case 0x00000050:{
								set_MR(input.readInt32());
								break;
							}
							case 0x00000058:{
								set_potionHP(input.readInt32());
								break;
							}
							case 0x00000060:{
								set_dmgReduction(input.readInt32());
								break;
							}
							case 0x00000068:{
								set_maxHP(input.readInt32());
								break;
							}
							case 0x00000070:{
								set_spellDmgMulti(input.readInt32());
								break;
							}
							case 0x00000078:{
								set_spellHit(input.readInt32());
								break;
							}
							case 0x00000081:{
								set_moveDelayReduce(input.readDouble());
								break;
							}
							case 0x00000089:{
								set_attackDelayReduce(input.readDouble());
								break;
							}
							case 0x00000090:{
								set_fireElementalDmg(input.readInt32());
								break;
							}
							case 0x00000098:{
								set_waterElementalDmg(input.readInt32());
								break;
							}
							case 0x000000A0:{
								set_airElementalDmg(input.readInt32());
								break;
							}
							case 0x000000A8:{
								set_earthElementalDmg(input.readInt32());
								break;
							}
							case 0x000000B0:{
								set_lightElementalDmg(input.readInt32());
								break;
							}
							case 0x000000B8:{
								set_comboDmgMulti(input.readInt32());
								break;
							}
							case 0x000000C0:{
								set_spellDmgAdd(input.readInt32());
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
					return new EnchantBonusT();
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
		}
	}
	public static class ClassInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static ClassInfoT newInstance(){
			return new ClassInfoT();
		}
		private java.util.LinkedList<ClassT> _Class;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ClassInfoT(){
		}
		public java.util.LinkedList<ClassT> get_Class(){
			return _Class;
		}
		public void add_Class(ClassT val){
			if(!has_Class()){
				_Class = new java.util.LinkedList<ClassT>();
				_bit |= 0x1;
			}
			_Class.add(val);
		}
		public boolean has_Class(){
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
			if (has_Class()){
				for(ClassT val : _Class)
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_Class()){
				for(ClassT val : _Class){
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
			if (has_Class()){
				for(ClassT val : _Class){
					output.writeMessage(1, val);
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
					case 0x0000000A:{
						add_Class((ClassT)input.readMessage(ClassT.newInstance()));
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
			return new ClassInfoT();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_Class()){
				for(ClassT val : _Class)
					val.dispose();
				_Class.clear();
				_Class = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public static class ClassT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static ClassT newInstance(){
				return new ClassT();
			}
			private String _class;
			private int _classId;
			private eCategory _category;
			private eElement _element;
			private java.util.LinkedList<SkillT> _Skill;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private ClassT(){
			}
			public String get_class(){
				return _class;
			}
			public void set_class(String val){
				_bit |= 0x1;
				_class = val;
			}
			public boolean has_class(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_classId(){
				return _classId;
			}
			public void set_classId(int val){
				_bit |= 0x2;
				_classId = val;
			}
			public boolean has_classId(){
				return (_bit & 0x2) == 0x2;
			}
			public eCategory get_category(){
				return _category;
			}
			public void set_category(eCategory val){
				_bit |= 0x4;
				_category = val;
			}
			public boolean has_category(){
				return (_bit & 0x4) == 0x4;
			}
			public eElement get_element(){
				return _element;
			}
			public void set_element(eElement val){
				_bit |= 0x10;
				_element = val;
			}
			public boolean has_element(){
				return (_bit & 0x10) == 0x10;
			}
			public java.util.LinkedList<SkillT> get_Skill(){
				return _Skill;
			}
			public void add_Skill(SkillT val){
				if(!has_Skill()){
					_Skill = new java.util.LinkedList<SkillT>();
					_bit |= 0x20;
				}
				_Skill.add(val);
			}
			public boolean has_Skill(){
				return (_bit & 0x20) == 0x20;
			}
			@Override
			public long getInitializeBit(){
				return (long)_bit;
			}
			@Override
			public int getMemorizedSerializeSizedSize(){
				return _memorizedSerializedSize;			}
			@Override
			public int getSerializedSize(){
				int size = 0;
				if (has_class())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _class);
				if (has_classId())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _classId);
				if (has_category())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _category.toInt());
				if (has_element())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _element.toInt());
				if (has_Skill()){
					for(SkillT val : _Skill)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_class()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_classId()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_category()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_element()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_Skill()){
					for(SkillT val : _Skill){
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
				if (has_class()){
					output.writeString(1, _class);
				}
				if (has_classId()){
					output.writeUInt32(2, _classId);
				}
				if (has_category()){
					output.writeEnum(3, _category.toInt());
				}
				if (has_element()){
					output.writeEnum(5, _element.toInt());
				}
				if (has_Skill()){
					for(SkillT val : _Skill){
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
						case 0x0000000A:{
							set_class(input.readString());
							break;
						}
						case 0x00000010:{
							set_classId(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_category(eCategory.fromInt(input.readEnum()));
							break;
						}
						case 0x00000028:{
							set_element(eElement.fromInt(input.readEnum()));
							break;
						}
						case 0x00000032:{
							add_Skill((SkillT)input.readMessage(SkillT.newInstance()));
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
				return new ClassT();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_Skill()){
					for(SkillT val : _Skill)
						val.dispose();
					_Skill.clear();
					_Skill = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
			public static class SkillT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
				public static SkillT newInstance(){
					return new SkillT();
				}
				private int _tier;
				private java.util.LinkedList<Integer> _skillId;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private SkillT(){
				}
				public int get_tier(){
					return _tier;
				}
				public void set_tier(int val){
					_bit |= 0x1;
					_tier = val;
				}
				public boolean has_tier(){
					return (_bit & 0x1) == 0x1;
				}
				public java.util.LinkedList<Integer> get_skillId(){
					return _skillId;
				}
				public void add_skillId(int val){
					if(!has_skillId()){
						_skillId = new java.util.LinkedList<Integer>();
						_bit |= 0x2;
					}
					_skillId.add(val);
				}
				public boolean has_skillId(){
					return (_bit & 0x2) == 0x2;
				}
				@Override
				public long getInitializeBit(){
					return (long)_bit;
				}
				@Override
				public int getMemorizedSerializeSizedSize(){
					return _memorizedSerializedSize;				}
				@Override
				public int getSerializedSize(){
					int size = 0;
					if (has_tier())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _tier);
					if (has_skillId()){
						for(int val : _skillId)
							size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, val);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_tier()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_skillId()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
					if (has_tier()){
						output.writeUInt32(1, _tier);
					}
					if (has_skillId()){
						for(int val : _skillId){
							output.writeUInt32(2, val);
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
								set_tier(input.readUInt32());
								break;
							}
							case 0x00000010:{
								add_skillId(input.readUInt32());
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
					return new SkillT();
				}
				@Override
				public MJIProtoMessage reloadInstance(){
					return newInstance();
				}
				@Override
				public void dispose(){
					if (has_skillId()){
						_skillId.clear();
						_skillId = null;
					}
					_bit = 0;
					_memorizedIsInitialized = -1;
				}
			}
		}
	}
	public enum eStatType{
		STR(0),
		CON(1),
		INT_(2);
		private int value;
		eStatType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eStatType v){
			return value == v.value;
		}
		public static eStatType fromInt(int i){
			switch(i){
			case 0:
				return STR;
			case 1:
				return CON;
			case 2:
				return INT_;
			default:
				return null;
			}
		}
	}
	public enum eCommand{
		TM_Aggressive(2),
		TM_Defensive(3),
		TM_GetItem(6),
		TM_Attack(7),
		TM_PullBack(9),
		Dismiss(100),
		Joke(101),
		Happy(102);
		private int value;
		eCommand(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCommand v){
			return value == v.value;
		}
		public static eCommand fromInt(int i){
			switch(i){
			case 2:
				return TM_Aggressive;
			case 3:
				return TM_Defensive;
			case 6:
				return TM_GetItem;
			case 7:
				return TM_Attack;
			case 9:
				return TM_PullBack;
			case 100:
				return Dismiss;
			case 101:
				return Joke;
			case 102:
				return Happy;
			default:
				return null;
			}
		}
	}
	public enum eCategory{
		FIERCE_ANIMAL(1),
		DEVINE_BEAST(2),
		PET(3),
		WILD(4),
		DOG_FIGHT(5);
		private int value;
		eCategory(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCategory v){
			return value == v.value;
		}
		public static eCategory fromInt(int i){
			switch(i){
			case 1:
				return FIERCE_ANIMAL;
			case 2:
				return DEVINE_BEAST;
			case 3:
				return PET;
			case 4:
				return WILD;
			case 5:
				return DOG_FIGHT;
			default:
				return null;
			}
		}
	}
	public enum eElement{
		NONE(0),
		FIRE(1),
		WATER(2),
		AIR(3),
		EARTH(4),
		LIGHT(5);
		private int value;
		eElement(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eElement v){
			return value == v.value;
		}
		public static eElement fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return FIRE;
			case 2:
				return WATER;
			case 3:
				return AIR;
			case 4:
				return EARTH;
			case 5:
				return LIGHT;
			default:
				return null;
			}
		}
	}
}
