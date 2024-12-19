package l1j.server.MJNetSafeSystem.Distribution;

import java.util.HashMap;

import l1j.server.Config;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.utils.MJHexHelper;

public enum MJClientStatus {
	CLNT_STS_HANDSHAKE(1, new HandShakeDistributor()),			// 최초 연결시 키 -> 버전 -> 핑패킷
	CLNT_STS_CONNECTED(2, new ConnectedDistributor()),			// 핑패킷 인증 후 계정 -> C_Read_News까지.
	CLNT_STS_AUTHLOGIN(3, new SelectCharacterDistributor()),	// 케릭터 선택창
	CLNT_STS_ENTERWORLD(4, new WorldDistributor()),				// 엔터월드
	CLNT_STS_CHANGENAME(5, new WorldDistributor());
	
	private static HashMap<Integer, MJClientStatus> _statuses = new HashMap<Integer, MJClientStatus>();
	static{
		MJClientStatus[] arr = MJClientStatus.values();
		for(MJClientStatus sts : arr)
			_statuses.put(sts.toInt(), sts);
	}
	
	private int 		_status;
	private Distributor _dist;
	MJClientStatus(int i, Distributor dist){
		_status = i;
		_dist	= dist;
	}
	
	public int toInt(){
		return _status;
	}
	

	public static long TestMillis = 0L;
	public void process(GameClient clnt, byte[] data){
		int op 	= data[0] & 0xff;
		if(Config.is_leaf){
		System.out.println(MJHexHelper.toString(data, data.length));
		}
		int ret = MJNetSafeLoadManager.isFilter(clnt, op);
		if(ret != MJNetSafeLoadManager.RESULT_NORMAL)
			return;

		if(op == Opcodes.C_USE_ITEM){
			TestMillis = System.currentTimeMillis();
		}
		
		ClientBasePacket cbp = null;
		try{
			cbp = _dist.handle(clnt, data, op);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(cbp != null){
			cbp.clear();
			cbp = null;
		}
	}
	
	public static MJClientStatus fromInt(int i){
		return _statuses.get(i);
	}
}
