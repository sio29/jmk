package sio29.jmk.backends.android;

import sio29.jmk.buildtask.*;

public class JmkAndroid{
	public static class Builder implements BuildTaskBuilder{
		public String getTaskName(){
			return "android";
		}
		public BuildTaskBase createBuildTask(){
			return new AndroidBuildTask();
		}
	}
}
