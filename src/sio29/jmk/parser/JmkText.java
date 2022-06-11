/******************************************************************************
;
******************************************************************************/
package sio29.jmk.parser;
//
//import sio29.ulib.umat.FVECTOR;
//import sio29.ulib.umat.FVECTOR2;
//import sio29.ulib.umat.FVECTOR4;
//import sio29.ulib.umat.CVECTOR;
//import sio29.ulib.umat.QUAT;
//import sio29.ulib.ufile.UFile;

public class JmkText extends JmkTextMemory{
	static class StringTmp{
		private byte[] buff=new byte[1024];
		private int index=0;
		public void Add(char mm){
			if(index>=buff.length){
				System.out.println("StringTmp.Add Error!!");
				return;
			}
			buff[index]=(byte)mm;
			index++;
		}
		public String GetString(){
			if(index==0)return "";
			return new String(buff,0,index);
		}
	}
	//===========================
	private int linenum;
	//
	public JmkText(){
		linenum=1;
	}
	public int GetLineNum(){
		return linenum;
	}
	//EOFかチェックする
	public boolean CheckEOF(){
		if(Eof())return true;
		return false;
	}
	//バイナリとしてサイズ分得る
	public byte[] FGetMem(int size){
		return GetMem(size);
	}
	//一文字戻る
	public void FGetDec(){
		Skip(-1);
		byte mm=GetUint8();
		if(mm==0x0a)linenum--;
		Skip(-1);
	}
	//一文字える
	public char FGetChar(){
		if(CheckEOF())return 0;
		char mm=(char)GetUint8();
		if(mm==0x0a)linenum++;
		return mm;
	}
	//チャンクの先頭を得る
	public char FGetChunkTop(){
		char mm;
		while(true){
			if(CheckEOF())return 0;
			mm=FGetChar();
			if(mm==0x20 ||
			   mm==0x09 ||
			   mm==0x0d ||
			   mm==0x0a){
				continue;
			}
			break;
		}
		if(CheckComment(mm)){
			if(CheckEOF())return 0;
			return FGetChunkTop();
		}
		//
		return mm;
	}
	//コメントチェック
	public boolean CheckComment(char mm){
		if(!CheckEOF()){
			if(mm=='/'){
				char mm2=FGetChar();
				if(mm2=='/'){
					if(!FGetNextLine())return true;
					return true;
				}else if(mm2=='*'){
					if(!FGetCommentEnd())return true;
					return true;
				}
			}
		}
		return false;
	}
	//チャンクを得る
	public String FGetChunk(){
		StringTmp stmp=new StringTmp();
		char mm=FGetChunkTop();
		if(mm==0)return null;
		stmp.Add(mm);
		while(true){
			if(CheckEOF())break;
			mm=(char)GetUint8();
			if(mm==0 ||
			   mm==0x09 ||
			   mm==0x20 ||
			   mm==0x0d ||
			   mm==0x0a ||
			   mm=='{' ||
			   mm=='}' ||
			   mm=='(' ||
			   mm==')' ||
			   mm==',' ||
			   mm==0x1a){
			   	break;
			}
			stmp.Add(mm);
		}
		FGetDec();
		String m=stmp.GetString();
		return m;
	}
	//サブチャンクの先頭を得る
	public boolean FGetSubChunkTop(){
		char mm;
		while(true){
			if(CheckEOF())return false;
			mm=FGetChar();
			if(mm==0x20 ||
			   mm==0x09)continue;
			break;
		}
		FGetDec();
		return true;
	}
	//サブチャンクを得る
	public String FGetSubChunk(){
		StringTmp stmp=new StringTmp();
		if(!FGetSubChunkTop())return null;
		while(true){
			if(CheckEOF())break;
			char mm=FGetChar();
			if(mm==0x09 || 
			   mm==0x20){
				break;
			}
			if(mm==0 ||
			   mm==0x0d ||
			   mm==0x0a ||
			   mm=='{' ||
			   mm=='}' ||
			   mm=='(' ||
			   mm==')' ||
			   mm==',' ||
			   mm==0x1a){
				break;
			}
			stmp.Add(mm);
		}
		FGetDec();
		String m=stmp.GetString();
		return m;
	}
	//一行分のデータを得る
	public String FGetLine(){
		StringTmp stmp=new StringTmp();
		char mm;
		while(true){
			if(CheckEOF())break;
			mm=FGetChar();
			if(mm==0x1a ||
			   mm==0x00)break;
			stmp.Add(mm);
			if(mm==0x0a){
				break;
			}
		}
		String m=stmp.GetString();
		return m;
	}
	//一文字得る(空白、TAB、改行はスキップ)
	public char FGetOne(){
		char mm=0;
		while(true){
			if(CheckEOF())break;
			mm=FGetChar();
			switch(mm){
				case 0x20:	//空白
				case 0x09:	//TAB
				case 0x0d:	//改行
				case 0x0a:	//改行
					continue;
			}
			break;
		}
		return mm;
	}
	//一文字得る(スキップ処理はしない)
	public char FGetOne2(){
		char mm=FGetChar();
		return mm;
	}
	//一文字得る(空白、TABはスキップ)
	public char FGetOne3(){
		char mm=0;
		while(true){
			if(CheckEOF())break;
			mm=FGetChar();
			switch(mm){
				case 0x20:	//空白
				case 0x09:	//TAB
					continue;
			}
			break;
		}
		return mm;
	}
	//
	public boolean CheckJmkStr(){
		int m=FGetOne();
		FGetDec();
		if(m!=0x22)return false;
		return true;
	}
	
