package sio29.jmk.cltools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

import sio29.jmk.tools.*;
//import sio29.jmk.cltools.*;

public class JmkFileLinkChecker_CSource implements JmkFileLinkChecker{
	final static String[] extlist={
		"c","cpp","cc",
		"h","hpp"
	};
	ArrayList<File> include_paths=new ArrayList<File>();
	
	public void clearIncludePath(){
		include_paths.clear();
	}
	public void addIncludePath(File include_path){
		include_paths.add(include_path);
	}
	public void addIncludePath(File[] _include_paths){
		for(int i=0;i<_include_paths.length;i++){
			include_paths.add(_include_paths[i]);
		}
	}
	
	//このチェッカーを使う
	public boolean hasFile(File file){
		String ext=JmkFileTools.getFileExt(file);
		if(ext==null)return false;
		return JmkFileTools.hasFileExt(extlist,ext);
	}
	//関連するファイルリストを返す
	public File[] getLinkFiles(File file){
//System.out.println("JmkFileLinkChecker_CSource::getLinkFiles_01:"+file);
		File[] _include_paths=(File[])include_paths.toArray(new File[]{});
//System.out.println("JmkFileLinkChecker_CSource::getLinkFiles_02");
		File[] incs=CSourceIncludeCollector.getIncludeList(file,_include_paths);
//System.out.println("JmkFileLinkChecker_CSource::getLinkFiles_03");
		return incs;
	}
	/*
	public boolean needToUpdate(File file){
		File[] files=getLinkFiles(file);
		return JmkFileLinkData.needToUpdate(file,files);
	}
	*/
}

