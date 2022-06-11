package sio29.jmk;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

//import sio29.ulib.argreader.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;
import sio29.jmk.parser.*;
import sio29.jmk.buildtask.*;

public class jmk{
	public static void main(String[] args){
		printTitle();
		ArgParam param=new ArgParam();
		if(!readArgs(param,args)){
			System.exit(1);
		}
		execParam(param);
		System.exit(0);
	}
	static void printTitle(){
		System.out.println("jmk - build tools - by sio29");
	}
	static void printHelp(){
		System.out.println("usage) java -jar jmk.jar {/,-option} filename");
		System.out.println(" -h,-help,-? help");
		System.out.println(" -clean clean build files");
		System.out.println(" -v verbose");
		System.out.println(" -f filename.jmk");
		System.out.println(" -t target_name(x86/x64)");
		System.out.println(" -c compile_type(debug/release)");
		System.out.println(" -skip_pre skip pre build");
	}
	static boolean readArgs(final ArgParam param,String[] args){
		boolean r=ArgReader.read(args,new ArgReader.Callback(){
			public boolean argNone(){
				param.parser_flg  =true;
				param.jmk_filename="build.jmk";
				return true;
			}
			public boolean optArg(ArgParser parser,String arg){
				if(arg.equals("h") || arg.equals("?") || arg.equals("help") || arg.equals("-help")){
					param.help_flg=true;
					param.parser_flg=false;
					return true;
				//==========================
				}else if(arg.equals("v") || arg.equals("verbose")){
					param.verbose_flg=true;
					param.parser_flg=true;
					return true;
				}else if(arg.equals("f")){
					param.jmk_filename=parser.get();
					param.parser_flg=true;
					return true;
				}else if(arg.equals("skip_pre") || arg.equals("skip")){
					param.skip_prebuild=true;
					param.parser_flg=true;
					return true;
				//==========================
				}else if(arg.equals("clean")){
					param.task_type=BuildTaskType.S_CLEAN;
					param.parser_flg=true;
					return true;
				}else if(arg.equals("run")){
					param.task_type=BuildTaskType.S_RUN;
					param.parser_flg=true;
					return true;
				}else if(arg.equals("build")){
					param.task_type=BuildTaskType.S_BUILD;
					param.parser_flg=true;
					return true;
				//==========================
				}else if(arg.equals("t") || arg.equals("target_type") || arg.equals("target")){
					param.target_type=parser.get();
					param.parser_flg=true;
					return true;
				}else if(arg.equals("c") || arg.equals("compile_type") || arg.equals("compile")){
					param.compile_type=parser.get();
					param.parser_flg=true;
					return true;
				}else if(arg.equals("compiler_type") || arg.equals("compiler")){
					param.compiler_type=parser.get();
					param.parser_flg=true;
					return true;
				//==========================
				}else if(arg.equals("x86")){
					param.target_type="x86";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("x64")){
					param.target_type="x64";
					param.parser_flg=true;
					return true;
				//==========================
				}else if(arg.equals("dbg") || arg.equals("debug")){
					param.compile_type="debug";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("dev") || arg.equals("develop")){
					param.compile_type="develop";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("rel") || arg.equals("release")){
					param.compile_type="release";
					param.parser_flg=true;
					return true;
				//==========================
				}else if(arg.equals("java")){
					param.compiler_type="java";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("win") || arg.equals("win_cpp")){
					param.compiler_type="win_cpp";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("em") || arg.equals("emscripten")){
					param.compiler_type="emscripten";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("ps4")){
					param.compiler_type="ps4";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("switch") || arg.equals("nx")){
					param.compiler_type="switch";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("ios")){
					param.compiler_type="ios";
					param.parser_flg=true;
					return true;
				}else if(arg.equals("android")){
					param.compiler_type="android";
					param.parser_flg=true;
					return true;
				//==========================
				}else if(arg.equals("test_inc")){
				//includeチェック
					String filename=parser.get();
					param.parser_flg=false;
//System.out.println("filename="+filename);
					File[] incs=CSourceIncludeCollector.getIncludeList(filename,new File[]{new File("e:\\prg\\")});
					for(int i=0;i<incs.length;i++){
						System.out.println("inc["+i+"]:"+incs[i]);
					}
					return true;
				}else if(arg.equals("test_files")){
					param.parser_flg=false;
					//
					JmkFileLinkChecker_CSource cs=new JmkFileLinkChecker_CSource();
					cs.addIncludePath(new File[]{new File("e:\\prg\\")});
					JmkFileLinkCheckerList checkers=new JmkFileLinkCheckerList();
					checkers.addChecker(cs);
					//
					File src_path=new File("e:\\prg\\cpp\\game\\mf2e\\src");
					File src_ignore_path=new File("e:\\prg\\cpp\\game\\mf2e\\src\\backends");
					File obj_path=new File("e:\\prg\\cpp\\game\\mf2e\\build\\x64\\Release");
					//
					JmkFileLinkData fld=new JmkFileLinkData();
					JmkFileSearchParam param=new JmkFileSearchParam();
					param.dirs=new String[]{src_path.toString()};
					param.ignore_dirs=new String[]{src_ignore_path.toString()};
					param.exts=new String[]{"c","cpp","cc"};
					String[] src_files=JmkFileTools.getSourceFiles(param);
					//
					System.out.println("src_files="+src_files.length);
					for(int i=0;i<src_files.length;i++){
						System.out.println("["+i+"]="+src_files[i]);
					}
					return true;
				}
				//==========================
				return false;
			}
			public boolean paramArg(ArgParser parser,String arg){
				return false;
			}
		});
		return r;
	}
	//
	static boolean execParam(ArgParam param){
		if(param.help_flg){
			printHelp();
			return true;
		}else if(param.parser_flg){
			JmkExecParser.execParser(param.jmk_filename,param.task_type,param.compiler_type,param.compile_type,param.target_type,param.skip_prebuild);
		}
		return true;
	}
}

class ArgParam{
	boolean default_flg=false;
	boolean help_flg=false;
	boolean parser_flg=false;
	String task_type=BuildTaskType.S_BUILD;
	boolean verbose_flg=false;
	String jmk_filename="build.jmk";
	String compile_type;
	String target_type;
	String compiler_type;
	boolean skip_prebuild=false;
}



