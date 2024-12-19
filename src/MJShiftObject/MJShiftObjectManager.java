package MJShiftObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import MJShiftObject.Battle.MJIShiftBattleNotify;
import MJShiftObject.Battle.MJShiftBattleManager;
import MJShiftObject.Battle.Thebe.MJThebePlayManager;
import MJShiftObject.DB.MJShiftDBArgs;
import MJShiftObject.DB.MJShiftDBFactory;
import MJShiftObject.DB.Helper.MJShiftSelector;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Object.MJShiftObjectOneTimeToken;
import MJShiftObject.Object.Converter.MJShiftObjectReceiver;
import MJShiftObject.Object.Converter.MJShiftObjectSender;
import MJShiftObject.Template.CommonServerBattleInfo;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.MJTemplate.Chain.Chat.MJIWhisperChatFilterHandler;
import l1j.server.MJTemplate.Chain.Chat.MJIWorldChatFilterHandler;
import l1j.server.MJTemplate.Chain.Chat.MJWhisperChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWorldChatFilterChain;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.Result;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_HIBREED_AUTH_ACK_PACKET;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;

public class MJShiftObjectManager implements MJIShiftBattleNotify, MJIWhisperChatFilterHandler, MJIWorldChatFilterHandler{
	public static void do_fail_send(final GameClient clnt){
		SC_HIBREED_AUTH_ACK_PACKET pck = SC_HIBREED_AUTH_ACK_PACKET.newInstance();
		pck.set_result(Result.Result_wrong_clientip);
		clnt.sendPacket(pck, MJEProtoMessages.SC_HIBREED_AUTH_ACK_PACKET.toInt(), true);
	}
	
	private static MJShiftObjectManager _instance;
	public static MJShiftObjectManager getInstance(){
		if(_instance == null)
			_instance = new MJShiftObjectManager();
		return _instance;
	}
	
	private MJShiftDBArgs m_db_args;
	private MJShiftDBFactory m_db_factory;
	private HashMap<String, MJShiftObjectReceiver> m_receivers;
	private MJShiftObjectSender m_sender;
	private MJShiftBattleManager m_battle_manager;
	private MJShiftObjectManager(){
		try{
			m_db_args = new MJShiftDBArgs("./config/mj_shiftserver.properties");
			m_db_factory = new MJShiftDBFactory(m_db_args);
			MJWhisperChatFilterChain.getInstance().add_handler(this);
			MJWorldChatFilterChain.getInstance().add_handler(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void reload_config(){
		m_db_args = new MJShiftDBArgs("./config/mj_shiftserver.properties");
	}
	
	public MJShiftObjectManager load_common_server_info() throws Exception{
		m_receivers = new HashMap<String, MJShiftObjectReceiver>();
		m_sender = new MJShiftObjectSender(m_db_args.SERVER_IDENTITY);
		MJShiftSelector.exec("select * from common_shift_server_info", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJShiftObjectReceiver receiver = MJShiftObjectReceiver.newInstance(rs);
					m_receivers.put(receiver.get_server_identity(), receiver);
				}
			}
		});
		MJShiftObjectReceiver receiver = m_receivers.get(m_db_args.SERVER_IDENTITY);
		if(receiver == null)
			throw new Exception("공용 디비에서 내 서버 정보를 찾을 수 없습니다.");			
		
		MJShiftObjectHelper.on_shift_server_info(m_db_args.SERVER_IDENTITY);
		return this;
	}
	
	public void release(){
		MJShiftObjectHelper.off_shift_server_info(m_db_args.SERVER_IDENTITY);
	}
	
	public List<CommonServerInfo> get_commons_servers(boolean is_exclude_my_server){
		return MJShiftObjectHelper.get_commons_servers(m_db_args.SERVER_IDENTITY, is_exclude_my_server);
	}
	
	public List<CommonServerBattleInfo> get_battle_servers_info(){
		return MJShiftObjectHelper.get_battle_servers_info();
	}
	public int get_receiver_team_id(String server_identity){
		MJShiftObjectReceiver receiver = m_receivers.get(server_identity);
		if(receiver == null)
			return -1;
		
		return receiver.get_server_battle_team_id();
	}
	
	public void do_receive(GameClient clnt, int reserved_number, String onetimetoken) throws Exception{
		final MJShiftObjectOneTimeToken token = MJShiftObjectOneTimeToken.from_onetimetoken(onetimetoken);
		if(token == null){
			System.out.println(String.format("인식할 수 없는 캐릭터 정보가 넘어왔습니다.(%s)(%s)", clnt.getIp(), onetimetoken));
			do_fail_send(clnt);
			return;
		}
		MJShiftObjectReceiver receiver = m_receivers.get(token.home_server_identity);
		if(receiver == null){
			System.out.println(String.format("알 수 없는 서버에서 캐릭터 정보가 넘어왔습니다.(%s)(%s)", clnt.getIp(), onetimetoken));
			do_fail_send(clnt);
			return;			
		}
		if(!token.is_returner && token.shift_object.get_shift_type().equals(MJEShiftObjectType.BATTLE)){
			if(!is_my_battle_server()){
				System.out.println(String.format("현재 만들어진 서버대항전이 없는데 서버대항전으로 캐릭터 정보가 넘어왔습니다.(%s)(%s)", clnt.getIp(), onetimetoken));
				do_fail_send(clnt);
				return;
			}			
		}
		
		receiver.do_receive(clnt, reserved_number, token);
	}
	
