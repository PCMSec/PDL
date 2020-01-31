package token;

public class Relacional {
		private String tipo;
		private int id;
		
		public Relacional(String tipo, int id) {
			this.tipo="Relacional";
			this.id=id;
		}
		
		public String tokenizar() {
			return "<"+tipo+", "+ id +">";
		}
		
		public String tipo() {
			String res="";
			if (id==Etiqueta.MAYOR)res="mayor";
			else if (id==Etiqueta.MENOR)res="menor";
			else {
				//Token no especificado en el esquema
				System.out.println("Token erroneo: ARITMETICO" + id);
				return null;
			}
			return res;
		}
}
