package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;


public class JmkValueStringArray extends JmkValueBase<ArrayList<String> >{
	public JmkValueStringArray(String _name){
		super(_name);
		initValue();
	}
	public JmkValueStringArray(String _name,String _field_name){
		super(_name,_field_name);
		initValue();
	}
	void initValue(){
		value_type=JmkValueType.STRINGARRAY;
		value=new ArrayList<String>();
	}
	@SuppressWarnings("unchecked")
	ArrayList<String>  _getValue(){
		return (ArrayList<String>  )value;
	}
	@SuppressWarnings("unchecked")
	public void setValue(String n){
		if(!enable_set)return;
		ArrayList<String> _value=_getValue();
		_value.add(n);
	}
	
	public String[] getStringArray(){
		ArrayList<String> _value=_getValue();
		return (String[])_value.toArray(new String[]{});
	}
	public void replaceEnv(Map<String,String> envs){
		ArrayList<String> _value=_getValue();
		value=JmkParser.replaceEnv(envs,_value);
	}
	public Object exec(int operator,Object right){
		switch(operator){
			case JmkOperatorType.LET:
				if(right instanceof String){
					setValue((String)right);
				}
				break;
		}
		if(JmkOperatorType.isSetOperator(operator)){
			return this;
		}else{
			return null;
		}
	}
}
