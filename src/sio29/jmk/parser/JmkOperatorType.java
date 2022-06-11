package sio29.jmk.parser;

public class JmkOperatorType{
	public final static int LET      =1;
	public final static int ADD      =2;
	public final static int SUB      =3;
	public final static int MUL      =4;
	public final static int DIV      =5;
	public final static int MOD      =6;
	public final static int ADD_LET  =7;
	public final static int SUB_LET  =8;
	public final static int MUL_LET  =9;
	public final static int DIV_LET  =10;
	public final static int MOD_LET  =11;
	public final static int EQUALS   =12;
	public final static int NOTEQUALS=13;
	
	public final static int IF  =0x1000;
	public final static int ELSE=0x1001;
	
	public static String getOperatorName(int operator_type){
		switch(operator_type){
			case JmkOperatorType.LET:return "=" ;
			case JmkOperatorType.ADD:return "+" ;
			case JmkOperatorType.SUB:return "-" ;
			case JmkOperatorType.MUL:return "*" ;
			case JmkOperatorType.DIV:return "/" ;
			case JmkOperatorType.MOD:return "%" ;
			case JmkOperatorType.ADD_LET:return "+=";
			case JmkOperatorType.SUB_LET:return "-=";
			case JmkOperatorType.MUL_LET:return "*=";
			case JmkOperatorType.DIV_LET:return "/=";
			case JmkOperatorType.MOD_LET:return "%=";
			case JmkOperatorType.EQUALS:return "==";
			case JmkOperatorType.NOTEQUALS:return "!=";
		}
		return null;
	}
	public static boolean isSetOperator(int operator_type){
		switch(operator_type){
			case JmkOperatorType.LET:
			case JmkOperatorType.ADD_LET:
			case JmkOperatorType.SUB_LET:
			case JmkOperatorType.MUL_LET:
			case JmkOperatorType.DIV_LET:
			case JmkOperatorType.MOD_LET:
				return true;
		}
		return false;
	}
	
	public static JmkOperatorPair[] g_operatore_tbl={
		new JmkOperatorPair("==",JmkOperatorType.EQUALS),
		new JmkOperatorPair("=" ,JmkOperatorType.LET),
		new JmkOperatorPair("+" ,JmkOperatorType.ADD),
		new JmkOperatorPair("-" ,JmkOperatorType.SUB),
		new JmkOperatorPair("*" ,JmkOperatorType.MUL),
		new JmkOperatorPair("/" ,JmkOperatorType.DIV),
		new JmkOperatorPair("%" ,JmkOperatorType.MOD),
		new JmkOperatorPair("+=",JmkOperatorType.ADD_LET),
		new JmkOperatorPair("-=",JmkOperatorType.SUB_LET),
		new JmkOperatorPair("*=",JmkOperatorType.MUL_LET),
		new JmkOperatorPair("/=",JmkOperatorType.DIV_LET),
		new JmkOperatorPair("%=",JmkOperatorType.MOD_LET),
		new JmkOperatorPair("!=",JmkOperatorType.NOTEQUALS),
	};
	
	public static int getOperatorType(JmkOperatorPair[] tbl,String op){
		for(int i=0;i<tbl.length;i++){
			String name=tbl[i].name;
			//if(op.startsWith(name))return tbl[i].type;
			if(op.equals(name)){
//System.out.println("op=["+op+"]:type("+tbl[i].type+")");
				return tbl[i].type;
			}
		}
		return -1;
	}
	public static int getOperatorType(String op){
		return getOperatorType(g_operatore_tbl,op);
	}
	public static JmkOperatorPair[] g_func_tbl={
		new JmkOperatorPair("if"     ,JmkOperatorType.IF),
		new JmkOperatorPair("else"   ,JmkOperatorType.ELSE),
		//new JmkOperatorPair("else if",JmkOperatorType.ELSEIF),
	};
	public static int getFuncType(String op){
		return getOperatorType(g_func_tbl,op);
	}
}


