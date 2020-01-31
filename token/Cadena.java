package token;
//Clase para implementar token para string
public class Cadena{
	private String tipo;
	private String cadena;
	
	public Cadena(String cadena) {
		this.tipo="Cadena";
		this.cadena=cadena;
	}

	public String tokenizar() {
		return "<" + tipo +", " + "\"" + cadena + "\"" + ">";
	}

	public String tipo() {
		return tipo;
	}
}
