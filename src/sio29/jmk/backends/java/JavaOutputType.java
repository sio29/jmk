package sio29.jmk.backends.java;

public class JavaOutputType{
//	public final static int CLASS=0;
//	public final static int JAR  =1;
	
	public final static String S_CLASS="class";
	public final static String S_JAR  ="jar";

	public static boolean isClass(String output_type){
		if(output_type==null)return false;
		return output_type.toLowerCase().equals(S_CLASS);
	}
	public static boolean isJar(String output_type){
		if(output_type==null)return false;
		return output_type.toLowerCase().equals(S_JAR);
	}
	
//	static int getOutputType(String type){
//		type=type.toLowerCase();
//		if(type.equals("jar")){
//			return JAR;
//		}else if(type.equals("class")){
//			return CLASS;
//		}
//		return JAR;
//	}
	
}
