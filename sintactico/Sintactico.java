package sintactico;

import token.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import errores.*;
import errores.Error;



import principal.Principal;
import tabla.Tipo;
import tabla.TablaSimbolos;
import token.TiposToken;


public class Sintactico {
	//
	private int contador=0;
	private String auxS2;
	private String auxV2;
	//NO BORRAR NO OLVIDAR IMPORTANTEtipos locales para comparar con la funcion en si
	private ArrayList<TiposToken> tiposFuncion;
	//linea en la que nos encontramos
	public int linea;
	//posicion de los tokens en la lista
	private int posicion;
	//auxiliar de token para empezar a leer
	private static Token aux;
	//booleano para ver si estamos o no DENTRO en una funcion
	public boolean dentroFuncion=false;
	//string global con el nombre de la funcion
	public String nombreFuncion=null;
	//lista de tokens normal y lista de tokens sin tokens de end of line
	private ArrayList<Token> listaTokens;
	private ArrayList<Token> listaTokensSinEol;
	//una tabla global y otra actual que al inicio apuntan al mismo sitio porque no hay locales
	public TablaSimbolos global;
	public TablaSimbolos actual;
	//nombre de archivo donde guardar el sintactico
	String filename="/home/pablo/eclipse-workspace/PDL/docs/sintactico.txt";
	//Objeto para escribir en archivo
	public static PrintWriter writer;
	//Constructor del sintactico
	public Sintactico(ArrayList<Token> listaTokens) {
		//Se empieza en la linea 1 del archivo
		this.contador=0;
		this.linea=1;
		//empezamos en la primera posicion de los tokens
		this.posicion=0;
		//inicializar normal
		this.listaTokens = listaTokens;
		//iniciar la que no tiene end of line
		this.listaTokensSinEol=quitarEOL();
		
		
		try {
			//se intenta escribir, si no se puede dara error en el catch
			writer = new PrintWriter(Principal.directorioADevolver+File.separator+"ResultadoSintactico.txt", "UTF-8");
			writer.print("D ");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//leo el primer token a la entrada para iniciar P
		aux=leerToken();
		//Tabla global y actual apuntando a lo mismo, cambiara mas adelante para reflejar las TS de funciones
		global = new TablaSimbolos("");
		actual = global;
		global.setNombreFuncion("GLOBAL");
		tiposFuncion=new ArrayList<TiposToken>();
	}

	public ArrayList<TiposToken> getTiposFuncion() {
		return tiposFuncion;
	}


	public void setTiposFuncion(ArrayList<TiposToken> tiposFuncion) {
		this.tiposFuncion = tiposFuncion;
	}


	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	//Gramatica propiamente dicha
	public void P() {

		//B
		if (tokenIgual(TiposToken.T_VAR)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_IF)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_SWITCH)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}
		//S
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}//F
		else if (tokenIgual(TiposToken.T_FUNC)) {
			escribirFichero(2);
			Tipo F=F();
			if (F.getTipoToken().equals(TiposToken.T_ERROR)) return;
			P();
			return;
		}//eof
		else if (tokenIgual(TiposToken.EOF)) {
			escribirFichero(3);
			writer.close();
			return;
		}
		else if (tokenIgual(TiposToken.EOL)) {
			aux=leerToken();
			P();
			return;
		}
		else {
			//error
			return;
		}
	}

	public Tipo B() {
		//Tipo a devolver por el procedure B
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//LEO UN VAR: var ...
		if (tokenIgual(TiposToken.T_VAR)) {
			aux=leerToken();
			escribirFichero(4);
			//obtengo tipo de variable
			Tipo T=T();
			//Tipo no compatible con los 3 soportados, int boolean o string
			if (T.getTipoToken().equals(TiposToken.T_ERROR)){
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			//continuo sin error
			//LEO VAR TIPO ID...
			if (tokenIgual(TiposToken.T_ID)) {
				//leo hasta: var ENTERO|CADENA|BOOLEAN ID
				if (actual.lexemaExisteLocal(aux.getLexema())) {
					//System.out.println("Var ya declarada anteriormente");
					//Si existe ya en la local, error
					Error.writer.write("SEMANTICO: ERROR, VARIABLE "+aux.getLexema()+" YA DEFINIDA CON ANTERIORIDAD");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
				//No existe en local, o ya esta en global y podemos hacerla local
				//esto es solo declaracion, por lo que no hay que usar nada
				String id=aux.getLexema();
				aux=leerToken();
				Tipo B2=B2();
				if (B2.getTipoToken().equals(TiposToken.T_ERROR)) {
					Error.writer.write("SEMANTICO: ERROR, TIPO EN B2 NO COMPATIBLE");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
				//var TIPO id
				if (B2.getTipoToken().equals(TiposToken.T_VACIO)) {
					actual.meterLexema(id);
					actual.meterTipo(T.getTipoToken());
					actual.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				}
				//var TIPO id = COMPATIBLE CON TIPO
				else if (B2.getTipoToken().equals(T.getTipoToken())) {
					actual.meterLexema(id);
					actual.meterTipo(T.getTipoToken());
					actual.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				}
				else {
					//error, ninguno de los dos formas de meterlo bien
					Error.writer.write("SEMANTICO: ERROR, LOS TIPOS "+B2.getTipoToken()+" Y "+T.getTipoToken()+" NO SON COMPATIBLES");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
				//fin de VAR TIPO ID ; O DE VAR TIPO ID = E ;
				if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
					//ALL IS OK; FIN
					devolver=new Tipo(TiposToken.T_OK);
					aux=leerToken();
				}
				else {
					Error.writer.write("SEMANTICO: SENTENCIA NO VALIDA");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
					//falta el punto y coma del final
				}
			}
			else {
				//lei var tipo pero luego no viene ID
				Error.writer.write("SEMANTICO: ERROR EN, EL TIPO "+T.getTipoToken() +" NO VA ACOMPAÑADO DE NINGUN ID");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			
			return devolver;
		}
		//INICIO IF
		else if (tokenIgual(TiposToken.T_IF)) {
			aux=leerToken();
			escribirFichero(5);

			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();
				if (!E.getTipoToken().equals(TiposToken.T_BOOLEAN)) {
					//error porque E no es un booleano
					Error.writer.write("SEMANTICO: ERROR , LA EXPRESION DEL IF NO ES DE TIPO BOOLEANO");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
				else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
					devolver=new Tipo(TiposToken.T_OK);
					aux=leerToken();
					//TODO mejorar lo devuelto quiza
					Tipo S=S();
				}
				else {
					Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN CIERRE DE PARENTESIS EN EL IF");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
					//error, no viene parentesis cierra
				}
			}
			else {
				//no abre parentesis despues del if, error
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ABRIR DE PARENTESIS EN EL IF");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			
			return devolver;
		}//FIN DE IF
		//INICIO SWITCH
		else if (tokenIgual(TiposToken.T_SWITCH)) {
			aux=leerToken();
			escribirFichero(7);

			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();
				if (!E.getTipoToken().equals(TiposToken.T_INT)) {
					//no es entero y por tanto es error en el switch y fin
					Error.writer.write("SEMANTICO: ERROR , LA EXPRESION DEL SWITCH NO ES UN ENTERO");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
				
				else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
					aux=leerToken();
					if (tokenIgual(TiposToken.T_LLAVEABRE)) {
						aux=leerToken();
						CASE();	
						if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
							devolver=new Tipo(TiposToken.T_OK);
							aux=leerToken();
						}
						else {
							//error sin llave cierra al final
							Error.writer.write("SEMANTICO: ERROR , SE ESPERABA CERRAR LLAVE EN EL SWITCH");
							devolver=new Tipo(TiposToken.T_ERROR);
							return devolver;
						}
					}
					else {
						Error.writer.write("SEMANTICO: ERROR , SE ESPERABA ABRIR LLAVE EN EL SWITCH");
						devolver=new Tipo(TiposToken.T_ERROR);
						return devolver;
						//error no abre llave al principio
					}
				}
				else {
					Error.writer.write("SEMANTICO: ERROR , SE ESPERABA CERRAR PARENTESIS EN EL SWITCH");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
					//error porque no hay parentesis cierra
				}
			}
			else {
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA ABRIR PARENTESIS EN EL SWITCH");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
				//no abre parentesis despues del switch
			}
			return devolver;
		}//FIM SWITCH
		//S
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(6);
			S();
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			escribirFichero(6);
			S();
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			escribirFichero(6);
			S();
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(6);
			S();
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else {
			Error.writer.write("SEMANTICO: ERROR , NO ES ENCONTRO NINGUN TOKEN COMPATIBLE");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}
	public Tipo B2() {
		//aux que tengo que devolver
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		if (tokenIgual(TiposToken.T_IGUAL)) {
			aux=leerToken();
			escribirFichero(8);
			Tipo E=E();
			// hemos leido "= EXPRESION"
			devolver=new Tipo(E.getTipoToken());
			return devolver;

		}
		//follow
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(9);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;
		}//error
		else {
			Error.writer.write("SEMANTICO: ERROR , NO SE ENCONTRO NINGUN TOKEN COMPATIBLE CON B2");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}
	//devuelvo el tipo de T, que es o int o string o boolean
	public Tipo T() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		if (tokenIgual(TiposToken.T_INT)) {
			escribirFichero(10);
			aux=leerToken();
			devolver=new Tipo(TiposToken.T_INT);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_STRING)) {
			escribirFichero(11);
			aux=leerToken();
			devolver=new Tipo(TiposToken.T_STRING);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_BOOLEAN)) {
			escribirFichero(12);
			aux=leerToken();
			devolver=new Tipo(TiposToken.T_BOOLEAN);
			return devolver;
		}
		else {
			//ninguno de los tipos basicos bro
			Error.writer.write("SEMANTICO: ERROR , NO ES UN TIPO VALIDO PARA T");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}
	//TODO hacer cosas con el tipo del B para ver si hay error
	public void C() {
		if (tokenIgual(TiposToken.T_VAR)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.T_IF)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.T_SWITCH)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		//S
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(13);
			B();
			C();
			return;
		}//follow
		else if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
			//escribir en fichero y return
			escribirFichero(14);
			return;
		}
		else {
			return;
		}
	}
	public Tipo S() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		if (tokenIgual(TiposToken.T_ID)) {
			
			auxS2=aux.getLexema();
		//	System.out.println("Estamos en "+auxS2);
			
			if (!actual.lexemaExiste(auxS2)) {
				//LEXEMA NO EXISTE EN TS
				global.meterLexema(auxS2);
				global.meterTipo(TiposToken.T_INT);
				global.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(TiposToken.T_INT));
			}
			//a partir de aqui el lexema existe, ya sea GLOBAL o LOCAL
			aux=leerToken();
			escribirFichero(15);
			Tipo S2=S2();
			//System.out.println("Al salir de S2 tenemos "+S2.getTipoToken());
			//Al llamar a s2 no reconoce cuando es func
			//System.out.println("OJO A ESTO "+actual.getTipoLexema(id));
			if (S2.getTipoToken().equals(TiposToken.T_INT) && actual.getTipoLexema(auxS2).equals(TiposToken.T_INT)) {
				devolver=new Tipo(TiposToken.T_INT);
			}
			
			//cambiado esto revisar bugs
			else if (actual.getTipoLexema(auxS2).equals(TiposToken.T_INT) && S2.getTipoToken().equals(TiposToken.T_VACIO)) {
				Error.writer.write("SEMANTICO: ERROR , LA FUNCION LLAMADA NO EXISTE  \n");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			else if (S2.getTipoToken().equals(TiposToken.T_VACIO)) {
				devolver=new Tipo(TiposToken.T_OK);
			}
			else {
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			//TODO poner aqui un caso de error?
			//rollo else{}
			//esto va para el final del punto y coma
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				aux=leerToken();
			}
			else {
				//TODO esto da error, se comenta y fuera
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			auxS2=null;
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			
			aux=leerToken();
			escribirFichero(16);
			Tipo X=X();
			//TODO revisar esto que tiene mala pinta
			
			TablaSimbolos auxaux=null;
			for (TablaSimbolos tabla : TablaSimbolos.getListaTablas()) {
				if (tabla.getNombreFuncion().equals(nombreFuncion)){
					auxaux=tabla;
				}
			}
			//si no son iguales los tipos devueltos error
			if (!dentroFuncion) {
				Error.writer.write("SEMANTICO: ERROR , USADO RETURN SIN ESTAR DENTRO DE UNA FUNCION");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			if (!X.getTipoToken().equals(auxaux.getTipoDevuelto())) {
				Error.writer.write("SEMANTICO: ERROR , TIPO DE FUNCION Y TIPO DEVUELTO SON DISTINTOS");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				devolver=new Tipo(X.getTipoToken());
				aux=leerToken();
			}
			else {
				//error falta el punto y coma del final
				Error.writer.write("SEMANTICO: ERROR , FALTA EL PUNTO Y COMA FINAL");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;	
			}
			//salimos de la funcion
			
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			aux=leerToken();
			escribirFichero(17);

			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();

				if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
					aux=leerToken();
					if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
						aux=leerToken();
					}
				}
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(18);
			aux=leerToken();
			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				if (tokenIgual(TiposToken.T_ID)) {
					//ya tenemos el id
					String id=aux.getLexema();
					//si no existe, es entera

					if (!actual.lexemaExiste(id)) {
						global.meterLexema(id);
						global.meterTipo(TiposToken.T_INT);
						global.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(TiposToken.T_INT));
					}
					if (actual.getTipoLexema(id).equals(TiposToken.T_BOOLEAN)) {
						Error.writer.write("SEMANTICO: ERROR , NO SE PUEDE METER UN BOOLEANO EN EL INPUT");
						devolver=new Tipo(TiposToken.T_ERROR);
						return devolver;	
					}
					aux=leerToken();
					if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
						aux=leerToken();
						if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
							aux=leerToken();
						}
						else {
							//error no hay punto y coma
							Error.writer.write("SEMANTICO: ERROR , FALTA EL PUNTO Y COMA DEL FINAL");
							devolver=new Tipo(TiposToken.T_ERROR);
							return devolver;	
						}
					}
					else {
						//erro no hay parentesis cierra
						Error.writer.write("SEMANTICO: ERROR , NO CIERRA PARENTESIS");
						devolver=new Tipo(TiposToken.T_ERROR);
						return devolver;
					}
				}
				else {
					//error no hay id
					Error.writer.write("SEMANTICO: ERROR , FALTA EL ID DEL INPUT");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
			}
			else {
				//error no abre parentesis
				Error.writer.write("SEMANTICO: ERROR , FALTA EL PARENTESIS ABRE DEL INPUT");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			return devolver;
		}
		else {
			//error no es ninguno de estos casos
			Error.writer.write("SEMANTICO: ERROR , NO SE RECONOCE NINGUNA SENTENCIA VALIDA");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
			
		}
	}
	public Tipo S2() {
		//System.out.println("Entrando en S2");
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_POSTDECREMENTO)) {
			aux=leerToken();
			escribirFichero(19);
			devolver=new Tipo(TiposToken.T_INT);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_IGUAL)) {
			aux=leerToken();
			escribirFichero(20);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		//TODO error cuando lee id del anterior y lo toma como entero y luego llama a esto. comprobar si es func o no
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			//anyadido nuevo
			TablaSimbolos comparator=null;
			for (TablaSimbolos tabla : TablaSimbolos.getListaTablas()) {
				if (tabla.getNombreFuncion().equals(auxS2)){
					comparator=tabla;
				}
			}
			if (comparator==null) {
				Error.writer.write("SEMANTICO: ERROR , LA FUNCION " +auxS2 +" NO EXISTE ");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			if (dentroFuncion) {
				if (!nombreFuncion.equals(auxS2)) {
					Error.writer.write("SEMANTICO: ERROR , NO SE PUEDE LLAMAR A LA FUNCION "+auxS2+" DENTRO DE "+nombreFuncion+"\n");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
				
			}
		//	System.out.println("hasta aca bien");
			aux=leerToken();
			escribirFichero(21);
			L();
			//System.out.println(tiposFuncion);
			//System.out.println("OJO "+comparator.getTipoDevuelto());
			//System.out.println("OJO2 "+comparator.getTiposLexemasFuncion());
			if(!comparator.compararFuncion(tiposFuncion)) {
				Error.writer.write("SEMANTICO: ERROR , PARAMETROS AL LLAMAR A "+auxS2+" INCORRECTOS\n");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				aux=leerToken();
			}
			return devolver;
		}
		else {
			Error.writer.write("SEMANTICO: ERROR , NO SE RECONOCE NINGUNA SENTENCIA S2 VALIDA");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}
	public Tipo X() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(22);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(22);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(22);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(22);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(22);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(22);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		//follow
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(23);
			return devolver;
		}
		else {
			Error.writer.write("SEMANTICO: ERROR , NO SE RECONOCE NINGUN RETURN VALIDO");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}

	}
	//cambiado tipo de retorno
	public Tipo F() {
		//tipo a devolver si se quiere
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		if (tokenIgual(TiposToken.T_FUNC)) {
			//estamos dentro de una funcion a partir de ahora
			dentroFuncion=true;
			escribirFichero(24);
			aux=leerToken();
			//tipo a devolver
			Tipo H=H();
			//tipo devuelto valido?
			if (!(H.getTipoToken().equals(TiposToken.T_BOOLEAN) || H.getTipoToken().equals(TiposToken.T_INT) || H.getTipoToken().equals(TiposToken.T_STRING) || H.getTipoToken().equals(TiposToken.T_VACIO))) {
				Error.writer.write("SEMANTICO: ERROR , NO SE RECONOCE "+H.getTipoToken()+" COMO TIPO VALIDO");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			
			if (tokenIgual(TiposToken.T_ID)) {
				nombreFuncion=aux.getLexema();
				//ver en global
				if (global.lexemaExiste(nombreFuncion)) {
					Error.writer.write("SEMANTICO: ERROR , FUNCION O ID "+nombreFuncion+ " YA DECLARADO EN ZONA GLOBAL");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
					//error porque ya esta definido de antes la funcion en zona global
				}
				global.meterLexema(nombreFuncion);
				global.meterTipo(TiposToken.T_FUNC);
				global.meterDesplazamiento(0);
				//hacemos la local, hacemos que la actual sea la local
				TablaSimbolos local=new TablaSimbolos(nombreFuncion);
				//ponemos en el local el tipo de retorno de la funcion
				actual=local;
				actual.setTipoDevuelto(H.getTipoToken());
				//System.out.println(actual.getIdTabla());
				aux=leerToken();
				if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
					aux=leerToken();
					A();
					
					//NO BORRAR
					//aqui es donde se meten los tipos de la func, en tabla local SIEMPRE
					//System.out.println(actual.getTiposLexemasFuncion());
					if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
						aux=leerToken();
						
						if (tokenIgual(TiposToken.T_LLAVEABRE)) {
							aux=leerToken();
							//aqui cambia el estado de denntroFuncion
							C();
							if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
								dentroFuncion=false;
								nombreFuncion=null;
								actual=global;
								aux=leerToken();
							}
							else {
								//aqui hay un bug pequeno
								Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN CIERRE DE LLAVE");
								devolver=new Tipo(TiposToken.T_ERROR);
								return devolver;
								//error no llave cierra
							}
						}
						else {
							Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ABRIR LLAVE");
							devolver=new Tipo(TiposToken.T_ERROR);
							return devolver;
							//error no abre llave
						}
					}else {
						Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN CIERRE DE PARENTESIS");
						devolver=new Tipo(TiposToken.T_ERROR);
						return devolver;
						//error no cierra parentesis
					}
				}
				else {
					Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ABRIR DE PARENTESIS");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
					//error no abre parentesis
				}
			}
			else {
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ID DE FUNCION");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
				//error no leemos id
			}

			return devolver;
		}
		else {
			//error porque no es funcion el token
			Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UNA FUNCION PERO NO SE RECIBIO NADA");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
			
		}
	}
	public Tipo A() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_INT)) {
			escribirFichero(25);
			Tipo T=T();
			if (tokenIgual(TiposToken.T_ID)) {
				String id=aux.getLexema();
				//actual.meterLexemaFuncion(id);
				//metemos lexema en tabla local
				actual.meterLexema(id);
				//y el tipo
				actual.meterTipo(T.getTipoToken());
				//metemos el tipo que queremos que el bicho compare luego uno a uno
				actual.meterTipoFuncion(T.getTipoToken());
				//actual.meterDesplazamientoFuncion(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				//desp de la tabla normal
				actual.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				aux=leerToken();
				Tipo K=K();
			}
			else {
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ID");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
				//error se esperaba un id
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_STRING)) {
			escribirFichero(25);
			Tipo T=T();
			if (tokenIgual(TiposToken.T_ID)) {
				String id=aux.getLexema();
				//actual.meterLexemaFuncion(id);
				actual.meterLexema(id);
				actual.meterTipo(T.getTipoToken());
				actual.meterTipoFuncion(T.getTipoToken());
				//actual.meterDesplazamientoFuncion(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				actual.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				aux=leerToken();
				Tipo K=K();
			}
			else {
				//error se esperaba un id
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ID");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_BOOLEAN)) {
			escribirFichero(25);
			Tipo T=T();
			if (tokenIgual(TiposToken.T_ID)) {
				String id=aux.getLexema();
				//actual.meterLexemaFuncion(id);
				actual.meterLexema(id);
				actual.meterTipo(T.getTipoToken());
				actual.meterTipoFuncion(T.getTipoToken());
				//actual.meterDesplazamientoFuncion(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				actual.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				aux=leerToken();
				Tipo K=K();
			}
			else {
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ID");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
				//error se esperaba un id
			}
			return devolver;
		}//follow
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			//TODO la lista sera null si no tiene parametros
			devolver=new Tipo(TiposToken.T_OK);
			escribirFichero(26);
			return devolver;
		}
		else {
			//error
			Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN TIPO");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}

	}
	public Tipo K() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_COMA)) {
			aux=leerToken();
			escribirFichero(27);
			Tipo T=T();

			if (tokenIgual(TiposToken.T_ID)) {
				String id=aux.getLexema();
				//actual.meterLexemaFuncion(id);
				actual.meterLexema(id);
				actual.meterTipo(T.getTipoToken());
				actual.meterTipoFuncion(T.getTipoToken());
				//actual.meterDesplazamientoFuncion(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				actual.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(T.getTipoToken()));
				aux=leerToken();
				Tipo K=K();
			}
			else {
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ID");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
				//error se esperaba un id
			}
			return devolver;
		}//follow
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(28);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		//error ninguno de los validos
		else {
			Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UNA COMA O UN CIERRE DE PARENTESIS");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}
	public Tipo H() {
		
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_INT)) {
			escribirFichero(29);
			Tipo T=T();
			TiposToken aux2=T.getTipoToken();
			devolver=new Tipo(aux2);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_STRING)) {
			escribirFichero(29);
			Tipo T=T();
			TiposToken aux2=T.getTipoToken();
			devolver=new Tipo(aux2);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_BOOLEAN)) {
			escribirFichero(29);
			Tipo T=T();
			TiposToken aux2=T.getTipoToken();
			devolver=new Tipo(aux2);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(30);
			return devolver;
		}
		else {
			Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN TIPO VALIDO");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}

	public Tipo L() {
		tiposFuncion.clear();
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(31);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(31);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(31);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(31);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(31);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(31);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		//follow
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(32);
			return devolver;
		}
		//error
		else {
			Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN TIPO VALIDO");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}

	public Tipo Q() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_COMA)) {
			aux=leerToken();
			escribirFichero(33);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		//follow
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(34);
			return devolver;
		}
		else {
			Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UNA COMA O UN FIN");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}

	}
	public Tipo E() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(35);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.T_OK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug

			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(35);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.T_OK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(35);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.T_OK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(35);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.T_OK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(35);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.T_OK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(35);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.T_OK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else {
			//error
			return devolver;
		}
	}
	public Tipo E2() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_AND)) {
			aux=leerToken();
			escribirFichero(36);
			Tipo Y=Y();
			Tipo E2=E2();
			if ((Y.getTipoToken().equals(TiposToken.T_BOOLEAN)) && (E2.getTipoToken().equals(TiposToken.T_OK) || E2.getTipoToken().equals(TiposToken.T_BOOLEAN))) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(37);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(37);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(37);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(37);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else {
			//error
			return devolver;
		}
	}
	public Tipo Y() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(38);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.T_INT)) && (Y2.getTipoToken().equals(TiposToken.T_BOOLEAN))) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.T_OK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(38);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.T_INT)) && (Y2.getTipoToken().equals(TiposToken.T_BOOLEAN))) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.T_OK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(38);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.T_INT)) && (Y2.getTipoToken().equals(TiposToken.T_BOOLEAN))) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.T_OK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(38);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.T_INT)) && (Y2.getTipoToken().equals(TiposToken.T_BOOLEAN))) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.T_OK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(38);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.T_INT)) && (Y2.getTipoToken().equals(TiposToken.T_BOOLEAN))) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.T_OK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(38);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.T_INT)) && (Y2.getTipoToken().equals(TiposToken.T_BOOLEAN))) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.T_OK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		//error
		else {
			return devolver;
		}
	}
	public Tipo Y2() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//leo un MENOR 
		if (tokenIgual(TiposToken.T_MENOR)) {
			aux=leerToken();
			escribirFichero(39);
			Tipo D=D();
			//Tipo Y2=Y2();
			if (D.getTipoToken().equals(TiposToken.T_INT)) {
				devolver=new Tipo(TiposToken.T_BOOLEAN);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_AND)) {
			escribirFichero(40);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(40);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;		
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(40);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;		
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(40);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;	
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(40);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;	
		}
		else {
			//error
			return devolver;
		}
	}
	public Tipo D() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//TODO ESTO ES V D2
		if (tokenIgual(TiposToken.T_ID)) {
			//se mete aqui para la funcion o =id existente
			escribirFichero(41);
			Tipo V=V();
			//System.out.println("AQUI TENEMOS "+V.getTipoToken());
			Tipo D2=D2();
			//System.out.println("DESPUES TENEMOS "+D2.getTipoToken());
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.T_OK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}

			else {
				//error
			}
			
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(41);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.T_OK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(41);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.T_OK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(41);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.T_OK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(41);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.T_OK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(41);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.T_OK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//ERROR COMO TAL DEVUELVE VACIO
			}
			return devolver;
		}

		else {
			//ERROR COMO TAL DEVUELVE VACIO
			return devolver;
		}

	}
	public Tipo D2() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//leemos un MAS
		if (tokenIgual(TiposToken.T_SUMA)) {
			aux=leerToken();
			escribirFichero(43);
			Tipo V=V();
			Tipo D2=D2();
			if ((V.getTipoToken().equals(TiposToken.T_INT))&&( D2.getTipoToken().equals(TiposToken.T_INT) || (D2.getTipoToken().equals(TiposToken.T_OK)))) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//ERROR COMO TAL DEVUELVE VACIO
			}
			return devolver;
		}
		//TODO FOLLOW MENOS EL ULTIMO QUE ES ERROR
		else if (tokenIgual(TiposToken.T_MENOR)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_AND)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else {
			//ERROR COMO TAL DEVUELVE VACIO
			return devolver;
		}
	}
	public Tipo V() {
		//aux que devolveremos luego
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token ID leido
		if (tokenIgual(TiposToken.T_ID)) {
			//System.out.println("LEEMOS UN ID QUE SERA LO QUE SEA");
			//TODO puede ser int --, puede ser func
			//Tenemos el string id para mas adelante en id y en auxV2
			auxV2=aux.getLexema();
			String id=aux.getLexema();

			if (!actual.lexemaExiste(id)) {
				//LEXEMA NO EXISTE EN TS LOCAL
				global.meterLexema(id);
				global.meterTipo(TiposToken.T_INT);
				global.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(TiposToken.T_INT));
			}
			//a partir de aqui el lexema existe, ya sea global o local
			aux=leerToken();
			escribirFichero(15);
			//Tenemos tipo de func en V2
			Tipo V2=V2();
			
			if (V2.getTipoToken().equals(TiposToken.T_INT) && actual.getTipoLexema(id).equals(TiposToken.T_INT)) {
				devolver=new Tipo(TiposToken.T_INT);
				//poner tipo de error que puede ser
			}
			
			//funcion con igual 
			else if (V2.getTipoToken().equals(TiposToken.T_INT)||V2.getTipoToken().equals(TiposToken.T_STRING)||V2.getTipoToken().equals(TiposToken.T_VACIO)||V2.getTipoToken().equals(TiposToken.T_BOOLEAN)) {
				//System.out.println("ESTAMOS EN ESTE CASO");
				devolver=new Tipo(V2.getTipoToken());
				return devolver;
			}
			//TODO TODO TODO OJO QUE AQUI PUEDE HABER CAGADA, esto hay que cambiar para que busque por
			//locales y luego globales, asi siempre
			else if (V2.getTipoToken().equals(TiposToken.T_OK)) {
				//devolver=new Tipo(TiposToken.T_OK);
				devolver=new Tipo(actual.getTipoLexema(id));
				//probnado este return BORRAR SI NO
				return devolver;
			}
			//esto va para el final del punto y coma
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				aux=leerToken();
			}
			else {
				//no termina en punto y coma y hay lio
			}
			auxV2=null;
			tiposFuncion=null;
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(46);
			Tipo E=E();

			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				devolver=new Tipo(E.getTipoToken());
				aux=leerToken();

			}
			return devolver; 
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			aux=leerToken();
			escribirFichero(47);
			devolver=new Tipo(TiposToken.T_INT);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			aux=leerToken();
			escribirFichero(48);
			devolver=new Tipo(TiposToken.T_STRING);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			aux=leerToken();
			escribirFichero(49);
			devolver=new Tipo(TiposToken.T_BOOLEAN);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			aux=leerToken();
			escribirFichero(50);
			devolver=new Tipo(TiposToken.T_BOOLEAN);
			return devolver;
		}
		else {
			return devolver;
		}
	}
	public Tipo V2() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//leemos un postdecremento y por tanto el tipo de v2 sera entero
		if (tokenIgual(TiposToken.T_POSTDECREMENTO)) {
			aux=leerToken();
			escribirFichero(51);
			devolver=new Tipo(TiposToken.T_INT);
			return devolver;
		}
		//TODO para las funciones, tener cuidado
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			//ES UNA FUNC y tenemos que mirar y meter los parametros con los que la invocamos
			//se viene L, que tiene dentro (E,E,E...etc)
			
			//tabla aux para coger el tipo de ret
			TablaSimbolos comparator=null;
			for (TablaSimbolos tabla : TablaSimbolos.getListaTablas()) {
				if (tabla.getNombreFuncion().equals(auxV2)){
					comparator=tabla;
				}
			}
			
			//TODO casos de si esta una func dentro de otra y tal
			//la func no existe y no se puede hacer nada con ella
			if (comparator==null) {
				Error.writer.write("SEMANTICO: ERROR , LA FUNCION NO EXISTE");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			//Ahora tenemos de arriba la tabla que tiene el tipo devuelto PORQUE EXISTE
			aux=leerToken();
			escribirFichero(52);
			//TODO si L distinto a tipo Ok, error
			Tipo L=L();
			//System.out.println("SE VIENE PAPA:"+tiposFuncion);
			//System.out.println(comparator.getTiposLexemasFuncion());
			//si dentro de func pero recursivo bien, else error
			//TODO comprobar tema de parametros
			if (dentroFuncion) {
				if (!nombreFuncion.equals(auxV2)) {
					Error.writer.write("SEMANTICO: ERROR , NO SE PUEDE LLAMAR A LA FUNCION "+auxV2+" DENTRO DE "+nombreFuncion+"\n");
					devolver=new Tipo(TiposToken.T_ERROR);
					return devolver;
				}
				
			}
			//System.out.println("OJO "+comparator.getTipoDevuelto());
			//System.out.println("OJO2 "+comparator.getTiposLexemasFuncion());
			//TODO tremendo bug aca
			if(!comparator.compararFuncion(tiposFuncion)) {
				Error.writer.write("SEMANTICO: ERROR , PARAMETROS AL LLAMAR A "+auxV2+" INCORRECTOS\n");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			
			
			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				//tipo de la funcion devuelto
				devolver.setTipoToken(comparator.getTipoDevuelto());
				aux=leerToken();
			}
			else {
				//error porque no hay fin parentesis
			}
			//por aqui sigue como si nada o esta bien
			return devolver;
		}
		//el follow da OK
		else if (tokenIgual(TiposToken.T_SUMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_MENOR)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_AND)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;

		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else {
			return devolver;
		}
	}
	public Tipo CASE() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_CASE)) {
			aux=leerToken();
			escribirFichero(54);

			Tipo E=E();
			if (!E.getTipoToken().equals(TiposToken.T_INT)) {
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN ID ENTERO O UNA CONSTANTE ENTERA");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
				aux=leerToken();
				CASE2();
				CASE();
			}
			else {
				Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN DOS PUNTOS");
				devolver=new Tipo(TiposToken.T_ERROR);
				return devolver;
			}
			return devolver;
		}
		//windols, que es windols
		else if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
			escribirFichero(55);
			return devolver;
		}
		else {
			Error.writer.write("SEMANTICO: ERROR , SE ESPERABA UN DOS PUNTOS O LLAVE CIERRA");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}
	public Tipo CASE2() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_BREAK)) {
			aux=leerToken();
			escribirFichero(57);
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				aux=leerToken();
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(56);
			S();
			CASE2();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			escribirFichero(56);
			S();
			CASE2();
			return devolver;

		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			escribirFichero(56);
			S();
			CASE2();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(56);
			S();
			CASE2();
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_CASE)) {
			escribirFichero(58);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
			escribirFichero(58);
			devolver=new Tipo(TiposToken.T_OK);
			return devolver;
		}
		else {
			Error.writer.write("SEMANTICO: ERROR , CASE INCORRECTO");
			devolver=new Tipo(TiposToken.T_ERROR);
			return devolver;
		}
	}

	public void escribirFichero(int numeroGramatica) {
		//System.out.println(numeroGramatica);
		writer.print(numeroGramatica+" ");
	}

	private boolean tokenIgual(TiposToken otro) {
		if (aux.getToken().equals(otro)){
			return true;
		}
		else return false;
	}

	//F estatica que lee de la lista de tokens segun la posicion que sea
	//se cambiO la lista por la que no tiene tokens para ver que pasa, va mejor, pero no cuenta lineas
	public Token leerToken() {
		if (posicion<listaTokensSinEol.size()) {
			contadorLineas();
			//System.out.println("ESTAMOS EN LINEA: "+linea);
			//System.out.println(listaTokensSinEol.get(posicion).tokenizar());
			return listaTokensSinEol.get(posicion++);

		}
		else {
			return null;
		}

	}
	
	private void contadorLineas() {
		if (listaTokens.get(contador).getToken().equals(TiposToken.EOL)) {
			linea=linea+1;
		}
		contador=contador+1;
	}

	private ArrayList<Token> quitarEOL() {

		ArrayList<Token> listaTokensAux=new ArrayList<Token>();
		for (Token token: listaTokens) {
			if (!token.getToken().equals(TiposToken.EOL)) {
				listaTokensAux.add(token);
			}
		}
		//for (Token token: listaTokensAux) {
		//	System.out.println(token.tokenizar());
		//}
		return listaTokensAux;
	}
}