package sio29.jmk.backends.emscripten;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;

//import sio29.ulib.ufile.*;

public class PreJsMaker_Emscripten{
	public final static String pre_js_head="Module['preRun'] = function () {";
	public final static String pre_js_end ="};";
	public final static String create_folder_head="FS.createFolder(";
	public final static String create_folder_end =");";
	public final static String create_preload_head="FS.createPreloadedFile(";
	public final static String create_preload_end =");";
	
	static class PreJsParam{
//		UFile output_filename;
//		UFile dir;
//		UFile[] files;
		File output_filename;
		File dir;
		File[] files;
	}
	
	
}
