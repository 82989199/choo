package l1j.server.MJTemplate.L1Instance;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.PacketHelper.S_ChangeAttr;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SupportInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_WorldPutObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJBytesOutputStream;

public class MJL1LiftGateInstance extends L1Object{
	private static final long serialVersionUID = 1L;
	public static final int LIFT_OPEN 	= 28;
	public static final int LIFT_CLOSE 	= 29;
	
	private ArrayList<ServerBasePacket> _attrs;
	private int 										_gfx;
	private int										_act;
	private int 										_heading;
	private boolean								_isDisposing;
	private ServerBasePacket					_worldPut;
	private boolean								_isWidth;
	private int										_weight;
	
	public MJL1LiftGateInstance(int id, int gfx, int x, int y, int h, short mapid, boolean isWidth, int weight){
		this(id, gfx, x, y, h, mapid, LIFT_CLOSE, isWidth, weight);
	}
		
	public MJL1LiftGateInstance(int id, int gfx, int x, int y, int h, short mapid, int act, boolean isWidth, int weight){
		super();
		setId(id);
		setX(x);
		setY(y);
		setMap(mapid);
		_heading 	= h;
		_gfx 		= gfx;
		_act 		= act;
		_isDisposing= false;
		
		_isWidth	= isWidth;
		_weight = weight;
		_attrs			= new ArrayList<ServerBasePacket>(_weight * 2);
		createAttrs();
		_worldPut = S_WorldPutObject.get(serialize());
		onPass(false);
	}
	
	private void createAttrs(){
		int x = getX(), y = getY();
		if(_isWidth){
			int diff = (_weight - 1) / 2;
			for(int i=0; i<_weight; ++i){
				_attrs.add(S_ChangeAttr.get(x - 1, 	y-diff + i, _isWidth, isClose()));
				_attrs.add(S_ChangeAttr.get(x, 		y-diff + i, _isWidth, isClose()));
			}
		}else{
			int diff = (_weight - 1) / 2;
			for(int i=0; i<_weight; ++i){
				_attrs.add(S_ChangeAttr.get(x - diff + i, y-1, _isWidth, isClose()));
				_attrs.add(S_ChangeAttr.get(x - diff + i,	y, _isWidth, isClose()));			
			}
		}
	}
	
	public int getHeading(){
		return _heading;
	}
	
	public int getGfx(){
		return _gfx;
	}
	
	public int getAction(){
		return _act;
	}
	
	public boolean isClose(){
		return _act == LIFT_CLOSE;
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if(this == null || perceivedFrom == null)
			return;
		
		perceivedFrom.addKnownObject(this);
		if(perceivedFrom.getAI() == null){
			perceivedFrom.sendPackets(_worldPut, false);
			for(ServerBasePacket pck : _attrs)
				perceivedFrom.sendPackets(pck, false);
		}
	}

	@Override
	public void onPerceive(L1SupportInstance perceivedFrom) {
	}
	
	public void up(){
		if(isClose())
			return;
		
		_act		= LIFT_CLOSE;
		swapWorldPut();
		createAttrs();
		notifyChanged();
		onPass(false);
	}
	
	public void down(){
		if(!isClose())
			return;
		
		_act		= LIFT_OPEN;
		swapWorldPut();
		createAttrs();
		if(_isDisposing)
			notifyDispose();
		else
			notifyChanged();
		onPass(true);
	}
	
	private void swapWorldPut(){
		if(!_isDisposing) 
			swap(S_WorldPutObject.get(serialize()));
	}
	
	private void swap(ServerBasePacket pck){
		ServerBasePacket tmp = _worldPut;
		_worldPut = pck;
		clearPacket(tmp);			
	}
	
	private void clearPacket(ServerBasePacket pck){
		if(pck != null){
			pck.clear();
			pck = null;
		}
	}
	
	private void onPass(boolean isPass){
		L1Map m = L1WorldMap.getInstance().getMap(getMapId());
		int x = getX();
		int y = getY();
		if(_isWidth){
			int diff = (_weight - 1) / 2;
			for(int i=0; i<_weight; ++i){
				m.setPassable(x - 1, y-diff + i, isPass);
				m.setPassable(x, 		y-diff + i, isPass);		
			}
		}else{
			int diff = (_weight - 1) / 2;
			for(int i=0; i<_weight; ++i){
				m.setPassable(x - diff + i, y-1, isPass);
				m.setPassable(x - diff + i, y, isPass);		
			}
		}
	}
	
