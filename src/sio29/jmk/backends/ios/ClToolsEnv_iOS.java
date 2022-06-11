package sio29.jmk.backends.ios;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsEnv_iOS extends ClToolsEnvDefault{
	private final static String clang_cpp_filename="orbis-clang++.exe";
	private final static String ar_filename="orbis-ar.exe";
	private final static String ld_filename="orbis-ld.exe";
	
	public ClToolsEnv_iOS(ShellExecBase _shellexec,String target_type) throws Exception {
		shellexec=_shellexec;
		if(!init(target_type)){
			throw new Exception("ClTools_iOS.Env init error !!");
		}
	}
	private boolean init(String target_type){
		//パスを得る
		File[] paths=shellexec.getPathArray();
		//cl.exeのパスを得る
		cl_file=JmkFileTools.findFile(paths,clang_cpp_filename);
System.out.println("cl_file="+cl_file);
		lib_file =JmkFileTools.findFile(paths,ar_filename);
System.out.println("lib_file="+lib_file);
		link_file=JmkFileTools.findFile(paths,ld_filename);
System.out.println("link_file="+link_file);
		return true;
	}
	public String makeBuildDir(String build_dir,String compile_type,String target_type){
		String m="";
		if(build_dir!=null){
			m=JmkFileTools.addPath(m,build_dir);
		}
		if(ClCompileType.isDebug(compile_type)){
			m=JmkFileTools.addPath(m,"Debug\\");
		}else{
			m=JmkFileTools.addPath(m,"Release\\");
		}
		return m;
	}
	public ClParamBase createClParam(JmkBuildParam jmk_param,String target_type){
		return new ClParam_iOS(jmk_param,target_type,this);
	}
	public boolean execEnv(JmkBuildParam jmk_param,String target_type){
		return execEnv_ClAndLink(jmk_param,target_type);
		//return execEnv_ClOnly(jmk_param,target_type);
	}
	
}


