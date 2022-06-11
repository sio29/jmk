package sio29.jmk.cltools;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public abstract class ClToolsEnvDefault implements ClToolsEnvBase {
	public Map<String,String> new_env;
	public File cl_file;
	public File lib_file;
	public File link_file;
	public ShellExecBase shellexec;
	public boolean print_flg=true;
	//
	//public boolean execCl(String[] opt){
	public boolean execCl(String[] opt,String src_filename){
		System.out.println("exec: cl.exe : "+src_filename);
		ShellExecParam execparam=new ShellExecParam();
		execparam.in_env=new_env;
		execparam.print_flg=print_flg;
		return shellexec.execCmd(cl_file.toString(),opt,execparam);
	}
	public boolean execLib(String[] opt){
		System.out.println("exec: lib.exe");
		ShellExecParam execparam=new ShellExecParam();
		execparam.in_env=new_env;
		execparam.print_flg=print_flg;
		return shellexec.execCmd(lib_file.toString(),opt,execparam);
	}
	public boolean execLink(String[] opt){
		System.out.println("exec: link.exe");
		ShellExecParam execparam=new ShellExecParam();
		execparam.in_env=new_env;
		execparam.print_flg=print_flg;
		return shellexec.execCmd(link_file.toString(),opt,execparam);
	}
	//public boolean execEnv_ClOnly(JmkBuildParam jmk_param,int target_type){
	public boolean execEnv_ClOnly(JmkBuildParam jmk_param,String target_type){
		ClParamBase cl_param=createClParam(jmk_param,target_type);
		//int output_type=cl_param.getOutputType();
		String output_type=cl_param.getOutputType();
		boolean all_flg=true;
		boolean use_fileupdate=false;
		//cl
		if(!cl_param.execCl(this,all_flg,use_fileupdate))return false;
		
		return true;
	}
	//public boolean execEnv_ClAndLink(JmkBuildParam jmk_param,int target_type){
	public boolean execEnv_ClAndLink(JmkBuildParam jmk_param,String target_type){
		ClParamBase cl_param=createClParam(jmk_param,target_type);
		//int output_type=cl_param.getOutputType();
		String output_type=cl_param.getOutputType();
		
		boolean all_flg=false;
		boolean use_fileupdate=true;
		
		//cl
		if(!cl_param.execCl(this,all_flg,use_fileupdate))return false;
		//lib
		if(ClOutputType.isLib(output_type)){
			LibParamBase lib_param=cl_param.createLibParam(this);
			return lib_param.execLib(this,all_flg,use_fileupdate);
		}
		//link
		if(ClOutputType.isExe(output_type) || ClOutputType.isDll(output_type)){
			LinkParamBase link_param=cl_param.createLinkParam(this);
			return link_param.execLink(this,all_flg,use_fileupdate);
		}
		return true;
	}
	
}

