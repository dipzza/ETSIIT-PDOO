/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

/**
 *
 * @author jakerxd
 */
public class Especulador extends Jugador 
{
	private int fianza;
	
	Especulador(Jugador unJugador, int fianza)
	{
		super(unJugador);
		this.fianza = fianza;
	}
	
	@Override
	protected void pagarImpuesto()
	{
		modificarSaldo(-getCasillaActual().getCoste()/2);
	}
	
	protected Especulador convertirme(int fianza)
	{
		this.fianza = fianza;
		
		return this;
	}
	
	protected boolean deboIrACarcel()
	{
		return ( super.deboIrACarcel() && !pagarFianza() );
	}
	
	private boolean pagarFianza()
	{
		boolean pagar = (getSaldo() > fianza);
		
		if(pagar)
			modificarSaldo(-fianza);
		
		return pagar;
	}
	
	protected boolean puedoEdificarCasa(TituloPropiedad titulo)
	{
		return (titulo.getNumCasas() < 8);
	}
	
	protected boolean puedoEdificarHotel(TituloPropiedad titulo)
	{
		return (titulo.getNumCasas() > 3 && titulo.getNumHoteles() < 8);
	}
	
	@Override
	public String toString()
	{
		return super.toString();
	}
}