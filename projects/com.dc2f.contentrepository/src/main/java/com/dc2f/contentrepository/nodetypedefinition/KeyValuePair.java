package com.dc2f.contentrepository.nodetypedefinition;

public class KeyValuePair {
	private String key;
	private Object value;

	public KeyValuePair(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
}
