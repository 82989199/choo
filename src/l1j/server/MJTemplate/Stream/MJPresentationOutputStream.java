package l1j.server.MJTemplate.Stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.MJBytesInputStream;

public class MJPresentationOutputStream extends OutputStream{
	public static MJPresentationOutputStream newInstance(){
		return new MJPresentationOutputStream(4096);
	}
	
	public static MJPresentationOutputStream newInstance(int capacity){
		return new MJPresentationOutputStream(capacity);
	}
	
	private byte[] 	_buf;		// 버퍼
	private int		_idx;		// 현재 버퍼가 가르키고 있는 인덱스
	private int		_capacity;	// 버퍼의 크기 / 확장될 크기
	private boolean _isClosed;	// 스트림이 닫혔는지?
	private boolean	_isShared;	// 스트림의 데이터가 공유가능한지
	
	protected MJPresentationOutputStream(int capacity){
		_isShared	= false;
		_isClosed	= false;
		_capacity 	= capacity;
		_buf		= new byte[_capacity];
	}
	
	/** 스트림의 크기를 재조정한다. **/
	private void realloc(int capacity){
		_capacity 	= capacity;
		byte[] tmp 	= new byte[_capacity];
		System.arraycopy(_buf, 0, tmp, 0, _idx);
		_buf 		= tmp;
		_isShared 	= false;
	}
	
	/** 데이터를 쓴다. **/
	@Override
	public void write(int i) throws IOException {
		if(_isClosed)
			throw new IOException("stream is Closed...");
		
		if(_idx >= _capacity)
			realloc(_capacity*2+1);
		
		_buf[_idx++] = (byte)(i & 0xff);
	}
	
