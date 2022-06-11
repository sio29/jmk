package sio29.jmk.backends.gcc;

public class ClTargetType_Gcc{
//	public final static int X86=0;
//	public final static int X64=1;
	//
	public final static String S_X86="x86";
	public final static String S_X64="x64";
	//
	public static boolean isX86(String target_type){
		if(target_type==null)return false;
		return target_type.toLowerCase().equals(S_X86);
	}
	public static boolean isX64(String target_type){
		if(target_type==null)return false;
		return target_type.toLowerCase().equals(S_X64);
	}
	//
//	public static int getTargetType(String type){
//		type=type.toLowerCase();
//		if(type.equals("x86")){
//			return X86;
//		}else if(type.equals("x64")){
//			return X64;
//		}
//		return X64;
//	}
}
