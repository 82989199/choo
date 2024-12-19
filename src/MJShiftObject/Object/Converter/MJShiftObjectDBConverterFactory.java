package MJShiftObject.Object.Converter;

import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.DB.Helper.MJShiftSelector;
import MJShiftObject.DB.Helper.MJShiftUpdator;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Object.Converter.Selector.MJShiftObjectSelector;
import MJShiftObject.Object.Converter.Updator.MJShiftObjectUpdator;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftObjectDBConverterFactory {
	
	public static IMJShiftObjectDBConverter create_sender(String server_identity){
		return new MJShiftObjectDBSender(server_identity);
	}
	
	public static IMJShiftObjectDBConverter create_receiver(String server_identity){
		return new MJShiftObjectDBReceiver(server_identity);
	}
	
	private static class MJShiftObjectDBSender implements IMJShiftObjectDBConverter{
		private MJShiftObjectUpdator m_updator;
		private MJShiftObjectSelector m_selector;
		
		MJShiftObjectDBSender(String server_identity){
			// 공용 db로 설정
			m_updator = MJShiftObjectUpdator.newInstance(server_identity, new MJShiftUpdator());
			
			// 내 db로 설정
			m_selector = MJShiftObjectSelector.newInstance("", new Selector());		
		}
		
		public int work(final MJShiftObject sobject){
			try{
				
				final int object_id = sobject.get_source_id();
				final int update_id = sobject.get_source_id();
				final L1PcInstance pc = m_selector.select_character_info(object_id);
				if(pc == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_PC;
				
				final Account account = m_selector.select_accounts(pc.getAccountName());
				if(account == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_ACCOUNT;
				
				m_updator.update_character_info(pc, update_id);
				m_updator.update_accounts(account);m_updator.update_character_buff(m_selector.select_character_buff(object_id), update_id);
				m_updator.update_character_config(m_selector.select_character_config(object_id), update_id);
				m_updator.update_character_items(m_selector.select_character_items(object_id), update_id);
				/*
				if(sobject.get_shift_type().equals(MJEShiftObjectType.TRANSFER)){					
					m_updator.update_character_items(m_selector.select_character_items(object_id), update_id);
				}else{
					m_updator.update_character_items(m_selector.select_character_items_euqipped(object_id), update_id);
				}*/
				m_updator.update_character_skills(m_selector.select_character_skills(object_id), update_id);
				m_updator.update_character_passive(m_selector.select_character_passive(object_id), update_id);
				m_updator.update_character_quest_info(m_selector.select_character_quest_info(object_id), update_id);
				m_updator.update_character_level_bonus(m_selector.select_character_level_bonus(object_id), update_id);
				MJShiftObjectHelper.update_shift_object(sobject, m_updator.get_server_identity());
			return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
			}
			return IMJShiftObjectDBConverter.CONVERT_FAIL_INVALID;
		}
	}
	
	
	private static class MJShiftObjectDBReceiver implements IMJShiftObjectDBConverter{
		private MJShiftObjectUpdator m_updator;
		private MJShiftObjectSelector m_selector;
		public MJShiftObjectDBReceiver(String server_identity){
			// 내 db로 설정
			m_updator = MJShiftObjectUpdator.newInstance("", new Updator());
			
			// 공용 db로 설정
			m_selector = MJShiftObjectSelector.newInstance(server_identity, new MJShiftSelector());		
		}
		
		public int work(final MJShiftObject sobject){
			try{
				final int object_id = sobject.get_source_id();
				final int update_id = sobject.get_destination_id();
				final L1PcInstance pc = m_selector.select_character_info(object_id);
				if(pc == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_PC;
				
				final Account account = m_selector.select_accounts(pc.getAccountName());
				if(account == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_ACCOUNT;
				
				// 내 서버 정보로 셋한다.
				pc.setId(update_id);
				pc.setName(sobject.get_destination_character_name());
				pc.setAccountName(sobject.get_destination_account_name());
				account.setName(sobject.get_destination_account_name());
				
				m_updator.delete_character_info(sobject.get_destination_character_name());
				m_updator.update_character_info(pc, update_id);
				m_updator.delete_accounts(sobject.get_destination_account_name());
				m_updator.update_accounts(account);
				m_updator.update_character_buff(m_selector.select_character_buff(object_id), update_id);
				m_updator.update_character_config(m_selector.select_character_config(object_id), update_id);
				m_updator.update_character_items(m_selector.select_character_items(object_id), update_id);
				m_updator.update_character_skills(m_selector.select_character_skills(object_id), update_id);
				m_updator.update_character_passive(m_selector.select_character_passive(object_id), update_id);
				m_updator.update_character_quest_info(m_selector.select_character_quest_info(object_id), update_id);
				m_updator.update_character_level_bonus(m_selector.select_character_level_bonus(object_id), update_id);
				MJShiftObjectHelper.update_shift_object(sobject, m_selector.get_server_identity());
			return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
			}
			return IMJShiftObjectDBConverter.CONVERT_FAIL_INVALID;
		}
	}
	
	/*
	private static class MJShiftObjectDBGetBacker implements IMJShiftObjectDBConverter {
		private MJShiftObjectUpdator m_updator;
		private MJShiftObjectSelector m_selector;
		
		public MJShiftObjectDBGetBacker(String server_identity){
			// 내 db로 설정
			m_updator = MJShiftObjectUpdator.newInstance("", new Updator());
			
			// 공용 db로 설정
			m_selector = MJShiftObjectSelector.newInstance(server_identity, new MJShiftSelector());		
		}
		
		public int work(final MJShiftObject sobject){
			try{
				final int object_id = sobject.get_source_id();
				final int update_id = sobject.get_source_id();
				final L1PcInstance pc = m_selector.select_character_info(object_id);
				if(pc == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_PC;
				
				final Account account = m_selector.select_accounts(pc.getAccountName());
				if(account == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_ACCOUNT;
				
				m_updator.update_character_info(pc, update_id);
				m_updator.update_accounts_only(account);
				if(sobject.get_shift_type().equals(MJEShiftObjectType.TRANSFER)){
					m_updator.update_character_buff(m_selector.select_character_buff(object_id), update_id);
					m_updator.update_character_config(m_selector.select_character_config(object_id), update_id);
					m_updator.update_character_items(m_selector.select_character_items(object_id), update_id);
					m_updator.update_character_skills(m_selector.select_character_skills(object_id), update_id);
					m_updator.update_character_passive(m_selector.select_character_passive(object_id), update_id);
					m_updator.update_character_quest_info(m_selector.select_character_quest_info(object_id), update_id);
					//m_updator.update_character_slot_items(m_selector.select_character_slot(object_id), update_id);
					//m_updator.update_character_tams(m_selector.select_character_tams(object_id, account.getAccountId()), update_id);
					m_updator.update_character_level_bonus(m_selector.select_character_level_bonus(object_id), update_id);
				}
			return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
			}
			return IMJShiftObjectDBConverter.CONVERT_FAIL_INVALID;
		}
	}
	

	private static class MJShiftObjectDBReturner implements IMJShiftObjectDBConverter{
		private MJShiftObjectUpdator m_updator;
		private MJShiftObjectSelector m_selector;
		
		public MJShiftObjectDBReturner(String server_identity){
			// 공용 db로 설정
			m_updator = MJShiftObjectUpdator.newInstance(server_identity, new MJShiftUpdator());
			
			// 내 db로 설정
			m_selector = MJShiftObjectSelector.newInstance("", new Selector());		
		}
		
		public int work(final MJShiftObject sobject){
			try{
				final int object_id = sobject.get_destination_id();
				final int update_id = sobject.get_source_id();
				final L1PcInstance pc = m_selector.select_character_info(object_id);
				if(pc == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_PC;
				
				final Account account = m_selector.select_accounts(pc.getAccountName());
				if(account == null)
					return IMJShiftObjectDBConverter.CONVERT_FAIL_NOT_FOUND_ACCOUNT;
				
				// 원본 서버 정보로 셋한다.
				pc.setId(update_id);
				pc.setName(sobject.get_source_character_name());
				pc.setAccountName(sobject.get_source_account_name());
				account.setName(sobject.get_source_account_name());
							
				m_updator.update_character_info(pc, update_id);
				m_updator.update_accounts(account);
				m_updator.update_character_buff(m_selector.select_character_buff(object_id), update_id);
				
				m_updator.update_character_config(m_selector.select_character_config(object_id), update_id);
				m_updator.update_character_items(m_selector.select_character_items(object_id), update_id);
				m_updator.update_character_skills(m_selector.select_character_skills(object_id), update_id);
				m_updator.update_character_passive(m_selector.select_character_passive(object_id), update_id);
				m_updator.update_character_quest_info(m_selector.select_character_quest_info(object_id), update_id);
				m_updator.update_character_level_bonus(m_selector.select_character_level_bonus(object_id), update_id);
			return IMJShiftObjectDBConverter.CONVERT_SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
			}
			return IMJShiftObjectDBConverter.CONVERT_FAIL_INVALID;
		}
	}*/
}
