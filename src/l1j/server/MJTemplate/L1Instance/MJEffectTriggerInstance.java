package l1j.server.MJTemplate.L1Instance;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJEffectTriggerInstance extends L1Object{

	public static MJEffectTriggerInstance newInstance(int identity, int sprite, int action, int x, int y, short map_id){
		MJEffectTriggerInstance trigger = new MJEffectTriggerInstance();
		trigger.set_identity(identity);
		trigger.set_sprite(sprite);
		trigger.set_action(action);
		trigger.setId(IdFactory.getInstance().nextId());
		trigger.setX(x);
		trigger.setY(y);
		trigger.setMap(map_id);
		L1World.getInstance().storeObject(trigger);
		L1World.getInstance().addVisibleObject(trigger);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(trigger)) {
			trigger.onPerceive(pc);
		}
		return trigger;
	}
	
	private static final long serialVersionUID = 1L;
	private int m_identity;
	private int m_sprite;
	private int m_action;
	
	private MJEffectTriggerInstance(){
		super();
	}
	public int get_identity(){
		return m_identity;
	}
	public void set_identity(int identity){
		m_identity = identity;
	}
	public int get_sprite(){
		return m_sprite;
	}
	public void set_sprite(int sprite){
		m_sprite = sprite;
	}
	public int get_action(){
		return m_action;
	}
	public void set_action(int action){
		m_action = action;
	}
	public int get_long_location() {
		int pt = (getX() << 16) & 0xffff0000;
		pt |= (getY() & 0x0000ffff);
		return pt;
	}
	public int get_long_location_reverse() {
		int pt = (getY() << 16) & 0xffff0000;
		pt |= (getX() & 0x0000ffff);
		return pt;
	}
	
	public void do_move(int x, int y, int h){
		setX(x);
		setY(y);
		broadcast(new S_MoveCharPacket(getId(), x, y, h), true);
	}
	
	public void do_action(int action){
		broadcast(new S_DoActionGFX(getId(), action), true);
	}
	
	public void broadcast(ServerBasePacket pck, boolean is_clear){
		for(L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)){
			if(pc == null)
				continue;

			pc.sendPackets(pck, false);
		}
		if(is_clear)
			pck.clear();
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if(perceivedFrom == null)
			return;
		
		perceivedFrom.addKnownObject(this);
		if(perceivedFrom.getAI() == null){
			SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(this);
			perceivedFrom.sendPackets(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI);
		}
	}
	
	public void dispose(){
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		S_RemoveObject remove = new S_RemoveObject(this);
		for(L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)){
			if(pc == null)
				continue;
			
			pc.removeKnownObject(this);
			pc.sendPackets(remove, false);
		}
		remove.clear();
	}
}
