package sio29.jmk.backends.ios;

import sio29.jmk.cltools.*;
import sio29.jmk.buildtask.*;

public class JmkiOS{
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "ios";
		}
		public BuildTaskBase createBuildTask(){
			return new ClBuildTask(new ClToolsBuilder_iOS());
		}
	}
}



