package sio29.jmk.backends.nx;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.cltools.*;

public class ClParam_Nx extends ClParamBase{
	public ClParam_Nx(JmkBuildParam jmk_param,String _target_type,ClToolsEnvBase tools_env){
		source_exts=new String[]{"cpp","c","cc"};
		obj_ext="o";
		setJmkBuildParam(jmk_param);
		target_type=_target_type;
	}
	public LibParamBase createLibParam(ClToolsEnvBase tools_env){
		return new LibParam_Nx(this,tools_env);
	}
	public LinkParamBase createLinkParam(ClToolsEnvBase tools_env){
		return new LinkParam_Nx(this,tools_env);
	}
	//=================================================
	public String[] getClOpt_Post(ClToolsEnvBase tools_env,ClParamCurrent current_param){
		return getClOpt_Post(this,tools_env,current_param);
	}
	private static String[] getClOpt_Post(final ClParamBase cl_param,ClToolsEnvBase tools_env,ClParamCurrent current_param){
		ClToolsEnv_Nx tools_env_nx=(ClToolsEnv_Nx)tools_env;
		String nintendo_sdk_root=tools_env_nx.getNintendoSdkRoot();
		String build_target="NX-NXFP2-a64";
		//======================
		//Cmd.exeのパラメータ作成
		ArrayList<String> set=new ArrayList<String>();
		//common
		JmkStringTools.addArrayList(set,cl_add_opt_common);
		//ターゲット(x86/x64)
		if(ClTargetType_Nx.isX86(cl_param.target_type)){
			JmkStringTools.addArrayList(set,cl_add_opt_32bit);
			
		}else{
			JmkStringTools.addArrayList(set,cl_add_opt_64bit);
		}
		//ビルドモード(debug/release)
		if(ClCompileType.isDebug(cl_param.compile_type)){
			JmkStringTools.addArrayList(set,cl_add_opt_debug);
		}else{
			JmkStringTools.addArrayList(set,cl_add_opt_release);
		}
		//文字コード
		if(cl_param.src_char_code!=null){
			if(cl_param.src_char_code.equals("unicode")){
				set.add("-DUNICODE");
			}
		}
		//実行文字コード
		if(cl_param.exec_char_code!=null){
			set.add("/execution-charset:"+cl_param.exec_char_code);
		}
		//コンパイルのみ
		boolean compile_only=true;
//		if(cl_param.output_type==ClParam.OUTPUT_LIB || cl_param.output_type==ClParam.OUTPUT_OBJ){
			compile_only=true;
//		}
		if(compile_only){
			set.add("-c");
		}
		//SDK include
		{
			set.add("-I"+nintendo_sdk_root+"\\Include");
			set.add("-I"+nintendo_sdk_root+"\\Common\\Configs\\Targets\\"+build_target+"\\Include");
		}
		//include
		if(cl_param.include_dirs_tmp!=null){
			for(int i=0;i<cl_param.include_dirs_tmp.length;i++){
				String inc_dir=cl_param.include_dirs_tmp[i];
				set.add("-I"+inc_dir);
			}
		}
		//define
		if(cl_param.defs!=null){
			for(int i=0;i<cl_param.defs.length;i++){
				String def=cl_param.defs[i];
				set.add("-D\""+def+"\"");
			}
		}
		//output filename
		if(compile_only){
			set.add("-o");
			set.add(current_param.output_file);
		}else{
			if(cl_param.output_dir_tmp!=null){
				String out_filename=JmkFileTools.makePathName(new File(cl_param.output_dir_tmp),cl_param.proj_name);
				out_filename+=".o";
	System.out.println("out_filename="+out_filename);
				set.add("-o"+out_filename);
			}
		}
		//ソースファイル
		JmkStringTools.addString(set,current_param.source_files);
		//
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	
	
	//=================================================
	//-fshort-wcharと-fpack-structは使用してはいけない
	//=================================================
	static String[] cl_add_opt_common={
//		"-x c++",
		//common
		"-std=gnu++14",
		"-fno-common",
		"-fno-short-enums",
		"-ffunction-sections",
		"-fdata-sections",
		"-fPIC",
		//64bit
//		"-mcpu=cortex-a57+fp+simd+crypto+crc",
		//debug info
		"-g",
		//compile only
		"-c",
		//NINTENDO SDK
		"-DNN_NINTENDO_SDK",
		//unuse off
		//"-Wunused-variable",
		"-Wunused-const-variable",
		//"-Wunused-const-variable=1",
		//"-Wunused-const-variable=2",
	};
	static String[] cl_add_opt_32bit={
		"-mabi=aapcs-linux",
		"-mcpu=cortex-a57",
		"-mfloat-abi=hard",
		"-mfpu=crypto-neon-fp-armv8",
	};
	static String[] cl_add_opt_64bit={
//		"-mcpu=cortex-a57+fp+simd+crypto+crc",
	};
	static String[] cl_add_opt_make={
	};
	static String[] cl_add_opt_debug={
		"-DNN_SDK_BUILD_DEBUG",
		//最適化
		"-O0","-fno-omit-frame-pointer",
		//warnning
		"-Wall",
	};
	static String[] cl_add_opt_develop={
		"-DNN_SDK_BUILD_DEVELOP",
		//最適化
		"-O3","-fno-omit-frame-pointer",
		//warnning
		"-Wall",
	};
	static String[] cl_add_opt_release={
		"-DNN_SDK_BUILD_RELEASE",
		//最適化
		"-O3","-fomit-frame-pointer",
		//warnning
		"-Wall",
	};
	static String[] g_build_target_windows={
		"Win32-v140",
		"Win32-v141",
		"x64-v140",
		"x64-v141",
	};
	static String[] g_build_target_nx={
		"NX-Win32-v140",
		"NX-Win32-v141",
		"NX-x64-v140",
		"NX-x64-v141",
		"NX-NXFP2-a32",
		"NX-NXFP2-a64",
	};
}
