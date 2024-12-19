package l1j.server.MJNetServer.Util;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import l1j.server.server.GameClient;

/**********************************
 * 
 * MJ Network Server System. data parser util.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJDataParser {
	
	public static long parseLong64(char[] buff, int start){
		return parseLong64(((buff[3 + start] & 0xFF) << 24) | ((buff[2 + start] & 0xFF) << 16) | ((buff[1 + start] & 0xFF) << 8) | (buff[start] & 0xFF));
	}
	
	public static long parseLong64(byte[] buff, int start){
		return parseLong64(((buff[3 + start] & 0xFF) << 24) | ((buff[2 + start] & 0xFF) << 16) | ((buff[1 + start] & 0xFF) << 8) | (buff[start] & 0xFF));
	}
	
	public static long parseLong64(char[] buff){
		return parseLong64(((buff[3] & 0xFF) << 24) | ((buff[2] & 0xFF) << 16) | ((buff[1] & 0xFF) << 8) | (buff[0] & 0xFF));
	}
	
	public static long parseLong64(byte[] buff){
		return parseLong64(((buff[3] & 0xFF) << 24) | ((buff[2] & 0xFF) << 16) | ((buff[1] & 0xFF) << 8) | (buff[0] & 0xFF));
	}
	
	public static long parseLong64(long l){
		return (((l << 32) >>> 32) & 0xFFFFFFFF);
	}
	
	public static long parseLong64(int i){
		return ((((long)i << 32) >>> 32) & 0xFFFFFFFF);
	}
	
	public static long parseLong64(long l1, long l2){
		return parseLong64((int)l1 + (int)l2);
	}
	
	public static int parseInt(InetAddress addr){
		byte[] b = addr.getAddress();
		return 	b[3] 		& 0xff 		| 
				b[2] << 8 	& 0xff00 	|
				b[1] << 16 	& 0xff0000 	|
				b[0] << 24 	& 0xff000000;		
	}
	
	public static int parseInt(GameClient clnt){
		InetSocketAddress 	inetAddr 	= (InetSocketAddress)clnt.getChannel().remoteAddress();
		return parseInt(inetAddr.getAddress());
	}
	
	public static byte[] parseByteBuff(char[] src){
		return parseByteBuff(src, 0, new byte[src.length], 0, src.length);
	}
	
	public static byte[] parseByteBuff(char[] src, byte[] dest){
		return parseByteBuff(src, 0, dest, 0, dest.length);
	}
	
	public static byte[] parseByteBuff(char[] src, int srcPos, byte[] dest, int destPos, int length){
		for(int i = length - 1; i>=0; i--)
			dest[i + destPos] = (byte)(src[i + srcPos] & 0xff);
		
		return dest;
	}
	
	public static char[] parseCharBuff(long[] buf){
		return parseCharBuff(buf, new char[buf.length * 4]);
	}
	
	public static char[] parseCharBuff(long[] buf, char[] charbuf){
		for(int i=0; i<buf.length; ++i){
			charbuf[(i*4)] 		= (char)(buf[i] 		& 0xff);
			charbuf[(i*4) + 1] 	= (char)((buf[i] >> 8) 	& 0xff);
			charbuf[(i*4) + 2] 	= (char)((buf[i] >> 16) & 0xff);
			charbuf[(i*4) + 3] 	= (char)((buf[i] >> 24) & 0xff);
		}
		return charbuf;
	}
	
	public static char[] parseCharBuff(byte[] src){
		return parseCharBuff(src, src.length);
	}
	
	public static char[] parseCharBuff(byte[] src, int size){
		return parseCharBuff(src, 0, new char[size], 0, size);
	}
	
	public static char[] parseCharBuff(byte[] src, char[] dest){
		return parseCharBuff(src, 0, dest, 0, dest.length);
	}
	
	public static char[] parseCharBuff(byte[] src, int srcPos, char[] dest, int destPos, int length){
		for(int i = length - 1; i>=0; i--)
			dest[i + destPos] = (char)src[i + srcPos];
		return dest;
	}
	
	public static int rotr(int v, int n){
		return (v >>> n) | (v << (32-n));
	}
	
	public static int rotr(long v, int n){
		return rotr((int)v, n);
	}
}
