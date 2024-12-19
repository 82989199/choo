package MJShiftObject.Object.Converter.Updator;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

import MJShiftObject.DB.MJEShiftDBName;
import MJShiftObject.Template.CharacterBuffInfo;
import MJShiftObject.Template.CharacterConfigInfo;
import MJShiftObject.Template.CharacterPassiveInfo;
import MJShiftObject.Template.CharacterQuestInfo;
import MJShiftObject.Template.CharacterSkillInfo;
import MJShiftObject.Template.CharacterSlotInfo;
import MJShiftObject.Template.CharacterTamInfo;
import l1j.server.Config;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.Account;
import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftObjectUpdator {
	public static MJShiftObjectUpdator newInstance(String server_identity, Updator updator){
		MJShiftObjectUpdator sUpdator = newInstance();
		sUpdator.m_server_identity = server_identity;
		sUpdator.m_updator = updator;
		return sUpdator;
	}
	
	private static MJShiftObjectUpdator newInstance(){
		return new MJShiftObjectUpdator();
	}
	
	private String m_server_identity;
	private Updator m_updator;
	private MJShiftObjectUpdator(){
	}
	
	public String get_server_identity(){
		return m_server_identity;
	}
	
	public void delete_character_info(final int object_id){
		m_updator.execute(String.format("delete from %s where objid=?", MJEShiftDBName.CHARACTERS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void delete_character_info(final String character_name){
		m_updator.execute(String.format("delete from %s where char_name=?", MJEShiftDBName.CHARACTERS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, character_name);
			}
		});
	}
	
	public void update_character_info(final L1PcInstance pc, final int object_id){
		m_updator.execute(String.format("insert into %s set account_name=?,objid=?,char_name=?"
				+ ",level=?,HighLevel=?,Exp=?,MaxHp=?"
				+ ",CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,BaseStr=?,Con=?,BaseCon=?,Dex=?,BaseDex=?"
				+ ",Cha=?,BaseCha=?,Intel=?,BaseIntel=?,Wis=?,BaseWis=?,Status=?,Class=?,Sex=?,Type=?"
				+ ",Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?"
				+ ",notes=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,PKcount=?,ExpRes=?,PartnerID=?,AccessLevel=?"
				+ ",OnlineStatus=?,HomeTownID=?,Contribution=?,HellTime=?,Banned=?,Karma=?,LastPk=?"
				+ ",DeleteTime=?,ReturnStat=?,sealScrollTime=?,sealScrollCount=?,lastLogoutTime=now()"
				+ ",AinState=?,SurvivalGauge=?"
				+ ",Mark_Count=?,Age=?,AddDamage=?,AddDamageRate=?,AddReduction=?,AddReductionRate=?,"
				+ "TamEndTime=?,SpecialSize=?,FishingShopBuyTime_1=?,slot_number=?,elf_attrinit_count=?, last_topBless=? "
				+ "on duplicate key update "
				+ "level=?,HighLevel=?,Exp=?,MaxHp=?"
				+ ",CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,BaseStr=?,Con=?,BaseCon=?,Dex=?,BaseDex=?"
				+ ",Cha=?,BaseCha=?,Intel=?,BaseIntel=?,Wis=?,BaseWis=?,Status=?,Class=?,Sex=?,Type=?"
				+ ",Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?"
				+ ",notes=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,PKcount=?,ExpRes=?,PartnerID=?,AccessLevel=?"
				+ ",OnlineStatus=?,HomeTownID=?,Contribution=?,HellTime=?,Banned=?,Karma=?,LastPk=?"
				+ ",DeleteTime=?,ReturnStat=?,sealScrollTime=?,sealScrollCount=?,lastLogoutTime=now()"
				+ ",AinState=?,SurvivalGauge=?"
				+ ",Mark_Count=?,Age=?,AddDamage=?,AddDamageRate=?,AddReduction=?,AddReductionRate=?,"
				+ "TamEndTime=?,SpecialSize=?,FishingShopBuyTime_1=?,slot_number=?,elf_attrinit_count=?, last_topBless=?", 
				MJEShiftDBName.CHARACTERS.get_table_name(m_server_identity)), new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						int hp = Math.max(pc.getCurrentHp(), 1);
						
						pstm.setString(++idx, pc.getAccountName());
						pstm.setInt(++idx, object_id);
						pstm.setString(++idx, pc.getName());
						pstm.setInt(++idx, pc.getLevel());
						pstm.setInt(++idx, pc.getHighLevel());
						pstm.setInt(++idx, pc.getExp());
						pstm.setInt(++idx, pc.getBaseMaxHp());
						pstm.setInt(++idx, hp);
						pstm.setInt(++idx, pc.getBaseMaxMp());
						pstm.setInt(++idx, pc.getCurrentMp());
						pstm.setInt(++idx, pc.getAC().getAc());
						pstm.setInt(++idx, pc.getAbility().getStr());
						pstm.setInt(++idx, pc.getAbility().getBaseStr());
						pstm.setInt(++idx, pc.getAbility().getCon());
						pstm.setInt(++idx, pc.getAbility().getBaseCon());
						pstm.setInt(++idx, pc.getAbility().getDex());
						pstm.setInt(++idx, pc.getAbility().getBaseDex());
						pstm.setInt(++idx, pc.getAbility().getCha());
						pstm.setInt(++idx, pc.getAbility().getBaseCha());
						pstm.setInt(++idx, pc.getAbility().getInt());
						pstm.setInt(++idx, pc.getAbility().getBaseInt());
						pstm.setInt(++idx, pc.getAbility().getWis());
						pstm.setInt(++idx, pc.getAbility().getBaseWis());
						pstm.setInt(++idx, pc.getCurrentWeapon());
						pstm.setInt(++idx, pc.getClassId());
						pstm.setInt(++idx, pc.get_sex());
						pstm.setInt(++idx, pc.getType());
						pstm.setInt(++idx, pc.getHeading());
						pstm.setInt(++idx, pc.getX());
						pstm.setInt(++idx, pc.getY());
						pstm.setInt(++idx, pc.getMapId());
						pstm.setInt(++idx, pc.get_food());
						pstm.setInt(++idx, pc.getLawful());
						pstm.setString(++idx, pc.getTitle());
						pstm.setInt(++idx, pc.getClanid());
						pstm.setString(++idx, pc.getClanname());
						pstm.setInt(++idx, pc.getClanRank());
						pstm.setString(++idx, pc.getClanMemberNotes());
						pstm.setInt(++idx, pc.getBonusStats());
						pstm.setInt(++idx, pc.getElixirStats());
						pstm.setInt(++idx, pc.getElfAttr());
						pstm.setInt(++idx, pc.get_PKcount());
						pstm.setInt(++idx, pc.getExpRes());
						pstm.setInt(++idx, pc.getPartnerId());
						pstm.setShort(++idx, pc.getAccessLevel());
						pstm.setInt(++idx, pc.getOnlineStatus());
						pstm.setInt(++idx, pc.getHomeTownId());
						pstm.setInt(++idx, pc.getContribution());
						pstm.setInt(++idx, pc.getHellTime());
						pstm.setBoolean(++idx, pc.isBanned());
						pstm.setInt(++idx, pc.getKarma());
						pstm.setTimestamp(++idx, pc.getLastPk());
						pstm.setTimestamp(++idx, pc.getDeleteTime());
						pstm.setInt(++idx, pc.getReturnStat());
						pstm.setInt(++idx, pc.getSealScrollTime());
						pstm.setInt(++idx, pc.getSealScrollCount());
						pstm.setInt(++idx, pc.getAinState()); // 생존 쿨타임
						pstm.setInt(++idx, pc.getSurvivalGauge()); // 생존게이지
						pstm.setInt(++idx, pc.getMark_count());
						pstm.setInt(++idx, pc.getAge()); // 나이설정
						pstm.setInt(++idx, pc.getAddDamage());
						pstm.setInt(++idx, pc.getAddDamageRate());
						pstm.setInt(++idx, pc.getAddReduction());
						pstm.setInt(++idx, pc.getAddReductionRate());
						pstm.setTimestamp(++idx, pc.getTamTime());
						pstm.setInt(++idx, pc.get_SpecialSize());
						pstm.setString(++idx, pc.getFishingShopBuyTime_1() == 0 ? "0000-00-00 00:00:00"	: new Timestamp(pc.getFishingShopBuyTime_1()).toString());
						pstm.setInt(++idx, pc.getSlotNumber());
						pstm.setInt(++idx, pc.getElfAttrInitCount());
						pstm.setTimestamp(++idx, pc.getLastTopBless());
						
						pstm.setInt(++idx, pc.getLevel());
						pstm.setInt(++idx, pc.getHighLevel());
						pstm.setInt(++idx, pc.getExp());
						pstm.setInt(++idx, pc.getBaseMaxHp());
						pstm.setInt(++idx, hp);
						pstm.setInt(++idx, pc.getBaseMaxMp());
						pstm.setInt(++idx, pc.getCurrentMp());
						pstm.setInt(++idx, pc.getAC().getAc());
						pstm.setInt(++idx, pc.getAbility().getStr());
						pstm.setInt(++idx, pc.getAbility().getBaseStr());
						pstm.setInt(++idx, pc.getAbility().getCon());
						pstm.setInt(++idx, pc.getAbility().getBaseCon());
						pstm.setInt(++idx, pc.getAbility().getDex());
						pstm.setInt(++idx, pc.getAbility().getBaseDex());
						pstm.setInt(++idx, pc.getAbility().getCha());
						pstm.setInt(++idx, pc.getAbility().getBaseCha());
						pstm.setInt(++idx, pc.getAbility().getInt());
						pstm.setInt(++idx, pc.getAbility().getBaseInt());
						pstm.setInt(++idx, pc.getAbility().getWis());
						pstm.setInt(++idx, pc.getAbility().getBaseWis());
						pstm.setInt(++idx, pc.getCurrentWeapon());
						pstm.setInt(++idx, pc.getClassId());
						pstm.setInt(++idx, pc.get_sex());
						pstm.setInt(++idx, pc.getType());
						pstm.setInt(++idx, pc.getHeading());
						pstm.setInt(++idx, pc.getX());
						pstm.setInt(++idx, pc.getY());
						pstm.setInt(++idx, pc.getMapId());
						pstm.setInt(++idx, pc.get_food());
						pstm.setInt(++idx, pc.getLawful());
						pstm.setString(++idx, pc.getTitle());
						pstm.setInt(++idx, pc.getClanid());
						pstm.setString(++idx, pc.getClanname());
						pstm.setInt(++idx, pc.getClanRank());
						pstm.setString(++idx, pc.getClanMemberNotes());
						pstm.setInt(++idx, pc.getBonusStats());
						pstm.setInt(++idx, pc.getElixirStats());
						pstm.setInt(++idx, pc.getElfAttr());
						pstm.setInt(++idx, pc.get_PKcount());
						pstm.setInt(++idx, pc.getExpRes());
						pstm.setInt(++idx, pc.getPartnerId());
						pstm.setShort(++idx, pc.getAccessLevel());
						pstm.setInt(++idx, pc.getOnlineStatus());
						pstm.setInt(++idx, pc.getHomeTownId());
						pstm.setInt(++idx, pc.getContribution());
						pstm.setInt(++idx, pc.getHellTime());
						pstm.setBoolean(++idx, pc.isBanned());
						pstm.setInt(++idx, pc.getKarma());
						pstm.setTimestamp(++idx, pc.getLastPk());
						pstm.setTimestamp(++idx, pc.getDeleteTime());
						pstm.setInt(++idx, pc.getReturnStat());
						pstm.setInt(++idx, pc.getSealScrollTime());
						pstm.setInt(++idx, pc.getSealScrollCount());
						pstm.setInt(++idx, pc.getAinState()); // 생존 쿨타임
						pstm.setInt(++idx, pc.getSurvivalGauge()); // 생존게이지
						pstm.setInt(++idx, pc.getMark_count());
						pstm.setInt(++idx, pc.getAge()); // 나이설정
						pstm.setInt(++idx, pc.getAddDamage());
						pstm.setInt(++idx, pc.getAddDamageRate());
						pstm.setInt(++idx, pc.getAddReduction());
						pstm.setInt(++idx, pc.getAddReductionRate());
						pstm.setTimestamp(++idx, pc.getTamTime());
						pstm.setInt(++idx, pc.get_SpecialSize());
						pstm.setString(++idx, pc.getFishingShopBuyTime_1() == 0 ? "0000-00-00 00:00:00"	: new Timestamp(pc.getFishingShopBuyTime_1()).toString());
						pstm.setInt(++idx, pc.getSlotNumber());
						pstm.setInt(++idx, pc.getElfAttrInitCount());
						pstm.setTimestamp(++idx, pc.getLastTopBless());
					}
				});
	}
	
	public void update_accounts_only(final Account account){
		m_updator.execute(String.format("update %s set "
				+ "password=?, "
				+ "lastactive=?, "
				+ "access_level=?, "
				+ "ip=?, "
				+ "host=?, "
				+ "banned=?, "
				+ "charslot=?, "
				+ "gamepassword=?, "
				+ "notice=?, "
				+ "phone=?, "
				+ "Tam_Point=?, "
				+ "tam=?, "
				+ "tamStep=?, "
				+ "BUFF_PCROOM_Time=?, "
				+ "CharPassword=?, "
				+ "Ncoin_Point=?, "
				+ "Shop_open_count=?, "
				+ "raid_buff=?, "
				+ "bless_of_ain=?, "
				+ "GrangKinAngerTime=?, "
				+ "last_log_out=? "
				+ "where login=?"
				, 
				MJEShiftDBName.ACCOUNTS.get_table_name(m_server_identity)), new Handler(){

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, account.get_Password());
				pstm.setTimestamp(++idx, account.getLastActive());
				pstm.setInt(++idx, account.getAccessLevel());
				pstm.setString(++idx, account.getHost());
				pstm.setString(++idx, account.getHost());
				pstm.setInt(++idx, account.getBannedCode());
				pstm.setInt(++idx, Config.Characters_CharSlot);
				pstm.setInt(++idx, account.getGamePassword());
				pstm.setInt(++idx, 0);
				pstm.setString(++idx, account.getphone());
				pstm.setInt(++idx, account.getTamPoint());
				pstm.setInt(++idx, account.getTam());
				pstm.setInt(++idx, account.getTamStep());
				pstm.setTimestamp(++idx, account.getBuff_PC방());
				pstm.setString(++idx, account.getCPW());
				pstm.setInt(++idx, account.Ncoin_point);
				pstm.setInt(++idx, account.Shop_open_count);
				pstm.setTimestamp(++idx, account.getDragonRaid());
				pstm.setInt(++idx, account.getBlessOfAin());
				pstm.setInt(++idx, account.getGrangKinAngerTime());
				pstm.setTimestamp(++idx, account.getLastLogOut());
				pstm.setString(++idx, account.getName());
			}
		});
	}
	
	public void update_accounts(final Account account){
		m_updator.execute(String.format("insert into %s set "
				+ "login=?, "
				+ "password=?, "
				+ "lastactive=?, "
				+ "access_level=?, "
				+ "ip=?, "
				+ "host=?, "
				+ "banned=?, "
				+ "charslot=?, "
				+ "gamepassword=?, "
				+ "notice=?, "
				+ "phone=?, "
				+ "Tam_Point=?, "
				+ "tam=?, "
				+ "tamStep=?, "
				+ "BUFF_PCROOM_Time=?, "
				+ "CharPassword=?, "
				+ "Ncoin_Point=?, "
				+ "Shop_open_count=?, "
				+ "raid_buff=?, "
				+ "bless_of_ain=?, "
				+ "GrangKinAngerTime=?, "
				+ "last_log_out=? "
				+ "on duplicate key update "
				+ "password=?, "
				+ "lastactive=?, "
				+ "access_level=?, "
				+ "ip=?, "
				+ "host=?, "
				+ "banned=?, "
				+ "charslot=?, "
				+ "gamepassword=?, "
				+ "notice=?, "
				+ "phone=?, "
				+ "Tam_Point=?, "
				+ "tam=?, "
				+ "tamStep=?, "
				+ "BUFF_PCROOM_Time=?, "
				+ "CharPassword=?, "
				+ "Ncoin_Point=?, "
				+ "Shop_open_count=?, "
				+ "raid_buff=?, "
				+ "bless_of_ain=?, "
				+ "GrangKinAngerTime=?, "
				+ "last_log_out=?", 
				MJEShiftDBName.ACCOUNTS.get_table_name(m_server_identity)), new Handler(){

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, account.getName());
				pstm.setString(++idx, account.get_Password());
				pstm.setTimestamp(++idx, account.getLastActive());
				pstm.setInt(++idx, account.getAccessLevel());
				pstm.setString(++idx, account.getHost());
				pstm.setString(++idx, account.getHost());
				pstm.setInt(++idx, account.getBannedCode());
				pstm.setInt(++idx, Config.Characters_CharSlot);
				pstm.setInt(++idx, account.getGamePassword());
				pstm.setInt(++idx, 0);
				pstm.setString(++idx, account.getphone());
				pstm.setInt(++idx, account.getTamPoint());
				pstm.setInt(++idx, account.getTam());
				pstm.setInt(++idx, account.getTamStep());
				pstm.setTimestamp(++idx, account.getBuff_PC방());
				pstm.setString(++idx, account.getCPW());
				pstm.setInt(++idx, account.Ncoin_point);
				pstm.setInt(++idx, account.Shop_open_count);
				pstm.setTimestamp(++idx, account.getDragonRaid());
				pstm.setInt(++idx, account.getBlessOfAin());
				pstm.setInt(++idx, account.getGrangKinAngerTime());
				pstm.setTimestamp(++idx, account.getLastLogOut());
				
				pstm.setString(++idx, account.get_Password());
				pstm.setTimestamp(++idx, account.getLastActive());
				pstm.setInt(++idx, account.getAccessLevel());
				pstm.setString(++idx, account.getHost());
				pstm.setString(++idx, account.getHost());
				pstm.setInt(++idx, account.getBannedCode());
				pstm.setInt(++idx, Config.Characters_CharSlot);
				pstm.setInt(++idx, account.getGamePassword());
				pstm.setInt(++idx, 0);
				pstm.setString(++idx, account.getphone());
				pstm.setInt(++idx, account.getTamPoint());
				pstm.setInt(++idx, account.getTam());
				pstm.setInt(++idx, account.getTamStep());
				pstm.setTimestamp(++idx, account.getBuff_PC방());
				pstm.setString(++idx, account.getCPW());
				pstm.setInt(++idx, account.Ncoin_point);
				pstm.setInt(++idx, account.Shop_open_count);
				pstm.setTimestamp(++idx, account.getDragonRaid());
				pstm.setInt(++idx, account.getBlessOfAin());
				pstm.setInt(++idx, account.getGrangKinAngerTime());
				pstm.setTimestamp(++idx, account.getLastLogOut());
			}
		});
	}
	

	public void delete_accounts(final String account_name){
		m_updator.execute(String.format("delete from %s where login=?", MJEShiftDBName.ACCOUNTS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account_name);
			}
		});
	}
	
	public void update_character_buff(final List<CharacterBuffInfo> buffs, final int object_id){
		if(buffs == null || buffs.size() <= 0)
			return;
		
		m_updator.execBatch(String.format("insert into %s SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=? on duplicate key update remaining_time=?, poly_id=?", MJEShiftDBName.CHARACTERS_BUFF.get_table_name(m_server_identity)), new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception{
				int idx = 0;
				CharacterBuffInfo bInfo = buffs.get(callNumber);
				pstm.setInt(++idx, object_id);
				pstm.setInt(++idx, bInfo.skill_id);
				pstm.setInt(++idx, bInfo.time_sec);
				pstm.setInt(++idx, bInfo.poly_id);
				pstm.setInt(++idx, bInfo.time_sec);
				pstm.setInt(++idx, bInfo.poly_id);
			}
		}, buffs.size());
	}
	
	public void delete_character_buff(final int object_id){
		m_updator.execute(String.format("delete from %s where char_obj_id=?", MJEShiftDBName.CHARACTERS_BUFF.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	
	public void update_character_config(final CharacterConfigInfo cInfo, final int object_id){
		if(cInfo == null)
			return;
		
		m_updator.execute(String.format("insert into %s SET object_id=?, length=?, data=? on duplicate key update length=?, data=?", MJEShiftDBName.CHARACTERS_CONFIG.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, object_id);
				pstm.setInt(++idx, cInfo.length);
				pstm.setBytes(++idx, cInfo.buff);
				pstm.setInt(++idx, cInfo.length);
				pstm.setBytes(++idx, cInfo.buff);
			}
		});	
	}
	
	public void delete_character_config(final int object_id){
		m_updator.execute(String.format("delete from %s where object_id=?" ,MJEShiftDBName.CHARACTERS_CONFIG.get_table_name(m_server_identity)),  new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void update_character_items(final List<L1ItemInstance> items, final int object_id){
		if(items == null || items.size() <= 0)
			return;
		
		m_updator.execBatch(String.format("insert into %s SET "
				+ "id=?, "
				+ "item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?, buy_time=?, package=?, bless_level=?, item_level=?, Hotel_Town=? "
				+ "on duplicate key update "
				+ "item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?, buy_time=?, package=?, bless_level=?, item_level=?, Hotel_Town=?", 
				MJEShiftDBName.CHARACTERS_ITEMS.get_table_name(m_server_identity)), new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				int idx = 0;
				L1ItemInstance item = items.get(callNumber);
				pstm.setInt(++idx, IdFactory.getInstance().nextId());
				pstm.setInt(++idx, item.getItem().getItemId());
				pstm.setInt(++idx, object_id);
				pstm.setString(++idx, item.getItem().getName());
				pstm.setInt(++idx, item.getCount());
				pstm.setInt(++idx, item.isEquipped() ? 1 : 0);
				pstm.setInt(++idx, item.getEnchantLevel());
				pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
				pstm.setInt(++idx, item.get_durability());
				pstm.setInt(++idx, item.getChargeCount());
				pstm.setInt(++idx, item.getRemainingTime());
				pstm.setTimestamp(++idx, item.getLastUsed());
				pstm.setInt(++idx, item.getBless());
				pstm.setInt(++idx, item.getAttrEnchantLevel());
				pstm.setTimestamp(++idx, item.getEndTime());
				pstm.setTimestamp(++idx, item.getEndTime());
				pstm.setInt(++idx, item.isPackage() == false ? 0 : 1);
				pstm.setInt(++idx, item.get_bless_level());
				pstm.setInt(++idx, item.get_item_level());
				pstm.setString(++idx, item.getHotel_Town());
				
				pstm.setInt(++idx, item.getItem().getItemId());
				pstm.setInt(++idx, object_id);
				pstm.setString(++idx, item.getItem().getName());
				pstm.setInt(++idx, item.getCount());
				pstm.setInt(++idx, item.isEquipped() ? 1 : 0);
				pstm.setInt(++idx, item.getEnchantLevel());
				pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
				pstm.setInt(++idx, item.get_durability());
				pstm.setInt(++idx, item.getChargeCount());
				pstm.setInt(++idx, item.getRemainingTime());
				pstm.setTimestamp(++idx, item.getLastUsed());
				pstm.setInt(++idx, item.getBless());
				pstm.setInt(++idx, item.getAttrEnchantLevel());
				pstm.setTimestamp(++idx, item.getEndTime());
				pstm.setTimestamp(++idx, item.getEndTime());
				pstm.setInt(++idx, item.isPackage() == false ? 0 : 1);
				pstm.setInt(++idx, item.get_bless_level());
				pstm.setInt(++idx, item.get_item_level());
				pstm.setString(++idx, item.getHotel_Town());
			}
		}, items.size());
	}
	
	public void delete_character_items(final int object_id){
		m_updator.execute(String.format("delete from %s where char_id=?", MJEShiftDBName.CHARACTERS_ITEMS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void update_character_skills(final List<CharacterSkillInfo> skills, final int object_id){
		if(skills == null || skills.size() <= 0)
			return;
		
		m_updator.execBatch(String.format("insert into %s set "
				+ "char_obj_id=?, skill_id=?, skill_name=?, is_active=?, activetimeleft=? "
				+ "on duplicate key update "
				+ "skill_name=?, is_active=?, activetimeleft=?", 
				MJEShiftDBName.CHARACTERS_SKILLS.get_table_name(m_server_identity)), 
				new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				CharacterSkillInfo csInfo = skills.get(callNumber);
				int idx = 0;
				pstm.setInt(++idx, object_id);
				pstm.setInt(++idx, csInfo.skill_id);
				pstm.setString(++idx, csInfo.skill_name);
				pstm.setInt(++idx, csInfo.is_active ? 1 : 0);
				pstm.setInt(++idx, csInfo.active_time_left);
				pstm.setString(++idx, csInfo.skill_name);
				pstm.setInt(++idx, csInfo.is_active ? 1 : 0);
				pstm.setInt(++idx, csInfo.active_time_left);
			}
			
		}, skills.size());
	}
	
	public void delete_character_skills(final int object_id){
		m_updator.execute(String.format("delete from %s where char_obj_id=?", MJEShiftDBName.CHARACTERS_SKILLS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void update_character_passive(final List<CharacterPassiveInfo> passives, final int object_id){
		if(passives == null || passives.size() <= 0)
			return;
		
		m_updator.execBatch(String.format("insert into %s set "
				+ "character_id=?, passive_id=?, passive_name=? "
				+ "on duplicate key update "
				+ "passive_name=?", 
				MJEShiftDBName.PASSIVE_USER_INFO.get_table_name(m_server_identity)), new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				int idx = 0;
				CharacterPassiveInfo pInfo = passives.get(callNumber);
				pstm.setInt(++idx, object_id);
				pstm.setInt(++idx, pInfo.passive_id);
				pstm.setString(++idx, pInfo.passive_name);
				pstm.setString(++idx, pInfo.passive_name);
			}
		}, passives.size());
	}
	
	public void delete_character_passive(final int object_id){
		m_updator.execute(String.format("delete from %s where character_id=?", MJEShiftDBName.PASSIVE_USER_INFO.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void update_character_slot_items(final List<CharacterSlotInfo> slots, final int object_id){
		if(slots == null || slots.size() <= 0)
			return;
		
		m_updator.execBatch(String.format("insert ignore into %s set "
				+ "char_id=?, item_objid=?, slot_number=?",
				MJEShiftDBName.CHARACTERS_SLOT_ITEMS.get_table_name(m_server_identity)), 
				new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				int idx = 0;
				CharacterSlotInfo csInfo = slots.get(callNumber);
				pstm.setInt(++idx, object_id);
				pstm.setInt(++idx, csInfo.source_item_id);
				pstm.setInt(++idx, csInfo.slot_number);
			}
			
		}, slots.size());
	}
	
	public void delete_character_slot_items(final int object_id){
		m_updator.execute(String.format("delete from %s where char_id=?", MJEShiftDBName.CHARACTERS_SLOT_ITEMS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void update_character_tams(List<CharacterTamInfo> tams, final int object_id){
		if(tams == null || tams.size() <= 0)
			return;
		
		m_updator.execBatch(String.format("insert into %s set "
				+ "account_id=?, char_id=?, char_name=?, skill_id=?, expiration_time=?, reserve=? "
				+ "on duplicate key update "
				+ "account_id=?, char_name=?, expiration_time=?, reserve=?", 
				MJEShiftDBName.CHARACTERS_TAMS.get_table_name(m_server_identity)), new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				CharacterTamInfo tInfo = tams.get(callNumber);
				int idx = 0;
				pstm.setInt(++idx, tInfo.account_id);
				pstm.setInt(++idx, object_id);
				pstm.setString(++idx, tInfo.character_name);
				pstm.setInt(++idx, tInfo.skill_id);
				pstm.setTimestamp(++idx, tInfo.expiration_time);
				pstm.setInt(++idx, tInfo.reserve);
				pstm.setInt(++idx, tInfo.account_id);
				pstm.setString(++idx, tInfo.character_name);
				pstm.setTimestamp(++idx, tInfo.expiration_time);
				pstm.setInt(++idx, tInfo.reserve);
			}
		}, tams.size());
	}
	
	public void delete_character_tams(final int object_id){
		m_updator.execute(String.format("delete from %s where char_id=?", MJEShiftDBName.CHARACTERS_TAMS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void update_character_level_bonus(final int character_level_bonus, final int object_id){
		m_updator.execute(String.format("insert into %s set objid=?, level=? on duplicate key update level=?", MJEShiftDBName.CHARACTERS_BONUS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
				pstm.setInt(2, character_level_bonus);
				pstm.setInt(3, character_level_bonus);
			}
		});
	}
	
	public void delete_character_level_bonus(final int object_id){
		m_updator.execute(String.format("delete from %s where objid=?", MJEShiftDBName.CHARACTERS_BONUS.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
	public void update_character_quest_info(List<CharacterQuestInfo> quests, final int object_id){
		if(quests == null || quests.size() <= 0)
			return;
		
		m_updator.execBatch(String.format("insert into %s set "
				+ "char_id=?, quest_id=?, quest_step=? "
				+ "on duplicate key update "
				+ "quest_id=?, quest_step=?", 
				MJEShiftDBName.CHARACTERS_QUEST.get_table_name(m_server_identity)), new BatchHandler(){
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				CharacterQuestInfo qInfo = quests.get(callNumber);
				int idx = 0;
				pstm.setInt(++idx, object_id);
				pstm.setInt(++idx, qInfo.quest_id);
				pstm.setInt(++idx, qInfo.quest_step);
				pstm.setInt(++idx, qInfo.quest_id);
				pstm.setInt(++idx, qInfo.quest_step);
			}
		}, quests.size());
	}
	
	public void delete_character_quest_info(final int object_id){
		m_updator.execute(String.format("delete from %s where char_id=?", MJEShiftDBName.CHARACTERS_QUEST.get_table_name(m_server_identity)), new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, object_id);
			}
		});
	}
	
}
