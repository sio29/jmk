package sio29.jmk.shellexec.backends.bashexec;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.shellexec.*;

public class ShellExec_Bash implements ShellExecBase{
	public static class Builder implements ShellExecBuilder{
		public boolean isSupportOS(String os){
			if(os.startsWith("freebsd"))return true;
			if(os.startsWith("linux"))return true;
			if(os.startsWith("unix"))return true;
			return false;
		}
		public ShellExecBase createShellExec(){
			return new ShellExec_Bash();
		}
	}
	public ShellExecReturn execShell(String[] args){
		return JmkBashExec.execShell(args);
	}
	public ShellExecReturn execShell(String[] args,ShellExecParam param){
		return JmkBashExec.execShell(args,param);
	}
	public boolean execCmd(String cmd,String[] opt){
		return JmkBashExec.execCmd(cmd,opt);
	}
	public boolean execCmd(String cmd,String[] opt,ShellExecParam param){
		return JmkBashExec.execCmd(cmd,opt,param);
	}
	public boolean execCmd(File cmd,String[] opt){
		return JmkBashExec.execCmd(cmd,opt);
	}
	public boolean execCmd(File cmd,String[] opt,ShellExecParam param){
		return JmkBashExec.execCmd(cmd,opt,param);
	}
	public File[] getPathArray(){
		return JmkBashExec.getPathArray();
	}
	public String getPaths(){
		return JmkBashExec.getPaths();
	}
	public String getPathSeparator(){
		return JmkBashExec.getPathSeparator();
	}
	public String getEnv(String env){
		return JmkBashExec.getEnv(env);
	}
}
