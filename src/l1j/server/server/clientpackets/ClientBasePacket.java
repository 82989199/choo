package l1j.server.server.clientpackets;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import l1j.server.server.GameClient;

public abstract class ClientBasePacket {

	private static Logger _log = Logger.getLogger(ClientBasePacket.class.getName());
	private byte _decrypt[];
	private int _off;

	public ClientBasePacket(byte abyte0[]) {
		_log.finest("type=" + getType() + ", len=" + abyte0.length);
		_decrypt = abyte0;
		_off = 1;
	}

	public ClientBasePacket(ByteBuffer bytebuffer, GameClient clientthread) {
	}

	public void clear() {
		_decrypt = null;
		_off = 0;
	}

	public int readD() {
		int i = 0;
		try{
			i = _decrypt[_off++] & 0xff;
			i |= _decrypt[_off++] << 8 & 0xff00;
			i |= _decrypt[_off++] << 16 & 0xff0000;
			i |= _decrypt[_off++] << 24 & 0xff000000;
		}catch(Exception e){}
		return i;
	}

	public void readP(int index) {
		_off += index;
	}
	public boolean isRead(int size){
		return _off+size <= _decrypt.length;
	}
	public int readC() {
		int i = _decrypt[_off++] & 0xff;
		return i;
	}

	public int readKH() {
		int i = (_decrypt[_off++] & 0xff) - 128;
		i |= (_decrypt[_off++] & 0xff) << 7;
		return i;
	}

	public int readKCH() {
		int i = (_decrypt[_off++] & 0xff) - 128;
		i |= ((_decrypt[_off++] & 0xff) - 128) << 7;
		i |= (_decrypt[_off++] & 0xff) << 14;
		return i;
	}

	public int readK() {
		int i = (_decrypt[_off++] & 0xff) - 128;
		i |= ((_decrypt[_off++] & 0xff) - 128) << 7;
		i |= ((_decrypt[_off++] & 0xff) - 128) << 14;
		i |= (_decrypt[_off++] & 0xff) << 21;
		return i;
	}

	public int read4(int size) {
		if (size == 0) return 0;
		int i = _decrypt[_off++] & 0x7f;
		if (size == 1) return i;
		if (size >= 2) i |= (_decrypt[_off++] << 8 & 0x7f00) >> 1;
		if (size >= 3) i |= (_decrypt[_off++] << 16 & 0x7f0000) >> 2;
		if (size >= 4) i |= (_decrypt[_off++] << 24 & 0x7f000000) >> 3;
		if (size >= 5) i |= ((long) _decrypt[_off++] << 32 & 0x7f00000000L) >> 4;
		return i;
	}

	public int read_size() {
		int i = 0;
		while (true) {
			if ((_decrypt[_off + i] & 0xff) < 0x80) {
				break;
			} else {
				i++;
			}
		}
		return i + 1;
	}
	
	public byte[] readByteL(int length)
	{	
		byte[] result = new byte[length];
		try
		{
			System.arraycopy(_decrypt, _off, result, 0, length);
			_off += length;
		} catch (Exception e)
		{
		}
		return result;
	}

	public int readH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		return i;
	}

	public int readCH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		i |= _decrypt[_off++] << 16 & 0xff0000;
		return i;
	}

	public double readF() {
		long l = _decrypt[_off++] & 0xff;
		l |= _decrypt[_off++] << 8 & 0xff00;
		l |= _decrypt[_off++] << 16 & 0xff0000;
		l |= _decrypt[_off++] << 24 & 0xff000000;
		l |= (long) _decrypt[_off++] << 32 & 0xff00000000L;
		l |= (long) _decrypt[_off++] << 40 & 0xff0000000000L;
		l |= (long) _decrypt[_off++] << 48 & 0xff000000000000L;
		l |= (long) _decrypt[_off++] << 56 & 0xff00000000000000L;
		return Double.longBitsToDouble(l);
	}

	public String readS() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, "EUC-KR");//MS949
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes("EUC-KR").length + 1;
		} catch (StringIndexOutOfBoundsException e) {
		} catch (Exception e) {
			//	_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}
	
	public String readS(int length)
	{
		String s = null;
		try
		{
			s = new String(_decrypt, _off, length, "EUC-KR");
			_off += length + 1;
		} catch (Exception e)
		{
		
		}
		return s;
	}
	
	public String readS2(int length) {
		String s = null;
		try {
			s = new String(_decrypt, _off, length, "MS949");
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes("MS949").length + 1;
		} catch (StringIndexOutOfBoundsException e) {
		} catch (Exception e) {
			//	_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}

	@SuppressWarnings("finally")
	public String readSS(){ 
		String text = null; 
		int loc = 0; 
		int start = 0; 
		try{ 
			start = _off; 
			while(readH()!=0){ 
				loc += 2; 
			} 
			StringBuffer test = new StringBuffer(); 
			do{ 
				if ((_decrypt[start]&0xff)>=127 || (_decrypt[start+1]&0xff)>=127){ 
					/** 한글 **/ 
					byte[] t = new byte[2]; 
					t[0] = _decrypt[start+1]; 
					t[1] = _decrypt[start]; 
					test.append(new String(t, 0, 2, "MS949")); 
				}else{ 
					/** 영문&숫자 **/ 
					test.append(new String(_decrypt, start, 1, "MS949")); 
				} 
				start+=2; 
				loc-=2; 
			}while(0<loc); 

			text = test.toString(); 
		}catch (Exception e){ 
			text = null; 
		}finally{ 
			return text; 
		} 
	}

	public String readS2() {
		String s = null;
		try {
			int size = this._decrypt[this._off++] & 0xFF;
			s = new String(this._decrypt, this._off, size, "MS949");
			this._off += size;
		} catch (Exception e) {
		}
		return s;
	}


	public byte[] readByte() {
		byte[] result = new byte[_decrypt.length - _off];
		try {
			System.arraycopy(_decrypt, _off, result, 0, _decrypt.length - _off);
			_off = _decrypt.length;
		} catch (Exception e) {
			//	_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return result;
	}

	public int readBit()  {
		int i = 0;
		int j = 0;

		while ((this._decrypt[this._off] & 0xFF) >= 128) {
			i |= (this._decrypt[(this._off++)] & 0xFF ^ 0x80) << 7 * j++;
		}
		return i |= (this._decrypt[(this._off++)] & 0xFF) << 7 * j;
	}
	
	/**
	 * 클라이언트 패킷의 종류를 나타내는 캐릭터 라인을 돌려준다. ("[C] C_DropItem" 등 )
	 */
	public String getType() {
		return "[C] " + this.getClass().getSimpleName();
	}
}
