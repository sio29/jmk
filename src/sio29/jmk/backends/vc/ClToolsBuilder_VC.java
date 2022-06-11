package sio29.jmk.backends.vc;

import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsBuilder_VC implements ClToolsBuilder{
	public ClToolsBase create(ShellExecBase _shellexec) throws Exception {
		return new ClTools_VC(_shellexec);
	}
}
