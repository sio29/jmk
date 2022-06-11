package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;


public class JmkValueMapStrStr extends JmkValueBase<HashMap<String,String> >{
	public JmkValueMapStrStr(String _name){
		super(_name);
		initValue();
	}
	public JmkValueMapStrStr(String _name,String _field_name){
		super(_name,_field_name);
		initValue();
	}
	void initValue(){
		value_type=JmkValueType.MAP_STR_STR;
		value=new HashMap<String,String>();
	}
	@SuppressWarnings("unchecked")
	HashMap<String,String> _getValue(){
		return (HashMap<String,String> )value;
	}
	@SuppressWarnings("unchecked")
	public void setValue(Object n){
		if(!enable_set)return;
		HashMap<String,String> _value=_getValue();
		if(n instanceof Map.Entry){
			Map.Entry<String,String> n2=(Map.Entry<String,String>)n;
			_value.put(n2.getKey(),n2.getValue());
		}
	}
	public void replaceEnv(Map<String,String> envs){
		HashMap<String,String> _value=_getValue();
		value=JmkParser.replaceEnv(envs,_value);
	}
	public Object exec(int operator,Object right){
		switch(operator){
			case JmkOperatorType.LET:
				setValue(right);
				break;
		}
		if(JmkOperatorType.isSetOperator(operator)){
			return this;
		}else{
			return null;
		}
	}
}
