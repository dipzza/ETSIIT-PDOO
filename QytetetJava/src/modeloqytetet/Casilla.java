/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author jakerxd
 */
abstract class Casilla {
	private int numeroCasilla;
	private int coste;
	protected TipoCasilla tipo;
	protected TituloPropiedad titulo;
	
	
	Casilla(int numeroCasilla, int coste, TipoCasilla tipo, TituloPropiedad titulo)
	{
		this.numeroCasilla=numeroCasilla;
		this.coste = coste;
		this.tipo = tipo;
		this.titulo = titulo;
	}
	
	TituloPropiedad asignarPropietario (Jugador jugador)
	{
		titulo.setPropietario(jugador);
		
		return titulo;
	}
	
	int getNumeroCasilla() {
		return numeroCasilla;
	}

	int getCoste(){
		return coste;
	}

	protected abstract TipoCasilla getTipo();

	protected abstract TituloPropiedad getTitulo();
	
	public void setCoste(int coste) {
		this.coste = coste;
	}
	
	private void setTitulo(TituloPropiedad titulo){
		this.titulo = titulo;
	}
	
	boolean propietarioEncarcelado() {
		return titulo.propietarioEncarcelado();
	}

	protected abstract boolean soyEdificable();
	
	@Override
	public abstract String toString();
}