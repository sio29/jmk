package sio29.jmk.parser;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.buildtask.*;

import sio29.jmk.shellexec.backends.dosexec.*;
import sio29.jmk.shellexec.backends.bashexec.*;
import sio29.jmk.backends.vc.*;
import sio29.jmk.backends.ps4.*;
import sio29.jmk.backends.nx.*;
import sio29.jmk.backends.emscripten.*;
import sio29.jmk.backends.java.*;
import sio29.jmk.backends.gcc.*;
import sio29.jmk.backends.android.*;
import sio29.jmk.backends.ios.*;
//import sio29.jmk.backends.teavm.*;

public class JmkExecParser{
	public static boolean execParser(String _filename,String _task_type,String _compiler_type,String _compile_type,String _target_type,boolean _skip_prebuild){
		JmkParserPreParam pre_param=new JmkParserPreParam();
		pre_param.compiler_type=_compiler_type;
		pre_param.compile_type =_compile_type;
		pre_param.target_type  =_target_type;
		//パース
		JmkBuildParam param=JmkParser.load(_filename,pre_param);
		if(param==null){
			System.out.println("file not found !!: "+_filename);
			return false;
		}
		//実行の場合はプレビルドはスキップ
		if(BuildTaskType.isRun(_task_type))_skip_prebuild=true;
		//プレビルド
		if(!_skip_prebuild){
			if(!execPreBuild(param,_task_type,pre_param)){
				return false;
			}
		}
		//シェル環境獲得
		String os=getOS();
		ShellExecBase shellexec=getShellExec(os);
		if(shellexec==null){
			System.out.println("unkown os error !!:"+os);
			return false;
		}
		//コンパイラータイプ獲得
		String compiler_type=null;
		{
			compiler_type=param.getCompilerType();
			if(compiler_type==null){
				System.out.println("compiler_type is null !!");
				return false;
			}
		}
		//タスク作成
		BuildTaskBase buildtask=getBuildTask(compiler_type);
		if(buildtask==null){
			System.out.println("unkown compiler_type error !!:"+compiler_type);
			return false;
		}
		//タスク実行
//System.out.println("compiler_type:"+compiler_type);
		boolean r=buildtask.exec(shellexec,param,_task_type);
		if(!r){
			System.out.println("build error !!: "+_filename);
			return false;
		}
//System.out.println("build end: "+filename);
		return r;
	}
	//======================================
	//プレビルド
	static boolean execPreBuild(JmkBuildParam param,String _task_type,JmkParserPreParam pre_param){
		String[] pre_builds=param.getPreBuilds();
		if(pre_builds==null)return true;
		for(int i=0;i<pre_builds.length;i++){
			String filename=pre_builds[i];
			File file=new File(filename);
			if(file.isDirectory()){
				file=new File(file,"build.jmk");
			}
			if(!file.exists())continue;
			String new_filename=file.toString();
//System.out.println("prebuild: "+new_filename);
			if(!execParser(new_filename,_task_type,pre_param.compiler_type,pre_param.compile_type,pre_param.target_type,false)){
				System.out.println("error !!: "+new_filename);
				return false;
			}
		}
		return true;
	}
	
	
	//windows
	//freebsd
	static String getOS(){
		return System.getProperty("os.name").toLowerCase();
	}
	static void printProperty(){
		Properties prop=System.getProperties();
		for(String key : prop.stringPropertyNames()){
			 String v=prop.getProperty(key);
			 System.out.println(key+" = "+v);
		}
		
	}
	//Builder
	static BuildTaskBuilder[] g_buildtask_builders={
		new JmkVC.Builder(),
		new JmkPS4.Builder(),
		new JmkNx.Builder(),
		new JmkEmscripten.Builder(),
		new JmkJava.Builder(),
		new JmkGcc.Builder(),
		new JmkAndroid.Builder(),
		new JmkiOS.Builder(),
//		new JmkTeaVM.Builder(),
	};
	//
	static HashMap<String,BuildTaskBase> g_buildtask_map=new HashMap<String,BuildTaskBase>();
	//
	static BuildTaskBuilder getBuildTaskBuilder(String compiler_type){
		for(int i=0;i<g_buildtask_builders.length;i++){
			BuildTaskBuilder builder=g_buildtask_builders[i];
			if(builder==null)continue;
			String name=builder.getTaskName();
			if(compiler_type.equals(name))return builder;
		}
		return null;
	}
	static BuildTaskBase getBuildTask(String compiler_type){
		BuildTaskBase buildtask=g_buildtask_map.get(compiler_type);
		if(buildtask==null){
			BuildTaskBuilder builder=getBuildTaskBuilder(compiler_type);
			if(builder!=null){
				buildtask=builder.createBuildTask();
				g_buildtask_map.put(compiler_type,buildtask);
			}
		}
		return buildtask;
	}
	static ShellExecBuilder[] shellexec_builders={
		new ShellExec_Dos.Builder(),
		new ShellExec_Bash.Builder(),
	};
	//
	static HashMap<String,ShellExecBase> g_shellexec_map=new HashMap<String,ShellExecBase>();
	
	static ShellExecBuilder getShellExecBuilder(String os){
		for(int i=0;i<shellexec_builders.length;i++){
			ShellExecBuilder builder=shellexec_builders[i];
			if(builder==null)continue;
			if(builder.isSupportOS(os))return builder;
		}
		return null;
	}
	static ShellExecBase getShellExec(String os){
		ShellExecBase shellexec=g_shellexec_map.get(os);
		if(shellexec==null){
			ShellExecBuilder builder=getShellExecBuilder(os);
			if(builder!=null){
				shellexec=builder.createShellExec();
				g_shellexec_map.put(os,shellexec);
			}
		}
		return shellexec;
	}
}
