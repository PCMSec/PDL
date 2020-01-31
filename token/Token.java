package token;
//Interfaz con los metodos basicos de un token
public class Token {
	String tipo;
	int adicional;
	
	public Token(String tipo, int adicional) {
		this.tipo=tipo;
		this.adicional=adicional;
	}
	
	public String tokenizar() {
		return "<" + tipo + ", " + adicional +">";
	}
	public String tipo() {
		return tipo;
	}
}
