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
public class Jugador implements Comparable{
	
	private String nombre;
	private int saldo;
	private boolean encarcelado;
	private Casilla casillaActual;
	private Sorpresa cartaLibertad;
	private ArrayList<TituloPropiedad> propiedades;
	
	
	Jugador(String nombre)
	{
		this.nombre = nombre;
		saldo = 7500;
		encarcelado = false;
		casillaActual = new OtraCasilla(0, 500, TipoCasilla.PARKING);
		cartaLibertad = null;
		propiedades = new ArrayList();
	}
	
	Jugador(Jugador otroJugador)
	{
		nombre = otroJugador.nombre;
		saldo  = otroJugador.saldo;
		encarcelado = otroJugador.encarcelado;
		casillaActual = otroJugador.casillaActual;
		cartaLibertad = otroJugador.cartaLibertad;
		propiedades = otroJugador.propiedades;
	}

	boolean getEncarcelado() {
		return encarcelado;
	}

	String getNombre() {
		return nombre;
	}

	public int getSaldo() {
		return saldo;
	}
	
	boolean cancelarHipoteca(TituloPropiedad titulo)
	{
		int costeHipotecar = titulo.calcularCosteCancelar();
		boolean tengoSaldo = tengoSaldo(costeHipotecar);
				
		if (tengoSaldo)
		{
			modificarSaldo(-costeHipotecar);
			titulo.cancelarHipoteca();
		}
			
		return tengoSaldo;
	}
	
	boolean comprarTituloPropiedad()
	{
		boolean comprado = false;
		int costeCompra = casillaActual.getCoste();
		
		if (costeCompra < saldo)
		{
			TituloPropiedad titulo = casillaActual.asignarPropietario(this);
			
			comprado = true;
			propiedades.add(titulo);
			modificarSaldo(-costeCompra);
		}
		
		return comprado;
	}
	
	protected Especulador convertirme(int fianza)
	{
		return new Especulador(this, fianza);
	}
	
	int cuantasCasasHotelesTengo()
	{
		int total = 0;
		
		for (int i=0; i<propiedades.size(); i++)
			total += propiedades.get(i).getNumCasas() + propiedades.get(i).getNumHoteles();
		
		return total;
	}
	
	protected boolean deboIrACarcel()
	{
		return !tengoCartaLibertad();
	}
	
	boolean deboPagarAlquiler()
	{
		TituloPropiedad titulo = casillaActual.getTitulo();
		boolean esDeMiPropiedad = esDeMiPropiedad(titulo);
		boolean deboPagar = false;
		
		if (!esDeMiPropiedad)
		{
			boolean tienePropietario = titulo.tengoPropietario();
			if (tienePropietario)
			{
				boolean encarcelado = titulo.propietarioEncarcelado();
				if (!encarcelado)
				{
					boolean estaHipotecada = titulo.getHipotecada();
					if (!estaHipotecada)
						deboPagar = true;
				}
			}
		}
		
		return deboPagar;
	}
	
	Sorpresa devolverCartaLibertad()
	{
		cartaLibertad = null;
		
		return new Sorpresa("Te sobra dinero para pagar la fianza, no necesitas estar en la cárcel como los putos pobres :)", 0, TipoSorpresa.SALIRCARCEL);
	}
	
	boolean edificarCasa(TituloPropiedad titulo)
	{
		boolean edificada = false;
		
		if (puedoEdificarCasa(titulo))
		{
			int costeEdificarCasa = titulo.getPrecioEdificar();
			boolean tengoSaldo = tengoSaldo(costeEdificarCasa);
			
			if (tengoSaldo)
			{
				titulo.edificarCasa();
				modificarSaldo(-costeEdificarCasa);
				edificada = true;
			}
		}
		
		return edificada;
	}
	
	boolean edificarHotel(TituloPropiedad titulo)
	{
		boolean edificada = false;
		
		if (puedoEdificarHotel(titulo))
		{
			int costeEdificarHotel = titulo.getPrecioEdificar();
			boolean tengoSaldo = tengoSaldo(costeEdificarHotel);
			
			if (tengoSaldo)
			{
				titulo.edificarHotel();
				modificarSaldo(-costeEdificarHotel);
				edificada = true;
			}
		}
		
		return edificada;
	}
	
	private void eliminarDeMisPropiedades(TituloPropiedad titulo)
	{
		propiedades.remove(titulo);
		titulo.setPropietario(null);
	}
	
