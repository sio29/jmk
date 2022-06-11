package sio29.jmk.backends.vc;

import sio29.jmk.tools.*;
import sio29.jmk.parser.*;
import sio29.jmk.shellexec.*;
import sio29.jmk.cltools.*;

public class ClTools_VC implements ClToolsBase {
	private String vsdevcmd_cmd;
	private String vs_version;
	private ClToolsEnvBase env_x86;
	private ClToolsEnvBase env_x64;
	private ShellExecBase shellexec;
	//
	public ClTools_VC(ShellExecBase _shellexec) throws Exception {
//System.out.println("ClTools_VC");
		shellexec=_shellexec;
		if(!init()){
			throw new Exception("ClTools_VC init error !!");
		}
	}
	private boolean init(){
		//JmkTimerTools.initTime();
		vsdevcmd_cmd=getVsDevCmd(shellexec);
		if(vsdevcmd_cmd==null){
			return false;
		}
		vs_version=getVsVersion(shellexec);
		if(vs_version==null){
			return false;
		}
		return true;
	}
	//VisualStudioのパスを得る
	private static String getVsDevCmd(ShellExecBase shellexec){
		//installationVersion
		ShellExecReturn retdata=shellexec.execShell(new String[]{"vswhere.exe","-nologo","-latest","-property","installationPath"});
		if(retdata.return_code!=0){
			System.out.println("vswhere.exe error !!");
			return null;
		}
System.out.println("vswhere installationPath:["+retdata.text+"]");
		String vc_path=retdata.text;
		vc_path=JmkStringTools.removeCR(vc_path);
		String vc_tools_path=vc_path+"\\Common7\\Tools";
		String vsdevcmd_cmd=vc_tools_path+"\\VsDevCmd.bat";
		return vsdevcmd_cmd;
	}
	//バージョン
	private static String getVsVersion(ShellExecBase shellexec){
		ShellExecReturn retdata=shellexec.execShell(new String[]{"vswhere.exe","-nologo","-latest","-property","installationVersion"});
		if(retdata.return_code!=0){
			System.out.println("vswhere.exe error !!");
			return null;
		}
System.out.println("vswhere installationVersion:["+retdata.text+"]");
		String vc_path=retdata.text;
		vc_path=JmkStringTools.removeCR(vc_path);
		return vc_path;
	}
	//====================
	public ClToolsEnvBase getEnv(String target_type){
		if(ClTargetType_VC.isX86(target_type)){
			if(env_x86==null){
				try{
					env_x86=new ClToolsEnv_VC(shellexec,target_type,vsdevcmd_cmd,vs_version);
				}catch(Exception ex){
					return null;
				}
			}
			return env_x86;
		}else{
			if(env_x64==null){
				try{
					env_x64=new ClToolsEnv_VC(shellexec,target_type,vsdevcmd_cmd,vs_version);
				}catch(Exception ex){
					return null;
				}
			}
			return env_x64;
		}
	}
//	public int getTargetType(String target_name){
//		return ClTargetType_VC.getTargetType(target_name);
//	}
};


