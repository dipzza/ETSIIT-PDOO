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
public class OtraCasilla extends Casilla{
	
	OtraCasilla(int numeroCasilla, int coste, TipoCasilla tipo)
	{
		super(numeroCasilla, coste, tipo, null);
	}
	
	@Override
	protected TipoCasilla getTipo() {
		return tipo;
	}
	
	@Override
	protected boolean soyEdificable() {
		return false;
	}
	
	@Override
	protected TituloPropiedad getTitulo() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Casilla{" + "numeroCasilla=" + super.getNumeroCasilla() + ", coste=" + super.getCoste() + ", tipo=" + getTipo() + '}';
	}
}
