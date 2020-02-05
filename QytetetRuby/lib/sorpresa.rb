#encoding: utf-8
module ModeloQytetet

class Sorpresa
	attr_reader :texto, :valor, :tipo
	
	def initialize(texto, valor, tipo)
		@texto=texto
		@valor=valor
		@tipo=tipo
	end
	
	def to_s
		"Sorpresa{Texto=#{@texto}, Valor=#{@valor}, Tipo=#{@tipo}}"
	end

end
end
