package sio29.jmk.buildtask;

public interface BuildTaskBuilder{
	public String getTaskName();
	public BuildTaskBase createBuildTask();
}
