package l1j.server.MJBookQuestSystem.UserSide;

import java.io.IOException;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CriteriaProgress;

public class BQSCharacterCriteriaProgress implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static BQSCharacterCriteriaProgress newInstance(BQSInformation bqsInfo){
		BQSCharacterCriteriaProgress progress = newInstance();
		int criteria_id = bqsInfo.getCriteriaId();
		progress.set_current_ahcievement_level(0);
		progress.set_progress(CriteriaProgress.newInstance(criteria_id));
		return progress;
	}
	
	public static BQSCharacterCriteriaProgress newInstance(){
		return new BQSCharacterCriteriaProgress();
	}
	
	private int _current_ahcievement_level;
	private CriteriaProgress _progress;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	private BQSCharacterCriteriaProgress(){}
	
	public int get_criteria_id(){
		return _progress.get_criteria_id();
	}
	
	public boolean onUpdateIsComplete(BQSInformation bqsInfo){
		long thisAchievementQuantity = bqsInfo.getBookStep(_current_ahcievement_level);
		long currentQuantity = _progress.get_quantity();
		if(currentQuantity >= thisAchievementQuantity)
			return false;
				
		return _progress.onUpdate() == thisAchievementQuantity;
	}
	
	public long getCurrentQuantity(){
		return _progress.get_quantity();
	}
	
	public void increase_current_achievement_level(){
		++_current_ahcievement_level;
	}
	
	public int get_current_ahcievement_level(){
		return _current_ahcievement_level;
	}
	public void set_current_ahcievement_level(int current_ahcievement_level){
		_bit |= 0x01;
		_current_ahcievement_level = current_ahcievement_level;
	}
	public boolean has_current_ahcievement_level(){
		return (_bit & 0x01) == 0x01;
	}
	public CriteriaProgress get_progress(){
		return _progress;
	}
	public void set_progress(CriteriaProgress progress){
		_bit |= 0x02;
		_progress = progress;
	}
	public boolean has_progress(){
		return (_bit & 0x02) == 0x02;
	}
	
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	
	}
	
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_current_ahcievement_level())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _current_ahcievement_level);
		if (has_progress())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _progress);
		_memorizedSerializedSize = size;
		return size;
	}
	
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		
		if (!has_current_ahcievement_level()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if(!has_progress()){
			_memorizedIsInitialized = -1;
			return false;
		}		
		_memorizedIsInitialized = 1;
		return true;
	}
	
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_current_ahcievement_level()){
			output.writeUInt32(1, _current_ahcievement_level);
		}
		
		if(has_progress()){
			output.writeMessage(2, _progress);
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
					set_current_ahcievement_level(input.readUInt32());
					break;
				}
				case 0x00000012:{
					set_progress((CriteriaProgress)input.readMessage(CriteriaProgress.newInstance()));
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

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	public MJIProtoMessage copyInstance(){
		return new BQSCharacterCriteriaProgress();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		if(has_progress()){
			_progress.dispose();
			_progress = null;
		}
		
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
