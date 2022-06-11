package sio29.jmk.backends.ps4;

import sio29.jmk.cltools.*;
import sio29.jmk.buildtask.*;

public class JmkPS4{
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "ps4";
		}
		public BuildTaskBase createBuildTask(){
			return new ClBuildTask(new ClToolsBuilder_PS4());
		}
	}
}


