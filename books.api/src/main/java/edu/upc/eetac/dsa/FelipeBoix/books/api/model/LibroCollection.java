package edu.upc.eetac.dsa.FelipeBoix.books.api.model;

import java.util.ArrayList;

import org.glassfish.jersey.linking.Binding;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;
import edu.upc.eetac.dsa.FelipeBoix.books.api.LibrosResource;
import edu.upc.eetac.dsa.FelipeBoix.books.api.MediaType;

public class LibroCollection {
	@InjectLinks({
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "create-libro", title = "Create libro", type = MediaType.BOOKS_API_BOOK),
		@InjectLink(value = "/stings?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous stings", type = MediaType.BOOK_API_BOOKS_COLLECTION, bindings = { @Binding(name = "before", value = "${instance.oldestTimestamp}") }),
		@InjectLink(value = "/stings?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest stings", type = MediaType.BOOK_API_BOOKS_COLLECTION, bindings = { @Binding(name = "after", value = "${instance.newestTimestamp}") }) })

private List<Link> links;
public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public long getNewestTimestamp() {
		return newestTimestamp;
	}

	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}

	public long getOldestTimestamp() {
		return oldestTimestamp;
	}

	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}

	public List<Libro> getLibros() {
		return libros;
	}

	public void setLibros(List<Libro> libros) {
		this.libros = libros;
	}

private long newestTimestamp;
private long oldestTimestamp;
	
	
	
	
	
	private List<Libro> libros;
	 
	public LibroCollection() {
		super();
		libros = new ArrayList<>();
	}
 
	public List<Libro> getBooks() {
		return libros;
	}
 
	public void setBooks(List<Libro> libros) {
		this.libros = libros;
	}
 
	public void addLibro(Libro libro) {
		libros.add(libro);
	}
}