	//ダフルクォーテーションで囲まれた文字列を獲得する
	public String FGetJmkStr(){
		char mm=FGetChunkTop();
		if(mm==0)return null;
		if(mm!=0x22){
			System.out.println("["+mm+"]\"が見つかりません");
			return null;
		}
		StringTmp stmp=new StringTmp();
		while(true){
			if(CheckEOF())return null;
			mm=FGetChar();
			if(mm==0 ||
			   mm==0x0d ||
			   mm==0x0a ||
			   mm==0x1a){
				FGetDec();
				return null;
			}
			if(mm==0x22)break;
			stmp.Add(mm);
		}
		String m=stmp.GetString();
		return m;
	}
	//文字列を獲得する
	public String FGetStr(){
		char mm=FGetChunkTop();
		if(mm==0)return null;
		FGetDec();
		StringTmp stmp=new StringTmp();
		while(true){
			if(CheckEOF())return null;
			mm=FGetChar();
			if(mm==0 ||
			   mm==0x0d ||
			   mm==0x0a ||
			   mm==0x20 ||
			   mm==0x1a){
				FGetDec();
				break;
			}
			stmp.Add(mm);
		}
		String m=stmp.GetString();
		return m;
	}
	
	//次の行までスキップする
	public boolean FGetNextLine(){
//System.out.println("FGetNextLine--------------------------------");
		char mm;
		while(true){
			if(CheckEOF())return false;
			mm=FGetChar();
//System.out.println(UString.format("FGetNextLine:%c,%02x",mm,(int)mm & 0xff));
			if(mm==0 ||
			   mm==0x1a ||
			   mm==0x0a){
				break;
			}
		}
		return true;
	}
	//コメントの最後までスキップする
	public boolean FGetCommentEnd(){
		char mm;
		while(true){
			if(CheckEOF())return false;
			mm=FGetChar();
			if(mm=='*'){
				if(CheckEOF())return false;
				char mm2=FGetChar();
				if(mm2=='/')break;
			}
		}
		return true;
	}
	//intデータとして獲得する
	public int FGetInt(){
		StringTmp stmp=new StringTmp();
		char mm=FGetOne();
		if(CheckEOF())return 0;
		if(mm=='+' || mm=='-'){
			stmp.Add(mm);
		}else{
			FGetDec();
		}
		while(true){
			if(CheckEOF())return 0;
			mm=FGetChar();
			if(!(mm>='0' && mm<='9'))break;
			stmp.Add(mm);
		}
		FGetDec();
		String m=stmp.GetString();
		int n=0;
		try{
			n=Integer.parseInt(m);
		}catch(Exception ex){
			/*
			long n2=0;
			try{
				n2=Long.parseLong(m);
				if(n2>Integer.MAX_VALUE){	//※TeaVMはここでエラー
				}
			}catch(Exception ex2){
				System.out.println("FGetInt Error!!:"+m);
			}
			*/
		}
		return n;
	}
	//longデータとして獲得する
	public long FGetLong(){
		StringTmp stmp=new StringTmp();
		char mm=FGetOne();
		if(CheckEOF())return 0;
		if(mm=='+' || mm=='-'){
			stmp.Add(mm);
		}else{
			FGetDec();
		}
		while(true){
			if(CheckEOF())return 0;
			mm=FGetChar();
			if(!(mm>='0' && mm<='9'))break;
			stmp.Add(mm);
		}
		FGetDec();
		String m=stmp.GetString();
		long n=0;
		try{
			n=Long.parseLong(m);
		}catch(Exception ex){
			System.out.println("FGetLong Error!!:"+m);
		}
		return n;
	}
	public boolean FGetBOOL(){
		int n=FGetInt();
		return (n!=0);
	}
	//uintデータとして獲得する
	public long FGetUInt(){
		StringTmp stmp=new StringTmp();
		char mm=FGetOne();
		if(CheckEOF())return 0;
		FGetDec();
		while(true){
			if(CheckEOF())return 0;
			mm=FGetChar();
			if(!(mm>='0' && mm<='9'))break;
			stmp.Add(mm);
		}
		FGetDec();
		String m=stmp.GetString();
		long n=0;
		try{
			n=Long.parseLong(m);
		}catch(Exception ex){
			System.out.println("FGetUInt Error!!:"+m);
		}
		return n;
	}
	//intデータとして獲得する
	public boolean FGetBool(){
		if(FGetInt()!=0){
			return true;
		}else{
			return false;
		}
	}
	//floatとして獲得する
	public float FGetFloat(){
		StringTmp stmp=new StringTmp();
		char mm=FGetOne();
		if(CheckEOF())return 0.0f;
		if(mm=='+' || mm=='-'){
			stmp.Add(mm);
		}else{
			FGetDec();
		}
		while(true){
			if(CheckEOF())return 0.0f;
			mm=FGetChar();
			if(!((mm>='0' & mm<='9') || mm=='.' || mm=='e' || mm=='E' || mm=='-' || mm=='+'))break;
			stmp.Add(mm);
		}
		FGetDec();
		String m=stmp.GetString();
		float n=0.0f;
		try{
			n=Float.parseFloat(m);
		}catch(Exception ex){
			System.out.println("Float.parseFloat Error!!:「"+m+"」");
			n=0.0f;
		}
		return n;
	}
	
