package l1j.server.MJTemplate.MJProto.MainServer_Client_Companion;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CompanionT.eCommand;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import java.io.IOException;

import l1j.server.MJCompanion.Basic.Joke.MJCompanionJokeInfo;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_COMPANION_TM_COMMAND_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_COMPANION_TM_COMMAND_REQ newInstance(){
		return new CS_COMPANION_TM_COMMAND_REQ();
	}
	private eCommand _command;
	private int _target_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_COMPANION_TM_COMMAND_REQ(){
	}
	public eCommand get_command(){
		return _command;
	}
	public void set_command(eCommand val){
		_bit |= 0x1;
		_command = val;
	}
	public boolean has_command(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_target_id(){
		return _target_id;
	}
	public void set_target_id(int val){
		_bit |= 0x2;
		_target_id = val;
	}
	public boolean has_target_id(){
		return (_bit & 0x2) == 0x2;
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
		if (has_command())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _command.toInt());
		if (has_target_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _target_id);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_command()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_command()){
			output.writeEnum(1, _command.toInt());
		}
		if (has_target_id()){
			output.writeUInt32(2, _target_id);
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
					set_command(eCommand.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_target_id(input.readUInt32());
					break;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			//System.out.println("CS_COMPANION_TM_COMMAND_REQ " + this._target_id + " " + this._command.toString());
			L1PcInstance master = clnt.getActiveChar();
			if(master == null)
				return this;
			
			MJCompanionInstance companion = master.get_companion();
			if(companion == null)
				return this;
			
			if(_command.equals(eCommand.TM_Attack)){
				L1Object obj = L1World.getInstance().findObject(_target_id);
				if(obj != null && obj instanceof L1Character)
					companion.set_target((L1Character)obj);
			}else if(_command.equals(eCommand.TM_PullBack)){
				companion.allTargetClear();
			}else if(_command.equals(eCommand.Joke)){
				companion.send_action(67);
				MJCompanionJokeInfo jInfo = MJCompanionJokeInfo.find_joke_info(companion.get_class_info().get_class_name(), companion.getLevel());
				if(jInfo != null && jInfo.get_joke_effect() > 0)
					companion.send_effect(jInfo.get_joke_effect());
			}else if(_command.equals(eCommand.Happy)){
				companion.send_action(68);				
			}else if(_command.equals(eCommand.TM_Aggressive) || _command.equals(eCommand.TM_GetItem) || _command.equals(eCommand.TM_Defensive)){
				companion.set_command_state(_command);
			}else{
				System.out.println(_command);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_COMPANION_TM_COMMAND_REQ();
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
