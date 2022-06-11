package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;

public class JmkValueString extends JmkValueBase<String>{
	public JmkValueString(String _name){
		super(_name);
		initValue();
	}
	public JmkValueString(String _name,String _field_name){
		super(_name,_field_name);
		initValue();
	}
	void initValue(){
		value_type=JmkValueType.STRING;
	}
	@SuppressWarnings("unchecked")
	String _getValue(){
		return (String)value;
	}
	
//	public void setValue(String n){
//		value=n;
//	}
	public void replaceEnv(Map<String,String> envs){
		String _value=_getValue();
		value=JmkParser.replaceEnv(envs,_value);
	}
	public Object exec(int operator,Object right){
		Object ret=null;
		switch(operator){
			case JmkOperatorType.LET:
				if(right instanceof String){
					setValue((String)right);
				}
				break;
			case JmkOperatorType.EQUALS:
			case JmkOperatorType.NOTEQUALS:
				{
				String _value=_getValue();
//System.out.println("value1:"+value);
//System.out.println("value2:"+right);
				if(_value!=null && (right instanceof String)){
					String _value2=(String)right;
					switch(operator){
						case JmkOperatorType.EQUALS:
							ret=_value.equals(_value2);
							break;
						case JmkOperatorType.NOTEQUALS:
							ret=!_value.equals(_value2);
							break;
					}
				}
				}
				break;
		}
		if(JmkOperatorType.isSetOperator(operator)){
			ret=this;
		}
		return ret;
	}
}
