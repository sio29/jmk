package sio29.jmk.backends.android;

import sio29.jmk.tools.*;
import sio29.jmk.shellexec.*;

public class AndroidTools{
	private AndroidToolsEnv env_x64;
	private ShellExecBase shellexec;
	//
	public AndroidTools(ShellExecBase _shellexec) throws Exception {
		shellexec=_shellexec;
		
		if(!init()){
			throw new Exception("AndroidTools init error !!");
		}
	}
	public boolean init(){
		try{
			env_x64=new AndroidToolsEnv(shellexec,null);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	public AndroidToolsEnv getEnv(String target_type){
		return env_x64;
	}
};


