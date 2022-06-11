package sio29.jmk.shellexec;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public class ShellExecReturn{
	public Map<String,String> out_env;
	public String text;
	public int return_code;				//0:正常終了、0以外:エラー
}
