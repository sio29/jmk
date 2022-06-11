package sio29.jmk.buildtask;

import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;

public interface BuildTaskBase{
	public boolean exec(ShellExecBase _shellexec,JmkBuildParam param,String task_type);
}
