package sio29.jmk.backends.emscripten;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.cltools.*;

public class ClParam_Emscripten extends ClParamBase{
	public String pre_js;
	public String[] js_opts;
	public String[] js_cfuncs;
	//
	public ClParam_Emscripten(JmkBuildParam jmk_param,String _target_type,ClToolsEnvBase tools_env){
		source_exts=new String[]{"cpp","c","cc"};
		obj_ext="o";
		setJmkBuildParam(jmk_param);
		target_type=_target_type;
	}
	public LibParamBase createLibParam(ClToolsEnvBase tools_env){
		return new LibParam_Emscripten(this,tools_env);
	}
	public LinkParamBase createLinkParam(ClToolsEnvBase tools_env){
		return new LinkParam_Emscripten(this,tools_env);
	}
	public void setJmkBuildParam_Ext(JmkBuildParam jmk_param){
		pre_js=jmk_param.getPreJs();
		js_opts=jmk_param.getJsOpts();
		js_cfuncs=jmk_param.getJsCFuncs();
//System.out.println("pre_js="+pre_js);
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
		if(ClTargetType_Emscripten.isX86(cl_param.target_type)){
			JmkStringTools.addArrayList(set,cl_add_opt_x86);
		}else{
			JmkStringTools.addArrayList(set,cl_add_opt_x64);
		}
		//ビルドモード(debug/release)
		boolean debug_flg=ClCompileType.isDebug(cl_param.compile_type);
		if(debug_flg){
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
		
//		set.add("--source-map-base");
//		set.add("build");
		
//		set.add("-s");
//		set.add("ERROR_ON_UNDEFINED_SYMBOLS=0");
		
		
		//set.add("-O2");
//		set.add("-O0");
//		set.add("--llvm-opts");
//		set.add("0");
		
		
		if(debug_flg){
		//debug
			set.add("-g4");
		}else{
		//release
			//最適化オプション
			set.add("-Oz");
			//クロージャー
			set.add("--closure");
			set.add("1");
			//
			set.add("--bind");
		}
		
//		set.add("-v");
		//Use OpenGL ES2.0
		set.add("-s");
		set.add("FULL_ES2=1");
		//メモリアロケーションエラーの有効
		set.add("-s");
		set.add("ASSERTIONS=1");
		//リンクタイムオプティマイズの無効
		set.add("-s");
		set.add("LINKABLE=1");
		//==========================
		String[] js_opts=((ClParam_Emscripten)cl_param).js_opts;
		if(js_opts!=null){
			for(int i=0;i<js_opts.length;i++){
				set.add("-s");
				set.add(js_opts[i]);
			}
		}
		//==========================
		
		
		//==========================
		//ソースファイル
		JmkStringTools.addString(set,current_param.source_files);
		//
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	static String[] cl_add_opt_common={
//		"-s ERROR_ON_UNDEFINED_SYMBOLS=0"
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
