package l1j.server.MJTemplate.Builder;

import java.io.IOException;

import l1j.server.MJTemplate.PacketHelper.S_BuilderPacket;
import l1j.server.server.utils.MJBytesOutputStream;

public class MJServerPacketBuilder extends MJBytesOutputStream{
	public MJServerPacketBuilder(int capacity){
		super(capacity);
	}
	
	public MJServerPacketBuilder addC(int i) throws IOException{
		write(i);
		return this;
	}
	
	public MJServerPacketBuilder addH(int h) throws IOException{
		writeH(h);
		return this;
	}
	
	public MJServerPacketBuilder addD(int d) throws IOException{
		writeD(d);
		return this;
	}
	
	public MJServerPacketBuilder addS(String s) throws IOException{
		writeS(s);
		return this;
	}
	
	public MJServerPacketBuilder addS2(String s) throws IOException{
		writeS2(s);
		return this;
	}
	
	public MJServerPacketBuilder addBit(long l) throws IOException{
		writeBit(l);
		return this;
	}

	public MJServerPacketBuilder addBytes(byte[] data, int offset, int length) throws IOException{
		write(data, offset, length);
		return this;
	}
	
	public MJServerPacketBuilder addBytes(byte[] data) throws IOException{
		writeBytes(data);
		return this;
	}
	
	public S_BuilderPacket build() throws IOException{
		if(isClose())
			throw new IOException("builder data down.");
		
		byte[] b = toArray();
		return new S_BuilderPacket(b.length + 2, b);
	}
	
	public S_BuilderPacket build(boolean isClose) throws IOException{
		if(isClose())
			throw new IOException("builder data down.");
		
		byte[] b = toArray();
		
		if(isClose)
			dispose();
		return new S_BuilderPacket(b.length + 2, b);
	}
}
