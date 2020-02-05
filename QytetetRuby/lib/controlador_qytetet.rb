# encoding: utf-8
module Controladorqytetet
	require "singleton"
	require_relative "qytetet"
	require_relative "opcion_menu"
	require_relative "estado_juego"
	class ControladorQytetet
		attr_writer :nombreJugadores
		
		include Singleton
		def initialize
			@nombreJugadores
			@@juego = ModeloQytetet::Qytetet.instance
		end
		
		def obtenerOperacionesJuegoValidas
			validas = Array.new
			estado = @@juego.estadoJuego

			if (@@juego.jugadores.empty?)
				validas << OpcionMenu.index(:INICIARJUEGO)
			else
				if (estado == ModeloQytetet::EstadoJuego::JA_PREPARADO)
					validas << OpcionMenu.index(:JUGAR)
				elsif (estado == ModeloQytetet::EstadoJuego::JA_PUEDECOMPRAROGESTIONAR)
					validas << OpcionMenu.index(:PASARTURNO)
					validas << OpcionMenu.index(:COMPRARTITULOPROPIEDAD)
					validas << OpcionMenu.index(:VENDERPROPIEDAD)
					validas << OpcionMenu.index(:HIPOTECARPROPIEDAD)
					validas << OpcionMenu.index(:CANCELARHIPOTECA)
					validas << OpcionMenu.index(:EDIFICARCASA)
					validas << OpcionMenu.index(:EDIFICARHOTEL)
				elsif (estado == ModeloQytetet::EstadoJuego::JA_PUEDEGESTIONAR)
					validas << OpcionMenu.index(:PASARTURNO)
					validas << OpcionMenu.index(:VENDERPROPIEDAD)
					validas << OpcionMenu.index(:HIPOTECARPROPIEDAD)
					validas << OpcionMenu.index(:CANCELARHIPOTECA)
					validas << OpcionMenu.index(:EDIFICARCASA)
					validas << OpcionMenu.index(:EDIFICARHOTEL)				
				elsif (estado == ModeloQytetet::EstadoJuego::JA_ENCARCELADO)
					validas << OpcionMenu.index(:PASARTURNO)			
				elsif (estado == ModeloQytetet::EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD)
					validas << OpcionMenu.index(:INTENTARSALIRCARCELPAGANDOLIBERTAD)
					validas << OpcionMenu.index(:INTENTARSALIRCARCELTIRANDODADO)
				elsif (estado == ModeloQytetet::EstadoJuego::JA_CONSORPRESA)
					validas << OpcionMenu.index(:APLICARSORPRESA)		
				elsif (estado == ModeloQytetet::EstadoJuego::ALGUNJUGADORENBANCARROTA)
					validas << OpcionMenu.index(:OBTENERRANKING)
				end
				validas << OpcionMenu.index(:TERMINARJUEGO)
				validas << OpcionMenu.index(:MOSTRARJUGADORACTUAL)
				validas << OpcionMenu.index(:MOSTRARJUGADORES)
				validas << OpcionMenu.index(:MOSTRARTABLERO)
			end

			return validas
		end
		
		def necesitaElegirCasilla(opcionMenu)
			(opcionMenu == OpcionMenu.index(:HIPOTECARPROPIEDAD) ||
				opcionMenu == OpcionMenu.index(:CANCELARHIPOTECA) ||
				opcionMenu == OpcionMenu.index(:EDIFICARCASA) ||
				opcionMenu == OpcionMenu.index(:EDIFICARHOTEL) ||
				opcionMenu == OpcionMenu.index(:VENDERPROPIEDAD))
		end
		
		def obtenerCasillasValidas(opcionMenu)
			casillasValidas = Array.new
			
			if (opcionMenu == OpcionMenu.index(:HIPOTECARPROPIEDAD) || 
				opcionMenu == OpcionMenu.index(:EDIFICARCASA) ||
				opcionMenu == OpcionMenu.index(:EDIFICARHOTEL) ||
				opcionMenu == OpcionMenu.index(:VENDERPROPIEDAD))
				casillasValidas = @@juego.obtenerPropiedadesJugadorSegunEstadoHipoteca(false)
			elsif opcionMenu == OpcionMenu.index(:CANCELARHIPOTECA)
				casillasValidas = @@juego.obtenerPropiedadesJugadorSegunEstadoHipoteca(true)
			end
			
			return casillasValidas
		end
		
		def realizarOperacion(opcionElegida, casillaElegida)
		
			if opcionElegida == OpcionMenu.index(:INICIARJUEGO)
				@@juego.inicializarJuego(@nombreJugadores)
				return "Empieza la partida de MonoPoly. Hasta 6horas de perder tu tiempo!"
				
			elsif opcionElegida == OpcionMenu.index(:JUGAR)
				@@juego.jugar
				return "Tirada = " + @@juego.getValorDado.to_s + ", cae en " + @@juego.jugadorActual.casillaActual.to_s
				
			elsif opcionElegida == OpcionMenu.index(:APLICARSORPRESA)
				@@juego.aplicarSorpresa
				return "SORPRESA!!!1!! " + @@juego.cartaActual.to_s
				
			elsif opcionElegida == OpcionMenu.index(:INTENTARSALIRCARCELPAGANDOLIBERTAD)
				if(@@juego.intentarSalirCarcel(MetodoSalirCarcel::PAGANDOLIBERTAD))
					return "Sale de la cárcel pagando la fianza"
				else
					return "El jugador no tiene saldo suficiente para salir de la cárcel"
				end
			elsif opcionElegida == OpcionMenu.index(:INTENTARSALIRCARCELTIRANDODADO)
				if(@@juego.intentarSalirCarcel(MetodoSalirCarcel::TIRANDODADO))
					return "Sale de la cárcel con una tirada mayor que 4"
				else
					return "No sale de la cárcel por una tirada menor que 5"
				end
			elsif opcionElegida == OpcionMenu.index(:COMPRARTITULOPROPIEDAD)
				if(@@juego.comprarTituloPropiedad)
					return "Titulo de propiedad adquirido"
				else
					return "Saldo insuficiente para adquirir propiedad"
				end
			elsif opcionElegida == OpcionMenu.index(:HIPOTECARPROPIEDAD)
				@@juego.hipotecarPropiedad(casillaElegida)
				return "Propiedad hipotecada"
				
			elsif opcionElegida == OpcionMenu.index(:CANCELARHIPOTECA)
				if(@@juego.cancelarHipoteca(casillaElegida))
					return "Hipoteca cancelada"
				else
					return "Saldo insuficiente para cancelar hipoteca"
				end
			elsif opcionElegida == OpcionMenu.index(:EDIFICARCASA)
				if(@@juego.edificarCasa(casillaElegida))
					return "Casa edificada"
				else
					return "Saldo insuficiente o 4 casas construidas"
				end
			elsif opcionElegida == OpcionMenu.index(:EDIFICARHOTEL)
				if(@@juego.edificarHotel(casillaElegida))
					return "Hotel edificado"
				else
					return "Saldo insuficiente o no hay 4 casas o hay 8 hoteles"
				end
			elsif opcionElegida == OpcionMenu.index(:VENDERPROPIEDAD)
				@@juego.venderPropiedad(casillaElegida)
				return "Propiedad vendida"
				
			elsif opcionElegida == OpcionMenu.index(:PASARTURNO)
				@@juego.siguienteJugador
				return "Pasa el turno"
				
			elsif opcionElegida == OpcionMenu.index(:OBTENERRANKING)
				@@juego.obtenerRanking
				return @@juego.jugadores.to_s
				
			elsif opcionElegida == OpcionMenu.index(:TERMINARJUEGO)
				exit(0)
				
			elsif opcionElegida == OpcionMenu.index(:MOSTRARJUGADORACTUAL)
				return @@juego.jugadorActual.to_s
				
			elsif opcionElegida == OpcionMenu.index(:MOSTRARJUGADORES)
				return @@juego.jugadores.to_s
				
			elsif opcionElegida == OpcionMenu.index(:MOSTRARTABLERO)
				return @@juego.tablero.to_s
			end
		return "Operación Invalida"
		end
	end
end
