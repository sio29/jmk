package sio29.jmk.shellexec;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public interface ShellExecBuilder{
	public boolean isSupportOS(String os);
	public ShellExecBase createShellExec();
}
