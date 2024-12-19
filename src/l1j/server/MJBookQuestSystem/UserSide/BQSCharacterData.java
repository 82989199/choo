package l1j.server.MJBookQuestSystem.UserSide;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJBookQuestSystem.Loader.BQSCharacterDataLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
import l1j.server.MJBookQuestSystem.Loader.BQSWQDecksLoader;
import l1j.server.MJBookQuestSystem.Loader.BQSWQRewardsLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eMonsterBookV2DeckState;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CompletedAchievement;
import l1j.server.MJTemplate.MJProto.MainServer_Client.MonsterBookV2Info;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ACHIEVEMENT_COMPLETE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_COMPLETED_ACHIEVEMENT_BATCH;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_CRITERIA_PROGRESS_BATCH;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRITERIA_UPDATE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class BQSCharacterData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static BQSCharacterData newInstance(ResultSet rs) throws Exception{
		BQSCharacterData bqsData = newInstance();
		bqsData.readFrom(ProtoInputStream.newInstance(rs.getBytes("character_data")));
		return bqsData;
	}
	
	public static BQSCharacterData newInstance(){
		return new BQSCharacterData();
	}
	
	private int _object_id;
	private HashMap<Integer, BQSCharacterCriteriaProgress> _progresses;
	private HashMap<Integer, CompletedAchievement> _achievements;
	private ArrayList<MonsterBookV2Info.DeckT> _decks;
	private long _decks_version;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private BQSCharacterData(){}
	
	public void onUpdate(BQSInformation bqsInfo){
		if(bqsInfo == null)
			return;
		
		onProgress(bqsInfo);
		onDeck(bqsInfo);
	}
	
	private void onProgress(BQSInformation bqsInfo){
		int criteria_id = bqsInfo.getCriteriaId();
		BQSCharacterCriteriaProgress progress = get_progress(criteria_id);
		if(progress == null){
			progress = BQSCharacterCriteriaProgress.newInstance(bqsInfo);
			add_progresses(progress);
		}else if(progress.get_current_ahcievement_level() >= 3)
			return;
		
		L1PcInstance pc = (L1PcInstance) L1World.getInstance().findObject(_object_id);
		boolean isComplete = progress.onUpdateIsComplete(bqsInfo);
		if(pc != null)
			pc.sendPackets(SC_CRITERIA_UPDATE_NOTI.newInstance(progress.get_progress()), MJEProtoMessages.SC_CRITERIA_UPDATE_NOTI, true);
		if(isComplete){
			CompletedAchievement achievement = CompletedAchievement.newInstance(bqsInfo.getAchievementId() + progress.get_current_ahcievement_level());
			add_achievements(achievement);
			pc.sendPackets(SC_ACHIEVEMENT_COMPLETE_NOTI.newInstance(achievement), MJEProtoMessages.SC_ACHIEVEMENT_COMPLETE_NOTI, true);
			BQSCharacterDataLoader.storeCharacterBqs(pc.getBqs(), false);
		}
	}
	
	private void onDeck(BQSInformation bqsInfo){
		int difficulty = bqsInfo.getWeekDifficulty();
		if(difficulty < 0 || difficulty > 2)
			return;
		
		MonsterBookV2Info.DeckT deck = get_deck(difficulty);
		if(deck == null)
			return;
		
		L1PcInstance pc = (L1PcInstance)L1World.getInstance().findObject(_object_id);
		if(pc == null)
			return;
		
		if(deck.onUpdateIsComplete(pc, bqsInfo.getCriteriaId())){
			deck.set_state(eMonsterBookV2DeckState.DS_COMPLETED);
			SC_MONSTER_BOOK_V2_UPDATE_STATE_NOTI.send(pc, deck);
			BQSCharacterDataLoader.storeCharacterBqs(pc.getBqs(), false);
		}
	}
	
	public BQSCharacterData send_decks_noti(L1PcInstance pc){
		SC_MONSTER_BOOK_V2_INFO_NOTI noti = SC_MONSTER_BOOK_V2_INFO_NOTI.newInstance();
		MonsterBookV2Info v2Info = MonsterBookV2Info.newInstance();
		v2Info.set_system(BQSWQRewardsLoader.getInstance().get());
		v2Info.set_decks(_decks);
		noti.set_info(v2Info);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_MONSTER_BOOK_V2_INFO_NOTI);
		noti.dispose();
		pc.sendPackets(stream, true);
		return this;
	}
	
	public BQSCharacterData send_bqs_batch(L1PcInstance pc){
		SC_ADD_COMPLETED_ACHIEVEMENT_BATCH achievement = SC_ADD_COMPLETED_ACHIEVEMENT_BATCH.newInstance();
		SC_ADD_CRITERIA_PROGRESS_BATCH criteria = SC_ADD_CRITERIA_PROGRESS_BATCH.newInstance();
		achievement.set_current_page(0);
		achievement.set_total_pages(0);		
		criteria.set_current_page(0);
		criteria.set_total_pages(0);
		
		if(has_progresses()){
			for(BQSCharacterCriteriaProgress val : get_progresses().values())
				criteria.add_criteria_progress(val.get_progress());
		}
		if(has_achievements()){
			for(CompletedAchievement val : get_achievements().values())
				achievement.add_completed_achievmenet(val);
		}
		pc.sendPackets(achievement, MJEProtoMessages.SC_ADD_COMPLETED_ACHIEVEMENT_BATCH, true);
		pc.sendPackets(criteria, MJEProtoMessages.SC_ADD_CRITERIA_PROGRESS_BATCH, true);
		return this;
	}
	
	public int get_object_id(){
		return _object_id;
	}
	public void set_object_id(int id){
		_bit |= 0x01;
		_object_id = id;
	}
	public boolean has_object_id(){
		return (_bit & 0x01) == 0x01;
	}
	public BQSCharacterCriteriaProgress get_progress(int criteria_id){
		return has_progresses() ? _progresses.get(criteria_id) : null;
	}
	public HashMap<Integer, BQSCharacterCriteriaProgress> get_progresses(){
		return _progresses;
	}
	public void add_progresses(BQSCharacterCriteriaProgress progress){
		if(!has_progresses()){
			_progresses = new HashMap<Integer, BQSCharacterCriteriaProgress>(256);
			_bit |= 0x02;
		}
		_progresses.put(progress.get_criteria_id(), progress);
	}
	public boolean has_progresses(){
		return (_bit & 0x02) == 0x02;
	}
	public HashMap<Integer, CompletedAchievement> get_achievements(){
		return _achievements;
	}
	public CompletedAchievement get_achievement(int achievement_id){
		return has_achievements() ? _achievements.get(achievement_id) : null;
	}
	public void add_achievements(CompletedAchievement achievement){
		if(!has_achievements()){
			_achievements = new HashMap<Integer, CompletedAchievement>(32);
			_bit |= 0x04;
		}
		_achievements.put(achievement.get_achievement_id(), achievement);
	}
	public boolean has_achievements(){
		return (_bit & 0x04) == 0x04;
	}
	public MonsterBookV2Info.DeckT get_deck(int difficulty){
		return has_decks() ? _decks.get(difficulty) : null;
	}
	public ArrayList<MonsterBookV2Info.DeckT> get_decks(){
		return _decks;
	}
	public BQSCharacterData realloc_decks(L1PcInstance pc){
		if(!has_decks_version() || Math.abs(BQSLoadManager.BQS_UPDATE_CALENDAR.getTimeInMillis() - get_decks_version()) > 1000L){
			drain_decks(BQSWQDecksLoader.getInstance().getDecks(), pc.getLevel());
			set_decks_version(BQSLoadManager.BQS_UPDATE_CALENDAR.getTimeInMillis());
			BQSCharacterDataLoader.storeCharacterBqs(this, false);
		}
		return this;
	}
	public void drain_decks(ArrayList<MonsterBookV2Info.DeckT> decks, int level){
		ArrayList<MonsterBookV2Info.DeckT> replace_decks = new ArrayList<MonsterBookV2Info.DeckT>(BQSLoadManager.BQS_WQ_HEIGHT);
		for(MonsterBookV2Info.DeckT deck : decks)
			replace_decks.add(deck.deepCopy(level));
		set_decks(replace_decks);
		
	}
	public void set_decks(ArrayList<MonsterBookV2Info.DeckT> decks){
		ArrayList<MonsterBookV2Info.DeckT> old = _decks;
		_bit |= 0x08;
		_decks = decks;
		if(old != null){
			for(MonsterBookV2Info.DeckT deck : old)
				deck.dispose();
			old.clear();
		}
	}
	public void add_decks(MonsterBookV2Info.DeckT deck){
		if(!has_decks()){
			_decks = new ArrayList<MonsterBookV2Info.DeckT>(BQSLoadManager.BQS_WQ_HEIGHT);
			_bit |= 0x08;
		}
		_decks.add(deck);
	}
	public boolean has_decks(){
		return (_bit & 0x08) == 0x08;
	}
	public long get_decks_version(){
		return _decks_version;
	}
	public BQSCharacterData set_decks_version(long val){
		_bit |= 0x10;
		_decks_version = val;
		return this;
	}
	public boolean has_decks_version(){
		return (_bit & 0x10) == 0x10;
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
		if(has_object_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _object_id);
		}
		if(has_progresses()){
			for(BQSCharacterCriteriaProgress progress : _progresses.values())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, progress);
		}
		if(has_achievements()){
			for(CompletedAchievement achievement : _achievements.values())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, achievement);
		}
		if(has_decks()){
			for(MonsterBookV2Info.DeckT deck : _decks)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, deck);
		}
		if(has_decks_version()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(5, _decks_version);
		}
			
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		
		if(!has_object_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		
		if(has_progresses()){
			for(BQSCharacterCriteriaProgress progress : _progresses.values()){
				if(!progress.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}				
		}
		if(has_achievements()){
			for(CompletedAchievement achievement : _achievements.values()){
				if(!achievement.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if(has_decks()){
			for(MonsterBookV2Info.DeckT deck : _decks){
				if(!deck.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if(!has_decks_version()){
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
		if (has_object_id()){
			output.writeUInt32(1, _object_id);
		}
		if (has_progresses()){
			for(BQSCharacterCriteriaProgress val : _progresses.values()){
				output.writeMessage(2, val);
			}
		}
		if(has_achievements()){
			for(CompletedAchievement val : _achievements.values()){
				output.writeMessage(3, val);
			}
		}
		if(has_decks()){
			for(MonsterBookV2Info.DeckT val : _decks){
				output.writeMessage(4, val);
			}
		}
		if(has_decks_version()){
			output.writeInt64(5, _decks_version);
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
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000012:{
					add_progresses((BQSCharacterCriteriaProgress)input.readMessage(BQSCharacterCriteriaProgress.newInstance()));
					break;
				}
				case 0x0000001A:{
					add_achievements((CompletedAchievement)input.readMessage(CompletedAchievement.newInstance()));
					break;
				}
				case 0x00000022:{
					add_decks((MonsterBookV2Info.DeckT)input.readMessage(MonsterBookV2Info.DeckT.newInstance()));
					break;
				}
				case 0x00000028:{
					set_decks_version(input.readInt64());
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
		return new BQSCharacterData();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if(has_progresses()){
			for(BQSCharacterCriteriaProgress val : _progresses.values())
				val.dispose();
			_progresses.clear();
			_progresses = null;
		}
		if(has_achievements()){
			for(CompletedAchievement val : _achievements.values())
				val.dispose();
			_achievements.clear();
			_achievements = null;
		}
		if(has_decks()){
			for(MonsterBookV2Info.DeckT val : _decks)
				val.dispose();
			_decks.clear();
			_decks = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
