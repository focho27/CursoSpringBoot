package com.bolsadeideas.springboot.jpa.data.app.util.paginator;

public class PageItem {

	private Integer num;
	private Boolean actual;
	
	public PageItem(Integer num, Boolean isActual) {

		this.num = num;
		this.actual = isActual;
	}

	public Integer getNum() {
		return num;
	}

	public Boolean isActual() {
		return actual;
	}
	
	
	
}
