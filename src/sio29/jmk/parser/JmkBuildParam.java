package sio29.jmk.parser;

import java.io.*;
import java.util.*;

import sio29.jmk.tools.*;

public class JmkBuildParam{
	//========================
	private JmkValueList valuelist=new JmkValueList();
	public JmkBuildParam(){
		initParamList();
	}
	public JmkValueList getValueList(){
		return valuelist;
	}
	public void initParamList(){
		initParamList(valuelist,this);
	}
	public void replaceEnv(Map<String,String> envs){
		valuelist.replaceEnv(envs);
	}
	public String toString(){
		return valuelist.toString();
	}
	//========================
	public final static String JMK_FILENAME="jmk_filename";
	public final static String ENVS="envs";
	public final static String COMPILER_TYPE="compiler_type";
	public final static String COMPILE_TYPE="compile_type";
	public final static String TARGET_TYPE="target_type";
	public final static String PROJ_NAME="proj_name";
	public final static String BUILD_DIR="build_dir";
	public final static String OUTPUT_DIR="output_dir";
	public final static String OUTPUT_TYPE="output_type";
	public final static String MAIN_CLASS="main_class";
	public final static String SOURCE_CHAR_CODE="char_code";
	public final static String EXEC_CHAR_CODE="exec_char_code";
	public final static String DATA_DIR="data_dir";
	public final static String OUTPUT_DATA_DIR="output_data_dir";
	public final static String PRE_JS="pre_js";
	public final static String JS_OPTS="js_opt";
	public final static String JS_CFUNCS="js_cfunc";
	//
	public final static String SOURCE_DIRS="source_dir";
	public final static String IGNORE_SOURCE_DIRS="ignore_source_dir";
	public final static String SOURCE_FILES="source_files";
	public final static String IGNORE_SOURCE_FILES="ignore_source_files";
	//
	public final static String SOURCE_DIRS2="source_dir2";
	public final static String IGNORE_SOURCE_DIRS2="ignore_source_dir2";
	public final static String SOURCE_FILES2="source_files2";
	public final static String IGNORE_SOURCE_FILES2="ignore_source_files2";
	//
	public final static String INCLUDE_DIRS="include_dirs";
	public final static String LIB_DIRS="lib_dirs";
	public final static String LIBS="libs";
	public final static String DEFS="defs";
	public final static String TARGETS="targets";
	public final static String PRE_BUILDS="pre_builds";
	public final static String ECHOS="echos";
	public final static String COPYS="copys";
	public final static String CL_OPT="cl_opt";
	public final static String LIB_OPT="lib_opt";
	public final static String LINK_OPT="link_opt";
	//
	//========================
	public void setJmkFilename(String n){
		valuelist.setValueByField(JMK_FILENAME,n);
	}
	public String getJmkFilename(){
		return valuelist.getValueStringByField(JMK_FILENAME);
	}
	//
	@SuppressWarnings("unchecked")
	public HashMap<String,String> getEnvs(){
		Object value=valuelist.getValueByField(ENVS);
		return (HashMap<String,String>)value;
	}
	//
	public void setCompilerType(String n){
		valuelist.setValueByField(COMPILER_TYPE,n);
	}
	public String getCompilerType(){
		return valuelist.getValueStringByField(COMPILER_TYPE);
	}
	public void setCompileType(String n){
		valuelist.setValueByField(COMPILE_TYPE,n);
	}
	public String getCompileType(){
		return valuelist.getValueStringByField(COMPILE_TYPE);
	}
	public void setTargetType(String n){
		valuelist.setValueByField(TARGET_TYPE,n);
	}
	public String getTargetType(){
		return valuelist.getValueStringByField(TARGET_TYPE);
	}
	public String getProjName(){
		return valuelist.getValueStringByField(PROJ_NAME);
	}
	public String getBuildDir(){
		return valuelist.getValueStringByField(BUILD_DIR);
	}
	public String getOutputDir(){
		return valuelist.getValueStringByField(OUTPUT_DIR);
	}
	public String getOutputType(){
		return valuelist.getValueStringByField(OUTPUT_TYPE);
	}
	public String getMainClass(){
		return valuelist.getValueStringByField(MAIN_CLASS);
	}
	public String getSourceCharCode(){
		return valuelist.getValueStringByField(SOURCE_CHAR_CODE);
	}
	public String getExecCharCode(){
		return valuelist.getValueStringByField(EXEC_CHAR_CODE);
	}
	public String getDataDir(){
		return valuelist.getValueStringByField(DATA_DIR);
	}
	public String getOutputDataDir(){
		return valuelist.getValueStringByField(OUTPUT_DATA_DIR);
	}
	public String getPreJs(){
		return valuelist.getValueStringByField(PRE_JS);
	}
	public String[] getJsOpts(){
		return valuelist.getValueStringArrayByField(JS_OPTS);
	}
	public String[] getJsCFuncs(){
		return valuelist.getValueStringArrayByField(JS_CFUNCS);
	}
	//
	public String[] getSourceDirs(){
		return valuelist.getValueStringArrayByField(SOURCE_DIRS);
	}
	public String[] getIgnoreSourceDirs(){
		return valuelist.getValueStringArrayByField(IGNORE_SOURCE_DIRS);
	}
	public String[] getSourceFiles(){
		return valuelist.getValueStringArrayByField(SOURCE_FILES);
	}
	public String[] getIgnoreSourceFiles(){
		return valuelist.getValueStringArrayByField(IGNORE_SOURCE_FILES);
	}
	//
	public boolean hasSourceDirs2(){
		String[] r=getSourceDirs2();
		if(r==null)return false;
		if(r.length==0)return false;
		return true;
	}
	public String[] getSourceDirs2(){
		return valuelist.getValueStringArrayByField(SOURCE_DIRS2);
	}
	public String[] getIgnoreSourceDirs2(){
		return valuelist.getValueStringArrayByField(IGNORE_SOURCE_DIRS2);
	}
	public String[] getSourceFiles2(){
		return valuelist.getValueStringArrayByField(SOURCE_FILES2);
	}
	public String[] getIgnoreSourceFiles2(){
		return valuelist.getValueStringArrayByField(IGNORE_SOURCE_FILES2);
	}
	//
	public String[] getIncludeDirs(){
		return valuelist.getValueStringArrayByField(INCLUDE_DIRS);
	}
	public String[] getLibDirs(){
		return valuelist.getValueStringArrayByField(LIB_DIRS);
	}
	public String[] getLibs(){
		return valuelist.getValueStringArrayByField(LIBS);
	}
	public String[] getDefs(){
		return valuelist.getValueStringArrayByField(DEFS);
	}
	public String[] getTargets(){
		return valuelist.getValueStringArrayByField(TARGETS);
	}
	public String[] getPreBuilds(){
		return valuelist.getValueStringArrayByField(PRE_BUILDS);
	}
	public String[] getEchos(){
		return valuelist.getValueStringArrayByField(ECHOS);
	}
	public String[] getClOpts(){
		return valuelist.getValueStringArrayByField(CL_OPT);
	}
	public String[] getLibOpts(){
		return valuelist.getValueStringArrayByField(LIB_OPT);
	}
	public String[] getLinkOpts(){
		return valuelist.getValueStringArrayByField(LINK_OPT);
	}
	//
	public JmkCopyPair[] getCopys(){
		return valuelist.getValueCopyArrayByField(COPYS);
	}
	
	
	public JmkFileSearchParam[] getSourceParams(File base_path,String[] exts){
		ArrayList<JmkFileSearchParam> source_params=new ArrayList<JmkFileSearchParam>();
		{
			JmkFileSearchParam source_param=new JmkFileSearchParam();;
			source_param.dirs        =JmkFileTools.makePathNames(base_path,getSourceDirs());
			source_param.ignore_dirs =JmkFileTools.makePathNames(base_path,getIgnoreSourceDirs());
			source_param.files       =JmkFileTools.makePathNames(base_path,getSourceFiles());
			source_param.ignore_files=JmkFileTools.makePathNames(base_path,getIgnoreSourceFiles());
			source_param.exts        =exts;
			source_params.add(source_param);
			//
			if(hasSourceDirs2()){
				JmkFileSearchParam source_param2=new JmkFileSearchParam();;
				source_param2.dirs        =JmkFileTools.makePathNames(base_path,getSourceDirs2());
				source_param2.ignore_dirs =JmkFileTools.makePathNames(base_path,getIgnoreSourceDirs2());
				source_param2.files       =JmkFileTools.makePathNames(base_path,getSourceFiles2());
				source_param2.ignore_files=JmkFileTools.makePathNames(base_path,getIgnoreSourceFiles2());
				source_param2.exts        =exts;
				source_params.add(source_param2);
			}
		}
		return (JmkFileSearchParam[] )source_params.toArray(new JmkFileSearchParam[]{});
	}
	//========================
	static void initParamList(final JmkValueList list,JmkBuildParam param){
		list.add(new JmkValueString("jmk_filename",JMK_FILENAME));
		list.add(new JmkValueString("compiler_type",COMPILER_TYPE));
		list.add(new JmkValueString("proj_name",PROJ_NAME));
		list.add(new JmkValueString("build_dir",BUILD_DIR));
		list.add(new JmkValueString("output_dir",OUTPUT_DIR));
		list.add(new JmkValueString("output_type",OUTPUT_TYPE));
		list.add(new JmkValueString("target_type",TARGET_TYPE));
		list.add(new JmkValueString("compile_type",COMPILE_TYPE));
		list.add(new JmkValueString("main_class",MAIN_CLASS));
		list.add(new JmkValueString("char_code",SOURCE_CHAR_CODE));
		list.add(new JmkValueString("exec_char_code",EXEC_CHAR_CODE));
		list.add(new JmkValueString("data_dir",DATA_DIR));
		list.add(new JmkValueString("output_data_dir",OUTPUT_DATA_DIR));
		list.add(new JmkValueString("pre_js",PRE_JS));
		list.add(new JmkValueStringArray("js_opt",JS_OPTS));
		list.add(new JmkValueStringArray("js_cfunc",JS_CFUNCS));
		//
		list.add(new JmkValueStringArray("source_dir"        ,SOURCE_DIRS));
		list.add(new JmkValueStringArray("ignore_source_dir" ,IGNORE_SOURCE_DIRS));
		list.add(new JmkValueStringArray("source"            ,SOURCE_FILES));
		list.add(new JmkValueStringArray("ignore_source"     ,IGNORE_SOURCE_FILES));
		//
		list.add(new JmkValueStringArray("source_dir2"       ,SOURCE_DIRS2));
		list.add(new JmkValueStringArray("ignore_source_dir2",IGNORE_SOURCE_DIRS2));
		list.add(new JmkValueStringArray("source2"           ,SOURCE_FILES2));
		list.add(new JmkValueStringArray("ignore_source2"    ,IGNORE_SOURCE_FILES2));
		//
		list.add(new JmkValueStringArray("cl_opt"  ,CL_OPT));
		list.add(new JmkValueStringArray("lib_opt" ,LIB_OPT));
		list.add(new JmkValueStringArray("link_opt",LINK_OPT));
		//
		list.add(new JmkValueStringArray("target","targets"));
		list.add(new JmkValueStringArray("cc_def","cc_defs"));
		list.add(new JmkValueStringArray("lib_def","lib_defs"));
		list.add(new JmkValueStringArray("link_def","link_defs"));
		list.add(new JmkValueStringArray("cc_undef","cc_undefs"));
		list.add(new JmkValueStringArray("lib_undef","lib_undefs"));
		list.add(new JmkValueStringArray("link_undef","link_undefs"));
		list.add(new JmkValueStringArray("include_dir","include_dirs"));
		list.add(new JmkValueStringArray("lib_dir","lib_dirs"));
		list.add(new JmkValueStringArray("lib",LIBS));	//@
		list.add(new JmkValueStringArray("echo","echos"));
		list.add(new JmkValueStringArray("pre_build","pre_builds"));
		//
		list.add(new JmkValueStringArray("def","defs"));
		//
		list.add(new JmkValueCopyArray("copy","copys"));
		list.add(new JmkValueMapStrStr("set","envs"));
		list.add(new JmkValueInt("debug","debug"){
			public void setValue(Integer n){
				String m=null;
				if(n==0){
					m="release";
				}else{
					m="debug";
				}
				list.setValueByField(COMPILE_TYPE,m);
			}
			public void replaceEnv(Map<String,String> envs){
			}
		});
	}
}
