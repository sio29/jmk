package sio29.jmk.backends.vc;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;

public class LibParam_VC extends LibParamBase{
	public LibParam_VC(ClParamBase cl_param,ClToolsEnvBase tools_env){
		lib_ext="lib";
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
		if(ClTargetType_VC.isX86(lib_param.target_type)){
			JmkStringTools.addArrayList(set,lib_add_opt_x86);
		}else{
			JmkStringTools.addArrayList(set,lib_add_opt_x64);
		}
		if(ClCompileType.isDebug(lib_param.compile_type)){
			JmkStringTools.addArrayList(set,lib_add_opt_debug);
		}else{
			JmkStringTools.addArrayList(set,lib_add_opt_release);
		}
		String[] lib_dirs=lib_param.lib_dirs;
		for(int i=0;i<lib_dirs.length;i++){
			set.add("/LIBPATH:"+lib_dirs[i]);
		}
		set.add("/OUT:"+lib_param.lib_filename_tmp);
		
		if(lib_param.obj_files!=null)JmkStringTools.addString(set,lib_param.obj_files);
		if(lib_param.lib_files!=null)JmkStringTools.addString(set,lib_param.lib_files);
		
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	private static String[] lib_add_opt_common={
		"/NOLOGO",
	};
	private static String[] lib_add_opt_debug={
	};
	private static String[] lib_add_opt_release={
		"/LTCG",			//リンク時のコード生成
	};
	private static String[] lib_add_opt_x86={
	};
	private static String[] lib_add_opt_x64={
		"/MACHINE:X64",		//ターゲット
	};
}
