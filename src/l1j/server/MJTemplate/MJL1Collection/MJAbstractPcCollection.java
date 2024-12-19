package l1j.server.MJTemplate.MJL1Collection;

import java.util.Iterator;

import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.Teleport.MJTeleportFilter;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public abstract class MJAbstractPcCollection {
	public abstract Iterator<L1PcInstance> toValuesIterator();
	
	public final void broadcast(String message){
		broadcast(message, true);
	}
	
	public final void broadcast(String[] messages){
		broadcast(messages, true);
	}
	
	public final void broadcast(String message, boolean isClear){
		broadcast(createMessage(message), isClear);
	}
	
	public final void broadcast(String[] messages, boolean isClear){
		broadcast(createMessage(messages), isClear);
	}
	
	public final void broadcast(ProtoOutputStream stream){
		broadcast(stream, true);
	}
	
	public final void broadcast(ProtoOutputStream stream, boolean isClear){
		Iterator<L1PcInstance> it = toValuesIterator();
		while(it.hasNext()){
			L1PcInstance pc = it.next();
			if(pc == null)
				continue;
			
			pc.sendPackets(stream, false);
		}
		if(isClear){
			stream.dispose();
			stream = null;
		}
	}
	
	public final void broadcast(ProtoOutputStream[] streams){
		broadcast(streams, true);
	}
	
	public final void broadcast(ProtoOutputStream[] streams, boolean isClear){
		Iterator<L1PcInstance> it = toValuesIterator();
		while(it.hasNext()){
			L1PcInstance pc = it.next();
			if(pc == null)
				continue;
			
			pc.sendPackets(streams, false);
		}
		if(isClear){
			for(ProtoOutputStream stream : streams)
				stream.dispose();
		}
	}
	
	public final void broadcast(ServerBasePacket sbp){
		broadcast(sbp, true);
	}
	
	public final void broadcast(ServerBasePacket[] sbps){
		broadcast(sbps, true);
	}
	
	public final void broadcast(ServerBasePacket sbp, boolean isClear){
		Iterator<L1PcInstance> it = toValuesIterator();
		while(it.hasNext()){
			L1PcInstance pc = it.next();
			if(pc == null)
				continue;
			
			pc.sendPackets(sbp, false);
		}
		if(isClear){
			sbp.clear();
			sbp = null;	
		}
	}
	
	public final void broadcast(ServerBasePacket[] sbps, boolean isClear){
		Iterator<L1PcInstance> it = toValuesIterator();
		while(it.hasNext()){
			L1PcInstance pc = it.next();
			if(pc == null)
				continue;
			
			pc.sendPackets(sbps, false);
		}
		if(isClear){
			for(int i=sbps.length - 1; i>=0; --i){
				sbps[i].clear();
				sbps[i] = null;
			}
			sbps = null;
		}
	}
	
	protected ServerBasePacket createMessage(String message){
		return new S_SystemMessage(message);
	}
	
	protected ServerBasePacket[] createMessage(String[] messages){
		ServerBasePacket[] sbps = new ServerBasePacket[messages.length];
		for(int i=messages.length - 1; i>=0; --i)
			sbps[i] = new S_SystemMessage(messages[i]);
		return sbps;
	}
	
	public final void teleport(int x, int y, short mapId, MJTeleportFilter filter){
		if(filter == null){
			teleport(x, y, mapId);
			return;
		}
		
		Iterator<L1PcInstance> it = toValuesIterator();
		while(it.hasNext()){
			L1PcInstance pc = it.next();
			if(pc == null)
				continue;
			if(!filter.isFilter(pc))
				pc.do_simple_teleport(x, y, mapId);
		}
	}
	
	public final void teleport(int x, int y, short mapId){
		Iterator<L1PcInstance> it = toValuesIterator();
		while(it.hasNext()){
			L1PcInstance pc = it.next();
			if(pc == null)
				continue;
			
			pc.do_simple_teleport(x, y, mapId);
		}
	}
}
