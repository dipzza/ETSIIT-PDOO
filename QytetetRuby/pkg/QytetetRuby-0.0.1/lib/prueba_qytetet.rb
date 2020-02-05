#encoding: utf-8
module ModeloQytetet
require_relative "qytetet"
	
	class PruebaQytetet
		@@juego = Qytetet.instance
	
	def self.main()
		@@juego.inicializarJuego(getNombreJugadores)
		
		while (@@juego.estadoJuego != EstadoJuego::ALGUNJUGADORENBANCARROTA)
			@@juego.siguienteJugador
			
			@@juego.jugar
			
			if (@@juego.estadoJuego == EstadoJuego::JA_CONSORPRESA)
				@@juego.aplicarSorpresa
			elsif (@@juego.estadoJuego == EstadoJuego::JA_PUEDECOMPRAROGESTIONAR)
				@@juego.comprarTituloPropiedad
			elsif (@@juego.estadoJuego == EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD)
				@@juego.intentarSalirCarcel(MetodoSalirCarcel::PAGANDOLIBERTAD)
			end
		end
	end
		
	def self.getSorpresasPositivas ()
		positivas = Array.new
		
		for carta in @@juego.mazo
			if carta.valor > 0
				positivas << carta;
			end
		end
		return positivas
	end

	def self.getSorpresasiraCasilla ()
		ira = []
		
		for carta in @@juego.mazo
			if carta.tipo == TipoSorpresa::IRACASILLA
				ira << carta;
			end
		end
		return ira
	end
	
	
	def self.getSorpresasTipo (tipo)
		cartasTipo = []
		
		for carta in @@juego.mazo
			if carta.tipo == TipoSorpresa::const_get(tipo)
				cartasTipo << carta;
			end
		end
		return cartasTipo
	end
	
	def self.putsSorpresas(lista)
		lista.each do |sorpresa|
			puts sorpresa.to_s()
		end
		puts ""
	end
	
	def self.getNombreJugadores
		nombres = Array.new
		
		puts "Introduzca el número de jugadores: "
		njugadores = gets.to_i
		
		i = 0
		while i < njugadores do
			puts "Introduzca el nombre del jugador #{i + 1}: "
			nombres << gets.chomp
			i +=1
		end
		
		return nombres
	end

	PruebaQytetet.main
	end
end