package sio29.jmk.parser;

import java.util.*;
import java.lang.reflect.*;


public class JmkValueList{
	private HashMap<String,String> aliasmap=new HashMap<String,String>();
	private HashMap<String,JmkValueBase> map=new HashMap<String,JmkValueBase>();
	
	public void putAlias(String src,String dst){
		aliasmap.put(src,dst);
	}
	public String getAlias(String src){
		String dst=aliasmap.get(src);
		if(dst==null)return src;
		return dst;
	}
	public void add(JmkValueBase param){
		map.put(param.name,param);
	}
	public JmkValueBase getByCommand(String command_name){
		return map.get(command_name);
	}
	public JmkValueBase getByField(String field_name){
		for(String name : map.keySet()){
			JmkValueBase pv=map.get(name);
			if(pv.field_name==null)continue;
			if(pv.field_name.equals(field_name))return pv;
		}
		return null;
	}
	
	public boolean isValueByCommand(String command_name){
		return (getByCommand(command_name)!=null);
	}
	public boolean isValueByField(String field_name){
		return (getByField(field_name)!=null);
	}
	static void setValueSub(JmkValueBase param,Object value){
		if(param instanceof JmkValueStringArray){
			((JmkValueStringArray)param).setValue((String)value);
		}else{
			param.setValue(value);
		}
	}
	@SuppressWarnings("unchecked")
	public void setValueByField(String field_name,Object value){
		JmkValueBase param=getByField(field_name);
		if(param==null)return;
		setValueSub(param,value);
	}
	
	@SuppressWarnings("unchecked")
	public void setValueByCommand(String command_name,Object value){
		JmkValueBase param=getByCommand(command_name);
		if(param==null)return;
		setValueSub(param,value);
	}
	
	public Object getValueByField(String name){
		JmkValueBase param=getByField(name);
		if(param==null)return null;
		return param.getValue();
	}
	public String getValueStringByField(String name){
		return (String)getValueByField(name);
	}
	@SuppressWarnings("unchecked")
	public String[] getValueStringArrayByField(String name){
		Object n=getValueByField(name);
		if(n==null){
			System.out.println("field value is null !");
			return null;
		}
		ArrayList<String> list=(ArrayList<String>)n;
		return (String[])list.toArray(new String[]{});
	}
	@SuppressWarnings("unchecked")
	public JmkCopyPair[] getValueCopyArrayByField(String name){
		Object n=getValueByField(name);
		if(n==null)return null;
		ArrayList<JmkCopyPair> list=(ArrayList<JmkCopyPair>)n;
		return (JmkCopyPair[])list.toArray(new JmkCopyPair[]{});
	}
	
	public int getValueTypeByCommand(String name){
		JmkValueBase pv=getByCommand(name);
		if(pv==null)return JmkValueType.NONE;
		return pv.getValueType();
	}
	public int getValueTypeByField(String name){
		JmkValueBase pv=getByField(name);
		if(pv==null)return JmkValueType.NONE;
		return pv.getValueType();
	}
	@SuppressWarnings("unchecked")
	public void replaceEnvByField(Map<String,String> envs,String field_name){
		JmkValueBase pv=getByField(field_name);
		if(pv==null)return;
		pv.replaceEnv(envs);
	}
	public void setEnableSetByField(String name,boolean n){
		JmkValueBase pv=getByField(name);
		if(pv==null)return;
		pv.enable_set=n;
	}
	
	public void printSetting(){
		System.out.println("--------------------------------");
		for(String name : map.keySet()){
			JmkValueBase pv=map.get(name);
			System.out.println(String.format("cmd[%s]:field[%s]:type(%s)",name,pv.field_name,JmkValueType.getName(pv.getValueType()) ));
		}
		System.out.println("--------------------------------");
	}
	public String toString(){
		String m="";
		for(String name : map.keySet()){
			JmkValueBase pv=map.get(name);
			m+=pv.toString()+"\n";
		}
		return m;
	}
	@SuppressWarnings("unchecked")
	public void replaceEnv(Map<String,String> envs){
		for(String name : map.keySet()){
			JmkValueBase pv=map.get(name);
			if(pv==null)continue;
			pv.replaceEnv(envs);
		}
	}
}

