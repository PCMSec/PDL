package sintactico;

import token.Token;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import tabla.Tipo;
import tabla.TablaSimbolos;
import token.TiposToken;


public class Sintactico {
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
		this.linea=1;
		//empezamos en la primera posicion de los tokens
		this.posicion=0;
		//inicializar normal
		this.listaTokens = listaTokens;
		//iniciar la que no tiene end of line
		this.listaTokensSinEol=quitarEOL();
		try {
			//se intenta escribir, si no se puede dará error en el catch
			writer = new PrintWriter(filename, "UTF-8");
			writer.print("D ");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//leo el primer token a la entrada para iniciar P
		aux=leerToken();
		//Tabla global y actual apuntando a lo mismo, cambiará más adelante para reflejar las TS de funciones
		global = new TablaSimbolos("");
		actual = global;
		global.setNombreFuncion("GLOBAL");
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
			B();
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_IF)) {
			escribirFichero(1);
			B();
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_SWITCH)) {
			escribirFichero(1);
			B();
			P();
			return;
		}
		//S
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(1);
			B();
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			escribirFichero(1);
			B();
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			escribirFichero(1);
			B();
			P();
			return;
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(1);
			B();
			P();
			return;
		}//F
		else if (tokenIgual(TiposToken.T_FUNC)) {
			escribirFichero(2);
			F();
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
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//leo var
		if (tokenIgual(TiposToken.T_VAR)) {
			aux=leerToken();
			escribirFichero(4);
			//obtengo tipo de var
			Tipo T=T();
			if (!(T.getTipoToken().equals(TiposToken.T_INT)) && !(T.getTipoToken().equals(TiposToken.T_STRING)) && !(T.getTipoToken().equals(TiposToken.T_BOOLEAN))){
				//error porque el tipo no es compatible con una variable
			}
			//continuo sin error
			if (tokenIgual(TiposToken.T_ID)) {
				//leo hasta: var ENTERO|CADENA|BOOLEAN ID
				if (actual.lexemaExiste(aux.getLexema())) {
					//ERROR si ya existe en la TS ACTUAL ya esta declarada de antes
					
				}
				//NO EXISTE, seguimos y obtenemos el string con nombre de ID
				String id=aux.getLexema();
				aux=leerToken();
				Tipo B2=B2();
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
					//error, ninguno de los dos
				}
				//fin de VAR TIPO ID ; O DE VAR TIPO ID = E ;
				if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
					devolver=new Tipo(TiposToken.T_OK);
					aux=leerToken();
				}
				else {
					//falta el punto y coma del final
				}
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_IF)) {
			aux=leerToken();
			escribirFichero(5);

			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();
				if (!E.getTipoToken().equals(TiposToken.T_BOOLEAN)) {
					//error porque E no es un booleano
				}
				else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
					devolver=new Tipo(TiposToken.T_OK);
					aux=leerToken();
					S();
				}
				else {
					//error
				}
			}
			else {
				//no abre parentesis despues del if
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_SWITCH)) {
			aux=leerToken();
			escribirFichero(7);

			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				Tipo E=E();
				if (!E.getTipoToken().equals(TiposToken.T_INT)) {
					//no es entero y por tanto es error
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
						}
					}
					else {
						//error no abre llave al principio
					}
				}
				else {
					//error
				}
			}
			else {
				//no abre parentesis despues del switch
			}
			return devolver;
		}
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
			//error
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
			return devolver;
		}
	}
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
			//Tenemos el string id para mas adelante
			String id=aux.getLexema();
			if (!actual.lexemaExiste(id)) {
				//LEXEMA NO EXISTE EN TS
				actual.meterLexema(id);
				actual.meterTipo(TiposToken.T_INT);
				actual.meterDesplazamiento(TablaSimbolos.getDesplazamientoTipo(TiposToken.T_INT));
			}
			//a partir de aqui el lexema existe, ya sea global o local
			aux=leerToken();
			escribirFichero(15);
			Tipo S2=S2();
			if (S2.getTipoToken().equals(TiposToken.T_INT) && actual.getTipoLexema(id).equals(TiposToken.T_INT)) {
				devolver=new Tipo(TiposToken.T_INT);
			}
			else if (S2.getTipoToken().equals(TiposToken.T_VACIO)) {
				devolver=new Tipo(TiposToken.T_OK);
			}
			else if (actual.getTipoLexema(id).equals(TiposToken.T_FUNC) && S2.getTipoToken().equals(TiposToken.T_OK)) {
				//devolver es igual al tipo devuelto por la funcion
			}
			//esto va para el final del punto y coma
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				aux=leerToken();
			}
			else {
				//no termina en punto y coma y hay lio
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			if (!dentroFuncion) {
				//no estamo dentro de func por lo que dara un error
			}
			aux=leerToken();
			escribirFichero(16);
			Tipo X=X();
			//TODO revisar esto que tiene mala pinta
			if (!X.getTipoToken().equals(global.getTipoLexema(nombreFuncion))) {
				//error tipos diferentes el devuelto y el dicho al principio
			}
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				devolver=new Tipo(X.getTipoToken());
				aux=leerToken();
			}
			//salimos de la funcion
			dentroFuncion=false;
			
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			aux=leerToken();
			escribirFichero(17);

			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				E();

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
					aux=leerToken();
					if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
						aux=leerToken();
						if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
							aux=leerToken();
						}
					}
				}
			}
			return devolver;
		}
		else {
			return devolver;
		}

	}
	public Tipo S2() {
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
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(21);
			L();	
			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				aux=leerToken();
			}
			//TODO no se hacer esto
			return devolver;
		}
		else {
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
			return devolver;
		}

	}
	public void F() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		if (tokenIgual(TiposToken.T_FUNC)) {
			escribirFichero(24);
			aux=leerToken();
			H();
			if (tokenIgual(TiposToken.T_ID)) {
				aux=leerToken();
				if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
					aux=leerToken();
					A();

					if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
						aux=leerToken();
						if (tokenIgual(TiposToken.T_LLAVEABRE)) {
							aux=leerToken();
							C();
							if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
								aux=leerToken();
							}
						}
					}
				}
			}
			return;
		}
		else {
			return;
		}
	}
	public void A() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_INT)) {
			escribirFichero(25);
			T();
			if (tokenIgual(TiposToken.T_ID)) {
				aux=leerToken();
				K();
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_STRING)) {
			escribirFichero(25);
			T();
			if (tokenIgual(TiposToken.T_ID)) {
				aux=leerToken();
				K();
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_BOOLEAN)) {
			escribirFichero(25);
			T();
			if (tokenIgual(TiposToken.T_ID)) {
				aux=leerToken();
				K();
			}
			return;
		}//follow
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(26);
			return;
		}
		else {
			return;
		}

	}
	public void K() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_COMA)) {
			aux=leerToken();
			escribirFichero(27);
			T();

			if (tokenIgual(TiposToken.T_ID)) {
				aux=leerToken();
				K();
			}
			return;
		}//follow
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(28);
			return;
		}
		//error
		else {
			return;
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
			//error
			return devolver;
		}
	}

	public void L() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(31);
			E();
			Q();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(31);
			E();
			Q();
			return;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(31);
			E();
			Q();
			return;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(31);
			E();
			Q();
			return;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(31);
			E();
			Q();
			return;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(31);
			E();
			Q();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(32);
			return;
		}
		//error
		else {
			return;
		}
	}
	public void Q() {
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_COMA)) {
			aux=leerToken();
			escribirFichero(33);
			E();
			Q();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(34);
			return;
		}
		else {
			return;
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
		//leemos un MENOS
		if (tokenIgual(TiposToken.T_MENOS)) {
			aux=leerToken();
			escribirFichero(42);
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
		//leemos un MAS
		else if (tokenIgual(TiposToken.T_SUMA)) {
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
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token
		if (tokenIgual(TiposToken.T_ID)) {
			if (!actual.lexemaExiste(aux.getLexema())) {
				//error, el lexema no existe
			}
			TiposToken tipoLexema=actual.getTipoLexema(aux.getLexema());
			aux=leerToken();
			escribirFichero(45);
			Tipo V2=V2();
			if (tipoLexema.equals(TiposToken.T_INT) && V2.getTipoToken().equals(TiposToken.T_INT)) {
				devolver=new Tipo(TiposToken.T_INT);
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(46);
			E();

			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
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
		//TODO puede haber aqui un error con el vacio de aqui y el siguiente
		Tipo devolver=new Tipo(TiposToken.T_VACIO);
		//Token 
		if (tokenIgual(TiposToken.T_POSTDECREMENTO)) {
			aux=leerToken();
			escribirFichero(51);
			devolver=new Tipo(TiposToken.T_INT);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(52);
			L();

			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				aux=leerToken();
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_MENOS)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_SUMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_MENOR)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_AND)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;

		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(53);
			devolver=new Tipo(TiposToken.T_VACIO);
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
				//error
			}
			else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
				aux=leerToken();
				CASE2();
				CASE();
			}
			else {
				//error
			}
			return devolver;
		}
		else if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
			escribirFichero(55);
			return devolver;
		}
		else {
			//error
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
			//error como tal
			return devolver;
		}
	}

	public void escribirFichero(int numeroGramatica) {
		System.out.println(numeroGramatica);
		writer.print(numeroGramatica+" ");
	}

	private boolean tokenIgual(TiposToken otro) {
		if (aux.getToken().equals(otro)){
			return true;
		}
		else return false;
	}

	//F estatica que lee de la lista de tokens segun la posicion que sea
	//TODO se puede cambiar la lista por la que no tiene tokens para ver que pasa
	public Token leerToken() {
		if (posicion<listaTokensSinEol.size()) {
			if (listaTokensSinEol.get(posicion).getToken().equals(TiposToken.EOL)) {
				linea++;
			}
			System.out.println(listaTokensSinEol.get(posicion).tokenizar());
			return listaTokensSinEol.get(posicion++);

		}
		else {
			return null;
		}

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
