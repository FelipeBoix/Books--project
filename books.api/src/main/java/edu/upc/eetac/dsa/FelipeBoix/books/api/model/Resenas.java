package edu.upc.eetac.dsa.FelipeBoix.books.api.model;

import java.sql.Date;

public class Resenas {
	
	private int resenaid;
	private String username;
	private String text;
	private long fechaupdate;
	private int libroid;
	
	
	public long getFechaupdate() {
		return fechaupdate;
	}
	public void setFechaupdate(long fechaupdate) {
		this.fechaupdate = fechaupdate;
	}
	
	public int getResenaid() {
		return resenaid;
	}
	public void setResenaid(int resenaid) {
		this.resenaid = resenaid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public int getLibroid() {
		return libroid;
	}
	public void setLibroid(int libroid) {
		this.libroid = libroid;
	}
	
}
