package sio29.jmk.tools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public class JmkFileLinkCheckerList{
	ArrayList<JmkFileLinkChecker> list=new ArrayList<JmkFileLinkChecker>();
	
	public void addChecker(JmkFileLinkChecker checker){
		list.add(checker);
	}
	public JmkFileLinkChecker getChecker(File file){
		if(file==null)return null;
		for(int i=0;i<list.size();i++){
			JmkFileLinkChecker checker=list.get(i);
			if(checker.hasFile(file))return checker;
		}
		return null;
	}
	public File[] getLinkFiles(File file){
//System.out.println("getLinkFiles_01");
		if(file==null)return null;
//System.out.println("getLinkFiles_02");
		if(!file.exists())return null;
//System.out.println("getLinkFiles_03");
		JmkFileLinkChecker checker=getChecker(file);
//System.out.println("getLinkFiles_04");
		if(checker==null)return null;
//System.out.println("getLinkFiles_05:"+checker.getClass());
		return checker.getLinkFiles(file);
	}
}
