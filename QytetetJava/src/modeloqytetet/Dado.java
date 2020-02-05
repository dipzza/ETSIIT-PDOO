package modeloqytetet;

import java.util.Random;

public class Dado {
	private static final Dado dado = new Dado();
	private int valor;
	private Random generador;
	
	private Dado()
	{
		generador = new Random();
	}
	
	public static Dado getDado(){
		return dado;
	}
	
	int tirar()
	{
		valor = 1 + generador.nextInt(6);
		return valor;
	}

	public int getValor()
	{
		return valor;
	}

	@Override
	public String toString()
	{
		return "Dado{valor=" + valor + '}';
	}
}