package sio29.jmk.backends.java;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;

public class JavaParam{
	public String build_dir;
	public String[] classpaths;
	public String[] add_jars;
	public String[] defs;
	public String main_class;
	public String jar_filename;
	//
	public String build_dir_tmp;
	public String[] classpaths_tmp;
	//
	public JavaParam(JavacParam param){
	}
	//=================================================
	private static JavaParam JavacParamToJavaParam(JavacParam param){
		return new JavaParam(param);
	}
	//=================================================
	public boolean execJava(JavaToolsEnv tools_env){
		String[] opt=JavaParam.converJavaParam(this,tools_env);
		return tools_env.execJava(opt);
	}
	//=================================================
	//cl.exeのオプション
	public static String[] getJavaOpt(final JavaParam param,JavaToolsEnv tools_env){
		//buildディレクトリ名作成
		param.build_dir_tmp=tools_env.makeBuildDir(param.build_dir);
		//libファイル名作成
		makeClassPathsTmp(param);
		//======================
		//Cmd.exeのパラメータ作成
		HashSet<String> set=new HashSet<String>();
		JmkStringTools.addHashSet(set,java_add_opt_common);
		//define
		/*
		if(param.defs!=null){
			for(int i=0;i<param.defs.length;i++){
				String def=param.defs[i];
				set.add("/D\""+def+"\"");
			}
		}
		*/
		String[] ret=(String[] )set.toArray(new String[]{});
		Arrays.sort(ret);
		return ret;
	}
	//=================================================
	public static String[] converJavaParam(JavaParam param,JavaToolsEnv tools_env){
		String[] java_opt=getJavaOpt(param,tools_env);
		ArrayList<String> opt=JmkStringTools.toArrayListString(java_opt);
		return (String[])opt.toArray(new String[]{});
	}
	//=================================================
	//libファイル名作成
	public static void makeClassPathsTmp(JavaParam param){
		HashSet<File> files=new HashSet<File>();
		int num=param.classpaths.length;
		for(int i=0;i<num;i++){
			String lib=param.classpaths[i];
			File file=new File(lib);
			files.add(file);
		}
		param.classpaths_tmp=JmkFileTools.toStringArray(files);
	}
	//=================================================
	static String[] java_add_opt_common={
	};
}
