package sio29.jmk.cltools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;

public abstract class LinkParamBase{
	//public int compile_type;
	//public int target_type;
	public String compile_type;
	public String target_type;
	//
	public String build_dir;
	public String output_dir;
	public String proj_name;
	public String exe_filename;
	//
	public String[] obj_files;
	public String[] lib_files;
	public String[] lib_dirs;
	//
	public String build_dir_tmp;
	public String exe_filename_tmp;
	
	public void setClParamBase(ClParamBase cl_param){
		setClParamBase(this,cl_param);
	}
	private static void setClParamBase(LinkParamBase link_param,ClParamBase cl_param){
		link_param.compile_type=cl_param.compile_type;
		link_param.target_type =cl_param.target_type;
		
		link_param.build_dir  =cl_param.build_dir;
		link_param.output_dir =cl_param.output_dir;
		link_param.proj_name  =cl_param.proj_name;
		
		link_param.build_dir_tmp   =cl_param.build_dir_tmp;
		link_param.exe_filename_tmp=cl_param.output_dir_tmp+link_param.exe_filename;
		//
		link_param.obj_files=cl_param.obj_files_tmp;
		link_param.lib_files=cl_param.libs_tmp;
		link_param.lib_dirs =cl_param.lib_dirs;
	}
	//======================
	//LinkParamBase
	public boolean execLink(ClToolsEnvBase tools_env,boolean all_flg,boolean use_fileupdate){
		if(use_fileupdate){
/*
System.out.println("lib_dirs_num="+lib_dirs.length);
for(int i=0;i<lib_dirs.length;i++){
System.out.println("lib_dirs["+i+"]:"+lib_dirs[i]);
}
System.out.println("lib_files_num="+lib_files.length);
for(int i=0;i<lib_files.length;i++){
System.out.println("lib_files["+i+"]:"+lib_files[i]);
}
*/
/*
			File[] link_files=JmkFileTools.getLibsWithLibDirsFile(lib_dirs,lib_files);
			//File[] link_files=JmkFileTools.getLinkFilesForExe(obj_files,lib_dirs,lib_files);
System.out.println("link_num="+link_files.length);
for(int i=0;i<link_files.length;i++){
System.out.println("link["+i+"]:"+link_files[i]);
}
*/
			
			boolean r=JmkFileTools.needToUpdateForExe(exe_filename_tmp,obj_files,lib_dirs,lib_files);
System.out.println("needToUpdateForExe:"+r);
			if(!r)return true;
		}
		String[] opt=converLinkParam(tools_env);
		return tools_env.execLink(opt);
	}
	//=====================
	public abstract String[] converLinkParam(ClToolsEnvBase tools_env);
}
