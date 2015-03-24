package edu.upc.eetac.dsa.FelipeBoix.books.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;



import edu.upc.eetac.dsa.FelipeBoix.books.api.LibrosResource;
import edu.upc.eetac.dsa.FelipeBoix.books.api.LibrosRootAPIResource;
import edu.upc.eetac.dsa.FelipeBoix.books.api.MediaType;

public class LibrosRootAPI {
	@InjectLinks({
        @InjectLink(resource = LibrosRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Books Root API",method = "getRootAPI"),
        @InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "collection", title = "Latest libros", type = MediaType.BOOK_API_BOOKS_COLLECTION),
        @InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "create-sting", title = "Create new libro", type = MediaType.BOOKS_API_BOOK)})
	
	private List<Link> links;

public List<Link> getLinks() {
	return links;
}

public void setLinks(List<Link> links) {
	this.links = links;
}
}