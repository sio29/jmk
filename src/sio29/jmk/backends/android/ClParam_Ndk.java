package sio29.jmk.backends.android;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.cltools.*;

public class ClParam_Ndk extends ClParamBase{
	public ClParam_Ndk(JmkBuildParam jmk_param,String _target_type,ClToolsEnvBase tools_env){
		source_exts=new String[]{"cpp","c","cc"};
		obj_ext="o";
		setJmkBuildParam(jmk_param);
		target_type=_target_type;
	}
	public LibParamBase createLibParam(ClToolsEnvBase tools_env){
		return new LibParam_Ndk(this,tools_env);
	}
	public LinkParamBase createLinkParam(ClToolsEnvBase tools_env){
		return new LinkParam_Ndk(this,tools_env);
	}
	//=================================================
	public String[] getClOpt_Post(ClToolsEnvBase tools_env,ClParamCurrent current_param){
		return getClOpt_Post(this,tools_env,current_param);
	}
	private static String[] getClOpt_Post(final ClParamBase cl_param,ClToolsEnvBase tools_env,ClParamCurrent current_param){
		//======================
		//Cmd.exeのパラメータ作成
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,cl_add_opt_common);
		//ターゲット(x86/x64)
		if(ClTargetType_Ndk.isX86(cl_param.target_type)){
			JmkStringTools.addArrayList(set,cl_add_opt_x86);
		}else{
			JmkStringTools.addArrayList(set,cl_add_opt_x64);
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
				//set.add("-D\"_UNICODE\"");
				//set.add("-source-charset:utf-8");
			}
		}
		//実行文字コード
		if(cl_param.exec_char_code!=null){
			//set.add("-execution-charset:"+param.exec_char_code);
		}
		//コンパイルのみ
		boolean compile_only=true;
//		if(cl_param.output_type==ClParam.OUTPUT_LIB || cl_param.output_type==ClParam.OUTPUT_OBJ){
			compile_only=true;
//		}
//compile_only=false;
		if(compile_only){
			set.add("-c");
		}
		//include
		String[] include_dirs=cl_param.include_dirs_tmp;
		if(include_dirs!=null){
			for(int i=0;i<include_dirs.length;i++){
				String inc_dir=include_dirs[i];
				set.add("-I"+inc_dir);
			}
		}
		//define
		String[] defs=cl_param.defs;
		if(defs!=null){
			for(int i=0;i<defs.length;i++){
				String def=defs[i];
				set.add("-D"+def);
			}
		}
		//output filename
		if(compile_only){
			set.add("-o");
			set.add(current_param.output_file);
		}else{
			if(cl_param.output_dir_tmp!=null){
				String out_filename=JmkFileTools.makePathName(new File(cl_param.output_dir_tmp),cl_param.proj_name);
				//String out_filename=cl_param.proj_name;
				//out_filename+=".o";
				//out_filename+=".js";
				out_filename+=".html";
	System.out.println("out_filename="+out_filename);
				set.add("-o");
				set.add(out_filename);
			}
		}
		//ソースファイル
		JmkStringTools.addString(set,current_param.source_files);
		//
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	static String[] cl_add_opt_common={
	};
	static String[] cl_add_opt_make={
	};
	static String[] cl_add_opt_debug={
	};
	static String[] cl_add_opt_release={
	};
	static String[] cl_add_opt_x86={
	};
	static String[] cl_add_opt_x64={
	};
}
