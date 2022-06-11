package sio29.jmk.cltools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.common.*;

public abstract class ClParamBase{
	public String[] source_exts={"cpp","c","cc"};
	public String obj_ext="obj";
	//
	//public int output_type;
	public String output_type;
	//public int compile_type;
	public String compile_type;
	//public int target_type;
	public String target_type;
	public String src_char_code;
	public String exec_char_code;
	//
	public String proj_name;
	public String build_dir;
	public String data_dir;
	public String output_dir;
	public String output_data_dir;
	public String[] defs;
	//
	//public String[] source_dir;
	//public String[] ignore_source_dir;
	//public String[] source_files;
	//public String[] ignore_source_files;
	public JmkFileSearchParam[] source_params;
	public String[] include_dirs;
	public String[] lib_dirs;
	public String[] libs;
	//
	public String output_dir_tmp;
	public String build_dir_tmp;
	public String[] source_files_tmp;
	public String[] include_dirs_tmp;
	public String[] obj_files_tmp;
	public String[] libs_tmp;
	//
	public String[] cl_opt;
	public String[] lib_opt;
	public String[] link_opt;
	//
	public String[] update_source_files;
	public String[] update_obj_files;
	//
	//public int current_output_type;
	public String current_output_type;
	public String[] current_source_files;
	public String current_output_file;
	//=================================================
	public abstract LibParamBase createLibParam(ClToolsEnvBase tools_env);
	public abstract LinkParamBase createLinkParam(ClToolsEnvBase tools_env);
	public abstract String[] getClOpt_Post(ClToolsEnvBase tools_env,ClParamCurrent current_param);
	public void makeParam_Ext(ClToolsEnvBase tools_env){}
	public void setJmkBuildParam_Ext(JmkBuildParam jmk_param){}
	//=================================================
	/*
	public int getOutputType(){
		return output_type;
	}
	public int getTargetType(){
		return target_type;
	}
	public int getCompileType(){
		return compile_type;
	}
	*/
	public String getOutputType(){
		return output_type;
	}
	public String getTargetType(){
		return target_type;
	}
	public String getCompileType(){
		return compile_type;
	}
	//=================================================
	public void setJmkBuildParam(JmkBuildParam jmk_param){
		setJmkBuildParam(this,jmk_param);
	}
	public void makeUpdateFiles(){
		makeUpdateFiles(this);
	}
	//=================================================
	//JmkBuildParam設定
	private static void setJmkBuildParam(ClParamBase param,JmkBuildParam jmk_param){
//System.out.println("setJmkBuildParam_01");
		File base_path=JmkFileTools.getBasePath(jmk_param.getJmkFilename());
		param.output_type   =jmk_param.getOutputType();
		param.compile_type  =jmk_param.getCompileType();
		//
		param.src_char_code =jmk_param.getSourceCharCode();
		param.exec_char_code=jmk_param.getExecCharCode();
		
		param.proj_name     =jmk_param.getProjName();
		//
		param.source_params=jmk_param.getSourceParams(base_path,param.source_exts);
		//
		param.data_dir       =JmkFileTools.makePathName(base_path,jmk_param.getDataDir());
		param.build_dir      =JmkFileTools.makePathName(base_path,jmk_param.getBuildDir());
		param.output_dir     =JmkFileTools.makePathName(base_path,jmk_param.getOutputDir());
		param.output_data_dir=JmkFileTools.makePathName(base_path,jmk_param.getOutputDataDir());
		
		param.include_dirs  =jmk_param.getIncludeDirs();
		param.lib_dirs      =jmk_param.getLibDirs();
		param.libs          =jmk_param.getLibs();
		//
		param.cl_opt        =jmk_param.getClOpts();
		param.lib_opt       =jmk_param.getLibOpts();
		param.link_opt      =jmk_param.getLinkOpts();
		//
		param.defs          =jmk_param.getDefs();
//System.out.println("setJmkBuildParam_09");
		param.setJmkBuildParam_Ext(jmk_param);
	}
	//=================================================
	//パラメータ作成
	public void makeParam(ClToolsEnvBase tools_env){
//System.out.println("makeParam_01");
		makeParam(this,tools_env);
//System.out.println("makeParam_09");
	}
	private static void makeParam(ClParamBase param,ClToolsEnvBase tools_env){
		//buildディレクトリ名作成
		param.build_dir_tmp=tools_env.makeBuildDir(param.build_dir,param.compile_type,param.target_type);
		//出力ディレクトリ名作成
		if(param.output_dir!=null){
			param.output_dir_tmp=tools_env.makeBuildDir(param.output_dir,param.compile_type,param.target_type);
		}
		//======================
		//sourceファイル名作成
		makeSourceFilesTmp(param);
		//objファイル名作成
		makeObjFilesTmp(param);
		//includeディレクトリ名作成
		makeIncludeDirsTmp(param);
		//libファイル名作成
		makeLibsTmp(param);
		//======================
		//ディレクトリ作成
		JmkFileTools.createDir(param.build_dir_tmp);
		JmkFileTools.createDir(param.output_dir_tmp);
	}
	//=================================================
	//更新ファイルの作成
	private static void makeUpdateFiles(ClParamBase param){
//System.out.println("makeUpdateFiles_01");
		JmkFileLinkData fldata=null;
		JmkFileLinkCheckerList checkers=null;
		//
//System.out.println("makeUpdateFiles_02");
		JmkFileLinkChecker_CSource cs=new JmkFileLinkChecker_CSource();
		cs.addIncludePath(JmkFileTools.stringToFileList(param.include_dirs_tmp));
		checkers=new JmkFileLinkCheckerList();
		checkers.addChecker(cs);
//System.out.println("makeUpdateFiles_03:"+param.source_files_tmp.length);
		//※※※ 循環参照の可能性あり
		fldata=new JmkFileLinkData();
		int files_num=param.source_files_tmp.length;
		for(int i=0;i<files_num;i++){
			File src_file=new File(param.source_files_tmp[i]);
			File obj_file=new File(param.obj_files_tmp[i]);
//System.out.println(String.format("makeUpdateFiles_03_01:[%d/%d]:src(%s),obj(%s)",i,files_num,src_file,obj_file));
			fldata.addFile(obj_file,new File[]{src_file},checkers);
		}
//System.out.println("makeUpdateFiles_04");
		//更新が必要なソース
		ArrayList<String> update_source_files=new ArrayList<String>();
		ArrayList<String> update_obj_files=new ArrayList<String>();
		for(int i=0;i<param.source_files_tmp.length;i++){
			String src_filename=param.source_files_tmp[i];
			String obj_filename=param.obj_files_tmp[i];
			File obj_file=new File(obj_filename);
			File[] link_files=fldata.getLinkFiles(obj_file);
			if(!JmkFileTools.needToUpdate(obj_file,link_files))continue;
			update_source_files.add(src_filename);
			update_obj_files.add(obj_filename);
		}
//System.out.println("makeUpdateFiles_05");
		//
		param.update_source_files=(String[])update_source_files.toArray(new String[]{});
		param.update_obj_files=(String[])update_obj_files.toArray(new String[]{});
//System.out.println("makeUpdateFiles_09");
	}
	static class SourceAndObjName{
		String[] src_filenames;
		String[] obj_filenames;
	}
	private static SourceAndObjName getSourceFilenames(ClParamBase param,boolean use_fileupdate){
		SourceAndObjName sa=new SourceAndObjName();
		String[] src_filenames=null;
		if(use_fileupdate){
			ClParamBase.makeUpdateFiles(param);
			sa.src_filenames=param.update_source_files;
			sa.obj_filenames=param.update_obj_files;
		}else{
			sa.src_filenames=param.source_files_tmp;
			sa.obj_filenames=param.obj_files_tmp;
		}
		return sa;
	}
	//=================================================
	//ソースファイル名作成
	private static void makeSourceFilesTmp(ClParamBase param){
		param.source_files_tmp=JmkFileTools.getSourceFiles(param.source_params);
	}
	//=================================================
	//objファイル名作成
	private static void makeObjFilesTmp(ClParamBase param){
		param.obj_files_tmp=JmkFileTools.getObjFiles(param.build_dir_tmp,null,param.source_files_tmp,param.obj_ext);
	}
	//=================================================
	//includeディレクトリ名作成
	private static void makeIncludeDirsTmp(ClParamBase param){
		param.include_dirs_tmp=JmkFileTools.getIncludeDirs(param.include_dirs);
	}
	//=================================================
	//libファイル名作成
	private static void makeLibsTmp(ClParamBase param){
		param.libs_tmp=JmkFileTools.getLibs(param.libs);
	}
	
