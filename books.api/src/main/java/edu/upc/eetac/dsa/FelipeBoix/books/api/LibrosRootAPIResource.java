package edu.upc.eetac.dsa.FelipeBoix.books.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import edu.upc.eetac.dsa.FelipeBoix.books.api.model.LibrosRootAPI;
 

 
@Path("/")
public class LibrosRootAPIResource {
	@GET
	public LibrosRootAPI getRootAPI() {
		LibrosRootAPI api = new LibrosRootAPI();
		return api;
	}
}