	private boolean esDeMiPropiedad(TituloPropiedad titulo)
	{
		boolean soyPropietario = false;
		
		for(int i=0; i<propiedades.size(); i++)
			if(titulo == propiedades.get(i))
				soyPropietario = true;
		
		return soyPropietario;
	}
	
	//boolean estoyEnCalleLibre(){}
	
	Sorpresa getCartaLibertad(){
		return cartaLibertad;
	}
	
	public Casilla getCasillaActual(){
		return casillaActual;
	}
	
	ArrayList<TituloPropiedad> getPropiedades(){
		return propiedades;
	}
	
	void hipotecarPropiedad(TituloPropiedad titulo)
	{
		int costeHipoteca = titulo.hipotecar();
		
		modificarSaldo(costeHipoteca);
	}
	
	void irACarcel(Casilla casilla)
	{
		setCasillaActual(casilla);
		setEncarcelado(true);
	}
	
	void modificarSaldo(int cantidad)
	{
		saldo += cantidad;
	}
	
	int obtenerCapital()
	{
		int capital = saldo;
		TituloPropiedad titulo;
		
		for (int i=0; i<propiedades.size(); i++)
		{
			titulo = propiedades.get(i);
			capital += titulo.getPrecioCompra();
			capital += titulo.getPrecioEdificar() * (titulo.getNumCasas() + titulo.getNumHoteles());
			if(titulo.getHipotecada())
				capital -= titulo.getHipotecaBase();
		}
		
		return capital;
	}
	
	ArrayList<TituloPropiedad> obtenerPropiedades(boolean estadoHipotecada)
	{
		ArrayList<TituloPropiedad> estado = new ArrayList();
		
		for(int i=0; i<propiedades.size(); i++)
		{
			if (propiedades.get(i).getHipotecada() == estadoHipotecada)
				estado.add(propiedades.get(i));
		}
		
		return estado;
	}
	
	void pagarAlquiler()
	{
		int costeAlquiler = ( (Calle) casillaActual).pagarAlquiler();
		
		modificarSaldo(-costeAlquiler);
	}
	
	protected void pagarImpuesto()
	{
		saldo -= casillaActual.getCoste();
	}
	
	void pagarLibertad(int cantidad)
	{
		boolean tengoSaldo = tengoSaldo(cantidad);
		
		if (tengoSaldo)
		{
			setEncarcelado(false);
			modificarSaldo(-cantidad);
		}
	}
	
	protected boolean puedoEdificarCasa(TituloPropiedad titulo)
	{
		return (titulo.getNumCasas() < 4);
	}
	
	protected boolean puedoEdificarHotel(TituloPropiedad titulo)
	{
		return (titulo.getNumCasas() == 4 && titulo.getNumHoteles() < 4);
	}
	
	void setCartaLibertad(Sorpresa carta){
		cartaLibertad = carta;
	}
	
	void setCasillaActual(Casilla casilla){
		casillaActual = casilla;
	}
	
	void setEncarcelado(boolean encarcelado){
		this.encarcelado = encarcelado;
	}
	
	void setEncarcelado(){
		this.encarcelado = false;
	}
	
	boolean tengoCartaLibertad(){return cartaLibertad != null;}
	
	protected boolean tengoSaldo(int cantidad)
	{
		return (saldo > cantidad);
	}
	
	void venderPropiedad(Casilla casilla)
	{
		TituloPropiedad titulo = casilla.getTitulo();
		int precioVenta = titulo.calcularPrecioVenta();
		
		eliminarDeMisPropiedades(titulo);
		modificarSaldo(precioVenta);
	}
	
	@Override
	public int compareTo (Object otroJugador)
	{
		return obtenerCapital() - ((Jugador)otroJugador).obtenerCapital();
	}
	
	@Override
	public String toString() 
	{
		if(cartaLibertad == null)
			return "Jugador{nombre=" + nombre + ", saldo=" + saldo + ", capital=" + obtenerCapital() + ", encarcelado=" + encarcelado + ", casillaActual=" + casillaActual.toString() + ", propiedades=" 
					+ propiedades.toString();
		else
			return "Jugador{nombre=" + nombre + ", saldo=" + saldo + ", capital=" + obtenerCapital() + ", encarcelado=" + encarcelado + ", casillaActual=" + casillaActual.toString() + ", cartaLibertad=" 
					+ cartaLibertad.toString() + ", propiedades=" + propiedades.toString();
	}
}