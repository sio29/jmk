package sio29.jmk.backends.emscripten;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;


public class LibParam_Emscripten extends LibParamBase{
	public LibParam_Emscripten(ClParamBase cl_param,ClToolsEnvBase tools_env){
		ClToolsEnv_Emscripten _tools_env=(ClToolsEnv_Emscripten)tools_env;
		boolean same_cl_file=_tools_env.isLibFileSameClFile();
		if(same_cl_file){
		//.bc
			lib_ext="bc";
		}else{
		//.a
			lib_ext="a";
		}
		setClParamBase(cl_param);
	}
	public String[] converLibParam(ClToolsEnvBase tools_env){
		return converLibParam(this,tools_env);
	}
	//=================================================
	private static String[] converLibParam(LibParamBase lib_param,ClToolsEnvBase tools_env){
		lib_param.build_dir_tmp=tools_env.makeBuildDir(lib_param.build_dir,lib_param.compile_type,lib_param.target_type);
		//
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,lib_add_opt_common);
		if(ClTargetType_Emscripten.isX86(lib_param.target_type)){
			JmkStringTools.addArrayList(set,lib_add_opt_x86);
		}else{
			JmkStringTools.addArrayList(set,lib_add_opt_x64);
		}
		boolean debug_flg=ClCompileType.isDebug(lib_param.compile_type);
		if(debug_flg){
			JmkStringTools.addArrayList(set,lib_add_opt_debug);
		}else{
			JmkStringTools.addArrayList(set,lib_add_opt_release);
		}
//		for(int i=0;i<lib_param.lib_dirs.length;i++){
//			set.add("-LIBPATH:"+lib_param.lib_dirs[i]);
//		}
		
		ClToolsEnv_Emscripten _tools_env=(ClToolsEnv_Emscripten)tools_env;
		boolean same_cl_file=_tools_env.isLibFileSameClFile();
		if(same_cl_file){
		//.bc
			//set.add("-s");
			//set.add("LINKABLE=1");
		
			set.add("-o");
			set.add(lib_param.lib_filename_tmp);
		}else{
		//.a
//			set.add("rcs");
//			set.add(lib_param.lib_filename_tmp);
			//
//			set.add("--source-map-base");
//			set.add("output");
		}
		
		//
		if(lib_param.obj_files!=null)JmkStringTools.addString(set,lib_param.obj_files);
		if(lib_param.lib_files!=null)JmkStringTools.addString(set,lib_param.lib_files);
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	static String[] lib_add_opt_common={
//		"-s ERROR_ON_UNDEFINED_SYMBOLS=0"
	};
	static String[] lib_add_opt_debug={
	};
	static String[] lib_add_opt_release={
	};
	static String[] lib_add_opt_x86={
	};
	static String[] lib_add_opt_x64={
	};
}
