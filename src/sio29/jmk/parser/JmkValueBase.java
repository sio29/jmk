package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;

public abstract class JmkValueBase<T>{
	public String name;
	public String field_name;
	public Object value;
	public int value_type;
	public boolean enable_set=true;
	//
	public JmkValueBase(String _name){
		name =_name;
	}
	public JmkValueBase(String _name,String _field_name){
		name      =_name;
		field_name=_field_name;
	}
	public void setValue(Object n){
		if(!enable_set)return;
		value=n;
	}
	public Object getValue(){
		return value;
	}
	public int getValueType(){
		return value_type;
	}
	//
	public void replaceEnv(Map<String,String> envs){
		//System.out.println("skip replaceEnv:"+field_name);
	}
	public String toStringValue(){
		if(value==null)return null;
		return value.toString();
	}
	public String toString(){
		String type_name=JmkValueType.getName(getValueType());
		String value_m=toStringValue();
		boolean same=false;
		if(name!=null && field_name!=null){
			same=name.equals(field_name);
		}
		return String.format("%s:cmd[%s]:field[%s]:type(%s):value(%s)",same,name,field_name,type_name,value_m);
	}
	public Object exec(int operator,Object right){
		return this;
	}
}
