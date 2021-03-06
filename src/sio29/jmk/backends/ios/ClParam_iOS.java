package sio29.jmk.backends.ios;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.cltools.*;

public class ClParam_iOS extends ClParamBase{
	public ClParam_iOS(JmkBuildParam jmk_param,String _target_type,ClToolsEnvBase tools_env){
		source_exts=new String[]{"cpp","c","cc"};
		obj_ext="o";
		setJmkBuildParam(jmk_param);
		target_type=_target_type;
	}
	public LibParamBase createLibParam(ClToolsEnvBase tools_env){
		return new LibParam_iOS(this,tools_env);
	}
	public LinkParamBase createLinkParam(ClToolsEnvBase tools_env){
		return new LinkParam_iOS(this,tools_env);
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
		if(ClTargetType_iOS.isX86(cl_param.target_type)){
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
				//set.add("/D\"_UNICODE\"");
				//set.add("/source-charset:utf-8");
			}
		}
		//実行文字コード
		if(cl_param.exec_char_code!=null){
			//set.add("/execution-charset:"+cl_param.exec_char_code);
		}
		//コンパイルのみ
		boolean compile_only=true;
//		if(cl_param.output_type==ClParam.OUTPUT_LIB || cl_param.output_type==ClParam.OUTPUT_OBJ){
			compile_only=true;
//		}
		if(compile_only){
			set.add("-c");
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
		if(compile_only){
			set.add("-o");
			set.add(current_param.output_file);
		}else{
			//output filename
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
