#encoding: utf-8
module ModeloQytetet

class Casilla
	attr_reader :numero, :coste, :tipo
	attr_accessor :titulo
	
	def initialize(numero, coste, tipo)
		@numero=numero
		@coste=coste
		@tipo=tipo
		@titulo=nil
	end
	
	def soyEdificable
		false
	end
	
	def to_s
		"Casilla{numeroCasilla=#{@numero}, coste=#{@coste}, tipo=#{@tipo}}"
	end
end
end