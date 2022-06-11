package sio29.jmk.backends.gcc;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClTools_Gcc implements ClToolsBase {
	private ClToolsEnvBase env_x64;
	private ShellExecBase shellexec;
	//
	public ClTools_Gcc(ShellExecBase _shellexec) throws Exception {
		shellexec=_shellexec;
		if(!init()){
			throw new Exception("GccTools init error !!");
		}
	}
	public boolean init(){
		try{
			env_x64=new ClToolsEnv_Gcc(shellexec,null);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	public ClToolsEnvBase getEnv(String target_type){
		return env_x64;
	}
};


