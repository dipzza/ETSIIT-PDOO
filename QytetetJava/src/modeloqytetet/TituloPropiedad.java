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
public class TituloPropiedad 
{
	private String nombre;
	private boolean hipotecada;
	private int precioCompra;
	private int alquilerBase;
	private float factorRevalorizacion;
	private int hipotecaBase;
	private int precioEdificar;
	private int numHoteles;
	private int numCasas;
	private Jugador propietario;
	
	
	public TituloPropiedad(String nombr, int pCompra, int aBase, float factorRev, int hipotecaBas, int precioEd) {
		hipotecada=false;
		numHoteles=0;
		numCasas=0;
		propietario= null;
		
		nombre=nombr;
		precioCompra=pCompra;
		alquilerBase=aBase;
		factorRevalorizacion=factorRev;
		hipotecaBase=hipotecaBas;
		precioEdificar=precioEd;
	}
	
	int calcularCosteCancelar()
	{
		return (int) (1.1 * calcularCosteHipotecar());
	}
	
	int calcularCosteHipotecar()
	{
		return (hipotecaBase + numCasas/2 * hipotecaBase + numHoteles * hipotecaBase);
	}
	
	int calcularImporteAlquiler()
	{
		return (alquilerBase + numCasas/2 + numHoteles*2);
	}
	
	int calcularPrecioVenta()
	{
		return (int) (precioCompra + (numCasas + numHoteles) * precioEdificar * factorRevalorizacion);
	}
	
	void cancelarHipoteca()
	{
		hipotecada = false;
	}
	
	//void cobrarAlquiler(int coste)
	
	void edificarCasa(){
		numCasas++;
	}
	
	void edificarHotel(){
		numCasas -= 4;
		numHoteles++;
	}
	
	Jugador getPropietario(){
		return propietario;
	}
	
	int hipotecar()
	{
		setHipotecada(true);
		
		return calcularCosteHipotecar();
	}
	
	int pagarAlquiler()
	{
		int costeAlquiler = calcularImporteAlquiler();
		
		propietario.modificarSaldo(costeAlquiler);
		
		return costeAlquiler;
	}
	
	boolean propietarioEncarcelado(){
		return propietario.getEncarcelado();
	}

	String getNombre() {
		return nombre;
	}

	boolean getHipotecada() {
		return hipotecada;
	}

	int getPrecioCompra() {
		return precioCompra;
	}

	int getAlquilerBase() {
		return alquilerBase;
	}

	float getFactorRevalorizacion() {
		return factorRevalorizacion;
	}

	int getHipotecaBase() {
		return hipotecaBase;
	}

	int getPrecioEdificar() {
		return precioEdificar;
	}

	int getNumHoteles() {
		return numHoteles;
	}

	int getNumCasas() {
		return numCasas;
	}

	void setHipotecada(boolean hipotecada) {
		this.hipotecada = hipotecada;
	}
	
	void setPropietario(Jugador propietario)
	{
		this.propietario = propietario;
	}
	
	boolean tengoPropietario()
	{
		return propietario != null;
	}

	@Override
	public String toString() {
		if (propietario == null || propietario.getPropiedades().contains(this))
			return "TituloPropiedad{" + "nombre=" + nombre + ", hipotecada=" + hipotecada + ", precioCompra=" + precioCompra + ", alquilerBase=" + alquilerBase + ", factorRevalorizacion=" + factorRevalorizacion + ", hipotecaBase=" + hipotecaBase + ", precioEdificar=" + precioEdificar + ", numHoteles=" + numHoteles + ", numCasas=" + numCasas + '}' + "\n";
		else
			return "TituloPropiedad{" + "nombre=" + nombre + ", hipotecada=" + hipotecada + ", precioCompra=" + precioCompra + ", alquilerBase=" + alquilerBase + ", factorRevalorizacion=" + factorRevalorizacion + ", hipotecaBase=" + hipotecaBase + ", precioEdificar=" + precioEdificar + ", numHoteles=" + numHoteles + ", numCasas=" + numCasas + ", propietario=" + propietario.toString() + '}' + "\n";
	}
}