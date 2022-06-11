package sio29.jmk.tools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public interface JmkFileLinkChecker{
	boolean hasFile(File file);					//このチェッカーを使う
	File[] getLinkFiles(File file);		//関連するファイルリストを返す
}
