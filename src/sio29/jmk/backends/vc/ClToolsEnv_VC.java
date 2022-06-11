package sio29.jmk.backends.vc;

import java.util.*;
import java.io.*;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClToolsEnv_VC extends ClToolsEnvDefault{
	private final static String env_log_filename="__env__.txt";
	
	public ClToolsEnv_VC(ShellExecBase _shellexec,String target_type,String vsdevcmd_cmd,String vs_version) throws Exception {
		shellexec=_shellexec;
		if(!init(target_type,vsdevcmd_cmd,vs_version)){
			throw new Exception("ClTools_VC.Env init error !!");
		}
	}
	private boolean init(String target_type,String vsdevcmd_cmd,String vs_version){
		if(!hasReg(target_type,vsdevcmd_cmd,vs_version)){
			return initByVsCmd(target_type,vsdevcmd_cmd,vs_version);
		}else{
			return initByReg(target_type,vsdevcmd_cmd,vs_version);
		}
	}
	//レジストリがあるか
	private boolean hasReg(String target_type,String vsdevcmd_cmd,String vs_version){
		return false;
	}
	//レジストリから得る
	private boolean initByReg(String target_type,String vsdevcmd_cmd,String vs_version){
		return false;
	}
	//VSコマンドから得る
	private boolean initByVsCmd(String target_type,String vsdevcmd_cmd,String vs_version){
		//環境の設定
		new_env=getVsDevCmdEnv(shellexec,vsdevcmd_cmd,target_type);
//JmkStringTools.printHashMap(new_env);
		//パスを得る
		new_env=JmkStringTools.toLowerCaseMap(new_env);
		String path=new_env.get("path");
		String path_separator=shellexec.getPathSeparator();
		File[] paths=JmkFileTools.getPathArray(path,path_separator);
		
		//cl.exeのパスを得る
		cl_file  =JmkFileTools.findFile(paths,"cl.exe");
		lib_file =JmkFileTools.findFile(paths,"lib.exe");
		link_file=JmkFileTools.findFile(paths,"link.exe");
		//
		boolean r=true;
		if(cl_file==null){
			System.out.println("cl.exe not fond !!");
			r=false;
		}
		if(lib_file==null){
			System.out.println("lib.exe not fond !!");
			r=false;
		}
		if(link_file==null){
			System.out.println("link.exe not fond !!");
			r=false;
		}
//System.out.println("cl_file:"+cl_file);
//System.out.println("lib_file:"+lib_file);
//System.out.println("link_file:"+link_file);
		return r;
	}
	private static Map<String,String> getVsDevCmdEnv(ShellExecBase shellexec,String vsdevcmd_cmd,String target_type){
		ArrayList<String> cmds=new ArrayList<String>();
		cmds.add("cmd.exe");
		cmds.add("/c");
		cmds.add(vsdevcmd_cmd);
		if(ClTargetType_VC.isX64(target_type)){
			cmds.add("-arch=amd64");
		}else{
			cmds.add("-arch=x86");
		}
		cmds.add("&");
		cmds.add("set");
		cmds.add(">");
		cmds.add(env_log_filename);
//System.out.println(JmkStringTools.getConnectString(cmds," "));
		ShellExecReturn retdata=shellexec.execShell((String[])cmds.toArray(new String[]{}));
		if(retdata.return_code!=0){
			System.out.println("error !!:"+JmkStringTools.getConnectString(cmds," "));
			return null;
		}
		String vsdevcmd_ret=retdata.text;
//System.out.println("retdata.text:\n"+retdata.text);
		Map<String,String> new_env=JmkFileTools.readEnvLogFile(env_log_filename);
		JmkFileTools.deleteFile(env_log_filename);
		return new_env;
	}
	public String makeBuildDir(String build_dir,String compile_type,String target_type){
		String m="";
		if(build_dir!=null){
			m=JmkFileTools.addPath(m,build_dir);
		}
		if(ClTargetType_VC.isX86(target_type)){
			m=JmkFileTools.addPath(m,"x86\\");
		}else{
			m=JmkFileTools.addPath(m,"x64\\");
		}
		if(ClCompileType.isDebug(compile_type)){
			m=JmkFileTools.addPath(m,"Debug\\");
		}else{
			m=JmkFileTools.addPath(m,"Release\\");
		}
		return m;
	}
	public ClParamBase createClParam(JmkBuildParam jmk_param,String target_type){
		return new ClParam_VC(jmk_param,target_type,this);
	}
	public boolean execEnv(JmkBuildParam jmk_param,String target_type){
		return execEnv_ClAndLink(jmk_param,target_type);
	}
}


