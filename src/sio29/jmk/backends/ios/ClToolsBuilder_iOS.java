package sio29.jmk.backends.ios;

import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsBuilder_iOS implements ClToolsBuilder{
	public ClToolsBase create(ShellExecBase _shellexec) throws Exception {
		return new ClTools_iOS(_shellexec);
	}
}
