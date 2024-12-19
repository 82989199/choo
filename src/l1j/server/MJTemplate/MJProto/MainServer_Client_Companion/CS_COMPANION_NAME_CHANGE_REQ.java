package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Companion.SC_COMPANION_NAME_CHANGE_ACK.eResult;
import l1j.server.server.BadNamesList;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.io.IOException;
import java.util.HashMap;

import l1j.server.MJCompanion.MJCompanionSettings;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJCompanion.Instance.MJCompanionNameHandler;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_COMPANION_NAME_CHANGE_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_COMPANION_NAME_CHANGE_REQ newInstance(){
		return new CS_COMPANION_NAME_CHANGE_REQ();
	}
	private byte[] _desired_name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_COMPANION_NAME_CHANGE_REQ(){
	}
	public byte[] get_desired_name(){
		return _desired_name;
	}
	public void set_desired_name(byte[] val){
		_bit |= 0x1;
		_desired_name = val;
	}
	public boolean has_desired_name(){
		return (_bit & 0x1) == 0x1;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_desired_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _desired_name);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_desired_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_desired_name()){
			output.writeBytes(1, _desired_name);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				default:{
					return this;
				}
				case 0x0000000A:{
					set_desired_name(input.readBytes());
					break;
				}
			}
		}
		return this;
	}
	
	private static final HashMap<Character, Byte> m_invalid_ko_character;
	static{
		m_invalid_ko_character = new HashMap<Character, Byte>();
		m_invalid_ko_character.put('ㄱ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㄲ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㄴ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㄷ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㄸ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㄹ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅁ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅂ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅃ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅅ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅆ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅇ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅈ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅉ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅊ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅋ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅌ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅍ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅎ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅛ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅕ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅑ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅐ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅔ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅗ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅓ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅏ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅣ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅠ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅜ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅡ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅒ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅖ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅢ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅟ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅝ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅞ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅙ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('ㅚ', Byte.MIN_VALUE);
		m_invalid_ko_character.put('씹', Byte.MIN_VALUE);
		m_invalid_ko_character.put('좆', Byte.MIN_VALUE);
		m_invalid_ko_character.put('좃', Byte.MIN_VALUE);
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null)
				return this;

			MJCompanionInstance companion = pc.get_companion();
			if(companion == null)
				return this;
			
			if(companion.get_name_changed_count() > 0){
				pc.sendPackets("펫 이름 변경 아이템을 사용하여 이름을 초기화해주세요.");
				return this;
			}
			
			if(_desired_name == null || _desired_name.length <= 0){
				SC_COMPANION_NAME_CHANGE_ACK.send(clnt, eResult.SystemError, null);
				return this;
			}
			
			if(_desired_name.length > 40){
				clnt.sendPacket(new S_SystemMessage("이름이 너무 깁니다."));
				SC_COMPANION_NAME_CHANGE_ACK.send(clnt, eResult.SystemError, null);
				return this;
			}
			
			String replace_name = new String(_desired_name, "MS949");
			if(BadNamesList.getInstance().isBadName(replace_name) || 5 < _desired_name.length - replace_name.length()){
				clnt.sendPacket(new S_SystemMessage("사용할 수 없는 이름입니다."));
				SC_COMPANION_NAME_CHANGE_ACK.send(clnt, eResult.SystemError, null);
				return this;
			}
			for(int i=replace_name.length() - 1; i>=0; --i){
				char c = replace_name.charAt(i);
				if(!Character.isLetterOrDigit(c) || m_invalid_ko_character.containsKey(replace_name.charAt(i))){
					clnt.sendPacket(new S_SystemMessage("사용할 수 없는 문자가 포함되어 있습니다."));
					SC_COMPANION_NAME_CHANGE_ACK.send(clnt, eResult.SystemError, null);
					return this;					
				}
			}
			if(MJCompanionNameHandler.exists_name(replace_name)){
				SC_COMPANION_NAME_CHANGE_ACK.send(clnt, eResult.SameNameExisted, null);
				return this;
			}
			companion.increase_name_changed_count();
			companion.do_name_change(replace_name);
			companion.setName(replace_name);
			companion.update_name();
			SC_COMPANION_NAME_CHANGE_ACK.send_bytes(clnt, eResult.Success, _desired_name);
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_COMPANION_NAME_CHANGE_REQ();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
