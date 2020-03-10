package tabla;

import token.TiposToken;

public class EntradaTS {
	//ID que lo identifica, id o funcion
	private String lexema;
	//Tipo del ID, que puede ser tipo devuelto o tipo simple
	private TiposToken tipo;
	//Desplazamiento en memoria del tipo desde 0
	private static int desplazamiento;
	//Para ver si el ID es una funcion. TRabajo facil porque solo hay 2 entornos.
	//Si id no esta en la funcion, mirar el global. Si no está, es que no está y fin.
	private boolean esFuncion=false;
	//Si funcion, numero de parametros
	private int nParametros;
}
