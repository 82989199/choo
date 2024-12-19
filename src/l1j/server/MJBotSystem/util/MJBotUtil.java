package l1j.server.MJBotSystem.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJBotSystem.MJBotBrain;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotMent;
import l1j.server.MJBotSystem.MJBotSpell;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Loader.MJBotMentLoader;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.Ability;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Bot;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

/**********************************
 * 
 * MJ bot utility functions.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotUtil {
	private static final Random _rnd = new Random(System.nanoTime());
	
	private static final HashMap<Integer, Integer[]>	_castleArea;
	private static final HashMap<Integer, Integer[]>	_castleStartPos;
	private static final HashMap<Integer, Integer[]>	_castleDefensePos;
	private static final HashMap<Integer, Integer[]>	_castleDoorIds;
	private static final HashMap<Integer, Integer[]>	_castleSubDoorIds;
	
	private static final HashMap<Integer, Integer[]> 	_rkKentDefensePos;
	private static final HashMap<Integer, Integer[]> 	_rkOrcDefensePos;
	private static final HashMap<Integer, Integer[]>	_rkGiranDefensePos;
	
	private static final HashMap<Integer, Integer[]>	_rkKentOffensePos;
	private static final HashMap<Integer, Integer[]>	_rkOrcOffensePos;
	private static final HashMap<Integer, Integer[]>	_rkGiranOffensePos;
	
	
	private static final HashMap<Integer, Integer[]>	_castleWayPointsLv1;
	private static final HashMap<Integer, Integer[]>	_castleWayPointsLv2;
	private static final HashMap<Integer, Integer[]>	_castleWayPointsLv3;
	private static final HashMap<Integer, Integer[]>	_castleWayForHeadings;
	private static final HashMap<Integer, Integer[][]>	_castleWayArea;
	
	static{
		_castleArea = new HashMap<Integer, Integer[]>(8);
		_castleArea.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{L1CastleLocation.KENT_X1, L1CastleLocation.KENT_Y1, L1CastleLocation.KENT_X2, L1CastleLocation.KENT_Y2, (int)L1CastleLocation.KENT_MAP});
		_castleArea.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{L1CastleLocation.OT_X1, L1CastleLocation.OT_Y1, L1CastleLocation.OT_X2, L1CastleLocation.OT_Y2, (int)L1CastleLocation.OT_MAP});
		_castleArea.put(L1CastleLocation.WW_CASTLE_ID, new Integer[]{L1CastleLocation.WW_X1, L1CastleLocation.WW_Y1, L1CastleLocation.WW_X2, L1CastleLocation.WW_Y2, (int)L1CastleLocation.WW_MAP});
		_castleArea.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{L1CastleLocation.GIRAN_X1, L1CastleLocation.GIRAN_Y1, L1CastleLocation.GIRAN_X2, L1CastleLocation.GIRAN_Y2, (int)L1CastleLocation.GIRAN_MAP});
		_castleArea.put(L1CastleLocation.HEINE_CASTLE_ID, new Integer[]{L1CastleLocation.HEINE_X1, L1CastleLocation.HEINE_Y1, L1CastleLocation.HEINE_X2, L1CastleLocation.HEINE_Y2, (int)L1CastleLocation.HEINE_MAP});
		_castleArea.put(L1CastleLocation.DOWA_CASTLE_ID, new Integer[]{L1CastleLocation.DOWA_X1, L1CastleLocation.DOWA_Y1, L1CastleLocation.DOWA_X2, L1CastleLocation.DOWA_Y2, (int)L1CastleLocation.DOWA_MAP});
		_castleArea.put(L1CastleLocation.ADEN_CASTLE_ID, new Integer[]{L1CastleLocation.ADEN_X1, L1CastleLocation.ADEN_Y1, L1CastleLocation.ADEN_X2, L1CastleLocation.ADEN_Y2, (int)L1CastleLocation.ADEN_MAP});
		_castleArea.put(L1CastleLocation.DIAD_CASTLE_ID, new Integer[]{L1CastleLocation.DIAD_X1, L1CastleLocation.DIAD_Y1, L1CastleLocation.DIAD_X2, L1CastleLocation.DIAD_Y2, (int)L1CastleLocation.DIAD_MAP});
		
		_castleStartPos = new HashMap<Integer, Integer[]>(8);
		_castleStartPos.put(L1CastleLocation.KENT_CASTLE_ID, 	new Integer[]{33070, 32765, 33079, 32774, 4});
		_castleStartPos.put(L1CastleLocation.OT_CASTLE_ID, 		new Integer[]{32775, 32351, 32786, 32363, 4});
		_castleStartPos.put(L1CastleLocation.GIRAN_CASTLE_ID, 	new Integer[]{33627, 32759, 33639, 32771, 4});
		
		_castleDefensePos	= new HashMap<Integer, Integer[]>(8);
		_castleDefensePos.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{33170, 32760, 4});
		_castleDefensePos.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{32797, 32265, 4});
		_castleDefensePos.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{33631, 32661, 4});
		
		_castleDoorIds = new HashMap<Integer, Integer[]>(8);
		_castleDoorIds.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{2001});
		_castleDoorIds.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{2010});
		_castleDoorIds.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{2031, 2033});
		
		_castleSubDoorIds = new HashMap<Integer, Integer[]>(8);
		_castleSubDoorIds.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{2002, 2003});
		_castleSubDoorIds.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{2011, 2012});
		_castleSubDoorIds.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{2034, 2035});
		
		_rkKentDefensePos 	= new HashMap<Integer, Integer[]>(512);
		_rkOrcDefensePos 	= new HashMap<Integer, Integer[]>(512);
		_rkGiranDefensePos 	= new HashMap<Integer, Integer[]>(512);
		
		_rkKentOffensePos	= new HashMap<Integer, Integer[]>();
		_rkOrcOffensePos	= new HashMap<Integer, Integer[]>();
		_rkGiranOffensePos	= new HashMap<Integer, Integer[]>();
		
		
		_castleWayPointsLv1	= new HashMap<Integer, Integer[]>(3);
		_castleWayPointsLv1.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{
				33110, 32771,
				33111, 32764,
				33111, 32776,
		});
		_castleWayPointsLv1.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{
				32795, 32319,
				32791, 32319,
				32798, 32319,
		});
		_castleWayPointsLv1.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{
				33630, 32735,
				33624, 32736,
				33639, 32736,
				33633, 32735,
		});
		
		_castleWayPointsLv2	= new HashMap<Integer, Integer[]>(3);
		_castleWayPointsLv2.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{
				33145, 32783,
				33149, 32766,
				33150, 32791,
		});
		_castleWayPointsLv2.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{
				32797, 32299,
				32800, 32299,
				32792, 32299,
		});
		_castleWayPointsLv2.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{
				33633, 32703,
				33635, 32703,
				33631, 32703,
		});
		
		_castleWayPointsLv3	= new HashMap<Integer, Integer[]>(3);
		_castleWayPointsLv3.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{
				33166, 32779,
				33169, 32768,
		});
		_castleWayPointsLv3.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{
				32798, 32287,
				32797, 32280,
		});
		_castleWayPointsLv3.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{
				33632, 32685,
				33639, 32677,
				33631, 32670,
				33624, 32678,
		});
		
		_castleWayForHeadings = new HashMap<Integer, Integer[]>(3);
		_castleWayForHeadings.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[]{
				2, 2, 2
		});
		_castleWayForHeadings.put(L1CastleLocation.OT_CASTLE_ID, new Integer[]{
				1, 0, 0
		});
		_castleWayForHeadings.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[]{
				0, 0, 0
		});
		
		_castleWayArea = new HashMap<Integer, Integer[][]>(3);
		_castleWayArea.put(L1CastleLocation.KENT_CASTLE_ID, new Integer[][]{
				{33064, 32763, 33111, 32777},
				{33112, 32734, 33147, 32805},
				{33148, 32762, 33187, 32803}
		});
		
		_castleWayArea.put(L1CastleLocation.OT_CASTLE_ID, new Integer[][]{
				{32789, 32319, 32800, 32364},
				{32788, 32299, 32802, 32318},
				{32788, 32277, 32802, 32298}
		});
		
		_castleWayArea.put(L1CastleLocation.GIRAN_CASTLE_ID, new Integer[][]{
				{33621, 32735, 33642, 32771},
				{33621, 32703, 33642, 32735},
				{33618, 32662, 33647, 32702}
		});
		
		Integer[] arr = new Integer[]{
			33112, 32768,
			33112, 32769,
			33112, 32770,
			33112, 32771,
			33112, 32772,
			
			33166, 32776,
			33167, 32776,
			33168, 32776,
			33169, 32776,
			33170, 32776,
			
			33113, 32772,
			33113, 32771,
			33113, 32770,
			33113, 32769,
			33113, 32768,
//			33114, 32768,
//			33114, 32769,
//			33114, 32770,
//			33114, 32771,
//			33114, 32772,
//			33115, 32772,
//			33115, 32771,
//			33115, 32770,
//			33115, 32769,
//			33115, 32768,
			
			
			33171, 32776,
			33171, 32777,
			33171, 32778,
			33171, 32779,
			33171, 32780,
			33171, 32781,
			33170, 32781,
			33169, 32781,
			33168, 32781,
			33167, 32781,
			33166, 32781,
			33166, 32780,
			33166, 32779,
			33166, 32778,
			33166, 32777,
			33113, 32763,
			33114, 32763,
			33113, 32777,
			33114, 32777,
		};
		_rkKentDefensePos.put(1, arr);
		
		arr = new Integer[]{
//				33122, 32771,
//				33122, 32770,
//				33122, 32769,
//				33122, 32768,
//				33122, 32767,
//				33122, 32766,
//				33123, 32766,
//				33124, 32766,
//				33125, 32766,
				33126, 32766,
				33127, 32766,
				33127, 32767,
				33127, 32768,
				33127, 32769,
				33127, 32770,
				33127, 32771,
				33126, 32771,
//				33125, 32771,
//				33124, 32771,
//				33123, 32771,
				33167, 32777,
				33168, 32777,
				33169, 32777,
				33170, 32777,
				33170, 32778,
				33170, 32779,
				33170, 32780,
				33169, 32780,
				33168, 32780,
				33167, 32780,
				33167, 32779,
				33167, 32778,
		};
		_rkKentDefensePos.put(2, arr);
		
		arr = new Integer[]{
//				33123, 32770,
//				33123, 32769,
//				33123, 32768,
//				33123, 32767,
//				33124, 32770,
//				33124, 32769,
//				33124, 32768,
//				33124, 32767,
//				33125, 32770,
//				33125, 32769,
//				33125, 32768,
//				33125, 32767,
				33126, 32770,
				33126, 32769,
				33126, 32768,
				33126, 32767,
				33168, 32778,
				33168, 32779,
				33169, 32779,
				33169, 32778,
//				33127, 32773,
				33127, 32772,
				33127, 32771,
				33128, 32771,
				33128, 32772,
//				33128, 32773,
//				33129, 32773,
				33129, 32772,
				33129, 32771,
				33127, 32764,
				33127, 32765,
//				33127, 32763,
//				33128, 32763,
				33128, 32764,
				33128, 32765,
				33129, 32765,
				33129, 32764,
//				33129, 32763,
		};
		_rkKentDefensePos.put(3, arr);
		
		arr = new Integer[]{
				33116, 32763,
				33116, 32764,
				33116, 32765,
				33116, 32766,
				33116, 32767,
				33116, 32768,
				33116, 32769,
				33116, 32770,
				33116, 32771,
				33116, 32772,
				33116, 32773,
				33116, 32774,
				33116, 32775,
				33116, 32776,
				33116, 32777,
//				33117, 32777,
//				33117, 32776,
//				33117, 32775,
//				33117, 32774,
//				33117, 32773,
//				33117, 32772,
//				33117, 32771,
//				33117, 32770,
//				33117, 32769,
//				33117, 32768,
//				33117, 32767,
//				33117, 32766,
//				33117, 32765,
//				33117, 32764,
//				33117, 32763,
//				33118, 32763,
//				33118, 32764,
//				33118, 32765,
//				33118, 32766,
//				33118, 32767,
//				33118, 32768,
//				33118, 32769,
//				33118, 32770,
//				33118, 32771,
//				33118, 32772,
//				33118, 32773,
//				33118, 32774,
//				33118, 32775,
//				33118, 32776,
//				33118, 32777,	
		};
		_rkKentDefensePos.put(5, arr);
		
		
		
		arr = new Integer[]{
				32790, 32318,
				32790, 32317,
				32790, 32316,
				32799, 32318,
				32799, 32317,
				32799, 32316,
				32792, 32315,
				32793, 32315,
				32794, 32315,
				32795, 32315,
				32796, 32315,
				32797, 32315,
				32797, 32316,
				32796, 32316,
				32795, 32316,
				32794, 32316,
				32793, 32316,
				32792, 32316,
				32792, 32317,
				32793, 32317,
				32794, 32317,
				32795, 32317,
				32796, 32317,
				32797, 32317,
				32801, 32282,
				32800, 32282,
				32799, 32282,
				32798, 32282,
				32797, 32282,
				32796, 32282,
				32796, 32283,
				32796, 32284,
				32796, 32285,
				32796, 32286,
				32796, 32287,
				32797, 32287,
				32798, 32287,
				32799, 32287,
				32800, 32287,
				32801, 32287,
				32801, 32286,
				32801, 32285,
				32801, 32284,
				32801, 32283,
		};
		_rkOrcDefensePos.put(1, arr);
		arr = new Integer[]{
				32793, 32308,
				32794, 32308,
				32795, 32308,
				32796, 32308,
				32793, 32307,
				32794, 32307,
				32795, 32307,
				32796, 32307,
				32793, 32306,
				32794, 32306,
				32795, 32306,
				32796, 32306,
				32793, 32305,
				32794, 32305,
				32795, 32305,
				32796, 32305,
				32793, 32304,
				32794, 32304,
				32795, 32304,
				32796, 32304,
				32793, 32303,
				32794, 32303,
				32795, 32303,
				32796, 32303,
				32800, 32283,
				32799, 32283,
				32798, 32283,
				32797, 32283,
				32797, 32284,
				32797, 32285,
				32797, 32286,
				32798, 32286,
				32799, 32286,
				32800, 32286,
				32800, 32285,
				32800, 32284,

		};
		_rkOrcDefensePos.put(2, arr);
		arr = new Integer[]{
				32792, 32304,
				32791, 32304,
				32791, 32303,
				32792, 32303,
				32792, 32302,
				32791, 32302,
				32791, 32301,
				32792, 32301,
				32798, 32304,
				32799, 32304,
				32799, 32303,
				32798, 32303,
				32798, 32302,
				32799, 32302,
				32799, 32301,
				32798, 32301,
				32798, 32285,
				32798, 32284,
				32799, 32285,
				32799, 32284,

		};
		_rkOrcDefensePos.put(3, arr);
		arr = new Integer[]{
				32799, 32315,
				32799, 32314,
				32799, 32313,
				32798, 32313,
				32797, 32313,
				32796, 32313,
				32795, 32313,
				32794, 32313,
				32793, 32313,
				32792, 32313,
				32790, 32315,
				32790, 32314,
				32790, 32313,
				32790, 32312,
				32791, 32312,
				32792, 32312,
				32793, 32312,
				32794, 32312,
				32795, 32312,
				32796, 32312,
				32797, 32312,
				32798, 32312,
				32799, 32312,
				32797, 32314,
				32796, 32314,
				32795, 32314,
				32794, 32314,
				32793, 32314,
				32792, 32314,
		};
		_rkOrcDefensePos.put(5, arr);
		
		
		arr = new Integer[]{
				33629, 32733,
				33630, 32733,
				33631, 32733,
				33632, 32733,
				33633, 32733,
				33634, 32733,
				33634, 32732,
				33633, 32732,
				33632, 32732,
				33631, 32732,
				33630, 32732,
				33629, 32732,
				33623, 32733,
				33640, 32733,
				33629, 32701,
				33630, 32701,
				33631, 32701,
				33632, 32701,
				33633, 32701,
				33634, 32701,
				33635, 32701,
				33636, 32701,
				33634, 32679,
				33634, 32678,
				33634, 32677,
				33634, 32676,
				33634, 32675,
				33633, 32675,
				33632, 32675,
				33631, 32675,
				33630, 32675,
				33629, 32675,
				33629, 32676,
				33629, 32677,
				33629, 32678,
				33629, 32679,
				33629, 32680,
				33630, 32680,
				33631, 32680,
				33632, 32680,
				33633, 32680,
				33634, 32680,
		};
		_rkGiranDefensePos.put(1, arr);
		arr = new Integer[]{
				33633, 32723,
				33632, 32723,
				33631, 32723,
				33630, 32723,
				33633, 32722,
				33632, 32722,
				33631, 32722,
				33630, 32722,
				33633, 32721,
				33632, 32721,
				33631, 32721,
				33630, 32721,
				33633, 32720,
				33632, 32720,
				33631, 32720,
				33630, 32720,
				33633, 32719,
				33632, 32719,
				33631, 32719,
				33630, 32719,
				33633, 32718,
				33632, 32718,
				33631, 32718,
				33630, 32718,
				33634, 32696,
				33633, 32696,
				33632, 32696,
				33634, 32695,
				33633, 32695,
				33632, 32695,
				33634, 32694,
				33633, 32694,
				33632, 32694,
				
				33634, 32693,
				33633, 32693,
				33632, 32693,
				33633, 32678,
				33633, 32677,
				33633, 32676,
				33632, 32676,
				33631, 32676,
				33630, 32676,
				33630, 32677,
				33630, 32678,
				33630, 32679,
				33631, 32679,
				33632, 32679,
				33633, 32679,
		};
		_rkGiranDefensePos.put(2, arr);
		arr = new Integer[]{
				33629, 32719,
				33628, 32719,
				33629, 32718,
				33628, 32718,
				33629, 32717,
				33628, 32717,
				33629, 32716,
				33628, 32716,
				33634, 32719,
				33635, 32719,
				33634, 32718,
				33635, 32718,
				33634, 32717,
				33635, 32717,
				33634, 32716,
				33635, 32716,
				33630, 32693,
				33629, 32693,
				33630, 32694,
				33629, 32694,
				33634, 32693,
				33635, 32693,
				33634, 32694,
				33635, 32694,
				33632, 32677,
				33631, 32677,
				33631, 32678,
				33632, 32678,
		};
		_rkGiranDefensePos.put(3, arr);
		arr = new Integer[]{
				33640, 32732,
				33640, 32731,
				33639, 32731,
				33638, 32731,
				33637, 32731,
				33636, 32731,
				33635, 32731,
				33634, 32731,
				33633, 32731,
				33632, 32731,
				33631, 32731,
				33630, 32731,
				33629, 32731,
				33628, 32731,
				33627, 32731,
				33626, 32731,
				33625, 32731,
				33624, 32731,
				33623, 32731,
				33623, 32732,
				33623, 32730,
				33624, 32730,
				33625, 32730,
				33626, 32730,
				33627, 32730,
				33628, 32730,
				33629, 32730,
				33630, 32730,
				33631, 32730,
				33632, 32730,
				33633, 32730,
				33634, 32730,
				33635, 32730,
				33636, 32730,
				33637, 32730,
				33638, 32730,
				33639, 32730,
				33640, 32730,
				33629, 32700,
				33630, 32700,
				33631, 32700,
				33632, 32700,
				33633, 32700,
				33634, 32700,
				33635, 32700,
				33636, 32700,
		};
		_rkGiranDefensePos.put(5, arr);
		
		
		
		arr = new Integer[]{
			33088, 32763,
			33088, 32764,
			33088, 32765,
			33088, 32766,
			33088, 32767,
			
			33089, 32763,
			33089, 32764,
			33089, 32765,
			33089, 32766,
			33089, 32767,
			
			33090, 32763,
			33090, 32764,
			33090, 32765,
			33090, 32766,
			33090, 32767,
			
			33091, 32763,
			33091, 32764,
			33091, 32765,
			33091, 32766,
			33091, 32767,
			
			33092, 32763,
			33092, 32764,
			33092, 32765,
			33092, 32766,
			33092, 32767,
			
			33088, 32769,
			33088, 32770,
			33088, 32771,
			33088, 32772,
			33088, 32773,
			
			33089, 32769,
			33089, 32770,
			33089, 32771,
			33089, 32772,
			33089, 32773,
			
			33090, 32769,
			33090, 32770,
			33090, 32771,
			33090, 32772,
			33090, 32773,
			
			33091, 32769,
			33091, 32770,
			33091, 32771,
			33091, 32772,
			33091, 32773,
			
			33092, 32769,
			33092, 32770,
			33092, 32771,
			33092, 32772,
			33092, 32773,
		};
		_rkKentOffensePos.put(1, arr);
		
		arr = new Integer[]{
			33088, 32757,
			33088, 32758,
			33088, 32759,
			33088, 32760,
			33088, 32761,
			
			33089, 32757,
			33089, 32758,
			33089, 32759,
			33089, 32760,
			33089, 32761,
			
			33090, 32757,
			33090, 32758,
			33090, 32759,
			33090, 32760,
			33090, 32761,
			
			33091, 32757,
			33091, 32758,
			33091, 32759,
			33091, 32760,
			33091, 32761,
			
			33092, 32757,
			33092, 32758,
			33092, 32759,
			33092, 32760,
			33092, 32761,
		};
		_rkKentOffensePos.put(2, arr);
		
		arr = new Integer[]{
			33088, 32775,
			33088, 32776,
			33088, 32777,
			33088, 32778,
			33088, 32779,
			
			33089, 32775,
			33089, 32776,
			33089, 32777,
			33089, 32778,
			33089, 32779,
			
			33090, 32775,
			33090, 32776,
			33090, 32777,
			33090, 32778,
			33090, 32779,
			
			33091, 32775,
			33091, 32776,
			33091, 32777,
			33091, 32778,
			33091, 32779,
			
			33092, 32775,
			33092, 32776,
			33092, 32777,
			33092, 32778,
			33092, 32779,
		};
		_rkKentOffensePos.put(5, arr);
		
		arr = new Integer[]{
			32792, 32335,
			32791, 32335,
			32790, 32335,
			32789, 32335,
			32788, 32335,
			
			32792, 32334,
			32791, 32334,
			32790, 32334,
			32789, 32334,
			32788, 32334,
			
			32792, 32333,
			32791, 32333,
			32790, 32333,
			32789, 32333,
			32788, 32333,
			
			32792, 32332,
			32791, 32332,
			32790, 32332,
			32789, 32332,
			32788, 32332,
			
			32792, 32331,
			32791, 32331,
			32790, 32331,
			32789, 32331,
			32788, 32331,
			
			32786, 32335,
			32785, 32335,
			32784, 32335,
			32783, 32335,
			32782, 32335,
			
			32786, 32334,
			32785, 32334,
			32784, 32334,
			32783, 32334,
			32782, 32334,
			
			32786, 32333,
			32785, 32333,
			32784, 32333,
			32783, 32333,
			32782, 32333,
			
			32786, 32332,
			32785, 32332,
			32784, 32332,
			32783, 32332,
			32782, 32332,
			
			32786, 32331,
			32785, 32331,
			32784, 32331,
			32783, 32331,
			32782, 32331,
		};
		_rkOrcOffensePos.put(1, arr);

		
		arr = new Integer[]{
			32798, 32335,
			32797, 32335,
			32796, 32335,
			32795, 32335,
			32794, 32335,
			
			32798, 32334,
			32797, 32334,
			32796, 32334,
			32795, 32334,
			32794, 32334,
			
			32798, 32333,
			32797, 32333,
			32796, 32333,
			32795, 32333,
			32794, 32333,
			
			32798, 32332,
			32797, 32332,
			32796, 32332,
			32795, 32332,
			32794, 32332,
			
			32798, 32331,
			32797, 32331,
			32796, 32331,
			32795, 32331,
			32794, 32331,
		};
		_rkOrcOffensePos.put(2, arr);
		
		arr	= new Integer[]{
			32780, 32335,
			32779, 32335,
			32778, 32335,
			32777, 32335,
			32776, 32335,	
			
			32780, 32334,
			32779, 32334,
			32778, 32334,
			32777, 32334,
			32776, 32334,	
			
			32780, 32333,
			32779, 32333,
			32778, 32333,
			32777, 32333,
			32776, 32333,	
			
			32780, 32332,
			32779, 32332,
			32778, 32332,
			32777, 32332,
			32776, 32332,	
			
			32780, 32331,
			32779, 32331,
			32778, 32331,
			32777, 32331,
			32776, 32331,	
		};
		_rkOrcOffensePos.put(5, arr);
		
		
		arr = new Integer[]{
			33636, 32759,
			33635, 32759,
			33634, 32759,
			33633, 32759,
			33632, 32759,
			
			33630, 32759,
			33629, 32759,
			33628, 32759,
			33627, 32759,
			33626, 32759,
			
			33636, 32758,
			33635, 32758,
			33634, 32758,
			33633, 32758,
			33632, 32758,
			
			33636, 32757,
			33635, 32757,
			33634, 32757,
			33633, 32757,
			33632, 32757,
			
			33636, 32756,
			33635, 32756,
			33634, 32756,
			33633, 32756,
			33632, 32756,
			
			33636, 32755,
			33635, 32755,
			33634, 32755,
			33633, 32755,
			33632, 32755,
			
			33630, 32758,
			33629, 32758,
			33628, 32758,
			33627, 32758,
			33626, 32758,
			
			33630, 32757,
			33629, 32757,
			33628, 32757,
			33627, 32757,
			33626, 32757,
			
			33630, 32756,
			33629, 32756,
			33628, 32756,
			33627, 32756,
			33626, 32756,
			
			33630, 32755,
			33629, 32755,
			33628, 32755,
			33627, 32755,
			33626, 32755,
		};
		_rkGiranOffensePos.put(1, arr);
		
		arr = new Integer[]{
			33642, 32759,
			33641, 32759,
			33640, 32759,
			33639, 32759,
			33638, 32759,
			
			33642, 32758,
			33641, 32758,
			33640, 32758,
			33639, 32758,
			33638, 32758,
			
			33642, 32757,
			33641, 32757,
			33640, 32757,
			33639, 32757,
			33638, 32757,
			
			33642, 32756,
			33641, 32756,
			33640, 32756,
			33639, 32756,
			33638, 32756,
			
			33642, 32755,
			33641, 32755,
			33640, 32755,
			33639, 32755,
			33638, 32755,
		};
		_rkGiranOffensePos.put(2, arr);
		
		
		arr = new Integer[]{
			33624, 32759,
			33623, 32759,
			33622, 32759,
			33621, 32759,
			33620, 32759,
			
			33624, 32758,
			33623, 32758,
			33622, 32758,
			33621, 32758,
			33620, 32758,
			
			33624, 32757,
			33623, 32757,
			33622, 32757,
			33621, 32757,
			33620, 32757,
			
			33624, 32756,
			33623, 32756,
			33622, 32756,
			33621, 32756,
			33620, 32756,
			
			33624, 32755,
			33623, 32755,
			33622, 32755,
			33621, 32755,
			33620, 32755,
		};
		_rkGiranOffensePos.put(5, arr);
	}
	
	public static int getCastleWayClanMemberNum(int clanid, int castleId, int wayLevel){
		Integer[][] locs = _castleWayArea.get(castleId);
		int x1 = locs[wayLevel][0];
		int y1 = locs[wayLevel][1];
		int x2 = locs[wayLevel][2];
		int y2 = locs[wayLevel][3];
		Collection<L1PcInstance> users = L1World.getInstance().getAllPlayers();
		int cnt = 0;
		for(L1PcInstance p : users){
			if(p == null || p.isDead() || p.getClanid() != clanid)
				continue;
			
			if(MJCommons.isInArea(p, x1, y1, x2, y2, 0, 5, 4))
				cnt++;
		}
		return cnt;
	}
	
	public static int getCastleWayLevel(L1PcInstance body, int castleId){
		Integer[][] locs 	= _castleWayArea.get(castleId);
		for(int i=2; i>=0; i--){
			if(MJCommons.isInArea(body, locs[i][0], locs[i][1], locs[i][2], locs[i][3], 5, 4))
				return i;
		}
		return 0;
	}
	
	public static MJBotLocation getCastleWayRandomLoc(int castleId, int wayLevel){
		Integer[][] locs	= _castleWayArea.get(castleId);
		return MJBotUtil.createRandomLocation(locs[wayLevel][0], locs[wayLevel][1], locs[wayLevel][2], locs[wayLevel][3], 4);
	}
	
	public static MJBotLocation getCastleWayCenterPoint(int castleId, int wayLevel){
		Integer[]	locs	= null;
		int			h		= 0;
		switch(wayLevel){
		case 0:
			locs 	= _castleWayPointsLv1.get(castleId);
			h		= _castleWayForHeadings.get(castleId)[0];
			break;
		case 1:
			locs 	= _castleWayPointsLv2.get(castleId);
			h		= _castleWayForHeadings.get(castleId)[1];
			break;
		case 2:
		default:
			locs 	= _castleWayPointsLv3.get(castleId);
			h		= _castleWayForHeadings.get(castleId)[2];
			break;
		}
		
		if(locs == null)
			return null;
		
		MJBotLocation bl = new MJBotLocation();
		h = MJCommons._headingLines[(h + _rnd.nextInt(3)) % 8];
		bl.x 	= locs[0] + (MJCommons.HEADING_TABLE_X[h]);
		bl.y 	= locs[1] + (MJCommons.HEADING_TABLE_Y[h]);
		bl.map 	= 4;
		return bl;
	}
	
	public static MJBotLocation getCastleWayPoint(int castleId, int wayLevel){
		Integer[]	locs	= null;
		int			h		= 0;
		switch(wayLevel){
		case 0:
			locs 	= _castleWayPointsLv1.get(castleId);
			h		= _castleWayForHeadings.get(castleId)[0];
			break;
		case 1:
			locs 	= _castleWayPointsLv2.get(castleId);
			h		= _castleWayForHeadings.get(castleId)[1];
			break;
		case 2:
		default:
			locs 	= _castleWayPointsLv3.get(castleId);
			h		= _castleWayForHeadings.get(castleId)[2];
			break;
		}
		
		if(locs == null)
			return null;
		
		MJBotLocation bl = new MJBotLocation();
		for(int i=0; i<locs.length; i+=2){
			if(_rnd.nextBoolean()){
				int r = 5;
				if(i == 0){
					h = MJCommons._headingLines[(h + _rnd.nextInt(3)) % 8];
					bl.x 	= locs[i]	+ (MJCommons.HEADING_TABLE_X[h] * r);
					bl.y 	= locs[i+1] + (MJCommons.HEADING_TABLE_Y[h] * r);
					bl.map 	= 4;
				}else{
					bl.x	= locs[i] - 2;
					bl.y	= locs[i+1] - 2;
					bl.map	= 4;
				}
				return bl;
			}
		}
		
		h = MJCommons._headingLines[(h + _rnd.nextInt(3)) % 8];
		bl.x 	= locs[0] + (MJCommons.HEADING_TABLE_X[h] * 5);
		bl.y 	= locs[1] + (MJCommons.HEADING_TABLE_Y[h] * 5);
		bl.map 	= 4;
		return bl;
	}
	
	public static Integer[] getRKPoses(int castleId, int classType, boolean isDefender){
		if(isDefender){
			switch(castleId){
			case 1:
				return _rkKentDefensePos.get(classType);
			case 2:
				return _rkOrcDefensePos.get(classType);
			case 4:
				return _rkGiranDefensePos.get(classType);
			default:
				return null;	
			}
		}else{
			switch(castleId){
			case 1:
				return _rkKentOffensePos.get(classType);
			case 2:
				return _rkOrcOffensePos.get(classType);
			case 4:
				return _rkGiranOffensePos.get(classType);
			default:
				return null;	
			}
		}
	}
	
	public static int getSpaceHeadingAndCombatZone(L1Character c){
		L1Map map = c.getMap();
		for(int h=0; h<8; h++){
			int cx = c.getX() + MJCommons.HEADING_TABLE_X[h];
			int cy = c.getY() + MJCommons.HEADING_TABLE_Y[h];
			if(!map.isCombatZone(cx, cy) || !map.isPassable(cx, cy))
				continue;
			
			//if(!L1World.getInstance().isVisibleObject(cx, cy, map.getId()))
				return h;
		}
		return -1;
	}
	
	// str, dex, con, wis, int, cha
	private static final int[][] _classBaseStat	= {
			{13, 9, 11, 11, 9, 13},		// royal
			{16, 12, 16, 9, 8, 10},		// knight
			{10, 12, 12, 12, 12, 9},	// elf
			{8, 7, 12, 14, 14, 8},		// wizard
			{15, 12, 12, 10, 11, 8},	// dark elf
			{13, 11, 14, 10, 10, 8},	// dragon knight
			{9, 10, 12, 14, 12, 8},		// witchcraft
			{16, 13, 16, 7, 10, 9}		// warrior
	};
	
	public static void calcStat(MJBotBrain brn, L1PcInstance body){
		int level 			= body.getLevel();
		int bonus 			= level - 50;
		int tmpHor 			= brn.getHormon();
		int tmpPri			= brn.getPride();
		int tmpSen			= brn.getSense();
		int classType		= body.getType();
		short maxHp 		= 0;
		short maxMp 		= 0;
		Ability abil 		= body.getAbility();
		abil.setBaseStr(_classBaseStat[classType][0]);
		abil.setBaseDex(_classBaseStat[classType][1]);
		abil.setBaseCon(_classBaseStat[classType][2]);
		abil.setBaseWis(_classBaseStat[classType][3]);
		abil.setBaseInt(_classBaseStat[classType][4]);
		abil.setBaseCha(_classBaseStat[classType][5]);
		switch(classType){
		case 0:	// crown
		case 1:	// knight
		case 4:	// darkelf
		case 5:	// dragon knight
		case 7:	// warrior
			for(int i=0; i<bonus; i++){
				if(tmpHor > tmpPri){
					abil.addBaseStr(1);
					tmpHor -= 5;
				}else{
					abil.addBaseCon(1);
					tmpPri -= 5;
				}
			}
			maxHp = (short) ((abil.getTotalCon() * 3) * body.getLevel());
			maxMp = (short) ((abil.getTotalWis() * 2) * body.getLevel());
			break;
		case 2:	// elf
			for(int i=0; i<bonus; i++){
				if(tmpHor > tmpPri){
					abil.addBaseDex(1);
					tmpHor -= 5;
				}else{
					if(tmpPri > tmpSen){
						abil.addBaseCon(1);
						tmpPri -= 5;
					}else{
						abil.addBaseWis(1);
						tmpSen -= 5;
					}
				}
			}
			maxHp = (short) ((abil.getTotalCon() * 2) * body.getLevel());
			maxMp = (short) ((abil.getTotalWis() * 3) * body.getLevel());
			break;
		case 3:	// wizard
		case 6:	// witchcraft
			if(tmpHor > tmpPri){
				if(tmpSen > tmpHor){
					abil.addBaseWis(1);
					tmpSen -= 5;
				}else{	
					abil.addBaseInt(1);
					tmpHor -= 5;
				}
			}else{
				abil.addBaseCon(1);
				tmpPri -= 5;
			} 
			maxHp = (short) ((abil.getTotalCon() * 2) * body.getLevel());
			maxMp = (short) ((abil.getTotalWis() * 3) * body.getLevel());
			break;
		}
		body.addBaseMaxHp(maxHp);
		body.setCurrentHp(maxHp);
		body.addBaseMaxMp(maxMp);
		body.setCurrentMp(maxMp);
	}
	
	private static final int[] _locationArea 	= { -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	public static void setPosition(MJBotBrain brn, L1PcInstance body, int x, int y){
		int cx = _locationArea[_rnd.nextInt(_locationArea.length)];
		int cy = _locationArea[_rnd.nextInt(_locationArea.length)];
		body.setX(x + cx);
		body.setY(y + cy);
	}
	
	public static MJBotLocation createRandomLocation(int x, int y, int mid){
		MJBotLocation loc = new MJBotLocation();
		loc.x = x + _locationArea[_rnd.nextInt(_locationArea.length)];
		loc.y = y + _locationArea[_rnd.nextInt(_locationArea.length)];
		loc.map = mid;
		return loc;
	}
	
	public static MJBotLocation createCastleLocation(int castleId){
		Integer[] array		= _castleStartPos.get(castleId);
		if(array == null)
			return null;
		
		return createRandomLocation(array[0], array[1], array[2], array[3], array[4]);
	}
	
	public static MJBotLocation createDefenseLocation(int castleId){
		Integer[] array	= _castleDefensePos.get(castleId);
		if(array == null)
			return null;
		
		return new MJBotLocation(array[0], array[1], array[2]);
	}
	
	public static MJBotLocation createRandomLocation(int left, int top, int right, int bottom, int mid){
		MJBotLocation loc 	= new MJBotLocation();
		L1Map map 			= L1WorldMap.getInstance().getMap((short)mid);
		int rx				= right		- left;
		int ry				= bottom 	- top;
		int cnt				= 0;
		int cx 				= 0;
		int cy				= 0;
		do{
			if(cnt++ > 15){
				if(_rnd.nextBoolean())	cx = left;
				else					cx = right;
				if(_rnd.nextBoolean())	cy = top;
				else					cy = bottom;
				break;
			}
			cx 				= _rnd.nextInt(rx) + left;
			cy				= _rnd.nextInt(ry) + top;
		}while(!map.isPassable(cx, cy));
		
		loc.map = mid;
		loc.x 	= cx;
		loc.y 	= cy;
		return loc;
	}
	
	private static final HashMap<String, Integer> _classNameToId;
	static{
		_classNameToId = new HashMap<String, Integer>(8);
		_classNameToId.put("군주", new Integer(0));
		_classNameToId.put("기사", new Integer(1));
		_classNameToId.put("요정", new Integer(2));
		_classNameToId.put("마법사", new Integer(3));
		_classNameToId.put("다크엘프", new Integer(4));
		_classNameToId.put("용기사", new Integer(5));
		_classNameToId.put("환술사", new Integer(6));
		_classNameToId.put("전사", new Integer(7));
	}
	
	public static int classNameToId(String s) throws Exception{
		Integer i = _classNameToId.get(s);
		if(i == null)
			throw new Exception(String.format("클래스 이름 [%s]에 해당하는 클래스ID를 찾을 수 없습니다.", s));
		return i;
	}
	
	public static int spellTargetAnalyst(String s){
		if(s.equals("유저"))
			return MJBotSpell.SPT_USER;
		else if(s.equals("몬스터"))
			return MJBotSpell.SPT_MOB;
		else if(s.equals("모두"))
			return MJBotSpell.SPT_ALWAYS;
		return MJBotSpell.SPT_NONE;
	}
	
	public static void deleteBots(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("delete from characters where account_name='MJBOT'");
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void deleteClanBots(){
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("delete from clan_data where bot=?");
			pstm.setBoolean(1, true);
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void deleteBot(MJBotAI ai){
		if(ai.getBody() == null)
			return;
		
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("delete from characters where account_name='MJBOT' and objid=?");
			pstm.setInt(1, ai.getBody().getId());
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}		
	}
	
	public static void deleteBot(L1PcInstance body){
		if(body == null)
			return;
		
		try{
			MJRankUserLoader.getInstance().removeUser(body);
		}catch(Exception e){}
		
		Connection 			con		= null;
		PreparedStatement 	pstm 	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("delete from characters where account_name='MJBOT' and objid=?");
			pstm.setInt(1, body.getId());
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}		
	}
	
	public static void sendBotDeathMent(MJBotAI ai, L1Character target){
		if(ai.getBrain().toRand(100) > MJBotLoadManager.MBO_MENTDICE_DEATH)
			return;
		
		sendBotMent(MJBotMentLoader.getInstance().get(MJBotMent.BMS_DIE), ai, target);
	}
	
	public static void sendBotKillMent(MJBotAI ai, L1Character target){
		if(ai.getBrain().toRand(100) > MJBotLoadManager.MBO_MENTDICE_KILL)
			return;
		
		sendBotMent(MJBotMentLoader.getInstance().get(MJBotMent.BMS_KILL), ai, target);
	}
	
	public static void sendBotOnTargetMent(MJBotAI ai, L1Character target){
		if(ai.getBrain().toRand(100) > MJBotLoadManager.MBO_MENTDICE_ONTARGET)
			return;
		
		sendBotMent(MJBotMentLoader.getInstance().get(MJBotMent.BMS_ONTARGET), ai, target);
	}
	
	public static void sendBotOnPerceiveMent(MJBotAI ai, L1Character target){
		if(ai.getBrain().toRand(100) > MJBotLoadManager.MBO_MENTDICE_ONPERCEIVE)
			return;
		
		sendBotMent(MJBotMentLoader.getInstance().get(MJBotMent.BMS_ONPERCEIVE), ai, target);		
	}
	
	public static void sendBotOnDamageMent(MJBotAI ai, L1Character target){
		if(ai.getBrain().toRand(100) > MJBotLoadManager.MBO_MENTDICE_ONDAMAGE)
			return;
		
		sendBotMent(MJBotMentLoader.getInstance().get(MJBotMent.BMS_ONDAMAGE), ai, target);		
	}
	
	public static void sendBotIdleMent(MJBotAI ai, L1Character target){
		if(ai.getBrain().toRand(100) > MJBotLoadManager.MBO_MENTDICE_IDLE)
			return;
		
		sendBotMent(MJBotMentLoader.getInstance().get(MJBotMent.BMS_IDLE), ai, target);				
	}
	
	private static void sendBotMent(MJBotMent ment, MJBotAI ai, L1Character target){
		S_Bot pck = null;
		if(ment != null){
			long cur = System.currentTimeMillis();
			if(ai.getMentDelay() > cur)
				return;
			
			ai.setMentDelay((ai.getBrain().getAge() * 10000) + cur);
			if((ment.type & MJBotMent.BMT_NORMAL) > 0){
				for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(ai.getBody())) {
					if(pck == null)
						pck = S_Bot.getBotChat(ai, ment.toString(ai, target));
					L1ExcludingList spamList3 = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList3.contains(0, ai.getBody().getName())) {
						listner.sendPackets(pck, false);
					}
				}		
			}else if((ment.type & MJBotMent.BMT_WHISPER) > 0 && target instanceof L1PcInstance){
				L1ExcludingList spamList2 = SpamTable.getInstance().getExcludeTable(target.getId());
				if (spamList2.contains(0, ai.getBody().getName())) 
					return;
				
				pck = S_Bot.getBotWhisperChat(ai, ment.toString(ai, target));
				((L1PcInstance)target).sendPackets(pck);
			}else if((ment.type & MJBotMent.BMT_WORLD) > 0){
				pck = S_Bot.getBotWorldChat(ai, ment.toString(ai, target));
				for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
					L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList15.contains(0, ai.getBody().getName())) 
							listner.sendPackets(pck, false);
				}
			}
		}
		
		if(pck != null){
			pck.clear();
			pck = null;
		}
	}
	
	public static MJBotLocation randomLoc(L1Object obj, int range){
		int sx = 1;
		int sy = 1;
		if(_rnd.nextBoolean())
			sx = -1;
		if(_rnd.nextBoolean())
			sy = -1;
		
		MJBotLocation loc 	= new MJBotLocation();
		loc.x 				= obj.getX() + (_rnd.nextInt(range) * sx);
		loc.y 				= obj.getY() + (_rnd.nextInt(range) * sy);
		loc.map 			= obj.getMapId();
		return loc;
	}
	
	public static int getDollType(int npcid) {
		switch (npcid) {
		case 200017:
			return L1DollInstance.DOLLTYPE_STONEGOLEM;

		case 80108:
			return L1DollInstance.DOLLTYPE_WAREWOLF;

		case 80106:
			return L1DollInstance.DOLLTYPE_BUGBEAR;

		case 200019:
			return L1DollInstance.DOLLTYPE_CRUSTACEA;

		case 200074:
			return L1DollInstance.DOLLTYPE_SNOWMAN;

		case 507:
			return L1DollInstance.DOLLTYPE_MOKAK;

		case 80107:
			return L1DollInstance.DOLLTYPE_SUCCUBUS;

		case 200018:
			return L1DollInstance.DOLLTYPE_ELDER;

		case 200012:
			return L1DollInstance.DOLLTYPE_COCA;

		case 900233:
			return L1DollInstance.DOLLTYPE_SNOWMAN_NEW;

		case 900226:
			return L1DollInstance.DOLLTYPE_SNOWMAN_A;

		case 900227:
			return L1DollInstance.DOLLTYPE_SNOWMAN_B;

		case 900228:
			return L1DollInstance.DOLLTYPE_SNOWMAN_C;

		case 81212:
			return L1DollInstance.DOLLTYPE_인어;

		case 508:
			return L1DollInstance.DOLLTYPE_LAVAGOLREM;

		case 900229:
			return L1DollInstance.DOLLTYPE_자이언트;

		case 900231:
			return L1DollInstance.DOLLTYPE_흑장로;

		case 900232:
			return L1DollInstance.DOLLTYPE_SUCCUBUSQUEEN;

		case 900225:
			return L1DollInstance.DOLLTYPE_DRAKE;

		case 81213:
			return L1DollInstance.DOLLTYPE_킹버그베어;

		case 509:
			return L1DollInstance.DOLLTYPE_DIAMONDGOLREM;

		case 900224:
			return L1DollInstance.DOLLTYPE_LICH;

		case 900230:
			return L1DollInstance.DOLLTYPE_사이클롭스;

		case 510:
			return L1DollInstance.DOLLTYPE_NIGHTBALD;

		case 511:
			return L1DollInstance.DOLLTYPE_SIER;

		case 7310082:
			return L1DollInstance.DOLL_Iris;

		case 7310083:
			return L1DollInstance.DOLL_vampire;

		case 7320069:
			return L1DollInstance.DOLL_mummy;

		case 512:
			return L1DollInstance.DOLLTYPE_DEMON;

		case 513:
			return L1DollInstance.DOLLTYPE_DEATHNIGHT;

		case 7310084:
			return L1DollInstance.DOLL_barranca;

		case 7320070:
			return L1DollInstance.DOLL_corruption;

		case 514:
			return L1DollInstance.DOLL_Baphomet;

		case 515:
			return L1DollInstance.DOLL_icequeen;

		case 516:
			return L1DollInstance.DOLL_Kouts;

		case 200068:
			return L1DollInstance.DOLLTYPE_SEADANCER;

		case 900179:
			return L1DollInstance.DOLLTYPE_SKELETON;

		case 900180:
			return L1DollInstance.DOLLTYPE_SCARECROW;

		case 900178:
			return L1DollInstance.DOLLTYPE_ETHYNE;

		case 5074:
			return L1DollInstance.DOLLTYPE_PIXIE_BLAG;

		case 5075:
			return L1DollInstance.DOLLTYPE_PIXIE_LESDAG;

		case 5076:
			return L1DollInstance.DOLLTYPE_PIXIE_ELREGEU;

		case 5077:
			return L1DollInstance.DOLLTYPE_PIXIE_GREG;

		case 200008:
		case 200009:
		case 200010:
		case 200011:
			return L1DollInstance.DOLLTYPE_HATCHLING;

		case 900220:
			return L1DollInstance.DOLLTYPE_PSY_CHAMPION;

		case 900221:
			return L1DollInstance.DOLLTYPE_PSY_BIRD;

		case 900222:
			return L1DollInstance.DOLLTYPE_PSY_GANGNAM_STYLE;

		case 900223:
			return L1DollInstance.DOLLTYPE_GREMLIN;

		case 900176:
			return L1DollInstance.DOLLTYPE_COBO;

		case 503:
			return L1DollInstance.ruler1;
			
		case 504:
			return L1DollInstance.ruler2;
			
		case 505:
			return L1DollInstance.ruler3;
			
		case 506:
			return L1DollInstance.ruler4;
			
		case 537:
			return L1DollInstance.ruler5;
			
		case 538:
			return L1DollInstance.ruler6;
			
		case 539:
			return L1DollInstance.ruler7;
			
		case 540:
			return L1DollInstance.ruler8;
			
		case 7320200:
			return L1DollInstance.Antaras;
			
		case 7320201:
			return L1DollInstance.Papou;
			
		case 7320202:
			return L1DollInstance.Lynd;
			
		case 7320203:
			return L1DollInstance.Valakas;
			
		case 7320261:
			return L1DollInstance.jindeathknight;
		}
		return 0;
	}
	
	public static boolean isInTown(L1Object obj){
		return MJCommons.isInArea(obj, MJBotLoadManager.MBO_WANDER_MAT_LEFT - 5, MJBotLoadManager.MBO_WANDER_MAT_TOP - 5, MJBotLoadManager.MBO_WANDER_MAT_RIGHT + 5, MJBotLoadManager.MBO_WANDER_MAT_BOTTOM + 5, (short)MJBotLoadManager.MBO_WANDER_MAT_MAPID);
	}
	
	public static boolean isInCastle(L1Object obj, int castleId){
		Integer[] array = _castleArea.get(castleId);
		if(array == null)
			return false;
		
		if(obj == null)
			return false;
		
		return MJCommons.isInArea(obj, array[0] - 20, array[1] - 20, array[2] + 20, array[3] + 20, array[4].shortValue());
	}
	
	public static boolean isInCastleStartup(L1Object obj, int castleId){
		Integer[] array = _castleStartPos.get(castleId);
		if(array == null)
			return false;
		
		return MJCommons.isInArea(obj, array[0], array[1], array[2], array[3], array[4].shortValue());
	}
	
	public static boolean isInCastleDefense(L1Object obj, int castleId){
		Integer[] array = _castleDefensePos.get(castleId);
		if(array == null)
			return false;
		
		return MJCommons.isInArea(obj, array[0] - 5, array[1] - 5, array[0] + 5, array[1] + 5, array[2].shortValue());
	}
	
	public static boolean isWeaponSkill(int skillId){
		return (skillId == L1SkillId.HOLY_WEAPON || skillId == L1SkillId.ENCHANT_WEAPON || skillId == L1SkillId.BLESS_WEAPON || skillId == L1SkillId.SHADOW_FANG);
	}
	
	public static ArrayDeque<L1DoorInstance> findCastleDoors(int castleId){
		Integer[] ids = _castleDoorIds.get(castleId);
		Integer[] subs = _castleSubDoorIds.get(castleId);
		ArrayDeque<L1DoorInstance> doors = new ArrayDeque<L1DoorInstance>(ids.length + subs.length);
		for(int i=0; i<ids.length; i++){
			L1DoorInstance 	door 	= DoorSpawnTable.getInstance().get(ids[i]);
			if(door == null /*|| door.isDead()*/)
				continue;
			
			doors.offer(door);
		}
		
		for(int i=0; i<subs.length; i++){
			L1DoorInstance 	door 	= DoorSpawnTable.getInstance().get(subs[i]);
			if(door == null || door.isDead())
				continue;
			doors.offer(door);
		}
		
		return doors;
	}
	
	public static L1DoorInstance findCastleSubDoor(int castleId){
		Integer[] 		ids		= _castleSubDoorIds.get(castleId);
		if(ids == null)
			return null;
		
		for(int i=0; i<ids.length; i++){
			L1DoorInstance 	door 	= DoorSpawnTable.getInstance().get(ids[i]);
			if(door == null || door.isDead())
				continue;
			
			return door;
		}
		
		return null;
	}
	
	public static L1DoorInstance findCastleDoor(int castleId){
		Integer[] 		ids		= _castleDoorIds.get(castleId);
		if(ids == null)
			return null;
		
		for(int i=0; i<ids.length; i++){
			L1DoorInstance 	door 	= DoorSpawnTable.getInstance().get(ids[i]);
			if(door == null || door.isDead())
				continue;
			
			return door;
		}
		
		return null;
	}
	
	public static String findCastleOwnerClan(int castleId){
		for(L1Clan clan : L1World.getInstance().getAllClans()){
			if(castleId == clan.getCastleId())
				return clan.getClanName();
		}
		return null;
	}
	
	private static final int[] _redknightGfx = new int[]{
		11949, 11949, 11983, 11930, 11949, 11973, 11949, 11949
	};
	
	public static int getRedKnightGfx(int classType){
		return _redknightGfx[classType];
	}
	
	private static final int[] _protectorGfx = new int []{
		12219,	// 데포로쥬
		12222,	// 이실로테
		12224,	// 아툰
		12220,	// 질리언(검)
		12314,	// 질리언(활)
		12221,	// 조우
		12223,	// 크리스터
	};
	private static final int[] _protectorToClassType = new int[]{
		0, 0, 1, 1, 2, 3, 4
	};
	private static final String[] _protectorNames = new String[]{
		"데포로쥬", "백조의 기사 이실로테", "철의 기사 아툰", "달의 기사 질리언", "달의 기사 질리언", "다크스타 조우", "그림자의 기사 크리스터"
	};
	
	public static int getProtectorGfx(int pid){
		return _protectorGfx[pid];
	}
	
	public static int protectorIdToClassType(int pid){
		return _protectorToClassType[pid];
	}
	
	public static int createProtectorId(){
		return _rnd.nextInt(6) + 1;
	}
	
	public static String getProtectorName(int pid){
		return _protectorNames[pid];
	}
}
