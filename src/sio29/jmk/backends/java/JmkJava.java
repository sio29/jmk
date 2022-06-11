package sio29.jmk.backends.java;

import sio29.jmk.buildtask.*;

public class JmkJava{
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "java";
		}
		public BuildTaskBase createBuildTask(){
			return new JavaBuildTask();
		}
	}
}
