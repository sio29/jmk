package sio29.jmk.backends.emscripten;

import sio29.jmk.cltools.*;
import sio29.jmk.buildtask.*;

public class JmkEmscripten {
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "emscripten";
		}
		public BuildTaskBase createBuildTask(){
			return new ClBuildTask(new ClToolsBuilder_Emscripten());
		}
	}
}


