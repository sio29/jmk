package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;

public class JmkValueInt extends JmkValueBase<Integer>{
	public JmkValueInt(String _name){
		super(_name);
		initValue();
	}
	public JmkValueInt(String _name,String _field_name){
		super(_name,_field_name);
		initValue();
	}
	void initValue(){
		value_type=JmkValueType.INT;
	}
	public void replaceEnv(Map<String,String> envs){
	}
	public Object exec(int operator,Object right){
		switch(operator){
			case JmkOperatorType.LET:
				if(right instanceof Integer){
					setValue((Integer)right);
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
