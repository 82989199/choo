package l1j.server.MJNetServer.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import l1j.server.MJNetServer.Codec.Cryptor.MJCryptor;

/**********************************
 * 
 * MJ Network Server System Encoder.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJNSEncoder extends MessageToByteEncoder<byte[]>{
	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
		MJCryptor cryptor = MJNSHandler.getCryptor(ctx);
		if(cryptor == null){
			ctx.close();
			return;
		}

		cryptor.encrypt(msg, out);
	}
}