	/*
	//Uint32色データとして獲得する
	public CVECTOR FGetIntCol(){
		CVECTOR col2=new CVECTOR();
		long col=FGetUInt();
		col2.a=(byte)((col >> 24) & 0xff);
		col2.b=(byte)((col >> 16) & 0xff);
		col2.g=(byte)((col >>  8) & 0xff);
		col2.r=(byte)((col      ) & 0xff);
		return col2;
	}
	//float色データとして獲得する
	public FVECTOR4 FGetFloatCol(){
		FVECTOR4 col2=new FVECTOR4();
		long col=FGetLong();
		col2.w=(float)((col >> 24) & 0xff)/255.0f;
		col2.z=(float)((col >> 16) & 0xff)/255.0f;
		col2.y=(float)((col >>  8) & 0xff)/255.0f;
		col2.x=(float)((col      ) & 0xff)/255.0f;
		return col2;
	}
	public CVECTOR FGetRGB(){
		CVECTOR c=new CVECTOR();
		c.r=(byte)FGetInt();
		c.g=(byte)FGetInt();
		c.b=(byte)FGetInt();
		c.a=(byte)255;
		return c;
	}
	public CVECTOR FGetRGBA(){
		CVECTOR c=new CVECTOR();
		c.r=(byte)FGetInt();
		c.g=(byte)FGetInt();
		c.b=(byte)FGetInt();
		c.a=(byte)FGetInt();
		return c;
	}
	//FVECTOR2として獲得する
	public FVECTOR2 FGetVector2(){
		FVECTOR2 p=new FVECTOR2();
		p.x=FGetFloat();
		p.y=FGetFloat();
		return p;
	}
	//FVECTORとして獲得する
	public FVECTOR FGetVector(){
		FVECTOR p=new FVECTOR();
		p.x=FGetFloat();
		p.y=FGetFloat();
		p.z=FGetFloat();
		return p;
	}
	//FVECTOR4として獲得する
	public FVECTOR4 FGetVector4(){
		FVECTOR4 p=new FVECTOR4();
		p.x=FGetFloat();
		p.y=FGetFloat();
		p.z=FGetFloat();
		p.w=FGetFloat();
		return p;
	}
	public QUAT FGetQUAT(){
		QUAT p=new QUAT();
		p.x=FGetFloat();
		p.y=FGetFloat();
		p.z=FGetFloat();
		p.w=FGetFloat();
		return p;
	}
	public FVECTOR4 FGetVEC4(){
		return FGetVector4();
	}
	public FVECTOR FGetVEC3(){
		return FGetVector();
	}
	public FVECTOR2 FGetVEC2(){
		return FGetVector2();
	}
	*/
	//「{」のチェック
	public boolean CheckKakkoL(){
		char mm=FGetOne();
		if(mm!='{'){
			System.out.println("["+mm+"] {がありません");
			return false;
		}
		return true;
	}
	//「}」のチェック
	public boolean CheckKakkoR(){
		char mm=FGetOne();
		if(mm!='}'){
			System.out.println("["+mm+"] }がありません");
			return false;
		}
		return true;
	}
	//「(」のチェック
	public boolean CheckKakkoML(){
		char mm=FGetOne3();
		if(mm!='('){
			System.out.println("["+mm+"] (がありません");
			return false;
		}
		return true;
	}
	//「)」のチェック
	public boolean CheckKakkoMR(){
		char mm=FGetOne3();
		if(mm!=')'){
			System.out.println("["+mm+"] )がありません");
			return false;
		}
		return true;
	}
	//「[」のチェック
	public boolean CheckKakkoSL(){
		char mm=FGetOne3();
		if(mm!='['){
			System.out.println("["+mm+"] (がありません");
			return false;
		}
		return true;
	}
	//「]」のチェック
	public boolean CheckKakkoSR(){
		char mm=FGetOne3();
		if(mm!=']'){
			System.out.println("["+mm+"] )がありません");
			return false;
		}
		return true;
	}
	//「=」のチェック
	public boolean CheckEqual(){
		char mm=FGetOne3();
		if(mm!='='){
			System.out.println("["+mm+"] )がありません");
			return false;
		}
		return true;
	}
	//「+」のチェック
	public boolean CheckPlus(){
		char mm=FGetOne3();
		if(mm!='+'){
			System.out.println("["+mm+"] )がありません");
			return false;
		}
		return true;
	}
	//「-」のチェック
	public boolean CheckMinus(){
		char mm=FGetOne3();
		if(mm!='-'){
			System.out.println("["+mm+"] )がありません");
			return false;
		}
		return true;
	}
	//「,」のチェック
	public boolean CheckComma(){
		char mm=FGetOne3();
		if(mm!=','){
			System.out.println("["+mm+"] )がありません");
			return false;
		}
		return true;
	}
	//コンマがあってもなくてもスルー
	public boolean CheckComma2(){
		char mm=FGetOne();
		if(mm!=','){
			FGetDec();
			return true;
		}
		return true;
	}
	//CheckSuji
	public static boolean CheckSuji(char mm){
		if(mm=='+' || mm=='-' || (mm>='0' && mm<='9')){
			return true;
		}else{
			return false;
		}
	}
	public static boolean CheckSuji(final String m){
		int len=m.length();
		if(len==0)return false;
		for(int i=0;i<len;i++){
			char mm=m.charAt(i);
			boolean r=false;
			if(mm==0){
				if(mm=='+' || mm=='-')r=true;
			}
			if(!r){
				if(!(mm>='0' & mm<='9'))return false;
			}
		}
		return true;
	}
	
