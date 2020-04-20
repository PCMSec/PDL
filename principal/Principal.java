package principal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.*;
import java.io.*;
import java.util.ArrayList; 
import lexico.Lex;
import token.*;
import tabla.*;
import sintactico.*;
import errores.*;
import errores.Error;
public class Principal {

	public static File tempFile;
	public static String directorioADevolver;
	
	public static void main(String[] args) {
		///home/pablo/eclipse-workspace/PDL/docs/sencillo.txt
		Scanner in = new Scanner(System.in);
		System.out.print("Introduzca dirección absoluta del fichero a analizar:"); 
		String filename = in.nextLine();
		tempFile = new File(filename);
		boolean exists = tempFile.exists();
		if (exists==false) {
			in.close();
			System.out.println("El nombre de archivo introducido no existe. Compruebe de nuevo el archivo, su extensión y su dirección absoluta al reiniciar el programa");
			return;
		}
		directorioADevolver=tempFile.getParent();
		in.close();
		System.out.println("Abriendo"+filename);
		String texto=lexico.Lex.leerArchivo(filename);
		//Se deja preparado el texto con el que se va a trabajar
		Error error=new Error();
		texto=texto.replace(";", " ; ");
		texto=texto.replace(",", " , ");
		texto=texto.replace(":", " : ");
		texto=texto.replace("(", " ( ");
		texto=texto.replace(")", " ) ");
		texto=texto.replace("{", " { ");
		texto=texto.replace("}", " } ");
		texto=texto.replace("--", " -- ");
		texto=texto.replace("+", " + ");
		texto=texto.replace("\"", " \" ");
		texto=texto.replace("=", " = ");
		texto=texto.replace("!", " ! ");
		texto=texto.replace("<", " < ");
		texto=texto.replace(">", " > ");
		texto=texto.replace("!  =", " != ");
		texto=texto.replace("\n", " _EOL_ ");
		texto=texto.replace("&&", " && ");
		//Arraylist con todos los tokens leidos del archivo de entrada
		ArrayList<Token> listaTokens=new ArrayList<Token>();
		//Se preparan los regex para identificar con lo que se trabaja
		String identificador=new String("^[a-zA-ZÀ-ÿ]+[0-9|_|a-zA-ZÀ-ÿ]*$");
		String entero=new String("^-?\\d+$");
		String comentario=new String("^[/][*]+$");
		String comentario2=new String("^[*]+[/]$");

		String tokens[]=texto.split("\\s+");
		//booleanos para comentarios tipo java y para concatenar strings
		boolean encadenado=false;
		boolean noContar=false;
		//String para concatenado que hay que limpiar cuando se escriba un string entero
		String concatenado="";
		for (String token:tokens) {
			//System.out.println(token);
			if ((noContar) && !((token.matches(comentario2)))) {
				continue;
			}
			else if (token.matches(entero)) {
				Token aux=new Token(TiposToken.T_ENTERO, Integer.parseInt(token));
				if (Integer.parseInt(token)>32767) {
					error.escribirError("LEXICO: Entero \"" + token + "\" supera el rango asignado por el lenguaje");
					return;
				}
				else {
					listaTokens.add(aux);
				}
			}
			else if ((encadenado) && !(token.equals("\""))) {
				concatenado+=" "+token;
				continue;
			}
			else if (token.matches(identificador)) {
				boolean reservada=false;
				//TODO mejorar esta parte para el ID si esta en tabla de simbolos
				for (String palabraReservada:Lex.palabrasReservadas) {
					if (palabraReservada.equals(token)) {
						reservada=true;
					}
				}
				if (reservada) {
					Token aux=new Token(Lex.decodificar(token));
					listaTokens.add(aux);
					continue;
				}
				else {
					Token aux=new Token(TiposToken.T_ID, token);
					listaTokens.add(aux);
				}
			}
			else if (token.matches(comentario)) {
				noContar=true;
			}
			else if (token.matches(comentario2)) {
				noContar=false;
			}
			else if (token.equals(";")) {
				Token aux=new Token(TiposToken.T_PUNTOCOMA);
				listaTokens.add(aux);
			}
			else if (token.equals(",")) {
				Token aux=new Token(TiposToken.T_COMA);
				listaTokens.add(aux);
			}
			else if (token.equals(":")) {
				Token aux=new Token(TiposToken.T_DOSPUNTOS);
				listaTokens.add(aux);
			}
			else if (token.equals("(")) {
				Token aux=new Token(TiposToken.T_PARENTESISABRE);
				listaTokens.add(aux);
			}
			else if (token.equals(")")) {
				Token aux=new Token(TiposToken.T_PARENTESISCIERRA);
				listaTokens.add(aux);
			}
			else if (token.equals("{")) {
				Token aux=new Token(TiposToken.T_LLAVEABRE);
				listaTokens.add(aux);
			}
			else if (token.equals("}")) {
				Token aux=new Token(TiposToken.T_LLAVECIERRA);
				listaTokens.add(aux);
			}
			else if (token.equals("--")) {
				Token aux=new Token(TiposToken.T_POSTDECREMENTO);
				listaTokens.add(aux);
			}
			else if (token.equals("=")) {
				Token aux=new Token(TiposToken.T_IGUAL);
				listaTokens.add(aux);
			}
			else if (token.equals("&&")) {
				Token aux=new Token(TiposToken.T_AND);
				listaTokens.add(aux);
			}
			else if (token.equals("+")) {
				Token aux=new Token(TiposToken.T_SUMA);
				listaTokens.add(aux);
			}
			else if (token.equals("-")) {
				Token aux=new Token(TiposToken.T_MENOS);
				listaTokens.add(aux);
			}
			else if (token.equals("_EOL_")) {
				Token aux=new Token(TiposToken.EOL);
				listaTokens.add(aux);
			}
			else if (token.equals("<")) {
				Token aux=new Token(TiposToken.T_MENOR);
				listaTokens.add(aux);
			}
			else if (token.equals("\"")) {
				if (encadenado) {//Hay que dejar de concatenar
					concatenado+=" "+token;
					concatenado=concatenado.replaceFirst(" ", "");
					concatenado=concatenado.replace(" \"", "\"");
					Token aux=new Token(TiposToken.T_CADENA,concatenado);
					listaTokens.add(aux);

					concatenado="";
					encadenado=false;
				}
				else {//Hay que empezar a concatenar
					concatenado+=token;
					encadenado=true;
				}
			}
			//no se reconoce el token leido
			else {
				error.escribirError("LEXICO: Token \"" + token + "\" no reconocido por la gramática");
				return;
			}
		}
		//anyade al final de la lista el token de fin de fichero
		Token EOF=new Token(TiposToken.EOF);
		listaTokens.add(EOF);
		try {
			PrintWriter writer = new PrintWriter(directorioADevolver+"tokens.txt", "UTF-8");
			for (Token token:listaTokens) {
				//System.out.println(token.tokenizar());
				writer.println(token.tokenizar());
			}
			writer.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}


		Sintactico sin = new Sintactico(listaTokens);
		//numero de linea en el que nos encontramos
		sin.P();
		try {
			PrintWriter writer2 = new PrintWriter(Principal.directorioADevolver+"/ResultadoGramatica.txt", "UTF-8");
			writer2.println("Terminales = { \n" + 
					"\n" + 
					"eof var id if abreParentesis cierraParentesis abreCorchete cierraCorchete switch case , and : ; igual int string boolean return print prompt postDecre function menor mas menos entero cadena true false break\n" + 
					"\n" + 
					"}\n" + 
					"NoTerminales = { P B B2 T C S S2 X F A K H L Q E E2 Y Y2 D D2 V V2 CASE CASE2 }\n" + 
					"Axioma = P\n" + 
					"Producciones = {\n" + 
					"	\n" + 
					"	P -> B P //1\n" + 
					"	P -> F P //2\n" + 
					"	P -> eof //3\n" + 
					"\n" + 
					"	B -> var T id B2 ; //4\n" + 
					"	B -> if abreParentesis  E  cierraParentesis S //5\n" + 
					"	B -> S //6\n" + 
					"	B -> switch abreParentesis  E  cierraParentesis abreCorchete CASE cierraCorchete //7\n" + 
					"\n" + 
					"	B2 -> igual E //8\n" + 
					"	B2 -> lambda //9\n" + 
					"\n" + 
					"	T -> int //10\n" + 
					"	T -> string //11\n" + 
					"	T -> boolean //12\n" + 
					"\n" + 
					"	C -> B C //13\n" + 
					"	C -> lambda //14\n" + 
					"	\n" + 
					"	S -> id S2 ; //15\n" + 
					"	S -> return X ; //16\n" + 
					"	S -> print abreParentesis  E  cierraParentesis ; //17\n" + 
					"	S -> prompt abreParentesis  id  cierraParentesis ; //18\n" + 
					"	\n" + 
					"	S2 -> postDecre //19\n" + 
					"	S2 -> igual E //20\n" + 
					"	S2 -> abreParentesis L cierraParentesis //21\n" + 
					"	\n" + 
					"	X -> E //22\n" + 
					"	X -> lambda //23\n" + 
					"	\n" + 
					"	F -> function H id abreParentesis  A  cierraParentesis abreCorchete C cierraCorchete //24\n" + 
					"	\n" + 
					"	A -> T id K //25\n" + 
					"	A -> lambda  //26\n" + 
					"	\n" + 
					"	K -> coma T id K //27\n" + 
					"	K -> lambda  //28\n" + 
					"	\n" + 
					"	H -> T //29\n" + 
					"	H -> lambda //30\n" + 
					"	\n" + 
					"	L -> E Q //31\n" + 
					"	L -> lambda //32\n" + 
					"	\n" + 
					"	Q -> coma E Q //33\n" + 
					"	Q -> lambda //34\n" + 
					"	\n" + 
					"	E -> Y E2 //35\n" + 
					"	\n" + 
					"	E2 -> and Y E2 //36\n" + 
					"	E2 -> lambda //37\n" + 
					"	\n" + 
					"	Y -> D Y2 //38\n" + 
					"	\n" + 
					"	Y2 -> menor D Y2 //39\n" + 
					"	Y2 -> lambda //40\n" + 
					"	\n" + 
					"	D -> V D2 //41\n" + 
					"	\n" + 
					"	D2 -> menos V D2 //42\n" + 
					"	D2 -> mas V D2 //43\n" + 
					"	D2 -> lambda //44\n" + 
					"	\n" + 
					"	V -> id V2 //45\n" + 
					"	V -> abreParentesis  E  cierraParentesis //46\n" + 
					"	V -> cte_entero //47\n" + 
					"	V -> cte_cadena //48\n" + 
					"	V -> true //49\n" + 
					"	V -> false //50\n" + 
					"	\n" + 
					"	V2 -> postDecre //51\n" + 
					"	V2 -> abreParentesis  L  cierraParentesis //52\n" + 
					"	V2 -> lambda //53\n" + 
					"	\n" + 
					"	CASE -> case E : CASE2 CASE //54\n" + 
					"	CASE -> lambda //55\n" + 
					"	\n" + 
					"	CASE2 -> S CASE2 //56\n" + 
					"	CASE2 -> break ; //57\n" + 
					"	CASE2 -> lambda //58\n" + 
					"	\n" + 
					"}");
			writer2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		TablaSimbolos.imprimirTablas();
		error.cerrarArchivo();
	}

}
