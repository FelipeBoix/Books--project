package edu.upc.eetac.dsa.FelipeBoix.books.api;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
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
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.FelipeBoix.books.api.model.Libro;
import edu.upc.eetac.dsa.FelipeBoix.books.api.model.LibroCollection;
import edu.upc.eetac.dsa.FelipeBoix.books.api.model.Resenas;
import edu.upc.eetac.dsa.FelipeBoix.books.api.model.ResenasCollection;


 
@Path("/libros")
public class LibrosResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	private SecurityContext security;
	
//private String GET_STINGS_QUERY = "select s.*, u.name from stings s, users u where u.username=s.username order by creation_timestamp desc";
private String GET_BOOKS_QUERY = " select * from libros";

@GET
@Produces(MediaType.BOOK_API_BOOKS_COLLECTION)
public LibroCollection getLibros() {
	
	System.out.println("no conectados a la BD"); 
	LibroCollection libros = new LibroCollection();
 
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
	System.out.println("conectados a la BD");
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(GET_BOOKS_QUERY);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			Libro libro = new Libro();
			libro.setLibrosid(rs.getInt("librosid"));
			libro.setTitulo(rs.getString("titulo"));
		    libro.setAutor(rs.getString("autor"));
			libro.setLengua(rs.getString("lengua"));
			libro.setEdicion(rs.getString("edicion"));
			libro.setFechaedicion(rs.getDate("fechaedicion"));
			libro.setFechaimpresion(rs.getDate("fechaimpresion"));
			libro.setEditorial(rs.getString("editorial"));
			
			libros.addLibro(libro);
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
 
	return libros;
}

private String GET_BOOK_ID = "select * from libros where librosid = ?";
@GET
@Path("/{librosid}")
@Produces(MediaType.BOOKS_API_BOOK)
public Response getLibro(@PathParam("librosid") String librosid,
		@Context Request request) {
	
	// Create CacheControl
		CacheControl cc = new CacheControl();
	 
		Libro libro = getLibroFromDatabase(librosid);
	 
		// Calculate the ETag on last modified date of user resource
		EntityTag eTag = new EntityTag(Long.toString(libro.getLastModified()));
	 
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
		rb = Response.ok(libro).cacheControl(cc).tag(eTag);
	 
		return rb.build();
	}

private Libro getLibroFromDatabase(String librosid) {
	
	Libro libro = new Libro();
	 
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(GET_BOOK_ID);
		stmt.setInt(1, Integer.valueOf(librosid));
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			libro.setLibrosid(rs.getInt("librosid"));
			libro.setTitulo(rs.getString("titulo"));
		    libro.setAutor(rs.getString("autor"));
			libro.setLengua(rs.getString("lengua"));
			libro.setEdicion(rs.getString("edicion"));
			libro.setFechaedicion(rs.getDate("fechaedicion"));
			libro.setFechaimpresion(rs.getDate("fechaimpresion"));
			libro.setEditorial(rs.getString("editorial"));
		}else {
			throw new NotFoundException("There's no libro with libroid="
					+ librosid);
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
 
	return libro;
}


private void validateSting(Libro libro) {//acabar
	if (libro.getTitulo() == null)
		throw new BadRequestException("Subject can't be null.");
	if (libro.getAutor() == null)
		throw new BadRequestException("Content can't be null.");
	if (libro.getLengua().length() > 100)
		throw new BadRequestException("Subject can't be greater than 100 characters.");
	if (libro.getEdicion().length() > 500)
		throw new BadRequestException("Content can't be greater than 500 characters.");
}
private String INSERT_LIBRO = "INSERT into libros (username, titulo, autor, lengua, edicion, fechaedicion, fechaimpresion, editorial) values (?,?,?,?,?,?,?,?);";

@POST
@Consumes(MediaType.BOOKS_API_BOOK)
@Produces(MediaType.BOOKS_API_BOOK)
public Libro createLibro(Libro libro) {
	validateSting(libro);
	Connection conn = null;//conexion
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(INSERT_LIBRO, Statement.RETURN_GENERATED_KEYS);
	
		stmt.setString(1, security.getUserPrincipal().getName());
		
		stmt.setString(2, libro.getTitulo());
		stmt.setString(3, libro.getAutor());
		stmt.setString(4, libro.getLengua());
		stmt.setString(5, libro.getEdicion());
		stmt.setDate(6, libro.getFechaedicion());
		stmt.setDate(7, libro.getFechaimpresion());
		stmt.setString(8, libro.getEditorial());
		stmt.executeUpdate();
		System.out.println(stmt);
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			int librosid = rs.getInt(1);
 
			libro = getLibroFromDatabase(Integer.toString(librosid));
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
 
	return libro;
}

private String DELETE_LIBRO = "delete from libros where librosid=?";