	/** 데이터를 쓴다. **/
	public void write(byte[] data, int offset, int length) throws IOException{
		if(data == null)
			throw new NullPointerException();
		
		if(offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		
		if(_isClosed)
			throw new IOException("stream is Closed...");
		
		int capacity = _capacity;
		while(_idx + length > capacity)
			capacity = capacity*2+1;
		if(capacity > _capacity)
			realloc(capacity);
		
		System.arraycopy(data, offset, _buf, _idx, length);
		_idx += length;
	}
	
	
	public void writeBytes(byte[] data) throws IOException{
		if(data == null || data.length <= 0)
			write(0);
		else{
			writeBit(data.length);
			write(data);
		}
	}
	
	/** short형(2byte) 데이터를 쓴다. **/
	public void writeH(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	}
	
	/** int형(4byte) 데이터를 쓴다. **/
	public void writeD(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	    write(i >> 16 	& 0xFF);
	    write(i >> 24 	& 0xFF);
	}
	
	public void writeBit(long value) throws IOException
	{
		if (value < 0L) {
			String str = Integer.toBinaryString((int)value);
			value = Long.valueOf(str, 2).longValue();
		}
		int i = 0;
		while (value >> 7 * (i + 1) > 0L)
			write((int)((value >> 7 * i++) % 128L | 0x80));
		write((int)((value >> 7 * i) % 128L));
	}
	
	public void writeS(String text) throws IOException{
		writeS(text, "MS949");
	}
	
	public void writeS(String text, String encoding) throws IOException{
		if(text != null){
			byte[] b = text.getBytes(encoding);
			write(b, 0, b.length);
		}
		write(0);
	}
	
	public void writeS2(String text) {
		try {
			if (text != null && !text.isEmpty()) {
				byte[] name = text.getBytes("MS949");
				write(name.length & 0xff);
				if (name.length > 0) {
					write(name);
				}
			} else {
				write(0 & 0xff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeSForMultiBytes(String text) throws IOException{
		writeSForMultiBytes(text, "MS949");
	}
	
	public void writeSForMultiBytes(String text, String encoding) throws IOException{
		if(text != null){
			byte[] b = text.getBytes(encoding);
			int i = 0;
			while(i < b.length){
				if((b[i] & 0xff) >= 0x7f){
					write(b[i + 1]);
					write(b[i]);
					i += 2;
				}else{
					write(b[i]);
					write(0);
					i += 1;
				}
			}
		}
		write(0);
		write(0);
	}
	
	/** 새로운 outputStream에 쓴다. **/
	public void writeTo(OutputStream out) throws IOException{
		out.write(_buf, 0, _idx);
	}
	
	public void writeB(boolean b) throws IOException{
		write(b ? 0x01 : 0x00);
	}
	
	public void writeB(Object o) throws IOException{
		write(o != null ? 0x01 : 0x00);
	}
	
	public void writePoint(int x, int y) throws Exception{
		int pt 	= 	(y << 16) 	& 0xffff0000;
		pt 		|= 	(x 			& 0x0000ffff);
		writeBit(pt);
	}
	
	/** InputStream으로 만든다. **/
	public InputStream toInputStream(){
		_isShared = true;
		return new MJBytesInputStream(_buf, 0, _idx);
	}
	
	/** 초기화 **/
	public void reset() throws IOException{
		if(_isClosed)
			_isClosed = false;
		
		if(_isShared){
			_buf = new byte[_capacity];
			_isShared = false;
		}
		
		_idx = 0;
	}
	
	/** 스트림을 닫는다. **/
	public void close(){
		_isClosed = true;
	}
	
	public void dispose(){
		_isClosed 	= true;
		_isShared	= false;
		_buf 		= null;
	}
	
	/** 스트림의 내용을 배열로 반환한다. **/
	public byte[] toArray(){
		byte[] result = new byte[_idx];
		System.arraycopy(_buf, 0, result, 0, _idx);
		return result;
	}
	
	public boolean isClose(){
		return _isClosed;
	}
	
	public MJPresentationOutputStream write_weapon_info(L1Item item, int weight) throws IOException{
		write(0x01);
		write(item.getDmgSmall());
		write(item.getDmgLarge());
		write(item.getMaterial());
		writeD(weight);
		return this;
	}
	
	public MJPresentationOutputStream write_weapon_etc_info(L1Item item, int durability) throws IOException{
		if(item.isTwohandedWeapon())
			write(0x04);
		if(durability != 0){
			write(0x03);
			write(durability);
		}
		
		write(0x07);
		write((item.isUseRoyal() ? 1 : 0)
				| (item.isUseKnight() ? 2 : 0)
				| (item.isUseElf() ? 4 : 0)
				| (item.isUseMage() ? 8 : 0)
				| (item.isUseDarkelf() ? 16 : 0)
				| (item.isUseDragonKnight() ? 32 : 0)
				| (item.isUseBlackwizard() ? 64 : 0)
				| (item.isUse전사() ? 128 : 0));
		
		write(130);
		if (!item.isTradable()) {
			writeD(6);
		} else {
			writeD(7);
		}
		
		write(132);
		writeD(3);
		
		if(item.get_canbedmg() == 0) {
			write(131);
			writeD(1);	// 비손상
		}
		
		if (isUndeadDmg(item)) {
			write(114);
			writeD(1);	// 언데드
		}
		
		return this;
	}
	
	public MJPresentationOutputStream write_weapon_add_damage(int enchant_level) throws IOException{
		write(0x02);
		write(enchant_level);
		return this;
	}
	
	public MJPresentationOutputStream write_weapon_add_damage(int small_damage, int large_damage) throws IOException{
		write(0x6B);
		write(small_damage);
		write(large_damage);
		return this;
	}
	
	
	public MJPresentationOutputStream writeAbilityPierce(int pierce) throws IOException{
		write(122);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream writeSpiritPierce(int pierce) throws IOException{
		write(123); //
		write(pierce);
		return this;
		
		// 122 기술 124 용언 125 공포 126 전체
	}
	
	public MJPresentationOutputStream writeDragonSpellPierce(int pierce) throws IOException{
		write(124);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream writeFearPierce(int pierce) throws IOException{
		write(125);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream writeAllPierce(int pierce) throws IOException{
		write(121);
		write(pierce);
		return this;
	}
	
	public MJPresentationOutputStream write_safeenchant(int Safeen) throws IOException{
		return write_text(String.format("\\aG[안전인챈: +%d]", Safeen));
	}

	public MJPresentationOutputStream write_short_damage(int dmg) throws IOException{
		write(0x2f);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_short_hit(int hit) throws IOException{
		write(0x30);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_short_critical(int critical) throws IOException{
		write(0x64);
		write(critical);
		return this;
	}
	
	public MJPresentationOutputStream write_long_damage(int dmg) throws IOException{
		write(0x23);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_long_hit(int hit) throws IOException{
		write(0x18);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_long_critical(int critical) throws IOException{
		write(0x63);
		write(critical);
		return this;
	}
	
	public MJPresentationOutputStream write_magic_hit(int hit) throws IOException{
		write(0x28);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_magic_critical(int critical) throws IOException{
		write(0x32);
		writeH(critical);
		return this;
	}
	
	public MJPresentationOutputStream write_text(String s) throws IOException{
		write(0x27);
		writeS(s);
		return this;
	}
	
	public MJPresentationOutputStream write_max_hp(int max_hp) throws IOException{
		write(0x0E);
		writeH(max_hp);
		return this;
	}
	
	public MJPresentationOutputStream write_drain_hp() throws IOException{
		write(0x22);
		return this;
	}
	
	public MJPresentationOutputStream write_str(int str) throws IOException{
		write(0x08);
		write(str);
		return this;
	}
	
	public MJPresentationOutputStream write_dex(int dex) throws IOException{
		write(0x09);
		write(dex);
		return this;
	}
	
	public MJPresentationOutputStream write_con(int con) throws IOException{
		write(0x0A);
		write(con);
		return this;
	}
	
	public MJPresentationOutputStream write_wis(int wis) throws IOException{
		write(0x0B);
		write(wis);
		return this;
	}
	
	public MJPresentationOutputStream write_int(int intel) throws IOException{
		write(0x0C);
		write(intel);
		return this;
	}
	
	public MJPresentationOutputStream write_cha(int cha) throws IOException{
		write(0x0D);
		write(cha);
		return this;
	}
	
	public MJPresentationOutputStream write_sp(int sp) throws IOException{
		write(0x11);
		write(sp);
		return this;
	}
	
	public MJPresentationOutputStream write_magic_name(L1ItemInstance item, String magic_name) throws IOException{
		int weaponlevel = item.get_item_level();
		if (weaponlevel == 0) {
		write(0x4A);
		writeS(magic_name);
		}
		return this;
	}
	
	public MJPresentationOutputStream write_damage_reduction(int reduction) throws IOException{
		write(0x3F);
		write(reduction);
		return this;
	}
	
	public MJPresentationOutputStream write_ignore_reduction(int ignore) throws IOException{
		return write_text(String.format("대미지 리덕션 무시 +%d", ignore));
	}
	
	public MJPresentationOutputStream write_addsub_hit(int hit) throws IOException{
		write(0x05);
		write(hit);
		return this;
	}
	
	public MJPresentationOutputStream write_addsub_damage(int dmg) throws IOException{
		write(0x06);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_addsub_short_damage(int dmg) throws IOException{
		write(47);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_addsub_long_damage(int dmg) throws IOException{
		write(35);
		write(dmg);
		return this;
	}
	
	public MJPresentationOutputStream write_blessed_options(L1ItemInstance item) throws IOException{
		int bless = item.get_bless_level();
		if(bless > 0){
			int type = item.getItem().getType();
			if (type == 7 || type == 16 || type == 17) {
				write_text("\\fI축복 SP:\\aA +" + bless);
			}else{
				write_text("\\fI축복 추타:\\aA +" + bless);
			}
		}
		return this;
	}
	
	public MJPresentationOutputStream write_weapon_level_options(L1ItemInstance item) throws IOException{
		int weaponlevel = item.get_item_level();
		if(weaponlevel > 0) {
			int type = item.getItem().getType2();
			if (type == 1) {
				switch (weaponlevel) {
				case 1:
					write_text("\\fI발동:\\aA 1단계 마법");
					break;
				case 2:
					write_text("\\fI발동:\\aA 2단계 마법");
					break;
				case 3:
					write_text("\\fI발동:\\aA 3단계 마법");
					break;
				case 4:
					write_text("\\fI발동:\\aA 4단계 마법");
					break;
				default:
					break;
				}
			}
		}
		return this;
	}
public MJPresentationOutputStream write_weapon_attr_options(L1ItemInstance item) throws IOException{
		
		int type = item.getItem().getType2();
		if (type == 1) {
			switch (item.getAttrEnchantLevel()) {
			case 1:
				
				write_text("\\fI불 속성 대미지: \\aA+1");
				break; // 화령1단
			case 2:
				
				write_text("\\fI불 속성 대미지: \\aA+3");
				break; // 화령2단
			case 3:
				
				write_text("\\fI불 속성 대미지: \\aA+5");
				break; // 화령3단 (불의속성)
			case 4:
				
				write_text("\\fI불 속성 대미지: \\aA+7");
				break; // 화령4단
			case 5:
				
				write_text("\\fI불 속성 대미지: \\aA+9");
				break; // 화령5단
			case 6:
				
				write_text("\\fI물 속성 대미지: \\aA+1");
				break; // 수령1단
			case 7:
				
				write_text("\\fI물 속성 대미지: \\aA+3");
				break; // 수령2단
			case 8:
				
				write_text("\\fI물 속성 대미지: \\aA+5");
				break; // 수령3단 (물의속성)
			case 9:
				
				write_text("\\fI물 속성 대미지: \\aA+7");
				break; // 수령4단
			case 10:
				
				write_text("\\fI물 속성 대미지: \\aA+9");
				break; // 수령5단
			case 11:
				
				write_text("\\fI바람 속성 대미지: \\aA+1");
				break; // 풍령1단
			case 12:
				
				write_text("\\fI바람 속성 대미지: \\aA+3");
				break; // 풍령2단
			case 13:
				
				write_text("\\fI바람 속성 대미지: \\aA+5");
				break; // 풍령3단 (바람의속성)
			case 14:
				
				write_text("\\fI바람 속성 대미지: \\aA+7");
				break; // 풍령4단
			case 15:
				
				write_text("\\fI바람 속성 대미지: \\aA+9");
				break; // 풍령5단
			case 16:
				
				write_text("\\fI땅 속성 대미지: \\aA+1");
				break; // 지령1단
			case 17:
				
				write_text("\\fI땅 속성 대미지: \\aA+3");
				break; // 지령2단
			case 18:
				
				write_text("\\fI땅 속성 대미지: \\aA+5");
				break; // 지령3단 (땅의속성)
			case 19:
				
				write_text("\\fI땅 속성 대미지: \\aA+7");
				break; // 지령4단
			case 20:
				
				write_text("\\fI땅 속성 대미지: \\aA+9");
				break; // 지령5단
			default:
				break;
			}
		}
		return this;
	}
	
	
	public boolean isUndeadDmg(L1Item item) {
		boolean result = false;
		
		if(item.getMaterial() == 14 || item.getMaterial() == 17 || item.getMaterial() == 22)
			result = true;
		
		return result;
	}
}
