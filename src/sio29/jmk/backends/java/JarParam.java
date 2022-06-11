package sio29.jmk.backends.java;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;


public class JarParam{
	public String base_path_tmp;
	public String manifest_filename;
	public String main_class;
	public String unjar_dir;
	public String manifest_dir;
	//
	public String build_dir_tmp;
	public String output_dir_tmp;
	public String[] libs_tmp;
	public String jar_filename_tmp;
	public String manifest_filename_tmp;
	public String unjar_dir_tmp;
	public String manifest_dir_tmp;
	
	public JarParam(JavacParam param){
		JavacParamToJarParam(this,param);
	}
	//=================================================
	public static JarParam JavacParamToJarParam(JavacParam param){
		//JarParam lib_param=new JarParam();
		//JavacParamToJarParam(JavacParam param){
		//return lib_param;
		return new JarParam(param);
	}
	private static void JavacParamToJarParam(JarParam lib_param,JavacParam param){
		lib_param.base_path_tmp    =param.base_path_tmp;
		lib_param.manifest_filename="MANIFEST.MF";
		lib_param.unjar_dir        ="__unjar_tmp__";
		lib_param.manifest_dir     ="__manifest_tmp__";
		//
		lib_param.main_class      =param.main_class;
		lib_param.build_dir_tmp   =param.build_dir_tmp;
		lib_param.output_dir_tmp  =param.output_dir_tmp;
		lib_param.libs_tmp        =param.libs_tmp;
		lib_param.jar_filename_tmp=param.jar_filename_tmp;
	}
	//=================================================
	public boolean execJar(JavaToolsEnv tools_env){
		return execJar(tools_env,null);
	}
	public boolean execJar(JavaToolsEnv tools_env,File current_dir){
		String[] opt=JarParam.converJarParam(this,tools_env);
		return tools_env.execJar(opt,current_dir);
	}
	//=================================================
	public static String[] converJarParam(JarParam param,JavaToolsEnv tools_env){
//		String[] jar_opt=getJarOpt(param,tools_env);
//		ArrayList<String> opt=JmkStringTools.toArrayListString(jar_opt);
//		//if(param.obj_files!=null)JmkStringTools.addString(opt,param.obj_files);
//		//if(param.lib_files!=null)JmkStringTools.addString(opt,param.lib_files);
//		return (String[])opt.toArray(new String[]{});
//	}
//	//=================================================
//	public static String[] getJarOpt(JarParam param,JavaToolsEnv tools_env){
		param.unjar_dir_tmp=tools_env.makeBuildDir(param.unjar_dir);
		param.manifest_dir_tmp=tools_env.makeBuildDir(param.manifest_dir);
		File unjar_dir_file=new File(param.unjar_dir_tmp);
		File manifest_dir_file=new File(param.manifest_dir_tmp);
		param.manifest_filename_tmp=JmkFileTools.makePathName(manifest_dir_file,param.manifest_filename);
		//======================
		//ディレクトリ作成
		JmkFileTools.createDir(param.output_dir_tmp);
		JmkFileTools.createDir(param.unjar_dir_tmp);
		JmkFileTools.createDir(param.manifest_dir_tmp);
		//======================
		//マニフェストの作成
		createManifestFile(param.manifest_filename_tmp,param.main_class);
		//======================
		//unjar
		unjarLibs(param.unjar_dir_tmp,param.libs_tmp,tools_env);
		//======================
		//コピー
		copyBuilderDir(param.build_dir_tmp,param.unjar_dir_tmp);
		//======================
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,jar_pack_opt_common);
		set.add(param.jar_filename_tmp);
		set.add(param.manifest_filename_tmp);
		set.add("-C");
		set.add(param.unjar_dir_tmp);
		set.add(".");
		//
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	public static boolean postExec(JarParam param,JavaToolsEnv tools_env){
		JmkFileTools.deleteDir(param.unjar_dir_tmp);
		JmkFileTools.deleteDir(param.manifest_dir_tmp);
		return true;
	}
	//=================================================
	//マニフェストファイルの作成
	static boolean createManifestFile(String filename,String main_class){
		try{
			FileOutputStream os=new FileOutputStream(filename);
			String m=String.format("Main-Class: "+main_class+"\n");
			os.write(m.getBytes());
			os.close();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	//=================================================
	//unjar
	public static boolean unjarLibs(String unjar_dir,String[] libs_tmp,JavaToolsEnv tools_env){
		File unjar_dir_file=new File(unjar_dir);
		for(int i=0;i<libs_tmp.length;i++){
			String lib=libs_tmp[i];
			ArrayList<String> opt=new ArrayList<String>();
			opt.add("-xf");
			opt.add(lib);
			tools_env.execJar((String[])opt.toArray(new String[]{}),unjar_dir_file);
		}
		//META-INFの削除
		File metainf_file=new File(unjar_dir_file,"META-INF");
		JmkFileTools.deleteDir(metainf_file.toString());
		return true;
	}
	//=================================================
	//unjar
	public static boolean copyBuilderDir(String src_dir,String dst_dir){
		JmkFileTools.copyDir(src_dir,dst_dir);
		return true;
	}
	//=================================================
	static String[] jar_pack_opt_common={
		"-cfm"
	};
	static String[] jar_unpack_opt_common={
		"-xf"
	};
}
