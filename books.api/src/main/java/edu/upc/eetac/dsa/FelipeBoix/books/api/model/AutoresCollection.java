package edu.upc.eetac.dsa.FelipeBoix.books.api.model;


import java.util.ArrayList;
import java.util.List;

public class AutoresCollection {

	private List<Autor> autores;
	 
	public AutoresCollection() {
		super();
		autores = new ArrayList<>();
	}
 
	public List<Autor> getAutores() {
		return autores;
	}
 
	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}
 
	public void addAutor(Autor autor) {
		autores.add(autor);
	}
}
