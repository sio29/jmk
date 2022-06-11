package sio29.jmk.tools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public class JmkFileLinkData{
	public static class FileLinker{
		public File file;
		public HashSet<File> list=new HashSet<File>();
		public boolean check_flg=false;					//�z�Q�Ƌ֎~�p
		public boolean done_flg=false;					//�ݒ�ς�
		//
		public FileLinker(){}
		public FileLinker(File _file){
			file=_file;
		}
		public FileLinker(File _file,File[] _list){
			file=_file;
			for(int i=0;i<_list.length;i++){
				list.add(_list[i]);
			}
			done_flg=true;
		}
		public void addLink(File _file){
			list.add(_file);
		}
		public void setLinks(File[] _files){
			if(_files==null)return;
			for(int i=0;i<_files.length;i++){
				File f=_files[i];
				if(f==null)continue;
				list.add(f);
			}
		}
		public File[] getLinkList(){
			return (File[])list.toArray(new File[]{});
		}
	}
	private HashMap<File,FileLinker> map=new HashMap<File,FileLinker>();
	
	private void clearCheckFlg(){
		for(FileLinker fl : map.values()){
			fl.check_flg=false;
		}
	}
	//�N���A
	public void clear(){
		map.clear();
	}
	//
	public void addFile(File file,File[] link_files,JmkFileLinkCheckerList checkers){
		if(file==null)return;
		if(link_files==null)return;
		if(link_files.length==0)return;
		//���Ƀ`�F�b�N�ς�?
		FileLinker fl=map.get(file);
		if(fl!=null)return;
		fl=new FileLinker(file,link_files);
		map.put(file,fl);
		//
		//�`�F�b�J�[������
		if(checkers!=null){
			//�}�b�v�֓o�^
			if(link_files!=null){
				for(int i=0;i<link_files.length;i++){
					File link_file=link_files[i];
					addFile(link_file,checkers);
				}
			}
		}
	}
	//
	public void addFile(File file,JmkFileLinkCheckerList checkers){
//System.out.println("addFile:"+file);
		if(file==null)return;
		//�t�@�C��������
		if(file.exists()){
			//��΃p�X��
			try{
				file=file.getCanonicalFile();
			}catch(Exception ex){
				ex.printStackTrace();
				return;
			}
		}else{
		//�t�@�C�����Ȃ�
			return;
		}
//System.out.println("addFile_02");
		//���Ƀ`�F�b�N�ς�?
		FileLinker fl=map.get(file);
		if(fl==null){
			fl=new FileLinker(file);
			map.put(file,fl);
		}
//System.out.println("addFile_03");
		//���Ɍ����ς�
		if(fl.done_flg)return;
//System.out.println("addFile_04");
		//�`�F�b�J�[������
		if(checkers!=null){
//System.out.println("addFile_05:"+file);
			//�֘A�t�@�C���l��
			File[] link_files=checkers.getLinkFiles(file);
//System.out.println("addFile_06");
			if(link_files!=null){
				fl.setLinks(link_files);
			}
//System.out.println("addFile_07");
			//�����ς݂�
			fl.done_flg=true;
//System.out.println("addFile_08");
			//�}�b�v�֓o�^
			if(link_files!=null){
//System.out.println("addFile_09");
				for(int i=0;i<link_files.length;i++){
					File lf=link_files[i];
					//addFile(lf,null);
					addFile(lf,checkers);
				}
//System.out.println("addFile_10");
			}
		}
	}
	public void addFiles(File[] files,JmkFileLinkCheckerList checkers){
		for(int i=0;i<files.length;i++){
			File f=files[i];
			addFile(f,checkers);
		}
	}
	public File[] getLinkFiles(){
		File[] files=(File[])map.keySet().toArray(new File[]{});
		Arrays.sort(files);
		return files;
	}
	
	
	
	private void getLinkFiles(HashSet<File> files,File file){
		FileLinker fl=map.get(file);
		if(fl==null)return;
		if(fl.check_flg)return;
		fl.check_flg=true;
		for(File f : fl.list){
			if(f==null)continue;
			files.add(f);
			getLinkFiles(files,f);
		}
	}
	//�֘A�t�@�C���̈ꗗ�𓾂�
	public File[] getLinkFiles(File src_file){
		clearCheckFlg();
		HashSet<File> files=new HashSet<File>();
		getLinkFiles(files,src_file);
		//
		File[] _files=(File[])files.toArray(new File[]{});
		Arrays.sort(_files);
		return _files;
	}
	public File[] getLinkFiles(File[] src_files){
		clearCheckFlg();
		HashSet<File> files=new HashSet<File>();
		for(int i=0;i<src_files.length;i++){
			getLinkFiles(files,src_files[i]);
		}
		//
		File[] _files=(File[])files.toArray(new File[]{});
		Arrays.sort(_files);
		return _files;
	}
	
	public boolean needToUpdate(File obj_file){
		File[] link_files=getLinkFiles(obj_file);
		return JmkFileTools.needToUpdate(obj_file,link_files);
	}
}