	public void do_send_battle_server(L1PcInstance pc) throws Exception{
		if(!is_battle_server_running()){
			pc.sendPackets("현재 진행중인 대항전이 없습니다.");
			return;
		}
		if(m_sender.get_object_count() >= m_db_args.BATTLE_SERVER_SEND_COUNT){
			pc.sendPackets("참가할 수 있는 인원이 꽉 찼습니다.");
			return;
		}
		
		String server_identity = get_battle_server_identity();
		do_send(pc, MJEShiftObjectType.BATTLE, server_identity);
	}
	
	public void do_send(L1PcInstance pc, MJEShiftObjectType shift_type, String server_identity) throws Exception{
		MJShiftObjectReceiver receiver = m_receivers.get(server_identity);
		m_sender.do_send(pc, shift_type, receiver);
	}
	public boolean is_shift_sender_contains(int object_id){
		return m_sender.get_object(object_id) != null;
	}
	public MJShiftObject get_shift_sender_object(int object_id){
		return m_sender.get_object(object_id);
	}
	
	public void do_returner(){
		for(MJShiftObjectReceiver receiver : m_receivers.values()){
			receiver.do_return_database();
		}
		for(int i=m_db_args.MY_BATTLE_SERVER_QUIT_READY_SECONDS; i>0; --i){
			try {
				String message = String.format("%d초 후 대항전이 종료됩니다.", i);
				S_PacketBox box = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message);
				System.out.println(message);
				L1World.getInstance().broadcastPacketToAll(box);
				Thread.sleep(1000L);
				if(i == m_db_args.MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS){
					m_sender.do_getback();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		for(MJShiftObjectReceiver receiver : m_receivers.values()){
			receiver.do_return_players();
		}
		for(MJShiftObjectReceiver receiver : m_receivers.values()){
			receiver.clear_objects();
		}
		m_battle_manager = null;
	}
	
	public void do_getbacker(){
		m_sender.do_getback();
		m_battle_manager = null;
	}
	
	public Connection get_connection() throws Exception{
		if(m_db_factory == null)
			throw new Exception("not initialized remote database.");
		
		return m_db_factory.get_connection();
	}
	public String get_home_server_identity(){
		return m_db_args.SERVER_IDENTITY;
	}
	public int get_character_transfer_itemid(){
		return m_db_args.CHARACTER_TRANSFER_ITEMID;
	}
	public int get_my_server_battle_ready_seconds(){
		return m_db_args.MY_BATTLE_SERVER_QUIT_READY_SECONDS;
	}
	public int get_my_server_battle_store_ready_seconds(){
		return m_db_args.MY_BATTLE_SERVER_RESERVATION_STORE_READY_SECONDS;
	}
	public boolean is_battle_server_enter(){
		return m_battle_manager != null;
	}
	public boolean is_battle_server_running(){
		return m_battle_manager != null && m_battle_manager.is_battle_server_running();
	}
	public String get_battle_server_identity(){
		return m_battle_manager == null ? "" : m_battle_manager.get_battle_server_identity();
	}
	
	
	public void do_enter_battle_server(CommonServerBattleInfo bInfo, MJThebePlayManager manager){
		if(m_battle_manager != null)
			return;
		
		m_battle_manager = new MJShiftBattleManager(bInfo, manager);
		m_battle_manager.add_notify(this);
		m_battle_manager.execute();
	}
	
	public void do_cancel_battle_server(){
		if(m_battle_manager == null)
			return;
		
		m_battle_manager.set_cancel_state(true);
		m_battle_manager = null;
	}
	public boolean is_my_battle_server(){
		return m_battle_manager != null && m_battle_manager.get_battle_server_identity().equals(m_db_args.SERVER_IDENTITY);
	}
	
	public void do_enter_battle_character(L1PcInstance pc){
		if(m_battle_manager != null)
			m_battle_manager.do_enter_battle_character(pc);
	}
	
	public String get_source_character_name(L1PcInstance pc){
		MJShiftObjectReceiver receiver = m_receivers.get(pc.getNetConnection().get_server_identity());
		return receiver.get_source_character_name(pc.getId());
	}
	public int get_source_character_id(L1PcInstance pc){
		MJShiftObjectReceiver receiver = m_receivers.get(pc.getNetConnection().get_server_identity());
		return receiver.get_source_character_id(pc.getId());
	}
	public MJShiftObject get_shift_object(L1PcInstance pc){
		MJShiftObjectReceiver receiver = m_receivers.get(pc.getNetConnection().get_server_identity());
		return receiver.get_object(pc.getId());
	}
	
	@Override
	public void do_ended(CommonServerBattleInfo bInfo){
		if(is_my_battle_server()){
			do_returner();
		}else{
			System.out.println(String.format("%s 대항전 참가가 종료되었습니다.", bInfo.get_server_identity()));
			do_getbacker();
		}
	}

	@Override
	public boolean is_chat(L1PcInstance from, String to_name, String message) {
		if(from.is_shift_client()){
			from.sendPackets("현재 귓속말을 사용할 수 없는 상태입니다.");
			return true;
		}
		return false;
	}

	@Override
	public boolean is_chat(L1PcInstance owner, String message) {
		if(owner.is_shift_client()){
			owner.sendPackets("현재 전체채팅을 사용할 수 없는 상태입니다.");
			return true;
		}
		return false;
	}
}
