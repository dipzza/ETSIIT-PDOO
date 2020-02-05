#encoding: utf-8
module ModeloQytetet


class Tablero
	attr_reader :casillas, :carcel
	
	def initialize
		@casillas = Array.new
		@carcel
		
		inicializar
	end
	
	def inicializar
		@casillas << Casilla.new(0, 1000, TipoCasilla::PARKING)
		@casillas << Calle.new(1, TituloPropiedad.new("Calle1", 500, 50, 0.13, 150, 250))
		@casillas << Calle.new(2, TituloPropiedad.new("Calle2", 550, 50,  0.12, 200, 260))
		@casillas << Calle.new(3, TituloPropiedad.new("Calle3", 520, 55,  0.11, 180, 250))
		@casillas << Casilla.new(4, 0, TipoCasilla::SORPRESA)
		
		@carcel =  Casilla.new(5, 0, TipoCasilla::CARCEL)
		@casillas << @carcel
		@casillas << Calle.new(6, TituloPropiedad.new("Calle4", 720, 70,  0.13, 450, 360))
		@casillas << Calle.new(7, TituloPropiedad.new("Calle5", 800, 80,  0.12, 500, 400))
		@casillas << Casilla.new(8, -200, TipoCasilla::IMPUESTO)
		@casillas << Calle.new(9, TituloPropiedad.new("Calle6", 750, 75,  0.15, 500, 420))
		
		@casillas << Calle.new(10, TituloPropiedad.new("Calle7", 600, 95,  0.14, 700, 425))
		@casillas << Casilla.new(11, 0, TipoCasilla::SORPRESA)
		@casillas << Calle.new(12, TituloPropiedad.new("Calle8", 560, 56,  0.17, 700, 425))
		@casillas << Calle.new(13, TituloPropiedad.new("Calle9", 700, 70,  0.15, 700, 425))
		@casillas << Calle.new(14, TituloPropiedad.new("Calle10", 580, 60,  0.13, 700, 425))
		
		@casillas << Casilla.new(15, 0, TipoCasilla::JUEZ)		
		@casillas << Calle.new(16, TituloPropiedad.new("Calle11", 925, 95,  0.18, 700, 425))
		@casillas << Calle.new(17, TituloPropiedad.new("Calle12", 950, 95,  0.19, 750, 450))
		@casillas << Casilla.new(18, 0, TipoCasilla::SORPRESA)
		@casillas << Calle.new(19, TituloPropiedad.new("Calle13", 1000, 100,  0.2, 1000, 450))
	end
	
	def esCasillaCarcel(numeroCasilla)
		numeroCasilla == @carcel.numero
	end
	
	def obtenerCasillaFinal(casilla, desplazamiento)
		obtenerCasillaNumero((casilla.numero + desplazamiento) % 20)
	end
	
	#Precondicion: numeroCasilla > 0 y menor que el max
	def obtenerCasillaNumero(numeroCasilla)
		@casillas[numeroCasilla]
	end
	
	def to_s()
		"Tablero{casillas=#{@casillas}, carcel=#{@carcel}}"
	end
	
	private :inicializar
end
end