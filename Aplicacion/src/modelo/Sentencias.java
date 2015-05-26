/*
 * SOFTBASE - GRUPO 10
 * AUTORES:
 * 		-Alberto Blasco
 * 		-Diego Galvez
 * 		-Patricia Lazaro
 * 		-Alejandro Marquez
 * 		-Alejandro Royo
 * 		-Jaime Ruiz-Borau
 * DESCRIPCION: clase que encapsula las distintas sentencias SQL (MySQL) para
 * 				el acceso a la Base de Datos.
 */

package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Sentencias {

	/* declaracion de atributos */
	private static Connection connection;
	private static boolean abierta;

	/* declaracion de metodos y funciones */

	/**
	 * Metodo constructor
	 */
	public Sentencias() {
		try {
			if (!abierta) {
				connection = GestorDeConexiones.getConnection();
				abierta = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que cierra la actual conexion
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param filtros
	 *            : TreeMap que contiene los distintos requisitos para la
	 *            consulta (Ej. que la plataforma sea PS3)
	 * @param nPagina
	 *            : Numero de pagina que se quiere consultar, la primera es la
	 *            numero 1
	 * @return Una lista con los 5 juegos que pertenezcan a la pagina pasada y
	 *         que cumplan los requisitos especificados en el TreeMap
	 * 
	 *         Metodo que aplica distintas querys en funcion del contenido del
	 *         parametro filtros. Ademas devuelve una consulta paginada, es
	 *         decir, dependiendo del numero de pagina, devuelve los 5 elementos
	 *         de dicha pagina. Se asume la primera pagina la numero 1.
	 */
	public ArrayList<Juego> listarJuegosMultipleFiltros(
			HashMap<String, String> filtros, int nPagina) {
		Logger.log("Accediendo a la BD para obtener juegos aplicando filtros...");
		String query = "select distinct JUEGO.id, titulo, imagen, resumen, rating, lanzamiento, precio, nombre, alias "
				+ "from JUEGO, JUEGO_GENERO, JUEGO_PLATAFORMA, PLATAFORMA where "
				+ "JUEGO.id = JUEGO_GENERO.id and JUEGO.id = JUEGO_PLATAFORMA.juego and "
				+ "JUEGO_PLATAFORMA.plataforma = PLATAFORMA.id";
		String order = "", type = "";
		boolean rat = false, fec = false;
		/* aplicar filtros */
		if (filtros != null) {
			for (Entry<String, String> e : filtros.entrySet()) {
				if (e.getKey() != null && e.getValue() != null
						&& !e.getValue().equals("")) {
					switch (e.getKey()) {
					case ("titulo"):
						query = query + " and JUEGO.titulo LIKE '%"
								+ e.getValue() + "%'";
						break;
					case ("preciomin"):
						query = query + " and JUEGO.precio >= " + e.getValue();
						break;
					case ("preciomax"):
						query = query + " and JUEGO.precio <= " + e.getValue();
						break;
					case ("genero"):
						query = query + " and JUEGO_GENERO.genero = '"
								+ e.getValue() + "'";
						break;
					case ("plataforma"):
						query = query + " and PLATAFORMA.alias = '"
								+ e.getValue() + "'";
						break;
					case ("ratingmin"):
						query = query + " and JUEGO.rating >= " + e.getValue();
						break;
					case ("ratingmax"):
						query = query + " and JUEGO.rating <= " + e.getValue();
						break;
					case ("order"):
						order = e.getValue();
						if (order.equals("rating"))
							rat = true;
						else if (order.equals("lanzamiento"))
							fec = true;
						break;
					case ("type"):
						type = e.getValue();
						break;
					default:
						Logger.log("Se ha introducido una clave invalida");
						break;
					}
				} else if (e.getKey() == null)
					Logger.log("Se ha introducido una clave nula");
				else
					Logger.log("Se ha introducido un valor invalido");
			}
		} else
			Logger.log("Se ha introducido un HashMap nulo");
		/* paginacion */
		if (rat)
			query = query + " AND RATING != 'null' ORDER BY cast(" + order
					+ " as DECIMAL(2,1)) " + type + " limit 5 offset "
					+ (5 * (nPagina - 1));
		else if (fec)
			query = query + " AND LANZAMIENTO != 'null' ORDER BY STR_TO_DATE("
					+ order + ", '%m/%d/%Y') " + type + " limit 5 offset "
					+ (5 * (nPagina - 1));
		else
			query = query + " ORDER BY " + order + " " + type
					+ " limit 5 offset " + (5 * (nPagina - 1));

		ArrayList<Juego> js = new ArrayList<Juego>();
		try {
			Statement st = connection.createStatement(), st2;
			ResultSet resul = st.executeQuery(query);
			Juego j;
			String q;
			ArrayList<String> generos = new ArrayList<String>();
			ResultSet res;
			/* recuperar resultados */
			while (resul.next()) {
				j = new Juego(resul.getLong("id"), resul.getString("titulo"),
						resul.getString("imagen"), resul.getString("resumen"),
						resul.getString("lanzamiento"),
						resul.getString("rating"), generos,
						resul.getInt("precio"), new Plataforma(
								resul.getString("nombre"),
								resul.getString("alias")));

				q = "SELECT * FROM JUEGO_GENERO WHERE id = '" + j.getId() + "'";
				st2 = connection.createStatement();
				res = st2.executeQuery(q);
				while (res.next()) {
					generos.add(res.getString("genero"));
				}
				j.setGenero(generos);
				js.add(j);
				generos = new ArrayList<String>();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		Logger.log("Juegos obtenidos");
		return js;
	}

	/**
	 * @param filtros
	 *            : Pares clave valor que establecen filtros a aplicar a la
	 *            busqueda
	 * @return numero de juegos que cumplen los filtros
	 * 
	 *         Devuelve la cantidad de juegos que cumplen las condiciones
	 *         establecidas en los filtros
	 */
	public int cantidadMultiples(HashMap<String, String> filtros) {
		String query = "select COUNT(DISTINCT JUEGO.id) AS CONTAR "
				+ "from JUEGO, JUEGO_GENERO, JUEGO_PLATAFORMA, PLATAFORMA where "
				+ "JUEGO.id = JUEGO_GENERO.id and JUEGO.id = JUEGO_PLATAFORMA.juego and "
				+ "JUEGO_PLATAFORMA.plataforma = PLATAFORMA.id";
		/* aplicar filtros */
		if (filtros != null) {
			for (Entry<String, String> e : filtros.entrySet()) {
				if (e.getKey() != null && e.getValue() != null
						&& !e.getValue().equals("")) {
					switch (e.getKey()) {
					case ("titulo"):
						query = query + " and JUEGO.titulo LIKE '%"
								+ e.getValue() + "%'";
						break;
					case ("preciomin"):
						query = query + " and JUEGO.precio >= " + e.getValue();
						break;
					case ("preciomax"):
						query = query + " and JUEGO.precio <= " + e.getValue();
						break;
					case ("genero"):
						query = query + " and JUEGO_GENERO.genero = '"
								+ e.getValue() + "'";
						break;
					case ("plataforma"):
						query = query + " and PLATAFORMA.alias = '"
								+ e.getValue() + "'";
						break;
					case ("ratingmin"):
						query = query + " and JUEGO.rating >= " + e.getValue();
						break;
					case ("ratingmax"):
						query = query + " and JUEGO.rating <= " + e.getValue();
						break;
					default:
						Logger.log("Se ha introducido una clave invalida");
						break;
					}
				} else if (e.getKey() == null)
					Logger.log("Se ha introducido una clave nula");
				else
					Logger.log("Se ha introducido un valor invalido");
			}
		} else
			Logger.log("Se ha introducido un HashMap nulo");
		try {
			Statement st = connection.createStatement();
			ResultSet resul = st.executeQuery(query);
			int cantidad = -1;
			/* recuperar resultados */
			while (resul.next()) {
				cantidad = resul.getInt("CONTAR");
			}
			return cantidad;
		} catch (SQLException ex) {
			return -1;
		}
	}

	/**
	 * @param min
	 *            : precio minimo por el que filtrar
	 * @param max
	 *            : precio maximo por el que filtrar
	 * @return una lista (ArrayList) de los juegos cuyo precio se encuentra
	 *         entre @param min y @param max
	 * 
	 *         Devuelve los juegos que se encuentran en un rango de precios
	 */
	public ArrayList<Juego> listarJuegosRangoPrecios(int min, int max) {
		return listarJuegos(" AND precio <= '" + max + "' AND precio >= '"
				+ min + "'");
	}

	/**
	 * @param min
	 *            : valoracion minima
	 * @param max
	 *            : valoracion maxima
	 * @return una lista (ArrayList) de los juegos cuyo rating se encuentre
	 *         entre @param min y @param max
	 * 
	 *         Devuelve los juegos que se encuentran en un rango de rating
	 */
	public ArrayList<Juego> listarJuegosRangoRating(String min, String max) {
		return listarJuegos(" AND rating <= '" + max + "' AND rating >= '"
				+ min + "'");
	}

	/**
	 * @param genero
	 *            : genero por el que filtrar
	 * @return una lista (ArrayList) de los juegos cuyo genero coincide con @param
	 *         genero
	 * 
	 *         Devuelve los juegos de un genero
	 */
	public ArrayList<Juego> listarJuegosGenero(String genero) {
		return listarJuegos(" AND JUEGO_GENERO.id = JUEGO.id AND genero = '"
				+ genero + "'");
	}

	/**
	 * @param nomP
	 *            : nombre de la plataforma
	 * @return una lista (ArrayList) de los juegos cuya plataforma coincide con @param
	 *         nomP
	 * 
	 *         Devuelve los juegos de una plataforma
	 */
	public ArrayList<Juego> listarJuegosPlataformaNombre(String nomP) {
		return listarJuegos(" AND PLATAFORMA.nombre = '" + nomP + "'");
	}

	/**
	 * @param alP
	 *            : alias de la plataforma
	 * @return una lista (ArrayList) de los juegos cuya plataforma coincide con @param
	 *         alP
	 * 
	 *         Devuelve los juegos de una plataforma
	 */
	public ArrayList<Juego> listarJuegosPlataformaAlias(String alP) {
		return listarJuegos(" AND PLATAFORMA.alias = '" + alP + "'");
	}

	/**
	 * @param id
	 *            : identificador del juego
	 * @return la informacion del juego cuyo identificador coincide con @param
	 *         id, si este no existe, devuelve un juego vacio.
	 * 
	 *         Devuelve un juego
	 */
	public Juego listarJuego(long id) {
		ArrayList<Juego> ar = listarJuegos(" AND JUEGO.id = '" + id + "'");
		if (ar.isEmpty())
			return null;
		else
			return ar.get(0);
	}

	/**
	 * @return una lista (ArrayList) con todos los juegos disponibles en la Base
	 *         de Datos (MySQL)
	 * 
	 *         Devuelve todos los juegos
	 */
	public ArrayList<Juego> listarTodosJuegos() {
		return listarJuegos("");
	}

	/**
	 * @param nombre
	 *            : nombre de la plataforma
	 * @return la informacion asociada a la plataforma cuyo nombre coincida con @param
	 *         nombre
	 * 
	 *         Devuelve la informacion asociada a una plataforma
	 */
	public Plataforma listarPlataformaNombre(String nombre) {
		return listarPlataforma(" WHERE nombre = '" + nombre + "'");
	}

	/**
	 * @param alias
	 *            : alias de la plataforma
	 * @return la informacion asociada a la plataforma cuyo alias coincida con @param
	 *         alias
	 * 
	 *         Devuelve la informacion asociada a una plataforma
	 */
	public Plataforma listarPlataformaAlias(String alias) {
		return listarPlataforma(" WHERE alias = '" + alias + "'");
	}

	/**
	 * @param id
	 *            : identificador de la plataforma
	 * @return la informacion asociada a la plataforma cuyo identificador con @param
	 *         id
	 * 
	 *         Devuelve la informacion asociada a una plataforma
	 */
	public Plataforma listarPlataformaId(long id) {
		return listarPlataforma(" WHERE id = '" + id + "'");
	}

	/**
	 * @return una lista (ArrayList) de todas las plataformas existentes en la
	 *         Base de Datos
	 * 
	 *         Devuelve todas las plataformas
	 */
	public ArrayList<Plataforma> listarTodasPlataformas() {
		Logger.log("Accediendo a la BD para listar los juegos de todas las plataformas...");
		String q = "SELECT * FROM PLATAFORMA";
		Statement st;
		Plataforma p = null;
		ArrayList<Plataforma> ps = new ArrayList<Plataforma>();
		try {
			st = connection.createStatement();
			ResultSet resul = st.executeQuery(q);
			while (resul.next()) {
				p = new Plataforma(resul.getLong("id"));
				p.setAlias(resul.getString("alias"));
				p.setNombre(resul.getString("nombre"));
				ps.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Logger.log("Juegos obtenidos");
		return ps;
	}

	/**
	 * @param id
	 *            : identificador del juego a eliminar de la Base de Datos
	 * 
	 *            Borra una juego de la base de datos
	 */
	public void borrarJuego(long id) {
		Logger.log("Accediendo a la BD para borrar con id " + id + "...");
		if (id > 0) {
			if (listarJuego(id) != null) {
				try {
					String query = "DELETE FROM JUEGO_GENERO WHERE id = " + id
							+ ";";
					Statement st = null;
					try {
						st = connection.createStatement();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					st.execute(query);
					query = "DELETE FROM JUEGO_PLATAFORMA WHERE juego = " + id
							+ ";";
					st.execute(query);
					query = "DELETE FROM JUEGO WHERE id = " + id + ";";
					st.execute(query);
					Logger.log("Juego con id " + id + " borrado");
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			} else {
				Logger.log("Error de existencia");
				// Mostrar mensaje en el log
			}

		} else {
			Logger.log("Error numero negativo");
			// Mostrar mensaje en el log
		}
	}

	/**
	 * @param juego
	 *            : informacion del juego a insertar
	 * 
	 *            Insertar un juego en la base de datos
	 */
	public void insertarJuego(Juego juego) {

		// Si el titulo no es vacio ni nulo, el precio mayor que cero, la
		// plataforma no se nula ni sus campos vacios,
		// se comienza a agregar
		if (juego.getTitulo() != null
				&& !juego.getTitulo().equals("")
				&& juego.getPrecio() > 0
				&& juego.getPlataforma() != null
				&& (juego.getPlataforma().getAlias().length() != 0 || juego
						.getPlataforma().getNombre().length() != 0)) {
			Logger.log("Accediendo a la BD para insertar el juego "
					+ juego.getTitulo() + "...");
			// formateo de la descripcion, si es necesario
			if (juego.getDescripcion() != null
					&& juego.getDescripcion().length() > 2500)
				juego.setDescripcion(juego.getDescripcion().substring(0, 2497)
						+ "...");

			/* insercion de la informacion en la tabla de juegos */

			String queryString = "INSERT INTO JUEGO "
					+ "(id,titulo,imagen,precio,resumen,lanzamiento,rating) "
					+ "VALUES (?,?,?,?,?,?,?)";
			try {
				PreparedStatement preparedStatement = connection
						.prepareStatement(queryString);

				preparedStatement.setLong(1, juego.getId());
				preparedStatement.setString(2, juego.getTitulo());
				preparedStatement.setString(3, juego.getImagen());
				preparedStatement.setLong(4, juego.getPrecio());
				preparedStatement.setString(5, juego.getDescripcion());
				preparedStatement.setString(6, juego.getLanzamiento());
				preparedStatement.setString(7, juego.getRating());

				preparedStatement.execute();
			} catch (SQLException ex) {
				if (ex.getSQLState().startsWith("23"))
					Logger.log("Entrada en juego duplicada");
				else
					ex.printStackTrace();
			}

			/* insercion de la informacion en la tabla de generos */
			for (String g : juego.getGenero()) {
				queryString = "INSERT INTO JUEGO_GENERO (id,genero) VALUES (?,?)";

				try {
					PreparedStatement preparedStatement = connection
							.prepareStatement(queryString);

					preparedStatement.setLong(1, juego.getId());
					preparedStatement.setString(2, g);

					preparedStatement.execute();
				} catch (SQLException ex) {
					if (ex.getSQLState().startsWith("23"))
						Logger.log("Entrada en juego_genero duplicada");
					else
						ex.printStackTrace();
				}
			}

			/* insercion de la informacion en la tabla juego_plataforma */

			queryString = "INSERT INTO JUEGO_PLATAFORMA (juego,plataforma) VALUES (?,?)";

			try {
				PreparedStatement preparedStatement = connection
						.prepareStatement(queryString);

				preparedStatement.setLong(1, juego.getId());
				preparedStatement.setLong(2, juego.getPlataforma().getId());

				preparedStatement.execute();
			} catch (SQLException ex) {
				if (ex.getSQLState().startsWith("23"))
					Logger.log("Entrada en juego_plataforma duplicada");

				else
					ex.printStackTrace();
			}
			Logger.log("Juego " + juego.getTitulo() + " insertado");
		}
	}

	/**
	 * @param juego
	 *            : nueva informacion del juego a actualizar
	 * 
	 *            Actualiza un juego en la base de datos
	 */
	public void actualizarJuego(Juego juego) {

		// Si el titulo no es vacio ni nulo, el precio mayor que cero, la
		// plataforma no se nula ni
		// sus campos vacios, se comienza a agregar
		if (juego.getTitulo() != null
				&& !juego.getTitulo().equals("")
				&& juego.getPrecio() > 0
				&& juego.getPlataforma() != null
				&& (juego.getPlataforma().getAlias().length() != 0 || juego
						.getPlataforma().getNombre().length() != 0)) {
			Logger.log("Accediendo a la BD para actualizar el juego "
					+ juego.getTitulo() + "...");
			if (juego.getDescripcion() != null
					&& juego.getDescripcion().length() > 2500)
				juego.setDescripcion(juego.getDescripcion().substring(0, 2497)
						+ "...");

			String queryString = "UPDATE JUEGO, JUEGO_PLATAFORMA "
					+ "SET titulo = ?,imagen = ?,precio = ?,resumen = ?,lanzamiento = ?,rating = ?, "
					+ "JUEGO_PLATAFORMA.plataforma = ? " + "WHERE JUEGO.id = '"
					+ juego.getId() + "' AND JUEGO.id = JUEGO_PLATAFORMA.juego";
			try {
				PreparedStatement preparedStatement = connection
						.prepareStatement(queryString);

				preparedStatement.setString(1, juego.getTitulo());
				preparedStatement.setString(2, juego.getImagen());
				preparedStatement.setLong(3, juego.getPrecio());
				preparedStatement.setString(4, juego.getDescripcion());
				preparedStatement.setString(5, juego.getLanzamiento());
				preparedStatement.setString(6, juego.getRating());
				preparedStatement.setLong(7, juego.getPlataforma().getId());
				preparedStatement.execute();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			for (String g : juego.getGenero()) {
				String query = "DELETE FROM JUEGO_GENERO WHERE id = "
						+ juego.getId() + ";";
				try {
					Statement st = connection.createStatement();
					st.execute(query);

					queryString = "INSERT INTO JUEGO_GENERO (id,genero) VALUES (?,?)";

					PreparedStatement preparedStatement = connection
							.prepareStatement(queryString);

					preparedStatement.setLong(1, juego.getId());
					preparedStatement.setString(2, g);

					preparedStatement.execute();
				} catch (SQLException ex) {
					if (ex.getSQLState().startsWith("23"))
						Logger.log("Entrada en juego_genero duplicada");
					else
						ex.printStackTrace();
				}
			}
			Logger.log("Juego " + juego.getTitulo() + " actualizado");
		}
	}

	/**
	 * @param p
	 *            : nueva informacion de la plataforma a actualizar
	 * 
	 *            Actualiza una plataforma en la base de datos
	 */
	public void actualizarPlataforma(Plataforma p) {
		Logger.log("Accediendo a la BD para actualizar la plataforma "
				+ p.getAlias() + "...");
		String queryString = "UPDATE PLATAFORMA " + "SET nombre = ?,alias = ? "
				+ "WHERE id = ?";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(queryString);

			preparedStatement.setString(1, p.getNombre());
			preparedStatement.setString(2, p.getAlias());
			preparedStatement.setLong(3, p.getId());

			preparedStatement.execute();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		Logger.log("Plataforma " + p.getAlias() + " actualizada");
	}

	/**
	 * @return el identificador del ultimo juego
	 * 
	 *         Obtiene el identificador del ultimo juego
	 */
	public long obtenerUltimoIdJuego() {
		Logger.log("Accediendo a la BD para obtener el id del ultimo juego...");
		String q = "SELECT MAX(id) AS mid FROM JUEGO";
		Statement st;
		long id = -1;
		try {
			st = connection.createStatement();
			ResultSet resul = st.executeQuery(q);
			while (resul.next())
				id = resul.getLong("mid");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Logger.log("Id obtenido");
		return id;
	}

	/**
	 * @param query
	 *            : datos adicionales para el filtrado de las consultas
	 * @return una lista (ArrayList) de todos los juegos de la Base de Datos que
	 *         coinciden con @param query
	 * 
	 *         Devuelve una lista de juegos que cumplen los requisitos
	 *         especificados en @param query
	 */
	private ArrayList<Juego> listarJuegos(String query) {
		Logger.log("Accediendo a la BD para obtener juegos bajo ciertos criterios...");
		String q = "SELECT * FROM JUEGO, PLATAFORMA, JUEGO_PLATAFORMA WHERE "
				+ "JUEGO.id = JUEGO_PLATAFORMA.juego AND PLATAFORMA.id = JUEGO_PLATAFORMA.plataforma "
				+ query + " LIMIT 5";
		Statement st, st2;
		ArrayList<Juego> js = new ArrayList<Juego>();
		try {
			st = connection.createStatement();
			ResultSet resul = st.executeQuery(q);
			Juego j;
			ArrayList<String> generos = new ArrayList<String>();
			ResultSet res;
			while (resul.next()) {
				j = new Juego(resul.getLong("id"), resul.getString("titulo"),
						resul.getString("imagen"), resul.getString("resumen"),
						resul.getString("lanzamiento"),
						resul.getString("rating"), generos,
						resul.getInt("precio"), new Plataforma(
								resul.getString("nombre"),
								resul.getString("alias")));
				q = "SELECT * FROM JUEGO_GENERO WHERE id = '" + j.getId() + "'";
				st2 = connection.createStatement();
				res = st2.executeQuery(q);
				while (res.next()) {
					generos.add(res.getString("genero"));
				}
				j.setGenero(generos);

				js.add(j);
				generos = new ArrayList<String>();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		Logger.log("Juegos obtenidos");
		return js;
	}

	/**
	 * @param query
	 *            : datos adicionales para la obtencion de la plataforma
	 * @return la plataforma cuya informacion coincida con @param query
	 * 
	 *         Devuelve una plataforma que cumple los requisitos de @param query
	 */
	private Plataforma listarPlataforma(String query) {
		Logger.log("Accediendo a la BD para obtener la plataforma " + query
				+ "...");
		String q = "SELECT * FROM PLATAFORMA" + query;
		Statement st;
		Plataforma p = null;
		try {
			st = connection.createStatement();
			ResultSet resul = st.executeQuery(q);
			while (resul.next()) {
				p = new Plataforma(resul.getLong("id"),
						resul.getString("nombre"), resul.getString("alias"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Logger.log("Plataforma " + query + " obtenida");
		return p;
	}
}
