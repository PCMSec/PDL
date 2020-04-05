package tabla;

import java.util.ArrayList;

import token.TiposToken;

public class TablaSimbolos {
	//Lista ESTATICA con todas las tablas de simbolos a lo largo del programa
	public static ArrayList<TablaSimbolos> listaTablas=new ArrayList<TablaSimbolos>();
	//Si es funcion tiene aqui el nombre de funcion
	private String nombreFuncion;
	//Numero de la tabla
	private int idTabla;
	//contador general para todas las tablas, para poner el ID actual
	private static int inicial=0;
	//Conjunto de lexemas de la tabla actual
	private ArrayList<String> lexemas;
	//Tipos de lexema en la TS
	private ArrayList<TiposToken> tiposLexemas;
	//Conjunto de desplazamiento asociado a los tipos de los lexemas
	private ArrayList<Integer> desplazamientos;
	//booleano para saber si es TS Global o TS de func
	public boolean esFuncion;
	
	public boolean esFuncion() {
		return esFuncion;
	}

	public void setEsFuncion(boolean esFuncion) {
		this.esFuncion = esFuncion;
	}

	public static ArrayList<TablaSimbolos> getListaTablas() {
		return listaTablas;
	}

	public static void setListaTablas(ArrayList<TablaSimbolos> listaTablas) {
		TablaSimbolos.listaTablas = listaTablas;
	}

	public String getNombreFuncion() {
		return nombreFuncion;
	}

	public void setNombreFuncion(String nombreFuncion) {
		this.nombreFuncion = nombreFuncion;
	}

	public int getIdTabla() {
		return idTabla;
	}

	public void setIdTabla(int idTabla) {
		this.idTabla = idTabla;
	}

	public static int getInicial() {
		return inicial;
	}

	public static void setInicial(int inicial) {
		TablaSimbolos.inicial = inicial;
	}

	public ArrayList<String> getLexemas() {
		return lexemas;
	}

	public void setLexemas(ArrayList<String> lexemas) {
		this.lexemas = lexemas;
	}

	public ArrayList<TiposToken> getTiposLexemas() {
		return tiposLexemas;
	}

	public void setTiposLexemas(ArrayList<TiposToken> tiposLexemas) {
		this.tiposLexemas = tiposLexemas;
	}

	public ArrayList<Integer> getDesplazamientos() {
		return desplazamientos;
	}

	public void setDesplazamientos(ArrayList<Integer> desplazamientos) {
		this.desplazamientos = desplazamientos;
	}
	
	public TablaSimbolos() {
		lexemas=new ArrayList<String>();
		tiposLexemas=new ArrayList<TiposToken>();
		desplazamientos=new ArrayList<Integer>();
		this.idTabla=inicial;
		inicial++;
	}
	
	//Devuelve true si el lexema esta en TS o false si no
	public boolean lexemaExiste(String lexema) {
		boolean aux=false;
		for (int i=0;i<lexemas.size();i++) {
			if (lexemas.get(i).equals(lexema) && (!aux)) {
				aux = true;
				break;
			}
		}
		return aux;
	}
	
	//Devuelve la posicion del lexema en Arraylist lexemas
	public int posicionLexema(String lexema) {
		return (lexemas.indexOf(lexema));
	}
	
	
	//Devuelve el tipo que tenga el lexema
	public TiposToken getTipoLexema(String lexema) {
		return (tiposLexemas.get(this.posicionLexema(lexema)));
	}
	
	//Devuelve el desplazamiento asociado a cada tipo de lexema
	public static int getDesplazamientoTipo(TiposToken Tipo) {
		int aux=-1;
		if (Tipo.equals(TiposToken.T_INT)) {
			aux = 2;
		}
		else if (Tipo.equals(TiposToken.T_STRING)) {
			aux=128;
		}
		else if (Tipo.equals(TiposToken.T_BOOLEAN)) {
			aux=2;
		}
		return aux;
	}
	
	//Mete el lexema en la tabla de lexemas de la TS
	public void meterLexema(String lexema) {
		lexemas.add(0, lexema);
	}
	
	//Mete el tipo del lexema en la tabla de tipos de la TS
	public void meterTipo(TiposToken tipo) {
		tiposLexemas.add(0, tipo);
	}
	
	//Mete el desplazamiento del tipo en la lista de desplazamientos de la TS
	public void meterDesplazamiento(int desplazamiento) {
		desplazamientos.add(0, desplazamiento);
	}
	
}
