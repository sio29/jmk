package sio29.jmk.backends.java;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;

public class JavacParam{
	public final static String[] source_exts={"java"};
	//public int output_type;
	public String output_type;
	public String build_dir;
	public String output_dir;
	public String proj_name;
	//public String source_dir;
//	public String[] source_dir;
//	public String[] ignore_source_dir;
//	public String[] source_files;
//	public String[] ignore_source_files;
	public JmkFileSearchParam[] source_params;
	public String[] lib_dirs;
	public String[] libs;
	public String[] classpaths;
	public String[] defs;
	public String jar_filename;
	public String main_class;
	//
	public String build_dir_tmp;
	public String output_dir_tmp;
	public String[] source_files_tmp;
	public String[] classpaths_tmp;
	public String[] libs_tmp;
	public String jar_filename_tmp;
	public String base_path_tmp;
	
	public JavacParam(JmkBuildParam param){
		convertJavacParam(this,param);
	}
	//=================================================
	public static JavacParam convertJavacParam(JmkBuildParam param){
		return new JavacParam(param);
	}
	private static void convertJavacParam(JavacParam javacparam,JmkBuildParam jmk_param){
		File base_path=JmkFileTools.getBasePath(jmk_param.getJmkFilename());
		javacparam.base_path_tmp=base_path.toString();
		javacparam.proj_name   =jmk_param.getProjName();
		//
		//javacparam.source_dir         =JmkFileTools.makePathNames(base_path,jmk_param.getSourceDirs());
		//javacparam.ignore_source_dir  =JmkFileTools.makePathNames(base_path,jmk_param.getIgnoreSourceDirs());
		//javacparam.source_files       =JmkFileTools.makePathNames(base_path,jmk_param.getSourceFiles());
		//javacparam.ignore_source_files=JmkFileTools.makePathNames(base_path,jmk_param.getIgnoreSourceFiles());
		/*
		javacparam.source_param=new JmkFileSearchParam();;
		javacparam.source_param.dirs        =JmkFileTools.makePathNames(base_path,jmk_param.getSourceDirs());
		javacparam.source_param.ignore_dirs =JmkFileTools.makePathNames(base_path,jmk_param.getIgnoreSourceDirs());
		javacparam.source_param.files       =JmkFileTools.makePathNames(base_path,jmk_param.getSourceFiles());
		javacparam.source_param.ignore_files=JmkFileTools.makePathNames(base_path,jmk_param.getIgnoreSourceFiles());
		javacparam.source_param.exts        =source_exts;
		*/
		//javacparam.source_files=jmk_param.getSourceFiles();
		javacparam.source_params=jmk_param.getSourceParams(base_path,source_exts);
		//
		javacparam.build_dir   =JmkFileTools.makePathName(base_path,jmk_param.getBuildDir());
		javacparam.output_dir  =JmkFileTools.makePathName(base_path,jmk_param.getOutputDir());
		javacparam.output_type =jmk_param.getOutputType();
		javacparam.lib_dirs    =jmk_param.getLibDirs();
		javacparam.libs        =jmk_param.getLibs();
		javacparam.defs        =jmk_param.getDefs();
		javacparam.main_class  =jmk_param.getMainClass();
		javacparam.jar_filename=JmkFileTools.changeFileExt(jmk_param.getProjName(),"jar");
//System.out.println("jar_filename:"+javacparam.jar_filename);
	}
	//=================================================
	public boolean execJavac(JavaToolsEnv tools_env){
		String[] opt=JavacParam.converJavacParam(this,tools_env);
		return tools_env.execJavac(opt);
	}
	//=================================================
	//cl.exeのオプション
	public static String[] getJavacOpt(final JavacParam param,JavaToolsEnv tools_env){
		//buildディレクトリ名作成
		param.build_dir_tmp=tools_env.makeBuildDir(param.build_dir);
		//出力ディレクトリ名作成
		if(param.output_dir!=null){
			param.output_dir_tmp=tools_env.makeBuildDir(param.output_dir);
			param.jar_filename_tmp=JmkFileTools.makePathName(new File(param.output_dir_tmp),param.jar_filename);
		}
		//jarファイル名作成
		makeLibsTmp(param);
		//sourceファイル名作成
		makeSourceFilesTmp(param);
		//libファイル名作成
		makeClassPathsTmp(param);
		//======================
		//ディレクトリ作成
		JmkFileTools.createDir(param.build_dir_tmp);
		JmkFileTools.createDir(param.output_dir_tmp);
		//======================
		//Cmd.exeのパラメータ作成
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,cl_add_opt_common);
		//build_dir
		if(param.build_dir_tmp!=null){
			set.add("-d");
			set.add(param.build_dir_tmp);
		}
		//sourcepath
		String[] source_dirs=param.source_params[0].dirs;
		if(source_dirs!=null){
			set.add("-sourcepath");
			//set.add(param.source_dir);
			String m="";
			for(int i=0;i<source_dirs.length;i++){
				if(i!=0){
					m+=";";
				}
				m+=source_dirs[i];
			}
			set.add(m);
		}
		//classpath
		if(param.classpaths_tmp!=null){
			String m="";
			for(int i=0;i<param.classpaths_tmp.length;i++){
				String f=param.classpaths_tmp[i];
				m+=f+";";
			}
			set.add("-cp");
			set.add(m);
		}
		//define
		if(param.defs!=null){
			for(int i=0;i<param.defs.length;i++){
				String def=param.defs[i];
				set.add("/D\""+def+"\"");
			}
		}
		//source file
		if(param.source_files_tmp!=null){
			for(int i=0;i<param.source_files_tmp.length;i++){
				String f=param.source_files_tmp[i];
				set.add(f);
			}
		}
		//
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	//cl.exeのオプション
	public static String[] converJavacParam(JavacParam param,JavaToolsEnv tools_env){
		String[] cl_opt=getJavacOpt(param,tools_env);
		ArrayList<String> opt=JmkStringTools.toArrayListString(cl_opt);
		JmkStringTools.addString(opt,param.source_files_tmp);
		return (String[])opt.toArray(new String[]{});
	}
	//=================================================
	//ソースファイル名作成
	public static void makeSourceFilesTmp(JavacParam param){
		//String[] exts=new String[]{"java"};
		//param.source_files_tmp=JmkFileTools.getSourceFiles(param.source_dir,param.ignore_source_dir,param.source_files,param.ignore_source_files,exts);
		param.source_files_tmp=JmkFileTools.getSourceFiles(param.source_params);
	}
	//=================================================
	//jarファイル名作成
	public static void makeLibsTmp(JavacParam param){
		param.libs_tmp=JmkFileTools.getLibsWithLibDirs(param.lib_dirs,param.libs);
	}
	//=================================================
	//libファイル名作成
	public static void makeClassPathsTmp(JavacParam param){
		HashSet<File> files=new HashSet<File>();
		if(param.classpaths!=null){
			int num=param.classpaths.length;
			for(int i=0;i<num;i++){
				String lib=param.classpaths[i];
				File f=new File(lib);
				if(f.exists()){
					files.add(f);
				}
			}
		}
		if(param.libs_tmp!=null){
			int num=param.libs_tmp.length;
			for(int i=0;i<num;i++){
				String lib=param.libs_tmp[i];
				files.add(new File(lib));
			}
		}
		param.classpaths_tmp=JmkFileTools.toStringArray(files);
	}
	//=================================================
	static String[] cl_add_opt_common={
		"-Xlint:unchecked",
	};
}
