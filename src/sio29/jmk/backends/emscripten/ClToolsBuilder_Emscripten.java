package sio29.jmk.backends.emscripten;

import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsBuilder_Emscripten implements ClToolsBuilder{
	public ClToolsBase create(ShellExecBase _shellexec) throws Exception {
		return new ClTools_Emscripten(_shellexec);
	}
}
