package sio29.jmk.tools;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;

public interface JmkFileLinkChecker{
	boolean hasFile(File file);					//���̃`�F�b�J�[���g��
	File[] getLinkFiles(File file);		//�֘A����t�@�C�����X�g��Ԃ�
}
