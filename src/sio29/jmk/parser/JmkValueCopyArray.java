package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;


public class JmkValueCopyArray extends JmkValueBase<ArrayList<JmkCopyPair> >{
	public JmkValueCopyArray(String _name){
		super(_name);
		initValue();
	}
	public JmkValueCopyArray(String _name,String _field_name){
		super(_name,_field_name);
		initValue();
	}
	void initValue(){
		value_type=JmkValueType.COPYARRAY;
		value=new ArrayList<JmkCopyPair>();
	}
	@SuppressWarnings("unchecked")
	ArrayList<JmkCopyPair> _getValue(){
		return (ArrayList<JmkCopyPair>)value;
	}
	@SuppressWarnings("unchecked")
	public void setValue(Object n){
		if(!enable_set)return;
		ArrayList<JmkCopyPair> _value=_getValue();
		if(n instanceof JmkCopyPair){
			((ArrayList<JmkCopyPair>)value).add((JmkCopyPair)n);
		}
	}
	@SuppressWarnings("unchecked")
	public void setValue(JmkPairStrStr n){
		if(!enable_set)return;
		ArrayList<JmkCopyPair> _value=_getValue();
		_value.add(new JmkCopyPair(n.val1,n.val2));
	}
	
	public JmkCopyPair[] getCopyArray(){
		ArrayList<JmkCopyPair> _value=_getValue();
		return (JmkCopyPair[])_value.toArray(new JmkCopyPair[]{});
	}
	public void replaceEnv(Map<String,String> envs){
		ArrayList<JmkCopyPair> _value=_getValue();
		value=JmkParser.replaceEnvCopyPairArray(envs,_value);
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