	//======================
	//ClParamBase
	public boolean execCl(ClToolsEnvBase tools_env,boolean all_flg,boolean use_fileupdate){
//System.out.println("execCl");
		makeParam(tools_env);
		makeParam_Ext(tools_env);
		if(all_flg){
		//纏めてコンパイル
			return execCl_All(tools_env);
		}else{
		//一つずつコンパイル
			return execCl_One(tools_env,use_fileupdate);
		}
	}
	//纏めてコンパイル
	public boolean execCl_All(ClToolsEnvBase tools_env){
//System.out.println("execCl_All");
		ClParamCurrent current_param=new ClParamCurrent();
		current_param.source_files=source_files_tmp;
		current_param.all_flg=true;
		String[] cl_opt=getClOpt_Post(tools_env,current_param);
		return tools_env.execCl(cl_opt,null);
	}
	//一つずつコンパイル
	public boolean execCl_One(ClToolsEnvBase tools_env,boolean use_fileupdate){
//System.out.println("execCl_One");
		SourceAndObjName sa=getSourceFilenames(this,use_fileupdate);
		boolean ret=true;
		for(int i=0;i<sa.src_filenames.length;i++){
			String src_filename=sa.src_filenames[i];
			String obj_filename=sa.obj_filenames[i];
//System.out.println(src_filename+" -> "+obj_filename);
			ClParamCurrent current_param=new ClParamCurrent();
			current_param.source_files=new String[]{src_filename};
			current_param.all_flg=false;
			current_param.output_file=obj_filename;
			String[] cl_opt=getClOpt_Post(tools_env,current_param);
			if(!tools_env.execCl(cl_opt,src_filename)){
				ret=false;
			}
		}
		return ret;
	}
}
