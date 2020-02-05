#encoding: utf-8
module ModeloQytetet
	class Especulador < Jugador
		
		def self.copia(unJugador, fianza)
			super(unJugador).convertirme(fianza)
		end
		
		def pagarImpuesto
			modificarSaldo(-@casillaActual.coste/2)
		end
		
		def convertirme(fianza)
			@fianza = fianza
			return self
		end
		
		def deboIrACarcel
			super && !pagarFianza
		end
		
		def pagarFianza
			if (@fianza < @saldo)
				@saldo -= @fianza
				return true
			end
			return false
		end
		
		def puedoEdificarCasa(titulo)
			titulo.numCasas < 8
		end
		
		def puedoEdificarHotel(titulo)
			titulo.numCasas > 3 && titulo.numHoteles < 8
		end
		
		def to_s
			super
		end
		
		def to_s_sinpropiedad
			super
		end
	end
end
