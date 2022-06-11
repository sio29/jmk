package sio29.jmk.cltools;

import java.io.*;

import sio29.jmk.cltools.*;
import sio29.jmk.parser.*;
import sio29.jmk.common.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.buildtask.*;
import sio29.jmk.tools.*;

public class ClBuildTask implements BuildTaskBase{
	public ClToolsBuilder builder;
	ClToolsBase g_tools=null;
	
	public ClBuildTask(ClToolsBuilder _builder){
		builder=_builder;
	}
	ClToolsBase geTools(ShellExecBase _shellexec){
		if(g_tools==null){
			try{
System.out.println("create ClToolsBase ");
				g_tools=builder.create(_shellexec);
			}catch(Exception ex){
				System.out.println("ClToolsBase init error !!");
			}
		}
		return g_tools;
	}
	
	public boolean exec(ShellExecBase _shellexec,JmkBuildParam jmk_param,String task_type){
		if(BuildTaskType.isBuild(task_type)){
			return execBuild(_shellexec,jmk_param);
		}else if(BuildTaskType.isClean(task_type)){
			return execClean(_shellexec,jmk_param);
		}else if(BuildTaskType.isRun(task_type)){
			return execRun(_shellexec,jmk_param);
		}
		return false;
	}
	public boolean execBuild(ShellExecBase _shellexec,JmkBuildParam jmk_param){
		/*
		ClToolsBase tools=null;
		try{
			tools=builder.create(_shellexec);
		}catch(Exception ex){
			System.out.println("ClToolsBase init error !!");
			return false;
		}
		*/
		ClToolsBase tools=geTools(_shellexec);
		if(tools==null)return false;
		
		//ターゲット分ループ
		String[] targets=jmk_param.getTargets();
		int target_num=targets.length;
		for(int target_index=0;target_index<target_num;target_index++){
			String target_type=targets[target_index];
			//String target_type_id=tools.getTargetType(target_name);
			ClToolsEnvBase env=tools.getEnv(target_type);
			if(env==null){
				System.out.println("env target_type["+target_type+"] not found !!");
				return false;
			}
			if(!env.execEnv(jmk_param,target_type)){
				System.out.println("env.execEnv error !!");
				return false;
			}
			//
			boolean update_flg=true;
			if(!CommonTools.execCopy(jmk_param,update_flg)){
				System.out.println("CommonTools.execCopy error !!");
				return false;
			}
		}
		return true;
	}
	public boolean execClean(ShellExecBase _shellexec,JmkBuildParam jmk_param){
		ClToolsBase tools=geTools(_shellexec);
		if(tools==null)return false;
		
		File base_path=JmkFileTools.getBasePath(jmk_param.getJmkFilename());
		String build_dir     =JmkFileTools.makePathName(base_path,jmk_param.getBuildDir());
		String output_dir    =JmkFileTools.makePathName(base_path,jmk_param.getOutputDir());
		String target_type=jmk_param.getTargetType();
		System.out.println("clean:"+build_dir);
		JmkFileTools.deleteDir(build_dir);
		System.out.println("clean:"+output_dir);
		JmkFileTools.deleteDir(output_dir);
		return true;
	}
	public boolean execRun(ShellExecBase _shellexec,JmkBuildParam jmk_param){
		String output_type=jmk_param.getOutputType();
		if(!output_type.equals("exe"))return false;
		
		ClToolsBase tools=geTools(_shellexec);
		if(tools==null)return false;
		
		
		String output_dir =jmk_param.getOutputDir();
		String target_type=jmk_param.getTargetType();
		String exe_filename=jmk_param.getProjName()+".exe";	//※
		String compiler_type=jmk_param.getCompilerType().toLowerCase();
		if(compiler_type.equals("win_cpp")){
			exe_filename=jmk_param.getProjName()+".exe";	//※
		}else if(compiler_type.equals("gcc")){
			exe_filename=jmk_param.getProjName();
		}
		String compile_type=jmk_param.getCompileType();
		
		System.out.println("output_type:"+output_type);
		System.out.println("target_type:"+target_type);
		System.out.println("compile_type:"+compile_type);
		//System.out.println("debug_flg:"+debug_flg);
		
		
		//String target_type_id=tools.getTargetType(target_type);
		ClToolsEnvBase tools_env=tools.getEnv(target_type);
		String output_dir_tmp=tools_env.makeBuildDir(output_dir,compile_type,target_type);
		System.out.println("output_dir_tmp:"+output_dir_tmp);
		
		String exe_filename_tmp=output_dir_tmp+exe_filename;
		
		System.out.println("exe_filename_tmp:"+exe_filename_tmp);
		
		
		String[] cmd={exe_filename_tmp};
		ShellExecReturn retdata=_shellexec.execShell(cmd);
		
		
		
		
		System.out.println("run");
		return true;
	}
}
