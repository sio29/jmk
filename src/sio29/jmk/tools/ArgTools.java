//package sio29.ulib.argreader;
package sio29.jmk.tools;

public class ArgTools{
	public static void normalizeArgs(String[] args){
		if(args==null)return;
		if(args.length==0)return;
		String arg=args[args.length-1];
		if(arg==null)return;
		if(arg.length()==0)return;
		char bottom=arg.charAt(arg.length()-1);
		if(bottom==0x0d){
			arg=arg.substring(0,arg.length()-1);
			args[args.length-1]=arg;
//System.out.println("conv_args:("+arg+")");
		}
	}
}
