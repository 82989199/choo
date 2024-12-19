package l1j.server.server.serverpackets;

import java.sql.PreparedStatement;

import MJShiftObject.MJEShiftObjectType;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.Account;
import l1j.server.server.BadNamesList;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.UserCommands;
import l1j.server.server.clientpackets.C_CommonClick;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ChangeCharName extends ServerBasePacket {
	public static S_ChangeCharName getChangedSuccess(){
		return getChangedResult(true);
	}
	
	public static S_ChangeCharName getChangedFailure(){
		return getChangedResult(false);
	}
	
	public static S_ChangeCharName getChangedResult(boolean isSuccess){
		S_ChangeCharName s = new S_ChangeCharName();
		s.writeC(Opcodes.S_VOICE_CHAT);
		s.writeC(0x1e);
		s.writeH(isSuccess ? 0x02 : 0x06);
		s.writeH(0x00);
		return s;
	}
	
	public static S_ChangeCharName getChangedStart(){
		S_ChangeCharName s = new S_ChangeCharName();
		s.writeC(Opcodes.S_VOICE_CHAT);
		s.writeC(0x1D);
		s.writeD(0x00);
		return s;
	}
	
	public static ServerBasePacket doChangeCharName(L1PcInstance pc, String sourceName, String destinationName){
		try{
			byte[] buff = destinationName.getBytes("MS949");
			if(buff.length <= 0)
				return getChangedFailure();
			if (CharacterTable.getInstance().isContainNameList(destinationName) || MJBotNameLoader.isAlreadyName(destinationName))
				return getChangedFailure();
			
			if (BadNamesList.getInstance().isBadName(destinationName))
				return getChangedFailure();
			
			for (int i = 0; i < destinationName.length(); i++) {
				if (destinationName.charAt(i) == 'ㄱ' || destinationName.charAt(i) == 'ㄲ' || destinationName.charAt(i) == 'ㄴ' || destinationName.charAt(i) == 'ㄷ'
						|| // 한문자(char)단위로 비교.
						destinationName.charAt(i) == 'ㄸ' || destinationName.charAt(i) == 'ㄹ' || destinationName.charAt(i) == 'ㅁ'
						|| destinationName.charAt(i) == 'ㅂ' || // 한문자(char)단위로 비교
								destinationName.charAt(i) == 'ㅃ' || destinationName.charAt(i) == 'ㅅ' || destinationName.charAt(i) == 'ㅆ'
						|| destinationName.charAt(i) == 'ㅇ' || // 한문자(char)단위로 비교
								destinationName.charAt(i) == 'ㅈ' || destinationName.charAt(i) == 'ㅉ' || destinationName.charAt(i) == 'ㅊ'
						|| destinationName.charAt(i) == 'ㅋ' || // 한문자(char)단위로 비교.
								destinationName.charAt(i) == 'ㅌ' || destinationName.charAt(i) == 'ㅍ' || destinationName.charAt(i) == 'ㅎ'
						|| destinationName.charAt(i) == 'ㅛ' || // 한문자(char)단위로 비교.
								destinationName.charAt(i) == 'ㅕ' || destinationName.charAt(i) == 'ㅑ' || destinationName.charAt(i) == 'ㅐ'
						|| destinationName.charAt(i) == 'ㅔ' || // 한문자(char)단위로 비교.
								destinationName.charAt(i) == 'ㅗ' || destinationName.charAt(i) == 'ㅓ' || destinationName.charAt(i) == 'ㅏ'
						|| destinationName.charAt(i) == 'ㅣ' || // 한문자(char)단위로 비교.
								destinationName.charAt(i) == 'ㅠ' || destinationName.charAt(i) == 'ㅜ' || destinationName.charAt(i) == 'ㅡ'
						|| destinationName.charAt(i) == 'ㅒ' || // 한문자(char)단위로 비교.
								destinationName.charAt(i) == 'ㅖ' || destinationName.charAt(i) == 'ㅢ' || destinationName.charAt(i) == 'ㅟ'
						|| destinationName.charAt(i) == 'ㅝ' || // 한문자(char)단위로 비교.
								destinationName.charAt(i) == 'ㅞ' || destinationName.charAt(i) == 'ㅙ' || destinationName.charAt(i) == 'ㅚ'
						|| destinationName.charAt(i) == 'ㅘ' || // 한문자(char)단위로 비교.
								destinationName.charAt(i) == '씹' || destinationName.charAt(i) == '좃' || destinationName.charAt(i) == '좆'
						|| destinationName.charAt(i) == 'ㅤ') {
					return getChangedFailure();
				}
			}
			
			for (int i = 0; i < destinationName.length(); i++) {
				if (!Character.isLetterOrDigit(destinationName.charAt(i)))
					return getChangedFailure();
			}
			
			if (!UserCommands.isAlphaNumeric(destinationName))
				return getChangedFailure();
			
			if(!pc.is_shift_transfer()){
				if (!pc.getInventory().checkItem(408991, 1)){
					pc.sendPackets(new S_Disconnect());
					pc.getNetConnection().close();
					return null;
				}
			}
			Updator.exec("UPDATE characters SET char_name=? WHERE char_name=?", new Handler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, destinationName);
					pstm.setString(2, pc.getName());
				}
			});
			
			Updator.exec("UPDATE tb_kda SET name=? WHERE objid=?", new Handler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, destinationName);
					pstm.setInt(2, pc.getId());
				}
			});
			
			MJKDALoader.getInstance().getChangeName(pc.getId(), destinationName);
			
			pc.save(); // 저장
			System.out.println(String.format("[%s][%s] %s에서 %s로 캐릭명 변경. \r\n", MJNSHandler.getLocalTime(), pc.getNetConnection().getIp(), sourceName, destinationName));
			if(pc.is_shift_transfer()){
				pc.set_shift_type(MJEShiftObjectType.NONE);
			}else{
				pc.getInventory().consumeItem(408991, 1);
			}
			
			UserCommands.buddys(pc); // 친구 삭제
			UserCommands.편지삭제(pc); // 편지삭제
			MJCopyMapObservable.getInstance().resetPosition(pc);
			MJRaidSpace.getInstance().getBackPc(pc);
			
			C_NewCharSelect.restartProcess(pc);
			return getChangedSuccess();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return getChangedFailure();
	}

	public S_ChangeCharName(){}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	
}
