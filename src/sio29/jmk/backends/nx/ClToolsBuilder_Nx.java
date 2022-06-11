package sio29.jmk.backends.nx;

import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsBuilder_Nx implements ClToolsBuilder{
	public ClToolsBase create(ShellExecBase _shellexec) throws Exception {
		return new ClTools_Nx(_shellexec);
	}
}
