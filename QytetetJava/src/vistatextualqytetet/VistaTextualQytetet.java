/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistatextualqytetet;

import controladorqytetet.ControladorQytetet;
import controladorqytetet.OpcionMenu;
import java.util.ArrayList;
import java.util.Scanner;

public class VistaTextualQytetet {
	private static ControladorQytetet controlador= ControladorQytetet.getControladorQytetet();
	private static Scanner in = new Scanner (System.in);
	
	public ArrayList<String> obtenerNombreJugadores()
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
	
	public int elegirCasilla(int opcionMenu)
	{
		ArrayList<Integer> validas = controlador.obtenerCasillasValidas(opcionMenu);
		ArrayList<String> validasString = new ArrayList();
		int resultado;
		String numero;
		
		if (validas.isEmpty())
			return -1;
		else
		{
			for (Integer valida : validas) {
				numero = valida.toString();
				
				validasString.add(numero);
				System.out.println(numero + ": " + "Casilla Valida");
			}
		}
		
		return Integer.parseInt(leerValorCorrecto(validasString));
	}
	
	public String leerValorCorrecto(ArrayList<String> valoresCorrectos)
	{
		String valor;
		Boolean correcto = false;
		
		do
		{
			System.out.print("Elija una opción: ");
			valor = in.nextLine();
			
			if (valoresCorrectos.contains(valor))
				correcto = true;
			else
				System.out.println("Opción incorrecta");

		} while (!correcto);
		
		return valor;
	}
	
	public int elegirOperacion()
	{
		ArrayList<Integer> operaciones = controlador.obtenerOperacionesJuegoValidas();
		ArrayList<String> operacionesString = new ArrayList();
		String numero;
		
		for (Integer operacion : operaciones) {
			numero = operacion.toString();
			
			operacionesString.add(numero);
			System.out.println(numero + ": " + OpcionMenu.values()[operacion]);
		}
		
		return Integer.parseInt(leerValorCorrecto(operacionesString));
	}
	
	public static void main(String[] args)
	{
		VistaTextualQytetet ui = new VistaTextualQytetet();
		
		controlador.setNombreJugadores(ui.obtenerNombreJugadores());
		int operacionElegida, casillaElegida = 0;
		boolean necesitaElegirCasilla;

		do {
			operacionElegida = ui.elegirOperacion();
			necesitaElegirCasilla = ui.controlador.necesitaElegirCasilla(operacionElegida);
			if (necesitaElegirCasilla)
				casillaElegida = ui.elegirCasilla(operacionElegida);
			if (!necesitaElegirCasilla || casillaElegida >= 0)
				System.out.println(ui.controlador.realizarOperacion(operacionElegida, casillaElegida));
		} while (true);
	}
}
