package l1j.server.MJNetServer.Codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJNetServer.Codec.Cryptor.MJCryptor;

/**********************************
 * 
 * MJ Network Server System Decoder.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJNSDecoder extends ByteToMessageDecoder{
	
//	final String auth_key = "cF6qFLWFplWaTT6uM1cVc9WtmOL-nniHRmjVwMIkJIMBUJYp_cXGZU8KB4Sm"; // 린카 인증
//	final int auth_key_size = auth_key.length(); // 린카인증 
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		int readable = msg.readableBytes();
		
		//린카 인증 START
//		if(readable == auth_key_size)
//		{
//			byte[] data = new byte[readable];
//			msg.readBytes(data, 0, readable);
//			
//			String _d = new String(data);
//			_d = _d.trim();
//			
//			if(auth_key.equals(_d)) 
//			{
//				byte[] aa = new byte[1];
//				aa[0] = 0x11;
//				out.add(aa);
//				return;
//			}
//          msg.resetReaderIndex();
//		}	
		//린카 인증 END
		
		
		
		
		if(readable < 2)
			return;
		
		MJCryptor cryptor = MJNSHandler.getCryptor(ctx);
		if(cryptor == null){
			ctx.close();
			return;
		}
		
		try{
			while(readable > 2){
				msg.markReaderIndex();
				int len = msg.readUnsignedShortLE() - 2;
				readable -= 2;
				if(isBad(cryptor, len)){
					MJNSHandler.print(ctx, "■ 패킷공격(의심) ■");
					ctx.close();
					return;
				}
				cryptor.overPending = 0;
				if(readable < len){
					msg.resetReaderIndex();
					break;
				}
				
				byte[] data = new byte[len];
				msg.readBytes(data, 0, len);
				data = cryptor.decrypt(data, len);
				
				out.add(data);
				readable -= len;
			}
		}catch(Exception e){
			MJNSHandler.print(ctx, "예외정보.");
			e.printStackTrace();
		}
	}
	
	private boolean isBad(MJCryptor cryptor, int len){
		return (len < 2) || (len > MJNetSafeLoadManager.NS_PACKET_MAXSIZE && ++cryptor.overPending >= MJNetSafeLoadManager.NS_PACKET_MAXOVER_COUNT);
	}
}
