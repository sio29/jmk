package sio29.jmk.backends.vc;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;

public class LinkParam_VC extends LinkParamBase{
	public LinkParam_VC(ClParamBase cl_param,ClToolsEnvBase tools_env){
		if(ClOutputType.isExe(cl_param.output_type)){
			exe_filename=cl_param.proj_name+".exe";
		}else{
			exe_filename=cl_param.proj_name+".dll";
		}
		setClParamBase(cl_param);
	}
	public String[] converLinkParam(ClToolsEnvBase tools_env){
		return converLinkParam(this,tools_env);
	}
	//=================================================
	private static String[] converLinkParam(LinkParamBase link_param,ClToolsEnvBase tools_env){
		link_param.build_dir_tmp=tools_env.makeBuildDir(link_param.build_dir,link_param.compile_type,link_param.target_type);
		//
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,link_add_opt_common);
		if(ClTargetType_VC.isX86(link_param.target_type)){
			JmkStringTools.addArrayList(set,link_add_opt_x86);
		}else{
			JmkStringTools.addArrayList(set,link_add_opt_x64);
		}
		if(ClCompileType.isDebug(link_param.compile_type)){
			JmkStringTools.addArrayList(set,link_add_opt_debug);
		}else{
			JmkStringTools.addArrayList(set,link_add_opt_release);
		}
		String[] lib_dirs=link_param.lib_dirs;
		for(int i=0;i<lib_dirs.length;i++){
			set.add("/LIBPATH:"+lib_dirs[i]);
		}
		set.add("/OUT:"+link_param.exe_filename_tmp);
		if(link_param.obj_files!=null)JmkStringTools.addString(set,link_param.obj_files);
		if(link_param.lib_files!=null)JmkStringTools.addString(set,link_param.lib_files);
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	// /LTCG (リンク時のコード生成)
	private static String[] link_add_opt_common={
		"/NOLOGO",
	};
	private static String[] link_add_opt_debug={
		"/DEBUG",
	};
	private static String[] link_add_opt_release={
		"/LTCG",
	};
	private static String[] link_add_opt_x86={
	};
	private static String[] link_add_opt_x64={
		"/MACHINE:X64",
	};
}
