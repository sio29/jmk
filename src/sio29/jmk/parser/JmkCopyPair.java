package sio29.jmk.parser;

public class JmkCopyPair{
	public String src;
	public String dst;
	//
	public JmkCopyPair(){
	}
	public JmkCopyPair(String _src,String _dst){
		src=_src;
		dst=_dst;
	}
	public String toString(){
		return String.format("copy: %s -> %s",src,dst);
	}
};
