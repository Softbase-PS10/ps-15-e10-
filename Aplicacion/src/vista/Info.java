/*
 * SOFTBASE - GRUPO 10
 * AUTORES:
 * 		-Alberto Blasco
 * 		-Diego Galvez
 * 		-Patricia Lazaro
 * 		-Alejandro Marquez
 * 		-Alejandro Royo
 * 		-Jaime Ruiz-Borau
 * DESCRIPCION: clase que contiene el diseno de la pantalla
 * 				de informacion de un juego de la aplicacion de
 * 				ESTIM
 */

package vista;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import modelo.Juego;
import modelo.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import controlador.BotonesInfo;

public class Info {

	/* declaracion de variables */
	private JFrame frmInformacinDelProduc;
	private static Juego juego;
	private ArrayList<Juego> cesta;

	/* declaracion de metodos y funciones */

	/**
	 * Devuelve un objeto de tipo Info e inicializa y dibuja la pantalla de
	 * informacion del objeto
	 * 
	 * @param fr
	 *            : Frame principal donde se dibuja la aplicacion
	 * @param juego
	 *            : Objeto juego del que mostrar informacion
	 * @param cesta
	 *            : Cesta de juegos anadidos al carro
	 */
	public static void main(final JFrame fr, Juego jue,
			final ArrayList<Juego> cesta) {
		juego = jue;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Info window = new Info(fr, juego, cesta);
					window.frmInformacinDelProduc.repaint();
					window.frmInformacinDelProduc.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Devuelve un objeto de tipo Info e inicializa y dibuja la pantalla de
	 * informacion del objeto
	 * 
	 * @param fr
	 *            : Frame principal donde se dibuja la aplicacion
	 * @param juego
	 *            : Objeto juego del que mostrar informacion
	 * @param cesta
	 *            : Cesta de juegos anadidos al carro
	 */
	public Info(JFrame fr, Juego juego, ArrayList<Juego> cesta) {
		this.cesta = cesta;
		frmInformacinDelProduc = fr;
		frmInformacinDelProduc.getContentPane().removeAll();
		initialize(juego);
	}

	/**
	 * Inicializa la pantalla de informacion del juego
	 * 
	 * @param juego
	 *            : Objeto juego del que mostrar informacion
	 */
	private void initialize(Juego juego) {
		Logger.log("Inicializando informacion del juego...");
		frmInformacinDelProduc.setTitle("Product information - Estim");
		frmInformacinDelProduc.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Info.class.getResource("/Imagenes/E.png")));

		// Cabecera
		JPanel cabecera = BotonesInfo
				.getCabecera(frmInformacinDelProduc, cesta);
		frmInformacinDelProduc.getContentPane().add(cabecera);

		// Panel de busqueda
		JTextField txtBuscar = BotonesInfo.getCuadroBusqueda();
		cabecera.add(txtBuscar);

		// Lupa de busqueda
		JPanel lupaBuscar = BotonesInfo.getLupaBuscar(txtBuscar, cesta);
		cabecera.add(lupaBuscar);

		// Opciones
		JPanel opciones = BotonesInfo.getOpciones(juego, null,
				frmInformacinDelProduc, cesta);
		cabecera.add(opciones);

		// Carrito
		JPanel carrito = BotonesInfo.getCarrito(cesta);
		cabecera.add(carrito);

		// Categorias
		JPanel categorias = BotonesInfo.getCategorias(frmInformacinDelProduc,
				cesta);
		frmInformacinDelProduc.getContentPane().add(categorias);

		// Imagen
		JLabel lblNewLabel = BotonesInfo.getImagenLabel(juego.getImagen());
		frmInformacinDelProduc.getContentPane().add(lblNewLabel);

		// Titulo
		JLabel Titulo = BotonesInfo.getTituloLabel(juego.getTitulo());
		frmInformacinDelProduc.getContentPane().add(Titulo);

		// Precio
		JLabel Precio = BotonesInfo.getPrecioLabel();
		frmInformacinDelProduc.getContentPane().add(Precio);

		// Valor del precio
		JLabel RespPrecio = BotonesInfo.getPrecioValueLabel(juego.getPrecio());
		frmInformacinDelProduc.getContentPane().add(RespPrecio);

		// Lanzamiento
		JLabel Anio = BotonesInfo.getLanzamientoLabel();
		frmInformacinDelProduc.getContentPane().add(Anio);

		// Valor del lanzamiento
		JLabel RespAnio = BotonesInfo.getLanzamientoValueLabel(juego
				.getLanzamiento());
		frmInformacinDelProduc.getContentPane().add(RespAnio);

		// Plataforma
		JLabel Plataforma = BotonesInfo.getPlataformaLabel();
		frmInformacinDelProduc.getContentPane().add(Plataforma);

		// Valor de la plataforma
		JLabel RespPlataforma = BotonesInfo.getPlataformaValueLabel(juego
				.getPlataforma().getNombre());
		frmInformacinDelProduc.getContentPane().add(RespPlataforma);

		// Genero
		JLabel Genero = BotonesInfo.getGeneroLabel();
		frmInformacinDelProduc.getContentPane().add(Genero);

		// Valor del genero
		JLabel RespGenero = BotonesInfo.getGeneroValueLabel(juego.getGenero()
				.toString()
				.substring(1, juego.getGenero().toString().length() - 1));
		frmInformacinDelProduc.getContentPane().add(RespGenero);

		// Descripcion
		JLabel Descripcion = BotonesInfo.getDescripcionLabel();
		frmInformacinDelProduc.getContentPane().add(Descripcion);

		// Valor de la descripcion
		JTextArea RespDescripcion = BotonesInfo.getDescripcionValueLabel(juego
				.getDescripcion());
		frmInformacinDelProduc.getContentPane().add(RespDescripcion);

		// Valoracion
		JLabel RespValoracion = BotonesInfo.getValoracionLabel(juego
				.getRating());
		frmInformacinDelProduc.getContentPane().add(RespValoracion);

		// Boton carro
		JButton Carro = BotonesInfo.getBotonCarro(frmInformacinDelProduc,
				juego, cesta);
		frmInformacinDelProduc.getContentPane().add(Carro);

		// Label fondo
		JLabel Fondo = BotonesInfo.getFondo();
		frmInformacinDelProduc.getContentPane().add(Fondo);

		Logger.log("Informacion del juego inicializada");
	}
}
