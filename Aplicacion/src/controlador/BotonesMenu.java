/*
 * SOFTBASE - GRUPO 10
 * AUTORES:
 * 		-Alberto Blasco
 * 		-Diego Galvez
 * 		-Patricia Lazaro
 * 		-Alejandro Marquez
 * 		-Alejandro Royo
 * 		-Jaime Ruiz-Borau
 * DESCRIPCION: clase que contiene la creacion de botones del menu de la
 * 				pantalla principal, encapsulando su comportamiento
 */

package controlador;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import vista.Principal;

public class BotonesMenu {

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de New 3DS
	 */
	public static JButton menuN3DS() {
		JButton n3ds = new JButton("");
		n3ds.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		n3ds.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/3ds.png")));
		n3ds.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		n3ds.setBounds(596, 322, 246, 125);

		n3ds.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return n3ds;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de la Wii
	 */
	public static JButton menuWii() {
		JButton wii = new JButton("");
		wii.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		wii.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/wii.png")));
		wii.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		wii.setBounds(313, 322, 246, 125);

		wii.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return wii;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de la WiiU
	 */
	public static JButton menuWiiU() {
		JButton wiiu = new JButton("");
		wiiu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		wiiu.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/wiiu.png")));
		wiiu.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		wiiu.setBounds(30, 322, 246, 125);

		wiiu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return wiiu;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de PC
	 */
	public static JButton menuPC() {
		JButton pc = new JButton("");
		pc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		pc.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/pc.png")));
		pc.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		pc.setBounds(596, 176, 246, 125);

		pc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return pc;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de Xbox 360
	 */
	public static JButton menuX360() {
		JButton x360 = new JButton("");
		x360.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		x360.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/xbox 360.png")));
		x360.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		x360.setBounds(313, 176, 246, 125);

		x360.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return x360;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de Xbox ONE
	 */
	public static JButton menuXONE() {
		JButton xone = new JButton("");
		xone.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		xone.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/xbox One.png")));
		xone.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		xone.setBounds(30, 176, 246, 125);

		xone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return xone;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de PlayStation Vita
	 */
	public static JButton menuVita() {
		JButton vita = new JButton("");
		vita.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		vita.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/psvita.png")));
		vita.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		vita.setBounds(596, 34, 246, 125);

		vita.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return vita;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de PlayStation 3
	 */
	public static JButton menuPS3() {
		JButton ps3 = new JButton("");
		ps3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ps3.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/ps3.png")));
		ps3.setBorder(new BevelBorder(BevelBorder.RAISED, Color.BLACK,
				Color.BLACK, Color.BLACK, Color.BLACK));
		ps3.setBounds(313, 34, 246, 125);

		ps3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return ps3;
	}

	/**
	 * @return un boton que redirige a la pantalla de listado de juegos de PlayStation 4
	 */
	public static JButton menuPS4() {
		JButton ps4 = new JButton("");
		ps4
				.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ps4.setBorder(new BevelBorder(BevelBorder.RAISED,
				Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		ps4.setIcon(new ImageIcon(Principal.class
				.getResource("/Imagenes/Menu/ps4.png")));
		ps4.setBounds(30, 34, 246, 125);

		ps4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return ps4;
	}
}
