package sio29.jmk.parser;

import java.util.*;
import java.util.AbstractMap.*;
import java.io.*;
import java.lang.reflect.*;

//import sio29.ulib.ufile.*;
//import sio29.ulib.ufile.url.*;
//import sio29.ulib.mqotext.*;

import sio29.jmk.tools.*;


public class JmkParser{
//	static {
//		try{
//			Class.forName("sio29.ulib.ufile.url.FileBaseURL");
//		}catch(Exception ex){}
//	}
	public static JmkBuildParam load(String filename,JmkParserPreParam pre_param){
		try{
			//File file=FileBuilder.createLocal(filename);
			//File file=new File(_filename).toURI().toURL();
			//
			File file=new File(filename);
			if(file==null){
				return null;
			}
			if(!file.exists()){
				return null;
			}
			return load(file,pre_param);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	public static JmkBuildParam load(File filename,JmkParserPreParam pre_param){
		JmkBuildParam param=new JmkBuildParam();
		if(!load(param,filename,pre_param)){
			return null;
		}
		return param;
	}
	public static boolean load(JmkBuildParam param,File filename,JmkParserPreParam pre_param){
		try{
			//File file=((FileURL)filename).getFile();
			File file=filename;
			param.setJmkFilename(file.toString());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		JmkText mem=new JmkText();
		if(!mem.LoadFromFile(filename)){
			System.out.println("load error !!:"+filename);
			return false;
		}
		boolean r=load(mem,param,filename,pre_param);
		if(!r){
			System.out.println("load error !!:"+param.getJmkFilename()+" ("+mem.GetLineNum()+")");
		}
		return r;
	}
	//==============
	static class SikiParam{
		String key;
		String siki;
		String value;
	}
	//==============
	static Object getKeyValue(JmkBuildParam param,JmkParserPreParam pre_param,String key){
		if(key.startsWith("\"")){
			int i0=key.indexOf("\"");
			int i1=key.lastIndexOf("\"");
			String m=key.substring(i0+1,i1);
//System.out.println("str="+m);
			return m;
		}else if(key.equals("compiler_type")){
			String m=null;
			if(pre_param.compiler_type!=null){
				m=pre_param.compiler_type;
			}else{
				m=param.getCompilerType();
			}
//System.out.println("var="+m);
			return m;
		}
		return null;
	}
	//==============
	//==============
	static boolean calcSiki(String siki,Object var1,Object var2){
		if(var1==null)return false;
		if(var2==null)return false;
		if(siki.startsWith("==")){
//System.out.println("siki==");
			return var1.equals(var2);
		}else if(siki.startsWith("!=")){
//System.out.println("siki!=");
			return !var1.equals(var2);
		}
		return false;
	}
	//==============
	static boolean checkSiki(JmkBuildParam param,JmkParserPreParam pre_param,SikiParam siki){
		Object var1=getKeyValue(param,pre_param,siki.key);
		Object var2=getKeyValue(param,pre_param,siki.value);
		return calcSiki(siki.siki,var1,var2);
	}
	//==============
	//public String compiler_type;
	//public String compile_type;
	//public String target_type;
	static void setVarsPrePram(HashMap<String,Object> vars,JmkParserPreParam pre_param){
		if(pre_param.compiler_type!=null){
			vars.put("compiler_type",pre_param.compiler_type);
		}
		if(pre_param.compile_type!=null){
			vars.put("compile_type",pre_param.compile_type);
		}
		if(pre_param.target_type!=null){
			vars.put("target_type",pre_param.target_type);
		}
	}
	//==============
	public static class JmkOperatorLeaf{
		public int operator;
		public Object left;
		public Object right;
		public JmkOperatorLeaf(int _op,Object _left,Object _right){
			operator=_op;
			left=_left;
			right=_right;
		}
		static String toStringOperand(Object operand){
			if(operand==null)return "null";
			if(operand instanceof JmkValueBase){
				return ((JmkValueBase)operand).name;
			}else if(operand instanceof JmkOperatorBuffer){
				return ((JmkOperatorBuffer)operand).toString(" ");
			}else{
				return operand.toString();
			}
		}
		
		public String toString(){
			if(operator==JmkOperatorType.IF){
				String left_m =toStringOperand(left);
				String right_m=toStringOperand(right);
				String m="\nif_check--\n";
				m+=left_m;
				m+="\nif_true--\n";
				m+=right_m;
				return m;
			}else{
				String left_m =toStringOperand(left);
				String right_m=toStringOperand(right);
				String op_m=JmkOperatorType.getOperatorName(operator);
				String m=String.format("%s [%s/%d] %s",left_m,op_m,operator,right_m);
				return m;
			}
		}
		public Object exec(){
			if(operator==JmkOperatorType.IF){
				Object if_check_ret=null;
				if(left instanceof JmkOperatorBuffer){
//System.out.println("exec:if_check");
					if_check_ret=((JmkOperatorBuffer)left).exec();
				}
				if(if_check_ret instanceof Boolean){
//System.out.println("exec:if:"+(Boolean)if_check_ret);
					if((Boolean)if_check_ret){
						if(right instanceof JmkOperatorBuffer){
							((JmkOperatorBuffer)right).exec();
						}
					}
				}
			}else{
				if(left instanceof JmkValueBase){
					return ((JmkValueBase)left).exec(operator,right);
				}
			}
			return null;
		}
	};
	public static class JmkOperatorBuffer{
		ArrayList<JmkOperatorLeaf> list=new ArrayList<JmkOperatorLeaf>();
		
		public void add(JmkOperatorLeaf opp){
			list.add(opp);
		}
		public String toString(String tab){
			String m="";
			for(int i=0;i<list.size();i++){
				JmkOperatorLeaf opp=list.get(i);
				m+=String.format("%s[%d]:%s\n",tab,i,opp.toString());
			}
			return m;
		}
		public void print(){
			System.out.println(toString(""));
		}
		public Object exec(){
			Object ret=null;
			for(int i=0;i<list.size();i++){
				JmkOperatorLeaf opp=list.get(i);
				ret=opp.exec();
			}
			return ret;
		}
	};
	
	
	static boolean putOperator(JmkOperatorBuffer buff,JmkText mem,JmkValueBase left_value){
		if(left_value instanceof JmkValueCopyArray){
			if(!mem.CheckEqual())return false;
			String src=mem.FGetJmkStr();
			if(!mem.CheckComma())return false;
			String dst=mem.FGetJmkStr();
			JmkPairStrStr right_value=new JmkPairStrStr(src,dst);
			buff.add(new JmkOperatorLeaf(JmkOperatorType.LET,left_value,right_value));
			return true;
		}else if(left_value instanceof JmkValueMapStrStr){
			String key=mem.FGetJmkStr();
			if(!mem.CheckEqual())return false;
			String value=mem.FGetJmkStr();
			AbstractMap.SimpleEntry<String,String> right_value=new AbstractMap.SimpleEntry<String,String>(key,value);
			buff.add(new JmkOperatorLeaf(JmkOperatorType.LET,left_value,right_value));
			return true;
		}else{
			String op_chunk=mem.FGetChunk();
			int op=JmkOperatorType.getOperatorType(op_chunk);
			Object right_value=null;
			if(left_value instanceof JmkValueString){
				right_value=mem.FGetJmkStr();
			}else if(left_value instanceof JmkValueStringArray){
				right_value=mem.FGetJmkStr();
			}else if(left_value instanceof JmkValueInt){
				right_value=mem.FGetInt();
			}else if(left_value instanceof JmkValueFloat){
				right_value=mem.FGetFloat();
			}
			//buff.add(new JmkOperatorLeaf(JmkOperatorType.LET,left_value,right_value));
			buff.add(new JmkOperatorLeaf(op,left_value,right_value));
			return true;
		}
		//return false;
	}
	public static boolean execOperatorBuffer(JmkOperatorBuffer buff,JmkBuildParam param){
		
		return true;
	}
	//==============
	public static boolean load(JmkText mem,JmkBuildParam param,File filename,JmkParserPreParam pre_param){
		setPreParam(param,pre_param);
		if(!parse(mem,param,filename))return false;
		if(!postLoad(param,pre_param))return false;
		System.out.println("load ok:"+filename);
		return true;
	}
	//==============
	public static boolean parse(JmkText mem,JmkBuildParam param,File filename){
		String head=mem.FGetLine();
		if(!head.startsWith("jmk")){
			System.out.println("is not jmk file!!:"+filename);
			System.out.println(""+head);
			return false;
		}
		//
		JmkOperatorBuffer buff=new JmkOperatorBuffer();
		boolean r=parseSub(buff,mem,param,filename);
		buff.exec();
		return true;
	}
	public static boolean parseSub(JmkOperatorBuffer buff,JmkText mem,JmkBuildParam param,File filename){
		JmkValueList valuelist=param.getValueList();
		boolean if_flg=false;
		boolean if_exec_flg=true;
		while(true){
			if(mem.CheckEOF())break;
			String chunk=mem.FGetChunk();
			if(chunk==null)break;
			if(chunk.equals("Eof") || chunk.equals("eof"))break;
			if(chunk.equals(")"))break;
			if(chunk.equals("}"))break;
			//
			JmkValueBase left_value=valuelist.getByCommand(chunk);
			if(left_value!=null){
				putOperator(buff,mem,left_value);
			}else{
				int func_type=JmkOperatorType.getFuncType(chunk);
//System.out.println("chunk:["+chunk+"]:type("+func_type+")");
				if(func_type>0){
					switch(func_type){
						case JmkOperatorType.IF:
//System.out.println("JmkOperatorType.IF");
							{
							if(!mem.CheckKakkoML())return false;
							JmkOperatorBuffer if_check_buff=new JmkOperatorBuffer();
							if(!parseSub(if_check_buff,mem,param,filename)){
								return false;
							}
							
//System.out.println("if_check_buff ****");
							//if_check_buff.print();
//							Object if_check_ret=if_check_buff.exec();
//System.out.println("if_check_buff:ret="+if_check_ret);
							
							if(!mem.CheckKakkoL())return false;
							//
							JmkOperatorBuffer if_true_buff=new JmkOperatorBuffer();
							if(!parseSub(if_true_buff,mem,param,filename)){
								return false;
							}
//System.out.println("if_true_buff ****");
							buff.add(new JmkOperatorLeaf(JmkOperatorType.IF,if_check_buff,if_true_buff));
							}
							break;
						case JmkOperatorType.ELSE:
							break;
					}
				}else{
					System.out.println("["+chunk+"]‘Î‰žŠO‚Ìƒ`ƒƒƒ“ƒN‚Å‚·");
					return false;
				}
			}
		}
		return true;
	}
	//====================
	public static boolean postLoad(JmkBuildParam param,JmkParserPreParam pre_param){
		JmkValueList valuelist=param.getValueList();
		setPreParam(param,pre_param);
		Map<String,String> envs=getEnvs(param);
		replaceEnv(envs);
		param.replaceEnv(envs);
		//System.out.println(valuelist.toString());
		printEchos(param);
		//printPreBuilds(param);
		return true;
	}
	//====================
	public static void setPreParam(JmkBuildParam param,JmkParserPreParam pre_param){
		if(pre_param!=null){
			JmkValueList valuelist=param.getValueList();
			if(pre_param.compiler_type!=null){
//System.out.println("pre_param.compiler_type");
				param.setCompilerType(pre_param.compiler_type);
				valuelist.setEnableSetByField(JmkBuildParam.COMPILER_TYPE,false);
			}
			if(pre_param.compile_type!=null){
				param.setCompileType(pre_param.compile_type);
				valuelist.setEnableSetByField(JmkBuildParam.COMPILE_TYPE,false);
			}
			if(pre_param.target_type!=null){
				param.setTargetType(pre_param.target_type);
				valuelist.setEnableSetByField(JmkBuildParam.TARGET_TYPE,false);
			}
		}
	}
	//====================
	public static Map<String,String> getEnvs(JmkBuildParam param){
		Map<String,String> envs=new HashMap<String,String>(JmkStringTools.getSystemEnvs());
		//
		if(param.getCompilerType()!=null){
			envs.put("compiler_type",param.getCompilerType());
		}
		if(param.getCompileType()!=null){
			envs.put("compile_type",param.getCompileType());
		}
		if(param.getTargetType()!=null){
			envs.put("target_type",param.getTargetType());
		}
		//
		envs=JmkStringTools.addMap(envs,param.getEnvs());
		envs=JmkStringTools.toLowerCaseMap(envs);
		
		return envs;
	}
	//====================
	public static void printEnvs(Map<String,String> envs){
		for(String key : envs.keySet()){
			String value=envs.get(key);
			System.out.println(String.format("envs[%s]=%s",key,value));
		}
	}
	//====================
	public static void printEchos(JmkBuildParam param){
		String[] echos=param.getEchos();
		//if(echos==null)return;
		for(int i=0;i<echos.length;i++){
			String m=echos[i];
			System.out.println("echo: "+m);
		}
	}
	public static void printPreBuilds(JmkBuildParam param){
		String[] pre_builds=param.getPreBuilds();
		//if(pre_builds==null)return;
		for(int i=0;i<pre_builds.length;i++){
			String m=pre_builds[i];
			System.out.println("pre_builds: "+m);
		}
	}
	//====================
	final static String g_sm="${";
	final static String g_em="}";
	public static String replaceEnv(Map<String,String> envs,String src){
		if(!hasReplaceEnv(src))return src;
		String new_m=replaceEnvSub(envs,src);
//System.out.println(src+" -> "+new_m);
		return new_m;
	}
	public static String replaceEnvSub(Map<String,String> envs,String src){
		if(src==null)return null;
		int len=src.length();
		if(len==0)return src;
		int top=0;
		int i0=src.indexOf(g_sm,top);
		if(i0<0)return src;
		int i1=src.indexOf(g_em,i0+2);
		if(i1<0)return src;
		//
		//
		String key=src.substring(i0+2,i1);
		key=key.toLowerCase();
		String rep_m="";
		if(envs.containsKey(key)){
			rep_m=envs.get(key);
		}
		String m0="";
		String m1="";
		if(i0>0)m0=src.substring(0,i0);
		if((i1+1)<len)m1=src.substring(i1+1);
		String new_src=m0+rep_m+m1;
		return replaceEnvSub(envs,new_src);
	}
	public static boolean hasReplaceEnv(String m){
		if(m==null)return false;
		return (m.indexOf(g_sm)!=-1);
	}
	public static void replaceEnv(Map<String,String> envs){
		while(true){
			boolean flg=true;
			for(String key : envs.keySet()){
				String value=envs.get(key);
				if(!hasReplaceEnv(value))continue;
				flg=false;
				String new_value=replaceEnv(envs,value);
				envs.put(key,new_value);
//System.out.println(value+" -> "+new_value);
			}
			if(flg)break;
		}
	}
	
	public static Map<String,String> replaceEnv(Map<String,String> envs,Map<String,String> map){
		Map<String,String> new_map=new HashMap<String,String>();
		for(String key : map.keySet()){
			String value=map.get(key);
			String new_value=value;
			if(hasReplaceEnv(value)){
				new_value=replaceEnv(envs,value);
			}
			new_map.put(key,new_value);
		}
		return new_map;
	}
	
	public static String[] replaceEnv(Map<String,String> envs,String[] src){
		String[] dst=new String[src.length];
		for(int i=0;i<src.length;i++){
			dst[i]=replaceEnv(envs,src[i]);
		}
		return dst;
	}
	public static ArrayList<String> replaceEnv(Map<String,String> envs,ArrayList<String> src){
		ArrayList<String> dst=new ArrayList<String>();
		for(int i=0;i<src.size();i++){
			dst.add(replaceEnv(envs,src.get(i)));
		}
		return dst;
	}
	public static JmkCopyPair replaceEnv(Map<String,String> envs,JmkCopyPair src){
		String _src=replaceEnv(envs,src.src);
		String _dst=replaceEnv(envs,src.dst);
		return new JmkCopyPair(_src,_dst);
	}
	public static JmkCopyPair[] replaceEnv(Map<String,String> envs,JmkCopyPair[] src){
		JmkCopyPair[] dst=new JmkCopyPair[src.length];
		for(int i=0;i<src.length;i++){
			dst[i]=replaceEnv(envs,src[i]);
		}
		return dst;
	}
	public static ArrayList<JmkCopyPair> replaceEnvCopyPairArray(Map<String,String> envs,ArrayList<JmkCopyPair> src){
		ArrayList<JmkCopyPair> dst=new ArrayList<JmkCopyPair>();
		for(int i=0;i<src.size();i++){
			dst.add(replaceEnv(envs,src.get(i)));
		}
		return dst;
	}
	public static JmkCopyPair[] replaceEnvCopyPairArray(Map<String,String> envs,JmkCopyPair[] src){
		JmkCopyPair[] dst=new JmkCopyPair[src.length];
		for(int i=0;i<src.length;i++){
			dst[i]=replaceEnv(envs,src[i]);
		}
		return dst;
	}
}
