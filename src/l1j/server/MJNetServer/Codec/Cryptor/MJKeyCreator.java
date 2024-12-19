package l1j.server.MJNetServer.Codec.Cryptor;

import l1j.server.MJNetServer.Util.MJDataParser;
/**********************************
 * 
 * MJ Network Server System. Blowfish key enc/dec for Lineage packet.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJKeyCreator {
	
	public static long[] get(long[] keys){
		keys[0] = MJDataParser.parseLong64(MJDataParser.rotr(MJDataParser.parseLong64(keys[0]^0x9C30D539), 13));
		keys[1] = (keys[1] ^ keys[0] ^ 0x7C72E993);
		return keys;
	}
}
