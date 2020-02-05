/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorqytetet;

import java.util.ArrayList;
import modeloqytetet.Qytetet;
import modeloqytetet.EstadoJuego;
import modeloqytetet.MetodoSalirCarcel;

public class ControladorQytetet {
	private static final ControladorQytetet controlador = new ControladorQytetet();
	private ArrayList<String> nombreJugadores = new ArrayList(); 
	private static Qytetet juego= Qytetet.getQytetet();
	
	public static ControladorQytetet getControladorQytetet()
	{
		return controlador;
	}
	
	public void setNombreJugadores(ArrayList<String> nombreJugadores)
	{
		this.nombreJugadores = nombreJugadores;
	}
	
	public ArrayList<Integer> obtenerOperacionesJuegoValidas()
	{
		ArrayList<Integer> validas = new ArrayList();
		EstadoJuego estado = juego.getEstadoJuego();
		
		if (juego.getJugadores().isEmpty())
		{
			validas.add(OpcionMenu.INICIARJUEGO.ordinal());
		}
		else
		{
			switch (estado) {
				case JA_PREPARADO:
					validas.add(OpcionMenu.JUGAR.ordinal());
					break;
				case JA_PUEDECOMPRAROGESTIONAR:
					validas.add(OpcionMenu.PASARTURNO.ordinal());
					validas.add(OpcionMenu.COMPRARTITULOPROPIEDAD.ordinal());
					validas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
					validas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
					validas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
					validas.add(OpcionMenu.EDIFICARCASA.ordinal());
					validas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
					break;
				case JA_PUEDEGESTIONAR:
					validas.add(OpcionMenu.PASARTURNO.ordinal());
					validas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
					validas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
					validas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
					validas.add(OpcionMenu.EDIFICARCASA.ordinal());
					validas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
					break;
				case JA_ENCARCELADO:
					validas.add(OpcionMenu.PASARTURNO.ordinal());
					break;
				case JA_ENCARCELADOCONOPCIONDELIBERTAD:
					validas.add(OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
					validas.add(OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.ordinal());
					break;
				case JA_CONSORPRESA:
					validas.add(OpcionMenu.APLICARSORPRESA.ordinal());
					break;
				case ALGUNJUGADORENBANCARROTA:
					validas.add(OpcionMenu.OBTENERRANKING.ordinal());
					break;
				default:
					break;
			}
			validas.add(OpcionMenu.TERMINARJUEGO.ordinal());
			validas.add(OpcionMenu.MOSTRARJUGADORACTUAL.ordinal());
			validas.add(OpcionMenu.MOSTRARJUGADORES.ordinal());
			validas.add(OpcionMenu.MOSTRARTABLERO.ordinal());
		}
		
		return validas;
	}
	
	public boolean necesitaElegirCasilla(int opcionMenu)
	{
		return (opcionMenu == OpcionMenu.HIPOTECARPROPIEDAD.ordinal() ||
				opcionMenu == OpcionMenu.CANCELARHIPOTECA.ordinal() ||
				opcionMenu == OpcionMenu.EDIFICARCASA.ordinal() ||
				opcionMenu == OpcionMenu.EDIFICARHOTEL.ordinal() ||
				opcionMenu == OpcionMenu.VENDERPROPIEDAD.ordinal());
	}
	
	public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu)
	{
		OpcionMenu opcion = OpcionMenu.values()[opcionMenu];
		ArrayList<Integer> casillasValidas = new ArrayList();
		
		if (opcion == OpcionMenu.HIPOTECARPROPIEDAD || 
			opcion == OpcionMenu.EDIFICARCASA ||
			opcion == OpcionMenu.EDIFICARHOTEL ||
			opcion == OpcionMenu.VENDERPROPIEDAD)
				casillasValidas = juego.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
		else if (opcion == OpcionMenu.CANCELARHIPOTECA)
			casillasValidas = juego.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
		
		return casillasValidas;
	}
	
	public String realizarOperacion(int opcionElegida, int casillaElegida)
	{
		OpcionMenu opcion = OpcionMenu.values()[opcionElegida];
		
		switch (opcion) {
			case INICIARJUEGO:
				juego.inicializarJuego(nombreJugadores);
				return "Empieza la partida de MonoPoly. Hasta 6horas de perder tu tiempo!";
				
			case JUGAR:
				juego.jugar();
				return "Tirada = " + juego.getValorDado() + ", cae en " + juego.getJugadorActual().getCasillaActual();
				
			case APLICARSORPRESA:
				juego.aplicarSorpresa();
				return "SORPRESA!!!1!! " + juego.getCartaActual().toString();
				
			case INTENTARSALIRCARCELPAGANDOLIBERTAD:
				if(juego.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD))
					return "Sale de la cárcel pagando la fianza";
				else
					return "El jugador no tiene saldo suficiente para salir de la cárcel";
				
			case INTENTARSALIRCARCELTIRANDODADO:
				if(juego.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO))
					return "Sale de la cárcel con una tirada mayor que 4";
				else
					return "No sale de la cárcel por una tirada menor que 5";
				
			case COMPRARTITULOPROPIEDAD:
				if(juego.comprarTituloPropiedad())
					return "Titulo de propiedad adquirido";
				else
					return "Saldo insuficiente para adquirir propiedad";
				
			case HIPOTECARPROPIEDAD:
				juego.hipotecarPropiedad(casillaElegida);
				return "Propiedad hipotecada";
				
			case CANCELARHIPOTECA:
				if(juego.cancelarHipoteca(casillaElegida))
					return "Hipoteca cancelada";
				else
					return "Saldo insuficiente para cancelar hipoteca";
				
			case EDIFICARCASA:
				if(juego.edificarCasa(casillaElegida))
					return "Casa edificada";
				else
					return "Saldo insuficiente o 4 casas construidas";
				
			case EDIFICARHOTEL:
				if(juego.edificarHotel(casillaElegida))
					return "Hotel edificado";
				else
					return "Saldo insuficiente o no hay 4 casas o hay 8 hoteles";
				
			case VENDERPROPIEDAD:
				juego.venderPropiedad(casillaElegida);
				return "Propiedad vendida";
				
			case PASARTURNO:
				juego.siguienteJugador();
				return "Pasa el turno";
				
			case OBTENERRANKING:
				juego.obtenerRanking();
				return juego.getJugadores().toString();
				
			case TERMINARJUEGO:
				System.exit(0);
				
			case MOSTRARJUGADORACTUAL:
				return juego.getJugadorActual().toString();
				
			case MOSTRARJUGADORES:
				return juego.getJugadores().toString();
				
			case MOSTRARTABLERO:
				return juego.getTablero().toString();
			default:
				
		}
		return "Opcion Incorrecta";
	}
}
