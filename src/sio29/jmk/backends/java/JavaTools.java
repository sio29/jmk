package sio29.jmk.backends.java;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.shellexec.*;

//=================================================
public class JavaTools{
	//====================
	private JavaToolsEnv env;
	private ShellExecBase shellexec;
	//
	public JavaTools(ShellExecBase _shellexec) throws Exception {
		shellexec=_shellexec;
		if(!init()){
			throw new Exception("JavaTools init error !!");
		}
	}
	private boolean init(){
		try{
			env=new JavaToolsEnv(shellexec,null);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	//=================================================
	public JavaToolsEnv getEnv(String target_type){
		return env;
	}
//	public int getTargetType(String target_name){
//		return JavaTargetType.getTargetType(target_name);
//	}
};


