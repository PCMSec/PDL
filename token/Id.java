package token;

public class Id extends Token{
	private String tipo;
	private int pos;
	
	
	public Id(String tipo,int pos){
		super(tipo, pos);
		this.tipo="Identificador";
		//this.pos=pos;
	}
	
	public String tokenizar() {
		return "<"+tipo+", "+ pos +">";
	}

	public String tipo() {
		return tipo;
	}
	
	public int getPos() {
		return pos;
	}
	


}
