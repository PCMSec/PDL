package token;

public class Aritmetico {
	private String tipo;
	private int id;
	
	public Aritmetico(int id) {
		this.tipo="Aritmetico";
		this.id=id;
	}

	public String tokenizar() {
		
		return "<"+tipo+ ", " + id +">";
	}
	//De momento suma y resta solo para aritmetico
	public String tipo() {
		String res;
		if (id==Etiqueta.SUMA) res="suma";
		else if (id==Etiqueta.RESTA) res="resta";
		else {
			//Token no especificado o incorrecto
			System.out.println("Token erroneo: ARITMETICO" + id);
			return null;
		}
		return res;
	}
}
