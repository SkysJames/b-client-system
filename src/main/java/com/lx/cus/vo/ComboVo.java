package com.lx.cus.vo;

import java.io.Serializable;

public class ComboVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Object id;
	
	private String text;
	
	public ComboVo() {}
	
	public ComboVo(Object id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
