#encoding: utf-8
module ModeloQytetet

class TituloPropiedad
	attr_reader :nombre, :precioCompra, :alquilerBase, :factorRevalorizacion,
				:hipotecaBase, :precioEdificar, :numHoteles, :numCasas
			  
	attr_accessor :hipotecada, :propietario
	
	def initialize(nombre, pcompra, aBase, factorRev, hipotecaBas, precioEd)
		@hipotecada=false
		@numHoteles=0
		@numCasas=0
		
		@nombre=nombre
		@precioCompra=pcompra
		@alquilerBase=aBase
		@factorRevalorizacion=factorRev
		@hipotecaBase=hipotecaBas
		@precioEdificar=precioEd
		@propietario=nil
	end
	
	def calcularCosteCancelar
		(1.1 * calcularCosteHipotecar).to_i
	end
	
	def calcularCosteHipotecar
		@hipotecaBase + @numCasas/2 * @hipotecaBase + @numHoteles * @hipotecaBase
	end
	
	def calcularImporteAlquiler
		@alquilerBase + @numCasas/2 + @numHoteles*2
	end
	
	def calcularPrecioVenta
		(@precioCompra + (@numCasas + @numHoteles) * @precioEdificar * @factorRevalorizacion).to_i
	end
	
	def cancelarHipoteca
		hipotecada = false;
	end
	
	#def cobrarAlquiler(coste)
	
	def edificarCasa
		@numCasas += 1
	end
	
	def edificarHotel
		@numCasas = 0
		@numHoteles += 1
	end
	
	def hipotecar
		@hipotecada = true
		
		return calcularCosteHipotecar
	end
	
	def pagarAlquiler
		costeAlquiler = calcularImporteAlquiler()
		
		@propietaro.modificarSaldo(costeAlquiler)
		
		return costeAlquiler
	end
	
	def propietarioEncarcelado
		@propietario.encarcelado
	end
	
	def tengoPropietario
		@propietario != nil
	end
	
	#Override
	def to_s()
		if propietario == nil
			"TituloPropiedad{nombre=#{@nombre}, hipotecada=#{@hipotecada}, precioCompra=#{@precioCompra}, alquilerBase=#{@alquilerBase}, factorRevalorizacion=#{@factorRevalorizacion}, hipotecaBase=#{@hipotecaBase}, precioEdificar=#{@precioEdificar}, numHoteles=#{@numHoteles}, numCasas=#{@numCasas}}"
		else
			"TituloPropiedad{nombre=#{@nombre}, hipotecada=#{@hipotecada}, precioCompra=#{@precioCompra}, alquilerBase=#{@alquilerBase}, factorRevalorizacion=#{@factorRevalorizacion}, hipotecaBase=#{@hipotecaBase}, precioEdificar=#{@precioEdificar}, numHoteles=#{@numHoteles}, numCasas=#{@numCasas}}"
		end
	end
end

end