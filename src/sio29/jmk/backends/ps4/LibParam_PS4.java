package sio29.jmk.backends.ps4;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;


public class LibParam_PS4 extends LibParamBase{
	public LibParam_PS4(ClParamBase cl_param,ClToolsEnvBase tools_env){
		lib_ext="a";
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
		if(ClTargetType_PS4.isX86(lib_param.target_type)){
			JmkStringTools.addArrayList(set,lib_add_opt_x86);
		}else{
			JmkStringTools.addArrayList(set,lib_add_opt_x64);
		}
		if(ClCompileType.isDebug(lib_param.compile_type)){
			JmkStringTools.addArrayList(set,lib_add_opt_debug);
		}else{
			JmkStringTools.addArrayList(set,lib_add_opt_release);
		}
//		for(int i=0;i<lib_param.lib_dirs.length;i++){
//			set.add("-LIBPATH:"+lib_param.lib_dirs[i]);
//		}
		set.add("rcs");
		set.add(lib_param.lib_filename_tmp);
		if(lib_param.obj_files!=null)JmkStringTools.addString(set,lib_param.obj_files);
		if(lib_param.lib_files!=null)JmkStringTools.addString(set,lib_param.lib_files);
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	static String[] lib_add_opt_common={
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
