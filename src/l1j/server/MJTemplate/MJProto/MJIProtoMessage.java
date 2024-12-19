package l1j.server.MJTemplate.MJProto;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.IO.ProtoInputStream;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.GameClient;

public interface MJIProtoMessage {
	public int getSerializedSize();
	public int getMemorizedSerializeSizedSize();
	public boolean isInitialized();
	public long getInitializeBit();
	public ProtoOutputStream writeTo(MJEProtoMessages message);
	public void writeTo(ProtoOutputStream stream) throws IOException;
	public MJIProtoMessage readFrom(ProtoInputStream stream) throws IOException;
	public MJIProtoMessage readFrom(GameClient clnt, byte[] bytes);
	public MJIProtoMessage copyInstance();
	public MJIProtoMessage reloadInstance();
	public void dispose();
}
