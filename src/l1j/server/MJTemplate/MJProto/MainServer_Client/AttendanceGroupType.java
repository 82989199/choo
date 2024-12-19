package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public enum AttendanceGroupType{
	NORMAL(0),
	PC_CAFE(1);
	private int value;
	private SC_ATTENDANCE_BONUS_GROUP_INFO _gInfo;
	private ProtoOutputStream _gInfoStream;
	private int _bonusSize;
	private int _checkSum;
	AttendanceGroupType(int val){
		value = val;
		_checkSum = 1 << val;
	}
	public int toInt(){
		return value;
	}
	
	public int toCheckSum(){
		return _checkSum;
	}
	
	public boolean isCheckSum(int val){
		return (val & _checkSum) == _checkSum;
	}
	
	public boolean equals(AttendanceGroupType v){
		return value == v.value;
	}
	
	public int getBonusSize(){
		return _bonusSize;
	}
	
	public void setGroupInfo(SC_ATTENDANCE_BONUS_GROUP_INFO gInfo){
		ProtoOutputStream tmpStream = _gInfoStream;
		SC_ATTENDANCE_BONUS_GROUP_INFO tmpInfo = _gInfo;
		if(gInfo.get_bonusGroup() != null)
			_bonusSize = gInfo.get_bonusGroup().size();
		gInfo.set_groupType(this);
		_gInfo = gInfo;
		_gInfoStream = gInfo.writeTo(MJEProtoMessages.SC_ATTENDANCE_BONUS_GROUP_INFO);
		if(tmpStream != null){
			tmpStream.dispose();
			tmpStream = null;
		}
		
		if(tmpInfo != null){
			tmpInfo.dispose();
			tmpInfo = null;
		}
	}
	
	public void send(L1PcInstance pc){
		if(_gInfoStream != null)
			pc.sendPackets(_gInfoStream, false);
	}
	
	public void doReward(L1PcInstance pc, int index){
		AttendanceBonusGroup bGroup = _gInfo.get_bonusGroup().get(index);
		L1Inventory inv = pc.getInventory();
		for(AttendanceBonusInfo bInfo : bGroup.get_bonuses()){
			if(inv.checkAddItem(bInfo.get_itemId(), bInfo.get_itemCount()) == L1Inventory.OK)
				inv.storeItem(bInfo.get_itemId(), bInfo.get_itemCount());
		}
	}
	
	public static AttendanceGroupType fromInt(int i){
		switch(i){
		case 0:
			return NORMAL;
		case 1:
			return PC_CAFE;
		default:
			return null;
		}
	}
	
	
}
