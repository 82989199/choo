package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_ALCHEMY_MAKE_REQ.Input;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.AlchemyProbability;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.MJHexHelper;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ALCHEMY_DESIGN_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	private static ArrayList<ProtoOutputStream> _alchemyCaches;
	private static ProtoOutputStream _alchemySameCache;
	
	private static ArrayList<ProtoOutputStream> createAlchemyInfoCache(){
		SC_ALCHEMY_DESIGN_ACK src = (SC_ALCHEMY_DESIGN_ACK) MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK.getMessageInstance();
		ArrayList<ProtoOutputStream> caches = new ArrayList<ProtoOutputStream>(4);

		SC_ALCHEMY_DESIGN_ACK ack = new SC_ALCHEMY_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_ALCHEMY_LOAD_START);
		ack.set_inputLists(src.get_inputLists());
		caches.add(doMakeCreateProto(ack));
		
		ack = new SC_ALCHEMY_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_ALCHEMY_LOADING);
		ack.set_outputLists(src.get_outputLists());
		caches.add(doMakeCreateProto(ack));
		
		ack = new SC_ALCHEMY_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_ALCHEMY_LOADING);
		ack.set_alchemies(src.get_alchemies());
		caches.add(doMakeCreateProto(ack));
		
		ack = new SC_ALCHEMY_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_ALCHEMY_LOAD_FINISH);
		caches.add(doMakeCreateProto(ack));
		ack.dispose();
		
		return caches;
	}
	
	private static ProtoOutputStream doMakeCreateProto(SC_ALCHEMY_DESIGN_ACK ack){
		ProtoOutputStream stream = ack.writeTo(MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK);
		stream.createProtoBytes();
		return stream;
	}
	
	public static void reloadedAlchemyCaches(){
		ArrayList<ProtoOutputStream> tmp = _alchemyCaches;
		_alchemyCaches = createAlchemyInfoCache();
		if(tmp != null){
			for(ProtoOutputStream s : tmp)
				s.dispose();
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void reloadedAlchemySameCaches(){
		ProtoOutputStream tmp = _alchemySameCache;
		SC_ALCHEMY_DESIGN_ACK ack = new SC_ALCHEMY_DESIGN_ACK();
		ack.set_result_code(ResultCode.RC_ALCHEMY_SAME_HASH_VAL);
		_alchemySameCache = ack.writeTo(MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK);
		ack.dispose();
		
		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	public static SC_ALCHEMY_DESIGN_ACK newInstance(){
		final SC_ALCHEMY_DESIGN_ACK message = new SC_ALCHEMY_DESIGN_ACK();
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				try {
					message.readFrom(ProtoInputStream.newInstance("./data/alchemyInfo.dat"));
					if(!message.isInitialized())
						throw new IllegalArgumentException(String.format("fail initialized craftinfo data.(SC_ALCHEMY_DESIGN_ACK) %d", message.getInitializeBit()));
					
					reloadedAlchemyCaches();
					reloadedAlchemySameCaches();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 2000L);
		
		return message;
	}
	
	public static void send(L1PcInstance pc, byte[] hash_val){
		try{
			
			if(MJHexHelper.compareArrays(Config.ALCHEMY_VERSION_HASH, hash_val, Config.ALCHEMY_VERSION_HASH.length)){
				pc.sendPackets(_alchemySameCache, false);
			}else if(Config.CRAFT_TRANSMIT_SAFELINE <= L1World.getInstance().getAllPlayersSize()){
				pc.sendPackets("현재 클라이언트의 인형 정보가 이전 버전임으로 모든 목록이 나오지 않을 수 있습니다.");
				pc.sendPackets("접속기에서 오류패치를 하신 후 다시 접속하면 모든 목록을 받아볼 수 있습니다.");
				pc.sendPackets(_alchemySameCache, false);
			}else{
//				pc.sendPackets("인형정보가 갱신됩니다. 1초후 창이 뜨니 잠시 기다려 주십시오.");
				GeneralThreadPool.getInstance().schedule(new Runnable(){
					@Override
					public void run(){
						pc.getNetConnection().directSendPacket(_alchemyCaches);
					}
				}, 100L);
			}
		}catch(Exception e){
			SC_ALCHEMY_DESIGN_ACK ack = new SC_ALCHEMY_DESIGN_ACK();
			ack.set_result_code(ResultCode.RC_ALCHEMY_CURRENTLY_CLOSED);
			pc.sendPackets(ack, MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK, true);
			e.printStackTrace();
		}
	}
	
	public static Alchemy getAlchemy(int alchemyId){
		SC_ALCHEMY_DESIGN_ACK ack = (SC_ALCHEMY_DESIGN_ACK)MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK.getMessageInstance();
		LinkedList<Alchemy> alchemies = ack.get_alchemies();
		if(alchemyId < 0 || alchemyId >= alchemies.size())
			return null;
		
		return alchemies.get(alchemyId);
	}
	
	public static OutputList getOutputList(int alchemyId){
		SC_ALCHEMY_DESIGN_ACK ack = (SC_ALCHEMY_DESIGN_ACK)MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK.getMessageInstance();
		LinkedList<OutputList> outputs = ack.get_outputLists();
		if(alchemyId < 0 || alchemyId >= outputs.size())
			return null;
		
		return outputs.get(alchemyId);
	}
	
	public static Item selectOutputItem(int alchemyId){
		OutputList outputs = getOutputList(alchemyId);
		HashMap<Integer, Item> output = outputs.get_item();
		int select = MJRnd.next(output.size());
		int idx = 0;
		Item result = null;
		
		for(Item item : output.values()){
			result = item;
			if(idx++ == select)
				break;
		}
		return result;
	}
	
	public static L1Item selectOutputL1Item(int alchemyId){
		return ItemTable.getInstance().getTemplate(AlchemyProbability.getInstance().select_id(alchemyId));
	}
	
	public static InputList getInputList(int alchemyId){
		SC_ALCHEMY_DESIGN_ACK ack = (SC_ALCHEMY_DESIGN_ACK)MJEProtoMessages.SC_ALCHEMY_DESIGN_ACK.getMessageInstance();
		LinkedList<InputList> inputs = ack.get_inputLists();
		if(alchemyId < 0 || alchemyId >= inputs.size()) {
			return null;
		}
		
		return inputs.get(alchemyId);
	}
	
	private ResultCode _result_code;
	private LinkedList<InputList> _inputLists;
	private LinkedList<OutputList> _outputLists;
	private LinkedList<Alchemy> _alchemies;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ALCHEMY_DESIGN_ACK(){
	}
	public ResultCode get_result_code(){
		return _result_code;
	}
	public void set_result_code(ResultCode val){
		_bit |= 0x00000001;
		_result_code = val;
	}
	public boolean has_result_code(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public LinkedList<InputList> get_inputLists(){
		return _inputLists;
	}
	public void set_inputLists(LinkedList<InputList> val){
		_bit |= 0x00000002;
		_inputLists = val;
	}
	public void add_inputLists(InputList val){
		if(!has_inputLists()){
			_inputLists = new LinkedList<InputList>();
			_bit |= 0x00000002;
		}
		_inputLists.add(val);
	}
	public boolean has_inputLists(){
		return (_bit & 0x00000002) == 0x00000002;
	}
	public LinkedList<OutputList> get_outputLists(){
		return _outputLists;
	}
	public void set_outputLists(LinkedList<OutputList> val){
		_bit |= 0x00000004;
		_outputLists = val;
	}
	public void add_outputLists(OutputList val){
		if(!has_outputLists()){
			_outputLists = new LinkedList<OutputList>();
			_bit |= 0x00000004;
		}
		_outputLists.add(val);
	}
	public boolean has_outputLists(){
		return (_bit & 0x00000004) == 0x00000004;
	}
	public LinkedList<Alchemy> get_alchemies(){
		return _alchemies;
	}
	public void set_alchemies(LinkedList<Alchemy> val){
		_bit |= 0x00000008;
		_alchemies = val;
	}
	public void add_alchemies(Alchemy val){
		if(!has_alchemies()){
			_alchemies = new LinkedList<Alchemy>();
			_bit |= 0x00000008;
		}
		_alchemies.add(val);
	}
	public boolean has_alchemies(){
		return (_bit & 0x00000008) == 0x00000008;
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
		if (has_result_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result_code.toInt());
		if (has_inputLists()){
			for(InputList val : _inputLists)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		if (has_outputLists()){
			for(OutputList val : _outputLists)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
		}
		if (has_alchemies()){
			for(Alchemy val : _alchemies)
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result_code()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_inputLists()){
			for(InputList val : _inputLists){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_outputLists()){
			for(OutputList val : _outputLists){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_alchemies()){
			for(Alchemy val : _alchemies){
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
		if (has_result_code()){
			output.writeEnum(1, _result_code.toInt());
		}
		if (has_inputLists()){
			for(InputList val : _inputLists){
				output.writeMessage(2, val);
			}
		}
		if (has_outputLists()){
			for(OutputList val : _outputLists){
				output.writeMessage(3, val);
			}
		}
		if (has_alchemies()){
			for(Alchemy val : _alchemies){
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
					set_result_code(ResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_inputLists((InputList)input.readMessage(InputList.newInstance()));
					break;
				}
				case 0x0000001A:{
					add_outputLists((OutputList)input.readMessage(OutputList.newInstance()));
					break;
				}
				case 0x00000022:{
					add_alchemies((Alchemy)input.readMessage(Alchemy.newInstance()));
					break;
				}
			}
		}
		if(has_inputLists()){
			_inputLists.sort(new Comparator<InputList>(){
				@Override
				public int compare(InputList o1, InputList o2) {
					return o1.get_id() - o2.get_id();
				}
			});
		}
		
		if(has_outputLists()){
			_outputLists.sort(new Comparator<OutputList>(){
				@Override
				public int compare(OutputList o1, OutputList o2) {
					return o1.get_id() - o2.get_id();
				}
			});
		}
		
		if(has_alchemies()){
			_alchemies.sort(new Comparator<Alchemy>(){
				@Override
				public int compare(Alchemy o1, Alchemy o2) {
					return o1.get_id() - o2.get_id();
				}
			});
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
		return new SC_ALCHEMY_DESIGN_ACK();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_inputLists()){
			for(InputList val : _inputLists)
				val.dispose();
			_inputLists.clear();
			_inputLists = null;
		}
		if (has_outputLists()){
			for(OutputList val : _outputLists)
				val.dispose();
			_outputLists.clear();
			_outputLists = null;
		}
		if (has_alchemies()){
			for(Alchemy val : _alchemies)
				val.dispose();
			_alchemies.clear();
			_alchemies = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class Item implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Item newInstance(){
			return new Item();
		}
		private int _name_id;
		private int _icon;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Item(){
		}
		public int get_name_id(){
			return _name_id;
		}
		public void set_name_id(int val){
			_bit |= 0x00000001;
			_name_id = val;
		}
		public boolean has_name_id(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		public int get_icon(){
			return _icon;
		}
		public void set_icon(int val){
			_bit |= 0x00000002;
			_icon = val;
		}
		public boolean has_icon(){
			return (_bit & 0x00000002) == 0x00000002;
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
			if (has_name_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
			if (has_icon())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _icon);
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
			if (has_name_id()){
				output.wirteInt32(1, _name_id);
			}
			if (has_icon()){
				output.wirteInt32(2, _icon);
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
						set_name_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_icon(input.readInt32());
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
			return new Item();
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
	public static class InputList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static InputList newInstance(){
			return new InputList();
		}
		
		private int _id;
		private HashMap<Integer, Item> _item;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private InputList(){
		}
		public int get_id(){
			return _id;
		}
		public void set_id(int val){
			_bit |= 0x00000001;
			_id = val;
		}
		public boolean has_id(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		
		public boolean isInInput(int descId){
			return _item.containsKey(descId);
		}
		
		public int isInInputs(LinkedList<CS_ALCHEMY_MAKE_REQ.Input> inputs){
			int inCount = 0;
			for(Input input : inputs){
				if(_item.containsKey(input.get_item_name_id()))
					++inCount;
			}
			return inCount;
		}
		
		public HashMap<Integer, Item> get_item(){
			return _item;
		}
		
		public Item get_item(int name_id){
			return _item.get(name_id);
		}
		
		public void add_item(Item val){
			if(!has_item()){
				_item = new HashMap<Integer, Item>(8);
				_bit |= 0x00000002;
			}
			_item.put(val.get_name_id(), val);
		}
		public boolean has_item(){
			return (_bit & 0x00000002) == 0x00000002;
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
			if (has_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
			if (has_item()){
				for(Item val : _item.values())
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
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
			if (has_item()){
				for(Item val : _item.values()){
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
			if (has_item()){
				for(Item val : _item.values()){
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
					case 0x00000008:{
						set_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_item((Item)input.readMessage(Item.newInstance()));
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
			return new InputList();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_item()){
				for(Item val : _item.values())
					val.dispose();
				_item.clear();
				_item = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public static class OutputList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static OutputList newInstance(){
			return new OutputList();
		}
		private int _id;
		private HashMap<Integer, Item> _item;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private OutputList(){
		}
		public int get_id(){
			return _id;
		}
		public void set_id(int val){
			_bit |= 0x00000001;
			_id = val;
		}
		public boolean has_id(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		public HashMap<Integer, Item> get_item(){
			return _item;
		}
		public void add_item(Item val){
			if(!has_item()){
				_item = new HashMap<Integer, Item>(8);
				_bit |= 0x00000002;
			}
			_item.put(val.get_name_id(), val);
		}
		public boolean has_item(){
			return (_bit & 0x00000002) == 0x00000002;
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
			if (has_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
			if (has_item()){
				for(Item val : _item.values())
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
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
			if (has_item()){
				for(Item val : _item.values()){
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
			if (has_item()){
				for(Item val : _item.values()){
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
					case 0x00000008:{
						set_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_item((Item)input.readMessage(Item.newInstance()));
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
			return new OutputList();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_item()){
				for(Item val : _item.values())
					val.dispose();
				_item.clear();
				_item = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public static class Alchemy implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Alchemy newInstance(){
			return new Alchemy();
		}
		private int _id;
		private LinkedList<Input> _inputs;
		private LinkedList<Output> _outputs;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Alchemy(){
		}
		public int get_id(){
			return _id;
		}
		public void set_id(int val){
			_bit |= 0x00000001;
			_id = val;
		}
		public boolean has_id(){
			return (_bit & 0x00000001) == 0x00000001;
		}
		public LinkedList<Input> get_inputs(){
			return _inputs;
		}
		public void add_inputs(Input val){
			if(!has_inputs()){
				_inputs = new LinkedList<Input>();
				_bit |= 0x00000002;
			}
			_inputs.add(val);
		}
		public boolean has_inputs(){
			return (_bit & 0x00000002) == 0x00000002;
		}
		public LinkedList<Output> get_outputs(){
			return _outputs;
		}
		public void add_outputs(Output val){
			if(!has_outputs()){
				_outputs = new LinkedList<Output>();
				_bit |= 0x00000004;
			}
			_outputs.add(val);
		}
		public boolean has_outputs(){
			return (_bit & 0x00000004) == 0x00000004;
		}
		
		public Output findOutput(LinkedList<Integer> filled_slots){
			for(Output output : _outputs){
				if(output.equalsFilled(filled_slots))
					return output;
			}
			return null;
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
			if (has_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
			if (has_inputs()){
				for(Input val : _inputs)
					size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
			if (has_outputs()){
				for(Output val : _outputs)
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
			if (has_inputs()){
				for(Input val : _inputs){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_outputs()){
				for(Output val : _outputs){
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
			if (has_inputs()){
				for(Input val : _inputs){
					output.writeMessage(2, val);
				}
			}
			if (has_outputs()){
				for(Output val : _outputs){
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
					case 0x00000012:{
						add_inputs((Input)input.readMessage(Input.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_outputs((Output)input.readMessage(Output.newInstance()));
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
			return new Alchemy();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_inputs()){
				for(Input val : _inputs)
					val.dispose();
				_inputs.clear();
				_inputs = null;
			}
			if (has_outputs()){
				for(Output val : _outputs)
					val.dispose();
				_outputs.clear();
				_outputs = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public static class Input implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static Input newInstance(){
				return new Input();
			}
			private int _slot_number;
			private boolean _is_required;
			private int _input_list_id;
			private boolean _is_hyper;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private Input(){
			}
			public int get_slot_number(){
				return _slot_number;
			}
			public void set_slot_number(int val){
				_bit |= 0x00000001;
				_slot_number = val;
			}
			public boolean has_slot_number(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			public boolean get_is_required(){
				return _is_required;
			}
			public void set_is_required(boolean val){
				_bit |= 0x00000002;
				_is_required = val;
			}
			public boolean has_is_required(){
				return (_bit & 0x00000002) == 0x00000002;
			}
			public int get_input_list_id(){
				return _input_list_id;
			}
			public void set_input_list_id(int val){
				_bit |= 0x00000004;
				_input_list_id = val;
			}
			public boolean has_input_list_id(){
				return (_bit & 0x00000004) == 0x00000004;
			}
			public boolean get_is_hyper(){
				return _is_hyper;
			}
			public void set_is_hyper(boolean val){
				_bit |= 0x00000008;
				_is_hyper = val;
			}
			public boolean has_is_hyper(){
				return (_bit & 0x00000008) == 0x00000008;
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
				if (has_slot_number())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_number);
				if (has_is_required())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_required);
				if (has_input_list_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _input_list_id);
				if (has_is_hyper())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(4, _is_hyper);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_slot_number()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_is_required()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_input_list_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_slot_number()){
					output.wirteInt32(1, _slot_number);
				}
				if (has_is_required()){
					output.writeBool(2, _is_required);
				}
				if (has_input_list_id()){
					output.wirteInt32(3, _input_list_id);
				}
				if (has_is_hyper()){
					output.writeBool(4, _is_hyper);
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
							set_slot_number(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_is_required(input.readBool());
							break;
						}
						case 0x00000018:{
							set_input_list_id(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_is_hyper(input.readBool());
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
				return new Input();
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
		public static class Output implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static Output newInstance(){
				return new Output();
			}
			private LinkedList<Integer> _filled_slots;
			private int _output_list_id;
			private LinkedList<Output> _hyper_outputs;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private Output(){
			}
			
			public LinkedList<Integer> get_filled_slots(){
				return _filled_slots;
			}
			public void add_filled_slots(int val){
				if(!has_filled_slots()){
					_filled_slots = new LinkedList<Integer>();;
					_bit |= 0x00000001;
				}
				_filled_slots.add(val);
			}
			public boolean has_filled_slots(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			
			public boolean equalsFilled(LinkedList<Integer> filled_slots){
				int size = filled_slots.size();
				if(size != _filled_slots.size())
					return false;
				
				for(int i=size - 1; i>=0; --i){
					if(filled_slots.get(i) != _filled_slots.get(i))
						return false;
				}
				return true;
			}
			
			public int get_output_list_id(){
				return _output_list_id;
			}
			public void set_output_list_id(int val){
				_bit |= 0x00000002;
				_output_list_id = val;
			}
			public boolean has_output_list_id(){
				return (_bit & 0x00000002) == 0x00000002;
			}
			public LinkedList<Output> get_hyper_outputs(){
				return _hyper_outputs;
			}
			public void add_hyper_outputs(Output val){
				if(!has_hyper_outputs()){
					_hyper_outputs = new LinkedList<Output>();
					_bit |= 0x00000004;
				}
				_hyper_outputs.add(val);
			}
			public boolean has_hyper_outputs(){
				return (_bit & 0x00000004) == 0x00000004;
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
				if (has_filled_slots()){
					for(int val : _filled_slots)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, val);
				}
				if (has_output_list_id())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _output_list_id);
				if (has_hyper_outputs()){
					for(Output val : _hyper_outputs)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_filled_slots()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_output_list_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_filled_slots()){
					for(int val : _filled_slots){
						output.wirteInt32(1, val);
					}
				}
				if (has_output_list_id()){
					output.wirteInt32(2, _output_list_id);
				}
				if (has_hyper_outputs()){
					for(Output val : _hyper_outputs){
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
							add_filled_slots(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_output_list_id(input.readInt32());
							break;
						}
						case 0x0000001A:{
							add_hyper_outputs((Output)input.readMessage(Output.newInstance()));
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
				return new Output();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_filled_slots()){
					_filled_slots = null;
				}
				if (has_hyper_outputs()){
					for(Output val : _hyper_outputs)
						val.dispose();
					_hyper_outputs.clear();
					_hyper_outputs = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
	}
	public enum ResultCode{
		RC_ALCHEMY_LOAD_START(0),
		RC_ALCHEMY_LOADING(1),
		RC_ALCHEMY_LOAD_FINISH(2),
		RC_ALCHEMY_SAME_HASH_VAL(3),
		RC_ALCHEMY_INVALID_HASH_VAL(4),
		RC_ALCHEMY_CURRENTLY_CLOSED(5),
		RC_ERROR_UNKNOWN(9999);
		private int value;
		ResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ResultCode v){
			return value == v.value;
		}
		public static ResultCode fromInt(int i){
			switch(i){
			case 0:
				return RC_ALCHEMY_LOAD_START;
			case 1:
				return RC_ALCHEMY_LOADING;
			case 2:
				return RC_ALCHEMY_LOAD_FINISH;
			case 3:
				return RC_ALCHEMY_SAME_HASH_VAL;
			case 4:
				return RC_ALCHEMY_INVALID_HASH_VAL;
			case 5:
				return RC_ALCHEMY_CURRENTLY_CLOSED;
			case 9999:
				return RC_ERROR_UNKNOWN;
			default:
				return null;
			}
		}
	}
}
