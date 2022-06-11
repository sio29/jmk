package sio29.jmk.cltools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

//Cのソースのincludeを得る
public class CSourceIncludeCollector{
	static class LineState{
		int comment_flg=0;
		char string_start_m=0;
	}
	public static File[] getIncludeList(String filename,File[] include_paths){
		if(filename==null)return null;
		return getIncludeList(new File(filename),include_paths);
	}
	public static File[] getIncludeList(File file,File[] include_paths){
//System.out.println("getIncludeList_01");
		if(file==null)return null;
		try{
			file=file.getCanonicalFile();
		}catch(Exception ex){
			return null;
		}
//System.out.println("getIncludeList_02");
		//※ ここでループしている!!
		String[] incs=getIncludeList(file);
//System.out.println("getIncludeList_03");
		File file_path=file.getParentFile();
//System.out.println("getIncludeList_04");
		
		//File file_path=file.getAbsoluteFile().getParentFile();
		//
		HashSet<File> incs_file=new HashSet<File>();
		for(int i=0;i<incs.length;i++){
			String inc=incs[i];
			File inc_file=findIncudePath(inc,file_path,include_paths);
			if(inc_file!=null){
				incs_file.add(inc_file);
			}
		}
//System.out.println("getIncludeList_05");
		//
		return (File[])incs_file.toArray(new File[]{});
	}
	static File findIncudePath(String inc,File file,File[] include_paths){
		File inc_path=findIncudePath(inc,file);
		if(inc_path!=null)return inc_path;
		for(int i=0;i<include_paths.length;i++){
			inc_path=findIncudePath(inc,include_paths[i]);
			if(inc_path!=null)return inc_path;
		}
		//
		//ファイルがない場合
		return new File(inc);
	}
	static File findIncudePath(String inc,File path){
		File inc_file=new File(path,inc);
		if(!inc_file.exists())return null;
		try{
			inc_file=inc_file.getCanonicalFile();
		}catch(Exception ex){
			return null;
		}
		return inc_file;
	}
	
	
	public static String[] getIncludeList(String filename){
		return getIncludeList(new File(filename));
	}
	//includeリストを求める
	public static String[] getIncludeList(File file){
//System.out.println("getIncludeList_100");
		LineState linestate=new LineState();
		HashSet<String> includes=new HashSet<String>();
		FileInputStream is=null;
		BufferedReader br=null;
		int ln=0;
		try{
//System.out.println("getIncludeList_101");
			is=new FileInputStream(file);
			br=new BufferedReader(new InputStreamReader(is));
			while(true){
//System.out.println(String.format("%s[%d]",file,ln+1));
				String line=br.readLine();ln++;
				if(line==null)break;
//System.out.println("line["+ln+"]="+line);
				//
				if(isSpaceLine(line))continue;
				line=removeCommentString(line,linestate);
				if(line==null || line.length()==0)continue;
				if(isSpaceLine(line))continue;
//System.out.println("line["+ln+"]="+line);
				String inc=getInclude(line);
				if(inc==null)continue;
//System.out.println("line["+ln+"],inc="+inc);
				includes.add(inc);
			}
//System.out.println("getIncludeList_109");
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(is!=null){
					is.close();
				}
			}catch(Exception ex){}
		}
		return (String[])includes.toArray(new String[]{});
	}
	//最小オフセットを求める
	private static int getMinOffset(int[] offs){
		int min_off=Integer.MAX_VALUE;
		int min_index=-1;
		for(int i=0;i<offs.length;i++){
			int off=offs[i];
			if(off<0)continue;
			if(off<min_off){
				min_off=off;
				min_index=i;
			}
		}
		return min_index;
	}
	//コメントを取り除いた一行
	private static String removeCommentString(String line,LineState linestate){
		int comment_top=0;
		int offset=0;
		while(true){
			if(linestate.comment_flg==0){
			//コメントでも文字列中でもない
				int ci0=line.indexOf("/*",offset);
				int ci1=line.indexOf("//",offset);
				int si0=line.indexOf("\"",offset);
				int si1=line.indexOf("\'",offset);
				int[] offs={ci0,ci1,si0,si1};
				int mo=getMinOffset(offs);
				//行内にない
				if(mo==-1)return line;
				switch(mo){
					case 0:
					//複数行コメント中
						linestate.comment_flg=1;
						comment_top=ci0;
						offset=ci0+2;
						break;
					case 1:
					//一行コメント中
						return line.substring(0,ci1);
					case 2:
					//文字列中、ダブルコーテーション
						linestate.comment_flg=2;
						linestate.string_start_m='\"';
						offset=si0+1;
						break;
					case 3:
					//文字列中、シングルコーテーション
						linestate.comment_flg=2;
						linestate.string_start_m='\'';
						offset=si1+1;
						break;
				}
			}else if(linestate.comment_flg==1){
			//複数行コメント中
				int i=line.indexOf("*/",offset);
				if(i>=0){
					linestate.comment_flg=0;
					int comment_bottom=i+2;
					StringBuilder sb=new StringBuilder();
					String line_top="";
					if(comment_top>0){
						sb.append(line.substring(0,comment_top));
					}
					int len=comment_bottom-comment_top;
					for(int j=0;j<len;j++){
						sb.append(' ');
					}
					String line_bottom=line.substring(i+2);
					sb.append(line_bottom);
					line=sb.toString();
				}else{
				//行内にない
					if(comment_top==0)return null;
					return line.substring(0,comment_top);
				}
			}else if(linestate.comment_flg==2){
			//文字列中
				int i=line.indexOf(linestate.string_start_m,offset);
				if(i>=0){
					offset=i+1;
					linestate.comment_flg=0;
				}else{
				//行内にない
					return line;
				}
			}
		}
	}
	//空行?
	private static boolean isSpaceLine(String line){
		if(line==null)return true;
		int len=line.length();
		if(len==0)return true;
		for(int i=0;i<len;i++){
			char m=line.charAt(i);
			if(m!=0x20 && m!=0x09)return false;
		}
		return true;
	}
	//spaceをskip
	private static int skipSpace(String line,int off){
		if(line==null)return off;
		int len=line.length();
		if(len==0)return off;
		if(off>=len)return off;
		int i=off;
		while(true){
			char m=line.charAt(i);
			if(m!=0x20 && m!=0x09)break;
			i++;
		}
		return i;
	}
	public final static int g_inc_len="#include".length();
	//includeの獲得
	private static String getInclude(String line){
		if(line==null)return null;
		int len=line.length();
		if(len<=g_inc_len)return null;
		int off=skipSpace(line,0);
		if(!line.startsWith("#",off)){
			//System.out.println("not # !!:off("+off+")");
			return null;
		}
		off=skipSpace(line,off+1);
		String include_m="include";
		if(!line.startsWith(include_m,off)){
			//System.out.println("not include !!:off("+off+")");
			return null;
		}
		off+=include_m.length();
		//
		int i0=line.indexOf("\"",off);
		int i1=line.indexOf("<",off);
		int[] offs={i0,i1};
		int mo=getMinOffset(offs);
		if(mo==-1)return null;
		int inc_top=-1;
		int inc_bottom=-1;
		switch(mo){
			case 0:
				{
				inc_top=i0;
				inc_bottom=line.indexOf("\"",i0+1);
				}
				break;
			case 1:
				{
				inc_top=i1;
				inc_bottom=line.indexOf(">",i1+1);
				}
				break;
		}
		if(inc_top<0 || inc_bottom<0)return null;
		return line.substring(inc_top+1,inc_bottom);
	}
}

