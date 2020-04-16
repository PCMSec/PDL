package tabla;

import java.util.ArrayList;
import java.util.Collections;

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
	//lexemas que tiene como parametros la funcion que sea
	private ArrayList<String> lexemasFuncion;
	//Tipos de los lexemas de los parametros de entrada
	private ArrayList<TiposToken> tiposLexemasFuncion;
	//Conjunto de desplazamiento asociado a los tipos de los lexemas de la funcion
	private ArrayList<Integer> desplazamientosFuncion;




	//getter para obtener lista de las tablas de simbolos
	public static ArrayList<TablaSimbolos> getListaTablas() {
		return listaTablas;
	}
	//setter listaS en plural
	public static void setListaTablas(ArrayList<TablaSimbolos> listaTablas) {
		TablaSimbolos.listaTablas = listaTablas;
	}
	//obtener nombre de la funcion actual o si es una global, GLOBAL
	public String getNombreFuncion() {
		return nombreFuncion;
	}
	//poner el nombre de la funcion con un string
	public void setNombreFuncion(String nombreFuncion) {
		this.nombreFuncion = nombreFuncion;
	}
	//obtener el id de la tabla actual
	public int getIdTabla() {
		return idTabla;
	}
	//settear el id de la tabla
	public void setIdTabla(int idTabla) {
		this.idTabla = idTabla;
	}
	//obtener inicial que es el estatico
	public static int getInicial() {
		return inicial;
	}
	//settear inicial que es estatico
	public static void setInicial(int inicial) {
		TablaSimbolos.inicial = inicial;
	}
	//Obtener lista de lexemas
	public ArrayList<String> getLexemas() {
		return lexemas;
	}
	//settearla
	public void setLexemas(ArrayList<String> lexemas) {
		this.lexemas = lexemas;
	}
	//etc
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

	public TablaSimbolos(String nombreTabla) {
		//lista de lexemas que se encuentran en la TS
		lexemas=new ArrayList<String>();
		//lista de tipos ordenados de los lexemas en TS
		tiposLexemas=new ArrayList<TiposToken>();
		//lo mismo pero con los desplazamientos
		desplazamientos=new ArrayList<Integer>();
		//igual pero con parametros
		lexemasFuncion=new ArrayList<String>();
		//igual pero con parametros
		tiposLexemasFuncion=new ArrayList<TiposToken>();
		//igual pero con parametros
		desplazamientosFuncion=new ArrayList<Integer>();
		//meter la TablaSimbolos actual en la lista con todas las tablas
		listaTablas.add(this);
		//id de la tabla es el que apunte inicial
		this.idTabla=inicial;
		//aumentar numero de la tabla
		inicial++;
		//nombre de la tabla
		this.setNombreFuncion(nombreTabla);
	}



	//Devuelve TRUE si el lexema esta en TS o FALSE si no
	public boolean lexemaExiste(String lexema) {
		boolean aux=false;
		if (lexemas.size()<=0) {
			return false;
		}
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
		lexemas.add(lexema);
	}

	//Mete el tipo del lexema en la tabla de tipos de la TS
	public void meterTipo(TiposToken tipo) {
		tiposLexemas.add(tipo);
	}

	//Mete el desplazamiento del tipo en la lista de desplazamientos de la TS
	public void meterDesplazamiento(int desplazamiento) {
		desplazamientos.add(desplazamiento);
	}
	//Meter el lexema que sea como entrada de la funcion
	public void meterLexemaFuncion(String lexema) {
		lexemasFuncion.add(lexema);
	}

	//Mete el tipo del lexema en la tabla de tipos de la TS de la funcion
	public void meterTipoFuncion(TiposToken tipo) {
		tiposLexemasFuncion.add(tipo);
	}

	//Mete el desplazamiento del tipo en la lista de desplazamientos de la TS de la funcion
	public void meterDesplazamientoFuncion(int desplazamiento) {
		desplazamientosFuncion.add(desplazamiento);
	}
	//ve si el elemento i de entrada a funcion es de igual tipo
	public boolean igualParametro(TiposToken tipo, int i) {
		boolean aux=false;
		if (tiposLexemasFuncion.get(i).equals(tipo)) {
			aux=true;
		}
		return aux;
	}
	public int numeroParametrosEntrada() {
		return tiposLexemasFuncion.size();
	}
	
	public static void imprimirTablas() {
		for (TablaSimbolos tabla:listaTablas) {
			System.out.println("CONTENIDO DE LA TABLA # " + tabla.getIdTabla() + ":");
			System.out.println();
			int desp=0;
			ArrayList<String> auxLexemas=new ArrayList<String>();
			ArrayList<TiposToken> auxTipos=new ArrayList<TiposToken>();
			ArrayList<Integer> auxDesp=new ArrayList<Integer>();
			for (int i=0;i<tabla.getLexemas().size();i++) {
				String lexema=tabla.getLexemas().get(i);
				auxLexemas.add(lexema);
				
				TiposToken tipoLexema=tabla.getTipoLexema(tabla.getLexemas().get(i));
				auxTipos.add(tipoLexema);
			}
			for (int i=auxTipos.size()-1;i>=0;i--) {
				auxDesp.add(desp);
				desp+=TablaSimbolos.getDesplazamientoTipo(auxTipos.get(i));
			}
			Collections.reverse(auxLexemas);
			Collections.reverse(auxTipos);
			Collections.reverse(auxDesp);
			for (int i=0;i<tabla.getLexemas().size();i++) {
				System.out.println("* LEXEMA: '"+auxLexemas.get(i)+"'");
				System.out.println("ATRIBUTOS");
				System.out.println("+ tipo: " + auxTipos.get(i));
				/*if (auxTipos.get(i).equals(TiposToken.T_FUNC)) {
					//TODO esta mal porque tengo que buscar la tabla primero y luego acceder con esa tabla de funcion
					System.out.println("  + numParam: " + tabla.lexemasFuncion.size());
					for (int j=0;i<tabla.lexemasFuncion.size();i++) {
						System.out.println("   TipoParam"+i+": " +tabla.);
					}
				}TODO poner el else el desp porque las funciones no tienen desp: else{}*/
				System.out.println("+ despl : " + auxDesp.get(i));
				System.out.println("--------- ----------");
			}
		}
	}
}
