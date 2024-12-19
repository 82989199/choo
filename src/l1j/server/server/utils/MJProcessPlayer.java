package l1j.server.server.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Calendar;

import l1j.server.server.model.gametime.RealTimeClock;

public class MJProcessPlayer {
	public static String getPid(){
		String s = ManagementFactory.getRuntimeMXBean().getName();
		return s.substring(0, s.indexOf("@"));
	}
	
	public static void dumpLog(){
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		String[] command = new String[]{
				"cmd", "/C", "C:\\Program Files\\Java\\jdk1.8.0_101\\bin\\jstack", getPid(), ">", 
				String.format("dump\\[%02d-%02d-%02d]dump.txt", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND))
		};
		MJProcessPlayer mpp = new MJProcessPlayer();
		try {
			mpp.byRuntime(command);
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}
	
	public void byRuntime(String[] command) throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command);
		printStream(process);
		
	}

	public void byProcessBuilder(String[] command) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		printStream(process);
	}

	private void printStream(Process process) throws IOException, InterruptedException {
		process.waitFor();
		try (InputStream psout = process.getInputStream()) {
			copy(psout, System.out);
		}
	}

	public void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		int n = 0;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
	}
}
