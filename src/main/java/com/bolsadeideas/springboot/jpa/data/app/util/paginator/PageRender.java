package com.bolsadeideas.springboot.jpa.data.app.util.paginator;

import java.util.ArrayList;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	
	
	private String url;
	private Page<T> page;
	private int totalDePaginas;
	private int numElementosPorPagina;
	private int paginaActual;
	
	private List<PageItem> paginas;
	
	public PageRender(String url, Page<T> page) {
		this.paginas = new ArrayList<PageItem>();
	
		this.url = url;
		this.page = page;
		this.numElementosPorPagina = page.getSize();
		this.totalDePaginas = page.getTotalPages();
		this.paginaActual = page.getNumber() + 1;
		Integer desde, hasta;
		if(totalDePaginas <= numElementosPorPagina) {
			desde = 1;
			hasta = totalDePaginas;
		}else {
			if(paginaActual<= numElementosPorPagina/2) {
				desde = 1;
				hasta = numElementosPorPagina;
			}else if(paginaActual>= (totalDePaginas - (numElementosPorPagina/2))){
				desde = totalDePaginas - numElementosPorPagina + 1;
				hasta = numElementosPorPagina;
			}else {
				desde = paginaActual - (numElementosPorPagina/2);
				
				hasta = numElementosPorPagina;
			}
			
		}
		
		for(Integer i = 0; i<hasta;i++){
			
			paginas.add(new PageItem((desde+i),(getPaginaActual() == (desde+i))));
		}
		
		
	}

	public String getUrl() {
		return url;
	}

	public int getTotalDePaginas() {
		return totalDePaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}
	
	public boolean isFirst(){
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public Boolean isHasNext() {
		return page.hasNext();
	}
	
	public Boolean isHasPrevious() {
		return page.hasPrevious();
	}
	
}
