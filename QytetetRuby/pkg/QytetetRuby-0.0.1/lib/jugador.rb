#encoding: utf-8
module ModeloQytetet
	
	class Jugador
		attr_accessor :cartaLibertad, :casillaActual, :encarcelado
		attr_reader :nombre, :propiedades, :saldo
		
		def initialize(nombre, saldo, encarcelado, casillaActual, cartaLibertad, propiedades)
			@nombre=nombre
			@saldo=saldo
			@encarcelado=encarcelado
			@casillaActual=casillaActual
			@cartaLibertad=cartaLibertad
			@propiedades=propiedades
		end
		
		def self.nuevo(nombre)
			new(nombre, 7500, false, Casilla.new(0, 1000, TipoCasilla::PARKING), nil, Array.new)
		end
		
		def self.copia(otroJugador)
			new(otroJugador.nombre, otroJugador.saldo, otroJugador.encarcelado, otroJugador.casillaActual, 
					 otroJugador.cartaLibertad, otroJugador.propiedades)
		end
		
		def cancelarHipoteca(titulo)
			costeHipotecar = titulo.calcularCosteCancelar
			tengoSaldo = tengoSaldo(costeHipotecar)
			
			if (tengoSaldo)
				modificarSaldo(-costeHipotecar)
				titulo.cancelarHipoteca
			end
			
			return tengoSaldo
		end
		
		def comprarTituloPropiedad
			comprado = false
			costeCompra = @casillaActual.coste
			
			if (costeCompra < @saldo)
				titulo = @casillaActual.asignarPropietario(self)
				comprado = true
				
				@propiedades << titulo
				modificarSaldo(-costeCompra)
			end
			
			return comprado
		end
		
		def convertirme(fianza)
			Especulador.copia(self, fianza)
		end

		def cuantasCasasHotelesTengo
			total = 0
			
			for propiedad in @propiedades
				total += propiedad.numCasas + propiedad.numHoteles
			end
			
			return total
		end
		
		def deboIrACarcel
			return !tengoCartaLibertad
		end
		
		def deboPagarAlquiler
			deboPagar = false
			titulo = @casillaActual.titulo
			esDeMiPropiedad = esDeMiPropiedad(titulo)
			
			if (!esDeMiPropiedad)
				tienePropietario = titulo.tengoPropietario
				
				if (tienePropietario)
					encarcelado = titulo.propietarioEncarcelado
					
					if (!encarcelado)
						estaHipotecada = titulo.hipotecada
						
						if (!estaHipotecada)
							deboPagar = true
						end
					end
				end
			end
			
			return deboPagar
		end
			
		def devolverCartaLibertad
			@cartaLibertad=nil
			
			Sorpresa.new("Te sobra dinero para pagar la fianza, no necesitas estar en la cárcel como los putos pobres :)", 0, TipoSorpresa::SALIRCARCEL)
		end
		
		def edificarCasa
			
		end
		
		def edificarHotel
			
		end
		
		def edificarCasa(titulo)
			edificada = false
			
			if (puedoEdificarCasa(titulo))
				costeEdificarCasa = titulo.precioEdificar
				tengoSaldo = tengoSaldo(costeEdificarCasa)
				
				if (tengoSaldo)
					titulo.edificarCasa()
					modificarSaldo(-costeEdificarCasa)
					edificada = true
				end
			end
			
			return edificada
		end
		
		def edificarHotel(titulo)
			edificada = false
			
			if (puedoEdificarHotel(titulo))
				costeEdificarHotel = titulo.precioEdificar
				tengoSaldo = tengoSaldo(costeEdificarHotel)
				
				if (tengoSaldo)
					titulo.edificarHotel()
					modificarSaldo(-costeEdificarHotel)
					edificada = true
				end
			end
			
			return edificada
		end
		
		def eliminarDeMisPropiedades(titulo)
			@propiedades.delete(titulo)
			@propietario = nil
		end
		
		def esDeMiPropiedad(titulo)
			soyPropietario = false
			
			for propiedad in @propiedades
				if(propiedad == titulo)
					soyPropietario = true;
				end
			end
		end
		
		#def estoyEnCalleLibre
		
		#def getCartaLibertad
		
		def hipotecarPropiedad(titulo)
			costeHipoteca = titulo.hipotecar
			modificarSaldo(costeHipoteca)
		end
		
		def irACarcel(casilla)
			@casillaActual = casilla
			@encarcelado = true
		end
		
		def modificarSaldo(cantidad)
			@saldo += cantidad
		end
		
		def obtenerCapital
			capital = @saldo
			
			for propiedad in @propiedades
				capital += propiedad.precioCompra + (propiedad.numCasas + propiedad.numHoteles) * propiedad.precioEdificar
				if(propiedad.hipotecada)
					capital -= propiedad.hipotecaBase
				end
			end
			
			return capital
		end
		
		def obtenerPropiedades(hipotecada)
			hipotecadas = Array.new
			
			for propiedad in @propiedades
				if (propiedad.hipotecada == hipotecada)
					hipotecadas << propiedad
				end
			end
		end
		
		def pagarAlquiler
			costeAlquiler = @casillaActual.pagarAlquiler()
			
			modificarSaldo(-costeAlquiler)
		end
		
		def pagarImpuesto
			@saldo -= @casillaActual.coste
		end
		
		def pagarLibertad(cantidad)
			tengoSaldo = tengoSaldo(cantidad)
			
			if (tengoSaldo)
				@encarcelado = false
				modificarSaldo(-cantidad)
			end
		end
		
		def puedoEdificarCasa(titulo)
			titulo.numCasas < 4
		end
		
		def puedoEdificarHotel(titulo)
			titulo.numCasas == 4 && titulo.numHoteles < 4
		end
		
		def tengoCartaLibertad
			cartaLibertad != nil
		end
		
		def tengoSaldo(cantidad)
			@saldo > cantidad
		end
		
		def venderPropiedad(casilla)
			titulo = casilla.titulo
			eleminarDeMisPropiedades(titulo)
			
			precioVenta = calcularPrecioVenta
			modificarSaldo(precioVenta)
		end
		
		#Override
		def <=> (otroJugador)
			otroJugador.ObtenerCapital <=> obtenerCapital
		end


		#Override
		def to_s()
			"Jugador{nombre=#{@nombre}, saldo=#{@saldo}, capital=#{obtenerCapital}, encarcelado=#{@encarcelado}, casillaActual=#{@casillaActual}, cartaLibertad=#{@cartaLibertad}, propiedades=#{@propiedades}}"
		end
		
		def to_s_sinpropiedad()
			"Jugador{nombre=#{@nombre}, saldo=#{@saldo}, capital=#{obtenerCapital}, encarcelado=#{@encarcelado}, casillaActual=#{@casillaActual}, cartaLibertad=#{@cartaLibertad}}"
		end
		
		#private :eliminarDeMisPropiedad, :esDeMiPropiedad
		#protected :convertirme, :deboIrACarcel, :copia, :pagarImpuesto, :puedoEdificarCasa, :puedoEdificarHotel, :tengoSaldo
	end
end