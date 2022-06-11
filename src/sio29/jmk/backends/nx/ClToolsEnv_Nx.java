package sio29.jmk.backends.nx;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

//=================================================
	/*
	clang
	-x c++
	-std=gnu++14
	-fno-common
	-fno-short-enums
	-ffunction-sections
	-fdata-sections
	-fPIC
	-mcpu=cortex-a57+fp+simd+crypto+crc
	-g
	-I%NINTENDO_SDK_ROOT%\Include
	-I%NINTENDO_SDK_ROOT%\Common\Configs\Targets\<ビルドターゲット>\Include
	-DNN_NINTENDO_SDK
	-DNN_SDK_BUILD_<ビルドタイプ>
	-c
	-o <オブジェクトファイル>
	<ソースファイル>
	*/
	
	/*
	//必須オプション
	-std=gnu++14
	-fno-common
	-fno-short-enums
	-ffunction-sections
	-fdata-sections
	-fPIC
	*/
	/*
	//32bit用
	-mabi=aapcs-linux
	-mcpu=cortex-a57
	-mfloat-abi=hard
	-mfpu=crypto-neon-fp-armv8
	*/
	/*
	//64bit用
	-mcpu=cortex-a57+fp+simd+crypto+crc
	*/
	/*
	//※禁止オプション
	-fshort-wchar
	-fpack-struct
	*/
//=================================================


public class ClToolsEnv_Nx extends ClToolsEnvDefault{
	//%NINTENDO_SDK_ROOT%\Compilers\NX
	private final static String nintendo_sdk_root="NINTENDO_SDK_ROOT";
	//private final static String compiler_path="\\Compiler\\NX";
	private final static String compiler_path="\\Compilers\\NX\\bin";
	private final static String compiler_path_arm32="\\Compilers\\NX\\nx\\armv7l\\bin";
	private final static String compiler_path_arm64="\\Compilers\\NX\\nx\\aarch64\\bin";
	private final static String tools_path="\\Tools";
	private final static String clang_filename="nx-clang.exe";
	private final static String clang_cpp_filename="nx-clang++.exe";
	private final static String ar_filename="llvm-ar.exe";
	private final static String ld_filename="ld.lld.exe";
	private final static String toolchain_32bit="armv7l-nintendo-nx-eabihf";
	private final static String toolchain_64bit="aarch64-nintendo-nx-elf";
	private final static String include_path="\\Include";
	private final static String target_include_path="\\Common\\Configs\\Targets\\%s\\Include";
	private final static String lib_base_path="\\Libraries";
	private final static String nintendo_sdk_def="NN_NINTENDO_SDK";
	private final static String build_type_debug  ="NN_SDK_BUILD_DEBUG";
	private final static String build_type_develop="NN_SDK_BUILD_DEVELOP";
	private final static String build_type_release="NN_SDK_BUILD_RELEASE";
	
	public ClToolsEnv_Nx(ShellExecBase _shellexec,String target_type) throws Exception {
		shellexec=_shellexec;
		if(!init(target_type)){
			throw new Exception("ClTools_Nx.Env init error !!");
		}
	}
	public String getNintendoSdkRoot(){
		return shellexec.getEnv("NINTENDO_SDK_ROOT");
	}
	private boolean init(String target_type){
		String sdk_root=getNintendoSdkRoot();
System.out.println("sdk_root="+sdk_root);
		String cl_path =sdk_root+compiler_path;
		String cl_path_arm32=sdk_root+compiler_path_arm32;
		String cl_path_arm64=sdk_root+compiler_path_arm64;
System.out.println("cl_path="+cl_path);
System.out.println("cl_path_arm64="+cl_path_arm64);
		//パスを得る
		File[] paths=new File[]{};
		paths=JmkFileTools.addFileArray(paths,cl_path);
		paths=JmkFileTools.addFileArray(paths,cl_path_arm64);
		//cl.exeのパスを得る
		cl_file=JmkFileTools.findFile(paths,clang_cpp_filename);
		lib_file =JmkFileTools.findFile(paths,ar_filename);
		//link_file=JmkFileTools.findFile(paths,ld_filename);
		link_file=cl_file;
System.out.println("cl_file="+cl_file);
System.out.println("lib_file="+lib_file);
System.out.println("link_file="+link_file);
		return true;
	}
	public String makeBuildDir(String build_dir,String compile_type,String target_type){
		String m="";
		if(build_dir!=null){
			m=JmkFileTools.addPath(m,build_dir);
		}
		if(ClTargetType_Nx.isX86(target_type)){
			m=JmkFileTools.addPath(m,"arm32\\");
		}else{
			m=JmkFileTools.addPath(m,"arm64\\");
		}
		if(ClCompileType.isDebug(compile_type)){
			m=JmkFileTools.addPath(m,"debug\\");
		}else{
			m=JmkFileTools.addPath(m,"release\\");
		}
		return m;
	}
	public ClParamBase createClParam(JmkBuildParam jmk_param,String target_type){
		return new ClParam_Nx(jmk_param,target_type,this);
	}
	public boolean execEnv(JmkBuildParam jmk_param,String target_type){
		return execEnv_ClAndLink(jmk_param,target_type);
	}
	
}


