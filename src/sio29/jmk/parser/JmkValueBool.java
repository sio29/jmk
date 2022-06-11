package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;

public class JmkValueBool extends JmkValueBase<Boolean>{
	public JmkValueBool(String _name){
		super(_name);
		initValue();
	}
	public JmkValueBool(String _name,String _field_name){
		super(_field_name);
		initValue();
	}
	void initValue(){
		value_type=JmkValueType.BOOL;
	}
	public void replaceEnv(Map<String,String> envs){
	}
}
