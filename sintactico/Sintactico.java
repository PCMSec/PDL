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
		global = new TablaSimbolos();
		actual = global;
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

	public void B() {
		if (tokenIgual(TiposToken.T_VAR)) {
			aux=leerToken();
			escribirFichero(4);
			T();
			//TODO guardar tipo devuelto?
			if (tokenIgual(TiposToken.T_ID)) {
				aux=leerToken();
				B2();
				if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
					aux=leerToken();
					
				}
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_IF)) {
			aux=leerToken();
			escribirFichero(5);
			
			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				E();
				if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
					aux=leerToken();
					S();
				}
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_SWITCH)) {
			aux=leerToken();
			escribirFichero(7);
			
			if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
				aux=leerToken();
				E();
				if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
					aux=leerToken();
					if (tokenIgual(TiposToken.T_LLAVEABRE)) {
						aux=leerToken();
						W();	
						if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
							aux=leerToken();
						}
					}
				}
			}
			return;
		}
		//S
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(6);
			S();
			return;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			escribirFichero(6);
			S();
			return;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			escribirFichero(6);
			S();
			return;
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(6);
			S();
			return;
		}//TODO: caso especial al leer end of line
		/*else if (tokenIgual(TiposToken.EOL)) {
			aux=leerToken();
			return;
		}*/
		else {
			//error
			return;
		}
	}
	public void B2() {
		if (tokenIgual(TiposToken.T_IGUAL)) {
			aux=leerToken();
			escribirFichero(8);
			E();	
			return;
		}
		//follow
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			//TODO repasar esto que los follows pueden estar mal
			escribirFichero(9);
			return;
		}//error
		else {
			return;
		}
	}
	public void T() {
		if (tokenIgual(TiposToken.T_INT)) {
			escribirFichero(10);
			aux=leerToken();
			return;
		}
		else if (tokenIgual(TiposToken.T_STRING)) {
			escribirFichero(11);
			aux=leerToken();
			return;
		}
		else if (tokenIgual(TiposToken.T_BOOLEAN)) {
			escribirFichero(12);
			aux=leerToken();
			return;
		}
		else {
			return;
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
	public void S() {
		if (tokenIgual(TiposToken.T_ID)) {
			aux=leerToken();
			escribirFichero(15);
			S2();
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				aux=leerToken();
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			aux=leerToken();
			escribirFichero(16);
			X();
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				aux=leerToken();
			}
			return;
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
			return;
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
			return;
		}
		else {
			return;
		}

	}
	public void S2() {
		//Token 
		if (tokenIgual(TiposToken.T_POSTDECREMENTO)) {
			aux=leerToken();
			escribirFichero(19);
			return;
		}
		else if (tokenIgual(TiposToken.T_IGUAL)) {
			aux=leerToken();
			escribirFichero(20);
			E();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(21);
			L();	
			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				aux=leerToken();
			}
			return;
		}
		else {
			return;
		}
	}
	public void X() {
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(22);
			E();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(22);
			E();
			return;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(22);
			E();
			return;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(22);
			E();
			return;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(22);
			E();
			return;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(22);
			E();
			return;
		}
		//follow
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(23);
			return;
		}
		else {
			return;
		}

	}
	public void F() {
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
	public void H() {
		//Token 
		if (tokenIgual(TiposToken.T_INT)) {
			escribirFichero(29);
			T();
			return;
		}
		else if (tokenIgual(TiposToken.T_STRING)) {
			escribirFichero(29);
			T();
			return;
		}
		else if (tokenIgual(TiposToken.T_BOOLEAN)) {
			escribirFichero(29);
			T();
			return;
		}
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(30);
			return;
		}
		else {
			return;
		}
	}

	public void L() {
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
	public void E() {
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(35);
			Y();
			E2();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(35);
			Y();
			E2();
			return;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(35);
			Y();
			E2();
			return;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(35);
			Y();
			E2();
			return;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(35);
			Y();
			E2();
			return;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(35);
			Y();
			E2();
			return;
		}
		else {
			return;
		}
	}
	public void E2() {
		//Token 
		if (tokenIgual(TiposToken.T_AND)) {
			aux=leerToken();
			escribirFichero(36);
			Y();
			E2();
			return;
		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(37);
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(37);
			return;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(37);
			return;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(37);
			return;
		}
		else {
			return;
		}
	}
	public void Y() {
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(38);
			D();
			Y2();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(38);
			D();
			Y2();
			return;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(38);
			D();
			Y2();
			return;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(38);
			D();
			Y2();
			return;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(38);
			D();
			Y2();
			return;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(38);
			D();
			Y2();
			return;
		}
		else {
			return;
		}
	}
	public void Y2() {
		//Token 
		if (tokenIgual(TiposToken.T_MENOR)) {
			aux=leerToken();
			escribirFichero(39);
			D();
			Y2();
			return;
		}
		else if (tokenIgual(TiposToken.T_AND)) {
			escribirFichero(40);
			return;
		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(40);
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(40);
			return;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(40);
			return;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(40);
			return;
		}
		else {
			return;
		}
	}
	public void D() {
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(41);
			V();
			D2();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			escribirFichero(41);
			V();
			D2();
			return;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			escribirFichero(41);
			V();
			D2();
			return;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			escribirFichero(41);
			V();
			D2();
			return;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			escribirFichero(41);
			V();
			D2();
			return;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			escribirFichero(41);
			V();
			D2();
			return;
		}
		else {
			return;
		}

	}
	public void D2() {
		//Token 
		if (tokenIgual(TiposToken.T_MENOS)) {
			aux=leerToken();
			escribirFichero(42);
			V();
			D2();
			return;
		}
		else if (tokenIgual(TiposToken.T_SUMA)) {
			aux=leerToken();
			escribirFichero(43);
			V();
			D2();
			return;
		}
		else if (tokenIgual(TiposToken.T_MENOR)) {
			escribirFichero(44);
			return;
		}
		else if (tokenIgual(TiposToken.T_AND)) {
			escribirFichero(44);
			return;
		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(44);
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(44);
			return;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(44);
			return;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(44);
			return;
		}
		else {
			return;
		}
	}
	public void V() {
		//Token 
		if (tokenIgual(TiposToken.T_ID)) {
			aux=leerToken();
			escribirFichero(45);
			V2();
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(46);
			E();
			
			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				aux=leerToken();

			}
			return;
		}
		else if (tokenIgual(TiposToken.T_ENTERO)) {
			aux=leerToken();
			escribirFichero(47);
			return;
		}
		else if (tokenIgual(TiposToken.T_CADENA)) {
			aux=leerToken();
			escribirFichero(48);
			return;
		}
		else if (tokenIgual(TiposToken.T_TRUE)) {
			aux=leerToken();
			escribirFichero(49);
			return;
		}
		else if (tokenIgual(TiposToken.T_FALSE)) {
			aux=leerToken();
			escribirFichero(50);
			return;
		}
		else {
			return;
		}
	}
	public void V2() {
		//Token 
		if (tokenIgual(TiposToken.T_POSTDECREMENTO)) {
			aux=leerToken();
			escribirFichero(51);
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISABRE)) {
			aux=leerToken();
			escribirFichero(52);
			L();
			
			if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
				aux=leerToken();
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_MENOS)) {
			escribirFichero(53);
			return;
		}
		else if (tokenIgual(TiposToken.T_SUMA)) {
			escribirFichero(53);
			return;
		}
		else if (tokenIgual(TiposToken.T_MENOR)) {
			escribirFichero(53);
			return;
		}
		else if (tokenIgual(TiposToken.T_AND)) {
			escribirFichero(53);
			return;

		}
		else if (tokenIgual(TiposToken.T_COMA)) {
			escribirFichero(53);
			return;
		}
		else if (tokenIgual(TiposToken.T_PARENTESISCIERRA)) {
			escribirFichero(53);
			return;
		}
		else if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
			escribirFichero(53);
			return;
		}
		else if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
			escribirFichero(53);
			return;
		}
		else {
			return;
		}
	}
	public void W() {
		//Token 
		if (tokenIgual(TiposToken.T_CASE)) {
			aux=leerToken();
			escribirFichero(54);

			E();
			
			if (tokenIgual(TiposToken.T_DOSPUNTOS)) {
				aux=leerToken();
				W2();
				W();
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
			escribirFichero(55);
			return;
		}
		else {
			return;
		}
	}
	public void W2() {
		//Token 
		if (tokenIgual(TiposToken.T_BREAK)) {
			aux=leerToken();
			escribirFichero(57);
			if (tokenIgual(TiposToken.T_PUNTOCOMA)) {
				aux=leerToken();
			}
			return;
		}
		else if (tokenIgual(TiposToken.T_ID)) {
			escribirFichero(56);
			S();
			W2();
			return;
		}
		else if (tokenIgual(TiposToken.T_RETURN)) {
			escribirFichero(56);
			S();
			W2();
			return;
		}
		else if (tokenIgual(TiposToken.T_PRINT)) {
			escribirFichero(56);
			S();
			W2();
		}
		else if (tokenIgual(TiposToken.T_INPUT)) {
			escribirFichero(56);
			S();
			W2();
			return;
		}
		else if (tokenIgual(TiposToken.T_CASE)) {
			escribirFichero(58);
			return;
		}
		else if (tokenIgual(TiposToken.T_LLAVECIERRA)) {
			escribirFichero(58);
			return;
		}
		else {
			return;
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
