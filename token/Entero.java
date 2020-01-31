package token;
//Clase para implementar token para numeros enteros
public class Entero{
	private String tipo;
	private int valor;
	
	public Entero (int valor) {
		this.tipo="Entero";
		this.valor=valor;
	}
	public String tokenizar() {
		return "<"+tipo+", " + valor +">";
	}

	public String tipo() {
		return tipo;
	}

}
