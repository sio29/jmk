package sio29.jmk.backends.gcc;

import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsBuilder_Gcc implements ClToolsBuilder{
	public ClToolsBase create(ShellExecBase _shellexec) throws Exception {
		return new ClTools_Gcc(_shellexec);
	}
}
