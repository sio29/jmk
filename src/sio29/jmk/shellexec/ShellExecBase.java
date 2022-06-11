package sio29.jmk.shellexec;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public interface ShellExecBase{
	public ShellExecReturn execShell(String[] args);
	public ShellExecReturn execShell(String[] args,ShellExecParam param);
	public boolean execCmd(String cmd,String[] opt);
	public boolean execCmd(String cmd,String[] opt,ShellExecParam param);
	public boolean execCmd(File cmd,String[] opt);
	public boolean execCmd(File cmd,String[] opt,ShellExecParam param);
	public String getPaths();
	public File[] getPathArray();
	public String getPathSeparator();
	public String getEnv(String env);
	
}
