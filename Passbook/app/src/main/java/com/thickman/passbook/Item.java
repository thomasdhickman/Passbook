package com.thickman.passbook;

public class Item {

	protected int id;
	protected String key;
	protected String value1;
	protected String value2;
	
	public Item() {

	}

	public Item(String key, String value1, String value2) {
		super();
		this.key = key;
		this.value1 = value1;
		this.value2 = value2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}
}
