package MJShiftObject.Object.Converter;

import MJShiftObject.Object.MJShiftObject;

public interface IMJShiftObjectDBConverter {
	public static final int CONVERT_SUCCESS = 1;
	public static final int CONVERT_FAIL_NOT_FOUND_PC = 2;
	public static final int CONVERT_FAIL_NOT_FOUND_ACCOUNT = 4;
	public static final int CONVERT_FAIL_INVALID = 8;
	
	public int work(final MJShiftObject sobject);
}
