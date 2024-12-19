package l1j.server.MJNetServer.Codec;

import io.netty.channel.ChannelHandlerContext;
import l1j.server.server.utils.MJHexHelper;

/**********************************
 * 
 * MJ Network Server System LogHandler(always test mode).
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJNSLogHandler extends MJNSHandler{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		byte[] b = (byte[])msg;
		System.out.println(String.format("[R]%s %s", ctx.channel().remoteAddress(), MJHexHelper.toString(b, b.length)));
		super.channelRead(ctx, msg);
	}
}
