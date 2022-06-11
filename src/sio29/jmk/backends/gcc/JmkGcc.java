package sio29.jmk.backends.gcc;

import sio29.jmk.cltools.*;
import sio29.jmk.buildtask.*;

public class JmkGcc{
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "gcc";
		}
		public BuildTaskBase createBuildTask(){
			return new ClBuildTask(new ClToolsBuilder_Gcc());
		}
	}
}


