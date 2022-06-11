//package sio29.ulib.argreader;
package sio29.jmk.tools;

public class ArgReader{
	public interface Callback{
		public boolean argNone();
		public boolean optArg(ArgParser parser,String arg);
		public boolean paramArg(ArgParser parser,String arg);
	}
	
	public static boolean read(String[] args,Callback callback){
		ArgTools.normalizeArgs(args);
		if(args.length<1){
			return callback.argNone();
		}
		//
		ArgParser parser=new ArgParser(args);
		while(true){
			if(parser.isEnded())break;
			String now_arg=parser.get();
			if(now_arg.length()>=2){
				char m1=now_arg.charAt(0);
				if(m1=='-' || m1=='/'){
					String m2=now_arg.substring(1).toLowerCase();
					boolean r=callback.optArg(parser,m2);
					if(!r){
						System.out.println("対応外のオプションです:"+now_arg);
						return false;
					}
				}else{
					boolean r=callback.paramArg(parser,now_arg);
					if(!r){
						System.out.println("対応外のオプションです:"+now_arg);
						return false;
					}
				}
			}
		}
		return true;
	}
}
