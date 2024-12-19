package l1j.server.MJNetServer.Codec.Cryptor;

import io.netty.buffer.ByteBuf;
import l1j.server.MJNetServer.Util.MJDataParser;

/**********************************
 * 
 * MJ Network Server System. Blowfish enc/dec for Lineage packet.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJCryptor {
	public int		overPending;
	
	private long[] 	_dk;
	private byte[]	_clntHash;
	private char[]	_db;
	
	public MJCryptor(){
		_dk 		= new long[2];
		_clntHash 	= new byte[256];
		_db			= new char[8];
	}
	
	public void initKey(long l){
		long al[] = {l, 0x930FD7E2L};
		MJKeyCreator.get(al);
		
		_dk[0] = al[0];
		_dk[1] = al[1];
		
		char[] tk = MJDataParser.parseCharBuff(al);
		initEncHash(tk);
	}
	
	private void initEncHash(char[] ck){
		int k = 0;
		for(int i=255; i>=0; i--)
			_clntHash[i] = (byte)i;
		for(int i=0; i<256; ++i){
			k = (_clntHash[i] + k + ck[i % 8]) & 0xff;
			byte tmp = _clntHash[k];
			_clntHash[k] = _clntHash[i];
			_clntHash[i] = tmp;
		}
	}
	
	public byte[] decrypt(byte[] data, int size){
		char[] ac = MJDataParser.parseCharBuff(data, 0, new char[size], 0, size);
		decwork(ac, size);
		long l = MJDataParser.parseLong64(ac, 4);
		_dk[0] ^= l;
		_dk[1] = MJDataParser.parseLong64(_dk[1], 679411651L);
		MJDataParser.parseByteBuff(ac, 4, data, 0, data.length - 4);
		data[data.length - 1] = 0;
		data[data.length - 2] = 0;
		data[data.length - 3] = 0;
		data[data.length - 4] = 0;
		return data;
	}
	
	private char[] decwork(char[] ac, int size){
		_db 		= MJDataParser.parseCharBuff(_dk, _db);
		char c		= ac[3];
		ac[3] 		= (char)(ac[3] ^ _db[2]);
		char c1		= ac[2];
		ac[2]		= (char)(ac[2] ^ c ^ _db[3]);
		char c2		= ac[1];
		ac[1]		= (char)(ac[1] ^ c1 ^ _db[4]);
		char c3		= (char)(ac[0] ^ c2 ^ _db[5]);
		ac[0]		= (char)(c3 ^ _db[0]);
		for(int i=1; i<size; ++i){
			char c4 = ac[i];
			ac[i] 	= (char)(ac[i] ^ _db[i & 7] ^ c3);
			c3 		= c4;
		}
		return ac;
	}
	
	public void encrypt(byte[] data, ByteBuf buf){
		int k = 0, j, m, o;
		buf.writeShortLE(data.length + 2);
		for(int i=0; i<data.length; ++i){
			m = i + 1;
			o = m / 256;
			m -= o * 256;

			j = m & 0xFF;
			k += _clntHash[m];
			k &= 255;

			byte tk = _clntHash[j];
			_clntHash[j] = _clntHash[k];
			_clntHash[k] = tk;

			int b3 = _clntHash[j];
			int b4 = _clntHash[k];

			b3 += b4;
			b3 &= 255;
			buf.writeByte(((data[i] ^ _clntHash[b3]) & 0xff));
		}
	}
}