@DELETE
@Path("/{librosid}")
public void deleteLibro(@PathParam("librosid") String librosid) {
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(DELETE_LIBRO);
		stmt.setInt(1, Integer.valueOf(librosid));
 
		int rows = stmt.executeUpdate();
		if (rows == 0)
			throw new NotFoundException("There's no sting with stingid="
					+ librosid);
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

//private String UPDATE_STING_QUERY = "update stings set subject=ifnull(?, subject), content=ifnull(?, content) where stingid=?";
private String UPDATE_LIBRO="UPDATE libros set titulo=ifnull(?, titulo), lengua=ifnull(?, lengua), edicion=ifnull(?,edicion) where librosid=?";
@PUT
@Path("/{librosid}")
@Consumes(MediaType.BOOKS_API_BOOK)
@Produces(MediaType.BOOKS_API_BOOK)
public Libro updateBook(@PathParam("librosid") String librosid, Libro libro) {
	validateUpdateSting(libro);
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(UPDATE_LIBRO);
		stmt.setString(1, libro.getTitulo());
		//stmt.setString(2, libro.getAutor());
		stmt.setString(2, libro.getLengua());
		stmt.setString(3, libro.getEdicion());
		//stmt.setDate(5, libro.getFechaedicion());
		//stmt.setDate(6, libro.getFechaimpresion());
		//stmt.setString(7, libro.getEditorial());
		stmt.setInt(4, Integer.valueOf(librosid)); 
 
		int rows = stmt.executeUpdate();
		if (rows == 1)
			libro = getLibroFromDatabase(librosid);
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
 
	return libro;
	}

private void validateUser(String librosid) {
    Libro libro = getLibroFromDatabase(librosid);
    String username = libro.getUsername();
	if (!security.getUserPrincipal().getName()
			.equals(username))
		throw new ForbiddenException(
				"You are not allowed to modify this libro.");
}
private void validateUpdateSting(Libro libro) {
	if (libro.getTitulo() != null && libro.getTitulo().length() > 100)
		throw new BadRequestException(
				"Subject can't be greater than 100 characters.");
	if (libro.getLengua() != null && libro.getLengua().length() > 500)
		throw new BadRequestException(
				"Content can't be greater than 500 characters.");
	if (libro.getEdicion() != null && libro.getEdicion().length() > 500)
		throw new BadRequestException(
				"Content can't be greater than 500 characters.");
}
private String GET_RESENAS = " select * from resena";
@GET
@Path("/resenas")
@Produces(MediaType.BOOKS_API_RESENAS_COLLECTION)
public ResenasCollection getResenas() {
	ResenasCollection resenas = new ResenasCollection();
 
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(GET_RESENAS);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			Resenas resena = new Resenas();
			resena.setResenaid(rs.getInt("resenaid"));
			resena.setUsername(rs.getString("username"));
			resena.setText(rs.getString("text"));
			resena.setLibroid(rs.getInt("libroid"));
			resena.setFechaupdate(rs.getTimestamp("fechaupdate").getTime());		
			resenas.addResena(resena);
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
 
	return resenas;
}
// funcion para listar una resena de un libro.
private String GET_CRITICA_ID_QUERY = "SELECT * FROM resena where resenaid=?";
public Resenas getResenas( String resenaid) {
	
	Resenas resena = new Resenas();
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(GET_CRITICA_ID_QUERY);
		stmt.setInt(1, Integer.valueOf(resenaid));
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) {
			
			resena.setResenaid(rs.getInt("resenaid"));
			resena.setUsername(rs.getString("username"));
			resena.setLibroid(rs.getInt("libroid"));
			resena.setText(rs.getString("text"));
			resena.setFechaupdate(rs.getTimestamp("fechaupdate").getTime());
			
		}else{
			throw new NotFoundException("There's no review with resenaid="
					+ resenaid);
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
 
	return resena;
}

private String GET_RESENA_ID = "select * from resena where resena.libroid = ?";
@GET
@Path("/resenas/{libroid}")
@Produces(MediaType.BOOKS_API_RESENAS_COLLECTION)
public ResenasCollection getResena(@PathParam("libroid") String libroid) {
	ResenasCollection resenas= new ResenasCollection();
	 
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(GET_RESENA_ID);
		stmt.setInt(1, Integer.valueOf(libroid));
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
		Resenas resena = new Resenas();
			resena.setResenaid(rs.getInt("resenaid"));
			resena.setUsername(rs.getString("username"));
		    resena.setText(rs.getString("text"));
			resena.setLibroid(rs.getInt("libroid"));
			resena.setFechaupdate(rs.getTimestamp("fechaupdate").getTime());
			resenas.addResena(resena);
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
 
	return resenas;
}

private void validateSting(Resenas resena) {
	if (resena.getUsername() == null)
		throw new BadRequestException("Subject can't be null.");
	if (resena.getText() == null)
		throw new BadRequestException("Content can't be null.");
}

private String INSERT_RESENA = "INSERT into resena (username,text,libroid) values (?,?,?)";
@POST
@Path("/resenas/{libroid}")
@Consumes(MediaType.BOOKS_API_RESENAS)
@Produces(MediaType.BOOKS_API_RESENAS)

public Resenas createResena(Resenas resena) {
	validateSting(resena);
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
	System.out.println("REVIEW creada");
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(INSERT_RESENA, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, resena.getUsername());
		stmt.setString(2, resena.getText());
		stmt.setInt(3, resena.getLibroid());
		//stmt.setInt(3, Integer.valueOf(libroid));
		
		stmt.executeUpdate();
		System.out.println(stmt);
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			int resenasid = rs.getInt(1);
			System.out.println(resenasid);
			resena = getResenas(Integer.toString(resenasid));
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
 
	return resena;
}
private String DELETE_RESENA = "delete from resenas where resenaid=?;";

@DELETE
@Path("/resenas/{resenaid}")
public void deleteResenas(@PathParam("resenaid") String resenaid) {
	Connection conn = null;
	try {
		conn = ds.getConnection();
	} catch (SQLException e) {
		throw new ServerErrorException("Could not connect to the database",
				Response.Status.SERVICE_UNAVAILABLE);
	}
 
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement(DELETE_RESENA);
		stmt.setInt(1, Integer.valueOf(resenaid));
		//stmt.setInt(2, Integer.valueOf(libroid));
 
		int rows = stmt.executeUpdate();
		if (rows == 0)
			throw new NotFoundException("There's no sting with stingid="
					+ resenaid);
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

}
	

