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
public class Calle extends Casilla{
	
	Calle(int numeroCasilla, TituloPropiedad titulo)
	{
		super(numeroCasilla, titulo.getPrecioCompra(), TipoCasilla.CALLE, titulo);
	}
	
	@Override
	protected TipoCasilla getTipo() {
		return TipoCasilla.CALLE;
	}
	
	@Override
	protected boolean soyEdificable() {
		return true;
	}
	
	@Override
	protected TituloPropiedad getTitulo() {
		return titulo;
	}
	
	public int pagarAlquiler()
	{
		return titulo.pagarAlquiler();
	}
	
	private void setTitulo(TituloPropiedad titulo){
		this.titulo = titulo;
	}
	
	public boolean tengoPropietario() {
		return titulo.tengoPropietario();
	}
	
	@Override
	public String toString() {
		return "Casilla{" + "numeroCasilla=" + super.getNumeroCasilla() + ", coste=" + super.getCoste() + ", tipo=" + getTipo() + ", titulo=" + getTitulo().toString() + '}';
	}
}
