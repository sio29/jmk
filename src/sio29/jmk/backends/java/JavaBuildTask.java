package sio29.jmk.backends.java;

import sio29.jmk.parser.*;
import sio29.jmk.common.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.buildtask.*;

public class JavaBuildTask implements BuildTaskBase{
	public boolean exec(ShellExecBase _shellexec,JmkBuildParam jmk_param,String task_type){
//System.out.println("execJava:");
		JavaTools tools=null;
		try{
			tools=new JavaTools(_shellexec);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		String target_type=null;
		JavaToolsEnv env=tools.getEnv(target_type);
		if(env.execEnv(jmk_param,target_type))return false;
		//
		boolean update_flg=true;
		if(!CommonTools.execCopy(jmk_param,update_flg))return false;
		return true;
	}
}
