package sio29.jmk.backends.emscripten;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClTools_Emscripten implements ClToolsBase {
	private ClToolsEnvBase env_x64;
	private ShellExecBase shellexec;
	//
	public ClTools_Emscripten(ShellExecBase _shellexec) throws Exception {
		shellexec=_shellexec;
		if(!init()){
			throw new Exception("EmscriptenTools init error !!");
		}
	}
	private boolean init(){
		try{
			env_x64=new ClToolsEnv_Emscripten(shellexec,null);
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
//		return ClTargetType_Emscripten.getTargetType(target_name);
//	}
};


