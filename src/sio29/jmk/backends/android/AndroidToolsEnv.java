package sio29.jmk.backends.android;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.parser.*;

public class AndroidToolsEnv extends ClToolsEnvDefault{
	private final static String ANDROID_HOME="ANDROID_HOME";
	private final static String ANDROID_SDK_ROOT="ANDROID_SDK_ROOT";
	private final static String ANDROID_SDK_VER="ANDROID_SDK_VER";

	private final static String clang_filename="emcc.bat";
	private final static String ar_filename="emar.bat";
	private final static String ld_filename="emlink.py";
	
	public AndroidToolsEnv(ShellExecBase _shellexec,String target_type) throws Exception {
		shellexec=_shellexec;
		if(!init(target_type)){
			throw new Exception("AndroidTools.Env init error !!");
		}
	}
	public boolean init(String target_type){
		String android_home_path=shellexec.getEnv(ANDROID_HOME);
		String android_sdk_root_path=shellexec.getEnv(ANDROID_SDK_ROOT);
		String android_sdk_ver=shellexec.getEnv(ANDROID_SDK_VER);
System.out.println("ANDROID_HOME="+android_home_path);
System.out.println("ANDROID_SDK_ROOT="+android_sdk_root_path);
System.out.println("ANDROID_SDK_VER="+android_sdk_ver);
		//パスを得る
		//String path=shellexec.getPaths();
		File[] paths=shellexec.getPathArray();
		//cl.exeのパスを得る
		cl_file=JmkFileTools.findFile(paths,clang_filename);
		lib_file =JmkFileTools.findFile(paths,ar_filename);
		//link_file=JmkFileTools.findFile(paths,ld_filename);
		link_file=cl_file;
System.out.println("cl_file="+cl_file);
System.out.println("lib_file="+lib_file);
System.out.println("link_file="+link_file);
		return true;
	}
	public String makeBuildDir(String build_dir,String compile_type,String target_type){
		String m="";
		if(build_dir!=null){
			m=JmkFileTools.addPath(m,build_dir);
		}
		if(ClCompileType.isDebug(compile_type)){
			m=JmkFileTools.addPath(m,"debug\\");
		}else{
			m=JmkFileTools.addPath(m,"release\\");
		}
		return m;
	}
	public ClParamBase createClParam(JmkBuildParam jmk_param,String target_type){
		return new ClParam_Ndk(jmk_param,target_type,this);
	}
	public boolean execEnv(JmkBuildParam jmk_param,String target_type){
		return execEnv_ClAndLink(jmk_param,target_type);
		//return execEnv_ClOnly(jmk_param,target_type);
	}
}
