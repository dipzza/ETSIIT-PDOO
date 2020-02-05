/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 *
 * @author jakerxd
 */
public class Qytetet 
{
	private static final Qytetet qytetet = new Qytetet();
	private Tablero tablero;
	private Dado dado;
	private ArrayList<Sorpresa> mazo;
	private Sorpresa cartaActual;
	private ArrayList<Jugador> jugadores;
	private Jugador jugadorActual;
	private EstadoJuego estadoJuego;
	
	public final int MAX_JUGADORES = 4;
	public final int NUM_CASILLAS = 20;
	final int NUM_SORPRESAS = 12;
	final int PRECIO_LIBERTAD = 200;
	final int SALDO_SALIDA = 500;
	
	private Qytetet()
	{
		tablero = null;
		dado = Dado.getDado();
		mazo = new ArrayList();
		cartaActual = null;
		jugadores = new ArrayList();
		jugadorActual = null;
		estadoJuego = null;
	}
	
	void actuarSiEnCasillaEdificable()
	{
		boolean deboPagar = jugadorActual.deboPagarAlquiler();
		
		if (deboPagar)
		{
			jugadorActual.pagarAlquiler();
			if (jugadorActual.getSaldo() <= 0)
				setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
		}
		
		Casilla casilla = obtenerCasillaJugadorActual();
		boolean tengoPropietario = ( (Calle) casilla).tengoPropietario();
		
		if (estadoJuego != EstadoJuego.ALGUNJUGADORENBANCARROTA)
			if (tengoPropietario)
				setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
			else
				setEstadoJuego(EstadoJuego.JA_PUEDECOMPRAROGESTIONAR);
	}
	
	void actuarSiEnCasillaNoEdificable()
	{
		setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
		
		Casilla casillaActual = jugadorActual.getCasillaActual();
		
		if (casillaActual.getTipo() == TipoCasilla.IMPUESTO)
			jugadorActual.pagarImpuesto();
		else if (casillaActual.getTipo() == TipoCasilla.JUEZ)
			encarcelarJugador();
		else if (casillaActual.getTipo() == TipoCasilla.SORPRESA)
		{
			cartaActual = mazo.remove(0);
			setEstadoJuego(EstadoJuego.JA_CONSORPRESA);
		}
	}
	
	public void aplicarSorpresa()
	{
		setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
		
		if (cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL)
			jugadorActual.setCartaLibertad(cartaActual);
		else
		{
			mazo.add(cartaActual);
			if (cartaActual.getTipo() == TipoSorpresa.PAGARCOBRAR)
			{
				jugadorActual.modificarSaldo(cartaActual.getValor());
				if (jugadorActual.getSaldo() < 0)
					setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
			}
			else if (cartaActual.getTipo() == TipoSorpresa.IRACASILLA)
			{
				int valor = cartaActual.getValor();
				boolean casillaCarcel = tablero.esCasillaCarcel(valor);
			
				if (casillaCarcel == true)
					encarcelarJugador();
				else
					mover(valor);
			}
			else if (cartaActual.getTipo() == TipoSorpresa.PORCASAHOTEL)
			{
				int cantidad = cartaActual.getValor();
				int numeroTotal = jugadorActual.cuantasCasasHotelesTengo();
			
				jugadorActual.modificarSaldo(cantidad*numeroTotal);
				if (jugadorActual.getSaldo() < 0)
					setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
			}
			else if (cartaActual.getTipo() == TipoSorpresa.PORJUGADOR) // No está igual que en el diagrama!!
			{
				Jugador jugador;
			
				for (int i=0; i < jugadores.size(); i++)
				{
					jugador = jugadores.get(i);
					if (jugador != jugadorActual)
					{
						jugador.modificarSaldo(cartaActual.getValor());
						if (jugador.getSaldo() < 0)
							setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
						
						jugadorActual.modificarSaldo(-cartaActual.getValor());
						if (jugador.getSaldo() < 0)
							setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
					}
				}
			}
			else if (cartaActual.getTipo() == TipoSorpresa.CONVERTIRME)
			{
				Especulador especulador = jugadorActual.convertirme(cartaActual.getValor());
				jugadores.set(jugadores.indexOf(jugadorActual), especulador);
				jugadorActual = especulador;
			}
		}
	}
	
	public boolean cancelarHipoteca(int numeroCasilla)
	{
		setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
		
		return jugadorActual.cancelarHipoteca(tablero.obtenerCasillaNumero(numeroCasilla).getTitulo());
	}
	
	public boolean comprarTituloPropiedad()
	{
		boolean comprado = jugadorActual.comprarTituloPropiedad();
		
		if (comprado)
			setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
		
		return comprado;
	}
	
