package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;

public class JmkValueFloat extends JmkValueBase<Float>{
	public JmkValueFloat(String _name){
		super(_name);
		initValue();
	}
	public JmkValueFloat(String _name,String _field_name){
		super(_name,_field_name);
		initValue();
	}
	void initValue(){
		value_type=JmkValueType.FLOAT;
	}
	public void replaceEnv(Map<String,String> envs){
	}
}