	public void dispose(){
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		_isDisposing = true;
		down();
	}
	
	public void notifyDispose(){
		ServerBasePacket action 	= new S_DoActionGFX(getId(), _act);
		ServerBasePacket remove 	= new S_RemoveObject(this);
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if(pc == null)
				continue;
			
			pc.removeKnownObject(this);
			for(ServerBasePacket attr : _attrs)
				pc.sendPackets(attr, false);
			pc.sendPackets(action, false);
			pc.sendPackets(remove, false);
		}
		clearPacket(action);
		clearPacket(remove);
		for(ServerBasePacket attr : _attrs)
			clearPacket(attr);
	}
	
	public void notifyChanged(){
		ServerBasePacket action 	= new S_DoActionGFX(getId(), _act);
		
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(action, 	false);
			for(ServerBasePacket attr : _attrs)
				pc.sendPackets(attr, false);
		}
		clearPacket(action);
	}
	
	public void broadcast(ServerBasePacket packet){
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		packet.clear();
	}
	
	public void broadcast(ServerBasePacket packet, boolean isClear){
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet, false);
		}
		if(isClear)
			packet.clear();
	}
	
	public void takeClose(long l){
		down();
		GeneralThreadPool.getInstance().schedule(new Runnable(){
			@Override
			public void run(){
				up();
			}
		}, l);
	}
	
	public byte[] serialize(){
		MJBytesOutputStream s = new MJBytesOutputStream(256);
		byte[] 				b = null;
		try{
			s.write(0x08);				// point
			s.writePoint(getX(), getY());
			s.write(0x10);				// objectnumber
			s.writeBit(getId());
			s.write(0x18); 				// objectsprite
			s.writeBit(getGfx());
			s.write(0x20); 				// action
			s.writeBit(getAction());
			s.write(0x28); 				// direction
			s.writeBit(getHeading());
			s.write(0x30); 				// lightRadius
			s.writeBit(15);
			s.write(0x38); 				// objectcount
			s.write(1);
			s.write(0x40); 				// alignment(lawful)
			s.writeBit(0);
			s.write(0x4A);				// desc
			s.write(0x00);
			s.write(0x52); 				// title
			s.write(0x00);
			s.write(0x58);	 			// speed data
			s.write(0x00);
			s.write(0x60); 				// emotion
			s.write(0x00);
			s.write(0x68);				// drunken
			s.writeBit(0x00);
			s.write(0x70); 				// isghost
			s.write(0x00);
			s.write(0x78); 				// isparalyzed
			s.write(0x00);
			s.writeBit(0x80);			// isuser
			s.write(0x00);
			s.writeBit(0x88);			// isinvisible
			s.write(0x00);
			s.writeBit(0x90); 			// ispoisoned
			s.write(0x00);
			s.writeBit(0x98);			// emblemid
			s.write(0x00);
			s.writeBit(0xA2); 			// pledgename
			s.write(0x00);
			s.writeBit(0xAA); 			// mastername
			s.write(0x00);
			s.writeBit(0xB0); 			// altitude
			s.write(0x00);
			s.writeBit(0xB8);			// hitratio
			s.writeBit(-1);
			s.writeBit(0xC0); 			// safelevel
			s.write(0x00);
			s.writeBit(0xD0);	 		// weapon sprite
			s.write(0x00);
			s.writeBit(0xD8);	 		// couplestate
			s.write(0x00);
			s.writeBit(0xE0); 			// boundarylevelindex
			s.writeBit(0x00);
			s.writeBit(0xE8); 			// weakelemental
			s.write(0x00);
			s.writeBit(0xF0);			// manaratio
			s.writeBit(-1);
			s.writeBit(0xF8);			// botindex
			s.write(0x00);
			s.writeBit(0x100); 			// home server no
			s.write(0x00);
			s.writeH(0x00);
			b = s.toArray();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(s != null){
				s.close();
				s.dispose();
				s = null;
			}
		}
		return b;
	}
	
	private static int _instanceType = -1;
	@Override
	public int getL1Type(){
		return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_LIFT : _instanceType;		
	}
}
