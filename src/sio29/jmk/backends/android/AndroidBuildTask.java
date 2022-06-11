package sio29.jmk.backends.android;

import sio29.jmk.parser.*;
import sio29.jmk.common.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.buildtask.*;

public class AndroidBuildTask implements BuildTaskBase{
	public boolean exec(ShellExecBase _shellexec,JmkBuildParam param,String task_type){
		AndroidTools tools=null;
		try{
			tools=new AndroidTools(_shellexec);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
		return false;
	}
}
