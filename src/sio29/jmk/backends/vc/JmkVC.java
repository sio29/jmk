package sio29.jmk.backends.vc;

import sio29.jmk.cltools.*;
import sio29.jmk.buildtask.*;

public class JmkVC{
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "win_cpp";
		}
		public BuildTaskBase createBuildTask(){
			return new ClBuildTask(new ClToolsBuilder_VC());
		}
	}
}
