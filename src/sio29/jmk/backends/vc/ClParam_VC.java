package sio29.jmk.backends.vc;

import java.util.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.cltools.*;

public class ClParam_VC extends ClParamBase{
	private String pdb_filename_tmp;
	private String pch_filename_tmp;
	private boolean use_asm=false;
	private boolean use_pdb=true;
	private boolean use_pch=true;
	//=================================================
	public ClParam_VC(JmkBuildParam jmk_param,String _target_type,ClToolsEnvBase tools_env){
		source_exts=new String[]{"cpp","c","cc"};
		obj_ext="obj";
		setJmkBuildParam(jmk_param);
		target_type=_target_type;
	}
	public LibParamBase createLibParam(ClToolsEnvBase tools_env){
		return new LibParam_VC(this,tools_env);
	}
	public LinkParamBase createLinkParam(ClToolsEnvBase tools_env){
		return new LinkParam_VC(this,tools_env);
	}
	public void makeParam_Ext(ClToolsEnvBase tools_env){
		//======================
		//.pdb�t�@�C�����쐬
		pdb_filename_tmp=JmkFileTools.addPathString(build_dir_tmp,proj_name+".pdb");
		//.pch�t�@�C�����쐬
		pch_filename_tmp=JmkFileTools.addPathString(build_dir_tmp,proj_name+".pch");
	}
	//=================================================
	//cl.exe�̃I�v�V����
	public String[] getClOpt_Post(ClToolsEnvBase tools_env,ClParamCurrent current_param){
		return getClOpt_Post(this,tools_env,current_param);
	}
	private static String[] getClOpt_Post(final ClParamBase cl_param,ClToolsEnvBase tools_env,ClParamCurrent current_param){
		ClParam_VC cl_param_vc=(ClParam_VC)cl_param;
		//======================
		//Cmd.exe�̃p�����[�^�쐬
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,cl_add_opt_common);
		//�^�[�Q�b�g(x86/x64)
		if(ClTargetType_VC.isX86(cl_param.target_type)){
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
				set.add("/D\"UNICODE\"");
				set.add("/D\"_UNICODE\"");
				set.add("/source-charset:utf-8");
			}
		}
		//���s�����R�[�h
		if(cl_param.exec_char_code!=null){
			set.add("/execution-charset:"+cl_param.exec_char_code);
		}
		//�R���p�C���̂�
		boolean compile_only=true;
//		if(param.output_type==ClParam.OUTPUT_LIB || param.output_type==ClParam.OUTPUT_OBJ){
			compile_only=true;
