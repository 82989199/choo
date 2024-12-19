package l1j.server.MJBookQuestSystem.Loader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.MonsterBookV2Info;
import l1j.server.MJTemplate.MJProto.MainServer_Client.MonsterBookV2Info.DeckT.CardT;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class BQSCharacterDataLoader {
	public static void in(L1PcInstance pc){
		loadCharacterBqs(pc)
		.realloc_decks(pc)
		.send_bqs_batch(pc)
		.send_decks_noti(pc);
		
		
		/*BQSCharacterData bqs = pc.getBqs();
		for(MonsterBookV2Info.DeckT deck : bqs.get_decks()){
			System.out.println(deck.get_index());
			for(CardT card : deck.get_cards()){
				System.out.println(card.get_index() + " " + card.get_criteria_id() + " " + card.get_amount() + " " + card.get_required_amount() + " " + card.get_achievement_id());
			}
		}*/
	}
	
	public static void out(L1PcInstance pc){
		BQSCharacterData bqs = pc.getBqs();
		if(bqs == null)
			return;
		
		storeCharacterBqs(bqs, true);
		pc.setBqs(null);
	}
	
	public static BQSCharacterData loadCharacterBqs(L1PcInstance pc){
		Selector.exec("select * from tb_mbook_characterInfo where character_id=?", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					pc.setBqs(BQSCharacterData.newInstance(rs));
				}else{
					BQSCharacterData bqs = BQSCharacterData.newInstance();
					bqs.set_object_id(pc.getId());
					pc.setBqs(bqs);
				}
			}
		});
		return pc.getBqs();
	}
	
	public static void storeCharacterBqs(BQSCharacterData bqs, boolean isDisposeBqs){
		try{
			ProtoOutputStream stream = ProtoOutputStream.newLocalInstance(bqs.getSerializedSize());
			bqs.writeTo(stream);
			final byte[] b = stream.getBytes();
			
			Updator.exec("insert into tb_mbook_characterInfo set character_id=?, character_data=? on duplicate key update character_data=?", new Handler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, bqs.get_object_id());
					pstm.setBytes(++idx, b);
					pstm.setBytes(++idx, b);
				}
			});
			if(isDisposeBqs)
				bqs.dispose();
			stream.dispose();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void deleteCharactersBps(Collection<L1PcInstance> col){
		Updator.exec("truncate table tb_mbook_characterInfo", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {}
		});
		
		for(L1PcInstance pc : col){
			if(pc == null)
				continue;
			BQSCharacterData bqs = pc.getBqs();
			if(bqs == null)
				continue;
			
			bqs.dispose();
			pc.setBqs(null);
		}
	}
	
	public static void deleteCharacterBps(L1PcInstance pc, boolean isDisposeBps){
		deleteCharacterBps(pc.getId());
		if(isDisposeBps){
			BQSCharacterData bqs = pc.getBqs();
			if(bqs != null){
				bqs.dispose();
				pc.setBqs(null);
			}
		}
	}
	
	public static void deleteCharacterBps(int object_id){
		Updator.exec("delete from tb_mbook_characterInfo where character_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, object_id);
			}
		});
	}
}
