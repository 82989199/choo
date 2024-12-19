package MJShiftObject.Object.Converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Object.MJShiftObjectOneTimeToken;
import MJShiftObject.Object.Converter.Selector.MJShiftObjectSelector;
import MJShiftObject.Object.Converter.Updator.MJShiftObjectUpdator;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.Result;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HIBREED_AUTH_ACK_PACKET;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.clientpackets.C_LoginToServer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftObjectReceiver {
	public static MJShiftObjectReceiver newInstance(ResultSet rs) throws SQLException{
		return newInstance(
				rs.getString("server_description"),
				rs.getString("server_identity"),
				rs.getString("server_address"),
				rs.getInt("server_port"),
				rs.getInt("server_battle_team_id")
				);
	}
	
	public static MJShiftObjectReceiver newInstance(String server_description, String server_identity, String server_address, int server_port, int server_battle_team_id){
		return new MJShiftObjectReceiver(server_description, server_identity, server_address, server_port, server_battle_team_id);
	}
	
	private String m_server_description;
	private String m_server_identity;
	private String m_server_address;
	private int m_server_address_bigendian;
	private int m_server_port; 
	private int m_server_battle_team_id;
	private ConcurrentHashMap<Integer, MJShiftObject> m_objects;
	private IMJShiftObjectDBConverter m_receiver;
	private MJShiftObjectSelector m_local_selector;
	private MJShiftObjectReceiver(String server_description, String server_identity, String server_address, int server_port, int server_battle_team_id){
		m_server_description = server_description;
		m_server_identity = server_identity;
		m_server_address = server_address;
		m_server_port = server_port;
		m_server_battle_team_id = server_battle_team_id;
		StringTokenizer tok = new StringTokenizer(m_server_address);
		m_server_address_bigendian = 0;
		for(int i=3; i>=0; --i){
			int bit = i * 8;
			m_server_address_bigendian |= 
					(Integer.parseInt(tok.nextToken(".")) << bit) & 
					(0xff << bit);
		}
		
		m_objects = new ConcurrentHashMap<Integer, MJShiftObject>();
		m_receiver = MJShiftObjectDBConverterFactory.create_receiver(m_server_identity);
		m_local_selector = MJShiftObjectSelector.newInstance("", new Selector());
	}

	public String get_server_description(){
		return m_server_description;
	}
	public String get_server_identity(){
		return m_server_identity;
	}
	public String get_server_address(){
		return m_server_address;
	}
	public int get_server_address_bigendian(){
		return m_server_address_bigendian;
	}
	public int get_server_port(){
		return m_server_port;
	}
	public int get_server_battle_team_id(){
		return m_server_battle_team_id;
	}
	public MJShiftObject get_object(final int object_id){
		return m_objects.get(object_id);
	}
	public void do_return_database(){
		MJShiftObjectUpdator local_updator = MJShiftObjectUpdator.newInstance("", new Updator());		
		for(MJShiftObject sobject : m_objects.values()){
			int destination_id = sobject.get_destination_id();
			local_updator.delete_character_buff(destination_id);
			local_updator.delete_character_items(destination_id);
			local_updator.delete_accounts(sobject.get_destination_account_name());
			local_updator.delete_character_info(destination_id);
			local_updator.delete_character_level_bonus(destination_id);
			local_updator.delete_character_passive(destination_id);
			local_updator.delete_character_quest_info(destination_id);
			local_updator.delete_character_skills(destination_id);
			local_updator.delete_character_slot_items(destination_id);
			local_updator.delete_character_tams(destination_id);
			local_updator.delete_character_config(destination_id);
		}
	}
	
	public void do_return_players(){
		for(MJShiftObject sobject : m_objects.values()){
			L1Object obj = L1World.getInstance().findObject(sobject.get_destination_id());
			if(obj == null)
				continue;
			
			try{
				L1PcInstance pc = (L1PcInstance)obj;
				pc.getNetConnection().set_shift_type(MJEShiftObjectType.NONE);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET pck = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance();
				pck.set_destIP(m_server_address_bigendian);
				pck.set_destPort(m_server_port);
				pck.set_domainname(m_server_address);
				pck.set_interkind(0);
				pck.set_onetimetoken(new MJShiftObjectOneTimeToken(m_server_identity, true, sobject, m_server_identity, false).to_onetime_token().getBytes());
				pck.set_reservednumber(sobject.get_source_id());
				pc.sendPackets(pck, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET, true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void clear_objects(){
		m_objects.clear();
	}
	
	private void do_getback_player(final GameClient clnt, final int source_object_id, final MJShiftObjectOneTimeToken token) throws GameServerFullException, AccountAlreadyLoginException{
		final MJShiftObject sobject = token.shift_object;
		Account account = m_local_selector.select_accounts(sobject.get_source_account_name());
		account.setValid(true);
		clnt.setAccount(account);
		clnt.set_server_identity("");
		clnt.set_shift_type(MJEShiftObjectType.NONE);
		LoginController.getInstance().login(clnt, account);
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run() {
				try{
					SC_HIBREED_AUTH_ACK_PACKET pck = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
					pck.set_result(Result.Result_sucess);
					clnt.sendPacket(pck, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt(), true);	
					clnt.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, 3000);
	}
	
	private MJShiftObject check_token(MJShiftObjectOneTimeToken token){
		MJShiftObject sobject = token.shift_object;
		if(token.is_reconnected){
			sobject = MJShiftObjectHelper.select_shift_object(sobject.get_source_id(), token.home_server_identity);
			if(sobject == null || !m_objects.containsKey(sobject.get_destination_id())){
				sobject = token.shift_object;
				token.is_reconnected = false;
			}else{
				sobject = m_objects.get(sobject.get_destination_id());
			}
		}
		return sobject;
	}
	
	private void check_shift_type(GameClient clnt, MJShiftObjectOneTimeToken token, MJShiftObject sobject){
		if(sobject.get_shift_type().equals(MJEShiftObjectType.TRANSFER)){
			MJShiftObjectHelper.delete_shift_object(sobject, token.home_server_identity);
		}else if(sobject.get_shift_type().equals(MJEShiftObjectType.BATTLE)){
			clnt.set_server_identity(token.home_server_identity);
			clnt.set_server_description(m_server_description);
			if(!token.is_reconnected)
				m_objects.put(sobject.get_destination_id(), sobject);
		}else{
			throw new IllegalArgumentException(String.format("invalid shift server type %s : %s", sobject.get_shift_type(), sobject.get_shift_type().to_name()));
		}
		clnt.set_shift_type(sobject.get_shift_type());
	}
	
	public void do_receive(final GameClient clnt, final int source_object_id, final MJShiftObjectOneTimeToken token) throws GameServerFullException, AccountAlreadyLoginException{
		if(token.is_returner){
			do_getback_player(clnt, source_object_id, token);
			return;
		}
		
		MJShiftObject sobject = check_token(token);		
		if(!token.is_reconnected){
			if(sobject.get_destination_id() == 0)
				sobject.set_destination_id(IdFactory.getInstance().nextId());

			int result = m_receiver.work(sobject);
			if(result != IMJShiftObjectDBConverter.CONVERT_SUCCESS){
				System.out.println(String.format("%s(%d)(%s)(%s)의 데이터베이스 컨버팅에 실패했습니다.(%d)", sobject.get_source_character_name(), sobject.get_source_id(), sobject.get_shift_type().to_name(), clnt.getIp(),result));
				MJShiftObjectManager.do_fail_send(clnt);
				return;
			}
		}
		do_login(clnt, sobject);
		check_shift_type(clnt, token, sobject);
		do_enter_world(clnt, sobject);
	}
	
	private void do_login(final GameClient clnt, final MJShiftObject sobject) throws GameServerFullException, AccountAlreadyLoginException{
		Account account = m_local_selector.select_accounts(sobject.get_destination_account_name());
		account.setValid(true);
		clnt.setAccount(account);
		LoginController.getInstance().login(clnt, account);
	}
	
	private void do_enter_world(final GameClient clnt, final MJShiftObject sobject){
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run() {
				try{
					SC_HIBREED_AUTH_ACK_PACKET pck = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
					pck.set_result(Result.Result_sucess);
					clnt.sendPacket(pck, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt(), true);	
					clnt.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, 3000);
	}
	
	public String get_source_character_name(int destination_id){
		MJShiftObject sobject = m_objects.get(destination_id);
		return sobject == null ? "" : sobject.get_source_character_name();
	}
	public int get_source_character_id(int destination_id){
		MJShiftObject sobject = m_objects.get(destination_id);
		return sobject == null ? 0 : sobject.get_source_id();
	}
}
