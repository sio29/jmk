package sio29.jmk.tools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public class JmkFileTools{
	//=================================================
	//ディレクトリ作成
	public static boolean createDir(String filename){
		return createDir(new File(filename));
	}
	public static boolean createDir(File file){
		try{
			if(file.exists())return true;
			file.mkdirs();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	//=================================================
	//ファイル削除
	public static boolean deleteDir(String filename){
		return deleteDir(new File(filename));
	}
	public static boolean deleteDir(File file){
		try{
			if(!file.exists())return true;
			if(file.isDirectory()){
				File[] list=file.listFiles();
				for(int i=0;i<list.length;i++){
					File f=list[i];
					deleteDir(f);
				}
			}
			file.delete();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	//=================================================
	//ファイル削除
	public static boolean deleteFile(String filename){
		return deleteFile(new File(filename));
	}
	public static boolean deleteFile(File file){
		try{
			if(!file.exists())return true;
			file.delete();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	//=================================================
	//ファイルコピー
	public static boolean copyDir(String src_dir,String dst_dir){
		return copyDir(new File(src_dir),new File(dst_dir));
	}
	public static boolean copyDir(File src_dir,File dst_dir){
		if(!src_dir.exists())return false;
		if(!src_dir.isDirectory())return false;
		if(!dst_dir.isDirectory())return false;
		if(!dst_dir.exists()){
			dst_dir.mkdirs();
		}
		File[] src_list=src_dir.listFiles();
		for(int i=0;i<src_list.length;i++){
			File sf=src_list[i];
			String name=sf.getName();
			File df=new File(dst_dir,name);
//System.out.println("sf:"+sf+":name:"+name);
			if(sf.isDirectory()){
				if(!df.exists()){
					df.mkdir();
				}
				copyDir(sf,df);
			}else if(sf.isFile()){
				copyFile(sf,df);
			}
		}
		return false;
	}
	public static boolean copyFile(File src,File dst){
		try{
			Files.copy(src.toPath(),dst.toPath(),StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	//=================================================
	//拡張子獲得
	public static String getFileExt(String filename){
		int i=filename.lastIndexOf(".");
		return filename.substring(i+1);
	}
	public static String getFileExt(File file){
		return getFileExt(file.toString());
	}
	public static String getFileExt(Path path){
		return getFileExt(path.toString());
	}
	//=================================================
	//拡張子変更
	public static String changeFileExt(String filename,String ext){
		int i=filename.lastIndexOf(".");
		if(i>=0){
			String body=filename.substring(0,i);
			return body+"."+ext;
		}else{
			return filename+"."+ext;
		}
	}
	public static File changeFileExt(File file,String ext){
		String filename2=changeFileExt(file.toString(),ext);
		return new File(filename2);
	}
	public static Path changeFileExt(Path path,String ext){
		String filename2=changeFileExt(path.toString(),ext);
		return new File(filename2).toPath();
	}
	//=================================================
	//ファイルを探す
	public static File findFile(File[] paths,String file){
		File[] files=getFileArray(paths,file);
		if(files==null || files.length<1)return null;
		return files[0];
	}
	public static File[] getFileArray(File[] paths,String file){
		ArrayList<File> _files=new ArrayList<File>();
		for(int i=0;i<paths.length;i++){
			File path=paths[i];
			File f=new File(path,file);
			if(f.exists()){
				if(f.isFile()){
					_files.add(f);
				}
			}
		}
		return (File[])_files.toArray(new File[]{});
	}
	//=================================================
	//拡張があるか?
	public static boolean hasFileExt(String[] extlist,String ext){
		if(ext==null)return false;
		ext=ext.toLowerCase();
		for(int i=0;i<extlist.length;i++){
			String e=extlist[i].toLowerCase();
			if(ext.equals(e))return true;
		}
		return false;
	}
	//対象外のファイルか?
	public static boolean isIgnoreFile(File file,File[] ignore_files){
		if(ignore_files==null)return false;
		if(ignore_files.length==0)return false;
		//
		if(file==null)return true;
		if(!file.isFile())return true;
		for(int i=0;i<ignore_files.length;i++){
			File ignore_file=ignore_files[i];
			if(ignore_file==null)continue;
			if(!ignore_file.isFile())continue;
			if(file.equals(ignore_file))return true;
		}
		return false;
	}
	//対象外dirか?
	private static boolean isIgnoreDir(File dir,final File[] ignore_dirs){
		if(ignore_dirs==null)return false;
		if(ignore_dirs.length==0)return false;
		//
		if(dir==null)return true;
		if(!dir.isDirectory())return true;
		//
		for(int i=0;i<ignore_dirs.length;i++){
			File ignore_dir=ignore_dirs[i];
			if(ignore_dir==null)continue;
			if(!ignore_dir.isDirectory())continue;
			if(dir.equals(ignore_dir))return true;
		}
		return false;
	}
	//ディレクトリを足す
	public static void getSearchDirs(final ArrayList<File> ret_dirs,File[] dirs,final File[] ignore_dirs){
		if(dirs==null)return;
		final FilenameFilter filter=new FilenameFilter(){
			public boolean accept(File dir, String name){
				File file=new File(dir,name);
				if(file.isDirectory()){
					return false;
				}
				if(isIgnoreDir(file,ignore_dirs)){
					ret_dirs.add(file);
					return true;
				}else{
					return false;
				}
			}
		};
		for(int i=0;i<dirs.length;i++){
			File f=dirs[i];
			if(f==null)continue;
			if(!f.exists())continue;
			f.listFiles(filter);
		}
	}
	//ファイルを足す
	private static void getSearchFiles(final ArrayList<File> ret,File[] dirs,final File[] ignore_dirs,final File[] ignore_files,final String[] exts){
		if(dirs==null)return;
		if(dirs.length==0)return;
		final FilenameFilter filter=new FilenameFilter(){
			public boolean accept(File dir, String name){
				File file=new File(dir,name);
				//ディレクトリ?
				if(file.isDirectory()){
					//対象ディレクトリか?
					if(!isIgnoreDir(file,ignore_dirs)){
						//再検索
						file.listFiles(this);
					}
					return false;
				}
				//指定の拡張か?
				boolean r=false;
				String ext=getFileExt(name);
				if(hasFileExt(exts,ext)){
					if(!isIgnoreFile(file,ignore_files)){
						ret.add(file);
						r=true;
					}
				}
				return r;
			}
		};
		//
		for(int i=0;i<dirs.length;i++){
			File dir=dirs[i];
			if(dir==null)continue;
			if(!dir.exists())continue;
			if(!dir.isDirectory())continue;
			dir.listFiles(filter);
		}
	}
	private static void addFiles(final ArrayList<File> ret,File[] files){
		if(files==null)return;
		for(int i=0;i<files.length;i++){
			ret.add(files[i]);
		}
	}
	//=================================================
	//ソースファイルを得る
	private static void getSourceFiles(ArrayList<File> ret,File[] source_dir,File[] ignore_source_dir,File[] source_files,File[] ignore_source_files,String[] exts){
		//ディレクトリ
		getSearchFiles(ret,source_dir,ignore_source_dir,ignore_source_files,exts);
		//ファイル
		addFiles(ret,source_files);
	}
	public static void getSourceFiles(ArrayList<File> ret,JmkFileSearchParam param){
		getSourceFiles(ret,stringToFileList(param.dirs),stringToFileList(param.ignore_dirs),stringToFileList(param.files),stringToFileList(param.ignore_files),param.exts);
	}
	public static String[] getSourceFiles(JmkFileSearchParam param){
		ArrayList<File> ret=new ArrayList<File>();
		getSourceFiles(ret,param);
		return fileToStringList(ret);
	}
	public static String[] getSourceFiles(JmkFileSearchParam[] params){
		ArrayList<File> ret=new ArrayList<File>();
		for(int i=0;i<params.length;i++){
			getSourceFiles(ret,params[i]);
		}
		return fileToStringList(ret);
	}
	
	//=================================================
	//File -> String
	public static String[] fileToStringList(File[] list){
		if(list==null)return null;
		String[] ret=new String[list.length];
		for(int i=0;i<list.length;i++){
			File f=list[i];
			ret[i]=fileToString(f);
		}
		return ret;
	}
	public static String[] fileToStringList(ArrayList<File> list){
		if(list==null)return null;
		String[] ret=new String[list.size()];
		for(int i=0;i<list.size();i++){
			File f=list.get(i);
			ret[i]=fileToString(f);
		}
		return ret;
	}
	//String -> File
	public static File[] stringToFileList(String[] list){
		if(list==null)return null;
		File[] ret=new File[list.length];
		for(int i=0;i<list.length;i++){
			String f=list[i];
			ret[i]=stringToFile(f);
		}
		return ret;
	}
	public static File stringToFile(String s){
		if(s==null)return null;
		return new File(s);
	}
	public static String fileToString(File f){
		if(f==null)return null;
		return f.toString();
	}
	//=================================================
	//objファイル名を得る
	public static File getObjFile(File out_path,File src_path,File src_file,String obj_ext){
		String rel_filename;
		if(src_path!=null){
			Path src_rel_path=src_path.toPath().relativize(src_file.toPath());
			rel_filename=src_rel_path.toString();
			/*
			File obj_file=new File(out_path,rel_filename);
			obj_file=JmkFileTools.changeFileExt(obj_file,obj_ext);
			return obj_file;
			*/
		}else{
			rel_filename=src_file.getName();
		}
		File obj_file=new File(out_path,rel_filename);
		obj_file=JmkFileTools.changeFileExt(obj_file,obj_ext);
		return obj_file;
		
	}
	public static File[] getObjFiles(File out_path,File src_path,File[] src_files,String obj_ext){
		File[] files=new File[src_files.length];
		for(int i=0;i<src_files.length;i++){
			File src_file=src_files[i];
			File f=getObjFile(out_path,src_path,src_file,obj_ext);
			files[i]=f;
		}
		return files;
	}
	public static String[] getObjFiles(String out_path,String src_path,String[] src_files,String obj_ext){
		File[] obj_files=getObjFiles(stringToFile(out_path),stringToFile(src_path),stringToFileList(src_files),obj_ext);
		return fileToStringList(obj_files);
	}
	//=================================================
	//includeディレクトリ名作成
	public static File[] getIncludeDirsFile(String[] include_dirs){
		HashSet<File> files=new HashSet<File>();
		int num=include_dirs.length;
		for(int i=0;i<num;i++){
			String inc=include_dirs[i];
			File file=new File(inc);
			files.add(file);
		}
		return (File[])files.toArray(new File[]{});
	}
	public static String[] getIncludeDirs(String[] include_dirs){
		File[] ret=getIncludeDirsFile(include_dirs);
		return fileToStringList(ret);
	}
	//=================================================
	//libリスト作成
	//lib_dirを検索する
	public static File[] getLibsWithLibDirsFile(String[] lib_dirs,String[] libs){
		ArrayList<File> _lib_dirs=new ArrayList<File>();
		for(int i=0;i<lib_dirs.length;i++){
			File lib_dir=new File(lib_dirs[i]);
			if(lib_dir.exists() && lib_dir.isDirectory()){
				_lib_dirs.add(lib_dir);
			}else{
				//System.out.println("lib dir none !!:"+lib_dir);
			}
		}
		//
		HashSet<File> files=new HashSet<File>();
		int num=libs.length;
		for(int i=0;i<num;i++){
			String lib_filename=libs[i];
			File lib_file=new File(lib_filename);
			if(lib_file.exists() && lib_file.isFile()){
			//絶対パス
				files.add(lib_file);
			}else{
			//ディレクトリからの検索
				for(int j=0;j<_lib_dirs.size();j++){
					File lib_dir=_lib_dirs.get(j);
					File lib_file2=new File(lib_dir,lib_filename);
					if(lib_file2.exists() && lib_file2.isFile()){
						files.add(lib_file2);
					}
				}
			}
		}
		return (File[])files.toArray(new File[]{});
	}
	public static String[] getLibsWithLibDirs(String[] lib_dirs,String[] libs){
		File[] files=getLibsWithLibDirsFile(lib_dirs,libs);
		return JmkFileTools.toStringArray(files);
	}
	//=================================================
	public static File[] getLinkFilesForExe(String[] obj_files,String[] lib_dirs,String[] lib_files){
		ArrayList<File> link_files=new ArrayList<File>();
		for(int i=0;i<obj_files.length;i++){
			File obj_file=new File(obj_files[i]);
			link_files.add(obj_file);
		}
		String[] tmp_libs=JmkFileTools.getLibsWithLibDirs(lib_dirs,lib_files);
		for(int i=0;i<tmp_libs.length;i++){
			File _lib_file=new File(tmp_libs[i]);
			link_files.add(_lib_file);
		}
		return (File[])link_files.toArray(new File[]{});
	}
	//=================================================
	public static boolean needToUpdateForExe(String exe_filename_tmp,String[] obj_files,String[] lib_dirs,String[] lib_files){
		File exe_file=new File(exe_filename_tmp);
		if(!exe_file.exists())return true;
		//
		File[] _link_files=getLinkFilesForExe(obj_files,lib_dirs,lib_files);
		boolean r=JmkFileTools.needToUpdate(exe_file,_link_files);
		return r;
	}
	//=================================================
	//libリスト作成
	//単純にlibリストのみを作る
	public static String[] getLibs(String[] libs){
		HashSet<File> files=new HashSet<File>();
		int num=libs.length;
		for(int i=0;i<num;i++){
			String lib=libs[i];
			File file=new File(lib);
			files.add(file);
		}
		return JmkFileTools.toStringArray(files);
	}
	//=================================================
	public static void printFiles(File[] files){
		int num=files.length;
		for(int i=0;i<num;i++){
			File f=files[i];
			System.out.println("["+i+"]:"+f);
		}
	}
	public static void printFiles(String[] files){
		int num=files.length;
		for(int i=0;i<num;i++){
			String f=files[i];
			System.out.println("["+i+"]:"+f);
		}
	}
	public static void printFiles(List<File> files){
		int num=files.size();
		for(int i=0;i<num;i++){
			File f=files.get(i);
			System.out.println("["+i+"]:"+f);
		}
	}
	public static String[] toStringArray(File[] files){
		int num=files.length;
		String[] ret=new String[num];
		for(int i=0;i<num;i++){
			File f=files[i];
			ret[i]=f.toString();
		}
		return ret;
	}
	public static String[] toStringArray(List<File> files){
		int num=files.size();
		String[] ret=new String[num];
		for(int i=0;i<num;i++){
			File f=files.get(i);
			ret[i]=f.toString();
		}
		return ret;
	}
	public static String[] toStringArray(Set<File> files){
		int num=files.size();
		String[] ret=new String[num];
		int i=0;
		for(File f : files){
			ret[i]=f.toString();
			i++;
		}
		return ret;
	}
	//=================================================
	public static ArrayList<File> toFileArrayList(File[] files){
		ArrayList<File> files2=new ArrayList<File>();
		for(int i=0;i<files.length;i++){
			files2.add(files[i]);
		}
		return files2;
	}
	public static File[] addFileArray(File[] files,String filename){
		return addFileArray(files,new File(filename));
	}
	public static File[] addFileArray(File[] files,File file){
		ArrayList<File> files2=toFileArrayList(files);
		files2.add(file);
		return (File[])files2.toArray(new File[]{});
	}
	//=================================================
	public static String getFileBody(File f){
		return f.getName();
	}
	public static String getFileBody(String filename){
		File f=new File(filename);
		return f.getName();
	}
	public static File getBasePath(String filename){
		try{
			File file=new File(filename);
			file=file.getCanonicalFile();
			File path=file.getParentFile();
			return path;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	public static File makePath(File base_path,String filename){
		if(filename==null)return null;
		File file=new File(base_path,filename);
		if(file.exists()){
			try{
				File new_file=file.getCanonicalFile();
				file=new_file;
			}catch(Exception ex){
			}
		}
		return file;
	}
	public static String[] makePathNames(File base_path,String[] filename){
		int num=filename.length;
		String[] dst=new String[num];
		for(int i=0;i<num;i++){
			dst[i]=makePathName(base_path,filename[i]);
		}
		return dst;
	}
	public static String makePathName(File base_path,String filename){
		File file=makePath(base_path,filename);
		if(file==null)return null;
		return file.toString();
	}
	public static String addPath(String s1,String s2){
		if(s1==null || s1.length()==0)return s2;
		s1=addSeparator(s1);
		return s1+s2;
	}
	public static String getFileSeparator(){
		return File.separator;
	}
	
	public static String addSeparator(String s1){
		if(s1==null || s1.length()==0)return null;
		String separator=getFileSeparator();
		char separator_c=separator.charAt(0);
		
		int l=s1.length();
		if(s1.charAt(l-1)!=separator_c){
			s1=s1+separator;
		}
		return s1;
	}
	public static String addPathString(String s1,String s2){
		String m="";
		if(s1!=null){
			m=JmkFileTools.addPath(m,s1);
		}
		if(s2!=null){
			m=JmkFileTools.addPath(m,s2);
		}
		return m;
	}
	
	//=================================================
	//pathを得る
	public static File[] getPathArray(String path,String separator){
		String[] _paths=path.split(separator);
		ArrayList<File> _files=new ArrayList<File>();
		_files.add(new File(".").getAbsoluteFile().getParentFile());
		for(int i=0;i<_paths.length;i++){
			File f=new File(_paths[i]);
			if(f.exists()){
				if(f.isDirectory()){
					_files.add(f);
				}else{
				}
			}else{
			}
		}
		return (File[])_files.toArray(new File[]{});
	}
	//=================================================
	//env.logをmapで得る
	public static Map<String,String> readEnvLogFile(String filename){
		String separator="=";
		try{
			Path path = Paths.get(filename);
			List<String> text = Files.readAllLines(path);
			Map<String,String> map=new HashMap<String,String>();
			for(int i=0;i<text.size();i++){
				String t=text.get(i);
				String[] m=t.split(separator);
				if(m.length>=2){
					map.put(m[0],m[1]);
				}
			}
			return map;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	//=================================================
	//更新が必要か?
	public static boolean needToUpdate(File file,File[] files){
		if(file==null)return false;
		if(!file.exists()){
			//System.out.println("needToUpdate_01:file not found !!:"+file);
			return true;
		}
		if(files!=null){
			long date=file.lastModified();
			for(int i=0;i<files.length;i++){
				File f=files[i];
				if(f==null)continue;
				if(!f.exists())continue;
				if(date<f.lastModified()){
					//System.out.println("needToUpdate_02:file is old !!:"+file);
					return true;
				}
			}
		}
		return false;
	}
}
