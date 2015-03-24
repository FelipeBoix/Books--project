package edu.upc.eetac.dsa.FelipeBoix.books.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import edu.upc.eetac.dsa.FelipeBoix.books.api.model.LibrosError;

@Provider
public class WebApplicationExceptionMapper implements
		ExceptionMapper<WebApplicationException> {
	@Override
	public Response toResponse(WebApplicationException exception) {
		LibrosError error = new LibrosError(
				exception.getResponse().getStatus(), exception.getMessage());
		return Response.status(error.getStatus()).entity(error)
				.type(MediaType.BOOKS_API_ERROR).build();
	}
 
}