	//チャンクエラー
	public static void ErrorChunkMess(String chunk,String p_chunk){
		System.out.println("["+chunk+"]対応外のチャンクです : "+p_chunk);
	}
	//チャンクチェック
	public static boolean CheckChunk(String chunk,String name){
		if(chunk.equals(name)){
			return true;
		}else{
			return false;
		}
	}
	//チャンク開始?
	public boolean CheckChunkStart(){
		if(!CheckKakkoL()){
			//System.out.println("CheckChunkStart:Error");
			return false;
		}
		return true;
	}
	//チャンク終了?
	public boolean CheckChunkEnd(String chunk){
		if(CheckChunk(chunk,"}"))return true;
		return false;
	}
	//一文字読み込んでチェック
	public boolean CheckOne(char mm){
		char m=FGetOne();
		if(m==mm)return true;
		FGetDec();
		return false;
	}
	//チャンク終了?一文字
	public boolean CheckChunkStartOne(){
		return CheckOne('{');
	}
	public boolean CheckChunkEndOne(){
		return CheckOne('}');
	}
	//ヘッダーチェック
	public boolean CheckHeader(String head_m){
		String head=FGetLine();
		if(!head.startsWith(head_m)){
			//System.out.println(filename+" はUADSceneファイルではありません");
			return false;
		}
		return true;
	}
	//Eofチャンク
	public static boolean CheckChunkEof(String chunk){
		return CheckChunk(chunk,"Eof");
	}
}