//		}
		if(compile_only){
			set.add("/c");
		}
		//include
		String[] include_dirs=cl_param.include_dirs_tmp;
		if(include_dirs!=null){
			for(int i=0;i<include_dirs.length;i++){
				String inc_dir=include_dirs[i];
				set.add("/I"+inc_dir);
			}
		}
		//define
		String[] defs=cl_param.defs;
		if(defs!=null){
			for(int i=0;i<defs.length;i++){
				String def=defs[i];
				set.add("/D\""+def+"\"");
			}
		}
		//.asm�t�@�C����
		if(cl_param_vc.use_asm){
			set.add("/Fa"+JmkStringTools.makeOptString(cl_param.build_dir_tmp));
		}
		//.pdb�t�@�C����
		if(cl_param_vc.use_pdb){
			set.add("/Fd"+JmkStringTools.makeOptString(cl_param_vc.pdb_filename_tmp));
		}
		//.pch�t�@�C����
		if(cl_param_vc.use_pch){
			set.add("/Fp"+JmkStringTools.makeOptString(cl_param_vc.pch_filename_tmp));
		}
		//.obj�̏o�͐�
		set.add("/Fo"+JmkStringTools.makeOptString(cl_param.build_dir_tmp));
		//
		if(cl_param.cl_opt!=null){
			for(int i=0;i<cl_param.cl_opt.length;i++){
				set.add(cl_param.cl_opt[i]);
			}
		}
		
		//�\�[�X�t�@�C��
		JmkStringTools.addString(set,current_param.source_files);
		//
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	//cl.exe x86 -> x64
	private static String[] cl_add_opt_common={
		"/D\"_LIB\"",			//.lib����
		"/EHsc",				//C++ EH ��L���ɂ��� (SEH ��O�Ȃ�) extern "C" �͊���� nothrow �ɂȂ� 
		"/FC",					//�f�f�Ŋ��S�p�X�����g�p����
		"/GS",					//�Z�L�����e�B �`�F�b�N��L���ɂ���
		"/Gd",					//__cdecl�̌Ăяo���K��
		"/Gm-",					//�ŏ����r���h��L���ɂ���
		"/W3",					//�x�����x����ݒ肷��
		"/WX-",					//�x�����G���[�Ƃ��Ĉ����܂�
		"/Zc:forScope",			//�X�R�[�v�K���ɕW�� C++ ��K�p���܂�
		"/Zc:inline",			//remove unreferenced function or data if it is COMDAT or has internal linkage only (off by default)
		"/Zc:wchar_t",			//wchar_t �̓l�C�e�B�u�^�ł����āAtypedef �ł͂���܂���
		"/diagnostics:classic",	//�f�f���b�Z�[�W�̌`���𐧌䂵�܂�:�ȑO�̌`����ێ����܂�
		"/errorReport:prompt",	//���|�[�g�������ɑ��M���邩�ǂ�����q�˂郁�b�Z�[�W��\�����܂�
		"/fp:precise",			//���������_���f���ł��B���ʂ͗\���\�ł�
		"/nologo",				//���쌠���b�Z�[�W��\�����Ȃ�
		"/permissive-",			//�񏀋��̃R�[�h�̈ꕔ���R���p�C���ł���悤�ɂ��܂�
		"/sdl",					//�ǉ��̃Z�L�����e�B�@�\�ƌx����L���ɂ���
	};
//	/Fa"Debug\\"
//	/Fa"x64\\Debug\\"
//	/Fd"Debug\\udraw2d.pdb"
//	/Fd"x64\\Debug\\udraw2d.pdb"
//	/Fo"Debug\\"
//	/Fo"x64\\Debug\\"
//	/Fp"Debug\\udraw2d.pch"
//	/Fp"x64\\Debug\\udraw2d.pch"
//	/Fa"Release\\"
//	/Fa"x64\\Release\\"
//	/Fd"Release\\udraw2d.pdb"
//	/Fd"x64\\Release\\udraw2d.pdb"
//	/Fo"Release\\"
//	/Fo"x64\\Release\\"
//	/Fp"Release\\udraw2d.pch"
//	/Fp"x64\\Release\\udraw2d.pch"
	/*
	private static String[] cl_add_opt_make={
		"/Fa",		//�A�Z���u�� ���X�e�B���O �t�@�C���ɖ��O��t���܂�
		"/Fd",		//.PDB �t�@�C�������w�肷��
		"/Fo",		//�I�u�W�F�N�g �t�@�C�������w�肷��
		"/Fp",		//.PCH �t�@�C�������w�肷��
	};
	*/
	private static String[] cl_add_opt_debug={
		"/D\"NDEBUG\"",
		"/D\"_DEBUG\"",
		"/MDd",		//MSVCRTD.LIB �f�o�b�O ���C�u�����Ń����N����
		"/Od",		//�œK���𖳌��ɂ��� 
		"/RTC1",	//�����`�F�b�N��L���ɂ���
		"/Zi",		//�f�o�b�O����L���ɂ��܂�
	};
	private static String[] cl_add_opt_release={
		"/GL",		//�����N���̃R�[�h������L���ɂ��� �v���O�����S�̂̍œK��
		"/Gy",		//�����J�[�̕ʂ̋@�\
		"/MD",		//MSVCRT.LIB �Ń����N����
		"/O2",		//�ő���̍œK��
		"/Oi",		//�g�ݍ��݊֐���L���ɂ���
		//"/Zi",		//�f�o�b�O����L���ɂ��܂�
	};
	private static String[] cl_add_opt_x86={
		"/D\"WIN32\"",
		"/Oy-",			//�t���[�� �|�C���^�[�̏ȗ���L���ɂ��� 
		"/analyze-",	//�l�C�e�B�u�̕��͂�L���ɂ���
	};
	private static String[] cl_add_opt_x64={
		"/D\"WIN32\"",
	};
}
