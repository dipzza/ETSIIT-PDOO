# encoding: utf-8
module ModeloQytetet
require "singleton"
require_relative "tipo_sorpresa"
require_relative "tipo_casilla"
require_relative "estado_juego"
require_relative "jugador"
require_relative "especulador"
require_relative "sorpresa"
require_relative "tablero"
require_relative "dado"
require_relative "casilla"
require_relative "calle"
require_relative "titulo_propiedad"
	
class Qytetet
	attr_reader :mazo, :dado, :jugadorActual, :jugadores, :estadoJuego, :tablero
	attr_accessor :cartaActual
	
	include Singleton
	
	MAX_JUGADORES = 4
	NUM_SORPRESAS = 12
	NUM_CASILLAS = 20
	PRECIO_LIBERTAD = 200
	SALDO_SALIDA = 1000
	
	def initialize
		@mazo = Array.new
		@tablero = nil
		@dado = Dado.instance
		@cartaActual = nil
		@jugadores = Array.new
		@jugadorActual = nil
		@estadoJuego = nil
	end
	
	def actuarSiEnCasillaEdificable
		deboPagar = @jugadorActual.deboPagarAlquiler
		
		if (deboPagar)
			@jugadorActual.pagarAlquiler
			if (@jugadorActual.saldo <= 0)
				@estadoJuego = EstadoJuego::ALGUNJUGADORENBANCARROTA
			end
		end
		
		casilla = obtenerCasillaJugadorActual
		tengoPropietario = casilla.tengoPropietario
		
		if (@estadoJuego != EstadoJuego::ALGUNJUGADORENBANCARROTA)
			if (tengoPropietario)
				@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
			else
				@estadoJuego = EstadoJuego::JA_PUEDECOMPRAROGESTIONAR
			end
		end
	end
	
	def actuarSiEnCasillaNoEdificable
		@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
		
		casillaActual = @jugadorActual.casillaActual
		
		if (casillaActual.tipo == TipoCasilla::IMPUESTO)
			@jugadorActual.pagarImpuesto
		elsif (casillaActual.tipo == TipoCasilla::JUEZ)
			encarcelarJugador
		elsif (casillaActual.tipo == TipoCasilla::SORPRESA)
			@cartaActual = @mazo.delete_at(0)
			@estadoJuego = EstadoJuego::JA_CONSORPRESA
		end
	end
	
	def aplicarSorpresa
		@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
		
		if (@cartaActual.tipo == TipoSorpresa::SALIRCARCEL)
			@jugadorActual.cartaLibertad = @cartaActual
		else
			@mazo << @cartaActual;
			
			if (@cartaActual.tipo == TipoSorpresa::PAGARCOBRAR)
				@jugadorActual.modificarSaldo(@cartaActual.valor)
			
				if (@jugadorActual.saldo < 0)
					@estadoJuego = EstadoJuego::ALGUNJUGADORENBANCARROTA
				end
			elsif (@cartaActual.tipo == TipoSorpresa::IRACASILLA)
				valor = @cartaActual.valor
				casillaCarcel = @tablero.esCasillaCarcel(valor)
				
				if (casillaCarcel)
					encarcelarJugador
					mover(valor)
				end
			elsif (@cartaActual.tipo == TipoSorpresa::PORCASAHOTEL)
				cantidad = @cartaActual.valor
				numeroTotal = @jugadorActual.cuantasCasasHotelesTengo
				
				@jugadorActual.modificarSaldo(cantidad * numeroTotal)
				
				if (@jugadorActual.saldo < 0)
					@estadoJuego = EstadoJuego::ALGUNJUGADORENBANCARROTA
				end
			elsif (@cartaActual.tipo == TipoSorpresa::PORJUGADOR)
				for jugador in @jugadores
					if (jugador != @jugadorActual)
						jugador.modificarSaldo(@cartaActual.valor)
						
						if (jugador.saldo < 0)
							@estadoJuego = EstadoJuego::ALGUNJUGADORENBANCARROTA
						end
						@jugadorActual.modificarSaldo(-@cartaActual.valor)
						
						if (@jugadorActual.saldo < 0)
							@estadoJuego = EstadoJuego::ALGUNJUGADORENBANCARROTA
						end
					end
				end
			elsif (@cartaActual.tipo == TipoSorpresa::CONVERTIRME)
				especulador = @jugadorActual.convertirme(@cartaActual.valor)
				
				@jugadores[@jugadores.index(@jugadorActual)] = especulador
				@jugadorActual = especulador
			end
		end
	end
	
	def cancelarHipoteca(numeroCasilla)
		@estadoJuego = EstadoJuego::	JA_PUEDEGESTIONAR
		
		return @jugadorActual.cancelarHipoteca(@tablero.obtenerCasillaNumero(numeroCasilla).titulo)
	end
	
	def comprarTituloPropiedad
		comprado = @jugadorActual.comprarTituloPropiedad
		
		if (comprado)
			@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
		end
		
		return comprado
	end
	
	def edificarCasa(numeroCasilla)
		casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
		titulo = casilla.titulo
		
		edificada = @jugadorActual.edificarCasa(titulo)
		
		if (edificada)
			@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
		end
		
		return edificada
	end
	
	def edificarHotel(numeroCasilla)
		casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
		titulo = casilla.titulo
		
		edificada = @jugadorActual.edificarHotel(titulo)
		
		if (edificada)
			@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
		end
		
		return edificada
	end
	
	def encarcelarJugador
		if(@jugadorActual.deboIrACarcel)
			casillaCarcel = @tablero.carcel
			
			@jugadorActual.irACarcel(casillaCarcel)
			@estadoJuego = EstadoJuego::JA_ENCARCELADO
		else
			carta = @jugadorActual.devolverCartaLibertad
			
			@mazo << carta
			@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
		end
	end
	
	def getValorDado
		dado.valor
	end
	
	def hipotecarPropiedad(numeroCasilla)
		casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
		titulo = @jugadorActual.titulo
		
		@jugadorActual.hipotecarPropiedad
		@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
	end
	
	def inicializarCartasSorpresa
		@mazo << Sorpresa.new("Tus amigos te presionan para comprar el COD IIII y sus DLCs. Pierdes 500 y se pasan al COD IIIII", -500, TipoSorpresa::PAGARCOBRAR)
		@mazo << Sorpresa.new("Te das cuenta de que estás jugando a una variación de La Oca. Toma 200 no te vayas porfi ;_;", 200, TipoSorpresa::PAGARCOBRAR)
		@mazo << Sorpresa.new("El FBI ha encontrado tu carpeta de Furros, te arrestan. OWO", 9, TipoSorpresa::IRACASILLA)
		@mazo << Sorpresa.new("Recuerdas que has dejado el Tamagotchi en el coche sin darle de comer!!!!11!. *running around at the speed of sound*", 0, TipoSorpresa::IRACASILLA)
		@mazo << Sorpresa.new("Vas a la casilla 15 por los loles", 15, TipoSorpresa::IRACASILLA)
		@mazo << Sorpresa.new("Evades 300GC en impuestos por propiedad pero hacienda te pilla. Te condenan a pagar 100GC por propiedad", 200, TipoSorpresa::PORCASAHOTEL)
		@mazo << Sorpresa.new("Ser feliz no existe, en tu vida siempre habrá momentos malos y tienes que aprender a vivir con ello y luchar cada día.", -50, TipoSorpresa::PORCASAHOTEL)
		@mazo << Sorpresa.new("Regalas 50GC a cada jugador para agradecerles su compañía y su amistad", 0, TipoSorpresa::PORJUGADOR)
		@mazo << Sorpresa.new("Me da pereza pensar otra frase más jej. El resto de jugadores te dan 100GC", 50, TipoSorpresa::PORJUGADOR)
		@mazo << Sorpresa.new("Te sobra dinero para pagar la fianza, no necesitas estar en la cárcel como los putos pobres :)", 0, TipoSorpresa::SALIRCARCEL)
		@mazo << Sorpresa.new("Te conviertes en especulador de viviendas, necesidad básica de la gente. Familias en la calle, pero ganas más dinero!", 3000, TipoSorpresa::CONVERTIRME)
		@mazo << Sorpresa.new("Te conviertes en especulador de viviendas, necesidad básica de la gente. Familias en la calle, pero ganas más dinero!", 5000, TipoSorpresa::CONVERTIRME)
	end
	
	def inicializarJuego(nombres)
		inicializarJugadores(nombres)
		inicializarTablero
		inicializarCartasSorpresa
		salidaJugadores
	end
	
	def inicializarJugadores(nombres)
		for nombre in nombres
			@jugadores << Jugador.nuevo(nombre)
		end
	end
	
	def inicializarTablero
		@tablero = Tablero.new
	end
	
	def intentarSalirCarcel(metodo)
		if (metodo == MetodoSalirCarcel::TIRANDODADO)
			resultado = tirarDado
		
			if (resultado >= 5)
				@encarcelado = false;
			end
		elsif (metodo == MetodoSalirCarcel::PAGANDOLIBERTAD)
			@jugadorActual.pagarLibertad(PRECIO_LIBERTAD)
		end
		
		encarcelado = @jugadorActual.encarcelado
		
		if (encarcelado)
			@estadoJuego = EstadoJuego::JA_ENCARCELADO
		else
			@estadoJuego = EstadoJuego::JA_PREPARADO
		end
		
		return encarcelado
	end
	
	def jugar
		tirarDado
		mover((@tablero.obtenerCasillaFinal(@jugadorActual.casillaActual, dado.valor)).numero)
	end
	
	def mover(numCasillaDestino)
		casillaInicial = @jugadorActual.casillaActual
		casillaFinal = @tablero.obtenerCasillaNumero(numCasillaDestino)
		
		@jugadorActual.casillaActual = casillaFinal
		
		if (numCasillaDestino < casillaInicial.numero)
			@jugadorActual.modificarSaldo(SALDO_SALIDA)
		end
		
		if (casillaFinal.soyEdificable)
			actuarSiEnCasillaEdificable
		else
			actuarSiEnCasillaNoEdificable
		end
	end
	
	def obtenerCasillaJugadorActual
		@jugadorActual.casillaActual
	end
	
	#def obtenerCasillasTablero
	
	def obtenerPropiedadesJugador
		numCasillasPropiedades = Array.new
		propiedadesJugador = @jugadorActual.propiedades
		
		for casilla in @tablero.casillas
			if(propiedadesJugador.include?(casilla.titulo))
				numCasillasPropiedades << casilla.numero
			end
		end
		
		return numCasillasPropiedades
	end
	
	def obtenerPropiedadesJugadorSegunEstadoHipoteca(estadoHipoteca)
		numCasillasPropiedades = Array.new
		
		for casilla in @tablero.casillas
			if(@jugadorActual.obtenerPropiedades(estadoHipoteca).include?(casilla.titulo))
				numCasillasPropiedades << casilla.numero
			end
		end
		
		return numCasillasPropiedades
	end
	
	def obtenerRanking
		@jugadores = @jugadores.sort
	end
	
	def obtenerSaldoJugadorActual
		@jugadorActual.saldo
	end
	
	def salidaJugadores
		for jugador in @jugadores
			jugador.casillaActual = @tablero.obtenerCasillaNumero(0);
		end
		
		@jugadorActual = @jugadores.at(rand(@jugadores.size))
		@estadoJuego = EstadoJuego::JA_PREPARADO
	end
	
	def siguienteJugador
		@jugadorActual = @jugadores.at((@jugadores.index(@jugadorActual) + 1) % @jugadores.size)
		
		@estadoJuego = (@jugadorActual.encarcelado ? EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD : EstadoJuego::JA_PREPARADO)
	end
	
	def tirarDado
		@dado.tirar
	end
	
	def venderPropiedad(numeroCasilla)
		casilla = @tablero.obtenerCasillaNumero(numeroCasilla)
		
		@jugadorActual.venderPropiedad(casilla)
		@estadoJuego = EstadoJuego::JA_PUEDEGESTIONAR
	end
	
	#Override
	def to_s
		"Qytetet{Mazo=#{@mazo}, Tablero=#{@tablero}, Dado=#{@dado}, CartaActual=#{@cartaActual}, Jugadores=#{@jugadores}, JugadorActual=#{@jugadorActual}, MAX_JUGADORES=#{MAX_JUGADORES}, NUM_SORPRESAS=#{NUM_SORPRESAS}, NUM_CASILLAS=#{NUM_CASILLAS}, PRECIO_LIBERTAD=#{PRECIO_LIBERTAD}, SALDO_SALIDA=#{SALDO_SALIDA}}"
	end
	
	#private :encarcelarJugador, :inicializarCartasSorpresa, :inicializarJugadores, :inicializarTablero, :salidaJugadores
end
end