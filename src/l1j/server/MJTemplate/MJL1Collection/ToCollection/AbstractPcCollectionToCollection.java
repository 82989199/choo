package l1j.server.MJTemplate.MJL1Collection.ToCollection;

import java.util.Iterator;

import l1j.server.MJTemplate.Interface.IOneToHandler;
import l1j.server.MJTemplate.MJL1Collection.MJPcList;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.Teleport.MJTeleportFilter;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public abstract class AbstractPcCollectionToCollection<T extends MJPcList>{
	public abstract Iterator<T> to_values_iterator();
	
	public final void broadcast(String message){
		broadcast(message, true);
	}
	
	public final void broadcast(String[] messages){
		broadcast(messages, true);
	}
	
	public final void broadcast(String message, boolean is_clear){
		broadcast(createMessage(message), is_clear);
	}
	
	public final void broadcast(String[] messages, boolean is_clear){
		broadcast(create_message(messages), is_clear);
	}
	
	public final void broadcast(ServerBasePacket sbp, boolean is_clear){
		Iterator<T> it = to_values_iterator();
		while(it.hasNext()){
			MJPcList list = it.next();
			if(list == null)
				continue;
			
			list.broadcast(sbp, false);
		}
		if(is_clear){
			sbp.clear();
			sbp = null;
		}
	}
	
	public final void broadcast(ServerBasePacket[] sbps, boolean is_clear){
		Iterator<T> it = to_values_iterator();
		while(it.hasNext()){
			MJPcList list = it.next();
			if(list == null)
				continue;
			
			list.broadcast(sbps, false);
		}
		if(is_clear){
			for(int i=sbps.length - 1; i>=0; --i){
				sbps[i].clear();
				sbps[i] = null;
			}
			sbps = null;
		}
	}
	
	public final void broadcast(ProtoOutputStream stream, boolean is_clear){
		Iterator<T> it = to_values_iterator();
		while(it.hasNext()){
			MJPcList list = it.next();
			if(list == null)
				continue;
			
			list.broadcast(stream, false);
		}
		if(is_clear){
			stream.dispose();
			stream = null;
		}
	}
	
	public final void broadcast(ProtoOutputStream[] streams, boolean is_clear){
		Iterator<T> it = to_values_iterator();
		while(it.hasNext()){
			MJPcList list = it.next();
			if(list == null)
				continue;
			
			list.broadcast(streams, false);
		}
		if(is_clear){
			for(ProtoOutputStream stream : streams)
				stream.dispose();
			streams = null;
		}
	}
	
	
	public final void teleport(int x, int y, short mapId, MJTeleportFilter filter){
		Iterator<T> it = to_values_iterator();
		while(it.hasNext()){
			MJPcList list = it.next();
			if(list == null)
				continue;
			
			list.teleport(x, y, mapId, filter);
		}
	}
	
	public final void teleport(int x, int y, short mapId){
		Iterator<T> it = to_values_iterator();
		while(it.hasNext()){
			MJPcList list = it.next();
			if(list == null)
				continue;
			
			list.teleport(x, y, mapId);
		}
	}
	
	public void traver_work(IOneToHandler<L1PcInstance>[] handlers){
		Iterator<T> it = to_values_iterator();
		try{
			int idx = -1;
			while(it.hasNext()){
				++idx;
				MJPcList list = it.next();
				if(list == null)
					continue;
				
				for(L1PcInstance pc : list){
					for(IOneToHandler<L1PcInstance> handler : handlers)
						handler.handle(idx, pc);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void traver_work(IOneToHandler<L1PcInstance> handler){
		Iterator<T> it = to_values_iterator();
		try{
			int idx = -1;
			while(it.hasNext()){
				++idx;
				MJPcList list = it.next();
				if(list == null)
					continue;
				
				for(L1PcInstance pc : list)
					handler.handle(idx, pc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected ServerBasePacket createMessage(String message){
		return new S_SystemMessage(message);
	}
	
	protected ServerBasePacket[] create_message(String[] messages){
		ServerBasePacket[] sbps = new ServerBasePacket[messages.length];
		for(int i=messages.length - 1; i>=0; --i)
			sbps[i] = createMessage(messages[i]);
		return sbps;
	}
}
