package sio29.jmk.backends.nx;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClTools_Nx implements ClToolsBase {
	private ClToolsEnvBase env_x64;
	private ShellExecBase shellexec;
	//
	public ClTools_Nx(ShellExecBase _shellexec) throws Exception {
		shellexec=_shellexec;
		if(!init()){
			throw new Exception("ClTools_Nx init error !!");
		}
	}
	private boolean init(){
		try{
			env_x64=new ClToolsEnv_Nx(shellexec,null);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	//====================
	public ClToolsEnvBase getEnv(String target_type){
		return env_x64;
	}
//	public int getTargetType(String target_name){
//		return ClTargetType_Nx.getTargetType(target_name);
//	}
};


