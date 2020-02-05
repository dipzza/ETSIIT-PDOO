#encoding: utf-8
module ModeloQytetet
	require "singleton"
	class Dado
		attr_reader :valor
		
		include Singleton
		
		def initialize
			@valor
		end
		
		def tirar
			@valor = rand(6) + 1
		end
		
		def to_s
			"Dado{valor=#{@valor}}"
		end
	end
end
