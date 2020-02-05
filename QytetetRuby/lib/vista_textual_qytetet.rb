# encoding: utf-8
module Vistatextualqytetet
	require_relative "opcion_menu"
	require_relative "controlador_qytetet"
	class VistaTextualQytetet
		def initialize
			@@controlador = Controladorqytetet::ControladorQytetet.instance
		end
		
		def obtenerNombreJugadores
			nombres = Array.new
			
			print "Introduzca el número de jugadores: "
			njugadores = gets.to_i
		
			i = 0
			while i < njugadores do
				print "Introduzca el nombre del jugador #{i + 1}: "
				nombres << gets.chomp
				i +=1
			end
		
			return nombres
		end
		
		def elegirCasilla(opcionMenu)
			validas = @@controlador.obtenerCasillasValidas(opcionMenu);
			validasString = Array.new
			
			if (validas.empty?)
				return -1
			else
				for valida in validas
					numero = valida.to_s

					validasString. << numero
					puts numero + ": " + "Casilla Valida"
				end
			end

			return leerValorCorrecto(validasString).to_i
		end
		
		def leerValorCorrecto(valoresCorrectos)
			correcto = false
			
			begin
				print "Elija una opción: "
				valor = gets.chomp
				
				if (valoresCorrectos.include?(valor))
					correcto = true;
				else
					puts "Opción incorrecta"
				end

			end while (!correcto)

			return valor;
		end
		
		def elegirOperacion
			operaciones = @@controlador.obtenerOperacionesJuegoValidas()
			operacionesString = Array.new
			
			for operacion in operaciones
				numero = operacion.to_s

				operacionesString << numero
				puts numero.to_s + ": " + Controladorqytetet::OpcionMenu[operacion].to_s
			end

			return leerValorCorrecto(operacionesString).to_i
		end
		
		def self.main
			ui = VistaTextualQytetet.new
		
			@@controlador.nombreJugadores = ui.obtenerNombreJugadores()
			casillaElegida = 0

			begin
				operacionElegida = ui.elegirOperacion()
				necesitaElegirCasilla = @@controlador.necesitaElegirCasilla(operacionElegida)
				if (necesitaElegirCasilla)
					casillaElegida = ui.elegirCasilla(operacionElegida)
				end
				if (!necesitaElegirCasilla || casillaElegida >= 0)
					puts @@controlador.realizarOperacion(operacionElegida, casillaElegida)
				end
			end while (true)
		end
		self.main
	end
end