package edu.upc.eetac.dsa.FelipeBoix.books.api.model;

import java.util.ArrayList;
import java.util.List;
public class ResenasCollection {

	private List<Resenas> resenas;
	 
	public ResenasCollection() {
		super();
		resenas = new ArrayList<>();
	}
 
	public List<Resenas> getResenas() {
		return resenas;
	}
 
	public void setResenas(List<Resenas> resenas) {
		this.resenas = resenas;
	}
 
	public void addResena(Resenas resena) {
		resenas.add(resena);
	}
}

