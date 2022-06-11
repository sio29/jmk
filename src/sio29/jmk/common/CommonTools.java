package sio29.jmk.common;

import java.io.*;
import java.nio.file.*;

import sio29.jmk.parser.*;

public class CommonTools{
	public static boolean execCopy(JmkCopyPair[] copys,boolean update_flg){
//System.out.println("CommonTools.execCopy ***");
		if(copys==null)return true;
		boolean r=true;
		//FileSystems.getDefault();
		for(int i=0;i<copys.length;i++){
			JmkCopyPair pair=copys[i];
			//System.out.println(""+pair);
			File src_file=new File(pair.src);
			Path src=src_file.toPath();
			Path filename=src.getFileName();
			File dst_dir=new File(pair.dst);
			File dst_file=new File(dst_dir,filename.toString());
			Path dst=dst_file.toPath();
			if(!src_file.exists()){
				r=false;
				System.out.println("file not found !!: "+src);
				continue;
			}
			if(dst_file.exists()){
				if(update_flg){
					long src_time=src_file.lastModified();
					long dst_time=dst_file.lastModified();
					if(dst_time>=src_time){
//System.out.println("update none : "+src+" -> "+dst);
						continue;
					}
				}
			}
			if(!dst_dir.exists()){
				try{
					dst_dir.mkdirs();
				}catch(Exception ex){
					r=false;
					System.out.println("mkdirs failed !!:"+dst_dir);
					continue;
				}
			}
			
			
			
			//Files.copy(src,dst);
//System.out.println("src: "+src);
//System.out.println("dst: "+dst);
			//System.out.println("filename:"+src.getFileName());
			try{
System.out.println("copy: "+src+" -> "+dst);
				Files.copy(src,dst,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);
			}catch(Exception ex){
				r=false;
				System.out.println("copy failed !!:"+pair);
			}
			
		}
		return r;
	}
	public static boolean execCopy(JmkBuildParam param,boolean update_flg){
		JmkCopyPair[] copys=param.getCopys();
		return CommonTools.execCopy(copys,update_flg);
	}
}

