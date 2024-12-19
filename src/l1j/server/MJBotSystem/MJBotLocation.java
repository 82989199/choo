package l1j.server.MJBotSystem;

/**********************************
 * 
 * MJ Bot teleport location.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotLocation {
	public boolean isTown;
	public int x;
	public int y;
	public int map;
	
	public MJBotLocation(){
		
	}
	
	public MJBotLocation(int sx, int sy, int smap){
		x 	= sx;
		y 	= sy;
		map	= smap;
	}
}
