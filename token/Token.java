package token;
import token.TiposToken;
//Token. Cuando es un {, { ... el valor es cero. Si necesita mas info, usamos valor != 0
public class Token {
	//Tipos dentro de los especificados
	private TiposToken token;
	//Valor para pos TS
	private int valor=0;
	//Valor para cadenas
	private String lexema="";
	
	
	//Constructores por defecto, tipo de token y su valor
	public Token(TiposToken token, int valor) {
		this.token=token;
		this.valor=valor;
	}
	
	//Constructor por defecto, tipo de token y el lexema que posee
	public Token(TiposToken token, String lexema) {
		this.token=token;
		this.lexema=lexema;
	}
	//Constructor para solo token
	public Token(TiposToken token) {
		this.token=token;
	}

	public TiposToken getToken() {
		return token;
	}

	public void setToken(TiposToken token) {
		this.token = token;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}


	//Imprimir estructura pedida
	public String tokenizar() {
		if (valor == 0 && lexema =="") {
			return "<" + token + ", " + valor + ">";
		}
		else if (valor != 0) {
			return "<" + token + ", " + valor + ">";
		}		
		else {	
			return "<" + token + ", " + lexema + ">";
		}
	}
}
