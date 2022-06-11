package sio29.jmk.backends.gcc;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.cltools.*;

public class ClParam_Gcc extends ClParamBase{
	public String exec_filename;
	
	public ClParam_Gcc(JmkBuildParam jmk_param,String _target_type,ClToolsEnvBase tools_env){
		source_exts=new String[]{"cpp","c","cc"};
		obj_ext="o";
		setJmkBuildParam(jmk_param);
		target_type=_target_type;
	}
	public LibParamBase createLibParam(ClToolsEnvBase tools_env){
		return new LibParam_Gcc(this,tools_env);
	}
	public LinkParamBase createLinkParam(ClToolsEnvBase tools_env){
		return new LinkParam_Gcc(this,tools_env);
	}
	//=================================================
	public String[] getClOpt_Post(ClToolsEnvBase tools_env,ClParamCurrent current_param){
		return getClOpt_Post(this,tools_env,current_param);
	}
	private static String[] getClOpt_Post(final ClParamBase cl_param,ClToolsEnvBase tools_env,ClParamCurrent current_param){
		ClParam_Gcc cl_param_gcc=(ClParam_Gcc)cl_param;
		if(cl_param.output_dir_tmp==null){
			cl_param_gcc.exec_filename=cl_param.proj_name;
		}else{
			cl_param_gcc.exec_filename=JmkFileTools.makePathName(new File(cl_param.output_dir_tmp),cl_param.proj_name);
		}
		
		//Cmd.exeのパラメータ作成
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,cl_add_opt_common);
		//ターゲット(x86/x64)
		if(ClTargetType_Gcc.isX86(cl_param.target_type)){
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
			}
		}
		//実行文字コード
		if(cl_param.exec_char_code!=null){
			set.add("-fexec-charset="+cl_param.exec_char_code.toLowerCase());
		}
		//コンパイルのみ
		boolean compile_only=true;
//compile_only=false;
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
				set.add("-D"+def);
			}
		}
		//lib
		if(cl_param.libs!=null){
			for(int i=0;i<cl_param.libs.length;i++){
				String lib=cl_param.libs[i];
				set.add("-l"+lib);
			}
		}
		//output
		if(compile_only){
			if(current_param.output_file!=null){
				set.add("-o"+current_param.output_file);
			}
		}else{
			if(cl_param_gcc.exec_filename!=null){
				set.add("-o"+cl_param_gcc.exec_filename);
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
	static String[] cl_add_opt_debug={
	};
	static String[] cl_add_opt_release={
	};
	static String[] cl_add_opt_x86={
	};
	static String[] cl_add_opt_x64={
	};
}
