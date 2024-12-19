package l1j.server.MJCaptchaSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT;

public class MJCaptchaData {
	public static MJCaptchaData newInstance(ResultSet rs) throws SQLException{
		return newInstance()
			.set_surf_id(rs.getInt("surfId"))
			.set_answer(rs.getInt("answer"));
	}
	
	public static MJCaptchaData newInstance(){
		return new MJCaptchaData();
	}
	
	private ProtoOutputStream _stream;
	private int _surf_id;
	private int _answer;
	private MJCaptchaData(){}
	
	public MJCaptchaData set_surf_id(int surf_id){
		_surf_id = surf_id;
		return this;
	}
	
	public int get_surf_id(){
		return _surf_id;
	}
	
	public MJCaptchaData set_answer(int answer){
		_answer = answer;
		return this;
	}
	
	public int get_answer(){
		return _answer;
	}
	
	public boolean is_answer(int answer){
		return _answer == answer;
	}
	
	public ProtoOutputStream get_stream(){
		if(_stream == null){
			SC_NOTIFICATION_MESSAGE_NOT noti = SC_NOTIFICATION_MESSAGE_NOT.newInstance();
			noti.set_suffileNumber(_surf_id);
			noti.set_notificationMessage(String.format("[오토방지]좌측 그림의 숫자를  입력해주세요. (%d초)", MJCaptchaLoadManager.CAPTCHA_INPUT_DURATION));
			noti.set_messageRGB(MJCaptchaLoadManager.CAPTCHA_MESSAGE_COLOR);
			noti.set_duration(MJCaptchaLoadManager.CAPTCHA_INPUT_DURATION);
			_stream = noti.writeTo(MJEProtoMessages.SC_NOTIFICATION_MESSAGE_NOT);
			noti.dispose();
		}
		return _stream;
	}
	
	public void dispose(){
		if(_stream != null){
			_stream.dispose();
			_stream = null;
		}
	}
}
