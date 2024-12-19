package l1j.server.MJNetServer.Buffer;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import server.KeyPacket;

/**********************************
 * 
 * MJ Network Server System ByteBuffer Factory.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJByteBufferFactory {
	private static KeyPacket _kp;
	public static ByteBuf createKey(ChannelHandlerContext ctx){
		if(_kp == null)
			_kp = new KeyPacket();
		ByteBuf buf = ctx.alloc().buffer(_kp.getLength());
		buf.writeShortLE(_kp.getLength());
		buf.writeBytes(_kp.getBytes());
		return buf;
	}
	
	@SuppressWarnings("deprecation")
	public static ByteBuf create(boolean isKernel){
		ByteBuf buf = isKernel ? ByteBufAllocator.DEFAULT.directBuffer() : ByteBufAllocator.DEFAULT.heapBuffer();
		return buf.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	@SuppressWarnings("deprecation")
	public static ByteBuf create(boolean isKernel, int capacity){
		ByteBuf buf = isKernel ? ByteBufAllocator.DEFAULT.directBuffer(capacity) : ByteBufAllocator.DEFAULT.heapBuffer(capacity);
		return buf.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public static void release(ByteBuf buf){
		if(buf != null)
			ReferenceCountUtil.release(buf);
	}
}
