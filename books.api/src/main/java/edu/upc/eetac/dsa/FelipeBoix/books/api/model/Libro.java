package edu.upc.eetac.dsa.FelipeBoix.books.api.model;

import java.sql.Date;

import org.glassfish.jersey.linking.Binding;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;
 


import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.FelipeBoix.books.api.LibrosResource;
import edu.upc.eetac.dsa.FelipeBoix.books.api.MediaType;
 


public class Libro {

	private int librosid;
	private String titulo;
	private String autor;
	private String lengua;
	private String edicion;
	private Date fechaedicion;
	private Date fechaimpresion;
	private String editorial;
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	private long lastModified;
	
	@InjectLinks({
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "libros", title = "Latest libros", type = MediaType.BOOK_API_BOOKS_COLLECTION),
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Libro", type = MediaType.BOOKS_API_BOOK, method = "getLibro", bindings = @Binding(name = "librosid", value = "${instance.librosid}")) })

private List<Link> links;

	
	
	
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public long getCreationTimestamp() {
		return creationTimestamp;
	}
	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	private long creationTimestamp;
	List<Resenas> resenas= new ArrayList<Resenas>();
	
	
	public List<Resenas> getResenas() {
		return resenas;
	}
	public void setResenas(List<Resenas> resenas) {
		this.resenas = resenas;
	}
	public int getLibrosid() {
		return librosid;
	}
	public void setLibrosid(int librosid) {
		this.librosid = librosid;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getLengua() {
		return lengua;
	}
	public void setLengua(String lengua) {
		this.lengua = lengua;
	}
	public String getEdicion() {
		return edicion;
	}
	public void setEdicion(String edicion) {
		this.edicion = edicion;
	}
	public Date getFechaedicion() {
		return fechaedicion;
	}
	public void setFechaedicion(Date fechaedicion) {
		this.fechaedicion = fechaedicion;
	}
	public Date getFechaimpresion() {
		return fechaimpresion;
	}
	public void setFechaimpresion(Date fechaimpresion) {
		this.fechaimpresion = fechaimpresion;
	}
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	

}
