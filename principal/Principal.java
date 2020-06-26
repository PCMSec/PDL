package principal;

import java.util.*;
import java.io.*;

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
		/*
		/home/pablo/eclipse-workspace/PDL/docs/sencillo.txt
		 */
		
		Scanner in = new Scanner(System.in);
		System.out.print("Introduzca direccion absoluta del fichero a analizar:"); 
		String filename = in.nextLine();
		tempFile = new File(filename);
		boolean exists = tempFile.exists();
		if (exists==false) {
			in.close();
			System.out.println("El nombre de archivo introducido no existe, hubo un problema. Compruebe de nuevo el archivo, su extension y su direccion absoluta al reiniciar el programa");
			return;
		}
		
		directorioADevolver=tempFile.getParent();
		in.close();
		System.out.println("Abriendo "+filename);
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
			else if (token.matches(entero) && (!encadenado)) {
				Token aux=new Token(TiposToken.TENTERO, Integer.parseInt(token));
				if (Integer.parseInt(token)>32767) {
					Error.writer.write("LEXICO: Entero \"" + token + "\" supera el rango asignado por el lenguaje");
					System.out.println("ERROR EN LA EJECUCION; COMPROBAR "+directorioADevolver+File.separator+"ResultadoErrores.txt");
					Error.writer.close();
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
				//si es reservada, fin
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
					
					Token aux=new Token(TiposToken.TID, token);
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
				Token aux=new Token(TiposToken.TPUNTOCOMA);
				listaTokens.add(aux);
			}
			else if (token.equals(",")) {
				Token aux=new Token(TiposToken.TCOMA);
				listaTokens.add(aux);
			}
			else if (token.equals(":")) {
				Token aux=new Token(TiposToken.TDOSPUNTOS);
				listaTokens.add(aux);
			}
			else if (token.equals("(")) {
				Token aux=new Token(TiposToken.TPARENTESISABRE);
				listaTokens.add(aux);
			}
			else if (token.equals(")")) {
				Token aux=new Token(TiposToken.TPARENTESISCIERRA);
				listaTokens.add(aux);
			}
			else if (token.equals("{")) {
				Token aux=new Token(TiposToken.TLLAVEABRE);
				listaTokens.add(aux);
			}
			else if (token.equals("}")) {
				Token aux=new Token(TiposToken.TLLAVECIERRA);
				listaTokens.add(aux);
			}
			else if (token.equals("--")) {
				Token aux=new Token(TiposToken.TPOSTDECREMENTO);
				listaTokens.add(aux);
			}
			else if (token.equals("=")) {
				Token aux=new Token(TiposToken.TIGUAL);
				listaTokens.add(aux);
			}
			else if (token.equals("&&")) {
				Token aux=new Token(TiposToken.TAND);
				listaTokens.add(aux);
			}
			else if (token.equals("+")) {
				Token aux=new Token(TiposToken.TSUMA);
				listaTokens.add(aux);
			}
			/*else if (token.equals("-")) {
				Token aux=new Token(TiposToken.T_MENOS);
				listaTokens.add(aux);
			}*/
			else if (token.equals("_EOL_")) {
				Token aux=new Token(TiposToken.EOL);
				listaTokens.add(aux);
			}
			else if (token.equals("<")) {
				Token aux=new Token(TiposToken.TMENOR);
				listaTokens.add(aux);
			}
			else if (token.equals("\"")) {
				if (encadenado) {//Hay que dejar de concatenar
					concatenado+=" "+token;
					concatenado=concatenado.replaceFirst(" ", "");
					concatenado=concatenado.replace(" \"", "\"");
					if (concatenado.length()>64) {
						Error.writer.write("LEXICO: La cadena " + concatenado + " supera los 64 caracteres maximos");
						Error.writer.close();
						System.out.println("ERROR EN LA EJECUCION; COMPROBAR "+directorioADevolver+File.separator+"ResultadoErrores.txt");
						return;
					}
					Token aux=new Token(TiposToken.TCADENA,concatenado);
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
				Error.writer.write("LEXICO: Token \"" + token + "\" no reconocido por la gramatica");
				Error.writer.close();
				System.out.println("ERROR EN LA EJECUCION; COMPROBAR "+directorioADevolver+File.separator+"ResultadoErrores.txt");
				return;
			}
		}

		//anyade al final de la lista el token de fin de fichero
		Token EOF=new Token(TiposToken.EOF);
		listaTokens.add(EOF);
		
		////////////////
		
		ArrayList<Token> listaTokensAux=new ArrayList<Token>();
		for (Token token: listaTokens) {
			if (!token.getToken().equals(TiposToken.EOL)) {
				listaTokensAux.add(token);
			}
		}
		
		///////////////
		
		try {
			PrintWriter writer = new PrintWriter(directorioADevolver+File.separator+"ResultadoTokens.txt", "UTF-8");
			for (Token token:listaTokensAux) {
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
			PrintWriter writer2 = new PrintWriter(Principal.directorioADevolver+File.separator+"ResultadoGramatica.txt", "UTF-8");
			writer2.println("Terminales = { \n" + 
					"\n" + 
					"eof lambda var id if abreParentesis cierraParentesis abreCorchete cierraCorchete switch case coma and : ; igual int string boolean return print input postDecre function menor mas cte_entero cte_cadena true false break\n" + 
					"\n" + 
					"}\n" + 
					"NoTerminales = { P B B2 T C S S2 X F A K H L Q E E2 Y Y2 D D2 V V2 CASE CASE2 }\n" + 
					"Axioma = P\n" + 
					"Producciones = {\n" + 
					"	\n" + 
					"	P -> B P \n" + 
					"	P -> F P \n" + 
					"	P -> eof \n" + 
					"\n" + 
					"	B -> var T id B2 ; \n" + 
					"	B -> if abreParentesis  E  cierraParentesis S \n" + 
					"	B -> S \n" + 
					"	B -> switch abreParentesis  E  cierraParentesis abreCorchete CASE cierraCorchete \n" + 
					"\n" + 
					"	B2 -> igual E \n" + 
					"	B2 -> lambda \n" + 
					"\n" + 
					"	T -> int \n" + 
					"	T -> string \n" + 
					"	T -> boolean \n" + 
					"\n" + 
					"	C -> B C \n" + 
					"	C -> lambda \n" + 
					"	\n" + 
					"	S -> id S2 ; \n" + 
					"	S -> return X ; \n" + 
					"	S -> print abreParentesis  E  cierraParentesis ; \n" + 
					"	S -> input abreParentesis  id  cierraParentesis ; \n" + 
					"	S -> break ; \n" + 
					"	\n" + 
					"	S2 -> postDecre \n" + 
					"	S2 -> igual E \n" + 
					"	S2 -> abreParentesis L cierraParentesis \n" + 
					"	\n" + 
					"	X -> E \n" + 
					"	X -> lambda \n" + 
					"	\n" + 
					"	F -> function H id abreParentesis  A  cierraParentesis abreCorchete C cierraCorchete \n" + 
					"	\n" + 
					"	A -> T id K \n" + 
					"	A -> lambda  \n" + 
					"	\n" + 
					"	K -> coma T id K \n" + 
					"	K -> lambda  \n" + 
					"	\n" + 
					"	H -> T \n" + 
					"	H -> lambda \n" + 
					"	\n" + 
					"	L -> E Q \n" + 
					"	L -> lambda \n" + 
					"	\n" + 
					"	Q -> coma E Q \n" + 
					"	Q -> lambda \n" + 
					"	\n" + 
					"	E -> Y E2 \n" + 
					"	\n" + 
					"	E2 -> and Y E2 \n" + 
					"	E2 -> lambda \n" + 
					"	\n" + 
					"	Y -> D Y2 \n" + 
					"	\n" + 
					"	Y2 -> menor D \n" + 
					"	Y2 -> lambda \n" + 
					"	\n" + 
					"	D -> V D2 \n" + 
					"	\n" + 
					"	D2 -> mas V D2 \n" + 
					"	D2 -> lambda \n" + 
					"	\n" + 
					"	V -> id V2 \n" + 
					"	V -> abreParentesis  E  cierraParentesis \n" + 
					"	V -> cte_entero \n" + 
					"	V -> cte_cadena \n" + 
					"	V -> true \n" + 
					"	V -> false \n" + 
					"	\n" + 
					"	V2 -> postDecre \n" + 
					"	V2 -> abreParentesis  L  cierraParentesis \n" + 
					"	V2 -> lambda \n" + 
					"	\n" + 
					"	CASE -> case E : C CASE \n" + 
					"	CASE -> lambda \n" + 
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
		
		File tempError=new File(directorioADevolver+File.separator+"ResultadoErrores.txt");
		if (tempError.length()==0) {
			System.out.println("EJECUCION COMPLETADA SIN PROBLEMAS");
		}
		else {
			System.out.println("ERROR EN LA EJECUCION; COMPROBAR "+directorioADevolver+File.separator+"ResultadoErrores.txt");
		}
		
		
		
		///home/pablo/eclipse-workspace/PDL/src/CASOS_OK/caso1/fuente.txt
		/*ArrayList<String> nombresTotales=new ArrayList<String>();
		ArrayList<Integer> desplazamientosTotales=new ArrayList<Integer>();
		
		
		//tengo la global
		TablaSimbolos finn=TablaSimbolos.getListaTablas().get(0);
		int contador=0;
		int tabla=1;
		for (int i=0;i<finn.getLexemas().size();i++) {
			nombresTotales.add(finn.getLexemas().get(i));
			if (finn.getDesplazamientos().get(i).equals(0)) {
				int contadorlocal=0;
				for (int j=0;j<TablaSimbolos.getListaTablas().get(tabla).getDesplazamientos().size();j++) {
					nombresTotales.add(TablaSimbolos.getListaTablas().get(tabla).getLexemas().get(j));
				}
				tabla++;
			}
			
			desplazamientosTotales.add(contador);
			contador+=
		}
		System.out.println(nombresTotales);*/
	}

}
