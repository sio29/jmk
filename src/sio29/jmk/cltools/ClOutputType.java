package sio29.jmk.cltools;

public class ClOutputType{
//	public final static int OBJ=0;
//	public final static int EXE=1;
//	public final static int LIB=2;
//	public final static int DLL=3;
	//
	public final static String S_OBJ="obj";
	public final static String S_EXE="exe";
	public final static String S_LIB="lib";
	public final static String S_DLL="dll";
	//
	public static boolean isObj(String output_type){
		if(output_type==null)return false;
		return output_type.toLowerCase().equals(S_OBJ);
	}
	public static boolean isExe(String output_type){
		if(output_type==null)return false;
		return output_type.toLowerCase().equals(S_EXE);
	}
	public static boolean isLib(String output_type){
		if(output_type==null)return false;
		return output_type.toLowerCase().equals(S_LIB);
	}
	public static boolean isDll(String output_type){
		if(output_type==null)return false;
		return output_type.toLowerCase().equals(S_DLL);
	}
//	public static int getOutputType(String output_type){
//		String type=output_type.toLowerCase();
//		if(type.equals(S_EXE)){
//			return EXE;
//		}else if(type.equals(S_OBJ)){
//			return OBJ;
//		}else if(type.equals(S_LIB)){
//			return LIB;
//		}else if(type.equals(S_DLL)){
//			return DLL;
//		}
//		return EXE;
//	}
}
