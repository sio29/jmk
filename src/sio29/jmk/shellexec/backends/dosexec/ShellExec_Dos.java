package sio29.jmk.shellexec.backends.dosexec;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.shellexec.*;

public class ShellExec_Dos implements ShellExecBase{
	public static class Builder implements ShellExecBuilder{
		public boolean isSupportOS(String os){
			return os.startsWith("windows");
		}
		public ShellExecBase createShellExec(){
			return new ShellExec_Dos();
		}
	}
	public ShellExecReturn execShell(String[] args){
		return JmkDosExec.execShell(args);
	}
	public ShellExecReturn execShell(String[] args,ShellExecParam param){
		return JmkDosExec.execShell(args,param);
	}
	public boolean execCmd(String cmd,String[] opt){
		return JmkDosExec.execCmd(cmd,opt);
	}
	public boolean execCmd(String cmd,String[] opt,ShellExecParam param){
		return JmkDosExec.execCmd(cmd,opt,param);
	}
	public boolean execCmd(File cmd,String[] opt){
		return JmkDosExec.execCmd(cmd,opt);
	}
	public boolean execCmd(File cmd,String[] opt,ShellExecParam param){
		return JmkDosExec.execCmd(cmd,opt,param);
	}
	public File[] getPathArray(){
		return JmkDosExec.getPathArray();
	}
	public String getPaths(){
		return JmkDosExec.getPaths();
	}
	public String getPathSeparator(){
		return JmkDosExec.getPathSeparator();
	}
	public String getEnv(String env){
		return JmkDosExec.getEnv(env);
	}
}
