/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author jakerxd
 */
public class PruebaQytetet 
		
{
	private static Qytetet juego= Qytetet.getQytetet();
	private static Scanner in = new Scanner (System.in);
	
	private static ArrayList<Sorpresa> getSorpresasPositivas ()
	{
		ArrayList<Sorpresa> cartasPositivas = new ArrayList<>();
		
		for (Sorpresa carta : juego.getMazo()) 
			if(carta.getValor() > 0)
				cartasPositivas.add(carta);
		
		return cartasPositivas;
	}
	
	private static ArrayList<Sorpresa> getSorpresasIracasilla ()
	{
		ArrayList<Sorpresa> cartasIra = new ArrayList<>();
		
		for (Sorpresa carta : juego.getMazo()) 
			if(carta.getTipo() == TipoSorpresa.IRACASILLA)
				cartasIra.add(carta);
		
		return cartasIra;
	}
	
	private static ArrayList<Sorpresa> getSorpresasTipo (TipoSorpresa tipo)
	{
		ArrayList<Sorpresa> cartasTipo = new ArrayList<>();
		
		for (Sorpresa carta : juego.getMazo()) 
			if(carta.getTipo() == tipo)
				cartasTipo.add(carta);
		
		return cartasTipo;
	}
	
	private static void printSorpresas(ArrayList<Sorpresa> lista)
	{
		for (int i=0; i<lista.size(); i++)
			System.out.println(lista.get(i).toString());
		System.out.println("\n");
	}
	
	static ArrayList<String> getNombreJugadores()
	{
		int num_jugadores;
		ArrayList<String> nombres = new ArrayList();
		
		System.out.print("Introduzca el número de jugadores: ");
		num_jugadores = in.nextInt();
		in.nextLine();
		
		for (int i=0; i<num_jugadores; i++)
		{
			System.out.print("Introduzca el nombre del jugador " + (i + 1) + " : ");
			nombres.add(in.nextLine());
		}
		
		return nombres;
	}
	
	public static void main(String[] args)
	{
		juego.inicializarJuego(getNombreJugadores());
		
		do
		{
			juego.siguienteJugador();
			juego.jugar();
			
			if (juego.getEstadoJuego() == EstadoJuego.JA_CONSORPRESA)
				juego.aplicarSorpresa();
			else if (juego.getEstadoJuego() == EstadoJuego.JA_PUEDECOMPRAROGESTIONAR)
				juego.comprarTituloPropiedad();
			else if (juego.getEstadoJuego() == EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD)
				juego.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD);
		} while (juego.getEstadoJuego() != EstadoJuego.ALGUNJUGADORENBANCARROTA);
		
		/*juego.mover(2);
		
		juego.comprarTituloPropiedad();
		juego.hipotecarPropiedad(2);
		juego.cancelarHipoteca(2);
		juego.venderPropiedad(2);
		juego.obtenerRanking();*/
		
		for (int i=0; i < juego.getJugadores().size(); i++)
			System.out.println("Jugador en posicion " + (i+1) + ": " + juego.getJugadores().get(i).toString());
	}
}