package MJShiftObject.Object.Converter;

import java.util.concurrent.ConcurrentHashMap;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Object.MJShiftObjectOneTimeToken;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

public class MJShiftObjectSender {
	private String m_server_identity;
	private ConcurrentHashMap<Integer, MJShiftObject> m_objects;
	private IMJShiftObjectDBConverter m_sender;
	public MJShiftObjectSender(String server_identity){
		m_server_identity = server_identity;
		m_objects = new ConcurrentHashMap<Integer, MJShiftObject>();
		m_sender = MJShiftObjectDBConverterFactory.create_sender(m_server_identity);
	}
	
	public MJShiftObject get_object(final int object_id){
		return m_objects.get(object_id);
	}
	
	private MJShiftObject create_object(final L1PcInstance pc, MJEShiftObjectType shift_type){
		final int object_id = pc.getId();
		MJShiftObject sobject = get_object(object_id);
		if(sobject == null){
			String string_identity = String.format("%s%X", MJShiftObjectManager.getInstance().get_home_server_identity(), object_id);
			sobject = MJShiftObject.newInstance()
				.set_source_id(object_id)
				.set_destination_id(0)
				.set_shift_type(shift_type)
				.set_source_character_name(pc.getName())
				.set_source_account_name(pc.getAccountName())
				.set_destination_character_name(string_identity)
				.set_destination_account_name(string_identity);
			m_objects.put(object_id, sobject);
		}
		sobject.set_shift_type(shift_type);
		return sobject;
	}

	public int get_object_count(){
		return m_objects.size();
	}
	
	public void do_getback(){
		MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), false);
		m_objects.clear();
	}
	
	private void leave_clan(final L1PcInstance pc) throws Exception{
		L1Clan clan = pc.getClan();
		if(clan == null)
			return;
		
		String player_name = pc.getName();
		String clan_name = pc.getClanname();
		L1PcInstance clanMember[] = clan.getOnlineClanMember();
		
		for (int i = 0; i < clanMember.length; i++) {
			clanMember[i].sendPackets(new S_ServerMessage(178, player_name, clan_name)); // \f1%0이%1혈맹을 탈퇴했습니다.
		}
		if (pc.isClanBuff()) {
			pc.killSkillEffectTimer(L1SkillId.CLANBUFF_YES);
			pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 450, false));
			pc.setClanBuff(false);
		}
		pc.ClearPlayerClanData(clan);
		clan.removeClanMember(player_name);
	}
	
	public void do_send(final L1PcInstance pc, final MJEShiftObjectType shift_type, final MJShiftObjectReceiver receiver) throws Exception{
		try{
			if(shift_type.equals(MJEShiftObjectType.TRANSFER))
				leave_clan(pc);
			final int object_id = pc.getId();
			boolean is_reconnected = true;
			MJShiftObject sobject = get_object(pc.getId()); 
			if(sobject == null){
				sobject = create_object(pc, shift_type);
				int result = m_sender.work(sobject);
				if(result != IMJShiftObjectDBConverter.CONVERT_SUCCESS){
					System.out.println(String.format("%s(%d)(%s)의 데이터베이스 컨버팅에 실패했습니다.(%d)", pc.getName(), object_id, shift_type.to_name(), result));
					return;
				}
				is_reconnected = false;
			}
			SC_CONNECT_HIBREEDSERVER_NOTI_PACKET pck = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance();
			pck.set_destIP(receiver.get_server_address_bigendian());
			pck.set_destPort(receiver.get_server_port());
			pck.set_domainname(receiver.get_server_address());
			pck.set_interkind(0);
			pck.set_onetimetoken(new MJShiftObjectOneTimeToken(receiver.get_server_identity(), false, sobject, m_server_identity, is_reconnected).to_onetime_token().getBytes());
			pck.set_reservednumber(object_id);
			pc.sendPackets(pck, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET, true);
			final MJShiftObject sobj = sobject;
			if(shift_type.equals(MJEShiftObjectType.TRANSFER) && !pc.isGm()){
				GeneralThreadPool.getInstance().execute(new Runnable(){
					@Override
					public void run(){
						MJShiftObjectHelper.shuffle_character_name(object_id, sobj.get_source_character_name(), sobj.get_source_account_name());
					}
				});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
