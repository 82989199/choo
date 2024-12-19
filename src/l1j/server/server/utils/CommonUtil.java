package l1j.server.server.utils;

import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.DESPERADO;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class CommonUtil {
	/**
	 * 2011.08.05 금액표시
	 * @param number
	 * @return
	 */
	public static String numberFormat(int number) {
		try {
			NumberFormat nf = NumberFormat.getInstance();

			return nf.format(number);
		} catch (Exception e) {
			return Integer.toString(number);
		}
	}

	/**
	 * 2011.08.05 랜덤함수
	 * @param number
	 * @return
	 */
	public static int random(int number) {
		Random rnd = new Random();
		return rnd.nextInt(number);
	}

	/**
	 * 2011.08.05 랜덤함수
	 * @param lbound
	 * @param ubound
	 * @return
	 */
	public static int random(int lbound, int ubound) {
		return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
	}

	/**
	 * 2011.08.30 데이터포맷
	 * @param type
	 * @return
	 */
	public static String dateFormat(String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.KOREA);
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * 2011.08.30 데이터포맷
	 * @param type
	 * @return
	 */
	public static String dateFormat(String type, Timestamp date) {
		SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.KOREA);
		return sdf.format(date.getTime());
	}

	/**
	 * 2011.08.31 아이템 종료 시간
	 * @param item
	 * @param minute
	 */
	public static void SetTodayDeleteTime(L1ItemInstance item, int minute) {
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
		item.setEndTime(deleteTime);
	}	
	
	/**
	 * 2017.12.02 여관열쇠 마을
	 * @param item
	 * @param townname
	 */
	public static void SetHotelTownName(L1ItemInstance item, String townname) {
		item.setHotel_Town(townname);;
	}

	/**
	 * 2011.08.31 아이템 종료 시간 지정(오늘 남은 시간 계산) - 로또 시스템
	 * @param item
	 */
	public static void SetTodayDeleteTime(L1ItemInstance item) {
		int hour = Integer.parseInt(dateFormat("HH"));
		int minute = Integer.parseInt(dateFormat("mm"));
		int time = 0;

		if (hour <= 22 && minute < 30) {
			time = (23 - hour) * 60 + (60 - minute) - 60;
		} else {
			time = (23 - hour) * 60 + (60 - minute) - 60 + (24 * 60);
		}

		Timestamp deleteTime = null;

		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * time));
		item.setEndTime(deleteTime);
	}	

	/**
	 * 2011.08.31 지정시간까지 남은 시간
	 * @param item
	 */
	public static int getRestTime(int hh) {		
		int hour = Integer.parseInt(dateFormat("HH"));
		int minute = Integer.parseInt(dateFormat("mm"));

		int time = 0;

		time = (hh - hour) * 60 - minute;	

		return time;
	}
	/**
	 * 지정된 최대치 최소치 이상 넘어가지 않도록 설정 하기위함.
	 * @param i   현재 수치
	 * @param min 최소치
	 * @param max 최대치
	 * @return
	 */
	public static int get_current(int i, int min, int max) {
		int current = i;
		if (current <= min) {
			current = min;
		} else if (current >= max) {
			current = max;
		}
		return current;
	} 
	/**
	 * 텔레포터 상태 이상 체크 
	 * @param pc
	 * @param item
	 * @return
	 */
	public static boolean teleport_check(L1PcInstance pc, L1ItemInstance item) {
	    if (pc.isDead()) {
	        return true;
	    } else if (pc.isParalyzed() || pc.isSleeped()) {
	        return true;
	    } else if ((pc.hasSkillEffect(L1SkillId.EMPIRE) || pc.hasSkillEffect(SHOCK_STUN) || pc.hasSkillEffect(ICE_LANCE) || pc.hasSkillEffect(BONE_BREAK) /*|| pc.hasSkillEffect(THUNDER_GRAB)*/ || pc.hasSkillEffect(EARTH_BIND) || pc.hasSkillEffect(DESPERADO)) && item.getItem().getType() == 17 && item.getItem().isTeleportScroll()) {
	        return true;
	    } else if (pc.isGm()) {
	        return false;
	    } else if (!(pc.getInventory().checkItem(900111) && pc.getMap().isRuler()) && !pc.getMap().isEscapable() && item.getItem().getType() == 17 && item.getItem().isTeleportScroll()) {
	        return true;
	    }
	    return false;
	}
}
