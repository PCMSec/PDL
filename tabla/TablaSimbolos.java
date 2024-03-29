package tabla;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import principal.Principal;
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
	//tipo devuelto por la funcion
	private TiposToken tipoDevuelto;

	public static PrintWriter writer;

	//acceder a estos datos para rehacer todo el tema de los tokens y los ID
	

	public ArrayList<String> getLexemasFuncion() {
		return lexemasFuncion;
	}
	public void setLexemasFuncion(ArrayList<String> lexemasFuncion) {
		this.lexemasFuncion = lexemasFuncion;
	}
	public ArrayList<TiposToken> getTiposLexemasFuncion() {
		return tiposLexemasFuncion;
	}
	public void setTiposLexemasFuncion(ArrayList<TiposToken> tiposLexemasFuncion) {
		this.tiposLexemasFuncion = tiposLexemasFuncion;
	}
	public ArrayList<Integer> getDesplazamientosFuncion() {
		return desplazamientosFuncion;
	}
	public void setDesplazamientosFuncion(ArrayList<Integer> desplazamientosFuncion) {
		this.desplazamientosFuncion = desplazamientosFuncion;
	}
	public TiposToken getTipoDevuelto() {
		return tipoDevuelto;
	}
	public void setTipoDevuelto(TiposToken tipoDevuelto) {
		this.tipoDevuelto = tipoDevuelto;
	}
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
		
		

		try {
			writer = new PrintWriter(Principal.directorioADevolver+File.separator+"ResultadoTablasSimbolos", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		this.setNombreFuncion(nombreTabla);
	}


	//Mejorado para que si global, solo mire la suya, y si no, mire la suya y luego la global
	//Devuelve TRUE si el lexema esta en TS o FALSE si no
	public boolean lexemaExiste(String lexema) {
		//auxiliar para ver si existe
		boolean existe=false;
		//si la tabla no tiene nada aun pues es falso
		int idTabla=this.getIdTabla();
		//GLOBAL
		if (idTabla==0) {
			if (this.getLexemas().size()<=0) {
				return false;
			}
			for (int i=0;i<this.getLexemas().size();i++) {
				if (this.getLexemas().get(i).equals(lexema) && (!existe)) {
					existe = true;
					return existe;
				}
			}
			return existe;
		}//FIN DE GLOBAL
		//ESTAMOS EN TABLA LOCAL
		else {
			//si el tamaño de los lexemas es positivo puedo iterar sobre ellos
			if (this.getLexemas().size()>=0) {
				//itero para ver si esta el lexema dentro
				for (int i=0;i<this.getLexemas().size();i++) {
					if (lexemas.get(i).equals(lexema) && (!existe)) {
						existe = true;
						return existe;
					}
				}
			}
			//si no se encuentra o no tiene nada en la local, miramos la global
			TablaSimbolos global=listaTablas.get(0);
			for (int i=0;i<global.getLexemas().size();i++) {
				if (global.getLexemas().get(i).equals(lexema) && (!existe)) {
					existe = true;
					return existe;
				}
			}
			//no existe
			return existe;
		}
		//FIN DE LOCAL
	}
	public boolean lexemaExisteLocal(String lexema) {
		boolean existe=false;
		if (this.getLexemas().size()>=0) {
			//itero para ver si esta el lexema dentro
			for (int i=0;i<this.getLexemas().size();i++) {
				if (lexemas.get(i).equals(lexema) && (!existe)) {
					existe = true;
					return existe;
				}
			}
		}
		return existe;
	}

	//Devuelve la posicion del lexema en Arraylist lexemas
	public int posicionLexema(String lexema) {
		int idTabla=this.getIdTabla();
		if (idTabla==0) {
			return (this.getLexemas().indexOf(lexema));
		}
		else {
			int aux=this.getLexemas().indexOf(lexema);
			if (aux==-1) {
				TablaSimbolos global=listaTablas.get(0);
				aux=global.getLexemas().indexOf(lexema);
			}
			return aux;
		}
	}


	//Devuelve el tipo que tenga el lexema
	public TiposToken getTipoLexema(String lexema) {
		//return (tiposLexemas.get(this.posicionLexema(lexema)));
		int idTabla=this.getIdTabla();
		if (idTabla==0) {
			return (this.getTiposLexemas().get(this.posicionLexema(lexema)));
		}
		else {
			int aux=this.getLexemas().indexOf(lexema);
			if (aux==-1) {
				TablaSimbolos global=listaTablas.get(0);
				return global.getTiposLexemas().get(global.posicionLexema(lexema));
			}
			else {
				return (this.getTiposLexemas().get(this.posicionLexema(lexema)));
			}

		}
	}

	//Devuelve el desplazamiento asociado a cada tipo de lexema
	public static int getDesplazamientoTipo(TiposToken Tipo) {
		int aux=-1;
		if (Tipo.equals(TiposToken.TINT)) {
			aux = 1; //1 palabra
		}
		else if (Tipo.equals(TiposToken.TSTRING)) {
			aux=64; //64 palabras
		}
		else if (Tipo.equals(TiposToken.TBOOLEAN)) {
			aux=1; //1 palabra
		}
		else if (Tipo.equals(TiposToken.TFUNC)) {
			aux=0;
		}
		return aux;
	}

	//Mete el lexema en la tabla de lexemas de la TS
	public void meterLexema(String lexema) {
		this.getLexemas().add(lexema);
	}

	//Mete el tipo del lexema en la tabla de tipos de la TS
	public void meterTipo(TiposToken tipo) {
		this.getTiposLexemas().add(tipo);
	}

	//Mete el desplazamiento del tipo en la lista de desplazamientos de la TS
	public void meterDesplazamiento(int desplazamiento) {
		this.getDesplazamientos().add(desplazamiento);
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
			if (tabla.getNombreFuncion().equals(null)) {
				writer.println("CONTENIDO DE LA TABLA # " + tabla.getIdTabla() + ":");
			}
			else {
				writer.println("CONTENIDO DE LA TABLA # " + tabla.getIdTabla() + " (de funcion "+ tabla.getNombreFuncion()+") :");
			}
			writer.println();
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
			//for (int i=auxTipos.size()-1;i>=0;i--) {
			for (int i=0;i<auxTipos.size();i++) {
				auxDesp.add(desp);
				desp+=TablaSimbolos.getDesplazamientoTipo(auxTipos.get(i));

			}
			Collections.reverse(auxLexemas);
			Collections.reverse(auxTipos);
			Collections.reverse(auxDesp);

			for (int i=0;i<tabla.getLexemas().size();i++) {
				writer.println("* LEXEMA: '"+auxLexemas.get(i)+"'");
				writer.println("ATRIBUTOS:");
				writer.println("+ tipo: '" + auxTipos.get(i) +"'");
				
				
				//si no es funcion, imprime normal
				if (!auxTipos.get(i).equals(TiposToken.TFUNC)) {
					writer.println("+ despl : " + auxDesp.get(i));
					
				}
				//es funcion lo que tengo y tengo que acceder a los parametros de la TFUNC
				else {
					TablaSimbolos aux=null;;
					for (int j=0;j<listaTablas.size();j++) {
						if (listaTablas.get(j).getNombreFuncion().equals(auxLexemas.get(i))){
							aux=listaTablas.get(j);
						}
					}
					writer.println(" +NumParam: " + aux.getTiposLexemasFuncion().size());
					String espaciado="  ";
					for (int k=0;k<aux.getTiposLexemasFuncion().size();k++) {
						int auxSuma=k+1;
						writer.println(espaciado+"+TipoParam"+auxSuma+": '"+aux.getTiposLexemasFuncion().get(k)+"'");
						writer.println(espaciado+"+ModoParam"+auxSuma+": "+"1     (es por valor)");
						espaciado+=" ";
					}
					writer.println(espaciado+"+TipoRetorno: '"+aux.getTipoDevuelto()+"'");
					writer.println(espaciado+" +EtiqFuncion: 'Et"+aux.getNombreFuncion()+"'");
				}
				writer.println();
			}
			writer.println("--------- ----------");
			writer.println();
		}
		writer.close();
	}
	
	//puede comparar en global o en local si es recursiva
	public boolean compararFuncion(ArrayList<TiposToken> tiposFuncion) {
		if (tiposFuncion.equals(null)&&this.getTiposLexemasFuncion().equals(null)) {
			return false;
		}
		boolean aux=false;
		if (tiposFuncion.size()==this.getTiposLexemasFuncion().size()) {
			for (int i=0;i<tiposFuncion.size();i++) {
				if (!tiposFuncion.get(i).equals(this.getTiposLexemasFuncion().get(i))) {
					return false;
				}	
			}
			aux=true;
		}
		//distinto tamano, error como una casa
		else {
			return false;
		}
		return aux;
	}
}
