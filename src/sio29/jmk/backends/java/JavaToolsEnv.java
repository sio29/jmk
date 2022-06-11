package sio29.jmk.backends.java;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.buildtask.*;

public class JavaToolsEnv{
	public File java_file;
	public File javac_file;
	public File jar_file;
	public ShellExecBase shellexec;
	private boolean print_flg=true;
	
	public JavaToolsEnv(ShellExecBase _shellexec,String target_type) throws Exception {
		shellexec=_shellexec;
		if(!init(target_type)){
			throw new Exception("JavaTools.Env init error !!");
		}
	}
	public boolean init(String target_type){
		//パスを得る
		File[] paths=shellexec.getPathArray();
		//cl.exeのパスを得る
		java_file =JmkFileTools.findFile(paths,"java.exe");
		javac_file=JmkFileTools.findFile(paths,"javac.exe");
		jar_file  =JmkFileTools.findFile(paths,"jar.exe");
		return true;
	}
	public boolean execEnv(JmkBuildParam jmk_param,String target_type){
		JavacParam javacparam=new JavacParam(jmk_param);
		if(!javacparam.execJavac(this))return false;
		//
		if(JavaOutputType.isJar(javacparam.output_type)){
			JarParam jar_param=new JarParam(javacparam);
			if(!jar_param.execJar(this))return false;
			//JarParam.postExec(jar_param,this);
			return true;
		}
		return true;
	}
	public boolean execJavac(String[] opt){
		System.out.println("exec: javac");
		ShellExecParam param=new ShellExecParam();
		param.print_flg=print_flg;
		return shellexec.execCmd(javac_file.toString(),opt,param);
	}
	public boolean execJar(String[] opt,File current_dir){
		ShellExecParam param=new ShellExecParam();
		param.current_dir=current_dir;
		param.print_flg=false;//print_flg;
		return shellexec.execCmd(jar_file.toString(),opt,param);
	}
	public boolean execJava(String[] opt){
		System.out.println("exec: java");
		ShellExecParam param=new ShellExecParam();
		param.print_flg=print_flg;
		return shellexec.execCmd(java_file.toString(),opt,param);
	}
	//=================================================
	public static String makeBuildDir(String build_dir){
		String m="";
		if(build_dir!=null){
			m=JmkFileTools.addPath(m,build_dir);
		}
		return m;
	}
}

