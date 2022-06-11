package sio29.jmk.tools;

public class JmkTimerTools{
	static long g_time=0;
	public static void initTime(){
		g_time=System.nanoTime();
	}
	public static double getTime(){
		long now_time=System.nanoTime();
		long old_time=g_time;
		g_time=now_time;
		return (now_time-old_time)/(1000.0*1000.0*1000.0);
	}
}
