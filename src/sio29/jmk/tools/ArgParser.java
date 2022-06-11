//package sio29.ulib.argreader;
package sio29.jmk.tools;

import java.util.*;

public class ArgParser{
	public String[] args;
	public int arg_index;
	
	public ArgParser(String[] _args,int _arg_index){
		args=_args;
		arg_index=_arg_index;
	}
	public ArgParser(String[] _args){
		this(_args,0);
	}
	public String get(){
		if(arg_index>=args.length)return null;
		String m=args[arg_index];
		arg_index++;
		return m;
	}
	public String getCurrent(){
		if(arg_index>=args.length)return null;
		return args[arg_index];
	}
	public void next(){
		arg_index++;
	}
	public void prev(){
		arg_index--;
	}
	public int getIndex(){
		return arg_index;
	}
	public String getOpt(){
		String m=get();
		if(m==null)return null;
		if(m.length()>=2){
			if(isOpt(m)){
				return m.substring(1).toLowerCase();
			}
		}
		return null;
	}
	public boolean isEnded(){
		return (arg_index>=args.length);
	}
	public static boolean isOpt(String m){
		char m1=m.charAt(0);
		return (m1=='-' || m1=='/');
	}
	public static boolean isInteger(String m){
		try{
			Integer.parseInt(m);
			return true;
		}catch(Exception ex){
		}
		return false;
	}
	public boolean isInteger(){
		return isInteger(getCurrent());
	}
	public boolean isInt(){
		return isInteger(getCurrent());
	}
	public static int getInteger(String m){
		try{
			return Integer.parseInt(m);
		}catch(Exception ex){
		}
		return 0;
	}
	public int getInteger(){
		return getInteger(get());
	}
	public int getInt(){
		return getInteger(get());
	}
	public String[] getFilenames(){
		ArrayList<String> filenames=new ArrayList<String>();
		while(true){
			if(isEnded())break;
			String filename=get();
			char m=filename.charAt(0);
			if(m=='-' || m=='/'){
				prev();
				break;
			}
			filenames.add(filename);
		}
		return (String[])filenames.toArray(new String[]{});
	}
	public int[] getBounds(int[] _bounds){
		int[] bounds=new int[2];
		bounds[0]=_bounds[0];
		bounds[1]=_bounds[1];
		String m=getCurrent();
		if(isEnded()){
			return bounds;
		}
		int index=m.indexOf("-");
		if(index<0){
			if(!ArgParser.isInteger(m)){
				return bounds;
			}
			bounds[0]=ArgParser.getInteger(m);
			next();
		}else if(index==0){
			if(1<m.length()){
				bounds[1]=ArgParser.getInteger(m.substring(1));
				next();
			}
		}else{
			bounds[0]=ArgParser.getInteger(m.substring(0,index));
			if((index+1)<m.length()){
				bounds[1]=ArgParser.getInteger(m.substring(index+1));
			}
			next();
		}
		return bounds;
	}
	public String[] getLeftArgs(){
		int left_num=args.length-arg_index;
		String[] left_args=new String[left_num];
		for(int i=0;i<left_num;i++){
			left_args[i]=args[arg_index];
			arg_index++;
		}
		return left_args;
	}
	public void parseEnd(){
		arg_index=args.length;
	}
}