	public boolean edificarCasa(int numeroCasilla)
	{
		Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
		TituloPropiedad titulo = casilla.getTitulo();
		boolean edificada = jugadorActual.edificarCasa(titulo);
		
		if (edificada)
			setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
		
		return edificada;
	}
	
	public boolean edificarHotel(int numeroCasilla)
	{
		Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
		TituloPropiedad titulo = casilla.getTitulo();
		boolean edificada = jugadorActual.edificarHotel(titulo);
		
		if (edificada)
			setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
		
		return edificada;
	}
	
	private void encarcelarJugador()
	{
		if (jugadorActual.deboIrACarcel())
		{
			Casilla casillaCarcel = tablero.getCarcel();
			
			jugadorActual.irACarcel(casillaCarcel);
			setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
		}
		else
		{
			Sorpresa carta = jugadorActual.devolverCartaLibertad();
			
			mazo.add(carta);
			setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
		}
	}

	public EstadoJuego getEstadoJuego() {
		return estadoJuego;
	}
	
	public Sorpresa getCartaActual(){
		return cartaActual;
	}
	
	Dado getDado(){
		return dado;
	}
	
	public Jugador getJugadorActual(){
		return jugadorActual;
	}
	
	public ArrayList<Jugador> getJugadores(){
		return jugadores;
	}
	
	public Tablero getTablero(){
		return tablero;
	}
	
	public ArrayList<Sorpresa> getMazo(){
		return mazo;
	}
	
	//ArrayList<Sorpresa> getMazo
	
	public int getValorDado(){
		return dado.getValor();
	}
	
	public void hipotecarPropiedad(int numeroCasilla)
	{
		Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
		TituloPropiedad titulo = casilla.getTitulo();
		
		jugadorActual.hipotecarPropiedad(titulo);
		setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
	}
	
	//private void inicializarCartasSorpresa()
	
	public void inicializarJuego(ArrayList<String> nombres)
	{
		inicializarJugadores(nombres);
		inicializarTablero();
		inicializarCartasSorpresa();
		salidaJugadores();
	}
	
	//precondicion nombres[2..MAX_JUGADORES]
	private void inicializarJugadores(ArrayList<String> nombres)
	{
		for (int i=0; i<nombres.size(); i++)
			jugadores.add(new Jugador(nombres.get(i)));
	}
	
	private void inicializarTablero()
	{
		tablero = new Tablero();
	}
	
	public boolean intentarSalirCarcel(MetodoSalirCarcel metodo)
	{
		if (metodo == MetodoSalirCarcel.TIRANDODADO)
		{
			int resultado = tirarDado();
			
			if (resultado >= 5)
				jugadorActual.setEncarcelado(false);
		}
		else if (metodo == MetodoSalirCarcel.PAGANDOLIBERTAD)
			jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
		
		boolean encarcelado = jugadorActual.getEncarcelado();
		
		if (encarcelado)
			setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
		else
			setEstadoJuego(EstadoJuego.JA_PREPARADO);
		
		return !encarcelado;
	}

	public void jugar()
	{
		tirarDado();
		mover(tablero.obtenerCasillaFinal(jugadorActual.getCasillaActual(), dado.getValor()).getNumeroCasilla());
	}
	
	void mover(int numCasillaDestino)
	{
		Casilla casillaInicial = jugadorActual.getCasillaActual();
		Casilla casillaFinal = tablero.obtenerCasillaNumero(numCasillaDestino);
		
		jugadorActual.setCasillaActual(casillaFinal);
		if (numCasillaDestino < casillaInicial.getNumeroCasilla())
			jugadorActual.modificarSaldo(SALDO_SALIDA);
		if (casillaFinal.soyEdificable())
			actuarSiEnCasillaEdificable();
		else
			actuarSiEnCasillaNoEdificable();
	}
	
	public Casilla obtenerCasillaJugadorActual() {
		return jugadorActual.getCasillaActual();
	}
	
	//public ArrayList<Casilla> obtenerCasillasTablero()
	
	public ArrayList<Integer> obtenerPropiedadesJugador()
	{
		ArrayList<Integer> numCasillasPropiedades = new ArrayList();
		ArrayList<Casilla> casillas = tablero.getCasillas();
		ArrayList<TituloPropiedad> propiedades = jugadorActual.getPropiedades();
		
		for (int i=0; i<casillas.size(); i++)
		{
			if (propiedades.contains(casillas.get(i).getTitulo()))
				numCasillasPropiedades.add(i);
		}
		
		return numCasillasPropiedades;
	}
	
	public ArrayList<Integer> obtenerPropiedadesJugadorSegunEstadoHipoteca(boolean estadoHipoteca)
	{
		ArrayList<Integer> numCasillasPropiedades = new ArrayList();
		ArrayList<Casilla> casillas = tablero.getCasillas();
		ArrayList<TituloPropiedad> propiedades = jugadorActual.obtenerPropiedades(estadoHipoteca);
		
		for (int i=0; i<casillas.size(); i++)
		{
			if (propiedades.contains(casillas.get(i).getTitulo()))
				numCasillasPropiedades.add(i);
		}
		
		return numCasillasPropiedades;
	}
	
