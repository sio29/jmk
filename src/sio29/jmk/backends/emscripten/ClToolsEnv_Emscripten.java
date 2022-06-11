package sio29.jmk.backends.emscripten;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsEnv_Emscripten extends ClToolsEnvDefault{
	private final static String EMSDK_ROOT_ENV="EMSCRIPTEN";
	private final static String python_filename="python.exe";
	private final static String clang_filename="emcc.bat";
	private final static String clang_cpp_filename="em++.bat";
	private final static String ar_filename="emar.bat";
	private final static String ld_filename="emlink.py";
	private File python_file;
	//private boolean use_emar=true;
	private boolean use_emar=false;
	
	public ClToolsEnv_Emscripten(ShellExecBase _shellexec,String target_type) throws Exception {
		shellexec=_shellexec;
		if(!init(target_type)){
			throw new Exception("ClTools_Emscripten.Env init error !!");
		}
	}
	private boolean init(String target_type){
		String emsdk_path=shellexec.getEnv(EMSDK_ROOT_ENV);
System.out.println("emsdk_path="+emsdk_path);
		//パスを得る
		File[] paths=shellexec.getPathArray();
		python_file=JmkFileTools.findFile(paths,python_filename);
System.out.println("python_file="+python_file);
		//cl.exeのパスを得る
		cl_file  =JmkFileTools.findFile(paths,clang_filename);
		//lib_file =JmkFileTools.findFile(paths,ar_filename);
		//link_file=JmkFileTools.findFile(paths,ld_filename);
		if(use_emar){
			lib_file=JmkFileTools.findFile(paths,ar_filename);
		}else{
			lib_file=cl_file;
		}
		link_file=cl_file;
System.out.println("cl_file="+cl_file);
System.out.println("lib_file="+lib_file);
System.out.println("link_file="+link_file);
		return true;
	}
	public boolean isLibFileSameClFile(){
		return cl_file.equals(lib_file);
	}
	public boolean isLinkFileSameClFile(){
		return cl_file.equals(link_file);
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
		return new ClParam_Emscripten(jmk_param,target_type,this);
	}
	public boolean execEnv(JmkBuildParam jmk_param,String target_type){
		return execEnv_ClAndLink(jmk_param,target_type);
	}
}


