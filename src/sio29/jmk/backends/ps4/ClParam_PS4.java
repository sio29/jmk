package sio29.jmk.backends.ps4;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.cltools.*;

public class ClParam_PS4 extends ClParamBase{
	public ClParam_PS4(JmkBuildParam jmk_param,String _target_type,ClToolsEnvBase tools_env){
		source_exts=new String[]{"cpp","c","cc"};
		obj_ext="o";
		setJmkBuildParam(jmk_param);
		target_type=_target_type;
	}
	public LibParamBase createLibParam(ClToolsEnvBase tools_env){
		return new LibParam_PS4(this,tools_env);
	}
	public LinkParamBase createLinkParam(ClToolsEnvBase tools_env){
		return new LinkParam_PS4(this,tools_env);
	}
	//=================================================
	public String[] getClOpt_Post(ClToolsEnvBase tools_env,ClParamCurrent current_param){
		return getClOpt_Post(this,tools_env,current_param);
	}
	private static String[] getClOpt_Post(final ClParamBase cl_param,ClToolsEnvBase tools_env,ClParamCurrent current_param){
		//======================
		//Cmd.exe�̃p�����[�^�쐬
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,cl_add_opt_common);
		//�^�[�Q�b�g(x86/x64)
		if(ClTargetType_PS4.isX86(cl_param.target_type)){
			JmkStringTools.addArrayList(set,cl_add_opt_x86);
		}else{
			JmkStringTools.addArrayList(set,cl_add_opt_x64);
		}
		//�r���h���[�h(debug/release)
		if(ClCompileType.isDebug(cl_param.compile_type)){
			JmkStringTools.addArrayList(set,cl_add_opt_debug);
		}else{
			JmkStringTools.addArrayList(set,cl_add_opt_release);
		}
		//�����R�[�h
		if(cl_param.src_char_code!=null){
			if(cl_param.src_char_code.equals("unicode")){
				set.add("-DUNICODE");
				//set.add("/D\"_UNICODE\"");
				//set.add("/source-charset:utf-8");
			}
		}
		//���s�����R�[�h
		if(cl_param.exec_char_code!=null){
			//set.add("/execution-charset:"+cl_param.exec_char_code);
		}
		//�R���p�C���̂�
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
		//�\�[�X�t�@�C��
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
