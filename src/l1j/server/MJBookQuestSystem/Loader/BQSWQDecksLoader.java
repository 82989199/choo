package l1j.server.MJBookQuestSystem.Loader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJTemplate.MJProto.MainServer_Client.MonsterBookV2Info;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class BQSWQDecksLoader {
	private static BQSWQDecksLoader _instance;
	public static BQSWQDecksLoader getInstance(){
		if(_instance == null)
			_instance = new BQSWQDecksLoader();
		return _instance;
	}
	
	public static void reload(){
		BQSWQDecksLoader old = _instance;
		_instance = new BQSWQDecksLoader();
		if(old != null){
			old.dispose();
			old = null;
		}
	}
	
	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}
	
	public static void truncate(){
		Updator.exec("truncate table tb_mbook_wq_decks", new Handler(){
			public void handle(PreparedStatement pstm) throws Exception{
			}
		});
	}
	
	private ArrayList<MonsterBookV2Info.DeckT> _decks;
	private BQSWQDecksLoader(){
		_decks = loadDecks();
	}
	
	public void updateDecks(){
		ArrayList<MonsterBookV2Info.DeckT> old = _decks;
		_decks = createDecks();
		storeDecks();
		if(old != null){
			for(MonsterBookV2Info.DeckT deck : old)
				deck.dispose();
			old.clear();
			old = null;
		}
		notifyUpdated();
	}
	
	public int notifyUpdated(){
		S_SystemMessage message = new S_SystemMessage("주간퀘스트가 갱신되었습니다.");
		int amount = 0;
		try{
			for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
				if(pc == null)
					continue;
				
				BQSCharacterData bqs = pc.getBqs();
				if(bqs == null)
					continue;
				
				++amount;
				bqs
				.set_decks_version(0L)
				.realloc_decks(pc)
				.send_decks_noti(pc);
				pc.sendPackets(message, false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			message.clear();
		}
		return amount;
	}
	
	public ArrayList<MonsterBookV2Info.DeckT> createDecks(){
		ArrayList<MonsterBookV2Info.DeckT> decks = createEmptyDecks();
		for(int i=0; i<BQSLoadManager.BQS_WQ_HEIGHT; ++i)
			decks.add(MonsterBookV2Info.DeckT.newInstance(i));
		return decks;
	}
	
	private ArrayList<MonsterBookV2Info.DeckT> createEmptyDecks(){
		return new ArrayList<MonsterBookV2Info.DeckT>(BQSLoadManager.BQS_WQ_HEIGHT);
	}
	
	public void storeDecks(){
		Updator.batch("insert into tb_mbook_wq_decks set difficulty=?, col1=?, col2=?, col3=? on duplicate key update col1=?, col2=?, col3=?", new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception{
				int idx = 0;
				pstm.setInt(++idx, callNumber);
				MonsterBookV2Info.DeckT deck = _decks.get(callNumber);
				for(MonsterBookV2Info.DeckT.CardT card : deck.get_cards())
					pstm.setInt(++idx, card.get_criteria_id());
				for(MonsterBookV2Info.DeckT.CardT card : deck.get_cards())
					pstm.setInt(++idx, card.get_criteria_id());
			}
		}, BQSLoadManager.BQS_WQ_HEIGHT);
	}
	
	public ArrayList<MonsterBookV2Info.DeckT> loadDecks(){
		ArrayList<MonsterBookV2Info.DeckT> decks = createEmptyDecks();
		Selector.exec("select * from tb_mbook_wq_decks order by difficulty asc", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next())
					decks.add(MonsterBookV2Info.DeckT.newInstance(rs));
			}
		});
		return decks;
	}
	
	public ArrayList<MonsterBookV2Info.DeckT> getDecks(){
		return _decks;
	}
	
	public MonsterBookV2Info.DeckT.CardT findDeck(int deck_index, int card_index){
		return _decks.get(deck_index).get_card(card_index);
	}
	
	public void dispose(){
		if(_decks != null){
			for(MonsterBookV2Info.DeckT deck : _decks)
				deck.dispose();
			_decks.clear();
			_decks = null;
		}
	}
}
