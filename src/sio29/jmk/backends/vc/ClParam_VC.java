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
		//.pdbファイル名作成
		pdb_filename_tmp=JmkFileTools.addPathString(build_dir_tmp,proj_name+".pdb");
		//.pchファイル名作成
		pch_filename_tmp=JmkFileTools.addPathString(build_dir_tmp,proj_name+".pch");
	}
	//=================================================
	//cl.exeのオプション
	public String[] getClOpt_Post(ClToolsEnvBase tools_env,ClParamCurrent current_param){
		return getClOpt_Post(this,tools_env,current_param);
	}
	private static String[] getClOpt_Post(final ClParamBase cl_param,ClToolsEnvBase tools_env,ClParamCurrent current_param){
		ClParam_VC cl_param_vc=(ClParam_VC)cl_param;
		//======================
		//Cmd.exeのパラメータ作成
		ArrayList<String> set=new ArrayList<String>();
		JmkStringTools.addArrayList(set,cl_add_opt_common);
		//ターゲット(x86/x64)
		if(ClTargetType_VC.isX86(cl_param.target_type)){
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
				set.add("/D\"UNICODE\"");
				set.add("/D\"_UNICODE\"");
				set.add("/source-charset:utf-8");
			}
		}
		//実行文字コード
		if(cl_param.exec_char_code!=null){
			set.add("/execution-charset:"+cl_param.exec_char_code);
		}
		//コンパイルのみ
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
		//.asmファイル名
		if(cl_param_vc.use_asm){
			set.add("/Fa"+JmkStringTools.makeOptString(cl_param.build_dir_tmp));
		}
		//.pdbファイル名
		if(cl_param_vc.use_pdb){
			set.add("/Fd"+JmkStringTools.makeOptString(cl_param_vc.pdb_filename_tmp));
		}
		//.pchファイル名
		if(cl_param_vc.use_pch){
			set.add("/Fp"+JmkStringTools.makeOptString(cl_param_vc.pch_filename_tmp));
		}
		//.objの出力先
		set.add("/Fo"+JmkStringTools.makeOptString(cl_param.build_dir_tmp));
		//
		if(cl_param.cl_opt!=null){
			for(int i=0;i<cl_param.cl_opt.length;i++){
				set.add(cl_param.cl_opt[i]);
			}
		}
		
		//ソースファイル
		JmkStringTools.addString(set,current_param.source_files);
		//
		String[] ret=(String[] )set.toArray(new String[]{});
		return ret;
	}
	//=================================================
	//cl.exe x86 -> x64
	private static String[] cl_add_opt_common={
		"/D\"_LIB\"",			//.lib生成
		"/EHsc",				//C++ EH を有効にする (SEH 例外なし) extern "C" は既定の nothrow になる 
		"/FC",					//診断で完全パス名を使用する
		"/GS",					//セキュリティ チェックを有効にする
		"/Gd",					//__cdeclの呼び出し規約
		"/Gm-",					//最小リビルドを有効にする
		"/W3",					//警告レベルを設定する
		"/WX-",					//警告をエラーとして扱います
		"/Zc:forScope",			//スコープ規則に標準 C++ を適用します
		"/Zc:inline",			//remove unreferenced function or data if it is COMDAT or has internal linkage only (off by default)
		"/Zc:wchar_t",			//wchar_t はネイティブ型であって、typedef ではありません
		"/diagnostics:classic",	//診断メッセージの形式を制御します:以前の形式を保持します
		"/errorReport:prompt",	//レポートをすぐに送信するかどうかを尋ねるメッセージを表示します
		"/fp:precise",			//浮動小数点モデルです。結果は予測可能です
		"/nologo",				//著作権メッセージを表示しない
		"/permissive-",			//非準拠のコードの一部をコンパイルできるようにします
		"/sdl",					//追加のセキュリティ機能と警告を有効にする
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
		"/Fa",		//アセンブリ リスティング ファイルに名前を付けます
		"/Fd",		//.PDB ファイル名を指定する
		"/Fo",		//オブジェクト ファイル名を指定する
		"/Fp",		//.PCH ファイル名を指定する
	};
	*/
	private static String[] cl_add_opt_debug={
		"/D\"NDEBUG\"",
		"/D\"_DEBUG\"",
		"/MDd",		//MSVCRTD.LIB デバッグ ライブラリでリンクする
		"/Od",		//最適化を無効にする 
		"/RTC1",	//高速チェックを有効にする
		"/Zi",		//デバッグ情報を有効にします
	};
	private static String[] cl_add_opt_release={
		"/GL",		//リンク時のコード生成を有効にする プログラム全体の最適化
		"/Gy",		//リンカーの別の機能
		"/MD",		//MSVCRT.LIB でリンクする
		"/O2",		//最大限の最適化
		"/Oi",		//組み込み関数を有効にする
		//"/Zi",		//デバッグ情報を有効にします
	};
	private static String[] cl_add_opt_x86={
		"/D\"WIN32\"",
		"/Oy-",			//フレーム ポインターの省略を有効にする 
		"/analyze-",	//ネイティブの分析を有効にする
	};
	private static String[] cl_add_opt_x64={
		"/D\"WIN32\"",
	};
}
