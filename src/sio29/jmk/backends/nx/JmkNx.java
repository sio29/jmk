package sio29.jmk.backends.nx;

import sio29.jmk.cltools.*;
import sio29.jmk.buildtask.*;

public class JmkNx{
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "switch";
		}
		public BuildTaskBase createBuildTask(){
			return new ClBuildTask(new ClToolsBuilder_Nx());
		}
	}
}


