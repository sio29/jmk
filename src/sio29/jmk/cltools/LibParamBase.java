package sio29.jmk.cltools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;

public abstract class LibParamBase{
	public String lib_ext="lib";
	//
	//public int compile_type;
	//public int target_type;
	public String compile_type;
	public String target_type;
	//
	public String build_dir;
	public String output_dir;
	public String proj_name;
	public String lib_filename;
	public String[] obj_files;
	public String[] lib_files;
	public String[] lib_dirs;
	//
	public String build_dir_tmp;
	public String lib_filename_tmp;
	
	public void setClParamBase(ClParamBase cl_param){
		setClParamBase(this,cl_param);
	}
	private static void setClParamBase(LibParamBase lib_param,ClParamBase cl_param){
		lib_param.build_dir  =cl_param.build_dir;
		
		lib_param.compile_type=cl_param.compile_type;
		lib_param.target_type =cl_param.target_type;
		
		lib_param.output_dir =cl_param.output_dir;
		lib_param.proj_name  =cl_param.proj_name;
		lib_param.lib_filename=cl_param.proj_name+"."+lib_param.lib_ext;
		//
		lib_param.build_dir_tmp=cl_param.build_dir_tmp;
		lib_param.lib_filename_tmp=cl_param.output_dir_tmp+lib_param.lib_filename;
		//
		lib_param.obj_files=cl_param.obj_files_tmp;
		lib_param.lib_files=cl_param.libs_tmp;
		lib_param.lib_dirs =cl_param.lib_dirs;
		
	}
	//======================
	//LibParamBase
	public boolean execLib(ClToolsEnvBase tools_env,boolean all_flg,boolean use_fileupdate){
		if(use_fileupdate){
			boolean r=JmkFileTools.needToUpdateForExe(lib_filename_tmp,obj_files,lib_dirs,lib_files);
			if(!r)return true;
		}
		String[] opt=converLibParam(tools_env);
		return tools_env.execLib(opt);
	}
	//=====================
	public abstract String[] converLibParam(ClToolsEnvBase tools_env);
	
}
