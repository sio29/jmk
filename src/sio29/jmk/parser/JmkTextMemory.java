/******************************************************************************
;	MQOTextMemory
******************************************************************************/
package sio29.jmk.parser;

import java.io.*;
//import sio29.ulib.ufile.UFile;

public class JmkTextMemory{
	private byte[] buff=null;
	private int buff_now_adr=0;
	private File url=null;
	private int mem_type=0;
	
	public JmkTextMemory(){
		buff=null;
		buff_now_adr=0;
		url=null;
		mem_type=0;
	}
	public void Free(){
		if(mem_type==0){
			buff=null;
		}
	}
	//ファイルから読み込み
	public static byte[] bload_malloc(File file){
		try{
			/*
			if(!file.isFile()){
				log_println(file+" は存在しません!!\n");
				return null;
			}
			*/
			long len=file.length();
			if(len<4)return null;
			FileInputStream is=new FileInputStream(file);	//※gdxエラー
			byte[] adr=new byte[(int)len];
			is.read(adr,0,(int)len);
			is.close();
			return adr;
		} catch (Exception e) {
			e.printStackTrace();
			//log_println(file+" の読み込みに失敗しました!!");
			return null;
		}
	}
	public boolean LoadFromFile(File _url){
		if(_url==null)return false;
		buff=bload_malloc(_url);
		if(buff==null)return false;
		url=_url;
		return true;
	}
	public File GetFile(){
		return url;
	}
	public boolean Eof(){
		if(buff_now_adr>=buff.length)return true;
		return false;
	}
	public byte[] GetMem(int size){
		byte[] data=new byte[size];
		for(int i=0;i<size;i++){
			data[i]=GetUint8();
		}
		return data;
	}
	public void Skip(int n){
		buff_now_adr+=n;
	}
	public byte GetUint8(){
		if(Eof())return 0;
		byte n=buff[buff_now_adr];
		buff_now_adr++;
		return n;
	}
	public int getPointer(){
		return buff_now_adr;
	}
	public void setPointer(int p){
		buff_now_adr=p;
	}
}



