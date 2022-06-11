package sio29.jmk.buildtask;

public class BuildTaskType{
	public final static String S_BUILD="build";
	public final static String S_CLEAN="clean";
	public final static String S_RUN  ="run";
	
	public static boolean isBuild(String task_type){
		if(task_type==null)return false;
		return task_type.toLowerCase().equals(S_BUILD);
	}
	public static boolean isClean(String task_type){
		if(task_type==null)return false;
		return task_type.toLowerCase().equals(S_CLEAN);
	}
	public static boolean isRun(String task_type){
		if(task_type==null)return false;
		return task_type.toLowerCase().equals(S_RUN);
	}
	
}
