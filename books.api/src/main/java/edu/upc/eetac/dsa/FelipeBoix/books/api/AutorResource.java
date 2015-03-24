package edu.upc.eetac.dsa.FelipeBoix.books.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.FelipeBoix.books.api.model.Autor;
import edu.upc.eetac.dsa.FelipeBoix.books.api.model.AutoresCollection;
import edu.upc.eetac.dsa.FelipeBoix.books.api.model.Libro;
import edu.upc.eetac.dsa.FelipeBoix.books.api.model.LibroCollection;

@Path("/autores")
public class AutorResource {
	
		private DataSource ds = DataSourceSPA.getInstance().getDataSource();

		private String GET_AUTOR = " select * from autores";
		@GET
		@Produces(MediaType.BOOKS_API_AUTOR)
		public AutoresCollection getAutores() {
			AutoresCollection autores = new AutoresCollection();
		 
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(GET_AUTOR);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Autor autor = new Autor();
					autor.setId(rs.getInt("id"));
					autor.setUsername(rs.getString("username"));
				
					
					autores.addAutor(autor);
				}
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return autores;
		}

		private String GET_AUTOR_ID = "select * from autores where id = ?";
		@GET
		@Path("/{id}")
		@Produces(MediaType.BOOKS_API_AUTOR)
		public Response getAutor(@PathParam("id") String id, @Context Request request) {
			// Create CacheControl
			CacheControl cc = new CacheControl();
		 
			Autor autor = getAutorFromDatabase(id);
		 
			// Calculate the ETag on last modified date of user resource
			EntityTag eTag = new EntityTag(Long.toString(autor.getLastModified()));
		 
			// Verify if it matched with etag available in http request
			Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);
		 
			// If ETag matches the rb will be non-null;
			// Use the rb to return the response without any further processing
			if (rb != null) {
				return rb.cacheControl(cc).tag(eTag).build();
			}
		 
			// If rb is null then either it is first time request; or resource is
			// modified
			// Get the updated representation and return with Etag attached to it
			rb = Response.ok(autor).cacheControl(cc).tag(eTag);
		 
			return rb.build();
		}
		
			
			/*	Autor autor = new Autor();
			 
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(GET_AUTOR_ID);
				stmt.setInt(1, Integer.valueOf(id));
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					autor.setId(rs.getInt("id"));
					autor.setUsername(rs.getString("username"));
				
				} else {
					throw new NotFoundException("There's no autor with id="
							+ id);
			}} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return autor;
		}*/
		
		private Autor getAutorFromDatabase(String id) {
			Autor autor = new Autor();
			 
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(GET_AUTOR_ID);
				stmt.setInt(1, Integer.valueOf(id));
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					autor.setId(rs.getInt("id"));
					autor.setUsername(rs.getString("username"));
				
				} else {
					throw new NotFoundException("There's no autor with id="
							+ id);
			}} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return autor;
		}
		
		
		private void validateSting(Autor autor) {
			if (autor.getUsername() == null)
				throw new BadRequestException("Subject can't be null.");
		}
		private String INSERT_AUTOR = "INSERT into autores (username) values (?);";

		@POST
		@Consumes(MediaType.BOOKS_API_AUTOR)
		@Produces(MediaType.BOOKS_API_AUTOR)
		public Autor createAutor(Autor autor) {
			validateSting(autor);
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(INSERT_AUTOR, Statement.RETURN_GENERATED_KEYS);
				
				
				stmt.setString(1, autor.getUsername());
			
				stmt.executeUpdate();
				System.out.println(stmt);
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
		 
					autor = getAutorFromDatabase(Integer.toString(id));
					System.out.println("autor creado");
				} else {
					// Something has failed...
				}
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return autor;
		}

		
		private String DELETE_AUTOR = "delete from autores where id=?";

		@DELETE
		@Path("/{id}")
		public void deleteLibro(@PathParam("id") String id) {
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(DELETE_AUTOR);
				stmt.setInt(1, Integer.valueOf(id));
		 
				int rows = stmt.executeUpdate();
				if (rows == 0)
					throw new NotFoundException("There's no sting with stingid="
							+ id);
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		
		private String UPDATE_AUTOR="UPDATE autores set username=ifnull(?, username) where id=?";
		@PUT
		@Path("/{id}")
		@Consumes(MediaType.BOOKS_API_AUTOR)
		@Produces(MediaType.BOOKS_API_AUTOR)
		public Autor updateAutor(@PathParam("id") String id, Autor autor) {
			validateUpdateSting(autor);
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		 
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(UPDATE_AUTOR);
				stmt.setString(1, autor.getUsername());
				stmt.setInt(2, Integer.valueOf(id)); 
		 
				int rows = stmt.executeUpdate();
				if (rows == 1)
					autor = getAutorFromDatabase(id);
				else {
					;// Updating inexistent sting
				}
		 
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					conn.close();
				} catch (SQLException e) {
				}
			}
		 
			return autor;
			}
		private void validateUpdateSting(Autor autor) {
			if (autor.getUsername() != null && autor.getUsername().length() > 100)
				throw new BadRequestException(
						"Subject can't be greater than 100 characters.");
		}
		}

