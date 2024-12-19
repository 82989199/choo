package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;

import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_PARTY_CONTROL_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_PARTY_CONTROL_REQ newInstance(){
		return new CS_PARTY_CONTROL_REQ();
	}
	private ePartyControlType _controlType;
	private int _target_object_id;
	private String _target_user_name;
	private int _target_mark;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_PARTY_CONTROL_REQ(){
	}
	public ePartyControlType get_controlType(){
		return _controlType;
	}
	public void set_controlType(ePartyControlType val){
		_bit |= 0x1;
		_controlType = val;
	}
	public boolean has_controlType(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_target_object_id(){
		return _target_object_id;
	}
	public void set_target_object_id(int val){
		_bit |= 0x2;
		_target_object_id = val;
	}
	public boolean has_target_object_id(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_target_user_name(){
		return _target_user_name;
	}
	public void set_target_user_name(String val){
		_bit |= 0x4;
		_target_user_name = val;
	}
	public boolean has_target_user_name(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_target_mark(){
		return _target_mark;
	}
	public void set_target_mark(int val){
		_bit |= 0x8;
		_target_mark = val;
	}
	public boolean has_target_mark(){
		return (_bit & 0x8) == 0x8;
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
		if (has_controlType())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _controlType.toInt());
		if (has_target_object_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _target_object_id);
		if (has_target_user_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _target_user_name);
		if (has_target_mark())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _target_mark);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_controlType()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_controlType()){
			output.writeEnum(1, _controlType.toInt());
		}
		if (has_target_object_id()){
			output.writeUInt32(2, _target_object_id);
		}
		if (has_target_user_name()){
			output.writeString(3, _target_user_name);
		}
		if (has_target_mark()){
			output.wirteInt32(4, _target_mark);
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
				case 0x00000008:{
					set_controlType(ePartyControlType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_target_object_id(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					set_target_user_name(input.readString());
					break;
				}
				case 0x00000020:{
					set_target_mark(input.readInt32());
					break;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		L1PcInstance pc = null;
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			pc = clnt.getActiveChar();
			if(pc == null)
				return this;
			
			if(_controlType.toInt() == ePartyControlType.Command_InvatePartyNormalById.toInt() || 	// 마우스로 대상을 찍어서 초대(자동분배 X)
				_controlType.toInt() == ePartyControlType.Command_InvatePratyRandomById.toInt() ||	// 마우스로 대상을 찍어서 초대(자동분배 O)
				_controlType.toInt() == ePartyControlType.Command_InvatePartyNormal.toInt() || 		// /초대 명령어로 초대(자동분배 X)
				_controlType.toInt() == ePartyControlType.Command_InvatePartyRandom.toInt() 		// /초대 명령어로 초대(자동분배 O)
					){
				
				if(pc.getMapId() == 621 || !pc.is_world()){
					pc.sendPackets("해당 맵에서는 파티 기능을 사용 할 수 없습니다.");
					return this;
				}
				L1PcInstance target = L1World.getInstance().getPlayer(_target_user_name);
				if(target == null || (_target_object_id > 0 && target.getId() != _target_object_id)){					
					pc.sendPackets("알 수 없는 대상입니다.");
					return this;
				}
				
				if(target.getId() == pc.getId()){
					pc.sendPackets("자기 자신에게는 초대할 수 없습니다.");
					return this;
				}
				
				if(target.isInParty()){
					pc.sendPackets(415);
					return this;					
				}
				
				L1Party party = pc.getParty();
				if(party != null){
					if(!party.isLeader(pc)){
						pc.sendPackets(416);	// 파티의 리더만을 초대할 수 있습니다. 
						return this;
					}
					target.setPartyID(pc.getId());
					target.sendPackets(new S_Message_YN(953 + pc.getPartyType(), pc.getName()));
					// 953 : \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
					// 954 : \f2%0\f>%s \fU자동분배파티\f> 초대하였습니다. 허락하시겠습니까? (Y/N)
				}else{
					target.setPartyID(pc.getId());
					pc.setPartyType(_controlType.getPartyServerType());
					target.sendPackets(new S_Message_YN(953 + pc.getPartyType(), pc.getName()));
					// 953 : \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
					// 954 : \f2%0\f>%s \fU자동분배파티\f> 초대하였습니다. 허락하시겠습니까? (Y/N)
				}

			}else if(_controlType.toInt() == ePartyControlType.Command_InvateChatParty.toInt()){
				if(pc.getMapId() == 621 || !pc.is_world()){
					pc.sendPackets("해당 맵에서는 파티 기능을 사용 할 수 없습니다.");
					return this;
				}
				L1PcInstance target = L1World.getInstance().getPlayer(_target_user_name);
				if(target == null || (_target_object_id > 0 && target.getId() != _target_object_id)){					
					pc.sendPackets("알 수 없는 대상입니다.");
					return this;
				}
				
				if(target.getId() == pc.getId()){
					pc.sendPackets("자기 자신에게는 초대할 수 없습니다.");
					return this;
				}
				
				if(target.isInChatParty()){
					pc.sendPackets(415);
					return this;					
				}
				
				L1Party party = pc.getParty();
				if(party != null){
					if(!party.isLeader(pc)){
						pc.sendPackets(416);	// 파티의 리더만을 초대할 수 있습니다. 
						return this;
					}
					target.sendPackets(new S_Message_YN(951, pc.getName()));	
				}else{
					target.setPartyID(pc.getId());
					target.sendPackets(new S_Message_YN(951, pc.getName()));	
				}
			}else if(_controlType.toInt() == ePartyControlType.Command_TransforLeader.toInt()){
				L1Party party = pc.getParty();
				if(party == null){
					System.out.println(String.format("파티 없는 사용자가 파티장 위임 : %s", pc.getName()));
					return this;
				}
				if(!party.isLeader(pc)){
					System.out.println(String.format("파티장 아닌 사용자가 파티장 위임 : %s", pc.getName()));
					return this;
				}
				L1PcInstance target = L1World.getInstance().getPlayer(_target_user_name);
				if(target == null || target.getId() != _target_object_id){
					pc.sendPackets("알 수 없는 대상입니다.");
					return this;
				}
				party.passLeader(target);
				
			}else if(_controlType.toInt() == ePartyControlType.Command_TransforMark.toInt()){
				L1Party party = pc.getParty();
				if(party == null){
					System.out.println(String.format("파티 없는 사용자가 표식 설정 : %s", pc.getName()));
					return this;
				}
				if(!party.isLeader(pc)){
					System.out.println(String.format("파티장 아닌 사용자가 표식 설정 : %s", pc.getName()));
					return this;
				}
				L1PcInstance target = L1World.getInstance().getPlayer(_target_user_name);
				if(target == null || target.getId() != _target_object_id){
					pc.sendPackets("알 수 없는 대상입니다.");
					return this;
				}
				//TODO 파티 프로토
				party.setPartyMark(target, _target_mark);
			}else if(_controlType.toInt() == ePartyControlType.Command_KickUser.toInt()){
				L1Party party = pc.getParty();
				if(party == null){
					System.out.println(String.format("파티 없는 사용자가 파티원 추방 : %s", pc.getName()));
					return this;
				}
				if(!party.isLeader(pc)){
					System.out.println(String.format("파티장 아닌 사용자가 파티원 추방 : %s", pc.getName()));
					return this;
				}
				L1PcInstance target = L1World.getInstance().getPlayer(_target_user_name);
				if(target == null){
					pc.sendPackets("알 수 없는 대상입니다.");
					return this;
				}
				party.kickMember(target);
			}else{
				System.out.println(String.format("알 수 없는 파티 컨트롤 타입. 이름 : %s, 타입 : %s", pc.getName(), _controlType.toString()));
			}
		} catch(Exception e){
			e.printStackTrace();
			if(pc != null){
				System.out.println(String.format("케릭터 이름 : %s", pc.getName()));
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_PARTY_CONTROL_REQ();
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
	public enum ePartyControlType{
		Command_InvatePartyNormalById(0, 0),
		Command_InvatePratyRandomById(1, 1),
		Command_InvateChatParty(2, 0),
		Command_TransforLeader(3, 0),
		Command_InvatePartyNormal(4, 0),
		Command_InvatePartyRandom(5, 1),
		Command_TransforMark(6, 0),
		Command_KickUser(7, 0);
		private int value;
		private int partyServerType;
		ePartyControlType(int val, int serverType){
			value = val;
			partyServerType = serverType;
		}
		public int toInt(){
			return value;
		}
		
		public int getPartyServerType(){
			return partyServerType;
		}
		
		public boolean equals(ePartyControlType v){
			return value == v.value;
		}
		public static ePartyControlType fromInt(int i){
			switch(i){
			case 0:
				return Command_InvatePartyNormalById;
			case 1:
				return Command_InvatePratyRandomById;
			case 2:
				return Command_InvateChatParty;
			case 3:
				return Command_TransforLeader;
			case 4:
				return Command_InvatePartyNormal;
			case 5:
				return Command_InvatePartyRandom;
			case 6:
				return Command_TransforMark;
			case 7:
				return Command_KickUser;
			default:
				return null;
			}
		}
	}
}
