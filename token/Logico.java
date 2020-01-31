package token;
//TODO terminar esto y revisar
public class Logico{
	private String tipo;
	private boolean logico;
	
	public Logico(boolean logico) {
		this.tipo="Logico";
		this.logico=logico;
	}
	public String tokenizar() {
		return "<"+tipo+", "+logico+">";
	}

	public String tipo() {
		return "Logico";
	}

}
