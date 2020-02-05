/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;

/**
 *
 * @author jakerxd
 */
public class Tablero {
	private ArrayList<Casilla> casillas;
	private Casilla carcel;

	public Tablero()
	{
		inicializar();
	}
	
	ArrayList<Casilla> getCasillas() {
		return casillas;
	}

	Casilla getCarcel() {
		return carcel;
	}
	
	// precondicion: numeroCasilla > 0 && numeroCasilla < numeroMaximo
	Casilla obtenerCasillaNumero(int numeroCasilla)
	{
		return casillas.get(numeroCasilla);
	}
	
	Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento)
	{
		return obtenerCasillaNumero((casilla.getNumeroCasilla() + desplazamiento) % 20);
	}
	
	boolean esCasillaCarcel(int numeroCasilla)
	{
		return numeroCasilla == carcel.getNumeroCasilla();
	}
	
	private void inicializar(){
		casillas=new ArrayList();
		
		casillas.add(new OtraCasilla(0, 1000, TipoCasilla.PARKING));
		casillas.add(new Calle(1, new TituloPropiedad("Calle1", 500, 50, (float) 0.13, 150, 250)));
		casillas.add(new Calle(2, new TituloPropiedad("Calle2", 550, 50, (float) 0.12, 200, 260)));
		casillas.add(new Calle(3, new TituloPropiedad("Calle3", 520, 55, (float) 0.11, 180, 250)));
		casillas.add(new OtraCasilla(4, 0, TipoCasilla.SORPRESA));
		
		// Preguntar si se copia o se apunta
		carcel = new OtraCasilla(5, 0, TipoCasilla.CARCEL);
		casillas.add(carcel);
		casillas.add(new Calle(6, new TituloPropiedad("Calle4", 720, 70, (float) 0.13, 450, 360)));
		casillas.add(new Calle(7, new TituloPropiedad("Calle5", 800, 80, (float) 0.12, 500, 400)));
		casillas.add(new OtraCasilla(8, -200, TipoCasilla.IMPUESTO));
		casillas.add(new Calle(9, new TituloPropiedad("Calle6", 750, 75, (float) 0.15, 500, 420)));
		
		casillas.add(new Calle(10, new TituloPropiedad("Calle7", 600, 95, (float) 0.14, 700, 425)));
		casillas.add(new OtraCasilla(11, 0, TipoCasilla.SORPRESA));
		casillas.add(new Calle(12, new TituloPropiedad("Calle8", 560, 56, (float) 0.17, 700, 425)));
		casillas.add(new Calle(13, new TituloPropiedad("Calle9", 700, 70, (float) 0.15, 700, 425)));
		casillas.add(new Calle(14, new TituloPropiedad("Calle10", 580, 60, (float) 0.13, 700, 425)));
		
		casillas.add(new OtraCasilla(15, 0, TipoCasilla.JUEZ));		
		casillas.add(new Calle(16, new TituloPropiedad("Calle11", 925, 95, (float) 0.18, 700, 425)));
		casillas.add(new Calle(17, new TituloPropiedad("Calle12", 950, 95, (float) 0.19, 750, 450)));
		casillas.add(new OtraCasilla(18, 0, TipoCasilla.SORPRESA));
		casillas.add(new Calle(19, new TituloPropiedad("Calle13", 1000, 100, (float) 0.2, 1000, 450)));
	}
	
	@Override
	public String toString() {
		return "Tablero{" + "casillas=" + casillas.toString() + ", carcel=" + carcel.toString() + '}' + "\n";
	}
}
