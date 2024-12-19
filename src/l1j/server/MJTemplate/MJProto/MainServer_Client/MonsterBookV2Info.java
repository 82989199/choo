package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJBookQuestSystem.BQSWQInformation;
import l1j.server.MJBookQuestSystem.Loader.BQSInformationLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSWQInformationLoader;
import l1j.server.MJBookQuestSystem.Reward.BQSExpReward;
import l1j.server.MJBookQuestSystem.Reward.BQSItemReward;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2BonusRewardGrade;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2DeckState;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2RewardGrade;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class MonsterBookV2Info implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static MonsterBookV2Info newInstance(){
		return new MonsterBookV2Info();
	}
	private SystemT _system;
	private java.util.ArrayList<DeckT> _decks;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private MonsterBookV2Info(){
	}
	public SystemT get_system(){
		return _system;
	}
	public void set_system(SystemT val){
		_bit |= 0x1;
		_system = val;
	}
	public boolean has_system(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.ArrayList<DeckT> get_decks(){
		return _decks;
	}
	public void set_decks(ArrayList<DeckT> val){
		_bit |= 0x02;
		_decks = val;
	}
	public void add_decks(DeckT val){
		if(!has_decks()){
			_decks = new java.util.ArrayList<DeckT>(8);
			_bit |= 0x2;
		}
		_decks.add(val);
	}
	public boolean has_decks(){
		return (_bit & 0x2) == 0x2;
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
		if (has_system())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _system);
		if (has_decks()){
			for(DeckT val : _decks)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_system()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_decks()){
			for(DeckT val : _decks){
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
		if (has_system()){
			output.writeMessage(1, _system);
		}
		if (has_decks()){
			for(DeckT val : _decks){
				output.writeMessage(2, val);
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
					set_system((SystemT)input.readMessage(SystemT.newInstance()));
					break;
				}
				case 0x00000012:{
					add_decks((DeckT)input.readMessage(DeckT.newInstance()));
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
		return new MonsterBookV2Info();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_system = null;
		_decks = null;
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class SystemT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static SystemT newInstance(){
			return new SystemT();
		}
		private java.util.HashMap<Integer, RewardT> _rewards;
		private java.util.ArrayList<BonusRewardT> _bonus_rewards;
		private int _min_level;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SystemT(){
		}
		
		public l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.eResultCode doReward(L1PcInstance pc, eMonsterBookV2RewardGrade grade){
			RewardT reward = get_rewards(grade.toInt());
			return reward == null ? 
					l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.eResultCode.REWARD_FAIL :
					reward.doReward(pc);
		}
		
		public java.util.HashMap<Integer, RewardT> get_rewards(){
			return _rewards;
		}
		public RewardT get_rewards(eMonsterBookV2RewardGrade grade){
			return get_rewards(grade.toInt());
		}
		public RewardT get_rewards(int grade){
			return _rewards.get(grade);
		}
		public void add_rewards(RewardT val){
			if(!has_rewards()){
				_rewards = new java.util.HashMap<Integer, RewardT>(4);
				_bit |= 0x2;
			}
			_rewards.put(val.get_grade().toInt(), val);
		}
		public boolean has_rewards(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.ArrayList<BonusRewardT> get_bonus_rewards(){
			return _bonus_rewards;
		}
		public void add_bonus_rewards(BonusRewardT val){
			if(!has_bonus_rewards()){
				_bonus_rewards = new java.util.ArrayList<BonusRewardT>(3);
				_bit |= 0x4;
			}
			_bonus_rewards.add(val);
		}
		public boolean has_bonus_rewards(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_min_level(){
			return _min_level;
		}
		public void set_min_level(int val){
			_bit |= 0x8;
			_min_level = val;
		}
		public boolean has_min_level(){
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
			if (has_rewards()){
				for(RewardT val : _rewards.values())
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
			if (has_bonus_rewards()){
				for(BonusRewardT val : _bonus_rewards)
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
			if (has_min_level())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _min_level);
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_rewards()){
				for(RewardT val : _rewards.values()){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_bonus_rewards()){
				for(BonusRewardT val : _bonus_rewards){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (!has_min_level()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_rewards()){
				for(RewardT val : _rewards.values()){
					output.writeMessage(2, val);
				}
			}
			if (has_bonus_rewards()){
				for(BonusRewardT val : _bonus_rewards){
					output.writeMessage(3, val);
				}
			}
			if (has_min_level()){
				output.writeUInt32(4, _min_level);
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
					case 0x00000012:{
						add_rewards((RewardT)input.readMessage(RewardT.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_bonus_rewards((BonusRewardT)input.readMessage(BonusRewardT.newInstance()));
						break;
					}
					case 0x00000020:{
						set_min_level(input.readUInt32());
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
			return new SystemT();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_rewards()){
				for(RewardT val : _rewards.values())
					val.dispose();
				_rewards.clear();
				_rewards = null;
			}
			if (has_bonus_rewards()){
				for(BonusRewardT val : _bonus_rewards)
					val.dispose();
				_bonus_rewards.clear();
				_bonus_rewards = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public static class RewardT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static RewardT newInstance(ResultSet rs) throws SQLException{
				RewardT reward = RewardT.newInstance();
				reward.set_grade(eMonsterBookV2RewardGrade.fromInt(rs.getInt("reward_grade")));
				reward.set_reward_exp(rs.getInt("reward_exp_percent") * BQSLoadManager.BQS_WQ_STD_EXP);
				reward.set_item_id(rs.getInt("reward_need_item_id"));
				
				int reward_exp = reward.get_reward_exp();
				if(reward_exp > 0)
					reward.set_bqsexp_reward(BQSExpReward.newInstance(reward.get_grade().toInt(), reward_exp));
				return reward;
			}
			
			public static RewardT newInstance(){
				return new RewardT();
			}
			private eMonsterBookV2RewardGrade _grade;
			private BQSExpReward _bqsexp_reward;
			private int _reward_exp;
			private int _item_id;
			private int _item_name_id;
			private java.util.ArrayList<ItemRewardT> _item_rewards;
			private int _high_level;
			private int _high_reward_exp;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private RewardT(){
			}
			
			public l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.eResultCode doReward(L1PcInstance pc){
				if(_item_id > 0 && !pc.getInventory().consumeItem(_item_id, 1)){
					return l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.eResultCode
							.REWARD_FAIL_NOT_ENOUGH_ITEM;
				}
				if(_bqsexp_reward != null)
					_bqsexp_reward.doReward(pc);
				
				if(has_item_rewards()){
					for(ItemRewardT reward : _item_rewards)
						reward.doReward(pc);
				}
				return l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_REWARD_ACK.eResultCode
						.REWARD_SUCCESS;
			}
			
			public void set_bqsexp_reward(BQSExpReward reward){
				_bqsexp_reward = reward;
			}
			
			public void set_item_id(int val){
				_item_id = val;
				if(_item_id != 0){
					L1Item item = ItemTable.getInstance().getTemplate(_item_id);
					if(item == null){
						System.out.println(String.format("[도감]아이템 테이블에서 아이템을 찾을 수 없습니다. 아이템아이디 : %d", _item_id));
						set_item_name_id(0);
					}else{
						set_item_name_id(item.getItemDescId());
					}
				}else{
					set_item_name_id(0);
				}
			}
			
			public int get_item_id(){
				return _item_id;
			}
			
			public eMonsterBookV2RewardGrade get_grade(){
				return _grade;
			}
			public void set_grade(eMonsterBookV2RewardGrade val){
				_bit |= 0x1;
				_grade = val;
			}
			public boolean has_grade(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_reward_exp(){
				return _reward_exp;
			}
			public void set_reward_exp(int val){
				_bit |= 0x2;
				_reward_exp = val;
			}
			public boolean has_reward_exp(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_item_name_id(){
				return _item_name_id;
			}
			public void set_item_name_id(int val){
				_bit |= 0x4;
				_item_name_id = val;
			}
			public boolean has_item_name_id(){
				return (_bit & 0x4) == 0x4;
			}
			public java.util.ArrayList<ItemRewardT> get_item_rewards(){
				return _item_rewards;
			}
			public void add_item_rewards(ItemRewardT val){
				if(!has_item_rewards()){
					_item_rewards = new java.util.ArrayList<ItemRewardT>(8);
					_bit |= 0x8;
				}
				_item_rewards.add(val);
			}
			public boolean has_item_rewards(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_high_level(){
				return _high_level;
			}
			public void set_high_level(int val){
				_bit |= 0x10;
				_high_level = val;
			}
			public boolean has_high_level(){
				return (_bit & 0x10) == 0x10;
			}
			public int get_high_reward_exp(){
				return _high_reward_exp;
			}
			public void set_high_reward_exp(int val){
				_bit |= 0x20;
				_high_reward_exp = val;
			}
			public boolean has_high_reward_exp(){
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
				if (has_grade())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _grade.toInt());
				if (has_reward_exp())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _reward_exp);
				if (has_item_name_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _item_name_id);
				if (has_item_rewards()){
					for(ItemRewardT val : _item_rewards)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
				}
				if (has_high_level())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _high_level);
				if (has_high_reward_exp())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _high_reward_exp);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_grade()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_reward_exp()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_item_rewards()){
					for(ItemRewardT val : _item_rewards){
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
				if (has_grade()){
					output.writeEnum(1, _grade.toInt());
				}
				if (has_reward_exp()){
					output.wirteInt32(2, _reward_exp);
				}
				if (has_item_name_id()){
					output.wirteInt32(3, _item_name_id);
				}
				if (has_item_rewards()){
					for(ItemRewardT val : _item_rewards){
						output.writeMessage(4, val);
					}
				}
				if (has_high_level()){
					output.writeUInt32(5, _high_level);
				}
				if (has_high_reward_exp()){
					output.writeUInt32(6, _high_reward_exp);
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
							set_grade(eMonsterBookV2RewardGrade.fromInt(input.readEnum()));
							break;
						}
						case 0x00000010:{
							set_reward_exp(input.readInt32());
							break;
						}
						case 0x00000018:{
							set_item_name_id(input.readInt32());
							break;
						}
						case 0x00000022:{
							add_item_rewards((ItemRewardT)input.readMessage(ItemRewardT.newInstance()));
							break;
						}
						case 0x00000028:{
							set_high_level(input.readUInt32());
							break;
						}
						case 0x00000030:{
							set_high_reward_exp(input.readUInt32());
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
				return new RewardT();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_item_rewards()){
					for(ItemRewardT val : _item_rewards)
						val.dispose();
					_item_rewards.clear();
					_item_rewards = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
			public static class ItemRewardT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
				public static ItemRewardT newInstance(ResultSet rs) throws SQLException{
					ItemRewardT rewardT = newInstance();
					rewardT.set_item_id(rs.getInt("reward_item_id"));
					rewardT.set_amount(rs.getInt("reward_amount"));
					
					int itemId = rewardT.get_item_id();
					if(itemId > 0)
						rewardT.set_bqsitem_reward(BQSItemReward.newInstance(1, itemId, rewardT.get_amount()));
					return rewardT;
				}
				public static ItemRewardT newInstance(){
					return new ItemRewardT();
				}
				private BQSItemReward _bqsitem_reward;
				private int _item_id;
				private int _item_name_id;
				private int _amount;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private ItemRewardT(){
				}
				
				public void doReward(L1PcInstance pc){
					if(_bqsitem_reward != null)
						_bqsitem_reward.doReward(pc);
				}
				
				public void set_bqsitem_reward(BQSItemReward reward){
					_bqsitem_reward = reward;
				}
				
				public int get_item_id(){
					return _item_id;
				}
				public void set_item_id(int i){
					_item_id = i;
					L1Item item = ItemTable.getInstance().getTemplate(i);
					if(item == null){
						System.out.println(String.format("[도감]아이템 테이블에서 아이템을 찾을 수 없습니다. 아이템아이디 : %d", i));
						set_item_name_id(0);
						return;
					}
					set_item_name_id(item.getItemDescId());
				}
				public int get_item_name_id(){
					return _item_name_id;
				}
				public void set_item_name_id(int val){
					_bit |= 0x1;
					_item_name_id = val;
				}
				public boolean has_item_name_id(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_amount(){
					return _amount;
				}
				public void set_amount(int val){
					_bit |= 0x2;
					_amount = val;
				}
				public boolean has_amount(){
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
					if (has_item_name_id())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _item_name_id);
					if (has_amount())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _amount);
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_item_name_id()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_amount()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
					if (has_item_name_id()){
						output.wirteInt32(1, _item_name_id);
					}
					if (has_amount()){
						output.writeUInt32(2, _amount);
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
								set_item_name_id(input.readInt32());
								break;
							}
							case 0x00000010:{
								set_amount(input.readUInt32());
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
					return new ItemRewardT();
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
		public static class BonusRewardT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static BonusRewardT newInstance(){
				return new BonusRewardT();
			}
			private eMonsterBookV2BonusRewardGrade _grade;
			private int _reward_exp;
			private int _item_name_id;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private BonusRewardT(){
			}
			public eMonsterBookV2BonusRewardGrade get_grade(){
				return _grade;
			}
			public void set_grade(eMonsterBookV2BonusRewardGrade val){
				_bit |= 0x1;
				_grade = val;
			}
			public boolean has_grade(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_reward_exp(){
				return _reward_exp;
			}
			public void set_reward_exp(int val){
				_bit |= 0x2;
				_reward_exp = val;
			}
			public boolean has_reward_exp(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_item_name_id(){
				return _item_name_id;
			}
			public void set_item_name_id(int val){
				_bit |= 0x4;
				_item_name_id = val;
			}
			public boolean has_item_name_id(){
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
				if (has_grade())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _grade.toInt());
				if (has_reward_exp())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _reward_exp);
				if (has_item_name_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _item_name_id);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_grade()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_reward_exp()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_item_name_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_grade()){
					output.writeEnum(1, _grade.toInt());
				}
				if (has_reward_exp()){
					output.wirteInt32(2, _reward_exp);
				}
				if (has_item_name_id()){
					output.wirteInt32(3, _item_name_id);
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
							set_grade(eMonsterBookV2BonusRewardGrade.fromInt(input.readEnum()));
							break;
						}
						case 0x00000010:{
							set_reward_exp(input.readInt32());
							break;
						}
						case 0x00000018:{
							set_item_name_id(input.readInt32());
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
				return new BonusRewardT();
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
	public static class DeckT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static DeckT newInstance(int i) {
			DeckT current_deck = newInstance();
			current_deck.set_index(i);
			current_deck.set_state(eMonsterBookV2DeckState.DS_IN_PROGRESS);
			ArrayList<BQSInformation> weeks = BQSInformationLoader.getInstance().createShuffleWeeks(i);
			int size = weeks.size();
			if(size < BQSLoadManager.BQS_WQ_WIDTH){
				System.out.println(String.format("[도감] 주간퀘스트 난이도 %d의 할당 갯수가 모자랍니다. %d개", i, size));	
			}
			for(int index = 0; index < BQSLoadManager.BQS_WQ_WIDTH; ++index){
				if(index >= size){
					current_deck.add_cards(CardT.nullInstance(index));
				}else{
					BQSInformation bqs = weeks.get(index);
					current_deck.add_cards(CardT.newInstance(bqs, index));
				}
			}
			return current_deck;
		}
		
		public static DeckT newInstance(ResultSet rs) throws SQLException{
			DeckT current_deck = newInstance();
			int difficulty = rs.getInt("difficulty");
			current_deck.set_index(difficulty);
			current_deck.set_state(eMonsterBookV2DeckState.DS_IN_PROGRESS);
			BQSInformationLoader loader = BQSInformationLoader.getInstance();
			
			for(int index = 0; index < BQSLoadManager.BQS_WQ_WIDTH; ++index){
				BQSInformation bqs = loader.getInformation(rs.getInt(String.format("col%d", index + 1)));
				current_deck.add_cards(MonsterBookV2Info.DeckT.CardT.newInstance(bqs, index));						
			}
			return current_deck;
		}
		
		public static DeckT newInstance(){
			return new DeckT();
		}
		private int _index;
		private eMonsterBookV2DeckState _state;
		private java.util.ArrayList<CardT> _cards;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private DeckT(){
		}
		
		public CardT findCard(int criteria_id){
			for(CardT card : _cards){
				if(card.get_criteria_id() == criteria_id)
					return card;
			}
			return null;
		}
		
		public boolean onUpdateIsComplete(L1PcInstance pc, int criteria_id){
			if(!_state.equals(eMonsterBookV2DeckState.DS_IN_PROGRESS))
				return false;
			
			int completed = 0;
			for(CardT card : _cards){
				if(card.get_criteria_id() == criteria_id){
					if(card.isCompleted())
						return false;
					
					boolean isComplete = card.onUpdateIsComplete();
					SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI noti = SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI.newInstance();
					noti.set_deck_index(_index);
					noti.set_card_index(card.get_index());
					noti.set_amount(card.get_amount());
					pc.sendPackets(noti, MJEProtoMessages.SC_MONSTER_BOOK_V2_UPDATE_AMOUNT_NOTI);
					if(isComplete)
						++completed;
				}else{
					if(card.isCompleted())
						++completed;
				}
			}
			return completed == _cards.size();
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
		public eMonsterBookV2DeckState get_state(){
			return _state;
		}
		public void set_state(eMonsterBookV2DeckState val){
			_bit |= 0x4;
			_state = val;
		}
		public boolean has_state(){
			return (_bit & 0x4) == 0x4;
		}
		public CardT get_card(int card_index){
			return _cards.get(card_index);
		}
		public java.util.ArrayList<CardT> get_cards(){
			return _cards;
		}
		public void add_cards(CardT val){
			if(!has_cards()){
				_cards = new java.util.ArrayList<CardT>(BQSLoadManager.BQS_WQ_WIDTH);
				_bit |= 0x8;
			}
			_cards.add(val);
		}
		public boolean has_cards(){
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
			if (has_index())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _index);
			if (has_state())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _state.toInt());
			if (has_cards()){
				for(CardT val : _cards)
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
			}
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
			if (!has_state()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_cards()){
				for(CardT val : _cards){
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
			if (has_index()){
				output.writeUInt32(1, _index);
			}
			if (has_state()){
				output.writeEnum(3, _state.toInt());
			}
			if (has_cards()){
				for(CardT val : _cards){
					output.writeMessage(4, val);
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
						set_index(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_state(eMonsterBookV2DeckState.fromInt(input.readEnum()));
						break;
					}
					case 0x00000022:{
						add_cards((CardT)input.readMessage(CardT.newInstance()));
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
			return new DeckT();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_cards()){
				for(CardT val : _cards)
					val.dispose();
				_cards.clear();
				_cards = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public DeckT deepCopy(int level){
			DeckT deck = newInstance();
			deck.set_index(_index);
			deck.set_state(_state);
			for(CardT val : _cards)
				deck.add_cards(val.deepCopy(level));
			return deck;
		}
		
		public static class CardT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static CardT newInstance(BQSInformation bqs, int index){
				CardT card = CardT.newInstance();
				card.set_achievement_id(bqs.getAchievementId());
				card.set_amount(0);
				card.set_criteria_id(bqs.getCriteriaId());
				card.set_index(index);
				card.set_required_amount(bqs.getWeekCompletedCount());
				return card;
			}
			
			public static CardT nullInstance(int index){
				CardT card = CardT.newInstance();
				card.set_achievement_id(0);
				card.set_amount(0);
				card.set_criteria_id(1);
				card.set_index(index);
				card.set_required_amount(10000000);
				return card;
			}
			
			public static CardT newInstance(){
				return new CardT();
			}
			private int _index;
			private int _required_amount;
			private int _achievement_id;
			private int _criteria_id;
			private int _amount;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CardT(){
			}
			
			public boolean isCompleted(){
				return _amount >= _required_amount;
			}
			
			public boolean onUpdateIsComplete(){
				return ++_amount >= _required_amount;
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
			public int get_required_amount(){
				return _required_amount;
			}
			public void set_required_amount(int val){
				_bit |= 0x2;
				_required_amount = val;
			}
			public boolean has_required_amount(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_achievement_id(){
				return _achievement_id;
			}
			public void set_achievement_id(int val){
				_bit |= 0x4;
				_achievement_id = val;
			}
			public boolean has_achievement_id(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_criteria_id(){
				return _criteria_id;
			}
			public void set_criteria_id(int val){
				_bit |= 0x8;
				_criteria_id = val;
			}
			public boolean has_criteria_id(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_amount(){
				return _amount;
			}
			public void set_amount(int val){
				_bit |= 0x10;
				_amount = val;
			}
			public boolean has_amount(){
				return (_bit & 0x10) == 0x10;
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
				if (has_index())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _index);
				if (has_required_amount())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _required_amount);
				if (has_achievement_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _achievement_id);
				if (has_criteria_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _criteria_id);
				if (has_amount())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _amount);
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
				if (!has_required_amount()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_achievement_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_criteria_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_amount()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_index()){
					output.writeUInt32(1, _index);
				}
				if (has_required_amount()){
					output.writeUInt32(2, _required_amount);
				}
				if (has_achievement_id()){
					output.writeUInt32(3, _achievement_id);
				}
				if (has_criteria_id()){
					output.writeUInt32(4, _criteria_id);
				}
				if (has_amount()){
					output.writeUInt32(5, _amount);
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
							set_index(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_required_amount(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_achievement_id(input.readUInt32());
							break;
						}
						case 0x00000020:{
							set_criteria_id(input.readUInt32());
							break;
						}
						case 0x00000028:{
							set_amount(input.readUInt32());
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
				return new CardT();
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
			public CardT deepCopy(int level){
				CardT card = CardT.newInstance();
				card.set_achievement_id(_achievement_id);
				BQSWQInformation wqInfo = BQSWQInformationLoader.getInstance().get(level);
				card.set_required_amount(wqInfo == null ? _required_amount : wqInfo.calc_amount(_required_amount));
				card.set_amount(_amount);
				card.set_criteria_id(_criteria_id);
				card.set_index(_index);
				return card;
			}
		}
	}
}
