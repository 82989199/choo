package l1j.server.server.serverpackets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public abstract class ServerBasePacket {

	private int OpKey; // opcode Key

	private boolean isKey = true;

	ByteArrayOutputStream 	_bao;
	private byte[]			_byte;
	protected ServerBasePacket() {
		_bao = new ByteArrayOutputStream(128);
	}
	
	protected ServerBasePacket(int capacity) {
		_bao = new ByteArrayOutputStream(capacity);
	}
	
	public void clear() {
		if(_bao == null)
			return;
			
		try {
			_bao.reset();
			_bao.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_bao = null;
	}

	// Key
	private void setKey(int i) {
		OpKey = i;
	}

	private int getKey() {
		return OpKey;
	}

	protected void writeD(int value) {
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);
		_bao.write(value >> 16 & 0xff);
		_bao.write(value >> 24 & 0xff);
	}
	
	protected void writeL(long value){
		_bao.write((byte)(value & 0xff));
		_bao.write((byte)(value >> 8 & 0xff));
		_bao.write((byte)(value >> 16 & 0xff));
		_bao.write((byte)(value >> 24 & 0xff));	
		_bao.write((byte)(value >> 32 & 0xff));		
		_bao.write((byte)(value >> 40 & 0xff));		
		_bao.write((byte)(value >> 48 & 0xff));		
		_bao.write((byte)(value >> 56 & 0xff));		
	}

	protected void writeH(int value) {
		_bao.write(value & 0xff);
		_bao.write(value >> 8 & 0xff);
	}

	protected void writeC(int value) {
		_bao.write(value & 0xff);
		// 옵코드 wirteC 첫번째 호출만 셋팅...
		if (isKey) {
			setKey(value);
			isKey = false;
		}
	}

	protected void writeSU16(String text) {
		try {
			if (text != null) {
				_bao.write(text.getBytes("UTF-16LE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		_bao.write(0);
		_bao.write(0);
	}


	protected void writeK(int value) {
		int valueK = (int) (value / 128);
		if(valueK == 0){
			_bao.write(value);
		}else if(valueK <= 127){
			_bao.write((value & 0x7f) + 128);
			_bao.write(valueK);
		}else if(valueK <= 16383){
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(valueK / 128);
		}else if(valueK <= 2097151){
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(((valueK / 128) & 0x7f) + 128);
			_bao.write(valueK / 16384);
		}else{
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(((valueK / 128) & 0x7f) + 128);
			_bao.write(((valueK / 16384) & 0x7f) + 128);
			_bao.write(valueK / 2097152);
		}
	}

	public int bitlengh(int obj) {
		int length = 0;
		if (obj < 0) {
			BigInteger b = new BigInteger("18446744073709551615");
			while (BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0) {
				length++;
			}
			length++;
		} else {
			if (obj <= 127) {
				length = 1;
			} else if (obj <= 16383) {
				length = 2;
			} else if (obj <= 2097151) {
				length = 3;
			} else if (obj <= 268435455) {
				length = 4;
			} else if ((long) obj <= 34359738367L) {
				length = 5;
			}
		}
		return length;
	}

	/* 한국 옵코드 추가 패킷 */
	protected void write4bit(int value)
	{
		if (value <= 127) {
			this._bao.write(value & 0x7F);
		} else if (value <= 16383) {
			this._bao.write(value & 0x7F | 0x80);
			this._bao.write(value >> 7 & 0x7F);
		} else if (value <= 2097151) {
			this._bao.write(value & 0x7F | 0x80);
			this._bao.write(value >> 7 & 0x7F | 0x80);
			this._bao.write(value >> 14 & 0x7F);
		} else if (value <= 268435455) {
			this._bao.write(value & 0x7F | 0x80);
			this._bao.write(value >> 7 & 0x7F | 0x80);
			this._bao.write(value >> 14 & 0x7F | 0x80);
			this._bao.write(value >> 21 & 0x7F);
		} else if (value <= 34359738367L) {
			this._bao.write(value & 0x7F | 0x80);
			this._bao.write(value >> 7 & 0x7F | 0x80);
			this._bao.write(value >> 14 & 0x7F | 0x80);
			this._bao.write(value >> 21 & 0x7F | 0x80);
			this._bao.write(value >> 28 & 0x7F);
		}
	}

	protected void writeBit(long value) {
		if (value < 0L) {
			String stringValue = Integer.toBinaryString((int) value);
			value = Long.valueOf(stringValue, 2).longValue();
		}
		int i = 0;
		while (value >> 7 * (i + 1) > 0L) {
			_bao.write((int) ((value >> 7 * i++) % 128L | 0x80));
		}
		_bao.write((int) ((value >> 7 * i) % 128L));
	}
	
	protected void writeBit(int x, int y) {
		String value = new StringBuilder().append(Integer.toBinaryString(y)).append("").append(x < 32768 ? "0" : "")
				.append(Integer.toBinaryString(x)).toString();
		writeBit(Long.valueOf(value, 2).longValue());
	}

	protected void writePoint(int x, int y){
		int pt 	= 	(y << 16) 	& 0xffff0000;
		pt 		|= 	(x 			& 0x0000ffff);
		writeBit(pt);
	}
	
	protected void writeB(boolean b){
		writeC(b ? 0x1 : 0x0);
	}
	
	protected void writeB(Object o){
		writeC(o != null ? 0x01 : 0x00);
	}
	
	protected int getBitSize(long value) {
		if (value < 0L) {
			String stringValue = Integer.toBinaryString((int) value);
			value = Long.valueOf(stringValue, 2).longValue();
		}
		int size = 0;
		while (value >> (size + 1) * 7 > 0L) {
			size++;
		}
		size++;

		return size;
	}

	protected void write7B(long value) {
		int i = 0;
		BigInteger b = new BigInteger("18446744073709551615");

		while (BigInteger.valueOf(value).and(b).shiftRight((i + 1) * 7).longValue() > 0) {
			_bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(0x80)).or(BigInteger.valueOf(0x80)).intValue());
		}
		_bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(0x80)).intValue());
	}

	public int size7B(int obj) {
		int length = 0;
		if (obj < 0) {
			BigInteger b = new BigInteger("18446744073709551615");
			while (BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0) {
				length++;
			}
			length++;
		} else {
			if (obj <= 127) {
				length = 1;
			} else if (obj <= 16383) {
				length = 2;
			} else if (obj <= 2097151) {
				length = 3;
			} else if (obj <= 268435455) {
				length = 4;
			} else if ((long) obj <= 34359738367L) {
				length = 5;
			}
		}
		return length;
	}

	protected void writeP(int value) {
		_bao.write(value);
	}

	protected void writeF(double org) {
		long value = Double.doubleToRawLongBits(org);
		_bao.write((int) (value & 0xff));
		_bao.write((int) (value >> 8 & 0xff));
		_bao.write((int) (value >> 16 & 0xff));
		_bao.write((int) (value >> 24 & 0xff));
		_bao.write((int) (value >> 32 & 0xff));
		_bao.write((int) (value >> 40 & 0xff));
		_bao.write((int) (value >> 48 & 0xff));
		_bao.write((int) (value >> 56 & 0xff));
	}

	protected void writeS(String text) {
		try {
			if (text != null) {
				_bao.write(text.getBytes("MS949"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		_bao.write(0);
	}

	protected void writeSS(String text) {
		try {
			if (text != null) {
				byte[] test = text.getBytes("MS949");
				for (int i = 0; i < test.length;) {
					if ((test[i] & 0xff) >= 0x7F) {
						/** 한글 **/
						_bao.write(test[i + 1]);
						_bao.write(test[i]);
						i += 2;
					} else {
						/** 영문&숫자 **/
						_bao.write(test[i]);
						_bao.write(0);
						i += 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		_bao.write(0);
		_bao.write(0);
	}

	protected void writeByte(byte[] text) {
		try {
			if (text != null) {
				_bao.write(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getLength() {
		return _bao.size() + 2;
	}

	public byte[] getBytes() {
		if(_byte == null)
			_byte = _bao.toByteArray();
		return _byte;
	}

	protected void writeS2(String text) {
		try {
			if (text != null && !text.isEmpty()) {
				byte[] name = text.getBytes("MS949");
				_bao.write(name.length & 0xff);
				if (name.length > 0) {
					_bao.write(name);
				}
			} else {
				_bao.write(0 & 0xff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public abstract byte[] getContent() throws IOException;

	/**
	 * 서버 패킷의 종류를 나타내는 캐릭터 라인을 돌려준다. ("[S] S_WhoAmount" 등 )
	 */
	public String getType() {
		return "";
	}

	public String toString() {
		// getType() 의 리턴이 "" 이라면 빈값 아니면 패킷이름 + 코드값 출력
		// [옵코드] 패킷명
		String sTemp = getType().equals("") ? "" : "[" + getKey() + "] " + getType();
		return sTemp;
	}

	public void writeBytes(byte[] data) throws Exception{
		if(data == null || data.length <= 0)
			writeC(0x00);
		else{
			writeBit(data.length);
			_bao.write(data);
		}
	}
	protected void writeS2(ByteArrayOutputStream bao, String text) {
		try {
			if (text != null && !text.isEmpty()) {
				byte[] name = text.getBytes("MS949");
				bao.write(name.length & 0xff);
				if (name.length > 0) {
					bao.write(name);
				}
			} else {
				bao.write(0 & 0xff);
			}
		} catch (Exception e) {
			
		}
	}
	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85,
			0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90,
			0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b,
			0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6,
			0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
			0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc,
			0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7,
			0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2,
			0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd,
			0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8,
			0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3,
			0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe,
			0xff };
	public void byteWrite(ByteArrayOutputStream _bao, long value) {
		long temp = value / 128;
		if (temp > 0) {
			_bao.write((int)(hextable[(int) (value % 128)] & 0xff));
			while (temp >= 128) {
				_bao.write((int)(hextable[(int) (temp % 128)] & 0xff));
				temp = temp / 128;
			}
			if (temp > 0)
				_bao.write((int) (temp & 0xff));
		} else {
			if (value == 0) {
				_bao.write(0);
			} else {
				_bao.write((int) (hextable[(int) value] & 0xff));
				_bao.write(0);
			}
		}
	}
	protected void writeByte(ByteArrayOutputStream __bao, byte[] text) {
		try {
			if (text != null) {
				__bao.write(text);
			}
		} catch (Exception e) {
			//_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	public void byteWrite(long value) {
		long temp = value / 128;
		if (temp > 0) {
			writeC(hextable[(int) value % 128]);
			while (temp >= 128) {
				writeC(hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				writeC((int) temp);
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(hextable[(int) value]);
				writeC(0);
			}
		}
	}
	
	public void close() {
		if(_byte != null)
			_byte = null;
		try {
			_bao.close();
		} catch (Exception e) {
		}
	}

}
