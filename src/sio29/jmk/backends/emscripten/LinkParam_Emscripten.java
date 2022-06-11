package sio29.jmk.backends.emscripten;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.cltools.*;

public class LinkParam_Emscripten extends LinkParamBase{
	public String pre_js;
	public String[] js_opts;
	public String[] js_cfuncs;
	//
	public LinkParam_Emscripten(ClParamBase cl_param,ClToolsEnvBase tools_env){
		if(ClOutputType.isExe(cl_param.output_type)){
			exe_filename=cl_param.proj_name+".html";
		}else{
			exe_filename=cl_param.proj_name+".so";
		}
		setClParamBase(cl_param);
		//
		pre_js =((ClParam_Emscripten)cl_param).pre_js;
		js_opts=((ClParam_Emscripten)cl_param).js_opts;
		js_cfuncs=((ClParam_Emscripten)cl_param).js_cfuncs;
	}
	public String[] converLinkParam(ClToolsEnvBase tools_env){
		return converLinkParam(this,tools_env);
	}
	//=================================================
	private static String[] converLinkParam(LinkParamBase link_param,ClToolsEnvBase tools_env){
		LinkParam_Emscripten _link_param=(LinkParam_Emscripten)link_param;
		link_param.build_dir_tmp=tools_env.makeBuildDir(link_param.build_dir,link_param.compile_type,link_param.target_type);
System.out.println("link_param.build_dir_tmp="+link_param.build_dir_tmp);
System.out.println("link_param.exe_filename_tmp="+link_param.exe_filename_tmp);
System.out.println("link_param.exe_filename="+link_param.exe_filename);
		//
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,link_add_opt_common);
//		if(ClTargetType_Emscripten.isX86(link_param.target_type)){
//			JmkStringTools.addArrayList(set,link_add_opt_x86);
//		}else{
//			JmkStringTools.addArrayList(set,link_add_opt_x64);
//		}
		boolean debug_flg=ClCompileType.isDebug(link_param.compile_type);
		if(debug_flg){
			JmkStringTools.addArrayList(set,link_add_opt_debug);
		}else{
			JmkStringTools.addArrayList(set,link_add_opt_release);
		}
//		for(int i=0;i<link_param.lib_dirs.length;i++){
//			set.add("-LIBPATH:"+link_param.lib_dirs[i]);
//		}
//set.add("-s");
//set.add("\"EXPORTED_FUNCTIONS=['_WebAudio_MakePcm']\"");
		
		//==========================
		
		//==========================
		//C言語でラップする関数
		set.add("-s");
		set.add("EXTRA_EXPORTED_RUNTIME_METHODS=\"[\'ccall\',\'cwrap\']\"");
		//set.add("EXTRA_EXPORTED_RUNTIME_METHODS=\"[\'ccall\']\"");
		
		String[] js_cfuncs=_link_param.js_cfuncs;
		if(js_cfuncs!=null && js_cfuncs.length>0){
			String cfunc_m="EXPORTED_FUNCTIONS=\"[";
			for(int i=0;i<js_cfuncs.length;i++){
				String cf=js_cfuncs[i];
				if(i!=0)cfunc_m+=",";
				cfunc_m+="\'"+cf+"\'";
			}
			cfunc_m+="]\"";
			set.add("-s");
			set.add(cfunc_m);
System.out.println(cfunc_m);
		}
		//==========================
		String[] js_opts=_link_param.js_opts;
		if(js_opts!=null){
			for(int i=0;i<js_opts.length;i++){
				set.add("-s");
				set.add(js_opts[i]);
			}
		}
		//==========================
		//出力ファイル名
		set.add("-o");
		set.add(link_param.exe_filename_tmp);
		//==========================
		
//		set.add("--source-map-base");
//		set.add("output");
		
		
		/*
		set.add("--llvm-lto");
		set.add("0");
		
		set.add("--llvm-opts");
		set.add("0");
		
		set.add("--js-opts");
		set.add("0");
		*/
		
		
		//Use OpenGL ES2.0
		set.add("-s");
		set.add("FULL_ES2=1");
		//SDL2.0使用
		set.add("-s");
		set.add("USE_SDL=2");
		//メモリ拡張許可
		set.add("-s");
		set.add("ALLOW_MEMORY_GROWTH=1");
		
		//int total_memory=18,550,784;
//		int total_memory=64*1024*1024;
//		set.add("-s");
//		set.add("TOTAL_MEMORY="+total_memory);
		
		
		//メモリアロケーションエラーの有効
		set.add("-s");
		set.add("ASSERTIONS=1");
		
		//リンクタイムオプティマイズの無効
		set.add("-s");
		set.add("LINKABLE=1");
		
//		if(debug_flg){
		//スタックトレースを使う?
		set.add("-s");
		set.add("DEMANGLE_SUPPORT=1");
		
		//シンボルがない場合、エラーを出す
		set.add("-s");
//		set.add("ERROR_ON_UNDEFINED_SYMBOLS=0");
		set.add("ERROR_ON_UNDEFINED_SYMBOLS=1");
		
//		}

		//シンボルがない場合、エラーを出す
//		set.add("-d");
//		set.add("EMCC_DEBUG=0");

//		set.add("-s");
//		set.add("MINIMAL_RUNTIME=1");

		if(!debug_flg){
		//set.add("-s");
		//set.add("MINIFY_HTML=1");
		//set.add("MINIFY_HTML=0");
		}
		if(debug_flg){
		//debug
			set.add("-s");
			set.add("SAFE_HEAP=1");
			//
			set.add("--emit-symbol-map");
			//
			set.add("-g4");
		}else{
		//relase
			//最適化オプション
			set.add("-Oz");
			//クロージャー
			set.add("--closure");
			set.add("1");
			//link最適化
			set.add("--llvm-lto");
			set.add("1");
			//例外無視
			//set.add("-fno-exceptions");
			//
			set.add("--bind");
		}
		
		//set.add("-v");
		
		//==========================
		//pre.jsの指定
		if(_link_param.pre_js!=null){
System.out.println("pre_js="+_link_param.pre_js);
			set.add("--pre-js");
			set.add(_link_param.pre_js);
		}
		//==========================
		if(link_param.obj_files!=null)JmkStringTools.addString(set,link_param.obj_files);
		if(link_param.lib_files!=null)JmkStringTools.addString(set,link_param.lib_files);
		
//for(int i=0;i<set.size();i++){
//System.out.println(""+set.get(i));
//}
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	// /LTCG (リンク時のコード生成)
	static String[] link_add_opt_common={
//		"-s ERROR_ON_UNDEFINED_SYMBOLS=0"
	};
	static String[] link_add_opt_debug={
	};
	static String[] link_add_opt_release={
	};
	static String[] link_add_opt_x86={
	};
	static String[] link_add_opt_x64={
	};
}
