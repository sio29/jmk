package sio29.jmk.shellexec.backends.dosexec;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.shellexec.*;
import sio29.jmk.tools.*;

public class JmkDosExec{
	//================================
	public static File[] getPathArray(){
		String path=getPaths();
		return JmkFileTools.getPathArray(path,";");
	}
	//
	public static ShellExecReturn execShell(String[] args){
		return execShell(args,null);
	}
	public static ShellExecReturn execShell(String[] args,ShellExecParam param){
		ShellExecReturn retdata=new ShellExecReturn();
		String text=null;
		try {
			ProcessBuilder pb=new ProcessBuilder(args);
			if(param!=null && param.current_dir!=null){
				pb.directory(param.current_dir);
			}
			Map<String,String> env=pb.environment();
			if(param!=null && param.in_env!=null){
				env.clear();
				for(String key : param.in_env.keySet()){
					env.put(key,param.in_env.get(key));
				}
			}
			
			Process process = pb.start();
			InputStream is = process.getInputStream();
			InputStream es = process.getErrorStream();
			
			//プロセス実行側での文字列等の出力によっては、
			//文字コードが一致しないと、受け取る際に文字化けを起こす
			InputStreamReader isr = new InputStreamReader(is, "Shift-JIS");
			InputStreamReader esr = new InputStreamReader(es, "Shift-JIS");
			//InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader reader_is = new BufferedReader(isr);
			BufferedReader reader_es = new BufferedReader(esr);
			StringBuilder builder_is = new StringBuilder();
			StringBuilder builder_es = new StringBuilder();
			int c;
			while ((c = reader_is.read()) != -1) {
				builder_is.append((char) c);
			}
			while ((c = reader_es.read()) != -1) {
				builder_es.append((char) c);
			}
			// コンソール出力される文字列の格納
			retdata.text = builder_is.toString();
			retdata.text += builder_es.toString();
			// 終了コードの格納(0:正常終了 1:異常終了)
			int ret = process.waitFor();
			retdata.return_code=ret;
			retdata.out_env=new HashMap<String,String>();
			env=pb.environment();
			for(String key : env.keySet()){
				retdata.out_env.put(key,env.get(key));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return retdata;
	}
	//=================================================
	// ￥を￥￥に
	public static String convertYen(String m){
		m=m.replace("\\","\\\\");
		return m;
	}
	public static boolean execCmd(File cmd,String[] opt){
		return execCmd(cmd.toString(),opt,null);
	}
	public static boolean execCmd(File cmd,String[] opt,ShellExecParam param){
		return execCmd(cmd.toString(),opt,param);
	}
	public static boolean execCmd(String cmd,String[] opt){
		return execCmd(cmd,opt,null);
	}
	public static boolean execCmd(String cmd,String[] opt,ShellExecParam param){
		ArrayList<String> args=new ArrayList<String>();
		args.add(cmd);
		if(opt!=null){
			for(int i=0;i<opt.length;i++){
				String m=convertYen(opt[i]);
				args.add(m);
			}
		}
		ShellExecReturn retdata=execShell((String[])args.toArray(new String[]{}),param);
		String ret=retdata.text;
		if(param!=null && param.print_flg)System.out.println(""+ret);
		if(retdata.return_code!=0){
			return false;
		}
		return true;
	}
	public static String getPaths(){
		Map<String,String> envmap=System.getenv();
		return envmap.get("Path");
	}
	public static String getEnv(String env){
		Map<String,String> envmap=System.getenv();
		return envmap.get(env);
	}
	public static String getPathSeparator(){
		return ";";
	}
}
