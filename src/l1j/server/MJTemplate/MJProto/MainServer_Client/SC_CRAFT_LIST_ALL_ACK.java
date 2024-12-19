package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParsee;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_CRAFT_MAKE_REQ.CraftInputItemSlotInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.CraftIdList;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.eCraftIdListReqResultType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.eCraftNpcType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_LIST_ALL_ACK.Craft.CraftOutputList.CraftOutputSFList.CraftOutputItem;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CraftProbability;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJHexHelper;
import l1j.server.server.utils.SQLUtil;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CRAFT_LIST_ALL_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static final int CRAFT_ITEM_SUCCESS_MILLIONS = 1000000;
	public static int BATCH_TRANSACTION = 0;
	private static HashMap<Integer, ProtoOutputStream> _npc2craftId;
	private static ArrayList<ProtoOutputStream> _craftCaches;
	private static ProtoOutputStream _craftSameCache;
	public static void reloadedCraftNpc(){
		HashMap<Integer, ProtoOutputStream> tmp = _npc2craftId;
		_npc2craftId = createNpcInfo();
		if(tmp != null){
			for(ProtoOutputStream s : tmp.values())
				s.dispose();
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void reloadedCraftCaches(){
		ArrayList<ProtoOutputStream> tmp = _craftCaches;
		_craftCaches = createCraftInfoCache();
		if(tmp != null){
			for(ProtoOutputStream s : tmp)
				s.dispose();
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void reloadedCraftSameCache(){
		ProtoOutputStream tmp = _craftSameCache;
		SC_CRAFT_LIST_ALL_ACK ack = new SC_CRAFT_LIST_ALL_ACK();
		ack.set_eResult(eCraftListAllReqResultType.RP_ERROR_SAME_HASH_VAL);
		_craftSameCache = ack.writeTo(MJEProtoMessages.SC_CRAFT_LIST_ALL_ACK);
		ack.dispose();
		
		if(tmp != null){
			tmp.dispose();
			tmp = null;
		}
	}
	
	public static SC_CRAFT_LIST_ALL_ACK newInstance(){
		try{
			final MJIProtoMessage message = new SC_CRAFT_LIST_ALL_ACK();
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					try {
						message.readFrom(ProtoInputStream.newInstance("./data/craftinfo.dat"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(!message.isInitialized())
						throw new IllegalArgumentException(String.format("fail initialized craftinfo data.(SC_CRAFT_LIST_ALL_ACK) %d", message.getInitializeBit()));
					
					reloadedCraftCaches();
					reloadedCraftNpc();
					reloadedCraftSameCache();
				}
			}, 2000L);
			return (SC_CRAFT_LIST_ALL_ACK) message;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static ArrayList<ProtoOutputStream> createCraftInfoCache(){
		SC_CRAFT_LIST_ALL_ACK src = (SC_CRAFT_LIST_ALL_ACK) MJEProtoMessages.SC_CRAFT_LIST_ALL_ACK.getMessageInstance();
		SC_CRAFT_LIST_ALL_ACK ack = null;
		ArrayList<ProtoOutputStream> caches = new ArrayList<ProtoOutputStream>((src.get_craft().size() / Config.CRAFT_FLAGMENTATION_SIZE) + 2);
		
		int i=0;
		eCraftListAllReqResultType resultType = eCraftListAllReqResultType.RP_CRAFT_LOAD_START;
		for(Craft c : src.get_craft().values()){
			if(ack == null){
				ack = new SC_CRAFT_LIST_ALL_ACK();
				ack.set_eResult(resultType);
				resultType = eCraftListAllReqResultType.RP_CRAFT_LOADING;
			}
			
			ack.add_craft(c);
			if(i++ % Config.CRAFT_FLAGMENTATION_SIZE == 0){
				caches.add(doMakeCreateProto(ack));
				ack = null;
			}
		}

		if(ack != null){
			caches.add(doMakeCreateProto(ack));
			ack = null;
		}
		
		ack = new SC_CRAFT_LIST_ALL_ACK();
		try {
			ack.set_eResult(eCraftListAllReqResultType.RP_CRAFT_LOAD_FINISH);
			caches.add(doMakeCreateProto(ack));
			ack.dispose();
			ack = null;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return caches;
	}
	
	private static HashMap<Integer, ProtoOutputStream> createNpcInfo(){
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		HashMap<Integer, ProtoOutputStream>	map = null;
		try{
			con 		= L1DatabaseFactory.getInstance().getConnection();
			pstm 		= con.prepareStatement("select npc_id, craft_list from craftlist");
			rs			= pstm.executeQuery();
			map			= new HashMap<Integer, ProtoOutputStream>(SQLUtil.calcRows(rs));
			MJArrangeParsee<Integer> parsee = MJArrangeParseeFactory.createIntArrange();
			
			SC_CRAFT_ID_LIST_ACK ack = (SC_CRAFT_ID_LIST_ACK)MJEProtoMessages.SC_CRAFT_ID_LIST_ACK.copyInstance();
			while(rs.next()){
				ack.set_eResult(eCraftIdListReqResultType.RP_SUCCESS);
				ack.set_eBlindType(0);
				ack.set_npc_type(eCraftNpcType.DEFAULT);

				Integer[] ids = (Integer[])MJArrangeParser.parsing(rs.getString("craft_list"), ",", parsee).result();
				for(Integer i : ids){
					CraftIdList idList = CraftIdList.newInstance();
					idList.set_craft_id(i);
					idList.set_cur_success_cnt(0);
					idList.set_max_success_cnt(0);
					ack.add_craft_id_list(idList);
				}
				ProtoOutputStream stream = ack.writeTo(MJEProtoMessages.SC_CRAFT_ID_LIST_ACK);
				map.put(rs.getInt("npc_id"), stream);
				ack.dispose();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
		return map;
	}
	
	public static ProtoOutputStream getCraftIdListFromNpcId(int npcId){
		return _npc2craftId.get(npcId);
	}
	
	public static Craft getCraft(int craftId){
		SC_CRAFT_LIST_ALL_ACK ack = (SC_CRAFT_LIST_ALL_ACK) MJEProtoMessages.SC_CRAFT_LIST_ALL_ACK.getMessageInstance();
//		System.out.println(craftId);
		return ack.get_craft(craftId);
	}
	
	public static void send(L1PcInstance pc, byte[] hash_val){
		long cur_second = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 10 > cur_second) {
			pc.sendPackets(_craftSameCache, false);
			return;
		}
		pc.setQuizTime(cur_second);
		
		if(MJHexHelper.compareArrays(Config.CRAFT_VERSION_HASH, hash_val, Config.CRAFT_VERSION_HASH.length)){
			pc.sendPackets(_craftSameCache, false);
		}else if(Config.CRAFT_TRANSMIT_SAFELINE <= L1World.getInstance().getAllPlayersSize()){
			pc.sendPackets("현재 클라이언트의 제작 정보가 이전 버전임으로 모든 목록이 나오지 않을 수 있습니다.");
			pc.sendPackets("접속기에서 오류패치를 하신 후 다시 접속하면 모든 목록을 받아볼 수 있습니다.");
//			pc.sendPackets(_craftSameCache);
			pc.sendPackets(_craftSameCache, false);
		}else{
//			pc.sendPackets("제작정보가 갱신됩니다. 3초후 창이 뜨니 잠시 기다려 주십시오. (반응이없으시다면 리스후 재접속하시길 바랍니다)");
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					pc.getNetConnection().directSendPacket(_craftCaches);
				}
			}, 100L);
		}
	}
	
	private static ProtoOutputStream doMakeCreateProto(SC_CRAFT_LIST_ALL_ACK ack){
		ProtoOutputStream stream = ack.writeTo(MJEProtoMessages.SC_CRAFT_LIST_ALL_ACK);
		stream.createProtoBytes();
		return stream;
	}
	
	private eCraftListAllReqResultType _eResult;
	private HashMap<Integer, Craft> _craft;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	private SC_CRAFT_LIST_ALL_ACK(){
	}
	public eCraftListAllReqResultType get_eResult(){
		return _eResult;
	}
	public void set_eResult(eCraftListAllReqResultType val){
		_bit |= 0x00000001;
		_eResult = val;
	}
	public boolean has_eResult(){
		return (_bit & 0x00000001) == 0x00000001;
	}
	public HashMap<Integer, Craft> get_craft(){
		return _craft;
	}
	public Craft get_craft(int craftId){
		return _craft.get(craftId);
	}
	public void add_craft(Craft val){
		if(!has_craft()){
			_craft = new HashMap<Integer, Craft>();
			_bit |= 0x00000002;
		}
		_craft.put(val.get_id(), val);
	}
	public boolean has_craft(){
		return (_bit & 0x00000002) == 0x00000002;
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
		if (has_eResult())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _eResult.toInt());
		if (has_craft()){
			for(Craft val : _craft.values())
				size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_eResult()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_craft()){
			for(Craft val : _craft.values()){
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
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_eResult()){
			output.writeEnum(1, _eResult.toInt());
		}
		if (has_craft()){
			for(Craft val : _craft.values()){
				output.writeMessage(2, val);
			}
		}
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
					set_eResult(eCraftListAllReqResultType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_craft((Craft)input.readMessage(Craft.newInstance()));
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
		return new SC_CRAFT_LIST_ALL_ACK();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if (has_craft()){
			for(Craft val : _craft.values())
				val.dispose();
			_craft.clear();
			_craft = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class Craft implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static Craft newInstance(){
			return new Craft();
		}
		private int _id;
		private CraftAttr _craft_attr;
		private int _required_classes;
		private CraftRequiredQuestList _required_quests;
		private CraftRequiredSpriteList _required_sprites;
		private CraftRequiredItemList _required_items;
		private CraftInputList _inputs;
		private CraftOutputList _outputs;
		private int _batch_delay_sec;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private Craft(){
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
		public CraftAttr get_craft_attr(){
			return _craft_attr;
		}
		public void set_craft_attr(CraftAttr val){
			_bit |= 0x00000002;
			_craft_attr = val;
		}
		public boolean has_craft_attr(){
			return (_bit & 0x00000002) == 0x00000002;
		}
		public int get_required_classes(){
			return _required_classes;
		}
		public void set_required_classes(int val){
			_bit |= 0x00000004;
			_required_classes = val;
		}
		public boolean has_required_classes(){
			return (_bit & 0x00000004) == 0x00000004;
		}
		public CraftRequiredQuestList get_required_quests(){
			return _required_quests;
		}
		public void set_required_quests(CraftRequiredQuestList val){
			_bit |= 0x00000008;
			_required_quests = val;
		}
		public boolean has_required_quests(){
			return (_bit & 0x00000008) == 0x00000008;
		}
		public CraftRequiredSpriteList get_required_sprites(){
			return _required_sprites;
		}
		public void set_required_sprites(CraftRequiredSpriteList val){
			_bit |= 0x00000010;
			_required_sprites = val;
		}
		public boolean has_required_sprites(){
			return (_bit & 0x00000010) == 0x00000010;
		}
		public CraftRequiredItemList get_required_items(){
			return _required_items;
		}
		public void set_required_items(CraftRequiredItemList val){
			_bit |= 0x00000020;
			_required_items = val;
		}
		public boolean has_required_items(){
			return (_bit & 0x00000020) == 0x00000020;
		}
		public CraftInputList get_inputs(){
			return _inputs;
		}
		public void set_inputs(CraftInputList val){
			_bit |= 0x00000040;
			_inputs = val;
		}
		public boolean has_inputs(){
			return (_bit & 0x00000040) == 0x00000040;
		}
		public CraftOutputList get_outputs(){
			return _outputs;
		}
		public void set_outputs(CraftOutputList val){
			_bit |= 0x00000080;
			_outputs = val;
		}
		public boolean has_outputs(){
			return (_bit & 0x00000080) == 0x00000080;
		}
		public int get_batch_delay_sec(){
			return _batch_delay_sec;
		}
		public void set_batch_delay_sec(int val){
			_bit |= 0x00000100;
			_batch_delay_sec = val;
		}
		public boolean has_batch_delay_sec(){
			return (_bit & 0x00000100) == 0x00000100;
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
			if (has_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _id);
			if (has_craft_attr())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _craft_attr);
			if (has_required_classes())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _required_classes);
			if (has_required_quests())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _required_quests);
			if (has_required_sprites())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _required_sprites);
			if (has_required_items())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _required_items);
			if (has_inputs())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _inputs);
			if (has_outputs())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(8, _outputs);
			if (has_batch_delay_sec())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _batch_delay_sec);
			
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
			if (!has_craft_attr()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_required_classes()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_required_quests()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_required_sprites()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_required_items()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_inputs()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_outputs()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try {
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
			if (has_id()){
				output.wirteInt32(1, _id);
			}
			if (has_craft_attr()){
				output.writeMessage(2, _craft_attr);
			}
			if (has_required_classes()){
				output.wirteInt32(3, _required_classes);
			}
			if (has_required_quests()){
				output.writeMessage(4, _required_quests);
			}
			if (has_required_sprites()){
				output.writeMessage(5, _required_sprites);
			}
			if (has_required_items()){
				output.writeMessage(6, _required_items);
			}
			if (has_inputs()){
				output.writeMessage(7, _inputs);
			}
			if (has_outputs()){
				output.writeMessage(8, _outputs);
			}
			if (has_batch_delay_sec()){
				output.wirteInt32(9, _batch_delay_sec);
			}
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
						set_craft_attr((CraftAttr)input.readMessage(CraftAttr.newInstance()));
						break;
					}
					case 0x00000018:{
						set_required_classes(input.readInt32());
						break;
					}
					case 0x00000022:{
						set_required_quests((CraftRequiredQuestList)input.readMessage(CraftRequiredQuestList.newInstance()));
						break;
					}
					case 0x0000002A:{
						set_required_sprites((CraftRequiredSpriteList)input.readMessage(CraftRequiredSpriteList.newInstance()));
						break;
					}
					case 0x00000032:{
						set_required_items((CraftRequiredItemList)input.readMessage(CraftRequiredItemList.newInstance()));
						break;
					}
					case 0x0000003A:{
						set_inputs((CraftInputList)input.readMessage(CraftInputList.newInstance()));
						break;
					}
					case 0x00000042:{
						set_outputs((CraftOutputList)input.readMessage(CraftOutputList.newInstance()));
						break;
					}
					case 0x00000048:{
						set_batch_delay_sec(input.readInt32());
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
			return new Craft();
		}
		@Override
		public MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			if (has_craft_attr() && _craft_attr != null){
				_craft_attr.dispose();
				_craft_attr = null;
			}
			if (has_required_quests() && _required_quests != null){
				_required_quests.dispose();
				_required_quests = null;
			}
			if (has_required_sprites() && _required_sprites != null){
				_required_sprites.dispose();
				_required_sprites = null;
			}
			if (has_required_items() && _required_items != null){
				_required_items.dispose();
				_required_items = null;
			}
			if (has_inputs() && _inputs != null){
				_inputs.dispose();
				_inputs = null;
			}
			if (has_outputs() && _outputs != null){
				_outputs.dispose();
				_outputs = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public static class CraftRequiredQuestList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static CraftRequiredQuestList newInstance(){
				return new CraftRequiredQuestList();
			}
			private boolean _and_or;
			private int _count;
			private CraftQuestFlag _flag;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CraftRequiredQuestList(){
			}
			public boolean get_and_or(){
				return _and_or;
			}
			public void set_and_or(boolean val){
				_bit |= 0x00000001;
				_and_or = val;
			}
			public boolean has_and_or(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			public int get_count(){
				return _count;
			}
			public void set_count(int val){
				_bit |= 0x00000002;
				_count = val;
			}
			public boolean has_count(){
				return (_bit & 0x00000002) == 0x00000002;
			}
			public CraftQuestFlag get_flag(){
				return _flag;
			}
			public void set_flag(CraftQuestFlag val){
				_bit |= 0x00000004;
				_flag = val;
			}
			public boolean has_flag(){
				return (_bit & 0x00000004) == 0x00000004;
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
				if (has_and_or())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _and_or);
				if (has_count())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
				if (has_flag())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _flag);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_and_or()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_count()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_flag()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			
			@Override
			public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
				try {
					writeTo(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return stream;
			}
			
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_and_or()){
					output.writeBool(1, _and_or);
				}
				if (has_count()){
					output.wirteInt32(2, _count);
				}
				if (has_flag()){
					output.writeMessage(3, _flag);
				}
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
							set_and_or(input.readBool());
							break;
						}
						case 0x00000010:{
							set_count(input.readInt32());
							break;
						}
						case 0x0000001A:{
							set_flag((CraftQuestFlag)input.readMessage(CraftQuestFlag.newInstance()));
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
				return new CraftRequiredQuestList();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_flag() && _flag != null){
					_flag.dispose();
					_flag = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
			public static class CraftQuestFlag implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
				public static CraftQuestFlag newInstance(){
					return new CraftQuestFlag();
				}
				private long _flag1;
				private long _flag2;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private CraftQuestFlag(){
				}
				public long get_flag1(){
					return _flag1;
				}
				public void set_flag1(long val){
					_bit |= 0x00000001;
					_flag1 = val;
				}
				public boolean has_flag1(){
					return (_bit & 0x00000001) == 0x00000001;
				}
				public long get_flag2(){
					return _flag2;
				}
				public void set_flag2(long val){
					_bit |= 0x00000002;
					_flag2 = val;
				}
				public boolean has_flag2(){
					return (_bit & 0x00000002) == 0x00000002;
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
					if (has_flag1())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(1, _flag1);
					if (has_flag2())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _flag2);
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_flag1()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_flag2()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				
				@Override
				public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
					try {
						writeTo(stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return stream;
				}
				
				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
					if (has_flag1()){
						output.writeInt64(1, _flag1);
					}
					if (has_flag2()){
						output.writeInt64(2, _flag2);
					}
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
								set_flag1(input.readInt64());
								break;
							}
							case 0x00000010:{
								set_flag2(input.readInt64());
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
					return new CraftQuestFlag();
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
		public static class CraftRequiredSpriteList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static CraftRequiredSpriteList newInstance(){
				return new CraftRequiredSpriteList();
			}
			private int _count;
			private LinkedList<Integer> _sprite_id;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CraftRequiredSpriteList(){
			}
			public int get_count(){
				return _count;
			}
			public void set_count(int val){
				_bit |= 0x00000001;
				_count = val;
			}
			public boolean has_count(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			public LinkedList<Integer> get_sprite_id(){
				return _sprite_id;
			}
			public void add_sprite_id(int val){
				if(!has_sprite_id()){
					_sprite_id = new LinkedList<Integer>();
					_bit |= 0x00000002;
				}
				_sprite_id.add(val);
			}
			public boolean has_sprite_id(){
				return (_bit & 0x00000002) == 0x00000002;
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
				if (has_count())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _count);
				if (has_sprite_id()){
					for(int val : _sprite_id)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, val);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_count()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_sprite_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			
			@Override
			public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
				try {
					writeTo(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return stream;
			}
			
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_count()){
					output.wirteInt32(1, _count);
				}
				if (has_sprite_id()){
					for(int val : _sprite_id){
						output.wirteInt32(2, val);
					}
				}
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
							set_count(input.readInt32());
							break;
						}
						case 0x00000010:{
							add_sprite_id(input.readInt32());
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
				return new CraftRequiredSpriteList();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_sprite_id() && _sprite_id != null){
					_sprite_id.clear();
					_sprite_id = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
		}
		public static class CraftRequiredItemList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static CraftRequiredItemList newInstance(){
				return new CraftRequiredItemList();
			}
			private boolean _and_or;
			private int _count;
			private LinkedList<CraftRequiredItem> _items;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CraftRequiredItemList(){
			}
			public boolean get_and_or(){
				return _and_or;
			}
			public void set_and_or(boolean val){
				_bit |= 0x00000001;
				_and_or = val;
			}
			public boolean has_and_or(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			public int get_count(){
				return _count;
			}
			public void set_count(int val){
				_bit |= 0x00000002;
				_count = val;
			}
			public boolean has_count(){
				return (_bit & 0x00000002) == 0x00000002;
			}
			public LinkedList<CraftRequiredItem> get_items(){
				return _items;
			}
			public void add_items(CraftRequiredItem val){
				if(!has_items()){
					_items = new LinkedList<CraftRequiredItem>();
					_bit |= 0x00000004;
				}
				_items.add(val);
			}
			public boolean has_items(){
				return (_bit & 0x00000004) == 0x00000004;
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
				if (has_and_or())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _and_or);
				if (has_count())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
				if (has_items()){
					for(CraftRequiredItem val : _items)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_and_or()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_count()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_items()){
					for(CraftRequiredItem val : _items){
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
			public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
				try {
					writeTo(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return stream;
			}
			
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_and_or()){
					output.writeBool(1, _and_or);
				}
				if (has_count()){
					output.wirteInt32(2, _count);
				}
				if (has_items()){
					for(CraftRequiredItem val : _items){
						output.writeMessage(3, val);
					}
				}
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
							set_and_or(input.readBool());
							break;
						}
						case 0x00000010:{
							set_count(input.readInt32());
							break;
						}
						case 0x0000001A:{
							add_items((CraftRequiredItem)input.readMessage(CraftRequiredItem.newInstance()));
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
				return new CraftRequiredItemList();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_items()){
					for(CraftRequiredItem val : _items)
						val.dispose();
					_items.clear();
					_items = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
			public static class CraftRequiredItem implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
				public static CraftRequiredItem newInstance(){
					return new CraftRequiredItem();
				}
				private int _name_id;
				private int _count;
				private boolean _is_nagative;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private CraftRequiredItem(){
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
				public int get_count(){
					return _count;
				}
				public void set_count(int val){
					_bit |= 0x00000002;
					_count = val;
				}
				public boolean has_count(){
					return (_bit & 0x00000002) == 0x00000002;
				}
				public boolean get_is_nagative(){
					return _is_nagative;
				}
				public void set_is_nagative(boolean val){
					_bit |= 0x00000004;
					_is_nagative = val;
				}
				public boolean has_is_nagative(){
					return (_bit & 0x00000004) == 0x00000004;
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
					if (has_name_id())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
					if (has_count())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
					if (has_is_nagative())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _is_nagative);
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_name_id()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_count()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_is_nagative()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				
				@Override
				public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
					try {
						writeTo(stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return stream;
				}
				
				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
					if (has_name_id()){
						output.wirteInt32(1, _name_id);
					}
					if (has_count()){
						output.wirteInt32(2, _count);
					}
					if (has_is_nagative()){
						output.writeBool(3, _is_nagative);
					}
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
								set_count(input.readInt32());
								break;
							}
							case 0x00000018:{
								set_is_nagative(input.readBool());
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
					return new CraftRequiredItem();
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
		public static class CraftInputList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static CraftInputList newInstance(){
				return new CraftInputList();
			}
			private LinkedList<CraftInputItem> _arr_input_item;
			private LinkedList<CraftInputItem> _arr_option_item;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			
			public int[] calculateSlotAndIncreaseProb(LinkedList<CraftInputItemSlotInfo> slotInfo){
				int currentSlotNumber = 1;
				int increase_prob = 0;
				
				for(CraftInputItem input : get_arr_input_item()){
					int slot = input.get_slot();
					if(currentSlotNumber < slot){
						break;					
					}else if(currentSlotNumber > slot)
						continue;
				
					CraftInputItemSlotInfo sInfo = slotInfo.get(currentSlotNumber - 1);
					if(sInfo.validation(input)){
						increase_prob += input.get_increase_prob();
						sInfo.set_count(input.get_count());
						++currentSlotNumber;
					}
				}
				return new int[]{currentSlotNumber, increase_prob};
			}
			
			private CraftInputList(){
			}
			public LinkedList<CraftInputItem> get_arr_input_item(){
				return _arr_input_item;
			}
			public void add_arr_input_item(CraftInputItem val){
				if(!has_arr_input_item()){
					_arr_input_item = new LinkedList<CraftInputItem>();
					_bit |= 0x00000001;
				}
				_arr_input_item.add(val);
			}
			public boolean has_arr_input_item(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			public LinkedList<CraftInputItem> get_arr_option_item(){
				return _arr_option_item;
			}
			public void add_arr_option_item(CraftInputItem val){
				if(!has_arr_option_item()){
					_arr_option_item = new LinkedList<CraftInputItem>();
					_bit |= 0x00000002;
				}
				_arr_option_item.add(val);
			}
			public boolean has_arr_option_item(){
				return (_bit & 0x00000002) == 0x00000002;
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
				if (has_arr_input_item()){
					for(CraftInputItem val : _arr_input_item)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
				}
				if (has_arr_option_item()){
					for(CraftInputItem val : _arr_option_item)
						size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (has_arr_input_item()){
					for(CraftInputItem val : _arr_input_item){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}
				if (has_arr_option_item()){
					for(CraftInputItem val : _arr_option_item){
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
			public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
				try {
					writeTo(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return stream;
			}
			
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_arr_input_item()){
					for(CraftInputItem val : _arr_input_item){
						output.writeMessage(1, val);
					}
				}
				if (has_arr_option_item()){
					for(CraftInputItem val : _arr_option_item){
						output.writeMessage(2, val);
					}
				}
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
							add_arr_input_item((CraftInputItem)input.readMessage(CraftInputItem.newInstance()));
							break;
						}
						case 0x00000012:{
							add_arr_option_item((CraftInputItem)input.readMessage(CraftInputItem.newInstance()));
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
				return new CraftInputList();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_arr_input_item()){
					for(CraftInputItem val : _arr_input_item)
						val.dispose();
					_arr_input_item.clear();
					_arr_input_item = null;
				}
				if (has_arr_option_item()){
					for(CraftInputItem val : _arr_option_item)
						val.dispose();
					_arr_option_item.clear();
					_arr_option_item = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
			public static class CraftInputItem implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
				public static CraftInputItem newInstance(){
					return new CraftInputItem();
				}
				private int _name_id;
				private int _count;
				private int _slot;
				private int _enchant;
				private int _bless;
				private String _desc;
				private int _iconId;
				private int _elemental_enchant_type;
				private int _elemental_enchant_value;
				private int _increase_prob;
				private boolean _all_enchants_allowed;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private CraftInputItem(){
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
				public int get_count(){
					return _count;
				}
				public void set_count(int val){
					_bit |= 0x00000002;
					_count = val;
				}
				public boolean has_count(){
					return (_bit & 0x00000002) == 0x00000002;
				}
				public int get_slot(){
					return _slot;
				}
				public void set_slot(int val){
					_bit |= 0x00000004;
					_slot = val;
				}
				public boolean has_slot(){
					return (_bit & 0x00000004) == 0x00000004;
				}
				public int get_enchant(){
					return _enchant;
				}
				public void set_enchant(int val){
					_bit |= 0x00000008;
					_enchant = val;
				}
				public boolean has_enchant(){
					return (_bit & 0x00000008) == 0x00000008;
				}
				public int get_bless(){
					return _bless;
				}
				public void set_bless(int val){
					_bit |= 0x00000010;
					_bless = val;
				}
				public boolean has_bless(){
					return (_bit & 0x00000010) == 0x00000010;
				}
				public String get_desc(){
					return _desc;
				}
				public void set_desc(String val){
					_bit |= 0x00000020;
					_desc = val;
				}
				public boolean has_desc(){
					return (_bit & 0x00000020) == 0x00000020;
				}
				public int get_iconId(){
					return _iconId;
				}
				public void set_iconId(int val){
					_bit |= 0x00000040;
					_iconId = val;
				}
				public boolean has_iconId(){
					return (_bit & 0x00000040) == 0x00000040;
				}
				public int get_elemental_enchant_type(){
					return _elemental_enchant_type;
				}
				public void set_elemental_enchant_type(int val){
					_bit |= 0x00000080;
					_elemental_enchant_type = val;
				}
				public boolean has_elemental_enchant_type(){
					return (_bit & 0x00000080) == 0x00000080;
				}
				public int get_elemental_enchant_value(){
					return _elemental_enchant_value;
				}
				public void set_elemental_enchant_value(int val){
					_bit |= 0x00000100;
					_elemental_enchant_value = val;
				}
				public boolean has_elemental_enchant_value(){
					return (_bit & 0x00000100) == 0x00000100;
				}
				public int get_increase_prob(){
					return _increase_prob;
				}
				public void set_increase_prob(int val){
					_bit |= 0x00000200;
					_increase_prob = val;
				}
				public boolean has_increase_prob(){
					return (_bit & 0x00000200) == 0x00000200;
				}
				public boolean get_all_enchants_allowed(){
					return _all_enchants_allowed;
				}
				public void set_all_enchants_allowed(boolean val){
					_bit |= 0x00000400;
					_all_enchants_allowed = val;
				}
				public boolean has_all_enchants_allowed(){
					return (_bit & 0x00000400) == 0x00000400;
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
					if (has_name_id())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
					if (has_count())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
					if (has_slot())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _slot);
					if (has_enchant())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _enchant);
					if (has_bless())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _bless);
					if (has_desc())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(6, _desc);
					if (has_iconId())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _iconId);
					if (has_elemental_enchant_type())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _elemental_enchant_type);
					if (has_elemental_enchant_value())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _elemental_enchant_value);
					if (has_increase_prob())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _increase_prob);
					if (has_all_enchants_allowed())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _all_enchants_allowed);
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_name_id()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_count()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_slot()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_enchant()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_bless()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_desc()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_iconId()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				
				@Override
				public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
					try {
						writeTo(stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return stream;
				}
				
				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
					if (has_name_id()){
						output.wirteInt32(1, _name_id);
					}
					if (has_count()){
						output.wirteInt32(2, _count);
					}
					if (has_slot()){
						output.wirteInt32(3, _slot);
					}
					if (has_enchant()){
						output.wirteInt32(4, _enchant);
					}
					if (has_bless()){
						output.wirteInt32(5, _bless);
					}
					if (has_desc()){
						output.writeString(6, _desc);
					}
					if (has_iconId()){
						output.wirteInt32(7, _iconId);
					}
					if (has_elemental_enchant_type()){
						output.wirteInt32(8, _elemental_enchant_type);
					}
					if (has_elemental_enchant_value()){
						output.wirteInt32(9, _elemental_enchant_value);
					}
					if (has_increase_prob()){
						output.wirteInt32(10, _increase_prob);
					}
					if (has_all_enchants_allowed()){
						output.writeBool(11, _all_enchants_allowed);
					}
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
								set_count(input.readInt32());
								break;
							}
							case 0x00000018:{
								set_slot(input.readInt32());
								break;
							}
							case 0x00000020:{
								set_enchant(input.readInt32());
								break;
							}
							case 0x00000028:{
								set_bless(input.readInt32());
								break;
							}
							case 0x00000032:{
								set_desc(input.readString());
								break;
							}
							case 0x00000038:{
								set_iconId(input.readInt32());
								break;
							}
							case 0x00000040:{
								set_elemental_enchant_type(input.readInt32());
								break;
							}
							case 0x00000048:{
								set_elemental_enchant_value(input.readInt32());
								break;
							}
							case 0x00000050:{
								set_increase_prob(input.readInt32());
								break;
							}
							case 0x00000058:{
								set_all_enchants_allowed(input.readBool());
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
					return new CraftInputItem();
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
		public static class CraftOutputList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static CraftOutputList newInstance(){
				return new CraftOutputList();
			}
			private CraftOutputSFList _success;
			private CraftOutputSFList _failure;
			private int _success_prob_by_million;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CraftOutputList(){
			}
			public CraftOutputSFList get_success(){
				return _success;
			}
			public void set_success(CraftOutputSFList val){
				_bit |= 0x00000001;
				_success = val;
			}
			public boolean has_success(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			public CraftOutputSFList get_failure(){
				return _failure;
			}
			public void set_failure(CraftOutputSFList val){
				_bit |= 0x00000002;
				_failure = val;
			}
			public boolean has_failure(){
				return (_bit & 0x00000002) == 0x00000002;
			}
			public int get_success_prob_by_million(){
				return _success_prob_by_million;
			}
			public void set_success_prob_by_million(int val){
				_bit |= 0x00000004;
				_success_prob_by_million = val;
			}
			public boolean has_success_prob_by_million(){
				return (_bit & 0x00000004) == 0x00000004;
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
				if (has_success())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _success);
				if (has_failure())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _failure);
				if (has_success_prob_by_million())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _success_prob_by_million);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_success()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_failure()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_success_prob_by_million()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			
			@Override
			public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
				try {
					writeTo(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return stream;
			}
			
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_success()){
					output.writeMessage(1, _success);
				}
				if (has_failure()){
					output.writeMessage(2, _failure);
				}
				if (has_success_prob_by_million()){
					output.wirteInt32(3, _success_prob_by_million);
				}
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
							set_success((CraftOutputSFList)input.readMessage(CraftOutputSFList.newInstance()));
							break;
						}
						case 0x00000012:{
							set_failure((CraftOutputSFList)input.readMessage(CraftOutputSFList.newInstance()));
							break;
						}
						case 0x00000018:{
							set_success_prob_by_million(input.readInt32());
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
				return new CraftOutputList();
			}
			@Override
			public MJIProtoMessage reloadInstance(){
				return newInstance();
			}
			@Override
			public void dispose(){
				if (has_success() && _success != null){
					_success.dispose();
					_success = null;
				}
				if (has_failure() && _failure != null){
					_failure.dispose();
					_failure = null;
				}
				_bit = 0;
				_memorizedIsInitialized = -1;
			}
			
			public CraftOutputItemResult createOutputItem(int increase_prob, int craft_id){
				int prob = get_success_prob_by_million() + increase_prob;
				if(prob >= CRAFT_ITEM_SUCCESS_MILLIONS)
					return new CraftOutputItemResult(extractOutputItem(get_success(), prob), true);

				int real_prob = (int)((double)prob * Config.CRAFT_INCREASE_PROB_BYMILLION);
				Integer db_prob = CraftProbability.getInstance().get(craft_id);
				if(db_prob != null && db_prob > 0)
					real_prob = db_prob;
				
//				System.out.println("제작확률: "+real_prob);
				
				if(MJRnd.isWinning(CRAFT_ITEM_SUCCESS_MILLIONS, real_prob))
					return new CraftOutputItemResult(extractOutputItem(get_success(), real_prob), true);
				
				CraftOutputSFList fail = get_failure();
	            if(fail.get_prob_count() <= 0 && fail.get_non_prob_count() <= 0)
	               return new CraftOutputItemResult(null, false);
				return new CraftOutputItemResult(extractOutputItem(get_failure(), real_prob), false);
			}
			
			private CraftOutputItem extractOutputItem(CraftOutputSFList sfList, int prob){
				int probCount = sfList.get_prob_count();
				if(probCount > 0){
					LinkedList<CraftOutputItem> outputItems = sfList.get_output_prob_items();
					return outputItems.get((probCount == 1 || MJRnd.isWinning(CRAFT_ITEM_SUCCESS_MILLIONS, prob) ? 0 : MJRnd.next(outputItems.size() - 1) + 1));
				}
				return sfList.get_output_items().get(0);
			}
			
			public static class CraftOutputSFList implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
				public static CraftOutputSFList newInstance(){
					return new CraftOutputSFList();
				}
				private CraftQuestFlag _on_flag;
				private CraftQuestFlag _off_flag;
				private int _prob_count;
				private int _non_prob_count;
				private LinkedList<CraftOutputItem> _output_prob_items;
				private LinkedList<CraftOutputItem> _output_items;
				private LinkedList<CraftEvent> _events;
				private int _event_count;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private CraftOutputSFList(){
				}
				public CraftQuestFlag get_on_flag(){
					return _on_flag;
				}
				public void set_on_flag(CraftQuestFlag val){
					_bit |= 0x00000001;
					_on_flag = val;
				}
				public boolean has_on_flag(){
					return (_bit & 0x00000001) == 0x00000001;
				}
				public CraftQuestFlag get_off_flag(){
					return _off_flag;
				}
				public void set_off_flag(CraftQuestFlag val){
					_bit |= 0x00000002;
					_off_flag = val;
				}
				public boolean has_off_flag(){
					return (_bit & 0x00000002) == 0x00000002;
				}
				public int get_prob_count(){
					return _prob_count;
				}
				public void set_prob_count(int val){
					_bit |= 0x00000004;
					_prob_count = val;
				}
				public boolean has_prob_count(){
					return (_bit & 0x00000004) == 0x00000004;
				}
				public int get_non_prob_count(){
					return _non_prob_count;
				}
				public void set_non_prob_count(int val){
					_bit |= 0x00000008;
					_non_prob_count = val;
				}
				public boolean has_non_prob_count(){
					return (_bit & 0x00000008) == 0x00000008;
				}
				public LinkedList<CraftOutputItem> get_output_prob_items(){
					return _output_prob_items;
				}
				public void add_output_prob_items(CraftOutputItem val){
					if(!has_output_prob_items()){
						_output_prob_items = new LinkedList<CraftOutputItem>();
						_bit |= 0x00000010;
					}
					_output_prob_items.add(val);
				}
				public boolean has_output_prob_items(){
					return (_bit & 0x00000010) == 0x00000010;
				}
				public LinkedList<CraftOutputItem> get_output_items(){
					return _output_items;
				}
				public void add_output_items(CraftOutputItem val){
					if(!has_output_items()){
						_output_items = new LinkedList<CraftOutputItem>();
						_bit |= 0x00000020;
					}
					_output_items.add(val);
				}
				public boolean has_output_items(){
					return (_bit & 0x00000020) == 0x00000020;
				}
				public LinkedList<CraftEvent> get_events(){
					return _events;
				}
				public void add_events(CraftEvent val){
					if(!has_events()){
						_events = new LinkedList<CraftEvent>();
						_bit |= 0x00000040;
					}
					_events.add(val);
				}
				public boolean has_events(){
					return (_bit & 0x00000040) == 0x00000040;
				}
				public int get_event_count(){
					return _event_count;
				}
				public void set_event_count(int val){
					_bit |= 0x00000080;
					_event_count = val;
				}
				public boolean has_event_count(){
					return (_bit & 0x00000080) == 0x00000080;
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
					if (has_on_flag())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _on_flag);
					if (has_off_flag())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _off_flag);
					if (has_prob_count())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _prob_count);
					if (has_non_prob_count())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _non_prob_count);
					if (has_output_prob_items()){
						for(CraftOutputItem val : _output_prob_items)
							size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
					}
					if (has_output_items()){
						for(CraftOutputItem val : _output_items)
							size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
					}
					if (has_events()){
						for(CraftEvent val : _events)
							size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, val);
					}
					if (has_event_count())
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _event_count);
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_on_flag()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_off_flag()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_prob_count()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_non_prob_count()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_output_prob_items()){
						for(CraftOutputItem val : _output_prob_items){
							if (!val.isInitialized()){
								_memorizedIsInitialized = -1;
								return false;
							}
						}
					}
					if (has_output_items()){
						for(CraftOutputItem val : _output_items){
							if (!val.isInitialized()){
								_memorizedIsInitialized = -1;
								return false;
							}
						}
					}
					if (has_events()){
						for(CraftEvent val : _events){
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
				public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
					try {
						writeTo(stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return stream;
				}
				
				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
					if (has_on_flag()){
						output.writeMessage(1, _on_flag);
					}
					if (has_off_flag()){
						output.writeMessage(2, _off_flag);
					}
					if (has_prob_count()){
						output.wirteInt32(3, _prob_count);
					}
					if (has_non_prob_count()){
						output.wirteInt32(4, _non_prob_count);
					}
					if (has_output_prob_items()){
						for(CraftOutputItem val : _output_prob_items){
							output.writeMessage(5, val);
						}
					}
					if (has_output_items()){
						for(CraftOutputItem val : _output_items){
							output.writeMessage(6, val);
						}
					}
					if (has_events()){
						for(CraftEvent val : _events){
							output.writeMessage(7, val);
						}
					}
					if (has_event_count()){
						output.wirteInt32(8, _event_count);
					}
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
								set_on_flag((CraftQuestFlag)input.readMessage(CraftQuestFlag.newInstance()));
								break;
							}
							case 0x00000012:{
								set_off_flag((CraftQuestFlag)input.readMessage(CraftQuestFlag.newInstance()));
								break;
							}
							case 0x00000018:{
								set_prob_count(input.readInt32());
								break;
							}
							case 0x00000020:{
								set_non_prob_count(input.readInt32());
								break;
							}
							case 0x0000002A:{
								add_output_prob_items((CraftOutputItem)input.readMessage(CraftOutputItem.newInstance()));
								break;
							}
							case 0x00000032:{
								add_output_items((CraftOutputItem)input.readMessage(CraftOutputItem.newInstance()));
								break;
							}
							case 0x0000003A:{
								add_events((CraftEvent)input.readMessage(CraftEvent.newInstance()));
								break;
							}
							case 0x00000040:{
								set_event_count(input.readInt32());
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
					return new CraftOutputSFList();
				}
				@Override
				public MJIProtoMessage reloadInstance(){
					return newInstance();
				}
				@Override
				public void dispose(){
					if (has_on_flag() && _on_flag != null){
						_on_flag.dispose();
						_on_flag = null;
					}
					if (has_off_flag() && _off_flag != null){
						_off_flag.dispose();
						_off_flag = null;
					}
					if (has_output_prob_items()){
						for(CraftOutputItem val : _output_prob_items)
							val.dispose();
						_output_prob_items.clear();
						_output_prob_items = null;
					}
					if (has_output_items()){
						for(CraftOutputItem val : _output_items)
							val.dispose();
						_output_items.clear();
						_output_items = null;
					}
					if (has_events()){
						for(CraftEvent val : _events)
							val.dispose();
						_events.clear();
						_events = null;
					}
					_bit = 0;
					_memorizedIsInitialized = -1;
				}
				public static class CraftQuestFlag implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
					public static CraftQuestFlag newInstance(){
						return new CraftQuestFlag();
					}
					private long _flag1;
					private long _flag2;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;
					private CraftQuestFlag(){
					}
					public long get_flag1(){
						return _flag1;
					}
					public void set_flag1(long val){
						_bit |= 0x00000001;
						_flag1 = val;
					}
					public boolean has_flag1(){
						return (_bit & 0x00000001) == 0x00000001;
					}
					public long get_flag2(){
						return _flag2;
					}
					public void set_flag2(long val){
						_bit |= 0x00000002;
						_flag2 = val;
					}
					public boolean has_flag2(){
						return (_bit & 0x00000002) == 0x00000002;
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
						if (has_flag1())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(1, _flag1);
						if (has_flag2())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _flag2);
						_memorizedSerializedSize = size;
						return size;
					}
					@Override
					public boolean isInitialized(){
						if(_memorizedIsInitialized == 1)
							return true;
						if (!has_flag1()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_flag2()){
							_memorizedIsInitialized = -1;
							return false;
						}
						_memorizedIsInitialized = 1;
						return true;
					}
					
					@Override
					public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
						l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
						try {
							writeTo(stream);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return stream;
					}
					
					@Override
					public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
						if (has_flag1()){
							output.writeInt64(1, _flag1);
						}
						if (has_flag2()){
							output.writeInt64(2, _flag2);
						}
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
									set_flag1(input.readInt64());
									break;
								}
								case 0x00000010:{
									set_flag2(input.readInt64());
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
						return new CraftQuestFlag();
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
				public static class CraftOutputItem implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
					public static CraftOutputItem newInstance(){
						return new CraftOutputItem();
					}
					private int _name_id;
					private int _count;
					private int _slot;
					private int _enchant;
					private int _bless;
					private int _elemental_type;
					private int _elemental_level;
					private String _desc;
					private int _system_desc;
					private int _broadcast_desc;
					private int _iconId;
					private String _url;
					private byte[] _extra_desc;
					private int _inherit_enchant_from;
					private int _inherit_elemental_enchant_from;
					private int _event_id;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;
					private CraftOutputItem(){
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
					public int get_count(){
						return _count;
					}
					public void set_count(int val){
						_bit |= 0x00000002;
						_count = val;
					}
					public boolean has_count(){
						return (_bit & 0x00000002) == 0x00000002;
					}
					public int get_slot(){
						return _slot;
					}
					public void set_slot(int val){
						_bit |= 0x00000004;
						_slot = val;
					}
					public boolean has_slot(){
						return (_bit & 0x00000004) == 0x00000004;
					}
					public int get_enchant(){
						return _enchant;
					}
					public void set_enchant(int val){
						_bit |= 0x00000008;
						_enchant = val;
					}
					public boolean has_enchant(){
						return (_bit & 0x00000008) == 0x00000008;
					}
					public int get_bless(){
						return _bless;
					}
					public void set_bless(int val){
						_bit |= 0x00000010;
						_bless = val;
					}
					public boolean has_bless(){
						return (_bit & 0x00000010) == 0x00000010;
					}
					public int get_elemental_type(){
						return _elemental_type;
					}
					public void set_elemental_type(int val){
						_bit |= 0x00000020;
						_elemental_type = val;
					}
					public boolean has_elemental_type(){
						return (_bit & 0x00000020) == 0x00000020;
					}
					public int get_elemental_level(){
						return _elemental_level;
					}
					public void set_elemental_level(int val){
						_bit |= 0x00000040;
						_elemental_level = val;
					}
					public boolean has_elemental_level(){
						return (_bit & 0x00000040) == 0x00000040;
					}
					public String get_desc(){
						return _desc;
					}
					public void set_desc(String val){
						_bit |= 0x00000080;
						_desc = val;
					}
					public boolean has_desc(){
						return (_bit & 0x00000080) == 0x00000080;
					}
					public int get_system_desc(){
						return _system_desc;
					}
					public void set_system_desc(int val){
						_bit |= 0x00000100;
						_system_desc = val;
					}
					public boolean has_system_desc(){
						return (_bit & 0x00000100) == 0x00000100;
					}
					public int get_broadcast_desc(){
						return _broadcast_desc;
					}
					public void set_broadcast_desc(int val){
						_bit |= 0x00000200;
						_broadcast_desc = val;
					}
					public boolean has_broadcast_desc(){
						return (_bit & 0x00000200) == 0x00000200;
					}
					public int get_iconId(){
						return _iconId;
					}
					public void set_iconId(int val){
						_bit |= 0x00000400;
						_iconId = val;
					}
					public boolean has_iconId(){
						return (_bit & 0x00000400) == 0x00000400;
					}
					public String get_url(){
						return _url;
					}
					public void set_url(String val){
						_bit |= 0x00000800;
						_url = val;
					}
					public boolean has_url(){
						return (_bit & 0x00000800) == 0x00000800;
					}
					public byte[] get_extra_desc(){
						return _extra_desc;
					}
					public void set_extra_desc(byte[] val){
						_bit |= 0x00001000;
						_extra_desc = val;
					}
					public boolean has_extra_desc(){
						return (_bit & 0x00001000) == 0x00001000;
					}
					public int get_inherit_enchant_from(){
						return _inherit_enchant_from;
					}
					public void set_inherit_enchant_from(int val){
						_bit |= 0x00002000;
						_inherit_enchant_from = val;
					}
					public boolean has_inherit_enchant_from(){
						return (_bit & 0x00002000) == 0x00002000;
					}
					public int get_inherit_elemental_enchant_from(){
						return _inherit_elemental_enchant_from;
					}
					public void set_inherit_elemental_enchant_from(int val){
						_bit |= 0x00004000;
						_inherit_elemental_enchant_from = val;
					}
					public boolean has_inherit_elemental_enchant_from(){
						return (_bit & 0x00004000) == 0x00004000;
					}
					public int get_event_id(){
						return _event_id;
					}
					public void set_event_id(int val){
						_bit |= 0x00008000;
						_event_id = val;
					}
					public boolean has_event_id(){
						return (_bit & 0x00008000) == 0x00008000;
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
						if (has_name_id())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _name_id);
						if (has_count())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _count);
						if (has_slot())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _slot);
						if (has_enchant())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _enchant);
						if (has_bless())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _bless);
						if (has_elemental_type())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _elemental_type);
						if (has_elemental_level())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _elemental_level);
						if (has_desc())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(8, _desc);
						if (has_system_desc())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _system_desc);
						if (has_broadcast_desc())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _broadcast_desc);
						if (has_iconId())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _iconId);
						if (has_url())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(12, _url);
						if (has_extra_desc())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(13, _extra_desc);
						if (has_inherit_enchant_from())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(14, _inherit_enchant_from);
						if (has_inherit_elemental_enchant_from())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(15, _inherit_elemental_enchant_from);
						if (has_event_id())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(16, _event_id);
						_memorizedSerializedSize = size;
						return size;
					}
					@Override
					public boolean isInitialized(){
						if(_memorizedIsInitialized == 1)
							return true;
						if (!has_name_id()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_count()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_slot()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_enchant()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_bless()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_elemental_type()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_elemental_level()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_desc()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_broadcast_desc()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_iconId()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_url()){
							_memorizedIsInitialized = -1;
							return false;
						}
						_memorizedIsInitialized = 1;
						return true;
					}
					
					@Override
					public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
						l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
						try {
							writeTo(stream);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return stream;
					}
					
					@Override
					public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
						if (has_name_id()){
							output.wirteInt32(1, _name_id);
						}
						if (has_count()){
							output.wirteInt32(2, _count);
						}
						if (has_slot()){
							output.wirteInt32(3, _slot);
						}
						if (has_enchant()){
							output.wirteInt32(4, _enchant);
						}
						if (has_bless()){
							output.wirteInt32(5, _bless);
						}
						if (has_elemental_type()){
							output.wirteInt32(6, _elemental_type);
						}
						if (has_elemental_level()){
							output.wirteInt32(7, _elemental_level);
						}
						if (has_desc()){
							output.writeString(8, _desc);
						}
						if (has_system_desc()){
							output.wirteInt32(9, _system_desc);
						}
						if (has_broadcast_desc()){
							output.wirteInt32(10, _broadcast_desc);
						}
						if (has_iconId()){
							output.wirteInt32(11, _iconId);
						}
						if (has_url()){
							output.writeString(12, _url);
						}
						if (has_extra_desc()){
							output.writeBytes(13, _extra_desc);
						}
						if (has_inherit_enchant_from()){
							output.wirteInt32(14, _inherit_enchant_from);
						}
						if (has_inherit_elemental_enchant_from()){
							output.wirteInt32(15, _inherit_elemental_enchant_from);
						}
						if (has_event_id()){
							output.wirteInt32(16, _event_id);
						}
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
									set_count(input.readInt32());
									break;
								}
								case 0x00000018:{
									set_slot(input.readInt32());
									break;
								}
								case 0x00000020:{
									set_enchant(input.readInt32());
									break;
								}
								case 0x00000028:{
									set_bless(input.readInt32());
									break;
								}
								case 0x00000030:{
									set_elemental_type(input.readInt32());
									break;
								}
								case 0x00000038:{
									set_elemental_level(input.readInt32());
									break;
								}
								case 0x00000042:{
									set_desc(input.readString());
									break;
								}
								case 0x00000048:{
									set_system_desc(input.readInt32());
									break;
								}
								case 0x00000050:{
									set_broadcast_desc(input.readInt32());
									break;
								}
								case 0x00000058:{
									set_iconId(input.readInt32());
									break;
								}
								case 0x00000062:{
									set_url(input.readString());
									break;
								}
								case 0x0000006A:{
									set_extra_desc(input.readBytes());
									break;
								}
								case 0x00000070:{
									set_inherit_enchant_from(input.readInt32());
									break;
								}
								case 0x00000078:{
									set_inherit_elemental_enchant_from(input.readInt32());
									break;
								}
								case 0x00000080:{
									set_event_id(input.readInt32());
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
						return new CraftOutputItem();
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
					
					public SC_CRAFT_MAKE_ACK.CraftOutputItem toMakeOutputItem(int eType, int eValue){
						SC_CRAFT_MAKE_ACK.CraftOutputItem outputItem = SC_CRAFT_MAKE_ACK.CraftOutputItem.newInstance();
						outputItem.set_name_id(_name_id);
						outputItem.set_count(_count);
						outputItem.set_slot(-1);
						outputItem.set_enchant(_enchant);
						outputItem.set_bless(_bless);
						outputItem.set_elemental_type(eType);
						outputItem.set_elemental_level(eValue);
						outputItem.set_desc(_desc);
						outputItem.set_system_desc(_system_desc);
						outputItem.set_broadcast_desc(_broadcast_desc);
						outputItem.set_iconId(_iconId);
						outputItem.set_url(_url);
						return outputItem;
					}
				}
				public static class CraftEvent implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
					public static CraftEvent newInstance(){
						return new CraftEvent();
					}
					private int _event_id;
					private int _prob_count;
					private int _non_prob_count;
					private LinkedList<CraftOutputItem> _output_prob_items;
					private LinkedList<CraftOutputItem> _output_items;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;
					private CraftEvent(){
					}
					public int get_event_id(){
						return _event_id;
					}
					public void set_event_id(int val){
						_bit |= 0x00000001;
						_event_id = val;
					}
					public boolean has_event_id(){
						return (_bit & 0x00000001) == 0x00000001;
					}
					public int get_prob_count(){
						return _prob_count;
					}
					public void set_prob_count(int val){
						_bit |= 0x00000002;
						_prob_count = val;
					}
					public boolean has_prob_count(){
						return (_bit & 0x00000002) == 0x00000002;
					}
					public int get_non_prob_count(){
						return _non_prob_count;
					}
					public void set_non_prob_count(int val){
						_bit |= 0x00000004;
						_non_prob_count = val;
					}
					public boolean has_non_prob_count(){
						return (_bit & 0x00000004) == 0x00000004;
					}
					public LinkedList<CraftOutputItem> get_output_prob_items(){
						return _output_prob_items;
					}
					public void add_output_prob_items(CraftOutputItem val){
						if(!has_output_prob_items()){
							_output_prob_items = new LinkedList<CraftOutputItem>();
							_bit |= 0x00000008;
						}
						_output_prob_items.add(val);
					}
					public boolean has_output_prob_items(){
						return (_bit & 0x00000008) == 0x00000008;
					}
					public LinkedList<CraftOutputItem> get_output_items(){
						return _output_items;
					}
					public void add_output_items(CraftOutputItem val){
						if(!has_output_items()){
							_output_items = new LinkedList<CraftOutputItem>();
							_bit |= 0x00000010;
						}
						_output_items.add(val);
					}
					public boolean has_output_items(){
						return (_bit & 0x00000010) == 0x00000010;
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
						if (has_event_id())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _event_id);
						if (has_prob_count())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _prob_count);
						if (has_non_prob_count())
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _non_prob_count);
						if (has_output_prob_items()){
							for(CraftOutputItem val : _output_prob_items)
								size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
						}
						if (has_output_items()){
							for(CraftOutputItem val : _output_items)
								size +=l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
						}
						_memorizedSerializedSize = size;
						return size;
					}
					@Override
					public boolean isInitialized(){
						if(_memorizedIsInitialized == 1)
							return true;
						if (!has_event_id()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_prob_count()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_non_prob_count()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (has_output_prob_items()){
							for(CraftOutputItem val : _output_prob_items){
								if (!val.isInitialized()){
									_memorizedIsInitialized = -1;
									return false;
								}
							}
						}
						if (has_output_items()){
							for(CraftOutputItem val : _output_items){
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
					public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
						l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
						try {
							writeTo(stream);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return stream;
					}
					
					@Override
					public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
						if (has_event_id()){
							output.wirteInt32(1, _event_id);
						}
						if (has_prob_count()){
							output.wirteInt32(2, _prob_count);
						}
						if (has_non_prob_count()){
							output.wirteInt32(3, _non_prob_count);
						}
						if (has_output_prob_items()){
							for(CraftOutputItem val : _output_prob_items){
								output.writeMessage(4, val);
							}
						}
						if (has_output_items()){
							for(CraftOutputItem val : _output_items){
								output.writeMessage(5, val);
							}
						}
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
									set_event_id(input.readInt32());
									break;
								}
								case 0x00000010:{
									set_prob_count(input.readInt32());
									break;
								}
								case 0x00000018:{
									set_non_prob_count(input.readInt32());
									break;
								}
								case 0x00000022:{
									add_output_prob_items((CraftOutputItem)input.readMessage(CraftOutputItem.newInstance()));
									break;
								}
								case 0x0000002A:{
									add_output_items((CraftOutputItem)input.readMessage(CraftOutputItem.newInstance()));
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
						return new CraftEvent();
					}
					@Override
					public MJIProtoMessage reloadInstance(){
						return newInstance();
					}
					@Override
					public void dispose(){
						if (has_output_prob_items()){
							for(CraftOutputItem val : _output_prob_items)
								val.dispose();
							_output_prob_items.clear();
							_output_prob_items = null;
						}
						if (has_output_items()){
							for(CraftOutputItem val : _output_items)
								val.dispose();
							_output_items.clear();
							_output_items = null;
						}
						_bit = 0;
						_memorizedIsInitialized = -1;
					}
				}
			}
		}
		public static class CraftAttr implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
			public static CraftAttr newInstance(){
				return new CraftAttr();
			}
			private int _desc;
			private int _min_level;
			private int _max_level;
			private int _required_gender;
			private int _min_align;
			private int _max_align;
			private int _min_karma;
			private int _max_karma;
			private int _max_count;
			private boolean _show;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CraftAttr(){
				_show = false;
			}
			public int get_desc(){
				return _desc;
			}
			public void set_desc(int val){
				_bit |= 0x00000001;
				_desc = val;
			}
			public boolean has_desc(){
				return (_bit & 0x00000001) == 0x00000001;
			}
			public int get_min_level(){
				return _min_level;
			}
			public void set_min_level(int val){
				_bit |= 0x00000002;
				_min_level = val;
			}
			public boolean has_min_level(){
				return (_bit & 0x00000002) == 0x00000002;
			}
			public int get_max_level(){
				return _max_level;
			}
			public void set_max_level(int val){
				_bit |= 0x00000004;
				_max_level = val;
			}
			public boolean has_max_level(){
				return (_bit & 0x00000004) == 0x00000004;
			}
			public int get_required_gender(){
				return _required_gender;
			}
			public void set_required_gender(int val){
				_bit |= 0x00000008;
				_required_gender = val;
			}
			public boolean has_required_gender(){
				return (_bit & 0x00000008) == 0x00000008;
			}
			public int get_min_align(){
				return _min_align;
			}
			public void set_min_align(int val){
				_bit |= 0x00000010;
				_min_align = val;
			}
			public boolean has_min_align(){
				return (_bit & 0x00000010) == 0x00000010;
			}
			public int get_max_align(){
				return _max_align;
			}
			public void set_max_align(int val){
				_bit |= 0x00000020;
				_max_align = val;
			}
			public boolean has_max_align(){
				return (_bit & 0x00000020) == 0x00000020;
			}
			public int get_min_karma(){
				return _min_karma;
			}
			public void set_min_karma(int val){
				_bit |= 0x00000040;
				_min_karma = val;
			}
			public boolean has_min_karma(){
				return (_bit & 0x00000040) == 0x00000040;
			}
			public int get_max_karma(){
				return _max_karma;
			}
			public void set_max_karma(int val){
				_bit |= 0x00000080;
				_max_karma = val;
			}
			public boolean has_max_karma(){
				return (_bit & 0x00000080) == 0x00000080;
			}
			public int get_max_count(){
				return _max_count;
			}
			public void set_max_count(int val){
				_bit |= 0x00000100;
				_max_count = val;
			}
			public boolean has_max_count(){
				return (_bit & 0x00000100) == 0x00000100;
			}
			public boolean get_show(){
				return _show;
			}
			public void set_show(boolean val){
				_bit |= 0x00000200;
				_show = val;
			}
			public boolean has_show(){
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
				if (has_desc())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _desc);
				if (has_min_level())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _min_level);
				if (has_max_level())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _max_level);
				if (has_required_gender())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _required_gender);
				if (has_min_align())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _min_align);
				if (has_max_align())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _max_align);
				if (has_min_karma())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _min_karma);
				if (has_max_karma())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _max_karma);
				if (has_max_count())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _max_count);
				if (has_show())
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(10, _show);
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_desc()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_min_level()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_max_level()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_required_gender()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_min_align()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_max_align()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_min_karma()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_max_karma()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_max_count()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			
			@Override
			public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
				try {
					writeTo(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return stream;
			}
			
			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
				if (has_desc()){
					output.wirteInt32(1, _desc);
				}
				if (has_min_level()){
					output.wirteInt32(2, _min_level);
				}
				if (has_max_level()){
					output.wirteInt32(3, _max_level);
				}
				if (has_required_gender()){
					output.wirteInt32(4, _required_gender);
				}
				if (has_min_align()){
					output.wirteInt32(5, _min_align);
				}
				if (has_max_align()){
					output.wirteInt32(6, _max_align);
				}
				if (has_min_karma()){
					output.wirteInt32(7, _min_karma);
				}
				if (has_max_karma()){
					output.wirteInt32(8, _max_karma);
				}
				if (has_max_count()){
					output.wirteInt32(9, _max_count);
				}
				if (has_show()){
					output.writeBool(10, _show);
				}
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
							set_desc(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_min_level(input.readInt32());
							break;
						}
						case 0x00000018:{
							set_max_level(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_required_gender(input.readInt32());
							break;
						}
						case 0x00000028:{
							set_min_align(input.readInt32());
							break;
						}
						case 0x00000030:{
							set_max_align(input.readInt32());
							break;
						}
						case 0x00000038:{
							set_min_karma(input.readInt32());
							break;
						}
						case 0x00000040:{
							set_max_karma(input.readInt32());
							break;
						}
						case 0x00000048:{
							set_max_count(input.readInt32());
							break;
						}
						case 0x00000050:{
							set_show(input.readBool());
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
				return new CraftAttr();
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
	
	public static class CraftOutputItemResult {
		public CraftOutputItemResult(CraftOutputItem outputItem, boolean isSuccess){
			this.outputItem = outputItem;
			this.isSuccess = isSuccess;
		}
		public CraftOutputItem outputItem;
		public boolean isSuccess;
	}
	
	public enum eCraftListAllReqResultType{
		RP_CRAFT_LOAD_START(0),
		RP_CRAFT_LOADING(1),
		RP_CRAFT_LOAD_FINISH(2),
		RP_ERROR_SAME_HASH_VAL(3),
		RP_ERROR_INVALID_HASH_VAL(4),
		RP_ERROR_UNKNOWN(9999);
		private int value;
		eCraftListAllReqResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCraftListAllReqResultType v){
			return value == v.value;
		}
		public static eCraftListAllReqResultType fromInt(int i){
			switch(i){
			case 0:
				return RP_CRAFT_LOAD_START;
			case 1:
				return RP_CRAFT_LOADING;
			case 2:
				return RP_CRAFT_LOAD_FINISH;
			case 3:
				return RP_ERROR_SAME_HASH_VAL;
			case 4:
				return RP_ERROR_INVALID_HASH_VAL;
			case 9999:
				return RP_ERROR_UNKNOWN;
			default:
				return null;
			}
		}
	}
}
