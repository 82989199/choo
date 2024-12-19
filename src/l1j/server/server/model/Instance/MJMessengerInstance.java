package l1j.server.server.model.Instance;
import l1j.server.DogFight.MJDogFightSettings;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.model.L1Attack;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.templates.L1Npc;

public class MJMessengerInstance extends L1NpcInstance{
	private static final long serialVersionUID = 1L;

	private String m_current_message;
	private boolean m_is_ghost;
	public MJMessengerInstance(L1Npc template) {
		super(template);
		m_is_ghost = false;
		m_current_message = "";
	}

	public String get_current_message(){
		return m_current_message;
	}
	public void set_current_message(String message){
		m_current_message = message;
	}
	public boolean is_ghost(){
		return m_is_ghost;
	}
	public void set_is_ghost(boolean is_ghost){
		m_is_ghost = is_ghost;
	}
	
	private SC_WORLD_PUT_OBJECT_NOTI make_object_packet(){
		return SC_WORLD_PUT_OBJECT_NOTI.new_namechat_isntance(this, get_current_message(), is_ghost());
	}
	
	public void broadcast_message() {
		broadcastPacket(new S_ChangeName(getId(), get_current_message()));
		/// broadcastPacket(make_object_packet(),
		/// MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, false);
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null)
			return;

		if(getNpcId() == 50000063 || getNpcId() == MJDogFightSettings.MESSENGER_NPC_ID)
			set_is_ghost(true);

		getMap().setPassable(getLocation(), true);
		perceivedFrom.addKnownObject(this);
		if (perceivedFrom.getAI() == null) {
			perceivedFrom.sendPackets(make_object_packet(), MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true);
		}
	}
	
	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		attack.calcHit();
		attack.action();
	}
	
	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		setActived(false);
		startAI();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
	}
	@Override
	public void onFinalAction(L1PcInstance player, String action) {
	}

	public void doFinalAction(L1PcInstance player) {
	}
}
