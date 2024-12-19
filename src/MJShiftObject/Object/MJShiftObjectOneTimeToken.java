package MJShiftObject.Object;

import MJShiftObject.MJEShiftObjectType;
import l1j.server.MJTemplate.Aes.MJAes;

public class MJShiftObjectOneTimeToken {
	public static MJShiftObjectOneTimeToken from_onetimetoken(String onetimetoken) throws Exception{
		MJAes aes = new MJAes("[MJSoftAesCoder]");
		String dec = aes.decrypt(onetimetoken);
		String[] array = dec.split("\\|");
		if(array.length != 11)
			return null;
		
		return new MJShiftObjectOneTimeToken(
				array[0],
				Boolean.parseBoolean(array[1]),
				MJShiftObject.newInstance()
				.set_source_id(Integer.parseInt(array[2]))
				.set_destination_id(Integer.parseInt(array[3]))
				.set_shift_type(MJEShiftObjectType.from_name(array[4]))
				.set_source_character_name(array[5])
				.set_source_account_name(array[6])
				.set_destination_character_name(array[7]) 
				.set_destination_account_name(array[8]),
				array[9],
				Boolean.parseBoolean(array[10])
				);
	}
	
	public String server_identity;
	public boolean is_returner;
	public boolean is_reconnected;
	public MJShiftObject shift_object;
	public String home_server_identity;
	
	public MJShiftObjectOneTimeToken(String server_identity, boolean is_returner, MJShiftObject shift_object, String home_server_identity, boolean is_reconnected){
		this.server_identity = server_identity;
		this.is_returner = is_returner;
		this.shift_object = shift_object;
		this.home_server_identity = home_server_identity;
		this.is_reconnected = is_reconnected;
	}
	
	public String to_onetime_token() throws Exception{
		MJAes aes = new MJAes("[MJSoftAesCoder]");
		String s = new StringBuilder()
				.append(server_identity)
				.append("|")
				.append(is_returner)
				.append("|")
				.append(shift_object.get_source_id())
				.append("|")
				.append(shift_object.get_destination_id())
				.append("|")
				.append(shift_object.get_shift_type().to_name())
				.append("|")
				.append(shift_object.get_source_character_name())
				.append("|")
				.append(shift_object.get_source_account_name())
				.append("|")
				.append(shift_object.get_destination_character_name())
				.append("|")
				.append(shift_object.get_destination_account_name())
				.append("|")
				.append(home_server_identity)
				.append("|")
				.append(is_reconnected)
				.toString();
		s = aes.encrypt(s);
		return s;
	}
}
