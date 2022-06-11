package sio29.jmk.tools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public class JmkStringTools{
	public static ArrayList<String> toArrayListString(String[] arg){
		ArrayList<String> list=new ArrayList<String>();
		for(int i=0;i<arg.length;i++){
			list.add(arg[i]);
		}
		return list;
	}
	public static String[] addString(String[] arg,String s){
		String[] ret=new String[arg.length+1];
		int i=0;
		for(;i<arg.length;i++){
			ret[i]=arg[i];
		}
		ret[i]=s;
		return ret;
	}
	public static String[] addString(String[] arg,String[] s){
		String[] ret=new String[arg.length+s.length];
		int ii=0;
		for(int i=0;i<arg.length;i++){
			ret[ii]=arg[i];
			ii++;
		}
		for(int i=0;i<s.length;i++){
			ret[ii]=s[i];
			ii++;
		}
		return ret;
	}
	public static void addString(List<String> list,String[] s){
		for(int i=0;i<s.length;i++){
			list.add(s[i]);
		}
	}
	public static void addString(List<String> list,List<String> s){
		for(int i=0;i<s.size();i++){
			list.add(s.get(i));
		}
	}
	public static void printStrings(String[] ret){
		for(int i=0;i<ret.length;i++){
			System.out.println("["+i+"]:"+ret[i]);
		}
	}
	public static HashSet<String> toHashSet(String[] arg){
		HashSet<String> set=new HashSet<String>();
		for(int i=0;i<arg.length;i++){
			set.add(arg[i]);
		}
		return set;
	}
	public static void addHashSet(HashSet<String> set,String[] arg){
		if(arg==null)return;
		for(int i=0;i<arg.length;i++){
			set.add(arg[i]);
		}
	}
	public static void addArrayList(ArrayList<String> set,String[] arg){
		if(arg==null)return;
		for(int i=0;i<arg.length;i++){
			set.add(arg[i]);
		}
	}
	public static String[] compareOpt(String[] opt1,String[] opt2){
		HashSet<String> set1=toHashSet(opt1);
		HashSet<String> set2=toHashSet(opt2);
		HashSet<String> ret=new HashSet<String>();
		for(String n : set1){
			if(!set2.contains(n)){
				ret.add(n);
			}
		}
		for(String n : set2){
			if(!set1.contains(n)){
				ret.add(n);
			}
		}
		String[] ret2=(String[])ret.toArray(new String[]{});
		Arrays.sort(ret2);
		return ret2;
	}
	@SuppressWarnings("unchecked")
	public static String[] getCommonOpt(String[][] opts){
		Object[] sets=new Object[opts.length];
		for(int i=0;i<opts.length;i++){
			sets[i]=toHashSet(opts[i]);
		}
		HashSet<String> ret=new HashSet<String>();
		for(int i=0;i<opts.length;i++){
			addHashSet(ret,opts[i]);
		}
		HashSet<String> ret2=new HashSet<String>();
		Class c=ret2.getClass();
		for(String n : ret){
			boolean set_flg=true;
			for(int i=0;i<opts.length;i++){
				HashSet<String> set=(HashSet<String>)sets[i];
				if(!set.contains(n)){
					set_flg=false;
					break;
				}
			}
			if(set_flg){
				ret2.add(n);
			}
		}
		String[] ret3=(String[])ret2.toArray(new String[]{});
		Arrays.sort(ret3);
		return ret3;
	}
	public static void printCompareOpt(String name1,String[] opt1,String name2,String[] opt2){
		String[] ret=compareOpt(opt1,opt2);
		System.out.println("compare("+name1+","+name2+")");
		printStrings(ret);
	}
	//=================================================
	public static void printHex(String m){
		int len=m.length();
		for(int i=0;i<len;i++){
			int n=m.codePointAt(i);
			System.out.println(String.format("[%d]:code(%08x):%c",i,n,n));
		}
	}
	//=================================================
	public static String removeCR(String m){
		if(m==null)return m;
		int i;
		i=m.lastIndexOf("\n");
		if(i>=0){
			m=m.substring(0,i);
		}else{
		}
		i=m.lastIndexOf("\r");
		if(i>=0){
			m=m.substring(0,i);
		}else{
		}
		return m;
	}
	//=================================================
	public static String getConnectString(ArrayList<String> cmd,String add){
		String m="";
		for(int i=0;i<cmd.size();i++){
			if(i!=0)m+=add;
			m+=cmd.get(i);
		}
		return m;
	}
	//=================================================
	public static void printHashMap(Map<String,String> map){
		if(map==null){
			System.out.println("map==null !!");
			return;
		}
		if(map.size()==0){
			System.out.println("map is key none !!");
			return;
		}
		for(String key : map.keySet()){
			String v=map.get(key);
			System.out.println(key+" = "+v);
		}
	}
	//=================================================
	public static Map<String,String> toLowerCaseMap(Map<String,String> env){
		if(env==null)return null;
		Map<String,String> map=new HashMap<String,String>();
		for(String key : env.keySet()){
			String v=env.get(key);
			key=key.toLowerCase();
			map.put(key,v);
		}
		return map;
	}
	//=================================================
	public static Map<String,String> getSystemEnvs(){
		return System.getenv();
	}
	//=================================================
	public static Map<String,String> addMap(Map<String,String> src,Map<String,String> dst){
		Map<String,String> map=new HashMap<String,String>();
		if(src!=null)map.putAll(src);
		if(dst!=null)map.putAll(dst);
		return map;
	}
	//ï∂éöóÒÇ…"Å`"ÇÇ¬ÇØÇÈ
	public static String makeOptString(String name){
		String m="";
		m+="\"";
		if(name!=null){
			m+=name;
		}
		m+="\"";
		return m;
	}
}
