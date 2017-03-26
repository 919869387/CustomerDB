package pojo;

import net.sf.json.JSONArray;

public class SystemDynamicValues {
	
	Integer id;
	String name;
	String dynamicvalues = new JSONArray().toString();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDynamicvalues() {
		return dynamicvalues;
	}
	public void setDynamicvalues(String dynamicvalues) {
		this.dynamicvalues = dynamicvalues;
	}
	
}
