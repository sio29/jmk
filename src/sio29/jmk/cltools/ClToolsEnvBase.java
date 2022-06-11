package sio29.jmk.cltools;

import sio29.jmk.parser.*;

public interface ClToolsEnvBase{
	public boolean execCl(String[] opt,String src_filename);
	public boolean execLib(String[] opt);
	public boolean execLink(String[] opt);
	public String makeBuildDir(String build_dir,String compile_type,String target_type);
	public ClParamBase createClParam(JmkBuildParam jmk_param,String target_type);
	public boolean execEnv(JmkBuildParam jmk_param,String target_type);
}
