# encoding: utf-8
module ModeloQytetet
	class Calle < Casilla
		def initialize(numero, titulo)
			super(numero, titulo.precioCompra, TipoCasilla::CALLE)
			@titulo = titulo
		end

		def asignarPropietario(jugador)
			@titulo.propietario = jugador

			return @titulo
		end

		def pagarAlquiler
			return @titulo.pagarAlquiler
		end

		def soyEdificable
			true
		end

		def tengoPropietario
			@titulo.tengoPropietario
		end

		def propietarioEncarcelado
			@titulo.propietarioEncarcelado
		end

		def to_s
			"Casilla{numeroCasilla=#{@numero}, coste=#{@coste}, tipo=#{@tipo}, titulo=#{@titulo.to_s()}"
		end
	end
end