	public void obtenerRanking(){
		Collections.sort(jugadores);
	}
	
	public int obtenerSaldoJugadorActual(){
		return jugadorActual.getSaldo();
	}
	
	private void salidaJugadores()
	{
		Random generador = new Random();
		
		for (int i=0; i<jugadores.size(); i++) {
			jugadores.get(i).setCasillaActual(tablero.obtenerCasillaNumero(0));
		}
		jugadorActual = jugadores.get(generador.nextInt(jugadores.size()));
		estadoJuego = EstadoJuego.JA_PREPARADO;
	}

	private void setCartaActual(Sorpresa cartaActual)
	{
		this.cartaActual = cartaActual;
	}
	
	public void setEstadoJuego (EstadoJuego estado){
		estadoJuego = estado;
	}
	
	public void siguienteJugador()
	{
		jugadorActual = jugadores.get((jugadores.indexOf(jugadorActual) + 1) % jugadores.size());
		estadoJuego = jugadorActual.getEncarcelado() ? EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD : EstadoJuego.JA_PREPARADO;
	}
	
	int tirarDado()
	{
		return dado.tirar();
	}
	
	public void venderPropiedad(int numeroCasilla)
	{
		Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
		
		jugadorActual.venderPropiedad(casilla);
		setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
	}
	
	public static Qytetet getQytetet()
	{
		return qytetet;
	}
	
	public void inicializarCartasSorpresa()
	{
		mazo.add(new Sorpresa("Tus amigos te presionan para comprar el COD IIII y sus DLCs. Pierdes 500 y se pasan al COD IIIII", -500, TipoSorpresa.PAGARCOBRAR));
		setCartaActual(mazo.get(0));
		mazo.add(new Sorpresa("Te das cuenta de que estás jugando a una variación de La Oca. Toma 200 no te vayas porfi ;_;", 200, TipoSorpresa.PAGARCOBRAR));
		mazo.add(new Sorpresa("El FBI ha encontrado tu carpeta de Furros, te arrestan. OWO", tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
		mazo.add(new Sorpresa("Recuerdas que has dejado el Tamagotchi en el coche sin darle de comer!!!!11!. *running around at the speed of sound*", 0, TipoSorpresa.IRACASILLA));
		mazo.add(new Sorpresa("Vas a la casilla 15 por los loles", 15, TipoSorpresa.IRACASILLA));
		mazo.add(new Sorpresa("Evades 300GC en impuestos por propiedad pero hacienda te pilla. Te condenan a pagar 100GC por propiedad", 200, TipoSorpresa.PORCASAHOTEL));
		mazo.add(new Sorpresa("Ser feliz no existe, en tu vida siempre habrá momentos malos y tienes que aprender a vivir con ello y luchar cada día.", -50, TipoSorpresa.PORCASAHOTEL));
		mazo.add(new Sorpresa("Regalas 50GC a cada jugador para agradecerles su compañía y su amistad", 0, TipoSorpresa.PORJUGADOR));
		mazo.add(new Sorpresa("Me da pereza pensar otra frase. El resto de jugadores te dan 100GC", 50, TipoSorpresa.PORJUGADOR));
		mazo.add(new Sorpresa("Te sobra dinero para pagar la fianza. Ir a la cárcel o la ley solo se aplica a los putos pobres :)", 0, TipoSorpresa.SALIRCARCEL));
		mazo.add(new Sorpresa("Te conviertes en especulador de viviendas, necesidad básica de la gente. Familias en la calle, pero ganas más dinero!", 3000, TipoSorpresa.CONVERTIRME));
		mazo.add(new Sorpresa("Te conviertes en especulador de viviendas, necesidad básica de la gente. Familias en la calle, pero ganas más dinero!", 5000, TipoSorpresa.CONVERTIRME));
	}
	
	@Override
	public String toString()
	{
		return "Qytetet{tablero=" + tablero.toString() + ", dado=" + dado.toString() + ", mazo=" + mazo.toString() + ", cartaActual=" + cartaActual.toString() + ", jugadores=" 
				+ jugadores.toString() + ", jugadorActual=" + jugadorActual.toString() + ", MAX_JUGADORES=" + MAX_JUGADORES + ", NUM_CASILLAS=" + NUM_CASILLAS + ", NUM_SORPRESAS="
				+ NUM_SORPRESAS + ", PRECIO_LIBERTAD=" + PRECIO_LIBERTAD + ", SALDO_SALIDA" + SALDO_SALIDA + '}' + "\n";
	}
}