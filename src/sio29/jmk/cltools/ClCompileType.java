package sio29.jmk.cltools;

public class ClCompileType{
//	public final static int DEBUG  =0;
//	public final static int RELEASE=1;
//	public final static int DEVELOP=2;
	//
	public final static String S_DEBUG  ="debug";
	public final static String S_RELEASE="release";
	public final static String S_DEVELOP="develop";
	//
	public static boolean isDebug(String compile_type){
		if(compile_type==null)return false;
		return compile_type.toLowerCase().equals(S_DEBUG);
	}
	public static boolean isRelease(String compile_type){
		if(compile_type==null)return false;
		return compile_type.toLowerCase().equals(S_RELEASE);
	}
	public static boolean isDevelop(String compile_type){
		if(compile_type==null)return false;
		return compile_type.toLowerCase().equals(S_DEVELOP);
	}
	//
//	public static int getCompileType(boolean debug_flg){
//		if(debug_flg){
//			return DEBUG;
//		}else{
//			return RELEASE;
//		}
//	}
}
