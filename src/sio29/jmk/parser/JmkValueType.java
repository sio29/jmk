package sio29.jmk.parser;

public class JmkValueType{
	public final static int NONE              =0;
	public final static int STRING            =1;
	public final static int STRINGARRAY       =2;
	public final static int INT               =3;
	public final static int BOOL              =4;
	public final static int FLOAT             =5;
	public final static int ARRAY_PAIR_STR_STR=6;
	public final static int MAP_STR_STR       =7;
	public final static int COPYARRAY         =8;
	//
	static String getName(int value_type){
		switch(value_type){
			case STRING:return "STRING";
			case STRINGARRAY:return "STRINGARRAY";
			case INT:return "INT";
			case BOOL:return "BOOL";
			case FLOAT:return "FLOAT";
			case ARRAY_PAIR_STR_STR:return "ARRAY_PAIR_STR_STR";
			case MAP_STR_STR:return "MAP_STR_STR";
			case COPYARRAY:return "COPYARRAY";
		}
		return "---";
	}
}

