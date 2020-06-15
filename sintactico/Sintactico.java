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
	//TODO ESTO PARA LUEGO
	private boolean breakEnSwitch=false;
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
	public Tipo P() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//B
		if (tokenIgual(TiposToken.TVAR)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TIF)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TSWITCH)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}
		//nuevo el de break
		else if (tokenIgual(TiposToken.TBREAK)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}
		//S
		else if (tokenIgual(TiposToken.TID)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TRETURN)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPRINT)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TINPUT)) {
			escribirFichero(1);
			Tipo B=B();
			if (B.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}//F
		else if (tokenIgual(TiposToken.TFUNC)) {
			escribirFichero(2);
			Tipo F=F();
			if (F.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			Tipo P=P();
			if (P.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			return devolver;
		}//eof
		else if (tokenIgual(TiposToken.EOF)) {
			escribirFichero(3);
			writer.close();
			return devolver;
		}
		//esto no hace nada pero no borrar porque me da miedo
		else if (tokenIgual(TiposToken.EOL)) {
			aux=leerToken();
			P();
			return devolver;
		}
		else {
			//error
			return devolver;
		}
	}

	public Tipo B() {
		//Tipo a devolver por el procedure B
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//LEO UN VAR: var ...
		if (tokenIgual(TiposToken.TVAR)) {
			aux=leerToken();
			escribirFichero(4);
			//obtengo tipo de variable
			Tipo T=T();
			//Tipo no compatible con los 3 soportados, int boolean o string
			if (T.getTipoToken().equals(TiposToken.TERROR)){
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			//continuo sin error
			//LEO VAR TIPO ID...
			if (tokenIgual(TiposToken.TID)) {
				//leo hasta: var ENTERO|CADENA|BOOLEAN ID
				if (actual.lexemaExisteLocal(aux.getLexema())) {
					//System.out.println("Var ya declarada anteriormente");
					//Si existe ya en la local, error
					Error.writer.write("SEMANTICO: ERROR, EN LINEA "+ linea +" VARIABLE "+aux.getLexema()+" YA DEFINIDA CON ANTERIORIDAD\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
				}
				//No existe en local, o ya esta en global y podemos hacerla local
				//esto es solo declaracion, por lo que no hay que usar nada
				String id=aux.getLexema();
				aux=leerToken();
				Tipo B2=B2();
				if (B2.getTipoToken().equals(TiposToken.TERROR)) {
					Error.writer.write("SEMANTICO: ERROR, EN LINEA "+ linea +" TIPO EN B2 NO COMPATIBLE\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
				}
				//var TIPO id
				if (B2.getTipoToken().equals(TiposToken.TVACIO)) {
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
					Error.writer.write("SEMANTICO: ERROR, EN LINEA "+ linea +" LOS TIPOS "+B2.getTipoToken()+" Y "+T.getTipoToken()+" NO SON COMPATIBLES\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
				}
				//fin de VAR TIPO ID ; O DE VAR TIPO ID = E ;
				if (tokenIgual(TiposToken.TPUNTOCOMA)) {
					//ALL IS OK; FIN
					devolver=new Tipo(TiposToken.TOK);
					aux=leerToken();
				}
				else {
					Error.writer.write("SINTACTICO: ERROR EN LINEA "+linea+" , FALTA PUNTO Y COMA\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
					//falta el punto y coma del final
				}
			}
			else {
				//lei var tipo pero luego no viene ID
				Error.writer.write("SINTACTICO: ERROR, EN LINEA "+ linea +" EL TIPO "+T.getTipoToken() +" NO VA SEGUIDO DE NINGUN ID\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			
			return devolver;
		}
		//INICIO IF
		else if (tokenIgual(TiposToken.TIF)) {
			aux=leerToken();
			escribirFichero(5);

			if (tokenIgual(TiposToken.TPARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();
				if (!E.getTipoToken().equals(TiposToken.TBOOLEAN)) {
					//error porque E no es un booleano
					Error.writer.write("SEMANTICO: ERROR, EN LINEA "+ linea +" , LA EXPRESION DEL IF NO ES DE TIPO BOOLEANO\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
				}
				else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
					devolver=new Tipo(TiposToken.TOK);
					aux=leerToken();
					//TODO mejorar lo devuelto quiza
					Tipo S=S();
				}
				else {
					Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN CIERRE DE PARENTESIS EN EL IF\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
					//error, no viene parentesis cierra
				}
			}
			else {
				//no abre parentesis despues del if, error
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ABRIR DE PARENTESIS EN EL IF\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			
			return devolver;
		}//FIN DE IF
		//INICIO SWITCH
		//TODO interesante el breakenswitch
		else if (tokenIgual(TiposToken.TSWITCH)) {
			breakEnSwitch=true;
			aux=leerToken();
			escribirFichero(7);

			if (tokenIgual(TiposToken.TPARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();
				if (!E.getTipoToken().equals(TiposToken.TINT)) {
					//no es entero y por tanto es error en el switch y fin
					Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , LA EXPRESION DEL SWITCH NO ES UN ENTERO\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
				}
				
				else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
					aux=leerToken();
					if (tokenIgual(TiposToken.TLLAVEABRE)) {
						aux=leerToken();
						CASE();	
						if (tokenIgual(TiposToken.TLLAVECIERRA)) {
							devolver=new Tipo(TiposToken.TOK);
							aux=leerToken();
						}
						else {
							//error sin llave cierra al final
							Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA CERRAR LLAVE EN EL SWITCH\n");
							devolver=new Tipo(TiposToken.TERROR);
							return devolver;
						}
					}
					else {
						Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA ABRIR LLAVE EN EL SWITCH\n");
						devolver=new Tipo(TiposToken.TERROR);
						return devolver;
						//error no abre llave al principio
					}
				}
				else {
					Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA CERRAR PARENTESIS EN EL SWITCH\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
					//error porque no hay parentesis cierra
				}
			}
			else {
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA ABRIR PARENTESIS EN EL SWITCH\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
				//no abre parentesis despues del switch
			}
			breakEnSwitch=false;
			return devolver;
		}//FIM SWITCH
		//S
		else if (tokenIgual(TiposToken.TID)) {
			escribirFichero(6);
			Tipo S=S();
			if (S.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TRETURN)) {
			escribirFichero(6);
			Tipo S=S();
			if (S.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPRINT)) {
			escribirFichero(6);
			Tipo S=S();
			if (S.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TINPUT)) {
			escribirFichero(6);
			Tipo S=S();
			if (S.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		//nuevo el de break
		else if (tokenIgual(TiposToken.TBREAK)) {
			escribirFichero(6);
			Tipo S=S();
			if (S.getTipoToken().equals(TiposToken.TERROR)) {devolver=new Tipo(TiposToken.TERROR); return devolver;}
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , NO ES ENCONTRO NINGUN TOKEN COMPATIBLE CON B\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}
	}
	public Tipo B2() {
		//aux que tengo que devolver
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		if (tokenIgual(TiposToken.TIGUAL)) {
			aux=leerToken();
			escribirFichero(8);
			Tipo E=E();
			// hemos leido "= EXPRESION"
			devolver=new Tipo(E.getTipoToken());
			return devolver;

		}
		//follow
		else if (tokenIgual(TiposToken.TPUNTOCOMA)) {
			escribirFichero(9);
			devolver=new Tipo(TiposToken.TVACIO);
			return devolver;
		}//error
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , NO SE ENCONTRO NINGUN TOKEN COMPATIBLE CON B2\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}
	}
	//devuelvo el tipo de T, que es o int o string o boolean
	public Tipo T() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		if (tokenIgual(TiposToken.TINT)) {
			escribirFichero(10);
			aux=leerToken();
			devolver=new Tipo(TiposToken.TINT);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TSTRING)) {
			escribirFichero(11);
			aux=leerToken();
			devolver=new Tipo(TiposToken.TSTRING);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TBOOLEAN)) {
			escribirFichero(12);
			aux=leerToken();
			devolver=new Tipo(TiposToken.TBOOLEAN);
			return devolver;
		}
		else {
			//ninguno de los tipos basicos bro
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , NO SE ENCONTRO UN TIPO VALIDO\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}
	}
	//TODO hacer cosas con el tipo del B para ver si hay error
	public void C() {
		if (tokenIgual(TiposToken.TVAR)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.TIF)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.TSWITCH)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		//S
		else if (tokenIgual(TiposToken.TID)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.TRETURN)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.TPRINT)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.TINPUT)) {
			escribirFichero(13);
			B();
			C();
			return;
		}
		else if (tokenIgual(TiposToken.TBREAK)) {
			escribirFichero(13);
			B();
			C();
			return;
		}//follow
		else if (tokenIgual(TiposToken.TLLAVECIERRA)) {
			//escribir en fichero y return
			escribirFichero(14);
			return;
		}
		else if (tokenIgual(TiposToken.TCASE)) {
			//escribir en fichero y return
			escribirFichero(14);
			return;
		}
		else {
			return;
		}
	}
	public Tipo S() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		if (tokenIgual(TiposToken.TID)) {
			
			auxS2=aux.getLexema();
		//	System.out.println("Estamos en "+auxS2);
			
			if (!actual.lexemaExiste(auxS2)) {
				//LEXEMA NO EXISTE EN TS
				global.meterLexema(auxS2);
				global.meterTipo(TiposToken.TINT);
				global.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(TiposToken.TINT));
			}
			//a partir de aqui el lexema existe, ya sea GLOBAL o LOCAL
			aux=leerToken();
			escribirFichero(15);
			Tipo S2=S2();
			//System.out.println("Al salir de S2 tenemos "+S2.getTipoToken());
			//Al llamar a s2 no reconoce cuando es func
			//System.out.println("OJO A ESTO "+actual.getTipoLexema(id));
			if (S2.getTipoToken().equals(TiposToken.TINT) && actual.getTipoLexema(auxS2).equals(TiposToken.TINT)) {
				devolver=new Tipo(TiposToken.TINT);
			}
			
			//cambiado esto revisar bugs
			else if (actual.getTipoLexema(auxS2).equals(TiposToken.TINT) && S2.getTipoToken().equals(TiposToken.TVACIO)) {
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , LA FUNCION LLAMADA NO EXISTE\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			else if (S2.getTipoToken().equals(TiposToken.TVACIO)) {
				devolver=new Tipo(TiposToken.TOK);
			}
			else {
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			//TODO poner aqui un caso de error?
			//rollo else{}
			//esto va para el final del punto y coma
			if (tokenIgual(TiposToken.TPUNTOCOMA)) {
				aux=leerToken();
			}
			else {
				//TODO esto da error, se comenta y fuera
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			auxS2=null;
			return devolver;
		}
		else if (tokenIgual(TiposToken.TRETURN)) {
			
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
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , USADO RETURN SIN ESTAR DENTRO DE UNA FUNCION\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			if (!X.getTipoToken().equals(auxaux.getTipoDevuelto())) {
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , TIPO DE FUNCION Y TIPO DEVUELTO SON DISTINTOS\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			if (tokenIgual(TiposToken.TPUNTOCOMA)) {
				devolver=new Tipo(X.getTipoToken());
				aux=leerToken();
			}
			else {
				//error falta el punto y coma del final
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA EL PUNTO Y COMA FINAL\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;	
			}
			//salimos de la funcion
			
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPRINT)) {
			aux=leerToken();
			escribirFichero(17);

			if (tokenIgual(TiposToken.TPARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();

				if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
					aux=leerToken();
					if (tokenIgual(TiposToken.TPUNTOCOMA)) {
						aux=leerToken();
					}
					else {
						//error falta el punto y coma del final
						Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA EL PUNTO Y COMA\n");
						devolver=new Tipo(TiposToken.TERROR);
						return devolver;	
					}
				}
				else {
					//error falta el punto y coma del final
					Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA EL CERRAR PARENTESIS\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;	
				}
			}
			else {
				//error falta el punto y coma del final
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA EL ABRIR PARENTESIS\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;	
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TINPUT)) {
			escribirFichero(18);
			aux=leerToken();
			if (tokenIgual(TiposToken.TPARENTESISABRE)) {
				aux=leerToken();
				if (tokenIgual(TiposToken.TID)) {
					//ya tenemos el id
					String id=aux.getLexema();
					//si no existe, es entera

					if (!actual.lexemaExiste(id)) {
						global.meterLexema(id);
						global.meterTipo(TiposToken.TINT);
						global.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(TiposToken.TINT));
					}
					if (actual.getTipoLexema(id).equals(TiposToken.TBOOLEAN)) {
						Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , NO SE PUEDE METER UN BOOLEANO EN EL INPUT\n");
						devolver=new Tipo(TiposToken.TERROR);
						return devolver;	
					}
					aux=leerToken();
					if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
						aux=leerToken();
						if (tokenIgual(TiposToken.TPUNTOCOMA)) {
							aux=leerToken();
						}
						else {
							//error no hay punto y coma
							Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA EL PUNTO Y COMA DEL FINAL\n");
							devolver=new Tipo(TiposToken.TERROR);
							return devolver;	
						}
					}
					else {
						//erro no hay parentesis cierra
						Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN CIERRA PARENTESIS\n");
						devolver=new Tipo(TiposToken.TERROR);
						return devolver;
					}
				}
				else {
					//error no hay id
					Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , FALTA EL ID DEL INPUT\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
				}
			}
			else {
				//error no abre parentesis
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , FALTA EL PARENTESIS ABRE DEL INPUT\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			return devolver;
		}
		//el token
		else if (tokenIgual(TiposToken.TBREAK)) {
			escribirFichero(19);
			
			//System.out.println(aux.tokenizar());
			if (!breakEnSwitch) {
			//	System.out.println("devuelve un error el break");
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , NO SE PUEDE PONER UN BREAK AQUI\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			aux=leerToken();
			if (tokenIgual(TiposToken.TPUNTOCOMA)) {
				aux=leerToken();
			}
			else {
				
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN PUNTO Y COMA\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			//esperamos el punto y coma
			return devolver;
		}
		else {
			//error no es ninguno de estos casos
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , NO SE RECONOCE NINGUNA SENTENCIA VALIDA EN S\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
			
		}
	}
	public Tipo S2() {
		//System.out.println("Entrando en S2");
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TPOSTDECREMENTO)) {
			aux=leerToken();
			escribirFichero(20);
			devolver=new Tipo(TiposToken.TINT);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TIGUAL)) {
			aux=leerToken();
			escribirFichero(21);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		//TODO error cuando lee id del anterior y lo toma como entero y luego llama a esto. comprobar si es func o no
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
			//anyadido nuevo
			TablaSimbolos comparator=null;
			for (TablaSimbolos tabla : TablaSimbolos.getListaTablas()) {
				if (tabla.getNombreFuncion().equals(auxS2)){
					comparator=tabla;
				}
			}
			if (comparator==null) {
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , LA FUNCION " +auxS2 +" NO EXISTE\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			if (dentroFuncion) {
				if (!nombreFuncion.equals(auxS2)) {
					Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , NO SE PUEDE LLAMAR A LA FUNCION "+auxS2+" DENTRO DE "+nombreFuncion+"\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
				}
				
			}
		//	System.out.println("hasta aca bien");
			aux=leerToken();
			escribirFichero(22);
			L();
			//System.out.println(tiposFuncion);
			//System.out.println("OJO "+comparator.getTipoDevuelto());
			//System.out.println("OJO2 "+comparator.getTiposLexemasFuncion());
			if(!comparator.compararFuncion(tiposFuncion)) {
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , PARAMETROS AL LLAMAR A "+auxS2+" INCORRECTOS\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
				aux=leerToken();
			}
			return devolver;
		}
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , NO SE RECONOCE NINGUNA SENTENCIA S2 VALIDA\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}
	}
	public Tipo X() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		if (tokenIgual(TiposToken.TID)) {
			escribirFichero(23);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
			escribirFichero(23);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.TENTERO)) {
			escribirFichero(23);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCADENA)) {
			escribirFichero(23);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.TTRUE)) {
			escribirFichero(23);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		else if (tokenIgual(TiposToken.TFALSE)) {
			escribirFichero(23);
			Tipo E=E();
			devolver=new Tipo(E.getTipoToken());
			return devolver;
		}
		//follow
		else if (tokenIgual(TiposToken.TPUNTOCOMA)) {
			escribirFichero(24);
			return devolver;
		}
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , NO SE RECONOCE NINGUN RETURN VALIDO\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}

	}
	//cambiado tipo de retorno
	public Tipo F() {
		//tipo a devolver si se quiere
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		if (tokenIgual(TiposToken.TFUNC)) {
			//estamos dentro de una funcion a partir de ahora
			dentroFuncion=true;
			escribirFichero(25);
			aux=leerToken();
			//tipo a devolver
			Tipo H=H();
			//tipo devuelto valido?
			if (!(H.getTipoToken().equals(TiposToken.TBOOLEAN) || H.getTipoToken().equals(TiposToken.TINT) || H.getTipoToken().equals(TiposToken.TSTRING) || H.getTipoToken().equals(TiposToken.TVACIO))) {
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , NO SE RECONOCE "+H.getTipoToken()+" COMO TIPO VALIDO\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			
			if (tokenIgual(TiposToken.TID)) {
				nombreFuncion=aux.getLexema();
				//ver en global
				if (global.lexemaExiste(nombreFuncion)) {
					Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , FUNCION O ID "+nombreFuncion+ " YA DECLARADO EN ZONA GLOBAL\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
					//error porque ya esta definido de antes la funcion en zona global
				}
				global.meterLexema(nombreFuncion);
				global.meterTipo(TiposToken.TFUNC);
				global.meterDesplazamiento(0);
				//hacemos la local, hacemos que la actual sea la local
				TablaSimbolos local=new TablaSimbolos(nombreFuncion);
				//ponemos en el local el tipo de retorno de la funcion
				actual=local;
				actual.setTipoDevuelto(H.getTipoToken());
				//System.out.println(actual.getIdTabla());
				aux=leerToken();
				if (tokenIgual(TiposToken.TPARENTESISABRE)) {
					aux=leerToken();
					A();
					
					//NO BORRAR
					//aqui es donde se meten los tipos de la func, en tabla local SIEMPRE
					//System.out.println(actual.getTiposLexemasFuncion());
					if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
						aux=leerToken();
						
						if (tokenIgual(TiposToken.TLLAVEABRE)) {
							aux=leerToken();
							//aqui cambia el estado de denntroFuncion
							C();
							if (tokenIgual(TiposToken.TLLAVECIERRA)) {
								dentroFuncion=false;
								nombreFuncion=null;
								actual=global;
								aux=leerToken();
							}
							else {
								//aqui hay un bug pequeno
								Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN CIERRE DE LLAVE\n");
								devolver=new Tipo(TiposToken.TERROR);
								return devolver;
								//error no llave cierra
							}
						}
						else {
							Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ABRIR LLAVE\n");
							devolver=new Tipo(TiposToken.TERROR);
							return devolver;
							//error no abre llave
						}
					}else {
						Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN CIERRE DE PARENTESIS\n");
						devolver=new Tipo(TiposToken.TERROR);
						return devolver;
						//error no cierra parentesis
					}
				}
				else {
					Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ABRIR DE PARENTESIS\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
					//error no abre parentesis
				}
			}
			else {
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ID DE FUNCION\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
				//error no leemos id
			}

			return devolver;
		}
		else {
			//error porque no es funcion el token
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UNA FUNCION PERO NO SE RECIBIO NADA\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
			
		}
	}
	public Tipo A() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TINT)) {
			escribirFichero(26);
			Tipo T=T();
			if (tokenIgual(TiposToken.TID)) {
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
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ID\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
				//error se esperaba un id
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TSTRING)) {
			escribirFichero(26);
			Tipo T=T();
			if (tokenIgual(TiposToken.TID)) {
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
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ID\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TBOOLEAN)) {
			escribirFichero(26);
			Tipo T=T();
			if (tokenIgual(TiposToken.TID)) {
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
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ID\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
				//error se esperaba un id
			}
			return devolver;
		}//follow
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			//TODO la lista sera null si no tiene parametros
			devolver=new Tipo(TiposToken.TOK);
			escribirFichero(27);
			return devolver;
		}
		else {
			//error
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN TIPO\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}

	}
	public Tipo K() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TCOMA)) {
			aux=leerToken();
			escribirFichero(28);
			Tipo T=T();

			if (tokenIgual(TiposToken.TID)) {
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
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ID\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
				//error se esperaba un id
			}
			return devolver;
		}//follow
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			escribirFichero(29);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		//error ninguno de los validos
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UNA COMA O UN CIERRE DE PARENTESIS\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}
	}
	public Tipo H() {
		
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TINT)) {
			escribirFichero(30);
			Tipo T=T();
			TiposToken aux2=T.getTipoToken();
			devolver=new Tipo(aux2);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TSTRING)) {
			escribirFichero(30);
			Tipo T=T();
			TiposToken aux2=T.getTipoToken();
			devolver=new Tipo(aux2);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TBOOLEAN)) {
			escribirFichero(30);
			Tipo T=T();
			TiposToken aux2=T.getTipoToken();
			devolver=new Tipo(aux2);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TID)) {
			escribirFichero(31);
			return devolver;
		}
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN TIPO VALIDO\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}
	}

	public Tipo L() {
		tiposFuncion.clear();
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TID)) {
			escribirFichero(32);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
			escribirFichero(32);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.TENTERO)) {
			escribirFichero(32);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCADENA)) {
			escribirFichero(32);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.TTRUE)) {
			escribirFichero(32);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		else if (tokenIgual(TiposToken.TFALSE)) {
			escribirFichero(32);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		//follow
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			escribirFichero(33);
			return devolver;
		}
		//error
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN TIPO VALIDO\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}
	}

	public Tipo Q() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TCOMA)) {
			aux=leerToken();
			escribirFichero(34);
			Tipo E=E();
			tiposFuncion.add(E.getTipoToken());
			Tipo Q=Q();
			return devolver;
		}
		//follow
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			escribirFichero(35);
			return devolver;
		}
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UNA COMA O UN FIN\n");
			devolver=new Tipo(TiposToken.TERROR);
			return devolver;
		}

	}
	public Tipo E() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TID)) {
			escribirFichero(36);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.TOK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug

			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
			escribirFichero(36);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.TOK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.TENTERO)) {
			escribirFichero(36);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.TOK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCADENA)) {
			escribirFichero(36);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.TOK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.TTRUE)) {
			escribirFichero(36);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.TOK))) {
				TiposToken aux2=Y.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			//TODO debug
			return devolver;
		}
		else if (tokenIgual(TiposToken.TFALSE)) {
			escribirFichero(36);
			Tipo Y=Y();
			Tipo E2=E2();
			if (Y.getTipoToken().equals(E2.getTipoToken()) || (E2.getTipoToken().equals(TiposToken.TOK))) {
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
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TAND)) {
			aux=leerToken();
			escribirFichero(37);
			Tipo Y=Y();
			Tipo E2=E2();
			if ((Y.getTipoToken().equals(TiposToken.TBOOLEAN)) && (E2.getTipoToken().equals(TiposToken.TOK) || E2.getTipoToken().equals(TiposToken.TBOOLEAN))) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCOMA)) {
			escribirFichero(38);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			escribirFichero(38);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TDOSPUNTOS)) {
			escribirFichero(38);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPUNTOCOMA)) {
			escribirFichero(38);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else {
			//error
			return devolver;
		}
	}
	public Tipo Y() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TID)) {
			escribirFichero(39);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.TINT)) && (Y2.getTipoToken().equals(TiposToken.TBOOLEAN))) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.TOK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
			escribirFichero(39);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.TINT)) && (Y2.getTipoToken().equals(TiposToken.TBOOLEAN))) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.TOK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TENTERO)) {
			escribirFichero(39);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.TINT)) && (Y2.getTipoToken().equals(TiposToken.TBOOLEAN))) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.TOK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCADENA)) {
			escribirFichero(39);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.TINT)) && (Y2.getTipoToken().equals(TiposToken.TBOOLEAN))) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.TOK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TTRUE)) {
			escribirFichero(39);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.TINT)) && (Y2.getTipoToken().equals(TiposToken.TBOOLEAN))) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.TOK)) {
				TiposToken aux2=D.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TFALSE)) {
			escribirFichero(39);
			Tipo D=D();
			Tipo Y2=Y2();
			if ((D.getTipoToken().equals(TiposToken.TINT)) && (Y2.getTipoToken().equals(TiposToken.TBOOLEAN))) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else if (Y2.getTipoToken().equals(TiposToken.TOK)) {
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
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//leo un MENOR 
		if (tokenIgual(TiposToken.TMENOR)) {
			aux=leerToken();
			escribirFichero(40);
			Tipo D=D();
			if (D.getTipoToken().equals(TiposToken.TINT)) {
				devolver=new Tipo(TiposToken.TBOOLEAN);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TAND)) {
			escribirFichero(41);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCOMA)) {
			escribirFichero(41);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;		
		}
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			escribirFichero(41);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;		
		}
		else if (tokenIgual(TiposToken.TDOSPUNTOS)) {
			escribirFichero(41);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;	
		}
		else if (tokenIgual(TiposToken.TPUNTOCOMA)) {
			escribirFichero(41);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;	
		}
		else {
			//error
			return devolver;
		}
	}
	public Tipo D() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//TODO ESTO ES V D2
		if (tokenIgual(TiposToken.TID)) {
			//se mete aqui para la funcion o =id existente
			escribirFichero(42);
			Tipo V=V();
			//System.out.println("AQUI TENEMOS "+V.getTipoToken());
			Tipo D2=D2();
			//System.out.println("DESPUES TENEMOS "+D2.getTipoToken());
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.TOK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}

			else {
				//error
			}
			
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
			escribirFichero(42);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.TOK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TENTERO)) {
			escribirFichero(42);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.TOK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCADENA)) {
			escribirFichero(42);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.TOK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TTRUE)) {
			escribirFichero(42);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.TOK) ) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.TFALSE)) {
			escribirFichero(42);
			Tipo V=V();
			Tipo D2=D2();
			if ( V.getTipoToken().equals(D2.getTipoToken()) || D2.getTipoToken().equals(TiposToken.TOK) ) {
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
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//leemos un MAS
		if (tokenIgual(TiposToken.TSUMA)) {
			aux=leerToken();
			escribirFichero(43);
			Tipo V=V();
			Tipo D2=D2();
			if ((V.getTipoToken().equals(TiposToken.TINT))&&( D2.getTipoToken().equals(TiposToken.TINT) || (D2.getTipoToken().equals(TiposToken.TOK)))) {
				TiposToken aux2=V.getTipoToken();
				devolver=new Tipo(aux2);
			}
			else {
				//ERROR COMO TAL DEVUELVE VACIO
			}
			return devolver;
		}
		//TODO FOLLOW MENOS EL ULTIMO QUE ES ERROR
		else if (tokenIgual(TiposToken.TMENOR)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TAND)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCOMA)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TDOSPUNTOS)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPUNTOCOMA)) {
			escribirFichero(44);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else {
			//ERROR COMO TAL DEVUELVE VACIO
			return devolver;
		}
	}
	public Tipo V() {
		//aux que devolveremos luego
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token ID leido
		if (tokenIgual(TiposToken.TID)) {
			//System.out.println("LEEMOS UN ID QUE SERA LO QUE SEA");
			//TODO puede ser int --, puede ser func
			//Tenemos el string id para mas adelante en id y en auxV2
			auxV2=aux.getLexema();
			String id=aux.getLexema();

			if (!actual.lexemaExiste(id)) {
				//LEXEMA NO EXISTE EN TS LOCAL
				global.meterLexema(id);
				global.meterTipo(TiposToken.TINT);
				global.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(TiposToken.TINT));
			}
			//a partir de aqui el lexema existe, ya sea global o local
			aux=leerToken();
			escribirFichero(45);
			//Tenemos tipo de func en V2
			Tipo V2=V2();
			
			if (V2.getTipoToken().equals(TiposToken.TINT) && actual.getTipoLexema(id).equals(TiposToken.TINT)) {
				devolver=new Tipo(TiposToken.TINT);
				//poner tipo de error que puede ser
			}
			
			//funcion con igual 
			else if (V2.getTipoToken().equals(TiposToken.TINT)||V2.getTipoToken().equals(TiposToken.TSTRING)||V2.getTipoToken().equals(TiposToken.TVACIO)||V2.getTipoToken().equals(TiposToken.TBOOLEAN)) {
				//System.out.println("ESTAMOS EN ESTE CASO");
				devolver=new Tipo(V2.getTipoToken());
				return devolver;
			}
			//TODO TODO TODO OJO QUE AQUI PUEDE HABER CAGADA, esto hay que cambiar para que busque por
			//locales y luego globales, asi siempre
			else if (V2.getTipoToken().equals(TiposToken.TOK)) {
<<<<<<< HEAD
				//devolver=new Tipo(TiposToken.T_OK);
=======
				//devolver=new Tipo(TiposToken.TOK);
>>>>>>> sin_barras
				devolver=new Tipo(actual.getTipoLexema(id));
				//probnado este return BORRAR SI NO
				return devolver;
			}
			//esto va para el final del punto y coma
			if (tokenIgual(TiposToken.TPUNTOCOMA)) {
				aux=leerToken();
			}
			else {
				//no termina en punto y coma y hay lio
			}
			auxV2=null;
			tiposFuncion=null;
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(46);
			Tipo E=E();

			if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
				devolver=new Tipo(E.getTipoToken());
				aux=leerToken();

			}
			return devolver; 
		}
		else if (tokenIgual(TiposToken.TENTERO)) {
			aux=leerToken();
			escribirFichero(47);
			devolver=new Tipo(TiposToken.TINT);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TCADENA)) {
			aux=leerToken();
			escribirFichero(48);
			devolver=new Tipo(TiposToken.TSTRING);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TTRUE)) {
			aux=leerToken();
			escribirFichero(49);
			devolver=new Tipo(TiposToken.TBOOLEAN);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TFALSE)) {
			aux=leerToken();
			escribirFichero(50);
			devolver=new Tipo(TiposToken.TBOOLEAN);
			return devolver;
		}
		else {
			return devolver;
		}
	}
	public Tipo V2() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//leemos un postdecremento y por tanto el tipo de v2 sera entero
		if (tokenIgual(TiposToken.TPOSTDECREMENTO)) {
			aux=leerToken();
			escribirFichero(51);
			devolver=new Tipo(TiposToken.TINT);
			return devolver;
		}
		//TODO para las funciones, tener cuidado
		else if (tokenIgual(TiposToken.TPARENTESISABRE)) {
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
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , LA FUNCION NO EXISTE\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			//Ahora tenemos de arriba la tabla que tiene el tipo devuelto PORQUE EXISTE
			aux=leerToken();
			escribirFichero(52);
			//TODO si L distinto a tipo Ok, error
			Tipo L=L();
			//TODO comprobar tema de parametros
			if (dentroFuncion) {
				if (!nombreFuncion.equals(auxV2)) {
					//comprobar antes si es un tipo y no una funcion
					if (!actual.lexemaExiste(auxV2)) {
					Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , NO SE PUEDE LLAMAR A LA FUNCION "+auxV2+" DENTRO DE "+nombreFuncion+"\n");
					devolver=new Tipo(TiposToken.TERROR);
					return devolver;
					}
				}
				
			}
			//System.out.println("OJO "+comparator.getTipoDevuelto());
			//System.out.println("OJO2 "+comparator.getTiposLexemasFuncion());
			//TODO tremendo bug aca
			if(!comparator.compararFuncion(tiposFuncion)) {
				Error.writer.write("SEMANTICO: ERROR EN LINEA "+ linea +" , PARAMETROS AL LLAMAR A "+auxV2+" INCORRECTOS\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			
			
			if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
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
		else if (tokenIgual(TiposToken.TSUMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TMENOR)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TAND)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;

		}
		else if (tokenIgual(TiposToken.TCOMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPARENTESISCIERRA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TDOSPUNTOS)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else if (tokenIgual(TiposToken.TPUNTOCOMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.TOK);
			return devolver;
		}
		else {
			return devolver;
		}
	}
	public Tipo CASE() {
		Tipo devolver=new Tipo(TiposToken.TVACIO);
		//Token 
		if (tokenIgual(TiposToken.TCASE)) {
			aux=leerToken();
			escribirFichero(54);

			Tipo E=E();
			if (!E.getTipoToken().equals(TiposToken.TINT)) {
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN ID ENTERO O UNA CONSTANTE ENTERA\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			if (tokenIgual(TiposToken.TDOSPUNTOS)) {
				aux=leerToken();
				C();
				CASE();
			}
			
			else {
				Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN DOS PUNTOS\n");
				devolver=new Tipo(TiposToken.TERROR);
				return devolver;
			}
			return devolver;
		}
		//windols, que es windols
		else if (tokenIgual(TiposToken.TLLAVECIERRA)) {
			escribirFichero(55);
			return devolver;
		}
		
		else {
			Error.writer.write("SINTACTICO: ERROR EN LINEA "+ linea +" , SE ESPERABA UN DOS PUNTOS O LLAVE CIERRA\n");
			devolver=new Tipo(TiposToken.TERROR);
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
		if (posicion<listaTokens.size()) {
			//contadorLineas();
			if (listaTokens.get(posicion).getToken().equals(TiposToken.EOL)) {
				for (int i=posicion;i<listaTokens.size();i++) {
					if (listaTokens.get(posicion).getToken().equals(TiposToken.EOL)) {
						linea++;
						posicion++;
					}
					else break;
				}
				//leerToken();
			}
		
			//System.out.println("ESTAMOS EN LINEA: "+linea+" LEYENDO "+listaTokensSinEol.get(posicion).tokenizar());
			//ESTE ES BUENO PARA DEBUG
			//System.out.println(listaTokens.get(posicion).tokenizar());
			return listaTokens.get(posicion++);
			
		}
		else {
			return null;
		}

	}
	
	private void contadorLineas() {
		//System.out.println("ESTAMOS EN LINEA: "+linea+" LEYENDO "+listaTokens.get(contador).getToken());
		if (listaTokens.get(contador).getToken().equals(TiposToken.EOL)) {
			linea++;
			
		}
		contador++;
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